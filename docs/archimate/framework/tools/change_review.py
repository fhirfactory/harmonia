# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only Change Review observation/interpretation; no disposition or execution.

An explicit no-edit control supplies per-record transport evidence, not global
normalisation rules. All native fields and derived claims remain observable.
"""
from collections import Counter, defaultdict

import generate_views as g
from element_correlation import canonical_index, correlate


def native_type(node):
    value = node.get(g.TYPE, '')
    if ':' in value:
        prefix, name = value.split(':', 1)
        return name if node.nsmap.get(prefix) == g.NS else value
    return value


def text_values(node, name):
    return [dict(text=c.text or '', attributes=dict(sorted(c.attrib.items())))
            for c in node.findall('a:' + name, g.N)]


def signature(node, model):
    """Exact content except XML indentation, prefixes and resolved Property IDs."""
    attrs = dict(node.attrib)
    if g.TYPE in attrs:
        attrs[g.TYPE] = native_type(node)
    if 'propertyDefinitionRef' in attrs:
        ref = attrs['propertyDefinitionRef']
        attrs['propertyDefinitionRef'] = model.keys.get(ref, 'UNRESOLVED:' + ref)
    text = node.text if not len(node) or (node.text or '').strip() else None
    return [node.tag, attrs, text, [signature(c, model) for c in node if isinstance(c.tag, str)]]


def provenance(model, rid=None):
    path = model.path
    result = dict(path=str(path), sha256=model.sha256, xml_sha256=model.xml_sha256)
    if rid is not None:
        node = model.elements[rid]
        result.update(representation_id=rid, line=node.sourceline)
    return result


def navigation(model):
    result = defaultdict(list)
    def walk(parent, path=()):
        for item in parent.findall('a:item', g.N):
            labels = tuple(c.text or '' for c in item.findall('a:label', g.N))
            current = (*path, *labels)
            if item.get('identifierRef'):
                result[item.get('identifierRef')].append(list(current))
            walk(item, current)
    parent = model.root.find('a:organizations', g.N)
    if parent is not None:
        walk(parent)
    return {rid: sorted(paths) for rid, paths in result.items()}


def specialization(model, node):
    properties = [p for key in ('Specialization', 'specialization') for p in model.claims(node, key)]
    texts = [v.text or '' for p in properties for v in p.findall('a:value', g.N)]
    return dict(claims=[signature(p, model) for p in properties], values=texts,
                single=texts[0] if len(properties) == len(texts) == 1 else None)


def authored_element(model, rid):
    node = model.elements[rid]
    spec = specialization(model, node)
    names = text_values(node, 'name')
    topic = native_type(node) == 'DataObject' and spec['single'] == 'Topic'
    properties = defaultdict(list)
    for prop in node.findall('a:properties/a:property', g.N):
        ref = prop.get('propertyDefinitionRef')
        properties[model.keys.get(ref, 'UNRESOLVED:' + str(ref))].append(signature(prop, model))
    return dict(native_type=native_type(node), names=names, documentation=text_values(node, 'documentation'),
                specialization=spec, topic=topic,
                topic_display_convention=len(names) == 1 and names[0]['text'].startswith('[Topic] '),
                properties=dict(sorted(properties.items())),
                attributes={k: v for k, v in node.attrib.items() if k not in ('identifier', g.TYPE)},
                residual=[signature(c, model) for c in node if c.tag not in
                          {g.tag(k) for k in ('name', 'documentation', 'properties')}])


def snapshot(model):
    index = canonical_index(model)
    def identity(rid):
        claim = index['claims'].get(rid, {})
        value = claim.get('canonicalId')
        return 'canonical:' + value if value in index['unique'] else 'representation:' + str(rid)
    parents, edges = defaultdict(list), []
    elements = {rid: authored_element(model, rid) for rid in sorted(model.elements)}
    for rid, node in sorted(model.relationships.items()):
        if native_type(node) == 'Composition':
            source, target = node.get('source'), node.get('target')
            edge = dict(relationship_id=rid, source=source, target=target,
                        source_identity=identity(source), target_identity=identity(target), line=node.sourceline)
            edges.append(edge)
            parents[target].append({k: edge[k] for k in ('relationship_id', 'source_identity')})
    homes = navigation(model)
    for rid, row in elements.items():
        row['navigation'] = homes.get(rid, [])
        row['composition_context'] = parents[rid]
    return dict(elements=elements, composition=edges, identity=identity)


def metadata_findings(model, rid, current, previous=None):
    node = model.elements[rid]
    result = []
    expected = dict(archimateType=current['native_type'])
    if len(current['names']) == 1:
        expected['displayName'] = current['names'][0]['text']
    if current['topic']:
        expected['harmoniaType'] = 'Topic'
    for key, value in expected.items():
        texts = model.texts(node, key)
        if not model.claims(node, key):
            if key == 'harmoniaType':
                result.append(dict(property=key, finding='MISSING_DERIVED_METADATA', authored=value))
        elif model.single(node, key) != value:
            result.append(dict(property=key, finding='STALE_OR_CONFLICTING_DERIVED_METADATA',
                               values=texts, authored=value))
    if previous and any(current[k] != previous[k] for k in
                        ('native_type', 'names', 'specialization', 'composition_context')):
        for key in ('friendlyName', 'canonicalName'):
            if model.claims(node, key):
                result.append(dict(property=key, finding='DERIVATION_REVIEW_REQUIRED',
                                   values=model.texts(node, key),
                                   reason='Authored naming inputs changed; no names derived or repaired'))
    return result


def topic_graph(state):
    topics = {rid for rid, e in state['elements'].items() if e['topic']}
    edges = [e for e in state['composition'] if e['source'] in topics and e['target'] in topics]
    pairs = Counter((e['source'], e['target']) for e in edges)
    parents, children = defaultdict(set), defaultdict(set)
    for source, target in pairs:
        parents[target].add(source)
        children[source].add(target)
    active, visited, cycles = set(), set(), []
    def visit(rid, path=()):
        if rid in active:
            cycles.append([*path[path.index(rid):], rid])
            return
        if rid in visited:
            return
        active.add(rid)
        for child in sorted(children[rid]):
            visit(child, (*path, rid))
        active.remove(rid)
        visited.add(rid)
    for rid in sorted(topics):
        visit(rid)
    return dict(nodes=sorted(topics), composition_edges=edges, cycles=cycles,
                duplicate_endpoint_pairs=[dict(source=s, target=t, count=count)
                                          for (s, t), count in sorted(pairs.items()) if count > 1],
                competing_parents={rid: sorted(ps) for rid, ps in sorted(parents.items()) if len(ps) > 1})


def review(before, after, control=None):
    correlation = correlate(before, after)
    old, new = snapshot(before), snapshot(after)
    transport = snapshot(control) if control is not None else None
    control_correlation = correlate(before, control) if control is not None else None
    control_matches = {row['baseline_id']: row['incoming_id'] for row in
                       control_correlation['matches']} if control_correlation else {}
    issues = list(correlation['issues'])
    if control is not None and (control_correlation['status'] != 'CORRELATED' or
                                control_correlation['new'] or control_correlation['deleted']):
        issues.append(dict(kind='CONTROL_IDENTITY_NOT_EQUIVALENT', evidence=control_correlation))
    records, unchanged, rebindings = [], [], []
    for match in correlation['matches']:
        a, b = match['baseline_id'], match['incoming_id']
        prior, current = old['elements'][a], new['elements'][b]
        reference = transport['elements'][control_matches[a]] if a in control_matches else prior
        changes, transport_differences = {}, {}
        for key in prior:
            if prior[key] != reference[key]:
                transport_differences[key] = dict(baseline=prior[key], control=reference[key])
            if current[key] != reference[key]:
                changes[key] = dict(baseline=prior[key], incoming=current[key],
                                    **({'control': reference[key]} if transport is not None else {}))
        evidence = dict(**match, baseline=provenance(before, a), incoming=provenance(after, b))
        if match['representation_identity'] == 'REBOUND':
            rebindings.append(evidence)
        if changes or match['representation_identity'] == 'REBOUND':
            records.append(dict(**evidence, changes=changes, transport_differences=transport_differences,
                                authored=current, stale_metadata=metadata_findings(after, b, current, reference)))
        else:
            unchanged.append(b)
    additions = [dict(incoming=provenance(after, rid), authored=new['elements'][rid],
                      stale_metadata=metadata_findings(after, rid, new['elements'][rid]))
                 for rid in correlation['new']]
    for rid, element in new['elements'].items():
        spec = element['specialization']
        if ((spec['claims'] and spec['single'] is None) or
                (spec['single'] == 'Topic' and element['native_type'] != 'DataObject') or
                (element['topic_display_convention'] and not element['topic'])):
            issues.append(dict(kind='UNRESOLVED_TOPIC_CLASSIFICATION', representation_id=rid,
                               native_type=element['native_type'], specialization=spec))
    graph = topic_graph(new)
    if graph['cycles'] or graph['duplicate_endpoint_pairs'] or graph['competing_parents']:
        issues.append(dict(kind='UNRESOLVED_TOPIC_COMPOSITION_GRAPH'))
    return dict(status='UNRESOLVED' if issues or correlation['status'] == 'UNRESOLVED' or
                (control is not None and control.issues) else 'DETECTED',
                lifecycle=dict(observation='RECORDED', interpretation='CANDIDATE', disposition='NOT_RECORDED',
                               outcome='Unresolved', execution='NOT_AUTHORISED'),
                inputs=dict(baseline=provenance(before), incoming=provenance(after),
                            control=provenance(control) if control is not None else None),
                input_issues=dict(baseline=before.issues, incoming=after.issues,
                                  control=control.issues if control is not None else []),
                population=dict(evolved=len(records), new=len(additions), deleted=len(correlation['deleted']),
                                unchanged=len(unchanged), canonical_matches=sum(m['basis'] == 'CANONICAL_ID' for m in correlation['matches']),
                                representation_only_matches=sum(m['basis'] == 'REPRESENTATION_ID_ONLY' for m in correlation['matches']),
                                representation_rebindings=len(rebindings),
                                unresolved_baseline=len(correlation['unresolved']['baseline']),
                                unresolved_incoming=len(correlation['unresolved']['incoming'])),
                correlation=correlation, evolved=records, new=additions,
                deleted=[dict(baseline=provenance(before, rid)) for rid in correlation['deleted']],
                unchanged=unchanged, representation_rebindings=rebindings,
                topic_graph=graph, issues=issues,
                transport=dict(method='Exact field comparison with explicit no-edit control' if control is not None else
                               'NO CONTROL: differences are observations; transport origin is not established',
                               control_correlation=control_correlation,
                               raw_findings='Retained separately by the exhaustive representation-fidelity comparator'),
                limitations=['Snapshot continuity does not prove lifetime non-reuse or architect intent.',
                             'Topic recognition and explicit Composition are submitted semantics, not acceptance.',
                             'Derived names are flagged for review; no metadata is regenerated.',
                             'No governed source, identity binding or architecture representation is updated.'])
