# Generate Provider Data Integrity Reports

## Purpose

Enable the target-state Provider Directory to support the capability described by SR-51 so that I can verify that the directory is maintained according to compliance requirements. The capability should operate as part of the governed Provider Directory service, with consistent identity, provenance, lifecycle, security, data-quality and audit controls applied irrespective of whether the activity is performed manually, through an API, or through system integration.

## Source Traceability

- **Source Use Case:** `SR-51`
- **Source Scope:** Additional Source Requirements – SR-26 to SR-58
- **Primary Actor:** Compliance Officer
- **Supporting Actors:** Provider Directory Administrator; Data Steward; Provider Directory
- **User Story:** As a Compliance Officer, I want to generate data integrity reports, so that I can verify that the directory is maintained according to compliance requirements.
- **Related Use Cases:** UC-Integration-ProviderDirectory-32 – Validate Provider Data Quality: supports validation, data-quality and remediation controls.

## Requirements

### Requirement: Generate Provider Data Integrity Reports
Harmonia SHALL support **Generate Provider Data Integrity Reports** so that Enable the target-state Provider Directory to support the capability described by SR-51 so that I can verify that the directory is maintained according to compliance requirements. The capability should operate as part of the governed Provider Directory service, with consistent identity, provenance, lifecycle, security, data-quality and audit controls applied irrespective of whether the activity is performed manually, through an API, or through system integration.

#### Scenario: Generate Provider Data Integrity Reports - successful outcome
- **GIVEN** The initiating actor is appropriately authenticated and authorised to perform the activity.
- **GIVEN** The relevant provider record, source or consumer system, interface, reference data and supporting configuration are available.
- **GIVEN** Applicable data definitions, validation rules, security controls, source-of-truth rules and lifecycle rules have been established.
- **GIVEN** Provider identity has been established or sufficient approved identifiers and matching attributes are available to resolve identity where required.
- **GIVEN** Monitoring, audit and exception-handling arrangements have been established for the activity.
- **WHEN** The Compliance Officer initiates the activity, a relevant provider or reference-data change occurs, or an authorised scheduled/event-driven process triggers the requirement described by SR-51.
- **THEN** 1. The Compliance Officer initiates or receives the event associated with “Generate Provider Data Integrity Reports”.
- **THEN** 2. The Provider Directory identifies the relevant provider, data set, integration endpoint or downstream consumer required by the activity.
- **THEN** 3. The solution validates the request and information against applicable structural, mandatory-field, reference-data, security and business rules.
- **THEN** 4. Where provider identity is involved, the solution resolves the provider using recognised identifiers and approved matching attributes before making changes or distributing information.
- **THEN** 5. The solution applies configured lifecycle, source-authority, publication, masking, distribution and access rules relevant to the activity.
- **THEN** 6. The requested action is performed and the result is recorded with source, date/time, method, processing status and sufficient detail for audit and reconciliation.
- **THEN** 7. Any required notification, report, downstream update or monitoring event is generated.
- **THEN** 8. The resulting information is made available only to authorised users, processes or consumers.
- **THEN** The requirement described by SR-51 is completed successfully so that I can verify that the directory is maintained according to compliance requirements. The Provider Directory maintains an auditable and governed record of the activity and any exceptions are clearly identified for follow-up.

### Requirement: Actor access and participation
Harmonia SHALL restrict participation in this capability to authorised actors and SHALL preserve the distinction between primary and supporting actor responsibilities.

#### Scenario: Authorised actor participates
- **GIVEN** the primary actor is Compliance Officer
- **AND** supporting participation may be provided by Provider Directory Administrator; Data Steward; Provider Directory
- **WHEN** the capability is invoked
- **THEN** Harmonia permits only authorised actions appropriate to the actor role

### Requirement: Exceptions and alternate outcomes
Harmonia SHALL handle validation, authorisation, conflict, approval and other exception conditions without silently completing an invalid business outcome.

#### Scenario: Exception 1
- **WHEN** Required information is missing, malformed or invalid – The request or transaction is rejected, quarantined or referred for correction.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 2
- **WHEN** Provider identity cannot be uniquely resolved – Processing is held for reconciliation and no uncontrolled duplicate is created.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 3
- **WHEN** A value is not recognised or fails reference-data validation – The exception is logged and routed for review according to configured rules.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 4
- **WHEN** The source or actor is not authorised to update, expose or distribute the requested information – The restricted action is prevented and recorded.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 5
- **WHEN** The provider is inactive, suspended, expired, cancelled or otherwise restricted – Lifecycle and distribution rules determine whether processing can continue.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 6
- **WHEN** An integration, API, export or messaging operation fails – The failure is logged, monitored and made available for retry or operational remediation.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 7
- **WHEN** A manual override is permitted – The override is explicitly authorised, justified and retained in the audit history.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 8
- **WHEN** A subsequent correction or authoritative update is received – The new information is applied in accordance with precedence rules without removing prior history.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 9
- **WHEN** Data-integrity reports can be scheduled or run on demand.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 10
- **WHEN** Reports can be exported in common formats such as XML or CSV.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 11
- **WHEN** Historical reports can be generated for a specified date range.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 12
- **WHEN** Information provenance should identify the source and update mechanism at the level required to support audit, compliance and source-of-truth decisions.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 13
- **WHEN** Where multiple sources can update a provider or reference-data attribute, authority and precedence rules should be explicitly configurable and consistently enforced.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 14
- **WHEN** Provider status, verification state and distribution eligibility should be managed as distinct concepts where required by the business process.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 15
- **WHEN** All material changes, manual overrides, exports, API activity and integration outcomes should retain sufficient history for investigation and compliance reporting.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 16
- **WHEN** Sensitive or restricted information should only be exposed to authorised consumers and should be masked, excluded or protected where policy requires.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 17
- **WHEN** UC-Integration-ProviderDirectory-32 – Validate Provider Data Quality: supports validation, data-quality and remediation controls.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

### Requirement: Business and data controls
Harmonia SHALL apply the business and data controls required by this capability.

#### Scenario: Business or data control 1
- **GIVEN** Data-integrity reports can be scheduled or run on demand.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 2
- **GIVEN** Reports can be exported in common formats such as XML or CSV.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 3
- **GIVEN** Historical reports can be generated for a specified date range.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 4
- **GIVEN** Information provenance should identify the source and update mechanism at the level required to support audit, compliance and source-of-truth decisions.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 5
- **GIVEN** Where multiple sources can update a provider or reference-data attribute, authority and precedence rules should be explicitly configurable and consistently enforced.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 6
- **GIVEN** Provider status, verification state and distribution eligibility should be managed as distinct concepts where required by the business process.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 7
- **GIVEN** All material changes, manual overrides, exports, API activity and integration outcomes should retain sufficient history for investigation and compliance reporting.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 8
- **GIVEN** Sensitive or restricted information should only be exposed to authorised consumers and should be masked, excluded or protected where policy requires.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

## Implementation Boundary

This use case describes a business and information-management requirement and is independent of the technology selected to implement the Provider Directory. The same requirement applies whether the Provider Directory is implemented using a PAS, bespoke database, FHIR Server, Medical-Objects, commercial product, Epic EMR, ADHA LDS or another technology. The selected technology determines how the requirement is implemented, secured, monitored and integrated—not whether the underlying business requirement exists.

## Acceptance Boundary

This specification defines observable Harmonia behaviour derived from the source use case. Internal classes, libraries, persistence mechanisms and deployment choices are implementation decisions unless explicitly constrained by a requirement above.
