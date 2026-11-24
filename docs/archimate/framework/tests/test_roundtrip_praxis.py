# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
import gzip
import hashlib
import json
from pathlib import Path
import sys
import tempfile
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'tools'))
import roundtrip_praxis as praxis
import governance_history
import test_workspace_navigation as fixtures


class PraxisCheckpointTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(dir='/tmp')
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        (self.root / 'architecture-release.json').write_text('{"version":"2.3.4","previous_version":"2.3.3"}')
        self.publish = self.root / 'docs/archimate/exchange/publish/2.3.4'
        self.ingest = self.root / 'docs/archimate/exchange/ingest/2.3.4'
        for path in (self.publish, self.ingest):
            path.mkdir(parents=True)
        (self.publish / 'baseline.xml').write_text('model')
        (self.ingest / 'architect.xml').write_text('model')

    def test_unique_discovery_ignores_backups_and_never_uses_timestamp(self):
        (self.ingest / 'architect.xml.bak').write_text('backup')
        chosen = praxis.discover(self.root)
        self.assertEqual(self.ingest / 'architect.xml', chosen['incoming'])
        (self.ingest / 'another.xml').write_text('model')
        with self.assertRaisesRegex(ValueError, 'missing/ambiguous'):
            praxis.discover(self.root)

    def test_explicit_input_resolves_ambiguity_but_same_input_pair_is_refused(self):
        (self.ingest / 'another.xml').write_text('model')
        chosen = praxis.discover(self.root, incoming=self.ingest / 'architect.xml')
        self.assertEqual(self.publish / 'baseline.xml', chosen['baseline'])
        with self.assertRaisesRegex(ValueError, 'distinct'):
            praxis.discover(self.root, baseline=self.publish / 'baseline.xml', incoming=self.publish / 'baseline.xml')

    def test_review_hash_applicability_does_not_imply_directive_execution_authority(self):
        review = self.root / 'change-review.md'
        intake = {'evidence':{'published_sha256':'a' * 64, 'incoming_sha256':'b' * 64}}
        text = 'Accept\n' + 'a' * 64 + '\n' + 'b' * 64
        review.write_text(text)
        result = praxis.inspect_review(review, intake)
        self.assertEqual(hashlib.sha256(text.encode()).hexdigest(), result['reviewed_document_sha256'])
        self.assertFalse(result['automatic_execution_authorised'])
        self.assertEqual(text, review.read_text())
        review.write_text('Accept\n' + 'c' * 64)
        with self.assertRaisesRegex(ValueError, 'applicable input'):
            praxis.inspect_review(review, intake)

    def test_intake_reuses_comparator_and_shared_containment_without_mutation(self):
        fixture = fixtures.WorkspaceNavigationTests()
        fixture.setUp()
        self.addCleanup(fixture.doCleanups)
        fixture.edge()
        model = fixture.model()
        before = model.path.read_bytes()
        # An explicit copy is an independent test input, not a real Archi proof.
        submitted = self.root / 'submission.xml'
        submitted.write_bytes(before)
        result = praxis.observe(model.path, submitted)
        self.assertEqual([], result['effective_containment']['findings'])
        self.assertEqual(['root', 'child'], result['effective_containment']['ancestry']['child'])
        self.assertEqual('NOT_AUTHORISED', result['evidence']['architectural_review']['lifecycle']['execution'])
        self.assertEqual(before, submitted.read_bytes())
        self.assertEqual(before, model.path.read_bytes())


class GovernanceHistoryTests(unittest.TestCase):
    def test_exact_registered_transition_chain_and_drift_rejection(self):
        with tempfile.TemporaryDirectory(dir='/tmp') as temporary:
            root = Path(temporary)
            entries = []
            for index, (before, after) in enumerate(((b'original', b'adjudication'), (b'adjudication', b'clarification'))):
                folder = root / str(index)
                folder.mkdir()
                (folder / 'before.gz').write_bytes(gzip.compress(before, mtime=0))
                manifest = folder / 'manifest.json'
                manifest.write_text(json.dumps({'files':{'governance.md':dict(
                    before_sha256=hashlib.sha256(before).hexdigest(), after_sha256=hashlib.sha256(after).hexdigest(),
                    snapshot='before.gz')}}))
                entries.append(dict(manifest=str(manifest.relative_to(root)), sha256=hashlib.sha256(manifest.read_bytes()).hexdigest()))
            register = root / 'register.json'
            register.write_text(json.dumps({'transitions':entries}))
            self.assertTrue(governance_history.follows(root, 'governance.md', hashlib.sha256(b'original').hexdigest(),
                                                      hashlib.sha256(b'clarification').hexdigest(), register))
            self.assertFalse(governance_history.follows(root, 'governance.md', hashlib.sha256(b'original').hexdigest(),
                                                       hashlib.sha256(b'unrecorded edit').hexdigest(), register))
            (root / '1/before.gz').write_bytes(gzip.compress(b'tampered'))
            with self.assertRaisesRegex(ValueError, 'before-snapshot drift'):
                governance_history.follows(root, 'governance.md', 'old', 'new', register)
