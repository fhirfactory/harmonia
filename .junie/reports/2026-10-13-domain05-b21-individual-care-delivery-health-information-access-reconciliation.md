<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 B2.1 — Individual Care Delivery / Health Information Access Reconciliation

**2026-10-13 — documentation reconciliation complete. B2.1: PASS.
B2.2 was not commenced.**

## 1. Result, authority and scope

The explicitly supplied B2.1 decisions are incorporated into RADS. The
[Business Capability catalogue](../../docs/markdown/02-strategy/capabilities/business-capabilities.md#01-individual-care-delivery)
now defines exactly six L2 Individual Care Delivery capabilities, with the
agreed definitions and essential state changes:

1. 01.01 Individual Health Assessment.
2. 01.02 Clinical Determination.
3. 01.03 Individual Care Planning.
4. 01.04 Clinical Intervention.
5. 01.05 Individual Health Monitoring.
6. 01.06 Individual Care Review.

Each is explained through Definition, State Change, Boundary, Illustrative
Activities, Example Care Contexts and Explicit Exclusions. The catalogue holds
the architectural-distinction decomposition rule and canonical B2-R05/R06.
L3 decomposition is not currently required; examples and care contexts do not
create subordinate capabilities or a formal care-context taxonomy.

The [B2.1 trace](../../docs/markdown/05-information-systems-architecture/traceability/b2-1-individual-care-delivery-health-information-access.md)
records the agreed L1 relationship, its retained rationale, and all six L2
`enabled-by Health Information Access` relationships with the supplied rationales
and qualifications. The [Step B method](../../docs/markdown/05-information-systems-architecture/traceability/step-b-method.md#capability-enablement-semantics)
holds enablement semantics, references the Domain02 context rules and defines
B2-R07. Material enablement transfers neither clinical accountability nor
authority to act and does not mandate invocation on every runtime exercise.

Authority is the repository Architectural Axioms, applicable AGENTS.md and
Completion Plan, authoritative Domain02/03 responsibilities and the explicitly
authorised task decisions, recorded as [U21](../../docs/markdown/05-information-systems-architecture/sources.md#u21).
This report is execution evidence, not an independent architectural authority.

Material axioms and consistency:

| Axiom | Application |
| :--- | :--- |
| AX-01 | Health Information Access remains concerned with individual clinical, health and care information; operational/corporate information is not included merely because it informs healthcare work. |
| AX-07 | Access to care intent confers no authority to perform care; existing security responsibility is retained. |
| AX-14 | Assessment, determination, intent, performed action, identified change and care-effectiveness evaluation retain their distinct meanings. |
| AX-17 / AX-18 | Only supplied relationships/decomposition are recorded; clinical responsibility is not inferred from enablement. Unestablished mappings, runtime allocation and capability ancestry remain unestablished. |

No axiom conflict or new architectural decision was identified. No production
code, tests, runtime configuration, component allocation, Domain03 or Domain04
architecture was changed. The user-linked
[Domain03-to-Domain04 traceability framework](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md)
was inspected; no change was required for this capability-enablement reconciliation.

## 2. Files created

| File | Purpose |
| :--- | :--- |
| [B2.1 trace](../../docs/markdown/05-information-systems-architecture/traceability/b2-1-individual-care-delivery-health-information-access.md) | Canonical PASS record, L1 and six L2 relationships/rationales and preserved boundaries. |
| `.junie/reports/2026-10-13-domain05-b21-individual-care-delivery-health-information-access-reconciliation.md` | This reconciliation and validation report. |

## 3. Files modified

| File | Change |
| :--- | :--- |
| [Business Capabilities](../../docs/markdown/02-strategy/capabilities/business-capabilities.md) | Six L2 definitions/state changes and educational explanations; no-L3 rule; decomposition rule and B2-R05/R06; B2.1 cross-reference. |
| [Business Enabling Capabilities](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) | Clarifies that existing Service Delivery contextual entries are not ICD L2 decomposition; existing entries/Features are preserved. |
| [Capability index](../../docs/markdown/02-strategy/capabilities/index.md) | Navigation to six L2 capabilities and modelling rules. |
| [Domain05 index](../../docs/markdown/05-information-systems-architecture/README.md) | Current B2.1 PASS/B2.2-not-commenced standing and trace navigation; dated review history retained. |
| [Step B method](../../docs/markdown/05-information-systems-architecture/traceability/step-b-method.md) | Enablement semantics, Domain02 rule references, B2-R07 and current bounded status; all B1-R01–R12 text preserved. |
| [B1 Health Information Access example](../../docs/markdown/05-information-systems-architecture/traceability/health-information-access-worked-example.md) | Reconciles deferred-anchor/status wording with the separate B2.1 trace; six Function-level traces are preserved. |
| [Candidate system map](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md) | Replaces current B2-not-started wording; no software hierarchy or diagram change. |
| [Sylloge](../../docs/markdown/05-information-systems-architecture/sylloge-binary-content-management.md) | Current-status correction only; no Sylloge mapping. |
| [Gap register](../../docs/markdown/05-information-systems-architecture/gaps.md) | Current-status correction only; existing gap dispositions retained. |
| [Source register](../../docs/markdown/05-information-systems-architecture/sources.md) | U21 decision provenance and distinction between earlier review scope and current bounded result. |
| [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md) | Step B and current-programme status reflect B2.1 PASS while retaining subsequent completion gates. |

## 4. Conflicting or incomplete material reconciled

- Domain02 previously described ICD only at L1. Its operational scope now uses
  the six agreed abilities, and the explicit L2 decomposition replaces reliance
  on general clinical activity/setting descriptions.
- No existing Acute / Planned-Scheduled / Longitudinal ICD L2 decomposition was
  found in the inspected repository text. No obsolete decomposition was deleted
  or retained as a competing current model. New normative text explicitly rules
  out that decomposition.
- Existing Acute Care and other Service Delivery entries belong to the
  Business Enabling contextual catalogue and its Domain03 behaviour/Feature
  references. They were not mechanically renamed, removed, mapped or recast as
  ICD abilities. A Domain02 clarification makes the distinction explicit.
- Current Domain05 method/index/system-map/Sylloge/gap and programme statements
  that B2 had not started contradicted the authorised B2.1 PASS. They now state
  the bounded result and retain B2.2 as not commenced.
- The B1 worked example's deferred Business Capability anchor wording now points
  to the separate B2.1 result. Its Functions and information trace are unchanged.
- The dated [B1 review record](../../docs/markdown/05-information-systems-architecture/reviews/b1-method-reconciliation.md)
  and historical reports/source accounts retain their original review scope.
  Their then-current B2-not-started statements do not override U21/current
  canonical standing. No independent B1 PASS declaration was inferred.
- The L2 rationales preserve direct acquisition of assessment information,
  operational-availability exclusion from Health Information Access and the
  distinction between reading an intervention intent and authority to act.

## 5. Validation and test results

All potentially blocking commands used explicit timeouts and termination grace.
No command timed out or stalled. Temporary scripts, rendered HTML, search output
and build logs are under `/tmp/harmonia-domain05-b21-aeWn9h/`; no validation
harness or generated publication was added to the repository.

| Check | Result and limits |
| :--- | :--- |
| Required architecture suite | **PASS** — `timeout --signal=TERM --kill-after=10s 180s mvn -B -ntp test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`. BUILD SUCCESS; 90 tests across 11 architecture classes, zero failures/errors/skips; Maven elapsed 21.680 seconds. This validates existing implementation guardrails, not runtime implementation of clinical capabilities. |
| Repository documentation validation | **PASS** — `timeout --signal=TERM --kill-after=10s 60s make -C docs/libreoffice validate`. Existing ODT package entries, mimetype, required XML parsing and 15 embedded diagrams passed the supplied validator. This is package validation of the existing legacy publication, not a regenerated RADS document or independent full ODF-schema certification. |
| Markdown structure and links | **PASS** — temporary MarkdownIt/CommonMark validation with table support rendered edited documents and checked fences, table widths, explicit anchor uniqueness, local files/fragments, Domain02/05 documents and incoming RADS links to edited targets. No missing files/anchors or structural errors found. No dedicated repository Markdown test runner was found. |
| Decision fidelity and scope | **PASS** — exact comparison after whitespace normalisation confirms all six supplied definitions, six state changes and seven rationales. Exactly six L2 entries, 36 educational elements and seven explicit enablement relationships; no L3 or subsequent B2 mapping heading. Unrelated L1 definitions and B1-R01–R12 are unchanged. |
| Preservation | **PASS** — 487 existing legacy Markdown/LaTeX and historical `.junie` files remain unchanged against HEAD. Domains03/04 and implementation files are untouched. |
| Mermaid | **PASS** for source preservation: all four existing Mermaid blocks in modified documents are unchanged against HEAD, and their Markdown fences parse. Manual source review found no affected relationship. **UNRESOLVED** for independent Mermaid parsing/rendering: `mmdc` is unavailable locally. No diagram was introduced or edited. |
| LaTeX publication compatibility | **PASS** — isolated copy of `docs/latex`, built with `timeout --signal=TERM --kill-after=10s 180s latexmk -pdf -interaction=nonstopmode -halt-on-error main.tex`; generated the existing 260-page book. Existing overfull/underfull boxes and oversized floats remain. This unchanged legacy book is not an export of the updated Markdown; no publication/source file was rewritten. |
| Markdown-to-LaTeX export | **UNRESOLVED / unavailable tooling** — Pandoc is not installed and the existing publication pipeline does not consume these RADS Markdown files. Markdown parsing/rendering passed; direct Markdown-to-LaTeX conversion is not claimed. |
| Stale decomposition search | **PASS** — reviewed `rg` results across documentation Markdown/LaTeX for ICD, Acute Care, Scheduled/Planned Care and Longitudinal Care. Remaining uses are legitimate contexts, existing enabling/behaviour references, review history or explicit decomposition prohibitions. |
| Runtime-dependency and subsequent-step search | **PASS** — reviewed `enabled-by`, runtime/invocation wording and later B2 references. Mandatory-runtime language occurs in explicit prohibitions/qualifications, not as enablement semantics; B2.2 references only preserve the stop boundary. |
| Diff hygiene | **PASS** — `git diff --check`; only documentation/report files are changed. |

## 6. Unresolved architectural questions and stopping boundary

No new unresolved architectural question is required to represent the supplied
B2.1 decisions. Existing candidate deferrals, unestablished capability ancestry,
detailed application allocation and broader Health Information Access edge cases
remain outside this task. B1's historical review standing is preserved without
inferring a separate approval. Tooling limitations above are validation limits,
not newly invented architecture gaps.

**B2.2 was not commenced.** No other Business Capability or Business Enabling
Capability trace was added; no further capability decomposition, runtime
dependency model, architecture decision or implementation work was introduced.
Work stops after reconciliation, validation and this report.

### Canonical Documentation Assessment

1. Repository Markdown/LaTeX searches found no relevant ICD decomposition or
   required B2.1 architectural knowledge held solely outside RADS.
2. Outside-RADS material consulted comprised repository/documentation governance,
   existing publication build/validation tooling and temporary prior validation
   helpers used only for tool mechanics. None supplied new architectural meaning.
3. The task-supplied adjudications, rather than legacy implementation or historical
   reports, supplied the six abilities and relationship decisions.
4. Capability definition, decomposition and context independence belong to
   Domain02; enablement traceability and Health Information Access scope belong
   to Domain05. Their canonical locations are linked above.
5. All supplied B2.1 architectural meaning was incorporated within this task.
   No legacy architectural section was imported or rewritten; no supersession
   wrapper was necessary.
6. No additional migration is required for this bounded result. No whole-domain
   completion or subsequent mapping approval is declared.
