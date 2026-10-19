#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Publish two projections of the governed baseline; never infer architecture.

The immutable 1.0.5A recovery is a correspondence/continuity boundary. Current
RADS capture and explicitly selected current Motivation sections are inputs.
No historical generator is run against newer live RADS sources.
"""
import argparse
import collections
import copy
import hashlib
import json
import posixpath
from pathlib import Path
import re
import sys

import recover_v02 as r
import generate_views as g
import publish_navigation as nav
import feature_projection as fp
from architecture_release import load_release
import governed_metadata as metadata
import home_relationships as home

from workspace_paths import SYNCHRONISATION_EVIDENCE, historical_path

ROOT, BASE = r.ROOT, r.BASE
RAD = ROOT / "docs/markdown/governance/baseline"
DEST = SYNCHRONISATION_EVIDENCE
MANIFEST = RAD / "correspondence.json"
SEPARATOR = "\n\n---\n\n"
HISTORY = SEPARATOR + "## History\n\n"
VIEW_DOMAINS = ["0-Motivation", "1-Strategy", "2-BusinessLayer", "3-ApplicationLayer",
                "4-TechnologyLayer", "5-Implementation"]
GROUPS = {"business": "business-capabilities", "business_enabling": "business-enabling-capabilities",
          "enterprise": "enterprise-capabilities", "feature": "features", "function": "functions",
          "service": "services", "process": "processes"}
# Resource categories and Views have different display rules. Path keys remain
# unchanged; presentation labels never allocate semantic identity.
NAVIGATION_ORDER = {
    # Exact native-root labels let Archi consume these as category markers,
    # avoiding an extra resource folder below its built-in semantic roots.
    (): {"BusinessLayer": "Business", "ApplicationLayer": "Application"},
    ("BusinessLayer",): {"BC-Grouped-Elements": "Business Capabilities", "BEC-Grouped-Elements": "Business Enabling Capabilities"},
    ("ApplicationLayer",): {"InformationMap": "Information Map", "EC-Grouped-Elements": "Enterprise Capabilities", "ApplicationMap": "Application Map", "MiddlewareMap": "Middleware Map"},
    ("Views", "1-Strategy", "Capabilities"): {"Business Capabilities": "1-Business Capabilities", "Business Enabling Capabilities": "2-Business Enabling Capabilities", "Enterprise Capabilities": "3-Enterprise Capabilities"},
    ("Views", "2-BusinessLayer"): {"Business Capabilities": "1-Business Capabilities", "Business Enabling Capabilities": "2-Business Enabling Capabilities"},
    ("Views", "3-ApplicationLayer"): {"InformationMap": "1-InformationMap", "Enterprise Capabilities": "2-Enterprise Capabilities", "ApplicationMap": "3-ApplicationMap", "MiddlewareMap": "4-MiddlewareMap"},
}
for _parent in (("Views", "1-Strategy", "Unreconciled 1.0.4 Reference"),):
    NAVIGATION_ORDER[_parent] = {"Business Capabilities": "1-Business Capabilities", "Business Enabling Capabilities": "2-Business Enabling Capabilities", "Enterprise Capabilities": "3-Enterprise Capabilities"}


def navigation_path(path):
    path = tuple(path)
    if path[:1] == ("Relations",):
        return ("Relations", *navigation_path(path[1:]))
    return tuple(NAVIGATION_ORDER.get(path[:i], {}).get(label, label) for i, label in enumerate(path))


def classification_evidence(e, title):
    """Conservative applicability proofs in the already reconciled RAD capture.

    This classifies existing words; it does not adjudicate changed semantics.
    The explicit field-level manifest is checked against these proofs on load.
    """
    if e["record_state"] != "current":
        return "historical", "non_current_record"
    if e.get("adjudication_note") or not (e.get("definition") or e.get("governed_definition_latex")):
        return "historical", "unresolved_applicability"
    if title == "Additional Notes / Unresolved Matters":
        return "historical", "unresolved_applicability"
    previous = e.get("pre_review_record")
    if previous is None:
        return "current", "retained_explanation"
    # Exact reuse as the reviewed current definition/overview establishes this
    # field independently of changes to other fields or structural placement.
    if title.endswith(" Overview") and e["baseline_description"][title] in (e.get("definition"), e.get("overview")):
        return "current", "exact_current_text"
    if e["type"] == "feature" and e.get("reviewed_responsibility_relocation") and all(
            e.get(k) == previous.get(k) for k in ("name", "definition", "overview", "intent", "boundary", "baseline_description")):
        return "current", "responsibility_preserving_relocation"
    return "historical", "reconciled_meaning_requires_review"


def read(path):
    return json.loads(historical_path(path).read_text())


def encoded(value):
    return (json.dumps(value, ensure_ascii=False, indent=2) + "\n").encode()


def group(e):
    return GROUPS[e["category"] if e["type"] in ("capability", "enterprise_capability") else e["type"]]


def anchor(e):
    # Existing snapshot reference, used only as a documentary fragment.
    return e["reference_id"].lower()


def link(e):
    return f"[{e['name']}]({group(e)}.md#{anchor(e)})"


def section(path, heading, level):
    """Select a reviewed complete section; heading discovery does not type it."""
    raw = Path(path).read_text()
    start = raw.index(heading + "\n")
    matches = list(re.finditer(r"(?m)^#{1," + str(level) + r"} .+$", raw[start + len(heading):]))
    end = start + len(heading) + matches[0].start() if matches else len(raw)
    body = raw[start + len(heading):end].strip()
    # An explicit anchor immediately before the next heading belongs to it.
    body = re.sub(r'\n<a id="[^"]+"></a>\s*$', "", body).rstrip()
    return body.removesuffix("---").rstrip()


def documentation(e, retained=None, classification=None):
    """Definition, proven current explanation, then genuinely historical input."""
    current, history = [], []
    if retained is not None:
        current.append(retained.strip())
    elif e.get("governed_definition_latex"):
        current.append(r.text_latex(e["governed_definition_latex"]))
    elif e.get("definition"):
        current.append(e["definition"])
    else:
        current.append("Definition unresolved in the baseline. No definition has been synthesised from the name.")
    if e.get("definition") and (retained is not None or e.get("governed_definition_latex")):
        current.extend(["Definition — reviewed candidate" if e.get("reviewed_name") else "Definition — Proposed", e["definition"]])
    for title, field in (("Overview", "overview"), ("Intent", "intent"), ("Boundary", "boundary"),
                         ("Proposed overview", "proposal_overview"), ("Proposed definition", "proposal_definition"),
                         ("Proposed boundary", "proposal_boundary")):
        if e.get(field):
            current.extend(["**" + title + "**", e[field]])
    for title, value in e.get("baseline_description", {}).items():
        disposition = classification[title]["section"]
        if disposition == "current":
            # The same verbatim text may already be the reviewed definition or
            # overview. One current occurrence suffices; no words are rewritten.
            if value not in current:
                if not title.endswith(" Overview"):
                    current.append("**" + title + "**")
                current.append(value)
        else:
            history.extend(["### " + title + " — predecessor explanation", value])
    body = "\n\n".join(current)
    return body + (HISTORY + "\n\n".join(history) if history else "")


def prop_values(element):
    return {p.get("propertyDefinitionRef"): [v.text for v in p.findall("a:value", g.N)]
            for p in element.findall("a:properties/a:property", g.N)}


def set_documentation(element, text):
    doc = element.find("a:documentation", g.N)
    if doc is None:
        doc = g.ET.Element(g.tag("documentation"), {g.LANG: "en"})
        element.insert(1 if element.find("a:name", g.N) is not None else 0, doc)
    doc.text = text


def load_inputs(manifest_path=MANIFEST):
    manifest = read(manifest_path)
    for relative, expected in manifest["pinned_inputs"].items():
        path = historical_path(ROOT / relative).resolve()
        g.require(path.is_relative_to(ROOT.resolve()) and r.sha(path) == expected,
                  f"Pinned baseline input drift: {relative}")
    for relative, expected in manifest["current_authority_sha256"].items():
        path = historical_path(ROOT / relative).resolve()
        matched = r.sha(path) == expected
        if not matched:
            import information_population
            matched = information_population.preserved_authority(relative, expected)
        g.require(path.is_relative_to(ROOT.resolve()) and matched,
                  f"Current baseline authority drift; reconcile explicitly: {relative}")
    # Verify the entire recovery boundary without refreshing or generating it.
    r.load_inputs()
    source = metadata.semantic_capture(metadata.read(RAD / "architecture.json"))
    snapshot = read(ROOT / manifest["snapshot_model"])
    g.require(source == snapshot, "Canonical baseline capture differs from its reconciled source; review a new correspondence explicitly")
    motivation = metadata.semantic_capture(metadata.read(RAD / "motivation.json"))
    g.require(hashlib.sha256(metadata.historical_bytes(RAD / "motivation.json")).hexdigest() == manifest["motivation_capture_sha256"],
              "Motivation correspondence changed; review the source/type/status crosswalk explicitly")
    for e in motivation["elements"]:
        path = ROOT / e["source"]
        g.require(r.sha(path) == e["source_sha256"], f"Current Motivation source drift: {e['key']}")
        if e["record_state"] == "current":
            g.require(section(path, e["heading"], e["heading_level"]) == e["documentation"],
                      f"Current Motivation definition differs: {e['key']}")
        elif e.get("source_assertion"):
            g.require(e["source_assertion"] in path.read_text(), "Historical Motivation assertion absent from source")
    keys = {e["key"] for e in motivation["elements"]}
    g.require(len(keys) == len(motivation["elements"]), "Duplicate Motivation projection key")
    for relation in motivation["relationships"]:
        path = ROOT / relation["source"]
        g.require(relation["from"] in keys and relation["to"] in keys and
                  r.sha(path) == relation["source_sha256"] and relation["assertion"] in path.read_text(),
                  "Motivation relationship source/endpoint correspondence invalid")
    return manifest, source, motivation, g.parse(ROOT / manifest["recovery_model"]), read(ROOT / manifest["recovery_crosswalk"])


class Baseline:
    def __init__(self, manifest, source, motivation, original, crosswalk):
        self.manifest, self.source, self.motivation = manifest, source, motivation
        self.original, self.crosswalk = original, crosswalk
        self.root = copy.deepcopy(original)
        self.mid = original.get("identifier")
        self.records = {e["key"]: e for e in source["elements"]}
        classification = read(RAD / "documentation-classification.json")
        g.require(classification["source_sha256"] == hashlib.sha256(metadata.historical_bytes(RAD / "architecture.json")).hexdigest(), "Documentation classification source drift")
        self.documentation_classification = classification["entries"]
        g.require(set(self.documentation_classification) == {k for k, e in self.records.items() if e.get("baseline_description")},
                  "Missing/extra Documentation classification record")
        for key, fields in self.documentation_classification.items():
            record = self.records[key]
            g.require(set(fields) == set(record["baseline_description"]), "Missing/extra Documentation classification field")
            for title, entry in fields.items():
                section_name, evidence = classification_evidence(record, title)
                g.require(entry == {"section": section_name, "evidence": evidence,
                                    "text_sha256": hashlib.sha256(record["baseline_description"][title].encode()).hexdigest()},
                          "Unproven Documentation promotion or changed explanatory text: " + key + " / " + title)
        self.ids = {e["capture_key"]: e["identifier"] for e in crosswalk["elements"]}
        self.byid = {self.ids[k]: e for k, e in self.records.items()}
        self.mot_ids, self.homes, self.view_scopes = {}, {}, {}
        self.definitions = self.root.find("a:propertyDefinitions", g.N)
        self.moved_views, self.new_views = [], []
        self.feature_associations, self.unresolved_feature_associations = fp.associations(self)

    def ident(self, role, *keys):
        return g.visual_id(self.mid, "synchronised-baseline-v1", role, *keys)

    def property(self, element, name, value):
        if value is None:
            return
        identifier = "baseline-" + re.sub(r"[^a-z0-9]+", "-", name.lower()).strip("-")
        if self.definitions.find(f"a:propertyDefinition[@identifier='{identifier}']", g.N) is None:
            definition = g.child(self.definitions, "propertyDefinition", identifier=identifier, type="string")
            g.child(definition, "name", "Baseline / " + name)
        props = element.find("a:properties", g.N)
        if props is None:
            props = g.ET.Element(g.tag("properties"))
            before = next((c for c in element if c.tag in {g.tag(n) for n in
                          ("elements", "relationships", "organizations", "propertyDefinitions", "views", "node", "connection")}), None)
            if before is None:
                element.append(props)
            else:
                element.insert(list(element).index(before), props)
        g.require(props.find(f"a:property[@propertyDefinitionRef='{identifier}']", g.N) is None, "Duplicate structured property")
        g.child(g.child(props, "property", propertyDefinitionRef=identifier), "value",
                value if isinstance(value, str) else r.js(value))

    def chain(self, key, owner=False):
        if owner:
            key = self.records[key].get("owner_key")
        result, seen = [], set()
        while key:
            g.require(key in self.records and key not in seen, "Missing/cyclic baseline context")
            seen.add(key)
            result.insert(0, self.records[key])
            key = self.records[key].get("parent_key")
        return result

    def category(self, e):
        chain = self.chain(e["key"], owner=e["type"] not in ("capability", "enterprise_capability"))
        return e.get("category") or (chain[0]["category"] if chain else None)

    def capability_path(self, e):
        category = self.category(e)
        layer = "Application Layer" if category == "enterprise" else "Business Layer"
        state = [] if e["record_state"] == "current" else [r.STATES[e["record_state"]]]
        chain = self.chain(e["key"]) if not state else [e]
        return ["Strategy", layer, r.CATEGORIES[category], *state, *(x["name"] for x in chain)]

    def feature_ancestry(self, e):
        """Only exact current source keys establish the complete Feature frame.

        Established here means recorded in this governed candidate, including
        Proposed placement; it never promotes that placement to Approved.
        """
        chain = self.chain(e["key"], owner=True)
        if ([x.get("tier") for x in chain] == ["CT1", "CT2", "CT3"] and
                all(x["record_state"] == "current" and x.get("category") == chain[0].get("category") for x in chain)):
            return chain
        return []

    def feature_path(self, e, ancestry=None):
        chain = self.feature_ancestry(e) if ancestry is None else ancestry
        if (ancestry is not None or e["record_state"] == "current") and chain:
            return [*self.capability_path(chain[-1]), e["name"]]
        # Do not render responsibility-only evidence as direct CT1/CT2 framing.
        owner = self.records.get(e.get("owner_key"))
        category = self.category(e) if owner else "business_enabling"
        state = "Unresolved Feature Ancestry" if e["record_state"] == "current" else r.STATES[e["record_state"]]
        return ["Strategy", "Business Layer", r.CATEGORIES[category], state, e["name"]]

    def context_paths(self, e, ancestry=None):
        """Four navigation projections of one semantic subject, never copies."""
        home = self.feature_path(e, ancestry) if e["type"] == "feature" else self.capability_path(e)
        category = self.category(e) if ancestry is None else ancestry[0]['category']
        paths = [home, ["Views", "1-Strategy", "Capabilities", *home[2:]]]
        if category != "enterprise":
            paths += [["BusinessLayer", "BC-Grouped-Elements" if category == "business" else "BEC-Grouped-Elements", *home[3:]],
                      ["Views", "2-BusinessLayer", r.CATEGORIES[category], *home[3:]]]
        else:
            paths += [["ApplicationLayer", "EC-Grouped-Elements", *home[3:]]]
        return paths

    def behaviour_path(self, e):
        category = self.category(e)
        g.require(category in ("business", "business_enabling"), "Business context needs adjudication")
        state = [] if e["record_state"] == "current" else [r.STATES[e["record_state"]]]
        associated = self.feature_associations.get(e["key"], {})
        if e["record_state"] == "current" and len(associated) == 1:
            feature = self.records[next(iter(associated))]
            return [*self.context_paths(feature)[2], r.BEHAVIOURS[e["type"]]]
        return ["BusinessLayer", "BC-Grouped-Elements" if category == "business" else "BEC-Grouped-Elements",
                *state, *(x["name"] for x in self.chain(e["key"], owner=True)), r.BEHAVIOURS[e["type"]]]

    def view_path(self, identifier, path):
        if path[1] == "Unreconciled 1.0.4 Reference":
            return ["Views", "1-Strategy", *path[1:]]
        if path[1] == "Capabilities":
            # The retained Feature overview is still the same View. Its tree
            # home is the CT3 frame; no incidental framing-only folder remains.
            return ["Views", "1-Strategy", *(p for p in path[1:] if p != "Features (framing only)")]
        if path[1] == "Business":
            label = path[2]
            return ["Views", "2-BusinessLayer", label, *path[3:]]
        if path[1] == "Traceability":
            spec = self.crosswalk["views"][identifier]
            # The assertion's exact target identifies its architectural subject,
            # including historical/Proposed standing; no preferred Feature is
            # selected from the Function's other associations.
            return fp.feature_view_path(self, spec["keys"][1])
        if path[1] == "Motivation":
            return ["Views", "0-Motivation"]
        if path[1] == "Recovery Evidence":
            return ["Views", "1-Strategy", "Recovery Evidence (documentation only)", *path[2:]]
        raise g.PublicationError(f"Unmapped retained View context: {path}")

    def add_view(self, key, name, identifiers, path, relations=(), note=None):
        vid = self.ident("view", key)
        view = g.child(self.root.find("a:views/a:diagrams", g.N), "view", identifier=vid)
        view.set(g.TYPE, "Diagram")
        g.child(view, "name", name)
        g.child(view, "documentation", note or "Existing baseline elements only. Inspect Properties for standing and provenance; visual proximity creates no semantic relationship.")
        nodes = {}
        for i, semantic in enumerate(identifiers):
            nid = self.ident("node", vid, semantic)
            node = g.child(view, "node", identifier=nid, elementRef=semantic,
                           x=str(40 + i % 4 * 410), y=str(40 + i // 4 * 190), w="360", h="148")
            node.set(g.TYPE, "Element")
            g.style(node, (225, 225, 225) if key.startswith("historical") else (245, 222, 245))
            nodes[semantic] = nid
        rels = {e.get("identifier"): e for e in self.root.findall("a:relationships/a:relationship", g.N)}
        for rid in relations:
            rel = rels[rid]
            conn = g.child(view, "connection", identifier=self.ident("connection", vid, rid), relationshipRef=rid,
                           source=nodes[rel.get("source")], target=nodes[rel.get("target")])
            conn.set(g.TYPE, "Relationship")
            g.style(conn)
        if not identifiers:
            node = g.child(view, "node", identifier=self.ident("note", vid), x="40", y="40", w="1050", h="200")
            node.set(g.TYPE, "Label")
            g.child(node, "label", note)
            g.style(node, (235, 242, 248))
        self.homes[vid] = path
        self.new_views.append(vid)
        self.view_scopes[vid] = {"elements": list(identifiers), "relationships": list(relations)}
        return vid

    def build(self):
        self.root.find("a:name", g.N).text = "Harmonia Architecture — Synchronised Markdown / ArchiMate Baseline"
        set_documentation(self.root, "The Harmonia governed architecture baseline, represented in Markdown and ArchiMate. "
                          "Currentness and governance standing are independent: Current does not mean Approved. "
                          "Unreconciled 1.0.4 Reference is historical comparison evidence. "
                          "Folders and numeric View prefixes are navigation only. Application/Technology coverage gaps remain explicit.")
        self.property(self.root, "Architecture Release", metadata.release()["version"])
        self.property(self.root, "Canonical Textual Projection", "docs/markdown/governance/baseline/README.md")
        elements = {e.get("identifier"): e for e in self.root.findall("a:elements/a:element", g.N)}
        old_homes = nav.navigation_homes(self.original)
        for identifier, e in self.byid.items():
            element = elements[identifier]
            retained = None
            if e["reference_id"] in {f"EC-{i:02}" for i in range(1, 15)}:
                retained = element.findtext("a:documentation", namespaces=g.N).split("\n\n----- LaTeX RAD v0.2 recovery -----")[0]
            set_documentation(element, documentation(e, retained, self.documentation_classification.get(e["key"])))
            if e["type"] in ("capability", "enterprise_capability"):
                self.homes[identifier] = self.capability_path(e)
            elif e["type"] == "feature":
                self.homes[identifier] = self.feature_path(e)
            else:
                self.homes[identifier] = self.behaviour_path(e)
            self.property(element, "Textual Baseline Reference", f"docs/markdown/governance/baseline/{group(e)}.md#{anchor(e)}")
        for identifier, element in elements.items():
            if identifier not in self.homes:
                self.homes[identifier] = list(old_homes[identifier][0])
                if "Unreconciled 1.0.4 Reference" in self.homes[identifier]:
                    self.property(element, "Record State", "historical_reference")
                    self.property(element, "Governance State", "Unreconciled — no proven baseline crosswalk")
        # Reuse existing five axiom IDs. Snapshot Properties stay untouched and
        # are explicitly version-qualified; current Properties are separate.
        principles = {e.findtext("a:name", namespaces=g.N).split()[0]: e.get("identifier")
                      for e in elements.values() if e.get(g.TYPE) == "Principle"}
        for e in self.motivation["elements"]:
            identifier = principles.get(e.get("native_id")) or self.ident("motivation", e["key"])
            self.mot_ids[e["key"]] = identifier
            element = elements.get(identifier)
            if element is None:
                element = g.child(self.root.find("a:elements", g.N), "element", identifier=identifier)
                element.set(g.TYPE, e["type"])
                g.child(element, "name", e["name"])
                elements[identifier] = element
            history = e.get("history", "")
            set_documentation(element, e["documentation"] + (HISTORY + history if history else ""))
            for field, value in {"Native Definition ID": e.get("native_id"), "Record State": e["record_state"],
                                 "Architectural Status": e["status"], "Source Path": e["source"],
                                 "Source Section": e["heading"], "Source SHA256": e["source_sha256"],
                                 "Architecture Identity Status": "Unresolved — representation ID only",
                                 "Source Projection Key": e["key"], "Review State": e.get("review_state", "Retained baseline standing")}.items():
                self.property(element, field, value)
            self.homes[identifier] = ["Motivation", *( ["Historical Motivation"] if e["record_state"] != "current" else []), e["subject"]]
        for entry in self.motivation["relationships"]:
            identifier = self.ident("motivation-relationship", entry["key"])
            rel = g.child(self.root.find("a:relationships", g.N), "relationship", identifier=identifier,
                          source=self.mot_ids[entry["from"]], target=self.mot_ids[entry["to"]])
            rel.set(g.TYPE, entry["type"])
            g.child(rel, "name", entry["predicate"])
            g.child(rel, "documentation", entry["assertion"])
            for field, value in {"Source Predicate": entry["predicate"], "Source Path": entry["source"],
                                 "Source SHA256": entry["source_sha256"], "Source Assertion": entry["assertion"],
                                 "Record State": entry["record_state"], "Architectural Status": entry["status"]}.items():
                self.property(rel, field, value)
        # Relations follow the source's actual home, including legacy edges
        # whose established EC source now has a reconciled Strategy home.
        for rel in self.root.findall("a:relationships/a:relationship", g.N):
            self.homes[rel.get("identifier")] = ["Relations", *self.homes[rel.get("source")]]
        for view in self.root.findall("a:views/a:diagrams/a:view", g.N):
            identifier = view.get("identifier")
            path = list(old_homes[identifier][0])
            self.homes[identifier] = self.view_path(identifier, path)
            self.moved_views.append(identifier)
        for subject in sorted({e["subject"] for e in self.motivation["elements"] if e["record_state"] == "current"}):
            ids = [self.mot_ids[e["key"]] for e in self.motivation["elements"] if e["subject"] == subject and e["record_state"] == "current"]
            self.add_view("current:" + subject, subject, ids, ["Views", "0-Motivation"])
        for requirement in [e for e in self.motivation["elements"] if e["type"] == "Requirement" and e["record_state"] == "current"]:
            entries = [e for e in self.motivation["relationships"] if e["to"] == requirement["key"] and e["record_state"] == "current"]
            if entries:
                ids = [self.mot_ids[requirement["key"]], *dict.fromkeys(self.mot_ids[e["from"]] for e in entries)]
                self.add_view("governance:" + requirement["key"], requirement["name"] + " — co-governance", ids,
                              ["Views", "0-Motivation", "Requirement Co-governance"],
                              [self.ident("motivation-relationship", e["key"]) for e in entries])
        hist_rels = [self.ident("motivation-relationship", e["key"]) for e in self.motivation["relationships"] if e["record_state"] != "current"]
        diagram_keys = {e["key"] for e in self.motivation["elements"] if e["subject"] == "Legacy Motivation Diagram"}
        self.add_view("historical:motivation", "Historical Motivation — legacy source assertions", [self.mot_ids[k] for k in sorted(diagram_keys)],
                      ["Views", "0-Motivation"], hist_rels,
                      note="Historical source assertions only; not current architectural intent. Source labels and asserted native types are retained. Current applicability and crosswalks remain unresolved except explicit supersession recorded in Properties.")
        for subject in sorted({e["subject"] for e in self.motivation["elements"] if e["record_state"] != "current" and e["subject"] != "Legacy Motivation Diagram"}):
            entries = [e for e in self.motivation["elements"] if e["subject"] == subject and e["record_state"] != "current"]
            for start in range(0, len(entries), 16):
                self.add_view("historical:" + subject + ":" + str(start // 16 + 1), subject + f" — {start // 16 + 1}",
                              [self.mot_ids[e["key"]] for e in entries[start:start+16]],
                              ["Views", "0-Motivation", "Historical Motivation"])
        for relative in self.manifest["pinned_inputs"]:
            if relative.startswith("docs/latex-legacy/"):
                vid = self.add_view("source-evidence:" + relative, "Source evidence — " + Path(relative).stem, [],
                                    ["Views", "0-Motivation", "Historical Source Evidence"],
                                    note="Historical documentation only. Select this View in the tree and read Documentation for the exact original source. Source presentation supplies no current architecture authority.")
                view = self.root.find(f"a:views/a:diagrams/a:view[@identifier='{vid}']", g.N)
                set_documentation(view, "Historical source evidence, preserved verbatim.\n\n" + (ROOT / relative).read_text())
                self.property(view, "Source Path", relative)
                self.property(view, "Source SHA256", r.sha(ROOT / relative))
        for subject in ("InformationMap", "Enterprise Capabilities", "ApplicationMap", "MiddlewareMap"):
            self.add_view("coverage:" + subject, subject + " — representation boundary", [],
                          ["Views", "3-ApplicationLayer", subject], note=
                          "NOT YET REPRESENTED / FUTURE ARCHITECTURE EXPANSION\n\n"
                          "The recovery has no native Application-layer objects or approved publication crosswalk for this projection. "
                          "Current RADS Domain04/05 contains architecture and unresolved candidates; it has not been allocated new semantic IDs by this task. "
                          "See docs/markdown/governance/baseline/coverage.md. No allocation or missing semantics is inferred from this empty canvas.")
        fp.landscapes(self)
        fp.business_layout(self)
        home.apply(self)
        self.organizations()
        return self.root

    def organizations(self):
        org = self.root.find("a:organizations", g.N)
        org.clear()
        folders, subjects = {}, {}
        # A governed context has stable source identity. Folder keys use that
        # identity and projection role, never its mutable name or ancestry.
        for key, e in self.records.items():
            if e["type"] not in ("capability", "enterprise_capability", "feature"):
                continue
            for path in self.context_paths(e):
                subjects[tuple(path)] = e["reference_id"]
        records_by_reference = {e["reference_id"]: e for e in self.records.values()}
        for rel in self.root.findall("a:relationships/a:relationship", g.N):
            source_path = self.homes[rel.get("source")]
            for depth in range(1, len(source_path)+1):
                prefix = tuple(source_path[:depth])
                if prefix in subjects:
                    subjects[("Relations", *prefix)] = subjects[prefix]
        def folder(path):
            parent = org
            for depth, label in enumerate(path, 1):
                prefix = tuple(path[:depth])
                if prefix not in folders:
                    bound = [(i, subjects[tuple(path[:i])]) for i in range(1, depth+1) if tuple(path[:i]) in subjects]
                    if bound:
                        index, subject = bound[-1]
                        # Prefix role distinguishes independent projections of
                        # the same context without asserting semantic copies.
                        role_depth = 3 if path[0] in ("Views", "Relations") else 2
                        role = "/".join(path[:role_depth])
                        keys = [role, subject, *path[index:depth]]
                    else:
                        keys = path[:depth]
                    display = navigation_path(prefix)[-1]
                    if prefix in subjects:
                        record = records_by_reference[subjects[prefix]]
                        tier = "Feature" if record["type"] == "feature" else record.get("tier")
                        display = f"[{tier or 'Tier unresolved'}] {record['name']}"
                    folders[prefix] = g.folder(parent, self.ident("folder", *keys), display)
                parent = folders[prefix]
            return parent
        required = [["Strategy", "Business Layer", x] for x in ("Business Capabilities", "Business Enabling Capabilities")]
        required += [["Strategy", "Application Layer", x] for x in ("Enterprise Capabilities", "Application Capabilities")]
        required += [["Strategy", "Unreconciled 1.0.4 Reference"]]
        required += [["BusinessLayer", x] for x in ("BC-Grouped-Elements", "BEC-Grouped-Elements")]
        required += [["ApplicationLayer", x] for x in ("InformationMap", "EC-Grouped-Elements", "ApplicationMap", "MiddlewareMap")]
        required += [["Views", x] for x in VIEW_DOMAINS]
        required += [["Views", "2-BusinessLayer", x] for x in ("Business Capabilities", "Business Enabling Capabilities")]
        for path in required:
            folder(path)
        # Capability contexts only; no additional semantic Capability references.
        # Current decomposition stops exactly where the governed source stops.
        for e in self.records.values():
            if e["type"] not in ("capability", "enterprise_capability", "feature") or e["record_state"] != "current":
                continue
            for path in self.context_paths(e):
                folder(path)
        for identifier, path in sorted(self.homes.items(), key=lambda p: (p[1], p[0])):
            g.child(folder(path), "item", identifier=self.ident("reference", identifier), identifierRef=identifier)
        # Serialise the same displayed sibling order Archi uses. Do not number
        # semantic-context descendants or manufacture a decomposition order.
        paths_by_folder = {id(f): p for p, f in folders.items()}
        view_names = {v.get("identifier"): v.findtext("a:name", namespaces=g.N) for v in self.root.findall("a:views/a:diagrams/a:view", g.N)}
        for parent in [org, *folders.values()]:
            items = parent.findall("a:item", g.N)
            for item in items:
                parent.remove(item)
            path = paths_by_folder.get(id(parent), ())
            first = self.landscape_first.get(path)
            for item in sorted(items, key=lambda e: (bool(e.get("identifierRef")),
                               e.get("identifierRef") != first,
                               e.findtext("a:label", default=view_names.get(e.get("identifierRef"), ""), namespaces=g.N), e.get("identifier"))):
                parent.append(item)
        self.display_paths = {path: tuple(folders[path[:i]].findtext("a:label", namespaces=g.N)
                                        for i in range(1, len(path)+1)) for path in folders}
        self.folder_paths = set(self.display_paths.values())


def counts(root):
    return {"elements": len(root.findall("a:elements/a:element", g.N)),
            "relationships": len(root.findall("a:relationships/a:relationship", g.N)),
            "views": len(root.findall("a:views/a:diagrams/a:view", g.N)),
            "element_types": dict(collections.Counter(e.get(g.TYPE) for e in root.findall("a:elements/a:element", g.N)))}


def validate_projection_preservation(root, b=None):
    """Compare with the actual accepted starting candidate, not recovery counts."""
    proof = read(DEST / "human-inspection-start-preservation.json")
    # The accepted Home migration adds only its literal, independently validated
    # assertions and their specialisation definition to this historical boundary.
    # Never exempt arbitrary Association edges or weaken the old digest check.
    additions = {"relationships": set(), "property_definitions": set()}
    if b is not None and hasattr(b, "business_homes"):
        home.validate(b, root)
        additions["relationships"] = {r["relationship_identifier"] for r in b.business_homes["records"]
                                       if r["status"] == "normalised"}
        additions["property_definitions"] = {"specialization"}
    for name, xpath in (("elements", "a:elements/a:element"), ("relationships", "a:relationships/a:relationship"),
                        ("property_definitions", "a:propertyDefinitions/a:propertyDefinition")):
        records = {e.get("identifier"): g.signature(e) for e in root.findall(xpath, g.N)
                   if e.get("identifier") not in additions.get(name, set())}
        g.require(len(records) == proof["checks"][name]["count"] and
                  g.digest(records) == proof["checks"][name]["sha256"],
                  "Starting candidate changed protected semantic/Documentation/View content: " + name)
    views = {v.get("identifier"): v for v in root.findall("a:views/a:diagrams/a:view", g.N)}
    g.require(set(proof["views"]) <= set(views), "Existing View identity lost")
    if b is not None:
        for vid, digest in proof["views"].items():
            if vid not in b.presentation_changes:
                g.require(g.digest(g.signature(views[vid])) == digest, "Unrelated existing View changed")
        # Changed existing Views still retain their original node/connection IDs,
        # styling, Documentation and metadata. Only authorised display fields
        # and the explicitly validated association population may change.
        for old in b.original.findall("a:views/a:diagrams/a:view", g.N):
            vid = old.get("identifier")
            if vid not in b.presentation_changes:
                continue
            actual = copy.deepcopy(views[vid])
            expected = copy.deepcopy(old)
            if b.presentation_changes[vid] == "landscape_name":
                actual.find("a:name", g.N).text = expected.findtext("a:name", namespaces=g.N)
                g.require(g.signature(actual) == g.signature(expected), "Landscape changed more than its presentation name")
            else:
                for tag_name, fields in (("node", ("x", "y")), ("connection", ("source", "target"))):
                    before = {x.get("identifier"): x for x in expected.findall("a:" + tag_name, g.N)}
                    after = {x.get("identifier"): x for x in actual.findall("a:" + tag_name, g.N)}
                    g.require(set(before) <= set(after), "Existing visual node/connection identity lost")
                    for identifier, item in before.items():
                        revised = after[identifier]
                        for field in fields:
                            revised.set(field, item.get(field))
                        g.require(g.signature(revised) == g.signature(item), "Business layout changed protected visual content")
                for view in (actual, expected):
                    for item in list(view):
                        if item.tag in (g.tag("node"), g.tag("connection")):
                            view.remove(item)
                g.require(g.signature(actual) == g.signature(expected), "Business View metadata changed")
    for relative, expected in proof["protected_files"].items():
        g.require(hashlib.sha256(metadata.historical_bytes(ROOT / relative)).hexdigest() == expected,
                  "Projection changed protected source/recovery/identity/release: " + relative)
    return {name: entry["count"] for name, entry in proof["checks"].items()}


def validate_capability_navigation(b, root):
    """Check actual folders against exact governed keys, independently by taxonomy.

    Capability/Feature folders in Business provide context, not a second element
    home or a new ArchiMate Business type. Empty leaf folders are intentional.
    """
    org = root.find("a:organizations", g.N)
    folders = {}
    def walk(parent, path=()):
        for item in parent.findall("a:item", g.N):
            if not item.get("identifierRef"):
                current = (*path, item.findtext("a:label", namespaces=g.N))
                folders[current] = item
                walk(item, current)
    walk(org)
    def labels(chain):
        return tuple(f"[{'Feature' if x['type'] == 'feature' else x.get('tier') or 'Tier unresolved'}] {x['name']}" for x in chain)
    def roots(category):
        title = r.CATEGORIES[category]
        return (("Strategy", "Business Layer", title),
                ("Business", title),
                ("Views", "1-Strategy", "Capabilities", NAVIGATION_ORDER[("Views", "1-Strategy", "Capabilities")][title]),
                ("Views", "2-BusinessLayer", NAVIGATION_ORDER[("Views", "2-BusinessLayer")][title]))
    homes = nav.navigation_homes(root)
    result = {}
    for category in ("business", "business_enabling"):
        capabilities = [e for e in b.records.values() if e["type"] == "capability" and e.get("category") == category and e["record_state"] == "current"]
        features = [e for e in b.records.values() if e["type"] == "feature" and e["record_state"] == "current" and b.category(e) == category]
        tiers = collections.Counter(e.get("tier") or "unresolved" for e in capabilities)
        for e in capabilities:
            chain = b.chain(e["key"])
            known = e.get("tier") in ("CT1", "CT2", "CT3")
            if known:
                g.require([x.get("tier") for x in chain] == ["CT1", "CT2", "CT3"][:int(e["tier"][-1])],
                          "Capability tier/ancestry mismatch: " + e["key"])
            for prefix in roots(category):
                g.require((*prefix, *labels(chain)) in folders, "Capability tier classification/navigation missing: " + e["key"])
        reached, unresolved = [], []
        for e in features:
            chain = b.feature_ancestry(e)
            if chain:
                g.require(b.ids[e["key"]] in homes, "Feature semantic element missing: " + e["key"])
                for prefix in roots(category):
                    g.require((*prefix, *labels([*chain, e])) in folders,
                              "Feature complete ancestry reachability missing in " + "/".join(prefix) + ": " + e["key"])
                reached.append(e["key"])
            else:
                unresolved.append({"key": e["key"], "owner_key": e.get("owner_key"), "reason": "Complete current CT1 -> CT2 -> CT3 ancestry not established"})
                for prefix in roots(category):
                    g.require((*prefix, "Unresolved Feature Ancestry", *labels([e])) in folders,
                              "Unresolved Feature ancestry must remain visible: " + e["key"])
                for path in folders:
                    if path[-1:] == labels([e]):
                        g.require(path[-2] == "Unresolved Feature Ancestry", "Invented Feature ancestry: " + e["key"])
        result[category] = {"capability_tiers": dict(tiers), "current_features": len(features),
                            "established_ct3_ancestry": len(reached), "complete_ancestry_reachable": len(reached),
                            "unresolved_ancestry": unresolved, "projections_checked": [list(x) for x in roots(category)]}
    # Enterprise Capabilities are a separate governed catalogue; classification
    # must remain visible there too, without adding Business Feature semantics.
    enterprise_tiers = collections.Counter()
    for e in b.records.values():
        if e["type"] == "enterprise_capability" and e["record_state"] == "current":
            enterprise_tiers[e.get("tier") or "unresolved"] += 1
            for prefix in (("Strategy", "Application Layer", "Enterprise Capabilities"),
                           ("Application", "Enterprise Capabilities"),
                           ("Views", "1-Strategy", "Capabilities", "3-Enterprise Capabilities")):
                g.require((*prefix, *labels(b.chain(e["key"]))) in folders,
                          "Enterprise Capability tier classification/navigation missing: " + e["key"])
    # View-only global ordering; resource prefixes cannot slip back in via a
    # shared display helper. Native Archi semantic roots are supplied by Archi.
    view = folders[("Views",)]
    g.require([x.findtext("a:label", namespaces=g.N) for x in view.findall("a:item", g.N)] == VIEW_DOMAINS,
              "View global numbering/order changed")
    for path in folders:
        if path[0] != "Views":
            g.require(not any(re.match(r"^\d+-", x) for x in path), "Redundant resource global layer numbering")
    return result


def validate(b, root, *, legacy_replay=False):
    if hasattr(b, 'information_index') and not legacy_replay:
        import information_population
        return information_population.validate(b, root)
    if hasattr(b, 'reconciliation') and not legacy_replay:
        import reconcile_108
        if hasattr(b, 'view_presentation'):
            import view_presentation
            view_presentation.audit(b.view_presentation_before, root, b.presentation_scope)
        return reconcile_108.validate(b, root)
    g.load_schema(BASE / "schemas/archimate3_Diagram.xsd").assertValid(root)
    g.validate_identifiers(root)
    if metadata.active() and not legacy_replay:
        metadata.validate_publication(b, root)
        root = metadata.remove_properties(root)
    elements = {e.get("identifier"): e for e in root.findall("a:elements/a:element", g.N)}
    rels = {e.get("identifier"): e for e in root.findall("a:relationships/a:relationship", g.N)}
    views = {e.get("identifier"): e for e in root.findall("a:views/a:diagrams/a:view", g.N)}
    expected = {e.get("identifier"): e for e in b.root.findall("a:elements/a:element", g.N)}
    g.require(set(elements) == set(expected), "Unexpected/missing semantic element; navigation must not recreate Capabilities")
    g.require(set(rels) == set(b.homes) - set(elements) - set(views), "Unexpected/missing semantic relationship")
    homes = nav.navigation_homes(root)
    g.require(set(homes) == set(b.homes) and all(len(x) == 1 for x in homes.values()), "Duplicate/missing navigation homes")
    for identifier, path in b.homes.items():
        g.require(homes[identifier] == [b.display_paths[tuple(path)]], f"Incorrect source-based navigation: {identifier}")
    # Independent preservation comparison, rather than counts alone.
    for before in b.original.findall("a:elements/a:element", g.N):
        identifier = before.get("identifier")
        after = elements[identifier]
        g.require(after.get(g.TYPE) == before.get(g.TYPE) and after.findtext("a:name", namespaces=g.N) == before.findtext("a:name", namespaces=g.N),
                  "Existing semantic identity/type/name changed")
        props = prop_values(after)
        g.require(all(props.get(k) == v for k, v in prop_values(before).items()), "Structured Properties lost or changed")
    for identifier, element in elements.items():
        g.require(g.signature(element) == g.signature(expected[identifier]), "Definition, status, Properties or Documentation convention changed")
    expected_rels = {e.get("identifier"): e for e in b.root.findall("a:relationships/a:relationship", g.N)}
    for identifier, rel in rels.items():
        g.require(g.signature(rel) == g.signature(expected_rels[identifier]), "Relationship assertion/endpoint changed")
        g.require(rel.get("source") in elements and rel.get("target") in elements, "Dangling semantic endpoint")
        g.require(homes[identifier] == [("Relations", *homes[rel.get("source")][0])], "Relationship home must follow source")
    for before in b.original.findall("a:relationships/a:relationship", g.N):
        g.require(g.signature(rels[before.get("identifier")]) == g.signature(before), "Existing relationship changed")
    expected_views = {e.get("identifier"): e for e in b.root.findall("a:views/a:diagrams/a:view", g.N)}
    g.require(set(views) == set(expected_views), "Missing/extra View")
    for identifier, view in views.items():
        g.require(g.signature(view) == g.signature(expected_views[identifier]), "View scope or visual identity changed")
        nodes = {n.get("identifier"): n for n in view.findall(".//a:node", g.N)}
        for node in nodes.values():
            if node.get(g.TYPE) == "Element":
                g.require(node.get("elementRef") in elements, "Invalid View elementRef")
        for conn in view.findall("a:connection", g.N):
            rel = rels.get(conn.get("relationshipRef"))
            g.require(rel is not None and conn.get("source") in nodes and conn.get("target") in nodes, "Invalid visual relationship reference")
            g.require(nodes[conn.get("source")].get("elementRef") == rel.get("source") and
                      nodes[conn.get("target")].get("elementRef") == rel.get("target"), "Invalid visual relationship endpoints")
    for old in b.original.findall("a:views/a:diagrams/a:view", g.N):
        if old.get("identifier") not in b.presentation_changes:
            g.require(g.signature(views[old.get("identifier")]) == g.signature(old), "Retained View changed")
    # Validate actual folders, including empty coverage contexts.
    paths = set()
    def walk(parent, path=()):
        labels = []
        for item in parent.findall("a:item", g.N):
            if not item.get("identifierRef"):
                label = item.findtext("a:label", namespaces=g.N)
                g.require(label not in labels, "Duplicate sibling navigation folder")
                labels.append(label)
                paths.add((*path, label))
                walk(item, (*path, label))
        if any(re.match(r"^\d+-", label) for label in labels):
            g.require(labels == sorted(labels), "Incorrect numeric navigation sibling order")
    walk(root.find("a:organizations", g.N))
    g.require(paths == b.folder_paths, "Resource/View tree integrity changed")
    for identifier, e in b.byid.items():
        if e["type"] in ("capability", "enterprise_capability", "feature"):
            g.require(homes[identifier][0][0] == "Strategy", "Capability duplicated/moved into Business/Application resources")
    for prop in root.findall(".//a:property", g.N):
        ref = prop.get("propertyDefinitionRef")
        g.require(root.find(f"a:propertyDefinitions/a:propertyDefinition[@identifier='{ref}']", g.N) is not None, "Invalid property definition reference")
    validate_capability_navigation(b, root)
    fp.validate(b, root)
    home.validate(b, root)
    validate_projection_preservation(root, b)
    return counts(root)


def markdown(b):
    output = {}
    groups = collections.defaultdict(list)
    for e in b.records.values():
        groups[group(e)].append(e)
    for name, records in groups.items():
        lines = ["<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->", "",
                 "# Baseline — " + name.replace("-", " ").title(), "",
                 "[Baseline authority and reading guide](README.md). Currentness does not imply approval. "
                 "Definitions are reproduced without semantic rewriting. IDs below retain their distinct roles.", "",
                 "## Catalogue", ""]
        ordered = sorted(records, key=lambda e: (e["record_state"], [x["name"] for x in b.chain(e["key"], owner=e["type"] not in ("capability", "enterprise_capability"))], e["name"], e["key"]))
        for e in ordered:
            lines.append(f"- [{e['name']}](#{anchor(e)}) — {e['architectural_status']}; {e['record_state']}; placement {e['placement_status']}")
        for e in ordered:
            element = b.root.find(f"a:elements/a:element[@identifier='{b.ids[e['key']]}']", g.N)
            body = element.findtext("a:documentation", namespaces=g.N)
            if e["reference_id"] in {f"EC-{i:02}" for i in range(1, 15)}:
                # The full retained EC source is verbatim architecture prose.
                # Rebase only its relative hyperlinks for this textual location.
                source_directory = "docs/markdown/02-strategy/capabilities"
                def rebase(match):
                    target = match[2]
                    if target.startswith(("#", "/")) or ":" in target:
                        return match[0]
                    path, marker, fragment = target.partition("#")
                    relocated = posixpath.relpath(posixpath.normpath(source_directory + "/" + path), "docs/markdown/governance/baseline")
                    return f"[{match[1]}]({relocated}{marker}{fragment})"
                body = re.sub(r"\[([^\]]+)\]\(([^)]+)\)", rebase, body)
            lines += ["", f'<a id="{anchor(e)}"></a>', "", "## " + e["name"], "",
                      body, "", "### Governed record", "",
                      "| Field | Value |", "| --- | --- |"]
            fields = {"Source capture key": e["key"], "Snapshot publication reference": e["reference_id"],
                      "ArchiMate semantic ID": b.ids[e["key"]], "Architecture Identity": e["reference_id"] if e["reference_id"].startswith("EC-") else "Unresolved — no allocation",
                      "Construct": e["type"], "Tier/classification": e.get("tier"), "Architectural standing": e["architectural_status"],
                      "Name standing": e.get("name_status"), "Canonical name": e.get("canonical_name"), "Reviewed name": e.get("reviewed_name"),
                      "Record currentness": e["record_state"], "Placement standing": e["placement_status"],
                      "Structural ID standing": e["structural_id_status"], "Structural Canonical ID": e.get("canonical_id"),
                      "Structural aliases": e.get("id_aliases"), "Name aliases": e.get("name_aliases"),
                      "Unresolved dimensions": e.get("unresolved"), "Human adjudication": e.get("adjudication_note"),
                      "Release scope": e.get("release_scope")}
            for title, value in fields.items():
                if value is not None and value != []:
                    if isinstance(value, (list, dict)):
                        value = r.js(value)
                    lines.append(f"| {title} | {str(value).replace('|', '&#124;').replace(chr(10), '<br>')} |")
            for title, field in (("Parent", "parent_key"), ("Responsibility/framing context", "owner_key")):
                if e.get(field):
                    lines += ["", f"{title}: {link(b.records[e[field]])}. This assertion retains {e['placement_status']} placement standing."]
            children = [c for c in b.records.values() if c.get("parent_key") == e["key"] and c["record_state"] == "current"]
            if children:
                lines += ["", "Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):", ""]
                lines += [f"- {link(c)} — {c['architectural_status']}; placement {c['placement_status']}" for c in sorted(children, key=lambda c: c["name"])]
            elif e["type"] in ("capability", "enterprise_capability"):
                lines += ["", "No further governed Capability decomposition recorded in this baseline. The anchor View is retained."]
            lines += ["", "Exact source locators, provenance, name/identity history and deferred assertions are retained in "
                      f"[architecture.json](architecture.json) (record `{e['key']}`) and the element's version-qualified Properties."]
        output[RAD / (name + ".md")] = ("\n".join(lines) + "\n").encode()
    lines = ["# Baseline relationship assertions", "", "All source assertions, including unresolved and historical assertions, are retained. "
             "Native mapping does not promote assertion standing. Metadata-only rows are UNRESOLVED BY ARCHITECTURAL DECISION; absence of an edge is deliberate.", "",
             "| Source | Predicate | Target | Standing | Currentness | Representation |", "| --- | --- | --- | --- | --- | --- |"]
    states = {e["relationship_index"]: e["record_state"] for e in b.source["relationship_currentness"]}
    native = {e["source_index"]: e for e in b.crosswalk["relationships"]}
    for i, e in enumerate(b.source["relationships"]):
        lines.append(f"| {link(b.records[e['from']])} | {e['type']} | {link(b.records[e['to']])} | {e['status']} | {states[i]} | " +
                     (f"{native[i]['archimate_type']} `{native[i]['identifier']}`" if i in native else "Metadata and finding; native profile/authority unresolved") + " |")
    lines.extend(home.markdown(b, link))
    output[RAD / "relationships.md"] = ("\n".join(lines) + "\n").encode()
    lines = ["# Baseline Motivation correspondence", "", "[Reading guide](README.md). Definitions remain in their canonical current sources. "
             "Historical source constructs remain historical. Publication keys and XML IDs allocate no persistent Architecture Identities.", "",
             "| Element | Type | Standing | Currentness | Source | Semantic ID |", "| --- | --- | --- | --- | --- | --- |"]
    for e in b.motivation["elements"]:
        # RAD baseline is four directories below repository root.
        source_link = "../../../../" + e["source"]
        lines.append(f"| {e['name']} | {e['type']} | {e['status']} | {e['record_state']} | [{e['heading']}]({source_link}) | `{b.mot_ids[e['key']]}` |")
    output[RAD / "motivation.md"] = ("\n".join(lines) + "\n").encode()
    return output


def discrepancies(b):
    previous = read(ROOT / "docs/markdown/03-business-architecture/reference-model/business-architecture.json")
    common = {e["key"]: e for e in previous["elements"]}
    g.require(set(common) <= set(b.records), "Missing earlier RAD capture record; correspondence needs adjudication")
    return {"authority": "Exact capture keys and explicit reviewed source decisions; no name-based identity reconciliation",
            "earlier_capture_records": len(common), "exact_key_continuities": len(common),
            "deterministically_reconciled_names": [{"key": k, "before": old["name"], "after": b.records[k]["name"],
                                                     "currentness": b.records[k]["record_state"],
                                                     "source": "architecture.json / reviewed_bec_reconciliation / decisions"}
                                                    for k, old in common.items() if old["name"] != b.records[k]["name"]],
            "human_adjudication_preserved": [{"key": e["key"], "reference_id": e["reference_id"], "name": e["name"],
                                               "standing": e["architectural_status"], "currentness": e["record_state"],
                                               "note": e["adjudication_note"]} for e in b.records.values() if e.get("adjudication_note")],
            "definition_gaps": [{"key": e["key"], "name": e["name"]} for e in b.records.values()
                                if not (e.get("governed_definition_latex") or e.get("definition"))],
            "unresolved_dimensions": [{"key": e["key"], "dimensions": e["unresolved"]} for e in b.records.values() if e.get("unresolved")],
            "historical_motivation_mapping_limits": b.motivation["deferred"],
            "other_preserved_review_items": ["214 unproven 1.0.4 crosswalks; no label-based equivalence", "CM-R05 Exchange naming qualification/conflict remains in snapshot name history and source notes; display rename is not closure", "Client consent / FEAT-EM-09 ownership history retained", "F46 and Domain05 C/F/D/M identity/type ambiguities remain in current RADS", "462 source relationship assertions retain their exact metadata-only findings", "Current Application/Information/Middleware/Technology semantic publication is future expansion, not absent RADS architecture"]}


def publish(*, reconciled=True, home_derived=True, presentation=True, information=True):
    b = Baseline(*load_inputs())
    b.build()
    data = g.serialize(b.root)
    # Validate a separate parse, never the same mutable object as expected.
    result = validate(b, g.ET.fromstring(data), legacy_replay=True)
    md = markdown(b)
    md[RAD / "discrepancies.json"] = encoded(discrepancies(b))
    old_ids = {e.get("identifier") for e in b.original.findall("a:elements/a:element", g.N)}
    old_views = {e.get("identifier") for e in b.original.findall("a:views/a:diagrams/a:view", g.N)}
    promoted = {key: [title for title, entry in fields.items() if entry["section"] == "current"]
                for key, fields in b.documentation_classification.items()}
    historical = [{"key": key, "name": b.records[key]["name"], "field": title, "reason": entry["evidence"]}
                  for key, fields in b.documentation_classification.items()
                  for title, entry in fields.items() if entry["section"] == "historical"]
    report = {"status": "PASS", "architecture_release": metadata.release(), "before": counts(b.original), "after": result,
              "existing_semantic_ids_retained": len(old_ids), "semantic_ids_removed": 0, "persistent_identities_allocated": 0,
              "adopted_architecture_identities_preserved": 14, "navigation_capability_duplicates_removed": 0,
              "existing_capability_population_retained": sum(e.get(g.TYPE) == "Capability" for e in b.original.findall("a:elements/a:element", g.N)),
              "existing_views_retained_and_moved": len(old_views), "views_added": b.new_views,
              "motivation_by_type_and_state": dict(collections.Counter(e["type"] + " / " + e["record_state"] + " / " + e["status"] for e in b.motivation["elements"])),
              "baseline_record_states": dict(collections.Counter(e["record_state"] for e in b.records.values())),
              "baseline_record_statuses": dict(collections.Counter(e["architectural_status"] for e in b.records.values())),
              "source_assertions": len(b.source["relationships"]), "native_source_assertions_retained": len(b.crosswalk["relationships"]),
              "unresolved_source_assertions_retained": len(b.source["relationships"])-len(b.crosswalk["relationships"]),
              "output_sha256": hashlib.sha256(data).hexdigest(),
              "markdown_sha256": {str(p.relative_to(ROOT)): hashlib.sha256(v).hexdigest() for p, v in md.items()},
              "resource_and_view_folder_count": len(b.folder_paths),
              "capability_navigation": validate_capability_navigation(b, g.ET.fromstring(data)),
              "starting_candidate_preservation": validate_projection_preservation(g.ET.fromstring(data), b),
              "human_inspection_projection": fp.validate(b, g.ET.fromstring(data)),
              "human_workbench": {"supported_version": "Archi 5.10.0", "gui_acceptance": "Pending Solution Architect's final import and inspection", "schema": "Existing bundled Open Exchange schema unchanged"},
              "documentation_classification": {"entries_reclassified": sum(bool(fields) for fields in promoted.values()),
                  "explanatory_fields_reclassified": sum(map(len, promoted.values())),
                  "historical_fields_retained": len(historical), "historical_entries_retained": len({e["key"] for e in historical}),
                  "retained_historical_material": historical,
                  "manifest": "docs/markdown/governance/baseline/documentation-classification.json"},
              "checks": {k: "PASS" for k in ("bundled_open_exchange_xsd", "all_existing_semantic_and_visual_identities", "adopted_identity_bindings",
                          "source_based_relationship_homes", "exactly_one_home", "no_capability_recreation", "view_endpoints", "resource_and_view_trees",
                          "documentation_convention", "all_existing_structured_properties", "current_and_historical_standing",
                          "unresolved_records_and_assertions", "markdown_archimate_exact_correspondence", "pinned_recovery_source_boundary",
                          "capability_tier_classification", "business_feature_reachability", "business_enabling_feature_reachability",
                          "no_invented_ancestry", "resource_view_numbering_separation", "starting_candidate_semantic_documentation_view_stability",
                          "feature_centred_resource_navigation", "feature_centred_view_navigation", "immediate_child_landscapes_first",
                          "feature_centred_diagram_layout", "no_false_feature_associations")}}
    outputs = {metadata.LEGACY_MODEL: data, DEST / "validation.json": encoded(report), **md}
    if metadata.active():
        result = metadata.overlay(outputs, b, report)
        if reconciled:
            import reconcile_108
            result = reconcile_108.publish(*result, home_derived=home_derived)
            if home_derived and presentation:
                import view_presentation
                result = view_presentation.publish(*result)
                validate(result[1], g.ET.fromstring(result[0][metadata.model_path()]))
                if information:
                    import information_population
                    if information_population.active():
                        result = information_population.overlay(*result)
            return result
        return result
    return outputs, b, report


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true")
    parser.add_argument("--output-directory", type=Path, help="Write an independent reproduction under /tmp (including Markdown)")
    args = parser.parse_args()
    try:
        outputs, _, report = publish()
        again, _, _ = publish()
        g.require(outputs == again, "Non-deterministic baseline regeneration")
        report["checks"]["deterministic_regeneration"] = "PASS"
        outputs[metadata.validation_path()] = encoded(report)
        for path, content in outputs.items():
            if args.output_directory:
                dest = args.output_directory.resolve()
                g.require(dest.is_relative_to(Path("/tmp")), "Independent output must be under /tmp")
                path = dest / path.relative_to(ROOT)
            if args.check:
                g.require(path.read_bytes() == content, f"Markdown/ArchiMate baseline divergence: {path.relative_to(ROOT) if path.is_relative_to(ROOT) else path}")
            else:
                path.parent.mkdir(parents=True, exist_ok=True)
                path.write_bytes(content)
        print(json.dumps({"status": "PASS", "before": report["before"], "after": report["after"],
                          "deterministic_regeneration": "PASS", "sha256": report["output_sha256"]}, indent=2))
        return 0
    except (OSError, ValueError, KeyError, g.ET.LxmlError) as exc:
        print(f"INVALID: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
