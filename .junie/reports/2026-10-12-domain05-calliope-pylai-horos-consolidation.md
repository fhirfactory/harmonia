<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 Calliope, Pylai and Praxis.Horos Candidate consolidation

**Date:** 2026-10-12. **Outcome:** bounded documentation consolidation complete;
the three components are represented sufficiently faithfully for Candidate
Architecture review, with retained conflicts and deferred design visible.
**Programme boundary:** Step A, A2/A3 reconciliation; A4 whole-candidate acceptance
remains pending and Step B has not started. No implementation, runtime milestone,
MVP determination or whole-Domain05 acceptance is claimed. Pre-task revision:
`8f6770c`; the working tree was clean.

## Authority and working adjudication inputs

Repository [AGENTS.md](../../AGENTS.md), [docs instructions](../../docs/AGENTS.md),
[Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md),
current RADS and [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md)
govern. This is documentation consolidation, not a convergence/runtime
implementation-plan step. No implementation sequence was advanced.

Both supplied DesignNotes were read in full from `/home/hunterm/Downloads/`.
They were not assumed to exist in the repository, copied into RADS or treated
as authoritative because of their location/status. The explicit task authorises
incorporation of adjudicated Candidate meanings, subject to repository authority.
Their provenance is now recorded as
[U13/U14](../../docs/markdown/05-information-systems-architecture/sources.md#u13).

| Working DesignNote | SHA-256, unchanged after consolidation |
| :--- | :--- |
| `DesignNote-Calliope-Model-DataSet-and-Nomenclature-Management.md` | `8c7e835d3cc457ac8f0e755f74b171716e947ea5b9e7a689a853bf572378adeb` |
| `DesignNote-Pylai-External-Interoperability-Architecture.md` | `063d2339a6fb69f030fd44179264a5000b6ad10852ed3c5ad4b08ba0fe873383` |

The Pylai note predates the task's Broker/Server/Controller, precise transience /
non-recoverability and specialised EgressHoros decisions. These additions are
attributed to the explicit task, not to absent note wording. Its remaining-nuance
warning is preserved as specific deferrals. Its generic Terma/BusinessLogic pairing
is scoped to the Pathway specialisation, preserving the explicit no-fabricated-work
rule for Server READ/SEARCH. No ongoing Downloads dependency is needed to read
the incorporated architecture.

| Material axioms | Consistency preserved |
| :--- | :--- |
| AX-01/02/03/04 | Health-information purpose, applicable external contracts, native models and Harmonia-owned semantics; Camel is a candidate mechanism, RMI does not mean Java RMI. No class, route or topology chosen. |
| AX-05/06/11/12 | Dataset application ownership differs from durable persistence and originating ontology authority. Existing Mneme services/Mnemosyne establishment remain; normal type-safe developer access is supported. |
| AX-07 | External identity carriage grants no authority; Authentication SEP and Themis resolution/local enforcement remain intact. No new public or audit-authorisation bypass. |
| AX-08/09 | Significant transactions are auditable through Kleio; transient Diadromí may be observable without being recoverable. Journey, diagnostics, Audit and Assurance remain distinct. |
| AX-10/14/15/16 | Durable Petasos boundary differs from transient interaction; protocol ACK, engine success and authoritative persistence outcome remain distinct. Lost interaction does not prove non-occurrence or permit unsafe mutation retry. Governed progression remains observable. |
| AX-17/18 | Cross-layer conflicts are explicit; deferred structures and domain mappings remain unestablished. Communication, observation, persistence and coordination confer no new domain authority. |

No axioms, AGENTS rules or Domains01–04 were modified. Relevant ADR responsibility,
durability, search and lifecycle decisions were checked; none was rescinded.

## Files reviewed

Review scope is relevant sections/search hits unless explicitly stated as full.
Prior reports were used only to establish naming/navigation, never as authority.

- Root `AGENTS.md` and `docs/AGENTS.md` (full); governance architectural axioms;
  Architecture Completion Plan, especially Step A and completion gates.
- All ten pre-existing Domain05 views: README, component register, system map,
  feature map, middleware, pathways/Journeys/handoff, Context/persistence management,
  data/persistence, gaps and sources. Particular attention went to U12 Identity,
  C02/C03 Themis/Kleio, C04/C05/C23/C24 execution/activity, C06 Praxis,
  C09/C12 Mneme/Mnemosyne, C10 Petasos and C20 Dokimasia/terminal constraints.
- `docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md`:
  G2/G4, Calliope/Pylai profiles, collaboration seams and summary descriptions.
- `docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md`:
  information access/communication/control, Clinical Knowledge Services and
  Information Design Governance; `behaviours/health-service-assurance.md` and
  `information-responsibility/information-responsibility.md`: assurance and bounded
  information responsibility. No capability mapping was performed.
- Domain04 `metamodel/information-architecture-metamodel.md`,
  `guardrails/modelling-guardrails.md`,
  `guardrails/observable-information-and-domain-meaning.md`,
  `patterns/definition-to-accountability.md`,
  `information-families/task-work.md` and
  `reviews/dokimasia-assurance-architectural-finding.md`: representation, Unit/Context,
  developer logic, work/undertaking and evidence/assurance distinctions.
- `docs/architecture-decisions.md`: ADR-001–010 responsibility/search boundaries,
  ADR-013–017 evidence/handoff/replay/backlog and ADR-018–020 durability/access/lifecycle
  decisions and relevant consequences. Existing supersession wrappers respected.
- Both external working DesignNotes above (full).
- All ten changed legacy files listed below (full), including legacy terminology;
  supplied `docs/modules/dokimasia.md` (full, existing wrappers/navigation unchanged).
- `.junie/reports/2026-10-12-domain05-identity-authentication-authorisation-consolidation.md`:
  report naming and navigation only. Targeted repository searches for logical
  Praxis Endpoint/Horos and Calliope/Pylai descriptions supported discovery.

## Files changed

All canonical files below are under `docs/markdown/05-information-systems-architecture/`:

| File | Change |
| :--- | :--- |
| [calliope-models-and-datasets.md](../../docs/markdown/05-information-systems-architecture/calliope-models-and-datasets.md) — new | Canonical Calliope responsibility, decomposition, lifecycles, runtime authority, development support, nomenclature and deferrals. |
| [pylai-external-interoperability.md](../../docs/markdown/05-information-systems-architecture/pylai-external-interoperability.md) — new | Canonical service/constructs/patterns, Horos, ingress/egress lifecycle, security/evidence and deferred design. |
| [README.md](../../docs/markdown/05-information-systems-architecture/README.md) | Navigation and U13/U14 consolidated Candidate decisions; bounded review standing. |
| [component-register.md](../../docs/markdown/05-information-systems-architecture/component-register.md) | Reconciled C01/C14; C41–46 Models/DataSets/Terma/Diadromí/Controller/Horos; links from nomenclature, Ergo, Praxis and Pattern. |
| [candidate-system-map.md](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md) | Responsibility tree and collaboration diagram now distinguish Models/DataSets and Pylai patterns; Horos shown under Praxis. |
| [candidate-feature-map.md](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md) | F01/02/21/25 refined; F45–47 record datasets/nomenclature/Pylai-Horos interaction responsibilities. |
| [data-and-persistence.md](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md) | D07/D16 and reference-structure boundaries distinguish build/runtime definitions, managed content and transient interaction. |
| [context-and-persistence-management.md](../../docs/markdown/05-information-systems-architecture/context-and-persistence-management.md) | Ordinary Loader versus special ingress establishment; DataSet persistence and Server Adhoc use; four services retained. |
| [pathways-journeys-and-handoff.md](../../docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md) | Horos cross-reference, transient/durable state distinction and Pylai Journey contribution. |
| [middleware.md](../../docs/markdown/05-information-systems-architecture/middleware.md) | Build/runtime collaboration, ingress/Server example, egress ordering and separate Petasos recovery. |
| [gaps.md](../../docs/markdown/05-information-systems-architecture/gaps.md) | CDG-24/32/34 partially resolved; CDG-49/50 deferrals and CDG-51 Strategy conflict; CDG-42 audit conflict retained. |
| [sources.md](../../docs/markdown/05-information-systems-architecture/sources.md) | U13/U14 provenance, later task decisions, revised legacy dispositions and section supersession record. |

The ten legacy files below received **wrapper insertions only**. This report is
the sole additional changed file. No production code, tests, dependencies,
runtime/deployment configuration or external notes were changed.

## Architectural and terminology reconciliation

Calliope is broader than semantic-model/converter support. Models defines Models,
Metadata Types, Criteria Definitions, Topics, Filter Types/Elements, core libraries
and security/assurance nomenclature through Design → Code → Build → Test → Release
→ Deploy. DataSets manages mutable governed ValueSets, CodeSets, Structured Metadata
and applicable ontology content. Mnemosyne remains Persistence Manager; Mneme remains
Context/Pragma Manager. Existing Ponos/Ergo/Praxis may support governed management
work without another workflow engine or UI framework.

Build-time representations support efficient human/IDE/AI-assisted programming,
including normal type safety and code assistance, while runtime authority determines
current validity/membership. Structural access contracts derive from Models/build
definitions, not runtime DataSet self-bootstrap. Criteria definitions do not transfer
Themis runtime ownership; Topic definitions do not become destinations; Filter
definitions do not transfer Mneme runtime Pattern matching to Calliope.

Calliope has first-class Security and Assurance Nomenclature responsibility.
Models owns structural definitions; DataSets owns controlled content, including
U12's four release-bounded authorisation ValueSets. Themis owns policy association,
resolution, Criteria/AuthorisationRoles, compilation and enforcement. Vocabulary
membership grants no authority, and this task grants no unrestricted runtime
mutation of release-bounded vocabularies. U12 conjunction/UNION, temporal/revocation,
Capacity, PEP and Authentication SEP semantics were preserved.

Pylai is the External Interoperability Service across communication styles.
Terma is the logical external interface and performs communication mechanics;
Diadromí carries/represents transient interaction. It may span active protocol
exchanges/segments/retries but cannot be recovered after loss. Appropriate client
retry creates a new Diadromí; existing uncertain effects remain uncertain. Controller
also maintains no recoverable interaction state and does not duplicate the
responsibilities it coordinates.

Pathway ingress uses IngressHoros and a special initial ContextLoader placing
Communication Information in Task.input, with semantic mapping in paired
BusinessLogic into Task.output. Ergo.Egress feeds Queue[TermaName] as the defined
Praxis.PathwaySegment. Durable Harmonia acceptance awaits authoritative Pragma/Task
persistence. Earlier intermediate ACKs, cache/queue acceptance, engine success and
authoritative persistence outcomes remain distinct.

EgressHoros hands out after BusinessLogicCapturer, then returns interaction
state/outcome before ContextUnloader completes. Broker communicates from existing
BusinessLogic without new work/Praxis/Pathway. Server changes use Pathway;
READ/SEARCH uses Controller and Mneme Adhoc access/search without fabricated
ActionableTasks/Praxes/Erga. Controller is not generalised to every interaction.

Horos replaces logical Praxis “Endpoint” meaning only. No prior occurrence
requiring a literal rename was found in the inspected canonical/legacy prose;
existing Ergon PathwayEndpoint interfaces and genuine FHIR/network/Camel endpoints
retain their meanings. Diadromí is neither Task/Pragma nor Ergon; Terma is not Ergo.
Ergon acquires no discrete Information Architecture representation by being an Ergon.

Pylai carries identity into the established Authentication/Themis boundary; it
does not derive authority from caller claims. Significant transactions are auditable
through Kleio, independently of Journey. Journey may record applicable business
progression or interoperability failure; Dokimasia may use that evidence against
Praxis. Pathway structure, Praxis expectations, Journey observations, Audit and
Assurance remain distinct. Dokimasia terminal constraints remain unchanged.

## Diagrams and cross-references

- Updated the system map's textual responsibility hierarchy and Mermaid
  collaboration diagram for distinct Calliope lifecycles, Pylai patterns,
  Themis participation, Kleio occurrence reporting and Journey observations.
- Added text diagrams for Calliope decomposition, build lifecycle, access/persistence
  and nomenclature; Pylai recoverability, pattern selection, full ingress sequence,
  EgressHoros handover out/back, Broker and Server READ/SEARCH progression.
- Existing general Pathway/Route and Journey/assurance diagrams retain their
  established meanings. Legacy diagrams remain byte-identical inside wrappers.
- Added reciprocal navigation among the two new views, register, maps, lifecycle,
  Pathway/Journey, gaps and sources. Added U13/U14, C41–46 and explicit deferred/conflict
  anchors. No external DesignNote link is required to understand the Candidate.

## Conflicts discovered and disposition

| Existing statement / tension | Adjudicated position and disposition |
| :--- | :--- |
| C01 treated runtime repository/persistence allocation as unknown and Calliope as a foundation library | U13 unambiguously establishes Models/DataSets and existing Mneme/Mnemosyne persistence. Reconciled in all Domain05 summary views; detailed mechanisms retained CDG-24/49. |
| Definition ownership could imply runtime Criteria/Topic/Filter execution or Themis vocabulary ownership | Separated Models definitions, DataSets controlled content and Themis/Mneme runtime responsibilities. U12 policy semantics unchanged. |
| Foundational Calliope dependency rules versus runtime DataSets collaboration | **Retained CDG-49 realisation tension.** Logical runtime collaboration does not itself permit higher-layer static dependencies. Existing AGENTS rules remain; physical allocation or rule clarification needs Mark + ChatGPT before implementation. |
| Strategy Component 5 / Seam 5 says Calliope does not synchronously mediate runtime transactions | Models is not a mandatory mediator of all transactions; runtime DataSets authority is documented. If blanket Strategy wording prohibits any runtime participation, explicit clarification is needed under CDG-49. No compiled-content substitution or silent upstream amendment. |
| C14 described Pylai mostly as HL7/FHIR gateway slices | U14 establishes the broader service and three patterns. Reconciled; technology remains a realisation concern. |
| Strategy G4 / Component 4 excludes connection, framing and retry mechanics | **Retained CDG-51 conflict.** U14 Terma owns external communication mechanics. Existing Strategy remains authoritative and unchanged; no unambiguous upstream amendment is inferred. Mark + ChatGPT must adjudicate allocation/wording. |
| Ordinary ContextLoader exposes already-loaded Context; Pylai ingress establishes initial Context | Explicitly documented the special ingress case while retaining Mneme acquisition and Mnemosyne persistence, with no semantic mapping in Loader. No additional service introduced. |
| Legacy cache/queue success treated as durable ACK; ADR-018 allows a general durable work boundary | U14's specific authoritative Pragma/Task persistence requirement is recorded. REC-001 remains a minimum failure guard; its success conditions alone do not prove durable acceptance. Protocol-specific coordination/retry remains CDG-34/50. |
| Legacy direct-storage FHIR reads, header-to-authority derivation and universal work pipelines | Superseded affected sections with Controller → Mneme Adhoc access/search and established Identity/Themis interpretation. No raw-store path, public bypass or fabricated read work adopted. |
| Recoverable external interaction versus Petasos/Ponos recovery | Diadromí and Controller explicitly non-recoverable; retained evidence does not resume them. Queue/handoff recovery remains Petasos, execution remains Ponos; uncertain previous effects remain AX-15. |
| Logical Praxis Endpoint ambiguity and egress result handling | Introduced precise Horos specialisations and pre-Unloader handover-back ordering. Existing interface/technical endpoint terms retained. Detailed Horos representation CDG-50. |
| Journey/transaction audit/assurance conflation and remote ACK → Task completion claims | Separate occurrence, observation, interpretation and result; legacy accounts wrapped unchanged. General parent/Task outcome contracts remain CDG-18, not invented from ACK. |
| Kleio audit-persistence authorisation exemption versus AX-07 | **Retained CDG-42.** No subsequent authoritative resolution found. Required audit capture and Themis governance both remain; no bypass adopted. |

## Deferred design preserved

Calliope deferrals include exact runtime ontology boundary/authority/review and
synchronisation, DataSet lifecycle/version/effective-date/history, historical Context
versus later dataset versions, development representations/code generation/IDE/AI
mechanisms, runtime APIs, packaging/cache/physical persistence, detailed workflow/UI
and final nomenclature/release-specific ValueSet inventories.

Pylai deferrals include Communication Information Unit schema/lifecycle and Domain04
mapping, Terma lifecycle/connector implementation, Diadromí representation,
protocol-specific acknowledgement/persistence/queue coordination, segmented/RMI
interactions, uncertain-effect/safe-retry detail, Server APIs/status projection,
token/claim carriage, telemetry and Journey/assurance observation contracts.
Broader Controller use remains a possible implementation consideration. Horos
representation/runtime handover mechanics remain deferred beyond established ordering.
PEP-Criteria representation and all previously recorded Identity deferrals remain.

## Legacy supersession and preservation

| Legacy file | Affected sections | New wrappers |
| :--- | :--- | ---: |
| `docs/concepts/calliope.md` | Opening, architectural rationale, §2, positive ownership, §4 | 5 |
| `docs/concepts/calliope-canonical.md` | Opening and §1; existing §2 wrapper unchanged | 2 |
| `docs/modules/calliope.md` | Opening, single-library architecture paragraph, positive ownership | 3 |
| `docs/concepts/pylai.md` | Opening/rationale, §2, positive ownership, REC-001 and REC-002 guarantees | 6 |
| `docs/concepts/pylai-gateways.md` | Opening, §1 summary/diagram, §2 ingress and §3 outbound | 4 |
| `docs/modules/pylai.md` | Opening, §1 tree, ingress guarantee, interceptor authority bullet, positive ownership | 5 |
| `docs/integration/pylai.md` | Opening, §1, §2.2, §3.2 delivery bullet, §4.1 and §4.2 | 6 |
| `docs/security/pylai-security.md` | §1, §2 and §3 independently | 3 |
| `docs/getting-started/terminology.md` | Only Calliope, Pylai and REC-001 rows | 3 |
| `../../docs/latex-legacy` | Opening, REC-001, ACK/Task-state and FHIR direct-read/change description | 4 |

All 41 wrappers provide specific RADS replacement references. Original content,
including earlier wrappers, code, diagrams, genuine endpoint names and configuration,
is unchanged. No whole document is marked superseded. Removal of only the inserted
wrapper bytes reproduces all ten pre-task files byte-for-byte against Git HEAD.
Both external DesignNotes retain the hashes above. The supplied Dokimasia legacy
orientation already had appropriate supersession/navigation and was not modified.

## Verification actually executed

**Architecture suite: PASS.** Executed with a 180-second command bound:

```bash
timeout --signal=TERM --kill-after=10s 180s mvn test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

Maven completed normally in **16.234 seconds**, exit 0, BUILD SUCCESS. **90 tests;
0 failures, 0 errors, 0 skipped** across these eleven classes:

| Architecture test | Tests |
| :--- | ---: |
| ParadeigmaIsolationArchitectureTest | 4 |
| PetasosApiIsolationArchitectureTest | 3 |
| PackageLayeringArchitectureTest | 4 |
| SecurityEnforcementArchitectureTest | 32 |
| GovernedWriteCompositionArchitectureTest | 7 |
| MnemosyneAuthoritativePersistenceArchitectureTest | 6 |
| AgoraIsolationArchitectureTest | 7 |
| ProviderRegistryArchitectureTest | 2 |
| IrisDecouplingArchitectureTest | 5 |
| PylaiPublicationBoundaryArchitectureTest | 8 |
| GovernedWriteContractArchitectureTest | 12 |

No timeout, stall or unresolved test execution occurred. These tests verify current
implementation guardrails; they do not prove architectural acceptance of the new
documentation. No tests were added for this documentation-only change.

**Local link / Markdown validation: PASS.** Validation used the installed
`markdown_it` CommonMark parser with tables enabled, local path/fragment resolution,
fence closure and table-width checks across changed/new Markdown, including this
report. Final counts: **22 Markdown files, 553 local links, 54 tables and 161 fenced
blocks**. No missing files/anchors or malformed
tables/fences were found. Nine added LaTeX wrapper references also resolve.
`git diff --check` passes.

**Legacy preservation: PASS.** Insertion-manifest reconstruction and removal of
only new wrapper bytes reproduced all ten baseline files exactly. Original
external-note hashes also matched. **Scope isolation: PASS.** No Domains01–04,
axioms, AGENTS rules or production/test/configuration files changed; the Themis
and Kleio component sections remain byte-identical.

Text lifecycle diagrams were checked against the task sequences and the modified
Mermaid source was inspected. No Mermaid render or LaTeX typesetting was run;
no renderer was available in the inspected environment. External HTTP links were
not network-validated. Validation scripts/manifests and raw Maven output were
temporary verification artefacts under `/tmp/`, not new repository authority.

## Remaining Mark + ChatGPT adjudication and assessment

Mark + ChatGPT review is still required for the CDG-51 Strategy/Terma allocation
conflict, CDG-49 repository realisation/Strategy clarification where material, and
the pre-existing CDG-42 Kleio audit-authorisation conflict. CDG-49/50 retains
deliberate design deferrals; settling implementation mechanisms is outside this task.
Existing general converter/activity, Task outcome, reference/version and Journey
contracts remain open where the task supplied no adjudication.

**Calliope + Pylai + Praxis.Horos are now represented faithfully enough for
Domain05 Candidate Architecture review.** Their adjudicated meanings can be read
in RADS without external DesignNotes or reverse-engineering code. The retained
cross-layer conflicts are visible subjects for that review, not declarations of
consistency or acceptance. Whole-Domain05 Candidate Architecture acceptance,
upstream coverage and the Domain Completion Gate are not established by this task.
