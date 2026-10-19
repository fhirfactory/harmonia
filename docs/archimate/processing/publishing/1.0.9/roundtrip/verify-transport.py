#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only reproduction of this release's bounded Archi transport assertions.

This is evidence inspection, not a comparator equivalence or reconciliation rule.
"""
from collections import Counter
import copy
import gzip
import json
from pathlib import Path
import sys
import tempfile

HERE = Path(__file__).resolve().parent
ROOT = next(p for p in HERE.parents if (p / 'architecture-release.json').is_file())
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import diagnose_roundtrip as d
import home_relationships as h
import publish_navigation as nav


def signature(element, model, properties=True):
    result = copy.deepcopy(element)
    if properties:
        for prop in result.findall('.//a:property', d.g.N):
            prop.set('propertyDefinitionRef', model.keys[prop.get('propertyDefinitionRef')])
    else:
        for prop in result.findall('a:properties', d.g.N):
            result.remove(prop)
    return d.g.signature(result)


def main():
    saved = json.loads((HERE / 'archi-transport-verification.json').read_text())
    before = d.Exchange(ROOT / 'docs/archimate/exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml')
    with tempfile.TemporaryDirectory(prefix='harmonia-rad109-transport-', dir='/tmp') as temp:
        incoming = Path(temp) / 'export.xml'
        incoming.write_bytes(gzip.decompress((HERE / 'Archi-no-edit-export.xml.gz').read_bytes()))
        after = d.Exchange(incoming)
    assert before.sha256 == saved['published_sha256'] and after.sha256 == saved['exported_sha256']
    assert not before.issues and not after.issues, (before.issues, after.issues)
    for role in d.ROLES:
        claims = []
        for model in (before, after):
            claims.append({identifier: [d.values(p, 'value') for p in model.claims(owner, role)]
                           for identifier, owner in model.elements.items() if model.claims(owner, role)})
        assert claims[0] == claims[1], role
    for name in ('elements', 'relationships'):
        left, right = getattr(before, name), getattr(after, name)
        assert left.keys() == right.keys(), name
        for identifier in left:
            assert signature(left[identifier], before, False) == signature(right[identifier], after, False), identifier
    homes = [{r['identifier']: r for r in h.authored_homes(model)} for model in (before, after)]
    assert homes[0] == homes[1] and len(homes[0]) == 212
    capture = json.loads((ROOT / 'docs/markdown/governance/baseline/accepted-ingestion-1.0.8.json').read_text())
    for name in ('elements', 'relationships', 'views'):
        for row in capture[name]:
            identifier = row['representationId']
            assert signature(getattr(before, name)[identifier], before) == signature(getattr(after, name)[identifier], after), identifier
    assert before.views.keys() == after.views.keys()
    objects = Counter()
    for identifier, view in before.views.items():
        target = after.views[identifier]
        assert view.attrib == target.attrib and d.values(view, 'name') == d.values(target, 'name'), identifier
        for tag in ('node', 'connection'):
            left = {n.get('identifier'): n for n in view.findall('.//a:' + tag, d.g.N)}
            right = {n.get('identifier'): n for n in target.findall('.//a:' + tag, d.g.N)}
            assert left.keys() == right.keys()
            for key, item in left.items():
                other = right[key]
                assert item.attrib == other.attrib and item.getparent().get('identifier') == other.getparent().get('identifier'), key
                if tag == 'node':
                    assert [n.get('identifier') for n in item.findall('a:node', d.g.N)] == [n.get('identifier') for n in other.findall('a:node', d.g.N)]
                else:
                    assert d.g.signature(item) == d.g.signature(other), key
            objects[tag] += len(left)
        assert [n.get('identifier') for n in view.findall('a:node', d.g.N)] == [n.get('identifier') for n in target.findall('a:node', d.g.N)]
    assert objects == {'node': 3923, 'connection': 1473}
    assert nav.navigation_homes(before.root) == nav.navigation_homes(after.root)
    raw = json.loads(gzip.decompress((HERE / 'raw-comparator.json.gz').read_bytes()))
    historical = json.loads((ROOT / '.junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-fidelity.json').read_text())
    def semantic(probe):
        return Counter(d.encoded(f) for f in probe['findings'] if f['classification'] == d.SEMANTIC)
    assert semantic(raw) == semantic(historical) and sum(semantic(raw).values()) == 1669
    assert raw['comparison_status'] == 'DIFFERENCES' and raw['classifications'] == saved['raw_classifications']
    print(json.dumps(dict(status='PASS', published_sha256=before.sha256, exported_sha256=after.sha256,
                          identity_and_naming_properties='EXACT', native_non_property_content='EXACT',
                          home_assertions=212, accepted_elements_relationships_and_views='EXACT',
                          all_view_geometry_and_connections='EXACT', navigation_homes='EXACT',
                          unchanged_historical_semantic_text_findings=1669,
                          raw_comparator_status='DIFFERENCES', human_rendering='PENDING INSPECTION'), indent=2))


if __name__ == '__main__':
    main()
