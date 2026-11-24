<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 Agora/Omilia reconciliation and repeated A2/A3 closure assessment

**Date:** 2026-10-12. **Scope:** Domain05 Step A, reconciliation of the supplied
adjudicated collaboration architecture and fresh closure assessment. This report
records execution/assessment evidence; it is not architectural authority.

The Agora/Omilia adjudication is reconciled. The previous roster, Agora record
custody and collaboration-to-formal-record blockers are resolved. Native
collaboration media ownership and the non-transfer of referenced binary ownership
are also resolved. **A2 remains INCOMPLETE on one narrower non-collaboration binary
application-custody question; recommendation: NOT READY FOR A4 ADJUDICATION.**
A4 remains a Mark + ChatGPT decision. **Step B has not begun.**

## Authority and review scope

Applied repository [AGENTS.md](../../AGENTS.md),
[documentation instructions](../../docs/AGENTS.md), the canonical
[Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md) and
[Step-A completion criteria](../../docs/markdown/architecture-completion-plan.md#33-domain05-five-stage-completion-method).
The task expressly makes
`DesignNote-Agora-Controlled-Collaboration-and-Omilia-Realisation.md` the current
adjudicated input. Read §§1–17 in full from Downloads; incorporated decisions are
recorded as [U16](../../docs/markdown/05-information-systems-architecture/sources.md#u16)
and are self-contained in RADS. The note's working status/location supplies no
independent authority; the explicit task authorises reconciliation. Original
external content is unchanged.

Reviewed current Domain05 component, system, feature, data/persistence, middleware,
source/disposition and gap views, following the related Context/persistence,
Calliope, Pylai/Horos and Pathway/Journey boundaries. The previous
[closure assessment](2026-10-12-domain05-boundary-reconciliation-a2-a3-closure-assessment.md)
was used to locate CA-01–04, not to supply architectural authority. All CDG-01–52
dispositions were considered for the fresh responsibility assessment. The
established Strategy logical-responsibility/seam view and collaboration/clinical
discourse/General Collaboration/adjacent roster statements were used as constraints,
without deriving new capabilities or conducting coverage analysis. No upstream
change was required.

Legacy searches covered Agora, Synapse/Matrix Synapse, Matrix, Element, Rooms/Spaces,
membership/roster, dialogue/messages, formal-record handoff and binary/media custody
across architecture/concept/module/middleware, overview/glossary, deployment,
configuration, security, Provider Registry, LaTeX and historical execution material.
Runtime catalogues, commands and reports remain implementation/historical evidence.
No runtime-convergence implementation step or later Domain05 stage was undertaken.

| Relevant axiom | Reconciliation consequence |
| :--- | :--- |
| AX-02/04 | Harmonia.Agora owns collaboration semantics/control; Harmonia.Omilia owns operational machinery. Synapse/Element lineage supplies provenance, not architectural identity or inferred design. |
| AX-05/11/12 | Agora application ownership and Omilia operational mechanics do not replace Mneme managed access or Mnemosyne authoritative durable establishment. Collaboration persistence remains deferred; no independent store/direct-JPA authority is granted. |
| AX-06/18 | Roster SEP supplies context without owning Room membership; publication through Agora does not transfer originating clinical/operational/workflow information authority. |
| AX-07/08/09 | Themis governance, zero-PHI metadata/diagnostics and Kleio authoritative audit remain. Retained dialogue and logged activity are not automatically AuditEvent. No Kleio authority/capture change. |
| AX-10/14/15 | Defined capture, original record ownership, new governed information, handoff and uncertain effects stay distinct. No every-message-to-Pragma or exactly-once interpretation. |
| AX-17 | Adjudicated boundaries replace obsolete Candidate statements; genuine missing responsibility and deliberate detailed deferrals remain explicit. No architecture is invented to manufacture closure. |

## Reconciliation and contradictions resolved

| Previous statement/question | Reconciled Candidate Architecture |
| :--- | :--- |
| Agora as a Matrix projection/integration gateway only | **Harmonia.Agora** controls Harmonia native Teams/Slack-like collaboration, managed lifecycle/membership/publication intent and listening/capture policy. |
| Synapse as architectural collaboration identity or operations treated as Agora-owned | **Harmonia.Omilia.Server**, derived from Matrix Synapse, owns/performs underlying Room/Space, user, membership, dialogue/message, event and applicable media operations. **Harmonia.Omilia.Client**, derived from Element, provides collaboration experience. **Agora controls; Omilia operates.** |
| One-way notification surface or historic Matrix endpoints taken as the new interface | Omilia.Server exposes the **Application Services API** for Agora operation requests and dialogue/activity delivery from **all** Agora-managed Rooms/Spaces. Exact technology/upstream mapping remains deferred. |
| Provider Registry/Harmonia roster authority and authoritative roster determining membership | Roster Management is **SEP**. R1.x/R2.x needs applicable **Shift Start / Shift End** periods only. Context may inform Agora's membership intent; Omilia.Server performs membership operations. PractitionerCapacity is not a roster. No scheduling, allocation, substitution or workforce management added. |
| Competing mapping/JPA/homeserver custody descriptions | Agora owns/manages native collaboration information/state; Omilia performs operations. AX-05 remains controlling. Legacy store/schema allocations are retained history, not alternative current information owners; detailed collaboration persistence is deferred. |
| Harmonia source information becoming Agora-owned on publication | Agora owns its collaboration representation/reference/activity; the originating responsibility retains the underlying information. Clinical, operational, workflow, task, escalation, approval and notification conduit use does not transfer ownership. |
| Generic clinical submission/commit-before-dialogue-purge | **Capture/derivation:** defined meaningful interactions may yield new Communication Information Units and/or Pragma for established governed processing. The original Agora dialogue/action stays Agora's. Not every utterance/message/keystroke becomes work or clinical record content. No NLP/AI/UI mechanism selected. |
| Captured dialogue/logged activity conflated with authoritative audit | Dialogue is retained as collaboration information; activity may be logged; audit-worthy occurrences go through **Kleio**, which constructs/captures authoritative audit information under its unchanged architecture. |
| All media treated as one unallocated collaboration store | Agora-native media is collaboration information; referenced formal binaries retain their originating ownership. Agora can own the reference/activity without owning the object. The residual F31/D15 question is isolated below. |

The canonical Diagnostic Report example shows publication of “New Diagnostic
Report Available”, a defined “Report Reviewed” interaction returning through
Omilia.Server/Application Services API to Agora, capture as new governed
information/activity and established Harmonia processing. Neither the Diagnostic
Report nor the original dialogue changes owner. No fixed Room taxonomy, interaction
recognition algorithm, new execution framework or clinical-work authority follows.

## RADS and governance files changed

| File | Change |
| :--- | :--- |
| [agora-omilia-collaboration.md](../../docs/markdown/05-information-systems-architecture/agora-omilia-collaboration.md) — new | Self-contained adjudicated control/operations, membership/roster, ownership/conduit, capture, audit, media and deferral view; API Mermaid and worked text diagrams. |
| [README.md](../../docs/markdown/05-information-systems-architecture/README.md) | Navigation, U16 reconciliation and dated independent A1/A2/A3/A4-readiness assessment. |
| [candidate-system-map.md](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md) | Agora and separate Omilia subsystem/Server/Client; bidirectional API, roster context, audit occurrence and Petasos relationships; residual binary question. |
| [component-register.md](../../docs/markdown/05-information-systems-architecture/component-register.md) | C18 corrected; C47 Omilia.Server and C48 Omilia.Client; PractitionerCapacity boundary names Agora rather than upstream Synapse. Kleio body unchanged. |
| [candidate-feature-map.md](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md) | F22–24 reconciled; F31 media custody split; F46 documents already-adjudicated Omilia operations/client. No upstream capability derivation. |
| [data-and-persistence.md](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md) | D02/D14/D15, information-authority terminology and historical collaboration structures distinguish ownership, operations, durable establishment and deferred storage. |
| [middleware.md](../../docs/markdown/05-information-systems-architecture/middleware.md) | M08 and technology-boundary row distinguish Agora control/API, Omilia operations/client, governed capture, Petasos and Kleio. |
| [gaps.md](../../docs/markdown/05-information-systems-architecture/gaps.md) | CDG-21–23 responsibility closure; narrowed CDG-25; CDG-52 explicit collaboration deferrals. Identity item 9 retains mechanisms with corrected Omilia naming. The other 47 existing gap rows are unchanged. |
| [sources.md](../../docs/markdown/05-information-systems-architecture/sources.md) | U16 provenance, L12/L20 dispositions and bounded legacy reconciliation inventory. Required architectural meaning no longer depends on the external note. |
| [AGENTS.md §2](../../AGENTS.md#2-system-taxonomy--module-hierarchy) | Agora taxonomy corrected and canonical Agora/Omilia boundary linked; build/dependency constraints and Invariant 10 preserved. |

The four other Domain05 detailed views, axioms, Strategy/Domain03/Domain04,
Architecture Completion Plan, production source, tests, POMs and deployment
configuration are unchanged. Programme status was not advanced; Step A remains open.

## Legacy sections wrapped and preserved

**47 new bounded wrappers across 18 legacy files.** Each links to the applicable
canonical Agora/Omilia section. Removing only added wrapper bytes restores every
original file exactly; the validation stores original SHA-256 hashes. Existing
wrappers, original prose/figures/tables/code and contradictory history remain.
No whole legacy document was superseded.

| Legacy file | Newly wrapped sections/content | Wrappers |
| :--- | :--- | :--- |
| [Agora concept](../../docs/concepts/agora.md) | Opening, architectural rationale, §2, §4, §5, §6 intro/figure/6.1, §6.3, §7, §8 and §9. Existing §3/6.2 wrappers retained. | 10 |
| [Agora module reference](../../docs/modules/agora.md) | Opening, §2.3 reconciliation-service bullet, §3 schemas and §4 projection/clinical-save invariant. | 4 |
| [Synapse middleware](../../docs/middleware/matrix-synapse.md) | Opening, topology figure, §2 configuration/retention, §4 capability table and §5 API catalogue. | 5 |
| [Agora LaTeX chapter](../../docs/latex-legacy/chapters/05a-collaboration-agora.tex) | Opening, ADR section, projections/lifecycle, membership-reconciliation subsection and AS/Petasos section. | 5 |
| [Synapse LaTeX chapter](../../docs/latex-legacy/chapters/24-matrix-synapse.tex) | Opening, Space hierarchy and retention/archival sections. | 3 |
| [Persistence chapter](../../docs/latex-legacy/chapters/10-postgresql-persistence.tex) | Collaboration-database subsection only. | 1 |
| [Persistence register](../../docs/latex-legacy/chapters/appendix-i-persistence-register.tex) | Collaboration database row and paired Agora table-definition items. | 2 |
| [Terminology](../../docs/getting-started/terminology.md) | Agora and AS rows. | 2 |
| [LaTeX glossary](../../docs/latex-legacy/chapters/appendix-a-glossary.tex) | Agora and AS rows. | 2 |
| [Architecture at a glance](../../docs/getting-started/architecture-at-a-glance.md) | Agora subsection and diagram wrapper explicitly limited to Agora subgraph/collaboration edge. | 2 |
| [Introduction](../../docs/getting-started/introduction.md) | Agora bullet. | 1 |
| [Root README](../../README.md) | Agora taxonomy row, Agora module group and diagram wrapper explicitly limited to Agora Collaboration Tier. | 3 |
| [Documentation README](../../docs/README.md) | Agora inventory row. | 1 |
| [System inventory](../../docs/architecture/system-inventory.md) | Three collaboration traceability rows; only identity/ownership classification superseded. Runtime values retained as historical evidence. | 1 |
| [LaTeX taxonomy](../../docs/latex-legacy/chapters/02-conceptual-taxonomy.tex) | Agora collaboration subsection. | 1 |
| [LaTeX middleware matrix](../../docs/latex-legacy/chapters/appendix-d-middleware-matrix.tex) | Synapse summary row and Synapse capability subsection. | 2 |
| [Iris design-system account](../../docs/architecture/iris-design-system.md) | Synapse-under-Agora ownership implication in one bullet only. | 1 |
| [Iris LaTeX appendix](../../docs/latex-legacy/chapters/appendix-iris-user-interfaces.tex) | Synapse-under-Agora ownership implication within middleware-subordination item only. | 1 |

Other runtime/module/configuration catalogues and registration/verification
examples remain historical implementation support. Legacy ADR-011's restricted,
self-hosted, non-federated position and its LaTeX copies remain compatible with
retained isolation constraints; no new federation decision is made. Historical
reports, including the prior closure assessment, retain their dated conclusions.
No retrospectively rewritten execution history or blanket document supersession.

## Fresh closure assessment

The assessment compared current component, feature, data, middleware, map,
terminology, source-disposition and CDG responsibility boundaries. Settled owners
are distinguished from absent detailed mechanisms, unselected proposals, historical
contradictions and future traceability. Missing schemas, retention values, APIs,
algorithms or deployment decisions do not alone block A4 readiness.

| Previous issue | Fresh disposition |
| :--- | :--- |
| CA-01 / CDG-21 — roster/membership authority | **RESOLVED.** SEP roster context, Agora membership intent and Omilia membership operations are distinct and assigned. |
| CA-02 / CDG-22 — native collaboration custody | **RESOLVED at responsibility level.** Agora owns native collaboration information; Omilia operates; AX-05 remains. Persistence realisation is deliberately deferred, not a competing owner. |
| CA-03 / CDG-23 — formal-record handoff | **RESOLVED.** The candidate now establishes capture/derivation of defined interactions through governed processing, retaining original record ownership; obsolete generic submission/purge transition is replaced. |
| CA-04 / CDG-25 — binary/media custody | **PARTLY RESOLVED; one A2 responsibility question remains.** Native media and references are allocated; F31/D15 formally managed clinical-binary application custody outside collaboration is still unassigned. |

The **only remaining A2 architectural responsibility question** is:

> Which application responsibility manages the candidate F31/D15 formally managed
> clinical binary content outside native Agora collaboration, or is that behaviour
> explicitly deferred/outside the current Candidate Architecture scope?

The evidence is the [F31 allocation](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md)
and [D15 responsibility](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md),
which previously recorded an unknown binary custodian and still have no adjudicated
application assignment. U16 names the owner of native collaboration and preserves
originating ownership of other information; it does not name this unassigned
Harmonia clinical-binary responsibility or establish reference-only scope. The
[precise retained question](../../docs/markdown/05-information-systems-architecture/gaps.md#binary-custody-question)
does not require a new service: an existing responsibility or explicit scope
disposition could resolve it. Mnemosyne durability and DocumentReference metadata
support do not by themselves identify the application custodian of the content.

This is not a request to design binary storage, media retrieval, link integrity,
retention or a Media Server. No one of those deferred mechanisms is elevated to a
blocker. Native Agora media persistence, API detail, membership derivation and
capture representation likewise do not prevent coherent collaboration responsibility.
No additional specific responsibility blocker was established by the review.

## Deferred items retained

[CDG-52](../../docs/markdown/05-information-systems-architecture/gaps.md#agora-omilia-contracts)
and the canonical collaboration view retain all deliberate DesignNote deferrals:

- Room/Space taxonomy, creation criteria and detailed lifecycle.
- Detailed membership derivation and use of PractitionerCapacity/Shift Start/End.
- Exact Application Services API contract/technology and upstream mapping.
- Agora collaboration persistence/storage, mapping/transaction allocation,
  binary/media persistence/access/link mechanics and retention policy.
- Capture provenance, Communication Information Unit representation, exact Pragma
  specialisations and definitions of meaningful interactions.
- Free-text interpretation, including any NLP/AI/recognition/UI mechanism.
- Detailed audit-event catalogue, logging architecture and security/privacy controls.
- Omilia.Server/Client modifications and Omilia.Client/Iris integration.
- Detailed external/federated Matrix interaction, notifications/escalation and
  optional Statistics/Tasks/Diagnostics projections/providers.

These are non-blocking design/discovery matters. Existing unrelated detailed
Context/Pragma, execution, assurance, Pub/Sub, Journey, security, persistence,
search, terminology and Pylai deferrals remain. CDG-19 partial/uncertain transaction
effects remain under AX-15; no exactly-once guarantee is imported. Optional proposals
and R3 adaptation remain explicitly unselected/deferred. No new Step-B coverage
question, capability, Information Concept or speculative responsibility is generated.

## Validation and test results

| Verification | Result and limits |
| :--- | :--- |
| Required architecture suite | **PASS:** 90 tests, 0 failures/errors/skips, 11 architecture classes; BUILD SUCCESS, Maven elapsed **22.686 s**, exit 0. No stall/timeout. Existing implementation invariants are checked; this does not prove Omilia implementation. |
| Architecture/document consistency | **PASS for supplied adjudications:** responsibility comparison across all changed views; original Kleio C03 and axioms byte-identical; 47 unrelated existing gap rows unchanged. The single CDG-25 responsibility question remains explicit. |
| Legacy preservation | **PASS:** 18 edited legacy files reconstructed exactly from HEAD plus recorded wrapper insertions; original content SHA-256 recorded. All 175 wrappers in 57 legacy files balance, including 47 new wrappers. No whole-document supersession. |
| Markdown and local links | **PASS:** CommonMark/table parsing, source table widths, closed fences, unique explicit anchors and local file/fragment targets. Zero new or pre-existing broken links in checked scope. Final counts below. External network URLs were not fetched. |
| LaTeX replacement references | **PASS:** all 53 RADS replacement links in legacy LaTeX resolve to existing files/fragments. |
| Mermaid | **Source/fence/reference review PASS; rendering NOT RUN.** Three new/modified canonical diagrams express the adjudicated boundaries; no local `mmdc`, `mermaid` or Mermaid CLI module is available. No rendered Mermaid success is claimed. Historical diagrams remain unchanged inside scoped wrappers. |
| LaTeX render | **PASS, focused build:** all eight changed chapters/appendices compiled into a 32-page PDF; first-page wrapper visually inspected. Baseline comparison also passes. Both runs have identical warning counts: 17 overfull, 83 underfull, one oversized float and three undefined-reference mentions. Existing layout/reference warnings are not repaired. This is not a full master-publication build. |
| Diff/scope | **PASS:** `git diff --check`; documentation/governance changes only. No production/test/build/deployment changes or Step B activity. |

Architecture command:

```bash
timeout --signal=TERM --kill-after=10s 180s mvn -B -ntp test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

Documentation validation uses installed `markdown-it-py`/BeautifulSoup and a
temporary runner, consistent with the established repository assessment approach.
LaTeX uses repository styles with only the eight changed chapters/appendices;
each run was bounded with `timeout --signal=TERM --kill-after=10s 120s pdflatex
-interaction=nonstopmode -halt-on-error`. No command stalled or timed out.

Evidence: `/tmp/harmonia-domain05-agora-architecture-tests.log`,
`/tmp/harmonia-domain05-agora-validate.py`,
`/tmp/harmonia-domain05-agora-validation.json`,
`/tmp/harmonia-domain05-agora-legacy-insertions.json`,
`/tmp/harmonia-domain05-agora-legacy-sections.json` and
`/tmp/harmonia-domain05-agora-render/` (current/baseline logs and PDFs).
These are temporary diagnostic artefacts, not architectural authority.

Final documentation counts, including this report: **25 Markdown files, 630 local
link occurrences, 69 tables, 172 fenced blocks and 11 Mermaid blocks**; zero broken
local targets/fragments, malformed table widths, duplicate explicit anchors,
unclosed fences or unbalanced wrappers.

## Independent conclusions and stopping boundary

| Stage | Assessment |
| :--- | :--- |
| **A1 — Candidate reconstruction/documentation** | **COMPLETE** — adjudicated knowledge is self-contained in RADS; unknowns/proposals are explicit. |
| **A2 — Architectural review/adjudication** | **INCOMPLETE** — only the F31/D15 non-collaboration binary application-custody/scope question above prevents closure. |
| **A3 — Candidate correction/reconciliation** | **COMPLETE WITH NON-BLOCKING DEFERRED ITEMS** — all supplied adjudications are reconciled. A later A2 decision may require one further bounded correction; this does not declare programme closure. |
| **Ready for A4 adjudication?** | **NO — NOT READY FOR A4 ADJUDICATION**, recommendation pending the specific A2 question. A4 is neither decided nor marked complete. |
| **Step B** | **NOT STARTED** — no capability/information traceability or upstream coverage/excess/MVP assessment. |

The next architectural action is Mark + ChatGPT adjudication of that single
responsibility/scope question, not implementation or detailed binary design.
No later stage or runtime step was commenced.

## Canonical Documentation Assessment

Relevant architectural material was found outside RADS: the supplied DesignNote,
the 18 bounded legacy sources listed above and supporting runtime/configuration,
ADR, binary-payload and historical execution references. Their collaboration
control, operations, membership, custody, capture, audit and media-boundary meaning
belongs in Domain05 and is incorporated by this authorised task. Superseded
legacy bodies remain unchanged with RADS references. Other technical catalogues
are retained supporting material; unestablished detail remains CDG-19/25/52 and
the other existing deferrals, not adopted architecture. No required incorporated
collaboration decision remains solely in the external DesignNote. The residual
binary responsibility is explicitly unresolved in RADS. Neither Domain05 nor the
Architecture Completion Programme is declared complete.
