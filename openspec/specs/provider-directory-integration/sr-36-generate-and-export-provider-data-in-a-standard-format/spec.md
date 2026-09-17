# Generate and Export Provider Data in a Standard Format

## Purpose

Enable the target-state Provider Directory to support the capability described by SR-36 so that I can manually share it with systems that are not integrated. The capability should operate as part of the governed Provider Directory service, with consistent identity, provenance, lifecycle, security, data-quality and audit controls applied irrespective of whether the activity is performed manually, through an API, or through system integration.

## Source Traceability

- **Source Use Case:** `SR-36`
- **Source Scope:** Additional Source Requirements – SR-26 to SR-58
- **Primary Actor:** Provider Directory Administrator
- **Supporting Actors:** System Administrator; Compliance Officer; Provider Directory; Integration Service
- **User Story:** As a Provider Directory Administrator, I want to generate and export provider data in a standard format, so that I can manually share it with systems that are not integrated.
- **Related Use Cases:** UC-Integration-ProviderDirectory-35 – Monitor Provider Integration Interfaces: supports operational monitoring of Provider Directory interfaces.; UC-Integration-ProviderDirectory-36 – Reconcile Provider Synchronisation Failures: supports remediation of failed or incomplete synchronisation.

## Requirements

### Requirement: Generate and Export Provider Data in a Standard F
Harmonia SHALL support **Generate and Export Provider Data in a Standard Format** so that Enable the target-state Provider Directory to support the capability described by SR-36 so that I can manually share it with systems that are not integrated. The capability should operate as part of the governed Provider Directory service, with consistent identity, provenance, lifecycle, security, data-quality and audit controls applied irrespective of whether the activity is performed manually, through an API, or through system integration.

#### Scenario: Generate and Export Provider Data in a Standard Format - successful outcome
- **GIVEN** The initiating actor is appropriately authenticated and authorised to perform the activity.
- **GIVEN** The relevant provider record, source or consumer system, interface, reference data and supporting configuration are available.
- **GIVEN** Applicable data definitions, validation rules, security controls, source-of-truth rules and lifecycle rules have been established.
- **GIVEN** Provider identity has been established or sufficient approved identifiers and matching attributes are available to resolve identity where required.
- **GIVEN** Monitoring, audit and exception-handling arrangements have been established for the activity.
- **WHEN** The Provider Directory Administrator initiates the activity, a relevant provider or reference-data change occurs, or an authorised scheduled/event-driven process triggers the requirement described by SR-36.
- **THEN** 1. The Provider Directory Administrator initiates or receives the event associated with “Generate and Export Provider Data in a Standard Format”.
- **THEN** 2. The Provider Directory identifies the relevant provider, data set, integration endpoint or downstream consumer required by the activity.
- **THEN** 3. The solution validates the request and information against applicable structural, mandatory-field, reference-data, security and business rules.
- **THEN** 4. Where provider identity is involved, the solution resolves the provider using recognised identifiers and approved matching attributes before making changes or distributing information.
- **THEN** 5. The solution applies configured lifecycle, source-authority, publication, masking, distribution and access rules relevant to the activity.
- **THEN** 6. The requested action is performed and the result is recorded with source, date/time, method, processing status and sufficient detail for audit and reconciliation.
- **THEN** 7. Any required notification, report, downstream update or monitoring event is generated.
- **THEN** 8. The resulting information is made available only to authorised users, processes or consumers.
- **THEN** The requirement described by SR-36 is completed successfully so that I can manually share it with systems that are not integrated. The Provider Directory maintains an auditable and governed record of the activity and any exceptions are clearly identified for follow-up.

### Requirement: Actor access and participation
Harmonia SHALL restrict participation in this capability to authorised actors and SHALL preserve the distinction between primary and supporting actor responsibilities.

#### Scenario: Authorised actor participates
- **GIVEN** the primary actor is Provider Directory Administrator
- **AND** supporting participation may be provided by System Administrator; Compliance Officer; Provider Directory; Integration Service
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
- **WHEN** Provider data can be exported in common formats such as XML or CSV.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 10
- **WHEN** Specific fields and filters can be selected before export.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 11
- **WHEN** Exports include useful metadata such as data source, generation date and last-updated information.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 12
- **WHEN** Reusable templates can define export fields and format.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 13
- **WHEN** Export templates can be saved and reused.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 14
- **WHEN** Information provenance should identify the source and update mechanism at the level required to support audit, compliance and source-of-truth decisions.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 15
- **WHEN** Where multiple sources can update a provider or reference-data attribute, authority and precedence rules should be explicitly configurable and consistently enforced.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 16
- **WHEN** Provider status, verification state and distribution eligibility should be managed as distinct concepts where required by the business process.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 17
- **WHEN** All material changes, manual overrides, exports, API activity and integration outcomes should retain sufficient history for investigation and compliance reporting.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 18
- **WHEN** Sensitive or restricted information should only be exposed to authorised consumers and should be masked, excluded or protected where policy requires.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 19
- **WHEN** UC-Integration-ProviderDirectory-35 – Monitor Provider Integration Interfaces: supports operational monitoring of Provider Directory interfaces.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 20
- **WHEN** UC-Integration-ProviderDirectory-36 – Reconcile Provider Synchronisation Failures: supports remediation of failed or incomplete synchronisation.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

### Requirement: Business and data controls
Harmonia SHALL apply the business and data controls required by this capability.

#### Scenario: Business or data control 1
- **GIVEN** Provider data can be exported in common formats such as XML or CSV.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 2
- **GIVEN** Specific fields and filters can be selected before export.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 3
- **GIVEN** Exports include useful metadata such as data source, generation date and last-updated information.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 4
- **GIVEN** Reusable templates can define export fields and format.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 5
- **GIVEN** Export templates can be saved and reused.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 6
- **GIVEN** Information provenance should identify the source and update mechanism at the level required to support audit, compliance and source-of-truth decisions.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 7
- **GIVEN** Where multiple sources can update a provider or reference-data attribute, authority and precedence rules should be explicitly configurable and consistently enforced.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 8
- **GIVEN** Provider status, verification state and distribution eligibility should be managed as distinct concepts where required by the business process.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 9
- **GIVEN** All material changes, manual overrides, exports, API activity and integration outcomes should retain sufficient history for investigation and compliance reporting.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 10
- **GIVEN** Sensitive or restricted information should only be exposed to authorised consumers and should be masked, excluded or protected where policy requires.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

## Implementation Boundary

This use case describes a business and information-management requirement and is independent of the technology selected to implement the Provider Directory. The same requirement applies whether the Provider Directory is implemented using a PAS, bespoke database, FHIR Server, Medical-Objects, commercial product, Epic EMR, ADHA LDS or another technology. The selected technology determines how the requirement is implemented, secured, monitored and integrated—not whether the underlying business requirement exists.

## Acceptance Boundary

This specification defines observable Harmonia behaviour derived from the source use case. Internal classes, libraries, persistence mechanisms and deployment choices are implementation decisions unless explicitly constrained by a requirement above.
