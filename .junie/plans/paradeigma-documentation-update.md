---
sessionId: session-260916-142045-147e
---

# Requirements

### Overview & Goals
The objective of this task is to provide complete, publication-grade documentation for the `Paradeigma` simulation and testbed subsystem across both the LaTeX architecture specification (`docs/latex/`) and the LibreOffice OpenDocument Text specification (`docs/libreoffice/`). 

With the recent introduction of HL7 FHIR R5 Provider Registry simulation capabilities and Themis security/authorization fixtures, the documentation must be updated to give developers and systems integrators clear, practical instructions for configuring, executing, and verifying all simulator modules, deterministic data generators, security actors, and test scenarios.

### Scope
- **In Scope**:
  - Updating `docs/latex/chapters/appendix-paradeigma.tex` with comprehensive sections covering:
    - Dedicated FHIR R5 Provider Registry synthetic generators and topological graph builders (`SyntheticProviderRegistryGenerator`).
    - Standardized Australian digital health identifiers (HPI-I, HPI-O, AHPRA, SNOMED CT).
    - Themis security actors catalog (`ParadeigmaSecurityActors`), security contexts (`SecurityScenarioContext`), and negative testing authorization failure seams.
    - PHI-aware dual-gate logging test probes (`PhiLogTestProbe`) and credential leakage assertions (`SecretLeakageAssertion`).
    - Complete configuration matrix covering system properties, environment variables, ports, and default values.
    - Step-by-step CLI and REST execution instructions (Maven, Spring Boot, Docker Compose, cURL, automated JUnit test suites).
  - Updating `docs/libreoffice/scripts/generate_odt.py` to ensure all updated LaTeX chapters (including `appendix-paradeigma.tex` and `appendix-phi-logging.tex`) are processed and compiled into `harmonia-architecture-specification.odt`.
  - Verifying document builds across both LaTeX (`make pdf`) and LibreOffice (`make build`, `make validate`).
- **Out of Scope**:
  - Modifying the underlying Java simulator code or runtime business logic in `paradeigma-common`, `paradeigma-scenarios`, or `paradeigma-test`.
  - Modifying core platform gateway or workflow engine implementations.

### User Stories
- **As a System Integrator / Test Engineer**, I want comprehensive configuration and execution instructions for Paradeigma so that I can reliably deploy, configure, and orchestrate realistic clinical simulations and Provider Registry workloads.
- **As a Security Auditor / Platform Developer**, I want clear documentation on `ParadeigmaSecurityActors`, Themis policy fixtures, and `PhiLogTestProbe` so that I can verify compliance with dual-gate logging policies, credential protection, and RBAC/ABAC authorization constraints.
- **As an Architect / Document Reader**, I want both the LaTeX PDF and LibreOffice ODT editions of the Harmonia architecture manual to remain strictly synchronized and complete.

### Functional Requirements
1. **Provider Registry Generator Reference**: Document the 7 dedicated FHIR R5 generators (`Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, `Group`) and 5 graph topologies (Solo Practitioner, Multi-Role Specialist, Healthcare Organization, Connected Provider Network, Invalid Reference Graph).
2. **Themis Security Actor Reference**: Detail the 7 standard actors (`Provider Steward`, `Clinician`, `System Administrator`, `Integration Service`, `Read-Only User`, `Unauthorized User`, `Unauthenticated`), their authority mappings, and simulation seams for failure injection (`expiredContext`, `revokedAuthorityContext`, `missingRoleContext`).
3. **PHI Logging & Secret Leakage Reference**: Detail the dual-gate evaluation rules, test probe integration, and zero-leakage assertions.
4. **Configuration Reference**: Provide exact configuration property tables (environment variables, system properties, default values, port mappings).
5. **Execution Instructions**: Provide concrete CLI commands for building, running standalone simulators, deploying Docker Compose topologies, executing cURL REST calls for scenario orchestration, and running automated test suites.
6. **LibreOffice Build Synchronicity**: Ensure `generate_odt.py` processes all chapter sources and generates a valid OASIS OpenDocument Text package.

# Technical Design

### Current Implementation
- `docs/latex/chapters/appendix-paradeigma.tex` documents the original 4 hospital MLLP simulators (`PAS`, `EMR`, `LMS`, `RIS-PACS`), 9-step Patient Journey, and `FailureSimulator` fault injection, but lacks documentation for the new Provider Registry generators, Themis security simulation actors, PHI log testing probes, and unified configuration/execution guides.
- `docs/latex/main.tex` includes chapters 1--6 and appendices A--F (`appendix-paradeigma`, `appendix-mllp-services`, `appendix-ergon-module`, `appendix-praxis-workflow`, `appendix-provider-registry`, `appendix-phi-logging`).
- `docs/libreoffice/scripts/generate_odt.py` converts LaTeX chapters to `harmonia-architecture-specification.odt`. Its `chapter_files` list currently omits `appendix-phi-logging.tex`.

### Proposed Changes

#### 1. LaTeX Documentation Updates (`docs/latex/chapters/appendix-paradeigma.tex`)
- **Section: Provider Registry Simulation & Synthetic Generators**:
  - Document `PractitionerGenerator`, `PractitionerRoleGenerator`, `OrganizationGenerator`, `LocationGenerator`, `HealthcareServiceGenerator`, `EndpointGenerator`, and `GroupGenerator`.
  - Detail Australian digital health identifiers (HPI-I: `800361...`, HPI-O: `800362...`, AHPRA, SNOMED CT).
  - Detail `SyntheticProviderRegistryGenerator` graph builders: `generateSoloPractitioner()`, `generatePractitionerWithMultipleRoles()`, `generateOrganizationWithLocations()`, `generateProviderNetwork()`, and `generateInvalidReferenceGraph()`.
  - Include deterministic seed behavior (`SeedRandom`).
- **Section: Themis Security Simulation & Authorization Fixtures**:
  - Document `ParadeigmaSecurityActors` catalog and role/authority mapping table (`PRV_ADM`, `PRV_SUB`, `PRV_APR`, `PRV_RDR`, `SYS_ADM`, `SYS_INT`, `PRV_PROC`).
  - Document `SecurityScenarioContext` usage and Themis authorization evaluation.
  - Document negative testing seams: `expiredContext()`, `revokedAuthorityContext()`, `missingRoleContext()`.
- **Section: PHI-Aware Logging Simulation & Credential Protection**:
  - Document `PhiLogTestProbe` lifecycle, dual-gate verification (`assertNoPhiInOperationalLogs`, `assertPhiPresentInDiagnosticLogs`, `assertPhiMarkerAttachedToDiagnosticLogs`).
  - Document `SecretLeakageAssertion` for synthetic secret scanning.
  - Include comprehensive worked example listing.
- **Section: Configuration Reference Matrix**:
  - Structured tables detailing simulator configuration keys, default ports (8090--8094, 2101--2104, 2201--2205, 8089), Themis switches, and PHI logging properties (`harmonia.logging.phi-enabled`).
- **Section: Step-by-Step Execution Guide**:
  - Maven commands (`mvn clean install -DskipTests`, `mvn test -pl paradeigma/...`).
  - Standalone Spring Boot runner commands for each simulator microservice.
  - Docker Compose lifecycle commands (`docker compose -f docker-compose-paradeigma.yml up -d`).
  - REST API orchestration cURL examples (journey execution, concurrency profiles, status monitoring).
  - Integration scenario test suite execution instructions (`ProviderRegistryLifecycleScenarioTest`, `ProviderRegistrySecurityScenarioTest`, `ProviderRegistryLoggingScenarioTest`, `ProviderRegistryCrossCapabilityE2ETest`).

#### 2. LibreOffice Generator Synchronization (`docs/libreoffice/scripts/generate_odt.py`)
- Append `appendix-phi-logging.tex` to `chapter_files` array.
- Verify table and listing parser compatibility for the expanded `appendix-paradeigma.tex` content.

### File Structure Modifications
- `docs/latex/chapters/appendix-paradeigma.tex`: Enhanced with Provider Registry, Security, Logging, Configuration, and Execution sections.
- `docs/libreoffice/scripts/generate_odt.py`: Updated chapter inclusions.
- `docs/libreoffice/harmonia-architecture-specification.odt`: Regenerated and validated binary output.

# Testing

### Validation Approach
The changes will be validated by executing the automated document build pipelines and verifying structural integrity, format validity, and content completeness.

### Key Scenarios
1. **LaTeX Document Compilation**:
   - Execute `make clean` followed by `make pdf` (or Docker-based compilation if local TeX Live is unavailable) in `docs/latex/`.
   - Verify that compilation completes without errors and produces `docs/latex/main.pdf`.
   - Confirm table of contents, list of figures, list of tables, and cross-references resolve properly.

2. **LibreOffice ODT Generation & Validation**:
   - Execute `make build` in `docs/libreoffice/` (`python3 scripts/generate_odt.py`).
   - Verify all chapters, sections, tables, code listings, and diagrams parse successfully.
   - Execute `make validate` in `docs/libreoffice/` (`python3 scripts/generate_odt.py --validate`).
   - Confirm valid OpenDocument zip structure, uncompressed `mimetype`, well-formed XML in `content.xml`, `styles.xml`, and `META-INF/manifest.xml`.

3. **Content Verification**:
   - Verify presence and accuracy of all 7 FHIR R5 generators and graph topologies.
   - Verify presence of Themis security actors table and context factories.
   - Verify presence of PHI logging dual-gate and secret leakage assertion guides.
   - Verify exact port numbers, configuration keys, and execution cURL / CLI snippets match actual project source code.

# Delivery Steps

### ✓ Step 1: Document Provider Registry, Security, and PHI Logging Subsystems in LaTeX
Expand `docs/latex/chapters/appendix-paradeigma.tex` to thoroughly document the provider registry synthetic generators, Themis security fixtures, and PHI logging test probes.

- Add documentation for the 7 FHIR R5 generators (`PractitionerGenerator`, `PractitionerRoleGenerator`, `OrganizationGenerator`, `LocationGenerator`, `HealthcareServiceGenerator`, `EndpointGenerator`, `GroupGenerator`) and `SyntheticProviderRegistryGenerator` graph topologies.
- Detail Australian digital health identifier rules (HPI-I, HPI-O, AHPRA, SNOMED CT) and seed determinism.
- Document `ParadeigmaSecurityActors` catalog, RBAC/ABAC role mappings, `SecurityScenarioContext`, and authorization failure test seams (`expiredContext`, `revokedAuthorityContext`, `missingRoleContext`).
- Detail `PhiLogTestProbe`, dual-gate logging validation, `SecretLeakageAssertion`, and include a multi-capability worked code listing.

### ✓ Step 2: Add Configuration Matrix and Step-by-Step Execution Instructions in LaTeX
Add comprehensive configuration parameters and step-by-step execution guides to `docs/latex/chapters/appendix-paradeigma.tex`.

- Add a structured Configuration Reference table detailing simulator properties, port allocations, Themis policy flags, and PHI logging switches (`harmonia.logging.phi-enabled`).
- Add step-by-step CLI execution instructions for Maven builds (`mvn clean install`, `mvn test`), individual Spring Boot microservices, and Docker Compose orchestration (`docker-compose-paradeigma.yml`).
- Detail REST API management commands (cURL snippets) for triggering hospital events, running scenario engine profiles (`TEST`, `DEMO`, `LOAD`), and executing Provider Registry journeys.
- Document automated scenario test suite execution covering `ProviderRegistryLifecycleScenarioTest`, `ProviderRegistrySecurityScenarioTest`, `ProviderRegistryLoggingScenarioTest`, and `ProviderRegistryCrossCapabilityE2ETest`.

### ✓ Step 3: Synchronize LibreOffice ODT Generator and Validate Document Builds
Synchronize the LibreOffice generator script and regenerate the master OpenDocument specification.

- Update `docs/libreoffice/scripts/generate_odt.py` to include `appendix-phi-logging.tex` in the chapter compilation sequence alongside the updated `appendix-paradeigma.tex`.
- Verify the parser compatibility for all newly added LaTeX environments (tables, code listings, callout boxes).
- Execute `make build` and `make validate` in `docs/libreoffice/` to generate and validate `harmonia-architecture-specification.odt`.
- Validate LaTeX compilation via `docs/latex/Makefile` (`make pdf` / `make clean`).