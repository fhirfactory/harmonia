#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""The canonical Continuum navigation tree, independent of semantic edges.

Locations are ordered paths, not identities. Publication never classifies an
element: it consumes the explicitly governed tree. Native-type file storage and
canonical identity remain independent of the reader's architectural taxonomy.
"""
import copy
from collections import Counter
from pathlib import Path

import architecture_continuum as c

SOURCE = c.CONTINUUM / 'navigation.json'
DOMAINS = (('G', 'Architecture Governance'), ('I', 'Motivation'),
           ('II', 'Strategy'), ('III', 'Business'), ('IV', 'Application'),
           ('V', 'Technology'), ('VI', 'Implementation & Migration'))


def walk(nodes):
    for node in nodes:
        yield node
        yield from walk(node.get('children', []))


def descendants(node):
    return [n['canonicalId'] for n in walk([node]) if n.get('canonicalId')]


def load(root=c.ROOT, elements=None, *, document=None):
    """Validate organisation and derive every coordinate in one shared traversal."""
    if elements is None:
        elements = c.load(root)[1]
    data = copy.deepcopy(document if document is not None else c.read_json(root / SOURCE))
    c.require(data.get('schemaVersion') == 1 and data.get('authority'), 'Unqualified navigation source')
    roots = data.get('roots', [])
    c.require([(n.get('namespace'), n.get('title')) for n in roots] == list(DOMAINS),
              'Navigation domains/order must match the authorised taxonomy')
    ids = []
    catalogue = c.read_json(root / c.CONFIG / 'type-catalogue.json')
    namespace_for_domain = {'motivation': 'I', 'strategy': 'II', 'business': 'III',
                            'application': 'IV', 'technology': 'V', 'physical': 'V',
                            'implementation-migration': 'VI', 'implementation-and-migration': 'VI'}

    def visit(node, number, path, namespace):
        c.require(isinstance(node, dict), 'Navigation node must be an object')
        c.require(not any(k in node for k in ('number', 'identifier', 'relationships', 'archimateType')),
                  'Navigation cannot supply identity, native semantics or persisted coordinates')
        cid = node.get('canonicalId')
        context = node.get('contextCanonicalId')
        if cid:
            c.require(cid in elements, 'Unknown navigation canonicalId: ' + cid)
            c.require('title' not in node, 'Element navigation names come from the canonical element')
            ids.append(cid)
            node['title'] = elements[cid][0]['displayName']
            native = elements[cid][0]['archimateType']
            domain = Path(catalogue['types'][native]['directory']).parts[0]
            expected = 'III' if native == 'ValueStream' else namespace_for_domain.get(domain)
            c.require(expected is None or namespace == expected,
                      'Navigation changes architectural domain without authority: ' + cid)
        if context:
            c.require(context in elements and not cid, 'Invalid navigation context reference')
            c.require('title' not in node, 'Context names come from their canonical elements')
            node['title'] = elements[context][0]['displayName']
        c.require(isinstance(node.get('title'), str) and node['title'], 'Unlabelled navigation node')
        if not cid and not context:
            c.require(node['title'] not in {'Continuum additions', 'Unreconciled 1.0.4 Reference',
                      'BusinessObjectMap', 'InformationMap', 'Application Layer', 'Business Layer',
                      'Pending Adjudication', 'Superseded', 'Stakeholders'},
                      'Model/history/type-bucket folder is not an authorised architectural subject')
        c.require(node.get('basis'), 'Navigation classification needs an explicit basis: ' + node['title'])
        c.require(node.get('kind', 'section') in ('section', 'element', 'perspective', 'reference'),
                  'Unknown navigation presentation kind')
        if node.get('kind') == 'perspective':
            c.require(namespace == 'I' and cid and elements[cid][0]['archimateType'] == 'Stakeholder',
                      'Cross-cutting Motivation perspectives must retain Stakeholder semantics')
        if node.get('source'):
            target = Path(node['source'].split('#')[0])
            c.require(not target.is_absolute() and '..' not in target.parts and str(target).startswith('enterprise-architecture/'),
                      'Navigation source must be a current EA locator')
        for related in node.get('relatedCanonicalIds', []):
            c.require(related in elements, 'Unknown navigation cross-reference: ' + related)
        children = node.get('children', [])
        c.require(isinstance(children, list), 'Navigation children must be ordered')
        c.require(not path or cid or context or node.get('source') or node.get('relatedCanonicalIds') or children,
                  'Empty architectural heading: ' + node['title'])
        node['number'], node['path'] = number, path + [node['title']]
        node['locator'] = str(SOURCE) + '#/' + number
        for index, child in enumerate(children, 1):
            visit(child, number + '.' + str(index), node['path'], namespace)
    for node in roots:
        visit(node, node['namespace'], [], node['namespace'])
    c.require(Counter(ids) == Counter(elements.keys()),
              'Navigation must locate every canonical element exactly once; missing=' +
              str(sorted(set(elements) - set(ids))[:5]) + '; repeated=' +
              str([cid for cid, n in Counter(ids).items() if n > 1][:5]))
    # The two information models are never interchangeable, even in navigation.
    for node in walk(roots):
        cid = node.get('canonicalId')
        if cid and node['path'][:3] == ['Application', 'Information Architecture', 'Topics']:
            c.require(elements[cid][0].get('specialization') == 'Topic', 'Non-Topic in logical Topics navigation')
    return roots


def by_element(roots):
    return {n['canonicalId']: n for n in walk(roots) if n.get('canonicalId')}


def title(node):
    return node['number'] + ' ' + node['title']


def transport_id(node):
    """Mutable XML organisation locator; never an architectural identity."""
    return 'navigation-' + node['number'].replace('.', '-')


def project(model, roots, elements):
    """Replace architecture folders; preserve authored diagrams and View folders."""
    org = model.find('a:organizations', c.N)
    views = {v.get('identifier') for v in model.findall('a:views/a:diagrams/a:view', c.N)}

    def only_views(node):
        for child in list(node.findall('a:item', c.N)):
            if not only_views(child):
                node.remove(child)
        return node.get('identifierRef') in views or bool(node.findall('a:item', c.N))

    for child in list(org):
        if not only_views(child):
            org.remove(child)
    retained = list(org)
    for child in retained:
        org.remove(child)

    def add(parent, node):
        attrs = {'identifier': transport_id(node)}
        if node.get('canonicalId'):
            attrs['identifierRef'] = elements[node['canonicalId']][0]['exchange']['identifier']
        item = c.ET.SubElement(parent, c.tag('item'), **attrs)
        c.ET.SubElement(item, c.tag('label'), {c.LANG: 'en'}).text = title(node)
        notes = ['Continuum navigation coordinate: ' + node['number'] + '. Navigation asserts no semantic containment.']
        if node.get('source'):
            notes.append('Source: ' + node['source'])
        if node.get('description'):
            notes.append(node['description'])
        c.ET.SubElement(item, c.tag('documentation'), {c.LANG: 'en'}).text = '\n\n'.join(notes)
        for child in node.get('children', []):
            add(item, child)
    for node in roots:
        add(org, node)
    for child in retained:
        org.append(child)


def with_element(document, cid, parent_path, elements=None):
    """Preflight explicit creation placement without writing or allocating IDs."""
    data = copy.deepcopy(document)
    nodes = data['roots']
    parent = None
    for name in parent_path:
        def node_name(node):
            reference = node.get('canonicalId') or node.get('contextCanonicalId')
            if reference and elements is not None:
                return elements[reference][0]['displayName']
            return node.get('title')
        matches = [n for n in nodes if node_name(n) == name]
        c.require(len(matches) == 1, 'Missing/ambiguous creation navigation parent: ' + name)
        parent = matches[0]
        nodes = parent.setdefault('children', [])
    c.require(parent is not None, 'Creation requires a navigation parent')
    nodes.append({'canonicalId': cid, 'kind': 'element', 'basis': 'Explicit authorised creation placement'})
    return data
