# Import External Terminology Content

<!-- harmonia-1.1.21-section-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by:
- [UseCase canonical definition](../../../../enterprise-architecture/continuum/motivation/requirement/use-case/0dc39a7a92574e64b737a03e35285e65.md) — Purpose.
Only the incorporated architectural meaning is superseded; source-specific scenarios and remaining sections retain their qualifications. Incorporated result remains pending 1.1.21 review.
---------------------------------------------------------

## Purpose

recognised standards can be used within departmental systems


------- Legacy Content - Superseded ------- Finish ----
<!-- harmonia-1.1.21-section-supersession:end -->

## Source Traceability

- **Source Use Case:** `UC-DepartmentalOfficer-Terminology-2`
- **Source Scope:** Scope - Terminology Management
- **Primary Actor:** Reference Data / Terminology Administrator
- **Supporting Actors:** Provider Directory
<!-- harmonia-1.1.21-section-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by:
- [UserStory canonical definition](../../../../enterprise-architecture/continuum/motivation/requirement/user-story/88e30ac516be4d9abc770930fccb7060.md) — Source Traceability / User Story.
Only the incorporated architectural meaning is superseded; source-specific scenarios and remaining sections retain their qualifications. Incorporated result remains pending 1.1.21 review.
---------------------------------------------------------

- **User Story:** Import approved external terminology content so that recognised standards can be used within departmental systems.

------- Legacy Content - Superseded ------- Finish ----
<!-- harmonia-1.1.21-section-supersession:end -->

- **Related Use Cases:** None identified.

## Requirements

<!-- harmonia-1.1.21-section-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by:
- [UseCase canonical definition](../../../../enterprise-architecture/continuum/motivation/requirement/use-case/0dc39a7a92574e64b737a03e35285e65.md) — Requirements / primary interaction statement.
Only the incorporated architectural meaning is superseded; source-specific scenarios and remaining sections retain their qualifications. Incorporated result remains pending 1.1.21 review.
---------------------------------------------------------

### Requirement: Import External Terminology Content
Harmonia SHALL support **Import External Terminology Content** so that recognised standards can be used within departmental systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- harmonia-1.1.21-section-supersession:end -->

#### Scenario: Import External Terminology Content - successful outcome
- **GIVEN** The actor is appropriately identified and authorised to perform the activity; the relevant provider, participant, organisation or reference information is available to the process; and any prerequisite records or relationships exist.
- **WHEN** The actor needs to perform the 'Import External Terminology Content' activity, or an authorised business process initiates the activity.
- **THEN** Reference Data / Terminology Administrator initiates the 'Import External Terminology Content' activity.
- **THEN** The solution presents the information and/or controls required to perform the activity described by the user story.
- **THEN** Reference Data / Terminology Administrator enters, selects, reviews or confirms the relevant information.
- **THEN** The solution validates the information and applies applicable business, security and data-quality controls.
- **THEN** The solution records the resulting state and makes the outcome available to authorised downstream users or processes.
- **THEN** The 'Import External Terminology Content' activity is completed successfully, or a clear validation, approval, exception or escalation outcome is recorded.

### Requirement: Actor access and participation
Harmonia SHALL restrict participation in this capability to authorised actors and SHALL preserve the distinction between primary and supporting actor responsibilities.

#### Scenario: Authorised actor participates
- **GIVEN** the primary actor is Reference Data / Terminology Administrator
- **AND** supporting participation may be provided by Provider Directory
- **WHEN** the capability is invoked
- **THEN** Harmonia permits only authorised actions appropriate to the actor role

### Requirement: Exceptions and alternate outcomes
Harmonia SHALL handle validation, authorisation, conflict, approval and other exception conditions without silently completing an invalid business outcome.

#### Scenario: Exception 1
- **WHEN** Required information is missing or incomplete and the activity cannot be completed until it is supplied.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 2
- **WHEN** The information fails validation or business rules and the actor is prompted to correct it.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 3
- **WHEN** The actor does not have the required authority, role or access and the activity is denied or referred for approval.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 4
- **WHEN** The requested change requires verification, approval or reconciliation before it can become effective.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 5
- **WHEN** A duplicate, conflicting or otherwise authoritative record is identified and the activity is routed to the appropriate resolution process.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

### Requirement: Business and data controls
Harmonia SHALL apply the business and data controls required by this capability.

#### Scenario: Business or data control 1
- **GIVEN** The activity should maintain data integrity, preserve the source and status of information where relevant, distinguish authoritative information from user-entered information, and retain sufficient history or evidence to support governance and audit requirements. Where the activity involves publication or exchange, only information that is authorised for the relevant audience should be made available.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

## Implementation Boundary

This is a business capability/use-case requirement rather than a requirement for a particular technology product. The same business outcome may be implemented through different applications, services, databases, APIs or manual processes. Technology options should therefore be assessed on how effectively they enable and govern the business process.

## Acceptance Boundary

This specification defines observable Harmonia behaviour derived from the source use case. Internal classes, libraries, persistence mechanisms and deployment choices are implementation decisions unless explicitly constrained by a requirement above.
