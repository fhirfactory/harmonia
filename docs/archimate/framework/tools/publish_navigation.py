#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Project the locked capability publication into source-based navigation and Views.

Semantics come only from the original governed publisher, never organisations.
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
import publish_documentation as d
from architecture_release import DEFAULT_RELEASE, load_release

BASE = Path(__file__).resolve().parents[1]
DEFAULT_LOCK = BASE / "baseline/navigation-baseline-1.0.3.json"
CATEGORIES = dict(zip(g.LAYERS, ("Business Capabilities", "Business Enabling Capabilities", "Enterprise Capabilities")))
SUBJECTS = dict(zip(g.LAYERS, ("business", "business-enabling", "enterprise")))


def architecture_snapshot(root):
    """Protect all model content except the two explicitly presentational structures."""
    protected = copy.deepcopy(root)
    for name in ("organizations", "views"):
        for item in protected.findall("a:" + name, g.N):
            protected.remove(item)
    return g.digest(g.signature(protected))


def name(model, key):
    return model.elements[key].findtext("a:name", namespaces=g.N)


def ancestry(model, key):
    return model.containment.ancestry(key)


def detail_id(model, key):
    return g.visual_id(model.identifier, "view", "capability-context", key)


def folder_id(model, role, key):
    return g.visual_id(model.identifier, "navigation-folder", role, key)


def add_folder(parent, model, role, key, label):
    return g.folder(parent, folder_id(model, role, key), label)


def add_reference(parent, model, role, key):
    return g.child(parent, "item", identifier=g.visual_id(model.identifier, "navigation-reference", role, key),
                   identifierRef=key)


def detail_view(model, key):
    """A context above a wrapped child grid; only direct existing edges are drawn."""
    identifier = detail_id(model, key)
    view = g.ET.Element(g.tag("view"), identifier=identifier, attrib={g.TYPE: "Diagram"})
    g.child(view, "name", name(model, key) + " Overview")
    g.child(view, "documentation", "Navigation projection: current capability and immediate Composition children only. "
            "Folder containment creates no architecture. Yellow nodes reference existing semantic elements. "
            "Use the matching Views folder to open each child's own overview. "
            "A leaf has no inferred descendants. Existing candidate standing and unresolved RAD crosswalks are retained.")
    children = model.ordered(model.children[key])
    top = g.MARGIN + model.height(key) + 80
    positions, _ = g.grid(model, children, top)
    positions = {key: (g.MARGIN, g.MARGIN, g.WIDTH, model.height(key)), **positions}
    nodes = {}
    for element in [key, *children]:
        x, y, width, height = positions[element]
        nodes[element] = g.visual_id(model.identifier, "node", identifier, element)
        node = g.child(view, "node", identifier=nodes[element], elementRef=element,
                       x=str(x), y=str(y), w=str(width), h=str(height))
        node.set(g.TYPE, "Element")
        g.style(node, (255, 222, 150) if element == key else (255, 255, 181))
    for rid, relation in sorted(model.relationships.items()):
        source, target = relation.get("source"), relation.get("target")
        if source != key or target not in children:
            continue
        connection = g.child(view, "connection", identifier=g.visual_id(
            model.identifier, "connection", identifier, rid, nodes[source], nodes[target]),
            relationshipRef=rid, source=nodes[source], target=nodes[target])
        connection.set(g.TYPE, "Relationship")
        g.style(connection)
        tx, ty, _, th = positions[target]
        # A bus above the grid and vertical column gutters avoid child rectangles.
        for x, y in ((g.MARGIN + g.WIDTH // 2, top - 40),
                     (tx - 20, top - 40), (tx - 20, ty + th // 2)):
            g.child(connection, "bendpoint", x=str(x), y=str(y))
    g.ET.indent(view, space="  ", level=3)
    view.tail = "\n    "
    return view


def organizations(model, original):
    """Build navigation from semantic indexes; input folder ancestry is not read."""
    result = g.ET.Element(g.tag("organizations"))
    strategy = add_folder(result, model, "root", "strategy", "Strategy")
    relations = add_folder(result, model, "root", "relations", "Relations")
    relation_capabilities = add_folder(relations, model, "relations", "capabilities", "Capabilities")
    views = add_folder(result, model, "root", "views", "Views")
    old_group = g.visual_id(model.identifier, "folder", "capability-architecture")
    # Retain the diagnostic's existing navigation item, including its attributes.
    for previous in original.xpath("a:organizations/a:item[a:label='Views']/a:item", namespaces=g.N):
        if previous.get("identifier") not in (old_group, folder_id(model, "views", "capabilities")):
            views.append(copy.deepcopy(previous))
    capability_views = add_folder(views, model, "views", "capabilities", "Capabilities")
    add_reference(capability_views, model, "view", g.visual_id(model.identifier, "view", "landscape"))
    outgoing = collections.defaultdict(list)
    for rid, relation in sorted(model.relationships.items()):
        outgoing[relation.get("source")].append(rid)

    def branch(parent, key, role):
        context = add_folder(parent, model, role, key, name(model, key))
        if role == "elements":
            add_reference(context, model, role, key)
        elif role == "relations":
            for rid in outgoing[key]:
                add_reference(context, model, role, rid)
        else:
            add_reference(context, model, "view", detail_id(model, key))
        for child in model.ordered(model.children[key]):
            branch(context, child, role)

    for layer in g.LAYERS:
        subject = SUBJECTS[layer]
        elements = add_folder(strategy, model, "elements", subject, CATEGORIES[layer])
        edges = add_folder(relation_capabilities, model, "relations", subject, CATEGORIES[layer])
        diagrams = add_folder(capability_views, model, "views", subject, CATEGORIES[layer])
        add_reference(diagrams, model, "view", g.visual_id(model.identifier, "view", subject))
        if layer == g.LAYERS[2]:
            for key in model.selected(layer):
                add_reference(elements, model, "elements", key)
                if outgoing[key]:
                    context = add_folder(edges, model, "relations", key, name(model, key))
                    for rid in outgoing[key]:
                        add_reference(context, model, "relations", rid)
        else:
            for key in model.roots(layer):
                branch(elements, key, "elements")
                branch(edges, key, "relations")
                branch(diagrams, key, "views")
    g.ET.indent(result, space="  ", level=1)
    result.tail = "\n  "
    return result


def augment(original):
    model = g.Model(original)
    result = copy.deepcopy(original)
    # Reprojection replaces only our detailed Views and organisations.
    diagrams = result.find("a:views/a:diagrams", g.N)
    owned = {detail_id(model, key) for key in model.elements if model.layer(key) != g.LAYERS[2]}
    for view in list(diagrams):
        if view.get("identifier") in owned:
            diagrams.remove(view)
    for layer in g.LAYERS[:2]:
        for key in model.selected(layer):
            diagrams.append(detail_view(model, key))
    previous = result.find("a:organizations", g.N)
    result.replace(previous, organizations(model, original))
    # Repositioned property definitions contain historical blank-line indentation.
    # Strip only spaces before line breaks in XML-only whitespace; governed
    # text (including multiline Documentation) is never reformatted.
    for element in result.find("a:propertyDefinitions", g.N).iter():
        for attribute in ("text", "tail"):
            value = getattr(element, attribute)
            if value and not value.strip():
                setattr(element, attribute, re.sub(r"[ \t]+(\r?\n)", r"\1", value))
    return result


def navigation_homes(root):
    """Inspect actual XML paths independently of semantic ancestry."""
    homes = collections.defaultdict(list)
    def visit(item, path):
        label = item.findtext("a:label", namespaces=g.N)
        if item.get("identifierRef"):
            g.require(label is None and not item.findall("a:item", g.N), "A reference must not masquerade as a folder")
            homes[item.get("identifierRef")].append(tuple(path))
        else:
            g.require(bool(label), "Navigation folder needs a label")
            for child in item.findall("a:item", g.N):
                visit(child, [*path, label])
    for item in root.findall("a:organizations/a:item", g.N):
        visit(item, [])
    return homes


def validate_navigation(original, result):
    model = g.Model(result)
    homes = navigation_homes(result)
    view_ids = {v.get("identifier") for v in result.findall("a:views/a:diagrams/a:view", g.N)}
    g.require(set(homes) == set(model.elements) | set(model.relationships) | view_ids,
              "Navigation references missing, dangling or outside the publication")
    g.require(all(len(paths) == 1 for paths in homes.values()), "Every element, relationship and View needs exactly one navigation home")
    for key in model.elements:
        layer = model.layer(key)
        path = tuple(name(model, ancestor) for ancestor in ancestry(model, key))
        element_path = ("Strategy", CATEGORIES[layer]) + (() if layer == g.LAYERS[2] else path)
        g.require(homes[key] == [element_path], f"BC/BEC decomposition or orthogonal EC navigation mismatch: {key}")
        if layer != g.LAYERS[2]:
            g.require(model.properties[key].get("prop-tier") == f"CT{len(path)}" and len(path) <= 3,
                      "Publication tier/decomposition mismatch; do not infer or repair ancestry")
            g.require(homes[detail_id(model, key)] == [("Views", "Capabilities", CATEGORIES[layer], *path)],
                      f"Detailed View is outside its context hierarchy: {key}")
    for rid, relation in model.relationships.items():
        source = relation.get("source")
        path = tuple(name(model, key) for key in ancestry(model, source))
        g.require(homes[rid] == [("Relations", "Capabilities", CATEGORIES[model.layer(source)], *path)],
                  f"Relationship navigation must follow its source, not its target: {rid}")
    for key, _, layer in g.VIEW_SPECS:
        expected = ("Views", "Capabilities") + ((CATEGORIES[layer],) if layer else ())
        g.require(homes[g.visual_id(model.identifier, "view", key)] == [expected], "Overview View navigation mismatch")
    # Also pin exact folder/reference identities, labels, empty branches and order.
    g.require(g.signature(result.find("a:organizations", g.N)) == g.signature(organizations(model, original)),
              "Deterministic folder/reference identity or navigation structure changed")
    return homes


def validate_views(original, result):
    model = g.Model(result)
    old = {v.get("identifier"): v for v in original.findall("a:views/a:diagrams/a:view", g.N)}
    actual = {v.get("identifier"): v for v in result.findall("a:views/a:diagrams/a:view", g.N)}
    subjects = [key for key in model.elements if model.layer(key) != g.LAYERS[2]]
    g.require(set(actual) == set(old) | {detail_id(model, key) for key in subjects}, "Unexpected, missing or replaced View")
    for identifier, view in old.items():
        g.require(g.signature(actual[identifier]) == g.signature(view), f"Retained View contents/visual identities changed: {identifier}")
    for identifier, view in actual.items():
        nodes = {n.get("identifier"): n for n in view.findall("a:node", g.N)}
        for node in nodes.values():
            g.require(node.get(g.TYPE) == "Element" and node.get("elementRef") in model.elements,
                      "Invalid visual element reference")
        for connection in view.findall("a:connection", g.N):
            rid = connection.get("relationshipRef")
            g.require(rid in model.relationships and connection.get("source") in nodes and connection.get("target") in nodes,
                      "Invalid relationship or View-local endpoint reference")
            relation = model.relationships[rid]
            g.require(nodes[connection.get("source")].get("elementRef") == relation.get("source") and
                      nodes[connection.get("target")].get("elementRef") == relation.get("target"),
                      "Visual connection endpoints disagree with semantic endpoints")
    report = []
    for key in model.ordered(subjects):
        identifier = detail_id(model, key)
        view = actual[identifier]
        nodes = view.findall("a:node", g.N)
        scope = {key, *model.children[key]}
        g.require(len(nodes) == len(scope) and {n.get("elementRef") for n in nodes} == scope,
                  "Detailed View must contain current context and immediate children only")
        g.require(view.findtext("a:name", namespaces=g.N) == name(model, key) + " Overview", "Detailed View label drift")
        rectangles = []
        for node in nodes:
            semantic = node.get("elementRef")
            g.require(node.get("identifier") == g.visual_id(model.identifier, "node", identifier, semantic),
                      "Non-deterministic visual node identity")
            g.require(not node.findall("a:viewRef", g.N), "Semantic appearances must remain capability nodes")
            g.require(node.find("a:name", g.N) is None and node.find("a:label", g.N) is None,
                      "Capability labels must be inherited from semantics")
            x, y, width, height = (int(node.get(a)) for a in ("x", "y", "w", "h"))
            g.require(width >= g.WIDTH and height >= model.height(semantic) and
                      node.find("a:style/a:font", g.N).get("size") == "12", "Unreadable detailed node")
            for bx, by, bw, bh in rectangles:
                g.require(x + width <= bx or bx + bw <= x or y + height <= by or by + bh <= y, "Overlapping detailed nodes")
            rectangles.append((x, y, width, height))
        connections = view.findall("a:connection", g.N)
        expected = {rid for rid, r in model.relationships.items() if r.get("source") == key and r.get("target") in model.children[key]}
        g.require(len(connections) == len(expected) and {c.get("relationshipRef") for c in connections} == expected,
                  "Detailed View relationship scope mismatch")
        for connection in connections:
            g.require(connection.get("identifier") == g.visual_id(model.identifier, "connection", identifier,
                connection.get("relationshipRef"), connection.get("source"), connection.get("target")),
                "Non-deterministic visual connection identity")
        report.append(dict(identifier=identifier, name=name(model, key) + " Overview", context=key,
                           nodes=len(nodes), connections=len(connections),
                           navigation_path=["Views", "Capabilities", CATEGORIES[model.layer(key)],
                                            *(name(model, k) for k in ancestry(model, key))],
                           width=max(x+w for x,y,w,h in rectangles)+g.MARGIN,
                           height=max(y+h for x,y,w,h in rectangles)+g.MARGIN))
    return report


def validate(original, result, schema, preservation):
    schema.assertValid(result)
    g.validate_identifiers(result)
    g.require(architecture_snapshot(original) == preservation["architecture_content_sha256"], "Pre-navigation architecture baseline drift")
    g.require(architecture_snapshot(result) == architecture_snapshot(original),
              "Semantic elements, relationships, endpoints, Documentation or model metadata changed")
    model = g.Model(result)
    g.require(len(model.elements) == 228 and len(model.relationships) == 189, "Bounded semantic population changed")
    old_views = {v.get("identifier"): dict(name=v.findtext("a:name", namespaces=g.N), sha256=g.digest(g.signature(v)))
                 for v in original.findall("a:views/a:diagrams/a:view", g.N)}
    g.require(old_views == preservation["views"], "Pre-navigation View preservation evidence drift")
    homes = validate_navigation(original, result)
    details = validate_views(original, result)
    old_folders = {i.get("identifier") for i in original.findall("a:organizations//a:item", g.N) if not i.get("identifierRef")}
    folders = {i.get("identifier") for i in result.findall("a:organizations//a:item", g.N) if not i.get("identifierRef")}
    return dict(status="PASS", semantic_elements=228, semantic_relationships=189,
                architecture_content_sha256=architecture_snapshot(result),
                semantic_changes={k: 0 for k in ("elements_added", "elements_removed", "elements_changed",
                    "relationships_added", "relationships_removed", "relationships_changed", "semantic_identity_changes")},
                navigation=dict(element_homes=228, relationship_homes=189, view_homes=len(homes)-417,
                    folders_before=len(old_folders), folders_after=len(folders),
                    folder_ids_added=len(folders-old_folders), folder_ids_removed=len(old_folders-folders)),
                view_changes=dict(retained=list(old_views), added=[v["identifier"] for v in details], replaced=[]),
                detailed_views=details,
                checks={key: "PASS" for key in ("architecture_independent_of_folders", "zero_semantic_changes",
                    "original_14_documentation_fields_preserved", "exactly_one_canonical_home_per_object",
                    "relationship_homes_follow_source", "bc_bec_navigation_matches_composition",
                    "ec_catalogue_remains_orthogonal", "deterministic_folder_reference_ids",
                    "view_hierarchy_and_immediate_decomposition_scope", "original_five_views_and_visual_ids_preserved",
                    "view_local_and_semantic_endpoint_agreement", "readable_nonoverlapping_detailed_views",
                    "progressive_view_tree_references", "archi_5_9_xsd")})


def publish(baseline, schema, lock, mapping_path=d.DEFAULT_MAP, release_file=DEFAULT_RELEASE,
            navigation_lock=DEFAULT_LOCK):
    original_data, original_report = d.publish(baseline, schema, lock, mapping_path, release_file)
    preservation = json.loads(Path(navigation_lock).read_text())
    xml_path = "docs/archimate/capability-architecture/Harmonia-Capability-Architecture.xml"
    g.require(hashlib.sha256(original_data).hexdigest() == preservation["files_sha256"][xml_path],
              "Original publisher no longer replays the committed 1.0.3 XML")
    original = g.ET.fromstring(original_data)
    result = augment(original)
    data = g.serialize(result)
    report = validate(original, g.ET.fromstring(data), schema, preservation)
    g.require(data == g.serialize(augment(original)) == g.serialize(augment(result)),
              "Navigation generation is not deterministic/idempotent")
    report.update(architecture_release=load_release(release_file), output_sha256=hashlib.sha256(data).hexdigest(),
                  previous_publication_sha256=hashlib.sha256(original_data).hexdigest(),
                  documentation=original_report["documentation"],
                  generated_views=[*original_report["generated_views"], *report["detailed_views"]], retained_views=1)
    report["checks"].update(original_publisher_and_immutable_semantic_lock="PASS", repeated_generation_and_idempotence="PASS")
    return data, report


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--baseline", type=Path, default=BASE / "baseline/Harmonia-Capability-Architecture-v2.1a.xml")
    parser.add_argument("--lock", type=Path, default=BASE / "baseline/semantic-lock.json")
    parser.add_argument("--schema", type=Path, default=BASE / "schemas/archimate3_Diagram.xsd")
    parser.add_argument("--documentation-map", type=Path, default=d.DEFAULT_MAP)
    parser.add_argument("--navigation-lock", type=Path, default=DEFAULT_LOCK)
    parser.add_argument("--release-file", type=Path, default=DEFAULT_RELEASE)
    parser.add_argument("--output", type=Path, default=g.workspace.LEGACY_PUBLICATION)
    parser.add_argument("--report", type=Path, default=g.workspace.LEGACY_VALIDATION)
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    try:
        manifest = json.loads(args.documentation_map.read_text())
        inputs = {p.resolve() for p in (args.baseline, args.lock, args.schema, args.documentation_map, args.navigation_lock,
                   args.release_file, args.documentation_map.parent / manifest["source_document"])}
        g.require(args.output.resolve() not in inputs and args.report.resolve() not in inputs and
                  args.output.resolve() != args.report.resolve(), "Output must not overwrite an input")
        data, report = publish(g.parse(args.baseline), g.load_schema(args.schema), json.loads(args.lock.read_text()),
                               args.documentation_map, args.release_file, args.navigation_lock)
        if args.check:
            g.require(args.output.read_bytes() == data, "Published navigation differs from deterministic regeneration")
            g.require(json.loads(args.report.read_text()) == report, "Navigation validation report is stale")
        else:
            args.output.parent.mkdir(parents=True, exist_ok=True)
            args.report.parent.mkdir(parents=True, exist_ok=True)
            args.output.write_bytes(data)
            args.report.write_text(json.dumps(report, indent=2) + "\n")
        print(json.dumps({k: v for k, v in report.items() if k not in ("detailed_views", "generated_views", "view_changes")}, indent=2))
        return 0
    except (OSError, ValueError, KeyError, g.ET.LxmlError) as exc:
        print(f"FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
