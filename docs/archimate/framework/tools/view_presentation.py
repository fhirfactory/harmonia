#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Semantically selected RAD-1.0.9 generated presentation; no architectural assertions.

Selection is pinned separately from generation. Historical replay remains
available with synchronise_baseline.publish(presentation=False).
"""
import argparse
import collections
import copy
import gzip
import hashlib
import json
import math

import generate_views as g
import governed_metadata as m
from architecture_release import load_release

PREVIOUS_EVIDENCE = m.PUBLISHING / '1.0.9/view-presentation-completion'
EVIDENCE = m.PUBLISHING / '1.0.9/containment-scope-completion'
SELECTION = EVIDENCE / 'selection.json'
AUTHORITY = 'docs/markdown/governance/archimate-ingestion-publication-semantics.md#deterministic-containment-view'
CLEARANCE = 24


def views(root):
    return {v.get('identifier'): v for v in root.findall('a:views/a:diagrams/a:view', g.N)}


def rect(node):
    return tuple(int(node.get(k)) for k in ('x', 'y', 'w', 'h'))


def centre(box):
    x, y, w, h = box
    return (x + w // 2, y + h // 2)


def bends(connection):
    return [tuple(int(p.get(k)) for k in ('x', 'y')) for p in connection.findall('a:bendpoint', g.N)]


def set_bends(connection, points):
    """Do not touch a connection, including its whitespace, if its route agrees."""
    if bends(connection) == points:
        return False
    old = connection.findall('a:bendpoint', g.N)
    offset = list(connection).index(old[0]) if old else len(connection)
    for point in old:
        connection.remove(point)
    for i, (x, y) in enumerate(points):
        point = g.ET.Element(g.tag('bendpoint'), x=str(x), y=str(y))
        connection.insert(offset + i, point)
    return True


def simplify(points):
    result = []
    for point in points:
        if result and point == result[-1]:
            continue
        while len(result) > 1:
            a, z = result[-2:]
            if (a[0] == z[0] == point[0] and (z[1]-a[1])*(point[1]-z[1]) >= 0 or
                    a[1] == z[1] == point[1] and (z[0]-a[0])*(point[0]-z[0]) >= 0):
                result.pop()
            else:
                break
        result.append(point)
    return result


def intersects(a, z, box):
    """Open rectangle intersection, including diagonal straight connections."""
    x, y, w, h = box
    low, high = 0., 1.
    for start, delta, lower, upper in ((a[0], z[0]-a[0], x, x+w),
                                       (a[1], z[1]-a[1], y, y+h)):
        if delta == 0:
            if not lower < start < upper:
                return False
        else:
            ends = sorted(((lower-start)/delta, (upper-start)/delta))
            low, high = max(low, ends[0]), min(high, ends[1])
    return low < high - 1e-9


def boundary(box, toward):
    cx, cy = box[0] + box[2]/2, box[1] + box[3]/2
    dx, dy = toward[0]-cx, toward[1]-cy
    g.require(dx or dy, 'Indeterminate connection attachment')
    scale = min(box[2]/(2*abs(dx)) if dx else math.inf,
                box[3]/(2*abs(dy)) if dy else math.inf)
    return (cx + scale*dx, cy + scale*dy)


def route_points(connection, nodes):
    a, z = [rect(nodes[connection.get(k)]) for k in ('source', 'target')]
    points = bends(connection)
    return [boundary(a, points[0] if points else centre(z)), *points,
            boundary(z, points[-1] if points else centre(a))]


def clear(points, boxes):
    return all(not intersects(a, z, box) for a, z in zip(points, points[1:]) for box in boxes)


def ports(box):
    x, y, w, h = box
    cx, cy = centre(box)
    return [((x, cy), (-1, 0)), ((x+w, cy), (1, 0)),
            ((cx, y), (0, -1)), ((cx, y+h), (0, 1))]


def business_route(connection, nodes):
    """Straight when clear, otherwise a deterministic shortest clear dogleg.

    Candidate lanes come from actual rectangle boundaries and clearance. An
    unsupported geometry fails explicitly instead of publishing an obstruction.
    """
    source, target = (connection.get(k) for k in ('source', 'target'))
    a, z = rect(nodes[source]), rect(nodes[target])
    unrelated = [rect(n) for key, n in nodes.items() if key not in (source, target)]
    if clear([boundary(a, centre(z)), boundary(z, centre(a))], unrelated):
        return []
    boxes = [rect(n) for n in nodes.values()]
    xs = sorted({value for x, y, w, h in boxes for value in (x-CLEARANCE, x+w+CLEARANCE)})
    ys = sorted({value for x, y, w, h in boxes for value in (y-CLEARANCE, y+h+CLEARANCE)})
    candidates = []
    for p, outward in ports(a):
        for q, inward in ports(z):
            paths = [[p, (x, p[1]), (x, q[1]), q] for x in xs]
            paths += [[p, (p[0], y), (q[0], y), q] for y in ys]
            for path in paths:
                path = simplify(path)
                if len(path) < 2:
                    continue
                if sum((path[1][i]-p[i])*outward[i] for i in (0, 1)) <= 0:
                    continue
                if sum((path[-2][i]-q[i])*inward[i] for i in (0, 1)) <= 0:
                    continue
                if clear(path, boxes):
                    length = sum(abs(v[0]-u[0])+abs(v[1]-u[1]) for u, v in zip(path, path[1:]))
                    candidates.append((length + 40*(len(path)-2), len(path), path))
    g.require(candidates, 'Unsupported Business routing geometry: ' + connection.get('identifier'))
    return min(candidates)[2][1:-1]


def containment_candidates(b, root, protected):
    """Project declared immediate-child purposes from accepted native semantics.

    The landscape registry declares a source/purpose, not membership authority.
    Its legacy children, roles, paths and existing visual objects are not inputs.
    Currentness comes from governed relationship records or accepted ingestion;
    neither ownership nor a diagram can establish Composition.
    """
    index = b.home_context_index
    accepted = b.reconciliation
    relationship_ids = {row['identifier'] for row in b.crosswalk['relationships']
                        if row['record_state'] == 'current'}
    relationship_ids.update(row['representationId'] for row in accepted['relationships'])
    element_ids = {key for key, item in index.items() if item['record'].get('record_state') == 'current'}
    element_ids.update(row['representationId'] for row in accepted['elements'])
    elements = {e.get('identifier'): e for e in root.findall('a:elements/a:element', g.N)}
    edges = root.findall('a:relationships/a:relationship', g.N)
    eligible, oversized = {}, []
    for vid, declaration in b.landscapes.items():
        context = declaration['context']
        if context is None:
            continue  # A root landscape has no containing/source Capability.
        source = b.ids[context]
        if source not in element_ids or elements[source].get(g.TYPE) != 'Capability':
            continue
        direct = {e.get('identifier'): e.get('target') for e in edges
                  if e.get('identifier') in relationship_ids and e.get(g.TYPE) == 'Composition'
                  and e.get('source') == source and e.get('target') in element_ids
                  and elements[e.get('target')].get(g.TYPE) == 'Capability'}
        if not direct or vid in protected:
            continue
        g.require(len(direct) == len(set(direct.values())), 'Duplicate direct Composition child: ' + vid)
        if len(direct) > 20:
            oversized.append(dict(view=vid, children=len(direct)))
            continue
        eligible[vid] = dict(source=source, purpose='source and immediate Capability children',
            standing_filter='current governed native relationships and accepted ingestion; current or accepted endpoints',
            children=sorted(direct.values()), relationships=sorted(direct))
    return eligible, oversized


def selection(b, root):
    import reconcile_108 as x
    protected = sorted(row['representationId'] for row in x.read()['views'])
    previous = m.read(PREVIOUS_EVIDENCE / 'selection.json')
    containment, oversized = containment_candidates(b, root, protected)
    # Recorded historical/diagnostic exclusions remain exclusions. Include the
    # third oversized generated declaration even though it has no visual edges.
    oversized = {row['view']: row for row in [*previous['oversized'], *oversized]}
    result = dict(authority=AUTHORITY, architecture_release=load_release(),
                  containment=containment, historical_candidates=previous['historical_candidates'],
                  oversized=sorted(oversized.values(), key=lambda row: row['view']),
                  protected_authored=protected, business={}, known_stale_connections=[],
                  uncertain_diagnostics=['id-ef8af9b152505720b37914080c957a1c'])
    for vid, view in views(root).items():
        connections = view.findall('a:connection', g.N)
        if vid in b.layout_groups:
            result['business'][vid] = [c.get('identifier') for c in connections]
            result['known_stale_connections'] += [c.get('identifier') for c in connections if bends(c)]
    for key in ('historical_candidates', 'known_stale_connections'):
        result[key] = sorted(result[key], key=str)
    g.require(not set(protected) & (result['containment'].keys() | result['business'].keys()),
              'Authored presentation entered generated selection')
    g.require((len(result['containment']), len(result['historical_candidates']), len(result['oversized']),
               len(protected), len(result['business']), len(result['known_stale_connections'])) == (231, 62, 3, 4, 61, 29),
              'Authorised presentation population drift')
    census = m.read(EVIDENCE / 'semantic-census-before.json')
    g.require(set(containment) == set(census['eligible']) and all(
        all(row[key] == census['eligible'][vid][key] for key in ('source', 'children', 'relationships'))
        for vid, row in containment.items()), 'Investigated semantic population drift')
    return result


def project_membership(root, view, spec):
    """Create only visual references; native architecture must already exist."""
    elements = {e.get('identifier'): e for e in root.findall('a:elements/a:element', g.N)}
    edges = {e.get('identifier'): e for e in root.findall('a:relationships/a:relationship', g.N)}
    expected = {spec['source'], *spec['children']}
    nodes = {n.get('elementRef'): n for n in view.findall('a:node', g.N)}
    g.require(len(nodes) == len(view.findall('a:node', g.N)) and set(nodes) <= expected,
              'Unexpected/duplicate generated Containment membership')
    g.require(expected <= elements.keys(), 'Projection cannot create semantic elements')
    g.require(set(spec['relationships']) <= edges.keys(), 'Projection cannot create semantic relationships')
    g.require({edges[rid].get('target') for rid in spec['relationships']} == set(spec['children']) and
              all(edges[rid].get(g.TYPE) == 'Composition' and edges[rid].get('source') == spec['source']
                  for rid in spec['relationships']), 'Projection is not accepted direct Composition')
    vid = view.get('identifier')
    for semantic in sorted(expected - nodes.keys()):
        node = g.ET.Element(g.tag('node'), identifier=g.visual_id(root.get('identifier'), 'synchronised-baseline-v1',
                            'node', vid, semantic), elementRef=semantic, x='40', y='40', w='360', h='148')
        node.set(g.TYPE, 'Element')
        # Reuse semantic presentation styling, without borrowing another View's
        # node identity or treating its membership as architectural authority.
        style = elements[semantic].find('a:style', g.N)
        if style is not None:
            node.append(copy.deepcopy(style))
        else:
            g.style(node, (255, 255, 181))
        first = view.find('a:connection', g.N)
        view.insert(list(view).index(first) if first is not None else len(view), node)
        nodes[semantic] = node
    connections = {c.get('relationshipRef'): c for c in view.findall('a:connection', g.N)}
    g.require(len(connections) == len(view.findall('a:connection', g.N)) and
              set(connections) <= set(spec['relationships']), 'Unexpected generated Containment connection')
    for rid in spec['relationships']:
        source = nodes[spec['source']].get('identifier')
        target = nodes[edges[rid].get('target')].get('identifier')
        connection = connections.get(rid)
        if connection is None:
            connection = g.child(view, 'connection', identifier=g.visual_id(root.get('identifier'),
                'synchronised-baseline-v1', 'connection', vid, rid), relationshipRef=rid, source=source, target=target)
            connection.set(g.TYPE, 'Relationship')
            g.style(connection)
        g.require(connection.get('source') == source and connection.get('target') == target,
                  'Visual endpoints disagree with native Composition')


def containment_layout(view):
    nodes = {n.get('identifier'): n for n in view.findall('a:node', g.N)}
    connections = view.findall('a:connection', g.N)
    source = nodes[connections[0].get('source')]
    children = sorted((nodes[c.get('target')] for c in connections), key=lambda n: n.get('elementRef'))
    count = len(children)
    g.require(1 <= count <= 20, 'Unsupported Containment child count')
    first = count if count <= 10 else (count+2)//2
    left, right = children[:first], children[first:]
    source.set('x', '40')
    child_x = 40 + int(source.get('w')) + 160
    y = 40
    for i, node in enumerate(left):
        node.set('x', str(child_x)); node.set('y', str(y))
        # Each first-column gap fits the second-column node and clearance.
        y += int(node.get('h')) + (int(right[i].get('h'))+2*CLEARANCE if i < len(right) else 2*CLEARANCE)
    second_x = child_x + max(int(n.get('w')) for n in left) + 160
    for i, node in enumerate(right):
        gap_top = int(left[i].get('y')) + int(left[i].get('h'))
        node.set('x', str(second_x)); node.set('y', str(gap_top+ CLEARANCE))
    middle = (centre(rect(left[0]))[1] + centre(rect(left[-1]))[1])//2
    source.set('y', str(middle-int(source.get('h'))//2))
    trunk = child_x - 80
    for connection in connections:
        target = nodes[connection.get('target')]
        cy = centre(rect(target))[1]
        start, end = (40+int(source.get('w')), middle), (int(target.get('x')), cy)
        points = simplify([start, (trunk, middle), (trunk, cy), end])
        set_bends(connection, points[1:-1])


def apply(root, scope):
    all_views = views(root)
    for vid, spec in scope['containment'].items():
        project_membership(root, all_views[vid], spec)
        containment_layout(all_views[vid])
    for vid in scope['business']:
        view = all_views[vid]
        nodes = {n.get('identifier'): n for n in view.findall('a:node', g.N)}
        for connection in view.findall('a:connection', g.N):
            set_bends(connection, business_route(connection, nodes))


def validate_geometry(view, containment=False):
    nodes = {n.get('identifier'): n for n in view.findall('a:node', g.N)}
    boxes = [rect(n) for n in nodes.values()]
    for i, (x, y, w, h) in enumerate(boxes):
        g.require(w > 0 and h > 0 and all(x+w <= a or a+c <= x or y+h <= z or z+d <= y
                  for a, z, c, d in boxes[i+1:]), 'Overlapping presentation nodes')
    for connection in view.findall('a:connection', g.N):
        points = route_points(connection, nodes)
        unrelated = [rect(n) for key, n in nodes.items() if key not in (connection.get('source'), connection.get('target'))]
        g.require(clear(points, unrelated), 'Connection crosses unrelated node: ' + connection.get('identifier'))
        # Include own endpoints: a route must leave/meet their boundary, never
        # traverse their interiors before reaching the other endpoint.
        g.require(clear(points, [rect(nodes[connection.get(k)]) for k in ('source', 'target')]),
                  'Connection crosses its own endpoint interior')
    if containment:
        connections = view.findall('a:connection', g.N)
        source = nodes[connections[0].get('source')]
        children = [nodes[c.get('target')] for c in connections]
        columns = sorted({int(n.get('x')) for n in children})
        g.require(len(columns) == (1 if len(children) <= 10 else 2), 'Wrong Containment column count')
        g.require(int(source.get('x'))+int(source.get('w')) < columns[0], 'Containment source must be on left')
        if len(columns) == 2:
            left = sorted((rect(n) for n in children if int(n.get('x')) == columns[0]), key=lambda r: r[1])
            right = [rect(n) for n in children if int(n.get('x')) == columns[1]]
            g.require(all(any(a[1]+a[3] < centre(z)[1] < c[1] for a, c in zip(left, left[1:])) for z in right),
                      'Second column is not offset into first-column corridors')


def audit(before, after, scope):
    """Constrain projection additions, selected coordinates and changed routes."""
    old, new = views(before), views(after)
    stripped = []
    for root in (before, after):
        value = copy.deepcopy(root)
        for element in value.findall('a:views', g.N):
            value.remove(element)
        stripped.append(g.signature(value))
    g.require(stripped[0] == stripped[1], 'Presentation changed semantics, metadata, identity or navigation')
    g.require(old.keys() == new.keys(), 'Presentation changed View population')
    changes, changed_connections, added_nodes, added_connections = [], [], [], []
    selected = set(scope['containment']) | set(scope['business'])
    for vid, original in old.items():
        actual = new[vid]
        if vid not in selected:
            g.require(g.ET.tostring(original) == g.ET.tostring(actual), 'Unselected/protected View XML changed: ' + vid)
            continue
        baseline = copy.deepcopy(original)
        if vid in scope['containment']:
            project_membership(before, baseline, scope['containment'][vid])
            spec = scope['containment'][vid]
            g.require({n.get('elementRef') for n in actual.findall('a:node', g.N)} ==
                      {spec['source'], *spec['children']} and
                      {c.get('relationshipRef') for c in actual.findall('a:connection', g.N)} == set(spec['relationships']),
                      'Incomplete direct-child projection: ' + vid)
        expected = copy.deepcopy(actual)
        for tag in ('node', 'connection'):
            left = {n.get('identifier'): n for n in baseline.findall('a:'+tag, g.N)}
            right = {n.get('identifier'): n for n in expected.findall('a:'+tag, g.N)}
            existing = {n.get('identifier') for n in original.findall('a:'+tag, g.N)}
            g.require(left.keys() == right.keys(), 'Presentation visual identities changed')
            for key, previous in left.items():
                revised = right[key]
                created = key not in existing
                if created:
                    (added_nodes if tag == 'node' else added_connections).append(dict(view=vid, identifier=key,
                        semantic=previous.get('elementRef') if tag == 'node' else previous.get('relationshipRef')))
                if tag == 'node' and vid in scope['containment']:
                    for attr in ('x', 'y'):
                        revised.set(attr, previous.get(attr))
                if tag == 'connection':
                    if created or bends(revised) != bends(previous):
                        changed_connections.append(dict(view=vid, connection=key,
                            created=created,
                            known_stale=key in scope['known_stale_connections'],
                            before=bends(previous), after=bends(revised)))
                        for point in revised.findall('a:bendpoint', g.N):
                            revised.remove(point)
                        for i, point in enumerate(previous):
                            if point.tag == g.tag('bendpoint'):
                                revised.insert(i, copy.deepcopy(point))
                    else:
                        g.require(g.ET.tostring(previous) == g.ET.tostring(revised), 'Unchanged connection XML churn: '+key)
        g.require(g.signature(expected) == g.signature(baseline), 'Unauthorised View content change: '+vid)
        validate_geometry(actual, vid in scope['containment'])
        if g.ET.tostring(original) != g.ET.tostring(actual):
            changes.append(vid)
    return dict(status='PASS', containment_views_validated=len(scope['containment']),
        business_views_validated=len(scope['business']),
        containment_views_changed=len(set(changes) & set(scope['containment'])),
        business_views_changed=len(set(changes) & set(scope['business'])),
        connections_changed=len(changed_connections),
        visual_nodes_created=added_nodes, visual_connections_created=added_connections,
        business_connections_changed=sum(c['view'] in scope['business'] for c in changed_connections),
        known_stale_connections_validated=len(scope['known_stale_connections']),
        known_stale_connections_changed=sum(c['known_stale'] for c in changed_connections),
        changed_views=changes, connection_changes=changed_connections,
        semantic_identity_metadata_and_navigation='EXACT', unselected_view_xml='BYTE_IDENTICAL',
        unchanged_connection_xml='BYTE_IDENTICAL', protected_authored_views=len(scope['protected_authored']),
        historical_candidates_preserved=len(scope['historical_candidates']), oversized_preserved=scope['oversized'])


def before_bytes(relative):
    row = m.read(EVIDENCE / 'before.json')[relative]
    value = gzip.decompress((m.ROOT / row['snapshot']).read_bytes()) if 'snapshot' in row else (m.ROOT / relative).read_bytes()
    g.require(hashlib.sha256(value).hexdigest() == row['sha256'], 'Presentation before evidence changed: '+relative)
    return value


def publish(outputs, b, report):
    scope = m.read(SELECTION)
    before = g.ET.fromstring(outputs[m.model_path()])
    g.require(scope == selection(b, before), 'Presentation selection drift')
    root = copy.deepcopy(before)
    apply(root, scope)
    check = audit(before, root, scope)
    b.view_presentation_before = before
    b.view_presentation = check
    b.reconciliation_root = copy.deepcopy(root)
    b.presentation_scope = scope
    outputs = dict(outputs)
    outputs[m.model_path()] = g.serialize(root)
    report = copy.deepcopy(report)
    report['view_presentation'] = {k: v for k, v in check.items() if k not in ('connection_changes', 'changed_views')}
    report['output_sha256'] = hashlib.sha256(outputs[m.model_path()]).hexdigest()
    report['roundtrip_status'] = 'See containment-scope-completion/verification.json; Solution Architect rendering inspection pending'
    report['checks']['generated_view_presentation_semantic_and_geometry_conformance'] = 'PASS'
    outputs[m.validation_path()] = m.encoded(report)
    return outputs, b, report


def render(report_path, guide_path):
    """Current handoff uses current evidence; earlier handoffs stay snapshotted."""
    import synchronise_baseline as s
    evidence = m.read(EVIDENCE / 'verification.json')
    required = {'archimate_tests', 'governance_tests', 'architecture_tests', 'publication', 'identity',
                'rads', 'release', 'preservation', 'serialization_roundtrip', 'archi_transport', 'history'}
    g.require(evidence['status'] == 'PASS' and evidence['architecture_release'] == load_release() and
              required <= evidence['checks'].keys(), 'Incomplete presentation verification')
    for name, check in evidence['checks'].items():
        g.require(check['status'] == 'PASS' and hashlib.sha256((m.ROOT / check['log']).read_bytes()).hexdigest() ==
                  check['log_sha256'], 'Incomplete/changed presentation evidence: '+name)
    outputs, b, result = s.publish()
    g.require(evidence['published_sha256'] == result['output_sha256'] == hashlib.sha256(m.model_path().read_bytes()).hexdigest(),
              'Stale View-presentation verification')
    generated_report = copy.deepcopy(result)
    generated_report['checks']['deterministic_regeneration'] = 'PASS'
    g.require(m.read(m.validation_path()) == generated_report, 'Stale presentation validation report')
    for path, content in outputs.items():
        if path != m.validation_path():
            g.require(path.read_bytes() == content, 'Stale presentation publication output: '+str(path))
    check = b.view_presentation
    lines = ['<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->', '',
        '# RAD-1.0.9 — reconciled candidate with generated View presentation', '',
        'The unsigned candidate incorporates all 32 accepted ingestion CRs and the completed',
        'Home-derived naming/navigation work. This subsequent task changes generated View',
        'presentation only. Final Solution Architect import/rendering inspection is pending.', '',
        f"Model: `{m.model_path().relative_to(m.ROOT)}`. SHA-256: `{result['output_sha256']}`.", '',
        '| Measurement | Result |', '| --- | ---: |',
        '| Elements / native relationships / Views | 1,308 / 1,077 / 1,198 |',
        f"| Containment Views migrated / validated | {check['containment_views_changed']} / {check['containment_views_validated']} |",
        '| Additional Containment Views changed in this correction | 112 |',
        '| Additional Business hierarchy Views: BC / BEC | 55 / 56 |',
        '| Strategy Person Identity Feature landscape | Six accepted children |',
        '| Visual nodes / Composition references added | 1 / 331 |',
        f"| Business Behaviour Views validated / changed | 61 / {check['business_views_changed']} |",
        f"| Business connections changed / validated | {check['business_connections_changed']} / 136 |",
        f"| Known stale connections changed / validated | {check['known_stale_connections_changed']} / 29 |",
        '| Protected authored presentations | 4, exact |',
        '| Historical candidates / 21-child Views preserved | 62 / 3 |', '',
        'Semantic records, identities, naming, Home/responsibility assertions, native endpoints,',
        'navigation and existing visual identities are unchanged. Direct-child membership',
        'and missing visual Composition references follow accepted semantics. Unselected Views',
        'and unchanged connections retain byte-identical XML. The uncertain diagnostic and',
        'unrelated presentation debt remain intact. Generation retains RAD-1.0.9, predecessor 1.0.8.', '',
        'The earlier 119 Containment Views and corrected Business routes remain identical',
        'to the starting candidate. All 61 Behaviour Views are recalculated: complete',
        'regeneration corrects the same 29 known stale routes across 19 Views; this',
        'scope correction introduces no further Business route changes. The protected',
        'Business Person Identity Feature landscape is exact, with its six children.', '',
        'Containment uses the [governed archetype](../../../../markdown/governance/archimate-ingestion-publication-semantics.md#deterministic-containment-view).',
        'Children use stable semantic-ID ordering and their actual rectangle dimensions.',
        'Second-column routes cross first-column gaps with clearance. Native Composition',
        'retains its source-side diamond. Business routing is recalculated from final',
        'rectangles and changes XML only when the deterministic route differs.', '',
        'Separate [presentation evidence](containment-scope-completion/README.md),',
        '[selection](containment-scope-completion/selection.json),',
        '[verification](containment-scope-completion/verification.json) and',
        '[inspection guide](archi-inspection-guide.md) define the current handoff.',
        'Earlier reconciliation and Home-completion evidence remains preserved at its',
        'original scope, with byte-recoverable prior publication snapshots.', '',
        'Archi 5.10 no-edit import/export completed within the recorded scope.',
        'Raw whole-document comparison remains DIFFERENCES; the 1,669 historical',
        'semantic-text findings remain identical. Whole-model lossless fidelity and',
        'human visual acceptance are not asserted.', '']
    guide = ['<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->', '',
        '# Solution Architect inspection — unsigned RAD-1.0.9', '',
        f"Import `{m.model_path().relative_to(m.ROOT)}` into Archi 5.10 as a fresh complete model.",
        f"Expected SHA-256: `{result['output_sha256']}`.", '',
        'Inspect Containment source-left orientation and the source-side diamond. Check',
        'one-column and two-column examples, second-column offsets and unobstructed routes.',
        'Inspect Business Feature columns and the recalculated routes. Confirm the four',
        'protected authored presentations retain their geometry, styling and relationships.',
        'Inspect the 55 BC and 56 BEC Business hierarchy Views and the Strategy Person',
        'Identity Feature landscape (`id-c796d1c0d4c158d08f8bd86308f2f0a0`), which now',
        'includes Identifier Capture among all six children. Preserve the authored Business equivalent.',
        'The three 21-child Views, 62 historical candidates and uncertain diagnostic are preserved.', '',
        'Representative Containment Views (name / View ID / selected children):', '']
    selected = b.presentation_scope['containment']
    current_views = views(b.reconciliation_root)
    counts = {len(row['children']) for row in selected.values()}
    examples = {min(counts), max(counts)}
    if any(n <= 10 for n in counts):
        examples.add(max(n for n in counts if n <= 10))
    if any(n > 10 for n in counts):
        examples.add(min(n for n in counts if n > 10))
    for count in sorted(examples):
        vid = next(vid for vid, row in selected.items() if len(row['children']) == count)
        guide.append(f"- {current_views[vid].findtext('a:name', namespaces=g.N)} / `{vid}` / {count}")
    guide += ['', 'Protected authored Views:', '']
    for vid in b.presentation_scope['protected_authored']:
        guide.append(f"- {current_views[vid].findtext('a:name', namespaces=g.N)} / `{vid}`")
    guide += ['', 'The complete View and per-connection changes are in',
        '[the separate presentation record](containment-scope-completion/README.md).',
        'The recorded CLI probe establishes transport and geometry preservation; human',
        'rendering acceptance remains **PENDING SOLUTION ARCHITECT INSPECTION**.',
        'No Information/Application Architecture expansion or >20-child adjudication is included.', '']
    return {report_path: ('\n'.join(lines)).encode(), guide_path: ('\n'.join(guide)).encode()}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('command', choices=('establish', 'audit'))
    args = parser.parse_args()
    import synchronise_baseline as s
    outputs, b, _ = s.publish(presentation=False)
    original = g.ET.fromstring(outputs[m.model_path()])
    if args.command == 'establish':
        g.require(not SELECTION.exists(), 'Presentation selection already established')
        SELECTION.write_bytes(m.encoded(selection(b, original)))
        print('Established exact 231 / 61 presentation populations; four authored Views protected')
    else:
        scope = m.read(SELECTION)
        g.require(selection(b, original) == scope, 'Selection no longer reproduces')
        current = g.parse(m.model_path())
        result = audit(original, current, scope)
        for relative in m.read(EVIDENCE / 'before.json'):
            before_bytes(relative)
        print(m.encoded(result).decode(), end='')


if __name__ == '__main__':
    main()
