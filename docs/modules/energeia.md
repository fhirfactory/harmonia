# Energeia Subproject Reference: Workflow & Task Processing `[IMPLEMENTED]`

Energeia coordinates clinical integration activities and executes asynchronous workflow task sequences across the Harmonia platform. It comprises workers (**Ponos**), discrete activity units (**Erga**), workflow sequence blueprints (**Praxis**), and canonical task state envelopes (**Pragma**).

---

## 1. Subproject Architecture & Leaf Modules `[IMPLEMENTED]`

```
energeia/
├── erga/         # Discrete activity library (ErgonBase, AdtDistributionErgon, HL7 transformers)
├── praxis/       # TaskSequence definitions, blueprints, loaders, and default seeders
├── ponos/        # WildFly Jakarta EE 10 bootable WAR workflow execution engine
└── ponos-cli/    # Administrative CLI for Ponos queue inspection and live reload
```

### Subproject Maven Coordinates `[CONFIGURED]`
- **Parent GroupId**: `net.fhirfactory.harmonia`
- **ArtifactId**: `energeia`
- **Version**: `1.0.0-SNAPSHOT`
- **Packaging**: `pom`

---

## 2. Leaf Module Deep-Dives `[IMPLEMENTED]`

### 2.1 `erga` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:erga:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.erga.base`
  - `net.fhirfactory.harmonia.erga.distribution`
  - `net.fhirfactory.harmonia.erga.fhir`
  - `net.fhirfactory.harmonia.erga.hl7v2x`
  - `net.fhirfactory.harmonia.erga.hl7v2x.factories`
  - `net.fhirfactory.harmonia.erga.order.routing`
  - `net.fhirfactory.harmonia.erga.patient.demographics`
  - `net.fhirfactory.harmonia.erga.patient.identity`
  - `net.fhirfactory.harmonia.erga.registry`
  - `net.fhirfactory.harmonia.erga.result.processing`
- **Key Classes & Erga**:
  - `ErgonBase`: Abstract base class providing lifecycle hooks, security assertions, timeout bounds, and checkpoint commits.
  - `AdtDistributionErgon`: Evaluates admission/discharge/transfer triggers, resolves egress destination endpoints, and records granular destination sub-statuses (REC-002).
  - `Adt2FhirMapper` & `Adt2FhirBundleBuilder`: Translates HL7 v2 ADT segments into FHIR R5 `Patient` and `Encounter` resources.
  - `Mfn2FhirBundle` & `Mfn2FhirBundleBuilder`: Transforms HL7 v2 Master File Notifications into FHIR master entities.
  - `OrmRoutingErgon`: Routes pharmacy and laboratory order messages (ORM).
  - `OruProcessingErgon`: Transforms pathology/laboratory observations (ORU^R01) into FHIR `DiagnosticReport` and `Observation` resources.
  - **Provider Registry Change Erga**:
    - `PractitionerChangeErgon` (`Practitioner`)
    - `PractitionerRoleChangeErgon` (`PractitionerRole`)
    - `OrganizationChangeErgon` (`Organization`)
    - `LocationChangeErgon` (`Location`)
    - `HealthcareServiceChangeErgon` (`HealthcareService`)
    - `EndpointChangeErgon` (`Endpoint`)
    - `GroupChangeErgon` (`Group`)
- **Runtime Dependencies**: `calliope`, `themis-api`, `petasos-api`, Apache Camel Core (`4.4.2`), HAPI FHIR Structures R5 (`7.2.0`).

### 2.2 `praxis` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:praxis:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.praxis.cache`
  - `net.fhirfactory.harmonia.praxis.sequence`
  - `net.fhirfactory.harmonia.praxis.service`
- **Key Classes & Interfaces**:
  - `Praxis`: Core workflow blueprint interface defining stage transitions and dependencies.
  - `TaskSequenceLoader`: Scans classpath and configuration directories for declarative workflow blueprints.
  - `TaskSequenceDefaultSeeder`: Startup component that automatically validates and seeds standard blueprints into Mneme's `task-sequence-cache`.
  - `PraxisCheckpointManager`: Coordinates transition commits between sequential Ergon steps.
  - `PragmaCacheService` & `ClusterReadinessService`: Manages Hot Rod cache connections and verifies cluster health before workflow dispatch.
- **Runtime Dependencies**: `erga`, `calliope`, Jackson Databind, Infinispan Hot Rod Client (`15.0.3.Final`).

### 2.3 `ponos` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:ponos:1.0.0-SNAPSHOT` (war)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.praxis.camel`
  - `net.fhirfactory.harmonia.praxis.conduit`
  - `net.fhirfactory.harmonia.praxis.config`
  - `net.fhirfactory.harmonia.praxis.messaging`
- **Key Classes & Services**:
  - `TaskProcessorRouteBuilder`: Apache Camel route builder consuming from `petasos.queue.task.inbound` and dispatching to worker threads.
  - `TaskEventMessageProcessor` & `TaskMessageProcessor`: Camel processors unmarshaling transport envelopes and invoking `PragmaWorkflowDispatcher`.
  - `PetasosQueueToExchangeConduit`: Bridges Petasos JMS consumer streams into Camel exchange pipelines.
  - `PragmaWorkflowDispatcher`: Instantiates target Praxis blueprints and executes Erga activities within thread pools.
  - `ArtemisBrokerManager`: Manages JMS connection factories and failover parameters.
  - `HotRodClientProducer`: Produces Hot Rod remote cache managers for Mneme cache synchronization.
- **Runtime Dependencies**: `praxis`, `erga`, `petasos-artemis`, Apache Camel Core (`4.4.2`), Apache Camel JMS (`4.4.2`), ActiveMQ Artemis Jakarta Server/Client (`2.33.0`), Infinispan Hot Rod (`15.0.3.Final`), HAPI FHIR Structures (`7.2.0`), Jakarta EE 10 API.

### 2.4 `ponos-cli` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:ponos-cli:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.workflowcli`
  - `net.fhirfactory.harmonia.workflowcli.client`
  - `net.fhirfactory.harmonia.workflowcli.command`
  - `net.fhirfactory.harmonia.workflowcli.formatter`
- **Key Classes**:
  - `WorkflowCliMain`: Command-line interface entry point.
  - `WorkflowHttpClient`: REST client connecting to Ponos and Mnemosyne Operations endpoints.
  - `WorkflowCliCommand`: Administrative commands for inspecting worker thread states, queue depths, and forcing blueprint reload.
  - `WorkflowOutputFormatter`: Pretty-prints workflow metrics in ASCII tables or JSON.
- **Runtime Dependencies**: Java 21, Picocli, Jackson Databind.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Energeia Owns
- Task sequence blueprint definitions (`Praxis`, `TaskSequenceLoader`).
- Discrete activity processors (`ErgonBase`, `AdtDistributionErgon`).
- Asynchronous task processing WorkEngine runtime (`Ponos`).
- Runtime task state execution and `PragmaCheckpoint` status management (REC-002).
- Dynamic reload and cache synchronization CLI (`ponos-cli`).

### What Energeia Explicitly Does NOT Own (Anti-Responsibilities)
- Direct MLLP wire socket listening (owned by Pylai).
- Relational database schema generation or direct JDBC pools (owned by Mnemosyne).
- Security policy definition or authority evaluation (delegated to Themis).
- Broker failover socket management (owned by Petasos).

---

## 4. Configuration Parameters `[CONFIGURED]`

| Property / Env Var | Target Module | Default Value | Purpose |
| :--- | :--- | :--- | :--- |
| `PONOS_CONCURRENCY` | `ponos` | `10` | Worker consumer thread concurrency |
| `PONOS_TASK_TIMEOUT_SEC` | `ponos` | `300` | Max activity execution timeout |
| `PRAXIS_AUTO_SEED` | `praxis` | `true` | Auto-seed TaskSequences on boot |
| `PETASOS_INBOUND_QUEUE` | `ponos` | `petasos.queue.task.inbound` | Ingress queue address |

---

## 5. Verification & Testing `[IMPLEMENTED]`

- **Execute All Energeia Tests**:
  ```bash
  mvn test -pl energeia/erga,energeia/praxis,energeia/ponos,energeia/ponos-cli -am
  ```
- **Key Test Classes**: `AdtDistributionErgonTest`, `TaskSequenceLoaderTest`, `TaskProcessorRouteBuilderTest`, `PragmaWorkflowDispatcherTest`.
