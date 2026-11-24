<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Bounded authored presentation comparison — 1.1.18 / 001

**UNRESOLVED.** Complete presentation retained in staged evidence; maintained
template unchanged. No importable verification XML was generated. The native
Groupings referenced by 57 newly authored Views require Solution Architect
disposition. Neither their creation nor their replacement by visual containers
is authorised by this presentation-only Task.

## Exact baselines

Paths, byte hashes, original Git state and protected-file hashes are retained in
[input pins](../../before/views-reconciliation/input-pins.json). The mutable
template preimage and pending HCRL/Change Review preimages are beside those pins.
The incoming 004 export, published baseline and Subject incorporation XML remain
at their original retained paths, unchanged.

| Baseline | Elements | Relationships | Views | View folders including `Views` | Empty folders |
| --- | ---: | ---: | ---: | ---: | ---: |
| Authored 004 | 1,962 | 1,884 | 1,285 | 1,183 | 42 |
| Maintained template at task entry | 0 | 0 | 1,216 | 1,046 | 1 |
| Published 1.1.18 | 1,842 | 1,882 | 1,216 | 1,043 | 0 |
| Accepted Subject incorporation verification | 1,874 | 1,909 | 1,216 | 1,046 | 1 |

Template semantic collections are intentionally empty; a template is not a full
Exchange model. Counts above distinguish folders from View-reference entries.

## Authored presentation retained in staging

[Staged template](projection-template.staged.xml) contains all 1,285 incoming
Views, including 69 additions and all 1,216 existing View identities. The incoming
Views branch is preserved, with the accepted G18.03 correction below. Every
existing View has a different folder path from the Subject projection. The three
incoming top-level branches are preserved verbatim and in order:

1. `1. Subject-Based-Views`
2. `2. Product-Based-Views`
3. `3. Achitecture-Based-Views`

The third branch's authored spelling is retained. All 1,183 folders, including
42 empty folders, labels, nesting, sibling order and item metadata are preserved.
No old-only folders are appended to alter the authored hierarchy. All 1,216 old
Views remain present. The complete ordered
[folder inventories](folder-inventories.json) preserve structural positions and
subtree signatures; [View comparison](view-comparison.json) records each View's
identity and exact/changed/moved/reference status. These inventories support
verification; they are not an architectural semantic delta requiring incorporation.

All membership, nodes, nesting, geometry, connection endpoints/routing, styles,
Documentation, property values and order compare exactly as parsed XML after
the documented definition-reference rebindings below. Whitespace used solely for
XML indentation and namespace-prefix spelling are excluded from signatures;
this is not a claim of byte identity or actual Archi rendering fidelity.

Of 35 raw differences in existing Views, 34 consist only of Property definition
identifier changes. The remaining existing View, `Assessments`
(`id-7d0bc19870225280ac6b0a268ce8a92b`), has an authored node fill-colour change
from RGB `(245,222,245)` to `(204,204,255)` with alpha 100 unchanged. It is
retained in staging. See the [specific observation](authored-existing-view-change.json).

## Intentional transformations

Only the following transformations are made in the staged presentation:

- CR-060: `03. Directory-Informaton-Maintenance` becomes
  `03. Directory-Information-Maintenance` under Subject-Based-Views / Provider
  Directory. The incoming export remains unchanged.
- 68 View Property references across 34 Views are rebound to the maintained
  definitions. Definition names, types and all definition content other than
  identifier match exactly. Their values, multiplicity and language are unchanged.

| Authored definition ID | Maintained definition ID | Exact definition name |
| --- | --- | --- |
| propid-49 | propid-43 | Baseline / Source Path |
| propid-51 | propid-45 | Baseline / Source SHA256 |
| propid-58 | propid-52 | v0.2 / Source Path |
| propid-59 | propid-53 | v0.2 / Source SHA256 |

Copying these IDs without rebinding would incorrectly attach the values to other
maintained definition names. No incoming property definition or model property
is imported. There are no architectural element identity rebindings. Of 1,285
Views, 1,251 are raw parsed-content exact matches to incoming, and all 1,285
match after those documented Property-reference transformations. The entire
Views organisation subtree is exact except for CR-060. See the
[transformation ledger](intentional-transformations.json).

## Unresolved architectural references

The existing identity correlator matches all 1,842 pre-existing canonical
elements, with zero architectural rebindings or ambiguous claims. It identifies
120 incoming additions with no canonical correspondence. All 120 are native
Grouping records with no canonicalId; their names commonly end in
`.Assessments`, `.Principles`, `.Constraints` or `.Requirements`. Their native
type is architectural content, even when used to arrange diagrams. Presentation
authorisation does not establish their semantics, equivalence or acceptance.

They occur in 127 View nodes across 57 of the 69 new Views. None occurs in the
1,216 existing Views. [Unresolved references](unresolved-references.json) retains
every incoming ID, native name, affected View/node, source XPath and line.
The complete export retains the original native records and presentation.
No missing record is mapped by lexical similarity, converted to a visual
container, removed, or incorporated into the Continuum.

All 1,804 visual relationship references resolve against accepted architectural
relationships. All their directly element-backed visual endpoints match the
canonical relationship endpoints; there are zero mismatches. This does not
accept unrelated incoming relationship/property changes. All 4,350 elementRef
occurrences are checked; 127 are unresolved. No View references were observed.
All 68 Property references have established definition mappings.

## Verification outcomes and limits

- **PASS:** secure parsing and the existing Exchange checks on the original
  incoming, published and Subject XMLs; original accepted projection generated
  from the current canonical source reproduces the Subject XML byte-for-byte.
- **PASS:** all 32 accepted Subject identities and 27 Composition IDs/endpoints
  remain intact; all 1,874 canonical resource files and all 1,882 original
  projected relationships are unchanged. All 1,946 pinned protected files,
  including original evidence and the 784-candidate assessment, remain unchanged.
- **PASS:** staged presentation fidelity after the enumerated transformations;
  architectural collections remain empty in the staged template. No semantic
  records, unrelated native folders or model properties are copied from 004.
- **FAIL:** the complete in-memory canonical-plus-staged-presentation probe fails
  offline Exchange XSD validation with 127 missing ElementRef keyref errors.
  The maintained projector independently rejects the same staged template in an
  isolated source root at the first missing Grouping. This is the expected
  fail-closed gate, not a successful candidate validation.
- **UNRESOLVED:** the requested import-ready XML cannot be emitted within the
  existing authority. The staged template is evidence and is **not for import**.
- **NOT_PERFORMED:** actual Archi import/export, visual inspection and Solution
  Architect acceptance. Java architecture tests were not executed in this
  activity; their prior dependency-cache blocker remains recorded in the earlier
  [incorporation Report](../ai-agent-reports/20261101T232558Z-accepted-subject-incorporation.md).
  No production code or maintained machinery was changed to evade a gate.

The system Python lacked `jsonschema`; the existing `/tmp/harmonia-tooling-venv`
was used for projector/preservation validation. Initial diagnostic harness
attempts exposed deliberate empty-template schema constraints, generated Property
definition requirements and the projector's `DocumentInvalid` exception type.
The final harness uses a complete in-memory probe and records the schema failure
explicitly; no failed attempt is represented as a successful candidate.

[Summary](summary.json), [canonical validation](canonical-projection-validation.json),
[XSD errors](structural-probe-xsd.json), [projector gate](staged-generation-preflight.json)
and [independent preservation verification](preservation-verification.json)
distinguish those outcomes. Reproduction uses the retained comparison/preservation
scripts with the maintained tooling and its declared Python dependencies.

## Required disposition and subsequent visual gate

Under AX-14/AX-17, the Round-Trip Praxis stop rule and the governance fail-closed
rule, decide the architectural/presentation treatment of the 120 Groupings or
supply established canonical identity correspondences. Any incorporation or
replacement by visual containers needs separately explicit authority. Their
names or diagram nesting cannot supply that decision. The maintained template
is unchanged while that dependency is unresolved.

After disposition, the complete staged presentation can be reconciled within
the approved treatment, applied to the maintained template and reprojected.
The resulting XML must pass XSD, references, canonical preservation and
presentation comparison before the Solution Architect imports it into a
separate supported Archi model and checks the tree, empty folders, new/existing
Views, layout, connections and G18.03. No actual transport/visual acceptance is
claimed here. No release transition, protected-publication replacement, HCRL
closure, commit or push occurred.
