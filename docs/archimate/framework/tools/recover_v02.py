#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Recover the pinned LaTeX RAD v0.2 snapshot, without allocating architecture IDs.

The model used by the existing LaTeX renderer supplies semantic records and
assertions. The actual included TeX corpus supplies frozen explanatory evidence.
Neither current RAD prose nor the old publication's names supply new semantics.
"""
import argparse
import collections
import copy
import hashlib
import json
from pathlib import Path
import re
import sys

import generate_views as g
import publish_navigation as nav
from architecture_release import load_release

from workspace_paths import historical_path

BASE = g.BASE
ROOT = BASE.parents[2]
DEST = g.workspace.RECOVERY_EVIDENCE
MANIFEST = DEST / "source-manifest.json"
OUTPUT = DEST / "Harmonia-Architecture-1.0.5A.xml"
RELEASE = DEST / "architecture-release.json"
TYPES = {"capability": "Capability", "enterprise_capability": "Capability",
         "feature": "Capability", "function": "BusinessFunction",
         "service": "BusinessService", "process": "BusinessProcess"}
CATEGORIES = {"business": "Business Capabilities", "business_enabling": "Business Enabling Capabilities",
              "enterprise": "Enterprise Capabilities"}
BEHAVIOURS = {"function": "Functions", "service": "Services", "process": "Processes"}
RELATIONS = {"capability_decomposition": "Composition", "realises_feature": "Realization"}
STATES = {"current": "Current", "superseded": "Superseded", "pending_adjudication": "Pending Adjudication"}
DEFERRED = {
    "associated_feature": "Feature association does not establish a particular native edge or whole-Feature realisation.",
    "enabled_by": "Material-enablement Association profile explicitly still requires review in the mapping authority.",
    "enterprise_enabled_by": "Enterprise enablement does not establish native Serving or Composition; qualified profile requires review.",
    "enterprise_contribution": "Contribution strengths retain source meaning; no approved native relationship profile.",
    "governed_process": "Process responsibility/governance is not automatically Assignment or Composition.",
    "participating_function": "Participation alone does not establish an ordered/whole-part process or realisation.",
    "uses_function": "Use does not establish transfer, triggering, exposed Service or native relationship direction.",
    "purpose_context": "Purpose context is preserved as context; it is not decomposition.",
    "responsibility_parent": "Broader responsibility and current framing remain distinct; ownership edge profile requires review.",
    "contributes_feature": "Partial contribution is not whole-Feature Realization; no approved qualified edge profile.",
    "uses_process": "Use alone does not establish invocation, sequencing or native Serving.",
    "exposes_process": "Exposure alone does not establish a process-to-Service realisation with the required direction.",
}


def sha(path):
    return hashlib.sha256(historical_path(path).read_bytes()).hexdigest()


def js(value):
    return json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(",", ":"))


def text_latex(value):
    """Decode the frozen transcriber's text macros; never interpret TeX as code.

    Definitions use a small known macro vocabulary. Unknown commands remain
    visible as source evidence instead of losing their argument/meaning.
    Exact original records and TeX fingerprints accompany the readable text.
    """
    value = re.sub(r"(?m)^%.*$", "", value or "")
    value = re.sub(r"\\(?:label|index)\{[^{}]*\}", "", value)
    value = re.sub(r"\\(?:begin|end)\{(?:itemize|enumerate)\}", "", value)
    value = re.sub(r"\\(?:allowbreak)\{\}", "", value)
    value = re.sub(r"\\(?:par|smallskip|phantomsection|footnotesize)\b", "\n", value)
    value = re.sub(r"\\item\b", "\n• ", value)
    value = re.sub(r"\\(?:textbf|texttt|textit|emph|seqsplit|subsubsection\*?)\{([^{}]*)\}", r"\1", value)
    # Nested texttt/seqsplit and nested emphasis are emitted by the transcriber.
    for _ in range(8):
        before = value
        value = re.sub(r"\\(?:textbf|texttt|textit|emph|seqsplit)\{([^{}]*)\}", r"\1", value)
        value = re.sub(r"\\href\{([^{}]*)\}\{([^{}]*)\}", r"\2 (\1)", value)
        if value == before:
            break
    value = re.sub(r"\\([&_#%${}])", r"\1", value)
    value = value.replace("---", "—").replace("~", " ")
    value = re.sub(r"[ \t]+\n", "\n", value)
    return re.sub(r"\n{3,}", "\n\n", value).strip()


def load_inputs(manifest_path=MANIFEST):
    manifest = json.loads(Path(manifest_path).read_text())
    for relative, expected in manifest["files_sha256"].items():
        path = historical_path(ROOT / relative)
        g.require(path.resolve().is_relative_to(ROOT.resolve()), "Snapshot path outside repository")
        g.require(sha(path) == expected, f"Pinned recovery source drift: {relative}")
    source = ROOT / manifest["source_directory"]
    model = json.loads((source / "model.json").read_text())
    g.require(model["version"] == "0.2-draft", "Unexpected snapshot version")
    records = {e["key"]: e for e in model["elements"]}
    g.require(len(records) == len(model["elements"]), "Duplicate snapshot capture key")
    lock = json.loads((source / "identity-lock.json").read_text())
    g.require(set(lock) == {e["reference_id"] for e in records.values()}, "Snapshot identity lock population mismatch")
    for record in records.values():
        g.require(record["type"] in TYPES, f"Unmapped construct: {record['key']}")
        entry = lock[record["reference_id"]]
        g.require(all(entry[k] == record[k] for k in ("key", "latex_label", "local_id", "canonical_id", "id_aliases")),
                  f"Snapshot identity lock mismatch: {record['key']}")
    currentness = {r["relationship_index"]: r for r in model["relationship_currentness"]}
    g.require(set(currentness) == set(range(len(model["relationships"]))), "Incomplete relationship currentness")
    original = g.parse(ROOT / manifest["baseline_exchange"])
    g.validate_lock(g.parse(BASE / "baseline/Harmonia-Capability-Architecture-v2.1a.xml"),
                    json.loads((BASE / "baseline/semantic-lock.json").read_text()))
    mapping = json.loads((BASE / "documentation-map.json").read_text())
    bindings = {m["reference_id"]: m["element_identifier"] for m in mapping["matches"]}
    registry = json.loads((ROOT / "docs/markdown/governance/architecture-identity/registry.json").read_text())
    # The live registry validator also reads today's RAD definitions. Keep that
    # separate verification outside snapshot generation: only pinned register,
    # schema and binding evidence may affect replay of this frozen recovery.
    import architecture_identity as identity
    schema = json.loads((ROOT / "docs/markdown/governance/architecture-identity/registry.schema.json").read_text())
    g.require(not identity.business.schema_errors(registry, schema), "Pinned identity registry schema is invalid")
    g.require(registry["semantic_namespace"] == str(identity.SEMANTIC_NAMESPACE), "Pinned identity namespace changed")
    g.require({e["canonical_architecture_id"]: e["archimate"]["semantic_id"] for e in registry["records"]} == bindings
              and all(e["origin"] == "retained" and e["archimate"]["mode"] == "locked" for e in registry["records"]),
              "Pinned EC identity bindings changed")
    return manifest, source, model, original, bindings


class Recovery:
    def __init__(self, model, original, bindings):
        self.model, self.original, self.bindings = model, original, bindings
        self.mid = original.get("identifier")
        self.records = {e["key"]: e for e in model["elements"]}
        self.ids = {k: bindings[e["reference_id"]] if e["reference_id"] in bindings else
                    self.ident("element", e["reference_id"]) for k, e in self.records.items()}
        g.require(len(set(self.ids.values())) == len(self.ids), "Representation identity collision")
        self.byid = {v: self.records[k] for k, v in self.ids.items()}
        self.currentness = {r["relationship_index"]: r["record_state"] for r in model["relationship_currentness"]}
        self.homes, self.view_scopes = {}, {}
        self.relation_map, self.findings = [], []

    def ident(self, role, *keys):
        # Publication role is a separate namespace key, never a persistent ID.
        return g.visual_id(self.mid, "latex-rad-recovery-v1", role, *keys)

    def ancestry(self, key, owner=False):
        record = self.records[key]
        key = record.get("owner_key") if owner else key
        result, seen = [], set()
        while key:
            g.require(key in self.records and key not in seen, "Missing/cyclic source ancestry")
            seen.add(key)
            item = self.records[key]
            result.insert(0, item)
            key = item.get("parent_key")
        return result

    def category(self, e):
        if e.get("category"):
            return CATEGORIES[e["category"]]
        chain = self.ancestry(e["key"], owner=True)
        return CATEGORIES[chain[0]["category"]] if chain and chain[0].get("category") else "Unresolved responsibility"

    def property(self, element, name, value):
        if value is None or value == "" or value == [] or value == {}:
            return
        identifier = "recovery-" + re.sub(r"[^a-z0-9]+", "-", name.lower()).strip("-")
        definitions = self.result.find("a:propertyDefinitions", g.N)
        if identifier not in self.property_ids:
            definition = g.child(definitions, "propertyDefinition", identifier=identifier, type="string")
            g.child(definition, "name", "v0.2 / " + name)
            self.property_ids.add(identifier)
        properties = element.find("a:properties", g.N)
        if properties is None:
            properties = g.ET.Element(g.tag("properties"))
            before = next((c for c in element if c.tag in (g.tag("node"), g.tag("connection"))), None)
            if before is None:
                element.append(properties)
            else:
                element.insert(list(element).index(before), properties)
        prop = g.child(properties, "property", propertyDefinitionRef=identifier)
        g.child(prop, "value", value if isinstance(value, str) else js(value))

    def document(self, element, body):
        old = element.find("a:documentation", g.N)
        if old is None:
            old = g.ET.Element(g.tag("documentation"))
            old.set(g.LANG, "en")
            element.insert(1 if element.find("a:name", g.N) is not None else 0, old)
            old.text = body
        else:
            old.text += "\n\n----- LaTeX RAD v0.2 recovery -----\n\n" + body

    def description(self, e):
        out = [f"LaTeX RAD v0.2-draft. Element: {e['architectural_status']}; record: {STATES[e['record_state']]}; "
               f"direct placement: {e['placement_status']}; structural identity/path: {e['structural_id_status']}.",
               "Recovery is a snapshot representation, not architectural acceptance. Persistent Architecture Identity "
               + ("retained through the existing EC binding." if e["reference_id"] in self.bindings else "unresolved; this XML ID is representation identity only.")]
        if e.get("governed_definition_latex"):
            out += ["Preserved source definition / semantic detail", text_latex(e["governed_definition_latex"])]
        for heading, field in (("Definition — Proposed / reviewed candidate", "definition"),
                               ("Overview — snapshot explanation", "overview"), ("Intent", "intent"),
                               ("Boundary", "boundary"), ("Proposed overview", "proposal_overview"),
                               ("Proposed definition", "proposal_definition"), ("Proposed boundary", "proposal_boundary"),
                               ("HUMAN ADJUDICATION REQUIRED", "adjudication_note")):
            if e.get(field):
                out += [heading, e[field]]
        for heading, value in e.get("baseline_description", {}).items():
            out += [heading + " — preserved v0.1 explanation", value]
        if not (e.get("governed_definition_latex") or e.get("definition")):
            out += ["Definition unresolved in this structured snapshot; cited source context is retained below. "
                    "No definition has been synthesised from the name."]
        if e["type"] == "feature":
            out += ["FT is a Feature classification, not CT4. Its owning CT3 is a source framing reference; "
                    "navigation containment creates no native Composition/Specialization edge."]
        if e.get("sources"):
            out += ["Source excerpts (snapshot standing retained)", "\n".join(
                f"{s['source']}:{s['line']} #{s.get('anchor') or ''}\n{s['text']}" for s in e["sources"])]
        if e.get("provenance"):
            out += ["Derivation / provenance (does not confer acceptance)", json.dumps(e["provenance"], ensure_ascii=False, indent=2)]
        if e.get("unresolved"):
            out += ["Unresolved source dimensions", "; ".join(e["unresolved"])]
        return "\n\n".join(out)

    def element_home(self, e):
        category = self.category(e)
        state = [] if e["record_state"] == "current" else [STATES[e["record_state"]]]
        if e["type"] in ("capability", "enterprise_capability"):
            chain = self.ancestry(e["key"]) if not state else [e]
            return ["Strategy", category, *state, *(x["name"] for x in chain)]
        chain = self.ancestry(e["key"], owner=True)
        if e["type"] == "feature":
            return ["Strategy", category, *state, *(x["name"] for x in chain), "Features (framing only)", e["name"]]
        return ["Business", category, *state, *(x["name"] for x in chain), BEHAVIOURS[e["type"]]]

    def add_elements(self):
        elements = self.result.find("a:elements", g.N)
        originals = {e.get("identifier"): e for e in elements}
        for key, e in sorted(self.records.items(), key=lambda p: p[1]["reference_id"]):
            identifier = self.ids[key]
            if identifier in originals:
                element = originals[identifier]
                refs = element.xpath("a:properties/a:property[@propertyDefinitionRef='prop-reference-id']/a:value/text()", namespaces=g.N)
                g.require(refs == [e["reference_id"]], "Retained EC Reference-ID binding changed")
            else:
                element = g.child(elements, "element", identifier=identifier)
                element.set(g.TYPE, TYPES[e["type"]])
                g.child(element, "name", e["name"])
            self.document(element, self.description(e))
            fields = {"Source Capture Key": key, "Snapshot Reference ID": e["reference_id"],
                      "LaTeX Label": e["latex_label"], "Construct Type": e["type"], "Capability Tier": e.get("tier"),
                      "Architectural Status": e["architectural_status"], "Record State": e["record_state"],
                      "Placement Status": e["placement_status"], "Structural ID Status": e["structural_id_status"],
                      "Structural Canonical ID": e.get("canonical_id"), "Historical ID Aliases": e.get("id_aliases"),
                      "Name Status": e.get("name_status"), "Canonical Name": e.get("canonical_name"),
                      "Name Aliases": e.get("name_aliases"), "Reviewed Name": e.get("reviewed_name"),
                      "Architecture Identity Status": "Retained existing EC binding" if e["reference_id"] in self.bindings else "Unresolved — representation ID only",
                      "Canonical Architecture ID": e["reference_id"] if e["reference_id"] in self.bindings else None,
                      "Source Locators": e.get("sources"), "Derivation Provenance": e.get("provenance"),
                      "Snapshot Record JSON": e, "Unresolved Dimensions": e.get("unresolved"),
                      "Source Parent Reference": e.get("parent_identifier"), "Source Owner Reference": e.get("owner_identifier"),
                      "Release Scope": e.get("release_scope"), "Adjudication Note": e.get("adjudication_note")}
            if e["record_state"] == "current":
                inherited = next((a for a in reversed(self.ancestry(key, owner=e["type"] not in ("capability", "enterprise_capability")))
                                  if a.get("release_scope")), None)
                if inherited and not e.get("release_scope"):
                    fields["Inherited Release Scope"] = inherited["release_scope"]
                    fields["Release Scope Source"] = inherited["reference_id"]
            for field, value in fields.items():
                self.property(element, field, value)
            self.homes[identifier] = self.element_home(e)

    def relationship_key(self, relation):
        # Source capture endpoint identities plus the asserted predicate. Mutable
        # text/status/index never enters identity. Distinct parallel assertions
        # require an explicit discriminator; collision is a blocking finding.
        return (relation["type"], self.records[relation["from"]]["reference_id"], self.records[relation["to"]]["reference_id"])

    def add_relationships(self):
        container = self.result.find("a:relationships", g.N)
        seen = set()
        for index, r in enumerate(self.model["relationships"]):
            g.require(r["from"] in self.ids and r["to"] in self.ids, "Missing relationship endpoint")
            entry = dict(source_index=index, snapshot_pointer=f"/relationships/{index}", source_assertion=r,
                         record_state=self.currentness[index])
            reason = DEFERRED.get(r["type"])
            if r["status"] == "unresolved":
                reason = "Unresolved source assertion; native mapping cannot promote it to a graph fact. " + (reason or "")
            if r["type"] not in RELATIONS or reason:
                entry.update(disposition="metadata_and_finding", reason=reason or "No established mapping")
                self.findings.append(entry)
                continue
            typ = RELATIONS[r["type"]]
            a, b = self.records[r["from"]], self.records[r["to"]]
            if typ == "Composition":
                g.require(a["type"] == b["type"] and a["type"] in ("capability", "enterprise_capability")
                          and a["category"] == b["category"], "Composition must be explicit same-category capability decomposition")
            else:
                g.require(a["type"] == "function" and b["type"] == "feature", "Unsupported Realization endpoints")
            identity = self.relationship_key(r)
            g.require(identity not in seen, "Parallel source assertions require reviewed identity discriminator")
            seen.add(identity)
            identifier = self.ident("relationship", *identity)
            relation = g.child(container, "relationship", identifier=identifier, source=self.ids[r["from"]], target=self.ids[r["to"]])
            relation.set(g.TYPE, typ)
            self.document(relation, f"Snapshot predicate: {r['type']}; assertion status: {r['status']}; "
                          f"currentness: {self.currentness[index]}.\n\n" + json.dumps(r, ensure_ascii=False, indent=2))
            for field, value in {"Assertion Status": r["status"], "Record State": self.currentness[index],
                                 "Source Predicate": r["type"], "Source Assertion JSON": r,
                                 "Snapshot Pointer": f"/relationships/{index}", "Architecture Identity Status": "Unresolved — representation ID only"}.items():
                self.property(relation, field, value)
            source_home = self.homes[self.ids[r["from"]]]
            self.homes[identifier] = ["Relations", "Capabilities" if source_home[0] == "Strategy" else source_home[0], *source_home[1:]]
            entry.update(disposition="native_relationship", archimate_type=typ, identifier=identifier)
            self.relation_map.append(entry)
        # Preserve every deferred predicate at both endpoints for Archi inspection.
        for key, identifier in self.ids.items():
            related = [f for f in self.findings if key in (f["source_assertion"]["from"], f["source_assertion"]["to"])]
            element = self.result.find(f"a:elements/a:element[@identifier='{identifier}']", g.N)
            self.property(element, "Deferred Relationship Assertions", related)

    def folder(self, path):
        parent = self.org
        for depth, label in enumerate(path, 1):
            prefix = tuple(path[:depth])
            if prefix not in self.folders:
                # Stable subject keys replace mutable capability labels.
                bound = [(i, self.path_keys[tuple(path[:i])]) for i in range(1, depth+1)
                         if tuple(path[:i]) in self.path_keys]
                if bound:
                    index, subject = bound[-1]
                    role = path[0] + ("/" + path[1] if path[0] in ("Views", "Relations") else "")
                    keys = [role, subject, *path[index:depth]]
                else:
                    keys = path[:depth]
                self.folders[prefix] = g.folder(parent, self.ident("folder", *keys), label)
            parent = self.folders[prefix]
        return parent

    def reference(self, identifier, path):
        g.child(self.folder(path), "item", identifier=self.ident("reference", identifier), identifierRef=identifier)

    def view(self, role, name, keys, path, connections=(), documentation=None, context=None, tier_positions=None):
        identifier = self.ident("view", role)
        view = g.child(self.diagrams, "view", identifier=identifier)
        view.set(g.TYPE, "Diagram")
        g.child(view, "name", name)
        g.child(view, "documentation", documentation or
                "LaTeX RAD v0.2 snapshot. Context and immediate source decomposition only. "
                "Inspect Documentation and v0.2 Properties for element, placement and relationship standing. "
                "Orange = Proposed; purple = Candidate; grey = historical/pending; yellow = Canonical. "
                "Folder placement and Feature framing do not create semantic edges.")
        keys = list(dict.fromkeys(keys))
        nodes, positions = {}, {}
        start = 0
        if context is not None:
            positions[context] = (g.MARGIN, g.MARGIN)
            start = 1
        for i, key in enumerate(k for k in keys if k != context):
            positions[key] = (g.MARGIN + (i % 4) * (g.WIDTH + g.COLUMN_GAP),
                              g.MARGIN + start * 180 + (i // 4) * 180)
        if tier_positions is not None:
            positions = tier_positions
        for key in keys:
            semantic = self.ids.get(key, key)
            record = self.records.get(key, {})
            x, y = positions[key]
            node = g.child(view, "node", identifier=self.ident("node", identifier, semantic), elementRef=semantic,
                           x=str(x), y=str(y), w=str(g.WIDTH), h="132")
            node.set(g.TYPE, "Element")
            nodes[semantic] = node.get("identifier")
            color = (225, 225, 225) if record.get("record_state", "current") != "current" else {
                "Proposed": (255, 222, 150), "Candidate": (224, 200, 245)}.get(record.get("architectural_status"), (255, 255, 181))
            g.style(node, color)
        if not keys:
            note = g.child(view, "node", identifier=self.ident("note", identifier), x="40", y="40", w="1100", h="160")
            note.set(g.TYPE, "Label")
            g.child(note, "label", name + "\n\nSelect this View in the model tree and open its Documentation. "
                    "The frozen source or recovery findings are stored there. This note is presentation only; "
                    "it creates no architectural element or relationship.")
            g.style(note, (235, 242, 248))
        relation_index = {r.get("identifier"): r for r in self.result.findall("a:relationships/a:relationship", g.N)}
        for rid in connections:
            r = relation_index[rid]
            a, b = r.get("source"), r.get("target")
            g.require(a in nodes and b in nodes, "View relationship endpoint outside scope")
            connection = g.child(view, "connection", identifier=self.ident("connection", identifier, rid, nodes[a], nodes[b]),
                                 relationshipRef=rid, source=nodes[a], target=nodes[b])
            connection.set(g.TYPE, "Relationship")
            g.style(connection)
            if tier_positions is not None:
                akey = next(k for k in keys if self.ids.get(k, k) == a)
                bkey = next(k for k in keys if self.ids.get(k, k) == b)
                sx, sy = positions[akey]
                _, ty = positions[bkey]
                trunk = sx + g.WIDTH + g.COLUMN_GAP // 2
                g.child(connection, "bendpoint", x=str(trunk), y=str(sy + 66))
                g.child(connection, "bendpoint", x=str(trunk), y=str(ty + 66))
            elif context:
                target_key = next(k for k in keys if self.ids.get(k, k) == b)
                tx, ty = positions[target_key]
                for x, y in ((g.MARGIN + g.WIDTH // 2, g.MARGIN + 160),
                             (tx - 20, g.MARGIN + 160), (tx - 20, ty + 66)):
                    g.child(connection, "bendpoint", x=str(x), y=str(y))
        self.homes[identifier] = path
        self.view_scopes[identifier] = dict(role=role, keys=keys, relationships=list(connections), path=path)
        return view

    def tier_layout(self, category, keys):
        """Reuse the proven three-tier layout engine with an explicit projection.

        This temporary layout index includes only existing recovered objects and
        current source Composition edges. It establishes no new semantics.
        """
        temporary = g.ET.Element(g.tag("model"), identifier=self.mid)
        elements = g.child(temporary, "elements")
        relations = g.child(temporary, "relationships")
        layer = g.LAYERS[list(CATEGORIES).index(category)]
        for key in keys:
            e = g.child(elements, "element", identifier=key)
            e.set(g.TYPE, "Capability")
            g.child(e, "name", self.records[key]["name"])
            properties = g.child(e, "properties")
            prop = g.child(properties, "property", propertyDefinitionRef="prop-layer")
            g.child(prop, "value", layer)
        edges = [r for r in self.relation_map if r["archimate_type"] == "Composition" and r["record_state"] == "current"
                 and r["source_assertion"]["from"] in keys and r["source_assertion"]["to"] in keys]
        for r in edges:
            e = g.child(relations, "relationship", identifier=r["identifier"],
                        source=r["source_assertion"]["from"], target=r["source_assertion"]["to"])
            e.set(g.TYPE, "Composition")
        model = g.Model(temporary)
        model.height = lambda key: 132
        return {k: (x, y) for k, (x, y, _, _) in g.layout(model, layer).items()}, [e["identifier"] for e in edges]

    def views_and_navigation(self):
        self.org = self.result.find("a:organizations", g.N)
        self.org.clear()
        self.folders, self.path_keys = {}, {}
        # Preserve proven reference Views and old semantic objects in a separate
        # unreconciled branch. Their identities/content/relative paths survive.
        old_homes = nav.navigation_homes(self.original)
        bound = set(self.bindings.values())
        old_elements = self.original.findall("a:elements/a:element", g.N)
        old_relations = self.original.findall("a:relationships/a:relationship", g.N)
        old_views = self.original.findall("a:views/a:diagrams/a:view", g.N)
        for element in [*old_elements, *old_relations, *old_views]:
            identifier = element.get("identifier")
            if identifier in bound:
                continue
            path = list(old_homes[identifier][0])
            self.homes[identifier] = [path[0], "Unreconciled 1.0.4 Reference", *path[1:]]
        # Use capture reference identity for folder keys, never name-derived IDs.
        for key, e in self.records.items():
            path = self.homes[self.ids[key]]
            if e["type"] in ("capability", "enterprise_capability", "feature"):
                self.path_keys[tuple(path)] = e["reference_id"]
                self.path_keys[tuple(["Views", "Capabilities", *path[1:]])] = e["reference_id"]
                self.path_keys[tuple(["Business", *path[1:]])] = e["reference_id"]
                self.path_keys[tuple(["Views", "Business", *path[1:]])] = e["reference_id"]
                self.path_keys[tuple(["Relations", "Capabilities", *path[1:]])] = e["reference_id"]
        old_model = g.Model(self.original)
        for key in old_model.elements:
            if key in bound:
                continue
            path = self.homes[key]
            self.path_keys[tuple(path)] = key
            self.path_keys[tuple(["Views", path[1], "Capabilities", *path[2:]])] = key
            self.path_keys[tuple(["Relations", path[1], "Capabilities", *path[2:]])] = key
        self.diagrams = self.result.find("a:views/a:diagrams", g.N)
        current = [e for e in self.records.values() if e["record_state"] == "current"]
        for category, label in CATEGORIES.items():
            overview = [e["key"] for e in current if e["category"] == category and e["tier"] in ("CT1", "CT2", "CT3")]
            # Category overviews retain the three-tier scope; detail Views are the
            # readable route. Features never enter the tier overview.
            name = {"business": "Business Capability Model", "business_enabling": "Business Enabling Capability Model",
                    "enterprise": "Enterprise Capability Model"}[category]
            positions, edges = self.tier_layout(category, overview)
            self.view("overview:" + category, name + " — v0.2", overview,
                      ["Views", "Capabilities", label], edges, tier_positions=positions,
                      documentation="CT1 + CT2 + CT3 source hierarchy, left to right, with current explicit Composition "
                      "relationships only. Features are excluded. Element status and Proposed placement are independent; "
                      "inspect v0.2 Properties. Use the context Views for readable progressive inspection. "
                      "The Enterprise hierarchy is a v0.2 Proposed assertion; it remains independent of BC/BEC.")
        roots = [e["key"] for e in current if e["type"] in ("capability", "enterprise_capability") and not e["parent_key"]]
        self.view("landscape", "Harmonia Capability Landscape — v0.2", roots, ["Views", "Capabilities"])
        for key, e in sorted(self.records.items()):
            if e["type"] not in ("capability", "enterprise_capability"):
                continue
            children = [c["key"] for c in current if c["parent_key"] == key] if e["record_state"] == "current" else []
            children.sort(key=lambda k: (self.records[k]["name"].casefold(), k))
            relations = [r["identifier"] for r in self.relation_map if r["archimate_type"] == "Composition"
                         and r["record_state"] == "current" and r["source_assertion"]["from"] == key
                         and r["source_assertion"]["to"] in children]
            self.view("context:" + e["reference_id"], e["name"] + " Overview — v0.2", [key, *children],
                      ["Views", "Capabilities", *self.homes[self.ids[key]][1:]], relations, context=key)
            features = [f["key"] for f in current if f["type"] == "feature" and f["owner_key"] == key]
            if features:
                self.view("features:" + e["reference_id"], e["name"] + " — Feature framing", [key, *features],
                          ["Views", "Capabilities", *self.homes[self.ids[key]][1:], "Features (framing only)"],
                          documentation="Explicit source owner/framing context. FT is not CT4. No native framing relationship "
                          "has been adjudicated; proximity creates no Composition or Specialization.", context=key)
            behaviours = [b["key"] for b in current if b["type"] in BEHAVIOURS and b["owner_key"] == key]
            if behaviours:
                items = [key, *features, *behaviours]
                # Bound large contexts into pages while retaining semantic IDs.
                for start in range(0, len(items)-1, 20):
                    page = [key, *items[1:][start:start+20]]
                    edges = [r["identifier"] for r in self.relation_map if r["archimate_type"] == "Realization"
                             and r["source_assertion"]["from"] in page and r["source_assertion"]["to"] in page]
                    self.view(f"behaviour:{e['reference_id']}:{start//20+1}", e["name"] + f" — Behaviour {start//20+1}",
                              page, ["Views", "Business", *self.homes[self.ids[key]][1:]], edges, context=key)
        # A realisation View for every explicit Function/Feature pair prevents
        # paging from hiding edges. Endpoint type and standing are not inferred.
        for r in self.relation_map:
            if r["archimate_type"] == "Realization":
                a = r["source_assertion"]
                self.view("assertion:" + r["identifier"], self.records[a["from"]]["name"] + " — Feature realisation",
                          [a["from"], a["to"]], ["Views", "Traceability", "Feature Realisations"], [r["identifier"]])

    def evidence(self, source):
        # Follow the editable master's actual include graph, not every similarly
        # named file in a working tree. Reject escaping paths and include cycles.
        visited = set()
        def visit(path, active):
            path = path.resolve()
            g.require(path.is_relative_to(source.resolve()) and path not in active, "Unsafe/cyclic TeX include")
            if path in visited:
                return
            visited.add(path)
            raw = path.read_text()
            for relative in re.findall(r"\\input\{([^}]+)\}", raw):
                target = source / relative
                if not target.suffix:
                    target = target.with_suffix(".tex")
                visit(target, {*active, path})
        visit(source / "Harmonia-EA-Capability-Map.tex", set())
        self.evidence_files = []
        for path in sorted(visited):
            if path.parent.name != "sections":
                continue
            relative = str(path.relative_to(source))
            content = path.read_text()
            # Source evidence is a View's Documentation, not a fabricated native
            # Gap, Requirement, application component or information object.
            view = self.view("evidence:" + relative, "Source evidence — " + path.stem, [],
                             ["Views", "Recovery Evidence", "LaTeX Source Corpus"],
                             documentation="Frozen v0.2 source evidence. This is documentation, not additional semantic allocation. "
                             "TeX is retained verbatim so unrecognised formatting cannot discard meaning.\n\n" + content)
            self.property(view, "Source Path", str(path.relative_to(ROOT)))
            self.property(view, "Source SHA256", sha(path))
            entry = dict(path=str(path.relative_to(ROOT)), sha256=sha(path), view_id=view.get("identifier"),
                         disposition="semantic_records_and_source_documentation" if path.stem in
                         ("business-capabilities", "business-enabling-capabilities", "enterprise-capabilities", "relationships", "identifiers")
                         else "source_documentation_with_recovery_findings")
            self.evidence_files.append(entry)
        # The selected axiom appendix has five explicit native definitions. Its
        # sparse source ranges must not be mistaken for the complete register.
        raw = (source / "sections/rad-axioms.tex").read_text()
        headings = list(re.finditer(r"\\subsubsection\*\{(AX-\d{2}) (?:---|—) ([^}]+)\}", raw))
        self.principles = []
        for i, match in enumerate(headings):
            identifier = self.ident("principle", match[1])
            element = g.child(self.result.find("a:elements", g.N), "element", identifier=identifier)
            element.set(g.TYPE, "Principle")
            g.child(element, "name", match[1] + " " + match[2])
            end = headings[i+1].start() if i+1 < len(headings) else len(raw)
            self.document(element, "Canonical axiom — selected v0.2 appendix transcription. "
                          "This is the snapshot selection, not a claim that the whole axiom register is included. "
                          "Persistent Architecture Identity adoption is unresolved.\n\n" + text_latex(raw[match.end():end]))
            for field, value in {"Native Definition ID": match[1], "Architectural Status": "Canonical",
                                 "Architecture Identity Status": "Unresolved — representation ID only",
                                 "Source Path": str((source / "sections/rad-axioms.tex").relative_to(ROOT)),
                                 "Source SHA256": sha(source / "sections/rad-axioms.tex")}.items():
                self.property(element, field, value)
            self.homes[identifier] = ["Motivation", "Architectural Axioms (snapshot selection)"]
            self.principles.append(identifier)
        self.view("principles", "Architectural Axioms — v0.2 source selection", self.principles,
                  ["Views", "Motivation"])
        self.view("findings", "Recovery Findings — mappings and identity", [], ["Views", "Recovery Evidence"],
                  documentation="No pending mapping is represented by a speculative native edge. "
                  "The complete machine-readable ledger accompanies the model in recovery-findings.json.\n\n" +
                  "\n".join(f"{kind}: {count} — {DEFERRED.get(kind, 'Unresolved source assertion')}" for kind, count in
                            sorted(collections.Counter(f["source_assertion"]["type"] for f in self.findings).items())) +
                  "\n\nFeature framing/ownership is metadata and navigation only. 214 old BC/BEC semantic objects "
                  "lack a proven v0.2 crosswalk; they survive under Unreconciled 1.0.4 Reference. "
                  "Application, information, technology and Motivation references without bounded typed definitions "
                  "remain source documentation. No post-v0.2 corpus-register-only material is imported.")
        for identifier, path in sorted(self.homes.items()):
            self.reference(identifier, path)

    def build(self, source):
        self.result = copy.deepcopy(self.original)
        self.property_ids = {p.get("identifier") for p in self.result.findall("a:propertyDefinitions/a:propertyDefinition", g.N)}
        self.result.find("a:name", g.N).text = "Harmonia Architecture — LaTeX RAD v0.2 Recovery (1.0.5A)"
        self.result.find("a:documentation", g.N).text = (
            "Visual recovery of the pinned LaTeX RAD v0.2-draft snapshot, including its Proposed, Candidate, "
            "Superseded and Pending Adjudication material. This is not the final reconciled architecture baseline. "
            "The 14 exact Enterprise bindings survive. The other 214 old publication elements and all 219 old Views "
            "are preserved in Unreconciled 1.0.4 Reference branches; name equality establishes no crosswalk. "
            "Source structural IDs, capture IDs and deterministic XML representation IDs are not persistent "
            "Architecture Identity allocations. Use the v0.2 context Views and Recovery Evidence to inspect standing and limitations.")
        self.add_elements()
        self.add_relationships()
        self.views_and_navigation()
        self.evidence(source)
        return self.result


def validate(recovery, root, schema):
    schema.assertValid(root)
    g.validate_identifiers(root)
    elements = {e.get("identifier"): e for e in root.findall("a:elements/a:element", g.N)}
    relations = {r.get("identifier"): r for r in root.findall("a:relationships/a:relationship", g.N)}
    views = {v.get("identifier"): v for v in root.findall("a:views/a:diagrams/a:view", g.N)}
    g.require(set(elements) == set(recovery.ids.values()) | set(recovery.principles) |
              {e.get("identifier") for e in recovery.original.findall("a:elements/a:element", g.N)}, "Unexpected/missing semantic element")
    g.require(set(relations) == {r["identifier"] for r in recovery.relation_map} |
              {r.get("identifier") for r in recovery.original.findall("a:relationships/a:relationship", g.N)}, "Unexpected/missing relationship")
    g.require(set(views) == set(recovery.view_scopes) |
              {v.get("identifier") for v in recovery.original.findall("a:views/a:diagrams/a:view", g.N)}, "Unexpected/missing View")
    homes = nav.navigation_homes(root)
    g.require(set(homes) == set(elements) | set(relations) | set(views) and all(len(p) == 1 for p in homes.values()),
              "Every semantic object and View needs exactly one navigation home")
    for identifier, expected in recovery.homes.items():
        g.require(homes[identifier] == [tuple(expected)], f"Incorrect source-based navigation: {identifier}")
    definitions = {p.get("identifier") for p in root.findall("a:propertyDefinitions/a:propertyDefinition", g.N)}
    for prop in root.findall(".//a:property", g.N):
        g.require(prop.get("propertyDefinitionRef") in definitions, "Dangling property definition")
    # Source standing, definitions and complete metadata are an assertion-level
    # contract, not merely a presence/count check. Detect silent promotion,
    # dropped provenance or rewritten definitions even when the XML is valid.
    for expected in recovery.result.findall("a:elements/a:element", g.N):
        identifier = expected.get("identifier")
        g.require(g.signature(elements[identifier]) == g.signature(expected),
                  f"Source semantics, standing or Documentation changed: {identifier}")
    for expected in recovery.result.findall("a:relationships/a:relationship", g.N):
        g.require(g.signature(relations[expected.get("identifier")]) == g.signature(expected),
                  "Source relationship predicate, standing or currentness changed")
    for old in recovery.original.findall("a:elements/a:element", g.N):
        identifier = old.get("identifier")
        actual = copy.deepcopy(elements[identifier])
        properties = actual.find("a:properties", g.N)
        for prop in list(properties):
            if prop.get("propertyDefinitionRef", "").startswith("recovery-"):
                properties.remove(prop)
        if identifier in recovery.bindings.values():
            doc = actual.find("a:documentation", g.N)
            before = old.find("a:documentation", g.N)
            g.require(doc.text.startswith(before.text + "\n\n----- LaTeX RAD v0.2 recovery -----"), "Existing EC Documentation lost")
            doc.text = before.text
        g.require(g.signature(actual) == g.signature(old), f"Retained semantic content/binding changed: {identifier}")
    for old in recovery.original.findall("a:relationships/a:relationship", g.N):
        g.require(g.signature(relations[old.get("identifier")]) == g.signature(old), "Retained relationship changed")
    for old in recovery.original.findall("a:views/a:diagrams/a:view", g.N):
        g.require(g.signature(views[old.get("identifier")]) == g.signature(old), "Retained 1.0.4 View/visual identity changed")
    for rid, relation in relations.items():
        g.require(relation.get("source") in elements and relation.get("target") in elements, "Dangling semantic endpoint")
    for identifier, view in views.items():
        all_nodes = view.findall("a:node", g.N)
        nodes = {n.get("identifier"): n for n in all_nodes if n.get(g.TYPE) == "Element"}
        for node in all_nodes:
            g.require(node.get(g.TYPE) == "Element" or (node.get(g.TYPE) == "Label" and identifier in recovery.view_scopes
                      and not recovery.view_scopes[identifier]["keys"]), "Unexpected visual node type")
        for node in nodes.values():
            g.require(node.get("elementRef") in elements, "Dangling visual semantic endpoint")
        for connection in view.findall("a:connection", g.N):
            r = relations[connection.get("relationshipRef")]
            g.require(connection.get("source") in nodes and connection.get("target") in nodes, "View-local endpoint missing")
            g.require(nodes[connection.get("source")].get("elementRef") == r.get("source") and
                      nodes[connection.get("target")].get("elementRef") == r.get("target"), "Visual/semantic endpoints disagree")
        if identifier in recovery.view_scopes:
            spec = recovery.view_scopes[identifier]
            g.require({n.get("elementRef") for n in nodes.values()} == {recovery.ids.get(k, k) for k in spec["keys"]},
                      "Recovery View scope changed")
            g.require({c.get("relationshipRef") for c in view.findall("a:connection", g.N)} == set(spec["relationships"]),
                      "Recovery View relationships changed")
            rectangles = []
            for node in nodes.values():
                x, y, w, h = (int(node.get(a)) for a in ("x", "y", "w", "h"))
                g.require(w >= 300 and h >= 132 and node.find("a:style/a:font", g.N).get("size") == "12", "Unreadable recovery node")
                for bx, by, bw, bh in rectangles:
                    g.require(x+w <= bx or bx+bw <= x or y+h <= by or by+bh <= y, "Overlapping recovery nodes")
                rectangles.append((x, y, w, h))
    # Current explicit placements must have the matching current native edge;
    # historical assertions cannot silently become current navigation ancestry.
    for e in recovery.records.values():
        if e["record_state"] == "current" and e["type"] in ("capability", "enterprise_capability") and e["parent_key"]:
            matches = [r for r in recovery.relation_map if r["source_assertion"]["type"] == "capability_decomposition"
                       and r["source_assertion"]["from"] == e["parent_key"] and r["source_assertion"]["to"] == e["key"]
                       and r["record_state"] == "current"]
            g.require(len(matches) == 1, "Current placement lacks exact explicit source relationship")
    return {"elements": len(elements), "relationships": len(relations), "views": len(views),
            "element_types": dict(collections.Counter(e.get(g.TYPE) for e in elements.values())),
            "relationship_types": dict(collections.Counter(r.get(g.TYPE) for r in relations.values()))}


def publish(manifest_path=MANIFEST):
    manifest, source, model, original, bindings = load_inputs(manifest_path)
    recovery = Recovery(model, original, bindings)
    root = recovery.build(source)
    data = g.serialize(root)
    counts = validate(recovery, g.ET.fromstring(data), g.load_schema(BASE / "schemas/archimate3_Diagram.xsd"))
    # Recovery findings include exact source records, statuses and reverse
    # provenance. No unrepresented relationship disappears into a summary count.
    findings = {"task": "1.0.5A", "snapshot": manifest["snapshot"], "deferred_relationships": recovery.findings,
                "relationship_constraints": model["relationship_constraints"],
                "feature_framing": [{"key": e["key"], "owner_key": e["owner_key"],
                                     "status": e["placement_status"], "record_state": e["record_state"],
                                     "reason": "Explicit framing retained in navigation/Properties; native edge requires adjudication."}
                                    for e in model["elements"] if e["type"] == "feature"],
                "definition_gaps": [e["key"] for e in model["elements"] if not(e.get("governed_definition_latex") or e.get("definition"))],
                "source_unresolved_dimensions": [{"key": e["key"], "dimensions": e["unresolved"]}
                                                 for e in model["elements"] if e["unresolved"]],
                "unreconciled_legacy_elements": [e.get("identifier") for e in original.findall("a:elements/a:element", g.N)
                                                if e.get("identifier") not in bindings.values()],
                "source_corpus_accounting": recovery.evidence_files,
                "additional_material_categories": [
                    {"category": "Capability enablement, contribution, participation and responsibility predicates", "reason": "Native qualified profiles remain unapproved; exact assertions are in deferred_relationships and endpoint Properties."},
                    {"category": "Information requirements/consumption/production and Domain04 review content", "reason": "Appendix narrative/matrices do not establish complete typed, identity-resolved information records. Preserved verbatim in evidence Views; no object-per-row or inferred Access."},
                    {"category": "Actors, Roles, service dependencies and contextual participants", "reason": "Qualified references are not independent bounded definitions or a participant matrix. Retained in source evidence and behaviour Documentation; no inferred Assignment/Serving."},
                    {"category": "Metamodel rules, identifiers, review decisions, reconciliation, constraints and source registers", "reason": "Publication/governance evidence is Documentation/Properties, not automatically a native Gap, Assessment, Requirement or semantic hierarchy."},
                    {"category": "Application/software, technology and additional Motivation/Strategy corpus-register entries", "reason": "The corpus-register is derivation evidence, not the set of transcribed v0.2 architectural definitions. No register-only/current/post-snapshot semantic import; references retained in source evidence."},
                    {"category": "Ordered Function chain", "reason": "One explicit Proposed three-Function chain retained verbatim in the Process Snapshot Record JSON; process composition/causal relationship publication needs finer assertion mapping. No sequence inferred from adjacency."},
                ]}
    crosswalk = {"snapshot": manifest["snapshot"], "elements": [
        {"capture_key": e["key"], "reference_id": e["reference_id"], "identifier": recovery.ids[e["key"]],
         "mapping": TYPES[e["type"]], "source_status": e["architectural_status"], "record_state": e["record_state"],
         "binding": "existing_exact_ec_reference" if e["reference_id"] in bindings else "deterministic_representation_only",
         "navigation_path": recovery.homes[recovery.ids[e["key"]]]} for e in model["elements"]],
        "relationships": recovery.relation_map, "principles": recovery.principles, "views": recovery.view_scopes}
    care = next(e for e in model["elements"] if e["reference_id"] == "HCM-BC-care-access-coordination")
    children = [e for e in model["elements"] if e["parent_key"] == care["key"]]
    g.require(collections.Counter(e["architectural_status"] for e in children) == {"Canonical": 4, "Proposed": 1}, "Care Access CT2 regression")
    regression = {"context": recovery.ids[care["key"]], "children": [
        {"name": e["name"], "status": e["architectural_status"], "identifier": recovery.ids[e["key"]],
         "children": [{"name": c["name"], "status": c["architectural_status"], "placement_status": c["placement_status"]}
                      for c in model["elements"] if c["parent_key"] == e["key"]]} for e in children]}
    # Historical replay must not silently adopt a later live release. The
    # current baseline publisher reads the repository's live release instead.
    report = {"status": "PASS", "task": "1.0.5A", "architecture_release": load_release(RELEASE), "counts": counts,
              "snapshot_records": len(model["elements"]), "retained_elements": 228, "retained_relationships": 189,
              "retained_views": 219, "retained_bindings": 14, "persistent_identities_allocated": 0,
              "new_representation_elements": len(model["elements"])-14+len(recovery.principles),
              "recovered_record_types": dict(collections.Counter(e["type"] for e in model["elements"])),
              "record_statuses": dict(collections.Counter(e["architectural_status"] for e in model["elements"])),
              "record_states": dict(collections.Counter(e["record_state"] for e in model["elements"])),
              "native_relationship_statuses": dict(collections.Counter(r["source_assertion"]["status"] for r in recovery.relation_map)),
              "native_relationship_currentness": dict(collections.Counter(r["record_state"] for r in recovery.relation_map)),
              "deferred_relationships": len(recovery.findings), "source_relationships": len(model["relationships"]),
              "documentation": {"snapshot_record_documentation": len(model["elements"]), "principle_documentation": len(recovery.principles),
                                "definitions_available": len(model["elements"])-len(findings["definition_gaps"]),
                                "definition_gaps": len(findings["definition_gaps"]), "complete_snapshot_record_properties": len(model["elements"]),
                                "source_evidence_views": len(recovery.evidence_files)},
              "care_access_regression": regression, "output_sha256": hashlib.sha256(data).hexdigest(),
              "source_manifest_sha256": sha(manifest_path),
              "checks": {k: "PASS" for k in ("archi_5_9_xsd", "unique_ids_and_references", "existing_semantics_and_bindings_preserved",
                         "existing_219_views_and_visual_ids_preserved", "source_based_navigation_and_exactly_one_home",
                         "snapshot_identity_locks_preserved", "no_persistent_identity_allocation", "explicit_relationship_accounting",
                         "documentation_and_metadata", "source_status_and_currentness_preserved", "care_access_regression",
                         "snapshot_fingerprints_and_no_current_rad_semantic_input")}}
    return data, report, findings, crosswalk


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true")
    parser.add_argument("--output-directory", type=Path, default=DEST)
    args = parser.parse_args()
    try:
        data, report, findings, crosswalk = publish()
        again = publish()
        g.require(data == again[0] and (report, findings, crosswalk) == again[1:], "Non-deterministic recovery")
        report["checks"]["deterministic_regeneration"] = "PASS"
        outputs = {OUTPUT.name: data, "validation.json": (json.dumps(report, ensure_ascii=False, indent=2)+"\n").encode(),
                   "recovery-findings.json": (json.dumps(findings, ensure_ascii=False, indent=2)+"\n").encode(),
                   "representation-crosswalk.json": (json.dumps(crosswalk, ensure_ascii=False, indent=2)+"\n").encode()}
        directory = args.output_directory.resolve()
        g.require(directory.is_relative_to(ROOT.resolve()) or directory.is_relative_to(Path('/tmp')), "Output outside repository or /tmp")
        inputs = {str((ROOT / p).resolve()) for p in json.loads(MANIFEST.read_text())["files_sha256"]}
        for name, content in outputs.items():
            path = directory / name
            g.require(str(path) not in inputs and path != MANIFEST, "Output would overwrite immutable input")
            if args.check:
                g.require(path.read_bytes() == content, f"Stale recovery output: {name}")
            else:
                directory.mkdir(parents=True, exist_ok=True)
                path.write_bytes(content)
        print(json.dumps({"status": "PASS", "counts": report["counts"], "output_sha256": report["output_sha256"]}, indent=2))
        return 0
    except (OSError, ValueError, KeyError, g.ET.LxmlError) as exc:
        print(f"FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
