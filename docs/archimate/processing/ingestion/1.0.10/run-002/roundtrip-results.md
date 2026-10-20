<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Run-002 Archi round-trip review results

**The export preserves the proposed Run-002 semantic model, governed metadata
and authored View content.** It introduces no additional architectural change
relative to the candidate. Exact XML/navigation serialization is not preserved.
No directives were applied, identities allocated, architecture accepted or RAD
release created. The release remains **1.0.10**, predecessor **1.0.9**.

| Check against the exact Run-002 candidate | Result |
| --- | --- |
| Elements | **PASS** — all 1,394 retained; zero new, deleted or evolved. |
| Canonical identity | **PASS** — all 1,362 claims, owners and representation bindings retained; 32 elements still have no canonical claim. |
| Governed metadata | **PASS** — all three names, archimateType and applicable harmoniaType values retain exact owner/value/language/multiplicity. |
| Relationships | **PASS** — all 1,137 retained with exact types, endpoints, directions and content after resolving Property references by definition key. |
| Views | **PASS** — all 1,209 retain content, identities, membership, nesting, order, layout, geometry, style, Documentation and connections after Property-key resolution. All 4,005 visual objects and 1,804 visual connections survive. |
| Navigation structure | **PASS** — all 3,740 reference paths and 3,598 folder paths preserved; all 993 containment participants retain own folders/self-references, including Care → Encounter. |
| Navigation serialization | **FAIL for exact preservation** — differences retained: **4,717 navigation item IDs lost**, **215 sibling contexts reordered**. These IDs are separate from architectural/Exchange element, relationship and View identities. |
| Other representation loss | The `Machinery validation / Candidate standing` Property-definition Name loses `xml:lang="en"`; text/value are retained. |
| Shared naming derivation | **PASS for 82 determinate information elements**, zero metadata corrections; **four inherited classification questions remain unresolved**. |
| Required architecture tests | **PASS** — 90 tests, zero failures/errors/skips; Maven 17.973 seconds. |
| Existing Run-002 replay/integrity | **PASS** — exact regeneration; all 95 output, 88 source and 28 frozen hashes verified. |

The reviewed input is the directory's sole XML file,
[ArchiMate-Architecture.xml](ArchiMate-Architecture.xml), SHA-256
`f18a4d143e683c3a24970978f3f8b8576f996fc12d770789fb723073510fc2d4`.
The supplied locator omitted the hyphen in its actual basename. Comparison uses
the manifest-pinned proposed candidate, SHA-256
`541219c023d2cd507d591e81df9a0a82fbf1c8f4d324448a02bc476e00c3e59a`,
and separately the governed RAD-1.0.10 publication and its actual no-edit control.
The proposed candidate is not promoted to an accepted baseline.

The [governed Change Review](change-review.md) records **154 inherited proposed
changes and four unchanged classification findings** as **158 independent CRs**:
32 new Topics; 38 BusinessObject → Topic evolutions with canonical continuity;
12 retained BusinessObject naming migrations; 59 Compositions; one Aggregation;
11 new Views; one revised View; and the four classification questions. All CRs
start **On-Hold / Unresolved**, with **Not authorised** instructions and **Not yet
processed** records. These counts describe proposals against the publication,
not new edits made during the supplied round trip. Derived navigation and raw XML
differences are not counted as additional architectural CRs.

The four unchanged native DataObjects are **Context Topic, Pragma Topic,
PetasosParcel Topic and Pylai.Diadromí Topic**. They lack explicit Topic
specialisation; their Names do not establish that classification. Their existing
metadata remains intact. Historical relationships remain preserved and excluded
from current ancestry as governed; no folder, View or Aggregation is promoted to
Composition or Home.

The user identifies this file as the Run-002 Archi round-trip export. Exact Archi
version, operator/export profile, a no-edit declaration and rendering/architectural
acceptance were not supplied or inferred. Observed full-population semantic/View
preservation, whole-XML fidelity, execution provenance and acceptance remain
separate results. Known navigation/language residuals and those missing
attestations remain explicit at the human checkpoint.

The review stops at **Solution Architect Change Review**, under the
[governed Praxis](../../../../../markdown/governance/archimate-round-trip-engineering-praxis.md).
Its [detailed audit](detection/review-audit.json), [raw comparator evidence](detection/candidate-to-export.json.gz),
[evidence manifest](validation/review-manifest.json), [verification results](validation/results.json)
and [read-only verifier](validation/verify-review.py) retain the findings.
