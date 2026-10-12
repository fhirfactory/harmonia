<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 Sylloge reconciliation and A2/A3/A4-readiness assessment

**Date:** 2026-10-12. **Scope:** Domain05 Step A only; explicit Sylloge
architectural adjudication, affected-view reconciliation and repeated closure
assessment. This report records execution evidence, not architectural authority.

**Recommendation: READY FOR A4 ADJUDICATION.** Sylloge resolves the remaining
F31/D15 Binary Content Management responsibility question. A4 acceptance remains
**PENDING for Mark + ChatGPT**. Step A and Domain05 are not declared complete.
**Step B has not begun.**

## Authority and bounded review

Applied repository [AGENTS.md](../../AGENTS.md), [documentation instructions](../../docs/AGENTS.md),
the canonical [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md),
the [Step-A sufficiency criterion](../../docs/markdown/architecture-completion-plan.md#33-domain05-five-stage-completion-method)
and the explicit task adjudication, now self-contained as
[U17](../../docs/markdown/05-information-systems-architecture/sources.md#u17).
The preceding [U16 closure report](2026-10-12-domain05-agora-omilia-reconciliation-a2-a3-closure-assessment.md)
located the residual question; its conclusions did not substitute for authority.
The runtime master plan was consulted for boundary context; no runtime milestone
or implementation step was executed or advanced.

| Relevant axiom | Applied consequence |
| :--- | :--- |
| AX-04/14 | Binary Content Management is an application responsibility. Logical information, binary payload, application representation, persistence structure and technology remain distinct. |
| AX-05/11/12 | Mneme remains Context and Pragma Manager with its established service/access boundary. Mnemosyne remains Persistence Manager and alone establishes Harmonia authoritative durable state; Sylloge creates no raw cache/database access path. |
| AX-06/13/18 | Management, source information authority and physical custody differ. External custody may remain; Room presentation/reference/capture does not transfer the underlying binary responsibility. |
| AX-07/08/15 | Existing Themis, Kleio and uncertainty constraints remain; no new security, audit or persistence mechanism/guarantee is introduced. |
| AX-17 | The explicit adjudication resolves application ownership; detailed design remains deferred and A4 remains a separate human acceptance decision. |

## Responsibility and F31/D15 reconciliation

**Harmonia.Sylloge — Binary Content Management subsystem responsible for the
governed association, resolution, retrieval and assembly of binary content
associated with Harmonia-managed information.**

The canonical view and **C49/F31/D15** establish metadata management, Information
Unit associations, location/resolution knowledge, retrieval, on-demand assembly
from one or more payloads and knowledge of Harmonia-managed versus externally
managed content. Formal clinical/operational binary content remains within the
Candidate Architecture; it has not been deferred out to manufacture closure.

Sylloge management does not require Harmonia to persist/copy every payload.
External authoritative/source systems may retain physical custody. Mnemosyne may
provide applicable Harmonia persistence services for content or metadata without
acquiring Binary Content Management; Sylloge is not a generic persistence manager.
Mneme/Ponos/Ergo or another applicable processing component may obtain or resolve
binary content through Sylloge; detailed interaction contracts remain deferred.

Agora retains native collaboration media and its collaboration record/reference/
activity concerning formal binary content. Presentation, links, discussion,
notifications and captured activity confer no custody of that content. Sylloge
retains Binary Content Management; Omilia remains collaboration operations and
acquires no binary-management responsibility. Originating logical information
authority is preserved.

## Files changed and affected views

All architecture changes are confined to RADS. This report is the only new
execution artefact in the repository.

| File | Bounded change |
| :--- | :--- |
| [sylloge-binary-content-management.md](../../docs/markdown/05-information-systems-architecture/sylloge-binary-content-management.md) — new | Self-contained U17 responsibility, custody, Mneme/Mnemosyne and Agora/Omilia boundaries, Mermaid responsibility diagram and explicit deferrals. |
| [candidate-system-map.md](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md) | Sylloge in the application hierarchy and collaboration map; Information Unit association, processing use, applicable persistence and Agora references; removed obsolete unassigned binary area. |
| [component-register.md](../../docs/markdown/05-information-systems-architecture/component-register.md#c49) | C49 Sylloge; C18 Agora's obsolete binary-question statement reconciled. C03 Kleio, C09 Mneme and C12 Mnemosyne bodies are unchanged. |
| [candidate-feature-map.md](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md) | F31 explicitly allocated to Sylloge Binary Content Management; carrier/storage representations remain deferred. |
| [data-and-persistence.md](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md) | D15 information responsibility and directly related D02, collaboration/binary ownership and historical structure explanations reconciled. Logical/application/persistence/technology levels and Clinical/C&G/O&A domains preserved. |
| [agora-omilia-collaboration.md](../../docs/markdown/05-information-systems-architecture/agora-omilia-collaboration.md#binary-and-media-custody) | Formal binaries allocated to Sylloge; native collaboration media, record/reference and Omilia operations retained. |
| [gaps.md](../../docs/markdown/05-information-systems-architecture/gaps.md#binary-custody-question) | CDG-25 responsibility closed with detailed deferrals retained; directly affected CDG-52 cross-reference corrected. Other 50 existing gap rows unchanged. |
| [sources.md](../../docs/markdown/05-information-systems-architecture/sources.md#u17) | U17 provenance, bounded legacy review and current disposition; U16 statements identified as historical snapshots. |
| [Domain05 README](../../docs/markdown/05-information-systems-architecture/README.md#sylloge-adjudication) | Navigation and current A1/A2/A3/A4-readiness assessment; preceding U16 snapshot retained with its subsequent resolution explicit. |
| [architecture-completion-plan.md](../../docs/markdown/architecture-completion-plan.md#33-domain05-five-stage-completion-method) | Only current A2/A3 status cells and Domain05 status row reconciled. A4 pending, Step A open and Step B not started. No gate, sequence or later stage changed. |
| [This report](2026-10-12-domain05-sylloge-reconciliation-a2-a3-a4-readiness-assessment.md) | Reconciliation, validation and independent readiness evidence. |

No production code, tests, POMs, deployment configuration, upstream capability or
Domain04 information model changed. Existing middleware relationships were
reviewed; no new middleware contract or map row is required by this adjudication.

## Legacy review and contradictions

Targeted searches covered clinical binary content/payloads, media custody and
repositories, Agora media ownership, DocumentReference and F31/D15 in legacy
Markdown/LaTeX, module material and historical execution reports/plans.
Compatible examples include Ergon binary-bearing input, FHIR metadata/resource
catalogues, Iris document views, persistence/cache catalogues and homeserver
native-media volumes. These do not assign a competing Binary Content Manager.

**New legacy wrappers: 0.** No legacy architectural meaning was newly incorporated
or displaced. All 487 checked tracked legacy/historical Markdown/LaTeX files are
byte-identical to HEAD; all existing 175 wrappers in 57 files remain balanced and
unchanged. Historical reports retain their dated conclusions. No compatible
implementation detail was wrapped merely because Sylloge now exists.

The directly contradictory current RADS statements were the unassigned F31/D15
owner, CDG-25's open responsibility and associated map/collaboration/closure
references; these are reconciled. No directly contradictory Strategy/axiom or
upstream register allocation was established. Strategic component profiles,
axiom responsibility summaries and build-subproject counts do not prohibit
additional application responsibilities. Those sources and root AGENTS.md remain
unchanged; no Mnemosyne redesign or upstream capability derivation was required.

## Deferred Sylloge design retained

Storage technology (object/filesystem/BLOB/PACS/VNA/content-addressable), streaming,
caching, replication, binary sharding, compression, encryption mechanisms and
physical topology remain unselected. FHIR Binary/DocumentReference/Media and DICOM
representations remain unselected. Detailed retrieval APIs, assembly algorithms,
lifecycle/retention, binary versioning and external repository adapters remain
deferred. None requires universal copying into Harmonia. These detailed matters
are not additional A2 responsibility blockers.

## Validation

| Verification | Result and limits |
| :--- | :--- |
| Required architecture suite | **PASS:** 90 tests in 11 architecture classes; 0 failures, errors or skips; BUILD SUCCESS, 18.224 s, exit 0. Existing implementation invariants verified, not a claim of Sylloge implementation. Maven emitted read-only local-cache metadata warnings but completed using available dependencies. |
| Markdown structure and local links | **PASS:** 16 Markdown files, 604 local links, 52 tables and 56 fences checked across all Domain05 Markdown plus changed programme/report material. CommonMark/table parsing, source table widths, closed fences, unique explicit anchors and local file/fragments pass. No broken links or structural errors. External URLs were not fetched. |
| Legacy-wrapper preservation | **PASS:** all 487 checked legacy/historical files unchanged; 175 wrappers in 57 files balanced; 53 LaTeX RADS replacement links resolve. No new wrappers. |
| Architectural consistency/scope | **PASS:** C49/F31/D15 and affected maps agree. 50 unrelated gap rows, C03/C09/C12 bodies and axioms unchanged. No production/build/deployment edits or Step B work. |
| Focused LaTeX rendering | **PASS:** seven directly related, unchanged legacy chapters/appendices compiled in two bounded passes to a 39-page PDF; binary-attachment example page visually inspected. Final log: no undefined references; 23 overfull and 67 underfull boxes and one oversized float remain in these unchanged legacy sources. No full-publication claim. |
| Mermaid | Fences/source/relationships reviewed, including the new Sylloge diagram and modified collaboration map. **Renderer/parser unavailable:** no mmdc/Mermaid CLI or installed Mermaid module. Render validation NOT RUN; no rendered success or tooling failure claimed. Pandoc is also unavailable; RADS was not converted to LaTeX. |
| Diff hygiene | **PASS:** git diff --check; documentation changes only. |

Architecture command:

```bash
timeout --signal=TERM --kill-after=10s 180s mvn -B -ntp test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

Markdown/local-link/wrapper checks use installed markdown-it-py and BeautifulSoup,
following the established temporary validation-runner convention. LaTeX uses
repository styles with `timeout --signal=TERM --kill-after=10s 120s pdflatex
-interaction=nonstopmode -halt-on-error` per pass. Included chapters/appendices:
03-information-architecture, 04-data-architecture, appendix-ergon-module,
05a-collaboration-agora, 24-matrix-synapse, appendix-i-persistence-register and
appendix-iris-user-interfaces. No command stalled or timed out.

Temporary evidence: `/tmp/harmonia-domain05-sylloge-architecture-tests.log`,
`/tmp/harmonia-domain05-sylloge-legacy-search.txt`,
`/tmp/harmonia-domain05-sylloge-validate.py`,
`/tmp/harmonia-domain05-sylloge-validation.json` (including original-file hashes),
and `/tmp/harmonia-domain05-sylloge-render/`. These are diagnostic artefacts,
not architectural authority.

## Independent repeated closure assessment and stopping boundary

Reviewed current system/component/feature/information/middleware boundaries,
source dispositions and all CDG-01–52 against Step-A sufficiency. CA-01–03 retain
their U16 resolution. **CA-04/CDG-25 is now RESOLVED by the explicit Sylloge
assignment.** This supplies the formerly absent application responsibility; it
does not substitute persistence or a representation for that owner.

Remaining detailed contracts and discovery do not imply missing application
ownership. In particular, terminology/Local Directory proposals (CDG-24/33)
remain explicitly unselected/unreconciled proposals, and historical EMPI/source-
authority claims (CDG-29) do not establish current master-patient authority.
Detailed assurance, security, execution, persistence and Pylai matters retain
their existing dispositions. No upstream coverage assessment was conducted and
no hypothetical new responsibility is inferred from absent implementation detail.

| Stage | Revised assessment |
| :--- | :--- |
| A1 — Candidate reconstruction/documentation | **COMPLETE** — adjudicated knowledge and its limits are self-contained in RADS. |
| A2 — Architectural review/adjudication | **COMPLETE** at Candidate responsibility level — final F31/D15 question resolved; no further concrete unresolved Candidate Architecture responsibility blocker established. |
| A3 — Candidate correction/reconciliation | **COMPLETE WITH NON-BLOCKING DEFERRED ITEMS** — affected views reconciled and validated; detailed design remains explicit. |
| A4 readiness | **READY FOR A4 ADJUDICATION** — recommendation only; A4 acceptance remains **PENDING for Mark + ChatGPT**. |
| Step B | **NOT STARTED**. |

Return the Candidate Architecture to Mark + ChatGPT for architectural review.
No A4 acceptance, Step-A/Domain05 completion, Step B, implementation planning or
runtime step is declared or commenced.

## Canonical Documentation Assessment

All required Sylloge knowledge from the explicit adjudication is now canonical
in RADS. Compatible legacy carriers/storage listings remain supporting detail;
no required decision depends on them or solely on conversation. No additional
migration, legacy rewriting or broader Domain05 cleanup was undertaken.
