#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Purpose-selected semantic correspondence for the authorised replacement."""
from investigate import *

AUTHORITY = 'enterprise-architecture/design/evidence/1.1.20/001/before/CapabilityReplacementTask.md'
HCRL = 'enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5'

def definitions(cid):
    return SNAPSHOT[cid]['body'].split('## Architectural content\n', 1)[1].split('**Overview**')[0].split('\n## ')[0].strip()

def prepare():
    selections = {}
    for line in (OUT / 'authoring/semantic-selections.txt').read_text().splitlines():
        if not line.strip() or line.startswith('#'): continue
        prefix, *codes = line.split()
        matches = [cid for cid in OLD if cid.startswith(prefix)]
        assert len(matches) == 1, (prefix, matches)
        assert matches[0] not in selections, prefix
        assert codes and all(code in TARGET or code == '-' for code in codes), (prefix, codes)
        selections[matches[0]] = [] if codes == ['-'] else codes
    byrid = {x['metadata']['exchange']['identifier']: cid for cid, x in SNAPSHOT.items()}
    incoming = collections.defaultdict(list)
    neighbors = collections.defaultdict(list)
    for cid, x in SNAPSHOT.items():
        for r in x['metadata']['relationships']:
            target = byrid.get(r['target'])
            if target not in OLD: continue
            if r['archimateType'] == 'Composition' and cid in OLD:
                incoming[target].append(cid)
            if cid not in OLD: neighbors[target].append(cid)
    retrieval = Retrieval()
    ledger = {}
    for cid in sorted(selections):
        codes = selections[cid]
        m = OLD[cid]['metadata']
        ledger[cid] = {'oldCanonicalId': cid, 'oldName': m['displayName'], 'oldDefinition': definitions(cid),
            'targetCatalogueIds': codes, 'method': 'explicit contextual semantic selection',
            'disposition': 'replacement-scope-correspondence' if codes else 'retired-without-equivalent',
            'rationale': ('Selected accepted responsibilities overlap the source definition and its governed information/coordination context. Multiple targets preserve separated contributions; no complete semantic equivalence, implementation satisfaction or transferred domain authority is asserted.' if codes else 'Corporate publicity and general external communications have no legitimate successor in this health-information/care capability model. Population risk communication and clinical collaboration have materially different purposes; retain the source without manufacturing a relationship.'),
            'sourceParentCanonicalIds': incoming[cid], 'relatedBusinessCanonicalIds': neighbors[cid]}
    for cid, x in OLD.items():
        if cid in ledger or x['metadata']['identity'].get('correspondence'): continue
        m = x['metadata']
        assert m.get('specialization') == 'Feature', ('Unselected Capability', cid, m['displayName'])
        parents = [p for p in incoming[cid] if p in ledger]
        name = m.get('friendlyName') or ''
        qualified = [p for p, px in OLD.items() if p in ledger and px['metadata'].get('friendlyName') and name.startswith(px['metadata']['friendlyName'] + '.')]
        if qualified:
            longest = max(len(OLD[p]['metadata']['friendlyName']) for p in qualified)
            parents = [p for p in qualified if len(OLD[p]['metadata']['friendlyName']) == longest]
        allowed = sorted({code for p in parents for code in ledger[p]['targetCatalogueIds']})
        query = old_text(cid)
        for n in neighbors[cid]:
            query += ' ' + SNAPSHOT[n]['metadata']['displayName'] + ' ' + definitions(n)[:350]
        suggestions = retrieval.search(query, level=4, allowed=allowed or None)
        assert suggestions and suggestions[0]['score'] > 0
        choice = suggestions[0]['id']
        ledger[cid] = {'oldCanonicalId': cid, 'oldName': m['displayName'], 'oldDefinition': definitions(cid),
            'targetCatalogueIds': [choice], 'method': 'definition/context inference within selected predecessor responsibility',
            'disposition': 'replacement-scope-correspondence', 'sourceParentCanonicalIds': parents,
            'relatedBusinessCanonicalIds': neighbors[cid], 'alternativesInvestigated': suggestions,
            'rationale': 'Compare the Feature definition, recorded parent/naming ancestry and related Business behaviour with accepted outcome definitions inside the selected replacement responsibility. Select the most specific supported outcome; original scope and authority remain with the preserved source. Correspondence is partial where scopes differ.'}
    def historical(cid, trail=()):
        if cid in ledger: return ledger[cid]
        assert cid not in trail
        m = OLD[cid]['metadata']; prior = m['identity']['correspondence']; current = historical(prior['successorCanonicalId'], trail + (cid,))
        ledger[cid] = {'oldCanonicalId': cid, 'oldName': m['displayName'], 'oldDefinition': definitions(cid),
            'targetCatalogueIds': list(current['targetCatalogueIds']), 'method': 'preserved historical correspondence through explicitly mapped successor',
            'disposition': 'historical-correspondence-carried', 'priorCorrespondence': prior,
            'rationale': 'Preserve the accepted historical-to-current correspondence as history and follow that current resource into its replacement scope. No historical UUID or exact definition is discarded and no complete equivalence to the new model is inferred.'}
        return ledger[cid]
    for cid in OLD: historical(cid)
    for row in ledger.values():
        row['targetDefinitions'] = [{'id': code, 'name': TARGET[code]['name'], 'definition': TARGET[code]['description']} for code in row['targetCatalogueIds']]
        row['authority'] = AUTHORITY
    assert len(ledger) == len(OLD)
    return ledger

if __name__ == '__main__':
    ledger = prepare()
    (OUT / 'semantic-correspondence.json').write_bytes(c.encoded(list(ledger.values())))
    print('MAPPED', len(ledger), 'RETIRED WITHOUT SUCCESSOR', sum(not x['targetCatalogueIds'] for x in ledger.values()))
    for cid, x in sorted(OLD.items(), key=lambda p:p[1]['metadata']['displayName']):
        if x['metadata'].get('specialization') == 'Feature':
            row=ledger[cid]
            print(cid[:8], row['oldName'], '=>', ', '.join(code + ' ' + TARGET[code]['name'] for code in row['targetCatalogueIds']))
