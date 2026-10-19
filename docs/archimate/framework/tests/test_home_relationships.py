# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Explicit Home survives interchange; no contextual evidence infers it."""
import copy
from pathlib import Path
import sys
from types import SimpleNamespace
import unittest
from unittest import mock

BASE = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(BASE / 'tools'))
import generate_views as g
import home_relationships as h
import synchronise_baseline as s


def proposal(native='Association', directed='true', specialisation='Home', name=None):
    rel = g.ET.Element(g.tag('relationship'), identifier='test', source='behaviour', target='feature')
    rel.set(g.TYPE, native)
    if directed is not None:
        rel.set('isDirected', directed)
    if name:
        g.child(rel, 'name', name)
    if specialisation is not None:
        g.child(g.child(g.child(rel, 'properties'), 'property', propertyDefinitionRef='arbitrary-key'),
                'value', specialisation)
    return SimpleNamespace(keys={'arbitrary-key': 'Specialization'}, relationships={'test': rel})


class AuthoredHomeTests(unittest.TestCase):
    def test_property_reference_direction_and_unnamed_native_association_survive(self):
        model = proposal()
        row, = h.authored_homes(model)
        self.assertEqual(dict(identifier='test', source='behaviour', target='feature',
                              native_type='Association', isDirected=True, specialization='Home'), row)
        xml = g.ET.tostring(model.relationships['test'])
        model.relationships['test'] = g.ET.fromstring(xml)
        self.assertEqual([row], h.authored_homes(model))

    def test_native_edges_and_home_name_never_infer_home(self):
        for native in ('Realization', 'Serving', 'Flow', 'Composition', 'Association'):
            with self.subTest(native=native):
                self.assertEqual([], h.authored_homes(proposal(native, specialisation=None, name='Home')))
        self.assertEqual([], h.authored_homes(proposal(specialisation='Other')))

    def test_invalid_underlying_type_or_missing_direction_is_not_home(self):
        for model in (proposal('Realization'), proposal(directed=None), proposal(directed='false')):
            with self.assertRaisesRegex(ValueError, 'directed native Association'):
                h.authored_homes(model)

    def test_competing_specialisation_claims_require_review(self):
        model = proposal()
        props = model.relationships['test'].find('a:properties', g.N)
        props.append(copy.deepcopy(props[0]))
        with self.assertRaisesRegex(ValueError, 'Ambiguous'):
            h.authored_homes(model)

    def test_ordinary_association_and_home_remain_distinct_proposals(self):
        self.assertNotEqual(h.assertion(proposal(), proposal().relationships['test']),
                            h.assertion(proposal(specialisation=None),
                                        proposal(specialisation=None).relationships['test']))

    def test_general_cardinality_is_not_decided_by_detection(self):
        model = proposal()
        second = copy.deepcopy(model.relationships['test'])
        second.set('identifier', 'second'); second.set('target', 'other-home')
        model.relationships['second'] = second
        self.assertEqual(2, len(h.authored_homes(model)))


class BusinessHomeMigrationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.outputs, cls.b, cls.report = s.publish(reconciled=False)
        cls.data = cls.outputs[s.metadata.model_path()]

    def test_fixed_manifest_is_exhaustive_and_uncertainty_never_emits(self):
        rows = self.b.business_homes['records']
        self.assertEqual(319, len(rows))
        self.assertEqual(208, sum(r['status'] == 'normalised' for r in rows))
        self.assertEqual(93, sum(r['status'] == 'unresolved' for r in rows))
        self.assertEqual(18, sum(r['status'] == 'ambiguous' for r in rows))
        root = g.ET.fromstring(self.data)
        h.validate(self.b, root)
        for row in rows:
            if row['status'] != 'normalised':
                self.assertNotIn('relationship_identifier', row)
                self.assertFalse(root.xpath('a:relationships/a:relationship[@xsi:type="Association"]'
                                            '[@source=$source]', namespaces={**g.N, 'xsi': 'http://www.w3.org/2001/XMLSchema-instance'},
                                            source=self.b.ids[row['source_key']]))

    def test_same_source_realization_does_not_supply_migration_evidence(self):
        rows = {r['source_key']: r for r in self.b.business_homes['records']}
        self.assertEqual('ambiguous', rows['function:manage-theatre-case-progression']['status'])
        self.assertEqual('unresolved', rows['function:design-service-assurance']['status'])
        for row in rows.values():
            if row['status'] == 'normalised':
                for proof in row['evidence']:
                    self.assertIn('**Feature:', proof['allocation'])
                    self.assertIn(proof['statement'], proof['block'])
                    if row['source_type'] == 'process':
                        self.assertIn('*Governed Process*', proof['statement'])

    def test_existing_native_relationships_and_views_are_preserved(self):
        # Independent previous publication comparison, including standing/properties.
        import analyse_home_submission as a
        import diagnose_roundtrip as d
        before = d.Exchange(s.ROOT / 'docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml')
        after_root = g.ET.fromstring(self.data)
        after = SimpleNamespace(keys={p.get('identifier'): p.findtext('a:name', namespaces=g.N)
                                      for p in after_root.findall('a:propertyDefinitions/a:propertyDefinition', g.N)})
        for kind, path in (('relationships', 'a:relationships/a:relationship'),
                           ('views', 'a:views/a:diagrams/a:view')):
            current = {e.get('identifier'): e for e in after_root.findall(path, g.N)}
            for key, node in getattr(before, kind).items():
                self.assertEqual(a.signature(node, before), a.signature(current[key], after))

    def test_home_direction_target_specialisation_and_name_mutations_are_rejected(self):
        row = next(r for r in self.b.business_homes['records'] if r['status'] == 'normalised')
        for attribute, value in (('isDirected', 'false'), ('target', self.b.ids[row['source_key']]),
                                 (g.TYPE, 'Realization'), ('specialization', 'Other'), ('name', 'Home')):
            root = g.ET.fromstring(self.data)
            rel = root.find(f'a:relationships/a:relationship[@identifier="{row["relationship_identifier"]}"]', g.N)
            if attribute == 'specialization':
                rel.find('a:properties/a:property/a:value', g.N).text = value
            elif attribute == 'name':
                rel.insert(0, g.ET.Element(g.tag('name'))); rel[0].text = value
            else:
                rel.set(attribute, value)
            with self.assertRaisesRegex(ValueError, 'Home interchange'):
                h.validate(self.b, root)

    def test_specialisation_definition_cannot_be_repurposed(self):
        root = g.ET.fromstring(self.data)
        root.find('a:propertyDefinitions/a:propertyDefinition[@identifier="specialization"]/a:name', g.N).text = 'Other'
        with self.assertRaisesRegex(ValueError, 'specialisation definition'):
            h.validate(self.b, root)


class HomeReportGateTests(unittest.TestCase):
    def test_failed_or_stale_verification_cannot_generate_a_passing_report(self):
        import report_home
        release = s.load_release()
        for evidence in ({'architecture_release': release, 'status': 'FAIL'},
                         {'architecture_release': {'version': '0.0.0'}, 'status': 'PASS'}):
            with mock.patch.object(s, 'read', return_value=evidence):
                with self.assertRaisesRegex(ValueError, 'Stale/incomplete'):
                    report_home.render(Path('/tmp/home-normalisation.md'), Path('/tmp/guide.md'))

    def test_missing_required_checks_cannot_generate_a_report(self):
        import report_home
        evidence = {'architecture_release': s.load_release(), 'status': 'PASS', 'checks': {}}
        with mock.patch.object(s, 'read', return_value=evidence):
            with self.assertRaisesRegex(ValueError, 'Required Home checks missing'):
                report_home.render(Path('/tmp/home-normalisation.md'), Path('/tmp/guide.md'))


if __name__ == '__main__':
    unittest.main()
