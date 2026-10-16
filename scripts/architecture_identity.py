#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Inventory architectural identity evidence and validate the reviewed identity contract.

No allocation or semantic publication command is provided.
"""
import argparse
import collections
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys
import uuid

from architecture_release import load_release, version_parts
import rads_business_architecture as business

ROOT = Path(__file__).resolve().parents[1]
DIRECTORY = ROOT / "docs/markdown/governance/architecture-identity"
SEMANTIC_NAMESPACE = uuid.uuid5(uuid.NAMESPACE_URL, "urn:harmonia:architecture:semantic:v1")
CLASSIFICATIONS = (
    "identity already governed", "safe deterministic migration", "requires identity allocation",
    "identity conflict", "requires architectural adjudication",
    "documentary/non-architectural and therefore does not require canonical architecture identity",
)
GOVERNED, SAFE, ALLOCATE, CONFLICT, ADJUDICATE, DOCUMENTARY = CLASSIFICATIONS
sys.path.insert(0, str(ROOT / "docs/archimate/capability-architecture/tools"))
import assess_rad
import generate_views as views
import publish_documentation as documentation


def read_json(path):
    return business.load_json(path)


def semantic_id(canonical_id):
    """Type, name, path, release, implementation and View are absent from the key."""
    key = json.dumps(["HarmoniaArchitecture", canonical_id], separators=(",", ":"))
    return "id-" + uuid.uuid5(SEMANTIC_NAMESPACE, key).hex


def baseline_crosswalk(root):
    base = Path(root) / "docs/archimate/capability-architecture"
    baseline = views.parse(base / "baseline/Harmonia-Capability-Architecture-v2.1a.xml")
    views.validate_lock(baseline, read_json(base / "baseline/semantic-lock.json"))
    manifest, _ = documentation.load_mapping(base / "documentation-map.json", baseline)
    return baseline, manifest


def accepted_registry(path):
    relative = str(Path(path).resolve().relative_to(ROOT))
    result = subprocess.run(["git", "show", f"HEAD:{relative}"], cwd=ROOT,
                            capture_output=True, text=True, timeout=10)
    if result.returncode:
        # A new registry is permitted only if the accepted tree has no such file.
        exists = subprocess.run(["git", "ls-tree", "HEAD", "--", relative], cwd=ROOT,
                                capture_output=True, text=True, check=True, timeout=10)
        if exists.stdout:
            raise ValueError("Cannot read accepted identity history")
        return None
    return json.loads(result.stdout)


def native_definitions(root):
    selectors = {
        "governance/architectural-axioms.md": (r"^### (AX-\d+)\s+[—–-]+\s+(.+)$", "Motivation/Principle"),
        "02-strategy/capabilities/enterprise-capabilities.md": (r"^### (EC-\d+): (.+)$", "Capability/Enterprise"),
        "01-motivation/requirements-constraints/foundational-requirements.md": (r"^## (REQ-FND-\d+): (.+)$", "Motivation/Requirement"),
        "01-motivation/requirements-constraints/external-constraints.md": (r"^## (CST-EXT-\d+): (.+)$", "Motivation/Constraint"),
        "02-strategy/courses-of-action/strategic-courses-of-action.md": (r"^### (COA-\d+): (.+)$", "Strategy/CourseOfAction"),
        "02-strategy/resources/strategic-resources.md": (r"^### (SR-\d+): (.+)$", "Strategy/Resource"),
        "02-strategy/strategic-views/strategic-value-streams.md": (r"^### (VS-\d+): (.+)$", "Strategy/ValueStream"),
    }
    result = []
    for relative, (pattern, kind) in selectors.items():
        path = Path(root) / "docs/markdown" / relative
        for number, line in enumerate(path.read_text().splitlines(), 1):
            match = re.match(pattern, line)
            if match:
                result.append(dict(identifier=match[1], name=match[2], construct_type=kind,
                                   source_document=str(path.relative_to(root)), source_line=number))
    return result


def validate_registry(data, previous=None, root=ROOT):
    errors = business.schema_errors(data, read_json(DIRECTORY / "registry.schema.json"))
    if errors:
        return errors
    if data["semantic_namespace"] != str(SEMANTIC_NAMESPACE):
        errors.append("semantic namespace changed")
    records, owners, semantic_owners, source_owners = {}, {}, {}, {}
    native_records = native_definitions(root)
    natives = {r["identifier"]: r for r in native_records}
    if len(natives) != len(native_records):
        errors.append("ambiguous native definition identity collision")
    published = views.parse(Path(root) / "docs/archimate/capability-architecture/Harmonia-Capability-Architecture.xml")
    reserved_xml_ids = {e.get("identifier") for e in published.iter() if e.get("identifier")}
    _, manifest = baseline_crosswalk(root)
    locked_matches = {m["reference_id"]: m["element_identifier"] for m in manifest["matches"]}
    prior_evidence = [] if previous is None else [e for r in previous["records"] for e in [r["authority"], *r["provenance"]]] + [e["authority"] for e in previous["events"]]

    def evidence_check(evidence):
        path = Path(evidence["source_document"])
        if path.is_absolute() or ".." in path.parts or not str(path).startswith("docs/markdown/"):
            errors.append("authority/provenance must identify RADS")
        elif evidence in prior_evidence:
            pass  # Accepted fingerprint/path/release remain historical provenance.
        elif not (Path(root) / path).is_file():
            errors.append(f"missing authoritative source: {path}")
        else:
            raw = (Path(root) / path).read_bytes()
            if hashlib.sha256(raw).hexdigest() != evidence["source_sha256"]:
                errors.append(f"source evidence fingerprint mismatch: {path}")
            if evidence["source_section"] not in raw.decode("utf-8"):
                errors.append(f"source evidence section missing: {path}")
        if version_parts(evidence["release"]) > version_parts(load_release()["version"]):
            errors.append("evidence claims a future acceptance release")

    def opaque(value):
        try:
            parsed = uuid.UUID(value.removeprefix("urn:uuid:"))
            return parsed.version == 4 and value == "urn:uuid:" + str(parsed)
        except ValueError:
            return False

    def claim(table, key, owner, label):
        if key in table:
            errors.append(f"{label} collision: {key}")
        table[key] = owner

    for record in data["records"]:
        identity = record["canonical_architecture_id"]
        claim(records, identity, record, "canonical identity")
        claim(owners, identity, identity, "identity/alias")
        if record["origin"] == "retained":
            if identity not in natives:
                errors.append(f"not an established native identity: {identity}")
            elif record["construct_type"] != natives[identity]["construct_type"].split("/")[0]:
                errors.append(f"native identity type mismatch: {identity}")
        else:
            if not opaque(identity):
                errors.append(f"new identity must be an approved opaque UUIDv4 URN: {identity}")
        for alias in record["aliases"]:
            claim(owners, alias, identity, "identity/alias")
            if alias in natives:
                errors.append(f"cannot capture another native governed identity as alias: {alias}")
            if alias not in natives and not opaque(alias) and not business.CANONICAL.fullmatch(alias):
                errors.append(f"navigation/local identifier is not a canonical alias: {alias}")
        for source in record["source_identifiers"]:
            key = tuple(source[k] for k in ("kind", "scope", "value", "facet"))
            claim(source_owners, key, identity, "qualified source identifier")
            if source["kind"] == "structural":
                value = source["value"]
                if not business.CANONICAL.fullmatch(value):
                    errors.append(f"invalid Domain03 structural identifier: {value}")
                if value in owners and owners[value] != identity:
                    errors.append(f"global structural identifier collision: {value}")
                owners[value] = identity
        for evidence in [record["authority"], *record["provenance"]]:
            evidence_check(evidence)
        binding = record.get("archimate")
        if binding:
            claim(semantic_owners, binding["semantic_id"], identity, "semantic identifier")
            if identity in locked_matches and (binding["mode"] != "locked" or binding["semantic_id"] != locked_matches[identity]):
                errors.append(f"existing exact baseline crosswalk takes precedence over derivation: {identity}")
            if binding["mode"] == "derived" and binding["semantic_id"] != semantic_id(identity):
                errors.append(f"semantic ID derivation mismatch: {identity}")
            if binding["mode"] == "derived" and binding["semantic_id"] in reserved_xml_ids:
                errors.append(f"semantic ID collides with existing model identifier: {identity}")
            if binding["mode"] == "locked":
                # Only the exact, previously demonstrated EC crosswalk may be
                # adopted without a separately reviewed new lock mechanism.
                if locked_matches.get(identity) != binding["semantic_id"]:
                    errors.append(f"locked binding lacks exact baseline crosswalk: {identity}")
        relation = record.get("relationship")
        if (record["construct_type"] == "TraceabilityRelationship") != bool(relation):
            errors.append(f"relationship identity requires its typed assertion: {identity}")
    for identity, record in records.items():
        admission = "adopt" if record["origin"] == "retained" else "allocate"
        if not any(e["kind"] == admission and identity in e["identities"] for e in data["events"]):
            errors.append(f"identity lacks allocation/adoption decision: {identity}")
        for successor in record["successors"]:
            if successor not in records or successor == identity:
                errors.append(f"invalid successor: {identity}")
        if record["successors"] and record["status"] == "active":
            errors.append(f"superseded identity cannot remain active: {identity}")
        relation = record.get("relationship")
        if relation and any(relation[endpoint] not in records for endpoint in ("source", "target")):
            errors.append(f"unresolved relationship endpoint: {identity}")
    names = collections.Counter(r["name"] for r in records.values() if r["construct_type"] == "Capability")
    if any(n > 1 for n in names.values()):
        errors.append("CM-R05 duplicate Capability name; identity uniqueness cannot waive it")
    def visit(identity, trail):
        if identity in trail:
            errors.append("cyclic successor lineage")
            return
        if identity in records:
            for successor in records[identity]["successors"]:
                visit(successor, trail | {identity})
    for identity in records:
        visit(identity, set())
    # Tombstones, aliases and locked IDs are append-only across an accepted history.
    if previous is not None:
        if previous["semantic_namespace"] != data["semantic_namespace"]:
            errors.append("semantic namespace history changed")
        old_events = previous["events"]
        if data["events"][:len(old_events)] != old_events:
            errors.append("identity history was rewritten")
        new_events = data["events"][len(old_events):]
        old = {r["canonical_architecture_id"]: r for r in previous["records"]}
        for identity, prior in old.items():
            current = records.get(identity)
            if current is None:
                errors.append(f"identity removed/rekeyed instead of retained: {identity}")
                continue
            for key in ("origin", "archimate", "relationship"):
                if prior.get(key) is not None and current.get(key) != prior.get(key):
                    errors.append(f"immutable {key} changed: {identity}")
            if prior.get("archimate") is None and current.get("archimate") is not None and not any(
                    e["kind"] == "bind" and identity in e["identities"] for e in new_events):
                errors.append(f"new semantic binding lacks bind decision: {identity}")
            if not set(prior["aliases"]) <= set(current["aliases"]):
                errors.append(f"alias removed/reused: {identity}")
            if any(s not in current["source_identifiers"] for s in prior["source_identifiers"]):
                errors.append(f"source identifier history removed: {identity}")
            if prior["status"] == "retired" and current["status"] != "retired":
                errors.append(f"retired identity reactivated: {identity}")
            if prior != current and not any(identity in e["identities"] for e in new_events):
                errors.append(f"changed identity lacks a new governance event: {identity}")
            changes = {"name": {"rename"}, "construct_type": {"reclassify"},
                       "status": {"deprecate", "retire", "replace", "merge", "split"},
                       "successors": {"replace", "merge", "split"},
                       "aliases": {"relocate", "adopt"}, "source_identifiers": {"relocate", "adopt", "provenance"},
                       "provenance": {"provenance"}, "authority": {"provenance"}}
            for field, kinds in changes.items():
                if prior[field] != current[field] and not any(e["kind"] in kinds and identity in e["identities"] for e in new_events):
                    errors.append(f"{field} change lacks its specific decision: {identity}")
        for identity in records.keys() - old.keys():
            if not any(e["kind"] in ("allocate", "adopt") and identity in e["identities"] for e in new_events):
                errors.append(f"identity lacks allocation/adoption decision: {identity}")
    for event in data["events"]:
        evidence_check(event["authority"])
        if any(i not in records for i in event["identities"]):
            errors.append("event references unknown identity")
    return errors


def verify_adoption(data, root=ROOT, published=None):
    """Prove the bounded 1.0.3 adoption against frozen, accepted 1.0.2 evidence.

    Recheck admission provenance even after commit: accepted historical evidence
    alone cannot establish that the current definition/Documentation still agrees.
    No allocation, registry mutation or XML enrichment occurs here.
    """
    root = Path(root)
    directory = root / DIRECTORY.relative_to(ROOT)
    base = root / "docs/archimate/capability-architecture"
    prior = read_json(directory / "adoption-baseline-1.0.2.json")
    views.require(prior["architecture_release"] == {"version": "1.0.2", "previous_version": "1.0.1"}
                  and prior["previous_registry"]["records"] == []
                  and prior["previous_registry"]["events"] == [], "Invalid pre-adoption baseline")
    errors = validate_registry(data, prior["previous_registry"], root)
    views.require(not errors, "; ".join(errors))
    baseline, manifest = baseline_crosswalk(root)
    expected = {m["reference_id"]: m for m in manifest["matches"]}
    records = {r["canonical_architecture_id"]: r for r in data["records"]}
    views.require(len(data["records"]) == 14 and set(records) == set(expected),
                  "Adoption requires exactly EC-01 through EC-14; no other identities")
    decision = directory / "adoption-1.0.3.md"
    authority = dict(source_document=str(decision.relative_to(root)), source_section="## Adoption decision",
                     source_sha256=hashlib.sha256(decision.read_bytes()).hexdigest(), release="1.0.3")
    views.require(data["events"] == [dict(kind="adopt", identities=list(expected), authority=authority)],
                  "Only the authorised 14-identity adoption event is permitted")
    path = base / "Harmonia-Capability-Architecture.xml"
    raw = path.read_bytes() if published is None else published
    result = views.ET.fromstring(raw)
    schema = views.load_schema(base / "schemas/archimate3_Diagram.xsd")
    lock = read_json(base / "baseline/semantic-lock.json")
    _, mapping = documentation.load_mapping(base / "documentation-map.json", baseline)
    publication = documentation.validate_projection(baseline, result, mapping, schema, lock)
    chain = []
    for key, match in expected.items():
        record = records[key]
        provenance = dict(source_document=manifest["rad_source_document"], source_section=match["source_section"],
                          source_sha256=manifest["source_sha256"], release="1.0.3")
        views.require(record["provenance"] == [provenance] and record["authority"] == authority,
                      f"Exact governed definition/adoption provenance required: {key}")
        views.require(record["name"] == match["source_name"] and record["origin"] == "retained"
                      and record["construct_type"] == "Capability" and record["status"] == "active"
                      and record["successors"] == [] and record["aliases"] == [],
                      f"Adopted name, lifecycle, native identity or aliases changed: {key}")
        views.require(record["source_identifiers"] == [dict(kind="source/capture", scope=manifest["rad_source_document"],
                                                           value=key, facet="")],
                      f"Exact qualified native definition locator required: {key}")
        semantic = match["element_identifier"]
        views.require(record.get("archimate") == dict(mode="locked", semantic_id=semantic),
                      f"Existing locked semantic binding required: {key}")
        elements = result.xpath("a:elements/a:element[@identifier=$id]", namespaces=views.N, id=semantic)
        views.require(len(elements) == 1, f"Adopted identity must resolve to one published semantic element: {key}")
        reference_owners = result.xpath(
            "a:elements/a:element[a:properties/a:property[@propertyDefinitionRef='prop-reference-id']/a:value=$ref]",
            namespaces=views.N, ref=key)
        views.require(reference_owners == elements, f"Published native Reference-ID binding drift: {key}")
        native = [n for n in native_definitions(root) if n["identifier"] == key]
        views.require(len(native) == 1 and native[0]["source_document"] == provenance["source_document"]
                      and native[0]["name"] == record["name"], f"Ambiguous governed RAD construct: {key}")
        chain.append(dict(canonical_architecture_id=key, **provenance,
                          semantic_id=semantic, binding_mode="locked", status=record["status"],
                          documentation_sha256=match["documentation_sha256"]))
    views.require(views.semantic_snapshot(result)["protected_content_sha256"] == prior["semantic_content_sha256"],
                  "Semantic identities, capability hierarchy/content or relationship endpoints changed since 1.0.2")
    views.require(views.digest(views.signature(result.find("a:views", views.N))) == prior["visual_content_sha256"],
                  "Visual identities, references or View content changed since 1.0.2")
    for relative, digest in prior["files_sha256"].items():
        content = raw if root / relative == path else (root / relative).read_bytes()
        views.require(hashlib.sha256(content).hexdigest() == digest, f"1.0.2 preservation fingerprint changed: {relative}")
    regenerated, _ = documentation.publish(baseline, schema, lock, base / "documentation-map.json",
                                           root / "architecture-release.json")
    repeated, _ = documentation.publish(baseline, schema, lock, base / "documentation-map.json",
                                        root / "architecture-release.json")
    views.require(raw == regenerated == repeated, "Publication differs from deterministic regeneration")
    schema.assertValid(views.ET.fromstring(raw))
    assessment = assess_rad.inventory(root, manifest)
    assertions = {k: v for k, v in assessment.items() if k not in ("architecture_release", "repository_head", "source_files")}
    views.require(views.digest(assertions) == prior["rad_assertions_sha256"], "RAD assertions or unresolved findings changed")
    census = inventory(root)
    views.require(views.digest(census["collisions"]) == prior["identity_collisions_sha256"],
                  "Existing F46/CM-R05 collision evidence changed")
    capture = read_json(root / "docs/markdown/03-business-architecture/reference-model/business-architecture.json")
    invalid, unresolved = business.validate(capture, root)
    views.require(not invalid and len(unresolved) == 2467, "RAD validation or unresolved dimensions changed")
    views.require(publication["semantic_elements"] == 228 and publication["semantic_relationships"] == 189
                  and len(publication["generated_views"]) == 4 and publication["retained_views"] == 1,
                  "Publication population changed")
    return dict(status="PASS", architecture_release=load_release(root / "architecture-release.json"),
                previous_publication_release=prior["architecture_release"]["version"],
                baseline_commit=prior["repository_commit"], identities_adopted=14, new_identities_allocated=0,
                changes={k: 0 for k in ("semantic_identity", "visual_identity", "capability_semantics",
                                        "capability_hierarchy", "relationships", "relationship_endpoints", "documentation")},
                unresolved_capability_identities=214, unresolved_rad_dimensions_relationships=len(unresolved),
                semantic_elements=228, composition_relationships=189, generated_views=4, retained_views=1,
                xml_sha256=hashlib.sha256(raw).hexdigest(), identity_chain=chain,
                checks={k: "PASS" for k in ("registry_schema_history_and_identity_alias_uniqueness",
                    "exactly_14_retained_ec_adoptions_no_allocations", "one_governed_definition_per_identity",
                    "one_existing_semantic_element_per_identity", "exact_source_and_decision_provenance_hashes",
                    "native_names_lifecycle_and_source_identifiers", "original_semantic_lock_and_relationship_endpoints",
                    "same_visual_identities_views_and_references", "same_14_governed_documentation_sections",
                    "byte_identical_to_release_1_0_2", "deterministic_generation", "archi_5_9_xsd",
                    "source_assertions_f46_and_other_findings_preserved", "rad_validation_2467_unresolved")})


def inventory(root=ROOT):
    root = Path(root)
    baseline, manifest = baseline_crosswalk(root)
    capture = assess_rad.inventory(root, manifest)
    records = []

    def add(population, kind, source_key, name, classification, reason, **extra):
        if "source_document" not in extra:
            extra["source_document"] = ("docs/archimate/capability-architecture/baseline/Harmonia-Capability-Architecture-v2.1a.xml"
                                        if population == "Proven Exchange" else "docs/markdown/03-business-architecture/reference-model/business-architecture.json")
        records.append(dict(population=population, construct_type=kind, source_capture_id=source_key,
                            name=name, classification=classification, reason=reason, **extra))

    for record in capture["business_elements"]:
        kind = record["type"].title()
        classification = CONFLICT if "name_uniqueness" in record["unresolved"] else ADJUDICATE if record["status"] != "established" else ALLOCATE
        add("Business capture", kind, record["key"], record["name"], classification,
            "Capture key/local token is not a persistent Canonical Architecture ID; structural canonical_id remains unresolved.",
            existing_canonical_id=record["canonical_id"], local_id=record.get("local_id"),
            unresolved=record["unresolved"], standing=record["status"], sources=record["sources"])
    native = native_definitions(root)
    for record in native:
        add("Native governed definitions", record["construct_type"], record["identifier"], record["name"], GOVERNED,
            "Existing exact definition ID retained verbatim; registry adoption and publication require separate explicit decisions.",
            existing_governed_id=record["identifier"], source_document=record["source_document"], source_line=record["source_line"])
    master = root / "docs/markdown/01-motivation/requirements-constraints/master-requirements-catalogue.md"
    for number, line in enumerate(master.read_text().splitlines(), 1):
        if line.startswith("|") and "Candidate" in line:
            cells = [c.strip() for c in line.strip("|").split("|")]
            add("Candidate requirement navigation", "Motivation candidate/bundle", cells[0].strip("*"), cells[1], ADJUDICATE,
                "Navigation index explicitly defers owning-domain reconciliation; ranges and labels do not allocate individual identities.",
                source_document=str(master.relative_to(root)), source_line=number)
    for relative, pattern, kind in (
        ("05-information-systems-architecture/gaps.md", r"^\| \*\*(CDG-\d+)\s+[—–-]+\s+(.+)$", "Gap navigation"),
        ("05-information-systems-architecture/sources.md", r"^\| <a id=\"[^\"]+\"></a>([AU]\d+) \| (.+)$", "Decision/source evidence"),
    ):
        path = root / "docs/markdown" / relative
        for number, line in enumerate(path.read_text().splitlines(), 1):
            match = re.match(pattern, line)
            if match:
                add("Governance navigation", kind, match[1], match[2], DOCUMENTARY,
                    "Governed source/review navigation identity; no automatic architectural element or Migration Gap.",
                    source_document=str(path.relative_to(root)), source_line=number)
    for relative, title, classification in (
        ("04-information-architecture/patterns/metabole.md", "Metabolē", ALLOCATE),
        ("04-information-architecture/metamodel/information-architecture-metamodel.md", "Information Unit", ADJUDICATE),
        ("04-information-architecture/patterns/metabole.md", "ActivityContext", ADJUDICATE),
    ):
        add("Information patterns/metamodel", "Information definition/pattern", None, title, classification,
            "Governed meaning exists; distinguish architectural concept/pattern/type from instances before registry adoption; no ID allocated.",
            source_document="docs/markdown/" + relative)
    # Other selected Motivation definitions have names/ordinals, not allocated IDs.
    for record in capture["motivation_strategy_section_inventory"]:
        if re.match(r"^(AX-|REQ-FND-|CST-EXT-|COA-|SR-|VS-)", record["heading"]):
            continue
        add("Motivation definition", record["proposed_archimate_type"], None, record["heading"], ALLOCATE,
            "Numbered definition heading is a locator, not identity.", source_document=record["source_document"], source_line=record["source_line"])
    for record in capture["business_participant_definitions"]:
        add("Business participants", record["proposed_archimate_type"], None, record.get("name", record.get("heading")), ALLOCATE,
            "Governed participant definition lacks allocated identity; no actor instance inferred.",
            source_document=record["source_document"], source_line=record["source_line"])
    for record in capture["actor_categories_not_instances"]:
        add("Actor categories", "Actor category", None, record["heading"], DOCUMENTARY,
            "Type/category definition is not an individual governed Actor instance.")
    for record in capture["information_family_heading_candidates"]:
        add("Information review candidates", "Information/Data candidate", None, record["heading"], ADJUDICATE,
            "Heading includes concepts, facets and repeated/qualified assertions; determine construct identity before allocation.",
            source_document=record["source_document"], source_line=record["source_line"])
    navigation_counts = collections.Counter((r["source_document"], r["source_key"]) for r in capture["application_register_rows"])
    for record in capture["application_register_rows"]:
        key = record["source_key"]
        classification = (CONFLICT if navigation_counts[(record["source_document"], key)] > 1 else DOCUMENTARY if key[0] in "FM" or record["classification"] == "Documentation only"
                          else ADJUDICATE if record["classification"] == "Requires architectural adjudication" else ALLOCATE)
        add("Domain05 register", record["proposed_archimate_type"] or "Mixed/documentary bundle", key, record["label"], classification,
            "Navigation key only. " + record["explanation"], source_document=record["source_document"], source_line=record["source_line"], standing=record["standing"])
    for index, record in enumerate(capture["business_relationship_assertions"]):
        add("Business assertion", "TraceabilityRelationship", f"relationships/{index}", record["type"],
            ADJUDICATE if record["status"] != "established" else ALLOCATE,
            "JSON pointer locates an assertion; it allocates no identity or publication edge.",
            source_endpoint=record["from"], target_endpoint=record["to"], standing=record["status"], sources=record["sources"])
    for record in capture["catalogue_contexts"]:
        add("Catalogue context", "Responsibility description", record["key"], record["name"], DOCUMENTARY if record["element"] else ADJUDICATE,
            "Explicit link reuses its existing capture element." if record["element"] else "Standalone Capability existence is unresolved; do not allocate it as a Capability.")
    for record in capture["presentation_groups"]:
        add("Presentation", "Presentation grouping", record["key"], record["name"], DOCUMENTARY,
            "Region/natural view is neither a Capability nor a tier.")
    model = views.Model(baseline)
    matches = {m["element_identifier"]: m for m in manifest["matches"]}
    for identifier, element in model.elements.items():
        add("Proven Exchange", "Capability publication", identifier, element.findtext("a:name", namespaces=views.N), SAFE if identifier in matches else ADJUDICATE,
            "Exact EC Reference-ID/UUID crosswalk; may adopt its existing locked ID after review, without new semantic identity." if identifier in matches else "Semantic publication identity exists; no exact RAD identity crosswalk. No name matching.",
            semantic_id=identifier, existing_governed_id=matches[identifier]["reference_id"] if identifier in matches else None)
    for identifier, relation in model.relationships.items():
        add("Proven Exchange", "Composition publication", identifier, "Composition", ADJUDICATE,
            "Existing locked relationship remains; canonical endpoints/assertion crosswalk not established.",
            semantic_id=identifier, source_endpoint=relation.get("source"), target_endpoint=relation.get("target"))
    # Navigation index of EVERY observed digit-bearing ID-shaped token. Repeated
    # references are never treated as allocations or collisions by this scanner.
    observations = collections.defaultdict(list)
    sources = []
    for path in sorted((root / "docs/markdown").rglob("*")):
        if not path.is_file() or "architecture-identity" in path.parts or path.name == "rad-archimate-source-inventory.json":
            continue
        relative = str(path.relative_to(root))
        sources.append(dict(path=relative, sha256=hashlib.sha256(path.read_bytes()).hexdigest()))
        if path.suffix != ".md":
            continue
        for number, line in enumerate(path.read_text().splitlines(), 1):
            tokens = set(re.findall(r"\b(?:[A-Z][A-Z0-9]*(?:-[A-Z0-9]+)+|[A-Z]{1,3}\d{1,3})\b", line))
            for token in sorted(tokens):
                if any(c.isdigit() for c in token):
                    observations[token].append(dict(source_document=relative, source_line=number,
                                                    standing="candidate/scratchpad" if "scratchpad" in path.parts else "assertion-specific; inspect source"))
    counts = collections.Counter((r["population"], r["construct_type"], r["classification"]) for r in records)
    collisions = []
    for (source, key), count in sorted(navigation_counts.items()):
        if count > 1:
            collisions.append(dict(kind="navigation identity collision", identifier=key, population="Domain05 register",
                                   source_document=source, records=[r for r in records if r["population"] == "Domain05 register" and r["source_capture_id"] == key], resolution="unresolved; no renumber/merge"))
    capability_names = collections.defaultdict(list)
    for record in records:
        if record["population"] == "Business capture" and record["construct_type"] == "Capability" or record["population"] == "Native governed definitions" and record["construct_type"] == "Capability/Enterprise":
            capability_names[record["name"]].append(record)
    for name, owners in sorted(capability_names.items()):
        if len(owners) > 1:
            collisions.append(dict(kind="CM-R05 name conflict", identifier=name, population="Governed Capability definitions", records=owners,
                                   resolution="unresolved; separate identities preserved"))
    native_counts = collections.Counter(r["identifier"] for r in native)
    for identifier, count in native_counts.items():
        if count > 1:
            collisions.append(dict(kind="native definition identity collision", identifier=identifier, count=count, resolution="requires review"))
    legacy = []
    for relative in ("docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/identity-lock.json",
                     "docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-identity-history.json"):
        path = root / relative
        if path.is_file():
            data = read_json(path)
            legacy.append(dict(path=relative, sha256=hashlib.sha256(path.read_bytes()).hexdigest(),
                               top_level_entries=len(data), authority="legacy publication lock/history; not RAD identity allocation authority",
                               disposition="preserve unchanged; no HCM/path-to-Exchange crosswalk inferred"))
    registry_data = read_json(DIRECTORY / "registry.json") if (DIRECTORY / "registry.json").exists() else {"events": [], "records": []}
    release = load_release()
    allocated = {key for e in registry_data["events"] if e["kind"] == "allocate" and e["authority"]["release"] == release["version"] for key in e["identities"]}
    adopted = {key for e in registry_data["events"] if e["kind"] == "adopt" and e["authority"]["release"] == release["version"] for key in e["identities"]}
    return dict(architecture_release=release, purpose="Identity evidence inventory; no allocation or semantic-generation authority; populations overlap",
                records=records, collisions=collisions, source_fingerprints=sources,
                legacy_publication_identity_mechanisms=legacy,
                observed_identifier_tokens=[dict(token=k, occurrences=v, role="navigation evidence only; definition/reference distinction requires source review") for k, v in sorted(observations.items())],
                coverage=[dict(population=p, construct_type=t, classification=c, count=n) for (p,t,c),n in sorted(counts.items())],
                summary=dict(record_count=len(records), native_governed_definitions=len(native), safe_existing_crosswalks=len(matches),
                             unresolved_exchange_capabilities=len(model.elements)-len(matches), allocated_in_this_release=len(allocated),
                             adopted_in_this_release=len(adopted),
                             registry_records=len(registry_data["records"]),
                             existing_structural_canonical_ids=capture["summary"]["canonical_ids_allocated_in_capture"],
                             unresolved_business_assertions=capture["summary"]["unresolved_relationship_assertions"],
                             technology_platform="No accepted first-class identity catalogue in inspected RADS; count unestablished",
                             implementation_migration="No governed WorkPackage/Plateau/Gap identity catalogue; CDG/release/plans are not automatically such elements"))


def report(data):
    release = data["architecture_release"]
    rows = "\n".join(f"| {r['population']} | {r['construct_type']} | {r['classification']} | {r['count']} |" for r in data["coverage"])
    return f"""<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Architecture identity inventory and collision report

Release {release['version']} (previous {release['previous_version']}); values read from
the root release authority. **{data['summary']['adopted_in_this_release']} existing identities adopted;
{data['summary']['allocated_in_this_release']} new identities allocated in this release.**
This is an evidence inventory, not an architecture model.

The [machine inventory](inventory.json) contains every inspected record, source
locator, classification, reason and collision evidence. Its populations overlap:
the same EC appears in its RAD definition and Exchange projection. Do not sum these
counts into an architectural element total. Source/JSON pointers are audit locators.

| Population | Architectural construct / record type | Classification | Records |
| --- | --- | --- | ---: |
{rows}

## Collisions and migration candidates

- F46 identifies both Omilia operations/client experience and Shared Security and
  Assurance Nomenclature in the same navigation table. This is an unresolved source
  identity collision, not authority to merge two architectural constructs.
- CM-R05 still requires global Capability name uniqueness. Health Information
  Exchange is used for separate BC/BEC responsibilities. Both keys/meanings remain;
  namespace qualification cannot waive the naming rule. Name equality never proves
  identity. The existing RAD validator retains its explicit uncertainty.
- The 14 EC Reference-ID/Exchange UUID crosswalks are the proven adoption population;
  [Release 1.0.3](adoption-1.0.3.md) adopts them into the common register with their
  exact native IDs and locked semantic bindings. They are not new allocations.
  Other native AX/REQ-FND/CST-EXT/COA/SR/VS IDs
  already govern their definitions; publication mappings still need individual review.
- 214 baseline Capabilities and all 189 baseline Composition assertion crosswalks
  remain unresolved. Semantic XML IDs are locked and retained, not promoted to RAD IDs.
- All 705 Business full structural Canonical IDs remain unallocated. The 137 local
  FEAT tokens retain spelling/owner evidence; neither their missing CT3 frames nor
  full ancestry is filled. Missing ancestry does not erase established responsibility.
- C/F/D/M and CDG keys remain navigation. Qualified source bindings may locate
  evidence after approved allocation, but must not become canonical IDs automatically.
- Repeated IDs in references, historical source wrappers and scratchpad examples
  are indexed as observations, not counted as collisions between constructs.
  Candidate requirements and ranges in the master navigation catalogue require
  owning-domain review; a range is not a set of allocated individual identities.
- Information headings, type categories, implementation names, model/FHIR instance
  identifiers and local review/question keys are not automatically architecture IDs.
  Domain05 candidate standing/deferrals and all 2,467 unresolved RAD dimensions are
  retained. Technology/Migration counts remain unestablished, not zero architecture.

## Scope limits and recommendation

This bounded census reuses the reviewed Business capture and definition selectors,
then indexes ID-shaped occurrences across RADS. It is not an exhaustive semantic
interpretation of every prose statement. Mixed Information and C/D rows remain
adjudication candidates; named roles are definitions, not inferred Actor instances.
The token index also exposes scoped rule/decision/review IDs (e.g. DN-BC, CM-R,
B1/B2, K, CDG and candidate requirement keys) as documentary evidence, without
mechanically publishing them as Motivation or Migration elements.

The [adoption decision](adoption-1.0.3.md) bounds this release to the 14 existing
EC identities. Their admission and publication continuity are checked against the
frozen [1.0.2 evidence](adoption-baseline-1.0.2.json). The classifications above
describe the source evidence; safe crosswalks do not authorise additional adoption.
Stop for human review. The 214 baseline crosswalks require separately authorised
batches before expanding publication. No bulk allocation or additional domain
publication is authorised here.
"""


def release_report(data, verification):
    release = load_release()
    if data["architecture_release"] != release or verification["architecture_release"] != release:
        raise ValueError("Stale identity release evidence")
    if any(check["status"] != "PASS" for check in verification["checks"]):
        raise ValueError("Identity verification incomplete")
    for path, digest in verification["verified_files_sha256"].items():
        if hashlib.sha256((ROOT / path).read_bytes()).hexdigest() != digest:
            raise ValueError(f"Stale release verification: {path}")
    adoption = verify_adoption(read_json(DIRECTORY / "registry.json"))
    if verification["adoption"] != adoption:
        raise ValueError("Stale adoption verification")
    checks = "\n".join(f"| {c['name']} | {c['status']} — {c['result']} |" for c in verification["checks"])
    files = "\n".join(f"- `{p}`" for p in verification["files_changed"])
    return f"""<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# First governed identity adoption — Release {release['version']}

Prepared for review: **{release['previous_version']} → {release['version']}**, incremented
with `scripts/architecture_release.py bump --expect {release['previous_version']}`.
The [root authority](../../../../architecture-release.json) remains the sole current
release value. Maven is unchanged. No commit or new GUI verification is claimed.
The owner confirmed the committed Release 1.0.1 publication was visually verified
and the 14 exact EC Documentation fields display correctly in Archi.

## Adopted identity chain and release counts

The [adoption decision](adoption-1.0.3.md) records the owner's bounded authorisation.
The [register](registry.json) adopts only EC-01–EC-14, retaining their governed
native IDs as persistent Architecture Identities, exact authoritative definition
provenance, qualified source locators, active lifecycle status and existing locked
semantic UUIDs. No historical aliases were needed. No IDs are rederived from EC
strings, and the XML gains no properties. The [1.0.2 identity model](model.md),
schema and immutable publication lock retain their meaning.

[adoption-validation.json](adoption-validation.json) proves each chain:
governed RAD definition → retained Architecture Identity → common register →
locked deterministic semantic binding → existing published element, with its
unchanged governed Documentation and View references. The
[frozen 1.0.2 snapshot](adoption-baseline-1.0.2.json) retains pre-adoption evidence
from commit `{adoption['baseline_commit']}`; validation does not rely on a moving HEAD
to establish publication continuity after commit.

| Measure | Expected | Observed |
| --- | ---: | ---: |
| Identities adopted | 14 | {adoption['identities_adopted']} |
| New identities allocated | 0 | {adoption['new_identities_allocated']} |
| Semantic identity changes | 0 | {adoption['changes']['semantic_identity']} |
| Visual identity changes | 0 | {adoption['changes']['visual_identity']} |
| Capability semantic changes | 0 | {adoption['changes']['capability_semantics']} |
| Capability hierarchy changes | 0 | {adoption['changes']['capability_hierarchy']} |
| Relationship changes / endpoint changes | 0 / 0 | {adoption['changes']['relationships']} / {adoption['changes']['relationship_endpoints']} |
| Documentation changes | 0 | {adoption['changes']['documentation']} |
| Unresolved Capability identities remaining | 214 | {adoption['unresolved_capability_identities']} |

The [inventory](inventory.md) still identifies 53 native governed definition IDs;
only the 14 authorised EC bindings are registered. Populations overlap and are
not a total architecture census.

## Collisions, uncertainty and retained findings

F46 remains an unresolved navigation-key collision between Omilia operations/client
experience and Shared Security and Assurance Nomenclature. Health Information
Exchange remains a CM-R05 name conflict between separate BC/BEC responsibilities.
Neither is repaired, merged or renamed. All 705 full structural Canonical IDs remain
unallocated; the 137 local Feature tokens retain their spelling and missing ancestry.
C/F/D/M/CDG keys remain navigation. The 22 unresolved captured assertions, all 189
baseline Composition crosswalks, Domain05 candidate standing and deferrals, and
**2,467 unresolved RAD dimensions/relationships** remain unchanged.

Information family candidates need construct/type adjudication; numbered Motivation
definitions need allocation; candidate requirement navigation rows require owning-
domain review. Technology/Platform and Implementation/Migration identity counts are
unestablished in inspected RADS. Legacy HCM/LaTeX identities and runtime/FHIR IDs
remain distinct evidence, not authority to assign RAD handles. Repeated reference
tokens are not counted as identity collisions.

## Validation

| Check | Result |
| --- | --- |
{checks}

The original 228 capabilities, 189 Composition relationships, four generated Views,
retained import diagnostic and 14 Documentation fields are byte-identical to the
committed Release 1.0.2 publication. Its SHA-256 is `{verification['xml_sha256']}`.
The generated artefact is
[Harmonia-Capability-Architecture.xml](../../../archimate/capability-architecture/Harmonia-Capability-Architecture.xml).
The 1.0.1 Documentation report and verification
snapshot remain historical; this report records the identity release's results.
No additional Archi import is required to establish byte preservation; no new
visual inspection is claimed. Later semantic changes still require Archi review.

## Files added or changed

{files}

Authoritative Capability definitions and all source architectural assertions
remain intact. The immutable baseline fixture/semantic lock and legacy
publication locks are unchanged. [verification.json](verification.json) pins the
verified files and results. Commands are bounded; the validator reports missing
history/authority as failures rather than inventing identities.

## Unexpected findings and review stop

No unexpected architectural or publication finding was established. All pre-existing
findings above remain explicit. This release stops after the 14 adoptions,
regeneration and validation for human review. The remaining 214 Capabilities,
F46 adjudication, Domain03 identity migration, opaque allocation, further domains
and additional semantic constructs require separate authorisation.

## Canonical Documentation Assessment

The existing Exchange baseline, semantic lock and Documentation manifest outside
RADS supply the already reviewed publication-binding evidence referenced by the
governed mapping and identity model. No new architectural knowledge was imported
from legacy documentation. The authoritative Strategy EC definitions stay in
RADS; this task records only their authorised identity adoption and continuity.
No legacy reconciliation or domain completion is claimed.
"""


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("action", choices=("inventory", "validate", "adoption", "report"))
    parser.add_argument("--check", action="store_true")
    parser.add_argument("--registry", type=Path, default=DIRECTORY / "registry.json")
    parser.add_argument("--previous-registry", type=Path)
    args = parser.parse_args()
    try:
        if args.action == "validate":
            data = read_json(args.registry)
            previous = read_json(args.previous_registry) if args.previous_registry else accepted_registry(args.registry)
            errors = validate_registry(data, previous)
            if errors:
                raise ValueError("; ".join(errors))
            print(json.dumps(dict(status="PASS", architecture_release=load_release(), registry_records=len(data["records"]),
                                  note="Validation does not authorise allocation or publish semantics"), indent=2))
        elif args.action == "adoption":
            result = verify_adoption(read_json(args.registry))
            path = DIRECTORY / "adoption-validation.json"
            value = json.dumps(result, indent=2) + "\n"
            if args.check:
                if path.read_text() != value:
                    raise ValueError("Stale adoption verification")
            else:
                path.write_text(value)
            print(json.dumps({k: v for k, v in result.items() if k != "identity_chain"}, indent=2))
        elif args.action == "report":
            data = read_json(DIRECTORY / "inventory.json")
            value = release_report(data, read_json(DIRECTORY / "verification.json"))
            path = DIRECTORY / "release-report.md"
            if args.check:
                if path.read_text() != value:
                    raise ValueError("Stale identity release report")
            else:
                path.write_text(value)
            print(f"PASS: identity release {load_release()['version']} report")
        else:
            data = inventory()
            outputs = {DIRECTORY / "inventory.json": json.dumps(data, indent=2, ensure_ascii=False) + "\n",
                       DIRECTORY / "inventory.md": report(data)}
            for path, value in outputs.items():
                if args.check:
                    if path.read_text() != value:
                        raise ValueError(f"Stale identity inventory: {path.name}")
                else:
                    path.write_text(value)
            print(json.dumps(data["summary"], indent=2))
        return 0
    except (OSError, ValueError, KeyError, views.ET.LxmlError) as exc:
        print(f"FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
