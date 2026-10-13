# Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later.
"""Metamodel conformance and reconciliation regressions; fixtures allocate no architecture IDs."""

import copy
import importlib.util
import json
import tempfile
import unittest
from collections import Counter
from pathlib import Path

TOOL_PATH = Path(__file__).resolve().parents[1] / 'rads_business_architecture.py'
spec = importlib.util.spec_from_file_location('rads_business_architecture', TOOL_PATH)
rads = importlib.util.module_from_spec(spec)
spec.loader.exec_module(rads)


def syntax_fixture():
    """Artificial fully resolved graph tests the grammar; it is never written to RADS."""
    model = copy.deepcopy(rads.load_json(rads.DEFAULT_MODEL))
    model['elements'] = []
    model['views'] = []
    model['catalogue_contexts'] = []
    model['relationships'] = []
    for name, kind, canonical, owner, parent, tier in [
        ('root', 'capability', 'CT1-TEST', None, None, 'CT1'),
        ('child', 'capability', 'CT1-TEST.CT2-TEST', None, 'root', 'CT2'),
        ('leaf', 'capability', 'CT1-TEST.CT2-TEST.CT3-TEST', None, 'child', 'CT3'),
        ('feature', 'feature', 'CT1-TEST.CT2-TEST.CT3-TEST.FEAT-TEST', 'leaf', None, 'FT'),
        ('function', 'function', 'CT1-TEST.CT2-TEST.CT3-TEST.FEAT-TEST.FN-TEST', 'feature', None, None),
        ('service', 'service', 'CT1-TEST.SV-TEST', 'root', None, None),
        ('process', 'process', 'CT1-TEST.CT2-TEST.PR-TEST', 'child', None, None),
    ]:
        e = {'key': name, 'name': name, 'type': kind, 'status': 'established',
             'canonical_id': canonical, 'ancestry_status': 'established', 'unresolved': [],
             'sources': [{'source': 'metamodel', 'line': 1, 'text': f'Syntax fixture {canonical}', 'anchor': None}]}
        if kind in ('feature', 'capability'):
            e['tier'] = tier
        if kind == 'capability':
            e['category'] = 'business'
        if owner:
            e['owner'] = owner
        if parent:
            e['capability_parent'] = parent
        model['elements'].append(e)
    return model


class ValidatorTests(unittest.TestCase):
    def setUp(self):
        self.model = syntax_fixture()

    def errors(self):
        return rads.validate(self.model, check_sources=False)[0]

    def assert_invalid(self, fragment):
        self.assertTrue(any(fragment in e for e in self.errors()), self.errors())

    def test_complete_typed_ancestry_and_direct_behaviour_anchoring(self):
        self.assertEqual([], self.errors())
        # A Feature may specialise CT1 or CT2 without materialising lower tiers.
        feature, function = self.model['elements'][3:5]
        for parent, prefix in [('root', 'CT1-TEST'), ('child', 'CT1-TEST.CT2-TEST')]:
            with self.subTest(parent=parent):
                feature['owner'] = parent
                feature['canonical_id'] = prefix + '.FEAT-TEST'
                function['canonical_id'] = feature['canonical_id'] + '.FN-TEST'
                self.assertEqual([], self.errors())
        # A Function may instead be owned directly at CT1, CT2 or CT3.
        for owner in self.model['elements'][:3]:
            with self.subTest(tier=owner['tier']):
                function['owner'] = owner['key']
                function['canonical_id'] = owner['canonical_id'] + '.FN-TEST'
                self.assertEqual([], self.errors())

    def test_duplicate_canonical_id(self):
        other = copy.deepcopy(self.model['elements'][-1])
        other.update(key='duplicate-process', name='duplicate-process')
        self.model['elements'].append(other)
        self.assert_invalid('identifier collision')

    def test_duplicate_alias_and_canonical_alias_collision(self):
        function, service = self.model['elements'][4:6]
        alias = 'CT1-OLD.FN-TEST'
        function['id_aliases'] = [alias]
        other = copy.deepcopy(function)
        other.update(key='other', name='other', canonical_id='CT1-TEST.FN-OTHER', owner='root')
        other['id_aliases'] = [alias]
        self.model['elements'].append(other)
        self.assert_invalid('identifier collision')
        self.model['elements'].pop()
        function['id_aliases'] = [service['canonical_id']]
        self.assert_invalid('identifier collision')

    def test_alias_resolves_directly_after_multiple_relocations(self):
        function = self.model['elements'][4]
        function['id_aliases'] = ['CT1-OLD.FN-TEST', 'CT1-OLD.CT2-OLD.FN-TEST']
        self.assertEqual([], self.errors())
        for alias in function['id_aliases']:
            self.assertIs(function, rads.resolve(self.model, alias))

    def test_historical_capability_alias_can_retain_previous_tier(self):
        self.model['elements'][2]['id_aliases'] = ['CT1-OLD.CT2-OLD']
        self.assertEqual([], self.errors())

    def test_invalid_token_types_and_skipped_tiers(self):
        for identifier in ['CT01-TEST', 'CT1-TEST.CT3-TEST', 'CT1-TEST.FT-TEST',
                           'CT1-TEST.SV-TEST.FN-TEST', 'CT1-TEST.CT2-', 'CT1-TEST.FN-two words']:
            with self.subTest(identifier=identifier):
                self.model['elements'][4]['canonical_id'] = identifier
                self.assert_invalid('token grammar')

    def test_wrong_element_suffix(self):
        self.model['elements'][4]['canonical_id'] = 'CT1-TEST.CT2-TEST.CT3-TEST.FEAT-TEST.SV-OTHER'
        self.assert_invalid('terminal token')

    def test_missing_established_ancestor(self):
        self.model['elements'].pop(1)
        self.assert_invalid('missing established ancestor')

    def test_canonical_id_must_match_current_owner(self):
        self.model['elements'][4]['owner'] = 'root'
        self.assert_invalid('current owning responsibility context')

    def test_orphaned_feature_and_behaviour(self):
        for index in [3, 4, 5, 6]:
            with self.subTest(type=self.model['elements'][index]['type']):
                self.model = syntax_fixture()
                self.model['elements'][index]['owner'] = 'missing'
                self.assert_invalid('responsibility context (orphan)')

    def test_feature_is_ft_and_view_is_not_ancestor(self):
        self.model['elements'][3]['tier'] = 'CT3'
        self.assert_invalid('retain FT')
        self.model = syntax_fixture()
        self.model['views'] = [{'key': 'view', 'name': 'view', 'kind': 'business_enabling_view',
                               'sources': self.model['elements'][0]['sources']}]
        self.model['elements'][1]['capability_parent'] = 'view'
        self.assert_invalid('views/contexts cannot be ancestors')

    def test_unresolved_architecture_is_accepted_without_making_an_orphan(self):
        root = self.model['elements'][0]
        root.update(tier=None, canonical_id=None, ancestry_status='unresolved',
                    unresolved=['tier', 'ancestry', 'canonical_id', 'root_status', 'code_allocation'])
        fn = self.model['elements'][4]
        fn.update(owner='root', canonical_id=None, ancestry_status='unresolved',
                  unresolved=['ancestry', 'canonical_id', 'code_allocation', 'feature_association'])
        self.model['elements'] = [root, fn]
        errors, unresolved = rads.validate(self.model, check_sources=False)
        self.assertEqual([], errors)
        self.assertTrue(unresolved)

    def test_unresolved_and_established_identity_cannot_be_collapsed(self):
        self.model['elements'][1]['unresolved'] = ['ancestry']
        self.assert_invalid('contradicts unresolved')

    def test_misspelled_model_reference_is_invalid_even_if_relationship_unresolved(self):
        self.model['relationships'] = [{'type': 'realises_feature', 'from': 'function', 'to': 'typo',
                                        'status': 'unresolved', 'reason': 'Not adjudicated.',
                                        'sources': self.model['elements'][0]['sources']}]
        self.assert_invalid('unresolved model reference')

    def test_feature_relationship_cannot_transfer_ownership(self):
        self.model['relationships'] = [{'type': 'realises_feature', 'from': 'function', 'to': 'feature',
                                        'status': 'established', 'sources': self.model['elements'][0]['sources']}]
        self.model['elements'][4]['owner'] = 'root'
        self.assert_invalid('crosses owning responsibility')

    def test_ancestry_cycle_is_invalid(self):
        self.model['elements'][0]['capability_parent'] = 'leaf'
        self.assert_invalid('ancestry cycle')

    def test_schema_rejects_unknown_fields_and_alias_chains(self):
        self.model['elements'][4]['id_aliases'] = [{'id': 'CT1-OLD.FN-TEST', 'target': 'another-alias'}]
        self.assert_invalid('expected string')
        self.model = syntax_fixture()
        self.model['elements'][4]['invented_state'] = 'approved'
        self.assert_invalid('unexpected property')

    def test_schema_and_json_cannot_silently_drop_validation(self):
        self.assertTrue(rads.schema_errors({}, {'allOf': []}))
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / 'bad.json'
            path.write_text('{"key":1,"key":2}')
            with self.assertRaisesRegex(ValueError, 'duplicate JSON property'):
                rads.load_json(path)

    def test_duplicate_capability_names_are_invalid(self):
        self.model['elements'][1]['name'] = 'root'
        self.assert_invalid('duplicate Capability canonical name')

    def test_feature_name_uniqueness_at_established_root(self):
        feature = copy.deepcopy(self.model['elements'][3])
        feature.update(key='second-feature', canonical_id='CT1-TEST.FEAT-SECOND', owner='root')
        self.model['elements'].append(feature)
        self.assert_invalid('duplicate Feature name within established CT1')


class CaptureReconciliationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.model = rads.load_json(rads.DEFAULT_MODEL)

    def test_capture_matches_authoritative_source_fingerprints_and_locators(self):
        errors, unresolved = rads.validate(self.model)
        self.assertEqual([], errors)
        self.assertTrue(unresolved)

    def test_current_capture_inventory_without_speculative_ids_or_tiers(self):
        elements = self.model['elements']
        self.assertEqual({'capability': 100, 'feature': 137, 'function': 147, 'service': 133, 'process': 19},
                         dict(Counter(e['type'] for e in elements)))
        self.assertEqual({'business': 24, 'business_enabling': 76},
                         dict(Counter(e['category'] for e in elements if e['type'] == 'capability')))
        self.assertTrue(all(e['canonical_id'] is None for e in elements))
        self.assertTrue(all(e.get('tier') is None for e in elements if e['type'] == 'capability'))
        self.assertEqual(69, sum(c['element_status'] == 'unresolved' for c in self.model['catalogue_contexts']))

    def test_all_137_feature_spellings_and_identifiers_are_retained(self):
        expected = {f'FEAT-{view}-{i:02}' for view, total in [('EM', 31), ('SA', 29), ('SD', 20), ('HSO', 29), ('ISE', 28)]
                    for i in range(1, total + 1)}
        actual = {e['local_id'] for e in self.model['elements'] if e['type'] == 'feature'}
        self.assertEqual(expected, actual)
        feature = rads.resolve(self.model, 'FEAT-EM-01')
        self.assertEqual('Identifier Resolution', feature['name'])
        self.assertEqual('FT', feature['tier'])
        self.assertIsNone(feature['canonical_id'])
        self.assertEqual('Person Identity', rads.resolve(self.model, feature['owner'])['name'])
        with self.assertRaises(ValueError):
            rads.resolve(self.model, 'FEAT-EM01')

    def test_individual_care_has_exactly_six_documented_children_and_no_ct3(self):
        parent = rads.resolve(self.model, 'Individual Care Delivery')
        children = [rads.resolve(self.model, r['to']) for r in self.model['relationships']
                    if r['type'] == 'capability_decomposition' and r['from'] == parent['key']]
        self.assertEqual(['Individual Health Assessment', 'Clinical Determination', 'Individual Care Planning',
                          'Clinical Intervention', 'Individual Health Monitoring', 'Individual Care Review'],
                         [e['name'] for e in children])
        self.assertTrue(all(e['documented_tier_label'] == 'L2' and e['tier'] is None for e in children))
        self.assertFalse(any(e.get('tier') == 'CT3' for e in self.model['elements']))

    def test_client_person_feature_partial_chain_does_not_allocate_complete_ancestry(self):
        person = rads.resolve(self.model, 'Person Identity')
        self.assertEqual(rads.resolve(self.model, 'Client Administration')['key'], person['capability_parent'])
        self.assertEqual('unresolved', person['ancestry_status'])
        self.assertIsNone(person['tier'])

    def test_unestablished_feature_associations_remain_outside_approved_graph(self):
        fn = rads.resolve(self.model, 'Route Acute Critical Result Alert')
        edges = [r for r in self.model['relationships'] if r['from'] == fn['key'] and r['type'] == 'realises_feature']
        self.assertEqual(['unresolved'], [r['status'] for r in edges])
        self.assertEqual('Acute Care', rads.resolve(self.model, fn['owner'])['name'])
        self.assertFalse(any(e['type'] == 'feature' and e['name'] == 'Order Result Association' for e in self.model['elements']))
        self.assertTrue('feature_association' in rads.resolve(self.model, 'Associate Order Outcome')['unresolved'])

    def test_existing_communication_decomposition_is_preserved_without_redesign(self):
        context = next(c for c in self.model['catalogue_contexts'] if c['name'] == 'Boundary Protocol Mediation')
        self.assertEqual(rads.resolve(self.model, 'Health Information Communication')['key'], context['container'])
        self.assertEqual('unresolved', context['element_status'])
        self.assertEqual(context['key'], rads.resolve(self.model, 'FEAT-ISE-10')['catalogue_context'])

    def test_aliases_and_qualification_access_distinction_are_preserved(self):
        qualification = rads.resolve(self.model, 'Clinical Credential Management')
        self.assertEqual('Clinical Qualification Management', qualification['name'])
        self.assertNotEqual(qualification['key'], rads.resolve(self.model, 'Credential Management')['key'])
        self.assertEqual('Manage Ward Capacity', rads.resolve(self.model, 'Coordinate Ward Operational State')['name'])
        self.assertEqual('Manage Outpatient Capacity', rads.resolve(self.model, 'Coordinate Outpatient Session Utilisation')['name'])
        self.assertFalse(any(e.get('id_aliases') for e in self.model['elements']))

    def test_semantic_qualifications_and_reported_conflict_are_preserved(self):
        mobile = rads.resolve(self.model, 'FEAT-HSO-16')
        self.assertEqual('Mobile Worker Task Dispatch', mobile['name'])
        self.assertTrue(any('does not thereby determine or manage clinical work' in ref['text'] for ref in mobile['sources']))
        self.assertNotIn('Mobile Work Task Management Integration', mobile.get('name_aliases', []))
        collaboration = rads.resolve(self.model, 'Coordinate Care-Team Collaboration')
        self.assertIn('clinical handovers', collaboration['summary'])
        self.assertIn('semantic_boundary_conflict', collaboration['unresolved'])
        self.assertTrue(any('manage clinical handover' in ref['text'] for ref in collaboration['sources']))

    def test_refactor_impact_includes_owned_features_functions_services_processes(self):
        affected = rads.impact(self.model, 'Person Identity')['affected_elements']
        names = {e['name'] for e in affected}
        self.assertTrue({'Person Identity', 'Identifier Resolution', 'Resolve Person Identifier',
                         'Person Identifier Resolution', 'Governed Person Identity Correction Process'} <= names)

    def test_contextual_diagram_keeps_uncertainty_visible(self):
        diagram = rads.mermaid(self.model, 'Service Delivery')
        self.assertIn('CT unresolved', diagram)
        self.assertIn('UNRESOLVED: realises_feature', diagram)
        self.assertIn('Canonical ID unresolved', diagram)
        self.assertNotIn('CT3-', diagram)

    def test_source_drift_is_invalid_capture_evidence_not_architectural_rejection(self):
        model = copy.deepcopy(self.model)
        model['sources']['business']['sha256'] = '0' * 64
        errors, _ = rads.validate(model)
        self.assertTrue(any('source drift' in e for e in errors))


if __name__ == '__main__':
    unittest.main()
