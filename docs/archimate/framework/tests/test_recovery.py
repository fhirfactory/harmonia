# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Snapshot recovery acceptance and rejection gates, independent of frozen counts."""
import collections
import copy
import json
from pathlib import Path
import sys
import tempfile
import unittest
from unittest import mock

BASE = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(BASE / "tools"))
import recover_v02 as r
import generate_views as g


class RecoveryTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.manifest, cls.source, cls.source_model, cls.original, cls.bindings = r.load_inputs()
        cls.recovery = r.Recovery(cls.source_model, cls.original, cls.bindings)
        cls.data = g.serialize(cls.recovery.build(cls.source))
        cls.schema = g.load_schema(BASE / "schemas/archimate3_Diagram.xsd")

    def root(self):
        return g.ET.fromstring(self.data)

    def reject(self, root, fragment):
        with self.assertRaisesRegex((g.PublicationError, g.ET.DocumentInvalid), fragment):
            r.validate(self.recovery, root, self.schema)

    def test_schema_references_navigation_and_retained_semantics(self):
        result = r.validate(self.recovery, self.root(), self.schema)
        self.assertGreater(result["elements"], len(self.source_model["elements"]))
        self.assertGreater(result["views"], 219)

    def test_complete_record_type_and_status_coverage_without_name_merging(self):
        root = self.root()
        self.assertEqual(len(self.source_model["elements"]), len(self.recovery.ids))
        for key, e in self.recovery.records.items():
            actual = root.find(f"a:elements/a:element[@identifier='{self.recovery.ids[key]}']", g.N)
            props = {p.get("propertyDefinitionRef"): p.findtext("a:value", namespaces=g.N)
                     for p in actual.findall("a:properties/a:property", g.N)}
            self.assertEqual(e, json.loads(props["recovery-snapshot-record-json"]))
            self.assertEqual(e["architectural_status"], props["recovery-architectural-status"])
            self.assertEqual(e["record_state"], props["recovery-record-state"])
            self.assertEqual(r.TYPES[e["type"]], actual.get(g.TYPE))
            self.assertTrue(actual.findtext("a:documentation", namespaces=g.N))
        self.assertEqual(14, len(set(self.recovery.ids.values()) & set(g.Model(self.original).elements)))

    def test_care_access_has_four_established_and_separate_proposed_ct2_and_deeper_children(self):
        records = self.recovery.records
        key = next(k for k, e in records.items() if e["reference_id"] == "HCM-BC-care-access-coordination")
        children = [e for e in records.values() if e["parent_key"] == key]
        self.assertEqual(collections.Counter({"Canonical": 4, "Proposed": 1}),
                         collections.Counter(e["architectural_status"] for e in children))
        self.assertEqual({"Care Access Management": 3, "Care Scheduling & Capacity Management": 6,
                          "Care Coordination & Transition": 3, "Care Support Logistics Management": 3,
                          "Service Delivery Preparedness Management": 1},
                         {e["name"]: sum(c["parent_key"] == e["key"] for c in records.values()) for e in children})
        candidate = next(e for e in records.values() if e["name"] == "Health Service Operational Readiness")
        self.assertEqual("Candidate", candidate["architectural_status"])
        self.assertEqual("Proposed", candidate["placement_status"])
        self.assertEqual("proposal:bc-service-delivery-preparedness-management", candidate["parent_key"])

    def test_every_relationship_is_native_or_explicitly_accounted_for(self):
        recovery = self.recovery
        accounted = [x["source_index"] for x in [*recovery.relation_map, *recovery.findings]]
        self.assertEqual(list(range(len(self.source_model["relationships"]))), sorted(accounted))
        for entry in recovery.relation_map:
            self.assertNotEqual("unresolved", entry["source_assertion"]["status"])
            self.assertIn(entry["source_assertion"]["type"], r.RELATIONS)
            source = recovery.ids[entry["source_assertion"]["from"]]
            source_home = recovery.homes[source]
            self.assertEqual(["Relations", "Capabilities" if source_home[0] == "Strategy" else source_home[0],
                              *source_home[1:]], recovery.homes[entry["identifier"]])
        self.assertTrue(all(x["reason"] and x["source_assertion"] for x in recovery.findings))
        self.assertTrue(any(x["record_state"] == "preserved historical assertion" for x in recovery.relation_map))

    def test_feature_framing_is_not_manufactured_composition(self):
        features = {k for k, e in self.recovery.records.items() if e["type"] == "feature"}
        for edge in self.recovery.relation_map:
            if edge["archimate_type"] == "Composition":
                self.assertNotIn(edge["source_assertion"]["to"], features)
        self.assertTrue(any(s["role"].startswith("features:") for s in self.recovery.view_scopes.values()))

    def test_only_proven_ec_architecture_id_properties_and_no_new_registry_allocation(self):
        with mock.patch("architecture_identity.validate_registry", side_effect=AssertionError("Live RAD validation must remain separate")):
            r.load_inputs()
        root = self.root()
        props = root.xpath("a:elements/a:element/a:properties/a:property[@propertyDefinitionRef='recovery-canonical-architecture-id']/a:value/text()", namespaces=g.N)
        self.assertEqual(set(self.bindings), set(props))
        self.assertEqual(14, len(props))
        original_ids = set(g.Model(self.original).elements)
        unresolved = original_ids - set(self.bindings.values())
        self.assertEqual(214, len(unresolved))
        self.assertTrue(all("Unreconciled 1.0.4 Reference" in self.recovery.homes[x] for x in unresolved))

    def test_source_appendices_are_all_inspectable_and_corpus_register_is_not_semantic_input(self):
        self.assertEqual({str(p.relative_to(r.ROOT)) for p in (self.source / "sections").glob("*.tex")},
                         {e["path"] for e in self.recovery.evidence_files})
        self.assertEqual({"AX-01", "AX-04", "AX-14", "AX-17", "AX-18"}, {
            e.findtext("a:name", namespaces=g.N).split()[0] for e in self.recovery.result.findall("a:elements/a:element", g.N)
            if e.get(g.TYPE) == "Principle"})
        self.assertEqual(set(r.TYPES.values()) | {"Principle"}, {
            e.get(g.TYPE) for e in self.recovery.result.findall("a:elements/a:element", g.N)})

    def test_pinned_source_drift_or_post_snapshot_edits_are_rejected(self):
        manifest = copy.deepcopy(self.manifest)
        manifest["files_sha256"][str((self.source / "model.json").relative_to(r.ROOT))] = "0" * 64
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "manifest.json"
            path.write_text(json.dumps(manifest))
            with self.assertRaisesRegex(ValueError, "Pinned recovery source drift"):
                r.load_inputs(path)

    def test_candidate_promotion_and_definition_loss_are_rejected(self):
        key = "proposal:bc-service-delivery-preparedness-management"
        identifier = self.recovery.ids[key]
        root = self.root()
        element = root.find(f"a:elements/a:element[@identifier='{identifier}']", g.N)
        element.find("a:properties/a:property[@propertyDefinitionRef='recovery-architectural-status']/a:value", g.N).text = "Canonical"
        self.reject(root, "standing or Documentation")
        root = self.root()
        root.find(f"a:elements/a:element[@identifier='{identifier}']/a:documentation", g.N).text = "Replacement name-derived summary"
        self.reject(root, "standing or Documentation")

    def test_speculative_graph_edge_is_rejected_even_when_schema_valid(self):
        root = self.root()
        key = next(iter(self.recovery.ids.values()))
        edge = g.child(root.find("a:relationships", g.N), "relationship", identifier="id-speculative", source=key, target=key)
        edge.set(g.TYPE, "Association")
        self.reject(root, "Unexpected/missing relationship")

    def test_grandchild_leaking_into_context_view_is_rejected(self):
        root = self.root()
        spec_id = next(i for i, s in self.recovery.view_scopes.items() if s["role"] == "context:HCM-BC-care-access-coordination")
        view = root.find(f"a:views/a:diagrams/a:view[@identifier='{spec_id}']", g.N)
        node = copy.deepcopy(view.find("a:node", g.N))
        node.set("identifier", "id-grandchild-leak")
        node.set("elementRef", self.recovery.ids["business-capability:health-service-operational-readiness"])
        connection = view.find("a:connection", g.N)
        view.insert(list(view).index(connection) if connection is not None else len(view), node)
        self.reject(root, "View scope")

    def test_documentation_decoder_preserves_nested_text_and_links(self):
        value = r.text_latex(r"\textbf{Boundary}: \texttt{\seqsplit{Pragma}} \href{https://example.org}{source} \allowbreak{}kept")
        self.assertEqual("Boundary: Pragma source (https://example.org) kept", value)

    def test_representation_and_context_folder_ids_survive_renames_and_relocation(self):
        model = copy.deepcopy(self.source_model)
        key = "proposal:bc-service-delivery-preparedness-management"
        entry = next(e for e in model["elements"] if e["key"] == key)
        entry["name"] = "Renamed solely for representation identity probe"
        renamed = r.Recovery(model, self.original, self.bindings)
        renamed.build(self.source)
        self.assertEqual(self.recovery.ids, renamed.ids)
        before_path = self.recovery.homes[self.recovery.ids[key]]
        after_path = renamed.homes[renamed.ids[key]]
        self.assertEqual(self.recovery.folders[tuple(before_path)].get("identifier"),
                         renamed.folders[tuple(after_path)].get("identifier"))
        entry["parent_key"] = "capability:individual-care-delivery"
        moved = r.Recovery(model, self.original, self.bindings)
        moved.build(self.source)
        moved_path = moved.homes[moved.ids[key]]
        self.assertEqual(renamed.folders[tuple(after_path)].get("identifier"),
                         moved.folders[tuple(moved_path)].get("identifier"))

    def test_independent_regeneration_is_byte_identical(self):
        other = r.Recovery(self.source_model, self.original, self.bindings)
        self.assertEqual(self.data, g.serialize(other.build(self.source)))


if __name__ == "__main__":
    unittest.main()
