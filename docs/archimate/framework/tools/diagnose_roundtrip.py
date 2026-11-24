#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only Exchange transport diagnostics and Change Review evidence.

Raw representation fidelity and candidate architectural interpretation are separate.
No IDs are allocated, no inputs are written, and no publication gates are changed.
"""
import argparse
from collections import Counter, defaultdict
import gzip
import hashlib
import json
from pathlib import Path
import re
import sys

import generate_views as g

from workspace_paths import historical_path, incoming_path

BASE = g.BASE
ROOT = BASE.parents[2]
sys.path.insert(0, str(ROOT / "scripts"))
from architecture_release import load_release
import governed_metadata as metadata
from element_correlation import correlate

MANIFEST = BASE / "tests/fixtures/roundtrip/manifest.json"
REGISTRY = ROOT / "docs/markdown/governance/architecture-identity/registry.json"
UUID32 = re.compile(r"[0-9a-f]{32}\Z")
ROLES = ("canonicalId", "displayName", "friendlyName", "canonicalName", "archimateType", "harmoniaType")
EC_PROPERTY = "v0.2 / Canonical Architecture ID"
SHORT_NAME = "v0.2 / Canonical Name"
QUALIFIED_NAME = "v0.2 / Structural Canonical ID"
LEGACY = (EC_PROPERTY, "Reference ID", "v0.2 / Snapshot Reference ID",
          "v0.2 / Source Capture Key", QUALIFIED_NAME, SHORT_NAME,
          "v0.2 / Construct Type",
          "v0.2 / Historical ID Aliases", "v0.2 / Name Aliases", "v0.2 / Reviewed Name",
          "v0.2 / Native Definition ID", "Baseline / Native Definition ID")
EQUIVALENT = "SEMANTICALLY EQUIVALENT NORMALISATION"
PRESENTATION = "PRESENTATION-PRESERVING TRANSFORMATION"
LOSS = "INFORMATION LOSS"
SEMANTIC = "SEMANTIC DIFFERENCE"
UNKNOWN = "UNEXPLAINED DIFFERENCE"
CATEGORIES = (EQUIVALENT, PRESENTATION, LOSS, SEMANTIC, UNKNOWN)


def __getattr__(name):
    # Preserve the historical public convenience attribute for its existing
    # callers, while keeping the maintained Exchange reader independent.
    if name == 'ACTIVE':
        return metadata.model_path()
    raise AttributeError(name)


def encoded(value):
    return json.dumps(value, ensure_ascii=False, sort_keys=True, indent=2) + "\n"


def sha(path):
    return hashlib.sha256(historical_path(path).read_bytes()).hexdigest()


def local(tag):
    return tag.removeprefix("{" + g.NS + "}")


def children(node):
    return [c for c in node if isinstance(c.tag, str)]


def expanded_type(node):
    value = node.get(g.TYPE)
    if value is None:
        return None
    prefix, name = value.split(":", 1) if ":" in value else (None, value)
    namespace = node.nsmap.get(prefix)
    return "{" + namespace + "}" + name if namespace else value


def values(node, name):
    # Preserve language, whitespace, multiplicity and attributes. Never strip UUIDs.
    return [{"text": c.text or "", "attributes": dict(sorted(c.attrib.items()))}
            for c in node.findall("a:" + name, g.N)]


class Exchange:
    """Shared Property resolver over the existing secure parser and offline XSDs.

    The historical g.Model is deliberately Capability/Composition-only. It also
    maps one value per definition ID. Intake needs all types and all claims, so
    this small index uses g.parse rather than changing the frozen reader.
    """
    def __init__(self, path):
        self.path = Path(path)
        self.root = g.parse(path)
        self.sha256 = sha(path)
        self.xml_sha256 = (hashlib.sha256(gzip.decompress(historical_path(self.path).read_bytes())).hexdigest()
                           if self.path.suffix == '.gz' else self.sha256)
        self.issues = []
        try:
            g.validate_identifiers(self.root)
        except ValueError as exc:
            self.issues.append(str(exc))
        schema = g.load_schema(BASE / "schemas/archimate3_Diagram.xsd")
        if not schema.validate(self.root):
            self.issues.extend("XSD: " + e.message for e in schema.error_log)
        self.elements = self.index("a:elements/a:element")
        self.relationships = self.index("a:relationships/a:relationship")
        self.views = self.index("a:views/a:diagrams/a:view")
        self.definitions = self.index("a:propertyDefinitions/a:propertyDefinition")
        by_name = defaultdict(list)
        for identifier, definition in self.definitions.items():
            names = definition.findall("a:name", g.N)
            if len(names) == 1 and names[0].text:
                by_name[names[0].text].append(identifier)
            else:
                self.issues.append("Ambiguous/empty Property definition name: " + str(identifier))
        self.keys = {ids[0]: key for key, ids in by_name.items() if len(ids) == 1}
        self.definition_ids = {key: identifier for identifier, key in self.keys.items()}
        for key, ids in sorted(by_name.items()):
            if len(ids) != 1:
                self.issues.append("Duplicate Property definition name: " + repr(key))
        for prop in self.root.findall(".//a:property", g.N):
            if prop.get("propertyDefinitionRef") not in self.keys:
                self.issues.append("Unresolved/ambiguous Property reference: " + str(prop.get("propertyDefinitionRef")))
        self.check_references()
        self.issues = sorted(set(self.issues))

    def index(self, path):
        result = {}
        for position, element in enumerate(self.root.findall(path, g.N)):
            key = element.get("identifier")
            if not key or key in result:
                # This is a diagnostic locator, never an allocated identity.
                # Invalid input remains unresolved; preserve every record.
                key = f"(unresolved identifier at {path} position {position})"
            result[key] = element
        return result

    def claims(self, owner, key):
        return [p for p in owner.findall("a:properties/a:property", g.N)
                if self.keys.get(p.get("propertyDefinitionRef")) == key]

    def texts(self, owner, key):
        return [v.text or "" for p in self.claims(owner, key) for v in p.findall("a:value", g.N)]

    def single(self, owner, key):
        claims = self.claims(owner, key)
        texts = self.texts(owner, key)
        return texts[0] if len(claims) == len(texts) == 1 else None

    def check_references(self):
        all_ids = {e.get("identifier") for e in self.root.iter() if e.get("identifier")}
        tables = {"elementRef": self.elements, "relationshipRef": self.relationships,
                  "viewRef": self.views, "identifierRef": all_ids}
        for node in self.root.iter():
            for attr, table in tables.items():
                if attr in node.attrib and node.get(attr) not in table:
                    self.issues.append("Unresolved " + attr + ": " + node.get(attr))
        semantic_ids = self.elements.keys() | self.relationships.keys()
        for identifier, rel in self.relationships.items():
            if any(rel.get(k) not in semantic_ids for k in ("source", "target")):
                self.issues.append("Unresolved relationship endpoint: " + str(identifier))
        for vid, view in self.views.items():
            nodes = {n.get("identifier"): n for n in view.findall(".//a:node", g.N)}
            connections = {n.get("identifier"): n for n in view.findall(".//a:connection", g.N)}
            for cid, connection in connections.items():
                endpoints = [connection.get(k) for k in ("source", "target")]
                if any(e not in nodes.keys() | connections.keys() for e in endpoints):
                    self.issues.append("Unresolved visual endpoint: " + str(vid) + "/" + str(cid))
                rel = self.relationships.get(connection.get("relationshipRef"))
                if rel is not None and all(e in nodes for e in endpoints):
                    refs = [nodes[e].get("elementRef") for e in endpoints]
                    if all(refs) and refs != [rel.get("source"), rel.get("target")]:
                        self.issues.append("Visual/semantic endpoint mismatch: " + str(cid))

    def metrics(self):
        rows = [{"identifier": key, "name": values(view, "name"),
                 "elements": len(view.findall(".//a:node[@elementRef]", g.N)),
                 "visual_objects": len(view.findall(".//a:node", g.N)),
                 "connections": len(view.findall(".//a:connection", g.N))}
                for key, view in self.views.items()]
        largest = sorted(rows, key=lambda r: (-r["visual_objects"], -r["connections"], r["identifier"]))
        return dict(semantic_elements=len(self.elements), relationships=len(self.relationships),
                    views=len(self.views), visual_objects=sum(r["visual_objects"] for r in rows),
                    visual_connections=sum(r["connections"] for r in rows),
                    largest_view=largest[0] if largest else None,
                    xml_bytes=self.path.stat().st_size, xml_decimal_mb=self.path.stat().st_size / 1_000_000)


def baseline_context():
    # Validate existing source/crosswalk authority, without publishing. Reuse the
    # source-path and ancestry evidence; do not invent friendly-name token rules.
    import synchronise_baseline as s
    return s.Baseline(*s.load_inputs())


def diagnose(model, previous=None, context=None, registry=None):
    missing, malformed, multiple = [], [], []
    owners, aliases = defaultdict(list), defaultdict(list)
    legacy_counts, naming_counts, standing = Counter(), Counter(), Counter()
    rows, conflicts, legacy_name_differences = [], [], []
    governed = None
    if context is not None and metadata.active() and ("canonicalId" in model.definition_ids or model.path == metadata.model_path()):
        governed, _ = metadata.load(context)
        import reconcile_108
        if reconcile_108.SOURCE.exists() and set(model.elements) & {
                e['representationId'] for e in reconcile_108.read()['elements']}:
            governed = reconcile_108.extend_index(governed)
            import home_context
            if home_context.SOURCE.exists():
                # Expected names come from governed resources and their explicit
                # Homes, never from the incoming file's folders or assertions.
                expected_root = g.parse(metadata.model_path())
                governed = home_context.amended_index(governed,
                    home_context.derive(context, expected_root, governed))
        import information_population as ia
        if ia.active():
            extra = ia.descriptors(ia.load())
            if set(model.elements) & set(extra):
                governed = {**governed, **extra}
        import publish_information_release as information_release
        if information_release.active() and model.path.resolve() == metadata.model_path().resolve():
            governed = information_release.amend_metadata_index(governed)
    source_identity_conflicts = []
    short_name_owners = defaultdict(list)
    for identifier, element in sorted(model.elements.items()):
        claims = model.claims(element, "canonicalId")
        ids = model.texts(element, "canonicalId")
        valid = model.single(element, "canonicalId")
        if not claims:
            missing.append(identifier)
        elif len(claims) != 1 or len(ids) != 1:
            multiple.append(identifier)
        for value in ids:
            if not UUID32.fullmatch(value):
                malformed.append(dict(element=identifier, value=value))
            else:
                owners[value].append(identifier)
        for value in model.texts(element, EC_PROPERTY):
            if re.fullmatch(r"EC-\d+", value):
                aliases[value].append(identifier)
        legacy = {key: model.texts(element, key) for key in LEGACY if model.claims(element, key)}
        legacy_counts.update(legacy.keys())
        names = {key: model.texts(element, key) for key in ROLES[1:4]}
        native = values(element, "name")
        if len(native) == 1 and native[0]["text"].strip():
            naming_counts["native_display_available"] += 1
        for key in ROLES[1:4]:
            if not model.claims(element, key):
                naming_counts[key + "_property_missing"] += 1
            elif model.single(element, key) is not None and model.single(element, key).strip():
                naming_counts[key + "_property_single_nonempty"] += 1
            else:
                naming_counts[key + "_property_empty_or_multiple"] += 1
        display = model.single(element, "displayName")
        item = governed.get(identifier) if governed is not None else None
        if item is not None:
            for role, expected_name in item['expected'].items():
                if model.single(element, role) != expected_name:
                    conflicts.append(dict(element=identifier, role=role, reason="Differs from deterministic governed source derivation", expected=expected_name, actual=model.single(element, role)))
                else:
                    naming_counts[role + '_derived_match'] += 1
            if model.single(element, 'canonicalId') != item['record']['canonicalId']:
                source_identity_conflicts.append(dict(element=identifier, expected=item['record']['canonicalId'], current=model.single(element, 'canonicalId'), finding='ESTABLISHED SOURCE BINDING CONFLICT'))
            for role in ('archimateType', 'harmoniaType'):
                if model.single(element, role) != item[role]:
                    conflicts.append(dict(element=identifier, role=role, reason='Differs from established semantic type', expected=item[role]))
        elif display is not None and (len(native) != 1 or native[0]["text"] != display):
            conflicts.append(dict(element=identifier, role="displayName", reason="Property differs from native Name"))
        short_name = model.single(element, SHORT_NAME)
        if short_name:
            short_name_owners[short_name].append(identifier)
            if len(native) == 1 and short_name != native[0]["text"]:
                legacy_name_differences.append(dict(element=identifier, legacy_short_name=short_name, native_name=native[0]["text"]))
        record = context.byid.get(identifier) if context is not None else None
        source = None
        if record is not None:
            expected = record.get("canonical_id")
            chain = context.chain(record["key"], owner=record["type"] not in ("capability", "enterprise_capability"))
            source = dict(capture_key=record["key"], legacy_qualified_path=expected,
                          structural_standing=record.get("structural_id_status"),
                          architectural_standing=record.get("architectural_status"),
                          currentness=record.get("record_state"),
                          context_keys=[e["key"] for e in chain])
            if expected:
                naming_counts["legacy_structural_path_available"] += 1
                standing[record.get("structural_id_status", "unspecified")] += 1
                for key in ((QUALIFIED_NAME,) if governed is not None else ("canonicalName", QUALIFIED_NAME)):
                    persisted = model.single(element, key)
                    if persisted is not None and persisted != expected:
                        conflicts.append(dict(element=identifier, role=key, reason="Differs from bound source path (standing retained)", expected=expected, actual=persisted))
        native_type = local(expanded_type(element) or "")
        if model.single(element, "archimateType") not in (None, native_type):
            conflicts.append(dict(element=identifier, role="archimateType", reason="Property differs from native type"))
        if model.single(element, "harmoniaType") in ("Feature", "feature") and native_type != "Capability":
            conflicts.append(dict(element=identifier, role="harmoniaType", reason="Feature must use native Capability"))
        rows.append(dict(element=identifier, native_type=native_type,
                         type_properties={key: model.texts(element, key) for key in ROLES[4:]}, canonicalId=ids,
                         canonicalId_single_valid=bool(valid is not None and UUID32.fullmatch(valid)),
                         native_names=native, governed_name_properties=names, legacy=legacy, source=source))
    duplicates = {value: sorted(set(ids)) for value, ids in sorted(owners.items()) if len(set(ids)) > 1}
    historical_conflicts = []
    if registry is not None:
        for record in registry["records"]:
            code, owner = record["canonical_architecture_id"], record["archimate"]["semantic_id"]
            if re.fullmatch(r"EC-\d+", code) and aliases.get(code) != [owner]:
                historical_conflicts.append(dict(alias=code, expected_owner=owner, observed_owners=aliases.get(code, [])))
    continuity = source_identity_conflicts
    if governed is not None:
        observed_canonical = set(owners)
        for identifier in sorted(governed.keys() - model.elements.keys()):
            if governed[identifier]['record']['canonicalId'] in observed_canonical:
                continue
            continuity.append(dict(element=identifier, finding='MISSING ESTABLISHED SOURCE OWNER'))
    correlation = None
    if previous is not None:
        correlation = correlate(previous, model)
        continuity.extend(dict(finding='CANONICAL IDENTITY CONFLICT / POSSIBLE REUSE', evidence=issue)
                          for issue in correlation['issues'])
        continuity.extend(dict(element=identifier, finding='ESTABLISHED CANONICAL ID DISAPPEARED')
                          for identifier in correlation['deleted']
                          if previous.claims(previous.elements[identifier], 'canonicalId'))
    valid_count = sum(row["canonicalId_single_valid"] for row in rows)
    identity_ready = not (missing or malformed or multiple or duplicates or continuity)
    for key in ROLES[1:4]:
        for suffix in ("_property_missing", "_property_single_nonempty", "_property_empty_or_multiple"):
            naming_counts[key + suffix] += 0
    naming_counts["native_display_missing_or_multiple"] = len(rows) - naming_counts["native_display_available"]
    if context is not None:
        naming_counts["legacy_structural_path_unavailable"] = len(rows) - naming_counts["legacy_structural_path_available"]
    return dict(audit_status="PASS" if not model.issues and not (previous and previous.issues) else "UNRESOLVED",
                future_identity_readiness="CONFORMANT_IN_SNAPSHOT" if identity_ready else "NONCONFORMANT",
                future_naming_readiness="NONCONFORMANT" if conflicts or naming_counts["native_display_available"] != len(rows) or any(naming_counts[k + "_property_missing"] or naming_counts[k + "_property_empty_or_multiple"] for k in ROLES[1:4]) else "CONFORMANT_WITH_GOVERNED_DERIVATION" if governed is not None else "PERSISTED_VALUES_PRESENT; derivation not established",
                input_sha256=model.sha256, metrics=model.metrics(), input_issues=model.issues,
                previous_input_issues=[] if previous is None else previous.issues,
                canonical_id=dict(total_elements=len(rows), single_valid_uuid32=valid_count,
                                  unique_valid_uuid32_elements=sum(row["canonicalId_single_valid"] and row["canonicalId"][0] not in duplicates for row in rows),
                                  missing=missing, malformed=malformed, multiple_claims=multiple, duplicates=duplicates,
                                  continuity_findings=continuity,
                                  representation_rebindings=[] if correlation is None else correlation['representation_rebindings'],
                                  snapshot_correlation=correlation,
                                  lifetime_nonreuse="Retained resource-owned IDs and pinned complete source history; physical source deletion is rejected" if governed is not None else "UNRESOLVED: no active UUID32 allocation/tombstone history; two snapshots cannot prove lifetime non-reuse"),
                historical_ec_aliases=dict(count=sum(len(v) for v in aliases.values()), owners=dict(sorted(aliases.items())),
                                           duplicates={k: v for k, v in sorted(aliases.items()) if len(v) > 1}, binding_conflicts=historical_conflicts),
                legacy_property_coverage=dict(sorted(legacy_counts.items())), naming=dict(counts=dict(sorted(naming_counts.items())),
                source_path_standing=dict(sorted(standing.items())), conflicts=conflicts,
                legacy_short_name_differences=legacy_name_differences,
                legacy_short_name_collisions={key: ids for key, ids in sorted(short_name_owners.items()) if len(ids) > 1},
                friendlyName_derivation="Validated against governed PascalCase components and semantic context" if governed is not None else "UNRESOLVED: no approved token/source grammar; no values derived",
                canonicalName_derivation="Validated against the same components/context with formal qualifiers" if governed is not None else "Existing bound source paths and Baseline.chain context only; no universal grammar or inferred ancestry"), elements=rows)


def compare(before, after, control=None):
    """Exhaustive XML probe; match existing representation IDs, never names.

    Only Property-definition *keys* use exact names (not architectural identity).
    Unordered keyed semantic collections can reorder. Visual stacking, routing,
    languages, text and anonymous organisation order cannot silently reorder.
    """
    findings = []
    common_keys = before.definition_ids.keys() & after.definition_ids.keys()

    def add(category, path, a, b, reason):
        findings.append(dict(classification=category, path=path, before=a, after=b, reason=reason))

    def attrs(node, doc):
        result = dict(node.attrib)
        if g.TYPE in result:
            result[g.TYPE] = expanded_type(node)
        ref = result.get("propertyDefinitionRef")
        if ref in doc.keys and doc.keys[ref] in common_keys:
            result["propertyDefinitionRef"] = "Property key: " + doc.keys[ref]
        return result

    def keyed(parent, doc):
        counts, result = Counter(), {}
        for child in children(parent):
            tag = local(child.tag)
            if child.tag == g.tag("propertyDefinition") and doc.keys.get(child.get("identifier")) in common_keys:
                key = tag + "[key=" + doc.keys[child.get("identifier")] + "]"
            elif child.tag == g.tag("property") and doc.keys.get(child.get("propertyDefinitionRef")) in common_keys:
                key = tag + "[key=" + doc.keys[child.get("propertyDefinitionRef")] + "]"
            elif child.tag == g.tag("item"):
                # Anonymous navigation after Archi export has no stable identity.
                # Pair strictly by position only for this no-edit probe, retaining
                # every label, reference, order and lost identifier as evidence.
                counts[tag] += 1
                key = tag + "[position=" + str(counts[tag]) + "]"
            elif child.get("identifier") is not None:
                key = tag + "[id=" + child.get("identifier") + "]"
            else:
                counts[tag] += 1
                key = tag + "[" + str(counts[tag]) + "]"
            # Preserve duplicate claims instead of dict overwrite.
            if key in result:
                counts[key] += 1
                key += "[duplicate=" + str(counts[key]) + "]"
            result[key] = child
        return result

    def content_text(node):
        # XML element-only indentation is syntax. Leaf strings, even all-space
        # values, are data. Non-whitespace mixed content remains observable.
        if children(node) and not (node.text or "").strip():
            return None
        return node.text

    def walk(a, b, path):
        aa, ba = attrs(a, before), attrs(b, after)
        if a.tag == g.tag("propertyDefinition") and before.keys.get(a.get("identifier")) in common_keys:
            old, new = aa.pop("identifier", None), ba.pop("identifier", None)
            if old != new:
                # Rekeying alone is equivalent. Metadata below is still compared.
                add(EQUIVALENT, path + "/@identifier", old, new, "Exact unique Property key resolves both definition IDs; all other definition content and owner values compared independently")
        for key in sorted(aa.keys() | ba.keys()):
            if aa.get(key) == ba.get(key):
                continue
            if key not in ba:
                category, reason = LOSS, "Attribute absent after transport"
            elif key == "identifier" and a.tag == g.tag("item"):
                category, reason = UNKNOWN, "Organisation item identity transformed; paths do not establish identity"
            elif "/element[id=" in path or "/relationship[id=" in path:
                category, reason = SEMANTIC, "Semantic record attribute changed"
            else:
                category, reason = UNKNOWN, "No proven representational equivalence for this attribute"
            add(category, path + "/@" + key, aa.get(key), ba.get(key), reason)
        at, bt = content_text(a), content_text(b)
        if at != bt:
            category = LOSS if at is not None and bt is None else SEMANTIC if "/element[id=" in path or "/relationship[id=" in path else UNKNOWN
            add(category, path + "/text()", at, bt, "Exact text compared; whitespace/language selection is not normalised")
        ac, bc = keyed(a, before), keyed(b, after)
        if list(ac) != list(bc):
            safe = a.tag in {g.tag(n) for n in ("elements", "relationships", "diagrams", "propertyDefinitions", "properties")}
            safe = safe and ac.keys() == bc.keys() and not any("duplicate=" in k for k in ac)
            add(EQUIVALENT if safe else UNKNOWN, path + "/child-order", list(ac), list(bc),
                "Uniquely keyed unordered collection" if safe else "Order retained as evidence: may affect stacking, routing, languages or anonymous navigation")
        for key in sorted(ac.keys() | bc.keys()):
            subpath = path + "/" + key
            if key not in bc:
                add(LOSS, subpath, g.signature(ac[key]), None, "Subtree absent after transport; raw input retained by hash")
            elif key not in ac:
                add(UNKNOWN, subpath, None, g.signature(bc[key]), "Additional subtree with no established equivalence")
            else:
                walk(ac[key], bc[key], subpath)
                tail_a, tail_b = ac[key].tail, bc[key].tail
                if (tail_a or "").strip() or (tail_b or "").strip():
                    if tail_a != tail_b:
                        add(UNKNOWN, subpath + "/tail()", tail_a, tail_b, "Mixed-content tail changed")

    walk(before.root, after.root, "/model")
    counts = Counter(f["classification"] for f in findings)
    issues = {"published": before.issues, "incoming": after.issues}
    status = "UNRESOLVED" if any(issues.values()) else "DIFFERENCES" if any(counts[k] for k in (LOSS, SEMANTIC, UNKNOWN)) else "EQUIVALENT"

    changed_records = defaultdict(set)
    record_pattern = re.compile(r'/((?:element|relationship|view))\[id=([^\]]+)\]')
    material_paths = [f['path'] for f in findings if f['classification'] != EQUIVALENT]
    for path in material_paths:
        for prefix, identifier in record_pattern.findall(path):
            changed_records[prefix].add(identifier)

    def record_evidence(table_a, table_b, prefix):
        common = table_a.keys() & table_b.keys()
        changed = sorted(key for key in common if key in changed_records[prefix] or
                         (']' in key and any(f'/{prefix}[id={key}]' in path for path in material_paths)))
        return dict(before=len(table_a), after=len(table_b), retained_ids=len(table_a.keys() & table_b.keys()),
                    missing_ids=sorted(table_a.keys() - table_b.keys()), additional_ids=sorted(table_b.keys() - table_a.keys()),
                    changed_records=changed)

    presentation = record_evidence(before.views, after.views, "view")
    for tag in ("node", "connection"):
        left = {(vid, n.get("identifier")) for vid, v in before.views.items() for n in v.findall(".//a:" + tag, g.N)}
        right = {(vid, n.get("identifier")) for vid, v in after.views.items() for n in v.findall(".//a:" + tag, g.N)}
        presentation[tag + "_identity"] = dict(before=len(left), after=len(right), retained=len(left & right),
                                                missing=sorted(left - right), additional=sorted(right - left))
    presentation["coverage"] = "View-local visual IDs, parent/nesting, membership, coordinates, dimensions, ordered bendpoints, child order, style, labels and residual XML; no presentation replay or speculative equivalence rules"
    contract = {}
    for role in ROLES:
        observed, preserved, changed = 0, 0, []
        for identifier, owner in sorted(before.elements.items()):
            claims = before.claims(owner, role)
            if not claims:
                continue
            observed += 1
            target = after.elements.get(identifier)
            left = [values(p, "value") for p in claims]
            right = [] if target is None else [values(p, "value") for p in after.claims(target, role)]
            if left == right:
                preserved += 1
            else:
                changed.append(dict(element=identifier, before=left, after=right))
        contract[role] = dict(published_owners=observed, exact_value_language_multiplicity_owner_preserved=preserved, changes=changed)
    from change_review import review
    return dict(comparison_status=status, purpose="Raw representation fidelity plus separate canonical-identity-first Change Review evidence; no architecture acceptance",
                published_sha256=before.sha256, incoming_sha256=after.sha256, input_issues=issues,
                classifications={k: counts[k] for k in CATEGORIES}, metrics={"published": before.metrics(), "incoming": after.metrics()},
                elements=record_evidence(before.elements, after.elements, "element"),
                relationships=record_evidence(before.relationships, after.relationships, "relationship"),
                presentation=presentation, contract_properties=contract, findings=findings,
                architectural_review=review(before, after, control),
                empirical_archi_fidelity="UNRESOLVED: comparator alone does not authenticate an Archi execution; use the hash-pinned evidence procedure")


def verify_exports(manifest_path=MANIFEST, operator=None, attest=False):
    """Run both fixed probes after export; pin sources without duplicating 1.0.6.

    Attestation records human provenance, never infers that a file came from Archi.
    An equivalent self-copy without attestation cannot pass an empirical proof.
    """
    manifest = json.loads(Path(manifest_path).read_text())
    g.require(manifest.get("schema_version") == 1 and set(manifest["fixtures"]) == {"release-1.0.6", "future-contract"},
              "Expected the two Increment 1 fixture profiles")
    g.require(manifest["export_profile"]["archi_version"] == "5.10.0", "Expected Archi 5.10.0 export profile")
    g.require(not attest or bool(operator), "Archi execution attestation requires --operator")
    probes = {}
    for name, fixture in sorted(manifest["fixtures"].items()):
        published, incoming = (historical_path(ROOT / fixture[key]) for key in ("published", "incoming"))
        g.require(published.resolve().is_relative_to(ROOT) and incoming.resolve().is_relative_to(ROOT), "Fixture path outside repository")
        g.require(sha(published) == fixture["published_sha256"], "Pinned input changed: " + name)
        if not incoming.exists():
            probes[name] = dict(comparison_status="AWAITING ARCHI EXPORT", published_sha256=sha(published),
                                expected_incoming=fixture["incoming"], empirical_archi_fidelity="UNRESOLVED")
        else:
            probes[name] = compare(Exchange(published), Exchange(incoming))
            if attest:
                probes[name]["empirical_archi_fidelity"] = "OPERATOR-ATTESTED Archi 5.10.0 no-edit full-model export; see comparison findings"
    statuses = {p["comparison_status"] for p in probes.values()}
    status = ("AWAITING ARCHI EXPORT" if "AWAITING ARCHI EXPORT" in statuses else
              "UNRESOLVED" if "UNRESOLVED" in statuses or not attest else
              "TRANSPORT FINDINGS" if "DIFFERENCES" in statuses else "PASS")
    return dict(status=status, accepted_architecture_baseline=manifest["accepted_architecture_baseline"],
                manifest_sha256=sha(manifest_path), expected_export_profile=manifest["export_profile"],
                execution_attestation=dict(operator=operator, archi_5_10_0_no_edit_full_model=attest,
                                           note="Operator declaration, not independent authentication; applies only to the reported input/output hashes"), probes=probes)


def emit(report, path, inputs):
    if path is None:
        print(encoded(report), end="")
        return
    path = Path(path).resolve()
    g.require(path not in {Path(p).resolve() for p in inputs}, "Output must not overwrite an input")
    g.require(path.is_relative_to(ROOT / ".junie/reports") or path.is_relative_to(Path("/tmp")),
              "Diagnostic outputs must be under .junie/reports or /tmp; governed/publication inputs are read-only")
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(encoded(report))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    commands = parser.add_subparsers(dest="command", required=True)
    audit = commands.add_parser("diagnose", help="Future-contract census, never a publication gate")
    audit.add_argument("--model", type=Path)
    audit.add_argument("--previous", type=Path, help="Optional earlier Exchange snapshot for observed immutable binding conflicts")
    audit.add_argument("--baseline-context", action="store_true", help="Read validated source naming context, established metadata and frozen EC bindings")
    audit.add_argument("--output", type=Path)
    fidelity = commands.add_parser("compare", help="Raw fidelity and canonical-identity-first Change Review evidence")
    fidelity.add_argument("--published", type=Path, required=True)
    fidelity.add_argument("--incoming", type=Path, required=True)
    fidelity.add_argument("--control", type=Path, help="Explicit no-edit export of this baseline; isolate per-field transport observations from submitted changes")
    fidelity.add_argument("--output", type=Path)
    proof = commands.add_parser("verify-exports", help="Run both hash-pinned no-edit probes; absent exports remain an explicit gate")
    proof.add_argument("--manifest", type=Path, default=MANIFEST)
    proof.add_argument("--operator", help="Person who executed the recorded export profile")
    proof.add_argument("--attest-archi-5.10", action="store_true", dest="attest", help="Declare that these exports were actually produced by Archi 5.10.0 using the manifest profile")
    proof.add_argument("--output", type=Path)
    args = parser.parse_args()
    if args.command == "compare":
        args.incoming = incoming_path(args.incoming)
    if args.command == "verify-exports":
        report = verify_exports(args.manifest, args.operator, args.attest)
        manifest = json.loads(args.manifest.read_text())
        inputs = [args.manifest, *(ROOT / f[k] for f in manifest["fixtures"].values() for k in ("published", "incoming"))]
        emit(report, args.output, inputs)
        return 0 if report["status"] == "PASS" else 3 if report["status"] == "AWAITING ARCHI EXPORT" else 2
    if args.command == "diagnose":
        # Historical default discovery belongs to that CLI operation. Importing
        # the reusable Exchange reader must not open historical source captures.
        if args.model is None:
            args.model = metadata.model_path()
        context = baseline_context() if args.baseline_context else None
        registry = json.loads(REGISTRY.read_text()) if args.baseline_context else None
        previous = Exchange(args.previous) if args.previous else None
        report = diagnose(Exchange(args.model), previous, context, registry)
        report["architecture_release"] = load_release()
        if args.baseline_context:
            report["context_sha256"] = {str(p.relative_to(ROOT)): sha(p) for p in (
                REGISTRY, ROOT / "docs/markdown/governance/baseline/architecture.json",
                ROOT / "docs/markdown/governance/baseline/correspondence.json",
                g.workspace.RECOVERY_EVIDENCE / "representation-crosswalk.json")}
        emit(report, args.output, [args.model, *([args.previous] if args.previous else []), REGISTRY])
        return 0 if report["audit_status"] == "PASS" else 2
    if not args.incoming.exists():
        report = dict(comparison_status="AWAITING ARCHI EXPORT", published_sha256=sha(args.published),
                      expected_incoming=str(args.incoming), empirical_archi_fidelity="UNRESOLVED")
        emit(report, args.output, [args.published, args.incoming])
        return 3
    report = compare(Exchange(args.published), Exchange(args.incoming), Exchange(args.control) if args.control else None)
    emit(report, args.output, [args.published, args.incoming, *([args.control] if args.control else [])])
    return 0 if report["comparison_status"] == "EQUIVALENT" else 2


if __name__ == "__main__":
    try:
        sys.exit(main())
    except (OSError, ValueError, g.ET.XMLSyntaxError) as exc:
        print("UNRESOLVED: " + str(exc), file=sys.stderr)
        sys.exit(2)
