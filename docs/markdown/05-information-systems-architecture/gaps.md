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

The [U02–U12 Step-A adjudications](sources.md#u02) partially resolve/reclassify
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
| **CDG-13 — partially resolved / historical evidence-persistence variants** | Kleio, Themis, Mnemosyne; C02/C03/C12/D03/12–13 | U07 establishes Kleio authoritative Audit Information Management, immutable FHIR AuditEvent and Mneme/O&A durability; U08 establishes Themis model/PRP ownership with possible Mneme/C&G durability. L08/L09/L16/L20 retain competing historical allocations. | **Application ownership and semantic persistence alignment resolved.** Supporting/prepared views differ from authoritative evidence. Historical FHIR/operations structures, privacy boundaries and concrete preservation mechanisms remain unresolved CDG-40/43; no schema/database selected. Dokimasia evidence role and terminal rules remain. | **Yes/Possibly** for detailed contracts and retained historical variants; settled ownership/alignment not reopened. |
| **CDG-14 — partially resolved / observation versus authoritative audit** | Checkpoints / Journey / AuditEvent / findings; F06/15/38/M07/15 | U07 establishes Kleio Capture independently of Journey: Journey may relate to many AuditEvents; AuditEvent may exist without Journey. AX-08/09 rejects indiscriminate audit and U02 preserves terminal rules. | **Journey is an audit source, not audit boundary; no Journey prerequisite and no universal observation-to-AuditEvent rule.** Required significant audit capture is authoritative/durable and essential, without later reconstruction assumptions. Detailed policies, retention and source/evidence relationships remain CDG-38/43. | **Possibly** for detailed policies; settled evidence distinctions and Dokimasia terminal rules not reopened. |
| **CDG-15 — retained historical contradiction / context representation** | Security context, FHIR Task extensions and external projection; D03/05 | L08 security concept §3 and L18 serialize caller context/authorities into Task extensions. AX-07/13, U08/U12 and L14 distinguish operational context from persisted content. L08 §3 is now explicitly superseded. | Automatic context persistence is rejected; a serialised carrier establishes neither persistable authority nor permission to export context. Detailed transport/retention and historical L18 relationships remain unresolved under CDG-42. | **Possibly** for remaining transport/retention contracts; no automatic persistence or external projection permission. |
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
| **CDG-27 — partially resolved / working structures and retained variants** | Mneme Context/Pragma, Petasos obligations, Ponos execution and definition working sets | U10 establishes Context/Pragma responsibility, four-service surface, temporal composed values and non-exhaustive expected sets; U05 state/selection boundaries retained. L09/L10/L17/L19/L20 cache catalogues remain historical. | **High-level working Context/Pragma meaning resolved.** Physical representation/distribution/reconstruction/retention and coordination remain CDG-45; no cache or queue topology/TTL/write-behind authority selected. | **Possibly** for detailed representation; technology catalogue cannot override settled semantics. |
| **CDG-28 — partially resolved / operational reporting and tools** | Energeia, Journey, Iris/support; C39/C22/C35/F27/38/39 | U06 assigns Harmonia operational observation/reporting and Journey ownership to Energeia; Iris presents; external IT/Ops manages technical platform. | **Owner, two management planes and R1.x/R2.x Observe → Report boundary resolved.** Provider freshness/failure, observation/reporting detail, retention/projection/consumption and command authority remain detailed contracts CDG-38/39. Absence of observation is not non-occurrence. | **Yes/Possibly** for detail; no autonomous adaptation or UI-derived hierarchy. |
| **CDG-29 — contradiction / feature / component** | Patient identity update versus EMPI/master-patient engine; F08 | L18 execution chapter explicitly reconciles MRNs to master patient IDs using an EMPI in Mneme; L13 ADR-007 and A01 AX-06/18 preserve source authority. Other Erga/converter sources describe identifiers/demographics without that authority. | Candidate identity correlation/referential checking and master identity determination are conflated; no consistent separate EMPI component/boundary is established. Do not invent one or silently remove the historical claim. | **Yes** for any proposed master-identity responsibility; source-authority guardrails remain settled. |
| **CDG-30 — partially resolved / fixed gate catalogues** | Themis distributed PEP and participating hosts; C02/F03/M07 | U08 places enforcement at the component capable of preventing the action, with Themis-owned capability and PEP-Criteria through PEP-Context; historical L08/L01 fixed gate/count variants remain provenance. | **Enforcement ownership/placement principle resolved**, including Petasos governed FORWARD. Exact participating-boundary contracts, caller/executor context relationships and legacy gate reconciliation remain unresolved CDG-42. Complete Authorisation Model is not distributed merely for local enforcement. | **Possibly** for detailed host contracts; default-deny remains, no central PEP or transport authentication invented. |
| **CDG-31 — partially resolved / observation and search-result contracts** | Mneme Context/access, Mnemosyne search; C09/C11/C12/F16/18/43 | U10/U11 establish four Mneme services, Context/resource version distinction and authoritative persistence search behind Adhoc Search. L14 token/convergence and L15 external searchset variants remain historical. | **Surface/search ownership and authoritative search space resolved.** Exact active-generation/token/Context mapping, internal search-result identity/retention and completeness/consistency contracts remain unresolved CDG-45/48. No cache-completeness inference or upstream coverage analysis. | **Possibly** for detail; no Domain04 semantics changed. |
| **CDG-32 — contradiction / external-boundary dependency** | Registry status/async response projections; C13/C14/F20–21 | L15/L18 describe HTTP 202 and Task polling for writes, direct synchronous store search and OperationOutcome/status conversion; A01 AX-02/13 requires applicable standards contract and private-semantic isolation. | Candidate internal request/change/status responsibilities and externally permitted projections are insufficiently separated. Inclusion does not approve protocol details; Domain06 contract review remains future work. | **Yes** if contract conflicts persist; no external integration redesign here. |
| **CDG-33 — orphan feature / component / Data Object** | Local Directory sync/enrichment and EndpointCapability; F30 | L24 §§5–9/13 proposes shared-component ingestion/synchronisation/enrichment and distinguishes endpoint from capability; detailed reconciliation/allocation remains expressly open there. | No coherent candidate component/service/state/persistence contract is established for those proposed features. Retained proposal is not an upstream requirement gap or permission to implement its waves. | **Possibly** after source reconciliation; explicit responsibility decisions may be necessary. |
| **CDG-34 — middleware / relationship** | Ingress acceptance/cache/publish and external dispatch uncertainty; C10/C14/M01 | L30 REC-001 requires cache and publish before AA; L26 says “durably persisted and enqueued”; L13 ADR-018 allows durable work acceptance with reconstructable state, distinct from committed information. | Failure/partial handoff and required recoverable context are not consistently documented. Cache success alone is insufficient; upstream acceptance, internal commit and external delivery observation cannot be one state. | **Yes** for complete transition/effect contracts; REC-001 is preserved without production changes. |
| **CDG-35 — established component / detailed architecture discovery** | AI Assurance Ergon; C27/F35/D17/19/M12 | U02 §§9/12 establishes discrete AI-based Dokimasia activity componentry as a candidate Dokimasia Ergon specialisation | **Component existence and discrete responsibility established; detailed Information Systems architecture unresolved.** Discovery below precedes final implementation modelling. This is not evidence that the boundary is unjustified, an upstream coverage gap or permission to select technology now | **Yes** for detailed architecture after discovery; settled existence/responsibility is preserved. |
| **CDG-36 — established responsibility / detailed selection contracts** | Publishing/Subscription/Pattern/Topic; C36–38/F37/D07/22–25/M13 | U05 settles asymmetric provider filtering/consumer selection, multi-level Topic metadata and Mneme semantic management. | Definition lifecycles/registration/discovery, supported metadata, language/operators/precedence, evaluation/error/security/disclosure and durable representation remain unresolved. Technology/index/algorithm/loop controls are not selected. | **Yes** for missing detailed architectural contracts where needed; implementation guardrails do not automatically become upstream gaps. |
| **CDG-37 — partially resolved / Pub/Sub fan-out** | Common Pragma, multiple Petasos obligations, consumer FulfillmentTasks; D05/11/F37 | U05 §§11–13/21 establishes common managed Pragma/payload and potential multiple reference handoffs; each consumer undertaking ActionableTask establishes a distinct FulfillmentTask. | **Principle partly settled; reference/copy/version and morph mechanics, Pragma identity, targetDestination, processing/concurrency/isolation, lineage, Journey correlation and assurance remain unresolved.** No copy-per-subscriber or shared mutable state inferred. | **Yes** for remaining identity/lifecycle contracts; established undertaking principle preserved. |
| **CDG-38 — partially resolved / detailed Journey contracts** | Journey; C35/C39/D21/F38/M15 | U06 resolves Energeia ownership/Journey Management Services and Mnemosyne O&A responsibility; U04/U05 observation and graph possibility retained. | Detailed representation/construction, aggregation/reference schema, PathwayElement relationships, evidence/causal/correlation/reconstruction algorithms, inference distinction, lifecycle/retention and consumption remain unresolved. Ownership transfers no referenced-information authority; Journey is not a shard key. | **Yes** for detailed contracts; settled ownership/basis-subject distinction preserved. |
| **CDG-39 — established responsibility / detailed Energeia decomposition** | Energeia C39, Praxis/Journey Management Services, operational observation/reporting; F11/38–39 | U06 establishes discrete component and direct application responsibilities. | Feature versus Service versus internal construct versus subordinate Component decomposition, precise observation/reporting contracts and remaining Praxis-to-activity relationships remain unresolved. Possible upstream omission of operational housekeeping demand has not been assessed; no requirement/capability or Business Architecture is derived. | **Yes/Possibly** for detailed reconciliation; possible upstream omission requires later bounded review, not an asserted coverage gap. |
| **CDG-40 — established persistence direction / downstream physical design** | Mnemosyne Clinical/C&G/O&A; C12/F19/42/M16/18 | U06 domains/scaling guardrails retained; U11 establishes Mnemosyne shard management within semantic constraints, not independent semantic sharding permission. | Physical topology, shard model/key/routing, replication/consistency mechanisms remain unresolved; U07 independent audit targets imply no general O&A topology and cross-target harmonisation is SEP. No multi-shard requirement; Journey locality an objective. | **Later design**, without choosing technology/keys or reopening established semantic domains. |
| **CDG-41 — deliberately deferred R3 direction** | Adaptive Load and Resilience Service; F40/C39 | U06 records future Observe → Analyse → Decide → Adapt direction, with feedback. | R3 policy/control architecture, adaptation/load/resilience algorithms/mechanisms, infrastructure integration and AI decision model deliberately deferred. R1.x/R2.x remains Observe → Report. | **Future bounded R3 work**, not an R1.x/R2.x gap or current design obligation. |
| **CDG-42 — established Identity/Themis architecture / detailed contracts and retained conflict** | Authentication SEP; Calliope nomenclature; Themis model/PAP/PRP/PDP/PEP-Criteria/PEP-Context/PEP; C01/C02/F01/F03/D03/M07 | U12 refines U08 with conjunctive Criterion/additive UNION semantics, nested collections, AuthorisationRole, PractitionerCapacity, distinct temporal validity/revocation, deterministic precomputed enforcement, untagged-information control, FORWARD provenance and Ergo/Praxis/Iris administration. | [Explicit deferred items](#identity-deferred-design) cover release contents, expressions/representations, invalidation, attribute derivation, Capacity mapping/UI, PAP work, forwarding and caching/distribution. No general policy language remains open. **U08 note §15 audit-persistence exception conflicts with AX-07; not adopted.** [Details](#themis-detailed-contracts). | **Mark + ChatGPT adjudication** for the exception conflict; later bounded design/discovery for mechanisms; no reopening of settled Identity semantics. |
| **CDG-43 — established Kleio architecture / detailed capture and preservation** | Kleio Capture/AuditEvent/history; C03/F15/D12/M07/16 | U07 resolves authoritative audit management, immutable FHIR AuditEvent, Journey independence, longitudinal evidence, essential durable capture, independent targets and R1.x/R2.x harmonisation SEP. | API/library/mapping, evidence attachment, indexing/query/correlation/preparation, retention/legal hold, originating activity/capture transaction, target selection/write strategy, retry/failover/readiness and physical design unresolved. Harmonisation is excluded release scope, not a missing R1.x/R2.x guarantee. [Details](#kleio-detailed-contracts). | **Later bounded design**, without weakening settled essential capture or adding convergence. |
| **CDG-44 — established Petasos architecture / detailed Parcel and queue contracts** | Purpose-defined queues / PetasosParcel / FORWARD; C10/F13/D11/M01 | U09 resolves queue creation/operation/status ownership, common by-value Parcel, predominantly referenced associated information, ActionableTask special case and Themis-owned local enforcement. | Taxonomy/names/purpose-registration, Parcel schema/reference/content and historical carrier relationships, topology/technology/persistence/partitioning/scaling, metrics/state, ordering/delivery/consumer coordination/recovery/dead-letter and Petasos/Themis/Kleio/Journey interactions unresolved. [Details](#petasos-detailed-contracts). | **Later bounded design**; no reference-only transport, FHIR equivalence, broker mechanism or new capability inferred. |
| **CDG-45 — established Context/Pragma architecture / detailed contracts** | Mneme four services, Context/Pragma Output and Ergo boundary; C09/C11/C05/C23/F16–18/D26–28 | U10 establishes value Composition, temporal/version distinction, likely non-exhaustive resource set, supported Adhoc acquisition and Loader/Unloader versus Load/Save. | Physical Context/bucket/identity/version schema, Information Unit mapping, declaration/acquisition strategy, concurrency/conflict detection, API/query/result and Output routing/representation remain unresolved. New Content not mere Context mutation. [Detail](#context-and-pragma-contracts). | **Later bounded adjudication/design**; no fifth service, universal snapshot or algorithm inferred. |
| **CDG-46 — established exceptional harmonisation / detailed work contracts** | Internal Mneme RHM C40/F41/M17 | U10 establishes exceptional-path design expectation of approximately 5% or less at worst, new governed Harmonisation Ergon work and reuse of Ponos via established contract. | Categories/detection, harmonisation algorithms/catalogue, narrow work-contract detail, repeated-harmonisation terminal/escalation and Ergo Library detail unresolved. No originating-Ergon resume or duplicate execution framework. [Detail](#harmonisation-contracts). | **Later bounded design**; percentages not SLA; no UI/forms design or Kleio cross-target harmonisation obligation. |
| **CDG-47 — established Persistence Manager / detailed oversight** | Mnemosyne C12/F42/M18 | U11 establishes transaction/service oversight, shard management, authoritative reporting/logging, compartment validation/reapplication and Themis-owned PEP hosting. | Transaction/outcome/status/rollback/retry, service-health, compartment applicability source/representation/validation, PEP interaction and logging contracts unresolved. API success not automatically Harmonia outcome. [Detail](#persistence-management-contracts). | **Later bounded design**; semantic responsibility/AX-05 and AX-07 settled; audit-authorisation conflict CDG-42 retained. |
| **CDG-48 — established Search / Research Metadata / detailed contracts** | Mnemosyne Search/Research Metadata, Mneme Adhoc Search; F43/44/D29/30/M19 | Explicit U11 task decisions establish authoritative persistence search, possible capability metadata and distinct AI-derived proximity-based metadata managed by Mnemosyne; derivation executes outside it. | Query/result/completeness/consistency, search metadata, deriving behavioural construct, provenance/confidence/Research Metadata schema unresolved. Source Content unchanged; no AI/provider/vector/embedding/proximity/index choice. [Detail](#search-research-contracts). | **Later bounded adjudication/design**; no assertion of unresolved detail, cache completeness or intrinsic Mnemosyne AI execution. |

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
U02–U12 correct only the affected Step-A candidate meanings recorded above.
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
U06; Mneme provides governed Context/Pragma access and Mnemosyne O&A supplies durable
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

<a id="themis-detailed-contracts"></a>

## CDG-42 — Themis detailed contracts and audit-authorisation conflict

U08 and [U12](sources.md#u12) settle the Authorisation Model, PAP/PRP/PDP,
PEP-Context, precomputed Identity-specific PEP-Criteria and Themis-owned
distributed enforcement in [C02](component-register.md#c02). U12 replaces the
earlier open-ended policy-language deferral with constrained governed Criterion
conjunction and additive UNION semantics, with no explicit negative/DENY Criteria.
It also settles authentication SEP, Calliope nomenclature, AuthorisationRole,
PractitionerCapacity, temporal validity versus revocation, governed
Ergo/Praxis/Iris administration and fail-closed handling of untagged information.
These meanings are not gaps requiring reinvention at implementation time.

U12 refines delegation to **Identity / PEP-Criteria pass-off (forwarding)**,
subject to FORWARD authority and provenance for originating and receiving/acting
Identities. It establishes no separate delegation subsystem or identity
impersonation. Lifecycle/representation, PDP involvement and detailed governance
remain deferred; they SHALL NOT be supplied by inference.

<a id="identity-deferred-design"></a>

### Identity deferred design / discovery

The following remain explicitly unresolved after consolidation:

1. Complete release-specific contents of all four controlled authorisation ValueSets.
2. Exact temporal expression model for Criterion, assignments and applicable context.
3. Revocation propagation and PEP-Context / PEP-Criteria invalidation mechanisms.
4. Exact Criteria collection implementation name/representation; nesting semantics are settled.
5. Exact PEP-Criteria wire/data representation and security-context carrier relationships.
6. Effective Information.Compartment / Information.Classification derivation ownership and precedence; Themis is not assumed to own all semantic derivation.
7. Detailed PractitionerCapacity selection UI.
8. Exact PractitionerCapacity ↔ FHIR PractitionerRole mapping; no mapping to a Domain04 Role Binding is inferred either.
9. Agora/Synapse membership, operational responsibility, escalation, approval-routing and notification mechanics.
10. Detailed PAP management Praxes/Erga, approval rules, UI and versioning mechanisms.
11. Detailed Identity / PEP-Criteria forwarding lifecycle and representation.
12. PEP-Criteria caching, refresh and distribution mechanics beyond established valid-context availability constraints.

Detailed host/decision implementation, policy-change propagation, persistence
realisation and timeout/retry/user-notification contracts also remain downstream.
No general-purpose policy language, new repository topology, historical carrier
equivalence or Information Unit equivalence is supplied by these deferrals.
They do not prevent review of the sufficiently adjudicated Identity Candidate
Architecture or constitute whole-Domain05 acceptance.

### Upstream business-security realisation boundary

[Domain03 Health Information Control §2.5](../03-business-architecture/behaviours/05-intrinsic-enablement.md#25-health-information-control)
names user role, organisation, patient relationship, purpose of use, break-glass
overrides and consent constraints in its access-control responsibilities. U12
establishes the bounded Candidate Architecture model; it does not establish
the detailed realisation of each of those Business responsibilities within it.
No new Criterion dimension, negative grant, arbitrary Boolean expression,
break-glass bypass or mapping through PractitionerCapacity is inferred here.
This is an unestablished cross-layer relationship discovered during constraint
review, not a proven coverage failure or the start of Step-B mapping. Any
required extension or conflict needs Mark + ChatGPT architectural adjudication;
the existing Business meanings and U12's Candidate semantics remain explicit.

### Retained audit-authorisation conflict

**Retained conflict:** supplied Themis DesignNote §15 calls Kleio's authoritative
audit-persistence stream an exception to obtaining Themis authorisation before
governed information actions. [AX-07](../governance/architectural-axioms.md#ax-07)
requires every governed operation to execute within established security context
and under Harmonia policy. The controlling task requirements do not adjudicate
this exemption. It is therefore **not adopted as an authorisation bypass**.
How essential audit capture remains available, including recording denied activity,
while preserving AX-07 requires explicit bounded adjudication before detailed
interaction design. Required capture and default-deny remain in force; no policy
or enforcement exception is silently manufactured. Domains01–04 and axioms are
unchanged. This is architectural uncertainty under AX-17, distinct from PDP
unavailability or a policy denial.

<a id="kleio-detailed-contracts"></a>

## CDG-43 — Kleio detailed capture, historical context and persistence

[C03](component-register.md#c03) establishes authoritative capture, immutable
AuditEvent, longitudinal reference/necessary-replication and fail-closed
availability. Capture API/client library, FHIR mapping/attachment/reference,
retention/archival/disposal/legal hold, query/index architecture, Journey/Praxis/
task/user/resource correlation and historical-context processing remain unresolved.
Supporting structures need not be AuditEvents; prepared views cannot replace
authoritative evidence. Dokimasia terminal constraints remain unchanged.

Originating-activity/capture transaction coordination, persistence-target selection,
one/some/all write strategy, retry/failover, health/readiness and physical design
remain downstream. Audit cannot become best-effort or reconstructable from logs/
Journey. One capable configured authoritative target suffices; no capable target
means Harmonia DOWN. An unresolved capture outcome remains uncertainty under
AX-15, without assumptions of success or non-occurrence.

Independent targets may expose differing histories. **R1.x/R2.x cross-target audit
harmonisation is SEP** and is not a design obligation/gap for this release. No
identical/complete-copy, convergence, reconnection-reconciliation or unified-history
guarantee is introduced, and no future mechanism is selected. Independent targets
fit O&A partitionability without selecting a general O&A topology.

<a id="petasos-detailed-contracts"></a>

## CDG-44 — Petasos Parcel, queue purposes and downstream mechanisms

[C10](component-register.md#c10) establishes purpose-defined durable queues,
their creation/operation/status, common by-value PetasosParcel and predominantly
referenced associated information. Detailed datatype/schema/reference/content,
relationships to historical carriers, final purpose taxonomy/names and registration/
configuration remain unresolved. FHIR Communication is a conceptual analogy only.

Technology, topology/partitioning/scaling, persistence, ordering/delivery,
consumer coordination, retry/recovery/dead-letter and queue-health/metrics/state
are not selected. Existing execution-retry/handoff-redelivery/replay distinctions
remain; this correction supplies no new guarantees/algorithms. Petasos/Themis
FORWARD, Petasos/Kleio audit and Journey correlation/reconstruction protocols remain
downstream. Petasos retains queue operation, Themis retains PEP semantics and
Energeia remains Observe → Report for R1.x/R2.x.

<a id="context-and-pragma-contracts"></a>

## CDG-45 — Context and Pragma detailed contracts

[Context/Pragma architecture](context-and-persistence-management.md#mneme-responsibility)
is adjudicated. Four principal exposed services, temporal loaded values with
bucket Composition, Context/resource-version distinction, likely non-exhaustive
expected sets, supported Adhoc acquisition, Loader/Unloader separation and new
Content as Pragma Output are settled.

Physical Context representation/bucket schema, identity/version/serialisation,
Information Unit Management Context mapping, declaration/resource mapping,
acquisition/caching/distribution/replication/prefetch/batching, concurrency/locking/
MVCC/conflict detection, detailed access/search APIs/results, exact Output
representation and downstream routing remain unresolved under AX-17. C11 historical
supporting contracts are not automatically four-service API definitions or extra
services. Working Context changes do not confer durable authority.

<a id="harmonisation-contracts"></a>

## CDG-46 — Resource Harmonisation detailed work and exception contracts

[Internal harmonisation responsibility](context-and-persistence-management.md#resource-harmonisation)
is established. Approximately 5% or less at worst is a design expectation, not
an SLA; normal delivery remains efficient. Actual harmonisation is new governed
Harmonisation Ergon work through the existing work/execution contract and common
Ponos execution; the manager does not harmonise domains or embed Ponos.

Condition categories/detection, algorithms, catalogue, exact narrow execution/
work contract and terminal/escalation handling of pathological repeated
harmonisation remain unresolved. Originating-Ergon resume/generic conflict
resolution is prohibited. Ergo Library remains candidate reuse; structure is
unresolved. No UI, Iris or reusable forms are designed. This does not change
Kleio's cross-target audit harmonisation SEP for R1.x/R2.x.

<a id="persistence-management-contracts"></a>

## CDG-47 — Persistence oversight, compartments and authoritative outcomes

[Mnemosyne Persistence Management](context-and-persistence-management.md#mnemosyne-responsibility)
is adjudicated. Detailed transaction boundaries, rollback/retry/distributed
transaction mechanisms, outcome/status schema, persistent-service capability/
health metrics, shard routing/topology/replication, compartment representation/
validation/reapplication, semantic compartment-applicability source, PEP
interaction and persistence logging schema remain unresolved.

Mnemosyne does not independently determine semantic compartment applicability or
sharding safety, replace Themis enforcement semantics or manage infrastructure.
Persistence API success alone cannot establish Harmonia's authoritative outcome;
uncertainty remains AX-15. Existing AX-05/14 state/version/generation distinctions,
Kleio audit boundaries and **CDG-42's unresolved Themis/AX-07 conflict** remain.
No audit-authorisation exemption is introduced.

<a id="search-research-contracts"></a>

## CDG-48 — Search and Research Metadata detailed architecture

[Search Management](context-and-persistence-management.md#search-management) and
[Research Metadata Management](context-and-persistence-management.md#research-metadata)
are first-class Mnemosyne responsibilities. Mneme exposes Adhoc Search;
authoritative persistence is the search space. Search capability metadata may
be managed; Research Metadata is distinct and usable by search.

Detailed query/results, completeness/consistency, search capability metadata
schema/lifecycle, Research Metadata schema, provenance, confidence and allocation
of the appropriate deriving behavioural/execution construct remain unresolved.
AI derivation stays outside Mnemosyne; authoritative source Content stays
unchanged and derived metadata distinguishable. No AI framework/model/provider,
vector/embedding mechanism, proximity algorithm or physical index is chosen.
Step B information traceability and upstream model changes have not begun.
