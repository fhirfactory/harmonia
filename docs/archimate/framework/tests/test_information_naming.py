# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Native semantic precedence, naming boundaries and immutable IA run regressions."""
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
import information_naming as n
import information_naming_run as run
import information_population as historical


class InformationNamingTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(prefix='harmonia-naming-', dir='/tmp')
        self.addCleanup(self.temp.cleanup)
        self.root = g.ET.Element(g.tag('model'), nsmap={None: g.NS, 'xsi': g.XSI}, identifier='test-model')
        g.child(self.root, 'name', 'NON-GOVERNED synthetic naming test')
        g.child(self.root, 'elements')
        g.child(self.root, 'relationships')
        g.child(self.root, 'propertyDefinitions')
        self.element('parent', 'DataObject', '[Topic] FHIR Messages', 'Topic')
        self.element('child', 'DataObject', '[Topic] Care Plan', 'Topic')

    def element(self, rid, native, label, specialization=None):
        node = g.child(self.root.find('a:elements', g.N), 'element', identifier=rid, **{g.TYPE: native})
        g.child(node, 'name', label)
        if specialization is not None:
            run.set_property(self.root, node, 'Specialization', specialization)
        return node

    def node(self, rid='child'):
        return self.root.find(f"a:elements/a:element[@identifier='{rid}']", g.N)

    def property(self, key, value, rid='child'):
        run.set_property(self.root, self.node(rid), key, value)

    def edge(self, source='parent', target='child', kind='Composition', rid='edge'):
        return g.child(self.root.find('a:relationships', g.N), 'relationship', identifier=rid,
                       source=source, target=target, **{g.TYPE: kind})

    def model(self):
        path = Path(self.temp.name) / 'input.xml'
        path.write_bytes(g.serialize(self.root))
        return d.Exchange(path)

    def proposal(self, rid='child'):
        return next(r for r in n.propose(self.model())['records'] if r['representation_id'] == rid)

    def test_legacy_businessobject_suffix_and_id_are_independent(self):
        node = self.element('bo', 'BusinessObject', 'Care Plan BO')
        run.set_property(self.root, node, 'canonicalId', 'a' * 32)
        row = self.proposal('bo')
        self.assertEqual(dict(displayName='[BusinessObject] Care Plan', friendlyName='CarePlan',
                              canonicalName='BO-CarePlan', archimateType='BusinessObject', harmoniaType=None), row['proposed'])
        self.assertEqual('a' * 32, row['governed_identity']['canonicalId'])
        self.assertNotIn('canonicalId', row['proposed'])

    def test_topic_native_semantics_override_all_stale_metadata(self):
        for key, value in dict(archimateType='BusinessObject', harmoniaType='Other',
                               displayName='Old BO', friendlyName='OldBo', canonicalName='BO-OldBo').items():
            self.property(key, value)
        self.edge()
        row = self.proposal()
        self.assertEqual('DataObject', row['proposed']['archimateType'])
        self.assertEqual('Topic', row['proposed']['harmoniaType'])
        self.assertEqual('[Topic] Care Plan', row['proposed']['displayName'])
        self.assertEqual('FhirMessages.CarePlan', row['proposed']['friendlyName'])
        self.assertEqual('TOPIC-FhirMessages.TOPIC-CarePlan', row['proposed']['canonicalName'])
        self.assertEqual('BusinessObject', row['submitted_derived']['archimateType']['values'][0])

    def test_topic_requires_explicit_specialization_not_prefix_or_derived_type(self):
        self.property('Specialization', None)
        self.property('harmoniaType', 'Topic')
        row = self.proposal()
        self.assertEqual({'archimateType': 'DataObject'}, row['proposed'])
        self.assertTrue(row['unresolved'])

    def test_lowercase_exchange_specialization_key_is_recognized(self):
        self.property('Specialization', None)
        self.property('specialization', 'Topic')
        self.assertEqual('Topic', self.proposal()['proposed']['harmoniaType'])

    def test_artifact_convention_without_inferred_counterparts(self):
        self.element('file', 'Artifact', '[Artifact] FHIR Package')
        row = self.proposal('file')
        self.assertEqual('ARTIFACT-FhirPackage', row['proposed']['canonicalName'])
        self.assertEqual('[Artifact] FHIR Package', row['proposed']['displayName'])
        self.assertIsNone(row['proposed']['harmoniaType'])
        self.assertEqual(3, len(n.propose(self.model())['records']))

    def test_unqualified_artifact_name_is_formatted(self):
        self.element('file', 'Artifact', 'FHIR Package')
        self.assertEqual('[Artifact] FHIR Package', self.proposal('file')['proposed']['displayName'])

    def test_multiple_and_incompatible_classification_or_display_are_unresolved(self):
        for native, label, specialization in (
                ('DataObject', '[Artifact] Thing', 'Topic'),
                ('BusinessObject', 'Thing BO', 'Topic'),
                ('DataObject', '[Topic] Thing', 'Unknown'),
                ('Artifact', '[Unknown] Thing', None),
                ('BusinessObject', '[BusinessObject] Thing BO', None)):
            with self.subTest(native=native, label=label):
                self.element('probe', native, label, specialization)
                self.assertTrue(self.proposal('probe')['unresolved'])
                self.root.find('a:elements', g.N).remove(self.node('probe'))
        self.property('specialization', 'Other')
        self.assertTrue(self.proposal()['unresolved'])

    def test_only_composition_contributes_context(self):
        for kind in ('Aggregation', 'Specialization', 'Realization', 'Association'):
            with self.subTest(kind=kind):
                edge = self.edge(kind=kind)
                row = self.proposal()
                self.assertEqual('CarePlan', row['proposed']['friendlyName'])
                self.assertEqual([], row['context']['composition'])
                edge.getparent().remove(edge)
        self.element('group', 'Grouping', 'Care Plan')
        self.assertEqual('CarePlan', self.proposal()['proposed']['friendlyName'])

    def test_composition_path_uses_each_component_in_order(self):
        self.element('root', 'DataObject', '[Topic] Clinical', 'Topic')
        self.edge(source='root', target='parent', rid='outer')
        self.edge()
        row = self.proposal()
        self.assertEqual('Clinical.FhirMessages.CarePlan', row['proposed']['friendlyName'])
        self.assertEqual('TOPIC-Clinical.TOPIC-FhirMessages.TOPIC-CarePlan', row['proposed']['canonicalName'])
        self.assertEqual(['root', 'parent', 'child'], row['context']['path'])

    def test_navigation_and_view_geometry_have_no_effect(self):
        before = self.proposal()['proposed']
        organizations = g.child(self.root, 'organizations')
        folder = g.child(organizations, 'item', identifier='folder')
        g.child(folder, 'label', 'Misleading Context')
        g.child(folder, 'item', identifier='ref', identifierRef='child')
        view = g.child(g.child(g.child(self.root, 'views'), 'diagrams'), 'view', identifier='view', **{g.TYPE: 'Diagram'})
        g.child(view, 'name', 'Nested information')
        node = g.child(view, 'node', identifier='visual-parent', elementRef='parent', x='0', y='0', w='200', h='100', **{g.TYPE: 'Element'})
        g.child(node, 'node', identifier='visual-child', elementRef='child', x='5', y='5', w='100', h='40', **{g.TYPE: 'Element'})
        self.assertEqual(before, self.proposal()['proposed'])

    def test_cycles_duplicate_and_competing_parents_block_only_dependent_names(self):
        self.edge()
        for edge in (dict(source='parent', target='child', rid='duplicate'),
                     dict(source='child', target='parent', rid='cycle')):
            node = self.edge(**edge)
            row = self.proposal()
            self.assertEqual('[Topic] Care Plan', row['proposed']['displayName'])
            self.assertNotIn('canonicalName', row['proposed'])
            self.assertTrue(row['unresolved'])
            node.getparent().remove(node)
        self.element('second', 'DataObject', '[Topic] Other', 'Topic')
        self.edge(source='second', rid='competing')
        self.assertNotIn('friendlyName', self.proposal()['proposed'])

    def test_missing_or_unknown_ancestor_does_not_invent_context(self):
        edge = self.edge(source='missing')
        self.assertIn('Missing Composition endpoint', self.proposal()['unresolved'][0]['reason'])
        edge.set('source', 'group')
        self.element('group', 'Grouping', 'Not Naming Authority')
        self.assertIn('Unestablished ancestor', self.proposal()['unresolved'][0]['reason'])

    def test_unproven_digit_spelling_and_empty_local_name_stop_names(self):
        self.node().find('a:name', g.N).text = '[Topic] 24/7 Availability'
        row = self.proposal()
        self.assertEqual('[Topic] 24/7 Availability', row['proposed']['displayName'])
        self.assertNotIn('friendlyName', row['proposed'])
        self.node().find('a:name', g.N).text = '[Topic] '
        self.assertTrue(self.proposal()['unresolved'])

    def test_candidate_is_idempotent_and_protects_non_metadata_content(self):
        self.edge()
        self.property('canonicalId', 'a' * 32)
        model = self.model()
        proposal = n.propose(model)
        content = run.candidate(model, proposal, 'run-001')
        self.assertEqual('PASS', run.verify_candidate(model, content, proposal, 'run-001')['exact_allowlist_preservation'])
        path = Path(self.temp.name) / 'candidate.xml'
        path.write_bytes(content)
        candidate = d.Exchange(path)
        self.assertFalse(candidate.issues)
        self.assertEqual(proposal['records'][0]['proposed'], n.propose(candidate)['records'][0]['proposed'])
        self.assertEqual(content, run.candidate(candidate, n.propose(candidate), 'run-001'))
        changed = g.ET.fromstring(content)
        changed.find('a:relationships/a:relationship', g.N).set('target', 'parent')
        with self.assertRaisesRegex(ValueError, 'protected submitted content'):
            run.verify_candidate(model, g.serialize(changed), proposal, 'run-001')

    def test_bad_or_duplicate_identity_is_retained_without_allocation(self):
        for value in ('bad', 'a' * 32):
            self.property('canonicalId', value)
            self.property('canonicalId', value, 'parent')
            proposal = n.propose(self.model())
            self.assertTrue(proposal['identity_issues'])
            self.assertEqual(0, proposal['summary']['canonical_ids_allocated'])
            self.assertTrue(all(not r['governed_identity']['unique'] for r in proposal['records']))

    def test_frozen_specimen_hash_mismatch_stops_before_processing(self):
        manifest = Path(self.temp.name) / 'bad-inputs.json'
        model = self.model()
        manifest.write_text(json.dumps(dict(incoming=dict(path=str(model.path), sha256='0' * 64))))
        with self.assertRaisesRegex(ValueError, 'Immutable input hash changed'):
            run.load_inputs(manifest)

    def test_output_requires_new_run_and_check_never_writes(self):
        outputs = {'manifest.json': b'synthetic evidence', 'proposed-metadata.json': b'{"summary": {}}'}
        with mock.patch.object(run, 'RUNS', Path(self.temp.name)), mock.patch.object(run, 'ROOT', Path(self.temp.name)), \
                mock.patch.object(run, 'build', return_value=outputs):
            self.assertEqual(0, run.main(['--run', 'run-001']))
            self.assertEqual(1, run.main(['--run', 'run-001']))
            self.assertEqual(0, run.main(['--run', 'run-001', '--check']))
            evidence = Path(self.temp.name) / 'run-001/manifest.json'
            evidence.write_bytes(b'changed')
            self.assertEqual(1, run.main(['--run', 'run-001', '--check']))
            self.assertEqual(b'changed', evidence.read_bytes())

    def test_governance_transition_checks_exact_live_and_snapshot_hashes(self):
        relative = 'docs/markdown/governance/canonical-identity-and-naming.md'
        manifest = historical.m.read(historical.COMPLETION / 'manifest.json')
        expected = manifest['governed_authority_amendments'][relative]['original_sha256']
        self.assertTrue(historical.preserved_authority(relative, expected))
        original = Path.read_bytes
        def altered(path):
            value = original(path)
            if str(path).endswith('run-001/governance/manifest.json'):
                document = json.loads(value)
                document['files'][relative]['after_sha256'] = '0' * 64
                value = json.dumps(document).encode()
            return value
        with mock.patch.object(Path, 'read_bytes', altered):
            with self.assertRaisesRegex(ValueError, 'transition manifest drift'):
                historical.preserved_authority(relative, expected)


class ImmutableInformationNamingRegressionTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.fixtures, cls.models = run.load_inputs()
        cls.proposal = n.propose(cls.models['incoming'])

    def test_submitted_information_population_and_metadata_results(self):
        summary = self.proposal['summary']
        self.assertEqual({'BusinessObject': 12, 'Topic': 70, 'UNRESOLVED': 4}, summary['classifications'])
        self.assertEqual((86, 82, 4, 59, 32, 0), tuple(summary[k] for k in
                         ('information_elements', 'fully_derived', 'unresolved', 'contextual', 'absent_identity', 'canonical_ids_allocated')))
        self.assertEqual(0, summary['native_types'].get('Artifact', 0))
        self.assertFalse(self.proposal['identity_issues'] or self.proposal['input_issues'])

    def test_all_50_frozen_baseline_businessobjects_have_prefix_proposals(self):
        proposal = n.propose(self.models['baseline'])
        rows = [r for r in proposal['records'] if r.get('classification') == 'BusinessObject']
        self.assertEqual(50, len(rows))
        for row in rows:
            self.assertEqual('[BusinessObject] ' + row['authored']['names'][0]['text'][:-3], row['proposed']['displayName'])
            self.assertFalse(row['unresolved'])

    def test_remaining_legacy_dataobjects_are_not_reclassified_by_name(self):
        rows = [r for r in self.proposal['records'] if r['unresolved']]
        self.assertEqual({'Context Topic', 'Pragma Topic', 'PetasosParcel Topic', 'Pylai.Diadromí Topic'},
                         {r['authored']['names'][0]['text'] for r in rows})
        self.assertTrue(all(set(r['proposed']) == {'archimateType'} for r in rows))

    def test_candidate_keeps_all_claims_and_representation_rebindings(self):
        incoming = self.models['incoming']
        content = run.candidate(incoming, self.proposal, 'run-001')
        self.assertEqual('PASS', run.verify_candidate(incoming, content, self.proposal, 'run-001')['xsd'])
        review = run.change_review.review(self.models['baseline'], incoming, self.models['control'])
        self.assertEqual((38, 32, 0, 38), tuple(review['population'][k] for k in
                         ('evolved', 'new', 'deleted', 'representation_rebindings')))
        for key, row in self.fixtures.items():
            self.assertEqual(row['sha256'], run.sha((run.ROOT / row['path']).read_bytes()))


if __name__ == '__main__':
    unittest.main()
