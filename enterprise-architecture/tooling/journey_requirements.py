# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Validate the authorised Journey and independent requirement specialisations.

No coverage quota, beneficiary, execution depth or paired requirement is implied.
"""
from collections import defaultdict

from governed_naming import component


PARTICIPATION = frozenset(('Initiator', 'Participant', 'Beneficiary', 'Observer'))
REQUIREMENTS = frozenset(('UseCase', 'UserStory'))


def validate(elements, relationships, representations, require):
    journeys = {cid for cid, (m, _, _) in elements.items()
                if m.get('specialization') == 'Journey'}
    for cid, (metadata, _, _) in elements.items():
        definition = metadata.get('journeyDefinition')
        if definition is not None:
            require(cid in journeys, 'Journey definition requires Journey classification: ' + cid)
            require(isinstance(definition, dict) and definition.get('intendedOutcome'),
                    'Journey needs an intended outcome: ' + cid)
        naming = metadata.get('naming', {})
        facet = naming.get('solutionFacetCanonicalId')
        if facet:
            require(metadata.get('specialization') in REQUIREMENTS,
                    'SolutionFacet requirement naming has incompatible specialization: ' + cid)
            require(facet in elements and elements[facet][0].get('specialization') == 'SolutionFacet',
                    'Requirement naming needs an established SolutionFacet: ' + cid)
            context = elements[facet][0]['friendlyName']
            require(context and naming.get('localName'), 'Incomplete SolutionFacet naming inputs: ' + cid)
            require(metadata['friendlyName'] == context + '.' + component(naming['localName']),
                    'SolutionFacet requirement naming mismatch: ' + cid)
    structural = defaultdict(list)
    composition_parents = defaultdict(set)
    for rid, row in relationships.items():
        source = representations[row['source']]
        target = representations.get(row['target'])
        characteristics = row.get('participationCharacteristics')
        if characteristics is not None:
            require(isinstance(characteristics, list)
                    and all(isinstance(value, str) for value in characteristics)
                    and len(characteristics) == len(set(characteristics))
                    and set(characteristics) <= PARTICIPATION,
                    'Invalid Journey participation characteristics: ' + rid)
            require(row['archimateType'] == 'Association'
                    and elements[source][0]['archimateType'] == 'Stakeholder'
                    and target in journeys,
                    'Participation must associate a Stakeholder with a Journey: ' + rid)
        if source in journeys and target in journeys and row['archimateType'] in ('Composition', 'Aggregation'):
            structural[source].append(target)
            if row['archimateType'] == 'Composition':
                composition_parents[target].add(source)
    for child, parents in composition_parents.items():
        require(len(parents) <= 1,
                'Shared Journey cannot have competing Composition owners; assess Aggregation: ' + child)
    visited, active = set(), set()

    def visit(cid):
        require(cid not in active, 'Recursive Journey structural cycle: ' + cid)
        if cid in visited:
            return
        active.add(cid)
        for child in structural[cid]:
            visit(child)
        active.remove(cid)
        visited.add(cid)

    for cid in journeys:
        visit(cid)
