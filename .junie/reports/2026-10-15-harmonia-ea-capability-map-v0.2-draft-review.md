<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia EA Capability Map 0.2-draft — architectural decision review

**Outcome:** Step 2 is an explicitly Proposed complete capability architecture,
ready to act as the working hypothesis for Application Component derivation.
The publication is at
[`docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/`](../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/).
The accepted v0.1 publication and governed Markdown remain unchanged.

The most consequential bets are the seven-root BEC hierarchy, the integrated
Enterprise hierarchy, the distinction between source authority and technical
state, and collective service readiness. Review those before treating the model's
downstream component implications as persuasive evidence about the existing
component map. Canonical responsibilities remain Canonical throughout; placements,
new responsibilities and new support relationships have their own status.

## Prioritised architectural decisions

### HIGH — D01: construct a BEC hierarchy from responsibility

The five views no longer determine hierarchy. Seven CT1s distinguish relatively
enduring entity information; care transactions/documentation; care-context
integration; operational coordination; governed information; human interaction/work;
and independent assurance. Twenty-eight CT2s and 102 CT3s frame every Feature.

Entity administration remains CT2 because practitioner identity, role, affiliation,
engagement, place definition and service mapping are genuinely different abilities.
Twenty-one named entity descriptions acquire Proposed CT3 identities, alongside
the four established Client Administration responsibilities. Elsewhere the existing
responsibility is usually the finest required capability frame, so it becomes CT3
under a meaningful proposed CT2. A second nearly synonymous CT3 is not made merely
because the old contextual view had a nested heading.

The important bet is the distinction between care-context integration and operational
care-setting coordination. Inpatient care context supplies clinical briefing and
information integration; Ward Operations manages local operational flow/capacity.
They can serve the same encounter without sharing responsibility. Likewise,
Clinical Record Administration, Patient Clinical Record and Associated Clinical
Binary Content Enablement retain document lifecycle, longitudinal synthesis and
payload-association responsibilities respectively.

### HIGH — D02: integrate EC-01–EC-14 into one architecture

The canonical fourteen responsibilities become CT2s beneath three Proposed CT1s:
Governed Information Operations; Coordinated Activity & Interaction; and Trust,
Accountability & Assurance. Their IDs, exact governed descriptions, exclusions and
meaning remain intact. This is an organising architecture, not three application
components. Other than selected useful decomposition, branches intentionally stop
at CT2. There is no universal requirement to fill Enterprise CT3s.

Selected EC-04 specialisations separate active availability, authoritative durable
establishment and binary-content association/resolution. EC-06 separates effective
policy resolution from operation-bound enforcement. EC-07 separates information
lineage from significant-activity audit evidence. EC-10 separates fulfilment
definition, governed execution control and entity-relative progression. These are
reusable responsibilities derived from recurrent information/activity concerns;
they do not reproduce a list of Mneme/Mnemosyne/Ponos/etc. components.

### HIGH — D03: propose Information Authority Qualification

This new Enterprise CT2 captures the standing of an assertion independently of
transport, persistence, version freshness and execution success. It recurs across
identity correlations, clinical synthesis, governed reference content and assurance
evidence. EC-13 governs its nomenclature, EC-07 records attribution, EC-06 applies
policy, and EC-14 evaluates assurance concerns; none expressly substitutes for
preserving the assertion's source-authority/credibility standing under AX-06.

**Review bet:** this might instead become a distinct EC-01 or EC-04 specialisation.
The proposal gives it peer responsibility so that downstream design cannot lose
the semantic distinction inside generic information machinery. It is not a truth
engine, universal conflict arbiter or clinical-adjudication mandate.

### HIGH — D04: place collective service readiness explicitly

The existing Candidate Health Service Operational Readiness is placed at BC CT3
under a Proposed Service Delivery Preparedness Management CT2 in Care Access &
Coordination. The offering-deliverability question is treated as an access
prerequisite, separate from scheduling, aggregate capacity, care-place readiness,
service alignment and a service's published availability. A Proposed BEC and
Feature assemble the dependency evidence needed for a qualified readiness view.

**Low confidence:** Health Service Direction & Stewardship is a credible alternative
for standing site/service readiness. That alternative remains recorded. The
Candidate element is not promoted and the canonical source's prohibition on
unadjudicated placement remains visible in the source appendix. This publication
is the explicit proposal for review, not an assertion that adjudication occurred.

The boundary between assembling a readiness view and possessing authority to make
a service go/no-go decision deserves particular review. Resource and source-readiness
owners retain their responsibilities; incomplete evidence is not treated as ready.

### HIGH — D05: preserve clinical, operational and source authority

BC-to-BEC support provides bounded information/coordination enablement, never an
automatic implementation or runtime dependency. New relationships explicitly
preserve the authority of clinical care, diagnostic interpretation, clinical
clearance, privilege, source rosters/bookings, research approvals, public-health
decisions and governance approvals. Aggregate Service Capacity Management consumes
local capacity without taking the originating capacity responsibility.

The three care-context CT2s are an organisation of integration purposes, not a
mutually exclusive taxonomy of care. Acute/emergency/inpatient and specialist/
longitudinal dimensions overlap. Existing acute-capture, inpatient-progression and
screening-recall sufficiency adjudications remain intact: their composition does
not justify specialised capture engines, clinical work management or automatic
replacement Feature-to-Function links.

### HIGH — D06: make recovery, evidence and assurance different abilities

Mandatory audit capture retains its specifically established intrinsic platform
authority, distinct from the identity being audited. It is not generalised to
other operations. Information lineage records meaningful attribution rather than
all runtime mechanics. Independent assurance retains the five established
Functions, distinct assessment/adjudication and explicit insufficiency. EC-12
operational assurance does not absorb EC-14 or acquire subject-management authority.

Entity-relative progression, behaviour failure, controlled administrative recovery
and indeterminate-effect reconciliation receive explicit proposed Features.
Failure does not prove no effects. The new reconciliation Process orders observation
basis, effect reconciliation and safe-continuation determination; a justified
disposition may remain uncertain. It is not a universal retry workflow or lifecycle.

### MEDIUM — D07: add specific Feature frames rather than recurring filler

The 41 additions include order outcomes, preliminary/corrected diagnostic handling,
encounter history, care-goal/plan progression, historical reconstruction, binary
resolution, mutable reference revisions, definition/schema/binding/profile
governance and six distinct local-capacity contexts. These local-capacity Features
are justified by existing explicit Business responsibility, with different clinic,
ward, theatre, ED, outpatient and mobile meanings; they are not symmetry generated
from neighbouring Feature counts.

Three proposed assurance Features frame the existing three canonical capability
responsibilities without inventing additional assurance Functions. Functions remain
directly Capability-owned where established. A proposed realisation relationship
does not silently rewrite the owning capability or imply one Function per Feature.

Some new Features deliberately have no new Function or Service: the responsibility
may be realised by composition or already established behaviour, and a contract is
not minted simply to populate a chain. All remain described and CT3-framed.

### MEDIUM — D08: represent secondary information purposes and BC-17 support

Authorised research provision, population notification and service-planning
information provision receive distinct BEC identities under Secondary Health
Information Use Enablement. Their different purpose/obligation boundaries prevent
a generic export utility from acquiring research, public-health or commissioning
authority. Governance Requirement Publication Support proposes a narrow
information-only response to the unresolved BC-17 enablement concern.

**Low confidence:** governance publication may ultimately be composition through
governed datasets and general information access rather than a separate BEC.
The current proposal keeps its requirement-authority boundary visible for review.
No approval role, requirement authoring mandate or universal publication workflow
is invented.

### MEDIUM — D09: reconcile v0.3 instead of importing its hierarchy

All 942 primary concepts and 277 captured behaviour occurrences have source-line
locators and individual decisions. The useful person/registry subresponsibilities
are retained or identified. Specific deltas such as referral routing, encounter
history, device association lineage and care-goal representation are refined.
Repeated generic Context Establishment, State Monitoring, Exception Management
and Information Retrieval labels are retained as scoped concerns/reuse evidence
where their old prose describes no independent Feature outcome.

The separate derived Enterprise hierarchy and "existing EC coexistence" branch
are integrated. Correlation does not become a standalone Enterprise responsibility;
context and state remain different ECs; resilience and independent assurance remain
different ECs. Candidate Enterprise Feature rows become recurrence evidence within
EC responsibilities because Harmonia Features require BEC CT3 framing.

The old Domain Health Information Services business grouping is rejected as
service/component shaped. Provider directory, LHR and immunisation integration
concerns remain in their meaningful canonical BECs. R1/R2 Person Identity does
not acquire an EMPI merely because the old candidate listed one; probabilistic
master reconciliation remains an external/uncommitted R3 concern. Placeholder rows
saying no decomposition exists are not treated as Features or Functions.

### MEDIUM — D10: make naming and credential distinctions reviewable

The BEC's current display name becomes **Health Information Exchange Enablement**,
with Proposed naming status. The Canonical BC remains Health Information Exchange.
The BEC retains its Reference ID, LaTeX label, established name, definition and
name history. This achieves uniqueness in the proposed model without asserting
that RADS accepted the rename or that the two responsibilities are identical.

Clinical Qualification Management remains professional/clinical qualification
checking. Credential Management remains physical badge/operational-access checking.
The inherited Operational Credential Check Feature retains its established clinical
qualification definition and owner despite its confusing name. Practitioner Role
resolution supplies context but grants neither clinical nor operational privilege.

### LOW — D11: structural identity expresses proposed context

Every current element has a Reference ID, LaTeX label and unique structural ID.
The 190 established BC structural IDs remain unchanged. All fourteen former EC
structural tokens become direct historical aliases of those same identities in
the proposed hierarchy. No alias chain or reuse occurs. New BEC/EC codes are
category-qualified semantic tokens. The extension of metamodel §8 typed ancestry
to Enterprise hierarchy is explicitly Proposed.

The three existing Candidate concerns retain Candidate status and receive Proposed
placement: readiness, device maintenance and sharing-programme management.
Maintenance membership remains in tension with the approved seven-child device
set. Sharing-programme semantics remain weakly evidenced; its new explanatory
definition is explicitly Proposed, never a retrospectively manufactured governed
definition. The 22 unresolved baseline associations remain unresolved non-edges.

## Downstream derivation guidance

Use `model.json` as the current explicit architecture assumption while retaining
its assertion status. Derive components from cohesive responsibility, state,
authority and contract boundaries; use enablement as support evidence rather than
containment or a mandatory runtime call graph. Do not mechanically turn CT1s, ECs,
Features or the proposed named Services into components or deployments.

The actor/domain boundary, active-versus-durable distinction, source-credibility
qualification, independent assurance, binary custody distinction and recovery
uncertainty are particularly important inputs. If the resulting component map
differs from current Harmonia components, retain that as architectural evidence.

## Preserved source tensions

- The v0.1 source detail records unresolved BEC ancestry, EC flat presentation and
  unestablished derivations. Step 2 supplies explicit Proposed answers while those
  baseline statements remain unchanged and separately labelled.
- Earlier Strategy assurance derivation text still says downstream Functions,
  Services and Processes remain unestablished; current Domain03 explicitly
  establishes them. Both texts are preserved. The five canonical Functions and
  established Domain03 exposure/progression relationships are not demoted.
- Some Domain05 overview programme-status statements lag the bounded B2.1 account.
  No architecture authority or programme-completion claim is derived from which
  prose happens to look newer.
- Clinical-context composition sufficiency and the inherited qualification Feature
  naming tension remain visible. New Features do not silently reinterpret them.

## Canonical Documentation Assessment

The accepted v0.1 publication is outside RADS and is explicitly authorised as the
frozen semantic substrate for this task. The old v0.3 candidate is inside the RADS
scratchpad but remains prior proposed reasoning; its location does not promote it.
Both are reconciled against current governed Strategy, Business, Information,
Information Systems, DesignNote and axiom material. Superseded legacy architecture
is not used as authority, and no original legacy section is rewritten.

The resulting book/model intentionally remains outside `/docs/markdown`.
No proposed architecture was migrated into governed Markdown. If accepted later,
capability/Feature/identity/support changes belong in Domains02–03, the proposed
authority/custody distinctions may affect Domain04, and the later component
derivation belongs in Domain05. A separate authorised reconciliation is needed;
this task does not declare those domains complete.

<!-- MODEL INVENTORIES FOLLOW: generated from the committed explicit proposal. -->

## Proposed BEC hierarchy

C = Canonical responsibility; P = Proposed responsibility. Every placement in this BEC hierarchy is Proposed.

### Healthcare Entity Information Enablement (P)

| CT2 responsibility | CT3 responsibility frames |
| --- | --- |
| Client Administration (C) | Person Identity (C); Healthcare Subject (C); Client Relationship (C); Client Privacy (C) |
| Provider Administration (C) | Practitioner (P); Practitioner Role (P); Provider Relationship (P); Provider Engagement (P) |
| Organisation Administration (C) | Healthcare Organisation (P); Organisational Structure (P); Organisation Contact (P) |
| Location Administration (C) | Healthcare Location (P); Location Structure (P); Care-Place Definition (P) |
| Health Service Administration (C) | Healthcare Service Definition (P); Service Provision Relationship (P); Service Availability Definition (P); Service Eligibility Definition (P) |
| Health Product Administration (C) | Health Product Definition (P); Product Formulary & Catalogue (P); Product Batch & Serial Tracking (P) |
| Clinical Device Administration (C) | Device Definition (P); Device Instance (P); Device Association (P); Device Connectivity (P) |

### Care Administration Information Enablement (P)

| CT2 responsibility | CT3 responsibility frames |
| --- | --- |
| Care Access Transaction Enablement (P) | Client-Centred Administration (C); Referral Administration (C); Scheduling Administration (C) |
| Care Fulfilment Administration Enablement (P) | Episode & Encounter Administration (C); Order Administration (C); Diagnostic Administration (C); Medication Administration (C); Procedure Administration (C) |
| Care Documentation & Communication Enablement (P) | Clinical Record Administration (C); Care Plan Administration (C); Clinical Communication Administration (C) |
| Care Financial Information Enablement (P) | Funding & Coverage Administration (C); Clinical Charging & Claims Administration (C) |

### Care Context Integration Enablement (P)

| CT2 responsibility | CT3 responsibility frames |
| --- | --- |
| Longitudinal & Community Care Integration (P) | Primary Care (C); Preventive Care (C); Community Care (C); Outreach Care (C); Remote & Virtual Care (C) |
| Facility & Episodic Care Integration (P) | Acute Care (C); Emergency Care (C); Inpatient Care (C); Ambulatory Care (C); Surgical & Procedural Care (C) |
| Specialist Clinical Context Integration (P) | Clinical Assessment (C); Diagnostic Services (C); Therapeutic Services (C); Medication Therapy (C); Allied Health (C); Rehabilitation (C) |

### Care Operations Coordination Enablement (P)

| CT2 responsibility | CT3 responsibility frames |
| --- | --- |
| Care Setting Operations Enablement (P) | Clinic & Practice Operations (C); Ward Operations (C); Theatre Operations (C); Emergency Department Operations (C); Outpatient Operations (C) |
| Care Resource & Capacity Coordination (P) | Bed & Care-Place Management (C); Clinical Resource Management (C); Service Capacity Management (C); Service Offering Readiness Coordination (P) |
| Operational Workforce Coordination (P) | Clinical Workforce Management (C); Clinical Rostering (C); Mobile Staff Management (C); On-Call Management (C); Work Allocation & Dispatch (C) |
| Workforce Qualification & Development Enablement (P) | Clinical Training & Education (C); Clinical Competency Management (C); Clinical Qualification Management (C); Clinical Supervision (C); Credential Management (C) |
| Care Movement & Transition Coordination (P) | Patient Transport (C); Clinical Logistics Coordination (C); Discharge Management (C) |

### Governed Health Information Enablement (P)

| CT2 responsibility | CT3 responsibility frames |
| --- | --- |
| Clinical Information Stewardship Enablement (P) | Patient Clinical Record (C); Associated Clinical Binary Content Enablement (P); Information Authority Representation (P) |
| Health Information Connectivity Enablement (P) | Health Information Exchange Enablement (C); Health Information Access (C); Health Information Communication (C) |
| Semantic & Reference Knowledge Enablement (P) | Clinical Knowledge Services (C); Information Design Governance (C); Governed Reference Dataset Administration (P) |
| Information Protection & Accountability Enablement (P) | Health Information Control (C); Significant Activity Audit Capture (P); Information Lineage Stewardship (P); Governance Requirement Publication Support (P) |
| Secondary Health Information Use Enablement (P) | Authorised Research Information Provision (P); Population Health Notification Enablement (P); Service Planning Information Provision (P) |

### Human Collaboration & Work Enablement (P)

| CT2 responsibility | CT3 responsibility frames |
| --- | --- |
| Collaborative Interaction Enablement (P) | Clinical Collaboration (C); General Collaboration (C); Presentation Services (C) |
| Operational Activity & Temporal Enablement (P) | Workflow & Activity Coordination (C); Calendar Management (C); Entity-Specific Operational Coordination (P); Governed Operational Activity Design (P) |

### Independent Service Assurance Enablement (P)

| CT2 responsibility | CT3 responsibility frames |
| --- | --- |
| Assurance Specification Enablement (P) | Assurance Design (C); Assurance Criteria Management (C) |
| Independent Assurance Evaluation Enablement (P) | Governed Assurance (C) |

## Proposed Enterprise hierarchy

| CT1 (all Proposed) | CT2 responsibility | CT3 responsibilities (all Proposed) |
| --- | --- | --- |
| Governed Information Operations | EC-01 — Managed Entity & Relationship (Canonical) | Intentionally undecomposed |
| Governed Information Operations | EC-02 — Context Management (Canonical) | Intentionally undecomposed |
| Governed Information Operations | EC-03 — Managed State & Lifecycle (Canonical) | Intentionally undecomposed |
| Governed Information Operations | EC-04 — Information Management (Canonical) | Active Information Availability; Authoritative Durable State Establishment; Binary Content Association & Resolution |
| Governed Information Operations | EC-05 — Search & Discovery (Canonical) | Intentionally undecomposed |
| Governed Information Operations | EC-13 — Semantic Governance & Conformance (Canonical) | Intentionally undecomposed |
| Coordinated Activity & Interaction | EC-08 — Interoperability & Exchange (Canonical) | Intentionally undecomposed |
| Coordinated Activity & Interaction | EC-09 — Event & Subscription (Canonical) | Intentionally undecomposed |
| Coordinated Activity & Interaction | EC-10 — Activity & Execution (Canonical) | Operational Fulfilment Definition; Governed Activity Execution Control; Entity-Relative Operational Progression |
| Coordinated Activity & Interaction | EC-11 — Interaction & Experience (Canonical) | Intentionally undecomposed |
| Trust, Accountability & Assurance | EC-06 — Policy & Control (Canonical) | Operation Policy Resolution; Operation-Bound Policy Enforcement |
| Trust, Accountability & Assurance | EC-07 — Provenance & Traceability (Canonical) | Information Attribution & Lineage; Governed Activity Audit Evidence |
| Trust, Accountability & Assurance | EC-12 — Operational Assurance (Canonical) | Intentionally undecomposed |
| Trust, Accountability & Assurance | EC-14 — Service Guardian (Canonical) | Intentionally undecomposed |
| Trust, Accountability & Assurance | HCM-EC-information-authority-qualification — Information Authority Qualification (Proposed) | Intentionally undecomposed |

## New Proposed BC responsibilities

- **Clinical Finding Interpretation** — Interpret observed clinical findings and their significance for the individual care concern. Parent: Clinical Determination.
- **Diagnosis & Problem Determination** — Establish and qualify the clinical problems or diagnoses applicable to an individual. Parent: Clinical Determination.
- **Health Risk & Prognostic Determination** — Establish clinically meaningful risk and expected-course judgements for an individual. Parent: Clinical Determination.
- **Care Goal Definition** — Establish the intended outcomes and priorities against which an individual care response is planned. Parent: Individual Care Planning.
- **Care Response Planning** — Determine the intended clinical response and its sequencing in relation to agreed care goals. Parent: Individual Care Planning.
- **Medication Compounding & Preparation** — Prepare or compound a clinically required medication formulation under the applicable professional and product-governance mandate. Parent: Medication & Pharmacy Services.
- **Service Delivery Preparedness Management** — Establish whether service offerings can be delivered in their intended operating contexts through the combined standing of their dependencies. Parent: Care Access & Coordination.

The BEC and Enterprise hierarchy tables identify every new capability with P/Proposed standing. Existing three Candidate concerns have their placements and alternatives described in D04/D11.

## All 41 new Features

| CT3 frame | Proposed Feature | Semantic delta |
| --- | --- | --- |
| Person Identity | Identity Source & Correction Lineage | Retain source-attributed identity assertions and externally decided correction history. |
| Practitioner | Provider Directory Discovery & Resolution | Expose qualified practitioner discovery using identifiers, profile criteria and effective standing. |
| Device Association | Device Association Lineage | Preserve the attributed history and effective basis of device-to-subject/place associations. |
| Referral Administration | Referral Destination Coordination | Coordinate qualified referral destination resolution and convey the request under its applicable disposition contract. |
| Episode & Encounter Administration | Encounter History Retrieval | Expose qualified episode, movement and encounter-history context. |
| Order Administration | Order Outcome Association | Associate an externally established outcome with the applicable bounded order and its progression. |
| Diagnostic Administration | Diagnostic Preliminary & Corrected Report Handling | Receive, qualify and distribute preliminary, corrected and addendum diagnostic information with its relationship to prior reports. |
| Care Plan Administration | Care Goal & Plan Progression Representation | Represent externally agreed care goals, participants and plan changes with their effective standing. |
| Patient Clinical Record | Clinical Information Historical Reconstruction | Reconstruct what information Harmonia received, knew or managed at a relevant historical basis. |
| Associated Clinical Binary Content Enablement | Binary Content Resolution & Assembly | Resolve governed payload associations and retrieve or assemble the content required by a requesting information context. |
| Information Authority Representation | Information Authority & Source Standing Representation | Attach and preserve qualified source-authority and credibility standing for managed assertions. |
| Governed Reference Dataset Administration | Governed Dataset Content Revision | Review, accept and expose authorised changes to mutable reference content with effective revision standing. |
| Significant Activity Audit Capture | Mandatory Significant Activity Audit Capture | Capture required significant activity evidence using the specifically governed authoritative audit-capture boundary. |
| Information Lineage Stewardship | Information Transformation Lineage | Record meaningful source-to-derived information relationships and transformation attribution. |
| Health Information Control | Trusted Identity Context Intake | Accept and qualify externally authenticated identity context for Harmonia-governed operations. |
| Health Information Control | Governed Policy Assignment & Revocation | Manage approved permission criteria and identity/role assignments, including temporal applicability and revocation. |
| Health Information Control | Operation-Bound Authorisation Enforcement | Apply effective policy at the governed action boundary and reject actions lacking sufficient authority. |
| Information Design Governance | Information Definition & Schema Governance | Govern structural definitions, data dictionaries and constraint meaning. |
| Information Design Governance | Terminology Binding Governance | Govern how information definitions are bound to applicable terminology and value sets. |
| Information Design Governance | Exchange Profile Governance | Govern the semantic and conformance constraints of an interoperability profile. |
| Entity-Specific Operational Coordination | Entity-Relative Activity Coordination | Sequence applicable behaviours for an identifiable entity input while preserving its operational reference frame. |
| Entity-Specific Operational Coordination | Entity Behaviour Failure & Recovery Governance | Apply explicit behaviour-failure dispositions and controlled, authorised resumption for entity-relative processing. |
| Governed Operational Activity Design | Operational Activity Context Definition | Define the anticipated information context and expected fulfilment basis of an operational undertaking. |
| Governed Operational Activity Design | Activity Outcome & Continuation Rules | Define how output roles, expected outcomes and continuation conditions are interpreted for an undertaking. |
| Workflow & Activity Coordination | Uncertain Activity Outcome Reconciliation | Reconcile indeterminate activity effects through authoritative observation before establishing a safe next disposition. |
| Workflow & Activity Coordination | Controlled Administrative Recovery | Perform authorised recovery as identifiable governed activity through normal execution and evidence boundaries. |
| Service Offering Readiness Coordination | Service Readiness Dependency Assessment | Associate time- and context-qualified dependency evidence to establish a bounded readiness view for an offering. |
| Authorised Research Information Provision | Authorised Research Release Assembly | Assemble and qualify the existing information allowed by a particular authorised research purpose. |
| Population Health Notification Enablement | Population Notification Obligation Tracking | Track the relation between an applicable notification obligation, the supplied source evidence and observed notification disposition. |
| Service Planning Information Provision | Demand & Capacity Planning Projection | Project qualified observed demand, capacity and utilisation for authorised planning consumers. |
| Governance Requirement Publication Support | Governance Requirement Version Publication | Publish attributable versions and applicability of externally approved governing requirements. |
| Clinical Collaboration | Collaboration Interaction Capture Qualification | Qualify when a defined collaboration interaction creates a new governed information or activity consequence. |
| Assurance Design | Assurance Disposition & Design Modelling | Model how satisfaction of an applicable governed concern will be independently established. |
| Assurance Criteria Management | Temporal Assurance Criteria Stewardship | Manage identifiable assurance criteria and their relevant temporal and version applicability. |
| Governed Assurance | Independent Evidence Assessment & Adjudication | Independently establish assurance context, assess evidence and adjudicate findings under applicable criteria. |
| Clinic & Practice Operations | Clinic Session Capacity Management | Represent available, committed and utilised session capacity under clinic-specific constraints. |
| Ward Operations | Ward Care Capacity Management | Represent ward care capacity in relation to staffing, care-place suitability and patient dependency. |
| Theatre Operations | Theatre Session Capacity Management | Represent available and committed procedural-session capacity with its resource dependencies. |
| Emergency Department Operations | Emergency Treatment Capacity Management | Represent current emergency treatment capacity under space, staffing and acuity constraints. |
| Outpatient Operations | Outpatient Appointment Capacity Management | Represent usable outpatient session capacity from booking, staffing and local flow information. |
| Mobile Staff Management | Mobile Service Capacity Management | Represent mobile service capacity with travel, coverage, work commitment and availability context. |

## Material Function, Service and Process additions

| Type | Proposed element / owning responsibility | Meaning |
| --- | --- | --- |
| Function | Resolve Associated Binary Content / Associated Clinical Binary Content Enablement | Resolve logical content associations to an authorised, custody-qualified retrieval basis. |
| Function | Assemble Associated Binary Content / Associated Clinical Binary Content Enablement | Retrieve and assemble the requested logical content from its permitted constituent payloads. |
| Service | Associated Binary Content Retrieval / Associated Clinical Binary Content Enablement | Allow information-consuming capabilities to request resolution and retrieval of associated content. |
| Function | Qualify Information Authority Standing / Information Authority Representation | Associate source and credibility qualifications with an information assertion under applicable governed semantics. |
| Function | Accept Governed Dataset Revision / Governed Reference Dataset Administration | Apply an explicitly authorised content revision and its effective standing through governed information-state progression. |
| Service | Governed Reference Dataset Content Query / Governed Reference Dataset Administration | Provide authorised consumers with approved content at an identified revision and applicability basis. |
| Function | Capture Required Significant Audit Evidence / Significant Activity Audit Capture | Establish required significant activity evidence under the governed authoritative audit-capture responsibility. |
| Service | Mandatory Audit Evidence Capture / Significant Activity Audit Capture | Allow governed-operation boundaries to require authoritative capture of significant activity evidence. |
| Function | Record Significant Information Lineage / Information Lineage Stewardship | Associate a managed assertion with its attributed sources and information-significant transformation. |
| Function | Coordinate Entity-Relative Input / Entity-Specific Operational Coordination | Select interested behaviours and progress one entity-relative input with explicit participation and effect standing. |
| Service | Entity Operational Input Submission / Entity-Specific Operational Coordination | Allow authorised domain coordinators to submit an entity-relative stimulus or requested operational effect. |
| Function | Maintain Operational Fulfilment Definition / Governed Operational Activity Design | Establish and revise the declared context needs, expected outcome basis and housekeeping of an operational undertaking. |
| Function | Reconcile Indeterminate Activity Effects / Workflow & Activity Coordination | Inspect authoritative observations to establish, qualify or retain uncertainty about a previously indeterminate activity effect. |
| Function | Establish Activity Effect Observation Basis / Workflow & Activity Coordination | Associate the applicable handoff contract and authoritative observations with an indeterminate operational undertaking. |
| Function | Determine Reconciled Activity Continuation / Workflow & Activity Coordination | Establish the permitted operational continuation from reconciled effect standing and applicable authorisation. |
| Process | Indeterminate Activity Outcome Reconciliation Process / Workflow & Activity Coordination | Order the observation, effect qualification and authorised disposition of a materially indeterminate operational outcome. |
| Function | Assess Service Offering Readiness / Service Offering Readiness Coordination | Evaluate applicable dependency evidence to establish a context- and time-qualified service readiness view. |
| Service | Service Offering Readiness Query / Service Offering Readiness Coordination | Expose the qualified readiness view of a defined offering and operating context to operational planning consumers. |
| Function | Assemble Purpose-Qualified Research Release / Authorised Research Information Provision | Construct a release containing only existing information permitted for the explicitly authorised research purpose. |
| Service | Authorised Research Information Export / Authorised Research Information Provision | Provide an approved research consumer with purpose-qualified existing health information under its release contract. |

## New BC-to-BEC support proposals

These are grouped for review; the model preserves an individually typed Proposed edge for each pair. All preserve AX-18 and source-information ownership.

| BC | Supporting BECs | Bounded purpose |
| --- | --- | --- |
| Individual Care Delivery | Patient Clinical Record; Care Plan Administration | Care decisions require qualified longitudinal information and the representation of externally agreed plans; professional authority remains with care actors. |
| Care Access Management | Referral Administration; Scheduling Administration; Client-Centred Administration | Intake, referral disposition and booking synchronisation support access without deciding acceptance of clinical responsibility. |
| Care Scheduling & Capacity Management | Bed & Care-Place Management; Service Capacity Management | Qualified care-place state and local-capacity aggregation inform operational scheduling without taking source slot or staffing ownership. |
| Care Coordination & Transition | Discharge Management; Clinical Collaboration; Care Plan Administration | Operational readiness, shared plan context and discussion support transition while clinical clearance and responsibility transfer retain their source authority. |
| Care Support Logistics Management | Patient Transport; Clinical Logistics Coordination; Work Allocation & Dispatch | Physical movement and support work need operational requests, custody/progression evidence and eligible dispatch; clinical work remains externally governed. |
| Diagnostic Support Provision | Diagnostic Administration; Diagnostic Services; Order Administration | Requisition, report and outcome correlation supports diagnostic provision without interpreting specimens or images. |
| Medication & Pharmacy Services | Medication Administration; Medication Therapy | Source prescription, dispensing, administration and allergy/history information supports pharmacy activity without Harmonia prescribing or dispensing. |
| Clinical Technology Management | Clinical Device Administration; Clinical Resource Management | Device identity, associations and operational equipment context support technology use; maintenance, regulatory and lifecycle decisions retain their owners. |
| Health Information Management | Patient Clinical Record; Clinical Record Administration; Associated Clinical Binary Content Enablement | Document lifecycle, longitudinal synthesis and payload association support governed information without collapsing legal originals, projections and custody. |
| Clinical Knowledge Management | Clinical Knowledge Services; Governed Reference Dataset Administration | Governed terminology and approved reference content support knowledge use while external ontology authority remains explicit. |
| Health Information Standards Governance | Information Design Governance | Model, dictionary, profile and conformance responsibilities make approved standards operationally usable without replacing standards authority. |
| Clinical Semantics Governance | Information Design Governance; Clinical Knowledge Services | Definition bindings and terminology resolution support shared meaning; runtime coding and ontology approval remain distinct. |
| Health Information Exchange | Health Information Exchange Enablement; Health Information Communication | Harmonia coordinates bounded information exchange under standards contracts; organisational sharing agreements and domain content retain their authority. |
| Digital Communication Channel | Clinical Communication Administration; General Collaboration | Directed clinical message handling and operational dialogue support different communication purposes without making a conversation a clinical record. |
| Digital Identity & Access Governance | Health Information Control; Client Privacy | Trusted identity intake, effective policy and patient restrictions support governed access without owning external authentication or consent authority. |
| Digital Accountability & Trust | Significant Activity Audit Capture; Information Lineage Stewardship; Information Authority Representation | Attribution, activity evidence and source standing answer different accountability questions; evidence alone is not an assurance conclusion. |
| Research Information Support | Authorised Research Information Provision | Provides existing information under explicitly authorised research-use constraints without taking research governance or trial management. |
| Population Health Surveillance | Population Health Notification Enablement; Health Information Access | Source information and required notification progression support surveillance without originating population-health conclusions or statutory duties. |
| Health-System Planning | Service Planning Information Provision | Qualified observed demand and capacity evidence supports planning without determining commissioned capacity or service distribution. |
| Health Workforce Management | Provider Administration; Clinical Qualification Management; Clinical Rostering | Professional standing, qualified checks and source coverage information support workforce activity without managing employment or granting clinical privilege. |
| Healthcare Organisation & Service Structure Management | Organisation Administration; Location Administration; Health Service Administration | Represented entity identities and service mappings support structure decisions; mapped entity owners retain authority. |
| Financial & Commercial Services | Funding & Coverage Administration; Clinical Charging & Claims Administration | Coverage flags and activity/charging exports support responsible financial systems without financial governance or claims adjudication. |
| Health Service Governance | Governance Requirement Publication Support | Proposed bounded publication support makes the governing requirement available to consumers without granting the platform requirement approval authority. |
| Health Service Operational Readiness | Service Offering Readiness Coordination | Proposed evidence-based coordination supports collective deliverability while dependency owners retain control. |

## New BEC-to-EC enablement proposals

Review by recurrence family. The model records 194 individual Proposed contributions; they are neither inherited nor component allocations. The separately captured 38 approved assurance contributions and four explicit No Material Role constraints remain established.

| BEC responsibility contexts | EC contribution | Recurrence rationale |
| --- | --- | --- |
| Person Identity; Healthcare Subject; Provider Administration; Organisation Administration; Location Administration; Health Service Administration; Clinical Device Administration | Managed Entity & Relationship | Entity/profile resolution, qualified relationships and non-destructive lifecycle recur across persons, providers, organisations, places, services and devices. |
| Person Identity; Healthcare Subject; Provider Administration; Organisation Administration; Location Administration; Health Service Administration; Clinical Device Administration | Managed State & Lifecycle | Entity/profile resolution, qualified relationships and non-destructive lifecycle recur across persons, providers, organisations, places, services and devices. |
| Person Identity; Healthcare Subject; Provider Administration; Organisation Administration; Location Administration; Health Service Administration; Clinical Device Administration | Search & Discovery | Entity/profile resolution, qualified relationships and non-destructive lifecycle recur across persons, providers, organisations, places, services and devices. |
| Person Identity; Healthcare Subject; Provider Administration; Organisation Administration; Location Administration; Health Service Administration; Clinical Device Administration | Provenance & Traceability | Entity/profile resolution, qualified relationships and non-destructive lifecycle recur across persons, providers, organisations, places, services and devices. |
| Client Privacy; Health Information Control | Policy & Control | Consent-qualified use and governed action need effective policy, trusted context and attributable significant decisions. |
| Client Privacy; Health Information Control | Context Management | Consent-qualified use and governed action need effective policy, trusted context and attributable significant decisions. |
| Client Privacy; Health Information Control | Provenance & Traceability | Consent-qualified use and governed action need effective policy, trusted context and attributable significant decisions. |
| Referral Administration; Order Administration; Diagnostic Administration; Medication Administration; Procedure Administration; Episode & Encounter Administration | Context Management | Transactions bind clinical/service context, preserve distinct lifecycle and acknowledgements, exchange source information and coordinate assigned operational consequences. |
| Referral Administration; Order Administration; Diagnostic Administration; Medication Administration; Procedure Administration; Episode & Encounter Administration | Managed State & Lifecycle | Transactions bind clinical/service context, preserve distinct lifecycle and acknowledgements, exchange source information and coordinate assigned operational consequences. |
| Referral Administration; Order Administration; Diagnostic Administration; Medication Administration; Procedure Administration; Episode & Encounter Administration | Interoperability & Exchange | Transactions bind clinical/service context, preserve distinct lifecycle and acknowledgements, exchange source information and coordinate assigned operational consequences. |
| Referral Administration; Order Administration; Diagnostic Administration; Medication Administration; Procedure Administration; Episode & Encounter Administration | Activity & Execution | Transactions bind clinical/service context, preserve distinct lifecycle and acknowledgements, exchange source information and coordinate assigned operational consequences. |
| Clinical Record Administration; Patient Clinical Record; Care Plan Administration | Managed State & Lifecycle | Document supersession, longitudinal synthesis and plan revision share managed-state, preservation and attribution needs while keeping different content ownership. |
| Clinical Record Administration; Patient Clinical Record; Care Plan Administration | Information Management | Document supersession, longitudinal synthesis and plan revision share managed-state, preservation and attribution needs while keeping different content ownership. |
| Clinical Record Administration; Patient Clinical Record; Care Plan Administration | Provenance & Traceability | Document supersession, longitudinal synthesis and plan revision share managed-state, preservation and attribution needs while keeping different content ownership. |
| Health Information Access; Clinical Knowledge Services; On-Call Management | Search & Discovery | Information, terminology and on-call resolution all expose criteria-qualified discovery under policy; the discovered subjects remain different. |
| Health Information Access; Clinical Knowledge Services; On-Call Management | Policy & Control | Information, terminology and on-call resolution all expose criteria-qualified discovery under policy; the discovered subjects remain different. |
| Health Information Exchange Enablement; Health Information Communication; Clinical Communication Administration; Population Health Notification Enablement | Interoperability & Exchange | Addressed exchange, syndication and notifications require explicit contract/delivery standing and resilient observation rather than equating transport acknowledgement with outcome. |
| Health Information Exchange Enablement; Health Information Communication; Clinical Communication Administration; Population Health Notification Enablement | Event & Subscription | Addressed exchange, syndication and notifications require explicit contract/delivery standing and resilient observation rather than equating transport acknowledgement with outcome. |
| Health Information Exchange Enablement; Health Information Communication; Clinical Communication Administration; Population Health Notification Enablement | Operational Assurance | Addressed exchange, syndication and notifications require explicit contract/delivery standing and resilient observation rather than equating transport acknowledgement with outcome. |
| Primary Care; Acute Care; Emergency Care; Inpatient Care; Community Care; Outreach Care; Remote & Virtual Care; Preventive Care; Diagnostic Services; Medication Therapy | Context Management | Care contexts compose qualified information and boundary exchange; this recurrence does not require another record store or specialised capture engine in every setting. |
| Primary Care; Acute Care; Emergency Care; Inpatient Care; Community Care; Outreach Care; Remote & Virtual Care; Preventive Care; Diagnostic Services; Medication Therapy | Information Management | Care contexts compose qualified information and boundary exchange; this recurrence does not require another record store or specialised capture engine in every setting. |
| Primary Care; Acute Care; Emergency Care; Inpatient Care; Community Care; Outreach Care; Remote & Virtual Care; Preventive Care; Diagnostic Services; Medication Therapy | Interoperability & Exchange | Care contexts compose qualified information and boundary exchange; this recurrence does not require another record store or specialised capture engine in every setting. |
| Clinic & Practice Operations; Ward Operations; Theatre Operations; Emergency Department Operations; Outpatient Operations; Mobile Staff Management; Service Capacity Management | Context Management | Local flow/capacity changes need context-qualified state and notifications; common mechanisms do not transfer capacity semantics to a central owner. |
| Clinic & Practice Operations; Ward Operations; Theatre Operations; Emergency Department Operations; Outpatient Operations; Mobile Staff Management; Service Capacity Management | Managed State & Lifecycle | Local flow/capacity changes need context-qualified state and notifications; common mechanisms do not transfer capacity semantics to a central owner. |
| Clinic & Practice Operations; Ward Operations; Theatre Operations; Emergency Department Operations; Outpatient Operations; Mobile Staff Management; Service Capacity Management | Event & Subscription | Local flow/capacity changes need context-qualified state and notifications; common mechanisms do not transfer capacity semantics to a central owner. |
| Work Allocation & Dispatch; Patient Transport; Clinical Logistics Coordination; Discharge Management; Workflow & Activity Coordination | Context Management | Operational activity needs eligible progression, effect standing and recoverable execution under context; clinical work and source clearances retain their authority. |
| Work Allocation & Dispatch; Patient Transport; Clinical Logistics Coordination; Discharge Management; Workflow & Activity Coordination | Activity & Execution | Operational activity needs eligible progression, effect standing and recoverable execution under context; clinical work and source clearances retain their authority. |
| Work Allocation & Dispatch; Patient Transport; Clinical Logistics Coordination; Discharge Management; Workflow & Activity Coordination | Operational Assurance | Operational activity needs eligible progression, effect standing and recoverable execution under context; clinical work and source clearances retain their authority. |
| Clinical Collaboration; General Collaboration; Presentation Services | Interaction & Experience | Human interaction and notifications consume governed context without becoming clinical truth, policy ownership or a central collaboration service. |
| Clinical Collaboration; General Collaboration; Presentation Services | Policy & Control | Human interaction and notifications consume governed context without becoming clinical truth, policy ownership or a central collaboration service. |
| Clinical Collaboration; General Collaboration; Presentation Services | Event & Subscription | Human interaction and notifications consume governed context without becoming clinical truth, policy ownership or a central collaboration service. |
| Information Design Governance; Clinical Knowledge Services; Governed Reference Dataset Administration | Semantic Governance & Conformance | Definition, binding and mutable reference-content governance share conformance and attribution while retaining separate lifecycle meanings. |
| Information Design Governance; Clinical Knowledge Services; Governed Reference Dataset Administration | Managed State & Lifecycle | Definition, binding and mutable reference-content governance share conformance and attribution while retaining separate lifecycle meanings. |
| Information Design Governance; Clinical Knowledge Services; Governed Reference Dataset Administration | Provenance & Traceability | Definition, binding and mutable reference-content governance share conformance and attribution while retaining separate lifecycle meanings. |
| Information Authority Representation; Patient Clinical Record; Person Identity; Governed Assurance; Governed Reference Dataset Administration | Information Authority Qualification | Source standing recurs in competing identity assertions, clinical synthesis, reference content and evidence use; persistence success and freshness are insufficient substitutes. |
| Associated Clinical Binary Content Enablement; Clinical Record Administration; Clinical Collaboration | Binary Content Association & Resolution | Documents and collaboration references can require payload resolution while logical ownership, collaboration-native media and physical custody stay distinct. |
| Significant Activity Audit Capture; Health Information Control; Clinical Communication Administration | Governed Activity Audit Evidence | Required evidence of governed actions recurs across policy, disclosure and communication; only mandatory authoritative capture has the specifically established intrinsic authority. |
| Information Lineage Stewardship; Person Identity; Patient Clinical Record | Information Attribution & Lineage | Correction history, source attribution and derived clinical synthesis require meaningful lineage rather than infrastructure traces. |
| Entity-Specific Operational Coordination; Discharge Management; Bed & Care-Place Management | Entity-Relative Operational Progression | Patient progression, bed turnover and entity-specific operational activity benefit from a coherent entity-relative execution frame; this is a proposed reusable contribution rather than component allocation. |
| Governed Operational Activity Design; Workflow & Activity Coordination | Operational Fulfilment Definition | Defined fulfilment context and execution control recur in human work, human decisions and synthetic processing, while remaining different abilities. |
| Governed Operational Activity Design; Workflow & Activity Coordination | Governed Activity Execution Control | Defined fulfilment context and execution control recur in human work, human decisions and synthetic processing, while remaining different abilities. |
| Patient Clinical Record; Governed Reference Dataset Administration | Active Information Availability | Both clinical and reference information require responsive use and authoritative revision establishment, with separate trust/concurrency domains. |
| Patient Clinical Record; Governed Reference Dataset Administration | Authoritative Durable State Establishment | Both clinical and reference information require responsive use and authoritative revision establishment, with separate trust/concurrency domains. |

## Machine-readable review trail

The publication directory contains `change-ledger.json`, `identity-lock.json`, `semantic-baseline.json`, `v0.1-fingerprints.json`, `v0.3-reconciliation.json`, `derivation-provenance.json`, `corpus-register.json`, and `source-coverage.json`. Together these identify baseline material, additions, new frames, proposed naming, allocations, retained aliases, new support relationships and the precise source/derivation basis.

## Verification

**PASS — model, source preservation and publication.** All 862 elements have
unique Reference IDs, LaTeX labels and Structural Canonical IDs; all Capabilities
have current placements and all 178 Features have BEC CT3 frames. The 719 baseline
identities, their governed definitions, all 462 baseline relationships and 2,418
existing LaTeX labels are preserved. The accepted v0.1 files and governed Markdown
remain unchanged. Proposed elements, placements, naming and relationships retain
their independent status and derivation provenance; existing Candidate concerns
and unresolved source assertions remain explicit.

The PDF builds cleanly to **1,139 pages**, with 862 indexed elements, no undefined
references, duplicate labels, overfull boxes or missing glyphs. Its SHA-256 is
`d2d435356091cba69392fa6c332cf6248354b173fc38051584bbbb30d367caea`.
Thirteen in-memory invalid-model probes were rejected, covering identity/alias
collisions, semantic preservation, accidental promotion and missing Feature frames.

**PASS — repository architecture suite.** The required command completed in
18.673 seconds: **90 tests across eleven suites, zero failures, errors or skips**.

```bash
mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

Detailed evidence is retained in `validation.json`, `validator-probes.json` and
`architecture-tests.json` in the publication directory. These checks establish
preservation, structural coherence and publication integrity; architectural
acceptance of the Proposed decisions remains the purpose of human review.
