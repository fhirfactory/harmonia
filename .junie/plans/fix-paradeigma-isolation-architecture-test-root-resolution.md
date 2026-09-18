---
sessionId: session-260916-184346-14ww
---

# Requirements

### Overview & Goals
The goal of this change is to fix the test failures in `ParadeigmaIsolationArchitectureTest` by ensuring reliable, environment-agnostic discovery of the Harmonia repository root when running tests from submodule working directories (e.g., `paradeigma/paradeigma-test`) as well as from the project root.

### Scope
- **In Scope**:
  - Fix `findProjectRoot()` in `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/ParadeigmaIsolationArchitectureTest.java`.
  - Ensure dynamic parent traversal locates the root containing `pom.xml`, `paradeigma`, and `calliope`.
  - Eliminate the hardcoded fallback path `/Users/markhunter/Development/SourceCode/Projects/github/harmonia`.
- **Out of Scope**:
  - Modifications to production code or POM dependencies.
  - Changes to other test suites in `paradeigma-test`.

### User Stories
- As a developer or CI pipeline running tests in the `paradeigma` test suite, I want `ParadeigmaIsolationArchitectureTest` to automatically locate the Harmonia repository root regardless of current working directory so that isolation checks run cleanly without failing on non-existent local file paths.

### Functional Requirements
- `ParadeigmaIsolationArchitectureTest.noProductionPomsShouldDeclareParadeigmaDependency` must locate all production `pom.xml` files and assert that none declare dependencies on `paradeigma` modules.
- `ParadeigmaIsolationArchitectureTest.noProductionCodeShouldContainSimulationFlagsOrImports` must locate all production Java source files under `calliope`, `themis`, `hestia`, `iris`, `pylai`, `energeia`, and `petasos` and assert that no forbidden simulation flags or imports exist.
- `ParadeigmaIsolationArchitectureTest.productionPackagingExcludesParadeigma` must locate CLI and console POM files and assert absence of `paradeigma` dependencies.

### Non-Functional Requirements
- **Portability**: Test execution must not depend on any specific developer username, local path, or operating system directory structure.
- **Fail-Fast**: If root detection fails due to being invoked completely outside a Harmonia repository, throw a clear `IllegalStateException` describing the unresolved directory.

# Technical Design

### Current Implementation
In `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/ParadeigmaIsolationArchitectureTest.java`, `findProjectRoot()` currently checks:
```java
private Path findProjectRoot() {
    Path current = Paths.get(".").toAbsolutePath().normalize();
    while (current != null && !Files.exists(current.resolve("pom.xml"))) {
        current = current.getParent();
    }
    if (current != null && Files.exists(current.resolve("paradeigma")) && Files.exists(current.resolve("calliope"))) {
        return current;
    }
    return Paths.get("/Users/markhunter/Development/SourceCode/Projects/github/harmonia");
}
```

### Root Cause Analysis
1. When Surefire executes tests from within the `paradeigma/paradeigma-test` submodule directory, `Paths.get(".")` is `/path/to/harmonia/paradeigma/paradeigma-test`.
2. Because `paradeigma-test` has its own `pom.xml`, the loop `!Files.exists(current.resolve("pom.xml"))` terminates immediately on the child module directory.
3. The child module directory does not contain `paradeigma` or `calliope` subdirectories, causing the condition `Files.exists(current.resolve("paradeigma")) && Files.exists(current.resolve("calliope"))` to evaluate to `false`.
4. The method falls back to the hardcoded macOS path `/Users/markhunter/Development/SourceCode/Projects/github/harmonia`.
5. On any other machine or CI container, this path does not exist, leading to:
   - `NoSuchFileException` in `Files.walk(projectRoot)` for `noProductionPomsShouldDeclareParadeigmaDependency`.
   - `AssertionError: Expecting actual not to be empty` in `noProductionCodeShouldContainSimulationFlagsOrImports` because `productionJavaFiles` is empty.

### Key Decisions
- **Decision**: Perform loop traversal where each iteration tests the root criteria (`pom.xml`, `paradeigma/`, and `calliope/`) before moving to `current.getParent()`.
  - *Rationale*: This guarantees that child modules containing their own `pom.xml` continue ascending until the true repository root is reached.
- **Decision**: Replace the hardcoded path fallback with an `IllegalStateException`.
  - *Rationale*: Fails with clear diagnostic context if run in an unsupported environment, rather than masking failures with an unreachable hardcoded path.

### Proposed Changes
Update `findProjectRoot()` in `ParadeigmaIsolationArchitectureTest.java`:
```java
private Path findProjectRoot() {
    Path current = Paths.get(".").toAbsolutePath().normalize();
    while (current != null) {
        if (Files.exists(current.resolve("pom.xml"))
                && Files.exists(current.resolve("paradeigma"))
                && Files.exists(current.resolve("calliope"))) {
            return current;
        }
        current = current.getParent();
    }
    throw new IllegalStateException("Could not determine Harmonia repository root from working directory: " 
            + Paths.get(".").toAbsolutePath().normalize());
}
```

### Components & File Structure
- Modified File:
  - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/ParadeigmaIsolationArchitectureTest.java`

### Risks & Mitigations
- *Risk*: Symbolic links or alternative working directory invocations.
  - *Mitigation*: Using `.toAbsolutePath().normalize()` ensures reliable canonical path ascension.

# Testing

### Validation Approach
Verify that the test suite compiles and runs all test cases in `ParadeigmaIsolationArchitectureTest` without errors or failures under Maven Surefire.

### Key Scenarios
1. **Repository-Level Test Execution**:
   - Run `mvn test -pl paradeigma/paradeigma-test -am -Dtest=ParadeigmaIsolationArchitectureTest -Dsurefire.failIfNoSpecifiedTests=false` from repository root.
   - Verify that all 4 tests pass:
     - `noProductionClassesShouldDependOnParadeigma`
     - `noProductionPomsShouldDeclareParadeigmaDependency`
     - `noProductionCodeShouldContainSimulationFlagsOrImports`
     - `productionPackagingExcludesParadeigma`
2. **Submodule Working Directory Test Execution**:
   - Run Maven test command inside `paradeigma/paradeigma-test` directory.
   - Verify that `findProjectRoot()` ascends past `paradeigma/paradeigma-test` and `paradeigma` to locate the platform root, verifying non-empty production POM and Java file lists.

# Delivery Steps

### ✓ Step 1: Refactor project root resolution in ParadeigmaIsolationArchitectureTest
ParadeigmaIsolationArchitectureTest resolves the Harmonia repository root dynamically without environment-specific hardcoded paths.

- Update `findProjectRoot()` in `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/ParadeigmaIsolationArchitectureTest.java` to traverse upwards through parent directories until the repository root is found.
- At each step in the traversal, verify the root markers (`pom.xml`, `paradeigma/`, and `calliope/` directories).
- Remove the hardcoded macOS developer path (`/Users/markhunter/...`) fallback and throw an descriptive `IllegalStateException` if root markers are not found.

### ✓ Step 2: Validate Paradeigma isolation architecture tests
All architectural isolation and POM dependency test assertions in ParadeigmaIsolationArchitectureTest execute successfully across all execution working directories.

- Execute `ParadeigmaIsolationArchitectureTest` via Maven Surefire across reactor build contexts.
- Verify that `noProductionClassesShouldDependOnParadeigma`, `noProductionPomsShouldDeclareParadeigmaDependency`, `noProductionCodeShouldContainSimulationFlagsOrImports`, and `productionPackagingExcludesParadeigma` pass with 0 failures and 0 errors.