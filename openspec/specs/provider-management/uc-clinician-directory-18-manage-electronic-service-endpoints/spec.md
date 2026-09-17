# Manage Electronic Service Endpoints

## Purpose

Information can be exchanged electronically via well-defined integration endpoints.

## Source Traceability

- **Source Use Case:** `UC-Clinician-Directory-18`
- **Source Scope:** Scope – Clinician Contact and Referral Information
- **User Story:** Maintain the electronic endpoints used for referrals and clinical communications so that information can be exchanged electronically.
- **Related Use Cases:** None identified.

## Requirements

### Requirement: Manage Electronic Service Endpoints
Harmonia SHALL support **Manage Electronic Service Endpoints** so that Information can be exchanged electronically via well-defined integration endpoints.

#### Scenario: Manage Electronic Service Endpoints - successful outcome
- **GIVEN** The electronic service capability has been defined, including the business purpose of the endpoint and the type of transactions or information it is intended to support, for example referral receipt, secure messaging, FHIR API access, document exchange, notifications or other machine-to-machine communication.
- **GIVEN** The endpoint type and technical standard have been defined, including the applicable protocol and interaction model, such as REST/FHIR, HL7 v2 messaging, secure messaging, SOAP, file transfer, event subscription or another approved integration pattern.
- **GIVEN** The supported message or resource structures have been agreed, including applicable schemas, profiles, versions, mandatory fields, code sets and conformance requirements.
- **GIVEN** The endpoint ownership has been established, identifying the provider, organisation, service or system responsible for the endpoint and the authorised party responsible for maintaining it.
- **GIVEN** The relationship between the endpoint and the provider/service context has been defined, including whether the endpoint applies to:
- **GIVEN** an individual clinician;
- **GIVEN** an organisation;
- **GIVEN** a location;
- **GIVEN** a specialty/service;
- **GIVEN** a particular role;
- **GIVEN** or a combination of these.
- **GIVEN** The connectivity constraints have been identified, including:
- **GIVEN** permitted network paths;
- **GIVEN** internet, Health network or private-network requirements;
- **GIVEN** firewall and routing rules;
- **GIVEN** proxy or gateway requirements;
- **GIVEN** inbound versus outbound connectivity;
- **GIVEN** DNS/name resolution requirements;
- **GIVEN** IP allow-listing where applicable;
- **GIVEN** and any jurisdictional or organisational network restrictions.
- **GIVEN** The security requirements have been defined, including authentication, authorisation, transport security, certificate requirements, encryption, token mechanisms, trust relationships and any required mutual authentication.
- **GIVEN** The endpoint address and addressing rules have been validated, including URI, hostname, port, messaging address, service identifier or other technical addressing attributes required by the integration standard.
- **GIVEN** The availability and service expectations have been defined, including expected hours of operation, service availability, response-time expectations and any dependency on scheduled or real-time communication.
- **GIVEN** The supported transaction direction has been defined, including whether the endpoint:
- **GIVEN** receives information;
- **GIVEN** sends information;
- **GIVEN** supports request/response interaction;
- **GIVEN** supports publish/subscribe;
- **GIVEN** or supports bi-directional exchange.
- **GIVEN** The supported payload size, volume and frequency constraints have been identified, including any throughput, concurrency, rate-limit, throttling or message-size limitations.
- **GIVEN** The error-handling and retry behaviour has been agreed, including how failed transactions are reported, whether automatic retries occur, and how unresolved failures are escalated.
- **GIVEN** The monitoring requirements have been defined, including endpoint health, availability, failed transactions, latency, certificate expiry and other operational conditions that may affect service.
- **GIVEN** The test and validation process has been completed or agreed, including connectivity testing, conformance testing, security testing and confirmation that the endpoint behaves as expected before being made active.
- **GIVEN** The lifecycle status of the endpoint is known, including whether it is proposed, testing, active, suspended, deprecated or retired.
- **GIVEN** Any dependencies on external services, vendors or national infrastructure have been identified, including constraints arising from their availability, interface specifications, certification requirements or change schedules.
- **GIVEN** The endpoint has been assessed against applicable privacy, security, architecture and information-governance requirements before being made available for production use.
- **WHEN** The actor needs to perform the 'Manage Electronic Service Endpoints' activity, or an authorised business process initiates the activity.
- **THEN** The Provider Directory Administrator selects the clinician, organisation, location or service for which an electronic service endpoint is to be managed.
- **THEN** The administrator selects or identifies the applicable Electronic Service Endpoint Capability.
- **THEN** The system captures the endpoint address and associates it with the defined capability.
- **THEN** The system validates the endpoint against the capability's required protocol, addressing, security and connectivity characteristics.
- **THEN** The system records ownership, status, effective dates and other required operational metadata.
- **THEN** The endpoint is validated/tested in accordance with the applicable capability requirements.
- **THEN** Once approved, the endpoint is made available for discovery and use by authorised consuming systems.
- **THEN** The 'Manage Electronic Service Endpoints' activity is completed successfully, or a clear validation, approval, exception or escalation outcome is recorded.

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
- **GIVEN** Electronic Service Endpoint Capability: An electronic service endpoint represents more than a technical address. It describes the set of technical and operational characteristics that define how a clinician, organisation, location or service can electronically exchange information. This includes the endpoint purpose, protocol, interface type, supported message/resource structures and profiles, addressing, security requirements, direction of exchange, connectivity constraints, availability, throughput, ownership and lifecycle status.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 2
- **GIVEN** Target-State Information Model:  For the target-state information model, a suitable model would specify the relationships as: 
       Clinician / Organisation / Location / Service → Electronic Service Endpoint → Endpoint Capability
where Endpoint Capability defines what/how it communicates and Electronic Service Endpoint identifies where that capability can actually be reached. This distinction will become particularly useful when used in conjunction with FHIR Endpoint, secure messaging, Rhapsody, API endpoints and national services.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 3
- **GIVEN** Multiple Provider Endpoints: A provider or organisation may have multiple endpoints for different purposes, and each endpoint should clearly identify the capability it supports.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 4
- **GIVEN** Technical Endpoint/Service Relationship: Endpoint information should distinguish technical endpoint identity from the business service it supports.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 5
- **GIVEN** Endpoint Function Definition: Endpoint records should include sufficient metadata for a consuming system to determine whether the endpoint is appropriate for its intended transaction.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 6
- **GIVEN** Security Restrictions: Security and connectivity information should not expose sensitive infrastructure details beyond what is required by authorised consumers.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 7
- **GIVEN** Endpoint Information Versioning: Endpoint changes should be versioned and auditable.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 8
- **GIVEN** Endpoint History Management: Retired or superseded endpoints should remain historically traceable but must not be returned as active endpoints.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 9
- **GIVEN** Endpoint Specification Detail: Where endpoint capability depends on a standard or profile, the supported version/profile should be recorded explicitly.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

#### Scenario: Business or data control 10
- **GIVEN** Endpoint Selection Rules: Where multiple candidate endpoints exist, selection rules should be deterministic and based on purpose, service, organisation, location, capability and status.
- **WHEN** information is created, changed, validated, published, exchanged or consumed through this capability
- **THEN** Harmonia SHALL preserve the stated control as an observable business rule or constraint

## Implementation Boundary

This is a business capability/use-case requirement rather than a requirement for a particular technology product. The same business outcome may be implemented through different applications, services, databases, APIs or manual processes.

## Acceptance Boundary

This specification defines observable Harmonia behaviour derived from the source use case. Internal classes, libraries, persistence mechanisms and deployment choices are implementation decisions unless explicitly constrained by a requirement above.
