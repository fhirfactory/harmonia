<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Harmonia RAD / Architecture release governance

## Authority and scope

The repository-root [architecture-release.json](../../../architecture-release.json)
is the single machine-readable authority for the current Harmonia RAD / Architecture
release (`version`). Its `previous_version` records the immediately preceding
accepted release. Generated reports read this authority; copied report values
are evidence of a publication, not independent release registers.

The architecture owner established the prior baseline as 1.0.0 and authorised
the mapping, publication and release-governance review bundle as Release 1.0.1.
This document establishes the mechanism in that same bundle. Preparation and
validation do not themselves claim that a commit or Archi visual review occurred.

Every accepted repository change SHALL increment the RAD / Architecture patch
release, regardless of author or tool. One accepted change set receives one
increment, including its governance documents, tests and generated artefacts.
Work-in-progress edits and repeated generation within that set do not each
receive another increment. The increment and all associated changes SHALL be
committed together. Concurrent change sets SHALL recheck the accepted current
release before acceptance and rebase their increment if necessary.

Minor or major increments require an explicit architectural/governance decision;
tools SHALL NOT infer them. Maven's `1.0.0-SNAPSHOT` software version, historical
branch names, baseline Exchange filenames and R1/R2 architectural scope are
independent and SHALL NOT determine or change this release value.

## Mechanism and repository convention

### AI-agent execution provenance and common Review Praxis — RAD-1.1.3, 2026-10-21

The Solution Architect's [reviewed HCRL 1.1.3 iteration 001](../../../enterprise-architecture/design/review/1.1.3/hcrl-1.1.3-001.pending.md)
and subsequent Apply instruction establish the common
[Architecture Review Praxis](../../../enterprise-architecture/governance/architecture-review-praxis.md)
and required formal AI-agent Task/Report execution provenance under
[Architecture Change Provenance](../../../enterprise-architecture/governance/architecture-change-provenance.md).
Task Details directing execution of an identifiable adjudicated/authorised change
and corresponding execution Reports SHALL be retained irrespective of materiality.
The Solution Architect's [reviewed iteration 003](../../../enterprise-architecture/design/review/1.1.3/hcrl-1.1.3-003.pending.md),
DR-6/DR-7, clarifies that investigation, analysis, candidate preparation and
pending-HCRL preparation/revision are Architecture Review provenance within the
HCRL lifecycle, without a new formal review Task/Report convention. Execution
records require authoritative issuance/production timestamps and filenames
beginning with filesystem-safe sortable UTC timestamps derived from those events,
under the common provenance contract. RAD-1.1.3 is the first
prospective use of `before/ai-agent-tasks/` and `after/ai-agent-reports/` beneath
the resulting release's evidence directory.

This bounded governance-only change advances **1.1.2 → 1.1.3**, using the
existing expected-current patch mechanism once. Application facts and validation
are preserved in [pending iteration 002](../../../enterprise-architecture/design/review/1.1.3/hcrl-1.1.3-002.pending.md);
[iteration 004](../../../enterprise-architecture/design/review/1.1.3/hcrl-1.1.3-004.pending.md)
records corrective application and then-unresolved original event times/prefixes.
Its updated Solution Architect table DR-9 expressly clarifies DR-7: attributable
system timestamps of operative instruction entry into the agent interaction and
completed Report emission constitute the governed issuance/production events;
subsequent amendments preserve original Report production time.
[Application iteration 005](../../../enterprise-architecture/design/review/1.1.3/hcrl-1.1.3-005.pending.md)
records recovery of the three times and the required filename mappings.
Mark Hunter, Solution Architect, confirmed the incorporated result on 2026-10-21:
“result confirmed. Complete!” The [completed HCRL iteration 005](../../../enterprise-architecture/design/register/1.1.3/hcrl-1.1.3-005.complete.md)
is the authoritative semantic record for this accepted transition; the increment
does not itself supply acceptance. Completion within this same
change set consumes no additional release. No model/publication regeneration,
repository migration, retrospective reconstruction, historical cleanup, erratum
machinery, production code or other machinery change is authorised. The existing
patch mechanism and historical decisions below remain unchanged.

### Common Architecture Governance adoption — RAD-1.1.2, 2026-10-21

The Solution Architect's reviewed
[HCRL 1.1.2 iteration 002](../../../enterprise-architecture/design/review/1.1.2/hcrl-1.1.2-002.pending.md)
and instruction to apply adopt the common
[Architecture Governance](../../../enterprise-architecture/governance/architecture-governance.md)
and [Architecture Change Provenance](../../../enterprise-architecture/governance/architecture-change-provenance.md)
contracts. HCRL is the single semantic Decision Review Log for the immediate
governed release transition, including transitions with only reviewed minutiae.
Specialised reviews retain their specialised purpose and feed HCRL. The mandatory
three-file evidence package is superseded; no universal archive, hash or duplicate
exact-byte requirement replaces it.

This governance-only change set advances **1.1.1 → 1.1.2**, without regenerating
the unchanged architecture/model publication. The normal accepted-change patch
rule remains in force; pending HCRL iterations consume no releases. Application
facts and final Solution Architect confirmation are recorded in the
[completed iteration 003](../../../enterprise-architecture/design/register/1.1.2/hcrl-1.1.2-003.complete.md).
Mark Hunter confirmed the result on 2026-10-21: “result confirmed - complete”.
Completion belongs to this same accepted change set and consumes no further
release increment.

The accepted target structure does not authorise repository migration. Erratum
mechanics are deferred until an actual need. HCRL adoption is prospective; the
historical 1.0.10 → 1.1.0 pilot is excluded and SHALL NOT be investigated,
completed, cancelled or otherwise reconciled unless a future specific
architectural need requires it. Historical decisions below remain preserved;
they do not instruct work in this transition or override the current root value.

### Architecture-change provenance — 2026-10-20

The Solution Architect adopts [Architecture Change Provenance](architecture-change-provenance.md)
as the single contract for durable architectural evidence. This explicitly
supersedes the permanent retention interpretation of machinery runs, frozen
machinery/governance snapshots and complete processing evidence in this policy
and the associated Praxes. Preserve processing material while necessary for
active recovery, validation, unresolved decisions or migration dependencies;
closure and any later disposal follow that contract. Immutable input pins and
frozen historical claims must not be rewritten to assert retrospective compliance.

The machinery-validation and earlier reconciliation decisions below record
bounded historical release decisions. They do not freeze the current root
authority or exempt this governance installation from the normal patch rule.
This change set advances RAD-1.1.0 to RAD-1.1.1 without regenerating or changing
the RAD-1.1.0 architecture/publication. No historical evidence package is created.
Current-operation independence from historical processing remains a required
end state for separately authorised work; no existing carrier becomes disposable
through this installation.

### Machinery-validation runs — 2026-10-20

The Solution Architect explicitly establishes RAD-1.0.10 as the frozen baseline
for Information Architecture machinery validation. Its published model and
architect-authored submission are immutable regression specimens:

```text
docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml
docs/archimate/exchange/ingest/1.0.10/harmonia-architecture-1.0.10.xml
```

`run-001`, `run-002`, … are processing attempts against these same hash-pinned
inputs. They are not RAD releases. Naming governance, machinery, tests, proposed
models and evidence developed in this cycle SHALL NOT increment the RAD patch
number or create RAD-1.1.0. This explicit bounded decision takes precedence over
the general accepted-change patch rule for this cycle, including its supporting
governance changes. Root `architecture-release.json` remains `1.0.10` with
predecessor `1.0.9`; runs introduce no second release allocator.

```text
RAD-1.0.10 + immutable architect submission
    → run-001 → review / machinery improvement → run-002 → …
    → successful automation and real Archi round-trip proof
    → Solution Architect acceptance → RAD-1.1.0
```

Allocate RAD-1.1.0 only at the explicitly accepted architectural transition after
those gates; do not create its directories, release authority or publication now.
Submitted semantics and regenerated metadata remain proposed until acceptance.
No run performs general IA reconciliation or rewrites its input to pass tooling.

For active processing, retain runs under
`docs/archimate/processing/machinery-validation/information-architecture/run-NNN/`,
outside publication-release directories. Each active run retains input paths and byte/XML
hashes, release authority, machinery/governance fingerprints, proposed metadata,
candidate model, identity/rebinding evidence, unresolved findings and bounded
verification results needed for its recovery/validation. Preserve prior runs
while those purposes or unresolved dependencies remain; a new attempt uses a new
directory. Read-only historical replay is a separate diagnostic activity, not
architectural-transition evidence or a permanent current-operation prerequisite.
Before snapshots and explicit governance hash transitions serve recovery and
remaining dependencies under Architecture Change Provenance; never rewrite old
pins or baseline source captures. This replaces the former unqualified "keep
prior runs intact" and machinery-snapshot preservation requirements, not the
requirement to preserve needed content until verified, authorised closure.

### Explicit RAD-1.0.9 reconciliation decision — 2026-10-19

The Solution Architect explicitly establishes the authorised 1.0.8 reconciliation
as **RAD-1.0.9**, with architecture baseline **1.0.8** as its predecessor. The
intervening governance/tooling sequence, recorded through 1.0.14, remains a
preserved historical record, subject to the provenance/recovery distinction
above, and does not determine this RAD version. For this accepted
change set the root authority records `version=1.0.9`, `previous_version=1.0.8`.
Publication reads that authority; it must not derive the RAD number from the
governance/tooling evidence sequence. The normal patch mechanism continues from
the explicitly established RAD authority. No second release allocator is introduced.

The Solution Architect's Home-derived naming/navigation decision completes this
currently unsigned RAD-1.0.9 candidate within the same accepted change set.
Regeneration and validation retain `version=1.0.9`, `previous_version=1.0.8`;
they SHALL NOT advance this completion to RAD-1.0.10. Final sign-off remains pending.

**Solution Architect decision — IA canonical-name completion, 2026-10-19:**
RAD-1.0.10 remains the current unsigned candidate (`previous_version=1.0.9`).
Authorising `BO` / `TOPIC` and completing the 54 IA canonicalName properties
resolves the naming requirement explicitly left open during that candidate's
creation. It belongs to the same change set and SHALL NOT increment the release.
The normal patch policy otherwise remains unchanged.

A small root JSON file follows existing repository JSON conventions without
coupling architectural governance to Maven. The standard-library Python helper
under [scripts/](../../../scripts/architecture_release.py) follows the repository's
existing validation-tool convention. No build dependency or second versioning
scheme is introduced. The root value is discoverable without choosing a domain
or treating generated publication metadata as authority.

From the repository root:

```bash
python3 scripts/architecture_release.py show
python3 scripts/architecture_release.py check
python3 scripts/architecture_release.py bump --expect CURRENT
```

`bump` is an explicit operation for the next accepted change set. It checks
`CURRENT` against the authority, increments only the patch, and records the old
value in `previous_version`. A stale expected value fails without changing the
file. Serialise acceptance operations; the helper is not a concurrent release
allocator. An explicitly authorised major/minor change must update the same
file with its preceding value and record the decision in governed documentation.

Publication and assessment tools load the authority through the shared helper.
They accept `--release-file` for an explicit portable release fixture; they
never invoke `bump`. Validate, regenerate and compare reports after a release
change when publication is in scope; a governance-only increment need not
regenerate frozen architecture publications. Validation conclusions must identify
the release used, check all applicable identity/reference/schema gates and retain
unresolved findings. Durable evidence follows Architecture Change Provenance;
complete execution history is not required. No release
increment creates architectural identity or supplies missing architecture.

## Review and publication gate

The [RAD → ArchiMate mapping](rad-archimate-model-mapping.md) and
[incremental plan](rad-archimate-publication-plan.md) govern publication slices.
Each slice preserves existing identities, validates its references and the
Archi 5.9 bundled XSDs, runs required bounded checks and records change/coverage
evidence. Archi visual inspection precedes the next architecture slice.
AX-14 and AX-17 apply: unresolved identities, conflicting assertions and
deferred decisions remain findings until separately adjudicated.
