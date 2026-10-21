---
{
  "schemaVersion": 1,
  "canonicalId": "f601fe1f39a3447f8e9fb2cc4ffe9dee",
  "archimateType": "CourseOfAction",
  "specialization": null,
  "displayName": "Entity-Centred Operational Coordination",
  "friendlyName": "EntityCentredOperationalCoordination",
  "canonicalName": null,
  "standing": {
    "semantic": "Canonical",
    "currentness": "current",
    "qualification": "Exact source standing retained; migration/identity allocation does not approve architectural assertions."
  },
  "identity": {
    "allocation": "UUIDv4 allocated once under the explicit RAD-1.1.5 migration Task",
    "authority": "enterprise-architecture/design/evidence/1.1.5/002/before/ai-agent-tasks/20261021T092725Z-establish-and-apply-element-continuum.md",
    "sourceAbsenceCheck": "No existing governed canonical source binding for this bounded definition; no name-based correspondence adopted."
  },
  "naming": {
    "authority": "docs/markdown/governance/canonical-identity-and-naming.md",
    "mode": "Local lexical component; no root or containment completeness inferred",
    "expected": {
      "friendlyName": "EntityCentredOperationalCoordination",
      "canonicalName": null
    }
  },
  "provenance": [
    {
      "source": "docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md",
      "selector": "### COA-05: Entity-Centred Operational Coordination",
      "role": "ingested architectural definition or explicit source contribution",
      "revision": "aadcd55252fa7965be8a4321832ecc6d5b97c6e0",
      "sha256": "dfc56c3a6de3284ecabc273032d9d8a3dabd9d41e01b29776e142a3aa0fd79f0",
      "standing": "Canonical"
    }
  ],
  "sourceBindings": [
    {
      "source": "docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md",
      "selector": "### COA-05: Entity-Centred Operational Coordination"
    }
  ],
  "exchange": {
    "identifier": "id-702fb2b9d02f581a8b5bbf9040f720c9",
    "properties": []
  },
  "relationships": [],
  "unresolvedProperties": {
    "canonicalName": "Formal qualifier for native CourseOfAction is not established in the current naming contract; HCRL 1.1.5-002 DR-5. Identity and source content are independently determinate."
  },
  "representation": {
    "sourceLinksRebased": true,
    "authority": "HCRL 1.1.5-002 DR-3; source meaning unchanged"
  }
}
---

# Entity-Centred Operational Coordination

## Architectural content

#### Strategic Approach
> **Coordinate governed information, state and operational activity around real-world healthcare entities where those entities are operationally significant in their own right and require active management, without requiring every managed entity to maintain a permanently active execution construct.**

Healthcare operations naturally center around authentic real-world entities: patients, practitioners, care teams, wards, and beds. This course of action establishes that:
- Harmonia organizes the coordination of state and ongoing activities around the operational identity of the real-world entity itself.
- **The Digital Twin is the architectural construct** through which this entity-centred coordination strategy is realised. A Twin coordinates information, state, and activity across the Mneme/Ponos seam for a specific real-world entity.
- **Demand-Driven Lifecycle**: A managed entity does not require a permanently running execution thread or process. Twins are activated when entity-centred operational activity or active monitoring is required, and become quiescent when work concludes.
- **Representation $\neq$ Twin**: Simply possessing a database record or FHIR resource does not justify or constitute a Digital Twin. Coordination is justified only when the entity is operationally active and evolving in state.

#### Motivational Grounding & Traceability
- **Architectural Axioms**:
  - `AX-01` (Health-Information Centric): Architecture aligns with genuine healthcare entities and clinical relationships.
  - `AX-11` (High Availability & Responsiveness): High concurrency is achieved by isolating entity coordination without central serialisation bottlenecks.
  - `AX-16` (Activity & State Progress Together): Entity state and active operational workflows are coordinated dynamically.
- **Strategic Drivers & Goals**: Holistic Patient-Centric Care, High Concurrency without Serialization, Real-Time Healthcare Coordination.

#### Capability Realisation & Configuration
- **Primary Capabilities**: Shapes `EC-01` (Managed Entity & Relationship), `EC-02` (Context Management), and `EC-10` (Activity & Execution).
- **Component Boundary Impact**: Validates the **Digital Twin** as an active management construct bridging the **Mneme ↔ Ponos** boundary, while asserting that the Twin is an architectural construct rather than an independent deployable platform component.

#### Downstream Direction (Domains 03–13)
- Application Architecture (Domain 05) must model Twin archetypes using composite context (combining multiple resources such as Practitioner, PractitionerRole, and Location) rather than equating twins to single FHIR resources or permanent background threads.
