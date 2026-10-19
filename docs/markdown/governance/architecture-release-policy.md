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

### Explicit RAD-1.0.9 reconciliation decision — 2026-10-19

The Solution Architect explicitly establishes the authorised 1.0.8 reconciliation
as **RAD-1.0.9**, with architecture baseline **1.0.8** as its predecessor. The
intervening governance/tooling sequence, recorded through 1.0.14, remains preserved
historical evidence and does not determine this RAD version. For this accepted
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
change. Generated evidence must identify the release used, check all relevant
identity/reference/schema gates and retain unresolved findings. No release
increment creates architectural identity or supplies missing architecture.

## Review and publication gate

The [RAD → ArchiMate mapping](rad-archimate-model-mapping.md) and
[incremental plan](rad-archimate-publication-plan.md) govern publication slices.
Each slice preserves existing identities, validates its references and the
Archi 5.9 bundled XSDs, runs required bounded checks and records change/coverage
evidence. Archi visual inspection precedes the next architecture slice.
AX-14 and AX-17 apply: unresolved identities, conflicting assertions and
deferred decisions remain findings until separately adjudicated.
