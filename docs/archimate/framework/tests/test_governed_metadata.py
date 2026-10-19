# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Identity immutability and governed naming conformance, including failure paths."""
import copy
import json
from pathlib import Path
import sys
import tempfile
import unittest
import uuid
from unittest import mock

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'tools'))
import governed_metadata as m
import synchronise_baseline as s


class IdentityContractTests(unittest.TestCase):
    def index(self):
        return {'representation-one': dict(record=dict(canonicalId='550e8400e29b41d4a716446655440000',
                                                       displayName='[CT3] Person Identity',
                                                       friendlyName='ClientAdministration.PersonIdentity',
                                                       canonicalName='CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentity'),
                                           expected=dict(displayName='[CT3] Person Identity',
                                                         friendlyName='ClientAdministration.PersonIdentity',
                                                         canonicalName='CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentity'))}

    def test_valid_identity_names_and_representation_separation(self):
        index = self.index()
        result = m.validate_records(index)
        self.assertEqual(1, result['valid_canonicalId'])
        self.assertNotEqual(next(iter(index)), index['representation-one']['record']['canonicalId'])

    def test_missing_and_deleted_id_after_establishment(self):
        index = self.index()
        old = {'representation-one': index['representation-one']['record'].pop('canonicalId')}
        with self.assertRaisesRegex(ValueError, 'Missing/deleted'):
            m.validate_records(index, old)

    def test_malformed_multiple_and_ec_substitution(self):
        for value in ('not-a-uuid', 'EC-01', 'A' * 32, ' ' + 'a' * 32, '550e8400-e29b-41d4-a716-446655440000',
                      ['a' * 32, 'b' * 32], None):
            with self.subTest(value=value):
                index = self.index()
                index['representation-one']['record']['canonicalId'] = value
                with self.assertRaisesRegex(ValueError, 'Malformed/multiple'):
                    m.validate_records(index)

    def test_duplicate_identity(self):
        index = self.index()
        index['another-representation'] = copy.deepcopy(index['representation-one'])
        with self.assertRaisesRegex(ValueError, 'Duplicate Canonical'):
            m.validate_records(index)

    def test_changed_identity_and_physical_resource_deletion(self):
        index = self.index()
        old = {'representation-one': index['representation-one']['record']['canonicalId']}
        index['representation-one']['record']['canonicalId'] = '1234567890ab4def8123456789abcde0'
        with self.assertRaisesRegex(ValueError, 'Changed/deleted established'):
            m.validate_records(index, old)
        with self.assertRaisesRegex(ValueError, 'physically deleted'):
            m.validate_records({}, old)

    def test_duplicate_json_claim_is_not_last_value_wins(self):
        with tempfile.TemporaryDirectory() as temp:
            path = Path(temp) / 'source.json'
            path.write_text('{"canonicalId":"' + 'a' * 32 + '","canonicalId":"' + 'b' * 32 + '"}')
            with self.assertRaisesRegex(ValueError, 'Multiple source claims'):
                m.read(path)

    def test_friendly_canonical_and_display_drift_fail_without_repair(self):
        for role in ('friendlyName', 'canonicalName', 'displayName'):
            index = self.index()
            index['representation-one']['record'][role] = 'DriftedName'
            with self.assertRaisesRegex(ValueError, role + ' drift'):
                m.validate_records(index)
            self.assertEqual('DriftedName', index['representation-one']['record'][role])

    def test_type_encoded_into_identity_is_rejected(self):
        index = self.index()
        index['representation-one']['record']['canonicalId'] = 'Capability-' + 'a' * 32
        with self.assertRaisesRegex(ValueError, 'Malformed/multiple'):
            m.validate_records(index)

    def test_bootstrap_preserves_existing_and_allocates_only_absent(self):
        index = self.index()
        old = index['representation-one']['record']['canonicalId']
        index['new-resource'] = copy.deepcopy(index['representation-one'])
        del index['new-resource']['record']['canonicalId']
        allocate = mock.Mock(return_value=uuid.UUID('12345678-90ab-4def-8123-456789abcde0'))
        self.assertEqual(1, m.allocate_missing(index, allocate))
        allocate.assert_called_once_with()
        self.assertEqual(old, index['representation-one']['record']['canonicalId'])
        self.assertEqual(0, m.allocate_missing(index, mock.Mock(side_effect=AssertionError('Must not allocate'))))

    def test_allocator_refuses_conflicts_before_any_allocation(self):
        index = self.index()
        index['representation-one']['record']['canonicalId'] = 'EC-01'
        factory = mock.Mock()
        with self.assertRaisesRegex(ValueError, 'Malformed/multiple'):
            m.allocate_missing(index, factory)
        factory.assert_not_called()

    def test_pascal_case_components_and_no_qualifiers_from_folders(self):
        self.assertEqual('HealthcareEntityInformationEnablement', m.component('Healthcare Entity Information Enablement'))
        self.assertEqual('CareAccessAndCoordination', m.component('Care Access & Coordination'))
        self.assertEqual('FhirRest', m.component('FHIR REST'))
        self.assertEqual('CarePlace', m.component('Care-Place'))

    def test_digit_leading_label_requires_a_governed_spelling(self):
        with self.assertRaisesRegex(ValueError, 'Cannot establish PascalCase'):
            m.component('24/7 Availability')

    def test_product_uses_governed_vocabulary_and_existing_name_derivation(self):
        record = dict(key='synthetic-product', type='product', name='Referral & Handoff Package')
        self.assertEqual('PRODUCT', m.formal_qualifier(record))
        self.assertEqual(dict(friendlyName='ReferralAndHandoffPackage',
                              canonicalName='PRODUCT-ReferralAndHandoffPackage'),
                         m.qualified_names([record]))
        # Context is supplied explicitly, never discovered by a Product algorithm.
        context = dict(key='synthetic-context', type='capability', tier='CT1',
                       category='business', name='Care Coordination')
        self.assertEqual(dict(friendlyName='CareCoordination.ReferralAndHandoffPackage',
                              canonicalName='CT1-CareCoordination.PRODUCT-ReferralAndHandoffPackage'),
                         m.qualified_names([context, record]))
        self.assertNotIn('canonicalId', record)

    def test_product_name_validation_rejects_drift_and_ungoverned_spelling(self):
        record = dict(key='synthetic-product', type='product', name='24/7 Availability')
        with self.assertRaisesRegex(ValueError, 'Cannot establish PascalCase'):
            m.qualified_names([record])
        record['name'] = 'Referral Package'
        index = self.index()
        expected = dict(displayName=record['name'], **m.qualified_names([record]))
        item = index['representation-one']
        item['expected'] = expected
        item['record'].update(expected)
        m.validate_records(index)
        item['record']['canonicalName'] = 'PRD-ReferralPackage'
        with self.assertRaisesRegex(ValueError, 'canonicalName drift'):
            m.validate_records(index)


    def test_information_vocabulary_reuses_derivation_and_limits_topic_scope(self):
        bo = dict(key='concept', type='BusinessObject', name='Practitioner BO')
        topic = dict(key='logical', type='DataObject', name='Context Topic', information_map=True)
        self.assertEqual(dict(friendlyName='PractitionerBo', canonicalName='BO-PractitionerBo'), m.qualified_names([bo]))
        self.assertEqual(dict(friendlyName='ContextTopic', canonicalName='TOPIC-ContextTopic'), m.qualified_names([topic]))
        del topic['information_map']
        with self.assertRaisesRegex(ValueError, 'TOPIC requires'):
            m.qualified_names([topic])
        with self.assertRaisesRegex(ValueError, 'Missing formal tier'):
            m.qualified_names([dict(key='physical', type='Artifact', name='Practitioner Artefact')])
        self.assertNotIn('Artifact', m.FORMAL)
        self.assertNotIn('ARTEFACT', m.FORMAL.values())

    def test_unadjudicated_digit_leading_name_stops_before_allocation(self):
        b = s.Baseline(*s.load_inputs())
        source, motivation = m.read(m.SOURCE), m.read(m.MOTIVATION)
        source['historical_reference_elements'] = m.historical_records(b)
        record = next(e for e in motivation['elements'] if e['key'] == 'historical:drv_uptime')
        record.pop('nameComponent', None)
        record.pop('nameComponentAuthority', None)
        with mock.patch.object(m, 'allocate_missing', side_effect=AssertionError('Must not allocate')) as allocator:
            with self.assertRaisesRegex(ValueError, '24/7 Availability'):
                m.descriptors(b, source, motivation)
        allocator.assert_not_called()


@unittest.skipUnless(m.active(), 'Source identity establishment has not executed')
class EstablishedSourceTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.outputs, cls.baseline, cls.report = s.publish(reconciled=False)

    def test_current_publication_has_one_source_owned_id_per_element(self):
        root = m.g.ET.fromstring(self.outputs[m.model_path()])
        result = m.validate_publication(self.baseline, root)
        self.assertEqual(len(root.findall('a:elements/a:element', m.g.N)), result['valid_canonicalId'])

    def test_approved_numeric_spelling_is_resource_local(self):
        record = next(e for e in m.read(m.MOTIVATION)['elements'] if e['key'] == 'historical:drv_uptime')
        self.assertEqual('24/7 Availability', record['displayName'])
        self.assertEqual('TwentyFourSevenAvailability', record['friendlyName'])
        self.assertEqual('MOT-TwentyFourSevenAvailability', record['canonicalName'])
        self.assertIn('Solution Architect', record['nameComponentAuthority'])

    def test_deleting_entire_metadata_block_cannot_bypass_publication_gate(self):
        root = m.remove_properties(m.g.ET.fromstring(self.outputs[m.model_path()]))
        with self.assertRaisesRegex(ValueError, 'governed Propert'):
            s.validate(self.baseline, root)

    def test_deleted_source_establishment_cannot_enable_legacy_fallback(self):
        read = m.read
        def missing_header(path):
            value = read(path)
            if Path(path) == m.SOURCE:
                value.pop('governedMetadata', None)
            return value
        with mock.patch.object(m, 'read', side_effect=missing_header):
            with self.assertRaisesRegex(ValueError, 'Deleted identity establishment'):
                m.active()

    def test_bootstrap_rerun_refuses_before_allocation(self):
        with mock.patch.object(m, 'allocate_missing', side_effect=AssertionError('Reallocation')):
            with self.assertRaisesRegex(ValueError, 'already exists'):
                m.bootstrap(m.read(m.SOURCE)['governedMetadata']['establishedRelease'])

    def test_ordinary_publication_never_allocates(self):
        with mock.patch.object(m, 'allocate_missing', side_effect=AssertionError('Reallocation')):
            outputs, _, _ = s.publish(reconciled=False)
        self.assertEqual(self.outputs, outputs)

    def test_subsequent_release_transports_existing_ids_and_names_without_allocation(self):
        from architecture_release import load_release, version_parts
        major, minor, patch = version_parts(load_release()['version'])
        later = dict(version=f'{major}.{minor}.{patch + 1}', previous_version=load_release()['version'])
        with mock.patch('architecture_release.load_release', return_value=later), mock.patch.object(m, 'allocate_missing', side_effect=AssertionError('Reallocation')):
            outputs, _, _ = s.publish(reconciled=False)
            root = m.g.ET.fromstring(outputs[m.model_path()])
        before = m.g.ET.fromstring(self.outputs[m.model_path()])
        for old, new in zip(before.findall('a:elements/a:element', m.g.N), root.findall('a:elements/a:element', m.g.N)):
            self.assertEqual(old.get('identifier'), new.get('identifier'))
            self.assertEqual(s.prop_values(old), s.prop_values(new))

    def test_multiple_publication_claim_and_missing_property_rejected(self):
        for change in ('duplicate', 'delete'):
            root = m.g.ET.fromstring(self.outputs[m.model_path()])
            props = root.find('a:elements/a:element/a:properties', m.g.N)
            claim = props.find("a:property[@propertyDefinitionRef='governed-canonical-id']", m.g.N)
            if change == 'duplicate':
                props.append(copy.deepcopy(claim))
            else:
                props.remove(claim)
            with self.assertRaisesRegex(ValueError, 'governed Properties'):
                m.validate_publication(self.baseline, root)

    def test_whole_architecture_and_presentation_survive_metadata_overlay(self):
        root = m.g.ET.fromstring(self.outputs[m.model_path()])
        self.assertEqual(m.g.signature(self.baseline.root), m.g.signature(m.remove_properties(root)))


if __name__ == '__main__':
    unittest.main()
