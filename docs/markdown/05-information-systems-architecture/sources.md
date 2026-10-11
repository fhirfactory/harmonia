<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Architecture Sources and Reconciliation

**Standing: provenance for the initial Harmonia Candidate Information Systems
Architecture.** [Orientation and scope](README.md) govern all uses of this register.
Source keys identify documentary evidence, not architectural authority rankings.

## Authority used as constraints

| Key | Source and inspected scope | Use |
| :--- | :--- | :--- |
| <a id="a01"></a>A01 | [Architectural Axioms](../governance/architectural-axioms.md), AX-01–18 and subsystem/FHIR boundaries | Existing architectural authority; state, security, evidence, interoperability, technology and uncertainty constraints. |
| <a id="a02"></a>A02 | [Strategic logical responsibilities](../02-strategy/strategic-views/logical-component-responsibilities.md), component profiles, Twin boundary and critical seams | Existing component meanings and anti-responsibilities. Capability compositions were not mapped or evaluated by this reconstruction. |
| <a id="a03"></a>A03 | [Task / Work](../04-information-architecture/information-families/task-work.md) | Distinct archetype, work instance, undertaking, outcome and retained ReportedTask questions; no coverage analysis. |
| <a id="a04"></a>A04 | [Dokimasia finding](../04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md), current disposition and §2 | Approved conceptual assurance boundaries; application placement remains unresolved. Historical upstream gap analysis was not renewed. |
| <a id="a05"></a>A05 | [Observable information and domain meaning](../04-information-architecture/guardrails/observable-information-and-domain-meaning.md), §§2–4.1 | Governed Ergo loading/logic/unloading, semantic opacity and optional runtime AI; existing supersession respected. |
| <a id="u01"></a>U01 | [Task-specific candidate decisions](README.md#task-decisions), explicit user instruction of 2026-10-11 | PragmaFactory embodiment, distinct Task/Pragma relationships and Ergon adjudication; not attributed retrospectively to legacy sources. |

## Legacy reconstruction sources

“Incorporated” means the identified architectural meaning is now represented
as candidate meaning subject to RADS constraints. “Partially incorporated”
leaves other meanings, product details or contradictions in the source.
Corroboration is not independent semantic migration. Conflicting sections are
retained unchanged and cited by [CDG issues](gaps.md); inspection alone causes
no supersession. No entire legacy document is retired by this task.

| Key | Legacy source and reviewed scope | Candidate meaning and disposition |
| :--- | :--- | :--- |
| <a id="l01"></a>L01 | [Execution model](../../architecture/execution-model.md), §§1–6.2 | Four-tier execution proposal, dispatch, dual-authority gates, states and checkpointing. **Conflicting/unresolved** hierarchy, write-behind durability and universal checkpoint claims retained. §§6–6.1 already superseded by A05; §6.2 is assurance/AI candidate input only. |
| <a id="l02"></a>L02 | [Ponos](../../concepts/ponos.md), full concept | **Partially incorporated:** worker execution, governance, timeouts, retry/escalation, checkpoint coordination and operator controls; §3 “What Ponos Owns” superseded by C04/F04–F06/F28. Runtime, queue names and exact limits remain supporting detail. |
| <a id="l03"></a>L03 | [Ergon](../../concepts/ergon.md), full concept | **Partially incorporated:** transformation, validation, fan-out and payload/checkpoint behaviour; §3 “What an Ergon Owns” superseded by C05/F07–F10. Pure/atomic/idempotent and orchestration claims remain unresolved, CDG-05. |
| <a id="l04"></a>L04 | [Praxis](../../concepts/praxis.md), full concept | **Partially incorporated:** blueprint loading/seeding, ordering/conditions and schemas; §3 “What Praxis Owns” superseded by C06/F11–F12. Blueprint-versus-runtime/actual activity meanings remain conflicting, CDG-01. |
| <a id="l05"></a>L05 | [Pragma](../../concepts/pragma.md), full concept | **Partially incorporated:** context and state carrier; §3 anti-responsibilities superseded by C07. Payload/reference, state, authority and persistence assertions retained as unresolved, CDG-02/03/10/15. |
| <a id="l06"></a>L06 | [Calliope canonical](../../concepts/calliope-canonical.md), full document | **Partially incorporated:** §2 event/payload/reason models and converter responsibilities superseded by C01/F01–F02/D01–D02/D08. Foundation dependency diagram retained as implementation support. |
| <a id="l07"></a>L07 | [Calliope](../../concepts/calliope.md), full concept | **Supporting/corroborating:** vocabulary, converter catalogue and topic constants. Architectural responsibility is constrained by A01/A02; no separate migration of implementation catalogue. |
| <a id="l08"></a>L08 | [Themis](../../concepts/themis.md), [security concept](../../concepts/themis-security.md), [decision audit](../../security/audit.md) | **Partially incorporated:** Themis §3 “What Themis Owns” superseded by C02/F03/D03. Fixed gate variants, automatic context-in-Task persistence and universal decision auditing remain conflicting, CDG-13–15/30. |
| <a id="l09"></a>L09 | [Hestia](../../concepts/hestia.md), [Mneme](../../concepts/mneme.md), [Mnemosyne](../../concepts/mnemosyne.md), full concepts | **Conflicting/unresolved and corroborating:** documented Hestia grouping, clinical/operations slices and active cache objects recorded, but cache-only/write-behind/access/authority/audit claims not reconciled. Correct responsibilities come from A01/A02. No wrapper around mixed sections. |
| <a id="l10"></a>L10 | [Hestia persistence](../../concepts/hestia-persistence.md), full document | **Retained for reconciliation:** cache catalogues, clinical/operations allocations and providers corroborate the candidate; direct access and write-behind model retained as conflicting. |
| <a id="l11"></a>L11 | [Iris](../../concepts/iris.md), full concept | **Partially incorporated:** presentation decomposition/features; §4 “Provider Registry Decoupling” superseded by C16/F20. Raw-cache ownership/access statements retained as conflicting, CDG-11. |
| <a id="l12"></a>L12 | [Agora](../../concepts/agora.md), §§2–9 and diagrams | **Partially incorporated:** §3 isolation constraints and §6.2 Practitioner/Group spaces superseded by C18/F22. Room lifecycle, memberships, mappings and AS handoff recorded as candidate with CDG-19–23; universal clinical authority, care-team source and storage ownership remain conflicting. Product-specific ADR table retained as supporting/unresolved detail. |
| <a id="l13"></a>L13 | [Architecture decisions](../../architecture-decisions.md), ADR-001–020, focused decisions/responsibility/failure/lifecycle sections | **Partially incorporated:** ADR-013–017 architectural descriptions superseded by C03/C10/F13–F15/M01–M02. Accepted ADR-018–020 constrain state/lifecycle in their governance role; product choices, detailed protocols and historical reasoning remain supporting material. No decision is rescinded. |
| <a id="l14"></a>L14 | [Governed write contract](../../design/governed-write-concurrency-contract.md), §§1–4, read semantics, invariants/results/version/uncertainty/context/service boundaries | **Partially incorporated:** §§10.2 and 14.2 superseded by C11/D04/M05. Exact Java signatures, CAS and reconciliation algorithms retained as implementation detail; old token/absence/convergence formulations remain CDG-16/31 input. |
| <a id="l15"></a>L15 | Provider Registry [architecture](../../provider-registry/architecture.md), [resource model](../../provider-registry/resource-model.md), [change processing](../../provider-registry/change-processing.md), [persistence](../../provider-registry/persistence.md), [search](../../provider-registry/search.md), [audit/provenance](../../provider-registry/audit-provenance.md) | **Partially incorporated:** resource-model §3 Endpoint/Group meanings superseded by D08/C13; architectural-object and feature descriptions recorded with direct-persistence, lifecycle and evidence contradictions retained. No universal external async/FHIR contract approval, CDG-03/11–14/32. |
| <a id="l16"></a>L16 | [Persistence architecture](../../persistence-architecture.md), [database schema](../../database-schema.md), [persistence lifecycle](../../architecture/persistence-lifecycle.md), full documents | **Conflicting/unresolved:** current-row versus per-version keys, FHIR/operations/audit stores, retention and cache “durability” variants recorded in the persistence view. DDL and ER diagrams retained; no schema chosen or entire tier model imported. |
| <a id="l17"></a>L17 | Middleware [overview](../../middleware/overview.md), [HAPI FHIR](../../middleware/hapi-fhir.md), [Infinispan](../../middleware/infinispan.md), stack/tables and used/avoided sections | **Implementation/supporting detail and conflicts:** products explain historical mechanisms, not logical components. Schema, cache, indexing and terminology differences retained, CDG-12/24/27. |
| <a id="l18"></a>L18 | LaTeX [application layer](../../latex/chapters/03-application-layer.tex), full chapter; [Energeia chapter](../../latex/chapters/06-workflow-energeia.tex), full chapter | **Retained for later reconciliation:** corroborating components/services plus direct-access, EMPI and broad clinical-work claims, CDG-01/11/20/29. Publication format establishes no authority. |
| <a id="l19"></a>L19 | LaTeX [Praxis appendix](../../latex/chapters/appendix-praxis-workflow.tex), overview/paradigm/topic/checkpoint/dual-cache sections; [Ergon appendix](../../latex/chapters/appendix-ergon-module.tex), overview/contract/carrier sections | **Retained supporting detail and unresolved:** declarative versus runtime design, topics, loading/processing/unloading mechanisms and binary-bearing payload examples captured as evidence in CDG-01/02/05/06/25. Code/tutorial/deployment examples are not components or current implementation proof. |
| <a id="l20"></a>L20 | LaTeX [data chapter](../../latex/chapters/04-data-architecture.tex), carrier/checkpoint passages; [persistence chapter](../../latex/chapters/10-postgresql-persistence.tex) and [persistence register](../../latex/chapters/appendix-i-persistence-register.tex), full persistence sections | **Conflicting/unresolved:** competing relational keys, history, operational evidence, collaboration stores and cache allocations. Retained unchanged, CDG-12/13/22/27. |
| <a id="l21"></a>L21 | [Iris console](../../architecture/iris-operations-console.md), perspectives/aggregation/data models/gap sections; [design system](../../architecture/iris-design-system.md), component catalogue and subsystem-view rules | **Supporting detail/retained for reconciliation:** diagnostic features, providers and shared presentation constructs; operational UI taxonomy is not the software hierarchy. Telemetry origin/lifecycle gaps retained, CDG-26/28. |
| <a id="l22"></a>L22 | [Paradeigma concept](../../concepts/paradeigma.md), decomposition/isolation; [simulation architecture](../../paradeigma/architecture.md), components/interfaces/journey | **Partially incorporated:** concept §3 production isolation superseded by C21/F29. Simulator/scenario/test roles corroborate the support boundary; external wire interfaces/deployment remain supporting material. Simulator “twins” do not define the Digital Twin construct. |
| <a id="l23"></a>L23 | Module references [Energeia](../../modules/energeia.md), [Hestia](../../modules/hestia.md), [Iris](../../modules/iris.md), responsibility and leaf-module sections; [operations CLI](../../../hestia/mnemosyne-operations-cli/README.md), overview | **Implementation/supporting detail:** explicit behaviour descriptions corroborate activities, loaders, service facades, presentation and operator tools. Package/module placement does not become logical hierarchy. Direct storage CLI access remains CDG-11/28 input. |
| <a id="l24"></a>L24 | [Strategic Local Directory position (DOCX)](../../Harmonia_Strategy_Local_Directory_and_Broader_Role.docx), extracted paragraphs, especially §§5–9/13 | **Retained for later reconciliation:** directory synchronisation/enrichment, endpoint capability and shared-foundation solution proposals recorded as unresolved candidate intent, F30/CDG-33. Its requirements/waves are not adopted or compared with upstream architecture. Original binary unchanged. |
| <a id="l25"></a>L25 | LaTeX diagrams: [system landscape](../../latex/diagrams/fig-app-overview-5tier.tex), [Praxis](../../latex/diagrams/fig-praxis-workflow-architecture.tex), [Ergon](../../latex/diagrams/fig-ergon-module-architecture.tex), [Hestia](../../latex/diagrams/fig-hestia-data-grid.tex), [Iris](../../latex/diagrams/fig-iris-architecture.tex), [Petasos](../../latex/diagrams/fig-petasos-messaging.tex), [Pragma](../../latex/diagrams/fig-pragma-state-flow.tex), selected node/relationship declarations | **Supporting/retained:** existing maps corroborate components and disputed relationships. Product tiers and diagram stereotypes are not copied as Domain05 hierarchy. |
| <a id="l26"></a>L26 | [Overview](../../architecture/overview.md), [runtime architecture](../../architecture/runtime-architecture.md), [system inventory](../../architecture/system-inventory.md), [architecture at a glance](../../getting-started/architecture-at-a-glance.md), tier/flow/inventory sections | **Implementation/supporting and conflicting:** topology/catalogue navigation; mixed ACK, cache, direct-access and Ponos/Agora descriptions retained. No inferred module-count architecture. |
| <a id="l27"></a>L27 | LaTeX [backlog](../../latex/chapters/appendix-backlog.tex), BL-CLIN-007/008 | **Retained proposal:** clinical viewer/authoring and terminology lookup, expansion and validation intent; owner and feature maturity unresolved, F18/F25/CDG-24. No implementation sequence imported. |
| <a id="l28"></a>L28 | [Message lifecycle](../../message-lifecycle.md), ADT/ORM sequence sections | **Conflicting/unresolved:** candidate fan-out and order-routing collaborations corroborated; combined Mneme/Mnemosyne participant, acceptance and completion boundaries retained, CDG-10/16/18. |
| <a id="l29"></a>L29 | [Petasos architecture](../../../petasos/docs/architecture.md), [root messaging architecture](../../architecture.md), overview; [Petasos concept](../../concepts/petasos.md) and [messaging concept](../../concepts/petasos-messaging.md), full concepts | **Partially incorporated:** Petasos concept §3 anti-responsibilities superseded by C10/M01; envelope, duplicate detection and telemetry corroborate middleware. Guarantee language, queue names, cluster topology and retention remain supporting/unresolved, CDG-16/19/27. |
| <a id="l30"></a>L30 | [Pylai concept](../../concepts/pylai.md), [gateway concept](../../concepts/pylai-gateways.md), full concepts | **Conflicting/unresolved and corroborating:** ingress/egress/registry functional slices retained, transport ownership and direct store access constrained by A01/A02; REC-001/002 remain repository invariants. No new external contract selected, CDG-11/18/32/34. |
| <a id="l31"></a>L31 | [Dokimasia orientation](../../modules/dokimasia.md), full document | **Navigation/support only:** A04 governs its meaning and explicit absent allocation. Runtime AI expectation remains optional candidate use, not a requirement. |

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
