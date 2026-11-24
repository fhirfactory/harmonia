#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Reproduce the candidate using archived machinery in an isolated temporary tree.

Run from the repository root. Frozen specimens are copied after hash checks. No
live processing module is imported. Temporary output is compared and discarded.
The inherited Run-001 absolute provenance locators remain in original evidence;
rebinding comparison here checks identities independently of temporary paths.
"""
import gzip
import hashlib
import json
from pathlib import Path
import subprocess
import sys
import tempfile


def sha(data):
    return hashlib.sha256(data).hexdigest()


def main():
    repository = Path.cwd()
    evidence = Path(__file__).resolve().parent.parent
    manifest = json.loads((evidence / 'manifest.json').read_bytes())
    dependencies = json.loads((evidence / 'validation/replay-dependencies.json').read_bytes())
    with tempfile.TemporaryDirectory(prefix='harmonia-run002-replay-', dir='/tmp') as temporary:
        root = Path(temporary)
        for relative, expected in manifest['source_sha256'].items():
            data = gzip.decompress((evidence / 'machinery' / (relative + '.gz')).read_bytes())
            assert sha(data) == expected, relative
            path = root / relative
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(data)
        for relative, expected in dependencies.items():
            data = gzip.decompress((evidence / 'validation/dependencies' / (relative + '.gz')).read_bytes())
            assert sha(data) == expected, relative
            path = root / relative
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(data)
        for relative, expected in manifest['frozen_files_before'].items():
            data = (repository / relative).read_bytes()
            assert sha(data) == expected, relative
            path = root / relative
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(data)
        for row in manifest['inputs'].values():
            data = (repository / row['path']).read_bytes()
            assert sha(data) == row['sha256'], row['path']
            path = root / row['path']
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(data)
        code = '''
import json, pathlib, sys
sys.path.insert(0, 'docs/archimate/framework/tools')
import information_naming_run as n
import workspace_navigation as w
import containment
import workspace_policy
_, models = n.load_inputs()
incoming = models['incoming']
resolved = containment.resolve(incoming.root)
proposal = n.naming.propose(incoming, resolved=resolved)
named = n.candidate(incoming, proposal, 'run-002')
records, collection_sources = workspace_policy.load_records()
placements = workspace_policy.placements(incoming, resolved, records)
root, plan = w.project(incoming, n.g.ET.fromstring(named), resolved=resolved, placements=placements)
content = n.g.serialize(root)
n.g.load_schema(n.g.BASE / 'schemas/archimate3_Diagram.xsd').assertValid(n.g.ET.fromstring(content))
w.verify(incoming, n.g.ET.fromstring(named), n.g.ET.fromstring(content), plan, resolved=resolved, placements=placements)
proof = w.prove_generation(incoming, n.g.ET.fromstring(named), n.g.ET.fromstring(content), placements, resolved)
plan['collection_sources_sha256'] = collection_sources
pathlib.Path('proof.json').write_bytes(n.encoded(proof))
pathlib.Path('containment.json').write_bytes(n.encoded(resolved.evidence()))
pathlib.Path('reproduced.xml').write_bytes(content)
pathlib.Path('metadata.json').write_bytes(n.encoded(proposal))
pathlib.Path('plan.json').write_bytes(n.encoded(plan))
review = n.change_review.review(models['baseline'], incoming, models['control'])
pathlib.Path('rebindings.json').write_bytes(n.encoded(review['representation_rebindings']))
'''
        subprocess.run([sys.executable, '-c', code], cwd=root, check=True, timeout=90)
        for generated, original in (('reproduced.xml', 'candidate.xml'),
                                     ('metadata.json', 'proposed-metadata.json'),
                                     ('plan.json', 'workspace-projection.json'),
                                     ('proof.json', 'element-generation-proof.json'),
                                     ('containment.json', 'resolved-containment.json')):
            assert (root / generated).read_bytes() == (evidence / original).read_bytes(), original
        fields = ('canonicalId', 'baseline_id', 'incoming_id', 'basis', 'representation_identity')
        def bindings(path):
            return [{key: row[key] for key in fields} for row in json.loads(path.read_bytes())]
        assert bindings(root / 'rebindings.json') == bindings(evidence / 'representation-rebindings.json')
        print(json.dumps(dict(status='PASS', machinery='ARCHIVED_SOURCES_ONLY',
                              isolated_candidate_xml='BYTE_IDENTICAL', metadata='BYTE_IDENTICAL',
                              navigation_evidence='BYTE_IDENTICAL', containment_evidence='BYTE_IDENTICAL',
                              element_generation_from_absent_and_misleading_folders='PASS',
                              representation_rebindings=38,
                              candidate_xml_sha256=sha((root / 'reproduced.xml').read_bytes())), indent=2))
    return 0


if __name__ == '__main__':
    sys.exit(main())
