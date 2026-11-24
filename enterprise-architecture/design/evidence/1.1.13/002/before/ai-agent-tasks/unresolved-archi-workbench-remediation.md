<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Archi workbench organisation remediation — operative Task

Task-Issued-At: UNRESOLVED — the system timestamp of this user instruction
entering the interaction is not exposed to the agent. The current clock is not
a substitute. The required filename prefix remains unresolved for Solution
Architect treatment before closure.

Issuer: Mark Hunter, Solution Architect, through the user instruction in this
interaction. Agent: Codex. Boundary: RAD-1.1.13, HCRL iteration 002.
Immediate governed release at entry: 1.1.12, predecessor 1.1.11.
Iteration 001 diagnostics and accepted historical evidence are preserved.
The linked historical HCRL 1.1.5-002 is navigation context, not new application
authority; the explicit dispositions below authorise this bounded remediation.

## Operative issued instruction (verbatim)

# Goal

Remediate the Archi workbench organisation defects established by the **1.1.13 iteration 001 diagnostic**.

The remediation applies only to the **physical Open Exchange / Archi workbench projection**.

Preserve the accepted Architecture Continuum, canonical navigation semantics, RAD, Manifest, architectural identities, semantic relationships and authored Views.

The objective is that the published Open Exchange XML imports into Archi as a coherent and usable architecture workbench, rather than merely being valid and lossless.

# Expected Outcome

After importing the remediated published Open Exchange XML into Archi 5.10:

1. canonical elements that are also logical navigation parents no longer appear detached at their native layer root merely because they have children;
2. those canonical parent elements appear within their intended workbench navigation context;
3. child navigation remains coherent beneath that context;
4. native-layer splitting does not cause foreign architectural navigation branches to appear misleadingly beneath another architectural domain;
5. required reference-only/contextual navigation remains visible where appropriate without duplicating canonical elements or inventing semantic relationships;
6. Business Behaviour contextual navigation remains coherent;
7. all 1,209 authored Views retain their existing content and organisation;
8. the imported model remains round-trip safe;
9. the physical Archi organisation is allowed to differ from the RAD/Manifest physical hierarchy where required by Archi mechanics while preserving the same canonical navigation semantics.

# Context

The 1.1.13 iteration 001 diagnostic established three distinct Archi/Open Exchange behaviours.

## 1. Parent-element separation

Archi 5.10 treats an Open Exchange organisation item with children as a folder even when it also contains an `identifierRef`.

Consequently, canonical elements used as logical navigation parents are not placed at that organisation location.

The diagnostic identified **218 affected canonical parent elements**:

- 204 Capabilities;
- 11 Topics;
- 3 Motivation Drivers.

Those elements remain separately placed at their native Archi layer root.

## 2. Native-layer splitting

Archi applies native ArchiMate layer organisation independently of the intended logical navigation hierarchy.

For example, `III Business` appeared beneath Strategy because the branch contains four ValueStreams, whose native ArchiMate layer is Strategy.

This behaviour is distinct from the Business Behaviour contextual Capability references.

## 3. Reference-only navigation

Repeated organisation references to the same element are not a reliable mechanism for contextual navigation.

Diagnostic fixtures established that two organisation references to one canonical element result in a single effective element placement at the last location, potentially leaving an empty folder at the first.

The diagnostic also established that explicit native-folder markers can preserve reference-only/contextual folders without adding architectural elements or semantic relationships.

## Views

The diagnostic confirmed that all **1,209 authored Views retain their original organisation and content**.

View organisation is therefore not part of the defect being remediated.

# Solution Architect Disposition 1 — Shared Semantics, Projection-Specific Physical Organisation

Adopt the following projection principle:

> **Canonical navigation semantics are shared across projections; physical navigation representation may be projection-specific where required by the target representation.**

The Continuum remains the source of canonical architectural organisation and navigation meaning.

RAD, Manifest and Archi projections must remain semantically aligned but are not required to use mechanically identical hierarchy representations.

Do not change canonical Continuum navigation merely to accommodate Archi's organisation mechanics.

# Solution Architect Disposition 2 — Native Archi Layer Containers

Retain Archi's native architectural layer folders as the outer workbench containers.

Do not attempt to suppress, bypass or reinterpret Archi's native layer model.

Within those layer containers, generate a coherent workbench organisation reflecting the applicable Continuum navigation.

The expected broad form remains:

- Motivation
- Strategy
- Business
- Application
- Technology
- Implementation & Migration

as applicable to the native Archi representation.

Cross-cutting/reference navigation must be represented without causing misleading cross-layer architectural ownership.

# Solution Architect Disposition 3 — Canonical Elements That Are Navigation Parents

A canonical architectural element must not be relied upon simultaneously to function as an Archi organisation folder.

Where a canonical element is also a logical navigation parent, use a distinct Archi navigation folder/context and place the canonical element itself within that context.

Conceptually, for Application Topics:

`IV.1.1.1 [Topic] Care`

- `[Topic] Care`
- `[Topic] CareLocation`
- `[Topic] Encounter`
- `[Topic] EncounterContext`
- `[Topic] EncounterProgression`
- `[Topic] Episode`
- ...

The outer `IV.1.1.1 [Topic] Care` occurrence is the **Archi navigation folder/context**.

The inner `[Topic] Care` occurrence is the **canonical architectural element**.

Likewise for Strategy:

`<CT1 navigation folder>`

- `[CT1] <canonical Capability>`
- `<CT2 navigation folder>`
  - `[CT2] <canonical Capability>`
  - `<CT3 navigation folder>`
    - `[CT3] <canonical Capability>`
    - ...

Do not allocate another Canonical ID to the navigation folder.

Do not create another Capability, Topic, Driver or other architectural element.

Do not create semantic relationships merely to represent this organisation.

The canonical element remains the canonical element; the surrounding folder is workbench navigation.

Apply this treatment consistently to the diagnosed population of canonical navigation-parent elements where technically appropriate.

# Solution Architect Disposition 4 — Leaf Elements

Canonical leaf elements that are not navigation parents should remain ordinary element placements within their applicable workbench navigation folder.

Do not wrap every element in an unnecessary folder.

The folder-plus-element pattern is required where needed to distinguish:

- navigation/container role; from
- canonical architectural element placement.

Avoid unnecessary structural noise.

# Solution Architect Disposition 5 — Contextual and Reference-Only Navigation

Where a navigation occurrence is contextual/reference-only rather than the primary placement of a canonical element, do not use repeated canonical element placement as the navigation mechanism.

Use the bounded native-folder representation proven by the diagnostic where required to preserve contextual navigation.

This applies particularly where the navigation is intended to provide:

- Business Behaviour capability context;
- cross-cutting/reference context;
- historical/reference navigation;
- other explicitly contextual navigation already present in the accepted Continuum.

A contextual folder:

- may identify the canonical subject it references through metadata;
- may carry the shared navigation coordinate;
- must not create another canonical architectural element;
- must not create another semantic relationship;
- must not cause Archi to relocate the canonical element from its primary workbench location.

Preserve the accepted distinction between canonical occurrence and contextual occurrence.

# Solution Architect Disposition 6 — Native-Layer Splitting

Correct misleading organisation caused by branches containing elements whose native ArchiMate layer differs from the architectural navigation context.

Do **not** change an element's native ArchiMate type or layer to make the tree look cleaner.

Do **not** move canonical authority or alter semantic architecture.

Instead, use projection-specific folders/reference organisation so that the workbench preserves both:

1. the element's correct native ArchiMate layer; and
2. the intended architectural navigation/context.

The four ValueStreams identified by the diagnostic are an explicit verification case.

They must retain their correct native ArchiMate representation.

At the same time, their existence must not cause a misleading `III Business` architectural branch to appear as though Business is structurally contained beneath Strategy.

Where Archi mechanics prevent literal reproduction of the logical hierarchy, prefer an explicit contextual/reference folder over misleading structural placement.

# Solution Architect Disposition 7 — Business Behaviour

Preserve the currently successful Business Behaviour organisation.

The diagnostic established that Business Behaviour contextual Capability folders behave coherently because they are contextual navigation rather than attempted primary Capability placement.

Do not regress:

`Business`

→ `III Business`

→ `III.4 Business Behaviour`

→ CT1 context

→ CT2 context

→ CT3 context where applicable

→ Business Services / Functions / Processes / Events as represented.

Preserve canonical Strategy references and contextual metadata established in 1.1.11.

Do not convert these contextual folders into repeated Capability element placements.

# Solution Architect Disposition 8 — Views

Do not reorganise the authored Views.

The diagnostic established that all **1,209 Views** retain their organisation and content.

Preserve that organisation.

Any change to View organisation discovered to be necessary during implementation requires separate Solution Architect disposition rather than silent correction.

# Solution Architect Disposition 9 — Published Projection

The remediation should produce the corrected Archi workbench projection through the maintained publication machinery.

Do not manually edit the published XML.

The durable published projection must remain deterministic and reproducible from its canonical source.

The accepted historical 1.1.11 publication remains historical evidence of the previously generated representation.

Do not rewrite accepted 1.1.11 or 1.1.12 evidence or completed HCRLs.

Follow the existing release/publication conventions for the 1.1.13 result rather than overwriting historical release artefacts in place.

If the current publication mechanism presents ambiguity about the correct release-labelled output for this tooling/projection change, stop and report that specific issue for Solution Architect disposition rather than inventing release semantics.

# Required Workbench Shape

Do not treat the following as a byte-for-byte prescribed tree.

It expresses the intended semantics.

## Strategy example

`Strategy`

→ `II Strategy`

→ `II.1 Capabilities`

→ `II.1.x <Capability scope>`

→ `<CT1 navigation folder>`

- `[CT1] canonical Capability`
- `<CT2 navigation folder>`
  - `[CT2] canonical Capability`
  - `<CT3 navigation folder>`
    - `[CT3] canonical Capability`
    - Feature/navigation descendants as applicable

The canonical Capability should not also appear detached at the Strategy root merely because it is a navigation parent.

## Application example

`Application`

→ `IV Application`

→ `IV.1 Information Architecture`

→ `IV.1.1 Topics`

→ `<Topic navigation folder>`

- canonical parent Topic
- child Topic elements/navigation as appropriate

The canonical parent Topic should not also appear detached at the Application root.

## Business example

Retain the coherent Business hierarchy already demonstrated by the diagnostic.

Do not introduce unnecessary folder+element duplication where existing leaf placement is already correct.

# Constraints

- Preserve accepted 1.1.11 canonical architecture.
- Preserve accepted 1.1.11 canonical navigation semantics.
- Preserve accepted RAD organisation.
- Preserve accepted Manifest organisation.
- Preserve Canonical IDs.
- Preserve names.
- Preserve standing.
- Preserve native ArchiMate types.
- Preserve all semantic relationships.
- Preserve all authored Views and their organisation.
- Do not invent semantic relationships for navigation.
- Do not duplicate canonical elements.
- Do not infer architectural containment from workbench folders.
- Do not change Business Behaviour allocations.
- Do not change Stakeholder associations.
- Do not change Capability correspondence/history.
- Do not redesign Architecture Archi-Roundtrip.
- Do not create a new Praxis.
- Do not introduce another architecture authority.
- Do not manually maintain a parallel Archi-specific architecture model.
- Keep the Archi projection derived, deterministic and reproducible.
- Do not perform unrelated cleanup.

# Authorised Activity / Scope

You are authorised to:

- modify the Open Exchange projection logic;
- introduce projection-specific Archi organisation rules;
- introduce native-folder markers where established by the diagnostic;
- distinguish navigation folders from canonical element placements in the Exchange organisation;
- preserve contextual/reference folders without repeated canonical placement;
- correct misleading native-layer splitting through projection organisation;
- update Archi projection documentation;
- add bounded regression tests;
- generate the corrected published Open Exchange projection according to existing release conventions;
- perform actual Archi 5.10 import/save/export verification;
- inspect the imported model tree;
- generate representative workbench inspection evidence;
- create 1.1.13 iteration 002 Task, Report and supporting evidence;
- prepare a pending HCRL after implementation.

# Required Verification

Verify at minimum:

1. all canonical element IDs are preserved;
2. all semantic relationships are preserved;
3. all 1,209 authored Views and their organisation are preserved;
4. the 218 diagnosed canonical navigation-parent elements no longer appear detached at native roots because of parent-folder behaviour;
5. canonical parent Capabilities appear in their intended Strategy workbench context;
6. canonical parent Topics appear in their intended Application workbench context;
7. the three diagnosed parent Drivers appear in their intended Motivation workbench context;
8. leaf elements remain sensibly placed without unnecessary wrapper folders;
9. Business Behaviour contextual navigation remains coherent;
10. contextual folders do not relocate canonical elements;
11. repeated canonical element references are not used as a substitute for contextual navigation;
12. the four ValueStreams retain their native type/layer without causing misleading Business-under-Strategy organisation;
13. Cross-Cutting References remain usable without misleading native-layer ownership;
14. historical/reference navigation remains usable;
15. the imported Archi model contains no unexplained detached population analogous to the diagnosed Strategy/Application root leakage;
16. Open Exchange validation passes;
17. actual Archi 5.10 import succeeds;
18. no-edit native save/export preserves canonical elements, relationships and Views;
19. a second Archi import of the returned representation remains structurally stable;
20. deterministic generation/reproduction passes;
21. maintained EA tests pass;
22. maintained Java architecture tests pass;
23. accepted historical evidence remains unchanged.

# Required Reader-Facing Archi Inspection

Mechanical verification is not sufficient for acceptance.

After generating the candidate projection, retain representative evidence of the actual imported Archi model tree for Solution Architect review.

At minimum provide inspectable evidence for:

- Motivation, including one of the three previously detached parent Drivers;
- Strategy Business Capabilities;
- Strategy Business Enabling Capabilities;
- one CT1 → CT2 → CT3 Capability path showing canonical element placement;
- Business Behaviour;
- Application Information Architecture / Topics;
- one parent Topic and its children;
- the four ValueStreams/native-layer case;
- Cross-Cutting References;
- historical Capability references;
- Views;
- one deep navigation path.

Do not treat agent inspection as Solution Architect acceptance.

The pending HCRL must remain pending until the Solution Architect has inspected the actual Archi workbench result.

# Future Verification Safeguard

Add the smallest bounded safeguard appropriate to the defect:

> **Where an Open Exchange publication materially changes Archi organisation, the generated model must be imported into the supported Archi version and its actual workbench tree structurally reviewed before HCRL acceptance.**

This complements, rather than replaces:

- Open Exchange validity;
- identity preservation;
- relationship preservation;
- View preservation;
- deterministic generation;
- no-edit round-trip verification.

Do not introduce a new Praxis or large semantic validation framework.

# Report

Create the formal remediation Task under:

`enterprise-architecture/design/evidence/1.1.13/002/before/ai-agent-tasks/`

Create the execution Report under:

`enterprise-architecture/design/evidence/1.1.13/002/after/ai-agent-reports/`

Retain iteration 001 diagnostic evidence unchanged.

The Report must include:

- implementation of each Solution Architect disposition;
- before/after Open Exchange organisation behaviour;
- treatment of all 218 canonical navigation-parent elements;
- Strategy workbench organisation;
- Application Topic organisation;
- Motivation parent-element organisation;
- Business Behaviour preservation;
- ValueStream/native-layer treatment;
- contextual/reference-folder treatment;
- Cross-Cutting References treatment;
- historical/reference treatment;
- View preservation;
- imported Archi workbench inspection evidence;
- first-pass and second-pass Archi stability results;
- canonical identity/relationship/View preservation results;
- Open Exchange validation;
- reproducibility results;
- maintained EA test results;
- Java architecture test results;
- any representation limitation that remains;
- any issue requiring Solution Architect disposition;
- proposed HCRL decision items.

Do not assert Solution Architect acceptance.

Do not complete the HCRL before the Solution Architect has inspected the actual imported Archi workbench.[@hcrl-1.1.5-002.pending.md](file:///home/hunterm/Development/Code/harmonia/enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md)
