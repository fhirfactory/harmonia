# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Initial IA publication cannot change established semantics or invent relations."""
import collections
import copy
from pathlib import Path
import sys
import unittest
from unittest import mock

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'tools'))
import generate_views as g
import governed_metadata as m
import information_population as ia
import synchronise_baseline as s


@unittest.skipUnless(ia.active(), 'Initial IA publication is not active')
class InformationPopulationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.outputs, cls.b, cls.report = s.publish(information=False)
        cls.data = ia.load()
        cls.old = g.parse(m.ROOT / cls.data['originating_model']['path'])
        cls.b.information_before = cls.old
        cls.b.information_index = ia.descriptors(cls.data)
        cls.root = g.parse(m.model_path())

    def test_population_has_native_types_unique_names_and_no_inferred_counterparts(self):
        result = ia.validate(self.b, self.root)
        self.assertEqual((1362, 1077, 1198), tuple(result[k] for k in ('elements', 'relationships', 'views')))
        self.assertEqual({'BusinessObject': 50, 'DataObject': 4},
                         collections.Counter(e['archimateType'] for e in self.data['elements']))
        self.assertEqual({'Context Topic', 'Pragma Topic', 'Pylai.Diadromí Topic', 'PetasosParcel Topic'},
                         {e['displayName'] for e in self.data['elements'] if e['archimateType'] == 'DataObject'})
        names = {e['displayName'] for e in self.data['elements']}
        self.assertNotIn('Endpoint BO', names)
        self.assertNotIn('Organization Topic', names)
        self.assertIn('Healthcare Organisation BO', names)
        self.assertIn('Referral Outcome BO', names)
        self.assertNotIn('Referral BO', names)
        self.assertNotIn('ReportedTask BO', names)

    def test_all_existing_semantics_and_views_are_exact_and_no_ia_edges_exist(self):
        for path in ('a:elements/a:element', 'a:relationships/a:relationship', 'a:views/a:diagrams/a:view'):
            after = {e.get('identifier'): e for e in self.root.findall(path, g.N)}
            for old in self.old.findall(path, g.N):
                self.assertEqual(g.signature(old), g.signature(after[old.get('identifier')]))
        ids = set(self.b.information_index)
        self.assertFalse(any(r.get('source') in ids or r.get('target') in ids
                             for r in self.root.findall('a:relationships/a:relationship', g.N)))

    def test_generated_metadata_is_source_owned_and_adjudicated_names_are_complete(self):
        for row in self.data['elements']:
            e = self.root.find("a:elements/a:element[@identifier='" + row['representationId'] + "']", g.N)
            props = s.prop_values(e)
            self.assertEqual([row['canonicalId']], props[m.property_id('canonicalId')])
            self.assertEqual(row['displayName'], e.findtext('a:name', namespaces=g.N))
            self.assertEqual([row['canonicalName']], props[m.property_id('canonicalName')])
            self.assertNotIn(m.property_id('harmoniaType'), props)
            self.assertEqual({}, row['metadata_stops'])
            self.assertEqual(m.component(row['displayName']), row['friendlyName'])
            prefix = 'BO-' if row['archimateType'] == 'BusinessObject' else 'TOPIC-'
            self.assertEqual(prefix + row['friendlyName'], row['canonicalName'])

    def test_naming_completion_changes_only_the_authorised_source_fields(self):
        import gzip
        import json
        original = json.loads(gzip.decompress((ia.EVIDENCE / 'allocation/information-architecture.json.gz').read_bytes()))
        self.assertEqual(ia.complete_names(original), self.data)
        changed = copy.deepcopy(self.data)
        changed['elements'][0]['canonicalName'] = 'TOPIC-Wrong'
        with self.assertRaisesRegex(ValueError, 'canonicalName drift'):
            m.validate_records(ia.descriptors(changed))
        del changed['elements'][0]['canonicalName']
        with self.assertRaisesRegex(ValueError, 'canonicalName drift'):
            m.validate_records(ia.descriptors(changed))

    def test_deterministic_replay_has_no_identity_allocation(self):
        # Independent base reconstruction plus the persisted overlay must produce
        # the exact published bytes, even when the UUID allocator cannot execute.
        with mock.patch.object(m.uuid, 'uuid4', side_effect=AssertionError('No publication allocation')):
            outputs, _, _ = ia.overlay(self.outputs, self.b, self.report)
        self.assertEqual(m.model_path().read_bytes(), outputs[m.model_path()])

    def test_ambiguous_names_are_rejected_without_renaming_or_merging(self):
        data = copy.deepcopy(self.data)
        data['elements'][0]['displayName'] = self.old.findtext('a:elements/a:element/a:name', namespaces=g.N)
        with self.assertRaisesRegex(ValueError, 'collision'):
            ia.project(self.old, data)

    def test_changes_to_existing_semantics_views_or_partial_metadata_are_rejected(self):
        cases = [('a:relationships/a:relationship', 'target', 'missing'),
                 ('a:views/a:diagrams/a:view/a:node', 'x', '98765'),
                 ('a:elements/a:element', g.TYPE, 'DataObject')]
        for path, field, value in cases:
            with self.subTest(path=path):
                changed = copy.deepcopy(self.root)
                changed.find(path, g.N).set(field, value)
                with self.assertRaises((ValueError, g.ET.DocumentInvalid)):
                    ia.validate(self.b, changed)

    def test_navigation_is_flat_and_physical_map_is_empty(self):
        import publish_navigation as nav
        homes = nav.navigation_homes(self.root)
        for row in self.data['elements']:
            self.assertEqual([ia.PATHS[row['archimateType']]], homes[row['representationId']])
        for path in ia.EMPTY_PATHS:
            self.assertEqual([], ia.folder(copy.deepcopy(self.root), path).findall('a:item', g.N))
        old_ids = {e.get('identifier') for e in self.old.iter() if e.get('identifier')}
        new_ids = {e.get('identifier') for e in self.root.iter() if e.get('identifier')}
        self.assertTrue(old_ids <= new_ids)

    def test_illegal_ia_edge_and_technology_artifact_are_rejected(self):
        for kind in ('relationship', 'artifact'):
            changed = copy.deepcopy(self.root)
            row = self.data['elements'][0]
            if kind == 'relationship':
                g.child(changed.find('a:relationships', g.N), 'relationship', identifier='unauthorised-ia-edge',
                        source=row['representationId'], target=self.data['elements'][1]['representationId'],
                        **{g.TYPE: 'Association'})
            else:
                e = changed.find("a:elements/a:element[@identifier='" + row['representationId'] + "']", g.N)
                e.set(g.TYPE, 'Artifact')
            with self.subTest(kind=kind), self.assertRaises((ValueError, g.ET.DocumentInvalid)):
                ia.validate(self.b, changed)
