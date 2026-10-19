#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Verify the authorised 1.0.14 namespace correction without refreshing history."""
import contextlib
import gzip
import hashlib
import io
import json
from pathlib import Path
import runpy
import sys
from types import SimpleNamespace

import workspace_paths as w

AUDIT = w.GOVERNANCE_TOOLING / 'migrations/2026-10-19-reclassify-1.0.14'


def require(condition, message):
    if not condition:
        raise ValueError(message)


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def verify_manifest(path, root=w.ROOT):
    data = json.loads(path.read_text())
    require(data['classification'] == 'governance-tooling-evidence' and
            data['rad_architecture_release'] is None, 'Evidence is not explicitly non-RAD')
    target = root / 'docs/archimate/governance-tooling' / data['governance_tooling_version']
    require(path.parent == target, 'Evidence manifest is outside its declared namespace')
    for row in data['files']:
        original, current = root / row['original_path'], root / row['path']
        require(current.is_relative_to(target), 'Evidence target escapes its namespace')
        require(not original.exists() and not original.is_symlink(), 'Original publication path still exists')
        require(not current.is_symlink() and current.stat().st_size == row['bytes'] and
                sha(current) == row['sha256'], 'Relocated evidence changed: ' + row['path'])
        require(row['original_link_base'] == str(Path(row['original_path']).parent),
                'Original link-base provenance changed')
    return data


def verify():
    w.validate_publication_namespaces()
    relocated = {}
    for identifier, relative in w.LOCATIONS['non_rad_evidence'].items():
        data = verify_manifest(w.ROOT / relative)
        require(data['governance_tooling_version'] == identifier, 'Namespace classification mismatch')
        for row in data['files']:
            require(w.historical_path(row['original_path']) == w.ROOT / row['path'],
                    'Historical locator does not resolve exactly')
            relocated[row['original_path']] = row['sha256']
        for original, current in data['directory_moves'].items():
            require(w.historical_path(original) == w.ROOT / current, 'Historical directory locator mismatch')
    before = json.loads((AUDIT / 'protected-before.json').read_text())
    import home_context as hc
    completion = hc.EVIDENCE / 'before.json'
    amendments = {'docs/markdown/governance/baseline/correspondence.json',
                  *(f'docs/markdown/governance/baseline/{name}.md' for name in ('functions', 'processes', 'services')),
                  'docs/archimate/exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml',
                  *(f'docs/archimate/processing/publishing/1.0.9/{name}' for name in (
                      'reconciliation-publication.md', 'archi-inspection-guide.md', 'publication-validation-1.0.9.json'))}
    completed = []
    for relative, digest in before.items():
        if sha(w.ROOT / relative) != digest:
            require(completion.exists() and relative in amendments and
                    hashlib.sha256(hc.before_bytes(relative)).hexdigest() == digest,
                    'Protected architecture/evidence changed without preserved Home-completion authority: ' + relative)
            completed.append(relative)

    # Check moved-file claims in the original ledgers without refreshing any
    # stale claims about other live files changed by earlier accepted tasks.
    claims = []
    def visit(value):
        if isinstance(value, dict):
            for key, child in value.items():
                if key in relocated and isinstance(child, str) and len(child) == 64:
                    require(child == relocated[key], 'Historical relocated-file hash claim differs: ' + key)
                    claims.append(key)
                visit(child)
        elif isinstance(value, list):
            for child in value:
                visit(child)
    for relative in w.LOCATIONS['non_rad_evidence'].values():
        for path in (w.ROOT / relative).parent.rglob('*.json'):
            if path.name != 'manifest.json':
                visit(json.loads(path.read_text()))
    visit(json.loads((w.INGESTION / '1.0.8/run-001/reconciliation/execution-001/before.json').read_text()))

    # Preserve the historical verifier's bytes. Its only required adaptation is
    # resolving the now-moved literal hash path through the exact location map.
    script = w.INGESTION / '1.0.8/run-001/reconciliation/execution-001/verify-execution.py'
    replay = runpy.run_path(str(script), run_name='preserved_execution_audit')
    def historical_sha(path):
        relative = str(path.relative_to(w.ROOT)) if path.is_relative_to(w.ROOT) else None
        if completion.exists() and relative in amendments:
            return hashlib.sha256(hc.before_bytes(relative)).hexdigest()
        return sha(w.historical_path(path))
    replay['main'].__globals__['sha'] = historical_sha
    if completion.exists():
        # Replay recorded resulting-state signatures against their original
        # candidate, then validate the current completion separately. Do not
        # rewrite the frozen reconciliation script or its recorded results.
        original_g = replay['g']
        replay['main'].__globals__['g'] = SimpleNamespace(**{
            **vars(original_g), 'parse': lambda path: original_g.ET.fromstring(hc.before_bytes(
                str(path.relative_to(w.ROOT))))})
    output = io.StringIO()
    with contextlib.redirect_stdout(output):
        replay['main']()
    actual = json.loads(output.getvalue())
    recorded = json.loads(gzip.decompress((w.PUBLISHING / '1.0.9/validation/rad109-preservation-final.json.gz').read_bytes()))
    require(actual == recorded, 'Historical reconciliation audit no longer reproduces')
    return dict(status='PASS', classification='non-RAD governance/tooling evidence',
                relocated_files=len(relocated), original_file_hashes='UNCHANGED',
                original_publication_paths='ABSENT', historical_moved_file_hash_claims=len(claims),
                protected_files=len(before),
                rad_1_0_9_and_ingestion_evidence=('ORIGINAL SNAPSHOTS BYTE_IDENTICAL; authorised Home completion separate'
                    if completed else 'BYTE_IDENTICAL'),
                authorised_home_completion_files=completed,
                historical_reconciliation_audit='REPRODUCED using exact locator resolution',
                historical_ledgers_and_reports='UNCHANGED; not refreshed as RAD-1.0.9 evidence')


if __name__ == '__main__':
    try:
        print(json.dumps(verify(), indent=2))
    except (OSError, ValueError, KeyError) as exc:
        print('INVALID: ' + str(exc), file=sys.stderr)
        sys.exit(1)
