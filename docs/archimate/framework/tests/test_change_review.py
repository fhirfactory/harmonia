# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Identity and interpretation regression tests; no architectural reconciliation."""
import copy
from collections import Counter
import gzip
import json
from pathlib import Path
import sys
import tempfile
import unittest

BASE = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(BASE / 'tools'))
import change_review as c
import diagnose_roundtrip as d

FIXTURE = BASE / 'tests/fixtures/roundtrip/future-contract.xml'


class ChangeReviewTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(prefix='harmonia-change-review-', dir='/tmp')
        self.addCleanup(self.temp.cleanup)
        self.before = d.Exchange(FIXTURE)
        self.root = copy.deepcopy(self.before.root)

    def incoming(self, name='incoming'):
        path = Path(self.temp.name) / (name + '.xml')
        path.write_bytes(d.g.serialize(self.root))
        return d.Exchange(path)

    def node(self, rid='test-function'):
        return self.root.find(f"a:elements/a:element[@identifier='{rid}']", d.g.N)

    def property(self, key, value, rid='test-function'):
        node = self.node(rid)
        props = node.find('a:properties', d.g.N)
        definition = self.before.definition_ids.get(key)
        if definition is None:
            definition = 'test-extra-' + key
            definitions = self.root.find('a:propertyDefinitions', d.g.N)
            if definitions.find(f"a:propertyDefinition[@identifier='{definition}']", d.g.N) is None:
                d.g.child(d.g.child(definitions, 'propertyDefinition', identifier=definition, type='string'), 'name', key)
        for prop in list(props):
            if prop.get('propertyDefinitionRef') == definition:
                props.remove(prop)
        if value is not None:
            d.g.child(d.g.child(props, 'property', propertyDefinitionRef=definition), 'value', value)

    def rebind(self, old='test-function', new='changed-representation'):
        self.node(old).set('identifier', new)
        for node in self.root.iter():
            for key in ('source', 'target', 'elementRef', 'identifierRef'):
                if node.get(key) == old:
                    node.set(key, new)

    def topic(self, rid='test-function'):
        self.node(rid).set(d.g.TYPE, 'DataObject')
        self.node(rid).find('a:name', d.g.N).text = '[Topic] Synthetic'
        self.property('Specialization', 'Topic', rid)

    def composition(self, identifier='test-composition', source='test-feature', target='test-function', kind='Composition'):
        rel = d.g.child(self.root.find('a:relationships', d.g.N), 'relationship',
                        identifier=identifier, source=source, target=target)
        rel.set(d.g.TYPE, kind)

    def review(self, control=None):
        return c.review(self.before, self.incoming(), control)

    def test_same_canonical_and_exchange_identity(self):
        report = self.review()
        self.assertEqual('DETECTED', report['status'])
        self.assertEqual(3, report['population']['canonical_matches'])
        self.assertEqual(0, report['population']['evolved'])
        self.assertEqual([], report['representation_rebindings'])

    def test_representation_rebinding_keeps_canonical_and_both_provenances(self):
        self.rebind()
        incoming = self.incoming()
        report = d.compare(self.before, incoming)['architectural_review']
        self.assertEqual(1, report['population']['evolved'])
        self.assertEqual(0, report['population']['new'])
        self.assertEqual(0, report['population']['deleted'])
        row, = report['representation_rebindings']
        self.assertEqual('test-function', row['baseline']['representation_id'])
        self.assertEqual('changed-representation', row['incoming']['representation_id'])
        self.assertEqual(self.before.sha256, row['baseline']['sha256'])
        self.assertEqual(incoming.sha256, row['incoming']['sha256'])
        self.assertTrue(row['incoming']['line'])
        self.assertEqual('abcdef0123454abc8def0123456789ab', row['canonicalId'])
        diagnostic = d.diagnose(incoming, previous=self.before)
        self.assertEqual([], diagnostic['canonical_id']['continuity_findings'])
        self.assertEqual(1, len(diagnostic['canonical_id']['representation_rebindings']))

    def test_native_retype_and_rename_preserve_canonical_continuity(self):
        for kind, name, expected in [('DataObject', None, 'native_type'), (None, 'Renamed', 'names')]:
            with self.subTest(expected=expected):
                self.root = copy.deepcopy(self.before.root)
                if kind:
                    self.node().set(d.g.TYPE, kind)
                if name:
                    self.node().find('a:name', d.g.N).text = name
                report = self.review()
                self.assertEqual(3, report['population']['canonical_matches'])
                self.assertIn(expected, report['evolved'][0]['changes'])
                self.assertEqual(0, report['population']['new'] + report['population']['deleted'])

    def test_topic_specialization_is_native_dataobject_not_derived_metadata(self):
        self.topic()
        report = self.review()
        row, = report['evolved']
        self.assertTrue(row['authored']['topic'])
        self.assertEqual('DataObject', row['authored']['native_type'])
        self.assertEqual('Topic', row['authored']['specialization']['single'])
        findings = {r['property']: r['finding'] for r in row['stale_metadata']}
        self.assertEqual('STALE_OR_CONFLICTING_DERIVED_METADATA', findings['archimateType'])
        self.assertEqual('STALE_OR_CONFLICTING_DERIVED_METADATA', findings['displayName'])
        self.assertEqual('DERIVATION_REVIEW_REQUIRED', findings['friendlyName'])
        self.assertEqual('DERIVATION_REVIEW_REQUIRED', findings['canonicalName'])

    def test_topic_specialization_alone_with_same_ids_is_an_evolution(self):
        self.node().set(d.g.TYPE, 'DataObject')
        self.before = self.incoming('baseline')
        self.property('Specialization', 'Topic')
        report = self.review()
        self.assertEqual(1, report['population']['evolved'])
        self.assertEqual('PRESERVED', report['evolved'][0]['representation_identity'])
        self.assertIn('specialization', report['evolved'][0]['changes'])

    def test_dataobject_or_display_qualifier_alone_does_not_establish_topic(self):
        self.node().set(d.g.TYPE, 'DataObject')
        self.assertFalse(self.review()['evolved'][0]['authored']['topic'])
        self.node().find('a:name', d.g.N).text = '[Topic] Synthetic'
        report = self.review()
        self.assertEqual('UNRESOLVED', report['status'])
        self.assertFalse(report['evolved'][0]['authored']['topic'])

    def test_incompatible_or_multiple_specializations_are_unresolved(self):
        self.property('Specialization', 'Topic')
        self.assertEqual('UNRESOLVED', self.review()['status'])
        self.topic()
        self.property('specialization', 'Other')
        self.assertEqual('UNRESOLVED', self.review()['status'])

    def test_new_element_without_canonical_identity_is_new_despite_identical_name(self):
        clone = copy.deepcopy(self.node())
        clone.set('identifier', 'genuinely-new')
        clone.remove(clone.find('a:properties', d.g.N))
        self.root.find('a:elements', d.g.N).append(clone)
        report = self.review()
        self.assertEqual(1, report['population']['new'])
        self.assertEqual(0, report['population']['evolved'])
        self.assertEqual('genuinely-new', report['new'][0]['incoming']['representation_id'])

    def test_genuine_disappearance_of_established_identity(self):
        self.node().getparent().remove(self.node())
        for node in list(self.root.iter()):
            if (node.get('elementRef') == 'test-function' or
                    node.get('identifier') in ('test-realization', 'test-connection') or
                    node.get('identifierRef') in ('test-function', 'test-realization')):
                node.getparent().remove(node)
        relationships = self.root.find('a:relationships', d.g.N)
        self.root.remove(relationships)
        incoming = self.incoming()
        self.assertEqual([], incoming.issues)
        report = self.review()
        self.assertEqual(1, report['population']['deleted'])
        self.assertEqual(['abcdef0123454abc8def0123456789ab'], report['correlation']['deleted_canonical_ids'])
        self.assertEqual('DETECTED', report['status'])
        self.assertTrue(d.diagnose(incoming, previous=self.before)['canonical_id']['continuity_findings'])

    def test_lost_or_changed_canonical_on_same_representation_is_not_new_or_deleted(self):
        for value in (None, 'f' * 32, ' malformed '):
            with self.subTest(value=value):
                self.root = copy.deepcopy(self.before.root)
                self.property('canonicalId', value)
                report = self.review()
                self.assertEqual('UNRESOLVED', report['status'])
                self.assertEqual(0, report['population']['new'] + report['population']['deleted'])
                self.assertEqual(1, report['population']['unresolved_baseline'])

    def test_duplicate_canonical_never_uses_representation_fallback(self):
        self.property('canonicalId', 'abcdef0123454abc8def0123456789ab', 'test-feature')
        report = self.review()
        self.assertEqual('UNRESOLVED', report['status'])
        self.assertEqual(1, report['population']['canonical_matches'])
        self.assertTrue(any(i['kind'] == 'DUPLICATE_CANONICAL_ID' for i in report['issues']))
        self.assertEqual(0, report['population']['new'] + report['population']['deleted'])

    def test_multiple_claims_and_ambiguous_property_definition_do_not_correlate_as_new(self):
        prop = self.before.claims(self.node(), 'canonicalId')[0]
        prop.getparent().append(copy.deepcopy(prop))
        report = self.review()
        self.assertEqual('UNRESOLVED', report['status'])
        self.assertEqual(0, report['population']['new'] + report['population']['deleted'])
        self.assertEqual(1, report['population']['unresolved_incoming'])
        definition = self.root.find("a:propertyDefinitions/a:propertyDefinition[@identifier='test-p-id']", d.g.N)
        clone = copy.deepcopy(definition)
        clone.set('identifier', 'ambiguous-id-definition')
        definition.getparent().append(clone)
        self.rebind()
        report = self.review()
        self.assertEqual('UNRESOLVED', report['status'])
        self.assertEqual(0, report['population']['new'] + report['population']['deleted'])

    def test_two_absent_canonical_claims_can_correlate_by_representation_only(self):
        self.property('canonicalId', None)
        self.before = self.incoming('baseline')
        report = self.review()
        self.assertEqual(1, report['population']['representation_only_matches'])
        self.assertEqual(2, report['population']['canonical_matches'])

    def test_composition_is_explicit_independent_of_folder_and_view_nesting(self):
        self.topic()
        self.topic('test-feature')
        view = self.root.find('a:views/a:diagrams/a:view', d.g.N)
        child = view.find("a:node[@identifier='test-function-node']", d.g.N)
        view.remove(child)
        view.find("a:node[@identifier='test-feature-node']", d.g.N).append(child)
        self.assertEqual([], self.review()['topic_graph']['composition_edges'])
        self.composition(kind='Aggregation')
        self.assertEqual([], self.review()['topic_graph']['composition_edges'])
        self.composition(identifier='actual-composition')
        report = self.review()
        self.assertEqual(1, len(report['topic_graph']['composition_edges']))
        row = next(r for r in report['evolved'] if r['incoming_id'] == 'test-function')
        self.assertEqual('canonical:1234567890ab4def8123456789abcde0',
                         row['authored']['composition_context'][0]['source_identity'])
        self.assertIn('composition_context', row['changes'])

    def test_topic_composition_graph_ambiguities_are_not_repaired(self):
        for rid in ('test-function', 'test-feature', 'test-capability'):
            self.topic(rid)
        self.composition('a')
        self.composition('b')
        self.composition('c', source='test-capability')
        self.composition('d', source='test-function', target='test-feature')
        report = self.review()
        self.assertEqual('UNRESOLVED', report['status'])
        self.assertTrue(report['topic_graph']['cycles'])
        self.assertTrue(report['topic_graph']['duplicate_endpoint_pairs'])
        self.assertTrue(report['topic_graph']['competing_parents'])

    def test_control_subtraction_is_exact_and_does_not_suppress_authored_documentation(self):
        self.property('friendlyName', 'Transport variation')
        control = self.incoming('control')
        doc = d.g.child(self.node(), 'documentation', 'Authored addition')
        self.node().remove(doc)
        self.node().insert(1, doc)  # Exchange documentation precedes properties.
        report = self.review(control)
        row, = report['evolved']
        self.assertEqual({'documentation'}, set(row['changes']))
        self.assertIn('properties', row['transport_differences'])

    def test_control_identity_conflict_remains_unresolved(self):
        self.property('canonicalId', 'f' * 32)
        control = self.incoming('control')
        self.root = copy.deepcopy(self.before.root)
        self.assertEqual('UNRESOLVED', self.review(control)['status'])

    def test_deterministic_read_only_and_lifecycle_separation(self):
        original = FIXTURE.read_bytes()
        self.topic()
        incoming = self.incoming()
        first = d.encoded(c.review(self.before, incoming))
        self.assertEqual(first, d.encoded(c.review(self.before, incoming)))
        self.assertEqual(original, FIXTURE.read_bytes())
        self.assertEqual('NOT_RECORDED', json.loads(first)['lifecycle']['disposition'])
        self.assertEqual('NOT_AUTHORISED', json.loads(first)['lifecycle']['execution'])


class InformationChangeReviewRegressionTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        manifest = json.loads((BASE / 'tests/fixtures/roundtrip/information-change-review.json').read_text())
        cls.models = {}
        for key, fixture in manifest.items():
            path = d.ROOT / fixture['path']
            if not path.exists():
                raise FileNotFoundError('Required hash-pinned regression input missing: ' + str(path))
            if d.sha(path) != fixture['sha256']:
                raise ValueError('Hash-pinned Change Review input changed: ' + str(path))
            cls.models[key] = d.Exchange(path)
        cls.control = c.review(cls.models['baseline'], cls.models['control'], cls.models['control'])
        cls.raw = d.compare(cls.models['baseline'], cls.models['incoming'], cls.models['control'])

    def test_no_edit_control_has_no_architectural_evolution(self):
        report = self.control
        self.assertEqual('DETECTED', report['status'])
        self.assertEqual((0, 0, 0, 1362), tuple(report['population'][k] for k in ('evolved', 'new', 'deleted', 'unchanged')))
        self.assertEqual('ac7fad3d30b230e894fa12e64e74e77939fc6574dd3336a8ddd7569f70c22b65',
                         report['inputs']['control']['xml_sha256'])

    def test_real_export_is_38_evolved_32_new_zero_deleted_with_preserved_ids(self):
        report = self.raw['architectural_review']
        self.assertEqual('DETECTED', report['status'])
        self.assertEqual((38, 32, 0, 1324, 1362, 38), tuple(report['population'][k] for k in
                         ('evolved', 'new', 'deleted', 'unchanged', 'canonical_matches', 'representation_rebindings')))
        self.assertEqual(38, len({row['canonicalId'] for row in report['evolved']}))
        self.assertEqual(38, len(self.raw['elements']['missing_ids']))
        self.assertEqual(70, len(self.raw['elements']['additional_ids']))
        for row in report['evolved']:
            self.assertEqual('BusinessObject', row['changes']['native_type']['baseline'])
            self.assertEqual('DataObject', row['authored']['native_type'])
            self.assertTrue(row['authored']['topic'])
            self.assertEqual('REBOUND', row['representation_identity'])
            self.assertTrue(row['canonicalId'])
            self.assertTrue({'names', 'documentation', 'specialization', 'navigation', 'composition_context'} <= row['changes'].keys())
        graph = report['topic_graph']
        self.assertEqual(70, len(graph['nodes']))
        self.assertEqual(59, len(graph['composition_edges']))
        self.assertFalse(graph['cycles'] or graph['duplicate_endpoint_pairs'] or graph['competing_parents'])
        self.assertTrue(all(row['authored']['topic'] for row in report['new']))

    def test_stale_metadata_does_not_override_authored_semantics(self):
        report = self.raw['architectural_review']
        counts = Counter((item['property'], item['finding']) for row in report['evolved'] for item in row['stale_metadata'])
        self.assertEqual(6, counts['archimateType', 'STALE_OR_CONFLICTING_DERIVED_METADATA'])
        self.assertEqual(36, counts['displayName', 'STALE_OR_CONFLICTING_DERIVED_METADATA'])
        self.assertEqual(38, counts['friendlyName', 'DERIVATION_REVIEW_REQUIRED'])
        self.assertEqual(38, counts['canonicalName', 'DERIVATION_REVIEW_REQUIRED'])
        for kind, expected in (('BusinessObject', 12), ('DataObject', 4)):
            model = self.models['incoming']
            self.assertEqual(expected, sum(c.native_type(model.elements[rid]) == kind for rid in report['unchanged']))

    def test_no_edit_raw_transport_findings_are_still_visible(self):
        baseline, control = self.models['baseline'], self.models['control']
        raw = d.compare(baseline, control, control)
        path = control.path.parent / 'raw-comparator.json.gz'
        historical = json.loads(gzip.decompress(path.read_bytes()))
        self.assertEqual(historical['classifications'], raw['classifications'])
        # JSON arrays represent Python tuple attributes in stored signatures.
        # Compare the exact JSON data without unittest attempting a multi-MB diff.
        self.assertEqual(d.g.digest(historical['findings']), d.g.digest(raw['findings']))


if __name__ == '__main__':
    unittest.main()
