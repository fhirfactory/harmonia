<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | First bounded Calliope OO Backlog Item | Accepted | Revision 1 accepted as a Backlog Item only; no implementation authority. |
| [DR-2](#dr-2) | Existing Calliope → Themis API authority discrepancy | Deferred | Unresolved architectural observation, outside the Backlog Item's scope. |
| [DR-3](#dr-3) | Existing Topic rename/identity authority tension | Deferred | Unresolved architectural observation, outside the Backlog Item's scope. |
| [DR-4](#dr-4) | Bounded Minutiae and temporal provenance | Accepted with qualification | Preserve supporting evidence and unresolved Task/follow-up times; fabricate no historical events. |

# HCRL 1.1.27/001 — Calliope OO Backlog Item preparation

**Status: CANCELLED — NON-RELEASE.**

Mark Hunter, Solution Architect, explicitly authorised this one-off administrative
cancellation in the retained [cancellation instruction](../../evidence/1.1.27/001/before/ai-agent-tasks/unresolved-calliope-oo-backlog-preparation.md#bounded-administrative-cancellation-authorisation).
The file remains at its original `design/review/1.1.27/` path, including the
historical `.pending.md` suffix, to preserve existing provenance and links.
Its standing is cancelled, not pending or registered complete. All four recorded
dispositions above remain in force; no Architecture/software release is accepted.

**Originally initiated target:** Architecture `1.1.27`, iteration `001`.
The Solution Architect's subsequent disposition identifies this as the wrong
methodology for software Backlog management. Preserve that history; the earlier
reply identifying `1.1.27` supplies no architectural change or release acceptance.
This record is not an active Architecture release candidate or implementation
assignment. Root Architecture authority remains **1.1.26**, previous **1.1.25**.
No Architecture or software release is allocated, proposed for acceptance or tagged.

**Originating state:** commit `dc30cfbcc7ad4113b29e3710904453c023df4dc1`, branch
`Mneme-and-Mnemosyne-Build`, initially clean. HCRL 1.1.26/001 remains complete and
unchanged. No accepted software predecessor exists for this item; the first
software version/baseline remains deferred under its accepted DR-5.

**Authority and limits:** Mark Hunter authorised independent investigation and
preparation of one coherent Backlog Item, expressly excluding Junie commissioning,
implementation changes and refactoring. The subsequent supplied investigation was
used to locate Junie's original Phase 1 result, not to commission its recommended
historical recovery. The subsequent **HCRL 1.1.27/001 — Solution Architect
Disposition** from Mark Hunter accepts the Backlog Item, defers DR-2/DR-3 and
qualifies DR-4. Its [issued wording and correspondence](../../evidence/1.1.27/001/before/ai-agent-tasks/unresolved-calliope-oo-backlog-preparation.md#solution-architect-disposition-and-reconciliation-follow-up)
are retained. The [pre-disposition HCRL](../../evidence/1.1.27/001/before/solution-architect-disposition/hcrl.before.md)
and [accepted revision 1 source](../../evidence/1.1.27/001/before/solution-architect-disposition/backlog-item.accepted-revision-1.md)
preserve the actual reviewed history; no earlier acceptance is invented.

**Governance reconciliation history, before the one-off cancellation authority:**
[Architecture Governance](../../../governance/architecture-governance.md#repository-structure-and-migration-boundary)
defines the target key as a resulting Architecture/software release, and
[Review Praxis §6](../../../governance/architecture-review-praxis.md#6-confirm-and-complete-the-transition)
provides confirmed-transition completion in `design/register/`.
[Release Policy](../../../governance/architecture-release-policy.md#authority-and-scope)
reserves Architecture increments for architectural/normative changes. No such
change is accepted here. The explicit non-release “Abandoned increment” treatment
in [Software Development Praxis §7](../../../governance/software-development-praxis.md#7-review-outcomes-and-corrective-iterations)
belongs to software increments; it does not explicitly govern cancellation of an
incorrectly initiated numeric Architecture-target review. This activity is neither
an abandoned software increment nor a rejected Backlog Item.

At that reconciliation, all attributable decisions were recorded, but this HCRL
remained at its original review path without terminal cancellation. The reported
limitation was the absence of an explicit non-release cancellation route for this
incorrectly initiated Architecture-target carrier, not missing Backlog acceptance.
The [pre-cancellation record](../../evidence/1.1.27/001/before/administrative-cancellation/hcrl.before.md)
preserves that actual standing and recommendation.

**Earlier recommendation, now explicitly authorised for this HCRL only:** under the existing
[bounded-departure authority](../../../governance/architecture-governance.md#governance-enforcement-and-fail-closed-authority),
explicitly authorise cancellation of this incorrectly initiated Architecture-target
review, retaining this file in `design/review/` as closed without acceptance of
an Architecture or software release, while preserving DR-1's Backlog acceptance,
DR-2/DR-3's deferrals, DR-4's qualifications and all useful evidence. Allocate no
release and make no completed-register entry. The subsequent Solution Architect
authorisation supplies precisely this bounded cancellation. It installs no general
closure mechanism or permanent governance rule and does not retrospectively make
the original Architecture-target initiation correct.

<a id="dr-1"></a>
## DR-1 — First bounded Calliope OO Backlog Item

**Accepted Backlog Item:**
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
exact reviewed source, without duplicating its specification. This acceptance
does not commission Junie or accept existing snapshots. Any future software
submission represents the integrated solution and must include applicable unit,
solution-wide regression/integration and conformance evidence. Missing required
verification requires explicit disposition; module success and historical
architecture-only qualifications do not satisfy that boundary.

**Actual preparation verification:** current naming-model suites PASS (23 tests)
and mandatory architecture suites PASS (90 tests), zero failures/errors/skips.
These are inspection-baseline results, not solution-wide readiness. The original
document/scope validation and subsequent
[reconciliation verification](../../evidence/1.1.27/001/after/reconciliation-verification.json)
are retained with the formal Report. No implementation/test source or canonical
authority was edited. Acceptance concerns the work specification only;
implementation acceptance remains a separate future request.

**Solution Architect disposition — Accepted:** Mark Hunter, in the retained
current-interaction disposition: “BL-CALLIOPE-OO-01, revision 1, is accepted as a
bounded software-development Backlog Item. This does not authorise implementation.”
The reviewed bytes are retained above (SHA-256
`47f83182b39de96824d475a883a858427647ff59a5e9b7ccc20c9d3a5bad21f4`).
Only acceptance/provenance annotations are updated in the maintained item; its
work specification and revision remain unchanged.

<a id="dr-2"></a>
## DR-2 — Existing Calliope → Themis API authority discrepancy

The root Calliope guardrail prohibits higher-layer dependencies including Themis;
the actual POM/source uses `themis-api`. The current layering test prohibits
`themis.core` while permitting API references. No explicit exemption was located
in the bounded governance/accepted-register search. Passing that test cannot
establish an exception to the instruction. See the
[assessment's source/effect analysis](../../evidence/1.1.27/001/after/engineering-assessment.md#material-discrepancies-and-adjudication-boundaries).

**Preparation request (historical):** identify applicable existing authority for the API
exception or direct a bounded governed resolution. No POM/contract relocation or
test weakening is proposed here. This is outside the item's naming-model scope;
unaffected work need not await an unrelated resolution. Preserve the discrepancy's
actual applicability in later solution-conformance review; no release waiver is
inferred.

**Solution Architect disposition — Deferred:** Mark Hunter: “Preserve the
Calliope/Themis API dependency discrepancy as an unresolved architectural
observation, outside this Backlog Item's scope.” No further investigation or
resolution is commissioned by this disposition.

<a id="dr-3"></a>
## DR-3 — Existing Topic rename/identity authority tension

The accepted [TopicIdentityStable Feature, E04.01.04.F01](../../../continuum/strategy/capability/feature/9210349f026b4ff1aca1092c309b4139.md)
says identities survive rename/reclassification. The accepted common Topic contract
and [1.1.25/001 DR-3](../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-3)
require immutable definitions and a different UUID for substantive replacement,
while preserving existing specialised Topic identities. Whether the older Feature
addresses another scope of rename is not established by this inspection.

**Preparation request (historical):** clarify that scope or direct its architectural
reconciliation before dependent rename/identity migration. The existing immutable
contract is preserved; no silent Feature amendment or reinterpretation is made.
The item performs no rename, reclassification or identity-policy change, so this
tension does not inherently block its unaffected decomposition/conformance work.
**Solution Architect disposition — Deferred:** Mark Hunter: “Preserve the Topic
rename/identity question as an unresolved architectural observation, outside this
Backlog Item's scope.” No further investigation or resolution is commissioned.

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
limits remain. Their explicit treatment is now supplied by this HCRL's DR-4
disposition, not by extending the earlier 1.1.26 qualifications by inference.
The original events and filename correspondence remain UNRESOLVED; qualification
does not make the checker pass or establish missing events.

Actual [document validation](../../evidence/1.1.27/001/after/document-validation.json)
passes required sections/metadata, local references and source/scope preservation.
The [post-emission iteration check](../../evidence/1.1.27/001/after/iteration-evidence-check.json)
records actual layout/event results separately from semantic acceptance. The initial
local whitespace check misinterpreted a benign new-file diff exit code; corrected
the checker and reran, with no whitespace defect found.

The earlier reconciliation changed no completed HCRL, governance contract, release
authority, implementation source or test. Original evidence content is preserved;
Task/Report correspondence is appended. The concurrent `AGENTS.md` and
`docs/AGENTS.md` additions were originally excluded. The subsequent cancellation
instruction expressly authorises their inclusion in the bounded commit/push,
subject to exact-content/scope verification. Their bytes are preserved, not edited
by this activity. Cancellation authority comes from the specific Solution Architect
instruction, not from those additions or from the Minutiae control.

**Solution Architect disposition — Accepted with qualification:** Mark Hunter:
“Preserve the supporting evidence and unresolved Task/follow-up timestamps without
fabricating historical events.” Recording this disposition and updating item
standing is provenance maintenance in this existing boundary; no new Task,
Report or HCRL iteration is instantiated. The original Report production event
and initial evidence remain intact; a dated amendment records this reconciliation.

```text
Minutiae-Accepted: Yes
Comment: Mark Hunter's current-interaction DR-4 acceptance with qualification; retain supporting evidence and unresolved Task/follow-up timestamps without fabricating events. This accepts the disclosed group only and does not authorise release acceptance or extend cancellation rules.
```

### One-off administrative cancellation and repository transition

The incorrectly initiated Architecture-target review is **CANCELLED — NON-RELEASE**
under Mark Hunter's explicit bounded authorisation. DR-1's accepted Backlog Item
and revision 1 specification remain intact; DR-2/DR-3 remain deferred architectural
observations outside its scope; DR-4 remains accepted with qualification, with
unresolved temporal events/prefixes preserved. No new architectural investigation,
implementation assignment, HCRL, release allocation, Continuum change, completed
register entry, release tag or general cancellation rule is introduced.

The separately prepared instruction additions were inspected: they append only
Backlog preparation operating guidance, preserve existing content, require the
accepted template/independent judgement, and retain separate implementation/Git
authority and binding-governance safeguards. They do not supply a blanket override
of mandatory governance or a general administrative cancellation rule. Their exact
hashes and the bounded candidate/preservation checks are retained in
[cancellation verification](../../evidence/1.1.27/001/after/administrative-cancellation/verification.json).

The same instruction authorises commit and push of the accepted Backlog Item,
cancelled review and associated evidence together with those two verified
instruction additions once bounded checks pass, excluding unrelated changes.
Actual commit/push correspondence is verified against the resulting Git state;
the record need not contain its own final commit hash. Architecture authority
remains **1.1.26** with predecessor **1.1.25**; no software version is allocated.
