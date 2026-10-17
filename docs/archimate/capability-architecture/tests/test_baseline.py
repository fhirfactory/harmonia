# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Baseline continuity and rejection tests independent of presentation counts."""
import copy
import json
from pathlib import Path
import sys
import unittest
from unittest import mock

BASE = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(BASE / "tools"))
import synchronise_baseline as s
import generate_views as g
import publish_navigation as nav
import recover_v02 as recovery


class BaselineTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.outputs, cls.baseline, cls.report = s.publish()
        cls.data = cls.outputs[s.DEST / "Harmonia-Architecture-Baseline.xml"]

    def root(self):
        return g.ET.fromstring(self.data)

    def reject(self, root, pattern):
        with self.assertRaisesRegex((ValueError, g.ET.DocumentInvalid), pattern):
            s.validate(self.baseline, root)

    def test_schema_all_semantic_ids_bindings_and_original_views_survive(self):
        s.validate(self.baseline, self.root())
        self.assertEqual(14, self.report["adopted_architecture_identities_preserved"])
        old = self.baseline.original
        for path in ("a:elements/a:element", "a:relationships/a:relationship", "a:views/a:diagrams/a:view"):
            self.assertTrue({e.get("identifier") for e in old.findall(path, g.N)} <=
                            {e.get("identifier") for e in self.root().findall(path, g.N)})

    def test_duplicate_capability_or_missing_semantic_element_is_invalid(self):
        root = self.root()
        clone = copy.deepcopy(root.find("a:elements/a:element", g.N))
        clone.set("identifier", "id-accidental-capability-copy")
        root.find("a:elements", g.N).append(clone)
        self.reject(root, "Unexpected/missing semantic element")
        root = self.root()
        elements = root.find("a:elements", g.N)
        elements.remove(elements[-1])
        self.reject(root, "Unexpected/missing|No match|not found|IDREF")

    def test_every_capability_has_one_strategy_home_and_context_folders_are_only_navigation(self):
        root = self.root()
        homes = nav.navigation_homes(root)
        for e in root.findall("a:elements/a:element", g.N):
            if e.get(g.TYPE) == "Capability":
                self.assertEqual(1, len(homes[e.get("identifier")]))
                self.assertEqual("Strategy", homes[e.get("identifier")][0][0])
        self.assertEqual(self.report["before"]["element_types"]["Capability"], self.report["after"]["element_types"]["Capability"])
        paths = self.baseline.folder_paths
        self.assertIn(("Strategy", "Application Layer", "Application Capabilities"), paths)
        self.assertIn(("Business", "Business Capabilities", "[CT1] Care Access & Coordination"), paths)
        self.assertIn(("Application", "Enterprise Capabilities"), paths)

    def test_source_relationship_path_cannot_be_replaced_with_target_path(self):
        root = self.root()
        edge = next(r for r in root.findall("a:relationships/a:relationship", g.N)
                    if r.get("source") != r.get("target") and self.baseline.homes[r.get("source")] != self.baseline.homes[r.get("target")])
        item = root.find(f".//a:item[@identifierRef='{edge.get('identifier')}']", g.N)
        other = root.find(f".//a:item[@identifierRef='{edge.get('target')}']", g.N).getparent()
        item.getparent().remove(item)
        other.append(item)
        self.reject(root, "source-based navigation")

    def test_existing_property_status_or_alias_loss_is_invalid(self):
        root = self.root()
        prop = root.find("a:elements/a:element/a:properties/a:property", g.N)
        prop.find("a:value", g.N).text = "unapproved replacement"
        self.reject(root, "Structured Properties|Properties or Documentation")

    def test_human_first_definition_and_separator_are_enforced(self):
        root = self.root()
        identifier = self.baseline.ids["capability:individual-care-delivery"]
        doc = root.find(f"a:elements/a:element[@identifier='{identifier}']/a:documentation", g.N)
        self.assertTrue(doc.text.startswith("Assess, clinically determine"))
        self.assertNotIn(s.HISTORY, doc.text)
        self.assertIn(self.baseline.records["capability:individual-care-delivery"]["baseline_description"]["Capability Overview"], doc.text)
        self.assertNotIn("Source excerpts", doc.text)
        self.assertNotIn('"derivation":', doc.text)
        reviewed = self.baseline.records["capability:person-identity"]
        reviewed_doc = root.find(f"a:elements/a:element[@identifier='{self.baseline.ids[reviewed['key']]}']/a:documentation", g.N).text
        self.assertIn("Definition — reviewed candidate\n\n" + reviewed["definition"], reviewed_doc)
        doc.text = "Source: some-file\n" + doc.text
        self.reject(root, "Documentation convention")

    def test_candidate_pending_and_historical_states_are_not_promoted(self):
        for key in ("business-capability:health-service-operational-readiness", "capability:health-information-communication"):
            e = self.baseline.records[key]
            self.assertTrue(e["architectural_status"] == "Candidate" or e["record_state"] == "pending_adjudication")
        root = self.root()
        eid = self.baseline.mot_ids["historical:prn_writebehind"]
        prop = root.find(f"a:elements/a:element[@identifier='{eid}']/a:properties/a:property[@propertyDefinitionRef='baseline-record-state']/a:value", g.N)
        prop.text = "current"
        self.reject(root, "status, Properties")

    def test_visual_endpoints_must_match_actual_semantic_endpoints(self):
        root = self.root()
        connection = root.find("a:views/a:diagrams/a:view/a:connection", g.N)
        connection.set("source", connection.get("target"))
        self.reject(root, "View scope|visual identity|visual relationship endpoints")

    def test_empty_domain_folders_and_numeric_view_order_are_enforced(self):
        root = self.root()
        folders = root.find("a:organizations", g.N)
        views = next(x for x in folders if x.findtext("a:label", namespaces=g.N) == "Views")
        self.assertEqual(s.VIEW_DOMAINS, [e.findtext("a:label", namespaces=g.N) for e in views.findall("a:item", g.N)])
        empty = next(x for x in views if x.findtext("a:label", namespaces=g.N) == "4-TechnologyLayer")
        views.remove(empty)
        self.reject(root, "tree integrity")

    def test_native_resource_categories_and_numbered_views_are_separate(self):
        root = self.root()
        def labels(path):
            parent = root.find("a:organizations", g.N)
            for label in path:
                parent = next(e for e in parent.findall("a:item", g.N) if e.findtext("a:label", namespaces=g.N) == label)
            return [e.findtext("a:label", namespaces=g.N) for e in parent.findall("a:item", g.N) if not e.get("identifierRef")]
        self.assertEqual(["Application", "Business", "Motivation", "Relations", "Strategy", "Views"], labels(()))
        for prefix in ((), ("Relations",)):
            self.assertEqual(["Application Layer", "Business Layer"], labels((*prefix, "Strategy"))[:2])
            self.assertEqual(["Business Capabilities", "Business Enabling Capabilities"], labels((*prefix, "Strategy", "Business Layer")))
        self.assertEqual(["Business Capabilities", "Business Enabling Capabilities"], labels(("Business",)))
        self.assertEqual(s.VIEW_DOMAINS, labels(("Views",)))
        self.assertEqual(["1-Business Capabilities", "2-Business Enabling Capabilities", "3-Enterprise Capabilities"], labels(("Views", "1-Strategy", "Capabilities")))
        self.assertEqual("Business Layer", s.navigation_path(["Strategy", "Business Layer"])[-1])
        # Prefixing changes display only: the original folder key is retained.
        strategy = root.find(f".//a:item[@identifier='{self.baseline.ident('folder', 'Strategy')}']", g.N)
        self.assertEqual("Strategy", strategy.findtext("a:label", namespaces=g.N))
        access = self.baseline.records["business-capability:care-access-management"]
        path = self.baseline.display_paths[tuple(self.baseline.capability_path(access))]
        self.assertEqual("[CT2] Care Access Management", path[-1])

    def test_navigation_numeric_order_is_validated_independently_of_folder_membership(self):
        root = self.root()
        org = next(x for x in root.find("a:organizations", g.N) if x.findtext("a:label", namespaces=g.N) == "Views")
        first = org.find("a:item", g.N)
        org.remove(first)
        org.append(first)
        self.reject(root, "numeric navigation sibling order")

    def test_feature_reachability_checked_independently_in_all_four_projections(self):
        feature = self.baseline.records["feature:electronic-endpoint-binding"]
        for raw_path in self.baseline.context_paths(feature):
            root = self.root()
            path = self.baseline.display_paths[tuple(raw_path)]
            parent = root.find("a:organizations", g.N)
            for label in path:
                parent = next(x for x in parent.findall("a:item", g.N) if x.findtext("a:label", namespaces=g.N) == label)
            parent.getparent().remove(parent)
            with self.assertRaisesRegex(ValueError, "Feature.*reachability missing|Feature semantic element missing"):
                s.validate_capability_navigation(self.baseline, root)
        coverage = s.validate_capability_navigation(self.baseline, self.root())
        self.assertEqual(0, coverage["business"]["current_features"])
        self.assertEqual(157, coverage["business_enabling"]["complete_ancestry_reachable"])
        self.assertEqual([], coverage["business_enabling"]["unresolved_ancestry"])

    def test_capability_classification_is_presentation_and_names_stay_governed(self):
        root = self.root()
        for key in ("proposal:bec-healthcare-entity-information-enablement", "capability:provider-administration", "proposal:bec-practitioner-role", "feature:electronic-endpoint-binding"):
            e = self.baseline.records[key]
            actual = root.find(f"a:elements/a:element[@identifier='{self.baseline.ids[key]}']/a:name", g.N).text
            self.assertEqual(e["name"], actual)
        label = root.find(".//a:item[a:label='[CT3] Practitioner Role Administration']/a:label", g.N)
        label.text = "Practitioner Role Administration"
        with self.assertRaisesRegex(ValueError, "Capability tier classification"):
            s.validate_capability_navigation(self.baseline, root)

    def test_incomplete_feature_ancestry_stays_unresolved_without_inferred_ct3(self):
        b = copy.copy(self.baseline)
        b.records = copy.deepcopy(b.records)
        feature = b.records["feature:electronic-endpoint-binding"]
        feature["owner_key"] = "capability:provider-administration"
        self.assertEqual([], b.feature_ancestry(feature))
        self.assertEqual(["Strategy", "Business Layer", "Business Enabling Capabilities", "Unresolved Feature Ancestry", feature["name"]], b.feature_path(feature))
        with self.assertRaisesRegex(ValueError, "Unresolved Feature ancestry must remain visible"):
            s.validate_capability_navigation(b, self.root())

    def test_global_resource_prefixes_and_starting_candidate_mutations_are_rejected(self):
        root = self.root()
        root.find("a:organizations/a:item[a:label='Strategy']/a:label", g.N).text = "1-Strategy"
        with self.assertRaisesRegex(ValueError, "Capability tier classification|resource global"):
            s.validate_capability_navigation(self.baseline, root)
        root = self.root()
        root.find("a:elements/a:element/a:name", g.N).text = "[CT1] mutated semantic name"
        with self.assertRaisesRegex(ValueError, "Starting candidate changed protected"):
            s.validate_projection_preservation(root)

    def test_documentation_definition_overview_intent_boundary_and_history_order(self):
        e = self.baseline.records["business-capability:care-access-management"]
        doc = s.documentation(e, classification=self.baseline.documentation_classification[e["key"]])
        definition = s.r.text_latex(e["governed_definition_latex"])
        self.assertTrue(doc.startswith(definition))
        self.assertLess(doc.index(definition), doc.index(e["baseline_description"]["Capability Overview"]))
        self.assertIn("**Intent**\n\n" + e["baseline_description"]["Intent"], doc)
        self.assertIn("**Boundary**\n\n" + e["baseline_description"]["Boundary"], doc)
        self.assertNotIn(s.HISTORY, doc)
        self.assertEqual("A definition.", s.documentation({"definition": "A definition."}))
        pending = self.baseline.records["capability:health-information-communication"]
        pending_doc = s.documentation(pending, classification=self.baseline.documentation_classification[pending["key"]])
        self.assertIn(s.HISTORY, pending_doc)
        self.assertIn(pending["baseline_description"]["Capability Overview"], pending_doc.split(s.HISTORY)[1])
        for record in self.baseline.motivation["elements"]:
            if record.get("history"):
                actual = self.root().find(f"a:elements/a:element[@identifier='{self.baseline.mot_ids[record['key']]}']/a:documentation", g.N).text
                self.assertEqual(record["documentation"] + s.HISTORY + record["history"], actual)

    def test_documentation_manifest_cannot_promote_unresolved_or_changed_semantics(self):
        manifest = s.read(s.RAD / "documentation-classification.json")
        key = "capability:health-information-communication"
        manifest["entries"][key]["Capability Overview"]["section"] = "current"
        read = s.read
        with mock.patch.object(s, "read", side_effect=lambda p: manifest if str(p).endswith("documentation-classification.json") else read(p)):
            with self.assertRaisesRegex(ValueError, "Unproven Documentation promotion"):
                s.Baseline(*s.load_inputs())

    def test_every_reclassified_field_remains_verbatim_and_status_is_independent(self):
        for key, fields in self.baseline.documentation_classification.items():
            e = self.baseline.records[key]
            doc = s.documentation(e, classification=fields)
            current, _, history = doc.partition(s.HISTORY)
            for title, entry in fields.items():
                text = e["baseline_description"][title]
                self.assertIn(text, current if entry["section"] == "current" else history, key + " / " + title)
        self.assertGreater(self.report["documentation_classification"]["entries_reclassified"], 0)

    def test_earlier_capture_has_exact_key_continuity_and_human_review_notes_survive(self):
        ledger = json.loads(self.outputs[s.RAD / "discrepancies.json"])
        self.assertEqual(ledger["earlier_capture_records"], ledger["exact_key_continuities"])
        self.assertTrue(ledger["deterministically_reconciled_names"])
        self.assertTrue(ledger["human_adjudication_preserved"])
        self.assertEqual(69, self.report["baseline_record_states"]["pending_adjudication"])
        self.assertEqual(32, self.report["baseline_record_states"]["superseded"])
        self.assertEqual(462, self.report["unresolved_source_assertions_retained"])

    def test_current_axioms_reuse_all_five_existing_semantic_ids(self):
        for old in self.baseline.original.findall("a:elements/a:element", g.N):
            if old.get(g.TYPE) == "Principle":
                native = old.findtext("a:name", namespaces=g.N).split()[0]
                self.assertEqual(old.get("identifier"), self.baseline.mot_ids[native])

    def test_markdown_and_archimate_share_exact_definitions_and_standing(self):
        for key, e in self.baseline.records.items():
            md = self.outputs[s.RAD / (s.group(e) + ".md")].decode()
            self.assertIn(f'<a id="{s.anchor(e)}"></a>', md)
            self.assertIn(self.baseline.ids[key], md)
            self.assertIn(e["architectural_status"], md)
            self.assertIn(e["record_state"], md)
        self.assertFalse(self.root().xpath("a:elements/a:element[@xsi:type='ApplicationComponent']", namespaces={**g.N, "xsi": g.XSI}))

    def test_pinned_recovery_and_current_motivation_sources_cannot_drift_silently(self):
        original_sha = s.r.sha
        with mock.patch.object(s.r, "sha", side_effect=lambda p: "0" * 64 if str(p).endswith("Harmonia-Architecture-1.0.5A.xml") else original_sha(p)):
            with self.assertRaisesRegex(ValueError, "Pinned baseline input drift"):
                s.load_inputs()
        with mock.patch.object(s, "section", return_value="Rewritten live definition"):
            with self.assertRaisesRegex(ValueError, "Current Motivation definition differs"):
                s.load_inputs()

    def test_historical_recovery_release_is_pinned_independently_of_live_release(self):
        self.assertEqual("1.0.5", recovery.load_release(recovery.RELEASE)["version"])
        self.assertEqual(self.baseline.manifest["starting_release"], recovery.load_release(recovery.RELEASE)["version"])

    def test_independent_generation_preserves_every_output_byte_and_does_not_bump_release(self):
        before = (s.ROOT / "architecture-release.json").read_bytes()
        again, _, _ = s.publish()
        self.assertEqual(self.outputs, again)
        self.assertEqual(before, (s.ROOT / "architecture-release.json").read_bytes())

    def test_person_identity_feature_context_survives_all_three_projections(self):
        b, root = self.baseline, self.root()
        feature = "feature:governed-identity-correction"
        function = "function:apply-governed-person-identity-correction"
        process = "process:governed-person-identity-correction-process"
        service = "service:person-identity-correction"
        for key in (function, process, service):
            self.assertEqual({feature}, set(b.feature_associations[key]))
            self.assertIn("[Feature] Governed Identity Correction", nav.navigation_homes(root)[b.ids[key]][0])
        self.assertEqual([11, 278], b.feature_associations[process][feature])
        contexts = [v for v, x in b.layout_groups.items() if x["context"] == "capability:person-identity"]
        self.assertEqual(1, len(contexts))
        self.assertEqual({function, process, service}, set(b.layout_groups[contexts[0]]["groups"][feature]))
        self.assertNotIn("[Feature]", "/".join(nav.navigation_homes(root)[contexts[0]][0]))
        s.fp.validate(b, root)

    def test_resource_feature_association_cannot_be_flattened_or_invented(self):
        root, b = self.root(), self.baseline
        key = "function:resolve-person-identifier"
        item = root.find(f".//a:item[@identifierRef='{b.ids[key]}']", g.N)
        parent = item.getparent()
        parent.remove(item)
        parent.getparent().getparent().append(item)
        with self.assertRaisesRegex(ValueError, "missing from Feature resource"):
            s.fp.validate(b, root)
        root = self.root()
        key = next(x["key"] for x in self.report["human_inspection_projection"]["no_established_feature_association"])
        item = root.find(f".//a:item[@identifierRef='{b.ids[key]}']", g.N)
        target = root.find(".//a:item[a:label='[Feature] Identifier Resolution']", g.N)
        item.getparent().remove(item); target.append(item)
        with self.assertRaisesRegex(ValueError, "False.*Feature resource"):
            s.fp.validate(b, root)

    def test_shared_and_unresolved_assertions_do_not_select_a_preferred_feature(self):
        b = self.baseline
        self.assertEqual(2, len(b.feature_associations["function:manage-theatre-case-progression"]))
        self.assertNotIn("[Feature]", "/".join(b.behaviour_path(b.records["function:manage-theatre-case-progression"])))
        for edge in b.unresolved_feature_associations:
            self.assertNotIn(edge["to"], b.feature_associations.get(edge["from"], {}))
        # No arbitrary transitive link through a participating Function or Service.
        self.assertNotIn("process:assurance-process-design", b.feature_associations)

    def test_realisation_views_stay_with_their_exact_feature_subject(self):
        root, b = self.root(), self.baseline
        vid = next(v for v, x in b.crosswalk["views"].items() if x["role"].startswith("assertion:") and
                   x["keys"][1] == "feature:identifier-resolution")
        item = root.find(f".//a:item[@identifierRef='{vid}']", g.N)
        parent = item.getparent(); parent.remove(item); parent.getparent().append(item)
        with self.assertRaisesRegex(ValueError, "realisation View outside Feature"):
            s.fp.validate(b, root)

    def test_landscapes_have_only_direct_children_and_are_first_views(self):
        b = self.baseline
        counts = self.report["human_inspection_projection"]["landscapes_by_taxonomy_and_child_level"]
        self.assertEqual(0, counts["business"].get("Feature", 0))
        for level in ("CT1", "CT2", "CT3", "Feature"):
            vid = next(v for v, x in b.landscapes.items() if x["level"] == level)
            root = self.root()
            item = root.find(f".//a:item[@identifierRef='{vid}']", g.N)
            parent = item.getparent(); parent.remove(item)
            with self.assertRaisesRegex(ValueError, "Landscape outside|not first"):
                s.fp.validate(b, root)
            root = self.root()
            view = root.find(f"a:views/a:diagrams/a:view[@identifier='{vid}']", g.N)
            node = copy.deepcopy(view.find("a:node", g.N))
            node.set("identifier", "id-invented-landscape-population")
            node.set("elementRef", b.ids["feature:identifier-resolution"])
            view.append(node)
            with self.assertRaisesRegex(ValueError, "only context and all immediate"):
                s.fp.validate(b, root)
        vid = next(v for v, x in b.landscapes.items() if x["context"] == "capability:person-identity" and x["path"][1] == "1-Strategy")
        root = self.root(); item = root.find(f".//a:item[@identifierRef='{vid}']", g.N)
        parent = item.getparent(); parent.remove(item); parent.append(item)
        with self.assertRaisesRegex(ValueError, "not first View"):
            s.fp.validate(b, root)

    def test_business_layout_rejects_flattening_and_overlap(self):
        b, root = self.baseline, self.root()
        vid = next(v for v, x in b.layout_groups.items() if x["context"] == "capability:person-identity")
        node = root.find(f"a:views/a:diagrams/a:view[@identifier='{vid}']/a:node[@elementRef='{b.ids['function:resolve-person-identifier']}']", g.N)
        node.set("x", "9999")
        with self.assertRaisesRegex(ValueError, "not grouped beneath Feature"):
            s.fp.validate(b, root)

    def test_landscape_display_order_survives_archi_name_sorting(self):
        b, root = self.baseline, self.root()
        vid = next(v for v, x in b.landscapes.items() if x["context"] == "capability:person-identity" and x["path"][1] == "2-BusinessLayer")
        name = root.find(f"a:views/a:diagrams/a:view[@identifier='{vid}']/a:name", g.N)
        self.assertEqual("0-Person Identity Administration — Feature Landscape", name.text)
        name.text = name.text.removeprefix("0-")
        with self.assertRaisesRegex(ValueError, "does not sort first"):
            s.fp.validate(b, root)


if __name__ == "__main__":
    unittest.main()
