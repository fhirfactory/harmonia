<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Diagnostic Report — Archi workbench organisation of the published 1.1.11 projection

Report-Produced-At: 2026-10-23T04:44:54.343048Z

Production event: the system UTC clock recorded emission of this completed Report
into its formal evidence file, using Python `datetime.now(timezone.utc)` at the
write boundary. This is Report production, not Task issuance, validation, human
review, architectural acceptance or an internal model-generation time.

Executor: Codex, under Mark Hunter's diagnostic instruction and continuation.
Corresponding [formal Task](../../before/ai-agent-tasks/unresolved-archi-workbench-organisation-diagnostic.md)
retains both operative instructions. Their instruction-entry event times and
required filename prefixes are **UNRESOLVED**, because attributable system
instruction-entry timestamps were not exposed. They are not inferred from the
current clock, file metadata, Git or the later continuation. Solution Architect
treatment remains required before any eventual closure; this Report does not
self-resolve the temporal provenance.

## Outcome and standing

**The reported defects are reproducible consequences of how the current
projector's organisation items interact with Archi 5.10's importer. They are
workbench presentation defects, not evidence of canonical architecture corruption.**
The diagnostic identifies three distinct mechanics:

1. An Exchange item with children cannot also place its referenced element in
   Archi. Archi treats it as a folder, ignores that item's element reference and
   leaves the element in the native root where it was initially created.
2. A leaf reference is placed beneath the element's **native type's default
   layer**, with its Exchange ancestor labels rebuilt as folders there. A logical
   navigation domain does not override native layer placement.
3. An unreferenced navigation item needs a recognised native-root anchor or
   a descendant object reference to materialise. Source-only and reference-only
   branches in the current numbered organisation can therefore disappear.

All **218** canonical navigation parents remain at native layer roots: **204
Capabilities** (29 CT1, 100 CT2, 75 CT3), **11 DataObjects specialised as Topics**,
and **3 Drivers**. The other **1,384** elements have nested placements. No direct
root element is unexplained by the parent-item rule. Of those 218 parent contexts,
215 also survive as folders populated by descendants; the 3 Driver contexts have
only reference-only children and do not survive as folders.

The surprising `Strategy / III Business` branch is caused by **four native
ValueStream elements**, not by Capability-context references. The Strategy `R`
branch contains **214 distinct historical Capability identities**. Motivation has
the other `R` branch, containing **10 Stakeholders**. These are layer-specific
reconstructions of the corresponding logical paths, not duplicated canonical
objects. All 1,602 elements occur once in both source and returned organisation.

The **1,209 authored Views and all 1,032 View-folder containers, including the
outer Views root, preserve their original paths**. Their hierarchy has a separate
origin and does not exhibit the parent-element defect. No View remediation is
evidenced by this diagnostic.

**Standing remains unchanged.** Accepted 1.1.11 remains the architecture baseline;
closed 1.1.12 remains accepted publication/tooling history. This is prospective
**1.1.13 / 001 diagnostic evidence**, with alternatives for Solution Architect
consideration. No HCRL is prepared or completed, no acceptance is asserted, and
root `architecture-release.json` remains 1.1.12 / predecessor 1.1.11. A prospective
evidence directory is not a release allocation. No accepted evidence, canonical
element, identity, name, standing, relationship, View, navigation, RAD, Manifest,
projector or published XML is modified.

## Authority, inputs and method

Entry state was clean Git commit `ae94a7ed067c436da49443d72d2eb72ad30fc5ff`.
The exact subject was
`enterprise-architecture/design/archimate/published/1.1.11/Harmonia-Architecture.xml`,
SHA-256 **`e33ae52b3934cf4d9b9b0102ce65e09461fa554022f43d56f2637685d8c39caa`**.
It contains 1,602 elements, 1,651 relationships and 1,209 Views. The maintained
presentation carrier is `design/archimate/projection-template.xml`; current
navigation is `continuum/navigation.json`, schema version 2.

Repository authority was loaded from Architecture Governance, Architecture Review
Praxis, Architecture Change Provenance, release policy, Continuum Representation,
Incorporation Praxis and the relevant round-trip/navigation provisions. Historical
[1.1.5-002](../../../../../review/1.1.5/hcrl-1.1.5-002.pending.md) is interpreted
through the prospective [1.1.7 acceptance](../../../../../register/1.1.7/hcrl-1.1.7-001.complete.md),
and the completed [1.1.10](../../../../../register/1.1.10/hcrl-1.1.10-001.complete.md),
[1.1.11](../../../../../register/1.1.11/hcrl-1.1.11-002.complete.md) and
[1.1.12](../../../../../register/1.1.12/hcrl-1.1.12-001.complete.md) records retain
their exact standing. No legacy implementation is promoted to architectural authority.

Relevant canonical axioms are AX-04 (engines provide machinery without redefining
Harmonia meaning), AX-08/09 (purpose-selected durable evidence), AX-14 (navigation,
identity, semantics, transport and acceptance remain distinct), AX-15 (uncertain
outcomes remain explicit) and AX-17 (no invented architecture or acceptance).
Separating a folder from its element is consistent with those axioms: both are
representations of an already established location; neither creates containment,
a second architecture element or a relationship. The alternatives below require
adjudication and are not adopted by this Report.

Inspection covered the complete saved native model and returned organisation,
not just the supplied observations. Bounded scripts inventory every element,
folder and View path, compare them to the published XML and template, and retain
selected importer bytecode. Actual Archi **5.10.0** was run through its standard
command-line application with isolated `/tmp` configuration/workspace directories:
import the exact publication, save a native `.archimate` model, export with
`--xmlexchange.exportFolders --xmlexchange.exportLang en`, then reimport that
no-edit export into another isolated workspace. Synthetic fixtures exercise the
same importer independently. This is programmatic inspection of the actual saved
workbench tree. No new interactive Solution Architect visual inspection or human
usability acceptance is asserted.

The initial sandbox launch failed before import at GTK initialisation (exit 13).
A display-enabled, automatically approved retry succeeded. Every subsequent Archi
fixture and second-pass run exited 0 and validated its Exchange input/output.
There was no timeout or stalled outcome.

## What the projector emits

`tooling/architecture_continuum.py:327` loads the template, accepted canonical
content and shared resolved navigation. Native element Name remains displayName.
The shared traversal derives numbers, paths and typed references, which are also
carried as the three `Continuum / Navigation ...` element properties.

`tooling/continuum_navigation.py:263` replaces architecture organisation while
retaining the template's View-bearing organisation. Its `add` function emits
**one item for every navigation node**, with:

- `identifier="navigation-<coordinate with dots replaced by hyphens>"`;
- a numbered/qualified label from the shared traversal;
- documentation identifying navigation, occurrence, reference role, source and
  any existing Association IDs;
- `identifierRef=<existing Exchange element ID>` when the node has `canonicalId`;
- recursively nested child items, even when this same item has `identifierRef`.

A contextual node has `contextCanonicalId`, not `canonicalId`. It gets no
`identifierRef`: its canonical target appears in documentation and derived
navigation properties. Thus Business Capability contexts do not request a second
Capability model-tree location. Stakeholder contexts use the same distinction.
An index/historical canonical node *does* have `canonicalId`, so it has a live
reference at its one canonical index/historical navigation location.

For example, the published Care item is structurally equivalent to this bounded
extract (additional children and documentation omitted):

```xml
<item identifier="navigation-IV-1-1-1"
      identifierRef="id-395f82a0d8664955b6f542d80de3f5b5">
  <label xml:lang="en">IV.1.1.1 [Topic] Care</label>
  <item identifier="navigation-IV-1-1-1-1"
        identifierRef="id-02f975c032b549d4b7e951d0a427533a">
    <label xml:lang="en">IV.1.1.1.1 [Topic] CareLocation</label>
  </item>
  <!-- other Care child items omitted -->
</item>
```

The parent reference is present. The defect is **not** that canonical Care is
missing from Exchange organisation, or that the projector explicitly emits a
second root-level Care reference. The projector emits no separate leaf placement
for Care, and Archi declines to use the nonleaf parent reference for placement.
Every canonical element has exactly one organisation reference in the publication.

The bundled `archimate3_Model.xsd:664` permits optional `identifierRef` alongside
nested items; structural containers may have no reference. Consequently this
combined representation is valid Open Exchange. The narrower importer behaviour
is not an XSD validity requirement. The schema does not require an architectural
relationship to nest a folder or place an element in one.

## What Archi actually does

The installed plugin is
`org.opengroup.archimate.xmlexchange_5.10.0.202607081558.jar`; its hash and execution
correspondence are retained in [execution.json](../diagnostics/execution.json).
The bounded [importer-method extract](../diagnostics/archi-importer-organisation.javap.txt)
was obtained with `javap -p -c`, and the actual fixture outputs confirm its effect.

The importer first creates each native element and adds it to
`getDefaultFolderForObject(element)`. Organisation parsing then works as follows:

| Importer method | Established behaviour | Workbench consequence |
| --- | --- | --- |
| `getItemObject` | Returns no object if `identifierRef` is absent **or if the item has any child items**. Only leaf references resolve to existing concepts/diagrams. | A canonical parent item acts as a folder, not as the element it references. |
| `addObjectItemToFolder` | For a resolved leaf, obtains the native default folder for that object, rebuilds its parent-item hierarchy below it and adds/moves that same object there. | The native type controls the outer layer. Ancestor navigation labels can reappear in a different native layer. |
| `getSubFolder` / `createSubFolder` | Reconstructs ancestor labels, reuses a same-named child folder, and otherwise creates a folder. Incoming organisation IDs are not used as folder identity. | Labels/path determine physical folders; generated navigation IDs do not pin their identity. Same-named siblings can merge; no such source-path collision is evidenced here. |
| `getTopLevelArchiFolder` | Recognises an organisation item only when it is top-level and its label exactly equals a native root name. | `Views` is a native marker; `II Strategy`, `I Motivation`, `V Technology` and `R ...` are not. |
| `parseItem` fallback | If an item did not place an object, attempts folder creation using a recognised native root in its ancestry. Descendant object references can also later create ancestor folders. | Unanchored documentation-only leaves/branches disappear. An explicit native marker can preserve them without model objects. |

Archi never makes a Capability or Topic object a folder. In the saved native
model, folder nodes and architecture element nodes are different physical kinds.
Archi can show a folder and its actual element together, with the element beneath
the folder alongside descendant navigation. No additional semantic relationship
is necessary.

A live reference from a second organisation location is not a persistent alias.
The repeated-reference fixture resolves the *same* object and moves it to the
last processed location. The first folder remains empty. This is not an extra
canonical instance, and should not be used to implement contextual references.
The published Harmonia organisation contains no such repeated live references.

The import/save/export normalisation adds native outer containers, turns
referenced parent items into unreferenced folders where descendants materialise
them, emits the canonical elements at their actual native locations, and replaces
organisation identifiers. Leaf organisation labels are not alternate element
Names: the native model displays the existing element Name. Numbering, occurrence
role and standing qualifiers written only into a leaf item's label therefore do
not appear in that model-tree element label. They remain in the canonical content
and applicable properties/documentation. Exact native folder IDs and raw XML
bytes are not preserved; canonical element and View identities are preserved.

The full no-edit return has SHA-256
`a61375509cfbd4c69dd8cee112ce72880c07122e9eec4dcc0c3dd9af1b60a8e6`, byte-identical
to the existing [1.1.12 retained control](../../../../1.1.12/001/after/archi-transport/returned-model.xml.gz)
after decompression. That existing copy is referenced rather than duplicated.
The actual new [native save](../diagnostics/published-imported.archimate.gz) is
retained. All object paths and folder paths are stable on the second pass,
including the already hybrid/disordered paths. Stability does not imply usefulness.

## Complete workbench inspection

Counts below are actual canonical object counts in the saved native model.
[summary.json](../diagnostics/summary.json), the complete compressed
[element inventory](../diagnostics/elements.json.gz) and
[folder inventory](../diagnostics/folders.json.gz) support the classifications.

| Significant domain/branch | Actual inspection | Classification and cause |
| --- | --- | --- |
| Motivation / `I Motivation` | 171 canonical Motivation objects logically belong here; 168 are beneath the numbered hierarchy and 3 Drivers remain at the native root. Eight populated groups remain: Drivers, Strategic Goals, Business Outcomes, Assessments, Requirements, Constraints, Architecture Axioms and Unclassified Motivation. | Generally coherent type/subject groups; partial separation in Drivers. AX-17 and the other Principle/Requirement/Constraint leaves import normally. |
| Motivation / three Driver contexts | 24/7 Availability, Audit & PHI Privacy and Clinical Interoperability have Stakeholder-reference child navigation. The 3 canonical Driver objects remain directly under Motivation. Their numbered parent labels, 3 reference groups and 4 Stakeholder-context labels do not materialise. | Parent/reference distinction plus unanchored reference-only omission. This is the same parent rule as Topics/Capabilities, with no descendant live reference to reconstruct the folder. |
| Motivation / `R.1 Stakeholder Index` | 10 canonical Stakeholders occupy `Motivation / R Cross-Cutting References — Reference navigation / R.1 Stakeholder Index — Reference navigation`. | Native-layer split of the logical cross-cutting index; no duplicated Stakeholder objects. Context labels under Drivers are not live references. |
| Strategy / `II.1 Capabilities` | 628 canonical Capabilities logically belong in Strategy, with 424 leaf objects in the hierarchy and 204 canonical parents separately at native Strategy root. Folder ancestry for the 204 parents survives around the leaf descendants. The three collections are Enterprise, Business and Business Enabling Capabilities. | Hybrid and partially flattened **object population**, not a flat accepted navigation tree. CT1/CT2/CT3 parent objects are separated from their existing navigation contexts. |
| Strategy / `II.2 Courses of Action`, `II.3 Resources` | 7 CourseOfAction objects, including the governed guideline context, and 3 Resources have nested locations. | Coherent populated navigation; native layer agrees with logical domain. |
| Strategy / `III Business / III.2 Products and Value Streams / III.2.2 Value Streams` | Exactly 4 ValueStreams; there are no Capability objects under this Strategy/Business branch. | Native-layer organisation effect. The logical Business path is rebuilt under native Strategy because ValueStream is a native Strategy type. The supplied Capability-reference hypothesis is disproved for this branch. |
| Strategy / `R.2 Historical Capability Identities` | 214 canonical historical/predecessor Capability objects are leaf references beneath the Strategy-side R index. | Native-layer split/reference-index effect; same displayNames can resemble duplicates of current elements, but IDs are distinct and standing is retained. Leaf label qualifiers such as “Historical identity [superseded]” are not native element Names. |
| Business / `III.1 Business Information Architecture` | 13 BusinessObjects. | Coherent nested leaf placement. |
| Business / `III.2 Products and Value Streams` | 4 Products remain in Business; its 4 ValueStreams occur under Strategy as described above. | Split logical branch across native layers; no semantic relocation or object duplication. |
| Business / `III.3 Organisation Architecture` | 34 Roles and 7 BusinessCollaborations in their two subject groups. | Coherent native Business hierarchy. |
| Business / `III.4 Business Behaviour` | All 89 Capability-context folders survive, including 7 CT1 contexts and their represented ancestry; 142 BusinessServices, 160 BusinessFunctions and 21 BusinessProcesses are nested beneath the contexts and behaviour groups. | Coherent/useful contextual hierarchy. Folders have no Capability identifierRef, and their 323 behaviour leaves materialise the ancestor folders in native Business. The actual Capability remains solely at its canonical Strategy placement. |
| Business / `III.5 Unclassified Business` | 69 BusinessInteractions in the explicitly unclassified context. No Business canonical object is loose at the native root. | Preserved architectural uncertainty, not importer flattening or authority to classify them. |
| Application / `IV.1 Information Architecture` | 70 Topics: 59 leaves nested under 11 Topic parent folders, with the 11 actual parent Topic objects at native Application root. The separate Reusable Information Concepts branch has 1 DataObject. | Hybrid Topic hierarchy from parent/reference distinction; reusable leaf placement is coherent. |
| Application / all 11 separated parents | Care, Coordination, Enterprise, Equipment, Individual, Ordering, Place, Scheduling, Service, TaskCompletion and Workforce. All have children in source, one live source ref apiece, and no own leaf ref. | Fully reproduces the supplied list. All parent canonical IDs/native types are unchanged. |
| Application / `IV.2 System-of-Systems Architecture` | 3 canonical objects: one in System Responsibility Map and two in Middleware Architecture. Source-only `IV.2.1 System Map` is absent from the imported folders. | Populated branches coherent; source-only reference omission for System Map. Its absence does not establish missing canonical model objects. |
| Application / `IV.3 Applications` | 41 objects remain within the 15 named application subject branches and their existing subdivisions. | Coherent nested placement; no further root-level separation. |
| Technology | Native `Technology & Physical` exists and is empty. Numbered `V Technology` does not materialise. | Native empty outer category plus omitted unpopulated navigation; there are zero canonical Technology/Physical objects to organise. No fabricated population is justified. |
| Implementation & Migration | Native root exists and is empty; numbered Part VI does not materialise. | Same native empty category effect; zero represented canonical objects. |
| Cross-Cutting References as a whole | One logical R area becomes Strategy/R.2 and Motivation/R.1. No independent global R root is created. | Projection/reference split across native roots, not wholesale relocation of architectural domains and not repeated canonical instances. A coherent global cross-cutting physical tree is not supplied by this importer. |
| Governance / Part G | All 28 source-only Governance navigation nodes are absent from the saved model tree. | Unanchored source-only omission. Governance prose does not become an ArchiMate element merely to make a folder persist. |
| Other and Relations | Other is empty; Relations contains all 1,651 semantic relationships directly at its native root, without the canonical navigation hierarchy. | Other is a native empty category. Relations is flat because the new organisation emits canonical **element** navigation, while the empty semantic template supplies no relationship organisation references. This is a separate workbench limitation; no relationship is lost and relationship-folder redesign is outside this diagnostic. |
| Views | 1,209 diagrams, 1,032 folder containers, four populated outer branches, no empty terminal View folders, maximum folder depth 8. All native paths match template, published and returned paths exactly. | Coherent/preserved authored presentation hierarchy; independent of element navigation. |

The current native model has 856 Strategy objects, 450 Business objects,
115 Application objects and 181 Motivation objects, totaling 1,602. Its three
empty native terminal categories are Technology & Physical, Implementation &
Migration and Other. There are no unexplained empty terminal folders in the
populated architecture branches. The source-only omission inventory contains
38 navigation-only nodes: 28 G nodes, 7 Motivation reference nodes, System Map,
and the unpopulated V and VI roots. The 3 missing canonical Driver context labels
are additional to those 38; their actual elements survive at root.

## Representative diagnostic correspondence

The table explicitly distinguishes the location of a navigation item from the
location of the actual canonical object. A canonical target ID in a contextual
row identifies the referenced architecture; the context has no independent
canonical ID. A View has a preserved diagram representation ID, not an invented
canonical element ID. Paths show actual numbered Exchange labels and actual native
locations; `/` separates folders. Full population correspondence is retained in
the inventories linked above.

| Architectural domain/example | Expected logical navigation path | Actual published Exchange organisation path/reference | Actual imported Archi path | Canonical element ID / View identity | Native type/layer | Navigation parent? | Separate occurrence? | Established reason | Classification | Candidate representation |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Strategy CT1 | II.1.2.1 Strategy / Capabilities / Business Capabilities / [CT1] Care Access & Coordination | II Strategy / II.1 Capabilities / II.1.2 Business Capabilities / II.1.2.1 [CT1] Care Access & Coordination; `navigation-II-1-2-1` ref=`id-e978dad02c4156938c03ac4b2d34e0c0` | Strategy / [CT1] Care Access & Coordination | ed342cb8656b4f80b55ad519ecbd2d3a | Capability / Strategy | Yes | Yes: canonical object at layer root; labelled context survives as a folder | Nonleaf identifierRef ignored; element retains initial default folder. | Parent/reference distinction | A: folder plus one leaf reference |
| Strategy CT2 | II.1.1.1.1 Strategy / Capabilities / Enterprise Capabilities / [CT1] Coordinated Activity & Interaction / [CT2] Activity & Execution | II Strategy / II.1 Capabilities / II.1.1 Enterprise Capabilities / II.1.1.1 [CT1] Coordinated Activity & Interaction / II.1.1.1.1 [CT2] Activity & Execution; `navigation-II-1-1-1-1` ref=`id-49d4f69ebeeb5ea782a52172fb3e8a7b` | Strategy / [CT2] Activity & Execution | e5ff80e655084524a8dcee8e4ded788a | Capability / Strategy | Yes | Yes: canonical object at layer root; labelled context survives as a folder | Same nonleaf rule through CT1/CT2 hierarchy. | Parent/reference distinction | A: folder plus one leaf reference |
| Strategy CT3 | II.1.3.6.3.2 Strategy / Capabilities / Business Enabling Capabilities / [CT1] Health Service Operations Enablement / [CT2] Care Resource & Capacity Coordination / [CT3] Service Offering Readiness Coordination | II Strategy / II.1 Capabilities / II.1.3 Business Enabling Capabilities / II.1.3.6 [CT1] Health Service Operations Enablement / II.1.3.6.3 [CT2] Care Resource & Capacity Coordination [superseded] / II.1.3.6.3.2 [CT3] Service Offering Readiness Coordination; `navigation-II-1-3-6-3-2` ref=`id-f4b94cd1b3af514eb9e99ddfd82245ba` | Strategy / [CT3] Service Offering Readiness Coordination | 0caecaba5d3e4781b8671b12c13ee283 | Capability / Strategy | Yes | Yes: canonical object at layer root; labelled context survives as a folder | Same rule; not confined to CT1/CT2. | Parent/reference distinction | A: folder plus one leaf reference |
| Business Behaviour contextual CT1 | III.4.1 Business / Business Behaviour / [CT1] Care Administration Enablement | III Business / III.4 Business Behaviour / III.4.1 [CT1] Care Administration Enablement — Capability context; `navigation-III-4-1`; NO identifierRef; documentation references `id-fe7b5e3e02bb53f59ab89c27a81e9e18` | Business / III Business / III.4 Business Behaviour / III.4.1 [CT1] Care Administration Enablement — Capability context (folder) | 28df27d55c514a37995145ab46945f78 (context target) | Capability / Strategy (target); Business folder (occurrence) | Yes, context folder; not an element occurrence | Canonical object is Strategy / [CT1] Care Administration Enablement; intentional identity separation | Folder materialises from Business descendants; no Capability object requested here. | Coherent/useful contextual navigation | Retain folder plus metadata reference; avoid duplicate live identifierRef |
| Business Behaviour contextual CT2 | III.4.1.1 Business / Business Behaviour / [CT1] Care Administration Enablement / [CT2] Care Plan Administration | III Business / III.4 Business Behaviour / III.4.1 [CT1] Care Administration Enablement — Capability context / III.4.1.1 [CT2] Care Plan Administration — Capability context; `navigation-III-4-1-1`; NO identifierRef; documentation references `id-8c082e2d6c605903834291c424722f75` | Business / III Business / III.4 Business Behaviour / III.4.1 [CT1] Care Administration Enablement — Capability context / III.4.1.1 [CT2] Care Plan Administration — Capability context (folder) | cf06eec5699f406fa30a00d2f76e3098 (context target) | Capability / Strategy (target); Business folder (occurrence) | Yes, context folder; not an element occurrence | Canonical object is Strategy / [CT2] Care Plan Administration; intentional identity separation | Folder materialises from Business descendants; no Capability object requested here. | Coherent/useful contextual navigation | Retain folder plus metadata reference; avoid duplicate live identifierRef |
| Business Service | III.4.1.1.3.1 Business / Business Behaviour / [CT1] Care Administration Enablement / [CT2] Care Plan Administration / Business Services / Care Plan Change Notification | III Business / III.4 Business Behaviour / III.4.1 [CT1] Care Administration Enablement — Capability context / III.4.1.1 [CT2] Care Plan Administration — Capability context / III.4.1.1.3 Business Services / III.4.1.1.3.1 Care Plan Change Notification; `navigation-III-4-1-1-3-1` ref=`id-6a7c9e0a85145a73bbb7190f033214ae` | Business / III Business / III.4 Business Behaviour / III.4.1 [CT1] Care Administration Enablement — Capability context / III.4.1.1 [CT2] Care Plan Administration — Capability context / III.4.1.1.3 Business Services / Care Plan Change Notification | 08a8309372734287b2c22a06b95080a4 | BusinessService / Business | No | No second canonical object | Leaf reference moved into Business context folders. | Coherent leaf | Retain |
| Application parent Topic | IV.1.1.1 Application / Information Architecture / Topics / [Topic] Care | IV Application / IV.1 Information Architecture / IV.1.1 Topics / IV.1.1.1 [Topic] Care; `navigation-IV-1-1-1` ref=`id-395f82a0d8664955b6f542d80de3f5b5` | Application / [Topic] Care | f702d357183847dd8f0fd09545679ffd | DataObject (Topic) / Application | Yes | Yes: canonical object at layer root; labelled context survives as a folder | Nonleaf identifierRef ignored; no second leaf ref for the parent. | Parent/reference distinction | A: Care folder with canonical Care leaf and descendant navigation |
| Application leaf Topic | IV.1.1.1.1 Application / Information Architecture / Topics / [Topic] Care / [Topic] CareLocation | IV Application / IV.1 Information Architecture / IV.1.1 Topics / IV.1.1.1 [Topic] Care / IV.1.1.1.1 [Topic] CareLocation; `navigation-IV-1-1-1-1` ref=`id-02f975c032b549d4b7e951d0a427533a` | Application / IV Application / IV.1 Information Architecture / IV.1.1 Topics / IV.1.1.1 [Topic] Care / [Topic] CareLocation | cfc241a7b6904bd19ccc1b9adbac0393 | DataObject (Topic) / Application | No | No second canonical object | Leaf reference accepted; ancestor Care item supplies a folder. | Coherent leaf | Retain leaf placement |
| Motivation Principle | I.7.17 Motivation / Architecture Axioms / AX-17 Architectural Authority and Explicit Uncertainty | I Motivation / I.7 Architecture Axioms / I.7.17 AX-17 Architectural Authority and Explicit Uncertainty; `navigation-I-7-17` ref=`id-b46235d7d9875eb893745a5f2c8a0b72` | Motivation / I Motivation / I.7 Architecture Axioms / AX-17 Architectural Authority and Explicit Uncertainty | 8882573df7ce43c6bc0f66da0a676139 | Principle / Motivation | No | No second canonical object | Leaf reference accepted. | Coherent leaf | Retain |
| Motivation Driver | I.1.1 Motivation / Drivers / 24/7 Availability | I Motivation / I.1 Drivers / I.1.1 24/7 Availability; `navigation-I-1-1` ref=`id-b415475f474d5b3785b517c5935e72d6` | Motivation / 24/7 Availability | 782f53e2190f4a8a8d0dc0a33cd6fbb3 | Driver / Motivation | Yes | Yes: canonical object at layer root; labelled context is absent | Driver has reference-only children; own nonleaf ref ignored, and no child object materialises that context. | Parent/reference distinction | A for Driver; keep Stakeholder links as metadata or separately adjudicate reference presentation |
| Logical Business / native Strategy | III.2.2.2 Business / Products and Value Streams / Value Streams / Multi-Source Clinical Information → Coherent Clinical Picture | III Business / III.2 Products and Value Streams / III.2.2 Value Streams / III.2.2.2 Multi-Source Clinical Information → Coherent Clinical Picture; `navigation-III-2-2-2` ref=`id-d72c559c8ca551de969eb0f6a639a442` | Strategy / III Business / III.2 Products and Value Streams / III.2.2 Value Streams / Multi-Source Clinical Information → Coherent Clinical Picture | 01f655eae3d8485699449c3dcf4f9b56 | ValueStream / Strategy | No | No second canonical object | ValueStream native default is Strategy; ancestor logical Business folders reconstructed there. No contextual Capability ref is involved. | Native-layer organisation effect | B: explicit native-layer mapping while retaining III coordinate |
| Cross-Cutting historical Capability | R.2.1 Cross-Cutting References / Historical Capability Identities / [CT1] Care Access & Coordination | R Cross-Cutting References — Reference navigation / R.2 Historical Capability Identities — Reference navigation / R.2.1 [CT1] Care Access & Coordination — Historical identity [superseded]; `navigation-R-2-1` ref=`id-f78a7520ea80532b8423fc652b12ff6a` | Strategy / R Cross-Cutting References — Reference navigation / R.2 Historical Capability Identities — Reference navigation / [CT1] Care Access & Coordination | 0dec07b6ddbe48bda93a2295dd3e2d7d | Capability / Strategy | No | No second canonical object | Leaf Capability ref has Strategy default; R path built there. Same-name current CT1 is a different ID (first row). Leaf organisation qualifiers are not object Names. | Native-layer organisation effect | B: explicit historical-reference navigation under Strategy; retain standing |
| Cross-Cutting Stakeholder | R.1.6 Cross-Cutting References / Stakeholder Index / Healthcare Provider | R Cross-Cutting References — Reference navigation / R.1 Stakeholder Index — Reference navigation / R.1.6 Healthcare Provider — Stakeholder index entry [historical_reference]; `navigation-R-1-6` ref=`id-a1c118892a525256a69f93d8488059b8` | Motivation / R Cross-Cutting References — Reference navigation / R.1 Stakeholder Index — Reference navigation / Healthcare Provider | 2018200a493d458096b72031cc2ae779 | Stakeholder / Motivation | No | No second canonical object | Leaf Stakeholder ref has Motivation default; R.1 path built there. | Native-layer organisation effect | B: explicit index placement; contextual links remain metadata |
| Motivation contextual Stakeholder | I.1.1.1.1 Motivation / Drivers / 24/7 Availability / Stakeholder references / Healthcare Provider | I Motivation / I.1 Drivers / I.1.1 24/7 Availability / I.1.1.1 Stakeholder references — Reference navigation / I.1.1.1.1 Healthcare Provider — Stakeholder reference [historical_reference]; NO identifierRef; target id-a1c118892a525256a69f93d8488059b8 | No folder occurrence; native Stakeholder remains at Motivation / R Cross-Cutting References — Reference navigation / R.1 Stakeholder Index — Reference navigation / Healthcare Provider | 2018200a493d458096b72031cc2ae779 (context target) | Stakeholder / Motivation | No; reference-only leaf | One canonical index object; context label absent | No object ref or recognised native anchor; folder not created. Existing Associations and navigation metadata survive. | Reference-only omission | Retain metadata; SA to choose any additional reference presentation |
| Views / 0-Motivation | Views / 0-Motivation / Historical Motivation / Legacy Chapter Principles — 1 (authored View path, separate from Continuum element coordinates) | Views / 0-Motivation / Historical Motivation / item ref=`id-82ce69f4f6fd57cebca31d426cf1b981` | Views / 0-Motivation / Historical Motivation / Legacy Chapter Principles — 1 | N/A — authored View ID `id-82ce69f4f6fd57cebca31d426cf1b981` | ArchiMate diagram / Views | No, View leaf | No; exactly one View placement | Separate preserved View-folder tree; leaf diagram refs are placed under native Views. | Coherent/preserved | Preserve authored View folders; no remediation evidenced |
| Views / 1-Strategy | Views / 1-Strategy / Capabilities / 1-Business Capabilities / [CT1] Care Access & Coordination / [CT2] Care Access Management / [CT3] Care Eligibility Determination / Care Eligibility Determination Overview — v0.2 (authored View path, separate from Continuum element coordinates) | Views / 1-Strategy / Capabilities / 1-Business Capabilities / [CT1] Care Access & Coordination / [CT2] Care Access Management / [CT3] Care Eligibility Determination / item ref=`id-afd497f842db56cda66a7f83cde826fc` | Views / 1-Strategy / Capabilities / 1-Business Capabilities / [CT1] Care Access & Coordination / [CT2] Care Access Management / [CT3] Care Eligibility Determination / Care Eligibility Determination Overview — v0.2 | N/A — authored View ID `id-afd497f842db56cda66a7f83cde826fc` | ArchiMate diagram / Views | No, View leaf | No; exactly one View placement | Separate preserved View-folder tree; leaf diagram refs are placed under native Views. | Coherent/preserved | Preserve authored View folders; no remediation evidenced |
| Views / 2-BusinessLayer | Views / 2-BusinessLayer / 1-BusinessCapabilities / [CT1] Care Access & Coordination / [CT2] Care Access Management / 0-Care Access Management — CT3 Landscape (authored View path, separate from Continuum element coordinates) | Views / 2-BusinessLayer / 1-BusinessCapabilities / [CT1] Care Access & Coordination / [CT2] Care Access Management / item ref=`id-dc13f9fb5fda51f5bfbf86afbff20d49` | Views / 2-BusinessLayer / 1-BusinessCapabilities / [CT1] Care Access & Coordination / [CT2] Care Access Management / 0-Care Access Management — CT3 Landscape | N/A — authored View ID `id-dc13f9fb5fda51f5bfbf86afbff20d49` | ArchiMate diagram / Views | No, View leaf | No; exactly one View placement | Separate preserved View-folder tree; leaf diagram refs are placed under native Views. | Coherent/preserved | Preserve authored View folders; no remediation evidenced |
| Views / 3-ApplicationLayer | Views / 3-ApplicationLayer / 1-InformationMap / Individual / [Topic] Individual (authored View path, separate from Continuum element coordinates) | Views / 3-ApplicationLayer / 1-InformationMap / Individual / item ref=`id-5ba19156cb0042078a310cbe57ead042` | Views / 3-ApplicationLayer / 1-InformationMap / Individual / [Topic] Individual | N/A — authored View ID `id-5ba19156cb0042078a310cbe57ead042` | ArchiMate diagram / Views | No, View leaf | No; exactly one View placement | Separate preserved View-folder tree; leaf diagram refs are placed under native Views. | Coherent/preserved | Preserve authored View folders; no remediation evidenced |

## Views: independent organisation and preservation

The existing View folders come from `projection-template.xml`. The navigation
projector's `only_views` traversal retains View references and their ancestors,
then appends that separate organisation after replacing architecture navigation.
The template has empty element/relationship collections and 1,209 authored Views;
it does not establish the current canonical element hierarchy.

| Actual outer View branch | Views | Inspection |
| --- | ---: | --- |
| `Views / 0-Motivation` | 25 | Historical Motivation, Historical Source Evidence, Requirement Co-governance and directly placed Motivation diagrams remain distinct. |
| `Views / 1-Strategy` | 772 | Capability ancestry is nested under Business, Business Enabling and Enterprise capability View collections; recovery/reference material remains explicitly separated. |
| `Views / 2-BusinessLayer` | 397 | Business Capability and Business Enabling Capability contexts retain their progressive diagram hierarchy. |
| `Views / 3-ApplicationLayer` | 15 | InformationMap, EnterpriseCapabilities, ApplicationMap and MiddlewareMap remain as authored; Topic-oriented folders retain their diagrams. |

Every View has exactly one organisation reference and exactly one native tree
location. All 1,032 folder paths occur unchanged in template, published Exchange,
native save and returned Exchange. No authored View path is lost, flattened or
duplicated, and every View's complete content signature, including geometry,
connections and style, matches the publication. A second import preserves the
same object/folder paths. The normal native `Views` root does not add a redundant
`Views / Views` nesting because the exact source `Views` label is recognised as
a native marker.

These are structurally useful, deliberately authored diagram hierarchies. Their
older numbering and vocabulary are not identical to the accepted I–VI/R element
navigation; preservation is intentional, not evidence that current canonical
navigation is wrong. Personal preference about a different future View taxonomy
is a separate Solution Architect decision. This diagnostic supplies no reason to
remediate Views. A bounded element-organisation correction can leave their retained
subtree untouched; a future change must verify that preservation rather than
assume it.

## Fixture results and the folder/element hypothesis

Four small fixture files, their native saves, no-edit exports, second-pass controls
and execution logs are retained under [diagnostics/fixtures](../diagnostics/fixtures).
All are synthetic diagnostic artefacts, never replacement Harmonia publications.
Capability and DataObject examples test native folder mechanics; the diagnostic
Topic labels do not author canonical Topic classification. The real published
Topics independently establish the same native DataObject behaviour.

| Case | Actual Archi 5.10 result | Conclusion |
| --- | --- | --- |
| Simple canonical leaf under a folder | The Capability moves from its initial native root into the folder. | Nested placement itself is supported. |
| Canonical reference item with child navigation | Parent Capability remains at Strategy root; the child goes into the folder bearing the parent's label. | A combined folder/reference item cannot place the parent object. |
| Business contextual CT1/CT2 folders, metadata reference to a Capability elsewhere | BusinessFunction appears beneath both context folders in Business; Capability remains at its separate Strategy primary location. | The contextual representation works without another live reference or semantic edge. |
| Nested CT1 → CT2 → CT3 canonical items | CT1 and CT2 objects remain at Strategy root; CT3 leaf is placed beneath their folder hierarchy. | Recursive parent separation, not a special CT1 rule. |
| Parent Topic/DataObject → leaf Topic/DataObject | Parent remains at Application root; leaf appears under the parent's labelled folder. | Reproduces the real Care/Encounter mechanism. |
| Candidate A, navigation folder plus its own canonical leaf, nested Capability and Topic examples | Parent and child actual elements occupy the intended folders alongside descendant navigation. Paths survive no-edit export/reimport. | Technically supports the hypothesis. Folder and element must be physically distinguished for this importer; this is not a semantic split. |
| Product and ValueStream in one logical folder | Product is under Business; ValueStream is under Strategy, each with the logical folder label. | Native type controls layer placement and can split a logical branch. |
| Repeated live reference to one Capability | One canonical object ends in the second folder; the first folder is empty. Both folder paths persist through the returned native-root-anchored organisation. | Repeated refs do not supply reliable contextual aliases. |
| Exact top-level `Strategy` marker with a BusinessFunction descendant | Marker is consumed; Capability appears under Strategy, BusinessFunction under Business. A same-named empty folder can remain on the Strategy side. | A marker does not override type placement; careless mixed-type root wrapping can create empty branches. |
| Unanchored source-only/reference-only navigation | Source-only case and Driver's reference-only child folder disappear. | A folder-only label is not always sufficient without an anchor or live descendant. |
| Explicit `Motivation` marker with Driver folder + Driver leaf and Stakeholder context notes; explicit `Other` marker with source-only reference folders | Driver, context folders and separately indexed Stakeholder retain intended paths. The source-only Other folder also survives. Two canonical fixture elements and zero semantic relationships suffice. | Native-root anchoring can preserve context/source-only navigation without alias elements or semantic relationships. The choice of destination still needs SA judgement. |

All fixture object and folder paths are stable on the second pass. Native folder
IDs are generated representation identities and are not used as canonical
correspondence. The published full model is likewise path-stable on the second
pass, including its undesirable organisation. These tests establish mechanics,
not a whole-Harmonia candidate implementation or final usability acceptance.

## Shared navigation meaning versus physical projection

**Shared canonical semantics and coordinates can be retained with a
representation-specific physical organisation.** This follows from the accepted
separation between canonical identity/navigation and derived folder presentation,
and is technically demonstrated by the fixtures.

It would overstate the historical evidence to say 1.1.10/1.1.11 unconditionally
asserted identical native trees: their completed records explicitly acknowledge
native roots, mixed-layer splitting and omitted non-element navigation. Their
implementation nevertheless projects the same resolved tree shape directly into
Exchange, combining canonical-parent refs and child navigation. Their verification
accepted those transformations without establishing that the resulting native
model tree was an effective workbench. The direct physical mapping is therefore
insufficient for this purpose even though logical navigation and transport remain
correct.

RAD headings/bookmarks and Manifest/browser nodes can combine a canonical
location with a document section and child sections. Standard Exchange can express
that structural tree. This Archi importer requires a different physical contract:

- native root categories remain the outer containers;
- each actual canonical object needs one effective **leaf** reference;
- a navigation parent needs a folder separate from the actual element;
- mixed-native-layer populations require intentional layer-specific allocation;
- references/contexts should not repeat live element placement refs;
- source-only/context-only folders need explicit native anchoring if they must
  survive independently of descendant objects;
- authored View navigation remains its own preserved tree.

The existing Archi Navigation & View Convention and specialised Round-Trip Praxis
already distinguish folder context from canonical elements and describe
folder-plus-element patterns, including a folder for contained leaves in their
semantic-containment scope. The newer Continuum projection consumes the accepted
canonical navigation instead of deriving another hierarchy from Home/Composition.
Those scopes must not be conflated. This diagnostic neither replays an older
containment convention over current navigation nor manufactures semantic ancestry.
A remediation decision should reconcile its exact physical policy with the retained
specialised convention, including whether only navigation parents or all elements
receive folders. No canonical navigation change is necessary to fix the mechanics.

## Bounded alternatives for Solution Architect consideration

These are candidate representation policies, **not directives applied here**.
All retain canonical IDs, element Names, types, standing, semantic relationships,
accepted navigation meaning/coordinates and authored Views. None requires a new
semantic relationship, proxy architectural element, duplicate canonical instance,
new Praxis or workbench subsystem.

| Alternative | Canonical Continuum and navigation semantics | RAD / Manifest | Exchange XML effect | Archi usability | Round-trip stability | Complexity and risk |
| --- | --- | --- | --- | --- | --- | --- |
| **A — Folder plus canonical element** | No canonical change; retain each existing logical node and coordinate. Distinguish its physical navigation folder from its canonical object. | No change to their navigation or content. | For each canonical node with children, keep its navigation item as an unreferenced folder and move its existing identifierRef to one new leaf child. Keep descendant navigation as siblings of that leaf. Parent-only policy adds 218 leaf items to this population, with no extra element or relationship. | Removes all 218 loose parent objects; preserves useful descendant hierarchies. By itself it does not resolve native ValueStream/R splitting or preserve all reference-only descendants. | Tested Capability/DataObject pattern is path-stable. One effective leaf ref per canonical object; organisation IDs may change on transport. Whole-model candidate remains untested. | Low, local projector change. Folder/element labels can look repetitive; decide parent-only versus all-element folder policy. Preserve View filtering and canonical naming. |
| **B — Separate Archi workbench physical organisation** | Retain the same canonical traversal, source coordinates and typed contexts; derive only Archi/Open Exchange placement rules. No new taxonomy master or canonical architecture. | No change. Shared logical navigation remains their source. | Apply A where needed, emit explicit native-root markers/anchors, intentionally partition native-type exceptions, and choose layer-specific R/context presentation. Keep a single canonical leaf ref and existing metadata links. Views retain their original subtree. | Addresses the complete defect set: parent separation, unexplained cross-layer branches, disappearing context/source-only navigation. R must have an explicit layer-aware presentation; ordinary importer cannot provide an independent global root. | Native-anchored reference-only fixtures and A are stable. Mixed-type marker fixture warns of empty branches if copying a whole logical root indiscriminately. Full candidate must be verified. | Low-to-moderate if confined to existing navigation projector. Requires SA choices for R, ValueStreams, unpopulated/source-only areas and physical folder policy. Risk is silent reclassification or duplicate live refs if implemented carelessly. |
| **C — Existing representation with bounded corrections** | No canonical or logical navigation change. Treat current hierarchy as usable once the parent placement defect is corrected, while explicitly accepting native splitting/reference limitations. | No change. | The smallest safe correction is the same parent-only leaf-ref split in A. Keeping identifierRef on the folder as well would be redundant; adding a root fallback or repeating refs elsewhere would not solve context placement. | Restores canonical parent elements inside their navigation contexts; leaves current Strategy/III, layer-split R, omitted source-only navigation and flat Relations as consciously accepted limitations. | Basic correction supported by A fixtures. No evidence that a smaller label/ID-only correction can change the importer rule. | Lowest scope, but inadequate if acceptance requires all observed branches to be coherent as a workbench. Residual limitations must be stated explicitly. |

**Smallest viable mechanical correction:** split only the 218 canonical parent
items into folder plus one canonical leaf reference. Folder identifiers remain
projection locators; a new leaf locator, if used, is not a new canonical ID.
There is no need to put every leaf in another folder merely to cure this defect.
This is the smallest correction for parent-element leakage, not a complete cure
for every usability issue identified here.

**Smallest candidate for the complete requested workbench outcome:** the same
local split plus a bounded native-layer-aware organisation adapter in the existing
projector, with explicit treatment of cross-cutting indexes and reference-only
contexts. This is A combined with the narrow form of B, not a second architecture
or a redesigned round-trip process. Exact folder policy remains a Solution
Architect decision. The diagnostic deliberately does not implement a full model
candidate, change current organisation, or claim that a chosen layout is accepted.
If native splitting and reference omissions are acceptable, C is correspondingly
smaller. No new semantic relationships are needed for any of these choices.

## Verification and limitations

[verification.json](../diagnostics/verification.json) records the independent
read-only comparisons and all fixture executions. The counts are explicit rather
than relying on an empty selection to imply success:

| Verification | Outcome |
| --- | --- |
| Actual published XML import/save/export in isolated Archi 5.10.0 | PASS; XSD validation succeeds before import and after export. |
| Canonical element records | PASS: all 1,602 IDs/bindings, names, native types, specialisations, documentation and **all property values** compare exactly to the publication. |
| Semantic relationship signatures | PASS: all 1,651 IDs and complete records compare exactly. |
| Authored View content signatures | PASS: explicit namespace-qualified selection of all 1,209 View subtrees, with exact signatures including geometry/style/connections. |
| View organisation | PASS: all 1,209 placements and 1,032 folder paths match template, publication, native save and return. |
| Full native organisation inventory | PASS: all canonical objects and folders inspected; all 218 loose elements explained by parent-item mechanics. |
| Published model second pass | PASS: same complete object/folder paths; this is stability, not usability. |
| Four synthetic fixture files, first and second passes | PASS: each input/output validates in actual Archi; expected paths/ref behaviour asserted independently. |
| Required Java architecture suite | PASS: Java 21, offline Maven, 90 tests across 11 architecture classes, zero failures/errors/skips; build succeeds in 20.775 seconds. See [bounded test summary](../diagnostics/architecture-tests.txt). |
| Scope preservation | PASS: protected prior files match entry hashes and no tracked file changed; only 1.1.13/001 diagnostic evidence is added. See [preservation.json](../diagnostics/preservation.json). |
| Evidence layout/event check | Direct Task/Report event/prefix checks performed. Full iteration checker requires an HCRL; none is created for this diagnostic. The reserved 001 boundary is prospective correspondence, not an accepted transition. |
| Task event/prefix provenance | UNRESOLVED for both original and continuation instructions; no substitute event or prefix manufactured. |
| Human usability acceptance / remediation acceptance | Not performed or asserted. |

The maintained `verify_continuum_transport.py` helper was attempted but stopped
at its current-projection gate: it invokes `continuum_workbench.review`, which
requires exact bytes of the projection of the **current** root release (1.1.12).
The diagnostic input intentionally projects accepted **1.1.11**. This was a
baseline-selection/precondition failure, not failed transport or model corruption.
The publication was not relabelled, the root authority was not changed, and no
application/review gate was bypassed. The diagnostic's independent exact-file
comparisons establish the preservation facts above without claiming an ingest
review against current architecture.

A further bounded inspection found that `continuum_workbench.signatures` constructs
an unqualified intermediate path for the nested `views/diagrams` group; that helper
selects zero Views for that argument. This diagnostic therefore uses explicit
`a:views/a:diagrams/a:view` selection and asserts the expected 1,209 count. It does
not modify the helper or retrospectively invalidate accepted 1.1.12 evidence,
whose separate raw comparator already checked all 1,209 Views. This is an
additional verification limitation, distinct from the model-tree root causes.

No publication-generation/staleness command was used to replace accepted material.
Temporary fixtures, generated inventories and a bounded retained importer extract
are diagnostic evidence only. Assertions were refined against observed fixture
outputs before final reporting: in particular, empty folders created by repeated
references remain in a native-marker-anchored return rather than disappearing.
No hypothetical output is reported as an actual Archi result.

## Review gap and smallest future safeguard

> Open Exchange validity and no-loss Archi transport do not establish that the
> imported model is organised usefully for architectural work.

The earlier evidence established identity/content transport and explicitly
acknowledged organisation normalisation. It did not establish the usability of
that normalised native tree. The existing human structural-review checkpoint was
satisfied for the accepted reader-facing result; this finding is a separate
workbench-specific gap and does not reopen or rewrite that acceptance.

The smallest additional safeguard is a **bounded Solution Architect inspection of
the actual imported native model tree for the exact proposed publication**,
recorded with the existing publication/HCRL evidence. Inspect a CT1/CT2/CT3 parent
and leaf, a contextual Business branch, a Topic parent and leaf, a Motivation Driver
with reference contexts, native-type exceptions such as ValueStream, both R
indexes, empty/source-only areas, and representative View folders. Confirm that
actual canonical objects are reachable in their intended contexts and that native
splitting/reference-only treatment is intentional and comprehensible. A short
record identifying the exact file and inspected native save is sufficient; a few
representative captures may help but are not a new mandatory subsystem.

Existing XSD, identity/no-loss and authored-View preservation checks should remain.
A small parent-placement/path count can support the human inspection if useful,
but cannot substitute for it. No new Praxis, factory, universal governance schema
or large automated acceptance mechanism is proposed or installed.

## Solution Architect decision questions

1. Should a navigation location be represented as a folder containing its canonical
   element alongside child navigation, and should that physical policy apply only
   to navigation parents or also to contained leaves under the existing specialised
   convention?
2. Should Archi use projection-specific physical organisation while retaining the
   same Continuum navigation meaning, coordinates and typed references as RAD and
   Manifest?
3. For canonical elements that are navigation parents, should the sole effective
   identifierRef move to a leaf inside the parent folder, preserving existing
   canonical IDs, Names, types and standing?
4. Should contextual references remain navigation folders plus metadata links,
   with explicit native-root anchors where necessary, and with no repeated live
   element references? Which reference-only contexts should remain visible?
5. Should native Archi layer folders remain visible outer containers, as the
   standard importer requires, with explicit presentation of exceptions such as
   logically Business ValueStreams in native Strategy?
6. Should Cross-Cutting References use clearly identified layer-specific indexes
   under Motivation and Strategy, and how should historical/index standing remain
   visible without renaming canonical elements or creating aliases?
7. Given exact preservation of all 1,209 Views and their 1,032 folder paths, should
   authored View organisation remain unchanged, or is a separately bounded View
   review desired?
8. Is the proposed bounded Solution Architect inspection of the imported native
   tree sufficient as the additional checkpoint before future workbench-publication
   acceptance, and which representative branches must that inspection record?
