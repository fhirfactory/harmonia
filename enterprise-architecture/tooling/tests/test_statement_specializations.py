# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Accepted statement types survive projection without changing normative level."""
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import architecture_continuum as c


class StatementSpecializationTests(unittest.TestCase):
    def test_governing_axioms_retain_identity_and_principle_semantics(self):
        _, elements = c.load()
        model = c.ET.fromstring(c.project()[0])
        projected = {node.get('identifier'): node
                     for node in model.findall('a:elements/a:element', c.N)}
        definitions = {node.get('identifier'): node.findtext('a:name', namespaces=c.N)
                       for node in model.findall('a:propertyDefinitions/a:propertyDefinition', c.N)}
        axioms = {
            '5de2ae2c3b794c2d9f3a14efd51d564f': 'AX-07',
            '8882573df7ce43c6bc0f66da0a676139': 'AX-17',
            'b8cafe3cf5904d2bb9a9767260fddf78': 'AX-18',
        }
        for cid, code in axioms.items():
            with self.subTest(axiom=code):
                metadata, body, _ = elements[cid]
                node = projected[metadata['exchange']['identifier']]
                self.assertEqual(node.get(c.XSI), 'Principle')
                properties = {definitions[prop.get('propertyDefinitionRef')]:
                              prop.findtext('a:value', namespaces=c.N)
                              for prop in node.findall('a:properties/a:property', c.N)}
                self.assertEqual(properties['canonicalId'], cid)
                self.assertEqual(properties['Specialization'], 'ArchitecturalAxiom')
                self.assertEqual(node.findtext('a:name', namespaces=c.N), metadata['displayName'])
                self.assertTrue(metadata['displayName'].startswith('[ArchitecturalAxiom] ' + code + ' '))
                self.assertIn('#### Axiom\n', body)
                self.assertIn('SHALL', node.findtext('a:documentation', namespaces=c.N))

    def test_specializations_reject_an_incompatible_native_base(self):
        bases = {
            'Axiom': 'Principle', 'ArchitecturalAxiom': 'Principle',
            'ImplementationGuideline': 'CourseOfAction', 'Mandate': 'Constraint',
            'FunctionalRequirement': 'Requirement',
            'NonFunctionalRequirement': 'Requirement',
            'InformationRequirement': 'Requirement',
            'TransitionRequirement': 'Requirement',
            'AssuranceRequirement': 'Requirement',
            'SolutionFacet': 'Grouping',
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

    def test_solution_facets_keep_identity_native_type_and_accessible_names(self):
        catalogue, elements = c.load()
        facets = {cid: metadata for cid, (metadata, _, _) in elements.items()
                  if metadata.get('specialization') == 'SolutionFacet'}
        self.assertEqual(len(facets), 229)
        self.assertIsNone(catalogue['specializations']['SolutionFacet']['displayPrefix'])
        self.assertIsNone(catalogue['specializations']['SolutionFacet']['canonicalQualifier'])
        model = c.ET.fromstring(c.project()[0])
        keys = {n.get('identifier'): n.findtext('a:name', namespaces=c.N)
                for n in model.findall('a:propertyDefinitions/a:propertyDefinition', c.N)}
        projected = {n.get('identifier'): n for n in model.findall('a:elements/a:element', c.N)}
        for cid, metadata in facets.items():
            with self.subTest(canonicalId=cid):
                node = projected[metadata['exchange']['identifier']]
                self.assertEqual(node.get(c.XSI), 'Grouping')
                self.assertEqual(node.findtext('a:name', namespaces=c.N), metadata['displayName'])
                properties = {keys[p.get('propertyDefinitionRef')]: p.findtext('a:value', namespaces=c.N)
                              for p in node.findall('a:properties/a:property', c.N)}
                self.assertEqual(properties['canonicalId'], cid)
                self.assertEqual(properties['Specialization'], 'SolutionFacet')
                self.assertEqual(properties['friendlyName'], metadata['friendlyName'])
        # Programmatic context, local presentation and unresolved formal naming
        # remain separate through the native Exchange projection.
        final_names = {
            'f4af2b5206264a6fa4c5040324ad272c': (
                'InformationManagement.InformationSovereigntyAndResidency',
                'Information Sovereignty and Residency'),
            '1f441cdb8fa342cca0afe5b3b65b368d': (
                'UserExperience.UserInterfaceResponsiveness',
                'User Interface Responsiveness'),
            '46dd1f4490ae47cc854ce70ff600cb47': (
                'ArchitecturalGovernance.ArchitectureConformance.Constraints',
                'ArchitectureConformance - Constraints'),
        }
        for cid, (friendly, display) in final_names.items():
            with self.subTest(finalNaming=cid):
                metadata = facets[cid]
                node = projected[metadata['exchange']['identifier']]
                properties = {keys[p.get('propertyDefinitionRef')]: p.findtext('a:value', namespaces=c.N)
                              for p in node.findall('a:properties/a:property', c.N)}
                self.assertEqual(metadata['friendlyName'], friendly)
                self.assertEqual(metadata['displayName'], display)
                self.assertNotEqual(friendly, display)
                self.assertIsNone(metadata['canonicalName'])
                self.assertNotIn('canonicalName', properties)
                self.assertEqual(properties['friendlyName'], friendly)
                self.assertEqual(node.findtext('a:name', namespaces=c.N), display)
        relations = {n.get('identifier'): n for n in model.findall('a:relationships/a:relationship', c.N)}
        authored = relations['id-8590dea2bc74433dbc99f17927cdc092']
        self.assertEqual((authored.get(c.XSI), authored.get('source'), authored.get('target')),
                         ('Aggregation', 'id-ca7d2832c33b4b4e82827df69324daba',
                          'id-78d8fa44cc7452fea57041c8a2b8b102'))
        self.assertEqual(len(model.findall('a:views/a:diagrams/a:view', c.N)), 1304)

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
