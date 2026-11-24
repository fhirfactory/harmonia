# Harmonia — Design Review Package (DRP)
## Agora, Omilia, Digital Twins and Agentic Participant Interaction

**Document class:** Design Review Package — discussion capture and architectural options  
**Standing:** Working DRP for Solution Architect review; not an accepted Architecture Continuum change  
**Context:** Harmonia Capability Model Consolidation 1.1.20, FR-003 follow-on architecture discussion  
**Date:** 10 November 2026  
**Scope:** Collaboration components, Digital Twin participant interfaces, agentic integration, Room/Space semantics and interaction-to-work boundaries

---

## 1. Purpose

Capture and preserve the architectural thinking developed while considering Matrix/Element as the human-facing and agent-facing interaction environment for Harmonia Digital Twins. In particular, retain the existing **Agora/Omilia** architecture rather than reinventing it; articulate the emerging **Digital Twin–represented participant** target state; and identify design options and unresolved questions for future adjudication.

This is a **DRP**, not an instruction to Codex, an implementation specification, a new requirement set or an approval to alter the governed Architecture Continuum. It records established candidate-design positions distinctly from recent Solution Architect direction, design implications and suggestions still requiring assessment.

## 2. Sources and authority

### 2.1 Existing design source recovered

*DesignNote — Agora Controlled Collaboration and Omilia Realisation* (working Domain05 Information Systems design record). It records the **Agora controls; Omilia operates** principle, Omilia software lineage, managed collaboration control, capture versus authoritative processing, and outstanding design matters. The DesignNote itself explicitly states that it is **working architecture, not independently RAD authority**. Its historic `/docs/markdown` location reference should not be assumed to supersede the current governed Continuum at `enterprise-architecture/continuum/`.

### 2.2 Current discussion direction

The Solution Architect clarified that:

1. A Digital Twin's target state includes representing a **human or an Agentic AI** within Harmonia, and providing the boundary by which Harmonia sends work/activities to and captures activities/responses from that represented participant. This adds to, rather than displaces, existing non-person Twins (e.g. bed or patient contexts).
2. Matrix is **definitely not a workflow engine**. Its role relative to governed work is, **at best, an interaction queue/cache**, with collaboration messaging/history as its own operational responsibility.
3. **Room/Space design remains open**, and their names may acquire Harmonia-specific meanings. There is no final taxonomy or fixed one-Room-per-Twin or one-Room-per-Pragma rule.
4. Harmonia intends to **clone and materially modify** both the Matrix/Synapse server and the Element client **without modifying the Matrix protocols**.
5. The named Harmonia collaboration components already exist: **Agora**, **Omilia.Server** and **Omilia.Client**.
6. Future **agentic behaviours** must be considered in capability and solution design. Human, conventional programmatic, Digital Twin and agentic interactions may coexist, without treating agentic autonomy as authority.

### 2.3 Authority boundary

The governed Markdown Architecture Continuum, accepted architecture axioms and subsequent adjudications take precedence over this DRP. Any apparent discrepancy should be reported for reconciliation; this DRP does not silently supersede accepted authority.

## 3. Existing candidate architecture to preserve

### 3.1 Components and identities

| Component | Established candidate responsibility | Intended technology lineage |
|---|---|---|
| **Harmonia.Agora** | Harmonia collaboration semantics and control; managed Room/Space lifecycle and membership intent; publication intent; observation and defined interaction capture | Harmonia application component |
| **Harmonia.Omilia.Server** | Executes collaboration-platform mechanics for Rooms/Spaces, users/membership, dialogue, events and applicable media; exposes operations/events through an **Application Services API** to Agora | Cloned and modified from **Matrix Synapse** |
| **Harmonia.Omilia.Client** | Human-facing collaboration experience; detailed integration and UX responsibilities to be designed | Cloned and modified from **Element** |

**Invariant: Agora controls; Omilia operates.** The Harmonia architectural identity of the latter components is **Omilia**, irrespective of the upstream codebase. Software provenance, licence obligations and upgrade strategy remain distinct engineering concerns.

The **Matrix protocol is retained**. Nontrivial modifications may occur in the server and client codebases and private application services, but Matrix protocol compatibility is an explicit design constraint. No protocol extension or fork-specific wire change is authorised by this DRP.

### 3.2 Existing collaboration boundaries

- Agora provides **native general-purpose collaboration**, not just a conduit for Harmonia clinical and operational notifications.
- Agora controls *intent* for Rooms/Spaces it manages; Omilia.Server performs the actual platform operations. The exact mechanism/contract of the Application Services API remains to be designed.
- Omilia.Server performs the operational mechanics of the collaboration system, including general and managed Rooms/Spaces.
- Agora may convey information owned by other Harmonia responsibilities; **publication does not transfer ownership** of the underlying clinical, operational or other authoritative information.
- Agora owns/manages its **native collaboration record and state** as an application responsibility, while Omilia.Server supplies the underlying platform mechanics; detailed record custody and retention design remains open.
- Agora observes dialogue/activity in all **Agora-managed Rooms**. It captures **defined meaningful interactions**, rather than treating all Room messages or utterances as work.
- A captured interaction may result in a **Communication Information Unit and/or Pragma**, processed by the existing Harmonia framework. This creates governed information/activity; it does **not** transfer ownership of the originating collaboration record.
- **Captured dialogue, operational logging and audit-worthy occurrences are not the same thing.** Kleio remains the authoritative AuditEvent responsibility; the applicable audit policy controls what is audited.
- Roster Management is a **Separately Provided Capability**. The earlier candidate design identifies **Shift Start/Shift End** as applicable R1.x/R2.x inputs for Agora membership context, not an instruction to build a roster engine in Agora.

### 3.3 Existing deliberate deferrals

The source DesignNote already leaves unsettled detailed Room/Space taxonomy, membership derivation, lifecycle rules, API contract, Omilia fork changes, client/Iris integration, collaboration custody and media architecture, retention, captured interaction representations, security/privacy mechanics, federated interactions, notification/escalation mechanics and free-text recognition. These remain **unresolved**, unless independently settled by later governed architecture.

## 4. Emerging Digital Twin and agentic target state

### 4.1 The represented participant

A Digital Twin is Harmonia's governed contextual representation of an entity. In the emerging target state, **the entity may be an active human or an Agentic AI participant**. The represented participant is distinct from its Twin; neither is reduced to a Matrix account.

For an active represented participant, the Twin can support a **bidirectional activity relationship**:

- Harmonia directs governed work, requests, context and activities *toward* the represented participant through appropriate interfaces.
- The represented participant returns dialogue, confirmations, artefacts, progress, observations and outcomes that can be considered for **governed capture** into Harmonia.
- The Twin maintains relevant context and activity continuity while the represented participant, channel, agent execution instance or communication availability changes.
- Human and agentic participants may collaborate with each other and their Twins; representation does not imply identical authority or interaction semantics.

Existing Twin examples concerning physical or operational entities should remain supported; the proposal must **not** redefine every Digital Twin as a human/AI proxy. Agentic AI is not inherently a Digital Twin, and a Digital Twin need not itself execute agentic inference.

### 4.2 Symbiosis, not identity merger

A Twin might present context or governed actions to an agent; an agent might provide planning, interpretation, requests, work products or recommendations back to the Twin. A human might perform the ultimate authorised decision, or an agent might perform a delegated task when permitted. These are possible interaction patterns, not authorised agentic clinical decision-making.

**Representation ≠ delegation ≠ authority ≠ execution success ≠ authoritative commit.**

The Twin, agent, agent operator/delegator and relevant human participant may require distinguishable identities and attributable evidence. Delegation, autonomy and model-generated instructions must not bypass Themis or other established policy enforcement points.

### 4.3 Agentic integration protocols

**MCP**, **A2A** and **AG-UI** were identified as possible complementary integration mechanisms, not selected technologies or architectural contracts:

- MCP: candidate governed tool and context access for an agent.
- A2A: candidate agent-to-agent collaboration/task interaction when justified.
- AG-UI: candidate agent-to-human application interaction where relevant.

Their presence does not change Agora/Omilia or authorise integration decisions. They must be evaluated against existing Harmonia interfaces, FOSS/licensing requirements, operational control and non-functional requirements.

## 5. Omilia as communications environment — explicit execution boundary

**Non-negotiable direction: Matrix/Omilia is NOT a workflow engine.**

Omilia may deliver, buffer and expose messages, commands, events and media, and maintain native collaboration history. For the Harmonia work lifecycle, Matrix is at most an **interaction queue/cache**. Omilia's collaboration persistence does not substitute for Harmonia's durable acceptance of work or information.

| Concern | Responsibility/boundary |
|---|---|
| Conversations, messages, reactions, threads, media, operational Room/Space mechanics | **Omilia.Server** and **Omilia.Client** as appropriate |
| Collaboration semantics; managed Room/Space and membership **intent**; publication and capture rules | **Agora** |
| Mapping captured input into a Twin stimulus or other governed input | Applicable **Digital Twin / Behaviour / integration** responsibility, according to later design |
| Tasks, processing acceptance, scheduling, asynchronous activity and execution outcomes | Existing **Pragma / Ponos / Ergon** and related Harmonia work framework; **not** Omilia |
| Authority for disclosed data, commands, participant actions | **Themis** and applicable governance/policy boundaries |
| Authoritative information versions and durability | **Mneme / Mnemosyne** and appropriate information-management services |
| Authoritative audit events | **Kleio** |

**Distinct observable states** must not be conflated:

1. Interaction accepted/delivered/stored by the collaboration platform.
2. Interaction observed by Agora and, if defined, captured for Harmonia.
3. Governed work accepted durably by Harmonia.
4. Work processing commenced and ultimately completed, failed or continued asynchronously.
5. Resulting information, where applicable, committed as authoritative information.

A Matrix delivery/read receipt, reaction, chat message such as “done”, file upload or membership change **does not by itself establish** Task acceptance, acknowledgement of clinical responsibility, authorised action, execution success or Information Unit commit.

Matrix and the existing Harmonia work broker have different lifecycle responsibilities. Omilia's collaboration replay/history may help with resynchronisation, but must not replace Harmonia's accepted-work durability, correlation, idempotency or recovery assurances.

## 6. Interaction primitives and their potential uses

| Omilia/Matrix primitive | Potential Harmonia use | Boundary/qualification |
|---|---|---|
| Dialogue messages | Human–Twin, agent–Twin and multi-party discussion | Speech is not automatically a command or Pragma |
| Structured events | Explicit actions, status submissions, correlation tokens | Contract to be specified; not all arbitrary events are authoritative |
| Commands / bot-directed text | Request a supported operation | Client slash commands may be local UX; explicit semantics and validation required |
| Files/media | Inputs, work products, communication media | Inbound files are not automatically governed IUs; apply classification, custody and policy |
| Replies and threads | Discuss a task or activity with local context | Thread ID is not Pragma ID |
| Rooms and Spaces | Collaboration contexts and presentation/navigation | Taxonomy and lifecycle unadjudicated |
| Reactions and receipts | Lightweight interaction feedback | Do not infer authoritative acknowledgement or decision |
| Membership and presence | Collaboration participation and engagement signals | Membership/presence is not Themis authority or reliable operational capacity |
| Widgets/specialised UI | Human review, confirmations, work/calendar views | Client support and Omilia.Client modification needed; not an execution engine |
| Agent/bot accounts | Programmatic peer participation | Agent/operator/delegation and Twin identity remain separately governed |

A participant may send unstructured dialogue and separately submit a machine-readable result. The latter becomes Harmonia work/information only at the established governed acceptance boundary.

## 7. Room and Space design: open problem, not prematurely settled

The Solution Architect explicitly expects Room/Space design to be **thrashed out** and is open to Harmonia-specific **overloading of names**. This is an intentional area for future design, not a gap to be filled by a generic Matrix convention.

### 7.1 Candidate dimensions for review

- **Purpose:** General collaboration, entity-associated interaction, work-related discussion, team operations, service coordination, research collaboration or administrative use.
- **Reference and cardinality:** Whether a Twin, represented participant, organisation, activity, context or task has zero, one or multiple Rooms; whether a Room spans many tasks/participants and whether a Task spans Rooms.
- **Space use:** Navigation/grouping of Rooms versus stronger governed organisation semantics; no assumed one-to-one mapping to capability hierarchy.
- **Names:** Human-friendly and possibly Harmonia-specialised, but should not be presumed unique, immutable or authoritative object identifiers.
- **Bindings:** Explicit governed binding from Room/Space identities to Harmonia representations or context when needed; associations may change over time.
- **Membership:** Agora membership intent derived from appropriate policy/context, Omilia execution; access to Room does not automatically confer access to all referenced Harmonia information.
- **Creation and retirement:** Dynamic vs pre-provisioned, ephemeral vs enduring, participant turnover, work completion and archival/retention policies.
- **Routing and capture:** Which Rooms are managed; which dialogue/actions are observed; which capture predicates are defined; how capture relates to Twin Stimulus and agent activity.
- **Agent participation:** Dedicated agent Rooms, shared human–agent Rooms, multiple agent instances, handoff/rotation and membership revocation.
- **Federation/privacy:** External participation, regulated information, encrypted Rooms, metadata minimisation and policy enforcement.

### 7.2 Avoid these unapproved equivalences

- Room = Digital Twin.
- Room = represented human or agent.
- Room = Pragma or Activity.
- Space = Business Capability or department.
- Room membership = clinical role or authorisation.
- Room title = authoritative identity, classification or routing key.

Any of these might become a useful **convention for a defined category**, but none is presently a global invariant. Name overloading and modified client UX should be judged for usability, namespace collision, identity stability, privacy and interoperability.

## 8. Forking and modifying Synapse/Element

The target is **Omilia.Server** and **Omilia.Client** as product-level forks/clones with **non-trivial code changes**, while retaining the Matrix protocols. This enables tailored UX, platform services and Harmonia-specific application integration; it does not require the collaboration system to own workflows.

### 8.1 Opportunities for later evaluation

- Specialised Omilia.Client views for Task/Calendar, Twin context, attachments, approvals and agent collaboration, potentially alongside ordinary chat.
- Agora-aware Room/Space organisation, naming and navigation.
- Strongly typed interaction affordances and agent-friendly client/event use, while preserving standards-compatible Matrix event exchange.
- Server-side administrative and collaboration-operation interfaces supporting Agora's intent/control decisions.
- Richer evidence and trace linking collaboration occurrences with captured Harmonia work.

### 8.2 Engineering boundaries and cautions

- Preserve Matrix wire-protocol interoperability and distinguish upstream protocol behaviour from Harmonia-only Application Services APIs, client extensions and private metadata.
- Evaluate actual upstream licences, notices, trademarks and contribution obligations for the exact versions forked; **FOSS compatibility must be established, not presumed**.
- Establish maintenance, upstream patch ingestion, security-response and interoperability testing strategies for substantial forks.
- End-to-end encrypted Room content is available only to authorised participating endpoints with relevant keys; server-side Agora capture cannot be presumed to read it. This is a design challenge, not a reason to silently disable encryption.
- Metadata (including Room names) can be security-sensitive; use minimum necessary disclosure and avoid protected clinical content in Room identifiers or diagnostic logs.
- Collaboration media custody, cryptographic key recovery, large-object handling and external federation must be addressed against Harmonia information governance.
- Client/server enhancements should not introduce a dependency by which ordinary matrix events bypass Harmonia policy, durable work acceptance or audit requirements.

This DRP does not prescribe particular code patches, libraries, server API endpoints or a branch/release choice.

## 9. Representative interaction scenarios for design examination

### 9.1 Human via Omilia.Client

1. Harmonia establishes an applicable governed activity.
2. Agora publishes or arranges a contextual communication through Omilia.Server into an appropriate managed collaboration context.
3. A practitioner reads details through Omilia.Client and replies or selects a defined action.
4. Omilia.Server records/exposes the collaboration event; Agora observes and assesses the defined capture rule.
5. Where applicable, a new Communication IU and/or Pragma is introduced at the appropriate Harmonia boundary.
6. The work framework determines actual acceptance, execution, continuation and any authoritative outcome.

### 9.2 Agentic AI via Omilia

1. A suitably identified agent participates through a programmatic Matrix-compatible client.
2. It receives permitted context/instructions, requests further context, returns a plan, artefact or explicit result.
3. Agora observes defined interaction activity without assuming agent-generated text is an authorised command or final work outcome.
4. The Digital Twin/Behaviour and work interfaces validate and contextualise required stimuli/results, enforcing delegated authority and any human approval boundary.
5. Governed results and evidence are accepted or rejected by Harmonia independently of Matrix delivery.

### 9.3 Mixed human/agent context

A human, agent and one or more Twins may collaborate in a shared managed Room. The agent may produce analysis, the human may undertake a reserved judgement and a Twin may carry activity context across a handoff. A room's conversation persists independently of the Task's authoritative state. This permits cooperative rather than substitutive agentic behaviour.

### 9.4 Non-Twin or lightweight inputs

Earlier XD-027 clarified that not every Stimulus requires substantive Twin-centric information transformation. A message, Boolean, name/value pair, attachment reference or typed event may already carry adequate context, or may require contextualisation before becoming governed work. The Matrix adapter should not force all inputs into a special Twin representation merely because the channel is Omilia.

## 10. Implications for current Capability Model adjudications

### 10.1 FR-003 — accepted boundary

`E07.02.02.F01 — Programmatic Action Confirmation` and `E07.02.02.F03 — Human Action Confirmation` have been accepted as **distinct**. The programme must preserve machine/agent confirmation separately from a human-facing request and explicit human response. The surrounding CT3 (`E07.02.02 — Review & Confirmation Interaction`) was agreed to support both interaction forms.

This DRP does not modify that disposition. Agentic involvement reinforces, rather than removes, the participant and authority distinction.

### 10.2 Potential cross-cutting principle (not yet independently approved)

A candidate **Agentic Behaviour Compatibility** principle would keep architectural capabilities neutral about who/what fulfils them except when the actor's nature is intrinsic to the responsibility. The stronger Digital Twin target should additionally permit **human–agent–Twin cooperation** through governed interfaces. This remains a design proposition, not a numbered and accepted AR-003.

### 10.3 Other Feature reviews

As FR-004–FR-009 are adjudicated, check for implicit human-only assumptions, machine-only assumptions, unjustified identity merging, and conflation of communication/confirmation with work acceptance. Do not pre-empt those adjudications, add new Features or rewrite unrelated capability descriptions solely because future agents are conceivable.

## 11. Candidate architectural positions and confidence

| Position | Standing |
|---|---|
| Agora controls collaboration; Omilia operates | Existing candidate design, recovered from working DesignNote |
| Omilia.Server fork of Synapse; Omilia.Client fork of Element | Existing candidate design and current Solution Architect reaffirmation |
| Retain Matrix protocols despite substantial implementation changes | Explicit current Solution Architect direction |
| Matrix/Omilia is not a workflow engine; at most interaction queue/cache for Harmonia work | Explicit current Solution Architect correction |
| Digital Twin represents and interacts bidirectionally with human or agentic participant | Explicit target-state direction, details to refine |
| General collaboration record is distinct from captured governed work and audit | Existing candidate design |
| Room/Space taxonomy and name overloading are open | Explicit current direction and historic deliberate deferral |
| Human/agent/Twin identity and delegated authority remain distinct | Architectural analysis consistent with Themis, subject to detailed design |
| MCP/A2A/AG-UI as candidate complements | Exploratory only; not selected |
| Specific Room-to-Twin, Room-to-Pragma or Space-to-capability cardinalities | Not decided |
| Omilia E2EE capture, agent message formats, exact fork modifications | Not decided |

## 12. Issues for later adjudication

1. **Digital Twin participant identity and addressability:** Separate Matrix identity for Twin and represented participant? One or multiple bindings? Agent instance/agent role/operator identity model?
2. **Room/Space taxonomy and naming:** Scope of name overloading; stable identity; lifecycle and cardinality; collaboration versus activity contexts.
3. **Agora–Twin/Behaviour integration:** How a captured dialogue/action is recognised, contextualised, admitted and correlated to work without allowing Agora to become an execution engine.
4. **Explicit interaction contracts:** Free-text commands, structured messages, confirmation requests, responses and machine-readable evidence; how to represent rejection/partial outcomes.
5. **Access/membership and Themis:** Membership policy, participant delegation, room privacy, source-information authorisation, revocation and agent-specific limits.
6. **Encryption/capture:** Endpoint participation for E2EE Rooms, key governance and protected metadata; avoid relying on server visibility of encrypted dialogue.
7. **Omilia forks:** License verification, supported protocol compatibility, extensions, product UX, upstream merge/test strategy.
8. **Collaboration information management:** Native record retention, media custody, provenance references, deletion and preservation; no automatic assignment of these concerns to authoritative clinical information stores.
9. **Agentic interoperability:** Whether and where MCP, A2A or AG-UI provide unique value alongside Omilia and Harmonia's established work/interface framework.
10. **Failure and recovery:** Interaction replay/deduplication, channel outages, unsynchronised participants, identity/device changes and long-running activity continuity.

## 13. Recommended next design activity

When the Solution Architect wishes to return to this subject, undertake a **bounded Room/Space and participant-identity modelling review**, starting from the existing Agora/Omilia DesignNote and current Continuum representation. Compare several concrete human, agentic, Twin and mixed collaboration scenarios before deciding Room/Space taxonomy, name semantics or the exact Agora–Behaviour interaction contract.

**No implementation, repository mutation, protocol change, release decision or new architecture authority is authorised by this DRP.**

---

## Appendix A — Compact responsibility summary

```text
Human / Agentic AI / Digital Twin endpoints
                  |
          Matrix protocol
                  |
     Omilia.Server / Omilia.Client
    (collaboration operations / UX)
                  |
     Application Services API
                  |
              Agora
  (collaboration control and capture)
                  |
   Defined interaction admission
                  |
      Twin / Stimulus / Behaviour
                  |
       Pragma / Ergon / Ponos
                  |
    Themis / Information / Kleio
```

The above is a **responsibility sketch**, not a mandatory call chain or deployment topology. In particular, Themis applies at each relevant enforcement boundary, not only at the bottom of a sequence; and no implication of automatic Pragma creation follows from merely observing a Matrix event.
