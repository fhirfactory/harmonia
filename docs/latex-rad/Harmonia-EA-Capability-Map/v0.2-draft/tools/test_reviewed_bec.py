#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Semantic preservation and negative regression tests for the reviewed decisions."""
import copy
import json
import unittest
from pathlib import Path

from check import validate_model, validate_review

BASE=Path(__file__).resolve().parents[1]


class ReviewedBecTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.model=json.loads((BASE/'model.json').read_text())

    def setUp(self):
        self.m=copy.deepcopy(self.model)
        self.e={x['key']:x for x in self.m['elements']}
        self.names={x['reviewed_name']:x for x in self.e.values() if x.get('reviewed_name')}
        self.refs={x['reference_id']:x for x in self.e.values()}

    def test_complete_model_and_semantic_preservation(self):
        validate_model(self.m)

    def test_reviewed_seven_roots_and_business_boundary(self):
        roots={x['reviewed_name'] for x in self.e.values() if x['category']=='business_enabling' and x['record_state']=='current' and x['tier']=='CT1'}
        self.assertEqual({'Entity Information Enablement','Care Administration Enablement','Clinical Service Dependency Management','Health Service Operations Enablement','Shared Information & Coordination Enablement','Health Informatics Management','Independent Assurance Enablement'},roots)
        self.assertEqual(228,self.m['reviewed_bec_reconciliation']['bc_ec_preservation']['elements'])

    def test_organisation_consolidation_preserves_each_predecessor(self):
        x=self.names['Healthcare Organisation Profile Administration']
        predecessors=[self.e[k]['pre_review_record']['name'] for k in x['predecessor_keys']]
        self.assertEqual(['Healthcare Organisation','Organisational Structure','Organisation Contact'],predecessors)
        for ref in ('FEAT-EM-17','FEAT-EM-18','FEAT-EM-19','FEAT-EM-20'):
            self.assertEqual(x['key'],self.refs[ref]['owner_key'])

    def test_device_association_distributes_semantics(self):
        d=next(d for d in self.m['reviewed_bec_reconciliation']['decisions'] if d['before_name']=='Device Association')
        self.assertEqual('decomposed',d['classification'])
        self.assertEqual({'Device Utilisation Administration','Device OA&M Metadata Management'},set(d['semantic_distribution']))
        self.assertEqual(self.names['Device Utilisation Administration']['key'],self.refs['FEAT-EM-30']['owner_key'])
        self.assertEqual(self.names['Device OA&M Metadata Management']['key'],self.refs['FEAT-EM-31']['owner_key'])
        self.assertEqual(self.names['Device Utilisation Administration']['key'],self.refs['FEAT-HSO-14']['owner_key'])

    def test_service_contexts_relocated_with_stable_identities_and_labels(self):
        mappings={'HCM-BEC-emergency-care':('Emergency Care Progression','Emergency Department Operations'),
                  'HCM-BEC-acute-care':('Acute Care Monitoring & Event Coordination','Episode & Encounter Administration'),
                  'HCM-BEC-inpatient-care':('Multidisciplinary Inpatient Care Coordination','Ward Operations')}
        for rid,(name,parent) in mappings.items():
            x=self.refs[rid]
            self.assertEqual(name,x['name'])
            self.assertEqual(self.names[parent]['key'],x['parent_key'])
            self.assertEqual(x['pre_review_record']['latex_label'],x['latex_label'])
            self.assertIn(x['pre_review_record']['canonical_id'],x['id_aliases'])

    def test_rejects_moving_care_progression_into_dependency_context(self):
        x=self.refs['HCM-BEC-emergency-care']
        x['parent_key']=self.names['Emergency Care']['key']
        with self.assertRaises(AssertionError):validate_review(self.m,self.e)

    def test_rejects_lost_scope_annotation_without_demoting_canonical(self):
        x=self.names['Medication Administration']
        self.assertEqual('Canonical',x['architectural_status'])
        x.pop('release_scope')
        with self.assertRaises(AssertionError):validate_review(self.m,self.e)

    def test_empty_scoped_and_informatics_branches_stay_empty(self):
        for name in ('Funding & Coverage Administration','Clinical Charging & Claims Administration','Service Establishment Support','Clinical Training & Education','Research Information Curation','Population Health Information Reporting','Service Planning Data Management'):
            self.assertFalse(any(x['record_state']=='current' and x.get('parent_key')==self.names[name]['key'] for x in self.e.values()),name)
        self.assertIn('adjacent',self.names['Health Informatics Management']['definition'])

    def test_rejects_artificial_scoped_decomposition(self):
        self.names['Vendor Liaison']['parent_key']=self.names['Funding & Coverage Administration']['key']
        with self.assertRaises(AssertionError):validate_review(self.m,self.e)

    def test_consent_owner_preserved_and_consumers_reference_it(self):
        owner=self.names['Client Privacy Guardrail Management']['key']
        self.assertEqual(owner,self.refs['FEAT-EM-09']['owner_key'])
        for name in ('Health Information Control','Patient Health Information Restriction Management','Patient Health Information Filter Management','Patient Health Information Disclosure Management'):
            self.assertEqual(owner,self.names[name]['respects_consent_responsibility'])
        self.refs['FEAT-EM-09']['owner_key']=self.names['Patient Health Information Restriction Management']['key']
        with self.assertRaises(AssertionError):validate_review(self.m,self.e)

    def test_internal_execution_and_external_work_not_equated(self):
        old=self.refs['HCM-BEC-workflow-activity-coordination']
        new=self.names['Workflow & Activity Coordination']
        self.assertNotEqual(old['key'],new['key'])
        self.assertEqual('pending_adjudication',old['record_state'])
        self.assertEqual(old['key'],self.refs['FEAT-ISE-22']['owner_key'])
        self.assertIn('execution boundary',new['definition'])
        self.assertEqual({'External Task Coordination','External Task Status & Traceability Management','Activity Routing Management','Work Boundary Governance'},
            {x['name'] for x in self.e.values() if x['record_state']=='current' and x.get('parent_key')==new['key']})

    def test_subject_assurance_not_hidden_under_service_review(self):
        old=self.refs['HCM-BEC-governed-assurance']
        self.assertEqual('Canonical',old['architectural_status'])
        self.assertEqual('pending_adjudication',old['record_state'])
        self.assertNotEqual(self.names['Assurance Service Review']['key'],old['parent_key'])
        self.assertIn('service itself',self.names['Assurance Service Evaluation']['definition'])
        old['record_state']='current'
        with self.assertRaises(AssertionError):validate_review(self.m,self.e)

    def test_rejects_in_scope_feature_directly_under_ct2(self):
        self.refs['FEAT-SA-03']['owner_key']=self.names['Referral Administration']['key']
        with self.assertRaisesRegex(AssertionError,'Feature lacks BEC CT3'):validate_model(self.m)

    def test_rejects_alias_loss_duplicate_reference_and_ct4(self):
        x=self.refs['HCM-BEC-emergency-care']
        original=copy.deepcopy(x)
        x['id_aliases']=[]
        with self.assertRaises(AssertionError):validate_model(self.m)
        x.update(original)
        x['reference_id']='FEAT-SA-03'
        with self.assertRaisesRegex(AssertionError,'Duplicate reference_id'):validate_model(self.m)
        x.update(original)
        x['tier']='CT4'
        with self.assertRaises(AssertionError):validate_model(self.m)

    def test_no_supply_movement_or_real_world_authority_inferred(self):
        self.assertIn('Supply/materiel movement belongs',self.names['Clinical Logistics Coordination']['boundary'])
        self.assertIn('does not make Harmonia',self.names['Health Materiel Traceability Administration']['boundary'])
        self.assertIn('responsible clinical participants perform supervision',self.names['Clinical Escalation Path Management']['boundary'])
        self.assertIn('not dynamic operational capacity',self.names['Healthcare Service Profile Administration']['boundary'])

    def test_mixed_canonical_specimen_materiel_semantics_are_preserved_unresolved(self):
        for rid in ('HCM-FN-manage-specimen-transport-progression','HCM-PR-specimen-transport-process'):
            x=self.refs[rid]
            self.assertEqual('Canonical',x['architectural_status'])
            self.assertEqual('pending_adjudication',x['record_state'])
            self.assertEqual(x['pre_review_record']['governed_definition_latex'],x['governed_definition_latex'])
            self.assertIn('HUMAN ADJUDICATION REQUIRED',x['adjudication_note'])
            x['record_state']='current'
            with self.assertRaises(AssertionError):validate_review(self.m,self.e)
            x['record_state']='pending_adjudication'

    def test_bec_exchange_homonym_is_explicit_without_bc_redesign(self):
        x=self.names['Health Information Exchange']
        self.assertEqual('Health Information Exchange Enablement',x['name'])
        self.assertIn('HUMAN ADJUDICATION REQUIRED',x['naming_rationale'])
        bc=[e for e in self.e.values() if e['category']=='business' and e['name']=='Health Information Exchange']
        self.assertEqual(1,len(bc))
        self.assertNotEqual(bc[0]['reference_id'],x['reference_id'])


if __name__=='__main__':unittest.main()
