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


class HistoricalAuthorityTests(unittest.TestCase):
    def fixture(self, directory):
        import gzip
        root = Path(directory)
        source = root / 'docs/markdown/source.md'
        source.parent.mkdir(parents=True)
        source.write_text('Accepted current metamodel rule\n')
        original = b'Source-era definition\n'
        expected = hashlib.sha256(original).hexdigest()
        archive = root / 'docs/markdown/governance/baseline/source-history/source.md.gz'
        archive.parent.mkdir(parents=True)
        archive.write_bytes(gzip.compress(original, mtime=0))
        manifest = archive.parent.parent / 'correspondence.json'
        relative = str(source.relative_to(root))
        data = {
            'current_authority_sha256': {relative: hashlib.sha256(source.read_bytes()).hexdigest()},
            'historical_authority_sources': {relative: {
                'original_sha256': expected,
                'snapshot': str(archive.relative_to(root)),
                'snapshot_sha256': hashlib.sha256(archive.read_bytes()).hexdigest()}}}
        manifest.write_text(json.dumps(data))
        return root, source, archive, manifest, data, original.decode(), expected

    def test_exact_archive_validates_frozen_source_without_changing_live_authority(self):
        with tempfile.TemporaryDirectory() as directory:
            root, source, archive, manifest, data, original, expected = self.fixture(directory)
            self.assertEqual(original, r.source_era_text(source, expected, root))
            self.assertEqual('Accepted current metamodel rule\n', source.read_text())

    def test_unrecorded_live_source_or_archive_drift_is_not_hidden(self):
        for target in ('live', 'archive'):
            with self.subTest(target=target), tempfile.TemporaryDirectory() as directory:
                root, source, archive, manifest, data, original, expected = self.fixture(directory)
                path = source if target == 'live' else archive
                path.write_bytes(b'Unrecorded mutation\n')
                self.assertNotEqual(original, r.source_era_text(source, expected, root))

    def test_archive_pin_cannot_replace_the_expected_original(self):
        import gzip
        with tempfile.TemporaryDirectory() as directory:
            root, source, archive, manifest, data, original, expected = self.fixture(directory)
            archive.write_bytes(gzip.compress(b'Wrong historical definition\n', mtime=0))
            entry = data['historical_authority_sources']['docs/markdown/source.md']
            entry['snapshot_sha256'] = hashlib.sha256(archive.read_bytes()).hexdigest()
            manifest.write_text(json.dumps(data))
            self.assertNotEqual(original, r.source_era_text(source, expected, root))

    def test_wrong_source_hash_or_archive_outside_history_is_rejected(self):
        for mutation in ('hash', 'path'):
            with self.subTest(mutation=mutation), tempfile.TemporaryDirectory() as directory:
                root, source, archive, manifest, data, original, expected = self.fixture(directory)
                entry = data['historical_authority_sources']['docs/markdown/source.md']
                if mutation == 'hash':
                    entry['original_sha256'] = '0' * 64
                else:
                    other = root / 'docs/markdown/unreviewed.md.gz'
                    other.write_bytes(archive.read_bytes())
                    entry['snapshot'] = str(other.relative_to(root))
                manifest.write_text(json.dumps(data))
                self.assertNotEqual(original, r.source_era_text(source, expected, root))
