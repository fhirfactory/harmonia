#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Execute the five-element approval with governed creation and preserved IDs."""
import copy
import json
from pathlib import Path
import re
import sys

ROOT = Path(__file__).resolve().parents[7]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c
from governed_naming import component

B = Path('enterprise-architecture/design/evidence/1.1.18/001')
A = ROOT / B / 'after/complete-views'
SOURCE = str(B / 'before/Harmonia-Continuum-1.1.18-001-004.xml')
TASK = str(B / 'before/ai-agent-tasks/20261102T011114Z-complete-subject-views.md')
AUTHORITY = 'enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md#dr-13'
PREDECESSOR = 'c20aa17ce4aa4a58be5eb3cff2ecdd1d'


def execute():
    rows = c.read_json(ROOT / B / 'after/subject-hierarchy/non-grouping-exceptions.json')
    assert len(rows) == 5
    ledger_path = A / 'five-element-results.json'
    ledger = c.read_json(ledger_path) if ledger_path.exists() else []
    source_hash = c.sha((ROOT / SOURCE).read_bytes())
    task_hash = c.sha((ROOT / TASK).read_bytes())
    for row in rows:
        _, existing = c.load(ROOT)
        bound = [(cid, m, body, path) for cid, (m, body, path) in existing.items()
                 if any(b['source'] == SOURCE and b['selector'] == row['selector'] for b in m['sourceBindings'])]
        assert len(bound) <= 1
        native = row['nativeType']
        spec = {'Principle': 'ArchitecturalAxiom', 'Constraint': 'Mandate', 'Stakeholder': None}[native]
        name = ('[' + spec + '] ' if spec else '') + row['name']
        if bound:
            cid, m, body, path = bound[0]
            assert m['archimateType'] == native and m['specialization'] == spec
            initial = m['exchange']['identifier']
        else:
            # Source definitions and the composite split were reviewed in DR-13.
            # Exact-name collisions are a stop, never permission to merge IDs.
            normal = lambda value: re.sub(r'[^a-z0-9]', '', value.lower())
            assert not any(normal(m['displayName']) == normal(name) for m, _, _ in existing.values())
            m = c.template(native, spec, ROOT)['metadata']
            friendly = component(row['name'])
            m.update(displayName=name, friendlyName=friendly, canonicalName='MOT-' + friendly,
                standing={'semantic': 'Solution Architect explicitly approved inclusion; incorporated-result/visual review pending',
                          'currentness': 'current'},
                identity={'authority': AUTHORITY, 'approvalTask': TASK},
                naming={'authority': 'enterprise-architecture/governance/canonical-identity-and-naming.md',
                        'mode': 'Approved local Motivation naming; native specialisation display prefix retained',
                        'inputs': [{'key': row['exchangeId'], 'type': native, 'name': row['name']}],
                        'expected': {'displayName': name, 'friendlyName': friendly, 'canonicalName': 'MOT-' + friendly}},
                provenance=[{'source': SOURCE, 'selector': row['selector'], 'retainedAt': SOURCE,
                             'sha256': source_hash, 'role': 'Authored native definition, approved by the operative Task'},
                            {'source': TASK, 'selector': 'Approved five architectural elements / ' + row['name'],
                             'retainedAt': TASK, 'sha256': task_hash,
                             'role': 'Explicit Solution Architect inclusion and necessary migration authority'}],
                sourceBindings=[{'source': SOURCE, 'selector': row['selector']}])
            definition = row['sourceDocumentation'].split('\n\n')[1]
            lineage = ''
            if native == 'Stakeholder':
                lineage = f'''
### Evolution from the combined stakeholder

This approved distinct actor population is one result of separating the historical
[Platform Operations & Integration Engineers](../stakeholder/{PREDECESSOR}.md)
population. The predecessor retains canonical UUID `{PREDECESSOR}`, its Exchange
binding, original definition and existing references. Neither distinct successor
is asserted to be identical to that combined population. The split is preserved
as attributable source/decision provenance and these reciprocal explanations;
it is not forced into the singular one-to-one identity.correspondence field.
No predecessor relationship or architectural responsibility is automatically
duplicated or retargeted. View membership does not select a new Home.
'''
                m['provenance'].append({'source': str(Path('enterprise-architecture/continuum/motivation/stakeholder') / (PREDECESSOR + '.md')),
                    'selector': 'Combined stakeholder profile and approved separation history', 'maintained': True,
                    'role': 'Retained historical predecessor population; no one-to-one identity equivalence'})
            body = f'''# {name}

## Architectural content

### Approved definition

{definition}

The Solution Architect's complete Subject/Views Task explicitly approves this
native {native}{(' / ' + spec) if spec else ''} for the governed Continuum.
The incoming candidate/pending-ADD caption records its earlier source standing;
it does not override this subsequent inclusion approval. Original source wording,
native type, specialisation and Exchange identity evidence remain recoverable in
the retained immutable 004 export. This incorporation supplies neither HCRL closure
nor actual Archi visual acceptance.

This element retains its own bounded meaning. Broader existing security,
authorisation, messaging or stakeholder concerns do not establish identity
equivalence. No unrelated incoming properties, relationships, classifications,
Capability allocations or OpenSpec candidates are incorporated.
{lineage}'''
            parent = ['Cross-Cutting References', 'Stakeholder Index'] if native == 'Stakeholder' else ['Motivation', 'Principles' if native == 'Principle' else 'Constraints']
            path = c.create({'metadata': m, 'body': body, 'navigationParent': parent}, ROOT)
            m, body = c.decode(path.read_text())
            cid = m['canonicalId']
            initial = m['exchange']['identifier']
        if not any(r['exchangeId'] == row['exchangeId'] for r in ledger):
            m['identity']['initialCreateRepresentationId'] = initial
            m['identity']['incomingRepresentationBinding'] = row['exchangeId']
            m['exchange']['identifier'] = row['exchangeId']
            m['exchange']['previousNativeName'] = row['name']
            path.write_text(c.encode(m, body))
            ledger.append(dict(authoredName=row['name'], displayName=name, nativeType=native,
                specialization=spec, canonicalId=cid, exchangeId=row['exchangeId'],
                source=str(path.relative_to(ROOT)), operation='SPLIT_SUCCESSOR_CREATED' if native == 'Stakeholder' else 'NEW_APPROVED_ELEMENT_CREATED',
                predecessorCanonicalId=PREDECESSOR if native == 'Stakeholder' else None,
                identityBasis='Distinct approved population after explicit composite separation' if native == 'Stakeholder' else 'No existing equivalent instance established by source/name/definition review',
                initialCreateRepresentationId=initial))
            ledger_path.write_bytes(c.encoded(ledger))
        else:
            assert m['exchange']['identifier'] == row['exchangeId']
        print(json.dumps(ledger[-1]), flush=True)
    # Preserve the combined population as history; do not select one of two
    # distinct successors for the existing singular correspondence protocol.
    _, all_elements = c.load(ROOT)
    old, body, path = all_elements[PREDECESSOR]
    before, before_body = c.decode((ROOT / B / 'before/complete-views/combined-stakeholder.md').read_text())
    successors = [r for r in ledger if r['nativeType']=='Stakeholder']
    assert len(successors) == 2
    old = copy.deepcopy(before)
    old['standing']['currentness'] = 'superseded'
    old['standing']['evolution'] = 'Approved separation into distinct integration/development and platform operations/OA&M populations; original combined identity reserved as history.'
    old['provenance'].append({'source': TASK, 'selector': 'Five approved elements / necessary stakeholder migration',
        'retainedAt': TASK, 'sha256': task_hash,
        'role': 'Authorised one-to-many population separation; original UUID/definition/relationships retained, no equivalence or retargeting'})
    for prop in old['exchange']['properties']:
        if prop['ref'] == 'propid-40':
            for value in prop['values']:
                value['text'] = 'superseded'
    body = before_body + '\n\n### Approved stakeholder separation — 1.1.18 / 001\n\n'
    body += 'The original profile above is retained as the historical combined population. The Solution Architect approved its separation into two distinct actor populations through the complete Subject/Views Task. Its canonical UUID and Exchange ID are reserved and unchanged. Neither successor is assigned the predecessor identity.\n\n'
    for row in successors:
        body += f"- [{row['displayName']}]({row['canonicalId']}.md), canonical UUID `{row['canonicalId']}`.\n"
    body += '\nExisting references and relationships remain attached to the historical combined population. No relationship is duplicated or retargeted by the separation. The one-to-many evolution is recorded by these reciprocal explanations and attributable provenance, rather than an invented one-to-one equivalence.\n'
    path.write_text(c.encode(old, body))
    save = {'kind': 'APPROVED_COMBINED_POPULATION_SEPARATION', 'predecessorCanonicalId': PREDECESSOR,
        'predecessorExchangeId': old['exchange']['identifier'], 'currentness': 'superseded',
        'successors': successors, 'originalDefinitionRetainedUnchanged': body.startswith(before_body),
        'predecessorRelationshipsUnchanged': old['relationships']==before['relationships'],
        'relationshipRetargeting': False, 'singularCorrespondenceInvented': False}
    (A / 'stakeholder-evolution.json').write_bytes(c.encoded(save))
    (A / 'incorporation-validation.json').write_bytes(c.encoded(c.validate(ROOT)))


if __name__ == '__main__':
    execute()
