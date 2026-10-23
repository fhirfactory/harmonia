<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — contained-element directory correction, existing 1.1.13 iteration 002

Task-Issued-At: UNRESOLVED

The system instruction-entry event timestamp is not exposed in this interaction.
No later clock reading, file metadata or capture time substitutes for that event.
The required timestamp filename prefix remains unresolved for Solution Architect
treatment before closure. Issuer: Mark Hunter, Solution Architect, through the
operative user instruction. Executor: Codex. Boundary: 1.1.13 / 002.

This follows [the original remediation Task](unresolved-archi-workbench-remediation.md)
and its [original Report](../../after/ai-agent-reports/20261023T050357Z-archi-workbench-remediation.md).
Entry release authority already records 1.1.13 / predecessor 1.1.12. The candidate
and implementation are uncommitted; entry HEAD does not recover those edits.
[Prior candidate XML](../parent-only-candidate.xml.gz) preserves the exact replaced
unaccepted projection. Earlier Reports, inspection evidence, accepted evidence
and iteration 001 remain unchanged.

## Operative issued instruction (verbatim)

Continue the existing **1.1.13 iteration 002 Archi workbench remediation**.

Do not start another diagnostic or redesign the projection.

The current candidate is substantially closer, but the Solution Architect review has identified one remaining workbench convention that must be applied consistently before acceptance.

# Governing Rule

There are two separate concerns:

1. **What constitutes architectural containment?**
2. **How an already-established containment relationship is represented in the Archi workbench.**

Do not conflate them.

The semantics that determine whether one architectural element is contained by another are defined by the applicable governed architecture/model.

Those semantics may differ by architectural area and element type.

Do **not** invent a new universal containment rule for this task.

Do **not** assume that every relationship represents containment.

Do **not** redefine containment merely to suit Archi organisation.

Use the containment already established by the governed architecture and its existing navigation/model semantics.

# Archi Representation Rule

Once an architectural element is determined to participate in a governed containment hierarchy:

> **Every contained architectural element MUST have its own Archi navigation directory at its governed containment position, irrespective of whether that element currently contains children.**

That directory contains the canonical architectural element itself.

If the element contains other governed elements, their directories are nested beneath its directory.

Therefore the invariant representation is:

`<directory representing governed location of Element A>`

- `<canonical Element A>`
- `<directory representing contained Element B>`
  - `<canonical Element B>`
  - `<directory representing contained Element C>`
    - `<canonical Element C>`

A leaf element is represented identically:

`<directory representing governed location of Element D>`

- `<canonical Element D>`

The existence of the directory MUST NOT depend upon whether the element currently has children.

# Important Workbench Invariant

Adding or removing a contained child must not require restructuring the existing parent's Archi representation.

If an element is a leaf today and receives a child tomorrow, its existing directory remains its directory and the new child's directory is simply added beneath it.

This is intentional.

The purpose is to make Archi usable as an architecture development workbench: the Solution Architect must be able to navigate to an architectural element and immediately have a stable location beneath which additional architecture can be developed.

# Example — Capability Hierarchy

The current candidate mixes two representations:

- elements with children receive directories;
- leaf elements appear directly beneath their parent's directory.

That is not the required convention.

Instead, a hierarchy must conceptually resemble:

`II.1.1.1 [CT1] Coordinated Activity & Interaction`

- `[CT1] Coordinated Activity & Interaction`

- `II.1.1.1.1 [CT2] Activity & Execution`
  - `[CT2] Activity & Execution`
  - `II.1.1.1.1.x [CT3] Entity-Relative Operational Progression`
    - `[CT3] Entity-Relative Operational Progression`
  - `II.1.1.1.1.x [CT3] Governed Activity Execution Control`
    - `[CT3] Governed Activity Execution Control`
  - `II.1.1.1.1.x [CT3] Operational Fulfilment Definition`
    - `[CT3] Operational Fulfilment Definition`

- `II.1.1.1.x [CT2] Event & Subscription`
  - `[CT2] Event & Subscription`

- `II.1.1.1.x [CT2] Interaction & Experience`
  - `[CT2] Interaction & Experience`

- `II.1.1.1.x [CT2] Interoperability & Exchange`
  - `[CT2] Interoperability & Exchange`

Use the actual governed coordinates. The numbering above is illustrative where marked `x`.

The important point is that `Event & Subscription`, for example, receives a directory even though it currently has no contained CT3 elements.

# Example — Information Architecture

Apply the same representation where the governed Information Architecture establishes Topic containment.

Conceptually:

`IV.1.1.x [Topic] Care`

- `[Topic] Care`
- `IV.1.1.x.x [Topic] Encounter`
  - `[Topic] Encounter`
- `IV.1.1.x.x [Topic] Episode`
  - `[Topic] Episode`
- ...

A leaf Topic still receives its own directory.

Do not infer new Topic containment merely for this projection.

Use the existing governed Information Architecture containment/navigation semantics.

# Scope of the Rule

Do not implement this only for the 218 canonical elements that exposed the original Archi parent-placement defect.

Those 218 elements identified a technical symptom: elements already acting as navigation parents were being separated by Archi.

The required convention is broader:

> Apply directory-plus-canonical-element representation consistently to every canonical architectural element participating in an established governed containment hierarchy.

This includes leaf members of those hierarchies.

# Contextual / Reference Navigation

Do not confuse contextual/reference navigation with canonical containment.

The previously agreed handling remains:

- contextual navigation may use folders;
- reference-only navigation may use folders;
- such folders may identify the canonical subject through metadata;
- they do not create another canonical element;
- they do not create a semantic containment relationship;
- they must not relocate the canonical element from its governed workbench home.

Business Behaviour Capability contexts are an example of contextual navigation and should retain their existing successful treatment.

Cross-Cutting References and historical/reference navigation should likewise remain reference navigation rather than being reinterpreted as canonical containment.

# Native ArchiMate Layers

Continue to respect native ArchiMate layers.

Do not change an element's ArchiMate type or layer to make navigation prettier.

Continue the bounded projection-specific treatment already established for cases such as the four ValueStreams.

A contextual Business navigation reference to a Strategy-native ValueStream does not change the ValueStream into a Business element.

# Do Not Change

Do not change:

- Architecture Continuum semantics;
- Canonical IDs;
- element names;
- native ArchiMate types;
- semantic relationships;
- governed containment semantics;
- RAD hierarchy;
- Manifest hierarchy;
- Business Behaviour allocation;
- Stakeholder associations;
- historical standing;
- authored Views or their organisation;
- accepted 1.1.11/1.1.12 evidence;
- iteration 001 diagnostic evidence.

Do not add semantic relationships merely to produce directories.

Directories are workbench organisation, not architectural semantics.

# Workbench Usability Objective

The resulting Archi tree should make this operation natural:

1. navigate to an architectural element;
2. expand its directory;
3. see the canonical element;
4. add a new contained element beneath that location when architecture development requires it.

The Solution Architect should not have to reorganise an existing element simply because it changes from a leaf to a parent.

This is a deliberate usability requirement for Archi as the primary architecture development workbench.

# Verification

Regenerate the 1.1.13 candidate projection and import it into an isolated Archi 5.10 workspace.

Verify specifically that:

- every governed contained CT1 has a directory containing its canonical CT1 element;
- every governed contained CT2 has a directory containing its canonical CT2 element, including leaf CT2s;
- every governed contained CT3 has a directory containing its canonical CT3 element, including leaf CT3s;
- Features follow the applicable governed containment semantics;
- governed Topic containment uses the same directory-plus-element convention, including leaf Topics;
- other architecture areas use the convention wherever governed containment exists;
- elements not established as contained are not arbitrarily forced into invented containment;
- contextual/reference folders remain distinguishable from canonical containment;
- no canonical elements return to unexplained native-root locations;
- no duplicate canonical elements are introduced;
- all semantic relationships remain unchanged;
- all 1,209 Views and their organisation remain unchanged;
- native-layer handling remains correct;
- first-pass and second-pass Archi imports remain stable;
- maintained EA and Java architecture tests pass.

# Solution Architect Review

Do not complete the HCRL after automated verification alone.

Provide the regenerated Open Exchange projection and representative inspection evidence, then stop for Solution Architect visual review of the actual Archi workbench.

The review should include at minimum:

- Enterprise Capabilities;
- Business Capabilities;
- Business Enabling Capabilities;
- a CT1 → CT2 → CT3 path;
- a leaf CT2;
- a leaf CT3;
- Application Information Architecture / Topics;
- a parent Topic;
- a leaf Topic;
- Business Behaviour;
- Motivation;
- the ValueStream/native-layer case;
- Cross-Cutting References.

Do not close 1.1.13 until that review is complete.[@hcrl-1.1.5-002.pending.md](file:///home/hunterm/Development/Code/harmonia/enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md)
