#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Resource-owned identity/names: explicit bootstrap, strict validation, publication.

No reconciliation. The archive is complete source history, never an ID registry.
Ordinary publication has no call to the allocator.
"""
import argparse
import collections
import copy
import hashlib
import json
import os
from pathlib import Path
import re
import sys
import uuid

import generate_views as g

from workspace_paths import PUBLISH, PUBLISHING, SYNCHRONISATION_EVIDENCE, ESTABLISHMENT_EVIDENCE, historical_path
from workspace_paths import require_rad_release, validate_publication_namespaces

ROOT, BASE = g.BASE.parents[2], g.BASE
sys.path.insert(0, str(ROOT / 'scripts'))
RAD = ROOT / 'docs/markdown/governance/baseline'
SOURCE, MOTIVATION = RAD / 'architecture.json', RAD / 'motivation.json'
DEST = SYNCHRONISATION_EVIDENCE
LEGACY_MODEL = PUBLISH / '1.0.6/Harmonia-Architecture-Baseline.xml'
ROLES = ('canonicalId', 'displayName', 'friendlyName', 'canonicalName', 'archimateType', 'harmoniaType')
FIELDS = ROLES[:4]
SOURCE_FIELDS = (*FIELDS, 'nameComponent', 'nameComponentAuthority')
UUID32 = re.compile(r'[0-9a-f]{32}\Z')
FORMAL = {'feature': 'FEAT', 'function': 'FN', 'service': 'SV', 'process': 'PR',
          'product': 'PRODUCT'}


def encoded(value):
    return (json.dumps(value, ensure_ascii=False, indent=2) + '\n').encode()


def read(path):
    def unique(pairs):
        result = {}
        for key, value in pairs:
            g.require(key not in result, 'Multiple source claims for ' + key)
            result[key] = value
        return result
    return json.loads(historical_path(path).read_text(), object_pairs_hook=unique)


def active():
    established = 'governedMetadata' in read(SOURCE)
    pinned = 'canonical_identity_establishment' in read(RAD / 'correspondence.json')
    g.require(not pinned or established, 'Deleted identity establishment metadata; legacy fallback forbidden')
    g.require(established or not any(PUBLISHING.glob('*/identity-establishment-*')),
              'Established/incomplete source history exists; legacy fallback or reallocation forbidden')
    return established


def semantic_capture(data):
    """Remove only this contract's fields to check the unchanged adopted capture."""
    result = copy.deepcopy(data)
    result.pop('governedMetadata', None)
    result.pop('historical_reference_elements', None)
    for record in result['elements']:
        for role in SOURCE_FIELDS:
            record.pop(role, None)
    return result


def historical_bytes(path):
    path = historical_path(path)
    if path in (SOURCE, MOTIVATION):
        return encoded(semantic_capture(read(path)))
    if path == ROOT / 'architecture-release.json' and active():
        return (archive() / 'prior-release/architecture-release.json').read_bytes()
    return path.read_bytes()


def archive():
    relative = read(SOURCE)['governedMetadata']['sourceHistory']
    path = historical_path(ROOT / relative).resolve()
    g.require(path.is_relative_to(ESTABLISHMENT_EVIDENCE.resolve()), 'Source history outside publication history')
    return path


def release():
    if active():
        return read(archive() / 'prior-release/architecture-release.json')
    from architecture_release import load_release
    return load_release()


def model_path():
    validate_publication_namespaces()
    if active():
        from architecture_release import load_release
        version = require_rad_release(load_release()['version'])
        return PUBLISH / version / ('Harmonia-Architecture-' + version + '.xml')
    return LEGACY_MODEL


def validation_path():
    validate_publication_namespaces()
    if active():
        from architecture_release import load_release
        version = require_rad_release(load_release()['version'])
        if version != read(SOURCE)['governedMetadata']['establishedRelease']:
            return PUBLISHING / version / ('publication-validation-' + version + '.json')
        return ESTABLISHMENT_EVIDENCE / 'canonical-identity-validation.json'
    return DEST / 'validation.json'


def component(label):
    """PascalCase lexical spelling; punctuation separates words, '&' spells And.

    Case boundaries preserve existing compound words; initialisms become ordinary
    PascalCase words. This never adds architectural grouping or removes words.
    """
    label = label.replace('&', ' And ')
    label = re.sub(r'([a-z0-9])([A-Z])', r'\1 \2', label)
    label = re.sub(r'([A-Z])([A-Z][a-z])', r'\1 \2', label)
    words = re.findall(r'[^\W_]+', label, re.UNICODE)
    value = ''.join(word[:1].upper() + word[1:].lower() for word in words)
    g.require(value and value[0].isalpha() and not any(c.isspace() for c in value),
              'Cannot establish PascalCase component: ' + repr(label))
    return value


def historical_records(b):
    """Capture accepted historical resources without adopting a RAD crosswalk.

    Only native Composition and literal semantic Properties establish context;
    organisations and Views are never read for naming.
    """
    root = g.parse(LEGACY_MODEL)
    definitions = {p.get('identifier'): p.findtext('a:name', namespaces=g.N)
                   for p in root.findall('a:propertyDefinitions/a:propertyDefinition', g.N)}
    records = []
    ids = {e.get('identifier') for e in root.findall('a:elements/a:element', g.N)
           if e.get(g.TYPE) == 'Capability' and e.get('identifier') not in b.byid}
    parents = collections.defaultdict(list)
    for edge in root.findall('a:relationships/a:relationship', g.N):
        if edge.get(g.TYPE) == 'Composition' and edge.get('target') in ids:
            g.require(edge.get('source') in ids, 'Historical context crosses an unestablished binding')
            parents[edge.get('target')].append(edge.get('source'))
    categories = {'Business Capability': 'business', 'Business Enabling Capability': 'business_enabling'}
    for element in root.findall('a:elements/a:element', g.N):
        identifier = element.get('identifier')
        if identifier not in ids:
            continue
        props = collections.defaultdict(list)
        for prop in element.findall('a:properties/a:property', g.N):
            props[definitions[prop.get('propertyDefinitionRef')]].extend(v.text for v in prop.findall('a:value', g.N))
        g.require(len(parents[identifier]) <= 1, 'Multiple historical responsibility parents')
        g.require(len(props['Capability Tier']) == len(props['Harmonia Architecture Layer']) == 1,
                  'Missing/ambiguous historical tier/category: ' + identifier)
        tier = props['Capability Tier'][0]
        g.require((tier == 'CT1') == (not parents[identifier]), 'Incomplete historical tier context: ' + identifier)
        records.append(dict(key='historical-reference:' + identifier, representationId=identifier,
                            name=element.findtext('a:name', namespaces=g.N), type='capability', tier=tier,
                            category=categories[props['Harmonia Architecture Layer'][0]],
                            parent_key=('historical-reference:' + parents[identifier][0]) if parents[identifier] else None,
                            record_state='historical_reference',
                            source='Accepted Release 1.0.6 historical resource; no RAD identity crosswalk adopted'))
    return records


def formal_qualifier(record):
    """The governed naming vocabulary, independently of identity or ancestry."""
    if record['type'] in FORMAL:
        return FORMAL[record['type']]
    tier = record.get('tier')
    g.require(tier in ('CT1', 'CT2', 'CT3'), 'Missing formal tier: ' + record['key'])
    return tier + {'business': '', 'business_enabling': '-BEC', 'enterprise': '-EC'}[record['category']]


def qualified_names(path):
    """Apply the same lexical and qualification rules to each governed component."""
    labels = []
    for resource in path:
        if 'nameComponent' in resource:
            value = resource['nameComponent']
            g.require(isinstance(value, str) and re.fullmatch(r'[A-Z][A-Za-z0-9]*', value)
                      and resource.get('nameComponentAuthority'), 'Unqualified/unproven name component decision')
        else:
            value = component(resource['name'])
        labels.append(value)
    return dict(friendlyName='.'.join(labels),
                canonicalName='.'.join(formal_qualifier(e) + '-' + label for e, label in zip(path, labels)))


def descriptors(b, source, motivation):
    """Index actual source resources using the existing publication bindings."""
    result = {}
    bykey = {e['key']: e for e in source['elements']}
    g.require(len(bykey) == len(source['elements']), 'Multiple source resource keys')
    historical = source.get('historical_reference_elements', [])
    expected_historical = historical_records(b)
    g.require([{k: v for k, v in e.items() if k not in SOURCE_FIELDS} for e in historical] == expected_historical,
              'Historical resource source/type/context drift')
    historical_keys = {e['key']: e for e in historical}

    def chain(record, population):
        path, seen = [], set()
        current = record
        while current:
            g.require(current['key'] not in seen, 'Cyclic governed naming context')
            seen.add(current['key'])
            path.insert(0, current)
            key = current.get('owner_key') if current['type'] not in ('capability', 'enterprise_capability') else current.get('parent_key')
            g.require(key is None or key in population, 'Unestablished naming context: ' + str(key))
            current = population.get(key)
        return path

    def add(identifier, record, path, native_type, motivation_record=False):
        g.require(identifier not in result, 'Multiple source owners for representation: ' + identifier)
        label = record['name']
        # Native definition codes are retained separately as identifier provenance.
        native_id = record.get('native_id')
        if motivation_record and native_id and label.startswith(native_id):
            label = label[len(native_id):].lstrip(' —–-:')
        def spelling(resource, text):
            if 'nameComponent' in resource:
                value = resource['nameComponent']
                g.require(isinstance(value, str) and re.fullmatch(r'[A-Z][A-Za-z0-9]*', value)
                          and resource.get('nameComponentAuthority'), 'Unqualified/unproven name component decision')
                return value
            return component(text)
        names = qualified_names(path) if not motivation_record else dict(
            friendlyName=spelling(record, label), canonicalName='MOT-' + spelling(record, label))
        kind = record['type']
        display = ('[' + (record['tier'] if kind in ('capability', 'enterprise_capability') else 'Feature') + '] ' + record['name']) if kind in ('capability', 'enterprise_capability', 'feature') else record['name']
        result[identifier] = dict(record=record, expected=dict(displayName=display, **names), archimateType=native_type,
                                  harmoniaType='Feature' if kind == 'feature' else None)

    import recover_v02 as r
    for record in source['elements']:
        add(b.ids[record['key']], record, chain(record, bykey), r.TYPES[record['type']])
    for record in historical:
        add(record['representationId'], record, chain(record, historical_keys), 'Capability')
    principles = {e.findtext('a:name', namespaces=g.N).split()[0]: e.get('identifier')
                  for e in b.original.findall('a:elements/a:element', g.N) if e.get(g.TYPE) == 'Principle'}
    for record in motivation['elements']:
        identifier = principles.get(record.get('native_id')) or b.ident('motivation', record['key'])
        add(identifier, record, [], record['type'], True)
    return result


def validate_records(index, previous=None, allow_absent=False):
    owners, missing = {}, []
    for identifier, item in index.items():
        record = item['record']
        if 'canonicalId' not in record:
            missing.append(identifier)
            g.require(allow_absent, 'Missing/deleted Canonical ID: ' + identifier)
        else:
            value = record['canonicalId']
            g.require(isinstance(value, str) and UUID32.fullmatch(value), 'Malformed/multiple Canonical ID: ' + identifier)
            g.require(value not in owners, 'Duplicate Canonical ID: ' + value)
            owners[value] = identifier
        for role, expected in item['expected'].items():
            g.require(record.get(role) == expected, role + ' drift: ' + identifier + '; expected ' + expected)
    if previous is not None:
        for identifier, value in previous.items():
            g.require(identifier in index, 'Governed resource physically deleted; non-reuse requires adjudication: ' + identifier)
            g.require(index[identifier]['record'].get('canonicalId') == value, 'Changed/deleted established Canonical ID: ' + identifier)
    return dict(total_elements=len(index), valid_canonicalId=len(owners), missing_canonicalId=len(missing),
                malformed_canonicalId=0, duplicate_canonicalId=0, multiple_canonicalId_claims=0)


def allocate_missing(index, allocator=uuid.uuid4):
    """Only bootstrap calls this. Validate all existing claims before allocating."""
    validate_records(index, allow_absent=True)
    allocated = 0
    used = {x['record']['canonicalId'] for x in index.values() if 'canonicalId' in x['record']}
    for item in index.values():
        if 'canonicalId' not in item['record']:
            value = allocator().hex
            g.require(UUID32.fullmatch(value) and value not in used, 'UUID allocation collision/invalid value; stop without publication')
            item['record']['canonicalId'] = value
            used.add(value)
            allocated += 1
    validate_records(index)
    return allocated


def load(b, check_history=True):
    source, motivation = read(SOURCE), read(MOTIVATION)
    index = descriptors(b, source, motivation)
    previous = None
    if check_history:
        location = archive()
        correspondence = read(RAD / 'correspondence.json')['canonical_identity_establishment']
        g.require(hashlib.sha256((location / 'manifest.json').read_bytes()).hexdigest() == correspondence['manifest_sha256'],
                  'Established source history manifest changed')
        manifest = read(location / 'manifest.json')
        g.require(hashlib.sha256(LEGACY_MODEL.read_bytes()).hexdigest() == manifest['accepted_publication_sha256'],
                  'Accepted Release 1.0.6 publication changed')
        for name, digest in manifest['files_sha256'].items():
            path = (location / name).resolve()
            g.require(path.is_relative_to(location) and hashlib.sha256(path.read_bytes()).hexdigest() == digest,
                      'Established source history changed: ' + name)
        old = descriptors(b, read(location / 'architecture.json'), read(location / 'motivation.json'))
        previous = {identifier: item['record']['canonicalId'] for identifier, item in old.items()}
    return index, validate_records(index, previous)


def property_id(role):
    return 'governed-' + re.sub(r'([a-z])([A-Z])', r'\1-\2', role).lower()


def remove_properties(root):
    root = copy.deepcopy(root)
    definitions = root.find('a:propertyDefinitions', g.N)
    ids = set()
    for definition in list(definitions):
        if definition.findtext('a:name', namespaces=g.N) in ROLES:
            ids.add(definition.get('identifier'))
            definitions.remove(definition)
    for props in root.findall('.//a:properties', g.N):
        for prop in list(props):
            if prop.get('propertyDefinitionRef') in ids:
                props.remove(prop)
    # The model release Property is publication metadata, not element semantics.
    value = root.find("a:properties/a:property[@propertyDefinitionRef='baseline-architecture-release']/a:value", g.N)
    if value is not None:
        value.text = release()['version']
    return root


def validate_publication(b, root):
    index, result = load(b)
    if hasattr(b, 'reconciliation_index'):
        index.update(b.reconciliation_index)
        result = validate_records(index)
    if hasattr(b, 'home_context_index'):
        index = b.home_context_index
        result = validate_records(index)
    definitions = {p.get('identifier'): p.findtext('a:name', namespaces=g.N)
                   for p in root.findall('a:propertyDefinitions/a:propertyDefinition', g.N)}
    for role in ROLES:
        g.require(list(definitions.values()).count(role) == 1, 'Missing/multiple governed Property definitions: ' + role)
    elements = {e.get('identifier'): e for e in root.findall('a:elements/a:element', g.N)}
    g.require(set(elements) == set(index), 'Unexpected/missing semantic element; missing/extra governed metadata owner')
    for identifier, item in index.items():
        element = elements[identifier]
        expected = {**{role: item['record'][role] for role in FIELDS}, 'archimateType': item['archimateType']}
        if item['harmoniaType']:
            expected['harmoniaType'] = item['harmoniaType']
        claims = collections.defaultdict(list)
        for prop in element.findall('a:properties/a:property', g.N):
            role = definitions[prop.get('propertyDefinitionRef')]
            if role in ROLES:
                claims[role].append([v.text for v in prop.findall('a:value', g.N)])
        g.require(dict(claims) == {k: [[v]] for k, v in expected.items()}, 'Missing/multiple/changed governed Properties: ' + identifier)
        g.require(element.get(g.TYPE) == item['archimateType'], 'Native type drift: ' + identifier)
    for prop in root.findall('.//a:properties/a:property', g.N):
        if definitions[prop.get('propertyDefinitionRef')] == 'canonicalId':
            g.require(prop.getparent().getparent().tag == g.tag('element'),
                      'Canonical IDs are restricted to architectural elements')
    return result


def overlay(outputs, b, report):
    """The active publication's allowlisted metadata step, after semantic replay."""
    from architecture_release import load_release, version_parts
    current = load_release()
    g.require(version_parts(current['version']) >= version_parts(read(SOURCE)['governedMetadata']['establishedRelease']),
              'Publication release precedes identity establishment')
    index, result = load(b)
    root = copy.deepcopy(b.root)
    definitions = root.find('a:propertyDefinitions', g.N)
    g.require(not any(d.findtext('a:name', namespaces=g.N) in ROLES for d in definitions), 'Pre-existing conflicting governed Property definitions')
    for role in ROLES:
        definition = g.child(definitions, 'propertyDefinition', identifier=property_id(role), type='string')
        g.child(definition, 'name', role)
    for element in root.findall('a:elements/a:element', g.N):
        item = index[element.get('identifier')]
        props = element.find('a:properties', g.N)
        if props is None:
            props = g.child(element, 'properties')
        values = {**{role: item['record'][role] for role in FIELDS}, 'archimateType': item['archimateType']}
        if item['harmoniaType']:
            values['harmoniaType'] = item['harmoniaType']
        for role, value in values.items():
            prop = g.child(props, 'property', propertyDefinitionRef=property_id(role))
            g.child(prop, 'value', value)
    root.find("a:properties/a:property[@propertyDefinitionRef='baseline-architecture-release']/a:value", g.N).text = current['version']
    data = g.serialize(root)
    parsed = g.ET.fromstring(data)
    g.load_schema(BASE / 'schemas/archimate3_Diagram.xsd').assertValid(parsed)
    g.validate_identifiers(parsed)
    validate_publication(b, parsed)
    # Exact semantic/presentation preservation after removing only new fields.
    g.require(g.signature(remove_properties(parsed)) == g.signature(b.root), 'Metadata publication changed architecture or presentation')
    outputs = dict(outputs)
    outputs.pop(LEGACY_MODEL)
    outputs.pop(DEST / 'validation.json')
    outputs[model_path()] = data
    # Extend the existing Markdown record tables; old names and aliases remain.
    for identifier, item in index.items():
        record = item['record']
        if identifier not in b.byid:
            continue
        import synchronise_baseline as s
        path = RAD / (s.group(record) + '.md')
        text = outputs[path].decode()
        marker = '| Source capture key | ' + record['key'] + ' |'
        rows = '\n'.join('| ' + role + ' | ' + record[role] + ' |' for role in FIELDS)
        text = text.replace(marker, marker + '\n' + rows)
        old = '| Architecture Identity | ' + (record['reference_id'] if record['reference_id'].startswith('EC-') else 'Unresolved — no allocation') + ' |'
        text = text.replace(old, '| Legacy identity evidence | ' + (record['reference_id'] if record['reference_id'].startswith('EC-') else 'No legacy allocation') + ' |', 1)
        outputs[path] = text.encode()
    path = RAD / 'motivation.md'
    text = outputs[path].decode().replace('Publication keys and XML IDs allocate no persistent Architecture Identities.',
        'Resource-owned metadata follows the identity and naming contract; publication keys and XML IDs remain separate.')
    text = text.replace('| Semantic ID |', '| Semantic ID | canonicalId | displayName | friendlyName | canonicalName |')
    text = text.replace('| --- | --- | --- | --- | --- | --- |', '| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |')
    for identifier, item in index.items():
        marker = '| `' + identifier + '` |'
        if marker in text:
            text = text.replace(marker, marker + ' ' + ' | '.join(item['record'][role].replace('|', '&#124;') for role in FIELDS) + ' |')
    outputs[path] = text.encode()
    report = copy.deepcopy(report)
    report.update(architecture_release=current, output_sha256=hashlib.sha256(data).hexdigest(),
                  governed_metadata=result, canonical_identity_source='Resource fields in governed RADS captures',
                  roundtrip_status='AWAITING REAL ARCHI 5.10 NO-EDIT EXPORT',
                  persistent_identities_allocated=0,
                  markdown_sha256={str(p.relative_to(ROOT)): hashlib.sha256(v).hexdigest() for p, v in outputs.items() if p.parent == RAD})
    report['checks']['governed_identity_and_naming'] = 'PASS'
    report['checks']['exact_semantic_and_presentation_preservation'] = 'PASS'
    outputs[validation_path()] = encoded(report)
    return outputs, b, report


def bootstrap(target_release):
    import synchronise_baseline as s
    from architecture_release import load_release, version_parts
    g.require(not active(), 'Identity establishment already exists; bootstrap refuses reallocation')
    major, minor, patch = version_parts(load_release()['version'])
    g.require(target_release == f'{major}.{minor}.{patch + 1}', 'Establishment requires the policy-defined next patch release')
    old_outputs, b, _ = s.publish()
    source, motivation = read(SOURCE), read(MOTIVATION)
    source['historical_reference_elements'] = historical_records(b)
    index = descriptors(b, source, motivation)
    for item in index.values():
        for role, expected in item['expected'].items():
            g.require(role not in item['record'] or item['record'][role] == expected, 'Conflicting pre-existing naming claim')
            item['record'][role] = expected
    validate_records(index, allow_absent=True)
    location = PUBLISHING / target_release / ('identity-establishment-' + target_release)
    g.require(not location.exists(), 'Existing/incomplete establishment history; inspect without reallocating')
    count = allocate_missing(index)
    source['governedMetadata'] = dict(schemaVersion=1, establishedRelease=target_release, uuidMechanism='UUIDv4 via Python uuid.uuid4().hex; persisted once',
                                    nameSource='current governed label', sourceHistory=str(location.relative_to(ROOT)))
    # Complete source history is written exclusively before replacing authority.
    # An interruption leaves an explicit incomplete establishment, never a retry
    # that invents replacement identities.
    location.mkdir(parents=True)
    prior = location / 'prior-release'
    prior.mkdir()
    preserved = {SOURCE: historical_bytes(SOURCE), MOTIVATION: historical_bytes(MOTIVATION), ROOT / 'architecture-release.json': (ROOT / 'architecture-release.json').read_bytes()}
    preserved.update({p: data for p, data in old_outputs.items() if p.parent == RAD})
    files = {}
    for path, data in preserved.items():
        name = 'prior-release/' + path.name
        (location / name).write_bytes(data)
        files[name] = hashlib.sha256(data).hexdigest()
    for name, value in (('architecture.json', source), ('motivation.json', motivation)):
        data = encoded(value)
        (location / name).write_bytes(data)
        files[name] = hashlib.sha256(data).hexdigest()
    (location / 'manifest.json').write_bytes(encoded(dict(startingRelease=load_release()['version'], establishedRelease=target_release,
                                                        accepted_publication_sha256=hashlib.sha256(LEGACY_MODEL.read_bytes()).hexdigest(),
                                                        allocated=count, uuidMechanism=source['governedMetadata']['uuidMechanism'], files_sha256=files)))
    for path, value in ((MOTIVATION, motivation), (SOURCE, source)):
        temporary = path.with_suffix('.establishing')
        with temporary.open('xb') as stream:
            stream.write(encoded(value))
        os.replace(temporary, path)
    correspondence = read(RAD / 'correspondence.json')
    correspondence['canonical_identity_establishment'] = dict(source_history=str(location.relative_to(ROOT)),
        manifest_sha256=hashlib.sha256((location / 'manifest.json').read_bytes()).hexdigest())
    (RAD / 'correspondence.json').write_bytes(encoded(correspondence))
    return dict(status='PASS', allocated=count, **load(b)[1])


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    commands = parser.add_subparsers(dest='command', required=True)
    migration = commands.add_parser('bootstrap')
    migration.add_argument('--establish-for', required=True)
    commands.add_parser('validate')
    args = parser.parse_args()
    try:
        if args.command == 'bootstrap':
            result = bootstrap(args.establish_for)
        else:
            import synchronise_baseline as s
            import reconcile_108
            outputs, b, _ = s.publish()
            result = dict(status='PASS', **validate_publication(b, g.ET.fromstring(outputs[model_path()])))
        print(json.dumps(result, indent=2))
        return 0
    except (OSError, ValueError, KeyError, g.ET.LxmlError) as exc:
        print('INVALID: ' + str(exc), file=sys.stderr)
        return 1


if __name__ == '__main__':
    sys.exit(main())
