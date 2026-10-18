# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Exact identity, source authority and non-destructive Documentation checks."""
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


class DocumentationPublicationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.baseline = g.parse(BASE / "baseline/Harmonia-Capability-Architecture-v2.1a.xml")
        cls.lock = json.loads((BASE / "baseline/semantic-lock.json").read_text())
        cls.schema = g.load_schema(BASE / "schemas/archimate3_Diagram.xsd")
        cls.manifest, cls.mapping = d.load_mapping(d.DEFAULT_MAP, cls.baseline)
        cls.data, cls.report = d.publish(cls.baseline, cls.schema, cls.lock)

    def test_exactly_14_documented_and_214_unchanged_without_documentation(self):
        root = g.ET.fromstring(self.data)
        docs = root.findall("a:elements/a:element/a:documentation", g.N)
        self.assertEqual(14, len(docs))
        self.assertEqual(214, len(root.findall("a:elements/a:element", g.N)) - len(docs))
        for element in root.findall("a:elements/a:element", g.N):
            if element.get("identifier") in self.mapping:
                self.assertEqual(self.mapping[element.get("identifier")], element.findtext("a:documentation", namespaces=g.N))
        d.validate_projection(self.baseline, root, self.mapping, self.schema, self.lock)

    def test_original_baseline_and_lock_survive_and_all_visuals_remain_identical(self):
        root = g.ET.fromstring(self.data)
        self.assertEqual(self.lock["source_sha256"], hashlib.sha256((BASE / "baseline/Harmonia-Capability-Architecture-v2.1a.xml").read_bytes()).hexdigest())
        g.validate_lock(self.baseline, self.lock)
        original_views = g.augment(self.baseline).find("a:views", g.N)
        self.assertEqual(g.signature(original_views), g.signature(root.find("a:views", g.N)))

    def test_full_qualified_descriptions_are_retained(self):
        by_ref = {r["reference_id"]: self.mapping[r["element_identifier"]] for r in self.manifest["matches"]}
        self.assertIn("EC-12 / EC-14 Boundary", by_ref["EC-12"])
        self.assertIn("No strategic logical component, application component", by_ref["EC-14"])
        self.assertIn("not an implementation workflow specification", by_ref["EC-14"])
        self.assertNotIn("## Summary Matrix", by_ref["EC-14"])

    def test_repeated_generation_and_reprojection_are_deterministic(self):
        self.assertEqual(self.data, d.publish(self.baseline, self.schema, self.lock)[0])
        self.assertEqual(self.data, g.serialize(g.augment(g.ET.fromstring(self.data))))

    def test_rejects_documentation_on_unmatched_capability(self):
        root = g.ET.fromstring(self.data)
        element = next(e for e in root.findall("a:elements/a:element", g.N) if e.get("identifier") not in self.mapping)
        element.insert(1, g.ET.Element(g.tag("documentation")))
        with self.assertRaises(g.PublicationError):
            d.validate_projection(self.baseline, root, self.mapping, self.schema, self.lock)

    def test_rejects_description_tampering_missing_or_extra_documentation(self):
        for mutation in ("text", "missing", "extra", "attribute"):
            root = g.ET.fromstring(self.data)
            doc = root.find("a:elements/a:element/a:documentation", g.N)
            if mutation == "text":
                doc.text += " inferred extra architecture"
            elif mutation == "missing":
                doc.getparent().remove(doc)
            elif mutation == "extra":
                doc.getparent().append(copy.deepcopy(doc))
            else:
                doc.set(g.LANG, "fr")
            with self.subTest(mutation=mutation), self.assertRaises(g.PublicationError):
                d.validate_projection(self.baseline, root, self.mapping, self.schema, self.lock)

    def test_rejects_semantic_mutation_even_on_documented_element(self):
        root = g.ET.fromstring(self.data)
        doc = root.find("a:elements/a:element/a:documentation", g.N)
        doc.getparent().find("a:name", g.N).text = "Changed identity label"
        with self.assertRaises(g.PublicationError):
            d.validate_projection(self.baseline, root, self.mapping, self.schema, self.lock)

    def test_rejects_same_name_without_exact_reference(self):
        root = copy.deepcopy(self.baseline)
        element = root.find(f"a:elements/a:element[@identifier='{next(iter(self.mapping))}']", g.N)
        element.find("a:properties/a:property[@propertyDefinitionRef='prop-reference-id']/a:value", g.N).text = "NO-MATCH"
        with self.assertRaisesRegex(g.PublicationError, "Reference ID mismatch"):
            d.load_mapping(d.DEFAULT_MAP, root)

    def test_rejects_duplicate_reference_id(self):
        root = copy.deepcopy(self.baseline)
        documented = root.find(f"a:elements/a:element[@identifier='{next(iter(self.mapping))}']", g.N)
        other = next(e for e in root.findall("a:elements/a:element", g.N) if e.get("identifier") not in self.mapping)
        other.find("a:properties", g.N).append(copy.deepcopy(documented.find("a:properties/a:property[@propertyDefinitionRef='prop-reference-id']", g.N)))
        with self.assertRaisesRegex(g.PublicationError, "Conflicting Reference ID"):
            d.load_mapping(d.DEFAULT_MAP, root)

    def test_cli_portable_with_explicit_baseline_manifest_and_source(self):
        with tempfile.TemporaryDirectory() as tmp:
            repository = Path(tmp) / "repository"
            dest = repository / "docs/archimate/framework"
            published = repository / "docs/archimate/exchange/publish/1.0.4"
            shutil.copytree(BASE, dest, ignore=shutil.ignore_patterns("__pycache__"))
            (repository / "scripts").mkdir()
            shutil.copy2(BASE.parents[2] / "scripts/architecture_release.py", repository / "scripts/architecture_release.py")
            release = repository / "architecture-release.json"
            release.write_text(json.dumps({"version": "7.8.9", "previous_version": "7.8.8"}))
            source = Path(tmp) / "governed-source.md"
            source.write_bytes((d.DEFAULT_MAP.parent / self.manifest["source_document"]).read_bytes())
            manifest = copy.deepcopy(self.manifest)
            # Resolve via a portable relative path, outside the copied publication.
            manifest["source_document"] = "../../../../governed-source.md"
            config = dest / "documentation-map.json"
            config.write_text(json.dumps(manifest))
            command = [sys.executable, str(dest / "tools/publish_documentation.py"), "--baseline", str(dest / "baseline/Harmonia-Capability-Architecture-v2.1a.xml"), "--documentation-map", str(config)]
            for extra in ([], ["--check"]):
                run = subprocess.run(command + extra, cwd=tmp, capture_output=True, text=True, timeout=30)
                self.assertEqual(0, run.returncode, run.stderr)
            report = json.loads((repository / "docs/archimate/processing/publishing/1.0.4/validation.json").read_text())
            self.assertEqual("7.8.9", report["architecture_release"]["version"])
            self.assertEqual("7.8.9", json.loads(release.read_text())["version"])
            # The release affects publication evidence, never semantic/visual identity.
            self.assertEqual(self.data, (published / "Harmonia-Capability-Architecture.xml").read_bytes())
            for output_option in ("--output", "--report"):
                run = subprocess.run(command + [output_option, str(release)], cwd=tmp, capture_output=True, text=True, timeout=30)
                self.assertNotEqual(0, run.returncode)
                self.assertIn("overwrite an input", run.stderr)
            source.write_text(source.read_text() + "\nsource drift\n")
            run = subprocess.run(command + ["--check"], cwd=tmp, capture_output=True, text=True, timeout=30)
            self.assertNotEqual(0, run.returncode)
            self.assertIn("source drift", run.stderr)


if __name__ == "__main__":
    unittest.main()
