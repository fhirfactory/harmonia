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
| <a id="u02"></a>U02 | User-supplied `DesignNote-Energeia-DigitalTwin-Dokimasia-Specialisation.md` (2026-10-11), §§2–14; [incorporated candidate adjudication](README.md#energeia-adjudication) | Primary input explicitly authorised by this task: distinct Energeia constructs, Mneme Pragma Management, execution/activity/information specialisations, common reuse, Twin pending-work queues, Dokimasia terminal rules, discrete AI responsibility and unresolved detailed architecture/discovery. Candidate correction only; no Step-B mapping or whole-candidate acceptance. |
| <a id="u03"></a>U03 | Supplied `DesignNote-Pragma-Management-Responsibility-and-Energeia-Boundary.md` (2026-10-11), full note | Primary human-adjudicated input authorised by this task; preserves Mneme Pragma Management/PragmaFactory and non-transfer of information responsibility through execution/behaviour/handoff. Incorporated [U02 boundary](README.md#energeia-adjudication) retained. |
| <a id="u04"></a>U04 | Supplied `DesignNote-Pathways-Journeys-and-Assurance-Representation.md` (2026-10-11), §§1–19, plus explicit bounded task decisions | [Pathway/Journey semantic view](pathways-journeys-and-handoff.md): endpoint/element/segment/traversal/route/pathway definitions, Praxis Pathway [0..1], distinct progression mechanisms, graph-shaped Journey and common reporting/assurance observation. Owners/schema/lifecycles remain open. Simplified legacy-like Topic topology in §9 is read with U05 classification semantics, not broker destinations. |
| <a id="u05"></a>U05 | Supplied `DesignNote-Mneme-Publication-Subscription-and-Petasos-Handoff-Architecture.md` (2026-10-11), §§1–25, plus explicit bounded task decisions | [Publication/selection](pathways-journeys-and-handoff.md#publication-and-selection), [persistent handoff](pathways-journeys-and-handoff.md#persistent-handoff), [state distinctions](pathways-journeys-and-handoff.md#state-and-recovery) and [fan-out boundary](pathways-journeys-and-handoff.md#fan-out-boundary) incorporated. Common managed Pragma/payload and distinct consumer FulfillmentTasks settled in principle; detailed reference/copy/version/lifecycle contracts unresolved. Petasos generality retained; technology possibilities in §22 are not adopted choices. |

The current task supplies all four DesignNotes as local attachments; the second
Energeia file with `(1)` suffix is byte-identical to U02, not another decision.
All were read in full. U04/U05 label themselves working/conversational input;
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

## Legacy reconstruction sources

“Incorporated” means the identified architectural meaning is now represented
as candidate meaning subject to RADS constraints. “Partially incorporated”
leaves other meanings, product details or contradictions in the source.
Corroboration is not independent semantic migration. Conflicting sections are
retained unchanged and cited by [CDG issues](gaps.md); inspection alone causes
no supersession. No entire legacy document is retired by this task.

| Key | Legacy source and reviewed scope | Candidate meaning and disposition |
| :--- | :--- | :--- |
| <a id="l01"></a>L01 | [Execution model](../../architecture/execution-model.md), §§1–6.2 | Structural introduction/§1 superseded by corrected system map and separate specialisation axes; Praxis remains unresolved. §§6–6.1 already superseded by A05; §6.2 now superseded by C20/C27, terminal constraints and CDG-35. §2.1 envelope-to-Pragma conversion and §4 combined state/checkpoint account now wrapped to U05 governed resolution, state separation and CDG-03/16/18. Other write-behind/universal checkpoint claims remain historical conflicts, not adopted architecture. |
| <a id="l02"></a>L02 | [Ponos](../../concepts/ponos.md), full concept | **Partially incorporated:** execution/governance/control support; §3 “What Ponos Owns” previously superseded by C04/F04–F06/F28. Opening daemon-only definition now superseded by C04 and U02 specialisation/reuse. Runtime, queue names and numerical limits remain supporting detail. |
| <a id="l03"></a>L03 | [Ergon](../../concepts/ergon.md), full concept | **Partially incorporated:** transformation, validation, fan-out and payload/checkpoint behaviour; §3 “What an Ergon Owns” previously superseded by the activity register/F07–F10. Opening definition and §2 Core Characteristics now wrapped to C23/C24 and CDG-05, preserving original content while distinguishing Ergon from meaningful Erga collections. Pure/atomic/idempotent/composition detail remains unresolved, not silently adjudicated. |
| <a id="l04"></a>L04 | [Praxis](../../concepts/praxis.md), full concept | **Partially incorporated:** blueprint loading/seeding, ordering/conditions and schemas; §3 “What Praxis Owns” superseded by C06/F11–F12. Opening definition and §2 DAG assertion now wrapped to C06, Pathway [0..1] and CDG-01. Broader blueprint/runtime/actual activity meanings remain unresolved; no replacement Praxis definition invented. |
| <a id="l05"></a>L05 | [Pragma](../../concepts/pragma.md), full concept | **Partially incorporated:** context/state carrier; §3 anti-responsibilities previously superseded by C07. §3 “What Pragma Owns” now wrapped to Mneme Pragma Management C28/C07. Opening definition, §2 architectural definition and §5 context-safety claims now wrapped to the U05 semantic shape, state separation and retained representation uncertainty; §3 existing wrappers remain. Detailed authority/persistence contracts remain CDG-02/03/10/15/18/37. |
| <a id="l06"></a>L06 | [Calliope canonical](../../concepts/calliope-canonical.md), full document | **Partially incorporated:** §2 event/payload/reason models and converter responsibilities superseded by C01/F01–F02/D01–D02/D08. Foundation dependency diagram retained as implementation support. |
| <a id="l07"></a>L07 | [Calliope](../../concepts/calliope.md), full concept | **Supporting/corroborating:** vocabulary, converter catalogue and topic constants. Architectural responsibility is constrained by A01/A02; no separate migration of implementation catalogue. |
| <a id="l08"></a>L08 | [Themis](../../concepts/themis.md), [security concept](../../concepts/themis-security.md), [decision audit](../../security/audit.md) | **Partially incorporated:** Themis §3 “What Themis Owns” superseded by C02/F03/D03. Fixed gate variants, automatic context-in-Task persistence and universal decision auditing remain conflicting, CDG-13–15/30. |
| <a id="l09"></a>L09 | [Hestia](../../concepts/hestia.md), [Mneme](../../concepts/mneme.md), [Mnemosyne](../../concepts/mnemosyne.md), full concepts | **Conflicting/unresolved and corroborating:** documented Hestia grouping, clinical/operations slices and active cache objects recorded, but cache-only/write-behind/access/authority/audit claims not reconciled. Correct responsibilities come from A01/A02. Mixed technology/write-behind sections remain unwrapped; they do not establish current responsibility. |
| <a id="l10"></a>L10 | [Hestia persistence](../../concepts/hestia-persistence.md), full document | **Retained for reconciliation:** cache catalogues, clinical/operations allocations and providers corroborate the candidate; direct access and write-behind model retained as conflicting. |
| <a id="l11"></a>L11 | [Iris](../../concepts/iris.md), full concept | **Partially incorporated:** presentation decomposition/features; §4 “Provider Registry Decoupling” superseded by C16/F20. Raw-cache ownership/access statements retained as conflicting, CDG-11. |
| <a id="l12"></a>L12 | [Agora](../../concepts/agora.md), §§2–9 and diagrams | **Partially incorporated:** §3 isolation constraints and §6.2 Practitioner/Group spaces superseded by C18/F22. Room lifecycle, memberships, mappings and AS handoff recorded as candidate with CDG-19–23; universal clinical authority, care-team source and storage ownership remain conflicting. Product-specific ADR table retained as supporting/unresolved detail. |
| <a id="l13"></a>L13 | [Architecture decisions](../../architecture-decisions.md), ADR-001–020, focused decisions/responsibility/failure/lifecycle sections | **Partially incorporated:** ADR-013–017 architectural descriptions superseded by C03/C10/F13–F15/M01–M02. Accepted ADR-018–020 constrain state/lifecycle in their governance role; product choices, detailed protocols and historical reasoning remain supporting material. No decision is rescinded. |
| <a id="l14"></a>L14 | [Governed write contract](../../design/governed-write-concurrency-contract.md), §§1–4, read semantics, invariants/results/version/uncertainty/context/service boundaries | **Partially incorporated:** §§10.2 and 14.2 superseded by C11/D04/M05. Exact Java signatures, CAS and reconciliation algorithms retained as implementation detail; old token/absence/convergence formulations remain CDG-16/31 input. |
| <a id="l15"></a>L15 | Provider Registry [architecture](../../provider-registry/architecture.md), [resource model](../../provider-registry/resource-model.md), [change processing](../../provider-registry/change-processing.md), [persistence](../../provider-registry/persistence.md), [search](../../provider-registry/search.md), [audit/provenance](../../provider-registry/audit-provenance.md) | **Partially incorporated:** resource-model §3 Endpoint/Group meanings superseded by D08/C13; architectural-object and feature descriptions recorded with direct-persistence, lifecycle and evidence contradictions retained. No universal external async/FHIR contract approval, CDG-03/11–14/32. |
| <a id="l16"></a>L16 | [Persistence architecture](../../persistence-architecture.md), [database schema](../../database-schema.md), [persistence lifecycle](../../architecture/persistence-lifecycle.md), full documents | **Conflicting/unresolved:** current-row versus per-version keys, FHIR/operations/audit stores, retention and cache “durability” variants recorded in the persistence view. DDL and ER diagrams retained; no schema chosen or entire tier model imported. |
| <a id="l17"></a>L17 | Middleware [overview](../../middleware/overview.md), [HAPI FHIR](../../middleware/hapi-fhir.md), [Infinispan](../../middleware/infinispan.md), stack/tables and used/avoided sections | **Implementation/supporting detail and conflicts:** products explain historical mechanisms, not logical components. Schema, cache, indexing and terminology differences retained, CDG-12/24/27. |
| <a id="l18"></a>L18 | LaTeX [application layer](../../latex/chapters/03-application-layer.tex), full chapter; [Energeia chapter](../../latex/chapters/06-workflow-energeia.tex), full chapter | **Retained for later reconciliation:** corroborating components/services plus direct-access, EMPI and broad clinical-work claims, CDG-01/11/20/29. Publication format establishes no authority. |
| <a id="l19"></a>L19 | LaTeX [Praxis appendix](../../latex/chapters/appendix-praxis-workflow.tex), overview/paradigm/topic/checkpoint/dual-cache sections; [Ergon appendix](../../latex/chapters/appendix-ergon-module.tex), overview/contract/carrier sections | **Retained supporting detail and unresolved:** declarative versus runtime design, topics, loading/processing/unloading mechanisms and binary-bearing payload examples captured as evidence in CDG-01/02/05/06/25. Topic Subscription Matching subsection now wrapped to Mneme Publishing/Subscription/Pattern, Topic semantics and CDG-36/37. Its tuple/wildcard/priority algorithm is preserved as historical detail, not selected. Other code/tutorial/deployment examples remain supporting material, not current architecture proof. |
| <a id="l20"></a>L20 | LaTeX [data chapter](../../latex/chapters/04-data-architecture.tex), carrier/checkpoint passages; [persistence chapter](../../latex/chapters/10-postgresql-persistence.tex) and [persistence register](../../latex/chapters/appendix-i-persistence-register.tex), full persistence sections | **Conflicting/unresolved:** competing relational keys, history, operational evidence, collaboration stores and cache allocations. Retained unchanged, CDG-12/13/22/27. |
| <a id="l21"></a>L21 | [Iris console](../../architecture/iris-operations-console.md), perspectives/aggregation/data models/gap sections; [design system](../../architecture/iris-design-system.md), component catalogue and subsystem-view rules | **Supporting detail/retained for reconciliation:** diagnostic features, providers and shared presentation constructs; operational UI taxonomy is not the software hierarchy. Telemetry origin/lifecycle gaps retained, CDG-26/28. |
| <a id="l22"></a>L22 | [Paradeigma concept](../../concepts/paradeigma.md), decomposition/isolation; [simulation architecture](../../paradeigma/architecture.md), components/interfaces/journey | **Partially incorporated:** concept §3 production isolation superseded by C21/F29. Simulator/scenario/test roles corroborate the support boundary; external wire interfaces/deployment remain supporting material. Simulator “twins” do not define the Digital Twin construct. |
| <a id="l23"></a>L23 | Module references [Energeia](../../modules/energeia.md), [Hestia](../../modules/hestia.md), [Iris](../../modules/iris.md), responsibility and leaf-module sections; [operations CLI](../../../hestia/mnemosyne-operations-cli/README.md), overview | Energeia opening composition and §3 “What Energeia Owns” now superseded by U02 structural/activity responsibility and Mneme Pragma Management; C06 preserves Praxis uncertainty. Module/class inventories and other sources remain **implementation/supporting detail**, not hierarchy authority. Direct storage CLI access remains CDG-11/28 input. |
| <a id="l24"></a>L24 | [Strategic Local Directory position (DOCX)](../../Harmonia_Strategy_Local_Directory_and_Broader_Role.docx), extracted paragraphs, especially §§5–9/13 | **Retained for later reconciliation:** directory synchronisation/enrichment, endpoint capability and shared-foundation solution proposals recorded as unresolved candidate intent, F30/CDG-33. Its requirements/waves are not adopted or compared with upstream architecture. Original binary unchanged. |
| <a id="l25"></a>L25 | LaTeX diagrams: [system landscape](../../latex/diagrams/fig-app-overview-5tier.tex), [Praxis](../../latex/diagrams/fig-praxis-workflow-architecture.tex), [Ergon](../../latex/diagrams/fig-ergon-module-architecture.tex), [Hestia](../../latex/diagrams/fig-hestia-data-grid.tex), [Iris](../../latex/diagrams/fig-iris-architecture.tex), [Petasos](../../latex/diagrams/fig-petasos-messaging.tex), [Pragma](../../latex/diagrams/fig-pragma-state-flow.tex), selected node/relationship declarations | **Supporting/retained:** existing maps corroborate components and disputed relationships. Product tiers and diagram stereotypes are not copied as Domain05 hierarchy. |
| <a id="l26"></a>L26 | [Overview](../../architecture/overview.md), [runtime architecture](../../architecture/runtime-architecture.md), [system inventory](../../architecture/system-inventory.md), [architecture at a glance](../../getting-started/architecture-at-a-glance.md), tier/flow/inventory sections | **Implementation/supporting and conflicting:** topology/catalogue navigation; mixed ACK, cache, direct-access and Ponos/Agora descriptions retained. No inferred module-count architecture. |
| <a id="l27"></a>L27 | LaTeX [backlog](../../latex/chapters/appendix-backlog.tex), BL-CLIN-007/008 | **Retained proposal:** clinical viewer/authoring and terminology lookup, expansion and validation intent; owner and feature maturity unresolved, F18/F25/CDG-24. No implementation sequence imported. |
| <a id="l28"></a>L28 | [Message lifecycle](../../message-lifecycle.md), ADT/ORM sequence sections | **Conflicting/unresolved:** candidate fan-out and order-routing collaborations corroborated; combined Mneme/Mnemosyne participant, acceptance and completion boundaries retained, CDG-10/16/18. |
| <a id="l29"></a>L29 | [Petasos architecture](../../../petasos/docs/architecture.md), [root messaging architecture](../../architecture.md), overview; [Petasos concept](../../concepts/petasos.md) and [messaging concept](../../concepts/petasos-messaging.md), full concepts | **Partially incorporated:** Petasos concept §3 anti-responsibilities superseded by C10/M01; opening Petasos definition, §3 positive ownership list and messaging-concept §2 universal envelope now wrapped to persistent handoff, Mneme payload/semantic ownership and retained message-form detail. API/product inventories remain supporting machinery. Guarantee language, queue names, cluster topology and retention remain supporting/unresolved, CDG-16/19/27. |
| <a id="l30"></a>L30 | [Pylai concept](../../concepts/pylai.md), [gateway concept](../../concepts/pylai-gateways.md), full concepts | **Conflicting/unresolved and corroborating:** ingress/egress/registry functional slices retained, transport ownership and direct store access constrained by A01/A02; REC-001/002 remain repository invariants. No new external contract selected, CDG-11/18/32/34. |
| <a id="l31"></a>L31 | [Dokimasia orientation](../../modules/dokimasia.md), full document | Opening paragraph and Digital Twins/Execution, Runtime AI and Open Downstream Architecture sections now wrapped to C19/C20/C27, specialisation axes, Mneme Pragma Management, terminal rules and CDG-35. Canonical assurance-navigation section remains unchanged. Component boundaries established by U02; detailed AI architecture remains discovery, not mandatory AI use in every workflow. |

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
