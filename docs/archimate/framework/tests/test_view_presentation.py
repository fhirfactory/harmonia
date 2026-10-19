# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Generated geometry cannot change architecture or protected presentation."""
import copy
import hashlib
from pathlib import Path
import sys
from types import SimpleNamespace
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'tools'))
import synchronise_baseline as s
import governed_metadata as m
import view_presentation as p


class ViewPresentationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        outputs, cls.b, cls.report = s.publish()
        cls.root = s.g.ET.fromstring(outputs[m.model_path()])
        cls.before = cls.b.view_presentation_before
        cls.scope = cls.b.presentation_scope

    def test_authorised_population_and_independent_semantic_preservation(self):
        self.assertEqual(s.load_release(), self.scope['architecture_release'])
        result = p.audit(self.before, self.root, self.scope)
        self.assertEqual((231, 61, 29, 4, 62, 3), tuple(result[k] if k != 'oversized_preserved'
            else len(result[k]) for k in ('containment_views_validated', 'business_views_validated',
            'known_stale_connections_validated', 'protected_authored_views',
            'historical_candidates_preserved', 'oversized_preserved')))
        self.assertEqual('EXACT', result['semantic_identity_metadata_and_navigation'])
        self.assertEqual('BYTE_IDENTICAL', result['unchanged_connection_xml'])
        self.assertEqual(231, result['containment_views_changed'])
        self.assertEqual(29, result['known_stale_connections_changed'])

    def test_every_selected_view_has_clear_paths_and_nonoverlapping_nodes(self):
        all_views = p.views(self.root)
        for vid in self.scope['containment']:
            p.validate_geometry(all_views[vid], containment=True)
        for vid in self.scope['business']:
            p.validate_geometry(all_views[vid])

    def test_reapplying_final_geometry_is_byte_identical(self):
        revised = copy.deepcopy(self.root)
        p.apply(revised, self.scope)
        self.assertEqual(s.g.serialize(self.root), s.g.serialize(revised))

    def test_semantic_navigation_protected_and_unselected_changes_are_rejected(self):
        protected = self.scope['protected_authored'][0]
        cases = [('a:elements/a:element/a:name', 'text'),
                 ('a:relationships/a:relationship', 'target'),
                 ('a:organizations/a:item', 'identifier'),
                 (f'a:views/a:diagrams/a:view[@identifier="{protected}"]/a:node', 'x')]
        for xpath, field in cases:
            root = copy.deepcopy(self.root)
            item = root.find(xpath, s.g.N)
            self.assertIsNotNone(item)
            if field == 'text':
                item.text = 'unapproved'
            else:
                item.set(field, 'unapproved')
            with self.assertRaises(ValueError):
                p.audit(self.before, root, self.scope)

    def test_routing_obstruction_and_endpoint_change_are_rejected(self):
        vid = next(iter(self.scope['containment']))
        for field, value in (('source', 'missing'), ('relationshipRef', 'missing')):
            root = copy.deepcopy(self.root)
            p.views(root)[vid].find('a:connection', s.g.N).set(field, value)
            with self.assertRaises(ValueError):
                p.audit(self.before, root, self.scope)
        root = copy.deepcopy(self.root)
        view = p.views(root)[vid]
        connection = view.find('a:connection', s.g.N)
        nodes = {n.get('identifier'): n for n in view.findall('a:node', s.g.N)}
        # Going through the source centre violates its boundary independently
        # of schema/reference validity.
        p.set_bends(connection, [p.centre(p.rect(nodes[connection.get('source')]))])
        with self.assertRaises(ValueError):
            p.audit(self.before, root, self.scope)

    def test_route_recalculation_does_not_rewrite_an_unchanged_connection(self):
        connection = s.g.ET.fromstring(b'<connection xmlns="http://www.opengroup.org/xsd/archimate/3.0/" identifier="c">\n <bendpoint x="5" y="7"/>\n</connection>')
        before = s.g.ET.tostring(connection)
        self.assertFalse(p.set_bends(connection, [(5, 7)]))
        self.assertEqual(before, s.g.ET.tostring(connection))

    def test_odd_sized_nodes_attach_at_the_actual_rectangle_boundary(self):
        box = (17, 23, 225, 83)
        for toward in ((-100, 50), (400, 90), (50, -100), (80, 500)):
            x, y = p.boundary(box, toward)
            self.assertTrue(17 <= x <= 242 and 23 <= y <= 106)
            self.assertTrue(any(abs(a-b) < 1e-8 for a, b in ((x,17), (x,242), (y,23), (y,106))))

    def test_all_counts_and_variable_node_sizes_obey_the_governed_archetype(self):
        for count in range(1, 21):
            view = s.g.ET.Element(s.g.tag('view'))
            s.g.child(view, 'node', identifier='parent', elementRef='source', x='20', y='10', w='310', h='110')
            for i in range(count):
                s.g.child(view, 'node', identifier='node'+str(i), elementRef=f'child-{i:02d}',
                          x='20', y='10', w=str(220+i*5), h=str(80+i*3))
                s.g.child(view, 'connection', identifier='conn'+str(i), source='parent', target='node'+str(i))
            p.containment_layout(view)
            p.validate_geometry(view, containment=True)
            before = s.g.ET.tostring(view)
            p.containment_layout(view)
            self.assertEqual(before, s.g.ET.tostring(view))
        s.g.child(view, 'node', identifier='overflow', elementRef='extra', x='20', y='10', w='300', h='100')
        s.g.child(view, 'connection', identifier='overflow-edge', source='parent', target='overflow')
        before = s.g.ET.tostring(view)
        with self.assertRaisesRegex(ValueError, 'Unsupported Containment'):
            p.containment_layout(view)
        self.assertEqual(before, s.g.ET.tostring(view))

    def test_historical_home_contract_remains_independently_validated(self):
        import home_context
        result = home_context.audit(self.b, self.root)
        self.assertEqual(212, result['counts']['applicable'])
        self.assertEqual(hashlib.sha256(s.g.serialize(self.root)).hexdigest(), result['published_sha256'])
        self.assertIn('separately validated', result['unrelated_architecture_and_all_views'])
        self.assertEqual(1308, m.validate_publication(self.b, self.root)['valid_canonicalId'])

    def test_business_hierarchy_and_person_identity_scope(self):
        previous = m.read(p.PREVIOUS_EVIDENCE / 'selection.json')
        added = self.scope['containment'].keys() - previous['containment'].keys()
        counts = __import__('collections').Counter(self.b.landscapes[vid]['category'] for vid in added
                    if self.b.landscapes[vid]['path'][1] == '2-BusinessLayer')
        self.assertEqual({'business': 55, 'business_enabling': 56}, counts)
        vid = 'id-c796d1c0d4c158d08f8bd86308f2f0a0'
        children = self.scope['containment'][vid]['children']
        self.assertEqual(6, len(children))
        self.assertIn('id-727a264026584f80bc3304ff8383dc92', children)
        self.assertEqual(1, len(self.b.view_presentation['visual_nodes_created']))
        self.assertEqual(331, len(self.b.view_presentation['visual_connections_created']))
        views = p.views(self.root)
        for identifier in self.scope['protected_authored']:
            self.assertEqual(s.g.ET.tostring(p.views(self.before)[identifier]), s.g.ET.tostring(views[identifier]))
        for row in self.scope['oversized']:
            self.assertEqual(s.g.ET.tostring(p.views(self.before)[row['view']]), s.g.ET.tostring(views[row['view']]))


class SemanticContainmentTests(unittest.TestCase):
    def fixture(self, count=2):
        g = s.g
        root = g.ET.Element(g.tag('model'), identifier='model')
        elements = g.child(root, 'elements')
        relationships = g.child(root, 'relationships')
        view = g.child(g.child(g.child(root, 'views'), 'diagrams'), 'view', identifier='hierarchy')
        index, edges = {}, []
        for identifier in ['source', *[f'child-{i:02}' for i in range(count)]]:
            element = g.child(elements, 'element', identifier=identifier)
            element.set(g.TYPE, 'Capability')
            # Features deliberately have no parent/owner pointer. Native
            # accepted Composition is the only containment authority here.
            index[identifier] = dict(record=dict(record_state='current', type='feature' if identifier != 'source' else 'capability'),
                                     archimateType='Capability')
        for i in range(count):
            rid = f'composition-{i:02}'
            edge = g.child(relationships, 'relationship', identifier=rid, source='source', target=f'child-{i:02}')
            edge.set(g.TYPE, 'Composition')
            edges.append(dict(identifier=rid, record_state='current'))
        b = SimpleNamespace(home_context_index=index, ids={'source-key': 'source'},
            reconciliation=dict(elements=[], relationships=[]),
            crosswalk=dict(relationships=edges, views={}),
            landscapes={'hierarchy': dict(context='source-key')})
        return b, root, view

    def test_eligibility_needs_no_visual_connections_nodes_or_context_role(self):
        b, root, view = self.fixture()
        selected, oversized = p.containment_candidates(b, root, [])
        self.assertIn('hierarchy', selected)
        self.assertEqual([], oversized)
        self.assertEqual([], list(view))
        # Legacy classification, folder metadata and responsibility cannot
        # remove an otherwise declared immediate-child purpose.
        b.crosswalk['views']['hierarchy'] = dict(role='features:old')
        b.landscapes['hierarchy'].update(path=['elsewhere'], children=[])
        b.home_context_index['source']['record']['owner_key'] = 'unrelated'
        self.assertEqual(selected, p.containment_candidates(b, root, [])[0])

    def test_complete_ct3_feature_membership_references_existing_composition(self):
        b, root, view = self.fixture(6)
        # An accepted ingested Feature need not have the legacy currentness
        # field. Its accepted identity and relationship have independent authority.
        del b.home_context_index['child-05']['record']['record_state']
        b.reconciliation['elements'] = [dict(representationId='child-05')]
        b.crosswalk['relationships'].pop()
        b.reconciliation['relationships'] = [dict(representationId='composition-05')]
        scope = p.containment_candidates(b, root, [])[0]
        semantics = [s.g.ET.tostring(root.find('a:'+tag, s.g.N)) for tag in ('elements', 'relationships')]
        p.apply(root, dict(containment=scope, business={}))
        self.assertEqual({'source', *[f'child-{i:02}' for i in range(6)]},
                         {n.get('elementRef') for n in view.findall('a:node', s.g.N)})
        self.assertEqual({f'composition-{i:02}' for i in range(6)},
                         {n.get('relationshipRef') for n in view.findall('a:connection', s.g.N)})
        self.assertEqual(semantics, [s.g.ET.tostring(root.find('a:'+tag, s.g.N)) for tag in ('elements', 'relationships')])
        p.validate_geometry(view, containment=True)
        before = s.g.ET.tostring(root)
        p.apply(root, dict(containment=scope, business={}))
        self.assertEqual(before, s.g.ET.tostring(root))

    def test_authored_protection_precedes_eligible_generated_purpose(self):
        b, root, view = self.fixture()
        before = s.g.ET.tostring(view)
        selected, oversized = p.containment_candidates(b, root, ['hierarchy'])
        self.assertEqual({}, selected)
        self.assertEqual([], oversized)
        p.apply(root, dict(containment=selected, business={}))
        self.assertEqual(before, s.g.ET.tostring(view))

    def test_twenty_is_supported_and_more_than_twenty_is_excluded_without_mutation(self):
        for count in (20, 21):
            b, root, view = self.fixture(count)
            before = s.g.ET.tostring(view)
            selected, oversized = p.containment_candidates(b, root, [])
            self.assertEqual(count == 20, 'hierarchy' in selected)
            self.assertEqual([] if count == 20 else [dict(view='hierarchy', children=21)], oversized)
            self.assertEqual(before, s.g.ET.tostring(view))

    def test_presentation_home_realization_and_ownership_cannot_supply_composition(self):
        b, root, view = self.fixture()
        for edge in root.findall('a:relationships/a:relationship', s.g.N):
            edge.set(s.g.TYPE, 'Realization')
        b.home_context_index['child-00']['record']['owner_key'] = 'source-key'
        home = s.g.child(root.find('a:relationships', s.g.N), 'relationship', identifier='home', source='child-00', target='source')
        home.set(s.g.TYPE, 'Association')
        s.g.child(view, 'node', identifier='visual-child', elementRef='child-00')
        s.g.child(view, 'connection', identifier='visual-edge', relationshipRef='composition-00')
        self.assertEqual({}, p.containment_candidates(b, root, [])[0])
        with self.assertRaisesRegex(ValueError, 'not accepted direct Composition'):
            p.project_membership(root, s.g.ET.Element(s.g.tag('view')), dict(source='source', children=['child-00'], relationships=['composition-00']))
        root.find('a:relationships', s.g.N).remove(root.find('a:relationships/a:relationship', s.g.N))
        with self.assertRaisesRegex(ValueError, 'cannot create semantic relationships'):
            p.project_membership(root, s.g.ET.Element(s.g.tag('view')), dict(source='source', children=['child-00'], relationships=['composition-00']))

    def test_historical_native_composition_does_not_enter_current_population(self):
        b, root, _ = self.fixture()
        b.crosswalk['relationships'][0]['record_state'] = 'preserved historical assertion'
        self.assertEqual(['child-01'], p.containment_candidates(b, root, [])[0]['hierarchy']['children'])
