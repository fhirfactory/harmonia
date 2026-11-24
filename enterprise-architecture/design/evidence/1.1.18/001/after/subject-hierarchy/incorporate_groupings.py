#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Execute the issued, bounded Grouping-only allocation plan in explicit batches.

Canonical creation and validation use maintained Continuum mechanisms. Existing
bindings are inspected before every allocation; a durable ledger supports recovery
without regenerating identities. This adapter does not ingest other native types.
"""
import argparse
import copy
import hashlib
import json
from pathlib import Path
import sys
import time

ROOT = Path(__file__).resolve().parents[7]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c
from governed_naming import component

B = Path('enterprise-architecture/design/evidence/1.1.18/001')
A = ROOT / B / 'after/subject-hierarchy'
SOURCE = str(B / 'before/Harmonia-Continuum-1.1.18-001-004.xml')
TASK = str(B / 'before/ai-agent-tasks/20261102T002914Z-subject-grouping-hierarchy.md')
AUTHORITY = 'enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md#dr-12'


def execute(limit):
    pins = json.loads((ROOT / B / 'before/subject-hierarchy/input-pins.json').read_text())
    assert c.sha((ROOT / SOURCE).read_bytes()) == pins['incomingSha256']
    plan = json.loads((A / 'grouping-plan.json').read_text())
    assert len(plan) == 115 and all(r['nativeType'] == 'Grouping' for r in plan)
    ledger_path = A / 'incorporated-groupings.json'
    ledger = json.loads(ledger_path.read_text()) if ledger_path.exists() else []
    recorded = {r['exchangeId']: r for r in ledger}
    completed = 0
    started = time.monotonic()
    for row in plan:
        if row['exchangeId'] in recorded:
            persisted = recorded[row['exchangeId']]
            metadata, _ = c.decode((ROOT / persisted['source']).read_text())
            assert metadata['canonicalId'] == persisted['canonicalId']
            assert metadata['exchange']['identifier'] == row['exchangeId']
            assert any(b['source'] == SOURCE and b['selector'] == row['selector']
                       for b in metadata['sourceBindings'])
            continue
        catalogue, elements = c.load(ROOT)
        candidates = [(cid, m, body, path) for cid, (m, body, path) in elements.items()
                      if any(b['source'] == SOURCE and b['selector'] == row['selector']
                             for b in m.get('sourceBindings', []))]
        assert len(candidates) <= 1
        if candidates:
            cid, m, body, path = candidates[0]
            assert m['archimateType'] == 'Grouping'
            # Recover a create that persisted before ledger emission/rebinding.
            if row['exchangeId'] not in recorded:
                assert not m.get('relationships')
                initial = m['exchange']['identifier']
                m['exchange']['identifier'] = row['exchangeId']
                m['identity']['incomingRepresentationBinding'] = row['exchangeId']
                path.write_text(c.encode(m, body))
                result = dict(row, canonicalId=cid, source=str(path.relative_to(ROOT)),
                              displayName=m['displayName'], initialCreateRepresentationId=initial,
                              operation='RECOVERED_PERSISTED_CREATE')
                ledger.append(result); recorded[row['exchangeId']] = result
                ledger_path.write_bytes(c.encoded(ledger))
            else:
                assert recorded[row['exchangeId']]['canonicalId'] == cid
                assert m['exchange']['identifier'] == row['exchangeId']
            continue
        assert row['exchangeId'] not in recorded, 'Recorded canonical resource is missing; stop recovery'
        assert all(m['exchange']['identifier'] != row['exchangeId'] for m, _, _ in elements.values())
        name = row['name'].replace('Worflow', 'Workflow')
        # No name match authorises merging. Exact name/source searches were
        # reviewed in the plan; repeat the collision guard before allocation.
        def lexical(value):
            import re
            return re.sub(r'[^a-z0-9]', '', value.lower())
        assert not any(m['archimateType'] == 'Grouping' and lexical(m['displayName']) == lexical(name)
                       for m, _, _ in elements.values()), 'New identity/name collision requires review'
        parent = elements[row['parentCanonicalId']][0]
        code = row['subjectCodes'][0]
        top = elements[pins['subjects'][code.split('.')[0]]][0]
        nav_parent = ['Cross-Cutting References', 'Architectural Subjects', top['displayName']]
        if '.' in code:
            nav_parent.append(parent['displayName'])
        m = c.template('Grouping', root=ROOT)['metadata']
        m.update(displayName=name, friendlyName=component(name), canonicalName=None,
            specialization=None,
            standing={'semantic': 'Solution Architect authorised Subject Grouping incorporation; visual/result review pending',
                      'currentness': 'current'},
            identity={'authority': AUTHORITY, 'incorporationTask': TASK},
            naming={'authority': 'enterprise-architecture/governance/canonical-identity-and-naming.md',
                    'mode': 'Local lexical component; unqualified Grouping supplies no formal naming token or ancestry',
                    'expected': {'displayName': name, 'friendlyName': component(name), 'canonicalName': None}},
            provenance=[{'source': SOURCE, 'selector': row['selector'],
                         'role': 'Authored native Grouping explicitly authorised for incorporation',
                         'retainedAt': SOURCE, 'sha256': pins['incomingSha256']},
                        {'source': TASK, 'selector': 'Consolidated authorised activity / native Groupings and relationships',
                         'role': 'Operative Solution Architect incorporation and spelling authority',
                         'retainedAt': TASK, 'sha256': c.sha((ROOT / TASK).read_bytes())}],
            sourceBindings=[{'source': SOURCE, 'selector': row['selector']}],
            unresolvedProperties={'canonicalName': 'No formal-name qualifier is established for unspecialised Grouping; no token or naming ancestry invented.'})
        body = f'''# {name}

## Architectural content

Native Subject Grouping authored as `{row['name']}` in the retained 004 export,
incorporated under the Solution Architect's Subject Grouping hierarchy Task.
It groups the source-qualified `{row['name'].rsplit('.', 1)[-1]}` concern within
the existing **{parent['displayName']}** Subject ({code}). Its authored full name
is retained apart from explicitly recorded unambiguous spelling corrections.
The incoming record has no Documentation or authored Properties; no normative
definition, native statement, classification of individual statements, or
additional responsibility is invented to complete that absence.

The Task explicitly establishes parent-to-child Composition as containment and
a separate directed child-to-parent Home Association as authoritative architectural
location. These are distinct assertions. Neither diagram membership nor the
Grouping's label alone establishes another Home or a statement allocation.
No component ownership, implementation dependency, Capability decomposition,
new Motivation statement or OpenSpec candidate incorporation is conferred.

Source Exchange identifier: `{row['exchangeId']}`. View placement corroborates
the authorised containing Subject; the exact correspondence is retained in the
iteration's folder/Grouping inventories. Existing canonical identities and all
pre-existing architectural assertions retain their standing. Archi visual
acceptance and HCRL closure remain separate.
'''
        path = c.create({'metadata': m, 'body': body, 'navigationParent': nav_parent}, ROOT)
        created, body = c.decode(path.read_text())
        initial = created['exchange']['identifier']
        created['exchange']['identifier'] = row['exchangeId']
        created['identity']['incomingRepresentationBinding'] = row['exchangeId']
        created['identity']['initialCreateRepresentationId'] = initial
        created['exchange']['previousNativeName'] = row['name']
        path.write_text(c.encode(created, body))
        result = dict(row, canonicalId=created['canonicalId'], source=str(path.relative_to(ROOT)),
                      displayName=name, initialCreateRepresentationId=initial,
                      operation='CREATED_BY_GOVERNED_CREATE; incoming Exchange binding preserved')
        ledger.append(result); recorded[row['exchangeId']] = result
        ledger_path.write_bytes(c.encoded(ledger))
        completed += 1
        print(json.dumps({'created': completed, 'totalPersisted': len(ledger), 'name': name,
                          'canonicalId': created['canonicalId'], 'elapsedSeconds': round(time.monotonic()-started, 2)}), flush=True)
        if completed >= limit:
            break
    report = c.validate(ROOT)
    (A / 'creation-validation.json').write_bytes(c.encoded(report))
    print(json.dumps({'batchCreated': completed, 'persistedGroupings': len(ledger),
                      'elements': report['elements'], 'relationships': report['relationships'],
                      'validation': report['status']}), flush=True)


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--limit', type=int, default=15)
    args = parser.parse_args()
    assert args.limit > 0
    execute(args.limit)
