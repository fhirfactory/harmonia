# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
import gzip
import json
from pathlib import Path
import tempfile
import unittest

import sys
sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'tools'))
import run_evidence as evidence


class RunEvidenceTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(dir='/tmp')
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.run = self.root / 'arbitrary-run'
        self.outputs = {'candidate.xml': b'old candidate', 'plan.json': b'old plan'}
        self.outputs['manifest.json'] = json.dumps({'files_sha256':{key:evidence.sha(value) for key,value in self.outputs.items()}}).encode()
        evidence.write(self.run, self.outputs)
        self.archive = self.root / 'before'
        self.archive.mkdir()
        rows = {}
        for name, data in self.outputs.items():
            (self.archive / (name + '.gz')).write_bytes(gzip.compress(data, mtime=0))
            rows['arbitrary-run/' + name] = dict(sha256=evidence.sha(data), snapshot=name + '.gz')
        (self.archive / 'manifest.json').write_text(json.dumps({'files':rows}))

    def test_correction_checks_all_preconditions_before_mutation(self):
        (self.run / 'plan.json').write_bytes(b'changed after capture')
        with self.assertRaisesRegex(ValueError, 'Run changed since'):
            evidence.write(self.run, {'candidate.xml':b'new'}, before_archive=self.archive, repository=self.root)
        self.assertEqual(b'old candidate', (self.run / 'candidate.xml').read_bytes())

    def test_verified_revision_then_stale_archive_is_refused(self):
        evidence.write(self.run, {'candidate.xml':b'new'}, before_archive=self.archive, repository=self.root)
        with self.assertRaisesRegex(ValueError, 'Run changed since'):
            evidence.write(self.run, {'candidate.xml':b'another'}, before_archive=self.archive, repository=self.root)

    def test_create_and_check_never_overwrite_existing_evidence(self):
        with self.assertRaisesRegex(ValueError, 'already exists'):
            evidence.write(self.run, {'candidate.xml':b'new'})
        evidence.write(self.run, self.outputs, check=True)
        with self.assertRaisesRegex(ValueError, 'Stale/changed'):
            evidence.write(self.run, {'candidate.xml':b'new'}, check=True)
        self.assertEqual(b'old candidate', (self.run / 'candidate.xml').read_bytes())

    def test_archive_and_output_paths_cannot_escape_their_boundaries(self):
        with self.assertRaisesRegex(ValueError, 'escapes'):
            evidence.write(self.run, {'../candidate.xml':b'new'}, check=True)
