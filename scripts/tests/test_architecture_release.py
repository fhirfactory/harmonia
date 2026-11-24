# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Release authority, explicit patch increments and stale-expectation guards."""
import importlib.util
import json
from pathlib import Path
import tempfile
import unittest

spec = importlib.util.spec_from_file_location("architecture_release", Path(__file__).resolve().parents[1] / "architecture_release.py")
release = importlib.util.module_from_spec(spec)
spec.loader.exec_module(release)


class ArchitectureReleaseTests(unittest.TestCase):
    def test_read_is_non_mutating(self):
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "release.json"
            raw = '{"version":"7.8.9","previous_version":"7.8.8"}\n'
            path.write_text(raw)
            self.assertEqual("7.8.9", release.load_release(path)["version"])
            self.assertEqual(raw, path.read_text())

    def test_explicit_increment_changes_only_patch_and_records_previous(self):
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "release.json"
            path.write_text('{"version":"7.8.99","previous_version":"7.8.98"}')
            self.assertEqual({"version": "7.8.100", "previous_version": "7.8.99"}, release.bump_patch(path, "7.8.99"))
            self.assertEqual("7.8.100", release.load_release(path)["version"])
            before = path.read_bytes()
            with self.assertRaisesRegex(ValueError, "Expected"):
                release.bump_patch(path, "7.8.99")
            self.assertEqual(before, path.read_bytes())

    def test_rejects_malformed_ambiguous_or_non_increasing_authority(self):
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "release.json"
            cases = [[], {"version": "1.0.1"},
                     {"version": "1.0.1", "previous_version": "1.0.0", "software": "1.0.0"}]
            cases += [{"version": value, "previous_version": "1.0.0"} for value in
                      ("1.0.0", "0.9.9", "01.0.1", "1.0.1-SNAPSHOT", 101, "1.1", "1.0.1\n")]
            for data in cases:
                with self.subTest(data=data):
                    path.write_text(json.dumps(data))
                    with self.assertRaises(ValueError):
                        release.load_release(path)
            path.write_text('{"version":"1.0.1","version":"2.0.0","previous_version":"1.0.0"}')
            with self.assertRaisesRegex(ValueError, "Duplicate"):
                release.load_release(path)
