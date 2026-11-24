#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Repeatable, non-release IA machinery runs over hash-pinned immutable specimens.

Generate once per run; --check regenerates in memory and compares without writes.
Candidates are full proposed Exchange models, compressed for evidence storage.
"""
import argparse
import copy
import gzip
import hashlib
import json
from pathlib import Path
import re
import sys

import change_review
import diagnose_roundtrip as diagnostic
import generate_views as g
import governed_metadata as metadata
import information_naming as naming
from workspace_paths import PROCESSING, ROOT

RUNS = PROCESSING / 'machinery-validation/information-architecture'
INPUTS = RUNS / 'specimen.json'
MARKER = 'Machinery validation / Candidate standing'


def sha(data):
    return hashlib.sha256(data).hexdigest()


def encoded(value):
    return (json.dumps(value, ensure_ascii=False, sort_keys=True, indent=2) + '\n').encode()


def compressed(data):
    return gzip.compress(data, mtime=0)


def load_inputs(path=INPUTS):
    fixtures = metadata.read(path)
    models = {}
    for key, row in fixtures.items():
        source = ROOT / row['path']
        g.require(sha(source.read_bytes()) == row['sha256'], 'Immutable input hash changed: ' + key)
        models[key] = diagnostic.Exchange(source)
        g.require(not models[key].issues, 'Invalid immutable specimen: ' + repr(models[key].issues))
        if 'xml_sha256' in row:
            g.require(models[key].xml_sha256 == row['xml_sha256'], 'Immutable XML hash changed: ' + key)
    return fixtures, models


def set_property(root, node, key, value):
    definitions = root.find('a:propertyDefinitions', g.N)
    matches = [p.get('identifier') for p in definitions
               if p.findtext('a:name', namespaces=g.N) == key]
    g.require(len(matches) <= 1, 'Ambiguous output Property definition: ' + key)
    if not matches:
        identifier = 'machinery-' + re.sub('[^a-z0-9]+', '-', key.lower()) + '-' + sha(key.encode())[:8]
        g.require(not root.xpath('//*[@identifier=$rid]', rid=identifier), 'Output Property ID collision')
        g.child(g.child(definitions, 'propertyDefinition', identifier=identifier, type='string'), 'name', key)
    else:
        identifier, = matches
    props = node.find('a:properties', g.N)
    if props is None and value is not None:
        props = g.child(node, 'properties')
        if node is root:
            root.remove(props)
            position = next((i for i, child in enumerate(root)
                             if child.tag not in (g.tag('name'), g.tag('documentation'))), len(root))
            root.insert(position, props)
    if props is not None:
        for prop in list(props):
            if prop.get('propertyDefinitionRef') == identifier:
                props.remove(prop)
        if value is not None:
            g.child(g.child(props, 'property', propertyDefinitionRef=identifier), 'value', value)


def candidate(model, proposal, run):
    root = copy.deepcopy(model.root)
    elements = {n.get('identifier'): n for n in root.findall('a:elements/a:element', g.N)}
    for row in proposal['records']:
        node = elements[row['representation_id']]
        for key, value in row['proposed'].items():
            set_property(root, node, key, value)
        if 'displayName' in row['proposed']:
            node.find('a:name', g.N).text = row['proposed']['displayName']
    set_property(root, root, MARKER, run + ': PROPOSED; NOT ACCEPTED; NOT A RAD RELEASE')
    g.load_schema(g.BASE / 'schemas/archimate3_Diagram.xsd').assertValid(root)
    g.validate_identifiers(root)
    return g.serialize(root)


def verify_candidate(model, content, proposal, run):
    """Independent allowlist reversal: every other subtree must match the specimen."""
    root = g.ET.fromstring(content)
    g.load_schema(g.BASE / 'schemas/archimate3_Diagram.xsd').assertValid(root)
    g.validate_identifiers(root)
    definitions = {p.get('identifier'): p.findtext('a:name', namespaces=g.N)
                   for p in root.findall('a:propertyDefinitions/a:propertyDefinition', g.N)}
    nodes = {p.get('identifier'): p for p in root.findall('a:elements/a:element', g.N)}
    for row in proposal['records']:
        rid = row['representation_id']
        node = nodes[rid]
        for key, expected in row['proposed'].items():
            props = [p for p in node.findall('a:properties/a:property', g.N)
                     if definitions[p.get('propertyDefinitionRef')] == key]
            values = [[v.text for v in p.findall('a:value', g.N)] for p in props]
            g.require(values == ([] if expected is None else [[expected]]), 'Candidate metadata drift: ' + key)
        if 'displayName' in row['proposed']:
            g.require(node.findtext('a:name', namespaces=g.N) == row['proposed']['displayName'], 'Candidate Name drift')
        # Replace only explicitly allowlisted fields with the submitted originals.
        original = model.elements[rid]
        node.find('a:name', g.N).text = original.findtext('a:name', namespaces=g.N)
        props = node.find('a:properties', g.N)
        if props is not None:
            for prop in list(props):
                if definitions[prop.get('propertyDefinitionRef')] in row['proposed']:
                    props.remove(prop)
            for prop in original.findall('a:properties/a:property', g.N):
                if model.keys[prop.get('propertyDefinitionRef')] in row['proposed']:
                    props.append(copy.deepcopy(prop))
            # Original property ordering is protected too, after restoring fields.
            prior = original.find('a:properties', g.N)
            expected = sorted(review_property(p, definitions) for p in props)
            before = sorted(review_property(p, model.keys) for p in prior) if prior is not None else []
            g.require(expected == before, 'Non-allowlisted Property changed: ' + rid)
            node.remove(props)
            if prior is not None:
                node.append(copy.deepcopy(prior))
    marker_id, = [rid for rid, key in definitions.items() if key == MARKER]
    marker = root.find(f"a:properties/a:property[@propertyDefinitionRef='{marker_id}']/a:value", g.N)
    g.require(marker is not None and marker.text == run + ': PROPOSED; NOT ACCEPTED; NOT A RAD RELEASE',
              'Candidate standing missing')
    marker.getparent().getparent().remove(marker.getparent())
    old_props = model.root.find('a:properties', g.N)
    if old_props is None:
        root.remove(root.find('a:properties', g.N))
    old_definitions = set(model.definitions)
    for definition in list(root.find('a:propertyDefinitions', g.N)):
        if definition.get('identifier') not in old_definitions:
            root.find('a:propertyDefinitions', g.N).remove(definition)
    g.require(g.signature(root) == g.signature(model.root), 'Candidate changed protected submitted content')
    return dict(xsd='PASS', exact_allowlist_preservation='PASS',
                canonical_identity_claims='UNCHANGED', representation_ids='UNCHANGED',
                authored_types_specializations_composition_views_navigation_documentation='UNCHANGED')


def review_property(prop, definitions):
    return json.dumps(change_review.signature(prop, type('Resolver', (), {'keys': definitions})()), sort_keys=True)


def build(run, inputs=INPUTS):
    g.require(re.fullmatch(r'run-[0-9]{3,}', run) and int(run[4:]) > 0, 'Expected run-NNN')
    release_path = ROOT / 'architecture-release.json'
    release_bytes = release_path.read_bytes()
    g.require(json.loads(release_bytes) == dict(version='1.0.10', previous_version='1.0.9'),
              'Machinery cycle requires unchanged RAD-1.0.10 release authority')
    fixtures, models = load_inputs(inputs)
    before, incoming, control = (models[key] for key in ('baseline', 'incoming', 'control'))
    review = change_review.review(before, incoming, control)
    proposal = naming.propose(incoming)
    baseline_proposal = naming.propose(before)
    content = candidate(incoming, proposal, run)
    preservation = verify_candidate(incoming, content, proposal, run)
    outputs = {
        'candidate.xml.gz': compressed(content),
        'proposed-metadata.json': encoded(proposal),
        'baseline-naming-preview.json': encoded(baseline_proposal),
        'change-review.json.gz': compressed(encoded(review)),
        'representation-rebindings.json': encoded(review['representation_rebindings']),
        'verification.json': encoded(dict(status='PASS', checks=preservation,
            population=review['population'], naming=proposal['summary'],
            candidate_xml_sha256=sha(content), candidate_standing='PROPOSED_NOT_ACCEPTED',
            archi_roundtrip='NOT_PERFORMED', architect_acceptance='NOT_RECORDED',
            release_created=False, canonical_ids_allocated=0)),
    }
    sources = [*Path(__file__).parent.glob('*.py'),
               ROOT / 'scripts/architecture_release.py',
               *(ROOT / 'docs/markdown/governance' / name for name in
                 ('canonical-identity-and-naming.md', 'architecture-release-policy.md',
                  'architecture-modelling-strategy.md', 'archimate-ingestion-publication-semantics.md'))]
    manifest = dict(run=run, classification='machinery-validation-evidence',
        rad_release_created=None, governed_baseline='RAD-1.0.10', intended_accepted_release='RAD-1.1.0',
        acceptance='NOT_RECORDED', inputs=fixtures,
        release_authority=dict(path='architecture-release.json', sha256=sha(release_bytes), value=json.loads(release_bytes)),
        source_sha256={str(path.relative_to(ROOT)): sha(path.read_bytes()) for path in sorted(sources)},
        files_sha256={key: sha(value) for key, value in outputs.items()})
    outputs['manifest.json'] = encoded(manifest)
    # Recheck every immutable byte source after processing, including release authority.
    for key, row in fixtures.items():
        g.require(sha((ROOT / row['path']).read_bytes()) == row['sha256'], 'Input changed during run: ' + key)
    g.require(release_path.read_bytes() == release_bytes, 'Release changed during run')
    return outputs


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--run', required=True)
    parser.add_argument('--check', action='store_true')
    args = parser.parse_args(argv)
    try:
        outputs = build(args.run)
        location = RUNS / args.run
        if args.check:
            for name, data in outputs.items():
                g.require((location / name).read_bytes() == data, 'Stale/changed run evidence: ' + name)
        else:
            g.require(not any((location / name).exists() for name in outputs),
                      'Run already exists/incomplete; preserve it and use a subsequent run number')
            location.mkdir(parents=True, exist_ok=True)
            for name, data in outputs.items():
                with (location / name).open('xb') as stream:
                    stream.write(data)
        print(json.dumps(dict(status='PASS', mode='CHECK' if args.check else 'GENERATE', run=args.run,
                              evidence=str(location.relative_to(ROOT)),
                              naming=json.loads(outputs['proposed-metadata.json'])['summary']), indent=2))
        return 0
    except (OSError, ValueError, KeyError, g.ET.LxmlError) as exc:
        print('UNRESOLVED: ' + str(exc), file=sys.stderr)
        return 1


if __name__ == '__main__':
    sys.exit(main())
