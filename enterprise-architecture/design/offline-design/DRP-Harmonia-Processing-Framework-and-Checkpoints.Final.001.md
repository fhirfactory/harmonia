<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal — Harmonia Processing Framework and Checkpoints

**Status** · Final.001 — Accepted with qualifications  
**Basis** · Architecture 1.1.26 · Software 0.1.0 · G03 / Iteration 005 / DR-12  
**Solution Architect** · Mark Hunter. [Solution Architect acceptance][acceptance-task] · [Accepted Draft.000.004][accepted-draft] · [Draft.000.004 execution report][accepted-draft-report] · [Draft.000.004 adjudications][final-review-task] · [Preceding draft][previous-draft] · [Preceding execution report][previous-report] · [Terminology reconciliation][terminology] · [HCRL.004 / DR-12][hcrl]

## 1. Scope and standing

This proposal describes three processing concerns. Its primary design subject is **Intra-Module Orchestration**, with Ponos and the common Ergon framework as the reference. Inter-module progression and OA&M Oversight define how that local processing relates to existing owners. The tiers are logical boundaries of responsibility. They imply no deployment layout, additional execution engine, coordination platform or common route for every module.

| Standing | Treatment in this DRP |
| --- | --- |
| Accepted architectural authority | Architecture 1.1.26 and canonical Principles govern the design. The accepted two-cache allocation, common Ergon checkpoint/recovery responsibility, authoritative persistence and independent Assurance remain controlling. Use accepted checkpoint Features for boundaries, required durability, recovery integrity and completeness. [Release authority][release] · [Principles][axioms] · [1.1.24 DR-1][persistence] · [1.1.24 DR-3][cache] · [Assurance][assurance] |
| Accepted offline design | The Solution Architect accepted the exact content of Draft.000.004 subject to all its qualifications and outstanding obligations. Final.001 records that bounded acceptance under DR-12. The collective G03 review remains pending. [Solution Architect acceptance][acceptance-task] · [Accepted Draft.000.004][accepted-draft] |
| Existing qualified designs | Ponos, Ergon, Praxis, Pathway endpoints, Petasos and Pylai contributions retain their recorded standing. G01 Final.001 records the accepted Ergon security design and preserves its transport and criteria-binding qualifications. Existing G03 Pragma, Pragmaton, publication and routing DRPs retain candidate standing. [Terminology authority matrix][terminology] · [G01 review decision][g01-decision] · [G03 review][hcrl] |
| Issued review direction | Use the three-tier model and a lightweight synchronous Checkpoint at each functional handover in the candidate Tier 1 framework. Preserve configurable logging, audit and checkpoint persistence, governed housekeeping and independently attributable evidence. The Draft.000.002 direction establishes the route sequence. Draft.000.003 specifies WorkSpace information, status ownership, Context Unloader, Praxis processing coordination and the Egress Filter. Draft.000.004 settles the full-Pragma Egress transfer, the distinct originating Pragma role and the final disposition of processing status. [Issued design direction][task] · [Draft.000.002 direction][revision-task] · [Draft.000.003 adjudications][adjudication-task] · [Draft.000.004 adjudications][final-review-task] |
| Candidate mechanisms and detailed design | Camel routes and processors, CDI lifecycle, named handover points, configuration levels and adaptations are assessed within the boundaries below. The detailed design and conformance work in §7 retains its recorded standing. |

AX-04 supports reuse of Camel and Artemis while keeping semantics with Harmonia. AX-05/06/09 distinguish active state, authoritative persistence and the meaning of evidence. AX-07/08 preserve authorisation and meaningful audit. AX-10/11 protect distributed recovery and responsiveness, while AX-14/15/16 require distinct, observable outcomes and explicit uncertainty. AX-17 preserves the standing of candidate decisions and any unestablished correspondence. [Canonical Principles][axioms]

## 2. Three-tier architecture

| Tier | Purpose and boundaries |
| --- | --- |
| **1 · Intra-Module Orchestration** | Sequence processing inside a module. Candidate Camel routes combine functional stages and lightweight synchronous named Checkpoints. The common framework supplies reusable contextualisation, enforcement, integration and recovery scaffolding, governed Mneme housekeeping and attributable operational, audit, persistence and Assurance evidence as applicable. Ponos/common Ergon is the reference. DigitalTwin-Behaviour is a specialised application. |
| **2 · Inter-Module Orchestration** | Coordinate progression and durable work transfer between modules. For defined Praxis progression, Ponos owns interpretation and evaluation, and Petasos fulfils the resulting routing obligations in the G03 design. For dynamic publication/subscription, Petasos matches subscriptions and routes subscriber-specific undertakings constructed through Mneme. Petasos comprises Apache ActiveMQ Artemis and Harmonia-specific support functionality for routing, queues, delivery and reconciliation. Work identity, authoritative persistence, Themis enforcement, execution admission and execution recovery keep their existing boundaries. [Praxis][praxis] · [Petasos][petasos] · [G03 publication][publication] · [Routing and reconciliation][routing] |
| **3 · OA&M Oversight** | Observe operations across modules and present governed evidence for operational review and independent Assurance. The tier is less mature. Correlation, investigation, exception detection, recovery oversight and authorised intervention are subjects for exploration. It does not add a progression decision-maker or transfer operational control. [Operational State Reporting][operational] · [Iris][iris] · [Assurance][assurance] |

OA&M reuses the contributing boundaries below. Operational evidence and presentation provide context for investigation. Any eventual intervention requires its own authorisation and owning operation.

| Component | Responsibility in OA&M Oversight |
| --- | --- |
| Iris | Present governed operational observations, status and metrics and convey authorised operator intent through existing interfaces. |
| Kleio | Capture required auditable occurrences and construct and preserve authoritative AuditEvents. |
| Dokimasia | Independently evaluate relevant evidence against applicable Assurance expectations. |
| Ponos / Petasos | Contribute attributable execution/progression or routing/delivery events and observations for their respective operations. |
| Mneme | Supply governed access to relevant operational and information state. |

### 2.1 Tier 1 applications and adaptations

| Application | Scope of reuse |
| --- | --- |
| Ponos / common Ergon | Reference route in §4. Ponos retains execution and recovery coordination and Praxis interpretation. Common Ergon supplies the execution-side context, checkpoint and recovery scaffolding. |
| DigitalTwin-Behaviour | Reuse appropriate common Ergon processing with fresh, entity-relative context for each Behaviour. Preserve within-Twin serialisation, work scope and the governing stop and failure semantics. Reuse of processors supplies no concurrency entitlement inside a Twin. |
| Pylai-Message-In | Adapt receipt, protocol parsing and authorisation, mapping and durable handover to the applicable inbound acknowledgement contract. Distinguish external receipt, queue acceptance and required authoritative work establishment. |
| Pylai-Message-Out | Adapt governed output projection, transmission, delivery outcomes and remote acknowledgements to the external protocol. Preserve external release restrictions and delivery/retry ownership. |
| Pylai-API-Server | Adapt the route to governed authentication/authorisation, request/response and applicable internal changes or read/search handling. Preserve the qualified distinction between Pathway-backed changes and Diadromí-Controller reads/searches. |
| Pylai-API-Client | Adapt outbound invocation and response, timeout and failure handling to the relevant protocol, security, egress and governing processing contract. |

The Pylai names identify candidate adaptation contexts. Each retains its interoperability and protocol obligations, separates its own acknowledgements from internal work acceptance, and selects stages for its actual interaction. [Common Ergon recovery][cache] · [DigitalTwin][twin] · [Pylai interoperability boundary][pylai] · [Repository ingress and egress guardrails][agents]

## 3. Intra-module processing framework

Apache Camel is the primary candidate mechanism for sequencing the processing functions and handovers. A processing route in this proposal is a conceptual Camel sequence. The qualified Pathway vocabulary also uses **PathwayTraversal** for internal passage and **Route** for ordered inter-component segments. Final correspondence of terms remains for review. A route's processing stages and named Checkpoints express semantics before selecting endpoints or adapters. [Terminology reconciliation][terminology] · [Qualified Pathway vocabulary][pathway-model]

`direct:` is a candidate for synchronous invocation within a Camel context. `seda:` provides asynchronous in-process queuing and supplies no durable recovery after JVM loss. It can be assessed for interactions that allow a separate asynchronous boundary. Artemis-backed messaging supports appropriate asynchronous or durable handoffs under their owning contracts. The approved mandate routes every PetasosParcel through Artemis, including co-located participants. Transport selection must preserve the interaction's security, concurrency, acknowledgement and recovery requirements. [Camel Direct][camel-direct] · [Camel SEDA][camel-seda] · [Exclusive PetasosParcel Routing][artemis]

Functional stages may use Camel-compatible `Processor` behaviour, illustrated as:

```java
void process(Exchange exchange);
```

The common Ergon framework supplies the surrounding scaffolding. Domain-specific `Ergon.Process` remains bounded business logic using the applicable working context. Framework and collaborating component responsibilities supply contextualisation, security, integration, logging, audit and recovery. Independent Assurance remains with Dokimasia. The illustration supplies no new Harmonia interface. [Camel Processor][camel-processor] · [Common Ergon][ergon] · [Ergon–Mneme interaction][context] · [Assurance][assurance]

Jakarta EE/CDI-managed functional objects are a candidate realisation, with `@Dependent` a lifecycle option. A dependent bean injected into a route owner is tied to that owner or lookup lifecycle. Its scope alone does not create one instance per Camel Exchange. Protect each execution's authorised working state and context under concurrency, failure and retry. Exact instance ownership and destruction, sharing, pooling, threading and throughput are implementation matters. Establish correct execution and isolation first, then optimise throughput with measured evidence. [CDI 4.0 §2.5.4][cdi] · [Ergon Execution Isolation][isolation]

## 4. Reference Ponos / Ergon processing route

The Solution Architect directed the following sequence for the candidate. This is **architectural pseudocode**. The checkpoint forms identify handover positions. Exact Camel DSL, Java classes, endpoint configurations and implementation interfaces remain detailed design matters. The common Ergon framework and Ponos retain their existing responsibilities. [Draft.000.002 direction][revision-task] · [Draft.000.003 adjudications][adjudication-task] · [Terminology reconciliation][terminology]

```java
from(ergon.ingress.queueId)
    .checkpoint()
    .process(ergon.ingress.pep)
    .checkpoint()
    .process(ergon.context-load)
    .checkpoint()
    .process(ergon.processor)
    .checkpoint()
    .process(ergon.egress.context-unloader)
    .checkpoint()
    .process(ergon.egress.output-unload/validate)
    .checkpoint()
    .process(ergon.egress.outcome-decision)
    .checkpoint()
    .process(ergon.egress.filter)
    .checkpoint()
    .process(ergon.egress.pep)
    .checkpoint()
    .process(ergon.egress.pragma-create)
    .checkpoint()
    .process(petasos.route-in)
    .to(checkpoint());
```

### 4.1 Logical processing row

```text
IngressEndpoint → [H01 Checkpoint] → Ergon.PEP.In → [H02 Checkpoint] → Context Loader → [H03 Checkpoint] → Ergon.Process → [H04 Checkpoint] → Context Unloader → [H05 Checkpoint] → Output Unload/Validate → [H06 Checkpoint] → Outcome Decision → [H07 Checkpoint] → Egress Filter → [H08 Checkpoint] → Ergon.PEP.Out → [H09 Checkpoint] → Originating Pragma Creation → [H10 Checkpoint] → Petasos Route-In → [H11 Checkpoint]
```

H01–H11 are provisional diagram references. H01 marks the initial receipt and H11 marks the terminal observation of the applicable Petasos handover effect. The row depicts the order of processing. Each handover retains its own outcomes and evidence requirements under §§4.2 and 5. The exact acceptance, acknowledgement and evidence issuer for H11 remain subject to the Petasos contract. [Draft.000.003 adjudications][adjudication-task] · [Routing and reconciliation][routing]

### 4.2 Stage and Checkpoint correspondence

| Processing slot | Governed or candidate correspondence and responsibility | Following Checkpoint and evidence boundary |
| --- | --- | --- |
| `from(ergon.ingress.queueId)` | **IngressEndpoint** (qualified C29) or `Ergon.In` (G01) receives the work trigger. Petasos owns its applicable queue and delivery. The queue identifier is an adapter binding. | **H01** · Receipt at the Ergon boundary, separately from execution admission. |
| `ergon.ingress.pep` | **`Ergon.PEP.In`** hosts Themis enforcement and controls admission before processing. Themis owns policy and enforcement semantics. | **H02** · Authorised admission, denial or unavailable determination under the applicable contract. |
| `ergon.context-load` | **Context Loader** (qualified **Ergo Context Loader**, G03 `Ergon.ContextLoader`) establishes the execution WorkSpace with Mneme and loads Category 1 IUs, immutable within the Ergon Processor. | **H03** · Initial permitted working context available, or the owning load outcome. |
| `ergon.processor` | **`Ergon.Process`** executes the bounded business logic using immutable context-loaded and explicitly retrieved IUs. It produces output IUs in `.output()` and separately produces and maintains `Workspace.Processor.Status`. Ergon owns the logic and status. Ponos governs execution. The G03 `Ergon.ProcessingEngine` naming question retains its standing. | **H04** · The logic's return or other observed result, attributable to the producing instance. Outcome Decision determines the execution outcome recorded at H07. |
| `ergon.egress.context-unloader` | **Context Unloader** uses `Workspace.Processor.Status` and the Ergon's business-defined branch rules to select generated-content release branches and prepare content and IU References. It transfers the entire Pragma by value into the Camel Exchange and closes the WorkSpace. | **H05** · The synchronous Checkpoint receives the Egress Pragma by value through the Exchange. Discrete IU establishment is evidenced at H06. |
| `ergon.egress.output-unload/validate` | **Output Unload/Validate** extracts the applicable generated output information from the Egress Pragma carried by value in the Exchange. It validates and establishes discrete InformationUnits through the applicable governed Mneme contracts, independently of the closed WorkSpace. | **H06** · Validation, creation and persistence outcomes for the discrete IUs. Successful establishment requires confirmation of the required creation and persistence outcomes. |
| `ergon.egress.outcome-decision` | **Outcome Decision** determines the actual Ergon instance's attributable execution outcome under the applicable Praxis rules. The undertaking and parent-work dispositions retain their own rules. | **H07** · Ergon-instance TaskOutcome in G03 terms, with the attempt, work and evidence attributable independently of downstream decisions. |
| `ergon.egress.filter` | **Egress Filter** first applies dead-task blocking under the applicable Pragma and Praxis contracts, then evaluates the producing Ergon's PublishingMask on the remaining output IUs. Ponos owns publication eligibility and Mneme evaluates the mask. | **H08** · Preserve the distinct reasons and evidence for a dead-task block and a mask disposition. An ordinary mask non-match leaves the IU governed under its own lifecycle. |
| `ergon.egress.pep` | **`Ergon.PEP.Out`** hosts Themis enforcement for onward release after the Egress Filter. | **H09** · Release authorisation, denial or unavailable determination. Originating work establishment and handover have separate outcomes. |
| `ergon.egress.pragma-create` | **Originating Pragma Creation** uses eligible and authorised output IU identities to request construction of new originating Pragmas. It invokes Mneme's PragmaFactory internally and requests authoritative persistence through the existing Mneme/Mnemosyne path. Each originating Pragma has a work identity distinct from the Egress Pragma. | **H10** · Construction and persistence results, with only successfully established originating Pragma identities available for handover. |
| `petasos.route-in` | **Petasos Route-In** is the candidate intake label. Ergon Egress hands over the established list across its qualified `EgressEndpoint` (G01 `Ergon.Out`) to Petasos under the applicable routing-acceptance contract. | **H11** · The fact confirmed by the owning Petasos contract. Intake, routing responsibility, queue acceptance, delivery and subsequent execution each retain separate evidence. |

The stage labels above identify functional responsibilities and the following Checkpoints identify their evidence boundaries. Detailed names, mandatory evidence and durable recovery boundaries remain subject to review. [Pathway endpoints][endpoint] · [G01 §4][g01] · [Ergon–Mneme interaction][context] · [G03 publication][publication] · [Ergon Egress][routing-egress] · [Draft.000.004 adjudications][final-review-task]

### 4.3 Supporting interactions and egress obligations

- **WorkSpace and output establishment.** Context Unloader prepares generated content and passes the entire Pragma by value into the Camel Exchange. The Exchange supplies the Egress context after WorkSpace closure. Output Unload/Validate takes the generated output information from that Pragma, validates it and uses Mneme to establish discrete IUs. Mnemosyne establishes the required authoritative durable effects. These outcomes remain attributable separately from preparation, handover and WorkSpace close-off. [Draft.000.004 adjudications][final-review-task] · [Accepted persistence contracts][persistence]
- **Execution outcome.** After Output Unload/Validate, Outcome Decision determines the actual Ergon instance's execution outcome under Praxis-defined rules, with attribution to the work and attempt. Preserve separate outcomes for WorkSpace close-off, output IU persistence, the two Egress Filter checks, policy enforcement, originating Pragma creation, routing acceptance and downstream execution. The operational processing status and its closure AuditEvent retain their own lifecycle. Execution success establishes the execution result only. G03 uses TaskOutcome for the Ergon instance and treats undertaking and parent-work dispositions independently. The existing TaskOutcome Topic describes ActionableTask resolution and retains that meaning. Formal correspondence remains for review. [G03 execution outcomes][pragma] · [TaskOutcome Topic][task-outcome]
- **Dead-task blocking, eligibility and release.** Within `ergon.egress.filter`, Ergon Egress first excludes content or work that must stop under the applicable task state. Any status consequences follow the governing Pragma and Praxis contracts. The producing Ergon's PublishingMask is then evaluated on the remaining output IUs, with the existing candidate default `ALLOW_ALL`. Ponos owns publication eligibility and Mneme evaluates the mask. A mask non-match leaves the IU governed and excludes it from ordinary publication. The qualified baseline's Publishing Pattern on egress Pragma retains its outstanding correspondence. The subsequent `Ergon.PEP.Out` determines Themis authorisation for onward release independently of both filter checks. [Draft.000.003 adjudications][adjudication-task] · [Pragma Management][publish-base] · [Publication and subscription][publication] · [Ergon Egress][routing-egress] · [G01 §4][g01]
- **Originating Pragma creation and handover.** After outbound enforcement, `ergon.egress.pragma-create` receives the eligible and authorised output IU identities and invokes Mneme's PragmaFactory internally. This call takes place within the Pragma Create function. Mneme applies the governed construction and persistence contracts, with Mnemosyne establishing authoritative durability. Only successfully established identities of newly created originating Pragmas may be returned. The function supplies that list to `petasos.route-in` under the applicable handover contract. Preserve uncertainty about partial or indeterminate establishment and the outstanding proof of complete list membership. Applicable authoritative Provenance can support reconstruction of the original complete list under the existing recovery contract. [PragmaFactory][factory] · [Ergon Egress][routing-egress] · [Egress recovery][routing-recovery]
- **Petasos routing and Praxis selection.** Petasos owns routing acceptance and applies its own governed FORWARD enforcement. Subscription-Router receives originating work and requests subscriber-specific Pragmatosis after durable decision capture. Praxis-Router invokes the separate Ponos-owned Praxis evaluation to select from the established originating Pragmas and fulfils the recorded obligations. Publication eligibility, protected release and Praxis progression retain their respective decisions. [Petasos][petasos] · [Routing and reconciliation][routing] · [Praxis-Router][praxis-router]

### 4.4 WorkSpace information and Context Unloader

The WorkSpace exposes the Mneme-managed Pragmaton for the execution. Context Loader establishes the initial governed context with Mneme. The candidate Ergon-InformationAdapter mediates explicitly requested access under the applicable Themis controls. The Processor uses existing governed information and produces generated information in these three categories. [Ergon–Mneme interaction][context] · [Draft.000.003 adjudications][adjudication-task]

| Category | Source and treatment |
| --- | --- |
| 1 · Context-loaded information | `ergon.context-load` loads existing governed InformationUnits to establish the initial execution context. Those IUs are immutable within the Ergon Processor. |
| 2 · Explicitly retrieved information | The Ergon's business logic explicitly retrieves existing governed InformationUnits as needed during execution. Those IUs are also immutable within the Ergon Processor. |
| 3 · Processor-generated information | The Processor produces newly created InformationUnits in the WorkSpace's `.output()` collection and separately produces and maintains `Workspace.Processor.Status`. Output IUs and processing status represent different information. Output Unload/Validate establishes discrete output IUs under the governing contracts. |

`Workspace.Processor.Status` is Ergon-owned processing information. It can contain substantial transitive processing details beyond diagnostics and the execution outcome. Those details matter to the eventual DigitalTwin-Behaviour design. The Ergon's business logic defines the branch-selection rules. The common framework supplies the Context Unloader that applies them. [Draft.000.003 adjudications][adjudication-task]

`ergon.egress.context-unloader` interprets `Workspace.Processor.Status` and applies the branch-selection rules supplied by that Ergon. It selects the applicable release branches and prepares the generated content and its reference relationships. Where the intended representation uses References, the Unloader prepares those References in place of embedded actual data, within the accepted InformationUnit framework. The context-loaded and explicitly retrieved IUs remain immutable during unloading. [Draft.000.003 adjudications][adjudication-task] · [Accepted InformationUnit framework][iu-framework]

#### WorkSpace-to-Egress transfer

Context Unloader owns the WorkSpace-to-Egress handover. It releases **the entire Pragma by value into the Camel Exchange** and then closes the WorkSpace. This supersedes the output-only interpretation of the transfer. The Pragma value includes the prepared generated output information and the applicable execution context, preserving governed IU References as references. The Pragma value in the existing Exchange supplies the Egress processing context. Subsequent Egress functions operate on that Pragma and can proceed independently of the closed WorkSpace. Pass-by-value defines the architectural handover irrespective of the eventual implementation technique. The Exchange supplies intra-module transfer. Mneme manages governed information and Mnemosyne establishes authoritative durable state. [Draft.000.004 adjudications][final-review-task] · [Pragma Topic][pragma-topic] · [Accepted persistence contracts][persistence]

Newly generated IUs remain represented in the output information carried by the Egress Pragma until established through the applicable governed contracts. Output Unload/Validate extracts the applicable output information from the Exchange, validates the generated IUs and obtains the required Mneme creation and persistence outcomes. This follows the Context Unloader's content preparation and transfer at H05. [Draft.000.004 adjudications][final-review-task] · [Accepted InformationUnit framework][iu-framework] · [Accepted persistence contracts][persistence]

| Pragma role | Identity and responsibility |
| --- | --- |
| **Egress Pragma** | The existing execution Pragma transferred in full by value from the WorkSpace. It retains its work identity and carries the output information and execution context for the remaining Egress processing. |
| **Newly created originating Pragmas** | Distinct work instances established later by Pragma Create. It invokes Mneme's PragmaFactory internally with eligible and authorised output IU identities. The applicable governed contracts establish each originating Pragma. The successfully established identities are the list supplied to Petasos Route-In. |

Both are roles of the existing Pragma information model. [Draft.000.004 adjudications][final-review-task] · [G03 work identity and realisation][pragma-work] · [Ergon Egress][routing-egress]

### 4.5 Status domains and processing-instance closure

Each status domain retains its own meaning, lifecycle and decision authority. Mneme manages persistence and availability for all three domains. The semantic owner makes the decisions that change the status, and authoritative durability follows the existing Mneme/Mnemosyne contracts where applicable. [Draft.000.003 adjudications][adjudication-task] · [Accepted persistence contracts][persistence]

| Status domain | Meaning | Semantic owner | Persistence and availability |
| --- | --- | --- | --- |
| `Workspace.Processor.Status` | Information produced during a particular Ergon execution, including details used to prepare applicable generated-content release branches. | Ergon | Mneme |
| `Pathway.Processing.Status` | Praxis processing and progression status, including functional concurrency information and coordination details. | Ponos | Mneme |
| `Pragma.Metadata.Status` | Status in Pragma metadata. Individual transitions follow their owning Pragma and Praxis contracts. | Ergon or Ponos, depending on the transition. The allocation remains open. | Mneme |

Allocation of individual `Pragma.Metadata.Status` transitions remains a subsequent design question. The Context Unloader, Outcome Decision, Praxis.Agent, Praxis.Server and Mneme have only their stated responsibilities in this candidate. Outcome Decision remains responsible for the separate, attributable Ergon-instance execution result. [G03 execution outcomes][pragma] · [Draft.000.003 adjudications][adjudication-task]

#### Workspace.Processor.Status lifecycle

**`Workspace.Processor.Status` ends its lifecycle in an AuditEvent following closure of the processing instance for the applicable Pragma.** [Draft.000.004 adjudications][final-review-task]

| Phase | Responsibility |
| --- | --- |
| Creation and maintenance | Ergon business logic produces and maintains the status during processing. |
| Operational persistence and availability | Mneme provides the required persistence and access during processing. |
| Context unloading | Context Unloader consumes the status and the Ergon's branch rules to select and prepare generated content for release. |
| Processing-instance closure | The status completes its operational lifecycle following closure of the processing instance for the applicable Pragma. |
| Final disposition | The complete `Workspace.Processor.Status` is captured in the authoritative AuditEvent associated with closure of that processing instance. Kleio constructs and preserves the AuditEvent. |

The authoritative AuditEvent preserves the complete status. Governed references may represent substantial underlying content where the referenced historical state can be reliably resolved. The operational status object and the authoritative AuditEvent retain their distinct lifecycles. The detailed audit representation, capture interaction and timing remain audit integration decisions within Kleio's existing mandatory capture and fail-closed requirements. Dokimasia retains independent Assurance under its existing terminal rules. [Draft.000.004 adjudications][final-review-task] · [Kleio, immutable history and evidence][kleio-history] · [Kleio auditability][kleio] · [Independent Assurance][assurance] · [Dokimasia self-audit boundary][self-audit]

### 4.6 Ponos Praxis processing coordination

Ponos owns `Pathway.Processing.Status` and the functional coordination of Praxis processing, including its progression and concurrency semantics. Mneme manages persistence and availability of that status. This is a Tier 2 orchestration responsibility with a local interaction at the Ergon execution framework. Energeia retains management of the Praxis definition and its applicable context, criteria and assurance basis. [Praxis ownership and fulfilment][praxis] · [Draft.000.003 adjudications][adjudication-task]

- **Praxis.Server (Manager)** is the Ponos-owned logical component that manages Praxis processing and its applicable coordination and concurrency semantics.
- **Praxis.Agent** is a Ponos-owned logical component embedded in each **Ergon-Execution-Module**. The Ergon framework interacts with its local Agent for relevant Praxis processing activities. The Agent provides the local boundary to the Ponos-owned management functionality.

The components express Ponos's existing processing responsibility. Their exact interface and internal division of responsibilities remain open. Concurrency here concerns functional orchestration. Transport, deployment and the implementation of concurrency are detailed design matters. [Draft.000.003 adjudications][adjudication-task]

## 5. Checkpoint architecture

### 5.1 Purpose and processing contract

Every functional handover in the proposed Tier 1 framework has a lightweight, synchronous, named Checkpoint. It validates the incoming basis where the handover requires it, applies conditional logging and audit, forwards synchronously, verifies the relevant forwarding outcome and responds under the governing contract. It may call APIs supplied by existing components as needed for that interaction.

> Checkpoint execution is mandatory at the defined handover. Checkpoint-state persistence is determined by the applicable checkpoint policy and mandatory recovery requirements.

The accepted Features retain their requirements for defined recovery boundaries, durable required task checkpoints, valid identifiable recovery state and evidence of completeness. [Checkpoint Boundary Definition][checkpoint-boundary] · [Durable Task Checkpoint][checkpoint-durable] · [Recoverable Checkpoint Integrity][checkpoint-integrity] · [Checkpoint Completeness Evidence][checkpoint-completeness]

H05 receives the entire Egress Pragma by value in the existing Camel Exchange between Context Unloader and Output Unload/Validate. H05 verifies the synchronous handover under its existing Checkpoint contract. Any required durable state uses the existing governed persistence path under the applicable checkpoint policy and mandatory recovery requirements. [WorkSpace-to-Egress transfer](#workspace-to-egress-transfer) · [Accepted persistence contracts][persistence]

The following **conceptual pseudocode** expresses those steps. `checkPersistedPragmaOrIU` applies where persisted state is a precondition. The stopping branches withhold dependent activity and use the outcome semantics in §5.2.

```java
from(checkpointIn)

    .process(checkPersistedPragmaOrIU)

    .choice()
        .when(checkFailed)
            .process(logIfRequired)
            .process(auditIfRequired)
            .stop()
    .end()

    .process(logIfRequired)
    .process(auditIfRequired)

    .to(checkpointOut)

    .process(checkSuccessfulForward)

    .choice()
        .when(forwardFailed)
            .process(logIfRequired)
            .process(auditIfRequired)
            .stop()
    .end();
```

The Checkpoint's internal sequence is synchronous, including its applicable forwarding verification. It introduces no asynchronous endpoint inside that sequence. A synchronous handover at the edge of a wider asynchronous flow can establish only the fact confirmed by its owning contract, such as durable acceptance where proved. It does not establish delivery or downstream execution. The governing operation still determines how to handle timeout, failure and uncertain effects. [Issued design direction][task] · [Explicit Indeterminate Outcome][indeterminate] · [Petasos routing and reconciliation][routing]

### 5.2 Outcomes and recovery

**Pass/Fail** describes the conceptual control decision to proceed or withhold a dependent step. Preserve the actual owning outcome and its evidence. An indeterminate write, denied access, unavailable authorisation, deferred execution or non-match for publication retains its distinct meaning. A known filter non-match is an ordinary disposition. A timeout is insufficient to decide occurrence or non-occurrence. Withhold claims of success lacking required proof and reconcile uncertain effects before any unsafe retry. The model prescribes no Boolean, two-value enum or universal outcome type. [AX-14/15][axioms] · [Explicit Indeterminate Outcome][indeterminate] · [Accepted persistence][persistence]

Common Ergon owns execution checkpoints and contextual recovery. Ponos coordinates governed execution. Petasos retains its durable handoff and redelivery/reconciliation responsibilities. Recover at the owning boundary using durable work, authoritative information and sufficient evidence of valid recovery state. A checkpoint observation does not authorise re-execution, change work identity or resume an Ergon internally after failure. [Common Ergon recovery][cache] · [Routing and reconciliation][routing] · [Checkpoint integrity][checkpoint-integrity]

### 5.3 Configuration and housekeeping

Each named Checkpoint supports independent **logging**, **audit** and **checkpoint persistence** settings. The proposed granularity levels are `COARSE | MEDIUM | FINE | EXTREME`. The levels describe extent of capture, with exact semantics, defaults and configuration policy left for design. Mandatory security, information governance, authoritative durability, audit capture and recovery obligations establish minimum behaviour at every level. An optional logging or observation path cannot supply missing mandatory audit evidence. Clients submit meaningful auditable occurrences. Kleio constructs and preserves authoritative AuditEvents and the affected operation fails closed if required capture cannot be established. Use existing diagnostic controls, including protection of PHI. [Canonical AX-07/08/09][axioms] · [Kleio capture][kleio] · [Pending audit-staging design][audit-draft]

Use existing Mneme capabilities for governed, high-speed access to relevant persisted facts and lightweight operational housekeeping. Keep ephemeral state, cached representations of governed information, authoritative persistence and evidence of a particular outcome distinguishable. Mneme manages active information and obtains authoritative facts through the applicable contracts. Mnemosyne establishes authoritative durable state. Cache residence or a local success flag is insufficient to prove durable acceptance. Preserve the two-cache and governing access allocations. Keep a Checkpoint lightweight by avoiding persistence, serialisation, network calls and other work where its contract does not require them. [Mneme][mneme] · [Cache governance][cache] · [Accepted persistence][persistence]

### 5.4 Independent Assurance

Checkpoints and functions contribute attributable observations and evidence of their own handovers. Correlate material evidence to the relevant work, execution and attempt, stage and known outcome under the owning contracts. Evidence completeness and retention minima remain for adjudication. Dokimasia independently evaluates evidence against applicable Praxis and other governing expectations. Journey records applicable observations, Provenance describes lineage and activity, and Kleio retains AuditEvents. LogEvents describe diagnostic or operational matters. These representations do not supply the assessment result. Processing functions provide evidence without deciding their own Assurance. Preserve the finite Assurance boundary and the existing self-audit rule. [Accepted Assurance and Provenance][assurance] · [Journey][journey] · [Dokimasia][dokimasia] · [Finite boundary][finite] · [Self-audit constraint][self-audit]

## 6. Reconciliation and G03 dependencies

| Surface | Correspondence and decision needed |
| --- | --- |
| WorkSpace, Context Unloader and Pragma output | **Resolved within this DRP.** The Context Unloader prepares generated content and IU References under Ergon-defined branch rules, hands the entire Pragma by value to the Exchange, and closes the WorkSpace. Egress uses the carried Pragma independently of the closed WorkSpace. Output Unload/Validate separately establishes discrete IUs. Categories 1 and 2 remain immutable and Category 3 retains the distinction between output IUs and Ergon-owned processing status. Reconcile the earlier changed-working-value return in the Pragma Topic and Ergon–Mneme candidate before any companion revision or canonical incorporation. The accepted cache/recovery allocation and governed writer contracts continue to apply. Loader spelling, ProcessingEngine scope, output-establishment contracts and qualified Pathway terminology retain their open detail. [WorkSpace-to-Egress transfer](#workspace-to-egress-transfer) · [Pragma Topic][pragma-topic] · [Earlier Ergon–Mneme completion and return][unload] · [Accepted cache and recovery][cache] · [Terminology matrix][terminology] |
| Ergon status and audit | **Resolved within this DRP.** Ergon owns `Workspace.Processor.Status`. The complete status ends its lifecycle in the authoritative AuditEvent for closure of the applicable Pragma's processing instance. Kleio constructs and preserves that AuditEvent. Representation, capture interaction and timing remain open within existing audit and Assurance obligations. [Status lifecycle](#workspaceprocessorstatus-lifecycle) · [Kleio][kleio] |
| Praxis processing and Pragma metadata status | **Resolved within this DRP.** Ponos owns `Pathway.Processing.Status`, including functional concurrency semantics, and the logical Praxis.Server and embedded Praxis.Agent roles. Energeia retains Praxis definition management. Exact Server/Agent interfaces and division of responsibilities, and Ergon/Ponos allocation of individual `Pragma.Metadata.Status` transitions, remain open. Mneme manages persistence and availability for all three status domains. [Status domains](#45-status-domains-and-processing-instance-closure) · [Ponos Praxis processing](#46-ponos-praxis-processing-coordination) · [Praxis][praxis] |
| Egress and publication | **Resolved within this DRP.** Egress Filter performs dead-task blocking, then PublishingMask evaluation after output establishment and Outcome Decision. The outbound PEP independently authorises release. Pragma Create invokes Mneme's PragmaFactory internally and passes only successfully established originating Pragma identities, distinct from the Egress Pragma, under the applicable Petasos handover contract. Status consequences follow the governing contracts. Qualified Publishing Pattern correspondence remains open. Ponos Praxis selection remains separate. [Draft.000.003 adjudications][adjudication-task] · [Pragma Management][publish-base] · [G03 publication review][publication-open] · [Routing Draft.000.003][routing] |
| Work, transport and execution | The earlier qualified model gives Mneme semantic selection, parcels originating work with receiver-side undertaking creation and describes Ponos/Ergo continuation routing. G03 gives Petasos matching and routing fulfilment, passes the established Pragmatosis identity for subscription work and uses Ponos evaluation for direct Praxis routing of selected existing Pragmas. Outcome Decision determines the attributable Ergon-instance result. Undertaking and parent-work dispositions remain separate. The direct Praxis parcel/receiving role and correspondence of the per-execution TaskOutcome to the existing ActionableTask-resolution Topic remain open. This DRP applies the candidate context without revising the routing DRP. [PetasosParcel][parcel] · [TaskOutcome Topic][task-outcome] · [G03 Pragma Model][pragma] · [Routing open decisions][routing-open] |
| Security and evidence | Preserve the Ergon-hosted ingress and egress PEPs and Petasos FORWARD enforcement. G01 retains the calling ID rule and Themis authority resolution. Reconcile trusted binding and qualified pass-off terminology without widening authority. Select the minimum independently attributable evidence by handover and owning contract. The pending audit-staging proposal does not alter the Kleio capture gate. [G01 qualifications][g01-open] · [Kleio][kleio] · [Terminology discrepancies][terminology] |
| PF-07 and PF-08 | **PF-07 remains partially resolved.** H01–H11 need minimum evidence and owning acknowledgements where applicable. H10 records originating Pragma establishment. H11 records the fact proved at Petasos Route-In. Receipt, authoritative work, durable routing and accepted queue effects remain distinct. Egress completeness evidence and recovery remain open. **PF-08 — Architectural strategy resolved; detailed transport, concurrency and recovery contracts outstanding.** Reuse the qualified routing strategy. A Checkpoint neither replaces durable routing records nor supplies a retry, reinjection, fencing or execution-admission proof. All remaining G03 qualifications retain their standing. [G03 exception register][pf] · [PF-08 disposition][routing-pf08] · [DR-12][hcrl] |

## 7. Decision and verification register

| Status | Matter |
| --- | --- |
| **Accepted framework direction** | Three distinct tiers. Camel-based intra-module sequencing and reusable scaffolding with a synchronous Checkpoint at each named functional handover. Independent configuration, Mneme-managed housekeeping and independently attributable Assurance evidence. Candidate reuse by Ponos/common Ergon and DigitalTwin-Behaviour with purpose-specific Pylai adaptations. The issued ten-function route and H01–H11 remain the reference. |
| **Resolved by the Draft.000.003 adjudications** | Three WorkSpace categories, immutability of context-loaded and explicitly retrieved IUs, Ergon ownership of `Workspace.Processor.Status`, Context Unloader's application of Ergon-defined branch rules and reference preparation, and processing-status final disposition in a Kleio AuditEvent. Ponos ownership of `Pathway.Processing.Status` including functional concurrency, the Praxis.Server / embedded Praxis.Agent logical model and dead-task blocking within the Egress Filter. [Draft.000.003 adjudications][adjudication-task] |
| **Resolved by the Draft.000.004 adjudications** | Context Unloader releases the entire Pragma by value at H05. The Exchange supplies the Egress context independently of the closed WorkSpace. The Egress Pragma carries its original identity and each newly created originating Pragma has a distinct work identity. The complete Ergon processing status ends its lifecycle in the authoritative AuditEvent for processing-instance closure. [Draft.000.004 adjudications][final-review-task] |
| **Architecture and interaction contracts outstanding** | Final stage names and detailed output IU and originating-work establishment contracts. Reconcile the earlier working-value return, canonical TaskOutcome correspondence and qualified publication/selection boundaries. Resolve allocation of individual `Pragma.Metadata.Status` transitions, exact Praxis.Server / Praxis.Agent interfaces and responsibilities, and processing-status audit representation, capture interaction and timing. Retain Checkpoint identity/lifecycle, durable recovery boundaries, minimum evidence, granularity semantics, trusted security-context propagation and independent Assurance integration. PF-07/PF-08, H11 acceptance, acknowledgement and reconciliation, and direct Praxis work-realisation correspondence retain their qualifications. Pylai adaptation and OA&M elaboration remain separate future work. |
| **Detailed implementation** | Camel endpoint/adapter choice, CDI instance ownership and lifecycle, processor sharing/pooling and concurrency configuration, interfaces and classes, checkpoint implementation and measured performance tuning. Verify correct isolation and effects before optimising. These choices stay within the agreed architectural contracts. |
| **Later conformance evidence** | Verify stage and recovery ownership, valid synchronous outcomes under success and failure, no internal asynchronous step, preserved Ack/indeterminate distinctions, sufficient evidence under every configuration, and fail-closed security and mandatory Kleio capture. Exercise isolation under concurrency and retry, durable recovery evidence versus transient observation, and independent Assurance. Qualify each integration against its actual protocol and transport. |

**Review disposition** · Mark Hunter, Solution Architect, accepted Draft.000.004 with the qualifications and outstanding obligations carried forward in §§6–7. Final.001 records the accepted offline design. Implementation and Architecture Continuum incorporation require separate authority. DR-12, HCRL.004 and the collective G03 review remain pending, and other G03 DRPs retain their existing standing. [Solution Architect acceptance][acceptance-task] · [Bounded DR-12 disposition][acceptance-review]

[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-processing-framework-and-checkpoints-task.md
[revision-task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/20261123T020707Z-g03-processing-framework-and-checkpoints-draft-000-002-task.md
[adjudication-task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/20261123T024204Z-g03-processing-framework-and-checkpoints-draft-000-003-task.md
[final-review-task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/20261123T025640Z-g03-processing-framework-and-checkpoints-draft-000-004-task.md
[acceptance-task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/20261123T030653Z-g03-processing-framework-and-checkpoints-final-001-task.md
[accepted-draft]: DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.004.md
[accepted-draft-report]: ../evidence/software-0.1.0/005/after/ai-agent-reports/20261123T025859Z-g03-processing-framework-and-checkpoints-draft-000-004-report.md
[acceptance-review]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#g03-processing-framework-final-001
[previous-draft]: DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.003.md
[previous-report]: ../evidence/software-0.1.0/005/after/ai-agent-reports/20261123T024438Z-g03-processing-framework-and-checkpoints-draft-000-003-report.md
[terminology]: ../evidence/software-0.1.0/005/after/ai-agent-reports/20261122T111009Z-g03-ergon-route-checkpoint-reconciliation-report.md
[hcrl]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[release]: ../../../architecture-release.json
[agents]: ../../../AGENTS.md
[axioms]: ../../governance/architectural-axioms.md
[persistence]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[cache]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[assurance]: ../register/1.1.20/hcrl-1.1.20-001.complete.md#dr-19
[g01-decision]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#11-review-decision
[g01]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#4-architectural-definition
[g01-open]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#9-outstanding-qualifications
[endpoint]: ../../continuum/application/application-interface/800789f54ba24f53a354600d5cd43f6f.md#architectural-content
[pathway-model]: ../../continuum/application/application-component/fb3f37a6ac614415ac486a6de9d35994.md#designnote-pathways-journeys-and-assurance-representation--original-lines-124-124
[ergon]: ../../continuum/application/application-component/49c9eb2bef0c47b8b5328621d5fc5479.md#architectural-content
[praxis]: ../../continuum/application/data-object/0f961462340348b290fd844357d54d67.md#architectural-content
[petasos]: ../../continuum/application/application-component/195e5f10db8e419391354197b5d7a8cc.md#architectural-content
[parcel]: ../../continuum/application/data-object/f0a88f05a0cb438cb42572ec07fb8513.md#architectural-content
[artemis]: ../../continuum/motivation/constraint/mandate/e024703b32c5437481ce290e8e50b079.md#approved-definition
[operational]: ../../continuum/motivation/principle/architectural-axiom/d7e3f0388d0043259585d1e4c5c948d6.md#architectural-content
[iris]: ../../continuum/application/application-component/e927c1af96c64fcd979be659bee2cbe3.md#architectural-content
[twin]: ../../continuum/application/application-component/3f64d339648246e99e8866c8089f8960.md#architectural-content
[pylai]: ../../continuum/application/application-component/e3e4147fea3640198dc251ed41ee854d.md#architectural-content
[mneme]: ../../continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md#architectural-content
[iu-framework]: ../register/1.1.22/hcrl-1.1.22-001.complete.md#dr-1
[kleio-history]: ../../continuum/application/application-component/fefda110e2274884ad58906733606efd.md#audit-history-and-evidence
[kleio]: ../../continuum/application/application-component/fefda110e2274884ad58906733606efd.md#auditability-and-availability
[journey]: ../../continuum/application/data-object/359b901df27f4b43b5a2ff14a8484e34.md#architectural-content
[dokimasia]: ../../continuum/application/application-component/d739c75dffe2453081735710b285f03b.md#architectural-content
[finite]: ../../continuum/motivation/constraint/3ba73277abf146728ca8c1f5846227d7.md#architectural-content
[self-audit]: ../../continuum/motivation/constraint/e1ce978457e543d5894de24e9be9ac2c.md#architectural-content
[isolation]: ../../continuum/strategy/course-of-action/implementation-guideline/4332542fe3c54af9ba07220d255de9c2.md#architectural-content
[indeterminate]: ../../continuum/motivation/requirement/fc732b64fc564127b73174e28d9ccce8.md#architectural-content
[checkpoint-boundary]: ../../continuum/strategy/capability/feature/bb57137dc8844c96ba2f0c3ccc88a2f9.md#architectural-content
[checkpoint-durable]: ../../continuum/strategy/capability/feature/4f7a02255cb9400ab944c2f3827463af.md#architectural-content
[checkpoint-integrity]: ../../continuum/strategy/capability/feature/5cce4e7eabbb4b63919ae43d591f1279.md#architectural-content
[checkpoint-completeness]: ../../continuum/strategy/capability/feature/06c40d4b74764978a2b0e16a3e5baa2f.md#architectural-content
[task-outcome]: ../../continuum/application/data-object/topic/08d8ee19a30f418f9d5bc497ced0d1ef.md#architectural-content
[publish-base]: ../../continuum/application/application-function/ab0cdaa7ed4149ee856216efa5a265c4.md#source-qualified-contribution
[context]: DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md#4-architectural-definition
[unload]: DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md#46-completion-and-return
[pragma-topic]: ../../continuum/application/data-object/27ad143e7c784e378556af8a57c438f7.md#architectural-content
[pragma-work]: DRP-Mneme-Pragma-Model.draft.000.004.md#41-work-identity-and-realisation
[pragma]: DRP-Mneme-Pragma-Model.draft.000.004.md#48-execution-outcomes-and-work-disposition
[publication]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md#41-output-and-publication
[publication-open]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md#6-reconciliation-and-open-decisions
[routing]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md
[routing-egress]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#21-ergon-egress-creates-and-hands-over-originating-pragmas
[factory]: ../../continuum/application/application-component/75e77d8dfd1d4870aa6151485d28d1dd.md#architectural-content
[praxis-router]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#4-petasospraxis-router
[routing-recovery]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#51-ergon-egress-handover-recovery-from-authoritative-provenance
[routing-open]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#7-architecture-correspondence-and-open-decisions
[routing-pf08]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#71-pf-08-qualified-disposition
[audit-draft]: DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md#3-candidate-staging-and-transaction-boundary
[pf]: ../evidence/software-0.1.0/005/after/G03-Pragma-Family-Adjudications-005.md#pf-01pf-12-disposition
[camel-direct]: https://github.com/apache/camel/blob/camel-4.4.2/components/camel-direct/src/main/docs/direct-component.adoc
[camel-seda]: https://github.com/apache/camel/blob/camel-4.4.2/components/camel-seda/src/main/docs/seda-component.adoc
[camel-processor]: https://github.com/apache/camel/blob/camel-4.4.2/core/camel-api/src/main/java/org/apache/camel/Processor.java
[cdi]: https://jakarta.ee/specifications/cdi/4.0/jakarta-cdi-spec-4.0.html#dependent-context
