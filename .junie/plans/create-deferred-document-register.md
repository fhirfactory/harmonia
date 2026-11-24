---
sessionId: session-261005-195112-1w7p
---

# Requirements

### Overview & Goals

The objective of this task is to establish a formal **Deferred Document Register** in `./docs/deferred-document-register.md` to track architectural items, review findings, and documentation refinements that have been intentionally deferred during incremental architecture passes.

Specifically, following the **Strategy Pass A.1** review (Canonical Capability & Feature Reconciliation in Domain 02), three specific wording and alignment actions were identified for resolution during **Pass C** (Domain 02 final Strategy reconciliation). This register captures these items so that:
1. They are not lost during subsequent authoring passes (e.g. Authoring Pass B: Strategic Resources, Courses of Action, and Logical Components).
2. They do not artificially expand the scope of completed passes (adhering to the principle of bounded incremental authoring).
3. Clear destination targets, origins, and statuses are maintained for governance traceability.

### Scope

#### In Scope
- Authoring `docs/deferred-document-register.md` with preamble, column schema, and the three deferred items from Strategy Pass A.1 review.
- Indexing the newly created register in `docs/README.md` under the root documentation hierarchy.
- Verifying markdown formatting, table syntax, and reference integrity.

#### Out of Scope
- Executing the deferred changes in `docs/markdown/02-strategy/capabilities/business-capabilities.md` or `business-enabling-capabilities.md` (these remain scheduled for Pass C).
- Commencing Authoring Pass B or Pass C.
- Modifying production code, architecture tests, or LaTeX documents.

### Functional Requirements

- **FR-1: Standardized Register Schema**: The register must present items using the four required columns:
  - `Item`: Detailed description of the deferred work or adjustment.
  - `Arose From`: Provenance of the finding (e.g., `Strategy Pass A.1 review`).
  - `Intended Destination`: Target architectural domain or pass where resolution will occur (e.g., `Domain 02 — final Strategy reconciliation (Pass C)`).
  - `Status`: Current state of the item (e.g., `Deferred`).
- **FR-2: Faithful Item Transcription**: The three specified items from the Strategy Pass A.1 review must be recorded verbatim:
  1. *Residual implementation-prescriptive wording* (`cryptographic`, `tamper-proof`, `immutable`).
  2. *Authoritative clinical record vs vendor-neutral longitudinal clinical representation*.
  3. *Harmonia Enabling Role statements vs solution-mechanism wording*.
- **FR-3: Master Documentation Indexing**: `docs/README.md` must be updated to register `docs/deferred-document-register.md` within the platform's information architecture tree.

### Traceability & Architectural Alignment

- **Axiom AX-04 (Technology Independence & Separation of Concerns)**: Item 1 and Item 3 protect the separation of business capability statements from solution-mechanism prescriptions.
- **Axiom AX-07 / ADR-007 (Harmonia as Interoperability Authority, Not Universal Clinical Truth)**: Item 2 aligns clinical representation statements with Harmonia's true role as a governed, vendor-neutral interoperability and preservation environment rather than a source-of-truth EHR.
- **AGENTS.md Section 6.2 (Scope Discipline)**: Registering deferred items preserves the bounded scope rule ("One plan. One current milestone. One next step.") by deferring reconciliation to its designated pass without losing auditability.

# Technical Design

### Current Context

During **Strategy Authoring Pass A.1** (documented in `.junie/reports/2026-10-05-strategy-authoring-pass-a1-reconciliation.md`), canonical capabilities and features in `docs/markdown/02-strategy/` were reconciled to eliminate component leakage and implementation-derived feature inflation.

During that review, three items were identified for final cleanup in **Domain 02 Pass C (Strategic Value Stream & Final Reconciliation)**. To maintain strict boundaries between Pass A.1, Pass B, and Pass C, these items must be tracked in a dedicated register under `./docs`.

### Proposed Changes

#### 1. Create `docs/deferred-document-register.md`

The file will be structured as follows:

```markdown

# Harmonia Deferred Document Register

This register records documentation tasks, wording adjustments, and architectural refinements that have been identified during architecture reviews but deferred to designated downstream passes or domains.

The register ensures governance traceability, prevents premature implementation during earlier passes, and provides an authoritative backlog of deferred documentation items.

---

## 1. Active Deferred Items

| Item | Arose From | Intended Destination | Status |
|---|---|---|---|
| Remove residual implementation-prescriptive wording such as `cryptographic`, `tamper-proof`, and `immutable` from Business Enabling Features where the required behaviour can be stated independently of mechanism. | Strategy Pass A.1 review | Domain 02 — final Strategy reconciliation (Pass C) | Deferred |
| Replace wording describing Harmonia as providing the “authoritative historical clinical record” with wording reflecting Harmonia's governed, vendor-neutral longitudinal clinical representation and preservation responsibilities. | Strategy Pass A.1 review | Domain 02 — Business Enabling Capabilities / Pass C | Deferred |
| Review Business Capability “Harmonia Enabling Role” statements and remove residual solution-mechanism wording so they describe strategic enablement rather than premature solution design. | Strategy Pass A.1 review | Domain 02 — Business Capabilities / Pass C | Deferred |

---

## 2. Maintenance & Lifecycle

- **Deferred**: Identified during review; intentionally held for a future authoring pass or domain.
- **In Progress**: Actively being addressed within the target pass.
- **Resolved**: Reconciled and verified against applicable Architectural Axioms and quality rules.
- **Superseded**: Rendered obsolete by an accepted Architecture Decision Record (ADR) or axiom update.
```

#### 2. Update `docs/README.md`

In `docs/README.md`, under `## 1. Documentation Information Architecture`:
Add `docs/deferred-document-register.md` to the root `docs/` hierarchy:
```markdown
docs/
├── README.md                                 # Master documentation index & navigation (this file)
├── AGENTS.md                                 # Authoritative architectural rules for autonomous agents
├── deferred-document-register.md             # Register of deferred architectural documentation items
...
```

### File Structure & Impact

| File | Action | Description |
|---|---|---|
| `docs/deferred-document-register.md` | **Create** | Authoritative register containing active deferred items. |
| `docs/README.md` | **Update** | Index the new register in the documentation tree. |

### Risks & Mitigations

- **Risk**: Premature application of deferred wording changes to `docs/markdown/02-strategy/` before Pass C.
  - **Mitigation**: The register explicitly isolates the items as `Deferred` with target `Pass C`; no edits to Strategy Markdown files are made during this task.
- **Risk**: Inconsistent table formatting or broken links.
  - **Mitigation**: Strict GitHub-flavored markdown formatting and validation of documentation links.

# Delivery Steps

### ✓ Step 1: Create Deferred Document Register in docs directory
`docs/deferred-document-register.md` is created with clear purpose, structural metadata, and the three deferred items from Strategy Pass A.1 review.

- Create `docs/deferred-document-register.md` following Harmonia documentation standards and markdown conventions.
- Add an architectural preamble establishing the register's purpose: tracking items deferred from architectural reviews, governance passes, and scope handoffs across Harmonia documentation domains.
- Populate the register table with the three deferred items arising from Strategy Pass A.1 review:
  1. Removal of residual implementation-prescriptive terms (`cryptographic`, `tamper-proof`, `immutable`) from Business Enabling Features in Domain 02 Pass C.
  2. Replacement of "authoritative historical clinical record" wording with governed, vendor-neutral longitudinal clinical representation and preservation responsibilities in Domain 02 Pass C.
  3. Review of Business Capability "Harmonia Enabling Role" statements to eliminate solution-mechanism wording in Domain 02 Pass C.
- Include columns: `Item`, `Arose From`, `Intended Destination`, and `Status`.

### ✓ Step 2: Index register in docs README and validate documentation integrity
`docs/deferred-document-register.md` is formally indexed within `docs/README.md` and verified for markdown table and link integrity.

- Update `docs/README.md` under Section 1 (Documentation Information Architecture) to index `docs/deferred-document-register.md` alongside existing architectural registers and axioms.
- Verify Markdown table syntax, column alignment, and backtick code span formatting.
- Confirm repository invariance across production code, build files, and LaTeX sources (`git status`).