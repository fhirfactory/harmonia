# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Common guarded writer for machinery-validation outputs, never RAD releases."""
import gzip
import hashlib
import json
from pathlib import Path


def require(value, message):
    if not value:
        raise ValueError(message)


def sha(data):
    return hashlib.sha256(data).hexdigest()


def write(destination, outputs, *, check=False, before_archive=None, repository=None):
    """Validate the entire correction precondition before writing any output.

    A before archive pins the original manifest and every generated output.
    Repeated correction with a stale archive is refused. Partial writes remain
    observable as changed output hashes; no successful transaction is presumed.
    """
    destination = Path(destination)
    for name in outputs:
        path = destination / name
        require(path.resolve().is_relative_to(destination.resolve()), 'Output escapes run directory')
    if check:
        for name, data in outputs.items():
            require((destination / name).read_bytes() == data, 'Stale/changed run evidence: ' + name)
        return
    if before_archive:
        require(repository is not None, 'Correction requires repository root')
        archive = Path(before_archive)
        rows = json.loads((archive / 'manifest.json').read_bytes())['files']
        def original(path):
            row = rows[str(path.relative_to(repository))]
            snapshot = (archive / row['snapshot']).resolve()
            require(snapshot.is_relative_to(archive.resolve()), 'Snapshot escapes archive')
            data = gzip.decompress(snapshot.read_bytes())
            require(sha(data) == row['sha256'], 'Before archive drift: ' + str(path))
            require(path.read_bytes() == data, 'Run changed since before archive: ' + str(path))
            return data
        previous = json.loads(original(destination / 'manifest.json'))
        for name, expected in previous['files_sha256'].items():
            require(sha(original(destination / name)) == expected, 'Before output hash mismatch: ' + name)
    else:
        require(not destination.exists(), 'Run already exists; preserve evidence and use --check')
        destination.mkdir(parents=True, exist_ok=False)
    for name, data in outputs.items():
        path = destination / name
        path.parent.mkdir(parents=True, exist_ok=True)
        if before_archive:
            path.write_bytes(data)
        else:
            with path.open('xb') as stream:
                stream.write(data)
