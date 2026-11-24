# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Synthetic perturbations test the probe; they never claim an Archi execution."""
import copy
import json
from pathlib import Path
import subprocess
import sys
import tempfile
import unittest

BASE = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(BASE / "tools"))
import diagnose_roundtrip as d
import generate_views as g

FIXTURE = BASE / "tests/fixtures/roundtrip/future-contract.xml"


class RoundtripTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(prefix="harmonia-roundtrip-test-", dir="/tmp")
        self.addCleanup(self.temp.cleanup)
        self.before = d.Exchange(FIXTURE)
        self.root = copy.deepcopy(self.before.root)

    def incoming(self):
        path = Path(self.temp.name) / "incoming.xml"
        path.write_bytes(g.serialize(self.root))
        return d.Exchange(path)

    def value(self, key="canonicalId", element="test-capability"):
        owner = self.root.find(f"a:elements/a:element[@identifier='{element}']", g.N)
        return self.before.claims(owner, key)[0].find("a:value", g.N)

    def comparison(self):
        return d.compare(self.before, self.incoming())

    def test_valid_uuid32_including_leading_zero_digit_and_alpha(self):
        report = d.diagnose(self.before)
        self.assertEqual("PASS", report["audit_status"])
        self.assertEqual(3, report["canonical_id"]["single_valid_uuid32"])
        self.assertEqual([], report["canonical_id"]["missing"])
        self.assertEqual("CONFORMANT_IN_SNAPSHOT", report["future_identity_readiness"])
        self.assertIn("UNRESOLVED", report["canonical_id"]["lifetime_nonreuse"])

    def test_malformed_uuid32_case_whitespace_prefix_separators_length(self):
        value = self.value().text
        for malformed in (value[:31], value + "0", "id-" + value, " " + value, value + "\n", "urn:uuid:" + value,
                          "00000000-0000-4000-8000-000000000001", "ABCDEF0123454ABC8DEF0123456789AB", "g" * 32, ""):
            with self.subTest(malformed=malformed):
                self.value().text = malformed
                report = d.diagnose(self.incoming())
                self.assertEqual(1, len(report["canonical_id"]["malformed"]))
                self.assertEqual(2, report["canonical_id"]["single_valid_uuid32"])

    def test_duplicate_and_multiple_claims_do_not_pass_uniqueness(self):
        self.value(element="test-feature").text = self.value().text
        report = d.diagnose(self.incoming())
        self.assertEqual(2, len(report["canonical_id"]["duplicates"][self.value().text]))
        self.assertEqual(1, report["canonical_id"]["unique_valid_uuid32_elements"])
        prop = self.value().getparent()
        prop.getparent().append(copy.deepcopy(prop))
        report = d.diagnose(self.incoming())
        self.assertIn("test-capability", report["canonical_id"]["multiple_claims"])

    def test_missing_canonical_id_and_ec_alias_are_distinct(self):
        prop = self.value().getparent()
        prop.getparent().remove(prop)
        definitions = self.root.find("a:propertyDefinitions", g.N)
        definition = g.child(definitions, "propertyDefinition", identifier="test-ec", type="string")
        g.child(definition, "name", d.EC_PROPERTY)
        owner = self.root.find("a:elements/a:element/a:properties", g.N)
        g.child(g.child(owner, "property", propertyDefinitionRef="test-ec"), "value", "EC-01")
        report = d.diagnose(self.incoming())
        self.assertEqual(["test-capability"], report["canonical_id"]["missing"])
        self.assertEqual([], report["canonical_id"]["malformed"])
        self.assertEqual(1, report["historical_ec_aliases"]["count"])

    def test_observed_mutation_and_possible_reuse_are_reported_without_inference(self):
        self.value().text, self.value(element="test-feature").text = self.value(element="test-feature").text, self.value().text
        report = d.diagnose(self.incoming(), previous=self.before)
        findings = report["canonical_id"]["continuity_findings"]
        self.assertEqual(2, len(findings))
        self.assertTrue(any("POSSIBLE REUSE" in f["finding"] for f in findings))
        self.assertEqual(2, len(report['canonical_id']['representation_rebindings']))

    def test_naming_roles_remain_independent_and_display_conflict_is_visible(self):
        report = d.diagnose(self.before)
        self.assertEqual(3, report["naming"]["counts"]["native_display_available"])
        for role in ("displayName", "friendlyName", "canonicalName"):
            with self.subTest(role=role):
                self.root = copy.deepcopy(self.before.root)
                self.value(role).text += " changed"
                comparison = self.comparison()
                self.assertEqual(1, len(comparison["contract_properties"][role]["changes"]))
                other = (set(d.ROLES) - {role})
                self.assertTrue(all(not comparison["contract_properties"][r]["changes"] for r in other))
        self.value("displayName").text = "conflicting label"
        self.assertTrue(d.diagnose(self.incoming())["naming"]["conflicts"])

    def test_feature_is_native_capability_and_type_is_not_identity(self):
        feature = self.root.find("a:elements/a:element[@identifier='test-feature']", g.N)
        feature.set(g.TYPE, "BusinessFunction")
        report = d.diagnose(self.incoming())
        self.assertEqual(3, report["canonical_id"]["single_valid_uuid32"])
        self.assertTrue(any(f["role"] == "harmoniaType" for f in report["naming"]["conflicts"]))
        self.assertGreater(self.comparison()["classifications"][d.SEMANTIC], 0)

    def rekey_definitions(self):
        mapping = {}
        for definition in self.root.findall("a:propertyDefinitions/a:propertyDefinition", g.N):
            old = definition.get("identifier")
            mapping[old] = "propid-rekey-" + old
            definition.set("identifier", mapping[old])
        for prop in self.root.findall(".//a:property", g.N):
            prop.set("propertyDefinitionRef", mapping[prop.get("propertyDefinitionRef")])

    def test_unique_property_definition_rekey_preserves_all_values_and_owners(self):
        self.rekey_definitions()
        report = self.comparison()
        self.assertEqual("EQUIVALENT", report["comparison_status"])
        self.assertEqual(7, report["classifications"][d.EQUIVALENT])
        for role in d.ROLES:
            self.assertEqual([], report["contract_properties"][role]["changes"])
        self.assertEqual([], report["elements"]["changed_records"])

    def test_wrong_owner_association_is_not_hidden_by_rekey(self):
        self.value("canonicalName").text, self.value("canonicalName", "test-feature").text = self.value("canonicalName", "test-feature").text, self.value("canonicalName").text
        self.rekey_definitions()
        report = self.comparison()
        self.assertEqual(2, len(report["contract_properties"]["canonicalName"]["changes"]))
        self.assertGreater(report["classifications"][d.SEMANTIC], 0)

    def test_definition_type_loss_and_duplicate_keys_are_not_normalised(self):
        definition = self.root.find("a:propertyDefinitions/a:propertyDefinition", g.N)
        definition.set("type", "integer")
        self.assertNotEqual("EQUIVALENT", self.comparison()["comparison_status"])
        clone = copy.deepcopy(definition)
        clone.set("identifier", "test-duplicate-key")
        definition.getparent().append(clone)
        report = self.comparison()
        self.assertEqual("UNRESOLVED", report["comparison_status"])
        self.assertTrue(any("Duplicate Property" in x for x in report["input_issues"]["incoming"]))

    def test_ordering_of_semantic_collections_and_properties_is_equivalent(self):
        for path in ("a:elements", "a:relationships", "a:propertyDefinitions", "a:elements/a:element/a:properties"):
            container = self.root.find(path, g.N)
            container[:] = list(reversed(container[:]))
        report = self.comparison()
        self.assertEqual("EQUIVALENT", report["comparison_status"])
        self.assertGreater(report["classifications"][d.EQUIVALENT], 0)

    def test_folder_item_identity_loss_retains_navigation_evidence(self):
        for item in self.root.findall(".//a:item", g.N):
            item.attrib.pop("identifier", None)
        report = self.comparison()
        self.assertEqual(9, report["classifications"][d.LOSS])
        self.assertTrue(all(f["path"].endswith("/@identifier") for f in report["findings"]))
        self.assertEqual([], report["elements"]["changed_records"])

    def test_whitespace_language_and_missing_property_are_visible(self):
        self.value("friendlyName").text = " changed  whitespace "
        self.value("canonicalName").set(g.LANG, "fr")
        prop = self.value("displayName").getparent()
        prop.getparent().remove(prop)
        report = self.comparison()
        self.assertGreater(report["classifications"][d.LOSS], 0)
        self.assertGreater(report["classifications"][d.SEMANTIC], 0)
        self.assertEqual(1, len(report["contract_properties"]["canonicalName"]["changes"]))

    def test_unexplained_presentation_and_unknown_metadata_remain_visible(self):
        node = self.root.find(".//a:node", g.N)
        node.set("x", "41")
        self.root.set("unexpected", "must remain visible")
        report = self.comparison()
        self.assertEqual("UNRESOLVED", report["comparison_status"])
        self.assertGreater(report["classifications"][d.UNKNOWN], 0)
        self.assertEqual(["test-view"], report["presentation"]["changed_records"])

    def test_bendpoint_and_visual_order_not_treated_as_unordered(self):
        connection = self.root.find(".//a:connection", g.N)
        connection[:] = list(reversed(connection[:]))
        view = self.root.find("a:views/a:diagrams/a:view", g.N)
        first = view.find("a:node", g.N)
        view.remove(first)
        view.insert(3, first)
        report = self.comparison()
        self.assertGreater(report["classifications"][d.UNKNOWN], 0)
        self.assertEqual(0, report["classifications"][d.EQUIVALENT])

    def test_relationship_endpoints_and_documentation_are_compared(self):
        relationship = self.root.find("a:relationships/a:relationship", g.N)
        relationship.set("target", "test-capability")
        relationship.find("a:documentation", g.N).text += " changed"
        report = self.comparison()
        self.assertEqual(["test-realization"], report["relationships"]["changed_records"])
        self.assertGreater(report["classifications"][d.SEMANTIC], 0)
        self.assertTrue(any("endpoint mismatch" in x for x in report["input_issues"]["incoming"]))

    def test_serialization_only_changes_and_namespace_prefixes_are_equivalent(self):
        # g.serialize changes XML indentation/declaration, not the data model.
        self.assertEqual("EQUIVALENT", self.comparison()["comparison_status"])
        # A complete prefix rewrite through lxml avoids any string rewrite assumptions.
        root = g.ET.Element(self.root.tag, nsmap={"a": g.NS, "xsi": g.XSI}, **dict(self.root.attrib))
        root.extend(copy.deepcopy(list(self.root)))
        for node in root.iter():
            if node.get(g.TYPE):
                node.set(g.TYPE, "a:" + node.get(g.TYPE))
        self.root = root
        self.assertEqual("EQUIVALENT", self.comparison()["comparison_status"])

    def test_diagnostic_and_comparison_are_deterministic_and_read_only(self):
        original = FIXTURE.read_bytes()
        first = d.encoded(d.diagnose(self.before))
        self.assertEqual(first, d.encoded(d.diagnose(d.Exchange(FIXTURE))))
        self.assertEqual(d.encoded(self.comparison()), d.encoded(self.comparison()))
        self.assertEqual(original, FIXTURE.read_bytes())
        with self.assertRaisesRegex(ValueError, "overwrite an input"):
            d.emit({}, FIXTURE, [FIXTURE])
        with self.assertRaisesRegex(ValueError, "read-only"):
            d.emit({}, d.ACTIVE.parent / "new-output.json", [])

    def test_dtd_rejected_and_unresolved_property_ref_not_silently_accepted(self):
        self.value().getparent().set("propertyDefinitionRef", "does-not-exist")
        self.assertEqual("UNRESOLVED", self.comparison()["comparison_status"])
        path = Path(self.temp.name) / "dtd.xml"
        path.write_text('<!DOCTYPE model [<!ENTITY bad "bad">]><model xmlns="' + g.NS + '" identifier="test-model"/>')
        with self.assertRaisesRegex(ValueError, "DTD"):
            d.Exchange(path)

    def test_missing_real_export_is_explicit_gate(self):
        out = Path(self.temp.name) / "gate.json"
        run = subprocess.run([sys.executable, str(BASE / "tools/diagnose_roundtrip.py"), "compare",
                              "--published", str(FIXTURE), "--incoming", str(Path(self.temp.name) / "absent.xml"),
                              "--output", str(out)], capture_output=True, timeout=10)
        self.assertEqual(3, run.returncode, run.stderr)
        self.assertEqual("AWAITING ARCHI EXPORT", json.loads(out.read_text())["comparison_status"])

    def test_both_sources_are_hash_pinned_and_missing_exports_gate_proof(self):
        manifest = json.loads(d.MANIFEST.read_text())
        for name, fixture in manifest["fixtures"].items():
            fixture["incoming"] = ".junie/reports/" + Path(self.temp.name).name + "/" + name + ".xml"
        path = Path(self.temp.name) / "manifest.json"
        path.write_text(json.dumps(manifest))
        report = d.verify_exports(path)
        self.assertEqual("AWAITING ARCHI EXPORT", report["status"])
        self.assertEqual(2, len(report["probes"]))
        manifest["fixtures"]["future-contract"]["published_sha256"] = "0" * 64
        path.write_text(json.dumps(manifest))
        with self.assertRaisesRegex(ValueError, "Pinned input changed"):
            d.verify_exports(path)

    def test_equivalent_files_do_not_authenticate_real_archi_execution(self):
        manifest = json.loads(d.MANIFEST.read_text())
        relative = str(FIXTURE.relative_to(d.ROOT))
        manifest["fixtures"] = {name: dict(published=relative, incoming=relative, published_sha256=d.sha(FIXTURE))
                                for name in ("release-1.0.6", "future-contract")}
        path = Path(self.temp.name) / "manifest.json"
        path.write_text(json.dumps(manifest))
        report = d.verify_exports(path)
        self.assertEqual("UNRESOLVED", report["status"])
        self.assertEqual("EQUIVALENT", report["probes"]["future-contract"]["comparison_status"])
        with self.assertRaisesRegex(ValueError, "requires --operator"):
            d.verify_exports(path, attest=True)

    def test_empty_leaf_whitespace_is_data_and_unrecognised_valid_metadata_is_compared(self):
        self.value("friendlyName").text = "   "
        metadata = g.ET.Element(g.tag("metadata"))
        self.root.insert(2, metadata)
        report = self.comparison()
        self.assertTrue(any(f["before"] is None and "/metadata" in f["path"] for f in report["findings"]))
        self.assertEqual(1, len(report["contract_properties"]["friendlyName"]["changes"]))
        self.assertEqual(1, d.diagnose(self.incoming())["naming"]["counts"]["friendlyName_property_empty_or_multiple"])

    def test_partial_export_or_missing_visual_identity_cannot_pass(self):
        node = self.root.find(".//a:node", g.N)
        node.getparent().remove(node)
        relationship = self.root.find("a:relationships/a:relationship", g.N)
        relationship.getparent().remove(relationship)
        report = self.comparison()
        self.assertNotEqual("EQUIVALENT", report["comparison_status"])
        self.assertEqual(["test-realization"], report["relationships"]["missing_ids"])
        self.assertEqual([["test-view", "test-capability-node"]],
                         json.loads(d.encoded(report["presentation"]["node_identity"]["missing"])))

    def test_invalid_missing_or_duplicate_element_id_preserves_record_census(self):
        element = self.root.find("a:elements/a:element", g.N)
        element.attrib.pop("identifier")
        clone = copy.deepcopy(self.root.find("a:elements/a:element[@identifier='test-feature']", g.N))
        element.getparent().append(clone)
        incoming = self.incoming()
        self.assertEqual(4, d.diagnose(incoming)["canonical_id"]["total_elements"])
        self.assertEqual("UNRESOLVED", d.compare(self.before, incoming)["comparison_status"])


class AcceptedBaselineDiagnosticTests(unittest.TestCase):
    def test_active_model_coverage_source_standing_and_frozen_ec_bindings(self):
        protected = [d.ACTIVE, d.REGISTRY, d.ROOT / "architecture-release.json"]
        before = {p: d.sha(p) for p in protected}
        model = d.Exchange(d.ACTIVE)
        context = d.baseline_context()
        report = d.diagnose(model, context=context, registry=json.loads(d.REGISTRY.read_text()))
        import information_population as ia
        extra = len(ia.load()['elements']) if ia.active() else 0
        import publish_information_release as information_release
        if information_release.active():
            extra = 86  # 54 original IA instances plus 32 accepted additions.
        # Expected accepted fixture counts are regression assertions; all report
        # values are computed from XML/source indexes, not from these constants.
        self.assertEqual("PASS", report["audit_status"])
        self.assertEqual(1308 + extra, report["canonical_id"]["total_elements"])
        self.assertEqual(1308 + extra, report["canonical_id"]["single_valid_uuid32"])
        self.assertEqual([], report["canonical_id"]["missing"])
        self.assertEqual('CONFORMANT_WITH_GOVERNED_DERIVATION', report['future_naming_readiness'])
        self.assertEqual(0, report['naming']['counts']['canonicalName_property_missing'])
        self.assertEqual([], report['naming']['conflicts'])
        for role in ('displayName', 'friendlyName', 'canonicalName'):
            self.assertEqual(1308 + extra, report['naming']['counts'][role + '_derived_match'])
        self.assertEqual(14, report["historical_ec_aliases"]["count"])
        self.assertEqual([], report["historical_ec_aliases"]["binding_conflicts"])
        self.assertEqual({"Established": 190, "Proposed": 756}, report["naming"]["source_path_standing"])
        self.assertEqual(719, report["legacy_property_coverage"][d.SHORT_NAME])
        self.assertEqual(before, {p: d.sha(p) for p in protected})

    def test_preserved_release_106_still_reports_its_original_identity_gap(self):
        report = d.diagnose(d.Exchange(d.metadata.LEGACY_MODEL), context=d.baseline_context())
        self.assertEqual(0, report['canonical_id']['single_valid_uuid32'])
        self.assertEqual(1299, len(report['canonical_id']['missing']))


if __name__ == "__main__":
    unittest.main()
