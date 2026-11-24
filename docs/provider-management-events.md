# Harmonia Provider Management Business Event Catalogue

**Status:** Draft v0.1  
**Domain:** Provider Management / Enterprise Local Directory  
**Purpose:** Define business-significant provider-domain events that can drive Agora/Synapse information flows, Praxis discovery and governed response processes, Artemis publication, and traceable audit/provenance.

## 1. Intent

These are **business/domain events**, not low-level persistence events and not audit records. An event states that something meaningful has happened in the provider domain which may require notification, collaboration, automated action, publication, or a governed Praxis response.

A single technical change may result in zero, one, or several business events. Consumers should subscribe to business meaning rather than infer meaning from messages such as `Practitioner.updated` or `PractitionerRole.updated`.

The catalogue is deliberately broader than FHIR resources. FHIR resources provide interoperable representations; Harmonia business events express the significance of changes to those representations.

## 2. Event Handling Model

```text
Domain change
    |
    v
Ergon evaluates business significance
    |
    +--> Business Event --> Artemis --> downstream subscribers
    |                         |
    |                         +--> Agora / Synapse
    |                         +--> Praxis initiation / update
    |                         +--> Integration publication
    |
    +--> Audit / provenance evidence
```

### Agora publication policy

| Policy | Meaning |
|---|---|
| `NONE` | No human-facing Agora publication is normally required. |
| `AUDIT_ONLY` | Retain as operational/audit evidence; do not normally surface as collaboration activity. |
| `ACTIVITY_STREAM` | Publish as useful informational activity. |
| `ROLE_ROOM` | Publish to the relevant organisational/provider-role collaboration context. |
| `PROVIDER_SPACE` | Publish to the provider-centred collaboration context. |
| `OPERATIONAL_ROOM` | Publish to an operational/integration administration context. |
| `PRAXIS_ROOM` | Publish into the collaboration context associated with a governed Praxis instance. |
| `URGENT_NOTIFICATION` | Generate a high-significance notification in addition to the appropriate room/space. |

Policies are defaults. Security, privacy, role and information-governance rules remain authoritative over delivery.

### Praxis relationship

An event does **not** imply one Praxis per event. Multiple related events should normally converge on a reusable governed response process. A Praxis may be initiated, updated, escalated, resolved or closed by different events.

## 3. Provider Management Event Catalogue

| ID | Name | Event description | Key impacts | Agora policy | Candidate Praxis |
|---|---|---|---|---|---|
| PM-EVT-001 | Provider Identified | A previously unknown provider has been identified from an authoritative, trusted or submitted source. | Identity resolution; duplicate search; provenance; possible onboarding | `ACTIVITY_STREAM` | Provider Onboarding |
| PM-EVT-002 | Provider Created | A new provider identity has been established in the Harmonia directory. | Directory availability; audit; downstream publication | `ACTIVITY_STREAM` | Provider Onboarding |
| PM-EVT-003 | Provider Details Changed | Material provider demographic or contact information has changed. | Validation; provenance; downstream synchronisation; possible review | `ACTIVITY_STREAM` | Provider Change Stewardship when review is required |
| PM-EVT-004 | Provider Identifier Added | A new identifier has been associated with a provider. | Identity confidence; matching; verification; downstream publication | `AUDIT_ONLY` | Identity Resolution when validation/conflict exists |
| PM-EVT-005 | Provider Identifier Changed | An identifier has been corrected, superseded, retired or materially changed. | Matching; reconciliation; consumer impact; audit | `PROVIDER_SPACE` | Identity Resolution |
| PM-EVT-006 | Provider Identity Conflict Detected | Available information indicates incompatible identity assertions or identifiers for a provider. | Publication hold; steward review; identity confidence; source reconciliation | `PRAXIS_ROOM` | Identity Resolution |
| PM-EVT-007 | Possible Duplicate Provider Detected | Matching identifies two or more provider records that may represent the same individual. | Duplicate review; canonical identity decision; downstream impact | `PRAXIS_ROOM` | Identity Resolution |
| PM-EVT-008 | Provider Identity Resolved | A provider identity ambiguity or duplicate has been resolved. | Canonical identity; links/redirects; downstream correction; audit | `PROVIDER_SPACE` | Identity Resolution |
| PM-EVT-010 | Registration Verified | A professional registration has been successfully verified against its designated authoritative source. | Trust status; eligibility; publication | `ACTIVITY_STREAM` | Credential and Registration Review |
| PM-EVT-011 | Registration Changed | Professional registration details, status or relevant conditions have materially changed. | Role/service eligibility; review; downstream notification | `PROVIDER_SPACE` | Credential and Registration Review |
| PM-EVT-012 | Registration Expiring | A professional registration is approaching its configured expiry threshold. | Review; renewal/remediation; notification; escalation | `PRAXIS_ROOM` | Credential and Registration Review |
| PM-EVT-013 | Registration Expired | A professional registration has expired. | Eligibility; roles/services; publication state; notification | `PRAXIS_ROOM` | Credential and Registration Review |
| PM-EVT-014 | Registration Suspended or Restricted | Registration becomes suspended, restricted, cancelled, or subject to a material condition relevant to directory use. | Immediate governance review; affected roles/services; discovery; notification | `URGENT_NOTIFICATION` | Credential and Registration Review / Provider Suspension |
| PM-EVT-015 | Credential Added | A qualification, credential or certification has been recorded for the provider. | Verification; eligibility; provenance | `AUDIT_ONLY` | Credential and Registration Review when verification is required |
| PM-EVT-016 | Credential Verified | A provider credential has been successfully verified. | Trust status; role/service eligibility; audit | `ACTIVITY_STREAM` | Credential and Registration Review |
| PM-EVT-017 | Credential Expiring | A time-limited credential is approaching expiry. | Renewal; notification; escalation | `PRAXIS_ROOM` | Credential and Registration Review |
| PM-EVT-018 | Credential Expired | A credential has expired. | Eligibility; governance; possible role/provider suspension | `PRAXIS_ROOM` | Credential and Registration Review |
| PM-EVT-020 | Organisation Affiliation Commenced | A provider commences a recognised relationship with an organisation. | PractitionerRole/affiliation; access; services; publication | `ROLE_ROOM` | Provider Onboarding / Provider Change Stewardship |
| PM-EVT-021 | Organisation Affiliation Changed | A material provider-to-organisation relationship has changed. | Roles; services; locations; access; publication | `ROLE_ROOM` | Provider Change Stewardship |
| PM-EVT-022 | Organisation Affiliation Ended | A provider's recognised affiliation with an organisation has ended. | Role closure; access review; service discovery; publication | `ROLE_ROOM` | Provider Offboarding |
| PM-EVT-023 | Provider Role Commenced | A provider commences a defined role in an organisational context. | Discovery; eligibility; access; service/endpoint relationships | `ROLE_ROOM` | Provider Onboarding / Provider Change Stewardship |
| PM-EVT-024 | Provider Role Changed | Specialty, function, scope, period or another material aspect of a provider role changes. | Discovery; authorisation; services; publication | `ROLE_ROOM` | Provider Change Stewardship |
| PM-EVT-025 | Provider Role Ended | A provider no longer performs a defined role. | Search/discovery; services; access; endpoints | `ROLE_ROOM` | Provider Offboarding |
| PM-EVT-026 | Provider Location Assigned | A provider or provider role becomes associated with a service location. | Discovery; routing; service availability | `ACTIVITY_STREAM` | Provider Change Stewardship if approval is required |
| PM-EVT-027 | Provider Location Removed | A provider or provider role ceases association with a location. | Discovery; routing; referrals; downstream update | `ACTIVITY_STREAM` | Provider Change Stewardship if consequential |
| PM-EVT-030 | Service Participation Commenced | A provider/role begins participation in a HealthcareService. | Service discovery; referral; routing; endpoint configuration | `ROLE_ROOM` | Provider Change Stewardship |
| PM-EVT-031 | Service Participation Changed | Provider participation in a HealthcareService materially changes. | Discovery; routing; referral eligibility | `ROLE_ROOM` | Provider Change Stewardship |
| PM-EVT-032 | Service Participation Ended | A provider/role ceases participation in a HealthcareService. | Discovery removal; routing; referrals; publication | `ROLE_ROOM` | Provider Offboarding / Provider Change Stewardship |
| PM-EVT-040 | Electronic Endpoint Registered | An electronic service endpoint has been registered against a provider, organisation, location or service context. | Validation; integration configuration; discovery | `OPERATIONAL_ROOM` | Endpoint Registration and Validation |
| PM-EVT-041 | Endpoint Capability Declared | A communication capability has been declared for an endpoint. | Routing; interoperability; profile/conformance validation | `OPERATIONAL_ROOM` | Endpoint Registration and Validation |
| PM-EVT-042 | Electronic Endpoint Validated | Endpoint connectivity, protocol, security and/or declared capability has been successfully validated. | Publication eligibility; routing activation | `OPERATIONAL_ROOM` | Endpoint Registration and Validation |
| PM-EVT-043 | Electronic Endpoint Changed | Address, protocol, security, capability or other material endpoint metadata has changed. | Revalidation; routing; downstream notification | `OPERATIONAL_ROOM` | Endpoint Registration and Validation |
| PM-EVT-044 | Electronic Endpoint Unavailable | A previously operational endpoint is unavailable or fails a material operational validation. | Routing/failover; operational response; notification | `URGENT_NOTIFICATION` | Endpoint Remediation |
| PM-EVT-045 | Electronic Endpoint Retired | An endpoint has been permanently retired. | Routing removal; discovery; publication | `OPERATIONAL_ROOM` | Endpoint Retirement / Provider Change Stewardship |
| PM-EVT-050 | Provider Record Submitted for Review | A provider-related addition or change requires human validation or approval before becoming effective/published. | Steward task; evidence gathering; publication hold | `PRAXIS_ROOM` | Provider Change Stewardship |
| PM-EVT-051 | Provider Record Approved | A submitted provider change has been approved. | Commit/effect; publication; provenance | `ACTIVITY_STREAM` | Provider Change Stewardship |
| PM-EVT-052 | Provider Record Rejected | A submitted provider change has been rejected. | Submitter notification; correction; audit | `PRAXIS_ROOM` | Provider Change Stewardship |
| PM-EVT-053 | Provider Data Conflict Detected | Different sources assert materially conflicting provider information. | Source-authority evaluation; publication decision; stewardship | `PRAXIS_ROOM` | Data Conflict Resolution |
| PM-EVT-054 | Provider Data Quality Issue Detected | Provider information violates a configured completeness, quality or conformance rule. | Stewardship; possible publication hold; remediation | `PRAXIS_ROOM` | Data Quality Remediation |
| PM-EVT-055 | Provider Data Quality Issue Resolved | A previously identified provider data-quality issue has been resolved. | Release hold; close tasks; audit | `ACTIVITY_STREAM` | Data Quality Remediation |
| PM-EVT-060 | External Provider Update Received | A material provider update has arrived from an external/national/jurisdictional source. | Reconciliation; provenance; rules evaluation | `AUDIT_ONLY` | External Source Reconciliation if not auto-applicable |
| PM-EVT-061 | External Provider Update Applied | An external provider update has been accepted into the Harmonia representation. | Versioning; downstream publication; cache/event invalidation | `AUDIT_ONLY` | External Source Reconciliation when applicable |
| PM-EVT-062 | External Provider Update Requires Review | An external update cannot safely be reconciled automatically. | Steward review; source-authority decision; publication hold | `PRAXIS_ROOM` | External Source Reconciliation |
| PM-EVT-063 | Provider Publication Required | A material provider-domain state change requires distribution to one or more consumers. | Artemis event; API/subscriber update; synchronisation state | `AUDIT_ONLY` | Integration/Publication Remediation only on failure |
| PM-EVT-064 | Provider Publication Completed | Required provider information has been successfully propagated to the intended consumer set. | Synchronisation state; audit | `AUDIT_ONLY` | None normally |
| PM-EVT-065 | Provider Publication Failed | Required provider publication failed or was rejected. | Retry; operational response; downstream inconsistency | `OPERATIONAL_ROOM` | Integration/Publication Remediation |
| PM-EVT-070 | Provider Activated | A provider becomes active and available for appropriate directory use/discovery. | Search/discovery; publication; dependent roles/services | `PROVIDER_SPACE` | Provider Onboarding |
| PM-EVT-071 | Provider Suspended | A provider is temporarily unavailable or ineligible for relevant directory functions. | Discovery; routing; access; affected roles/services; notification | `URGENT_NOTIFICATION` | Provider Suspension and Reinstatement |
| PM-EVT-072 | Provider Reinstated | A suspended provider returns to active status. | Discovery; routing; publication; dependent roles/services | `PROVIDER_SPACE` | Provider Suspension and Reinstatement |
| PM-EVT-073 | Provider Retired | A provider is no longer active in the relevant operational directory context. | Role/service/endpoint lifecycle; historical retention; downstream notification | `PROVIDER_SPACE` | Provider Offboarding |

## 4. Candidate Provider Praxis Streams

| Praxis | Typical initiating/updating events | Purpose |
|---|---|---|
| Provider Onboarding | PM-EVT-001, 002, 020, 023, 070 | Establish a provider and the relationships required for operational directory use. |
| Identity Resolution | PM-EVT-004–008 | Resolve identifier conflicts, duplicates and ambiguous provider identity. |
| Credential and Registration Review | PM-EVT-010–018 | Verify and manage registrations, credentials, expiry and restrictions. |
| Provider Change Stewardship | PM-EVT-003, 020–032, 050–052 | Govern material changes that require review or approval. |
| Provider Offboarding | PM-EVT-022, 025, 032, 073 | Safely end affiliations, roles, services, access and publication. |
| Provider Suspension and Reinstatement | PM-EVT-014, 071, 072 | Govern temporary ineligibility and restoration. |
| Data Conflict Resolution | PM-EVT-053 | Resolve incompatible assertions using source authority and evidence. |
| Data Quality Remediation | PM-EVT-054, 055 | Correct incomplete/non-conformant directory information. |
| Endpoint Registration and Validation | PM-EVT-040–043 | Register, validate and approve electronic service endpoints/capabilities. |
| Endpoint Remediation | PM-EVT-044 | Restore, reroute or otherwise resolve endpoint operational failure. |
| External Source Reconciliation | PM-EVT-060–062 | Reconcile externally sourced changes that cannot be safely auto-applied. |
| Integration/Publication Remediation | PM-EVT-063–065 | Restore failed downstream publication and reconcile consumer state. |

## 5. Suggested Event Envelope

The catalogue describes event *types*. Runtime events should use a common envelope rather than bespoke message structures for each type.

```yaml
eventId: <globally unique event instance id>
eventType: PM-EVT-014
eventName: Registration Suspended or Restricted
occurredAt: <timestamp>
recordedAt: <timestamp>
subject:
  type: Practitioner
  id: <canonical Harmonia id>
context:
  practitionerRole: <optional id>
  organisation: <optional id>
  location: <optional id>
  healthcareService: <optional id>
source:
  system: <source system>
  authority: <authority classification>
  sourceReference: <source event/resource/message id>
provenance:
  correlationId: <correlation id>
  causationId: <causing event/task id>
significance: <informational|action-required|urgent>
agoraPolicy: <policy>
praxis:
  type: <candidate/actual Praxis type>
  instanceId: <when instantiated>
impacts:
  - <structured impact>
```

The event should normally reference the affected domain objects rather than embedding complete provider records. Consumers requiring current state should retrieve it through the appropriate Harmonia service, subject to authorisation.

## 6. Design Guardrails

1. Emit business events because business meaning changed, not merely because a database row or FHIR resource changed.
2. Keep event type identity stable; evolve payload/envelope versions explicitly.
3. Preserve source authority, causation, correlation and provenance.
4. Do not publish sensitive provider information to Agora merely because it is present in the source event.
5. A Praxis is a governed response pattern, not a synonym for an event handler.
6. Agora is a collaboration surface, not the authoritative provider record.
7. Artemis is the event/distribution mechanism; durable domain state remains in the appropriate Harmonia persistence service.
8. Events should be idempotently consumable and suitable for replay where the integration design permits it.
9. Publication policy must be evaluated with role, security and information-governance policy before delivery.
10. Event completion does not imply downstream publication completion unless the event explicitly represents that outcome.

## 7. Open Questions

- Confirm the formal distinction between `Provider`, FHIR `Practitioner`, and the Harmonia canonical provider identity.
- Define the authoritative-source hierarchy and conflict rules for identifiers, registration, employment, roles, services and endpoints.
- Define significance/escalation thresholds for expiry, restriction and endpoint availability events.
- Determine whether publication lifecycle events belong in this catalogue or a future shared Integration Event catalogue once that domain is formalised.
- Define the exact mapping between Provider Praxis instances, Ponos work, Ergon definitions and Agora/Synapse rooms.
