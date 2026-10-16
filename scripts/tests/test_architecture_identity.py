# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Identity governance and the bounded retained-EC adoption contract."""
import copy
import hashlib
import json
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import architecture_identity as identity

A = "urn:uuid:00000000-0000-4000-8000-000000000001"
B = "urn:uuid:00000000-0000-4000-8000-000000000002"
R = "urn:uuid:00000000-0000-4000-8000-000000000003"


def evidence():
    source = "docs/markdown/governance/architectural-axioms.md"
    return dict(source_document=source, source_section="# Harmonia Architectural Axioms",
                source_sha256=hashlib.sha256((identity.ROOT / source).read_bytes()).hexdigest(),
                release=identity.load_release()["version"])


def record(key=A, name="Synthetic capability", kind="Capability"):
    return dict(canonical_architecture_id=key, origin="allocated", construct_type=kind,
                name=name, status="active", aliases=[], source_identifiers=[], provenance=[evidence()],
                authority=evidence(), successors=[])


def event(kind, *keys):
    return dict(kind=kind, identities=list(keys), authority=evidence())


def registry(*records):
    return dict(schema_version=1, semantic_namespace=str(identity.SEMANTIC_NAMESPACE),
                records=list(records), events=[event("allocate", r["canonical_architecture_id"]) for r in records])


class ArchitectureIdentityTests(unittest.TestCase):
    def assert_invalid(self, data, fragment, previous=None):
        errors = identity.validate_registry(data, previous)
        self.assertTrue(any(fragment in e for e in errors), errors)

    def test_actual_registry_adopts_only_14_ecs_and_validation_does_not_publish(self):
        path = identity.ROOT / "docs/archimate/capability-architecture/Harmonia-Capability-Architecture.xml"
        before = path.read_bytes()
        data = identity.read_json(identity.DIRECTORY / "registry.json")
        self.assertEqual({f"EC-{n:02d}" for n in range(1, 15)},
                         {r["canonical_architecture_id"] for r in data["records"]})
        previous = identity.read_json(identity.DIRECTORY / "adoption-baseline-1.0.2.json")["previous_registry"]
        self.assertEqual([], identity.validate_registry(data, previous))
        self.assertEqual(before, path.read_bytes())

    def test_retained_ec_uses_exact_locked_uuid_instead_of_rederivation(self):
        r = record("EC-01")
        r["origin"] = "retained"
        manifest = identity.read_json(identity.ROOT / "docs/archimate/capability-architecture/documentation-map.json")
        exact = next(m["element_identifier"] for m in manifest["matches"] if m["reference_id"] == "EC-01")
        r["archimate"] = dict(mode="locked", semantic_id=exact)
        data = registry(r)
        data["events"] = [event("adopt", "EC-01")]
        self.assertEqual([], identity.validate_registry(data))
        self.assertNotEqual(exact, identity.semantic_id("EC-01"))
        r["archimate"]["semantic_id"] = identity.semantic_id("EC-01")
        self.assert_invalid(data, "exact baseline crosswalk")
        r["archimate"]["mode"] = "derived"
        self.assert_invalid(data, "takes precedence")

    def test_navigation_capture_and_unapproved_native_ids_cannot_be_promoted(self):
        for value in ("C01", "F46", "FEAT-EM-01", "capability:person-identity", "CT1-TEST", "id-" + "a" * 32):
            with self.subTest(value=value):
                self.assert_invalid(registry(record(value)), "opaque UUIDv4")
        r = record("F46"); r["origin"] = "retained"
        data = registry(r); data["events"] = [event("adopt", "F46")]
        self.assert_invalid(data, "not an established native")

    def test_global_identity_alias_and_source_collisions(self):
        self.assert_invalid(registry(record(), record()), "canonical identity collision")
        first, second = record(), record(B, "Second")
        first["aliases"] = [B]
        self.assert_invalid(registry(first, second), "identity/alias collision")
        first["aliases"] = ["C01"]
        self.assert_invalid(registry(first), "not a canonical alias")
        first["aliases"] = ["EC-01"]
        self.assert_invalid(registry(first), "another native")
        first["aliases"] = []
        source = dict(kind="source/capture", scope="Business capture", value="function:test", facet="")
        first["source_identifiers"] = [source]; second["source_identifiers"] = [source]
        self.assert_invalid(registry(first, second), "qualified source identifier collision")
        first["source_identifiers"] = [dict(kind="structural", scope="one", value="CT1-TEST", facet="")]
        second["source_identifiers"] = [dict(kind="structural", scope="two", value="CT1-TEST", facet="")]
        self.assert_invalid(registry(first, second), "global structural identifier collision")

    def test_missing_authority_and_source_drift_reject_admission(self):
        data = registry(record())
        data["events"] = []
        self.assert_invalid(data, "allocation/adoption decision")
        data = registry(record())
        data["records"][0]["authority"]["source_sha256"] = "0" * 64
        self.assert_invalid(data, "fingerprint mismatch")
        data["records"][0]["authority"] = evidence()
        data["events"][0]["authority"]["source_document"] = "docs/../legacy.md"
        self.assert_invalid(data, "must identify RADS")

    def test_semantic_derivation_and_namespace_are_locked(self):
        r = record(); r["archimate"] = dict(mode="derived", semantic_id=identity.semantic_id(A))
        data = registry(r)
        self.assertEqual([], identity.validate_registry(data))
        r["archimate"]["semantic_id"] = identity.semantic_id(B)
        self.assert_invalid(data, "derivation mismatch")
        data["semantic_namespace"] = str(identity.uuid.NAMESPACE_URL)
        self.assert_invalid(data, "namespace changed")

    def test_rename_and_structural_relocation_keep_lifetime_and_semantic_ids(self):
        r = record(); r["archimate"] = dict(mode="derived", semantic_id=identity.semantic_id(A))
        before = registry(r)
        after = copy.deepcopy(before)
        after["records"][0]["name"] = "Renamed responsibility"
        after["records"][0]["aliases"] = ["CT1-TEST.FN-OLD"]
        after["events"] += [event("rename", A), event("relocate", A)]
        self.assertEqual([], identity.validate_registry(after, before))
        self.assertEqual(before["records"][0]["archimate"], after["records"][0]["archimate"])
        self.assertEqual(A, after["records"][0]["canonical_architecture_id"])
        after["events"] = before["events"] + [event("provenance", A)]
        self.assert_invalid(after, "specific decision", before)

    def test_history_tombstones_aliases_and_semantic_bindings_cannot_be_rewritten(self):
        r = record(); r["aliases"] = ["CT1-TEST.FN-OLD"]
        r["archimate"] = dict(mode="derived", semantic_id=identity.semantic_id(A))
        before = registry(r)
        mutations = ("remove", "alias", "semantic", "events")
        for mutation in mutations:
            after = copy.deepcopy(before)
            if mutation == "remove": after["records"] = []
            elif mutation == "alias": after["records"][0]["aliases"] = []
            elif mutation == "semantic": after["records"][0]["archimate"]["semantic_id"] = identity.semantic_id(B)
            else: after["events"] = []
            with self.subTest(mutation=mutation):
                self.assertTrue(identity.validate_registry(after, before))

    def test_replacement_retains_predecessor_and_successor_lineage(self):
        before = registry(record())
        after = copy.deepcopy(before)
        after["records"][0].update(status="retired", successors=[B])
        after["records"].append(record(B, "Replacement"))
        after["events"] += [event("allocate", B), event("replace", A, B)]
        self.assertEqual([], identity.validate_registry(after, before))
        later = copy.deepcopy(after); later["records"][0]["status"] = "active"
        later["events"].append(event("rename", A))
        self.assert_invalid(later, "retired identity reactivated", after)

    def test_relationship_requires_exact_endpoints_and_immutable_assertion(self):
        relationship = record(R, "Synthetic assertion", "TraceabilityRelationship")
        relationship["relationship"] = dict(source=A, target=B, predicate="explicit test assertion")
        data = registry(record(), record(B, "Second"), relationship)
        self.assertEqual([], identity.validate_registry(data))
        after = copy.deepcopy(data)
        after["records"][2]["relationship"]["source"] = B
        after["events"].append(event("rename", R))
        self.assert_invalid(after, "immutable relationship", data)
        relationship["relationship"]["target"] = "C01"
        self.assert_invalid(data, "unresolved relationship endpoint")

    def test_unique_ids_do_not_waive_cm_r05_names(self):
        self.assert_invalid(registry(record(), record(B)), "CM-R05")

    def test_successor_lineage_cannot_cycle_or_target_missing_identity(self):
        first, second = record(), record(B, "Second")
        first.update(status="retired", successors=[B])
        second.update(status="retired", successors=[A])
        self.assert_invalid(registry(first, second), "cyclic successor")
        second["successors"] = [R]
        self.assert_invalid(registry(first, second), "invalid successor")

    def test_inventory_retains_collision_and_unresolved_identity_populations(self):
        data = identity.inventory()
        self.assertEqual(0, data["summary"]["allocated_in_this_release"])
        self.assertEqual(53, data["summary"]["native_governed_definitions"])
        self.assertEqual(14, data["summary"]["safe_existing_crosswalks"])
        adopted = {key for event in identity.read_json(identity.DIRECTORY / "registry.json")["events"]
                   if event["kind"] == "adopt" and event["authority"]["release"] == identity.load_release()["version"]
                   for key in event["identities"]}
        self.assertEqual(len(adopted), data["summary"]["adopted_in_this_release"])
        self.assertEqual(14, data["summary"]["registry_records"])
        self.assertEqual(214, data["summary"]["unresolved_exchange_capabilities"])
        self.assertEqual(22, data["summary"]["unresolved_business_assertions"])
        self.assertEqual(2, len(data["collisions"][0]["records"]))
        self.assertEqual(2, len(data["collisions"][1]["records"]))
        self.assertEqual(set(identity.CLASSIFICATIONS), {r["classification"] for r in data["records"]})


class GovernedAdoptionTests(unittest.TestCase):
    def setUp(self):
        self.data = identity.read_json(identity.DIRECTORY / "registry.json")
        self.xml = identity.ROOT / "docs/archimate/capability-architecture/Harmonia-Capability-Architecture.xml"
        self.historical = identity.replay_adoption_publication()

    def test_complete_chain_preserves_previous_publication_and_release(self):
        before, release = self.xml.read_bytes(), identity.load_release()
        proof = identity.verify_adoption(self.data, published=self.historical)
        self.assertEqual(14, proof["identities_adopted"])
        self.assertEqual(0, proof["new_identities_allocated"])
        self.assertEqual({0}, set(proof["changes"].values()))
        self.assertEqual(214, proof["unresolved_capability_identities"])
        self.assertEqual(2467, proof["unresolved_rad_dimensions_relationships"])
        self.assertEqual(14, len(proof["identity_chain"]))
        self.assertEqual({"PASS"}, set(proof["checks"].values()))
        self.assertEqual(before, self.xml.read_bytes())
        self.assertEqual(release, identity.load_release())
        decision = (identity.DIRECTORY / "adoption-1.0.3.md").read_text()
        for row in proof["identity_chain"]:
            self.assertIn(f"| {row['canonical_architecture_id']} | `{row['semantic_id']}` |", decision)

    def test_rejects_missing_ec_or_additional_native_identity(self):
        self.data["records"].pop()
        self.data["events"][0]["identities"].pop()
        with self.assertRaisesRegex(ValueError, "exactly EC-01 through EC-14"):
            identity.verify_adoption(self.data, published=self.historical)
        extra = record("AX-01", "Harmonia Is Health-Information Centric", "Motivation")
        extra["origin"] = "retained"
        self.data = identity.read_json(identity.DIRECTORY / "registry.json")
        self.data["records"].append(extra)
        self.data["events"].append(event("adopt", "AX-01"))
        with self.assertRaisesRegex(ValueError, "exactly EC-01 through EC-14"):
            identity.verify_adoption(self.data, published=self.historical)

    def test_rejects_reallocated_ec_and_changed_or_missing_semantic_binding(self):
        for mode in ("missing", "derived", "other"):
            data = copy.deepcopy(self.data)
            if mode == "missing":
                del data["records"][0]["archimate"]
            elif mode == "derived":
                data["records"][0]["archimate"] = dict(mode="derived", semantic_id=identity.semantic_id("EC-01"))
            else:
                data["records"][0]["archimate"] = data["records"][1]["archimate"]
            with self.subTest(mode=mode), self.assertRaises(ValueError):
                identity.verify_adoption(data, published=self.historical)

    def test_rejects_name_status_alias_or_scoped_source_drift(self):
        for field, value in (("name", "Invented name"), ("status", "deprecated"),
                             ("aliases", [A]), ("source_identifiers", [])):
            data = copy.deepcopy(self.data)
            data["records"][0][field] = value
            with self.subTest(field=field), self.assertRaises(ValueError):
                identity.verify_adoption(data, published=self.historical)

    def test_rejects_wrong_definition_even_with_a_valid_document_fingerprint(self):
        self.data["records"][0]["provenance"] = self.data["records"][1]["provenance"]
        self.assertEqual([], identity.validate_registry(self.data))
        with self.assertRaisesRegex(ValueError, "Exact governed definition/adoption provenance"):
            identity.verify_adoption(self.data, published=self.historical)

    def test_current_provenance_must_be_valid_even_after_history_accepts_it(self):
        self.data["records"][0]["provenance"][0]["source_sha256"] = "0" * 64
        self.assertEqual([], identity.validate_registry(self.data, copy.deepcopy(self.data)))
        with self.assertRaisesRegex(ValueError, "fingerprint mismatch"):
            identity.verify_adoption(self.data, published=self.historical)

    def test_rejects_wrong_or_extra_governance_event(self):
        self.data["events"].append(event("bind", "EC-01"))
        with self.assertRaisesRegex(ValueError, "Only the authorised"):
            identity.verify_adoption(self.data, published=self.historical)

    def test_rejects_semantic_visual_documentation_and_endpoint_changes(self):
        g = identity.views
        for mutation in ("semantic", "visual", "documentation", "endpoint"):
            root = g.ET.fromstring(self.historical)
            if mutation == "semantic":
                root.find("a:elements/a:element/a:name", g.N).text = "Altered capability"
            elif mutation == "visual":
                # An EC has no Composition edges: changing its appearance ID
                # leaves the XSD valid and exercises the visual continuity gate.
                node = root.xpath(".//a:node[@elementRef=$id]", namespaces=g.N,
                                  id=self.data["records"][0]["archimate"]["semantic_id"])[0]
                node.set("identifier", "id-" + "0" * 32)
            elif mutation == "documentation":
                root.find("a:elements/a:element/a:documentation", g.N).text += "\nUngoverned addition"
            else:
                relation = root.find("a:relationships/a:relationship", g.N)
                relation.set("source", relation.get("target"))
            with self.subTest(mutation=mutation), self.assertRaises(ValueError):
                identity.verify_adoption(self.data, published=g.serialize(root))

    def test_current_bindings_survive_navigation_but_reject_semantic_drift(self):
        proof = identity.verify_bindings(self.data)
        self.assertEqual(14, proof["retained_identities"])
        self.assertEqual(14, len(proof["identity_chain"]))
        changed = identity.views.ET.fromstring(self.xml.read_bytes())
        changed.find("a:elements/a:element/a:name", identity.views.N).text = "Changed construct"
        with self.assertRaisesRegex(ValueError, "Current semantic model"):
            identity.verify_bindings(self.data, published=identity.views.serialize(changed))
