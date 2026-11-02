#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Materialise review recommendations, never canonical architecture.

These source-family/action selections are analyst proposals based on the supplied
source definitions and canonical responsibility boundaries. No lexical identity
resolution, relationship allocation, capability creation or release assignment.
A Feature fit concerns the stated action; it does not satisfy every source rule.
"""
import collections
import csv
import json
from pathlib import Path
import re
import sys
ROOT=Path.cwd()
sys.path.insert(0,str(ROOT/'enterprise-architecture/tooling'))
import architecture_continuum as c
OUT=ROOT/'enterprise-architecture/design/evidence/1.1.18/001/after/reconciliation'
_,elements=c.load()
sources=json.loads((OUT/'source-records.json').read_text())
rows=json.loads((OUT/'candidates.json').read_text())

# Aliases identify existing records explicitly. They are local script variables,
# not architectural identifiers or a proposed modelling vocabulary.
ids={
 'profile':'d23be319c6114c28bcb9e24e1258a0fc',
 'nationalPractitioner':'12c5a17dcc564619a6a9222dd2e15528',
 'nationalOrganisation':'0f17fe5439fe45dc8f4edbff3cca0776',
 'roles':'ef859d4934514f4eb601c6abf9708760',
 'affiliation':'9501e88ea4b7474a8aa63191687db51e',
 'locationService':'37188bb916244f41b1b7d988ad52a2eb',
 'schedule':'4753953665d343c5a97bc4965be3f615',
 'endpoint':'5dc3a424e68d47aea64ec573dc4b1225',
 'discovery':'cd3bd35f7f9e4852bbbce299bb795d8a',
 'identityCorrection':'f7ab7a24e8234129b2ab02e9abd4fba2',
 'identityLineage':'d72134e75001449a9e7ffae53f61458f',
 'identifiers':'6d74d143c7454fa0bf30743ddf7ba094',
 'alias':'97721dbd017e4c2daa73e1e87a8499b9',
 'demographic':'3d0377451a1b44d0bb41404207cdd867',
 'demographicIngest':'594ccbd95f5841e4b874fd1bade34a5d',
 'relationship':'05de74a8a7dc4334ba60543a228e73cb',
 'relationshipValidity':'309603a8330545abb5db5f9889aa60d9',
 'consentEvaluation':'baf6dc6ef38a4256a9a67a65fd3f31ba',
 'confidentiality':'1c18e7c2ccf640c9afc2525503b505a6',
 'filtering':'a9f1d2100faa44beb69add62bb4f4b3f',
 'accessConsent':'e2643fa59303401abb1b778f3295e536',
 'conceptResolution':'30cdb9b2a810467c9b56e9870de4367e',
 'terminologyMap':'6134dc91af8d45e8b81cc28d18a5251f',
 'terminologyBinding':'b999df692ca64010b4adeedbcb0d3a5a',
 'datasetRevision':'89123c868b8a46c38800916110cd918d',
 'schema':'3317bbb7e2db4384abf44a50bf67422c',
 'syndication':'30bb720ed4e64a47a1fcf3be811c1931',
 'conformance':'d170a68efcae4aa59350f565ca69443a',
 'profileCt3':'40759a835f7b456e9257c38b67b0a380',
 'qualificationCt3':'8fd5c78eba3046fa9153fb4efb44e9c8',
 'engagementCt3':'36d5bf4c19d34172b72f4b77099ff235',
 'personCt3':'6c3de7750ef1436cad6a9533731782b6',
 'assuranceCt3':'56ccd6d3bf5b4ec980a333dfadf8d9fa',
 'clientRelationshipCt3':'d682c11d3eb04770b1f20be478fd475e',
 'privacyCt3':'7577a6e1815243648dd5356c21cca151',
 'auditCt3':'c8d93ddc5aa84960964b78345fff1b29',
 'accountabilityCt3':'a33d0cc14c384bd48012def4ded54a67',
 'terminologyCt3':'e926a47881fa482088859f59fa6fe7e4',
 'referenceStewardshipCt3':'971d4b079ad24e89b70a8ed8dec0c8bf',
 'datasetCt3':'682b8684ad9e49d5a5c348a148081607',
 'organisationCt3':'f2de9e9394b849f2943355c7a2882a15',
}
assert all(v in elements for v in ids.values())
assert all(elements[v][0]['standing']['currentness']=='current' for v in ids.values())
decisions={}

def add(family, numbers, targets, reason, subject=None, gap=None):
    for number in numbers:
        key=f'{family}-{number}'
        decisions[key]=dict(recommendationIds=[ids[k] for k in targets], rationale=reason, recommendedSubject=subject, gap=gap)

def full(family, count, targets, reason, **kw):
    add(family,range(1,count+1),targets,reason,**kw)

# Consent: directive capture, evaluation, representation and evidence differ.
consent_reason='Client Privacy Guardrail Management explicitly manages client directives; Patient Health Information Restriction Management explicitly references those determinations and is not a second consent owner.'
for fam,n in [('UC-Clinician-Consent',20),('UC-ExternalClinician-Consent',25),('UC-Participant-Consent',20)]:
    full(fam,n,['privacyCt3'],consent_reason,gap='No specific existing Feature established here for the full capture/renewal/withdrawal/history workflow; evaluation alone is insufficient.')
for fam,nums in [('UC-Clinician-Consent',[5,14,17]),('UC-ExternalClinician-Consent',[1,2,4,10,16,20]),('UC-Participant-Consent',[19])]:
    add(fam,nums,['consentEvaluation','accessConsent'],'Evaluate directives against a requested information use; policy, actor authority and disclosure remain separate considerations.',subject='G16.03')
for fam,nums in [('UC-Clinician-Consent',[2,3,4,13,16,19]),('UC-ExternalClinician-Consent',[5,6,7,8]),('UC-Participant-Consent',[2,3,17])]:
    add(fam,nums,['privacyCt3'],consent_reason,subject='G16.01',gap='Feature gap for consent capture/renewal/withdrawal; do not use Consent Directive Evaluation as capture responsibility.')
for fam,nums in [('UC-Clinician-Consent',[6,7,8,18]),('UC-ExternalClinician-Consent',[13,14]),('UC-Participant-Consent',[10])]:
    add(fam,nums,['relationshipValidity','relationship','privacyCt3'],'Representative association and effective authority scope must be distinguished from consent and legal capacity decisions.',subject='G16.04',gap='Clinical capacity/legal delegation decision is not established by a relationship record.')
for fam,nums in [('UC-Clinician-Consent',[15]),('UC-ExternalClinician-Consent',[17,18])]:
    add(fam,nums,['privacyCt3','auditCt3'],'Emergency disclosure requires explicitly governed authority and activity evidence; ordinary consent evaluation supplies no emergency override.',subject='G16.06',gap='Emergency exception/override Feature and its policy are unestablished.')
for fam,nums in [('UC-ExternalClinician-Consent',[22,25]),('UC-Participant-Consent',[20])]:
    add(fam,nums,['auditCt3','accountabilityCt3'],'Capturing activity evidence and demonstrating appropriate use are distinct responsibilities; sharing-programme management does not establish either.',subject='G04.04')
add('UC-ExternalClinician-Consent',[19,21],['engagementCt3','privacyCt3'],'Care-team participation and access consequences span participation and consent; a consent-history subject alone is inadequate.',gap='Participant care-team scope needs adjudication; practitioner engagement may supply only a partial frame.')
add('UC-ExternalClinician-Consent',[24],['organisationCt3','privacyCt3'],'Organisation identity/profile facts do not establish organisation authority to use a participant record.',gap='Organisation authority decision and evidence remain unestablished.')
add('UC-ExternalClinician-Consent',[9],['consentEvaluation','accessConsent'],'Sharing requires consent evaluation plus authorised exchange; evaluating consent does not itself deliver information.',gap='Exchange/delivery responsibility requires separate alignment.')
add('UC-Integration-Identity',[24,25],['accessConsent'],'Identity access requires established actor authority and applicable information-use policy.',subject='G03.02')
add('UC-Integration-Identity',[26],['filtering','confidentiality'],'Masking or suppressing restricted identity information is an information-disclosure control.',subject='G03.04')
add('UC-Integration-Identity',[27],['auditCt3'],'Exchange audit is significant activity evidence rather than consent-scope administration.',subject='G04.01')

# Patient identity: correlation consumes source identity decisions; no master MPI
# authority or R1/R2 scope expansion follows from this target-state catalogue.
full('UC-DepartmentalOfficer-Identity',20,['assuranceCt3'],'Identity evidence/confidence requires Identity Assurance rather than contextual access authority.',subject='G14.01',gap='No bounded Feature established for the complete identity evidence review workflow.')
add('UC-DepartmentalOfficer-Identity',[4,5,7,14],['identityCorrection','identityLineage'],'Existing correction Feature applies externally decided corrections; autonomous identity reconciliation is not established.',subject='G14.02',gap='Determine who decides corrections/merges; preserve upstream authority and R1/R2 exclusions.')
add('UC-DepartmentalOfficer-Identity',[6,19],['identifiers'],'Cross-authority identifier correlation is more specific than health-information correlation.',subject='G14.02',gap='Probabilistic duplicate detection is not established by identifier correlation.')
add('UC-DepartmentalOfficer-Identity',[9,10,11],['personCt3'],'Represent person identity status while keeping source authority; status is not assurance quality.',subject='G14.03',gap='Suspension/reactivation decision authority and Feature scope require review.')
add('UC-DepartmentalOfficer-Identity',[12,20],['identityLineage','auditCt3'],'Identity assertion history and activity audit have different meanings.',subject='G14.06')
add('UC-DepartmentalOfficer-Identity',[13],['accessConsent'],'Approving information access is not establishing person identity confidence.',subject='G14.05')
add('UC-DepartmentalOfficer-Identity',[8,17],['assuranceCt3'],'Identity exception/risk may affect confidence but has no fully established Feature coverage.',subject='G14.06',gap='Risk scoring and exception adjudication are not established by the general assurance definition.')
add('UC-DepartmentalOfficer-Identity',[18],['personCt3'],'Identity lookup is a person-information action; assurance does not define search.',subject='G14.05',gap='Person identity discovery Feature/criteria require adjudication.')
full('UC-Integration-Identity',30,['personCt3'],'Integrating source-qualified identity facts differs from establishing an identity claim.',gap='Integration transport and error handling need separate coverage; no identity-model governance implication.')
add('UC-Integration-Identity',[1,10,12],['demographicIngest'],'Receive and validate external PAS registration/demographic updates; do not originate master identity authority.',subject='G14.04')
add('UC-Integration-Identity',[2,9],['personCt3'],'Submitting/receiving source identity facts does not authorise a Harmonia golden-record authority.',subject='G14.01',gap='Golden identity source and decision authority need explicit adjudication.')
add('UC-Integration-Identity',[5,6,7,18,19],['identifiers'],'Correlate person identifiers across issuing namespaces; health-information correlation explicitly excludes establishing person identity.',subject='G14.02',gap='Matching confidence, duplicate adjudication and service interface scope remain unresolved.')
add('UC-Integration-Identity',[8,14,15],['identityCorrection','identityLineage'],'Consume source-attributed reconciliation/split/merge decisions while preserving their history.',subject='G14.02',gap='Reconciliation request does not establish local decision authority.')
add('UC-Integration-Identity',[11],['syndication','identityLineage'],'Distribute qualified identity change events; event delivery does not confer identity authority.',subject='G14.03')
add('UC-Integration-Identity',[13],['personCt3'],'Synchronise externally established identity status.',subject='G14.03',gap='Dedicated identity-status synchronisation Feature is unestablished.')
add('UC-Integration-Identity',[16,17],['personCt3'],'Retrieve person identity details under access control.',subject='G14.05',gap='Identity discovery/retrieval Feature scope requires adjudication.')
add('UC-Integration-Identity',[20,21,22,23,3,4],['assuranceCt3'],'Convey or evaluate source-qualified identity evidence/confidence; structural message validation alone cannot establish identity.',subject='G14.01',gap='Separate semantic conformance from identity assurance and preserve confidence provenance.')
# Restore explicit security/evidence proposals overwritten by the family default.
add('UC-Integration-Identity',[24,25],['accessConsent'],'Identity access requires established actor authority and applicable information-use policy.',subject='G03.02')
add('UC-Integration-Identity',[26],['filtering','confidentiality'],'Restrict disclosure at query/presentation boundaries.',subject='G03.04')
add('UC-Integration-Identity',[27],['auditCt3'],'Audit identity data exchange under the governed audit boundary.',subject='G04.01')
add('UC-Integration-Identity',[28,29,30],['personCt3'],'Interface monitoring and failure reconciliation have operational concerns beyond business identity administration.',subject='G09.01',gap='Operational availability/error/recovery capability coverage requires separate review; no appropriate identity Feature is established.')
full('UC-Participant-Identity',20,['personCt3'],'Source-qualified identity maintenance is distinct from identity assurance.',gap='Self-service data entry must retain verification and source-authority boundaries.')
add('UC-Participant-Identity',[1,2,3,4,8,9,11,15],['assuranceCt3'],'Identity evidence and confidence are more relevant than general authorisation or identity-model governance.',subject='G14.01',gap='Evidence capture/verification-status user workflow lacks a specific established Feature.')
add('UC-Participant-Identity',[5,12],['identityCorrection','identityLineage'],'Correction requests do not establish a locally authorised correction decision.',subject='G14.02',gap='External correction authority and response workflow remain to be established.')
add('UC-Participant-Identity',[6,7,17],['demographic'],'Maintain demographic context with provenance; no assurance claim follows.',subject='G14.04')
add('UC-Participant-Identity',[13],['identifiers'],'Link a healthcare identifier within its issuing context.',subject='G14.02')
add('UC-Participant-Identity',[14,20],['identityLineage'],'Retain attributable identity change history; notification delivery requires separate coverage.',subject='G14.03')
add('UC-Participant-Identity',[16],['alias','demographic'],'A preferred name is source-qualified demographic/alias information, not a replacement identity.',subject='G14.04')
add('UC-Participant-Identity',[18],['confidentiality','accessConsent'],'Protecting identity information is a disclosure/security concern.',subject='G14.05')
add('UC-Participant-Identity',[19],['accountabilityCt3','auditCt3'],'Suspected identity misuse requires evidence and assessment, not confidence inferred from a successful operation.',subject='G14.06',gap='Misuse investigation and response workflow is not fully established.')
full('UC-Participant-Demographics',29,['demographic'],'Demographic context governance is a narrower semantic fit than general identity qualification.',subject='G14.04')
add('UC-Participant-Demographics',[12,13],['relationship'],'Nominated emergency contacts/carers are relationships rather than demographic facts.',gap='No general client-relationship subgroup exists in the supplied G14 proposal; G16.04 covers delegated consent only.')
add('UC-Participant-Demographics',[7],['demographic'],'A recorded language/accessibility need does not establish interpreter booking/delivery responsibility.',subject='G14.04',gap='Interpreter-service request/fulfilment capability coverage is not established.')
add('UC-Participant-Demographics',[17,18],['identityLineage'],'Change history and confirmation need attributed state/evidence and notification scope.',subject='G14.04',gap='Identity lineage covers only a qualified part of demographic history; notification workflow remains unestablished.')
for fam in ('UC-Participant-Relationship','UC-RelatedPerson-Relationship'):
    full(fam,20,['relationship','relationshipValidity'],'Client Relationship Administration and its nominated-association/effective-scope Features fit relationship information; neither Identity Assurance nor Identity Modelling establishes these responsibilities.',gap='General client relationships need a Subject decision; do not equate all relationships with delegated consent or infer legal authority.')
add('UC-RelatedPerson-Relationship',[11,12],['demographic'],'The related person has contact/preferences information of their own; it is not a provider directory record.',subject='G14.04')
add('UC-RelatedPerson-Relationship',[20],['relationshipValidity','accessConsent'],'A valid relationship alone does not authorise access to the linked participant information.',subject='G16.04')

# Practitioner information is not organisation information. Qualification facts,
# roles, affiliations, endpoints and publication retain independent boundaries.
full('UC-Clinician-Directory',30,['profile'],'Practitioner profile information belongs to Practitioner Profile Governance, not Healthcare Organisation Profile Administration.',subject='G15.04')
add('UC-Clinician-Directory',[5,8],['profile','qualificationCt3'],'Supplied qualifications/certificates retain issuing authority; the record does not grant practice privilege.',subject='G15.02')
add('UC-Clinician-Directory',[6],['nationalPractitioner'],'Professional registration needs recognised practitioner-source verification.',subject='G15.02')
add('UC-Clinician-Directory',[7],['profile'],'Professional membership is supplied practitioner profile information; it is not identity assurance.',subject='G15.02')
add('UC-Clinician-Directory',[9,10,13,27],['roles'],'Represent specialties, clinical roles and acting capacities without treating them as a grant of privilege.',subject='G15.03')
add('UC-Clinician-Directory',[12],['affiliation'],'Maintain temporal practitioner-organisation affiliation; the old Provider Relationship target is superseded.',subject='G15.03')
add('UC-Clinician-Directory',[14,26],['locationService'],'Practice locations associate practitioner, service and place; Profile Administration is not a place/role decision.',subject='G15.03')
add('UC-Clinician-Directory',[15],['schedule'],'Represent operating availability; do not infer current dispatch availability from a published schedule.',subject='G15.03')
add('UC-Clinician-Directory',[18],['endpoint'],'Associate validated communication endpoints with practitioner roles.',subject='G15.03')
add('UC-Clinician-Directory',[17,29],['profile'],'Referral acceptance/intake requirements are wider than core practitioner demographics.',subject='G15.04',gap='Service eligibility/referral criteria require their service capability context; profile Feature is partial.')
add('UC-Clinician-Directory',[19,20,21],['profile','discovery'],'Publishing/hiding profiles concerns directory visibility; discovery alone does not define publication policy.',subject='G18.02',gap='Practitioner publication/visibility policy Feature not established by profile or discovery definitions.')
add('UC-Clinician-Directory',[22,23,24],['profile'],'Profile verification/approval is not organisational or person-identity qualification by default.',subject='G18.04',gap='Approval/rejection/verification workflow needs a bounded Feature/policy decision.')
add('UC-Clinician-Directory',[25],['auditCt3','profile'],'Capture/retrieve profile-change evidence without assuming audit capture equals history presentation.',subject='G04.04')
add('UC-Clinician-Directory',[30],['profile','syndication'],'A profile fact change and its notification delivery are distinct.',subject='G15.04')
full('UC-Clinician-Identity',4,['profile','nationalPractitioner'],'Register practitioner identity and supplied identifiers with source authority; general person assurance alone does not cover professional registration.',subject='G15.01')
full('UC-Clinician-ParticipantIdentity',2,['personCt3'],'Lookup participant identities under their own identity namespace and access controls.',subject='G14.05',gap='Do not allocate participant search to Practitioner Profile Administration; specific discovery Feature remains unresolved.')
full('UC-Clinician-ProviderDirectorySearch',18,['discovery'],'Qualified practitioner discovery matches the search action; discovery explicitly does not decide privilege or availability.',subject='G18.01')
add('UC-Clinician-ProviderDirectorySearch',[16],['discovery','schedule'],'Published/provider availability data needs schedule or current-availability context beyond discovery.',subject='G18.01',gap='Current service availability and source freshness require review.')
add('UC-Clinician-ProviderDirectorySearch',[17],['discovery'],'Finding referral requirements is not deciding service eligibility.',subject='G18.01',gap='Service referral criteria responsibility requires separate alignment.')
full('UC-DepartmentalOfficer-ProviderDirectory',30,['profile'],'Practitioner directory/profile administration is not healthcare-organisation profile administration.',subject='G18.03')
add('UC-DepartmentalOfficer-ProviderDirectory',[1,27],['profile'],'Register practitioner profile/identifiers while retaining issuing-source context.',subject='G15.01')
add('UC-DepartmentalOfficer-ProviderDirectory',[2,3],['nationalPractitioner'],'Verify recognised practitioner identifiers and professional registration status.',subject='G15.02')
add('UC-DepartmentalOfficer-ProviderDirectory',[4,13,14],['profile','qualificationCt3'],'Qualification tracking retains certificates, dates and issuing authority; contextual Identity Qualification is not clinical qualification.',subject='G15.02')
add('UC-DepartmentalOfficer-ProviderDirectory',[9],['affiliation'],'Temporal practitioner affiliation is specifically covered; preserve superseded Provider Relationship history.',subject='G15.03')
add('UC-DepartmentalOfficer-ProviderDirectory',[10,28],['roles'],'Practice roles and their history remain distinct from practitioner identity and clinical privilege.',subject='G15.03')
add('UC-DepartmentalOfficer-ProviderDirectory',[11],['locationService'],'Associate provider/service/place rather than administer organisation identity.',subject='G15.03')
add('UC-DepartmentalOfficer-ProviderDirectory',[12],['schedule'],'Maintain service operating availability with temporal context.',subject='G15.03')
add('UC-DepartmentalOfficer-ProviderDirectory',[17,18],['endpoint'],'Maintain validated role-specific endpoints; do not treat identity confidence as endpoint reachability.',subject='G15.03',gap='Reachability/verification contract beyond endpoint binding requires review.')
add('UC-DepartmentalOfficer-ProviderDirectory',[26],['nationalOrganisation'],'Verify an external organisation identifier against its recognised source; this is a genuine organisation responsibility.',subject='G18.04')
add('UC-DepartmentalOfficer-ProviderDirectory',[21,25,30],['auditCt3','accountabilityCt3'],'Audit evidence and usage/integrity assessment are distinct from contextual identity qualification.',subject='G04.04',gap='Directory report/query presentation and quality metrics require additional coverage.')
add('UC-DepartmentalOfficer-ProviderDirectory',[5,6,7,22,23,29],['profile'],'Profile changes, duplicates and verification policy require domain-specific rules, not general identity qualification.',subject='G18.04',gap='Quality/approval/duplicate-resolution policy and Feature scope are unestablished.')
add('UC-DepartmentalOfficer-ProviderDirectory',[15,16,19,20,24],['profile'],'Listing lifecycle/publication is not clinical suspension, legal deregistration or privilege withdrawal.',subject='G18.02',gap='Directory visibility/lifecycle Feature and source-authority distinction require adjudication.')

# Terminology and datasets: build-time meaning and mutable runtime content differ.
for fam,n in [('UC-DepartmentalOfficer-Terminology',10),('UC-DepartmentalOfficer-CodeSet',5),('UC-DepartmentalOfficer-Classification',4)]:
    full(fam,n,['terminologyCt3'],'Terminology lifecycle/standing is governed independently of runtime dataset storage.',subject='G17.01',gap='Dedicated terminology authoring/release/version comparison Feature is not established.')
add('UC-DepartmentalOfficer-Classification',[4],['terminologyMap'],'Translate through governed concept correspondence; related concepts are not identical.',subject='G17.02')
add('UC-DepartmentalOfficer-CodeSet',[4],['terminologyMap'],'Mapping is more specific than broad terminology governance.',subject='G17.02')
add('UC-DepartmentalOfficer-Terminology',[9],['terminologyMap'],'Map validity and map authority require explicit qualification; translation alone is partial coverage.',subject='G17.02',gap='Map validation/assurance workflow is not established.')
add('UC-DepartmentalOfficer-Terminology',[3],['conceptResolution','terminologyCt3'],'Concept reference validity differs from accepting externally supplied terminology content.',subject='G17.02',gap='Content quality/acceptance workflow remains broader than resolution.')
add('UC-DepartmentalOfficer-Terminology',[2,4,5,8,10],['terminologyCt3'],'Import/version/release/retirement are lifecycle and standing decisions.',subject='G17.03',gap='No specific governed Feature selected for the entire lifecycle workflow.')
full('UC-DepartmentalOfficer-ReferenceData',18,['datasetCt3'],'Mutable approved runtime reference content belongs to Governed Reference Dataset Administration, preserving Calliope Models/DataSets and durable-state boundaries.',subject='G17.04',gap='Distribution/query/availability and quality metrics need separate coverage; stewardship is not runtime content administration.')
add('UC-DepartmentalOfficer-ReferenceData',[2,4,5],['schema','referenceStewardshipCt3'],'Define dataset structure/categories/relationships as governed meaning separately from mutable content.',subject='G17.04')
add('UC-DepartmentalOfficer-ReferenceData',[3,6,7,8,9],['datasetRevision'],'Authorised dataset content revisions fit the existing content-revision Feature; approval rules/authority remain explicit.',subject='G17.04')
add('UC-DepartmentalOfficer-ReferenceData',[10],['referenceStewardshipCt3'],'Reference-data ownership is stewardship, not directory verification.',subject='G17.05')
add('UC-DepartmentalOfficer-ReferenceData',[13],['syndication','datasetCt3'],'Notify consumers of a governed reference-content change; do not classify every notification as terminology definition.',subject='G17.03')
add('UC-DepartmentalOfficer-ReferenceData',[15,16],['referenceStewardshipCt3'],'Quality and duplicate review are governed stewardship decisions.',subject='G17.05',gap='Specific data-quality assessment Feature/criteria not established.')
add('UC-DepartmentalOfficer-ReferenceData',[17,18],['auditCt3','referenceStewardshipCt3'],'Audit history and reference usage reporting differ from approving a content revision.',subject='G17.05',gap='Specific report/usage assessment Feature not established.')

# Legacy PD requirements are kept as distinct UC and SR source contributions.
full('SR',58,['profileCt3'],'Provider-data exchange and directory administration span practitioner/organisation information; Healthcare Organisation Profile Administration alone cannot cover this source.',subject='G18.03',gap='Resolve practitioner versus organisation object, source authority and integration/quality concerns before selecting a single Feature.')
for nums in ([1,7,20,33],[32,34,35,36,37,40,41,42,43]):
    add('SR',nums,['profileCt3'],'Provider ingestion/export/API behaviour needs an explicit object and exchange boundary; organisation-profile scope alone is insufficient.',subject='G18.05',gap='Specific provider feed/API/manual-exchange Feature and responsibility remain unresolved; no new Capability proposed.')
add('SR',[2,8,21,49,50,51],['profileCt3'],'Provider-data quality, duplicates and verification are not the contextual adequacy of a human identity.',subject='G18.04',gap='Provider integrity/duplicate-handling Feature and criteria require adjudication.')
add('SR',[3,4,9,22,23],['profile'],'Supplied practitioner identifiers belong to source-qualified profiles, not general organisation identity.',subject='G15.01',gap='Where records also represent organisations, separate their identifier/source contributions.')
add('SR',[13,31,39],['auditCt3','profileCt3'],'Attribute/source lineage, verification evidence and export audit need distinct evidence semantics.',subject='G04.04',gap='Activity audit does not itself capture attribute-level information lineage.')
add('SR',[14,15,16,17],['profileCt3'],'Source-specific update authority is not conferred by possession of provider data.',subject='G18.04',gap='Attribute update authority/policy and enforcement require explicit governance; profile Feature alone is insufficient.')
add('SR',[26,27,29],['nationalPractitioner'],'National provider registration/identity facts have recognised source authority.',subject='G15.02',gap='If an organisation is represented instead, National Organisation Verification is separately relevant.')
add('SR',[30],['nationalPractitioner','nationalOrganisation'],'Source explicitly distinguishes HPI-I and HPI-O; use both existing verification Features without merging person and organisation identity.',subject='G18.04')
add('SR',[28,38,45],['syndication'],'Distribute provider status/update events under governed subscription and source semantics; naming Rhapsody does not grant an engine architectural authority.',subject='G18.05')
add('SR',[6,11,25,44,46],['profileCt3'],'Feed scheduling, schema change and broadcast monitoring are operational integration concerns beyond directory profile content.',subject='G18.05',gap='Operational controls/monitoring and exchange-profile coverage need separate review.')
add('SR',[52,54],['datasetRevision','datasetCt3'],'Maintain/version runtime reference values through governed content revision.',subject='G17.04')
add('SR',[53],['referenceStewardshipCt3'],'Using reference data to verify a provider is not person identity qualification.',subject='G17.05',gap='Provider/reference validation rule and consumer responsibility are unestablished.')
add('SR',[55],['datasetCt3'],'Reference-content write restriction requires established security governance beyond content administration.',subject='G17.05',gap='Reference-dataset change authorisation policy/Feature remains unresolved.')
add('SR',[56,57],['referenceStewardshipCt3','terminologyBinding'],'National/code-set/category bindings need governed definitions and source authority, separately from runtime values.',subject='G17.04')
add('SR',[58],['datasetCt3'],'Expose governed reference content through an authorised application interface.',subject='G17.04',gap='Reference data API/query Feature remains unestablished; no persistence bypass follows.')
full('UC',13,['profileCt3'],'Legacy provider-directory use case spans practitioner/organisation profile and exchange concerns.',subject='G18.05',gap='Resolve provider object and exchange boundary; general organisation-profile association is insufficient.')
add('UC',[5,6],['nationalPractitioner'],'Verify national practitioner registration/identity with issuing authority.',subject='G18.04',gap='Organisation provider sources may instead require National Organisation Verification.')
add('UC',[10],['syndication'],'Broadcast provider updates; the named Rhapsody engine is an implementation constraint candidate, not Harmonia semantic ownership.',subject='G18.05')
add('UC',[11],['profile'],'Maintain the provider profile with explicit source/object qualification.',subject='G18.03',gap='Practitioner and organisation records must remain distinct.')
add('UC',[12],['profileCt3'],'Directory integrity reporting is not contextual person identity qualification.',subject='G18.04',gap='Directory data-quality reporting Feature is unestablished.')
add('UC',[13],['datasetRevision','datasetCt3'],'Provider-directory reference data is governed runtime reference content.',subject='G17.04')
full('UC-Integration-ProviderDirectory',36,['profile'],'Integrate source-qualified practitioner profiles; do not substitute healthcare-organisation profiles.',subject='G18.05',gap='Transport/outcome and failure handling remain broader than profile content administration.')
add('UC-Integration-ProviderDirectory',[11,12,13,14,15,16,17,18,19,20],['discovery'],'Qualified provider discovery/resolution fits the lookup action; it does not decide clinical suitability, referral acceptance or privilege.',subject='G18.01',gap='Referral criteria/recipient authority may require additional independently established coverage.')
add('UC-Integration-ProviderDirectory',[7],['profile','qualificationCt3'],'Synchronise supplied qualifications without granting professional standing.',subject='G15.02')
add('UC-Integration-ProviderDirectory',[8],['roles'],'Synchronise practitioner roles without implying authority to grant them.',subject='G15.03')
add('UC-Integration-ProviderDirectory',[9],['affiliation'],'Synchronise temporal organisational affiliations.',subject='G15.03')
add('UC-Integration-ProviderDirectory',[10],['locationService'],'Synchronise practitioner/service/place associations.',subject='G15.03')
add('UC-Integration-ProviderDirectory',[21,22,23,24],['endpoint'],'Role-specific endpoint binding is more specific than practitioner profile administration.',subject='G15.03',gap='Endpoint availability/verification and delivery contracts may exceed binding.')
add('UC-Integration-ProviderDirectory',[6],['syndication','profile'],'Publish source-qualified provider changes with governed notification semantics.',subject='G18.05')
add('UC-Integration-ProviderDirectory',[29,30,31],['profile'],'Practitioner identifier matching is not healthcare-organisation administration.',subject='G15.01',gap='Matching, duplicate resolution and provider cross-reference authority require explicit review.')
add('UC-Integration-ProviderDirectory',[32,33],['profileCt3'],'Provider quality assessment is not contextual identity qualification.',subject='G18.04',gap='Specific data-quality Feature/criteria are unestablished.')
add('UC-Integration-ProviderDirectory',[34],['auditCt3'],'Provider exchange auditing records significant activity under its capture authority.',subject='G04.01')
add('UC-Integration-ProviderDirectory',[35,36],['profileCt3'],'Interface monitoring/recovery are distinct operational responsibilities.',subject='G09.01',gap='Operational integration monitoring/recovery coverage remains unestablished.')

targets=json.loads((OUT/'mapping-targets.json').read_text())
for t in targets:
    corr=t['identity'].get('correspondence',{}) if t['identity'] else {}
    t['explicitSuccessorCanonicalId']=corr.get('successorCanonicalId')
    t['mappingRows']=sum(r['proposedCapability']==t['exchangeId'] for r in rows)
    t['identityResolution']='Explicit historical correspondence; preserve both identities' if corr.get('successorCanonicalId') else ('Current governed record; semantic applicability still requires review' if t['standing']['currentness']=='current' else 'Superseded target; no direct successor identity inferred')
(OUT/'mapping-targets.json').write_text(json.dumps(targets,ensure_ascii=False,indent=2)+'\n')

reviews={}
for source in sources:
    sid=source['sourceId']
    d=decisions.get(sid)
    if d is None and sid=='UC-1 of DoH_Requirements_Specification_PD v0.3.docx.':
        d=dict(recommendationIds=[ids['profileCt3']],rationale='Receiving external provider details is an integration/profile action, not itself an audit-accountability function. Preserve this irregular source identifier verbatim.',recommendedSubject='G18.05',gap='Tasmanian source object, feed scope and legacy UC-1 correspondence need explicit adjudication; do not merge with another UC-1 by prefix.')
    assert d is not None,(sid,source['path'])
    selected=[]
    for cid in d['recommendationIds']:
        m,b,p=elements[cid]
        selected.append(dict(canonicalId=cid,exchangeId=m['exchange']['identifier'],name=m['displayName'],source=str(p.relative_to(ROOT)),standing=m['standing'],definition=b.split('## Architectural content\n',1)[-1].strip().split('\n\n')[0]))
    feature=any(elements[cid][0]['specialization']=='Feature' for cid in d['recommendationIds'])
    reviews[source['path']]=dict(source=source['path'],sourceId=sid,title=source['title'],originalUserStory=source['userStory'],candidateRecommendation=selected,rationale=d['rationale'],recommendedSubject=d['recommendedSubject'],subjectQualification='Proposed classification only; incoming Subject correspondence remains behind bounded-review gate',featureGap=d['gap'],alignmentOutcome=('FEATURE_FIT_WITH_RESIDUAL_SCOPE' if d['gap'] else 'FEATURE_FIT_FOR_REVIEW') if feature else 'CT3_ONLY_FEATURE_GAP_OR_UNRESOLVED_SCOPE',coverageQualification='Recommendation, not an asserted architectural edge, satisfaction claim, ownership assignment, implementation allocation, or release commitment. No Feature ancestry inferred from a name.',releaseAssignment='UNRESOLVED: no current/future release allocated by this assessment')

for r in rows:
    d=reviews[r['source']]
    r['alignmentOutcome']=d['alignmentOutcome']
    r['alignmentRecommendation']=d['candidateRecommendation']
    r['alignmentRationale']=d['rationale']
    r['featureGap']=d['featureGap']
    r['recommendedSubject']=d['recommendedSubject']
    r['subjectReassessmentRequired']=d['recommendedSubject'] is None or d['recommendedSubject']!=r['primarySubject']
    r['incorporationOutcome']='HOLD: modelling specialisation, Subject correspondence, relationship meaning, identity allocation and release scope require Solution Architect adjudication'
    r['capabilityRelationshipType']=None
    r['subjectRelationshipType']=None

(OUT/'candidates.json').write_text(json.dumps(rows,ensure_ascii=False,indent=2)+'\n')
(OUT/'source-alignment-review.json').write_text(json.dumps(list(reviews.values()),ensure_ascii=False,indent=2)+'\n')
with (OUT/'candidate-assessment.csv').open('w',newline='') as f:
    fields=['mapping_line','source_id','specialization','source','original_text','primary_subject','recommended_subject','subject_reassessment','proposed_capability_exchange_id','proposed_capability_canonical_id','recommended_existing_canonical_ids','alignment_outcome','rationale','feature_gap','incorporation_outcome']
    w=csv.DictWriter(f,fieldnames=fields);w.writeheader()
    for r in rows:
        w.writerow(dict(mapping_line=r['row'],source_id=r['sourceId'],specialization=r['specialization'],source=r['source'],original_text=r['originalText'],primary_subject=r['primarySubject'],recommended_subject=r['recommendedSubject'],subject_reassessment=r['subjectReassessmentRequired'],proposed_capability_exchange_id=r['proposedCapability'],proposed_capability_canonical_id=r['proposedCapabilityCanonicalId'],recommended_existing_canonical_ids=';'.join(x['canonicalId'] for x in r['alignmentRecommendation']),alignment_outcome=r['alignmentOutcome'],rationale=r['alignmentRationale'],feature_gap=r['featureGap'],incorporation_outcome=r['incorporationOutcome']))

summary=dict(candidateCount=len(rows),sourceCount=len(reviews),alignmentOutcomes=dict(collections.Counter(r['alignmentOutcome'] for r in rows)),sourcesWithFeatureGap=sum(bool(x['featureGap']) for x in reviews.values()),rowsWithFeatureGap=sum(bool(x['featureGap']) for x in rows),subjectReassessmentRows=sum(r['subjectReassessmentRequired'] for r in rows),multipleCapabilityRows=sum(len(r['alignmentRecommendation'])>1 for r in rows),historicalTargetRows=sum(t['mappingRows'] for t in targets if t['standing']['currentness']!='current'),proposedCanonicalIdsAllocated=0,relationshipsAsserted=0,incorporated=0)
(OUT/'alignment-summary.json').write_text(json.dumps(summary,indent=2)+'\n')
print(json.dumps(summary,indent=2))
