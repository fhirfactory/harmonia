#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Explicit Step 2 architectural proposals over the immutable 0.1 substrate.

This file is authored architecture data plus deterministic composition, not an
extractor that turns source headings, lexical similarity or software into authority.
All new assertions are Proposed. Run --check to compare the committed model.
"""
import collections
import copy
import hashlib
import importlib.util
import json
import re
import sys
from pathlib import Path

BASE = Path(__file__).resolve().parents[1]
V01 = BASE.parent
ROOT = V01.parents[2]
spec = importlib.util.spec_from_file_location('v01', V01 / 'tools/render.py')
v01 = importlib.util.module_from_spec(spec)
spec.loader.exec_module(v01)
ORIGINAL = copy.deepcopy(v01.M)
M = copy.deepcopy(ORIGINAL)
M.update(version='0.2-draft', standing='Proposed Complete Architecture; review artefact, not RADS adjudication')
E = {e['key']: e for e in M['elements']}
R = M['relationships']
DECISIONS = []


def slug(name):
    return re.sub(r'[^a-z0-9]+', '-', name.lower()).strip('-')


def evidence(path, phrase):
    """Exact current-corpus locator; fail if the authored evidence no longer exists."""
    lines = (ROOT / path).read_text().splitlines()
    hits = [i for i, line in enumerate(lines, 1) if phrase.lower() in line.lower()]
    assert hits, (path, phrase)
    return {'path': path, 'line': hits[0], 'text': lines[hits[0] - 1],
            'standing': 'Current corpus evidence; does not promote the derived assertion'}


def src(path, phrase):
    return evidence('docs/markdown/' + path, phrase)


AX = src('governance/architectural-axioms.md', 'AX-14 —')
META = src('03-business-architecture/metamodel/business-architecture-metamodel.md', 'fixed three-tier')
INFO = src('03-business-architecture/information-responsibility/information-responsibility.md', 'Non-Transfer of Ownership')
TWIN = src('05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md', 'Twin-relative execution')
CONTEXT = src('05-information-systems-architecture/context-and-persistence-management.md', 'versioned temporal values')
BINARY = src('05-information-systems-architecture/sylloge-binary-content-management.md', 'Application responsibility')
DATASET = src('05-information-systems-architecture/calliope-models-and-datasets.md', '## Calliope-DataSets')
AUTH = src('05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md', 'Identity.Authentication is a')
AUDIT = src('governance/architectural-axioms.md', 'Kleio mandatory authoritative audit capture')
WORK = src('03-business-architecture/behaviours/05-intrinsic-enablement.md', '### 2.9 Workflow')
CAPACITY = src('03-business-architecture/information-responsibility/information-responsibility.md', 'Context-specific Capacity Management')
ASSURANCE = src('03-business-architecture/behaviours/health-service-assurance.md', 'exactly five Business Functions')
STRATEGY = 'docs/markdown/02-strategy/capabilities/business-capabilities.md'
V03PATH = 'docs/markdown/scratchpad/capability-reference-candidate/Harmonia-Capability-Reference-Candidate-v0.3.tex'


def provenance(reason, *citations):
    return {'derivation': reason, 'evidence': list(citations),
            'method': 'AI architectural synthesis under the explicitly authorised Step 2 task',
            'authority': 'Proposed only; cited source standing is not inherited'}


for e in E.values():
    e['baseline_version'] = '0.1'
    e['canonical_name'] = e['name']
    e['name_status'] = 'Canonical' if e['architectural_status'] == 'Canonical' else 'Candidate'
    e['id_aliases'] = e.get('id_aliases', [])
    e['name_aliases'] = e.get('name_aliases', [])
    e['placement_status'] = 'Established' if e['ancestry_status'] == 'established' else 'Proposed'
    e['responsibility_owner_key'] = e.get('owner_key')
    # Rendered definition and qualifications are retained byte for byte. Changes
    # to hierarchy never trigger re-extraction under a new name or tier.
    e['governed_definition_latex'] = v01.source_excerpt(e)
    e['baseline_description'] = copy.deepcopy(v01.PROSE.get(e['key'], {}).get('sections', {}))
    e['baseline_unresolved'] = list(e['unresolved'])
    e['baseline_placement'] = {k: e.get(k) for k in ('tier', 'parent_key', 'owner_key', 'canonical_id')}


def find(name, category=None, typ=None):
    es = [e for e in E.values() if name in (e['name'], e.get('canonical_name'), e['key'], e['reference_id'])
          and (not category or e['category'] == category) and (not typ or e['type'] == typ)]
    assert len(es) == 1, ('Ambiguous/missing identity', name, category, typ)
    return es[0]


def proposed(name, category, tier, parent, definition, overview, boundary, citations, intent=None, confidence='High'):
    kind = {'business': 'BC', 'business_enabling': 'BEC', 'enterprise': 'EC'}[category]
    key = 'proposal:' + kind.lower() + '-' + slug(name)
    assert key not in E
    typ = 'enterprise_capability' if category == 'enterprise' else 'capability'
    e = dict(key=key, reference_id='HCM-' + kind + '-' + slug(name),
             latex_label='cap:' + kind.lower() + '-' + slug(name), name=name,
             canonical_name=None, name_status='Proposed', type=typ, category=category,
             tier=tier, parent_key=parent, owner_key=None, architectural_status='Proposed',
             canonical_id=None, local_id=kind + '-' + slug(name).upper(),
             ancestry_status='proposed', placement_status='Proposed', sources=[], unresolved=[],
             views=[], id_aliases=[], name_aliases=[], baseline_version=None,
             definition=definition, overview=overview, intent=intent or overview,
             boundary=boundary, confidence=confidence,
             provenance=provenance('Inferred from bounded responsibilities and their semantic decomposition', *citations),
             placement_rationale=overview,
             relevance='Proposed materiality; no inherited Core classification')
    E[key] = e
    M['elements'].append(e)
    return e


def cap(name, category='business_enabling'):
    return find(name, category)


def place(e, tier, parent, rationale, confidence='High', alternative=None):
    e.update(tier=tier, parent_key=parent, ancestry_status='proposed', placement_status='Proposed',
             placement_rationale=rationale, confidence=confidence)
    if alternative:
        e['alternative_considered'] = alternative
    e['placement_provenance'] = provenance('Responsibility-preserving proposed decomposition', META, AX)
    e['unresolved'] = [u for u in e['unresolved'] if u not in
                       ('tier', 'ancestry', 'root_status', 'canonical_id', 'code_allocation', 'contextual_view')]


# CT1s express cohesive abilities, rather than renaming the five contextual views.
ROOTS = [
 ('Healthcare Entity Information Enablement',
  'Maintain the identity, descriptive structure and qualified relationships of the participants, places, services, products and devices referenced in healthcare activity.',
  'A care transaction needs to identify its subject, provider, service and place before it can be interpreted safely. This frame brings together those relatively enduring descriptions while keeping each kind of entity and relationship accountable within its own boundary.',
  'Registry information does not establish clinical privilege, service readiness, occupancy or originating national identity authority.'),
 ('Care Administration Information Enablement',
  'Receive, correlate and communicate the administrative requests, episodes, fulfilment records and documents through which care is arranged and recorded.',
  'Referrals, bookings, encounters, orders and clinical documents have different progression and acknowledgement meanings. Their common ability is to make care administration intelligible across organisations without making those transactions interchangeable.',
  'Information administration does not perform the clinical act, own host booking engines or infer Transfer of Care from referral acceptance.'),
 ('Care Context Integration Enablement',
  'Compose governed information and coordination behaviour for materially different care settings, continuity arrangements and specialist clinical purposes.',
  'Emergency presentation, inpatient rounds and outreach visits need different contextual assemblies of the same underlying information. This frame retains those established setting-specific responsibilities as compositions rather than duplicate records or clinical execution authorities.',
  'Setting, care mode and specialty are overlapping contextual dimensions. These children are integration responsibilities, not a mutually exclusive taxonomy of patients or clinical care.'),
 ('Care Operations Coordination Enablement',
  'Represent and coordinate care-setting flow, operational resources, workforce participation, movement and service-delivery preparedness.',
  'Operational coordination connects available resources with actual care-setting constraints and observable work progression. Bed state, local capacity, aggregate capacity, transport and discharge remain separate abilities even when they exchange the same milestones.',
  'Coordination owns only explicitly assigned operational activity. It does not originate clinical clearance, determine clinical work or take workforce and resource governance from their sources.'),
 ('Governed Health Information Enablement',
  'Preserve, find, exchange, qualify and protect health information and the governed definitions needed to interpret it.',
  'This frame makes a managed record usable over time and across authorised purposes. It separates clinical synthesis, binary-content association, external exchange, semantic definitions, privacy and evidence so that none becomes an undifferentiated information utility.',
  'Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities.'),
 ('Human Collaboration & Work Enablement',
  'Enable contextual human interaction, collaboration and the definition and progression of governed operational activity.',
  'People discuss information, inspect it, make authorised decisions and undertake work. Those interactions need reusable coordination and temporal context, while entity-relative progression and activity design retain distinct semantics from message exchange and screen rendering.',
  'Conversation is not a clinical record, presentation is not authority, and generic work coordination does not determine clinical work or own its outcome.'),
 ('Independent Service Assurance Enablement',
  'Specify how governed service behaviour is assured and independently establish what applicable evidence demonstrates.',
  'The frame separates design and criteria stewardship from the evaluation activity that produces findings. It preserves the established three assurance capabilities and five Functions without turning observation or operational health into an assurance conclusion.',
  'Assurance cannot manage, remediate or approve its subject merely because it observes it. Clinical adequacy, financial governance and source-information authority remain outside this mandate.'),
]
for name, definition, overview, boundary in ROOTS:
    proposed(name, 'business_enabling', 'CT1', None, definition, overview, boundary,
             [META, INFO, ASSURANCE if name.startswith('Independent') else AX])


def group(name, root, children, definition, boundary, confidence='High'):
    parent = proposed(name, 'business_enabling', 'CT2', cap(root)['key'], definition,
                      definition + ' Its constituent abilities distinguish ' + '; '.join(children) + '.',
                      boundary, [META, INFO], confidence=confidence)
    for child in children:
        place(cap(child), 'CT3', parent['key'],
              'The responsibility concerns ' + definition[0].lower() + definition[1:] +
              ' It is the finest capability frame required for the established behaviour; the older contextual heading is not another capability tier.',
              confidence)
    return parent


ENTITY_ROOT = cap('Healthcare Entity Information Enablement')['key']
for name in ['Client Administration', 'Provider Administration', 'Organisation Administration', 'Location Administration',
             'Health Service Administration', 'Health Product Administration', 'Clinical Device Administration']:
    place(cap(name), 'CT2', ENTITY_ROOT,
          'This established responsibility contains materially different identity, structure, relationship or definition abilities; those become CT3s rather than treating the whole registry as a leaf.')
for name in ['Person Identity', 'Healthcare Subject', 'Client Relationship', 'Client Privacy']:
    place(cap(name), 'CT3', cap('Client Administration')['key'],
          'Identity resolution, subject context, support relationships and individual privacy are distinct constituent abilities of client representation.')

# For entity contexts only, promote useful descriptions to Proposed CT3s. Elsewhere
# canonical responsibilities themselves are CT3s, so their contextual descriptions
# become navigation/name history instead of mechanically adding another tier.
CONTEXT_BOUNDARIES = {
 'Practitioner': 'Professional registration and profile representation do not grant clinical or operational privilege.',
 'Practitioner Role': 'Role, specialty, capacity and endpoint information supply context; they do not constitute a privilege determination.',
 'Provider Relationship': 'Affiliation records do not transfer the participating organisations or practitioner identities to this capability.',
 'Provider Engagement': 'Records externally granted service terms and privileges; it does not independently grant those privileges.',
 'Healthcare Organisation': 'Maintains verified legal and operational identity information without corporate management authority.',
 'Organisational Structure': 'Structural relationships do not acquire ownership of every related organisation or service.',
 'Organisation Contact': 'Contact validity is distinct from clinical recipient eligibility and policy permission.',
 'Healthcare Location': 'Location description does not establish occupancy, cleanliness, service capacity or service readiness.',
 'Location Structure': 'Physical containment differs from organisational structure and operational responsibility.',
 'Care-Place Definition': 'Describes a place and its capabilities; operational bed state and readiness remain separately managed.',
 'Healthcare Service Definition': 'Defines what is offered; offering existence does not establish current availability or collective readiness.',
 'Service Provision Relationship': 'Owns mapping associations, retaining the owners of each participating entity.',
 'Service Availability Definition': 'Represents operating windows and exceptions; it does not establish staffing, capacity or readiness.',
 'Service Eligibility Definition': 'Represents governed eligibility criteria; it does not make clinical acceptance or triage decisions.',
 'Health Product Definition': 'Reference representation is distinct from procurement, manufacturing and prescribing.',
 'Product Formulary & Catalogue': 'Represents externally approved formularies; it does not acquire formulary approval or dispensing authority.',
 'Product Batch & Serial Tracking': 'Retains product trace context without running stock control or supply-chain operations.',
 'Device Definition': 'Describes device types and capabilities without taking regulatory approval or clinical-use authority.',
 'Device Instance': 'Identifies physical hardware; serial tracking is distinct from maintenance and live observation content.',
 'Device Association': 'Represents qualified device-to-subject/place association without owning those subjects or observations.',
 'Device Connectivity': 'Represents communication bindings; it does not acquire authentication credential issuance or telemetry interpretation.'
}
CONTEXT_MAP = {}
CONTEXT_OVERVIEWS = {
 'Practitioner': 'Practitioner identity and professional profile information let care participants be recognised across organisations. Registration and profile standing remain attributed to the issuing authority, while discovery makes that described person usable in care integration.',
 'Practitioner Role': 'The same practitioner may act in different specialties, organisations and operational capacities. Role-specific associations and communication endpoints make that participation intelligible without converting a directory role into permission or professional privilege.',
 'Provider Relationship': 'Effective affiliations explain which practitioner or team participates with which organisation. These relationships support collaboration and routing while each participating identity keeps its own responsibility owner.',
 'Provider Engagement': 'Engagement information represents the terms and externally granted practice privileges under which a provider participates. This differs from who the practitioner is and from whether a particular operation is authorised now.',
 'Healthcare Organisation': 'An identifiable legal or operational healthcare organisation needs verified identifiers, a profile and published standing. This description anchors service and affiliation relationships without managing the organisation itself.',
 'Organisational Structure': 'Departments, divisions and regional networks need qualified structural relationships that can be traversed and understood over time. Structural membership is distinct from physical location and from authority to manage another organisation.',
 'Organisation Contact': 'Verified facility contact channels make an organisation reachable for appropriate administrative and clinical communication. The contact record says how it may be reached; recipient qualification and disclosure policy govern a particular use.',
 'Healthcare Location': 'Addresses, geospatial context and facility identity establish which physical place an information or activity concern refers to. Describing a location remains separate from deciding what services can operate there now.',
 'Location Structure': 'Campus, building, floor, ward and room relationships explain physical containment. The structure provides navigation and context without determining organisational reporting lines or current operational capacity.',
 'Care-Place Definition': 'A particular bed, bay, treatment chair or procedural place has stable physical characteristics relevant to care. Those descriptive capabilities remain separate from occupancy, cleaning state and suitability for a current patient.',
 'Healthcare Service Definition': 'An offered service has an identifiable clinical or administrative purpose, specialty and published intake description. That definition is distinct from the provider/location binding through which the service can be delivered.',
 'Service Provision Relationship': 'Service provision joins a named offering to the organisations, places and practitioner roles through which it is provided. The mapping is the owned concern; the identity and meaning of each mapped participant remain with its established capability.',
 'Service Availability Definition': 'Regular hours, on-call windows and exceptional closures describe when an offering is nominally available. They provide an input to readiness and scheduling without proving that all required dependencies are ready.',
 'Service Eligibility Definition': 'Published catchment, referral and clinical eligibility conditions explain the intended audience and access constraints of an offering. Their representation supports responsible assessment without deciding acceptance or clinical priority.',
 'Health Product Definition': 'Medication, blood-product, implant and consumable descriptions provide reference context for care information. Product identity and classification are retained without turning this reference responsibility into procurement or prescribing.',
 'Product Formulary & Catalogue': 'An approved facility formulary or product catalogue provides a qualified set of products relevant to local care. Representing its membership and source standing remains distinct from deciding formulary approval and from generic terminology governance.',
 'Product Batch & Serial Tracking': 'Batch, lot and serial references make a product instance or supply-chain association traceable when care information needs it. This reference scope does not run inventory, recall operations or the supply chain.',
 'Device Definition': 'A device type has a model, regulatory description and stated capabilities used to interpret equipment context. This type-level responsibility differs from identifying the actual physical instance that produced information.',
 'Device Instance': 'An identifiable physical device carries UDI and serial context through its use. The instance record supports association and resolution without originating device observations or making maintenance decisions.',
 'Device Association': 'A device can be associated with different patients and care places at different times. Qualified association and its lineage allow observations to be interpreted against the applicable binding rather than the current assignment alone.',
 'Device Connectivity': 'A device communication binding identifies the protocol and endpoint through which its feed can participate. Connectivity metadata supports routing without owning authentication issuance, payload semantics or physiological interpretation.'
}
for c in M['catalogue_contexts']:
    if c['element']:
        CONTEXT_MAP[c['key']] = c['element']
    elif c['view'] == 'view:entity-management':
        lines = v01.LINES['enabling']; a = c['sources'][0]['line']
        text = ' '.join(lines[a:a + 3])
        match = re.search(r'\*Definition\*:\s*(.*?)(?=\s+- `FEAT|$)', text)
        definition = match[1].strip() if match else re.sub(r'^.*?\*\*[^*]+\*\*[^:]*:\s*', '', lines[a-1])
        definition = re.sub(r'\*\([^)]*\)\*\.?', '', definition).strip()
        if len(definition) < 20:
            definition = CONTEXT_BOUNDARIES[c['name']].split(';')[0] + '.'
        e = proposed(c['name'], 'business_enabling', 'CT3', c['container'], definition,
                     CONTEXT_OVERVIEWS[c['name']],
                     CONTEXT_BOUNDARIES[c['name']],
                     [src('02-strategy/capabilities/business-enabling-capabilities.md', lines[a-1]), INFO])
        CONTEXT_MAP[c['key']] = e['key']
    else:
        CONTEXT_MAP[c['key']] = c['container']

for e in list(E.values()):
    if e['type'] == 'feature' and e.get('catalogue_context') in CONTEXT_MAP:
        frame = CONTEXT_MAP[e['catalogue_context']]
        if frame != e['owner_key']:
            e['owner_key'] = frame
            e['placement_status'] = 'Proposed'
            e['placement_rationale'] = 'The named contextual responsibility provides a semantically narrower CT3 frame; the established broader capability retains responsibility through ancestry.'
            e['placement_provenance'] = provenance('Repositioned within established responsibility', META, INFO)
        e['ancestry_status'] = 'proposed'
        e['placement_status'] = 'Proposed'
        e['unresolved'] = [u for u in e['unresolved'] if u not in ('ancestry', 'canonical_id')]

ADMIN = 'Care Administration Information Enablement'
group('Care Access Transaction Enablement', ADMIN,
      ['Client-Centred Administration', 'Referral Administration', 'Scheduling Administration'],
      'Coordinate the intake, care-request disposition and appointment information through which a person accesses care.',
      'A referral requests care responsibility; an appointment concerns a booking. Neither establishes care delivery or host booking authority.')
group('Care Fulfilment Administration Enablement', ADMIN,
      ['Episode & Encounter Administration', 'Order Administration', 'Diagnostic Administration', 'Medication Administration', 'Procedure Administration'],
      'Maintain distinguishable care-episode, bounded fulfilment and delivered-activity information across performing systems.',
      'Encounter progression, order outcome, diagnostic interpretation, medication events and procedure documentation retain different source authorities.')
group('Care Documentation & Communication Enablement', ADMIN,
      ['Clinical Record Administration', 'Care Plan Administration', 'Clinical Communication Administration'],
      'Administer the lifecycle, shared-plan context and directed communication of care documentation.',
      'This frame does not absorb longitudinal synthesis, collaboration discourse, originating clinical decisions or generic transport.')
group('Care Financial Information Enablement', ADMIN,
      ['Funding & Coverage Administration', 'Clinical Charging & Claims Administration'],
      'Represent coverage context and convey care-activity evidence to responsible funding and charging systems.',
      'Adjacent information participation does not provide financial governance, coverage adjudication or a billing platform.', 'Medium')

CARE = 'Care Context Integration Enablement'
group('Longitudinal & Community Care Integration', CARE,
      ['Primary Care', 'Community Care', 'Outreach Care', 'Remote & Virtual Care', 'Preventive Care'],
      'Support continuity, remote participation and preventive follow-up through appropriately assembled and exchanged care context.',
      'Longitudinal purpose does not make these care contexts exclusive. Outreach location and remote mode remain distinct from source clinical responsibility.', 'Medium')
group('Facility & Episodic Care Integration', CARE,
      ['Acute Care', 'Emergency Care', 'Inpatient Care', 'Ambulatory Care', 'Surgical & Procedural Care'],
      'Provide the context assemblies and boundary integration needed for facility presentations and bounded care episodes.',
      'Acute, emergency and inpatient perspectives overlap; each capability owns its named integration purpose rather than a population or clinical workflow.', 'Medium')
group('Specialist Clinical Context Integration', CARE,
      ['Clinical Assessment', 'Diagnostic Services', 'Therapeutic Services', 'Medication Therapy', 'Allied Health', 'Rehabilitation'],
      'Integrate specialist assessment, investigation and therapeutic information with longitudinal and episode context.',
      'A clinical specialty is evidence for integration purpose, not authority to invent a specialist engine, clinical decision or duplicated general capture function.', 'Medium')

OPS = 'Care Operations Coordination Enablement'
group('Care Setting Operations Enablement', OPS,
      ['Clinic & Practice Operations', 'Ward Operations', 'Theatre Operations', 'Emergency Department Operations', 'Outpatient Operations'],
      'Coordinate the local flow, session state and capacity semantics of a care-delivery setting.',
      'Local capacity is established here; aggregate telemetry, bed state and equipment state cannot substitute for it.')
group('Care Resource & Capacity Coordination', OPS,
      ['Bed & Care-Place Management', 'Clinical Resource Management', 'Service Capacity Management'],
      'Relate operational care-place and equipment state to the coordinated view of service demand and capacity.',
      'Bed/equipment availability is a contribution to capacity, not its definition. Higher-order capacity consumes local capacity without taking its ownership.')
group('Operational Workforce Coordination', OPS,
      ['Clinical Workforce Management', 'Clinical Rostering', 'Mobile Staff Management', 'On-Call Management', 'Work Allocation & Dispatch'],
      'Make workforce availability, coverage and eligible operational work participation usable in coordination.',
      'Source rosters remain separately authoritative; operational dispatch does not acquire clinical-work allocation authority.')
group('Workforce Qualification & Development Enablement', OPS,
      ['Clinical Training & Education', 'Clinical Competency Management', 'Clinical Qualification Management', 'Clinical Supervision', 'Credential Management'],
      'Represent learning, competence, professional-standing checks and separately governed operational access qualifications.',
      'Clinical qualifications and physical badges remain distinct CT3 responsibilities. Representation or checking does not grant privilege or become an authentication provider.', 'Medium')
group('Care Movement & Transition Coordination', OPS,
      ['Patient Transport', 'Clinical Logistics Coordination', 'Discharge Management'],
      'Coordinate movement of people/materials and operational departure readiness across care boundaries.',
      'Physical movement, clinical handover, discharge clinical authorisation and care-responsibility transition remain distinct.')

GOV = 'Governed Health Information Enablement'
group('Clinical Information Stewardship Enablement', GOV, ['Patient Clinical Record'],
      'Maintain governed longitudinal synthesis and the associations needed to use its constituent clinical information safely.',
      'The synthesis preserves what Harmonia received or managed without becoming the originator of the clinical facts.')
group('Health Information Connectivity Enablement', GOV,
      ['Health Information Exchange', 'Health Information Access', 'Health Information Communication'],
      'Make governed health information discoverable and exchangeable under explicit internal and external contracts.',
      'Query execution, transaction coordination and boundary protocol mediation are separate responsibilities; none transfers content ownership.')
group('Semantic & Reference Knowledge Enablement', GOV,
      ['Clinical Knowledge Services', 'Information Design Governance'],
      'Govern the definitions, terminology and reference content that make healthcare information consistently interpretable.',
      'Definition semantics, mutable approved content and runtime policy decisions retain distinct ownership.')
group('Information Protection & Accountability Enablement', GOV, ['Health Information Control'],
      'Apply information-use policy and preserve the accountability context needed for governed health-information activity.',
      'Patient directives remain with Client Privacy; runtime authorisation does not become external authentication or clinical authority.')
group('Secondary Health Information Use Enablement', GOV, [],
      'Provide existing health information for authorised research, required population notification and service planning purposes.',
      'Each purpose has different release criteria. This grouping does not originate research approval, public-health decisions or planning authority.', 'Medium')

HUMAN = 'Human Collaboration & Work Enablement'
group('Collaborative Interaction Enablement', HUMAN,
      ['Clinical Collaboration', 'General Collaboration', 'Presentation Services'],
      'Support contextual discussion and human interaction with governed information and activity.',
      'Collaboration control, collaboration operations and general presentation remain distinct; a discussion or screen is not a new clinical fact.')
group('Operational Activity & Temporal Enablement', HUMAN,
      ['Workflow & Activity Coordination', 'Calendar Management'],
      'Progress governed operational work in identifiable activity and temporal contexts.',
      'Work Order is human doing, To Do is human review/update/approval and Task is synthetic work. Projection does not own authoritative bookings or rosters.')

ASSURE_ROOT = 'Independent Service Assurance Enablement'
group('Assurance Specification Enablement', ASSURE_ROOT,
      ['Assurance Design', 'Assurance Criteria Management'],
      'Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.',
      'Modelling and criteria stewardship do not approve governing requirements or perform independent conclusion-making.')
group('Independent Assurance Evaluation Enablement', ASSURE_ROOT, ['Governed Assurance'],
      'Associate trustworthy evidence, assess it and adjudicate independently reportable findings about governed service behaviour.',
      'The subject can supply evidence but cannot control its conclusion. Evidence insufficiency remains distinct from execution uncertainty and clinical judgement.')

# A proposed display rename resolves the current-model collision; the established
# name and exact definition remain searchable and visible in the record.
hie = cap('Health Information Exchange')
hie.update(name='Health Information Exchange Enablement', name_status='Proposed',
           name_aliases=sorted(set(hie['name_aliases'] + ['Health Information Exchange'])),
           naming_rationale='Distinguishes reusable Harmonia exchange enablement from the organisational BC of the same governed name; identity and responsibility remain Canonical.')

# New responsibilities follow information/work semantics, not component names.
EXTRA_BEC = [
 ('Associated Clinical Binary Content Enablement', 'Clinical Information Stewardship Enablement',
  'Associate, resolve, retrieve and assemble binary content needed to use managed clinical or operational information.',
  'Logical information may refer to one or several payloads held internally or by an external custodian. This capability keeps association and custody knowledge explicit so that content can be obtained under the governing information contract.',
  'Association is not physical custody. Persistence authority, source clinical meaning and external repository authority remain separate.', [BINARY, INFO]),
 ('Information Authority Representation', 'Clinical Information Stewardship Enablement',
  'Represent the source, credibility and qualified authority standing of competing health-information assertions.',
  'A persisted assertion may be authoritative, informational or anecdotal. This ability carries that standing into interpretation without treating transport reliability, freshness or successful commit as proof of credibility.',
  'Classification does not originate clinical facts or universally adjudicate conflicts. Policy determines how qualified assertions are used.', [src('governance/architectural-axioms.md', 'AX-06 ---'), INFO]),
 ('Governed Reference Dataset Administration', 'Semantic & Reference Knowledge Enablement',
  'Manage the approved runtime content and revision standing of healthcare reference datasets.',
  'Value-set membership, mapping content and governed nomenclature may change independently of the structural definition that gives them meaning. Updates need identifiable review and effective standing without redefining model libraries as mutable data.',
  'Does not own external ontology authority, execute authorisation policy or establish durable truth independently of the normal persistence boundary.', [DATASET, INFO]),
 ('Significant Activity Audit Capture', 'Information Protection & Accountability Enablement',
  'Capture required security- and information-significant activity as governed, attributable audit evidence.',
  'Mandatory evidence capture has its own policy-governed platform authority and must remain possible when the audited identity is denied an action. The captured fact is distinguished from debug output and the identity whose activity is recorded.',
  'The specific intrinsic authority for mandatory audit capture is not generalised to other activities. Capture is not an assurance finding and cannot expose unmasked PHI in diagnostic logs.', [AUDIT, INFO]),
 ('Information Lineage Stewardship', 'Information Protection & Accountability Enablement',
  'Maintain source attribution and meaningful transformation lineage for health information managed across care boundaries.',
  'Recipients need to know which assertions were received, transformed or superseded and why. This ability preserves those meaningful relationships without making every retry or active-state reconstruction permanent provenance.',
  'Lineage does not grant originating authority, decide clinical correctness or replace activity audit and independent assurance.', [src('governance/architectural-axioms.md', 'AX-08 ---'), INFO]),
 ('Authorised Research Information Provision', 'Secondary Health Information Use Enablement',
  'Assemble and release existing health information for an explicitly authorised research purpose under applicable use constraints.',
  'Research support must retain the purpose, applicable approvals and release qualification of supplied information. It reuses governed retrieval and privacy controls rather than claiming a clinical-trial or research-governance platform.',
  'Does not grant ethics approval, recruit trial participants, manage trials or become the authority for derived research conclusions.', [evidence(STRATEGY, 'Governed Research Information Provision'), INFO]),
 ('Population Health Notification Enablement', 'Secondary Health Information Use Enablement',
  'Prepare, convey and track required population-health notifications from applicable governed source information.',
  'A reportable event and the obligation to notify are different from sending a message. This capability preserves the established obligation and observes the notification outcome under its contract without deciding public-health response.',
  'Does not determine population-health findings, create statutory obligations or equate transport delivery with discharge of notification duty.', [evidence(STRATEGY, 'Notifiable Health Event Management'), INFO]),
 ('Service Planning Information Provision', 'Secondary Health Information Use Enablement',
  'Supply qualified demand, utilisation and capacity evidence to responsible health-service planning activities.',
  'Planners need comparable time- and service-qualified observations from multiple operating settings. This ability prepares those projections while leaving the interpretation of required future capacity and distribution with the planning authority.',
  'Observed utilisation, operational capacity and planned capacity are distinct; information provision does not commission services or allocate resources.', [evidence(STRATEGY, 'Health Service Demand Assessment'), CAPACITY]),
 ('Governance Requirement Publication Support', 'Information Protection & Accountability Enablement',
  'Represent and publish versioned governing requirements for use by authorised operational and assurance consumers.',
  'Consumers need an identifiable statement of which rule applies and who established it. This proposal fills BC-17 information support without giving the platform authority to make legislation, approve governance requirements or reinterpret their force.',
  'Governance approval remains externally mandated; operational policy, assurance criteria and requirement authority are not interchangeable.', [evidence(STRATEGY, '#### 17. Health Service Governance'), ASSURANCE]),
 ('Entity-Specific Operational Coordination', 'Operational Activity & Temporal Enablement',
  'Coordinate governed operational progression relative to one represented entity, including interested behaviour participation and controlled recovery.',
  'Activity concerning the same patient, practitioner or bed needs a coherent entity-relative frame. Interest fan-out, serial progression, fresh information context and uncertain effects remain distinguishable from generic multi-work-unit progression.',
  'Entity coordination does not own all entity information, determine clinical work or become the source of clinical decisions. No Twin component allocation follows from this proposal.', [TWIN, src('governance/architectural-axioms.md', 'AX-16 —')]),
 ('Governed Operational Activity Design', 'Operational Activity & Temporal Enablement',
  'Define the information context, expected outcomes, continuation conditions and housekeeping of governed operational activity.',
  'An activity needs a fulfilment definition even when only one bounded execution unit is involved. This capability specifies its semantic envelope while execution and event delivery remain separate responsibilities.',
  'An activity definition is neither an orchestrator nor a choreographer. It does not choose clinical care or dynamically invent execution logic.', [CONTEXT, src('05-information-systems-architecture/pathways-journeys-and-handoff.md', 'Praxis')]),
 ('Service Offering Readiness Coordination', 'Care Resource & Capacity Coordination',
  'Assemble the dependency evidence needed to determine and communicate whether a defined service offering is operationally ready in a particular context.',
  'Collective readiness depends on suitable workforce, spaces, equipment, materials and supporting services. This proposal evaluates their joint availability for an offering without treating one available bed, an operating-hours declaration or aggregate capacity as service readiness.',
  'Contributing capabilities retain source readiness and resource management. Clinical go/no-go decisions and service governance are not granted by this information-based coordination.', [evidence(STRATEGY, 'OI-BC-002'), CAPACITY]),
]
for name, parent, definition, overview, boundary, citations in EXTRA_BEC:
    proposed(name, 'business_enabling', 'CT3', cap(parent)['key'], definition, overview, boundary, citations,
             confidence='Low' if name in ('Governance Requirement Publication Support', 'Service Offering Readiness Coordination') else 'Medium')

# Five v0.3 refinements survive as proposed BCs; clinical authority remains human.
for name, parent, definition, boundary in [
 ('Clinical Finding Interpretation', 'Clinical Determination', 'Interpret observed clinical findings and their significance for the individual care concern.', 'Does not itself establish a diagnosis, prescribe intervention or confer this clinical responsibility on Harmonia.'),
 ('Diagnosis & Problem Determination', 'Clinical Determination', 'Establish and qualify the clinical problems or diagnoses applicable to an individual.', 'Clinical determination remains with the mandated professional; a coded record is not evidence that Harmonia made the diagnosis.'),
 ('Health Risk & Prognostic Determination', 'Clinical Determination', 'Establish clinically meaningful risk and expected-course judgements for an individual.', 'Risk/prognostic judgement is distinct from operational urgency, source-information authority and platform assurance.'),
 ('Care Goal Definition', 'Individual Care Planning', 'Establish the intended outcomes and priorities against which an individual care response is planned.', 'Goal definition remains distinct from selecting an intervention and from recording an externally agreed care plan.'),
 ('Care Response Planning', 'Individual Care Planning', 'Determine the intended clinical response and its sequencing in relation to agreed care goals.', 'Planning is distinct from intervention delivery and from the operational definition of a synthetic activity.'),
 ('Medication Compounding & Preparation', 'Medication & Pharmacy Services', 'Prepare or compound a clinically required medication formulation under the applicable professional and product-governance mandate.', 'Preparation differs from dispensing and administration; this proposal assigns no compounding activity to Harmonia.')
]:
    proposed(name, 'business', 'CT3', cap(parent, 'business')['key'], definition,
             definition + ' The proposed decomposition separates this outcome from the other responsibilities within ' + parent + '.',
             boundary, [evidence(STRATEGY, 'Clinical Determination and Individual Care Planning') if parent in ('Clinical Determination', 'Individual Care Planning') else evidence(STRATEGY, 'OI-BC-001')], confidence='Medium')

readiness_parent = proposed('Service Delivery Preparedness Management', 'business', 'CT2', cap('Care Access & Coordination', 'business')['key'],
    'Establish whether service offerings can be delivered in their intended operating contexts through the combined standing of their dependencies.',
    'Preparedness asks whether the service as a whole can operate, distinct from scheduling capacity or readiness of a single place. This is a proposed missing business frame for the already identified readiness concern.',
    'Does not own the dependencies, their management or clinical execution.', [evidence(STRATEGY, 'OI-BC-002')], confidence='Low')
for name, parent, rationale, alternative in [
 ('Health Service Operational Readiness', readiness_parent['key'], 'Collective readiness is an access prerequisite and has a coherent proposed preparedness frame; it is not reduced to capacity, resource state or service alignment.', 'Health Service Direction & Stewardship is plausible for standing site/service readiness; access-oriented placement is chosen to retain the deliverability question.'),
 ('Clinical Device Maintenance Management', cap('Clinical Technology Management', 'business')['key'], 'Maintenance/restoration is materially distinct from overall lifecycle disposition and monitoring; include the Candidate concern as a proposed eighth CT3.', 'The seven-child adjudicated set omits it. Membership remains Candidate rather than silently amending that approved set.'),
 ('Health Information Sharing Programme Management', cap('Health Information Exchange', 'business')['key'], 'Programme-level coordination supplies a plausible frame for the preserved name without replacing the established exchange abilities.', 'Framework oversight alone is too narrow; programme authority and exact boundary require review.')
]:
    e = cap(name, 'business')
    place(e, 'CT3', parent, rationale, 'Low', alternative)
    e['proposal_overview'] = rationale
    e['proposal_definition'] = {
        'Health Service Operational Readiness': 'Establish collective operational readiness of a service offering in a site or session context from its applicable dependencies.',
        'Clinical Device Maintenance Management': 'Maintain or restore acceptable device condition through testing, calibration, inspection, maintenance and repair.',
        'Health Information Sharing Programme Management': 'Coordinate the governed scope, participants and change context of a health-information-sharing programme.'
    }[name]
    e['proposal_boundary'] = 'The element remains Candidate. The definition/placement elaboration is Proposed and does not adjudicate the unresolved RAD question or transfer source-domain authority.'

# Enterprise hierarchy: no parallel hierarchy of look-alike responsibilities.
EC_ROOTS = [
 ('Governed Information Operations', ['EC-01', 'EC-02', 'EC-03', 'EC-04', 'EC-05', 'EC-13'],
  'Maintain identifiable, meaningful, context-qualified and retrievable information through valid state progression.',
  'Entity integrity, context, lifecycle, information maintenance, discovery and semantic conformance are constituent reusable abilities; none is an implementation subsystem.',
  'Information management and semantic governance do not grant originating domain authority or erase active/durable state separation.'),
 ('Coordinated Activity & Interaction', ['EC-08', 'EC-09', 'EC-10', 'EC-11'],
  'Enable governed interaction, event participation, boundary exchange and accountable activity progression.',
  'Requests, events, work and human interaction are related ways to cause or observe change, with distinct contracts and progression responsibilities.',
  'Exchange is not execution, notification is not outcome, and interaction is not clinical authority.'),
 ('Trust, Accountability & Assurance', ['EC-06', 'EC-07', 'EC-12', 'EC-14'],
  'Make governed activity permissible, attributable, operationally dependable and independently assessable.',
  'Policy, evidence, resilience and independent assurance contribute to confidence through different responsibilities. Keeping them as peers prevents technical success or recorded evidence from becoming a business assurance conclusion.',
  'Cross-cutting participation does not distribute semantic ownership, mandate centralisation or authorise the assured subject to determine its own conclusion.')
]
for name, children, definition, overview, boundary in EC_ROOTS:
    root = proposed(name, 'enterprise', 'CT1', None, definition, overview, boundary, [AX, INFO])
    for rid in children:
        ec = find(rid)
        place(ec, 'CT2', root['key'], 'The established reusable responsibility is a constituent ability of ' + name + '; its canonical scope and exclusions remain unchanged.')

EC_SPECIAL = [
 ('Active Information Availability', 'EC-04', 'Maintain responsive, reconstructable active information and coordinate its safe use.', 'Active representation, observation and convergence support availability while preserving an explicit trust basis for use.', 'An active generation is not an authoritative version; failure to converge does not establish valid active state.', [src('governance/architectural-axioms.md', 'AX-05 ---'), CONTEXT]),
 ('Authoritative Durable State Establishment', 'EC-04', 'Atomically establish durable managed state and its authoritative version progression.', 'Durable acceptance gives managed information an explicit committed state from which applicable active representations can be reconstructed.', 'Commit is distinct from semantic source authority and from successful active convergence; applications do not gain a raw persistence path.', [src('governance/architectural-axioms.md', 'AX-05 ---')]),
 ('Binary Content Association & Resolution', 'EC-04', 'Maintain content associations and custody knowledge sufficient for governed payload resolution, retrieval and assembly.', 'Document attachments, diagnostic media and operational artefacts share a need to locate usable content while distinguishing logical meaning from physical possession.', 'No universal binary custody, storage topology or persistence authority is implied.', [BINARY, INFO]),
 ('Operation Policy Resolution', 'EC-06', 'Resolve effective, temporally applicable permission criteria for an established identity and governed operation.', 'Permission depends on controlled criteria, role assignments, action and information scope; revocation is distinct from natural expiry.', 'Does not authenticate identity or grant permission from caller-controlled tags or mere role names.', [AUTH]),
 ('Operation-Bound Policy Enforcement', 'EC-06', 'Enforce resolved policy constraints at the boundary where a governed action is attempted.', 'Distributed participants apply the applicable effective authorisation and fail closed when authority is absent or untrustworthy.', 'Enforcement participation does not distribute policy ownership or make security context persistent content.', [AUTH, src('governance/architectural-axioms.md', 'AX-07 ---')]),
 ('Information Attribution & Lineage', 'EC-07', 'Preserve source attribution and significant transformation relationships for managed information.', 'Lineage explains how an assertion reached its present form without turning every infrastructure event into provenance.', 'Does not infer clinical credibility from a successful transport or establish independent assurance findings.', [INFO, src('governance/architectural-axioms.md', 'AX-08 ---')]),
 ('Governed Activity Audit Evidence', 'EC-07', 'Capture required significant activity and decision evidence under its applicable governed capture authority.', 'Denied actions and successful mutations may both require trustworthy evidence; the audited identity and capture authority remain distinct.', 'Mandatory capture authority is specific to the established audit responsibility, not a platform-wide permission shortcut.', [AUDIT]),
 ('Operational Fulfilment Definition', 'EC-10', 'Define the context requirements, expected outcomes and housekeeping of an operational undertaking.', 'The semantic execution envelope applies to one-step and multi-step activity and stays distinct from dispatch and supervision.', 'A fulfilment definition does not execute, orchestrate or choreograph its activity.', [CONTEXT, TWIN]),
 ('Governed Activity Execution Control', 'EC-10', 'Instantiate, dispatch, supervise and progress authorised operational undertakings with explicit outcome standing.', 'Reusable execution control respects work-unit archetype, participation and uncertain effects rather than equating dispatch with completion.', 'Does not own underlying clinical-work meaning, clinical decisions or originator business authority.', [WORK, TWIN]),
 ('Entity-Relative Operational Progression', 'EC-10', 'Coordinate serial, entity-relative operational inputs and their interested behaviours.', 'Patient, practitioner and bed concerns recur across different business contexts; each can need a coherent progression frame without one central entity service.', 'Interest fan-out is distinct from intra-entity concurrency; entity progression does not acquire originating facts.', [TWIN])
]
for name, parent, definition, overview, boundary, citations in EC_SPECIAL:
    proposed(name, 'enterprise', 'CT3', find(parent)['key'], definition, overview, boundary, citations)
proposed('Information Authority Qualification', 'enterprise', 'CT2', cap('Trust, Accountability & Assurance', 'enterprise')['key'],
    'Represent and qualify the source-authority and credibility standing of information independently of transport, persistence and concurrency.',
    'Identity correlation, clinical-history synthesis, reference-content use and assurance evidence all need to distinguish the standing of an assertion from technical acceptance. This recurring ability is insufficiently explicit in the existing fourteen EC definitions and is proposed as a new peer responsibility.',
    'This is not independent service assurance, a universal conflict arbiter or clinical truth determination. Existing EC-13 governs authority nomenclature; this capability applies and preserves the qualified standing.',
    [src('governance/architectural-axioms.md', 'AX-06 ---'), INFO, DATASET], confidence='Medium')


def feature(name, owner, definition, overview, boundary, citations, confidence='High'):
    o = cap(owner)
    assert o['tier'] == 'CT3'
    key = 'proposal:feature-' + slug(name)
    assert key not in E
    e = dict(key=key, reference_id='HCM-FT-' + slug(name), latex_label='feature:' + slug(name),
             name=name, canonical_name=None, name_status='Proposed', type='feature', category=None, tier='FT',
             parent_key=None, owner_key=o['key'], architectural_status='Proposed', placement_status='Proposed',
             canonical_id=None, local_id='STEP2-' + slug(name).upper(), ancestry_status='proposed',
             sources=[], unresolved=[], views=[], id_aliases=[], name_aliases=[], baseline_version=None,
             definition=definition, overview=overview, boundary=boundary, confidence=confidence,
             provenance=provenance('Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence', *citations),
             placement_rationale='This behaviour changes or exposes the bounded concern of ' + o['name'] + ', rather than inheriting an adjacent responsibility.')
    E[key] = e; M['elements'].append(e)
    return e


# No symmetrical Feature templates: each row identifies an independently useful
# semantic delta or reconciles an existing behaviour without a Strategy Feature.
FEATURES = [
 ('Identity Source & Correction Lineage', 'Person Identity', 'Retain source-attributed identity assertions and externally decided correction history.', 'Identifier correlations can change when an upstream authority corrects a relationship. Reviewers must be able to distinguish the source decision, its effective standing and the managed links affected.', 'No probabilistic matching, golden-record selection or autonomous merge authority.', [INFO]),
 ('Provider Directory Discovery & Resolution', 'Practitioner', 'Expose qualified practitioner discovery using identifiers, profile criteria and effective standing.', 'Routing and care participation need to find a described provider matching stated criteria, beyond merely ingesting a national identifier or updating a profile.', 'Discovery supplies information; it does not determine privilege or availability.', [INFO]),
 ('Device Association Lineage', 'Device Association', 'Preserve the attributed history and effective basis of device-to-subject/place associations.', 'A telemetry record needs to be interpreted against the association applicable when it arose, rather than the device current location alone.', 'Association history does not interpret physiological observations or prove clinical correctness.', [INFO]),
 ('Referral Destination Coordination', 'Referral Administration', 'Coordinate qualified referral destination resolution and convey the request under its applicable disposition contract.', 'Referral intake and status capture leave a distinct routing responsibility: finding the intended service and preserving redirection without turning technical delivery into assumed care responsibility.', 'Does not make clinical triage decisions or infer Transfer of Care from acceptance.', [INFO]),
 ('Encounter History Retrieval', 'Episode & Encounter Administration', 'Expose qualified episode, movement and encounter-history context.', 'Consumers need to interpret prior care-place and episode relationships, not just the latest encounter state; historical retrieval remains bounded by source attribution and access policy.', 'A historical view does not retrospectively alter clinical events or acquire longitudinal-record ownership.', [INFO]),
 ('Order Outcome Association', 'Order Administration', 'Associate an externally established outcome with the applicable bounded order and its progression.', 'Execution completion and returned outcome are different checkpoints. This Feature makes the result link explicit without equating an order acknowledgement with an outcome.', 'Does not interpret diagnostic findings or declare fulfilment from transport delivery alone.', [INFO]),
 ('Diagnostic Preliminary & Corrected Report Handling', 'Diagnostic Administration', 'Receive, qualify and distribute preliminary, corrected and addendum diagnostic information with its relationship to prior reports.', 'The finalised-report Feature remains unchanged. The broader documented diagnostic behaviour needs a separate frame for information whose preliminary or correction standing is material.', 'Does not originate diagnostic interpretation or silently overwrite a previously received report.', [INFO, src('03-business-architecture/behaviours/02-service-administration.md', 'Diagnostic Administration')]),
 ('Care Goal & Plan Progression Representation', 'Care Plan Administration', 'Represent externally agreed care goals, participants and plan changes with their effective standing.', 'Plan ingestion/distribution is complemented by the ability to distinguish the applicable goal and plan context as a shared plan progresses.', 'Representing a goal does not choose treatment, determine clinical completion or grant plan approval authority.', [INFO]),
 ('Clinical Information Historical Reconstruction', 'Patient Clinical Record', 'Reconstruct what information Harmonia received, knew or managed at a relevant historical basis.', 'Current state and historical operational truth answer different questions. Reconstruction retains superseded and corrected knowledge without rewriting the earlier record.', 'Does not infer that historical information was clinically true or that a present source state applied in the past.', [INFO]),
 ('Binary Content Resolution & Assembly', 'Associated Clinical Binary Content Enablement', 'Resolve governed payload associations and retrieve or assemble the content required by a requesting information context.', 'A logical clinical item may reference externally held or multipart payloads. The Feature supplies a usable content result while retaining custody and missing-content standing.', 'No assumption of local copying, shared storage, universal content retention or originating clinical authority.', [BINARY]),
 ('Information Authority & Source Standing Representation', 'Information Authority Representation', 'Attach and preserve qualified source-authority and credibility standing for managed assertions.', 'The user of an assertion needs to distinguish authoritative, informational and anecdotal standing even when each was technically valid and durably accepted.', 'Authority classification is not a clinical truth judgement or a universal rule for choosing competing assertions.', [src('governance/architectural-axioms.md', 'AX-06 ---')]),
 ('Governed Dataset Content Revision', 'Governed Reference Dataset Administration', 'Review, accept and expose authorised changes to mutable reference content with effective revision standing.', 'A model definition can remain stable while approved terminology membership or mapping content changes. This Feature represents that operational governance without making every definition runtime mutable.', 'Does not invent an approval authority or bypass normal durable establishment.', [DATASET]),
 ('Mandatory Significant Activity Audit Capture', 'Significant Activity Audit Capture', 'Capture required significant activity evidence using the specifically governed authoritative audit-capture boundary.', 'The actor whose action is recorded need not be entitled to persist the audit. Required capture uses its own established authority and failure can block the governed operation.', 'Specific audit authority is not borrowed from the audited identity or generalised to other operations.', [AUDIT]),
 ('Information Transformation Lineage', 'Information Lineage Stewardship', 'Record meaningful source-to-derived information relationships and transformation attribution.', 'A consumer should be able to explain which source assertions contributed to a managed representation and what significant transformation occurred.', 'Routine cache reconstruction, retries and diagnostics do not automatically become permanent lineage.', [INFO]),
 ('Trusted Identity Context Intake', 'Health Information Control', 'Accept and qualify externally authenticated identity context for Harmonia-governed operations.', 'The boundary between WHO and WHAT remains explicit: an external identity assertion is verified as trusted input before internal authorisation is resolved.', 'Harmonia does not become an authentication or credential-issuance provider.', [AUTH]),
 ('Governed Policy Assignment & Revocation', 'Health Information Control', 'Manage approved permission criteria and identity/role assignments, including temporal applicability and revocation.', 'An assignment becoming inapplicable by expiry differs from an explicit revocation; both must affect the effective authority used for operations.', 'Management does not grant unconstrained administrative authority or derive permission from resource tags.', [AUTH]),
 ('Operation-Bound Authorisation Enforcement', 'Health Information Control', 'Apply effective policy at the governed action boundary and reject actions lacking sufficient authority.', 'Consistent enforcement is required across mutation, access, execution and administration rather than relying on each caller to voluntarily invoke a check.', 'Consumes trusted identity and criteria; it does not authenticate or establish clinical privilege from directory roles.', [AUTH]),
 ('Information Definition & Schema Governance', 'Information Design Governance', 'Govern structural definitions, data dictionaries and constraint meaning.', 'Existing definition-governance behaviour needs a distinct Feature frame from general semantic-model authorship and runtime validation.', 'Governed definitions do not own runtime instances or their clinical facts.', [INFO, src('03-business-architecture/behaviours/05-intrinsic-enablement.md', 'Govern Information Definition')]),
 ('Terminology Binding Governance', 'Information Design Governance', 'Govern how information definitions are bound to applicable terminology and value sets.', 'Binding a data element to a vocabulary is distinct from resolving a patient code or changing the runtime membership of a dataset.', 'Definition of a binding does not take external ontology authority or execute policy.', [DATASET, INFO]),
 ('Exchange Profile Governance', 'Information Design Governance', 'Govern the semantic and conformance constraints of an interoperability profile.', 'An external contract must state which representations and extensions are permitted; validation applies that contract but does not define it.', 'Profile governance does not transmit messages or publish internal operational metadata.', [src('governance/architectural-axioms.md', 'AX-13 ---'), INFO]),
 ('Entity-Relative Activity Coordination', 'Entity-Specific Operational Coordination', 'Sequence applicable behaviours for an identifiable entity input while preserving its operational reference frame.', 'Interest matching can select multiple behaviours while the entity progresses one input at a time. Each behaviour uses a fresh managed information context.', 'Fan-out is not concurrent execution; clinical decisions and originating information remain with their owners.', [TWIN]),
 ('Entity Behaviour Failure & Recovery Governance', 'Entity-Specific Operational Coordination', 'Apply explicit behaviour-failure dispositions and controlled, authorised resumption for entity-relative processing.', 'Failure may stop a behaviour, an input or the entity progression and may leave effects uncertain. Resumption must preserve that distinction rather than imply rollback or automatic safe retry.', 'Recovery cannot silently declare no effects, bypass governance or resume a halted entity automatically.', [TWIN]),
 ('Operational Activity Context Definition', 'Governed Operational Activity Design', 'Define the anticipated information context and expected fulfilment basis of an operational undertaking.', 'Even a one-unit activity needs a declared information and outcome frame. The definition is separate from loading values or running the business logic.', 'No dynamically invented resource set or orchestration engine is implied.', [CONTEXT, TWIN]),
 ('Activity Outcome & Continuation Rules', 'Governed Operational Activity Design', 'Define how output roles, expected outcomes and continuation conditions are interpreted for an undertaking.', 'An output may contribute to completion, subsequent activity or retained evidence under different rules. Those meanings need to be specified rather than inferred from a returned payload.', 'Completion policy is not business outcome truth; uncertain effects remain explicit.', [src('04-information-architecture/information-families/task-work.md', 'ActionableTask'), CONTEXT]),
 ('Uncertain Activity Outcome Reconciliation', 'Workflow & Activity Coordination', 'Reconcile indeterminate activity effects through authoritative observation before establishing a safe next disposition.', 'A timeout or absent response leaves a materially significant outcome unknown. Reconciliation can confirm an effect, establish absence or retain uncertainty without automatically retrying the original work.', 'Execution uncertainty differs from insufficient assurance evidence; a retry does not prove the earlier attempt had no effect.', [src('01-motivation/requirements-constraints/foundational-requirements.md', 'REQ-FND-004:'), WORK]),
 ('Controlled Administrative Recovery', 'Workflow & Activity Coordination', 'Perform authorised recovery as identifiable governed activity through normal execution and evidence boundaries.', 'Operators may need to resume, reconcile or repair an operational concern. That is first-class work with permission and outcome standing, rather than direct mutation through a console.', 'Administrative interaction grants no raw cache/database or subsystem-internal bypass.', [TWIN, AUTH]),
 ('Service Readiness Dependency Assessment', 'Service Offering Readiness Coordination', 'Associate time- and context-qualified dependency evidence to establish a bounded readiness view for an offering.', 'An offering may be in operating hours but not deliverable because staffing, equipment, materials or supporting services are missing. A readiness view retains insufficiency and the owners of those dependency assertions.', 'Readiness evidence does not originate clinical clearance or transfer resource management.', [evidence(STRATEGY, 'OI-BC-002'), CAPACITY]),
 ('Authorised Research Release Assembly', 'Authorised Research Information Provision', 'Assemble and qualify the existing information allowed by a particular authorised research purpose.', 'The release carries purpose and applicable privacy/consent/use constraints; the requester does not gain unrestricted clinical-record access by having a research role.', 'No ethics approval, de-identification guarantee or clinical-trial management is inferred.', [evidence(STRATEGY, 'Governed Research Information Provision'), INFO]),
 ('Population Notification Obligation Tracking', 'Population Health Notification Enablement', 'Track the relation between an applicable notification obligation, the supplied source evidence and observed notification disposition.', 'Sending a required notification and discharging the governing obligation are different assertions. This Feature keeps the explicit contract and indeterminate responses visible.', 'Does not define notifiability, author statutory rules or own public-health response.', [evidence(STRATEGY, 'Notifiable Health Event Management')]),
 ('Demand & Capacity Planning Projection', 'Service Planning Information Provision', 'Project qualified observed demand, capacity and utilisation for authorised planning consumers.', 'Comparisons need service, temporal and context qualifiers so that planned future capacity is not confused with present operational capacity.', 'Projection does not establish a planning conclusion or allocate services and staff.', [evidence(STRATEGY, 'Health Service Demand Assessment'), CAPACITY]),
 ('Governance Requirement Version Publication', 'Governance Requirement Publication Support', 'Publish attributable versions and applicability of externally approved governing requirements.', 'Operational and assurance consumers need to identify the governing basis they used rather than silently substitute a current rule for a historical one.', 'Publication does not confer requirement approval, authorisation policy or assurance-criteria authority.', [evidence(STRATEGY, '#### 17. Health Service Governance'), ASSURANCE]),
 ('Collaboration Interaction Capture Qualification', 'Clinical Collaboration', 'Qualify when a defined collaboration interaction creates a new governed information or activity consequence.', 'A message may convey existing information or an explicit interaction may derive a new managed consequence. The distinction preserves collaboration-native records while routing governed consequences to their accountable responsibility.', 'Conversation presence, display or read receipt does not itself make a clinical decision, approval or final record.', [src('05-information-systems-architecture/agora-omilia-collaboration.md', 'capture'), INFO]),
 ('Assurance Disposition & Design Modelling', 'Assurance Design', 'Model how satisfaction of an applicable governed concern will be independently established.', 'This proposal provides a Feature frame for the established design responsibility, including disposition, criteria and evidence expectations, without creating an additional assurance Function.', 'Modelling is distinct from requirement approval, subject management and evaluation.', [ASSURANCE]),
 ('Temporal Assurance Criteria Stewardship', 'Assurance Criteria Management', 'Manage identifiable assurance criteria and their relevant temporal and version applicability.', 'Evidence must be evaluated against the criteria applicable to its purpose and time rather than an unqualified current rule.', 'Criteria stewardship does not approve governing requirements or select a universal result taxonomy.', [ASSURANCE]),
 ('Independent Evidence Assessment & Adjudication', 'Governed Assurance', 'Independently establish assurance context, assess evidence and adjudicate findings under applicable criteria.', 'The Feature unifies the review frame while retaining the three distinct canonical Functions and the boundary between criterion-level assessment and reportable conclusion.', 'No new assurance Function or clinical-adequacy mandate is introduced; insufficient evidence establishes neither satisfaction nor non-satisfaction.', [ASSURANCE]),
]
for args in FEATURES:
    feature(*args, confidence='Low' if args[1] in ('Governance Requirement Publication Support', 'Service Offering Readiness Coordination') else 'High')

for owner, name, definition, overview in [
 ('Clinic & Practice Operations', 'Clinic Session Capacity Management', 'Represent available, committed and utilised session capacity under clinic-specific constraints.', 'Clinic capacity depends on session staffing and service mix as well as places; queue progression alone is insufficient.'),
 ('Ward Operations', 'Ward Care Capacity Management', 'Represent ward care capacity in relation to staffing, care-place suitability and patient dependency.', 'A clean bed can remain unusable when staffing or patient needs constrain the ward; source clinical facts remain attributed.'),
 ('Theatre Operations', 'Theatre Session Capacity Management', 'Represent available and committed procedural-session capacity with its resource dependencies.', 'Theatre capacity involves session time, team capability, equipment and recovery provision rather than a count of theatres.'),
 ('Emergency Department Operations', 'Emergency Treatment Capacity Management', 'Represent current emergency treatment capacity under space, staffing and acuity constraints.', 'Bay state and length of stay contribute evidence but do not alone establish the ability to accept another care demand.'),
 ('Outpatient Operations', 'Outpatient Appointment Capacity Management', 'Represent usable outpatient session capacity from booking, staffing and local flow information.', 'The projection distinguishes scheduled slots from currently usable care capacity without taking source booking authority.'),
 ('Mobile Staff Management', 'Mobile Service Capacity Management', 'Represent mobile service capacity with travel, coverage, work commitment and availability context.', 'Mobile availability cannot be interpreted as a simple on-duty count because distance, assignments and suitable skills affect deliverability.')
]:
    feature(name, owner, definition, overview,
            'Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.', [CAPACITY])


def relation(typ, start, end, rationale, citations=None, **extra):
    a = E[start] if start in E else find(start)
    b = E[end] if end in E else find(end)
    assert a['key'] != b['key']
    if any(r['type'] == typ and r['from'] == a['key'] and r['to'] == b['key'] and r['status'] != 'unresolved' for r in R):
        return
    R.append(dict(type=typ, **{'from': a['key'], 'to': b['key']}, status='Proposed', sources=[],
                  rationale=rationale, provenance=provenance('Semantic responsibility analysis; no authority transfer', *(citations or [INFO])), **extra))


def behaviour(typ, name, owner, definition, overview, boundary, citations):
    o = find(owner)
    code = {'function':'FN', 'service':'SV', 'process':'PR'}[typ]
    key = 'proposal:' + typ + '-' + slug(name)
    assert key not in E
    e = dict(key=key, reference_id='HCM-' + code + '-' + slug(name), latex_label=typ + ':' + slug(name),
             name=name, canonical_name=None, name_status='Proposed', type=typ, category=None, tier=None,
             parent_key=None, owner_key=o['key'], architectural_status='Proposed', placement_status='Proposed',
             canonical_id=None, local_id=slug(name).upper(), ancestry_status='proposed', sources=[], unresolved=[],
             views=[], id_aliases=[], name_aliases=[], baseline_version=None,
             definition=definition, overview=overview, boundary=boundary,
             provenance=provenance('Strongly implied bounded information/work responsibility', *citations),
             placement_rationale='Behaviour remains within ' + o['name'] + ' responsibility.')
    E[key] = e; M['elements'].append(e)
    return e

BEHAVIOURS = [
 ('function', 'Resolve Associated Binary Content', 'Associated Clinical Binary Content Enablement', 'Resolve logical content associations to an authorised, custody-qualified retrieval basis.', 'Changes an unresolved content request into an identified retrieval basis or an explicit inability to resolve it.', 'Does not assert payload existence, copy all content or establish clinical meaning.', [BINARY]),
 ('function', 'Assemble Associated Binary Content', 'Associated Clinical Binary Content Enablement', 'Retrieve and assemble the requested logical content from its permitted constituent payloads.', 'Moves a qualified retrieval request to an assembled result or explicit unavailable/partial standing.', 'No storage topology or universal local custody is selected.', [BINARY]),
 ('service', 'Associated Binary Content Retrieval', 'Associated Clinical Binary Content Enablement', 'Allow information-consuming capabilities to request resolution and retrieval of associated content.', 'The client contract returns authorised content or explicit incomplete/unavailable standing while retaining custody attribution.', 'The client acquires neither physical repository access nor originating clinical authority.', [BINARY]),
 ('function', 'Qualify Information Authority Standing', 'Information Authority Representation', 'Associate source and credibility qualifications with an information assertion under applicable governed semantics.', 'Moves an unqualified received assertion to explicitly attributed authority standing without equating persistence success to credibility.', 'Does not adjudicate all competing claims or originate clinical truth.', [src('governance/architectural-axioms.md', 'AX-06 ---')]),
 ('function', 'Accept Governed Dataset Revision', 'Governed Reference Dataset Administration', 'Apply an explicitly authorised content revision and its effective standing through governed information-state progression.', 'Moves a reviewed change to the applicable approved content revision, retaining attribution and uncertainty if durable establishment cannot be confirmed.', 'The Function does not invent who approves or become a persistence manager.', [DATASET]),
 ('service', 'Governed Reference Dataset Content Query', 'Governed Reference Dataset Administration', 'Provide authorised consumers with approved content at an identified revision and applicability basis.', 'Consumers can resolve the membership or mapping content they actually used without treating build-time model definitions as runtime data.', 'Does not grant unrestricted dataset mutation or external terminology authority.', [DATASET]),
 ('function', 'Capture Required Significant Audit Evidence', 'Significant Activity Audit Capture', 'Establish required significant activity evidence under the governed authoritative audit-capture responsibility.', 'Moves an auditable fact to confirmed captured standing or an explicit capture failure affecting the protected operation.', 'Specific capture authority is not borrowed from the originating identity and is not generalised.', [AUDIT]),
 ('service', 'Mandatory Audit Evidence Capture', 'Significant Activity Audit Capture', 'Allow governed-operation boundaries to require authoritative capture of significant activity evidence.', 'The client contract distinguishes confirmed capture from inability to capture, allowing required operations to fail closed.', 'The exposed boundary does not permit callers to assign themselves audit platform authority.', [AUDIT]),
 ('function', 'Record Significant Information Lineage', 'Information Lineage Stewardship', 'Associate a managed assertion with its attributed sources and information-significant transformation.', 'Changes an unexplained derived representation into an attributable relationship to the source information and transformation basis.', 'Routine retries, cache mechanics and debug traces are excluded unless information-significant.', [INFO]),
 ('function', 'Coordinate Entity-Relative Input', 'Entity-Specific Operational Coordination', 'Select interested behaviours and progress one entity-relative input with explicit participation and effect standing.', 'Moves an accepted input through the applicable stable behaviour sequence using fresh managed contexts.', 'No concurrent intra-entity progression or implicit clinical authority is introduced.', [TWIN]),
 ('service', 'Entity Operational Input Submission', 'Entity-Specific Operational Coordination', 'Allow authorised domain coordinators to submit an entity-relative stimulus or requested operational effect.', 'Acceptance concerns the stated handoff contract; the caller observes completion/effect through separately qualified progression information.', 'Input acceptance does not establish successful completion or authority over source clinical facts.', [TWIN]),
 ('function', 'Maintain Operational Fulfilment Definition', 'Governed Operational Activity Design', 'Establish and revise the declared context needs, expected outcome basis and housekeeping of an operational undertaking.', 'Changes governed activity design while preserving the applicability of the definition used by an existing undertaking.', 'A definition revision is not a command to run work or a dynamic clinical workflow generator.', [CONTEXT, TWIN]),
 ('function', 'Reconcile Indeterminate Activity Effects', 'Workflow & Activity Coordination', 'Inspect authoritative observations to establish, qualify or retain uncertainty about a previously indeterminate activity effect.', 'Moves an uncertainty concern to a justified disposition while preserving the possibility that the original attempt produced an effect.', 'Missing evidence does not establish absence of effect and cannot automatically justify a repeat mutation.', [WORK, src('01-motivation/requirements-constraints/foundational-requirements.md', 'REQ-FND-004:')]),
 ('function', 'Establish Activity Effect Observation Basis', 'Workflow & Activity Coordination', 'Associate the applicable handoff contract and authoritative observations with an indeterminate operational undertaking.', 'Changes an unqualified uncertainty concern into a time- and attempt-specific observation basis for reconciliation.', 'Source observations retain their authority; this is an operational observation basis rather than an independent assurance assessment.', [WORK, CONTEXT]),
 ('function', 'Determine Reconciled Activity Continuation', 'Workflow & Activity Coordination', 'Establish the permitted operational continuation from reconciled effect standing and applicable authorisation.', 'Moves a reconciled undertaking to justified continuation, authorised retry or explicitly retained uncertainty.', 'Does not confer clinical decision authority or infer absence of effects from silence; policy evaluation remains separately owned.', [WORK, AUTH]),
 ('process', 'Indeterminate Activity Outcome Reconciliation Process', 'Workflow & Activity Coordination', 'Order the observation, effect qualification and authorised disposition of a materially indeterminate operational outcome.', 'First inspect the governing handoff and available authoritative state; then reconcile observed effects; finally authorise a safe continuation, retry or retained uncertainty. Each participating Function keeps its responsibility owner.', 'Not a universal lifecycle or automatic retry loop. A safe disposition may remain unresolved.', [WORK, TWIN]),
 ('function', 'Assess Service Offering Readiness', 'Service Offering Readiness Coordination', 'Evaluate applicable dependency evidence to establish a context- and time-qualified service readiness view.', 'Combines qualified dependency standing without converting missing evidence into ready or assuming that an available resource establishes deliverability.', 'Resource ownership and clinical clearance remain external to this coordination.', [CAPACITY, evidence(STRATEGY, 'OI-BC-002')]),
 ('service', 'Service Offering Readiness Query', 'Service Offering Readiness Coordination', 'Expose the qualified readiness view of a defined offering and operating context to operational planning consumers.', 'The response identifies relevant evidence basis and insufficiency rather than an unconditional ready flag.', 'A readiness answer grants no resource allocation or clinical go/no-go authority.', [CAPACITY]),
 ('function', 'Assemble Purpose-Qualified Research Release', 'Authorised Research Information Provision', 'Construct a release containing only existing information permitted for the explicitly authorised research purpose.', 'Moves a qualified request to a bounded release with source attribution and applicable use constraints.', 'Does not author ethics approvals, promise de-identification or acquire ownership of research conclusions.', [INFO, evidence(STRATEGY, 'Governed Research Information Provision')]),
 ('service', 'Authorised Research Information Export', 'Authorised Research Information Provision', 'Provide an approved research consumer with purpose-qualified existing health information under its release contract.', 'The service exposes the permitted release and its applicable use basis rather than unrestricted clinical repository access.', 'No new research-governance mandate or universal release format is allocated.', [INFO]),
]
for args in BEHAVIOURS:
    behaviour(*args)

# Proposed realisation/exposure links for genuine semantic deltas only. Canonical
# behaviour remains directly Capability-owned even when it participates in a Feature.
REALISATIONS = {
 'Provider Directory Discovery & Resolution': ['service:practitioner-directory-query'],
 'Order Outcome Association': ['function:associate-order-outcome'],
 'Information Definition & Schema Governance': ['function:govern-information-definition'],
 'Terminology Binding Governance': ['function:govern-terminology-binding'],
 'Exchange Profile Governance': ['function:govern-exchange-profile'],
 'Assurance Disposition & Design Modelling': ['function:design-service-assurance'],
 'Temporal Assurance Criteria Stewardship': ['function:manage-assurance-criteria'],
 'Independent Evidence Assessment & Adjudication': ['function:establish-assurance-context', 'function:assess-assurance-evidence', 'function:adjudicate-assurance-assessment'],
 'Clinic Session Capacity Management': ['function:manage-clinic--practice-capacity'],
 'Ward Care Capacity Management': ['function:manage-ward-capacity'],
 'Theatre Session Capacity Management': ['function:manage-theatre-capacity'],
 'Emergency Treatment Capacity Management': ['function:coordinate-ed-departmental-capacity'],
 'Outpatient Appointment Capacity Management': ['function:manage-outpatient-capacity'],
 'Mobile Service Capacity Management': ['function:manage-mobile-service-capacity']
}
for feat, behaviours in REALISATIONS.items():
    for b in behaviours:
        # Resolve by exact semantic name below if local legacy key differs.
        if b not in E:
            choices = [e for e in E.values() if slug(e['name']) == b.split(':')[1]]
            assert len(choices) == 1, ('Explicit behaviour identity requires correction', b)
            b = choices[0]['key']
        relation('realises_feature' if E[b]['type'] == 'function' else 'associated_feature', b, find(feat)['key'],
                 'This existing behaviour materially discharges the proposed Feature; ownership and canonical definition remain unchanged.')

for feat, names in {
 'Binary Content Resolution & Assembly': ['Resolve Associated Binary Content', 'Assemble Associated Binary Content'],
 'Information Authority & Source Standing Representation': ['Qualify Information Authority Standing'],
 'Governed Dataset Content Revision': ['Accept Governed Dataset Revision'],
 'Mandatory Significant Activity Audit Capture': ['Capture Required Significant Audit Evidence'],
 'Information Transformation Lineage': ['Record Significant Information Lineage'],
 'Entity-Relative Activity Coordination': ['Coordinate Entity-Relative Input'],
 'Operational Activity Context Definition': ['Maintain Operational Fulfilment Definition'],
 'Uncertain Activity Outcome Reconciliation': ['Establish Activity Effect Observation Basis', 'Reconcile Indeterminate Activity Effects', 'Determine Reconciled Activity Continuation'],
 'Service Readiness Dependency Assessment': ['Assess Service Offering Readiness'],
 'Authorised Research Release Assembly': ['Assemble Purpose-Qualified Research Release']
}.items():
    for name in names:
        relation('realises_feature', find(name)['key'], find(feat)['key'], 'The proposed state change materially realises this Feature; this is a graph edge, not an ownership chain.')
for service, names in {
 'Associated Binary Content Retrieval': ['Resolve Associated Binary Content', 'Assemble Associated Binary Content'],
 'Mandatory Audit Evidence Capture': ['Capture Required Significant Audit Evidence'],
 'Entity Operational Input Submission': ['Coordinate Entity-Relative Input'],
 'Service Offering Readiness Query': ['Assess Service Offering Readiness'],
 'Authorised Research Information Export': ['Assemble Purpose-Qualified Research Release']
}.items():
    for name in names:
        relation('uses_function', find(service)['key'], find(name)['key'], 'The exposed client contract is fulfilled using the bounded state-changing behaviour; no one-to-one generation rule is implied.')
relation('participating_function', find('Indeterminate Activity Outcome Reconciliation Process')['key'],
         find('Reconcile Indeterminate Activity Effects')['key'], 'Reconciliation supplies effect qualification within the process; remaining observation and authorisation use existing responsibilities.')
process = find('Indeterminate Activity Outcome Reconciliation Process')
process['ordered_function_chain'] = [find(n)['key'] for n in ['Establish Activity Effect Observation Basis', 'Reconcile Indeterminate Activity Effects', 'Determine Reconciled Activity Continuation']]
for fn in process['ordered_function_chain']:
    relation('participating_function', process['key'], fn, 'This Function participates in the explicitly ordered proposed reconciliation chain, retaining its capability responsibility.')

# Proposed decomposition edges are independent from element status. Original
# established partial chains remain in R, including Client Administration -> Person Identity.
for e in E.values():
    if e.get('parent_key') and e['placement_status'] == 'Proposed':
        relation('capability_decomposition', e['parent_key'], e['key'], e['placement_rationale'], [META])

# Domain-purpose BC/BEC support: explicitly bounded pairs, never lexical joins.
BC_SUPPORT = [
 ('Individual Care Delivery', ['Health Information Access', 'Patient Clinical Record', 'Care Plan Administration'], 'Care decisions require qualified longitudinal information and the representation of externally agreed plans; professional authority remains with care actors.'),
 ('Care Access Management', ['Referral Administration', 'Scheduling Administration', 'Client-Centred Administration'], 'Intake, referral disposition and booking synchronisation support access without deciding acceptance of clinical responsibility.'),
 ('Care Scheduling & Capacity Management', ['Bed & Care-Place Management', 'Service Capacity Management'], 'Qualified care-place state and local-capacity aggregation inform operational scheduling without taking source slot or staffing ownership.'),
 ('Care Coordination & Transition', ['Discharge Management', 'Clinical Collaboration', 'Care Plan Administration'], 'Operational readiness, shared plan context and discussion support transition while clinical clearance and responsibility transfer retain their source authority.'),
 ('Care Support Logistics Management', ['Patient Transport', 'Clinical Logistics Coordination', 'Work Allocation & Dispatch'], 'Physical movement and support work need operational requests, custody/progression evidence and eligible dispatch; clinical work remains externally governed.'),
 ('Diagnostic Support Provision', ['Diagnostic Administration', 'Diagnostic Services', 'Order Administration'], 'Requisition, report and outcome correlation supports diagnostic provision without interpreting specimens or images.'),
 ('Medication & Pharmacy Services', ['Medication Administration', 'Medication Therapy'], 'Source prescription, dispensing, administration and allergy/history information supports pharmacy activity without Harmonia prescribing or dispensing.'),
 ('Clinical Technology Management', ['Clinical Device Administration', 'Clinical Resource Management'], 'Device identity, associations and operational equipment context support technology use; maintenance, regulatory and lifecycle decisions retain their owners.'),
 ('Health Information Management', ['Patient Clinical Record', 'Clinical Record Administration', 'Associated Clinical Binary Content Enablement'], 'Document lifecycle, longitudinal synthesis and payload association support governed information without collapsing legal originals, projections and custody.'),
 ('Clinical Knowledge Management', ['Clinical Knowledge Services', 'Governed Reference Dataset Administration'], 'Governed terminology and approved reference content support knowledge use while external ontology authority remains explicit.'),
 ('Health Information Standards Governance', ['Information Design Governance'], 'Model, dictionary, profile and conformance responsibilities make approved standards operationally usable without replacing standards authority.'),
 ('Clinical Semantics Governance', ['Information Design Governance', 'Clinical Knowledge Services'], 'Definition bindings and terminology resolution support shared meaning; runtime coding and ontology approval remain distinct.'),
 ('Health Information Exchange', ['Health Information Exchange Enablement', 'Health Information Communication'], 'Harmonia coordinates bounded information exchange under standards contracts; organisational sharing agreements and domain content retain their authority.'),
 ('Digital Communication Channel', ['Clinical Communication Administration', 'General Collaboration'], 'Directed clinical message handling and operational dialogue support different communication purposes without making a conversation a clinical record.'),
 ('Digital Identity & Access Governance', ['Health Information Control', 'Client Privacy'], 'Trusted identity intake, effective policy and patient restrictions support governed access without owning external authentication or consent authority.'),
 ('Digital Accountability & Trust', ['Significant Activity Audit Capture', 'Information Lineage Stewardship', 'Information Authority Representation'], 'Attribution, activity evidence and source standing answer different accountability questions; evidence alone is not an assurance conclusion.'),
 ('Research Information Support', ['Authorised Research Information Provision'], 'Provides existing information under explicitly authorised research-use constraints without taking research governance or trial management.'),
 ('Population Health Surveillance', ['Population Health Notification Enablement', 'Health Information Access'], 'Source information and required notification progression support surveillance without originating population-health conclusions or statutory duties.'),
 ('Health-System Planning', ['Service Planning Information Provision'], 'Qualified observed demand and capacity evidence supports planning without determining commissioned capacity or service distribution.'),
 ('Health Workforce Management', ['Provider Administration', 'Clinical Qualification Management', 'Clinical Rostering'], 'Professional standing, qualified checks and source coverage information support workforce activity without managing employment or granting clinical privilege.'),
 ('Healthcare Organisation & Service Structure Management', ['Organisation Administration', 'Location Administration', 'Health Service Administration'], 'Represented entity identities and service mappings support structure decisions; mapped entity owners retain authority.'),
 ('Financial & Commercial Services', ['Funding & Coverage Administration', 'Clinical Charging & Claims Administration'], 'Coverage flags and activity/charging exports support responsible financial systems without financial governance or claims adjudication.'),
 ('Health Service Governance', ['Governance Requirement Publication Support'], 'Proposed bounded publication support makes the governing requirement available to consumers without granting the platform requirement approval authority.'),
 ('Health Service Operational Readiness', ['Service Offering Readiness Coordination'], 'Proposed evidence-based coordination supports collective deliverability while dependency owners retain control.'),
]
for bc, becs, rationale in BC_SUPPORT:
    for bec in becs:
        relation('enabled_by', cap(bc, 'business')['key'], cap(bec)['key'], rationale)

# Explicit recurrence families and evidence examples; broad CT1 participation is
# not inherited and no mapping is generated from names or software allocation.
EC_ENABLEMENT = [
 (['Person Identity', 'Healthcare Subject', 'Provider Administration', 'Organisation Administration', 'Location Administration', 'Health Service Administration', 'Clinical Device Administration'],
  ['EC-01', 'EC-03', 'EC-05', 'EC-07'], 'Entity/profile resolution, qualified relationships and non-destructive lifecycle recur across persons, providers, organisations, places, services and devices.'),
 (['Client Privacy', 'Health Information Control'], ['EC-06', 'EC-02', 'EC-07'], 'Consent-qualified use and governed action need effective policy, trusted context and attributable significant decisions.'),
 (['Referral Administration', 'Order Administration', 'Diagnostic Administration', 'Medication Administration', 'Procedure Administration', 'Episode & Encounter Administration'],
  ['EC-02', 'EC-03', 'EC-08', 'EC-10'], 'Transactions bind clinical/service context, preserve distinct lifecycle and acknowledgements, exchange source information and coordinate assigned operational consequences.'),
 (['Clinical Record Administration', 'Patient Clinical Record', 'Care Plan Administration'], ['EC-03', 'EC-04', 'EC-07'], 'Document supersession, longitudinal synthesis and plan revision share managed-state, preservation and attribution needs while keeping different content ownership.'),
 (['Health Information Access', 'Clinical Knowledge Services', 'On-Call Management'], ['EC-05', 'EC-06'], 'Information, terminology and on-call resolution all expose criteria-qualified discovery under policy; the discovered subjects remain different.'),
 (['Health Information Exchange Enablement', 'Health Information Communication', 'Clinical Communication Administration', 'Population Health Notification Enablement'],
  ['EC-08', 'EC-09', 'EC-12'], 'Addressed exchange, syndication and notifications require explicit contract/delivery standing and resilient observation rather than equating transport acknowledgement with outcome.'),
 (['Primary Care', 'Acute Care', 'Emergency Care', 'Inpatient Care', 'Community Care', 'Outreach Care', 'Remote & Virtual Care', 'Preventive Care', 'Diagnostic Services', 'Medication Therapy'],
  ['EC-02', 'EC-04', 'EC-08'], 'Care contexts compose qualified information and boundary exchange; this recurrence does not require another record store or specialised capture engine in every setting.'),
 (['Clinic & Practice Operations', 'Ward Operations', 'Theatre Operations', 'Emergency Department Operations', 'Outpatient Operations', 'Mobile Staff Management', 'Service Capacity Management'],
  ['EC-02', 'EC-03', 'EC-09'], 'Local flow/capacity changes need context-qualified state and notifications; common mechanisms do not transfer capacity semantics to a central owner.'),
 (['Work Allocation & Dispatch', 'Patient Transport', 'Clinical Logistics Coordination', 'Discharge Management', 'Workflow & Activity Coordination'],
  ['EC-02', 'EC-10', 'EC-12'], 'Operational activity needs eligible progression, effect standing and recoverable execution under context; clinical work and source clearances retain their authority.'),
 (['Clinical Collaboration', 'General Collaboration', 'Presentation Services'], ['EC-11', 'EC-06', 'EC-09'], 'Human interaction and notifications consume governed context without becoming clinical truth, policy ownership or a central collaboration service.'),
 (['Information Design Governance', 'Clinical Knowledge Services', 'Governed Reference Dataset Administration'], ['EC-13', 'EC-03', 'EC-07'], 'Definition, binding and mutable reference-content governance share conformance and attribution while retaining separate lifecycle meanings.'),
 (['Information Authority Representation', 'Patient Clinical Record', 'Person Identity', 'Governed Assurance', 'Governed Reference Dataset Administration'],
  ['Information Authority Qualification'], 'Source standing recurs in competing identity assertions, clinical synthesis, reference content and evidence use; persistence success and freshness are insufficient substitutes.'),
 (['Associated Clinical Binary Content Enablement', 'Clinical Record Administration', 'Clinical Collaboration'],
  ['Binary Content Association & Resolution'], 'Documents and collaboration references can require payload resolution while logical ownership, collaboration-native media and physical custody stay distinct.'),
 (['Significant Activity Audit Capture', 'Health Information Control', 'Clinical Communication Administration'],
  ['Governed Activity Audit Evidence'], 'Required evidence of governed actions recurs across policy, disclosure and communication; only mandatory authoritative capture has the specifically established intrinsic authority.'),
 (['Information Lineage Stewardship', 'Person Identity', 'Patient Clinical Record'],
  ['Information Attribution & Lineage'], 'Correction history, source attribution and derived clinical synthesis require meaningful lineage rather than infrastructure traces.'),
 (['Entity-Specific Operational Coordination', 'Discharge Management', 'Bed & Care-Place Management'],
  ['Entity-Relative Operational Progression'], 'Patient progression, bed turnover and entity-specific operational activity benefit from a coherent entity-relative execution frame; this is a proposed reusable contribution rather than component allocation.'),
 (['Governed Operational Activity Design', 'Workflow & Activity Coordination'],
  ['Operational Fulfilment Definition', 'Governed Activity Execution Control'], 'Defined fulfilment context and execution control recur in human work, human decisions and synthetic processing, while remaining different abilities.'),
 (['Patient Clinical Record', 'Governed Reference Dataset Administration'],
  ['Active Information Availability', 'Authoritative Durable State Establishment'], 'Both clinical and reference information require responsive use and authoritative revision establishment, with separate trust/concurrency domains.'),
]
for becs, ecs, rationale in EC_ENABLEMENT:
    for bec in becs:
        for ec in ecs:
            relation('enterprise_enabled_by', cap(bec)['key'], find(ec, category='enterprise')['key'], rationale)

# Import the explicit approved assurance contribution matrix, including negative
# statements as constraints rather than silently turning absence into a new edge.
assurance_text = (ROOT / 'docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md').read_text()
M['relationship_constraints'] = []
for line in assurance_text.splitlines():
    if not line.startswith('| [EC-'):
        continue
    cells = [s.strip() for s in line.strip('|').split('|')]
    ec = re.search(r'EC-\d+', cells[0])[0]
    for bec, contribution in zip(['Assurance Design', 'Assurance Criteria Management', 'Governed Assurance'], cells[1:]):
        record = dict(type='enterprise_contribution', **{'from': cap(bec)['key'], 'to': find(ec)['key']},
                      status='established', contribution=contribution,
                      sources=[{'source':'assurance-strategy', 'line': assurance_text.splitlines().index(line)+1, 'text':line, 'anchor':'collaborative-enterprise-capability-contribution-matrix'}])
        if contribution == 'No Material Role':
            M['relationship_constraints'].append(record)
        else:
            R.append(record)

# Authored purpose statements describe the benefit, separately from the ability
# definition and explanatory overview. No generic per-capability filler template.
INTENTS = {
 'Healthcare Entity Information Enablement':'Make the participant, place and service references in a care concern consistently interpretable across organisational boundaries.',
 'Care Administration Information Enablement':'Keep the requested, accepted, performed and recorded aspects of care administration distinguishable as information crosses systems.',
 'Care Context Integration Enablement':'Give a care participant the appropriate information context for the particular integration purpose without duplicating general information responsibilities.',
 'Care Operations Coordination Enablement':'Make operational constraints and progression visible enough to coordinate the care-delivery responsibilities actually assigned to Harmonia.',
 'Governed Health Information Enablement':'Keep managed health information usable and attributable across time, boundaries and authorised purposes.',
 'Human Collaboration & Work Enablement':'Allow people and automated participants to interact and progress authorised work with intelligible context and accountability.',
 'Independent Service Assurance Enablement':'Establish justified confidence in governed service behaviour without giving the subject control of its own assurance conclusion.',
 'Care Access Transaction Enablement':'Make an access request and its disposition traceable without confusing a booking with care responsibility or delivery.',
 'Care Fulfilment Administration Enablement':'Maintain the link between a bounded care request, the relevant episode and the externally observed fulfilment outcome.',
 'Care Documentation & Communication Enablement':'Preserve which document or plan applies and what a directed recipient has actually acknowledged.',
 'Care Financial Information Enablement':'Give responsible financial systems the care-activity and coverage context they need without creating another financial authority.',
 'Longitudinal & Community Care Integration':'Support continuity when care information must follow the person across providers, visits and distance.',
 'Facility & Episodic Care Integration':'Make presentation- and episode-specific information available to the responsible care team in its applicable context.',
 'Specialist Clinical Context Integration':'Keep specialised clinical context connected to the wider record while retaining the specialist source of clinical judgement.',
 'Care Setting Operations Enablement':'Express the actual operating constraints of each setting rather than substituting a generic resource count for capacity.',
 'Care Resource & Capacity Coordination':'Make resource-state contributions and coordinated capacity views usable without collapsing their different responsibilities.',
 'Operational Workforce Coordination':'Support eligible operational participation using qualified availability and coverage information.',
 'Workforce Qualification & Development Enablement':'Keep professional standing, competence, supervision and operational-access evidence distinguishable when work is considered.',
 'Care Movement & Transition Coordination':'Coordinate the operational prerequisites and observed milestones of movement and departure without assuming clinical clearance.',
 'Clinical Information Stewardship Enablement':'Make the integrated clinical account usable while keeping its source assertions and associated content attributable.',
 'Health Information Connectivity Enablement':'Let authorised participants find and exchange the information their contract permits.',
 'Semantic & Reference Knowledge Enablement':'Keep structural meaning, terminology bindings and approved reference content consistent enough for shared interpretation.',
 'Information Protection & Accountability Enablement':'Make information use both governed and explainable through policy, attributable rules and significant evidence.',
 'Secondary Health Information Use Enablement':'Preserve the particular authorised purpose and obligation when clinical information is used beyond individual care delivery.',
 'Collaborative Interaction Enablement':'Give participants a useful discussion and interaction context without turning the communication medium into a clinical authority.',
 'Operational Activity & Temporal Enablement':'Keep work progression and its timing intelligible across human and synthetic undertakings.',
 'Assurance Specification Enablement':'Make the intended basis of assurance explicit before evidence is assessed.',
 'Independent Assurance Evaluation Enablement':'Produce findings supported by applicable evidence and criteria while retaining inability to conclude.',
 'Practitioner':'Allow a described healthcare professional to be reliably recognised and found across participating systems.',
 'Practitioner Role':'Make the capacity and organisational context of a practitioner interaction explicit.',
 'Provider Relationship':'Preserve which affiliations apply when interpreting provider participation.',
 'Provider Engagement':'Make the externally established terms and privilege standing of provider participation available without granting it.',
 'Healthcare Organisation':'Give service and affiliation references a verified organisational identity anchor.',
 'Organisational Structure':'Make organisational relationships navigable without confusing structure with ownership or physical containment.',
 'Organisation Contact':'Provide verified channels for reaching the intended healthcare organisation.',
 'Healthcare Location':'Identify the physical place relevant to care information and operational activity.',
 'Location Structure':'Allow a particular place to be interpreted within its physical surroundings.',
 'Care-Place Definition':'Describe the stable characteristics that distinguish one care place from another.',
 'Healthcare Service Definition':'Make the purpose and published offering of a healthcare service recognisable independently of its current delivery arrangement.',
 'Service Provision Relationship':'Make it clear which provider delivers which service at which place and through which participating roles.',
 'Service Availability Definition':'Express the operating windows against which a service participation request can be considered.',
 'Service Eligibility Definition':'Make the governed intake and catchment conditions available to the party responsible for access decisions.',
 'Health Product Definition':'Provide interpretable product context for health information without acquiring product-management authority.',
 'Product Formulary & Catalogue':'Preserve the applicable approved product set and the source of its standing.',
 'Product Batch & Serial Tracking':'Allow material product references in care information to be traced to their batch or instance context.',
 'Device Definition':'Make the stated type and capability of a device understandable before its instance is used as an information source.',
 'Device Instance':'Keep the identity of the actual physical equipment distinguishable from its model and endpoint.',
 'Device Association':'Allow a device-originated item to be associated with the correct subject and place at the relevant time.',
 'Device Connectivity':'Make a device feed reachable through its registered communication binding.',
 'Associated Clinical Binary Content Enablement':'Obtain the content needed to use a managed information item without assuming where every payload is physically held.',
 'Information Authority Representation':'Keep an assertion technical acceptance separate from the standing of the source that asserted it.',
 'Governed Reference Dataset Administration':'Make approved reference-content change usable during operation without changing the meaning of its structural definition.',
 'Significant Activity Audit Capture':'Ensure required significant activity evidence can be captured under the authority specifically assigned to that responsibility.',
 'Information Lineage Stewardship':'Allow a reader to explain the meaningful source and transformation history of a managed assertion.',
 'Authorised Research Information Provision':'Supply the permitted existing information for a particular authorised research use.',
 'Population Health Notification Enablement':'Keep the applicable notification duty and the observed communication disposition traceable to each other.',
 'Service Planning Information Provision':'Give planners qualified observed evidence without predetermining the planning judgement.',
 'Governance Requirement Publication Support':'Let operational and assurance consumers identify the governing statement and applicability basis they actually used.',
 'Entity-Specific Operational Coordination':'Keep the progression of one represented entity coherent across interested operational behaviours.',
 'Governed Operational Activity Design':'Make the expected information and fulfilment basis of an undertaking explicit before it executes.',
 'Service Offering Readiness Coordination':'Distinguish collective service deliverability from nominal availability or readiness of any single dependency.',
 'Clinical Finding Interpretation':'Make the clinical significance of observations intelligible before diagnostic or response decisions are taken.',
 'Diagnosis & Problem Determination':'Establish the clinically accountable problem statement that subsequent care decisions address.',
 'Health Risk & Prognostic Determination':'Make anticipated clinical risk and course explicit for the individual care concern.',
 'Care Goal Definition':'Establish the intended clinical benefit and priorities against which care responses can be considered.',
 'Care Response Planning':'Make the intended clinical response coherent in relation to the agreed care goals.',
 'Medication Compounding & Preparation':'Provide the required medication formulation when ordinary supply alone does not satisfy the authorised preparation need.',
 'Service Delivery Preparedness Management':'Recognise whether the whole service can actually operate in its intended context.',
 'Governed Information Operations':'Keep information identity, meaning, context, state and retrieval compatible across diverse healthcare uses.',
 'Coordinated Activity & Interaction':'Make requests, event participation, work and human interaction progress according to their different contracts.',
 'Trust, Accountability & Assurance':'Keep permission, attribution, operational integrity and independent confidence distinguishable and usable together.',
 'Active Information Availability':'Provide responsive managed use without promoting active representation into durable truth.',
 'Authoritative Durable State Establishment':'Give managed change an explicit committed state and authoritative progression basis.',
 'Binary Content Association & Resolution':'Make logical information and its usable payloads consistently associable while retaining custody boundaries.',
 'Operation Policy Resolution':'Determine the effective permitted action under the applicable identity, information and temporal criteria.',
 'Operation-Bound Policy Enforcement':'Ensure an operation cannot proceed merely because its caller omitted or bypassed a voluntary policy check.',
 'Information Attribution & Lineage':'Explain the information-significant origin and transformation of an assertion.',
 'Governed Activity Audit Evidence':'Preserve required significant action evidence under its proper capture authority.',
 'Operational Fulfilment Definition':'Keep the semantic promise of an undertaking separate from the machinery that executes it.',
 'Governed Activity Execution Control':'Progress accountable operational work without equating dispatch, execution and successful outcome.',
 'Entity-Relative Operational Progression':'Maintain a coherent operational reference frame as one entity reacts to successive inputs.',
 'Information Authority Qualification':'Keep source credibility explicit even when the technical handling of competing assertions is equally valid.'
}
for e in E.values():
    if e['architectural_status']=='Proposed' and e['type'] in ('capability','enterprise_capability'):
        assert e['name'] in INTENTS, ('Missing authored capability intent',e['name'])
        e['intent']=INTENTS[e['name']]

# Structural identity allocation: semantic codes are category-qualified where
# new; v0.1 codes/paths remain unchanged unless an actual proposed relocation occurs.
def allocate(e, stack=None):
    if e.get('_allocated'):
        return e['canonical_id']
    stack = stack or set()
    assert e['key'] not in stack, 'Cyclic responsibility context'
    context = e.get('parent_key') or e.get('owner_key')
    prefix = allocate(E[context], stack | {e['key']}) + '.' if context else ''
    if e['type'] in ('capability', 'enterprise_capability'):
        code = e['local_id'] or ('BEC-' if e['category']=='business_enabling' else 'BC-') + slug(e['name']).upper()
        if e['type'] == 'enterprise_capability' and e['baseline_version']:
            code = e['reference_id']
        token = e['tier'] + '-' + code
    elif e['type'] == 'feature':
        token = e['local_id'] if e['local_id'].startswith('FEAT-') else 'FEAT-' + e['local_id']
    else:
        code = e['local_id'] or slug(e['name']).upper()
        token = {'function':'FN-', 'service':'SV-', 'process':'PR-'}[e['type']] + code
    current = prefix + token
    old = e.get('canonical_id')
    if old and old != current:
        e['id_aliases'] = sorted(set(e['id_aliases'] + [old]))
    e['canonical_id'] = current
    e['local_id'] = code if e['type'] != 'feature' else e['local_id']
    e['structural_id_status'] = 'Established' if e['category'] == 'business' and e['architectural_status']=='Canonical' else 'Proposed'
    e['unresolved'] = [u for u in e['unresolved'] if u not in ('canonical_id','code_allocation','ancestry')]
    e['_allocated'] = True
    return current

for e in E.values():
    allocate(e)
    # Existing directly owned behaviour retains established responsibility even
    # though its newly completed ancestry/structural identifier is Proposed.
    if e['type'] in ('function','service','process') and e['baseline_version']:
        e['placement_status'] = 'Established'
        e['ancestry_status'] = 'proposed'
for e in E.values():
    e.pop('_allocated', None)
    parent = e.get('parent_key'); owner = e.get('owner_key')
    e['parent_identifier'] = E[parent]['reference_id'] if parent else None
    e['owner_identifier'] = E[owner]['reference_id'] if owner else None

M['placement_assertions'] = [dict(element=e['key'], parent=e.get('parent_key') or e.get('owner_key'),
    assertion_type='root' if e['tier']=='CT1' else 'capability_parent' if e.get('parent_key') else 'feature_frame' if e['type']=='feature' else 'behaviour_owner',
    tier=e['tier'], direct_status=e['placement_status'], complete_path_status=e['structural_id_status'],
    established_responsibility_owner=e.get('responsibility_owner_key'),
    rationale=e.get('placement_rationale', 'Preserved established responsibility placement; completing proposed ancestry does not change the direct owner.')) for e in E.values()]

# Explicit evidence samples for downstream recurrence analysis. These are
# supporting responsibility records, not an invented per-Function EC mapping.
for r in R:
    if r['status']=='Proposed' and r['type'] in ('enabled_by','enterprise_enabled_by'):
        context = r['from'] if r['type']=='enterprise_enabled_by' else r['to']
        r['supporting_responsibility_elements'] = [e['key'] for e in E.values() if
            e.get('owner_key')==context and e['type'] in ('feature','function','service')][:6]
        r['responsibility_boundary'] = 'Enablement only; no decomposition, implementation equivalence, inherited descendant mapping, source-ownership transfer or domain-authority transfer.'

M['identity_policy'] = 'Reference IDs and labels are immutable; structural IDs express current proposed placement. Historical allocated IDs resolve directly to the same element and are never reused.'
M['canonical_id_policy'] = {'grammar': 'Metamodel §8 typed context path; category-qualified semantic codes for new allocations.',
 'ec_extension': 'Proposed application of CT1/CT2/CT3 grammar to EC hierarchy. EC-01..EC-14 remain stable Reference IDs and direct ID aliases.',
 'standing': 'Allocation does not adjudicate Proposed placement. Original 190 canonical BC structural IDs remain established.',
 'code_source': 'Explicit initial semantic allocation in compose.py, frozen in identity-lock.json; never recycle a used ID.'}
M['baseline_metadata'] = {k:copy.deepcopy(ORIGINAL[k]) for k in
    ('identity_policy','navigation_aid','canonical_id_policy','explanatory_prose')}
M['navigation_aid'] = 'The frozen v0.1 substrate, current RADS corpus and explicitly authorised v0.3 prior reasoning are reconciled into this proposal. Candidate reasoning is evidence for Proposed assertions, not architectural authority. Superseded legacy architecture is excluded.'
M['explanatory_prose'] = {
    'standing':'Baseline explanation retains its v0.1 standing. New authored definitions, overviews, intent and boundaries are explicitly Proposed Step 2 architecture.',
    'authored_proposal_source':'tools/compose.py',
    'reviewable_proposal_catalogue':'catalogue/proposals.md',
    'baseline_files':{'../'+p:sha for p,sha in ORIGINAL['explanatory_prose']['files'].items()},
    'reconciliation':'v0.3-reconciliation.json',
    'reverse_derivation_provenance':'derivation-provenance.json'}
M['catalogue_contexts_role'] = 'Frozen v0.1 descriptive/contextual records; current Proposed identity/frame resolutions are recorded independently in catalogue_context_resolutions.'
M['catalogue_context_resolutions'] = [{'context': k, 'element': v, 'assertion_status':'Proposed',
 'rationale':'Separate CT3 identity for materially distinct entity responsibilities; otherwise the contextual description is encompassed by the existing canonical CT3 responsibility.'} for k,v in CONTEXT_MAP.items()]
M['sources'] = copy.deepcopy(ORIGINAL['sources'])


def save(name, value):
    text = json.dumps(value, indent=2, ensure_ascii=False) + '\n'
    p = BASE / name
    if name in ('semantic-baseline.json','v0.1-fingerprints.json') and p.exists():
        assert p.read_text() == text, 'Immutable baseline changed: ' + name
    if '--check' in sys.argv:
        assert p.read_text() == text, 'Composed artefact drift: ' + name
    else:
        p.write_text(text)


if __name__ == '__main__':
    # Source content actually inspected/used, kept separate from canonical source
    # transcription coverage. Fingerprints preserve evidence, not approval.
    used = {str(p.relative_to(ROOT)) for p in (ROOT / 'docs/markdown').rglob('*.md')
            if any(part in p.parts for part in ('01-motivation','02-strategy','03-business-architecture','04-information-architecture','05-information-systems-architecture','governance'))}
    corpus = [{'path':p, 'sha256':hashlib.sha256((ROOT/p).read_bytes()).hexdigest(),
               'role':'Current architectural corpus; source standing and unresolved matters remain applicable'} for p in sorted(used)]
    corpus.append({'path':V03PATH, 'sha256':hashlib.sha256((ROOT/V03PATH).read_bytes()).hexdigest(),
                   'role':'Explicitly authorised prior reasoning, reconciled concept by concept; not architectural authority'})
    save('corpus-register.json', corpus)
    save('model.json', M)
    lock = {e['reference_id']: {k:e[k] for k in ('key','latex_label','local_id','canonical_id','id_aliases')} for e in E.values()}
    # Existing locks cannot be silently reallocated by running composition.
    if (BASE/'identity-lock.json').exists():
        assert json.loads((BASE/'identity-lock.json').read_text()) == lock, 'Identity allocation changed; explicit alias-preserving model maintenance required'
    save('identity-lock.json', lock)
    semantic = {e['reference_id']:{k:e[k] for k in ('key','latex_label','canonical_name','architectural_status','sources','governed_definition_latex','baseline_placement')} for e in M['elements'] if e['baseline_version']}
    save('semantic-baseline.json', semantic)
    frozen_files = {str(p.relative_to(V01)):hashlib.sha256(p.read_bytes()).hexdigest() for p in V01.rglob('*')
                    if p.is_file() and not any(x in p.relative_to(V01).parts for x in ('v0.2-draft','build','__pycache__'))}
    save('v0.1-fingerprints.json', frozen_files)
    changes = []
    original_by_key = {e['key']:e for e in ORIGINAL['elements']}
    for e in E.values():
        before = original_by_key.get(e['key'])
        fields = ['name','tier','parent_key','owner_key','canonical_id']
        changes.append({'reference_id':e['reference_id'], 'change':'Added' if before is None else 'Preserved/enriched',
                        'element_status':e['architectural_status'], 'placement_status':e['placement_status'],
                        'changes':{k:{'before':before.get(k) if before else None,'after':e.get(k)} for k in fields if before is None or before.get(k)!=e.get(k)},
                        'id_aliases':e['id_aliases']})
    save('change-ledger.json', {'version':'0.2-draft','elements':changes,
         'new_relationships':[r for r in R if r not in ORIGINAL['relationships']],
         'preserved_relationships':len(ORIGINAL['relationships']),
         'note':'Baseline unresolved associations remain non-edges; new Proposed assertions do not retroactively adjudicate the RAD.'})
    print('Composed',len(E),'elements;',collections.Counter(e['architectural_status'] for e in E.values()))
