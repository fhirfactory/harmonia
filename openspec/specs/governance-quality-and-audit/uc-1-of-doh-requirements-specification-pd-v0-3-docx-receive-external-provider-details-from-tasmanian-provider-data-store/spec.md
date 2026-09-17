# Receive External Provider Details from Tasmanian Provider Data Store

## Purpose

Enable provider information maintained within the Tasmanian Provider Data Store to be received and incorporated into the Provider Directory through controlled system integration. This supports the establishment of a consolidated view of providers without requiring provider information originating from an external authoritative or reference source to be manually re-entered. The capability should support both the creation of previously unknown providers and the update of provider information already represented within the Provider Directory.

## Source Traceability

- **Source Use Case:** `UC-1 of DoH_Requirements_Specification_PD v0.3.docx.`
- **Source Scope:** Scope - Governance, Quality and Audit
- **Primary Actor:** Tasmanian Provider Data Store
- **Supporting Actors:** Provider Directory; Integration Service; Provider Identity / Matching Service; Data Steward
- **User Story:** As the Provider Directory, I need to receive provider details from the Tasmanian Provider Data Store, so that externally sourced provider information can be incorporated into the enterprise Provider Directory and made available to authorised downstream consumers.
- **Related Use Cases:** This use case has relationships with several of the existing Provider Directory integration use cases:; UC-Integration-ProviderDirectory-1 – Receive New Clinician Registration: Applies where the incoming provider is not already represented in the Provider Directory.; UC-Integration-ProviderDirectory-5 – Receive Clinician Profile Updates: Applies to subsequent changes to externally sourced provider profile information.; UC-Integration-ProviderDirectory-30 – Match Provider Records: Supports identification of the Provider Directory record corresponding to the incoming provider.; UC-Integration-ProviderDirectory-31 – Receive Provider Identifier Cross References: Supports reconciliation of identifiers used by the external source and the Provider Directory.; UC-Integration-ProviderDirectory-32 – Validate Provider Data Quality: Supports validation of incoming provider information before it is accepted.; UC-Integration-ProviderDirectory-35 – Monitor Provider Integration Interfaces: Provides operational monitoring of the integration.; UC-Integration-ProviderDirectory-36 – Reconcile Provider Synchronisation Failures: Handles transactions that cannot be successfully processed automatically.; Appendix A – Source Use Case Coverage; The source workbook contains 350 use cases/user stories across 57 groupings. All source rows were carried forward into this document in their original order and with their original identifiers.; Appendix B – Interpretation Notes; The source workbook contains several domains that extend beyond the Provider Directory-specific use cases, including participant identity, consent, demographics, terminology, reference data and enterprise identity capabilities. These have been retained rather than removed.; Where a source user story is abbreviated or grammatically incomplete, the substantive wording has been preserved and the derived workflow language has been kept technology-neutral.; The expanded scenarios intentionally avoid prescribing a particular product, database, FHIR implementation, application or integration technology.; The document can be used as the functional baseline for mapping the use cases to business capabilities, information requirements, application services and technology options.

## Requirements

### Requirement: Receive External Provider Details from Tasmanian
Harmonia SHALL support **Receive External Provider Details from Tasmanian Provider Data Store** so that Enable provider information maintained within the Tasmanian Provider Data Store to be received and incorporated into the Provider Directory through controlled system integration. This supports the establishment of a consolidated view of providers without requiring provider information originating from an external authoritative or reference source to be manually re-entered. The capability should support both the creation of previously unknown providers and the update of provider information already represented within the Provider Directory.

#### Scenario: Receive External Provider Details from Tasmanian Provider Data Store - successful outcome
- **GIVEN** An approved integration exists between the Tasmanian Provider Data Store and the Provider Directory.
- **GIVEN** The Provider Directory is authorised to receive and process the relevant provider information.
- **GIVEN** Provider information supplied by the source conforms to an agreed information structure or can be transformed into the Provider Directory information model.
- **GIVEN** Appropriate provider identifiers or other matching attributes are available to support identity resolution.
- **GIVEN** Rules defining the authority of the Tasmanian Provider Data Store for individual data elements have been established.
- **WHEN** The Tasmanian Provider Data Store creates or changes provider information that is required by the Provider Directory, or a scheduled/requested synchronisation of provider information occurs.
- **THEN** The Tasmanian Provider Data Store makes provider information available for synchronisation.
- **THEN** The integration service receives the provider information and validates that the transaction is structurally complete and conforms to the agreed interface.
- **THEN** The Provider Directory identifies the provider using available identifiers and identity attributes.
- **THEN** Where an existing provider is identified, the incoming information is matched against the existing Provider Directory record.
- **THEN** Where no existing provider can be identified, the information is processed in accordance with the rules for establishing a new provider.
- **THEN** The Provider Directory determines which incoming data elements may be accepted based on the defined source-of-truth and data-authority rules.
- **THEN** Provider information is created or updated as appropriate.
- **THEN** Existing locally authoritative information is retained where the Tasmanian Provider Data Store is not authoritative for that information.
- **THEN** The source, date/time and outcome of the update are recorded for audit and provenance purposes.
- **THEN** Updated provider information is made available to authorised Provider Directory consumers in accordance with applicable verification, publication and visibility rules.
- **THEN** Provider information supplied by the Tasmanian Provider Data Store is reliably received, matched, validated and incorporated into the Provider Directory while maintaining provider identity, provenance, data quality and defined information ownership. The Provider Directory consequently provides consumers with a more complete and current representation of provider information without unnecessarily duplicating manual data-maintenance processes.

### Requirement: Actor access and participation
Harmonia SHALL restrict participation in this capability to authorised actors and SHALL preserve the distinction between primary and supporting actor responsibilities.

#### Scenario: Authorised actor participates
- **GIVEN** the primary actor is Tasmanian Provider Data Store
- **AND** supporting participation may be provided by Provider Directory; Integration Service; Provider Identity / Matching Service; Data Steward
- **WHEN** the capability is invoked
- **THEN** Harmonia permits only authorised actions appropriate to the actor role

### Requirement: Exceptions and alternate outcomes
Harmonia SHALL handle validation, authorisation, conflict, approval and other exception conditions without silently completing an invalid business outcome.

#### Scenario: Exception 1
- **WHEN** Provider cannot be uniquely matched – The incoming record is held for reconciliation rather than automatically creating or updating a provider.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 2
- **WHEN** Multiple potential matches are identified – The transaction is referred for identity resolution or Data Steward review.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 3
- **WHEN** Mandatory information is missing or invalid – The transaction is rejected, quarantined or flagged for remediation.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 4
- **WHEN** Conflicting information is received – Source-of-truth rules determine whether the incoming or existing value is retained; unresolved conflicts are referred for review.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 5
- **WHEN** Provider is new to the Provider Directory – A new provider record is created subject to the applicable registration and verification rules.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 6
- **WHEN** Provider is inactive or retired – The incoming information is processed according to provider lifecycle and reactivation rules rather than automatically changing provider status.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 7
- **WHEN** Integration failure occurs – The transaction is logged and made available for retry, reconciliation and operational monitoring.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 8
- **WHEN** Source information is subsequently corrected – The corrected information is processed as a subsequent synchronisation transaction without removing the history of the earlier change.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 9
- **WHEN** The Tasmanian Provider Data Store should not automatically be assumed to be authoritative for every provider attribute. Authority should be established at the data-element or information-domain level.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 10
- **WHEN** Provider identity must be resolved before incoming information is applied to an existing provider record.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 11
- **WHEN** Provenance should identify the Tasmanian Provider Data Store as the source of externally supplied information.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 12
- **WHEN** Locally managed information—such as local roles, affiliations, credentialing decisions, visibility settings or other organisation-specific attributes—should not be overwritten unless explicitly governed by the integration.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 13
- **WHEN** Synchronisation should preserve sufficient history to determine what information was received, when it was received and what changes were subsequently made.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 14
- **WHEN** Provider information should only become visible to downstream consumers where applicable verification, governance and publication requirements have been satisfied.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 15
- **WHEN** The integration should support appropriate monitoring, reconciliation and management of failed or incomplete transactions.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 16
- **WHEN** UC-Integration-ProviderDirectory-1 – Receive New Clinician Registration: Applies where the incoming provider is not already represented in the Provider Directory.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 17
- **WHEN** UC-Integration-ProviderDirectory-5 – Receive Clinician Profile Updates: Applies to subsequent changes to externally sourced provider profile information.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 18
- **WHEN** UC-Integration-ProviderDirectory-30 – Match Provider Records: Supports identification of the Provider Directory record corresponding to the incoming provider.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 19
- **WHEN** UC-Integration-ProviderDirectory-31 – Receive Provider Identifier Cross References: Supports reconciliation of identifiers used by the external source and the Provider Directory.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 20
- **WHEN** UC-Integration-ProviderDirectory-32 – Validate Provider Data Quality: Supports validation of incoming provider information before it is accepted.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 21
- **WHEN** UC-Integration-ProviderDirectory-35 – Monitor Provider Integration Interfaces: Provides operational monitoring of the integration.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 22
- **WHEN** UC-Integration-ProviderDirectory-36 – Reconcile Provider Synchronisation Failures: Handles transactions that cannot be successfully processed automatically.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 23
- **WHEN** The source workbook contains several domains that extend beyond the Provider Directory-specific use cases, including participant identity, consent, demographics, terminology, reference data and enterprise identity capabilities. These have been retained rather than removed.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 24
- **WHEN** Where a source user story is abbreviated or grammatically incomplete, the substantive wording has been preserved and the derived workflow language has been kept technology-neutral.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 25
- **WHEN** The expanded scenarios intentionally avoid prescribing a particular product, database, FHIR implementation, application or integration technology.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

#### Scenario: Exception 26
- **WHEN** The document can be used as the functional baseline for mapping the use cases to business capabilities, information requirements, application services and technology options.
- **THEN** Harmonia SHALL prevent an invalid or unauthorised outcome and record or present the applicable correction, approval, reconciliation, escalation or failure state

### Requirement: Business and data controls
Harmonia SHALL apply the business and data controls required by this capability.

#### Scenario: Business or data control 1
- **GIVEN** The Tasmanian Provider Data Store should not automatically be assumed to be authoritative for every provider attribute. Authority should be established at the data-element or information-domain level.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 2
- **GIVEN** Provider identity must be resolved before incoming information is applied to an existing provider record.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 3
- **GIVEN** Provenance should identify the Tasmanian Provider Data Store as the source of externally supplied information.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 4
- **GIVEN** Locally managed information—such as local roles, affiliations, credentialing decisions, visibility settings or other organisation-specific attributes—should not be overwritten unless explicitly governed by the integration.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 5
- **GIVEN** Synchronisation should preserve sufficient history to determine what information was received, when it was received and what changes were subsequently made.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 6
- **GIVEN** Provider information should only become visible to downstream consumers where applicable verification, governance and publication requirements have been satisfied.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 7
- **GIVEN** The integration should support appropriate monitoring, reconciliation and management of failed or incomplete transactions.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

## Implementation Boundary

This use case describes the business requirement to receive and reconcile externally sourced provider informationand is independent of the technology selected to implement the Provider Directory. The same requirement applies whether the Provider Directory is implemented using a PAS, bespoke database, FHIR Server, Medical-Objects, commercial product, Epic EMR, ADHA LDS or another technology. The selected technology determines how the integration, matching, validation, provenance and synchronisation requirements are implemented—not whether the business requirement exists.

## Acceptance Boundary

This specification defines observable Harmonia behaviour derived from the source use case. Internal classes, libraries, persistence mechanisms and deployment choices are implementation decisions unless explicitly constrained by a requirement above.
