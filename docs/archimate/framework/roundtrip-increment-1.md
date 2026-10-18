<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Increment 1: read-only conformance and Archi transport proof

Release **1.0.6** remains the accepted architecture baseline, inspected by Mark in
Archi **5.10.0**. This opt-in probe does not publish, reconcile or change architecture.
The accepted model has **zero** future UUID32 `canonicalId` Properties. Its **14 EC
codes remain protected identifier aliases/provenance**, with their frozen adoption
proof intact. Allocation and naming migration require a later authorised task.

## Run the diagnostic

From the repository root, using the publication tools' existing Python/lxml environment:

```bash
timeout --signal=TERM --kill-after=5s 60s env PYTHONDONTWRITEBYTECODE=1 python3 docs/archimate/capability-architecture/tools/diagnose_roundtrip.py diagnose --baseline-context --output /tmp/harmonia-106-diagnostic.json
```

`audit_status=PASS` means the audit ran, while `future_identity_readiness=NONCONFORMANT`
reports the unmigrated model. Exact Property keys are `canonicalId`, `displayName`,
`friendlyName`, `canonicalName`, `archimateType` and optional `harmoniaType`.
UUID32 uses the final assessment's lowercase `[0-9a-f]{32}` contract; values are
never trimmed, converted or allocated. Native `<name>` remains the display carrier;
an explicit `displayName` Property must agree if present. The synthetic fixture
exercises both carriers. Native type comes from `xsi:type`.

Legacy short `v0.2 / Canonical Name`, qualified `v0.2 / Structural Canonical ID`,
EC identity, source references and alias channels are inventoried separately.
`--baseline-context` checks existing source/crosswalk authority, uses `Baseline.chain`
for exact context, compares to existing persisted source paths and retains standing.
It derives no friendly-name tokens and invents no missing ancestry or universal
canonical-name grammar. Legacy short-label differences are observations, not future
naming conflicts or evidence of identity. Without this option, source comparison is
unavailable; XML-only coverage remains complete.

`--previous <earlier.xml>` optionally detects changed UUID claims at the same
representation binding and possible reuse across bindings. A changed binding is
reported for adjudication; two snapshots cannot prove lifetime non-reuse. The
current frozen register contains EC codes, with no future UUID allocation/history.
No new active register or relationship identity policy is introduced.

## Prepare and perform the two Archi exports

Current state: **AWAITING ARCHI EXPORT** for both fixtures. The installed Exchange
plug-in was inspected; no accessible display or Xvfb is available to this agent.
Existing historical GTK failure evidence is preserved. GUI acceptance of 1.0.6 is
established, but does not prove an unchanged export.

The [manifest](tests/fixtures/roundtrip/manifest.json) pins both exact inputs, the
source revision, the inspected plug-in and the expected export profile. The large
accepted XML is reused in place; it is not duplicated or modified. Ensure Archi
5.10.0 is using English for this profile. Prepare the destination once:

```bash
mkdir -p .junie/reports/2026-10-18-archimate-roundtrip-increment-1/archi-exports
```

For each row below:

1. Use **File → Import → Open Exchange XML Model…** to import the supplied input as
   a fresh model. Do not use a previously edited working copy or merge models.
2. Make no changes.
3. Select that model and use **File → Export → Model To Open Exchange File…** to
   export to the specified destination. Enable **Include Folder Organization** and
   **Validate after export**; set **Language code** to `en`. Export the entire model,
   including all Views. Copying XSD files is unnecessary; offline schemas are bundled.

Paths are relative to `/home/mhunter/Development/code/projects/github/harmonia`:

| Input | Export destination |
| --- | --- |
| `docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-Baseline.xml` | `.junie/reports/2026-10-18-archimate-roundtrip-increment-1/archi-exports/release-1.0.6.xml` |
| `docs/archimate/capability-architecture/tests/fixtures/roundtrip/future-contract.xml` | `.junie/reports/2026-10-18-archimate-roundtrip-increment-1/archi-exports/future-contract.xml` |

The second model is **non-governed synthetic test data**: three unrelated test
elements, literal qualified names, leading-zero/digit/letter UUID32 strings, a
Feature represented as native Capability, one relationship and one View with an
annotation and ordered bendpoints. These strings are not allocations to governed
elements, and these qualified names define no naming algorithm. Never incorporate
this model into the governed architecture.

After both real exports, run:

```bash
timeout --signal=TERM --kill-after=5s 60s env PYTHONDONTWRITEBYTECODE=1 python3 docs/archimate/capability-architecture/tools/diagnose_roundtrip.py verify-exports --operator Mark --attest-archi-5.10 --output .junie/reports/2026-10-18-archimate-roundtrip-increment-1/archi-fidelity.json
```

The attestation declares that the operator performed that exact profile using
Archi 5.10.0. Use it only after doing so. The report binds the declaration to both
input/output hashes; it cannot independently authenticate provenance. Without the
attestation, equivalent XML alone leaves empirical fidelity unresolved. If any
option/version/locale differs, preserve the actual export and report the deviation
before asserting that the requested profile was executed. Retain the raw exports
unchanged with the report. No manual XML inspection is required.

Missing exports produce `AWAITING ARCHI EXPORT` (exit 3). Invalid/ambiguous inputs
produce `UNRESOLVED`; observed loss/content/residual differences produce
`TRANSPORT FINDINGS` (exit 2). A `PASS` requires operator-attested execution and
equivalence in both probes. Known losses remain findings requiring review; they
cannot become PASS through an allowlist. Passing proves transport behaviour only,
never architecture acceptance, naming authority or permission to allocate IDs.

## Interpretation and limits

The same-representation probe compares all XML content on the exact published/
exported pair, not an architecture change set. It reuses secure parsing, offline
XSDs, global identifier checks, serialization/signatures, release loading and
existing source/crosswalk/ancestry validation. The old `g.Model` assumes only
Capabilities and Compositions and one value per definition ID; the small shared
`Exchange` index supports the active model's mixed types and retains all claims
without altering historical readers or recovery.

Exact unique Property-definition names resolve each document's definition IDs
independently. Rekeying is equivalent only for the key association; definition
datatype/language/metadata and every value/owner are still compared. Duplicate or
ambiguous names/references remain unresolved. Uniquely keyed unordered element,
relationship, View, definition and Property collections may reorder. XML namespace
prefixes (including QName `xsi:type`) and element-only indentation are syntax.
There is no blanket whitespace, number, JSON, geometry, language or style cleanup.

Every other difference remains classified as semantic difference, information loss
or unexplained difference. The presentation-preserving category is reserved; no
unproven visual equivalence rule is installed. Organisation items are paired strictly
by position for this no-edit probe, retaining every lost item ID and all
label/reference/hierarchy/order differences. This does not recover folder identity
or infer architectural ancestry. Folder reordering may yield conservative residual
findings requiring review.

The report retains relationship IDs, types, endpoints, Documentation/Properties and
all residual attributes. It separately records View and View-local visual IDs,
membership/nesting, coordinates, dimensions, styles, annotations, stacking order,
ordered bendpoints and residual XML. Changes to visual order or numeric geometry
are unexplained until equivalence is demonstrated. It performs no reconciliation,
new-identity matching, conflict resolution or presentation replay.

For an individual pair, `compare --published <input.xml> --incoming <export.xml>`
provides the same findings without execution attestation. Direct comparison exit 0
means XML-model equivalence only; exit 2 means findings/unresolved input, exit 3
means absent export. Diagnostics exit 0 on a completed audit even when future
conformance is missing. Output files are restricted to `.junie/reports` or `/tmp`,
and cannot overwrite input files.

Model-size metrics are calculated from each input: semantic elements, relationships,
Views, visual objects/connections, largest View and XML bytes/decimal MB. Publication
generation/validation timing is deferred to the existing publisher verification
layer; the probe does not time or regenerate architecture. Neither measurement nor
timing is an acceptance threshold.

Focused tests run through the existing publication suite:

```bash
timeout --signal=TERM --kill-after=5s 120s env PYTHONDONTWRITEBYTECODE=1 python3 -m unittest discover -s docs/archimate/capability-architecture/tests -v
```

The [Increment 1 report](../../../.junie/reports/2026-10-18-archimate-roundtrip-increment-1.md)
records actual validation and the outstanding human gate.

## Release 1.0.7 complete-model empirical acceptance — 2026-10-18

The Solution Architect's real no-edit Archi 5.10 export of the governed Release
1.0.7 publication has now completed the Canonical Identity and Governed Naming
acceptance gate. All 1,299 Canonical IDs and each of the three governed names
survive exactly on their original elements; element/relationship identities,
relationship endpoints and all 14 EC bindings survive. The
[completed establishment report](../../../.junie/reports/2026-10-18-archimate-canonical-identity-establishment.md#real-archi-proof-and-increment-2-gate)
records the input/export hashes, counts, presentation/navigation checks and validation.

The existing comparator and its equivalence rules are unchanged. Whole-document
comparison remains **DIFFERENCES**, fully explained by the accepted Increment 1
behaviours. Historical Release 1.0.6 evidence above and in the original report is
preserved. This acceptance authorises no Increment 2 work.
