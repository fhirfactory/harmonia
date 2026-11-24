<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture Governance and HCRL — Discovery and Candidate Design

**Date:** 2026-10-21 (Australia/Sydney)

**Branch:** `ArchitecturalGovernance`

**Inspected starting commit:** `2cbd9ded6be9f38cb4f18fe2ec083a7375750945`

**Current release authority:** `1.1.1`, immediate predecessor `1.1.0`

**Prepared by:** Codex, as discovery/design submitter

**Standing:** Working report for Architecture Review Board adjudication. No proposal in this report is authorised architecture or governance.

## 1. Findings and recommended design direction

Harmonia already has most of the semantic foundations needed for the Harmonia
Change Review Log (HCRL). The governed [Architecture Change Provenance contract][provenance]
applies across Markdown/RADS, ArchiMate and future representations. Its
`change-review.md` already records architectural meaning, attributable decisions,
authorised instructions, changed-content correspondence and transition closure.
The [ArchiMate Change Review contract][review] adds independently adjudicable
Change Requests (CRs), stable review-local identifiers, dependencies, and separate
Disposition, Outcome, Outcome Reason and execution evidence.

The minimal candidate direction is to evolve these foundations into one
release-level semantic review/change record, with specialised producers such as
ArchiMate intake feeding that record. A second competing provenance policy or a
repository-wide rearrangement is unnecessary to establish that model.

Five findings materially constrain the design:

1. **HCRL is broader than ArchiMate, but broader provenance is already governed.**
   Generalisation should preserve that existing representation-neutral contract.
2. **The requested Board model differs from the installed acceptance model.**
   Current contracts reserve acceptance and disposition changes to the Solution
   Architect. Board membership is supplied task input; voting, delegation and
   formal Board acceptance rules are not established in the inspected contracts.
3. **The requested filename convention changes an exact evidence contract.**
   Current durable evidence consists of exactly `before.tar.gz`, `after.tar.gz`
   and `change-review.md`. Making a completed HCRL the third component needs an
   explicit amendment, not an additional permanent fourth component by accident.
4. **Knowledge kinds, document forms and approval standing are different things.**
   Principles, constraints, rules, definitions and rationale occur inside many
   document forms. The repository already recognises assertion-level standing;
   location in RADS does not accept every statement in a file.
5. **Release policy is wider than architectural materiality.** Every accepted
   repository change currently increments the architecture patch release. The
   Board must decide how an immediate-predecessor HCRL chain covers releases
   containing only ordinary implementation changes.

The recommendation is a small conceptual addition: an explicit candidate-knowledge
intake and navigation guide; a release-level HCRL lifecycle; and an adjudicated
reconciliation of existing authority, review and evidence contracts. Sections
6–10 describe candidates only. Section 11 identifies the decisions needed before
implementation can be authorised.

## 2. Basis, scope and inspection limits

### 2.1 Existing authority versus supplied design input

The current governed facts in this report come from repository-held sources,
especially [AGENTS.md][agents], [Architectural Axioms][axioms], [provenance][provenance],
[release governance][release], and the review/authoring Praxes. Source references
below identify their actual semantic role, including historical qualifications.
Implementation and generated output were not used to invent architectural intent.

The following are **explicit design inputs from this task**, not discoveries
attributed retrospectively to existing governance:

- The RAD holds durable Harmonia architecture and framework/governance knowledge.
- The Board consists of Mark Hunter, Solution Architect and architectural
  authority, and ChatGPT, architecture peer/reviewer. Codex and Junie normally
  submit candidate changes and cannot independently authorise architecture.
- HCRL is the architectural change-log for one immediate governed transition,
  with accumulating pending iterations and one completed record for the target
  release. Longer provenance is traversed backwards on demand.
- Material review matters are distinguished from supporting/minute detail, which
  can receive an explicit Board `ACCEPTED_WITHOUT_COMMENT` disposition.
- Candidate RAD Knowledge can be submitted with provisional classification and
  proposed RAD location; the Board adjudicates its treatment.

These inputs establish the design problem and direction. They do not authorise
installation of the proposed model or silently amend existing contracts.

### 2.2 What was inspected

Inspection covered maintained governance contracts and templates, representative
domain definitions/principles/constraints/patterns, domain review/decision records,
source-incorporation guidance, practical architecture entry points, selected
maintained review/intake code and tests, and the release helper. The inventory
in section 3 records the sources and purposes. This is a bounded semantic
inventory, not a claim to have audited every assertion in the RAD or all code.

Selected legacy material was read to establish its role and reconciliation need:
the documentation index and architecture-decision catalogue. Its assertions were
not promoted into current architecture. No `.junie` historical report was used
as architectural authority; links to such reports were treated as provenance or
navigation leads only.

**Excluded:** current architect WIP ArchiMate ingest material for the separate
Information Architecture work; incoming/native model contents; run manifests,
candidate models and run-local evidence; frozen snapshots and archive contents;
historical machinery reproduction; publication generation; and evidence migration.
Initial filename discovery exposed processing filenames, but those WIP contents
were not opened, parsed, compared or executed. Maintained documentation referring
to processing was read without following its WIP inputs.

### 2.3 Release handling for this task

The task explicitly prohibits release-metadata changes. This report is an
unaccepted discovery/design submission, not an installed governance change or a
new governed release. `architecture-release.json` remains unchanged. This does
not create a general exception to the existing patch rule: any later accepted
repository change set must address [release governance][release] explicitly.
No `1.1.2` release or HCRL is allocated by the examples in this report.

## 3. Current-state inventory and RAD locations

The table classifies sources by their content and stated authority, rather than
by directory or filename alone. An entry can contain several knowledge kinds.

| Source / current location | Actual architectural or governance purpose | Standing and qualification |
| --- | --- | --- |
| [Root AGENTS.md][agents], [docs/AGENTS.md][docs-agents] | Repository authority, RADS/legacy reconciliation, derived invariants, agent boundaries, execution constraints and required checks. The docs file delegates to the root. | Maintained repository instructions; not a competing architecture catalogue. Sections 1 and 6 explicitly exclude plans/reports as architectural authority. |
| [governance/architectural-axioms.md][axioms] | Single cross-domain register AX-01–AX-18, rationale, consequences, review questions and authority hierarchy. | Highest repository design authority. Distinguishes non-normative realisation/examples from axioms. AX-12 remains an axiom. |
| [Motivation axiom orientation][axiom-orientation] and [reclassified-principles.md][reclassified] | Compatibility navigation to canonical axioms; correction of an earlier AX-12 transfer claim. | Explicitly non-authoritative orientation. A `principles/` path is not evidence of a separate principle register. |
| [Foundational requirements][foundational], [external constraints][external], [master navigation catalogue][requirements] | Five platform obligations; three general external constraint categories; navigation to downstream requirements. | Foundational/external entries have stated acceptance. Downstream candidates are not accepted by being indexed. Requirements and constraints are already different semantic concepts. |
| [Strategy capability tier model][tiers], [Enterprise Capabilities][enterprise] | Capability/Feature semantics, component-clustering constraints and recurrence/derivation principles. | Contains principles within architecture explanations, not a separately governed principle lifecycle. Derivation examples retain explicit bounds. |
| [Business metamodel][business-model] | Definitions of Capability, Feature, Function, Service, Process, identity/framing and modelling rules. | Durable definitions and normative constraints occur together. Model rules are not all architectural decisions or axioms. |
| [Business information responsibility][business-information], [cross-capability dependencies][dependencies] | Ownership invariants, Service-exposure pattern, dependency rules and established/unestablished relationships. | A heading called “Architectural Principles” includes definitions, rules and patterns. Unsupported dependencies are explicitly withdrawn rather than replaced by inference. |
| [Information modelling guardrails][information-guardrails], [Information metamodel][information-model] | Sixteen core modelling rules plus model-local Information Unit G1–G8; conceptual semantics and boundaries. | Guardrail identifiers have local scope. These are not ten root invariants or a globally numbered rule register. |
| [Containment and collections][collections], [Metabolē][metabole] | Reusable relationship/progression semantics, qualifications, exclusions and architectural realisations. | Pattern documents can establish normative semantics; examples inside them do not establish every possible instance. Metabolē is a conceptual construct, not an execution manager. |
| [Domain05 overview][domain05], [component register][components], [data/persistence view][data], [gap register][gaps] | Current bounded solution baseline, responsibilities, semantic constraints, accepted specialisations and unresolved detailed contracts. | U20 records A4 acceptance for the Step-B baseline; that is not final Domain05/MVP acceptance or resolution of all gaps. “Candidate” filenames do not determine every assertion's current standing. |
| [Attached Energeia/DigitalTwin/Dokimasia DesignNote][attached-note] | Source design decision/input: execution specialisation, common inheritance/reuse principle, scoped Dokimasia constraints, candidates and discovery obligations. | Header says candidate input; purpose also says current adjudication. Actual incorporated standing is traced through [U02/U20][sources], the Domain05 overview and component register, not inferred from either phrase alone. |
| [Mneme publication/subscription DesignNote §21][handoff-note] | Several statements labelled architectural principles: responsibility boundaries, concrete handoff rules, instance semantics and subsystem generality. | Mixed semantic kinds under one heading; implementation possibilities are separately deferred in §22. Incorporation/authority must be checked through the maintained Domain05 sources. |
| [Step-B method][step-b], [Health Information Access example][worked-example] | Canonical method/rules and a bounded method-validation example. | The example excludes automatic component allocation and B1 PASS; rule authority and example purpose remain separate. |
| [Domain01 assurance reconciliation][assurance-review] | Approved bounded reopening/refreeze and rationale for REQ-FND-005. | Review/decision provenance; explicitly not a new ADR. Normative requirement is maintained in its requirement source. |
| [Fixed capability decomposition decision][decomposition-decision] | Approved metamodel decision, rationale and reconciliation record, with later current-authority qualification. | Normative rules reside in Business metamodel/Strategy; decision-time content is retained and qualified. |
| [Domain04 G1 review][g1], [G2 review][g2] | Architectural findings, partial approvals, decisions, uncertainty, reconciliation and historical context. | Includes durable decisions outside an ADR filename. G1 K12/K13 show the reasoning later formalised as AX-17; G2 approvals are bounded and do not approve its whole package. |
| [Domain05 sources][sources], [B1 reconciliation record][b1-review] | Attributable task inputs, incorporation locations, differences, validation and residual questions. | Source keys are evidence locators, not authority rankings. B1 review says it is not an additional source of architectural decisions. |
| [governance/README.md][governance] | Cross-domain governance entry point and links to contracts. | Explicitly says governance is not a numbered architecture domain. Some linked documents retain historical release-bound statements. |
| [Architecture Modelling Strategy][modelling] | Native-semantic compatibility, explicit Home and qualified current containment interpretation. | Accepted modelling decisions, with current clarification distinguishing earlier replay evidence from current rules. General process design must preserve these specialised semantics rather than infer replacements. |
| [Architecture Completion Plan §§5–8,11][completion] | Canonical-corpus obligations, semantic migration questions, task control, progressive authority loading and reporting. | Programme guidance does not itself authorise migration. It already asks which domain/governance artefact owns newly found knowledge and whether it is represented. |
| [Architecture Authoring Praxis][authoring] | Governed repository/workbench model; baseline, authoring, one-time submission, review and serial next-publication lifecycle. | Broad architectural purpose, but operational guidance is strongly Archi-centred. No branching/merge semantics are established. |
| [ArchiMate Round-Trip Engineering Praxis][roundtrip] | Specialist input discovery, observation, review and application checkpoints, reconciliation, proof, acceptance/publication. | Governed mechanism, not a runtime Energeia component or unattended adjudication engine. Current View preservation qualifies older generated-View wording. |
| [ArchiMate Change Review contract][review] and [template][template] | Independent CR decomposition; four dispositions; separate outcomes/reasons; decision-linked instructions; transition inventory and closure fields. | Generated defaults are On-Hold/Unresolved/Not authorised. Template examples are not decisions. Exact durable evidence is governed by provenance. |
| [Architecture Change Provenance][provenance] | Representation-neutral definition of authorised transitions, exact changed-file archives, semantic review, closure and processing independence. | Accepted 2026-10-20. Reconciles older permanent processing-retention interpretations. Adoption authorises no reconstruction or disposal. |
| [Release policy][release], root [architecture-release.json][release-json], [release helper][release-helper] | Acceptance-unit release allocation, immediate previous version, explicit patch update and read-only publication consumption. | Current `1.1.1 ← 1.1.0`; historical bounded decisions in the policy do not freeze today's root allocator. Software version and run numbers are independent. |
| [Current identity/naming contract][identity], [earlier identity model][old-identity] | Resource-owned UUID32 canonical element identity; naming; distinct representation identity; earlier adoption/binding concepts. | Current contract explicitly supersedes earlier UUID-URN/EC lifetime-handle recommendations for these fields. It allocates no canonical IDs to relationships or Views. Old and current identity terminology must not be blended. |
| [RAD→ArchiMate mapping][mapping], [synchronised baseline guide][baseline] | Assertion-level source/standing, exact correspondence, structured captures and projections. | Structured RAD captures can be governed source, not merely disposable generated files. The baseline guide contains older “current publication” locators; consult root authority and applicable publication provenance. |
| [Architecture scratchpad][scratchpad] | Working discussion, distinctions, candidate decisions, rationale, warnings, open issues and promotion targets. | Explicitly non-authoritative despite residing within RADS. Already a natural capture source; not an approval mechanism. |
| [Practical architecture entry][archimate-entry], [publication index][publish-index], [processing guide][processing] and [ingestion layout][ingestion-layout] | Navigation and operational boundaries for workbench, exchange and processing. | Maintained role descriptions; frozen history remains qualified. Published index identifies `1.1.0` baseline while root is `1.1.1`; governance-only increments need not regenerate a model. |
| [Framework guide][framework], selected maintained review/intake tools and tests | Executable observation/correlation/applicability support, specialised adapters, regression checks and declared limitations. | Machinery supplies evidence, not acceptance. Inspection did not execute intake, adapters or fixture replays against current work. Section 5 gives precise reuse limits. |
| Legacy [docs/architecture-decisions.md][legacy-decisions], [docs/README.md][docs-index] | Historical decisions/rationale, engineering navigation and implementation-status labels. | Outside RADS. Some ADR sections have scoped supersession wrappers; others retain “Accepted”/“Current” headings. Those labels alone do not establish current architectural authority. |

## 4. Terminology, semantic overlaps and unresolved gaps

### 4.1 What is already defined

| Term / dimension | Existing meaning or observed usage | Consequence for HCRL design |
| --- | --- | --- |
| RAD / RADS | RAD is used for governed architecture/publications; RADS specifically denotes `/docs/markdown`, the current authoritative documentation set. The repository is the system of record. | Define the abbreviations once; preserve source versus projection without creating competing stores. |
| Axiom | Fundamental, stable architectural truth constraining decisions, invariants, requirements and implementation. | Preserve AX identities and higher review obligation; no automatic promotion or demotion based on wording. |
| Architecture/design principle | “Architectural principle” labels axioms, recurring design guidance, capability derivation tests and subsystem obligations. No common separate architecture-versus-design-principle definition/lifecycle was found in the inspected sources. | Refine by semantic role and scope; do not assume two new authority levels. |
| Requirement | Obligation/outcome to be achieved; foundational versus detailed downstream requirements are distinguished. | Retain requirements in the durable taxonomy, even though the supplied illustrative list did not enumerate them. |
| Constraint | Limits choices; external categories explicitly distinguish deployment-specific instances from universal constraints. Internal constraints also occur in metamodels/component contracts. | Record source, applicability and scope; a constraint need not be external. |
| Invariant, rule, guardrail | Root instructions call the ten invariants “Derived Architectural Invariants and Guardrails,” each with a Rule and enforcement. Domain rules use additional local names/numbers. | These overlap, but express property, prescription and protective purpose respectively. They need not become three duplicate registers. |
| Pattern | Reusable relationship, responsibility or progression arrangement; also used for runtime Publishing/Subscription matching semantics. | Architectural design patterns and runtime Pattern definitions are different concepts despite sharing a word. |
| Anti-pattern | Used descriptively, for example “solutions looking for problems”; rejected designs also occur in legacy ADRs and reclassification material. | No common maintained anti-pattern catalogue/acceptance contract was found. Rejection of one proposal does not automatically create a universal anti-pattern. |
| Exemplar / worked example | Paradeigma is an isolated exemplar/test subsystem; the Health Information Access example validates a method within explicit bounds. | A teaching example, runtime simulator and regression fixture have different purposes. No common exemplar governance was found. |
| Decision / ADR / adjudication | A consequential selected approach and rationale; decisions are also preserved in reviews, source registers, metamodel amendments and contract decision sections. | ADR is a record form, not the sole way accepted knowledge currently exists. Decision event versus resulting current rule must remain distinct. |
| Praxis | Architecture Praxes prescribe engineering context/checkpoints. Runtime Energeia Praxis defines the pathway execution envelope and is not an orchestrator/choreographer. | Governance Praxis introduces no runtime component, service or queue. |
| Disposition | Exactly Accept, Reject, On-Hold, Manual Intervention in current ArchiMate review; Solution Architect's instruction to the agent. | Revised, deferred, already represented, complete and resolved cannot silently become extra existing disposition values. |
| Outcome / Outcome Reason | Unresolved or Resolved, plus evidence-referencing explanation; independent of instruction and execution. | A held finding may become resolved by another governing decision without acceptance of its submitted instance. |
| Standing / currentness / completeness | Baseline guidance distinguishes Canonical/Proposed/Candidate, Current/Historical and placement/identity/coverage questions. | “Current” need not mean accepted; accepting a candidate baseline does not complete a domain or resolve all details. |
| Release / publication / run / iteration | Separate architectural state label, representation output, processing attempt, authoring/review progression. | Pending HCRL iteration number cannot replace a release allocator or processing run identifier. |
| Provenance / audit / assurance | Architecture-change provenance is the repository transition contract; runtime information derivation, Kleio audit and Dokimasia assurance have distinct meanings. | HCRL does not imply new Kleio/Dokimasia runtime persistence or assurance machinery. |
| Configuration & Governance information | The [Domain05 persistence view][data] distinguishes runtime definitions/configuration from Operations & Assurance observations. | Repository architecture/framework knowledge is not automatically an allocation to Mnemosyne's runtime C&G information responsibility or a new persisted object. |

### 4.2 Findings requiring reconciliation, not inferred resolution

| Finding | Existing governed fact / evidence | Observed problem or gap |
| --- | --- | --- |
| F01 — authority transition | [Review][review], [authoring][authoring] and [provenance][provenance] reserve acceptance/disposition changes to the Solution Architect. | Supplied Board membership does not define quorum, disagreement handling, delegation or whether a peer recommendation constitutes acceptance. |
| F02 — broader record name and exact packaging | [Provenance][provenance] requires exactly three specifically named files; [review][review] is specialised for ArchiMate. | HCRL naming and release-level scope require an explicit contract amendment. Treating archives as optional would also change current obligations. |
| F03 — no-comment acceptance | [Review][review] governs exactly four dispositions and forbids generated acceptance. No HCRL/`ACCEPTED_WITHOUT_COMMENT`/Board contract was found in the inspected maintained governance. | A new Board disposition and grouped/minutiae handling need explicit semantics; absence of comment cannot grant authority. |
| F04 — release/materiality mismatch | [Release policy][release] increments for every accepted repository change. [Provenance][provenance] does not equate machinery runs or no-change attempts with architectural transitions. | A chain that traverses immediate governed releases needs an explicit treatment for non-material software-only releases. |
| F05 — classification guidance is partial | [Completion Plan §6][completion] has semantic migration questions; [scratchpad][scratchpad] offers Promotion Target; rules/principles occur throughout domains. | There is no shared, simple route from “this should be a principle/exemplar” to a classification proposal, owner/location and review disposition. |
| F06 — location versus status | [Mapping][mapping] explicitly attaches standing to assertions; the scratchpad is non-authoritative inside RADS. [Sources U02/U20][sources] qualify the attached note's incorporation. | File-level labels and location are insufficient. A discovery tool could otherwise promote candidate SHALL statements or overlook accepted content in “candidate” files. |
| F07 — prospective Domain13 versus current cross-domain governance | [Requirements navigation][requirements] assigns formal guardrails/ADRs/compliance to Domain13. [Governance index][governance] explicitly says the current governance location is not a numbered domain. | Relationship between prospective domain taxonomy and installed framework governance needs adjudication. This is not authority to create Domain13 or relocate governance. |
| F08 — distributed enduring rationale | [G1][g1], [decomposition decision][decomposition-decision], [sources][sources], [legacy ADRs][legacy-decisions] and contract decision sections contain different forms of rationale/decision history. | Architects lack one navigation route to current meaning, its rationale and the transition that established it. Some legacy dependencies remain to be individually reconciled. |
| F09 — identity terminology has evolved | [Current identity contract][identity] supersedes earlier recommendations; [review][review] makes CR IDs process-local. | Candidate knowledge and HCRL correlation need stable references without making every sentence a canonical element or reviving the old register as current identity authority. |
| F10 — archival history versus current operation | [Provenance][provenance] requires eventual independence; [framework guide][framework] acknowledges existing historical-processing dependencies. | Closure/cleanup cannot be inferred from a completed record. The actual dependency graph and unique surviving bytes were not audited here. |
| F11 — navigation drift | [Baseline guide][baseline] retains an older “current publication” path; [published index][publish-index] and [release authority][release-json] reflect later boundaries. | Cross-domain navigation needs a reliable current-source/standing route; “current” inside dated prose is not another release register. |
| F12 — acceptance granularity | Current review explicitly separates instance acceptance from general metamodel/inference rules. [G2][g2] retains partial approvals. | Candidate RAD Knowledge needs separate review when extracting a general rule from a particular case; broad acceptance of a document cannot be assumed. |

These findings are not declarations that every apparent tension is a contradiction.
For example, release `1.1.1` with applicable model publication `1.1.0` is expressly
permitted for a governance-only increment. Older identity recommendations are
explicitly superseded for the current fields. The remaining uncertainty concerns
their navigation and future integration, not permission to disregard the newer
authority.

## 5. Existing Change Review machinery and reuse limits

| Maintained mechanism inspected | What can naturally be reused | What it does not establish |
| --- | --- | --- |
| [CR contract][review] | Independently adjudicable granularity; immutable observations; interpretation/decision/instruction separation; stable CR IDs; prerequisites; separate disposition/outcome/reason; stop on ambiguity. | A Board constitution, release-wide accumulating HCRL or no-comment acceptance. |
| [Review template][template] | Summary dashboard; originating/intended/actual state; scope; authorisation history; changed-content inventory; absences/renames/succession; archive hashes and closure fields. | Approval of example CRs or authority from a completed form. |
| [roundtrip_praxis.py][intake-code] | Discover exact inputs, observe through shared tools, bind a reviewed document to exact input hashes, explicit agent checkpoints. | `inspect_review` checks that baseline/incoming hashes occur in the document and reports required contract checks; it explicitly returns `automatic_execution_authorised=False`. It is not an approval parser. Default discovery expects a publication under the root release; it cannot infer the applicable older publication for a governance-only increment. |
| [change_review.py][review-code], [element_correlation.py][correlation-code] | Read-only field evidence; candidate interpretation; unique canonical-first correlation; explicit representation rebinding; control-based transport distinctions; unresolved conflicts. | Automatic CR decomposition, acceptance or reconciliation. The evidence lifecycle deliberately records `disposition=NOT_RECORDED`, `outcome=Unresolved`, `execution=NOT_AUTHORISED`. |
| [Reconciliation adapter contract][adapter-guide] | Exact decision/instruction/plan binding, bounded capture/replay/check, persisted resource-owned identities and preservation checks for supported scope. | General architecture adjudication, support for every future HCRL producer, or publication/Archi acceptance. Adapter behaviour was inspected through maintained documentation, not executed. |
| [Praxis checkpoint tests][intake-tests], [Change Review tests][review-tests] | Evidence that the maintained design distinguishes hash applicability from execution authority, ambiguous inputs, correlation and candidate observations. | Proof of Board acceptance or a completed HCRL. Fixture contents and historical/current ingest specimens were not opened or replayed. |
| [Identity/naming contract][identity], source correspondence and captures | Existing resource continuity and explicit before/after representation correspondence. | Identity from names, file paths, CR numbers, documentation headings or a newly invented candidate-knowledge UUID scheme. |
| [Provenance contract][provenance] | Authorised incremental evidence, exact full changed files, meaningful final review, no recursive capture, separate Git history and disposal gate. | An implemented general evidence packager or proof that all historical dependencies are already removed. No such implementation claim is made here. |
| [Release helper][release-helper] | Single validated current/previous release authority; explicit expected-current patch bump; publication reads rather than allocates. | Acceptance, semantic review, concurrent release allocation or independent HCRL versioning. |

The reusable core is the review semantics. XML comparison, native type/relationship
interpretation, containment, View preservation and actual Archi round-trip proof
should remain specialised ArchiMate producer/validation responsibilities. Their
details should not become the vocabulary an architect must learn merely to
submit an architectural principle.

## 6. Candidate conceptual model and authority boundaries

All proposals P01–P10 below require Board adjudication. “Existing fact → observed
problem → candidate proposal” is stated for each proposal; later detail elaborates
that candidate, without installing it.

### P01 — One common review process and release-level semantic record

**Existing fact:** Provenance is representation-neutral, while the most explicit
review/CR contract is ArchiMate-specific ([provenance][provenance], [review][review]).
**Observed problem:** Material Markdown, framework, modelling-method and future
implementation-originated architecture changes have no equally clear common
entry point. **Candidate proposal:** Define Harmonia Architecture Review once,
using a release-level HCRL to accumulate independently adjudicable changes and
supporting transition detail from specialised producers.

```text
Architecture work: text, models, framework design, significant code discoveries
    ↓
Candidate Architecture / Framework Changes, including Candidate RAD Knowledge
    ↓
HCRL for the proposed immediate successor release — pending iterations
    ↓
Architecture Review Board adjudication of precise proposals and bounded detail
    ↓
Authorised governed RAD update, with validation and applicable publication gates
    ↓
Completed HCRL confirming the authorised and evidenced transition
    ↓
New governed architecture release: final promotion of that same result
```

The HCRL has two views of one record: a material decision agenda and supporting
transition appendices. It is not a second current architecture definition store.
Accepted meaning and enduring rationale become readable in the appropriate RAD
sources. The completed HCRL explains their introduction/change from the immediate
predecessor, including exclusions and unresolved matters. It need not reproduce
the whole current architecture or every earlier decision.

A code discovery enters architectural review when it exposes a new or changed
responsibility, boundary, invariant, contract, reusable architectural rule or other
material understanding. The agent records whether it is a conformance defect,
missing architecture, or a proposed change. Existing code is evidence of the
issue, not authority for the answer. An ordinary fix that merely restores known
behaviour supplies no new principle or decision by implication.

### P02 — Explicit Board adjudication with attributable authority

**Existing fact:** Only the Solution Architect supplies architectural acceptance
and changes issued dispositions. The task supplies Mark/ChatGPT Board membership.
**Observed problem:** Membership does not settle the acceptance protocol or grant
independent authority to an agent. **Candidate proposal:** Record Board review and
peer advice explicitly, with Mark's attributable Solution Architect confirmation
as the operative acceptance until a different governance decision establishes
delegation or another Board decision rule.

| Participant | Proposed responsibility | Authority boundary |
| --- | --- | --- |
| Mark Hunter — Solution Architect / architectural authority | Sponsor scope, resolve architectural meaning/conflicts, confirm Board dispositions, authorise bounded application and final release closure. | Existing higher authority remains binding; changing an axiom requires explicit higher architectural consideration and consequence assessment. |
| ChatGPT — architecture peer/reviewer and Board member | Challenge reasoning, assess alternatives, review classification/location/impacts, record concurrence or disagreement. | Peer participation does not independently replace Mark's acceptance under the installed contracts. Proposed Board protocol requires adjudication. |
| Codex, Junie and other submitters | Discover authority/evidence, prepare candidates and HCRL updates, propose classification and deterministic directives, execute authorised work and record validation. | No architectural self-approval, default acceptance, inferred generalisation, silent disposition revision or completion by successful tooling. |
| Maintained tools | Observe, correlate, validate, render and enforce declared checks. | Tool output is evidence. It cannot supply missing intent, adjudication, exception authority or final architectural acceptance. |

The proposed decision record identifies the decision maker, peer review where
provided, date, exact reviewed iteration/content, bounded decision, rationale,
conditions, exclusions and resulting application authority. A revised proposal
returns for the required review; advice and an unresolved disagreement remain
visible. This report does not establish unanimity, majority voting, quorum,
delegation, or a requirement to obtain a second review for every minor edit.

## 7. Candidate durable-knowledge taxonomy and simple navigation

### P03 — Classify meaning independently of document form and standing

**Existing fact:** Durable definitions, rules, principles, patterns and decisions
already coexist in metamodels, domain sources, DesignNotes and review records.
**Observed problem:** Labels overlap; classifying by filename can confuse a
candidate, an example, an architectural fact and an accepted rule. **Candidate
proposal:** Use a small set of semantic kinds with explicit scope, force and
standing. Treat these as classification aids, not a new authority hierarchy.

| Candidate knowledge kind | Practical classification question | Likely current RAD home / treatment |
| --- | --- | --- |
| **Axiom** | Is this a fundamental architecture-wide truth that all downstream choices must respect? | Existing canonical axiom register only after explicit adjudication; preserve AX identity and heightened review. A memorable maxim alone is insufficient. |
| **Principle** — architecture or design scope | Does this guide recurring architectural/design judgement and explain why one approach is preferred? | Architecture-wide guidance in cross-domain governance; domain/design specialisation near its owning metamodel or contract. “Architecture”/“design” initially qualify scope/abstraction, not two new precedence levels. |
| **Requirement** | What required capability, outcome or obligation must be achieved? | Motivation for foundational obligations; owning downstream domain for detailed obligations, using established navigation. Do not rewrite it as a solution pattern. |
| **Constraint** | What boundary limits permitted choices, and under which context or external obligation? | Owning domain/contract, or cross-domain governance if genuinely cross-cutting; state applicability, source and qualifications. External legal instances retain jurisdiction/deployment context. |
| **Rule** | What sufficiently precise prescription can a contributor apply or check? | Relevant metamodel, design contract or governance process, linked to governing decisions/principles/axioms and conformance evidence where practical. |
| **Pattern** | What reusable arrangement solves a recurring problem within stated context, with forces and consequences? | Owning domain pattern/contract source. Reference established realisations without making every use mandatory. Keep architectural pattern meaning separate from runtime Pattern matching. |
| **Anti-pattern** | What recurring approach is harmful under stated conditions, and why? | Linked guidance beside the owning rule/pattern, with consequences and supported alternatives. An individually rejected idea is not automatically a universal prohibition. |
| **Exemplar / worked example** | What bounded example teaches or demonstrates established architecture or a method? | Owning domain or linked example, with the demonstrated authority, scope and limits. It supplies no general norm beyond that authority. Simulator/test locations keep their distinct engineering purpose. |
| **Significant decision and rationale** | Which consequential alternative was selected, why, with what scope and consequences? | Maintain the enduring decision/rationale in the appropriate RAD source or linked governed decision record. HCRL records the decision event and transition effects. ADR is one possible record form. |
| **Definition / semantic model / design contract** | What does a concept mean; which responsibility, relationship or interaction semantics are established? | Existing domain catalogue/metamodel/contract. These foundational knowledge kinds must not be forced into “principle” merely to enter review. |
| **Governance/process knowledge** | How is architecture authored, reviewed, accepted, changed, published or evidenced? | Current cross-domain governance/Praxes; can itself contain definitions, rules, constraints and rationale. This is a subject area as well as a classification facet, not necessarily an exclusive peer of the other kinds. |

An **invariant** describes a property that must hold. A **guardrail** describes
how rules/constraints protect a boundary or prevent a class of error. Both can
qualify an established rule/constraint; neither requires a duplicate definition
register. The Board may choose a stricter distinction if it improves actual
architectural decisions, but no current statement is reclassified here.

Record independently: knowledge kind; subject/domain and scope; normative force;
standing/approval basis; applicability and exceptions; lifecycle/currentness;
canonical source; and traceability to governing authority. These are semantic
dimensions, not a requirement for a new schema or a field on every paragraph.
`SHALL` versus `SHOULD` does not, by itself, identify the knowledge kind or prove
acceptance. The same accepted statement may play several roles; maintain one
canonical definition and express relationships instead of copying it into every
category.

Preserve the installed authority hierarchy. This taxonomy does not insert
“Principles” above decisions or below axioms by inference, turn AX-12 into a
design principle, or promote existing local guardrails into new axioms.

### P04 — A navigation guide over semantic ownership

**Existing fact:** The Completion Plan already asks where architectural meaning
belongs, and cross-domain governance coexists with domain-specific definitions
and patterns. **Observed problem:** The question is difficult to answer without
knowing many documents; prospective Domain13 references add ambiguity.
**Candidate proposal:** Provide one short governed knowledge/navigation guide
linked from cross-domain governance and domain indexes, pointing to maintained
definitions and their standing rather than relocating content for symmetry.

The guide's user-facing route would be:

1. **State the important thing in plain language.** Include the context, why it
   matters, evidence and what remains unknown. Classification can be uncertain.
2. **Check whether it is already represented.** Find its governing axiom,
   definition, rule or decision; distinguish a restatement, clarification,
   specialisation, contradiction and genuinely new knowledge.
3. **Propose its kind and scope.** Use the questions above. A proposal can say
   “principle or scoped constraint — Board classification required.”
4. **Propose the owning RAD source.** Architecture-wide foundations/governance
   go to cross-domain governance; domain semantics remain with that domain;
   bounded examples remain linked to the concept/method they illustrate.
5. **Submit through the pending HCRL.** The Board decides meaning, standing,
   classification, location and authorised incorporation, or another disposition.
6. **After authorised incorporation, link current knowledge and transition.**
   The RAD answers what is governed; the completed HCRL answers how it changed.

For navigation, three questions suffice:

| Reader question | Proposed route |
| --- | --- |
| What governs architecture and how do I contribute? | Governance entry → authority/axioms → knowledge guide → review/Praxes. |
| What does this part of Harmonia currently mean? | Owning domain index → canonical definition/contract → linked rule/pattern/rationale and explicit unresolved matters. |
| What changed and why? | Applicable release → completed HCRL from its immediate predecessor → affected canonical source/correspondence; earlier HCRLs only as needed. |

This proposes a navigation surface, not new folders for every taxonomy entry.
Existing metamodels, domain pattern sources and governed decision records can
remain in place. Long-lived decisions necessary to understand the current
architecture remain available in RAD even after their transition leaves the
latest HCRL. Chronological notes and failed processing attempts do not become
the current-knowledge index. The Domain13 relationship remains a Board decision.

### P05 — Candidate RAD Knowledge as explicit review content

**Existing fact:** AX-17 permits clearly identified candidates; scratchpad notes
already support Promotion Target, and review CRs separate general rules from
instance acceptance. **Observed problem:** Candidate extraction, deduplication,
classification and incorporation have no shared submission model. **Candidate
proposal:** Make Candidate RAD Knowledge a recognisable HCRL item or linked part
of an architectural CR. It is a proposed semantic change, not another permanent
unreviewed knowledge repository.

A useful candidate submission contains:

- Exact statement or bounded proposed wording, originating context and evidence.
- Proposed kind, scope, applicability, normative force and rationale; alternatives
  or classification uncertainty where relevant.
- Existing authority and comparison: new, clarification, specialisation,
  correction, contradiction, duplication, or uncertain.
- Proposed canonical RAD location and related sources needing reconciliation.
- Affected knowledge/construct references, dependencies and consequences for
  architecture, models, implementation, documentation and conformance checks.
- The precise question for adjudication, and expected bounded incorporation if
  accepted; initial state explicitly unapproved.

The Board can accept, request revision, reject, defer, determine that the matter
is already represented, narrow it to an example, or issue another explicit
treatment. Meaning, classification/location, and incorporation authority should
be ascertainable separately. Accepting content for continued candidate standing
does not make it an approved normative rule. Accepted-in-principle wording with
material details open does not authorise an agent to supply those details.

For compatibility, retain the current execution instruction distinction:

| Board treatment being designed | Relationship to existing instruction/outcome semantics |
| --- | --- |
| Accept bounded content | Can yield an explicit Accept instruction with exact incorporation scope and prerequisites. Validation may still be outstanding. |
| Revise / defer | Usually requires a revised proposal or held execution. It does not authorise the submitter to invent missing meaning. |
| Reject | No reconciliation of rejected content; preserve the attributable decision and reason. |
| Already represented | Record exact existing authority and why it resolves the candidate. Can resolve the question without new knowledge or an Accept instruction. |
| Human-controlled resolution | Preserve Manual Intervention handling where applicable. |
| ACCEPTED_WITHOUT_COMMENT | A new, expressly adjudicated treatment for bounded supporting detail; requires the contract decision described in P06. |

These are proposed relationships, not an installed enumeration or automatic
mapping. Issued historical dispositions remain unchanged. Board decision wording,
execution authority, resulting Outcome and validation results remain distinct.

**Worked classification probes from inspected material — no new adjudications:**

| Recognised important statement | Evidence and current standing | Candidate handling to test the model |
| --- | --- | --- |
| Common inherited platform capabilities should be reused by specialised execution constructs. | Attached note §7; already incorporated at [Domain05 common specialisation principle][specialisation], with [U02/U20][sources] acceptance context and explicit limits. | First test “already represented”; propose a discoverable principle entry/link if useful. Do not create a new universal rule or infer mandatory programming-language inheritance. |
| Dokimasia does not recursively assure itself or create a separate own-execution AuditEvent. | Attached note §8; explicitly represented as [component terminal constraints][terminal]. Detailed evidence contracts remain open. | Classify the existing bounded constraints and link them; do not generalise them to every assurance or audit activity. |
| The six-Function Health Information Access example teaches information traceability. | [Worked example][worked-example] explicitly excludes automatic allocations and broader coverage. | Consider an exemplar/navigation entry with its method and limits. Do not turn the six Functions into a mandatory pipeline or component architecture. |
| Missing architectural relationships must remain unknown rather than inferred. | [G1 review][g1] retains the historical discovery; [AX-17][axioms] is now the canonical cross-domain authority. | Demonstrates candidate-to-general-knowledge promotion already achieved through explicit adjudication. A future HCRL would describe that transition, without retrospective fabrication of one here. |

## 8. Candidate HCRL lifecycle, identity and completion

### P06 — Accumulating release review, explicit adjudication and final closure

**Existing fact:** Current CR reviews progress through observation, adjudication,
application, validation and closure, retaining material decision history.
**Observed problem:** They are run-centred; neither a release-level accumulation
contract nor the requested supporting-detail disposition exists. **Candidate
proposal:** One logical HCRL for each immediate governed transition, with the
supplied filenames identifying successive review states for its target release.

For the supplied example only:

```text
Originating governed release: RAD-1.1.1
Proposed target:              RAD-1.1.2

hcrl-1.1.2-001.pending.md  initial accumulated proposals
hcrl-1.1.2-002.pending.md  revised accumulated proposals and decision facts
...
hcrl-1.1.2-NNN.complete.md final authorised transition record

RAD-1.1.1 ── completed HCRL for 1.1.2 ──▶ RAD-1.1.2
```

The target is the release being constructed; it is not the baseline or processing
run. `NNN` is a review-update iteration, not a patch release or CR identifier.
Each pending iteration contains the accumulated release story and current
decisions, not just changes since the previous pending file. Retain material
earlier decisions/revisions within the final semantic record; complete does not
mean “retain only the last answer.” Routine saves need not be new formal review
iterations. The exact checkpoint/allocation rule is for Board adjudication.

| Proposed stage | Required meaning and gate |
| --- | --- |
| Establish the transition | Identify exact governed predecessor, source boundaries, applicable authority and proposed target. Pin source/input provenance as relevant; secure original changed-file bytes before any authorised mutation. A branch or timestamp is insufficient. |
| Accumulate and triage | Add candidates, observations and dependencies to pending HCRL. Identify material review matters and bounded supporting detail. Classification is proposed; no acceptance is manufactured. |
| Board review/update | Record explicit item or bounded-group decisions, revised wording and questions against the exact iteration/content. Carry rejected and held material with its treatment. An iteration number supplies no acceptance. |
| Authorise application | Establish exact approved content, deterministic derivations, target locations, prerequisites, exclusions and validation expectations. Check baseline drift and contradictory directives before mutation. |
| Incorporate and validate | Update only authorised canonical sources/representations in the change set. Record actual effects, continuity/correspondence and results. Separate technical PASS from architectural acceptance. Uncertain partial effects block affected closure. |
| Close the semantic record | Bind actual before/after governed states; reconcile the changed-content inventory, decisions, archive/evidence facts and applicable gates; confirm the result is precisely authorised. Later factual completion entries remain distinguishable from original authorisation. |
| Complete and promote | Issue one complete HCRL for the immediate transition with attributable closure approval. Final release acceptance/promotion binds that record and the governed result in the same accepted change set under the release policy. |

Pending and complete describe **record lifecycle**. A pending file can contain
issued decisions; it is not blanket application authority. A complete filename
alone proves neither acceptance nor valid evidence. Completion and release
promotion are closure of the same transition, not two successive releases.

Recommended completion conditions are:

1. Exact immediate predecessor and resulting release/state are identifiable;
   every material change and supporting changed-content item is accounted for.
2. All included changes have applicable explicit authorisation; no unresolved
   prerequisite or ambiguity affects the accepted result.
3. Accepted knowledge is incorporated at the adjudicated canonical locations,
   with source standing, identity and useful rationale preserved.
4. Actual results meet authorised scope and applicable validation/publication/
   inspection gates; limitations and distinct outcomes remain explicit.
5. Required transition evidence and correspondence are verified. The final HCRL
   can be understood without processing workspaces or pending iteration files.
6. Held/rejected/deferred matters have a recorded treatment. A nonblocking
   deferral may remain unresolved if the Board explicitly excludes its proposed
   change and preserves any current architectural uncertainty in RAD. Completion
   does not pretend that the architecture has no open questions.
7. Closure authority confirms material decisions, result and evidence. An agent
   may report verified facts but does not self-issue the architectural closure.

**Material matters versus supporting detail.** Materiality turns on architectural
effect, not file size or number of changed lines. A single altered responsibility,
axiom interpretation, exception or authority rule can be material. Deterministic
representation consequences, unchanged-identity rebindings and minor editorial
detail can be supporting matters where their meaning and scope are established.
Presentation changes can still be materially significant; triage is reviewable.

For `ACCEPTED_WITHOUT_COMMENT`, the proposed rule is explicit Board disposition of
an enumerated appendix/group bound to the reviewed iteration and scope. Each
item remains traceable to its affected content and constraints. The Board can
remove an item for separate adjudication. Added or materially changed detail is
unreviewed until covered by a subsequent explicit disposition. Silence, an empty
comment, an agent's triage decision, successful validation or elapsed time never
constitutes that disposition. “Without comment” permits omission of individual
rationale; it does not omit the actor/date, accepted scope or applicable
incorporation authority. Current four-value contracts require amendment before
this new treatment can be used operationally.

### P07 — Stable references and predecessor traversal

**Existing fact:** Canonical resource identity is separate from representation
identity, file paths and CR-local process identity; provenance requires path
correspondence and no recursive evidence capture. **Observed problem:** Renaming
or replacing a source could interrupt semantic traversal; assigning new IDs to
all knowledge would invent an unauthorised identity mechanism. **Candidate
proposal:** Retain existing identities and use explicit scoped source/decision
references and correspondence where canonical identity does not apply.

One logical HCRL identity binds its originating state and target release, remaining
stable across pending iterations. CR IDs remain stable within that logical review;
cross-producer references include the originating review/run and CR. The final
record maps any producer IDs to its own stable entries without silently merging
independent decisions or renumbering issued records. HCRL iteration filenames,
knowledge item locators and canonical element identities keep their separate roles.

Each affected item identifies its existing canonical identity where applicable,
or a precise scoped definition/rule/source anchor. Record semantic operation
(addition, clarification, specialisation, replacement, retirement, etc.),
before/after source locators and identity continuity or explicit successor
relationships. File moves are not semantic replacement; equal names are not
continuity. Splits/merges need explicit correspondence and adjudicated identity
treatment. The proposal allocates no canonical IDs to prose, relationships or
HCRL entries by inference.

To investigate the current state, read its canonical RAD source. To investigate
its origin/change, inspect the completed HCRL entering that release and follow
its predecessor link. If the item was unchanged, continue backwards as needed.
Follow recorded canonical identity/correspondence through moves, rebindings and
replacements. Each HCRL describes only its adjacent transition; no cumulative
history or embedded older evidence packages are required. A missing transition,
unbound identity or unavailable source remains an explicit provenance limit,
not permission to reconstruct authority from filenames or regenerate originals.

## 9. Candidate relationship between RAD, HCRL, evidence and processing

### P08 — Evolve the semantic record without expanding architectural history

**Existing fact:** [Provenance][provenance] requires exactly two changed-file
archives and one `change-review.md`, with exact whole-file preservation and no
recursive evidence. Git records maintained machinery; processing has operational,
recovery and unresolved-dependency purposes. **Observed problem:** New HCRL names
and “archives may support” wording can conflict with that exact contract or create
duplicate authoritative reviews. **Candidate proposal:** Make the completed HCRL
the successor semantic third component under an explicitly amended provenance
contract, preserving the existing changed-file evidence obligations initially.

| Information class | Proposed architectural purpose | Boundary retained from current governance |
| --- | --- | --- |
| **Current RAD / framework knowledge** | What the governed architecture and its authoring/review framework currently mean, with current definitions, rationale, standing and unresolved matters. | Canonical semantic sources in RADS; governed captures and representations retain their declared role. Not every resident proposal is accepted. |
| **Completed HCRL** | Authoritative semantic change-log from the immediate governed predecessor to this release, including decisions, correspondence, accepted scope and closure. | Understandable without run histories; not a replacement current-definition catalogue or cumulative history. |
| **Architecture-transition archives** | Exact complete before/after changed-file bytes where byte evidence is required; actual paths, explicit absences and release-representation succession. | No unchanged dependency tree, snapshots of every tool read, recursive evidence or fabricated originals. HCRL explains narrower meaning inside the preserved files. |
| **Maintained machinery/configuration** | Reusable processing, validation, generation and protection of governed semantics. | Git version history; no architectural authority from execution. Governance knowledge within a code change must be deliberately identified and adjudicated. |
| **Processing workspace / pending review states** | Discovery, preparation, immutable input pins, active decisions, recovery, validation and unresolved handling. | Operationally necessary retention does not automatically confer durable architectural-history standing. Final semantic facts survive in the completed HCRL. |
| **Regression/test fixtures** | Deliberately bounded specimens proving machinery behaviour, including historical inputs when explicitly declared. | Test purpose, not architectural acceptance; tests do not make their original processing carrier permanently authoritative. |
| **Engineering diagnostics/history** | Explain a failure, reproduce machinery or support engineering investigation under its own purpose. | Separate from architecture history; raw commands, failed attempts and machinery snapshots are not automatically transition evidence. |

The prospective logical package would have `before.tar.gz`, `after.tar.gz` and
`hcrl-<target>-<iteration>.complete.md` as its single semantic record. This is a
**candidate amendment** to the current three-file naming rule. It does not add a
permanent `change-review.md` duplicate, a manifest or a fourth decision file.
Physical package/HCRL directory layout remains for adjudication. No such package
or filename is created by this task.

The least disruptive recommendation retains current archive requirements for
changed governed architectural source. If the Board instead intends archives to
be optional for some transitions, it must explicitly decide the selection rule,
recovery guarantees and amendments to provenance. The phrase “may support” in the
design input cannot silently waive installed exact-byte preservation.

The final HCRL retains material originating evidence, decisions/revisions,
instructions, correspondence, validation conclusions/limits and closure facts.
It may link earlier transitions and maintained authority, but essential meaning
must not live only behind a disposable processing link. Pending iteration files
need not be a separately permanent evidence series once their material decision
history has been faithfully incorporated and closure/retention authority is
established. This is a prospective retention design, not disposal permission.

Failed/no-change processing attempts do not receive architectural transition
packages merely because they ran. Material held/rejected decisions needed for
future work must nevertheless survive in the reviewed record or maintained
governed unresolved matter. A completed HCRL does not prove current operation is
independent of processing or permit cleanup. Frozen snapshots/reviews retain
their original bytes; a later authorised historical reconstruction records its
provenance and limitations rather than rewriting them into this model.

### P09 — Align immediate predecessor semantics with existing release policy

**Existing fact:** Every accepted repository change increments the patch release;
publication generation does not. Root release can advance without a new XML
publication, as `1.1.1` following `1.1.0` demonstrates. **Observed problem:** HCRLs
limited to material semantic changes would leave holes when traversing every
immediate governed release. **Candidate proposal:** Preserve the installed release
policy initially and require an appropriately small release-transition HCRL even
when the semantic architecture delta is empty.

Such a record would say that architectural meaning is unchanged, identify the
release-authority transition and relevant change-set classification, and preserve
the applicable architecture/publication baseline. It need not turn ordinary bug
fixes or usability minutiae into individually adjudicated architectural changes.
The Board must determine whether release metadata alone constitutes changed
governed content for the existing archive contract in that case, or explicitly
authorise a different evidence rule for these records. Empty archives must not
claim that an actually changed governed release file did not change; a no-semantic-
delta record must not fabricate an architectural change or evidence package.

An alternative is to revise architecture release allocation so only material
architecture/framework changes advance it. That is a larger governance change
and is not assumed here. Skipping intervening governed releases while describing
the link as “immediate predecessor” would contradict the supplied HCRL semantics.

Maintain one root current-release authority. A pending filename declares a proposed
target rather than allocating it. Before acceptance, recheck the actual predecessor
and intended release under the current concurrent-change-set rule. If another
change set advances it, reconsider scope, comparison and authorisation and record
the correspondence; do not mechanically rename an old HCRL and claim its approval
applies to a changed baseline. This introduces no parallel submission or merge
semantics beyond the installed serial lineage.

Release acceptance, governed update, completed HCRL and relevant metadata belong
to one accepted change set. A governance-only transition need not regenerate
an unchanged ArchiMate publication. The review identifies the applicable model
baseline separately from the current release and does not claim a new published
baseline merely because a patch number changed.

## 10. Candidate implications for ArchiMate and adoption boundaries

### P10 — Specialised ArchiMate producer into a broader review process

**Existing fact:** The [Round-Trip Praxis][roundtrip] defines exact model intake,
independent CRs, reviewed directives, shared semantics and actual Archi proof;
[provenance][provenance] already governs its durable transition evidence.
**Observed problem:** Making ArchiMate review the entire HCRL process would force
all framework/knowledge work into model terminology; retaining two independent
acceptance records would create competing authority. **Candidate proposal:** Keep
the Praxis specialised while general review/closure semantics are governed once.

| Current responsibility | Candidate future relationship to HCRL |
| --- | --- |
| Exact baseline/export/control discovery and transport diagnosis | Remains specialist intake; supplies pinned observations and limits to the release review. No current WIP is consumed during this design task. |
| Canonical continuity, Exchange rebinding and relationship/View evidence | Remains specialist evidence. HCRL summarises material semantic effects and retains required correspondence. |
| CR decomposition and dependencies | Reuse independent-decision granularity. Preserve producer review/run/CR identities and map them to HCRL entries without changing issued decisions. |
| General rule uncovered by an instance | Submit a separate Candidate RAD Knowledge matter where a general rule needs adjudication. Acceptance of the instance alone remains insufficient. |
| Change Review human checkpoint | Becomes an input/checkpoint within the release-level HCRL, with one unambiguous operative adjudication basis. Active specialised reviews can retain working purpose. |
| Reviewed directive/application checkpoint | Continue to require exact authorised scope, prerequisites, instruction revision and applicable governed target state. HCRL status supplies no missing directive. |
| Identity, native type semantics, Home/Composition, navigation and authored Views | Stay governed by specialist contracts; HCRL generalisation does not revise these semantics or permit wholesale regeneration. |
| Schema, deterministic checks and real Archi import/export/inspection | Remain distinct applicable gates. Text-only governance work does not automatically require a new model publication or Archi session. |
| Final review/evidence closure | Material decisions and transition facts consolidate into the completed HCRL under amended common provenance, avoiding a permanently required second semantic review. |
| Two existing user triggers | Keep their current meaning until explicitly updated. A future Praxis may prepare/update an identified target HCRL while retaining the review/application split; this report changes no trigger. |

Prospective governance work would reconcile the provenance contract, common review
definition, ArchiMate review/template, authoring and Round-Trip Praxes, root agent
references, release policy and navigation. These are **candidate impact locations**,
not an instruction to edit all those files. Identity, mapping and domain material
need changes only where an adjudicated semantic impact actually exists. Machinery
then implements the accepted contract in a separately authorised bounded task.

Do not generalise by merely renaming a template: it contains incoming XML/control
assumptions, ArchiMate-specific instruction fields, run-local references and the
current Solution Architect/four-disposition contract. Equally, no wholesale
rewrite is required: most decision/instruction/outcome/evidence semantics already
fit the target.

No retrospective wholesale conversion is recommended. Establish a prospective
start boundary and, if authorised, one exact pilot transition with known state
and acceptance evidence. The existing provenance contract names
`RAD-1.0.10 → RAD-1.1.0` as an intended separately authorised migration/pilot;
this report neither performs nor substitutes a new decision for that pilot.
Historical gaps in a future HCRL traversal remain visible until specifically
authorised reconstruction is possible. Legacy semantic migration, historical
evidence reconstruction, removal of machinery dependencies and disposal are
separate scopes, each with their own authority and preservation checks.

### Axiom consistency of the candidate design

| Material axiom | Consistency argument / design limit |
| --- | --- |
| AX-04 — semantics versus machinery | Generalise established governance meaning; reuse suitable review/evidence mechanisms. Tool data formats and run layouts do not define architectural authority. No new runtime HCRL engine is proposed. |
| AX-08 — evidence records meaning | Completed HCRL retains significant architectural change, decisions and rationale. Raw XML deltas, retries and failed machinery attempts are not automatically architectural history. |
| AX-09 — transient operational purpose | Pending work/processing retain operational importance without acquiring automatic permanent-evidence standing. Unique content and unresolved dependencies still block disposal. |
| AX-14 — semantic distinctions | Keep knowledge kind, standing, instruction, outcome, execution, identity, representation, publication and release separate. No-comment is an explicit decision; resolution does not imply acceptance. |
| AX-15 — uncertain effects | Interrupted application/unknown changed bytes cannot be completed by assumption. Preserve uncertainty and required recovery material until resolved. |
| AX-17 — authority and architectural uncertainty | Every proposed change here remains candidate. Existing authority, partial approvals, gaps and supersessions are preserved; Board questions are explicit. No code-, filename- or pattern-driven completion of missing architecture. |

The candidate model changes repository governance rather than runtime subsystem
boundaries. It assigns no new Mneme/Mnemosyne, Kleio, Dokimasia, Energeia or Ponos
responsibility. AX-18's no-inferred-authority distinction is respected; the Board
protocol is justified by explicit task input and existing acceptance rules, not
inferred from runtime domain authority or a peer's participation.

## 11. Specific matters for Architecture Review Board adjudication

The following are an agenda, not decisions or executable directives. Recommendations
identify the smallest coherent option found during this discovery.

| Matter | Decision needed | Candidate recommendation and consequence |
| --- | --- | --- |
| A01 — acceptance authority (F01/P02) | How does the named Board reach and record an operative architectural decision; who can confirm acceptance or change an issued disposition? | Board review with attributable Mark/Solution Architect confirmation, peer advice/disagreement recorded. Explicitly decide any delegation or alternative rule; preserve agent non-authorisation. |
| A02 — common review/HCRL authority (F02/P01/P10) | Is HCRL the sole final semantic review for every prospective architecture/framework transition, and which existing contract becomes or hosts the common review semantics? | Generalise existing review semantics and amend the existing provenance authority; keep ArchiMate as a specialised producer. Avoid two final records with competing decisions. |
| A03 — filename and package contract (F02/P06/P08) | Approve target-release/iteration naming, completion binding and whether the completed HCRL replaces `change-review.md` as the third evidence component. Decide durable placement and pending-iteration retention. | Use supplied names; one completed record per adjacent transition. Retain current exact-byte archive obligations initially; explicitly adjudicate any optionality. No fourth permanent component. |
| A04 — supporting-detail disposition (F03/P06) | Define `ACCEPTED_WITHOUT_COMMENT`, its reviewed scope, grouped approval, materiality/escalation criteria and application authority. | Explicit bounded Board disposition, never generated default or silence. Amend four-value contracts before use; preserve historical dispositions. |
| A05 — ordinary changes and release lineage (F04/P09) | How does every accepted repository patch release participate in an immediate-predecessor HCRL chain if architectural meaning did not change? | Keep release policy initially; use a concise no-semantic-delta transition record and adjudicate release-metadata/archive treatment. Alternatively amend the release policy explicitly. Do not silently skip releases. |
| A06 — durable taxonomy (F05/P03) | Approve/refine principle scope, constraints/rules/guardrails/invariants, patterns/anti-patterns/exemplars, enduring decisions, requirements, definitions and governance knowledge. | Use semantic kinds plus independent scope/force/standing dimensions. No new precedence hierarchy or compulsory register per category. Existing content is reclassified only through bounded review. |
| A07 — candidate submission and incorporation (F05/F06/F12/P05) | What makes a Candidate RAD Knowledge item reviewable; which dispositions and location/wording decisions permit incorporation? | Explicit statement/evidence, existing-authority comparison, provisional kind/location, questions and bounded incorporation. Allow uncertain classification; distinguish already represented from new acceptance. |
| A08 — navigation and Domain13 (F07/F11/P04) | How should prospective Domain13 ownership statements relate to installed cross-domain governance, and where should the short knowledge guide live? | Preserve current semantic homes while adjudicating the relationship. A guide/index first; no directory migration for neatness. |
| A09 — identity/correspondence (F09/P07) | What stable review/source references suffice for principles/rules/decisions without existing canonical element identity? | Preserve existing canonical IDs and scoped native/section references; record explicit move/replacement correspondence. Do not allocate a new identity regime to all prose. |
| A10 — completion and residual uncertainty (P06/P08) | Define final closure authority, handling of nonblocking deferred matters, factual completion after application authorisation, and correction of a completed record. | Final confirmation binds authorised meaning and actual result. Preserve material decision revisions; later substantive changes use a new governed transition. Adjudicate an explicit non-semantic correction procedure rather than allowing silent complete-record rewrites. |
| A11 — prospective start and historical gaps (F08/F10/P08/P10) | When does HCRL become mandatory; is any historical reconstruction authorised, and how are existing pilot/dependency obligations sequenced? | Prospective start plus a separately bounded pilot. Preserve the existing intended pilot decision and frozen records; no blanket backfill, snapshot rewrite, processing migration or disposal. |
| A12 — significant implementation knowledge (P01/P08/P09) | Which code-derived discoveries change architectural understanding and what exact code/source becomes governed architectural evidence? | Review responsibility/boundary/contract/invariant changes and conformance conflicts; exclude ordinary fixes/minutiae. Machinery-only code remains Git history unless a specifically adjudicated architectural-content role establishes otherwise. |

## 12. Remaining uncertainty and limitations

The following remain deliberately unestablished:

- Board operating rules and the adoption date/scope of the common HCRL model.
- Final taxonomy and a complete assertion-level classification of all existing
  RAD content. This report supplies representative evidence and proposed tests,
  not a retrospective reclassification decision.
- Resolution of prospective Domain13 references, current-navigation drift and
  all remaining legacy ADR/contract dependencies. Finding them does not authorise
  migration, and “Accepted” in a legacy source is insufficient current authority.
- Exact durable HCRL/package layout, treatment of earlier pending versions,
  completion correction procedure and any optional archive rules.
- Treatment of non-material repository releases in the immediate-predecessor
  chain and exact release-metadata evidence in those cases.
- The current processing-dependency graph, uniqueness/recoverability of archived
  architectural bytes, and which historical transitions can be reconstructed.
  These were expressly outside the inspection performed here.
- Whether every future material code concept should become explicit governed
  source, or merely trigger reconciliation of existing RAD authority. Each
  case needs its architectural meaning established.
- Any state, intent, readiness or effects of the separate current Information
  Architecture WIP ingest. None was investigated or adjudicated.

No missing historical approval, identity correspondence, general rule, deployment
boundary or architectural decision has been invented to close these questions.

## 13. Verification and change boundary

The report is the only intended repository addition. The following checks were
performed for this bounded documentation/discovery task:

| Check | Result and practical limit |
| --- | --- |
| Prescribed architecture suite | **PASS:** 90 tests in 11 architecture-test classes; zero failures, errors or skips. Maven BUILD SUCCESS, 18.017 seconds. Command below, bounded to 240 seconds with a 5-second termination grace. This verifies existing implementation invariants, not acceptance of this report's design candidates. |
| Release authority read-only validation | **PASS:** `python3 -B scripts/architecture_release.py check`; current `1.1.1`, previous `1.1.0`. No bump invoked. |
| Report structure, local references and whitespace | **PASS:** CommonMark/table parsing and temporary HTML rendering; 32 headings, 16 tables and three closed fenced blocks. All reference definitions, local file targets and referenced explicit/heading anchors resolve; no trailing whitespace. The tracked diff is empty; status identifies only this new report. These are documentation/preservation checks, not architectural adjudication. |
| Machinery/publication/model execution | Not performed: no intake, current-ingest comparison, reconciliation, publication generation, fixture replay or historical reproduction. The prescribed Java architecture tests do not process ArchiMate ingest material. |

```bash
timeout --signal=TERM --kill-after=5s 240s mvn test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

The command log is engineering validation material at
`/tmp/harmonia-hcrl-discovery-architecture-tests.log`; the material conclusion
is recorded above without making that log architectural authority or permanent
transition evidence. No new tests or HCRL machinery were implemented.

### Canonical Documentation Assessment

Relevant architectural material outside `/docs/markdown` was found:

| Source | Knowledge / role | Canonical destination or relationship | Incorporated in this task? |
| --- | --- | --- | --- |
| Root/docs agent instructions | Repository authority, guardrails and execution obligations. | Existing authoritative instruction role; future common governance semantics may be referenced from RADS after adjudication. No wholesale relocation inferred. | No; used as applicable instructions. |
| `docs/architecture-decisions.md` | Historical decisions, rejected alternatives and rationale, with some section-specific RAD supersession. | Owning domain contracts/definitions or cross-domain governed decision knowledge, after individual reconciliation against current RAD. | No; future work must establish continuing meaning, incorporation and scoped wrappers where required. |
| `docs/README.md` | Engineering navigation and implementation-status classification, with explicit RADS authority disclaimer. | Navigation can point to a future governed knowledge guide; status labels remain separate from architectural acceptance. | No. |
| Maintained ArchiMate framework/entry/processing documentation and selected tools/tests | Machinery responsibilities, specialised workflow support, preservation gates and acknowledged historical dependencies. | Normative governance remains in current RADS contracts; mechanics and tests remain maintained engineering assets. | No; inspected to identify reuse and boundaries, without changing or executing the pipeline. |
| This `.junie/reports` report | Discovery findings and candidate governance design. | Future explicitly accepted meanings belong in adjudicated RAD governance/domain sources; this report remains a submission, not architectural authority. | No proposal installed. |

**Completion statement:** The task produced a discovery/design report for Board
review. No governed architecture, governance document, release metadata, ArchiMate
model, publication, framework machinery, frozen snapshot or existing historical
evidence was modified. No HCRL machinery was created. All proposed changes remain
candidates for adjudication.

## Source references

References point to maintained sources inspected in full or in the bounded
sections identified in the inventory. Linked historical evidence within those
sources was not automatically followed or treated as current authority.

[agents]: ../../AGENTS.md
[docs-agents]: ../../docs/AGENTS.md
[axioms]: ../../docs/markdown/governance/architectural-axioms.md
[provenance]: ../../docs/markdown/governance/architecture-change-provenance.md
[release]: ../../docs/markdown/governance/architecture-release-policy.md
[release-json]: ../../architecture-release.json
[release-helper]: ../../scripts/architecture_release.py
[review]: ../../docs/markdown/governance/archimate-ingestion-change-review.md
[template]: ../../docs/markdown/governance/templates/archimate-ingestion-change-review-template.md
[authoring]: ../../docs/markdown/governance/architecture-authoring-praxis.md
[roundtrip]: ../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md
[governance]: ../../docs/markdown/governance/README.md
[modelling]: ../../docs/markdown/governance/architecture-modelling-strategy.md
[completion]: ../../docs/markdown/architecture-completion-plan.md
[axiom-orientation]: ../../docs/markdown/01-motivation/principles/architectural-axioms.md
[reclassified]: ../../docs/markdown/01-motivation/principles/reclassified-principles.md
[foundational]: ../../docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md
[external]: ../../docs/markdown/01-motivation/requirements-constraints/external-constraints.md
[requirements]: ../../docs/markdown/01-motivation/requirements-constraints/master-requirements-catalogue.md
[tiers]: ../../docs/markdown/02-strategy/capability-maps/capability-tier-model.md
[enterprise]: ../../docs/markdown/02-strategy/capabilities/enterprise-capabilities.md
[business-model]: ../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md
[business-information]: ../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md
[dependencies]: ../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md
[information-guardrails]: ../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md
[information-model]: ../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md
[collections]: ../../docs/markdown/04-information-architecture/patterns/containment-and-collections.md
[metabole]: ../../docs/markdown/04-information-architecture/patterns/metabole.md
[domain05]: ../../docs/markdown/05-information-systems-architecture/README.md
[components]: ../../docs/markdown/05-information-systems-architecture/component-register.md
[data]: ../../docs/markdown/05-information-systems-architecture/data-and-persistence.md
[gaps]: ../../docs/markdown/05-information-systems-architecture/gaps.md
[attached-note]: ../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Energeia-DigitalTwin-Dokimasia-Specialisation.md
[handoff-note]: ../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mneme-Publication-Subscription-and-Petasos-Handoff-Architecture.md#21-architectural-principles-established
[step-b]: ../../docs/markdown/05-information-systems-architecture/traceability/step-b-method.md
[worked-example]: ../../docs/markdown/05-information-systems-architecture/traceability/health-information-access-worked-example.md
[assurance-review]: ../../docs/markdown/01-motivation/reviews/independent-assurance-reconciliation.md
[decomposition-decision]: ../../docs/markdown/02-strategy/reviews/fixed-capability-decomposition-decision.md
[g1]: ../../docs/markdown/04-information-architecture/reviews/package2-g1-review.md
[g2]: ../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md
[sources]: ../../docs/markdown/05-information-systems-architecture/sources.md
[b1-review]: ../../docs/markdown/05-information-systems-architecture/reviews/b1-method-reconciliation.md
[identity]: ../../docs/markdown/governance/canonical-identity-and-naming.md
[old-identity]: ../../docs/markdown/governance/architecture-identity/model.md
[mapping]: ../../docs/markdown/governance/rad-archimate-model-mapping.md
[baseline]: ../../docs/markdown/governance/baseline/README.md
[scratchpad]: ../../docs/markdown/scratchpad/architecture-design-notes.md
[archimate-entry]: ../../docs/archimate/README.md
[publish-index]: ../../docs/archimate/exchange/publish/README.md
[processing]: ../../docs/archimate/processing/README.md
[ingestion-layout]: ../../docs/archimate/processing/ingestion/README.md
[framework]: ../../docs/archimate/framework/README.md
[intake-code]: ../../docs/archimate/framework/tools/roundtrip_praxis.py
[review-code]: ../../docs/archimate/framework/tools/change_review.py
[correlation-code]: ../../docs/archimate/framework/tools/element_correlation.py
[adapter-guide]: ../../docs/archimate/framework/tools/reconciliation/README.md
[intake-tests]: ../../docs/archimate/framework/tests/test_roundtrip_praxis.py
[review-tests]: ../../docs/archimate/framework/tests/test_change_review.py
[legacy-decisions]: ../../docs/architecture-decisions.md
[docs-index]: ../../docs/README.md
[specialisation]: ../../docs/markdown/05-information-systems-architecture/README.md#specialisation-principle
[terminal]: ../../docs/markdown/05-information-systems-architecture/component-register.md#dokimasia-terminal-boundaries
