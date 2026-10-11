<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Architecture Documentation / Component-Feature Gap Register

**Standing: gaps in the documented Harmonia Candidate Information Systems
Architecture itself.** [Orientation](README.md) governs. This register does not
ask which upstream capabilities, requirements or Information Concepts lack a
component. It contains no coverage score, MVP selection or implementation sequence.

Source keys link through [Sources](sources.md); affected C/F/D/M identifiers
resolve in the [component](component-register.md), [feature](candidate-feature-map.md),
[data](data-and-persistence.md) and [middleware](middleware.md) views. No issue
below reopens an established RADS responsibility. “Yes” indicates that selecting
or establishing missing architectural meaning appears to require human
adjudication; “Possibly” separates further documentary recovery from a decision.
It is not a request to adjudicate during reconstruction.

The [U02–U06 Step-A adjudications](sources.md#u02) partially resolve/reclassify
the affected entries below. Settled component boundaries and management/reuse
relationships are separated from remaining detailed design. The primary note's
earlier access issue was an input-access dependency, not an architectural gap.

| Gap ID / type | Affected construct / feature / relationship | Description and source evidence | Why candidate documentation is incomplete or ambiguous | Human adjudication appears necessary? |
| :--- | :--- | :--- | :--- | :--- |
| **CDG-01 — partially resolved / detailed Praxis contracts** | Praxis/Pathway/Journey; C06/C39/F11–12/36 | U06 establishes Praxis as higher-order expected/permitted operational and assurance envelope, owned by Energeia; working Aggregation of Pathway [0..1] and relevant Journey, with Journey cardinalities unresolved. | **Broader meaning, owner and assurance role resolved.** Detailed representation, historical definition-to-instance equivalences, versioning, activation/deactivation, replacement, retirement and in-flight treatment remain unresolved. No Composition inferred. | **Yes** for remaining detailed contracts; no re-adjudication of settled ownership/envelope/cardinality. |
| **CDG-02 — partially resolved / Pragma representation** | Pragma and payload; C07/D01–02/05/25 | U05 establishes contained/referenced payload such as Information Unit, distinct Pragma/Container/Content metadata, and Mneme-managed payload with identity/reference handoff through Petasos. L05/L19 retain historical carrier variants. | **Payload-management/handoff boundary and multi-level metadata meaning resolved.** Exact event/carrier relationships, containment/reference/version mechanics and Unit-level Container relationship to Domain04 partition remain unresolved. Zero-PHI logging does not prescribe payload representation. | **Yes/Possibly** for remaining representation contracts; no final schema derived from historical classes. |
| **CDG-03 — partially resolved / work, undertaking and runtime state** | Pragma, ActionableTask/FulfillmentTask, Petasos and Ponos; F34/D05/11 | U03/U05 preserve Mneme management and establish distinct consumer FulfillmentTasks for ActionableTask undertakings; separate managed-information, handoff, undertaking and execution state. Historical L01/L05/L15 state sets conflict. | **Ownership/state distinction and undertaking principle resolved.** Exact representation identities, lifecycle/status qualification, consumer morph/copy mechanics, concurrency and control contracts remain unresolved, with fan-out CDG-37. Domain04 outcome ownership and orthogonal controls remain established. | **Yes** for detail; no universal state machine or reopening of TaskOutcome/ReportedTask semantics. |
| **CDG-04 — partially resolved / factory contract** | Pragma Management / PragmaFactory; C28/C08/F34 | U01/U02 establish archetype-associated logical embodiment, Mneme Pragma Management allocation and creation/initialisation of appropriate Pragma representations. The archetype meta-concept is not itself an instantiated Information Asset | **Placement and basic responsibility resolved for the candidate.** Detailed parameters, selection/validation/policy contracts, definition Data Objects, callers and persistence remain unknown. No seeder/Praxis equivalence inferred | **Yes** for remaining detailed contracts; **no** re-adjudication of settled placement required. |
| **CDG-05 — partially resolved / behavioural contract contradiction** | Distinct Ergo/Ergon/Erga; converter/branch allocation; C01/C05/C23/C24/F02/07–10 | U02 separates conceptual activity contract, bounded component and meaningful collections. L03/L15/L18/L19 retain competing purity, atomicity, idempotence, state-changing and branch descriptions | **Construct distinction and common-platform reuse resolved for the candidate.** Scope of atomicity/side effects/idempotence, branch ownership and converter-support versus executed behaviour remain incomplete. No Ergon Information Resource is created | **Yes** for remaining behavioural/composition contracts; framework/class details cannot settle them. |
| **CDG-06 — partially resolved / definition lifecycle and control contracts** | Praxis, Pathway/Route, Publishing/Subscription/Pattern; D06/20/22–24 | U06 allocates Praxis Management Services to Energeia and semantic persistence to C&G; Mneme retains governed information management and Pub/Sub semantic definitions. | **Ownership and persistence domain direction resolved.** Detailed lifecycle/activation/version binding, reconstruction, control and in-flight effects of reload remain unresolved. Historical Ponos/Praxis topic matching is not semantic selection authority. | **Yes/Possibly** for detailed contracts; no topology, consistency or reload algorithm selected. |
| **CDG-07 — partially resolved / coordination detail** | DigitalTwin / Behaviour / specialised Pragma; C19/C25/F32/D17–18/M11 | U02 establishes Ponos-specialised DigitalTwin, Ergon-specialised Behaviour, per-Twin queue responsibility and candidate Stimulus/Effector Pragma specialisations, preserving A02 entity boundary | **Component/reuse boundary resolved for the candidate.** Only Twins with pending work become active under bounded resources; no thread per Twin. Detailed scheduling/activation/deactivation, continuing responsibility, state/persistence and Stimulus/Effector schemas/lifecycles remain unspecified | **Yes** for detail; settled specialisation and entity boundary are not reopened. |
| **CDG-08 — partially resolved / independence and assurance consumption** | Dokimasia/Praxis/Journey; C20/C26/C35/F33/38 | U02 settles reuse/terminal rules; U06 establishes Praxis assurance basis and Journey observed evidence subject, preserving A04 independence. | **Execution/activity boundaries, terminal rules and basis/subject roles resolved.** Independent control/evidence access, detailed consumption and assurance workflow contracts remain open. Journey cannot become inferred fact; detailed Praxis CDG-01. | **Yes** for remaining contracts; no recursive assurance or parallel platform inferred. |
| **CDG-09 — partially resolved / assurance information and evidence contracts** | Dokimasia Pragma and Journey interpretation; D12/17/19/21 | U02 establishes candidate assurance Pragma roles and terminal evidence constraint. U04 separates governed Journey observations, reasoned interpretation and findings/results. | **Assurance roles and observation/interpretation distinction resolved.** Names, schemas, lifecycles, representation/preservation and detailed evidence access remain unresolved; Journey does not manufacture an assurance Information Family or TaskOutcome identity. CDG-35/38 retain AI/evidence detail. | **Yes** for remaining contracts, preserving upstream deferrals. |
| **CDG-10 — contradiction / persistence** | Mneme cache/write-behind as “durability”; C09/C12/F17/D05 | L09/L10/L01/L16/L19 treat cache checkpointing/replication as durability or completion before async persistence. A01 AX-05 and L13 ADR-018 reject that acceptance authority. | Source-described recovery/continuation features lack a conforming durable boundary. The controlling state responsibility is settled; the candidate cache/write-behind story remains historically inconsistent. | **Possibly** for remaining solution contracts; **no** adjudication needed to decide whether cache is durable authority. |
| **CDG-11 — contradiction / relationship** | Application information access and CLI paths; C09/C11/C13/C15/C17/C22 | L11/L21/L23 describe raw-cache access; L15 read/search and L18 FHIR/Erga route directly to FhirStorageService; operations CLI accesses persistence endpoints. A01/A02 require Mneme-facing governed access. | The legacy service collaborations do not consistently explain the governed application access boundary. Preserving it does not invent replacement APIs or classify every administration operation as an application query. | **Possibly** for exact contracts/tool scope; authoritative access responsibility is already settled. |
| **CDG-12 — contradiction / historical persistence structures** | Clinical/operational structure variants, history/indexing/search; C12/F18–19 | L16/L20 use incompatible keys/history models; L09/L18 versus L17 differ on HAPI-native structures. U06 replaces two-domain semantic classification with Clinical/C&G/O&A. | Historical-version preservation/index/retrieval structures remain unresolved. **Semantic persistence domains and scaling guardrails are established**, without reconciling physical structures or changing clinical architecture; downstream implementation CDG-40. | **Yes** for unresolved architecture contracts where needed; no schema/product selection here. |
| **CDG-13 — contradiction / persistence / relationship** | Kleio, Themis, Mnemosyne evidence versus operations records; C02/C03/C12/D12–13 | L13 ADR-013 assigns immutable evidence to Kleio; L08/L09/L16/L20 offer competing audit/provenance/history allocations. U02 separately establishes Dokimasia assurance record/result as evidence of its activity | Detailed model/custody, storage and mutable operational records remain incompletely separated. Dokimasia evidence role and no-separate-own-execution-AuditEvent rule are settled; neither rule specifies a schema/repository or makes a custodian the evaluator | **Yes** for detailed evidence/persistence collaborations, not settled evidence roles or terminal rules. |
| **CDG-14 — partially resolved / observation versus audit** | Checkpoints / Journey / AuditEvent / findings; F06/15/38/M07/15 | AX-08/09 and U02 terminal rules reject universal durable checkpoint auditing. U04/U05 establish multi-responsibility Journey evidence distinct from operational observation, AuditEvent and assurance finding. | **No universal observation-to-AuditEvent requirement; canonical Journey role resolved.** Significant-evidence promotion, retention and detailed observation/evidence relationships remain unresolved CDG-38. Dokimasia own-execution AuditEvent and recursive assessment remain prohibited. | **Possibly** for general policies; settled evidence distinctions/terminal rules not reopened. |
| **CDG-15 — contradiction / Data Object / persistence** | Security context, FHIR Task extensions and external projection; D03/05 | L08 security concept §3 and L18 serialize caller context/authorities into Task extensions. A01 AX-07/13 and L14 reuse canonical operational context without automatic content persistence. | Transport/context versus durable resource content and fail-closed external projection are not consistently described. A serialized carrier does not establish persistable authority or permission to export context. | **Possibly** for transport/retention contracts; governing prohibition already established. |
| **CDG-16 — partially resolved / handoff and execution continuation** | Mneme selection → Petasos obligation → consumer undertaking → Ponos; M01/03/10/13 | U05 establishes persistent identity/reference handoff for Pragma with payload through Mneme and distinct state responsibilities. L01/L02 ACK/reject and L29 guarantees remain historical incomplete contracts. | **Responsibility boundary resolved.** Acceptance/ACK timing, uncertain effects, safe execution continuation, reference availability/version binding and recovery evidence remain unresolved. Technical ACK/exception does not establish commit, undertaking completion or work resolution. | **Yes** for missing continuation/recovery contracts; no safe retry algorithm invented. |
| **CDG-17 — partially resolved / execution retry, redelivery and replay** | Ponos execution re-attempt; Petasos redelivery/recovery/dead-letter; F05/14/M02 | U05 separates execution/progression from handoff recovery; L13 ADR-015 restricts deliberate replay to designated durable transitions, while L19 tool examples mix cached/DLQ re-drive and replay. | **Ownership/conceptual distinction resolved.** Retry qualification, duplicate/redelivery/replay identity, new-attempt linkage, permitted anchors and uncertain-effect reconciliation remain unresolved. No automatic exactly-once or safe re-execution contract. | **Yes/Possibly** for detailed contracts/tool semantics; not the settled responsibility distinction. |
| **CDG-18 — partially resolved / destination observation and parent progression** | Petasos handoff state versus Pragma/Task observations; F10/D10/Journey | U05 gives Petasos operational handoff/delivery obligation ownership; governed information may record observations. L03/L30/L28 and REC-002 establish destination granularity, with competing completion descriptions. | **Operational ownership versus observation distinction resolved.** Exact Petasos-to-Pragma/Task relationships, combination of observations, parent completion, partial/unknown delivery and outcome resolution remain unresolved. No state mirror or TaskOutcome algorithm inferred; dynamic fan-out CDG-37. | **Yes** for representation/progression detail; REC-002 is preserved. |
| **CDG-19 — partially resolved / duplicates and uncertain effects** | Petasos handoff duplicate recovery; consumer execution and Agora partial transactions | U05 allocates persistent handoff recovery to Petasos, separate from Ponos execution. L29 sliding windows and L12 transaction publish/marker sequence do not establish effect idempotence. | **Handoff versus execution responsibility resolved.** Partial publication, lost responses, marker failure, retention-window expiry and duplicate/reference/undertaking identity remain unresolved. Transport duplicate detection is not business-effect idempotence. | **Yes/Possibly** after further contract review; no exactly-once architecture inferred. |
| **CDG-20 — contradiction / component** | Agora/Harmonia universal clinical authority and workflow claims; C18 | L12 §2 calls Harmonia single source of truth for clinical workflows/information; L18/L26 use broad clinical-process language. A01 AX-06/18 and L13 ADR-007 preserve external/source authority. | Candidate projection/information processing is conflated with clinical-work management or originating fact authority. Broad claims are retained as historical contradictions, not adopted responsibilities. | **No** to reopen established authority boundary; **possibly** to clarify bounded candidate behaviours. |
| **CDG-21 — relationship / Data Object** | Care-team roster source for Agora membership; F22/D14 | L12 §8 says Provider Registry in Mnemosyne authoritatively governs care-team membership; L15 registry scope/resource model supplies directory references/Group, not a complete roster ownership contract. | Directory representation and collaborative membership do not establish the authoritative roster/source service, context, freshness or governing membership decision. | **Yes** if further documentation does not establish that relationship; no new upstream owner inferred. |
| **CDG-22 — contradiction / persistence** | Agora mappings/transactions versus homeserver stores; C18/D14 | L12 §§4/5 allocates durable mappings to Mnemosyne; §3 describes Agora Core JPA; L20 persistence chapter/register lists Synapse and Agora as store owners. | Information management, authoritative persistence port and independent discourse storage are not consistently separated. Direct JPA/module placement cannot settle ownership. | **Yes** unless these are documented distinct variants; no database/platform allocation made. |
| **CDG-23 — orphan feature / relationship** | Statistics/Tasks/Diagnostics projections and clinical submission; F22/F24 | L12 §§2/6 describes summaries, workflow notifications and committing clinical decisions/outputs before discourse purge. No consistent providing-component/submission contract is supplied in that source. | Projection producers, permitted disclosure, formal-record handoff and authority/retention responsibility are insufficiently defined. Room names do not establish content services or clinical-work authority. | **Possibly** after recovering feature/producer contracts; adjudication for absent responsibility boundaries. |
| **CDG-24 — feature / orphan feature / persistence** | Terminology/reference services and conditional authoring; C01/F25/D16 | A02 establishes semantic definitions/bindings; L17 uses compiled value sets and excludes standalone terminology server; L27 proposes lookup/search/expansion/validation before clinical authoring. | Reference publication/version/persistence and runtime service allocations are incomplete; existing bindings neither implement nor invalidate proposed service features. No product/feature scope chosen. | **Possibly**; documentary detail first, then missing architectural service/ownership decisions. |
| **CDG-25 — persistence / relationship / Data Object** | Media/binary management; F31/D15 | L19 Ergon payload includes clinical binary attachments; L10/L17/L23 name DocumentReference support/cache. Broad discovery found no consistent Media Server or equivalent contract. | Payload/metadata support does not establish durable binary custodian, access/link integrity, content lifecycle, authorization, media structures or recovery. No blob product or server component invented. | **Possibly**; source recovery or explicit bounded architecture decision needed. |
| **CDG-26 — contradiction / feature** | Clinical presentation versus diagnostic PHI boundary; C15–17/F26–27 | L11/L18/L23 describe clinical record/timeline views; L21 design-system safety wording limits presentation/diagnostic identifiers. AGENTS Invariant 7 governs non-clinical logs. | Reusable diagnostic control/logging rules and clinical presentation permissions are not consistently distinguished. No ban on authorised clinical display is inferred from zero-PHI diagnostics. | **Possibly** for view/projection contracts; established logging prohibition stays intact. |
| **CDG-27 — partially resolved / working structures and retained variants** | Mneme information, Petasos obligations, Ponos execution and definition working sets | U05 separates state responsibilities and Topic from destination. L09/L10/L17/L19/L20 retain competing cache/dedup/TTL/write-behind catalogues. | **Semantic state/selection ownership resolved.** Working-set reconstructability, retention, concrete representation and coordination contracts remain incomplete beneath historical technology lists. No cache or queue topology is selected. | **Possibly** for representation/detail; no technology catalogue may override settled ownership. |
| **CDG-28 — partially resolved / operational reporting and tools** | Energeia, Journey, Iris/support; C39/C22/C35/F27/38/39 | U06 assigns Harmonia operational observation/monitoring/reporting and Journey ownership to Energeia; Iris presents; external IT/Ops manages technical platform. | **Owner, two management planes and R1.x/R2.x Observe → Monitor → Report boundary resolved.** Provider freshness/failure, thresholds, retention/projection/consumption and command authority remain detailed contracts CDG-38/39. Absence of observation is not non-occurrence. | **Yes/Possibly** for detail; no autonomous adaptation or UI-derived hierarchy. |
| **CDG-29 — contradiction / feature / component** | Patient identity update versus EMPI/master-patient engine; F08 | L18 execution chapter explicitly reconciles MRNs to master patient IDs using an EMPI in Mneme; L13 ADR-007 and A01 AX-06/18 preserve source authority. Other Erga/converter sources describe identifiers/demographics without that authority. | Candidate identity correlation/referential checking and master identity determination are conflated; no consistent separate EMPI component/boundary is established. Do not invent one or silently remove the historical claim. | **Yes** for any proposed master-identity responsibility; source-authority guardrails remain settled. |
| **CDG-30 — contradiction / relationship** | Fixed security-gate catalogues; C02/F03/M07 | L08 Themis concept has Pylai/Petasos/Energeia/Hestia gates; its security companion has Pylai/Ponos/Ergon/Mnemosyne; L01 dispatcher separately evaluates requester and executor. | Enforcement placement/count, caller-versus-executor permission and gate coverage are not one consistent candidate collaboration contract. Default-deny remains established regardless of gate count. | **Possibly** after recovering participating-boundary contracts; no transport authentication invented. |
| **CDG-31 — Data Object / relationship / feature** | Active observation/generation, result/absence and search-result contracts; C09/C11/F16/18 | L14 warm read uses entry metadata as token and older convergence algorithms; A01 AX-05 requires valid generation of successfully established state. L15 describes external resource searchset, not internal result management. | Existing candidate access contracts do not establish all token/generation/manifestation distinctions or internal search-result identity/retention/ownership. This is documentary insufficiency, not Domain04 coverage analysis. | **Possibly** for missing application contracts; no upstream semantics changed. |
| **CDG-32 — contradiction / external-boundary dependency** | Registry status/async response projections; C13/C14/F20–21 | L15/L18 describe HTTP 202 and Task polling for writes, direct synchronous store search and OperationOutcome/status conversion; A01 AX-02/13 requires applicable standards contract and private-semantic isolation. | Candidate internal request/change/status responsibilities and externally permitted projections are insufficiently separated. Inclusion does not approve protocol details; Domain06 contract review remains future work. | **Yes** if contract conflicts persist; no external integration redesign here. |
| **CDG-33 — orphan feature / component / Data Object** | Local Directory sync/enrichment and EndpointCapability; F30 | L24 §§5–9/13 proposes shared-component ingestion/synchronisation/enrichment and distinguishes endpoint from capability; detailed reconciliation/allocation remains expressly open there. | No coherent candidate component/service/state/persistence contract is established for those proposed features. Retained proposal is not an upstream requirement gap or permission to implement its waves. | **Possibly** after source reconciliation; explicit responsibility decisions may be necessary. |
| **CDG-34 — middleware / relationship** | Ingress acceptance/cache/publish and external dispatch uncertainty; C10/C14/M01 | L30 REC-001 requires cache and publish before AA; L26 says “durably persisted and enqueued”; L13 ADR-018 allows durable work acceptance with reconstructable state, distinct from committed information. | Failure/partial handoff and required recoverable context are not consistently documented. Cache success alone is insufficient; upstream acceptance, internal commit and external delivery observation cannot be one state. | **Yes** for complete transition/effect contracts; REC-001 is preserved without production changes. |
| **CDG-35 — established component / detailed architecture discovery** | AI Assurance Ergon; C27/F35/D17/19/M12 | U02 §§9/12 establishes discrete AI-based Dokimasia activity componentry as a candidate Dokimasia Ergon specialisation | **Component existence and discrete responsibility established; detailed Information Systems architecture unresolved.** Discovery below precedes final implementation modelling. This is not evidence that the boundary is unjustified, an upstream coverage gap or permission to select technology now | **Yes** for detailed architecture after discovery; settled existence/responsibility is preserved. |
| **CDG-36 — established responsibility / detailed selection contracts** | Publishing/Subscription/Pattern/Topic; C36–38/F37/D07/22–25/M13 | U05 settles asymmetric provider filtering/consumer selection, multi-level Topic metadata and Mneme semantic management. | Definition lifecycles/registration/discovery, supported metadata, language/operators/precedence, evaluation/error/security/disclosure and durable representation remain unresolved. Technology/index/algorithm/loop controls are not selected. | **Yes** for missing detailed architectural contracts where needed; implementation guardrails do not automatically become upstream gaps. |
| **CDG-37 — partially resolved / Pub/Sub fan-out** | Common Pragma, multiple Petasos obligations, consumer FulfillmentTasks; D05/11/F37 | U05 §§11–13/21 establishes common managed Pragma/payload and potential multiple reference handoffs; each consumer undertaking ActionableTask establishes a distinct FulfillmentTask. | **Principle partly settled; reference/copy/version and morph mechanics, Pragma identity, targetDestination, processing/concurrency/isolation, lineage, Journey correlation and assurance remain unresolved.** No copy-per-subscriber or shared mutable state inferred. | **Yes** for remaining identity/lifecycle contracts; established undertaking principle preserved. |
| **CDG-38 — partially resolved / detailed Journey contracts** | Journey; C35/C39/D21/F38/M15 | U06 resolves Energeia ownership/Journey Management Services and Mnemosyne O&A responsibility; U04/U05 observation and graph possibility retained. | Detailed representation/construction, aggregation/reference schema, PathwayElement relationships, evidence/causal/correlation/reconstruction algorithms, inference distinction, lifecycle/retention and consumption remain unresolved. Ownership transfers no referenced-information authority; Journey is not a shard key. | **Yes** for detailed contracts; settled ownership/basis-subject distinction preserved. |

| **CDG-39 — established responsibility / detailed Energeia decomposition** | Energeia C39, Praxis/Journey Management Services, operational observation/monitoring/reporting; F11/38–39 | U06 establishes discrete component and direct application responsibilities. | Feature versus Service versus internal construct versus subordinate Component decomposition, precise contracts/monitoring thresholds and remaining Praxis-to-activity relationships remain unresolved. Possible upstream omission of operational housekeeping demand has not been assessed; no requirement/capability or Business Architecture is derived. | **Yes/Possibly** for detailed reconciliation; possible upstream omission requires later bounded review, not an asserted coverage gap. |
| **CDG-40 — established persistence direction / downstream physical design** | Mnemosyne Clinical/C&G/O&A; C12/F19/M16 | U06 establishes three semantic domains, coherent C&G authority and O&A horizontal partitionability preserving identity/provenance/relationships/meaning. | C&G physical persistence/topology, O&A physical partition/shard model and key, replication/consistency mechanisms deliberately unresolved. No multi-shard R1.x/R2.x requirement; Journey locality is an objective. | **Later design**, not missing high-level architecture or permission to select technology/keys now. |
| **CDG-41 — deliberately deferred R3 direction** | Adaptive Load and Resilience Service; F40/C39 | U06 records future Observe → Analyse → Decide → Adapt direction, with feedback. | R3 policy/control architecture, adaptation/load/resilience algorithms/mechanisms, infrastructure integration and AI decision model deliberately deferred. R1.x/R2.x remains Observe → Monitor → Report. | **Future bounded R3 work**, not an R1.x/R2.x gap or current design obligation. |

<a id="dokimasia-ai-discovery"></a>

## CDG-35 — Dokimasia AI Information Systems Architecture discovery

The existence and discrete responsibility of AI-based Dokimasia componentry are
established by U02. AI Assurance Ergon represents AI processing as an explicit
governed activity within Dokimasia workflow, distinct from ordinary Ergon
internally invoking AI as an implementation choice. Its detailed Information
Systems architecture is not finalised; discovery SHALL precede final implementation
modelling. This obligation does not allocate all Harmonia AI use to this construct.

Discovery must address at least:

- AI model/service selection and abstraction;
- context/input construction;
- information disclosure and security;
- model/service provenance and versioning;
- confidence, uncertainty and result representation where applicable;
- deterministic versus non-deterministic behaviour;
- reproducibility requirements and limitations;
- result validation;
- human review/escalation;
- assurance evidence representation;
- Dokimasia workflow integration;
- failure, timeout and service-unavailability behaviour; and
- reporting/observability consistent with no recursive Dokimasia monitoring,
  assurance or assessment and no separate AuditEvent for its own execution.

These subjects remain unresolved. No model/provider, agent framework, SDK,
service technology, evidence schema, confidence scale or workflow/lifecycle model
is selected by this correction. Discovery must reuse the common platform and
preserve independent assurance, security, uncertainty and originating information/
domain authority. Other possible assurance Ergon classifications require separate
adjudication; deterministic/human subclasses are not established by symmetry.

## Retained uncertainty and disposition

The high-level Mneme/Mnemosyne, Themis/Kleio, Pylai publication, Twin coordination,
Ergon behavioural and assurance independence boundaries are not unresolved merely
because their detailed candidate realisation is incomplete. Rows distinguish
conflicting historical statements from genuinely absent solution contracts.

ReportedTask and reserved TaskOutcome details remain Domain04 deferrals; they
are not automatically Domain05 blockers or a requested adjudication. No row
declares unsupported upstream capability/information coverage. The previous
assessment's Q01–Q05 may overlap these evidence-backed issues, but supplies
neither authority nor a work sequence.

Resolution and prioritisation of CDG issues require subsequent bounded work.
U02–U06 correct only the affected Step-A candidate meanings recorded above.
Praxis's higher-order meaning and Energeia Praxis/Journey ownership are resolved;
detailed lifecycle/representation and downstream designs remain unresolved.
This register proposes no implementation plan, dependency schedule or automatic
“next issue”; Domain05's separately authorised traceability phase remains future.

<a id="pubsub-fanout"></a>

## CDG-37 — Retained Pub/Sub fan-out identity and lifecycle uncertainty

[The bounded fan-out decision](pathways-journeys-and-handoff.md#fan-out-boundary)
partially settles the question from the supplied DesignNote; detailed contracts
remain open. Common managed Pragma/payload and distinct ActionableTask consumer
undertakings do not establish copies, shared mutable processing state, reference
version binding, targetDestination population, lineage/causation or Journey
reconstruction. Domain04 concurrency, outcome participation/output controls and
TaskOutcome ownership remain authoritative and unchanged.

<a id="journey-contracts"></a>

## CDG-38 — Journey representation, construction and interpretation contracts

[Journey's meaning and assurance role](pathways-journeys-and-handoff.md#journey-and-assurance)
are adjudicated. **Energeia owns Journey and Journey Management Services** under
U06; Mneme manages governed information and Mnemosyne O&A supplies durable
persistence responsibility. Detailed construction/representation, aggregation/
reference schema, causal/correlation/evidence-reconstruction algorithms, lifecycle/
retention and reporting/assurance consumption contracts remain unresolved.
Contributors retain information ownership; small discrete Journey information
references substantial activity/evidence without indiscriminate duplication.
Observed versus inferred relationships remain distinguishable. Efficient Journey
reconstruction/use is an objective, not a schema or shard-key decision.


<a id="persistence-realisation"></a>

## CDG-40 — Persistence guardrails and deferred physical realisation

[The semantic persistence domains](data-and-persistence.md#persistence-responsibility-domains)
are established architecture direction, not an unresolved partitioning principle.
C&G remains coherent without independently divergent governance authority; O&A
permits horizontal partitioning while preserving information meaning. Physical
implementation/topology, shard model/key, replication and consistency mechanisms
remain downstream. Historical structures are retained variants, not technology
choices. Journey locality/reconstruction efficiency is a consideration without
established partition-key status. Clinical persistence is not redesigned.

<a id="r3-adaptive-direction"></a>

## CDG-41 — R3 Adaptive Load and Resilience direction

The direction is explicitly deferred; its absence from R1.x/R2.x is intentional.
Observability should permit later adaptive capability without introducing automatic
workload redistribution, route/Pathway/PubSub/Praxis modification, resilience
intervention or capacity adjustment now. No policy, algorithm, control-loop,
infrastructure-integration or AI decision architecture is specified by this task.
