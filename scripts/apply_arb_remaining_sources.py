#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Apply the explicit ARB catalogue/matrix correspondence; never infer instances.

This bounded migration adapter is separate from ordinary publication. Repeated
application preserves element and relationship identities and contributions.
"""
from collections import Counter
import copy
from pathlib import Path
import subprocess
import uuid

import architecture_continuum as c

EVIDENCE = Path('enterprise-architecture/design/evidence/1.1.5/002')
PLAN = EVIDENCE / 'before/arb-remaining-source-plan.json'


def apply(root=c.ROOT):
    plan = c.read_json(root / PLAN)
    _, elements = c.load(root)
    sources = {}
    for row in plan['catalogue'] + plan['responsibilityAssertions'] + plan['nativeRelationships']:
        if row['source'] not in sources:
            sources[row['source']] = subprocess.check_output(
                ['git', 'show', plan['entryRevision'] + ':' + row['source']], cwd=root, text=True)
        # G2 text is an explicit bounded interpretation of the cited decision;
        # table contributions retain the exact reviewed source row.
        if 'row ' in row['selector']:
            c.require(row['sourceText'] in sources[row['source']], 'Reviewed row changed: ' + row['selector'])
    for row in plan['catalogue'] + plan['responsibilityAssertions']:
        cid = row.get('canonicalId')
        if not cid:
            continue
        m, body, path = elements[cid]
        binding = {'source': row['source'], 'selector': row['selector']}
        provenance = {**binding, 'revision': plan['entryRevision'], 'role': row['disposition'],
                      'authority': plan['authority'], 'standing': row['standing']}
        if binding not in m['sourceBindings']:
            m['sourceBindings'].append(binding)
            m['provenance'].append(provenance)
            # Tables need their headers to render; quote their full literal row.
            body += '\n## ARB source correspondence\n\n' + row['reason'] + '\n\n'
            body += 'Source: `' + row['source'] + '` — ' + row['selector'] + '. Standing: ' + row['standing'] + '.\n\n'
            body += '```text\n' + row['sourceText'] + '\n```\n'
        if row.get('alias'):
            aliases = m.setdefault('aliases', [])
            alias = {'value': row['alias'], 'role': 'Candidate catalogue navigation code; not identity authority',
                     'source': row['source'], 'authority': plan['authority']}
            if alias not in aliases:
                aliases.append(alias)
        elements[cid] = m, body, path
    matrix = c.read_json(root / c.CONFIG / 'native-relationships.json')
    for row in plan['nativeRelationships']:
        a, b = row['sourceCanonicalId'], row['targetCanonicalId']
        m, body, path = elements[a]
        target = elements[b][0]
        c.require(matrix['keys'][row['archimateType']] in matrix['endpoints'].get(m['archimateType'], {}).get(target['archimateType'], ''), 'Illegal matrix endpoints')
        if row['name'] == 'serves capability context':
            c.require(matrix['keys']['Serving'] not in matrix['endpoints'][m['archimateType']][target['archimateType']], 'Association fallback is not justified')
        rid = 'id-' + uuid.uuid5(c.SEMANTIC_NAMESPACE, 'arb-matrix:' + row['source'] + ':' + row['selector']).hex
        relation = {'identifier': rid, 'archimateType': row['archimateType'],
                    'source': m['exchange']['identifier'], 'target': target['exchange']['identifier'],
                    'attributes': {'isDirected': 'true'} if row['archimateType'] == 'Association' else {},
                    'name': [{'text': row['name'], 'attributes': {c.LANG: 'en'}}],
                    'documentation': [{'text': row['reason'] + '\nSource assertion: ' + row['sourceText'], 'attributes': {c.LANG: 'en'}}],
                    'standing': copy.deepcopy(row['standing']),
                    'provenance': {'source': row['source'], 'selector': row['selector'],
                                   'revision': plan['entryRevision'], 'authority': plan['authority']}, 'properties': []}
        old = next((r for r in m['relationships'] if r['identifier'] == rid), None)
        if old:
            c.require(old == relation, 'Conflicting matrix interpretation: ' + rid)
        else:
            m['relationships'].append(relation)
        row['relationshipIdentifier'] = rid
        elements[a] = m, body, path
    for m, body, path in elements.values():
        content = c.encode(m, body)
        if path.read_text() != content:
            path.write_text(content)
    plan['counts'] = {'catalogueRows': len(plan['catalogue']),
                      'responsibilityRows': len(plan['responsibilityAssertions']),
                      'nativeRelationships': dict(Counter(r['archimateType'] for r in plan['nativeRelationships'])),
                      'allocatedElementIdentities': 0}
    (root / EVIDENCE / 'after/arb-remaining-source-dispositions.json').write_bytes(c.encoded(plan))
    c.validate(root)
    return plan


if __name__ == '__main__':
    print(apply()['counts'])
