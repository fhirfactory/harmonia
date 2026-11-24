#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Apply explicit Subject containment/Home pairs, preserving original assertions."""
import copy
import json
from pathlib import Path
import sys
import uuid

ROOT = Path(__file__).resolve().parents[7]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c

B = Path('enterprise-architecture/design/evidence/1.1.18/001')
A = ROOT / B / 'after/subject-hierarchy'
TASK = str(B / 'before/ai-agent-tasks/20261102T002914Z-subject-grouping-hierarchy.md')
AUTHORITY = 'enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md#dr-12'


def execute():
    pins = json.loads((ROOT / B / 'before/subject-hierarchy/input-pins.json').read_text())
    created = json.loads((A / 'incorporated-groupings.json').read_text())
    assert len(created) == 115, 'Complete the governed Grouping population before relationships'
    _, original = c.load(ROOT)
    elements = {cid: (copy.deepcopy(m), body, p) for cid, (m, body, p) in original.items()}
    subjects = pins['subjects']
    pairs = [(subjects[code.split('.')[0]], child, 'Existing Subject ' + code)
             for code, child in subjects.items() if '.' in code]
    pairs += [(r['parentCanonicalId'], r['canonicalId'], 'Authored Grouping ' + r['exchangeId']) for r in created]
    assert len(pairs) == len(set((a, b) for a, b, _ in pairs)) == 211
    by_rep = {m['exchange']['identifier']: cid for cid, (m, _, _) in elements.items()}
    keys = {p.get('identifier'): p.findtext('a:name', namespaces=c.N)
            for p in c.secure_xml(ROOT / c.DESIGN / 'projection-template.xml').findall('a:propertyDefinitions/a:propertyDefinition', c.N)}

    def is_home(row):
        claims = [v['text'] for p in row.get('properties', [])
                  if keys.get(p['ref']) in ('Specialization', 'specialization') for v in p['values']]
        assert len(claims) <= 1, ('Ambiguous Home specialization', row['identifier'])
        return row.get('specialization') == 'Home' or claims == ['Home']

    all_rows = [r for m, _, _ in elements.values() for r in m.get('relationships', [])]
    ledger = []
    changed = set()
    identifiers = {r['identifier'] for r in all_rows}
    for parent, child, context in pairs:
        p, ch = elements[parent][0], elements[child][0]
        assert p['archimateType'] == ch['archimateType'] == 'Grouping'
        pr, cr = p['exchange']['identifier'], ch['exchange']['identifier']
        compositions = [r for r in all_rows if r['archimateType'] == 'Composition' and r['target'] == cr]
        homes = [r for r in all_rows if is_home(r) and r['source'] == cr]
        assert len(compositions) <= 1 and all(r['source'] == pr for r in compositions), ('Conflicting Composition', child, compositions)
        assert len(homes) <= 1 and all(r['target'] == pr for r in homes), ('Conflicting Home', child, homes)
        for kind, owner, source, target, existing in (
                ('Composition', p, pr, cr, compositions), ('Home', ch, cr, pr, homes)):
            if existing:
                row = existing[0]
                if kind == 'Home':
                    assert row['archimateType'] == 'Association' and row.get('attributes', {}).get('isDirected') == 'true'
                operation = 'REUSED'
            else:
                identifier = 'id-' + uuid.uuid4().hex
                assert identifier not in identifiers
                identifiers.add(identifier)
                row = dict(identifier=identifier, source=source, target=target,
                    archimateType='Association' if kind == 'Home' else 'Composition',
                    attributes={'isDirected': 'true'} if kind == 'Home' else {},
                    standing={'semantic': 'Solution Architect authorised Subject hierarchy and Home completion; result review pending',
                              'currentness': 'current'},
                    provenance={'source': TASK, 'retainedAt': TASK,
                        'selector': 'Consolidated scope / Composition containment and distinct authoritative Home; ' + context,
                        'sha256': c.sha((ROOT / TASK).read_bytes()), 'authority': AUTHORITY,
                        'meaning': 'Authoritative architectural location' if kind == 'Home' else 'Subject Grouping containment'},
                    name=[{'text': 'Home' if kind == 'Home' else 'Subject Grouping containment'}],
                    documentation=[{'text': context + '. Composition defines containment; the separate directed child-to-parent Home Association identifies authoritative architectural location. No new statement allocation or component responsibility is asserted.'}],
                    properties=[{'ref': 'propid-40', 'values': [{'text': 'current', 'attributes': {}}]}])
                if kind == 'Home':
                    row['specialization'] = 'Home'
                    row['properties'].append({'ref': 'specialization', 'values': [{'text': 'Home', 'attributes': {}}]})
                owner['relationships'].append(row)
                all_rows.append(row)
                changed.add(by_rep[source])
                operation = 'CREATED'
            ledger.append(dict(parentCanonicalId=parent, childCanonicalId=child, context=context,
                kind=kind, operation=operation, identifier=row['identifier'], source=source, target=target))
    # Preflight the complete source-owned assertion delta before any write.
    from jsonschema import Draft202012Validator
    schema = Draft202012Validator(c.read_json(ROOT / c.CONFIG / 'element.schema.json'))
    for cid in changed:
        metadata = elements[cid][0]
        schema.validate(metadata)
        assert metadata['canonicalId'] == original[cid][0]['canonicalId']
        assert metadata['relationships'][:len(original[cid][0]['relationships'])] == original[cid][0]['relationships']
    # Record exact IDs before applying, permitting inspection/recovery if stopped.
    path = A / 'relationship-results.json'
    assert not path.exists(), 'Inspect existing application ledger before another attempt'
    path.write_bytes(c.encoded(ledger))
    for cid in sorted(changed):
        m, body, target = elements[cid]
        target.write_text(c.encode(m, body))
    report = c.validate(ROOT)
    (A / 'relationship-validation.json').write_bytes(c.encoded(report))
    print(json.dumps(dict(elements=report['elements'], relationships=report['relationships'],
        containedGroupingPairs=len(pairs), compositionCreated=sum(r['kind']=='Composition' and r['operation']=='CREATED' for r in ledger),
        compositionReused=sum(r['kind']=='Composition' and r['operation']=='REUSED' for r in ledger),
        homeCreated=sum(r['kind']=='Home' and r['operation']=='CREATED' for r in ledger),
        homeReused=sum(r['kind']=='Home' and r['operation']=='REUSED' for r in ledger),
        changedSourceOwners=len(changed), validation=report['status']), indent=2))


if __name__ == '__main__':
    execute()
