# Reject Updates to Restricted Attributes

## Purpose

Enable the Provider Directory to enforce attribute restrictions while allowing permitted fields in the same transaction to be processed. This supports the target-state objective that sr-15 is implemented as a governed business capability rather than as an isolated technical function. The intended outcome is that critical information remains controlled.

## Source Traceability

- **Source Use Case:** `SR-15`
- **Source Scope:** Additional Source Use Cases and System Requirements
- **Primary Actor:** Integration Administrator
- **Supporting Actors:** Compliance Officer; Provider Directory; Data Steward
- **User Story:** As an Integration Administrator, I want the system to reject an update to a restricted attribute, so that critical information remains controlled.
- **Related Use Cases:** UC-Integration-ProviderDirectory-30 – Match Provider Records: supports reliable identity matching where provider information is received or reconciled.; UC-Integration-ProviderDirectory-32 – Validate Provider Data Quality: supports validation and data-quality controls.; UC-Integration-ProviderDirectory-34 – Audit Provider Information Exchange: supports traceability and audit of information exchange.; UC-Integration-ProviderDirectory-35 – Monitor Provider Integration Interfaces: supports operational monitoring of automated interfaces.; UC-Integration-ProviderDirectory-36 – Reconcile Provider Synchronisation Failures: supports remediation of failed or ambiguous synchronisation.

## Requirements

### Requirement: Reject Updates to Restricted Attributes
Harmonia SHALL support **Reject Updates to Restricted Attributes** so that Enable the Provider Directory to enforce attribute restrictions while allowing permitted fields in the same transaction to be processed. This supports the target-state objective that sr-15 is implemented as a governed business capability rather than as an isolated technical function. The intended outcome is that critical information remains controlled.

#### Scenario: Reject Updates to Restricted Attributes - successful outcome
- **GIVEN** The actor is appropriately authenticated and authorised to perform the activity.
- **GIVEN** The relevant provider, source-system, consumer-system or reference information required by the process is available.
- **GIVEN** Applicable interface, validation, matching, governance and data-authority rules have been defined.
- **GIVEN** Provider identifiers and other matching attributes are available where identity resolution is required.
- **GIVEN** Security, privacy, audit and information-sharing requirements applicable to the activity have been established.
- **WHEN** The Integration Administrator initiates the 'Reject Updates to Restricted Attributes' activity, a relevant provider-data event occurs, or an authorised scheduled process initiates the activity.
- **THEN** 1. The Integration Administrator initiates or causes the 'Reject Updates to Restricted Attributes' activity.
- **THEN** 2. The Provider Directory receives, retrieves, presents or prepares the information required to perform the activity.
- **THEN** 3. The solution validates the information against applicable structural, mandatory-field, reference-data, security and business rules.
- **THEN** 4. Where provider identity is relevant, the solution resolves the provider using recognised identifiers and other approved matching attributes before applying the transaction.
- **THEN** 5. The solution evaluates applicable source-of-truth, attribute-authority, lifecycle, publication and distribution rules.
- **THEN** 6. The Provider Directory performs the required action to enforce attribute restrictions while allowing permitted fields in the same transaction to be processed.
- **THEN** 7. The solution records the source, processing status, date/time, method and outcome, together with sufficient detail to support audit and reconciliation.
- **THEN** 8. The resulting information or outcome is made available only to authorised users, processes or downstream consumers.
- **THEN** The 'Reject Updates to Restricted Attributes' activity is completed successfully and enforce attribute restrictions while allowing permitted fields in the same transaction to be processed, while maintaining provider identity, provenance, data quality, governance and auditability. Where the activity cannot be completed automatically, a clear validation, exception, reconciliation or escalation outcome is recorded.

### Requirement: Actor access and participation
Harmonia SHALL restrict participation in this capability to authorised actors and SHALL preserve the distinction between primary and supporting actor responsibilities.

#### Scenario: Authorised actor participates
- **GIVEN** the primary actor is Integration Administrator
- **AND** supporting participation may be provided by Compliance Officer; Provider Directory; Data Steward
- **WHEN** the capability is invoked
- **THEN** Harmonia permits only authorised actions appropriate to the actor role

### Requirement: Exceptions and alternate outcomes
Harmonia SHALL handle validation, authorisation, conflict, approval and other exception conditions without silently completing an invalid business outcome.

#### Scenario: Exception 1
- **WHEN** Required information is missing or invalid – The transaction is rejected, quarantined or referred for correction.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 2
- **WHEN** Provider identity cannot be uniquely resolved – The activity is held for reconciliation and no uncontrolled duplicate is created.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 3
- **WHEN** Multiple potential provider matches are identified – The transaction is referred for identity resolution or Data Steward review.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 4
- **WHEN** The source is not authorised to update one or more attributes – Restricted changes are not applied; permitted changes may continue where business rules allow.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 5
- **WHEN** Conflicting information is received – Defined source-of-truth and precedence rules determine the outcome, with unresolved conflicts referred for review.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 6
- **WHEN** The actor or consuming system is not authorised – Access, update, publication or distribution is denied and the attempt is auditable.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 7
- **WHEN** Processing or integration fails – The failure is logged with sufficient information for retry, reconciliation and operational monitoring.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 8
- **WHEN** A subsequent correction is received – The corrected information is processed without removing the history and provenance of the earlier transaction.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 9
- **WHEN** Provider identity should be resolved before information is applied to an existing provider record.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 10
- **WHEN** Authority should be established at the appropriate source, information-domain or individual-attribute level rather than assuming that one system is authoritative for the entire provider record.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 11
- **WHEN** Provenance should identify the source and update method for information received, created or changed.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 12
- **WHEN** Locally authoritative information should not be overwritten by a source that is not authorised for that information.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 13
- **WHEN** The solution should retain sufficient history to establish what changed, when it changed, the source or user responsible, and the processing outcome.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 14
- **WHEN** Provider lifecycle status, verification state and publication/distribution rules should be enforced independently of the mechanism used to store the record.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 15
- **WHEN** Reference data, identifiers and controlled values should be governed consistently across manual and automated processes.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 16
- **WHEN** Exceptions, failed transactions and ambiguous matches should be visible to authorised operational or data-governance roles for remediation.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 17
- **WHEN** UC-Integration-ProviderDirectory-30 – Match Provider Records: supports reliable identity matching where provider information is received or reconciled.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 18
- **WHEN** UC-Integration-ProviderDirectory-32 – Validate Provider Data Quality: supports validation and data-quality controls.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 19
- **WHEN** UC-Integration-ProviderDirectory-34 – Audit Provider Information Exchange: supports traceability and audit of information exchange.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 20
- **WHEN** UC-Integration-ProviderDirectory-35 – Monitor Provider Integration Interfaces: supports operational monitoring of automated interfaces.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 21
- **WHEN** UC-Integration-ProviderDirectory-36 – Reconcile Provider Synchronisation Failures: supports remediation of failed or ambiguous synchronisation.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

### Requirement: Business and data controls
Harmonia SHALL apply the business and data controls required by this capability.

#### Scenario: Business or data control 1
- **GIVEN** Provider identity should be resolved before information is applied to an existing provider record.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 2
- **GIVEN** Authority should be established at the appropriate source, information-domain or individual-attribute level rather than assuming that one system is authoritative for the entire provider record.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 3
- **GIVEN** Provenance should identify the source and update method for information received, created or changed.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 4
- **GIVEN** Locally authoritative information should not be overwritten by a source that is not authorised for that information.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 5
- **GIVEN** The solution should retain sufficient history to establish what changed, when it changed, the source or user responsible, and the processing outcome.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 6
- **GIVEN** Provider lifecycle status, verification state and publication/distribution rules should be enforced independently of the mechanism used to store the record.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 7
- **GIVEN** Reference data, identifiers and controlled values should be governed consistently across manual and automated processes.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 8
- **GIVEN** Exceptions, failed transactions and ambiguous matches should be visible to authorised operational or data-governance roles for remediation.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

## Implementation Boundary

This use case describes a business and information-management requirement and is independent of the technology selected to implement the Provider Directory. The same requirement applies whether the Provider Directory is implemented using a PAS, bespoke database, FHIR Server, Medical-Objects, commercial product, Epic EMR, ADHA LDS or another technology. The selected technology determines how the requirement is implemented, controlled and integrated—not whether the underlying business requirement exists.

## Acceptance Boundary

This specification defines observable Harmonia behaviour derived from the source use case. Internal classes, libraries, persistence mechanisms and deployment choices are implementation decisions unless explicitly constrained by a requirement above.
