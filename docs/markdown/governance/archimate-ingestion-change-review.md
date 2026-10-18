<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate Ingestion Change Review contract

## Purpose and authority

An ArchiMate Ingestion Change Review records independently adjudicable
architectural changes detected from an incoming ArchiMate model, provides the
explicit Solution Architect adjudication required before those changes may affect
the governed Harmonia architecture, and records the resulting reconciliation
instructions and execution evidence.

Detection does not imply acceptance. Acceptance does not require the executing
agent to reinterpret the architecture. Once adjudicated, an accepted Change
Request (CR) becomes an **authorised executable reconciliation contract** for that
architectural change. The agent executes an architectural decision; it is not
delegated authority to invent or re-adjudicate that decision.

This contract governs the transaction between detection and authorised
reconciliation. It establishes no reconciliation engine, automatic inference,
identity allocation, View generation or layout implementation, and accepts none
of the current blind-test proposals.

The [Architectural Axioms](architectural-axioms.md), repository
[authority rules](../../../AGENTS.md#1-architectural-authority) and applicable
accepted architectural decisions continue to govern. In particular:

| Authority | Application to this contract |
| --- | --- |
| [AX-04](architectural-axioms.md#ax-04) | Detection and execution machinery preserve Harmonia semantics; tool behaviour does not decide meaning. |
| [AX-14](architectural-axioms.md#ax-14) | Evidence, interpretation, decision, instruction and processing outcome remain distinct, as do semantics, navigation, presentation and identities. |
| [AX-15](architectural-axioms.md#ax-15) | Uncertain execution effects remain explicit until evidence establishes the outcome; a failed command alone does not establish non-occurrence. |
| [AX-17](architectural-axioms.md#ax-17) | Missing architecture is raised for Solution Architect adjudication; agents cannot fill gaps through convention or implementation convenience. |

These applications preserve the axioms without changing subsystem or runtime
authority. Existing [identity and naming](canonical-identity-and-naming.md),
[navigation and View](archi-navigation-and-view-convention.md),
[mapping](rad-archimate-model-mapping.md) and
[release](architecture-release-policy.md) governance remain authoritative in
their respective roles. A review cannot override higher authority; a conflict
SHALL be recorded and raised before dependent execution.

## Lifecycle and transaction boundary

```text
ArchiMate Export
    -> Detect Incoming Architectural Changes
    -> Decompose into Architectural Change Requests
    -> Generate Ingestion Change Review
    -> Solution Architect Adjudication
    -> Produce Authorised Reconciliation Instructions
    -> Execute Approved Changes
    -> Validate
    -> Record Reconciliation Outcome
    -> Publish
```

The review SHALL retain its detection basis, decomposition, decisions,
instructions and subsequent evidence throughout this lifecycle. Only authorised
changes whose execution and applicable validation have been established may
proceed to publication under existing publication/release gates. Reject,
On-Hold and Manual Intervention CRs remain in the record. The pipeline is not
permission to execute every CR or to publish an unresolved dependent change.

## Change Request granularity and identity

A Change Request represents an **independently adjudicable architectural change**,
not an XML difference, Open Exchange record, GUI editing action or file delta.
Decomposition SHALL operate at architectural adjudication granularity, rather
than use a one-record-one-CR algorithm.

One Archi editing activity may yield one CR, several independently adjudicable
CRs, or several pieces of evidence supporting one CR. Multiple Open Exchange
differences may collectively evidence one change. Evidence may include element
records, semantic relationships, organisation references, View nodes, visual
connections, geometry/presentation, Properties and Documentation. These are not
automatically separate CRs. Conversely, proposals that can reasonably receive
independent dispositions SHALL NOT be silently bundled.

For example, adding a Business Process, relating it to a Feature, assigning a
Harmonia-specific interpretation to that relationship, and adding a Flow to
another behaviour may require four decisions, despite arising from one activity.
The review SHALL explain the decomposition and map relevant observations to CRs,
including shared evidence and observations retained only as transport findings
or unresolved evidence. Raw difference counts are not architectural change counts.
Unknown GUI edit history SHALL remain unknown.

Each CR SHALL have a review-local sequential identifier: `CR-001`, `CR-002`,
`CR-003`, and so on (minimum three digits). Allocate once within that review;
do not renumber or reuse an allocated identifier. It remains stable through
review, adjudication, reconciliation, validation and audit. Corrections or later
splits SHALL retain the original record and explain links to any new CRs.
Cross-review references SHALL identify the review/run as well as the CR.

A CR identifier is process/review identity only. It is not a Harmonia Canonical
ID, SHALL NOT become architectural identity and SHALL NOT supply an identity
binding, alias or governed name.

## Review format

Every review SHALL begin with this compact Markdown summary table as its first
visible content, before review metadata and detailed CRs:

```markdown
| ID | Brief Description | Disposition | Outcome |
| --- | --- | --- | --- |
| [CR-001](#cr-001--add-identifier-capture-feature) | Add Identifier Capture Feature | On-Hold | Unresolved |
| [CR-002](#cr-002--add-identifier-verification-business-process) | Add Identifier Verification Business Process | On-Hold | Unresolved |
```

These rows illustrate format only and submit or accept no blind-test CRs. Each ID
SHALL link to the corresponding detailed section in the same document. Explicit
Markdown HTML anchors MAY keep links stable through description edits. The table
is an index/dashboard, not the CR. Its Disposition and Outcome SHALL match the
corresponding separate fields in the detailed CR.

The Brief Description SHALL be one concise phrase describing the architectural
change, with a deterministic maximum of **80 characters**, including spaces.
It SHALL NOT contain supporting evidence, XML identifiers, rationale,
adjudication questions or implementation detail. Detail belongs in the CR, which
may occupy as many pages as required.

After the table, record the review/run identifier, author/date, incoming artefact
and SHA-256, governed comparison baseline/release and SHA-256, relevant transport
control, detection methods/tool versions and evidence locations. Record unavailable
snapshots or uncertain provenance explicitly; do not reconstruct them as observed
input. Record how observations were decomposed into CRs.

The [governed template](templates/archimate-ingestion-change-review-template.md)
demonstrates the standard structure. Each detailed CR SHALL distinguish:

1. **Proposed Architectural Change:** the candidate meaning and bounded scope.
2. **Detected Evidence:** actual observations, exact source locators and references
   to preserved evidence, including before/after records where available.
3. **Disposition:** the Solution Architect call to action, or explicit initial/default
   On-Hold pending adjudication; it is distinct from the resulting state.
4. **Architectural Questions / Adjudication:** questions and the separate Solution
   Architect decision, rationale, authority reference and adjudicator/date.
5. **Outcome and Outcome Reason:** resulting CR state and the evidence-referencing
   explanation establishing it, independently of Disposition and processing.
6. **Related Change Requests:** dependencies or other material connections.
7. **Authorised Reconciliation Instruction:** precisely what execution is permitted.
8. **Reconciliation / Processing Record:** what subsequently happened and its evidence.

Use these headings where useful. Simple CRs need no meaningless empty subsections:
state that existing authority resolves the question or that there are no related
CRs inline where appropriate. All eight distinctions SHALL remain ascertainable.
New CRs SHALL start with `Disposition: On-Hold`, `Outcome: Unresolved`, an Outcome
Reason stating the outstanding question/authority/evidence, an instruction of
**Not authorised** and a processing record of **Not yet processed**. Initial/default
On-Hold supplies no fabricated Solution Architect adjudicator or decision date.

## Solution Architect dispositions

**Disposition is the Solution Architect's authorised call to action to the
processing agent concerning the CR.** It records what the architect instructed,
not the eventual lifecycle result. Exactly four Disposition values are governed:

| Disposition | Instruction and execution consequence |
| --- | --- |
| **Accept** | Approve the bounded architectural change and permit reconciliation only to the extent explicitly described by the CR and its decision-linked Authorised Reconciliation Instruction, subject to prerequisites and applicable governance. It does not assert execution, validation or resolution. |
| **Reject** | Do not reconcile the proposed change as accepted architecture. Preserve the proposal, evidence and decision. This instruction does not by itself establish the CR's Outcome. |
| **On-Hold** | Do not progress the architectural proposal pending further authority/resolution; reconciliation is not authorised. This is the initial/default Disposition for every new CR and may also be explicitly issued by the Solution Architect. It can remain recorded after a subsequent governing decision resolves the question. |
| **Manual Intervention** | Direct the case to deliberate human-controlled resolution; automated reconciliation SHALL NOT execute this CR. This is an instruction, not evidence that manual resolution has occurred. |

Resolved, Superseded, Implemented and other lifecycle-result labels SHALL NOT be
added to this Disposition enumeration.

Only the Solution Architect adjudicates architectural disposition. An agent MAY
produce the resulting instruction from an unambiguous recorded decision, but
SHALL NOT supply missing architectural meaning. The decision and resulting
instruction SHALL together establish the exact authorised scope. Record the
instruction revision and its decision reference; drafting alone adds no authority.
If instruction generation requires another architectural decision, return that
question for adjudication. An issued Disposition is an adjudication/audit record.
Subsequent governing decisions, execution, validation or failure SHALL NOT silently
rewrite it. Only an explicit subsequent Solution Architect instruction may revise
an issued Disposition; retain the earlier instruction and decision with who changed
it, when and why. Record later resulting states through Outcome, Outcome Reason
and processing evidence. Keep prior adjudication, instruction and Outcome revisions
recoverable in the same run under the existing revision mechanism.

Acceptance of an architectural instance SHALL NOT automatically establish a
universal Harmonia metamodel, inference, naming, containment, reconciliation or
publication rule. Such a general rule requires explicit architectural/governance
adjudication in its own right.

## Outcome and Outcome Reason

**Outcome** records the resulting state of the CR after adjudication and any
subsequent architectural or processing activity. It is independent of Disposition;
no Disposition uniquely determines an Outcome. The minimum governed vocabulary is:

| Outcome | Meaning |
| --- | --- |
| **Unresolved** | Available authority/evidence has not yet established resolution of the CR's bounded question/change. Pending decisions, incomplete required processing/validation and uncertain effects remain explicit here and in their evidence. |
| **Resolved** | Referenced architectural authority or processing/validation evidence establishes resolution of the CR's bounded question/change. Resolution can occur without reconciliation, including resolution of an interpretation finding by a governing metamodel decision. |

These are CR Outcomes, not execution-result codes. Existing PASS, FAIL, TIMEOUT,
STALLED and UNRESOLVED command/verification results keep their existing meaning
in processing records. They do not automatically assign a CR Outcome. No further
Outcome values are required for this increment's evidenced lifecycle cases.

**Outcome Reason** SHALL be recorded separately and explain why that Outcome is
established or what remains unresolved. Reference the governing decision,
architectural authority, processing evidence or validation evidence as applicable.
It is descriptive, not a closed enumeration. Record the actor/date and basis for
an Outcome update through the review revision/audit evidence; retain prior values
and reasons. An agent may record a deterministically evidenced Outcome but SHALL
NOT supply missing architectural meaning or invent a Solution Architect instruction.

For example, a valid finding initially held because a metamodel rule was missing
may retain `Disposition: On-Hold` while acquiring `Outcome: Resolved` and
`Outcome Reason: Resolved by Governing Decision <authority reference>`. Its original
call to action remains correct and auditable. Resolution does not accept a related
submitted relationship or authorise its reconciliation. An Accept instruction may
instead have Outcome Unresolved while its bounded execution or validation remains
outstanding. Outcome SHALL NOT be collapsed into the Reconciliation / Processing
Record or treated as reconciliation authority.

**Solution Architect governance decision — 2026-10-19 (Australia/Sydney), release
1.0.13:** this distinction resolves the record-handling gap exposed by CR-016–CR-021
in [1.0.8/run-001](../../archimate/processing/ingestion/1.0.8/run-001/change-review.md#disposition-outcome-transition).
Their common question was resolved by the separate
[CT3 → Feature Composition rule](../03-business-architecture/metamodel/business-architecture-metamodel.md#bec-composition-hierarchy)
established in release 1.0.12. The four previously labelled Status values are now
explicitly Dispositions; historical review/detection/template representations
remain unchanged in retained history. This semantic transition does not change
any issued call to action or adjudicate an unaccepted instance.

## Architectural questions and separate interpretations

Observed evidence SHALL NOT be rewritten after adjudication to make the incoming
model appear to contain the accepted interpretation. Keep **Observed Evidence**,
**Candidate Architectural Interpretation**, **Solution Architect Decision** and
**Authorised Reconciliation Instruction** distinguishable, rather than blending
them into one narrative.

The current blind-test pattern motivates the distinction without deciding it:

```text
Business Process "Identifier Verification"
    -> Realization ->
Feature "Identifier Capture"
```

The explicit native Realization is observed evidence. Whether it also establishes
logical containment/context of the Business Process by the Feature is a separate
Harmonia question. Its existence alone supplies no answer. If independently
adjudicable, a possible decomposition is:

```text
CR-007  Add Identifier Verification Business Process
CR-008  Add Realization to Identifier Capture
CR-009  Treat Realization as establishing logical Feature context
```

These are illustrative review identities only, not generated or adjudicated CRs.
Each could receive its own disposition. This contract establishes neither that
interpretation nor an ArchiMate Composition to represent it. Accepting a specific
Process A → Realization → Feature B, and separately accepting context for that
instance, would not imply that all such Realizations establish logical context.

## Executable reconciliation instructions

A CR with Disposition Accept SHALL contain sufficient unambiguous reconciliation instruction
and source/target evidence for an authorised execution agent (Codex, Junie or
another authorised repository agent) to implement the approved architectural
change without re-adjudicating its architectural meaning. The instruction SHALL
state **what is authorised**, rather than merely repeat an incoming observation.

Include, where relevant to deterministic execution:

- affected elements and exact source evidence/governed target locators;
- affected semantic relationships, their identities, source, target and direction;
- approved native type, Harmonia classification and bounded semantic interpretation;
- identity/naming treatment already governed or separately authorised;
- required model organisation/navigation and authorised View treatment;
- constraints on what must not change, and the expected resulting state;
- prerequisite CRs and the governed state their reconciliation must establish;
- validation criteria/evidence sufficient to establish that the instruction was met.

Every field is not required for every CR. Sufficient precision is required.
Missing metadata is a finding, not permission to allocate identities or invent
names/context. Cite existing authority for deterministic governed treatment;
where it is insufficient, obtain adjudication before execution.

**Form-only example; not a decision on the blind-test relationship:**

```text
Preserve the native ArchiMate Realization relationship from
Business Process "Identifier Verification" to Feature "Identifier Capture".
For this accepted architectural instance, treat the Realization as establishing
the governed Feature context authorised by this Change Request.
Do not create an ArchiMate Composition relationship.
Apply only identity, naming, organisation and publication rules independently
authorised by applicable governance or related accepted Change Requests.
```

An actual instruction also needs the precise evidence/target references and
validation criteria relevant to its instance. The example grants no authority.

An execution agent processing a CR with Disposition Accept SHALL NOT broaden, reinterpret or
improve the authorised architectural decision. Execution-time convenience confers
no architectural authority. Before mutation, check the decision-linked instruction,
evidence/baseline applicability and prerequisites. If ambiguity or changed target
state prevents deterministic implementation, stop affected execution, record the
question/effects, and return the CR for further Solution Architect adjudication
or designation as Manual Intervention, as appropriate. The agent SHALL NOT
invent an answer or change disposition on its own. Preserve uncertainty about
partial execution under AX-15; do not report a presumed success or non-occurrence.

## Manual intervention and dependencies

Disposition Manual Intervention deliberately requires **no automatically executable
reconciliation instruction**. Instead record why automation is unauthorised or
insufficient, the deliberate human-controlled action required to the extent
known, and how completion can be evidenced and validated. Unknown details remain
explicit. Record the actual human action and subsequent validation in the
processing record. Do not silently turn that disposition into an automated
operation. Any later authorisation for automation requires explicit Solution
Architect re-adjudication with retained history.

Related CRs SHALL distinguish an informational connection from an execution
prerequisite. A prerequisite entry SHALL identify the CR and the exact governed
state required. For example, adding a relationship may depend on successful
reconciliation of the CR adding its source Business Process, and on an established
target. Acceptance alone is not proof that either endpoint exists.

An agent SHALL NOT execute a dependent CR if the required proposal's Disposition
is Reject or On-Hold, if required Manual Intervention remains incomplete, or if
reconciliation has failed or otherwise not produced the required governed state
and evidence. Outcome Resolved alone supplies neither that resulting state nor
reconciliation authority; resolution of an interpretation question cannot stand in
for the actual governed elements/relationships a dependent operation requires.
Completed manual resolution can supply that state when evidenced and validated;
it does not authorise automatic execution of the manual CR. Missing, contradictory
or circular prerequisites SHALL be raised before dependent execution. This is a
dependency contract, not a workflow engine or complete scheduling model.

## Semantic relationships, navigation and Views

The [Increment 2B ingestion/publication semantics](archimate-ingestion-publication-semantics.md)
govern recognition of classifications, semantic naming context, machine metadata,
relationship reuse/duplicate comparison, source-based navigation and View
representation after acceptance. Established rules may be applied during
detection; unresolved architectural interpretations still require this contract's
adjudication. Publication rules never rewrite observed submission evidence.

Detection and instructions SHALL distinguish creation of a semantic relationship,
modification/removal of a semantic relationship, and another visual appearance of
an already-existing semantic relationship. Resolve a visual connection's
`relationshipRef` against the governed baseline and incoming semantic records;
record its semantic and View-local endpoints separately. Adding a connection
referencing an existing governed Composition does not create another Composition.

The existing [navigation/View convention](archi-navigation-and-view-convention.md)
and [identity contract](canonical-identity-and-naming.md) govern these boundaries:
folder placement and View placement are not semantic containment; a visual
connection is not itself a semantic relationship; Open Exchange representation
identity is not governed architectural identity. Do not manufacture semantic
edges, governed names or context from geometry, nesting, folders or labels.

## Processing record and audit evidence

Disposition, Outcome, Outcome Reason and processing state SHALL remain separate.
A CR with Disposition Accept might await execution, reconcile successfully or
unsuccessfully, await validation or be validated without changing that issued
Disposition. Processing records say what occurred; Outcome and Outcome Reason
say what ultimately happened to the bounded CR and why. The architect's instruction,
actual processing, resulting CR state and its explanation SHALL all remain
ascertainable. This contract defines no complete processing-state vocabulary.

The Reconciliation / Processing Record SHALL be capable of retaining the exact
instruction revision used, actor/date, automatic or manual handling, prerequisite
observations, before/after artefact references and hashes, actual changes,
commands/results, validation criteria/results, unresolved effects and publication
reference when applicable. Record unsuccessful attempts and deviations explicitly;
they do not extend authority. Subsequent corrections SHALL preserve prior evidence.

A future reviewer SHALL be able to establish what was submitted, detected and
decomposed; the evidence/questions and independent decisions for each CR;
dependencies; exact authorised reconciliation; automatic/manual handling; actual
changes; how the resulting state was validated; and the distinct Outcome and
Outcome Reason. CRs with Disposition Reject, On-Hold or Manual Intervention remain
auditable, including after resolution without reconciliation. Release and publication gates remain separate from
architectural acceptance and from validation success.

## Run placement and template

Use the deterministic run structure in
[ingestion processing](../../archimate/processing/ingestion/README.md#run-layout).
Keep Change Reviews with detection, reconciliation and validation evidence,
separate from immutable incoming exports and published packages. The reusable
[template](templates/archimate-ingestion-change-review-template.md) is governed
here in RADS; individual run records are scoped adjudication/execution evidence,
not a second architectural repository. Approved changes enter RADS through their
authorised instructions.

## Empirical motivation and bounded deferrals

The three observation-only Increment 2 reports informed this mechanism:

- [First blind analysis](../../../.junie/reports/2026-10-18-archimate-increment-2-blind-analysis-1.0.8.md): element additions, incomplete metadata and navigation evidence do not establish acceptance or canonical identity.
- [Updated blind analysis](../../../.junie/reports/2026-10-18-archimate-increment-2-updated-blind-analysis-1.0.8.md): explicit Composition records, relationship homes and View edits are different observations, with independent interpretation questions.
- [Third blind analysis](../../../.junie/reports/2026-10-18-archimate-increment-2-third-blind-analysis-1.0.8.md): Business Services/Process, Realization/Flow/Serving, new Views, connections to new relationships and connections to existing governed relationships require distinctions beyond XML delta counts. Missing metadata, interpretation questions and the unavailable exact second XML remain explicit limitations.

These reports supply empirical evidence only and remain unchanged; no observed
pattern becomes architectural authority. Automated detection/decomposition,
instruction generation, reconciliation machinery, complete processing states,
presentation replay/layout and any general Realization/context or Composition
profile decision remain separate work. This documentation change processes none
of the blind-test additions.
