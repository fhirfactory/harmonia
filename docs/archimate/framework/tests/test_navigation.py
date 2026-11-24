# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Navigation is a checked projection and cannot supply semantic architecture."""
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
import publish_documentation as d
import publish_navigation as n
import report_navigation as r


class NavigationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.baseline = g.parse(BASE / "baseline/Harmonia-Capability-Architecture-v2.1a.xml")
        cls.lock = json.loads((BASE / "baseline/semantic-lock.json").read_text())
        cls.schema = g.load_schema(BASE / "schemas/archimate3_Diagram.xsd")
        cls.previous = json.loads(n.DEFAULT_LOCK.read_text())
        cls.original = g.ET.fromstring(d.publish(cls.baseline, cls.schema, cls.lock)[0])
        cls.data, cls.report = n.publish(cls.baseline, cls.schema, cls.lock)
        cls.model = g.Model(cls.original)

    def setUp(self):
        self.root = g.ET.fromstring(self.data)

    def invalid(self, fragment=None):
        error = (g.PublicationError, g.ET.DocumentInvalid)
        if fragment:
            with self.assertRaisesRegex(error, fragment):
                n.validate(self.original, self.root, self.schema, self.previous)
        else:
            with self.assertRaises(error):
                n.validate(self.original, self.root, self.schema, self.previous)

    def item(self, identifier):
        return self.root.xpath("a:organizations//a:item[@identifierRef=$ref]", namespaces=g.N, ref=identifier)[0]

    def test_architecture_is_unchanged_and_independent_of_navigation(self):
        self.assertEqual(n.architecture_snapshot(self.original), n.architecture_snapshot(self.root))
        self.assertEqual(228, len(self.model.elements))
        self.assertEqual(189, len(self.model.relationships))
        self.assertEqual({0}, set(self.report["semantic_changes"].values()))
        self.assertNotEqual(g.signature(self.original.find("a:organizations", g.N)),
                            g.signature(self.root.find("a:organizations", g.N)))
        self.assertEqual(self.lock["source_sha256"], hashlib.sha256((BASE / "baseline/Harmonia-Capability-Architecture-v2.1a.xml").read_bytes()).hexdigest())
        g.validate_lock(self.baseline, self.lock)

    def test_each_semantic_object_has_one_home_and_relationships_follow_source(self):
        homes = n.navigation_homes(self.root)
        self.assertTrue(all(len(paths) == 1 for paths in homes.values()))
        for rid, relation in self.model.relationships.items():
            source, target = relation.get("source"), relation.get("target")
            context = (n.name(self.model, k) for k in n.ancestry(self.model, source))
            self.assertEqual([("Relations", "Capabilities", n.CATEGORIES[self.model.layer(source)], *context)], homes[rid])
            self.assertNotEqual(homes[source], homes[target])
        for layer in g.LAYERS[:2]:
            for key in self.model.selected(layer):
                context = tuple(n.name(self.model, k) for k in n.ancestry(self.model, key))
                self.assertEqual([("Strategy", n.CATEGORIES[layer], *context)], homes[key])

    def test_enterprise_is_a_flat_orthogonal_catalogue_with_same_documentation(self):
        homes = n.navigation_homes(self.root)
        for key in self.model.selected(g.LAYERS[2]):
            self.assertEqual([("Strategy", "Enterprise Capabilities")], homes[key])
            before = self.original.xpath("a:elements/a:element[@identifier=$id]", namespaces=g.N, id=key)[0]
            after = self.root.xpath("a:elements/a:element[@identifier=$id]", namespaces=g.N, id=key)[0]
            self.assertEqual(g.signature(before), g.signature(after))

    def test_retained_views_are_identical_and_details_are_immediate_only(self):
        self.assertEqual(5, len(self.report["view_changes"]["retained"]))
        self.assertEqual(214, len(self.report["view_changes"]["added"]))
        self.assertEqual([], self.report["view_changes"]["replaced"])
        self.assertEqual(219, len(self.root.findall("a:views/a:diagrams/a:view", g.N)))
        for old in self.original.findall("a:views/a:diagrams/a:view", g.N):
            current = self.root.xpath("a:views/a:diagrams/a:view[@identifier=$id]", namespaces=g.N, id=old.get("identifier"))[0]
            self.assertEqual(g.signature(old), g.signature(current))
        for view in self.report["detailed_views"]:
            self.assertEqual(1 + len(self.model.children[view["context"]]), view["nodes"])
            self.assertLessEqual(view["nodes"], 22)
            self.assertLessEqual(view["height"], 1100)
        self.assertEqual(189, sum(v["connections"] for v in self.report["detailed_views"]))

    def test_regeneration_is_deterministic_and_does_not_bump_the_release(self):
        release = n.load_release()
        again, report = n.publish(self.baseline, self.schema, self.lock)
        self.assertEqual(self.data, again)
        self.assertEqual(self.report, report)
        self.assertEqual(self.data, g.serialize(n.augment(self.root)))
        self.assertEqual(release, n.load_release())

    def test_navigation_and_visual_ids_ignore_labels_coordinates_and_input_order(self):
        changed = copy.deepcopy(self.original)
        for path in ("a:elements", "a:relationships"):
            parent = changed.find(path, g.N)
            parent[:] = list(reversed(parent))
        reordered = n.augment(changed)
        self.assertEqual(g.signature(self.root.find("a:organizations", g.N)), g.signature(reordered.find("a:organizations", g.N)))
        for e in changed.findall("a:elements/a:element", g.N):
            e.find("a:name", g.N).text += " renamed for identity probe"
        renamed = n.augment(changed)
        def identifiers(root):
            return {e.get("identifier") for e in root.iter() if e.get("identifier")}
        self.assertEqual(identifiers(self.root), identifiers(renamed))

    def test_folder_relocation_changes_no_semantics_but_is_rejected_as_navigation(self):
        key = next(k for k in self.model.elements if k in self.model.parents)
        folder = self.item(key).getparent()
        self.item(self.model.parents[key]).getparent().getparent().append(folder)
        self.schema.assertValid(self.root)
        self.assertEqual(n.architecture_snapshot(self.original), n.architecture_snapshot(self.root))
        self.invalid("navigation mismatch")

    def test_target_based_relationship_home_is_rejected(self):
        rid, relation = next(iter(self.model.relationships.items()))
        target = relation.get("target")
        folder = self.root.xpath("a:organizations//a:item[@identifier=$id]", namespaces=g.N,
                                 id=n.folder_id(self.model, "relations", target))[0]
        folder.append(self.item(rid))
        self.schema.assertValid(self.root)
        self.invalid("must follow its source")

    def test_duplicate_semantic_home_is_rejected_even_with_unique_xml_ids(self):
        item = self.item(next(iter(self.model.elements)))
        duplicate = copy.deepcopy(item)
        duplicate.set("identifier", "id-" + "0" * 32)
        item.getparent().append(duplicate)
        self.schema.assertValid(self.root)
        self.invalid("exactly one navigation home")

    def test_nested_folder_cannot_authorise_an_extra_composition(self):
        edges = self.root.find("a:relationships", g.N)
        extra = copy.deepcopy(edges[0])
        extra.set("identifier", "id-" + "0" * 32)
        edges.append(extra)
        self.schema.assertValid(self.root)
        self.invalid("Semantic elements, relationships")

    def test_semantic_identity_metadata_documentation_and_endpoints_remain_locked(self):
        for mutation in ("name", "identity", "documentation", "endpoint"):
            self.root = g.ET.fromstring(self.data)
            if mutation == "name": self.root.find("a:elements/a:element/a:name", g.N).text = "Changed architecture"
            elif mutation == "identity": self.root.find("a:elements/a:element", g.N).set("identifier", "id-" + "0" * 32)
            elif mutation == "documentation": self.root.find("a:elements/a:element/a:documentation", g.N).text += " Inferred content"
            else:
                relation = self.root.find("a:relationships/a:relationship", g.N)
                relation.set("source", relation.get("target"))
            with self.subTest(mutation=mutation): self.invalid()

    def test_grandchildren_cannot_leak_into_a_context_view(self):
        key = next(k for k in self.model.elements if any(self.model.children[c] for c in self.model.children[k]))
        grandchild = next(gchild for child in self.model.children[key] for gchild in self.model.children[child])
        identifier = n.detail_id(self.model, key)
        view = self.root.xpath("a:views/a:diagrams/a:view[@identifier=$id]", namespaces=g.N, id=identifier)[0]
        extra = copy.deepcopy(view.find("a:node", g.N))
        extra.set("identifier", g.visual_id(self.model.identifier, "node", identifier, grandchild))
        extra.set("elementRef", grandchild)
        view.insert(list(view).index(view.find("a:node", g.N)), extra)
        self.schema.assertValid(self.root)
        self.invalid("immediate children only")

    def test_view_misplacement_and_retained_view_replacement_are_rejected(self):
        detail = self.report["detailed_views"][0]
        item = self.item(detail["identifier"])
        item.getparent().getparent().append(item)
        self.invalid("outside its context hierarchy")
        self.root = g.ET.fromstring(self.data)
        self.root.find("a:views/a:diagrams/a:view/a:name", g.N).text = "Replaced diagnostic"
        self.invalid("Retained View")

    def test_portable_cli_uses_explicit_governed_source_and_refuses_input_overwrite(self):
        with tempfile.TemporaryDirectory() as tmp:
            repository = Path(tmp) / "repository"
            dest = repository / "docs/archimate/framework"
            published = repository / "docs/archimate/exchange/publish/1.0.4"
            shutil.copytree(BASE, dest, ignore=shutil.ignore_patterns("__pycache__"))
            (repository / "scripts").mkdir()
            shutil.copy2(BASE.parents[2] / "scripts/architecture_release.py", repository / "scripts/architecture_release.py")
            release = repository / "architecture-release.json"
            release.write_text('{"version":"7.8.9","previous_version":"7.8.8"}')
            manifest = json.loads(d.DEFAULT_MAP.read_text())
            source = Path(tmp) / "governed-source.md"
            source.write_bytes((BASE / manifest["source_document"]).read_bytes())
            manifest["source_document"] = "../../../../governed-source.md"
            (dest / "documentation-map.json").write_text(json.dumps(manifest))
            command = [sys.executable, str(dest / "tools/publish_navigation.py")]
            for extra in ([], ["--check"]):
                run = subprocess.run(command + extra, cwd=tmp, capture_output=True, text=True, timeout=30)
                self.assertEqual(0, run.returncode, run.stderr)
            self.assertEqual(self.data, (published / "Harmonia-Capability-Architecture.xml").read_bytes())
            self.assertEqual("7.8.9", json.loads(release.read_text())["version"])
            for flag in ("--output", "--report"):
                run = subprocess.run(command + [flag, str(dest / "baseline/navigation-baseline-1.0.3.json")],
                                     cwd=tmp, capture_output=True, text=True, timeout=30)
                self.assertNotEqual(0, run.returncode)
                self.assertIn("must not overwrite", run.stderr)

    def test_release_report_rejects_missing_failed_or_stale_verification(self):
        release = n.load_release()
        inventory = json.loads((BASE.parents[2] / "docs/markdown/governance/rad-archimate-source-inventory.json").read_text())
        bindings = json.loads((BASE.parents[2] / "docs/markdown/governance/architecture-identity/bindings-validation.json").read_text())
        # These saved records are historical release fixtures. Exercise the
        # renderer using one consistent fixture release after later increments.
        inventory["architecture_release"] = bindings["architecture_release"] = release
        verification = dict(architecture_release=release, status="PASS", checks=[
            dict(key=key, name=key, status="PASS", result="test fixture") for key in sorted(r.REQUIRED_CHECKS)])
        report = r.render(release, self.report, inventory, bindings, verification, self.previous)
        self.assertIn("Human visual acceptance remains pending", report)
        self.assertIn("214 unresolved capability", report)
        for mode in ("missing", "timeout", "stale", "semantic"):
            changed = copy.deepcopy(verification)
            publication = copy.deepcopy(self.report)
            if mode == "missing": changed["checks"].pop()
            elif mode == "timeout": changed["checks"][0]["status"] = "TIMEOUT"
            elif mode == "stale": changed["architecture_release"] = self.previous["architecture_release"]
            else: publication["semantic_changes"]["elements_added"] = 1
            with self.subTest(mode=mode), self.assertRaises(ValueError):
                r.render(release, publication, inventory, bindings, changed, self.previous)

    def test_release_report_rejects_unpinned_or_changed_verified_inputs(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            source = root / "source.md"
            source.write_text("governed fixture")
            evidence = dict(verified_files_sha256={"source.md": hashlib.sha256(source.read_bytes()).hexdigest()})
            r.verify_fingerprints(evidence, {"source.md"}, root)
            with self.assertRaisesRegex(ValueError, "Incomplete"):
                r.verify_fingerprints(evidence, {"source.md", "another.md"}, root)
            source.write_text("changed after verification")
            with self.assertRaisesRegex(ValueError, "Stale verification fingerprint"):
                r.verify_fingerprints(evidence, {"source.md"}, root)


if __name__ == "__main__":
    unittest.main()
