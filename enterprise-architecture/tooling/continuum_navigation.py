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


def workbench_hierarchy_members(roots, elements, model):
    """Members of the existing governed hierarchies, including their leaves.

    The accepted Continuum organisation retains Capability parent/owner ancestry
    in the three Strategy capability scopes and logical Topic decomposition in
    Application Information Architecture. Consume those established scopes; do
    not reinterpret arbitrary navigation, native relationships, contextual
    allocations or historical indexes as containment. Additionally consume the
    existing governed model containment resolver for explicitly represented
    Home/type-selected Composition participation, retaining its state selection.
    This never changes the accepted navigation home or substitutes a new
    universal containment rule for the applicable area-specific architecture.
    """
    result = set()
    for node in walk(roots):
        cid = node.get('canonicalId')
        if not cid or node['occurrence'] != 'architectural':
            continue
        element = elements[cid][0]
        capability = (element['archimateType'] == 'Capability'
                      and node['path'][:2] == ['Strategy', 'Capabilities']
                      and node['path'][2] in ('Enterprise Capabilities', 'Business Capabilities',
                                             'Business Enabling Capabilities'))
        topic = (element.get('specialization') == 'Topic'
                 and node['path'][:3] == ['Application', 'Information Architecture', 'Topics'])
        if capability or topic:
            result.add(cid)
    import containment
    locations = by_element(roots)
    by_rep = {m['exchange']['identifier']: cid for cid, (m, _, _) in elements.items()}
    for link in containment.resolve(model).selected.values():
        members = [by_rep[link[key]] for key in ('parent', 'child')]
        # Historical/index occurrences retain reference navigation, even when
        # their retained source relationships describe an earlier hierarchy.
        if all(locations[cid]['occurrence'] == 'architectural' for cid in members):
            result.update(members)
    return result


def project(model, roots, elements, catalogue, *, preserve_view_hierarchy=False):
    """Derive Archi physical organisation from shared navigation semantics.

    Native category markers anchor even source-only/context-only folders. A
    governed hierarchy member has a directory plus one canonical leaf placement,
    including leaves. Other navigation parents also need folders for Archi's
    mechanics, without implying containment. Cross-layer subjects have one native
    placement and a reference-only folder in their logical context.
    """
    org = model.find('a:organizations', c.N)
    views = {v.get('identifier') for v in model.findall('a:views/a:diagrams/a:view', c.N)}
    hierarchy_members = workbench_hierarchy_members(roots, elements, model)

    def only_views(node, in_views=False):
        in_views = in_views or node.findtext('a:label', namespaces=c.N) == 'Views'
        if in_views and preserve_view_hierarchy:
            # Authored View hierarchy includes empty folders and their metadata.
            return True
        for child in list(node.findall('a:item', c.N)):
            if not only_views(child, in_views):
                node.remove(child)
        return node.get('identifierRef') in views or bool(node.findall('a:item', c.N))

    for child in list(org):
        if not only_views(child):
            org.remove(child)
    retained = list(org)
    for child in retained:
        org.remove(child)

    native_labels = {'motivation': 'Motivation', 'strategy': 'Strategy',
                     'business': 'Business', 'application': 'Application',
                     'technology': 'Technology & Physical', 'physical': 'Technology & Physical',
                     'implementation-migration': 'Implementation & Migration',
                     'implementation-and-migration': 'Implementation & Migration',
                     'other': 'Other', 'generic': 'Other'}
    domain_layers = {'G': 'Other', 'I': 'Motivation', 'II': 'Strategy',
                     'III': 'Business', 'IV': 'Application', 'V': 'Technology & Physical',
                     'VI': 'Implementation & Migration'}
    anchors = {}

    def anchor(layer):
        if layer not in anchors:
            item = c.ET.SubElement(org, c.tag('item'), identifier='archi-native-' + layer.split()[0].lower())
            c.ET.SubElement(item, c.tag('label'), {c.LANG: 'en'}).text = layer
            anchors[layer] = item
        return anchors[layer]

    def native_layer(cid):
        elem = elements[cid][0]
        native = elem['archimateType']
        spec = elem.get('specialization')
        if native == 'Grouping':
            if spec == 'Package':
                return 'Application'
            if spec == 'RequirementGroup':
                return 'Motivation'
            return 'Other'
        domain = Path(catalogue['types'][native]['directory']).parts[0]
        c.require(domain in native_labels, 'Unsupported Archi native domain: ' + domain)
        return native_labels[domain]

    def notes(node):
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
        for cid in node.get('relatedCanonicalIds', []):
            notes.append('Related canonical subject: canonicalId=' + cid + '; Exchange identifier='
                         + elements[cid][0]['exchange']['identifier'])
        return notes

    def add(parent, node, layer, *, suffix=''):
        cid = node.get('canonicalId')
        foreign = cid and native_layer(cid) != layer
        item = c.ET.SubElement(parent, c.tag('item'), identifier=transport_id(node) + suffix)
        c.ET.SubElement(item, c.tag('label'), {c.LANG: 'en'}).text = title(node)
        doc = notes(node)
        documentation = c.ET.SubElement(item, c.tag('documentation'), {c.LANG: 'en'})
        if foreign:
            doc.append('Reference only: canonicalId=' + cid + '; Exchange identifier='
                       + elements[cid][0]['exchange']['identifier'] + '; native placement: ' + native_layer(cid)
                       + '. This folder does not place or duplicate the canonical element.')
            # Visible qualification survives even when folder documentation is
            # not exposed by a downstream workbench.
            item.find('a:label', c.N).text += ' — reference; canonical element in ' + native_layer(cid)
        elif cid:
            target = item
            if cid in hierarchy_members or node.get('children'):
                doc.append('Canonical element directory: canonicalId=' + cid
                           + '; Exchange identifier=' + elements[cid][0]['exchange']['identifier']
                           + ('. Existing governed hierarchy member; directory retained for leaves.'
                              if cid in hierarchy_members else '. Navigation context; no containment inferred.'))
                target = c.ET.SubElement(item, c.tag('item'), identifier=transport_id(node) + '-element')
                c.ET.SubElement(target, c.tag('label'), {c.LANG: 'en'}).text = node['title']
            target.set('identifierRef', elements[cid][0]['exchange']['identifier'])
        documentation.text = '\n\n'.join(doc)
        for child in node.get('children', []):
            add(item, child, layer, suffix=suffix)
        return item

    indexes = {}
    for node in roots:
        namespace = node['namespace']
        if namespace == 'R':
            # R is a cross-cutting index, not a native layer or decomposition.
            # Partition its primary index populations by native type, retaining
            # their shared R coordinates and historical/currentness qualifiers.
            for section in node.get('children', []):
                layers = {native_layer(cid) for cid in descendants(section)}
                c.require(len(layers) == 1, 'Cross-cutting index requires an explicit native-layer allocation')
                layer = layers.pop()
                root_item = indexes.get(layer)
                if root_item is None:
                    root_item = c.ET.SubElement(anchor(layer), c.tag('item'), identifier='navigation-R-' + layer.lower())
                    c.ET.SubElement(root_item, c.tag('label'), {c.LANG: 'en'}).text = title(node) + ' — ' + layer + ' index; not decomposition'
                    c.ET.SubElement(root_item, c.tag('documentation'), {c.LANG: 'en'}).text = '\n\n'.join(notes(node))
                    indexes[layer] = root_item
                add(root_item, section, layer)
        else:
            layer = domain_layers[namespace]
            add(anchor(layer), node, layer)

    # Currently the only architectural/native-layer exception is ValueStream:
    # Business reading context, native Strategy representation. Export its
    # nearest shared navigation section directly as an explicit reference
    # context, never reproduce the foreign domain root under Strategy.
    def exceptions(node, layer, parent=None):
        cid = node.get('canonicalId')
        if cid and native_layer(cid) != layer:
            c.require(elements[cid][0]['archimateType'] == 'ValueStream'
                      and layer == 'Business' and parent is not None,
                      'New cross-layer navigation needs explicit projection disposition: ' + cid)
            native = native_layer(cid)
            key = transport_id(parent) + '-native-' + native.lower()
            target = next((i for i in anchor(native).findall('a:item', c.N) if i.get('identifier') == key), None)
            if target is None:
                target = c.ET.SubElement(anchor(native), c.tag('item'), identifier=key)
                c.ET.SubElement(target, c.tag('label'), {c.LANG: 'en'}).text = title(parent) + ' — Business navigation reference; native ' + native
                c.ET.SubElement(target, c.tag('documentation'), {c.LANG: 'en'}).text = '\n\n'.join(notes(parent) + [
                    'Canonical navigation path: ' + ' / '.join(parent['path']) + '. Native placement does not change architectural domain.'])
            add(target, node, native, suffix='-native-' + native.lower())
        for child in node.get('children', []):
            exceptions(child, layer, node)
    for node in roots:
        if node['namespace'] in domain_layers:
            exceptions(node, domain_layers[node['namespace']])
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
