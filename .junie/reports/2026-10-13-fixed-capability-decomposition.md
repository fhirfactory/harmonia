<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Fixed Capability Decomposition — Completion Report

**Date:** 2026-10-13. **Repository:** fhirfactory/harmonia.
**Branch:** Harmonia-GovernedAccess.
**Outcome:** Metamodel/documentation reconciliation complete. The canonical
target is **CT1 → CT2 → CT3 → FT**. Existing catalogue reconciliation status is
preserved; Capability catalogue reconciliation and Domain05 B2.2 are not commenced.

## 1. Authority and decision record

The explicitly supplied human architectural decision is recorded using the
existing canonical review/decision-record pattern in
[Fixed Capability Decomposition — Governed Metamodel Decision](../../docs/markdown/02-strategy/reviews/fixed-capability-decomposition-decision.md).
The normative hierarchy belongs in the Domain03 metamodel, with its Domain02
Strategy interpretation. No new governance mechanism, ADR numbering or central
axiom amendment is introduced.

AX-01 bounds the model to Harmonia's health-information purpose; AX-14 preserves
Capability / Feature and Layer / Tier / Derivation distinctions; AX-17 requires
explicit supersession of the permissive target while preserving unestablished
identity, ancestry and allocation. The task is outside convergence/runtime
implementation sequencing and changes no application/component allocation.

## 2. Files changed

| File | Change |
| :--- | :--- |
| [Strategy overview](../../docs/markdown/02-strategy/README.md) | Distinguish five derivation stages from fixed Capability decomposition; link the decision and bounded CT3 rule. |
| [Business Capability model](../../docs/markdown/02-strategy/capabilities/business-capabilities.md) | Qualify legacy labels and enterprise reference coverage; replace the Individual Care Delivery no-L3-required wording with unestablished CT3 decomposition and future Feature-framing obligations. Preserve all Capability definitions. |
| [Business Enabling model](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) | Record mandatory target ancestry and existing Feature reconciliation needs, without changing the catalogue. |
| [Capability framework index](../../docs/markdown/02-strategy/capabilities/index.md) | State fixed hierarchy and semantic roles; label the separate progression as derivation stages; reconcile Individual Care Delivery and Feature context summaries. |
| [Capability decomposition/derivation model](../../docs/markdown/02-strategy/capability-maps/capability-tier-model.md) | Replace permissive decomposition with fixed tier roles, parent requirements, anti-fabrication principle and intentional CT3 boundary; retain five-stage meaning. |
| [Capability maps index](../../docs/markdown/02-strategy/capability-maps/index.md) | Separate decomposition from derivation and navigate to the governed decision. |
| [Fixed-decomposition decision record](../../docs/markdown/02-strategy/reviews/fixed-capability-decomposition-decision.md) | New canonical record of the approved decision, rationale, supersession, preserved evidence and stop boundary. |
| [Business Architecture overview](../../docs/markdown/03-business-architecture/README.md) | Align the metamodel summary with fixed Feature ancestry while retaining direct behavioural anchoring. |
| [Business Architecture metamodel](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md) | Correct the diagram, define tier roles, mandate complete Feature ancestry, prohibit skipped/deeper tiers, constrain Feature identifier paths and preserve K9 uncertainty. |
| [Reference candidate README](../../docs/markdown/03-business-architecture/reference-model/README.md) | Distinguish existing ownership evidence from reconciled CT3 framing; record unchanged capture status, source drift and validator enforcement gap. |
| [G1 review record](../../docs/markdown/04-information-architecture/reviews/package2-g1-review.md) | Add a supersession wrapper around the affected historical §10.2 paragraph and a dated §11.8 reconciliation note. All original text remains unchanged and in order. |
| [B2.1 Individual Care Delivery worked mapping](../../docs/markdown/05-information-systems-architecture/traceability/b2-1-individual-care-delivery-health-information-access.md) | Correct only the current §4 decomposition qualification; retain its existing enabled-by relationships and B2.1 standing. This does not commence B2.2. |
| [This report](2026-10-13-fixed-capability-decomposition.md) | Execution and validation record; not architectural authority. |

## 3. Normative statements added or superseded

- CT1 establishes the broad frame; CT2 its constituent Capabilities; CT3 the
  finest required Capability frame; FT required system-enabled behaviour within CT3.
- Every Feature SHALL be framed by CT3, every CT3 SHALL have a CT2 parent, and
  every CT2 SHALL have a CT1 parent. Tiers SHALL NOT be skipped. A fully
  reconciled Feature requires complete CT1 → CT2 → CT3 ancestry.
- A Feature cannot compensate for incomplete Capability decomposition. A
  meaningful CT3 must first be identified where a Feature is required. Missing
  CT3 exposes incomplete upstream modelling, not a tier-skipping exception.
- The obligation does not authorise fabricated Capabilities. Existing Features,
  established responsibilities, local IDs and partial relationships remain
  preserved with ancestry/decomposition requiring separate reconciliation.
- CT3 is the intentional Harmonia scope boundary. Harmonia does not attempt
  complete healthcare-enterprise architecture; CT4, CT5 and deeper Capability
  tiers SHALL NOT be defined.
- The identifier grammar permits a Feature token only after the complete CT3
  path. Shorter Capability paths remain valid for Capabilities and directly
  anchored Functions, Services and Processes, without intermediate Features.
- The old MAY-decompose, arbitrary-depth Feature framing and no-L3-required
  positions are superseded. L1/L2/L3 remain deprecated as canonical
  classifications; retained labels are not validated CT assignments.
- `Layer ≠ Capability Tier ≠ Derivation Stage` remains controlling. The five
  Domain02 derivation stages retain their meaning and establish no CT allocation.

**Capability Specialisation** is retained as the Feature semantic classification,
including inherited Capability semantics. It does not make FT CT3/CT4 or permit
arbitrary-depth framing. No substantive Feature-semantic redefinition was needed
to make the fixed model coherent; no new semantic question is introduced by
retaining this terminology. CT1 → CT2 and CT2 → CT3 are described as decomposition.

## 4. Historical decisions and unresolved consequences preserved

The G1 §10.2 original permissive paragraph remains historical evidence inside
explicit supersession boundaries linked to the new decision. K9 remains closed;
its original evidence, partial ownership, uncertainty and downstream derivation
permission are preserved. The dated five-region reconciliation remains unchanged;
its preservation of then-current decomposition rules is historical scope, not
current permission to bypass the superseding target model.

No legacy L1/L2/L3 label, document nesting, numbering, contextual view or catalogue
presentation has been converted into Capability identity, ancestry or an ID.
`Client Administration → Person Identity → Identifier Resolution [FT /
FEAT-EM-01]` remains a partial chain with unresolved numerical tiers, full ancestry
and complete structural Canonical ID.

Individual Care Delivery's six established child definitions and decomposition
remain unchanged: Individual Health Assessment, Clinical Determination,
Individual Care Planning, Clinical Intervention, Individual Health Monitoring
and Individual Care Review. CT3 decomposition is unestablished. Care settings
and illustrative clinical activities do not automatically become CT3, and no
CT3 is invented or assigned.

The JSON capture remains byte-identical to HEAD: **100 Capabilities** (24 Business,
76 Business Enabling), **137 Features**, **147 Functions**, **133 Services** and
**19 Processes**. It contains **zero allocated numerical Capability tiers** and
**zero complete Canonical IDs**. All **137 Features retain unresolved ancestry**;
all **69 enabling descriptions with unresolved standalone identity remain
unresolved**. The unchanged capture still reports **2,015 unresolved dimensions
and relationships** through the existing validator when source checking is
disabled; this is not a count of distinct elements or conformance to the new rule.

Capture SHA-256:
`5de1603f3bdc0f901d8355733570623f78a09b5c3ce8611f7a45e6ec196645c9`.
The schema, validator, validator tests, local Feature IDs and instance
relationships are unchanged. New diagram edges and grammar clauses express
only the approved abstract metamodel; no instance ancestry relationship is added.

## 5. Validation and results

| Verification | Result and evidence |
| :--- | :--- |
| Pre-edit `timeout 60s python3 scripts/rads_business_architecture.py validate` | **PASS**; invalid 0; 2,015 unresolved dimensions/relationships. |
| Post-edit same RADS validation | **FAIL / INVALID capture evidence**, exit 1: 401 diagnostics, comprising six changed-source fingerprints and 395 shifted evidence locators. Sources: business, enabling, metamodel, k9, tier-model, b2-1. This establishes stale capture evidence, not rejection of the new architecture. |
| `timeout 60s python3 -m unittest discover -s scripts/tests -p 'test_rads_business_architecture.py' -v` | **FAIL**, 32 tests in 1.609 seconds: 31 pass; only `test_capture_matches_authoritative_source_fingerprints_and_locators` fails with the same 401 source-drift/locator diagnostics. Preservation tests for all Feature IDs, six children/no CT3, inventory and partial ancestry pass. |
| Existing `validate(model, check_sources=False)` | **PASS**, no representation errors; unchanged 2,015 unresolved dimensions/relationships. This excludes source freshness and does not enforce the new mandatory CT3 rule. |
| Required architecture command, bounded by 480 seconds | Initial command exits 1 in 1.984 seconds before architecture tests: `/home/mhunter/.m2/repository/com/sun/mail/jakarta.mail/resolver-status.properties` is read-only. Semantic test result **UNRESOLVED** for this invocation; harness/dependency-cache problem, not production failure. |
| Offline architecture retry, bounded by 180 seconds | **PASS**, Maven BUILD SUCCESS in 25.776 seconds; 90 tests across 11 architecture classes, zero failures/errors/skips. Offline mode is the concrete correction that avoids dependency metadata writes. |
| Preservation checks against HEAD | **PASS**: JSON, schema, validator and its tests unchanged; all Business Capability definition bodies from Individual Health Assessment onward and the enabling catalogue from View 1 onward byte-preserved; every original G1 review line retained in order. |
| Residual searches over the complete `docs/markdown` tree | **PASS**: residual permissive statements occur only in the new decision's prior-position column, the explicitly superseded historical G1 paragraph, and the labelled existing-tool enforcement gap. No remaining current normative tier-skipping or direct CT1/CT2 Feature permission was found. |
| Local Markdown links/anchors | **PASS for introduced changes**: 118 Markdown files and 2,310 inline local links checked, including the untracked completion report. Canonical targets/anchors were compared with HEAD; zero newly broken links/anchors. One pre-existing broken link remains outside this task. The initial ad hoc checker could not decode a linked binary target; treating non-Markdown targets as existence checks corrected that harness issue before the successful run. |
| Whitespace and final scope checks | **PASS**: `git diff HEAD --check`, separate report whitespace check and final tracked/untracked inventory. Exactly 12 canonical documentation files plus this completion report; no code, JSON, schema or generated bytecode changes. |

Successful architecture invocation:

```bash
timeout 180s mvn -o test -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

Session diagnostics are captured under `/tmp/harmonia-fixed-capability-*.log`.
No command timed out or stalled. The source-drift failure is deliberately
retained: refreshing fingerprints/locators or reconciling catalogue ancestry
would exceed the authorised scope.

## 6. Remaining semantic and enforcement work

No further adjudication is required to establish the explicitly approved fixed
metamodel. Meaningful CT3 frames, actual tier assignments, full ancestry, the
69 unresolved standalone identities and complete Canonical ID allocations remain
for separately authorised architectural adjudication. The modelling obligation
does not decide any of them.

The existing validator still accepts resolved CT1/CT2 → Feature paths, including
an explicit passing test of that former permission. The schema alone supplies
no CT3-owner constraint. This is a known enforcement conflict with the new target,
documented in the reference README and decision record; it needs separate
tooling reconciliation without forcing unresolved instances into fabricated
hierarchies. Full source validation also requires separately authorised semantic
capture review and locator/fingerprint reconciliation.

The pre-existing reported `Coordinate Care-Team Collaboration` clinical-handover
wording conflict remains preserved in the reference capture/documentation and
is not adjudicated by this task. No Business/Business Enabling redesign or
application/component allocation follows from the new rule.

### Canonical Documentation Assessment

1. `docs/architecture-decisions.md` was inspected outside RADS to determine the
   existing decision-record mechanism. It contains the legacy ADR register,
   including separate responsibility/realisation decisions; none supplies this
   new Capability hierarchy or overrides the current RADS metamodel.
2. Existing canonical Domain02 review/decision records and the Domain03
   metamodel already provide the mechanism used here. No legacy architectural
   content was imported or rewritten, and no new mechanism was invented.
3. No additional architectural knowledge required for this bounded decision was
   found solely outside RADS. Broader ADR migration remains outside scope; the
   newly authorised decision and rationale are canonical. This report is
   execution evidence only.
4. This task does not declare any architecture domain or the wider programme
   complete. It stops after metamodel/documentation reconciliation.
