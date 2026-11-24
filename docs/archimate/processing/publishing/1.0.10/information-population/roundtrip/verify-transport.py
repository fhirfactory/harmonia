#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Inspect retained actual Archi no-edit transport; no comparator normalisation."""
import copy
from collections import Counter
import gzip
import json
from pathlib import Path
import sys
import tempfile

HERE = Path(__file__).resolve().parent
ROOT = next(p for p in HERE.parents if (p / 'architecture-release.json').is_file())
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import diagnose_roundtrip as d
import information_population as ia
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


def folders(root):
    result = set()
    def walk(parent, path=()):
        for item in parent.findall('a:item', d.g.N):
            if not item.get('identifierRef'):
                current = (*path, item.findtext('a:label', namespaces=d.g.N))
                result.add(current)
                walk(item, current)
    walk(root.find('a:organizations', d.g.N))
    return result


def main():
    before = d.Exchange(ia.m.model_path())
    with tempfile.TemporaryDirectory(prefix='harmonia-ia-transport-', dir='/tmp') as temp:
        path = Path(temp) / 'export.xml'
        path.write_bytes(gzip.decompress((HERE / 'Archi-no-edit-export.xml.gz').read_bytes()))
        after = d.Exchange(path)
    assert not before.issues and not after.issues, (before.issues, after.issues)
    roles = {}
    for role in d.ROLES:
        claims = [{key: [d.values(p, 'value') for p in model.claims(e, role)]
                   for key, e in model.elements.items() if model.claims(e, role)} for model in (before, after)]
        assert claims[0] == claims[1], role
        roles[role] = len(claims[0])
    for name in ('elements', 'relationships'):
        old, new = getattr(before, name), getattr(after, name)
        assert old.keys() == new.keys(), name
        assert all(signature(e, before, False) == signature(new[key], after, False) for key, e in old.items()), name
    assert before.views.keys() == after.views.keys()
    for key, view in before.views.items():
        other = after.views[key]
        assert view.attrib == other.attrib and d.values(view, 'name') == d.values(other, 'name'), key
        for kind in ('node', 'connection'):
            old = {e.get('identifier'): e for e in view.findall('.//a:'+kind, d.g.N)}
            new = {e.get('identifier'): e for e in other.findall('.//a:'+kind, d.g.N)}
            assert old.keys() == new.keys()
            for identifier, item in old.items():
                target = new[identifier]
                assert item.attrib == target.attrib and item.getparent().get('identifier') == target.getparent().get('identifier')
                if kind == 'connection':
                    assert d.g.signature(item) == d.g.signature(target)
                else:
                    assert [e.get('identifier') for e in item.findall('a:node', d.g.N)] == [e.get('identifier') for e in target.findall('a:node', d.g.N)]
    assert nav.navigation_homes(before.root) == nav.navigation_homes(after.root), 'Principal resource navigation'
    assert folders(before.root) == folders(after.root), 'Folder locations, including empty IA/DataMap folders'
    raw = json.loads(gzip.decompress((HERE / 'raw-comparator.json.gz').read_bytes()))
    historical = json.loads(gzip.decompress((ROOT / 'docs/archimate/processing/publishing/1.0.9/containment-scope-completion/roundtrip/raw-comparator.json.gz').read_bytes()))
    def semantic(probe):
        return Counter(json.dumps(f, sort_keys=True) for f in probe['findings'] if f['classification'] == d.SEMANTIC)
    assert semantic(raw) == semantic(historical) and sum(semantic(raw).values()) == 1669
    result = dict(status='PASS', scope='Actual installed Archi 5.10 no-edit full-model import/export; no human rendering or architect-authored IA reconciliation assertion',
        published_sha256=before.sha256, exported_sha256=after.sha256,
        elements=len(before.elements), relationships=len(before.relationships), views=len(before.views),
        exact_contract_properties=roles, metadata_stops='54 canonicalName absences preserved exactly',
        semantic_non_property_content='EXACT', existing_view_geometry_membership_and_connections='EXACT',
        resource_and_view_navigation_including_empty_maps='EXACT',
        historical_semantic_findings='1669; EXACT same findings as originating RAD-1.0.9 transport',
        raw_comparator_status=raw['comparison_status'], raw_comparator_classifications=raw['classifications'])
    saved = HERE / 'archi-transport-verification.json'
    if saved.exists():
        assert result == json.loads(saved.read_text()), 'Changed transport assertion/evidence'
    print(json.dumps(result, indent=2))


if __name__ == '__main__':
    main()
