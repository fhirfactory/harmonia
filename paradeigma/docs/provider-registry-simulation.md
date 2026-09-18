# Harmonia Paradeigma — Provider Registry Simulation Guide

### 1. Overview
The Paradeigma Provider Registry Simulation framework enables deterministic generation, testing, and scenario orchestration of HL7 FHIR Release 5 Provider Registry resources.

All generated resources strictly follow canonical Australian digital health identifiers:
- **HPI-I**: Healthcare Provider Identifier - Individual (`http://ns.electronichealth.net.au/id/hi/hpii/1.0`, prefix `800361...`)
- **HPI-O**: Healthcare Provider Identifier - Organisation (`http://ns.electronichealth.net.au/id/hi/hpio/1.0`, prefix `800362...`)
- **AHPRA**: Australian Health Practitioner Regulation Agency registration numbers
- **SNOMED CT**: Standardized Australian clinical specialty codes

---

### 2. Dedicated Synthetic Generators

Each resource type has a dedicated, seed-controlled generator in `net.fhirfactory.harmonia.paradeigma.common.generator`:

| Generator | Resource Type | Key Capabilities |
|-----------|---------------|------------------|
| `PractitionerGenerator` | `Practitioner` | Deterministic names, HPI-I, AHPRA, qualifications, telecoms, update and duplicate variations |
| `PractitionerRoleGenerator` | `PractitionerRole` | Role codes, SNOMED specialty codes, multi-target references (Org, Loc, Svc, Endpoint) |
| `OrganizationGenerator` | `Organization` | Network names, HPI-O, ABN, provider types, physical and postal addresses |
| `LocationGenerator` | `Location` | Facility and wing names, physical forms, managing organization references |
| `HealthcareServiceGenerator` | `HealthcareService` | Service specialties, opening hours, contact details, organization references |
| `EndpointGenerator` | `Endpoint` | FHIR REST, HL7 v2 MLLP, and Direct secure messaging connection configurations |
| `GroupGenerator` | `Group` | Practitioner specialist panels, committees, and enumerated clinical teams |

#### Example: Seeded Practitioner Generation
```java
// Identical seed produces bit-for-bit identical FHIR resource
PractitionerGenerator generator = new PractitionerGenerator(12345L);
Practitioner drBowman = generator.generateValid("pract-dr-bowman-01");

// Generate duplicate identifier for conflict testing
Practitioner duplicate = generator.generateDuplicateIdentifier("pract-dup-01", "8003610000000001");

// Generate incomplete payload for validation testing
Practitioner incomplete = generator.generateIncomplete("pract-inc-01");
```

---

### 3. Interconnected Topology Graph Builders

`SyntheticProviderRegistryGenerator` provides high-level graph builders to construct realistic topologies:

1. **Solo Practitioner**:
   ```java
   SyntheticProviderRegistryGenerator gen = new SyntheticProviderRegistryGenerator(42L);
   ProviderRegistryGraph solo = gen.generateSoloPractitioner(42L);
   // Returns Organization, Practitioner, and PractitionerRole
   ```

2. **Specialist with Multiple Roles**:
   ```java
   ProviderRegistryGraph multi = gen.generatePractitionerWithMultipleRoles(42L);
   // Returns Practitioner linked to multiple Organizations and Locations via distinct Roles
   ```

3. **Healthcare Organization with Locations & Endpoints**:
   ```java
   ProviderRegistryGraph orgTopology = gen.generateOrganizationWithLocations(42L);
   ```

4. **Connected Provider Network**:
   ```java
   ProviderRegistryGraph network = gen.generateProviderNetwork(42L);
   // Returns Organization, Location, Endpoint, HealthcareService, Practitioner, PractitionerRole, Group
   ```

5. **Deliberately Broken Reference Graph (Negative Testing)**:
   ```java
   ProviderRegistryGraph broken = gen.generateInvalidReferenceGraph(42L);
   // Contains references to non-existent Organization and Location for testing OperationOutcome rejections
   ```

---

### 4. Governed Write Lifecycle Testing

```java
// 1. Ingress Change Submission
ResponseEntity<String> response = gatewayController.createResource(
    "Practitioner",
    fhirJson,
    "corr-id-001",
    "pas",
    "steward",
    null
);
assertThat(response.getStatusCode().value()).isEqualTo(202);
String location = response.getHeaders().getLocation().toString();
String pragmaId = location.substring(location.lastIndexOf('/') + 1);

// 2. Ergon Processing
Pragma pragma = pragmaCache.get(pragmaId);
practitionerErgon.processErgon(pragma, exchange);
assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

// 3. Synchronous Read & Search
ResponseEntity<String> getResp = gatewayController.getResource("Practitioner", "pract-id", null);
assertThat(getResp.getStatusCode().value()).isEqualTo(200);
```
