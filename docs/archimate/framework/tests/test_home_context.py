# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Home controls qualified names/navigation without changing architectural meaning."""
import copy
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'tools'))
import home_context as c
import governed_metadata as m
import synchronise_baseline as s


class HomeContextTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.outputs, cls.b, cls.report = s.publish(presentation=False)
        cls.root = s.g.ET.fromstring(cls.outputs[m.model_path()])
        cls.index, _ = m.load(cls.b)
        cls.index.update(cls.b.reconciliation_index)
        cls.derived = c.derive(cls.b, cls.root, cls.index)

    def test_complete_population_and_frozen_architecture_delta(self):
        result = c.audit(self.b, self.root)
        self.assertEqual(212, result['counts']['applicable'])
        self.assertEqual(212, result['counts']['friendlyName_changes'])
        self.assertEqual(212, result['counts']['canonicalName_changes'])
        self.assertEqual({'BusinessFunction': 104, 'BusinessService': 99, 'BusinessProcess': 9},
                         self.report['home_context']['by_type'])
        self.assertEqual('UNCHANGED', result['responsibility_semantics'])
        self.assertEqual(dict(unestablished=93, ambiguous=18, names_and_navigation='UNCHANGED'),
                         result['unresolved_homes'])

    def test_six_representatives_share_the_same_context_for_names_and_navigation(self):
        representatives = {'Identifier Create', 'Identifier Update', 'Identifier Verification',
                           'Verify Identity Source', 'Maintain Clinical Privileges', 'Clinical Privilege Verification'}
        found = set()
        for key, row in self.derived.items():
            record = self.index[key]['record']
            if record['name'] not in representatives:
                continue
            found.add(record['name'])
            context = [self.index[k]['record'] for k in row['semantic_context']]
            self.assertEqual(m.qualified_names([*context, record]),
                             {role: row[role] for role in ('friendlyName', 'canonicalName')})
            self.assertEqual('[Feature] ' + context[-1]['name'], row['navigation_path'][-2])
            self.assertEqual(record['canonicalId'], self.b.home_context_index[key]['record']['canonicalId'])
            if record['name'] in ('Identifier Create', 'Identifier Update', 'Identifier Verification'):
                self.assertTrue(row['friendlyName'].startswith('EntityInformationEnablement.ClientAdministration.'
                                'PersonIdentityAdministration.IdentifierCapture.'))
                self.assertIn('.FEAT-IdentifierCapture.', row['canonicalName'])
            if 'Privilege' in record['name']:
                self.assertNotEqual(record['owner_key'], context[-1].get('owner_key'))
        self.assertEqual(representatives, found)

    def test_responsibility_cannot_override_an_established_home(self):
        index = copy.deepcopy(self.index)
        for key in self.derived:
            index[key]['record']['owner_key'] = 'capability:person-identity'
        self.assertEqual(self.derived, c.derive(self.b, self.root, index))

    def test_folder_placement_cannot_establish_home_or_ancestry(self):
        root = copy.deepcopy(self.root)
        root.find('a:organizations', s.g.N).clear()
        self.assertEqual(self.derived, c.derive(self.b, root, self.index))
        for edge in list(root.find('a:relationships', s.g.N)):
            if c.h.assertion(c.model(root), edge)['specialization'] == 'Home':
                edge.getparent().remove(edge)
        self.assertEqual({}, c.derive(self.b, root, self.index))

    def test_multiple_explicit_homes_stop_even_when_targets_are_equal(self):
        root = copy.deepcopy(self.root)
        key = next(iter(self.derived))
        edge = copy.deepcopy(c.model(root).relationships[self.derived[key]['home_relationship']])
        edge.set('identifier', 'duplicate-home')
        root.find('a:relationships', s.g.N).append(edge)
        with self.assertRaisesRegex(ValueError, 'Multiple explicit Homes'):
            c.derive(self.b, root, self.index)

    def test_invalid_home_targets_direction_and_missing_or_cyclic_ancestry_stop(self):
        key = next(iter(self.derived))
        for attribute, value in (('target', key), ('target', 'absent'), ('isDirected', 'false')):
            root = copy.deepcopy(self.root)
            c.model(root).relationships[self.derived[key]['home_relationship']].set(attribute, value)
            with self.assertRaises(ValueError):
                c.derive(self.b, root, self.index)
        target = next(row['home'] for row in self.derived.values()
                      if self.index[row['home']]['record']['key'] in self.b.records)
        for owner in ('absent', self.index[target]['record']['key']):
            index = copy.deepcopy(self.index)
            index[target]['record']['owner_key'] = owner
            with self.assertRaisesRegex(ValueError, 'Home ancestry'):
                c.derive(self.b, self.root, index)

    def test_persisted_metadata_drift_and_unresolved_amendments_are_rejected_without_repair(self):
        key = next(iter(self.derived))
        for field in ('friendlyName', 'canonicalName', 'canonicalId', 'home', 'navigation_path'):
            source = m.read(c.SOURCE)
            source['resources'][key][field] = 'invalid'
            before = copy.deepcopy(source)
            with self.assertRaisesRegex(ValueError, 'metadata drift'):
                c.amended_index(self.index, self.derived, source)
            self.assertEqual(before, source)
        source = m.read(c.SOURCE)
        source['resources']['unresolved'] = source['resources'][key]
        with self.assertRaisesRegex(ValueError, 'Missing/extra'):
            c.amended_index(self.index, self.derived, source)

    def test_audit_rejects_identity_display_responsibility_view_and_navigation_changes(self):
        key = next(iter(self.derived))
        mutations = (
            (f'a:elements/a:element[@identifier="{key}"]/a:name', 'text', 'renamed'),
            (f'a:elements/a:element[@identifier="{key}"]//a:property[@propertyDefinitionRef="governed-canonical-id"]/a:value', 'text', '0' * 32),
            ('a:views/a:diagrams/a:view/a:node', 'x', '123456'),
            ('a:relationships/a:relationship', 'target', 'absent'))
        for path, field, value in mutations:
            root = copy.deepcopy(self.root)
            node = root.find(path, s.g.N)
            self.assertIsNotNone(node)
            if field == 'text':
                node.text = value
            else:
                node.set(field, value)
            with self.assertRaises(ValueError):
                c.audit(self.b, root)

    def test_historical_names_and_ancestor_standing_remain_source_provenance(self):
        source = m.read(c.SOURCE)
        for key, row in self.derived.items():
            self.assertEqual({role: self.index[key]['record'][role] for role in ('friendlyName', 'canonicalName')},
                             source['resources'][key]['previous_names'])
            for ancestor in row['semantic_context']:
                self.assertEqual(self.index[ancestor]['record'], self.b.home_context_index[ancestor]['record'])
        self.assertEqual(1308, m.validate_publication(self.b, self.root)['valid_canonicalId'])
