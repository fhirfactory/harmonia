#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only source/history, complete-model and current review verification."""
import collections
import gzip
import hashlib
import json
from pathlib import Path
import re
import sys
from urllib.parse import unquote
import uuid

EX = Path(__file__).resolve().parent.parent
RUN = EX.parents[1]
ROOT = next(p for p in EX.parents if (p / 'architecture-release.json').is_file())
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import change_review
import containment
import diagnose_roundtrip as d
import element_correlation
import generate_views as g
import information_naming
import workspace_navigation as nav


def sha(data):
    return hashlib.sha256(data).hexdigest()


def read(path):
    return json.loads(path.read_bytes())


def main(*, preflight=False):
    original = read(RUN / 'validation/review-manifest.json')
    prior = EX / 'before/change-review-revision-001.md'
    for name, digest in original['evidence_sha256'].items():
        target = prior if name == 'change-review.md' else RUN / name
        assert sha(target.read_bytes()) == digest, name
    for section in ('protected_sha256', 'machinery_sha256'):
        for name, digest in original[section].items():
            assert sha((ROOT / name).read_bytes()) == digest, name
    before = read(EX / 'before/manifest.json')
    changed = []
    for name, row in before['files'].items():
        snapshot = gzip.decompress((EX / 'before' / row['snapshot']).read_bytes())
        assert sha(snapshot) == row['sha256'], ('before archive', name)
        if (ROOT / name).read_bytes() != snapshot:
            changed.append(name)
    assert changed == [str((RUN / 'change-review.md').relative_to(ROOT))], changed
    if not preflight:
        manifest = read(EX / 'manifest.json')
        for name, digest in manifest['files_sha256'].items():
            assert sha((ROOT / name).read_bytes()) == digest, name
    plan = read(EX / 'plan.json')
    for name, row in plan['inputs'].items():
        assert sha((ROOT / row['path']).read_bytes()) == row['sha256'], name
    for name in ('decision', 'review'):
        row = plan['authority'][name]
        assert sha((ROOT / row['path']).read_bytes()) == row['sha256'], name
    source = read(ROOT / plan['source_path'])
    established = read(EX / 'artifacts/source-manifest.json')
    assert sha((ROOT / plan['source_path']).read_bytes()) == established['source_sha256']
    assert sha((EX / 'plan.json').read_bytes()) == established['plan_sha256']
    assert source['authority'] == established['instructions'] == plan['authority']
    assert read(ROOT / 'architecture-release.json') == plan['release_authority'] == {
        'version': '1.0.10', 'previous_version': '1.0.9'}

    candidate = d.Exchange(ROOT / plan['inputs']['candidate']['path'])
    baseline = d.Exchange(ROOT / plan['inputs']['baseline']['path'])
    final = d.Exchange(EX / 'artifacts/reconciled.xml')
    assert not candidate.issues and not baseline.issues and not final.issues
    assert final.elements.keys() == candidate.elements.keys()
    a = element_correlation.canonical_index(candidate)
    b = element_correlation.canonical_index(final)
    assert not a['issues'] and not b['issues']
    assert len(b['unique']) == len(final.elements) == 1394
    new = set()
    for rid, claim in a['claims'].items():
        if claim['present']:
            assert b['claims'][rid] == claim, rid
        else:
            new.add(rid)
            value = uuid.UUID(b['claims'][rid]['canonicalId'])
            assert value.version == 4 and value.variant == uuid.RFC_4122, rid
    assert len(new) == source['allocation']['count'] == 32
    corr = element_correlation.correlate(baseline, final)
    assert corr['status'] == 'CORRELATED' and not corr['deleted']
    assert len(corr['matches']) == 1362 and len(corr['representation_rebindings']) == 38
    assert set(corr['new']) == new
    baseline_ids = element_correlation.canonical_index(baseline)['unique']
    original_information = read(ROOT / 'docs/markdown/governance/baseline/information-architecture.json')
    assert len(original_information['elements']) == 54
    for row in original_information['elements']:
        old = baseline.elements[baseline_ids[row['canonicalId']]]
        assert old.get('identifier') == row['representationId']
        for key in ('canonicalId', 'displayName', 'friendlyName', 'canonicalName', 'archimateType'):
            assert baseline.single(old, key) == row[key], (row['representationId'], key)

    accepted = {row['representationId']: row for row in source['elements']}
    assert len(accepted) == len(source['elements']) == 82
    for rid in final.elements.keys() - accepted.keys():
        assert g.signature(candidate.elements[rid]) == g.signature(final.elements[rid]), rid
    for rid, row in accepted.items():
        if row['operation'] != 'add':
            previous = row['previous']
            old = baseline.elements[baseline_ids[row['canonicalId']]]
            assert previous['canonicalId'] == row['canonicalId']
            assert previous['representationId'] == old.get('identifier')
            assert previous['archimateType'] == change_review.native_type(old)
            for key in ('displayName', 'friendlyName', 'canonicalName'):
                assert previous[key] == baseline.single(old, key), (rid, key)
        left = change_review.authored_element(candidate, rid)
        right = change_review.authored_element(final, rid)
        for item in (left, right):
            item['properties'] = {key: value for key, value in item['properties'].items()
                                  if key not in ('canonicalId', 'displayName', 'friendlyName',
                                                 'canonicalName', 'archimateType', 'harmoniaType')}
        assert left == right, rid
        assert final.single(final.elements[rid], 'canonicalId') == row['canonicalId']
    assert final.relationships.keys() == candidate.relationships.keys()
    assert all(g.signature(node) == g.signature(final.relationships[rid])
               for rid, node in candidate.relationships.items())
    assert len(final.relationships) == 1137 and len(source['relationships']) == 60
    assert collections.Counter(change_review.native_type(g.ET.fromstring(row['xml'].encode()))
                               for row in source['relationships']) == {'Composition': 59, 'Aggregation': 1}
    for path in ('a:views', "a:organizations/a:item[a:label='Views']"):
        assert g.ET.tostring(candidate.root.find(path, g.N)) == g.ET.tostring(final.root.find(path, g.N))
    assert len(final.views) == 1209 and len(source['views']) == 12

    resolved = containment.resolve(final.root, accepted=[row['representationId'] for row in source['relationships']])
    assert not resolved.findings()
    proposals = information_naming.propose(final, resolved)
    assert len(proposals['records']) == 86 and not proposals['identity_issues']
    names = {row['representation_id']: row for row in proposals['records']}
    for rid, row in accepted.items():
        assert not names[rid]['unresolved']
        for key, value in names[rid]['proposed'].items():
            assert row[key] == value
            assert final.single(final.elements[rid], key) == value
        assert final.elements[rid].findtext('a:name', namespaces=g.N) == row['displayName']
    assert {row['representation_id'] for row in proposals['records'] if row['unresolved']} == {
        row['id'] for row in source['held']}
    paths = resolved.paths()
    participants = {rid for rid, path in paths.items() if len(path) > 1}
    participants.update(path[-2] for path in paths.values() if len(path) > 1)
    assert len(participants) == 993
    locations = nav.locations(final.root)
    for rid in participants:
        folder = locations[rid][0][0][-1]
        assert folder.findtext('a:label', namespaces=g.N) == final.elements[rid].findtext('a:name', namespaces=g.N)
        assert folder.xpath('a:item[@identifierRef=$rid]/@identifierRef', namespaces=g.N, rid=rid) == [rid]
    for rid, node in final.relationships.items():
        source_id = node.get('source')
        expected = nav.labels(locations[source_id][0][0])
        if source_id not in participants:
            expected += [final.elements[source_id].findtext('a:name', namespaces=g.N)]
        assert nav.labels(locations[rid][0][0]) == ['Relations', *expected], rid
    care, = final.root.xpath("a:organizations/a:item[a:label='Application']/a:item[a:label='InformationMap']/a:item[a:label='[Topic] Care']", namespaces=g.N)
    encounter, = care.xpath("a:item[a:label='[Topic] Encounter']", namespaces=g.N)
    assert len(care.xpath('a:item/@identifierRef', namespaces=g.N)) == 1
    assert len(encounter.xpath('a:item/@identifierRef', namespaces=g.N)) == 1

    index = read(RUN / 'detection/change-request-index.json')
    current = (RUN / 'change-review.md').read_text()
    instructions = (EX / 'reviewed-instructions.md').read_text()
    records = read(EX / 'processing-records.json')['records']
    assert len(index) == len(records) == 158
    bycr = {row['id']: row for row in records}
    for item in index:
        cid = item['id']
        body = current.split('## ' + cid + ' — ', 1)[1].split('\n## ', 1)[0]
        accepted_cr = int(cid[3:]) <= 154
        disposition, outcome = ('Accept', 'Resolved') if accepted_cr else ('On-Hold', 'Unresolved')
        assert f'**Disposition:** {disposition}' in body
        assert f'**Outcome:** {outcome}' in body
        assert f'| [{cid}](#{cid.lower()}) | {item["brief"]} | {disposition} | {outcome} |' in current
        assert bycr[cid]['disposition'] == disposition and bycr[cid]['outcome'] == outcome
        if accepted_cr:
            issued = instructions.split('## ' + cid + ' — ', 1)[1].split('\n## ', 1)[0]
            assert body.split('### Authorised Reconciliation Instruction', 1)[1].split('### Reconciliation / Processing Record', 1)[0] == issued.split('### Authorised Reconciliation Instruction', 1)[1].split('### Reconciliation / Processing Record', 1)[0]
        else:
            previous = prior.read_text().split('## ' + cid + ' — ', 1)[1].split('\n## ', 1)[0]
            # The final CR's following review-wide footer is excluded by split.
            assert body.strip() == previous.strip(), cid
    links = 0
    docs = [RUN / 'change-review.md', EX / 'reviewed-instructions.md', EX / 'results.md',
            ROOT / 'docs/markdown/governance/baseline/README.md',
            ROOT / 'docs/markdown/governance/baseline/information-architecture.md',
            ROOT / 'docs/archimate/framework/tools/reconciliation/README.md']
    for doc in docs:
        for target in re.findall(r'\]\(([^)]+)\)', doc.read_text()):
            if '://' in target:
                continue
            path, _, fragment = target.partition('#')
            # Immutable instruction text uses review-relative links by design.
            base = RUN if doc.name == 'reviewed-instructions.md' else doc.parent
            resolved_path = (base / unquote(path)).resolve() if path else doc
            assert resolved_path.exists(), (doc.name, target)
            if not path and fragment.startswith('cr-'):
                assert f'<a id="{fragment}"></a>' in doc.read_text(), (doc.name, fragment)
            links += 1
    print(json.dumps(dict(status='PASS', phase='PREFLIGHT_NO_CURRENT_MANIFEST' if preflight else 'FINAL', identities=1394, existing_ids_retained=1362,
          new_uuidv4_ids=32, representation_rebindings=38, accepted_elements=82,
          relationships_preserved=1137, accepted_relationships=60, authored_views_preserved=1209,
          accepted_view_changes=12, exact_serialized_view_navigation=True,
          containment_self_folders=993, unchanged_held=4, resolved_crs=154,
          local_links=links, protected_before_files=len(before['files']),
          release='1.0.10', release_created=False, mutations=0), sort_keys=True))


if __name__ == '__main__':
    import argparse
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--preflight', action='store_true', help='Skip only the not-yet-issued current execution manifest')
    main(preflight=parser.parse_args().preflight)
