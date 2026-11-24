#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Navigation and visual projections of exact governed keys and assertions.

No semantic element or relationship is created here. A Process's contextual
Feature path may follow its explicit governed_process Function; that path is
not a newly asserted Process/Feature semantic relationship.
"""
import collections
import copy

import generate_views as g
import recover_v02 as r
import publish_navigation as nav

DIRECT = {"realises_feature", "associated_feature", "contributes_feature"}


def associations(b):
    states = {x["relationship_index"]: x["record_state"] for x in b.source["relationship_currentness"]}
    result, unresolved = collections.defaultdict(dict), []
    current = []
    for i, edge in enumerate(b.source["relationships"]):
        if states[i] != "current":
            continue
        if edge["type"] in DIRECT and edge["status"] == "unresolved":
            unresolved.append({"source_index": i, **edge})
        if edge["status"] == "unresolved" or any(b.records[edge[k]]["record_state"] != "current" for k in ("from", "to")):
            continue
        current.append((i, edge))
        if edge["type"] in DIRECT:
            g.require(b.records[edge["from"]]["type"] in r.BEHAVIOURS and b.records[edge["to"]]["type"] == "feature",
                      "Feature association endpoint types changed")
            result[edge["from"]][edge["to"]] = [i]
    # This deliberately does not traverse uses_function, participating_function,
    # common ownership or arbitrary graph paths. Participation is not ownership.
    for i, edge in current:
        if edge["type"] == "governed_process":
            for feature, evidence in result.get(edge["from"], {}).items():
                result[edge["to"]][feature] = [*evidence, i]
    return dict(result), unresolved


def feature_view_path(b, key):
    return next(path for path in b.context_paths(b.records[key]) if path[:2] == ["Views", "2-BusinessLayer"])


def ordered(b, keys):
    return sorted(keys, key=lambda k: (b.records[k]["name"].casefold(), k))


def landscapes(b):
    """Reuse the existing immediate-child Views; add missing projection Views."""
    b.landscapes, b.landscape_first = {}, {}
    b.presentation_changes = {}
    views = {v.get("identifier"): v for v in b.root.findall("a:views/a:diagrams/a:view", g.N)}
    roles = {spec["role"]: vid for vid, spec in b.crosswalk["views"].items()}
    current = {k: e for k, e in b.records.items() if e["record_state"] == "current"}
    for category, title in r.CATEGORIES.items():
        roots = ordered(b, [k for k, e in current.items() if e.get("category") == category and
                            e["type"] in ("capability", "enterprise_capability") and e.get("tier") == "CT1"])
        strategy = ["Views", "1-Strategy", "Capabilities", title]
        contexts = [("root:" + category, None, roots, "CT1", strategy)]
        if category != "enterprise":
            contexts.append(("root:" + category, None, roots, "CT1", ["Views", "2-BusinessLayer", title]))
        for key, e in current.items():
            if e["type"] not in ("capability", "enterprise_capability") or e.get("category") != category:
                continue
            children = ordered(b, [k for k, c in current.items() if c.get("parent_key") == key and
                                  c["type"] in ("capability", "enterprise_capability")])
            level = {"CT1": "CT2", "CT2": "CT3"}.get(e.get("tier"))
            if e.get("tier") == "CT3":
                children = ordered(b, [k for k, c in current.items() if c["type"] == "feature" and
                                      b.feature_ancestry(c) and c.get("owner_key") == key])
                level = "Feature"
            if children:
                for path in b.context_paths(e):
                    if path[0] == "Views":
                        contexts.append((key, key, children, level, path))
        for subject, context, children, level, path in contexts:
            if not children:
                continue
            # Archi 5.10.0 sorts tree Views by displayed Name, independently of
            # Exchange item order. This local View-only prefix has a demonstrated
            # ordering purpose; semantic element Names/resources stay untouched.
            name = "0-" + (b.records[context]["name"] if context else title) + " — " + level + " Landscape"
            keys = ([context] if context else []) + children
            reuse = None
            if context and path[1] == "1-Strategy":
                role = ("features:" if level == "Feature" else "context:") + b.records[context]["reference_id"]
                reuse = roles[role]
            if reuse:
                view = views[reuse]
                g.require({n.get("elementRef") for n in view.findall("a:node", g.N) if n.get("elementRef")} == {b.ids[k] for k in keys},
                          "Retained landscape differs from immediate governed child population")
                view.find("a:name", g.N).text = name
                b.presentation_changes[reuse] = "landscape_name"
                vid = reuse
            else:
                role = path[1] + ":" + subject
                vid = b.add_view("landscape:" + role, name, [b.ids[k] for k in keys], path,
                                 note="Context and immediate governed children only. Standing remains in element Properties. Navigation and proximity create no semantic relationships.")
                # The parent appears above its direct children, without drawing
                # ungoverned containment lines, including CT3/Feature framing.
                view = b.root.find(f"a:views/a:diagrams/a:view[@identifier='{vid}']", g.N)
                for i, node in enumerate(view.findall("a:node", g.N)):
                    j = i - bool(context)
                    node.set("x", str(40 if j < 0 else 40 + j % 4 * 410))
                    node.set("y", str(40 if j < 0 else (230 if context else 40) + j // 4 * 190))
                    # Use the same standing colours as the retained context
                    # Views, rather than suggesting Candidate for every node.
                    style = node.find("a:style", g.N)
                    if style is not None:
                        node.remove(style)
                    color = {"Proposed": (255, 222, 150), "Candidate": (224, 200, 245)}.get(b.records[keys[i]]["architectural_status"], (255, 255, 181))
                    g.style(node, color)
            b.landscapes[vid] = {"category": category, "context": context, "children": children,
                                 "level": level, "path": path, "new": not bool(reuse)}
            b.landscape_first[tuple(path)] = vid


def business_layout(b):
    """Feature columns, with repeated *visual* appearances for shared behaviour.

    Existing pages retain their nodes/IDs. Missing associated Features and
    behaviour are added to the relevant page, so pagination cannot sever a
    known association. Only existing native relationships can be drawn.
    """
    b.layout_groups = {}
    rels = {x.get("identifier"): x for x in b.root.findall("a:relationships/a:relationship", g.N)}
    for vid, spec in b.crosswalk["views"].items():
        if not spec["role"].startswith("behaviour:"):
            continue
        view = b.root.find(f"a:views/a:diagrams/a:view[@identifier='{vid}']", g.N)
        context = spec["keys"][0]
        if b.records[context]["record_state"] != "current":
            continue
        keys = set(spec["keys"])
        features = {k for k in keys if b.records[k]["type"] == "feature" and b.records[k]["record_state"] == "current"}
        features.update(f for k in keys for f in b.feature_associations.get(k, {}))
        if not features:
            continue
        # Realising Functions sit nearest their Feature, so their native edges
        # do not run through a Service/Process box just because its name sorts
        # earlier. Type ordering is presentation, never a behavioural chain.
        groups = {f: sorted([k for k, fs in b.feature_associations.items() if f in fs],
                            key=lambda k: ({"function": 0, "process": 1, "service": 2}[b.records[k]["type"]],
                                           b.records[k]["name"].casefold(), k)) for f in ordered(b, features)}
        keys.update(features)
        keys.update(k for group in groups.values() for k in group)
        nodes = {n.get("elementRef"): n for n in view.findall("a:node", g.N) if n.get("elementRef")}
        for key in ordered(b, keys):
            semantic = b.ids[key]
            if semantic not in nodes:
                node = g.child(view, "node", identifier=b.ident("layout-node", vid, semantic), elementRef=semantic,
                               x="40", y="40", w="360", h="132")
                node.set(g.TYPE, "Element")
                color = {"Proposed": (255, 222, 150), "Candidate": (224, 200, 245)}.get(b.records[key]["architectural_status"], (255, 255, 181))
                g.style(node, color)
                nodes[semantic] = node
        appearances, used = {}, set()
        ctx = nodes[b.ids[context]]
        ctx.set("x", str(40 + max(0, len(groups)-1)*410//2))
        ctx.set("y", "40")
        for column, (feature, behaviours) in enumerate(groups.items()):
            x = 40 + column * 410
            fnode = nodes[b.ids[feature]]
            fnode.set("x", str(x)); fnode.set("y", "230")
            for row, key in enumerate(behaviours):
                node = nodes[b.ids[key]]
                if key in used:
                    node = copy.deepcopy(node)
                    node.set("identifier", b.ident("shared-layout-node", vid, b.ids[feature], b.ids[key]))
                    view.insert(list(view).index(view.find("a:connection", g.N)) if view.find("a:connection", g.N) is not None else len(view), node)
                used.add(key)
                node.set("x", str(x)); node.set("y", str(420 + row*180))
                appearances[(key, feature)] = node
        remaining = ordered(b, keys - features - used - {context})
        bottom = 420 + max((len(v) for v in groups.values()), default=0)*180
        for i, key in enumerate(remaining):
            node = nodes[b.ids[key]]
            node.set("x", str(40 + i % 4 * 410)); node.set("y", str(bottom + i//4*180))
        connections = {c.get("relationshipRef"): c for c in view.findall("a:connection", g.N)}
        for rid, rel in rels.items():
            a, z = rel.get("source"), rel.get("target")
            if a not in nodes or z not in nodes:
                continue
            source_record, target_record = b.byid.get(a), b.byid.get(z)
            pair = (source_record["key"], target_record["key"]) if source_record and target_record else None
            if pair not in appearances:
                continue
            conn = connections.get(rid)
            if conn is None:
                conn = g.child(view, "connection", identifier=b.ident("layout-connection", vid, rid), relationshipRef=rid,
                               source=appearances[pair].get("identifier"), target=nodes[z].get("identifier"))
                conn.set(g.TYPE, "Relationship"); g.style(conn)
            conn.set("source", appearances[pair].get("identifier"))
            conn.set("target", nodes[z].get("identifier"))
        # New nodes must precede connections in the Exchange sequence.
        for node in view.findall("a:node", g.N):
            view.remove(node)
            first = view.find("a:connection", g.N)
            view.insert(list(view).index(first) if first is not None else len(view), node)
        b.layout_groups[vid] = {"context": context, "groups": groups, "unassociated": remaining}
        b.presentation_changes[vid] = "feature_layout"


def folders(root):
    result = {}
    def walk(parent, path=()):
        for item in parent.findall("a:item", g.N):
            if not item.get("identifierRef"):
                current = (*path, item.findtext("a:label", namespaces=g.N))
                result[current] = item
                walk(item, current)
    walk(root.find("a:organizations", g.N))
    return result


def validate(b, root):
    """Assert the association survives each projection, and reject false homes."""
    homes, fs = nav.navigation_homes(root), folders(root)
    views = {v.get("identifier"): v for v in root.findall("a:views/a:diagrams/a:view", g.N)}
    associations_now, unresolved = associations(b)
    g.require(associations_now == b.feature_associations, "Feature association source index drift")
    grouped, shared, retained = collections.Counter(), [], []
    for key, e in b.records.items():
        if e["type"] not in r.BEHAVIOURS or e["record_state"] != "current":
            continue
        features = b.feature_associations.get(key, {})
        actual = homes[b.ids[key]][0]
        if len(features) == 1:
            feature = next(iter(features))
            path = b.context_paths(b.records[feature])[2]
            expected = (*b.display_paths[tuple(path)], r.BEHAVIOURS[e["type"]])
            g.require(actual == expected, "Associated Business behaviour missing from Feature resource context: " + key)
            grouped[e["type"]] += 1
        else:
            g.require(not any(x.startswith("[Feature]") for x in actual), "False or arbitrarily selected Feature resource association: " + key)
            (shared if features else retained).append({"key": key, "features": sorted(features), "home": list(actual)})
    relocated = 0
    for vid, spec in b.crosswalk["views"].items():
        if not spec["role"].startswith("assertion:"):
            continue
        feature = spec["keys"][1]
        expected = b.display_paths[tuple(feature_view_path(b, feature))]
        g.require(homes[vid] == [expected], "Feature-specific realisation View outside Feature context")
        g.require({b.ids[k] for k in spec["keys"]} <= {n.get("elementRef") for n in views[vid].findall("a:node", g.N)},
                  "Feature realisation View missing its governed endpoints")
        relocated += 1
    landscape_counts = collections.defaultdict(collections.Counter)
    # Reconstruct coverage from source, independently of the emitted registry.
    for category, title in r.CATEGORIES.items():
        paths = [("Views", "1-Strategy", "Capabilities", title)]
        if category != "enterprise":
            paths.append(("Views", "2-BusinessLayer", title))
        for key, e in b.records.items():
            if e["record_state"] != "current" or e.get("category") != category or e["type"] not in ("capability", "enterprise_capability"):
                continue
            children = [c for c in b.records.values() if c["record_state"] == "current" and
                        (c.get("parent_key") == key or (e.get("tier") == "CT3" and c["type"] == "feature" and
                         c.get("owner_key") == key and b.feature_ancestry(c)))]
            if children:
                paths.extend(tuple(p) for p in b.context_paths(e) if p[0] == "Views")
        for path in paths:
            g.require(sum(tuple(x["path"]) == path for x in b.landscapes.values()) == 1,
                      "Missing/duplicate landscape for governed hierarchical context")
    for vid, spec in b.landscapes.items():
        path = b.display_paths[tuple(spec["path"])]
        g.require(homes[vid] == [path], "Landscape outside its context")
        references = [x.get("identifierRef") for x in fs[path].findall("a:item", g.N) if x.get("identifierRef") in views]
        g.require(references and references[0] == vid, "Landscape is not first View in its context")
        names = {ref: views[ref].findtext("a:name", namespaces=g.N) for ref in references}
        g.require(names[vid].startswith("0-") and min(references, key=lambda ref: names[ref].casefold()) == vid,
                  "Landscape display Name does not sort first among context Views")
        context = spec["context"]
        if context:
            expected_children = {k for k,c in b.records.items() if c["record_state"] == "current" and
                                 (c.get("parent_key") == context or
                                  (b.records[context].get("tier") == "CT3" and c["type"] == "feature" and
                                   c.get("owner_key") == context and b.feature_ancestry(c)))}
        else:
            expected_children = {k for k,c in b.records.items() if c["record_state"] == "current" and
                                 c.get("category") == spec["category"] and c["type"] in ("capability", "enterprise_capability") and c.get("tier") == "CT1"}
        g.require(set(spec["children"]) == expected_children, "Landscape registry dropped or invented an immediate governed child")
        expected = {b.ids[k] for k in expected_children | ({context} if context else set())}
        population = [n.get("elementRef") for n in views[vid].findall("a:node", g.N) if n.get("elementRef")]
        g.require(set(population) == expected and len(population) == len(expected), "Landscape must contain only context and all immediate governed children")
        if spec["new"]:
            subject = context or "root:" + spec["category"]
            g.require(vid == b.ident("view", "landscape:" + spec["path"][1] + ":" + subject), "Non-deterministic landscape View ID")
        landscape_counts[spec["category"]][spec["level"]] += 1
    projected_pairs = set()
    for vid, spec in b.layout_groups.items():
        view = views[vid]
        nodes = view.findall("a:node", g.N)
        context = next(n for n in nodes if n.get("elementRef") == b.ids[spec["context"]])
        columns = set()
        for feature, behaviours in spec["groups"].items():
            fnode = next(n for n in nodes if n.get("elementRef") == b.ids[feature])
            x, y = int(fnode.get("x")), int(fnode.get("y"))
            g.require(x not in columns and y > int(context.get("y")), "Feature columns overlap or precede Capability context")
            columns.add(x)
            for key in behaviours:
                projected_pairs.add((key, feature))
                g.require(feature in b.feature_associations.get(key, {}), "Invented layout Feature association")
                g.require(any(n.get("elementRef") == b.ids[key] and int(n.get("x")) == x and int(n.get("y")) >= y + int(fnode.get("h"))
                              for n in nodes), "Associated behaviour not grouped beneath Feature in Business diagram")
        bottom = 420 + max((len(v) for v in spec["groups"].values()), default=0)*180
        for key in spec["unassociated"]:
            g.require(all(int(n.get("y")) >= bottom for n in nodes if n.get("elementRef") == b.ids[key]),
                      "Behaviour without an established association placed within a Feature column")
        rectangles = [(int(n.get("x")), int(n.get("y")), int(n.get("w")), int(n.get("h"))) for n in nodes]
        for i, (x, y, w, h) in enumerate(rectangles):
            g.require(all(x+w <= a or a+c <= x or y+h <= z or z+d <= y for a,z,c,d in rectangles[i+1:]), "Business diagram nodes overlap")
    g.require({(k,f) for k, features in b.feature_associations.items() for f in features} <= projected_pairs,
              "Governed Feature/Business association missing from diagram projection")
    return {"features_with_behaviour_associations": len({f for fs in b.feature_associations.values() for f in fs}),
            "grouped_by_feature": dict(grouped), "shared_behaviour_retained_at_capability": shared,
            "no_established_feature_association": retained, "unresolved_feature_assertions": unresolved,
            "realisation_views_in_feature_context": relocated,
            "landscapes_by_taxonomy_and_child_level": {k: dict(v) for k,v in landscape_counts.items()},
            "new_landscape_views": sum(x["new"] for x in b.landscapes.values()),
            "reused_landscape_views": sum(not x["new"] for x in b.landscapes.values()),
            "business_views_with_feature_layout": len(b.layout_groups)}
