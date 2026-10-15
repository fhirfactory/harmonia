<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
<!-- Enabling responsibility is established; heading order supplies no CT ancestry. -->

## Client Administration
### Capability Overview
Client Administration provides the representation and demographic context needed to recognise individuals receiving care. It brings identity, healthcare-subject context, relationships and privacy into the same administrative frame while preserving their different responsibilities. The established partial chain to Person Identity is retained; the catalogue's other contextual descriptions do not supply a complete capability hierarchy.
### Intent
Keep the individual and the circumstances of their representation intelligible across care information and operational activity.
### Boundary
R1/R2 identifier correlation and administrative correction do not make Harmonia the authoritative clinical EMPI. Probabilistic matching and master identity merge/unmerge remain an uncommitted roadmap candidate.

## Person Identity
### Capability Overview
Person Identity resolves and correlates the identities and identifiers assigned to one individual by different authorities. It enables an incoming local identifier to be understood in its issuing namespace and related to other known identifiers without destructively replacing their history. Resolution, cross-authority correlation, alias association and governed correction preserve different aspects of that responsibility.
### Intent
Support reliable association of care information with the intended individual across participating systems.
### Boundary
Harmonia relies on authoritative upstream PAS/MPI information for R1/R2. Correlation and correction notifications do not establish an autonomous master-patient reconciliation engine.
### Harmonia Architectural Relevance
Identity association is a direct platform concern because clinical records, orders and operational activity need an enduring subject context. Its Core standing does not imply originating master identity authority.

## Healthcare Subject
### Capability Overview
Healthcare Subject maintains the subject-of-care context attached to clinical records and operational activity. It connects a resolved person identity with the encounters, orders and observations concerning that person, while preserving the provenance and temporal standing of demographic attributes. Its responsibility differs from resolving identifiers: it establishes how the person is represented as the continuing subject of care.
### Boundary
Subject association does not originate clinical findings or confer authority to change the source person's identity.
### Harmonia Architectural Relevance
Verifiable subject binding prevents otherwise valid information from being interpreted in the wrong person's care context, making it a substantive information-management responsibility.

## Client Relationship
### Capability Overview
Client Relationship represents nominated carers, guardians, contacts and care agents together with the effective period and authority scope of those relationships. It supports correct interpretation of who is related to a client and in what capacity, rather than treating a contact name as unrestricted authority. The relationship context remains distinct from the client's own identity and privacy directives.
### Boundary
Representation of a relationship does not by itself authorise every disclosure or establish a new legal mandate.
### Harmonia Architectural Relevance
Temporal relationship information materially supports appropriate care communication and context without assigning guardianship decisions to Harmonia.

## Client Privacy
### Capability Overview
Client Privacy applies client-level consent directives, confidentiality markers and privacy preferences to governed information use. It makes the person's restrictions operationally relevant at disclosure, query and presentation boundaries instead of leaving them as passive record annotations. Its responsibility complements the broader policy boundary while retaining the distinction between the directive and an action evaluated against it.
### Boundary
A privacy flag does not become a caller-controlled grant of authority. Consent evaluation and information access must respect established policy ownership.
### Harmonia Architectural Relevance
Consistent application of client restrictions across information pathways is a direct platform responsibility, supporting Core classification.

## Provider Administration
### Capability Overview
Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing.
### Intent
Provide a trustworthy provider context for routing, participation and the application of relevant qualification and privilege constraints.
### Boundary
Practitioner Role Resolution establishes neither Clinical Privilege nor Operational Privilege. The descriptive practitioner and role groupings do not establish reconciled CT ancestry.

## Organisation Administration
### Capability Overview
Organisation Administration maintains the identity, profiles, structural relationships and contact context of participating healthcare organisations. It enables a legal or operational entity to be distinguished from a department, network relationship or communication channel. This supports consistent interpretation of providers and destinations across information flows rather than allowing each interface to create an incompatible organisational account.
### Boundary
Representing organisational structure does not acquire corporate authority or ownership of every service offered by the organisation.

## Location Administration
### Capability Overview
Location Administration represents the places and physical relationships relevant to healthcare activity, from facilities to particular care places. Address and geospatial context answer where a place is; hierarchy explains its surroundings; care-place definition records the local characteristics relevant to care. These are stable descriptive responsibilities, distinct from the changing operational state of a bed or ward.
### Boundary
Defining a place does not establish occupancy, readiness, facilities maintenance or service capacity.

## Health Service Administration
### Capability Overview
Health Service Administration represents identifiable services and the conditions under which organisations, locations and practitioner roles provide them. Catalogue publishing, service-location-provider association, operating schedules and eligibility definitions allow a service to be understood as more than a location or specialty label. The service context remains material wherever activity meaning or accountability depends on it.
### Boundary
This capability owns the service mapping associations, not the mapped organisations, locations or practitioners. The supplied Domain03-to-Domain04 trace preserves that distinction; it does not transfer entity ownership.

## Health Product Administration
### Capability Overview
Health Product Administration retains the reference context for medications, implants, blood products and consumables. Product definitions, formularies and batch or serial information help explain what is being referenced without assigning a supply-chain implementation to Harmonia. No Feature decomposition is established for this responsibility in the present baseline.
### Boundary
Product representation does not perform procurement, stock control, dispensing or clinical administration. The contextual child descriptions do not establish additional canonical capabilities.

## Clinical Device Administration
### Capability Overview
Clinical Device Administration represents device types, identifiable instances, patient or location associations and communication endpoints. This lets device-originated information be interpreted in relation to the actual device and care context, rather than merely a network address. Type, instance, assignment and connectivity each answer a different question about the source of device data.
### Boundary
Device registration is not maintenance, release management or clinical interpretation. An endpoint credential does not by itself authorise clinical use of the device.

## Client-Centred Administration
### Capability Overview
Client-Centred Administration receives demographic and registration changes and makes verified updates available to participating clinical systems. It connects administrative events from external PAS environments with the client context used across care. The enabling responsibility concerns integration of changes, rather than independently deciding the authoritative demographic truth whenever a source sends an update.
### Boundary
Ingestion and distribution do not establish master clinical identity reconciliation or authority over the originating registration process.
### Harmonia Architectural Relevance
Demographic changes materially affect downstream association and continuity, so their governed propagation is active enablement rather than passive directory context.

## Referral Administration
### Capability Overview
Referral Administration receives referral material, associates the supporting clinical context and tracks the recipient's explicit disposition and outcome. Its purpose is to keep the request and its progression intelligible across care boundaries. It distinguishes a request for another participant's involvement from an order for bounded fulfilment and from actual acceptance of care responsibility.
### Boundary
Transport acceptance does not establish clinical acceptance. A referral need not universally produce an appointment, encounter, order or transfer of care.
### Harmonia Architectural Relevance
Harmonia materially connects the referral, supporting information and reported disposition while the receiving service retains clinical and responsibility decisions.

## Scheduling Administration
### Capability Overview
Scheduling Administration integrates appointment information established by departmental scheduling systems. It receives scheduled events and propagates cancellation, rescheduling and attendance changes so that care-coordination contexts do not diverge. Its concern is the coherence of scheduling information across participants rather than originating every booking decision.
### Boundary
A synchronised booking does not prove attendance, service capacity or delivered clinical care.
### Harmonia Architectural Relevance
Timely appointment changes materially support coordination across systems without giving Harmonia the scheduler's clinical or departmental authority.

## Episode & Encounter Administration
### Capability Overview
This capability maintains identifiable encounter contexts and their governed progression, including association with care teams and locations. It enables information from an admission, visit or community contact to be understood within the relevant episode of activity. Encounter progression is an administrative and operational account, separate from the clinical acts that occur during it.
### Boundary
The source lists ON_LEAVE in the Feature description but does not fully connect it in the Process illustration. That transition gap remains unresolved rather than completed by convention.
### Harmonia Architectural Relevance
Encounter context and tracked state are substantive platform responsibilities supporting correlation across records, orders and operational events.

## Order Administration
### Capability Overview
Order Administration manages the closed loop around a request for defined fulfilment. It receives the request, resolves and routes to a destination, distinguishes delivery from business disposition, tracks progress and associates the eventual outcome. This makes the difference between an order sent and an order fulfilled visible throughout the requester's interaction.
### Intent
Preserve an intelligible connection between the original request, its destination, progression and returned outcome.
### Boundary
Technical acknowledgements have only their contractual meaning. Receipt does not universally establish durable acceptance, business acceptance or completion; outcome association has no invented Feature equivalence.
### Harmonia Architectural Relevance
Reliable routing and closed-loop state are direct platform responsibilities. The performing service retains authority over clinical execution and its result.

## Diagnostic Administration
### Capability Overview
Diagnostic Administration correlates requests with specimens and encounters and integrates diagnostic reports into their request context. It connects the laboratory or imaging contribution with the participants expecting an outcome, preserving report identity and standing as updates occur. Its enabling role differs from performing the diagnostic procedure or professionally interpreting evidence.
### Boundary
The finalised-report distribution Feature retains its narrower scope. Broader Domain03 behaviour covering preliminary, corrected or addendum information does not silently expand that Feature.
### Harmonia Architectural Relevance
Request, specimen and report correlation and governed distribution are substantive information-integration responsibilities.

## Medication Administration
### Capability Overview
In this enabling catalogue, Medication Administration integrates medication orders, dispensing evidence and point-of-care administration records. It preserves the distinct events through which intended therapy becomes supplied and, where reported, administered. This administrative integration supports a coherent medication account across systems without itself dispensing or administering a medicine.
### Boundary
Prescribed, dispensed and administered are different states. The capability name does not assign bedside medication delivery to Harmonia.
### Harmonia Architectural Relevance
Connecting medication events materially supports continuity and interpretation while clinical and pharmacy actors retain originating responsibility.

## Procedure Administration
### Capability Overview
Procedure Administration integrates procedural bookings and the documentation of procedures performed. It keeps the scheduled case distinct from operative, anaesthetic and post-procedure information received later. That separation supports continuity across scheduling and clinical documentation without inferring that a booking establishes performance.
### Boundary
Administrative integration neither performs the procedure nor originates its professional findings.
### Harmonia Architectural Relevance
Correlated bookings and procedural documents support cross-system care context while the surgical or procedural service retains clinical authority.

## Clinical Record Administration
### Capability Overview
Clinical Record Administration manages clinical documents as governed information with authorship, classification, versions and supersession history. It distinguishes a new document from an amendment or withdrawal and maintains metadata for discovery. Its purpose is to preserve an intelligible documentary record across change, rather than merely accumulate files.
### Boundary
This responsibility owns document governance; exchange of documents does not transfer that ownership to the transporting capability. Preservation does not originate the clinical facts.
### Harmonia Architectural Relevance
Non-destructive document lifecycle and governed discovery are direct platform information-management responsibilities.

## Care Plan Administration
### Capability Overview
Care Plan Administration receives care plans and coordinates the distribution of updates to authorised team members. It supports shared understanding of established clinical intent across participating organisations. The responsibility concerns the plan as managed information, distinct from deciding the intended course of care or performing the actions expressed by it.
### Boundary
Synchronising a plan does not author its clinical intent, approve treatment or prove planned care occurred.
### Harmonia Architectural Relevance
Timely propagation of multidisciplinary plan information materially supports continuity without transferring clinical planning authority.

## Clinical Communication Administration
### Capability Overview
This capability manages secure clinical communication transactions, delivery and receipt evidence, and the required enduring audit trail. Verified provider and team endpoints support appropriate addressing while transaction correlation makes the communication history intelligible. The responsibility preserves separate meanings for transport delivery, recipient acknowledgement and clinical or business disposition.
### Boundary
Delivery or read receipts do not prove clinical review, incorporation, approval or acceptance. Audit evidence must remain governed without unmasked PHI in diagnostic streams.
### Harmonia Architectural Relevance
Secure addressed communication and attributable transaction evidence are substantive platform responsibilities.

## Funding & Coverage Administration
### Capability Overview
Funding & Coverage Administration represents externally supplied insurance coverage and eligibility flags needed as care context. It allows those administrative conditions to be available without implying that Harmonia determines entitlements. The established Adjacent standing and absence of Features bound the current responsibility to this information contribution.
### Boundary
Coverage information is not claims adjudication, funding governance or a universal determination of care eligibility.
### Harmonia Architectural Relevance
Harmonia consumes the administrative context; it does not acquire the payer's authority or manage the underlying coverage arrangement.

## Clinical Charging & Claims Administration
### Capability Overview
This capability supplies clinical activity summaries and charging records to billing contexts. It preserves a route from activity information to the systems responsible for financial processing without treating the export as a completed bill or claim. No Feature decomposition or comprehensive revenue-cycle responsibility is established.
### Boundary
Export of activity evidence does not confer pricing, billing, claim or accounting authority.
### Harmonia Architectural Relevance
Its Adjacent standing reflects information supplied to financial systems while the financial activity remains externally governed.

## Clinical Assessment
### Capability Overview
Clinical Assessment is retained as an Adjacent Service Delivery context. It identifies the setting of assessment-related information needs without establishing a specialised Harmonia Feature set or replacing the Business Capability Individual Health Assessment. The distinction allows the catalogue to recognise clinical activity while keeping actual examination, interpretation and professional judgement outside the platform's assigned practice.
### Boundary
No atomic Features are established. The contextual name does not make Harmonia a clinical assessor or establish BC ancestry.
### Harmonia Architectural Relevance
The Adjacent classification denotes contextual information participation rather than material execution of clinical assessment.

## Primary Care
### Capability Overview
Primary Care enables information continuity between general practice and other care settings through shared-record ingestion and notifications of acute events. A GP's notes, summaries and immunisation records can contribute to the longitudinal view, while admission and discharge information can return to the registered practitioner. The enabling responsibility is the connection between contexts, rather than practice management or primary clinical care.
### Boundary
Receiving a summary or dispatching a notification does not perform consultation, clinical review or follow-up.
### Harmonia Architectural Relevance
These flows materially support continuity across primary and acute care without assigning general-practice authority to Harmonia.

## Acute Care
### Capability Overview
Acute Care provides the information context needed around acute clinical events, including externally reported deterioration and timely access to history and active medications. It helps current events be interpreted alongside prior information rather than as isolated messages. Its Service Delivery context does not become a child of Individual Care Delivery's setting-independent Business Capability hierarchy.
### Boundary
Capturing a deterioration event does not determine clinical significance or prescribe an acute response.
### Harmonia Architectural Relevance
Harmonia materially supplies and correlates acute-care information while clinical decisions and intervention remain with the care service.

## Emergency Care
### Capability Overview
Emergency Care integrates presentation and triage information and correlates it with recent admissions, summaries and known allergies. It supports a rapidly intelligible account of the patient arriving for emergency care without replacing the clinical triage decision. The context is valuable because the regional history may be distributed across participants outside the receiving department.
### Boundary
Ingested triage categories remain externally established clinical information; correlation does not confer emergency decision authority.
### Harmonia Architectural Relevance
Cross-facility context and current presentation information materially support emergency care continuity.

## Inpatient Care
### Capability Overview
Inpatient Care brings together information for multidisciplinary review and captures externally established clinical milestones during an admission. Results, medication records and nursing notes support a shared contextual view, while progression events make reported care status visible. The responsibility concerns information availability and correlation rather than control of the inpatient clinical worklist.
### Boundary
Clinical milestone capture does not establish the milestone's clinical authority or manage rounds and handover.
### Harmonia Architectural Relevance
The platform materially supplies consolidated inpatient context; milestone tracking may be realised through approved reusable composition without an invented specialised Function.

## Ambulatory Care
### Capability Overview
Ambulatory Care is an established Adjacent delivery context retained for understanding information used around care without inpatient admission. The RAD establishes no atomic enabling Feature decomposition for it. The entry therefore locates the context without claiming a separate scheduling, treatment or clinical progression mandate for Harmonia.
### Boundary
The contextual entry does not allocate BC tiers or require a distinct platform function for every ambulatory activity.
### Harmonia Architectural Relevance
Adjacent standing preserves contextual participation while clinical service delivery remains external.

## Surgical & Procedural Care
### Capability Overview
Surgical & Procedural Care retains the clinical delivery context around procedures as Adjacent. It is distinct from the established integration of bookings and documents in Procedure Administration and from theatre operational coordination. Recognising those neighbouring responsibilities prevents information about a procedure from being read as an assignment to perform or clinically manage it.
### Boundary
No atomic Features are allocated to this context, and Harmonia does not acquire surgical decision or procedural authority.
### Harmonia Architectural Relevance
The context is represented without assigning direct clinical delivery responsibility.

## Diagnostic Services
### Capability Overview
Diagnostic Services supports cross-provider availability of reports and diagnostic history. Result distribution brings established findings to the ordering and designated participants; history consolidation makes relevant evidence visible across institutional boundaries. This delivery context complements Diagnostic Administration's request and report associations without making information integration the professional diagnostic service itself.
### Boundary
Consolidating trends does not interpret diagnostic evidence or establish a patient-level clinical conclusion.
### Harmonia Architectural Relevance
Governed report distribution and integrated history materially enable diagnostic information use across care boundaries.

## Therapeutic Services
### Capability Overview
Therapeutic Services is retained as an Adjacent clinical delivery context, recognising care activity whose information may concern Harmonia without assigning treatment performance to it. The current model establishes no atomic Features for this context. The broad label is not used to invent therapy-specific Functions or reclassify clinical interventions as platform execution.
### Boundary
Information participation does not confer authority to select, perform or assess the clinical adequacy of therapy.
### Harmonia Architectural Relevance
Adjacent standing preserves the contextual role while treatment remains the responsible service's activity.

## Medication Therapy
### Capability Overview
Medication Therapy connects reconciled histories and medication events across care transitions. The clinically established medication account is received and made available with order, dispensing and administration information still distinguishable. This supports continuity of therapy understanding without turning the platform into the clinician performing medication reconciliation or choosing treatment.
### Boundary
Ingesting a reconciled history is not clinical reconciliation, and an order or dispense event does not prove administration.
### Harmonia Architectural Relevance
Cross-boundary medication information materially supports care continuity and interpretation.

## Allied Health
### Capability Overview
Allied Health is preserved as an Adjacent Service Delivery context without an established atomic Feature set. It acknowledges the professional care context in which information can be needed or produced, while leaving the specific therapeutic and assessment responsibilities with their clinical participants. The model does not create a generic allied-health workflow from the contextual name.
### Boundary
Care coordination and record access do not transfer professional judgement or therapy delivery to Harmonia.
### Harmonia Architectural Relevance
The formal Adjacent position is retained rather than inherited from neighbouring Relevant delivery contexts.

## Preventive Care
### Capability Overview
Preventive Care enables information flows around immunisation and screening recall. Administration records can be conveyed to national registers, while established reminders can reach designated providers. This Service Delivery context supplies bounded information enablement; it does not replace the broader Business Capability Preventative Health or its programme and cohort responsibilities.
### Boundary
Submission does not perform immunisation, and a recall notice does not determine individual clinical eligibility or complete screening.
### Harmonia Architectural Relevance
Register integration and recall distribution materially connect preventative activity across participants.

## Rehabilitation
### Capability Overview
Rehabilitation is an Adjacent clinical delivery context whose information can be represented without assigning the rehabilitation programme to Harmonia. No atomic Features are established in the current catalogue. Its presence records a healthcare context rather than requiring a new specialised coordination engine or a clinical outcome authority.
### Boundary
Information integration does not perform therapy, determine rehabilitation goals or judge clinical effectiveness.
### Harmonia Architectural Relevance
Contextual participation is retained within the formal Adjacent classification.

## Community Care
### Capability Overview
Community Care integrates encounters, observations and plans across home, community and hospital outreach participants. It supports continuity where clinical activity occurs outside the hospital's immediate information environment. Receiving visit records and synchronising plan changes helps make community activity visible without turning the platform into the provider of that activity.
### Boundary
Plan synchronisation does not originate clinical intent, assign community work or establish that a visit occurred beyond its source evidence.
### Harmonia Architectural Relevance
Cross-organisational encounter and care-plan information materially supports community care continuity.

## Outreach Care
### Capability Overview
Outreach Care supplies portable clinical context and receives documentation from rural or remote outreach activity. It addresses continuity where the relevant care information may not be available through the usual on-site environment. The responsibility connects the prior record with the outcomes reported after the visit while keeping source and temporal context intelligible.
### Boundary
An offline-capable summary does not acquire clinical authority or guarantee that the remote copy represents current authoritative state.
### Harmonia Architectural Relevance
Context provision and outcome ingestion materially connect outreach activity to the longitudinal record.

## Remote & Virtual Care
### Capability Overview
Remote & Virtual Care integrates remotely obtained observations and associates virtual consultations with their patient and episode context. It distinguishes a telemetry contribution from the encounter in which a practitioner may interpret it. The enabling responsibility concerns information continuity across delivery modes, preserving the setting-independent meaning of the Business care capabilities.
### Boundary
Telemetry acquisition is not clinical monitoring judgement or treatment; session association does not establish clinical adequacy.
### Harmonia Architectural Relevance
Remote data and virtual-session context materially extend information continuity across care locations and modes.

## Clinic & Practice Operations
### Capability Overview
Clinic & Practice Operations tracks operating sessions and the operational progression of people through the clinic environment. It makes delays, waiting and completion context visible alongside service-specific capacity responsibilities. This is real-world clinic operation, distinct from the technical queueing machinery that might realise the information flow.
### Boundary
Operational queue progression does not determine clinical priority or own the clinician's worklist.
### Harmonia Architectural Relevance
Session and flow information materially supports clinic coordination while the clinic retains its contextual capacity responsibility.

## Ward Operations
### Capability Overview
Ward Operations coordinates ward occupancy and care-place context and assembles the information needed around shift changes. Its responsibility includes the ward-specific meaning of capacity and patient flow rather than a generic resource counter. It can make pending concerns and tasks visible without becoming the authority managing clinical handover.
### Boundary
Handover information assembly is distinct from performing or managing handover. Context-specific capacity remains with Ward Operations.
### Harmonia Architectural Relevance
Correlated ward state and contextual information materially support operational continuity.

## Theatre Operations
### Capability Overview
Theatre Operations integrates surgical case progression and the operational consequences relevant to portering, sterilisation and recovery participants. It gives reported milestones an operational coordination context without originating the surgical acts they describe. Theatre-specific capacity remains a business responsibility even when shared information machinery supports several service contexts.
### Boundary
Tracking incision or recovery information does not authorise surgery or transfer control of the clinical procedure.
### Harmonia Architectural Relevance
Milestone-driven operational notifications materially connect supporting activity around the theatre service.

## Emergency Department Operations
### Capability Overview
This capability maintains the operational context of emergency places, waits and length-of-stay milestones. It supports understanding of occupancy and impediments in the department while keeping capacity's emergency-service meaning intact. Its concern differs from Emergency Care's clinical presentation context, even though both can refer to the same patient encounter.
### Boundary
Operational timing and escalation do not determine triage or acquire clinical decision authority.
### Harmonia Architectural Relevance
Care-place state and operational waiting information materially support coordination of the emergency service.

## Outpatient Operations
### Capability Overview
Outpatient Operations integrates arrival events and follow-up requirements generated as clinics progress. It connects a person's operational presence with the relevant service context and makes post-clinic booking or diagnostic needs available for subsequent activity. The responsibility retains outpatient-specific capacity semantics without requiring a separate arrival Function where approved reusable composition suffices.
### Boundary
An arrival does not establish consultation completion, and a captured follow-up requirement does not perform the clinical activity.
### Harmonia Architectural Relevance
Arrival and clinic-outcome information materially connect outpatient progression to care coordination.

## Bed & Care-Place Management
### Capability Overview
Bed & Care-Place Management maintains operational place state and the turnover progression that makes a place usable again. Vacancy, reservation, cleaning and readiness must remain distinguishable so that an empty bed is not automatically treated as available for allocation. Notifications expose meaningful readiness changes to the responsible operational participants.
### Intent
Support credible care-place decisions using governed state rather than physical presence alone.
### Boundary
A ready bed does not establish service-level capacity or collective readiness, and place state does not determine clinical suitability or care responsibility.
### Harmonia Architectural Relevance
Governed place-state progression and operational notification are substantive assigned platform responsibilities.

## Clinical Resource Management
### Capability Overview
Clinical Resource Management integrates location and allocation information for shared clinical equipment. It makes the current operational resource context available rather than treating device inventory as proof that equipment can be used for a particular need. The responsibility supports coordination while retaining the distinction between resource state and a service's capacity.
### Boundary
Equipment tracking does not acquire enterprise asset ownership, maintenance responsibility or clinical authority to use it.
### Harmonia Architectural Relevance
Dynamic equipment context materially supports operational coordination across care environments.

## Service Capacity Management
### Capability Overview
Service Capacity Management provides a higher-order view by aggregating capacity information across services and facilities. It can consider occupancy, waits and diversion while preserving the service-specific meaning of the underlying capacity. Its purpose is not to erase differences between clinic, ward, theatre, emergency, outpatient and mobile-service operation.
### Boundary
Consuming contextual capacity does not acquire the originating capacity-management responsibility or infer capacity solely from resource counts.
### Harmonia Architectural Relevance
Cross-service operational visibility materially supports coordination while source service contexts retain their authority.

## Clinical Workforce Management
### Capability Overview
Clinical Workforce Management is retained as an Adjacent operational context for the workforce on which services depend. The current enabling catalogue does not assign an atomic Feature set. Its presence allows workforce constraints to be recognised without treating integration of workforce facts as authority to govern employment, coverage or clinical allocation.
### Boundary
No new workforce system responsibility or complete CT ancestry is inferred from this contextual entry.
### Harmonia Architectural Relevance
Adjacent standing reflects contextual participation rather than management of the workforce itself.

## Clinical Rostering
### Capability Overview
Clinical Rostering provides the Adjacent context of planned workforce availability. It is distinct from consuming roster projections for operational views or resolving who is currently on call. The present architecture retains the context without establishing a Harmonia Feature decomposition for authoring or managing the authoritative roster.
### Boundary
A projected roster does not confer qualification, privilege, deployment or ownership of the originating schedule.
### Harmonia Architectural Relevance
Roster information can inform operations while the formal Adjacent classification preserves its external management.

## Mobile Staff Management
### Capability Overview
Mobile Staff Management integrates with external business mobile-work mechanisms and receives visit progression and safety milestones. Its established dispatch Feature exchanges assignment, dispatch and status information with the relevant worker context. This is an information integration boundary around mobile activity, rather than a platform claim to decide the clinician's work.
### Boundary
Harmonia does not determine clinical allocation, handover or EMR worklists. External work/task language does not redefine the internal Synthetic Task.
### Harmonia Architectural Relevance
Mobile assignment and progression exchanges materially connect roving activity to the wider care environment within that bounded integration role.

## On-Call Management
### Capability Overview
On-Call Management resolves the active provider for a specialty, service or hospital at the relevant time. It turns changing coverage information into usable routing context so that operational communication can reach the intended capacity. This differs from authoring the roster or deciding who is professionally qualified for the requested activity.
### Boundary
Being on call does not by itself establish clinical privilege or assignment of a particular care responsibility.
### Harmonia Architectural Relevance
Current provider resolution materially enables appropriate contact and routing across operational boundaries.

## Work Allocation & Dispatch
### Capability Overview
Work Allocation & Dispatch progresses authorised operational work from a recognised requirement through matching, dispatch, acknowledgement and oversight. It distinguishes an instantiated work item from its assignment and from evidence of performance, allowing stalled activity to be identified and escalated. Examples such as cleaning or transport concern real-world support work, not technical message queues.
### Intent
Make operational work progression accountable from requirement to reported outcome within applicable policy and role constraints.
### Boundary
This assignment responsibility does not confer clinical work allocation, qualification or privilege. A technical receipt has only its interaction contract's meaning.
### Harmonia Architectural Relevance
Governed operational work progression is a substantive platform responsibility supporting the Core classification.

## Clinical Training & Education
### Capability Overview
Clinical Training & Education remains a Reference context for developing practitioners' knowledge and skills. The catalogue recognises its enterprise significance without assigning training delivery or a Feature decomposition to Harmonia. Its separation from qualification checks prevents a represented certificate or training record from being mistaken for the education activity itself.
### Boundary
No platform training programme, assessment process or clinical teaching authority is established.
### Harmonia Architectural Relevance
Reference standing retains enterprise context without a material assigned processing responsibility in this entry.

## Clinical Competency Management
### Capability Overview
Clinical Competency Management is retained as a Reference context concerning practitioner competence. It is distinguishable from checking supplied qualification or competency evidence before specialised work. The model does not establish that representing evidence makes Harmonia the authority that assesses or grants competence.
### Boundary
No atomic Features or competency-assessment authority are allocated to this context.
### Harmonia Architectural Relevance
The formal Reference classification remains separate from the relevance of operational qualification checking elsewhere.

## Clinical Qualification Management
### Capability Overview
Clinical Qualification Management checks professional and clinical qualification or competency context before specialised operational work is dispatched. It makes relevant standing available at the point where an operational action must respect it. The approved rename preserves the professional responsibility while distinguishing it from operational badges and physical access credentials.
### Boundary
The historical name Clinical Credential Management is an alias only for this element. It is not equivalent to Credential Management, and role resolution alone establishes neither clinical nor operational privilege.
### Harmonia Architectural Relevance
Qualification context materially constrains legitimate dispatch without making Harmonia the originating professional regulator or training authority.

## Clinical Supervision
### Capability Overview
Clinical Supervision is a Reference context for the professional supervision of care activity. Its inclusion locates that enterprise responsibility without assigning a Harmonia supervision Feature or clinical-work mandate. Operational visibility into activity and qualifications can be useful context without becoming the professional act of supervising practice.
### Boundary
Workflow oversight or platform escalation does not confer clinical supervision or responsibility for clinical adequacy.
### Harmonia Architectural Relevance
Reference standing is preserved; no direct platform supervision responsibility is established.

## Patient Transport
### Capability Overview
Patient Transport coordinates the operational progression of movement requests, including the patient's applicable mobility constraints. Departure, transit and arrival checkpoints make the physical journey visible to participants. The responsibility supports internal and inter-facility movement without treating arrival as acceptance of clinical responsibility at the destination.
### Boundary
Transport completion does not perform clinical handover or establish a care responsibility transition.
### Harmonia Architectural Relevance
Transport request and dispatch progression are substantive assigned operational coordination responsibilities.

## Clinical Logistics Coordination
### Capability Overview
Clinical Logistics Coordination integrates the movement and custody context of clinical material, notably specimen pickup, transport conditions and laboratory delivery. It keeps a material's journey connected to the fulfilment context requiring it. This enables clinical logistics to be followed without reducing specimen integrity to a delivery timestamp alone.
### Boundary
Tracking material movement does not perform diagnostic interpretation or establish general enterprise supply-chain ownership.
### Harmonia Architectural Relevance
Custody and transport information materially support diagnostic continuity and appropriate interpretation of specimen handling.

## Discharge Management
### Capability Overview
Discharge Management coordinates the reported milestones needed around discharge, including medication supply, transport and home-care confirmations. It makes readiness and impediments visible across disciplines and communicates departure information when physical exit occurs. The responsibility preserves a distinction between gathering confirmations and making the clinical decision that discharge is appropriate.
### Boundary
Milestone completion does not transfer clinical responsibility or prove the receiving provider accepted it. Readiness, exit and clinical decision authority remain distinct.
### Harmonia Architectural Relevance
Cross-team milestone coordination and departure notification are substantive assigned platform responsibilities.

## Patient Clinical Record
### Capability Overview
Patient Clinical Record provides a governed longitudinal representation across encounters, providers and source systems. Assembly supplies the timeline, active access supports current care context, and durable preservation retains the history of what Harmonia received, knew or managed. These contributions are complementary: a responsive working view must not become the authority for durable history.
### Intent
Make cross-institutional information usable over time without erasing source provenance or prior knowledge through correction.
### Boundary
Preservation does not originate clinical facts. Active distributed access and authoritative durable preservation remain separate under AX-05.
### Harmonia Architectural Relevance
Longitudinal assembly, active access and governed durable history are direct information-management responsibilities within the Core boundary.

## Health Information Exchange
### Capability Overview
As a BEC, Health Information Exchange provides reusable information-movement enablement through submission, retrieval mediation, addressed distribution and syndication. The models distinguish sending to a participant, requesting information, deliberately distributing to named recipients and publishing changes to matching subscribers. Their common responsibility is the exchange context, including routing and delivery state, rather than the business meaning of every payload.
### Boundary
The capability owns exchange transaction state without acquiring clinical-document ownership. Its name collision with the BC remains unresolved; exchange egress ends management of the emitted representation.
### Harmonia Architectural Relevance
Multi-party exchange and per-destination delivery tracking are substantive Core responsibilities, bounded by the external contract and originating information authority.

## Health Information Access
### Capability Overview
Health Information Access enables authorised discovery and retrieval across clinical information. It combines semantic search and discrete retrieval with access-qualified filtering so that a discoverable item is not automatically disclosable. It consumes established policy and consent determinations rather than making the search interface an alternative security authority.
### Boundary
Retrieval alone does not realise the filtering Feature. Unestablished Feature associations remain unestablished even where the behaviour shares the same owner.
### Harmonia Architectural Relevance
Responsive governed access is a direct platform responsibility. The approved support to Individual Care Delivery remains bounded and does not establish runtime dependencies for every clinical ability.

## Health Information Communication
### Capability Overview
Health Information Communication supplies standards-based transmission mediation between participating parties. It addresses protocol-facing transaction coordination and technical error handling while the business interaction retains its own meaning and authority. This differentiates the communication mechanism from the exchange model and from the clinical or operational purpose of the message.
### Boundary
Transport mediation does not own every communicated interaction or payload. External contracts must preserve standards semantics and exclude private operational metadata.
### Harmonia Architectural Relevance
Standards-conformant communication boundaries are a direct interoperability responsibility.

## Health Information Control
### Capability Overview
Health Information Control establishes the policy and security basis for governed information operations. Default-deny authority evaluation, consent constraints, security-context propagation and security-significant evidence have distinct purposes: deciding permission, retaining restrictions, carrying attributable operational context and preserving required evidence. The capability makes security intrinsic to the operation rather than optional presentation behaviour.
### Boundary
Security context is operational context and does not automatically become resource content. Security-context validation has no invented one-to-one mapping to propagation, and audit recording is not an assurance conclusion.
### Harmonia Architectural Relevance
Assigned policy enforcement and attributable context are substantive Core responsibilities across managed operations.

## Clinical Knowledge Services
### Capability Overview
Clinical Knowledge Services resolves governed clinical concepts and applies governed terminology mappings. It makes codes usable across local and standard semantic systems while retaining their source meaning. The responsibility is semantic reference and mapping enablement, distinct from formulating reusable clinical knowledge or applying that knowledge to an individual.
### Boundary
Code translation does not establish equivalence beyond the governed map or confer clinical decision authority.
### Harmonia Architectural Relevance
Concept resolution and mapping materially support meaningful information exchange and interpretation across regional systems.

## Clinical Collaboration
### Capability Overview
Clinical Collaboration provides secure multidisciplinary spaces bound to clinical context, including governed access to longitudinal information during discussion. Its purpose is contextual conversation between care participants rather than a separate source of authoritative clinical facts. A statement in a conversation remains discourse until explicitly authored through the governed record boundary.
### Boundary
Channel metadata must remain free of PHI. The source Function's mention of managing clinical handovers conflicts with the metamodel's clinical-work boundary; both are preserved for review without assigning that authority here.
### Harmonia Architectural Relevance
LHR-aware collaboration and controlled contextual queries are substantive Core responsibilities, distinct from clinical practice or record authorship.

## General Collaboration
### Capability Overview
General Collaboration provides secure operational messaging for departmental, administrative and facility-support participants. It gives non-clinical coordination an interaction context without assuming every discussion requires a patient-linked clinical workspace. Its role is communication enablement rather than ownership of the support activity being discussed.
### Boundary
Conversation does not assign operational authority, complete work or become an authoritative record merely through retention.
### Harmonia Architectural Relevance
Operational messaging materially enables coordination while the participants' business responsibilities remain external to the collaboration mechanism.

## Workflow & Activity Coordination
### Capability Overview
Workflow & Activity Coordination provides governed progression for three distinct work meanings: human operational Work Orders, human review or decision To Dos, and non-human Synthetic Tasks. It supports dispatch, state and deadline oversight without collapsing those meanings into a generic technical task. The common coordination responsibility makes progression visible while preserving who has authority to perform or decide the underlying activity.
### Intent
Keep assigned activity, acknowledgement, progress and outcome distinguishable across human and automated work.
### Boundary
Coordination does not confer clinical work ownership. Synthetic Tasks are not external clinical worklists, and the incomplete stalled/failed Process paths remain unresolved.
### Harmonia Architectural Relevance
Governed activity progression is a substantive Core responsibility; information traceability preserves archetype, work and undertaking distinctions without transferring outcome authority.

## Calendar Management
### Capability Overview
Calendar Management projects schedules and correlates events with temporal commitments. It brings rosters, on-call windows, facility calendars and appointment milestones into operational views without replacing their authoritative sources. This distinguishes the temporal context used for coordination from the business decisions that establish the schedule.
### Boundary
Projection does not author the roster or confer qualification, privilege or clinical assignment authority.
### Harmonia Architectural Relevance
Consistent temporal projections and event correlation are direct platform coordination responsibilities.

## Presentation Services
### Capability Overview
Presentation Services supplies contextual human interaction through clinical timelines, directory views and operational dashboards. It makes governed information usable by people while consuming the identity, security and clinical context established elsewhere. The responsibility is the interaction and presentation experience rather than a new authority for the information shown.
### Boundary
Presentation does not own authentication, credential storage, consent decisions, EMPI reconciliation or authoritative clinical state. A displayed value is not made authoritative by its appearance in a dashboard.
### Harmonia Architectural Relevance
Decoupled contextual interaction is a substantive Core responsibility that preserves the established information-access and control boundaries.

## Information Design Governance
### Capability Overview
Information Design Governance maintains canonical definitions, semantic bindings, exchange profiles and the conformance rules applied to representations. It makes meaning and constraints available across regional information flows so that an accepted technical format is not mistaken for semantic correctness. Governance and verification remain distinguishable from the clinical facts represented.
### Boundary
Consumers of definitions do not acquire their semantic authority, and conformance checking does not establish clinical truth or permission to disclose.
### Harmonia Architectural Relevance
Governed semantics and representation conformance are direct platform responsibilities that constrain both internal use and boundary exchange.

## Assurance Design
### Capability Overview
Assurance Design determines how satisfaction of a governed requirement or expected behaviour will be assured. It makes the disposition, criteria, evidence and evaluation expectations explicit alongside behaviour and failure/recovery design. Its output is a basis for assurance rather than proof that the implemented behaviour has satisfied the requirement.
### Boundary
Designing assurance does not confer approval authority, define the underlying obligation or allocate a runtime component. No Feature decomposition or CT ancestry is established.
### Harmonia Architectural Relevance
Formal classification remains Unresolved. Its approved support to Health Service Assurance establishes purpose and Enterprise Capability contributions without adjudicating this separate relevance dimension.

## Assurance Criteria Management
### Capability Overview
Assurance Criteria Management maintains reusable evaluative criteria with governed standing and temporal applicability. Version and effective context matter because a conclusion must be understood against the criteria applicable to the subject and time being assessed. Criteria may operationalise governing requirements, but their management is not authority to change those requirements.
### Boundary
Modelling criteria does not inherently approve them. Detailed criterion states and approval workflows remain unresolved, and criteria need not all originate in formal Governance.
### Harmonia Architectural Relevance
Formal relevance remains Unresolved. Established assurance support explains the enabling purpose without classifying it by inference or allocating software responsibility.

## Governed Assurance
### Capability Overview
Governed Assurance independently evaluates a subject through applicable criteria and trustworthy evidence, establishing explicit findings and conclusions. Context establishment associates source-owned information with a particular assurance purpose; assessment evaluates it; adjudication establishes the supported outcome. Insufficient evidence remains a meaningful limit rather than a forced positive or negative result.

The capability can manage its own assurance activity without managing the subject. Request, progression status and established outcome are different client concerns; the requester does not thereby control the evaluation or gain unrestricted status access.
### Boundary
No remediation, assignment or operational escalation of the subject is acquired. Financial governance is deliberately excluded; assurance does not recursively assure its own execution, and no component allocation is established.
### Harmonia Architectural Relevance
Formal classification remains Unresolved. The approved independent-assurance responsibility and contribution model establish material purpose without resolving that metadata dimension.

## Credential Management
### Capability Overview
Credential Management concerns operational badges and physical-access credentials used in authorised operational participation. It retains a distinct Domain03 responsibility even though Strategy has no matching catalogue entry. This prevents the word credential from merging physical-access context with the professional qualifications and competency evidence checked by Clinical Qualification Management.
### Boundary
No Strategy correspondence or Feature association is inferred from name similarity. Operational credentials do not themselves establish professional clinical qualification or privilege.
### Harmonia Architectural Relevance
Relevance remains Unresolved. Established operational behaviour provides context for possible enablement but does not authorise a formal classification.
