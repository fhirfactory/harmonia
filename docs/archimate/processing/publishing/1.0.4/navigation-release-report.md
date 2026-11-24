<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Harmonia Architecture Release 1.0.4 — Archi navigation and Views

**Ready for human inspection: 1.0.3 → 1.0.4.**
The [root release authority](../../../architecture-release.json) was incremented
with `scripts/architecture_release.py bump --expect 1.0.3`.
Generation does not increment it. Maven is unchanged. No commit or human visual
acceptance is claimed. The previous accepted baseline is commit
`053f17a7f3ccfa31bf18fc7d611c480a467884c3`.

## Semantic preservation and identity

| Measure | Observed |
| --- | ---: |
| Existing Capability elements preserved | 228 |
| Semantic elements added / removed / changed | 0 / 0 / 0 |
| Existing Composition relationships preserved | 189 |
| Relationships added / removed / changed | 0 / 0 / 0 |
| Semantic identity changes | 0 |
| Capability hierarchy / relationship endpoint changes | 0 / 0 |
| Existing visual identity / contents changes | 0 |
| EC Architecture Identity bindings retained | 14 (EC-01–EC-14) |
| Identity adoptions / allocations / migrations in 1.0.4 | 0 / 0 / 0 |
| Governed Documentation fields preserved / changed | 14 / 0 |
| Unresolved capability identity crosswalks | 214 |

[Binding validation](../../markdown/governance/architecture-identity/bindings-validation.json)
checks the same governed definitions, provenance hashes, register and existing
semantic bindings. The register and its schema remain byte-identical. Architecture
Identity, native definition/source IDs, navigation keys, semantic IDs, visual IDs
and implementation IDs retain their separate meanings. No identity property was
added to the XML. Names, descriptions, tiers and relationships are unchanged.

The unchanged original publishers replay the exact 1.0.3 XML against the immutable
baseline/semantic lock. Comparing all content except organisations and Views gives
`b71284a557278201f2301d464da92bcc322d0ed8553aa387d9f61842205072b6` in both releases. Each retained
View is also compared independently, including its original visual IDs and geometry.
Folder containment cannot supply semantics: the hierarchy is reconstructed from
existing Composition relationships before navigation is generated. Rejection tests
cover moved folders, duplicate homes, target-based relationship placement, invented
Composition, changed semantics/endpoints and incorrect View scopes.

## Visible navigation and View changes

The reusable [governed convention](../../markdown/governance/archi-navigation-and-view-convention.md)
separates architecture, model-tree navigation and View navigation. It also records
future Business context and independent Application Data Objects Model / Software
Components structures; **none of those domains is published in this release**.

| Presentation measure | Before | After |
| --- | ---: | ---: |
| Exchange navigation folders (including empty context branches) | 7 | 656 |
| BC detail Views | 0 | 52 |
| BEC detail Views | 0 | 162 |
| Existing diagrams retained unchanged | 5 | 5 |
| Total diagrams | 5 | 219 |
| Views replaced / removed | 0 | 0 |

All 7 prior folder IDs are replaced by 656
deterministic presentation folder IDs. Semantic element/relationship IDs and
retained diagram/node/connection IDs do not change. Each element has one home;
each of the 189 relationships has exactly one home following its source capability.
The diagnostic's original reference item remains directly under Views.

BC/BEC use same-named folders containing the existing capability and its child
folders. EC stays an orthogonal catalogue. Detailed View folders mirror the same
BC/BEC decomposition. Each new diagram shows its current context and immediate
children only, including leaf overviews. The 214 additional diagrams add
403 visual nodes and 189 visual
connections representing existing semantic objects. The largest detail View has
22 nodes, within a four-column layout of at most
1520 × 888 units.
The retained BEC all-tier overview remains large; smaller branch Views provide the
progressive route through it. No missing natural-view memberships are inferred.

### Views retained

| Existing View | Preserved ID |
| --- | --- |
| Archi Import Test - Individual Health Assessment | `id-ef8af9b152505720b37914080c957a1c` |
| Harmonia Capability Landscape | `id-a4bbb7df8ab95f8ebcadd44f43ecc9ce` |
| Business Capability Model | `id-030f98270e13527db62632fd9c31df86` |
| Business Enabling Capability Model | `id-b531027008a75f34a4d8401ac0d22568` |
| Enterprise Capability Model | `id-2cf3bc990f115e36987f676ae6ea8656` |

The landscape and the three category overviews move under `Views / Capabilities`.
Their contents remain unchanged. BC/BEC overviews show CT1 + CT2 + CT3 and no deeper
constructs. The import diagnostic stays at its original direct Views home.
No View is replaced. [validation.json](validation.json) lists every added View ID,
context, exact navigation path, node/connection count and dimensions.

## Inspect in Archi

Import this exact Open Exchange artefact:
[Harmonia-Capability-Architecture.xml](Harmonia-Capability-Architecture.xml).
SHA-256: `98c1074c8ead4c55708dc85b327a98e077feb8e59ff58cec9c2852a1332e9e75`.

1. Expand `Strategy / Business Capabilities / Individual Care Delivery`.
   The folder contains its same-named CT1 element and the existing child folders.
   Follow `Individual Health Assessment` to its existing CT3 folders. Also inspect
   the independent `Strategy / Business Enabling Capabilities / Care Administration
   Enablement / Care Plan Administration / Care Plan Lifecycle & Coordination` path.
2. Expand `Strategy / Enterprise Capabilities`: the 14 existing EC elements remain
   a flat, separate catalogue. Check EC-12/EC-14 Documentation and identity continuity.
3. Expand `Relations / Capabilities / Business Capabilities / Individual Care Delivery`:
   outgoing CT1 relationships live here; outgoing CT2 relationships live under their
   source's child folder. Each relationship occurs once. Empty source-context folders
   may be omitted by Archi's importer because they contain no relationships.
4. Open `Views / Capabilities / Harmonia Capability Landscape`, then
   `Business Capabilities / Business Capability Model` and
   `Business Enabling Capabilities / Business Enabling Capability Model` for the
   retained three-tier overview diagrams.
5. Under `Views / Capabilities / Business Capabilities / Individual Care Delivery`,
   open `Individual Care Delivery Overview`. Then follow the `Individual Health
   Assessment` folder and open `Individual Health Assessment Overview`; follow its
   CT3 folders for leaf overviews. Each drill-down narrows to context plus immediate
   children. Repeat with the BEC Care Plan Administration path above.
6. Open `Views / Capabilities / Enterprise Capabilities / Enterprise Capability Model`
   to confirm the independent catalogue projection.

These tree structures and bounded diagrams are the visible change to review.
**Human visual acceptance remains pending.** The optional installed-Archi 5.9 CLI
attempt returned exit 13 before model import with SWT `gtk_init_check() failed`;
`xdpyinfo -display :0` independently could not open the display. This is an
**UNRESOLVED environment check**, not a model rejection. No repeated blind import
attempt or GUI success is claimed. Import in a working desktop session is the
next visual check. Offline Archi 5.9 XSD validation below passes.

## Validation

| Required check | Result |
| --- | --- |
| Required repository architecture suite | PASS — 90 tests; 0 failures, errors or skips |
| Publication, navigation and rejection tests | PASS — 57 tests; original publication tests plus navigation/preservation and report-evidence rejection tests |
| Root script and identity regression tests | PASS — 65 tests; identity lifecycle, provenance, binding continuity, release governance and RAD capture |
| RAD validation | PASS — 0 invalid; 2,467 unresolved dimensions/relationships retained |
| Registry, historical adoption and current bindings | PASS — Exactly 14 retained EC identities; no register/alias collision; frozen historical proof and current source/semantic continuity pass |
| Generation, offline XSD and read-only regeneration | PASS — Byte-identical repeated generation and idempotence; 228 elements / 189 relations / 219 diagrams; XSD and source fingerprints agree |
| Baseline, historical evidence, source assertions and Maven | PASS — Frozen hashes match accepted 1.0.3 commit; 127 prior inventory sources unchanged; all 50 POMs unchanged; 4 superseded README sections preserved verbatim; assertion/collision digests unchanged; git diff --check clean |
| Local Markdown links | PASS — 83 local file links resolve across 7 changed/added Markdown documents |
| Offline Archi 5.9 Model / View / Diagram XSD validation | PASS |
| Unique semantic objects, canonical homes and source-based relation paths | PASS |
| Stable semantic hierarchy, endpoints, EC bindings and governed Documentation | PASS |
| Independent semantics, exact progressive scopes and retained View preservation | PASS |
| Byte-identical repeated generation and idempotent presentation | PASS |

[navigation-release-verification.json](navigation-release-verification.json) pins
completed commands and verified files. The report renderer refuses stale release,
missing required checks, failed checks and stale file fingerprints. Historical
1.0.1/1.0.3 reports and proofs remain unchanged. The source inventory currently
fingerprints 129 RADS documents; its file census
increases for the new convention, while all prior architectural assertion and
collision digests remain unchanged. Commands run from the repository root:

```bash
timeout --signal=TERM --kill-after=10s 300s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
timeout --signal=TERM --kill-after=5s 90s python3 -m unittest discover -s docs/archimate/capability-architecture/tests -v
timeout --signal=TERM --kill-after=5s 90s python3 -m unittest discover -s scripts/tests -v
timeout --signal=TERM --kill-after=5s 60s python3 scripts/rads_business_architecture.py validate
make -C docs/archimate/capability-architecture generate bindings check
```

## Unresolved findings preserved and stop point

- 214 unresolved capability crosswalks remain unallocated and unreconciled.
- F46 remains the Omilia / Shared Security and Assurance Nomenclature navigation-key
  collision. The BC/BEC Health Information Exchange CM-R05 name conflict also remains.
- C/F/D/M/CDG keys remain navigation/register keys. Incomplete Canonical IDs, 705
  unallocated full structural IDs and 137 local Feature tokens retain their standing.
- The 22 unresolved captured relationship assertions and all 189 baseline Composition
  RAD crosswalks remain unresolved; folders add no missing assertion.
- Domain05 candidate standing, B2.1-only completion and later allocation/derivation
  deferrals remain. No additional architecture domain or semantic construct is added.
- All 2,467 unresolved RAD dimensions/relationships remain. Existing incomplete
  ancestry, source conflicts and five natural-view membership gaps are preserved.

No unexpected semantic architecture finding was established. The display failure
above is the outstanding environment limitation. Stop after this navigation slice
for human Archi inspection; further publication, F46 adjudication, Domain03 identity
migration and allocation to the remaining 214 capabilities require separate work.

## Canonical Documentation Assessment

The convention is recorded in RADS under the owner's explicit bounded instruction.
The reviewed Exchange baseline supplies only the already-authorised publication
hierarchy; no legacy architecture is promoted to RADS or used to resolve missing
crosswalks. Superseded presentation sections in the publication README are preserved
unchanged inside explicit wrappers referencing the new governed convention.
