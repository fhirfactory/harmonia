# View Consent History

## Purpose

I understand previous authorisations and changes

## Source Traceability

- **Source Use Case:** `UC-Clinician-Consent-11`
- **Source Scope:** Scope - Consent – Internal Clinicians
- **Primary Actor:** Clinician (Treating Clinician)
- **Supporting Actors:** Provider Directory
- **User Story:** Review the history of consent decisions so that I understand previous authorisations and changes.
- **Related Use Cases:** None identified.

## Requirements

### Requirement: View Consent History
Harmonia SHALL support **View Consent History** so that I understand previous authorisations and changes.

#### Scenario: View Consent History - successful outcome
- **GIVEN** The actor is appropriately identified and authorised to perform the activity; the relevant provider, participant, organisation or reference information is available to the process; and any prerequisite records or relationships exist.
- **WHEN** The actor needs to perform the 'View Consent History' activity, or an authorised business process initiates the activity.
- **THEN** Clinician (Treating Clinician) initiates the 'View Consent History' activity.
- **THEN** The solution presents the information and/or controls required to perform the activity described by the user story.
- **THEN** Clinician (Treating Clinician) enters, selects, reviews or confirms the relevant information.
- **THEN** The solution validates the information and applies applicable business, security and data-quality controls.
- **THEN** The solution records the resulting state and makes the outcome available to authorised downstream users or processes.
- **THEN** The 'View Consent History' activity is completed successfully, or a clear validation, approval, exception or escalation outcome is recorded.

### Requirement: Actor access and participation
Harmonia SHALL restrict participation in this capability to authorised actors and SHALL preserve the distinction between primary and supporting actor responsibilities.

#### Scenario: Authorised actor participates
- **GIVEN** the primary actor is Clinician (Treating Clinician)
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
