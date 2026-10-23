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
           ('V', 'Technology'), ('VI', 'Implementation & Migration'),
           ('R', 'Cross-Cutting References'))


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
    c.require(data.get('schemaVersion') == 2 and data.get('authority'), 'Unqualified navigation source')
    roots = data.get('roots', [])
    c.require([(n.get('namespace'), n.get('title')) for n in roots] == list(DOMAINS),
              'Navigation domains/order must match the authorised taxonomy')
    ids = []
    catalogue = c.read_json(root / c.CONFIG / 'type-catalogue.json')
    namespace_for_domain = {'motivation': 'I', 'strategy': 'II', 'business': 'III',
                            'application': 'IV', 'technology': 'V', 'physical': 'V',
                            'implementation-migration': 'VI', 'implementation-and-migration': 'VI'}

    def visit(node, number, path, namespace, parent=None):
        c.require(isinstance(node, dict), 'Navigation node must be an object')
        c.require(not any(k in node for k in ('number', 'identifier', 'relationships', 'archimateType')),
                  'Navigation cannot supply identity, native semantics or persisted coordinates')
        c.require(not any(k in node for k in ('canonicalReference', 'historicalReferences', 'successorReference', 'occurrence', 'standing')),
                  'Resolved reference locations and qualifications must be derived from canonical sources')
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
            reference_entry = namespace == 'R' and (
                (node.get('kind') == 'index-entry' and native == 'Stakeholder') or
                (node.get('kind') == 'historical' and native == 'Capability'
                 and elements[cid][0]['identity'].get('correspondence')))
            c.require(reference_entry or expected is None or namespace == expected,
                      'Navigation changes architectural domain without authority: ' + cid)
            c.require(native != 'Stakeholder' or reference_entry,
                      'Stakeholders belong in the cross-cutting index, not the Motivation decomposition')
            correspondence = elements[cid][0]['identity'].get('correspondence')
            if correspondence:
                c.require(reference_entry and node.get('kind') == 'historical',
                          'Governed predecessor must use historical reference navigation: ' + cid)
            if native in ('Capability', 'Stakeholder'):
                node['standing'] = elements[cid][0]['standing']
        if context:
            c.require(context in elements and not cid, 'Invalid navigation context reference')
            c.require('title' not in node, 'Context names come from their canonical elements')
            node['title'] = elements[context][0]['displayName']
            c.require(node.get('kind') == 'context' and node.get('contextRole') in ('capability', 'stakeholder'),
                      'Context must explicitly identify its reference role')
            native = elements[context][0]['archimateType']
            c.require((node['contextRole'], native, namespace) in
                      (('capability', 'Capability', 'III'), ('stakeholder', 'Stakeholder', 'I')),
                      'Context role/type/domain mismatch')
            node['standing'] = elements[context][0]['standing']
            if native == 'Stakeholder':
                concern = parent.get('associatedCanonicalId') if parent else None
                c.require(concern in elements and elements[concern][0]['archimateType'] != 'Stakeholder',
                          'Stakeholder context needs a canonical Motivation concern')
                actual = stakeholder_associations(elements).get(concern, {}).get(context, [])
                c.require(actual and sorted(node.get('relationshipIds', [])) == sorted(actual),
                          'Stakeholder context must cite exactly the existing canonical associations')
        c.require(isinstance(node.get('title'), str) and node['title'], 'Unlabelled navigation node')
        if not cid and not context:
            c.require(node['title'] not in {'Continuum additions', 'Unreconciled 1.0.4 Reference',
                      'BusinessObjectMap', 'InformationMap', 'Application Layer', 'Business Layer',
                      'Pending Adjudication', 'Superseded', 'Stakeholders'},
                      'Model/history/type-bucket folder is not an authorised architectural subject')
        c.require(node.get('basis'), 'Navigation classification needs an explicit basis: ' + node['title'])
        c.require(node.get('kind', 'section') in ('section', 'element', 'reference', 'context',
                                               'reference-section', 'historical', 'index-entry'),
                  'Unknown navigation presentation kind')
        if node.get('source'):
            target = Path(node['source'].split('#')[0])
            c.require(not target.is_absolute() and '..' not in target.parts and str(target).startswith('enterprise-architecture/'),
                      'Navigation source must be a current EA locator')
        for related in node.get('relatedCanonicalIds', []):
            c.require(related in elements, 'Unknown navigation cross-reference: ' + related)
        children = node.get('children', [])
        if node.get('associatedCanonicalId'):
            c.require(parent and node['associatedCanonicalId'] == parent.get('canonicalId') and namespace == 'I',
                      'Stakeholder reference group must belong to the associated Motivation element')
        c.require(isinstance(children, list), 'Navigation children must be ordered')
        c.require(not path or cid or context or node.get('source') or node.get('relatedCanonicalIds') or children,
                  'Empty architectural heading: ' + node['title'])
        node['number'], node['path'] = number, path + [node['title']]
        node['locator'] = str(SOURCE) + '#/' + number
        for index, child in enumerate(children, 1):
            visit(child, number + '.' + str(index), node['path'], namespace, node)
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
    locations = by_element(roots)
    canonical_chains = {}

    def chains(node, chain=()):
        cid = node.get('canonicalId')
        if cid and elements[cid][0]['archimateType'] == 'Capability':
            chain += (cid,)
            canonical_chains[cid] = chain
        for child in node.get('children', []):
            chains(child, chain)
    chains(roots[2])

    def contexts(node, chain=()):
        cid = node.get('contextCanonicalId')
        if cid and node.get('contextRole') == 'capability':
            chain += (cid,)
            c.require(chain == canonical_chains.get(cid), 'Incomplete or incorrect contextual Capability ancestry: ' + cid)
            c.require(descendants(node), 'Capability context has no represented behaviour descendants: ' + cid)
        for child in node.get('children', []):
            contexts(child, chain)
    contexts(roots[3])
    represented = {(group['associatedCanonicalId'], child['contextCanonicalId'], edge)
                   for group in walk(roots) if group.get('associatedCanonicalId')
                   for child in group.get('children', []) for edge in child.get('relationshipIds', [])}
    expected = {(concern, stakeholder, edge) for concern, related in stakeholder_associations(elements).items()
                for stakeholder, edges in related.items() for edge in edges}
    c.require(represented == expected, 'Stakeholder contextual association coverage differs from canonical relationships')

    def reference(cid):
        m = elements[cid][0]
        return {'canonicalId': cid, 'number': locations[cid]['number'], 'title': m['displayName'],
                'locator': locations[cid]['locator'], 'standing': m['standing']}

    for node in walk(roots):
        cid = node.get('canonicalId')
        context = node.get('contextCanonicalId')
        node['occurrence'] = ('contextual' if context else 'historical' if node.get('kind') == 'historical'
                              else 'index' if node.get('kind') == 'index-entry'
                              else 'architectural' if cid else 'navigation')
        if context:
            node['canonicalReference'] = reference(context)
        if cid:
            m = elements[cid][0]
            if m['identity'].get('correspondence'):
                node['successorReference'] = reference(m['identity']['correspondence']['successorCanonicalId'])
                node['correspondence'] = m['identity']['correspondence']
            if m['identity'].get('predecessorCanonicalIds'):
                node['historicalReferences'] = [reference(old) for old in m['identity']['predecessorCanonicalIds']]
    return roots


def by_element(roots):
    return {n['canonicalId']: n for n in walk(roots) if n.get('canonicalId')}


def title(node):
    role = {'context': 'Capability context' if node.get('contextRole') == 'capability' else 'Stakeholder reference',
            'historical': 'Historical identity', 'reference-section': 'Reference navigation',
            'index-entry': 'Stakeholder index entry'}.get(node.get('kind'))
    standing = node.get('standing', {}).get('currentness')
    qualifier = (' [' + standing + ']') if standing in ('superseded', 'historical_reference') else ''
    return node['number'] + ' ' + node['title'] + (' — ' + role if role else '') + qualifier


def stakeholder_associations(elements):
    """Existing native associations in either direction, never lexical inference."""
    by_rep = {m['exchange']['identifier']: cid for cid, (m, _, _) in elements.items()}
    result = {}
    for m, _, _ in elements.values():
        for edge in m['relationships']:
            if edge['archimateType'] != 'Association':
                continue
            a, b = (by_rep[edge[key]] for key in ('source', 'target'))
            for stakeholder, concern in ((a, b), (b, a)):
                if (elements[stakeholder][0]['archimateType'] == 'Stakeholder'
                        and elements[concern][0]['archimateType'] in
                        {'Driver', 'Assessment', 'Goal', 'Outcome', 'Principle', 'Requirement', 'Constraint', 'Meaning', 'Value'}
                        and elements[concern][0]['archimateType'] != 'Stakeholder'):
                    result.setdefault(concern, {}).setdefault(stakeholder, []).append(edge['identifier'])
    return result


def references(node):
    """Uniform typed links shared by all reading/transport projections."""
    result = []
    if node.get('canonicalReference'):
        result.append(('Canonical Strategy Capability' if node.get('contextRole') == 'capability'
                       else 'Canonical Stakeholder', node['canonicalReference']))
    if node.get('successorReference'):
        result.append(('Governed successor (' + node['correspondence']['disposition'] + ')', node['successorReference']))
    result.extend(('Historical predecessor', ref) for ref in node.get('historicalReferences', []))
    return result


def transport_metadata(roots):
    """Navigation properties survive Archi's lossy non-element folder import.

    These are presentation metadata, never additional ArchiMate relationships.
    """
    result = {cid: {'occurrence': node['occurrence'], 'references': [
        {'role': role, **reference} for role, reference in references(node)]}
        for cid, node in by_element(roots).items()}

    def context_record(node):
        return {key: node[key] for key in ('number', 'contextRole', 'canonicalReference', 'relationshipIds') if key in node}

    def visit(node, contexts):
        if node.get('contextCanonicalId'):
            record = context_record(node)
            result[node['contextCanonicalId']].setdefault('contextualOccurrences', []).append(record)
            contexts = contexts + [record]
        if node.get('canonicalId'):
            row = result[node['canonicalId']]
            if contexts:
                row['contextPath'] = contexts
            for group in node.get('children', []):
                if group.get('associatedCanonicalId') == node['canonicalId']:
                    row['stakeholderReferences'] = [context_record(child) for child in group['children']]
        for child in node.get('children', []):
            visit(child, contexts)
    for root in roots:
        visit(root, [])
    return result


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
        notes.append('Occurrence: ' + node['occurrence'] + '; no additional architectural instance or semantic relationship.')
        for role, reference in references(node):
            notes.append(role + ': ' + reference['number'] + ' ' + reference['title']
                         + '; canonicalId=' + reference['canonicalId']
                         + '; Exchange identifier=' + elements[reference['canonicalId']][0]['exchange']['identifier'])
        if node.get('relationshipIds'):
            notes.append('Existing canonical associations: ' + ', '.join(node['relationshipIds']))
        # Keep contexts as navigation-only items: no semantic edge and no second
        # canonical occurrence. Archi can discard these folders; documentation
        # on the behaviour below also carries the full contextual path.
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
    kind = 'index-entry' if parent_path == ['Cross-Cutting References', 'Stakeholder Index'] else 'element'
    nodes.append({'canonicalId': cid, 'kind': kind, 'basis': 'Explicit authorised creation placement'})
    return data
