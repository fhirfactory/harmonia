<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Stakeholder, Journey, UseCase and UserStory representation

## Authority and standing

The Solution Architect's [1.1.21 ADD Task](../design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T104352Z-stakeholder-journey-usecase-userstory-add.md)
establishes the semantics below and authorises substantive incorporation and
evidence-based modelling judgement. The [pending HCRL](../design/review/1.1.21/hcrl-1.1.21-001.pending.md)
records actual incorporation and outstanding review. This profile implements the
issued decisions; it supplies no final acceptance of the resulting instances.
The accepted predecessor is 1.1.20. Its identities, assertions, qualifications,
Views and immutable publications remain preserved.

AX-01 retains health-information purpose. AX-04 separates semantic progression
from execution machinery; AX-07 separates participation from authorisation.
AX-08 retains meaningful source contributions. AX-14 distinguishes perspectives,
needs, interactions, progression, structure and occurrences. AX-15/AX-17 preserve
uncertain outcomes, source qualifications and unestablished coverage. AX-18
prevents Journey coordination or contextual participation from creating domain
authority. No runtime responsibility, executable workflow or deployment is added.

## Stakeholder perspectives and participation

Use native Stakeholder for the accepted perspectives: Participant; Patient;
Practitioner; Allied Health; Nurse & Support; Diagnostics; Operations (Facility);
Administrators (Facility); Executive; Regulatory / Government; Researchers;
Operations (System); Administrators (System); Responsible Person; External
Organisation; Autonomous System. A perspective is not a Person superclass,
persona or organisational role hierarchy. Family/carer contexts use Participant
and Responsible Person rather than a seventeenth perspective.

Responsible Person means recognised responsibility for, or representation of,
a Patient or Participant. Recognition alone grants no permission. External
Organisation participates in its own organisational capacity. Autonomous System
requires independently meaningful participation by a computational entity;
ordinary automated processing alone does not establish that participation.

Use native Association from Stakeholder to Journey for contextual participation.
Optional `participationCharacteristics` are Initiator, Participant, Beneficiary
and Observer; any supported combination, including none, is valid. These are
properties of the contextual assertion, not relationship types or stakeholder
subclasses. Projection preserves them as `Journey / Participation characteristics`.
Accountability remains an independent governance concern. No participation edge
grants access or determines progression. Older broader/historical Stakeholders
retain their identities and standing; index presence does not make them additional
members of the accepted sixteen-perspective catalogue.

The [bounded Patient follow-up](../design/evidence/1.1.21/001/before/ai-agent-tasks/20261110T215321Z-bounded-patient-and-transport-follow-up.md)
reconciles the historical **Care Recipient** definition with the current Patient
perspective. Established identity `5f6d333e94634420b8a8bbf8b9cb6e2a` now carries
that catalogue perspective and the current participation assertion. Its original
privacy association remains historical. Duplicate unaccepted allocation
`ad2012d59bb542e8bc7d8bd20547e342` is reserved as superseded history through the
existing correspondence/inverse-predecessor mechanism, with a qualified display
name and historical navigation in the Stakeholder index. This is definition/scope
reconciliation, not equivalence inferred from matching names. The broader
**Patients & Care Recipients** group, which includes families and communities,
remains distinct. The catalogue therefore reuses both Patient and OA&M identities;
its other fourteen members use the new allocations.

## Journeys

Journey specialises native ValueStream. Identity follows meaningful progression
and intended outcome. A definition may deliver value, enable another outcome,
fulfil a clinical/regulatory obligation or describe necessary operational work;
direct stakeholder value and an identified beneficiary are not mandatory fields.
Do not manufacture either. Preserve the native strategic progression meaning;
do not recast an arbitrary function or runtime task as a Journey.

A stage is another Journey. Explicit Composition represents genuine exclusive
structural constitution; Aggregation represents independently reusable/shared
constituents. Decomposition is recursive with no prescribed depth. Structural
cycles are invalid. Triggering can describe meaningful temporal/causal progression
or dependencies; it does not prescribe execution orchestration. Conditional,
optional, repeated, concurrent and asynchronous progression, relevant events and
alternative outcomes belong in the definition and relationship documentation.
Actual occurrence states and outcomes remain outside reusable definitions.

The established Application DataObject [Journey](../continuum/application/data-object/359b901df27f4b43b5a2ff14a8484e34.md)
represents observed progression reconstructed from execution evidence. It retains
that identity, its Energeia responsibility, source boundaries and qualifications.
The strategic Journey definition specialisation is a different architectural
construct. No retyping, identity equivalence or definition-to-occurrence
instantiation contract is inferred between them.

`journeyDefinition` retains intended outcome and relevant definition-level
conditions; it is projected as `Journey / Definition`. It creates no kernel type.
The inherited four Value Stream names remain unchanged when classified as
Journeys; their canonical and Exchange identities are preserved. No new formal
Journey naming token is established. Missing formal vocabulary remains explicit.

## Independent requirements and naming

UseCase and UserStory specialise native Requirement and retain separate identities.
A UseCase expresses an interaction directed towards a result. A UserStory expresses
a stakeholder need, expectation or desired outcome that can span interactions,
system/non-system activities, functional/non-functional concerns and multiple
Journeys. Execution of interactions is not proof of satisfaction of the story.
Neither construct requires the other or a Journey; all associations are optional
and many-to-many. Zero associations is not a coverage defect.

The accepted [predecessor naming decision](../design/register/1.1.20/hcrl-1.1.20-001.complete.md#dr-3)
uses SolutionFacet context. For these requirement specialisations, retain the
explicitly reconciled primary `naming.solutionFacetCanonicalId`, a local semantic
`naming.localName` and the existing facet's verified programmatic friendlyName.
The requirement friendlyName is that existing friendlyName followed by the
PascalCase local component. This is explicit requirement classification/naming
context; no Composition, Home, ownership or naming ancestry is inferred from it.
`canonicalName` retains independent formal semantics. Where the facet's formal
vocabulary is unestablished it remains null with that reason; no UC, US, Journey
or SolutionFacet qualifier is invented. Display uses `[UseCase]` / `[UserStory]`.

Reconcile source-plus-kind contributions before allocating identities. Semantic
equivalence needs matching meaning and scope, not equal text. Preserve independent
source constraints, source identifiers (including irregular ones), missing actors,
release/satisfaction qualifications and the historical assessment. Ingesting a
source's interaction does not require also creating a story, or vice versa.
Source acceptance scenarios remain source evidence, not implementation proof.

## Native relationships and coverage

Capability/Feature contributions use endpoint-valid Serving towards the Journey
when they provide its enabling ability. Explicitly contextual required/contributing
meaning belongs in documentation. Use Association for independent requirement to
Journey, UseCase to UserStory and other justified cross-layer traceability where
stronger native meaning is not established. Do not equate traceability with
Realization, satisfaction, ownership or complete implementation coverage.

**Solution Architect disposition, 2026-11-10T10:47:35.537Z:** use directed
Association with explicit constraint meaning for NFR → Journey. The native
endpoint table permits Association, not Influence, from Requirement to ValueStream.
The attributable follow-up is preserved in the ADD Task. Native Influence remains
appropriate wherever both endpoint applicability and its actual semantics hold;
do not reverse an edge merely to satisfy the endpoint table. Functional
requirements may likewise associate explicitly with the behaviour/progression
they constrain. Requirements retain independent applicability elsewhere.

Coverage is evidence-based and partial where appropriate. Historical Capability
successor correspondence supplies navigation to investigate, not automatic
equivalence of responsibility or coverage. Source-specific future scope is not
a commitment to implement it in 1.1.21. Reader/workbench structural review, actual
Archi transport and final HCRL confirmation remain separate checkpoints.
