# DesignNote — Energeia Specialisation, Digital Twin and Dokimasia

**Status:** Candidate Architecture design decision / input to Domain05 adjudication  
**Date:** 2026-10-11  
**Scope:** Harmonia Information Systems Layer Architecture — Energeia, Ponos, Ergo/Ergon/Erga, DigitalTwin, Dokimasia, Pragma specialisation

## 1. Purpose
This DesignNote records the current architectural adjudication so Domain05 can be corrected without rediscovering design from implementation. It separates justified candidate component boundaries from detailed Dokimasia/AI discovery still required.

## 2. Energeia
Energeia is Harmonia's activity/execution architectural subsystem. Ponos, Ergo, Ergon and Erga belong within Energeia. Ponos does not contain Ergo or Ergon; Ponos and Ergo/Ergon are tightly coupled peer responsibilities.

Pragma does not belong within Energeia merely because Energeia operates upon it. Pragma is managed information and its management responsibility belongs with Mneme.

## 3. Ponos
Ponos is Harmonia's reusable execution framework/component pattern. It provides common execution machinery upon which specialised execution components can be built. Architecturally this is similar to a supertype/superclass relationship, without requiring literal implementation inheritance.

DigitalTwin and Dokimasia are specialised Ponos-based execution components. They are not structurally contained by the Ponos runtime component.

## 4. Ergo, Ergon and Erga
**Ergo** establishes the reusable conceptual activity pattern and platform-integration contract for conforming Ergon. This includes Context Loading/Unloading and standard integration points/placeholders for Petasos adapters, assurance services, logging/observability, checkpoints and other platform integration. Ergo is not the execution framework; Ponos is.

**Ergon** is a single, well-defined software component encapsulating a bounded, fine-grained task-completion activity set. It supplies specialised activity/business behaviour within the Ergo boundary and is executed/managed through Ponos. An Ergon has no discrete Information Architecture representation merely because it is an Ergon.

**Erga** is a meaningful collection of Ergon for: (a) collating components into executable hubs; (b) capturing/observing activity where multiple concurrent Ergon, including instances of the same type, may execute; and (c) libraries of conceptually related Ergon suitably constrained for coherent change-management, testing, versioning and release. Erga is not merely an informal plural or List<Ergon>.

## 5. Pragma Management
Pragma is managed information / an Application Data Object. Pragma management belongs with Mneme:

    Mneme
      └── Pragma Management
            ├── PragmaFactory
            └── Pragma

PragmaFactory is the logical application embodiment associated with the Domain04 ActionableTaskArchetype meta-concept and creates/initialises appropriate Pragma representations.

Execution, behaviour upon, or transport of managed information does not confer information-management responsibility.

## 6. DigitalTwin Specialisation
DigitalTwin is a specialised Ponos-based execution component for entity-specific operational coordination. It adds Twin-specific execution semantics, including the per-Twin queue pattern. The per-Twin queue model does not imply one thread per Twin.

**Behaviour** is a specialised Ergon for Digital Twin operation. It therefore inherits the process-management, assurance, reporting, Context Loading/Unloading, platform-integration, Petasos, logging/observability and other common functionality established through Ergo/Ergon.

**Stimulus** and **Effector** are candidate specialisations of Pragma where Digital Twin semantics require additional information behaviour. Because they remain Pragma, common Pragma process-management, assurance and reporting functionality is already available to the Digital Twin variants.

## 7. Architectural Inheritance Principle
Specialised Harmonia execution constructs SHALL reuse common platform management, assurance, reporting and integration capabilities inherited through their base architectural constructs. Specialisation introduces only behaviour and information semantics necessary for the specialised role.

DigitalTwin does not independently establish parallel logging, assurance, reporting, messaging, context-loading, task/progression or persistence frameworks unless a genuine Twin-specific architectural requirement is established.

## 8. Dokimasia Specialisation
Dokimasia follows the same specialisation strategy. It is a specialised Ponos-based execution component for independent governed assurance activity, reusing Ponos, Ergo/Ergon, Pragma management, platform integration, process management and reporting.

Two significant exceptions apply:

### 8.1 No recursive Dokimasia monitoring
A Dokimasia execution SHALL NOT itself be subject to monitoring, assurance or assessment by another Dokimasia execution. This establishes a finite assurance boundary.

### 8.2 No separate AuditEvent
A Dokimasia execution SHALL NOT generate a separate AuditEvent for its own execution. The governed Dokimasia assurance record/result provides the authoritative evidence of that assurance activity.

## 9. Dokimasia Ergon Specialisation
A discrete Dokimasia-specific Ergon specialisation is required.

A **Dokimasia Ergon** is an Ergon specialisation representing a bounded assurance activity executed within a Dokimasia workflow. It inherits standard Ergo/Ergon execution, context, integration, reporting and process-management contracts, subject to Dokimasia constraints.

An **AI Assurance Ergon** is a candidate Dokimasia Ergon specialisation representing AI-based processing as an explicit governed activity within a Dokimasia assurance workflow. This is architecturally distinct from an ordinary Ergon internally calling an AI service as an implementation choice.

Other potential assurance activity classifications/specialisations require further adjudication.

## 10. Dokimasia Pragma Specialisation
Dokimasia-specific Pragma semantics are justified, but exact names and detailed information semantics remain subject to Domain05 discovery. Candidate semantic roles include an assurance request/input and assurance result/finding. They should remain candidate Pragma specialisations until responsibilities, lifecycle and relationships are adjudicated.

## 11. Candidate Structural Representation

    EXECUTION                 ACTIVITY                  INFORMATION

    Ponos                     Ergo                      Pragma
      │                         │                         │
      ├─ DigitalTwin            └─ Ergon                  ├─ Stimulus
      │                            ├─ Behaviour            ├─ Effector
      │                            │
      └─ Dokimasia                └─ Dokimasia Ergon      ├─ Assurance request*
                                      └─ AI Assurance     └─ Assurance result*
                                          Ergon

    * candidate semantic roles; names/details not final

These are specialisation/reuse relationships and must not be misrepresented as simple runtime containment.

## 12. Dokimasia AI Discovery Obligation
The discrete component boundary for AI participation in Dokimasia is justified. Its detailed implementation model is not finalised.

Domain05 must undertake explicit Information Systems Layer Architecture discovery before implementation models are finalised, including: AI model/service selection and abstraction; context/input construction; information disclosure/security; model/service provenance and versioning; confidence/uncertainty/result representation; deterministic versus non-deterministic behaviour; reproducibility; result validation; human review/escalation; assurance evidence representation; workflow integration; failure/timeout/unavailability behaviour; and reporting/observability consistent with Dokimasia's terminal-boundary rules.

Unresolved detailed design does not invalidate the candidate component boundary; it establishes a Domain05 discovery obligation.

## 13. Consequences for Current Domain05 Reconstruction
The Candidate Architecture should be reviewed/corrected to reflect at least:
1. Pragma is not structurally beneath Energeia; Pragma management belongs with Mneme.
2. Ponos, Ergo/Ergon and Erga belong within Energeia, with Ponos and Ergo/Ergon tightly coupled rather than containment.
3. Ergo, Ergon and Erga are not one undifferentiated component.
4. DigitalTwin is a Ponos-specialised execution component within the Energeia family.
5. Behaviour is a DigitalTwin-specific Ergon specialisation.
6. Stimulus and Effector are candidate Pragma specialisations; detailed semantics remain open.
7. Dokimasia is a Ponos-specialised assurance execution component within Energeia.
8. Dokimasia Ergon and AI Assurance Ergon candidate specialisations are represented.
9. Dokimasia-specific Pragma semantic roles remain explicitly candidate/unresolved.
10. No-recursive-Dokimasia and no-separate-AuditEvent rules are explicit.
11. AI-based Dokimasia participation is a Domain05 discovery obligation before implementation models are finalised.

## 14. Not Finalised
This note does not finalise detailed Stimulus/Effector schemas/lifecycle; final names/schema for Dokimasia Pragma specialisations; complete Dokimasia workflow/Praxis semantics; whether deterministic/human assurance warrant formal Ergon subclasses; detailed AI service/model architecture; persistence structures; implementation inheritance mechanisms; complete Energeia deployment topology; or the full relationship of Praxis to Ergo, Ergon and Erga.
