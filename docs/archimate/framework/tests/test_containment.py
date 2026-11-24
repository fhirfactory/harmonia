# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Governed type selection, standing and shared semantic ancestry regressions."""
import copy
from pathlib import Path
import sys
import unittest
from unittest import mock

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'tools'))
import containment as c
import generate_views as g
import information_naming as naming
import information_naming_run as run
import workspace_navigation as nav


class ContainmentTests(unittest.TestCase):
    def setUp(self):
        self.root = g.ET.Element(g.tag('model'), identifier='test', nsmap={None: g.NS, 'xsi': g.XSI})
        for tag in ('elements', 'relationships', 'propertyDefinitions'):
            g.child(self.root, tag)
        self.element('parent')
        self.element('other')

    def element(self, key, native='Capability', specialization=None):
        node = g.child(self.root.find('a:elements', g.N), 'element', identifier=key, **{g.TYPE: native})
        g.child(node, 'name', key.title())
        if specialization:
            run.set_property(self.root, node, 'Specialization', specialization)
        return node

    def edge(self, key, source, target, kind='Composition', state=None):
        edge = g.child(self.root.find('a:relationships', g.N), 'relationship', identifier=key,
                       source=source, target=target, **{g.TYPE: 'Association' if kind == 'Home' else kind})
        if kind == 'Home':
            edge.set('isDirected', 'true')
            run.set_property(self.root, edge, 'Specialization', 'Home')
        if state is not None:
            run.set_property(self.root, edge, 'v0.2 / Record State', state)
        return edge

    def test_default_home_reverses_association_direction_for_ancestry(self):
        self.element('child', 'ApplicationComponent')
        self.edge('home', 'child', 'parent', 'Home')
        resolved = c.resolve(self.root)
        self.assertEqual(['parent', 'child'], resolved.ancestry('child'))
        self.assertEqual('Home', resolved.parent_link('child')['mechanism'])

    def test_ordinary_association_and_home_name_do_not_establish_home(self):
        self.element('child', 'BusinessFunction')
        edge = self.edge('association', 'child', 'parent', 'Association')
        g.child(edge, 'name', 'Home')
        self.assertEqual(['child'], c.resolve(self.root).ancestry('child'))

    def test_each_native_exception_and_specialisation_inherits_composition(self):
        for native, specialization in (('Capability', None), ('Capability', 'Feature'),
                ('BusinessObject', None), ('DataObject', None), ('DataObject', 'Topic'), ('Artifact', None)):
            with self.subTest(native=native, specialization=specialization):
                key = native + (specialization or '')
                node = self.element(key, native, specialization)
                self.edge(key + '-composition', 'parent', key)
                self.edge(key + '-home', key, 'other', 'Home')
                self.assertEqual('Composition', c.mechanism(node))
                self.assertEqual(['parent', key], c.resolve(self.root).ancestry(key))

    def test_composition_on_other_types_never_overrides_home(self):
        for native in ('BusinessFunction', 'BusinessProcess', 'BusinessService', 'ApplicationComponent'):
            with self.subTest(native=native):
                self.element(native, native)
                self.edge(native + '-composition', 'other', native)
                self.edge(native + '-home', native, 'parent', 'Home')
                self.assertEqual(['parent', native], c.resolve(self.root).ancestry(native))

    def test_no_home_does_not_fall_back_to_composition(self):
        self.element('child', 'BusinessFunction')
        self.edge('composition', 'parent', 'child')
        self.assertEqual(['child'], c.resolve(self.root).ancestry('child'))

    def test_current_and_preserved_history_are_filtered_before_ancestry(self):
        self.element('child')
        old = self.edge('old', 'other', 'child', state='preserved historical assertion')
        self.edge('current', 'parent', 'child', state='current')
        snapshot = g.serialize(old)
        resolved = c.resolve(self.root)
        self.assertEqual(['parent', 'child'], resolved.ancestry('child'))
        self.assertEqual([], resolved.findings())
        self.assertIn('old', resolved.population['excluded'])
        self.assertEqual(snapshot, g.serialize(old))

    def test_historical_home_is_filtered_before_duplicate_or_specialisation_checks(self):
        self.element('child', 'BusinessFunction')
        self.edge('old', 'child', 'other', 'Home', 'preserved historical assertion').set('isDirected', 'false')
        self.edge('current', 'child', 'parent', 'Home', 'current')
        self.assertEqual(['parent', 'child'], c.resolve(self.root).ancestry('child'))

    def test_current_competing_parents_and_duplicate_assertions_remain_ambiguous(self):
        self.element('child')
        self.edge('first', 'parent', 'child', state='current')
        self.edge('second', 'other', 'child', state='current')
        resolved = c.resolve(self.root)
        with self.assertRaisesRegex(ValueError, 'Competing/duplicate Composition parents'):
            resolved.ancestry('child')
        self.assertEqual(['first', 'second'], resolved.findings()[0]['direct_parent_relationships'])
        self.root.find("a:relationships/a:relationship[@identifier='second']", g.N).set('source', 'parent')
        self.assertTrue(c.resolve(self.root).findings())

    def test_current_multiple_homes_remain_ambiguous(self):
        self.element('child', 'BusinessFunction')
        self.edge('first', 'child', 'parent', 'Home', 'current')
        self.edge('second', 'child', 'other', 'Home', 'current')
        with self.assertRaisesRegex(ValueError, 'Multiple explicit Homes'):
            c.resolve(self.root).ancestry('child')

    def test_ancestry_is_deterministic_across_mixed_mechanisms(self):
        self.element('feature')
        self.element('function', 'BusinessFunction')
        self.edge('frame', 'parent', 'feature')
        self.edge('home', 'function', 'feature', 'Home')
        first = c.resolve(self.root).evidence()
        for tag in ('elements', 'relationships'):
            parent = self.root.find('a:' + tag, g.N)
            parent[:] = reversed(list(parent))
        self.assertEqual(first, c.resolve(self.root).evidence())
        self.assertEqual(['parent', 'feature', 'function'], first['ancestry']['function'])

    def test_invalid_home_cannot_be_reinterpreted_as_an_ordinary_association(self):
        self.element('child', 'BusinessFunction')
        self.edge('bad', 'child', 'parent', 'Home').set('isDirected', 'false')
        with self.assertRaisesRegex(ValueError, 'directed native Association'):
            c.resolve(self.root)

    def test_cycle_and_missing_endpoint_are_not_repaired(self):
        self.element('child')
        self.edge('down', 'parent', 'child')
        self.edge('up', 'child', 'parent')
        self.assertTrue(c.resolve(self.root).findings())
        self.root.find("a:relationships/a:relationship[@identifier='up']", g.N).set('source', 'absent')
        with self.assertRaisesRegex(ValueError, 'Missing Composition endpoint'):
            c.resolve(self.root).ancestry('child')
        self.root.find("a:relationships/a:relationship[@identifier='up']", g.N).set('source', 'child')
        self.root.find("a:relationships/a:relationship[@identifier='down']", g.N).set('target', 'absent')
        self.assertTrue(any(row['element'] == 'absent' for row in c.resolve(self.root).findings()))

    def test_record_population_is_exhaustive_and_ingestion_acceptance_is_explicit(self):
        self.element('child')
        self.edge('edge', 'parent', 'child')
        self.assertEqual(['child'], c.resolve(self.root, records=[], include_proposals=False).ancestry('child'))
        self.assertEqual(['parent', 'child'], c.resolve(self.root, records=[], accepted=['edge']).ancestry('child'))

    def test_names_and_folders_consume_the_same_resolved_model(self):
        # Reuse the actual integration fixtures, rather than mirror folder logic.
        from test_workspace_navigation import WorkspaceNavigationTests
        fixture = WorkspaceNavigationTests()
        fixture.setUp()
        self.addCleanup(fixture.doCleanups)
        fixture.edge()
        model = fixture.model()
        resolved = c.resolve(fixture.root)
        proposal = naming.propose(model, resolved=resolved)
        with mock.patch.object(c, 'resolve', side_effect=AssertionError('second semantic resolution')):
            result, plan = nav.project(model, fixture.root, resolved=resolved)
            nav.verify(model, fixture.root, result, plan, resolved=resolved)
        child = next(row for row in proposal['records'] if row['representation_id'] == 'child')
        self.assertEqual(child['context']['path'], plan['element_plan']['child']['composition_path'])
        changed = copy.deepcopy(fixture.root)
        changed.find('a:organizations', g.N).clear()
        self.assertEqual(resolved.evidence(), c.resolve(changed).evidence())

    def test_home_derived_folders_use_resolved_semantics_without_reverse_inference(self):
        from test_workspace_navigation import WorkspaceNavigationTests
        fixture = WorkspaceNavigationTests()
        fixture.setUp()
        self.addCleanup(fixture.doCleanups)
        for node in fixture.root.findall('a:elements/a:element', g.N):
            node.set(g.TYPE, 'ApplicationComponent')
        edge = fixture.edge('child', 'root', kind='Association')
        edge.set('isDirected', 'true')
        run.set_property(fixture.root, edge, 'Specialization', 'Home')
        anchors = {'ApplicationComponent': ('Application', 'InformationMap')}
        result, plan = nav.project(fixture.model(), fixture.root, anchors)
        self.assertEqual(['root', 'child'], plan['element_plan']['child']['composition_path'])
        self.assertEqual(['Application', 'InformationMap', '[Topic] Individual', '[Topic] Identity'],
                         fixture.home(result, 'child'))
        self.assertEqual('PASS', nav.verify(fixture.model(), fixture.root, result, plan, anchors)['composition_navigation'])


if __name__ == '__main__':
    unittest.main()
