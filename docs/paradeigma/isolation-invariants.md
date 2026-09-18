# Paradeigma Isolation Invariants & Architectural Rules `[IMPLEMENTED]`

> **Notice**: For the exhaustive specification of all four isolation dimensions, static token scans, packaging rules, and ArchUnit suites, see **[Production Isolation & ArchUnit Guardrails](production-isolation.md)**.

---

## 1. Architectural Invariant Rules

1. **Unidirectional Dependency**:
   - `Paradeigma` $\rightarrow$ `Production APIs`: **ALLOWED**. Paradeigma generates standard wire messages (HL7 v2 over TCP :2575, FHIR REST over HTTP :8080) and sends them to standard gateway endpoints.
   - `Production Modules` $\rightarrow$ `Paradeigma`: **FORBIDDEN**. No production code may depend on, import, or reference Paradeigma.
2. **Zero Dependency Declaration**:
   - No POM file under `calliope/`, `themis/`, `hestia/`, `petasos/`, `energeia/`, `pylai/`, or `iris/` may declare a `<dependency>` on any `paradeigma*` artifact.
3. **No Simulation Logic in Production Code**:
   - Production classes must never contain runtime checks such as `if (simulationMode)` or flags like `isSyntheticData`.
4. **No Packaging Leaks**:
   - Paradeigma classes are strictly excluded from production JARs, WARs, and Docker container images.

---

## 2. Automated ArchUnit Enforcement

These invariants are guarded by automated architecture tests located in `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/`:

- `ParadeigmaIsolationArchitectureTest.java`:
  - `productionModulesMustNotDependOnParadeigma()`: Scans all compiled production classpaths for forbidden `net.fhirfactory.harmonia.paradeigma` package references.
  - `productionPomsMustNotDeclareParadeigmaDependencies()`: Parses all 41 Maven POMs to assert zero forbidden `<dependency>` tags.
  - `productionSourceMustNotContainSimulationFlags()`: Scans Java source trees for simulation flag keywords.

---

## 3. Verification Command

```bash
mvn test -pl paradeigma/paradeigma-test -am -Dtest="ParadeigmaIsolationArchitectureTest"
```
