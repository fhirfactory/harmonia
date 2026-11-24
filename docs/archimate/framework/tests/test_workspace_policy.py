# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Governed collection classification is separate from actual containment."""
import unittest

import test_workspace_navigation as fixtures
import containment
import workspace_policy as policy
import workspace_navigation as nav
import generate_views as g
import information_naming_run as naming


class WorkspacePolicyTests(unittest.TestCase):
    def setUp(self):
        self.fixture = fixtures.WorkspaceNavigationTests()
        self.fixture.setUp()
        self.addCleanup(self.fixture.doCleanups)
        self.root = self.fixture.root
        self.records = {}
        for rid, canonical, kind, name in (
                ('root', 'a' * 32, 'Capability', '[Feature] Parent'),
                ('child', 'b' * 32, 'BusinessFunction', 'Operation')):
            node = self.root.find("a:elements/a:element[@identifier='" + rid + "']", g.N)
            node.set(g.TYPE, kind)
            node.find('a:name', g.N).text = name
            naming.set_property(self.root, node, 'canonicalId', canonical)
            self.records[canonical] = dict(canonicalId=canonical, key=rid, type='feature' if rid == 'root' else 'function',
                                           category='business_enabling', record_state='current')

    def place(self):
        model = self.fixture.model()
        resolved = containment.resolve(self.root)
        placements = policy.placements(model, resolved, self.records)
        result, report = nav.project(model, self.root, resolved=resolved, placements=placements)
        nav.verify(model, self.root, result, report, resolved=resolved, placements=placements)
        return result, report, placements

    def test_explicit_home_generates_business_context_without_composition_or_input_folders(self):
        home = self.fixture.edge('child', 'root', 'Association', 'home')
        home.set('isDirected', 'true')
        naming.set_property(self.root, home, 'Specialization', 'Home')
        org = self.root.find('a:organizations', g.N)
        for item in list(org):
            if item.findtext('a:label', namespaces=g.N) != 'Views':
                org.remove(item)
        result, report, _ = self.place()
        self.assertEqual(['root', 'child'], report['element_plan']['child']['composition_path'])
        self.assertEqual(['Business', 'Business Enabling Capabilities', '[Feature] Parent', 'Functions', 'Operation'],
                         nav.labels(nav.locations(result)['child'][0][0]))
        self.assertEqual([], [e for e in result.findall('a:relationships/a:relationship', g.N)
                              if e.get(g.TYPE) == 'Composition'])

    def test_recorded_owner_category_does_not_manufacture_current_containment(self):
        self.records['a' * 32].pop('category')
        self.records['a' * 32]['owner_key'] = 'owner'
        self.records['c' * 32] = dict(key='owner', category='business_enabling', canonicalId='c' * 32)
        _, report, placements = self.place()
        self.assertEqual(['root'], report['element_plan']['root']['composition_path'])
        self.assertIn('Unresolved Feature Ancestry', placements['root']['anchor'])
        self.assertIn('Unestablished Home', placements['child']['anchor'])

    def test_unmapped_or_conflicting_classification_stops_before_projection(self):
        self.records.pop('a' * 32)
        with self.assertRaisesRegex(ValueError, 'Unmapped collection'):
            self.place()

    def test_current_composition_category_precedes_recorded_owner_context(self):
        child = self.root.find("a:elements/a:element[@identifier='child']", g.N)
        child.set(g.TYPE, 'Capability')
        self.records['b' * 32].update(type='feature', category='business')
        self.fixture.edge('root', 'child')
        _, report, placements = self.place()
        self.assertEqual('Business Enabling Capabilities', placements['child']['anchor'][-1])
        self.assertEqual(['root', 'child'], report['element_plan']['child']['composition_path'])

    def test_information_population_needs_no_specimen_names_or_preexisting_collections(self):
        self.fixture.root = self.root = g.ET.Element(g.tag('model'), nsmap={None:g.NS, 'xsi':g.XSI}, identifier='arbitrary')
        g.child(self.root, 'name', 'Unrelated test population')
        elements = g.child(self.root, 'elements')
        g.child(self.root, 'relationships')
        g.child(self.root, 'organizations')
        g.child(self.root, 'propertyDefinitions')
        for rid, kind in (('parcel', 'Artifact'), ('concept', 'BusinessObject'), ('logical', 'DataObject')):
            g.child(g.child(elements, 'element', identifier=rid, **{g.TYPE:kind}), 'name', rid)
        result, report, _ = self.place()
        self.assertEqual(3, report['summary']['projected_elements'])
        self.assertEqual(['Technology & Physical', 'DataMap'], nav.labels(nav.locations(result)['parcel'][0][0]))
