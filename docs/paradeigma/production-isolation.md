# Paradeigma Production Isolation & ArchUnit Guardrails `[IMPLEMENTED]`

To ensure production stability, clinical safety, and regulatory compliance, Harmonia enforces an uncompromised **architectural isolation boundary** between production subsystems and the Paradeigma simulation framework.

---

## 1. Unidirectional Dependency Invariant `[IMPLEMENTED]`

> **Rule: `Paradeigma -> Production` is ALLOWED; `Production -> Paradeigma` is STRICTLY FORBIDDEN.**

```
+-------------------------------------------------------------------------+
|                  Paradeigma Simulation & Testbed                        |
|   (paradeigma-common, pas, emr, lms, rispac, scenarios, test)           |
+-------------------------------------------------------------------------+
                                    │
                                    │ [ALLOWED] Invocations via public
                                    │ wire protocols (MLLP :2575, REST :8080)
                                    ▼
+-------------------------------------------------------------------------+
|                      Production Harmonia HIE                            |
| (calliope, themis, hestia, petasos, energeia, pylai, iris, agora)       |
+-------------------------------------------------------------------------+
                                    │
                                    │ [FORBIDDEN] Zero imports, zero deps,
                                    │ zero simulation flags, zero leaks
                                    X (BLOCKED BY ARCHUNIT)
```

1. **Simulators as Decoupled Clients**: Paradeigma simulators interact with Harmonia as external wire clients. They send standard HL7 v2 frames over MLLP TCP ports and FHIR R5 bundles over HTTP REST endpoints.
2. **Zero Production Contamination**: Production code does not know Paradeigma exists. No production Java class may import, reference, or bundle Paradeigma code.
3. **No Synthetic Runtime Branches**: Production classes must never contain runtime checks such as `if (simulationMode)` or `if (paradeigmaMode)`.

---

## 2. Four Dimensions of Isolation `[IMPLEMENTED]`

Harmonia enforces isolation across four distinct technical dimensions:

### 2.1 Dimension 1: Compile-Time Maven Dependency Isolation
- **Invariant**: No Maven POM under `calliope/`, `themis/`, `hestia/`, `petasos/`, `energeia/`, `pylai/`, `iris/`, or `agora/` may declare a `<dependency>` on any `net.fhirfactory.harmonia:paradeigma*` artifact.
- **Enforcement**: Continuous automated POM tree scan across all reactor modules.

### 2.2 Dimension 2: Package & Classpath Import Isolation
- **Invariant**: No compiled production class outside of `net.fhirfactory.harmonia.paradeigma..` may depend on or import classes in `net.fhirfactory.harmonia.paradeigma..`.
- **Enforcement**: Classpath byte-code inspection via ArchUnit `noClasses().that().resideOutsideOfPackage("..paradeigma..").should().dependOnClassesThat().resideInAPackage("..paradeigma..")`.

### 2.3 Dimension 3: Source Code Token & Flag Isolation
- **Invariant**: Production Java source files (`src/main/java`) must never contain simulation token keywords:
  - `import net.fhirfactory.harmonia.paradeigma`
  - `paradeigmaMode`
  - `simulationMode`
  - `syntheticRequest`
  - `isParadeigmaGenerated`
  - `harmonia.paradeigma.enabled`
- **Enforcement**: Recursive AST and static source scanner scanning all production source directories.

### 2.4 Dimension 4: Deployment Packaging & Container Image Isolation
- **Invariant**: Production deployment artifacts (Iris BEFE WAR, Ponos bootable JAR, Pylai gateway, Agora service, and production Docker container images) must never package Paradeigma JARs or test dependencies.
- **Enforcement**: Maven assembly descriptor audit and container image layer inspection.

---

## 3. Automated ArchUnit Architecture Test Suite `[IMPLEMENTED]`

These invariants are continuously verified by JUnit 5 tests utilizing **ArchUnit 1.3** located in `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/`:

### 3.1 `ParadeigmaIsolationArchitectureTest`
- **`noProductionClassesShouldDependOnParadeigma()`**: Evaluates compiled classes in `net.fhirfactory.harmonia..` (excluding test scopes) to assert zero dependency on the Paradeigma package.
- **`noProductionPomsShouldDeclareParadeigmaDependency()`**: Walks the filesystem and parses every production `pom.xml`, asserting zero `<artifactId>paradeigma*` entries.
- **`noProductionCodeShouldContainSimulationFlagsOrImports()`**: Scans all production `src/main/java` files for forbidden simulation tokens.
- **`productionPackagingExcludesParadeigma()`**: Verifies that CLI and server deployment POMs exclude Paradeigma artifacts.

### 3.2 `AgoraIsolationArchitectureTest`
- **`agoraMustNotDependOnPonos()`**: Asserts that Agora never directly imports or depends on Ponos task execution classes (`net.fhirfactory.harmonia.energeia.ponos..`).
- **`matrixTypesMustNotLeakOutsideAgora()`**: Asserts that Matrix protocol DTOs and client adapters (`net.fhirfactory.harmonia.agora.matrix..`) never leak into Calliope, Themis, Petasos, Energeia, Pylai, or Iris.
- **`agoraApiMustNotDependOnMatrixInternalTypes()`**: Asserts that `agora-api` public contracts remain free of `agora-matrix` types.
- **`agoraMustNotDependOnParadeigma()`**: Asserts that Agora production classes never import Paradeigma simulation modules.

### 3.3 Platform-Wide Architectural Governance Suites
- **`PetasosApiIsolationArchitectureTest`**: Asserts that `petasos-api` remains strictly free of JMS or ActiveMQ Artemis dependencies.
- **`IrisDecouplingArchitectureTest`**: Asserts zero direct JPA, Hibernate, or PostgreSQL database dependencies in Iris BEFE or SPAs.
- **`ProviderRegistryArchitectureTest`**: Asserts that `iris-administration` consumes Provider Registry APIs without owning server-side validation or relational tables.
- **`PackageLayeringArchitectureTest`**: Asserts strict unidirectional dependency layering across all 9 subprojects.
- **`SecurityEnforcementArchitectureTest`**: Asserts Themis policy contracts and security context propagation.

---

## 4. Execution & CI/CD Verification Commands `[IMPLEMENTED]`

Run the full architectural isolation suite in under 30 seconds:

```bash
# Run all ArchUnit architecture tests
mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
```

Targeted execution:
```bash
# Verify Paradeigma isolation boundary specifically
mvn test -pl paradeigma/paradeigma-test -am -Dtest="ParadeigmaIsolationArchitectureTest"

# Verify Agora subsystem isolation
mvn test -pl paradeigma/paradeigma-test -am -Dtest="AgoraIsolationArchitectureTest"
```
