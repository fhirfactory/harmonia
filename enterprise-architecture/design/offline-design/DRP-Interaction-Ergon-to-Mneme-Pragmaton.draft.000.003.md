<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal (DRP)

**DRP:** DRP-Interaction-Ergon-to-Mneme-Pragmaton-001\
**Title:** Interaction::Ergon-to-Mneme — Pragmaton\
**Status:** Draft.000.003 · Pending Solution Architect review\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Basis:** Architecture 1.1.26 · Software 0.1.0 / Iteration 005 / G03  
**Lineage:** [Interaction Draft.002][previous] · [Pragma Model][pragma-model] · [Pragmaton Model][pragmaton] · [Publication/subscription][publication] · [DR-12][review]

## 1. Feature Realisation

This interaction makes governed information available for a particular
realised undertaking, returns its working changes and enables recovery of
the information required to execute it.

### 1.1 New or Updated Feature Details

Reuse the accepted **Pragmaton Context Integrity** capability and
**Interrupted Work Context Recovery** Feature. The execution-facing
interaction uses the existing governed information-access and persistence
responsibilities. [Context integrity][context] · [Context recovery][recovery-feature] · [Mneme–Mnemosyne interaction][persistence]

### 1.2 Associated Requirements

- Mneme SHALL contextualise information and preserve its applicable identity,
  version and authority before it is exposed as working information.
- ContextLoader SHALL establish initial processing context with Mneme.
  ProcessingEngine SHALL operate through `Ergon.WorkSpace`.
- Initial and later access SHALL use the applicable Themis enforcement
  boundary. Additional access through the Ergon-InformationAdapter SHALL
  remain within Mneme's governed access contracts.
- Context Unloader SHALL return working changes through governed Mneme save.
  Changed values, newly generated outputs and work outcomes SHALL remain
  distinguishable.
- Recovery SHALL use durable work and governed information at the existing
  execution boundary. Execution SHALL NOT resume inside a failed Ergon.
- The interaction SHALL preserve applicable security, provenance and audit
  context, and report indeterminate outcomes explicitly.

### 1.3 Associated Strategy and Motivation

AX-04 keeps associative access within Harmonia's governed semantics.
AX-05, AX-06 and AX-09 distinguish working state and its source versions from
authoritative durability. AX-07 governs every protected access, and AX-08
governs evidence of meaningful events. AX-13 preserves the Pylai boundary.
AX-14 and AX-15 separate outcomes and preserve uncertainty under
REQ-FND-004. AX-17 governs incorporation and unresolved contract
correspondence. [Canonical Principles][axioms] · [Explicit Indeterminate Outcome][indeterminate]

## 2. Proposal

Establish the governed interaction through which an Ergon accesses a
Mneme-managed Pragmaton during a realised undertaking and returns its working
results. **Pragmaton** is the execution-information environment.
**`Ergon.WorkSpace`** is the Ergon-facing access abstraction for that same
environment. It exposes both initially contextualised and subsequently
acquired working information. [Pragma Model][pragma-model] · [Pragmaton and WorkSpace][pragmaton-workspace]

## 3. Design Context

The accepted Mneme–Mnemosyne information-access, persistence and two-cache
contracts govern this interaction. G01 records the Solution Architect's
Ergon authorisation decision. The Pragma, Pragmaton and publication/subscription
drafts record design agreement for this review. Their canonical correspondence
and incorporation remain pending. [Five-contract interaction][persistence] · [Two-cache architecture][caches] · [IU authority][iu] · [G01][security] · [DR-12][review]

## 4. Architectural Definition

### 4.1 Establishment

The Ergon executes a particular subscriber-specific undertaking, identified
by its Pragmatosis (or Noesis for a realised Stimulus). Mneme retains
information management of the originating Pragma and its realisation, owns
Pragmaton contextualisation, and coordinates governed access and persistence.
Common Ergon owns its execution-side context loading and unloading,
checkpointing and recovery. Mnemosyne establishes authoritative state
through the governed Mneme interaction. [Pragma Model][pragma-model] · [Two-cache architecture][caches] · [Five-contract interaction][persistence]

`Ergon.ContextLoader` establishes and exposes initial WorkSpace with Mneme
for the identified undertaking. Mneme supplies and contextualises governed
information using the single Pragma input, expected information defined by
the applicable execution context, and current authorisation. The common
preferred path loads anticipated information before bounded processing.
Contextualisation preserves each source IU identity and applicable
authoritative version. An immutable input summary supplies reference and
lineage information. A full read uses the governed materialisation contract.
The single input cardinality leaves room for other working information in
Pragmaton. [Established Context Load and Loader roles][ergo] · [Pragma Model][pragma-model] · [IU authority][iu]

Digital Twin Behaviour may start with Twin-relative Stimulus/Noesis
information and uses fresh, authorised context per Behaviour under the
existing serialisation rule. Pylai ingress may establish initial context
from a Communication InformationUnit. Pylai ingress and egress retain their
specialised contextualisation and external boundary semantics, including
authoritative handover where an acknowledgement claims durable acceptance.
These entry points share the Pragmaton and WorkSpace responsibilities.
[Specialised entry points][pragmaton-entry] · [Publication/subscription processing environment][pub-processing]

### 4.2 Processing and associative access

`Ergon.ProcessingEngine` performs bounded activity through WorkSpace. The
JavaSpaces analogy describes identity-based and associative access by
relevant characteristics and relationships. It is an information-space
analogy only. WorkSpace supports a working set that may expand through
permitted resolution and acquisition during execution. The accepted cache
and governed access contracts continue to apply. [Associative information-space model][pragmaton-space] · [On-demand acquisition][pragmaton-access]

| Access case | Interaction meaning |
| --- | --- |
| Already contextualised value | Access the permitted working value through WorkSpace. Its lineage identifies the IU and version where applicable. Local absence describes the current Pragmaton. |
| Existing InformationUnit reference | WorkSpace may surface a lazy resolution request. The adapter mediates it through Mneme using the applicable version and authorisation constraints. |
| Additional governed information | The ProcessingEngine may request information beyond the initial set. The adapter mediates access or bounded discovery through the applicable existing Mneme contracts. Admission into Pragmaton or return as a bounded result depends on the governed interaction and disclosure decision. |

Lazy reference resolution and explicit ad hoc acquisition retain their
separate meanings. Bounded discovery preserves the accepted R1/R2
Summary-only search scope. Any subsequent Content access uses its own
governed read path. Criteria-driven dynamic Context determination remains
deferred. [Ergo contextualisation and ad hoc boundary][ergo] · [Ad hoc access candidate][adhoc] · [Five-contract interaction][persistence]

### 4.3 Ergon-InformationAdapter

The `Ergon-InformationAdapter` is the logical mediation role for additional
governed information access, including unresolved references. WorkSpace
surfaces the processing need. The adapter conveys the request and governed
outcome through Mneme under applicable trusted context and Themis controls.
Mneme owns retrieval, admission of working information and coordination
with Mnemosyne. Save and authoritative persistence follow their existing
owning interactions. Correspondence to existing application interfaces
remains open for review. [Pragmaton acquisition][pragmaton-access] · [G01][security] · [Five-contract interaction][persistence]

### 4.4 Workflow Cache

Mneme's Workflow Cache supports Pragmaton with transient, workflow-local
materialised working information. Preserve the accepted embedded Infinispan
LOCAL allocation in each applicable execution JVM. That allocation excludes
inter-JVM Workflow Cache replication and retains context validity, Themis
isolation and the MC-01–MC-07 qualifications. The separate Standard
Cache supports lightweight IU summary and reference access. Cache state
supplies active working information. Mnemosyne establishes authoritative
durability. [Workflow Cache][workflow] · [Two-cache architecture][caches]

For full reads, `GovernedReader` attempts Content materialisation through
Mneme/Mnemosyne subject to the established large-binary pass-by-reference
exception. A summary or an unresolved reference does not establish
materialised Content. Explicit version reads preserve the requested
version. An executing version-specific immutable input retains that version
when another authoritative revision is established. Recovery uses the
governed source versions and durable execution state described in §4.7.
[Five-contract interaction][persistence] · [IU authority][iu]

### 4.5 Security and evidence

Themis governs initial contextualisation, reads of protected working values,
lazy resolution and additional acquisition through the applicable PEP/PDP
mechanisms. Each governed operation requires applicable trusted identity
and authority under G01, including its processing identity and delegation
rules. A reference, cached value or earlier access decision grants no
independent authority for another operation. Ergon admission and release
retain their inbound and outbound enforcement decisions. [G01][security] · [Trusted transport question][dr7]

Preserve policy denial, known unavailability and indeterminate outcomes
where the owning contract distinguishes them. When authority cannot be
established, withhold access or release. Do not treat an unknown effect as
success or failure. Correlate material access, release and resulting
outcomes with the undertaking and source information for applicable
provenance, Kleio audit and Dokimasia Assurance. [Explicit Indeterminate Outcome][indeterminate] · [G01][security] · [Publication/subscription evidence][publication]

### 4.6 Completion and return

| Result | Governed handling |
| --- | --- |
| Changed existing working information | Context Unloader returns changes through the governed Mneme save interaction. Mneme determines the information-management consequences and invokes the authoritative path through Mnemosyne and the full Writer where required. |
| Newly generated InformationUnits | Attribute the Pragma Outputs to the producing undertaking. The separate PublishingMask process governs downstream publication and subscription. |
| Work execution outcome | Record the outcome against the realised undertaking. Progression or resolution of originating work follows its applicable outcome policy. |

Save processing and authoritative persistence have separate outcomes.
Local mutation and successful unload establish working-state facts only.
Authoritative mutation success requires the accepted durability and
version correspondence. Governed Context Save uses the existing writer
contracts. Raw cache write-through and Summary UPDATE are outside those
contracts. [Pragma Model][pragma-model] · [Publication flow][pub-output] · [Five-contract interaction][persistence] · [Workflow Cache][workflow]

### 4.7 Failure and recovery

The existing Ergon execution boundary governs recovery. An Ergon never
resumes internally after failure. Reconstruct the required Pragmaton and
WorkSpace from the applicable durable undertaking identity, governed
authoritative information and durable execution state or checkpoints.
Preserve the input-version basis and any applicable `DoNotBefore` constraint.
Identify missing, unrecoverable or uncertain information explicitly.
The underlying cache entries may be reconstructed or evicted. Cache
presence and transient checkpoints give no evidence of authoritative
durability. Redelivery and retry preserve the identity and lineage of
the same work obligation under the applicable work contract.
[Context recovery][recovery-feature] · [Two-cache architecture][caches] · [Pragma Model][pragma-model]

Keep work execution, information access, Mneme save, authoritative
persistence and Petasos delivery outcomes independently attributable.
Where an operation is indeterminate, use the applicable governed evidence
and reconciliation before deciding its outcome or any dependent retry.
[Explicit Indeterminate Outcome][indeterminate] · [Five-contract interaction][persistence] · [Publication/subscription reliability][pub-reliability]

## 5. Explicitly Not Prescribed

Interface signatures, data structures, associative lookup algorithms,
checkpoint representation and other implementation choices remain in
solution design under the existing contracts and accepted cache allocation.

## 6. Evidence and Rationale

The G03 evidence maps the source Draft.002 requirements, candidate decisions,
accepted contracts and historical discrepancies. It records the Petasos–Mneme
dependency and the decisions that still need adjudication.
[Interaction reconciliation][evidence] · [Earlier Pragma model reconciliation][pragma-evidence] · [Earlier interaction proposals][pub-evidence]

## 7. Relationship to Current Architecture

| Existing terminology | Correspondence for this candidate |
| --- | --- |
| Praxis Context requirements and Ergo expected resource set | Continue to define information expected by the activity. `Context` as the working-information environment corresponds conceptually to Pragmaton. Other uses of Context retain their established meanings. |
| Mneme Context Load and Ergo Context Loader | Mneme acquires and contextualises governed values under applicable Themis controls. The Ergon-side ContextLoader establishes the execution-facing context with Mneme and exposes WorkSpace through the common Ergo/Ergon framework. |
| Ergo Context Unloader and Mneme Context Save | Unloader crosses the processing boundary with working results and distinguishes new outputs. Mneme governs save and subsequent information consequences. |
| Governed ad hoc access | Retains the governed runtime acquisition role. Exact interface correspondence of lazy resolution and adapter mediation requires review. |

Preserve existing element identities and their accepted meanings during
any governed reconciliation. Review canonical names and relationships before
allocation. The WorkSpace relationship settles the earlier whole-Pragmaton-or-view
question at design level. [Ergo responsibility and terminology][ergo] · [Pragmaton and WorkSpace][pragmaton-workspace] · [Interaction reconciliation][evidence]

## 8. Conformance Expectations

Verify version-preserving initial load and local access, governed resolution
and additional acquisition, denied and indeterminate outcomes, recovery after
loss of local working values, and distinct attribution of changed information,
new output, work completion and durable effects. [Review cases][evidence]

## 9. Open Questions / Matters for Review

1. Confirm the existing Load/Save and Ergo/Ergon element correspondence and
   the adapter's relation to existing governed interfaces.
2. Establish contractual outcomes and recovery evidence when initial
   contextualisation cannot safely complete. Confirm the minimum source
   revision and checkpoint evidence needed to reconstruct eligible work.
3. Establish the permitted associative and additional-acquisition scope,
   result admission versus bounded return, policy-change handling and
   non-disclosure and unavailable outcomes.
4. Confirm the information and lineage needed at unload/save, including how
   to evidence working return, authoritative disposition and uncertainty across
   retries.
5. Confirm the formal Pragmaton association and lifecycle, and reconcile the
   downstream Petasos reference, acknowledgement and recovery correspondence.
   Prior DR-12 obligations carry forward. [Interaction reconciliation][evidence] · [DR-12][review]


## 10. Continuum Update

The entries below identify candidate Continuum correspondence for adjudication
before any governed incorporation.

### 10.1 Motivation Updates

- Associate the Feature realisation with the Requirements, Axioms, Principles, Constraints, Goals/Outcomes and other Motivation elements identified in Section 1.
- Add/refine any requirement made explicit by this DRP only after architectural review; do not delegate requirement discovery to implementation.
- Preserve existing Motivation elements unchanged where they already express the required semantics.

### 10.2 Strategy Updates

- Establish/refine the Enterprise Capability and Feature context identified in Section 1.
- Map the Enterprise Capability/Features to the applicable Business and Business-Enabling capability context.
- Associate applicable Courses of Action and other Strategy elements.
- If no defensible Feature and Enterprise Capability placement can be established, stop and reconsider the architectural justification for the DRP.

### 10.3 Business Layer — Information Architecture Updates

- Reuse Pragmaton, Pragma, Pragmatosis and InformationUnit concepts.

### 10.4 Business Layer — Feature-Scoped Function, Service and Process Updates

- No Business behaviour merely to mirror loader/unloader mechanics.

### 10.5 Application Layer — Information Architecture Updates

- Define only shared application information semantics needed by the interaction.

### 10.6 Application Layer — Feature-Scoped Enterprise Capability Realisation

- Establish/refine the Ergon-to-Mneme Pragmaton Interaction and relate it to Contextualisation and Feature realisation.

### 10.7 Application Layer — Structural / Component Updates

- Reuse Ergon/Ergo and Mneme; loader/unloader/helper classes remain implementation design.

### 10.8 Cross-Layer and Cross-Feature Relationships

- Connect the application realisation to the Feature(s) it realises/supports rather than leaving application constructs isolated.
- Preserve explicit relationships to applicable Requirements, Axioms, Courses of Action and information concepts.
- Establish Access, Realisation, Serving, Assignment, Association, Influence or other native/specialised relationships according to their actual semantics; do not use generic Association where a stronger governed relationship is known.
- Connect related Features where the design depends on another Feature's services or information rather than duplicating that capability locally.

### 10.9 Explicitly Unaffected / No-Change Architecture

- Implementation classes, interfaces, DTOs, algorithms, products, deployment topology and internal module decomposition remain Technical Lead solution-design space unless explicitly elevated by this DRP.
- Existing architecture referenced only as context is not modified unless this section explicitly says so.

## 11. Review Decision

**Decision:** Pending\
**Decision authority:** Solution Architect / Architecture Review\
**Decision notes:** (To be completed during review.)

## 12. Post-Decision Actions

(To be completed after review.)

## Appendix A --- Design Provenance

This appendix is informative, not normative.

The source lineage is Draft.002 and the historical Context Load/Save
interaction. The current revision reconciles the Pragma-family candidates
under the G03 instruction. [Source draft][previous] · [Ergo roles][ergo] · [G03 instruction][task]

[previous]: DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md
[pragma-model]: DRP-Mneme-Pragma-Model.draft.000.003.md
[pragmaton]: DRP-Mneme-Pragmaton-Model.draft.000.003.md
[pragmaton-workspace]: DRP-Mneme-Pragmaton-Model.draft.000.003.md#37-pragmaton-and-ergonworkspace
[pragmaton-space]: DRP-Mneme-Pragmaton-Model.draft.000.003.md#38-associative-managed-information-space-semantics-javaspaces-inspiration
[pragmaton-entry]: DRP-Mneme-Pragmaton-Model.draft.000.003.md#310-initial-contextualisation-and-specialised-entry-points
[pragmaton-access]: DRP-Mneme-Pragmaton-Model.draft.000.003.md#311-on-demand-acquisition-and-authorisation
[publication]: DRP-Pragma-Publication-and-Subscription.draft.000.002.md
[pub-output]: DRP-Pragma-Publication-and-Subscription.draft.000.002.md#41-output-and-publication
[pub-processing]: DRP-Pragma-Publication-and-Subscription.draft.000.002.md#43-ergon-processing-environment
[pub-reliability]: DRP-Pragma-Publication-and-Subscription.draft.000.002.md#5-reliability-and-consistency-requirements
[adhoc]: DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.001.002.md#4-architectural-definition
[security]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#4-architectural-definition
[review]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[dr7]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-7-pragma-fhir-security-context-authority
[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-ergon-to-mneme-pragmaton-reconciliation-task.md
[evidence]: ../evidence/software-0.1.0/005/after/G03-Ergon-to-Mneme-Pragmaton-Reconciliation-005.md
[pragma-evidence]: ../evidence/software-0.1.0/005/after/G03-Mneme-Pragma-Model-Reconciliation-005.md
[pub-evidence]: ../evidence/software-0.1.0/005/after/Pragma-Publication-and-Subscription-Reconciliation-005.md#proposed-ergonmneme-interaction-amendments
[persistence]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[caches]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[iu]: ../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[ergo]: ../../continuum/application/application-component/49c9eb2bef0c47b8b5328621d5fc5479.md
[workflow]: ../../continuum/application/application-function/0bddade1309e426aa84d0cd97c35742a.md
[context]: ../../continuum/strategy/capability/3c87e2b0806647dfa0daa16d523a3915.md
[recovery-feature]: ../../continuum/strategy/capability/feature/3603ab61b6b74f3d9064918dde0c4fdf.md
[indeterminate]: ../../continuum/motivation/requirement/fc732b64fc564127b73174e28d9ccce8.md
[axioms]: ../../governance/architectural-axioms.md
