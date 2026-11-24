#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Purpose-selected verification of the authorised Iteration 001 increment.

Run from repository root with the declared current EA Python dependencies.
The before record pins recoverable Git inputs; no hidden working snapshot used.
This evidence check does not provide Solution Architect acceptance.
"""
from collections import Counter
import hashlib
import io
import json
from pathlib import Path
import re
import subprocess
import sys
import tarfile

sys.path.insert(0, 'enterprise-architecture/tooling')
import architecture_continuum as c
import continuum_navigation as nav

E = Path(__file__).resolve().parents[1]
BEFORE = json.loads((E / 'before/baseline.json').read_text())
SOURCE = BEFORE['source']
REV = BEFORE['baselineCommit']
REUSED = {'NFR-019': '3e45e60affb44bf384d54de26d3add6a',
          'NFR-057': 'b5d3447a05b249a8af154136fc36c4d9',
          'NFR-058': 'f2e504e7d6374e41a81303265adda6de',
          'NFR-059': '4c02fec3af264b878ee3e0cfaecbf55f'}
RESERVED = {'NFR-116', 'NFR-120', 'NFR-128', 'NFR-134', 'NFR-140', 'NFR-144'}


def require(condition, message):
    if not condition:
        raise AssertionError(message)


def main():
    original = subprocess.check_output(['git', 'show', REV + ':' + SOURCE])
    require(hashlib.sha256(original).hexdigest() == BEFORE['sourceSha256'], 'Input source pin mismatch')
    require(original in Path(SOURCE).read_bytes(), 'Original source bytes not preserved')
    source = original.decode()
    statements = {}
    groups = {}
    for line in source.splitlines():
        if line.startswith('| `NFR-'):
            v = [s.strip() for s in line.strip('|').split('|')]
            spec = re.search(r'«(\w+)»', v[2])
            statements[v[0].strip('`')] = {'name': v[1], 'type': re.search(r'`(\w+)`', v[2])[1],
                                         'specialization': spec[1] if spec else None, 'description': v[3],
                                         'domain': re.search(r'`([^`]+)`', v[4])[1]}
        elif line.startswith('### G'):
            key, name = line[4:].split(' — ', 1)
            groups[key] = {'name': name}
        elif line.startswith('| `G'):
            v = [s.strip() for s in line.strip('|').split('|')]
            groups[v[0].strip('`')] = {'name': v[1], 'concern': v[2]}
    require((len(statements), len(groups)) == (150, 82), 'Source inventory mismatch')
    catalogue, elements = c.load()
    bindings = {b['selector']: cid for cid, (m, _, _) in elements.items()
                for b in m['sourceBindings'] if b['source'] == SOURCE}
    require(set(bindings) == set(statements) | set(groups), 'Incomplete/extra source correspondence')
    require(RESERVED.isdisjoint(bindings), 'Reserved identifier incorporated')
    for sid, row in statements.items():
        metadata, body, _ = elements[bindings[sid]]
        require(metadata['archimateType'] == row['type'] and metadata['specialization'] == row['specialization'], sid + ': reclassification')
        require(row['description'] in body, sid + ': missing or changed description/qualifiers')
        require(metadata['registerEntries'][0] == {'id': sid, **row}, sid + ': source facets changed')
        if sid in REUSED:
            require(bindings[sid] == REUSED[sid], sid + ': identity not preserved')
    expected_edges = {(key.split('.')[0], key, 'Composition') for key in groups if '.' in key}
    expected_edges |= {(s['domain'], key, 'Composition') for key, s in statements.items()}
    expected_edges |= {('NFR-063', key, 'Association') for key in ['NFR-066', 'NFR-068', 'NFR-069']}
    source_edges = [edge for m, _, _ in elements.values() for edge in m['relationships']
                    if edge['provenance'].get('source') == SOURCE]
    reverse = {elements[cid][0]['exchange']['identifier']: sid for sid, cid in bindings.items()}
    actual_edges = [(reverse[e['source']], reverse[e['target']], e['archimateType']) for e in source_edges]
    require(len(actual_edges) == 222 and set(actual_edges) == expected_edges, 'Source edge semantics/coverage mismatch')
    tree = nav.load(elements=elements)
    locations = nav.by_element(tree)
    for key, row in groups.items():
        metadata, body, _ = elements[bindings[key]]
        require(metadata['archimateType'] == 'Grouping' and metadata['specialization'] is None, key + ': group reinterpreted')
        require(metadata['displayName'] == row['name'], key + ': group name changed')
        if row.get('concern', '—') != '—':
            require(row['concern'] in body, key + ': concern changed')
        if '.' in key:
            expected = {bindings[sid] for sid, s in statements.items() if s['domain'] == key}
            require(set(locations[bindings[key]].get('relatedCanonicalIds', [])) == expected, key + ': missing navigation allocation')
    # Recover all baseline canonical bytes directly from the governed Git state.
    archive = subprocess.check_output(['git', 'archive', REV, 'enterprise-architecture/continuum'])
    baseline = {}
    with tarfile.open(fileobj=io.BytesIO(archive)) as tar:
        for member in tar.getmembers():
            if not member.isfile() or not member.name.endswith('.md') or member.name.endswith('/MANIFEST.md'):
                continue
            content = tar.extractfile(member).read()
            metadata, body = c.decode(content.decode())
            baseline[metadata['canonicalId']] = (metadata, body, member.name, content)
    require(len(baseline) == 1614 and set(baseline) <= set(elements), 'Lost baseline identity')
    require(len(elements) == 1842 and len(set(elements) - set(baseline)) == 228, 'Unexpected population delta')
    baseline_edges = {r['identifier']: r for m, _, _, _ in baseline.values() for r in m['relationships']}
    current_edges = {r['identifier']: r for m, _, _ in elements.values() for r in m['relationships']}
    require(all(current_edges.get(key) == row for key, row in baseline_edges.items()), 'Existing relationship changed')
    require(set(current_edges) - set(baseline_edges) == {e['identifier'] for e in source_edges}, 'Unintended new relationship')
    changed = []
    for cid, (old, body, path, content) in baseline.items():
        metadata, current_body, current_path = elements[cid]
        require(metadata['exchange']['identifier'] == old['exchange']['identifier'], cid + ': representation identity changed')
        require(metadata['archimateType'] == old['archimateType'], cid + ': native type changed')
        if cid in REUSED.values():
            original_content = body.split('## Architectural content\n', 1)[1].strip()
            require(original_content in current_body, cid + ': historical contribution changed')
            require(metadata['historicalContributionStanding'] == old['standing'], cid + ': historical standing changed')
            require(all(binding in metadata['sourceBindings'] for binding in old['sourceBindings']), cid + ': original binding lost')
            require(all(item in metadata['provenance'] for item in old['provenance']), cid + ': original provenance lost')
            require(all(metadata[key] == old[key] for key in ['friendlyName', 'canonicalName']), cid + ': governed name changed')
        if current_path.read_bytes() != content:
            changed.append(cid)
        if cid not in REUSED.values():
            require(metadata == old, cid + ': unrelated baseline metadata changed')
            require(current_body == body, cid + ': unrelated baseline meaning changed')
    require(set(changed) == set(REUSED.values()), 'Unexpected baseline canonical edits')
    xml, projection = c.project()
    model = c.ET.fromstring(xml)
    prior_path = 'enterprise-architecture/design/archimate/published/1.1.16/Harmonia-Architecture.xml'
    prior_bytes = subprocess.check_output(['git', 'show', REV + ':' + prior_path])
    require(Path(prior_path).read_bytes() == prior_bytes, 'Predecessor publication changed')
    prior = c.ET.fromstring(prior_bytes)
    def views(root):
        return {v.get('identifier'): c.ET.tostring(v, method='c14n')
                for v in root.findall('a:views/a:diagrams/a:view', c.N)}
    require(views(prior) == views(model), 'Authored View content changed')
    definitions = {'Axiom': 'Principle', 'ArchitecturalAxiom': 'Principle',
                   'ImplementationGuideline': 'CourseOfAction', 'Mandate': 'Constraint',
                   **{n: 'Requirement' for n in ['FunctionalRequirement', 'NonFunctionalRequirement',
                                                'InformationRequirement', 'TransitionRequirement', 'AssuranceRequirement']}}
    require(all(catalogue['specializations'][name]['nativeType'] == base for name, base in definitions.items()), 'Specialisation base changed')
    require('DerivedRequirement' not in catalogue['specializations'], 'Provenance turned into subtype')
    return {'status': 'PASS', 'baselineRelease': '1.1.16', 'resultRelease': '1.1.17',
            'activeSourceStatements': len(statements), 'reusedAndExtended': REUSED,
            'newStatements': 146, 'subjectGroups': 13, 'subjectSubgroups': 69,
            'newCanonicalElements': 228, 'newRelationships': 222,
            'relationshipSemantics': {'subjectContainmentCompositions': 219, 'explicitJointWorkloadAssociations': 3},
            'resultElements': len(elements), 'resultRelationships': len(current_edges),
            'allBaselineIdentitiesPreserved': True, 'baselineCanonicalChanges': changed,
            'baselineNativeTypesAndRepresentationIdsPreserved': True,
            'existingRelationshipsAndAuthoredViewsUnchanged': True,
            'reusedHistoricalContributionsVerbatim': True,
            'predecessorPublicationUnchanged': True, 'reviewedSourceBytesPreserved': True,
            'reservedSourceIdsExcluded': sorted(RESERVED),
            'sourceClassificationCounts': dict(Counter((s['specialization'] or s['type']) for s in statements.values())),
            'specialisationsRegistered': definitions,
            'projection': projection,
            'limits': 'Purpose-selected source, identity, graph, schema and preservation checks. No actual Archi import/export, Solution Architect structural review or result acceptance asserted.'}


if __name__ == '__main__':
    result = main()
    (E / 'after/increment-verification.json').write_bytes(c.encoded(result))
    print(json.dumps({k: v for k, v in result.items() if k != 'projection'}, indent=2))
