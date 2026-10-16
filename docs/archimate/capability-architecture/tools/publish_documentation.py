#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Apply an explicit, exact-ID Documentation projection to the locked model."""
import argparse
import copy
import hashlib
import json
from pathlib import Path
import re
import sys

import generate_views as g


BASE = Path(__file__).resolve().parents[1]
DEFAULT_MAP = BASE / "documentation-map.json"
sys.path.insert(0, str(BASE.parents[2] / "scripts"))
from architecture_release import DEFAULT_RELEASE, load_release


def section_text(text, reference_id):
    """Retain the entire governed section, including subordinate qualifications."""
    matches = list(re.finditer(r"^### (EC-\d{2}): (.+)$", text, re.MULTILINE))
    selected = [i for i, match in enumerate(matches) if match[1] == reference_id]
    g.require(len(selected) == 1, f"Missing or duplicate source heading: {reference_id}")
    index = selected[0]
    end = matches[index + 1].start() if index + 1 < len(matches) else len(text)
    # The catalogue ends before its next level-two section, not at EOF.
    tail = text[matches[index].end():end]
    next_section = re.search(r"^## ", tail, re.MULTILINE)
    if next_section:
        tail = tail[:next_section.start()]
    body = tail.strip()
    # A horizontal document separator is not part of the definition.
    body = re.sub(r"\n---\s*$", "", body).rstrip()
    g.require(body and "**Architectural Scope**:" in body,
              f"No governed description: {reference_id}")
    return matches[index][2], body


def load_mapping(path, baseline):
    path = Path(path)
    manifest = json.loads(path.read_text())
    g.require(manifest["model_identifier"] == baseline.get("identifier"), "Wrong documentation model")
    source = (path.parent / manifest["source_document"]).resolve()
    raw = source.read_bytes()
    g.require(hashlib.sha256(raw).hexdigest() == manifest["source_sha256"],
              "Governed description source drift; review mapping before publication")
    text = raw.decode("utf-8")
    model = g.Model(baseline)
    mapping, refs = {}, set()
    for record in manifest["matches"]:
        reference = record["reference_id"]
        identifier = record["element_identifier"]
        g.require(identifier not in mapping and reference not in refs, "Duplicate documentation identity")
        refs.add(reference)
        g.require(identifier in model.elements, f"Unknown documentation element: {identifier}")
        element = model.elements[identifier]
        values = element.xpath("a:properties/a:property[@propertyDefinitionRef='prop-reference-id']/a:value/text()", namespaces=g.N)
        g.require(values == [reference], f"Exact Reference ID mismatch: {identifier}")
        # A duplicate Reference ID elsewhere is a conflict, even if its name differs.
        owners = [e for e in model.elements.values() if reference in e.xpath(
            "a:properties/a:property[@propertyDefinitionRef='prop-reference-id']/a:value/text()", namespaces=g.N)]
        g.require(len(owners) == 1, f"Conflicting Reference ID: {reference}")
        g.require(model.properties[identifier].get("prop-layer") == "Enterprise Capability",
                  f"Wrong documentation layer: {reference}")
        name, body = section_text(text, reference)
        g.require(name == record["source_name"], f"Governed name drift: {reference}")
        g.require(element.findtext("a:name", namespaces=g.N) == record["exchange_name"],
                  f"Exchange label drift: {reference}")
        g.require(hashlib.sha256(body.encode()).hexdigest() == record["documentation_sha256"],
                  f"Description drift: {reference}")
        g.require(not element.findall("a:documentation", g.N), "Baseline Documentation replacement is not authorised")
        mapping[identifier] = body
    g.require(refs == {f"EC-{n:02d}" for n in range(1, 15)}, "Expected the 14 reconciled Enterprise identities")
    return manifest, mapping


def enrich(baseline, mapping):
    result = copy.deepcopy(baseline)
    for identifier, body in mapping.items():
        element = result.find(f"a:elements/a:element[@identifier='{identifier}']", g.N)
        doc = g.ET.Element(g.tag("documentation"))
        doc.set(g.LANG, "en")
        doc.text = body
        # Exchange orders documentation after name and before properties.
        element.insert(list(element).index(element.find("a:name", g.N)) + 1, doc)
    return result


def validate_projection(baseline, result, mapping, schema, lock):
    """Restore only authorised Documentation additions, then use the original lock."""
    stripped = copy.deepcopy(result)
    for element in stripped.findall("a:elements/a:element", g.N):
        identifier = element.get("identifier")
        docs = element.findall("a:documentation", g.N)
        if identifier in mapping:
            g.require(len(docs) == 1 and docs[0].text == mapping[identifier]
                      and dict(docs[0].attrib) == {g.LANG: "en"} and not len(docs[0]),
                      f"Unauthorised Documentation: {identifier}")
            element.remove(docs[0])
    schema.assertValid(result)
    return g.validate(baseline, stripped, schema, lock)


def publish(baseline, schema, lock, mapping_path=DEFAULT_MAP, release_file=DEFAULT_RELEASE):
    release = load_release(release_file)
    g.validate_lock(baseline, lock)
    schema.assertValid(baseline)
    manifest, mapping = load_mapping(mapping_path, baseline)
    expected = enrich(baseline, mapping)
    # This ephemeral expected-content snapshot never replaces the immutable lock.
    data, report = g.publish(expected, schema, {"semantic_snapshot": g.semantic_snapshot(expected)})
    result = g.ET.fromstring(data)
    validate_projection(baseline, result, mapping, schema, lock)
    report["baseline_protected_content_sha256"] = lock["semantic_snapshot"]["protected_content_sha256"]
    report["architecture_release"] = release
    report["documentation"] = {
        "enriched_elements": len(mapping), "unresolved_elements": len(g.Model(baseline).elements) - len(mapping),
        "reference_ids": [r["reference_id"] for r in manifest["matches"]],
        "source_document": manifest["rad_source_document"], "source_sha256": manifest["source_sha256"],
        "mapping_sha256": hashlib.sha256(Path(mapping_path).read_bytes()).hexdigest(),
        "format": "verbatim Markdown section body; all qualifications retained",
    }
    report["checks"].update({k: "PASS" for k in (
        "original_baseline_lock_after_removing_only_authorised_documentation",
        "exact_unique_reference_id_matches", "governed_source_and_description_hashes",
        "no_unmatched_capability_documentation", "documentation_xml_schema")})
    return data, report


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--baseline", type=Path, default=BASE / "baseline/Harmonia-Capability-Architecture-v2.1a.xml")
    parser.add_argument("--lock", type=Path, default=BASE / "baseline/semantic-lock.json")
    parser.add_argument("--schema", type=Path, default=BASE / "schemas/archimate3_Diagram.xsd")
    parser.add_argument("--documentation-map", type=Path, default=DEFAULT_MAP)
    parser.add_argument("--release-file", type=Path, default=DEFAULT_RELEASE)
    parser.add_argument("--output", type=Path, default=BASE / "Harmonia-Capability-Architecture.xml")
    parser.add_argument("--report", type=Path, default=BASE / "validation.json")
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    try:
        manifest = json.loads(args.documentation_map.read_text())
        source = args.documentation_map.parent / manifest["source_document"]
        inputs = {p.resolve() for p in (args.baseline, args.lock, args.schema, args.documentation_map, source, args.release_file)}
        g.require(args.output.resolve() not in inputs and args.report.resolve() not in inputs
                  and args.output.resolve() != args.report.resolve(), "Output must not overwrite an input")
        baseline, schema = g.parse(args.baseline), g.load_schema(args.schema)
        lock = json.loads(args.lock.read_text())
        data, report = publish(baseline, schema, lock, args.documentation_map, args.release_file)
        if args.check:
            g.require(args.output.read_bytes() == data, "Published output differs from deterministic regeneration")
            g.require(json.loads(args.report.read_text()) == report, "Published report differs from current checks")
        else:
            args.output.write_bytes(data)
            args.report.write_text(json.dumps(report, indent=2) + "\n")
        print(json.dumps(report, indent=2))
        return 0
    except (g.PublicationError, g.ET.LxmlError, OSError, KeyError, ValueError) as exc:
        print(f"FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
