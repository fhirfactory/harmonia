#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Apply the bounded human-reviewed BEC reconciliation after Stage-2 composition.

This is an explicit semantic migration, not taxonomy inference. Superseded records
and unresolved responsibilities remain addressable, with exact pre-review material.
The reviewed candidate does not promote Canonical or relationship assertion status.
"""
import collections
import copy
import hashlib
import json
import re
from pathlib import Path

BASE = Path(__file__).resolve().parents[1]
SCOPE = 'Not Addressed in R1.x / R2.x'
INPUT = 'docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json'

# Every reused identity denotes a surviving responsibility. The six unelaborated
# clinical contexts retain their Canonical contextual participation, now explicitly
# bounded by the reviewed operational-dependency purpose, with source history intact.
REUSE = {
 'Entity Information Enablement':'Healthcare Entity Information Enablement',
 'Care Administration Enablement':'Care Administration Information Enablement',
 'Health Service Operations Enablement':'Care Operations Coordination Enablement',
 'Shared Information & Coordination Enablement':'Governed Health Information Enablement',
 'Health Informatics Management':'Secondary Health Information Use Enablement',
 'Independent Assurance Enablement':'Independent Service Assurance Enablement',
 'Health Materiel Administration':'Health Product Administration',
 'Person Identity Administration':'Person Identity',
 'Healthcare Subject Profile Administration':'Healthcare Subject',
 'Client Relationship Administration':'Client Relationship',
 'Client Privacy Guardrail Management':'Client Privacy',
 'Practitioner Profile Administration':'Practitioner',
 'Practitioner Role Administration':'Practitioner Role',
 'Healthcare Organisation Profile Administration':'Healthcare Organisation',
 'Healthcare Location Profile Administration':'Healthcare Location',
 'Care-Place Definition Administration':'Care-Place Definition',
 'Healthcare Service Profile Administration':'Healthcare Service Definition',
 'Health Materiel Profile Administration':'Health Product Definition',
 'Health Materiel Catalogue & Formulary Administration':'Product Formulary & Catalogue',
 'Health Materiel Traceability Administration':'Product Batch & Serial Tracking',
 'Clinical Device Profile Administration':'Device Definition',
 'Clinical Workforce Capability Monitoring':'Clinical Workforce Management',
 'Clinical Competency Administration':'Clinical Competency Management',
 'Clinical Qualification Administration':'Clinical Qualification Management',
 'Linked Information Curation':'Associated Clinical Binary Content Enablement',
 'Data Provenance Capture':'Information Lineage Stewardship',
 'Research Information Curation':'Authorised Research Information Provision',
 'Population Health Information Reporting':'Population Health Notification Enablement',
 'Service Planning Data Management':'Service Planning Information Provision',
 'Assurance Specification':'Assurance Specification Enablement',
 'Assurance Criteria Administration':'Assurance Criteria Management',
 'Health Information Exchange':'Health Information Exchange Enablement',
}
# These names coincide, but the original Canonical semantics do not survive as
# the reviewed meaning. Never reuse an identity on lexical similarity alone.
SEPARATE = {'Credential Management','Health Information Communication','Workflow & Activity Coordination'}

DEFINITIONS = {
 'Entity Information Enablement':'Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices.',
 'Care Administration Enablement':'Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems.',
 'Clinical Service Dependency Management':'Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.',
 'Health Service Operations Enablement':'Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.',
 'Shared Information & Coordination Enablement':'Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.',
 'Health Informatics Management':'Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.',
 'Independent Assurance Enablement':'Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.',
 'Client Privacy Guardrail Management':'Manage client-associated privacy constraints, preferences, permissions, restrictions and consent directives, and evaluate their applicability to information use.',
 'Healthcare Organisation Profile Administration':'Administer identifying, descriptive, structural, location, contact, registered/legal and recognised endpoint information needed to represent and interact with a healthcare organisation.',
 'Healthcare Location Profile Administration':'Administer the identity, address, geospatial description and physical containment relationships of healthcare locations.',
 'Healthcare Service Profile Administration':'Administer service definitions, provision relationships, defined availability and eligibility information describing offered healthcare services.',
 'Health Materiel Traceability Administration':'Administer batch/lot, serial, supplier, manufacturer, receipt/delivery and relevant movement/provenance information concerning health materiel.',
 'Clinical Device Profile Administration':'Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities.',
 'Device Utilisation Administration':'Administer qualified device-to-subject/place allocation, location, utilisation and operational readiness information, including the time at which an association applies.',
 'Device OA&M Metadata Management':'Manage operational and administrative metadata needed to understand how a clinical device is connected, monitored and administered.',
 'Device Connectivity Compliance Management':'Manage information about whether device connectivity satisfies applicable standards, policies and integration constraints.',
 'Vendor Liaison':'Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision.',
 'Clinical Workforce Capability Monitoring':'Monitor supplied information about the clinical capability available from the workforce without acquiring employment or clinical allocation authority.',
 'Clinical Competency Administration':'Administer information about what clinical capability an organisation can provide and what a practitioner is competent to perform.',
 'Clinical Qualification Administration':'Administer organisation clinical certification/compliance information and provider qualification evidence with its externally established standing.',
 'Clinical Service Credential Definition Management':'Manage the definitions of credentials required for participation in a clinical service, distinct from physical badges and authentication credentials.',
 'Clinical Escalation Path Management':'Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants.',
 'Clinical Logistics Coordination':'Coordinate specimen metadata and specimen movement traceability across responsible collection, courier and diagnostic services.',
 'Health Information Control':'Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls.',
 'Workflow & Activity Coordination':'Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.',
 'External Task Coordination':'Coordinate work entering or leaving the Harmonia execution boundary with the system or actor responsible for undertaking and completing that work.',
 'External Task Status & Traceability Management':'Manage the observable status and attributable traceability of work as it progresses through systems outside the Harmonia execution boundary.',
 'Activity Routing Management':'Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility.',
 'Work Boundary Governance':'Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.',
 'Information Asset Stewardship Management':'Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses.',
 'Information Content Maintainer Management':'Administer information-content maintainer participation, source provenance and governed information products.',
 'Data Scientist Administration':'Administer profiles and qualified participation information for data scientists maintaining governed information content.',
 'Data Provenance Capture':'Capture meaningful source attribution and transformation lineage for governed information content without transferring originating authority.',
 'Information Product Administration':'Administer identifiable governed information products, their purpose, content standing and qualified release/use context.',
 'Clinical Knowledge Services':'Administer clinical knowledge requests, distribute governed knowledge artefacts and administer approved reference datasets.',
 'Research Information Curation':'Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints.',
 'Population Health Information Reporting':'Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations.',
 'Service Planning Data Management':'Establish and maintain governed information assets suitable for health-service planning, modelling and analysis.',
 'Assurance Service Review':'Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself.',
 'Assurance Service Evaluation':'Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review.',
 'Clinical Supervision':'Enable responsible clinical participants to identify and use the applicable clinical escalation path.',
 'Credential Management':'Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges.',
 'Health Information Communication':'Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.',
}
# Authored concise definitions for the remaining reviewed leaf abilities.
LEAF_DEFINITIONS = {
 'Person Identity Administration':'Resolve, maintain and correlate identities and identifiers assigned to a person across issuing authorities, retaining source-qualified correction history.',
 'Healthcare Subject Profile Administration':'Administer demographic and subject-of-care profile information bound to clinical records and operational activities.',
 'Client Relationship Administration':'Administer qualified client relationships with carers, guardians, contacts and other nominated care participants.',
 'Practitioner Profile Administration':'Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution.',
 'Practitioner Role Administration':'Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints.',
 'Care-Place Definition Administration':'Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place.',
 'Health Materiel Profile Administration':'Administer identifying and descriptive information about health materiel referenced in healthcare information and activity.',
 'Health Materiel Catalogue & Formulary Administration':'Administer health-materiel catalogue and externally approved formulary information, preserving approval and version standing.',
 'Referral Intake & Progression':'Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems.',
 'Appointment & Booking Administration':'Administer externally supplied appointment and booking information and communicate changes in their scheduling status.',
 'Closed-Loop Order Progression':'Administer the traceable progression of an order from request through destination, delivery, acknowledgement, fulfilment and outcome association.',
 'Clinical Documentation Lifecycle Administration':'Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation.',
 'Care Plan Lifecycle & Coordination':'Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source.',
 'Session & Queue Coordination':'Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information.',
 'Ward Logistics Coordination':'Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information.',
 'Perioperative Logistics Coordination':'Coordinate theatre case progression information and notifications to supporting perioperative operational teams.',
 'Emergency Department Operational Coordination':'Coordinate emergency-department bed/bay occupancy, operational waits, length-of-stay milestones and local capacity information.',
 'Outpatient Flow Coordination':'Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information.',
 'Bed & Care-Place State Administration':'Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places.',
 'Bed & Care-Place Turnover Coordination':'Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications.',
 'Service Demand & Capacity Coordination':'Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers.',
 'Clinical Roster Capture & Communication':'Capture and communicate supplied clinical roster information, its effective periods and subsequent coverage changes.',
 'Mobile Worker Dispatch & Safety Coordination':'Exchange externally established mobile work assignments, visit progression and safety check-in information with qualified worker context.',
 'On-Call Availability & Contact Resolution':'Resolve currently available on-call providers and their contact paths for the applicable specialty, service and time.',
 'Operational Work Assignment & Dispatch Coordination':'Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority.',
 'Organisation Clinical Certification & Compliance Support':'Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation.',
 'Provider Clinical Qualification Tracking':'Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing.',
 'Patient Transfer Coordination':'Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones.',
 'Specimen Metadata Management':'Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen.',
 'Specimen Movement Traceability':'Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens.',
 'Discharge Progression & Coordination':'Coordinate externally established discharge milestones, multidisciplinary readiness information and departure notifications.',
 'Longitudinal Clinical Record Administration':'Administer longitudinal record assembly, active clinical summaries and durable received/managed history with source attribution and supersession.',
 'Linked Information Curation':'Curate associations to information and binary content needed to interpret and use a clinical record, retaining custody and source-authority distinctions.',
 'HIE Requirement Management':'Manage the identified information-exchange requirements, interaction contracts and participant obligations applicable to a health-information exchange.',
 'HIE Implementation Management':'Manage implementation information and progression for established exchange requirements, including submission, retrieval, distribution and syndication interactions.',
 'HIE Endpoint State Management':'Manage recognised exchange endpoint registrations, operational standing and contract-qualified availability information.',
 'Governed Health Information Retrieval':'Retrieve and search health information and apply access-qualified result filtering under established access and consent determinations.',
 'Patient Notification Services':'Enable governed patient notifications concerning health information and applicable care or operational events.',
 'Patient Task Communication Services':'Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts.',
 'Patient Self-Service':'Enable patients to undertake permitted information-access and administrative self-service interactions.',
 'Patient Health Library Access':'Enable authorised patient access to appropriately governed health knowledge and library information.',
 'Patient Health Information Restriction Management':'Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management.',
 'Patient Health Information Filter Management':'Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations.',
 'Patient Health Information Disclosure Management':'Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions.',
 'Information Asset Definition':'Define the identifying, semantic and governed-use characteristics of an information asset.',
 'Information Asset Tag Management':'Manage attributable tags describing information assets without treating caller-controlled tags as security authority.',
 'Clinical Knowledge Request Administration':'Administer clinical knowledge requests and their qualified resolution to applicable governed knowledge or reference content.',
 'Clinical Knowledge Artefact Distribution Management':'Manage distribution of governed clinical knowledge artefacts to intended consumers under their applicable use contracts.',
 'Governed Reference Dataset Administration':'Administer approved runtime reference-dataset content and revisions separately from the reusable models defining its meaning.',
 'Clinical Context Collaboration':'Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records.',
 'Operational Collaboration':'Enable operational team discussion and collaboration without promoting conversational content to clinical record authority.',
 'Operational & Clinical Calendar Coordination':'Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters.',
 'Contextual Information Presentation':'Present governed longitudinal clinical and operational information in its applicable human interaction context.',
 'Enterprise Canonical/Reusable Model Design':'Design canonical and reusable enterprise information models and semantic bindings under applicable information-design governance.',
 'Enterprise Canonical/Reusable Model Library Management':'Manage the identifiable library of canonical and reusable enterprise model definitions and their governed revisions.',
 'Enterprise Canonical/Reusable Model Utilisation Support':'Support correct utilisation and conformance of canonical and reusable models across their intended consumers.',
 'Enterprise Reference Data Stewardship':'Steward enterprise reference-data definitions, bindings and applicable governance independently of mutable runtime dataset administration.',
 'Assurance Design':'Model how satisfaction of a governed subject or requirement will be assured, preserving applicable assurance dispositions and boundaries.',
 'Assurance Criteria Administration':'Administer reusable governed assurance criteria and their version-specific and temporal applicability without changing governing requirement authority.',
}
DEFINITIONS.update(LEAF_DEFINITIONS)

# Existing service-context semantics are relocated, never converted to dependency
# tracking. Each entry records a responsibility, its meaningful name and target.
RELOCATIONS = {
 'Primary Care':('Primary Care Coordination','Patient Clinical Record'),
 'Acute Care':('Acute Care Monitoring & Event Coordination','Episode & Encounter Administration'),
 'Emergency Care':('Emergency Care Progression','Emergency Department Operations'),
 'Inpatient Care':('Multidisciplinary Inpatient Care Coordination','Ward Operations'),
 'Diagnostic Services':('Diagnostic Service Integration','Diagnostic Administration'),
 'Medication Therapy':('Medication Therapy Provision and Monitoring','Medication Administration'),
 'Preventive Care':('Screening and Immunisation Enablement','Client-Centred Administration'),
 'Community Care':('Community Care Coordination','Care Plan Administration'),
 'Outreach Care':('Outreach Care Continuity','Care Plan Administration'),
 'Remote & Virtual Care':('Remote Patient Monitoring','Patient Clinical Record'),
}
# Useful established descriptions retained only where behaviour already warrants
# a CT3 frame; these are not invented decomposition for release-scope symmetry.
RETAINED_FRAMES = {
 'Client-Centred Administration':'Client Intake & Demographic Update',
 'Episode & Encounter Administration':'Encounter Lifecycle Management',
 'Diagnostic Administration':'Diagnostic Workflow Management',
 'Medication Administration':'Medication Order & Dispense Management',
 'Procedure Administration':'Procedure & Surgical Administration',
 'Clinical Communication Administration':'Secure Clinical Communication',
}


def apply_review(m):
    before = copy.deepcopy(m)
    old = {e['key']:e for e in before['elements']}
    e = {x['key']:x for x in m['elements']}
    bec_before = [x for x in before['elements'] if x['category']=='business_enabling']
    old_names = {x['name']:x['key'] for x in bec_before}
    spec = json.loads((BASE/'reviewed-bec-hierarchy.json').read_text())
    resolved = {}
    decisions = {}
    evidence = {'path':INPUT, 'line':2, 'text':(BASE/'reviewed-bec-hierarchy.json').read_text().splitlines()[1],
                'standing':'Explicit human task decision; reviewed candidate, not RADS acceptance'}
    prov = {'derivation':'Reconciliation against the explicit human-reviewed BEC candidate hierarchy',
            'evidence':[evidence], 'method':'Bounded semantic reconciliation; no unconstrained synthesis',
            'authority':'Proposed structural assertions; existing Canonical semantic standing retained'}
    for x in e.values(): x['record_state']='current'

    def slug(s): return re.sub(r'[^a-z0-9]+','-',s.lower()).strip('-')
    def definition(node):
        name=node['name']
        if node.get('parent')=='Clinical Service Dependency Management':
            return 'Identify and track the operational and infrastructure dependencies necessary for '+name.lower()+' services to function, retaining each dependency provider\'s responsibility.'
        if name in DEFINITIONS: return DEFINITIONS[name]
        # CT2s already carry exact source semantics, preserved beneath the new root.
        k=old_names.get(REUSE.get(name,name))
        if k:
            x=old[k]
            return x.get('definition') or x.get('baseline_description',{}).get('Capability Overview') or 'Administer the established information and coordination responsibilities of '+name+'.'
        return {
            'Service Establishment Support':'Support the information needed to establish a health service in its intended operating context.',
        }[name]

    def new(name,tier,parent,definition_text,reviewed=True):
        key='reviewed:bec-'+slug(name)
        assert key not in e
        x=dict(key=key, reference_id='HCM-BEC-reviewed-'+slug(name), latex_label='cap:bec-reviewed-'+slug(name),
               name=name,canonical_name=None,name_status='Proposed',type='capability',category='business_enabling',
               tier=tier,parent_key=parent,owner_key=None,architectural_status='Proposed',canonical_id=None,
               local_id='BEC-'+slug(name).upper(),ancestry_status='proposed',placement_status='Proposed',
               structural_id_status='Proposed',sources=[],unresolved=[],views=[],id_aliases=[],name_aliases=[],
               baseline_version=None,definition=definition_text,
               overview=definition_text+' The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.',
               intent=definition_text,boundary='Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.',
               provenance=copy.deepcopy(prov),placement_provenance=copy.deepcopy(prov),record_state='current')
        m['elements'].append(x);e[key]=x
        return x

    def rename(x,name):
        if x['name']!=name:
            x['name_aliases']=sorted(set(x.get('name_aliases',[])+[x['name']]))
            x.update(name=name,name_status='Proposed')

    def place(x,tier,parent):
        x.update(tier=tier,parent_key=parent,placement_status='Proposed',ancestry_status='proposed',
                 structural_id_status='Proposed',placement_provenance=copy.deepcopy(prov),
                 placement_rationale='Responsibility-preserving placement under the human-reviewed BEC hierarchy; the structural assertion remains Proposed.')

    # All old BEC identities start preserved as superseded records. Explicitly
    # selected survivors re-enter the current tree; no implicit catch-all branch.
    for x in bec_before:
        e[x['key']]['record_state']='superseded'
    for node in spec['nodes']:
        name=node['name']; parent=resolved.get(node['parent'])
        prior=REUSE.get(name,name)
        k=old_names.get(prior)
        if name in SEPARATE or (node.get('parent')=='Clinical Service Dependency Management' and name in RELOCATIONS): k=None
        x=e[k] if k else new(name,node['tier'],parent,definition(node))
        if k:
            x['record_state']='current';rename(x,name);place(x,node['tier'],parent)
            # Original definitions/provenance stay in the immutable review history.
            x.update(definition=definition(node),overview=definition(node),intent=definition(node))
            x['review_provenance']=copy.deepcopy(prov)
        x['reviewed_name']=name
        x['boundary']=x.get('boundary') or 'Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.'
        if 'release_scope' in node:x['release_scope']=node['release_scope']
        resolved[name]=x['key']
        if k:decisions[k]={'classification':'renamed' if old[k]['name']!=x['name'] else 'retained unchanged', 'targets':[x['key']]}

    def current(name): return e[resolved[name]]
    def add_frame(name,parent,definition_text=None):
        x=new(name,'CT3',resolved[parent],definition_text or ('Administer the established '+name.lower()+' information and coordination responsibility while preserving its source authority.'),False)
        return x
    frames={}
    for parent,name in RETAINED_FRAMES.items():
        if not name:continue
        contexts=[c for c in before['catalogue_contexts'] if c['name']==name]
        assert len(contexts)==1
        # Contextual descriptions were not identities in v0.2: allocate a new stable
        # CT3 identity and preserve the old context locator in provenance.
        x=add_frame(name,parent)
        x['retained_context']=contexts[0]['key'];frames[parent]=x['key']

    # Consolidations: preserve the selected survivor and each predecessor record.
    merges={
      'Practitioner Role Administration':['Provider Relationship'],
      'Provider Clinical Qualification Tracking':['Provider Engagement'],
      'Healthcare Organisation Profile Administration':['Organisational Structure','Organisation Contact'],
      'Healthcare Location Profile Administration':['Location Structure'],
      'Healthcare Service Profile Administration':['Service Provision Relationship','Service Availability Definition','Service Eligibility Definition'],
      'Clinical Device Profile Administration':['Device Instance'],
    }
    remap={}
    for target,names in merges.items():
        for name in names:
            k=old_names[name];remap[k]=resolved[target]
            decisions[k]={'classification':'consolidated','targets':[resolved[target]],'reason':'Predecessor information semantics incorporated without retaining a noun-based peer capability.'}
        survivor=resolved[target]
        if survivor in old:
            decisions[survivor]['classification']='consolidated'
        current(target)['predecessor_keys']=[k for k in [survivor]+[old_names[n] for n in names] if k in old]

    # Device Association is distributed, not silently renamed to one successor.
    association=old_names['Device Association']
    remap[association]=resolved['Device Utilisation Administration']
    decisions[association]={'classification':'decomposed','targets':[resolved['Device Utilisation Administration'],resolved['Device OA&M Metadata Management']],
      'semantic_distribution':{'Device Utilisation Administration':'Subject/place assignment, allocation, utilisation intervals and association lineage.',
                               'Device OA&M Metadata Management':'Administrative association metadata needed to describe device connection, monitoring and administration.'}}
    current('Device Utilisation Administration')['predecessor_keys']=[association,old_names['Clinical Resource Management']]
    current('Device OA&M Metadata Management')['predecessor_keys']=[association,old_names['Device Connectivity']]
    remap[old_names['Device Connectivity']]=resolved['Device OA&M Metadata Management']
    decisions[old_names['Device Connectivity']]={'classification':'consolidated','targets':[resolved['Device OA&M Metadata Management']],
      'reason':'Existing endpoint registrations and connectivity bindings are retained as operational/administrative metadata. Connectivity compliance is a separately reviewed new ability; registration is not evidence of compliance.'}
    engagement=old_names['Provider Engagement']
    decisions[engagement]={'classification':'decomposed','targets':[resolved['Practitioner Role Administration'],resolved['Provider Clinical Qualification Tracking']],
      'semantic_distribution':{'Practitioner Role Administration':'Information about the terms and organisational context of provider participation, without contract-management authority.',
                               'Provider Clinical Qualification Tracking':'Externally established clinical privilege evidence; FEAT-EM-16 remains privilege tracking, not privilege granting.'}}
    resource=old_names['Clinical Resource Management'];remap[resource]=resolved['Device Utilisation Administration']
    decisions[resource]={'classification':'superseded by reviewed capability','targets':[resolved['Device Utilisation Administration'],resolved['Clinical Service Dependency Management']],
                         'reason':'Equipment location/allocation survives under device utilisation; service dependency tracking belongs to the reviewed dependency CT1. No independent generic resource manager is retained.'}

    for source,(name,parent) in RELOCATIONS.items():
        x=e[old_names[source]];x['record_state']='current';rename(x,name);place(x,'CT3',resolved[parent])
        x['review_provenance']=copy.deepcopy(prov)
        x['relocation_reason']='The established information integration/coordination responsibility survives; it does not identify or track infrastructure prerequisites of a clinical service.'
        decisions[x['key']]={'classification':'relocated','targets':[x['key']],'reason':x['relocation_reason']}

    # Strict semantic conflicts retain the original responsibility and exact owned
    # material outside the reviewed hierarchy, rather than borrowing a false parent.
    pending_reasons={
      'Credential Management':'Canonical operational badges/physical-access credentials are distinct from reviewed clinical-service credential definitions.',
      'Health Information Communication':'Canonical boundary protocol mediation and technical transmission are distinct from reviewed patient-facing communication.',
      'Workflow & Activity Coordination':'Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge.',
      'Governed Assurance':'Canonical assurance against a governed subject is not independent review of the assurance service itself.',
      'Independent Assurance Evaluation Enablement':'The previous grouping frames Canonical subject-assurance execution; it cannot be equated with review of the assurance service itself.',
      'Information Authority Representation':'Source credibility and authority standing require an explicit home; information-asset tags alone do not establish that responsibility.',
      'Significant Activity Audit Capture':'The reviewed hierarchy does not allocate general mandatory authoritative audit capture; disclosure or provenance alone is not an equivalent responsibility.',
      'Governance Requirement Publication Support':'Requirement publication/authority is not assurance-service review or information-model design; the reviewed hierarchy does not allocate a home.',
      'Entity-Specific Operational Coordination':'Internal entity-relative progression is not the reviewed external work bridge; a current BEC destination has not been adjudicated.',
      'Governed Operational Activity Design':'Internal execution-envelope design is distinct from governance of work across an external execution boundary.',
    }
    # Names already reused for the reviewed successor must refer to the original
    # identity here, not the new same-named reviewed element.
    for name,reason in pending_reasons.items():
        x=e[old_names[name]];x.update(record_state='pending_adjudication',adjudication_note='HUMAN ADJUDICATION REQUIRED: '+reason)
        decisions[x['key']]={'classification':'genuine unresolved conflict requiring human adjudication','targets':[],'reason':reason}

    # Compatible Stage-2 additions retain meaning and explicit purpose association.
    remap[old_names['Service Offering Readiness Coordination']]=resolved['Clinical Service Dependency Management']
    decisions[old_names['Service Offering Readiness Coordination']]={'classification':'superseded by reviewed capability','targets':[resolved['Clinical Service Dependency Management']],
      'reason':'Dependency evidence material retained at the out-of-scope CT1. A proposed determination of collective go/no-go readiness is not inherited as authority by dependency tracking.'}
    # Preserve the old readiness decision semantics as unresolved, not republish as
    # newly authorised dependency assessment. Only neutral dependency evidence is reusable.
    e[old_names['Service Offering Readiness Coordination']].update(record_state='pending_adjudication',
      adjudication_note='HUMAN ADJUDICATION REQUIRED: source proposal determines collective readiness; reviewed scope establishes dependency tracking only. Evidence may be reused, but determination authority is not silently transferred.')
    decisions[old_names['Service Offering Readiness Coordination']].update(classification='genuine unresolved conflict requiring human adjudication',targets=[resolved['Clinical Service Dependency Management']])
    remap.pop(old_names['Service Offering Readiness Coordination'])

    # Feature frames selected for actual semantics, never mechanical symmetry.
    single_frames={
      'Referral Administration':'Referral Intake & Progression','Scheduling Administration':'Appointment & Booking Administration',
      'Order Administration':'Closed-Loop Order Progression','Clinical Record Administration':'Clinical Documentation Lifecycle Administration',
      'Care Plan Administration':'Care Plan Lifecycle & Coordination','Clinic & Practice Operations':'Session & Queue Coordination',
      'Ward Operations':'Ward Logistics Coordination','Theatre Operations':'Perioperative Logistics Coordination',
      'Emergency Department Operations':'Emergency Department Operational Coordination','Outpatient Operations':'Outpatient Flow Coordination',
      'Service Capacity Management':'Service Demand & Capacity Coordination','Mobile Staff Management':'Mobile Worker Dispatch & Safety Coordination',
      'On-Call Management':'On-Call Availability & Contact Resolution','Work Allocation & Dispatch':'Operational Work Assignment & Dispatch Coordination',
      'Clinical Qualification Management':'Provider Clinical Qualification Tracking','Patient Transport':'Patient Transfer Coordination',
      'Clinical Logistics Coordination':'Specimen Movement Traceability','Discharge Management':'Discharge Progression & Coordination',
      'Patient Clinical Record':'Longitudinal Clinical Record Administration','Health Information Exchange Enablement':'HIE Implementation Management',
      'Health Information Access':'Governed Health Information Retrieval','Clinical Knowledge Services':'Governed Reference Dataset Administration',
      'Clinical Collaboration':'Clinical Context Collaboration','General Collaboration':'Operational Collaboration',
      'Calendar Management':'Operational & Clinical Calendar Coordination','Presentation Services':'Contextual Information Presentation',
      'Information Design Governance':'Enterprise Canonical/Reusable Model Design',
    }
    overrides={
      'FEAT-HSO-11':'Bed & Care-Place State Administration','FEAT-HSO-12':'Bed & Care-Place Turnover Coordination','FEAT-HSO-13':'Bed & Care-Place Turnover Coordination',
      'FEAT-ISE-11':'Patient Health Information Restriction Management',
      'FEAT-ISE-14':'Clinical Knowledge Request Administration','FEAT-ISE-15':'Clinical Knowledge Request Administration',
      'FEAT-ISE-27':'Enterprise Canonical/Reusable Model Design','FEAT-ISE-28':'Enterprise Canonical/Reusable Model Utilisation Support',
    }
    feature_moves=[]
    for x in list(e.values()):
        if x['type']!='feature':continue
        original=old[x['key']];owner=original['owner_key'];owner_name=old[owner]['name']
        destination=None
        if owner in remap:destination=remap[owner]
        elif owner_name in frames:destination=frames[owner_name]
        elif owner_name in single_frames:destination=resolved[single_frames[owner_name]]
        elif owner_name in ['Authorised Research Information Provision','Population Health Notification Enablement','Service Planning Information Provision']:
            destination=resolved['Information Product Administration'];x['purpose_context_keys']=[owner]
        elif owner_name=='Health Information Control':
            # General security-context, authentication, policy assignment and audit
            # Features have no safe patient-only CT3 frame in the reviewed candidate.
            if x['reference_id']!='FEAT-ISE-11':
                x.update(record_state='pending_adjudication',adjudication_note='HUMAN ADJUDICATION REQUIRED: general platform security/evidence Feature has no adjudicated patient-specific CT3 frame; established Health Information Control responsibility is preserved.')
        if x['reference_id'] in overrides:destination=resolved[overrides[x['reference_id']]]
        if destination:
            x.update(owner_key=destination,placement_status='Proposed',placement_provenance=copy.deepcopy(prov),
                     placement_rationale='Meaningful reviewed CT3 framing selected for the existing Feature semantics; established responsibility provenance is preserved.')
        if e[x['owner_key']]['record_state']=='pending_adjudication':
            x.update(record_state='pending_adjudication',adjudication_note=e[x['owner_key']]['adjudication_note'])
        if owner!=x['owner_key']:
            x['reviewed_responsibility_relocation']={'previous_owner':owner,'current_frame':x['owner_key'],'status':'Proposed','provenance':copy.deepcopy(prov)}
            feature_moves.append({'reference_id':x['reference_id'],'before_owner':owner,'after_owner':x['owner_key'],'reason':x['placement_rationale']})

    # Direct Function/Service/Process ownership survives unless its predecessor is
    # consolidated or decomposed. Those explicit moves retain original ownership
    # evidence and do not invent one-to-one Feature/Function links.
    behaviour_moves=[]
    for x in e.values():
        if x['type'] not in ('function','service','process'):continue
        owner=x['owner_key']
        if owner in remap:
            x['owner_key']=remap[owner];x['placement_status']='Proposed'
            x['reviewed_responsibility_relocation']={'previous_owner':owner,'current_frame':x['owner_key'],'status':'Proposed','provenance':copy.deepcopy(prov)}
            behaviour_moves.append({'reference_id':x['reference_id'],'before_owner':owner,'after_owner':x['owner_key']})
        if e[x['owner_key']]['record_state']=='pending_adjudication':
            x.update(record_state='pending_adjudication',adjudication_note=e[x['owner_key']]['adjudication_note'])

    # Exact Canonical wording mixes specimens and materiel. The reviewed boundary
    # excludes materiel here; do not silently narrow a Canonical Function/Process
    # or claim a newly split realisation without adjudication.
    mixed_logistics={
      'HCM-FN-manage-specimen-transport-progression':'The Canonical Function includes pathology bio-specimens, blood products and surgical trays. Surgical-tray/materiel movement is excluded from reviewed Clinical Logistics Coordination; a specimen/materiel semantic split requires human judgement.',
      'HCM-PR-specimen-transport-process':'The Canonical Process includes blood products as well as bio-specimens and surgical biopsies. Whether the blood-product wording denotes diagnostic specimens or transported health materiel is unadjudicated; the original Process is preserved without narrowing it.',
    }
    for x in e.values():
        if x['reference_id'] in mixed_logistics:
            x.update(record_state='pending_adjudication',adjudication_note='HUMAN ADJUDICATION REQUIRED: '+mixed_logistics[x['reference_id']])

    # Explicit reviewed qualifications replace contradicting current descriptions;
    # exact old text remains in pre_review_record / governed source appendix.
    for name in ['Client Privacy Guardrail Management','Healthcare Service Profile Administration','Health Materiel Traceability Administration','Device OA&M Metadata Management','Device Connectivity Compliance Management','Clinical Escalation Path Management','Clinical Logistics Coordination','Health Informatics Management','Assurance Service Review','Assurance Service Evaluation','Workflow & Activity Coordination']:
        x=current(name)
        x['boundary']={
         'Client Privacy Guardrail Management':'Owns client-associated constraints and consent directives; general enterprise privacy policy remains outside this capability.',
         'Healthcare Service Profile Administration':'Defined availability is not dynamic operational capacity, collective readiness or authority to accept clinical care.',
         'Health Materiel Traceability Administration':'Materiel traceability does not make Harmonia a procurement, stock-control or supply-chain manager; specimen movement remains Clinical Logistics Coordination.',
         'Device OA&M Metadata Management':'Connectivity, monitoring and administration metadata do not perform device maintenance, interpret clinical telemetry or determine conformance.',
         'Device Connectivity Compliance Management':'Endpoint registration alone does not establish compliance; conformity concerns applicable standards, policies and integration constraints.',
         'Clinical Escalation Path Management':'Harmonia manages the escalation path; responsible clinical participants perform supervision and retain clinical judgement.',
         'Clinical Logistics Coordination':'Includes specimen metadata and specimen movement traceability. Supply/materiel movement belongs to Health Materiel Traceability Administration.',
         'Health Informatics Management':'Harmonia is adjacent to a Big Data/analytical environment. It establishes governed assets and reporting inputs; it does not become that analytical platform.',
         'Assurance Service Review':'Reviews the assurance service itself. Canonical evidence association, subject assessment, adjudication, findings and conclusions are retained separately pending adjudication.',
         'Assurance Service Evaluation':'Evaluation concerns operation, effectiveness, appropriateness and conformance of the assurance service; it is not execution of assurance against a subject.',
         'Workflow & Activity Coordination':'External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.',
        }[name]

    # Respect Canonical consent authority; references express use, not duplication.
    consent=resolved['Client Privacy Guardrail Management']
    for name in ['Health Information Control','Patient Health Information Restriction Management','Patient Health Information Filter Management','Patient Health Information Disclosure Management']:
        x=current(name);x['respects_consent_responsibility']=consent
        x['boundary']+=' Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.'

    # CM-R05 is a real conflict between reviewed wording and Canonical BC wording.
    # Keep the existing unique BEC display qualification pending name adjudication.
    hie=current('Health Information Exchange');rename(hie,'Health Information Exchange Enablement')
    hie['naming_rationale']='HUMAN ADJUDICATION REQUIRED: reviewed name Health Information Exchange duplicates the Canonical BC name under CM-R05. The existing BEC display qualification is retained; reviewed_name records the exact task wording. Identities and BC/BEC semantics remain distinct.'

    # Superseded grouping identities remain published as historical records.
    replacements={
      'Care Context Integration Enablement':['Clinical Service Dependency Management','Patient Clinical Record','Episode & Encounter Administration','Emergency Department Operations','Ward Operations','Diagnostic Administration','Medication Administration','Client-Centred Administration','Care Plan Administration'],
      'Human Collaboration & Work Enablement':['Shared Information & Coordination Enablement'],
      'Care Access Transaction Enablement':['Client-Centred Administration','Referral Administration','Scheduling Administration'],
      'Care Fulfilment Administration Enablement':['Episode & Encounter Administration','Order Administration','Diagnostic Administration','Medication Administration','Procedure Administration'],
      'Care Documentation & Communication Enablement':['Clinical Record Administration','Care Plan Administration','Clinical Communication Administration'],
      'Care Financial Information Enablement':['Funding & Coverage Administration','Clinical Charging & Claims Administration'],
      'Longitudinal & Community Care Integration':['Patient Clinical Record','Care Plan Administration','Client-Centred Administration'],
      'Facility & Episodic Care Integration':['Episode & Encounter Administration','Emergency Department Operations','Ward Operations','Clinical Service Dependency Management'],
      'Specialist Clinical Context Integration':['Diagnostic Administration','Medication Administration','Clinical Service Dependency Management'],
      'Care Setting Operations Enablement':['Clinic & Practice Operations','Ward Operations','Theatre Operations','Emergency Department Operations','Outpatient Operations'],
      'Care Resource & Capacity Coordination':['Bed & Care-Place Management','Service Capacity Management','Device Utilisation Administration','Clinical Service Dependency Management'],
      'Operational Workforce Coordination':['Clinical Workforce Capability Monitoring','Clinical Rostering','Mobile Staff Management','On-Call Management','Work Allocation & Dispatch'],
      'Workforce Qualification & Development Enablement':['Clinical Training & Education','Clinical Competency Administration','Clinical Qualification Administration','Clinical Supervision','Credential Management'],
      'Care Movement & Transition Coordination':['Patient Transport','Clinical Logistics Coordination','Discharge Management'],
      'Clinical Information Stewardship Enablement':['Patient Clinical Record'],
      'Health Information Connectivity Enablement':['Health Information Exchange','Health Information Access','Health Information Communication'],
      'Semantic & Reference Knowledge Enablement':['Clinical Knowledge Services','Information Design Governance'],
      'Information Protection & Accountability Enablement':['Health Information Control','Information Content Maintainer Management'],
      'Collaborative Interaction Enablement':['Clinical Collaboration','General Collaboration','Presentation Services'],
      'Operational Activity & Temporal Enablement':['Workflow & Activity Coordination','Calendar Management'],
    }
    for x in bec_before:
        k=x['key'];now=e[k]
        if k not in decisions:
            # These are structural groups, not additional surviving abilities.
            decisions[k]={'classification':'superseded by reviewed capability','targets':[resolved[n] for n in replacements[x['name']]],
                          'reason':'Stage-2 grouping superseded by the reviewed BEC hierarchy; exact description, provenance, identity and previous placement retained as history.'}
        if now.get('release_scope')==SCOPE and decisions[k]['classification']=='retained unchanged':
            decisions[k]['classification']='retained but outside R1.x/R2.x'
        if decisions[k]['classification']=='retained unchanged' and now['record_state']=='current' and any(now.get(f)!=x.get(f) for f in ('tier','parent_key')):
            decisions[k]['classification']='relocated'
        if now['record_state']!='current':now['reconciliation_targets']=decisions[k].get('targets',[])
        # Exact complete pre-review responsibility, identity, semantic elaboration,
        # derivation and assertion statuses remain available for every affected BEC.
        now['pre_review_record']=copy.deepcopy(x)
    for x in e.values():
        if x['key'] in old and x['category']!='business_enabling':
            fields=('owner_key','record_state','canonical_id')
            if any(x.get(f)!=old[x['key']].get(f, 'current' if f=='record_state' else None) for f in fields):
                x['pre_review_record']=copy.deepcopy(old[x['key']])

    # Allocate context-correct IDs only for current responsibilities. Non-current
    # records retain the last allocated structural ID as a reserved history value,
    # explicitly excluded from the current hierarchy and its prefix grammar.
    allocated=set()
    def allocate(x):
        if x['key'] in allocated:return x['canonical_id']
        if x['record_state']!='current':
            allocated.add(x['key']);return x['canonical_id']
        owner=x.get('parent_key') or x.get('owner_key')
        if owner:assert e[owner]['record_state']=='current',('Current element has historical owner',x['name'])
        prefix=allocate(e[owner])+'.' if owner else ''
        if x['type'] in ('capability','enterprise_capability'):
            token=x['tier']+'-'+(x['reference_id'] if x['type']=='enterprise_capability' and x['baseline_version'] else x['local_id'])
        elif x['type']=='feature':token=x['local_id'] if x['local_id'].startswith('FEAT-') else 'FEAT-'+x['local_id']
        else:token={'function':'FN-','service':'SV-','process':'PR-'}[x['type']]+x['local_id']
        value=prefix+token
        if x.get('canonical_id') and x['canonical_id']!=value:x['id_aliases']=sorted(set(x['id_aliases']+[x['canonical_id']]))
        x['canonical_id']=value;allocated.add(x['key']);return value
    for x in e.values():allocate(x)
    # Allocations used during the bounded reconciliation remain reserved too.
    # This explicit small register makes later corrections deterministic while
    # retaining every already allocated current/historical ID as a direct alias.
    for rid,history in json.loads((BASE/'reviewed-identity-history.json').read_text()).items():
        x=next(x for x in e.values() if x['reference_id']==rid)
        aliases=set(x['id_aliases'])|set(history['id_aliases'])
        if x['record_state']!='current':
            aliases.add(x['canonical_id']);x['canonical_id']=history['canonical_id']
        else:aliases.add(history['canonical_id'])
        x['id_aliases']=sorted(aliases-{x['canonical_id']})
    for x in e.values():
        x['parent_identifier']=e[x['parent_key']]['reference_id'] if x.get('parent_key') else None
        x['owner_identifier']=e[x['owner_key']]['reference_id'] if x.get('owner_key') else None

    # Repair only references supported by explicit consolidation/distribution.
    # Original edges (including unresolved and Canonical) remain byte-for-byte.
    additional=[]
    for r in before['relationships']:
        if r['type'] not in ('enabled_by','enterprise_enabled_by','enterprise_contribution'):continue
        a,b=r['from'],r['to'];aa,bb=remap.get(a,a),remap.get(b,b)
        if (aa,bb)==(a,b) or any(e[k]['record_state']!='current' for k in (aa,bb)):continue
        candidate=dict(type=r['type'],**{'from':aa,'to':bb},status='Proposed',
                       rationale='Reference repair following explicit responsibility consolidation/decomposition; original assertion and its standing remain in the history.',
                       provenance=copy.deepcopy(prov),predecessor_relationship=copy.deepcopy(r))
        if not any(v['type']==candidate['type'] and v['from']==aa and v['to']==bb for v in m['relationships']+additional):additional.append(candidate)
    for x in e.values():
        if x['record_state']=='current' and x['category']=='business_enabling' and x.get('parent_key'):
            if not any(r['type']=='capability_decomposition' and r['from']==x['parent_key'] and r['to']==x['key'] for r in m['relationships']):
                additional.append(dict(type='capability_decomposition',**{'from':x['parent_key'],'to':x['key']},status='Proposed',
                                       rationale='Human-reviewed candidate hierarchy; structural relationship remains Proposed.',provenance=copy.deepcopy(prov)))
    m['relationships'].extend(additional)
    for x in e.values():
        for context in x.get('purpose_context_keys',[]):
            m['relationships'].append(dict(type='purpose_context',**{'from':x['key'],'to':context},status='Proposed',
                rationale='The existing purpose-specific Feature uses shared Information Product Administration framing while retaining its research, population-health or planning purpose. This does not decompose the purpose CT2 or confer analytical authority.',provenance=copy.deepcopy(prov)))
    currentness=[]
    for i,r in enumerate(m['relationships']):
        active=all(e[k]['record_state']=='current' for k in (r['from'],r['to']))
        if r['type'] in ('capability_decomposition','responsibility_parent'):
            active=active and (e[r['to']].get('parent_key') or e[r['to']].get('owner_key'))==r['from']
        currentness.append({'relationship_index':i,'record_state':'current' if active else 'preserved historical assertion',
                            'assertion_status':r['status']})
    m['relationship_currentness']=currentness
    # Existing contexts resolve to current semantic destinations or explicit pending
    # records. v0.3 reconciliation follows those decisions without erasing history.
    for c in m['catalogue_context_resolutions']:
        k=c['element'];c['element']=remap.get(k,k)
        old_context=next(t for t in before['catalogue_contexts'] if t['key']==c['context'])
        name=old_context['name'];owner_name=old[old_context['container']]['name']
        if owner_name in frames:c['element']=frames[owner_name]
        elif owner_name in single_frames:c['element']=resolved[single_frames[owner_name]]
        if name=='Device Association':c['element']=resolved['Device Utilisation Administration']
        if name=='Device Connectivity':c['element']=resolved['Device OA&M Metadata Management']
        if name=='Specialized Equipment Coordination':c['element']=resolved['Device Utilisation Administration']
        if name=='Bed State & Turnover Management':
            c['element']=resolved['Bed & Care-Place State Administration'];c['additional_elements']=[resolved['Bed & Care-Place Turnover Coordination']]
        c['rationale']='Explicit human-reviewed reconciliation; predecessor/context provenance is retained separately.'
    m['placement_assertions']=[dict(element=x['key'],parent=x.get('parent_key') or x.get('owner_key'),
       assertion_type='root' if x['tier']=='CT1' else 'capability_parent' if x.get('parent_key') else 'feature_frame' if x['type']=='feature' else 'behaviour_owner',
       tier=x['tier'],direct_status=x['placement_status'],complete_path_status=x['structural_id_status'],
       record_state=x['record_state'],established_responsibility_owner=x.get('responsibility_owner_key'),
       rationale=x.get('placement_rationale','Established responsibility retained; current and historical assertions are distinguished.')) for x in e.values()]
    structural_changes=[{'reference_id':x['reference_id'],'before':old[x['key']]['canonical_id'],'after':x['canonical_id'],
                         'aliases':x['id_aliases']} for x in e.values() if x['key'] in old and x['canonical_id']!=old[x['key']]['canonical_id']]
    ledger=[]
    for k,d in decisions.items():
        x=e[k];prior=old[k]
        ledger.append(dict(reference_id=x['reference_id'],before_name=prior['name'],after_name=x['name'],
          before_tier=prior['tier'],after_tier=x['tier'],before_parent=prior.get('parent_key'),after_parent=x.get('parent_key'),
          record_state=x['record_state'],**d,
          operations=([d['classification']]+(['renamed'] if prior['name']!=x['name'] and d['classification']!='renamed' else [])+
                      (['relocated'] if prior.get('parent_key')!=x.get('parent_key') and d['classification']!='relocated' and x['record_state']=='current' else [])),
          predecessor_keys=x.get('predecessor_keys',[])))
    new_records=[x['reference_id'] for x in e.values() if x['key'] not in old]
    m['reviewed_bec_reconciliation']={
      'authority':spec['authority'],'source':INPUT,'decisions':ledger,'new_records':new_records,
      'classification_counts':dict(collections.Counter(d['classification'] for d in ledger)),
      'operation_counts':dict(collections.Counter(op for d in ledger for op in d['operations'])),
      'structural_id_changes':structural_changes,'feature_ancestry_changes':feature_moves,'behaviour_ownership_changes':behaviour_moves,
      'scope_annotations':[{'key':x['key'],'reference_id':x['reference_id'],'name':x['name'],'annotation':x['release_scope']} for x in e.values() if x.get('release_scope')],
      'reviewed_nodes':[dict(node,element=resolved[node['name']]) for node in spec['nodes']],
      'service_context_relocations':[{'previous_name':name,'reference_id':e[old_names[name]]['reference_id'],'name':new_name,'parent':parent,'key':old_names[name]} for name,(new_name,parent) in RELOCATIONS.items()],
      'consent':{'owner':consent,'canonical_feature':'FEAT-EM-09','result':'Preserved; restriction/filter/disclosure references the existing client-associated responsibility. No duplicate consent capability introduced.'},
      'naming_conflict':hie['naming_rationale'],
      'semantic_conflicts':[{'reference_id':x['reference_id'],'name':x['name'],'reason':x['adjudication_note']} for x in e.values() if x['record_state']=='pending_adjudication'],
      'bc_ec_independent_redesign':False,
      'before_hierarchy':[{'key':x['key'],'name':x['name'],'tier':x['tier'],'parent_key':x.get('parent_key')} for x in bec_before],
    }
    # Compare the complete BC/EC element records, not just hierarchy counts.
    unchanged=[]
    for x in e.values():
        if x['category'] not in ('business','enterprise'):continue
        original=old[x['key']]
        current_record={k:v for k,v in x.items() if k!='record_state'}
        assert original==current_record,('BC/EC element independently changed',x['reference_id'])
        unchanged.append(original)
    m['reviewed_bec_reconciliation']['bc_ec_preservation']={
      'elements':len(unchanged),'exact_element_records_unchanged':True,
      'sha256':hashlib.sha256(json.dumps(unchanged,sort_keys=True).encode()).hexdigest()}
    m['standing']='Human-reviewed candidate BEC reconciliation; Proposed structural assertions; not RADS acceptance'
    m['navigation_aid']='Current governed RADS, frozen v0.1, explicitly permitted v0.2 evidence and v0.3 candidate, and the explicit human-reviewed task hierarchy. Legacy documentation is excluded.'
    m['explanatory_prose']['reviewed_bec_source']='reviewed-bec-hierarchy.json and tools/reviewed_bec.py'
    m['explanatory_prose']['reconciliation_note']='Exact previous descriptions remain in pre_review_record; the current hierarchy uses reviewed semantics. Pending and superseded records are published outside the current tree.'
    return m
