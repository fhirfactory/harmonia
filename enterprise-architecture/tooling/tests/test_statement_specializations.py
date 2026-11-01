# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Accepted statement types survive projection without changing normative level."""
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import architecture_continuum as c


class StatementSpecializationTests(unittest.TestCase):
    def test_specializations_reject_an_incompatible_native_base(self):
        bases = {
            'Axiom': 'Principle', 'ArchitecturalAxiom': 'Principle',
            'ImplementationGuideline': 'CourseOfAction', 'Mandate': 'Constraint',
            'FunctionalRequirement': 'Requirement',
            'NonFunctionalRequirement': 'Requirement',
            'InformationRequirement': 'Requirement',
            'TransitionRequirement': 'Requirement',
            'AssuranceRequirement': 'Requirement',
        }
        for specialization, native in bases.items():
            with self.subTest(specialization=specialization):
                spec = c.template(native, specialization)
                self.assertEqual(spec['metadata']['archimateType'], native)
                wrong = 'Goal' if native != 'Goal' else 'Principle'
                with self.assertRaisesRegex(ValueError, 'incompatible'):
                    c.template(wrong, specialization)
        with self.assertRaisesRegex(ValueError, 'Ungoverned specialization'):
            c.template('Requirement', 'DerivedRequirement')

    def test_projection_preserves_statement_type_and_explicit_subject_edges(self):
        _, elements = c.load()
        source = ('enterprise-architecture/design/offline-design/'
                  'Harmoni-NFR-Derived Architetural Elements - Iteration 001.md')
        statements = {b['selector']: (m, body) for m, body, _ in elements.values()
                      for b in m['sourceBindings'] if b['source'] == source
                      and b['selector'].startswith('NFR-')}
        self.assertEqual(len(statements), 150)
        self.assertTrue(set(statements).isdisjoint(
            {'NFR-116', 'NFR-120', 'NFR-128', 'NFR-134', 'NFR-140', 'NFR-144'}))
        model = c.ET.fromstring(c.project()[0])
        keys = {n.get('identifier'): n.findtext('a:name', namespaces=c.N)
                for n in model.findall('a:propertyDefinitions/a:propertyDefinition', c.N)}
        projected = {n.get('identifier'): n for n in model.findall('a:elements/a:element', c.N)}
        for selector, (metadata, body) in statements.items():
            with self.subTest(selector=selector):
                node = projected[metadata['exchange']['identifier']]
                self.assertEqual(node.get(c.XSI), metadata['archimateType'])
                props = {keys[p.get('propertyDefinitionRef')]: p.findtext('a:value', namespaces=c.N)
                         for p in node.findall('a:properties/a:property', c.N)}
                self.assertEqual(props.get('Specialization'), metadata['specialization'])
                entry = metadata['registerEntries'][0]
                self.assertIn(entry['description'], body)
                self.assertIn(entry['description'], node.findtext('a:documentation', namespaces=c.N))
                parents = [m for m, _, _ in elements.values() for edge in m['relationships']
                           if edge['archimateType'] == 'Composition'
                           and edge['target'] == node.get('identifier')
                           and edge['provenance'].get('source') == source]
                self.assertEqual(len(parents), 1)
                self.assertEqual(parents[0]['subjectGroup']['sourceId'], entry['domain'])


if __name__ == '__main__':
    unittest.main()
