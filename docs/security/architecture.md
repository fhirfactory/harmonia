# Harmonia Security Architecture: Defence-in-Depth & Themis

## 1. Executive Summary & Philosophy

In complex distributed healthcare integration architectures, single-perimeter security at the edge API gateway is insufficient to mitigate systemic risk. Internal misconfigurations, rogue microservices, compromised edge nodes, or lateral movement attacks can lead to catastrophic data leaks or unauthorized clinical data modification.

Harmonia implements a comprehensive **defence-in-depth security architecture** governed by **Themis**, the centralized Policy and Authorisation Service.

### Foundational Security Principle
```
DEFAULT DENY
```
In the absence of an explicit policy rule granting permission, every HTTP request, asynchronous message, background processing task, and internal persistence operation is denied. Internal components and processes are treated as untrusted actors requiring explicit cryptographic or context-backed identities and authorities.

---

## 2. Defence-in-Depth Boundary Topology

Every major architectural tier independently evaluates policy before taking consequential action:

```mermaid
graph TD
    subgraph ClientTier [External Consumers]
        Client[HTTP Client / External System]
    end

    subgraph PylaiTier [Pylai Ingress Gateway]
        GatewayController[FhirRestGatewayController]
        SecurityInterceptor[FhirSecurityInterceptor]
        SubmissionService[ChangeRequestSubmissionService]
    end

    subgraph ThemisSubsystem [Themis Policy & Authorisation Service]
        ThemisAPI[Themis API & Models]
        PolicyEvaluator[Deterministic Policy Evaluator]
        ThemisAudit[Security Decision Audit Service]
    end

    subgraph TransportTier [Petasos Transport Subsystem]
        PetasosProducer[Petasos Producer]
        ArtemisQueue[(ActiveMQ Artemis)]
        PetasosConsumer[Petasos Consumer]
    end

    subgraph EnergeiaTier [Energeia / Ponos Workflow Engine]
        WorkflowDispatcher[PragmaWorkflowDispatcher]
        ErgonExecutor[PractitionerChangeErgon / Erga]
    end

    subgraph HestiaTier [Hestia / Mnemosyne Persistence]
        StorageService[FhirStorageService]
        PostgresDB[(PostgreSQL / H2 Store)]
    end

    Client -->|1. REST Request| GatewayController
    GatewayController -->|2. Authorize Request| SecurityInterceptor
    SecurityInterceptor -->|3. Evaluate Ingress Policy [SUBMIT_UPDATE]| ThemisAPI
    ThemisAPI --> PolicyEvaluator
    PolicyEvaluator -.-> ThemisAudit
    SecurityInterceptor -->|4. ALLOW| SubmissionService
    SubmissionService -->|5. Build Pragma with Immutable Originating Context| PetasosProducer
    PetasosProducer -->|6. Publish Envelope| ArtemisQueue
    ArtemisQueue -->|7. Receive| PetasosConsumer
    PetasosConsumer -->|8. Dispatch Task| WorkflowDispatcher
    WorkflowDispatcher -->|9. Authorize Execution [PROCESS]| ThemisAPI
    WorkflowDispatcher -->|10. ALLOW| ErgonExecutor
    ErgonExecutor -->|11. Persist Change| StorageService
    StorageService -->|12. Authorize Persistence [UPDATE]| ThemisAPI
    StorageService -->|13. ALLOW| PostgresDB
```

---

## 3. Boundary Enforcement Points

| Tier | Component | Evaluated Action | Required Authority | Target Security Domain |
| :--- | :--- | :--- | :--- | :--- |
| **Pylai Ingress** | `FhirSecurityInterceptor` | `READ` / `SEARCH`<br>`SUBMIT_CREATE`<br>`SUBMIT_UPDATE` | `provider.read`<br>`provider.search`<br>`provider.change.submit` | `PROVIDER_REGISTRY` |
| **Pragma Context** | `ChangeRequestSubmissionService` | Context Attachment | Originating Principal + Authorities | Immutable |
| **Petasos Transport** | `PetasosMessage` | Queue Propagation | Envelope Integrity | `INTERNAL` |
| **Ponos Dispatch** | `PragmaWorkflowDispatcher` | `PROCESS` / `EXECUTE` | `provider.change.process` | `PROVIDER_REGISTRY` |
| **Erga Execution** | `AbstractProviderRegistryChangeErgon` | `CREATE` / `UPDATE` | `provider.resource.create`<br>`provider.resource.update` | `PROVIDER_REGISTRY` |
| **Mnemosyne Store** | `FhirStorageService` | `CREATE` / `UPDATE`<br>`DELETE` / `READ` | `provider.resource.create`<br>`provider.resource.update`<br>`provider.resource.delete` | `PROVIDER_REGISTRY` |

---

## 4. Invariant: No Privilege Amplification

Asynchronous processing across Petasos and Ponos **never amplifies requester privileges**.
1. Originating requester claims (`PRV_SUB`) are preserved immutably inside `Pragma`.
2. Ponos verifies that the executing `Ergon` possesses `provider.change.process`.
3. The persistence gate independently requires `provider.resource.update` before modifying persistent storage.
4. Compromising or bypassing any single checkpoint halts execution without modifying the database.
