#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded application of HCRL 1.1.5-002's reviewed semantic reconciliation.

Consumes the retained explicit correspondence plan, never discovers identity from
names. Repeated application preserves IDs. Routine publication does not call it.
"""
import argparse
from collections import Counter
import copy
import json
import os
from pathlib import Path
import re
import uuid

import architecture_continuum as c

EVIDENCE = Path('enterprise-architecture/design/evidence/1.1.5/002')
PLAN = EVIDENCE / 'before/arb-reconciliation-plan.json'
AUTHORITY = 'enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation'


def native_treatment(assertion, source_type, target_type, plan, matrix):
    original = assertion['source_assertion']
    if original['status'].lower() in plan['excludedSourceStatuses']:
        return {'disposition': 'explicitly unestablished', 'relationshipType': None,
                'reverse': False, 'reason': original.get('reason', 'Source does not establish relationship existence.')}
    rule = plan['predicateRules'].get(original['type'], {'type': plan['fallback'], 'reverse': False})
    a, b = (target_type, source_type) if rule['reverse'] else (source_type, target_type)
    valid = matrix['keys'][rule['type']] in matrix['endpoints'].get(a, {}).get(b, '')
    native = rule['type'] if valid else plan['fallback']
    c.require(matrix['keys'][native] in matrix['endpoints'].get(a, {}).get(b, ''), 'No valid native fallback')
    return {'disposition': 'native relationship', 'relationshipType': native,
            'reverse': rule['reverse'], 'reason': 'ARB predicate rule; native endpoint applicability verified.' if valid else 'Stronger rule is endpoint-invalid; ARB Association fallback.'}


def apply(root=c.ROOT):
    plan = c.read_json(root / PLAN)
    matrix = c.read_json(root / c.CONFIG / 'native-relationships.json')
    _, elements = c.load(root)
    before = {cid: c.sha(path.read_bytes()) for cid, (_, _, path) in elements.items()}
    capability_rows = []
    for decision in plan['capabilities']:
        old_id, current_id = decision['historicalCanonicalId'], decision['currentCanonicalId']
        old, old_body, old_path = elements[old_id]
        current, current_body, current_path = elements[current_id]
        c.require(old_id != current_id and old['archimateType'] == current['archimateType'] == 'Capability', 'Invalid capability correspondence')
        c.require(current['standing'] == decision['currentStanding'], 'Reviewed target standing changed')
        for key, expected in [('displayName', decision['historicalDisplayName']), ('canonicalName', decision['historicalCanonicalName'])]:
            c.require(old[key] == expected, 'Reviewed historical capability changed: ' + old_id)
        c.require(current['canonicalName'] == decision['currentCanonicalName'], 'Reviewed target context changed')
        prior = old['identity'].get('correspondence')
        if not prior:
            old['identity']['correspondence'] = {'authority': AUTHORITY, 'disposition': decision['disposition'],
                'successorCanonicalId': current_id, 'previousStanding': copy.deepcopy(old['standing']),
                'historicalIdentityReserved': True, 'reason': decision['reason']}
            old['standing'].update(semantic='Superseded historical identity — ' + decision['disposition'], currentness='superseded')
            old_body += '\n## ARB correspondence\n\n' + decision['reason'] + '\n\nCurrent representation: [' + current['displayName'] + '](' + os.path.relpath(current_path, old_path.parent) + '). This resource reserves the historical identity and representation for provenance and existing Views. Its missing original definition remains missing.\n'
        else:
            c.require(prior['successorCanonicalId'] == current_id, 'Conflicting prior lineage')
        predecessors = current['identity'].setdefault('predecessorCanonicalIds', [])
        if old_id not in predecessors:
            predecessors.append(old_id); predecessors.sort()
            current_body += '\n## ARB identity continuity\n\nHistorical identity [' + old_id + '](' + os.path.relpath(old_path, current_path.parent) + ') has ' + decision['disposition'] + ' with this capability under HCRL 1.1.5-002. Historical provenance and UUID reservation remain with that record; this resource retains its existing identity and independent standing.\n'
        old['identity']['correspondence']['authority'] = AUTHORITY
        capability_rows.append(decision)
        elements[old_id] = old, old_body, old_path
        elements[current_id] = current, current_body, current_path

    defs = {p.findtext('a:name', namespaces=c.N): p.get('identifier') for p in c.secure_xml(root / c.DESIGN / 'projection-template.xml').findall('a:propertyDefinitions/a:propertyDefinition', c.N)}
    existing = {r['identifier']: r for m, _, _ in elements.values() for r in m['relationships']}
    predicate_rows = []
    for cid, (m, body, path) in list(elements.items()):
        for q in m.get('qualifiedAssertions', []):
            target = elements[q['targetCanonicalId']][0]
            mapping = native_treatment(q, m['archimateType'], target['archimateType'], plan, matrix)
            mapping['authority'] = AUTHORITY
            if mapping['relationshipType']:
                provider, consumer = (target, m) if mapping['reverse'] else (m, target)
                # Relationship representation identity is distinct from resource UUID.
                rid = 'id-' + uuid.uuid5(c.SEMANTIC_NAMESPACE, 'arb-qualified:' + q['sourcePointer']).hex
                mapping['relationshipIdentifier'] = rid
                mapping['sourceCanonicalId'] = provider['canonicalId']
                mapping['targetCanonicalId'] = consumer['canonicalId']
                props = {'Baseline / Record State': q['record_state'],
                         'Baseline / Architectural Status': q['source_assertion']['status'],
                         'Baseline / Source Predicate': q['source_assertion']['type'],
                         'Baseline / Source Assertion': json.dumps(q['source_assertion'], ensure_ascii=False, sort_keys=True),
                         'v0.2 / Snapshot Pointer': q['sourcePointer']}
                row = {'identifier': rid, 'archimateType': mapping['relationshipType'],
                       'source': provider['exchange']['identifier'], 'target': consumer['exchange']['identifier'],
                       'attributes': {'isDirected': 'true'} if mapping['relationshipType'] == 'Association' else {},
                       'standing': {'semantic': q['source_assertion']['status'], 'currentness': q['record_state']},
                       'provenance': {'source': str(PLAN), 'selector': q['sourcePointer'], 'authority': AUTHORITY,
                                      'qualifiedSourceCanonicalId': cid, 'sourceAssertion': copy.deepcopy(q['source_assertion'])},
                       'name': [{'text': q['source_assertion']['type'], 'attributes': {c.LANG: 'en'}}],
                       'documentation': [{'text': 'Native treatment authorised by HCRL 1.1.5-002. Original qualification: ' + json.dumps(q['source_assertion'], ensure_ascii=False, sort_keys=True), 'attributes': {c.LANG: 'en'}}],
                       'properties': [{'ref': defs[k], 'values': [{'text': v, 'attributes': {c.LANG: 'en'}}]} for k, v in props.items()]}
                if row['archimateType'] == 'Composition':
                    # A structural assertion already present is not another parent.
                    prior = next((r for r in provider['relationships'] if r['identifier'] != rid and
                                  r['archimateType'] == 'Composition' and r['source'] == row['source'] and r['target'] == row['target']), None)
                    if prior:
                        provider['relationships'][:] = [r for r in provider['relationships'] if r['identifier'] != rid]
                        existing.pop(rid, None)
                        contribution = {'sourcePointer': q['sourcePointer'], 'sourceAssertion': copy.deepcopy(q['source_assertion']),
                                        'recordState': q['record_state'], 'authority': AUTHORITY}
                        contributions = prior.setdefault('qualifiedPredicateContributions', [])
                        if contribution not in contributions: contributions.append(contribution)
                        rid = prior['identifier']; row = prior
                        mapping['relationshipIdentifier'] = rid
                        mapping['existingNativeRelationshipReused'] = True
                if rid in existing:
                    c.require(existing[rid] == row, 'Conflicting materialised assertion: ' + rid)
                else:
                    provider['relationships'].append(row); existing[rid] = row
            q['nativeMapping'] = mapping
            predicate_rows.append({'sourcePointer': q['sourcePointer'], 'predicate': q['source_assertion']['type'],
                                   'originalSubject': cid, 'originalTarget': q['targetCanonicalId'],
                                   'sourceStanding': q['source_assertion']['status'], 'recordState': q['record_state'], **mapping})
        if m.get('qualifiedAssertions'):
            introduction = 'Original source predicates, evidence and qualifications remain in the metadata. The ARB has now adjudicated their native treatment; explicitly unestablished relationship existence remains excluded. Native representation does not promote source standing.\n\n'
            rows = ['| Source predicate | Target element | Source standing / currentness | ARB native treatment |', '| --- | --- | --- | --- |']
            for q in m['qualifiedAssertions']:
                target, _, dest = elements[q['targetCanonicalId']]
                mapping = q['nativeMapping']
                treatment = (mapping['relationshipType'] + (' (reverse)' if mapping['reverse'] else '') + ' — `' + mapping['relationshipIdentifier'] + '`') if mapping['relationshipType'] else 'Excluded: source expressly leaves relationship unestablished'
                rows.append('| ' + q['source_assertion']['type'] + ' | [' + target['displayName'].replace('|', '\\|') + '](' + os.path.relpath(dest, path.parent) + ') | ' + q['source_assertion']['status'] + ' / ' + q['record_state'] + ' | ' + treatment + ' |')
            section = '## Qualified relationship assertions\n\n' + introduction + '\n'.join(rows) + '\n\n'
            body, count = re.subn(r'(?ms)^## Qualified relationship assertions\n.*?(?=^## |\Z)', lambda _: section, body, count=1)
            c.require(count == 1, 'Missing qualified assertion body: ' + cid)
            elements[cid] = m, body, path
    for m, body, path in elements.values():
        content = c.encode(m, body)
        if content != path.read_text(): path.write_text(content)
    report = {'authority': AUTHORITY, 'entryRevision': plan['entryRevision'],
              'capabilityCounts': dict(Counter(r['disposition'] for r in capability_rows)),
              'capabilities': capability_rows,
              'predicateCounts': dict(Counter(r['relationshipType'] or r['disposition'] for r in predicate_rows)),
              'predicates': predicate_rows, 'allocatedElementIdentities': 0,
              'modifiedElementsThisInvocation': sum(before[cid] != c.sha(path.read_bytes()) for cid, (_, _, path) in elements.items())}
    dest = root / EVIDENCE / 'after/arb-semantic-reconciliation.json'
    # Correspondences are stable on retry. Invocation-local writes are explicitly
    # distinguished from the complete entry-to-result transition verification.
    dest.write_bytes(c.encoded(report))
    c.validate(root)
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--root', type=Path, default=c.ROOT)
    args = parser.parse_args()
    result = apply(args.root)
    print(json.dumps({k: result[k] for k in ('capabilityCounts', 'predicateCounts', 'allocatedElementIdentities', 'modifiedElementsThisInvocation')}))
