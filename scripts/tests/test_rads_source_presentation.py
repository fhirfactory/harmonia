# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Presentation notices cannot relax historical source evidence validation."""
import hashlib
import json
from pathlib import Path
import sys
import tempfile
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import rads_business_architecture as r


class SourcePresentationTests(unittest.TestCase):
    def fixture(self, directory):
        root = Path(directory)
        path = root / 'docs/markdown/source.md'
        path.parent.mkdir(parents=True)
        original = 'Original title\nOriginal definition\n'
        annotation = '> Current baseline navigation\n\n'
        annotated = annotation + original
        path.write_text(annotated)
        sha = lambda text: hashlib.sha256(text.encode()).hexdigest()
        overlay = root / 'docs/markdown/governance/baseline/source-presentation-overlays.json'
        overlay.parent.mkdir(parents=True)
        overlay.write_text(json.dumps({'files': {'docs/markdown/source.md': {
            'original_sha256': sha(original), 'annotated_sha256': sha(annotated),
            'insertions': [{'line': 1, 'text': annotation}]}}}))
        return root, path, original, sha(original)

    def test_only_exact_reviewed_insertions_are_removed_in_memory(self):
        with tempfile.TemporaryDirectory() as directory:
            root, path, original, expected = self.fixture(directory)
            self.assertEqual(original, r.source_era_text(path, expected, root))
            self.assertNotEqual(original, path.read_text())

    def test_changed_definition_or_unknown_notice_cannot_bypass_source_drift(self):
        for change in ('Original definition', 'Current baseline navigation'):
            with tempfile.TemporaryDirectory() as directory:
                root, path, original, expected = self.fixture(directory)
                path.write_text(path.read_text().replace(change, 'Changed source semantics or unreviewed annotation'))
                self.assertNotEqual(original, r.source_era_text(path, expected, root))

    def test_a_false_annotated_hash_cannot_hide_modified_original_content(self):
        with tempfile.TemporaryDirectory() as directory:
            root, path, original, expected = self.fixture(directory)
            path.write_text(path.read_text().replace('Original definition', 'Unapproved definition'))
            overlay = root / 'docs/markdown/governance/baseline/source-presentation-overlays.json'
            data = json.loads(overlay.read_text())
            data['files']['docs/markdown/source.md']['annotated_sha256'] = hashlib.sha256(path.read_bytes()).hexdigest()
            overlay.write_text(json.dumps(data))
            self.assertNotEqual(original, r.source_era_text(path, expected, root))
