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
from current_source_fixture import copy_retained_inputs


class ContinuumNavigationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.temp = tempfile.TemporaryDirectory(prefix='harmonia-navigation-test-')
        cls.root = Path(cls.temp.name)
        for rel in (c.CONTINUUM, c.CONFIG, c.DESIGN, c.SCHEMAS):
            shutil.copytree(c.ROOT / rel, cls.root / rel, ignore=shutil.ignore_patterns('published', '__pycache__'))
        shutil.copyfile(c.ROOT / 'architecture-release.json', cls.root / 'architecture-release.json')
        shutil.copyfile(c.ROOT / 'enterprise-architecture/governance/authority-successors.json', cls.root / 'enterprise-architecture/governance/authority-successors.json')
        copy_retained_inputs(c.ROOT, cls.root)
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
        perspectives = [n for n in nav.walk(self.tree) if n.get('kind') == 'index-entry']
        self.assertEqual(len(perspectives), 12)
        self.assertTrue(all(n['number'].startswith('R.1.') for n in perspectives))
        self.assertEqual(len(self.tree[1]['children']), 10)
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
            if self.elements[cid][0]['identity'].get('correspondence'):
                continue
            parent = data.get('parent_key') or data.get('owner_key')
            if parent:
                self.assertEqual(parents[cid].get('canonicalId'), keys[parent], cid)
        # Business contexts refer to Strategy; no second capability population.
        self.assertFalse(any(self.elements[cid][0]['archimateType'] == 'Capability' for cid in nav.descendants(self.tree[3])))

    def test_history_uses_governed_correspondence_and_preserves_distinct_exchange_responsibilities(self):
        pairs = {cid: m['identity']['correspondence']['successorCanonicalId']
                 for cid, (m, _, _) in self.elements.items() if m['identity'].get('correspondence')}
        self.assertEqual(len(pairs), 214)
        self.assertEqual(sum(self.elements[a][0]['displayName'] == self.elements[b][0]['displayName'] for a, b in pairs.items()), 212)
        for old, successor in pairs.items():
            self.assertEqual(self.locations[old]['kind'], 'historical')
            self.assertTrue(self.locations[old]['number'].startswith('R.2.'))
            self.assertEqual(self.locations[old]['successorReference']['canonicalId'], successor)
            self.assertIn(old, [r['canonicalId'] for r in self.locations[successor]['historicalReferences']])
        bc, old_bec, bec = ('baad6b6117084311acbf5fc4044dfabf', '330d1c43856a4322a2e9f331b1a5ed20', 'a61a15908ed544e6acf3e9cab63a0267')
        self.assertEqual(pairs[old_bec], bec)
        self.assertNotEqual(bc, bec)
        self.assertIn('Business Capabilities', self.locations[bc]['path'])
        self.assertIn('Business Enabling Capabilities', self.locations[bec]['path'])

    def test_complete_relevant_contexts_and_existing_stakeholder_associations_are_guarded(self):
        contexts = [n for n in nav.walk(self.tree) if n.get('contextRole') == 'capability']
        self.assertEqual(len(contexts), 89)
        self.assertEqual(sum(any(ch.get('title') in ('Business Services', 'Business Functions', 'Business Processes', 'Business Events')
                                 for ch in n.get('children', [])) for n in contexts), 72)
        for node in contexts:
            self.assertEqual(node['occurrence'], 'contextual')
            self.assertEqual(node['canonicalReference']['number'], self.locations[node['contextCanonicalId']]['number'])
        with self.changed() as data:
            behaviour = next(n for n in nav.walk(data['roots']) if n.get('title') == 'Business Behaviour')
            ancestor = next(n for n in behaviour['children'] if any(ch.get('contextCanonicalId') for ch in n['children']))
            child = next(n for n in ancestor['children'] if n.get('contextCanonicalId'))
            behaviour['children'].append(child)
            ancestor['children'].remove(child)
        with self.changed() as data:
            context = next(n for n in nav.walk(data['roots']) if n.get('contextRole') == 'stakeholder')
            context['relationshipIds'] = ['invented-association']
        with self.changed() as data:
            group = next(n for n in nav.walk(data['roots']) if n.get('associatedCanonicalId'))
            group['children'].pop()
        stakeholders = [n for n in nav.walk(self.tree) if n.get('contextRole') == 'stakeholder']
        self.assertEqual(len(stakeholders), 4)
        self.assertEqual(sum(len(n['relationshipIds']) for n in stakeholders), 4)

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
        for cid, node in self.locations.items():
            m = self.elements[cid][0]
            element = rows[m['exchange']['identifier']]
            self.assertEqual(element.findtext('a:name', namespaces=c.N), m['displayName'])
            number = element.findtext('a:properties/a:property[@propertyDefinitionRef="continuum-navigation-number"]/a:value', namespaces=c.N)
            self.assertEqual(number, node['number'])
            metadata = element.findtext('a:properties/a:property[@propertyDefinitionRef="continuum-navigation-references"]/a:value', namespaces=c.N)
            self.assertEqual(json.loads(metadata), nav.transport_metadata(self.tree)[cid])
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

    def test_catalogues_cover_every_element_and_explicit_source_once(self):
        from collections import Counter
        model = c.ET.fromstring(c.project(self.root)[0])
        catalogue = c.read_json(self.root / c.CONFIG / 'type-catalogue.json')
        org = model.find('a:organizations', c.N)
        placements = org.findall('.//a:item[@identifierRef]', c.N)
        by_rep = {m['exchange']['identifier']: cid for cid, (m, _, _) in self.elements.items()}
        edges = {e.get('identifier'): e for e in model.findall('a:relationships/a:relationship', c.N)}
        self.assertEqual(Counter(p.get('identifierRef') for p in placements if p.get('identifierRef') in by_rep),
                         Counter(by_rep.keys()))
        self.assertEqual(Counter(p.get('identifierRef') for p in placements if p.get('identifierRef') in edges),
                         Counter(edges.keys()))
        for item in placements:
            ref = item.get('identifierRef')
            if ref in by_rep:
                m = self.elements[by_rep[ref]][0]
                path = [n.findtext('a:label', namespaces=c.N) for n in reversed(list(item.iterancestors(c.tag('item'))))]
                self.assertEqual(path, [nav.native_layer(m, catalogue), 'Catalogue', m['archimateType']])
                self.assertEqual(item.findtext('a:label', namespaces=c.N), m['displayName'])
                self.assertEqual(item.findall('a:item', c.N), [])
            elif ref in edges:
                cid = by_rep[edges[ref].get('source')]
                self.assertEqual(item.getparent().get('identifier'), 'catalogue-source-' + cid)
                self.assertEqual(item.getparent().getparent().findtext('a:label', namespaces=c.N), 'Catalogue')
                self.assertEqual(item.getparent().getparent().getparent().findtext('a:label', namespaces=c.N), 'Relations')
        sources = {by_rep[e.get('source')] for e in edges.values()}
        folders = org.findall('.//a:item[@identifier="catalogue-relations"]/a:item', c.N)
        self.assertEqual({f.get('identifier') for f in folders}, {'catalogue-source-' + cid for cid in sources})
        self.assertEqual(len({f.findtext('a:label', namespaces=c.N) for f in folders}), len(sources))
        # Native Grouping remains Other even for Package / RequirementGroup;
        # specialisation properties do not create catalogue levels.
        self.assertTrue(all(nav.native_layer(m, catalogue) == 'Other' for m, _, _ in self.elements.values()
                            if m['archimateType'] == 'Grouping'))

    def test_catalogue_is_independent_of_navigation_edges_specialisation_and_input_folders(self):
        import exchange_xml as xml
        catalogue = c.read_json(self.root / c.CONFIG / 'type-catalogue.json')
        model = c.ET.fromstring(c.project(self.root)[0])
        expected = xml.signature(model.find('a:organizations', c.N))
        elements = copy.deepcopy(self.elements)
        for m, _, _ in elements.values():
            m['specialization'] = 'ignored-by-catalogue'
            m['relationships'] = []  # catalogue uses the model's explicit edges
        nav.project(model, elements, catalogue)
        self.assertEqual(xml.signature(model.find('a:organizations', c.N)), expected)
        # Changing explicit source (even when storage owner stays unchanged)
        # must move exactly that relationship to its source catalogue.
        edge = model.find('a:relationships/a:relationship', c.N)
        cid, (m, _, _) = next((cid, row) for cid, row in elements.items()
                              if row[0]['exchange']['identifier'] != edge.get('source'))
        edge.set('source', m['exchange']['identifier'])
        nav.project(model, elements, catalogue)
        entry = model.find('.//a:organizations//a:item[@identifierRef="' + edge.get('identifier') + '"]', c.N)
        self.assertEqual(entry.getparent().get('identifier'), 'catalogue-source-' + cid)

    def test_all_native_types_exact_names_and_collision_safe_sources(self):
        catalogue = c.read_json(self.root / c.CONFIG / 'type-catalogue.json')
        model = c.secure_xml(self.root / c.DESIGN / 'projection-template.xml')
        elements = {}
        for i, native in enumerate(catalogue['types'], 1):
            cid = format(i, '032x')
            name = 'I.5.1 Meaningful governed identifier' if i == 1 else 'Same name'
            m = dict(canonicalId=cid, archimateType=native, displayName=name,
                     specialization='not-a-folder', exchange={'identifier': 'fixture-' + cid})
            elements[cid] = (m, '', None)
            edge = c.ET.SubElement(model.find('a:relationships', c.N), c.tag('relationship'),
                identifier='edge-' + cid, source=m['exchange']['identifier'], target=m['exchange']['identifier'])
        nav.project(model, elements, catalogue)
        org = model.find('a:organizations', c.N)
        for cid, (m, _, _) in elements.items():
            entry = org.find('.//a:item[@identifierRef="' + m['exchange']['identifier'] + '"]', c.N)
            self.assertEqual(entry.getparent().findtext('a:label', namespaces=c.N), m['archimateType'])
            self.assertEqual(entry.findtext('a:label', namespaces=c.N), m['displayName'])
        sources = org.findall('.//a:item[@identifier="catalogue-relations"]/a:item', c.N)
        self.assertEqual(len(sources), 60)
        self.assertEqual(len({n.findtext('a:label', namespaces=c.N) for n in sources}), 60)

    def test_workbench_view_signatures_detect_authored_view_changes(self):
        import continuum_workbench as workbench
        model = c.secure_xml(self.root / c.DESIGN / 'projection-template.xml')
        before = workbench.signatures(model, 'views/diagrams', 'view')
        self.assertEqual(len(before), 1285)
        first = model.find('a:views/a:diagrams/a:view', c.N)
        first.find('a:name', c.N).text += ' changed'
        after = workbench.signatures(model, 'views/diagrams', 'view')
        self.assertNotEqual(before[first.get('identifier')], after[first.get('identifier')])

    def test_non_view_navigation_regenerates_with_missing_or_misleading_input_preserving_empty_view_folders(self):
        import exchange_xml as xml
        catalogue = c.read_json(self.root / c.CONFIG / 'type-catalogue.json')
        original = c.ET.fromstring(c.project(self.root)[0])
        org = original.find('a:organizations', c.N)
        views = next(n for n in org if n.findtext('a:label', namespaces=c.N) == 'Views')
        empty = c.ET.SubElement(views, c.tag('item'), identifier='authored-empty-folder')
        c.ET.SubElement(empty, c.tag('label'), {c.LANG: 'en'}).text = 'Authored empty folder'
        c.ET.SubElement(empty, c.tag('documentation')).text = 'Preserve this authored View navigation.'
        expected_views = xml.signature(views)
        expected_semantics = [xml.signature(original.find('a:' + k, c.N)) for k in ('elements', 'relationships', 'views')]
        outputs = []
        for misleading in (False, True):
            model = copy.deepcopy(original)
            incoming = model.find('a:organizations', c.N)
            for node in list(incoming):
                if node.findtext('a:label', namespaces=c.N) != 'Views': incoming.remove(node)
            if misleading:
                wrong = c.ET.SubElement(incoming, c.tag('item'), identifier='misleading-derived')
                c.ET.SubElement(wrong, c.tag('label')).text = 'Incorrect semantic ancestry'
                c.ET.SubElement(wrong, c.tag('item'), identifierRef=next(iter(self.elements.values()))[0]['exchange']['identifier'])
            nav.project(model, self.elements, catalogue)
            retained = next(n for n in incoming if n.findtext('a:label', namespaces=c.N) == 'Views')
            self.assertEqual(xml.signature(retained), expected_views)
            self.assertEqual([xml.signature(model.find('a:' + k, c.N)) for k in ('elements', 'relationships', 'views')], expected_semantics)
            outputs.append(xml.signature(incoming))
        self.assertEqual(outputs[0], outputs[1])

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
        result = nav.with_element(self.raw, '0' * 32, ['Cross-Cutting References', 'Stakeholder Index'], self.elements)
        self.assertEqual(result['roots'][-1]['children'][0]['children'][-1]['kind'], 'index-entry')
