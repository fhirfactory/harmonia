<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Canonical Identity and Governed Naming establishment

Date: 2026-10-18. Active branch: `Harmonia-GovernedAccess`.
Starting commit: `d060702d6055bc42ef4e0b64917703636f466faa`.
Starting release: **1.0.6**. Resulting governed release: **1.0.7**, previous **1.0.6**.
Status: **EMPIRICALLY ACCEPTED — CANONICAL IDENTITY AND GOVERNED NAMING TRANSPORT**.

This completes the existing establishment report with the Solution Architect's
real no-edit Archi 5.10 import/export of the complete Release 1.0.7 model.
The earlier formatting and numeric-name decisions remain unchanged. The Solution
Architect authorised the spelling:

```text
source key    = historical:drv_uptime
displayName   = 24/7 Availability
friendlyName  = TwentyFourSevenAvailability
canonicalName = MOT-TwentyFourSevenAvailability
```

The exact `nameComponent` and decision provenance are persisted on that Motivation
resource. No naming value remains awaiting a decision. Increment 2, architecture
change reconciliation and presentation replay have not been implemented.

## Authority and contract

The single authoritative definition is the
[Canonical Identity and Governed Naming Contract](../../docs/markdown/governance/canonical-identity-and-naming.md).
The mapping, Business metamodel, navigation convention and baseline README reference
it. Earlier structural-ID terminology is explicitly distinguished from UUID32
identity; frozen identity/adoption evidence remains unchanged.

AX-04 supports using existing source capture, Property, publication and validation
machinery. AX-14 preserves architectural identity, names, types, standing,
representation identity and alias roles separately. AX-15 and AX-17 preserve
uncertainty and missing authority: historical crosswalks, Proposed context and
unresolved Feature associations were not inferred or promoted. The explicit task
authorises this metadata establishment; no architectural meaning was changed.

The [release policy](../../docs/markdown/governance/architecture-release-policy.md)
requires a patch increment for this change set. The bounded bootstrap established
metadata for 1.0.7, then `architecture_release.py bump --expect 1.0.6` applied that
single increment. Publication reads the root authority and never increments it.
This empirical acceptance closes that same change set; it does not increment the
release again. No commit is claimed. GUI execution provenance is the Solution
Architect's supplied declaration, tied below to the actual input/export hashes.

## Resource-owned source representation

The existing governed RADS captures own the literal metadata:

| Source population | Resource records | Authority/context |
| --- | ---: | --- |
| `baseline/architecture.json` — `elements` | 946 | Existing reconciled resource records, `parent_key` / `owner_key`, labels and standing |
| Same capture — `historical_reference_elements` | 214 | Already accepted historical Capabilities; explicit native tier/category Properties and Composition ancestry, without adopting a missing RAD crosswalk |
| `baseline/motivation.json` — `elements` | 139 | Existing typed current/historical Motivation resources; local names where no architectural grouping is established |

Each record now owns `canonicalId`, `displayName`, `friendlyName`, `canonicalName`.
The exceptional numeric spelling is resource-local. Existing semantic type fields
supply native `archimateType`; the established Feature specialisation supplies
`harmoniaType=Feature`. No new semantic types were invented.

The historical population has 25 CT1 roots, 85 CT2 and 104 CT3 resources and 189
explicit Composition edges. Every non-root has one recorded parent. Capturing this
accepted historical data into the existing resource source does not make generated
XML the owner of new metadata, establish current RAD equivalence or infer ancestry
from folders. Ordinary publication transports literal source-owned values.

The original semantic capture is checked exactly against frozen evidence after
removing only the new metadata and historical-reference extension. A complete
[source-history archive](../../docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/manifest.json)
contains established resource snapshots and exact prior-release resources/catalogues.
Its manifest is pinned through existing `correspondence.json`. This is complete
resource history and mutation evidence, not a parallel identity registry.

## Allocation and safety

The dedicated command executed once:

```bash
python3 -B docs/archimate/capability-architecture/tools/governed_metadata.py bootstrap --establish-for 1.0.7
```

Mechanism actually used: **Python standard-library `uuid.uuid4().hex`, random UUIDv4**.
**1,299 IDs were allocated**, and all 1,299 values have UUID version 4. They are
literal persisted UUID32 values; no name, tier, type, layer, ancestry or lifecycle
information is encoded. Persistence provides stability, not deterministic allocation.
See [allocation results](2026-10-18-archimate-canonical-identity-establishment/allocation-result.json).

Validation precedes allocation. Existing valid values are preserved; malformed,
duplicate or multiple claims block it. Only absent IDs are allocated. Established
bootstrap reruns refuse before the allocator. Interrupted source-history establishment
blocks fallback/reallocation. Ordinary publication contains no allocation step,
including publication into a subsequent release. Deleting the establishment marker
or the entire published metadata block cannot silently restore the legacy contract.

Pinned complete source history detects changed/deleted IDs and physical deletion of
an established resource. Retained historical, superseded and non-current resources
keep their IDs. No concrete physical deletion requiring a separate identity ledger
was found. Such deletion would require architectural adjudication, not ID recycling.

## Identity, names, aliases and types

| Measurement | Result |
| --- | ---: |
| Governed elements | 1,299 |
| Valid, unique UUID32 Canonical IDs | 1,299 |
| Missing / malformed / duplicate / multiple ID claims | 0 / 0 / 0 / 0 |
| `displayName` persisted and matching expected presentation | 1,299 |
| `friendlyName` persisted and matching deterministic derivation | 1,299 |
| `canonicalName` persisted and matching deterministic derivation | 1,299 |
| Naming derivation conflicts / values awaiting decision | 0 / 0 |
| Native `archimateType` Properties | 1,299 |
| Established `harmoniaType=Feature` Properties | 178 |
| EC aliases / duplicate EC aliases / frozen-binding conflicts | 14 / 0 / 0 |

Counts are observations/regression expectations for this baseline. Generic validators
derive their populations from source/XML and do not hard-code 1,299.

`displayName` uses the current governed label, qualified as `[CTn]` for Capabilities
and `[Feature]` for Features. Other elements retain their established presentation
label. Native semantic Names and diagram labels remain unchanged. Legacy short-name
fields were not mechanically renamed into presentation names.

`friendlyName` has whitespace-free PascalCase components and only genuine semantic
context separated by dots. `canonicalName` uses those same components/context with
the existing formal tier/type vocabulary: `CTn`, `CTn-BEC`, `CTn-EC`, `FEAT`, `FN`,
`SV`, `PR`, or local `MOT`. Punctuation separates words, `&` becomes `And`, case
boundaries are recognised and initialisms follow word casing (`FHIR REST` → `FhirRest`).
Archi navigation folders, View placement and presentation containment supply no
qualification. Persisted qualified names are validated; drift fails without repair.

The actual Person Identity Administration resource therefore has:

```text
displayName   = [CT3] Person Identity Administration
friendlyName  = EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration
canonicalName = CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration
```

Illustrative task examples did not rename it or restore an older root label.
The 946 legacy structural paths retain their 190 Established / 756 Proposed standings.
The other 353 resources lack that *legacy* field; all 353 now have complete new names
from their explicit historical context or governed local Motivation form.

Legacy migration is semantic:

| Legacy field/evidence | Current treatment |
| --- | --- |
| `canonical_id` | Retained original structural-path provenance; corresponds semantically to `canonicalName`, never UUID32 `canonicalId` |
| `canonical_name` | Retained short-label source/provenance; not assumed equal to `displayName` |
| Name aliases | Preserved in the existing naming channel; 46 entries on 44 published owners |
| Historical structural/identifier aliases | Preserved separately; 432 entries on 430 published owners |
| EC-01–EC-14 | Unchanged frozen external/historical identifier aliases and owner bindings; each affected element also owns a new UUID32 |

No old name/path was discarded or silently reclassified. All 719 published legacy
short labels, 34 native/short-label differences and three legacy duplicate-name pairs
remain observable. Across the full historical and reconciled populations, 213 display
values and 209 friendly/canonical paths repeat. These are explicit
[naming observations](2026-10-18-archimate-canonical-identity-establishment/name-collision-observations.json),
not evidence of equivalence. Distinct instances retain distinct Canonical IDs and
standing. The 214 historical crosswalks remain unresolved; artificial qualification
was not added to manufacture uniqueness. No ambiguity about a *new value's derivation*
was concealed to obtain coverage.

Canonical identity remains independent of native and Harmonia type. A later retype
of the same ID is a significant change requiring review, not permission to allocate
a replacement ID. This task implements validation, not reconciliation.

## Publication and preservation

The existing synchroniser performs its validated semantic replay, then an allowlisted
metadata overlay publishes six Property definitions and the applicable resource
values. Existing Property/source/alias/lifecycle machinery, XML parser, bundled
schemas, representation bindings and preservation signatures are reused. Nine
existing Markdown catalogues also show the current resource metadata.

The current publication is
[Harmonia-Architecture-1.0.7.xml](../../docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-1.0.7.xml)
(**20,800,334 bytes**), SHA-256:

```text
def447de2f522737c8b9c30dd9850a57bfec71eaa01f0820ff49283c40fba4bb
```

The [active diagnostic summary](2026-10-18-archimate-canonical-identity-establishment/established-diagnostic-summary.json)
reports full identity and governed-name conformance. Historical 1.0.6 diagnostics
remain available explicitly and still correctly report zero future IDs.

Exact whole-model signatures prove preservation after removing only the new Property
structures and restoring the old model-release Property. All existing legacy
Properties, semantic Names, Documentation, types, element IDs, relationships,
endpoints, organisation/navigation structures and presentation remain unchanged:

| Preserved collection | Before → after |
| --- | ---: |
| Elements / representation IDs | 1,299 → 1,299 |
| Relationships / IDs, types and endpoints | 852 → 852 |
| Views / representation IDs | 1,196 → 1,196 |
| Visual objects / identities | 3,915 → 3,915 |
| Visual connections / identities | 1,449 → 1,449 |

Geometry, labels, styles, routing, nesting and order are preserved. Relationships,
Views, nodes and connections receive no Canonical IDs. No concrete relationship-ID
limitation requiring adjudication was exposed.

The accepted 1.0.6 XML remains byte-identical at
`d57a5adeeef6b0906b30a02c3ea80cc6ff6308988775e7e3a619db01564ca52a`.
Its verification, reports and guides remain unchanged historical evidence. All 13
prior-release resource/catalogue/release files in the archive match their initial
bytes. The frozen EC register remains
`aadfaa9c1c6d9d35908036683fff4837537de9fe0683c192e2fd0959fced6073`.
Protected recovery XML remains
`f85be36ff6edb3c6491dd526c42d4207db9214154da437941b709de34dc106a1`.

The [preservation audit](2026-10-18-archimate-canonical-identity-establishment/final-preservation.json)
checks 219 initial pinned files: 197 unchanged, 22 intentional task changes, zero
unexpected changes. Protected recovery and Increment 1 evidence remain intact.
Existing index/staged work was preserved.

## Tests and complete validation

All executions used bounded waits. None timed out or stalled. Current evidence is
pinned in [canonical-identity-verification.json](../../docs/archimate/capability-architecture/synchronised-baseline/canonical-identity-verification.json).

| Verification | Result |
| --- | --- |
| Full architecture-publication/recovery/round-trip unittest discovery, 180 s bound | **PASS**, 150 tests, 119.116 s, zero failures/errors/skips |
| Final focused identity/naming tests after subsequent-release guard improvement, 90 s bound | **PASS**, 22 tests, 35.902 s |
| Final Increment 1 diagnostic/comparator regressions, 45 s bound | **PASS**, 27 tests, 6.405 s |
| Existing identity/release script tests, 60 s bound | **PASS**, 25 tests, 29.548 s |
| Required offline Maven architecture suite on JDK 21, 180 s bound | **PASS**, 90 tests, zero failures/errors/skips; BUILD SUCCESS, 30.688 s |
| Frozen EC registry validator and current release validator | **PASS**; 14 retained EC aliases; release 1.0.7 / previous 1.0.6 |
| Source UUID/name validation and active Increment 1 diagnostic | **PASS**, full computed element coverage and no conflicts |
| Publication XSD, references, existing architecture/navigation/presentation checks | **PASS** |
| Protected recovery `--check` | **PASS**, exact historical replay |
| Ordinary and independent core regeneration | **PASS**, all 12 outputs byte-identical; source IDs/names/history/release unchanged |
| Complete publication + current report/guide independent regeneration | **PASS**, all 14 outputs byte for byte in independent processes |
| Actual Archi 5.10 no-edit export/comparison | **PASS for Canonical Identity and Governed Naming Transport**; complete-model evidence below; raw whole-document result remains **DIFFERENCES** |

The focused tests reject missing, malformed, duplicate, changed/deleted and multiple
IDs, duplicate JSON claims, EC substitution, encoded type prefixes, physical resource
deletion, friendly/canonical/display drift, multiple/lost Property claims, lost
establishment markers and deletion of the entire metadata block. Positive tests
prove one source-owned ID per element, exact numeric-name decision, allocation only
where absent, preservation of valid IDs, bootstrap rerun refusal, zero ordinary
allocation, later-release transport without allocation and whole-model preservation.
The 22-test focused run adds one subsequent-release regression to the earlier full
suite; test-run counts overlap and are not summed as distinct tests.

Initial finalisation runs positively exposed the metamodel notice's source-era
fingerprint/line-locator drift: 149 publication tests had one error; 25 identity/release
tests had two errors. The existing exact-insertion source-presentation overlay now
proves removal of only that notice in memory and recovers the original metamodel
hash and locator. Frozen capture/line evidence and all 2,467 unresolved findings
remain unchanged. Subsequent full and script suites pass. The initial JDK 25 Maven
run passed but emitted unsupported-JDK-class import warnings; the JDK 21 rerun above
removes that environment limitation. Earlier historical-report hash-guard failure
is resolved through separate current-release report/verification files, leaving the
old verification untouched. Initial failures/logs remain available as evidence.

## Real Archi proof and Increment 2 gate

The Solution Architect supplied the real complete-model Open Exchange export after
importing the governed Release 1.0.7 publication into **Archi 5.10** and making no
intentional architectural edits. This is the incoming model, not a self-copy or
synthetic fixture. The [proof manifest](2026-10-18-archimate-canonical-identity-establishment/archi-proof-manifest.json)
records that declaration and pins both files. The declaration establishes operator
provenance; the comparison does not independently authenticate GUI execution or
the exact patch version. The preparation handoff requested 5.10.0 and English;
the supplied task confirms 5.10, complete-model export and no intentional edits.
No additional patch-version/locale attestation is invented.

The original export bytes are retained at:

```text
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-exports/release-1.0.7.xml
```

Published input: **20,800,334 bytes**, SHA-256
`def447de2f522737c8b9c30dd9850a57bfec71eaa01f0820ff49283c40fba4bb`.
Real export: **21,835,649 bytes**, SHA-256
`38e49fa348d015aba6b19e21b4f96dc00fddf3c4d7671b3e352b4c07748917ea`.

The existing Increment 1 command was executed unchanged:

```bash
timeout --signal=TERM --kill-after=5s 60s python3 -B docs/archimate/capability-architecture/tools/diagnose_roundtrip.py compare \
  --published docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-1.0.7.xml \
  --incoming .junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-exports/release-1.0.7.xml \
  --output .junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-fidelity.json
```

The [raw comparison](2026-10-18-archimate-canonical-identity-establishment/archi-fidelity.json)
returns **DIFFERENCES**, exit **2**, with zero XSD, identifier or reference issues
on either input. Exit 2 preserves observed whole-document transport findings; it
is not a failed identity/naming gate. No comparison normalisation was added.
The existing `diagnose --model <real-export> --previous <published-1.0.7>
--baseline-context` [diagnostic](2026-10-18-archimate-canonical-identity-establishment/roundtrip-diagnostic.json)
also reports **CONFORMANT_IN_SNAPSHOT** identity and
**CONFORMANT_WITH_GOVERNED_DERIVATION** naming, with zero continuity/source-binding
or naming/type conflicts.

**CANONICAL ID ROUND-TRIP: PASS**

| Measurement | Count |
| --- | ---: |
| Expected semantic elements | 1,299 |
| Published `canonicalId` | 1,299 |
| Exported `canonicalId` | 1,299 |
| Exactly retained on the same element | 1,299 |
| Missing | 0 |
| Changed | 0 |
| Duplicated | 0 |
| Wrong-owner association / moved IDs | 0 |
| Malformed / multiple claims | 0 / 0 |

**displayName ROUND-TRIP: PASS**

**friendlyName ROUND-TRIP: PASS**

**canonicalName ROUND-TRIP: PASS**

| Property | Published | Exported | Exactly retained | Changed | Missing |
| --- | ---: | ---: | ---: | ---: | ---: |
| `displayName` | 1,299 | 1,299 | 1,299 | 0 | 0 |
| `friendlyName` | 1,299 | 1,299 | 1,299 | 0 | 0 |
| `canonicalName` | 1,299 | 1,299 | 1,299 | 0 | 0 |
| `archimateType` | 1,299 | 1,299 | 1,299 | 0 | 0 |
| `harmoniaType`, where present | 178 | 178 | 178 | 0 | 0 |

Exact retention includes value characters, value-language attributes, multiplicity
and owner association. No claims appeared on additional owners. All native
ArchiMate types, semantic Names and Documentation survive separately. Every one
of the **1,299 element representation IDs** and **852 relationship IDs** survives;
all relationship source/target endpoints, types and non-Property content are exact.
Element count remains **1,299**, relationship count **852**. All **14 EC identifier
bindings** retain their registered elements and those elements' Canonical IDs.
The [empirical acceptance evidence](2026-10-18-archimate-canonical-identity-establishment/empirical-acceptance.json)
contains the counts and all 14 bindings; its companion
[read-only analytical probe](2026-10-18-archimate-canonical-identity-establishment/analyse-archi-export.py)
reuses the existing parser, Exchange index, signatures and navigation checks.

Presentation and navigation were compared across the complete model:

| Collection or attribute | Real round-trip result |
| --- | --- |
| Views / View IDs, attributes and names | 1,196 / 1,196 retained exactly |
| Visual objects / node IDs | 3,915 / 3,915 retained in the same Views |
| Visual connections / IDs | 1,449 / 1,449 retained in the same Views |
| Node geometry, parent/nesting, semantic references, node order | Exact |
| Visual connection endpoints, semantic references, styles and all 2,922 ordered bendpoints | Exact |
| Node styles | Known 3,910 absent-alpha → `100` materialisations; no other style change |
| View Documentation | Known 34 losses of exactly one trailing LF |
| Connection sibling order | Known 15 Views reordered by source-node traversal; visual paint-order equivalence remains unproved |
| Organisation/folder/reference items | 7,096 → 7,096; all 7,096 organisation item IDs lost, with known ordering changes |
| Folder paths / reference memberships / empty folders | Exact multisets: 3,749 / 3,347 / 636; existing `navigation_homes` results equal |

The raw classifications remain **1,669 SEMANTIC DIFFERENCE**, **4,269 INFORMATION
LOSS**, **4,688 UNEXPLAINED DIFFERENCE**, **65 SEMANTICALLY EQUIVALENT NORMALISATION**
and **0 PRESENTATION-PRESERVING TRANSFORMATION**. Report-level explanation does
not alter these classifications or grant additional equivalence.

Every one of the **10,568 non-Property-definition comparison events** is exactly
identical to the accepted Increment 1 Release 1.0.6 events, including paths,
before/after data, classifications and reasons. This retains the same 1,669 embedded
evidence/definition Property whitespace changes, the same organisation positional
effects and the same presentation findings. Property definitions increase **55 → 61**
because the six governed metadata keys are now present. All **61** definition keys
and datatypes survive, all **61** definition IDs rekey and all **61** definition-name
`xml:lang="en"` attributes are lost, exactly the previously characterised behaviour.
Thus the six additional loss events and six additional equivalent events are
explained by those new definitions. **No behaviour unexplained by Increment 1 was
observed.** Raw findings and historical evidence were not rewritten.

Post-export validation was run after incorporating the empirical result. The
[closure validation](2026-10-18-archimate-canonical-identity-establishment/empirical-validation.json)
pins the commands, results, logs and preservation hashes. These are additional
executions, not replacements for the establishment-time results above.

| Post-export verification | Result |
| --- | --- |
| Increment 1 round-trip regressions, 90 s bound | **PASS**, 27 tests, 7.071 s |
| Governed identity/naming regressions, 120 s bound | **PASS**, 22 tests, 41.595 s |
| Identity/release script regressions, 120 s bound | **PASS**, 25 tests, 30.458 s |
| Required offline Maven architecture suite, JDK 21, 180 s bound | **PASS**, 90 tests, zero failures/errors/skips; BUILD SUCCESS, 28.749 s |
| Source identity/naming, frozen EC registry and release validation | **PASS**, 1,299 valid IDs, 14 EC bindings, release 1.0.7 / previous 1.0.6 |
| Publication `--check`, 90 s bound | **PASS**, deterministic output with the original 1.0.7 hash |
| Independent complete publication/report/guide reproduction, 150 s bound | **PASS**, all 14 generated files byte-identical, 47.234 s |
| Historical Release 1.0.6 XML, report, verification, source archive and Increment 1 evidence | **PASS**, byte-identical to pre-comparison hashes |
| Governed Release 1.0.7 source, publication and raw incoming export | **PASS**, byte-identical to pre-comparison hashes |
| Comparator, tests, publication implementation and release authority | **PASS**, unchanged; no new equivalence rules |

No command timed out or stalled. No architecture was repaired or rewritten.

**Overall acceptance:** Release 1.0.7 has empirically demonstrated that Harmonia
Canonical Identity and Governed Naming metadata survive a real Archi 5.10 no-edit
round trip across the complete governed architecture. **Release 1.0.7 is empirically
accepted for Canonical Identity and Governed Naming round-trip transport.** This
does not establish whole-document lossless fidelity; the documented evidence-text,
language, organisation-identity, newline and ordering limitations remain.

Earlier generated preparation-time report/guide artefacts retain their original
pending wording as historical establishment evidence. This section and the
hash-pinned real-export evidence supersede that pending empirical gate only; the
current verification manifest links the completed proof. Its two changed
evidence-document hashes were refreshed without altering earlier check results.
Existing historical crosswalks, naming observations and unresolved standing or
association findings remain. **Increment 2 has not begun and is not authorised by
this acceptance. Work stops at this gate.**

## Files changed

The [complete task file list](2026-10-18-archimate-canonical-identity-establishment/establishment-files.json)
separates this task from separately staged Increment 1 work. Principal changes:

- `architecture-release.json` — policy-defined 1.0.7 transition.
- `docs/markdown/governance/canonical-identity-and-naming.md` — single contract;
  references in mapping, navigation convention, Business metamodel and baseline README.
- `docs/markdown/governance/baseline/architecture.json`, `motivation.json`,
  `correspondence.json`, `source-presentation-overlays.json` — resource metadata,
  complete source history pin and exact historical notice handling.
- Nine existing baseline Markdown catalogues — current metadata columns/record rows.
- `tools/governed_metadata.py`, `synchronise_baseline.py`, `diagnose_roundtrip.py`,
  `report_baseline.py` — bounded bootstrap, validation, transport, diagnostics and
  separate current-release report evidence.
- `tests/test_governed_metadata.py`, `test_baseline.py`, `test_roundtrip.py` — focused
  identity/name guards and active/historical publication regression coverage.
- `synchronised-baseline/identity-establishment-1.0.7/` — 16 complete source-history
  and prior-release evidence files; new 1.0.7 XML, validation, verification, report
  and Archi guide alongside preserved 1.0.6 artefacts.
- This existing report and its linked establishment evidence directory — retained
  preflight evidence, current diagnostics, logs, preservation/regeneration results
  and completed real-Archi acceptance evidence. Architectural reviewer terminology is
  **Solution Architect**; original historical/evidentiary attribution remains intact.

This empirical closure changes this report, the existing 1.0.7 proof manifest,
the current verification manifest's proof links/two evidence hashes, and an
appended 1.0.7 reference in the Increment 1 operating procedure. The rerun raw
`archi-fidelity.json` is byte-identical to the file present at task entry. Closure
adds the real-export diagnostic, analytical evidence/probe, bounded-validation
results and logs. All earlier Release 1.0.6 evidence, source metadata, publication
XML and comparison/publication implementations are unchanged.
