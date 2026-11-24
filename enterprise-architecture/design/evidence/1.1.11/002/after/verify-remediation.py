#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded evidence check for the explicitly authorised 1.1.11 transition.

Not a semantic acceptance mechanism. Reads the exact retained 1.1.10 publication,
this iteration's input inventory and current canonical/derived representations.
Run from the repository root with the declared EA dependencies installed.
"""
import collections
import gzip
import json
from pathlib import Path
import sys
sys.path.insert(0, str(Path(__file__).resolve().parents[5] / 'tooling'))
# The script belongs to evidence, not routine generation or architectural authority.
sys.path.insert(0, str(Path.cwd() / 'enterprise-architecture/tooling'))
import architecture_continuum as c
import continuum_navigation as nav
from lxml import html

BASE = Path('enterprise-architecture/design/evidence/1.1.11/002')
PUB = Path('enterprise-architecture/documentation/continuum-manifest/published')
RAD = Path('enterprise-architecture/documentation/continuum-rad/published/1.1.11')


def main():
    before = c.read_json(BASE / 'before/source-state.json')
    elements = c.load()[1]
    tree = nav.load(elements=elements)
    locations = nav.by_element(tree)
    nodes = list(nav.walk(tree))
    allowed = {str(c.MANIFEST), str(nav.SOURCE)}
    preserved = {p: c.sha(Path(p).read_bytes()) == sha for p, sha in before['sha256'].items() if p not in allowed}
    assert all(preserved.values()), [p for p, ok in preserved.items() if not ok]
    assert sorted(elements) == before['canonicalIds']
    relationships = {r['identifier']: r for m, _, _ in elements.values() for r in m['relationships']}
    assert len(relationships) == before['relationshipCount']
    assert c.sha(c.encoded(relationships)) == before['relationshipSha256']
    old = c.secure_xml(PUB / '1.1.10/Harmonia-Architecture.xml')
    new = c.secure_xml(PUB / '1.1.11/Harmonia-Architecture.xml')
    for path in ['a:relationships', 'a:views']:
        assert c.ET.tostring(old.find(path, c.N), method='c14n') == c.ET.tostring(new.find(path, c.N), method='c14n'), path
    oldtree = c.read_json(PUB / '1.1.10/navigation.json')['roots']
    oldlocations = nav.by_element(oldtree)
    changes = [{'canonicalId': cid, 'displayName': elements[cid][0]['displayName'],
                'beforeNumber': oldlocations[cid]['number'], 'beforePath': oldlocations[cid]['path'],
                'afterNumber': node['number'], 'afterPath': node['path'], 'occurrence': node['occurrence']}
               for cid, node in locations.items()]
    (BASE / 'after/navigation-correspondence.json').write_bytes(c.encoded(changes))
    history = []
    for cid, (m, _, _) in elements.items():
        if m['identity'].get('correspondence'):
            successor = m['identity']['correspondence']['successorCanonicalId']
            assert locations[cid]['number'].startswith('R.2.')
            assert locations[successor]['number'].startswith('II.')
            assert locations[cid]['successorReference']['canonicalId'] == successor
            assert cid in [r['canonicalId'] for r in locations[successor]['historicalReferences']]
            history.append({'historicalCanonicalId': cid, 'successorCanonicalId': successor,
                            'historicalCoordinate': locations[cid]['number'], 'successorCoordinate': locations[successor]['number'],
                            'historicalName': m['displayName'], 'successorName': elements[successor][0]['displayName'],
                            'correspondence': m['identity']['correspondence']})
    assert len(history) == 214
    assert sum(h['historicalName'] == h['successorName'] for h in history) == 212
    assert locations['330d1c43856a4322a2e9f331b1a5ed20']['successorReference']['canonicalId'] == 'a61a15908ed544e6acf3e9cab63a0267'
    assert 'Business Capabilities' in locations['baad6b6117084311acbf5fc4044dfabf']['path']
    assert 'Business Enabling Capabilities' in locations['a61a15908ed544e6acf3e9cab63a0267']['path']
    (BASE / 'after/capability-history.json').write_bytes(c.encoded(history))
    contexts = [n for n in nodes if n.get('contextRole') == 'capability']
    oldcontexts = [n for n in nav.walk(oldtree) if n.get('contextCanonicalId')]
    oldids = {n['contextCanonicalId'] for n in oldcontexts}
    assert len(contexts) == 89 and len(oldids) == 72
    contextmap = {n['contextCanonicalId']: n for n in contexts}
    for original in oldcontexts:
        current = contextmap[original['contextCanonicalId']]
        assert set(nav.descendants(original)) == set(nav.descendants({**current, 'children': [ch for ch in current['children'] if not ch.get('contextCanonicalId')]}))
    chains = {}
    def visit(node, path=()):
        if node.get('canonicalId') and elements[node['canonicalId']][0]['archimateType'] == 'Capability':
            path += (node['canonicalId'],)
            chains[node['canonicalId']] = path
        for ch in node.get('children', []): visit(ch, path)
    visit(tree[2])
    contextpaths = []
    def visitcontext(node, path=()):
        if node.get('contextRole') == 'capability':
            path += (node['contextCanonicalId'],)
            assert path == chains[node['contextCanonicalId']]
            assert nav.descendants(node)
            r = node['canonicalReference']
            assert r['canonicalId'] == node['contextCanonicalId'] and r['number'] == locations[r['canonicalId']]['number']
            contextpaths.append({'number': node['number'], 'path': node['path'], 'canonicalIdPath': list(path),
                                 'canonicalReference': r, 'originalPopulatedContext': node['contextCanonicalId'] in oldids})
        for ch in node.get('children', []): visitcontext(ch, path)
    visitcontext(tree[3])
    (BASE / 'after/business-contexts.json').write_bytes(c.encoded(contextpaths))
    behaviour = next(n for n in nodes if n['path'] == ['Business', 'Business Behaviour'])
    behaviourcounts = collections.Counter(elements[cid][0]['archimateType'] for cid in nav.descendants(behaviour))
    assert behaviourcounts == {'BusinessService': 142, 'BusinessFunction': 160, 'BusinessProcess': 21}
    stakeholders = {cid for cid, (m, _, _) in elements.items() if m['archimateType'] == 'Stakeholder'}
    assert set(nav.descendants(tree[-1]['children'][0])) == stakeholders and len(stakeholders) == 10
    assert len(tree[1]['children']) == 8
    actual = [(n['associatedCanonicalId'], child['contextCanonicalId'], edge)
              for n in nodes if n.get('associatedCanonicalId') for child in n['children'] for edge in child['relationshipIds']]
    expected = [(concern, stakeholder, edge) for concern, related in nav.stakeholder_associations(elements).items()
                for stakeholder, ids in related.items() for edge in ids]
    assert sorted(actual) == sorted(expected) and len(actual) == 4
    motivation = (RAD / 'parts/motivation.tex').read_text()
    for _, _, edge in actual: assert edge in motivation
    (BASE / 'after/stakeholder-contexts.json').write_bytes(c.encoded({'associations': actual, 'indexCanonicalIds': sorted(stakeholders)}))
    assert len({n['number'] for n in nodes}) == len(nodes)
    assert sum(n['number'] == 'I.6' for n in nodes) == 1
    manifesttree = c.read_json(PUB / '1.1.11/navigation.json')['roots']
    assert manifesttree == tree
    structure = c.read_json(RAD / 'structure.json')
    assert [h['number'] for h in structure['headings']] == [n['number'] for n in nodes]
    browser = html.fromstring((PUB / '1.1.11/continuum.html').read_bytes(), parser=html.HTMLParser(encoding='utf-8', huge_tree=True))
    assert browser.xpath('//nav[@id="hierarchy"]//li/@data-number') == [n['number'] for n in nodes]
    org = {n.get('identifier'): n for n in new.findall('.//a:organizations//a:item', c.N)}
    for node in nodes:
        item = org[nav.transport_id(node)]
        assert item.findtext('a:label', namespaces=c.N) == nav.title(node)
        for role, r in nav.references(node):
            note = item.findtext('a:documentation', namespaces=c.N)
            assert role in note and r['canonicalId'] in note and r['number'] in note
            if node.get('contextCanonicalId'): assert item.get('identifierRef') is None
    returned = c.ET.fromstring(gzip.decompress((BASE / 'after/archi-transport/returned-model.xml.gz').read_bytes()))
    new_elements = {el.get('identifier'): el for el in new.findall('a:elements/a:element', c.N)}
    returned_elements = {el.get('identifier'): el for el in returned.findall('a:elements/a:element', c.N)}
    for rid, el in new_elements.items():
        projected = el.findtext('a:properties/a:property[@propertyDefinitionRef="continuum-navigation-references"]/a:value', namespaces=c.N)
        transported = returned_elements[rid].findtext('a:properties/a:property[@propertyDefinitionRef="continuum-navigation-references"]/a:value', namespaces=c.N)
        # Archi rebinds propertyDefinition IDs; compare by definition names.
        if transported is None:
            keys = {d.get('identifier'): d.findtext('a:name', namespaces=c.N) for d in returned.findall('a:propertyDefinitions/a:propertyDefinition', c.N)}
            transported = next(p.findtext('a:value', namespaces=c.N) for p in returned_elements[rid].findall('a:properties/a:property', c.N)
                               if keys[p.get('propertyDefinitionRef')] == 'Continuum / Navigation References')
        assert json.loads(projected) == json.loads(transported)
    summary = {'status': 'PASS', 'canonicalIdentitiesPreserved': len(elements), 'canonicalElementBytesUnchanged': len(elements),
               'semanticRelationshipsPreserved': len(relationships), 'authoredViewsPreserved': len(new.findall('a:views/a:diagrams/a:view', c.N)),
               'protectedInputFilesUnchanged': len(preserved), 'historicalCorrespondencesPreserved': len(history),
               'sameNameHistoricalSuccessorGroups': 212, 'exceptionalDistinctBcBecCase': 'PASS',
               'capabilityContexts': len(contexts), 'originalPopulatedContexts': len(oldids), 'requiredAncestorsAdded': 17,
               'behaviourCounts': dict(behaviourcounts), 'stakeholderIndex': len(stakeholders), 'existingStakeholderAssociations': len(actual),
               'inventedStakeholderAssociations': 0, 'navigationNodes': len(nodes), 'uniqueCoordinates': len(nodes),
               'elementCoordinateChanges': sum(row['beforeNumber'] != row['afterNumber'] for row in changes),
               'elementPathChanges': sum(row['beforePath'] != row['afterPath'] for row in changes),
               'sharedProjectionNavigation': 'PASS', 'derivedNavigationPropertiesPreservedInArchi': len(new_elements),
               'reportedExtraI6': 'NOT_REPRODUCED; no remediation',
               'limits': 'Bounded preservation/navigation verification and transport metadata check. No human acceptance or exhaustive semantic review.'}
    (BASE / 'after/verification.json').write_bytes(c.encoded(summary))
    print(json.dumps(summary, indent=2))

if __name__ == '__main__': main()
