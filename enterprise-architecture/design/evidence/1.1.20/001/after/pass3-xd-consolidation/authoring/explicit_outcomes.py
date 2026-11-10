# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
# Explicit domain outcomes for test-oriented source measures; measures remain verbatim in evidence.
OUTCOMES={}
def add(prefix, lines):
 for line in lines.strip().splitlines():
  suffix,desc=line.split('|',1);OUTCOMES[prefix+suffix.strip()]=desc.strip()
add('E', '''
01.01.01.F01|Acknowledged information units can be retrieved intact after interruption.
01.01.02.F01|In-scope information changes receive the correct governed acceptance or rejection outcome.
01.01.02.F02|Accepted information changes remain recoverable after the specified disruption.
01.01.03.F01|Required information content is returned within the agreed availability conditions.
01.02.02.F01|Version histories retain resolvable predecessor and successor context.
01.02.03.F01|Preserved information versions match their recorded integrity evidence.
01.03.01.F01|Information integrity can be verified against valid integrity evidence.
01.03.02.F01|Governed information retains its required origin and contextual evidence.
01.03.03.F01|Detected integrity exceptions are made available within the prescribed conditions.
01.04.01.F01|Records are retained in accordance with their applicable obligations.
01.04.02.F01|Authorised archive requests return the correct information versions.
01.04.03.F01|Custody recovery restores the required information and its context under the prescribed recovery conditions.
01.04.04.F01|Information lifecycle actions respect applicable policy and holds.
01.05.01.F01|Valid information references resolve to the intended information.
01.05.02.F01|Authorised consumers can retrieve the large content identified by governed references.
01.05.03.F01|Retained references remain resolvable or receive an explicit disposition.
02.01.01.F01|Information retrieval applies the correct permission and scope conditions.
02.01.02.F01|Information changes receive the correct acceptance, rejection and durability outcome.
02.01.03.F01|Retrieved information conforms to the requested authorised scope.
02.02.01.F01|Search result version references resolve to the intended information versions.
02.02.02.F01|Searches return only permitted results within the applicable scope.
02.02.03.F01|Persisted search result collections retain their required meaning and references for reuse.
02.03.01.F01|Distributed retrieval remains responsive under the defined loads.
02.03.01.F02|Distributed changes receive the correct governed acceptance, rejection and durability outcome.
02.03.01.F03|Concurrent distributed operations satisfy the applicable consistency requirements.
02.03.02.F01|In-scope information references resolve correctly across permitted locations.
02.03.03.F01|Information reads satisfy their required currency constraints.
02.04.01.F01|Information distributions reach only their intended recipients.
02.04.02.F01|Qualifying information updates produce relevant notifications within the required conditions.
02.04.03.F01|Information distributions use resolvable, authorised shared references where applicable.
02.05.01.F01|Information access meets the applicable availability conditions through specified disruptions.
02.05.02.F01|Restored access satisfies the required scope, freshness and security conditions.
03.01.01.F01|Clinical exchanges satisfy their applicable exchange specifications.
03.01.02.F01|Valid authorised inter-organisation exchanges can be completed correctly.
03.01.03.F01|Inbound interactions receive a defined correct outcome with the required evidence.
03.02.01.F01|Exposed service interactions satisfy their exposure and access contracts.
03.02.02.F01|External service use meets the applicable identity, contract and outcome conditions.
03.02.03.F01|Published service contracts accurately describe the supported behaviour.
03.03.01.F01|Exchanged information is accepted, transformed or rejected according to its applicable format contract.
03.03.02.F01|Protocol interactions satisfy the declared boundary rules.
03.03.03.F01|Required native standards representations remain conformant and retrievable.
03.04.01.F01|Exchanges receive the acknowledgement outcome specified by the applicable exchange contract.
03.04.02.F01|Rejected exchanges provide the required actionable rejection information.
03.04.03.F01|Exchange timeout and failure conditions yield a defined outcome or reconciliation path.
03.05.01.F01|Supported exchange versions conform to the applicable contract, with conformance demonstrated by their required test suites.
03.05.02.F01|Exchange contract deviations are detected and surfaced.
03.05.03.F01|Required exchange conformance claims have reviewable supporting evidence.
04.01.01.F01|Consumers can resolve valid requests for the correct model and version definition.
04.01.02.F01|Information is bound to the correct applicable model version.
04.02.01.F01|Coded terms resolve against the required vocabulary versions.
04.02.02.F01|Released value sets are consistent with their approved versions.
04.02.03.F01|Governed terminology changes provide accessible compatibility and change details.
04.03.01.F01|Terminology mappings preserve the required meaning.
04.03.02.F01|Terminology translation qualifications are identified and conveyed.
04.04.01.F01|Model verification distinguishes conforming and nonconforming models under the applicable specifications.
04.04.02.F01|Terminology validation correctly distinguishes conforming and nonconforming codes and values.
04.04.03.F01|Known semantic incompatibilities are detected.
04.05.01.F01|Semantic releases can be reproduced from their accepted inputs.
04.05.02.F01|Relevant semantic changes have evidence-supported compatibility findings.
04.05.03.F01|Version transitions provide sufficient guidance and provenance for consumers.
05.01.01.F01|Authentication correctly establishes identity or denies the interaction under the applicable conditions.
05.01.02.F01|Machine interactions establish the required mutual identity and trust.
05.01.03.F01|Authentication outcomes retain complete required evidence for review.
05.02.01.F01|Authorisation decisions correctly apply the governing context and policy.
05.02.02.F01|Authorisation decisions correctly apply purpose and compartment conditions.
05.03.01.F01|Local boundaries enforce the required authorisation conditions.
05.03.02.F01|Rejected unauthorised operations leave governed state unchanged.
05.03.03.F01|Controlled actions retain the required decision context as reviewable, attributable evidence.
05.04.01.F01|Applicable exchanges carry complete, valid governance context.
05.04.02.F01|Exchanges preserve the applicable credential-exclusion controls.
05.05.01.F01|Information exchanges meet the required protection conditions.
05.05.02.F01|Information in custody is protected according to its classification and applicable controls.
05.05.03.F01|Information isolation policy conditions receive the correct governed handling.
05.05.04.F01|Significant security events retain their required attributes as reviewable evidence.
06.01.01.F01|Accepted actionable work retains its required task context.
06.01.02.F01|Completed work units have valid, attributable result or outcome references.
06.01.03.F01|Work units have a reconciled state that accurately represents their progression.
06.02.01.F01|Workflows progress to the correct permitted state under their conformance conditions.
06.02.02.F01|Individual tasks produce correct outcomes with independent accountability.
06.02.03.F01|Digital Twin work obeys its prescribed ordering and state conditions.
06.03.01.F01|Routed Parcels are delivered to their prescribed destinations.
06.03.02.F01|Parcels retain sufficient routing and accountability evidence.
06.04.01.F01|Accepted Parcels remain preserved through the specified failure conditions.
06.04.02.F01|Delivery failures receive the correct retry or rejection outcome.
06.04.03.F01|Each Parcel has an accurate and determinable current delivery status.
06.05.01.F01|Scheduled work is triggered within its defined acceptable conditions.
06.05.02.F01|Work deadlines and temporal constraints receive their prescribed handling.
06.05.03.F01|Governed events retain the required verifiable time provenance.
06.06.01.F01|Interrupted work units can be restored to valid states.
06.06.02.F01|Workflow interruption recovery preserves valid progression.
06.06.03.F01|Restored work contexts retain valid in-scope information references.
07.01.01.F01|Clinical presentations expose the contextual information required by the agreed clinical scope.
07.01.02.F01|Care-team views provide the required governed shared-care context.
07.01.03.F01|Support-context presentations provide the required information under its applicable restrictions.
07.01.04.F01|Operational presentations expose the required indicators and context.
07.01.05.F01|Administrative views accurately reflect the governed current state.
07.02.01.F01|User actions receive the correct governed acceptance or rejection outcome.
07.02.02.F01|Actions requiring confirmation retain valid acknowledgement evidence.
07.02.03.F01|Interactions support the defined accessibility requirements.
07.02.04.F01|Interactions expose and enforce only the actions permitted in their applicable context.
07.03.01.F01|Longitudinal presentations preserve correct chronology and provenance context.
07.03.02.F01|Cross-service information collections are presented without misattribution or silent omission.
07.03.03.F01|Event sequences display correct chronology and qualifications.
07.04.01.F01|User work recovery restores the required work context under the prescribed recovery conditions.
07.04.02.F01|Navigation transitions maintain the permitted relevant context.
07.04.03.F01|Context switches respect their required scope boundaries.
07.05.01.F01|Displayed information provides its required source attribution indicators.
07.05.02.F01|Information presentations correctly display the required freshness and status indicators.
07.05.03.F01|Known incompleteness in presented information is explicitly qualified.
07.05.04.F01|Restricted views indicate applicable scope limitations without leaking protected content.
08.01.01.F01|Service health is accurately represented for the applicable operational conditions.
08.01.02.F01|Defined performance conditions are represented by appropriate actionable measures.
08.01.03.F01|Dependency faults are surfaced at the appropriate operational scope.
08.02.01.F01|Required operational events are captured with their required context.
08.02.02.F01|Operational logging excludes prohibited content, including under adverse conditions.
08.02.03.F01|Valid operational evidence requests can be completed correctly.
08.03.01.F01|In-scope activity generates the required attributable AuditEvents.
08.03.02.F01|Action chains can be traced through the required audit evidence.
08.03.03.F01|Governed information has retrievable provenance context within the applicable scope.
08.04.01.F01|Applicable release controls have the required verified conformance evidence.
08.04.02.F01|Required assurance observations produce inspectable governed evidence.
08.04.03.F01|Insufficient assurance evidence produces a qualified finding rather than an unsupported passing result.
08.05.01.F01|Operational faults are detected within the defined observation conditions.
08.05.02.F01|Faults are assigned to their correct affected scope.
08.05.03.F01|Qualifying faults are notified to their required audiences.
08.06.01.F01|Service recovery meets its declared recovery expectations.
08.06.02.F01|Accepted work remains preserved under the prescribed failure conditions.
08.06.03.F01|Prescribed recovery capabilities retain recent valid exercise evidence.
09.01.01.F01|Result-set references resolve to the correct governed information versions.
09.01.02.F01|Persisted result sets retain the context required for reuse across resumption.
09.01.03.F01|Result sets retain the required query and formation provenance.
09.02.01.F01|Aggregates satisfy their applicable scope and source constraints.
09.02.02.F01|Analyses are repeatable within their declared tolerances.
09.03.01.F01|Operational reports accurately reflect accepted source evidence.
09.03.02.F01|Business reports can be traced to governed input versions.
09.03.03.F01|Report delivery satisfies the applicable scope, format and information rights.
09.04.01.F01|Insights have accessible supporting evidence within the applicable scope.
09.04.02.F01|Trend presentations retain the required time, scope and quality qualifications.
09.05.01.F01|Analytical access respects purpose and compartment controls.
09.05.02.F01|Analytical disclosures conform to the governing restrictions.
09.05.03.F01|Applicable automated outputs are identified and handled as non-authoritative advice.
''')
add('B','''
01.01.01.F01|People making health enquiries can identify an appropriate next action.
01.01.02.F01|Service options are relevant to the stated need and usable by the person seeking assistance.
01.01.03.F01|People can complete service navigation without avoidable access barriers.
01.02.01.F01|Valid care requests receive an attributable receipt and disposition.
01.02.02.F01|Valid access arrangements are confirmed with essential time, location and preparation details.
01.02.02.F02|People can complete and confirm arrangement changes through an appropriate access route.
01.02.03.F01|Qualified urgent requests reach the specified clinical access point within the applicable interval.
01.03.01.F01|Eligibility decisions provide intelligible reasons and the applicable criteria.
01.03.02.F01|Entitlement determinations use correct, current evidence.
01.03.03.F01|Eligibility exceptions have assigned, traceable resolutions.
01.04.01.F01|Care options receive suitable explanation and an assessment of the person's understanding.
01.04.02.F01|Applicable care decisions have a current, usable record of relevant preferences.
01.04.03.F01|Shared decisions retain evidence of participation appropriate to the context.
01.05.01.F01|Representative participation correctly applies its verified scope and participation conditions.
01.05.02.F01|Carer involvement and its applicable limits are correctly recorded.
01.05.03.F01|Interactions apply the current relevant representation arrangement.
01.06.01.F01|Agreed self-management actions are reviewed for progress.
01.06.02.F01|Health guidance is understandable to its intended audience groups.
01.06.03.F01|Changes requiring escalation can be recognised and an appropriate response identified.
02.01.01.F01|Required clinical context is available when a decision is made.
02.01.02.F01|Clinical history inconsistencies are resolved or appropriately qualified.
02.01.03.F01|Clinical decisions retain the applicable limitations on their contextual information.
02.02.01.F01|Clinical assessments cover the domains required by their clinical context.
02.02.02.F01|Applicable care decisions are informed by an appropriate functional assessment.
02.02.03.F01|Qualifying changes prompt appropriate reassessment of care needs.
02.03.01.F01|Investigation requests state an explicit, relevant clinical indication.
02.03.02.F01|Investigation requests receive an appropriate priority.
02.03.03.F01|Investigation plans meet the documented appropriateness criteria.
02.04.01.F01|Clinical finding interpretations include the required context and caveats.
02.04.02.F01|Material new findings prompt documented diagnostic reconsideration.
02.04.03.F01|Uncertain diagnostic cases retain recorded uncertainty and follow-up intent.
02.05.01.F01|Relevant risk factors are considered in applicable clinical cases.
02.05.02.F01|Clinical presentations receive an appropriate response priority.
02.05.03.F01|Clinical risk escalations are managed within the applicable clinical conditions.
02.06.01.F01|Care plans communicate their intent and agreed responsibilities.
02.06.02.F01|Care decisions retain relevant rationale and consideration of preferences.
02.06.03.F01|Qualifying changes are reflected in updated and communicated care plans.
03.01.01.F01|Prescribed interventions are delivered or appropriately deferred with a reason.
03.01.02.F01|Qualifying interventions receive the required treatment response review.
03.01.03.F01|Changed clinical conditions receive a documented treatment disposition.
03.02.01.F01|Required care activities are fulfilled or safely reassigned during coverage transitions.
03.02.02.F01|Applicable episodes have a current and understandable nursing care plan.
03.02.03.F01|Support needs are fulfilled in accordance with relevant dignity and preference requirements.
03.03.01.F01|Defined observation requirements are completed or explicitly qualified.
03.03.02.F01|Qualifying deterioration is escalated within the applicable conditions.
03.03.02.F02|Escalations are received by the appropriate clinical responder.
03.03.03.F01|Qualifying clinical responses receive documented post-action evaluation.
03.04.01.F01|Critical clinical conditions are recognised appropriately.
03.04.02.F01|Qualifying presentations receive indicated stabilisation within the defined windows.
03.04.03.F01|Emergency responses have the required participants and resources available.
03.05.01.F01|Procedures satisfy the prescribed preparation checks.
03.05.02.F01|Procedural care meets its applicable care requirements.
03.05.03.F01|Recovery episodes have a verified disposition and follow-up.
03.06.01.F01|Symptom burden and response are reassessed during supported care.
03.06.02.F01|Care decisions are consistent with documented goals and governing requirements.
03.06.03.F01|Assessed end-of-life support needs are addressed or transparently referred.
04.01.01.F01|Diagnostic requests are complete enough to direct the intended investigation.
04.01.02.F01|Scheduled investigations have their prerequisites fulfilled or exceptions resolved before commencement.
04.01.03.F01|Investigations receive a justified priority.
04.02.01.F01|Diagnostic evidence has verified linkage to the intended subject and request.
04.02.02.F01|Investigations produce evidence fit for its intended interpretation.
04.02.03.F01|Diagnostic evidence quality exceptions are recognised before unqualified reporting.
04.03.01.F01|Diagnostic reports contain the required interpretation and qualifiers.
04.03.02.F01|Reportable diagnostic results reach the intended responsible destination.
04.04.01.F01|Investigations satisfy the relevant process controls.
04.04.02.F01|Released diagnostic reports pass the required identity and quality checks.
04.04.03.F01|Diagnostic results retain the required unbroken investigation history.
05.01.01.F01|Shared care maintains a consistent documented multidisciplinary plan.
05.01.02.F01|Required care participants are engaged at the appropriate point.
05.01.03.F01|Multidisciplinary decisions have an accountable disposition.
05.02.01.F01|Accepted referrals meet the relevant destination criteria.
05.02.02.F01|Referrals have accountable and visible outcomes.
05.02.03.F01|Care transfers satisfy the applicable pre-transfer requirements.
05.03.01.F01|Care transfers have verified acceptance by a responsible receiving party.
05.03.01.F02|Accepted transfers identify consequential outstanding care obligations.
05.03.02.F01|Handovers transfer the required care context.
05.03.03.F01|Outstanding care actions have an acknowledged next responsibility.
05.04.01.F01|Longitudinal care remains free of unexplained contradictory care intent.
05.04.02.F01|Later care episodes have the required earlier care context available.
05.04.03.F01|Care continuity gaps are detected and assigned for action.
05.05.01.F01|Required follow-ups have a confirmed provider, timing or accountable arrangement.
05.05.02.F01|Tracked follow-up obligations have a known completion or exception disposition.
05.05.03.F01|Qualifying overdue follow-ups are acted on within the relevant conditions.
05.06.01.F01|Receiving care providers obtain the relevant care intent during transitions.
05.06.02.F01|Qualifying cases have established community support arrangements.
05.06.03.F01|Location changes preserve required care access and responsibilities.
06.01.01.F01|Medication regimens state clear dose, purpose, route and review intent.
06.01.02.F01|Medication information is clinically reconciled during relevant transitions.
06.01.03.F01|Qualifying medicine therapy reviews have a documented disposition.
06.02.01.F01|Medicine supplies meet prescribed verification and labelling requirements.
06.02.02.F01|Required medicine courses remain available without avoidable supply interruption.
06.02.03.F01|Medicine recipients receive suitable information and understanding checks.
06.03.01.F01|Functional goals have recorded progress and an agreed reassessment.
06.03.02.F01|Planned rehabilitation interventions are fulfilled or appropriately adjusted.
06.03.03.F01|Completed rehabilitation plans have an attributable functional outcome assessment.
06.04.01.F01|Assistive supports match the identified functional requirements.
06.04.02.F01|Agreed assistive provisions are available within the relevant timeframe.
06.04.03.F01|Qualifying changes prompt appropriate reassessment of assistive support.
06.05.01.F01|Psychosocial assessments address the agreed applicable domains.
06.05.02.F01|Psychological and behavioural intervention plans receive outcome and experience review.
06.05.03.F01|Agreed support referrals have a confirmed connection or a reasoned exception.
07.01.01.F01|Service offerings have defined scope, eligibility and access conditions.
07.01.02.F01|Designated service offerings are reviewed against current need and demand.
07.01.03.F01|Changed or retired offerings have an evidenced transition disposition.
07.02.01.F01|Participating providers meet their required participation conditions.
07.02.02.F01|Active provider arrangements have current, accountable terms.
07.02.03.F01|Defined network coverage areas have a current capability and gap assessment.
07.03.01.F01|Expected service demand conditions are correctly identified in assessments.
07.03.02.F01|Accepted service needs are allocated within applicable priority and capacity conditions.
07.03.02.F02|Unsatisfied qualifying service needs are explicitly identified for disposition.
07.03.03.F01|Scheduled service commitments are feasible for their required resources and participants.
07.04.01.F01|Service deliveries meet the applicable readiness conditions.
07.04.02.F01|Shared service deliveries avoid preventable coordination gaps.
07.04.03.F01|Service disruptions receive a timely disposition and affected-party communication.
07.05.01.F01|Service registrations retain the required validated details.
07.05.02.F01|Appointments have an accurate and timely attendance disposition.
07.05.03.F01|Access administration exceptions receive a documented resolution or responsible referral.
07.06.01.F01|Defined reporting periods have complete, timely, evidence-backed service performance reports.
07.06.02.F01|Service reports state correct scope, denominators and outcome qualifiers.
07.06.03.F01|Qualifying service performance deviations are reviewed and dispositioned.
08.01.01.F01|Designated service plans include an explicit workforce-demand assessment.
08.01.02.F01|Required shifts meet the defined coverage and qualification criteria.
08.01.02.F02|Shift assignments meet the relevant skill requirements.
08.01.03.F01|Qualifying workforce coverage gaps are addressed within relevant conditions.
08.02.01.F01|Practitioners have verified applicable current professional eligibility.
08.02.02.F01|Active professional arrangements have current supporting credential evidence.
08.02.03.F01|Material credential exceptions are actioned before incompatible practice.
08.03.01.F01|Relevant practice activities remain within current authorised clinical privileges.
08.03.02.F01|Practitioners can access their applicable practice scope limits.
08.03.03.F01|Privilege changes are reflected before affected practice takes place.
08.04.01.F01|Designated practitioners have current development needs and plans.
08.04.02.F01|Applicable competence requirements have current supporting evidence.
08.04.03.F01|Learning records meet the accepted evidence requirements for recognition.
08.05.01.F01|Qualifying practice has appropriate supervision arrangements.
08.05.02.F01|Valid consultation needs receive an accountable professional response within relevant conditions.
08.05.03.F01|Qualifying practice concerns receive documented attention and disposition.
08.06.01.F01|Workforce support requests receive an appropriate response.
08.06.02.F01|Designated assignments have a valid workforce readiness status.
08.06.03.F01|Relevant returns to work have agreed requirements and progress review.
09.01.01.F01|Planned service events have an appropriately equipped and accessible care space.
09.01.02.F01|Care spaces meet their declared use requirements.
09.01.03.F01|Care-space disruptions receive a safe disposition.
09.02.01.F01|Service events have their required ready equipment available when needed.
09.02.02.F01|Equipment use has current required fitness checks.
09.02.03.F01|Qualifying equipment failures receive an acceptable alternate arrangement.
09.03.01.F01|Legitimate therapeutic needs are met without avoidable shortage.
09.03.02.F01|Therapeutic goods retain sufficient product, lot and destination lineage.
09.03.03.F01|Recalled items receive timely, attributable action.
09.04.01.F01|Defined essential clinical stock is available at the required care points.
09.04.02.F01|Material deliveries reach the intended destination intact and on time.
09.04.03.F01|Expired and waste stock is handled under the applicable requirements.
09.05.01.F01|Clinical transport meets the required clinical and accessibility conditions.
09.05.02.F01|Transfers have verified transport and receiving-readiness coordination.
09.05.03.F01|Clinical resource consignments meet their time and condition requirements.
09.06.01.F01|Designated services have a current resource completeness assessment.
09.06.02.F01|Material resource hazards receive accountable action.
09.06.03.F01|Critical resource shortages have a viable contingency disposition.
10.01.01.F01|Care records have their applicable content completed or qualified.
10.01.02.F01|Clinical records are associated with the intended subject and context.
10.01.03.F01|Material contributions to longitudinal records are correctly contextualised.
10.02.01.F01|Known information incompleteness is explicit at the point of decision.
10.02.02.F01|Business information retains retrievable required origin context.
10.02.03.F01|Changed records retain sufficient traceable information history.
10.03.01.F01|Authorised necessary information is available at required care transitions.
10.03.01.F02|Care information exchanges include necessary information without unnecessary disclosure.
10.03.02.F01|Business disclosures match the applicable purpose and scope.
10.03.03.F01|Consequential blocked sharing needs have a documented disposition.
10.04.01.F01|Qualifying care decisions apply the relevant recorded consent context.
10.04.02.F01|Applicable care interactions conform to current participation preferences.
10.04.03.F01|Care decisions use a valid applicable decision authority basis.
10.05.01.F01|Clinical guidance is current and applicable to its intended scope.
10.05.02.F01|Material clinical knowledge changes receive an accountable business review.
10.05.03.F01|Clinical decisions have appropriate guidance and its qualifications available.
10.06.01.F01|Records are managed under the relevant retention obligations.
10.06.02.F01|Valid record disclosure requests receive the correct outcome and a traceable decision.
10.06.03.F01|Disclosable record activities retain the required evidence.
10.07.01.F01|Record corrections have appropriate authorisation and traceability.
10.07.02.F01|Information disputes have a recorded outcome or qualification.
10.07.03.F01|Cross-source discrepancies are resolved or visibly qualified.
11.01.01.F01|Clinical hazards are identified by the relevant controls.
11.01.02.F01|Material clinical risks have active, reviewed mitigation.
11.01.03.F01|Qualifying high-consequence concerns are escalated within the applicable interval.
11.02.01.F01|Qualifying reported incidents retain their required core information.
11.02.02.F01|Qualifying incidents have evidenced follow-up and accountable closure.
11.02.03.F01|Significant incident findings prompt evaluated improvement action.
11.03.01.F01|Practice areas have current, applicable guidance and accountability.
11.03.02.F01|Designated practice reviews are completed with adequate supporting evidence.
11.03.03.F01|Material practice exceptions receive recorded, responsible review.
11.04.01.F01|Services have defensible outcome measures and interpretation.
11.04.02.F01|Designated services have appropriate service experience evidence and review.
11.04.03.F01|Material quality improvement commitments receive effectiveness review.
11.05.01.F01|Service areas have correctly mapped relevant regulatory requirements.
11.05.02.F01|Applicable accreditation criteria have current, attributable evidence.
11.05.03.F01|Consequential conformance findings have an approved action or qualified exception.
11.06.01.F01|Complaints can be successfully submitted and acknowledged through accessible pathways.
11.06.02.F01|Complaints have reasoned and appropriately communicated outcomes.
11.06.03.F01|Agreed remedies are completed or transparently renegotiated.
12.01.01.F01|Designated strategic outcomes have accountable definitions and measures.
12.01.02.F01|Investment decisions have consistent supporting option assessments.
12.01.03.F01|Investment decisions retain documented strategic alignment and rationale.
12.02.01.F01|Designated service commitments have a current financial basis.
12.02.02.F01|Active commercial arrangements have defined obligations and accountable oversight.
12.02.03.F01|Financial reporting periods have complete, reconcilable financial evidence.
12.03.01.F01|Enterprise commitments have current ownership and a recorded outcome or exception.
12.03.02.F01|Designated enterprise outcomes have current evidence and relevant qualifications.
12.03.03.F01|Management decisions retain adequate rationale, approval and action context.
12.04.01.F01|Designated enterprise risk areas receive current risk review.
12.04.02.F01|Business continuity arrangements meet the agreed business requirements under prescribed continuity scenarios.
12.04.03.F01|Enterprise recovery has accountable prioritisation and follow-through.
13.01.01.F01|Designated surveillance periods have complete, interpretable population evidence.
13.01.02.F01|Population threats are detected and escalated under the defined conditions.
13.01.03.F01|Priority population groups have appropriately evidenced current needs assessments.
13.02.01.F01|Prevention programmes state their population need and evaluable intended outcome.
13.02.02.F01|Intended population groups are reached through suitable prevention channels.
13.02.03.F01|Completed prevention programmes have defensible outcome assessments.
13.03.01.F01|Qualifying public health threats receive timely, documented multi-party responses.
13.03.02.F01|Active public health measures receive relevant effect and proportionality assessments.
13.03.03.F01|Public risk communications meet their comprehension and timeliness criteria.
13.04.01.F01|Studies have valid approvals and applicable conditions established before commencement.
13.04.02.F01|Research participant interactions meet the relevant consent and safeguarding conditions.
13.04.03.F01|Material research changes receive the required disposition before affected activity.
13.05.01.F01|Study designs have sufficient question, method and outcome definition.
13.05.02.F01|Research evidence is collected within documented protocol and permission boundaries.
13.05.03.F01|Research findings retain traceable methods, inputs and uncertainty.
13.06.01.F01|Research findings receive documented relevance and confidence assessments.
13.06.02.F01|Authorised practice changes retain evidence of uptake and evaluated effect.
13.06.03.F01|Designated practice improvements receive follow-up review and communicated lessons.
14.01.01.F01|Participant priorities are accessible and self-expressed or appropriately supported.
14.01.02.F01|Agreed goal reviews involve meaningful participant participation.
14.01.03.F01|Support plans align with relevant current participant preferences.
14.02.01.F01|Support needs assessments address the agreed functioning and participation areas.
14.02.02.F01|Participation context assessments identify relevant barriers and accommodations.
14.02.03.F01|Qualifying changes prompt timely support review.
14.03.01.F01|Support choices have accessible option information and recorded preferences.
14.03.02.F01|Support arrangements are consistent with the participant's current valid choice.
14.03.02.F02|Requested support changes are dispositioned consistently with preferences and applicable authority.
14.03.03.F01|Support changes have clear participation, responsibility and transition arrangements.
14.04.01.F01|Shared support maintains compatible goals and responsibilities.
14.04.02.F01|Material support commitments have a current accountable status.
14.04.03.F01|Life transitions have confirmed continuation or adjusted support arrangements.
14.05.01.F01|Agreed independent-living outcomes receive achievement review.
14.05.02.F01|Supported participation goals have an assessed outcome.
14.05.03.F01|Identified participation barriers are addressed or transparently dispositioned.
14.06.01.F01|Qualifying safeguarding concerns receive appropriate review and protective action.
14.06.02.F01|Supported decisions retain evidence of participation and authority boundaries.
14.06.03.F01|Valid advocacy requests receive an appropriate connection or reasoned alternative.
15.01.01.F01|Commissioning decisions link to a documented service need.
15.01.02.F01|Commissioned arrangements have defined assessable outcomes.
15.01.03.F01|Commissioned service types have current provider capability and capacity evidence.
15.02.01.F01|Coverage determinations have the correct policy basis and intelligible notice.
15.02.02.F01|Coverage cases are assessed against the correct current terms.
15.02.03.F01|Coverage appeals retain a documented basis, action and notification.
15.03.01.F01|Service purchases have defined service and quality obligations.
15.03.02.F01|Selected provider contracts receive current, evidence-backed performance evaluation.
15.03.03.F01|Contract transitions receive impact review and a service-continuity disposition.
15.04.01.F01|Claims are correctly paid, rejected or referred for review.
15.04.01.F02|Claim decisions provide an intelligible explanation.
15.04.02.F01|Funding transactions retain an explainable underlying service basis.
15.04.03.F01|Qualifying funding exceptions receive documented review and appropriate action.
15.05.01.F01|Designated funding programmes have current outcome evidence.
15.05.02.F01|Designated population groups have interpretable access and funding equity analysis.
15.05.03.F01|Funding changes retain documented rationale and outcome follow-up.
''')
add('B','''
12.05.01.F01|Designated services meet their defined readiness evidence requirements.
12.05.02.F01|Surge response meets the specified availability conditions.
12.05.03.F01|Remote and deployed services have the required staff, supply and support readiness.
''')
add('E','''
01.01.01.F02|Durable persistence is evidenced before a durable-acceptance acknowledgement is issued.
01.01.04.F03|Interrupted commits produce an indeterminate outcome where success is not established.
01.03.04.F03|Container, content and Information Unit metadata remain distinguishable.
01.04.05.F02|Protected or held information versions remain available under the applicable retention conditions.
01.05.04.F01|Large payloads use authorised media custody rather than generic relational Information Unit storage.
02.02.07.F05|Clinically distinct information is not silently fused, including under adverse conditions.
02.06.01.F02|Each directory search discloses only information permitted for the actual purpose.
03.07.02.F05|Unrelated or unauthorised domain records cannot enter an exchange merely because they are technically discoverable.
03.07.08.F05|The interconnect pattern supports at least two independently governed record domains without duplicating the transport framework.
04.03.03.F03|Semantic equivalence claims conform to their reference examples and adverse conditions.
05.04.03.F02|Revocation reaches subscribed enforcement points under the defined recovery conditions.
05.06.03.F01|Authorisation change impact identifies all affected effective authorisations within the applicable scope.
05.07.01.F01|Identity handling distinguishes a represented person from the Information Unit carrying an assertion about that person.
07.01.06.F05|Users can identify the correct intended action in the applicable domain context.
08.07.02.F05|Immutable evidence records survive the required recovery and retention conditions.
08.07.03.F01|Evidence list ordering matches the chronology of the represented activities.
09.04.03.F05|Plausible but unsupported recommendations receive an appropriate qualification.
''')
# Context-specific corrections after full-result language screening.
add('B','''
01.02.03.F04|Access delays are escalated within the applicable context-defined interval.
01.03.03.F04|Eligibility exceptions receive an explicit disposition within the applicable context-defined interval.
01.05.03.F02|Representation arrangements are updated within the applicable context-defined interval.
02.01.01.F04|Clinical context retains the required currency through relevant changes or disruptions, with recovery where required.
02.01.03.F03|Clinical context remains relevant to the current decision through relevant changes or disruptions, with recovery where required.
02.02.01.F03|Clinical assessments include observations appropriate to the clinical context.
02.02.03.F05|Clinical needs are reassessed within the applicable context-defined interval.
02.05.03.F03|Clinical risk is escalated within the applicable context-defined interval.
03.03.01.F04|Clinical observations use suitable intervals and meet the applicable context-defined timing conditions.
03.04.03.F04|Emergency response information is shared within the applicable context-defined interval.
04.01.01.F03|Diagnostic requests retain the clinical context relevant to the intended investigation.
04.01.03.F02|Investigation priorities are appropriate to the clinical context.
04.01.03.F05|Investigation delay risks are escalated within the applicable context-defined interval.
05.01.02.F03|Care contribution requests reach participants within the applicable context-defined interval.
05.02.03.F05|Care transfer delays receive a contingency arrangement within the applicable context-defined interval.
05.03.01.F05|Care handovers are confirmed within the applicable context-defined interval.
06.04.02.F03|Required assistive equipment is available within the applicable context-defined interval.
07.06.02.F05|Service demand and outcome analysis informs the relevant service decisions.
08.01.03.F03|Workforce assignments are communicated within the applicable context-defined interval.
08.02.01.F04|Professional eligibility is appropriate to the intended role, distinguishing eligible and ineligible practice.
08.05.03.F03|Practice concern reviews are assigned within the applicable context-defined interval.
08.06.01.F03|Workforce wellbeing needs are recognised within the applicable context-defined interval.
09.04.02.F03|Time-critical clinical materials arrive within the applicable context-defined interval.
10.01.01.F03|Clinical records are finalised within the applicable context-defined interval.
10.06.01.F04|Scheduled record dispositions occur within the applicable context-defined interval under the relevant retention obligations.
10.06.02.F04|Record disclosure requests receive a disposition within the applicable context-defined interval.
11.02.01.F04|Qualifying incidents are notified within the applicable context-defined interval.
11.02.02.F04|Incident follow-up actions occur within the applicable context-defined interval.
11.03.03.F02|Clinical accountability is explicit for the applicable practice decisions and exceptions.
12.02.03.F04|Financial reports are available within the applicable context-defined interval.
12.05.02.F03|Additional surge capacity is available within the applicable context-defined interval.
13.01.01.F05|Population surveillance summaries are available within the applicable context-defined interval.
13.01.02.F02|Population surveillance recognises early threat signals within the applicable context-defined interval.
14.02.03.F03|Support needs are reassessed within the applicable context-defined interval.
15.04.02.F04|Service funding information can be corrected within the applicable context-defined interval.
''')
add('E','''
01.04.02.F05|Authorised archive retrieval completes within the applicable context-defined interval.
02.05.02.F02|Authorised information access can be recovered, distinguishing permitted restoration from nonconforming access.
03.04.01.F02|Transport acknowledgements are issued within the applicable context-defined interval.
06.05.01.F02|Work schedules correctly apply the relevant calendar conditions.
06.05.01.F05|Scheduled execution evidence is available within the applicable context-defined interval.
06.05.02.F02|Deadline breaches are recognised within the applicable context-defined interval.
07.03.03.F04|Event intervals can be recognised within the applicable context-defined interval.
08.01.02.F03|Service latency distributions are visible within the applicable context-defined interval.
08.02.03.F03|Requested operational evidence can be selected within the applicable context-defined interval.
08.05.01.F02|Operational fault signals are available within the applicable context-defined interval.
09.03.01.F05|Scheduled operational reports are available within the applicable context-defined interval.
''')
