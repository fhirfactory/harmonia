# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Release metadata cannot change identities or erase assessment findings."""
import copy
import json
from pathlib import Path
import sys
import tempfile
import unittest

BASE = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(BASE / "tools"))
import assess_rad as a
import report_publication as r


class PublicationMetadataTests(unittest.TestCase):
    def test_assessment_uses_explicit_release_and_retains_unresolved_assertions(self):
        with tempfile.TemporaryDirectory() as tmp:
            release = Path(tmp) / "release.json"
            release.write_text('{"version":"7.8.9","previous_version":"7.8.8"}')
            manifest = json.loads((BASE / "documentation-map.json").read_text())
            data = a.inventory(BASE.parents[2], manifest, release)
            self.assertEqual("7.8.9", data["architecture_release"]["version"])
            self.assertEqual(0, data["summary"]["canonical_ids_allocated_in_capture"])
            self.assertEqual(22, data["summary"]["unresolved_relationship_assertions"])
            collisions = [row for row in data["application_register_rows"] if row["source_key"] == "F46"]
            self.assertEqual(2, len(collisions))
            self.assertNotEqual(collisions[0]["label"], collisions[1]["label"])
            self.assertTrue(all("navigation key" in row["identity_status"]
                                for row in data["application_register_rows"] if row["source_key"].startswith("C")))
            self.assertEqual(14, len(data["exact_enterprise_documentation_crosswalk"]))

    def test_report_rejects_mismatched_release_and_failed_verification(self):
        release = {"version": "7.8.9", "previous_version": "7.8.8"}
        publication = json.loads((BASE / "validation.json").read_text())
        inventory = json.loads((BASE.parents[2] / "docs/markdown/governance/rad-archimate-source-inventory.json").read_text())
        publication["architecture_release"] = inventory["architecture_release"] = release
        verification = {"architecture_release": release, "checks": [{"name": "suite", "status": "PASS", "result": "verified"}]}
        text = r.render(release, publication, inventory, verification)
        self.assertIn("7.8.8 → 7.8.9", text)
        self.assertIn("214 unresolved", text)
        self.assertIn("2,467 unresolved", text)
        for record in (publication, inventory, verification):
            stale = copy.deepcopy(record)
            record["architecture_release"] = {"version": "7.8.8", "previous_version": "7.8.7"}
            with self.assertRaisesRegex(ValueError, "Stale"):
                r.render(release, publication, inventory, verification)
            record.update(stale)
        verification["checks"][0]["status"] = "TIMEOUT"
        with self.assertRaisesRegex(ValueError, "incomplete"):
            r.render(release, publication, inventory, verification)
