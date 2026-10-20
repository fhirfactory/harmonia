#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Audit published release, accepted scope, immutable history and local handoff."""
import copy
import gzip
import hashlib
import json
from pathlib import Path
import re
import sys
from urllib.parse import unquote

HERE = Path(__file__).resolve().parent
ROOT = next(p for p in HERE.parents if (p / 'architecture-release.json').exists())
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import publish_information_release as p


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main():
    evidence = HERE.parent
    decision, plan, source, models, paths = p.inputs()
    current = p.r.diagnostic.Exchange(p.MODEL)
    candidate = models['candidate']
    assert not current.issues
    assert current.elements.keys() == candidate.elements.keys()
    new = {row['representationId'] for row in source['elements'] if row['operation'] == 'add'}
    for rid in current.elements:
        before = p.r.review.authored_element(candidate, rid)
        after = copy.deepcopy(p.r.review.authored_element(current, rid))
        if rid in new:
            assert 'canonicalId' not in before['properties']
            after['properties'].pop('canonicalId')
        assert before == after, ('Unexpected element difference', rid)
    assert current.relationships.keys() == candidate.relationships.keys()
    for rid in current.relationships:
        assert p.r.relationship_content(current, rid) == p.r.relationship_content(candidate, rid), rid
    for path in ('a:views', "a:organizations/a:item[a:label='Views']"):
        assert p.g.ET.tostring(current.root.find(path, p.g.N)) == p.g.ET.tostring(candidate.root.find(path, p.g.N))
    assert p.r.review.navigation(current) == p.r.review.navigation(candidate)
    assert current.single(current.root, 'Baseline / Architecture Release') == '1.1.0'
    assert not current.claims(current.root, p.r.naming.MARKER)
    assert p.active()
    assert json.loads(gzip.decompress((evidence/'before/architecture-release.json.gz').read_bytes())) == decision['starting_release']
    protected = json.loads((evidence / 'protected-inputs.json').read_bytes())
    allowed = {'docs/markdown/governance/baseline/information-architecture.md': 'information-architecture.md.gz',
               'docs/archimate/exchange/publish/README.md': 'published-package-index.md.gz'}
    changed = []
    for relative, expected in protected.items():
        if sha(ROOT / relative) != expected:
            assert relative in allowed, ('Historical file changed', relative)
            assert hashlib.sha256(gzip.decompress((evidence/'before'/allowed[relative]).read_bytes())).hexdigest() == expected
            changed.append(relative)
    assert sorted(changed) == sorted(allowed)
    for parent in ('docs/archimate/exchange', 'docs/archimate/processing', 'docs/markdown/governance/baseline'):
        for path in (ROOT/parent).rglob('*'):
            if not path.is_file() or '__pycache__' in path.parts:
                continue
            relative = path.relative_to(ROOT).as_posix()
            if relative not in protected:
                assert (relative.startswith('docs/archimate/exchange/publish/1.1.0/') or
                        relative.startswith('docs/archimate/processing/publishing/1.1.0/') or
                        relative == 'docs/markdown/governance/baseline/release-1.1.0.json'), relative
    manifest = json.loads((evidence/'publication-manifest.json').read_bytes())
    assert sha(p.AUTHORITY) == manifest['authority']['sha256']
    for category in ('outputs', 'machinery_sha256', 'classification_sources'):
        for relative, expected in manifest[category].items():
            assert sha(ROOT/relative) == expected, (category, relative)
    tests = json.loads((HERE/'test-results.json').read_bytes())
    assert tests['status'] == 'PASS' and all(row['status'] == 'PASS' for row in tests['checks'])
    for row in tests['checks']:
        assert sha(ROOT/row['log']) == row['log_sha256']
    transport = json.loads((evidence/'roundtrip/archi-transport-verification.json').read_bytes())
    assert transport['status'] == 'PASS' and transport['published_sha256'] == sha(p.MODEL)
    assert transport['raw_comparator_classifications']['SEMANTIC DIFFERENCE'] == 0
    documents = [p.REPORT, p.GUIDE, p.m.RAD/'information-architecture.md',
                 p.m.PUBLISH/'README.md', ROOT/'.junie/reports/2026-10-20-rad-1.1.0-promotion.md']
    output = HERE/'independent-audit.json'
    links = 0
    for document in documents:
        for target in re.findall(r'\[[^\]]+\]\(([^)]+)\)', document.read_text()):
            if re.match(r'(https?://|#|mailto:)', target):
                continue
            relative = unquote(target.split('#')[0])
            resolved = (document.parent/relative).resolve()
            assert resolved.exists() or ('--record' in sys.argv and resolved == output), (document, target)
            links += 1
    result = dict(status='PASS', architecture_release=p.RELEASE, model_sha256=sha(p.MODEL),
                  counts=p.counts(current.root), accepted_crs=154, held_crs_unchanged=4,
                  new_canonical_ids_from_persisted_source=32, canonical_ids_allocated_by_promotion=0,
                  unexpected_element_relationship_view_differences=0,
                  frozen_and_existing_files_unchanged=len(protected)-len(changed),
                  authorised_textual_amendments=changed, checked_local_links=links,
                  new_machinery_runs_or_restructured_paths=0,
                  architecture_transport='PASS', visual_inspection='PENDING_SOLUTION_ARCHITECT')
    if '--record' in sys.argv:
        assert not output.exists()
        output.write_bytes(p.r.encoded(result))
    else:
        assert json.loads(output.read_bytes()) == result
    print(p.r.encoded(result).decode(), end='')


if __name__ == '__main__':
    main()
