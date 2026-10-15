<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

## FEAT-EM-01
### Feature Overview
Identifier Resolution interprets an identifier in its issuing-authority and namespace context, so that the same local value is not assumed to identify the same person everywhere. It provides an identifiable subject reference without deciding an authoritative master-person match.

## FEAT-EM-02
### Feature Overview
Cross-authority correlation preserves the association between identifiers used for one person by different participants. It enables information to be related across regional systems without erasing the separate identifier namespaces or claiming that Harmonia originated the identity decision.

## FEAT-EM-03
### Feature Overview
Alias association keeps historical and demographic identity references navigable alongside active identifiers. The non-destructive relationship preserves how earlier information identified the person rather than rewriting that history whenever the current identity representation changes.

## FEAT-EM-04
### Feature Overview
Governed correction applies externally authoritative identity updates and their consequences to Harmonia-managed associations. Administrative review concerns the legitimate application of the source outcome; it does not grant Harmonia an EMPI merge engine or authority to select a golden person record.

## FEAT-EM-05
### Feature Overview
Subject binding connects resolved identity with the records and activities concerning that person. It makes the association verifiable across encounters, orders and observations rather than assuming that proximity in a message or screen establishes the enduring subject of care.

## FEAT-EM-06
### Feature Overview
Demographic governance retains the provenance and temporal context of attributes used to interpret the subject of care. A changed date, status or demographic assertion can therefore be understood as sourced information rather than an unexplained overwrite of previously known context.

## FEAT-EM-07
### Feature Overview
This Feature records the nominated people and capacities through which a client can be contacted or represented. Association preserves who the contact or guardian is in relation to the client; it does not make every nominated person an unrestricted recipient of clinical information.

## FEAT-EM-08
### Feature Overview
Relationship validity makes effective periods and authority scope part of the relationship's meaning. A contact association that was legitimate in one period or capacity need not be treated as currently valid for every purpose.

## FEAT-EM-09
### Feature Overview
Consent evaluation brings the client's applicable directives into consideration of a requested disclosure or exchange. It distinguishes the recorded directive from the particular action being evaluated, preserving the need for established policy and authority rather than treating consent data as a universal permission token.

## FEAT-EM-10
### Feature Overview
Confidentiality enforcement makes a restricted-record condition effective across discovery and presentation paths. Its purpose is consistent governed treatment of sensitive information, rather than a flag displayed in one interface while other paths disclose the same content without restriction.

## FEAT-EM-11
### Feature Overview
National verification connects practitioner identifiers and registration information to their recognised source context. It helps distinguish an identifiable professional from an unverified directory assertion without implying that verification alone grants local clinical privileges.

## FEAT-EM-12
### Feature Overview
Practitioner profile governance maintains the professional context used across care information and communication. Qualifications and practice status remain attributable facts, distinct from the organisation-specific role or privilege under which the practitioner may act.

## FEAT-EM-13
### Feature Overview
Role and specialty modelling describes the capacity in which a practitioner participates at an organisation or location. This keeps the person separate from each acting role and its service context; role resolution itself establishes neither Clinical Privilege nor Operational Privilege.

## FEAT-EM-14
### Feature Overview
Endpoint binding makes a verified communication destination specific to the practitioner's acting role. It supports routing to the intended professional capacity rather than assuming that every address attached to the person is interchangeable across organisations and services.

## FEAT-EM-15
### Feature Overview
Affiliation records keep practitioner participation in an organisation temporally intelligible. They support interpretation of an actor's organisational context while preserving that affiliation, employment, qualification and permission to undertake a particular activity answer different questions.

## FEAT-EM-16
### Feature Overview
The privileging record makes applicable admitting and practice permissions available as contextual evidence. It represents the health service's established privilege standing rather than granting new authority merely because a record exists in the directory.

## FEAT-EM-17
### Feature Overview
National organisation verification grounds an organisation identifier and entity classification in their recognised context. This supports reliable interpretation of the healthcare participant without assuming that its identity establishes every service, location or authority associated with it.

## FEAT-EM-18
### Feature Overview
Organisation profiles provide a governed account of the participant's status and published service descriptions. They support consistent directory use across systems while leaving the organisation's real-world governance and the definition of individual offerings distinguishable.

## FEAT-EM-19
### Feature Overview
Hierarchy navigation makes departmental, divisional and network relationships usable without flattening them into unrelated organisations. Traversal reflects established structure; it does not create corporate authority or infer responsibility from the way a directory happens to display a tree.

## FEAT-EM-20
### Feature Overview
Contact management provides verified administrative and clinical communication channels for organisations. It supports reaching the appropriate participant while distinguishing a directory contact from the role-specific destination and authority relevant to a particular clinical exchange.

## FEAT-EM-21
### Feature Overview
Address and geospatial resolution gives a recognised healthcare location an intelligible physical reference. It supports place interpretation without treating a valid address or recorded operating status as proof that a particular service or care place is currently ready.

## FEAT-EM-22
### Feature Overview
Physical hierarchy modelling relates a care environment to its campus, building, floor or ward context. The relationships describe where places sit physically; they do not automatically reproduce organisational hierarchy, service responsibility or operational capacity.

## FEAT-EM-23
### Feature Overview
Care-place modelling identifies the particular physical place and characteristics relevant to care, such as isolation or telemetry capability. It supplies the descriptive frame against which changing occupancy and readiness can be considered without making those states part of the place's identity.

## FEAT-EM-24
### Feature Overview
Service catalogue publishing makes identifiable service definitions and intake context available to regional participants. A published service can be discovered as an offering rather than inferred from an organisation's name; publication alone does not establish present availability or collective readiness.

## FEAT-EM-25
### Feature Overview
Service-location-provider binding explains which participant provides an identifiable service in which place and acting capacities. The mapping associations belong to Health Service Administration, while the organisations, locations and practitioner entities retain their own information responsibility.

## FEAT-EM-26
### Feature Overview
Operating schedules make temporal availability and exceptions explicit for a service. They distinguish an existing offering from one available at the relevant time, while leaving actual resource readiness and operational capacity as separate concerns.

## FEAT-EM-27
### Feature Overview
Eligibility specification captures the clinical thresholds and geographical bounds applicable to service access. It provides the criteria context needed to consider a request without making publication of those criteria an individual eligibility decision or a completed allocation.

## FEAT-EM-28
### Feature Overview
Device type specification provides model-level capabilities and regulatory context against which individual devices can be understood. It describes the kind of equipment without substituting for the release, condition or deployment of a particular instance.

## FEAT-EM-29
### Feature Overview
Serial tracking makes individual hardware distinguishable within the device estate. A unique device reference supports subsequent assignment and source interpretation, while remaining separate from evidence that the device is maintained, connected or fit for a particular clinical use.

## FEAT-EM-30
### Feature Overview
Dynamic device association preserves which patient or physical place a device is linked to as assignments change. This is essential context for interpreting telemetry: a stable device identity does not imply that every observation concerns the same patient over time.

## FEAT-EM-31
### Feature Overview
Endpoint registration supplies the interfaces and credential context through which a diagnostic or telemetry feed can participate. It describes provisioned connectivity rather than proving that data has arrived, been clinically interpreted or acquired authoritative standing.

## FEAT-SA-01
### Feature Overview
Demographic ingestion receives administrative changes from upstream admission and registration systems. It makes externally established updates available for governed processing without treating each incoming assertion as an independent master-person adjudication by Harmonia.

## FEAT-SA-02
### Feature Overview
Change distribution propagates verified demographic updates to participating clinical systems. It supports consistent subject context across the network while keeping delivery to each destination distinguishable from that destination's eventual incorporation or business response.

## FEAT-SA-03
### Feature Overview
Referral ingestion establishes the received referral material within the integration boundary. It preserves the request for another participant's involvement without treating successful receipt as clinical acceptance, responsibility assumption or a promise that care will be delivered.

## FEAT-SA-04
### Feature Overview
Supporting information gives the referral its relevant history and clinical context. Association makes the material available for the recipient's consideration while preserving source provenance and the distinction between evidence supplied and the recipient's own clinical decision.

## FEAT-SA-05
### Feature Overview
Status and outcome tracking makes explicit referral dispositions visible to the referring participant. Acceptance, decline and redirection remain meaningful alternatives; a transport acknowledgement does not fill in a missing business decision or establish a universal referral progression.

## FEAT-SA-06
### Feature Overview
Appointment ingestion receives scheduling facts established by departmental systems. It supports shared awareness of planned care opportunities without claiming authority over the underlying booking decision or proving that attendance and clinical fulfilment will occur.

## FEAT-SA-07
### Feature Overview
Status synchronisation keeps cancellation, rescheduling and attendance information coherent across coordination contexts. The purpose is propagation of the externally established booking change rather than independent scheduling or inference of a clinical outcome from appointment state.

## FEAT-SA-08
### Feature Overview
Encounter context creation supplies an identifiable frame for information concerning a particular admission, visit or contact. Its governed tracking authority concerns the encounter representation within Harmonia; it does not make Harmonia the originator of every clinical fact associated with that encounter.

## FEAT-SA-09
### Feature Overview
Encounter progression preserves the difference between planned, arrived, active, leave and discharged circumstances. These states make administrative activity interpretable without implying a clinical outcome. The source's ON_LEAVE transition detail remains incomplete in the corresponding Process and is not repaired here.

## FEAT-SA-10
### Feature Overview
Encounter association keeps the relevant place and care-team context aligned with the episode as the patient moves. Location changes are meaningful operational facts, but they do not themselves establish a transfer of clinical responsibility.

## FEAT-SA-11
### Feature Overview
Order ingestion establishes the received bounded fulfilment request with its ordering-clinician context. It begins the information account needed for a closed loop while leaving the performing service's business acceptance and execution distinguishable from technical ingress acceptance.

## FEAT-SA-12
### Feature Overview
Destination resolution connects an order with the service endpoint intended to fulfil it. Routing advances the request towards that destination without proving execution or changing the original clinical intent simply because a transport route was chosen.

## FEAT-SA-13
### Feature Overview
Closed-loop tracking keeps delivery, technical acknowledgement, business disposition, execution and returned outcome distinct. Its purpose is to expose what has actually been established at each stage, preventing a successful handoff from being reported as eventual fulfilment.

## FEAT-SA-14
### Feature Overview
Cancellation and amendment coordination carries a requested change into the performing-system context. It preserves the difference between asking to change an order and evidence that the change took effect, rather than silently rewriting the original request as though no prior intent existed.

## FEAT-SA-15
### Feature Overview
Diagnostic request correlation connects the order with its specimen and encounter context. It helps distinguish evidence belonging to the intended fulfilment from superficially similar results, without treating association as proof of specimen suitability or diagnostic interpretation.

## FEAT-SA-16
### Feature Overview
Report ingestion binds a finalised diagnostic contribution to the order it answers and verifies content integrity. The report's professional standing comes from its diagnostic source; successful ingestion does not independently authorise its findings or establish a patient-level clinical conclusion.

## FEAT-SA-17
### Feature Overview
This Feature distributes finalised diagnostic reports to the ordering and designated care participants. Its narrower finalised-report scope is preserved even though broader Diagnostic Administration behaviour also handles preliminary, corrected and addendum information; those behaviours do not silently redefine the Feature.

## FEAT-SA-18
### Feature Overview
Medication order ingestion preserves the intended therapy expressed by inpatient or discharge prescriptions. The received order is evidence of clinical intent, distinct from pharmacy dispensing and the patient's subsequent administration record.

## FEAT-SA-19
### Feature Overview
Dispense tracking receives the pharmacy's evidence that medication was supplied under the applicable context. It connects supply with the medication account without inferring that the patient received a dose or that Harmonia performed the professional dispensing activity.

## FEAT-SA-20
### Feature Overview
MAR ingestion captures source-reported point-of-care administration events. It preserves the crucial distinction between intended or supplied medication and an administration recorded as performed, without originating that bedside event or its clinical authority.

## FEAT-SA-21
### Feature Overview
Procedure booking ingestion makes scheduled cases and procedural requests available in the wider care context. It conveys intended or arranged activity while retaining the distinction between a booked procedure and one actually performed.

## FEAT-SA-22
### Feature Overview
Procedural documentation ingestion preserves the operative, anaesthetic and post-procedure account supplied by the clinical service. It connects performed-care documentation with the wider record without giving the integration process authority to originate or judge the clinical findings.

## FEAT-SA-23
### Feature Overview
Clinical document ingestion receives authored documents into governed information management. It supports both structured and unstructured contributions while preserving that receipt establishes a managed contribution, not independent clinical verification of every assertion in the document.

## FEAT-SA-24
### Feature Overview
Versioning and supersession make document change understandable without destructive loss of the previous account. Author and amendment attribution distinguish who established each contribution; withdrawal or correction changes current standing without retrospectively erasing what Harmonia previously received or managed.

## FEAT-SA-25
### Feature Overview
Metadata indexing makes documents discoverable by clinically and operationally relevant attributes. It describes content for search without making the index the document itself or granting access merely because a matching record can be located.

## FEAT-SA-26
### Feature Overview
Care-plan exchange keeps authorised team members aware of established multidisciplinary intent and its updates. It provides information continuity while preserving clinical planning authority and the distinction between a distributed plan and care actually performed.

## FEAT-SA-27
### Feature Overview
Clinical communication distribution connects verified providers and designated team endpoints through governed addressed delivery. The responsibility concerns reaching the intended participant, rather than assuming that delivered information has been read, incorporated or acted upon clinically.

## FEAT-SA-28
### Feature Overview
Acknowledgement tracking associates delivery and read evidence with the communication transaction. Each receipt retains its contractual meaning; neither delivery nor reading alone proves business acceptance, clinical review, approval, finality or originating authority.

## FEAT-SA-29
### Feature Overview
Communication audit evidence preserves the attributable transaction history required for scrutiny of clinical communication. It records meaningful communication facts under the zero-PHI constraints rather than turning the audit trail into a copy of message content or a permanent diagnostic trace.

## FEAT-SD-01
### Feature Overview
Shared-record ingestion brings general-practice contributions into cross-provider continuity. The summary or encounter note retains its clinical source while becoming available alongside regional information; consolidation does not transfer the GP's professional authority to the platform.

## FEAT-SD-02
### Feature Overview
Primary-care notifications inform the registered GP of significant acute-care events. This outward notification responsibility is distinct from querying a chronic-care timeline: the latter shares context but has no established equivalence to dispatching the required arrival, admission or discharge notice.

## FEAT-SD-03
### Feature Overview
Acute-state capture records deterioration notifications and current clinical status as externally established information. Approved reusable clinical-information composition is sufficient for this responsibility; routing a critical-result alert does not become a specialised capture Function merely because both concern urgent care.

## FEAT-SD-04
### Feature Overview
Acute context provision brings relevant history and active medications together for clinicians facing a current presentation. It supports rapid understanding without substituting the consolidated view for clinical assessment, triage or a decision about treatment.

## FEAT-SD-05
### Feature Overview
Emergency presentation ingestion retains the arrival, triage category and presenting complaint reported by the emergency context. It makes that event available for regional correlation while preserving that the clinical triage judgement was established by the responsible source.

## FEAT-SD-06
### Feature Overview
Cross-facility correlation connects an emergency presentation with relevant recent history and allergy information. It supports situational understanding across institutional boundaries without deciding the clinical significance of the combined evidence or assigning emergency treatment.

## FEAT-SD-07
### Feature Overview
Rounding context brings results, medication records and nursing notes into an accessible multidisciplinary view. Assembly serves the clinical discussion but does not perform rounds, determine pending clinical work or establish the team's professional conclusions.

## FEAT-SD-08
### Feature Overview
Inpatient progression tracking captures clinical milestones reported by the responsible participants. General clinical-information capabilities and configured workflow composition suffice for the established Feature; capture does not require inventing a specialised Function or make Harmonia the authority declaring clinical stability.

## FEAT-SD-09
### Feature Overview
Result distribution connects established diagnostic reports with ordering and designated secondary recipients. It supports continuity of diagnostic information across care settings while retaining the distinction between delivery, recipient review and the clinical decision based on the result.

## FEAT-SD-10
### Feature Overview
Diagnostic history consolidation makes prior evidence visible across regional providers. A combined historical view can support comparison over time without itself establishing a diagnostic interpretation, reconciling conflicting clinical assertions or replacing each report's source standing.

## FEAT-SD-11
### Feature Overview
This Feature receives medication histories already reconciled by clinical participants during care transitions. The platform preserves and connects that account; ingestion is distinct from performing the clinical reconciliation or independently deciding which regimen is correct.

## FEAT-SD-12
### Feature Overview
Medication tracking conveys orders, dispensing changes and administration information across care boundaries while keeping their meanings separate. This supports a coherent therapy account without treating a supplied medicine as an administered dose or a distributed order as treatment performed.

## FEAT-SD-13
### Feature Overview
Immunisation submission connects a source-recorded administration event with the national register context. It supports continuity and reporting without performing the immunisation or making successful transmission equivalent to every downstream registry outcome.

## FEAT-SD-14
### Feature Overview
Screening recall distribution conveys established reminders to the designated care providers. Its notification responsibility is expressible through approved event and workflow composition; the catalogue does not invent a specialised Function or transfer the clinical decision to recall the patient.

## FEAT-SD-15
### Feature Overview
Community encounter ingestion brings home-visit and community assessments into the longitudinal information context. It supports continuity across organisational boundaries while preserving source attribution and the difference between the documentation received and care performed by the platform.

## FEAT-SD-16
### Feature Overview
Community plan synchronisation keeps hospital outreach and community participants informed of established plan changes. It supports shared clinical intent without allowing synchronisation to become authority to choose that intent or allocate clinical work.

## FEAT-SD-17
### Feature Overview
Outreach context provision prepares portable summaries for care beyond the usual connected environment. Their usefulness depends on retaining clinical and temporal context; an offline-capable copy is not automatically current authoritative state or a new origin of clinical facts.

## FEAT-SD-18
### Feature Overview
Outreach outcome ingestion brings documentation collected during remote visits back into the governed record. It reconnects previously separated activity with the regional context without erasing when, where and by whom the source information was established.

## FEAT-SD-19
### Feature Overview
Remote telemetry ingestion accepts measurements and patient-reported contributions from outside the usual care site. The information can inform subsequent monitoring and interpretation, but acquisition alone does not establish clinical significance or authorise an intervention.

## FEAT-SD-20
### Feature Overview
Virtual-session binding associates a consultation with the enduring record and the relevant episode. It preserves the clinical context across delivery modes without turning the communication session itself into proof of clinical conclusions or adequacy of care.

## FEAT-HSO-01
### Feature Overview
Clinic-session tracking records the operating session's start, delay and completion as service-level context. It is distinct from an individual patient's queue state: knowing where one patient is waiting does not establish whether the clinic session is running as planned.

## FEAT-HSO-02
### Feature Overview
Operational queue progression makes the patient's passage through clinic stages visible. The queue concerns real-world arrival, waiting and consultation context, rather than a broker queue, and does not give Harmonia authority to determine clinical priority or own the clinician's worklist.

## FEAT-HSO-03
### Feature Overview
Ward flow coordination connects occupancy, place allocation and bed status within the ward's operational context. Those facts contribute to ward capacity but do not exhaust its acuity, staffing and cohorting responsibilities or prove that another patient can be accepted.

## FEAT-HSO-04
### Feature Overview
Handover context assembly brings patient lists, current concerns and pending tasks together for the participants conducting a shift handover. Assembly is the established responsibility: it does not perform clinical review, decide pending clinical work or manage the handover itself.

## FEAT-HSO-05
### Feature Overview
Case progression records externally established surgical milestones in an operationally meaningful sequence. It makes the theatre situation visible without authorising or performing the clinical acts denoted by anaesthesia, incision, closure or recovery.

## FEAT-HSO-06
### Feature Overview
Perioperative notification connects relevant case milestones with portering, sterilisation and recovery participants. Observation alone does not realise the Feature's dispatch responsibility; the established case-progression behaviour includes the required alerts without adding another Function or Service.

## FEAT-HSO-07
### Feature Overview
Emergency-place tracking distinguishes occupancy and status across cubicles, resuscitation bays and short-stay beds. It provides place context for departmental operations while leaving the service's usable capacity and clinical triage decisions separate from an inventory of spaces.

## FEAT-HSO-08
### Feature Overview
Length-of-stay tracking makes waits and operational timing thresholds visible for escalation. The timing information supports management of impediments without determining clinical urgency or making an elapsed-time alert an independent assurance finding.

## FEAT-HSO-09
### Feature Overview
Outpatient arrival registration connects a check-in event with the treating context and required notification. Approved reusable composition supplies this integration responsibility; it does not require a specialised arrival Function or prove that a consultation has begun or finished.

## FEAT-HSO-10
### Feature Overview
Post-clinic coordination captures the follow-up bookings and diagnostic requirements established at clinic completion. It connects those requirements with subsequent operational activity while preserving that capture is not the clinical decision, a completed booking or diagnostic fulfilment.

## FEAT-HSO-11
### Feature Overview
Availability tracking preserves the operational distinctions among vacancy, occupancy, reservation, blockage and cleaning conditions. These states support meaningful care-place coordination without equating an unoccupied bed with a ready place or usable service capacity.

## FEAT-HSO-12
### Feature Overview
Bed-state progression applies the consequences of relevant clinical and environmental events to the managed place state. The resulting change is an operational fact, such as a need for cleaning after departure, rather than a transfer of clinical responsibility.

## FEAT-HSO-13
### Feature Overview
Readiness notification exposes the established readiness of a care place to the responsible allocation participants. It makes a relevant transition actionable without deciding the clinical suitability of the next allocation or establishing whole-service readiness.

## FEAT-HSO-14
### Feature Overview
Equipment tracking provides the dynamic location and allocation context of shared devices. It distinguishes an identifiable asset from one available for the present operational need, while leaving maintenance, ownership and clinical authorisation with their respective responsibilities.

## FEAT-HSO-15
### Feature Overview
Capacity aggregation combines occupancy, waiting and diversion context across facilities. The higher-order view must retain the originating service-specific capacity meaning; a shared metric does not transfer clinic, ward or theatre capacity responsibility to the aggregator.

## FEAT-HSO-16
### Feature Overview
Mobile dispatch exchanges externally established assignment, dispatch and progression information with business mobile-work mechanisms. The retained name does not grant Harmonia authority to allocate clinical work, manage handover or own EMR worklists, and external task terminology does not redefine Synthetic Task.
### Boundary
The proposed replacement name remains unconfirmed. This entry preserves the existing identity and the human-adjudicated integration boundary.

## FEAT-HSO-17
### Feature Overview
Mobile visit tracking receives arrival, progression and safety checkpoints from roving workers. It makes reported activity visible in the wider operational context without originating the clinical visit, deciding its clinical content or assuming that a checkpoint proves the intended clinical outcome.

## FEAT-HSO-18
### Feature Overview
Active on-call resolution identifies the current provider in the required specialty and hospital context. It supports correct routing at the relevant time without authoring the roster or treating current coverage as proof of qualification, privilege or responsibility acceptance.

## FEAT-HSO-19
### Feature Overview
Work-item instantiation gives an authorised operational requirement an identifiable unit of work. It distinguishes recognising that support activity is needed from assigning it, dispatching it and recording its completion; a clinical trigger does not make the resulting support item clinical work authority.

## FEAT-HSO-20
### Feature Overview
Worker matching relates an operational requirement to eligible roles, credentials, locations and availability under policy. It consumes those constraints rather than changing their authoritative records, and does not turn operational allocation into permission to decide clinical work.

## FEAT-HSO-21
### Feature Overview
Dispatch delivery brings assigned operational work to the intended worker or team and retains acknowledgement evidence. Technical receipt and operational acknowledgement must remain distinguishable; neither alone proves that the work was performed or its intended outcome achieved.

## FEAT-HSO-22
### Feature Overview
Progress oversight identifies stalled or unacknowledged operational work against applicable timing expectations. Escalation serves completion management and does not itself constitute independent assurance or transfer authority over clinical decisions related to the support activity.

## FEAT-HSO-23
### Feature Overview
The qualification checkpoint considers active professional credentials and competency evidence before specialised dispatch. Its historical credential terminology does not make it the operational badge or physical-access responsibility of Credential Management, nor does a qualification check alone establish all required privilege.

## FEAT-HSO-24
### Feature Overview
Transport ingestion establishes the requested movement and applicable clinical mobility constraints. It gives operational coordination the context needed to arrange transit without making the request itself a departure, arrival or accepted care transition.

## FEAT-HSO-25
### Feature Overview
Transport progression connects dispatch, departure, transit and arrival evidence for a physical patient journey. The checkpoints make movement intelligible while preserving that reaching a destination does not establish clinical handover or assumption of responsibility there.

## FEAT-HSO-26
### Feature Overview
Specimen transport tracking retains pickup, custody and condition context through laboratory delivery. It supports interpretation of the material's journey without treating a delivered package as proof of specimen suitability, completed analysis or an authorised diagnostic report.

## FEAT-HSO-27
### Feature Overview
Discharge milestone tracking records confirmations contributed by the relevant clinical and operational participants. It makes outstanding requirements visible without allowing the tracking mechanism to declare clinical readiness or assume that every local milestone defines a universal discharge pathway.

## FEAT-HSO-28
### Feature Overview
Readiness oversight brings multidisciplinary, home-care and transport confirmations into one coordination view. Aggregation supports the responsible participants' decisions but does not itself confer authority to discharge or establish acceptance of continuing care by another provider.

## FEAT-HSO-29
### Feature Overview
Departure notification communicates the physical exit and associated record and place-state context to the applicable participants. It distinguishes departure information from earlier readiness and from the clinical decision, while retaining the separate question of responsibility actually assumed elsewhere.

## FEAT-ISE-01
### Feature Overview
Longitudinal assembly relates contributions from disparate systems into a usable chronology. The unified view preserves source and subject context rather than turning consolidation into originating clinical authority or erasing differences in the standing of individual assertions.

## FEAT-ISE-02
### Feature Overview
Active access supports responsive use of consolidated clinical summaries in ongoing care. The working representation remains reconstructable active state; speed and availability do not promote it to authoritative durable truth when persistence or convergence is unavailable.

## FEAT-ISE-03
### Feature Overview
Durable preservation retains the governed longitudinal representation and the history of what Harmonia received or managed. Corrections establish changed knowledge without retrospectively rewriting that history, and preservation neither originates clinical facts nor automatically establishes legal qualification of the record.

## FEAT-ISE-04
### Feature Overview
Direct submission supports a sender's deliberate push of information across an exchange boundary. It preserves the distinction between ingress or delivery acceptance and the recipient's business handling, under the actual contract rather than an assumed meaning of transport success.

## FEAT-ISE-05
### Feature Overview
Retrieval mediation connects a request with information held in participating repositories. It makes distributed retrieval possible without transferring repository authority, acquiring the clinical payload or treating a resolvable request as automatic permission to disclose.

## FEAT-ISE-06
### Feature Overview
Addressed distribution sends information to explicitly designated recipients and tracks each destination's delivery separately. This preserves partial outcomes in a fan-out rather than collapsing several deliveries into a single success that hides a missing recipient.

## FEAT-ISE-07
### Feature Overview
Syndication connects published changes with subscribers whose registered interests match them. It differs from named-recipient distribution in how consumers are selected, while retaining governed delivery and the distinction between notification and the subscriber's eventual business action.

## FEAT-ISE-08
### Feature Overview
Federated query makes relevant clinical and directory information discoverable across distributed contexts using bounded criteria. Discovery is a governed information-access responsibility rather than authority to disclose every match or reinterpret the clinical meaning of the results.

## FEAT-ISE-09
### Feature Overview
Access-qualified filtering restricts the disclosed result to the verified caller's authority and applicable consent. The responsibility is more specific than retrieving a resource; the latter's shared owner and subject matter do not establish a Feature realisation relationship.

## FEAT-ISE-10
### Feature Overview
Boundary exchange preserves the standards contract understood by the external participant. Harmonia's internal management metadata must not leak into that contract, and publication must construct an allowed external representation without destructively changing the internally managed information.

## FEAT-ISE-11
### Feature Overview
Access and consent control evaluates whether an information operation is legitimate in its practitioner, patient and purpose context. It supplies policy determinations consumed by access and presentation paths rather than allowing those consumers to establish authority from caller-supplied content.

## FEAT-ISE-12
### Feature Overview
Security-context propagation carries attributable operational authority across asynchronous boundaries. Its responsibility is preservation of established context, distinct from evaluating a consent constraint; shared security subject matter is insufficient to assign that Function to this Feature.

## FEAT-ISE-13
### Feature Overview
Security-significant audit evidence makes governed actions attributable and verifiable while respecting PHI restrictions. The record concerns meaningful security facts under policy rather than routine implementation events, and recording it does not independently establish an assurance conclusion.

## FEAT-ISE-14
### Feature Overview
Concept resolution makes a coded clinical reference interpretable against its governed terminology. It validates the reference context without making a recognised code proof of clinical truth or giving the consuming system authority to redefine the concept.

## FEAT-ISE-15
### Feature Overview
Terminology mapping applies governed correspondence between local and canonical semantic systems. Translation preserves the limitations and authority of the map; it does not justify silently treating related source concepts as identical or clinically adjudicating the underlying information.

## FEAT-ISE-16
### Feature Overview
Clinical collaboration coordination binds secure multidisciplinary exchange to the relevant encounter or clinical topic. It supports conversation among authorised participants without making discourse an authoritative observation or acquiring the clinical handover authority excluded by the metamodel.

## FEAT-ISE-17
### Feature Overview
In-conversation query supplies governed clinical summaries within an authorised collaboration context. The convenience of retrieving information during discussion does not weaken consent or access controls, and displaying a summary does not promote the surrounding conversation into the clinical record.

## FEAT-ISE-18
### Feature Overview
Metadata governance keeps patient names, identifiers and sensitive detail out of collaboration titles, topics and aliases. Opaque references preserve useful contextual association without placing PHI in broadly visible channel metadata or diagnostic surfaces.

## FEAT-ISE-19
### Feature Overview
Operational chat provides secure communication for administrative and support teams. It enables participants to discuss work while leaving assignment, completion and the authority of operational decisions with the capabilities responsible for that activity.

## FEAT-ISE-20
### Feature Overview
Work Order progression coordinates human operational doing as a distinct work meaning. Dispatch, acceptance and completion describe the undertaking without making the workflow engine the authority over clinical work or treating a receipt as performance.

## FEAT-ISE-21
### Feature Overview
To Do coordination supports human review, sign-off and decision activity. Its progression can expose whether the review item has been handled while leaving the meaning and authority of the professional or governance decision with the responsible participant.

## FEAT-ISE-22
### Feature Overview
Synthetic Task orchestration governs non-human executable work and its sequencing. This technical work meaning remains distinct from human Work Orders, clinical worklists and external systems' generic task terminology; incomplete stalled/failed Process paths are not supplied by convention.

## FEAT-ISE-23
### Feature Overview
Timeout oversight observes deadlines and initiates the established coordination response to stalled activity. It is management of progression rather than an assurance conclusion, and escalation does not grant the coordinator authority over the clinical meaning of the underlying work.

## FEAT-ISE-24
### Feature Overview
Schedule projection makes external roster, on-call and facility calendars available in operational views. It preserves their temporal usefulness without making the projection the authoritative schedule or granting authority to alter staffing and coverage decisions.

## FEAT-ISE-25
### Feature Overview
Temporal correlation relates incoming events to the relevant appointment or calendar milestone. Association provides context for interpretation and coordination without assuming that temporal proximity proves attendance, fulfilment or the clinical outcome expected at that time.

## FEAT-ISE-26
### Feature Overview
Contextual presentation makes clinical timelines, directories and operational information usable for people. It consumes established identity and security context while preserving the originating authority of what is shown; a user interface does not become an alternative persistence or policy boundary.

## FEAT-ISE-27
### Feature Overview
Semantic governance provides canonical definitions, bindings and profiles through which regional representations can be understood consistently. Its purpose concerns meaning and constraints rather than a particular schema technology, and consuming a definition does not transfer its governing authority.

## FEAT-ISE-28
### Feature Overview
Conformance verification evaluates a representation against published semantic rules and constraints. It distinguishes meaningful conformity from simple parseability while retaining that a conformant message is not necessarily clinically true, authoritative or permitted for disclosure.
