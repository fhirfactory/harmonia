<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | First bounded Calliope OO Backlog Item | Not recorded | Review the work specification only; no implementation commissioned. |
| [DR-2](#dr-2) | Existing Calliope → Themis API authority discrepancy | Not recorded | Clarify dependency authority; excluded from the item. |
| [DR-3](#dr-3) | Existing Topic rename/identity authority tension | Not recorded | Clarify scope before dependent work; item performs no rename. |
| [DR-4](#dr-4) | Bounded Minutiae and temporal provenance | Not recorded | New Task/follow-up times and filename correspondence remain UNRESOLVED. |

# HCRL 1.1.27/001 — Calliope OO Backlog Item preparation

**Status: PENDING — Solution Architect review requested.**

**Target:** `1.1.27`, explicitly identified by Mark Hunter in the current interaction
for this preparation review. Iteration `001` was unused. This carrier does not
allocate a release. Root Architecture authority remains **1.1.26**, previous
**1.1.25**. No architectural/normative amendment is applied or software version
proposed by this task. Any resulting-release treatment depends on the actual
adjudicated transition; backlog preparation alone asserts no increment.

**Originating state:** commit `dc30cfbcc7ad4113b29e3710904453c023df4dc1`, branch
`Mneme-and-Mnemosyne-Build`, initially clean. HCRL 1.1.26/001 remains complete and
unchanged. No accepted software predecessor exists for this item; the first
software version/baseline remains deferred under its accepted DR-5.

**Authority and limits:** Mark Hunter authorised independent investigation and
preparation of one coherent Backlog Item, expressly excluding Junie commissioning,
implementation changes and refactoring. The subsequent supplied investigation was
used to locate Junie's original Phase 1 result, not to commission its recommended
historical recovery. No acceptance, finalisation, release allocation, commit or
push is performed. This record requests review; it does not supply a disposition.

<a id="dr-1"></a>
## DR-1 — First bounded Calliope OO Backlog Item

**Proposed result:**
[BL-CALLIOPE-OO-01, revision 1](../../../../docs/backlog/calliope-oo-01-topic-models.md),
covering cohesive Topic/TopicMask decomposition, educational documentation,
compatibility and construction conformance. The
[independent assessment](../../evidence/1.1.27/001/after/engineering-assessment.md)
records actual source findings and the treatment of Junie's recommendations.

The pair is a bounded shared naming/neutral selection concern with accepted
immutable contracts. Independent review finds Topic's builder silently replaces
duplicate explicit positions, contrary to its accepted canonical definition; the
[probe](../../evidence/1.1.27/001/after/duplicate-position-probe.json) confirms it.
The item proposes correcting that implementation discrepancy under existing
authority, preserving valid-input behaviour and the public/serialisation surface.
Ordinary internal class/package detail remains delegated engineering. No new
architectural element, registry, package migration, task-model merger or runtime
responsibility is proposed. Other oversized files remain disclosed future work.

Junie's whole-module migration/size estimates are not adopted. The Guide's
illustrative packages and Continuum friendlyName metadata do not prescribe Java
packages. All affected production files must meet the existing complete-file
500-line rule or receive an explicit justified Solution Architect exception;
removing documentation or compatibility contracts to meet it is excluded.

Future execution requires a specific Task referencing the item ID, revision and
exact reviewed source, without duplicating its specification. Item acceptance
would not commission Junie or accept existing snapshots. Any future software
submission represents the integrated solution and must include applicable unit,
solution-wide regression/integration and conformance evidence. Missing required
verification requires explicit disposition; module success and historical
architecture-only qualifications do not satisfy that boundary.

**Actual preparation verification:** current naming-model suites PASS (23 tests)
and mandatory architecture suites PASS (90 tests), zero failures/errors/skips.
These are inspection-baseline results, not solution-wide readiness. Document/scope
validation is retained with the formal Report. No implementation/test source or
canonical authority was edited. Review the proposed work specification and its
scope; implementation acceptance remains a separate future request.

**Solution Architect disposition:** not recorded.

<a id="dr-2"></a>
## DR-2 — Existing Calliope → Themis API authority discrepancy

The root Calliope guardrail prohibits higher-layer dependencies including Themis;
the actual POM/source uses `themis-api`. The current layering test prohibits
`themis.core` while permitting API references. No explicit exemption was located
in the bounded governance/accepted-register search. Passing that test cannot
establish an exception to the instruction. See the
[assessment's source/effect analysis](../../evidence/1.1.27/001/after/engineering-assessment.md#material-discrepancies-and-adjudication-boundaries).

**Requested adjudication:** identify applicable existing authority for the API
exception or direct a bounded governed resolution. No POM/contract relocation or
test weakening is proposed here. This is outside the item's naming-model scope;
unaffected work need not await an unrelated resolution. Preserve the discrepancy's
actual applicability in later solution-conformance review; no release waiver is
inferred. **Solution Architect disposition:** not recorded.

<a id="dr-3"></a>
## DR-3 — Existing Topic rename/identity authority tension

The accepted [TopicIdentityStable Feature, E04.01.04.F01](../../../continuum/strategy/capability/feature/9210349f026b4ff1aca1092c309b4139.md)
says identities survive rename/reclassification. The accepted common Topic contract
and [1.1.25/001 DR-3](../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-3)
require immutable definitions and a different UUID for substantive replacement,
while preserving existing specialised Topic identities. Whether the older Feature
addresses another scope of rename is not established by this inspection.

**Requested adjudication:** clarify that scope or direct its architectural
reconciliation before dependent rename/identity migration. The existing immutable
contract is preserved; no silent Feature amendment or reinterpretation is made.
The item performs no rename, reclassification or identity-policy change, so this
tension does not inherently block its unaffected decomposition/conformance work.
**Solution Architect disposition:** not recorded.

<a id="dr-4"></a>
## DR-4 — Bounded Minutiae and temporal provenance

The bounded supporting group comprises the new item's accepted-template structure
and ID/revision metadata; preserved input excerpt/source and consumer inventories;
the formal [Task Detail](../../evidence/1.1.27/001/before/ai-agent-tasks/unresolved-calliope-oo-backlog-preparation.md),
corresponding [Report](../../evidence/1.1.27/001/after/ai-agent-reports/20261116T031417Z-calliope-oo-backlog-preparation.md)
and bounded document/source/test checks. This is supporting
provenance, not retrospective acceptance of the original Junie review or code.
The recovered Phase 1 result is retained as a current input with source hashes;
its original issuance/production times are not asserted by this capture.

The current preparation Task's instruction-entry timestamp and filename prefix,
and the carrier-selection follow-up's instruction-entry time/prefix correspondence,
are **UNRESOLVED**: attributable event times were not exposed to this agent.
No current clock, session name, file metadata or commit time is substituted.
The Report uses the existing completed-emission event mechanism. The iteration
checker retains UNRESOLVED with no structural errors when only these Task-event
limits remain. Their explicit treatment is requested before closure; the earlier
1.1.26 temporal qualifications do not extend to this task by inference.

Actual [document validation](../../evidence/1.1.27/001/after/document-validation.json)
passes required sections/metadata, local references and source/scope preservation.
The [post-emission iteration check](../../evidence/1.1.27/001/after/iteration-evidence-check.json)
records actual layout/event results separately from semantic acceptance. The initial
local whitespace check misinterpreted a benign new-file diff exit code; corrected
the checker and reran, with no whitespace defect found.

No historical evidence, completed HCRL, governance contract, release authority,
implementation source or test was changed. All current artefacts remain uncommitted.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect treatment, including the disclosed temporal uncertainties]
```
