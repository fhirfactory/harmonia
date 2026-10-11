<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Internal Information Systems Middleware

**Standing: Harmonia Candidate Information Systems Architecture.**
[Scope](README.md) applies. Internal middleware responsibilities are reconstructed
from documented collaboration intent, principally [L13](sources.md#l13),
[L29](sources.md#l29), [L01](sources.md#l01), [L14](sources.md#l14) and
[L19](sources.md#l19). External integration contracts and runtime products remain
separate architectural concerns.

## Middleware responsibilities

| ID | Internal responsibility / candidate allocation | Information and collaboration contract | Current standing / limits |
| :--- | :--- | :--- | :--- |
| M01 | **Petasos opaque message collaboration and durable work handoff** | Producers/consumers use destination and envelope contracts. Independent activities transfer responsibility at explicitly durable, recoverable transition points; correlation/causation links connect activity attempts. | A01 AX-10 and L13 ADR-014; L29. Work acceptance is distinct from Mnemosyne state commit, processing completion, external acknowledgement and assurance. Detailed transition/recovery contracts incomplete, CDG-16/19/34. |
| M02 | **Recoverable backlog, redelivery, rejection, dead-letter and designated replay** | Durable backlog absorbs pressure; consumer acknowledgement/rejection and dead-letter observations support recovery. Deliberate replay starts a new linked attempt at a designated transition, preserving the original. | L13 ADR-015/017, L02/L29. Exact queue sizes/retry limits are implementation detail. Replay is not transport redelivery, arbitrary cache restart or redelivery of an established result, CDG-17. |
| M03 | **Ponos work ingress / dispatcher and sequence resolution** | Converts/receives an opaque transport envelope into execution context; identifies the applicable workflow using metadata/subscriptions; Themis governs requester and executor; control returns an execution/continuation observation. | L01/L02/L19/L23. Conduit/Camel route names describe mechanisms, not new generic application components. Selection, acknowledgement and unknown effects need a complete responsibility contract, CDG-01/03/16. |
| M04 | **Praxis composition and activity boundary support** | Definitions supply order/dependencies/conditions and topic matching; defined Ergon logic executes within context and yields information/observations; checkpoint support records progression. | L04/L19, constrained by A05. No universal DAG or per-step permanent audit boundary established. Synchronous direct routes/asynchronous queues are mechanisms, CDG-01/05/14. |
| M05 | **Mneme-facing governed access, observation and change coordination** | Read/change services combine governed access, active coordination, authoritative conditional commit and guarded post-commit convergence. Context and semantically distinct outcomes travel with the operation. | L14; A01 AX-05/07/14/15 and A02. Facade is an application access contract, not another persistence owner or mandatory network boundary. Tokens/versions/errors require distinctions, CDG-11/16/31. |
| M06 | **Distributed definition/reference availability** | Calliope reference definitions are consumed locally; Praxis loaders/seeders distribute supported sequence definitions through Mneme working state. | A02, L06/L04/L19. Logical availability is documented; authoritative definition storage/release/reload contracts remain unknown, CDG-06/24. |
| M07 | **Security/context and meaningful evidence participation** | Trusted context reaches enforcing boundaries; Themis evaluates; significant evidence is produced for Kleio according to policy, distinct from transient metrics and checkpoints. | A01 AX-07–09, L08/L13/L14. No universal second security-context wrapper, persistence of all transport metadata or permanent audit of all machinery, CDG-13–15/30. |
| M08 | **Agora collaboration handoff** | Collaboration requests/events move through Petasos; Agora identity/lifecycle/membership/transaction handling governs collaboration protocol projection. | AGENTS Invariant 10; L12. No direct Agora-to-Ponos dependency or raw Matrix type leakage; summary sources/partial transaction effects unresolved, CDG-19/21–23. |
| M09 | **Operator/presentation observation mediation** | Component telemetry/health providers and Iris aggregation expose status, backlog, interfaces, work/progression and event correlation under policy. | L21/L23. Observation has no automatic durable authority or assurance standing; origin, retention, access and operational control contracts incomplete, CDG-26/28. |

## Documented collaboration examples

These examples summarise source-described collaborations; they are not approved
end-to-end algorithms or new external contracts.

1. **Inbound clinical message to operational processing:** a Pylai interaction
   produces managed communication/work context, evaluates Themis and requests a
   Petasos handoff. Ponos receives, resolves a sequence and executes defined
   activities, which use governed information services. The source diagrams
   combine cache/state/queue acceptance in different ways; their precise durable
   responsibility-transfer contract remains CDG-16/34. REC-001 remains a repository
   invariant: downstream publish/cache failure must not yield an AA response.
2. **Destination fan-out:** a distribution activity generates separate destination
   requests and checkpoints; Petasos handoff isolates pending destination work;
   Pylai observes external response/delivery outcomes. Task/Pragma status and Iris
   views retain destination granularity (REC-002). Enqueue, send, acknowledgement,
   state commit and parent-work outcome remain distinct (CDG-18).
3. **Registry change:** Pylai captures a change request; Petasos transfers it to
   Ponos/Praxis/per-resource Erga; validation/change uses governed access and
   Mnemosyne durable establishment, with progression exposed for presentation.
   Historical direct-store paths and external async/status semantics remain
   unresolved rather than becoming the authoritative Domain05 path (CDG-11/32).
4. **Collaboration transaction:** Agora receives and authorizes collaboration
   events, records transaction progress and hands events to Petasos. L12's
   publication followed by a processed marker does not specify recovery from
   partial publish or an unknown effect; no exactly-once business-effect contract
   is reconstructed from the diagram (CDG-19).

## Responsibility boundaries

| Concern | Domain05 meaning | Separate downstream concern |
| :--- | :--- | :--- |
| Internal work handoff | Transfer/acceptance/recovery responsibility, semantic opacity, context and observations | Broker product/API, queue bindings, wire protocols, journal flush/failover implementation. |
| Internal activity collaboration | Execution/context/composition and continuing responsibility | Camel, direct/SEDA/JMS routes, threads, process/runtime topology. |
| Managed information collaboration | Application-facing access, active coordination, durable establishment and truthful outcomes | Hot Rod, CAS/locking, HTTP/internal persistence transport, SQL/JPA and cache distribution. |
| External interaction | Execution requests an interaction; Pylai governs representation/conformance and non-destructive publication | Domain06 external/cross-boundary interfaces, standards interaction details and Domain07 connectivity/runtime realisation. |
| Matrix collaboration | Agora governs collaboration context and protocol encapsulation, using internal handoff | Matrix Client-Server/Admin/Application Service transports and homeserver deployment. |

The middleware product inventory in L17 is a technology/support catalogue. It
does not mean that all internal middleware belongs in Domain06 or that each
runtime library/product becomes an Information Systems component. Pylai's
standards-facing boundary is preserved without transferring all generic transport
machinery to it. Database/access and evidence/assurance seams remain separate;
technology availability cannot silently weaken those responsibilities.
