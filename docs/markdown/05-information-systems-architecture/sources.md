<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Architecture Sources and Reconciliation

**Standing: reconstruction and bounded Step-A adjudication provenance for the
Harmonia Candidate Information Systems Architecture.** [Orientation and scope](README.md)
govern all uses of this register.
Source keys identify documentary evidence, not architectural authority rankings.

## Authority used as constraints

| Key | Source and inspected scope | Use |
| :--- | :--- | :--- |
| <a id="a01"></a>A01 | [Architectural Axioms](../governance/architectural-axioms.md), AX-01–18 and subsystem/FHIR boundaries | Existing architectural authority; state, security, evidence, interoperability, technology and uncertainty constraints. |
| <a id="a02"></a>A02 | [Strategic logical responsibilities](../02-strategy/strategic-views/logical-component-responsibilities.md), component profiles, Twin boundary and critical seams | Existing component meanings and anti-responsibilities. Capability compositions were not mapped or evaluated by this reconstruction. |
| <a id="a03"></a>A03 | [Task / Work](../04-information-architecture/information-families/task-work.md) | Distinct archetype, work instance, undertaking, outcome and retained ReportedTask questions; no coverage analysis. |
| <a id="a04"></a>A04 | [Dokimasia finding](../04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md), current disposition and §2 | Approved independence/subject-management boundaries. This upstream capture deferred application placement; U02 now adjudicates the Domain05 candidate specialisation without altering the finding or finalising detailed control/independence contracts. Historical upstream gap analysis was not renewed. |
| <a id="a05"></a>A05 | [Observable information and domain meaning](../04-information-architecture/guardrails/observable-information-and-domain-meaning.md), §§2–4.1 | Governed Ergo loading/logic/unloading, semantic opacity and optional runtime AI; existing supersession respected. |
| <a id="a06"></a>A06 | [Domain04 Information Unit metamodel](../04-information-architecture/metamodel/information-architecture-metamodel.md), §§5–6 and model guardrails; [Domain04 baseline](../04-information-architecture/README.md#completion-status-and-semantic-baseline) | Managed responsibility, Identity/Metadata/Content/Context, distinct identity/authority/provenance/version/active generation and representation independence. U05 Container/Content notation does not replace this partition. |
| <a id="u01"></a>U01 | [Task-specific candidate decisions](README.md#task-decisions), explicit user instruction of 2026-10-11 | PragmaFactory embodiment, distinct Task/Pragma relationships and Ergon adjudication; not attributed retrospectively to legacy sources. |
| <a id="u02"></a>U02 | User-supplied `DesignNote-Energeia-DigitalTwin-Dokimasia-Specialisation.md` (2026-10-11), §§2–14; [incorporated candidate adjudication](README.md#energeia-adjudication) | Primary input explicitly authorised by this task: distinct execution/activity constructs (Energeia classification refined by U06), Mneme Pragma Management, execution/activity/information specialisations, common reuse, Twin pending-work queues, Dokimasia terminal rules, discrete AI responsibility and unresolved detailed architecture/discovery. Candidate correction only; no Step-B mapping or whole-candidate acceptance. |
| <a id="u03"></a>U03 | Supplied `DesignNote-Pragma-Management-Responsibility-and-Energeia-Boundary.md` (2026-10-11), full note | Primary human-adjudicated input authorised by this task; preserves Mneme Pragma Management/PragmaFactory and non-transfer of information responsibility through execution/behaviour/handoff. Incorporated [U02 boundary](README.md#energeia-adjudication) retained. |
| <a id="u04"></a>U04 | Supplied `DesignNote-Pathways-Journeys-and-Assurance-Representation.md` (2026-10-11), §§1–19, plus explicit bounded task decisions | [Pathway/Journey semantic view](pathways-journeys-and-handoff.md): endpoint/element/segment/traversal/route/pathway definitions, Praxis Pathway [0..1], distinct progression mechanisms, graph-shaped Journey and common reporting/assurance observation. Input-level owner deferrals are resolved by U06 for Journey; detailed schema/lifecycles remain open. Simplified legacy-like Topic topology in §9 is read with U05 classification semantics, not broker destinations. |
| <a id="u05"></a>U05 | Supplied `DesignNote-Mneme-Publication-Subscription-and-Petasos-Handoff-Architecture.md` (2026-10-11), §§1–25, plus explicit bounded task decisions | [Publication/selection](pathways-journeys-and-handoff.md#publication-and-selection), [persistent handoff](pathways-journeys-and-handoff.md#persistent-handoff), [state distinctions](pathways-journeys-and-handoff.md#state-and-recovery) and [fan-out boundary](pathways-journeys-and-handoff.md#fan-out-boundary) incorporated. Common managed Pragma/payload and distinct consumer FulfillmentTasks settled in principle; detailed reference/copy/version/lifecycle contracts unresolved. Petasos generality retained; technology possibilities in §22 are not adopted choices. |
| <a id="u06"></a>U06 | Supplied `DesignNote-Energeia-Operational-Management-and-Praxis-Responsibility.md` (2026-10-11), §§1–15, **plus explicit current task adjudications** | [Incorporated operational-management decisions](README.md#operational-management-adjudication): discrete Energeia component; Praxis/Journey ownership and candidate Management Services; Praxis assurance basis/Journey evidence subject; two management planes; R1.x/R2.x observation/reporting (Observe → Report correction under U09/current task) and deferred R3 direction; precise Mneme semantic eligibility versus Petasos concrete handoff; [Clinical/C&G/O&A persistence guardrails](data-and-persistence.md#persistence-responsibility-domains). Task instructions establish Journey ownership/persistence domains beyond note wording. Detailed lifecycle/representation/decomposition/physical designs remain explicit. |

The U06 correction incorporated the earlier U02–U05 decisions; its review covered
all four earlier notes alongside the then-new U06 note. Duplicate note attachments do not
establish additional decisions. That correction read all five supplied DesignNotes in full.
The U06 task decisions extend that note with Journey ownership and the three
Mnemosyne persistence domains/scaling guardrails; these are not attributed to
wording absent from that note. U04/U05 label themselves working/conversational input;
the explicit task instruction makes them the primary human-adjudicated inputs
for the matters they address, within existing axiom/Domain04 authority. Their
decisions are faithfully incorporated into these RADS Candidate Architecture
views, without accepting the whole candidate or choosing implementations.

External filenames and dates record provenance only. Required incorporated
meaning is readable in RADS without Downloads/Library access. The original
notes remain unchanged outside the repository; full-document external legacy
migration is not performed. Their unestablished details remain explicit in
CDG-01/03/06/16–19/36–38. DesignNotes and legacy/source descriptions are not
implementation evidence. This is Step-A source reconciliation, not Step-B
upstream capability or information traceability.

## This Kleio / Themis / Petasos adjudication input

| Key | Source and inspected scope | Incorporated candidate meaning |
| :--- | :--- | :--- |
| <a id="u07"></a>U07 | Supplied `DesignNote-Kleio-Audit-Information-Management-and-Resilient-Capture.md` (2026-10-11), §§1–17, read in full; explicit bounded task requirements | [Kleio C03](component-register.md#c03): authoritative Audit Information Management/Capture, client occurrence contract, immutable FHIR AuditEvent/new linked corrections, meaningful user activity, Journey independence, longitudinal reference/necessary replication, evidence versus prepared views, essential durable capture, independent O&A targets and R1.x/R2.x harmonisation SEP. Detail [CDG-43](gaps.md#kleio-detailed-contracts). |
| <a id="u08"></a>U08 | Supplied `DesignNote-Themis-Authorisation-Model-and-Distributed-Policy-Enforcement.md` (2026-10-11), §§1–18, read in full; explicit bounded task requirements | [Themis C02](component-register.md#c02): Authorisation Model/PAP/PRP/PDP, distinct ACCESS/CHANGE/FORWARD, contextual PEP-Context Criteria, Themis-owned distributed local PEP, valid-context/PDP availability distinction, potential delegation/provenance and possible PRP durability through Mneme/C&G. §15 audit-authorisation exception retained as AX-07 conflict, not incorporated as bypass; detailed governance/design [CDG-42](gaps.md#themis-detailed-contracts). |
| <a id="u09"></a>U09 | Supplied `DesignNote-Petasos-Durable-Message-Event-Queue-Management.md` (2026-10-11), §§1–16, read in full; explicit bounded task requirements | [Petasos C10](component-register.md#c10): durable purpose-defined queue creation/operation/status, common by-value PetasosParcel with predominantly referenced associated information, ActionableTask special case, local Themis-owned FORWARD enforcement and Petasos queue-state ownership. Energeia R1.x/R2.x **Observe → Report** corrects earlier U06 propagation. Detail [CDG-44](gaps.md#petasos-detailed-contracts). |

The supplied files were accessible in Downloads; no copies were present in the
repository. Their incorporated decisions and controlling task requirements are
self-contained in [the orientation](README.md#kleio-themis-petasos-adjudication)
and supporting views. Filenames are provenance, not an ongoing external authority
dependency. All original note bytes remain unchanged. The explicit unresolved
exception conflict and deferred mechanisms are preserved under AX-17; no source
code, transport/carrier schema, upstream capability or Step-B mapping supplies
missing decisions. The current correction preserves U01–U06 adjudications except
the expressly corrected Energeia Monitor responsibility.

## This Mneme / Mnemosyne adjudication input

| Key | Source and inspected scope | Incorporated candidate meaning |
| :--- | :--- | :--- |
| <a id="u10"></a>U10 | Supplied `DesignNote-Mneme-Context-and-Pragma-Management.md` (2026-10-11), §§1–21, read in full; explicit bounded task decisions | [Canonical Context/Pragma view](context-and-persistence-management.md#mneme-responsibility): Context/Pragma Manager, four exposed services, temporal authoritative loaded values and bucket Composition, non-exhaustive likely set, supported Adhoc access, distinct Loader/Unloader versus Load/Save, new Content as Pragma Output, internal exceptional RHM/new Harmonisation Ergon work through common Ponos. Detail CDG-45/46 retained. |
| <a id="u11"></a>U11 | Supplied `DesignNote-Mnemosyne-Persistence-Management-and-Authoritative-Persistence-Oversight.md` (2026-10-11), §§1–15, read in full; **explicit task decisions for first-class Search and Research Metadata Management** | [Persistence Manager](context-and-persistence-management.md#mnemosyne-responsibility): transaction/service oversight, shard management, authoritative reporting/logging, compartments and Themis-owned PEP. [Search](context-and-persistence-management.md#search-management) and [Research Metadata](context-and-persistence-management.md#research-metadata) elaboration is explicitly task-adjudicated, not attributed to absent note wording. AI derivation outside Mnemosyne, source Content unchanged, detail CDG-47/48. |

The Mnemosyne note's generic Mneme information-management shorthand is interpreted
with U10's more specific Context/Pragma adjudication. The persistence note does not
itself elaborate Research Metadata; the explicit task supplies that authority.
The notes' original bytes remain unchanged in Downloads; no external-note migration
or rewriting is performed. Required incorporated meaning is self-contained in
RADS, with no ongoing architectural dependency on access to the attachments.

The explicit task confirms **Energeia R1.x/R2.x Observe → Report** and removes
residual wording suggesting an intermediate architectural responsibility. The
existing literal stale sequence was already absent at the task baseline; the
README's residual allowance is corrected. R3 direction, legitimate Dokimasia
monitoring restrictions and unaffected U01–U09 meanings remain. Themis/AX-07
conflict remains unresolved; this is A2/A3 work, A4 pending, Step B not started.

## This Identity candidate consolidation input

| Key | Source and inspected scope | Incorporated candidate meaning |
| :--- | :--- | :--- |
| <a id="u12"></a>U12 | Supplied `DesignNote-Identity-Authentication-and-Authorisation-Architecture.md`, §§1–18, read in full from Downloads; explicit consolidation task decisions, 2026-10-12 | [Identity consolidation](README.md#identity-candidate-adjudication), [Calliope nomenclature](component-register.md#authorisation-nomenclature) and [Themis C02](component-register.md#c02): authentication SEP/external OIDC IdP, Keycloak + PostgreSQL development/test/POC default; release-bounded ValueSets; Criterion conjunction/additive UNION, nested collections, AuthorisationRole and direct/role assignments; temporal validity versus revocation; precomputed Identity-specific PEP-Criteria and constrained PEP; untagged-information control; PractitionerCapacity and responsibility boundary; forwarding provenance; Ergo/Praxis/Iris administration. [Detailed deferrals](gaps.md#identity-deferred-design) and the U08 audit/AX-07 conflict remain explicit. |

The working note explicitly does not supersede RADS by itself. The current task
authorises incorporation of its adjudicated Candidate Architecture into the
existing RADS views, subject to the axioms. U12 unambiguously refines U08's open
Criteria/policy-language and delegation descriptions; it does not adjudicate
the Kleio audit-authorisation exception. No subsequent explicit decision resolving
that conflict was found in the relevant RADS material reviewed.

The note was supplied outside the repository and remains unchanged there; no
repository copy is assumed or added. Its filename records provenance, not an
ongoing external architecture dependency. Required meanings are self-contained
in RADS. Domains01–04 and the axioms remain unchanged; Practitioner and Business
Role meanings, professional assertions and Information Unit/containment boundaries
were reviewed to avoid inferred mappings. This is A2/A3 reconciliation, not A4
acceptance, upstream traceability, implementation or a runtime-programme step.
The [Business security realisation boundary](gaps.md#upstream-business-security-realisation-boundary)
is retained explicitly: Domain03's purpose/relationship/consent/break-glass
responsibilities are not silently translated into new policy semantics.

## Legacy reconstruction sources

“Incorporated” means the identified architectural meaning is now represented
as candidate meaning subject to RADS constraints. “Partially incorporated”
leaves other meanings, product details or contradictions in the source.
Corroboration is not independent semantic migration. Conflicting sections are
retained unchanged and cited by [CDG issues](gaps.md); inspection alone causes
no supersession. No entire legacy document is retired by this task.

| Key | Legacy source and reviewed scope | Candidate meaning and disposition |
| :--- | :--- | :--- |
| <a id="l01"></a>L01 | [Execution model](../../architecture/execution-model.md), §§1–6.2 | Structural introduction/§1 superseded by the system map and explicit relationships, now extended by U06; detailed Praxis contracts remain unresolved. §§6–6.1 already superseded by A05; §6.2 now superseded by C20/C27, terminal constraints and CDG-35. §2.1 envelope-to-Pragma conversion and §4 combined state/checkpoint account now wrapped to U05 governed resolution, state separation and CDG-03/16/18. Other write-behind/universal checkpoint claims remain historical conflicts, not adopted architecture. |
| <a id="l02"></a>L02 | [Ponos](../../concepts/ponos.md), full concept | **Partially incorporated:** execution/governance/control support; §3 “What Ponos Owns” previously superseded by C04/F04–F06/F28. Opening daemon-only definition now superseded by C04 and U02 specialisation/reuse. Runtime, queue names and numerical limits remain supporting detail. |
| <a id="l03"></a>L03 | [Ergon](../../concepts/ergon.md), full concept | **Partially incorporated:** transformation, validation, fan-out and payload/checkpoint behaviour; §3 “What an Ergon Owns” previously superseded by the activity register/F07–F10. Opening definition and §2 Core Characteristics now wrapped to C23/C24 and CDG-05, preserving original content while distinguishing Ergon from meaningful Erga collections. Pure/atomic/idempotent/composition detail remains unresolved, not silently adjudicated. |
| <a id="l04"></a>L04 | [Praxis](../../concepts/praxis.md), full concept | **Partially incorporated:** blueprint loading/seeding, ordering/conditions and schemas; §3 “What Praxis Owns” superseded by C06/F11–F12. Opening definition and §2 DAG assertion now wrapped to C06, Pathway [0..1] and CDG-01. U06 now establishes the higher-order envelope and Energeia ownership; historical blueprint/runtime/actual activity equivalences remain unresolved. §1 Architectural Rationale newly wrapped to the Praxis envelope/ownership and detailed-contract boundary. |
| <a id="l05"></a>L05 | [Pragma](../../concepts/pragma.md), full concept | **Partially incorporated:** context/state carrier; §3 anti-responsibilities previously superseded by C07. §3 “What Pragma Owns” now wrapped to Mneme Pragma Management C28/C07. Opening definition, §2 architectural definition and §5 context-safety claims now wrapped to the U05 semantic shape, state separation and retained representation uncertainty; §3 existing wrappers remain. Detailed authority/persistence contracts remain CDG-02/03/10/15/18/37. |
| <a id="l06"></a>L06 | [Calliope canonical](../../concepts/calliope-canonical.md), full document | **Partially incorporated:** §2 event/payload/reason models and converter responsibilities superseded by C01/F01–F02/D01–D02/D08. Foundation dependency diagram retained as implementation support. |
| <a id="l07"></a>L07 | [Calliope](../../concepts/calliope.md), full concept | **Supporting/corroborating:** vocabulary, converter catalogue and topic constants. Architectural responsibility is constrained by A01/A02; no separate migration of implementation catalogue. |
| <a id="l08"></a>L08 | [Themis](../../concepts/themis.md), [security concept](../../concepts/themis-security.md), [decision audit](../../security/audit.md), [module reference](../../modules/themis.md) | **Partially incorporated:** existing positive ownership wrapper resolves to corrected C02; this correction wraps Themis opening/§2 fixed gates, security-concept opening/§1 fixed gates/§4 decision-audit claims, decision-audit §1 overview and module opening/positive ownership. U08 now establishes model management/local PEP; U07 establishes Kleio capture. Module/class/role/schema details remain historical support; automatic context persistence and detailed host/evidence/privacy relationships remain CDG-13/15/30/42/43. |
| <a id="l09"></a>L09 | [Hestia](../../concepts/hestia.md), [Mneme](../../concepts/mneme.md), [Mnemosyne](../../concepts/mnemosyne.md), full concepts | **Partially incorporated and retained conflicts:** U06 supersedes Mnemosyne opening and §2 two-service Clinical/Operational definition with Clinical/C&G/O&A semantic responsibilities. U10/U11 additionally supersede Mneme opening, §2 introductory architectural definition and §3 positive ownership list; Hestia opening and §2 dual-state architectural account; Mnemosyne §3 positive ownership list. Existing wrappers remain unchanged and resolve to updated RADS. Remaining metaphor, product/cache/write-behind/access/audit and implementation catalogues remain historical/supporting or retained conflicts, not current authority. |
| <a id="l10"></a>L10 | [Hestia persistence](../../concepts/hestia-persistence.md), full document | **Partially incorporated:** §3.2 Operations Scope bullet now wrapped to C&G/O&A semantic classification and scaling guardrails. Cache/product/provider catalogues remain support; mixed diagrams, direct access and write-behind model remain historical/conflicting, not adopted physical architecture. |
| <a id="l11"></a>L11 | [Iris](../../concepts/iris.md), full concept | **Partially incorporated:** presentation decomposition/features; §4 “Provider Registry Decoupling” superseded by C16/F20. Raw-cache ownership/access statements retained as conflicting, CDG-11. |
| <a id="l12"></a>L12 | [Agora](../../concepts/agora.md), §§2–9 and diagrams | **Partially incorporated:** §3 isolation constraints and §6.2 Practitioner/Group spaces superseded by C18/F22. Room lifecycle, memberships, mappings and AS handoff recorded as candidate with CDG-19–23; universal clinical authority, care-team source and storage ownership remain conflicting. Product-specific ADR table retained as supporting/unresolved detail. |
| <a id="l13"></a>L13 | [Architecture decisions](../../architecture-decisions.md), ADR-001–020, focused decisions/responsibility/failure/lifecycle sections | **Partially incorporated:** ADR-013–017 architectural descriptions superseded by C03/C10/F13–F15/M01–M02. Accepted ADR-018–020 constrain state/lifecycle in their governance role; product choices, detailed protocols and historical reasoning remain supporting material. No decision is rescinded. |
| <a id="l14"></a>L14 | [Governed write contract](../../design/governed-write-concurrency-contract.md), §§1–4, read semantics, invariants/results/version/uncertainty/context/service boundaries | **Partially incorporated:** §§10.2 and 14.2 superseded by C11/D04/M05. Exact Java signatures, CAS and reconciliation algorithms retained as implementation detail; old token/absence/convergence formulations remain CDG-16/31 input. |
| <a id="l15"></a>L15 | Provider Registry [architecture](../../provider-registry/architecture.md), [resource model](../../provider-registry/resource-model.md), [change processing](../../provider-registry/change-processing.md), [persistence](../../provider-registry/persistence.md), [search](../../provider-registry/search.md), [audit/provenance](../../provider-registry/audit-provenance.md) | **Partially incorporated:** resource-model §3 Endpoint/Group meanings superseded by D08/C13; architectural-object and feature descriptions recorded with direct-persistence, lifecycle and evidence contradictions retained. No universal external async/FHIR contract approval, CDG-03/11–14/32. |
| <a id="l16"></a>L16 | [Persistence architecture](../../persistence-architecture.md), [database schema](../../database-schema.md), [persistence lifecycle](../../architecture/persistence-lifecycle.md), full documents | **Conflicting/unresolved:** current-row versus per-version keys, FHIR/operations/audit stores, retention and cache “durability” variants recorded in the persistence view. DDL and ER diagrams retained; no schema chosen or entire tier model imported. |
| <a id="l17"></a>L17 | Middleware [overview](../../middleware/overview.md), [HAPI FHIR](../../middleware/hapi-fhir.md), [Infinispan](../../middleware/infinispan.md), stack/tables and used/avoided sections | **Implementation/supporting detail and conflicts:** products explain historical mechanisms, not logical components. Schema, cache, indexing and terminology differences retained, CDG-12/24/27. |
| <a id="l18"></a>L18 | LaTeX [application layer](../../latex/chapters/03-application-layer.tex), full chapter; [Energeia chapter](../../latex/chapters/06-workflow-energeia.tex), full chapter | **Partially incorporated:** Energeia chapter opening engine/Pragma-ownership paragraph and Praxis sequence-engine definition now wrapped to C39 and the Praxis envelope. Activity catalogues, dynamic-route mechanisms, EMPI/direct-access and broad clinical-work claims remain historical/supporting or unresolved CDG-01/11/20/29. No tutorial/code migration or publication-derived authority. |
| <a id="l19"></a>L19 | LaTeX [Praxis appendix](../../latex/chapters/appendix-praxis-workflow.tex), overview/paradigm/topic/checkpoint/dual-cache sections; [Ergon appendix](../../latex/chapters/appendix-ergon-module.tex), overview/contract/carrier sections | **Retained supporting detail and unresolved:** declarative versus runtime design, topics, loading/processing/unloading mechanisms and binary-bearing payload examples captured as evidence in CDG-01/02/05/06/25. Overview opening allocation and Praxis Paradigm component-topology paragraph now additionally wrapped to C39/Praxis envelope and detailed uncertainty. Topic Subscription Matching subsection previously wrapped to Mneme Publishing/Subscription/Pattern, Topic semantics and CDG-36/37. Its tuple/wildcard/priority algorithm is preserved as historical detail, not selected. Other code/tutorial/deployment examples remain supporting material, not current architecture proof. |
| <a id="l20"></a>L20 | LaTeX [data chapter](../../latex/chapters/04-data-architecture.tex), carrier/checkpoint passages; [persistence chapter](../../latex/chapters/10-postgresql-persistence.tex) and [persistence register](../../latex/chapters/appendix-i-persistence-register.tex), full persistence sections | **Partially incorporated:** persistence chapter opening two-module description and Database Separation lead-in now wrapped to three semantic domains and their scaling guardrails. Physical cluster/schema descriptions, collaboration storage, competing keys/history/evidence/cache detail remain historical/supporting or unresolved CDG-12/13/22/27/40; all original content unchanged. |
| <a id="l21"></a>L21 | [Iris console](../../architecture/iris-operations-console.md), perspectives/aggregation/data models/gap sections; [design system](../../architecture/iris-design-system.md), component catalogue and subsystem-view rules | **Supporting detail/retained for reconciliation:** diagnostic features, providers and shared presentation constructs; operational UI taxonomy is not the software hierarchy. Telemetry origin/lifecycle gaps retained, CDG-26/28. |
| <a id="l22"></a>L22 | [Paradeigma concept](../../concepts/paradeigma.md), decomposition/isolation; [simulation architecture](../../paradeigma/architecture.md), components/interfaces/journey | **Partially incorporated:** concept §3 production isolation superseded by C21/F29. Simulator/scenario/test roles corroborate the support boundary; external wire interfaces/deployment remain supporting material. Simulator “twins” do not define the Digital Twin construct. |
| <a id="l23"></a>L23 | Module references [Energeia](../../modules/energeia.md), [Hestia](../../modules/hestia.md), [Iris](../../modules/iris.md), responsibility and leaf-module sections; [operations CLI](../../../hestia/mnemosyne-operations-cli/README.md), overview | Existing Energeia opening/ownership wrappers now resolve to U06 discrete operational management and U02 activity/information boundaries; detailed Praxis uncertainty remains. Hestia opening two-domain description newly wrapped to Clinical/C&G/O&A responsibilities. Module/class inventories and other sources remain **implementation/supporting detail**, not hierarchy authority. Direct storage CLI access remains CDG-11/28 input. |
| <a id="l24"></a>L24 | [Strategic Local Directory position (DOCX)](../../Harmonia_Strategy_Local_Directory_and_Broader_Role.docx), extracted paragraphs, especially §§5–9/13 | **Retained for later reconciliation:** directory synchronisation/enrichment, endpoint capability and shared-foundation solution proposals recorded as unresolved candidate intent, F30/CDG-33. Its requirements/waves are not adopted or compared with upstream architecture. Original binary unchanged. |
| <a id="l25"></a>L25 | LaTeX diagrams: [system landscape](../../latex/diagrams/fig-app-overview-5tier.tex), [Praxis](../../latex/diagrams/fig-praxis-workflow-architecture.tex), [Ergon](../../latex/diagrams/fig-ergon-module-architecture.tex), [Hestia](../../latex/diagrams/fig-hestia-data-grid.tex), [Iris](../../latex/diagrams/fig-iris-architecture.tex), [Petasos](../../latex/diagrams/fig-petasos-messaging.tex), [Pragma](../../latex/diagrams/fig-pragma-state-flow.tex), selected node/relationship declarations | **Supporting/retained:** existing maps corroborate components and disputed relationships. Product tiers and diagram stereotypes are not copied as Domain05 hierarchy. |
| <a id="l26"></a>L26 | [Overview](../../architecture/overview.md), [runtime architecture](../../architecture/runtime-architecture.md), [system inventory](../../architecture/system-inventory.md), [architecture at a glance](../../getting-started/architecture-at-a-glance.md), tier/flow/inventory sections | **Implementation/supporting and conflicting:** topology/catalogue navigation; mixed ACK, cache, direct-access and Ponos/Agora descriptions retained. No inferred module-count architecture. |
| <a id="l27"></a>L27 | LaTeX [backlog](../../latex/chapters/appendix-backlog.tex), BL-CLIN-007/008 | **Retained proposal:** clinical viewer/authoring and terminology lookup, expansion and validation intent; owner and feature maturity unresolved, F18/F25/CDG-24. No implementation sequence imported. |
| <a id="l28"></a>L28 | [Message lifecycle](../../message-lifecycle.md), ADT/ORM sequence sections | **Conflicting/unresolved:** candidate fan-out and order-routing collaborations corroborated; combined Mneme/Mnemosyne participant, acceptance and completion boundaries retained, CDG-10/16/18. |
| <a id="l29"></a>L29 | [Petasos architecture](../../../petasos/docs/architecture.md), [root messaging architecture](../../architecture.md), overview; [Petasos concept](../../concepts/petasos.md), [messaging concept](../../concepts/petasos-messaging.md), [module reference](../../modules/petasos.md) | **Partially incorporated:** existing concept definition/ownership and messaging §2 wrappers resolve to updated C10/handoff. This correction wraps messaging opening/§1 generic-broker introduction, module opening/positive ownership and root messaging-architecture opening and Standard Message Envelope. U09 now establishes purpose-defined durable queues/common by-value Parcel; technology/module/schema/topology catalogues remain historical supporting detail. Exact historical carrier relationships/delivery guarantees remain CDG-16/19/27/44. |
| <a id="l30"></a>L30 | [Pylai concept](../../concepts/pylai.md), [gateway concept](../../concepts/pylai-gateways.md), full concepts | **Conflicting/unresolved and corroborating:** ingress/egress/registry functional slices retained, transport ownership and direct store access constrained by A01/A02; REC-001/002 remain repository invariants. No new external contract selected, CDG-11/18/32/34. |
| <a id="l31"></a>L31 | [Dokimasia orientation](../../modules/dokimasia.md), full document | Opening paragraph and Digital Twins/Execution, Runtime AI and Open Downstream Architecture sections now wrapped to C19/C20/C27, specialisation axes, Mneme Pragma Management, terminal rules and CDG-35. Canonical assurance-navigation section remains unchanged. Component boundaries established by U02; detailed AI architecture remains discovery, not mandatory AI use in every workflow. |

## This Identity legacy reconciliation

Existing L08 wrappers remain unchanged. Additional section-level supersession
preserves original text while linking to the U12 canonical definitions:

| Legacy source / affected content | RADS replacement / disposition |
| :--- | :--- |
| [Themis security service](../../security/themis.md), §1 overview and §4 explicit-deny predicate pipeline | [C02 model and deterministic enforcement](component-register.md#c02), [Criterion/UNION semantics](component-register.md#authorisation-criteria); §2–3 module/code contracts retained as historical implementation detail. |
| L08 [security concept](../../concepts/themis-security.md), §2 role/authority table and §3 persisted context account | [AuthorisationRole/assignments](component-register.md#criteria-collections-and-authorisationroles), [operational PEP-Context](component-register.md#pep-context); exact legacy enums/carrier mappings not adopted. |
| L08 [Themis concept](../../concepts/themis.md), built-in policy portfolio; [module reference](../../modules/themis.md), built-in portfolio within §2.2 | [Governed permitted combinations](component-register.md#authorisation-criteria); historical wildcard/blanket-admin claims establish no bypass. Module/class inventories remain supporting detail. |
| L06 [Calliope concept](../../concepts/calliope.md), security-policy/identity anti-responsibility bullet | [Nomenclature ownership](component-register.md#authorisation-nomenclature), [authentication SEP](component-register.md#identity-authentication) and [Themis](component-register.md#c02); other semantic-library detail retained. |
| [Terminology guide](../../getting-started/terminology.md), Themis row; [LaTeX glossary](../../latex/chapters/appendix-a-glossary.tex), Themis row | [Identity/role terminology](component-register.md#identity-terminology), [Themis](component-register.md#c02) and [Kleio](component-register.md#c03); unrelated glossary entries unchanged. |
| [LaTeX Themis chapter](../../latex/chapters/21-themis-security.tex), opening paragraph, pipeline figure and four-gate section; L18 [application chapter](../../latex/chapters/03-application-layer.tex), Themis opening paragraph and multi-tier figure | [Authentication SEP](component-register.md#identity-authentication), [nomenclature](component-register.md#authorisation-nomenclature), [PEP-Criteria/local enforcement](component-register.md#pep-context), [Kleio](component-register.md#c03). Both included diagram sources remain unchanged historical content inside the publication-section wrappers; the canonical system-map diagram is updated. |

The supplied [Dokimasia orientation](../../modules/dokimasia.md) was reviewed;
its existing wrappers, independent assurance and terminal boundaries remain
unchanged. No Audit/Governance/Assurance/Authorisation merger is introduced.
Original legacy bodies, diagrams and earlier wrapper bytes are preserved; only
the listed affected sections/rows are newly wrapped. This is not whole-document
supersession or migration of implementation catalogues into RADS.

## Discovery limits and preservation

Discovery covered repository documentation filenames and broad text searches
for named constructs, state, middleware, terminology, media/binary and persistence
across Markdown, LaTeX and diagram source. DOCX paragraphs and
[ODT publication](../../libreoffice/harmonia-architecture-specification.odt)
text were extracted read-only; the ODT corroborates the LaTeX publication and is
retained as generated/supporting material. The discovered
[PDF](../../latex/main.pdf) was not independently interpreted as authority
where editable LaTeX sources were available.

The [previous Domain05 assessment](../../../.junie/reports/2026-10-10-domain05-application-architecture-assessment.md)
was navigation/discovery input only. Its derived responsibilities, Q01–Q05
sequence, architecture recommendation and suggested directory are not adopted.
Historical plans/reports, the legacy docs index, and OpenSpec use-case inventories
were navigation aids; no use-case/capability coverage analysis was performed.

Supersession in the identified sections preserves the complete original text
between explicit boundaries and links its replacement sections. This replaces
the legacy description's documentary location, not a candidate's approval
status or an ADR's governance role. Conflicting surrounding content is preserved
and remains historical/candidate evidence, not a competing authority over RADS.
Unlisted inspected supporting sections have not been independently migrated.

The U02 correction adds 12 section-level wrappers across L01/L02/L03/L05/L23/L31
(six legacy files), in addition to the reconstruction's existing wrappers.
No original legacy wording is rewritten. The supplied external DesignNote is
review provenance; it is not a repository legacy document requiring modification.
Neither legacy supersession nor source incorporation accepts the whole Candidate
Architecture or resolves the retained Praxis and detailed-design questions.

## This Pathway / Journey / Publish/Subscribe / handoff correction

The U03–U05 correction adds 11 targeted section-level wrappers across six legacy
files: L01 execution model (§2.1 and §4); L04 Praxis opening definition and §2
DAG assertion; L05 Pragma opening, §2 and §5; L29 Petasos opening and positive
ownership list plus messaging-concept §2; L19 LaTeX Topic Subscription Matching.
Existing wrappers and all enclosed original bytes remain unchanged. Wrappers
link to precise RADS replacements, including unresolved boundaries where the
historical assertion is not adopted. Whole documents are not superseded.

No unrelated migration is performed. Historical topology, broker/JMS APIs,
queue names, schema, performance guarantees, tutorials and other unresolved
legacy descriptions retain their supporting/historical standing. The source
register records the corrected meanings and remaining CDG dependencies without
turning candidate-documentation questions into upstream coverage gaps.


## This Energeia / Praxis / Journey / persistence correction

U06 adds **11 section-level wrappers across seven legacy files**:

| Source / affected section | Authoritative RADS replacement |
| :--- | :--- |
| L09 Mnemosyne concept: opening definition; §2 Architectural Definition (including the two-service diagram) | [Persistence responsibility domains](data-and-persistence.md#persistence-responsibility-domains), [C&G authority](data-and-persistence.md#cg-governance-space), [O&A partitionability](data-and-persistence.md#oa-partitionability). |
| L10 Hestia persistence: §3.2 Operations Scope bullet | Same semantic domains/classification and scaling guardrails; framework/database bullets unchanged. |
| L23 Hestia module reference: opening two-domain description | Same semantic persistence responsibilities; module tree/class/technology details unchanged. |
| L04 Praxis concept: §1 Architectural Rationale paragraph | [Praxis envelope and ownership](pathways-journeys-and-handoff.md#praxis-operational-context), with [detailed contracts](gaps.md) retained. Existing definition/ownership wrappers remain unchanged and resolve to updated RADS. |
| L18 Energeia chapter: opening engine/Pragma-ownership paragraph; Praxis sequence-engine definition paragraph | [Energeia C39](component-register.md#c39), [explicit relationships](candidate-system-map.md#specialisation-and-reuse), [Praxis envelope](pathways-journeys-and-handoff.md#praxis-operational-context). |
| L19 Praxis appendix: Overview opening allocation paragraph; Praxis Paradigm component-topology paragraph | Energeia C39 and Praxis envelope/ownership, with detailed contracts unresolved. Tutorial/classes/dynamic-route mechanisms unchanged. |
| L20 persistence chapter: opening two-module description; Database Separation lead-in | Semantic domains and coherent C&G/O&A partitionability guardrails, without adopting or redesigning the physical cluster descriptions that follow. |

Original legacy content is unchanged; existing wrappers remain byte-preserved.
No whole document, unrelated implementation catalogue, clinical structure or
historical execution algorithm is superseded by this bounded correction. Mixed
Hestia topology/write-behind diagrams and physical persistence catalogues remain
historical/supporting material or retained conflicts; this task adopts none of
their product, database-count or deployment assumptions. Simulator “journeys”
are scenario terminology, not conflicting Journey ownership. Historical memory/
assessment artefacts remain navigation only and are not migrated into authority.

U06 required meaning is incorporated into RADS and is readable without external
DesignNote access. Detailed upstream assurance information remains deferred under
Domain04; no upstream model is changed. Possible omissions of operational-management
business demand are recorded for later review (CDG-39), without inventing required
capabilities or commencing Step B. Unrelated legacy conflicts retain their earlier
disposition; this is not a full legacy consolidation or candidate acceptance.

## This Kleio / Themis / Petasos legacy supersession correction

The targeted legacy wrappers preserve original sections unchanged and link
specific RADS replacements. Existing wrappers remain byte-preserved and now
resolve to the corrected responsibility views. **15 new wrappers across eight legacy files** cover:

- **L08:** Themis concept opening and §2 Architectural Definition; security
  concept opening, §1 fixed-gate model and §4 decision auditing; decision-audit
  §1 Overview; Themis module-reference opening and positive ownership list.
- **L29:** messaging-concept opening and §1 generic-broker introductory paragraph;
  Petasos module-reference opening and §3 positive ownership list; root
  messaging-architecture opening and Standard Message Envelope.
- **L13:** ADR-020 Audit and Provenance subsection, replacing its assumed
  permanent-source longitudinal-retrieval guarantee with Kleio's reference-by-default,
  necessary-evidence-replication boundary while preserving original text and ADR role.

No whole document is superseded. Detailed implementation catalogues, policy/role
tables, example schemas, broker topology, historical guarantees, tutorials and
unrelated conflict sections remain unchanged; the source register retains them
as historical/supporting or unresolved material. Original content and earlier
wrapper bytes are verified by removal of only this correction's wrappers.

Domains01–04 remain unchanged. The new inputs correct only Domain05 Step A and
its activity-status record; A1 COMPLETE, A2/A3 IN PROGRESS, A4 PENDING, Step B
NOT STARTED. No candidate acceptance, capability/information coverage, MVP or
implementation plan is established.

## This Mneme / Mnemosyne legacy supersession correction

**Six new section-level wrappers across three legacy concept files**:

| Source / affected section | Authoritative RADS replacement |
| :--- | :--- |
| L09 Mneme: opening definition; §2 introductory architectural-definition paragraph; §3 positive ownership heading/list | [Context/Pragma responsibility and four services](context-and-persistence-management.md#mneme-responsibility), [Context values](context-and-persistence-management.md#context-values), [C09](component-register.md#c09), [internal harmonisation](context-and-persistence-management.md#resource-harmonisation). |
| L09 Hestia: opening definition; §2 dual-state architecture account including diagram and three explanatory items | [Mneme/Mnemosyne boundary](context-and-persistence-management.md#mnemosyne-responsibility), [Context lifecycle](context-and-persistence-management.md#ergon-information-access), [state boundaries](data-and-persistence.md). |
| L09 Mnemosyne: §3 positive ownership heading/list | [Persistence Manager responsibilities](context-and-persistence-management.md#mnemosyne-responsibility), [Search](context-and-persistence-management.md#search-management), [C12](component-register.md#c12). |

All original enclosed bytes and existing wrappers are preserved. Original files
are recovered byte-for-byte by removing only the six newly inserted wrappers.
No entire legacy document is superseded. Remaining historical technology,
metaphor, diagrams, catalogues and detailed contracts are not broadly migrated or
redesigned. Existing Pragma/Ergon wrappers link the corrected register; their
original content and wrapper bytes remain unchanged. The supplied `themis.md`
legacy source and the unresolved AX-07 conflict are unchanged.

U10/U11 incorporated meaning is canonical; unresolved Context/concurrency,
harmonisation, transaction/compartment/outcome and Search/Research Metadata detail
remains explicit under AX-17. This records source reconciliation, not Step-B
capability/information traceability, MVP or A4 acceptance. Domains01–04 and
implementation/source/configuration remain outside this correction.
