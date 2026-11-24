# Harmonia Patient Identity Business Event Catalogue

**Status:** Draft v0.1  
**Domain:** Patient Identity Management  
**Purpose:** Define business-significant patient-identity events that can drive Agora/Synapse information flows, Praxis discovery and governed identity-management processes, Artemis publication, and traceable audit/provenance.

## 1. Scope and Boundary

This catalogue is intentionally limited to **patient identity management**. It does **not** define broader clinical-domain events.

Patient identity events concern the establishment, identification, matching, linking, merging, correction, verification and lifecycle of the identity used to associate information with the correct patient/participant.

The catalogue should not be interpreted as making Harmonia the authoritative source of every patient demographic fact. Source authority and provenance remain explicit. Harmonia may maintain a governed enterprise identity representation while source systems remain authoritative for designated source facts.

These are business/domain events rather than raw technical changes such as `Patient.updated`.

## 2. Event Handling Model

```text
Patient identity input/change
        |
        v
Identity/matching rules + Ergon evaluation
        |
        +--> safe automatic outcome --> Business Event --> Artemis
        |
        +--> ambiguity/conflict ------> Praxis
                                          |
                                          +--> steward tasks
                                          +--> evidence
                                          +--> Agora/Synapse collaboration
                                          +--> governed decision
                                          +--> resulting Business Event
```

### Agora publication policy

| Policy | Meaning |
|---|---|
| `NONE` | No human-facing Agora publication is normally required. |
| `AUDIT_ONLY` | Retain as operational/audit evidence; do not normally surface as collaboration activity. |
| `ACTIVITY_STREAM` | Publish as useful informational activity where policy permits. |
| `PATIENT_IDENTITY_ROOM` | Publish to a restricted patient-identity/stewardship context. |
| `OPERATIONAL_ROOM` | Publish to an operational/integration administration context. |
| `PRAXIS_ROOM` | Publish into the restricted collaboration context associated with the relevant identity Praxis. |
| `URGENT_NOTIFICATION` | Generate a high-significance notification in addition to the appropriate restricted room. |

Patient identity information is sensitive. Agora publication should therefore default to the **minimum information necessary**, with access to full identity details obtained from the authoritative Harmonia service under role-based controls.

## 3. Patient Identity Event Catalogue

| ID | Name | Event description | Key impacts | Agora policy | Candidate Praxis |
|---|---|---|---|---|---|
| PI-EVT-001 | Patient Identity Observed | A patient identity assertion not previously known in the relevant Harmonia context has been received from a source system or external service. | Search/match; source provenance; possible identity creation | `AUDIT_ONLY` | Patient Identity Establishment if not automatically resolved |
| PI-EVT-002 | Patient Identity Established | A canonical/enterprise patient identity has been established for operational use. | Identity availability; linking; downstream publication | `ACTIVITY_STREAM` | Patient Identity Establishment |
| PI-EVT-003 | Patient Demographics Changed | Material identity/demographic attributes used for identification or matching have changed. | Match confidence; verification; downstream synchronisation | `AUDIT_ONLY` | Demographic Change Review when risk threshold exceeded |
| PI-EVT-004 | Patient Identifier Added | A new identifier has been associated with the patient identity. | Matching; cross-system linkage; verification | `AUDIT_ONLY` | Identifier Resolution when ambiguous/conflicting |
| PI-EVT-005 | Patient Identifier Changed | An existing identifier has been corrected, superseded, retired or otherwise materially changed. | Cross-system linkage; reconciliation; downstream correction | `PATIENT_IDENTITY_ROOM` | Identifier Resolution |
| PI-EVT-006 | Patient Identifier Verified | An identifier has been successfully verified against its designated authoritative source. | Identity confidence; match strength; publication | `AUDIT_ONLY` | Patient Identity Verification |
| PI-EVT-007 | Patient Identifier Verification Failed | An identifier could not be verified or was rejected by its designated authoritative source. | Identity confidence; publication/use constraints; steward review | `PRAXIS_ROOM` | Patient Identity Verification |
| PI-EVT-008 | Patient Identifier Conflict Detected | Two or more identifier assertions are incompatible or cannot safely coexist on the same patient identity. | Identity safety; publication hold; reconciliation | `URGENT_NOTIFICATION` | Identifier Resolution |
| PI-EVT-010 | Patient Match Found | An incoming identity assertion has been matched to an existing patient with sufficient confidence for the configured context. | Link source record; provenance; downstream use | `AUDIT_ONLY` | None normally |
| PI-EVT-011 | Patient Match Ambiguous | Matching produced multiple plausible candidates or insufficient confidence for automatic resolution. | Human review; hold association; evidence collection | `PRAXIS_ROOM` | Patient Identity Resolution |
| PI-EVT-012 | Patient Match Rejected | A proposed patient match has been determined to be incorrect. | Prevent incorrect linkage; rematch; audit | `PRAXIS_ROOM` | Patient Identity Resolution |
| PI-EVT-013 | No Patient Match Found | No existing identity satisfies the configured match threshold. | Potential new identity; further search/verification | `AUDIT_ONLY` | Patient Identity Establishment when manual review is required |
| PI-EVT-014 | Potential Duplicate Patient Detected | Two or more patient identities may represent the same person. | Duplicate investigation; safety review; downstream implications | `URGENT_NOTIFICATION` | Duplicate Patient Resolution |
| PI-EVT-015 | Duplicate Patient Confirmed | Two or more records have been determined to represent the same patient. | Merge/link decision; source reconciliation; downstream correction | `PRAXIS_ROOM` | Duplicate Patient Resolution |
| PI-EVT-016 | Duplicate Patient Excluded | Suspected duplicate records have been confirmed to represent different people. | Suppress repeated false matches where appropriate; audit | `AUDIT_ONLY` | Duplicate Patient Resolution |
| PI-EVT-020 | Patient Identities Linked | Two patient identity records have been explicitly linked while retaining distinct identity records. | Search/matching; navigation; downstream linkage semantics | `PATIENT_IDENTITY_ROOM` | Patient Identity Resolution |
| PI-EVT-021 | Patient Identity Link Removed | A previously established patient identity link has been removed or invalidated. | Matching; downstream correction; safety review | `PRAXIS_ROOM` | Patient Identity Resolution |
| PI-EVT-022 | Patient Merge Proposed | A merge of patient identities has been proposed but not yet authorised/effected. | Evidence; governance; publication hold | `PRAXIS_ROOM` | Patient Merge |
| PI-EVT-023 | Patient Identities Merged | Multiple patient identities have been resolved into a designated surviving/canonical identity according to governance rules. | Redirect/linkage; downstream correction; historical traceability | `URGENT_NOTIFICATION` | Patient Merge |
| PI-EVT-024 | Patient Merge Reversed | A previous merge has been reversed/unmerged following error or new evidence. | High-risk downstream correction; re-link records; safety review | `URGENT_NOTIFICATION` | Patient Merge Reversal |
| PI-EVT-025 | Patient Identity Marked Entered-in-Error | An identity/record has been determined to have been created or associated in error and is governed accordingly. | Exclusion from normal use; downstream correction; audit | `URGENT_NOTIFICATION` | Identity Error Remediation |
| PI-EVT-030 | IHI Lookup Requested | An Individual Healthcare Identifier lookup/verification process has been initiated for a patient identity. | External identity verification; audit/correlation | `AUDIT_ONLY` | Patient Identity Verification only if unresolved |
| PI-EVT-031 | IHI Confirmed | An IHI has been successfully resolved/verified for the patient under applicable rules. | National identity confidence; identifier association; downstream publication | `AUDIT_ONLY` | Patient Identity Verification |
| PI-EVT-032 | IHI Not Resolved | An IHI lookup did not produce an acceptable result. | Further verification; demographic review; possible manual action | `PRAXIS_ROOM` | Patient Identity Verification |
| PI-EVT-033 | IHI Conflict Detected | An asserted or returned IHI conflicts with existing patient identity information. | Patient-safety risk; publication/use hold; urgent reconciliation | `URGENT_NOTIFICATION` | Identifier Resolution / Patient Identity Resolution |
| PI-EVT-040 | Identity Data Conflict Detected | Authoritative or trusted sources provide materially conflicting patient identity/demographic assertions. | Source-authority review; matching confidence; downstream use | `PRAXIS_ROOM` | Identity Data Conflict Resolution |
| PI-EVT-041 | Identity Data Quality Issue Detected | Patient identity information fails configured completeness, validity or quality rules. | Matching reliability; remediation; possible processing hold | `PRAXIS_ROOM` | Identity Data Quality Remediation |
| PI-EVT-042 | Identity Data Quality Issue Resolved | A previously identified patient identity quality issue has been resolved. | Restore normal processing; close task; audit | `ACTIVITY_STREAM` | Identity Data Quality Remediation |
| PI-EVT-043 | Identity Source Authority Changed | The designated authority or trust classification for a patient identity attribute/source has changed. | Re-evaluation; reconciliation; governance | `PATIENT_IDENTITY_ROOM` | Identity Data Conflict Resolution if existing values affected |
| PI-EVT-050 | Patient Identity Submitted for Review | A patient identity, match, link, merge or demographic change requires governed human review. | Steward task; evidence; processing/publication hold | `PRAXIS_ROOM` | Patient Identity Stewardship |
| PI-EVT-051 | Patient Identity Decision Approved | A proposed governed identity action has been approved. | Apply decision; provenance; downstream consequences | `PATIENT_IDENTITY_ROOM` | Relevant active Praxis |
| PI-EVT-052 | Patient Identity Decision Rejected | A proposed governed identity action has been rejected. | Preserve prior state; notify initiator; audit | `PRAXIS_ROOM` | Relevant active Praxis |
| PI-EVT-053 | Patient Identity Decision Escalated | An identity decision cannot be resolved at the current authority/role and has been escalated. | Higher-authority review; restricted collaboration; SLA/escalation | `URGENT_NOTIFICATION` | Relevant active Praxis |
| PI-EVT-060 | External Patient Identity Update Received | An external source has supplied a material identity/demographic update. | Reconciliation; provenance; matching-rule evaluation | `AUDIT_ONLY` | External Identity Reconciliation if not auto-applicable |
| PI-EVT-061 | External Patient Identity Update Applied | An external patient identity update has been accepted into the Harmonia representation. | Versioning; downstream synchronisation | `AUDIT_ONLY` | External Identity Reconciliation when applicable |
| PI-EVT-062 | External Patient Identity Update Requires Review | An external identity update cannot safely be reconciled automatically. | Steward review; source-authority decision; processing hold | `PRAXIS_ROOM` | External Identity Reconciliation |
| PI-EVT-063 | Patient Identity Publication Required | A material identity state change requires distribution to one or more consumers. | Artemis; downstream synchronisation; correction propagation | `AUDIT_ONLY` | Identity Publication Remediation only on failure |
| PI-EVT-064 | Patient Identity Publication Completed | Required identity information/correction has been propagated successfully to intended consumers. | Synchronisation state; audit | `AUDIT_ONLY` | None normally |
| PI-EVT-065 | Patient Identity Publication Failed | Required identity information/correction could not be propagated to an intended consumer. | Inconsistent identity state; retry; operational remediation | `OPERATIONAL_ROOM` | Identity Publication Remediation |
| PI-EVT-070 | Patient Identity Activated | A patient identity becomes available for normal operational identification/matching use. | Search/matching; publication | `AUDIT_ONLY` | Patient Identity Establishment |
| PI-EVT-071 | Patient Identity Restricted | Use of a patient identity is restricted because of identity quality, governance or safety concerns. | Search/use constraints; notification; steward review | `URGENT_NOTIFICATION` | Identity Error Remediation / Patient Identity Resolution |
| PI-EVT-072 | Patient Identity Restriction Removed | A previous identity-use restriction has been resolved and removed. | Restore normal matching/use; publication | `PATIENT_IDENTITY_ROOM` | Relevant remediation Praxis |
| PI-EVT-073 | Patient Identity Deactivated | A patient identity is no longer active for normal operational use while historical traceability is retained. | Matching/search behaviour; downstream publication; history | `PATIENT_IDENTITY_ROOM` | Patient Identity Stewardship |

## 4. Candidate Patient Identity Praxis Streams

| Praxis | Typical initiating/updating events | Purpose |
|---|---|---|
| Patient Identity Establishment | PI-EVT-001, 002, 013, 070 | Establish a usable enterprise patient identity when automatic resolution is insufficient. |
| Patient Identity Verification | PI-EVT-006, 007, 030–032 | Verify identifiers and identity assertions against authoritative services/sources. |
| Identifier Resolution | PI-EVT-004–008, 033 | Resolve incompatible, invalid or uncertain patient identifiers. |
| Patient Identity Resolution | PI-EVT-011, 012, 020, 021, 033 | Resolve ambiguous matches and identity relationships. |
| Duplicate Patient Resolution | PI-EVT-014–016 | Determine whether suspected duplicate identities represent the same person and govern the outcome. |
| Patient Merge | PI-EVT-022, 023 | Govern a high-impact identity merge and its downstream consequences. |
| Patient Merge Reversal | PI-EVT-024 | Govern reversal of an incorrect merge and coordinated downstream correction. |
| Identity Error Remediation | PI-EVT-025, 071, 072 | Control records/identities that are unsafe or erroneous and restore safe use when resolved. |
| Demographic Change Review | PI-EVT-003 | Review demographic changes that materially affect matching confidence or identity safety. |
| Identity Data Conflict Resolution | PI-EVT-040, 043 | Resolve conflicting identity assertions according to authority, evidence and governance. |
| Identity Data Quality Remediation | PI-EVT-041, 042 | Correct identity information that fails quality/completeness/conformance rules. |
| Patient Identity Stewardship | PI-EVT-050–053, 073 | Provide the general governed review/escalation process for identity decisions not covered by a more specific Praxis. |
| External Identity Reconciliation | PI-EVT-060–062 | Reconcile externally sourced patient identity changes that cannot be safely auto-applied. |
| Identity Publication Remediation | PI-EVT-063–065 | Restore failed propagation of identity changes/corrections to consumers. |

## 5. Important Identity Semantics

### Link is not merge

`Patient Identities Linked` and `Patient Identities Merged` are intentionally different business events.

A **link** records a governed relationship between identities while retaining their distinct identity records. A **merge** changes which identity is treated as the surviving/canonical operational identity and normally has substantially greater downstream consequences.

### Matching outcome is not identity truth

A matching algorithm produces evidence and a confidence-based outcome under configured rules. Harmonia should not equate a probabilistic match with an immutable statement of real-world identity. Ambiguous and high-risk outcomes should be capable of entering a governed Praxis.

### Identity events are not clinical events

An identity merge may have consequences for clinical information, but this catalogue stops at the identity-domain event and its required downstream correction/notification. It does not define clinical-record reconciliation events or clinical workflow. Those belong to a later, separately governed domain design.

## 6. Suggested Event Envelope

```yaml
eventId: <globally unique event instance id>
eventType: PI-EVT-014
eventName: Potential Duplicate Patient Detected
occurredAt: <timestamp>
recordedAt: <timestamp>
subject:
  type: Patient
  id: <canonical Harmonia identity id or candidate-set reference>
context:
  sourcePatientIds:
    - <reference>
  candidatePatientIds:
    - <reference>
source:
  system: <source system/service>
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

The event envelope should contain only the identity information necessary to identify and route the event. Sensitive demographic details and matching evidence should be retrieved from controlled Harmonia services by authorised consumers rather than indiscriminately copied into Artemis or Matrix/Synapse messages.

## 7. Design Guardrails

1. Patient identity events must be treated as potentially patient-safety significant.
2. Do not expose unnecessary demographics, identifiers or matching evidence in Agora/Synapse event text.
3. Preserve every merge, link, unlink, identifier correction and reversal as traceable history; do not erase the lineage of identity decisions.
4. Separate automatic matching evidence from governed identity decisions.
5. Keep source authority and provenance explicit for every material identity assertion.
6. A FHIR `Patient` representation is an interoperability representation; Harmonia identity governance must not be reduced to CRUD operations on that resource.
7. Agora provides restricted collaboration around identity work; it is not the authoritative patient identity store.
8. Praxis should govern ambiguous/high-impact identity decisions rather than embedding ad-hoc manual states in integration code.
9. Publication/correction events must support idempotency and correlation so downstream identity state can be reconciled safely.
10. No broader clinical-domain event semantics are defined by this catalogue.

## 8. Open Questions

- Confirm Harmonia's canonical distinction between `Person`, `Patient`, source-system patient records and enterprise identity.
- Define match confidence levels and which outcomes may be automatically accepted versus requiring Praxis.
- Define merge/unmerge governance, authority and minimum evidence requirements.
- Define how source-system MRNs/UR numbers, IHI and other identifiers are classified and verified.
- Define the authoritative-source hierarchy for demographic attributes and the handling of conflicting values.
- Define whether identity restrictions must propagate synchronously to selected safety-critical consumers.
- Define the relationship between patient identity Praxis, Ponos work items, Ergon rules and restricted Agora/Synapse rooms.
