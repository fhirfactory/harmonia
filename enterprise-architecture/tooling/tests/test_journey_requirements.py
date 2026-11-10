# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Meaningful mutation checks for recursive Journeys and independent needs."""
import copy
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import architecture_continuum as c
import journey_requirements as profile


def element(cid, native, specialization=None, **fields):
    return ({'canonicalId': cid, 'archimateType': native, 'specialization': specialization,
             'exchange': {'identifier': cid}, **fields}, '', None)


class JourneyRequirementTests(unittest.TestCase):
    def check(self, elements, edges=()):
        profile.validate(elements, {edge['identifier']: edge for edge in edges},
                         {cid: cid for cid in elements}, c.require)

    def edge(self, source, target, kind='Aggregation', **fields):
        return {'identifier': source + '-' + target + '-' + kind,
                'source': source, 'target': target, 'archimateType': kind, **fields}

    def test_native_bases_and_no_separate_stage_specialization(self):
        for specialization, native in [('Journey', 'ValueStream'), ('UseCase', 'Requirement'), ('UserStory', 'Requirement')]:
            self.assertEqual(c.template(native, specialization)['metadata']['archimateType'], native)
            with self.assertRaisesRegex(ValueError, 'incompatible'):
                c.template('BusinessProcess', specialization)
        with self.assertRaisesRegex(ValueError, 'Ungoverned specialization'):
            c.template('ValueStream', 'JourneyStage')

    def test_independent_requirements_and_journey_without_beneficiary_are_valid(self):
        self.check({'j': element('j', 'ValueStream', 'Journey', journeyDefinition={
                        'intendedOutcome': 'A required operational obligation is fulfilled.'}),
                    'u': element('u', 'Requirement', 'UseCase'),
                    's': element('s', 'Requirement', 'UserStory')})

    def test_arbitrary_structural_depth_shared_aggregation_and_progression_repetition(self):
        elements = {str(i): element(str(i), 'ValueStream', 'Journey') for i in range(9)}
        edges = [self.edge(str(i), str(i+1)) for i in range(7)]
        edges += [self.edge('8', '6'), self.edge('7', '1', 'Triggering')]
        self.check(elements, edges)
        with self.assertRaisesRegex(ValueError, 'structural cycle'):
            self.check(elements, edges + [self.edge('7', '1')])

    def test_shared_structure_cannot_silently_acquire_multiple_composition_owners(self):
        elements = {cid: element(cid, 'ValueStream', 'Journey') for cid in ('one', 'two', 'shared')}
        edges = [self.edge('one', 'shared', 'Composition'), self.edge('two', 'shared', 'Composition')]
        with self.assertRaisesRegex(ValueError, 'competing Composition'):
            self.check(elements, edges)
        edges[1]['archimateType'] = 'Aggregation'
        self.check(elements, edges)

    def test_participation_is_contextual_optional_nonexclusive_and_separate_from_accountability(self):
        elements = {'s': element('s', 'Stakeholder'), 'j': element('j', 'ValueStream', 'Journey'),
                    'u': element('u', 'Requirement', 'UseCase')}
        for values in ([], ['Initiator', 'Beneficiary'], ['Participant', 'Observer']):
            self.check(elements, [self.edge('s', 'j', 'Association', participationCharacteristics=values)])
        for values in (['Accountable'], ['Initiator', 'Initiator'], 'Participant'):
            with self.subTest(values=values), self.assertRaisesRegex(ValueError, 'participation characteristics'):
                self.check(elements, [self.edge('s', 'j', 'Association', participationCharacteristics=values)])
        with self.assertRaisesRegex(ValueError, 'must associate a Stakeholder'):
            self.check(elements, [self.edge('u', 'j', 'Association', participationCharacteristics=['Initiator'])])

    def test_requirement_context_checks_actual_facet_and_persisted_name(self):
        elements = {'f': element('f', 'Grouping', 'SolutionFacet', friendlyName='ConsentAndInformationSharing'),
                    'u': element('u', 'Requirement', 'UseCase', friendlyName='ConsentAndInformationSharing.VerifyConsent',
                                 naming={'solutionFacetCanonicalId': 'f', 'localName': 'Verify Consent'})}
        self.check(elements)
        changed = copy.deepcopy(elements)
        changed['u'][0]['friendlyName'] = 'Unrelated.VerifyConsent'
        with self.assertRaisesRegex(ValueError, 'naming mismatch'):
            self.check(changed)
        changed = copy.deepcopy(elements)
        changed['f'][0]['specialization'] = None
        with self.assertRaisesRegex(ValueError, 'established SolutionFacet'):
            self.check(changed)

    def test_native_nfr_direction_remains_endpoint_valid(self):
        table = c.read_json(c.ROOT / c.CONFIG / 'native-relationships.json')
        allowed = table['endpoints']['Requirement']['ValueStream']
        self.assertIn(table['keys']['Association'], allowed)
        self.assertNotIn(table['keys']['Influence'], allowed)


if __name__ == '__main__':
    unittest.main()
