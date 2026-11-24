#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only assertions on final Run-002 bytes and the archived preceding state."""
import gzip
import hashlib
import json
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[8]
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import generate_views as g
import workspace_navigation as nav


def sha(data):
    return hashlib.sha256(data).hexdigest()


def main():
    run = Path(__file__).resolve().parents[2]
    archive = run / 'validation/pre-contained-leaf-correction'
    before = json.loads((archive / 'manifest.json').read_bytes())['files']

    def original(path):
        row = before[str(path.relative_to(ROOT))]
        data = gzip.decompress((archive / row['snapshot']).read_bytes())
        assert sha(data) == row['sha256'], path
        return data

    for path in before:
        original(ROOT / path)
    history_archives = {}
    for path in sorted((run / 'validation').glob('pre-*/manifest.json')):
        rows = json.loads(path.read_bytes())['files']
        for relative, row in rows.items():
            data = gzip.decompress((path.parent / row['snapshot']).read_bytes())
            assert sha(data) == row['sha256'], (path, relative)
        history_archives[path.parent.name] = len(rows)
    raw = (run / 'candidate.xml').read_bytes()
    current = g.ET.fromstring(raw)
    previous = g.ET.fromstring(original(run / 'candidate.xml'))
    manifest = json.loads((run / 'manifest.json').read_bytes())
    for path, expected in manifest['files_sha256'].items():
        assert sha((run / path).read_bytes()) == expected, path
    for path, expected in manifest['source_sha256'].items():
        data = (ROOT / path).read_bytes()
        assert sha(data) == expected, path
        assert gzip.decompress((run / ('machinery/' + path + '.gz')).read_bytes()) == data, path
    for path, expected in manifest['frozen_files_before'].items():
        assert sha((ROOT / path).read_bytes()) == expected, path
    assert manifest['frozen_files_before'] == manifest['frozen_files_after']
    assert nav.protected(previous) == nav.protected(current)
    branches = {}
    for label in ('Relations', 'Views'):
        xpath = "a:organizations/a:item[a:label=$label]"
        old, = previous.xpath(xpath, namespaces=g.N, label=label)
        new, = current.xpath(xpath, namespaces=g.N, label=label)
        assert g.signature(old) == g.signature(new), label
        assert g.ET.tostring(old) == g.ET.tostring(new), label
        branches[label] = dict(status='EXACT_MATCH', signature_sha256=g.digest(g.signature(new)),
                               serialized_subtree_sha256=sha(g.ET.tostring(new)))
    old_views = previous.find('a:views', g.N)
    views = current.find('a:views', g.N)
    assert g.signature(old_views) == g.signature(views)
    assert g.ET.tostring(old_views) == g.ET.tostring(views)
    plan = json.loads((run / 'workspace-projection.json').read_bytes())
    old_plan = json.loads(original(run / 'workspace-projection.json'))
    assert old_plan['relationship_plan'] == plan['relationship_plan']
    for name in ('resolved-containment.json', 'proposed-metadata.json', 'representation-rebindings.json'):
        assert original(run / name) == (run / name).read_bytes(), name
    for path in ('docs/archimate/framework/tools/containment.py',
                 'docs/archimate/framework/tools/relationship_state.py',
                 'docs/archimate/framework/tools/workspace_policy.py'):
        assert original(ROOT / path) == (ROOT / path).read_bytes(), path

    elements = nav.index(current, 'a:elements/a:element')
    names = {n.findtext('a:name', namespaces=g.N): rid for rid, n in elements.items()}
    # Direct XML structure assertions; a plan or merely having two locations
    # cannot satisfy this acceptance proof.
    care, = current.xpath("a:organizations/a:item[a:label='Application']/a:item[a:label='InformationMap']/a:item[a:label='[Topic] Care']", namespaces=g.N)
    assert care.xpath('a:item/@identifierRef', namespaces=g.N) == [names['[Topic] Care']]
    encounter, = care.xpath("a:item[a:label='[Topic] Encounter']", namespaces=g.N)
    assert encounter.xpath('a:item/@identifierRef', namespaces=g.N) == [names['[Topic] Encounter']]
    assert (Path(__file__).parent / 'care-context.xml').read_bytes() == g.ET.tostring(
        care, pretty_print=True, encoding='UTF-8', xml_declaration=True, with_tail=False)
    homes = nav.locations(current)
    parents = {row['composition_path'][-2] for row in plan['element_plan'].values() if len(row['composition_path']) > 1}
    participants = {rid for rid, row in plan['element_plan'].items() if len(row['composition_path']) > 1} | parents
    for rid in participants:
        folder = homes[rid][0][0][-1]
        assert folder.findtext('a:label', namespaces=g.N) == elements[rid].findtext('a:name', namespaces=g.N), rid
        assert folder.xpath('a:item/@identifierRef', namespaces=g.N) == [rid], rid
    org = current.find('a:organizations', g.N)
    element_branches = [n for n in org if n.findtext('a:label', namespaces=g.N) not in ('Relations', 'Views')]
    folder_count = sum(len(n.xpath('descendant-or-self::a:item[not(@identifierRef)]', namespaces=g.N)) for n in element_branches)
    reference_count = sum(len(n.xpath('descendant-or-self::a:item[@identifierRef]', namespaces=g.N)) for n in element_branches)
    proof = json.loads((run / 'element-generation-proof.json').read_bytes())
    assert proof['status'] == 'PASS'
    assert [r['input_element_relationship_navigation'] for r in proof['cases']] == ['ABSENT', 'MISLEADING']
    assert all(r['regenerated_organisation'] == 'EXACT_MATCH' for r in proof['cases'])
    assert json.loads((ROOT / 'architecture-release.json').read_bytes()) == {'version':'1.0.10', 'previous_version':'1.0.9'}
    assert not (run.parent / 'run-003').exists()
    assert not (ROOT / 'docs/archimate/exchange/publish/1.1.0').exists()
    assert not (ROOT / 'docs/archimate/processing/publishing/1.1.0').exists()
    print(json.dumps(dict(status='PASS', candidate_xml_sha256=sha(raw), candidate_bytes=len(raw),
        final_serialized_care_encounter=dict(status='PASS',
            care_folder=care.get('identifier'), care_reference=names['[Topic] Care'],
            encounter_folder=encounter.get('identifier'), encounter_reference=names['[Topic] Encounter'],
            care_direct_references=care.xpath('a:item/@identifierRef', namespaces=g.N),
            encounter_direct_references=encounter.xpath('a:item/@identifierRef', namespaces=g.N)),
        element_navigation=dict(total_folders=folder_count, references=reference_count,
            containment_participants=len(participants), contained_leaf_folders=len(participants - parents),
            context_and_role_folders=plan['summary']['element_context_folders']),
        relationship_navigation=branches['Relations'], view_navigation=branches['Views'],
        views=dict(count=len(views.findall('a:diagrams/a:view', g.N)), status='EXACT_MATCH',
            signature_sha256=g.digest(g.signature(views)), serialized_subtree_sha256=sha(g.ET.tostring(views))),
        containment_resolver_standing_and_collection_policy='BYTE_IDENTICAL',
        naming_types_specialisation_identities_and_protected_content='UNCHANGED',
        metadata_containment_and_rebinding_evidence='BYTE_IDENTICAL',
        counterfactuals=proof['cases'], verified_before_archive_files=len(before),
        history_archives=history_archives,
        generated_outputs=len(manifest['files_sha256']), machinery_sources=len(manifest['source_sha256']),
        frozen_files=len(manifest['frozen_files_before']), release_created=False,
        archi_import='PENDING_SOLUTION_ARCHITECT', architect_acceptance='NOT_RECORDED'), indent=2))


if __name__ == '__main__':
    main()
