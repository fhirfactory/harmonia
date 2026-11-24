# Capability Modelling Metamodel Deferred Register Update

**Date:** 2026-10-08

**Scope:** Documentation control only.

**Outcome:** Active item **04 — Capability Modelling Metamodel and Identifier Conventions** recorded with status **Deferred**. Formal canonical reconciliation remains outstanding.

## Files Changed

| File | Change |
|---|---|
| [docs/deferred-document-register.md](../../docs/deferred-document-register.md) | Added one coherent active deferred item; replaced the statement that there were no active Domain 02 Strategy items. Preserved resolved items 01–03, their resolution history and the maintenance/lifecycle section byte-for-byte. |
| `.junie/reports/2026-10-08-capability-metamodel-deferred-register-update.md` | Added this completion report. |

The requested `docs/markdown/deferred-document-register.md` did not exist. The existing register was at `docs/deferred-document-register.md`, which is indexed in `docs/README.md` and referenced by earlier register work. After requesting location clarification, the update proceeded in that existing location. The register was updated in place; no duplicate register or document relocation was introduced.

## Active Deferred Item Added

Item 04 records that the decisions were identified/recovered during architectural review following the Domain 02 Health Service Assurance Strategy reconciliation on 2026-10-08. The approved Strategy derivation is linked as review context, without claiming that it already documents every recovered metamodel decision.

The item captures the recovered decisions as architectural decisions supplied for this task, rather than new Codex proposals:

- CT1/CT2/CT3 capability-set inclusion/encompassing, with Business Enabling contextual/natural views excluded from Capability Tiers and identifier ancestry.
- FT/SN/FN/PS element types and the boundary between strategically relevant articulation and premature downstream Application Architecture or implementation derivation.
- BL/BE/EN architectural namespaces; the Business Layer canonical structural identifier, higher-tier shorter forms and unambiguous reference/summary forms that preserve canonical identity.
- Relocation to a new canonical structural identifier, retirement and alias retention of the previous identifier, and prohibition on reassignment of retired identifiers.
- Rejection of a special “cross-cutting capability/feature” construct; consumption across capabilities without ownership transfer, membership change, identity change or duplicate definition.
- Capability-scoped Information Object ownership/definition, consumption elsewhere, and downstream Data Object realisation without redefining the established business information meaning.
- Required future assessment against CM-R01 Coverage, CM-R02 Orthogonality, CM-R03 Semantic Clarity, CM-R04 No Junk Drawers and CM-R05 Global Capability Name Uniqueness.
- [AX-17 — Architectural Authority and Explicit Uncertainty](../../docs/architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty), truthful traceability and the prohibition on completing gaps from conventions, lexical similarity, ArchiMate/TOGAF familiarity or anticipated implementation.

The reason for deferral is explicit: recovered decisions require formal documentation and reconciliation into canonical architecture documentation, while this task authorises only capture and documentation control. Recording the debt does not establish that reconciliation has been completed. Item 04 remains **Deferred**.

## Validation Performed

No dedicated committed documentation/link validation suite was found in the inspected scripts, build configuration and validation-file inventory. Bounded temporary documentation checks were used; no repository test definitions were added or modified.

| Check | Result |
|---|---|
| Markdown and local links | **PASS** — both task files parsed using `markdown_it`; local link targets and heading anchors checked; code fences closed; final newlines and whitespace checked. External URLs were not network-tested. |
| Register history | **PASS** — the original preamble and all content from resolved items through maintenance/lifecycle remain byte-identical; exactly one active item 04 is recorded as Deferred. |
| Scope preservation | **PASS** — SHA-256 comparison against 2,269 incoming tracked/untracked files: only the existing register changed; the other 2,268 remain byte-identical. The completion report is the only new repository file. Incoming staged and unstaged work was preserved. |
| Diff/whitespace review | **PASS** — task-scoped `git diff --check`, `git diff HEAD --check` and review of the register diff; the new report was also checked directly for whitespace. |
| Required architecture suite | **PASS** — exit 0, BUILD SUCCESS, Maven elapsed 18.143 seconds; 90 tests across 11 architecture suites, zero failures, errors or skipped tests. Maven output and Surefire XML agree. No timeout or stall occurred. |

Architecture command, bounded to 180 seconds:

```bash
timeout --signal=TERM --kill-after=10s 180s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

Documentation and preservation command, bounded to 30 seconds:

```bash
timeout --signal=TERM --kill-after=5s 30s python3 /tmp/harmonia-capability-metamodel-validate.py
```

Task-local diagnostics are retained under `/tmp/harmonia-capability-metamodel-*`, including the incoming file manifest, original register, initial Git status, validation script and architecture-test log. These are verification artefacts; the recovered decisions are recorded in the register itself.

## Relevant Pre-Existing Observations

- The register path differed from the requested path, as recorded above.
- [The capabilities index](../../docs/markdown/02-strategy/capabilities/index.md) and [the capability tier model](../../docs/markdown/02-strategy/capability-maps/capability-tier-model.md) contain “Cross-cutting capabilities” wording. This presents a reconciliation issue against the recovered rejection of that special classification. It was reported, without silently resolving the wording or reinterpreting the existing model.
- The current Strategy material explicitly preserves unresolved capability placement, ancestry and structural identifiers. Recovery of the metamodel decisions does not establish those element-specific facts. Existing identifiers were not inferred, normalised or migrated.
- The workspace already contained substantial staged and unstaged architecture/documentation work. That incoming work was preserved.

## Scope and Uncertainty Confirmation

No downstream architectural reconciliation was performed. Domain 01 Motivation, Domain 02 Strategy capability artefacts, Domain 03 Business Architecture, Domain 04 Information Architecture, Architectural Axioms, ADRs, source code, tests and capability identifiers elsewhere remain unchanged from task entry. No Capability Model change, capability/Feature rename or repository-wide identifier migration was performed.

Exact BE and EN canonical structures, Information Object and Data Object identifier abbreviations/syntax, deletion, replacement, split and merge semantics, and additional unrecovered relationship names or semantics remain explicitly unresolved. No missing metamodel detail was inferred. Future reconciliation is outstanding; item 04 remains **Deferred**.
