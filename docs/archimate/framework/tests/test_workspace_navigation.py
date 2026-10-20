# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Semantic-to-workspace projection, preservation and immutable IA regressions."""
import copy
import gzip
import json
from pathlib import Path
import sys
import tempfile
import unittest
from unittest import mock

BASE = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(BASE / 'tools'))
import diagnose_roundtrip as d
import generate_views as g
import information_naming_run as naming
import information_workspace_run as run
import workspace_navigation as nav


class WorkspaceNavigationTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(prefix='harmonia-workspace-', dir='/tmp')
        self.addCleanup(self.temp.cleanup)
        self.root = g.ET.Element(g.tag('model'), nsmap={None: g.NS, 'xsi': g.XSI}, identifier='test-model')
        g.child(self.root, 'name', 'NON-GOVERNED workspace test')
        g.child(self.root, 'elements')
        g.child(self.root, 'relationships')
        org = g.child(self.root, 'organizations')
        for layer, collection in nav.INFORMATION_ANCHORS.values():
            parent = g.child(org, 'item')
            g.child(parent, 'label', layer)
            folder = g.child(parent, 'item')
            g.child(folder, 'label', collection)
        for label in ('Relations', 'Views'):
            g.child(g.child(org, 'item'), 'label', label)
        g.child(self.root, 'propertyDefinitions')
        self.element('root', '[Topic] Individual')
        self.element('child', '[Topic] Identity')

    def anchor(self, kind='DataObject'):
        layer, collection = nav.INFORMATION_ANCHORS[kind]
        return self.root.xpath('a:organizations/a:item[a:label=$layer]/a:item[a:label=$collection]',
                               namespaces=g.N, layer=layer, collection=collection)[0]

    def element(self, rid, name, kind='DataObject', canonical=None):
        node = g.child(self.root.find('a:elements', g.N), 'element', identifier=rid, **{g.TYPE: kind})
        g.child(node, 'name', name)
        g.child(node, 'documentation', 'Keep **authored** text,\nincluding history and whitespace.')
        if kind == 'DataObject':
            naming.set_property(self.root, node, 'Specialization', 'Topic')
        if canonical is not None:
            naming.set_property(self.root, node, 'canonicalId', canonical)
        g.child(self.anchor(kind), 'item', identifierRef=rid)
        return node

    def edge(self, source='root', target='child', kind='Composition', rid='edge'):
        edge = g.child(self.root.find('a:relationships', g.N), 'relationship', identifier=rid,
                       source=source, target=target, **{g.TYPE: kind})
        g.child(edge, 'documentation', 'Untouched relationship documentation')
        return edge

    def model(self):
        path = Path(self.temp.name) / 'input.xml'
        path.write_bytes(g.serialize(self.root))
        return d.Exchange(path)

    def project(self):
        model = self.model()
        return nav.project(model, self.root)

    def home(self, root, rid):
        return nav.labels(nav.locations(root)[rid][0][0])

    def test_parent_and_contained_leaf_each_have_self_context(self):
        self.edge()
        result, report = self.project()
        expected = ['Application', 'InformationMap', '[Topic] Individual']
        self.assertEqual(expected, self.home(result, 'root'))
        self.assertEqual([*expected, '[Topic] Identity'], self.home(result, 'child'))
        self.assertEqual(['Relations', *expected], self.home(result, 'edge'))
        folder = nav.locations(result)['root'][0][0][-1]
        self.assertEqual(['root', None], [n.get('identifierRef') for n in folder.findall('a:item', g.N)])
        self.assertEqual(2, report['summary']['element_context_folders'])
        self.assertEqual('PASS', nav.verify(self.model(), self.root, result, report)['actual_source_navigation'])

    def test_final_serialized_care_encounter_leaf_and_nonleaf_structure(self):
        for rid, name in (('root', '[Topic] Care'), ('child', '[Topic] Encounter')):
            self.root.find(f"a:elements/a:element[@identifier='{rid}']/a:name", g.N).text = name
        self.element('episode', '[Topic] Episode')
        self.edge()
        self.edge('root', 'episode', rid='episode-edge')
        for nonleaf in (False, True):
            if nonleaf:
                self.element('nested', '[Topic] Child')
                self.edge('child', 'nested', rid='nested-edge')
            with self.subTest(contained_nonleaf=nonleaf):
                result, _ = self.project()
                parsed = g.ET.fromstring(g.serialize(result))
                care, = parsed.xpath("a:organizations/a:item[a:label='Application']/a:item[a:label='InformationMap']/a:item[a:label='[Topic] Care']", namespaces=g.N)
                self.assertEqual(['root'], care.xpath('a:item/@identifierRef', namespaces=g.N))
                encounter, = care.xpath("a:item[a:label='[Topic] Encounter']", namespaces=g.N)
                self.assertEqual(['child'], encounter.xpath('a:item/@identifierRef', namespaces=g.N))
                episode, = care.xpath("a:item[a:label='[Topic] Episode']", namespaces=g.N)
                self.assertEqual(['episode'], episode.xpath('a:item/@identifierRef', namespaces=g.N))
                if nonleaf:
                    nested, = encounter.xpath("a:item[a:label='[Topic] Child']", namespaces=g.N)
                    self.assertEqual(['nested'], nested.xpath('a:item/@identifierRef', namespaces=g.N))

    def test_home_governed_leaf_and_nonleaf_contexts_recurse_without_input_folders(self):
        self.element('leaf', 'Child')
        for rid in ('root', 'child', 'leaf'):
            self.root.find(f"a:elements/a:element[@identifier='{rid}']", g.N).set(g.TYPE, 'BusinessFunction')
        for source, target in (('child', 'root'), ('leaf', 'child')):
            edge = self.edge(source, target, 'Association', source + '-home')
            edge.set('isDirected', 'true')
            naming.set_property(self.root, edge, 'Specialization', 'Home')
        org = self.root.find('a:organizations', g.N)
        for item in list(org):
            if item.findtext('a:label', namespaces=g.N) != 'Views':
                org.remove(item)
        anchors = {'BusinessFunction': ('Business', 'Functions')}
        result, report = nav.project(self.model(), self.root, anchors)
        parsed = g.ET.fromstring(g.serialize(result))
        base = ['Business', 'Functions', '[Topic] Individual', '[Topic] Identity']
        self.assertEqual(base, self.home(parsed, 'child'))
        self.assertEqual([*base, 'Child'], self.home(parsed, 'leaf'))
        self.assertEqual(3, report['summary']['element_context_folders'])
        self.assertEqual('PASS', nav.verify(self.model(), self.root, parsed, report, anchors)['actual_source_navigation'])

    def test_namespace_qualified_native_types_are_resolved_without_retyping(self):
        root = g.ET.Element(self.root.tag, attrib=self.root.attrib,
                            nsmap={None: g.NS, 'xsi': g.XSI, 'archimate': g.NS})
        root[:] = list(self.root)
        self.root = root
        for node in self.root.findall('a:elements/a:element', g.N):
            node.set(g.TYPE, 'archimate:DataObject')
        self.edge().set(g.TYPE, 'archimate:Composition')
        result, report = self.project()
        self.assertEqual(2, report['summary']['element_context_folders'])
        self.assertEqual(nav.protected(self.root), nav.protected(result))
        self.assertEqual('PASS', nav.verify(self.model(), self.root, result, report)['composition_navigation'])

    def test_deeper_composition_and_leaf_source_navigation(self):
        self.element('leaf', '[Topic] Identifier')
        self.edge()
        self.edge('child', 'leaf', rid='inner')
        self.edge('leaf', 'root', 'Association', 'outgoing')
        result, report = self.project()
        base = ['Application', 'InformationMap', '[Topic] Individual', '[Topic] Identity']
        self.assertEqual(base, self.home(result, 'child'))
        self.assertEqual([*base, '[Topic] Identifier'], self.home(result, 'leaf'))
        self.assertEqual(['Relations', *base, '[Topic] Identifier'], self.home(result, 'outgoing'))
        self.assertEqual(2, report['summary']['maximum_composition_depth'])

    def test_non_contained_elements_remain_local_without_root_completeness_claim(self):
        self.element('artifact', '[Artifact] Package', 'Artifact')
        self.element('bo', '[BusinessObject] Person', 'BusinessObject')
        result, report = self.project()
        self.assertEqual(['Application', 'InformationMap'], self.home(result, 'child'))
        self.assertEqual(['Technology & Physical', 'DataMap'], self.home(result, 'artifact'))
        self.assertEqual(['Business', 'BusinessObjectMap'], self.home(result, 'bo'))
        self.assertEqual((0, 4), (report['summary']['element_context_folders'], report['summary']['non_contained_elements']))

    def test_aggregation_and_other_relationship_types_never_create_containment(self):
        self.edge('root', 'child', rid='contained')
        self.element('equipment', '[Topic] Equipment')
        for i, kind in enumerate(('Aggregation', 'Association', 'Specialization', 'Realization', 'Flow')):
            self.edge('equipment', 'child', kind, 'other-' + str(i))
        result, report = self.project()
        self.assertEqual(['root', 'child'], report['element_plan']['child']['composition_path'])
        for i in range(5):
            self.assertEqual(['Relations', 'Application', 'InformationMap', '[Topic] Equipment'],
                             self.home(result, 'other-' + str(i)))
        self.assertEqual(nav.protected(self.root), nav.protected(result))

    def test_misleading_authored_folder_nesting_does_not_change_derived_navigation(self):
        self.edge()
        before, plan = self.project()
        refs = nav.locations(self.root)
        misleading = g.child(self.anchor(), 'item')
        g.child(misleading, 'label', '[Topic] Equipment')
        misleading.append(refs['child'][0][1])
        after, repeated = self.project()
        self.assertEqual(plan, repeated)
        self.assertEqual(g.signature(before), g.signature(after))

    def test_missing_all_element_relationship_folders_is_generated_from_semantics(self):
        self.edge()
        original, plan = self.project()
        org = self.root.find('a:organizations', g.N)
        for item in list(org):
            if item.findtext('a:label', namespaces=g.N) != 'Views':
                org.remove(item)
        generated, repeated = self.project()
        self.assertEqual(g.signature(original), g.signature(generated))
        self.assertEqual(plan, repeated)
        self.assertEqual(2, repeated['projection_coverage']['generated_elements'])
        self.assertFalse(repeated['projection_coverage']['input_folder_ancestry_used'])

    def test_changed_semantics_reprojects_folders_instead_of_preserving_old_tree(self):
        edge = self.edge()
        first, plan = self.project()
        self.root = first
        edge = self.root.find('a:relationships/a:relationship', g.N)
        edge.set('source', 'child')
        edge.set('target', 'root')
        second, changed = self.project()
        self.assertEqual(['Application', 'InformationMap', '[Topic] Identity', '[Topic] Individual'], self.home(second, 'root'))
        self.assertNotEqual(plan['element_plan'], changed['element_plan'])

    def test_views_geometry_grouping_and_documentation_are_preserved_without_containment(self):
        group = g.child(self.root.find('a:elements', g.N), 'element', identifier='group', **{g.TYPE: 'Grouping'})
        g.child(group, 'name', 'Individual Identity')
        g.child(self.anchor('BusinessObject'), 'item', identifierRef='group')
        view = g.child(g.child(g.child(self.root, 'views'), 'diagrams'), 'view',
                       identifier='view', **{g.TYPE: 'Diagram'})
        g.child(view, 'name', 'Authored nested presentation')
        visual = g.child(view, 'node', identifier='visual-root', elementRef='root', x='0', y='0', w='300', h='200', **{g.TYPE: 'Element'})
        g.child(visual, 'node', identifier='visual-child', elementRef='child', x='10', y='10', w='100', h='40', **{g.TYPE: 'Element'})
        views_folder = self.root.xpath("a:organizations/a:item[a:label='Views']", namespaces=g.N)[0]
        g.child(views_folder, 'item', identifierRef='view')
        result, report = self.project()
        self.assertEqual(0, report['summary']['element_context_folders'])
        self.assertEqual(nav.protected(self.root), nav.protected(result))
        self.assertEqual(g.signature(views_folder), g.signature(result.xpath("a:organizations/a:item[a:label='Views']", namespaces=g.N)[0]))
        self.assertEqual('PASS', nav.verify(self.model(), self.root, result, report)['protected_content'])

    def test_unestablished_view_navigation_stops_before_it_can_be_discarded(self):
        self.edge()
        view = g.child(g.child(g.child(self.root, 'views'), 'diagrams'), 'view',
                       identifier='view', **{g.TYPE: 'Diagram'})
        g.child(view, 'name', 'Preserve this authored presentation')
        before = g.serialize(self.root)
        with self.assertRaisesRegex(ValueError, 'Unestablished View navigation'):
            self.project()
        self.assertEqual(before, g.serialize(self.root))
        g.child(self.anchor(), 'item', identifierRef='view')
        before = g.serialize(self.root)
        with self.assertRaisesRegex(ValueError, 'Unestablished View navigation'):
            self.project()
        self.assertEqual(before, g.serialize(self.root))

    def test_declared_population_is_generic_and_supports_many_levels(self):
        org = self.root.find('a:organizations', g.N)
        strategy = g.folder(org, 'strategy-folder', 'Strategy')
        collection = g.folder(strategy, 'capability-folder', 'Capabilities')
        for i in range(8):
            rid = 'capability-' + str(i)
            node = g.child(self.root.find('a:elements', g.N), 'element', identifier=rid, **{g.TYPE: 'Capability'})
            g.child(node, 'name', 'Level ' + str(i))
            g.child(collection, 'item', identifierRef=rid)
            if i:
                self.edge('capability-' + str(i - 1), rid, rid='composition-' + str(i))
        anchors = {'Capability': ('Strategy', 'Capabilities')}
        result, report = nav.project(self.model(), self.root, anchors)
        self.assertEqual(7, report['summary']['maximum_composition_depth'])
        self.assertEqual(['Strategy', 'Capabilities', *('Level ' + str(i) for i in range(8))],
                         self.home(result, 'capability-7'))
        self.assertEqual('PASS', nav.verify(self.model(), self.root, result, report, anchors)['composition_navigation'])

    def test_competing_duplicate_missing_and_cycle_inputs_stop_without_mutation(self):
        self.edge()
        self.element('other', '[Topic] Other')
        for source, target, message in (
                ('other', 'child', 'Competing/duplicate'), ('root', 'child', 'Competing/duplicate'),
                ('child', 'root', 'cycle'), ('root', 'missing', 'Missing Composition endpoint'),
                ('missing', 'root', 'Missing Composition endpoint')):
            extra = self.edge(source, target, rid='bad')
            before = g.serialize(self.root)
            with self.subTest(source=source, target=target), self.assertRaisesRegex(ValueError, message):
                self.project()
            self.assertEqual(before, g.serialize(self.root))
            extra.getparent().remove(extra)

    def test_self_cycle_and_cross_scope_containment_stop(self):
        self.edge('root', 'root')
        with self.assertRaisesRegex(ValueError, 'cycle'):
            self.project()
        self.root.find('a:relationships', g.N).clear()
        self.edge()
        with self.assertRaisesRegex(ValueError, 'outside declared projection scope'):
            nav.containment.resolve(self.root).paths({'child'}, closed=True)

    def test_projection_ids_are_separate_stable_across_rename_rebind_and_reparent(self):
        for rid, value in (('root', 'a' * 32), ('child', 'b' * 32)):
            node = self.root.find(f"a:elements/a:element[@identifier='{rid}']", g.N)
            naming.set_property(self.root, node, 'canonicalId', value)
        self.edge()
        first, report = self.project()
        root_folder = report['element_plan']['root']['context_folder']
        self.assertTrue(root_folder.startswith('workspace-'))
        node = self.root.find("a:elements/a:element[@identifier='root']", g.N)
        node.set('identifier', 'rebound')
        node.find('a:name', g.N).text = '[Topic] Renamed'
        self.root.find('a:relationships/a:relationship', g.N).set('source', 'rebound')
        nav.locations(self.root)['root'][0][1].set('identifierRef', 'rebound')
        second, report2 = self.project()
        self.assertEqual(root_folder, report2['element_plan']['rebound']['context_folder'])
        self.assertNotIn(root_folder, ('a' * 32, 'b' * 32))
        self.element('outer', '[Topic] Outer')
        self.edge('outer', 'rebound', rid='outer-edge')
        _, report3 = self.project()
        self.assertEqual(root_folder, report3['element_plan']['rebound']['context_folder'])

    def test_reordering_semantics_and_repeated_projection_are_deterministic(self):
        self.edge()
        first, report = self.project()
        for tag in ('elements', 'relationships'):
            node = self.root.find('a:' + tag, g.N)
            node[:] = reversed(list(node))
        second, again = self.project()
        self.assertEqual(g.signature(first.find('a:organizations', g.N)), g.signature(second.find('a:organizations', g.N)))
        self.assertEqual(report, again)
        third, third_report = nav.project(self.model(), first)
        self.assertEqual(g.serialize(first), g.serialize(third))
        self.assertEqual(report, third_report)

    def test_same_labels_keep_distinct_identity_contexts(self):
        self.element('other', '[Topic] Individual')
        self.element('other-child', '[Topic] Identity')
        self.edge()
        self.edge('other', 'other-child', rid='second')
        result, report = self.project()
        self.assertNotEqual(report['element_plan']['root']['context_folder'], report['element_plan']['other']['context_folder'])
        self.assertNotEqual(report['relationship_plan']['edge']['source_context_folder'], report['relationship_plan']['second']['source_context_folder'])
        self.assertEqual('PASS', nav.verify(self.model(), self.root, result, report)['unique_navigation_references'])

    def test_verifier_rejects_target_navigation_extra_references_and_semantic_mutation(self):
        self.edge()
        good, report = self.project()
        for mode in ('target', 'duplicate', 'semantic', 'folder-id'):
            result = copy.deepcopy(good)
            homes = nav.locations(result)
            if mode == 'target':
                folder = homes['edge'][0][0][-1]
                folder.find('a:label', g.N).text = '[Topic] Identity'
            elif mode == 'duplicate':
                homes['child'][0][1].getparent().append(copy.deepcopy(homes['child'][0][1]))
            elif mode == 'semantic':
                result.find('a:relationships/a:relationship', g.N).set('source', 'child')
            else:
                homes['child'][0][0][-1].set('identifier', 'wrong-projection-id')
            with self.subTest(mode=mode), self.assertRaises(ValueError):
                nav.verify(self.model(), self.root, result, report)


class FrozenWorkspaceRegressionTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        # This is a historical specimen after release promotion, not permission
        # to execute another machinery run against the current release authority.
        evidence = run.RUNS / 'run-002'
        manifest_bytes = (evidence / 'manifest.json').read_bytes()
        manifest = json.loads(manifest_bytes)
        cls.outputs = {name: (evidence / name).read_bytes() for name in manifest['files_sha256']}
        for name, expected in manifest['files_sha256'].items():
            g.require(naming.sha(cls.outputs[name]) == expected, 'Frozen specimen drift: ' + name)
        cls.outputs['manifest.json'] = manifest_bytes
        # Exercise live shared projection independently of the run-generation
        # guard, and require the exact frozen output, without writing a run.
        _, models = naming.load_inputs()
        incoming = models['incoming']
        resolved = run.containment.resolve(incoming.root)
        proposal = naming.naming.propose(incoming, resolved=resolved)
        named = naming.candidate(incoming, proposal, 'run-002')
        records, sources = run.workspace_policy.load_records()
        placements = run.workspace_policy.placements(incoming, resolved, records)
        projected, plan = nav.project(incoming, g.ET.fromstring(named), resolved=resolved, placements=placements)
        plan['collection_sources_sha256'] = sources
        g.require(g.serialize(projected) == cls.outputs['candidate.xml'], 'Live projection changed frozen candidate')
        g.require(naming.encoded(plan) == cls.outputs['workspace-projection.json'], 'Live projection plan drift')
        g.require(naming.encoded(proposal) == cls.outputs['proposed-metadata.json'], 'Live naming drift')
        cls.root = g.ET.fromstring(cls.outputs['candidate.xml'])
        cls.plan = json.loads(cls.outputs['workspace-projection.json'])
        cls.verification = json.loads(cls.outputs['verification.json'])

    def test_run001_naming_rebindings_identities_and_deferred_classification_are_preserved(self):
        for name in ('proposed-metadata.json', 'representation-rebindings.json'):
            self.assertEqual((run.RUNS / 'run-001' / name).read_bytes(), self.outputs[name])
        self.assertEqual((38, 32, 0), tuple(self.verification['population'][k] for k in ('representation_rebindings', 'new', 'deleted')))
        self.assertEqual(4, self.verification['naming']['unresolved'])
        self.assertEqual(0, self.verification['canonical_ids_allocated'])
        self.assertEqual(0, self.verification['semantic_changes'])

    def test_individual_tree_is_driven_by_composition_and_relations_by_actual_source(self):
        elements = nav.index(self.root, 'a:elements/a:element')
        individual, = [key for key, node in elements.items() if node.findtext('a:name', namespaces=g.N) == '[Topic] Individual']
        folder = nav.locations(self.root)[individual][0][0][-1]
        self.assertEqual('[Topic] Individual', folder.findtext('a:label', namespaces=g.N))
        self.assertEqual([individual], folder.xpath('a:item/@identifierRef', namespaces=g.N))
        children = folder.xpath('a:item[a:label]', namespaces=g.N)
        self.assertEqual({'[Topic] ' + s for s in ('Authority', 'Contact', 'Demographics', 'Identity', 'Patient', 'Person', 'Relationship')},
                         {n.findtext('a:label', namespaces=g.N) for n in children})
        for child in children:
            reference, = child.xpath('a:item/@identifierRef', namespaces=g.N)
            self.assertEqual(child.findtext('a:label', namespaces=g.N), elements[reference].findtext('a:name', namespaces=g.N))
        homes = nav.locations(self.root)
        for rid, edge in nav.index(self.root, 'a:relationships/a:relationship').items():
            self.assertEqual(edge.get('source'), self.plan['relationship_plan'][rid]['source'])
            self.assertEqual(1, len(homes[rid]))

    def test_workforce_composition_and_equipment_aggregation_remain_distinct(self):
        elements = nav.index(self.root, 'a:elements/a:element')
        edges = nav.index(self.root, 'a:relationships/a:relationship')
        names = {node.findtext('a:name', namespaces=g.N): rid for rid, node in elements.items()}
        target = names['[Topic] CommunicationAddress']
        selected = {(elements[e.get('source')].findtext('a:name', namespaces=g.N), e.get(g.TYPE))
                    for e in edges.values() if e.get('target') == target}
        self.assertIn(('[Topic] Workforce', 'Composition'), selected)
        self.assertIn(('[Topic] Equipment', 'Aggregation'), selected)
        self.assertEqual([names['[Topic] Workforce'], target], self.plan['element_plan'][target]['composition_path'])

    def test_frozen_inputs_and_release_are_unchanged_and_sources_are_archived(self):
        manifest = json.loads(self.outputs['manifest.json'])
        self.assertEqual(manifest['frozen_files_before'], manifest['frozen_files_after'])
        self.assertEqual('1.0.10', manifest['release_authority']['version'])
        self.assertEqual(1394, len(self.root.findall('a:elements/a:element', g.N)))
        self.assertEqual(1137, len(self.root.findall('a:relationships/a:relationship', g.N)))
        self.assertTrue(self.outputs['candidate.xml'].startswith(b'<?xml'))
        for path, expected in manifest['source_sha256'].items():
            self.assertEqual(expected, naming.sha(naming.gzip.decompress(self.outputs['machinery/' + path + '.gz'])))

    def test_current_containment_and_complete_generated_workspace_result(self):
        resolved = json.loads(self.outputs['resolved-containment.json'])
        self.assertEqual([], resolved['findings'])
        self.assertEqual([], self.plan['out_of_scope_containment_findings'])
        self.assertEqual(121, len(resolved['excluded_relationships']))
        self.assertEqual({'preserved historical assertion'}, set(resolved['excluded_relationships'].values()))
        self.assertNotEqual('589759a0dbca2fb19f5a263a65309cc94fa10260797299d6c7e245061a828599',
                            naming.sha(self.outputs['candidate.xml']))
        archive = run.RUNS / 'run-002/validation/pre-containment-correction'
        manifest = json.loads((archive / 'manifest.json').read_bytes())
        path = str((run.RUNS / 'run-002/workspace-projection.json').relative_to(run.ROOT))
        row = manifest['files'][path]
        data = gzip.decompress((archive / row['snapshot']).read_bytes())
        self.assertEqual(row['sha256'], naming.sha(data))
        previous = json.loads(data)
        self.assertEqual(109, len(previous['out_of_scope_containment_findings']))
        direct = [r for r in previous['out_of_scope_containment_findings'] if len(r['direct_parent_relationships']) > 1]
        self.assertEqual(61, len(direct))
        for finding in direct:
            edges = finding['direct_parent_relationships']
            self.assertEqual(1, sum(edge in resolved['selected_relationships'] for edge in edges))
            self.assertEqual(1, sum(edge in resolved['excluded_relationships'] for edge in edges))
        parents = {row['composition_path'][-2] for row in self.plan['element_plan'].values()
                   if len(row['composition_path']) > 1}
        for rid, entry in previous['element_plan'].items():
            self.assertEqual(entry['composition_path'], self.plan['element_plan'][rid]['composition_path'])
            contexts = entry['composition_path'] if len(entry['composition_path']) > 1 or rid in parents else []
            self.assertEqual(contexts, self.plan['element_plan'][rid]['folder_ancestors'])
        self.assertEqual(1394, self.plan['summary']['projected_elements'])
        self.assertEqual([], self.plan['projection_coverage']['retained_elements'])
        proof = json.loads(self.outputs['element-generation-proof.json'])
        self.assertEqual('PASS', proof['status'])
        self.assertEqual(['ABSENT', 'MISLEADING'], [r['input_element_relationship_navigation'] for r in proof['cases']])
        self.assertFalse(proof['immutable_input_modified'])

    def test_final_candidate_xml_has_care_self_and_nested_encounter_self(self):
        # Assert actual serialized candidate.xml, independently of its plan.
        root = g.ET.fromstring(self.outputs['candidate.xml'])
        elements = nav.index(root, 'a:elements/a:element')
        names = {node.findtext('a:name', namespaces=g.N): rid for rid, node in elements.items()}
        care, = root.xpath("a:organizations/a:item[a:label='Application']/a:item[a:label='InformationMap']/a:item[a:label='[Topic] Care']", namespaces=g.N)
        self.assertEqual([names['[Topic] Care']], care.xpath('a:item/@identifierRef', namespaces=g.N))
        encounter, = care.xpath("a:item[a:label='[Topic] Encounter']", namespaces=g.N)
        self.assertEqual([names['[Topic] Encounter']], encounter.xpath('a:item/@identifierRef', namespaces=g.N))

    def test_relationship_navigation_and_protected_content_match_pre_correction_exactly(self):
        archive = run.RUNS / 'run-002/validation/pre-contained-leaf-correction'
        manifest = json.loads((archive / 'manifest.json').read_bytes())
        def previous(name):
            relative = str((run.RUNS / 'run-002' / name).relative_to(run.ROOT))
            row = manifest['files'][relative]
            data = gzip.decompress((archive / row['snapshot']).read_bytes())
            self.assertEqual(row['sha256'], naming.sha(data))
            return data
        old = g.ET.fromstring(previous('candidate.xml'))
        self.assertEqual(nav.protected(old), nav.protected(self.root))
        xpath = "a:organizations/a:item[a:label='Relations']"
        self.assertEqual([g.signature(n) for n in old.xpath(xpath, namespaces=g.N)],
                         [g.signature(n) for n in self.root.xpath(xpath, namespaces=g.N)])
        self.assertEqual(json.loads(previous('workspace-projection.json'))['relationship_plan'], self.plan['relationship_plan'])
        for name in ('resolved-containment.json', 'proposed-metadata.json', 'representation-rebindings.json'):
            self.assertEqual(previous(name), self.outputs[name])

    def test_every_existing_view_and_view_navigation_is_preserved_exactly(self):
        _, models = naming.load_inputs()
        incoming = models['incoming'].root
        self.assertEqual(1209, len(self.root.findall('a:views/a:diagrams/a:view', g.N)))
        self.assertEqual(g.signature(incoming.find('a:views', g.N)), g.signature(self.root.find('a:views', g.N)))
        self.assertEqual([g.signature(n) for n in incoming.xpath("a:organizations/a:item[a:label='Views']", namespaces=g.N)],
                         [g.signature(n) for n in self.root.xpath("a:organizations/a:item[a:label='Views']", namespaces=g.N)])

    def test_writer_refuses_existing_run_and_readonly_check_detects_changed_evidence(self):
        outputs = {'candidate.xml': b'plain XML', 'workspace-projection.json': b'{"summary":{}}'}
        with tempfile.TemporaryDirectory(dir='/tmp') as tmp, \
                mock.patch.object(run, 'RUNS', Path(tmp)), mock.patch.object(run, 'ROOT', Path(tmp)), \
                mock.patch.object(run, 'build', return_value=outputs):
            self.assertEqual(0, run.main([]))
            self.assertEqual(1, run.main([]))
            self.assertEqual(0, run.main(['--check']))
            candidate = Path(tmp) / 'run-002/candidate.xml'
            candidate.write_bytes(b'changed')
            self.assertEqual(1, run.main(['--check']))
            self.assertEqual(b'changed', candidate.read_bytes())


if __name__ == '__main__':
    unittest.main()
