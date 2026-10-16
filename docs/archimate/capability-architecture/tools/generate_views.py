#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Project a locked Exchange semantic baseline into four deterministic Views.

No repository JSON model is read. Existing semantic records are copied, never
allocated or reconstructed. Only this tool's visual objects are replaceable.
"""
import argparse
import copy
import hashlib
import json
from pathlib import Path
import sys
import textwrap
import uuid

from lxml import etree as ET


BASE = Path(__file__).resolve().parents[1]
NS = "http://www.opengroup.org/xsd/archimate/3.0/"
XSI = "http://www.w3.org/2001/XMLSchema-instance"
LANG = "{http://www.w3.org/XML/1998/namespace}lang"
TYPE = f"{{{XSI}}}type"
N = {"a": NS}
VISUAL_NAMESPACE = uuid.uuid5(
    uuid.NAMESPACE_URL, "urn:harmonia:capability-architecture:exchange-views:v1"
)
LAYERS = (
    "Business Capability", "Business Enabling Capability", "Enterprise Capability"
)
VIEW_SPECS = (
    ("landscape", "Harmonia Capability Landscape", None),
    ("business", "Business Capability Model", LAYERS[0]),
    ("business-enabling", "Business Enabling Capability Model", LAYERS[1]),
    ("enterprise", "Enterprise Capability Model", LAYERS[2]),
)
WIDTH, MIN_HEIGHT, COLUMN_GAP, ROW_GAP, BLOCK_GAP, MARGIN = 300, 84, 80, 28, 56, 40
GRID_COLUMNS = 4


class PublicationError(ValueError):
    """The baseline or projection violates the publication contract."""


def require(condition, message):
    if not condition:
        raise PublicationError(message)


def tag(name):
    return f"{{{NS}}}{name}"


def child(parent, local_name, text=None, **attributes):
    result = ET.SubElement(parent, tag(local_name), **attributes)
    if text is not None:
        result.text = text
        result.set(LANG, "en")
    return result


def parse(path):
    parser = ET.XMLParser(resolve_entities=False, no_network=True)
    tree = ET.parse(str(path), parser)
    require(not tree.docinfo.doctype, "A publication input must not contain a DTD")
    require(tree.getroot().tag == tag("model"), "Expected an ArchiMate Exchange model")
    return tree.getroot()


def visual_id(model_id, kind, *parts):
    """IDs depend on identity, never on labels, positions, order or clock time."""
    key = json.dumps([model_id, kind, *parts], separators=(",", ":"))
    return "id-" + uuid.uuid5(VISUAL_NAMESPACE, key).hex


def signature(element):
    """Compare XML content exactly, excluding indentation and namespace prefixes."""
    return [
        element.tag, sorted(element.attrib.items()),
        element.text if element.text and element.text.strip() else None,
        [signature(c) for c in element if isinstance(c.tag, str)],
    ]


def digest(value):
    return hashlib.sha256(json.dumps(value, ensure_ascii=False, sort_keys=True,
                                     separators=(",", ":")).encode()).hexdigest()


def semantic_snapshot(root):
    """Protect model metadata, records, properties and semantic organisations.

    Views and the Views navigation folder are the only excluded content.
    Semantic organisation order/structure is protected along with the records.
    """
    protected = copy.deepcopy(root)
    for views in protected.findall("a:views", N):
        protected.remove(views)
    for organizations in protected.findall("a:organizations", N):
        for item in list(organizations):
            if item.findtext("a:label", namespaces=N) == "Views":
                organizations.remove(item)
        if not len(organizations):
            protected.remove(organizations)
    return {
        "model_identifier": root.get("identifier"),
        "protected_content_sha256": digest(signature(protected)),
        "elements": {
            e.get("identifier"): digest(signature(e))
            for e in root.findall("a:elements/a:element", N)
        },
        "relationships": {
            r.get("identifier"): digest(signature(r))
            for r in root.findall("a:relationships/a:relationship", N)
        },
    }


def validate_lock(root, lock):
    require(semantic_snapshot(root) == lock["semantic_snapshot"],
            "Semantic identity/content lock mismatch; do not regenerate or reconcile semantics")


def retained_navigation(root):
    """Only the generated navigation branch may be replaced on regeneration."""
    group_id = visual_id(root.get("identifier"), "folder", "capability-architecture")
    created_folder_id = visual_id(root.get("identifier"), "folder", "Views")
    result = []
    for item in root.xpath("a:organizations/a:item[a:label='Views']", namespaces=N):
        item = copy.deepcopy(item)
        for group in list(item):
            if group.get("identifier") == group_id:
                item.remove(group)
        if item.get("identifier") == created_folder_id and not item.findall("a:item", N):
            continue
        result.append(signature(item))
    return result


class LocalSchemaResolver(ET.Resolver):
    def __init__(self, directory):
        super().__init__()
        self.directory = directory

    def resolve(self, url, public_id, context):
        if url in ("http://www.w3.org/2001/xml.xsd", "http://www.w3.org/2001/03/xml.xsd"):
            return self.resolve_filename(str(self.directory / "xml.xsd"), context)
        if "://" in url:
            raise PublicationError(f"Remote schema resolution is disabled: {url}")
        return None


def load_schema(path):
    path = Path(path).resolve()
    parser = ET.XMLParser(resolve_entities=False, no_network=True)
    parser.resolvers.add(LocalSchemaResolver(path.parent))
    return ET.XMLSchema(ET.parse(str(path), parser))


def validate_identifiers(root):
    ids = [e.get("identifier") for e in root.iter() if e.get("identifier") is not None]
    require(all(ids) and len(ids) == len(set(ids)), "Duplicate or empty identifier")


class Model:
    """Read-only indexes over capabilities and existing Composition edges."""
    def __init__(self, root):
        validate_identifiers(root)
        self.identifier = root.get("identifier")
        self.elements = {e.get("identifier"): e for e in root.findall("a:elements/a:element", N)}
        self.relationships = {
            r.get("identifier"): r for r in root.findall("a:relationships/a:relationship", N)
        }
        self.properties = {
            key: {p.get("propertyDefinitionRef"): p.findtext("a:value", namespaces=N)
                  for p in e.findall("a:properties/a:property", N)}
            for key, e in self.elements.items()
        }
        self.children = {key: [] for key in self.elements}
        self.parents = {}
        for key, e in self.elements.items():
            require(e.get(TYPE) == "Capability", f"Non-capability in bounded baseline: {key}")
            require(self.properties[key].get("prop-layer") in LAYERS,
                    f"Missing or unsupported existing layer property: {key}")
        for key, r in self.relationships.items():
            require(r.get(TYPE) == "Composition", f"Non-Composition in bounded baseline: {key}")
            source, target = r.get("source"), r.get("target")
            require(source in self.elements and target in self.elements,
                    f"Unresolved semantic relationship endpoint: {key}")
            require(self.layer(source) == self.layer(target),
                    f"Cross-layer Composition needs explicit presentation handling: {key}")
            require(target not in self.parents,
                    f"Multiple Composition parents need explicit presentation handling: {target}")
            self.parents[target] = source
            self.children[source].append(target)
        # Walk from existing roots, detecting cycles without filling missing ancestry.
        reached = set()
        def visit(key):
            require(key not in reached, f"Cyclic Composition hierarchy: {key}")
            reached.add(key)
            for c in self.children[key]:
                visit(c)
        for key in self.elements.keys() - self.parents.keys():
            visit(key)
        require(len(reached) == len(self.elements), "Cyclic Composition hierarchy has no root")

    def layer(self, key):
        return self.properties[key]["prop-layer"]

    def ordered(self, keys):
        return sorted(keys, key=lambda k: (self.elements[k].findtext("a:name", "", N).casefold(), k))

    def selected(self, layer):
        return self.ordered(k for k in self.elements if self.layer(k) == layer)

    def roots(self, layer):
        return self.ordered(k for k in self.selected(layer) if k not in self.parents)

    def height(self, key):
        name = self.elements[key].findtext("a:name", "", N)
        lines = textwrap.wrap(name, width=30, break_on_hyphens=False)
        return max(MIN_HEIGHT, 28 + 18 * len(lines))


def grid(model, keys, top):
    """Wrap siblings at a fixed column count; never reduce font or node width."""
    positions = {}
    for start in range(0, len(keys), GRID_COLUMNS):
        row = keys[start:start + GRID_COLUMNS]
        for column, key in enumerate(row):
            positions[key] = (MARGIN + column * (WIDTH + COLUMN_GAP), top, WIDTH, model.height(key))
        top += max(model.height(k) for k in row) + ROW_GAP
    return positions, top


def layout(model, layer):
    if layer is None:
        positions, top = {}, MARGIN
        for band in LAYERS:
            placed, top = grid(model, model.roots(band), top)
            positions.update(placed)
            top += BLOCK_GAP
        return positions
    keys = model.selected(layer)
    if not any(model.children[k] for k in keys):
        return grid(model, keys, MARGIN)[0]
    heights = {}
    def measure(key):
        children = model.ordered(model.children[key])
        height = sum(measure(c) for c in children) + ROW_GAP * max(0, len(children) - 1)
        heights[key] = max(model.height(key), height)
        return heights[key]
    positions = {}
    def place(key, depth, top):
        positions[key] = (MARGIN + depth * (WIDTH + COLUMN_GAP),
                          top + (heights[key] - model.height(key)) // 2,
                          WIDTH, model.height(key))
        for c in model.ordered(model.children[key]):
            place(c, depth + 1, top)
            top += heights[c] + ROW_GAP
    top = MARGIN
    for key in model.roots(layer):
        measure(key)
        place(key, 0, top)
        top += heights[key] + BLOCK_GAP
    return positions


def style(parent, fill=None):
    s = child(parent, "style")
    if fill:
        child(s, "fillColor", r=str(fill[0]), g=str(fill[1]), b=str(fill[2]), a="100")
    child(s, "lineColor", r="92", g="92", b="92")
    font = child(s, "font", name="Sans", size="12")
    child(font, "color", r="0", g="0", b="0")


def generate_view(model, key, name, layer):
    view_id = visual_id(model.identifier, "view", key)
    view = ET.Element(tag("view"), identifier=view_id, attrib={TYPE: "Diagram"})
    child(view, "name", name)
    description = (
        "Visual projection only; labels and semantic properties are inherited unchanged. "
        "Only existing Composition relationships are drawn. Features are excluded. "
    )
    if layer is None:
        description += "Top-level capabilities in wrapped bands: Business, Business Enabling, Enterprise."
    elif layer == LAYERS[1]:
        description += (
            "Existing hierarchy runs left to right, with siblings stacked vertically. "
            "Entity Management, Service Administration, Service Delivery, Health Service Operations, "
            "and Intrinsic / Shared Enablement are presentation groupings, not capability tiers. "
            "This baseline provides no natural-view membership; none is inferred."
        )
    elif layer == LAYERS[2] and not any(model.children[k] for k in model.selected(layer)):
        description += "The baseline establishes no Enterprise Composition hierarchy; capabilities use a wrapped grid."
    else:
        description += "Existing hierarchy runs left to right; siblings are stacked vertically."
    child(view, "documentation", description)
    positions = layout(model, layer)
    nodes = {}
    for element_id in model.ordered(positions):
        x, y, w, h = positions[element_id]
        node_id = visual_id(model.identifier, "node", view_id, element_id)
        nodes[element_id] = node_id
        node = child(view, "node", identifier=node_id, elementRef=element_id,
                     x=str(x), y=str(y), w=str(w), h=str(h))
        node.set(TYPE, "Element")
        style(node, (255, 255, 181))
    for relationship_id, r in sorted(model.relationships.items()):
        source, target = r.get("source"), r.get("target")
        if source not in nodes or target not in nodes:
            continue
        connection = child(view, "connection", identifier=visual_id(
            model.identifier, "connection", view_id, relationship_id, nodes[source], nodes[target]
        ), relationshipRef=relationship_id, source=nodes[source], target=nodes[target])
        connection.set(TYPE, "Relationship")
        style(connection)
        sx, sy, sw, sh = positions[source]
        _, ty, _, th = positions[target]
        # A shared vertical trunk stays in the column gap, outside node bounds.
        trunk = sx + sw + COLUMN_GAP // 2
        child(connection, "bendpoint", x=str(trunk), y=str(sy + sh // 2))
        child(connection, "bendpoint", x=str(trunk), y=str(ty + th // 2))
    ET.indent(view, space="  ", level=3)
    view.tail = "\n    "
    return view


def folder(parent, identifier, label):
    item = child(parent, "item", identifier=identifier)
    child(item, "label", label)
    return item


def augment(baseline):
    """Replace our four Views on regeneration, retaining every unrelated View."""
    model = Model(baseline)
    root = copy.deepcopy(baseline)
    views = root.find("a:views", N)
    if views is None:
        views = child(root, "views")
    diagrams = views.find("a:diagrams", N)
    if diagrams is None:
        diagrams = child(views, "diagrams")
    owned = {visual_id(model.identifier, "view", key) for key, _, _ in VIEW_SPECS}
    for view in list(diagrams):
        if view.get("identifier") in owned:
            diagrams.remove(view)
    for spec in VIEW_SPECS:
        diagrams.append(generate_view(model, *spec))
    organizations = root.find("a:organizations", N)
    if organizations is None:
        organizations = ET.Element(tag("organizations"))
        # ModelType orders organisations before propertyDefinitions and views.
        before = next((e for e in root if e.tag in (tag("propertyDefinitions"), tag("views"))), None)
        if before is None:
            root.append(organizations)
        else:
            root.insert(root.index(before), organizations)
    views_folder = next((item for org in root.findall("a:organizations", N)
                         for item in org.findall("a:item", N)
                         if item.findtext("a:label", namespaces=N) == "Views"), None)
    if views_folder is None:
        views_folder = folder(organizations, visual_id(model.identifier, "folder", "Views"), "Views")
    group_id = visual_id(model.identifier, "folder", "capability-architecture")
    for item in list(views_folder):
        if item.get("identifier") == group_id:
            views_folder.remove(item)
    group = folder(views_folder, group_id, "Capability Architecture")
    overview = folder(group, visual_id(model.identifier, "folder", "overview"), "Overview")
    models = folder(group, visual_id(model.identifier, "folder", "models"), "Capability Models")
    for key, _, layer in VIEW_SPECS:
        child(overview if layer is None else models, "item",
              identifier=visual_id(model.identifier, "view-reference", key),
              identifierRef=visual_id(model.identifier, "view", key))
    ET.indent(group, space="  ", level=3)
    group.tail = "\n    "
    return root


def validate(baseline, result, schema, lock):
    """Validate preservation, coverage, visual identity, references and geometry."""
    schema.assertValid(baseline)
    schema.assertValid(result)
    validate_lock(baseline, lock)
    validate_lock(result, lock)
    require(semantic_snapshot(baseline) == semantic_snapshot(result), "Semantic content changed")
    require(retained_navigation(baseline) == retained_navigation(result),
            "Existing Views navigation changed")
    validate_identifiers(result)
    model = Model(result)
    all_views = result.findall("a:views/a:diagrams/a:view", N)
    by_id = {v.get("identifier"): v for v in all_views}
    owned = {visual_id(model.identifier, "view", key) for key, _, _ in VIEW_SPECS}
    for old in baseline.findall("a:views/a:diagrams/a:view", N):
        if old.get("identifier") not in owned:
            require(old.get("identifier") in by_id and signature(old) == signature(by_id[old.get("identifier")]),
                    "Existing unrelated View changed")
    for view in all_views:
        nodes = {node.get("identifier"): node for node in view.findall(".//a:node", N)}
        for node in nodes.values():
            require(node.get(TYPE) == "Element" and node.get("elementRef") in model.elements,
                    "Unresolved capability elementRef or non-element visual node")
        for c in view.findall("a:connection", N):
            require(c.get(TYPE) == "Relationship" and c.get("relationshipRef") in model.relationships,
                    "Unresolved relationshipRef or non-relationship visual connection")
            require(c.get("source") in nodes and c.get("target") in nodes,
                    "Connection endpoint is not a node in its View")
            relationship = model.relationships[c.get("relationshipRef")]
            require(nodes[c.get("source")].get("elementRef") == relationship.get("source") and
                    nodes[c.get("target")].get("elementRef") == relationship.get("target"),
                    "Visual endpoints disagree with semantic relationship endpoints")
    group_id = visual_id(model.identifier, "folder", "capability-architecture")
    groups = result.xpath("a:organizations/a:item[a:label='Views']/a:item[@identifier=$id]", namespaces=N, id=group_id)
    require(len(groups) == 1, "Missing or duplicate generated Views organisation")
    group = groups[0]
    report = []
    for key, name, layer in VIEW_SPECS:
        view_id = visual_id(model.identifier, "view", key)
        require(view_id in by_id, f"Missing generated View: {name}")
        view = by_id[view_id]
        require(view.findtext("a:name", namespaces=N) == name, "Generated View label changed")
        target_folder = "Overview" if layer is None else "Capability Models"
        refs = group.xpath("a:item[a:label=$label]/a:item/@identifierRef", namespaces=N, label=target_folder)
        require(refs.count(view_id) == 1, f"View not referenced in appropriate Views organisation: {name}")
        nodes = view.findall("a:node", N)
        represented = {node.get("elementRef") for node in nodes}
        expected = (set().union(*(set(model.roots(l)) for l in LAYERS))
                    if layer is None else set(model.selected(layer)))
        require(represented == expected and len(nodes) == len(expected), f"View capability coverage changed: {name}")
        for node in nodes:
            require(node.get("identifier") == visual_id(model.identifier, "node", view_id, node.get("elementRef")),
                    "Visual node identifier is not deterministic")
        connections = view.findall("a:connection", N)
        expected_relations = {rid for rid, r in model.relationships.items()
                              if r.get("source") in expected and r.get("target") in expected}
        require({c.get("relationshipRef") for c in connections} == expected_relations and
                len(connections) == len(expected_relations), f"View relationship coverage changed: {name}")
        for c in connections:
            require(c.get("identifier") == visual_id(model.identifier, "connection", view_id,
                    c.get("relationshipRef"), c.get("source"), c.get("target")),
                    "Visual connection identifier is not deterministic")
        rectangles = [tuple(int(node.get(k)) for k in ("x", "y", "w", "h")) for node in nodes]
        for i, (x, y, w, h) in enumerate(rectangles):
            require(w >= WIDTH and h >= MIN_HEIGHT, "Unreadably reduced generated node dimensions")
            for bx, by, bw, bh in rectangles[i + 1:]:
                require(x + w <= bx or bx + bw <= x or y + h <= by or by + bh <= y,
                        f"Overlapping generated nodes: {name}")
        report.append({"name": name, "identifier": view_id, "nodes": len(nodes),
                       "connections": len(connections),
                       "width": max(x + w for x, y, w, h in rectangles) + MARGIN,
                       "height": max(y + h for x, y, w, h in rectangles) + MARGIN})
    return {
        "status": "PASS", "semantic_elements": len(model.elements),
        "semantic_relationships": len(model.relationships),
        "protected_content_sha256": semantic_snapshot(result)["protected_content_sha256"],
        "checks": {name: "PASS" for name in (
            "semantic_identity_and_content_lock", "global_identifier_uniqueness", "element_references",
            "relationship_references", "view_local_connection_endpoints", "semantic_endpoint_agreement",
            "generated_view_coverage", "views_organisation", "visual_identity_derivation",
            "node_dimensions_and_no_overlap", "xml_schema", "existing_view_preservation",
            "existing_view_navigation_preservation")},
        "generated_views": report, "retained_views": len(all_views) - len(owned),
    }


def serialize(root):
    # Existing subtree whitespace is retained; only new subtrees were indented.
    return ET.tostring(root, encoding="UTF-8", xml_declaration=True) + b"\n"


def publish(baseline, schema, lock):
    result = augment(baseline)
    # Validate the serialized/reparsed representation, as the importer will see it.
    data = serialize(result)
    parsed = ET.fromstring(data, ET.XMLParser(resolve_entities=False, no_network=True))
    report = validate(baseline, parsed, schema, lock)
    require(data == serialize(augment(baseline)), "Repeated generation differs")
    require(data == serialize(augment(parsed)), "Regeneration is not idempotent")
    report["checks"]["repeated_generation_and_idempotence"] = "PASS"
    report["output_sha256"] = hashlib.sha256(data).hexdigest()
    return data, report


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--baseline", type=Path, default=BASE / "baseline/Harmonia-Capability-Architecture-v2.1a.xml")
    parser.add_argument("--lock", type=Path, default=BASE / "baseline/semantic-lock.json")
    parser.add_argument("--schema", type=Path, default=BASE / "schemas/archimate3_Diagram.xsd")
    parser.add_argument("--output", type=Path, default=BASE / "Harmonia-Capability-Architecture.xml")
    parser.add_argument("--report", type=Path, default=BASE / "validation.json")
    parser.add_argument("--check", action="store_true", help="Validate committed output and compare with regeneration; write nothing")
    args = parser.parse_args()
    try:
        require(args.output.resolve() not in (args.baseline.resolve(), args.lock.resolve(), args.schema.resolve()),
                "Output must not overwrite an input")
        require(args.report.resolve() not in (args.baseline.resolve(), args.lock.resolve(), args.schema.resolve(), args.output.resolve()),
                "Report must not overwrite an input or the output")
        baseline = parse(args.baseline)
        lock = json.loads(args.lock.read_text())
        schema = load_schema(args.schema)
        data, report = publish(baseline, schema, lock)
        if args.check:
            validate(baseline, parse(args.output), schema, lock)
            require(args.output.read_bytes() == data, "Published output differs from deterministic regeneration")
            require(json.loads(args.report.read_text()) == report, "Published validation report differs from current checks")
        else:
            args.output.write_bytes(data)
            args.report.write_text(json.dumps(report, indent=2) + "\n")
        print(json.dumps(report, indent=2))
        return 0
    except (PublicationError, ET.LxmlError, OSError, KeyError, json.JSONDecodeError) as exc:
        print(f"FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
