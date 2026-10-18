#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Reproduce this hash-pinned blind observation; writes analysis evidence only.

This task-local probe is not an ingestion framework, identity allocator,
reconciler, publication command or change to Increment 1 equivalence rules.
Run from any directory with an external bounded timeout; see report.md.
"""
import argparse
from collections import Counter, defaultdict
import gzip
import hashlib
import json
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[6]
HERE = Path(__file__).resolve().parent
sys.path.insert(0, str(ROOT / "docs/archimate/framework/tools"))
import diagnose_roundtrip as diagnostic

PUBLISHED = "docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml"
INCOMING = "docs/archimate/exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml"
HISTORICAL = ".junie/reports/2026-10-18-archimate-canonical-identity-establishment"
CONTROL = HISTORICAL + "/archi-exports/release-1.0.7.xml"
HISTORICAL_REPORT = HISTORICAL + "/archi-fidelity.json"
PINNED = {
    PUBLISHED: "504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a",
    INCOMING: "4d178c7625744f34cfc34213468bc2c6e9ee833c570bfbbfcf65916ab9fe1af2",
    CONTROL: "38e49fa348d015aba6b19e21b4f96dc00fddf3c4d7671b3e352b4c07748917ea",
}
N = diagnostic.g.N
TAG = diagnostic.g.tag
RELEASE_KEY = "Baseline / Architecture Release"
FEATURE_PATH = (
    "Strategy", "Business Layer", "Business Enabling Capabilities",
    "[CT1] Entity Information Enablement", "[CT2] Client Administration",
    "[CT3] Person Identity Administration", "[Feature] Identifier Capture",
)
PRODUCT_PATH = ("Business", "Products")


def require(condition, message):
    if not condition:
        raise ValueError(message)


def encoded(value):
    return (json.dumps(value, ensure_ascii=False, sort_keys=True, indent=2) + "\n").encode()


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def folder_path(item):
    return tuple(
        node.findtext("a:label", namespaces=N)
        for node in [*reversed(list(item.iterancestors())), item]
        if node.tag == TAG("item") and node.find("a:label", N) is not None
    )


def organisation(model):
    folders, references, empty = Counter(), Counter(), Counter()
    items = model.root.findall(".//a:organizations//a:item", N)
    for item in items:
        path = folder_path(item)
        if item.find("a:label", N) is not None:
            folders[path] += 1
            if not item.findall("a:item", N):
                empty[path] += 1
        if item.get("identifierRef"):
            references[path + (item.get("identifierRef"),)] += 1
    return dict(items=len(items), identifiers=sum("identifier" in n.attrib for n in items),
                folders=folders, references=references, empty=empty)


def signature(node, omit=frozenset(), release_value=None):
    """Exact element content, apart from XML syntax and separately accounted nodes.

    Preserve whitespace in leaf values, meaningful mixed text/tails, ordering,
    multiplicity, languages, geometry, styles and all other attributes/content.
    Omit sets contain original tree nodes; this function never mutates a tree.
    """
    children = [n for n in node if isinstance(n.tag, str)]
    attrs = dict(node.attrib)
    if diagnostic.g.TYPE in attrs:
        attrs[diagnostic.g.TYPE] = diagnostic.expanded_type(node)
    value = node.text
    if children and not (value or "").strip():
        value = None
    if node is release_value:
        value = "[separately verified model-level release value]"
    return [node.tag, sorted(attrs.items()), value,
            [[signature(n, omit, release_value),
              n.tail if (n.tail or "").strip() else None]
             for n in children if n not in omit]]


def release_node(model):
    claims = model.claims(model.root, RELEASE_KEY)
    require(len(claims) == 1, "Model-level release must have one Property claim")
    values = claims[0].findall("a:value", N)
    require(len(values) == 1, "Model-level release must have one value")
    return values[0]


def read_or_compare(path, left, right):
    if path:
        raw = Path(path).read_bytes()
        if str(path).endswith(".gz"):
            raw = gzip.decompress(raw)
        result = json.loads(raw)
    else:
        result = diagnostic.compare(left, right)
    require(result["published_sha256"] == left.sha256 and
            result["incoming_sha256"] == right.sha256,
            "Comparison report is not bound to these input hashes")
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output-dir", type=Path, default=HERE)
    parser.add_argument("--transport-report", type=Path)
    parser.add_argument("--control-report", type=Path)
    args = parser.parse_args()
    out = args.output_dir.resolve()
    permitted = (ROOT / "docs/archimate/processing/ingestion").resolve()
    require(out.is_relative_to(permitted) or out.is_relative_to(Path("/tmp")),
            "Evidence output must stay within processing/ingestion or /tmp")
    for path, expected in PINNED.items():
        require(sha(ROOT / path) == expected, "Hash-pinned input changed: " + path)
    before, incoming, control = [diagnostic.Exchange(ROOT / p)
                                 for p in (PUBLISHED, INCOMING, CONTROL)]
    require(not before.issues and not incoming.issues and not control.issues,
            "Input validation issues; preserve and investigate before reproducing this observation")
    raw = read_or_compare(args.transport_report, before, incoming)
    control_raw = read_or_compare(args.control_report, control, incoming)
    historical = json.loads((ROOT / HISTORICAL_REPORT).read_bytes())
    require(historical["incoming_sha256"] == control.sha256,
            "Frozen no-edit report must bind the frozen control export")

    added_ids = set(incoming.elements) - set(before.elements)
    tables = {}
    for section in ("elements", "relationships", "views"):
        left, right = getattr(before, section), getattr(incoming, section)
        prior = getattr(control, section)
        retained = set(left) & set(right)
        changed_control = sorted(k for k in retained
                                 if signature(prior[k]) != signature(right[k]))
        tables[section] = dict(published=len(left), incoming=len(right), retained=len(retained),
                               missing=sorted(set(left) - set(right)),
                               additional=sorted(set(right) - set(left)),
                               existing_records_different_from_no_edit_control=changed_control)
        require(not changed_control, "Unaccounted existing record changes: " + section)
    require(set(control.elements) == set(before.elements), "Control population differs from baseline")
    require(not any(t["missing"] for t in tables.values()), "Unexpected removal")

    org_before, org_after = organisation(before), organisation(incoming)
    org_result = {k: [org_before[k], org_after[k]] for k in ("items", "identifiers")}
    for key in ("folders", "references", "empty"):
        left, right = org_before[key], org_after[key]
        org_result[key] = dict(
            published=sum(left.values()), incoming=sum(right.values()),
            removed=[dict(path=list(p), count=n) for p, n in sorted((left - right).items())],
            added=[dict(path=list(p), count=n) for p, n in sorted((right - left).items())])
    require(not any(org_result[k]["removed"] for k in ("folders", "references", "empty")),
            "Existing navigation removed or changed")

    new_elements = []
    for key in sorted(added_ids):
        element = incoming.elements[key]
        homes = [dict(path=list(folder_path(n)), line=n.sourceline)
                 for n in incoming.root.findall('.//a:item[@identifierRef="' + key + '"]', N)]
        new_elements.append(dict(
            classification="ADDED XML SEMANTIC ELEMENT; ARCHITECTURAL IDENTITY UNESTABLISHED",
            representation_id=key, line=element.sourceline,
            native_type=diagnostic.expanded_type(element).split("}")[-1],
            names=diagnostic.values(element, "name"),
            documentation=diagnostic.values(element, "documentation"),
            properties_count=len(element.findall("a:properties/a:property", N)),
            governed_properties={role: incoming.texts(element, role) for role in diagnostic.ROLES},
            relationships=[k for k, rel in incoming.relationships.items()
                           if key in (rel.get("source"), rel.get("target"))],
            view_appearances=[dict(view=vid, node=n.get("identifier"))
                              for vid, view in incoming.views.items()
                              for n in view.findall('.//a:node[@elementRef="' + key + '"]', N)],
            organisation_references=homes,
            xml=diagnostic.g.ET.tostring(element, encoding="unicode", with_tail=False)))

    claims = defaultdict(list)
    missing, malformed, multiple = [], [], []
    for identifier, element in incoming.elements.items():
        properties, values = incoming.claims(element, "canonicalId"), incoming.texts(element, "canonicalId")
        if not properties:
            missing.append(identifier)
        elif len(properties) != 1 or len(values) != 1:
            multiple.append(identifier)
        else:
            claims[values[0]].append(identifier)
            if not diagnostic.UUID32.fullmatch(values[0]):
                malformed.append(identifier)
    identity = dict(existing_contract_properties=raw["contract_properties"],
                    missing_canonicalId=sorted(missing), malformed_canonicalId=sorted(malformed),
                    multiple_canonicalId=sorted(multiple),
                    duplicate_canonicalId={v: ids for v, ids in claims.items() if len(ids) > 1},
                    allocations_performed=0)
    ec_bindings = []
    for entry in json.loads(diagnostic.REGISTRY.read_bytes())["records"]:
        code, identifier = entry["canonical_architecture_id"], entry["archimate"]["semantic_id"]
        owners = [[k for k, e in m.elements.items() if code in m.texts(e, diagnostic.EC_PROPERTY)]
                  for m in (before, incoming)]
        require(owners == [[identifier], [identifier]], "EC alias binding changed: " + code)
        ec_bindings.append(dict(alias=code, representation_id=identifier,
                                canonicalId=incoming.single(incoming.elements[identifier], "canonicalId")))
    identity["historical_ec_alias_bindings_retained"] = ec_bindings

    # Account for precisely these observed additions in a signature comparison.
    # No tree/source model is edited and no repaired/reconciled XML is emitted.
    omit = {incoming.elements[k] for k in added_ids}
    folder_nodes = [n for n in incoming.root.findall(".//a:organizations//a:item", N)
                    if n.find("a:label", N) is not None and folder_path(n) in (FEATURE_PATH, PRODUCT_PATH)]
    require(len(folder_nodes) == 2, "Expected exactly two newly observed folder subtrees")
    omit.update(folder_nodes)
    release_values = {"published": before.single(before.root, RELEASE_KEY),
                      "incoming": incoming.single(incoming.root, RELEASE_KEY),
                      "control": control.single(control.root, RELEASE_KEY)}
    require(release_values == dict(published="1.0.8", incoming="1.0.8", control="1.0.7"),
            "Unexpected model-level release values")
    residual_equal = (signature(incoming.root, omit, release_node(incoming)) ==
                      signature(control.root, release_value=release_node(control)))
    require(residual_equal, "Additional unaccounted XML content; observation must be reopened")

    events = lambda report: Counter(json.dumps(f, sort_keys=True, ensure_ascii=False)
                                    for f in report["findings"])
    current_events, prior_events = events(raw), events(historical)
    event_delta = dict(
        exact_shared_events=sum((current_events & prior_events).values()),
        additional_current_events=[dict(multiplicity=n, finding=json.loads(s))
                                   for s, n in sorted((current_events - prior_events).items())],
        absent_historical_events=[dict(multiplicity=n, finding=json.loads(s))
                                  for s, n in sorted((prior_events - current_events).items())])
    result = dict(
        status="OBSERVATION COMPLETE; SOLUTION ARCHITECT REVIEW PENDING",
        authority="Analysis evidence only; no incoming assertion accepted or reconciled",
        baseline_release="1.0.8", release_increment_performed=False,
        inputs={path: dict(sha256=sha(ROOT / path), bytes=(ROOT / path).stat().st_size)
                for path in (PUBLISHED, INCOMING, CONTROL, HISTORICAL_REPORT)},
        validation=dict(published=before.issues, incoming=incoming.issues, control=control.issues,
                        meaning="Offline XSD, identifier uniqueness and references; not architectural approval"),
        metrics=raw["metrics"], populations=tables, identity=identity,
        element_type_counts={label: dict(sorted(Counter(diagnostic.expanded_type(e).split('}')[-1]
                                                       for e in model.elements.values()).items()))
                             for label, model in (("published", before), ("incoming", incoming))},
        new_elements=new_elements, organisation=org_result,
        transport=dict(classifications=raw["classifications"], comparison_status=raw["comparison_status"],
                       historical_classifications=historical["classifications"],
                       event_delta=event_delta, comparator_rules_changed=False,
                       control_classifications=control_raw["classifications"]),
        presentation=dict(view_ids_retained=tables["views"]["retained"],
                          node_identity=raw["presentation"]["node_identity"],
                          connection_identity=raw["presentation"]["connection_identity"],
                          bendpoints=sum(len(v.findall('.//a:bendpoint', N)) for v in incoming.views.values()),
                          existing_view_content_exact_against_control=True,
                          visual_import_or_rendering_performed=False),
        completeness=dict(residual_content_equal_to_frozen_no_edit_control=residual_equal,
                          accounted_content="Five added elements, two added folder subtrees containing five new references, and the separately checked 1.0.7/1.0.8 model-level release value",
                          method="Order-sensitive element-tree signatures preserve leaf text and metadata; omit only identified additions in comparison; do not modify models",
                          byte_equality_claimed=False, architecture_acceptance_claimed=False))
    out.mkdir(parents=True, exist_ok=True)
    (out / "observations.json").write_bytes(encoded(result))
    for name, report in (("transport-comparison.json.gz", raw), ("control-comparison.json.gz", control_raw)):
        (out / name).write_bytes(gzip.compress(encoded(report), mtime=0))
    print(json.dumps(dict(status=result["status"], added_elements=len(added_ids),
                          residual_content_equal=True, output=str(out)), ensure_ascii=False))


if __name__ == "__main__":
    main()
