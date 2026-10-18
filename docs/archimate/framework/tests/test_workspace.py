# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Protect lifecycle boundaries and the exact migration-only release delta."""
import copy
from pathlib import Path
import subprocess
import sys
import tempfile
import unittest

BASE = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(BASE / 'tools'))
import generate_views as g
import synchronise_baseline as s
import workspace_paths as w
import verify_workspace as v


class WorkspaceTests(unittest.TestCase):
    def test_boundaries_and_pinned_historical_locators(self):
        self.assertEqual(BASE, w.FRAMEWORK)
        self.assertEqual(w.ROOT / 'docs/archimate/exchange/publish', w.PUBLISH)
        self.assertEqual(w.ROOT / 'docs/archimate/exchange/ingest', w.INGEST)
        self.assertFalse((w.ROOT / 'docs/archimate/capability-architecture').exists())
        self.assertEqual(w.PUBLISH / '1.0.8/Harmonia-Architecture-1.0.8.xml', s.metadata.model_path())
        self.assertEqual(w.PUBLISHING / '1.0.8', s.metadata.validation_path().parent)
        self.assertEqual(w.ROOT / 'docs/archimate/processing/publishing', w.PUBLISHING)
        self.assertEqual(w.ROOT / 'docs/archimate/processing/ingestion', w.INGESTION)
        self.assertTrue(w.INGESTION.is_dir())
        self.assertFalse((w.PUBLISH / 'synchronised-baseline').exists())
        for directory in w.PUBLISH.iterdir():
            if directory.is_dir():
                self.assertTrue(all(p.suffix == '.xml' for p in directory.iterdir()))
        old = 'docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-1.0.7.xml'
        self.assertEqual(v.BASELINE, w.historical_path(old))
        self.assertEqual(v.BASELINE_SHA256, s.r.sha(w.historical_path(old)))
        self.assertEqual(w.ESTABLISHMENT_EVIDENCE / 'identity-establishment-1.0.7',
                         s.metadata.archive())

    def test_incoming_filename_resolves_to_ingest_without_overriding_explicit_paths(self):
        self.assertEqual(w.INGEST / 'proposal.xml', w.incoming_path('proposal.xml'))
        for path in (Path('/tmp/explicit.xml'), Path('relative/export.xml')):
            self.assertEqual(path, w.incoming_path(path))
        with tempfile.TemporaryDirectory() as directory:
            output = Path(directory) / 'missing-export.json'
            run = subprocess.run([sys.executable, '-B', str(BASE / 'tools/diagnose_roundtrip.py'),
                'compare', '--published', str(v.BASELINE), '--incoming', 'missing-export.xml',
                '--output', str(output)], cwd=directory, capture_output=True, text=True, timeout=30)
            self.assertEqual(3, run.returncode, run.stderr)
            self.assertIn(str(w.INGEST / 'missing-export.xml'), output.read_text())

    def test_release_only_proof_rejects_semantic_identity_relationship_and_presentation_mutation(self):
        old = g.parse(v.BASELINE)
        new = copy.deepcopy(old)
        new.find("a:properties/a:property[@propertyDefinitionRef='baseline-architecture-release']/a:value", g.N).text = '1.0.8'
        before, after = g.serialize(old), g.serialize(new)
        self.assertEqual('PASS', v.compare_release_only(before, after)['status'])
        mutations = (
            ('a:elements/a:element/a:name', 'text', 'changed name'),
            ("a:elements/a:element/a:properties/a:property[@propertyDefinitionRef='governed-canonical-id']/a:value", 'text', '0' * 32),
            ('a:elements/a:element', g.TYPE, 'Resource'),
            ('a:relationships/a:relationship', 'target', 'changed-endpoint'),
            ('a:views/a:diagrams/a:view/a:node', 'x', '999999'),
            ('a:views/a:diagrams/a:view/a:connection', 'target', 'changed-visual-endpoint'))
        for path, field, value in mutations:
            with self.subTest(path=path, field=field):
                changed = copy.deepcopy(new)
                item = changed.find(path, g.N)
                self.assertIsNotNone(item)
                if field == 'text':
                    item.text = value
                else:
                    item.set(field, value)
                with self.assertRaisesRegex(ValueError, 'Unexpected architecture or presentation'):
                    v.compare_release_only(before, g.serialize(changed))
