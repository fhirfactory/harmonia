<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Capability replacement candidate

The working Continuum incorporates the accepted Pass3/014 Capability Model under
the [separately issued replacement Task](../../before/CapabilityReplacementTask.md).
The migration result remains pending Solution Architect review in
[HCRL 1.1.20/001](../../../../../review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5).

[Formal execution Report](../ai-agent-reports/20261110T062320Z-capability-model-replacement.md)
records the completed migration, authority, inferred choices, verification and
remaining gates. [Verification summary](verification-summary.json) retains the
final **87 tooling / 90 architecture tests PASS**, candidate checks and earlier
FAIL/TIMEOUT outcomes. [Result manifest](result-manifest.json) pins selected
inputs, candidate/correspondence evidence and actual Git state.

Use the **second candidate**, which preserves the existing Business Functions,
Business Services and Business Processes navigation conventions inside the new
Capability contexts:

- [HTML architecture reader](candidate-v2/reader/continuum.html).
- [ArchiMate Open Exchange candidate](candidate-v2/Harmonia-Architecture-candidate.xml).
- [Narrative publication source and structural correspondence](candidate-v2/narrative/README.md).

The first candidate is retained in `candidate/` with its original bytes. Its
definitions, identities, relationships and authored Views agree with the second
candidate; the latter refines Business navigation only. Neither candidate is an
accepted reference publication. The existing 1.1.19 publication is immutable.

| Active population | Count |
| --- | ---: |
| CT1 | 24 |
| CT2 | 145 |
| CT3 | 585 |
| Feature | 2,935 |

There are 3,689 new canonical identities, 842 retained legacy Capability/Feature
identities and 1,154 retained elements of other native types: 5,685 elements in
total. Native Capability includes Feature. Historical populations remain visible
with their actual currentness; they are excluded from the active model counts.

The accepted combined Business/Business Enabling and separate Enterprise
hierarchies retain their catalogue IDs and definitions. The single editorial
`**[NEW — P3/002, FC-076]**` marker on E07.02.08 is retained in source-name
provenance rather than treated as part of its human or programmatic name.

628 explicit contextual semantic selections and 214 previously accepted historical
correspondences account for the entire old population. Correspondence can be
partial or multiple. Its primary successor supports historical navigation and
never establishes complete semantic equivalence. Corporate Communications &
Publicity has no legitimate successor in this model and remains a reserved,
retired resource. This is a permitted unmapped disposition, not a review question.

| Original boundary relationship disposition | Count |
| --- | ---: |
| Old Composition replaced by the accepted hierarchy | 676 |
| Migrated as a distinct native assertion | 725 |
| Consolidated into an already represented migrated assertion | 31 |
| Collapsed within one replacement responsibility, without a self-dependency | 20 |
| Explicitly historical assertion retained without promotion | 18 |

All 1,470 original boundary relationships retain their original identities,
endpoints, native types, labels and explanations. Explicit superseded transport
state excludes them from current containment. The 725 distinct migrated edges
comprise 138 Realization, 217 Serving and 370 Association assertions; 212 of the
Associations retain the explicit, directed Home specialisation. Home determines
the corresponding 212 behaviour naming updates. Realization never generates Home.
The new hierarchy has exactly 3,665 Composition assertions.

Direct non-hierarchy links reach 223 replacement elements, including 21 Features.
This is the extent of represented migrated traceability. It supplies neither
implementation satisfaction nor an implementation-gap census of the much larger
accepted model. Existing behaviour definitions and domain authority are unchanged.
Independent assurance judgement is not reassigned to evidence-collection machinery.

Inspect the machine-readable evidence:

- [Old-to-new scope, identity and definition correspondence](semantic-correspondence.json).
- [Every original relationship and its actual disposition](relationship-reconciliation.json).
- [Accepted parent/child relationship correspondence](hierarchy-reconciliation.json).
- [Explicit Home and preserved old/new naming](home-naming-reconciliation.json).
- [Full canonical validation](canonical-validation.json).
- [Independent preservation and scope checks](preservation-validation.json).
- [Final candidate schema, containment and interchange checks](projection-validation-v2.json).
- [Exact repeated-projection check](determinism-validation-v2.json).
- [Canonical local links](link-validation.json).

The original source criteria/status, XD/SR/FR and AR-001/AR-002 contributions,
offline retirement correspondence and workbook remain retained at their original
locations. The source-incorporation record identifies only the definitions and
hierarchy whose active architectural role now belongs to canonical Markdown.

Actual supported-Archi import/export, Solution Architect workbench/reader
structural review, the inherited office/JDK assurance limitations and unresolved
temporal provenance retain their recorded standing. E07.03.05 and the unrelated
HCRL residual matters remain qualified. No HCRL completion, release allocation,
accepted publication, commit or push is performed.

`authoring/` contains purpose-selected one-time authoring and verification
evidence. The persisted allocation ledger records calls to the maintained
`architecture_continuum.create`; IDs are never reallocated during projection.
The initial sparse allocation workspace does not replace full baseline-preserving
validation. Normal authoring/publication reads canonical Markdown, current
navigation and maintained tooling without loading this adapter or historical
evidence as architectural source.
