# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Accepted-instance preservation, dependencies, identity and explicit Home."""
import copy
import hashlib
from pathlib import Path
import sys
from types import SimpleNamespace
import unittest
from unittest import mock

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'tools'))
import synchronise_baseline as s
import reconcile_108 as x
import governed_metadata as m
import home_relationships as h


@unittest.skipUnless(x.SOURCE.exists(), 'Accepted 1.0.8 capture not yet established')
class ReconciledIngestionTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.old, cls.legacy, _ = s.publish(reconciled=False)
        cls.outputs, cls.b, cls.report = s.publish(presentation=False)
        cls.root = s.g.ET.fromstring(cls.outputs[m.model_path()])
        cls.capture = x.read()

    def test_complete_accepted_population_and_valid_references(self):
        result = s.validate(self.b, self.root)
        self.assertEqual((1308, 1077, 1198), tuple(result[k] for k in ('elements', 'relationships', 'views')))
        self.assertEqual(32, self.report['reconciliation']['run_001_accepted'] +
                         self.report['reconciliation']['run_002_accepted'])

    def test_existing_identities_elements_and_unaffected_relationships_and_views_survive(self):
        old = s.g.ET.fromstring(self.old[m.model_path()])
        changed = {v['representationId'] for v in self.capture['views']}
        for path in ('a:elements/a:element', 'a:relationships/a:relationship', 'a:views/a:diagrams/a:view'):
            after = {e.get('identifier'): e for e in self.root.findall(path, s.g.N)}
            for element in old.findall(path, s.g.N):
                if element.get('identifier') not in changed:
                    left, right = copy.deepcopy(element), copy.deepcopy(after[element.get('identifier')])
                    if path.startswith('a:elements') and element.get('identifier') in self.b.home_context:
                        for node in (left, right):
                            for prop in node.findall('a:properties/a:property', s.g.N):
                                if prop.get('propertyDefinitionRef') in (m.property_id('friendlyName'), m.property_id('canonicalName')):
                                    prop.getparent().remove(prop)
                    self.assertEqual(s.g.signature(left), s.g.signature(right))

    def test_authored_view_geometry_membership_connections_and_types_are_exact(self):
        views = {v.get('identifier'): v for v in self.root.findall('a:views/a:diagrams/a:view', s.g.N)}
        for record in self.capture['views']:
            self.assertEqual(s.g.signature(x.parse(record['xml'])),
                             s.g.signature(views[record['representationId']]))

    def test_all_four_authored_homes_are_independent_of_native_realization(self):
        model = SimpleNamespace(keys={p.get('identifier'): p.findtext('a:name', namespaces=s.g.N)
                                      for p in self.root.findall('a:propertyDefinitions/a:propertyDefinition', s.g.N)},
                                relationships={r.get('identifier'): r for r in self.root.findall('a:relationships/a:relationship', s.g.N)})
        homes = h.authored_homes(model)
        expected = [r for r in self.capture['relationships'] if r.get('specialization') == 'Home']
        self.assertEqual(4, len(expected))
        self.assertEqual(212, len(homes))
        for record in expected:
            edge = model.relationships[record['representationId']]
            self.assertEqual('Association', edge.get(s.g.TYPE))
            self.assertEqual('true', edge.get('isDirected'))
            self.assertIsNone(edge.find('a:name', s.g.N))
            self.assertTrue(any(r.get(s.g.TYPE) == 'Realization' and r.get('source') == edge.get('source')
                                and r.get('target') == edge.get('target') for r in model.relationships.values()))

    def test_baseline_home_debt_remains_93_unestablished_and_18_ambiguous(self):
        rows = self.b.business_homes['records']
        self.assertEqual(93, sum(r['status'] == 'unresolved' for r in rows))
        self.assertEqual(18, sum(r['status'] == 'ambiguous' for r in rows))
        for row in rows:
            if row['status'] != 'normalised':
                self.assertFalse(self.root.xpath('a:relationships/a:relationship[@source=$source]'
                    '/a:properties/a:property[a:value="Home"]', namespaces=s.g.N,
                    source=self.b.ids[row['source_key']]))

    def test_new_identity_names_are_persisted_and_derived_without_allocation(self):
        with mock.patch.object(m.uuid, 'uuid4', side_effect=AssertionError('Publication cannot allocate')):
            fresh, _, _ = s.publish(presentation=False)
        self.assertEqual(self.outputs, fresh)
        self.assertEqual(1308, m.validate_publication(self.b, self.root)['valid_canonicalId'])
        legacy, _ = m.load(self.legacy)
        for record in self.capture['elements']:
            path = [legacy[i]['record'] for i in record['naming_context']]
            names = m.qualified_names([*path, record])
            for key, value in names.items():
                self.assertEqual(value, record[key])
            if record['type'] == 'product':
                self.assertTrue(record['canonicalName'].startswith('PRODUCT-'))

    def test_dependency_semantics_and_view_mutations_cannot_be_published(self):
        for path, field, value in (
                ('a:relationships/a:relationship', 'target', 'absent'),
                ('a:views/a:diagrams/a:view/a:node', 'x', '123456'),
                ('a:elements/a:element', s.g.TYPE, 'Product')):
            root = copy.deepcopy(self.root)
            root.find(path, s.g.N).set(field, value)
            with self.assertRaises((ValueError, s.g.ET.DocumentInvalid)):
                s.validate(self.b, root)

    def test_frozen_inputs_and_prior_review_snapshots_remain_pinned(self):
        for path, digest in self.capture['evidence_sha256'].items():
            self.assertEqual(digest, hashlib.sha256(x.evidence_bytes(path)).hexdigest())
