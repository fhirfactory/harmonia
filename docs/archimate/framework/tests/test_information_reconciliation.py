# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Accepted IA scope, identity persistence, pure replay and preservation gates."""
import copy
import json
from pathlib import Path
import sys
import tempfile
import unittest
from unittest import mock
import uuid

BASE = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(BASE / 'tools'))
from reconciliation import information as r

g, naming = r.g, r.naming


class InformationReconciliationTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(prefix='harmonia-reconciliation-', dir='/tmp')
        self.addCleanup(self.temp.cleanup)
        self.root = g.ET.Element(g.tag('model'), nsmap={None: g.NS, 'xsi': g.XSI}, identifier='test-model')
        g.child(self.root, 'name', 'NON-GOVERNED reconciliation regression fixture')
        g.child(self.root, 'elements')
        g.child(self.root, 'relationships')
        org = g.child(self.root, 'organizations')
        vf = g.child(org, 'item', identifier='authored-view-folder')
        g.child(vf, 'label', 'Views')
        g.child(self.root, 'propertyDefinitions')
        for key in r.metadata.ROLES:
            g.child(g.child(self.root.find('a:propertyDefinitions', g.N), 'propertyDefinition',
                            identifier='test-' + key, type='string'), 'name', key)
        self.element('old', 'BusinessObject', 'Encounter BO', 'a' * 32)
        self.element('rename', 'BusinessObject', 'Identifier BO', 'b' * 32)
        self.element('held', 'DataObject', 'Context Topic', 'c' * 32)
        self.edge('rename', 'held', 'retained-edge').set(g.TYPE, 'Association')
        diagrams = g.child(g.child(self.root, 'views'), 'diagrams')
        view = g.child(diagrams, 'view', identifier='existing-view', **{g.TYPE: 'Diagram'})
        g.child(view, 'name', 'Preserve this authored presentation')
        g.child(view, 'documentation', 'Authored **Markdown**\nwith retained whitespace.')
        g.child(view, 'node', identifier='visual', elementRef='rename', x='19', y='31', w='100', h='50',
                **{g.TYPE: 'Element'})
        g.child(vf, 'item', identifier='authored-view-ref', identifierRef='existing-view')
        self.baseline = self.model('baseline')
        self.control = self.baseline
        self.root.find("a:elements/a:element[@identifier='old']", g.N).set('identifier', 'rebound')
        node = self.node('rebound')
        node.set(g.TYPE, 'DataObject')
        node.find('a:name', g.N).text = '[Topic] Encounter'
        naming.set_property(self.root, node, 'Specialization', 'Topic')
        self.node('rename').find('a:name', g.N).text = '[BusinessObject] Identifier'
        self.element('new', 'DataObject', '[Topic] Care', specialization='Topic')
        self.edge('new', 'rebound', 'edge')
        g.child(view, 'node', identifier='added-visual', elementRef='new', x='3', y='7', w='200', h='90',
                **{g.TYPE: 'Element'})
        naming.set_property(self.root, self.root, naming.MARKER, 'PROPOSED; NOT ACCEPTED')
        self.candidate = self.model('candidate')
        self.scope = dict(elements=[dict(id='new', cr='CR-001', operation='add'),
                                   dict(id='rebound', cr='CR-002', operation='evolve', baseline_id='old'),
                                   dict(id='rename', cr='CR-003', operation='rename', baseline_id='rename')],
                          relationships=[dict(id='edge', cr='CR-004')],
                          views=[dict(id='existing-view', cr='CR-005')],
                          held=[dict(id='held', cr='CR-006')])
        self.authority = dict(review_identity='synthetic-test-only', decision={'test': True})
        self.allocator = mock.Mock(return_value=uuid.UUID('11111111-1111-4111-8111-111111111111'))

    def node(self, rid):
        return self.root.find(f"a:elements/a:element[@identifier='{rid}']", g.N)

    def element(self, rid, native, name, identity=None, specialization=None):
        node = g.child(self.root.find('a:elements', g.N), 'element', identifier=rid, **{g.TYPE: native})
        g.child(node, 'name', name)
        g.child(node, 'documentation', 'Keep this architectural definition.')
        for key, value in dict(canonicalId=identity, Specialization=specialization).items():
            if value is not None:
                naming.set_property(self.root, node, key, value)
        return node

    def edge(self, source, target, rid):
        return g.child(self.root.find('a:relationships', g.N), 'relationship', identifier=rid,
                       source=source, target=target, **{g.TYPE: 'Composition'})

    def model(self, name='changed'):
        path = Path(self.temp.name) / (name + '.xml')
        path.write_bytes(g.serialize(self.root))
        return r.diagnostic.Exchange(path)

    def capture(self):
        return r.capture(self.baseline, self.control, self.candidate, self.scope, self.authority,
                         allocator=self.allocator)

    def test_capture_allocates_only_approved_new_resource_and_records_rebinding(self):
        source = self.capture()
        self.allocator.assert_called_once_with()
        rows = {row['representationId']: row for row in source['elements']}
        self.assertEqual(1, source['allocation']['count'])
        self.assertEqual('a' * 32, rows['rebound']['canonicalId'])
        self.assertEqual('old', rows['rebound']['previous']['representationId'])
        self.assertEqual('Care.Encounter', rows['rebound']['friendlyName'])
        self.assertEqual('TOPIC-Care.TOPIC-Encounter', rows['rebound']['canonicalName'])
        self.assertEqual('11111111111141118111111111111111', rows['new']['canonicalId'])

    def test_persisted_replay_never_allocates_and_preserves_views_held_and_care_leaf(self):
        source = json.loads(r.encoded(self.capture()))
        with mock.patch.object(r.metadata, 'allocate_missing', side_effect=AssertionError('Replay allocated')):
            outputs = r.render(self.candidate, source, self.scope, {})
            self.assertEqual(outputs, r.render(self.candidate, source, self.scope, {}))
        parsed = g.ET.fromstring(outputs['reconciled.xml'])
        report = json.loads(outputs['validation.json'])
        self.assertEqual('PASS', report['status'])
        self.assertEqual(0, report['identities_allocated_by_replay'])
        self.assertFalse(report['release_created'])
        for path in ('a:views', "a:organizations/a:item[a:label='Views']"):
            self.assertEqual(g.ET.tostring(self.candidate.root.find(path, g.N)),
                             g.ET.tostring(parsed.find(path, g.N)))
        care, = parsed.xpath("a:organizations/a:item[a:label='Application']/a:item[a:label='InformationMap']/a:item[a:label='[Topic] Care']", namespaces=g.N)
        self.assertEqual(['new'], care.xpath('a:item/@identifierRef', namespaces=g.N))
        encounter, = care.xpath("a:item[a:label='[Topic] Encounter']", namespaces=g.N)
        self.assertEqual(['rebound'], encounter.xpath('a:item/@identifierRef', namespaces=g.N))
        self.assertEqual(g.signature(self.candidate.elements['held']),
                         g.signature(parsed.find("a:elements/a:element[@identifier='held']", g.N)))

    def test_unaccepted_element_relationship_or_view_change_stops_before_allocation(self):
        for mode in ('element', 'relationship', 'view'):
            scope = copy.deepcopy(self.scope)
            if mode == 'element':
                scope['elements'].pop(0)
            elif mode == 'relationship':
                scope['relationships'].clear()
            else:
                scope['views'].clear()
            with self.subTest(mode=mode), self.assertRaises(ValueError):
                r.capture(self.baseline, self.control, self.candidate, scope, self.authority, self.allocator)
        self.allocator.assert_not_called()

    def test_held_authored_change_and_lost_established_identity_are_rejected(self):
        for key, value in (('canonicalId', None), ('canonicalId', 'e' * 32)):
            naming.set_property(self.root, self.node('held'), key, value)
            with self.assertRaisesRegex(ValueError, 'canonical correlation'):
                r.capture(self.baseline, self.control, self.model(), self.scope, self.authority, self.allocator)
        naming.set_property(self.root, self.node('held'), 'canonicalId', 'c' * 32)
        self.node('held').find('a:documentation', g.N).text = 'Unaccepted edit'
        with self.assertRaisesRegex(ValueError, 'Unaccepted authored element change'):
            r.capture(self.baseline, self.control, self.model(), self.scope, self.authority, self.allocator)
        self.allocator.assert_not_called()

    def test_unresolved_containment_or_classification_stops_before_allocation(self):
        extra = self.edge('rename', 'rebound', 'competing')
        scope = copy.deepcopy(self.scope)
        scope['relationships'].append(dict(id='competing', cr='CR-007'))
        with self.assertRaisesRegex(ValueError, 'containment'):
            r.capture(self.baseline, self.control, self.model(), scope, self.authority, self.allocator)
        extra.getparent().remove(extra)
        naming.set_property(self.root, self.node('new'), 'harmoniaType', 'Topic')
        naming.set_property(self.root, self.node('new'), 'Specialization', None)
        with self.assertRaisesRegex(ValueError, 'Unresolved approved naming'):
            r.capture(self.baseline, self.control, self.model(), self.scope, self.authority, self.allocator)
        self.allocator.assert_not_called()

    def test_collision_and_ambiguous_claims_never_make_a_source(self):
        self.allocator.return_value = uuid.UUID('a' * 32)
        with self.assertRaisesRegex(ValueError, 'collision'):
            self.capture()
        naming.set_property(self.root, self.node('new'), 'canonicalId', 'b' * 32)
        self.candidate = self.model()
        with self.assertRaisesRegex(ValueError, 'canonical correlation'):
            self.capture()

    def test_source_identity_metadata_semantics_and_scope_tampering_are_rejected(self):
        source = self.capture()
        for mode in ('identity', 'metadata', 'semantics', 'property-definitions', 'duplicate', 'operation'):
            broken = copy.deepcopy(source)
            row = next(row for row in broken['elements'] if row['representationId'] == 'rebound')
            if mode == 'identity':
                row['xml'] = row['xml'].replace('a' * 32, 'f' * 32)
                row['canonicalId'] = 'f' * 32
            elif mode == 'metadata':
                row['friendlyName'] = 'Wrong.Context'
            elif mode == 'semantics':
                row['xml'] = row['xml'].replace('Keep this architectural definition.', 'Changed meaning')
            elif mode == 'property-definitions':
                broken['property_definitions_sha256'] = '0' * 64
            elif mode == 'duplicate':
                broken['elements'].append(copy.deepcopy(row))
            else:
                row['operation'] = 'add'
            with self.subTest(mode=mode), self.assertRaises(ValueError):
                r.render(self.candidate, broken, self.scope, {})

    def test_scope_refuses_held_mutation_unknown_operations_and_duplicate_crs(self):
        for mode in ('held', 'operation', 'duplicate'):
            scope = copy.deepcopy(self.scope)
            if mode == 'held':
                scope['elements'].append(dict(id='held', cr='CR-007', operation='rename', baseline_id='held'))
            elif mode == 'operation':
                scope['elements'][0]['operation'] = 'guess'
            else:
                scope['views'][0]['cr'] = 'CR-001'
            with self.subTest(mode=mode), self.assertRaises(ValueError):
                r.scope_index(scope)

    def test_establishment_manifest_rejects_changed_new_identity_or_authority(self):
        source = r.encoded(self.capture())
        plan = dict(source_path='synthetic.json', authority=self.authority, release_authority={'version': 'test'})
        plan_bytes = r.encoded(plan)
        manifest = dict(source_path=plan['source_path'], source_sha256=r.sha(source),
                        plan_sha256=r.sha(plan_bytes), instructions=self.authority,
                        release_authority=plan['release_authority'])
        r.validate_establishment(manifest, source, plan_bytes, plan)
        with self.assertRaisesRegex(ValueError, 'Established accepted source changed'):
            r.validate_establishment(manifest, source.replace(b'11111111111141118111111111111111', b'd' * 32), plan_bytes, plan)
        with self.assertRaisesRegex(ValueError, 'authority drift'):
            r.validate_establishment(manifest, source, plan_bytes + b' ', plan)


if __name__ == '__main__':
    unittest.main()
