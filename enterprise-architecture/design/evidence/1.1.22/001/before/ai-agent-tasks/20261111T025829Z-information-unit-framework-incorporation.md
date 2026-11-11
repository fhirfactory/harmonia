Task-Issued-At: 2026-11-11T02:58:29.162Z

Event source: Codex user response_item instruction-entry event; session 01a1c88a-df1e-7b12-9a68-6efa865cf212, message msg_01a1c88a-e8aa-7a73-b7c7-e7d14523925d

# Information Unit Framework — governed incorporation Task

Actor: Mark Hunter, Solution Architect (operative user instruction).
Event source: Codex session 01a1c88a-df1e-7b12-9a68-6efa865cf212, user response_item msg_01a1c88a-e8aa-7a73-b7c7-e7d14523925d; system timestamp recording instruction entry. Exact issued wording follows. This retained event/source removes any continuing local-log dependency.

Provenance boundary: prospective 1.1.21 → 1.1.22, HCRL 001; this locator allocates no release. The user expressly accepts the whole Final.001 DRP and authorises ADD/reconciliation; the DRP's earlier review-stage status is preserved as original text. Incorporated-result acceptance, human transport/structural checks and terminal actions remain separate.

## Operative issued instruction (unchanged)

# Harmonia — Information Unit Framework: Governed Continuum Incorporation

## Goal

Incorporate the accepted Information Unit Framework DRP into the governed Harmonia Architecture Continuum.

## Expected Outcome

The Architecture Continuum accurately represents the accepted Information Unit architecture, including its conceptual and logical models, metadata structure, identity, Topic and TopicMask semantics, relationships, versioning, operational contexts and applicable cross-component responsibilities.

Existing canonical architectural elements are reused wherever their semantics match. New elements or relationships are introduced only where required by the accepted DRP.

## Context

**Repository:** `fhirfactory/harmonia`

**Branch:** `Mneme-and-Mnemosyne-Build`

**Accepted architectural input:**

`enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md`

**Architecture authority:**

`enterprise-architecture/continuum/`

The DRP has been reviewed and accepted by the Solution Architect. Its IU-01 through IU-13 adjudications are complete.

This is an authorised architectural incorporation activity, **not a new discovery or design exercise**.

## Constraints

1. Treat the accepted DRP as the architectural specification for this increment.
2. Follow current repository architecture governance, naming, identity, reconciliation, publication and release procedures.
3. Preserve established canonical identities, classifications and relationships.
4. Reconcile the DRP against existing Continuum elements before creating new ones. This reconciliation is part of the authorised ADD operation, not a separate preliminary activity.
5. Reuse the accepted 1.1.20/1.1.21 Capability and Feature hierarchy and existing Motivation elements.
6. Preserve Information Unit atomicity and the exactly-one-InformationRepresentation invariant.
7. Do not introduce additional Information Unit representations, transformation management, automatic relationship synchronisation or universal lifecycle machinery.
8. Preserve existing Mneme, Mnemosyne, Themis, Kleio and Dokimasia responsibility boundaries.
9. Do not infer implementation Classes, Functions, Services or interfaces merely from conceptual decomposition.
10. Do not modify accepted architectural decisions to simplify modelling. Escalate genuine conflicts requiring Solution Architect adjudication.
11. Do not implement application code or expand the activity into other DRP consolidations.
12. Do not automatically commit, push or bypass acceptance controls unless authorised by current repository governance and the applicable release instructions.

## Authorised Activity / Scope

Perform the governed ADD/reconciliation necessary to incorporate the accepted Information Unit Framework.

This includes:

- Conceptual Information Unit architecture and corresponding logical Calliope model.
- Information Unit identity and its Topic-based canonical naming.
- InformationRepresentation, ContainerMetadata, ContentMetadata and Content.
- ManagementContext, InformationCoordinates, SecurityContext and LifecycleContext.
- Topic and TopicMask relationships and applicable semantic references.
- Operational Version, Active Generation and Content Structure Version.
- Optional Information Unit relationships and local/remote reference semantics.
- Applicable Capability, Feature, Requirement and cross-layer relationships.
- Architectural boundaries and relevant dependencies involving Mneme, Mnemosyne and associated governance components.

Where related DRPs contain overlapping proposals, reconcile the relevant architectural semantics without treating those drafts as authority over the accepted Information Unit DRP.

Use the established Architecture Continuum round-trip, validation and publication mechanisms.

Exercise independent judgement in determining the appropriate architectural representation. Do not mechanically create one element for every heading, property or conceptual label.

## Report

Record governed task inputs and execution evidence using the repository's established evidence structure:

`enterprise-architecture/design/evidence/<version>/<iteration>/before/`

`enterprise-architecture/design/evidence/<version>/<iteration>/after/`

The execution report must identify:

- The architecture baseline and version used.
- Existing elements and relationships reused.
- Elements and relationships created or modified.
- Reconciliation of historical candidate Capabilities, Features and Requirements.
- Any genuine unresolved conflicts or gaps.
- Validation and round-trip results.
- Publication and release status.
- Any qualifications requiring Solution Architect attention.

Do not place the governed execution report under `.junie/`.

**Completion criterion:** The accepted Information Unit architecture is represented consistently in the governed Continuum, with traceable reconciliation and validation evidence, subject to the established acceptance and release controls.