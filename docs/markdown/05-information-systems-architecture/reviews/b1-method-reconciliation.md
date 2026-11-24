<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 B1 — Documentation Reconciliation for Adjudication

**2026-10-13 — returned to Mark/ChatGPT. B1 PASS is not declared.
B2 capability mapping has not commenced.**

This is a reconciliation and validation record, not an additional source of
architectural decisions. The explicit task decisions are recorded as
[U20](../sources.md#u20); their canonical meaning is held in the linked
architecture documents.

## 1. Result and canonical locations

The supplied Metabolē construct is established in Domain04 as a reusable
[information-progression pattern](../../04-information-architecture/patterns/metabole.md),
alongside the Information Unit metamodel. This location expresses progression
across one or more Units without inventing an Information Family, application
component or universal runtime container. Unit/Context and provenance definitions
link to it.

The [Step B traceability method](../traceability/step-b-method.md) is the
single maintained source of B1-R01–R12 and the graph. The
[Business Architecture metamodel](../../03-business-architecture/metamodel/business-architecture-metamodel.md#3-function-and-service-semantics)
holds the reconciled Harmonia Service/Function/Process definitions. The separate
[Health Information Access example](../traceability/health-information-access-worked-example.md)
validates the method with six Function-level traces and additive progression.

Pragma and Pylai.Diadromí each **realise Metabolē**, using genuine architectural
Realisation in the ArchiMate/TOGAF sense. The canonical pattern shows the two
realisations converging on the conceptual construct; their application
definitions, register entries and logical-data views reference that relationship.
They are neither synonyms nor inherited types and share no inferred manager,
lifecycle, physical representation or persistence requirement. Mneme retains
Pragma/Context management; Pylai retains Diadromí responsibility.

The explicit A4 PASS decision is reflected in current programme/index standing.
Earlier dated review accounts retain their historical scope and are visibly
subordinate to the current acceptance. Candidate acceptance does not establish
upstream coverage, MVP or final Domain05 completion.

## 2. Files created

| File | Purpose |
| :--- | :--- |
| [Domain04 Metabolē](../../04-information-architecture/patterns/metabole.md) | Canonical concept, ActivityContext, Realisation, progression and provenance boundaries. |
| [Step B method](../traceability/step-b-method.md) | Twelve governing rules, graph, accountability and application restraint. |
| [Health Information Access example](../traceability/health-information-access-worked-example.md) | Agreed Service, six Functions and additive information trace. |
| [This review record](b1-method-reconciliation.md) | Reconciliation inventory, conflicts, validation and retained questions. |

## 3. Files modified

| File | Reconciliation |
| :--- | :--- |
| [Domain03 metamodel](../../03-business-architecture/metamodel/business-architecture-metamodel.md) | Canonical Service/Function/Process definitions, recursive decomposition, accountability and graph. |
| [Domain03 overview](../../03-business-architecture/README.md) | Align semantic summaries and remove categorical exclusion of retrieval/search decomposition. |
| [Behaviour index](../../03-business-architecture/behaviours/index.md) | Align semantic summaries and clarify reading order versus mandatory hierarchy. |
| [Business Processes](../../03-business-architecture/processes/business-processes.md) | Reconcile Process inclusion rule while preserving catalogue and existing states/boundaries. |
| [Domain04 overview](../../04-information-architecture/README.md) | Record governed extension and pattern navigation. |
| [Information metamodel](../../04-information-architecture/metamodel/information-architecture-metamodel.md) | Link Units/Management Context to Metabolē and distinguish Context meanings. |
| [Provenance governance](../../04-information-architecture/governance/authority-custody-provenance.md) | Separate derivation provenance from audit and assurance evidence. |
| [Domain05 overview](../README.md) | Current A4 acceptance/B1 scope, historical standing and navigation. |
| [Calliope](../calliope-models-and-datasets.md) | Shared ActivityContext semantic governance without authorisation transfer. |
| [Candidate feature map](../candidate-feature-map.md) | Accepted baseline and method reference; no new allocations. |
| [Candidate system map](../candidate-system-map.md) | Realisation reference without adding Metabolē as a software component. |
| [Component/construct register](../component-register.md) | C07/C44 Realisation references and current baseline standing. |
| [Context/Pragma management](../context-and-persistence-management.md) | Pragma Realisation, Context distinctions and current scope. |
| [Logical data/persistence](../data-and-persistence.md) | Distinct Pragma/Diadromí Realisation references without new persistence contracts. |
| [Pylai DesignNote](../design-notes/DesignNote-Pylai-External-Interoperability-Architecture.md) | Supplied attachment's Diadromí definition linked to canonical Realisation and non-recoverability. |
| [Pathway/Pragma semantics](../pathways-journeys-and-handoff.md) | Pragma Realisation reference preserving payload/progression meaning. |
| [Pylai architecture](../pylai-external-interoperability.md) | Diadromí Realisation preserving Pylai responsibility and transience. |
| [Source dispositions](../sources.md) | U20 explicit task decisions and canonical incorporation references. |
| [Sylloge](../sylloge-binary-content-management.md) | Distinguish historical U17 standing from current A4 acceptance and bounded B1 scope. |
| [Candidate gaps](../gaps.md) | Correct historical U17 status wording without changing any gap disposition. |
| [Completion Plan](../../architecture-completion-plan.md) | Current A4 PASS, B1 method reference/adjudication scope and B2 restraint. |

No production code, module/deployment structure, axiom, candidate identity,
Feature allocation, gap-register disposition or existing legacy document is
changed. No legacy architectural meaning is imported; no supersession wrapper
is required for this task.

## 4. Pre-existing terminology and standing conflicts

| Finding | Reconciliation |
| :--- | :--- |
| Function described only as internal behaviour; Service principally as exposed Function behaviour. | Retain owning responsibility and consumer boundaries while defining state-transition and Service Contract semantics, direct use of Functions/Processes and bounded accountability. No mechanical one-Service-per-Function relationship. |
| Domain03 metamodel, overview and Process catalogue categorically exclude search, mapping and other generic operations from Processes. | Replace the exclusion with justified ordered Function decomposition. Operation labels and Function size do not decide decomposition; recursive Function/Process composition is permitted. Existing named Processes, ownership and state qualifications remain. |
| Domain04 provenance described as an immutable audit/evidentiary chain. | Define derivation/origin meaning explicitly and preserve provenance ≠ audit evidence ≠ assurance evidence. Kleio/Dokimasia responsibilities remain separate. |
| Current overview/Completion Plan still say A4 pending and Step B not started. | Apply the user's explicit A4 PASS/accepted-baseline decision and bounded B1 scope. Dated Step-A records remain history. |
| Existing Health Information Access catalogue uses Clinical Information Search Service, Clinical Resource Retrieval Service and broader Function names. | Preserve those identities. Health Information Retrieval Service is the agreed B1 example name; no alias, rename, replacement or catalogue-to-example mapping is inferred. Those relationships belong to later traceability passes. |

The existing narrower Business Process progression descriptions remain useful
within their scopes; they no longer redefine the general Harmonia Process.
Existing assurance non-recursion restricts assurance of assurance, not ordinary
recursive Function decomposition, and remains intact.

## 5. Validation

Execution evidence is held under `/tmp/harmonia-domain05-b1/`; it is not
architectural authority. These are verification outcomes, not a B1 gate decision.

| Check | Result and scope |
| :--- | :--- |
| Architecture suite | **PASS** — 90 tests across 11 classes; no failures, errors or skips; Maven BUILD SUCCESS in 17.649 seconds. Command: `mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`, bounded by a 480-second timeout. |
| Markdown structure / document rendering | **PASS** — CommonMark/table parsing across 69 documents, 181 tables and 229 fenced blocks; no structural findings. All 25 B1 files generated temporary HTML successfully. `git diff --check` is clean. |
| Local links | **PASS** — 1,635 local file/anchor links across the same scope; no broken links or anchors. |
| Mermaid source checks | **PASS within source-review scope** — 18 Mermaid blocks extracted from affected files; both new/revised diagrams checked for graph structure, node delimiters and required relationships. The B1 graph retains direct Service/Function use, optional Processes, recursive Function decomposition and three distinct Information edge types. |
| Mermaid rendering | **UNRESOLVED / tool unavailable** — no installed Mermaid CLI/parser; diagrams have not been independently rendered. Source checks do not establish render success. |
| LaTeX generation | **PASS** — isolated temporary copy built with `latexmk -gg -pdf -interaction=nonstopmode -halt-on-error main.tex`, bounded by a 150-second timeout; 260-page PDF produced. Existing overfull/underfull box and oversized-float warnings remain in unchanged LaTeX sources. This is the existing LaTeX publication smoke check, not a claim that the independent Markdown B1 additions are included in that PDF. |
| Semantic reconciliation | **PASS within B1 scope** — searches/review found no remaining contradictory current Service/Function/Process definition in the affected architecture, synonymy of Pragma/Diadromí with Metabolē, or requirement that every Function consumes the whole progressing Metabolē. All 12 rule anchors, six Functions with requires/consumes/produces, and exactly the six supplied initial ActivityContext examples were checked. |
| Responsibility / preservation | **PASS** — Calliope, Themis, Kleio, Mneme, Mnemosyne, Pylai and Dokimasia register responsibility entries remain byte-identical; the separate Pragma/Diadromí entries gain Realisation references, and Energeia's historical status wording is clarified. Candidate component IDs, Feature rows, Process catalogue identities and all gap dispositions are unchanged. Axioms and 487 pre-existing legacy Markdown/LaTeX/history files remain unchanged. |

Additional DesignNotes appeared/staged during execution. They were left
untouched and are excluded from the four-created/twenty-one-modified B1 inventory
above. The validation scope distinguishes those concurrent additions from the
baseline and this reconciliation; it does not adjudicate or migrate them.

## 6. Retained questions and review boundary

No new unresolved architectural contradiction is identified in the supplied B1
semantics. Existing detailed Pragma/Context/Diadromí representations,
ActivityContext physical binding, lifecycle/security retention mechanisms,
Service Contract outcome dispositions and application contracts remain
downstream or explicitly deferred. The worked example does not settle them.

The example's enterprise Business Capability relationships, associations to the
existing Service/Function catalogue and component realisations remain to be
established by later Step B passes. Their absence here is deliberate scope
restraint, not a fabricated trace or a claim of coverage.

**Review requested: Mark/ChatGPT adjudication of the reconciled B1 documentation.
No B1 PASS is declared and no B2 work is authorised by this record.**
