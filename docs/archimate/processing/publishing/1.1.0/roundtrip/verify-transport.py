#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Verify retained actual Archi transport separately from raw XML fidelity."""
from collections import Counter
import gzip
import hashlib
import json
from pathlib import Path
import sys

HERE = Path(__file__).resolve().parent
ROOT = next(p for p in HERE.parents if (p / 'architecture-release.json').exists())
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
from reconciliation import information as r
import publish_information_release as release


def navigation_inventory(model):
    folders, sibling_order, identifiers = Counter(), {}, set()
    def walk(parent, path=()):
        tokens = []
        for item in parent.findall('a:item', r.g.N):
            labels = tuple(x.text or '' for x in item.findall('a:label', r.g.N))
            current = path + labels
            rid = item.get('identifierRef')
            tokens.append(('reference', rid, labels) if rid else ('folder', labels))
            if item.get('identifier'):
                identifiers.add(item.get('identifier'))
            if not rid:
                folders[current] += 1
            walk(item, current)
        if tokens:
            assert path not in sibling_order
            sibling_order[path] = tokens
    walk(model.root.find('a:organizations', r.g.N))
    return folders, sibling_order, identifiers


def main():
    before = r.diagnostic.Exchange(release.MODEL)
    after = r.diagnostic.Exchange(HERE / 'Archi-no-edit-export.xml.gz')
    execution = json.loads((HERE / 'execution.json').read_bytes())
    assert execution['status'] == 'PASS' and execution['exit_code'] == 0
    assert execution['input_sha256'] == before.sha256
    assert hashlib.sha256((HERE / 'archi-cli.log').read_bytes()).hexdigest() == execution['log_sha256']
    assert not before.issues and not after.issues, (before.issues, after.issues)
    assert before.root.attrib == after.root.attrib
    for tag in ('name', 'documentation'):
        assert r.diagnostic.values(before.root, tag) == r.diagnostic.values(after.root, tag)
    for name in ('elements', 'relationships', 'views'):
        assert getattr(before, name).keys() == getattr(after, name).keys(), name
    exact_properties = {}
    for role in r.diagnostic.ROLES:
        def claims(model):
            return {rid: [r.diagnostic.values(p, 'value') for p in model.claims(node, role)]
                    for rid, node in model.elements.items() if model.claims(node, role)}
        assert claims(before) == claims(after), role
        exact_properties[role] = len(claims(before))
    for rid in before.elements:
        assert r.review.authored_element(before, rid) == r.review.authored_element(after, rid), rid
    for rid in before.relationships:
        assert r.relationship_content(before, rid) == r.relationship_content(after, rid), rid
    assert r.review.signature(before.root.find('a:views', r.g.N), before) == r.review.signature(
        after.root.find('a:views', r.g.N), after), 'Complete authored View content'
    def properties(model):
        return {model.keys[node.get('propertyDefinitionRef')]: r.review.signature(node, model)
                for node in model.root.findall('a:properties/a:property', r.g.N)}
    assert properties(before) == properties(after), 'Model properties'
    assert before.single(before.root, 'Baseline / Architecture Release') == '1.1.0'
    assert after.single(after.root, 'Baseline / Architecture Release') == '1.1.0'
    assert r.review.navigation(before) == r.review.navigation(after), 'Reference paths'
    old, new = navigation_inventory(before), navigation_inventory(after)
    assert old[0] == new[0], 'Folder paths including empty folders'
    assert old[1].keys() == new[1].keys(), 'Navigation contexts'
    reordered = []
    for path in sorted(old[1]):
        assert Counter(old[1][path]) == Counter(new[1][path]), 'Navigation membership'
        if old[1][path] != new[1][path]:
            reordered.append(list(path))
    effective = r.containment.resolve(after.root)
    assert not effective.findings()
    locations = {node.get('identifierRef'): node for node in
                 after.root.findall('a:organizations//a:item', r.g.N) if node.get('identifierRef')}
    participants = {edge[key] for edge in effective.evidence()['selected_relationships'].values()
                    for key in ('parent', 'child')}
    for rid in participants:
        folder = locations[rid].getparent()
        assert folder.findtext('a:label', namespaces=r.g.N) == after.elements[rid].findtext('a:name', namespaces=r.g.N)
        assert folder.xpath('a:item/@identifierRef', namespaces=r.g.N) == [rid]
    names = r.information_naming.propose(after, effective)
    assert not any(row['changes'] for row in names['records'])
    assert len([row for row in names['records'] if row['unresolved']]) == 4
    raw = r.diagnostic.compare(before, after)
    raw_bytes = r.encoded(raw)
    summary = dict(status='PASS', scope=execution['scope'],
        published_sha256=before.sha256, exported_xml_sha256=after.xml_sha256,
        export_gzip_sha256=after.sha256, elements=len(after.elements),
        relationships=len(after.relationships), views=len(after.views),
        exact_contract_properties=exact_properties, element_authored_content='EXACT',
        relationship_content='EXACT', complete_authored_view_content='EXACT',
        canonical_identity_and_owners='EXACT_ALL_1394_INCLUDING_32_NEW_IDS',
        reference_paths='EXACT', folder_paths='EXACT', containment_self_folders=len(participants),
        held_classifications='FOUR_UNCHANGED_UNRESOLVED_DATAOBJECTS',
        navigation_item_ids_lost=len(old[2] - new[2]), sibling_contexts_reordered=len(reordered),
        reordered_contexts=reordered, raw_comparator_status=raw['comparison_status'],
        raw_comparator_classifications=raw['classifications'], whole_xml_lossless=False,
        architectural_changes_introduced_by_transport=[],
        human_visual_acceptance='PENDING_SOLUTION_ARCHITECT')
    if '--record' in sys.argv:
        assert not (HERE / 'archi-transport-verification.json').exists(), 'Transport assertion already recorded'
        (HERE / 'raw-comparator.json.gz').write_bytes(gzip.compress(raw_bytes, mtime=0))
        (HERE / 'archi-transport-verification.json').write_bytes(r.encoded(summary))
    else:
        assert gzip.decompress((HERE / 'raw-comparator.json.gz').read_bytes()) == raw_bytes
        assert json.loads((HERE / 'archi-transport-verification.json').read_bytes()) == summary
    print(r.encoded(summary).decode(), end='')


if __name__ == '__main__':
    main()
