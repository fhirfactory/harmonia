# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Review boundaries: transport noise, complete discovery and authored Views."""
import copy
import json
from pathlib import Path
import sys
import tempfile
import unittest
from unittest.mock import patch

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import architecture_continuum as c
import continuum_workbench as w
import exchange_compare as e
import exchange_xml as g
from test_current_ea import MODEL

ORG = '<organizations><item identifier="derived"><label>Elements</label><item identifierRef="one"/></item><item><label>Views</label><item><label>Folder</label><item identifierRef="view"/><item identifierRef="other-view"/></item><item><label>Empty</label></item></item></organizations>'
VIEWS = '<views><diagrams><view identifier="view" xsi:type="Diagram"><name>View</name><node identifier="node" xsi:type="Element" elementRef="one" x="0" y="0" w="120" h="55"/></view><view identifier="other-view" xsi:type="Diagram"><name>Other</name></view></diagrams></views>'


class NormalisedReviewTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.before = self.root / 'before.xml'
        self.after = self.root / 'after.xml'
        self.before.write_text(MODEL.replace('<propertyDefinitions>', ORG + '<propertyDefinitions>').replace('</model>', VIEWS + '</model>'))
        self.tree = g.parse(self.before)

    def delta(self):
        self.after.write_bytes(c.ET.tostring(self.tree))
        return e.architectural_delta(e.Exchange(self.before), e.Exchange(self.after))

    def test_generated_scalar_english_is_classified_but_raw_fidelity_is_preserved(self):
        self.tree.find('.//a:value', g.N).set(e.LANG, 'en')
        delta = self.delta()
        self.assertEqual(delta['outcome'], 'REVIEW_READY')
        self.assertFalse(delta['elements']['changed'])
        self.assertEqual(sum(delta['classified']['normalisations'].values()), 1)
        raw = e.compare(e.Exchange(self.before), e.Exchange(self.after))
        self.assertEqual(raw['classifications'][e.SEMANTIC], 1)

    def test_text_language_inheritance_and_unknown_properties_are_not_normalised(self):
        original = copy.deepcopy(self.tree)
        cases = ('text', 'french', 'inherited-french', 'unknown-key', 'multiple-values')
        for case in cases:
            with self.subTest(case=case):
                self.tree = copy.deepcopy(original)
                value = self.tree.find('.//a:value', g.N)
                value.set(e.LANG, 'fr' if case == 'french' else 'en')
                if case == 'text': value.text = 'Changed'
                if case == 'inherited-french': self.tree.set(e.LANG, 'fr')
                if case == 'unknown-key': self.tree.find('a:propertyDefinitions/a:propertyDefinition/a:name', g.N).text = 'Authored Property'
                if case == 'multiple-values': value.getparent().append(copy.deepcopy(value))
                delta = self.delta()
                self.assertTrue(delta['elements']['changed'])

    def test_derived_navigation_is_ignored_without_hiding_view_moves(self):
        self.tree.find('a:organizations/a:item/a:label', g.N).text = 'Different derived folder'
        self.tree.find('a:organizations/a:item', g.N).attrib.pop('identifier')
        delta = self.delta()
        self.assertFalse(delta['viewNavigation']['changedViews'])
        self.assertFalse(delta['representationAnomalies'])
        folder = self.tree.find('a:organizations/a:item[2]/a:item/a:label', g.N)
        folder.text = 'Moved'
        delta = self.delta()
        self.assertEqual(len(delta['viewNavigation']['changedViews']), 2)

    def test_view_order_geometry_empty_folders_and_folder_metadata_remain_visible(self):
        folder = self.tree.find('a:organizations/a:item[2]/a:item', g.N)
        leaf = folder.findall('a:item', g.N)[0]
        folder.remove(leaf); folder.append(leaf)
        self.tree.find('.//a:node', g.N).set('x', '42')
        self.tree.find('a:organizations/a:item[2]/a:item[2]/a:label', g.N).text = 'Renamed empty'
        delta = self.delta()
        self.assertEqual(len(delta['views']['changed']), 1)
        self.assertEqual(len(delta['viewNavigation']['orderChanges']), 1)
        self.assertTrue(delta['viewNavigation']['folderChanges'])

    def test_new_elements_without_ids_do_not_stop_other_elements_relationships_or_views(self):
        elements = self.tree.find('a:elements', g.N)
        for rid in ('new-one', 'new-two'):
            node = c.ET.SubElement(elements, g.tag('element'), identifier=rid)
            node.set(g.TYPE, 'DataObject'); c.ET.SubElement(node, g.tag('name')).text = rid
        rel = c.ET.SubElement(self.tree.find('a:relationships', g.N), g.tag('relationship'),
                             identifier='new-rel', source='new-one', target='new-two')
        rel.set(g.TYPE, 'Composition')
        self.tree.find('.//a:node', g.N).set('x', '17')
        delta = self.delta()
        self.assertEqual(len(delta['elements']['added']), 2)
        self.assertEqual(len(delta['relationships']['added']), 1)
        self.assertEqual(len(delta['views']['changed']), 1)
        self.assertEqual(delta['identity']['newWithoutCanonicalId'], ['new-one', 'new-two'])
        self.assertEqual(delta['canonicalIdsAllocated'], 0)

    def test_identity_loss_duplicate_claims_and_invalid_references_fail_closed_after_discovery(self):
        self.tree.find('.//a:value', g.N).text = 'bad'
        self.tree.find('a:relationships/a:relationship', g.N).set('target', 'missing')
        delta = self.delta()
        self.assertEqual(delta['outcome'], 'UNRESOLVED')
        self.assertTrue(delta['exceptionalConditions'])
        self.assertEqual(delta['elements']['retainedIds'], 2)

    def test_ambiguous_view_folder_or_repeated_reference_is_not_silently_selected(self):
        folder = self.tree.find('a:organizations/a:item[2]/a:item', g.N)
        folder.getparent().append(copy.deepcopy(folder))
        delta = self.delta()
        self.assertEqual(delta['outcome'], 'UNRESOLVED')
        self.assertTrue(any(x['kind'] == 'VIEW_NAVIGATION' for x in delta['exceptionalConditions']))

    def test_canonical_baseline_failure_is_retained_and_no_drp_is_read(self):
        self.after.write_bytes(c.ET.tostring(self.tree))
        with patch.object(c, 'project', return_value=(b'other', {})):
            result = w.normalised_review(self.before, self.after, self.root)
        self.assertEqual(result['outcome'], 'UNRESOLVED')
        self.assertEqual(result['exceptionalConditions'][0]['kind'], 'BASELINE_OR_CANONICAL_VALIDATION')

    def test_malformed_input_emits_a_condition_file_and_stops(self):
        self.after.write_text('<malformed')
        output = self.root / 'failure.json'
        argv = ['continuum_workbench.py', 'review', '--normalised', '--baseline', str(self.before),
                '--submitted', str(self.after), '--output', str(output)]
        with patch.object(sys, 'argv', argv):
            self.assertEqual(w.main(), 1)
        result = json.loads(output.read_text())
        self.assertEqual(result['outcome'], 'FAIL')
        self.assertEqual(result['exceptionalConditions'][0]['kind'], 'INPUT_OR_PROCESSING')

    def test_context_seeds_follow_changed_relationship_endpoints_without_corpus_retrieval(self):
        self.tree.find('a:relationships/a:relationship', g.N).set('source', 'two')
        delta = self.delta()
        self.assertEqual(delta['contextSeeds'], ['one', 'two'])
        self.assertEqual(len(delta['relationships']['changed']), 1)


if __name__ == '__main__':
    unittest.main()
