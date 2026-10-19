#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Replay the explicitly accepted 1.0.8 changes; allocate nothing at publication.

The resource capture is reconciled once, separately from this publication replay.
It retains authored XML and source-qualified metadata, not inferred architecture.
"""
import copy
import gzip
import hashlib
import json
from pathlib import Path

import generate_views as g
import governed_metadata as m
import publish_navigation as nav

SOURCE = m.RAD / 'accepted-ingestion-1.0.8.json'


def evidence_bytes(relative):
    """Read the source-era naming decision without rewriting reconciliation history."""
    if relative == 'docs/markdown/governance/canonical-identity-and-naming.md':
        import home_context
        manifest = home_context.EVIDENCE / 'before.json'
        if manifest.exists():
            row = m.read(manifest)[relative]
            data = gzip.decompress((m.ROOT / row['snapshot']).read_bytes())
            g.require(hashlib.sha256(data).hexdigest() == row['sha256'], 'Changed source-era naming evidence')
            return data
    return (m.ROOT / relative).read_bytes()


def read():
    data = m.read(SOURCE)
    correspondence = m.read(m.RAD / 'correspondence.json')['accepted_ingestion']
    digest = hashlib.sha256(SOURCE.read_bytes()).hexdigest()
    g.require(digest == correspondence['source_sha256'] ==
              hashlib.sha256((m.ROOT / correspondence['source_history']).read_bytes()).hexdigest(),
              'Accepted ingestion source/identity history changed')
    for relative, digest in data['evidence_sha256'].items():
        g.require(hashlib.sha256(evidence_bytes(relative)).hexdigest() == digest,
                  'Accepted ingestion evidence drift: ' + relative)
    return data


def extend_index(legacy):
    if not SOURCE.exists():
        return legacy
    extra = descriptors(read(), legacy)
    g.require(not set(legacy) & set(extra), 'Governed identity binding collision')
    result = {**legacy, **extra}
    m.validate_records(result)
    return result


def descriptors(data, legacy):
    result = {}
    for record in data['elements']:
        path = [legacy[identifier]['record'] for identifier in record['naming_context']]
        expected = dict(displayName=record['authored_name'], **m.qualified_names([*path, record]))
        g.require(record['archimateType'] == {'feature': 'Capability', 'product': 'Product',
                  'service': 'BusinessService', 'process': 'BusinessProcess'}[record['type']],
                  'Accepted native type/classification changed')
        result[record['representationId']] = dict(record=record, expected=expected,
            archimateType=record['archimateType'], harmoniaType='Feature' if record['type'] == 'feature' else None)
    return result


def parse(text):
    return g.ET.fromstring(text.encode(), parser=g.ET.XMLParser(resolve_entities=False, no_network=True))


def add_reference(root, identifier, path, reference=None, folder_context=()):
    """Presentation replay only; semantic records have already been established."""
    parent = root.find('a:organizations', g.N)
    model = root.get('identifier')
    for depth, label in enumerate(path):
        matches = [x for x in parent.findall('a:item', g.N)
                   if not x.get('identifierRef') and x.findtext('a:label', namespaces=g.N) == label]
        g.require(len(matches) <= 1, 'Ambiguous accepted navigation path')
        if matches:
            parent = matches[0]
        else:
            # Context folders bind to immutable resource identity and projection
            # role, independently of mutable names and semantic ancestry.
            bound = [(i, record) for i, label in enumerate(path[:depth+1]) for record in folder_context
                     if label == '[' + ('Feature' if record['type'] == 'feature' else record['tier']) + '] ' + record['name']]
            if bound:
                offset, record = bound[-1]
                role_depth = 3 if path[0] == 'Relations' else 2
                keys = ['home-context-folder', '/'.join(path[:role_depth]), record['canonicalId'], *path[offset+1:depth+1]]
            else:
                keys = ['accepted-108-folder', *path[:depth+1]]
            parent = g.child(parent, 'item', identifier=g.visual_id(model, *keys))
            g.child(parent, 'label', label, **{g.LANG: 'en'})
    if reference is None:
        g.child(parent, 'item', identifier=g.visual_id(model, 'accepted-108-reference', identifier), identifierRef=identifier)
    else:
        parent.append(reference)


def apply(root, data, index):
    elements = root.find('a:elements', g.N)
    for record in data['elements']:
        element = parse(record['xml'])
        g.require(element.get('identifier') == record['representationId'] and
                  element.get(g.TYPE) == record['archimateType'] and
                  element.findtext('a:name', namespaces=g.N) == record['displayName'],
                  'Accepted authored element drift')
        props = g.child(element, 'properties')
        item = index[record['representationId']]
        values = {**{k: record[k] for k in m.FIELDS}, 'archimateType': item['archimateType']}
        if item['harmoniaType']:
            values['harmoniaType'] = item['harmoniaType']
        for role, value in values.items():
            g.child(g.child(props, 'property', propertyDefinitionRef=m.property_id(role)), 'value', value)
        elements.append(element)
        add_reference(root, record['representationId'], record['navigation_path'])
    relationships = root.find('a:relationships', g.N)
    existing = {(e.get(g.TYPE), e.get('source'), e.get('target'), e.get('isDirected'),
                 tuple(e.xpath('a:properties/a:property/a:value/text()', namespaces=g.N)))
                for e in relationships}
    homes = nav.navigation_homes(root)
    for record in data['relationships']:
        edge = parse(record['xml'])
        key = (edge.get(g.TYPE), edge.get('source'), edge.get('target'), edge.get('isDirected'),
               tuple(edge.xpath('a:properties/a:property/a:value/text()', namespaces=g.N)))
        g.require(key not in existing, 'Duplicate accepted semantic assertion')
        g.require(edge.get('source') in index and edge.get('target') in index,
                  'Required reconciled endpoint does not exist')
        existing.add(key)
        relationships.append(edge)
        g.require(len(homes[edge.get('source')]) == 1, 'Unestablished source navigation home')
        add_reference(root, edge.get('identifier'), ['Relations', *homes[edge.get('source')][0]])
    views = root.find('a:views/a:diagrams', g.N)
    for record in data['views']:
        view = parse(record['xml'])
        previous = views.find(f'a:view[@identifier="{view.get("identifier")}"]', g.N)
        if previous is not None:
            views.replace(previous, view)
        else:
            views.append(view)
            add_reference(root, view.get('identifier'), record['navigation_path'])
    return root


def validate(b, root):
    """Check the entire result and all references against the bounded replay."""
    g.load_schema(g.BASE / 'schemas/archimate3_Diagram.xsd').assertValid(root)
    g.validate_identifiers(root)
    g.require(g.signature(root) == g.signature(b.reconciliation_root),
              'Accepted reconciliation semantic/presentation/reference discrepancy')
    elements = {e.get('identifier'): e for e in root.findall('a:elements/a:element', g.N)}
    relationships = {e.get('identifier'): e for e in root.findall('a:relationships/a:relationship', g.N)}
    homes = nav.navigation_homes(root)
    g.require(set(homes) == set(elements) | set(relationships) |
              {v.get('identifier') for v in root.findall('a:views/a:diagrams/a:view', g.N)} and
              all(len(paths) == 1 for paths in homes.values()), 'Duplicate/missing navigation reference')
    for edge in relationships.values():
        g.require(edge.get('source') in elements and edge.get('target') in elements,
                  'Unestablished reconciled semantic endpoint')
        g.require(homes[edge.get('identifier')] == [('Relations', *homes[edge.get('source')][0])],
                  'Relationship navigation does not follow source')
    for view in root.findall('a:views/a:diagrams/a:view', g.N):
        nodes = {n.get('identifier'): n for n in view.findall('.//a:node', g.N)}
        for node in nodes.values():
            if node.get(g.TYPE) == 'Element':
                g.require(node.get('elementRef') in elements, 'Unestablished View elementRef')
        for connection in view.findall('.//a:connection', g.N):
            edge = relationships[connection.get('relationshipRef')]
            g.require(nodes[connection.get('source')].get('elementRef') == edge.get('source') and
                      nodes[connection.get('target')].get('elementRef') == edge.get('target'),
                      'Authored View connection disagrees with semantic endpoints')
    m.validate_publication(b, root)
    return __import__('synchronise_baseline').counts(root)


def markdown(outputs, data):
    groups = {'feature': 'features.md', 'service': 'services.md', 'process': 'processes.md', 'product': 'products.md'}
    for record in data['elements']:
        path = m.RAD / groups[record['type']]
        text = outputs.get(path, b'<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->\n\n# Accepted native Products\n').decode()
        rows = '\n'.join('| ' + key + ' | ' + str(record[key]) + ' |' for key in
                         ('representationId', 'archimateType', *m.FIELDS))
        text += ('\n## ' + record['authored_name'] + '\n\nAccepted as submitted in ' + record['cr'] +
                 ' of [run-001](../../../archimate/processing/ingestion/1.0.8/run-001/change-review.md). '
                 'No additional definition, responsibility or standing is inferred. '
                 'Machine-readable source: [accepted ingestion capture](accepted-ingestion-1.0.8.json).\n\n'
                 '| Field | Value |\n| --- | --- |\n' + rows + '\n')
        outputs[path] = text.encode()
    path = m.RAD / 'relationships.md'
    text = outputs[path].decode() + '\n## Accepted 1.0.8 instance relationships\n\n'
    text += 'Native Composition, Realization, Flow, Serving and explicit Home retain independent meanings.\n\n'
    text += '| Review / CR | Relationship | Native type | Source | Target | Specialisation |\n| --- | --- | --- | --- | --- | --- |\n'
    for record in data['relationships']:
        edge = parse(record['xml'])
        text += '| ' + ' | '.join((record['run'] + '/' + record['cr'], edge.get('identifier'), edge.get(g.TYPE),
                                   edge.get('source'), edge.get('target'), record.get('specialization', ''))) + ' |\n'
    outputs[path] = text.encode()


def publish(outputs, b, report, *, home_derived=True):
    if not SOURCE.exists():
        return outputs, b, report
    data = read()
    legacy, _ = m.load(b)
    extra = descriptors(data, legacy)
    index = {**legacy, **extra}
    g.require(len(index) == len(legacy) + len(extra), 'Governed identity binding collision')
    m.validate_records(index)
    root = apply(g.ET.fromstring(outputs[m.model_path()]), data, index)
    b.reconciliation = data
    b.reconciliation_root = copy.deepcopy(root)
    b.reconciliation_index = extra
    result = validate(b, g.ET.fromstring(g.serialize(root)))
    outputs = dict(outputs)
    outputs[m.model_path()] = g.serialize(root)
    markdown(outputs, data)
    report = copy.deepcopy(report)
    report.update(after=result, output_sha256=hashlib.sha256(outputs[m.model_path()]).hexdigest(),
                  reconciliation_source=str(SOURCE.relative_to(m.ROOT)),
                  reconciliation=dict(run_001_accepted=26, run_002_accepted=6, elements_added=9,
                                      relationships_added=17, authored_home_relationships=4,
                                      views_changed=2, views_added=2, identities_allocated_during_publication=0),
                  governed_metadata=m.validate_records(index),
                  markdown_sha256={str(p.relative_to(m.ROOT)): hashlib.sha256(value).hexdigest()
                                   for p, value in outputs.items() if p.parent == m.RAD})
    report['checks']['accepted_ingestion_replay_and_references'] = 'PASS'
    report['checks']['authored_home_independent_of_realization'] = 'PASS'
    outputs[m.validation_path()] = m.encoded(report)
    if home_derived:
        import home_context
        outputs, b, report = home_context.publish(outputs, b, report, index)
        validate(b, g.ET.fromstring(outputs[m.model_path()]))
    return outputs, b, report
