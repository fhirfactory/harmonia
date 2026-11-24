<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Execution-contract observation — authorised 1.0.8 reconciliation

Recorded by Codex, 2026-10-19. This is execution evidence, not a proposed future
reconciliation contract. AX-04 preserves authored meaning independently of the
tooling; AX-14 separates acceptance, resulting state, semantic edges, presentation
and identities; AX-15 preserves uncertain effects; AX-17 forbids filling gaps.

## Product naming stop — resolved by Solution Architect decision

Execution stopped before model mutation or identity allocation because the four
accepted native Products (run-001 CR-002–005) required canonical names and the
governed formal naming vocabulary did not contain a Product qualifier. Choosing
a qualifier would have required a new governance decision. The stop was reported
to the Solution Architect; no Product responsibility, hierarchy or name was guessed.

The Solution Architect explicitly confirmed the stop was valid and decided that
native ArchiMate Product is an authorised canonical-name element class using
`PRODUCT`. All other naming derivation rules remain unchanged. The architect
directed an update to the governed naming contract and validation/tests, then
resumption of the authorised reconciliation. The updated contract is
[canonical identity and naming](../../../../../../../markdown/governance/canonical-identity-and-naming.md#native-product-canonical-name-vocabulary).

The implementation adds `product: PRODUCT` to the existing vocabulary and shares
the existing lexical/qualification derivation across element classes. No four-name
table or Product-specific naming algorithm supplies the results. Product naming
tests include ordinary lexical spelling, explicit context, rejected digit-leading
spelling and rejected persisted-name drift. All existing name derivation tests
remain in force. The original naming contract is retained in
[canonical-identity-and-naming-before.md](canonical-identity-and-naming-before.md).

After this decision, nine UUIDv4 identities were allocated once and persisted in
the accepted resource capture. [accepted-source.json](accepted-source.json) retains
the entire allocation/source history. Ordinary publication reads these values and
allocates none. Original source keys/bindings and Canonical IDs remain distinct
from CR process identity.

## Resulting-state execution and presentation

The reconciled source captures the nine element instances and their identities,
six explicit CT3 → Feature Compositions, four native Realizations, two Flows,
one Serving and four independent directed Association «Home» edges. Endpoint
instances exist before relationship projection; actual semantic edges exist before
dependent View connections are replayed. The complete source capture is validated
and projected as a bounded resulting state, never as a partially published model.

The Feature name uses its accepted Composition context. New behaviour names remain
local under the existing naming rules; no naming context is inferred from Home or
Realization. Authored navigation and accepted View layouts are presentation only.
Relations follow actual source-resource navigation homes. No automatic Home rule,
Product hierarchy or general naming/navigation consequence of Home was introduced.

The fixed baseline Home register is unchanged: 208 normalised, 93 unestablished
and 18 ambiguous. The four authored assertions are recorded separately; they do
not modify that register or resolve its debt. Unrelated elements/relationships and
Views remain preserved. Existing baseline Name/displayName discrepancies remain
outside the accepted CRs; all nine new elements satisfy Name == displayName.

## Record handling and release

The adjudicated reviews had later Accept calls in their dashboards while detailed
records still retained earlier On-Hold/Not authorised fields. The explicit
execution task confirmed those later calls. Current execution revisions record
the later Accept instructions and actual resulting states; the original bytes are
retained in each run's change-review-before.md, with source/hash/link-origin
manifests. The ten interpretation findings' complete detailed blocks, including
On-Hold Dispositions and Resolved Outcome Reasons, remain byte-identical.

The explicit target is RAD-1.0.9, predecessor RAD-1.0.8. The prior root value
1.0.14 is preserved in architecture-release-before.json and historical governance/
tooling artefacts. The task's explicit architecture-release decision resolves the
numbering; the tooling sequence does not allocate the RAD number.

## Verification observations

Initial Maven verification hit read-only resolver metadata. Offline verification
completed; Java 25 also exposed old ArchUnit bytecode-parser warnings. Repeating
with the repository's configured Java 21 completed all 90 architecture tests.
These were environment/harness observations; production code was not changed.

The original Home-report reproduction check rejected stale review hashes after
the Solution Architect adjudicated the reviews. That failure remains in the
preflight evidence. Historical verification was not refreshed or rewritten.
The current reconciliation publication/report has its own actual verification
evidence, and independently reproduces the entire output set. The original 14
outputs remain; the accepted native Product catalogue adds one derived output.

The first complete current ArchiMate run passed 176 of 177 tests: one regression
still expected the old active population of 1,299. Updating that current-model
expectation to 1,308 resolved the harness mismatch; the focused 27 round-trip tests
passed. Historical 1.0.6 expectations remain unchanged. The full final rerun is
recorded separately; the failed invocation remains preserved.

Archi's command-line help ran inside the sandbox, but actual import failed when
GTK initialised its display. An explicit X11-backend attempt failed similarly.
The authorised CLI run outside the sandbox, with isolated temporary configuration,
workspace, keyring and output, imported, exported, schema-validated and saved the
complete model successfully. No running human model was edited.

The actual exported model preserves all identities, names, native non-Property
semantic content, endpoints, all 212 Home assertions, navigation memberships,
View identities/geometry and visual connection content. All four accepted Views
survive exactly. The raw comparator remains DIFFERENCES, including 1,669 semantic
text findings identical to the established 1.0.7 evidence. No comparator equivalence
rule or normalisation was added; whole-model lossless fidelity is not claimed.
Human rendering acceptance remains pending under the existing publication gate.

No further architectural/governance execution gap was encountered. No future
short execution contract was designed. No Information or Application Architecture
work or unrelated debt cleanup was commenced.
