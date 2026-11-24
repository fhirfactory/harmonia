<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
<!-- Authored interpretation of fingerprinted RAD architecture; not new architectural authority. -->

## Individual Care Delivery
### Capability Overview
Individual Care Delivery is the organisation's ability to understand an individual's health and act on that understanding through purposeful care. Its six constituent abilities distinguish establishing findings, interpreting their clinical meaning, choosing a response, performing care, observing change and evaluating care against its objectives. These distinctions describe responsibilities and outcomes rather than a compulsory sequence: a consultation or continuing episode may revisit several of them.

The same ability can be exercised in a hospital, a general practice, a home or a virtual consultation. Setting and time pattern affect the circumstances of care without changing the responsibility represented by this capability.
### Intent
Support clinically meaningful care whose intent, performance, observed effects and continuing appropriateness can be distinguished.
### Boundary
Care access, appointments and operational coordination enable care but do not establish that it has been performed. Clinical judgement and the authority to deliver care remain with the responsible participants; possession or coordination of their information does not transfer that authority to Harmonia.
### Harmonia Architectural Relevance
Harmonia supplies and connects information used in individual care. The approved Health Information Access support trace is bounded: records can inform care, but direct assessment can establish findings independently, and access to a proposed intervention does not authorise its performance.

## Individual Health Assessment
### Capability Overview
Assessment establishes what is currently known about an individual's health through history, examination and measurement. Within Individual Care Delivery it supplies assessed findings, including findings obtained directly from the person rather than retrieved from an existing record. Its three children distinguish ways of establishing knowledge, rather than professions, clinical specialties or locations.
### Intent
Give subsequent clinical interpretation a defensible account of the person's current state and relevant history.
### Boundary
Finding an abnormal measurement is distinguishable from deciding what it means clinically. Diagnosis, prognosis, selection of care and performance of care belong to adjacent abilities.

## Clinical Determination
### Capability Overview
Clinical Determination turns assessed findings into clinically interpreted understanding of a person's condition, risk or prognosis. It represents the professional responsibility for deciding what the findings mean in the individual's clinical context, including the significance of changes identified through monitoring. This is the interpretive responsibility within Individual Care Delivery, rather than another method of collecting evidence.
### Intent
Establish clinical conclusions on which an intended response can be based.
### Boundary
Determining clinical meaning does not itself select the care response. No CT3 taxonomy has been adjudicated; diagnosis and prognostic judgement illustrate this responsibility without becoming additional catalogue elements.

## Individual Care Planning
### Capability Overview
Individual Care Planning establishes the intended course of care in response to assessed and clinically interpreted health information. It connects clinical understanding to objectives, intended interventions and monitoring needs. Its outcome is an expression of what should happen, allowing intended care to remain distinguishable from requests, operational arrangements and actions subsequently performed.
### Intent
Make the clinical response and its objectives explicit enough to guide care and later evaluate its effectiveness.
### Boundary
Prescribing can express clinical intent without establishing dispensing or administration. Scheduling or allocating resources does not perform the planned intervention. No CT3 decomposition is established.

## Clinical Intervention
### Capability Overview
Clinical Intervention is the ability to perform a clinical action under an established care intent. It marks the transition from intended care to care actually performed, whether the action seeks to alter, maintain or otherwise manage health. Its exercise can establish that treatment occurred even when the desired clinical result was not achieved.
### Intent
Put the intended clinical response into practice through appropriately authorised care.
### Boundary
Orders and appointments express or arrange future activity; they are not evidence that the intervention occurred. Therapy, medication and procedural examples do not constitute an approved three-child taxonomy: that proposed decomposition remains provisional.

## Individual Health Monitoring
### Capability Overview
Monitoring develops a view of an individual's health over time so that change, stability or deviation can be recognised. Health State Monitoring establishes the aggregate temporally situated view; Care Response Monitoring considers that view in relation to a particular condition and treatment. Together they distinguish observation over time from a single measurement and from the clinical judgement prompted by a trend.
### Intent
Make relevant trajectories visible while preserving what was observed and what still requires interpretation.
### Boundary
Monitoring does not establish the clinical significance of a change or judge whether care achieved its objectives. Those responsibilities remain Clinical Determination and Individual Care Review respectively.

## Individual Care Review
### Capability Overview
Individual Care Review asks whether care is achieving its intended objectives and whether the existing care intent remains suitable. Its children separate effectiveness from appropriateness: an intervention may have an effect yet no longer be suitable to continue, or remain a reasonable choice despite an inadequate response. Review therefore evaluates the relationship between care, outcomes and intent rather than simply repeating assessment.
### Intent
Establish whether continuing the current approach is justified and whether reconsideration is needed.
### Boundary
Recognising a need to change care does not establish the replacement plan. Monitoring supplies relevant observations; planning establishes revised intent.

## Care Access & Coordination
### Capability Overview
This capability makes care reachable and operationally coherent across participating services. It encompasses entering care, obtaining scheduled opportunities and usable capacity, coordinating participation and responsibility transitions, and arranging bounded support logistics. Its decomposition separates access decisions, resource use, continuity and supporting work so that movement or message delivery cannot stand in for acceptance of care responsibility.
### Intent
Help a person's care requirements progress across organisational and operational boundaries with the relevant access and continuity context intact.
### Boundary
Coordination does not determine clinical work or confer clinical authority. Corporate resource governance and general hospital logistics are outside the bounded care-support responsibility.
### Harmonia Architectural Relevance
Referrals, encounters, appointments, operational state and communication provide material information for continuity. Harmonia's integration and coordination support does not make it the owner of clinical handover or the originating workforce and resource facts.

## Care Access Management
### Capability Overview
Care Access Management establishes the circumstances in which an individual seeks or enters care. Intake creates the access context, referral requests another participant's involvement, and eligibility determines whether applicable access conditions are met. These are separate contributions to access: a person can be eligible without having a booking, and a received referral can remain undecided.
### Intent
Make entry requirements and referral disposition intelligible without implying that care has already been accepted or delivered.
### Boundary
Eligibility does not allocate care, and referral receipt does not establish an assumption of clinical responsibility.

## Care Scheduling & Capacity Management
### Capability Overview
This capability connects care demand with scheduled opportunities, suitable care places and legitimately usable operational resources. Its children distinguish a booking, a place allocation, the readiness of that place, resource deployment, flow coordination and service-level usable capacity. An empty bed or an available practitioner is relevant evidence, but neither alone establishes that the service can accommodate care.
### Intent
Support care requirements with an operationally credible combination of time, place, resources and capacity.
### Boundary
Deployment consumes roster, qualification and privilege context without changing their authoritative records. Operational capacity differs from strategic capacity planning and from the unresolved collective service-readiness concern.

## Care Coordination & Transition
### Capability Overview
Care Coordination & Transition supports continuity by keeping participation, actual responsibility assumption and discharge progression distinct. Care teams may collaborate while responsibility is shared or changes, but physical transit and information exchange do not themselves establish who has accepted care. The capability therefore addresses continuity of accountable participation as well as the practical circumstances of departure.
### Intent
Avoid gaps in care continuity caused by treating a movement or communication as an accepted responsibility transition.
### Boundary
Harmonia may assemble handover information without managing clinical handover. Arrival, discharge readiness and physical departure are not equivalent to responsibility acceptance.

## Care Support Logistics Management
### Capability Overview
Care Support Logistics Management coordinates the bounded physical and operational support needed to fulfil care. Patient transit, clinical-material movement and authorised support work have different subjects and completion evidence, even when the same operational environment supports them. This capability provides their common care-support frame without absorbing every logistical activity of a healthcare enterprise.
### Intent
Ensure necessary supporting activity can progress with the relevant patient, material and operational constraints visible.
### Boundary
Assignment does not establish a worker's qualification or clinical authority. Enterprise supply chains, facilities and workforce governance retain their own responsibilities.

## Health Rights, Advocacy & Participation
### Capability Overview
This capability represents the organisation's responsibility to protect patients' rights and enable their voice in health decisions. Consent, advance care directives, advocacy and consumer participation concern the legitimacy and personal consequences of care, rather than merely the availability of clinical information. The governed model retains this broad enterprise responsibility without an established subordinate capability set.
### Intent
Support informed participation and respect for the individual's rights in decisions affecting their health.
### Boundary
Recording a directive or restricting access can support rights without performing advocacy or managing consumer participation.
### Harmonia Architectural Relevance
The established Adjacent classification reflects the use and supply of consent and record-access evidence. It does not assign the underlying rights, advocacy or participation activity to Harmonia.

## Diagnostic, Therapeutic & Clinical Support Services
### Capability Overview
Specialised services contribute evidence, professional interpretation, medication provision and therapeutic support to individual care. The established decomposition distinguishes diagnostic provision from medication and pharmacy services, while retaining wider therapeutic and allied-health scope as enterprise context. The catalogue deliberately explains the modelled branches without presenting them as an exhaustive inventory of clinical support.
### Intent
Provide the specialised contributions that treating participants need to assess and care for individuals.
### Boundary
Producing diagnostic evidence, authorising a report, dispensing medication and administering treatment establish different outcomes and authorities.
### Harmonia Architectural Relevance
Requests, specimen associations, reports and medication events require reliable correlation and exchange. Supporting those information flows does not make Harmonia the diagnostic practitioner, pharmacy or therapy provider.

## Diagnostic Support Provision
### Capability Overview
Diagnostic Support Provision covers the distinct responsibilities through which a diagnostic service obtains evidence and makes its findings available. Diagnostic scheduling, service performance, specimen stewardship, data management, interpretation, report governance and report distribution each establish a different outcome. Their separation allows preliminary, amended or final information to retain its professional and evidentiary standing without treating delivery as authorisation.
### Intent
Make diagnostic evidence and professionally governed findings usable by the appropriate care participants.
### Boundary
Diagnostic interpretation is limited to the service's professional scope; patient-level Clinical Determination considers the wider clinical context. The children do not prescribe a universal diagnostic workflow or establish a Diagnostic Order Management capability.

## Medication & Pharmacy Services
### Capability Overview
This capability frames professional medication provision and medication-specific advice. Prescription dispensing, non-prescription provision, advice and dose administration aids answer different needs, even where exercised by one pharmacy. The decomposition keeps the authorised regimen, provision of medicine and actual administration separate so that a supply event is not mistaken for treatment performed.
### Intent
Support safe, professionally controlled access to and use of medication within the established pharmacy responsibility.
### Boundary
Advice does not confer prescribing authority, and dose aids do not establish a regimen. Whether specialised compounding/preparation warrants a separate capability remains unresolved under OI-BC-001.

## Health Products & Clinical Technology
### Capability Overview
This capability manages health products and clinical technology as business concerns in the care environment. Its established clinical-technology branch distinguishes knowledge of the device estate, functional state, deployment, user enablement, integration and lifecycle disposition. Product procurement and supply remain relevant context without a newly inferred Health Product Management decomposition.
### Intent
Keep the technology context needed for safe service activity and information exchange coherent and governed.
### Boundary
Device-specific enablement, clinical privilege and use in care are different concerns. The seven established device children do not resolve the separate maintenance-membership discrepancy.
### Harmonia Architectural Relevance
Integration provision and device-data integration materially affect exchanged information and its care context. Supply-chain, maintenance and clinical-use authority are not transferred to Harmonia.

## Clinical Technology Management
### Capability Overview
Clinical Technology Management treats a device as more than a data endpoint. Its responsibilities distinguish what device exists, what functionality and release it has, where and to whom it is deployed, how authorised users are supported, whether exchange is provisioned, what data is integrated and whether it should remain in service. These distinctions preserve the business meaning of the device estate beyond the subset of information used by Harmonia.
### Intent
Maintain intelligible device capability, deployment and lifecycle context for clinical service use.
### Boundary
Provisioning integration does not demonstrate that data has flowed. Monitoring, restoring device condition and deciding lifecycle disposition remain distinct; maintenance's tier membership requires adjudication.

## Clinical Quality, Safety & Improvement
### Capability Overview
This capability addresses operational clinical quality and safety, including concerns, incidents, response and improvement. Its established safety branch distinguishes risk identification, prospective mitigation and active response. Its quality branch distinguishes criteria, evidence, assessment and reporting. Together these make it possible to reason about clinical quality without equating an observation, a judgement and the action taken in response.
### Intent
Support recognition and management of clinical safety concerns and informed improvement of clinical quality.
### Boundary
This is neither a generic enterprise incident container nor a replacement for macro-level Health Service Assurance. Its bounded decomposition does not exhaust clinical quality and safety practice.
### Harmonia Architectural Relevance
Information about outcomes, risks, incidents and operational responses materially supports these responsibilities. Integrating that evidence does not assign clinical quality judgement or originating clinical authority to the platform.

## Clinical Safety Management
### Capability Overview
Clinical Safety Management distinguishes recognising a safety risk from reducing future exposure and actively dealing with a developing or materialised concern. These responsibilities may use overlapping evidence, but their purposes differ: characterisation makes the risk intelligible, mitigation changes prospective risk, and response addresses the situation requiring action.
### Intent
Support proportionate safety action grounded in the nature of the identified clinical risk.
### Boundary
Monitoring evidence is not automatically risk identification. Other capabilities can perform required actions without transferring their responsibilities; the three children are not a mandatory sequence or exhaustive safety taxonomy.

## Clinical Quality Management
### Capability Overview
Clinical Quality Management supplies the criteria and evidence basis for evaluating clinical quality and communicating established findings. Its four children preserve the difference between the expected standard, the material considered, the assessment reached and the report of that assessment. This supports scrutiny of why a quality conclusion is justified rather than treating the presence of measurements as a judgement.
### Intent
Make clinical-quality conclusions understandable in relation to their criteria, evidence and assessment authority.
### Boundary
Reporting does not originate the conclusion or perform subsequent improvement. Neither a separate monitoring provider nor generic Continuous Improvement placement is established by this decomposition.

## Community Health & Wellbeing
### Capability Overview
Community Health & Wellbeing frames organised activity supporting health beyond a single person's episode of care. The governed decomposition focuses on preventative health: identifying needs, defining programmes and cohorts, educating, observing programme operation and reporting. Wider outreach, environmental and social-determinant concerns remain enterprise context rather than additional modelled children.
### Intent
Support organised prevention with a clear relationship between the health problem, intended population and programme activity.
### Boundary
Programme management does not acquire the clinical or appointment responsibilities needed to deliver a programme.
### Harmonia Architectural Relevance
Programme and cohort distinctions materially shape the information, notifications and care enablement required. This supports the Relevant classification without assigning wider public-health operating authority to Harmonia.

## Preventative Health
### Capability Overview
Preventative Health organises a response to a health problem before adverse outcomes arise or worsen. The established children distinguish why attention is warranted, how an organised programme is managed, who it targets, the education provided, what programme operation shows and what is reported. This allows a screening or immunisation programme to be explained without creating a new capability for every programme type.
### Intent
Connect a preventative purpose with an identifiable target population and observable organised activity.
### Boundary
Programme performance differs from population-health assessment and individual clinical monitoring. Unmodelled specialist responsibilities are not silently assigned to the existing children.

## Population Health & Health-System Planning
### Capability Overview
This capability connects an understanding of population health with consideration of future health-service needs. Surveillance addresses population state, significant events and notification; planning assesses service demand and required capacity and distribution. Population evidence can inform planning without predetermining its conclusions or becoming the operational allocation of resources.
### Intent
Support informed public-health awareness and a reasoned view of the services a population will require.
### Boundary
Population conclusions differ from individual clinical findings, and planned capacity differs from capacity currently usable by a service.
### Harmonia Architectural Relevance
Governed population information and formal notifications materially support health activity. Consuming planning context does not make the platform the health-system planner or owner of its resources.

## Population Health Surveillance
### Capability Overview
Population Health Surveillance establishes patterns and change across a defined population and manages the surveillance arrangements through which they are considered. Its children separate longitudinal assessment, surveillance management, event identification, formal notification and reporting. These distinctions matter because an observed pattern can be significant without being notifiable, and identifying a notifiable event does not demonstrate that the notification obligation has been discharged.
### Intent
Make population-health concerns and applicable communication obligations visible in their population and temporal context.
### Boundary
Notification management does not define the obligation or own the operational response. Surveillance evidence can inform other abilities without establishing their findings.

## Health-System Planning
### Capability Overview
Health-System Planning considers the capacity and service footprint required to meet assessed demand. Demand Assessment establishes the basis and scale of need, Capacity Planning determines what is required, and Distribution Planning addresses where that capacity should sit across the service network. The decomposition preserves the difference between understanding demand and deciding a response to it.
### Intent
Support a defensible relationship between expected demand and the planned scale and distribution of health services.
### Boundary
Plans do not establish available resources or operational allocations. Detailed commissioning, capital, funding and workforce planning remain deliberately unmodelled.

## Health Information & Knowledge Management
### Capability Overview
This capability governs both health information and reusable clinical knowledge while preserving their different meanings. Health Information Management concerns content, structures, lifecycle, discovery context, grouping, correlation and retrieval. Clinical Knowledge Management concerns identifying and formulating reusable knowledge and establishing its governed standing. A source clinical fact can contribute to knowledge without becoming a general clinical conclusion merely through reuse.
### Intent
Keep information interpretable and usable across time and context while making the standing and limits of reusable knowledge explicit.
### Boundary
Managing or preserving information does not originate its clinical facts. Governing structures and binding concepts does not acquire the authority of the terminology they reference.
### Harmonia Architectural Relevance
These are substantive information-management responsibilities within Harmonia's assigned boundary. The Core classification remains bounded by information authority, active/durable-state separation and clinical decision authority.

## Health Information Management
### Capability Overview
Health Information Management maintains health information as meaningful governed content rather than isolated persisted values. Its children distinguish the content itself from the structures that explain it, lifecycle and history, discovery aids, contextual groupings, relationships and retrieval. These distinctions let an architect ask whether information exists, what it means, which state applies and whether it may be obtained without conflating those questions.
### Intent
Preserve usable health information whose meaning, relationships and temporal standing can be understood.
### Boundary
Discoverability is not permission, compartment membership is not an access decision, and correlation is not identity or clinical interpretation. Current state and immutable historical operational truth remain distinct.

## Clinical Knowledge Management
### Capability Overview
Clinical Knowledge Management turns potentially useful source material into explicit reusable knowledge with an intelligible basis and governed standing. Context identification and extraction preserve where material came from; formulation expresses applicability and rationale; concept association supplies semantic context; review and publication govern its intended use. These responsibilities distinguish source information, formulated knowledge and approved knowledge without prescribing a compulsory workflow.
### Intent
Make reusable clinical knowledge available with the qualifications and approval standing needed to judge its use.
### Boundary
Publication does not establish applicability to an individual or a clinical decision. Concept association does not redefine terminology, and formulation does not constitute approval.

## Standards, Semantics & Reference Governance
### Capability Overview
This capability establishes the organisational standing and appropriate use of models, standards and semantic artefacts. Standards governance addresses discovery, adoption, education, compliance and impacts; clinical-semantics governance addresses terminology, classifications and mappings. It provides the governed meaning within which information can be represented and exchanged, rather than merely maintaining technical schemas.
### Intent
Support consistent, appropriate interpretation and use of health-information standards and semantic references.
### Boundary
Using a terminology binding, mapping or classification does not transfer its governing authority. The established peer set must not be expanded simply to create a symmetrical taxonomy.
### Harmonia Architectural Relevance
Semantic and conformance governance is a substantive platform responsibility. Its organisational meaning constrains technical representations; implementation products do not define that meaning.

## Health Information Standards Governance
### Capability Overview
This capability governs which information models and standards the organisation adopts and how their use is supported and evaluated. The model distinguishes discovering candidates, adopting models, educating users, addressing conformity and overseeing governance. Standards impact identification has a separate established place because its implications may extend into clinical practice, policy and obligations beyond information-system implementation.
### Intent
Make organisational standards use deliberate and its consequences understandable.
### Boundary
Oversight evaluates governance without performing every peer activity. Adoption includes its own model-impact consideration; the listed abilities do not establish a sequential adoption Process.

## Clinical Semantics Governance
### Capability Overview
Clinical Semantics Governance maintains the standing and appropriate use of terminology, classifications and semantic mappings. Terminology provides consistent meaning; classification concerns sensitivity and consequences of exposure or mishandling. The established children distinguish governing artefacts from overseeing actual use and permit controlled terminology localisation where adopted material does not meet legitimate needs.
### Intent
Keep meaning and classification intelligible across organisational use without concealing semantic drift or local variation.
### Boundary
Compartmentalisation concerns contextual relationships rather than classification or terminology. Classification Localisation is not an established peer; mappings must preserve the separate meanings and authorities they connect.

## Connected Health Services
### Capability Overview
Connected Health Services represents the organisation's ability to exchange health information and communicate digitally between health-service participants. Its two children distinguish exchange of health-information artefacts from participant communication, notification and collaboration. Both support connected care, but a user viewing information through a screen is not thereby participating in a communication channel.
### Intent
Enable appropriate information movement and interaction between otherwise separate participants and services.
### Boundary
This organisational capability is not defined by an interface engine or messaging product. General presentation remains a separate enabling concern.
### Harmonia Architectural Relevance
Information exchange and digitally mediated interaction are direct platform responsibilities within their assigned contracts, supporting the Core classification without transferring payload or clinical authority.

## Health Information Exchange
### Capability Overview
As a Business Capability, Health Information Exchange is the organisational ability to move health information appropriately between participating parties, systems and services. It explains the enterprise purpose served by exchange without defining that purpose as a protocol, server or software product. Its placement under Connected Health Services distinguishes information exchange from the broader interaction and notification purposes of Digital Communication Channel.
### Boundary
No CT3 decomposition is established. The identically named BEC is a distinct enabling responsibility; the naming conflict remains unresolved, and this entry does not merge the two or assign the sharing-programme concern a parent.

## Digital Communication Channel
### Capability Overview
Digital Communication Channel supports communication between patients, practitioners and other participants through digitally mediated messages, notifications and collaborative interaction. Its organisational purpose is the exchange between participants: for example, a notification can connect someone to a care event without itself delivering care or changing the event's originating facts.
### Boundary
A presentation interface used to exercise another capability is not automatically this communication ability. No CT3 set or general presentation BEC placement is established here.

## Security, Privacy & Digital Trust
### Capability Overview
This capability establishes confidence in identity, coherent control of access, protection of information and accountability for its use. Its three branches distinguish identity/access governance, protection and privacy, and attribution/accountability. The scope includes physical and procedural arrangements as well as digital controls, preventing a software authorisation check from standing in for the organisation's whole trust responsibility.
### Intent
Support information use and governed activity within legitimate identity, authority, privacy and protection constraints.
### Boundary
Identifying an actor, permitting an action, recording participation and judging appropriateness are different outcomes. Evidence production is not itself independent assurance.
### Harmonia Architectural Relevance
Security and governance are intrinsic to Harmonia-managed operations. The Core classification concerns assigned platform controls and evidence; it does not acquire every organisational security or privacy responsibility.

## Digital Identity & Access Governance
### Capability Overview
This capability governs who may act and how controlled concerns are identified and protected across the organisation. Its children separate confidence in a claimed identity, identity models, contextual identity quality, authentication, access-control context, assurance, collaboration and enforcement. That separation prevents successful authentication from being treated as sufficient authority for every use.
### Intent
Make identity confidence and control requirements appropriate to the circumstances in which access or activity occurs.
### Boundary
Federation or Single Sign-On does not guarantee identity quality. Enforcement spans physical, procedural and digital controls; it is broader than a runtime policy engine.

## Health Information Protection
### Capability Overview
Health Information Protection governs the consequences of information exposure and the safeguards protecting information and its operating environment. Privacy safeguards, estate protection, access-practice monitoring, compartment assurance and classification assurance address different questions. Together they distinguish appropriate arrangements from evidence of how information is actually handled, including physical and printed handling.
### Intent
Protect information and the people affected by its use, with an intelligible basis for evaluating protective arrangements.
### Boundary
Compartment assurance does not decide access. Subject-specific protection assurance does not create additional macro-level Health Service Assurance trees.

## Digital Accountability & Trust
### Capability Overview
Digital Accountability & Trust makes activity attributable and evaluates whether security-relevant activity and information use were appropriate. Attribution records participation together with acting capacity and relevant permission or constraint context; accountability uses that context to consider conduct against policy and obligations. A record of who acted is therefore necessary context, rather than proof that the action was permitted.
### Intent
Support meaningful responsibility for information use and security-relevant conduct.
### Boundary
Authentication, attribution and authorisation must remain distinguishable. Expected downstream evidentiary use does not establish new BC-to-BEC relationships.

## Health Research & Innovation
### Capability Overview
This capability frames the deliberately bounded research and innovation responsibilities material to Harmonia's R1/R2 architecture. Research Information Support concerns provision of existing information for authorised research; Health Innovation concerns evaluation of proposed or emerging innovation. These establish useful enterprise context without implying a complete research organisation or innovation lifecycle.
### Intent
Support legitimate research information needs and informed consideration of changes to health-service or clinical practice.
### Boundary
Information provision does not own trials, ethics approval or specialist research analysis. Evaluating innovation does not authorise adoption or implementation.
### Harmonia Architectural Relevance
Governed provision of existing information materially enables authorised research. Innovation evaluation has no separate platform assignment; the CT1 classification must not be inherited by each descendant.

## Research Information Support
### Capability Overview
Research Information Support represents the ability to make existing health information available for a legitimate research purpose. Its single established child concentrates on governed provision, preserving the constraints under which information can be used rather than treating technical availability as research permission. The narrow decomposition reflects R1/R2 sufficiency rather than a claim to encompass every form of research support.
### Intent
Support authorised research with information supplied under the applicable ethical, privacy and governance conditions.
### Boundary
Research governance, trial oversight and specialist analysis remain unmodelled and are not absorbed into provision.

## Health Innovation
### Capability Overview
Health Innovation provides the business frame for considering changes in health-service and clinical practice. Its established evaluation child examines suitability, value and implications so that an innovation can be understood in the context in which it might be used. The model stops at that evaluative responsibility rather than presenting a complete pipeline from idea to operational adoption.
### Intent
Make consideration of innovation informed by its prospective benefits, consequences and suitability.
### Boundary
Evaluation does not establish an adoption decision, professional authority or an implementation programme.

## Health Service Direction & Stewardship
### Capability Overview
This capability considers what health services should exist, what makes them viable and how they fit within the wider service ecosystem. Modelling frames the need and required abilities; alignment addresses dependencies, resources, financial arrangements and surrounding change. The distinction keeps the rationale for a service separate from its identifiable offering and its current operational delivery state.
### Intent
Support viable, coherent service development and improvement within organisational and ecosystem constraints.
### Boundary
Identifying dependencies and resources does not acquire their operating authority. Service modelling and alignment do not establish the authoritative service catalogue.
### Harmonia Architectural Relevance
Service need, viability and alignment facts materially shape service information and derivation requirements. That relevance does not assign executive authority or establish a future BEC mapping.

## Health Service Modelling
### Capability Overview
Health Service Modelling establishes the service needed and the ordinary business abilities required to make it viable. Its children distinguish recognising unmet need, identifying required abilities, supporting establishment and evaluating an existing service for improvement. The model can therefore revisit service assumptions without treating the original definition as permanently sufficient.
### Intent
Connect a service's purpose with the abilities and preparation required for it to exist usefully.
### Boundary
Capability Identification uses ordinary business meaning; it does not allocate Harmonia capability tiers. Implementation support does not take over recruitment, facilities, procurement or ICT responsibilities.

## Health Service Alignment
### Capability Overview
Health Service Alignment positions a service among the other services, resources and arrangements on which it depends. It considers reciprocal dependencies, required resources, financial fit and the organisational changes needed for introduction or material change. Where modelling asks what service is needed, alignment asks how it can fit coherently into its environment.
### Intent
Expose the dependencies and surrounding commitments that affect service viability.
### Boundary
Alignment does not establish the service offering, own the required resources or settle the unresolved ancestry of collective operational readiness.

## Workforce & Organisational Capability
### Capability Overview
This capability governs the workforce and organisational conditions under which health services can operate. Its workforce branch addresses professional standing, participation, privilege and enterprise coverage; its structural branch addresses organisations, recognised locations and identifiable service offerings. Directories can represent these facts, but their representation is separate from business authority to establish or change them.
### Intent
Make the organisational and workforce context of care explicit enough to constrain legitimate participation and service availability.
### Boundary
Qualification, engagement, privilege, service coverage and deployment are distinct. Organisation or location association does not substitute for material Healthcare Service context.
### Harmonia Architectural Relevance
These facts materially constrain care enablement, routing and operational context. Directory support preserves the originating workforce and organisational governance rather than making it platform-owned.

## Health Workforce Management
### Capability Overview
Health Workforce Management governs practitioners' professional standing, authorised participation and the ability to cover required enterprise work. Its three children separate qualification from engagement and privilege, and both from whole-enterprise coverage. This prevents an available or rostered person from being treated as automatically qualified and privileged for a particular activity.
### Intent
Support legitimate practitioner participation and an informed understanding of service-coverage gaps and risks.
### Boundary
Coverage is broader than rostering. Applying an available resource to a care requirement and progressing a particular support work item remain separate operational responsibilities.

## Healthcare Organisation & Service Structure Management
### Capability Overview
This capability establishes the organisation, location and service-offering context through which health services are recognised. Its children distinguish the identity and structure of participants, the definition of places, and what identifiable services are offered under temporal and conditional availability constraints. The distinction allows a service to exist without assuming it is offered everywhere or currently available.
### Intent
Provide an intelligible account of who offers what service, where and under which conditions.
### Boundary
Location definition is not facilities operation. Offering availability does not establish that all resources and dependencies are collectively ready to deliver the service.

## Corporate Resources & Enterprise Services
### Capability Overview
This capability retains the financial and corporate-support context material to R1/R2. The financial branch concentrates on information about clinical activity relevant to charging or cost; corporate support covers administrative, physical, technical, people and communication conditions affecting service activity. The limited decomposition makes these dependencies visible without assigning a complete corporate enterprise architecture to Harmonia.
### Intent
Support health-service activity with the required corporate context and legitimate activity information.
### Boundary
Evidence about cost-relevant activity does not perform billing or accounting. Consuming corporate resource state does not acquire the authority to operate those resources.
### Harmonia Architectural Relevance
Clinical activity evidence provides material financial support. Corporate constraints may also affect enablement, but contextual consumption does not classify every corporate descendant as Relevant or Core.

## Financial & Commercial Services
### Capability Overview
Financial & Commercial Services provides the financial business frame needed for the bounded R1/R2 responsibility. Its established child identifies and supplies information about chargeable or cost-relevant clinical activity. The purpose is to make applicable activity evidence available to the responsible financial context without expanding the model into pricing, claims or revenue management.
### Intent
Support legitimate funding, financial and charging purposes with relevant clinical-activity information.
### Boundary
Not every exchanged request or activity is chargeable. Provision of evidence does not establish a charging decision or an enterprise financial system.

## Corporate Support Services
### Capability Overview
Corporate Support Services frames enterprise support whose resources, availability or constraints affect health-service activity. Its children retain recognisable administrative, facilities, infrastructure, communications, people and operational-oversight responsibilities. This explains why their context matters to service delivery while keeping the people who operate those concerns accountable for them.
### Intent
Make material support conditions visible to health-service activity without confusing consumption with ownership.
### Boundary
No further R1/R2 decomposition is established. Context from these abilities does not settle the collective service-readiness placement question.

## Health Service Governance
### Capability Overview
Health Service Governance establishes the normative envelope within which health-service activity operates: what obligations apply, who may decide, what constraints bind decisions and how adherence will be assessed and reported. Its framework and compliance branches distinguish the authoritative requirements from the arrangements used to judge satisfaction. Governance therefore shapes legitimate activity without becoming the manager of every governed operation.
### Intent
Make obligations, decision authority and compliance expectations explicit and traceable.
### Boundary
Operational management chooses and progresses means within the governing envelope; assurance evaluates evidence. Neither responsibility is absorbed merely because governance defines its constraints.
### Harmonia Architectural Relevance
Formal relevance remains Unresolved. The requirements plainly constrain managed activity, but the RAD has not assigned enough platform participation or enabling derivation to adjudicate its relevance classification.

## Governance Framework Management
### Capability Overview
Governance Framework Management establishes what must be satisfied and the legitimate authority and bounds for organisational decisions. Its two children separate obligations attributable to source authority from the allocation and limitation of decision authority. Together they supply the authoritative reference against which operational arrangements and compliance assessments can be considered.
### Intent
Keep required outcomes and legitimate decision parameters distinguishable and traceable to their authority.
### Boundary
Interpreting an external obligation or engaging with a regulator does not transfer that body's authority. Operational means remain with management.

## Compliance Governance
### Capability Overview
Compliance Governance defines how adherence to established obligations, authorities and constraints will be determined. Assessment Definition specifies the evidence and basis for evaluation; Reporting & Checkpoint Definition specifies where adherence is checked and what must be communicated. This makes the compliance test explicit without treating the design of a test as its successful execution.
### Intent
Provide a defensible basis for checking and reporting whether the governing envelope has been respected.
### Boundary
Defining assessment arrangements does not perform assurance or manage the governed activity. The obligations being assessed retain their authoritative source.

## Health Service Assurance
### Capability Overview
Health Service Assurance establishes justified confidence in the ability to deliver health services appropriately. Specification defines how confidence will be established; evaluation determines what the available evidence supports. Its subject is macro-level service delivery, with workforce, clinical, information, security and other concerns contributing evidence or criteria without becoming separate assurance trees.

Where independent governed assurance is required, the subject cannot control its own evaluation or conclusion. Independence concerns control of the assurance judgement rather than requiring a separate organisation or technology installation.
### Intent
Distinguish justified confidence, qualified conclusions and insufficient evidence so that assurance communicates what is actually supported.
### Boundary
Assurance manages its own evaluation and does not assign, remediate or progress the subject activity. Harmonia does not thereby assume responsibility for clinical adequacy or professional practice.
### Harmonia Architectural Relevance
Approved independent governed assurance materially supports service confidence. The three established assurance BECs provide bounded support; they are not remapped to the newer BC descendants and are not assigned implementation components.

## Health Service Assurance Specification
### Capability Overview
This capability defines the approach and evaluation criteria through which service-delivery assurance can be justified. Approach Definition addresses scope, method, responsibility, evidence and applicable independence; Criteria Governance establishes the basis for judging sufficiency. Their separation distinguishes the design of an assurance activity from the normative or evaluative material it applies.
### Intent
Make the intended basis of confidence explicit before a conclusion is treated as assurance.
### Boundary
An approach cannot waive a higher-level independence requirement. Contributing specialist concerns do not create separate Business Assurance hierarchies.

## Assurance Evaluation
### Capability Overview
Assurance Evaluation determines what confidence the evidence legitimately supports under the established approach and criteria. Evidence Assessment considers the material against that basis; Conclusion Determination establishes the resulting position, including qualifications and unresolved concerns. The distinction prevents accumulated evidence or execution completion from being mistaken for a justified assurance conclusion.
### Intent
State the supported level and limits of confidence without concealing insufficient evidence.
### Boundary
Insufficient evidence establishes neither assurance nor non-assurance. Operational response remains with the activity's responsible capability, and the existing assurance BEC ownership is unchanged.
