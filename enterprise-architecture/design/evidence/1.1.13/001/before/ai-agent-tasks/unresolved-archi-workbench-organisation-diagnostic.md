<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Archi workbench organisation diagnostic

Task-Issued-At: UNRESOLVED — no attributable system instruction-entry timestamp is exposed in this interaction.

Issuer: Mark Hunter, Solution Architect, through the operative user instruction.
Executor: Codex. Prospective provenance boundary: 1.1.13 / iteration 001.
No 1.1.13 HCRL exists at entry; this allocation reserves diagnostic correspondence,
not acceptance or authority to remediate. The unavailable Task event and sortable
filename prefix require Solution Architect treatment before closure. No current
clock, recording time, file metadata or commit timestamp substitutes for that event.

## Originating state and scope

Clean worktree at `ae94a7ed067c436da49443d72d2eb72ad30fc5ff`.
Root release is 1.1.12 / predecessor 1.1.11. Accepted 1.1.11 architecture and closed
1.1.12 publication/tooling history retain their standing. The linked 1.1.5-002
pending HCRL is historical application/review context, not authority to reopen it.
Current Continuum Representation and completed 1.1.7, 1.1.10, 1.1.11 and 1.1.12
records supply the relevant succession/acceptance context.

Apply AGENTS.md, Architecture Governance, Architecture Review Praxis, Architecture
Change Provenance, release policy and canonical AX-04/08/09/14/15/17. The task is
diagnostic/advisory only: no release allocation, remediation acceptance HCRL,
canonical change, publication regeneration or workbench reorganisation is applied.
Required formal evidence is the only repository output.

## Operative instruction (verbatim)

# Goal

Perform a bounded diagnostic review of the **Archi workbench organisation produced by the published Harmonia ArchiMate Open Exchange projection**.

The diagnostic is triggered by Solution Architect inspection of the accepted/published 1.1.11 Open Exchange XML in Archi 5.10.

The Open Exchange XML is mechanically valid and transport-safe, but its imported Archi model tree is not consistently usable as an architectural workbench.

This task is **diagnostic only**.

Do not remediate the Exchange projection, Continuum, navigation, RAD, Manifest, canonical architecture or Archi organisation as part of this task.

# Expected Outcome

Determine precisely why the published Open Exchange projection produces a hybrid/disordered Archi model tree despite correctly representing the accepted Continuum and passing transport verification.

In particular establish:

1. how the current projector maps canonical navigation nodes, contextual navigation nodes and canonical architectural elements into Open Exchange `<organization>` structures;
2. how Archi 5.10 interprets those structures on import;
3. why some navigation hierarchies import correctly while canonical parent elements also appear separately at their native layer root;
4. why navigation branches associated with other architectural domains can appear beneath an Archi native-layer folder;
5. whether canonical elements used as navigation parents require a distinct physical representation from their navigation folder/context;
6. whether the same logical Continuum navigation can be retained while using an Archi-specific physical organisation;
7. what the smallest viable projection change would be to make Archi a coherent architecture workbench without changing canonical architecture.

Do not assume the proposed solution before establishing the cause.

# Context

The accepted authority model remains:

**Architecture Continuum = canonical architecture authority**

**Open Exchange XML = derived projection/interchange**

**Archi = architecture workbench**

Release 1.1.12 established the durable published projection:

`enterprise-architecture/design/archimate/published/1.1.11/Harmonia-Architecture.xml`

That XML was deterministically generated from accepted 1.1.11, matched the accepted projection bytes, passed Open Exchange validation and successfully completed Archi 5.10 import/save/export verification without canonical element, semantic relationship or authored View loss.

Subsequent Solution Architect inspection of the actual imported Archi model tree identified a separate usability defect.

The defect does **not currently demonstrate corruption of canonical architecture or transport loss**.

It demonstrates that successful transport verification is insufficient to establish a useful Archi workbench organisation.

# Observed Finding 1 — Strategy Hybrid Organisation

The imported Archi Strategy tree presents a structure broadly resembling:

`Strategy`

- `III Business`
- `II Strategy`
- `R Cross-Cutting References — Reference navigation`
- `[CT1] Care Access & Coordination`
- `[CT1] Care Administration Enablement`
- `[CT1] Care Context Integration Enablement`
- ...
- `[CT2] Activity & Execution`
- `[CT2] Assurance Evaluation`
- ...

This combines:

- navigation-derived organisational branches;
- architectural domains apparently foreign to the native Strategy layer;
- canonical CT1/CT2 Capability elements appearing directly beneath the native Strategy organisation.

The accepted Strategy navigation itself is not intended to be flat.

Diagnose why the actual canonical Capability population appears separately at this level rather than solely within a coherent Strategy workbench hierarchy.

Also diagnose why `III Business` and `R Cross-Cutting References` appear beneath the native Strategy organisation.

Determine whether those branches are being relocated or replicated because they contain references to Strategy-layer Capability elements.

Do not assume that explanation without evidence.

# Observed Finding 2 — Business Organisation

Business behaves substantially differently and demonstrates that hierarchical organisation can work.

The imported tree includes a coherent structure broadly resembling:

`Business`

→ `III Business`

→ `III.4 Business Behaviour`

→ `III.4.x [CT1] <Capability> — Capability context`

→ `III.4.x.x [CT2] <Capability> — Capability context`

→ behaviour groupings such as:

- Business Services
- Business Functions
- Business Processes

with actual behaviour elements beneath those groupings.

This broadly matches the accepted 1.1.11 Business Behaviour contextual navigation.

Determine why these contextual Capability navigation nodes behave correctly in Archi while canonical Capability elements in Strategy do not.

In particular determine whether the distinction arises because the Business Behaviour Capability occurrences are navigation/context items rather than the canonical Capability element's primary organisational occurrence.

# Observed Finding 3 — Application Parent Topic Separation

Application provides a particularly clear example.

The imported tree contains a coherent navigation hierarchy such as:

`Application`

→ `IV Application`

→ `IV.1 Information Architecture`

→ `IV.1.1 Topics`

→ `IV.1.1.1 [Topic] Care`

beneath which actual Topic elements such as:

- `[Topic] CareLocation`
- `[Topic] Encounter`
- `[Topic] EncounterContext`
- `[Topic] EncounterProgression`
- `[Topic] Episode`

appear.

However, the canonical parent Topics used to establish the hierarchy also appear separately beneath the native Application root, including:

- `[Topic] Care`
- `[Topic] Coordination`
- `[Topic] Enterprise`
- `[Topic] Equipment`
- `[Topic] Individual`
- `[Topic] Ordering`
- `[Topic] Place`
- `[Topic] Scheduling`
- `[Topic] Service`
- `[Topic] TaskCompletion`
- `[Topic] Workforce`

This strongly suggests a distinction between:

- an organisation/navigation item representing the architectural position of a Topic; and
- the actual canonical Topic element reference.

Diagnose this explicitly.

Determine whether the current Exchange organisation uses the canonical parent Topic as an organisation node without also placing its actual element reference at that location, causing the projector or Archi to place the element separately at the native Application root.

Again, treat this as a hypothesis to test, not an assumed conclusion.

# Diagnostic Question — Navigation Node Versus Canonical Element

Determine the actual Open Exchange and Archi behaviour when a canonical architectural element is also intended to act as a parent in the reader/workbench hierarchy.

Investigate whether the correct physical Archi representation needs to distinguish:

**Navigation folder/context**

from:

**Canonical architectural element**

even when both represent the same conceptual architectural location.

For example, determine whether an Archi-compatible representation may need to resemble:

`IV.1.1.1 Care`

- `[Topic] Care`
- `[Topic] CareLocation`
- `[Topic] Encounter`
- ...

rather than attempting to make `[Topic] Care` itself function as the organisational container.

Likewise determine whether Strategy might require:

`<navigation folder for CT1 Care Access & Coordination>`

- `[CT1] Care Access & Coordination`
- `<CT2 navigation folders>`
  - `[CT2] ...`
  - ...

Do not implement this representation in the diagnostic task.

Establish whether it is technically and architecturally appropriate first.

# Diagnostic Question — Shared Semantics Versus Projection-Specific Organisation

The Continuum navigation is shared architectural/navigation meaning.

The RAD, Manifest and Archi do not necessarily have identical physical navigation mechanisms.

Determine whether 1.1.10/1.1.11 incorrectly assumed that a single physical hierarchy could be projected identically into:

- RAD headings/bookmarks;
- Manifest/browser navigation;
- Open Exchange organisation;
- Archi's model tree.

Assess whether the correct principle should instead be:

> The projections share canonical navigation semantics and coordinates, while each projection may require a representation-specific physical structure.

If so, establish precisely what Archi/Open Exchange requires.

Do not change the canonical navigation merely to accommodate Archi implementation mechanics.

# Complete Workbench Inspection

Do not limit diagnosis to the three supplied screenshots.

Inspect the complete imported Archi organisation across:

- Motivation;
- Strategy;
- Business;
- Application;
- Technology;
- Implementation & Migration;
- Cross-Cutting References;
- Views.

Classify each significant branch as:

- coherent/useful;
- duplicated/hybrid;
- flattened;
- misplaced;
- canonical element separated from navigation context;
- projection/reference leakage;
- native-layer organisation effect;
- empty/irrelevant;
- or another evidence-based classification.

Determine whether the same root cause explains all defects or whether multiple projection behaviours are involved.

# Views

Inspect the organisation of the existing **1,209 authored Views** separately.

Do not assume that element-tree organisation and View organisation have the same problem.

Establish:

- whether Views remain sensibly organised in Archi;
- whether View folders reflect the intended architecture/workbench organisation;
- whether navigation changes would affect View placement;
- whether any authored View organisation is currently lost, flattened or duplicated.

Do not modify Views.

# Open Exchange Mechanics

Inspect the generated Open Exchange XML and projector implementation sufficiently to establish:

- organisation hierarchy emitted;
- folder/item identifiers;
- element references;
- nested organisation items;
- how canonical elements used as navigation parents are represented;
- whether elements can occur/reference within the desired nested organisation;
- whether the same element is referenced from multiple organisation locations;
- how Archi resolves such references;
- whether Archi imposes native layer/type placement constraints;
- what Archi does when an organisation item acts as both contextual parent and element reference;
- what changes Archi makes during import/save/export.

Use small diagnostic extracts or scripts.

Do not dump or broadly rewrite the full Open Exchange XML merely for inspection.

# Actual Archi Verification

Use Archi 5.10 with an isolated temporary workspace to test representative minimal cases if required.

Where useful, construct bounded diagnostic Open Exchange fixtures demonstrating:

1. a simple canonical element inside a navigation folder;
2. a canonical element that also has child navigation;
3. a contextual navigation folder referencing a canonical element elsewhere;
4. nested Strategy Capability examples;
5. nested Application Topic examples.

These fixtures are diagnostic evidence only.

Do not replace the published Harmonia projection with a fixture or experimental result.

# Required Diagnostic Evidence

Produce a human-readable diagnostic table containing, at minimum:

- architectural domain;
- expected logical navigation path;
- actual Open Exchange organisation path;
- actual Archi imported path;
- canonical element ID;
- native ArchiMate type/layer;
- whether the element is a navigation parent;
- whether it appears separately elsewhere;
- reason for separate placement if established;
- root-cause classification;
- candidate representation approach.

Include representative examples from:

- Strategy CT1;
- Strategy CT2;
- Business Behaviour contextual CT1/CT2;
- Application parent Topic;
- Application leaf Topic;
- Motivation;
- Cross-Cutting References;
- Views.

# Candidate Remediation Alternatives

Conclude with bounded alternatives for Solution Architect consideration.

At minimum consider, if technically supported by evidence:

## Alternative A — Folder plus canonical element

Use a navigation folder as the organisational parent and place/reference the canonical architectural element within that folder alongside its child navigation.

## Alternative B — Separate Archi workbench organisation

Retain shared navigation semantics/coordinates but generate an Archi-specific physical organisation optimised for workbench use.

## Alternative C — Existing representation with bounded corrections

If the current representation is fundamentally sound and only specific projector behaviour causes root-level leakage, identify the smallest correction.

Do not assume these are the only alternatives if evidence establishes a better bounded option.

For each alternative report:

- effect on canonical Continuum;
- effect on navigation semantics;
- effect on RAD/Manifest;
- effect on Exchange XML;
- effect on Archi workbench usability;
- effect on round-trip stability;
- complexity;
- risks;
- whether new semantic relationships would be required.

Prefer no new semantic relationships.

# Review Gap

Record the specific verification gap exposed by this finding:

> Open Exchange validity and no-loss Archi transport do not establish that the imported model is organised usefully for architectural work.

Identify the smallest additional verification appropriate for future publication of an Archi workbench projection.

Do not create another Praxis, factory subsystem or large automated governance mechanism.

A bounded Solution Architect inspection of the imported Archi model tree may be sufficient.

This is diagnostic/advisory only.

# Constraints

- 1.1.11 remains accepted architecture.
- 1.1.12 remains accepted publication/tooling history if already committed/closed.
- Do not rewrite accepted evidence or HCRLs.
- Do not modify canonical Continuum elements.
- Do not modify Canonical IDs.
- Do not rename elements.
- Do not change standing.
- Do not add/remove semantic relationships.
- Do not modify authored Views.
- Do not change accepted RAD navigation.
- Do not change accepted Manifest navigation.
- Do not change canonical navigation semantics merely to accommodate Archi.
- Do not modify the published 1.1.11 XML during this diagnostic.
- Do not implement a remediation.
- Do not redesign Architecture Archi-Roundtrip.
- Do not create a new Praxis.
- Do not perform unrelated cleanup.
- Preserve uncertainty where mechanics are not established.

# Authorised Activity / Scope

You are authorised to:

- inspect the current Open Exchange projector;
- inspect `projection-template.xml`;
- inspect the published 1.1.11 XML using bounded/programmatic diagnostics;
- inspect current navigation metadata;
- inspect relevant publication code;
- run read-only structural analysis;
- import the published XML into an isolated Archi 5.10 workspace;
- create bounded diagnostic Open Exchange fixtures;
- inspect Archi's no-edit returned XML;
- compare organisation structures;
- create 1.1.13 diagnostic evidence;
- identify bounded remediation alternatives.

You are not authorised to apply those alternatives.

# Expected Solution Architect Decisions

End the Report with explicit questions concerning:

1. the desired relationship between canonical elements and navigation folders in Archi;
2. whether Archi should use projection-specific physical organisation while retaining shared canonical navigation semantics;
3. treatment of canonical elements that are also navigation parents;
4. treatment of contextual references;
5. whether native Archi layer folders should remain visible as outer containers;
6. whether Cross-Cutting References require special Archi treatment;
7. whether View organisation requires remediation;
8. the smallest additional Archi workbench inspection checkpoint required before future publication acceptance.

Do not implement the decisions.

# Report

Create the formal diagnostic Task under:

`enterprise-architecture/design/evidence/1.1.13/<iteration>/before/ai-agent-tasks/`

Create the diagnostic Report under:

`enterprise-architecture/design/evidence/1.1.13/<iteration>/after/ai-agent-reports/`

The Report must:

- explicitly distinguish architecture correctness from workbench usability;
- preserve 1.1.11 and 1.1.12 historical standing;
- describe the observed Archi organisation defects;
- establish the Open Exchange/Archi mechanics causing them;
- compare Strategy, Business and Application behaviour;
- inspect the other architecture domains and Views;
- test the navigation-folder/canonical-element hypothesis;
- distinguish logical navigation semantics from projection-specific physical organisation;
- provide the required diagnostic table;
- provide bounded remediation alternatives;
- identify the smallest future verification safeguard;
- end with Solution Architect decision questions.

Do not prepare or complete an acceptance HCRL for a remediation that has not yet been authorised.

Do not modify the published Open Exchange projection as part of this diagnostic.

User-supplied reference: [hcrl-1.1.5-002.pending.md](../../../../../review/1.1.5/hcrl-1.1.5-002.pending.md).

## Operative continuation (verbatim)

Task-Issued-At (continuation): UNRESOLVED — no attributable instruction-entry
timestamp is exposed for this follow-up either. This records continued authority
within the same bounded diagnostic, not a new task or remediation authorisation.

> Continue the 1.1.13 diagnostic from the existing working state. The 502 interrupted the interaction, not the task. Preserve the findings already established and complete the remaining fixture tests, full workbench inspection, diagnostic evidence and Report. Do not remediate.

The follow-up again links the historical 1.1.5-002 pending HCRL above.
