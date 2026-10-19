# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Internal evidence cannot become a RAD publication or lose its provenance."""
import hashlib
import json
from pathlib import Path
import sys
import tempfile
import unittest
from unittest import mock

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'tools'))
import governed_metadata as m
import workspace_paths as w
import verify_governance_evidence as v


class GovernanceNamespaceTests(unittest.TestCase):
    def test_tooling_identifier_cannot_select_rad_publication_paths(self):
        with mock.patch('architecture_release.load_release', return_value={
                'version': '1.0.14', 'previous_version': '1.0.13'}):
            for select in (m.model_path, m.validation_path):
                with self.assertRaisesRegex(ValueError, 'not a RAD architecture release'):
                    select()

    def test_reappearing_directory_or_symlink_is_rejected_in_both_publication_roots(self):
        with tempfile.TemporaryDirectory() as temp:
            roots = [Path(temp) / name for name in ('exchange', 'processing')]
            for root in roots:
                root.mkdir()
            with mock.patch.object(w, 'PUBLISH', roots[0]), mock.patch.object(w, 'PUBLISHING', roots[1]):
                for root in roots:
                    intruder = root / '1.0.14'
                    intruder.mkdir()
                    with self.assertRaisesRegex(ValueError, 'Non-RAD evidence'):
                        w.validate_publication_namespaces()
                    intruder.rmdir()
                    intruder.symlink_to(root / 'absent')
                    with self.assertRaisesRegex(ValueError, 'Non-RAD evidence'):
                        w.validate_publication_namespaces()
                    intruder.unlink()

    def test_exact_relocation_and_original_relative_link_context_resolve_without_aliases(self):
        for relative in w.LOCATIONS['non_rad_evidence'].values():
            manifest = v.verify_manifest(w.ROOT / relative)
            for row in manifest['files']:
                self.assertEqual(w.ROOT / row['path'], w.historical_path(row['original_path']))
                self.assertFalse((w.ROOT / row['original_path']).exists())
        original_base = w.ROOT / 'docs/archimate/processing/publishing/1.0.14'
        target = original_base / '../../../exchange/publish/1.0.14/Harmonia-Architecture-1.0.14.xml'
        self.assertEqual(w.GOVERNANCE_TOOLING / '1.0.14/exchange/Harmonia-Architecture-1.0.14.xml',
                         w.historical_path(target))

    def test_evidence_mutation_and_rad_reclassification_fail_validation(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            folder = root / 'docs/archimate/governance-tooling/1.0.14'
            folder.mkdir(parents=True)
            evidence = folder / 'original.log'
            evidence.write_bytes(b'original evidence\n')
            data = dict(classification='governance-tooling-evidence', rad_architecture_release=None,
                        governance_tooling_version='1.0.14', files=[dict(
                            original_path='docs/archimate/processing/publishing/1.0.14/original.log',
                            original_link_base='docs/archimate/processing/publishing/1.0.14',
                            path=str(evidence.relative_to(root)), bytes=evidence.stat().st_size,
                            sha256=hashlib.sha256(evidence.read_bytes()).hexdigest())])
            manifest = folder / 'manifest.json'
            manifest.write_text(json.dumps(data))
            v.verify_manifest(manifest, root)
            evidence.write_bytes(b'modified evidence\n')
            with self.assertRaisesRegex(ValueError, 'evidence changed'):
                v.verify_manifest(manifest, root)
            data['rad_architecture_release'] = '1.0.14'
            manifest.write_text(json.dumps(data))
            with self.assertRaisesRegex(ValueError, 'explicitly non-RAD'):
                v.verify_manifest(manifest, root)
