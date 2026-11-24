# Harmonia 1.1.20 — Consolidated Business and Enterprise Capability Catalogue

**Offline working candidate — Pass 3/012.** Prepared under accepted XD-001–XD-028 Revision 2 and AR-001/AR-002. Not incorporated into the Architecture Continuum; not implementation verification or release acceptance. The combined Business/Business Enabling hierarchy remains separate from Enterprise; CT1 → CT2 → CT3 → Feature identities and containment are preserved.

Definitions below express required architectural capabilities and outcomes, not claims that they are implemented. Original wording, proposal/verification standing, measurements and source traceability are retained by identifier in [the complete reconciliation register](feature-reconciliation.json) and [the measurement register](measurement-criteria.csv), and in the companion adjudication workbook. Original source criteria are historical evidence where Revision 2 supersedes their meaning; they must not override accepted revised definitions. No numerical target or new measurement programme is introduced.

**XD-027 placement:** The Solution Architect authorised refining existing `E06.02.03.F03` to retain Twin event context and add selective Stimulus information contextualisation. Its original criterion is preserved; no Feature is added or repurposed from recovery, security or work allocation. See [the reconciliation report](reconciliation-report.md). Pre-existing accepted-SR correspondence discrepancies are preserved in [the qualification register](sr-correspondence-qualifications.json); they are not represented as newly incorporated. FR-001–FR-009, E07.03.05's separate semantic review and FC-042/059/101 retain their existing unresolved/excluded standing. No completion of those workstreams is implied.

# Business Capabilities
## B01 — Care Participation & Access
People can understand their health and care options, obtain access, and participate on appropriate terms.

### B01.01 — Health Needs & Service Navigation
Help people recognise needs and find suitable sources of assistance.

#### B01.01.01 — Health Need Recognition Support
People can describe a concern and understand whether they need clinical care, wellbeing support or other assistance.

- **`B01.01.01.F01` — UnderstoodNextStep:** People making health enquiries can identify an appropriate next action.
- **`B01.01.01.F02` — Clear Concern Expression:** People can clearly express their health concern.
- **`B01.01.01.F03` — High-Risk Need Recognition:** High-risk need is recognised, including in adverse conditions.
- **`B01.01.01.F04` — Appropriate Self-Help Guidance:** People can obtain appropriate self-help guidance.
- **`B01.01.01.F05` — Supported Escalation Choice:** Supported escalation choice is supported through available and usable support.

#### B01.01.02 — Suitable Service Identification
People can discover services appropriate to their stated need and circumstances.

- **`B01.01.02.F01` — RelevantServiceOptions:** Service options are relevant to the stated need and usable by the person seeking assistance.
- **`B01.01.02.F02` — Needs-Matched Service Options:** Needs-matched service options are available.
- **`B01.01.02.F03` — Service Suitability Explanation:** Service suitability is explained.
- **`B01.01.02.F04` — Available Alternative Services:** Alternative services remain available through relevant changes or disruptions, with recovery where required.
- **`B01.01.02.F05` — Service Contact Usability:** Service contact usability reaches and can be understood by the intended recipient.

#### B01.01.03 — Accessible Service Navigation
People with differing access or communication needs can obtain practical navigation assistance.

- **`B01.01.03.F01` — AccessibleNavigationCompletion:** People can complete service navigation without avoidable access barriers.
- **`B01.01.03.F02` — Accessible Language Pathway:** Accessible language pathway is supported through available and usable support.
- **`B01.01.03.F03` — Assisted Navigation Option:** Assisted navigation option is supported through available and usable support.
- **`B01.01.03.F04` — Navigation Barrier Escalation:** Navigation barrier is escalated through available and usable support.
- **`B01.01.03.F05` — Successful Service Connection:** Successful service can be connected.

### B01.02 — Care Entry & Access Arrangement
Make it possible to request, arrange and enter appropriate care.

#### B01.02.01 — Care Request Reception
A person or referring party can initiate a request for care and receive a clear disposition.

- **`B01.02.01.F01` — AcknowledgedCareRequest:** Valid care requests receive an attributable receipt and disposition.
- **`B01.02.01.F02` — Complete Request Capture:** Complete request is captured.
- **`B01.02.01.F03` — Appropriate Request Classification:** Appropriate request is classified.
- **`B01.02.01.F04` — Request Status Visibility:** Request status is visible, with accessible and accurate supporting evidence.
- **`B01.02.01.F05` — Redirected Request Accountability:** Redirected request has an accountable party, with accessible and accurate supporting evidence.

#### B01.02.02 — Care Appointment Arrangement
Appropriate attendance or contact can be scheduled and confirmed.

- **`B01.02.02.F01` — Confirmed Access Arrangement:** Valid access arrangements are confirmed with essential time, location and preparation details.
- **`B01.02.02.F02` — AccessibleArrangementChange:** People can complete and confirm arrangement changes through an appropriate access route.
- **`B01.02.02.F03` — Suitable Appointment Choice:** Suitable appointment choice is supported through available and usable support.
- **`B01.02.02.F04` — Accessible Booking Confirmation:** Accessible booking is confirmed through available and usable support.
- **`B01.02.02.F05` — Preparation Requirements Visibility:** Preparation requirements are visible, with accessible and accurate supporting evidence.
- **`B01.02.02.F06` — Appointment Change Continuity:** Appointment change is maintained through relevant changes or disruptions, with recovery where required.

#### B01.02.03 — Urgent Care Entry
A person needing urgent assessment can enter an appropriate care pathway without inappropriate administrative delay.

- **`B01.02.03.F01` — TimelyUrgentAccess:** Qualified urgent requests reach the specified clinical access point within the applicable interval.
- **`B01.02.03.F02` — Correct Urgency Recognition:** Correct urgency is recognised.
- **`B01.02.03.F03` — Immediate Access Direction:** Immediate access direction is available.
- **`B01.02.03.F04` — Escalation of Access Delays:** Access delays are escalated within the applicable context-defined interval.
- **`B01.02.03.F05` — Urgent Entry Handover:** Urgent entry can be handed over through relevant changes or disruptions, with recovery where required.

### B01.03 — Eligibility & Entitlement Determination
Establish a person's basis and conditions for receiving services.

#### B01.03.01 — Service Eligibility Assessment
A person can have eligibility for a particular service assessed against applicable criteria.

- **`B01.03.01.F01` — ExplainedEligibilityDecision:** Eligibility decisions provide intelligible reasons and the applicable criteria.
- **`B01.03.01.F02` — Criteria-Based Assessment:** Criteria-based is assessed.
- **`B01.03.01.F03` — Eligibility Evidence Sufficiency:** Eligibility evidence is sufficient, distinguishing permitted and nonconforming conditions.
- **`B01.03.01.F04` — Decision Reason Visibility:** Decision reason is visible, with accessible and accurate supporting evidence.
- **`B01.03.01.F05` — Review Route Availability:** Review route is available, with accessible and accurate supporting evidence.

#### B01.03.02 — Entitlement Verification
A valid entitlement can be established or questioned before a funding or access decision.

- **`B01.03.02.F01` — CurrentEntitlementConfirmation:** Entitlement determinations use correct, current evidence.
- **`B01.03.02.F02` — Authoritative Entitlement Source:** Authoritative entitlement has an identifiable source, distinguishing permitted and nonconforming conditions.
- **`B01.03.02.F03` — Entitlement Currency Verification:** Entitlement currency is verified, distinguishing permitted and nonconforming conditions.
- **`B01.03.02.F04` — Scope of Entitlement Clarity:** Scope of entitlement is clear, distinguishing permitted and nonconforming conditions.
- **`B01.03.02.F05` — Contested Entitlement Referral:** Contested entitlement can be referred, distinguishing permitted and nonconforming conditions.

#### B01.03.03 — Eligibility Exception Resolution
Ambiguous or contested eligibility can be reviewed without silently denying access.

- **`B01.03.03.F01` — ReviewableEligibilityException:** Eligibility exceptions have assigned, traceable resolutions.
- **`B01.03.03.F02` — Exception Ownership:** Exception has an identifiable responsible owner.
- **`B01.03.03.F03` — Evidence-Grounded Review:** Evidence-grounded is reviewed, with accessible and accurate supporting evidence.
- **`B01.03.03.F04` — Timely Exception Disposition:** Eligibility exceptions receive an explicit disposition within the applicable context-defined interval.
- **`B01.03.03.F05` — Explained Resolution Outcome:** Explained resolution has an explicit outcome, with accessible and accurate supporting evidence.

### B01.04 — Informed Participation & Choice
Support people in understanding, expressing preferences and participating in decisions.

#### B01.04.01 — Care Option Explanation
People can understand materially different care options and relevant consequences.

- **`B01.04.01.F01` — DemonstratedOptionUnderstanding:** Care options receive suitable explanation and an assessment of the person's understanding.
- **`B01.04.01.F02` — Material Alternative Visibility:** Material alternative is visible, with accessible and accurate supporting evidence.
- **`B01.04.01.F03` — Benefit and Risk Clarity:** Benefit and risk is clear, including in adverse conditions.
- **`B01.04.01.F04` — Accessible Option Communication:** Accessible option is communicated through available and usable support.
- **`B01.04.01.F05` — Opportunity to Seek Clarification:** Opportunity to seek can be clarified through available and usable support.

#### B01.04.02 — Preference Expression Support
People can express priorities and preferences for their care in an accessible manner.

- **`B01.04.02.F01` — RecordedRelevantPreferences:** Applicable care decisions have a current, usable record of relevant preferences.
- **`B01.04.02.F02` — Accessible Preference Capture:** Accessible preference is captured through available and usable support.
- **`B01.04.02.F03` — Preference Currency:** Preference is current through available and usable support.
- **`B01.04.02.F04` — Context-Appropriate Preference Use:** Context-appropriate preference can be used through available and usable support.
- **`B01.04.02.F05` — Changed Preference Recognition:** Changed preference is recognised through available and usable support.

#### B01.04.03 — Shared Care Decision Participation
Care decisions can reflect informed participation and applicable clinical responsibilities.

- **`B01.04.03.F01` — DocumentedSharedDecision:** Shared decisions retain evidence of participation appropriate to the context.
- **`B01.04.03.F02` — Participation Opportunity:** Participation opportunity is supported through available and usable support.
- **`B01.04.03.F03` — Relevant Decision Alternatives:** Relevant decision alternatives are available through relevant changes or disruptions, with recovery where required.
- **`B01.04.03.F04` — Decision Responsibility Clarity:** Decision responsibility is clear.
- **`B01.04.03.F05` — Respect for Expressed Preference:** Respect for expressed preference is supported through available and usable support.

### B01.05 — Representation & Carer Participation
Enable duly authorised advocates, carers and related people to assist care.

#### B01.05.01 — Authorised Care Representation
Representatives can participate in decisions or arrangements within their established authority.

- **`B01.05.01.F01` — Recognised Representative Participation:** Representative participation correctly applies its verified scope and participation conditions.
- **`B01.05.01.F02` — Representation Scope Verification:** Representation scope is verified, distinguishing permitted and nonconforming conditions.
- **`B01.05.01.F03` — Representation Limit Visibility:** Representation limit is visible, with accessible and accurate supporting evidence.
- **`B01.05.01.F04` — Valid Representation Currency:** Valid representation is current.
- **`B01.05.01.F05` — Representation Decision Record:** Representation decision is recorded, with accessible and accurate supporting evidence.

#### B01.05.02 — Family and Carer Involvement
Appropriate family or carers can contribute information and support without being assumed to hold decision authority.

- **`B01.05.02.F01` — AppropriateCarerInvolvement:** Carer involvement and its applicable limits are correctly recorded.
- **`B01.05.02.F02` — Consent-Appropriate Carer Input:** Carer input respects the applicable consent conditions, distinguishing permitted and nonconforming conditions.
- **`B01.05.02.F03` — Carer Contribution Recognition:** Carer contribution is recognised, with accessible and accurate supporting evidence.
- **`B01.05.02.F04` — Participation Boundary Clarity:** Participation boundary is clear through available and usable support.
- **`B01.05.02.F05` — Carer Communication Continuity:** Carer communication is maintained through relevant changes or disruptions, with recovery where required.

#### B01.05.03 — Representation Change Management
Changes to representation or participation arrangements can be recognised promptly.

- **`B01.05.03.F01` — CurrentRepresentativeStatus:** Interactions apply the current relevant representation arrangement.
- **`B01.05.03.F02` — Timely Representation Update:** Representation arrangements are updated within the applicable context-defined interval.
- **`B01.05.03.F03` — Withdrawn Authority Recognition:** Withdrawn authority is recognised, distinguishing permitted and nonconforming conditions.
- **`B01.05.03.F04` — Affected Parties Notification:** Affected parties notification reaches and can be understood by the intended recipient.
- **`B01.05.03.F05` — Historical Authority Trace:** Historical authority is traceable, distinguishing permitted and nonconforming conditions.

#### B01.05.04 — Representation Authority Accountability
Establish and review what a carer, advocate or responsible person can legitimately decide or access, without replacing the person’s own voice.

*Enterprise contributions: E05.06, E05.02, E07.02; findings: R01, R11.*

- **`B01.05.04.F01` — RepresentationBasisKnown:** Authority basis is documented and current.
- **`B01.05.04.F02` — ScopeOfAssistanceClear:** Permitted support differs explicitly from permitted decision-making.
- **`B01.05.04.F03` — RevokedAuthorityHonoured:** Expired authority no longer permits representative action.
- **`B01.05.04.F04` — PersonPreferenceRecorded:** Represented person’s wishes remain separately visible.
- **`B01.05.04.F05` — DisputedAuthorityEscalated:** Disputed representation triggers a documented review path.

### B01.06 — Self-Management & Wellbeing Support
Enable ongoing health actions and participation outside formal encounters.

#### B01.06.01 — Individual Health Action Support
People can undertake agreed actions that help them manage ongoing health or wellbeing.

- **`B01.06.01.F01` — SupportedSelfManagementProgress:** Agreed self-management actions are reviewed for progress.
- **`B01.06.01.F02` — Agreed Action Clarity:** Agreed action is clear.
- **`B01.06.01.F03` — Practical Action Accessibility:** Practical action is accessible and usable.
- **`B01.06.01.F04` — Action Support Follow-Up:** Action support receives follow-up.
- **`B01.06.01.F05` — Changed Need Adjustment:** Changed need can be adjusted through relevant changes or disruptions, with recovery where required.

#### B01.06.02 — Health Information Understanding
People can obtain and understand information appropriate to managing their own health.

- **`B01.06.02.F01` — UsableHealthGuidance:** Health guidance is understandable to its intended audience groups.
- **`B01.06.02.F02` — Appropriate Health Literacy:** Health literacy is appropriate through available and usable support.
- **`B01.06.02.F03` — Multilingual Information Availability:** Multilingual information is available.
- **`B01.06.02.F04` — Misunderstanding Identification:** Misunderstanding is identified through available and usable support.
- **`B01.06.02.F05` — Information Source Clarity:** Information source is clear, with accessible and accurate supporting evidence.

#### B01.06.03 — Change and Escalation Guidance
People can recognise when their circumstances warrant additional advice or care.

- **`B01.06.03.F01` — RecognisedEscalationNeed:** Changes requiring escalation can be recognised and an appropriate response identified.
- **`B01.06.03.F02` — Observable Warning Sign Guidance:** Observable warning sign guidance reaches and can be understood by the intended recipient.
- **`B01.06.03.F03` — Escalation Contact Availability:** Escalation contact availability reaches and can be understood by the intended recipient.
- **`B01.06.03.F04` — Clear Urgency Differentiation:** Clear urgency can be differentiated.
- **`B01.06.03.F05` — Follow-Up after Escalation:** Follow-up after escalation reaches and can be understood by the intended recipient.

## B02 — Clinical Assessment & Determination
Clinicians and people can establish, assess and revise clinical understanding and appropriate care intent.

### B02.01 — Clinical Context Establishment
Obtain and interpret what is known about the person's relevant circumstances and history.

#### B02.01.01 — Relevant Clinical Context Assembly
Clinicians can establish the relevant history, current circumstances and care context for a decision.

- **`B02.01.01.F01` — Context Available at Decision:** Required clinical context is available when a decision is made.
- **`B02.01.01.F02` — Relevant History Coverage:** Relevant history is covered, with accessible and accurate supporting evidence.
- **`B02.01.01.F03` — Context Source Traceability:** Context source is traceable, with accessible and accurate supporting evidence.
- **`B02.01.01.F04` — Currency of Clinical Context:** Clinical context retains the required currency through relevant changes or disruptions, with recovery where required.
- **`B02.01.01.F05` — Important Missing Context Disclosure:** Important missing context is disclosed, with accessible and accurate supporting evidence.

#### B02.01.02 — Clinical History Reconciliation
Material inconsistencies or omissions in a clinical history can be recognised and addressed.

- **`B02.01.02.F01` — ReconciledClinicalHistory:** Clinical history inconsistencies are resolved or appropriately qualified.
- **`B02.01.02.F02` — Conflicting History Recognition:** Conflicting history is recognised, with accessible and accurate supporting evidence.
- **`B02.01.02.F03` — Medication and Allergy Reconciliation:** Medication and allergy is reconciled.
- **`B02.01.02.F04` — Patient-Contributed History Inclusion:** Patient-contributed history is included, with accessible and accurate supporting evidence.
- **`B02.01.02.F05` — Explained History Reconciliation:** Explained history is reconciled.

#### B02.01.03 — Contextual Information Qualification
Clinicians can distinguish verified, reported, provisional and unavailable context.

- **`B02.01.03.F01` — QualifiedContextUse:** Clinical decisions retain the applicable limitations on their contextual information.
- **`B02.01.03.F02` — Source Reliability Qualification:** Source reliability is qualified.
- **`B02.01.03.F03` — Relevance to Current Decision:** Clinical context remains relevant to the current decision through relevant changes or disruptions, with recovery where required.
- **`B02.01.03.F04` — Uncertainty Visibility:** Uncertainty is visible, with accessible and accurate supporting evidence.
- **`B02.01.03.F05` — Conflicting Context Flagging:** Conflicting context is flagged.

#### B02.01.04 — Clinically Meaningful Information Currency
Enable clinicians to identify whether information is current, historic, disputed, incomplete or unavailable when establishing clinical context.

*Enterprise contributions: E02.01, E07.05, E01.02; findings: R09, R11.*

- **`B02.01.04.F01` — HistoricalStateQualified:** Clinician distinguishes historic state from current presentation.
- **`B02.01.04.F02` — UnavailableNotAssumedAbsent:** Service outage is not mistaken for negative clinical history.
- **`B02.01.04.F03` — DisputedFactsVisible:** Relevant unresolved corrections are clearly identified.
- **`B02.01.04.F04` — SourceAndTimeKnown:** Material facts include source, occurrence and documentation time.
- **`B02.01.04.F05` — CriticalContextIntegrity:** Missing critical context is flagged before consequential clinical decisions.

### B02.02 — Health & Functional Assessment
Determine current clinical, functional and support needs.

#### B02.02.01 — Clinical Condition Assessment
The current clinical state and presenting concern can be assessed within appropriate expertise.

- **`B02.02.01.F01` — CompletedRelevantAssessment:** Clinical assessments cover the domains required by their clinical context.
- **`B02.02.01.F02` — Comprehensive Presenting Assessment:** Comprehensive presenting is assessed through available and usable support.
- **`B02.02.01.F03` — Clinically Appropriate Observation:** Clinical assessments include observations appropriate to the clinical context.
- **`B02.02.01.F04` — Assessment Finding Communication:** Assessment finding communication reaches and can be understood by the intended recipient.
- **`B02.02.01.F05` — Reassessment Trigger Recognition:** Reassessment trigger is recognised, with accessible and accurate supporting evidence.

#### B02.02.02 — Functional Ability Assessment
A person's functional capacities and limitations can be established where relevant.

- **`B02.02.02.F01` — AssessedFunctionalNeeds:** Applicable care decisions are informed by an appropriate functional assessment.
- **`B02.02.02.F02` — Daily Function Coverage:** Daily function is covered.
- **`B02.02.02.F03` — Person-Centred Function Goals:** Person-centred function goals are established.
- **`B02.02.02.F04` — Environmental Factors Inclusion:** Environmental factors are included.
- **`B02.02.02.F05` — Function Change Visibility:** Function change is visible, with accessible and accurate supporting evidence.

#### B02.02.03 — Health Needs Reassessment
Assessments can be revised when a person's condition or circumstances change.

- **`B02.02.03.F01` — TimelyNeedsReassessment:** Qualifying changes prompt appropriate reassessment of care needs.
- **`B02.02.03.F02` — Meaningful Change Recognition:** Meaningful change is recognised, with accessible and accurate supporting evidence.
- **`B02.02.03.F03` — Prior Assessment Comparison:** Prior assessment can be compared.
- **`B02.02.03.F04` — Updated Need Communication:** Updated need is communicated through relevant changes or disruptions, with recovery where required.
- **`B02.02.03.F05` — Timely Reassessment:** Clinical needs are reassessed within the applicable context-defined interval.

### B02.03 — Clinical Investigation Direction
Decide what further evidence is needed and why.

#### B02.03.01 — Investigation Need Determination
Clinicians can decide what further investigation is justified by the clinical question.

- **`B02.03.01.F01` — JustifiedInvestigationIntent:** Investigation requests state an explicit, relevant clinical indication.
- **`B02.03.01.F02` — Investigative Purpose Clarity:** Investigative purpose is clear.
- **`B02.03.01.F03` — Proportionate Investigation Selection:** Proportionate investigation is selected.
- **`B02.03.01.F04` — Avoidance of Unnecessary Testing:** Unnecessary investigation testing is avoided.
- **`B02.03.01.F05` — Patient-Circumstance Consideration:** Patient-circumstance is considered.

#### B02.03.02 — Investigation Prioritisation
Investigation urgency and intended timing can reflect clinical need.

- **`B02.03.02.F01` — CorrectInvestigationPriority:** Investigation requests receive an appropriate priority.
- **`B02.03.02.F02` — Clinical Priority Accuracy:** Clinical priority is accurate.
- **`B02.03.02.F03` — Priority Change Recognition:** Priority change is recognised, with accessible and accurate supporting evidence.
- **`B02.03.02.F04` — Urgent Investigation Escalation:** Urgent investigation escalation reaches and can be understood by the intended recipient.
- **`B02.03.02.F05` — Priority Rationale Visibility:** Priority rationale is visible, with accessible and accurate supporting evidence.

#### B02.03.03 — Investigation Selection Review
Investigation choices can be reviewed to avoid unnecessary duplication or missed information.

- **`B02.03.03.F01` — AppropriateInvestigationSelection:** Investigation plans meet the documented appropriateness criteria.
- **`B02.03.03.F02` — Investigation Appropriateness Review:** Investigation appropriateness are reviewed.
- **`B02.03.03.F03` — Contraindication Visibility:** Contraindication is visible, with accessible and accurate supporting evidence.
- **`B02.03.03.F04` — Redundant Investigation Recognition:** Redundant investigation is recognised, with accessible and accurate supporting evidence.
- **`B02.03.03.F05` — Revised Investigation Plan:** Revised investigation has a plan.

### B02.04 — Diagnostic & Clinical Reasoning
Interpret findings and establish or revise clinical understanding.

#### B02.04.01 — Clinical Finding Interpretation
Clinicians can interpret findings in relevant patient and clinical context.

- **`B02.04.01.F01` — ContextualFindingInterpretation:** Clinical finding interpretations include the required context and caveats.
- **`B02.04.01.F02` — Finding Context Adequacy:** Finding context is adequate.
- **`B02.04.01.F03` — Uncertain Finding Identification:** Uncertain finding is identified.
- **`B02.04.01.F04` — Contradictory Finding Review:** Contradictory finding is reviewed, with accessible and accurate supporting evidence.
- **`B02.04.01.F05` — Communicated Clinical Significance:** Communicated clinical significance reaches and can be understood by the intended recipient.

#### B02.04.02 — Diagnostic Understanding Revision
New evidence can appropriately confirm, refine or challenge the current clinical understanding.

- **`B02.04.02.F01` — EvidenceResponsiveDiagnosticRevision:** Material new findings prompt documented diagnostic reconsideration.
- **`B02.04.02.F02` — New-Evidence Integration:** New-evidence can be integrated, with accessible and accurate supporting evidence.
- **`B02.04.02.F03` — Diagnostic Alternative Review:** Diagnostic alternative is reviewed, with accessible and accurate supporting evidence.
- **`B02.04.02.F04` — Prior Reasoning Traceability:** Prior reasoning is traceable, with accessible and accurate supporting evidence.
- **`B02.04.02.F05` — Revised Understanding Communication:** Revised understanding is communicated through available and usable support.

#### B02.04.03 — Diagnostic Uncertainty Communication
Clinicians can explicitly communicate unresolved uncertainty and next steps.

- **`B02.04.03.F01` — TransparentDiagnosticUncertainty:** Uncertain diagnostic cases retain recorded uncertainty and follow-up intent.
- **`B02.04.03.F02` — Uncertainty Explanation:** Uncertainty is explained.
- **`B02.04.03.F03` — Confidence Limitation Visibility:** Confidence limitation is visible, with accessible and accurate supporting evidence.
- **`B02.04.03.F04` — Follow-Up Investigation Advice:** Follow-up investigation advice reaches and can be understood by the intended recipient.
- **`B02.04.03.F05` — Appropriate Stakeholder Notification:** Appropriate stakeholder is notified.

### B02.05 — Clinical Risk & Priority Determination
Determine urgency, risk and priority of care.

#### B02.05.01 — Clinical Risk Appraisal
Clinical risks can be identified and assessed to inform decisions.

- **`B02.05.01.F01` — RecordedMaterialRisk:** Relevant risk factors are considered in applicable clinical cases.
- **`B02.05.01.F02` — Relevant Risk Factor Coverage:** Relevant risk factor is covered, including in adverse conditions.
- **`B02.05.01.F03` — Risk Level Justification:** Risk level is justified, including in adverse conditions.
- **`B02.05.01.F04` — Mitigation Need Recognition:** Mitigation need is recognised, with accessible and accurate supporting evidence.
- **`B02.05.01.F05` — Risk Change Sensitivity:** Risk change can be recognised, including in adverse conditions.

#### B02.05.02 — Urgency and Acuity Determination
The relative urgency of care can be determined from clinical presentation and circumstances.

- **`B02.05.02.F01` — AppropriateClinicalPriority:** Clinical presentations receive an appropriate response priority.
- **`B02.05.02.F02` — Clinically Consistent Acuity Assignment:** Clinically consistent acuity is assigned.
- **`B02.05.02.F03` — Urgent Priority Notification:** Urgent priority notification reaches and can be understood by the intended recipient.
- **`B02.05.02.F04` — Changed Acuity Recognition:** Changed acuity is recognised, with accessible and accurate supporting evidence.
- **`B02.05.02.F05` — Appropriate Escalation Path:** Appropriate escalation has a defined path.

#### B02.05.03 — Risk Reassessment and Escalation
Changing risks can lead to appropriate reassessment and escalation.

- **`B02.05.03.F01` — ResponsiveRiskEscalation:** Clinical risk escalations are managed within the applicable clinical conditions.
- **`B02.05.03.F02` — Risk Trigger Recognition:** Risk trigger is recognised, including in adverse conditions.
- **`B02.05.03.F03` — Timely Clinical Escalation:** Clinical risk is escalated within the applicable context-defined interval.
- **`B02.05.03.F04` — Escalation Receipt Confirmation:** Escalation receipt confirmation reaches and can be understood by the intended recipient.
- **`B02.05.03.F05` — Reassessment Outcome Trace:** Reassessment outcome is traceable, with accessible and accurate supporting evidence.

### B02.06 — Care Decision & Planning
Decide, agree, communicate and revise intended care.

#### B02.06.01 — Individual Care Plan Establishment
Clinicians and the person can establish coherent intended care and responsibilities.

- **`B02.06.01.F01` — Agreed, Communicated Care Intent:** Care plans communicate their intent and agreed responsibilities.
- **`B02.06.01.F02` — Agreed Care Objective Clarity:** Agreed care objective is clear.
- **`B02.06.01.F03` — Appropriate Care Contribution Assignment:** Appropriate care contribution is assigned.
- **`B02.06.01.F04` — Care Plan Accessibility:** Care plan is accessible and usable.
- **`B02.06.01.F05` — Plan Acceptance Visibility:** Plan acceptance is visible, with accessible and accurate supporting evidence.

#### B02.06.02 — Care Option Selection
The responsible clinician can select an appropriate care direction using available evidence and preferences.

- **`B02.06.02.F01` — DefensibleCareChoice:** Care decisions retain relevant rationale and consideration of preferences.
- **`B02.06.02.F02` — Benefit-Harm Comparison:** Benefit-harm can be compared, including in adverse conditions.
- **`B02.06.02.F03` — Preference-Concordant Selection:** Preference-concordant is selected through available and usable support.
- **`B02.06.02.F04` — Contraindication Consideration:** Contraindication is considered.
- **`B02.06.02.F05` — Recorded Option Rationale:** Recorded option has a recorded rationale, with accessible and accurate supporting evidence.

#### B02.06.03 — Care Plan Revision
Care intentions can change coherently when needs, findings or goals change.

- **`B02.06.03.F01` — CurrentCarePlanIntent:** Qualifying changes are reflected in updated and communicated care plans.
- **`B02.06.03.F02` — Changed Circumstances Recognition:** Changed circumstances are recognised, with accessible and accurate supporting evidence.
- **`B02.06.03.F03` — Updated Plan Communication:** Updated plan is communicated through relevant changes or disruptions, with recovery where required.
- **`B02.06.03.F04` — Revised Responsibility Assignment:** Revised responsibility is assigned.
- **`B02.06.03.F05` — Superseded Plan Visibility:** Superseded plan is visible, with accessible and accurate supporting evidence.

## B03 — Clinical Care & Response
Indicated clinical care and direct assistance can be provided, observed, adapted and continued safely.

### B03.01 — Direct Clinical Intervention
Provide indicated clinical treatments or procedures.

#### B03.01.01 — Indicated Treatment Delivery
Appropriate treatment can be delivered to a person under the relevant clinical responsibility.

- **`B03.01.01.F01` — CompletedIndicatedIntervention:** Prescribed interventions are delivered or appropriately deferred with a reason.
- **`B03.01.01.F02` — Treatment Identity Confirmation:** Treatment identity is confirmed.
- **`B03.01.01.F03` — Correct Treatment Execution:** Correct treatment can be executed.
- **`B03.01.01.F04` — Informed Treatment Readiness:** Informed treatment is established.
- **`B03.01.01.F05` — Treatment Outcome Capture:** Treatment outcome is captured.

#### B03.01.02 — Treatment Response Review
The effects and adverse responses of a treatment can be evaluated.

- **`B03.01.02.F01` — EvaluatedTreatmentResponse:** Qualifying interventions receive the required treatment response review.
- **`B03.01.02.F02` — Expected Response Monitoring:** Expected response can be monitored, with accessible and accurate supporting evidence.
- **`B03.01.02.F03` — Adverse Response Recognition:** Adverse response is recognised, including in adverse conditions.
- **`B03.01.02.F04` — Treatment Effect Interpretation:** Treatment effect is interpreted.
- **`B03.01.02.F05` — Response-Based Reassessment:** Response-based reassessment reaches and can be understood by the intended recipient.

#### B03.01.03 — Treatment Adaptation
Clinical interventions can be adjusted when evidence or the person's circumstances warrant it.

- **`B03.01.03.F01` — ResponsiveTreatmentAdjustment:** Changed clinical conditions receive a documented treatment disposition.
- **`B03.01.03.F02` — Indication for Change Clarity:** Indication for change is clear through relevant changes or disruptions, with recovery where required.
- **`B03.01.03.F03` — Adaptation Authority Confirmation:** Adaptation authority is confirmed, distinguishing permitted and nonconforming conditions.
- **`B03.01.03.F04` — Changed Treatment Communication:** Changed treatment is communicated through relevant changes or disruptions, with recovery where required.
- **`B03.01.03.F05` — Adaptation Effect Follow-Up:** Adaptation effect receives follow-up through relevant changes or disruptions, with recovery where required.

### B03.02 — Nursing & Direct Care
Provide ongoing nursing, observation and care assistance.

#### B03.02.01 — Continuity of Direct Care Delivery
Nursing and direct care needs can be met over the required period.

- **`B03.02.01.F01` — Uninterrupted Required Care:** Required care activities are fulfilled or safely reassigned during coverage transitions.
- **`B03.02.01.F02` — Uninterrupted Essential Care:** Essential care continues without interruption.
- **`B03.02.01.F03` — Responsible Caregiver Availability:** Responsible caregiver is available.
- **`B03.02.01.F04` — Care Activity Handover:** Care activity can be handed over through relevant changes or disruptions, with recovery where required.
- **`B03.02.01.F05` — Missed Care Detection:** Missed care is detected.

#### B03.02.02 — Nursing Care Planning
Nursing priorities, planned care and responsibilities can be established and updated.

- **`B03.02.02.F01` — CurrentNursingCareIntent:** Applicable episodes have a current and understandable nursing care plan.
- **`B03.02.02.F02` — Individual Nursing Need Coverage:** Individual nursing need is covered.
- **`B03.02.02.F03` — Nursing Intervention Priority:** Nursing intervention has an established priority.
- **`B03.02.02.F04` — Plan Communication to Care Team:** Plan communication to care team reaches and can be understood by the intended recipient.
- **`B03.02.02.F05` — Nursing Plan Review:** Nursing plan is reviewed, with accessible and accurate supporting evidence.

#### B03.02.03 — Personal Care Support
Appropriate assistance with daily care needs can be provided safely and respectfully.

- **`B03.02.03.F01` — RespectfulCareCompletion:** Support needs are fulfilled in accordance with relevant dignity and preference requirements.
- **`B03.02.03.F02` — Dignified Assistance:** Assistance respects the person’s dignity.
- **`B03.02.03.F03` — Preference-Respecting Care:** Preference-respecting care is supported through available and usable support.
- **`B03.02.03.F04` — Safe Assistance Availability:** Safe assistance is available, including in adverse conditions.
- **`B03.02.03.F05` — Personal Care Completion Awareness:** Personal care completion is made known.

### B03.03 — Clinical Observation & Response
Observe condition, detect changes and respond appropriately.

#### B03.03.01 — Condition Observation
Clinical changes can be recognised through appropriate observation.

- **`B03.03.01.F01` — RequiredObservationCoverage:** Defined observation requirements are completed or explicitly qualified.
- **`B03.03.01.F02` — Clinically Relevant Observation Coverage:** Clinically relevant observation is covered.
- **`B03.03.01.F03` — Observation Accuracy:** Observation is accurate.
- **`B03.03.01.F04` — Observation Interval Suitability:** Clinical observations use suitable intervals and meet the applicable context-defined timing conditions.
- **`B03.03.01.F05` — Deterioration Signal Visibility:** Deterioration signal is visible, including in adverse conditions.

#### B03.03.02 — Deterioration Recognition & Escalation
Material deterioration can be recognised and brought to the responsible clinical attention.

- **`B03.03.02.F01` — Timely Deterioration Escalation:** Qualifying deterioration is escalated within the applicable conditions.
- **`B03.03.02.F02` — ReceivedEscalationHandover:** Escalations are received by the appropriate clinical responder.
- **`B03.03.02.F03` — Deterioration Threshold Recognition:** Deterioration threshold is recognised, including in adverse conditions.
- **`B03.03.02.F04` — Escalation Recipient Availability:** Escalation recipient availability reaches and can be understood by the intended recipient.
- **`B03.03.02.F05` — Escalation Acknowledgement:** Escalation acknowledgement reaches and can be understood by the intended recipient.
- **`B03.03.02.F06` — Response Initiation Awareness:** Response initiation awareness reaches and can be understood by the intended recipient.

#### B03.03.03 — Response Effect Evaluation
The clinical response to a detected change can be reassessed.

- **`B03.03.03.F01` — EvaluatedClinicalResponse:** Qualifying clinical responses receive documented post-action evaluation.
- **`B03.03.03.F02` — Response Outcome Capture:** Response outcome capture reaches and can be understood by the intended recipient.
- **`B03.03.03.F03` — Residual Risk Review:** Residual risk is reviewed, including in adverse conditions.
- **`B03.03.03.F04` — Further Intervention Trigger:** Conditions requiring further intervention can trigger the appropriate response.
- **`B03.03.03.F05` — Evaluation Communication:** Evaluation communication reaches and can be understood by the intended recipient.

### B03.04 — Urgent & Emergency Response
Provide timely action when health is seriously threatened.

#### B03.04.01 — Emergency Need Recognition
A presentation requiring immediate action can be identified and prioritised.

- **`B03.04.01.F01` — CorrectEmergencyIdentification:** Critical clinical conditions are recognised appropriately.
- **`B03.04.01.F02` — Emergent Condition Identification:** Emergent condition is identified, distinguishing permitted and nonconforming conditions.
- **`B03.04.01.F03` — Immediate Safety Advice:** Immediate safety advice is available, including in adverse conditions.
- **`B03.04.01.F04` — Emergency Pathway Initiation:** Emergency pathway can be initiated.
- **`B03.04.01.F05` — Incorrect Triage Recognition:** Incorrect triage is recognised.

#### B03.04.02 — Immediate Clinical Stabilisation
The enterprise can organise and deliver time-critical stabilising care.

- **`B03.04.02.F01` — TimeCriticalStabilisation:** Qualifying presentations receive indicated stabilisation within the defined windows.
- **`B03.04.02.F02` — Stabilisation Readiness:** Stabilisation is established.
- **`B03.04.02.F03` — Priority Intervention Timeliness:** Priority intervention meets the applicable timing conditions.
- **`B03.04.02.F04` — Patient Response Visibility:** Patient response is visible, with accessible and accurate supporting evidence.
- **`B03.04.02.F05` — Continued Stabilisation Handover:** Continued stabilisation can be handed over through relevant changes or disruptions, with recovery where required.

#### B03.04.03 — Emergency Care Coordination
Relevant people and resources can be coordinated during urgent response.

- **`B03.04.03.F01` — CoordinatedEmergencyResponse:** Emergency responses have the required participants and resources available.
- **`B03.04.03.F02` — Team Activation Reliability:** Team activation is reliable.
- **`B03.04.03.F03` — Responsibility Assignment Clarity:** Responsibility assignment is clear.
- **`B03.04.03.F04` — Rapid Information Sharing:** Emergency response information is shared within the applicable context-defined interval.
- **`B03.04.03.F05` — Emergency Transition Readiness:** Emergency transition is established through relevant changes or disruptions, with recovery where required.

### B03.05 — Procedural & Peri-Procedural Care
Establish readiness, deliver and follow up clinical procedures.

#### B03.05.01 — Procedure Readiness Verification
A planned procedure can proceed only with essential clinical, consent and resource preparations in place.

- **`B03.05.01.F01` — VerifiedProceduralReadiness:** Procedures satisfy the prescribed preparation checks.
- **`B03.05.01.F02` — Consent and Authority Check:** Consent and authority is checked, distinguishing permitted and nonconforming conditions.
- **`B03.05.01.F03` — Pre-Procedure Safety Verification:** Pre-procedure safety is verified, including in adverse conditions.
- **`B03.05.01.F04` — Equipment and Team Availability:** Equipment and team is available.
- **`B03.05.01.F05` — Readiness Exception Resolution:** Readiness exception is resolved.

#### B03.05.02 — Procedural Care Delivery
Clinical procedures can be performed and documented under appropriate governance.

- **`B03.05.02.F01` — ProceduralCareConformance:** Procedural care meets its applicable care requirements.
- **`B03.05.02.F02` — Procedure Identity Assurance:** Procedure identity can be assured.
- **`B03.05.02.F03` — Safety Checkpoint Adherence:** Safety checkpoint is adhered to, including in adverse conditions.
- **`B03.05.02.F04` — Procedural Deviation Recording:** Procedural deviation is recorded, with accessible and accurate supporting evidence.
- **`B03.05.02.F05` — Immediate Outcome Communication:** Immediate outcome communication reaches and can be understood by the intended recipient.

#### B03.05.03 — Post-Procedural Recovery Support
Recovery needs and complications can be observed and managed following a procedure.

- **`B03.05.03.F01` — SafeRecoveryDisposition:** Recovery episodes have a verified disposition and follow-up.
- **`B03.05.03.F02` — Recovery Observation Coverage:** Recovery observation is covered through relevant changes or disruptions, with recovery where required.
- **`B03.05.03.F03` — Complication Recognition:** Complication is recognised, distinguishing permitted and nonconforming conditions.
- **`B03.05.03.F04` — Recovery Handover Sufficiency:** Recovery handover is sufficient.
- **`B03.05.03.F05` — Safe Discharge Readiness:** Safe discharge is established, including in adverse conditions.

### B03.06 — Supportive, Palliative & End-of-Life Care
Provide appropriate comfort, support and care in serious or life-limiting illness.

#### B03.06.01 — Comfort and Symptom Support
People with serious illness can receive appropriate relief and supportive care.

- **`B03.06.01.F01` — AssessedSymptomRelief:** Symptom burden and response are reassessed during supported care.
- **`B03.06.01.F02` — Person-Reported Comfort Goals:** Person-reported comfort goals are established, with accessible and accurate supporting evidence.
- **`B03.06.01.F03` — Symptom Relief Assessment:** Symptom relief is assessed.
- **`B03.06.01.F04` — Distress Escalation:** Distress escalation reaches and can be understood by the intended recipient.
- **`B03.06.01.F05` — Comfort Plan Adaptation:** Comfort plan can be adapted through relevant changes or disruptions, with recovery where required.

#### B03.06.02 — Goals of Care Alignment
Care can be guided by the person's applicable goals, values and expressed preferences.

- **`B03.06.02.F01` — GoalsAlignedCare:** Care decisions are consistent with documented goals and governing requirements.
- **`B03.06.02.F02` — Recorded Care Goals:** Recorded care goals are established, with accessible and accurate supporting evidence.
- **`B03.06.02.F03` — Preference and Authority Consistency:** Preference and authority is consistent through available and usable support.
- **`B03.06.02.F04` — Changed Goal Recognition:** Changed goal is recognised, with accessible and accurate supporting evidence.
- **`B03.06.02.F05` — Care Team Goal Understanding:** Care team goal can be understood through available and usable support.

#### B03.06.03 — End-of-Life and Family Support
Appropriate clinical, emotional and practical support can be arranged for the person and relevant others.

- **`B03.06.03.F01` — AppropriateEndOfLifeSupport:** Assessed end-of-life support needs are addressed or transparently referred.
- **`B03.06.03.F02` — Dignity and Cultural Respect:** Dignity and cultural is respected through available and usable support.
- **`B03.06.03.F03` — Family Communication Support:** Family communication support reaches and can be understood by the intended recipient.
- **`B03.06.03.F04` — Anticipatory Support Availability:** Anticipatory support is available.
- **`B03.06.03.F05` — Bereavement Transition Advice:** Bereavement transition advice is available through relevant changes or disruptions, with recovery where required.

## B04 — Diagnostic Investigation
Reliable diagnostic evidence can be requested, generated, interpreted, verified and communicated.

### B04.01 — Diagnostic Investigation Commissioning
Make an appropriate investigation available against a justified and valid request, including preparation and allocation of responsibility.

#### B04.01.01 — Diagnostic Request Qualification
A request can be accepted for investigation with an adequate clinical question and ordering context.

- **`B04.01.01.F01` — ValidDiagnosticRequest:** Diagnostic requests are complete enough to direct the intended investigation.
- **`B04.01.01.F02` — Investigation Intent Clarity:** Investigation intent is clear.
- **`B04.01.01.F03` — Relevant Clinical Context:** Diagnostic requests retain the clinical context relevant to the intended investigation.
- **`B04.01.01.F04` — Request Completeness:** Request is complete.
- **`B04.01.01.F05` — Inappropriate Request Resolution:** Inappropriate request is resolved.

#### B04.01.02 — Investigation Preparation Coordination
Preparation, setting and relevant resources can be arranged for the investigation.

- **`B04.01.02.F01` — InvestigationReadyAtStart:** Scheduled investigations have their prerequisites fulfilled or exceptions resolved before commencement.
- **`B04.01.02.F02` — Preparation Instruction Clarity:** Preparation instruction clarity reaches and can be understood by the intended recipient.
- **`B04.01.02.F03` — Readiness Confirmation:** Readiness confirmation reaches and can be understood by the intended recipient.
- **`B04.01.02.F04` — Preparation Exception Escalation:** Preparation exception escalation reaches and can be understood by the intended recipient.
- **`B04.01.02.F05` — Preparation Timing Coordination:** Preparation timing can be coordinated.

#### B04.01.03 — Diagnostic Priority Allocation
Diagnostic work can be assigned urgency and capacity consistent with need.

- **`B04.01.03.F01` — AppropriateDiagnosticPriority:** Investigations receive a justified priority.
- **`B04.01.03.F02` — Clinically Appropriate Priority:** Investigation priorities are appropriate to the clinical context.
- **`B04.01.03.F03` — Priority Decision Visibility:** Priority decision is visible, with accessible and accurate supporting evidence.
- **`B04.01.03.F04` — Priority Change Response:** Priority change receives an appropriate response through relevant changes or disruptions, with recovery where required.
- **`B04.01.03.F05` — Delay Risk Escalation:** Investigation delay risks are escalated within the applicable context-defined interval.

### B04.02 — Diagnostic Evidence Production
Establish observations, measurements, images or other evidence suitable for the intended investigation.

#### B04.02.01 — Specimen and Subject Identification
Diagnostic material or the examined subject can be reliably associated with the correct investigation.

- **`B04.02.01.F01` — CorrectSubjectAssociation:** Diagnostic evidence has verified linkage to the intended subject and request.
- **`B04.02.01.F02` — Correct Subject Matching:** Correct subject is matched.
- **`B04.02.01.F03` — Specimen Chain Integrity:** Specimen chain retains its integrity.
- **`B04.02.01.F04` — Misidentification Detection:** Misidentification is detected.
- **`B04.02.01.F05` — Specimen Provenance Availability:** Specimen provenance is available, with accessible and accurate supporting evidence.

#### B04.02.02 — Diagnostic Measurement Acquisition
Required images, measurements, specimens or observations can be obtained at adequate quality.

- **`B04.02.02.F01` — UsableDiagnosticEvidence:** Investigations produce evidence fit for its intended interpretation.
- **`B04.02.02.F02` — Measurement Method Suitability:** Measurement method is suitable.
- **`B04.02.02.F03` — Measurement Quality Completeness:** Measurement quality is complete.
- **`B04.02.02.F04` — Repeat Measurement Availability:** Repeat measurement is available through relevant changes or disruptions, with recovery where required.
- **`B04.02.02.F05` — Measurement Source Trace:** Measurement source is traceable, with accessible and accurate supporting evidence.

#### B04.02.03 — Evidence Quality Exception Handling
Unusable or incomplete evidence can be identified and appropriately corrected or qualified.

- **`B04.02.03.F01` — TransparentEvidenceExceptions:** Diagnostic evidence quality exceptions are recognised before unqualified reporting.
- **`B04.02.03.F02` — Invalid Evidence Recognition:** Invalid evidence is recognised.
- **`B04.02.03.F03` — Quality Exception Attribution:** Quality exception is attributable.
- **`B04.02.03.F04` — Repeat Acquisition Direction:** Repeat acquisition direction is available through relevant changes or disruptions, with recovery where required.
- **`B04.02.03.F05` — Quality Disposition Visibility:** Quality disposition is visible.

### B04.03 — Diagnostic Findings Reporting
Produce, communicate and appropriately escalate clinically usable findings within relevant professional authority.

#### B04.03.01 — Diagnostic Findings Interpretation
Appropriate specialists can prepare intelligible findings from diagnostic evidence.

- **`B04.03.01.F01` — ClinicallyUsableFinding:** Diagnostic reports contain the required interpretation and qualifiers.
- **`B04.03.01.F02` — Appropriate Reference Context:** Appropriate reference has its relevant context.
- **`B04.03.01.F03` — Interpretation Qualification:** Interpretation is qualified.
- **`B04.03.01.F04` — Clinically Significant Finding Recognition:** Clinically significant finding is recognised, with accessible and accurate supporting evidence.
- **`B04.03.01.F05` — Reviewable Finding Rationale:** Reviewable finding has a recorded rationale, with accessible and accurate supporting evidence.

#### B04.03.02 — Diagnostic Result Communication
Findings can reach the responsible care context with their significance preserved.

- **`B04.03.02.F01` — ConfirmedDiagnosticDelivery:** Reportable diagnostic results reach the intended responsible destination.
- **`B04.03.02.F02` — Correct Recipient Delivery:** Correct recipient can be delivered.
- **`B04.03.02.F03` — Result Receipt Visibility:** Result receipt is visible, with accessible and accurate supporting evidence.
- **`B04.03.02.F04` — Result Interpretation Context:** Result interpretation has its relevant context.
- **`B04.03.02.F05` — Unreceived Result Escalation:** Unreceived result escalation reaches and can be understood by the intended recipient.

#### B04.03.04 — Critical Result Receipt & Follow-Up Confirmation
Critical findings are classified, rapidly notified to an appropriately accountable and sufficiently identity-assured recipient, acknowledged within the applicable interval, escalated when missing or late, and followed up under the applicable clinical responsibility arrangements. Attributable evidence distinguishes delivery, acknowledgement and clinical action.

- **`B04.03.04.F01` — Accountable Critical-Result Recipient:** Critical diagnostic findings are directed to an appropriately accountable intended recipient under the applicable clinical responsibility arrangements, with sufficient recipient identity assurance. The responsibility basis can be established without Harmonia assuming authority to assign clinical responsibility.
- **`B04.03.04.F02` — ReceiptNotClinicalAction:** Notification delivery is not reported as clinical response completion.
- **`B04.03.04.F03` — RequiredFollowUpTracked:** The clinical response obligation remains open until suitably disposed.
- **`B04.03.04.F04` — Timely Critical-Result Acknowledgement and Escalation:** Critical-result acknowledgement is required within the applicable interval. Missing or late acknowledgement triggers the specified escalation pathway; notification or acknowledgement alone does not establish clinical response completion.
- **`B04.03.04.F05` — Attributable Critical-Result Communication Evidence:** Critical-result communication retains attributable evidence of the finding, intended and actual recipient, delivery, acknowledgement timing and applicable follow-up. Delivery, acknowledgement and clinical action remain distinguishable.
- **`B04.03.04.F06` — Critical Finding Classification:** Critical finding is classified, including in adverse conditions.
- **`B04.03.04.F07` — Rapid Appropriate Notification:** Critical findings are notified to an appropriate recipient within the applicable context-defined interval, with notification timing and delivery outcome identifiable.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-HIE-GATE, C-CT2-ACT. *Release horizon:* Unscheduled.

### B04.04 — Diagnostic Quality & Safety Assurance
Establish appropriate identification, traceability, technical validity and safety of investigations and findings.

#### B04.04.01 — Diagnostic Process Validity
Investigations can be conducted under required quality and safety controls.

- **`B04.04.01.F01` — VerifiedProcessConformance:** Investigations satisfy the relevant process controls.
- **`B04.04.01.F02` — Required Control Completion:** Required control can be completed.
- **`B04.04.01.F03` — Process Deviation Recognition:** Process deviation is recognised, with accessible and accurate supporting evidence.
- **`B04.04.01.F04` — Validity Evidence Availability:** Validity evidence is available.
- **`B04.04.01.F05` — Remediation of Invalid Investigation:** Invalid investigations can be remediated.

#### B04.04.02 — Verified Diagnostic Result Release
Diagnostic reports can be released only after necessary verification of source and quality.

- **`B04.04.02.F01` — Result Released with Verified Identity and Quality:** Released diagnostic reports pass the required identity and quality checks.
- **`B04.04.02.F02` — Authorised Result Release:** Authorised result is released under the applicable conditions, distinguishing permitted and nonconforming conditions.
- **`B04.04.02.F03` — Preliminary Versus Final Status:** Preliminary versus final has an explicit status, with accessible and accurate supporting evidence.
- **`B04.04.02.F04` — Corrected Result Communication:** Corrected result is communicated.
- **`B04.04.02.F05` — Release Auditability:** Release is auditable, with accessible and accurate supporting evidence.

#### B04.04.03 — Diagnostic Traceability
Investigation, evidence, interpretation and report can be traced together.

- **`B04.04.03.F01` — TraceableDiagnosticLineage:** Diagnostic results retain the required unbroken investigation history.
- **`B04.04.03.F02` — Source-to-Finding Linkage:** Source-to-finding is linked.
- **`B04.04.03.F03` — Investigation Event Chronology:** Investigation event has an identifiable chronology, with accessible and accurate supporting evidence.
- **`B04.04.03.F04` — Method and Equipment Attribution:** Method and equipment is attributable, with accessible and accurate supporting evidence.
- **`B04.04.03.F05` — Retained Investigation Evidence:** Retained investigation is evidenced, with accessible and accurate supporting evidence.

#### B04.04.04 — Diagnostic Information Release Accountability
Determine that diagnostic results are validly released and traceable without equating transport receipt with clinical responsibility transfer.

*Enterprise contributions: E03.01, E03.04, E08.07; findings: R16.*

- **`B04.04.04.F01` — ResultVerifiedBeforeRelease:** Published diagnostic result has required verification.
- **`B04.04.04.F02` — ReleaseIdentityKnown:** Accountable releaser and release instant can be established.
- **`B04.04.04.F03` — DeliveryNotClinicalAcceptance:** Technical delivery is distinguished from clinician review.
- **`B04.04.04.F04` — CorrectionHistoryPreserved:** Corrected results retain historical traceability.
- **`B04.04.04.F05` — CriticalResultEscalationTracked:** Urgent findings have a traceable acknowledgement/escalation path.

## B05 — Care Coordination & Continuity
Care intentions, contributions and responsibilities can remain coherent over time and across boundaries.

### B05.01 — Multidisciplinary Care Coordination
Enable several parties to plan and contribute coherently.

#### B05.01.01 — Shared Care Intent Alignment
Different contributors can work from a coherent understanding of intended care.

- **`B05.01.01.F01` — AlignedMultidisciplinaryIntent:** Shared care maintains a consistent documented multidisciplinary plan.
- **`B05.01.01.F02` — Shared Outcome Understanding:** Shared outcome can be understood through available and usable support.
- **`B05.01.01.F03` — Care Goal Disagreement Recognition:** Care goal disagreement is recognised, with accessible and accurate supporting evidence.
- **`B05.01.01.F04` — Agreed Priority Visibility:** Agreed priority is visible, with accessible and accurate supporting evidence.
- **`B05.01.01.F05` — Intent Update Communication:** Intent update communication reaches and can be understood by the intended recipient.

#### B05.01.02 — Care Contribution Coordination
Required clinical and supporting contributions can be arranged without avoidable conflict.

- **`B05.01.02.F01` — CoordinatedCareContributions:** Required care participants are engaged at the appropriate point.
- **`B05.01.02.F02` — Contributor Responsibility Clarity:** Contributor responsibility is clear.
- **`B05.01.02.F03` — Timely Contribution Requests:** Care contribution requests reach participants within the applicable context-defined interval.
- **`B05.01.02.F04` — Contribution Status Visibility:** Contribution status is visible, with accessible and accurate supporting evidence.
- **`B05.01.02.F05` — Unfulfilled Contribution Escalation:** Unfulfilled contribution escalation reaches and can be understood by the intended recipient.

#### B05.01.03 — Multidisciplinary Decision Review
Material differences of clinical opinion can be surfaced and reconciled or qualified.

- **`B05.01.03.F01` — ReviewableSharedCareDecision:** Multidisciplinary decisions have an accountable disposition.
- **`B05.01.03.F02` — Appropriate Discipline Inclusion:** Appropriate discipline is included.
- **`B05.01.03.F03` — Review Finding Record:** Review finding is recorded, with accessible and accurate supporting evidence.
- **`B05.01.03.F04` — Decision Conflict Resolution:** Decision conflict is resolved.
- **`B05.01.03.F05` — Review Action Follow-Up:** Review action receives follow-up, with accessible and accurate supporting evidence.

#### B05.01.04 — Team Task Participation & Handover
Enable multidisciplinary teams to participate in delegated or shared work without losing the accountability for each contribution.

- **`B05.01.04.F01` — TeamContributionOwnership:** Each assigned contribution has a clear responsible party and review status.
- **`B05.01.04.F02` — SharedTaskPolicyVisible:** Aggregate or assigned-to-finish requirements are known to relevant participants.
- **`B05.01.04.F03` — HandoverOutstandingWork:** Transferred responsibilities retain context and unfinished obligations.
- **`B05.01.04.F04` — UnfinishedContributionVisible:** One participant’s completion does not silently complete all required team contributions.
- **`B05.01.04.F05` — ClinicalTeamContextProtected:** Team collaboration does not bypass patient consent or confidentiality boundaries.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-CT2-ACT, DRP-PR. *Release horizon:* Unscheduled.

### B05.02 — Referral & Transfer Coordination
Arrange movement to an appropriate provider, service or care setting.

#### B05.02.01 — Appropriate Referral Destination
A request for another service can be directed to an appropriate provider or setting.

- **`B05.02.01.F01` — SuitableReferralDestination:** Accepted referrals meet the relevant destination criteria.
- **`B05.02.01.F02` — Needs-Matched Referral Selection:** Needs-matched referral is selected.
- **`B05.02.01.F03` — Destination Eligibility Confirmation:** Destination eligibility is confirmed, distinguishing permitted and nonconforming conditions.
- **`B05.02.01.F04` — Referral Acceptance Feasibility:** Referral acceptance feasibility reaches and can be understood by the intended recipient.
- **`B05.02.01.F05` — Alternative Destination Availability:** Alternative destination is available through relevant changes or disruptions, with recovery where required.

#### B05.02.02 — Referral Acceptance and Tracking
A referral can be tracked until accepted, declined or otherwise resolved.

- **`B05.02.02.F01` — ResolvedReferralDisposition:** Referrals have accountable and visible outcomes.
- **`B05.02.02.F02` — Referral Receipt Confirmation:** Referral receipt confirmation reaches and can be understood by the intended recipient.
- **`B05.02.02.F03` — Referral Disposition Visibility:** Referral disposition is visible, with accessible and accurate supporting evidence.
- **`B05.02.02.F04` — Unaccepted Referral Escalation:** Unaccepted referral escalation reaches and can be understood by the intended recipient.
- **`B05.02.02.F05` — Referral Closure Evidence:** Referral closure is evidenced, with accessible and accurate supporting evidence.

#### B05.02.03 — Transport and Transfer Preparation
Necessary readiness for safe transfer can be coordinated across parties.

- **`B05.02.03.F01` — ReadyCareTransfer:** Care transfers satisfy the applicable pre-transfer requirements.
- **`B05.02.03.F02` — Transport Need Suitability:** Transport need is suitable.
- **`B05.02.03.F03` — Transfer Readiness Confirmation:** Transfer readiness are confirmed through relevant changes or disruptions, with recovery where required.
- **`B05.02.03.F04` — Clinical Escort Arrangements:** Clinical escort arrangements are established.
- **`B05.02.03.F05` — Contingency for Transfer Delay:** Care transfer delays receive a contingency arrangement within the applicable context-defined interval.

### B05.03 — Care Responsibility Transfer
Establish and communicate who is responsible for continuing care.

#### B05.03.01 — Clinical Handover Acceptance
Receiving care responsibility can be explicitly acknowledged in the applicable context.

- **`B05.03.01.F01` — Confirmed Receiving Responsibility:** Care transfers have verified acceptance by a responsible receiving party.
- **`B05.03.01.F02` — OutstandingCareContinuity:** Accepted transfers identify consequential outstanding care obligations.
- **`B05.03.01.F03` — Receiving Party Verification:** Receiving party is verified.
- **`B05.03.01.F04` — Clear Responsibility Boundary:** Clear responsibility has an explicit boundary.
- **`B05.03.01.F05` — Timely Handover Confirmation:** Care handovers are confirmed within the applicable context-defined interval.
- **`B05.03.01.F06` — Rejected Handover Resolution:** Rejected handover is resolved through relevant changes or disruptions, with recovery where required.

#### B05.03.02 — Handover Information Sufficiency
The receiving party can obtain the information necessary to continue care appropriately.

- **`B05.03.02.F01` — SufficientHandoverContext:** Handovers transfer the required care context.
- **`B05.03.02.F02` — Essential Context Completeness:** Essential context is complete.
- **`B05.03.02.F03` — Pending Result Visibility:** Pending result is visible, with accessible and accurate supporting evidence.
- **`B05.03.02.F04` — Current Plan Transfer:** Current plan can be transferred through relevant changes or disruptions, with recovery where required.
- **`B05.03.02.F05` — Missing Information Escalation:** Missing information escalation reaches and can be understood by the intended recipient.

#### B05.03.03 — Outstanding Obligation Transfer
Unfinished actions and follow-up responsibilities can be identified and assigned.

- **`B05.03.03.F01` — AccountableOutstandingCare:** Outstanding care actions have an acknowledged next responsibility.
- **`B05.03.03.F02` — Outstanding Action Identification:** Outstanding action is identified.
- **`B05.03.03.F03` — Responsible Recipient Assignment:** Responsible recipient assignment reaches and can be understood by the intended recipient.
- **`B05.03.03.F04` — Due-Time Communication:** Due-time communication reaches and can be understood by the intended recipient.
- **`B05.03.03.F05` — Unaccepted Obligation Escalation:** Unaccepted obligation escalation reaches and can be understood by the intended recipient.

#### B05.03.04 — Handover Acceptance & Evidence
Establish whether receiving responsibility was actually accepted and whether outstanding obligations remain accounted for.

*Enterprise contributions: E06.07, E03.04, E08.07; findings: R07, R16.*

- **`B05.03.04.F01` — ReceivingPartyAgreed:** Handover identifies who accepted clinical responsibility.
- **`B05.03.04.F02` — UnacceptedHandoverVisible:** No receipt notification is treated as acceptance without required evidence.
- **`B05.03.04.F03` — OutstandingWorkOwned:** Each unresolved obligation has accountable next owner.
- **`B05.03.04.F04` — TransferredContextSufficient:** Required care context is available to receiver.
- **`B05.03.04.F05` — AcceptanceTimeTraceable:** Timing of acceptance and any delay is independently evidenced.

### B05.04 — Longitudinal Care Coordination
Maintain coherent care intentions and activities over time.

#### B05.04.01 — Longitudinal Care Intent Continuity
Care plans and decisions can remain coherent over multiple episodes and settings.

- **`B05.04.01.F01` — Coherent Longitudinal Care:** Care intent remains coherent over time and across care episodes, with unexplained contradictory intent identified and appropriately qualified.
- **`B05.04.01.F02` — Persistent Care Goal Context:** Persistent care goal has its relevant context.
- **`B05.04.01.F03` — Cross-Setting Goal Alignment:** Cross-setting goal is aligned.
- **`B05.04.01.F04` — Changed Intent Recognition:** Changed intent is recognised, with accessible and accurate supporting evidence.
- **`B05.04.01.F05` — Accessible Continuity Plan:** Accessible continuity has a plan through available and usable support.

#### B05.04.02 — Cross-Episode Care Context
Participants in later episodes can understand relevant previous care.

- **`B05.04.02.F01` — AvailablePriorCareContext:** Later care episodes have the required earlier care context available.
- **`B05.04.02.F02` — Prior Episode Relevance:** Prior episode is relevant.
- **`B05.04.02.F03` — Cross-Episode Information Availability:** Cross-episode information is available.
- **`B05.04.02.F04` — Repeated Assessment Reduction:** Repeated assessment is reduced through relevant changes or disruptions, with recovery where required.
- **`B05.04.02.F05` — Cross-Episode Source Traceability:** Cross-episode source is traceable, with accessible and accurate supporting evidence.

#### B05.04.03 — Continuity Exception Recognition
Interruptions or gaps in ongoing care can be recognised and addressed.

- **`B05.04.03.F01` — DetectedContinuityGap:** Care continuity gaps are detected and assigned for action.
- **`B05.04.03.F02` — Interrupted Care Recognition:** Interrupted care is recognised, with accessible and accurate supporting evidence.
- **`B05.04.03.F03` — Unassigned Responsibility Detection:** Unassigned responsibility is detected.
- **`B05.04.03.F04` — Continuity Risk Escalation:** Continuity risk is escalated, including in adverse conditions.
- **`B05.04.03.F05` — Restoration of Care Arrangements:** Restoration of care arrangements are established through relevant changes or disruptions, with recovery where required.

#### B05.04.04 — Longitudinal Care Context Continuity
Longitudinal care context preserves and exposes meaningful source-established encounter and episode associations, historical context and uncertainty across care transitions without assuming ownership of related information or inferring clinical links from temporal adjacency. Participants can obtain the appropriate care history across organisation, encounter and clinical service transitions.

- **`B05.04.04.F01` — Longitudinal Encounter Continuity:** Clinical teams can distinguish events belonging to separate care episodes and use meaningful source-established encounter or episode associations across referrals, investigations, results, follow-ups and subsequent work, with attribution and qualifications preserved. These associations do not transfer ownership of the related information.
- **`B05.04.04.F02` — TransitionContextAvailability:** Relevant prior-care context can be accessed during authorised handover.
- **`B05.04.04.F03` — StaleCareContextWarning:** Materially outdated care facts are distinguishable from current facts.
- **`B05.04.04.F04` — CrossProviderEvidenceTraceability:** Record contributors and source organisations remain identifiable.
- **`B05.04.04.F05` — IncompleteJourneyQualification:** Gaps in longitudinal history are disclosed where clinically significant and permitted.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-LHR-QUERY, C-FED-QUERY. *Release horizon:* Unscheduled.

### B05.05 — Follow-Up & Ongoing Care Arrangement
Ensure required follow-up is organised and not lost.

#### B05.05.01 — Required Follow-Up Coordination
Necessary later care can be arranged and communicated.

- **`B05.05.01.F01` — Follow-Up Arrangement Confirmed:** Required follow-ups have a confirmed provider, timing or accountable arrangement.
- **`B05.05.01.F02` — Follow-Up Need Identification:** Follow-up need is identified.
- **`B05.05.01.F03` — Follow-Up Responsibility Assignment:** Follow-up responsibility is assigned.
- **`B05.05.01.F04` — Suitable Follow-Up Timing:** Suitable follow-up has its timing established.
- **`B05.05.01.F05` — Patient Follow-Up Information:** Patient follow-up information is available.

#### B05.05.02 — Follow-Up Completion Awareness
The enterprise can determine whether required follow-up actually occurred.

- **`B05.05.02.F01` — VisibleFollowUpDisposition:** Tracked follow-up obligations have a known completion or exception disposition.
- **`B05.05.02.F02` — Follow-Up Status Visibility:** Follow-up status is visible, with accessible and accurate supporting evidence.
- **`B05.05.02.F03` — Completed Care Confirmation:** Completed care is confirmed.
- **`B05.05.02.F04` — Missed Follow-Up Recognition:** Missed follow-up is recognised, with accessible and accurate supporting evidence.
- **`B05.05.02.F05` — Follow-Up Closure Trace:** Follow-up closure is traceable, with accessible and accurate supporting evidence.

#### B05.05.03 — Unattended Follow-Up Escalation
Missed consequential follow-ups can trigger appropriate action.

- **`B05.05.03.F01` — ActionedOverdueFollowUp:** Qualifying overdue follow-ups are acted on within the relevant conditions.
- **`B05.05.03.F02` — Overdue Action Detection:** Overdue action is detected.
- **`B05.05.03.F03` — Appropriate Escalation Recipient:** Escalation recipient is appropriate.
- **`B05.05.03.F04` — Follow-Up Escalation Acknowledgement:** Follow-up escalation acknowledgement reaches and can be understood by the intended recipient.
- **`B05.05.03.F05` — Escalation Outcome Visibility:** Escalation outcome is visible, with accessible and accurate supporting evidence.

#### B05.05.04 — Deferred Clinical Follow-Up Accountability
Maintain continuity of care obligations that can only be fulfilled after prolonged human or external activity.

- **`B05.05.04.F01` — LongFollowUpIdentifiable:** A follow-up obligation remains identifiable over multi-day or multi-week periods.
- **`B05.05.04.F02` — NoPrematureFulfilment:** Digital acknowledgement alone does not close a clinically required follow-up.
- **`B05.05.04.F03` — EscalationAccountability:** Overdue clinical follow-up has an accountable escalation pathway.
- **`B05.05.04.F04` — LateOutcomeIntegration:** Relevant completion reports can be reconciled after interrupted or offline periods.
- **`B05.05.04.F05` — CrossCareTeamHandover:** Transferred follow-up preserves responsibility and its prior context.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-CT2-ACT, DRP-PR. *Release horizon:* Unscheduled.

### B05.06 — Community & Cross-Organisation Continuity
Support continuity where care crosses organisational, geographic or sector boundaries.

#### B05.06.01 — Cross-Provider Care Continuity
Care can continue coherently across separate organisations.

- **`B05.06.01.F01` — PreservedCrossProviderIntent:** Receiving care providers obtain the relevant care intent during transitions.
- **`B05.06.01.F02` — Shared Continuity Intent:** Shared continuity has explicit intent through relevant changes or disruptions, with recovery where required.
- **`B05.06.01.F03` — Cross-Provider Information Sufficiency:** Cross-provider information is sufficient.
- **`B05.06.01.F04` — Handover Across Organisation:** Care handovers preserve the required context across organisational boundaries through relevant changes or disruptions, with recovery where required.
- **`B05.06.01.F05` — Responsibility Across Boundaries:** Care responsibility remains identifiable across provider boundaries.

#### B05.06.02 — Community Service Integration
Clinical and community support contributions can be coordinated where needed.

- **`B05.06.02.F01` — CoordinatedCommunityContribution:** Qualifying cases have established community support arrangements.
- **`B05.06.02.F02` — Community Support Needs Matching:** Community support needs are matched.
- **`B05.06.02.F03` — Community Provider Engagement:** Community provider can be engaged.
- **`B05.06.02.F04` — Coordination Agreement Visibility:** Coordination agreement is visible, with accessible and accurate supporting evidence.
- **`B05.06.02.F05` — Community Transition Follow-Up:** Community transition receives follow-up through relevant changes or disruptions, with recovery where required.

#### B05.06.03 — Geographic Care Continuity
Necessary care can be maintained despite travel, relocation or geographic access constraints.

- **`B05.06.03.F01` — ContinuityAcrossLocationChange:** Location changes preserve required care access and responsibilities.
- **`B05.06.03.F02` — Remote Care Accessibility:** Remote care is accessible and usable.
- **`B05.06.03.F03` — Location-Appropriate Service Options:** Location-appropriate service options are available.
- **`B05.06.03.F04` — Cross-Region Information Continuity:** Cross-region information is maintained through relevant changes or disruptions, with recovery where required.
- **`B05.06.03.F05` — Travel Disruption Alternatives:** Travel disruption alternatives are available through relevant changes or disruptions, with recovery where required.

## B06 — Therapies, Medicines & Functional Support
Medicines, rehabilitation and therapeutic or psychosocial support can be appropriately provided and reviewed.

### B06.01 — Medicines Therapy Management
Prescribe, reconcile, administer, review and optimise medicines as appropriate.

#### B06.01.01 — Medicine Regimen Establishment
A clinically appropriate medicine regimen can be established and communicated.

- **`B06.01.01.F01` — DocumentedRegimenIntent:** Medication regimens state clear dose, purpose, route and review intent.
- **`B06.01.01.F02` — Indication and Regimen Clarity:** Indication and regimen is clear.
- **`B06.01.01.F03` — Medicine Risk and Interaction Review:** Medicine risk and interaction is reviewed, including in adverse conditions.
- **`B06.01.01.F04` — Agreed Regimen Communication:** Agreed regimen communication reaches and can be understood by the intended recipient.
- **`B06.01.01.F05` — Regimen Change History:** Regimen change retains its history, with accessible and accurate supporting evidence.

#### B06.01.02 — Medication Reconciliation at Care Transition
Medicine information can be compared and resolved when care responsibility changes.

- **`B06.01.02.F01` — Reconciled Medication Intent:** Medication information is clinically reconciled during relevant transitions.
- **`B06.01.02.F02` — Complete Medicine History Comparison:** Complete medicine history can be compared.
- **`B06.01.02.F03` — Discrepancy Detection:** Discrepancy is detected.
- **`B06.01.02.F04` — Agreed Medication List:** The clinically agreed medication list is available.
- **`B06.01.02.F05` — Reconciliation Communication:** Reconciliation is communicated.

#### B06.01.03 — Medicine Therapy Review
Medicine effectiveness, safety and continuing need can be reassessed.

- **`B06.01.03.F01` — ReviewedMedicationNeed:** Qualifying medicine therapy reviews have a documented disposition.
- **`B06.01.03.F02` — Efficacy and Harm Review:** Efficacy and harm is reviewed, including in adverse conditions.
- **`B06.01.03.F03` — Medicines Burden Recognition:** Medicines burden is recognised, with accessible and accurate supporting evidence.
- **`B06.01.03.F04` — Review Outcome Communication:** Review outcome is communicated, with accessible and accurate supporting evidence.
- **`B06.01.03.F05` — Treatment Adjustment Follow-Up:** Treatment adjustment receives follow-up.

### B06.02 — Dispensing & Medication Supply
Dispense and make appropriate medicines available.

#### B06.02.01 — Prescription Dispensing
Authorised prescriptions can be checked and supplied appropriately.

- **`B06.02.01.F01` — CorrectDispensingOutcome:** Medicine supplies meet prescribed verification and labelling requirements.
- **`B06.02.01.F02` — Prescription Legitimacy Confirmation:** Prescription legitimacy confirmation reaches and can be understood by the intended recipient.
- **`B06.02.01.F03` — Correct Medicine Supply:** Medicine supply is correct.
- **`B06.02.01.F04` — Dispensing Counselling Sufficiency:** Dispensing counselling is sufficient.
- **`B06.02.01.F05` — Dispensing Exception Escalation:** Dispensing exception escalation reaches and can be understood by the intended recipient.

#### B06.02.02 — Medicine Supply Continuity
People can obtain required medicines across predictable changes in care or supply.

- **`B06.02.02.F01` — UninterruptedNeededMedicineSupply:** Required medicine courses remain available without avoidable supply interruption.
- **`B06.02.02.F02` — Supply Interruption Detection:** Supply interruption is detected through relevant changes or disruptions, with recovery where required.
- **`B06.02.02.F03` — Therapeutic Alternative Communication:** Therapeutic alternative is communicated through relevant changes or disruptions, with recovery where required.
- **`B06.02.02.F04` — Refill Arrangement Visibility:** Refill arrangement is visible, with accessible and accurate supporting evidence.
- **`B06.02.02.F05` — Medicine Availability Assurance:** Medicine availability can be assured.

#### B06.02.03 — Medicine Counselling and Use Support
Recipients can receive appropriate instructions for safe and effective medicine use.

- **`B06.02.03.F01` — UnderstoodMedicineInstructions:** Medicine recipients receive suitable information and understanding checks.
- **`B06.02.03.F02` — Understandable Administration Guidance:** Understandable administration guidance is available through available and usable support.
- **`B06.02.03.F03` — Individual Use Barrier Recognition:** Individual use barrier is recognised through available and usable support.
- **`B06.02.03.F04` — Adverse Effect Advice:** Adverse effect advice is available, including in adverse conditions.
- **`B06.02.03.F05` — Adherence Support Follow-Up:** Adherence support receives follow-up.

### B06.03 — Functional Rehabilitation & Restoration
Improve or maintain function, participation and independence.

#### B06.03.01 — Functional Goal Management
Clinicians and participants can establish and review meaningful functional goals.

- **`B06.03.01.F01` — Progress Against Agreed Functional Goals:** Functional goals have recorded progress and an agreed reassessment.
- **`B06.03.01.F02` — Personally Meaningful Goals:** Personally meaningful goals are established.
- **`B06.03.01.F03` — Baseline Function Assessment:** Baseline function is assessed.
- **`B06.03.01.F04` — Goal Progress Review:** Goal progress are reviewed, with accessible and accurate supporting evidence.
- **`B06.03.01.F05` — Goal Change Agreement:** Goal change has an established agreement through relevant changes or disruptions, with recovery where required.

#### B06.03.02 — Rehabilitation Intervention Delivery
Appropriate rehabilitation interventions can be provided against assessed needs.

- **`B06.03.02.F01` — DeliveredRelevantRehabilitation:** Planned rehabilitation interventions are fulfilled or appropriately adjusted.
- **`B06.03.02.F02` — Intervention Plan Suitability:** Intervention plan is suitable.
- **`B06.03.02.F03` — Safe Intervention Completion:** Safe intervention can be completed, including in adverse conditions.
- **`B06.03.02.F04` — Participation Accessibility:** Participation is accessible and usable.
- **`B06.03.02.F05` — Rehabilitation Progress Visibility:** Rehabilitation progress are visible, with accessible and accurate supporting evidence.

#### B06.03.03 — Functional Outcome Evaluation
Functional outcomes can be evaluated against the person's starting state and goals.

- **`B06.03.03.F01` — EvaluatedFunctionalChange:** Completed rehabilitation plans have an attributable functional outcome assessment.
- **`B06.03.03.F02` — Functional Improvement Assessment:** Functional improvement is assessed.
- **`B06.03.03.F03` — Goal Achievement Evidence:** Goal achievement is evidenced, with accessible and accurate supporting evidence.
- **`B06.03.03.F04` — Unmet Need Recognition:** Unmet need is recognised, with accessible and accurate supporting evidence.
- **`B06.03.03.F05` — Outcome Communication:** Outcome communication reaches and can be understood by the intended recipient.

### B06.04 — Assistive & Therapeutic Support
Provide equipment, devices and tailored assistance needed by people.

#### B06.04.01 — Assistive Support Selection
People can obtain assistance or devices appropriate to their assessed needs and preferences.

- **`B06.04.01.F01` — SuitableAssistiveSupport:** Assistive supports match the identified functional requirements.
- **`B06.04.01.F02` — Individual Need Match:** Individual need is matched.
- **`B06.04.01.F03` — Accessibility and Preference Fit:** Accessibility and preference is suitable through available and usable support.
- **`B06.04.01.F04` — Safe Support Suitability:** Safe support is suitable, including in adverse conditions.
- **`B06.04.01.F05` — Alternative Support Options:** Alternative support options are available through relevant changes or disruptions, with recovery where required.

#### B06.04.02 — Assistive Equipment Provision
Required assistive equipment or adaptations can be arranged and made usable.

- **`B06.04.02.F01` — TimelyAssistiveProvision:** Agreed assistive provisions are available within the relevant timeframe.
- **`B06.04.02.F02` — Appropriate Equipment Fit:** Appropriate equipment is suitable.
- **`B06.04.02.F03` — Timely Equipment Availability:** Required assistive equipment is available within the applicable context-defined interval.
- **`B06.04.02.F04` — Equipment Use Instruction:** Equipment use instruction reaches and can be understood by the intended recipient.
- **`B06.04.02.F05` — Equipment Adjustment Support:** Equipment adjustments can be supported.

#### B06.04.03 — Assistive Support Review
Changes in needs or equipment suitability can prompt adjustment or replacement.

- **`B06.04.03.F01` — ReviewedAssistiveSuitability:** Qualifying changes prompt appropriate reassessment of assistive support.
- **`B06.04.03.F02` — Ongoing Support Suitability:** Ongoing support is suitable.
- **`B06.04.03.F03` — Changed Function Recognition:** Changed function is recognised, with accessible and accurate supporting evidence.
- **`B06.04.03.F04` — Safe Use Review:** Safe use is reviewed, including in adverse conditions.
- **`B06.04.03.F05` — Support Replacement Decisions:** Changes of assistive support have an explicit replacement decision through relevant changes or disruptions, with recovery where required.

### B06.05 — Psychosocial & Behavioural Support
Provide appropriate mental, behavioural and social intervention/support.

#### B06.05.01 — Psychosocial Needs Understanding
Relevant psychosocial circumstances and support needs can be understood respectfully.

- **`B06.05.01.F01` — RecognisedPsychosocialNeeds:** Psychosocial assessments address the agreed applicable domains.
- **`B06.05.01.F02` — Person-Defined Psychosocial Concern:** People can express their own psychosocial concerns.
- **`B06.05.01.F03` — Social Context Inclusion:** Social context is included.
- **`B06.05.01.F04` — Risk and Protection Factors:** Relevant psychosocial risk and protection factors can be identified, including in adverse conditions.
- **`B06.05.01.F05` — Support Preference Visibility:** Support preference is visible through available and usable support.

#### B06.05.02 — Psychological and Behavioural Intervention
Appropriate support or therapeutic interventions can be provided within professional scope.

- **`B06.05.02.F01` — EvaluatedSupportResponse:** Psychological and behavioural intervention plans receive outcome and experience review.
- **`B06.05.02.F02` — Agreed Therapeutic Objective:** Agreed therapeutic has an explicit objective.
- **`B06.05.02.F03` — Appropriate Intervention Access:** Appropriate intervention can be accessed.
- **`B06.05.02.F04` — Change in Wellbeing Assessment:** Change in wellbeing is assessed through relevant changes or disruptions, with recovery where required.
- **`B06.05.02.F05` — Concern Escalation:** Concern escalation reaches and can be understood by the intended recipient.

#### B06.05.03 — Social Support Connection
People can be connected with appropriate community and social supports.

- **`B06.05.03.F01` — ConfirmedSupportConnection:** Agreed support referrals have a confirmed connection or a reasoned exception.
- **`B06.05.03.F02` — Suitable Community Connection:** Suitable community can be connected.
- **`B06.05.03.F03` — Warm Referral Confirmation:** Warm referral confirmation reaches and can be understood by the intended recipient.
- **`B06.05.03.F04` — Social Support Accessibility:** Social support is accessible and usable.
- **`B06.05.03.F05` — Connection Continuity:** Connection is maintained through relevant changes or disruptions, with recovery where required.

## B07 — Health Service Provision
Healthcare services can be defined, made accessible, resourced, delivered and accounted for.

### B07.01 — Health Service Portfolio Management
Define the services available and the needs they address.

#### B07.01.01 — Service Offering Definition
The enterprise can specify what healthcare services it offers and to whom.

- **`B07.01.01.F01` — ClearServiceDescription:** Service offerings have defined scope, eligibility and access conditions.
- **`B07.01.01.F02` — Clear Service Scope:** Clear service has an explicit scope, distinguishing permitted and nonconforming conditions.
- **`B07.01.01.F03` — Intended Population Clarity:** Intended population is clear.
- **`B07.01.01.F04` — Access Condition Visibility:** Access condition is visible, distinguishing permitted and nonconforming conditions.
- **`B07.01.01.F05` — Service Owner Accountability:** Service owner has an accountable party, with accessible and accurate supporting evidence.

#### B07.01.02 — Service Portfolio Review
Service offerings can be revised as needs, priorities or resources change.

- **`B07.01.02.F01` — ReviewedPortfolioFit:** Designated service offerings are reviewed against current need and demand.
- **`B07.01.02.F02` — Demand-Need Alignment:** Demand-need is aligned.
- **`B07.01.02.F03` — Redundant Service Recognition:** Redundant service is recognised, with accessible and accurate supporting evidence.
- **`B07.01.02.F04` — Service Gap Discovery:** Service gap can be discovered.
- **`B07.01.02.F05` — Portfolio Decision Transparency:** Portfolio decision is transparent.

#### B07.01.03 — Service Withdrawal and Transition
A service can change or cease without ignoring affected care commitments.

- **`B07.01.03.F01` — ManagedServiceTransition:** Changed or retired offerings have an evidenced transition disposition.
- **`B07.01.03.F02` — Service Withdrawal Notice:** Service withdrawal is made known.
- **`B07.01.03.F03` — Alternative Service Arrangements:** Alternative service arrangements are established through relevant changes or disruptions, with recovery where required.
- **`B07.01.03.F04` — Client Continuity Protection:** Client continuity is protected through relevant changes or disruptions, with recovery where required.
- **`B07.01.03.F05` — Withdrawal Outcome Review:** Withdrawal outcome is reviewed, with accessible and accurate supporting evidence.

#### B07.01.04 — Service Directory Publication Stewardship
Ensure services offered by participating providers are accurately described, current and responsibly published.

- **`B07.01.04.F01` — OwnedServiceDefinition:** Every published service offering has an accountable steward.
- **`B07.01.04.F02` — VerifiedProvisionContext:** Published provider, location and contact associations are supported by current evidence.
- **`B07.01.04.F03` — ChangedServicePublication:** Suspended or withdrawn services receive an attributable published disposition.
- **`B07.01.04.F04` — DirectoryConsumerCorrection:** Incorrect public or partner service information can be reported and corrected.
- **`B07.01.04.F05` — ServiceRecordTraceability:** Users can trace the current offering to the responsible organisation and source evidence.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-SVC-DIR, C-ORG-DIR. *Release horizon:* Unscheduled.

### B07.02 — Service Provider & Network Participation
Establish appropriate provider relationships and service contributions.

#### B07.02.01 — Provider Participation Establishment
Suitable providers can be admitted to a service arrangement under applicable conditions.

- **`B07.02.01.F01` — VerifiedProviderParticipation:** Participating providers meet their required participation conditions.
- **`B07.02.01.F02` — Provider Suitability Verification:** Provider suitability is verified.
- **`B07.02.01.F03` — Participation Terms Clarity:** Participation terms are clear through available and usable support.
- **`B07.02.01.F04` — Service Scope Confirmation:** Service scope is confirmed, distinguishing permitted and nonconforming conditions.
- **`B07.02.01.F05` — Active Provider Status:** Active provider has an explicit status, with accessible and accurate supporting evidence.

#### B07.02.02 — Provider Relationship Management
Responsibilities and service contributions can be maintained across provider relationships.

- **`B07.02.02.F01` — CurrentProviderAgreement:** Active provider arrangements have current, accountable terms.
- **`B07.02.02.F02` — Contact and Accountability Currency:** Contact and accountability is current, with accessible and accurate supporting evidence.
- **`B07.02.02.F03` — Collaboration Issue Resolution:** Collaboration issue is resolved.
- **`B07.02.02.F04` — Provider Performance Feedback:** Provider performance feedback reaches and can be understood by the intended recipient.
- **`B07.02.02.F05` — Relationship Transition Continuity:** Relationship transition is maintained through relevant changes or disruptions, with recovery where required.

#### B07.02.03 — Provider Network Coverage
Necessary service coverage can be assessed across a network of providers.

- **`B07.02.03.F01` — IdentifiedNetworkGaps:** Defined network coverage areas have a current capability and gap assessment.
- **`B07.02.03.F02` — Geographic Provider Availability:** Geographic provider is available.
- **`B07.02.03.F03` — Required Specialty Coverage:** Required specialty is covered.
- **`B07.02.03.F04` — Network Access Equity:** Provider network access can be assessed for equity.
- **`B07.02.03.F05` — Provider Gap Escalation:** Provider gap escalation reaches and can be understood by the intended recipient.

#### B07.02.04 — Provider Directory Relationship Stewardship
Maintain authoritative, interpretable records of participating providers and their service-network relationships.

- **`B07.02.04.F01` — ProviderMembershipAccountability:** A network can identify who authorised and maintains each provider participation record.
- **`B07.02.04.F02` — RelationshipChangeVerified:** Changes in provider–organisation relations are reviewed and effective at the correct time.
- **`B07.02.04.F03` — NoncurrentAssociationVisible:** Former provider relationships are not presented as currently effective.
- **`B07.02.04.F04` — ProviderRelationshipDispute:** Contested provider membership has an accountable review process.
- **`B07.02.04.F05` — DirectoryDistributionScope:** Partner and public disclosures of relationships respect business information-sharing conditions.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PRAC-DIR, C-PRAC-AFF, C-ORG-DIR. *Release horizon:* Unscheduled.

#### B07.02.05 — Provider Affiliation & Contact Currency
Maintain trustworthy current role, affiliation and contact information for participating providers.

- **`B07.02.05.F01` — CurrentProviderContact:** Contact pathways match a verified relevant provider relationship.
- **`B07.02.05.F02` — ProfessionalRoleContext:** Role information identifies the relevant organisation, service and effective period.
- **`B07.02.05.F03` — FormerContactQualified:** Withdrawn or stale contact pathways are not represented as current.
- **`B07.02.05.F04` — CrossOrganisationDisagreement:** Conflicting provider affiliation claims can be reviewed by accountable parties.
- **`B07.02.05.F05` — EvidenceBasedUpdate:** Updates are supported by traceable authoritative or attested evidence.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PRAC-AFF, C-PRAC-DIR. *Release horizon:* Unscheduled.

### B07.03 — Demand, Capacity & Scheduling
Match service demand with available capacity, including urgent demand.

#### B07.03.01 — Service Demand Assessment
Service demand can be estimated and monitored for relevant populations and periods, with demand and unmet needs assessed in the context of where and when the relevant service offering is intended to be provided.

- **`B07.03.01.F01` — VisibleDemandPressure:** Expected service demand conditions are correctly identified in assessments.
- **`B07.03.01.F02` — Demand Signal Completeness:** Demand signal is complete.
- **`B07.03.01.F03` — Demand Forecast Refresh:** Demand forecast is refreshed.
- **`B07.03.01.F04` — Service Offering Availability:** The availability of a service offering is established for its applicable locations and time periods, identifying where and when the service is intended to be provided.
- **`B07.03.01.F05` — Demand Segmentation Clarity:** Demand segmentation is clear.

#### B07.03.02 — Service Capacity Allocation
Service needs can be allocated against actual or anticipated capacity and priority, with capacity adequacy and shortfalls identifiable.

- **`B07.03.02.F01` — Demand Matched to Appropriate Capacity:** Accepted service needs are allocated within applicable priority and capacity conditions.
- **`B07.03.02.F02` — Service Capacity Adequacy:** The capacity available to deliver a service is assessed against its actual or anticipated demand, identifying whether sufficient capacity exists and exposing any shortfall.
- **`B07.03.02.F03` — Capacity-to-Need Matching:** Capacity-to-need is matched.
- **`B07.03.02.F04` — Capacity Availability Visibility:** Capacity availability is visible, with accessible and accurate supporting evidence.
- **`B07.03.02.F05` — Constraint-Aware Allocation:** Constraint-aware is allocated.
- **`B07.03.02.F06` — Capacity Reallocation Readiness:** Capacity reallocation is established.

#### B07.03.03 — Care Service Scheduling
Services, people and places can be coordinated into workable schedules.

- **`B07.03.03.F01` — ViableServiceSchedule:** Scheduled service commitments are feasible for their required resources and participants.
- **`B07.03.03.F02` — Suitable Service Slot Allocation:** Suitable service slot is allocated.
- **`B07.03.03.F03` — Capacity-Aware Scheduling:** Capacity-aware can be scheduled.
- **`B07.03.03.F04` — Change and Cancellation Handling:** Change and cancellation is handled through relevant changes or disruptions, with recovery where required.
- **`B07.03.03.F05` — Schedule Communication Reliability:** Schedule communication is reliable.

#### B07.03.04 — Work & Calendar Responsibility Coordination
Coordinate scheduled attendance and other active work obligations without conflating an appointment with completed clinical work.

- **`B07.03.04.F01` — CentralScheduleConsistency:** Work scheduling uses the established enterprise calendar authority.
- **`B07.03.04.F02` — TaskAppointmentDistinction:** A scheduled event is distinguishable from the responsibility to perform work.
- **`B07.03.04.F03` — UnscheduledObligationVisible:** Clinically important unscheduled tasks remain tracked.
- **`B07.03.04.F04` — ScheduleChangeAccountability:** Changes to timing preserve the relevant work obligation and rationale.
- **`B07.03.04.F05` — HumanConflictResolution:** Competing time commitments can be escalated without silently discarding tasks.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-CT2-ACT, DRP-PR. *Release horizon:* Unscheduled.

### B07.04 — Service Delivery Coordination
Keep service delivery organised across participants and resources.

#### B07.04.01 — Service Delivery Readiness
A service event can be made ready with necessary people, information and resources.

- **`B07.04.01.F01` — ReadyServiceCommencement:** Service deliveries meet the applicable readiness conditions.
- **`B07.04.01.F02` — Required Staff and Resources:** Staff and resources are available when required, with accessible and accurate supporting evidence.
- **`B07.04.01.F03` — Service Opening Readiness:** Service opening is established.
- **`B07.04.01.F04` — Readiness Defect Escalation:** Readiness defect escalation reaches and can be understood by the intended recipient.
- **`B07.04.01.F05` — Readiness Reverification:** Service readiness can be verified again when relevant conditions change.

#### B07.04.02 — Cross-Service Delivery Coordination
Interdependent service contributions can be organised coherently.

- **`B07.04.02.F01` — CoordinatedServiceContributions:** Shared service deliveries avoid preventable coordination gaps.
- **`B07.04.02.F02` — Shared Service Dependencies:** Shared service dependencies reaches and can be understood by the intended recipient.
- **`B07.04.02.F03` — Cross-Service Responsibility Clarity:** Cross-service responsibility is clear.
- **`B07.04.02.F04` — Coordination Exception Escalation:** Coordination exception escalation reaches and can be understood by the intended recipient.
- **`B07.04.02.F05` — Delivery Status Awareness:** Delivery status is made known, with accessible and accurate supporting evidence.

#### B07.04.03 — Service Disruption Management
Unavailable capacity or changed circumstances can be managed with appropriate alternatives.

- **`B07.04.03.F01` — ManagedServiceDisruption:** Service disruptions receive a timely disposition and affected-party communication.
- **`B07.04.03.F02` — Disruption Impact Recognition:** Disruption impact is recognised, with accessible and accurate supporting evidence.
- **`B07.04.03.F03` — Appropriate Service Diversion:** Service diversion is appropriate.
- **`B07.04.03.F04` — Continuity Advice:** Continuity advice is available through relevant changes or disruptions, with recovery where required.
- **`B07.04.03.F05` — Service Restoration Confirmation:** Service restoration is confirmed through relevant changes or disruptions, with recovery where required.

#### B07.04.04 — Digital Service Handoff Accountability
Coordinate business activity across service boundaries while knowing what has been requested, executed, accepted and committed.

*Enterprise contributions: E06.01, E06.07, E08.03; findings: R06, R07.*

- **`B07.04.04.F01` — SubmittedRequestTraceable:** A business request has an identifiable digital work obligation.
- **`B07.04.04.F02` — ExecutionOutcomeKnown:** Work execution results are visible independently of persistence.
- **`B07.04.04.F03` — CommitOutcomeKnown:** Authoritative acceptance result can be established.
- **`B07.04.04.F04` — UncertainHandoffReconciled:** Timeout/unknown states have accountable reconciliation.
- **`B07.04.04.F05` — NoDuplicateObligation:** Retries do not create unaccounted duplicate business demands.

#### B07.04.05 — Human Work Obligation Assignment & Accountability
Enable accountable assignment, acceptance and follow-up of human-delivered work supported by digital coordination.

- **`B07.04.05.F01` — AssignedHumanResponsibility:** The person expected to perform physical work is identifiable and distinct from the managing system.
- **`B07.04.05.F02` — WorkAcceptanceAttribution:** Acceptance is distinguishable from an assignment offer or message receipt.
- **`B07.04.05.F03` — DeferredWorkVisible:** Accepted human work remains tracked while physical activity continues.
- **`B07.04.05.F04` — ReportedCompletionQualified:** A reported completion can be verified, rejected or qualified under the applicable policy.
- **`B07.04.05.F05` — ReassignmentEvidence:** Changes of responsibility preserve prior responsibility and reason.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-CT2-ACT, DRP-PR. *Release horizon:* Unscheduled.

### B07.05 — Service Access Administration
Maintain the administrative arrangements through which care is accessed.

#### B07.05.01 — Service Registration Administration
Administrative details necessary to access a service can be established accurately.

- **`B07.05.01.F01` — CorrectServiceRegistration:** Service registrations retain the required validated details.
- **`B07.05.01.F02` — Correct Registration Details:** Registration details are correct.
- **`B07.05.01.F03` — Duplicate Registration Recognition:** Duplicate registration is recognised.
- **`B07.05.01.F04` — Registration Status Visibility:** Registration status is visible, with accessible and accurate supporting evidence.
- **`B07.05.01.F05` — Registration Correction Access:** Registration correction can be accessed.

#### B07.05.02 — Appointment and Attendance Administration
Service attendance changes, cancellations and non-attendance can be managed.

- **`B07.05.02.F01` — CurrentAttendanceStatus:** Appointments have an accurate and timely attendance disposition.
- **`B07.05.02.F02` — Attendance State Accuracy:** Attendance state is accurate.
- **`B07.05.02.F03` — Missed Attendance Recognition:** Missed attendance is recognised, with accessible and accurate supporting evidence.
- **`B07.05.02.F04` — Appointment Rescheduling Continuity:** Appointment rescheduling is maintained through relevant changes or disruptions, with recovery where required.
- **`B07.05.02.F05` — Attendance Communication:** Attendance communication reaches and can be understood by the intended recipient.

#### B07.05.03 — Administrative Exception Resolution
Incomplete, conflicting or disputed access arrangements can be reviewed and resolved.

- **`B07.05.03.F01` — ResolvedAccessAdministrationIssue:** Access administration exceptions receive a documented resolution or responsible referral.
- **`B07.05.03.F02` — Exception Classification:** Exception is classified.
- **`B07.05.03.F03` — Responsible Case Assignment:** Responsible case is assigned.
- **`B07.05.03.F04` — Evidence-Supported Resolution:** Evidence-supported is resolved, with accessible and accurate supporting evidence.
- **`B07.05.03.F05` — Resolution Communication:** Resolution communication reaches and can be understood by the intended recipient.

### B07.06 — Service Performance & Reporting
Determine and report whether services are being delivered as required.

#### B07.06.01 — Service Performance Visibility
Responsible parties can understand delivery against expected service commitments.

- **`B07.06.01.F01` — Timely, Complete Service Performance Report:** Defined reporting periods have complete, timely, evidence-backed service performance reports.
- **`B07.06.01.F02` — Current Service Performance Evidence:** Current service performance is evidenced, with accessible and accurate supporting evidence.
- **`B07.06.01.F03` — Meaningful Performance Segmentation:** Meaningful performance can be segmented.
- **`B07.06.01.F04` — Performance Indicator Provenance:** Performance indicator retains its provenance, with accessible and accurate supporting evidence.
- **`B07.06.01.F05` — Accessible Performance Reporting:** Accessible performance is reported through available and usable support.

#### B07.06.02 — Service Demand and Outcome Analysis
Service volume and results can be compared to relevant needs and commitments.

- **`B07.06.02.F01` — InterpretableServicePerformance:** Service reports state correct scope, denominators and outcome qualifiers.
- **`B07.06.02.F02` — Demand Outcome Linkage:** Demand outcome is linked.
- **`B07.06.02.F03` — Trend Detection:** Trend is detected.
- **`B07.06.02.F04` — Variance Explanation:** Variance is explained.
- **`B07.06.02.F05` — Decision-Relevant Analysis:** Service demand and outcome analysis informs the relevant service decisions.

#### B07.06.03 — Service Performance Exception Management
Material service deviations can be brought to accountable attention.

- **`B07.06.03.F01` — ActionedServiceException:** Qualifying service performance deviations are reviewed and dispositioned.
- **`B07.06.03.F02` — Performance Deviation Recognition:** Performance deviation is recognised, with accessible and accurate supporting evidence.
- **`B07.06.03.F03` — Accountable Performance Follow-Up:** Accountable performance receives follow-up, with accessible and accurate supporting evidence.
- **`B07.06.03.F04` — Corrective Action Tracking:** Corrective action can be tracked.
- **`B07.06.03.F05` — Effectiveness Reassessment:** Effectiveness are reassessed.

## B08 — Workforce & Professional Practice
An appropriately qualified, available and supported workforce can practise safely and effectively.

### B08.01 — Workforce Supply & Deployment
Obtain and allocate required workforce capacity.

#### B08.01.01 — Workforce Demand Planning
Required workforce capacity and skills can be estimated against service intentions.

- **`B08.01.01.F01` — EvidenceBasedWorkforceNeed:** Designated service plans include an explicit workforce-demand assessment.
- **`B08.01.01.F02` — Skill-Mix Demand Visibility:** Skill-mix demand is visible, with accessible and accurate supporting evidence.
- **`B08.01.01.F03` — Anticipated Staffing Gap:** Anticipated staffing gaps are identifiable.
- **`B08.01.01.F04` — Workforce Contingency Options:** Workforce contingency options are available.
- **`B08.01.01.F05` — Updated Demand Projection:** Updated demand can be projected through relevant changes or disruptions, with recovery where required.

#### B08.01.02 — Clinical Shift Coverage Assurance
Critical service periods can be staffed by appropriately available people.

- **`B08.01.02.F01` — Qualified Workforce Coverage:** Required shifts meet the defined coverage and qualification criteria.
- **`B08.01.02.F02` — CompetenceMatchedAssignment:** Shift assignments meet the relevant skill requirements.
- **`B08.01.02.F03` — Competent Staff Assignment:** Competent staff is assigned.
- **`B08.01.02.F04` — Minimum Coverage Visibility:** Minimum coverage is visible, with accessible and accurate supporting evidence.
- **`B08.01.02.F05` — Unexpected Absence Response:** Unexpected absence response reaches and can be understood by the intended recipient.
- **`B08.01.02.F06` — Shift Coverage Handover:** Shift coverage can be handed over through relevant changes or disruptions, with recovery where required.

#### B08.01.03 — Workforce Reassignment
Staffing can be adapted to urgent demand, absences and service changes.

- **`B08.01.03.F01` — ResponsiveStaffReallocation:** Qualifying workforce coverage gaps are addressed within relevant conditions.
- **`B08.01.03.F02` — Eligibility for Reassignment:** Eligibility for can be reassigned, distinguishing permitted and nonconforming conditions.
- **`B08.01.03.F03` — Rapid Assignment Communication:** Workforce assignments are communicated within the applicable context-defined interval.
- **`B08.01.03.F04` — Continuity of Duties:** Workforce reassignment preserves the continuity of required duties through relevant changes or disruptions, with recovery where required.
- **`B08.01.03.F05` — Reassignment Impact Review:** Reassignment impact is reviewed, with accessible and accurate supporting evidence.

#### B08.01.04 — Concurrent Practitioner Work Visibility
Enable a practitioner or authorised work coordinator to understand concurrent assignments, priorities, offers and scheduling obligations.

- **`B08.01.04.F01` — CurrentWorkView:** Outstanding assigned and offered work is accurately represented.
- **`B08.01.04.F02` — SchedulingConflictKnown:** Conflicting commitments can be identified for action.
- **`B08.01.04.F03` — PriorityResponsibilityClear:** Higher-priority tasks and overdue work are distinguishable from routine obligations.
- **`B08.01.04.F04` — HumanWorkNotAssumedAvailable:** Task assignment does not imply instantaneous physical availability.
- **`B08.01.04.F05` — MultipleDeferredTasksAllowed:** Concurrent outstanding responsibilities remain individually accountable.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-CT2-ACT, DRP-PR. *Release horizon:* Unscheduled.

### B08.02 — Professional Qualification & Credential Assurance
Establish eligibility, qualifications and conditions for practice.

#### B08.02.01 — Professional Eligibility Verification
Current professional registration and eligibility can be established where required.

- **`B08.02.01.F01` — Verified Current Eligibility to Practise:** Practitioners have verified applicable current professional eligibility.
- **`B08.02.01.F02` — Primary Qualification Evidence:** Primary qualification is evidenced.
- **`B08.02.01.F03` — Registration Currency:** Registration is current through relevant changes or disruptions, with recovery where required.
- **`B08.02.01.F04` — Role-Appropriate Eligibility:** Professional eligibility is appropriate to the intended role, distinguishing eligible and ineligible practice.
- **`B08.02.01.F05` — Expired Eligibility Alert:** Expired professional eligibility produces an alert, distinguishing permitted and nonconforming conditions.

#### B08.02.02 — Credential Evidence Maintenance
Qualifications and professional evidence can remain current, attributable and reviewable.

- **`B08.02.02.F01` — CurrentProfessionalCredentialEvidence:** Active professional arrangements have current supporting credential evidence.
- **`B08.02.02.F02` — Authoritative Credential Source:** Authoritative credential has an identifiable source, distinguishing permitted and nonconforming conditions.
- **`B08.02.02.F03` — Credential Expiry Awareness:** Credential expiry is made known, distinguishing permitted and nonconforming conditions.
- **`B08.02.02.F04` — Credential Renewal Evidence:** Credential renewal is evidenced, distinguishing permitted and nonconforming conditions.
- **`B08.02.02.F05` — Credential Audit Trail:** Credential evidence retains an audit trail, distinguishing permitted and nonconforming conditions.

#### B08.02.03 — Credential Exception Management
Expired, missing or disputed qualification evidence can be safely investigated.

- **`B08.02.03.F01` — DispositionedCredentialException:** Material credential exceptions are actioned before incompatible practice.
- **`B08.02.03.F02` — Credential Exception Classification:** Credential exception is classified, distinguishing permitted and nonconforming conditions.
- **`B08.02.03.F03` — Restricted Practice Enforcement:** Restricted practice is enforced.
- **`B08.02.03.F04` — Exception Resolution Responsibility:** Exception resolution has an identifiable responsible party.
- **`B08.02.03.F05` — Exception Closure Evidence:** Exception closure is evidenced, with accessible and accurate supporting evidence.

### B08.03 — Scope of Practice & Clinical Privileges
Determine what a practitioner may undertake in the service context.

#### B08.03.01 — Service-Specific Clinical Privileging
Practitioner permissions can be established for an actual service setting and competence.

- **`B08.03.01.F01` — VerifiedClinicalPrivileges:** Relevant practice activities remain within current authorised clinical privileges.
- **`B08.03.01.F02` — Facility-Specific Privilege Scope:** Facility-specific privilege has an explicit scope, distinguishing permitted and nonconforming conditions.
- **`B08.03.01.F03` — Approved Privilege Currency:** Approved privilege is current, distinguishing permitted and nonconforming conditions.
- **`B08.03.01.F04` — Restricted Activity Recognition:** Restricted activity is recognised, with accessible and accurate supporting evidence.
- **`B08.03.01.F05` — Privilege Evidence Access:** Privilege evidence can be accessed, distinguishing permitted and nonconforming conditions.

#### B08.03.02 — Scope Boundary Communication
Practitioners and services can understand applicable clinical responsibility limits.

- **`B08.03.02.F01` — KnownPracticeBoundaries:** Practitioners can access their applicable practice scope limits.
- **`B08.03.02.F02` — Scope Restriction Visibility:** Scope restriction is visible, distinguishing permitted and nonconforming conditions.
- **`B08.03.02.F03` — Authorised Practice Clarification:** Authorised practice can be clarified, distinguishing permitted and nonconforming conditions.
- **`B08.03.02.F04` — Updated Scope Notification:** Updated scope is notified, distinguishing permitted and nonconforming conditions.
- **`B08.03.02.F05` — Scope Misuse Escalation:** Scope misuse is escalated, including in adverse conditions.

#### B08.03.03 — Privilege Change Control
Scope or privileges can be reviewed when competence, service context or authority changes.

- **`B08.03.03.F01` — CurrentPrivilegeStatus:** Privilege changes are reflected before affected practice takes place.
- **`B08.03.03.F02` — Change Approval Accountability:** Change approval has an accountable party, distinguishing permitted and nonconforming conditions.
- **`B08.03.03.F03` — Changed Privilege Effective Date:** Privilege changes identify their applicable effective date, distinguishing permitted and nonconforming conditions.
- **`B08.03.03.F04` — Affected Service Notification:** Affected service notification reaches and can be understood by the intended recipient.
- **`B08.03.03.F05` — Revoked Privilege Enforcement:** Revoked clinical privileges cannot be exercised as current authority, distinguishing permitted and nonconforming conditions.

#### B08.03.04 — Effective Clinical Privilege Evidence
Ensure professional privileges for a particular care context are valid and reviewable independently of technical authentication.

*Enterprise contributions: E05.02, E05.06, E07.05; findings: R22.*

- **`B08.03.04.F01` — PrivilegeForServiceValidated:** Actual service and setting privileges are checked.
- **`B08.03.04.F02` — TechnicalLoginNotEnough:** Successful login alone does not imply clinical privilege.
- **`B08.03.04.F03` — PrivilegeExpiryHonoured:** Expired/suspended authority cannot be used as current evidence.
- **`B08.03.04.F04` — ExceptionalAuthorisationLogged:** Emergency or exceptional authority is separately accountable.
- **`B08.03.04.F05` — PrivilegeSourceTraceable:** The organisation and acceptance basis of privileges can be reviewed.

#### B08.03.05 — Clinical Privilege Currency Stewardship
Ensure provider directory consumers can distinguish current clinical privileges and practice restrictions from historical or merely claimed eligibility.

- **`B08.03.05.F01` — PrivilegeEffectivePeriod:** Clinical privileges are associated with the correct effective period and service setting.
- **`B08.03.05.F02` — RestrictionPromptlyVisible:** A changed restriction is reflected to authorised downstream consumers with accountable currency.
- **`B08.03.05.F03` — UnverifiedPrivilegeQualified:** Unverified privilege assertions cannot be used as established clinical authority.
- **`B08.03.05.F04` — RolePrivilegeSeparation:** A directory role is not assumed to confer every corresponding clinical privilege.
- **`B08.03.05.F05` — PrivilegeReviewTraceability:** Privilege review and change decisions are attributable.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PRAC-VERIFY, C-PRAC-AFF. *Release horizon:* Unscheduled.

### B08.04 — Professional Development & Competence
Sustain necessary skills and professional readiness.

#### B08.04.01 — Competence Development Planning
Learning and professional development needs can be established and prioritised.

- **`B08.04.01.F01` — CurrentDevelopmentIntent:** Designated practitioners have current development needs and plans.
- **`B08.04.01.F02` — Skill Gap Recognition:** Skill gap is recognised, with accessible and accurate supporting evidence.
- **`B08.04.01.F03` — Agreed Development Goal:** Professional development goals are agreed.
- **`B08.04.01.F04` — Protected Learning Opportunity:** Protected learning opportunity is supported through available and usable support.
- **`B08.04.01.F05` — Development Plan Review:** Development plan is reviewed, with accessible and accurate supporting evidence.

#### B08.04.02 — Competence Maintenance
Required professional capabilities can be refreshed and evaluated over time.

- **`B08.04.02.F01` — DemonstratedCurrentCompetence:** Applicable competence requirements have current supporting evidence.
- **`B08.04.02.F02` — Competence Evidence Currency:** Competence evidence is current, with accessible and accurate supporting evidence.
- **`B08.04.02.F03` — Periodic Skill Assessment:** Periodic skill is assessed.
- **`B08.04.02.F04` — Practice Change Learning:** Learning relevant to practice change can be applied through relevant changes or disruptions, with recovery where required.
- **`B08.04.02.F05` — Competence Concern Escalation:** Competence concern escalation reaches and can be understood by the intended recipient.

#### B08.04.03 — Learning Contribution Recognition
Relevant professional learning and experience can be recorded and assessed.

- **`B08.04.03.F01` — VerifiedLearningContribution:** Learning records meet the accepted evidence requirements for recognition.
- **`B08.04.03.F02` — Learning Activity Evidence:** Learning activity is evidenced, with accessible and accurate supporting evidence.
- **`B08.04.03.F03` — Relevant Learning Credit:** Learning credit is relevant.
- **`B08.04.03.F04` — Learning Outcome Application:** Learning outcome can be applied.
- **`B08.04.03.F05` — Learning Contribution Feedback:** Learning contribution feedback reaches and can be understood by the intended recipient.

### B08.05 — Clinical Supervision & Practice Support
Provide appropriate supervision, consultation and professional support.

#### B08.05.01 — Clinical Supervision Access
Professionals requiring supervision can obtain appropriately qualified support.

- **`B08.05.01.F01` — AvailableRequiredSupervision:** Qualifying practice has appropriate supervision arrangements.
- **`B08.05.01.F02` — Suitable Supervisor Availability:** Suitable supervisor is available.
- **`B08.05.01.F03` — Supervision Escalation Path:** Supervision escalation path reaches and can be understood by the intended recipient.
- **`B08.05.01.F04` — Protected Supervision Time:** Required supervision time remains protected, including in adverse conditions.
- **`B08.05.01.F05` — Supervision Outcome Record:** Supervision outcome is recorded, with accessible and accurate supporting evidence.

#### B08.05.02 — Professional Consultation Support
Practitioners can request and receive relevant clinical consultation.

- **`B08.05.02.F01` — TimelyProfessionalAdvice:** Valid consultation needs receive an accountable professional response within relevant conditions.
- **`B08.05.02.F02` — Appropriate Expert Access:** Appropriate expert can be accessed.
- **`B08.05.02.F03` — Consultation Request Context:** Consultation request has its relevant context.
- **`B08.05.02.F04` — Advice Receipt Confirmation:** Advice receipt confirmation reaches and can be understood by the intended recipient.
- **`B08.05.02.F05` — Consultation Outcome Trace:** Consultation outcome is traceable, with accessible and accurate supporting evidence.

#### B08.05.03 — Practice Concern Escalation
Clinical practice concerns can be raised for review without losing accountability.

- **`B08.05.03.F01` — ReviewablePracticeConcern:** Qualifying practice concerns receive documented attention and disposition.
- **`B08.05.03.F02` — Concern Reporting Safety:** Practice concerns can be reported safely, including in adverse conditions.
- **`B08.05.03.F03` — Timely Review Assignment:** Practice concern reviews are assigned within the applicable context-defined interval.
- **`B08.05.03.F04` — Proportionate Practice Response:** Proportionate practice response reaches and can be understood by the intended recipient.
- **`B08.05.03.F05` — Reporter Follow-Up:** Reporter receives follow-up, with accessible and accurate supporting evidence.

### B08.06 — Workforce Health, Wellbeing & Readiness
Support workforce health and ability to meet the service mission.

#### B08.06.01 — Workforce Health Support
Staff can access appropriate support to maintain occupational and personal wellbeing.

- **`B08.06.01.F01` — AccessibleWorkforceSupport:** Workforce support requests receive an appropriate response.
- **`B08.06.01.F02` — Confidential Support Access:** Confidential support can be accessed, including in adverse conditions.
- **`B08.06.01.F03` — Early Wellbeing Need Recognition:** Workforce wellbeing needs are recognised within the applicable context-defined interval.
- **`B08.06.01.F04` — Suitable Support Referral:** Suitable support can be referred.
- **`B08.06.01.F05` — Continued Support Availability:** Continued support is available through relevant changes or disruptions, with recovery where required.

#### B08.06.02 — Workforce Fitness and Readiness Assessment
The enterprise can determine readiness for work or deployment under applicable conditions.

- **`B08.06.02.F01` — CurrentReadinessAssessment:** Designated assignments have a valid workforce readiness status.
- **`B08.06.02.F02` — Duty-Relevant Readiness Criteria:** Duty-relevant readiness criteria are explicit.
- **`B08.06.02.F03` — Readiness Assessment Currency:** Readiness assessment is current through relevant changes or disruptions, with recovery where required.
- **`B08.06.02.F04` — Restriction Visibility:** Restriction is visible, distinguishing permitted and nonconforming conditions.
- **`B08.06.02.F05` — Reassessment after Change:** Reassessment after can be changed through relevant changes or disruptions, with recovery where required.

#### B08.06.03 — Workforce Recovery and Return Support
Safe arrangements can support personnel returning after injury, illness or absence.

- **`B08.06.03.F01` — SupportedReturnToWork:** Relevant returns to work have agreed requirements and progress review.
- **`B08.06.03.F02` — Individual Return Plan:** Individual return has a plan.
- **`B08.06.03.F03` — Safe Duty Adjustment:** Safe duty can be adjusted, including in adverse conditions.
- **`B08.06.03.F04` — Rehabilitation Progress Review:** Rehabilitation progress are reviewed, with accessible and accurate supporting evidence.
- **`B08.06.03.F05` — Supported Return Confirmation:** Supported return confirmation reaches and can be understood by the intended recipient.

## B09 — Clinical Facilities & Supply
Facilities, equipment, goods, logistics and transport can be ready where healthcare requires them.

### B09.01 — Clinical Facility Availability
Provide suitable places and physical conditions for care.

#### B09.01.01 — Appropriate Care Space Availability
Suitable clinical space can be allocated for intended services.

- **`B09.01.01.F01` — AvailableSuitableCareSpace:** Planned service events have an appropriately equipped and accessible care space.
- **`B09.01.01.F02` — Suitable Space Identification:** Suitable space is identified.
- **`B09.01.01.F03` — Accessibility and Infection Controls:** Accessibility and infection controls is supported through available and usable support.
- **`B09.01.01.F04` — Capacity Status Currency:** Capacity status is current, with accessible and accurate supporting evidence.
- **`B09.01.01.F05` — Alternative Space Availability:** Alternative space is available through relevant changes or disruptions, with recovery where required.

#### B09.01.02 — Facility Condition Readiness
Care environments can meet applicable safety and operational conditions.

- **`B09.01.02.F01` — VerifiedFacilityReadiness:** Care spaces meet their declared use requirements.
- **`B09.01.02.F02` — Safety Condition Verification:** Safety condition is verified, including in adverse conditions.
- **`B09.01.02.F03` — Essential Utility Availability:** Essential utility is available.
- **`B09.01.02.F04` — Facility Defect Escalation:** Facility defect escalation reaches and can be understood by the intended recipient.
- **`B09.01.02.F05` — Changed Condition Reassessment:** Changed condition is reassessed, distinguishing permitted and nonconforming conditions.

#### B09.01.03 — Facility Disruption Adaptation
Care environments can be reassigned or alternatives arranged when spaces become unavailable.

- **`B09.01.03.F01` — ManagedSpaceDisruption:** Care-space disruptions receive a safe disposition.
- **`B09.01.03.F02` — Impacted Care Identification:** Impacted care is identified.
- **`B09.01.03.F03` — Alternative Location Allocation:** Alternative location is allocated through relevant changes or disruptions, with recovery where required.
- **`B09.01.03.F04` — Safe Relocation Communication:** Safe relocation is communicated, including in adverse conditions.
- **`B09.01.03.F05` — Restoration of Facility Use:** Restoration of facility can be used through relevant changes or disruptions, with recovery where required.

### B09.02 — Clinical Equipment Readiness
Make necessary equipment available and safe to use.

#### B09.02.01 — Critical Equipment Availability
Necessary clinical equipment can be present and usable at point of need.

- **`B09.02.01.F01` — Equipment Available When Needed:** Service events have their required ready equipment available when needed.
- **`B09.02.01.F02` — Required Equipment Inventory:** Equipment inventory is available when required.
- **`B09.02.01.F03` — Operational Equipment State:** Operational equipment has an explicit state.
- **`B09.02.01.F04` — Backup Device Availability:** Backup device is available through relevant changes or disruptions, with recovery where required.
- **`B09.02.01.F05` — Equipment Request Fulfilment:** Equipment request can be fulfilled.

#### B09.02.02 — Equipment Safety and Maintenance
Equipment can be inspected, maintained and kept within appropriate use conditions.

- **`B09.02.02.F01` — VerifiedEquipmentFitness:** Equipment use has current required fitness checks.
- **`B09.02.02.F02` — Maintenance Compliance:** Maintenance complies with the applicable requirements, distinguishing permitted and nonconforming conditions.
- **`B09.02.02.F03` — Unsafe Equipment Quarantine:** Unsafe equipment can be quarantined, including in adverse conditions.
- **`B09.02.02.F04` — Maintenance Evidence Availability:** Maintenance evidence is available, with accessible and accurate supporting evidence.
- **`B09.02.02.F05` — Post-Maintenance Release:** Post-maintenance is released under the applicable conditions.

#### B09.02.03 — Equipment Failure Replacement
Unavailable or unsafe equipment can be isolated and substituted appropriately.

- **`B09.02.03.F01` — ResponsiveEquipmentSubstitution:** Qualifying equipment failures receive an acceptable alternate arrangement.
- **`B09.02.03.F02` — Failure Recognition:** Failure is recognised, with accessible and accurate supporting evidence.
- **`B09.02.03.F03` — Safe Replacement Assignment:** Safe replacement is assigned, including in adverse conditions.
- **`B09.02.03.F04` — Replacement Timeliness:** Replacement meets the applicable timing conditions through relevant changes or disruptions, with recovery where required.
- **`B09.02.03.F05` — Replacement Traceability:** Replacement is traceable, with accessible and accurate supporting evidence.

### B09.03 — Medicines & Therapeutic Goods Supply
Maintain appropriate supply, availability and integrity of regulated goods.

#### B09.03.01 — Therapeutic Supply Availability
Regulated medicines and therapeutic goods can be made available for permitted service needs.

- **`B09.03.01.F01` — RequiredTherapeuticAvailability:** Legitimate therapeutic needs are met without avoidable shortage.
- **`B09.03.01.F02` — Required Stock Sufficiency:** Required stock is sufficient.
- **`B09.03.01.F03` — Forecast Supply Shortage:** Anticipated therapeutic supply shortages can be identified.
- **`B09.03.01.F04` — Therapeutic Substitute Access:** Therapeutic substitute can be accessed.
- **`B09.03.01.F05` — Stock Replenishment Visibility:** Stock replenishment is visible, with accessible and accurate supporting evidence.

#### B09.03.02 — Therapeutic Goods Traceability
Regulated products can be traced through necessary custody and use stages.

- **`B09.03.02.F01` — Traceable Product Use:** Therapeutic goods retain sufficient product, lot and destination lineage.
- **`B09.03.02.F02` — Batch and Lot Identification:** Batch and lot is identified.
- **`B09.03.02.F03` — Custody Chain Visibility:** Custody chain is visible, with accessible and accurate supporting evidence.
- **`B09.03.02.F04` — Expiry and Recall Identification:** Expiry and recall is identified.
- **`B09.03.02.F05` — Dispense-to-Batch Linkage:** Dispense-to-batch is linked.

#### B09.03.03 — Therapeutic Recall Response
Affected goods can be identified and appropriately quarantined or withdrawn.

- **`B09.03.03.F01` — EffectiveRecallDisposition:** Recalled items receive timely, attributable action.
- **`B09.03.03.F02` — Recall Scope Recognition:** Recall scope is recognised, distinguishing permitted and nonconforming conditions.
- **`B09.03.03.F03` — Affected Stock Quarantine:** Affected stock can be quarantined, including in adverse conditions.
- **`B09.03.03.F04` — Exposure Notification:** Exposure notification reaches and can be understood by the intended recipient.
- **`B09.03.03.F05` — Recall Clearance Evidence:** Recall clearance is evidenced, with accessible and accurate supporting evidence.

### B09.04 — Consumables & Clinical Logistics
Acquire, store, position and replenish materials used in care.

#### B09.04.01 — Clinical Stock Replenishment
Consumable supplies can be replenished where the care service needs them.

- **`B09.04.01.F01` — AdequateClinicalStock:** Defined essential clinical stock is available at the required care points.
- **`B09.04.01.F02` — Reorder Threshold Visibility:** Reorder threshold is visible, with accessible and accurate supporting evidence.
- **`B09.04.01.F03` — Appropriate Replenishment Quantity:** Replenishment quantity is appropriate.
- **`B09.04.01.F04` — Stock Arrival Confirmation:** Stock arrival confirmation reaches and can be understood by the intended recipient.
- **`B09.04.01.F05` — Replenishment Exception Handling:** Replenishment exception is handled.

#### B09.04.02 — Clinical Material Distribution
Materials can be moved safely and predictably between required locations.

- **`B09.04.02.F01` — CompleteMaterialDelivery:** Material deliveries reach the intended destination intact and on time.
- **`B09.04.02.F02` — Delivery Destination Accuracy:** Delivery destination is accurate.
- **`B09.04.02.F03` — Time-Critical Material Arrival:** Time-critical clinical materials arrive within the applicable context-defined interval.
- **`B09.04.02.F04` — Distribution Status Tracking:** Distribution status can be tracked, with accessible and accurate supporting evidence.
- **`B09.04.02.F05` — Lost Material Resolution:** Lost material is resolved.

#### B09.04.03 — Stock Expiry and Waste Control
Stock can be rotated and disposed of under appropriate controls.

- **`B09.04.03.F01` — ControlledStockLoss:** Expired and waste stock is handled under the applicable requirements.
- **`B09.04.03.F02` — Expiry Recognition:** Expiry is recognised, with accessible and accurate supporting evidence.
- **`B09.04.03.F03` — Stock Rotation Application:** Stock rotation can be applied.
- **`B09.04.03.F04` — Waste Quantity Accountability:** Waste quantity has an accountable party, with accessible and accurate supporting evidence.
- **`B09.04.03.F05` — Waste Disposal Conformance:** Waste disposal conforms to the applicable requirements, distinguishing permitted and nonconforming conditions.

### B09.05 — Patient & Clinical Transport
Move patients and necessary clinical resources where required.

#### B09.05.01 — Safe Patient Transport Arrangement
Suitable transport can be arranged for a person's clinical condition and access needs.

- **`B09.05.01.F01` — AppropriatelySupportedTransport:** Clinical transport meets the required clinical and accessibility conditions.
- **`B09.05.01.F02` — Clinical Transport Suitability:** Clinical transport is suitable.
- **`B09.05.01.F03` — Patient Mobility Accommodation:** Patient mobility can be accommodated.
- **`B09.05.01.F04` — Escort and Monitoring Readiness:** Escort and monitoring is established, with accessible and accurate supporting evidence.
- **`B09.05.01.F05` — Transport Handover:** Transport can be handed over through relevant changes or disruptions, with recovery where required.

#### B09.05.02 — Interfacility Transfer Transport
Transport and clinical handover needs can be aligned for a transfer.

- **`B09.05.02.F01` — CoordinatedTransportHandover:** Transfers have verified transport and receiving-readiness coordination.
- **`B09.05.02.F02` — Receiving Facility Readiness:** Receiving facility is established.
- **`B09.05.02.F03` — Transport Coordination Timing:** Transport coordination has its timing established.
- **`B09.05.02.F04` — Cross-Facility Handover:** Cross-facility can be handed over through relevant changes or disruptions, with recovery where required.
- **`B09.05.02.F05` — Contingency Transport Access:** Contingency transport can be accessed.

#### B09.05.03 — Clinical Specimen and Resource Transport
Time-sensitive diagnostic materials and clinical resources can reach intended destinations safely.

- **`B09.05.03.F01` — ValidClinicalResourceTransit:** Clinical resource consignments meet their time and condition requirements.
- **`B09.05.03.F02` — Specimen Integrity During Transit:** Specimens retain their required integrity during transit.
- **`B09.05.03.F03` — Temperature and Handling Control:** Temperature and handling is controlled.
- **`B09.05.03.F04` — Collection-to-Receipt Trace:** Collection-to-receipt is traceable, with accessible and accurate supporting evidence.
- **`B09.05.03.F05` — Lost Specimen Escalation:** Lost specimen escalation reaches and can be understood by the intended recipient.

### B09.06 — Resource Safety & Availability Assurance
Assess and address gaps affecting resource readiness.

#### B09.06.01 — Resource Readiness Assessment
Services can assess whether the required material resources are ready for use.

- **`B09.06.01.F01` — VerifiedResourceReadiness:** Designated services have a current resource completeness assessment.
- **`B09.06.01.F02` — Required Resource Sufficiency:** Required resource is sufficient.
- **`B09.06.01.F03` — Fitness-for-Use Evidence:** Fitness-for-use is evidenced, with accessible and accurate supporting evidence.
- **`B09.06.01.F04` — Readiness Defect Visibility:** Readiness defect is visible, with accessible and accurate supporting evidence.
- **`B09.06.01.F05` — Readiness Review Currency:** Readiness review is current, with accessible and accurate supporting evidence.

#### B09.06.02 — Resource Hazard Identification
Hazards associated with facilities, supplies or equipment can be recognised and addressed.

- **`B09.06.02.F01` — ActionedResourceHazard:** Material resource hazards receive accountable action.
- **`B09.06.02.F02` — Relevant Hazard Detection:** Relevant hazard is detected, including in adverse conditions.
- **`B09.06.02.F03` — Risk Communication:** Risk is communicated, including in adverse conditions.
- **`B09.06.02.F04` — Hazard Control Assignment:** Hazard control is assigned, including in adverse conditions.
- **`B09.06.02.F05` — Hazard Reassessment:** Hazard is reassessed, including in adverse conditions.

#### B09.06.03 — Critical Resource Contingency
Critical shortages or failures can be managed through appropriate alternatives.

- **`B09.06.03.F01` — EffectiveResourceContingency:** Critical resource shortages have a viable contingency disposition.
- **`B09.06.03.F02` — Alternate Resource Identification:** Alternate resource is identified.
- **`B09.06.03.F03` — Contingency Activation Timing:** Contingency activation has its timing established.
- **`B09.06.03.F04` — Continuity of Essential Service:** Essential services can continue under the applicable contingency conditions through relevant changes or disruptions, with recovery where required.
- **`B09.06.03.F05` — Contingency Recovery Review:** Contingency recovery is reviewed, with accessible and accurate supporting evidence.

## B10 — Health Information Stewardship
The healthcare enterprise can meet its responsibilities for health records, knowledge, information use and accountability.

### B10.01 — Health Record Stewardship
Establish and maintain clinically and legally meaningful records.

#### B10.01.01 — Clinically Meaningful Record Completion
The organisation can ensure required records of care are coherent and complete.

- **`B10.01.01.F01` — Required Record Content Completed:** Care records have their applicable content completed or qualified.
- **`B10.01.01.F02` — Relevant Record Content:** Relevant record content is available, with accessible and accurate supporting evidence.
- **`B10.01.01.F03` — Timely Record Finalisation:** Clinical records are finalised within the applicable context-defined interval.
- **`B10.01.01.F04` — Record Authorship Clarity:** Record authorship is clear, with accessible and accurate supporting evidence.
- **`B10.01.01.F05` — Correction of Record Omissions:** Clinical record omissions can be corrected.

#### B10.01.02 — Record Identity and Association
Records can be attributed to the correct person, service and encounter or care context.

- **`B10.01.02.F01` — CorrectRecordAssociation:** Clinical records are associated with the intended subject and context.
- **`B10.01.02.F02` — Correct Person Association:** Correct person is associated.
- **`B10.01.02.F03` — Duplicate Record Recognition:** Duplicate record is recognised.
- **`B10.01.02.F04` — Cross-Record Link Trace:** Cross-record link is traceable, with accessible and accurate supporting evidence.
- **`B10.01.02.F05` — Misassociation Correction:** Misassociation can be corrected.

#### B10.01.03 — Record Reconciliation and Continuity
Multiple contributions can form a useful longitudinal record without hiding differences.

- **`B10.01.03.F01` — CoherentHealthRecord:** Material contributions to longitudinal records are correctly contextualised.
- **`B10.01.03.F02` — Cross-Source Record Comparison:** Cross-source record can be compared, with accessible and accurate supporting evidence.
- **`B10.01.03.F03` — Resolved Record Disagreement:** Resolved record disagreements are identifiable, with accessible and accurate supporting evidence.
- **`B10.01.03.F04` — Historical Record Retention:** Historical record is retained, with accessible and accurate supporting evidence.
- **`B10.01.03.F05` — Continuity across Encounters:** Clinical record context is preserved across encounters through relevant changes or disruptions, with recovery where required.

#### B10.01.04 — Person Identity & Record Association Stewardship
Account for person identities and the evidence linking records to individuals across health information sources.

- **`B10.01.04.F01` — IdentitySourceAccountability:** Person identifiers and the source responsible for each assertion remain distinguishable.
- **`B10.01.04.F02` — UncertainSubjectAssociation:** An uncertain person–record match is qualified before clinical use.
- **`B10.01.04.F03` — IdentityChangeAwareness:** Record stewards can identify relevant person identity changes.
- **`B10.01.04.F04` — WrongPersonRiskHandling:** Potential wrong-person clinical record associations have an accountable escalation route.
- **`B10.01.04.F05` — CrossSourceIdentityHistory:** Past and current record-to-person associations can be reconstructed.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PERSON-CORR, C-PERSON-RESOLVE. *Release horizon:* Unscheduled.

#### B10.01.05 — Longitudinal Record Assembly Stewardship
Ensure cross-episode health record contributions are appropriately associated, qualified and discoverable without turning summaries into invented clinical truth.

- **`B10.01.05.F01` — CrossEpisodeContentAssociation:** A longitudinal record includes appropriately related episodes with attributable sources.
- **`B10.01.05.F02` — KnownRecordGapsReported:** Missing/unavailable source material is not misrepresented as evidence of absence.
- **`B10.01.05.F03` — AggregateNotAuthoritativeOverwrite:** Summaries retain the identity and authority of contributing source records.
- **`B10.01.05.F04` — ClinicalRecordVersionContext:** Historic and amended clinical contributions remain interpretable.
- **`B10.01.05.F05` — ConflictingRecordsNotHidden:** Materially contradictory source statements are qualified rather than silently erased.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-LHR-QUERY, C-FED-QUERY. *Release horizon:* Unscheduled.

### B10.02 — Information Completeness & Provenance Accountability
Know whether the record has the evidence, source and history necessary for use.

#### B10.02.01 — Information Sufficiency Appraisal
Information users can recognise whether available evidence is adequate for the intended business decision.

- **`B10.02.01.F01` — QualifiedInformationSufficiency:** Known information incompleteness is explicit at the point of decision.
- **`B10.02.01.F02` — Required Data Coverage:** Required data is covered.
- **`B10.02.01.F03` — Missing Information Visibility:** Missing information is visible, with accessible and accurate supporting evidence.
- **`B10.02.01.F04` — Qualified Information Use:** Qualified information can be used.
- **`B10.02.01.F05` — Reassessment after Data Arrival:** Reassessment after data arrives under the applicable conditions.

#### B10.02.02 — Clinical Information Source Accountability
The enterprise can establish where information came from and who contributed it.

- **`B10.02.02.F01` — AttributableBusinessInformation:** Business information retains retrievable required origin context.
- **`B10.02.02.F02` — Identifiable Information Origin:** The origin of business information is identifiable.
- **`B10.02.02.F03` — Source Trust Qualification:** Source trust is qualified.
- **`B10.02.02.F04` — Transformation History Access:** Transformation history can be accessed, with accessible and accurate supporting evidence.
- **`B10.02.02.F05` — Source Challenge Procedure:** Source challenge has an established procedure, with accessible and accurate supporting evidence.

#### B10.02.03 — Information History Review
Relevant updates or interpretations can be reviewed without silently replacing prior context.

- **`B10.02.03.F01` — ReviewableInformationHistory:** Changed records retain sufficient traceable information history.
- **`B10.02.03.F02` — Prior Version Access:** Prior version can be accessed.
- **`B10.02.03.F03` — Consequential Change Recognition:** Consequential change is recognised, with accessible and accurate supporting evidence.
- **`B10.02.03.F04` — Change Contributor Identification:** Change contributor is identified.
- **`B10.02.03.F05` — Historic Context Explanation:** Historic context is explained.

#### B10.02.04 — Provenance Evidence Stewardship
Ensure that evidence about information origin, transformations and accountability remains identifiable, interpretable and protected.

*Enterprise contributions: E08.07, E08.08, E01.03; findings: R03, R04.*

- **`B10.02.04.F01` — ProvenanceChronologyFaithful:** Evidence chronology matches represented event order.
- **`B10.02.04.F02` — SourceIdentityRetained:** Every governed derivative has attributable source identity where required.
- **`B10.02.04.F03` — IncompleteProvenanceQualified:** Missing evidence does not silently imply full lineage.
- **`B10.02.04.F04` — ProvenanceCorrectionAdditive:** Qualifications are recorded without altering immutable history.
- **`B10.02.04.F05` — ReviewAccessControlled:** Only authorised reviewers can obtain protected evidence.

#### B10.02.05 — Clinical Source & Temporal Qualification Stewardship
Help health information users assess the source, time and interpretation limitations of assembled clinical evidence.

- **`B10.02.05.F01` — OccurrenceVsRecordingClarified:** Clinically relevant chronology reflects event occurrence separately from documentation time.
- **`B10.02.05.F02` — SourceConfidenceVisible:** Information provenance and known limitations can be reviewed by authorised clinicians.
- **`B10.02.05.F03` — CorrectionLineageKnown:** An amended clinical statement remains linked to the prior statement.
- **`B10.02.05.F04` — ContradictionReviewPath:** Inconsistent source information has a recognisable clinical review route.
- **`B10.02.05.F05` — QualifiedSummaryUse:** Summaries identify when source coverage or identity association remains uncertain.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-LHR-QUERY, DRP-IU. *Release horizon:* Unscheduled.

### B10.03 — Appropriate Information Access & Sharing
Make information available to authorised participants for valid business purposes.

#### B10.03.01 — Relevant Care Information Sharing
Necessary health information can be made available to permitted care recipients.

- **`B10.03.01.F01` — Information Available to Authorised Receiving Care:** Authorised necessary information is available at required care transitions.
- **`B10.03.01.F02` — DisclosedInformationScope:** Care information exchanges include necessary information without unnecessary disclosure.
- **`B10.03.01.F03` — Recipient Relevance:** Recipient relevance reaches and can be understood by the intended recipient.
- **`B10.03.01.F04` — Clinical Context Preservation:** Clinical context is preserved through relevant changes or disruptions, with recovery where required.
- **`B10.03.01.F05` — Sharing Timeliness:** Sharing meets the applicable timing conditions.
- **`B10.03.01.F06` — Sharing Receipt Awareness:** Sharing receipt awareness reaches and can be understood by the intended recipient.

#### B10.03.02 — Purpose-Appropriate Information Disclosure
Information can be shared for legitimate business purposes at an appropriate scope.

- **`B10.03.02.F01` — CorrectBusinessDisclosure:** Business disclosures match the applicable purpose and scope.
- **`B10.03.02.F02` — Legitimate Disclosure Purpose:** Legitimate disclosure has an explicit purpose, with accessible and accurate supporting evidence.
- **`B10.03.02.F03` — Minimum Necessary Information:** Minimum necessary information is available.
- **`B10.03.02.F04` — Recipient Authority Check:** Recipient authority is checked, distinguishing permitted and nonconforming conditions.
- **`B10.03.02.F05` — Disclosure Accountability:** Disclosure has an accountable party, with accessible and accurate supporting evidence.

#### B10.03.03 — Information Sharing Exception Handling
Barriers or disputes concerning information sharing can be recognised and resolved or escalated.

- **`B10.03.03.F01` — AccountableSharingException:** Consequential blocked sharing needs have a documented disposition.
- **`B10.03.03.F02` — Blocked Disclosure Explanation:** Blocked disclosure is explained, with accessible and accurate supporting evidence.
- **`B10.03.03.F03` — Corrective Referral Route:** Corrective referral has an identifiable route.
- **`B10.03.03.F04` — Reviewable Exception Evidence:** Reviewable exception is evidenced, with accessible and accurate supporting evidence.
- **`B10.03.03.F05` — Resolved Sharing Outcome:** Resolved sharing has an explicit outcome.

#### B10.03.04 — Disclosure Authority Revalidation
Ensure sharing decisions reflect current consent, policy, compartment and representative authority.

*Enterprise contributions: E05.06, E05.03, E02.01; findings: R01, R02, R22.*

- **`B10.03.04.F01` — RevokedPermissionHonoured:** Withdrawn sharing authority prevents subsequent disclosure.
- **`B10.03.04.F02` — ChangedPolicyReassessed:** Previously granted access is reconsidered when policy changes.
- **`B10.03.04.F03` — RecipientPurposeVerified:** Sharing is limited to intended lawful/authorised purpose.
- **`B10.03.04.F04` — NoMetadataDisclosure:** Restricted records cannot be discovered through unprivileged metadata.
- **`B10.03.04.F05` — DenialAccountable:** A defensible record of an authorisation refusal is available where required.

#### B10.03.05 — External Health Information Receipt Stewardship
Account for receipt, acceptance and subsequent availability of clinical information exchanged across organisations.

- **`B10.03.05.F01` — ReceivedNotNecessarilyAccepted:** Business recipients can distinguish a received submission from an accepted contribution.
- **`B10.03.05.F02` — AcceptedInformationAccountable:** Accepted inbound clinical information has an identified custody obligation.
- **`B10.03.05.F03` — RejectedContributionExplained:** Material rejections carry an actionable qualification for the submitting party.
- **`B10.03.05.F04` — ReceivedSourceAttribution:** Source organisation, subject and exchange time are recorded where applicable.
- **`B10.03.05.F05` — DelayedDispositionVisible:** Information pending validation or import has a known and reviewable status.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-HIE-GATE, C-DOC-INGRESS. *Release horizon:* Unscheduled.

#### B10.03.06 — Cross-Organisation Exchange Exception Resolution
Coordinate the business handling of ambiguous, incomplete or rejected health information exchanges without silently losing care obligations.

- **`B10.03.06.F01` — ExchangeExceptionOwned:** A significant exchange exception has an accountable resolution responsibility.
- **`B10.03.06.F02` — BusinessTechnicalStatusSeparated:** A technical receipt failure is distinguishable from clinical information rejection.
- **`B10.03.06.F03` — ExceptionCareImpactVisible:** Clinical care impacts of an exchange problem can be escalated where appropriate.
- **`B10.03.06.F04` — PartnerNotificationAppropriate:** The sending partner receives an applicable, actionable disposition.
- **`B10.03.06.F05` — CorrectedExchangeTraceable:** A corrected submission can be related to the rejected or uncertain predecessor.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-HIE-GATE, C-DOC-INGRESS. *Release horizon:* Unscheduled.

### B10.04 — Consent, Preference & Representation Stewardship
Maintain and respect consent, decision authority and participation preferences.

#### B10.04.01 — Consent Intent Stewardship
Applicable permissions, refusals and qualifications can be recorded and respected in business decisions.

- **`B10.04.01.F01` — CurrentConsentIntent:** Qualifying care decisions apply the relevant recorded consent context.
- **`B10.04.01.F02` — Consent Communication Clarity:** Consent communication is clear, distinguishing permitted and nonconforming conditions.
- **`B10.04.01.F03` — Consent Scope Clarity:** Consent scope is clear, distinguishing permitted and nonconforming conditions.
- **`B10.04.01.F04` — Consent Withdrawal Recognition:** Consent withdrawal is recognised, distinguishing permitted and nonconforming conditions.
- **`B10.04.01.F05` — Consent Decision History:** Consent decision retains its history, distinguishing permitted and nonconforming conditions.

#### B10.04.02 — Participation Preference Stewardship
Communication and participation preferences can remain available to relevant service participants.

- **`B10.04.02.F01` — RespectedParticipationPreferences:** Applicable care interactions conform to current participation preferences.
- **`B10.04.02.F02` — Current Participation Preference:** Participation preference is current through available and usable support.
- **`B10.04.02.F03` — Context-Sensitive Preference Use:** Context-sensitive preference can be used through available and usable support.
- **`B10.04.02.F04` — Changed Preference Communication:** Changed preference is communicated through available and usable support.
- **`B10.04.02.F05` — Preference Conflict Recognition:** Preference conflict is recognised through available and usable support.

#### B10.04.03 — Decision Authority Stewardship
The enterprise can identify appropriate decision authority and its limits where required.

- **`B10.04.03.F01` — CorrectDecisionAuthorityUse:** Care decisions use a valid applicable decision authority basis.
- **`B10.04.03.F02` — Authoritative Decision Scope:** Authoritative decision has an explicit scope, distinguishing permitted and nonconforming conditions.
- **`B10.04.03.F03` — Delegation Currency:** Delegation is current through relevant changes or disruptions, with recovery where required.
- **`B10.04.03.F04` — Unauthorised Decision Detection:** Unauthorised decision is detected, including in adverse conditions.
- **`B10.04.03.F05` — Authority Change Traceability:** Authority change is traceable, distinguishing permitted and nonconforming conditions.

### B10.05 — Health Knowledge Stewardship
Curate trusted clinical guidance and domain knowledge for professional use.

#### B10.05.01 — Clinical Guidance Stewardship
Trusted clinical guidance can be curated and made available to relevant professional contexts.

- **`B10.05.01.F01` — CurrentApplicableGuidance:** Clinical guidance is current and applicable to its intended scope.
- **`B10.05.01.F02` — Guidance Source Authority:** Guidance source has an established authority basis, distinguishing permitted and nonconforming conditions.
- **`B10.05.01.F03` — Current Guidance Availability:** Current guidance is available through relevant changes or disruptions, with recovery where required.
- **`B10.05.01.F04` — Retired Guidance Visibility:** Retired guidance is visible, with accessible and accurate supporting evidence.
- **`B10.05.01.F05` — Guidance Revision Record:** Guidance revision is recorded, with accessible and accurate supporting evidence.

#### B10.05.02 — Knowledge Change Review
Changes in clinical knowledge can be assessed for their impact on practice.

- **`B10.05.02.F01` — ReviewedKnowledgeChange:** Material clinical knowledge changes receive an accountable business review.
- **`B10.05.02.F02` — Evidence-Grounded Knowledge Revision:** Evidence-grounded knowledge can be revised, with accessible and accurate supporting evidence.
- **`B10.05.02.F03` — Appropriate Knowledge Review:** Appropriate knowledge is reviewed.
- **`B10.05.02.F04` — Change Impact Awareness:** Change impact is made known through relevant changes or disruptions, with recovery where required.
- **`B10.05.02.F05` — Knowledge Release Trace:** Knowledge release is traceable, with accessible and accurate supporting evidence.

#### B10.05.03 — Knowledge Use Support
Clinical teams can obtain useful knowledge for decisions without obscuring uncertainty.

- **`B10.05.03.F01` — UsableClinicalKnowledge:** Clinical decisions have appropriate guidance and its qualifications available.
- **`B10.05.03.F02` — Clinically Relevant Knowledge Access:** Clinically relevant knowledge can be accessed.
- **`B10.05.03.F03` — Knowledge Applicability Clarity:** Knowledge applicability is clear.
- **`B10.05.03.F04` — Knowledge Limitations Disclosure:** Knowledge limitations are disclosed, with accessible and accurate supporting evidence.
- **`B10.05.03.F05` — Feedback on Knowledge Use:** Feedback on knowledge use reaches and can be understood by the intended recipient.

### B10.06 — Retention, Disclosure & Record Accountability
Meet obligations for retention, disclosure, correction and traceability.

#### B10.06.01 — Business Record Retention Stewardship
The enterprise can meet applicable duties to retain and protect health records.

- **`B10.06.01.F01` — CompliantRecordRetention:** Records are managed under the relevant retention obligations.
- **`B10.06.01.F02` — Applicable Retention Obligation:** Applicable retention has an explicit obligation.
- **`B10.06.01.F03` — Legal Hold Recognition:** Legal hold is recognised, with accessible and accurate supporting evidence.
- **`B10.06.01.F04` — Scheduled Record Disposition:** Scheduled record dispositions occur within the applicable context-defined interval under the relevant retention obligations.
- **`B10.06.01.F05` — Retention Evidence Availability:** Retention evidence is available, with accessible and accurate supporting evidence.

#### B10.06.02 — Request for Record Disclosure Management
Requests to access or disclose records can be assessed and fulfilled where authorised.

- **`B10.06.02.F01` — AccountableRecordDisclosure:** Valid record disclosure requests receive the correct outcome and a traceable decision.
- **`B10.06.02.F02` — Requester Entitlement Check:** Requester entitlement is checked, distinguishing permitted and nonconforming conditions.
- **`B10.06.02.F03` — Required Record Identification:** Required record is identified.
- **`B10.06.02.F04` — Timely Disclosure Disposition:** Record disclosure requests receive a disposition within the applicable context-defined interval.
- **`B10.06.02.F05` — Redaction and Scope Explanation:** Redaction and scope is explained, distinguishing permitted and nonconforming conditions.

#### B10.06.03 — Record Disclosure Accounting
Disclosures and significant record actions can be accounted for under business obligations.

- **`B10.06.03.F01` — TraceableDisclosureHistory:** Disclosable record activities retain the required evidence.
- **`B10.06.03.F02` — Disclosure Recipient Trace:** Disclosure recipient is traceable, with accessible and accurate supporting evidence.
- **`B10.06.03.F03` — Disclosure Content Identification:** Disclosure content is identified.
- **`B10.06.03.F04` — Authorised Disclosure Evidence:** Authorised disclosure is evidenced, distinguishing permitted and nonconforming conditions.
- **`B10.06.03.F05` — Disclosure History Review:** Disclosure history is reviewed, with accessible and accurate supporting evidence.

#### B10.06.04 — Comprehensive Evidence Disclosure
Requested evidence is assembled and disclosed under the applicable authorisation and completeness policy, preserving attributable decisions, qualifications and the distinction between record exports and dereferenced explanatory evidence. Selective or all-or-nothing release follows policy without leaking protected withheld information.

*Enterprise contributions: E08.08, E05.05, E09.03; findings: R04, R23.*

- **`B10.06.04.F01` — Requested Evidence Assembly Completeness:** The approved requested evidence set is assembled with its completeness and qualifications established before disclosure of the policy-permitted scope.
- **`B10.06.04.F02` — Policy-Governed Evidence Release:** Evidence is released selectively where the applicable policy permits, or withheld as a whole where required completeness cannot be authorised. Withheld content and prohibited omission details remain protected.
- **`B10.06.04.F03` — RawVsInterpretedDistinct:** A record export differs clearly from dereferenced explanatory evidence.
- **`B10.06.04.F04` — DisclosureBasisRetained:** Purpose, authority and release decision remain attributable.
- **`B10.06.04.F05` — IncompleteRequestQualified:** Missing essential evidence yields refusal/exception rather than misleading completeness.

### B10.07 — Information Correction & Reconciliation
Resolve inaccurate, inconsistent or disputed health information without obscuring history.

#### B10.07.01 — Record Error Correction
Known errors can be corrected appropriately while retaining necessary history.

- **`B10.07.01.F01` — AccountableRecordCorrection:** Record corrections have appropriate authorisation and traceability.
- **`B10.07.01.F02` — Correction Request Capture:** Correction request is captured.
- **`B10.07.01.F03` — Evidence-Supported Correction:** Evidence-supported can be corrected.
- **`B10.07.01.F04` — Corrected Record Visibility:** Corrected record is visible.
- **`B10.07.01.F05` — Prior Statement Preservation:** Prior statement is preserved through relevant changes or disruptions, with recovery where required.

#### B10.07.02 — Disputed Information Resolution
Conflicting or disputed statements can be reviewed without silently asserting certainty.

- **`B10.07.02.F01` — DispositionedInformationDispute:** Information disputes have a recorded outcome or qualification.
- **`B10.07.02.F02` — Dispute Claim Acknowledgement:** Dispute claim acknowledgement reaches and can be understood by the intended recipient.
- **`B10.07.02.F03` — Independent Dispute Review:** Independent dispute is reviewed through available and usable support.
- **`B10.07.02.F04` — Qualified Disputed Content:** Qualified disputed content is available.
- **`B10.07.02.F05` — Dispute Resolution Communication:** Dispute resolution communication reaches and can be understood by the intended recipient.

#### B10.07.03 — Cross-Source Information Reconciliation
Material differences between information sources can be identified and appropriately addressed.

- **`B10.07.03.F01` — QualifiedCrossSourceConsistency:** Cross-source discrepancies are resolved or visibly qualified.
- **`B10.07.03.F02` — Conflicting Source Detection:** Conflicting source is detected, with accessible and accurate supporting evidence.
- **`B10.07.03.F03` — Preferred Source Rationale:** Preferred source has a recorded rationale, with accessible and accurate supporting evidence.
- **`B10.07.03.F04` — Reconciliation Change Trace:** Reconciliation change is traceable.
- **`B10.07.03.F05` — Unresolved Disagreement Disclosure:** Unresolved disagreement is disclosed, with accessible and accurate supporting evidence.

#### B10.07.04 — Correction Commitment Accountability
Distinguish working correction, review, execution and authoritative acceptance while preserving immutable prior versions.

*Enterprise contributions: E01.01, E06.07, E07.02; findings: R06, R09.*

- **`B10.07.04.F01` — PreparedCorrectionNotFinal:** Uncommitted drafts are not treated as authoritative.
- **`B10.07.04.F02` — ValidationSeparate:** Quality checks produce findings before acceptance.
- **`B10.07.04.F03` — CommittedVersionIdentified:** Accepted correction has a resolvable new governed version.
- **`B10.07.04.F04` — RejectedCorrectionSafe:** Rejected action does not alter previous authoritative content.
- **`B10.07.04.F05` — ConflictResolutionTraceable:** Competing corrections retain decision evidence and prior history.

#### B10.07.05 — Uncertain Person Identity Association Resolution
Provide an accountable business process for assessing ambiguous, conflicting and contested patient identity matches.

- **`B10.07.05.F01` — AmbiguousMatchReferral:** A disputed match reaches an appropriately authorised reviewer.
- **`B10.07.05.F02` — ResolutionEvidence:** A resolution references its source evidence, decision maker and applicable time.
- **`B10.07.05.F03` — HighRiskFalseMergeGuard:** Known wrong-person adverse cases are not accepted as unqualified linkage.
- **`B10.07.05.F04` — ExceptionTimelinessVisible:** Pending identity cases have measurable age and escalation status.
- **`B10.07.05.F05` — DisputedIdentityVisibility:** A dispute cannot disappear simply because one consumer selects a preferred source.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PERSON-CORR, C-PERSON-CORRECT. *Release horizon:* R3+ full EMPI scope; earlier identity safety remains applicable.

#### B10.07.06 — Person Identity Linkage Reversal Accountability
Ensure identity association corrections and reversals preserve prior clinical attribution and downstream impact accountability.

- **`B10.07.06.F01` — ReversalAuthority:** A wrong association is reversed only under the applicable review authority.
- **`B10.07.06.F02` — AffectedClinicalRecordsKnown:** Dependent record associations can be identified for review or qualification.
- **`B10.07.06.F03` — HistoricalDecisionPreserved:** The prior identity link and its reason remain auditable after correction.
- **`B10.07.06.F04` — WrongPersonExposureAssessed:** The impact of wrong-person disclosure or clinical use can be assessed.
- **`B10.07.06.F05` — CorrectedLinkDisclosedSafely:** Authorised consumers receive appropriate information about a corrected association.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PERSON-CORRECT, DRP-IU. *Release horizon:* R3+ full EMPI scope.

## B11 — Safety, Quality & Accountability
Healthcare quality, safety, conformance and concerns can be examined, assured, improved and accounted for.

### B11.01 — Clinical Safety & Risk Management
Identify, reduce and respond to risks arising from care.

#### B11.01.01 — Clinical Hazard Identification
Risks of harm arising from the care context can be recognised before or during delivery.

- **`B11.01.01.F01` — RecognisedClinicalHazards:** Clinical hazards are identified by the relevant controls.
- **`B11.01.01.F02` — Clinical Hazard Signal Recognition:** Clinical hazard signal is recognised, including in adverse conditions.
- **`B11.01.01.F03` — Hazard Severity Appraisal:** Hazard severity can be appraised, including in adverse conditions.
- **`B11.01.01.F04` — Hazard Reporter Feedback:** Hazard reporter feedback is available, including in adverse conditions.
- **`B11.01.01.F05` — Hazard Register Currency:** Hazard register is current, including in adverse conditions.

#### B11.01.02 — Clinical Risk Mitigation
Appropriate prevention and mitigation arrangements can be established for material risks.

- **`B11.01.02.F01` — EffectiveRiskMitigation:** Material clinical risks have active, reviewed mitigation.
- **`B11.01.02.F02` — Proportionate Risk Control:** Risk controls are proportionate to the applicable risk, including in adverse conditions.
- **`B11.01.02.F03` — Control Responsibility Assignment:** Control responsibility is assigned.
- **`B11.01.02.F04` — Mitigation Progress Monitoring:** Mitigation progress can be monitored, with accessible and accurate supporting evidence.
- **`B11.01.02.F05` — Residual Risk Visibility:** Residual risk is visible, including in adverse conditions.

#### B11.01.03 — Clinical Safety Escalation
Emerging or unresolved safety concerns can reach an accountable decision-maker.

- **`B11.01.03.F01` — TimelySafetyEscalation:** Qualifying high-consequence concerns are escalated within the applicable interval.
- **`B11.01.03.F02` — Safety Concern Escalation Route:** Safety concern escalation has an identifiable route, including in adverse conditions.
- **`B11.01.03.F03` — Safety Escalation Receipt Confirmation:** Safety escalation receipt is confirmed, including in adverse conditions.
- **`B11.01.03.F04` — Protective Action Timing:** Protective action has its timing established.
- **`B11.01.03.F05` — Safety Escalation Closure:** Safety escalation has an explicit closure, including in adverse conditions.

### B11.02 — Incident Reporting & Learning
Capture, investigate, respond to and learn from incidents.

#### B11.02.01 — Incident Recognition and Reporting
Significant safety or service events can be reported without unnecessary loss of context.

- **`B11.02.01.F01` — CompleteIncidentReporting:** Qualifying reported incidents retain their required core information.
- **`B11.02.01.F02` — Incident Classification Accuracy:** Incident classification is accurate.
- **`B11.02.01.F03` — Safe Reporting Accessibility:** Safe reporting is accessible and usable.
- **`B11.02.01.F04` — Prompt Incident Notification:** Qualifying incidents are notified within the applicable context-defined interval.
- **`B11.02.01.F05` — Incident Report Completeness:** Incident report is complete.

#### B11.02.02 — Clinical Incident Follow-Up
Reported incidents can be investigated and acted upon to an accountable disposition.

- **`B11.02.02.F01` — Timely Closure with Documented Action:** Qualifying incidents have evidenced follow-up and accountable closure.
- **`B11.02.02.F02` — Investigation Ownership:** Investigation has an identifiable responsible owner.
- **`B11.02.02.F03` — Harm and Contributor Assessment:** Harm and contributor is assessed, including in adverse conditions.
- **`B11.02.02.F04` — Timely Follow-Up Action:** Incident follow-up actions occur within the applicable context-defined interval.
- **`B11.02.02.F05` — Affected Party Communication:** Affected party communication reaches and can be understood by the intended recipient.

#### B11.02.03 — Incident Learning Application
Lessons from incidents can inform changes to care and services.

- **`B11.02.03.F01` — DemonstratedIncidentLearning:** Significant incident findings prompt evaluated improvement action.
- **`B11.02.03.F02` — Evidence-Based Improvement Lesson:** Incident improvement lessons have an evidence basis, with accessible and accurate supporting evidence.
- **`B11.02.03.F03` — Learning Dissemination:** Learning dissemination reaches and can be understood by the intended recipient.
- **`B11.02.03.F04` — Corrective Practice Adoption:** Corrective practice can be adopted.
- **`B11.02.03.F05` — Learning Effect Verification:** Learning effect is verified.

### B11.03 — Clinical Governance & Practice Assurance
Establish accountability for the appropriateness and safety of clinical practice.

#### B11.03.01 — Clinical Practice Standards Stewardship
Applicable clinical standards and responsibilities can be established and maintained.

- **`B11.03.01.F01` — CurrentPracticeExpectations:** Practice areas have current, applicable guidance and accountability.
- **`B11.03.01.F02` — Authoritative Practice Standard:** The applicable authoritative practice standard is identifiable.
- **`B11.03.01.F03` — Standards Change Communication:** Standards change is communicated through relevant changes or disruptions, with recovery where required.
- **`B11.03.01.F04` — Applicable Standard Availability:** Applicable standard is available.
- **`B11.03.01.F05` — Standard Exception Awareness:** Standard exception is made known.

#### B11.03.02 — Clinical Practice Review
Appropriateness and safety of clinical practice can be examined using relevant evidence.

- **`B11.03.02.F01` — EvidenceBasedPracticeReview:** Designated practice reviews are completed with adequate supporting evidence.
- **`B11.03.02.F02` — Representative Practice Sampling:** Practice reviews can use representative samples.
- **`B11.03.02.F03` — Documented Practice Findings:** Documented practice findings are available, with accessible and accurate supporting evidence.
- **`B11.03.02.F04` — Practice Improvement Assignment:** Practice improvement is assigned.
- **`B11.03.02.F05` — Review Outcome Feedback:** Review outcome feedback is available, with accessible and accurate supporting evidence.

#### B11.03.03 — Clinical Accountability Management
Material practice issues can be assigned to appropriate oversight and disposition.

- **`B11.03.03.F01` — AccountableClinicalGovernance:** Material practice exceptions receive recorded, responsible review.
- **`B11.03.03.F02` — Clear Clinical Responsibility:** Clinical accountability is explicit for the applicable practice decisions and exceptions.
- **`B11.03.03.F03` — Decision Authority Trace:** Decision authority is traceable, distinguishing permitted and nonconforming conditions.
- **`B11.03.03.F04` — Accountability Transfer Record:** Accountability transfer is recorded, with accessible and accurate supporting evidence.
- **`B11.03.03.F05` — Unresolved Accountability Escalation:** Unresolved accountability is escalated, with accessible and accurate supporting evidence.

#### B11.03.04 — Auditable Clinical Governance Evidence
Obtain adequate, immutable and interpretable evidence to review clinical governance and material practice events.

*Enterprise contributions: E08.07, E08.08, E09.04; findings: R03, R04.*

- **`B11.03.04.F01` — MandatoryActionsEvidenced:** Relevant clinical activity generates required audit evidence.
- **`B11.03.04.F02` — GovernanceEvidenceImmutable:** Previously accepted audit/provenance records cannot be rewritten.
- **`B11.03.04.F03` — EvidenceCorrelatable:** Clinician, subject, event, decision and information version can be related.
- **`B11.03.04.F04` — Authorised Review Package Disclosure:** Review evidence assemblies are disclosed only within the authorised scope, with all-or-nothing treatment where required by policy and selective disclosure where permitted, without revealing protected withheld evidence.
- **`B11.03.04.F05` — AdverseFindingVisible:** Qualified or contrary evidence is not suppressed from authorised review.

### B11.04 — Service Quality & Outcome Evaluation
Assess whether service delivery produces acceptable outcomes and experience.

#### B11.04.01 — Service Outcome Assessment
Service outcomes can be evaluated in relation to intended purpose and population.

- **`B11.04.01.F01` — Evaluated Service Outcomes:** Services have defensible outcome measures and interpretation.
- **`B11.04.01.F02` — Outcome Indicator Relevance:** Outcome indicator is relevant.
- **`B11.04.01.F03` — Risk-Adjusted Outcome Interpretation:** Risk-adjusted outcome is interpreted, including in adverse conditions.
- **`B11.04.01.F04` — Outcome Data Completeness:** Outcome data is complete.
- **`B11.04.01.F05` — Outcome Trend Review:** Outcome trend is reviewed, with accessible and accurate supporting evidence.

#### B11.04.02 — Service Experience Evaluation
The experience of people receiving or contributing services can be assessed.

- **`B11.04.02.F01` — RepresentativeServiceExperience:** Designated services have appropriate service experience evidence and review.
- **`B11.04.02.F02` — Inclusive Feedback Capture:** Inclusive feedback is captured through available and usable support.
- **`B11.04.02.F03` — Experience Issue Classification:** Experience issue is classified.
- **`B11.04.02.F04` — Respondent Privacy:** Service experience respondents retain the applicable privacy protections, including in adverse conditions.
- **`B11.04.02.F05` — Feedback-Informed Improvement:** Feedback-informed improvement reaches and can be understood by the intended recipient.

#### B11.04.03 — Quality Improvement Follow-Through
Quality findings can produce accountable service improvements and checks of effect.

- **`B11.04.03.F01` — VerifiedQualityImprovement:** Material quality improvement commitments receive effectiveness review.
- **`B11.04.03.F02` — Improvement Commitment Ownership:** Improvement commitment has an identifiable responsible owner.
- **`B11.04.03.F03` — Implementation Progress Visibility:** Implementation progress are visible, with accessible and accurate supporting evidence.
- **`B11.04.03.F04` — Measured Improvement Impact:** Measured improvement has its impact assessed.
- **`B11.04.03.F05` — Sustained Change Review:** Sustained change is reviewed, with accessible and accurate supporting evidence.

### B11.05 — Regulatory Conformance & Accreditation
Demonstrate and manage conformance with external obligations.

#### B11.05.01 — Regulatory Obligation Interpretation
Applicable external obligations can be identified for the service context.

- **`B11.05.01.F01` — IdentifiedApplicableObligations:** Service areas have correctly mapped relevant regulatory requirements.
- **`B11.05.01.F02` — Applicable Obligation Identification:** Applicable obligation is identified.
- **`B11.05.01.F03` — Obligation Owner Awareness:** Obligation owner is made known.
- **`B11.05.01.F04` — Change in Regulation Recognition:** Change in regulation is recognised, distinguishing permitted and nonconforming conditions.
- **`B11.05.01.F05` — Interpretable Compliance Guidance:** Interpretable compliance guidance is available, distinguishing permitted and nonconforming conditions.

#### B11.05.02 — Accreditation Evidence Preparation
The enterprise can assemble evidence demonstrating its conformity for required reviews.

- **`B11.05.02.F01` — AvailableAccreditationEvidence:** Applicable accreditation criteria have current, attributable evidence.
- **`B11.05.02.F02` — Evidence-to-Standard Mapping:** Evidence-to-standard can be mapped, with accessible and accurate supporting evidence.
- **`B11.05.02.F03` — Evidence Currency Verification:** Evidence currency is verified, with accessible and accurate supporting evidence.
- **`B11.05.02.F04` — Evidence Gap Identification:** Evidence gap is identified.
- **`B11.05.02.F05` — Accreditation Submission Trace:** Accreditation submission is traceable, with accessible and accurate supporting evidence.

#### B11.05.03 — Conformance Gap Resolution
Actual or suspected nonconformance can be investigated and corrected or qualified.

- **`B11.05.03.F01` — AccountableConformanceDisposition:** Consequential conformance findings have an approved action or qualified exception.
- **`B11.05.03.F02` — Verified Conformance Gap:** Verified conformance gaps are identifiable, distinguishing permitted and nonconforming conditions.
- **`B11.05.03.F03` — Accountable Remediation:** Accountable can be remediated, with accessible and accurate supporting evidence.
- **`B11.05.03.F04` — Exception and Waiver Visibility:** Exception and waiver is visible, with accessible and accurate supporting evidence.
- **`B11.05.03.F05` — Independent Gap Closure Check:** Independent gap closure is checked through available and usable support.

#### B11.05.04 — Evidence-Based Accreditation Verification
Demonstrate conformance through repeatable observations without asserting success solely from a complete-looking model.

*Enterprise contributions: E08.04, E08.07, E09.04; findings: R18.*

- **`B11.05.04.F01` — RequirementToEvidenceTrace:** A compliance claim links to explicit tested requirement and version.
- **`B11.05.04.F02` — UnverifiedNotCompliant:** Missing evidence cannot be recorded as passing conformance.
- **`B11.05.04.F03` — ExceptionDisclosed:** Known qualification and deferral remain visible in review.
- **`B11.05.04.F04` — RepeatableTestAvailable:** Assurance checks can be rerun with controlled inputs.
- **`B11.05.04.F05` — ReviewerAuthorityEstablished:** The approval and evidence-review roles are identifiable.

### B11.06 — Complaints, Advocacy & Redress
Receive and address concerns and remedy service shortcomings.

#### B11.06.01 — Complaint Reception and Recognition
People can raise service concerns through accessible, respectful means.

- **`B11.06.01.F01` — AccessibleComplaintLodgement:** Complaints can be successfully submitted and acknowledged through accessible pathways.
- **`B11.06.01.F02` — Accessible Complaint Submission:** Accessible complaint submission is supported through available and usable support.
- **`B11.06.01.F03` — Complaint Acknowledgement:** Complaint acknowledgement reaches and can be understood by the intended recipient.
- **`B11.06.01.F04` — Appropriate Complaint Classification:** Appropriate complaint is classified.
- **`B11.06.01.F05` — Urgent Concern Escalation:** Urgent concern escalation reaches and can be understood by the intended recipient.

#### B11.06.02 — Fair Complaint Review
Complaints can be considered with suitable evidence and accountable judgement.

- **`B11.06.02.F01` — ReviewableComplaintOutcome:** Complaints have reasoned and appropriately communicated outcomes.
- **`B11.06.02.F02` — Impartial Reviewer Assignment:** Impartial reviewer is assigned, with accessible and accurate supporting evidence.
- **`B11.06.02.F03` — Relevant Evidence Review:** Relevant evidence is reviewed, with accessible and accurate supporting evidence.
- **`B11.06.02.F04` — Review Timeliness:** Review meets the applicable timing conditions, with accessible and accurate supporting evidence.
- **`B11.06.02.F05` — Explained Review Decision:** Explained review has an explicit decision, with accessible and accurate supporting evidence.

#### B11.06.03 — Redress and Service Remedy
Substantiated shortcomings can lead to suitable remedial arrangements.

- **`B11.06.03.F01` — DeliveredAgreedRemedy:** Agreed remedies are completed or transparently renegotiated.
- **`B11.06.03.F02` — Agreed Remedy Definition:** Agreed remedy is defined.
- **`B11.06.03.F03` — Remedy Completion Evidence:** Remedy completion is evidenced, with accessible and accurate supporting evidence.
- **`B11.06.03.F04` — Complainant Communication:** Complainant communication reaches and can be understood by the intended recipient.
- **`B11.06.03.F05` — Remedy Effectiveness Review:** Remedy effectiveness are reviewed, with accessible and accurate supporting evidence.

## B12 — Enterprise Direction & Readiness
The healthcare enterprise can set direction, steward resources, manage risk and demonstrate operational readiness.

### B12.01 — Strategic Direction & Service Investment
Determine desired outcomes, strategic priorities and investments.

#### B12.01.01 — Health Enterprise Strategy Formulation
Desired business outcomes and strategic priorities can be established from purpose and evidence.

- **`B12.01.01.F01` — ClearStrategicIntent:** Designated strategic outcomes have accountable definitions and measures.
- **`B12.01.01.F02` — Evidence-Informed Strategic Choices:** Evidence-informed strategic choices is supported through available and usable support.
- **`B12.01.01.F03` — Strategic Outcome Clarity:** Strategic outcome is clear.
- **`B12.01.01.F04` — Stakeholder Consultation Coverage:** Stakeholder consultation is covered.
- **`B12.01.01.F05` — Strategy Revision Trace:** Strategy revision is traceable, with accessible and accurate supporting evidence.

#### B12.01.02 — Investment Option Appraisal
Competing investments can be evaluated against expected value, cost, risk and alternatives.

- **`B12.01.02.F01` — ComparableInvestmentEvidence:** Investment decisions have consistent supporting option assessments.
- **`B12.01.02.F02` — Comparable Investment Options:** Comparable investment options are available.
- **`B12.01.02.F03` — Full Lifecycle Cost Visibility:** Full lifecycle cost is visible, with accessible and accurate supporting evidence.
- **`B12.01.02.F04` — Benefit and Risk Appraisal:** Benefit and risk can be appraised, including in adverse conditions.
- **`B12.01.02.F05` — Decision Assumption Transparency:** Decision assumption is transparent.

#### B12.01.03 — Investment Portfolio Prioritisation
Resources can be assigned across competing service priorities coherently.

- **`B12.01.03.F01` — TraceableInvestmentPriority:** Investment decisions retain documented strategic alignment and rationale.
- **`B12.01.03.F02` — Portfolio Trade-Off Visibility:** Portfolio trade-off is visible, with accessible and accurate supporting evidence.
- **`B12.01.03.F03` — Resource-Constrained Priorities:** Investment priorities account for resource constraints, with accessible and accurate supporting evidence.
- **`B12.01.03.F04` — Reprioritisation Transparency:** Reprioritisation is transparent.
- **`B12.01.03.F05` — Portfolio Balance Review:** Portfolio balance is reviewed, with accessible and accurate supporting evidence.

### B12.02 — Finance, Funding & Commercial Stewardship
Obtain, account for and manage financial and contractual resources.

#### B12.02.01 — Financial Resource Planning
The organisation can plan financial resources needed for its services and obligations.

- **`B12.02.01.F01` — CompleteServiceFinancialPlan:** Designated service commitments have a current financial basis.
- **`B12.02.01.F02` — Forecast Funding Sufficiency:** Forecast funding is sufficient.
- **`B12.02.01.F03` — Cost Driver Visibility:** Cost driver is visible, with accessible and accurate supporting evidence.
- **`B12.02.01.F04` — Scenario-Based Budget Revision:** Scenario-based budget can be revised.
- **`B12.02.01.F05` — Financial Plan Accountability:** Financial plan has an accountable party, with accessible and accurate supporting evidence.

#### B12.02.02 — Commercial Arrangement Stewardship
Commercial terms and supplier responsibilities can be established and managed.

- **`B12.02.02.F01` — GovernedCommercialCommitments:** Active commercial arrangements have defined obligations and accountable oversight.
- **`B12.02.02.F02` — Contract Obligation Visibility:** Contract obligation is visible, distinguishing permitted and nonconforming conditions.
- **`B12.02.02.F03` — Supplier Performance Follow-Up:** Supplier performance receives follow-up.
- **`B12.02.02.F04` — Commercial Risk Recognition:** Commercial risk is recognised, including in adverse conditions.
- **`B12.02.02.F05` — Contract Renewal Readiness:** Contract renewal is established, distinguishing permitted and nonconforming conditions.

#### B12.02.03 — Financial Accountability and Reporting
Financial activity can be accounted for and explained to relevant oversight.

- **`B12.02.03.F01` — ReconciledFinancialPosition:** Financial reporting periods have complete, reconcilable financial evidence.
- **`B12.02.03.F02` — Accurate Financial Statement:** Financial statements accurately represent the relevant financial position.
- **`B12.02.03.F03` — Funds Use Traceability:** Funds use is traceable, with accessible and accurate supporting evidence.
- **`B12.02.03.F04` — Timely Financial Reporting:** Financial reports are available within the applicable context-defined interval.
- **`B12.02.03.F05` — Financial Exception Explanation:** Financial exception is explained.

### B12.03 — Enterprise Performance & Accountability
Explain enterprise position, performance and delivery of commitments.

#### B12.03.01 — Enterprise Commitment Tracking
Material commitments can be monitored and brought to an accountable disposition.

- **`B12.03.01.F01` — Accountable Commitment Disposition:** Enterprise commitments have current ownership and a recorded outcome or exception.
- **`B12.03.01.F02` — Named Commitment Accountability:** Named commitment has an accountable party, with accessible and accurate supporting evidence.
- **`B12.03.01.F03` — Milestone Progress Visibility:** Milestone progress are visible, with accessible and accurate supporting evidence.
- **`B12.03.01.F04` — Overdue Commitment Escalation:** Overdue commitment escalation reaches and can be understood by the intended recipient.
- **`B12.03.01.F05` — Commitment Closure Evidence:** Commitment closure is evidenced, with accessible and accurate supporting evidence.

#### B12.03.02 — Enterprise Outcome Reporting
Leaders can obtain an intelligible view of performance against enterprise objectives.

- **`B12.03.02.F01` — CredibleOutcomeReporting:** Designated enterprise outcomes have current evidence and relevant qualifications.
- **`B12.03.02.F02` — Outcome Measure Integrity:** Outcome measure retains its integrity.
- **`B12.03.02.F03` — Strategic Outcome Comparison:** Strategic outcome can be compared.
- **`B12.03.02.F04` — Exception Context Explanation:** Exception context is explained.
- **`B12.03.02.F05` — Decision-Ready Reporting:** Decision-ready is reported, with accessible and accurate supporting evidence.

#### B12.03.03 — Management Decision Accountability
Consequential management decisions can be explained and reviewed.

- **`B12.03.03.F01` — ReviewableManagementDecision:** Management decisions retain adequate rationale, approval and action context.
- **`B12.03.03.F02` — Recorded Decision Rationale:** Recorded decision has a recorded rationale, with accessible and accurate supporting evidence.
- **`B12.03.03.F03` — Delegated Decision Authority:** Delegated decision has an established authority basis, distinguishing permitted and nonconforming conditions.
- **`B12.03.03.F04` — Decision Communication:** Decision communication reaches and can be understood by the intended recipient.
- **`B12.03.03.F05` — Decision Outcome Follow-Up:** Decision outcome receives follow-up.

### B12.04 — Enterprise Risk & Resilience
Understand material risks, prepare for disruption and sustain essential activity.

#### B12.04.01 — Enterprise Risk Assessment
The organisation can identify and evaluate material strategic and operational risks.

- **`B12.04.01.F01` — CurrentEnterpriseRiskUnderstanding:** Designated enterprise risk areas receive current risk review.
- **`B12.04.01.F02` — Current Risk Exposure:** Risk exposure is current, including in adverse conditions.
- **`B12.04.01.F03` — Cross-Enterprise Risk Aggregation:** Cross-enterprise risk can be aggregated, including in adverse conditions.
- **`B12.04.01.F04` — Risk Treatment Option Visibility:** Risk treatment option is visible, including in adverse conditions.
- **`B12.04.01.F05` — Risk Acceptance Trace:** Risk acceptance is traceable, including in adverse conditions.

#### B12.04.02 — Business Continuity Preparedness
Essential business activities can continue or recover through specified disruptions.

- **`B12.04.02.F01` — TestedBusinessContinuity:** Business continuity arrangements meet the agreed business requirements under prescribed continuity scenarios.
- **`B12.04.02.F02` — Prioritised Essential Services:** Prioritised essential services are available.
- **`B12.04.02.F03` — Business Recovery Scenario Coverage:** Business recovery scenario is covered through relevant changes or disruptions, with recovery where required.
- **`B12.04.02.F04` — Continuity Exercise Evidence:** Continuity exercise is evidenced, with accessible and accurate supporting evidence.
- **`B12.04.02.F05` — Continuity Plan Currency:** Continuity plan is current through relevant changes or disruptions, with recovery where required.

#### B12.04.03 — Enterprise Recovery Coordination
The organisation can coordinate the recovery of affected people, services and resources.

- **`B12.04.03.F01` — CoordinatedEnterpriseRecovery:** Enterprise recovery has accountable prioritisation and follow-through.
- **`B12.04.03.F02` — Recovery Responsibility Allocation:** Recovery responsibility is allocated through relevant changes or disruptions, with recovery where required.
- **`B12.04.03.F03` — Recovery Priority Sequencing:** Recovery priority is sequenced through relevant changes or disruptions, with recovery where required.
- **`B12.04.03.F04` — Restoration Status Visibility:** Restoration status is visible, with accessible and accurate supporting evidence.
- **`B12.04.03.F05` — Recovery Completion Review:** Recovery completion is reviewed, with accessible and accurate supporting evidence.

#### B12.04.04 — Accepted Service Obligation Continuity
Ensure business-critical accepted obligations survive interruption and their completion or failure can be accounted for.

*Enterprise contributions: E06.04, E06.07, E08.06; findings: R21, R24.*

- **`B12.04.04.F01` — AcceptedWorkNotLost:** Accepted work remains recoverable after bounded failure.
- **`B12.04.04.F02` — OverloadRefusalDeclared:** Refused work is not reported as accepted.
- **`B12.04.04.F03` — RecoveryStatusAccurate:** Operators know the actual recovery/unfinished-work state.
- **`B12.04.04.F04` — NoUncontrolledDuplicateWork:** Replay does not silently duplicate business obligations.
- **`B12.04.04.F05` — CriticalServiceRestored:** Declared critical service continuity is verified by representative operation.

### B12.05 — Health Service Readiness & Mobilisation
Demonstrate and restore preparedness to provide required health services.

#### B12.05.01 — Health Service Readiness Demonstration
The enterprise can show that required services are ready for their designated mission or demand.

- **`B12.05.01.F01` — Verified Required Readiness:** Designated services meet their defined readiness evidence requirements.
- **`B12.05.01.F02` — Operational Capability Evidence:** Operational capability is evidenced, with accessible and accurate supporting evidence.
- **`B12.05.01.F03` — Readiness Constraint Disclosure:** Readiness constraint is disclosed, with accessible and accurate supporting evidence.
- **`B12.05.01.F04` — Scenario-Based Readiness Test:** Readiness can be tested against the applicable service scenarios.
- **`B12.05.01.F05` — Readiness Acceptance Accountability:** Readiness acceptance has an accountable party, with accessible and accurate supporting evidence.

#### B12.05.02 — Surge Capacity Mobilisation
Additional clinical and supporting capacity can be marshalled when demand rises sharply.

- **`B12.05.02.F01` — TimelySurgeMobilisation:** Surge response meets the specified availability conditions.
- **`B12.05.02.F02` — Surge Trigger Definition:** Surge trigger is defined.
- **`B12.05.02.F03` — Rapid Additional Capacity:** Additional surge capacity is available within the applicable context-defined interval.
- **`B12.05.02.F04` — Surge Staff Competence:** Staff supporting surge response have the required competence.
- **`B12.05.02.F05` — Surge Demobilisation Review:** Surge demobilisation is reviewed, with accessible and accurate supporting evidence.

#### B12.05.03 — Deployed or Remote Care Readiness
Care can be prepared for operation outside its usual locations when applicable.

- **`B12.05.03.F01` — VerifiedDeployedCareReadiness:** Remote and deployed services have the required staff, supply and support readiness.
- **`B12.05.03.F02` — Deployment Resource Completeness:** Deployment resource is complete.
- **`B12.05.03.F03` — Disconnected Operations Readiness:** Disconnected operations are established.
- **`B12.05.03.F04` — Remote Referral and Evacuation:** Remote referral and evacuation reaches and can be understood by the intended recipient.
- **`B12.05.03.F05` — Deployment Continuity Check:** Deployment continuity is checked through relevant changes or disruptions, with recovery where required.

## B13 — Population Health & Learning
Population health can be assessed and improved, and trustworthy research and learning can influence practice.

### B13.01 — Population Health Surveillance
Understand the health patterns, threats and needs of populations.

#### B13.01.01 — Population Health Pattern Assessment
Health patterns and material changes can be understood across defined populations.

- **`B13.01.01.F01` — QualifiedPopulationIndicators:** Designated surveillance periods have complete, interpretable population evidence.
- **`B13.01.01.F02` — Population Indicator Coverage:** Population indicator is covered.
- **`B13.01.01.F03` — Disparity and Equity Analysis:** Disparity and equity can be analysed.
- **`B13.01.01.F04` — Trend Confidence Qualification:** Trend confidence is qualified.
- **`B13.01.01.F05` — Timely Surveillance Summary:** Population surveillance summaries are available within the applicable context-defined interval.

#### B13.01.02 — Emerging Health Threat Detection
Relevant health anomalies can be recognised for further evaluation.

- **`B13.01.02.F01` — TimelyThreatRecognition:** Population threats are detected and escalated under the defined conditions.
- **`B13.01.02.F02` — Early Signal Sensitivity:** Population surveillance recognises early threat signals within the applicable context-defined interval.
- **`B13.01.02.F03` — False Alarm Review:** False alarm is reviewed, including in adverse conditions.
- **`B13.01.02.F04` — Threat Escalation Timing:** Threat escalation timing reaches and can be understood by the intended recipient.
- **`B13.01.02.F05` — Cross-Source Threat Confirmation:** Cross-source threat is confirmed, with accessible and accurate supporting evidence.

#### B13.01.03 — Population Health Needs Analysis
Unmet health needs and inequities can be examined across groups and locations.

- **`B13.01.03.F01` — EvidenceBasedNeedsAssessment:** Priority population groups have appropriately evidenced current needs assessments.
- **`B13.01.03.F02` — Needs Evidence Coverage:** Needs evidence is covered, with accessible and accurate supporting evidence.
- **`B13.01.03.F03` — Community Difference Visibility:** Community difference is visible, with accessible and accurate supporting evidence.
- **`B13.01.03.F04` — Priority Need Justification:** Priority need is justified.
- **`B13.01.03.F05` — Periodic Needs Reassessment:** Periodic needs are reassessed.

### B13.02 — Health Promotion & Prevention
Prevent avoidable illness and support healthier participation.

#### B13.02.01 — Population Prevention Programme Planning
Prevention activities can be designed for recognised risk and community need.

- **`B13.02.01.F01` — TargetedPreventionIntent:** Prevention programmes state their population need and evaluable intended outcome.
- **`B13.02.01.F02` — At-Risk Group Identification:** At-risk group is identified, including in adverse conditions.
- **`B13.02.01.F03` — Preventive Objective Clarity:** Preventive objective is clear.
- **`B13.02.01.F04` — Programme Reach Planning:** Prevention programmes plan their intended population reach.
- **`B13.02.01.F05` — Resource-Need Match:** Resource-need is matched.

#### B13.02.02 — Health Promotion Engagement
Intended groups can access usable health promotion information and opportunities.

- **`B13.02.02.F01` — EffectivePreventionReach:** Intended population groups are reached through suitable prevention channels.
- **`B13.02.02.F02` — Accessible Health Promotion:** Accessible health promotion is supported through available and usable support.
- **`B13.02.02.F03` — Community Engagement Reach:** Community engagement reaches the intended population groups.
- **`B13.02.02.F04` — Message Understanding:** Message can be understood through available and usable support.
- **`B13.02.02.F05` — Engagement Follow-Up:** Engagement receives follow-up.

#### B13.02.03 — Preventive Intervention Evaluation
Preventive efforts can be reviewed for their effects and unintended consequences.

- **`B13.02.03.F01` — EvaluatedPreventionEffect:** Completed prevention programmes have defensible outcome assessments.
- **`B13.02.03.F02` — Baseline Risk Assessment:** Baseline risk is assessed, including in adverse conditions.
- **`B13.02.03.F03` — Intervention Reach Measurement:** The population reach of preventive interventions can be measured.
- **`B13.02.03.F04` — Prevention Outcome Review:** Prevention outcome is reviewed, with accessible and accurate supporting evidence.
- **`B13.02.03.F05` — Unintended Effect Recognition:** Unintended effect is recognised, with accessible and accurate supporting evidence.

### B13.03 — Public Health Protection & Response
Coordinate action for population-scale risks and events.

#### B13.03.01 — Public Health Threat Response Coordination
Population-scale health threats can trigger coordinated action by responsible parties.

- **`B13.03.01.F01` — Timely Coordinated Response:** Qualifying public health threats receive timely, documented multi-party responses.
- **`B13.03.01.F02` — Authority-to-Act Clarity:** Authority-to-act is clear, distinguishing permitted and nonconforming conditions.
- **`B13.03.01.F03` — Multi-Agency Response Alignment:** Multi-agency response alignment reaches and can be understood by the intended recipient.
- **`B13.03.01.F04` — Public Health Action Timing:** Public health action has its timing established.
- **`B13.03.01.F05` — Response Outcome Monitoring:** Response outcome can be monitored, with accessible and accurate supporting evidence.

#### B13.03.02 — Public Health Control Measure Management
Required public health measures can be organised, communicated and reviewed.

- **`B13.03.02.F01` — ReviewedProtectiveMeasures:** Active public health measures receive relevant effect and proportionality assessments.
- **`B13.03.02.F02` — Appropriate Control Measure Selection:** Appropriate control measure is selected.
- **`B13.03.02.F03` — Compliance and Uptake Awareness:** Compliance and uptake is made known, distinguishing permitted and nonconforming conditions.
- **`B13.03.02.F04` — Control Side-Effect Review:** Control side-effect is reviewed, with accessible and accurate supporting evidence.
- **`B13.03.02.F05` — Measure Cessation Decision:** Measure cessation has an explicit decision.

#### B13.03.03 — Population Risk Communication
Relevant communities can receive clear, timely and actionable public-health information.

- **`B13.03.03.F01` — UsablePublicRiskCommunication:** Public risk communications meet their comprehension and timeliness criteria.
- **`B13.03.03.F02` — Plain-Language Public Advice:** Plain-language public advice is available through available and usable support.
- **`B13.03.03.F03` — Communication Channel Coverage:** Communication channel coverage reaches and can be understood by the intended recipient.
- **`B13.03.03.F04` — Misinformation Correction:** Misinformation can be corrected.
- **`B13.03.03.F05` — Communication Effectiveness Feedback:** Communication effectiveness feedback reaches and can be understood by the intended recipient.

### B13.04 — Research Governance & Participant Protection
Establish ethical, legal and methodological conditions for health research.

#### B13.04.01 — Research Authority Establishment
Proposed research can be assessed and approved under applicable ethical and legal authority.

- **`B13.04.01.F01` — VerifiedResearchAuthority:** Studies have valid approvals and applicable conditions established before commencement.
- **`B13.04.01.F02` — Approved Research Purpose:** Approved research has an explicit purpose.
- **`B13.04.01.F03` — Ethics and Governance Clearance:** Ethics and governance has the applicable clearance, distinguishing permitted and nonconforming conditions.
- **`B13.04.01.F04` — Research Permission Currency:** Research permission is current, distinguishing permitted and nonconforming conditions.
- **`B13.04.01.F05` — Authority Breach Escalation:** Authority breach is escalated, distinguishing permitted and nonconforming conditions.

#### B13.04.02 — Research Participant Safeguarding
Participants' rights, information and wellbeing can be protected through research activity.

- **`B13.04.02.F01` — ProtectedResearchParticipation:** Research participant interactions meet the relevant consent and safeguarding conditions.
- **`B13.04.02.F02` — Meaningful Participant Consent:** Meaningful participant requires applicable consent, distinguishing permitted and nonconforming conditions.
- **`B13.04.02.F03` — Participant Risk Monitoring:** Participant risk can be monitored, including in adverse conditions.
- **`B13.04.02.F04` — Withdrawal Recognition:** Withdrawal is recognised, with accessible and accurate supporting evidence.
- **`B13.04.02.F05` — Adverse Event Response:** Adverse event receives an appropriate response, including in adverse conditions.

#### B13.04.03 — Research Change and Deviation Oversight
Significant amendments and deviations can receive appropriate review.

- **`B13.04.03.F01` — AccountableResearchChange:** Material research changes receive the required disposition before affected activity.
- **`B13.04.03.F02` — Material Protocol Change Approval:** Material protocol change has the applicable approval, distinguishing permitted and nonconforming conditions.
- **`B13.04.03.F03` — Deviation Recognition:** Deviation is recognised, with accessible and accurate supporting evidence.
- **`B13.04.03.F04` — Sponsor and Ethics Notification:** Sponsor and ethics notification reaches and can be understood by the intended recipient.
- **`B13.04.03.F05` — Deviation Resolution Trace:** Deviation resolution is traceable, with accessible and accurate supporting evidence.

#### B13.04.04 — Research Information Release Accountability
Protect participants and authorised research purposes through governed, reproducible information release.

*Enterprise contributions: E09.05, E08.08, E05.06; findings: R23.*

- **`B13.04.04.F01` — StudyPurposeApproved:** Research request is aligned with approved purpose and authority.
- **`B13.04.04.F02` — ParticipantRestrictionApplied:** Applicable participant restrictions are verified.
- **`B13.04.04.F03` — DatasetVersionKnown:** Provided research information is identified as a stable snapshot.
- **`B13.04.04.F04` — NoPartialRestrictedRelease:** Required but unauthorised data prevents improper extract release.
- **`B13.04.04.F05` — DisclosureTrailReviewable:** Released evidence includes selection and authority provenance.

### B13.05 — Health Research Execution
Conduct valid investigations and produce defensible findings.

#### B13.05.01 — Research Question and Study Design
Research can be framed into a valid, assessable study design.

- **`B13.05.01.F01` — ReviewableStudyDesign:** Study designs have sufficient question, method and outcome definition.
- **`B13.05.01.F02` — Research Objective Specificity:** Research objective is specific.
- **`B13.05.01.F03` — Method Suitability:** Method is suitable.
- **`B13.05.01.F04` — Sample and Bias Appraisal:** Sample and bias can be appraised.
- **`B13.05.01.F05` — Reproducible Study Protocol:** Study protocols support reproducible research.

#### B13.05.02 — Governed Research Evidence Collection
Study evidence can be collected consistently within the study's authority and methods.

- **`B13.05.02.F01` — Evidence Collected Under Valid Authority:** Research evidence is collected within documented protocol and permission boundaries.
- **`B13.05.02.F02` — Protocol-Conformant Evidence Capture:** Protocol-conformant evidence is captured, distinguishing permitted and nonconforming conditions.
- **`B13.05.02.F03` — Research Data Quality:** Research data meets the applicable quality requirements.
- **`B13.05.02.F04` — Participant Data Protection:** Participant data is protected.
- **`B13.05.02.F05` — Data Collection Lineage:** Data collection retains its lineage, with accessible and accurate supporting evidence.

#### B13.05.03 — Research Finding Validation
Analyses and conclusions can be checked for reliability and limitations.

- **`B13.05.03.F01` — DefensibleResearchFindings:** Research findings retain traceable methods, inputs and uncertainty.
- **`B13.05.03.F02` — Independent Analysis Verification:** Independent analysis are verified through available and usable support.
- **`B13.05.03.F03` — Statistical Uncertainty Disclosure:** Statistical uncertainty is disclosed, with accessible and accurate supporting evidence.
- **`B13.05.03.F04` — Reproducible Findings:** Reproducible findings are available.
- **`B13.05.03.F05` — Peer Review Trace:** Peer review is traceable, with accessible and accurate supporting evidence.

### B13.06 — Evidence Translation & Practice Improvement
Incorporate trustworthy findings into policy and healthcare practice.

#### B13.06.01 — Evidence Relevance Assessment
New findings can be reviewed for applicability to services and populations.

- **`B13.06.01.F01` — ApplicableEvidenceReview:** Research findings receive documented relevance and confidence assessments.
- **`B13.06.01.F02` — Applicability to Care Context:** Applicability to care has its relevant context.
- **`B13.06.01.F03` — Evidence Quality Appraisal:** Evidence quality can be appraised.
- **`B13.06.01.F04` — Conflicting Evidence Recognition:** Conflicting evidence is recognised, with accessible and accurate supporting evidence.
- **`B13.06.01.F05` — Evidence Currency:** Evidence is current, with accessible and accurate supporting evidence.

#### B13.06.02 — Evidence-Informed Practice Change
Findings can inform controlled changes to professional or service practice.

- **`B13.06.02.F01` — Measured Adoption and Effect:** Authorised practice changes retain evidence of uptake and evaluated effect.
- **`B13.06.02.F02` — Approved Practice Change:** Approved practice can be changed through relevant changes or disruptions, with recovery where required.
- **`B13.06.02.F03` — Change Communication:** Change is communicated through relevant changes or disruptions, with recovery where required.
- **`B13.06.02.F04` — Practice Adoption Evidence:** Practice adoption is evidenced, with accessible and accurate supporting evidence.
- **`B13.06.02.F05` — Change Effect Follow-Up:** Change effect receives follow-up through relevant changes or disruptions, with recovery where required.

#### B13.06.03 — Practice Learning Feedback
Outcomes of practice changes can inform subsequent guidance and learning.

- **`B13.06.03.F01` — ClosedLearningFeedback:** Designated practice improvements receive follow-up review and communicated lessons.
- **`B13.06.03.F02` — Frontline Learning Capture:** Frontline learning is captured.
- **`B13.06.03.F03` — Feedback to Evidence Owner:** Practice learning feedback reaches the responsible evidence owner, with accessible and accurate supporting evidence.
- **`B13.06.03.F04` — Learning-to-Change Linkage:** Learning-to-change is linked through relevant changes or disruptions, with recovery where required.
- **`B13.06.03.F05` — Repeated Learning Assessment:** Repeated learning is assessed through relevant changes or disruptions, with recovery where required.

## B14 — Participation, Independence & Support
People can pursue personally meaningful goals, independence and participation with appropriate assistance.

### B14.01 — Personal Goal & Preference Support
Help a participant articulate, review and change personally meaningful goals and choices.

#### B14.01.01 — Participant Goal Articulation
People can describe personally meaningful goals and priorities in their own context.

- **`B14.01.01.F01` — RecordedParticipantPriorities:** Participant priorities are accessible and self-expressed or appropriately supported.
- **`B14.01.01.F02` — Participant-Led Goal Expression:** Participant-led goal can be expressed.
- **`B14.01.01.F03` — Accessible Goal Conversation:** Accessible goal conversation is supported through available and usable support.
- **`B14.01.01.F04` — Goal Priority Clarity:** Goal priority is clear.
- **`B14.01.01.F05` — Supporter Contribution Boundary:** Supporter contribution has an explicit boundary.

#### B14.01.02 — Participant Goal Review
Goals can be revisited and changed as aspirations or circumstances evolve.

- **`B14.01.02.F01` — Goals Reviewed with the Participant:** Agreed goal reviews involve meaningful participant participation.
- **`B14.01.02.F02` — Participant-Reported Progress:** Participants can report progress against their goals, with accessible and accurate supporting evidence.
- **`B14.01.02.F03` — Changed Aspiration Recognition:** Changed aspiration is recognised, with accessible and accurate supporting evidence.
- **`B14.01.02.F04` — Goal Achievability Review:** Goal achievability is reviewed, with accessible and accurate supporting evidence.
- **`B14.01.02.F05` — Agreed Goal Update:** Agreed goal is updated.

#### B14.01.03 — Preference-Led Support Direction
Support intentions can be aligned with expressed preferences without presuming clinical treatment.

- **`B14.01.03.F01` — PreferenceAlignedSupport:** Support plans align with relevant current participant preferences.
- **`B14.01.03.F02` — Current Preference Accessibility:** Current preference is accessible and usable.
- **`B14.01.03.F03` — Choice Alignment of Supports:** Choice alignment of supports is supported through available and usable support.
- **`B14.01.03.F04` — Refusal and Withdrawal Respect:** Refusal and withdrawal is respected.
- **`B14.01.03.F05` — Preference Change Response:** Preference change receives an appropriate response through available and usable support.

### B14.02 — Support Needs & Circumstances Assessment
Determine what assistance or accommodation would help achieve intended participation outcomes.

#### B14.02.01 — Everyday Support Needs Understanding
Assistance needs can be evaluated across daily activities and life circumstances.

- **`B14.02.01.F01` — UnderstoodDailySupportNeeds:** Support needs assessments address the agreed functioning and participation areas.
- **`B14.02.01.F02` — Daily Living Need Coverage:** Daily living need is covered.
- **`B14.02.01.F03` — Strength and Independence Recognition:** Strength and independence is recognised through available and usable support.
- **`B14.02.01.F04` — Cultural and Personal Context:** Cultural and personal has its relevant context.
- **`B14.02.01.F05` — Support Need Explanation:** Support need is explained.

#### B14.02.02 — Environmental and Accessibility Needs Assessment
Barriers in physical, social or communication environments can be identified.

- **`B14.02.02.F01` — RecognisedParticipationBarriers:** Participation context assessments identify relevant barriers and accommodations.
- **`B14.02.02.F02` — Accessibility Barrier Recognition:** Accessibility barrier is recognised through available and usable support.
- **`B14.02.02.F03` — Suitable Accommodation Options:** Suitable accommodation options are available.
- **`B14.02.02.F04` — Environment Change Recommendation:** Participation assessments can recommend relevant environmental changes through relevant changes or disruptions, with recovery where required.
- **`B14.02.02.F05` — Barrier Resolution Follow-Up:** Barrier resolution receives follow-up through available and usable support.

#### B14.02.03 — Changing Circumstances Reassessment
Support needs can be reassessed after material changes in circumstances.

- **`B14.02.03.F01` — ResponsiveSupportReassessment:** Qualifying changes prompt timely support review.
- **`B14.02.03.F02` — Change Trigger Sensitivity:** Change trigger can be recognised through relevant changes or disruptions, with recovery where required.
- **`B14.02.03.F03` — Timely Support Reassessment:** Support needs are reassessed within the applicable context-defined interval.
- **`B14.02.03.F04` — Updated Support Need Communication:** Updated support need is communicated through relevant changes or disruptions, with recovery where required.
- **`B14.02.03.F05` — Reassessment Ownership:** Reassessment has an identifiable responsible owner.

### B14.03 — Choice & Support Arrangement
Enable selection, initiation, modification or cessation of appropriate supports.

#### B14.03.01 — Support Option Exploration
People can understand and compare feasible support options.

- **`B14.03.01.F01` — InformedSupportChoice:** Support choices have accessible option information and recorded preferences.
- **`B14.03.01.F02` — Comparable Support Alternatives:** Comparable support alternatives are available through relevant changes or disruptions, with recovery where required.
- **`B14.03.01.F03` — Independent Advice Availability:** Independent advice is available and usable.
- **`B14.03.01.F04` — Choice Consequence Clarity:** Choice consequence is clear through available and usable support.
- **`B14.03.01.F05` — Unavailable Option Explanation:** Unavailable option is explained.

#### B14.03.02 — Chosen Support Arrangement
Supports can be established consistently with the person's choices and applicable authority.

- **`B14.03.02.F01` — Support Consistent with Recorded Choice:** Support arrangements are consistent with the participant's current valid choice.
- **`B14.03.02.F02` — ParticipantDirectedSupportChange:** Requested support changes are dispositioned consistently with preferences and applicable authority.
- **`B14.03.02.F03` — Participant Approval Visibility:** Participant approval is visible, distinguishing permitted and nonconforming conditions.
- **`B14.03.02.F04` — Provider Commitment Confirmation:** Provider commitment confirmation reaches and can be understood by the intended recipient.
- **`B14.03.02.F05` — Support Schedule Clarity:** Support schedule is clear.
- **`B14.03.02.F06` — Arrangement Revision Access:** Arrangement revision can be accessed.

#### B14.03.03 — Support Change or Cessation
Support arrangements can change or end in an orderly way when directed or justified.

- **`B14.03.03.F01` — ControlledSupportChange:** Support changes have clear participation, responsibility and transition arrangements.
- **`B14.03.03.F02` — Participant Choice Recognition:** Participant choice is recognised through available and usable support.
- **`B14.03.03.F03` — Safe Support Transition:** Safe support can be transitioned, including in adverse conditions.
- **`B14.03.03.F04` — Cessation Notice Clarity:** Cessation notice is clear.
- **`B14.03.03.F05` — Unmet Support Risk Detection:** Unmet support risk is detected, including in adverse conditions.

### B14.04 — Ongoing Support Coordination
Maintain coherence of supports across providers, settings and changes in circumstances.

#### B14.04.01 — Multi-Provider Support Continuity
Multiple support providers can coordinate without losing the person's chosen objectives.

- **`B14.04.01.F01` — CoherentSupportContinuity:** Shared support maintains compatible goals and responsibilities.
- **`B14.04.01.F02` — Shared Support Objective:** Shared support objective reaches and can be understood by the intended recipient.
- **`B14.04.01.F03` — Provider Handover Completeness:** Provider handover is complete.
- **`B14.04.01.F04` — Cross-Provider Status Visibility:** Cross-provider status is visible, with accessible and accurate supporting evidence.
- **`B14.04.01.F05` — Unfilled Support Obligation:** Unfilled support has an explicit obligation.

#### B14.04.02 — Support Obligation Follow-Up
Agreed support commitments can be monitored until fulfilled or dispositioned.

- **`B14.04.02.F01` — VisibleSupportCommitments:** Material support commitments have a current accountable status.
- **`B14.04.02.F02` — Support Action Tracking:** Support action can be tracked.
- **`B14.04.02.F03` — Obligation Owner Clarity:** Obligation owner is clear.
- **`B14.04.02.F04` — Overdue Support Escalation:** Overdue support escalation reaches and can be understood by the intended recipient.
- **`B14.04.02.F05` — Completed Support Confirmation:** Completed support is confirmed.

#### B14.04.03 — Support Transition Coordination
Support can be maintained or deliberately changed across life-stage, service or location transitions.

- **`B14.04.03.F01` — SupportedLifeTransition:** Life transitions have confirmed continuation or adjusted support arrangements.
- **`B14.04.03.F02` — Receiving Support Acceptance:** Receiving support has an explicit acceptance outcome.
- **`B14.04.03.F03` — Changing Support Plan Communication:** Changing support plan communication reaches and can be understood by the intended recipient.
- **`B14.04.03.F04` — Transition Continuity Check:** Transition continuity is checked through relevant changes or disruptions, with recovery where required.
- **`B14.04.03.F05` — Transition Exception Resolution:** Transition exception is resolved through relevant changes or disruptions, with recovery where required.

#### B14.04.04 — Participant-Centred Task Coordination
Support tasks and follow-up involving participants or carers without treating participants as patients or automatically granting proxy authority.

- **`B14.04.04.F01` — ParticipantRoleDistinct:** Participant identity and representation authority are explicit.
- **`B14.04.04.F02` — CarerAuthorityChecked:** A carer cannot claim protected tasks solely because they are a contact.
- **`B14.04.04.F03` — LongDurationSupportWork:** Support tasks remain accountable when fulfilled outside the digital system.
- **`B14.04.04.F04` — SupportedParticipationEnabled:** Relevant participants can report progress through authorised pathways.
- **`B14.04.04.F05` — TaskContentDisclosureAppropriate:** Task descriptions and attachments obey applicable compartment and disclosure limits.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: DRP-PR, DRP-SEC. *Release horizon:* Unscheduled.

### B14.05 — Independent Living & Community Participation
Enable access to activities, environments, relationships and resources needed for everyday participation.

#### B14.05.01 — Independent Living Assistance
People can obtain relevant support to exercise everyday independence.

- **`B14.05.01.F01` — SupportedEverydayIndependence:** Agreed independent-living outcomes receive achievement review.
- **`B14.05.01.F02` — Independence-Preserving Assistance:** Independence-preserving assistance is supported through available and usable support.
- **`B14.05.01.F03` — Assistance Suitability Review:** Assistance suitability is reviewed, with accessible and accurate supporting evidence.
- **`B14.05.01.F04` — Supported Daily Choice:** Supported daily choice is supported through available and usable support.
- **`B14.05.01.F05` — Growing Independence Recognition:** Growing independence is recognised through available and usable support.

#### B14.05.02 — Community Participation Support
Barriers to chosen community activities and relationships can be addressed.

- **`B14.05.02.F01` — Participation Outcome Reviewed:** Supported participation goals have an assessed outcome.
- **`B14.05.02.F02` — Desired Participation Opportunity:** Desired participation opportunity is supported through available and usable support.
- **`B14.05.02.F03` — Participation Barrier Reduction:** Participation barrier is reduced through available and usable support.
- **`B14.05.02.F04` — Accessible Community Connection:** Accessible community can be connected through available and usable support.
- **`B14.05.02.F05` — Participant-Reported Value:** Participants can report the value of supported participation, with accessible and accurate supporting evidence.

#### B14.05.03 — Accessible Participation Arrangement
Environments and services can be adapted to enable equitable participation.

- **`B14.05.03.F01` — EffectiveParticipationAccommodation:** Identified participation barriers are addressed or transparently dispositioned.
- **`B14.05.03.F02` — Accessibility Adjustment Availability:** Accessibility adjustment is available and usable.
- **`B14.05.03.F03` — Inclusive Participation Timing:** Inclusive participation has its timing established through available and usable support.
- **`B14.05.03.F04` — Support for Communication:** Support for communication reaches and can be understood by the intended recipient.
- **`B14.05.03.F05` — Participation Arrangement Confirmation:** Participation arrangement is confirmed through available and usable support.

### B14.06 — Participant Safeguarding & Supported Decision Participation
Respect wishes, prevent avoidable harm, and support decisions or representation within appropriate authority.

#### B14.06.01 — Participant Safeguarding
Safeguarding concerns can be identified and managed while respecting autonomy.

- **`B14.06.01.F01` — ActionedSafeguardingConcern:** Qualifying safeguarding concerns receive appropriate review and protective action.
- **`B14.06.01.F02` — Abuse and Neglect Recognition:** Abuse and neglect is recognised, including in adverse conditions.
- **`B14.06.01.F03` — Safe Concern Reporting:** Safe concern is reported, including in adverse conditions.
- **`B14.06.01.F04` — Appropriate Protective Response:** Appropriate protective receives an appropriate response.
- **`B14.06.01.F05` — Safeguarding Follow-Up:** Safeguarding receives follow-up.

#### B14.06.02 — Supported Decision Participation
People can receive support to participate in decisions without their preferences being silently replaced.

- **`B14.06.02.F01` — MeaningfulSupportedDecision:** Supported decisions retain evidence of participation and authority boundaries.
- **`B14.06.02.F02` — Accessible Decision Information:** Accessible decision information is available through available and usable support.
- **`B14.06.02.F03` — Appropriate Decision Support:** Decision support is appropriate.
- **`B14.06.02.F04` — Participant Will and Preference:** Participant will and preference is supported through available and usable support.
- **`B14.06.02.F05` — Coercion and Conflict Recognition:** Coercion and conflict is recognised, including in adverse conditions.

#### B14.06.03 — Advocacy and Independent Assistance
People can access advocacy or independent support when concerns or disputes arise.

- **`B14.06.03.F01` — AvailableIndependentAdvocacy:** Valid advocacy requests receive an appropriate connection or reasoned alternative.
- **`B14.06.03.F02` — Independent Advocate Availability:** Independent advocate is available and usable.
- **`B14.06.03.F03` — Advocacy Scope Clarity:** Advocacy scope is clear, distinguishing permitted and nonconforming conditions.
- **`B14.06.03.F04` — Advocacy Request Responsiveness:** Advocacy request is responsive.
- **`B14.06.03.F05` — Advocacy Outcome Communication:** Advocacy outcome communication reaches and can be understood by the intended recipient.

#### B14.06.04 — Participant Decision Authority Transparency
Protect a participant’s own decisions while keeping supporters, representatives and automated advice in their proper roles.

*Enterprise contributions: E07.02, E05.06, E09.04; findings: R11, R15, R22.*

- **`B14.06.04.F01` — ParticipantVoicePrimary:** Participant goals and choices are recorded distinctly from supporters.
- **`B14.06.04.F02` — SupporterAuthorityLimited:** Supporter may not exceed verified decision scope.
- **`B14.06.04.F03` — AIAdviceNotDecision:** Automated recommendations do not become authoritative participant decisions.
- **`B14.06.04.F04` — DisagreementResolutionAvailable:** Conflict between participant and representative has accountable escalation.
- **`B14.06.04.F05` — DecisionEvidenceAccessible:** Participant or authorised reviewer can examine decision basis.

## B15 — Health System Funding & Commissioning
Services and supports can be commissioned, purchased, financed and evaluated against population need.

### B15.01 — Health Needs-Based Commissioning
Identify the services/outcomes a population or covered group requires and arrange service provision.

#### B15.01.01 — Population Service Gap Commissioning
Commissioners can arrange services in response to established unmet population needs.

- **`B15.01.01.F01` — Commissioned Capacity Addresses an Identified Gap:** Commissioning decisions link to a documented service need.
- **`B15.01.01.F02` — Unmet Population Need Evidence:** Unmet population need is evidenced, with accessible and accurate supporting evidence.
- **`B15.01.01.F03` — Geographic Service Gap Visibility:** Geographic service gap is visible, with accessible and accurate supporting evidence.
- **`B15.01.01.F04` — Commissioning Priority Trace:** Commissioning priority is traceable, with accessible and accurate supporting evidence.
- **`B15.01.01.F05` — Gap Closure Verification:** Gap closure is verified.

#### B15.01.02 — Commissioning Outcome Specification
The intended public or service value of commissioning can be expressed and assessed.

- **`B15.01.02.F01` — MeasurableCommissionedOutcome:** Commissioned arrangements have defined assessable outcomes.
- **`B15.01.02.F02` — Commissioned Outcome Clarity:** Commissioned outcome is clear.
- **`B15.01.02.F03` — Measurable Service Expectations:** Commissioned service expectations are defined so their fulfilment can be assessed.
- **`B15.01.02.F04` — Provider Accountability Terms:** Provider accountability terms are explicit, with accessible and accurate supporting evidence.
- **`B15.01.02.F05` — Outcome Review Schedule:** Commissioned outcomes have an established review schedule, with accessible and accurate supporting evidence.

#### B15.01.03 — Provider Market and Capacity Assessment
Potential service supply can be evaluated against required coverage and capability.

- **`B15.01.03.F01` — EvidenceBasedProviderMarketView:** Commissioned service types have current provider capability and capacity evidence.
- **`B15.01.03.F02` — Viable Provider Market Visibility:** Viable provider market is visible, with accessible and accurate supporting evidence.
- **`B15.01.03.F03` — Capacity and Capability Assessment:** Capacity and capability is assessed.
- **`B15.01.03.F04` — Market Failure Risk:** Provider market failure risks can be identified, including in adverse conditions.
- **`B15.01.03.F05` — Commissioning Alternative Analysis:** Commissioning alternative can be analysed through relevant changes or disruptions, with recovery where required.

### B15.02 — Coverage & Entitlement Administration
Establish and maintain the funding terms under which people may access services or supports.

#### B15.02.01 — Covered Service Determination
A person can receive a reasoned decision about covered services or supports.

- **`B15.02.01.F01` — Explained Coverage Decision:** Coverage determinations have the correct policy basis and intelligible notice.
- **`B15.02.01.F02` — Coverage Rule Applicability:** Coverage rule has its applicability established.
- **`B15.02.01.F03` — Service Eligibility Clarity:** Service eligibility is clear, distinguishing permitted and nonconforming conditions.
- **`B15.02.01.F04` — Coverage Rationale Record:** Coverage rationale is recorded, with accessible and accurate supporting evidence.
- **`B15.02.01.F05` — Coverage Boundary Appeal:** Coverage boundary can be appealed.

#### B15.02.02 — Coverage Condition Maintenance
Applicable funding coverage terms and changes can be maintained accurately.

- **`B15.02.02.F01` — CurrentCoverageConditions:** Coverage cases are assessed against the correct current terms.
- **`B15.02.02.F02` — Coverage Change Effective Dating:** Coverage changes identify their effective period through relevant changes or disruptions, with recovery where required.
- **`B15.02.02.F03` — Policy Change Propagation:** Policy change propagation is identifiable, distinguishing permitted and nonconforming conditions.
- **`B15.02.02.F04` — Exceptional Condition Treatment:** Exceptional coverage conditions receive an explicit treatment, distinguishing permitted and nonconforming conditions.
- **`B15.02.02.F05` — Coverage Condition History:** Coverage condition retains its history, distinguishing permitted and nonconforming conditions.

#### B15.02.03 — Coverage Dispute Review
Contested coverage decisions can receive an accountable review and disposition.

- **`B15.02.03.F01` — ReviewableCoverageAppeal:** Coverage appeals retain a documented basis, action and notification.
- **`B15.02.03.F02` — Fair Review Access:** Fair review can be accessed, with accessible and accurate supporting evidence.
- **`B15.02.03.F03` — Evidence-Grounded Dispute Decision:** Evidence-grounded dispute has an explicit decision, with accessible and accurate supporting evidence.
- **`B15.02.03.F04` — Decision Timeliness:** Decision meets the applicable timing conditions.
- **`B15.02.03.F05` — Coverage Review Communication:** Coverage review is communicated, with accessible and accurate supporting evidence.

### B15.03 — Health Service Purchasing & Contract Stewardship
Arrange and oversee the purchase of services from providers.

#### B15.03.01 — Provider Service Purchasing
A purchaser can establish service arrangements consistent with required outcomes and safeguards.

- **`B15.03.01.F01` — OutcomeAlignedServicePurchase:** Service purchases have defined service and quality obligations.
- **`B15.03.01.F02` — Transparent Purchasing Criteria:** Transparent purchasing criteria are explicit, with accessible and accurate supporting evidence.
- **`B15.03.01.F03` — Procurement Suitability Review:** Procurement suitability is reviewed, with accessible and accurate supporting evidence.
- **`B15.03.01.F04` — Purchased Service Specification:** Purchased service has an explicit specification.
- **`B15.03.01.F05` — Purchasing Decision Audit:** Service purchasing decisions can be audited, with accessible and accurate supporting evidence.

#### B15.03.02 — Provider Contract Performance Oversight
Purchased service commitments can be assessed against contractual responsibilities.

- **`B15.03.02.F01` — AssessedProviderCommitments:** Selected provider contracts receive current, evidence-backed performance evaluation.
- **`B15.03.02.F02` — Contract Outcome Evidence:** Contract outcome is evidenced, distinguishing permitted and nonconforming conditions.
- **`B15.03.02.F03` — Contract Compliance Visibility:** Contract compliance is visible, distinguishing permitted and nonconforming conditions.
- **`B15.03.02.F04` — Corrective Supplier Engagement:** Corrective supplier can be engaged.
- **`B15.03.02.F05` — Contract Performance Trend:** Provider contract performance trends can be identified, distinguishing permitted and nonconforming conditions.

#### B15.03.03 — Contract Modification and Exit
Changed or ending purchases can be managed with continuity safeguards.

- **`B15.03.03.F01` — ManagedCommissioningTransition:** Contract transitions receive impact review and a service-continuity disposition.
- **`B15.03.03.F02` — Change Impact Assessment:** Change impact is assessed through relevant changes or disruptions, with recovery where required.
- **`B15.03.03.F03` — Contract Change Authority:** Contract change has an established authority basis, distinguishing permitted and nonconforming conditions.
- **`B15.03.03.F04` — Service Continuity on Exit:** Commissioning exit preserves the applicable service continuity obligations through relevant changes or disruptions, with recovery where required.
- **`B15.03.03.F05` — Contract Closure Accountability:** Contract closure has an accountable party, distinguishing permitted and nonconforming conditions.

### B15.04 — Claims & Funding Accountability
Account for qualifying service activity and applicable payment arrangements.

#### B15.04.01 — Service Claim Assessment
Claims can be evaluated against eligible activity and applicable terms.

- **`B15.04.01.F01` — CorrectClaimDisposition:** Claims are correctly paid, rejected or referred for review.
- **`B15.04.01.F02` — TransparentClaimExplanation:** Claim decisions provide an intelligible explanation.
- **`B15.04.01.F03` — Valid Service Claim Evidence:** Valid service claim is evidenced.
- **`B15.04.01.F04` — Claim Rule Conformance:** Claim rule conforms to the applicable requirements, distinguishing permitted and nonconforming conditions.
- **`B15.04.01.F05` — Duplicate Claim Recognition:** Duplicate claim is recognised.
- **`B15.04.01.F06` — Explained Claim Disposition:** Explained claim has an explicit disposition, with accessible and accurate supporting evidence.

#### B15.04.02 — Payment and Funding Reconciliation
Funding movements can be reconciled to the relevant service obligations.

- **`B15.04.02.F01` — ReconciledServiceFunding:** Funding transactions retain an explainable underlying service basis.
- **`B15.04.02.F02` — Payment-to-Obligation Matching:** Payment-to-obligation is matched.
- **`B15.04.02.F03` — Unmatched Payment Detection:** Unmatched payment is detected.
- **`B15.04.02.F04` — Timely Financial Correction:** Service funding information can be corrected within the applicable context-defined interval.
- **`B15.04.02.F05` — Reconciliation Audit Evidence:** Reconciliation audit is evidenced.

#### B15.04.03 — Funding Exception Investigation
Suspected incorrect, duplicate or unsupported claims can be investigated.

- **`B15.04.03.F01` — AccountableFundingException:** Qualifying funding exceptions receive documented review and appropriate action.
- **`B15.04.03.F02` — Exception Risk Prioritisation:** Exception risk can be prioritised, including in adverse conditions.
- **`B15.04.03.F03` — Authorised Investigation Access:** Authorised investigation can be accessed, distinguishing permitted and nonconforming conditions.
- **`B15.04.03.F04` — Evidence-Backed Finding:** Evidence-backed has an explicit finding, with accessible and accurate supporting evidence.
- **`B15.04.03.F05` — Funding Exception Resolution:** Funding exception is resolved.

#### B15.04.04 — Funding Information Accountability Boundary
Support trustworthy reporting and reconciliation while identifying where claims adjudication and payment occur outside the digital contribution.

*Enterprise contributions: E09.03, E09.04, E08.08; findings: R12.*

- **`B15.04.04.F01` — FinancialSourceDefined:** Reported amounts link to qualified source systems and data.
- **`B15.04.04.F02` — PaymentStatusNotInvented:** An information report does not assert actual remittance without source evidence.
- **`B15.04.04.F03` — ClaimsAuthorityKnown:** Decision-making responsibility for claims is identifiable.
- **`B15.04.04.F04` — ReconciliationExceptionVisible:** Unmatched or disputed funding records remain explicit.
- **`B15.04.04.F05` — DataDisclosurePermitted:** Funding and person-level protected data obey applicable access constraints.

### B15.05 — Funding Outcomes & Equity Review
Evaluate whether funding arrangements meet intended access, quality and fairness goals.

#### B15.05.01 — Funded Service Outcome Evaluation
Funding arrangements can be assessed for the value and outcomes delivered.

- **`B15.05.01.F01` — EvaluatedFundingEffect:** Designated funding programmes have current outcome evidence.
- **`B15.05.01.F02` — Funding-to-Service Linkage:** Funding-to-service is linked.
- **`B15.05.01.F03` — Outcome Quality Comparison:** Outcome quality can be compared.
- **`B15.05.01.F04` — Value-for-Money Evidence:** Value-for-money is evidenced, with accessible and accurate supporting evidence.
- **`B15.05.01.F05` — Commissioning Feedback:** Commissioning feedback reaches and can be understood by the intended recipient.

#### B15.05.02 — Equitable Funding Access Review
Differences in access or outcomes across eligible groups can be identified and examined.

- **`B15.05.02.F01` — EvidencedFundingEquity:** Designated population groups have interpretable access and funding equity analysis.
- **`B15.05.02.F02` — Population Funding Distribution:** Population funding can be distributed.
- **`B15.05.02.F03` — Access Disparity Recognition:** Access disparity is recognised, with accessible and accurate supporting evidence.
- **`B15.05.02.F04` — Equity Impact of Funding:** The equity impact of funding can be assessed.
- **`B15.05.02.F05` — Equity Review Response:** Equity review receives an appropriate response, with accessible and accurate supporting evidence.

#### B15.05.03 — Funding Model Adjustment
Funding terms can be reviewed and revised in response to defensible evidence.

- **`B15.05.03.F01` — ResponsiveFundingRevision:** Funding changes retain documented rationale and outcome follow-up.
- **`B15.05.03.F02` — Evidence-Based Adjustment Case:** Funding adjustments have an evidence-supported case, with accessible and accurate supporting evidence.
- **`B15.05.03.F03` — Stakeholder Impact Analysis:** Stakeholder impact can be analysed.
- **`B15.05.03.F04` — Adjustment Approval Clarity:** Adjustment approval is clear, distinguishing permitted and nonconforming conditions.
- **`B15.05.03.F05` — Revised Funding Evaluation:** Revised funding is evaluated.

# Enterprise Capabilities
## E01 — Governed Information Custody
The system can hold, preserve, recover and account for governed information.

### E01.01 — Governed Information Persistence
Preserve accepted information reliably with its governance context.

#### E01.01.01 — Durable Information Acceptance
Accepted governed information remains retrievable following an acknowledgement.

- **`E01.01.01.F01` — AcceptedInformationRecoverable:** Acknowledged information units can be retrieved intact after interruption.
- **`E01.01.01.F02` — PersistenceBeforeAcknowledgement:** Durable persistence is evidenced before a durable-acceptance acknowledgement is issued.
- **`E01.01.01.F03` — Confirmed Acceptance Identity:** Confirmed acceptance has an identifiable identity.
- **`E01.01.01.F04` — Recovery of Accepted State:** Recovery of accepted has an explicit state through relevant changes or disruptions, with recovery where required.
- **`E01.01.01.F05` — Incomplete Acceptance Rejection:** Incomplete acceptance is rejected.
- **`E01.01.01.F06` — Acceptance Outcome Visibility:** Acceptance outcome is visible, with accessible and accurate supporting evidence.

#### E01.01.02 — Controlled Information Change
Changes to held information respect the required governing conditions.

- **`E01.01.02.F01` — CustodyChangeIntegrity:** In-scope information changes receive the correct governed acceptance or rejection outcome.
- **`E01.01.02.F02` — DurableChangeOutcome:** Accepted information changes remain recoverable after the specified disruption.
- **`E01.01.02.F03` — Governed Change Preconditions:** Information changes coordinated through Mneme and Mnemosyne satisfy the applicable governing preconditions across their interaction, including appropriate handling and reporting of rejected changes.
- **`E01.01.02.F04` — Authorised Change Attribution:** Authorised change is attributable, distinguishing permitted and nonconforming conditions.
- **`E01.01.02.F05` — Concurrent Change Conflict Detection:** Concurrent change conflict is detected through relevant changes or disruptions, with recovery where required.
- **`E01.01.02.F06` — Rejected Change Preservation:** Rejected changes leave previously governed information intact through relevant changes or disruptions, with recovery where required.

#### E01.01.03 — Governed Content Custody
Content remains available in an appropriate storage context without constraining storage technology.

- **`E01.01.03.F01` — ContentAvailability:** Required information content is returned within the agreed availability conditions.
- **`E01.01.03.F02` — Content Integrity Verification:** Content integrity is verified.
- **`E01.01.03.F03` — Controlled Content Retrieval:** Controlled content can be retrieved.
- **`E01.01.03.F04` — Content Custody Accounting:** Content custody is accounted for.
- **`E01.01.03.F05` — Content Disposition Protection:** Content disposition is protected.

#### E01.01.04 — Authoritative Commitment Outcome
Report a uniquely interpretable acceptance, rejection, absence or indeterminate disposition for an attempted durable change.

*Demand: B10.01, B10.07, B11.05; evidence: MN, PERSIST, IU; findings: R06, R16.*

- **`E01.01.04.F01` — AcceptedStateVerified:** Every reported committed result is read back with the same governed identity and version.
- **`E01.01.04.F02` — RejectedStateUnchanged:** Rejected changes leave the authoritative version and integrity state unchanged.
- **`E01.01.04.F03` — OutcomeUncertaintyExplicit:** Interrupted commits produce an indeterminate outcome where success is not established.
- **`E01.01.04.F04` — ReplayCorrelationPreserved:** Repeated requests are correlated to the original disposition without false duplicate acceptance.
- **`E01.01.04.F05` — CommitEvidenceResolvable:** The commit response carries enough references to retrieve authoritative disposition evidence.

#### E01.01.05 — Independent Persistence Custody
Provide governed durable custody without binding authoritative identity or lifecycle to a storage provider or cache.

*Demand: B10.01, B10.06; evidence: PERSIST, DIST, IU; findings: R05, R20.*

- **`E01.01.05.F01` — ProviderIdentityIndependence:** Replacing a provider leaves governed identity and version references resolvable.
- **`E01.01.05.F03` — UnknownProviderOutcomeReported:** Provider timeouts produce a bounded uncertain outcome with reconciliation route.
- **`E01.01.05.F04` — DomainInstanceIsolation:** A failure in one persistence domain does not silently change another domain state.
- **`E01.01.05.F05` — CustodyEvidenceTraceable:** Stored unit, version and provider event can be correlated without exposing provider IDs as authoritative.

### E01.02 — Information Version & History Management
Retain and retrieve the relevant versions and histories of information.

#### E01.02.01 — Historic Version Retrieval
Consumers can request the particular preserved state of information that they need.

- **`E01.02.01.F01` — CorrectVersionRetrieval:** correct immutable versions returned for valid version requests.
- **`E01.02.01.F02` — Explicit Version Selection:** Explicit version is selected.
- **`E01.02.01.F03` — Version-Specific Information Retrieval:** Consumers can retrieve the precise specified information version, preserving its content and historical retrieval context.
- **`E01.02.01.F04` — Historic Retrieval Authorisation:** Historic retrieval has the applicable authorisation, distinguishing permitted and nonconforming conditions.
- **`E01.02.01.F05` — Unavailable Version Explanation:** Unavailable version is explained.

#### E01.02.02 — Information History Navigation
Consumers can understand how a managed information record has changed over time.

- **`E01.02.02.F01` — CompleteVersionLineage:** Version histories retain resolvable predecessor and successor context.
- **`E01.02.02.F02` — Chronological Change Visibility:** Chronological change is visible, with accessible and accurate supporting evidence.
- **`E01.02.02.F03` — Version Relationship Completeness:** Version relationship is complete.
- **`E01.02.02.F04` — Historic Source Attribution:** Historic source is attributable, with accessible and accurate supporting evidence.
- **`E01.02.02.F05` — History Navigation Continuity:** History navigation is maintained through available and usable support.

#### E01.02.03 — Immutable Version Preservation
Preserved versions remain unchanged through later updates.

- **`E01.02.03.F01` — PreservedVersionIntegrity:** Preserved information versions match their recorded integrity evidence.
- **`E01.02.03.F02` — Unauthorized Mutation Detection:** Unauthorized mutation is detected, including in adverse conditions.
- **`E01.02.03.F03` — Version Specification Integrity:** The identity, metadata, specification and relationships of a preserved information version retain verifiable integrity independently of current content availability.
- **`E01.02.03.F04` — Integrity After Migration:** Integrity after can be migrated.
- **`E01.02.03.F05` — Preserved History Recovery:** Preserved history can be recovered, with accessible and accurate supporting evidence.

#### E01.02.04 — Versioned Correction & Qualification
Correct or qualify governed information while preserving prior versions, references and immutable history constraints.

*Demand: B10.02, B10.07, B11.03; evidence: IU, MN, PERSIST; findings: R05, R09.*

- **`E01.02.04.F01` — PriorVersionRetained:** Correction leaves previous immutable versions retrievable.
- **`E01.02.04.F02` — CorrectionLineageLinked:** Every correction identifies its antecedent and reason where required.
- **`E01.02.04.F03` — HistoricalReferenceStable:** References to an earlier version continue to resolve to that earlier version.
- **`E01.02.04.F04` — ConflictingCorrectionDetected:** Competing updates identify version or state conflict without silent overwrite.
- **`E01.02.04.F05` — QualifiedRecordVisible:** Affected readers can distinguish original content from later correction or qualification.

### E01.03 — Information Integrity Management
Verify and report whether information remains intact and attributable.

#### E01.03.01 — Information Integrity Verification
The system can demonstrate whether held information remains unaltered.

- **`E01.03.01.F01` — VerifiedInformationIntegrity:** Information integrity can be verified against valid integrity evidence.
- **`E01.03.01.F02` — IntegrityFailureDetection:** known corruption cases detected during prescribed testing.
- **`E01.03.01.F03` — End-to-End Integrity Evidence:** End-to-end integrity is evidenced.
- **`E01.03.01.F04` — Qualified Verification Result:** Qualified verification has an explicit result.
- **`E01.03.01.F05` — Verification Failure Alerting:** Verification failure alerting reaches and can be understood by the intended recipient.
- **`E01.03.01.F06` — Repeatable Integrity Assessment:** Repeatable integrity is assessed.

#### E01.03.02 — Information Origin Preservation
Preserve and associate provenance for governed information and events, **including provenance captured at external acquisition boundaries**. Record available original-source identity, acquisition context, source attribution and relevant temporal information, distinguishing externally supplied evidence from Harmonia-generated observations and transformations. Preserve missing, uncertain or unverified provenance as explicitly qualified information.

- **`E01.03.02.F01` — PreservedSourceContext:** Governed information retains its required origin and contextual evidence.
- **`E01.03.02.F02` — Source Identity Continuity:** Source identity is maintained.
- **`E01.03.02.F03` — Acquisition Context Preservation:** Acquisition context is preserved through relevant changes or disruptions, with recovery where required.
- **`E01.03.02.F04` — Transformation Lineage:** Transformation retains its lineage, with accessible and accurate supporting evidence.
- **`E01.03.02.F05` — Uncertain Origin Qualification:** Uncertain origin is qualified.

#### E01.03.03 — Integrity Exception Reporting
Consumers can discover and investigate information-integrity concerns.

- **`E01.03.03.F01` — ReportedIntegrityExceptions:** Detected integrity exceptions are made available within the prescribed conditions.
- **`E01.03.03.F02` — Actionable Corruption Notification:** Actionable corruption notification reaches and can be understood by the intended recipient.
- **`E01.03.03.F03` — Affected Information Identification:** Affected information is identified.
- **`E01.03.03.F04` — Integrity Incident Trace:** Integrity incident is traceable.
- **`E01.03.03.F05` — Resolution Evidence:** Resolution is evidenced, with accessible and accurate supporting evidence.

#### E01.03.04 — Cross-Representation Integrity
Preserve identity, relationship and validity assertions across representation changes or independent derived Information Units.

*Demand: B10.02, B10.05, B13.05; evidence: IU, REF, MODEL; findings: R05, R09.*

- **`E01.03.04.F01` — RepresentationBindingValid:** Exactly one governed representation is associated with the Information Unit being tested.
- **`E01.03.04.F02` — DerivationLineageIntact:** Separately governed derivatives retain a traceable source relationship.
- **`E01.03.04.F03` — MetadataDomainSeparation:** Container, content and Information Unit metadata remain distinguishable.
- **`E01.03.04.F04` — IntegrityMismatchReported:** Corrupted content or incompatible metadata produces an attributable exception.
- **`E01.03.04.F05` — NativeRepresentationTraceable:** Preserved native content remains interpretable with its source representation definition.

#### E01.03.05 — Custody & Reference Consistency Assessment
Assess whether references, relationships and authoritative custody agree after changes, migration or partial failure.

*Demand: B10.02, B10.07, B12.04; evidence: IU, DIST, RES; findings: R05, R20, R24.*

- **`E01.03.05.F01` — OrphanReferenceDetected:** Broken governed references are detected in a sampled migration.
- **`E01.03.05.F02` — RelationshipEndpointsVerified:** Relationship endpoints resolve to existing governed identities or explicit tombstone states.
- **`E01.03.05.F03` — ReplicaDivergenceReported:** Detected divergent authoritative copies are reported without silent resolution.
- **`E01.03.05.F04` — IntegrityRepairTraceable:** Corrective action retains the original exception and remediation evidence.
- **`E01.03.05.F05` — AssessmentRepeatable:** Repeated assessment of an unchanged snapshot yields equivalent findings.

### E01.04 — Information Retention & Recovery
Retain, archive, recover and restore governed information in accordance with obligation.

#### E01.04.01 — Governed Information Retention
Information remains available for the duration and conditions required.

- **`E01.04.01.F01` — RetentionConformance:** Records are retained in accordance with their applicable obligations.
- **`E01.04.01.F02` — Retention Obligation Recognition:** Retention obligation is recognised, with accessible and accurate supporting evidence.
- **`E01.04.01.F03` — Retention Hold Protection:** Information subject to an applicable retention hold remains preserved and protected for the duration and under the conditions of that hold, preventing unauthorised loss, alteration or retirement that would violate the governing obligation.
- **`E01.04.01.F04` — Retained Content Accessibility:** Retained content is accessible and usable.
- **`E01.04.01.F05` — Retention Status Disclosure:** Retention status is disclosed, with accessible and accurate supporting evidence.

#### E01.04.02 — Archived Information Retrieval
Retained information can be obtained from archival custody when authorised.

- **`E01.04.02.F01` — ArchiveRetrievalSuccess:** Authorised archive requests return the correct information versions.
- **`E01.04.02.F02` — Archive Reference Validity:** Archive reference is valid.
- **`E01.04.02.F03` — Archived Version Fidelity:** Archived version is faithfully preserved.
- **`E01.04.02.F04` — Governed Archive Access:** Governed archive can be accessed, distinguishing permitted and nonconforming conditions.
- **`E01.04.02.F05` — Archive Retrieval Latency:** Authorised archive retrieval completes within the applicable context-defined interval.

#### E01.04.03 — Custody Recovery
Governed information can be restored after a specified loss or outage.

- **`E01.04.03.F01` — VerifiedCustodyRecovery:** Custody recovery restores the required information and its context under the prescribed recovery conditions.
- **`E01.04.03.F02` — Restored Version Completeness:** Restored version is complete.
- **`E01.04.03.F03` — Recoverable Governance Context:** Recoverable governance has its relevant context, distinguishing permitted and nonconforming conditions.
- **`E01.04.03.F04` — Recovery Point Evidence:** Recovery point is evidenced, with accessible and accurate supporting evidence.
- **`E01.04.03.F05` — Recovery Outcome Verification:** Recovery outcome is verified through relevant changes or disruptions, with recovery where required.

#### E01.04.04 — Governed Information Lifecycle Actions
Requested information lifecycle actions are evaluated and performed under applicable policy, authority and retention holds, preserving evidence and preventing unintended loss. Holds prohibit actions that violate their conditions rather than every possible change; retirement-specific processing remains a distinct responsibility.

- **`E01.04.04.F01` — Lifecycle Action Conformance:** Requested governed information lifecycle actions conform to applicable policy and retention holds, with an explicit permitted or rejected outcome.
- **`E01.04.04.F02` — Retention-Hold Respect:** Requested governed information lifecycle actions are evaluated against applicable retention holds, preventing actions that would violate those holds and providing an appropriately governed outcome for rejected requests.
- **`E01.04.04.F03` — Authorised Lifecycle Action Confirmation:** A requested governed information lifecycle action proceeds only with the applicable authorisation, including the relevant policy and hold conditions.
- **`E01.04.04.F04` — Lifecycle Action Evidence Preservation:** Evidence of governed information lifecycle actions and their dispositions remains accessible, accurate and attributable.
- **`E01.04.04.F05` — Unintended Loss Prevention:** Unintended loss is prevented.

#### E01.04.05 — Policy-Governed Retire & Retrieval
Enforce retention and retirement decisions without destroying information that must remain recoverable or independently evidenced.

*Demand: B10.06, B11.05; evidence: IU, PERSIST, RES; findings: R05, R24.*

- **`E01.04.05.F01` — RetentionEligibilityEnforced:** Disallowed retirement requests are rejected with reason.
- **`E01.04.05.F02` — LegalHoldPreserved:** Protected or held information versions remain available under the applicable retention conditions.
- **`E01.04.05.F03` — RetirementStateVisible:** Readers can distinguish retained, retired and unavailable dispositions.
- **`E01.04.05.F04` — ArchiveVersionIntegrity:** Archived versions round-trip without altered identity, timestamps or content hashes.
- **`E01.04.05.F05` — RecoveryExceptionsReported:** Unrecoverable archival states produce explicit exceptions rather than empty results.

### E01.05 — Governed Information Reference Management
Identify and resolve information via references without passing large content indiscriminately.

#### E01.05.01 — Governed Reference Resolution
Consumers can obtain the intended governed information using its reference.

- **`E01.05.01.F01` — ResolvableInformationReference:** Valid information references resolve to the intended information.
- **`E01.05.01.F02` — Target Identity Correctness:** Target identity is correct.
- **`E01.05.01.F03` — Reference Scope Enforcement:** Reference scope is enforced, distinguishing permitted and nonconforming conditions.
- **`E01.05.01.F04` — Reference Failure Explanation:** Reference failure is explained.
- **`E01.05.01.F05` — Stable Reference Semantics:** Information reference semantics remain stable.

#### E01.05.02 — Large Content Access by Reference
Consumers can obtain large governed content through references without indiscriminate in-band transfer. Content managed within Harmonia’s custodial boundary retains its integrity and applicable access conditions.

- **`E01.05.02.F01` — SuccessfulReferencedContentAccess:** Authorised consumers can retrieve the large content identified by governed references.
- **`E01.05.02.F02` — Managed Media Integrity:** Media managed within Harmonia’s custodial boundary retains its integrity and governed references during storage and access.
- **`E01.05.02.F03` — Controlled Media Retrieval:** Controlled media can be retrieved.
- **`E01.05.02.F04` — Reference-Only Payload Transfer:** Reference-only payload can be transferred through relevant changes or disruptions, with recovery where required.
- **`E01.05.02.F05` — Large-Content Failure Reporting:** Large-content failure is reported, with accessible and accurate supporting evidence.

#### E01.05.03 — Long-Lived Reference Validity
Persisted references remain meaningful through authorised information lifecycle changes.

- **`E01.05.03.F01` — PreservedReferenceValidity:** Retained references remain resolvable or receive an explicit disposition.
- **`E01.05.03.F02` — Reference Survival across Versioning:** Reference survival across are versioned.
- **`E01.05.03.F03` — Resolvable Archived Target:** Archived reference targets remain resolvable.
- **`E01.05.03.F04` — Retired Reference Disposition:** Retired reference has an explicit disposition.
- **`E01.05.03.F05` — Reference Migration Compatibility:** Reference migration is compatible.

#### E01.05.04 — External Media Reference Governance
Durable, controlled references support access to large externally managed media outside ordinary Information Unit persistence, preserving integrity, identity and access context without Harmonia assuming custody of the original media objects.

*Demand: B10.01, B10.06, B09.02; evidence: IU, NFR, REF; findings: R20.*

- **`E01.05.04.F01` — MediaNotStoredInOrdinaryStore:** Large payloads use authorised media custody rather than generic relational Information Unit storage.
- **`E01.05.04.F02` — MediaReferenceSurvivesRelocation:** A media reference remains resolvable when storage location changes.
- **`E01.05.04.F03` — MediaAccessAuthorised:** Denied requesters cannot dereference restricted media.
- **`E01.05.04.F04` — Externally Managed Media Integrity:** Externally managed media referenced by Harmonia retains verifiable integrity and governed reference context without Harmonia assuming custody of the original media objects.
- **`E01.05.04.F05` — BrokenReferenceDistinguished:** Missing media is reported distinctly from unauthorised access or transient failure.

#### E01.05.05 — Information Coordinates & Addressability
Resolve governed references using identity, context and temporal coordinates without coupling callers to provider-native addresses.

*Demand: B10.01, B10.07, B02.01; evidence: REF, IU; findings: R05, R09.*

- **`E01.05.05.F01` — CoordinateResolutionDeterministic:** Identical stable coordinates select the same governed identity and version.
- **`E01.05.05.F02` — TemporalReferenceUnambiguous:** Historic-state coordinates resolve to the intended point-in-time version.
- **`E01.05.05.F03` — UnauthorisedCoordinatesProtected:** Reference enumeration does not disclose restricted subject characteristics.
- **`E01.05.05.F04` — MovedIdentityPreserved:** Relocation or sharding does not change governed coordinates.
- **`E01.05.05.F05` — MissingCoordinateExplained:** Invalid, ambiguous and absent reference outcomes remain distinguishable.

### E01.06 — Governed Binary & Large-Media Content Management
Manage the identity, metadata, information associations, custody arrangements, resolution, acquisition, retrieval, assembly and lifecycle of formally managed binary or large-media content. This is an **Enterprise Capability**, not a synonym for Sylloge or a storage product. Existing reference and distribution capabilities remain intact.

**Provenance:** Existing Continuum `Harmonia.Sylloge` (canonical component `feea742463b84021b66853d287d8916f`), Domain05 U17 / F31 / D15, and NFR-087; Candidate 015 `E01.05.01`–`E01.05.05` and `E02.04.03`. **Standing:** Candidate 016 proactive completeness expansion, not incorporated into the governed Continuum.

**Business enablement hypotheses:** B10.01 Health Record Stewardship, B10.02 Information Completeness & Provenance Accountability, B10.03 Appropriate Information Access & Sharing, B10.06 Retention, Disclosure & Record Accountability, B10.07 Information Correction & Reconciliation. Additional clinical demand is available through LHR, HIE, diagnostics and provider-directory service journeys; detailed links require review.

**Boundary:** A binary payload, its metadata, the Information Unit that references it, and its physical custodian are distinct. Harmonia may manage a reference to externally held content without copying the bytes. Where Harmonia has custody, suitable media storage is used rather than assuming ordinary IU/database persistence. Sylloge remains the established application responsibility; Mneme/Mnemosyne remain the governed information and persistence boundaries. Agora-native collaboration media remains distinct. References are not automatically publicly dereferenceable.

**Technology interfaces:** ICT Foundation lenses 09 (Platform & Middleware), 11 (Storage & Persistence), 12 (Network & Connectivity), 15 (Observability & Operational Management), 16 (Resilience & Continuity). This does not select object storage, PACS/VNA, DICOM representation, FHIR Binary/DocumentReference operation, or any specific media-server product.

**Release note:** NFR-087 states a *defined size threshold*; the historically discussed 64 KiB boundary should be checked against the exact currently governed release policy rather than silently hardened as a new universally fixed threshold.

#### E01.06.01 — Binary Content Registration & Description
Register content metadata and maintain the information necessary to identify and govern an associated binary payload, without equating the payload with an Information Unit.

- **`E01.06.01.F01` — DescribedContentIdentity:** A registered payload or externally held payload can be distinguished from the associated Information Unit and from other payloads.
- **`E01.06.01.F02` — ContentFormatDeclared:** The available type, format and representation details are recorded accurately where supplied or established.
- **`E01.06.01.F03` — ContentProvenanceRetained:** The content registration preserves available source, origin and provenance references.
- **`E01.06.01.F04` — DescriptiveMetadataBound:** Descriptive metadata can be retrieved without retrieving the full binary content.
- **`E01.06.01.F05` — RegistrationExceptionsExplicit:** Unsupported, incomplete or inconsistent registration requests have explicit outcomes.

#### E01.06.02 — Information Unit–Binary Association Management
Associate logical Information Units with zero or more supporting binary payloads while preserving the distinct identity, version and lifecycle semantics of both.

- **`E01.06.02.F01` — AssociationTargetCorrect:** A governed Information Unit association resolves to the intended binary content identity.
- **`E01.06.02.F02` — MultiplePayloadAssociation:** An information item may explicitly reference multiple associated payloads where its defined semantics require them.
- **`E01.06.02.F03` — VersionAssociationTraceable:** An association identifies the applicable logical information version and appropriate binary-content version or standing.
- **`E01.06.02.F04` — RelationshipChangeGoverned:** Creating, changing or retiring an association is authorised and attributable.
- **`E01.06.02.F05` — AssociationIntegrityDetectable:** Missing, mismatched or contradictory payload associations are detected or explicitly reported.

#### E01.06.03 — Binary Custody & Source Location Management
Determine which party or technical service holds physical custody and how content can be resolved while preserving external source authority and storage independence.

- **`E01.06.03.F01` — CustodyKindKnown:** Harmonia-managed and externally managed physical custody are distinguishable.
- **`E01.06.03.F02` — ExternalContentNoForcedCopy:** A valid externally managed item can be governed and referenced without requiring automatic replication into Harmonia.
- **`E01.06.03.F03` — PhysicalLocationIndependent:** Logical references remain stable through authorised storage-provider or physical-location changes.
- **`E01.06.03.F04` — CustodyAvailabilityQualified:** Provider unavailability, known absence and broken reference are reported as distinct cases.
- **`E01.06.03.F05` — SourceAuthorityPreserved:** Binary-content management does not imply ownership of originating clinical facts or the external source repository.

#### E01.06.04 — Large-Content Ingestion & Custody Establishment
Accept or register large content through appropriate governed custody arrangements without using ordinary structured Information Unit persistence for bulk media bytes.

- **`E01.06.04.F01` — ThresholdPolicyApplied:** Content above the release-defined large-content threshold follows the designated media path.
- **`E01.06.04.F02` — ReferenceCreatedOnAcceptedIngest:** Where physical custody is accepted, a governed content reference is established and correlated to the accepted payload.
- **`E01.06.04.F03` — IngestOutcomeUnambiguous:** Accepted, rejected, incomplete and indeterminate acquisition or storage outcomes are distinguishable.
- **`E01.06.04.F04` — PayloadByteIntegrityChecked:** Accepted physical content can be verified against the applicable integrity evidence.
- **`E01.06.04.F05` — UncommittedMediaNotPublished:** Uncertain or incomplete content capture is not misrepresented as authoritative available content.

#### E01.06.05 — Governed Binary Retrieval & Delivery
Supply authorised binary content through bounded access semantics without requiring every consumer or information exchange to carry the payload in-band.

- **`E01.06.05.F01` — AuthorisedRetrievalOnly:** Permitted and denied retrievals respect the applicable effective identity and information-protection rules.
- **`E01.06.05.F02` — ReferenceBasedAcquisition:** Consumers can acquire the content through a governed reference independently of Information Unit structural data retrieval.
- **`E01.06.05.F03` — ResourceBoundedTransfer:** Large-content retrieval can be delivered without unbounded application memory demand under a stated performance contract.
- **`E01.06.05.F04` — AvailabilityFailureQualified:** Missing, inaccessible, corrupted and temporarily unavailable content produce distinguishable outcomes.
- **`E01.06.05.F05` — TransferIntegrityAssessed:** Received bytes can be verified against the expected governed content identity and integrity evidence.

#### E01.06.06 — On-Demand Binary Assembly & Presentation
Resolve and assemble the logical content required by a consumer from one or more governed binary payloads when authorised and required.

- **`E01.06.06.F01` — AssemblySourcesTraceable:** The payload sources and transformations contributing to assembled content remain attributable.
- **`E01.06.06.F02` — MultiPartAssemblyCorrect:** Supported multi-payload content is assembled in the defined order and structure.
- **`E01.06.06.F03` — SourceAuthorityNotRewritten:** Assembly does not silently change the authoritative status of source information.
- **`E01.06.06.F04` — PartialAssemblyNotMisrepresented:** Missing or rejected assembly components do not produce a falsely complete result.
- **`E01.06.06.F05` — AssemblyDisclosureConstrained:** An assembled result is released only when all applicable disclosure obligations are satisfied.

#### E01.06.07 — Binary Content Lifecycle & Reference Continuity
Maintain consistency of content references, retention and availability across binary-content lifecycle events, without prescribing a particular storage engine.

- **`E01.06.07.F01` — MediaRetentionRespected:** Applicable retention constraints and holds are preserved by the managed content arrangement.
- **`E01.06.07.F02` — MediaReferenceRetained:** Valid media references remain resolvable or explicitly dispositioned across archive, relocation and provider changes.
- **`E01.06.07.F03` — MediaVersionSemanticsExplicit:** Replacement, new representation and version changes do not silently rewrite prior source identity.
- **`E01.06.07.F04` — MediaRetirementGoverned:** Content retirement and deletion are prohibited when retention or external custody constraints disallow them.
- **`E01.06.07.F05` — MediaRecoveryAccountable:** Recovery/restoration exercises verify consistency between metadata, governed references and available physical payloads.

#### E01.06.08 — Binary Content Protection & Operational Integration
Apply security and operational obligations to managed binary content and integrate with fit-for-purpose technical storage and external custodians without treating the platform service as the capability itself.

- **`E01.06.08.F01` — ProtectionContextCarried:** Information labels, relevant authorisation context and disclosure constraints accompany applicable media access.
- **`E01.06.08.F02` — MediaAccessAuditable:** Material retrieval, disclosure and governed lifecycle changes are represented in the applicable audit/provenance framework.
- **`E01.06.08.F03` — PlatformStorageIntegrationQualified:** Unavailable, degraded and restored media providers are observable through explicit application/technical status interfaces.
- **`E01.06.08.F04` — NoGenericDatabaseFallback:** Large media is not silently redirected to ordinary structured Information Unit persistence when its configured media facility is unavailable.
- **`E01.06.08.F05` — TechnicalSubstitutionIndependent:** Changing the underlying repository or storage technology does not redefine governed media identities or consumer contract semantics.

**Non-duplication:** E01.05 remains the general capability to manage and resolve governed references, and E02.04.03 remains authorised reference-based information sharing. E01.06 covers **the governed binary content itself and its association/custody lifecycle**. E05 retains cross-cutting identity/security obligations; E03 governs whichever explicit external API/media contracts are selected.

**Review scenarios:** external PACS or source unavailable but logical reference retained; metadata exists but binary missing; multipart document assembly missing one part; caller can retrieve IU metadata but not binary bytes; provider storage moves without a logical reference change; content exceeds media threshold; media capture fails after partial transfer; authorised historical content requested after archive; malicious substitution/corruption detected; storage unavailable with no fallback to relational IU store.

## E02 — Information Access, Discovery & Distribution
The system can make information available to appropriate consumers at the time and scope required.

### E02.01 — Governed Information Access
Provide controlled retrieval and change through governed contracts.

#### E02.01.01 — Authorised Information Retrieval
Consumers can retrieve the information they are entitled to use.

- **`E02.01.01.F01` — PolicyConformantRead:** Information retrieval applies the correct permission and scope conditions.
- **`E02.01.01.F02` — Access Decision Correctness:** Access decision is correct.
- **`E02.01.01.F03` — Purpose-Limited Retrieval:** Purpose-limited can be retrieved.
- **`E02.01.01.F04` — Retrieval Denial Explanation:** Retrieval denial is explained.
- **`E02.01.01.F05` — Retrieved Version Integrity:** Retrieved version retains its integrity.

#### E02.01.02 — Governed Information Change
Consumers can request appropriately controlled modifications.

- **`E02.01.02.F01` — CorrectChangeDisposition:** Information changes receive the correct acceptance, rejection and durability outcome.
- **`E02.01.02.F02` — Write Authority Verification:** Write authority is verified, distinguishing permitted and nonconforming conditions.
- **`E02.01.02.F03` — Persistence Change Preconditions:** Mnemosyne independently verifies and enforces applicable information-change preconditions before committing a change, rejecting operations that fail those conditions without compromising the governed information state.
- **`E02.01.02.F04` — Accepted Change Durability:** Accepted change is durable through relevant changes or disruptions, with recovery where required.
- **`E02.01.02.F05` — Governed Rejection Evidence:** Governed rejection is evidenced, distinguishing permitted and nonconforming conditions.

#### E02.01.03 — Scoped Information Selection
Consumers can request information relevant to an authorised subject or task.

- **`E02.01.03.F01` — ScopeCorrectRetrieval:** Retrieved information conforms to the requested authorised scope.
- **`E02.01.03.F02` — Subject-Scoped Result Accuracy:** Subject-scoped result is accurate, distinguishing permitted and nonconforming conditions.
- **`E02.01.03.F03` — Compartment Boundary Conformance:** Compartment boundary conforms to the applicable requirements, distinguishing permitted and nonconforming conditions.
- **`E02.01.03.F04` — Irrelevant Result Exclusion:** Irrelevant result is excluded.
- **`E02.01.03.F05` — Contextual Selection Explanation:** Contextual selection is explained.

#### E02.01.04 — Authorised Version-Specific Retrieval
Allow a consumer to retrieve a precisely identified governed version with explicit authority and absence outcomes.

*Demand: B02.01, B10.03, B10.07; evidence: MN, IU, SEC; findings: R05, R09.*

- **`E02.01.04.F01` — VersionSelectionCorrect:** Specified historical reference returns only the designated version.
- **`E02.01.04.F02` — DenialNotAbsence:** Unauthorised retrieval cannot be mistaken for absence of information.
- **`E02.01.04.F03` — ReadEvidenceAttributed:** Disclosures requiring audit are correlated with requesting authority and source.
- **`E02.01.04.F04` — RepresentationMeaningPreserved:** Returned data retains sufficient model context for interpretation.
- **`E02.01.04.F05` — StaleReadQualified:** Where currentness is not assured the response exposes its temporal/availability qualification.

#### E02.01.05 — Purpose-Limited Disclosure
Make authorised information available only in the requested and permitted purpose, content and temporal scope.

*Demand: B01.05, B10.03, B10.04, B14.06; evidence: SEC, TH, IU; findings: R02, R22.*

- **`E02.01.05.F01` — PurposeConstraintApplied:** Purpose-incompatible information is not disclosed.
- **`E02.01.05.F02` — CompartmentFilterEnforced:** Compartment-restricted content does not escape filtered responses.
- **`E02.01.05.F03` — DelegatedScopeValidated:** Representative permissions are checked against applicable current authority.
- **`E02.01.05.F04` — NoUnauthorisedMetadataLeak:** Denied results expose no protected presence/identity hints.
- **`E02.01.05.F05` — DisclosureDecisionAttributable:** Permit and deny decisions can be examined with effective authority context.

### E02.02 — Information Discovery & Search
Locate information and return appropriate versioned references/results.

#### E02.02.01 — Version-Referenced Information Search
Search can locate governed information and identify the exact versions returned.

- **`E02.02.01.F01` — ResolvableSearchReferences:** Search result version references resolve to the intended information versions.
- **`E02.02.01.F02` — Correct Version Targeting:** Correct version is targeted.
- **`E02.02.01.F03` — Search Result Completeness Qualification:** Search result completeness are qualified.
- **`E02.02.01.F04` — Search Reference Persistence:** Search reference can be persisted.
- **`E02.02.01.F05` — Controlled Version Disclosure:** Controlled version is disclosed.

#### E02.02.02 — Contextual Information Discovery
Search reflects applicable business and information constraints.

- **`E02.02.02.F01` — CorrectSearchScoping:** Searches return only permitted results within the applicable scope.
- **`E02.02.02.F02` — Purpose-Relevant Query Scope:** Purpose-relevant query has an explicit scope, distinguishing permitted and nonconforming conditions.
- **`E02.02.02.F03` — Authorised Result Filtering:** Authorised result is filtered, distinguishing permitted and nonconforming conditions.
- **`E02.02.02.F04` — Discoverability of Relevant Sources:** Relevant information sources can be discovered, with accessible and accurate supporting evidence.
- **`E02.02.02.F05` — Explainable Result Qualification:** Explainable result is qualified.

#### E02.02.03 — Search Result Continuity
A discovered result collection remains interpretable for subsequent governed work.

- **`E02.02.03.F01` — ReusableSearchResultContext:** Persisted search result collections retain their required meaning and references for reuse.
- **`E02.02.03.F02` — Persistent Result-Set Identity:** Persistent result-set has an identifiable identity.
- **`E02.02.03.F03` — Stable Result References:** Search result references remain stable.
- **`E02.02.03.F04` — Reusable Selection Context:** Reusable selection has its relevant context through relevant changes or disruptions, with recovery where required.
- **`E02.02.03.F05` — Result-Set Change Awareness:** Result-set change is made known through relevant changes or disruptions, with recovery where required.

#### E02.02.04 — Governed Search Completeness Qualification
Describe which authoritative search domain, versions and filters were actually evaluated.

*Demand: B02.01, B10.03, B13.05; evidence: MN, IU, RESULT; findings: R09, R18.*

- **`E02.02.04.F01` — SearchScopeDeclared:** Results identify source scope and temporal cut-off.
- **`E02.02.04.F02` — SearchNotCacheOnly:** Complete authoritative searches are not satisfied from incomplete active-cache state.
- **`E02.02.04.F03` — ResultVersionsPinned:** Persisted result sets retain version references used during the search.
- **`E02.02.04.F04` — SearchDenialNotMissing:** Search restriction is distinguishable from no matching authoritative content.
- **`E02.02.04.F05` — PartialSearchExposed:** Partial failures or inaccessible partitions are reported rather than implied complete.

#### E02.02.05 — Cross-Domain Discovery & Correlation
Discover information across healthcare domains using governed identifiers while avoiding identity conflation.

*Demand: B02.01, B05.04, B13.05; evidence: IU, REF, MODEL; findings: R09.*

- **`E02.02.05.F01` — SubjectCorrelationQualified:** Cross-domain correlations report matching confidence and provenance.
- **`E02.02.05.F02` — DifferentIdentityTypesPreserved:** Person, episode and IU identities remain distinguishable.
- **`E02.02.05.F03` — DuplicateCandidatesExposed:** Ambiguous matches are visible rather than silently merged.
- **`E02.02.05.F04` — TemporalCorrelationCorrect:** Cross-domain results retain applicable occurrence and recording times.
- **`E02.02.05.F05` — UnauthorisedSourcesNotExposed:** Search federation does not disclose unauthorised source existence.

#### E02.02.06 — Subject-Linked Longitudinal Information Discovery
Discover available clinical information across encounters and sources while qualifying the governed subject association.

- **`E02.02.06.F01` — SubjectLinkageQualified:** Information is not associated with a person when the available identity evidence is insufficient.
- **`E02.02.06.F02` — MultiEpisodeDiscovery:** Relevant information can be discovered across more than one encounter or provider without assuming co-location.
- **`E02.02.06.F03` — HistoricVersionSelectable:** Discovery can distinguish historic information versions from current versions.
- **`E02.02.06.F04` — DistributedCompletenessQualified:** Unavailable or excluded sources are visible as coverage qualifications where lawful.
- **`E02.02.06.F05` — SourceAndTimeRetained:** Discovered information retains its source, effective time and record-version association.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-LHR-QUERY, C-FED-QUERY, C-PERSON-CORR. *Release horizon:* Unscheduled.

#### E02.02.07 — Cross-Source Clinical Record Correspondence
Recognise probable duplicate, related and conflicting clinical information without silently collapsing distinct source records.

- **`E02.02.07.F01` — CrossSourceDuplicateQualification:** Same-looking records from different sources remain distinguishable pending governed correspondence.
- **`E02.02.07.F02` — RelatedNotIdentical:** A derived clinical summary is distinguishable from the information from which it was derived.
- **`E02.02.07.F03` — ClinicalContradictionVisible:** Conflicting clinical facts can be surfaced as qualified competing assertions.
- **`E02.02.07.F04` — ReferenceCorrelationTraceable:** Each correspondence retains references to the contributing governed information versions.
- **`E02.02.07.F05` — UnsafeConflationPrevented:** Clinically distinct information is not silently fused, including under adverse conditions.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-FED-QUERY, DRP-IU. *Release horizon:* Unscheduled.

### E02.03 — Distributed Information Availability
Maintain/use distributed or cached information where appropriate to access and performance needs.

#### E02.03.01 — Distributed Access
Consumers can access and change governed information consistently across eligible system locations and concurrent activity.

- **`E02.03.01.F01` — FastRead:** Distributed retrieval remains responsive under the defined loads.
- **`E02.03.01.F02` — GovernedWrite:** Distributed changes receive the correct governed acceptance, rejection and durability outcome.
- **`E02.03.01.F03` — ConsistentSharedAccess:** Concurrent distributed operations satisfy the applicable consistency requirements.
- **`E02.03.01.F04` — Distributed Read Availability:** Distributed read is available.
- **`E02.03.01.F05` — Conflicting Update Recognition:** Conflicting update is recognised, with accessible and accurate supporting evidence.
- **`E02.03.01.F06` — Correct Cross-Node State:** Correct cross-node has an explicit state.
- **`E02.03.01.F07` — Access Recovery after Node Loss:** Distributed information access is restored after complete node loss or replacement, including required resynchronisation and truthful reporting of availability or degradation.

#### E02.03.02 — Distributed Reference Availability
Governed references remain coherent, identifiable, meaningful, faithful and resolvable across eligible distributed contexts, whether embedded or independently managed. Reference coherence, current payload availability and access permission remain separate obligations.

- **`E02.03.02.F01` — Cross-Location Reference Resolution:** Governed references resolve to the intended identifiable information across eligible distributed contexts and permitted locations. An unavailable payload has an explicit outcome rather than being inferred available from reference coherence.
- **`E02.03.02.F02` — Cross-Node Reference Fidelity:** References retain their identity, meaning and fidelity across eligible nodes, whether embedded in another structure or independently managed.
- **`E02.03.02.F03` — Reference Resolution Continuity:** Governed references retain coherent identity and resolvability through relevant distributed changes and disruptions, with recovery or an explicit qualified outcome where required.
- **`E02.03.02.F04` — Invalid Reference Detection:** Invalid, inconsistent or unresolvable distributed references are identified and qualified without silently changing reference identity or misrepresenting information availability.
- **`E02.03.02.F05` — Distributed Access Permission:** Distributed reference access enforces the applicable authorisation independently of reference coherence and payload availability.

#### E02.03.03 — Fresh Information Availability
Consumers can distinguish information that is suitably current from information that is stale.

- **`E02.03.03.F01` — FreshnessConformantAccess:** Information reads satisfy their required currency constraints.
- **`E02.03.03.F02` — Declared Information Currency:** Declared information is current through relevant changes or disruptions, with recovery where required.
- **`E02.03.03.F03` — Stale Data Recognition:** Stale data is recognised, with accessible and accurate supporting evidence.
- **`E02.03.03.F04` — Currency-Constrained Retrieval:** Currency-constrained can be retrieved through relevant changes or disruptions, with recovery where required.
- **`E02.03.03.F05` — Refresh Availability:** Refresh is available.

#### E02.03.04 — Distributed Access Consistency
Preserve consumer-visible access and update semantics across concurrent nodes and partitions.

*Demand: B05.06, B10.01, B12.04; evidence: DIST, RES, NFR; findings: R10, R21.*

- **`E02.03.04.F01` — ConcurrentVersionSafety:** Concurrent updates obey declared version policy without silent lost changes.
- **`E02.03.04.F02` — Intermittent Connectivity Consistency:** Active distributed nodes maintain the applicable coherence and consistency guarantees during intermittent or partitioned connectivity; permitted reads remain available or degrade explicitly under the applicable conditions.
- **`E02.03.04.F03` — PartitionOutcomeExplicit:** Partition uncertainty never appears as unqualified authoritative success.
- **`E02.03.04.F04` — ReplicaCurrencyQualified:** Observed replica freshness is exposed where material.
- **`E02.03.04.F05` — RecoveryNoIdentityChange:** After recovery no governed reference changes identity or version unexpectedly.

#### E02.03.05 — Accepted Clinical Information Availability
Make accepted received clinical information available through governed access despite its source, ingest route or internal storage placement.

- **`E02.03.05.F01` — AcceptedReceiptAccessible:** Accepted information is retrievable or has an explicitly governed pending/recovery disposition.
- **`E02.03.05.F02` — AccessNotRawIngressOnly:** An exchange receipt is not the sole location from which accepted clinical content may be obtained.
- **`E02.03.05.F03` — NoFalseAvailability:** A transport ACK does not imply authoritative persisted availability unless the applicable acceptance contract establishes it.
- **`E02.03.05.F04` — SourceLossContinuity:** Accepted information remains recoverable through specified source and infrastructure failures.
- **`E02.03.05.F05` — AuthorisedAvailability:** Availability to an authorised consumer never overrides subject, compartment or disclosure controls.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-DOC-INGRESS, C-HIE-GATE, DRP-IU, DRP-MN. *Release horizon:* Unscheduled.

### E02.04 — Information Distribution & Notification
Make appropriate information or references available across required participants.

#### E02.04.01 — Selective Information Distribution
Relevant information or references reach authorised recipients according to governing rules.

- **`E02.04.01.F01` — CorrectDistributionScope:** Information distributions reach only their intended recipients.
- **`E02.04.01.F02` — Intended Recipient Selection:** Intended recipient selection reaches and can be understood by the intended recipient.
- **`E02.04.01.F03` — Distribution Scope Conformance:** Distribution scope conforms to the applicable requirements, distinguishing permitted and nonconforming conditions.
- **`E02.04.01.F04` — Confidentiality-Constrained Delivery:** Confidentiality-constrained can be delivered, including in adverse conditions.
- **`E02.04.01.F05` — Distribution Status Visibility:** Distribution status is visible, with accessible and accurate supporting evidence.

#### E02.04.02 — Information Availability Notification
Consumers can be informed when relevant information becomes available or changes.

- **`E02.04.02.F01` — TimelyRelevantNotification:** Qualifying information updates produce relevant notifications within the required conditions.
- **`E02.04.02.F02` — Notification Relevance:** Notification relevance reaches and can be understood by the intended recipient.
- **`E02.04.02.F03` — Recipient Delivery Visibility:** Recipient delivery is visible, with accessible and accurate supporting evidence.
- **`E02.04.02.F04` — Notification Currency:** Notification is current through relevant changes or disruptions, with recovery where required.
- **`E02.04.02.F05` — Duplicate Notification Control:** Duplicate notification is controlled.

#### E02.04.03 — Referenced Information Sharing
Large or governed information is shared as an appropriate reference when applicable.

- **`E02.04.03.F01` — ValidSharedReference:** Information distributions use resolvable, authorised shared references where applicable.
- **`E02.04.03.F02` — Recipient Reference Usability:** Recipient reference usability reaches and can be understood by the intended recipient.
- **`E02.04.03.F03` — Disclosure Scope Enforcement:** Disclosure scope is enforced, distinguishing permitted and nonconforming conditions.
- **`E02.04.03.F04` — No-Unnecessary-Content Transfer:** No-unnecessary-content can be transferred through relevant changes or disruptions, with recovery where required.
- **`E02.04.03.F05` — Shared Reference Expiration:** Shared reference expiration reaches and can be understood by the intended recipient.

#### E02.04.04 — Governed Subscription Matching
Select interested consumers based on governed information and event characteristics without making internal metadata a public API.

*Demand: B05.06, B10.03, B13.01; evidence: IU, REF, SEC; findings: R05, R18.*

- **`E02.04.04.F01` — SubscriptionMatchCorrect:** Authorized subscribers receive events matching agreed criteria.
- **`E02.04.04.F02` — NoRestrictedDelivery:** Subscriber filters never override local information disclosure requirements.
- **`E02.04.04.F03` — ChangeNotificationCorrelation:** Notifications identify the changed governed unit and relevant version.
- **`E02.04.04.F04` — WithdrawalStopsDelivery:** Revoked subscription or authority stops further protected delivery.
- **`E02.04.04.F05` — DeliveryQualificationKnown:** Delayed, duplicate or failed notices are distinguishable where delivery is tracked.

#### E02.04.05 — Selective Publication & Withdrawn Information
Make new, superseded and withdrawn information availability visible without modifying immutable historic versions.

*Demand: B05.04, B10.03, B10.07; evidence: IU, SEC; findings: R05, R18.*

- **`E02.04.05.F01` — PublicationScopeApplied:** Only authorised target consumers see published material.
- **`E02.04.05.F02` — SupersessionTraceable:** Receivers can locate the subsequent governed version.
- **`E02.04.05.F03` — WithdrawalCommunicated:** Impacted consumers receive a qualified withdrawal notification where required.
- **`E02.04.05.F04` — HistoricViewPreserved:** Withdrawal leaves required historical version references interpretable.
- **`E02.04.05.F05` — DuplicateNoticeHandled:** Repeated notifications do not create conflicting authoritative change records.

### E02.05 — Information Access Continuity
Preserve useful access through recoverable disruptions where architecturally supported.

#### E02.05.01 — Continuing Information Access
Governed access remains useful through supported infrastructure and service disruptions.

- **`E02.05.01.F01` — ContinuityOfAccess:** Information access meets the applicable availability conditions through specified disruptions.
- **`E02.05.01.F02` — Alternate Access Path Availability:** Alternate access path is available through relevant changes or disruptions, with recovery where required.
- **`E02.05.01.F03` — Degraded Access Disclosure:** Degraded access is disclosed, with accessible and accurate supporting evidence.
- **`E02.05.01.F04` — Continuity under Node Failure:** Governed information access continues or receives an explicit degraded disposition after node failure through relevant changes or disruptions, with recovery where required.
- **`E02.05.01.F05` — Essential Read Priority:** Essential read has an established priority.

#### E02.05.02 — Access Restoration
Consumers can regain correct access following interruption without violating governing controls.

- **`E02.05.02.F01` — CorrectRestoredAccess:** Restored access satisfies the required scope, freshness and security conditions.
- **`E02.05.02.F02` — Recovery of Authorised Access:** Authorised information access can be recovered, distinguishing permitted restoration from nonconforming access.
- **`E02.05.02.F03` — Restoration Outcome Confirmation:** Restoration outcome is confirmed through relevant changes or disruptions, with recovery where required.
- **`E02.05.02.F04` — Recovery Status Visibility:** Recovery status is visible, with accessible and accurate supporting evidence.
- **`E02.05.02.F05` — Post-Recovery Access Consistency:** Post-recovery access is consistent through relevant changes or disruptions, with recovery where required.

#### E02.05.03 — Degraded Access & Restoration
Report and restore governed access following outage without promising unverified data freshness or availability.

*Demand: B05.06, B12.04, B10.01; evidence: RES, DIST, OPS; findings: R10, R24.*

- **`E02.05.03.F01` — AccessDegradationVisible:** Users and consumers can distinguish degraded and normal access modes.
- **`E02.05.03.F02` — RestoredVersionVerified:** Recovered access returns an authoritative version after consistency checks.
- **`E02.05.03.F03` — HIEAvailabilityProtected:** Accepted incoming clinical content remains retrievable after relevant failure.
- **`E02.05.03.F04` — RecoveryTimeEvidence:** Recovery sequence and relevant timing are recorded for conformance assessment.
- **`E02.05.03.F05` — NeverInventedEmptyResult:** Temporary inaccessibility is not reported as no record found.

### E02.06 — Entity & Service Directory Discovery
Discover, resolve and qualify healthcare entities, organisations, service offerings and their relationships through governed directory contracts, independently of any specific directory application.

*Evidence-led candidate.* Lenses: PD,EMPI,HIE,LHR; sources: C-PRAC-DIR,C-ORG-DIR,C-SVC-DIR,C-PRAC-AFF. No solution component or release commitment implied.

#### E02.06.01 — Directory Record Discovery
Resolve attributable, uniquely identified entries within the applicable directory domain.

- **`E02.06.01.F01` — IdentifiableDirectoryEntry:** Tested directory results distinguish a record identity from its represented person, organisation or service identity.
- **`E02.06.01.F02` — PurposeBoundDiscovery:** Each directory search discloses only information permitted for the actual purpose.
- **`E02.06.01.F03` — MultipleEntryDisambiguation:** Ambiguous or duplicate directory matches are returned with an explicit qualifier, never silently assumed equivalent.
- **`E02.06.01.F04` — MissingEntryTruthfulness:** Unavailable or absent entries remain distinguishable from failed or denied directory retrieval.
- **`E02.06.01.F05` — DirectoryReferenceContinuity:** A saved directory reference can be resolved to the intended entry or an explicit change/disposition.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PRAC-DIR, C-ORG-DIR, C-SVC-DIR. *Release horizon:* Unscheduled.

#### E02.06.02 — Provider Role & Affiliation Resolution
Obtain the context-bound roles, organisational relationships and privileges relevant to a healthcare provider.

- **`E02.06.02.F01` — AffiliationTemporality:** Past, prospective and currently effective affiliations are distinguishable in tested responses.
- **`E02.06.02.F02` — RoleScopeExplicit:** An individual’s role is returned with the organisation or care setting in which it applies.
- **`E02.06.02.F03` — PrivilegeNotInferredFromAuthentication:** A valid login never alone implies a current clinical privilege or professional authority.
- **`E02.06.02.F04` — ConflictingAffiliationVisible:** Contradictory affiliations are attributed or qualified rather than silently overwritten.
- **`E02.06.02.F05` — ProviderRelationshipEvidence:** Users can trace each returned relationship to its governed source and relevant effective period.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PRAC-AFF, C-PRAC-VERIFY, C-PRAC-DIR. *Release horizon:* Unscheduled.

#### E02.06.03 — Service & Location Discovery
Find current service offerings, provision locations, contact routes and operating context.

- **`E02.06.03.F01` — ServiceEligibilitySeparability:** A discovered service is not presented as guaranteed entitlement or appointment availability.
- **`E02.06.03.F02` — LocationContextAccuracy:** Location and service-provider associations are correctly scoped at the effective time.
- **`E02.06.03.F03` — ServiceContactUsability:** Representative service searches yield a usable and appropriately authorised contact or endpoint route.
- **`E02.06.03.F04` — ServiceDiscontinuationVisible:** Withdrawn or suspended service offerings are not falsely presented as currently available.
- **`E02.06.03.F05` — MultiOrganisationDiscovery:** Searches across organisations retain source attribution and distinguish unavailable providers from none matching.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-SVC-DIR, C-ORG-DIR. *Release horizon:* Unscheduled.

#### E02.06.04 — Directory Currency & Provenance Qualification
Qualify directory entries by source, verified currency, lifecycle and conflicting assertions.

- **`E02.06.04.F01` — VerificationStatusVisible:** A consumer can tell verified, unverified and stale directory assertions apart.
- **`E02.06.04.F02` — SourceResponsibilityExplicit:** The party responsible for the asserted entry or relationship is identified or its absence declared.
- **`E02.06.04.F03` — TemporalHistoryRetained:** An affiliation or location change does not erase the interpretability of its prior effective state.
- **`E02.06.04.F04` — ContradictionQualified:** Conflicting source assertions are explicitly marked and made reviewable.
- **`E02.06.04.F05` — UnresolvedDirectoryChangeAuditable:** An unverified change is not silently promoted to trusted effective directory data.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PRAC-VERIFY, C-PRAC-DIR, DRP-IU. *Release horizon:* Unscheduled.

#### E02.06.05 — Directory Change Notification
Publish governed directory entry and relationship changes to eligible interested consumers.

- **`E02.06.05.F01` — RelevantChangeSubscription:** Eligible directory consumers receive or can discover changes within the declared criteria.
- **`E02.06.05.F02` — WithdrawnAccessStopsDisclosure:** Access or subscription withdrawal prevents future unauthorised directory change delivery.
- **`E02.06.05.F03` — ChangeEventVersionCorrelation:** A change notice links to the correct entity/relationship identity and version or status.
- **`E02.06.05.F04` — UnreportedChangeDetected:** Delayed or undelivered change notices are discernible rather than interpreted as no change.
- **`E02.06.05.F05` — NotificationNotAuthoritativeUpdate:** Receiving a directory change notification does not independently establish an authoritative correction.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-CT2-EVENT, C-PRAC-DIR. *Release horizon:* Unscheduled.

#### E02.06.06 — Federated Directory Resolution
Resolve entities and service offerings across separately authoritative directory sources without pretending that all sources share one current truth.

- **`E02.06.06.F01` — FederatedAuthorityAttribution:** Each returned assertion has a discernible source and authority qualification.
- **`E02.06.06.F02` — PartialFederationQualified:** Incomplete or failing directory sources produce an explicit completeness qualifier.
- **`E02.06.06.F03` — EntityCrossReferenceNotAutomaticMerge:** Linkable records are not silently merged solely because a search returns them together.
- **`E02.06.06.F04` — CrossDirectorySourceIsolation:** One source’s error or unavailability does not corrupt independently valid results from another.
- **`E02.06.06.F05` — FederatedResultSecurity:** Identity, provider and endpoint details obey each source’s applicable disclosure constraints.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-ORG-DIR, C-PRAC-DIR, C-PERSON-RESOLVE. *Release horizon:* Unscheduled.

#### E02.06.07 — Directory Disclosure & Boundary Control
Provide safe and understandable differences between public, partner, practitioner and administrative views of directory information.

- **`E02.06.07.F01` — PublicPartnerInternalSeparation:** Public, partner and internal consumers obtain only their authorised directory representations.
- **`E02.06.07.F02` — ContactDetailRestriction:** Restricted contacts are excluded from unauthorised directory responses and notifications.
- **`E02.06.07.F03` — DirectoryQueryAuditCoverage:** Disclosure events meeting audit criteria remain attributable to the request and effective authority.
- **`E02.06.07.F04` — LegitimateMinimalResult:** A restricted query can return the usable minimum without revealing protected nonmatches.
- **`E02.06.07.F05` — BoundaryRuleConsistency:** The same governed assertion is not disclosed inconsistently merely because a different directory access path is used.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: DRP-SEC, C-PRAC-DIR. *Release horizon:* Unscheduled.

## E03 — Digital Interoperability & Service Interaction
The system can communicate with external/internal services using understandable, governed contracts.

### E03.01 — External Information Exchange
Exchange information across system and organisational boundaries.

#### E03.01.01 — Standards-Based Clinical Exchange
External parties can exchange supported clinical information under authoritative contracts.

- **`E03.01.01.F01` — ContractConformantClinicalExchange:** Clinical exchanges satisfy their applicable exchange specifications.
- **`E03.01.01.F02` — Contract Conformant Payloads:** Clinical exchange payloads conform to the applicable contract, distinguishing permitted and nonconforming conditions.
- **`E03.01.01.F03` — Clinically Meaningful Content Preservation:** Clinically meaningful content is preserved through relevant changes or disruptions, with recovery where required.
- **`E03.01.01.F04` — External Acknowledgement Correctness:** External acknowledgement is correct.
- **`E03.01.01.F05` — Unsupported Variation Reporting:** Unsupported variation is reported, with accessible and accurate supporting evidence.

#### E03.01.02 — Cross-Organisation Information Exchange
Information can be exchanged appropriately between separate healthcare organisations.

- **`E03.01.02.F01` — SuccessfulAuthorisedExternalExchange:** Valid authorised inter-organisation exchanges can be completed correctly.
- **`E03.01.02.F02` — Recipient Endpoint Trust:** Recipient endpoint trust reaches and can be understood by the intended recipient.
- **`E03.01.02.F03` — Cross-Boundary Context Preservation:** Cross-boundary context is preserved through relevant changes or disruptions, with recovery where required.
- **`E03.01.02.F04` — Exchange Outcome Visibility:** Exchange outcome is visible, with accessible and accurate supporting evidence.
- **`E03.01.02.F05` — Failed Exchange Resolution:** Failed exchange is resolved through relevant changes or disruptions, with recovery where required.

#### E03.01.03 — External Exchange Acceptance
An inbound exchange receives a clear governed acceptance or rejection outcome.

- **`E03.01.03.F01` — DefinedInboundOutcome:** Inbound interactions receive a defined correct outcome with the required evidence.
- **`E03.01.03.F02` — Durable Accepted Exchange:** Accepted inbound exchange content remains durable through relevant changes or disruptions, with recovery where required.
- **`E03.01.03.F03` — Validated Incoming Contract:** Inbound exchange contracts are validated, distinguishing permitted and nonconforming conditions.
- **`E03.01.03.F04` — Duplicate Exchange Recognition:** Duplicate exchange is recognised.
- **`E03.01.03.F05` — Rejected Exchange Explanation:** Rejected exchange is explained through relevant changes or disruptions, with recovery where required.

#### E03.01.04 — External Acceptance versus Receipt
Separate transport-level receipt, validation, governed acceptance and clinical responsibility acceptance.

*Demand: B04.03, B05.03, B10.01; evidence: EXCH, MN, NFR; findings: R16.*

- **`E03.01.04.F01` — TransportReceiptClassified:** Transport receipt is never presented as authoritative durable acceptance.
- **`E03.01.04.F02` — ValidationRejectionExplicit:** Invalid incoming content has a distinct actionable rejection result.
- **`E03.01.04.F03` — DurableAcceptanceCorrelated:** Positive authoritative acceptance corresponds to retrieved governed content.
- **`E03.01.04.F04` — DuplicateSubmissionHandled:** Repeat submissions preserve idempotency or explicit duplicate meaning.
- **`E03.01.04.F05` — ClinicalResponsibilityNotInferred:** Technical exchange responses do not assert clinical handover acceptance.

### E03.02 — Service Contract Exposure & Consumption
Offer and use controlled system services where authorised.

#### E03.02.01 — Governed Service Exposure
Authorised consumers can request published information-system services.

- **`E03.02.01.F01` — ContractConformantServiceAccess:** Exposed service interactions satisfy their exposure and access contracts.
- **`E03.02.01.F02` — Authorised Interface Availability:** Authorised interface is available, distinguishing permitted and nonconforming conditions.
- **`E03.02.01.F03` — Contract Version Visibility:** Contract version is visible, distinguishing permitted and nonconforming conditions.
- **`E03.02.01.F04` — Request Scope Enforcement:** Request scope is enforced, distinguishing permitted and nonconforming conditions.
- **`E03.02.01.F05` — Interface Failure Signalling:** Interface failure is signalled.

#### E03.02.02 — Governed Service Consumption
The information system can use external services within required constraints.

- **`E03.02.02.F01` — ControlledExternalServiceUse:** External service use meets the applicable identity, contract and outcome conditions.
- **`E03.02.02.F02` — Service Endpoint Trust:** External service endpoints meet the applicable trust conditions.
- **`E03.02.02.F03` — Controlled Outbound Request:** Outbound request is controlled.
- **`E03.02.02.F04` — Response Contract Validation:** Response contract is validated, distinguishing permitted and nonconforming conditions.
- **`E03.02.02.F05` — External Dependency Failure Handling:** External dependency failure is handled.

#### E03.02.03 — Service Interaction Discoverability
Service consumers can identify the supported capabilities and interaction contracts available to them.

- **`E03.02.03.F01` — AccurateServiceContractPublication:** Published service contracts accurately describe the supported behaviour.
- **`E03.02.03.F02` — Discoverable Service Purpose:** Discoverable service has an explicit purpose.
- **`E03.02.03.F03` — Published Contract Currency:** Published contract is current, distinguishing permitted and nonconforming conditions.
- **`E03.02.03.F04` — Consumer Eligibility Clarity:** Consumer eligibility is clear, distinguishing permitted and nonconforming conditions.
- **`E03.02.03.F05` — Interface Deprecation Visibility:** Interface deprecation is visible, with accessible and accurate supporting evidence.

#### E03.02.04 — Consumer-Specific Contract Negotiation
Expose stable service contracts and their compatible versions without binding consumers to implementation topology.

*Demand: B07.02, B10.05, B12.05; evidence: MODEL, EXCH, NFR; findings: R13.*

- **`E03.02.04.F01` — ContractVersionDeclared:** Consumer sees the agreed contract and version.
- **`E03.02.04.F02` — UnsupportedContractRejected:** Unsupported protocol/version is rejected with a bounded explanation.
- **`E03.02.04.F03` — ContractChangeTraceable:** A contract change has a traceable publication and compatibility history.
- **`E03.02.04.F04` — ImplementationTopologyHidden:** Client-facing contract is unchanged by internal provider replacement.
- **`E03.02.04.F05` — ContractAuthnRequired:** Protected contracts cannot be invoked through anonymous fallback.

#### E03.02.05 — Service Invocation Evidence & Accountability
Correlate service calls, consumers and outcomes without treating invocation as completed business work.

*Demand: B05.03, B10.02, B11.05; evidence: AUD, NFR, TH; findings: R16, R18.*

- **`E03.02.05.F01` — InvocationIdentityCorrelated:** Service interaction references requester and execution context.
- **`E03.02.05.F02` — OutcomeDistinctFromRequest:** Accepted invocation is not reported as completed processing.
- **`E03.02.05.F03` — CrossBoundaryTraceCoherent:** Distributed spans can be connected without logging protected content by default.
- **`E03.02.05.F04` — AuthorisationEnforced:** Contract consumption revalidates applicable effective authority.
- **`E03.02.05.F05` — AuditableRejectionRecorded:** Mandatory denied or rejected attempts contribute the required audit evidence.

#### E03.02.06 — Partner Exchange Contract Resolution
Resolve the applicable exchange profile, endpoint and acceptance terms for an external provider or system.

- **`E03.02.06.F01` — PartnerProfileVersionExplicit:** The selected external profile and version are identifiable at the exchange boundary.
- **`E03.02.06.F02` — EndpointAuthorityChecked:** The intended delivery endpoint is associated with the properly authorised partner.
- **`E03.02.06.F03` — ContractUnsupportedExplicit:** Unsupported partner versions or contracts yield explicit rejection or qualification.
- **`E03.02.06.F04` — PartnerChangeEffective:** An updated endpoint/profile follows defined effective-time rules.
- **`E03.02.06.F05` — ExternalAuthorityNotAssumed:** Matching a partner endpoint does not itself prove authority to disclose patient information.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-HIE-GATE, C-SVC-DIR. *Release horizon:* Unscheduled.

### E03.03 — Protocol & Format Mediation
Accommodate supported exchange formats/protocols while preserving meaning.

#### E03.03.01 — Supported Exchange Format Handling
Consumers can exchange supported information formats without needless loss of meaning.

- **`E03.03.01.F01` — CorrectFormatHandling:** Exchanged information is accepted, transformed or rejected according to its applicable format contract.
- **`E03.03.01.F02` — Format Correctness:** Format is correct.
- **`E03.03.01.F03` — Character Set Preservation:** Character set is preserved through relevant changes or disruptions, with recovery where required.
- **`E03.03.01.F04` — Format Variant Visibility:** Format variant is visible, with accessible and accurate supporting evidence.
- **`E03.03.01.F05` — Unsupported Format Rejection:** Unsupported format is rejected.

#### E03.03.02 — Protocol Boundary Adaptation
Different supported interaction mechanisms can be accommodated at their declared boundaries.

- **`E03.03.02.F01` — CorrectProtocolBoundaryOutcome:** Protocol interactions satisfy the declared boundary rules.
- **`E03.03.02.F02` — Correct Protocol Framing:** Correct protocol has an explicit frame.
- **`E03.03.02.F03` — Timeout and Retry Discipline:** Protocol timeouts and retries respect the applicable exchange rules.
- **`E03.03.02.F04` — Protocol Failure Explanation:** Protocol failure is explained.
- **`E03.03.02.F05` — Exchange Boundary Isolation:** Exchange boundary is isolated, including in adverse conditions.

#### E03.03.03 — Native Representation Preservation
Authoritative information representations remain intact where preservation is required.

- **`E03.03.03.F01` — StandardsNativeFidelity:** Required native standards representations remain conformant and retrievable.
- **`E03.03.03.F02` — Standards-Native Data Fidelity:** Standards-native data is faithfully preserved.
- **`E03.03.03.F03` — Original Payload Retention:** Original payload is retained, with accessible and accurate supporting evidence.
- **`E03.03.03.F04` — Transformation Qualification:** Transformation is qualified.
- **`E03.03.03.F05` — Round-Trip Representation Verification:** Round-trip representation is verified.

#### E03.03.04 — Standards-Native Content Preservation
Retain incoming native content and the governed representation relationship when standards require preservation.

*Demand: B10.01, B04.03, B13.05; evidence: IU, EXCH, MODEL; findings: R09, R13.*

- **`E03.03.04.F01` — NativePayloadRetained:** Original standard-native representation is retrievable when governed preservation applies.
- **`E03.03.04.F02` — DerivedMappingLinked:** Any mapped Information Unit refers to its native source provenance.
- **`E03.03.04.F03` — UnknownFieldsReported:** Unmapped or unsupported attributes receive explicit handling qualifications.
- **`E03.03.04.F04` — RoundTripLossMeasured:** Known semantic loss is detected and reported in controlled round trips.
- **`E03.03.04.F05` — NoFakeStandardsConformance:** Conformance reporting distinguishes native compliance and local mapping acceptance.

#### E03.03.05 — Protocol Boundary Isolation
Adapt network and message protocols without exposing transport implementation as a business-domain service contract.

*Demand: B07.02, B05.06; evidence: NFR, EXCH; findings: R16.*

- **`E03.03.05.F01` — ConfigurableBoundaryTimeouts:** MLLP and equivalent boundary timeout policies are tested.
- **`E03.03.05.F02` — SlowPeerIsolation:** One slow or malformed peer does not block unrelated exchanges.
- **`E03.03.05.F03` — AcknowledgementModeConformance:** Configured protocol acknowledgement modes match the applicable standard.
- **`E03.03.05.F04` — BoundaryErrorAttribution:** A transport fault can be correlated to the affected interaction.
- **`E03.03.05.F05` — MalformedPayloadContained:** Failed format handling does not create authoritative clinical information.

### E03.04 — Interaction Acknowledgement & Error Handling
Provide suitable receipts, rejection and error indications for interactions.

#### E03.04.01 — Exchange Receipt Confirmation
An interacting party can learn whether an exchange was received and accepted.

- **`E03.04.01.F01` — CorrectExchangeAcknowledgement:** Exchanges receive the acknowledgement outcome specified by the applicable exchange contract.
- **`E03.04.01.F02` — Timely Transport Acknowledgement:** Transport acknowledgements are issued within the applicable context-defined interval.
- **`E03.04.01.F03` — Standard-Compliant Acknowledgement:** Standard-compliant is acknowledged, distinguishing permitted and nonconforming conditions.
- **`E03.04.01.F04` — Protocol Acknowledgement Correlation:** Each protocol-level acknowledgement can be associated with its corresponding transaction or interaction, preserving the identity and context required to establish that relationship. A protocol acknowledgement alone does not establish application or logical processing success.
- **`E03.04.01.F05` — Durable-Acceptance Signalling:** Durable-acceptance is signalled.

#### E03.04.02 — Actionable Exchange Rejection
Rejected interactions provide sufficient detail for an authorised consumer to respond.

- **`E03.04.02.F01` — UsableRejectionInformation:** Rejected exchanges provide the required actionable rejection information.
- **`E03.04.02.F02` — Precise Rejection Cause:** Exchange rejections identify their precise cause.
- **`E03.04.02.F03` — Recoverable Rejection Classification:** Recoverable rejection is classified through relevant changes or disruptions, with recovery where required.
- **`E03.04.02.F04` — Safe Rejection Handling:** Safe rejection is handled, including in adverse conditions.
- **`E03.04.02.F05` — Rejection Notification Trace:** Rejection notification is traceable, with accessible and accurate supporting evidence.

#### E03.04.03 — Bounded Interaction Outcome
Consumers can determine the result of interactions that fail or exceed prescribed time limits.

- **`E03.04.03.F01` — DeterminateTimedOutcome:** Exchange timeout and failure conditions yield a defined outcome or reconciliation path.
- **`E03.04.03.F02` — Explicit Interaction Completion:** Explicit interaction can be completed.
- **`E03.04.03.F03` — Time-Limited Uncertainty:** Exchange uncertainty is bounded by the applicable time conditions.
- **`E03.04.03.F04` — Idempotent Duplicate Handling:** Idempotent duplicate is handled.
- **`E03.04.03.F05` — Outcome Correlation:** Outcome is correlated.

#### E03.04.04 — End-to-End Exchange State Reconciliation
Make uncertain exchange results discoverable after retries, lost acknowledgements or boundary failure.

*Demand: B04.03, B05.02, B10.01; evidence: EXCH, MN, PERSIST; findings: R16, R06.*

- **`E03.04.04.F01` — UnknownDispositionQuery:** A sender can obtain or reconcile an initially unknown interaction outcome.
- **`E03.04.04.F02` — RetrySafeCorrelation:** Replays do not create unexplained duplicate authoritative content.
- **`E03.04.04.F03` — ResponseCommitConsistency:** Positive business acceptance has durable downstream evidence where promised.
- **`E03.04.04.F04` — FailureStageIdentifiable:** Failure can be attributed to receipt, validation, acceptance or publication stage.
- **`E03.04.04.F05` — OutOfOrderReplyHandled:** Late acknowledgement does not override a newer authoritative result.

#### E03.04.05 — Transport & Application Acknowledgement Distinction
Track transport receipt, application acceptance and authoritative information commitment as separately interpretable outcomes.

- **`E03.04.05.F01` — ReceiptNotProcessing:** Transport receipt never alone asserts successful application processing.
- **`E03.04.05.F02` — ApplicationOutcomeSeparatelyKnown:** Application acceptance or rejection can be distinguished from transport status.
- **`E03.04.05.F03` — DurableCommitNotAssumed:** An application acknowledgement cannot be mistaken for committed storage absent an explicit contract.
- **`E03.04.05.F04` — End-to-End Acknowledgement Correlation:** All acknowledgements associated with a governed transaction can be identified and correlated across applicable protocol, application and logical processing or commitment layers, including multiple acknowledgements relating to the same transaction, with their distinct meanings preserved.
- **`E03.04.05.F05` — UncertainAcknowledgementQualified:** A lost acknowledgement produces a reconciliable uncertain state rather than a silent retry assumption.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-HIE-GATE, C-DOC-INGRESS, DRP-MN. *Release horizon:* Unscheduled.

#### E03.04.06 — Exchange Replay & Duplicate Disposition
Recognise repeated clinical submissions and replay attempts while retaining legitimate event-history semantics.

- **`E03.04.06.F01` — RepeatedExchangeCorrelated:** Replayed submissions correlate to their earlier accepted or attempted exchange identity.
- **`E03.04.06.F02` — IdempotencePolicyApplied:** A repeated delivery does not create unintended duplicate authoritative results under the applicable contract.
- **`E03.04.06.F03` — LegitimateRepeatDistinct:** A clinically new event is not rejected solely because its content resembles a previous event.
- **`E03.04.06.F04` — ReplayOutcomeAuditable:** The disposition of replay, duplicate and corrected submissions can be reviewed.
- **`E03.04.06.F05` — RetryContractVisible:** Senders receive the applicable outcome rather than an ambiguous generic success.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-HIE-GATE, DRP-MN. *Release horizon:* Unscheduled.

#### E03.04.07 — Delayed & Partial Exchange Outcome Reconciliation
Resolve asynchronous and partly completed external exchanges, including failures after intermediate acknowledgements.

- **`E03.04.07.F01` — PendingExchangeVisible:** An exchange pending downstream processing remains identifiable.
- **`E03.04.07.F02` — StageSpecificFailure:** Transport, transform, authorisation, persistence and distribution failures are distinguishable.
- **`E03.04.07.F03` — RecoveryDoesNotLoseAcceptance:** Accepted clinical information is recoverable following the defined processing failure.
- **`E03.04.07.F04` — PartnerOutcomeCorrelated:** The ultimate outcome can be traced to its original partner submission.
- **`E03.04.07.F05` — NoPrematureCompletion:** The exchange is not reported as completed while mandatory obligations remain outstanding.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-HIE-GATE, DRP-MN. *Release horizon:* Unscheduled.

### E03.05 — Exchange Compatibility & Conformance
Validate interactions against applicable authoritative contracts and supported versions.

#### E03.05.01 — Versioned Contract Compatibility
Supported versions of exchange contracts behave as declared.

- **`E03.05.01.F01` — SupportedVersionConformance:** Supported exchange versions conform to the applicable contract, with conformance demonstrated by their required test suites.
- **`E03.05.01.F02` — Supported Contract Version Resolution:** Supported contract version is resolved, distinguishing permitted and nonconforming conditions.
- **`E03.05.01.F03` — Version Upgrade Compatibility:** Version upgrade is compatible.
- **`E03.05.01.F04` — Unsupported Version Explanation:** Unsupported version is explained.
- **`E03.05.01.F05` — Consumer Compatibility Evidence:** Consumer compatibility is evidenced, with accessible and accurate supporting evidence.

#### E03.05.02 — Exchange Deviation Visibility
Material deviations from external contracts can be identified and reported.

- **`E03.05.02.F01` — ReportedContractDeviation:** Exchange contract deviations are detected and surfaced.
- **`E03.05.02.F02` — Nonstandard Variation Detection:** Nonstandard variation is detected.
- **`E03.05.02.F03` — Source Deviation Attribution:** Source deviation is attributable, with accessible and accurate supporting evidence.
- **`E03.05.02.F04` — Deviation Impact Qualification:** Deviation impact is qualified.
- **`E03.05.02.F05` — Deviation Trend Reporting:** Deviation trend is reported, with accessible and accurate supporting evidence.

#### E03.05.03 — Exchange Conformance Evidence
Consumers can examine evidence that supported exchanges meet their obligations.

- **`E03.05.03.F01` — AvailableExchangeEvidence:** Required exchange conformance claims have reviewable supporting evidence.
- **`E03.05.03.F02` — Repeatable Conformance Test:** For each applicable Harmonia release and supported external exchange contract/version, the conformance test suite can be rerun, producing reproducible and reviewable results for positive and negative test cases.
- **`E03.05.03.F03` — Contract Conformance Result:** Contract conformance has an explicit result, distinguishing permitted and nonconforming conditions.
- **`E03.05.03.F04` — Failed Requirement Identification:** Failed requirement is identified.
- **`E03.05.03.F05` — Release Conformance Trace:** Release conformance is traceable, distinguishing permitted and nonconforming conditions.

#### E03.05.04 — Conformance Evidence for External Interfaces
Demonstrate that external interfaces honour authoritative standards, declared deviations and compatible releases.

*Demand: B10.05, B11.05, B07.02; evidence: MODEL, NFR, EXCH; findings: R13.*

- **`E03.05.04.F01` — KnownDeviationRecorded:** Nonconforming external input has a retained deviation record.

- **`E03.05.04.F03` — ExternalContractPrecedence:** Authoritative external contract is respected over internal preference.
- **`E03.05.04.F04` — VersionCompatibilityReported:** Supported and unsupported contract versions are explicit.
- **`E03.05.04.F05` — FailureEvidenceReproducible:** A rejected exchange can be recreated using protected representative evidence.

#### E03.05.05 — External Deviation Qualification & Remediation
Maintain accountable detection, classification and disposition of nonconformant incoming or outgoing healthcare exchanges.

- **`E03.05.05.F01` — DeviationTypeExplicit:** Nonconforming messages yield an interpretable conformance category.
- **`E03.05.05.F02` — ToleratedDeviationQualified:** A tolerated deviation remains visible; adaptation does not silently redefine an external standard.
- **`E03.05.05.F03` — RejectionReasonActionable:** The sender or authorised operator can distinguish correctable from nonrecoverable rejection.
- **`E03.05.05.F04` — DeviationStatisticsReportable:** Conformance exceptions can be grouped by partner, message type and governing profile.
- **`E03.05.05.F05` — ReferenceContractAuthority:** Any authorised local deviation remains explicitly subordinate to the authoritative external contract.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-DOC-INGRESS, C-HIE-GATE. *Release horizon:* Unscheduled.

### E03.06 — Standards-Native Healthcare Interaction Behaviour
Offer standards-native healthcare information exchange with explicitly selected, versioned interaction semantics, authoritative profile precedence, governed exceptions and repeatable evidence of conformance. This is an enterprise capability, independent of transport adapter or information-system ownership.

**Architecture placement:** E03. Existing E03.01–E03.05 retain their definitions. This adds explicit standards-native obligations rather than replacing general exchange capability. E04 continues to own broader semantic meaning and model governance. No capability is allocated to Pylai or another Solution Facet here.

**Business demand:** B04.03 Diagnostic Findings Reporting; B05.06 Community & Cross-Organisation Continuity; B07.02 Service Provider & Network Participation; B10.03 Appropriate Information Access & Sharing; B11.05 Regulatory Conformance & Accreditation. Additional relationships may be discovered; these are hypotheses.

**Evidence:** Governed NFR-052, NFR-088–091, NFR-148; source Continuum EC-08 cross-boundary protocols; existing E03.03/E03.04/E03.05 capability contracts; authoritative HL7 v2.x/FHIR documentation. Candidate contractual boundaries require review.

**Standards profiles:** The selected release, partner, trigger/operation, processing mode and deviations belong in a governed versioned Standards Interaction Profile / external contract—not as fabricated blanket requirements in the capability name. No standard version, complete resource list or unsupported optional interaction is selected here.

#### E03.06.01 — Supported Healthcare Standards Contract Declaration
Select and publish the applicable standards, versions, implementation guides, message/interaction profiles, supported operations and effective rules for each interface; an implementation's supported subset must be unambiguous.

- **`E03.06.01.F01` — SupportedStandardDeclared:** Every enabled healthcare interface exposes an attributable, versioned statement of its supported standards and profiles.
- **`E03.06.01.F02` — UnsupportedBehaviourExplicit:** Requests or messages outside the declared support envelope produce the applicable explicit rejection or qualification.
- **`E03.06.01.F03` — ProfileVersionResolved:** Each interaction is evaluated against the correct effective version and partner-specific contract.
- **`E03.06.01.F04` — AuthoritativeContractPrecedence:** Where a published external standard governs an interaction, a local default cannot silently replace its normative meaning.
- **`E03.06.01.F05` — ContractChangeTraceable:** New or revised standards/profile support has a reviewable release record and an identified effective boundary.

#### E03.06.02 — HL7 v2 Message Interpretation & Structural Validation
Interpret supported HL7 v2.x messages against their declared version, message type, trigger event and sender-specific profile, including structural validation and governed deviations.

- **`E03.06.02.F01` — VersionTriggerAndProfileResolved:** The correct message version, event/structure and effective sender profile are identified or explicitly rejected.
- **`E03.06.02.F02` — EncodingAndSegmentInterpretation:** Applicable delimiters, escaping, repetitions, character encodings, segments and field constraints are interpreted under the selected profile.
- **`E03.06.02.F03` — StructuralDeviationReported:** Unexpected, missing or malformed structures produce attributable conformance findings rather than silent normalisation.
- **`E03.06.02.F04` — SenderSpecificToleranceGoverned:** A configurable tolerated deviation is accepted only when meaning remains reliably interpretable; it stays classified as nonconformant.
- **`E03.06.02.F05` — NativeMessageSemanticsPreserved:** Acceptance, mapping and retention preserve the original message's meaningful content, identifier references and source provenance under applicable policy.

#### E03.06.03 — HL7 v2 Transport & Acknowledgement Choreography
Provide declared HL7 v2.x message transport and acknowledgement behaviour, including MLLP framing and original/enhanced acknowledgement choreography where applicable, without conflating receipt and application outcome.

- **`E03.06.03.F01` — MLLPFramingAndPeerHandling:** Supported MLLP message framing, boundaries and connection lifecycle are validated against normal and malformed peer cases.
- **`E03.06.03.F02` — ConnectionTimeoutsConfigurable:** Connection, I/O and idle timeouts are configurable and verified where applicable without stalling unrelated peers.
- **`E03.06.03.F03` — OriginalEnhancedACKConformant:** The chosen v2 version/profile governs original or enhanced acknowledgement obligations and timing, including relevant MSH-15/MSH-16 semantics.
- **`E03.06.03.F04` — AcknowledgementIdentityAndErrors:** Returned HL7 acknowledgements carry the correct correlation identifiers and applicable accept/error/reject detail under the selected contract.
- **`E03.06.03.F05` — AcceptanceDoesNotOverclaim:** No acknowledgement asserts more acceptance, durability or application processing than the actual declared contract and achieved state warrant.

#### E03.06.04 — FHIR RESTful Server Interaction Support
Expose a FHIR RESTful service whose declared FHIR release, supported resource types, interactions and operations control actual request/response behaviour; this does not mandate all optional FHIR interactions.

- **`E03.06.04.F01` — FHIRCapabilitiesReflectBehaviour:** The supported FHIR API publishes an accurate CapabilityStatement for its declared release and resource interactions.
- **`E03.06.04.F02` — FHIRReadWriteInteractionConformance:** Enabled instance/type/system interactions, such as read, create, update and delete, follow their applicable FHIR/HTTP rules.
- **`E03.06.04.F03` — FHIRSearchAndPagingContract:** Declared search parameters, result bundles, paging and filtering produce standards-conformant outcomes for supported interactions.
- **`E03.06.04.F04` — FHIRConditionalHistoryVersionPolicy:** Declared conditional requests, version-specific read/history, version metadata and concurrency behaviour are honoured where supported.
- **`E03.06.04.F05` — FHIRResponseAndFailureSemantics:** Enabled interactions return FHIR/HTTP status, headers and error representations, including OperationOutcome when prescribed, without claiming unperformed commitment.

#### E03.06.05 — FHIR RESTful Client Interaction Support
Consume external FHIR RESTful interfaces according to their declared capabilities, governing implementation guides and current trust/authorisation context.

- **`E03.06.05.F01` — FHIRPeerCapabilityKnown:** A client can identify or explicitly configure the external FHIR release, capability profile and supported interaction subset.
- **`E03.06.05.F02` — FHIRClientNegotiatedFormats:** Requests and responses use compatible media types, serialization and applicable profile expectations.
- **`E03.06.05.F03` — FHIRClientSearchContinuation:** FHIR search continuation, paging and bundle correlation are correctly handled for supported queries.
- **`E03.06.05.F04` — FHIRClientErrorAndRetryDiscipline:** Failures, conditional conflicts, throttling and uncertain responses follow the declared contract without unsafe blind repetition.
- **`E03.06.05.F05` — FHIRClientIdentityAndDisclosure:** Outbound requests preserve identity, effective authority and governed disclosure obligations rather than assuming endpoint possession conveys permission.

#### E03.06.06 — FHIR Resource, Profile & Terminology Semantics
Support FHIR resource exchange without conflating resource identity/version with the Harmonia Information Unit model, and validate against applicable profiles and terminology where required.

- **`E03.06.06.F01` — FHIRResourceProfileValidated:** Incoming and outgoing resources are assessed against declared FHIR release, StructureDefinitions and relevant implementation-guide constraints.
- **`E03.06.06.F02` — FHIRTerminologyBindingsApplied:** Applicable code-system, value-set and terminology binding expectations are validated using the governed release.
- **`E03.06.06.F03` — FHIRIdentityVersionBoundary:** FHIR resource logical identity and meta.versionId remain distinguishable from Information Unit identity and authoritative version.
- **`E03.06.06.F04` — FHIRReferenceAndBundleConsistency:** Supported reference resolution, bundles and resource relationships preserve semantics or explicitly report uncertainty and failure.
- **`E03.06.06.F05` — FHIRRepresentationMappingFidelity:** Mapping FHIR to/from governed information preserves native source, transformation provenance and known semantic loss/limitations.

#### E03.06.07 — Standards-Specific Conformance & Partner Deviation Governance
Demonstrate that operational behaviour conforms to the chosen external-standard/profile contract, while documenting permitted local adaptation and nonconformant counterpart behaviour.

- **`E03.06.07.F01` — NormativeAndLocalRulesSeparated:** Each tested requirement is traceable to an external normative rule, a governed profile rule or an explicit qualified deviation.
- **`E03.06.07.F02` — PositiveNegativeConformanceRepeatable:** A versioned test suite covers permitted interactions, rejected interactions, error cases and recovery paths.
- **`E03.06.07.F03` — PartnerDeviationAttributable:** A sender/partner-specific deviation and its effective tolerance are discoverable with cause and approval provenance.
- **`E03.06.07.F04` — ReleaseConformanceGate:** A solution release records test results and explicitly declares unsupported or unverified parts of the standards contract.
- **`E03.06.07.F05` — SourceVariationsNotSilentlyCanonical:** Local interpretation or tolerant handling never rewrites the meaning or claimed conformance of a normative standard.

#### E03.06.08 — Additional Healthcare Standard Interaction Profiles
Allow other approved healthcare exchange standards, including established DICOM and secure-messaging considerations, to acquire their own versioned interaction profiles without assuming universal or immediate support.

- **`E03.06.08.F01` — AdditionalStandardSupportExplicit:** DICOM, secure messaging or another standard becomes supported only with an explicit agreed release/contract scope.
- **`E03.06.08.F02` — AdditionalProtocolNativeRules:** An enabled additional standard has its required interaction, transport and content rules bound to a specific version/profile.
- **`E03.06.08.F03` — AdditionalStandardFailureOutcomes:** The standard-specific rejection, error, transport acknowledgement and recovery semantics are separately assessed where applicable.
- **`E03.06.08.F04` — CrossStandardMeaningPreserved:** Any conversion between standards retains identifiable source meaning, provenance and declared mapping limitations.
- **`E03.06.08.F05` — NoImplicitImplementationCommitment:** Mention of a possible standard does not imply that an adapter, endpoint, licence or full feature set already exists.

*Clarification:* A supported standards profile is not itself a claim that a particular implementation conforms. Each relevant release and interface must produce and preserve its own conformance evidence.

### E03.07 — Governed Bulk Data Interconnect
Provide an enterprise-scale, governed mechanism for the exchange of large **populations of information**, including bulk exports, applicable inbound bulk submissions and contractually supported ongoing delivery, without equating bulk transfer to large-media object management or to unrestricted database access. Domain owners retain information selection, semantics, source authority, validation and acceptance; the interconnect provides reusable exchange contracts and controlled transfer capabilities.

**Architecture placement:** Enterprise Capability `E03 — Digital Interoperability & Service Interaction`. The specialised `E08.08 — Evidence Collation, Export & Controlled Release` retains its assurance/evidence responsibility, and `E01.06 — Governed Binary & Large-Media Content Management` retains its media responsibility. E03.07 is general infrastructure-neutral enterprise capability, **not** a declaration of a new deployed subsystem, a distributed analytics engine, or a raw bulk endpoint directly into Mnemosyne.

**Governed evidence:** `enterprise-architecture/design/offline-design/DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md` §§4.1–4.4, 5, 7 (non-dereferenced bulk/on-demand/time-bounded export of LogEvent/AuditEvent/Provenance; specialised Kleio/Dokimasia dereferenced export; authorised release). `DesignNote-Pylai-External-Interoperability-Architecture.md` (Pylai external boundary and Terma protocol behaviour). Existing NFR-087 applies to large individual objects but **does not substitute for bulk population exchange**. Candidate 016 media capability is preserved unchanged.

**Business enablement hypotheses:** Information Stewardship `B10`, cross-organisation sharing/HIE, operational assurance, authorised research/analytics, plus permitted administrative export or migration. These are candidate relationships, not a new authorisation to disclose all information or a commitment to implement an external analytics platform.

**Separation of responsibility:** Domain service + Mneme/Mnemosyne or other governed persistence service defines and supplies eligible records; Sylloge resolves binary content only where required; Pylai is the existing external interoperability boundary; Themis governs access and disclosure; Kleio/Dokimasia specialise evidence export. The candidate does not make an interconnect service the owner of every source record or prescribe an information-system implementation allocation.

**Release qualification:** The DRP establishes non-dereferenced bulk export for evidence-related record domains. Full generic bidirectional imports, continuous feeds and a particular bulk interface standard are *candidate expansions requiring separate R1.x/R2.x scoping*. FHIR Bulk Data (if selected) would be a specific external contract, not an automatic synonym for this general capability. Continuous streaming is not silently mandated.

**Important DRP constraints:** The assurance DRP requires dereferenced AuditEvent/Provenance exports to represent a specified historical point in time, remain interpretable if references later become unavailable, and fail without releasing partial information if any required underlying access is unauthorised. Those domain-specific invariants remain intact and are **not broadened to an unsupported universal all-or-nothing rule for every bulk transfer**.

#### E03.07.01 — Governed Bulk Exchange Contract & Scope
Declare the content domain, provider/consumer roles, permitted modes, contractual rules, selection boundaries and applicable release support before any bulk exchange is undertaken.

- **`E03.07.01.F01` — SupportedScopeDeclared:** Every enabled bulk exchange identifies its source domain, eligible information classes and supported transfer direction.
- **`E03.07.01.F02` — ProviderConsumerRoleKnown:** Each exchange contract identifies which participant supplies, receives and remains authoritative for the data.
- **`E03.07.01.F03` — SelectionAuthorityPreserved:** An exchange request cannot expand a data selection beyond the scope authorised by its owning information domain.
- **`E03.07.01.F04` — ModeSupportExplicit:** The contract states whether bulk export, bulk import, recurring delivery or incremental updates are supported; unsupported modes are rejected.
- **`E03.07.01.F05` — ContractVersionTraceable:** A changed bulk interface contract is versioned, reviewed and tested against its declared consumers.

#### E03.07.02 — Governed Dataset Selection & Version Context
Accept domain-authorised selections of records, versions and time windows; establish interpretable membership and consistency context without becoming an unrestricted information search facility.

- **`E03.07.02.F01` — DomainSelectionRetained:** The interconnect operates on a selection supplied or approved by the accountable information-domain service.
- **`E03.07.02.F02` — VersionScopeDeclared:** Where selected information is versioned, the delivered record versions or version-selection rule are identified.
- **`E03.07.02.F03` — TemporalBasisExplicit:** A time-bounded extract declares the applicable occurrence, creation, persistence or effective-time basis.
- **`E03.07.02.F04` — SnapshotConsistencyQualified:** Where a historical consistency point is required, the resulting package identifies its consistency basis and exceptions.
- **`E03.07.02.F05` — OutOfScopeRecordsExcluded:** Unrelated or unauthorised domain records cannot enter an exchange merely because they are technically discoverable.

#### E03.07.03 — Bulk Extraction & Governed Ingestion
Support governed extraction and submission of authorised information populations, with domain services retaining record interpretation, validation, authoritative acceptance and persistence responsibility.

- **`E03.07.03.F01` — BulkExtractionComplete:** An authorised export contains the declared eligible record population or reports a verifiable incomplete outcome.
- **`E03.07.03.F02` — InboundValidationDelegated:** Inbound bulk content is evaluated by the applicable domain service and is not declared accepted solely upon transport receipt.
- **`E03.07.03.F03` — ReferenceSemanticsPreserved:** Reference-valued data is preserved as such unless the owning export contract explicitly requires and authorises dereferencing.
- **`E03.07.03.F04` — RecordIdentityPreserved:** Transferred records retain the source identities, source references and version markers required by the exchange contract.
- **`E03.07.03.F05` — DomainAcceptanceDistinguished:** Per-domain acceptance, durable commitment and exchange completion are individually reported rather than conflated.

#### E03.07.04 — Bulk Package Manifest & Interpretable Representation
Describe what a bulk exchange contains and how a recipient can independently identify, validate and interpret the delivered records and references.

- **`E03.07.04.F01` — PackageManifestAvailable:** Each completed bulk package identifies contract version, content scope and the constituent parts or accessible references.
- **`E03.07.04.F02` — RepresentationDeclared:** Each part has a declared format and interpretation basis, including applicable standards profiles where used.
- **`E03.07.04.F03` — IntegrityEvidenceAvailable:** Receivers can identify corruption, missing parts or unexpected modifications using applicable integrity metadata.
- **`E03.07.04.F04` — PopulationCompletenessQualified:** The package reports expected and actual population/part counts or equivalent verifiable completeness evidence where applicable.
- **`E03.07.04.F05` — UnresolvedReferenceStatusVisible:** Where references cannot be resolved or were intentionally not dereferenced, the package preserves that fact without implying absent evidence was supplied.

#### E03.07.05 — Bulk Transfer Modes & External Endpoint Integration
Provide an extensible external exchange boundary for large datasets using applicable push, pull, file, object-reference or other contractually selected patterns without coupling to one transport product.

- **`E03.07.05.F01` — PushPullModesContractual:** Configured interfaces distinguish requester-initiated retrieval from producer-initiated delivery where supported.
- **`E03.07.05.F02` — LargeTransferFlowControlled:** Large transfers employ bounded resource use and controlled pressure rather than consuming unbounded memory or obstructing unrelated service traffic.
- **`E03.07.05.F03` — EndpointTrustValidated:** The external endpoint identity, permitted destination and applicable transport protections are validated before exchange.
- **`E03.07.05.F04` — ProtocolBehaviourBounded:** Transport acknowledgements, session results and final package outcomes are distinguished for each supported endpoint pattern.
- **`E03.07.05.F05` — TransportProductReplaceable:** The information contract and outcome semantics remain intelligible if the external transfer mechanism changes.

#### E03.07.06 — Asynchronous Bulk Exchange Lifecycle & Recovery
Manage the progress and exceptional outcomes of long-running bulk exchanges and support bounded recovery, restart or reconciliation according to each contract.

- **`E03.07.06.F01` — ExchangeStateObservable:** An authorised operator or requester can distinguish pending, preparing, transferring, completed, failed and indeterminate exchanges as applicable.
- **`E03.07.06.F02` — CheckpointRecoveryGoverned:** An interrupted exchange can resume or restart only within recorded, contractually permitted recovery semantics.
- **`E03.07.06.F03` — DuplicateEffectsControlled:** Resubmission or replay is identified and handled without silently duplicating domain effects.
- **`E03.07.06.F04` — BackpressureAndAdmissionControlled:** Overcapacity exchanges are delayed or rejected with explicit status while accepted obligations remain tracked.
- **`E03.07.06.F05` — UncertainOutcomeRetained:** Network or recipient ambiguity is recorded as indeterminate until supported reconciliation resolves it.

#### E03.07.07 — Bulk Disclosure & Access Enforcement
Apply authorisation, information-protection and accountable disclosure to the whole exchange and to the underlying information without creating a privileged bulk-access bypass.

- **`E03.07.07.F01` — ThemisAuthorisationApplied:** Source selection, retrieval and disclosure use applicable Themis policy enforcement and the local PEP at each controlling boundary.
- **`E03.07.07.F02` — NoImplicitBulkPrivilege:** Possession of a bulk interconnect operation does not grant access to constituent information or across compartments.
- **`E03.07.07.F03` — RecipientAndPurposeBounded:** Where required by policy, destination identity, use/purpose and disclosure constraints are verified prior to release.
- **`E03.07.07.F04` — RequiredAtomicReleaseEnforced:** Where an owning export contract requires all-or-nothing release, incomplete or unauthorised packages cannot be disclosed.
- **`E03.07.07.F05` — RevocationAndFailureProtected:** A relevant authorisation failure, revocation or cancelled exchange is handled without uncontrolled release of staged information.

#### E03.07.08 — Bulk Exchange Reconciliation, Evidence & Reuse
Provide independent transfer evidence and reusable outcomes while preserving the separation between transport facts, information authority and domain-specific audit or assurance meaning.

- **`E03.07.08.F01` — EndToEndTransferAccountable:** The exchange records attributable origin, destination, declared scope and outcome without conflating them with clinical or assurance conclusions.
- **`E03.07.08.F02` — PartLevelReconciliationAvailable:** Where supported, source and recipient can reconcile missing, rejected, accepted and indeterminate package parts.
- **`E03.07.08.F03` — DomainEvidenceResponsibilitiesIntact:** Kleio, Dokimasia and other owning domains retain authority over their own export interpretation and specialised evidence.
- **`E03.07.08.F04` — NoExternalAnalyticsDependency:** A deployment can operate without an external data lake or analytics platform unless an explicit solution contract requires one.
- **`E03.07.08.F05` — MultipleDomainReuseDemonstrated:** The interconnect pattern supports at least two independently governed record domains without duplicating the transport framework.

## E04 — Semantic Meaning & Model Governance
The system can preserve, interpret, transform and evolve the meaning of managed information.

### E04.01 — Information Model Definition & Resolution
Publish and use controlled definitions of information.

#### E04.01.01 — Governed Information Model Publication
Consumers can obtain the authoritative definitions applicable to governed information.

- **`E04.01.01.F01` — AvailableModelDefinition:** Consumers can resolve valid requests for the correct model and version definition.
- **`E04.01.01.F02` — Approved Model Availability:** Approved model is available.
- **`E04.01.01.F03` — Model Definition Integrity:** Model definition retains its integrity.
- **`E04.01.01.F04` — Model Release Identification:** Model release is identified.
- **`E04.01.01.F05` — Superseded Model Visibility:** Superseded model is visible, with accessible and accurate supporting evidence.

#### E04.01.02 — Versioned Model Resolution
Consumers can identify and access the precise model definition governing particular information and the applicable versioned reference datasets, including approved publication, lifecycle and historical references needed for interpretation.

- **`E04.01.02.F01` — CorrectModelVersionBinding:** Information is bound to the correct applicable model version.
- **`E04.01.02.F02` — Exact Model Version Access:** Exact model version can be accessed.
- **`E04.01.02.F03` — Version Resolution Provenance:** Version resolution retains its provenance.
- **`E04.01.02.F04` — Missing Model Qualification:** Missing model is qualified.
- **`E04.01.02.F05` — Versioned Reference Data Management:** The lifecycle, approved publication, retrieval and historical referencing of versioned reference datasets are governed, including terminology value sets, application reference data and enumerated values. Consumers can identify and access the applicable dataset version; historical model definitions remain available for interpretation. This does not require every enumeration consumer to retrieve data dynamically at runtime.

#### E04.01.03 — Ontology-Tied Model Resolution
Governed information models can be resolved under the applicable ontology and version. Ontology concepts, relationships, semantics and historical releases remain managed and available for interpretation in their applicable context.

*Demand: B10.05, B13.06; evidence: MODEL, IU, REF; findings: R13, R09.*

- **`E04.01.03.F01` — OntologyVersionPinned:** Model resolution includes associated ontology release identity.
- **`E04.01.03.F02` — Ontology Management:** Ontologies and their defined concepts, relationships and semantics are managed, versioned and made available, including historical versions needed to interpret information within its applicable semantic and model context.
- **`E04.01.03.F03` — UnknownDefinitionQualified:** Unknown Topic or class version is not silently replaced by latest.
- **`E04.01.03.F04` — IndependentReleaseCorrelated:** Dependent component acceptance identifies the exact published model release.
- **`E04.01.03.F05` — DefinitionIntegrityVerifiable:** Resolved model metadata matches its governed release evidence.

#### E04.01.04 — Governed Topic Identity & Namespace
Preserve meaning and canonical identity of Topics and named references across authoring, distribution and runtime.

*Demand: B10.05, B10.07; evidence: REF, MODEL; findings: R05, R09.*

- **`E04.01.04.F01` — TopicIdentityStable:** Accepted Topic identities survive rename and reclassification.
- **`E04.01.04.F02` — CanonicalNameDerived:** Canonical names reflect governed Composition ancestry where defined.
- **`E04.01.04.F03` — AliasAmbiguityExposed:** Conflicting aliases cannot silently select an incorrect Topic.
- **`E04.01.04.F04` — RuntimeDefinitionImmutable:** Accepted runtime Topic definitions cannot mutate without release.
- **`E04.01.04.F05` — HierarchyMatchingConformant:** Containment matching uses the governed scope and Topic definition.

### E04.02 — Terminology & Value Set Management
Maintain controlled clinical/operational vocabularies and their versions.

#### E04.02.01 — Terminology Resolution
Consumers can resolve supported coded terms to authoritative meaning.

- **`E04.02.01.F01` — CorrectTerminologyResolution:** Coded terms resolve against the required vocabulary versions.
- **`E04.02.01.F02` — Code Meaning Accuracy:** Code meaning is accurate.
- **`E04.02.01.F03` — Terminology Version Awareness:** Terminology version is made known.
- **`E04.02.01.F04` — Unresolvable Code Explanation:** Unresolvable code is explained.
- **`E04.02.01.F05` — Source Code Preservation:** Source code is preserved, with accessible and accurate supporting evidence.

#### E04.02.02 — Versioned Value Set Access
Consumers can obtain the correct approved value-set membership for a specified release.

- **`E04.02.02.F01` — ApprovedValueSetConsistency:** Released value sets are consistent with their approved versions.
- **`E04.02.02.F02` — Published Value Set Version:** Consumers can retrieve the approved membership and definition of a specified published value-set version, with sufficient version identification to distinguish it from other available versions.
- **`E04.02.02.F03` — Value Set Membership Integrity:** Value set membership retains its integrity.
- **`E04.02.02.F04` — Stable Value Set Reference:** Value-set references retain stable identity and meaning.
- **`E04.02.02.F05` — Unsupported Version Qualification:** Unsupported version is qualified.

#### E04.02.03 — Terminology Change Visibility
Consumers can determine whether terminology changes affect their interpretation of information.

- **`E04.02.03.F01` — ExplainableTerminologyChange:** Governed terminology changes provide accessible compatibility and change details.
- **`E04.02.03.F02` — Changed Concept Identification:** Changed concept is identified.
- **`E04.02.03.F03` — Meaningful Deprecation Notice:** Meaningful deprecation is made known.
- **`E04.02.03.F04` — Affected Model Awareness:** Affected model is made known.
- **`E04.02.03.F05` — Terminology Release Trace:** Terminology release is traceable, with accessible and accurate supporting evidence.

#### E04.02.04 — Terminology Publication Stability
Prevent unreviewed dynamic terminology changes from altering operational meaning within an accepted release.

*Demand: B10.05, B04.04, B13.06; evidence: MODEL, NFR; findings: R13.*

- **`E04.02.04.F01` — Published Value Set Stability:** The definition and membership of a published value-set version remain semantically stable throughout the applicable governed release, so repeated retrieval produces semantically consistent results without unauthorised modification. New versions remain subject to release and version governance; transport serialisation need not be byte-identical.
- **`E04.02.04.F02` — UnapprovedUpdateBlocked:** Runtime terminology changes bypassing release governance are rejected.
- **`E04.02.04.F03` — DeprecatedTermQualified:** Deprecated concepts remain distinguishable from currently approved concepts.
- **`E04.02.04.F04` — TerminologyRollbackTraceable:** A rollback retains release and validation evidence.
- **`E04.02.04.F05` — DependentRegressionsMeasured:** Terminology changes trigger bounded conformance regression suites.

### E04.03 — Semantic Transformation
Translate supported representations without hiding material meaning loss.

#### E04.03.01 — Governed Representation Translation
Information can be translated among supported representations without unreported meaning loss.

- **`E04.03.01.F01` — SemanticallyConformantTranslation:** Terminology mappings preserve the required meaning.
- **`E04.03.01.F02` — Meaning Preservation Evidence:** Meaning preservation is evidenced, with accessible and accurate supporting evidence.
- **`E04.03.01.F03` — Translation Rule Version:** Translation rule has an identifiable version.
- **`E04.03.01.F04` — Qualified Translation Loss:** Qualified translation loss is controlled.
- **`E04.03.01.F05` — Repeatable Translation Outcome:** Repeatable translation has an explicit outcome through relevant changes or disruptions, with recovery where required.

#### E04.03.02 — Transparent Transformation Qualifications
Consumers can discover significant limitations or loss during translation.

- **`E04.03.02.F01` — VisibleTranslationQualifications:** Terminology translation qualifications are identified and conveyed.
- **`E04.03.02.F02` — Transformation Limitation Disclosure:** Transformation limitation is disclosed, with accessible and accurate supporting evidence.
- **`E04.03.02.F03` — Source-to-Target Traceability:** Source-to-target is traceable.
- **`E04.03.02.F04` — Unsupported Mapping Recognition:** Unsupported mapping is recognised, with accessible and accurate supporting evidence.
- **`E04.03.02.F05` — Qualification Consumer Visibility:** Qualification consumer is visible.

#### E04.03.03 — Loss-Aware Semantic Transformation
Transform representations while exposing provenance, semantic degradation and incompatibility.

*Demand: B02.01, B04.03, B10.05; evidence: MODEL, IU, EXCH; findings: R09, R13.*

- **`E04.03.03.F01` — UnmappedConceptsReported:** No unrepresentable source semantics vanish without qualification.
- **`E04.03.03.F02` — TransformationVersionPinned:** Mapping rules and versions are recoverable for every governed derivative.
- **`E04.03.03.F03` — MeaningEquivalentWhereClaimed:** Semantic equivalence claims conform to their reference examples and adverse conditions.
- **`E04.03.03.F04` — OriginalRepresentationRetrievable:** Original preserved information remains referenceable.
- **`E04.03.03.F05` — TransformationProvenanceLinked:** Output IU is linked to source IU and transform evidence.

#### E04.03.04 — Identity-Preserving Clinical Translation
Preserve subject/entity references, uncertainty and source semantics through standards conversion and canonical information mapping.

- **`E04.03.04.F01` — SourceIdentifierProvenance:** A translated identifier retains its source/issuer semantics.
- **`E04.03.04.F02` — NoInventedPatientMatch:** Transformation does not create an authoritative patient association without identity evidence.
- **`E04.03.04.F03` — LossQualificationReported:** Untranslatable clinically meaningful or security-related semantics are reported or explicitly qualified.
- **`E04.03.04.F04` — NativeRepresentationTraceable:** The source-native representation remains attributable and retrievable under applicable governance.
- **`E04.03.04.F05` — TranslationVersionReproducible:** A translated output can be attributed to the model/terminology release used.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: DRP-IU, C-DOC-INGRESS. *Release horizon:* Unscheduled.

### E04.04 — Semantic Validation
Determine conformance to applicable information models and terminology.

#### E04.04.01 — Information Model Conformance Checking
A model verification framework assesses structural definitions, relationships, constraints and invariants, reference data, value-set and terminology dependencies, coherence, completeness and conformity with governing specifications. Schema and cardinality assessment, precise findings, version-specific validation and retained evidence remain supported. Model findings can inform publication eligibility without owning publication or establishing conformance of every information instance. Consumers can also determine information-instance conformance to the applicable model through schema, cardinality and version-specific validation; model verification findings and instance-validation findings remain distinct.

- **`E04.04.01.F01` — Model Verification Conformance:** Models are verified against their governing specifications for structural definitions, relationships, constraints and invariants, reference data, value-set and terminology dependencies, coherence and completeness. Findings establish model conformance without implying that all information instances conform or assuming ownership of model publication.
- **`E04.04.01.F02` — Schema and Cardinality Assessment:** Model structural definitions and applicable information instances can be assessed for schema and cardinality conformance, with model-level and instance-level findings distinguished.
- **`E04.04.01.F03` — Precise Conformance Findings:** Precise conformance findings are available, distinguishing permitted and nonconforming conditions.
- **`E04.04.01.F04` — Version-Specific Validation:** Model verification and information-instance validation use the applicable model version, preserving the version context needed to interpret their distinct findings.
- **`E04.04.01.F05` — Validation Evidence Retention:** Validation evidence is retained.

#### E04.04.02 — Terminology Conformance Checking
Consumers can determine whether coded information conforms to applicable terminology.

- **`E04.04.02.F01` — Terminology Conformance:** Information codes and values are checked against the applicable terminology bindings and versions, with conforming and nonconforming values correctly distinguished.
- **`E04.04.02.F02` — Permitted Code Membership:** Permitted code has identifiable membership.
- **`E04.04.02.F03` — Applicable Terminology Version:** Applicable terminology has an identifiable version.
- **`E04.04.02.F04` — Invalid Code Classification:** Invalid code is classified.
- **`E04.04.02.F05` — Terminology Finding Attribution:** Terminology finding is attributable, with accessible and accurate supporting evidence.

#### E04.04.03 — Contextual Semantic Compatibility
Related information can be checked for known incompatible semantic versions or constraints.

- **`E04.04.03.F01` — IdentifiedSemanticIncompatibility:** Known semantic incompatibilities are detected.
- **`E04.04.03.F02` — Domain Context Recognition:** Domain context is recognised, with accessible and accurate supporting evidence.
- **`E04.04.03.F03` — Conflicting Semantic Assumptions:** Conflicting semantic assumptions can be identified.
- **`E04.04.03.F04` — Qualified Partial Compatibility:** Qualified partial is compatible.
- **`E04.04.03.F05` — Context Preservation across Mapping:** Context preservation across can be mapped through relevant changes or disruptions, with recovery where required.

#### E04.04.04 — Context-Dependent Rule Evaluation
Evaluate model, terminology and contextual invariants without confusing semantic validity with clinical judgement.

*Demand: B04.04, B02.04, B10.07; evidence: MODEL, REF, SEC; findings: R09, R13.*

- **`E04.04.04.F01` — ContextRulesVersioned:** Rule results cite the applicable semantic release.
- **`E04.04.04.F02` — UnknownContextReported:** Missing context yields an indeterminate or incomplete result rather than false validation.
- **`E04.04.04.F03` — RuleConflictDetected:** Contradictory constraints produce an attributable finding.
- **`E04.04.04.F04` — DataNotClinicalDecision:** A semantically valid result does not assert clinical correctness.
- **`E04.04.04.F05` — ValidationEvidenceRepeatable:** Running the same rules against an unchanged snapshot repeats the outcome.

### E04.05 — Model Change & Release Governance
Manage compatibility and changes as controlled architectural releases.

#### E04.05.01 — Controlled Semantic Publication
Model and terminology updates can be published in approved, reproducible releases.

- **`E04.05.01.F01` — ReproducibleSemanticRelease:** Semantic releases can be reproduced from their accepted inputs.
- **`E04.05.01.F02` — Release Approval Evidence:** Release approval is evidenced, distinguishing permitted and nonconforming conditions.
- **`E04.05.01.F03` — Published Artifact Immutability:** Published artifact is immutable.
- **`E04.05.01.F04` — Publication Dependency Validation:** Publication dependency is validated.
- **`E04.05.01.F05` — Rollback Reference Availability:** Rollback reference is available.

#### E04.05.02 — Model Compatibility Assessment
Consumers can determine whether a model change is compatible with supported information.

- **`E04.05.02.F01` — AvailableCompatibilityDetermination:** Relevant semantic changes have evidence-supported compatibility findings.
- **`E04.05.02.F02` — Dependent Model Impact Analysis:** Dependent model impact can be analysed.
- **`E04.05.02.F03` — Backward Compatibility Classification:** Backward compatibility is classified.
- **`E04.05.02.F04` — Breaking Change Disclosure:** Breaking change is disclosed, with accessible and accurate supporting evidence.
- **`E04.05.02.F05` — Compatibility Review Evidence:** Compatibility review is evidenced, with accessible and accurate supporting evidence.

#### E04.05.03 — Version Transition Guidance
Consumers can understand the changes and restrictions accompanying semantic evolution.

- **`E04.05.03.F01` — UsableVersionTransitionAdvice:** Version transitions provide sufficient guidance and provenance for consumers.
- **`E04.05.03.F02` — Source Version Eligibility:** Source version has its eligibility established, distinguishing permitted and nonconforming conditions.
- **`E04.05.03.F03` — Required Migration Action:** Required migration receives action.
- **`E04.05.03.F04` — Transition Qualification Reporting:** Transition qualification is reported.
- **`E04.05.03.F05` — Post-Transition Model Validation:** Post-transition model is validated.

#### E04.05.04 — Controlled Semantic Change Impact
Determine whether revised definitions affect stored meaning, clients, transformations or accepted behaviour.

*Demand: B10.05, B11.05, B13.06; evidence: MODEL, IU, NFR; findings: R13.*

- **`E04.05.04.F01` — DependencyImpactEnumerated:** Affected contracts, topics and transformations are identified before release.
- **`E04.05.04.F02` — MigrationNeedExplicit:** Model changes requiring data migration are distinguished from compatible extensions.
- **`E04.05.04.F03` — RegressionEvidenceLinked:** Accepted release records associate test evidence and exceptions.
- **`E04.05.04.F04` — RollbackCompatibilityKnown:** Rollback viability and information state risk are classified.
- **`E04.05.04.F05` — IndependentReleaseGateEnforced:** Dependent release activation is gated where architecturally required.

#### E04.05.05 — Reference Dataset Release & Reproducibility
Provide repeatable access to versioned datasets and terminology references needed for conformance and clinical information.

*Demand: B10.05, B13.05, B11.05; evidence: MODEL, NFR; findings: R13, R23.*

- **`E04.05.05.F01` — DatasetSnapshotReproducible:** A named dataset version yields identical governed content on repeat.
- **`E04.05.05.F02` — LatestVersusPinnedExplicit:** Consumers can distinguish latest from snapshot-selection semantics.
- **`E04.05.05.F03` — ReferenceSourceAttributable:** Published reference data retains source and acceptance provenance.
- **`E04.05.05.F04` — UnauthorisedRefreshBlocked:** Changes without release approval do not alter accepted dataset content.
- **`E04.05.05.F05` — RegressionSetAvailable:** A prior release can be tested against its associated reference dataset.

## E05 — Identity, Trust & Information Protection
The system can establish, propagate and enforce appropriate digital trust and access boundaries.

### E05.01 — Digital Identity & Authentication
Establish the authenticated identity of an interacting principal.

#### E05.01.01 — Authenticated Digital Interaction
The system can establish authenticated identity at a required access boundary.

- **`E05.01.01.F01` — AuthenticationOutcomeCorrectness:** Authentication correctly establishes identity or denies the interaction under the applicable conditions.
- **`E05.01.01.F02` — Verified Principal Authentication:** Verified principal can be authenticated.
- **`E05.01.01.F03` — Authentication Failure Disclosure:** Authentication failure is disclosed, with accessible and accurate supporting evidence.
- **`E05.01.01.F04` — Authentication Session Validity:** Authentication session is valid.
- **`E05.01.01.F05` — Restricted Anonymous Access:** Restricted anonymous can be accessed.

#### E05.01.02 — Trusted System Interaction
Internal Harmonia systems, components and automated principals can authenticate to the services they are authorised to use, establishing valid identity and trust and rejecting expired or revoked trust.

- **`E05.01.02.F01` — VerifiedSystemIdentity:** Machine interactions establish the required mutual identity and trust.
- **`E05.01.02.F02` — Peer Trust Evidence:** Peer trust is evidenced, with accessible and accurate supporting evidence.
- **`E05.01.02.F03` — Mutual Identity Confirmation:** Mutual identity is confirmed.
- **`E05.01.02.F04` — Internal Expired Trust Rejection:** Expired or revoked machine and component trust is rejected within Harmonia’s internal subsystem boundary.
- **`E05.01.02.F05` — System Trust Rotation:** System trust can be rotated.

#### E05.01.03 — Authentication Evidence Availability
Required authentication outcomes are attributable for subsequent oversight.

- **`E05.01.03.F01` — ReviewableAuthenticationEvidence:** Authentication outcomes retain complete required evidence for review.
- **`E05.01.03.F02` — Authenticated Action Attribution:** Authenticated action is attributable, with accessible and accurate supporting evidence.
- **`E05.01.03.F03` — Evidence Integrity:** Evidence retains its integrity.
- **`E05.01.03.F04` — Evidence Retrieval Scope:** Evidence retrieval has an explicit scope, distinguishing permitted and nonconforming conditions.
- **`E05.01.03.F05` — Evidence Retention Status:** Evidence retention has an explicit status, with accessible and accurate supporting evidence.

#### E05.01.04 — Machine-to-Machine Trust Continuity
Services and workloads authenticate across the boundary between Harmonia and external systems, maintaining valid machine-to-machine trust and rejecting expired or revoked trust without conflating workload identity with human business authority.

*Demand: B07.02, B12.04, B10.03; evidence: TH, NFR; findings: R02, R22.*

- **`E05.01.04.F01` — WorkloadIdentityBound:** Calls carry verifiable workload identity independent of user identity.
- **`E05.01.04.F02` — DelegatedAuthorityExplicit:** Machine identity alone does not imply human purpose-scoped permission.
- **`E05.01.04.F03` — CredentialRotationContinuity:** Credential rollover does not require ungoverned anonymous fallback.
- **`E05.01.04.F04` — External Expired Trust Rejection:** Expired or revoked machine-to-machine trust is rejected at the boundary between Harmonia and external systems.
- **`E05.01.04.F05` — TrustEventAccountable:** Trust failures carry suitable diagnostic/audit correlation.

### E05.02 — Contextual Authorisation Determination
Determine whether an action is permitted within its applicable context.

#### E05.02.01 — Context-Sensitive Access Decision
A requested action can be assessed against governing policies and circumstances.

- **`E05.02.01.F01` — CorrectContextualDecision:** Authorisation decisions correctly apply the governing context and policy.
- **`E05.02.01.F02` — Action and Compartment Evaluation:** Action and compartment is evaluated.
- **`E05.02.01.F03` — Context Snapshot Validity:** Context snapshot is valid.
- **`E05.02.01.F04` — Denial Reason Qualification:** Denial reason is qualified.
- **`E05.02.01.F05` — Determination Repeatability:** Determination is repeatable through relevant changes or disruptions, with recovery where required.

#### E05.02.02 — Purpose-Scoped Permission
Information access can be constrained to the authorised purpose or compartment.

- **`E05.02.02.F01` — CorrectPurposeAndCompartmentDecision:** Authorisation decisions correctly apply purpose and compartment conditions.
- **`E05.02.02.F02` — Purpose Requirement Evaluation:** Purpose requirement is evaluated.
- **`E05.02.02.F03` — Purpose Mismatch Rejection:** Purpose mismatch is rejected.
- **`E05.02.02.F04` — Purpose Change Recognition:** Purpose change is recognised, with accessible and accurate supporting evidence.
- **`E05.02.02.F05` — Granted Scope Clarity:** Granted scope is clear, distinguishing permitted and nonconforming conditions.

#### E05.02.03 — Subject-Specific Required Criteria
Evaluate protected-subject requirements separately from the effective authority presented by the requester.

*Demand: B10.03, B10.04, B14.06; evidence: TH, IU, REF; findings: R22.*

- **`E05.02.03.F01` — CriteriaProviderEncapsulated:** Evaluation does not require consumers to parse protected-object internals.
- **`E05.02.03.F02` — ActionMaskIndependent:** Action limitations are applied independently of compartment restrictions.
- **`E05.02.03.F03` — CompartmentMaskCorrect:** Classification/compartment conditions alter outcome as defined.
- **`E05.02.03.F04` — TimeContextApplied:** Temporal and contextual changes can revoke permission to an unchanged subject.
- **`E05.02.03.F05` — DeniedCriteriaExplainedSafely:** Rejection exposes safe reason classes without protected criteria leakage.

### E05.03 — Local Access Policy Enforcement
Apply authorisation decisions at every relevant component boundary.

#### E05.03.01 — Boundary-Enforced Access
Each relevant boundary can apply its own required access decision.

- **`E05.03.01.F01` — LocalEnforcementConformance:** Local boundaries enforce the required authorisation conditions.
- **`E05.03.01.F02` — Local Enforcement Availability:** Local enforcement is available.
- **`E05.03.01.F03` — No Unchecked Entry Path:** Unchecked entry path is prevented.
- **`E05.03.01.F04` — Policy Decision Conformance:** Policy decision conforms to the applicable requirements, distinguishing permitted and nonconforming conditions.
- **`E05.03.01.F05` — Compromised Boundary Detection:** Compromised boundary is detected.

#### E05.03.02 — Rejected Unauthorised Action
Prohibited requests are rejected without unintended side effects.

- **`E05.03.02.F01` — SafeUnauthorisedRejection:** Rejected unauthorised operations leave governed state unchanged.
- **`E05.03.02.F02` — Deterministic Denial Outcome:** Deterministic denial has an explicit outcome.
- **`E05.03.02.F03` — Non-Disclosure on Rejection:** Non-disclosure on is rejected, with accessible and accurate supporting evidence.
- **`E05.03.02.F04` — Denial Evidence Availability:** Denial evidence is available, with accessible and accurate supporting evidence.
- **`E05.03.02.F05` — Controlled Error Response:** Controlled error response reaches and can be understood by the intended recipient.

#### E05.03.03 — Enforcement Decision Traceability
Required access-decision evidence is retained near the enforcing boundary.

- **`E05.03.03.F01` — AttributableEnforcementEvidence:** Controlled actions retain the required decision context as reviewable, attributable evidence.
- **`E05.03.03.F02` — Policy Version Attribution:** Policy version is attributable, distinguishing permitted and nonconforming conditions.
- **`E05.03.03.F03` — Enforcement Context Preservation:** Enforcement context is preserved through relevant changes or disruptions, with recovery where required.
- **`E05.03.03.F04` — Decision-to-Action Correlation:** Decision-to-action is correlated.
- **`E05.03.03.F05` — Trace Access Protection:** Trace access is protected, with accessible and accurate supporting evidence.

#### E05.03.04 — Local PEP Decision Integrity
Require independently enforced authorisation at protected service, information and workflow boundaries.

*Demand: B01.05, B10.03, B14.06; evidence: TH, SEC; findings: R02, R22.*

- **`E05.03.04.F01` — EveryProtectedBoundaryChecks:** Selected protected boundaries demonstrably invoke local enforcement.
- **`E05.03.04.F02` — InheritedScopeHonoured:** Contained-scope inheritance never exceeds approved bounds.
- **`E05.03.04.F03` — ForwardScopeConstrained:** Forwarded authority remains limited by applicable originating criteria.
- **`E05.03.04.F04` — FailureDefaultsDeny:** Evaluation failure or missing context cannot produce permit.
- **`E05.03.04.F05` — PEPDecisionEvidence:** Permit/deny result is attributable without disclosing credentials or unnecessary PHI.

### E05.04 — Security Context & Passport Propagation
Carry governed security context with exchanges without distributing credentials.

#### E05.04.01 — Governed Security Context Transfer
Required governing context can accompany information and work exchanges.

- **`E05.04.01.F01` — CompleteGovernedPassport:** Applicable exchanges carry complete, valid governance context.
- **`E05.04.01.F02` — Passport Integrity:** Passport retains its integrity.
- **`E05.04.01.F03` — Applicable Context Propagation:** Applicable context propagation is identifiable.
- **`E05.04.01.F04` — Context Expiry Recognition:** Context expiry is recognised, with accessible and accurate supporting evidence.
- **`E05.04.01.F05` — Boundary Acceptance Qualification:** Boundary acceptance is qualified.

#### E05.04.02 — Credential-Free Context Propagation
Security decisions can be informed across boundaries without transmitting credentials as context.

- **`E05.04.02.F01` — CredentialExclusionConformance:** Exchanges preserve the applicable credential-exclusion controls.
- **`E05.04.02.F02` — No Credential Leakage:** Credential leakage is prevented, distinguishing permitted and nonconforming conditions.
- **`E05.04.02.F03` — Authorisation Snapshot Fidelity:** Authorisation snapshot is faithfully preserved, distinguishing permitted and nonconforming conditions.
- **`E05.04.02.F04` — Downstream Context Validity:** Downstream context is valid.
- **`E05.04.02.F05` — Rejection of Untrusted Context:** Rejection of untrusted has its relevant context.

#### E05.04.03 — Operational Passport Currency
Make current effective authority available to distributed consumers while preventing use of invalid or expired manifestations.

*Demand: B10.03, B10.04, B14.06; evidence: TH, SEC, MN; findings: R01, R02, R22.*

- **`E05.04.03.F01` — ExpiredPassportDenied:** An expired Passport never authorises a protected action.
- **`E05.04.03.F02` — InvalidationPropagated:** Revocation reaches subscribed enforcement points under the defined recovery conditions.
- **`E05.04.03.F03` — DisconnectedConsumerFailsSafe:** Loss of policy currency does not create unchecked authority.
- **`E05.04.03.F04` — OperationalCopyDistinguished:** Operational Passport storage is not mistaken for authoritative policy state.
- **`E05.04.03.F05` — PassportFreshnessAssessable:** Consumers can assess currency against governing policy epoch or equivalent contract.

### E05.05 — Information Protection & Security Evidence
Protect information and produce necessary security accountability evidence.

#### E05.05.01 — Protected Information Exchange
Information is appropriately protected while crossing system boundaries.

- **`E05.05.01.F01` — ProtectedExchangeConformance:** Information exchanges meet the required protection conditions.
- **`E05.05.01.F02` — Encrypted Exchange Channel:** Information exchange channels provide the required encryption through relevant changes or disruptions, with recovery where required.
- **`E05.05.01.F03` — Protocol Protection Coverage:** Protocol protection is covered.
- **`E05.05.01.F04` — Invalid Protection Rejection:** Invalid protection is rejected.
- **`E05.05.01.F05` — Protection Failure Evidence:** Protection failure is evidenced, with accessible and accurate supporting evidence.

#### E05.05.02 — Protected Information Custody
Sensitive information remains appropriately protected in storage and retrieval.

- **`E05.05.02.F01` — ProtectedCustodyConformance:** Information in custody is protected according to its classification and applicable controls.
- **`E05.05.02.F02` — Encrypted Custody Confirmation:** Encrypted custody confirmation reaches and can be understood by the intended recipient.
- **`E05.05.02.F03` — Key Change Continuity:** Key change is maintained through relevant changes or disruptions, with recovery where required.
- **`E05.05.02.F04` — Protection Scope Coverage:** Protection scope is covered, distinguishing permitted and nonconforming conditions.
- **`E05.05.02.F05` — Unprotected Content Detection:** Unprotected content is detected, including in adverse conditions.

#### E05.05.03 — Isolation Constraint Enforcement
Information subject to isolation constraints remains within its permitted boundaries.

- **`E05.05.03.F01` — CorrectIsolationHandling:** Information isolation policy conditions receive the correct governed handling.
- **`E05.05.03.F02` — Isolated Information Non-Disclosure:** Isolated information is not disclosed, with accessible and accurate supporting evidence.
- **`E05.05.03.F03` — Isolation Change Authorization:** Isolation change has the applicable authorisation, including in adverse conditions.
- **`E05.05.03.F04` — Cross-Context Isolation Accuracy:** Cross-context isolation is accurate, including in adverse conditions.
- **`E05.05.03.F05` — Isolation Violation Alerting:** Isolation violation produces an alert, including in adverse conditions.

#### E05.05.04 — Security Accountability Evidence
Required evidence of significant security activity can be reviewed.

- **`E05.05.04.F01` — ReviewableSecurityEvidence:** Significant security events retain their required attributes as reviewable evidence.
- **`E05.05.04.F02` — Sensitive-Data-Minimised Security Events:** Security events minimise sensitive data while retaining the required evidence.
- **`E05.05.04.F03` — Evidence Correlation:** Evidence is correlated, with accessible and accurate supporting evidence.
- **`E05.05.04.F04` — Security Event Integrity:** Security event retains its integrity.
- **`E05.05.04.F05` — Governed Security Evidence Access:** Governed security evidence can be accessed, distinguishing permitted and nonconforming conditions.

#### E05.05.05 — Security Label & Isolation Assurance
Preserve information classifications and isolation constraints across storage, processing, references and distributed exchange.

*Demand: B10.03, B10.06, B14.06; evidence: IU, SEC, TH; findings: R22, R05.*

- **`E05.05.05.F01` — LabelPropagationComplete:** Governed information derivatives retain applicable security labels.
- **`E05.05.05.F02` — IsolationCrossingBlocked:** Incompatible isolation contexts cannot exchange protected data.
- **`E05.05.05.F03` — NoBypassViaSearch:** Search metadata cannot reveal restricted existence or content.
- **`E05.05.05.F04` — ReclassificationAccountable:** Changes in classification produce attributable governed events.
- **`E05.05.05.F05` — RecoveryPreservesProtection:** Restored data retains original enforceable security constraints.

#### E05.05.06 — Protected Release Integrity
Withhold sensitive information products unless the entire requested content set is authorised and safely releasable.

*Demand: B10.06, B11.05, B13.04; evidence: PROV, TH, SEC; findings: R04, R23.*

- **`E05.05.06.F01` — CompleteSetAuthorisation:** Each requested protected item passes release-time checks.
- **`E05.05.06.F02` — NoPartialReleaseOnDenial:** Any denied required item prevents publication of the assembled set.
- **`E05.05.06.F03` — StagedInformationNotExposed:** In-progress assembly cannot be accessed through ungoverned paths.
- **`E05.05.06.F04` — ReleaseDecisionRecorded:** Published and rejected outcomes retain attributable decision evidence.
- **`E05.05.06.F05` — AuthorityChangeRechecked:** Changes between assembly and release cannot silently leave stale permission.

### E05.06 — Authorisation Policy & Effective Authority Management
Define and maintain authoritative policy and effective authorisation representations independently of login, distribution and local enforcement.


#### E05.06.01 — Authoritative Policy Definition
Maintain a versioned, reviewable policy model for protected subjects and information use.

*Demand: B01.05, B10.03, B10.04, B14.06; evidence: TH, SEC; findings: R01, R22.*

- **`E05.06.01.F01` — PolicyVersionAuthoritative:** Every effective policy selection refers to an approved authoritative version.
- **`E05.06.01.F02` — PolicyChangeAccountable:** Authorised policy changes record actor, reason and effective point.
- **`E05.06.01.F03` — InvalidPolicyRejected:** Contradictory or malformed conditions cannot be silently activated.
- **`E05.06.01.F04` — PolicyScopeDiscriminated:** Subject, compartment, action and contextual limits remain independently evaluable.
- **`E05.06.01.F05` — OldPolicyRecoverable:** A prior policy definition remains available for historical decision assessment.

#### E05.06.02 — Effective Passport Construction
Construct and preserve an effective authority representation when policy or identity circumstances change, independently of routine login.

*Demand: B01.05, B10.04, B14.06; evidence: TH, REF; findings: R01, R02.*

- **`E05.06.02.F01` — PassportChangeDriven:** Relevant authoritative changes cause affected Passport recalculation.
- **`E05.06.02.F02` — LoginNoForcedCompilation:** Ordinary valid login retrieves suitable preconstructed effective authority.
- **`E05.06.02.F03` — PassportSourceTraceable:** Constructed authority references input policy and identity versions.
- **`E05.06.02.F04` — PassportCriteriaComplete:** RequiredCriteria dimensions are evaluated for supported protected subjects.
- **`E05.06.02.F05` — PassportBuildFailureSafe:** Construction failure cannot create an over-permissive operational representation.

#### E05.06.03 — Policy Change Impact & Revalidation
Identify affected authority grants and revalidate operational Passports after a governing change.

*Demand: B01.05, B10.03, B14.06; evidence: TH, SEC; findings: R01, R02.*

- **`E05.06.03.F01` — AffectedPassportSetFound:** Authorisation change impact identifies all affected effective authorisations within the applicable scope.
- **`E05.06.03.F02` — RevocationBeforeContinuedGrant:** Stale effective authority cannot grant access after the governing change.
- **`E05.06.03.F03` — RevalidationOutcomeVisible:** Completed, failed and pending revalidations have distinct statuses.
- **`E05.06.03.F04` — RecoveryOfDistributionGap:** Disconnected consumers reconcile effective authority on rejoin.
- **`E05.06.03.F05` — NoUncontrolledPrivilegeExpansion:** Revalidation never broadens authority without a valid governing change.

#### E05.06.04 — Policy Administration & Evidence
Enable authorised policy maintenance, review and accountable release without bypassing policy authority.

*Demand: B10.04, B11.05, B12.03; evidence: TH, AUD; findings: R01, R22.*

- **`E05.06.04.F01` — AdministrativeActorVerified:** Every change action is attributable to authorised administration context.
- **`E05.06.04.F02` — SeparationOfAuthorityApplied:** Administrative access does not imply authority to bypass policy acceptance.
- **`E05.06.04.F03` — PolicyReleaseReviewed:** New policy state has required approval and release evidence.
- **`E05.06.04.F04` — ChangeImpactExplained:** Affected subjects and permitted actions are identified before activation.
- **`E05.06.04.F05` — RejectedAdminChangeRecorded:** Mandatory rejected attempts produce audit evidence without sensitive leakage.

### E05.07 — Health Entity Identity & Association
Distinguish the identity of clinical/physical subjects, providers and organisations from digital authentication identities and from Information Unit identities, including governed cross-source correlation and explicit uncertainty.

*Evidence-led candidate.* Lenses: EMPI,PD,LHR,HIE,DT; sources: C-PERSON-CORR,C-PERSON-RESOLVE,C-PERSON-CORRECT,DRP-IU. No solution component or release commitment implied.

#### E05.07.01 — Represented Entity Identity Distinction
Preserve a person, practitioner, organisation or physical entity identity independently of digital credentials, information artefacts and Twin instances.

- **`E05.07.01.F01` — EntityAndRecordIdentityDistinct:** Identity handling distinguishes a represented person from the Information Unit carrying an assertion about that person.
- **`E05.07.01.F02` — CredentialIdentityDistinct:** Changing a login or authentication credential does not automatically change the represented entity’s identity.
- **`E05.07.01.F03` — MultiRoleEntityContinuity:** The same entity remains identifiable while its organisational roles or service relationships change.
- **`E05.07.01.F04` — IdentityScopeDocumented:** Global, local and source-scoped identifiers are never treated as interchangeable without governed correlation.
- **`E05.07.01.F05` — IdentityAssociationEvidence:** Entity–record associations can be independently reviewed and traced.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: DRP-IU, C-PERSON-RESOLVE. *Release horizon:* Unscheduled.

#### E05.07.02 — Source Identifier & Alias Stewardship
Record and qualify source-assigned identifiers, alternate identifiers and changes without destroying provenance.

- **`E05.07.02.F01` — SourceIdentifierNamespaceRetained:** A repeated identifier from different issuing authorities cannot be mistaken for the same identifier.
- **`E05.07.02.F02` — AliasHistoryVisible:** Current and former identifiers can be distinguished with temporal context.
- **`E05.07.02.F03` — IdentifierChangeTraceability:** Identifier corrections retain a traceable prior association.
- **`E05.07.02.F04` — UnknownIdentifierStateExplicit:** Unresolvable identifiers yield qualified uncertainty rather than fabricated entity matches.
- **`E05.07.02.F05` — IdentifierDisclosureProtected:** Source identifiers and aliases obey applicable information-protection constraints.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PERSON-CORR, C-PERSON-RESOLVE. *Release horizon:* Unscheduled.

#### E05.07.03 — Cross-Source Identity Candidate Correlation
Identify potential correspondence between person or other entity records without automatically declaring identity equivalence.

- **`E05.07.03.F01` — CandidateMatchIsQualified:** A candidate correlation is distinguishable from an authoritative identity-link decision.
- **`E05.07.03.F02` — AdverseMatchEvaluation:** Known false-match and missed-match scenarios can be assessed against explicit, governed criteria.
- **`E05.07.03.F03` — MatchContextExplainable:** Decision support exposes relevant matching evidence and source limits to authorised reviewers.
- **`E05.07.03.F04` — UnresolvedCandidateContinuity:** Unresolved identity candidates remain correlatable and reviewable across later updates.
- **`E05.07.03.F05` — MatchingMethodNotPrescribed:** Conformance can be assessed without requiring a particular algorithm or external EMPI product.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PERSON-CORR, C-PERSON-RESOLVE. *Release horizon:* Unscheduled.

#### E05.07.04 — Uncertain Identity Association Review
Refer uncertain, conflicting or contested identity associations for accountable disposition.

- **`E05.07.04.F01` — AmbiguousAssociationHeld:** An unresolved person association cannot silently become a trusted cross-source linkage.
- **`E05.07.04.F02` — IdentityReviewAttribution:** The authority, evidence and disposition of an identity review are recorded.
- **`E05.07.04.F03` — ClinicalRiskVisible:** Potential wrong-person linkage conditions can be reported to the relevant authorised stakeholders.
- **`E05.07.04.F04` — ReviewStateContinuity:** Identity cases remain visible across handoff or prolonged human review.
- **`E05.07.04.F05` — ContestedOutcomePreserved:** Disputed identity resolutions remain qualified rather than disappearing from provenance.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-PERSON-CORRECT, C-PERSON-CORR. *Release horizon:* Unscheduled.

#### E05.07.05 — Controlled Identity Link & Unlink
Establish, correct and reverse cross-source entity associations under explicit governance, preserving the impact on previously linked information.

- **`E05.07.05.F01` — AuthorisedLinkDecision:** Link decisions require the applicable authority and are attributable to a decision context.
- **`E05.07.05.F02` — ReversalSupported:** Erroneous record association can be reversed without destroying prior linkage evidence.
- **`E05.07.05.F03` — AffectedReferenceQualification:** Dependent longitudinal references can be identified and qualified after a link correction.
- **`E05.07.05.F04` — NoSilentClinicalRecordMutation:** A linkage change does not silently rewrite immutable clinical content or source records.
- **`E05.07.05.F05` — HistoricalAssociationInterpretability:** A past query can be interpreted against the identity association effective at the relevant time.

*Source reasoning:* INFERRED_FULL_EMPI_GOVERNANCE; evidence keys: C-PERSON-CORRECT, C-PERSON-CORR, DRP-IU. *Release horizon:* R3+ full EMPI scope; earlier bounded correlation allowed.

#### E05.07.06 — Temporal Identity State & Continuity
Maintain a consistent interpretation of entity identity across time, source-system changes, record correction and life-cycle transitions.

- **`E05.07.06.F01` — EffectiveIdentityState:** Current and historical identity dispositions are distinguishable at a defined point in time.
- **`E05.07.06.F02` — RetiredIdentifierResolvable:** A retired identifier can yield a governed disposition rather than an unexplained missing record.
- **`E05.07.06.F03` — SourceSystemChangeSafe:** Replaced or migrated source systems do not silently break a recorded entity association.
- **`E05.07.06.F04` — ProvenancePreservingReidentification:** Reidentification or correction preserves source evidence and the responsible actor.
- **`E05.07.06.F05` — CrossDomainIdentityConsistency:** Permitted references to the same entity do not disagree without an explicit qualification.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: DRP-IU, C-PERSON-RESOLVE. *Release horizon:* Unscheduled.

#### E05.07.07 — Enterprise Person Identity Authority
Represent an accountable, governed determination of person identity when an enterprise-level master-index service is in scope.

- **`E05.07.07.F01` — MasterDecisionAuthorityExplicit:** The authority responsible for enterprise person-identity decisions is defined and testable.
- **`E05.07.07.F02` — MasterAssertionTraceability:** A master identity assertion is supported by identifiable source evidence and decisions.
- **`E05.07.07.F03` — ConflictNotSilentlyResolved:** Competing enterprise identity determinations remain visible until the applicable resolution occurs.
- **`E05.07.07.F04` — GovernedMasterIdentityChange:** Authoritative master identity change produces an accountable, reviewable lifecycle event.
- **`E05.07.07.F05` — CrossSystemImpactQualified:** Consumers can distinguish propagating an identity update from proving that every affected record was corrected.

*Source reasoning:* INFERRED_FULL_EMPI_GOVERNANCE; evidence keys: C-PERSON-CORR, C-PERSON-CORRECT, DRP-IU. *Release horizon:* R3+ EMPI service objective.

#### E05.07.08 — Digital Twin Referent Association
Preserve the association between a Digital Twin and the real-world entity it models without equating the Twin with that entity or its current fulfiller.

- **`E05.07.08.F01` — TwinReferentDistinction:** A task carried by a Twin does not imply that the Twin itself physically fulfils it.
- **`E05.07.08.F02` — ReferentAssociationStable:** The intended physical referent remains identifiable across Twin state changes.
- **`E05.07.08.F03` — CorrectedAssociationGoverned:** A misidentified Twin–entity association has an accountable correction route.
- **`E05.07.08.F04` — AuthorityDoesNotInheritBlindly:** Twin participation never automatically confers every right or privilege of the represented entity.
- **`E05.07.08.F05` — MultipleTwinContextQualified:** Multiple possible representations or contexts for one entity are distinguishable and reviewable.

*Source reasoning:* AGREED_DIRECTION_WITH_INFERRED_CAPABILITY_DETAIL; evidence keys: DRP-PR, C-CT2-ACT. *Release horizon:* Unscheduled.

## E06 — Digital Work, Events & Temporal Coordination
The system can carry forward and account for work, events and time-dependent execution.

### E06.01 — Task Representation & State Management
Represent actionable work, its state and resulting outcomes.

#### E06.01.01 — Actionable Work Representation
Work can be described with sufficient meaning and state for governed handling.

- **`E06.01.01.F01` — CompleteActionableWorkContext:** Accepted actionable work retains its required task context.
- **`E06.01.01.F02` — Defined Work Objective:** Defined work has an explicit objective.
- **`E06.01.01.F03` — Valid Work State:** Valid work has an explicit state.
- **`E06.01.01.F04` — Assigned Work Responsibility:** Assigned work has an identifiable responsible party.
- **`E06.01.01.F05` — Actionable Work Eligibility:** Actionable work has its eligibility established, distinguishing permitted and nonconforming conditions.

#### E06.01.02 — Work Outcome Recording
The outcome of a work unit can be obtained and interpreted.

- **`E06.01.02.F01` — AttributableWorkOutcome:** Completed work units have valid, attributable result or outcome references.
- **`E06.01.02.F02` — Explicit Work Completion Result:** Explicit work completion has an explicit result.
- **`E06.01.02.F03` — Outcome Version Association:** Outcome version is associated.
- **`E06.01.02.F04` — Failed Work Disposition:** Failed work has an explicit disposition.
- **`E06.01.02.F05` — Outcome Provenance:** Outcome retains its provenance, with accessible and accurate supporting evidence.

#### E06.01.03 — Work Lifecycle Visibility
Consumers can determine the current governed state of work.

- **`E06.01.03.F01` — CorrectWorkState:** Work units have a reconciled state that accurately represents their progression.
- **`E06.01.03.F02` — Work State Transition Trace:** Work state transition is traceable, with accessible and accurate supporting evidence.
- **`E06.01.03.F03` — In-Flight Work Discoverability:** In-flight work can be discovered.
- **`E06.01.03.F04` — Work Ownership Visibility:** Work ownership is visible, with accessible and accurate supporting evidence.
- **`E06.01.03.F05` — Abnormal Work State Flagging:** Abnormal work state is flagged.

#### E06.01.04 — Task Obligation & Outcome Semantics
Represent actionable task, fulfillment, reporting and outcome relationships without using transport status as task completion.

**Adjudicated FC-001 clarification:** `ActionableTask` names the abstract identifiable work obligation, and `FulfillmentTask` names an abstract identifiable undertaking. In an actionable-work context, `Pragma` is the common governed Harmonia representation from which a `Contextualiser` may produce **zero or more distinct, recipient-specific `Analipsi`**. Each `Analipsi` realises one undertaking (`FulfillmentTask`), contains its **own contextualised `Pragmaton`** and is presented for processing by its intended Ergon or equivalent recipient. Its Pragmaton state is specific to that Analipsi, **not cumulative state shared by all realisations** of the source Pragma. Each Analipsi supplies distinct identity and correlation context for progression, outputs, AuditEvent, provenance and independent assurance. Independent undertakings of a Pragma may be concurrent; their contribution to `TaskOutcome` remains governed by the parent work obligation's existing concurrency and outcome rules. This statement does not equate every kind of Pragma with an ActionableTask, and does not equate an Analipsi with a technical Ergon invocation or retry.

*Demand: B05.01, B07.04, B11.02; evidence: PRAGMA, TASK; findings: R07, R18.*

- **`E06.01.04.F01` — TaskTypeDistinguished:** Actionable, fulfillment and reported task representations remain distinguishable.
- **`E06.01.04.F02` — OutcomeModeApplied:** Aggregate/collection/derived outcomes follow the designated contract.
- **`E06.01.04.F03` — CompletionPolicyApplied:** FirstToFinish, AssignedToFinish and Aggregate behave as specified.
- **`E06.01.04.F04` — TaskIdentityPreserved:** Task lineage survives restart and repartitioning.
- **`E06.01.04.F05` — UndeliveredNotComplete:** Queued or accepted transport never alone indicates task fulfillment.

### E06.02 — Workflow & Work Progression
Coordinate work progression across actors and components.

#### E06.02.01 — Coordinated Work Progression
Related work can progress coherently across participating systems.

- **`E06.02.01.F01` — CorrectWorkflowProgression:** Workflows progress to the correct permitted state under their conformance conditions.
- **`E06.02.01.F02` — Required Step Ordering:** Required step is ordered.
- **`E06.02.01.F03` — Dependency-Aware Advancement:** Dependency-aware can advance.
- **`E06.02.01.F04` — Correct Branch Progression:** Correct branch can progress.
- **`E06.02.01.F05` — Workflow Completion Visibility:** Workflow completion is visible, with accessible and accurate supporting evidence.

#### E06.02.02 — Individual Work Execution
A bounded work unit can be processed and reported distinctly from its wider workflow. Processing of an individual `Analipsi` uses that realisation's instance-specific `Pragmaton`, progression and evidence correlation; downstream independent recipients receive their own Analipsi derived from the applicable Pragma rather than inheriting an undifferentiated, cumulatively mutable execution context.

- **`E06.02.02.F01` — IsolatedWorkOutcome:** Individual tasks produce correct outcomes with independent accountability.
- **`E06.02.02.F02` — Scoped Task Context:** Scoped task has its relevant context, distinguishing permitted and nonconforming conditions.
- **`E06.02.02.F03` — Single-Task Completion Evidence:** Single-task completion is evidenced, with accessible and accurate supporting evidence.
- **`E06.02.02.F04` — Task Failure Qualification:** Task failure is qualified.
- **`E06.02.02.F05` — Recoverable Task Interruption:** Recoverable task interruptions have explicit outcomes through relevant changes or disruptions, with recovery where required.

#### E06.02.03 — Digital Twin Work Progression
Work concerning a governed digital twin can progress in its required ordering/context.

- **`E06.02.03.F01` — TwinWorkOrderingConformance:** Digital Twin work obeys its prescribed ordering and state conditions.
- **`E06.02.03.F02` — Twin-Scoped Work Sequence:** Twin-scoped work has an explicit sequence, distinguishing permitted and nonconforming conditions.
- **`E06.02.03.F03` — Twin Event Context:** Twin events retain the context required for their governed work progression. The Digital Twin Behaviour framework establishes the required Twin/Behaviour execution context and determines whether input information needs Twin-centric contextualisation, already has it, or needs none. General inputs remain valid; inputs already fit for processing pass through without artificial conversion or destructive rewriting, preserving original information and governed execution semantics.
- **`E06.02.03.F04` — Twin Execution Context Isolation:** Each Digital Twin instance maintains a logically isolated execution context encompassing its inputs, outputs, work state and activity progression, allowing work to proceed independently or asynchronously relative to other Twin instances without unintended context interference.
- **`E06.02.03.F05` — Twin Execution Recovery:** The Digital Twin execution framework supports recovery and continuation of governed work following execution failures or interruptions, preserving applicable work progression and ensuring that recovery does not result in unintended loss or duplication of work.

#### E06.02.04 — Non-Digital-Twin Workflow Progression
Coordinate and observe non-Twin workflow obligations without equating them to individual work execution or parcel routing.

*Demand: B05.01, B07.04, B11.02; evidence: TASK, WORK, PRAGMA; findings: R08.*

- **`E06.02.04.F01` — WorkflowStepOrderPreserved:** Required predecessor and successor constraints are observed.
- **`E06.02.04.F02` — NonTwinWorkRecognised:** Non-Twin workflow can be expressed without fake Twin identity.
- **`E06.02.04.F03` — FailureStatePreserved:** Unfinished workflow state remains inspectable across execution loss.
- **`E06.02.04.F04` — HumanActionGateRespected:** Required clinical or administrative human decision cannot be silently automated.
- **`E06.02.04.F05` — WorkflowCompletionEvidence:** The declared completion condition is evidenced independently of transport receipt.

#### E06.02.05 — Digital Twin Workflow Coordination
Coordinate Twin-scoped work while preserving Twin-local ownership of execution and ordered activity.

*Demand: B03.03, B05.01, B07.04; evidence: TASK, PRAGMA, WORK; findings: R08.*

- **`E06.02.05.F01` — Twin Input Allocation:** The controlling choreography/orchestration framework directs incoming work and associated inputs to the appropriate Digital Twin instance, preserving instance-specific ownership and preventing unintended cross-instance consumption or context contamination.
- **`E06.02.05.F02` — TwinOrderMaintained:** Ordered Twin actions obey selected sequencing policy.
- **`E06.02.05.F03` — TwinFailureVisible:** Unavailable Twin state is not reported as finished work.
- **`E06.02.05.F04` — Twin Operational Context Recoverability:** The operational context of each Digital Twin instance, including its governed work state and associated information, remains recoverable following complete failure of the Digital Twin execution engine, independently of the continued availability of that engine.
- **`E06.02.05.F05` — CrossTwinCorrelation:** Multi-Twin interactions remain correlated without falsely merging their identities.

### E06.03 — Event & Parcel Routing
Direct governed work/event parcels to the appropriate destination.

#### E06.03.01 — Governed Parcel Routing
An event or work parcel is delivered to the destination prescribed by routing policy.

- **`E06.03.01.F01` — CorrectParcelDestination:** Routed Parcels are delivered to their prescribed destinations.
- **`E06.03.01.F02` — Correct Destination Resolution:** Correct destination is resolved.
- **`E06.03.01.F03` — Parcel Metadata Fidelity:** Parcel metadata is faithfully preserved.
- **`E06.03.01.F04` — Routing Policy Conformance:** Routing policy conforms to the applicable requirements, distinguishing permitted and nonconforming conditions.
- **`E06.03.01.F05` — Undeliverable Parcel Disposition:** Undeliverable parcel has an explicit disposition.

#### E06.03.02 — Routed Parcel Accountability
Consumers can determine the handling status of routed parcels.

- **`E06.03.02.F01` — AttributableParcelRoute:** Parcels retain sufficient routing and accountability evidence.
- **`E06.03.02.F02` — Parcel Dispatch Trace:** Parcel dispatch is traceable, with accessible and accurate supporting evidence.
- **`E06.03.02.F03` — Delivery Correlation:** Delivery is correlated.
- **`E06.03.02.F04` — Parcel Routing Assurance:** Actual Parcel movements through Harmonia can be observed, tracked and reported, including their sources, destinations, routing transitions and outcomes, using governed routing evidence correlated with routing decisions.
- **`E06.03.02.F05` — Parcel Journey Reconstruction:** Parcel journey can be reconstructed.

#### E06.03.03 — Exclusive Parcel Routing Decision
Determine a governed PetasosParcel route without taking ownership of queue delivery, work execution or payload interpretation.

*Demand: B07.04, B05.06, B12.04; evidence: PARCEL, PRAGMA; findings: R08.*

- **`E06.03.03.F01` — SingleRoutingAuthority:** Each parcel destination decision has one responsible routing authority.
- **`E06.03.03.F02` — Routing Decision Evidence:** Each governed routing decision records sufficient information to establish its selected destination, applicable rules or criteria and rationale, including decision context and qualifications. The decision can be correlated with actual movement without treating a selected route as proof of delivery.
- **`E06.03.03.F03` — UnavailableDestinationHandled:** No valid route produces an explicit exception, not disappearance.
- **`E06.03.03.F04` — RoutingNoPayloadMutation:** Routing does not silently transform the parcel state or governed payload meaning.
- **`E06.03.03.F05` — NoDeliveryOwnershipLeak:** Routing decision is not represented as evidence of successful delivery.

#### E06.03.04 — Parcel State & Metadata Preservation
Preserve by-value parcel envelope semantics and required metadata while routing through distributed components.

*Demand: B07.04, B10.02; evidence: PARCEL, PRAGMA, IU; findings: R07, R08.*

- **`E06.03.04.F01` — EnvelopeValueIntegrity:** Parcel value and metadata are preserved through the route.
- **`E06.03.04.F02` — ParcelScopeInterComponent:** Cross-component route follows declared parcel-domain scope.
- **`E06.03.04.F03` — CorrelationIdPersistent:** Retry and reroute retain identity/correlation information.
- **`E06.03.04.F04` — SecurityContextCarriesSafely:** Passport snapshot constraints propagate without credentials.
- **`E06.03.04.F05` — ParcelNotInformationUnit:** Parcel transfer state cannot redefine durable IU identity/version state.

### E06.04 — Delivery Reliability & Recovery
Queue, retry, acknowledge or recover delivery where the applicable interaction requires it.

#### E06.04.01 — Dependable Parcel Delivery
Accepted parcels can survive interruptions until delivery or prescribed disposition.

- **`E06.04.01.F01` — NoLostAcceptedParcel:** Accepted Parcels remain preserved through the specified failure conditions.
- **`E06.04.01.F02` — At-Least-Required Delivery Outcome:** At-least-required delivery has an explicit outcome.
- **`E06.04.01.F03` — Duplicate Delivery Recognition:** Duplicate delivery is recognised.
- **`E06.04.01.F04` — Delivery Failure Visibility:** Delivery failure is visible, with accessible and accurate supporting evidence.
- **`E06.04.01.F05` — Confirmed Recipient Processing:** Confirmed recipient processing reaches and can be understood by the intended recipient.

#### E06.04.02 — Controlled Retry & Rejection
Delivery failures are retried or disposed of according to declared policy.

- **`E06.04.02.F01` — CorrectDeliveryRecovery:** Delivery failures receive the correct retry or rejection outcome.
- **`E06.04.02.F02` — Bounded Retry Policy:** Delivery retries follow the applicable bounded retry policy, distinguishing permitted and nonconforming conditions.
- **`E06.04.02.F03` — Dead-Letter Disposition:** Dead-letter has an explicit disposition.
- **`E06.04.02.F04` — Retry Cause Visibility:** Retry cause is visible, with accessible and accurate supporting evidence.
- **`E06.04.02.F05` — Exhausted Retry Escalation:** Exhausted retry escalation reaches and can be understood by the intended recipient.

#### E06.04.03 — Delivery Outcome Visibility
Consumers can distinguish accepted, pending, delivered, failed and dispositioned delivery states.

- **`E06.04.03.F01` — DeterminateDeliveryStatus:** Each Parcel has an accurate and determinable current delivery status.
- **`E06.04.03.F02` — Dispatch-to-Receipt Correlation:** Dispatch-to-receipt correlation reaches and can be understood by the intended recipient.
- **`E06.04.03.F03` — Pending Delivery Recognition:** Pending delivery is recognised, with accessible and accurate supporting evidence.
- **`E06.04.03.F04` — Delivery Outcome Finality:** Delivery outcome has an explicit final outcome.
- **`E06.04.03.F05` — Missing Delivery Alerting:** Missing delivery alerting reaches and can be understood by the intended recipient.

#### E06.04.04 — Backpressure & Capacity Admission
Maintain accepted work obligations while responding safely to overload and delivery congestion.

*Demand: B07.03, B12.04, B07.04; evidence: PARCEL, NFR, RES-W; findings: R21.*

- **`E06.04.04.F01` — OverCapacityExplicitRejection:** Refused new work receives a clear capacity disposition before acceptance.
- **`E06.04.04.F02` — AcceptedQueueNotDropped:** Accepted parcels remain traceable when downstream consumers stall.
- **`E06.04.04.F03` — BackpressureSignalVisible:** Upstream capacity signals are measurable and interpretable.
- **`E06.04.04.F04` — RetryLoadBounded:** Retries obey configured limits without amplifying an outage uncontrolled.
- **`E06.04.04.F05` — ServiceRecoveryDrainsQueue:** Recovered receivers process preserved accepted work according to policy.

### E06.05 — Temporal Execution & Scheduling
Coordinate time-based execution, deadlines and temporal constraints.

#### E06.05.01 — Scheduled Work Execution
Work can be initiated according to agreed scheduling and calendar conditions.

- **`E06.05.01.F01` — ScheduleConformantExecution:** Scheduled work is triggered within its defined acceptable conditions.
- **`E06.05.01.F02` — Calendar-Correct Scheduling:** Work schedules correctly apply the relevant calendar conditions.
- **`E06.05.01.F03` — Due Work Triggering:** Due work is triggered.
- **`E06.05.01.F04` — Duplicate Trigger Prevention:** Duplicate trigger is prevented.
- **`E06.05.01.F05` — Scheduled Execution Evidence:** Scheduled execution evidence is available within the applicable context-defined interval.

#### E06.05.02 — Deadline & Temporal Constraint Handling
Time-critical work is identified and treated under applicable timing rules.

- **`E06.05.02.F01` — CorrectTemporalConstraintHandling:** Work deadlines and temporal constraints receive their prescribed handling.
- **`E06.05.02.F02` — Deadline Breach Recognition:** Deadline breaches are recognised within the applicable context-defined interval.
- **`E06.05.02.F03` — Temporal Rule Transparency:** Temporal rule is transparent.
- **`E06.05.02.F04` — Time-Zone Correctness:** Time-zone is correct.
- **`E06.05.02.F05` — Late Work Disposition:** Late work has an explicit disposition.

#### E06.05.03 — Temporal Evidence Preservation
Work and information events retain sufficient time provenance for interpretation.

- **`E06.05.03.F01` — VerifiableEventTimeContext:** Governed events retain the required verifiable time provenance.
- **`E06.05.03.F02` — Timestamp Source Attribution:** Timestamp source is attributable, with accessible and accurate supporting evidence.
- **`E06.05.03.F03` — Time Ordering Integrity:** Time ordering retains its integrity.
- **`E06.05.03.F04` — Clock Discrepancy Disclosure:** Clock discrepancy is disclosed.
- **`E06.05.03.F05` — Retained Temporal Provenance:** Retained temporal retains its provenance, with accessible and accurate supporting evidence.

#### E06.05.04 — Central Time & Scheduling Authority
Provide a consistent scheduling/calendar context and temporal evaluation for distributed work without component-local competing calendars.

*Demand: B07.03, B05.05, B12.05; evidence: NFR, TIME, PRAGMA; findings: R17.*

- **`E06.05.04.F01` — SingleSchedulingAuthority:** Scheduled execution derives from the governed scheduler responsibility.
- **`E06.05.04.F02` — TimezoneBoundaryCorrect:** Cross-timezone transitions produce correct due instants.
- **`E06.05.04.F03` — LateExecutionQualified:** Delayed work retains its original scheduled and actual execution times.
- **`E06.05.04.F04` — ClockSkewDetected:** Material component clock differences are reported as temporal conditions.
- **`E06.05.04.F05` — CalendarUpdateAccountable:** Changes in configured schedule/calendar carry release or governance evidence.

### E06.06 — Work Context & Checkpoint Preservation
Retain the state necessary for coherent, recoverable execution.

#### E06.06.01 — Recoverable Individual Work Context
A bounded unit of work retains the state needed for legitimate recovery.

- **`E06.06.01.F01` — ResumableWorkUnit:** Interrupted work units can be restored to valid states.
- **`E06.06.01.F02` — Durable Task Checkpoint:** Required task checkpoints remain durable.
- **`E06.06.01.F03` — Context Reconstruction Fidelity:** Context reconstruction is faithfully preserved.
- **`E06.06.01.F04` — Interruption Recovery Decision:** Interruption recovery has an explicit decision through relevant changes or disruptions, with recovery where required.
- **`E06.06.01.F05` — Resumed Work Trace:** Resumed work is traceable, with accessible and accurate supporting evidence.

#### E06.06.02 — Workflow Progress Checkpoint
Longer-running work preserves valid progression across interruption.

- **`E06.06.02.F01` — RecoverableWorkflowCheckpoint:** Workflow interruption recovery preserves valid progression.
- **`E06.06.02.F02` — Checkpoint Boundary Definition:** Checkpoint boundary is defined.
- **`E06.06.02.F03` — Recoverable Workflow Position:** Recoverable workflow has an identifiable position through relevant changes or disruptions, with recovery where required.
- **`E06.06.02.F04` — Dependent Work State Consistency:** Dependent work state is consistent.
- **`E06.06.02.F05` — Checkpoint Completeness Evidence:** Checkpoint completeness are evidenced.

#### E06.06.03 — Preserved Work Reference Context
Contextual references used during work remain meaningful at continuation.

- **`E06.06.03.F01` — ValidWorkContextReferences:** Restored work contexts retain valid in-scope information references.
- **`E06.06.03.F02` — Referenced Information Version Pinning:** Referenced information version is pinned.
- **`E06.06.03.F03` — Governed Work Context Reuse:** Governed work context can be reused, distinguishing permitted and nonconforming conditions.
- **`E06.06.03.F04` — Broken Work Reference Detection:** Broken work reference is detected.
- **`E06.06.03.F05` — Context Retention Compatibility:** Context retention is compatible.

#### E06.06.04 — Safe Work Reconstruction Boundary
Reconstruct missing active working context from durable references and accepted progress, never resuming in a failed Ergon.

*Demand: B07.04, B12.04, B10.01; evidence: RES-W, WORK, PRAGMATON; findings: R07, R24.*

- **`E06.06.04.F01` — NoMidErgonResume:** Post-failure execution begins at a governed restart boundary.
- **`E06.06.04.F02` — ContextRehydratedFromAuthority:** Reconstruction uses authoritative versions and durable transitions.
- **`E06.06.04.F03` — LostCacheNotLostWork:** Active cache failure does not erase accepted obligations.
- **`E06.06.04.F04` — RecoveredInputsVersioned:** Reconstituted input references identify their intended versions.
- **`E06.06.04.F05` — RestartExceptionVisible:** Unreconstructable context is reported with correlated failure evidence.

#### E06.06.05 — Pragmaton Context Integrity
Establish and maintain bounded working information without allowing active state to masquerade as authoritative information.

*Demand: B07.04, B10.01; evidence: PRAGMATON, WORK, MN; findings: R06, R07.*

- **`E06.06.05.F01` — ContextScopeBounded:** An Ergon receives only its expected and explicitly authorised ad-hoc working context.
- **`E06.06.05.F02` — WorkingStateNotAuthoritative:** Intermediate mutation cannot be mistaken for durable version progression.
- **`E06.06.05.F03` — OutputNewVersusChanged:** Genuinely new outputs remain distinct from changes to loaded inputs.
- **`E06.06.05.F04` — ContextAuthorityPreserved:** Requester and executor authority and information labels survive contextualisation.
- **`E06.06.05.F05` — ContextReleaseAccountable:** Required context disposal does not silently discard accepted authoritative information.

### E06.07 — Work Acceptance & Outcome Reconciliation
Distinguish work acceptance, execution outcome, durable information commitment and the reconciliation of uncertain transitions.


#### E06.07.01 — Durable Work Acceptance
Accept externally or internally submitted work with a durable obligation independent of Information Unit commitment.

*Demand: B07.04, B11.02, B12.04; evidence: PARCEL, PRAGMA, NFR; findings: R06, R07, R21.*

- **`E06.07.01.F01` — AcceptedWorkRecordedBeforeAck:** An acceptance acknowledgment corresponds to a recoverable work obligation.
- **`E06.07.01.F02` — RejectedWorkNotQueued:** Capacity or authority rejection leaves no silently accepted obligation.
- **`E06.07.01.F03` — DuplicateWorkCorrelated:** Retries correlate to the original accepted work identity.
- **`E06.07.01.F04` — WorkAcceptanceNotIUCommit:** Acceptance status does not assert authoritative information version progression.
- **`E06.07.01.F05` — PendingWorkSurvivesRestart:** Accepted but unfulfilled work reappears after planned or unplanned restart.

#### E06.07.02 — Execution Outcome Reconciliation
Reconcile produced work outcomes with accepted work obligations and subsequent authoritative information commitment.

*Demand: B05.03, B07.04, B11.02; evidence: PRAGMA, MN, RES-W; findings: R06, R07.*

- **`E06.07.02.F01` — ExecutionResultNotCommit:** Completed Ergon outcome remains distinct from durable information acceptance.
- **`E06.07.02.F02` — OutcomeLineageComplete:** Task, input, execution and output correlation survives recovery.
- **`E06.07.02.F03` — UnknownCommitReconcilable:** Indeterminate write can be resolved without reexecuting clinical business logic blindly.
- **`E06.07.02.F04` — FailureBetweenStagesVisible:** A failed commit after successful processing is visible as incomplete disposition.
- **`E06.07.02.F05` — NoDuplicateOutcomePromotion:** Replay does not create multiple accepted outcomes for a single governed obligation.

#### E06.07.03 — Staged Change & Commit Coordination
Support Retrieve, Check, Update, ProcessSomething and Commit as distinct service intentions with independently assessable outcomes.

*Demand: B10.07, B05.03, B11.03; evidence: MN, WORK, PERSIST; findings: R06, R07.*

- **`E06.07.03.F01` — RetrievalVersionKnown:** Work begins with a known governed source version where needed.
- **`E06.07.03.F02` — CheckDoesNotCommit:** Validation changes no authoritative state without an explicit authorised mutation.
- **`E06.07.03.F03` — UpdateRemainsProvisional:** Working-state updates do not imply durable commitment.
- **`E06.07.03.F04` — ProcessProducesQualifiedOutcome:** Execution output is separately inspectable before commitment where applicable.
- **`E06.07.03.F05` — CommitDispositionAuthoritative:** Final accepted state can be verified and differentiated from pre-commit stages.

### E06.08 — Task Publication, Subscription & Assignment
Provide discoverable, governed, subscription-based actionable work with explicit matching, offering, claiming, assignment and acceptance semantics. This is a consumer-relevant enterprise ability; it does not prescribe a broker, worker pool, queue topology or owning component.

**Source standing:** Candidate architectural inference, preserving existing Continuum meaning from `Event & Subscription` (`b768b129971e4bf5bccc03d33d2fcb4e`) and `Activity & Execution` (`e5ff80e655084524a8dcee8e4ded788a`); not a new governed canonical identity.

**Business-demand hypotheses:** B07.04 Service Delivery Coordination; B07.03 Demand, Capacity & Scheduling; B05.01 Multidisciplinary Care Coordination; B05.06 Community & Cross-Organisation Continuity. These are proposed enablement relationships, not satisfaction claims.

**Architecture boundaries:** E02.04.04 concerns publication/subscription of governed information and events. E06.08 concerns actionable-task offer and allocation. E06.03 determines parcel destinations, E06.04 manages delivery, E06.01 represents task semantics, E06.02 coordinates workflow and E06.07 accounts for durable work acceptance and outcomes. None may be inferred from another's acknowledgement.

#### E06.08.01 — Task Publication
Make independently actionable work discoverable under a governed publication scope without implying that anyone has accepted or performed it.

- **`E06.08.01.F01` — EligibleTaskPublication:** Only eligible, properly formed tasks become available for subscription matching.
- **`E06.08.01.F02` — PublicationScopeExplicit:** The task's intended subscriber scope and disclosure constraints are available to matching consumers.
- **`E06.08.01.F03` — Task Processing Acceptance:** A published work obligation becomes eligible for processing but enters active fulfilment only when a processing engine accepts the undertaking and establishes its corresponding fulfilment task. Publication alone establishes neither dispatch receipt, execution nor commitment.
- **`E06.08.01.F04` — PublishedTaskIdentityStable:** Publication and subsequent notification preserve the task identity and applicable version.
- **`E06.08.01.F05` — PublicationExpiryOrWithdrawal:** Expired or withdrawn publications cease to be offered under applicable policy.

#### E06.08.02 — Task Subscription Declaration & Lifecycle
Let prospective consumers declare, inspect, change and withdraw governed interest in categories of actionable work.

- **`E06.08.02.F01` — SubscriptionIntentExplicit:** A subscriber's declared task interest is distinguishable from work assignment or acceptance.
- **`E06.08.02.F02` — SubscriptionCriteriaGoverned:** Subscription criteria and their changes are interpretable under the applicable policy and model release.
- **`E06.08.02.F03` — SubscriberAuthorityEvaluated:** Only authorised consumers can establish or exercise applicable subscriptions.
- **`E06.08.02.F04` — SubscriptionLifecycleVisible:** Active, suspended, expired and withdrawn subscriptions have distinguishable states.
- **`E06.08.02.F05` — SubscriptionChangeEffective:** Changes to eligibility or subscription terms take effect according to stated temporal and security rules.

#### E06.08.03 — Task Subscription Matching & Eligibility
Match published task requirements to interested and currently eligible consumers without yet claiming that work is assigned.

- **`E06.08.03.F01` — TaskMatchAccurate:** Eligible consumers are matched to tasks under the declared task and subscription criteria.
- **`E06.08.03.F02` — UnmatchedWorkVisible:** A task with no eligible subscriber remains explicitly accounted for.
- **`E06.08.03.F03` — MatchingContextCurrent:** Matches use the applicable task, subscription and authority state at the decision boundary.
- **`E06.08.03.F04` — UnauthorisedDisclosurePrevented:** Subscription matching does not disclose protected task information to ineligible consumers.
- **`E06.08.03.F05` — MatchEvidenceTraceable:** The basis for a match, nonmatch or exclusion can be independently assessed.

#### E06.08.04 — Task Offering, Claiming & Selection
Support the policy-defined transition from discoverable work to offers and claims, including competing or multiple interested consumers.

- **`E06.08.04.F01` — OfferDistinctFromAssignment:** An offer alone does not assert that work was accepted or assigned.
- **`E06.08.04.F02` — ClaimEligibilityEnforced:** A claim is permitted only under the task's current authority and eligibility conditions.
- **`E06.08.04.F03` — CompetingClaimsReconciled:** Concurrent claims resolve according to the declared assignment/concurrency policy.
- **`E06.08.04.F04` — MultiConsumerPolicySupported:** First-to-finish, assigned-to-finish, aggregate and other applicable completion policies are not silently reduced to single-consumer handling.
- **`E06.08.04.F05` — UnclaimedWorkDisposition:** Unclaimed, declined or expired offers remain visible and are re-offered or escalated only as policy permits.

#### E06.08.05 — Task Assignment & Acceptance
Establish an accountable work undertaking independently of its publication, offer and transport state.

- **`E06.08.05.F01` — AssignedResponsibilityAttributable:** Assigned performers and the effective undertaking are unambiguously recorded.
- **`E06.08.05.F02` — AssignmentAuthorityValid:** Assignment and acceptance respect current eligibility, authorisation and task constraints.
- **`E06.08.05.F03` — Assured Task Execution Commencement:** Execution commencement is established only after an accepted fulfilment task has been fully contextualised, loaded into its assigned execution context and progressed beyond the governed start checkpoint with the required execution state established. Delivery alone establishes neither acceptance nor commencement, and commencement does not establish completion.
- **`E06.08.05.F04` — DurableAcceptanceBeforeAcknowledgement:** Where Harmonia acknowledges accepted governed work, the corresponding recoverable obligation already exists.
- **`E06.08.05.F05` — DeclineAndTimeoutHandled:** Decline, acceptance timeout and reassignment produce explicit, policy-conformant outcomes.

#### E06.08.06 — Task Subscription Notification & State Feedback
Inform eligible subscribers and interested parties of relevant task offers and transitions without redefining task authority.

- **`E06.08.06.F01` — TaskNotificationRelevant:** Notifications reflect applicable subscriptions and task changes.
- **`E06.08.06.F02` — OfferAndTaskCorrelated:** Notifications link to the correct task, publication, offer and applicable version.
- **`E06.08.06.F03` — StaleOfferDetected:** Stale or superseded offers cannot silently create a valid new undertaking.
- **`E06.08.06.F04` — WithdrawalStopsNewOffers:** Withdrawn subscriptions or revoked authority prevent subsequent protected offers.
- **`E06.08.06.F05` — TaskStatusFeedbackQualified:** Interested authorised parties can distinguish offered, claimed, assigned, accepted, completed and failed work as applicable.

#### E06.08.07 — Published Work Recovery & Reconciliation
Preserve and reconcile task-publication and subscription obligations during subscriber loss, redelivery and partition/restart conditions.

- **`E06.08.07.F01` — AcceptedWorkSurvivesFailure:** Accepted governed work remains recoverable across the specified failure scenarios.
- **`E06.08.07.F02` — SubscriptionRecoveryQualified:** Recovered subscriptions have explicit currency and effective-state semantics.
- **`E06.08.07.F03` — RepeatedOfferRecognised:** Redelivery and repeated offers do not silently create duplicate accepted undertakings.
- **`E06.08.07.F04` — LostSubscriberDisposition:** Unavailable subscribers do not cause accepted tasks to disappear or become falsely completed.
- **`E06.08.07.F05` — RecoveryPreservesTaskLineage:** Publication, match, claim, assignment and eventual outcome remain correlatable across recovery.

**Minimum integrity scenarios:** publish a task with no subscriber; match competing eligible subscribers; reject an unauthorised subscription; accept work but fail before dispatch; receive a parcel without task acceptance; recover from subscriber loss; withdraw a subscription after an offer; and reconcile first-to-finish versus aggregate completion without duplicating accepted obligations.

**Open design questions:** Whether publication is available for every task archetype; how subscriptions relate to task affinity and assigned-performer modes; how claims relate to Task concurrency/completion policy; and which operational behaviours are native to the platform versus externally delegated. These do not block defining the candidate capabilities.

### E06.09 — Asynchronous Digital Twin–Entity Work Coordination
Preserve governed work across the digital/physical boundary: a Twin may delegate or reflect a real-world undertaking, remain able to progress independent stimuli and subsequently reconcile outcomes without a continuously executing Ergon.

*Evidence-led candidate.* Lenses: DT,LHR; sources: C-CT2-ACT,DRP-PR,DRP-PG,DRP-ER. No solution component or release commitment implied.

#### E06.09.01 — Entity-Associated Work Delegation
Establish a governed task whose undertaking may be performed by the physical entity represented by a Twin or another explicitly accountable performer.

- **`E06.09.01.F01` — DelegatedWorkAttributable:** Task and intended performer are distinct, identifiable and traceable.
- **`E06.09.01.F02` — DigitalReferentNotFulfiller:** The Twin’s digital progression does not claim the Twin physically performed human care.
- **`E06.09.01.F03` — DelegationAuthorityVerified:** The delegating actor’s effective authority is checked for the work scope.
- **`E06.09.01.F04` — DelegationContextSufficient:** The recipient can obtain the permitted task context needed to undertake the work.
- **`E06.09.01.F05` — DeclinedDelegationVisible:** A declined or unaccepted delegation remains an explicitly outstanding or disposed obligation.

*Source reasoning:* AGREED_DIRECTION_WITH_INFERRED_CAPABILITY_DETAIL; evidence keys: DRP-PR, C-CT2-ACT. *Release horizon:* Unscheduled.

#### E06.09.02 — Deferred Task Lifecycle
Maintain long-duration task obligations without keeping an Ergon thread, transaction or execution envelope continuously active.

- **`E06.09.02.F01` — DeferredNotCompleted:** A deferred task is never counted as fulfilled merely because an invocation returned.
- **`E06.09.02.F02` — NoLongLivedErgonRequired:** A multi-day scenario passes without a continuously executing bounded Ergon.
- **`E06.09.02.F03` — DeferredIdentityStable:** Task and correlation identity survive deferred periods and component restarts.
- **`E06.09.02.F04` — PendingObligationsVisible:** Pending information, work acceptance and output obligations remain discoverable.
- **`E06.09.02.F05` — DeferredContextRestorable:** Progression can resume from governed durable state without restoring an in-flight Ergon stack.

*Source reasoning:* AGREED_DIRECTION_WITH_INFERRED_CAPABILITY_DETAIL; evidence keys: DRP-PR, DRP-PG, DRP-ER. *Release horizon:* Unscheduled.

#### E06.09.03 — Effector/Stimulus Work Correlation
Associate outgoing/incoming Digital Twin interaction events with the correct task, physical entity, authority and eventual disposition.

- **`E06.09.03.F01` — WorkInteractionCorrelation:** A later Stimulus can be matched to the intended outstanding undertaking.
- **`E06.09.03.F02` — NonmatchingStimulusContained:** An unrelated or forged interaction cannot alter a protected task.
- **`E06.09.03.F03` — RepeatedStimulusDisposition:** Duplicate and late responses have a policy-defined, reviewable treatment.
- **`E06.09.03.F04` — StimulusTemporalContext:** The occurrence and receipt times of reported physical-world actions remain distinguishable.
- **`E06.09.03.F05` — SecurityContextPropagation:** The relevant effective authority and disclosure restrictions survive asynchronous correlation.

*Source reasoning:* AGREED_DIRECTION_WITH_INFERRED_CAPABILITY_DETAIL; evidence keys: DRP-PR, C-CT2-EVENT. *Release horizon:* Unscheduled.

#### E06.09.04 — Independent Twin Activity Progression
Process other permitted Digital Twin activities while one or more externally fulfilled tasks remain pending, subject to actual ordering/dependency policy.

- **`E06.09.04.F01` — UnrelatedTaskNotBlocked:** A days-long Task X does not block independent Task Y progression.
- **`E06.09.04.F02` — DeclaredOrderingRespected:** Dependent tasks cannot incorrectly pass a declared sequencing barrier.
- **`E06.09.04.F03` — MultiplePendingObligations:** One Twin can manage several independently tracked deferred undertakings.
- **`E06.09.04.F04` — ConcurrentStimulusConsistency:** Simultaneous permitted events produce policy-conformant, explainable outcomes.
- **`E06.09.04.F05` — NoImplicitGlobalLock:** External fulfilment does not require a Twin-wide execution lock for its entire physical duration.

*Source reasoning:* AGREED_DIRECTION_WITH_INFERRED_CAPABILITY_DETAIL; evidence keys: DRP-ER, C-CT2-ACT. *Release horizon:* Unscheduled.

#### E06.09.05 — External Fulfilment Outcome Assimilation
Capture reports of physical-world task progress, completion or failure as evidence for governed task progression, not automatic proof of authoritative data commitment.

- **`E06.09.05.F01` — ReportedNotAutomaticallyAccepted:** A human completion report can remain pending validation or approval.
- **`E06.09.05.F02` — OutcomeSourceAttribution:** The reporter and claimed physical fulfiller can be distinguished where they differ.
- **`E06.09.05.F03` — PartialOutcomeRepresentable:** Partial, blocked and qualified fulfilment outcomes remain interpretable.
- **`E06.09.05.F04` — InformationCommitSeparate:** An accepted task result does not automatically establish that all clinical information versions were committed.
- **`E06.09.05.F05` — PhysicalDigitalProgressionReconciled:** Final task state correctly reflects the physical-world report and associated decision evidence.

*Source reasoning:* AGREED_DIRECTION_WITH_INFERRED_CAPABILITY_DETAIL; evidence keys: DRP-PR, DRP-MN. *Release horizon:* Unscheduled.

#### E06.09.06 — External Work Temporal Obligations
Maintain reminders, escalation, expiry and appropriate exceptional handling for physically fulfilled work over extended periods.

- **`E06.09.06.F01` — DurationWithoutFailure:** Ordinary multi-day duration does not by itself imply task failure.
- **`E06.09.06.F02` — DeadlineEvidenceRetained:** Applicable due times and deadline changes remain attributable and interpretable.
- **`E06.09.06.F03` — ReminderEligibilityEnforced:** Notifications are sent only to authorised, relevant parties.
- **`E06.09.06.F04` — EscalationNotFalseCompletion:** Escalation changes obligation handling without silently completing the original work.
- **`E06.09.06.F05` — TemporalBoundaryRecoverable:** Calendar and deadline obligations survive restart or service interruption.

*Source reasoning:* AGREED_DIRECTION_WITH_INFERRED_CAPABILITY_DETAIL; evidence keys: C-CT2-ACT, DRP-PR. *Release horizon:* Unscheduled.

#### E06.09.07 — Deferred Work Recovery & Reconciliation
Recover outstanding real-world obligations despite lost clients, offline periods, inconsistent reports or Digital Twin restart.

- **`E06.09.07.F01` — RestartPreservesOutstandingWork:** The Twin’s accepted unresolved work survives its own restart.
- **`E06.09.07.F02` — OfflineCompletionReconciled:** A late progress report can be matched to the correct historical task and current policy.
- **`E06.09.07.F03` — ConflictingOutcomeVisible:** Two contradictory completion reports are surfaced rather than arbitrarily collapsed.
- **`E06.09.07.F04` — ReassignmentTraceability:** Reassignments preserve the prior responsible party and reason.
- **`E06.09.07.F05` — NoLostAcceptedObligation:** Loss of a communication or UI session does not lose a durably accepted task.

*Source reasoning:* AGREED_DIRECTION_WITH_INFERRED_CAPABILITY_DETAIL; evidence keys: DRP-PR, DRP-MN. *Release horizon:* Unscheduled.

## E07 — Information Presentation & Human Interaction
The system can expose governed information and work context to people in useful, controlled ways.

### E07.01 — Contextual Information Presentation
Present relevant information for a legitimate user task.

#### E07.01.01 — Clinical Context Presentation
Clinical consumers can view governed information relevant to a clinical question.

- **`E07.01.01.F01` — RelevantClinicalContext:** Clinical presentations expose the contextual information required by the agreed clinical scope.
- **`E07.01.01.F02` — Relevant Clinical Information Selection:** Relevant clinical information is selected.
- **`E07.01.01.F03` — Clinician-Readable Presentation:** Clinician-readable can be presented.
- **`E07.01.01.F04` — Clinical Risk Highlighting:** Clinical risk is highlighted, including in adverse conditions.
- **`E07.01.01.F05` — Clinical Context Freshness:** Clinical context has its freshness established.

#### E07.01.02 — Care Team Context Presentation
Participating care workers can view relevant shared care context.

- **`E07.01.02.F01` — SharedCareContextCompleteness:** Care-team views provide the required governed shared-care context.
- **`E07.01.02.F02` — Assigned Team Visibility:** Assigned team is visible, with accessible and accurate supporting evidence.
- **`E07.01.02.F03` — Current Responsibility View:** Current responsibility can be viewed through relevant changes or disruptions, with recovery where required.
- **`E07.01.02.F04` — Multidisciplinary Care Context:** Multidisciplinary care has its relevant context.
- **`E07.01.02.F05` — Team Handover Context:** Team handover has its relevant context through relevant changes or disruptions, with recovery where required.

#### E07.01.03 — Participant Support Context Presentation
Support settings can present information relevant to participant goals and supports.

- **`E07.01.03.F01` — AppropriateSupportContext:** Support-context presentations provide the required information under its applicable restrictions.
- **`E07.01.03.F02` — Participant-Appropriate Language:** Participant-appropriate language is supported through available and usable support.
- **`E07.01.03.F03` — Current Support Goal Visibility:** Current support goal is visible, with accessible and accurate supporting evidence.
- **`E07.01.03.F04` — Accessible Support Information:** Accessible support information is available through available and usable support.
- **`E07.01.03.F05` — Support Preference Presentation:** Support preference can be presented through available and usable support.

#### E07.01.04 — Service Operations Context Presentation
Service operators can view demand, capacity and work context relevant to service operation.

- **`E07.01.04.F01` — UsefulOperationalContext:** Operational presentations expose the required indicators and context.
- **`E07.01.04.F02` — Live Service Demand View:** Live service demand can be viewed.
- **`E07.01.04.F03` — Capacity Constraint Visibility:** Capacity constraint is visible, with accessible and accurate supporting evidence.
- **`E07.01.04.F04` — Operational Exception Highlighting:** Operational exception is highlighted.
- **`E07.01.04.F05` — Service Status Currency:** Service status is current, with accessible and accurate supporting evidence.

#### E07.01.05 — Digital Service Administration Context
Administrators can inspect authorised service configuration and administration context.

- **`E07.01.05.F01` — AccurateAdministrationView:** Administrative views accurately reflect the governed current state.
- **`E07.01.05.F02` — Administrative Scope Visibility:** Administrative scope is visible, distinguishing permitted and nonconforming conditions.
- **`E07.01.05.F03` — Configuration State Explanation:** Configuration state is explained.
- **`E07.01.05.F04` — Change Impact Preview:** Change impact can be previewed, with accessible and accurate supporting evidence.
- **`E07.01.05.F05` — Administrative Action Confirmation:** Administrative action confirmation reaches and can be understood by the intended recipient.

#### E07.01.06 — Human Role & Context Sensitive Presentation
Present care, participant, administrative and operational information in their own appropriate conceptual and decision contexts.

*Demand: B01.04, B05.01, B14.01, B07.04; evidence: PRESENT, TH, JOURNEY; findings: R11, R18.*

- **`E07.01.06.F01` — AudienceContextAppropriate:** Patient and participant interfaces are assessed against distinct journeys.
- **`E07.01.06.F02` — ClinicalAndNonclinicalSeparated:** Clinical judgements are not silently inferred from participant support preferences.
- **`E07.01.06.F03` — RestrictedFieldsOmitted:** Presentation respects field and compartment restrictions.
- **`E07.01.06.F04` — SourceAndCurrencyVisible:** Display includes source and relevant age of information.
- **`E07.01.06.F05` — ScenarioMeaningValidated:** Users can identify the correct intended action in the applicable domain context.

#### E07.01.07 — Operational Assurance Presentation
Support OA&M users with status, exceptions and service readiness without exposing clinical content unnecessarily.

*Demand: B07.06, B12.04, B12.05; evidence: OPS, LOG, PRESENT; findings: R14, R24.*

- **`E07.01.07.F01` — ReadinessStatusGrounded:** UI readiness state corresponds to observed component and dependency health.
- **`E07.01.07.F02` — IncidentDrilldownAvailable:** An incident can be traced to correlated diagnostics.
- **`E07.01.07.F03` — NoDefaultClinicalPayload:** OA&M displays avoid PHI unless separately authorised and necessary.
- **`E07.01.07.F04` — RecoveryProgressVisible:** Restoration state differentiates in-progress and verified recovered.
- **`E07.01.07.F05` — OperatorActionsAudited:** Operational actions with material consequences are attributable.

### E07.02 — Governed User Interaction
Allow authorised users to initiate and complete governed interactions.

#### E07.02.01 — Governed User Action
An authorised user can initiate and receive the outcome of a governed action.

- **`E07.02.01.F01` — CorrectUserActionDisposition:** User actions receive the correct governed acceptance or rejection outcome.
- **`E07.02.01.F02` — Eligible Action Availability:** Eligible action is available, distinguishing permitted and nonconforming conditions.
- **`E07.02.01.F03` — Authorisation-Bound User Action:** Authorisation-bound user receives action, distinguishing permitted and nonconforming conditions.
- **`E07.02.01.F04` — Action Consequence Explanation:** Action consequence is explained.
- **`E07.02.01.F05` — Action Outcome Feedback:** Action outcome feedback reaches and can be understood by the intended recipient.

#### E07.02.02 — Review & Confirmation Interaction
Users can review intended changes and acknowledge significant actions where required.

- **`E07.02.02.F01` — ExplicitActionConfirmation:** Actions requiring confirmation retain valid acknowledgement evidence.
- **`E07.02.02.F02` — Reviewable Action Detail:** Reviewable action has the required detail, with accessible and accurate supporting evidence.
- **`E07.02.02.F03` — Explicit User Confirmation:** Explicit user confirmation reaches and can be understood by the intended recipient.
- **`E07.02.02.F04` — Material Change Highlight:** Material change is highlighted through relevant changes or disruptions, with recovery where required.
- **`E07.02.02.F05` — Reversible Draft Action:** Reversible draft receives action.

#### E07.02.03 — Accessible Interaction
Information-system interaction remains usable under the supported accessibility requirements.

- **`E07.02.03.F01` — SupportedInteractionAccessibility:** Interactions support the defined accessibility requirements.
- **`E07.02.03.F02` — Keyboard and Assistive Access:** Keyboard and assistive can be accessed through available and usable support.
- **`E07.02.03.F03` — Understandable Interaction Language:** Understandable interaction language is supported through available and usable support.
- **`E07.02.03.F04` — Accessible Error Recovery:** Accessible error can be recovered through available and usable support.
- **`E07.02.03.F05` — Alternative Interaction Method:** Alternative interaction methods are available where required through relevant changes or disruptions, with recovery where required.

#### E07.02.04 — Controlled Interaction Scope
Available actions reflect the current information and security scope.

- **`E07.02.04.F01` — ScopeAppropriateInteraction:** Interactions expose and enforce only the actions permitted in their applicable context.
- **`E07.02.04.F02` — Work-Context Action Restriction:** Work-context action has an explicit restriction, distinguishing permitted and nonconforming conditions.
- **`E07.02.04.F03` — Relevant Data Scoping:** Relevant data has an explicit scope.
- **`E07.02.04.F04` — Cross-Subject Leakage Prevention:** Cross-subject leakage is prevented.
- **`E07.02.04.F05` — Context Change Warning:** Context change has an explicit warning through relevant changes or disruptions, with recovery where required.

#### E07.02.05 — Staged Change Review & Confirmation
Allow a user to review, check, adjust and explicitly submit a change without collapsing those intentions into commitment.

*Demand: B01.04, B10.07, B14.03; evidence: PRESENT, MN, TH; findings: R06, R11.*

- **`E07.02.05.F01` — ReviewDoesNotMutate:** Review interaction does not commit authoritative changes.
- **`E07.02.05.F02` — ValidationFindingsActionable:** Check results identify correctable conditions in consumer language.
- **`E07.02.05.F03` — PreparedChangeDiffVisible:** A user can see intended differences before submission.
- **`E07.02.05.F04` — CommitConsentExplicit:** Where required, authoritative submission requires a distinct confirmation.
- **`E07.02.05.F05` — UnknownCommitNotSuccess:** An uncertain commitment cannot be displayed as confirmed success.

#### E07.02.06 — Supported & Represented Participation
Allow appropriately authorised assistance and representation while recording the participant or patient’s own preferences and authority limits.

*Demand: B01.05, B14.01, B14.06; evidence: TH, PRESENT, JOURNEY; findings: R11, R22.*

- **`E07.02.06.F01` — RepresentativeScopeVisible:** The effective scope and duration of a representative role can be inspected.
- **`E07.02.06.F02` — SupportedVoiceRecorded:** Participant preference is recorded separately from representative instruction.
- **`E07.02.06.F03` — AuthorityRevocationImmediate:** Expired or revoked representation is not permitted to continue.
- **`E07.02.06.F04` — DisagreementNotSilenced:** Conflicting preferences and instructions are distinguishable for escalation.
- **`E07.02.06.F05` — DecisionEvidenceAttributable:** The person, representative and reviewer actions remain separately attributable.

#### E07.02.07 — Accessibility & Language Scope Transparency
Express actual accessibility and language support without silently asserting unapproved release commitments.

*Demand: B01.04, B14.06; evidence: PRESENT, NFR, JOURNEY; findings: R11.*

- **`E07.02.07.F01` — SupportedLocaleDeclared:** Available release-language support is explicit.
- **`E07.02.07.F02` — UnsupportedNeedEscalatable:** A user can obtain a bounded alternative when needed support is absent.
- **`E07.02.07.F03` — NoFalseWCAGClaim:** Testing does not assert WCAG conformance when it is outside the release baseline.
- **`E07.02.07.F04` — CriticalContentLegible:** Known accessibility-critical scenarios receive targeted review against actual contract.
- **`E07.02.07.F05` — HelpContextRelevant:** Service guidance is expressed in the current user participation context.

#### E07.02.08 — Governed Collaboration Interaction **[NEW — P3/002, FC-076]**
Enable directed communication and native conversational collaboration, including governed capture of defined interactions, without confusing communication with authoritative changes to work responsibility.

*Accepted CT3 scope: FC-076. General collaboration intent applies to Agora/Omilia; E02.04 continues information distribution, E06.08 continues accountable Task assignment/acceptance/reassignment, and E07.06.04 continues task-context collaboration. The following individual Feature formulations are **proposed for semantic review**, not separately adjudicated.*

- **`E07.02.08.F01` — Directed Communication Interaction:** Governed point-to-point or addressed messaging can identify sender, recipient, permitted content and message disposition without treating delivery as acceptance of work.
- **`E07.02.08.F02` — Managed Conversational Collaboration:** Authorised participants can engage in direct/group dialogue and managed Rooms/Spaces, with controlled membership and distinguishable native collaboration records/media.
- **`E07.02.08.F03` — Defined Collaboration Action Capture:** Explicitly defined, meaningful interactions in managed collaboration can generate governed information or work with attributable source context; ordinary utterances do not automatically become Pragma.
- **`E07.02.08.F04` — Task Reassignment Authority Separation:** A message, chat request or acknowledgement does not by itself reassign a Task; any authoritative responsibility change is established through the governed assignment/reassignment capability and remains attributable.

### E07.03 — Longitudinal Information Viewing
Present related information across appropriate time/encounter boundaries.

#### E07.03.01 — Longitudinal Health Record Viewing
Consumers can examine relevant information across encounters and time.

- **`E07.03.01.F01` — CorrectLongitudinalContext:** Longitudinal presentations preserve correct chronology and provenance context.
- **`E07.03.01.F02` — Chronological Record Continuity:** Chronological record is maintained, with accessible and accurate supporting evidence.
- **`E07.03.01.F03` — Correct Event Association:** Correct event is associated.
- **`E07.03.01.F04` — Historical Version Accessibility:** Historical version is accessible and usable.
- **`E07.03.01.F05` — Missing Period Disclosure:** Missing period is disclosed, with accessible and accurate supporting evidence.

#### E07.03.02 — Cross-Service Information Viewing
Related information from multiple supported services can be inspected in meaningful context.

- **`E07.03.02.F01` — CoherentCrossServiceView:** Cross-service information collections are presented without misattribution or silent omission.
- **`E07.03.02.F02` — Cross-Service Context Alignment:** Cross-service context is aligned.
- **`E07.03.02.F03` — Source-Specific Attribution:** Source-specific is attributable, with accessible and accurate supporting evidence.
- **`E07.03.02.F04` — Information Source Disagreement:** Information source disagreements are identifiable, with accessible and accurate supporting evidence.
- **`E07.03.02.F05` — Combined View Relevance:** Combined view is relevant.

#### E07.03.03 — Time-Oriented Event Viewing
Consumers can understand significant events through an attributable temporal perspective.

- **`E07.03.03.F01` — AccurateEventSequencePresentation:** Event sequences display correct chronology and qualifications.
- **`E07.03.03.F02` — Correct Event Sequence:** Correct event has an explicit sequence.
- **`E07.03.03.F03` — Time-Zone-Aware Display:** Time-zone-aware can be displayed.
- **`E07.03.03.F04` — Event Interval Recognition:** Event intervals can be recognised within the applicable context-defined interval.
- **`E07.03.03.F05` — Late Event Qualification:** Late event is qualified.

#### E07.03.04 — Cross-Domain Longitudinal Presentation
Governed historical information is presented over time across domains, encounters, providers and representations with source attribution, qualifications and existing governed associations. Entries need not belong to an encounter or episode; encounter-oriented presentation uses pre-existing governed associations and does not infer clinical links from chronological adjacency. Occurrence, documentation and custody times remain distinct.

*Demand: B02.01, B05.04, B14.04; evidence: IU, REF, PRESENT; findings: R09, R11.*

- **`E07.03.04.F01` — TimeAxisDiscriminated:** Occurrence and recorded/persisted times can be distinguished.
- **`E07.03.04.F02` — SourceSystemRetained:** Cross-provider timeline entries show source lineage.
- **`E07.03.04.F04` — QualifiedAssociationPresentation:** Display only governed, source-established cross-source or identity associations, preserving their recorded qualifications and uncertainties. Presentation does not infer identity matches or clinical relationships.
- **`E07.03.04.F05` — RestrictedTimelineFiltered:** Protected records do not leak through timeline metadata.

#### E07.03.05 — Cross-Source Clinical Timeline Synthesis
Present an ordered longitudinal view that respects event times, recording times and uncertain or conflicting chronology.

- **`E07.03.05.F01` — TemporalDimensionsVisible:** The view distinguishes occurrence and recording time when the distinction is material.
- **`E07.03.05.F02` — CrossSourceEventOrderQualified:** Ambiguous event order is qualified instead of presented as certain chronology.
- **`E07.03.05.F03` — EventSourceAttribution:** Each displayed event remains attributable to its source and underlying record.
- **`E07.03.05.F04` — ClinicalGroupingWithoutDataLoss:** Episode or event grouping does not discard independently governed clinical assertions.
- **`E07.03.05.F05` — LateArrivingInformationReconciled:** Late clinical information is correctly incorporated without silently rewriting the meaning of prior views.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-LHR-QUERY, C-FED-QUERY, DRP-IU. *Release horizon:* Unscheduled.

#### E07.03.06 — Encounter & Episode Context Navigation
Navigate clinical information by patient, encounter, care episode and responsible care context without conflating these identities.

- **`E07.03.06.F01` — EncounterBoundaryVisible:** The user can distinguish encounters and episodes even when their dates overlap.
- **`E07.03.06.F02` — PatientContextProtected:** Switching patient context cannot silently retain the previous patient’s clinical data.
- **`E07.03.06.F03` — MultiProviderEpisodeLinkage:** Related care episodes can be discovered with explicit provenance of the relationship.
- **`E07.03.06.F04` — NoImplicitEpisodeMerge:** Related encounters are not treated as one authoritative episode without an established basis.
- **`E07.03.06.F05` — ScopedClinicalNavigation:** Navigation respects current consent, purpose and information restrictions.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-LHR-QUERY, C-FED-QUERY. *Release horizon:* Unscheduled.

#### E07.03.07 — Longitudinal Correction & Disagreement Awareness
Make corrected, superseded, disputed and incomplete clinical assertions understandable during longitudinal review.

- **`E07.03.07.F01` — SupersededDataQualified:** A corrected observation is not displayed as current unqualified truth.
- **`E07.03.07.F02` — DisputedAssertionVisible:** Disputed clinical information has an identifiable qualification where disclosure is permitted.
- **`E07.03.07.F03` — PriorRecordPreserved:** A reviewer can trace an amendment to earlier governed evidence.
- **`E07.03.07.F04` — AbsentVersusWithheld:** No matching clinical content is distinguishable from restricted or unavailable information as permitted.
- **`E07.03.07.F05` — CorrectionImpactContextual:** Relevant downstream summaries reveal when their source information has been superseded.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-LHR-QUERY, DRP-IU, DRP-AUD. *Release horizon:* Unscheduled.

### E07.04 — User-Context Continuity
Maintain consistent user context during relevant work where supported.

#### E07.04.01 — Persistent Work Context
A user can return to relevant unfinished digital work without losing necessary context.

- **`E07.04.01.F01` — RestoredUserWorkContext:** User work recovery restores the required work context under the prescribed recovery conditions.
- **`E07.04.01.F02` — Restored User Work Position:** Restored user work has an identifiable position through relevant changes or disruptions, with recovery where required.
- **`E07.04.01.F03` — Unsaved Work Warning:** Unsaved work has an explicit warning.
- **`E07.04.01.F04` — Context Expiry Handling:** Context expiry is handled through relevant changes or disruptions, with recovery where required.
- **`E07.04.01.F05` — Context Recovery after Interruption:** Context recovery after interruptions have explicit outcomes through relevant changes or disruptions, with recovery where required.

#### E07.04.02 — Contextual Navigation Continuity
Navigation between related scopes preserves appropriate contextual references.

- **`E07.04.02.F01` — PreservedNavigationContext:** Navigation transitions maintain the permitted relevant context.
- **`E07.04.02.F02` — Relevant Navigation History:** Relevant navigation retains its history through available and usable support.
- **`E07.04.02.F03` — Stable Subject Context:** Stable subject has its relevant context.
- **`E07.04.02.F04` — Context Preservation between Views:** Relevant permitted context is preserved when moving between views through relevant changes or disruptions, with recovery where required.
- **`E07.04.02.F05` — Back-Navigation Integrity:** Back-navigation retains its integrity through available and usable support.

#### E07.04.03 — Safe Context Switching
A user can change work context without carrying unintended sensitive information into the new scope.

- **`E07.04.03.F01` — CorrectContextIsolation:** Context switches respect their required scope boundaries.
- **`E07.04.03.F02` — Explicit Subject Transition:** Explicit subject can be transitioned through relevant changes or disruptions, with recovery where required.
- **`E07.04.03.F03` — Uncommitted Change Protection:** Uncommitted change is protected through relevant changes or disruptions, with recovery where required.
- **`E07.04.03.F04` — Cross-Context Data Isolation:** Cross-context data is isolated, including in adverse conditions.
- **`E07.04.03.F05` — Current Context Indicator:** Current context has an indicator through relevant changes or disruptions, with recovery where required.

#### E07.04.04 — Safe Interrupted Session Continuation
Restore a user’s work and navigation context after interruption without implying uncommitted actions were accepted.

*Demand: B01.04, B05.01, B14.04; evidence: PRAGMATON, PRESENT, TH; findings: R06, R11.*

- **`E07.04.04.F01` — UncommittedChangesLabelled:** Recovered drafts are clearly not authoritative.
- **`E07.04.04.F02` — AuthorityRecheckedOnResume:** Session restoration does not bypass new authorisation conditions.
- **`E07.04.04.F03` — OriginalContextIdentifiable:** Restored work preserves source unit/version identifiers.
- **`E07.04.04.F04` — InterruptedActionDisambiguated:** The UI identifies whether submission outcome is pending, failed or committed.
- **`E07.04.04.F05` — SafeContextSwitch:** Switching patient/participant context cannot carry prior restricted content across.

#### E07.04.05 — Review-to-Action Context Preservation
Maintain the identity, source version and authority of information as a consumer moves from finding to action.

*Demand: B02.01, B05.03, B10.07; evidence: PRESENT, REF, TH; findings: R06, R18.*

- **`E07.04.05.F01` — SelectedObjectStable:** A selected item remains bound to the intended governed identity.
- **`E07.04.05.F02` — VersionChangeAlerted:** Concurrent updates trigger visible context-change qualification.
- **`E07.04.05.F03` — NoCrossPersonLeakage:** Switching subjects cannot reuse the previous subject information.
- **`E07.04.05.F04` — ActionTargetConfirmed:** High-impact actions identify the actual target object and effective role.
- **`E07.04.05.F05` — AuditContextCorrelatable:** User action evidence links to the viewed source when required.

### E07.05 — Information Presentation Accountability
Show enough context about freshness, source, status and constraints to support safe interpretation.

#### E07.05.01 — Source-Attributed Information Display
Presented information carries sufficient source and provenance indicators.

- **`E07.05.01.F01` — VisibleSourceAttribution:** Displayed information provides its required source attribution indicators.
- **`E07.05.01.F02` — Visible Origin and Author:** Origin and author is visible, with accessible and accurate supporting evidence.
- **`E07.05.01.F03` — Source Trust Qualification:** Source trust is qualified.
- **`E07.05.01.F04` — Derived Versus Original Distinction:** Derived and original information remain distinguishable, with accessible and accurate supporting evidence.
- **`E07.05.01.F05` — Source Detail Accessibility:** Source detail is accessible and usable.

#### E07.05.02 — Information Currency & Status Display
Consumers can distinguish currency, provisional status and known limitations.

- **`E07.05.02.F01` — VisibleFreshnessAndStatus:** Information presentations correctly display the required freshness and status indicators.
- **`E07.05.02.F02` — Last-Updated Visibility:** Last-updated is visible, with accessible and accurate supporting evidence.
- **`E07.05.02.F03` — Provisional Versus Final Status:** Provisional versus final has an explicit status, with accessible and accurate supporting evidence.
- **`E07.05.02.F04` — Stale Information Warning:** Stale information has an explicit warning.
- **`E07.05.02.F05` — Information Validity Context:** Information validity has its relevant context.

#### E07.05.03 — Incomplete Information Disclosure
Known gaps or unavailable information are visible instead of silently implied complete.

- **`E07.05.03.F01` — VisibleKnownIncompleteness:** Known incompleteness in presented information is explicitly qualified.
- **`E07.05.03.F02` — Missing Data Notice:** Missing data is made known.
- **`E07.05.03.F03` — Incomplete View Qualification:** Incomplete view is qualified.
- **`E07.05.03.F04` — Partial Information Safe Use:** Partial information safe can be used, including in adverse conditions.
- **`E07.05.03.F05` — Missing Source Escalation Path:** Missing source escalation has a defined path, with accessible and accurate supporting evidence.

#### E07.05.04 — Presentation Restriction Disclosure
Consumers can identify where a view is purposefully restricted or redacted.

- **`E07.05.04.F01` — TransparentScopeRestriction:** Restricted views indicate applicable scope limitations without leaking protected content.
- **`E07.05.04.F02` — Restricted Content Indicator:** Restricted content has an indicator.
- **`E07.05.04.F03` — Non-Disclosure of Protected Detail:** Protected details are not disclosed, including in adverse conditions.
- **`E07.05.04.F04` — Restriction Reason Qualification:** Restriction reason is qualified, distinguishing permitted and nonconforming conditions.
- **`E07.05.04.F05` — Availability of Review Route:** Availability of review has an identifiable route, with accessible and accurate supporting evidence.

#### E07.05.05 — Decision Provenance Presentation
Make visible which human and automated inputs influenced a displayed conclusion without assigning decision authority to advice.

*Demand: B02.04, B11.03, B14.06; evidence: PROV, ASSURE, PRESENT; findings: R15, R18.*

- **`E07.05.05.F01` — EvidenceLinksUsable:** A displayed recommendation links to retrievable authorised evidence.
- **`E07.05.05.F02` — AutomatedAdviceLabelled:** AI/automated observations are recognisable as non-authoritative.
- **`E07.05.05.F03` — DecisionOwnerDistinct:** The human or authorised decision owner is distinguished from advice origin.
- **`E07.05.05.F04` — DataGapsDisplayed:** Material gaps and outdated inputs are explicit.
- **`E07.05.05.F05` — RecommendationReproducible:** Available provenance permits review of the basis used for advice.

#### E07.05.06 — Incomplete & Qualified Information Disclosure
Distinguish absent, unavailable, restricted, stale and partially derived information in human-facing presentation.

*Demand: B02.01, B10.03, B14.02; evidence: PRESENT, MN, IU; findings: R09, R11.*

- **`E07.05.06.F01` — UnavailableNotAbsent:** Temporary inaccessibility is not displayed as an absent clinical fact.
- **`E07.05.06.F02` — PermissionNotExistence:** Restriction notices do not leak protected existence details.
- **`E07.05.06.F03` — StalenessVisible:** Known source currency is shown where relevant.
- **`E07.05.06.F04` — DerivedQualificationShown:** Inferred and transformed information is visually distinguishable from native source.
- **`E07.05.06.F05` — PartialResultCaveat:** Material incompleteness is shown before a consequential user decision.

### E07.06 — Human–Digital Twin Work Interaction
Provide authorised, context-sensitive work inbox, calendar, collaboration and progress interactions between human/physical-world actors and the work governed through Digital Twins, separate from administrative OA&M control.

*Evidence-led candidate.* Lenses: DT,PD; sources: C-CT2-ACT,DRP-PR,DRP-SEC. No solution component or release commitment implied.

#### E07.06.01 — Human Work Inbox
Present task offers, assignments, deferred obligations and actionable status in a role-appropriate Twin-associated work view.

- **`E07.06.01.F01` — InboxScopeCorrect:** A practitioner sees only work appropriate to their role, patient context and effective authority.
- **`E07.06.01.F02` — OfferVsAssignedVisible:** Work offered for claim is visually distinguishable from work already assigned or accepted.
- **`E07.06.01.F03` — PendingOutcomeVisibility:** Outstanding, deferred and completed work have distinguishable dispositions.
- **`E07.06.01.F04` — MultipleTwinContextSafe:** A user acting for multiple represented entities cannot accidentally complete a task in the wrong context.
- **`E07.06.01.F05` — InboxRecovery:** Tasks remain discoverable after client restart or a period offline.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-CT2-ACT, DRP-PR. *Release horizon:* Unscheduled.

#### E07.06.02 — Task Acceptance & Participation Interaction
Enable authorised human participants to claim, accept, decline, progress and reassign work within the governing policy.

- **`E07.06.02.F01` — HumanAcceptanceAttributable:** The accepted task is linked to the participant, time and applicable authority.
- **`E07.06.02.F02` — DeclineReassignmentPath:** Decline or reassignment offers a visible, valid alternative disposition.
- **`E07.06.02.F03` — UnauthorizedActionRejected:** An unauthorised actor cannot claim, complete or reassign protected work.
- **`E07.06.02.F04` — ConcurrencyConflictExplained:** A lost race between competing task claimants yields an intelligible disposition.
- **`E07.06.02.F05` — NoUIFalseAcceptance:** Clicking an action is not portrayed as accepted until the governing task state confirms it.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: DRP-PR, DRP-SEC. *Release horizon:* Unscheduled.

#### E07.06.03 — Integrated Work & Calendar View
Present scheduled and unscheduled obligations, deadlines and work context as coherent but distinct aspects of the represented entity’s work.

- **`E07.06.03.F01` — SingleCalendarAuthorityRespected:** The view consumes the governed central calendar without creating a competing scheduling authority.
- **`E07.06.03.F02` — TaskScheduleAssociation:** A scheduled activity can be traced to its governing work obligation.
- **`E07.06.03.F03` — UnscheduledWorkVisible:** Important unscheduled obligations are not hidden merely because they have no calendar event.
- **`E07.06.03.F04` — TimeZoneAndTemporalAccuracy:** Displayed appointments, due times and changes respect relevant time-zone and temporal semantics.
- **`E07.06.03.F05` — CalendarConflictQualified:** Clashes, cancellations and reschedules are represented without silently changing task responsibility.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: C-CT2-ACT, DRP-PR. *Release horizon:* Unscheduled.

#### E07.06.04 — Task-Context Collaboration
Support contextual conversations and sharing about work without making a collaboration room authoritative for task or clinical information state.

- **`E07.06.04.F01` — ConversationTaskCorrelation:** Relevant conversations can be associated with the correct task without replacing its governed identity.
- **`E07.06.04.F02` — CollaborationDisclosureEnforced:** Messages or attachments do not bypass clinical information disclosure restrictions.
- **`E07.06.04.F03` — CommunicationNotWorkCommit:** A message stating a task is complete does not automatically commit the task or its output.
- **`E07.06.04.F04` — ParticipantMembershipCurrent:** Removed or newly unauthorised participants do not retain protected access without explicit authority.
- **`E07.06.04.F05` — ClientTechnologyIndependent:** Capability conformance can be tested without mandating Matrix/Element or a particular UI framework.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: DRP-PR, DRP-SEC. *Release horizon:* Unscheduled.

#### E07.06.05 — Asynchronous Progress & Outcome Reporting UI
Capture human reports of in-progress, partial, complete or failed physical work with attributable evidence and understandable dispositions.

- **`E07.06.05.F01` — ProgressReportAttributable:** Reporter identity and the subject task are correctly recorded.
- **`E07.06.05.F02` — IncompleteWorkNotClosed:** A partial report cannot inadvertently close an outstanding task.
- **`E07.06.05.F03` — OutcomeReviewVisible:** A report awaiting review or commit is not shown as authoritative completion.
- **`E07.06.05.F04` — LateReportHandled:** A report submitted after an offline period receives a reconciliable disposition.
- **`E07.06.05.F05` — EvidenceAttachmentGoverned:** Any submitted clinical or supporting content remains subject to Information Unit and security governance.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: DRP-PR, DRP-MN. *Release horizon:* Unscheduled.

#### E07.06.06 — Administration vs Operational Work Experience
Maintain a clear boundary between administration/OA&M of Digital Twins and their human task, collaboration and calendar experience.

- **`E07.06.06.F01` — OAAMActionSeparation:** Operational maintenance actions are not presented as clinical or human task fulfilment.
- **`E07.06.06.F02` — RoleScopedAdministrativeControls:** Administrative permissions do not automatically grant clinical information disclosure or task decision authority.
- **`E07.06.06.F03` — TwinStateVsHumanWorkClarity:** A user can distinguish Digital Twin platform state from the physical entity’s outstanding work.
- **`E07.06.06.F04` — UserContextIsolation:** Moving between administrative and task contexts does not leak previously viewed protected information.
- **`E07.06.06.F05` — UsableWorkNavigation:** A relevant authorised participant can find and act on their tasks without OA&M platform knowledge.

*Source reasoning:* INFERRED_FROM_EVIDENCE; evidence keys: DRP-SEC, C-CT2-ACT. *Release horizon:* Unscheduled.

## E08 — Operational Observability, Assurance & Resilience
The system can reveal its operational state, preserve evidence, support controlled response and recover service.

### E08.01 — Operational Observability
Provide meaningful operational health, event and performance signals.

#### E08.01.01 — Service Health Visibility
Operations can determine whether required digital services are healthy.

- **`E08.01.01.F01` — AccurateServiceHealth:** Service health is accurately represented for the applicable operational conditions.
- **`E08.01.01.F02` — Current Health Status:** Current health has an explicit status, with accessible and accurate supporting evidence.
- **`E08.01.01.F03` — Service Dependency Awareness:** Service dependency is made known.
- **`E08.01.01.F04` — Degradation State Recognition:** Degradation state is recognised, with accessible and accurate supporting evidence.
- **`E08.01.01.F05` — Health Status History:** Health status retains its history, with accessible and accurate supporting evidence.

#### E08.01.02 — Operational Performance Visibility
Operations can inspect response, queue, throughput and pressure indicators.

- **`E08.01.02.F01` — ActionablePerformanceSignals:** Defined performance conditions are represented by appropriate actionable measures.
- **`E08.01.02.F02` — Workload and Throughput View:** Workload and throughput can be viewed.
- **`E08.01.02.F03` — Latency Distribution Visibility:** Service latency distributions are visible within the applicable context-defined interval.
- **`E08.01.02.F04` — Resource Saturation Awareness:** Resource saturation is made known, with accessible and accurate supporting evidence.
- **`E08.01.02.F05` — Performance Trend History:** Performance trend retains its history, with accessible and accurate supporting evidence.

#### E08.01.03 — Dependency Status Visibility
Operations can inspect relevant supporting-service status for diagnosis.

- **`E08.01.03.F01` — VisibleDependencyHealth:** Dependency faults are surfaced at the appropriate operational scope.
- **`E08.01.03.F02` — Dependency Availability Recognition:** Dependency availability is recognised, with accessible and accurate supporting evidence.
- **`E08.01.03.F03` — Upstream Failure Propagation:** The propagation of upstream failures can be identified.
- **`E08.01.03.F04` — Dependency Impact View:** Dependency impact can be viewed.
- **`E08.01.03.F05` — Dependency Recovery Status:** Dependency recovery has an explicit status, with accessible and accurate supporting evidence.

#### E08.01.04 — Dependency & Readiness Truthfulness
Represent dependency availability, degraded status and recovery readiness across service domains.

*Demand: B07.06, B12.04, B12.05; evidence: OPS, NFR; findings: R24.*

- **`E08.01.04.F01` — CriticalDependencyKnown:** Each in-scope component declares material execution dependencies.
- **`E08.01.04.F02` — DependencyFaultPropagated:** Unhealthy dependency affects readiness as required.
- **`E08.01.04.F03` — NoFalseHealthyOnIsolation:** Partitioned service is not reported healthy when unable to meet its contract.
- **`E08.01.04.F04` — RestorationVerified:** Green status requires bounded functional recovery evidence where applicable.
- **`E08.01.04.F05` — OperationalViewTimely:** Material readiness changes reach operational observers within the defined target.

### E08.02 — Operational Logging & Evidence Collection
Capture relevant event information safely and traceably.

#### E08.02.01 — Governed Operational Logging
Significant operational events can be recorded with necessary context and restrictions.

- **`E08.02.01.F01` — CompleteOperationalEventEvidence:** Required operational events are captured with their required context.
- **`E08.02.01.F02` — Structured Operational Event:** Operational events retain their required structure.
- **`E08.02.01.F03` — Required Log Event Coverage:** Required log event is covered.
- **`E08.02.01.F04` — Log Transport Reliability:** Log transport is reliable.
- **`E08.02.01.F05` — Log Retention Visibility:** Log retention is visible, with accessible and accurate supporting evidence.

#### E08.02.02 — Sensitive Information Exclusion
Logging does not expose protected information beyond approved controls.

- **`E08.02.02.F01` — LoggingPrivacyConformance:** Operational logging excludes prohibited content, including under adverse conditions.
- **`E08.02.02.F02` — Protected Content Redaction:** Protected content is redacted, including in adverse conditions.
- **`E08.02.02.F03` — Log Privacy Boundary Check:** Log privacy boundary is checked, including in adverse conditions.
- **`E08.02.02.F04` — Sensitive Field Detection:** Sensitive field is detected.
- **`E08.02.02.F05` — Controlled Logging Override:** Controlled logging has an explicit override.

#### E08.02.03 — Operational Evidence Retrieval
Authorised operations personnel can retrieve relevant retained operational records.

- **`E08.02.03.F01` — SuccessfulOperationalEvidenceRetrieval:** Valid operational evidence requests can be completed correctly.
- **`E08.02.03.F02` — Relevant Evidence Search:** Relevant evidence can be searched, with accessible and accurate supporting evidence.
- **`E08.02.03.F03` — Time-Bounded Evidence Selection:** Requested operational evidence can be selected within the applicable context-defined interval.
- **`E08.02.03.F04` — Evidence Integrity Check:** Evidence integrity is checked.
- **`E08.02.03.F05` — Evidence Access Control:** Evidence access is controlled, with accessible and accurate supporting evidence.

#### E08.02.04 — Operational Log Satellite & Receipt
Capture local operational events through in-JVM reception, buffering and forwarding with explicit server-receipt semantics.

*Demand: B07.06, B12.04; evidence: LOG, OPS; findings: R14.*

- **`E08.02.04.F01` — LocalReceiverPresent:** Required application components can emit standard log events.
- **`E08.02.04.F02` — BufferPressureObserved:** Buffer saturation is measured and reported.
- **`E08.02.04.F03` — ServerReceiptAccurate:** Satellite ACK refers to server receipt, not persistent storage.
- **`E08.02.04.F04` — PHIControlsApplied:** Logging paths apply approved data-minimisation/handling requirements.
- **`E08.02.04.F05` — SatelliteFailureQualification:** JVM loss and unpersisted event exposure are not falsely claimed lossless.

#### E08.02.05 — Log Server & Persistence Independence
Operate receiving/forwarding/log storage boundaries without conflating operational log custody with Mnemosyne or audit custody.

*Demand: B12.04, B12.03; evidence: LOG, OPS; findings: R14.*

- **`E08.02.05.F01` — LogServerAckDistinct:** Receipt ACK remains distinct from durable log storage acknowledgement.
- **`E08.02.05.F02` — LogStoreIndependent:** Log-Persistence is not represented as a Mnemosyne PersistenceServiceInstance.
- **`E08.02.05.F03` — BackpressureHandled:** Log transport congestion is observable and controlled.
- **`E08.02.05.F04` — LogRecoveryQualified:** Post-failure log completeness claims reflect the actual receipt/durability model.
- **`E08.02.05.F05` — FOSSDeploymentConformant:** Chosen logging infrastructure licensing remains within mandated FOSS bounds.

### E08.03 — Audit & Provenance Reporting
Establish attributable records of significant actions and information origins.

#### E08.03.01 — Governed Audit Event Availability
Required significant activity can be inspected as structured audit evidence.

- **`E08.03.01.F01` — CompleteAuditEventEvidence:** In-scope activity generates the required attributable AuditEvents.
- **`E08.03.01.F02` — Near-Source Audit Capture:** Near-source audit is captured, with accessible and accurate supporting evidence.
- **`E08.03.01.F03` — Audit Event Completeness:** Audit event is complete.
- **`E08.03.01.F04` — Durable Audit Availability:** Durable audit is available, with accessible and accurate supporting evidence.
- **`E08.03.01.F05` — Audit Event Correlation:** Audit event is correlated, with accessible and accurate supporting evidence.

#### E08.03.02 — Auditable Activity History
Reviewers can follow relevant sequences of governed actions and outcomes.

- **`E08.03.02.F01` — ReviewableActivitySequence:** Action chains can be traced through the required audit evidence.
- **`E08.03.02.F02` — Attributable Event Chronology:** Attributable event has an identifiable chronology, with accessible and accurate supporting evidence.
- **`E08.03.02.F03` — Activity Scope Filtering:** Activity scope is filtered, distinguishing permitted and nonconforming conditions.
- **`E08.03.02.F04` — Tamper-Evident History:** Tamper-evident retains its history, with accessible and accurate supporting evidence.
- **`E08.03.02.F05` — Historical Audit Retrieval:** Historical audit can be retrieved, with accessible and accurate supporting evidence.

#### E08.03.03 — Information Provenance Visibility
Consumers can determine the available origin and derivation of governed information.

- **`E08.03.03.F01` — AvailableInformationProvenance:** Governed information has retrievable provenance context within the applicable scope.
- **`E08.03.03.F02` — Origin Trace Availability:** Origin trace is available, with accessible and accurate supporting evidence.
- **`E08.03.03.F03` — Transformation Chain Visibility:** Transformation chain is visible, with accessible and accurate supporting evidence.
- **`E08.03.03.F04` — Version Provenance Consistency:** Version provenance is consistent.
- **`E08.03.03.F05` — Uncertain Provenance Qualification:** Uncertain provenance is qualified.

### E08.04 — Conformance & Assurance Support
Supply evidence for evaluating whether system behaviour meets required constraints.

**Adjudicated FC-016 / FC-033 / FC-074 clarification — Independent Assurance Execution:** Assurance assessment **SHALL execute as a separately governed activity, logically independent** of the service, Praxis, Journey or processing activity being assessed. It SHALL evaluate applicable assurance criteria against available evidence and produce its own findings, qualifications and conclusions. Assurance execution SHALL NOT form part of the assessed activity's fulfilment or determine its operational outcome, and SHALL NOT recursively assure itself. This is logical/execution separation, **not** a mandate for separate JVMs or physical deployment. Evidence capture during ordinary processing is distinct from independent assessment through Dokimasia. R1.x/R2.x assurance conclusions remain observational/advisory, not autonomous operational or release authority.

#### E08.04.01 — Release Conformance Evidence
Reviewers can obtain evidence of required conformance for a released information system.

- **`E08.04.01.F01` — CompleteReleaseConformanceEvidence:** Applicable release controls have the required verified conformance evidence.
- **`E08.04.01.F02` — Required Test Evidence Completeness:** Required test evidence is complete.
- **`E08.04.01.F03` — Release Baseline Correlation:** Release baseline is correlated.
- **`E08.04.01.F04` — Independent Evidence Access:** Independent evidence can be accessed through available and usable support.
- **`E08.04.01.F05` — Conformance Finding Qualification:** Conformance finding is qualified, distinguishing permitted and nonconforming conditions.

#### E08.04.02 — Assurance Observation Support
Assurance reviewers can obtain relevant information about observed behaviour.

- **`E08.04.02.F01` — AvailableAssuranceObservation:** Required assurance observations produce inspectable governed evidence.
- **`E08.04.02.F02` — Observation Scope Transparency:** Observation scope is transparent, distinguishing permitted and nonconforming conditions.
- **`E08.04.02.F03` — Non-Authoritative Assurance Findings:** Assurance findings remain explicitly non-authoritative.
- **`E08.04.02.F04` — Assurance Evidence Correlation:** Assurance evidence is correlated, with accessible and accurate supporting evidence.
- **`E08.04.02.F05` — Assurance Exception Reporting:** Assurance exception is reported, with accessible and accurate supporting evidence.

#### E08.04.03 — Qualification & Exception Reporting
Limitations and unresolved assurance concerns can be reported without false pass/fail claims.

- **`E08.04.03.F01` — AccurateAssuranceQualification:** Insufficient assurance evidence produces a qualified finding rather than an unsupported passing result.
- **`E08.04.03.F02` — Evidence Insufficiency Disclosure:** Evidence insufficiency is disclosed.
- **`E08.04.03.F03` — Qualified Finding Severity:** Assurance findings have their severity appropriately qualified.
- **`E08.04.03.F04` — Exception Ownership Visibility:** Exception ownership is visible, with accessible and accurate supporting evidence.
- **`E08.04.03.F05` — Corrective Evidence Follow-Up:** Corrective evidence receives follow-up.

#### E08.04.04 — Evidence-Based Architectural Conformance
Compare release state against governed capabilities, service obligations, NFRs and integrity tests without mistaking test presence for satisfaction.

*Demand: B11.03, B11.05, B12.03; evidence: NFR, ASSURE, JOURNEY; findings: R15, R18.*

- **`E08.04.04.F01` — EveryRequirementDispositioned:** Applicable requirements have independently evidenced pass/fail/qualified statuses.
- **`E08.04.04.F02` — CapabilityClaimTraceable:** Claims point to tests and exact architecture versions.
- **`E08.04.04.F03` — UnmeasuredFeatureUnclaimed:** No Feature is labelled satisfied solely from a mapped CT2 or Service.
- **`E08.04.04.F04` — ExceptionsInReleaseReport:** Deferred and failed checks remain visible in assurance publication.
- **`E08.04.04.F05` — ReproductionEvidenceAvailable:** A reviewer can repeat a documented reference test without unavailable private state.

### E08.05 — Operational Fault Management
Detect, diagnose, contain and support remedy of faults.

#### E08.05.01 — Operational Fault Detection
Operations can identify specified faults and degradations.

- **`E08.05.01.F01` — TimelyFaultVisibility:** Operational faults are detected within the defined observation conditions.
- **`E08.05.01.F02` — Timely Fault Signal:** Operational fault signals are available within the applicable context-defined interval.
- **`E08.05.01.F03` — Fault Type Classification:** Fault type is classified.
- **`E08.05.01.F04` — False Alarm Qualification:** False alarm is qualified, including in adverse conditions.
- **`E08.05.01.F05` — Escalation Trigger Reliability:** Escalation trigger is reliable.

#### E08.05.02 — Fault Context Isolation
Operations can distinguish the failing concern and affected scope.

- **`E08.05.02.F01` — AccurateFaultScope:** Faults are assigned to their correct affected scope.
- **`E08.05.02.F02` — Affected Component Boundary:** Affected component has an explicit boundary.
- **`E08.05.02.F03` — Fault Dependency Context:** Fault dependency has its relevant context.
- **`E08.05.02.F04` — Safe Diagnostic Evidence:** Safe diagnostic is evidenced, including in adverse conditions.
- **`E08.05.02.F05` — Isolation Decision Trace:** Isolation decision is traceable, including in adverse conditions.

#### E08.05.03 — Operational Incident Notification
Responsible operations functions can be informed of significant faults.

- **`E08.05.03.F01` — CorrectIncidentNotification:** Qualifying faults are notified to their required audiences.
- **`E08.05.03.F02` — Intended On-Call Recipient:** Intended on-call recipient reaches and can be understood by the intended recipient.
- **`E08.05.03.F03` — Incident Severity Explanation:** Incident severity is explained.
- **`E08.05.03.F04` — Notification Acknowledgement:** Notification acknowledgement reaches and can be understood by the intended recipient.
- **`E08.05.03.F05` — Missed Notification Escalation:** Missed notification escalation reaches and can be understood by the intended recipient.

### E08.06 — Service Continuity & Recovery
Recover necessary operation and preserve accepted work through failures.

#### E08.06.01 — Controlled Service Recovery
Services can recover through prescribed failures or controlled restarts.

- **`E08.06.01.F01` — VerifiedServiceRecovery:** Service recovery meets its declared recovery expectations.
- **`E08.06.01.F02` — Service Recovery Procedure:** Service recovery has an established procedure through relevant changes or disruptions, with recovery where required.
- **`E08.06.01.F03` — Recovery Dependency Ordering:** Recovery dependency is ordered through relevant changes or disruptions, with recovery where required.
- **`E08.06.01.F04` — Service Recovery Outcome Verification:** Service recovery outcome is verified through relevant changes or disruptions, with recovery where required.
- **`E08.06.01.F05` — Controlled Restart Visibility:** Controlled restart is visible, with accessible and accurate supporting evidence.

#### E08.06.02 — Accepted Work Preservation
Information system interruption does not silently lose accepted governed work.

- **`E08.06.02.F01` — Pre-Processing Work Preservation Assurance:** As accepted work is prepared for processing, its existing durable preservation and the recoverability of required processing state are checked and established for supported ordinary component or node failures. This assurance does not require a second persistence of already preserved work and does not establish catastrophic recovery.
- **`E08.06.02.F02` — Durable In-Flight Work:** In-flight work remains durable.
- **`E08.06.02.F03` — Interrupted Work Recognition:** Interrupted work is recognised, with accessible and accurate supporting evidence.
- **`E08.06.02.F04` — Resumed Work Continuity:** Resumed work is maintained through relevant changes or disruptions, with recovery where required.
- **`E08.06.02.F05` — No-Loss Recovery Evidence:** No-loss recovery is evidenced, with accessible and accurate supporting evidence.

#### E08.06.03 — Recovery Readiness Evidence
Operators can determine whether required recovery procedures remain supportable.

- **`E08.06.03.F01` — CurrentRecoveryEvidence:** Prescribed recovery capabilities retain recent valid exercise evidence.
- **`E08.06.03.F02` — Recovery Exercise Currency:** Recovery exercise is current through relevant changes or disruptions, with recovery where required.
- **`E08.06.03.F03` — Failover Evidence Availability:** Failover evidence is available, with accessible and accurate supporting evidence.
- **`E08.06.03.F04` — Critical Recovery Gap Visibility:** Critical recovery gap is visible, including in adverse conditions.
- **`E08.06.03.F05` — Recovery Capacity Qualification:** Recovery capacity is qualified.

#### E08.06.04 — Disaster Recovery Consistency Proof
Demonstrate that information, accepted work and operational services recover coherently after bounded and catastrophic failure.

*Demand: B12.04, B12.05, B10.01; evidence: RES, RES-W, NFR; findings: R24, R21.*

- **`E08.06.04.F01` — SingleNodeFailureTransparent:** Accepted work remains recoverable across tested single-node loss.
- **`E08.06.04.F02` — DisasterRecoveryPointKnown:** Recovered authoritative state has known consistent recovery point.
- **`E08.06.04.F03` — Catastrophic Work Recoverability:** Following catastrophic infrastructure failure, previously accepted governed work and its required processing state can be recovered from surviving or restored recovery mechanisms, made accessible to Harmonia and returned to governed processing without loss of accepted work.
- **`E08.06.04.F04` — RecoveryIdentityUnchanged:** Restoration preserves canonical information and task identities.
- **`E08.06.04.F05` — RecoveryEvidenceReproducible:** Fault injection and restoration outcomes are independently documented.

### E08.07 — Audit, Provenance & Assurance Evidence Recording
Create and preserve distinct immutable audit and provenance evidence and accountable assurance observations without conflating operational logs.


#### E08.07.01 — Mandatory Audit Contribution
Capture auditable actions close to their source without making each source the authoritative AuditEvent custodian.

*Demand: B10.02, B10.06, B11.02; evidence: AUD, TH, PROV; findings: R03, R14.*

- **`E08.07.01.F01` — SourceAuditContributionPresent:** Mandatory covered actions generate required contributions.
- **`E08.07.01.F02` — AuditNotOperationalLog:** A log message alone cannot satisfy mandatory AuditEvent contribution.
- **`E08.07.01.F03` — EventActorContextCorrelated:** Subject, action, identity and relevant Task/IU references are retained.
- **`E08.07.01.F04` — AuditFailureReported:** Failure to meet mandatory audit obligations is never silent.
- **`E08.07.01.F05` — SensitiveEvidenceProtected:** Audit contribution avoids unnecessary PHI leakage while retaining required accountability.

#### E08.07.02 — Immutable AuditEvent Record Management
Maintain append-only governed AuditEvent records, including correction by subsequent records and independent access history.

*Demand: B10.06, B11.03, B11.05; evidence: AUD, PROV; findings: R03.*

- **`E08.07.02.F01` — NoInPlaceAuditMutation:** Attempted alteration of stored AuditEvent is rejected.
- **`E08.07.02.F02` — QualificationIsNewRecord:** A correction creates attributable new record linked to predecessor.
- **`E08.07.02.F03` — AuditRecordReadSeparated:** Reading an AuditEvent does not create a recursive new AuditEvent.
- **`E08.07.02.F04` — AccessHistoryIndependent:** Accesses to audit records are captured through the agreed separate history mechanism.
- **`E08.07.02.F05` — AuditRecordRecoverable:** Immutable evidence records survive the required recovery and retention conditions.

#### E08.07.03 — Chronological Provenance Evidence
Preserve ordered provenance evidence with temporal interpretation independent of recording order.

*Demand: B10.02, B10.07, B11.03; evidence: PROV, IU, REF; findings: R03.*

- **`E08.07.03.F01` — ChronologicalOrderPreserved:** Evidence list ordering matches the chronology of the represented activities.
- **`E08.07.03.F02` — RecordedTimeNotSubstituted:** Late evidence insertion does not silently redefine occurrence order.
- **`E08.07.03.F03` — EvidenceReferencesValid:** Referenced evidence has stable identity and role semantics.
- **`E08.07.03.F04` — ImmutableProvenanceProtected:** Recorded Provenance and its evidence order cannot be edited in place.
- **`E08.07.03.F05` — IncompleteLineageQualified:** Unresolved evidence is explicitly identified without inventing a complete chain.

#### E08.07.04 — Assurance Evidence Collection & Classification
Collect and classify conformance observations, test results and exceptions without confounding audit or provenance authority.

*Demand: B11.03, B11.05, B12.03; evidence: ASSURE, NFR, PROV; findings: R03, R15.*

- **`E08.07.04.F01` — EvidenceTypeDistinguished:** Assurance, operational log, audit and provenance types remain separately identifiable.
- **`E08.07.04.F02` — ObservationSourceKnown:** Collected findings reference the tested system/version and source.
- **`E08.07.04.F03` — AssuranceFindingRepeatable:** A known test observation can be reproduced or its limits explained.
- **`E08.07.04.F04` — QualificationNotDiscarded:** Adverse or partial findings remain visible within release evidence.
- **`E08.07.04.F05` — NoAutomatedApproval:** AI assurance output does not automatically confer release acceptance.

### E08.08 — Evidence Collation, Export & Controlled Release
Select, collate, validate and release audit, provenance and assurance evidence while preserving provenance, completeness and authorisation.


#### E08.08.01 — Non-Dereferenced Record Export
Export governed raw logging, AuditEvent and Provenance records and their references without resolving underlying evidence payloads.

*Demand: B10.06, B11.05, B12.03; evidence: PROV, LOG, TH; findings: R04, R14.*

- **`E08.08.01.F01` — RawRecordsRemainRaw:** Export does not silently dereference protected underlying information.
- **`E08.08.01.F02` — TemporalSelectionExplicit:** Occurrence-time and persistence-time filtering are distinguished or left unresolved.
- **`E08.08.01.F03` — RecordIntegrityInExport:** Exported record identities and version/hash evidence survive round trip.
- **`E08.08.01.F04` — BulkAndDemandSelection:** Bulk and on-demand record selections produce traceable result scopes.
- **`E08.08.01.F05` — ExportAuthorityEnforced:** Unauthorised record selection fails without leaking restricted content.

#### E08.08.02 — Dereferenced Audit Evidence Assembly
Assemble authorised explanatory packages for selected AuditEvents while preserving underlying event immutability.

*Demand: B10.06, B11.03, B11.05; evidence: PROV, TH, ASSURE; findings: R04.*

- **`E08.08.02.F01` — EventSetPreciselySelected:** Requested AuditEvent set and selection criteria are recoverable.
- **`E08.08.02.F02` — ReferencedEvidenceGathered:** Required evidence follows governed reference resolution rules.
- **`E08.08.02.F03` — UnresolvedEvidenceDeclared:** Missing/invalid evidence triggers explicit bounded outcome.
- **`E08.08.02.F04` — Policy-Governed Audit Evidence Release:** Audit evidence release respects constituent authorisation and applicable completeness policy. A denied required constituent prevents release of the whole set where all-or-nothing treatment applies; policy-permitted selective release protects withheld information and its protected existence.
- **`E08.08.02.F05` — AuditPackageInterpretable:** An authorised recipient can understand the export without presumed database access.

#### E08.08.03 — Dereferenced Provenance Evidence Assembly
Provenance chains and contextual evidence are assembled and dereferenced with attribution, relationships, completeness, chronology and qualifications preserved. Assembly completeness remains distinct from the policy-permitted disclosure scope.

*Demand: B10.02, B11.05, B13.05; evidence: PROV, REF, TH; findings: R04, R23.*

- **`E08.08.03.F01` — ProvenanceSetPrecise:** Selected lineage roots and temporal context are explicit.
- **`E08.08.03.F02` — ChronologyPreservedInExport:** Evidence remains in agreed chronological semantic order.
- **`E08.08.03.F03` — RecursionPolicyBounded:** Recursive expansion follows the declared traversal constraints.
- **`E08.08.03.F04` — MissingEvidenceQualified:** Unavailable but relevant evidence is not silently invented.
- **`E08.08.03.F05` — Evidence Assembly Completeness and Qualification:** Assembled provenance evidence identifies its required constituents, relationships and completeness, preserving attribution, chronology and qualifications for unavailable evidence. Assembly completeness is distinguished from the portion permitted for disclosure.

#### E08.08.04 — Governed Evidence Disclosure
Evidence is disclosed within policy-governed constituent authorisation and completeness constraints, supporting selective disclosure where permitted and all-or-nothing treatment where required. Staged payloads, release-time authority and failure reporting protect withheld content and its protected existence.

*Demand: B10.06, B11.05, B13.04; evidence: PROV, TH, SEC; findings: R04, R23.*

- **`E08.08.04.F01` — Selective Evidence Disclosure:** Evidence disclosure releases only the constituents permitted by the applicable policy. Selective release is supported where permitted, and all-or-nothing release is enforced where the governing policy requires completeness; withheld content is protected.
- **`E08.08.04.F02` — Disclosure Completeness and Qualification:** The disclosed evidence set is complete for its permitted scope or appropriately qualified under the applicable policy. Omission details, qualifications and errors do not reveal protected content or the protected existence of withheld evidence.
- **`E08.08.04.F03` — Staged Payload Protection:** Intermediate assembled information remains protected and is never publicly addressable before authorised release.
- **`E08.08.04.F04` — Release-Time Authorisation:** Authorisation is valid for each released constituent and the applicable disclosure scope at the actual release boundary.
- **`E08.08.04.F05` — Protected Disclosure Failure Reporting:** Disclosure failures provide an appropriately governed remedial path without revealing protected evidence through content, omission details or error metadata.

## E09 — Information Insight & Evidence Use
The system can organise information for queries, analysis and decision-supporting evidence without acquiring the decision authority itself.

### E09.01 — Information Set & Result Organisation
Establish referenced result sets with explicit meaning and context.

#### E09.01.01 — Version-Referenced Result Set
Consumers can use a governed collection of identified information versions.

- **`E09.01.01.F01` — ValidResultSetReferences:** Result-set references resolve to the correct governed information versions.
- **`E09.01.01.F02` — Version-Identified Search Membership:** Version-identified search has identifiable membership.
- **`E09.01.01.F03` — Result-Set Identity Stability:** Result-set identity remains stable.
- **`E09.01.01.F04` — Source Version Resolution:** Source version is resolved.
- **`E09.01.01.F05` — Result Membership Explanation:** Result membership is explained.

#### E09.01.02 — Preserved Result Set Context
Work can resume from an understandable previously identified result set.

- **`E09.01.02.F01` — ReusableResultSetContext:** Persisted result sets retain the context required for reuse across resumption.
- **`E09.01.02.F02` — Stored Query Intent:** Stored query has explicit intent.
- **`E09.01.02.F03` — Reusable Selection Parameters:** Search selection parameters can be reused through relevant changes or disruptions, with recovery where required.
- **`E09.01.02.F04` — Result Context Integrity:** Result context retains its integrity.
- **`E09.01.02.F05` — Context Expiration Qualification:** Context expiration is qualified.

#### E09.01.03 — Result Set Provenance
Consumers can tell how and under which conditions a result set was formed.

- **`E09.01.03.F01` — AttributableResultSetFormation:** Result sets retain the required query and formation provenance.
- **`E09.01.03.F02` — Query Origin Attribution:** Query origin is attributable, with accessible and accurate supporting evidence.
- **`E09.01.03.F03` — Collection Assembly Trace:** Collection assembly is traceable, with accessible and accurate supporting evidence.
- **`E09.01.03.F04` — Result Version Provenance:** Result version retains its provenance.
- **`E09.01.03.F05` — Result-Set Review History:** Result-set review retains its history, with accessible and accurate supporting evidence.

#### E09.01.04 — Stable Versioned Information Set
Define a governed collection of IU version references with reproducible selection and work-context preservation.

*Demand: B13.05, B10.02, B11.03; evidence: IU, RESULT, PRAGMATON; findings: R23, R09.*

- **`E09.01.04.F01` — IUVersionRefsOnly:** Search result sets retain governed IU version references rather than uncontrolled payload copies.
- **`E09.01.04.F02` — SetIdentityRepeatable:** A fixed set reference reproduces identical membership.
- **`E09.01.04.F03` — SelectionProvenanceKnown:** Query criteria, requester and time context can be inspected.
- **`E09.01.04.F04` — SetContextDurablyPreserved:** Relevant Ergon context can recover result-set membership.
- **`E09.01.04.F05` — RestrictedMembersNotLeaked:** Results are re-evaluated for applicable access before disclosure.

### E09.02 — Information Aggregation & Analysis
Combine information appropriately for analytical use.

#### E09.02.01 — Governed Information Aggregation
Information from multiple authorised sources can be assembled for an analytical purpose.

- **`E09.02.01.F01` — CorrectAggregatedScope:** Aggregates satisfy their applicable scope and source constraints.
- **`E09.02.01.F02` — Aggregation Scope Conformance:** Aggregation scope conforms to the applicable requirements, distinguishing permitted and nonconforming conditions.
- **`E09.02.01.F03` — Aggregate Source Lineage:** Aggregate source retains its lineage, with accessible and accurate supporting evidence.
- **`E09.02.01.F04` — Completeness of Contributing Inputs:** Completeness of contributing inputs are available.
- **`E09.02.01.F05` — Governed Aggregate Disclosure:** Governed aggregate is disclosed, distinguishing permitted and nonconforming conditions.

#### E09.02.02 — Repeatable Information Analysis
Consumers can reproduce supported analysis from appropriately preserved inputs and methods.

- **`E09.02.02.F01` — ReproducibleAnalysisOutcome:** Analyses are repeatable within their declared tolerances.
- **`E09.02.02.F02` — Reproducible Analysis Inputs:** Analysis inputs can be reproduced.
- **`E09.02.02.F03` — Analytical Method Version:** Analytical method has an identifiable version.
- **`E09.02.02.F04` — Deterministic Result Reproduction:** Deterministic result can be reproduced.
- **`E09.02.02.F05` — Analysis Limitation Disclosure:** Analysis limitation is disclosed, with accessible and accurate supporting evidence.

#### E09.02.03 — Temporal & Provenance Aware Aggregation
Aggregate information while retaining source versions, definition changes and temporal applicability.

*Demand: B13.01, B11.04, B15.05; evidence: IU, PROV, INSIGHT; findings: R23.*

- **`E09.02.03.F01` — SourceVersionTraceable:** Reported aggregate can identify contributing governed versions.
- **`E09.02.03.F02` — TemporalWindowCorrect:** The declared occurrence/recording time basis is applied consistently.
- **`E09.02.03.F03` — DefinitionChangeQualified:** Model changes affecting comparability are declared.
- **`E09.02.03.F04` — RestrictedContributionControlled:** Security limits apply to aggregate disclosure or suppressions.
- **`E09.02.03.F05` — CalculationReproducible:** A fixed dataset and transformation definition reproduce an aggregate.

#### E09.02.04 — Population Insight Without Identity Leakage
Produce allowed population-level metrics with defensible suppression, scope and data quality checks.

*Demand: B13.01, B13.03, B15.05; evidence: INSIGHT, TH, IU; findings: R12, R23.*

- **`E09.02.04.F01` — SuppressionPolicyEnforced:** Disallowed small-cell or sensitive breakdown release is blocked where policy applies.
- **`E09.02.04.F02` — PopulationDenominatorKnown:** Reported rates state inclusion criteria and denominator version.
- **`E09.02.04.F03` — MissingDataBiasFlagged:** Material incompleteness is visible rather than hidden.
- **`E09.02.04.F04` — TimeComparisonComparable:** Series identify source and definition changes.
- **`E09.02.04.F05` — NoIndividualDisclosure:** Aggregate pathways do not reveal protected individual records.

### E09.03 — Operational & Business Information Reporting
Produce timely, provenance-aware information products and reports.

#### E09.03.01 — Operational Information Reporting
Operators can receive reports about digital service state and trends.

- **`E09.03.01.F01` — CorrectOperationalReport:** Operational reports accurately reflect accepted source evidence.
- **`E09.03.01.F02` — Relevant Operational Indicators:** Operational indicators are relevant.
- **`E09.03.01.F03` — Current Reporting Period:** Reporting period is current, with accessible and accurate supporting evidence.
- **`E09.03.01.F04` — Report Exception Highlight:** Report exception is highlighted, with accessible and accurate supporting evidence.
- **`E09.03.01.F05` — Scheduled Report Availability:** Scheduled operational reports are available within the applicable context-defined interval.

#### E09.03.02 — Business Information Reporting
Authorised users can receive coherent information products for enterprise decisions.

- **`E09.03.02.F01` — TraceableBusinessReport:** Business reports can be traced to governed input versions.
- **`E09.03.02.F02` — Decision-Relevant Business Measure:** Business reports provide measures relevant to the intended decision.
- **`E09.03.02.F03` — Business Context Attribution:** Business context is attributable, with accessible and accurate supporting evidence.
- **`E09.03.02.F04` — Business Report Comparability:** Business report can be compared, with accessible and accurate supporting evidence.
- **`E09.03.02.F05` — Governed Business Disclosure:** Governed business are disclosed, distinguishing permitted and nonconforming conditions.

#### E09.03.03 — Controlled Information Export
Authorised report outputs can be shared in supported forms.

- **`E09.03.03.F01` — GovernedReportDelivery:** Report delivery satisfies the applicable scope, format and information rights.
- **`E09.03.03.F02` — Authorised Export Purpose:** Authorised export has an explicit purpose, distinguishing permitted and nonconforming conditions.
- **`E09.03.03.F03` — Minimum Necessary Export Content:** Minimum necessary export content is available.
- **`E09.03.03.F04` — Export Format Fidelity:** Export format is faithfully preserved.
- **`E09.03.03.F05` — Export Recipient Accountability:** Export recipient has an accountable party, with accessible and accurate supporting evidence.

#### E09.03.04 — Controlled Business Evidence Reporting
Provide accountable business and service reports without implying operation of the underlying business transaction engines.

*Demand: B07.06, B12.03, B15.04; evidence: INSIGHT, PROV, TH; findings: R12, R23.*

- **`E09.03.04.F01` — ReportingSourceKnown:** Report measures link to governed sources and calculation definitions.
- **`E09.03.04.F02` — NoImplicitClaimsAdjudication:** A funding report is not represented as a claims decision service.
- **`E09.03.04.F03` — ReportScopeAuthorised:** Recipient receives only data within authorised purpose.
- **`E09.03.04.F04` — ReportQualificationVisible:** Known coverage and completeness limitations are explicit.
- **`E09.03.04.F05` — ReportVersionReproducible:** A historical report can be reconstructed from pinned inputs.

#### E09.03.05 — Commissioning & Funding Accountability Data
Expose trusted information supporting commissioning and funding review without presuming an internal payer platform.

*Demand: B15.01, B15.04, B15.05; evidence: INSIGHT, TH, PROV; findings: R12.*

- **`E09.03.05.F01` — CommissioningContextTraceable:** Recommendations identify source population/service need evidence.
- **`E09.03.05.F02` — EntitlementNotDigitalGrant:** Funding eligibility is distinguished from information access permission.
- **`E09.03.05.F03` — ClaimsStatusNotAdjudication:** Status reports do not assert payment/claims processing authority.
- **`E09.03.05.F04` — FundingReportReproducible:** Versioned inputs support independent audit of reported amount/status.
- **`E09.03.05.F05` — ScopeOfSolutionExplicit:** Unimplemented or external financial functions remain documented as dependencies.

### E09.04 — Evidence & Insight Presentation
Expose patterns, measures and supporting evidence with their limitations.

#### E09.04.01 — Evidence-Linked Insight Presentation
Analytical findings can be presented with source evidence and limitations.

- **`E09.04.01.F01` — SupportedInsightClaim:** Insights have accessible supporting evidence within the applicable scope.
- **`E09.04.01.F02` — Insight Source Traceability:** Insight source is traceable, with accessible and accurate supporting evidence.
- **`E09.04.01.F03` — Supporting Evidence Accessibility:** Supporting evidence is accessible and usable.
- **`E09.04.01.F04` — Qualified Insight Confidence:** Qualified insight has its confidence qualified.
- **`E09.04.01.F05` — Evidence-Insight Consistency:** Evidence-insight is consistent, with accessible and accurate supporting evidence.

#### E09.04.02 — Trend & Change Interpretation
Users can inspect observed change through meaningful time and population context.

- **`E09.04.02.F01` — QualifiedTrendPresentation:** Trend presentations retain the required time, scope and quality qualifications.
- **`E09.04.02.F02` — Valid Time-Series Comparison:** Valid time-series can be compared.
- **`E09.04.02.F03` — Meaningful Baseline Selection:** Meaningful baseline is selected.
- **`E09.04.02.F04` — Trend Uncertainty Disclosure:** Trend uncertainty is disclosed, with accessible and accurate supporting evidence.
- **`E09.04.02.F05` — Changed Pattern Detection:** Changed pattern is detected through relevant changes or disruptions, with recovery where required.

#### E09.04.03 — Advisory Evidence & Confidence Presentation
Expose recommendations and interpretations as qualified guidance with evidence and without unapproved decision authority.

*Demand: B02.04, B11.04, B13.06; evidence: ASSURE, INSIGHT, PROV; findings: R15.*

- **`E09.04.03.F01` — AdviceNotAuthoritative:** AI recommendations never write an authoritative business decision without authorised action.
- **`E09.04.03.F02` — ConfidenceAndLimitsShown:** Known uncertainty and model limitations accompany advice.
- **`E09.04.03.F03` — SourceEvidenceAvailable:** Recommendations link to available relevant evidence.
- **`E09.04.03.F04` — HumanDecisionAttributable:** Acceptance/rejection by an authorised decision maker is separate and auditable.
- **`E09.04.03.F05` — AdverseAdviceDetected:** Plausible but unsupported recommendations receive an appropriate qualification.

#### E09.04.04 — Journey Outcome & Integrity Evidence
Correlate stakeholder Journey value stages with separately evidenced business outcomes and IT contributions.

*Demand: B11.04, B12.03, B14.05; evidence: JOURNEY, ASSURE, PROV; findings: R18.*

- **`E09.04.04.F01` — StageValueIndependentlyTested:** Each scoped Journey stage has a value assertion not merely a technical call.
- **`E09.04.04.F02` — ContributionNotDelivery:** An EC Service test is not reported as business-outcome fulfilment.
- **`E09.04.04.F03` — StageExceptionBounded:** Failure and alternative paths carry explicit expected outcomes.
- **`E09.04.04.F04` — EvidenceAcrossStagesCorrelated:** Identity, authority, output and commitment can be traced end to end.
- **`E09.04.04.F05` — UnverifiedStageVisible:** Missing or untested Journey stage cannot be silently marked covered.

### E09.05 — Governed Analytical Access
Ensure analytical access respects information rights, purpose and provenance.

#### E09.05.01 — Purpose-Bound Analytical Access
Analytical information use respects the declared authorised purpose.

- **`E09.05.01.F01` — AnalyticalPurposeConformance:** Analytical access respects purpose and compartment controls.
- **`E09.05.01.F02` — Authorised Analysis Purpose:** Authorised analysis have an explicit purpose, distinguishing permitted and nonconforming conditions.
- **`E09.05.01.F03` — Dataset Eligibility Filtering:** Dataset eligibility is filtered, distinguishing permitted and nonconforming conditions.
- **`E09.05.01.F04` — Purpose Change Enforcement:** Purpose change is enforced through relevant changes or disruptions, with recovery where required.
- **`E09.05.01.F05` — Access Decision Trace:** Access decision is traceable, with accessible and accurate supporting evidence.

#### E09.05.02 — Controlled Analytical Disclosure
Analytical products can be disclosed only to authorised audiences at permitted granularity.

- **`E09.05.02.F01` — CorrectAnalyticalDisclosure:** Analytical disclosures conform to the governing restrictions.
- **`E09.05.02.F02` — Aggregate Disclosure Protection:** Aggregate disclosure is protected, with accessible and accurate supporting evidence.
- **`E09.05.02.F03` — Sensitive Small-Cell Protection:** Sensitive small-cell is protected.
- **`E09.05.02.F04` — Approved Analysis Output:** Approved analysis output is available.
- **`E09.05.02.F05` — Disclosure Audit Evidence:** Disclosure audit is evidenced, with accessible and accurate supporting evidence.

#### E09.05.03 — Advisory Insight Identification
Automatically generated observations or recommendations remain explicitly advisory where required.

- **`E09.05.03.F01` — IdentifiedAdvisoryOutput:** Applicable automated outputs are identified and handled as non-authoritative advice.
- **`E09.05.03.F02` — Explicit Advisory Label:** Explicit advisory has an explicit label.
- **`E09.05.03.F03` — No Autonomous Authority:** Automated recommendations do not confer autonomous decision authority, distinguishing permitted and nonconforming conditions.
- **`E09.05.03.F04` — Recommendation Provenance:** Recommendation retains its provenance, with accessible and accurate supporting evidence.
- **`E09.05.03.F05` — Human Review Visibility:** Human review is visible, with accessible and accurate supporting evidence.

#### E09.05.04 — Research Purpose & Data Release Controls
Enforce approved research purpose, participant protections and evidence access without assuming a whole research solution is in Harmonia.

*Demand: B13.04, B13.05, B10.03; evidence: TH, PROV, INSIGHT; findings: R23, R12.*

- **`E09.05.04.F01` — ApprovedPurposeVerified:** Research information retrieval checks permitted purpose and applicable grant.
- **`E09.05.04.F02` — ParticipantRestrictionsHonoured:** Consent, restrictions and representation conditions are applied.
- **`E09.05.04.F03` — DataSnapshotPinned:** Study dataset references preserve a reproducible authorised version.
- **`E09.05.04.F04` — ExtractAllOrNothingWhereRequired:** High-risk export checks reject unauthorised partial release.
- **`E09.05.04.F05` — ResearchScopeQualified:** Capabilities are labelled contribution/optional where a full research runtime is not committed.

