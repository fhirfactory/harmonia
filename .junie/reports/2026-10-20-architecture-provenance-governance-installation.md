<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture provenance governance installation — 2026-10-20

## Result and authorisation

Installed the approved [Architecture Change Provenance contract](../../docs/markdown/governance/architecture-change-provenance.md)
as governed, representation-neutral RADS governance. The Solution Architect's
2026-10-20 installation instruction accepts the candidate in the
[design report](2026-10-20-architecture-provenance-and-evidence-design.md), with
the explicit amendments below, and authorises coordinated reconciliation of
maintained governance. That historical report remains unchanged; it is not a
second normative definition. This installation report records execution and
verification, not a separate provenance policy or transition evidence package.

The installed contract records the authorised incremental change between
governed architecture states, rather than machinery execution history. Its
lifecycle ends with verified transition evidence and a processing workspace
eligible for separately authorised disposal, subject to resolved dependencies
and retained unique architectural content. Installing governance itself creates
no such eligibility.

The normal release policy applies: root [architecture-release.json](../../architecture-release.json)
now records **1.1.1**, predecessor **1.1.0**, for this governance change set.
The bounded RAD-1.0.10 proving exception was not reused. No exchange publication
was generated for this governance-only increment. The existing RAD-1.1.0 source
and publication remain unchanged.

## Solution Architect amendments incorporated

| Adjudication | Installed treatment |
| --- | --- |
| Whole-file preservation is authoritative | Each applicable archive preserves the complete changed repository file's exact bytes. Extracted XML records, Markdown sections and other fragments cannot substitute. The review explains the narrower architectural meaning and authorisation. No alternative partial preservation unit remains in the candidate wording. |
| Review is not a machine manifest | Changed-content inventory, paths, operations, correspondence and existence states remain mandatory. Per-file/member hashes are optional where materially useful; verification wording no longer mandates their enumeration. Final before/after archive SHA-256 hashes are recommended with SHOULD, not made mandatory. The template follows the same distinction. |
| Historical reconstruction is demand-driven | Adoption creates no obligation to package every past RAD release. RAD-1.0.10 → RAD-1.1.0 is the intended first separately authorised migration/pilot. Earlier reconstruction is limited to retiring processing dependencies, preserving otherwise-lost architectural evidence, resolving provenance requirements or explicit Solution Architect direction. |
| A run is not inherently a transition | Failed, diagnostic, superseded and no-change attempts create no architecture-transition package merely by existing. Acceptance, processing outcomes and publication remain distinct. |
| Recovery and durable evidence have different purposes | Processing records remain while required for recovery, validation, unresolved decisions or migration dependencies. Final evidence follows the one governed contract; installation neither deletes records nor makes them disposable. |
| Current operation must become independent of historical processing | The contract establishes the required end state and requires separately authorised dependency removal before affected material can become disposable. It does not claim present framework compliance or authorise implementation work here. |

## Files installed or changed

| File | Change |
| --- | --- |
| [architecture-change-provenance.md](../../docs/markdown/governance/architecture-change-provenance.md) | New normative contract, approved standing, amendments and explicit adoption/reconciliation decision. |
| [Root AGENTS.md](../../AGENTS.md) | All-architecture discovery in section 1.4, both ArchiMate trigger paths, and explicit Junie discovery in section 6. |
| [docs/AGENTS.md](../../docs/AGENTS.md) | Navigation to the same contract under inherited root instructions. |
| [Architecture Authoring Praxis](../../docs/markdown/governance/architecture-authoring-praxis.md) | Contract reference; scoped submission retention; working recovery versus final evidence. |
| [ArchiMate Round-Trip Engineering Praxis](../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md) | Contract reference; recovery/reproduction distinction; historical proving-cycle scope; eventual processing independence. |
| [Change Review contract](../../docs/markdown/governance/archimate-ingestion-change-review.md) | Explicit reconciliation of complete processing-history retention; material decision/conclusion closure; active run-layout scope. |
| [Change Review template](../../docs/markdown/governance/templates/archimate-ingestion-change-review-template.md) | State bindings, authorisation/revisions, changed-content/existence inventory, optional member hashes, recommended archive hashes, reconstruction provenance and closure. |
| [Architecture Release Policy](../../docs/markdown/governance/architecture-release-policy.md) | Explicit retention adjudication; historical exception boundaries; governance-only release/publication treatment. |
| [Publication semantics](../../docs/markdown/governance/archimate-ingestion-publication-semantics.md) | Evidence-standing reference while preserving accepted identity, semantics, authored Views and input integrity. |
| [Governance index](../../docs/markdown/governance/README.md) | Normative contract discovery and corrected processing-record description. |
| [ArchiMate workspace README](../../docs/archimate/README.md) | Provenance discovery and submission-retention reconciliation. |
| [Framework README](../../docs/archimate/framework/README.md) | Provenance discovery, existing processing-dependency limits and scoped snapshot/correction supersession. |
| [Processing README](../../docs/archimate/processing/README.md) | Working/historical material distinguished from durable architectural evidence. |
| [Ingestion processing README](../../docs/archimate/processing/ingestion/README.md) | Active run identity and recovery distinguished from final review/evidence closure. |
| [Incoming exchange README](../../docs/archimate/exchange/ingest/README.md) | Same contract linked for landing files and preserved analysis inputs. |
| [Published exchange README](../../docs/archimate/exchange/publish/README.md) | Release representation distinguished from processing records and transition evidence. |
| [architecture-release.json](../../architecture-release.json) | Required patch increment to 1.1.1, predecessor 1.1.0. |
| This report | Installation, reconciliation, discovery, checks and scope record. |

## Previous conflicting requirements and explicit reconciliation

| Maintained requirement or ambiguity | Reconciliation |
| --- | --- |
| Release policy: keep prior machinery-validation runs intact and preserve before snapshots, machinery/governance fingerprints and replay transitions without an end condition | The dated adoption decision explicitly supersedes the permanent-retention interpretation. The operative run paragraph now preserves these while recovery, validation or unresolved dependencies require them. Historical pins and claims remain unchanged; replay is not permanent architectural evidence or a prerequisite for current operation. |
| Authoring Praxis: retain immutable submission/run evidence under the old 2A rules and preserve the submitted model as evidence/history | Submission provenance and immutable inputs remain required during active processing. Durable closure follows the new contract; Submission imposes no permanent export/run retention. Serial authoring and iteration closure are unchanged. |
| Round-Trip Praxis: preserve before evidence, pin machinery/configuration and provide reproduction | Secure originals and preserve recovery safeguards while needed. No permanent machinery snapshot or complete historical reproduction requirement remains. Validation conclusions and limitations belong in the reviewed transition record. |
| Change Review: retain commands/results, unsuccessful attempts, prior evidence and review/instruction/Outcome revisions in the same run | Detailed records serve active work. Final review preserves material facts, attributable decisions/revisions, directives and validation conclusions without permanent companion histories. Issued Dispositions, Outcomes, Outcome Reasons and processing results remain independent. |
| Change Review run placement/template: run directories and links could define permanent evidence | Run layout is explicitly operational. The template supports durable state bindings, changed-content inventory and authorisation/closure without depending on run-relative links. It does not prescribe a new evidence directory layout. |
| Publication semantics: retained submission/identity/View evidence could imply preservation of the entire processing tree | New reference assigns evidence standing to the provenance contract; governed identity, semantic content and authored presentation remain preserved. Full processing trees and execution histories gain no permanent standing through these semantic obligations. |
| Workspace/framework/ingestion/publish README descriptions: source snapshots, processing histories and run evidence treated as the continuing evidence store | Current guidance references the normative contract. Reconciled original passages remain unchanged inside section-scoped supersession wrappers with specific RADS replacements. No entire legacy document was marked superseded. |
| Existing release text describes older root values and a bounded proving-cycle exception | Its historical scope is explicit. It neither overrides the current root release nor excuses this accepted governance installation's patch increment. No historical decision was converted into new architecture-processing authority. |

This is an express governance adjudication authorised by the Solution Architect,
not a tooling workaround or a hash refresh. Maintained RADS requirements were
amended directly; their historical frozen copies were not edited. All original
lines in the six maintained ArchiMate READMEs were preserved.

## Architectural consistency

| Axiom | Application |
| --- | --- |
| AX-04 | Governed meaning determines provenance; Git, tar and processors implement it. |
| AX-08 | Evidence preserves the authorised architectural change and decision rather than the machinery history. |
| AX-09 | Processing material serves a bounded operational purpose, without automatic promotion to durable evidence or automatic deletion. |
| AX-14 | Governed states, submissions, decisions, processing results, releases and publications remain distinct. Whole-file byte preservation and architectural meaning have separate roles. |
| AX-15 | Missing original bytes or uncertain effects remain explicit; no inferred success or regeneration substitutes for actual history. |
| AX-17 | Solution Architect acceptance authorises this installation and explicit retention reconciliation. Missing historical authority is not invented; held matters remain held. |

These applications use the [repository axiom register](../../docs/markdown/governance/architectural-axioms.md).
No runtime Kleio service, module boundary, identity allocator, Class or Package
architecture is introduced.

## Agent and Praxis discovery after installation

1. All architecture work, including Markdown-only work: root AGENTS.md section
   1.4 directly requires Architecture Change Provenance.
2. Documentation work: docs/AGENTS.md inherits the root rules and links directly
   to the same normative document; the governance index lists its standing.
3. Junie architecture planning, reporting and execution: root section 6 directly
   requires the same contract. Plans and reports supply no alternative definition.
4. ArchiMate triggers: root instructions lead to the Round-Trip Praxis and
   provenance contract. Both Praxes, the Change Review contract/template and
   publication semantics link to that one contract.
5. Practical workspace entry: the six maintained README entry points link to it
   and explain the working/historical role of retained processing material.

**Codex and Junie reach the same normative contract.** No separate Junie policy
file or Codex-specific provenance definition was created. Discovery was checked
statically through actual repository links; no separate agent session or current
ArchiMate submission was processed as a discovery test.

## Verification performed

| Check | Result and limits |
| --- | --- |
| Required ArchUnit suite | **PASS** — bounded command below; 90 tests, zero failures/errors/skips, all 36 selected reactor modules successful. Maven elapsed 17.578 seconds; finished 2026-10-20 13:53:03 Australia/Sydney. This checks repository architectural invariants, not provenance machinery or processing independence. |
| Release helper checks | **PASS** — `python3 -B scripts/architecture_release.py check`; 1.1.1 with predecessor 1.1.0. Existing `test_architecture_release.py` suite: 3 tests passed in 0.005 seconds under a 30-second bound. No new tests or framework code were added. |
| Documentation/discovery | **PASS** — introduced local file links and anchors resolve; all maintained Markdown entry points changed here directly reach the same normative document. Code fences close; introduced text has no trailing whitespace; `git diff --check` passes. |
| Legacy preservation | **PASS** — all original lines in the six ArchiMate READMEs remain; only insertions were made. Supersession start/finish boundaries are paired and replacement references resolve. |
| Protected bytes | **PASS** — 1,864 protected file identities match pre-edit SHA-256 values, excluding four expressly authorised README amendments in protected directories. This covers tracked processing/history, exchange content, baseline source, non-Markdown framework/configuration, .gitignore, the existing design/audit reports and pre-existing untracked 1.1.0 incoming files. |
| Scope and historical integrity | **PASS** — changed/new authored paths are limited to the 18 files above; no tracked file was removed or renamed. No new evidence archive or unexpected untracked file was created. Frozen RAD-1.1.0 XML SHA-256 remains `4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1`. |

```bash
timeout --signal=TERM --kill-after=10s 180s mvn test \
  -pl paradeigma/paradeigma-test -am \
  -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

timeout --signal=TERM --kill-after=5s 30s python3 -B -m unittest discover \
  -s scripts/tests -p test_architecture_release.py -v

python3 -B scripts/architecture_release.py check
git diff --check
```

Temporary documentation/scope/hash checks and Maven output were held in `/tmp`,
outside repository evidence. No architecture generator, Makefile publication,
Python framework suite, historical replay, Archi invocation or machinery-validation
run was executed. Entry HEAD remains `8f3244b1af1481885b578f3749b35ebf0ffdc6a6`;
no commit, remote update or publication was performed by this task.

## Remaining uncertainty and stop boundary

No unresolved governance conflict was identified within the maintained scope
reviewed and reconciled here. Current machinery still has historical processing
and frozen authority-byte dependencies identified by the design. Updating
governance does not remove those dependencies or justify rewriting their pins;
current-operation independence is not established by these checks. Resolving
that implementation gap requires separately authorised work. Any further
retention obligation or unknown unique content continues to block disposal.

No evidence migration, historical reconstruction, framework refactoring,
processing movement/deletion or cleanup occurred. Run-001 and Run-002 remain
unchanged. No RAD-1.1.0 architectural content or current Solution Architect
ArchiMate work was processed or changed. Frozen historical records, CR-155–158
and .gitignore remain unchanged. No Class/Package architecture or deferred
documentation-quality sweep was undertaken.

**No historical transition evidence package was created by this increment.**
Neither before.tar.gz nor after.tar.gz was created, including for this
governance-only installation increment.

**No existing processing material has become disposable merely as a consequence
of installing the governance contract.**

Work stops after governance installation and verification.
