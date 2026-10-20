# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Project semantic containment and relationship sources into Exchange navigation.

The caller declares the element population and native collection anchors. Existing
folders supply presentation scaffolding only; they never supply semantic ancestry.
No element, relationship, Property, Documentation or View is authored here.
"""
from collections import defaultdict
import copy
import hashlib
import json

from element_correlation import canonical_index
from change_review import native_type
import generate_views as g
import containment

VERSION = 'composition-source-navigation-v1'
INFORMATION_ANCHORS = {
    'BusinessObject': ('Business', 'BusinessObjectMap'),
    'DataObject': ('Application', 'InformationMap'),
    'Artifact': ('Technology & Physical', 'DataMap'),
}


def index(root, path):
    return {n.get('identifier'): n for n in root.findall(path, g.N)}


def projection_id(model_id, role, key):
    """A separate, versioned workspace namespace; never a canonicalId allocation."""
    value = json.dumps([VERSION, model_id, role, key], ensure_ascii=False, separators=(',', ':'))
    return 'workspace-' + hashlib.sha256(value.encode()).hexdigest()[:32]


def locations(root):
    """Reference locations, using folder nodes rather than label-based identity."""
    result = defaultdict(list)

    def walk(parent, path):
        for item in parent.findall('a:item', g.N):
            if item.get('identifierRef'):
                result[item.get('identifierRef')].append((path, item))
            else:
                walk(item, [*path, item])

    for org in root.findall('a:organizations', g.N):
        walk(org, [])
    return result


def labels(path):
    return [node.findtext('a:label', namespaces=g.N) for node in path]


def protected(root):
    result = copy.deepcopy(root)
    for org in result.findall('a:organizations', g.N):
        result.remove(org)
    return g.signature(result)


def project(model, root, anchors=INFORMATION_ANCHORS, resolved=None, placements=None):
    """Return a new tree and auditable identity-based placement plan.

    Only the declared native types' Elements navigation is rebuilt. All semantic
    relationship types, including sources outside that population, get Relations
    navigation. Other element homes and the complete Views tree are retained.
    """
    result = copy.deepcopy(root)
    elements = index(result, 'a:elements/a:element')
    relationships = index(result, 'a:relationships/a:relationship')
    if placements is None:
        placements = {rid: dict(anchor=anchors[native_type(node)], suffix=())
                      for rid, node in elements.items() if native_type(node) in anchors}
    scope = set(placements)
    g.require(scope <= elements.keys(), 'Unknown declared navigation element')
    resolved = containment.resolve(root) if resolved is None else resolved
    paths = resolved.paths(scope, closed=True)
    claims = canonical_index(model)
    g.require(not claims['issues'], 'Ambiguous canonical identity prevents workspace projection')
    keys = {rid: 'canonical:' + claim['canonicalId'] if claim['valid'] else 'representation:' + rid
            for rid, claim in claims['claims'].items()}
    model_id = result.get('identifier')
    orgs = result.findall('a:organizations', g.N)
    g.require(len(orgs) == 1, 'Expected one unambiguous Archi organisation tree')
    org, = orgs
    before_locations = locations(result)
    for rid in index(result, 'a:views/a:diagrams/a:view'):
        homes = before_locations[rid]
        g.require(len(homes) == 1 and labels(homes[0][0])[:1] == ['Views'],
                  'Unestablished View navigation; preserve for adjudication: ' + rid)
    for rid in elements:
        g.require(len(before_locations[rid]) <= 1, 'Multiple element navigation references: ' + rid)
        if rid not in scope:
            g.require(len(before_locations[rid]) == 1, 'Unprojected element has no navigation home: ' + rid)

    # For a complete declared population retain only Views. No submitted Element
    # or Relationship folders (including collection anchors) feed this rebuild.
    if scope == elements.keys():
        for item in list(org):
            if item.findtext('a:label', namespaces=g.N) != 'Views':
                org.remove(item)

    def find_anchor(path):
        parent = org
        for depth, label in enumerate(path, 1):
            matches = [n for n in parent.findall('a:item', g.N)
                       if not n.get('identifierRef') and n.findtext('a:label', namespaces=g.N) == label]
            g.require(len(matches) <= 1, 'Ambiguous declared navigation anchor: ' + '/'.join(path))
            if matches:
                parent, = matches
            else:
                # The explicit policy path is presentation configuration.
                parent = g.folder(parent, projection_id(model_id, 'collection', path[:depth]), label)
        return parent

    anchor_nodes = {path: find_anchor(path) for path in sorted({tuple(p['anchor']) for p in placements.values()})}
    # Remove the prior IA references and only their emptied label-only folders.
    # Existing folder ancestry is discarded, not interpreted as Composition.
    prune = set()
    references = {}
    for rid in sorted(scope):
        references[rid] = g.ET.Element(g.tag('item'), identifier=projection_id(model_id, 'element-reference', keys[rid]),
                                       identifierRef=rid)
        if not before_locations[rid]:
            continue
        path, item = before_locations[rid][0]
        item.getparent().remove(item)
        prune.update(n for n in path if n not in anchor_nodes.values() and n.getparent() is not org)
    for node in sorted(prune, key=lambda n: len(list(n.iterancestors())), reverse=True):
        if node.getparent() is not None and all(n.tag == g.tag('label') for n in node):
            node.getparent().remove(node)

    child_parents = {path[-2] for path in paths.values() if len(path) > 1}
    participants = child_parents | {rid for rid, path in paths.items() if len(path) > 1}
    folders, context_nodes, plan = {}, {}, {}
    for rid in sorted(scope):
        anchor = tuple(placements[rid]['anchor'])
        parent = anchor_nodes[anchor]
        chain = paths[rid] if rid in child_parents else paths[rid][:-1]
        for ancestor in chain:
            token = (anchor, ancestor)
            if token not in folders:
                folder = g.folder(parent, projection_id(model_id, 'element-context', [anchor, keys[ancestor]]),
                                  elements[ancestor].findtext('a:name', namespaces=g.N))
                folders[token] = folder
            parent = folders[token]
        for offset, label in enumerate(placements[rid].get('suffix', ())):
            token = (anchor, tuple(chain), 'role', offset, label)
            if token not in folders:
                folders[token] = g.folder(parent, projection_id(model_id, 'element-role',
                    [anchor, [keys[key] for key in chain], offset, label]), label)
            parent = folders[token]
        # Keep governed collection/role placement, then give every contained
        # leaf its own context too. Standalone elements acquire no containment.
        if rid in participants and rid not in child_parents:
            token = (anchor, rid)
            folders[token] = g.folder(parent, projection_id(model_id, 'element-context', [anchor, keys[rid]]),
                                      elements[rid].findtext('a:name', namespaces=g.N))
            parent = folders[token]
        parent.append(references[rid])
        if rid in participants:
            context_nodes[rid] = parent
        plan[rid] = dict(identity=keys[rid], composition_path=paths[rid], anchor=list(anchor),
                         folder_ancestors=paths[rid] if rid in participants else chain,
                         suffix=list(placements[rid].get('suffix', ())),
                         context_folder=parent.get('identifier') if rid in participants else None)

    # Replace Relations only. Views and all other top-level items are untouched.
    prior_relations = [n for n in org.findall('a:item', g.N)
                       if n.findtext('a:label', namespaces=g.N) == 'Relations' and not n.get('identifierRef')]
    g.require(len(prior_relations) <= 1, 'Ambiguous Relations category')
    relations = g.ET.Element(g.tag('item'), identifier=projection_id(model_id, 'category', 'Relations'))
    g.child(relations, 'label', 'Relations')
    if prior_relations:
        org.replace(prior_relations[0], relations)
    else:
        views = org.xpath("a:item[a:label='Views']", namespaces=g.N)
        org.insert(list(org).index(views[0]) if views else len(org), relations)
    homes = locations(result)
    outgoing = defaultdict(list)
    for rid, edge in sorted(relationships.items()):
        g.require(edge.get('source') in elements and edge.get('target') in elements,
                  'Unresolved relationship endpoint: ' + rid)
        outgoing[edge.get('source')].append(rid)

    mirrored, sources, relationship_plan = {}, {}, {}
    for source in sorted(outgoing):
        path, _ = homes[source][0]
        # Relationship navigation retains its established source contexts and
        # projection identities. A new Element leaf context replaces the former
        # bare reference, not the existing Relationship source folder.
        if source in participants and source not in child_parents:
            path = path[:-1]
        parent = relations
        for depth, original in enumerate(path):
            # Retained scaffolding has separate presentation identity. No folder
            # key, label or position is used to construct semantic ancestry.
            folder_key = original.get('identifier') or ['retained-path', labels(path[:depth + 1])]
            token = tuple(n.get('identifier') or n.findtext('a:label', namespaces=g.N) for n in path[:depth + 1])
            if token not in mirrored:
                mirrored[token] = g.folder(parent, projection_id(model_id, 'relations-mirror', folder_key),
                                           original.findtext('a:label', namespaces=g.N))
            parent = mirrored[token]
        if source not in child_parents:
            parent = g.folder(parent, projection_id(model_id, 'relationship-source', keys[source]),
                              elements[source].findtext('a:name', namespaces=g.N))
        sources[source] = parent
        for rid in outgoing[source]:
            g.child(parent, 'item', identifier=projection_id(model_id, 'relationship-reference', rid), identifierRef=rid)
            relationship_plan[rid] = dict(source=source, source_identity=keys[source],
                                          source_context_folder=parent.get('identifier'))

    # Stable sibling ordering is presentation only. Keep the element itself first
    # within its containment context; stable IDs break equal-label ties.
    touched = {*anchor_nodes.values(), *folders.values(), relations, *mirrored.values(), *sources.values()}
    for parent in touched:
        items = parent.findall('a:item', g.N)
        def order(item):
            ref = item.get('identifierRef')
            own = ref in context_nodes and context_nodes[ref] is parent
            label = item.findtext('a:label', namespaces=g.N) or (
                elements[ref].findtext('a:name', namespaces=g.N) if ref in elements else ref or '')
            return (not own, label.casefold(), label, item.get('identifier') or ref or '')
        for item in sorted(items, key=order):
            parent.append(item)
    if scope == elements.keys():
        categories = ['Strategy', 'Business', 'Application', 'Technology & Physical', 'Motivation', 'Relations', 'Views']
        for item in sorted(org, key=lambda n: categories.index(n.findtext('a:label', namespaces=g.N))
                           if n.findtext('a:label', namespaces=g.N) in categories else len(categories)):
            org.append(item)
    # Indent only new branches; existing Views and semantic subtrees stay intact.
    for node in [*folders.values(), relations]:
        g.ET.indent(node, space='  ', level=len(list(node.iterancestors())))
    g.require(protected(root) == protected(result), 'Navigation changed protected semantic/presentation content')
    g.validate_identifiers(result)
    report = dict(version=VERSION, element_projection_policy='folder-per-containment-participant-v1',
                  standing='PROPOSED_NAVIGATION_NOT_ARCHITECTURE',
                  projection_coverage=dict(generated_elements=len(scope),
                      retained_elements=sorted(elements.keys() - scope),
                      input_folder_ancestry_used=False),
                  element_plan=plan, relationship_plan=relationship_plan,
                  out_of_scope_containment_findings=resolved.findings(set(elements) - scope),
                  containment_policy=containment.VERSION,
                  summary=dict(projected_elements=len(scope), composition_roots=sum(len(p) == 1 and k in child_parents for k, p in paths.items()),
                               contained_elements=sum(len(p) > 1 for p in paths.values()),
                               non_contained_elements=sum(len(p) == 1 and k not in child_parents for k, p in paths.items()),
                               maximum_composition_depth=max((len(p) - 1 for p in paths.values()), default=0),
                               element_context_folders=len(folders),
                               containment_participant_elements=len(participants),
                               contained_leaf_contexts=len(participants - child_parents),
                               relationship_source_contexts=len(sources),
                               relationship_mirror_folders=len(mirrored), relationship_references=len(relationships),
                               projected_without_canonical_identity=sum(not claims['claims'][rid]['present'] for rid in scope)))
    return result, report


def verify(model, named_root, candidate_root, report, anchors=INFORMATION_ANCHORS, resolved=None, placements=None):
    """Check projection against common semantic containment and actual sources."""
    g.require(protected(named_root) == protected(candidate_root), 'Workspace changed protected content')
    elements = index(candidate_root, 'a:elements/a:element')
    edges = index(candidate_root, 'a:relationships/a:relationship')
    homes = locations(candidate_root)
    if placements is None:
        placements = {rid: dict(anchor=anchors[native_type(node)], suffix=())
                      for rid, node in elements.items() if native_type(node) in anchors}
    scope = set(placements)
    resolved = containment.resolve(named_root) if resolved is None else resolved
    paths = resolved.paths(scope, closed=True)
    containing = {path[-2] for path in paths.values() if len(path) > 1}
    participants = containing | {rid for rid, path in paths.items() if len(path) > 1}
    for rid in elements.keys() | edges.keys() | index(candidate_root, 'a:views/a:diagrams/a:view').keys():
        g.require(len(homes[rid]) == 1, 'Expected exactly one navigation reference: ' + rid)
    for rid in scope:
        chain = paths[rid]
        contexts = chain if rid in containing else chain[:-1]
        expected = [*placements[rid]['anchor'],
                    *(elements[key].findtext('a:name', namespaces=g.N) for key in contexts),
                    *placements[rid].get('suffix', ())]
        if rid in participants and rid not in containing:
            expected.append(elements[rid].findtext('a:name', namespaces=g.N))
        g.require(labels(homes[rid][0][0]) == expected, 'Composition-derived element home mismatch: ' + rid)
        g.require(report['element_plan'][rid]['composition_path'] == chain, 'Incorrect ancestry evidence')
        if rid in participants:
            folder = homes[rid][0][0][-1]
            g.require(folder.get('identifier') == report['element_plan'][rid]['context_folder'] and
                      folder.findtext('a:label', namespaces=g.N) == elements[rid].findtext('a:name', namespaces=g.N),
                      'Missing corresponding element context: ' + rid)
    before_homes = locations(named_root)
    for rid in elements.keys() - scope:
        g.require([g.signature(n)[:3] for n in homes[rid][0][0]] ==
                  [g.signature(n)[:3] for n in before_homes[rid][0][0]], 'Unrelated element home changed')
    for rid, edge in edges.items():
        source = edge.get('source')
        source_path = labels(homes[source][0][0])
        if source not in participants:
            source_path.append(elements[source].findtext('a:name', namespaces=g.N))
        g.require(labels(homes[rid][0][0]) == ['Relations', *source_path], 'Relationship home must follow source: ' + rid)
        leaf = homes[rid][0][0][-1]
        g.require(leaf.get('identifier') == report['relationship_plan'][rid]['source_context_folder'], 'Source folder identity mismatch')
        g.require(report['relationship_plan'][rid]['source'] == source, 'Incorrect relationship source evidence')
    old_views = named_root.xpath("a:organizations/a:item[a:label='Views']", namespaces=g.N)
    new_views = candidate_root.xpath("a:organizations/a:item[a:label='Views']", namespaces=g.N)
    g.require([g.signature(n) for n in old_views] == [g.signature(n) for n in new_views], 'Views organisation changed')
    # Exact regeneration also detects extra folders, missing/duplicated branches,
    # projection-ID substitution and reference ordering changes.
    expected, repeated_report = project(model, named_root, anchors, resolved=resolved, placements=placements)
    g.require(g.signature(expected) == g.signature(candidate_root) and repeated_report == report,
              'Workspace projection is not deterministic/exact')
    return dict(composition_navigation='PASS', actual_source_navigation='PASS',
                protected_content='PASS', unique_navigation_references='PASS',
                views_and_view_navigation='UNCHANGED', exact_regeneration='PASS')


def prove_generation(model, named_root, candidate_root, placements, resolved):
    """Counterfactual proof: regenerate from absent and misleading navigation.

    Perturb copies only. The immutable submission is never changed. A complete
    population is required so retained input scaffolding cannot satisfy proof.
    """
    elements = index(named_root, 'a:elements/a:element')
    g.require(set(placements) == elements.keys(), 'Generation proof needs complete declared coverage')
    expected = g.signature(candidate_root.find('a:organizations', g.N))
    cases = []
    for mode in ('ABSENT', 'MISLEADING'):
        altered = copy.deepcopy(named_root)
        org = altered.find('a:organizations', g.N)
        for item in list(org):
            if item.findtext('a:label', namespaces=g.N) != 'Views':
                org.remove(item)
        if mode == 'MISLEADING':
            wrong = g.folder(org, 'adversarial-navigation', 'Deliberately wrong workspace')
            for rid in sorted(elements):
                g.child(wrong, 'item', identifierRef=rid)
        generated, plan = project(model, altered, resolved=resolved, placements=placements)
        g.require(g.signature(generated.find('a:organizations', g.N)) == expected,
                  'Navigation depends on input folders: ' + mode)
        g.require(protected(generated) == protected(named_root), 'Generation proof changed protected content')
        cases.append(dict(input_element_relationship_navigation=mode,
                          generated_element_references=plan['summary']['projected_elements'],
                          regenerated_organisation='EXACT_MATCH', protected_content='UNCHANGED'))
    return dict(status='PASS', immutable_input_modified=False, cases=cases,
                views_sha256=g.digest(g.signature(named_root.find('a:views', g.N))),
                view_navigation_sha256=g.digest([g.signature(n) for n in named_root.xpath(
                    "a:organizations/a:item[a:label='Views']", namespaces=g.N)]))
