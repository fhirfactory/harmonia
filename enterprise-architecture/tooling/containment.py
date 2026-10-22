# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Governed type-selected Home/Composition ancestry over current Exchange."""
from collections import defaultdict
from types import SimpleNamespace
import exchange_xml as g
import home_relationships as home
import relationship_state

COMPOSITION_TYPES = frozenset(('Capability', 'BusinessObject', 'DataObject', 'Artifact'))
VERSION = 'harmonia-type-selected-containment-v1'

def native_type(node):
    return (node.get(g.TYPE) or '').split(':')[-1]



def mechanism(node):
    return 'Composition' if native_type(node) in COMPOSITION_TYPES else 'Home'



class Ancestry:
    """The single graph walk, also used for exact historical context replay."""
    def __init__(self, elements, incoming):
        self.elements = elements
        self.incoming = incoming

    def parent_link(self, identifier):
        links = self.incoming.get(identifier, [])
        kind = links[0]['mechanism'] if links else 'containment'
        message = 'Multiple explicit Homes' if kind == 'Home' else 'Competing/duplicate ' + kind + ' parents'
        g.require(len(links) <= 1, message + ': ' + identifier)
        return links[0] if links else None

    def ancestry(self, identifier):
        reverse, seen, current = [], set(), identifier
        kind = 'containment'
        while True:
            g.require(current in self.elements, 'Missing ' + kind + ' endpoint: ' + str(current))
            g.require(current not in seen, 'Containment cycle affecting: ' + identifier)
            seen.add(current)
            reverse.append(current)
            link = self.parent_link(current)
            if link is None:
                return list(reversed(reverse))
            current, kind = link['parent'], link['mechanism']

    def paths(self, scope=None, closed=False):
        scope = set(self.elements) if scope is None else set(scope)
        if closed:
            for child, links in self.incoming.items():
                for link in links:
                    if child in scope or link['parent'] in scope:
                        g.require(child in self.elements and link['parent'] in self.elements,
                                  'Missing ' + link['mechanism'] + ' endpoint: ' + link['identifier'])
                        g.require(child in scope and link['parent'] in scope,
                                  'Containment outside declared projection scope: ' + link['identifier'])
        paths = {key: self.ancestry(key) for key in sorted(scope)}
        if closed:
            g.require(all(set(path) <= scope for path in paths.values()),
                      'Containment ancestor outside declared projection scope')
        return paths

    def findings(self, scope=None):
        result = []
        for identifier in sorted(set(self.elements) | set(self.incoming) if scope is None else scope):
            try:
                self.ancestry(identifier)
            except ValueError as exc:
                result.append(dict(element=identifier, reason=str(exc),
                    direct_parent_relationships=[e['identifier'] for e in self.incoming.get(identifier, [])]))
        return result



class ResolvedContainment(Ancestry):
    def __init__(self, root, records=None, accepted=(), include_proposals=True):
        elements = {e.get('identifier'): e for e in root.findall('a:elements/a:element', g.N)}
        model = SimpleNamespace(
            keys={p.get('identifier'): p.findtext('a:name', namespaces=g.N)
                  for p in root.findall('a:propertyDefinitions/a:propertyDefinition', g.N)},
            relationships={e.get('identifier'): e for e in root.findall('a:relationships/a:relationship', g.N)})
        self.population = relationship_state.exchange_population(model, records, accepted, include_proposals)
        # Reuse the existing Home recogniser, after relationship-state filtering.
        active = SimpleNamespace(keys=model.keys, relationships={key: model.relationships[key]
            for key in sorted(self.population['eligible']) if key in model.relationships})
        homes = {row['identifier']: row for row in home.authored_homes(active)}
        incoming = defaultdict(list)
        self.selected, self.ignored = {}, []
        for identifier, edge in sorted(active.relationships.items()):
            if native_type(edge) == 'Composition':
                parent, child, kind = edge.get('source'), edge.get('target'), 'Composition'
            elif identifier in homes:
                child, parent, kind = edge.get('source'), edge.get('target'), 'Home'
            else:
                continue
            if child in elements and mechanism(elements[child]) != kind:
                self.ignored.append(identifier)
                continue
            link = dict(identifier=identifier, parent=parent, child=child, mechanism=kind,
                        source=edge.get('source'), target=edge.get('target'), line=edge.sourceline)
            incoming[child].append(link)
            self.selected[identifier] = link
        super().__init__(elements, incoming)

    def evidence(self):
        findings = self.findings()
        invalid = {row['element'] for row in findings}
        return dict(version=VERSION, default='Home', home_native_type='Association',
                    home_specialization='Home', composition_native_types=sorted(COMPOSITION_TYPES),
                    selected_relationships=self.selected,
                    excluded_relationships={key: self.population['states'].get(key)
                                            for key in sorted(self.population['excluded'])},
                    ignored_mechanism_relationships=self.ignored,
                    conditional_proposals=sorted(self.population['proposed']),
                    ancestry=self.paths(set(self.elements) - invalid), findings=findings)



def resolve(root, **kwargs):
    return ResolvedContainment(root, **kwargs)

