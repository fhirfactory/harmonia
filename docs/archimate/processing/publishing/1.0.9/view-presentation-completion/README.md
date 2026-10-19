<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# RAD-1.0.9 Generated View Presentation completion

The authorised generated presentation corrections are implemented in the unsigned
RAD-1.0.9 candidate, predecessor 1.0.8. Automated conformance and actual Archi 5.10
transport validation are complete. **Solution Architect import/rendering inspection
and final sign-off remain pending.**

The earlier documentation-only interpretation was incorrect. The amendment to
`home-context-completion/README.md` has been removed, restoring that record to its
pre-amendment content. That completed task's exclusion of presentation remains
correct at its own boundary. This separate record covers the subsequent authorised
presentation work; it does not reinterpret the earlier verification as evidence
for this task.

## Authority and bounded implementation

The Solution Architect's task instruction authorises migration of the verified
119 generated non-historical 1–20-child direct-Composition Views and recalculation
across all 61 generated Business Behaviour Views. The existing
[Containment archetype](../../../../../markdown/governance/archimate-ingestion-publication-semantics.md#deterministic-containment-view)
governs selection, orientation, columns and clear routing. Existing
[Business Feature columns](../../../../../markdown/governance/archi-navigation-and-view-convention.md#final-human-inspection-projection-correction--same-106-candidate)
remain in place. This work implements those presentation rules without a new
architectural decision. The same unsigned change set retains the release under
the [release policy](../../../../../markdown/governance/architecture-release-policy.md#explicit-rad-109-reconciliation-decision--2026-10-19).

AX-04 preserves semantic authority independently of diagram machinery. AX-14
keeps Composition, Home, responsibility, naming, navigation and presentation
distinct. AX-17 preserves historical assertions and unresolved cases. No semantic
element, relationship, identity, naming field, Home or responsibility assertion,
standing, membership or navigation path changes.

The [pinned selection](selection.json) identifies every selected source, purpose,
standing filter, child and native Composition relationship, the 61 Behaviour
Views and their connections, the 29 known stale connections, and the exclusions.
The current-context filter uses existing governed `parent_key` and currentness;
retained assertions from earlier hierarchies do not add children to that declared
View purpose. Membership is independently checked against native direct
Composition and the governed current parent context. No child is dropped to
avoid a count above 20.

`view_presentation.py` runs after accepted ingestion and Home-context publication.
It moves only selected Containment node coordinates and recalculates connection
bendpoints. Semantic-ID ordering is deterministic. Node sizes, styles, labels,
View metadata, visual identities and semantic endpoints are preserved. The
existing publisher performs schema, reference, metadata and exact-result checks
on the final serialized candidate. Historical replay is explicitly available as
`synchronise_baseline.publish(presentation=False)`; the earlier Home/reconciliation
tests continue to enforce their original boundaries.

## Executed population and changes

| Population | Validated | Changed | Preserved |
| --- | ---: | ---: | ---: |
| Generated Containment Views, 1–20 children | 119 | 119 | Same membership, identities, sizes and styles |
| Containment connections | 357 | 357 routes | Same native Composition and visual endpoints |
| Generated Business Behaviour Views | 61 | 19 | 42 byte-identical Views |
| Business Behaviour connections | 136 | 29 routes | 107 byte-identical connection XML records |
| Specifically identified stale retained connections | 29 | 29 routes | Separately identified in the change ledger |
| Protected architect-authored presentations | 4 | 0 | Exact XML |
| Historical candidates with 1–20 children | 62 | 0 | Exact XML |
| Views with more than 20 children | 2 | 0 | Both have 21 children; exact XML |
| Entire model | 1,198 Views | 138 Views | 1,060 byte-identical Views |

The Containment population has 116 one-column Views and three two-column Views
with 13, 14 and 16 children. The source is on the left, with the native Composition
diamond at the whole/source side. Second-column nodes occupy vertical gaps between
first-column nodes. Corridors and routes use actual rectangle dimensions with
clearance. Tests exercise every count from 1 through 20, variable and odd-sized
rectangles, overlapping/obstructed geometry rejection and explicit >20 rejection.

All 61 Business Views are processed from their final Feature-column node geometry.
Straight routes are retained when clear. Required doglegs use deterministic lanes
derived from actual rectangle boundaries. The recalculated route is compared with
the existing ordered bendpoints before any XML mutation. Reapplication to the final
candidate produces no XML differences. Inclusion in validation alone changes no XML.

[population-and-changes.json](population-and-changes.json) records every changed
View and connection, including before/after bendpoints and the separate known-stale
flag. Selection and change counts describe distinct populations.

The uncertain diagnostic remains unchanged: `Archi Import Test - Individual Health
Assessment`, View `id-ef8af9b152505720b37914080c957a1c`. All unselected View XML,
including unrelated presentation debt, is byte-identical. No historical migration,
>20-child presentation strategy, Information/Application Architecture expansion or
other presentation correction is included.

## Preservation and actual validation

[before.json](before.json) pins 530 existing files. Fifty-two files have
byte-recoverable gzip snapshots under `before/`; 478 other protected files remain
in place with their original hashes. The starting candidate SHA-256 is
`efe3b986ae3e031d35bf38e7af47bb733af090dab63e46726f21b4e0a557cfec`.
The current model SHA-256 is
`b9c63aef051f5424b937c7b01951cabd38389c4352610a8331a0d40e449dbf24`.

The independent delta audit removes no semantic differences: all 1,308 elements,
1,077 native relationships, Properties, qualified names, 212 explicit Homes,
responsibility assertions and organisations/navigation compare exactly. It
allows only selected node x/y coordinates and changed ordered bendpoints.
Unselected Views and unchanged connections are also compared as XML bytes.
Original ingestion inputs, CR dispositions, identities, source captures, Home
assessments, previous publication evidence and the original Home README remain
preserved. The prior publisher reproduces the pinned pre-presentation candidate.

[verification.json](verification.json) binds the actual commands, limits, exit
codes, elapsed times and log hashes to this candidate. Final checks include:

| Check | Result |
| --- | --- |
| Full ArchiMate/framework suite | 198 passed |
| Final focused presentation suite, including odd-size attachment and current Home-audit scope | 9 passed |
| Governance suite | 72 passed |
| Required Java architecture suite, JDK 21/offline Maven | 90 passed |
| Publication regeneration, XSD, semantic/visual references and governed metadata | PASS |
| Resource identity, identity registry, RADS and unchanged release authority | PASS |
| Independent semantic delta, populations, protected files and XML preservation | PASS |
| Serialization-only round trip, unmodified comparator | EQUIVALENT |
| Actual installed Archi 5.10 no-edit import/export | PASS within recorded scope |
| Historical reconciliation/governance evidence, both prior Archi probes and Home population audit | PASS against their original candidates |

The final focused run covers the additional odd-size boundary regression added
after full-suite discovery; full-suite and focused counts are reported separately.
The [final output reproduction record](reproduction.json) verifies all 15 current
generated outputs in independent processes, including the current report and
inspection guide. Generation does not allocate identities or increment release.

The [actual transport evidence](roundtrip/archi-transport-verification.json) uses
the installed Archi editor bundle `5.10.0.202608252016`, an isolated temporary
configuration/workspace/keyring, English `en`, folder export and enabled schema
validation. The [executed CLI record](validation/archi-cli.json), native model,
no-edit export, toolchain manifest and full raw comparison are retained alongside
the transport proof. All 3,923 nodes and 1,473 connections preserve their geometry
and references, including every selected and protected View.

The raw whole-document comparator remains **DIFFERENCES**. Its 1,669 historical
semantic-text findings match the established 1.0.7 transport evidence exactly.
Raw organisation/default-style and other transport findings remain visible;
no comparator equivalence or normalisation rule was added. Whole-model lossless
fidelity and human rendering acceptance are not asserted.

The first new transport-verification harness attempt removed its temporary export
before the existing comparator read file-size metrics. That failed attempt is
preserved as `validation/archi-transport.log` and its result JSON. The harness was
corrected to keep the export alive for comparison; the recorded successful Archi
run and export were reused. No production architecture changed to address the
harness error. No command timed out or stalled.

Report preparation also caught two tooling integration errors before handoff:
the selection initially read the historical identity-establishment release helper
instead of the root current-release authority, and the new report compared an
in-memory validation record without the publisher CLI's existing successful
determinism marker. The selection now reads `architecture-release.json` directly,
and the report validates the exact CLI record including that marker. The earlier
selection and unsuccessful report commands remain under `validation/`.
The final focused suite also checks selection release provenance. These corrections
changed no candidate geometry, semantic content or model hash.

## Solution Architect inspection boundary

Use the current [inspection guide](../archi-inspection-guide.md) to import the
complete unsigned candidate into Archi 5.10. Inspect one- and two-column
Containment examples, source-side diamonds and clear corridors, Business routes,
and the four protected architect-authored presentations. The guide includes exact
representative View IDs. The automated task stops here. Human rendering acceptance
and final Solution Architect sign-off remain outstanding; no approval is implied.

Reproduce the bounded checks from the repository root:

```bash
timeout --signal=TERM --kill-after=5s 120s python3 -B docs/archimate/framework/tools/synchronise_baseline.py --check
timeout --signal=TERM --kill-after=5s 90s python3 -B docs/archimate/processing/publishing/1.0.9/view-presentation-completion/verify.py preservation
timeout --signal=TERM --kill-after=5s 90s python3 -B docs/archimate/processing/publishing/1.0.9/view-presentation-completion/verify.py transport
timeout --signal=TERM --kill-after=5s 120s python3 -B docs/archimate/processing/publishing/1.0.9/view-presentation-completion/verify.py history
```
