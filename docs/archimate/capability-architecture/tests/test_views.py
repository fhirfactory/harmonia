# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Preservation, importer constraints, layout and rejection probes for publication."""
import copy
import hashlib
import json
from pathlib import Path
import shutil
import subprocess
import sys
import tempfile
import unittest

BASE = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(BASE / "tools"))
import generate_views as g


class ViewPublicationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.baseline_path = BASE / "baseline/Harmonia-Capability-Architecture-v2.1a.xml"
        cls.baseline = g.parse(cls.baseline_path)
        cls.lock = json.loads((BASE / "baseline/semantic-lock.json").read_text())
        cls.schema = g.load_schema(BASE / "schemas/archimate3_Diagram.xsd")
        cls.generated = g.augment(cls.baseline)

    def setUp(self):
        self.result = copy.deepcopy(self.generated)

    def view(self, key="business"):
        identifier = g.visual_id(self.result.get("identifier"), "view", key)
        return self.result.find(f"a:views/a:diagrams/a:view[@identifier='{identifier}']", g.N)

    def assert_invalid(self):
        with self.assertRaises((g.PublicationError, g.ET.DocumentInvalid)):
            g.validate(self.baseline, self.result, self.schema, self.lock)

    def test_fixture_and_schema_provenance(self):
        self.assertEqual(self.lock["source_sha256"], hashlib.sha256(self.baseline_path.read_bytes()).hexdigest())
        provenance = json.loads((BASE / "schemas/provenance.json").read_text())
        self.assertEqual("5.9.0", provenance["archi_version"])
        self.assertEqual(5, len(provenance["files"]))
        for name, expected in provenance["files"].items():
            self.assertEqual(expected, hashlib.sha256((BASE / "schemas" / name).read_bytes()).hexdigest(), name)
        self.schema.assertValid(self.baseline)

    def test_complete_publication_and_locked_semantic_identifiers(self):
        report = g.validate(self.baseline, self.result, self.schema, self.lock)
        self.assertEqual(228, report["semantic_elements"])
        self.assertEqual(189, report["semantic_relationships"])
        self.assertEqual(4, len(report["generated_views"]))
        self.assertEqual(1, report["retained_views"])
        for parent, item in (("elements", "element"), ("relationships", "relationship")):
            path = f"a:{parent}/a:{item}"
            self.assertEqual([e.get("identifier") for e in self.baseline.findall(path, g.N)],
                             [e.get("identifier") for e in self.result.findall(path, g.N)])

    def test_semantic_subtrees_survive_serialization_unchanged(self):
        data = g.serialize(self.result)
        original = self.baseline_path.read_bytes()
        for name in ("elements", "relationships", "propertyDefinitions"):
            opening, closing = f"<{name}>".encode(), f"</{name}>".encode()
            before = original.split(opening, 1)[1].split(closing, 1)[0]
            after = data.split(opening, 1)[1].split(closing, 1)[0]
            self.assertEqual(hashlib.sha256(before).hexdigest(), hashlib.sha256(after).hexdigest(), name)
        self.assertEqual(g.semantic_snapshot(self.baseline), g.semantic_snapshot(self.result))

    def test_exact_view_coverage_and_no_invented_enterprise_hierarchy(self):
        report = g.validate(self.baseline, self.result, self.schema, self.lock)
        self.assertEqual([(39, 0), (52, 34), (162, 155), (14, 0)],
                         [(v["nodes"], v["connections"]) for v in report["generated_views"]])
        for spec in g.VIEW_SPECS:
            for node in self.view(spec[0]).findall("a:node", g.N):
                self.assertIsNone(node.find("a:label", g.N))
                self.assertIsNone(node.find("a:name", g.N))
                self.assertEqual("12", node.find("a:style/a:font", g.N).get("size"))

    def test_natural_view_groups_do_not_become_capabilities_or_nodes(self):
        model = g.Model(self.result)
        represented = {n.get("elementRef") for n in self.view("business-enabling").findall("a:node", g.N)}
        self.assertEqual(set(model.selected(g.LAYERS[1])), represented)
        self.assertIn("none is inferred", self.view("business-enabling").findtext("a:documentation", namespaces=g.N))
        self.assertEqual(g.semantic_snapshot(self.baseline), g.semantic_snapshot(self.result))

    def test_repeated_generation_and_republication_are_byte_identical(self):
        data, report = g.publish(self.baseline, self.schema, self.lock)
        self.assertEqual(data, g.publish(self.baseline, self.schema, self.lock)[0])
        self.assertEqual(data, g.serialize(g.augment(g.ET.fromstring(data))))
        self.assertEqual("PASS", report["checks"]["repeated_generation_and_idempotence"])

    def test_view_layout_and_ids_are_independent_of_input_record_order(self):
        reordered = copy.deepcopy(self.baseline)
        for path in ("a:elements", "a:relationships"):
            parent = reordered.find(path, g.N)
            parent[:] = list(reversed(parent))
        other = g.augment(reordered)
        for key, _, _ in g.VIEW_SPECS:
            identifier = g.visual_id(other.get("identifier"), "view", key)
            view = other.find(f"a:views/a:diagrams/a:view[@identifier='{identifier}']", g.N)
            self.assertEqual(g.signature(self.view(key)), g.signature(view))

    def test_visual_ids_do_not_depend_on_label_or_coordinates(self):
        changed = copy.deepcopy(self.baseline)
        for element in changed.findall("a:elements/a:element", g.N):
            element.find("a:name", g.N).text += " changed label for identity probe"
        other = g.augment(changed)
        def ids(root):
            return {item.get("identifier") for view in root.findall("a:views/a:diagrams/a:view", g.N)
                    for item in view.iter() if item.get("identifier")}
        self.assertEqual(ids(self.generated), ids(other))

    def test_rejects_semantic_element_id_change(self):
        self.result.find("a:elements/a:element", g.N).set("identifier", "id-changed")
        self.assert_invalid()

    def test_rejects_semantic_relationship_id_change(self):
        self.result.find("a:relationships/a:relationship", g.N).set("identifier", "id-changed")
        self.assert_invalid()

    def test_lock_detects_label_property_and_hierarchy_changes(self):
        mutations = (
            ("a:elements/a:element/a:name", "text", "Changed name"),
            ("a:elements/a:element/a:properties/a:property/a:value", "text", "Changed property"),
            ("a:relationships/a:relationship", "source", "id-changed"),
            ("a:organizations/a:item/a:item", "identifierRef", "id-changed"),
        )
        for path, attribute, value in mutations:
            with self.subTest(path=path):
                altered = copy.deepcopy(self.baseline)
                node = altered.find(path, g.N)
                if attribute == "text":
                    node.text = value
                else:
                    node.set(attribute, value)
                with self.assertRaisesRegex(g.PublicationError, "lock mismatch"):
                    g.validate_lock(altered, self.lock)

    def test_rejects_duplicate_identifiers_across_views(self):
        self.view("enterprise").find("a:node", g.N).set("identifier", self.view().find("a:node", g.N).get("identifier"))
        self.assert_invalid()

    def test_rejects_dangling_element_reference(self):
        self.view().find("a:node", g.N).set("elementRef", "id-missing")
        self.assert_invalid()

    def test_rejects_node_without_capability_reference(self):
        del self.view().find("a:node", g.N).attrib["elementRef"]
        self.assert_invalid()

    def test_rejects_dangling_relationship_reference(self):
        self.view().find("a:connection", g.N).set("relationshipRef", "id-missing")
        self.assert_invalid()

    def test_rejects_connection_with_semantic_id_as_source(self):
        connection = self.view().find("a:connection", g.N)
        node = self.view().find("a:node", g.N)
        connection.set("source", node.get("elementRef"))
        self.assert_invalid()

    def test_rejects_connection_to_node_in_another_view(self):
        self.view().find("a:connection", g.N).set("target", self.view("landscape").find("a:node", g.N).get("identifier"))
        self.assert_invalid()

    def test_rejects_schema_valid_semantically_wrong_connection_endpoint(self):
        connection = self.view().find("a:connection", g.N)
        connection.set("target", connection.get("source"))
        self.schema.assertValid(self.result)
        with self.assertRaisesRegex(g.PublicationError, "disagree with semantic"):
            g.validate(self.baseline, self.result, self.schema, self.lock)

    def test_rejects_missing_relationship_projection(self):
        view = self.view()
        view.remove(view.find("a:connection", g.N))
        self.assert_invalid()

    def test_rejects_missing_capability_projection(self):
        view = self.view("landscape")
        view.remove(view.find("a:node", g.N))
        self.assert_invalid()

    def test_rejects_view_in_wrong_organisation_folder(self):
        group = self.result.xpath("a:organizations/a:item[a:label='Views']/a:item[a:label='Capability Architecture']", namespaces=g.N)[0]
        overview, models = group.findall("a:item", g.N)
        item = overview.find("a:item", g.N)
        models.append(item)
        self.schema.assertValid(self.result)
        self.assert_invalid()

    def test_rejects_modified_retained_import_test_view(self):
        self.result.find("a:views/a:diagrams/a:view/a:name", g.N).text = "Changed diagnostic"
        self.assert_invalid()

    def test_rejects_removed_retained_view_navigation(self):
        views = self.result.xpath("a:organizations/a:item[a:label='Views']", namespaces=g.N)[0]
        views.remove(views.find("a:item", g.N))
        self.assert_invalid()

    def test_rejects_overlapping_nodes(self):
        first, second = self.view("landscape").findall("a:node", g.N)[:2]
        second.set("x", first.get("x"))
        second.set("y", first.get("y"))
        self.assert_invalid()

    def test_rejects_invalid_xml_geometry(self):
        self.view().find("a:node", g.N).set("x", "-1")
        self.assert_invalid()

    def test_creation_of_views_organisation_when_absent(self):
        baseline = copy.deepcopy(self.baseline)
        views = baseline.xpath("a:organizations/a:item[a:label='Views']", namespaces=g.N)[0]
        views.getparent().remove(views)
        baseline.remove(baseline.find("a:views", g.N))
        result = g.augment(baseline)
        report = g.validate(baseline, result, self.schema, self.lock)
        self.assertEqual(0, report["retained_views"])
        self.assertEqual(g.serialize(result), g.serialize(g.augment(result)))

    def test_cli_runs_from_an_isolated_copy_without_downloads_or_repository_json(self):
        with tempfile.TemporaryDirectory() as tmp:
            dest = Path(tmp) / "publication"
            shutil.copytree(BASE, dest, ignore=shutil.ignore_patterns("__pycache__"))
            command = [sys.executable, str(dest / "tools/generate_views.py"),
                       "--baseline", str(dest / "baseline/Harmonia-Capability-Architecture-v2.1a.xml"),
                       "--schema", str(dest / "schemas/archimate3_Diagram.xsd")]
            result = subprocess.run(command, cwd=tmp, capture_output=True, text=True, timeout=20)
            self.assertEqual(0, result.returncode, result.stderr)
            self.assertEqual(g.serialize(self.generated), (dest / "Harmonia-Capability-Architecture.xml").read_bytes())
            checked = subprocess.run(command + ["--check"], cwd=tmp, capture_output=True, text=True, timeout=20)
            self.assertEqual(0, checked.returncode, checked.stderr)

    def test_cli_refuses_overwriting_baseline(self):
        with tempfile.TemporaryDirectory() as tmp:
            baseline = Path(tmp) / "baseline.xml"
            baseline.write_bytes(self.baseline_path.read_bytes())
            result = subprocess.run([sys.executable, str(BASE / "tools/generate_views.py"),
                                     "--baseline", str(baseline), "--output", str(baseline)],
                                    capture_output=True, text=True, timeout=20)
            self.assertEqual(1, result.returncode)
            self.assertIn("must not overwrite", result.stderr)
            self.assertEqual(self.baseline_path.read_bytes(), baseline.read_bytes())


if __name__ == "__main__":
    unittest.main()
