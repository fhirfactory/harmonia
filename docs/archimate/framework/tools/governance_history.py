# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Verify explicit governance-only hash transitions without rewriting old pins."""
import gzip
import hashlib
import json
from pathlib import Path


def follows(root, relative, previous, current, register):
    """Follow a registered, snapshot-proven chain, failing closed on drift."""
    entries = json.loads(Path(register).read_bytes())['transitions']
    remaining = []
    for entry in entries:
        manifest = (root / entry['manifest']).resolve()
        if not manifest.is_relative_to(root.resolve()):
            raise ValueError('Governance transition outside repository')
        raw = manifest.read_bytes()
        if hashlib.sha256(raw).hexdigest() != entry['sha256']:
            raise ValueError('Governance transition manifest drift')
        row = json.loads(raw)['files'].get(relative)
        if row:
            snapshot = (manifest.parent / row['snapshot']).resolve()
            if not snapshot.is_relative_to(manifest.parent):
                raise ValueError('Governance snapshot outside transition archive')
            data = gzip.decompress(snapshot.read_bytes())
            if hashlib.sha256(data).hexdigest() != row['before_sha256']:
                raise ValueError('Governance before-snapshot drift')
            remaining.append(row)
    seen = set()
    while previous != current:
        if previous in seen:
            return False
        seen.add(previous)
        matches = [row for row in remaining if row['before_sha256'] == previous]
        if len(matches) != 1:
            return False
        previous = matches[0]['after_sha256']
    return True
