#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only integrity and document checks for this pinned review evidence."""
import hashlib
import json
from pathlib import Path
import re
from urllib.parse import unquote

RUN = Path(__file__).resolve().parent.parent
ROOT = RUN.parents[5]
sha = lambda p: hashlib.sha256(p.read_bytes()).hexdigest()


def main():
    manifest = json.loads((RUN / 'validation/review-manifest.json').read_bytes())
    for name, expected in manifest['evidence_sha256'].items():
        path = (RUN / name).resolve()
        assert path.is_relative_to(RUN), name
        assert sha(path) == expected, name
    for name, expected in manifest['protected_sha256'].items():
        assert sha(ROOT / name) == expected, name
    for name, expected in manifest['machinery_sha256'].items():
        assert sha(ROOT / name) == expected, name
    assert json.loads((ROOT / 'architecture-release.json').read_bytes()) == {
        'version': '1.0.10', 'previous_version': '1.0.9'}
    index = json.loads((RUN / 'detection/change-request-index.json').read_bytes())
    text = (RUN / 'change-review.md').read_text()
    assert text.startswith('| ID | Brief Description | Disposition | Outcome |\n')
    ids = [row['id'] for row in index]
    assert ids == [f'CR-{i:03}' for i in range(1, 159)]
    for row in index:
        cid, brief = row['id'], row['brief']
        assert len(brief) <= 80, cid
        assert f'| [{cid}](#{cid.lower()}) | {brief} | On-Hold | Unresolved |' in text
        assert text.count(f'<a id="{cid.lower()}"></a>') == 1
        body = text.split(f'## {cid} — ', 1)[1].split('\n## ', 1)[0]
        for phrase in ('**Disposition:** On-Hold', '**Outcome:** Unresolved',
                       '**Outcome Reason:**', '### Proposed Architectural Change',
                       '### Detected Evidence', '### Architectural Questions / Adjudication',
                       '**Solution Architect Decision:** Not recorded',
                       '### Related Change Requests', '### Authorised Reconciliation Instruction',
                       '**Not authorised.**', '### Reconciliation / Processing Record',
                       '**Not yet processed.**'):
            assert phrase in body, (cid, phrase)
        assert all(dependency in ids for dependency in row['related']), cid
    links = 0
    for doc in (RUN / 'change-review.md', RUN / 'roundtrip-results.md'):
        content = doc.read_text()
        for target in re.findall(r'\]\(([^)]+)\)', content):
            if '://' in target:
                continue
            path, _, fragment = target.partition('#')
            resolved = (doc.parent / unquote(path)).resolve() if path else doc
            assert resolved.exists(), (doc.name, target)
            if not path and fragment.startswith('cr-'):
                assert f'<a id="{fragment}"></a>' in content, fragment
            links += 1
    print(json.dumps(dict(status='PASS', change_requests=len(ids), local_links=links,
                          evidence_files=len(manifest['evidence_sha256']),
                          protected_files=len(manifest['protected_sha256']),
                          machinery_files=len(manifest['machinery_sha256']),
                          release='1.0.10', mutations=0), sort_keys=True))


if __name__ == '__main__':
    main()
