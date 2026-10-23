# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Taxonomy corruption, coordinate continuity and semantic-preservation checks."""
import copy
from contextlib import contextmanager
import json
from pathlib import Path
import shutil
import sys
import tempfile
import unittest
from unittest.mock import patch

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import architecture_continuum as c
import continuum_navigation as nav
import publish_continuum as pub


class ContinuumNavigationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.temp = tempfile.TemporaryDirectory(prefix='harmonia-navigation-test-')
        cls.root = Path(cls.temp.name)
        for rel in (c.CONTINUUM, c.CONFIG, c.DESIGN, c.SCHEMAS):
            shutil.copytree(c.ROOT / rel, cls.root / rel, ignore=shutil.ignore_patterns('published', '__pycache__'))
        shutil.copyfile(c.ROOT / 'architecture-release.json', cls.root / 'architecture-release.json')
        shutil.copyfile(c.ROOT / 'enterprise-architecture/governance/authority-successors.json', cls.root / 'enterprise-architecture/governance/authority-successors.json')
        cls.elements = c.load(cls.root)[1]
        cls.raw = c.read_json(cls.root / nav.SOURCE)
        cls.tree = nav.load(cls.root, cls.elements)
        cls.locations = nav.by_element(cls.tree)

    @classmethod
    def tearDownClass(cls):
        cls.temp.cleanup()

    @contextmanager
    def changed(self):
        path = self.root / nav.SOURCE
        before = path.read_bytes()
        data = copy.deepcopy(self.raw)
        try:
            yield data
            path.write_bytes(c.encoded(data))
            with self.assertRaises(ValueError):
                nav.load(self.root, self.elements)
        finally:
            path.write_bytes(before)

    def test_corrupt_coverage_unknown_identity_empty_headings_and_second_identity_fail(self):
        with self.changed() as data:
            data['roots'][1]['children'][0]['children'].pop()
        with self.changed() as data:
            rows = data['roots'][1]['children'][0]['children']
            rows.append(copy.deepcopy(rows[0]))
        with self.changed() as data:
            data['roots'][1]['children'][0]['children'][0]['canonicalId'] = '0' * 32
        with self.changed() as data:
            data['roots'][4]['children'].append({'title': 'Classes', 'basis': 'Invalid empty test heading'})
        with self.changed() as data:
            data['roots'][1]['children'][0]['children'][0]['number'] = 'new-identity'
        with self.changed() as data:
            data['roots'][1]['children'][0]['title'] = 'Stakeholders'

    def test_numbering_covers_every_node_and_qualified_population(self):
        numbers = [node['number'] for node in nav.walk(self.tree)]
        self.assertEqual(len(numbers), len(set(numbers)))
        self.assertEqual(set(self.locations), set(self.elements))
        self.assertGreater(max(n.count('.') for n in numbers), 5)
        self.assertEqual([(n['namespace'], n['title']) for n in self.tree], list(nav.DOMAINS))
        for part in self.tree:
            for node in nav.walk([part]):
                for i, child in enumerate(node.get('children', []), 1):
                    self.assertEqual(child['number'], node['number'] + '.' + str(i))
        self.assertFalse(self.tree[5].get('children'))
        self.assertFalse(self.tree[6].get('children'))
        perspectives = [n for n in nav.walk(self.tree) if n.get('kind') == 'perspective']
        self.assertEqual(len(perspectives), 10)
        self.assertTrue(all(n in self.tree[1]['children'] for n in perspectives))
        branches = {n['title']: nav.descendants(n) for n in nav.walk(self.tree) if n['title'].startswith('Unclassified ')}
        self.assertEqual({k: len(v) for k, v in branches.items()}, {'Unclassified Motivation': 7, 'Unclassified Business': 69})

    def test_capability_decomposition_uses_recorded_parentage_without_standing_branches(self):
        snapshots, keys = {}, {}
        for cid, (m, _, _) in self.elements.items():
            for row in m['exchange']['properties']:
                if row['ref'] == 'propid-14':
                    data = json.loads(row['values'][0]['text'])
                    snapshots[cid] = data
                    keys[data['key']] = cid
        parents = {child.get('canonicalId'): node for node in nav.walk(self.tree) for child in node.get('children', []) if child.get('canonicalId')}
        for cid, data in snapshots.items():
            if self.elements[cid][0]['archimateType'] != 'Capability':
                continue
            parent = data.get('parent_key') or data.get('owner_key')
            if parent:
                self.assertEqual(parents[cid].get('canonicalId'), keys[parent], cid)
        # Business contexts refer to Strategy; no second capability population.
        self.assertFalse(any(self.elements[cid][0]['archimateType'] == 'Capability' for cid in nav.descendants(self.tree[3])))

    def test_topics_stay_logical_and_classes_are_not_invented(self):
        topics = next(n for n in nav.walk(self.tree) if n['path'] == ['Application', 'Information Architecture', 'Topics'])
        self.assertEqual(len(nav.descendants(topics)), 70)
        self.assertTrue(all(self.elements[cid][0].get('specialization') == 'Topic' for cid in nav.descendants(topics)))
        for name in ['Pragma Topic', 'Context Topic', 'Pylai.Diadromí Topic', 'PetasosParcel Topic']:
            cid, = [cid for cid, (m, _, _) in self.elements.items() if m['displayName'] == name]
            self.assertNotIn(cid, nav.descendants(topics))
        self.assertFalse(any(n['title'] == 'Classes' for n in nav.walk(self.tree)))
        middleware = next(n for n in nav.walk(self.tree) if n['title'] == 'Middleware Architecture')
        self.assertEqual(middleware['path'][:2], ['Application', 'System-of-Systems Architecture'])

    def test_projection_and_browser_expose_the_same_full_coordinates_without_semantic_changes(self):
        with patch.object(c.uuid, 'uuid4', side_effect=AssertionError('Navigation allocated identity')):
            content, _ = c.project(self.root)
            output = self.root / c.PUBLICATION / 'test'
            artifacts = pub.outputs(self.root, output)
        model = c.ET.fromstring(content)
        rows = {n.get('identifier'): n for n in model.findall('a:elements/a:element', c.N)}
        org = {n.get('identifier'): n for n in model.findall('.//a:organizations//a:item', c.N)}
        for node in nav.walk(self.tree):
            self.assertEqual(org[nav.transport_id(node)].findtext('a:label', namespaces=c.N), nav.title(node))
        for cid, node in self.locations.items():
            m = self.elements[cid][0]
            element = rows[m['exchange']['identifier']]
            self.assertEqual(element.findtext('a:name', namespaces=c.N), m['displayName'])
            number = element.findtext('a:properties/a:property[@propertyDefinitionRef="continuum-navigation-number"]/a:value', namespaces=c.N)
            self.assertEqual(number, node['number'])
        expected = {r['identifier']: r for m, _, _ in self.elements.values() for r in m['relationships']}
        self.assertEqual({r.get('identifier') for r in model.findall('a:relationships/a:relationship', c.N)}, set(expected))
        for edge in model.findall('a:relationships/a:relationship', c.N):
            original = expected[edge.get('identifier')]
            self.assertEqual((edge.get(c.XSI), edge.get('source'), edge.get('target')),
                             (original['archimateType'], original['source'], original['target']))
        from lxml import html
        doc = html.fromstring(artifacts['continuum.html'], parser=html.HTMLParser(encoding='utf-8', huge_tree=True))
        exposed = json.loads(doc.get_element_by_id('architecture').text)
        self.assertEqual({r['id']: r['number'] for r in exposed}, {cid: n['number'] for cid, n in self.locations.items()})
        self.assertEqual(doc.xpath('//nav[@id="hierarchy"]//li/@data-number'), [n['number'] for n in nav.walk(self.tree)])
        self.assertEqual(json.loads(artifacts['navigation.json'])['roots'], self.tree)

    def test_relocation_changes_coordinates_without_changing_identity_names_or_semantics(self):
        path = self.root / nav.SOURCE
        original = path.read_bytes()
        source_bytes = {cid: p.read_bytes() for cid, (_, _, p) in self.elements.items()}
        changed = copy.deepcopy(self.raw)
        rows = changed['roots'][1]['children'][0]['children']
        first, second = rows[0]['canonicalId'], rows[1]['canonicalId']
        rows[0], rows[1] = rows[1], rows[0]
        try:
            path.write_bytes(c.encoded(changed))
            after = nav.by_element(nav.load(self.root, self.elements))
            self.assertEqual(after[first]['number'], self.locations[second]['number'])
            self.assertEqual(after[second]['number'], self.locations[first]['number'])
            self.assertEqual(source_bytes, {cid: p.read_bytes() for cid, (_, _, p) in self.elements.items()})
        finally:
            path.write_bytes(original)

    def test_creation_placement_resolves_element_and_capability_context_names(self):
        # These raw nodes deliberately have no stored title: the name belongs
        # to the element, so placement must resolve it without copying it.
        for key in ('canonicalId', 'contextCanonicalId'):
            parent = next(n for n in nav.walk(self.tree) if n.get(key) and n.get('children'))
            result = nav.with_element(self.raw, '0' * 32, parent['path'], self.elements)
            actual = next(n for n in nav.walk(result['roots']) if n.get(key) == parent[key])
            self.assertEqual(actual['children'][-1]['canonicalId'], '0' * 32)
            self.assertNotIn('title', actual)
        with self.assertRaisesRegex(ValueError, 'Missing/ambiguous'):
            nav.with_element(self.raw, '0' * 32, ['Application', 'Missing context'], self.elements)
        self.assertFalse(any(n.get('canonicalId') == '0' * 32 for n in nav.walk(self.raw['roots'])))
