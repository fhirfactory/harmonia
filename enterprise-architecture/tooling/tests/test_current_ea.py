# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Current shared semantics and explicit-path transport without legacy fixtures."""
import copy
import gzip
from pathlib import Path
import re
import sys
import tempfile
import unittest
from unittest.mock import patch

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import architecture_continuum as c
import exchange_compare as exchange
import exchange_xml as xml
import exchange_signatures as signatures
import governed_naming as naming
import containment
import ea_references
import verify_continuum_links as links


MODEL = '''<model xmlns="http://www.opengroup.org/xsd/archimate/3.0/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" identifier="model">
<name>Fixture</name><elements><element identifier="one" xsi:type="BusinessObject"><name>One</name><properties><property propertyDefinitionRef="canonical"><value>11111111111111111111111111111111</value></property></properties></element><element identifier="two" xsi:type="BusinessObject"><name>Two</name></element></elements>
<relationships><relationship identifier="composition" source="one" target="two" xsi:type="Composition"/></relationships>
<propertyDefinitions><propertyDefinition identifier="canonical" type="string"><name>canonicalId</name></propertyDefinition></propertyDefinitions></model>'''


class CurrentEaTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.path = Path(self.temp.name) / 'explicit.xml'
        self.path.write_text(MODEL)

    def test_offline_schema_and_exact_explicit_gzip_input(self):
        before = exchange.Exchange(self.path)
        self.assertEqual(before.issues, [])
        zipped = self.path.with_suffix('.xml.gz')
        zipped.write_bytes(gzip.compress(self.path.read_bytes(), mtime=0))
        after = exchange.Exchange(zipped)
        report = exchange.compare(before, after)
        self.assertEqual(report['comparison_status'], 'EQUIVALENT')
        self.assertEqual(before.xml_sha256, after.xml_sha256)
        self.assertEqual(report['relationships']['changed_records'], [])

    def test_raw_comparator_preserves_owner_text_language_and_visual_changes(self):
        before = exchange.Exchange(self.path)
        node = before.root.find('a:elements/a:element/a:name', xml.N)
        node.text = 'Changed'
        node.set('{http://www.w3.org/XML/1998/namespace}lang', 'en')
        changed = self.path.with_name('changed.xml')
        changed.write_bytes(c.ET.tostring(before.root))
        report = exchange.compare(exchange.Exchange(self.path), exchange.Exchange(changed))
        self.assertEqual(report['comparison_status'], 'DIFFERENCES')
        self.assertIn('one', report['elements']['changed_records'])
        self.assertTrue(any(f['path'].endswith('/text()') for f in report['findings']))

    def test_property_rekey_is_transport_only_but_value_drift_remains_visible(self):
        before = exchange.Exchange(self.path)
        changed = self.path.with_name('rekeyed.xml')
        changed.write_text(MODEL.replace('identifier="canonical"', 'identifier="different"').replace('propertyDefinitionRef="canonical"', 'propertyDefinitionRef="different"'))
        after = exchange.Exchange(changed)
        self.assertEqual(exchange.compare(before, after)['comparison_status'], 'EQUIVALENT')
        self.assertEqual(signatures.signature(before.elements['one'], before), signatures.signature(after.elements['one'], after))
        changed.write_text(changed.read_text().replace('11111111111111111111111111111111', '22222222222222222222222222222222'))
        self.assertEqual(exchange.compare(before, exchange.Exchange(changed))['comparison_status'], 'DIFFERENCES')

    def test_dtd_duplicate_ids_and_dangling_references_fail(self):
        self.path.write_text('<!DOCTYPE model [<!ENTITY bad "bad">]>' + MODEL)
        with self.assertRaisesRegex(ValueError, 'DTD'):
            exchange.Exchange(self.path)
        self.path.write_text(MODEL.replace('identifier="two"', 'identifier="one"'))
        self.assertTrue(exchange.Exchange(self.path).issues)
        self.path.write_text(MODEL.replace('target="two"', 'target="absent"'))
        self.assertTrue(any('endpoint' in issue for issue in exchange.Exchange(self.path).issues))

    def test_native_containment_ignores_historical_relationships_before_ancestry(self):
        model = exchange.Exchange(self.path)
        root = model.root
        self.assertEqual(containment.resolve(root).ancestry('two'), ['one', 'two'])
        excluded = containment.resolve(root, records=[{'identifier': 'composition', 'record_state': 'superseded'}])
        self.assertEqual(excluded.ancestry('two'), ['two'])
        duplicate = copy.deepcopy(root.find('a:relationships/a:relationship', xml.N))
        duplicate.set('identifier', 'duplicate')
        root.find('a:relationships', xml.N).append(duplicate)
        self.assertTrue(containment.resolve(root).findings())

    def test_lexical_spelling_and_explicit_vocabulary_do_not_allocate_identity(self):
        self.assertEqual(naming.component('FHIR REST & DataObject'), 'FhirRestAndDataObject')
        result = naming.qualified_names([{'type': 'BusinessObject', 'name': 'Care', 'key': 'care'}, {'type': 'DataObject', 'specialization': 'Topic', 'name': 'Encounter', 'key': 'encounter'}])
        self.assertEqual(result, {'friendlyName': 'Care.Encounter', 'canonicalName': 'BO-Care.TOPIC-Encounter'})
        with self.assertRaises(ValueError):
            naming.qualified_names([{'type': 'DataObject', 'name': 'Unclassified', 'key': 'unclassified'}])

    def test_authority_succession_is_explicit_and_archive_presence_is_unnecessary(self):
        root = Path(self.temp.name)
        old = root / 'docs/markdown/governance/architectural-axioms.md'
        new = root / 'enterprise-architecture/governance/architectural-axioms.md'
        self.assertEqual(ea_references.current_target(root, old, {'docs/markdown/governance/architectural-axioms.md': 'enterprise-architecture/governance/architectural-axioms.md'}), new)
        self.assertFalse(old.exists())
        self.assertTrue(ea_references.historical_target(root, old))
        self.assertFalse(ea_references.historical_target(root, new))

    def test_standing_links_require_current_targets_and_preserve_source_citations(self):
        root = Path(self.temp.name)
        config = root / 'enterprise-architecture/governance'
        config.mkdir(parents=True)
        (config / 'authority-successors.json').write_text('{"successors":{}}')
        path = root / 'enterprise-architecture/continuum/motivation/principle/test.md'
        body = '[Archive](' + str(root / 'docs/missing-history.md') + ')\n[Instructions](' + str(root / 'AGENTS.md') + ')\n[Current](' + str(config / 'current.md') + ')'
        with patch.object(c, 'load', return_value=({}, {'test': ({}, body, path)})):
            report = links.verify(root)
            self.assertEqual(report['status'], 'FAIL')
            self.assertEqual(len(report['missingTargetFiles']), 1)
            self.assertEqual(report['historicalProvenanceLinks'], 1)
            self.assertEqual(report['repositoryInstructionSourceCitations'], 1)
            (config / 'current.md').write_text('# Current\n')
            self.assertEqual(links.verify(root)['status'], 'PASS')

    def test_axiom_navigation_identifies_each_canonical_principle(self):
        elements = c.load()[1]
        axioms = {m['displayName'].split()[0]: (m, path)
                  for m, _, path in elements.values()
                  if m['archimateType'] == 'Principle'
                  and re.match(r'^AX-\d{2} ', m['displayName'])}
        self.assertEqual(set(axioms), {f'AX-{n:02}' for n in range(1, 19)})
        page = c.ROOT / 'enterprise-architecture/governance/architectural-axioms.md'
        text = page.read_text()
        for axiom, (metadata, path) in axioms.items():
            with self.subTest(axiom=axiom):
                self.assertIn(f'<a id="{axiom.lower()}"></a>', text)
                self.assertIn(f'[{metadata["displayName"]}](../continuum/motivation/principle/{path.name})', text)
        self.assertIn('sole current normative definitions', ' '.join(text.split()))
        self.assertNotIn('This document defines the Architectural Axioms', text)

    def test_current_ea_does_not_copy_axiom_definition_paragraphs(self):
        # A bounded copy guard, not a semantic classifier: contextual application
        # remains legitimate. Canonical content, prior publications and governed
        # historical review/register/evidence are deliberately outside this scan.
        definitions = []
        for metadata, body, _ in c.load()[1].values():
            if metadata['archimateType'] != 'Principle' or not re.match(r'^AX-\d{2} ', metadata['displayName']):
                continue
            section = re.search(r'^#### Axiom\s*\n(.*?)(?=^#### |\Z)', body, re.M | re.S)
            self.assertIsNotNone(section, metadata['displayName'])
            for paragraph in re.split(r'\n\s*\n', section[1].strip()):
                normalized = ' '.join(paragraph.split())
                if len(normalized.split()) >= 10:
                    definitions.append((metadata['displayName'], normalized))
        ea = c.ROOT / 'enterprise-architecture'
        for path in sorted(ea.rglob('*')):
            relative = path.relative_to(ea)
            if relative.parts[0] == 'continuum' or set(relative.parts) & {'published', '__pycache__', 'schemas'}:
                continue
            if relative.parts[:2] in (('design', 'review'), ('design', 'register'), ('design', 'evidence')):
                continue
            if not path.is_file() or path.suffix not in {'.md', '.tex', '.json', '.py', '.js'}:
                continue
            text = ' '.join(path.read_text().split())
            for name, definition in definitions:
                self.assertNotIn(definition, text, f'{relative}: copied definition of {name}; reference its Continuum Principle')


if __name__ == '__main__':
    unittest.main()
