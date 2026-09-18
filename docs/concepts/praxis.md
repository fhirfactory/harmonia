# Concept: Praxis `[IMPLEMENTED]`

Praxis is Harmonia's workflow orchestration and blueprint definition component within the Energeia subsystem, responsible for defining declarative execution DAGs, stage transitions, and pipeline dependencies for multi-step clinical processes.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Πρᾶξις* (Praxis)
- **Etymology**: Ancient Greek feminine noun derived from the verb *πράσσω* (prassō - to do, to act, to accomplish).
- **Philosophical Context**: In Aristotelian philosophy, human activity is divided into three fundamental domains:
  1. *Theoria* (θεωρία) — theoretical contemplation aimed at discovering truth.
  2. *Poiesis* (ποίησις) — production or fabrication aimed at creating an external artifact.
  3. *Praxis* (πρᾶξις) — practical, purposeful action guided by rational decision-making, where the action itself is directed toward a good or ethical end.
- **Architectural Rationale**: Healthcare integration is fundamentally *praxis*: it is not passive contemplation, nor is it the isolated production of widgets. It is deliberate, coordinated, and morally significant clinical action. Praxis defines how multiple discrete activities (*erga*) are composed into an orderly, coherent medical workflow that safely delivers patient information to clinicians.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Praxis specifies the structural blueprint of a clinical workflow as a Directed Acyclic Graph (DAG) of activities:

```
+---------------------------------------------------------------------------------------+
|                                    PRAXIS BLUEPRINT                                   |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                       WORKFLOW BLUEPRINT SPECIFICATION                         |  |
|   |                                                                                |  |
|   |   TaskSequence: InpatientAdmissionWorkflow                                     |  |
|   |   Trigger: HL7 ADT^A01 (Inpatient Admission Event)                             |  |
|   |                                                                                |  |
|   |   Stage 1: Validation & Security Gate                                         |  |
|   |            -> Step 1.1: Themis Ingress Authorization Gate                      |  |
|   |            -> Step 1.2: Payload Integrity & MSH Syntax Validator               |  |
|   |                                                                                |  |
|   |   Stage 2: Transformation & Canonical Mapping                                  |  |
|   |            -> Step 2.1: Adt2FhirErgon (Maps ADT to FHIR Patient & Encounter)  |  |
|   |            -> Step 2.2: Mnemosyne Clinical JPA Commit                          |  |
|   |                                                                                |  |
|   |   Stage 3: Fan-Out Distribution & Notification                                 |  |
|   |            -> Step 3.1: AdtDistributionErgon (Egress Queues: HIS, LIS, RIS)    |  |
|   |            -> Step 3.2: Agora Patient Space Collaboration Event Dispatch       |  |
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

### Dynamic Blueprint Seeding `[IMPLEMENTED]`
- On system initialization, `TaskSequenceDefaultSeeder` reads declarative blueprint definitions from classpath resources or YAML configuration files.
- Blueprints are automatically validated and seeded into the distributed Mneme cache (`task-sequence-cache`), ensuring that all worker nodes in the cluster share identical execution definitions.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Praxis Owns
- TaskSequence blueprint definitions, schemas, and step declarations.
- Dynamic blueprint loader service (`TaskSequenceLoader`).
- Startup cache seeding and validation (`TaskSequenceDefaultSeeder`).
- Dependency ordering, condition evaluation, and execution step transition rules.
- JSON/YAML schema definitions for clinical workflow blueprints.

### What Praxis Explicitly Does NOT Own (Anti-Responsibilities)
- Worker thread management or execution concurrency (owned by Ponos).
- Concrete activity processing or payload manipulation (owned by Erga).
- Runtime state tracking of a live transaction (owned by Pragma).
- Message queue transport or broker sessions (owned by Petasos).

---

## 4. Key Classes & Configuration `[IMPLEMENTED]`

| Class / Component | Module Name | Role | Status |
| :--- | :--- | :--- | :--- |
| `TaskSequenceLoader` | `energeia-praxis` | Reads and parses workflow blueprint definitions | `[IMPLEMENTED]` |
| `TaskSequenceDefaultSeeder` | `energeia-praxis` | Automatically seeds standard blueprints into Mneme cache | `[IMPLEMENTED]` |
| `TaskSequenceBlueprint` | `energeia-praxis` | Domain model representing an ordered sequence of Erga steps | `[IMPLEMENTED]` |
| `TaskSequenceStep` | `energeia-praxis` | Individual step configuration within a blueprint | `[IMPLEMENTED]` |

### Configuration Parameters `[CONFIGURED]`
- `PRAXIS_AUTO_SEED`: `true` (Enables automatic seeding of standard blueprints into Mneme on startup)
- `PRAXIS_BLUEPRINT_PATH`: `classpath:blueprints/` (Location of workflow blueprint definitions)
