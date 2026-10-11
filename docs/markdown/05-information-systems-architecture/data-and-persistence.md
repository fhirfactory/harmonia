<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Logical Data, Information Responsibility and Persistence

**Standing: reconstructed Harmonia Candidate Information Systems Architecture.**
[Scope](README.md) and [source dispositions](sources.md) apply. This view records
data already coupled to documented solution features/components. It does not
catalogue every Domain04 concept, test Information Architecture coverage, invent
Information Resources or approve technology-specific schemas.

## Distinct concerns

```text
Logical Data meaning described by candidate sources
  → Application Data Object / representation
  → Information Management Responsibility
  → Persistence Responsibility
  → Persistence Data Structure
  → Technology Platform (documented historical mechanism, not selected here)
```

The arrows describe levels of explanation, not a required one-to-one realisation
or an upstream traceability chain. A native FHIR resource may be an application
Data Object; it is not automatically a component, Information Unit, Twin,
authoritative assertion, durable row or external contract. A schema/table does
not define the managed information's meaning or authority.

## Coupled logical/application Data Objects

| ID | Logical data / candidate application object | Information management / production / use | State and persistence responsibility | Evidence and unresolved allocation |
| :--- | :--- | :--- | :--- | :--- |
| D01 | Trigger/event: **ErgonEvent**, reason and correlation metadata | Calliope defines envelope/reason vocabulary; Pylai and activities produce/use events; Ponos consumes | Operational event/context; recoverable transfer through Petasos where designated, not an automatic permanent audit record | L06/L07/L23; envelope relations to Pragma/PetasosMessage not completely specified, CDG-02/16. |
| D02 | Activity input/output: **ErgonPayload**, native resource/Bundle, raw HL7/JSON or references | Calliope contract support; Ergo/Ergon acts upon/generates; Pragma carries or references | Active use through Mneme; durable resulting managed information through Mnemosyne; binary custodian unknown | L06/L03/L19. Reference-only versus payload-bearing Pragma variants CDG-02/25. |
| D03 | Security: **ThemisSecurityContext**, principal, role/authority/label, authorization request/decision, **ThemisAuditEvent** | Themis policy/contracts; trusted context supplied at governed boundaries; evidence produced according to policy | Context transient/operational by default; significant evidence preserved through Kleio; policy/identity store unspecified | A01 AX-07–09, L08/L14. Context serialization is not automatic resource persistence, CDG-13–15/30. |
| D04 | Governed access/change: **ResourceKey**, **GovernedRead**, active token, expected/authoritative version, **WriteResult**, **PersistenceOperationEnvelope** | Mneme-facing read/change/coordination service contracts; Mnemosyne establishes commit; Themis governs | Observation token/active generation and durable version remain distinct; known commit/degraded convergence/unknown outcome remain distinct | A01 AX-05/14/15, L14. Carriers are not extra domain assets; exact generation/token/result mapping CDG-16/31. |
| D05 | Work/progression: **Pragma**, checkpoint, parent/correlation association, payload/reference and task output | Pragma carrier used by Pylai, Ponos/Praxis/Erga and outbound observations; Calliope supplies model support | Active state described in Mneme; durable Task/non-FHIR history allocations differ; Pragma does not establish authority | L01/L05/L16/L19; CDG-02/03/10/12/13. U01 preserves distinct ActionableTask/FulfillmentTask use. |
| D06 | Definition/composition: **TaskSequence / PraxisDefinition**, activity ordering, subscriptions, triggers/gateway targets | Praxis definition/composition support; loaders/seeders distribute; Ponos resolves and executes | Active definition cache and operations persistence described; authority/lifecycle/history/version-binding unestablished | L04/L19/L23/L10. No automatic archetype asset or definition-to-instance equivalence, CDG-01/04/06. |
| D07 | Routing/subscription: **Topic**, **TopicSubscription**, destination configuration / registry | Calliope definitions; Praxis matching; execution distribution and Pylai destination selection | Definition/configuration and operational routing state; authoritative configuration lifecycle/storage unspecified | L06/L19/L30. Logical routing intention is distinct from queue/host/port configuration, CDG-06/28. |
| D08 | Directory: **Practitioner, PractitionerRole, Organization, Location, HealthcareService, Endpoint, Group** | Pylai interaction, registry Erga validation/change, Themis policy, Iris presentation; Mneme active access | Mnemosyne clinical/FHIR preservation; independently referenced Endpoint and Group; Group is not an IAM/security group | L15/L18. Candidate resource topology is not Domain04 coverage or universal authority; search/store/history CDG-11/12/21/33. |
| D09 | Clinical exchange/content: **Patient, Encounter, Location, ServiceRequest, Observation, DiagnosticReport, Communication, Consent** and resource Bundles | Documented converter/Erga inputs/outputs; Pylai boundary; Iris context; Mneme access | Clinical/FHIR Mnemosyne slice described; exact supported inventories vary. Persisted assertion retains its own source authority | L03/L06/L09/L10/L18. No Master Patient or originating clinical-work authority inferred, CDG-20/29. |
| D10 | Destination progression: **destination status/checkpoint**, Task output delivery extensions, ACK/error/time observations | Distribution Ergon initiates; Petasos transfers; outbound Pylai records externally observed outcome; Iris displays | Active carrier plus durable Task/history intent; parent/completion/evidence persistence contract unclear | AGENTS REC-002, L03/L30/L28. Queue, delivery, durable commit and work resolution distinct, CDG-18/34. |
| D11 | Work transfer: **PetasosMessage / destination**; message identity, type/schema, source, correlation/causation, opaque payload, durable marker | Petasos manages transfer/envelope; producer and consumer retain domain meaning | In-flight queue/journal recoverability at designated transitions; redelivery and dead-letter state; not permanent committed resource storage | L29/L13 ADR-014–017; CDG-16/17/19. Transport metadata need not become payload content. |
| D12 | Evidence: **AuditEvent / Provenance**, significant transition/attempt references | Producers describe significant facts; Kleio preserves evidence; Themis evaluation and assurance use are separate | Append-only accepted evidence; detailed Kleio/Mnemosyne repository and FHIR/operations allocation unresolved | A01 AX-08/09, L13/L08/L15/L20; CDG-13/14. Diagnostic checkpoint persistence is not inherently evidence. |
| D13 | Operational support: **OperationResourceEntity**, task/sequence metadata, module/queue/health/alert/trace observations | Component producers; Ponos/Praxis progression; Iris aggregator/providers present; Mneme active access | Non-FHIR Mnemosyne slice documented for selected records; transient metrics/diagnostics not all durable by default | L16/L21/L23. No single universal telemetry owner or retention policy established, CDG-13/27/28. |
| D14 | Collaboration: **AgoraSpaceRequest**, collaboration event/result, identity/context mapping, transaction record and room/membership projections | Agora lifecycle/identity/reconciliation, Themis, Petasos; context-source contract incomplete | Mapping/transaction relational persistence and separate homeserver room/event state described; authority/storage allocation unresolved | L12/L20; CDG-19–23. Conversation and formal managed clinical information are distinct. |
| D15 | Document/media: **DocumentReference** metadata and clinical binary attachment/payload | FHIR resource support/cache named; Ergon payload examples carry attachments | Mnemosyne FHIR metadata intent; payload/media persistence owner, structures, retrieval and lifecycle unknown | L10/L17/L19/L23. DocumentReference support alone does not establish a Media Server or blob architecture, CDG-25. |
| D16 | Reference/terminology: **model/profile/value set/binding/concept map**, compiled enums; proposed expansion/validation results | Calliope semantic reference authority; runtime defined logic consumes; proposed services not allocated | Shared/compiled artefacts documented; runtime terminology/reference durability and version lifecycle unspecified | A02, L06/L17/L27, CDG-24. No technology or separate terminology component selected. |
| D17 | Twin and assurance context, basis/evidence/assessment/finding/outcome candidate areas | Twin uses entity context and coordinates activity; Dokimasia coordinates independent assurance; evidence retains originating responsibility | Specific application Data Objects and persistence/control allocation unestablished; no Twin table or assurance family fabricated | A02/A04/L31, CDG-07–09. Behaviour/context resemblance supplies no inheritance or Information Resource. |

### Task and Pragma candidate meaning

The following records [U01](sources.md#u01), preserving [A03](sources.md#a03):

```text
ActionableTaskArchetype (meta-concept)
  -- logically embodied by --> PragmaFactory (candidate)

ActionableTask (work instance)       FulfillmentTask (undertaking)
  -- candidate representation --> Pragma <-- candidate representation --
       WorkOrder / ToDo / Stimulus / Effector forms
```

Shared representation does not imply shared identity, lifecycle, execution
state or outcome ownership. ActionableTask concurrency, outcome participation
and output rules remain orthogonal. TaskOutcome resolves the ActionableTask;
FulfillmentTask does not independently own it. Candidate form discriminators,
factory inputs/outputs and concrete relationships to carrier state are unknown
(CDG-03/04). No exhaustive IA-to-object mapping has been performed. ReportedTask
identity, necessity and meaning remain deferred; no accountable report object
or automatic assurance output is created.

## Responsibility and state boundaries

| Concern | Current governing meaning / reconstructed candidate intent | What remains incomplete in candidate documentation |
| :--- | :--- | :--- |
| Application-facing information use | Mneme owns managed access, active observation/coordination; activities/requesting applications use governed contracts | Legacy raw cache/direct storage paths conflict; complete service/data contracts not reconstructed from code. A01/A02, CDG-11/31. |
| Durable establishment and recovery | Mnemosyne atomically establishes durable state/versions/management metadata; Mneme converges after commit | Cache write-behind cannot establish authority; structure/history/storage slices compete. A01, L13, CDG-10/12. |
| Activity and entity progression | Ponos progresses activity; Twin coordinates entity activity against governed entity information | Specific coordination/control/activation Data Objects and persistence are unknown. A02, CDG-03/07. |
| Definitions and configuration | Praxis and Calliope provide documented definition/reference support | Runtime definition governance, release/version binding, recovery and persistence structures incompletely described. L04/L19, CDG-06/24. |
| Information authority | Source assertion credibility/domain authority independent of storage or transport; Calliope governs authority semantics | Historical universal truth, clinical workflow and roster ownership claims conflict. A01 AX-06/18, CDG-20/21/29. |
| Security/evidence/assurance | Operational context, evaluation, evidence production/preservation and independent assessment retain distinct responsibilities | Automatic security persistence/all-checkpoint audit and evidence-store allocations conflict; assurance execution remains open. A01/A04, CDG-08/09/13–15. |

Known commit with degraded convergence is not a failed commit. An unknown commit
effect remains unknown until established; availability failure or denial is not
resource absence. Active generation must represent successfully established
active state, not merely a cache entry token or attempted convergence. These
existing AX-05/14/15 constraints govern candidate contract interpretations even
where legacy APIs or algorithms simplify them.

## Candidate persistence structures

These are source-described structures, not a selected schema or technology
architecture. Structure names are retained to make competing designs reviewable.

| Structure / variant | Documented information and persistence responsibility | Standing / conflict |
| :--- | :--- | :--- |
| **Clinical/FHIR hybrid relational-document row** | `hie_fhir_resources`: surrogate `id`, type/logical ID, durable `version_id`, full `resource_json`, lifecycle flag and update timestamp; unique `(resource_type, fhir_id)`. Mnemosyne clinical slice. | L16 database-schema §§1–2 and L15 persistence. Describes one current row per resource; does not establish where complete historical versions are preserved. CDG-12. |
| **Per-version clinical row** | Same named table described with `(res_type, res_id, res_version)` or `(resource_type, resource_id, version_id)` key, resource text/JSON and security/update fields. | L16 persistence-lifecycle §4.1, L20 register. Incompatible key/history description retained; no “newest document wins” selection, CDG-12. |
| **HAPI-native FHIR persistence/index structures** | Clinical persistence/index/history responsibility described through HAPI JPA by L09/L18; L17 explicitly avoids default `HFJ_RESOURCE`/index tables in favour of custom rows. | A01 §4 prefers engine capabilities where suitable but does not settle these candidate structure variants. Technology detail remains downstream; candidate history/index architecture is unresolved, CDG-12. |
| **Non-FHIR operational current-row store** | `hie_operations_resources`: surrogate `id`, `(object_type, object_id)` uniqueness, version, full `data_json`, lifecycle/created/update fields. | L16 database-schema §2.2. Payload categories include telemetry/configuration; not all operational state merits durable retention. CDG-13/28. |
| **Operational history/sequence variants** | Same named table described with `(resource_type, resource_id, resource_version)` key (L16 lifecycle), or `(operation_id, sequence_id)` (L20 register). `hie_task_sequences`, TaskSequenceEntity and PragmaAuditEntity also appear in L09. | Candidate durable definition/progression/history structures conflict or lack field/lifecycle contracts. CDG-06/12/13; no separate component inferred from an entity name. |
| **Kleio evidence structures** | Immutable accepted audit/provenance; FHIR AuditEvent/Provenance in clinical store/cache versus operations audit/lineage rows/streams are variously described. | L13 ADR-013/016 preserve evidence intent. L08/L15/L16/L20 offer incomplete/incompatible storage allocations, CDG-13/14. No new audit database chosen. |
| **Agora mapping/transaction records** | `agora_resource_mappings`: Harmonia resource/identity to collaboration entity IDs, status/timestamps. `agora_as_transactions`: transaction ID and processing marker/timestamps. | L12 and L20 describe durable mappings/idempotency. Mnemosyne versus Agora Core JPA/Synapse database ownership unresolved, CDG-19/22. |
| **Homeserver discourse/room/membership store** | Collaboration implementation preserves room/state/event/membership data, distinct from formal Harmonia-managed clinical representation. | L12/L20; retention/submission/evidence boundaries insufficiently described, CDG-22/23. Homeserver database does not originate clinical authority. |
| **Petasos in-flight work/transition journal** | Opaque work records and recovery metadata associated with queue destinations, acknowledgement/redelivery/dead-letter state; designated transition points support replay. | L13/L29; message compaction is not governed resource deletion. Contract-specific recovery/duplicate/replay details remain CDG-16/17/19. |
| **Mneme active working structures** | Resource-keyed active payloads, task/Pragma/checkpoint working state, sequence definitions, observation/coordination records and telemetry working sets. | A01 governs reconstructability/non-authority. Legacy cache names/distribution/TTL/dedup/store lists differ (L09/L10/L17/L19/L20), CDG-27/31. Cache product is not the persistence architecture. |
| **Reference/terminology and binary/media structures** | Compiled semantic definitions and DocumentReference/resource payload metadata documented; service reference stores and binary durable objects not specified. | CDG-24/25. No inferred ValueSet database, object store, Media Server or attachment ownership from framework capability. |
| **Simulation state** | Seeded fixtures and ephemeral simulator/scenario data outside production dependencies. | L22. Kept as support context, not a production persistence tier. |

### Lifecycle and search limits

L13 ADR-020 distinguishes domain lifecycle updates from Mneme eviction, message
compaction, archival and physical disposal. Governed information has no normal
physical DELETE path; a relational `is_deleted` column is an existing schema
artefact, not a universal lifecycle semantic. Archival/purge/disposal are not
designed by this task.

L15 describes persisted-resource search/filtering and external FHIR searchset
Bundles; L09/L17 describe indexing support. Those descriptions do not establish
an internal managed search-result component, object identity, persistence schema,
ownership or retention contract. That candidate-documentation boundary remains
CDG-11/12/31; this reconstruction has not assessed Domain04 search coverage.

Historical technology names (Infinispan, PostgreSQL, HAPI FHIR, Artemis and
Synapse) explain source mechanisms. They neither create logical components nor
settle persistence responsibility, information authority, storage topology or
future product selection.
