# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Synthetic identity fixtures prove governance gates without allocating RAD IDs."""
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

    def test_actual_registry_is_empty_and_xml_is_not_published_by_validation(self):
        path = identity.ROOT / "docs/archimate/capability-architecture/Harmonia-Capability-Architecture.xml"
        before = path.read_bytes()
        data = identity.read_json(identity.DIRECTORY / "registry.json")
        self.assertEqual([], data["records"])
        self.assertEqual([], identity.validate_registry(data))
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
        self.assertEqual(214, data["summary"]["unresolved_exchange_capabilities"])
        self.assertEqual(22, data["summary"]["unresolved_business_assertions"])
        self.assertEqual(2, len(data["collisions"][0]["records"]))
        self.assertEqual(2, len(data["collisions"][1]["records"]))
        self.assertEqual(set(identity.CLASSIFICATIONS), {r["classification"] for r in data["records"]})
