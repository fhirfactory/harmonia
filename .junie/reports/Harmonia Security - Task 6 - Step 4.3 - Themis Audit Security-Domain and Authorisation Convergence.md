Optional spending limit; leave empty for no limit: 50
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Inspect the current repository state for the Step 04.3 security-boundary work around FHIR AuditEvent authorization. Identify the exact files and line numbers for at least: ThemisClinicalAuthorizationFilter, DefaultThemisAuthorizer, DeterministicPolicyEvaluator, AuditReadPolicy, ClinicalAuthorizationPolicy, OperationsAuthorizationPolicy, SystemAdminPolicy, security-domain constants/vocabulary, role-to-authority mapping, FHIR HTTP method/action mapping, AuditEventResource, existing iris-befe authorization tests, existing Themis policy tests, and relevant architecture/security tests.

Confirm the current behavior for `/api/fhir/AuditEvent` and whether it is currently classified as CLINICAL via generic `/api/fhir/*` handling. Also note any equivalent AuditEvent routes exposed.

Please report concise findings with exact file paths and approximate line numbers so I can avoid re-searching later. Focus on the smallest likely change surface and any obvious architectural risk, especially around `system.admin` potentially authorizing mutation.



Exploration is Ready

**Step 04.3 targeted repository findings (read-only)**

No files were modified and no tests/applications were run.

**1. Current BEFE implementation and classification**

- **Filter:** `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java`
    - Class and annotations: lines **46–68** (`@Provider`, `@PreMatching`, `@Priority(AUTHORIZATION)`).
    - Generic path gate: lines **118–122** calls `isClinicalFhirPath(path)`.
    - Path parsing: lines **127–131** extracts the first segment as `resourceType` and the second as `resourceId`.
    - The generic path predicate at lines **239–246** accepts both `fhir/*` and `api/fhir/*`; it has no AuditEvent exception.
    - Target construction at lines **158–164** hard-codes `HarmoniaSecurityLabelEnum.CLINICAL` as both `securityDomain` and the sole security label.
    - Security context construction at lines **166–174** likewise hard-codes `CLINICAL`.
    - Authorization request and canonical delegation: lines **175–188** call `getAuthorizer().authorize(authRequest)`; `getAuthorizer()` falls back to `new DefaultThemisAuthorizer()` at lines **219–224**.

**Conclusion:** `/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}` currently enter the generic `/api/fhir/*` perimeter and are classified as **CLINICAL**, not AUDIT. The current filter does not inspect the resource type when choosing the domain.

With the default evaluator, this means:

- `audit.read` does not reach `AuditReadPolicy`, because the target is CLINICAL; it is denied by `ClinicalAuthorizationPolicy` for lacking clinical authorities.
- Clinical authorities can currently authorize the corresponding clinical READ/SEARCH/CREATE/UPDATE operations on AuditEvent.
- DELETE is denied by `ClinicalAuthorizationPolicy` for ordinary clinical authorities.
- `system.admin` is currently allowed for every action before domain policy evaluation, including AuditEvent mutation (see risk below).

**2. Exposed AuditEvent routes**

- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/JaxRsActivator.java`, lines **20–25**: `@ApplicationPath("/api")`.
- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java`, lines **33–37**: `@Path("/fhir/AuditEvent")`.
    - Collection GET/search: lines **42–50**.
    - Instance GET/read: lines **52–62**, `@Path("/{id}")`.
    - POST/create: lines **64–76**.
    - PUT/update: lines **78–90**, `@Path("/{id}")`.
    - DELETE: lines **92–100**, `@Path("/{id}")`.
- Therefore the BEFE routes are `/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}`. No second Iris BEFE JAX-RS AuditEvent route was found.
- Separate non-BEFE route/provider: `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/AuditEventResourceProvider.java`, lines **33–92**, a Spring/HAPI `IResourceProvider` with `@Create`, `@Read`, `@Update`, `@Delete`, and `@Search`. This is a distinct Hestia provider, not the Iris BEFE boundary.

**3. Canonical Themis chain**

- **BEFE authorizer:** `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/DefaultThemisAuthorizer.java`, lines **25–62**.
    - Constructor lines **47–49** uses `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - `authorize()` lines **55–61** delegates directly; no BEFE-local Audit policy exists.
- **Evaluator:** `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/evaluator/DeterministicPolicyEvaluator.java`.
    - Default policy registration lines **76–90** includes, in order, `SystemAdminPolicy`, provider policies, `AuditReadPolicy`, `ClinicalAuthorizationPolicy`, and `OperationsAuthorizationPolicy`.
    - Fail-closed validation lines **118–141** rejects null/malformed request, missing principal, null action, and missing target/resource type.
    - Explicit-deny pass and ordered allow/deny evaluation lines **143–183**.
- This is the intended canonical path: container `SecurityContext` → BEFE filter → `DefaultThemisAuthorizer` → default evaluator → policy set.

**4. Security-domain vocabulary and role mapping**

- **Domain/authority/role constants:** `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/constants/HarmoniaSecurityConstants.java`
    - Domain labels lines **37–43**, including `LABEL_AUDIT = "AUDIT"` and `LABEL_CLINICAL = "CLINICAL"`.
    - Authorities lines **45–67**, including `AUTH_AUDIT_READ = "audit.read"`; there is no `audit.write` constant.
    - Role codes lines **69–82**, including `ROLE_AUD_RDR`, `ROLE_SYS_ADM`, and clinical/operations/provider roles.
    - Canonical role objects lines **125–185**; `AUD_RDR` maps to `audit.read` at lines **169–173**, and `SYS_ADM` maps to `system.admin` at lines **181–185**.
    - Registry and lookup lines **187–215**.
- **Container role → Themis authority mapping:** `calliope/src/main/java/net/fhirfactory/harmonia/model/security/HarmoniaRoleEnum.java`
    - Enum mappings lines **31–81**. `AUD_RDR` maps to `HarmoniaAuthorityEnum.AUDIT_READ` at lines **74–75**; `SYS_ADM` maps to `SYSTEM_ADMIN` at lines **80–81**.
    - Conversion to Themis authorities lines **101–112**.
- **Vocabulary tests:** `calliope/src/test/java/net/fhirfactory/harmonia/model/security/HarmoniaSecurityVocabularyTest.java`, lines **70–120**, explicitly verifies `AUD_RDR → audit.read`, `SYS_INT → system.integration`, and `SYS_ADM → system.admin`.
- The filter obtains authorities only from trusted `SecurityContext.isUserInRole(...)` for every `HarmoniaRoleEnum` at `ThemisClinicalAuthorizationFilter.java`, lines **295–306**. It does not read principal attributes, JWT claims, or caller-provided authority headers.

**5. Existing policies**

- **AuditReadPolicy:** `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/policy/AuditReadPolicy.java`, lines **26–70**.
    - `DOMAIN = "AUDIT"` and `AUTH_AUDIT_READ = "audit.read"`: lines **29–34**.
    - Applies only when target domain/label is AUDIT and action is READ or SEARCH: lines **51–60**.
    - Allows `audit.read` or `system.admin`: lines **63–68**.
    - It has no CREATE/UPDATE/PATCH/DELETE allowance and no `audit.write` vocabulary.
- **ClinicalAuthorizationPolicy:** `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/policy/ClinicalAuthorizationPolicy.java`, lines **27–188**.
    - Domain matching: lines **58–65** and **98–107**.
    - READ/SEARCH/CREATE/UPDATE/ADMINISTER authority checks: lines **118–173**.
    - DELETE is explicitly denied: lines **174–179**.
- **OperationsAuthorizationPolicy:** `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/policy/OperationsAuthorizationPolicy.java`, lines **28–173**.
    - Domain/resource matching: lines **59–75**.
    - READ/SEARCH allows operations.read, operations.admin, or system.integration: lines **126–143**.
    - EXECUTE/ADMINISTER allows operations.admin: lines **144–157**; DELETE/default actions deny: lines **159–170**.
- **SystemAdminPolicy:** `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/policy/SystemAdminPolicy.java`, lines **24–64**.
    - Priority/order 10: lines **44–47**.
    - Applies to any request carrying `system.admin`, `system/*.*`, or `*`: lines **50–57**.
    - Unconditionally returns ALLOW without checking domain or action: lines **59–63**.

**6. HTTP method → Themis action mapping**

`ThemisClinicalAuthorizationFilter.mapMethodToAction()` at lines **226–237**:

| HTTP request | Themis action |
|---|---|
| GET collection (`resourceId == null`) | `SEARCH` |
| GET instance (`resourceId != null`) | `READ` |
| HEAD collection/instance | `SEARCH`/`READ` respectively |
| POST | `CREATE` |
| PUT | `UPDATE` |
| PATCH | `UPDATE` |
| DELETE | `DELETE` |
| unsupported/null method | `null` (fail closed by lines **136–142**) |

`ThemisAction` itself is defined at `themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisAction.java`, lines **20–36**; there is no PATCH action, so the existing repository convention is PATCH → UPDATE.

**7. 401/403 boundary**

- Missing/anonymous container principal is rejected directly by the filter at lines **144–153** with HTTP **401 Unauthorized** and a FHIR `OperationOutcome`.
- A denied Themis decision is mapped at lines **190–203**: `PRINCIPAL_MISSING` → **401**, all other denials → **403 Forbidden**.
- `abortWithOutcome()` at lines **322–344** emits FHIR JSON and uses security/forbidden issue codes.
- The filter’s class documentation at lines **53–63** states authentication is upstream WildFly/Elytron and caller-controlled identity/role headers are ignored. This is consistent with the existing mocked `SecurityContext` tests; those are application authorization-boundary tests, not WildFly OIDC/JWT integration tests.

**8. Existing BEFE authorization tests**

Primary file: `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/ThemisClinicalAuthorizationFilterTest.java`, lines **50–998**.

Relevant sections:

- Basic filter setup and application-boundary contract: lines **50–72**.
- Existing clinical SEARCH/READ/CREATE/UPDATE tests: approximately **218–336**.
- Action mapping test: lines **438–451**; verifies GET collection/instance, POST, PUT, DELETE, HEAD, PATCH→UPDATE, and invalid method→null.
- Header anti-spoofing: lines **409–436** and **842–894**.
- Cross-domain isolation currently tests that provider/audit/operations roles cannot access a **Clinical** Person target: lines **810–840**; it does not test AuditEvent as an AUDIT target.
- End-to-end evaluator-backed clinical outcomes: lines **582–894**.
- Unknown role denial: approximately **800–808**.
- SecurityContext propagation and missing-principal behavior: lines **896–967**.
- Spoofed headers do not alter trusted principal/context: lines **968–997**.

There are no AuditEvent-specific filter tests and no direct `AuditReadPolicyTest` file/results in the current test tree.

**9. Existing Themis policy tests**

- `themis/themis-core/src/test/java/net/fhirfactory/harmonia/themis/core/DeterministicPolicyEvaluatorTest.java`, lines **45–627**:
    - Policy/evaluator fixture and default evaluator around **45–90**.
    - Explicit deny precedence around **390–413**.
    - Operations policy tests around **489–583**.
    - `system.admin` is explicitly expected to allow an Operations READ via `SystemAdminPolicy` at **555–567**.
    - Unknown resource/domain/action default deny at **611–625**.
    - No AuditReadPolicy-specific coverage was found.
- `themis/themis-core/src/test/java/net/fhirfactory/harmonia/themis/core/policy/ClinicalAuthorizationPolicyTest.java`, lines **39–461**, covers clinical authorities and confirms clinical DELETE denial in role outcome tests around **430–459**.
- `themis/themis-core/src/test/java/net/fhirfactory/harmonia/themis/core/policy/OperationsAuthorizationPolicyTest.java`, lines **44–695**, covers Operations domain isolation, method/action outcomes, and role vocabulary.
- `themis/themis-core/src/test/java/net/fhirfactory/harmonia/themis/core/HarmoniaSecurityConstantsTest.java`, lines **27–96**, covers clinical/operations role registration and existing AUD_RDR/SYS_ADM entries.

**10. Relevant architecture/security tests**

- `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`, lines **65–979**.
    - Generic BEFE perimeter guardrail at lines **219–268** verifies that BEFE `*Resource.java` classes are routed under `/fhir/*` and that `ThemisClinicalAuthorizationFilter` exists with `@Provider`/`@PreMatching`.
    - It does not currently assert that `AuditEvent` is AUDIT, that BEFE delegates to `AuditReadPolicy`, or that system-admin AuditEvent mutation is denied.
    - The remainder covers general Themis/audit model/security-context guardrails, not this resource-specific classification.
- Other architecture files present in the repository include `ParadeigmaIsolationArchitectureTest`, `AgoraIsolationArchitectureTest`, `IrisDecouplingArchitectureTest`, `ProviderRegistryArchitectureTest`, `PetasosApiIsolationArchitectureTest`, and `PackageLayeringArchitectureTest`; no AuditEvent-specific assertion was found in the targeted searches.

**11. Smallest likely change surface and architectural risk**

The smallest classification change would be in `ThemisClinicalAuthorizationFilter` around lines **158–170**, where the resource type could select AUDIT for `AuditEvent` and CLINICAL for ordinary FHIR resources. Both the target and security-context domain/labels need to remain consistent; changing only one would produce policy/context divergence.

However, classification alone does **not** satisfy the requested fail-closed mutation invariant because `SystemAdminPolicy` is a priority-10 unconditional allow for all domains/actions. After changing AuditEvent to AUDIT, a principal with `system.admin` would still be authorized for CREATE/UPDATE/PATCH/DELETE before `AuditReadPolicy` is considered. This is a canonical Themis policy-architecture conflict, not a BEFE-local issue; it should be resolved explicitly in Themis (for example, via a canonical explicit-deny mechanism or a carefully scoped SystemAdmin semantic change) rather than by adding an Iris exception or broadening `AuditReadPolicy`.

The role/authority vocabulary otherwise already has the intended `AUD_RDR → audit.read` mapping and has no `audit.write` authority.

**Bottom line**

The requested invariant does **not** currently hold. At the Iris BEFE boundary, AuditEvent is presently CLINICAL; `audit.read` cannot use `AuditReadPolicy`, clinical authorities can reach several AuditEvent mutations, and `system.admin` can authorize every mapped mutation. The likely first code touchpoint is the domain construction in `ThemisClinicalAuthorizationFilter`, but the `SystemAdminPolicy` behavior must be addressed before it is truthful to claim that external AuditEvent CREATE/UPDATE/PATCH/DELETE fail closed through canonical Themis.

Step 04.4+ work was not performed: `AuditEventResource` persistence/cache behavior, Hestia provider behavior, UI, database placement, and immutability/database triggers were not changed.



ANALYSIS

**Task description**  
Assess whether Step 04.3 can be truthfully completed within scope given the discovered `system.admin` conflict, or whether the correct outcome is to stop and report an architectural blocker. Evaluate minimal canonical-Themis options only (not BEFE-local exceptions) to make external FHIR AuditEvent mutation fail closed while preserving the requested authorization chain and avoiding casual weakening of `SystemAdminPolicy`.

Please compare these options:
1. Stop now and report blocker because `SystemAdminPolicy` currently defeats immutable-audit mutation denial.
2. Add a canonical explicit-deny policy for AUDIT-domain external mutation actions ahead of `SystemAdminPolicy`.
3. Narrow `SystemAdminPolicy` semantics for AUDIT mutation.
4. Any other clean canonical approach you find in the current architecture.

For each option, judge scope fit against the issue text, semantic risk, test impact, and whether the final invariant could be stated truthfully. Conclude with your recommendation as final-arbiter input.



**Architectural Assessment: SystemAdmin Conflict and Audit Immutability in Step 04.3**

**Executive Summary**

Step 04.3 **cannot** be truthfully completed by merely altering the URI/domain mapping in `ThemisClinicalAuthorizationFilter`, because `SystemAdminPolicy` (order 10, unconditional allow for `system.admin`, `system/*.*`, `*`) short-circuits evaluation before domain-specific policies are checked.

However, rather than being an impasse requiring an architectural halt, the canonical Themis framework **already possesses first-class explicit-deny mechanics** (`ThemisPolicy.isExplicitDeny()` evaluated in Step 2 of `DeterministicPolicyEvaluator` prior to Step 3 ordered allows).

A comparative utility and risk evaluation shows that implementing a canonical explicit-deny policy in `themis-core` maximizes net system utility by enforcing non-repudiation invariants without degrading existing administrative capabilities across other domains.

---

**Comparative Evaluation of Options**

**Option 1: Stop and Report an Architectural Blocker**
- **Description**: Halt Step 04.3 immediately, mark the step blocked by `SystemAdminPolicy`'s global-allow semantics, and defer any code changes until a wider Themis policy architecture review is completed.
- **Scope Fit**: High fidelity to the conservative "STOP on conflict" prompt instruction, avoiding any out-of-spec scope creep.
- **Semantic Risk**: Zero immediate code regression risk, but leaves the BEFE attack surface wide open (AuditEvent remains classified as `CLINICAL`, allowing clinical users and admins to mutate audit records).
- **Test Impact**: Zero test impact; pipeline remains as-is.
- **Truthful Invariant Assessment**: **False**. The required invariant cannot be stated.
- **Net Utility**: Low. It stalls the overall migration pipeline (blocking Step 04.4 and downstream Kleio integration) without offering technical discovery beyond what is already verified.

---

**Option 2: Introduce Canonical Explicit-Deny Policy for External AUDIT Mutations (Recommended)**
- **Description**: Add a dedicated `AuditImmutabilityDenyPolicy` (or `AuditMutationDenyPolicy`) in `themis-core` registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`. This policy implements `ThemisPolicy.isExplicitDeny() == true` and returns `DENY` (`MUTATION_NOT_PERMITTED` or `ACTION_NOT_PERMITTED`) for any request where `target.securityDomain == "AUDIT"` (or `LABEL_AUDIT`) and `action` is in `{CREATE, UPDATE, DELETE}` (or not in `{READ, SEARCH}`).
- **Scope Fit**: High. Uses the pre-existing, native explicit-deny execution path in `DeterministicPolicyEvaluator` (lines 143–152) designed specifically to override high-priority allows like `SystemAdminPolicy`. Keeps logic inside `themis-core` rather than creating ad-hoc BEFE hacks.
- **Semantic Risk**: Low and localized. It establishes the healthcare compliance invariant: audit logs are append-only via trusted internal pipelines (Kleio) and immutable from external endpoints, even against superuser credentials. It leaves `SystemAdminPolicy` intact for all non-AUDIT domains.
- **Test Impact**: Requires adding unit tests in `themis-core` (`DeterministicPolicyEvaluatorTest`, new policy test) and verifying that `ThemisClinicalAuthorizationFilterTest` confirms `system.admin` cannot mutate AuditEvents. Zero regression on existing clinical/operations admin tests.
- **Truthful Invariant Assessment**: **True**. External READ/SEARCH require valid audit read permissions, while all external mutation actions fail closed regardless of role.
- **Net Utility**: Highest. Resolves the security contradiction cleanly within existing engine abstractions, unblocks Step 04.4, and guarantees verifiable non-repudiation.

---

**Option 3: Narrow `SystemAdminPolicy` Semantics Directly**
- **Description**: Modify `SystemAdminPolicy.appliesTo()` or `SystemAdminPolicy.evaluate()` to exclude or deny requests targeting the `AUDIT` security domain when the action is mutating (or delegate domain handling).
- **Scope Fit**: Moderate. Directly touches `SystemAdminPolicy`, which the step guidelines caution against casually modifying.
- **Semantic Risk**: Medium. `SystemAdminPolicy` is conceived as an ordered allow policy (`order = 10`), not a negative enforcement policy. Mixing domain exclusions into a general role policy muddies role-based authorization with attribute/domain filtering, increasing maintenance friction and the risk of unintended bypasses if other policies are added.
- **Test Impact**: Requires modifying existing `SystemAdminPolicyTest` and `DeterministicPolicyEvaluatorTest`.
- **Truthful Invariant Assessment**: **True**, provided the exclusion is comprehensive across wildcards.
- **Net Utility**: Moderate. Achieves the functional outcome but violates separation of concerns (role evaluation vs. domain immutability governance).

---

**Option 4: Narrow `SystemAdminPolicy` Scope to Non-AUDIT Domains and Rely on Default Deny**
- **Description**: Make `SystemAdminPolicy.appliesTo(request)` return `false` whenever `request.target().securityDomain()` is `AUDIT` (or make `AuditReadPolicy` explicitly govern admins for reads).
- **Scope Fit**: Moderate.
- **Semantic Risk**: Medium. If `SystemAdminPolicy` merely ignores AUDIT, then an admin caller performing a `CREATE` on an `AUDIT` resource will fall through the policy list and hit the default-deny at the end of the evaluator. While functionally denying the mutation, it produces a `DEFAULT_DENY` reason rather than an explicit immutability violation reason, and risks allowing mutations if another downstream policy accidentally matches.
- **Test Impact**: Low-to-moderate.
- **Truthful Invariant Assessment**: **True**, but fragile compared to explicit deny.
- **Net Utility**: Lower than Option 2 due to reliance on fall-through behavior rather than active invariant enforcement.

---

**Trade-Off and Decision Matrix**

| Dimension | Option 1 (Stop/Blocker) | Option 2 (Explicit-Deny Policy) | Option 3 (Narrow SystemAdmin) | Option 4 (Audit Exclusion Fall-through) |
| :--- | :--- | :--- | :--- | :--- |
| **Architectural Purity** | High (Defers) | **High (Uses existing `isExplicitDeny` engine)** | Medium (Mixes concerns) | Medium (Fragile fall-through) |
| **Security / Non-Repudiation** | Nil (Remains vulnerable) | **Maximum (Guaranteed override of all allows)** | High | Medium |
| **Blast Radius / Risk** | None | **Zero outside AUDIT mutations** | Low-Medium | Low |
| **Fulfills Step 04.3 Invariant** | No | **Yes** | **Yes** | **Yes** |
| **Pipeline Velocity / Net Utility**| Blocks progress | **Unblocks Step 04.4 cleanly** | Unblocks | Unblocks |

---

**Final Arbiter Recommendation**

1. **Do not stop at a blocker** — the repository is not missing architectural primitives. `DeterministicPolicyEvaluator` lines 143–152 were explicitly authored to support `ThemisPolicy.isExplicitDeny()` for domain-level constraints that supersede administrative roles.
2. **Execute Option 2**:
    - Classify `/api/fhir/AuditEvent` as `AUDIT` in `ThemisClinicalAuthorizationFilter`.
    - Add `AuditImmutabilityDenyPolicy` (explicit deny for mutating actions on `AUDIT` targets) to `themis-core` registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Verify that `AuditReadPolicy` continues to govern `READ` and `SEARCH` for authorised readers (and admins if read access is granted).
    - This delivers a completely fail-closed, truthful implementation of the Step 04.3 security invariant with minimal diff surface and zero architectural pollution.

Analysis is Ready

**Architectural Assessment: SystemAdmin Conflict and Audit Immutability in Step 04.3**

**Executive Summary**

Step 04.3 **cannot** be truthfully completed by merely altering the URI/domain mapping in `ThemisClinicalAuthorizationFilter`, because `SystemAdminPolicy` (order 10, unconditional allow for `system.admin`, `system/*.*`, `*`) short-circuits evaluation before domain-specific policies are checked.

However, rather than being an impasse requiring an architectural halt, the canonical Themis framework **already possesses first-class explicit-deny mechanics** (`ThemisPolicy.isExplicitDeny()` evaluated in Step 2 of `DeterministicPolicyEvaluator` prior to Step 3 ordered allows).

A comparative utility and risk evaluation shows that implementing a canonical explicit-deny policy in `themis-core` maximizes net system utility by enforcing non-repudiation invariants without degrading existing administrative capabilities across other domains.

---

**Comparative Evaluation of Options**

**Option 1: Stop and Report an Architectural Blocker**
- **Description**: Halt Step 04.3 immediately, mark the step blocked by `SystemAdminPolicy`'s global-allow semantics, and defer any code changes until a wider Themis policy architecture review is completed.
- **Scope Fit**: High fidelity to the conservative "STOP on conflict" prompt instruction, avoiding any out-of-spec scope creep.
- **Semantic Risk**: Zero immediate code regression risk, but leaves the BEFE attack surface wide open (AuditEvent remains classified as `CLINICAL`, allowing clinical users and admins to mutate audit records).
- **Test Impact**: Zero test impact; pipeline remains as-is.
- **Truthful Invariant Assessment**: **False**. The required invariant cannot be stated.
- **Net Utility**: Low. It stalls the overall migration pipeline (blocking Step 04.4 and downstream Kleio integration) without offering technical discovery beyond what is already verified.

---

**Option 2: Introduce Canonical Explicit-Deny Policy for External AUDIT Mutations (Recommended)**
- **Description**: Add a dedicated `AuditImmutabilityDenyPolicy` (or `AuditMutationDenyPolicy`) in `themis-core` registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`. This policy implements `ThemisPolicy.isExplicitDeny() == true` and returns `DENY` (`MUTATION_NOT_PERMITTED` or `ACTION_NOT_PERMITTED`) for any request where `target.securityDomain == "AUDIT"` (or `LABEL_AUDIT`) and `action` is in `{CREATE, UPDATE, DELETE}` (or not in `{READ, SEARCH}`).
- **Scope Fit**: High. Uses the pre-existing, native explicit-deny execution path in `DeterministicPolicyEvaluator` (lines 143–152) designed specifically to override high-priority allows like `SystemAdminPolicy`. Keeps logic inside `themis-core` rather than creating ad-hoc BEFE hacks.
- **Semantic Risk**: Low and localized. It establishes the healthcare compliance invariant: audit logs are append-only via trusted internal pipelines (Kleio) and immutable from external endpoints, even against superuser credentials. It leaves `SystemAdminPolicy` intact for all non-AUDIT domains.
- **Test Impact**: Requires adding unit tests in `themis-core` (`DeterministicPolicyEvaluatorTest`, new policy test) and verifying that `ThemisClinicalAuthorizationFilterTest` confirms `system.admin` cannot mutate AuditEvents. Zero regression on existing clinical/operations admin tests.
- **Truthful Invariant Assessment**: **True**. External READ/SEARCH require valid audit read permissions, while all external mutation actions fail closed regardless of role.
- **Net Utility**: Highest. Resolves the security contradiction cleanly within existing engine abstractions, unblocks Step 04.4, and guarantees verifiable non-repudiation.

---

**Option 3: Narrow `SystemAdminPolicy` Semantics Directly**
- **Description**: Modify `SystemAdminPolicy.appliesTo()` or `SystemAdminPolicy.evaluate()` to exclude or deny requests targeting the `AUDIT` security domain when the action is mutating (or delegate domain handling).
- **Scope Fit**: Moderate. Directly touches `SystemAdminPolicy`, which the step guidelines caution against casually modifying.
- **Semantic Risk**: Medium. `SystemAdminPolicy` is conceived as an ordered allow policy (`order = 10`), not a negative enforcement policy. Mixing domain exclusions into a general role policy muddies role-based authorization with attribute/domain filtering, increasing maintenance friction and the risk of unintended bypasses if other policies are added.
- **Test Impact**: Requires modifying existing `SystemAdminPolicyTest` and `DeterministicPolicyEvaluatorTest`.
- **Truthful Invariant Assessment**: **True**, provided the exclusion is comprehensive across wildcards.
- **Net Utility**: Moderate. Achieves the functional outcome but violates separation of concerns (role evaluation vs. domain immutability governance).

---

**Option 4: Narrow `SystemAdminPolicy` Scope to Non-AUDIT Domains and Rely on Default Deny**
- **Description**: Make `SystemAdminPolicy.appliesTo(request)` return `false` whenever `request.target().securityDomain()` is `AUDIT` (or make `AuditReadPolicy` explicitly govern admins for reads).
- **Scope Fit**: Moderate.
- **Semantic Risk**: Medium. If `SystemAdminPolicy` merely ignores AUDIT, then an admin caller performing a `CREATE` on an `AUDIT` resource will fall through the policy list and hit the default-deny at the end of the evaluator. While functionally denying the mutation, it produces a `DEFAULT_DENY` reason rather than an explicit immutability violation reason, and risks allowing mutations if another downstream policy accidentally matches.
- **Test Impact**: Low-to-moderate.
- **Truthful Invariant Assessment**: **True**, but fragile compared to explicit deny.
- **Net Utility**: Lower than Option 2 due to reliance on fall-through behavior rather than active invariant enforcement.

---

**Trade-Off and Decision Matrix**

| Dimension | Option 1 (Stop/Blocker) | Option 2 (Explicit-Deny Policy) | Option 3 (Narrow SystemAdmin) | Option 4 (Audit Exclusion Fall-through) |
| :--- | :--- | :--- | :--- | :--- |
| **Architectural Purity** | High (Defers) | **High (Uses existing `isExplicitDeny` engine)** | Medium (Mixes concerns) | Medium (Fragile fall-through) |
| **Security / Non-Repudiation** | Nil (Remains vulnerable) | **Maximum (Guaranteed override of all allows)** | High | Medium |
| **Blast Radius / Risk** | None | **Zero outside AUDIT mutations** | Low-Medium | Low |
| **Fulfills Step 04.3 Invariant** | No | **Yes** | **Yes** | **Yes** |
| **Pipeline Velocity / Net Utility**| Blocks progress | **Unblocks Step 04.4 cleanly** | Unblocks | Unblocks |

---

**Final Arbiter Recommendation**

1. **Do not stop at a blocker** — the repository is not missing architectural primitives. `DeterministicPolicyEvaluator` lines 143–152 were explicitly authored to support `ThemisPolicy.isExplicitDeny()` for domain-level constraints that supersede administrative roles.
2. **Execute Option 2**:
    - Classify `/api/fhir/AuditEvent` as `AUDIT` in `ThemisClinicalAuthorizationFilter`.
    - Add `AuditImmutabilityDenyPolicy` (explicit deny for mutating actions on `AUDIT` targets) to `themis-core` registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Verify that `AuditReadPolicy` continues to govern `READ` and `SEARCH` for authorised readers (and admins if read access is granted).
    - This delivers a completely fail-closed, truthful implementation of the Step 04.3 security invariant with minimal diff surface and zero architectural pollution.



PLAN THE TASK

**Task description**  
Plan the whole task.

**Feedback**  
Use the repository findings already established: current BEFE AuditEvent requests are classified as CLINICAL in `ThemisClinicalAuthorizationFilter`; canonical Themis already has `AuditReadPolicy` for AUDIT READ/SEARCH and an explicit-deny mechanism in `DeterministicPolicyEvaluator`; `SystemAdminPolicy` currently unconditionally allows everything and therefore needs a canonical solution, not a BEFE-local exception.  
Prioritize the smallest clean change set that can truthfully satisfy the requested invariant and completion report. Include focused verification and proportional guardrails. Avoid Step 04.4+ changes.

**Constraints**  
No git operations. Plan only Step 04.3 scope.



**Requirements**

**Goal / Outcome**  
Converge the Iris BEFE security boundary for FHIR `AuditEvent` so that `AuditEvent` endpoints (`/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}`) are governed under the `AUDIT` security domain instead of `CLINICAL`. Authorize `READ` and `SEARCH` for callers holding `audit.read` (or `system.admin` read privileges) via canonical `AuditReadPolicy`, while ensuring all external mutation actions (`CREATE`, `UPDATE`, `PATCH`, `DELETE`) fail closed through canonical Themis explicit-deny evaluation.

**Scope**
- **In Scope:**
    - Resource security-domain classification in `ThemisClinicalAuthorizationFilter` (route `AuditEvent` to `AUDIT` domain/label while keeping other FHIR resources as `CLINICAL`).
    - Canonical explicit-deny policy `AuditImmutabilityDenyPolicy` in `themis-core` registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Fail-closed external mutation denial for `AuditEvent` across all roles (including `system.admin`, clinical, operations, provider registry).
    - Authority and cross-domain isolation verification (`audit.read` permits READ/SEARCH; clinical/ops/unknown denied; unauthenticated returns 401).
    - Unit, boundary, and architectural tests in `themis-core`, `iris-befe`, and `paradeigma-test`.
- **Out of Scope:**
    - Step 04.4 `AuditEventResource` refactoring, method removal, or Kleio delegation.
    - Modifying `AuditService`, `DurableAuditService`, or Kleio persistence layer.
    - Step 04.5 Mnemosyne backend JPA/Infinispan immutability triggers.
    - Step 04.6 Iris UI modifications.
    - Introducing any new `audit.write` role or authority.

**Done When**
- [ ] `/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}` requests construct `ThemisResource` and `ThemisSecurityContext` with domain `AUDIT` and security label `AUDIT`.
- [ ] `ThemisPolicy` explicit-deny policy `AuditImmutabilityDenyPolicy` denies any mutating action (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] Principal with `audit.read` is allowed `READ` and `SEARCH` on `AuditEvent`, but denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` with HTTP 403.
- [ ] Principal with `system.admin` is denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` on `AuditEvent` via canonical explicit-deny precedence.
- [ ] Clinical-only, operations-only, and provider-registry authorities are denied all access (`READ`, `SEARCH`, `CREATE`, `UPDATE`, `DELETE`) to `AuditEvent`.
- [ ] Unauthenticated requests preserve HTTP 401 `OperationOutcome` boundary.
- [ ] Existing clinical FHIR resources (`Patient`, `Observation`, etc.) retain `CLINICAL` domain classification and behavior without regression.

---

**Technical Design**

**Decisions**
- **Chose canonical explicit-deny `AuditImmutabilityDenyPolicy` in `themis-core` / not ad-hoc BEFE filter checks or narrowing `SystemAdminPolicy` directly**: `DeterministicPolicyEvaluator` already implements explicit-deny evaluation (Step 2 before Step 3 ordered allow policies). An explicit-deny policy cleanly enforces non-repudiation invariants for `AUDIT` targets across all roles without mutating role semantics in `SystemAdminPolicy` or bypassing canonical Themis in Iris BEFE.
- **Chose selective domain resolution in `ThemisClinicalAuthorizationFilter` / not separate JAX-RS filter or URI rework**: The filter already parses `resourceType` from the URI subpath; mapping `AuditEvent` (case-insensitive) to `HarmoniaSecurityLabelEnum.AUDIT` and other FHIR resources to `HarmoniaSecurityLabelEnum.CLINICAL` establishes domain isolation with minimal diff surface.

**Approach & Touches**
- **Themis Core (`themis/themis-core`)**:
    - Add `AuditImmutabilityDenyPolicy.java` in `net.fhirfactory.harmonia.themis.core.policy`:
        - Implements `ThemisPolicy` with `isExplicitDeny() == true`, `order = 10`, `domain = "AUDIT"`.
        - `appliesTo(request)`: returns true when `target.securityDomain == "AUDIT"` (or has label `AUDIT`) AND `action` is one of `CREATE`, `UPDATE`, `DELETE` (or not `READ`/`SEARCH`).
        - `evaluate(request)`: returns `ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, POLICY_ID, corrId, "External mutation of AUDIT records is strictly prohibited")`.
    - Register `new AuditImmutabilityDenyPolicy()` in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Reference analog: `AuditReadPolicy.java` for domain matching and `ClinicalAuthorizationPolicy.java` for explicit DELETE denial pattern.
- **Iris BEFE (`iris/iris-befe`)**:
    - Update `ThemisClinicalAuthorizationFilter.java`:
        - In `filter()` around line 158–174, determine target domain and security label based on `resourceType`: if `"AuditEvent".equalsIgnoreCase(resourceType)`, use `HarmoniaSecurityLabelEnum.AUDIT`, else `HarmoniaSecurityLabelEnum.CLINICAL`.
        - Apply the resolved label/domain consistently to both `ThemisResource` and `ThemisSecurityContext`.
        - Retain HTTP method-to-action mapping (`GET` id == null -> `SEARCH`, `GET` id != null -> `READ`, `POST` -> `CREATE`, `PUT`/`PATCH` -> `UPDATE`, `DELETE` -> `DELETE`).
- **Architecture & Security Guardrails (`paradeigma/paradeigma-test`)**:
    - Update `SecurityEnforcementArchitectureTest.java` to verify that `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator` and that BEFE relies on canonical Themis without local audit bypasses.

**Nuances, Risks & Corner Cases**
- **PATCH Mapping Convention**: Themis action model has no `PATCH` action; filter maps `PATCH` -> `ThemisAction.UPDATE`. Immutability policy checks `UPDATE`, correctly denying both PUT and PATCH.
- **AuditReadPolicy Order**: `AuditReadPolicy` has order 100. Explicit-deny policies evaluate before Step 3 ordered policies, guaranteeing mutations are rejected before `SystemAdminPolicy` (order 10) or any allow policy is considered.
- **Resource Code Immutability**: `AuditEventResource.java` retains its existing endpoints during Step 04.3. No methods are deleted or refactored in this step; security is enforced strictly at the authorization filter boundary.

**Contracts**
```java
// Policy Contract
public class AuditImmutabilityDenyPolicy implements ThemisPolicy {
    public static final String POLICY_ID = "audit-immutability-deny-policy";
    public static final String DOMAIN = "AUDIT";

    @Override
    public String getPolicyId() { return POLICY_ID; }
    @Override
    public int getOrder() { return 10; }
    @Override
    public boolean isExplicitDeny() { return true; }
    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request);
    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request);
}
```

---

**Testing**

**Checklist**
- **Audit Read/Search Authorization**: Authenticated principal with `audit.read` (or `AUD_RDR` role) successfully executes `GET /api/fhir/AuditEvent` (SEARCH) and `GET /api/fhir/AuditEvent/123` (READ) -> 200/green.
- **Audit Mutation Denial**: Authenticated principal with `audit.read` receives 403 Forbidden on `POST`, `PUT`, `PATCH`, `DELETE` to `/api/fhir/AuditEvent` -> `OperationOutcome` with `ACTION_NOT_PERMITTED`.
- **System Admin Audit Immutability**: Principal with `system.admin` / `SYS_ADM` role is allowed `READ`/`SEARCH` but denied `POST`, `PUT`, `PATCH`, `DELETE` on `AuditEvent` -> 403 Forbidden.
- **Cross-Domain Isolation**: Principal with Clinical (`CLINICAL_WRITE`, `CLINICAL_ADMIN`), Operations (`OPS_ADM`, `OPS_VIEWER`), or Provider Registry roles is denied all actions on `AuditEvent` -> 403 Forbidden.
- **Clinical Resource Isolation**: Standard clinical resources (`Patient`, `Observation`) retain `CLINICAL` domain classification and allow clinical read/write roles as before.
- **Unauthenticated / Missing Principal**: Anonymous or missing principal returns 401 Unauthorized (`PRINCIPAL_MISSING`).
- **Anti-Spoofing & Header Isolation**: Injected `X-Harmonia-*` or caller-controlled headers do not alter SecurityContext identity or domain classification.

---

**Delivery Steps**

**Step 1: Canonical Themis Audit Immutability Explicit-Deny Policy**  
Goal: Enforce fail-closed immutability for the `AUDIT` security domain within `themis-core` using the existing explicit-deny engine.  
Scope: `themis/themis-core`  
Acceptance Criteria:
- [ ] `AuditImmutabilityDenyPolicy` is implemented in `net.fhirfactory.harmonia.themis.core.policy` with `isExplicitDeny() == true` matching mutating actions (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
- [ ] Unit tests in `AuditImmutabilityDenyPolicyTest` verify explicit deny on mutating actions for any caller role (including `system.admin`).
- [ ] Evaluator tests in `DeterministicPolicyEvaluatorTest` confirm explicit-deny precedence over `SystemAdminPolicy` for AUDIT mutation actions while keeping AUDIT READ/SEARCH allowed.
  Verification: `mvn test -pl themis/themis-core -Dtest="AuditImmutabilityDenyPolicyTest,DeterministicPolicyEvaluatorTest,ClinicalAuthorizationPolicyTest,OperationsAuthorizationPolicyTest"` → green

**Step 2: Iris BEFE AuditEvent Domain Convergence and Guardrails**  
Goal: Route BEFE `AuditEvent` requests into the `AUDIT` security domain, establish boundary tests, and enforce architecture guardrails.  
Scope: `iris/iris-befe`, `paradeigma/paradeigma-test`  
Acceptance Criteria:
- [ ] `ThemisClinicalAuthorizationFilter` dynamically classifies `AuditEvent` requests as `HarmoniaSecurityLabelEnum.AUDIT` and non-AuditEvent FHIR requests as `HarmoniaSecurityLabelEnum.CLINICAL` for both `ThemisResource` and `ThemisSecurityContext`.
- [ ] `ThemisClinicalAuthorizationFilterTest` validates all 15 required boundary scenarios (AuditEvent AUDIT classification, `audit.read` READ/SEARCH allow, mutation deny for all roles including `system.admin`, cross-domain isolation, anti-spoofing, and clinical regression prevention).
- [ ] `SecurityEnforcementArchitectureTest` asserts canonical Themis governance of `AuditEvent`, presence of `AuditImmutabilityDenyPolicy`, and absence of ad-hoc Iris audit authorization bypasses.
  Verification: `mvn test -pl iris/iris-befe,paradeigma/paradeigma-test -Dtest="ThemisClinicalAuthorizationFilterTest,SecurityEnforcementArchitectureTest"` → green



REVIEW THE PLAN

**Task description**  
Review the current delivery plan.

**Feedback**  
Focus on whether the plan is the smallest clean path for Step 04.3, especially the canonical handling of the discovered `system.admin` conflict, coverage of the requested verification/reporting points, and whether any missing step is needed for Themis policy tests vs BEFE boundary tests vs proportional architecture guardrails.

**Constraints**  
Keep review scoped to Step 04.3 only; no Step 04.4+ work.



Plan Approved

**Verification**
- **Explicit-deny engine (core mechanism)**: Confirmed `DeterministicPolicyEvaluator.authorize()` evaluates explicit-deny policies in Step 2 (lines 143–152) *before* ordered allow policies in Step 3 (lines 154–170). This guarantees an `isExplicitDeny()==true` policy overrides `SystemAdminPolicy`'s unconditional allow — the plan's foundational assumption is correct.
- **SystemAdminPolicy conflict**: Confirmed `SystemAdminPolicy` (order 10) unconditionally returns ALLOW for `system.admin`/`system/*.*`/`*` with no domain/action check (lines 50–63). The plan's canonical explicit-deny approach (not narrowing SystemAdmin, not a BEFE-local hack) is the correct, minimal fix.
- **`ThemisPolicy` contract**: `isExplicitDeny()` and `getOrder()` are `default` methods; `getPolicyId()`, `getDescription()`, `appliesTo()`, `evaluate()` are mandatory. Plan's policy contract is implementable.
- **Decision factory**: `ThemisAuthorizationDecision.deny(reason, policyId, corrId, message)` signature matches the plan's usage exactly; `ThemisDecisionReason.ACTION_NOT_PERMITTED` exists.
- **AuditReadPolicy**: Confirmed AUDIT domain, allows READ/SEARCH for `audit.read` or `system.admin` (lines 51–68) — plan correctly leaves it untouched for the read path.
- **Filter construction**: Confirmed `ThemisClinicalAuthorizationFilter` hard-codes `HarmoniaSecurityLabelEnum.CLINICAL` for both `ThemisResource.securityDomain/securityLabels` (lines 159–164) and `ThemisSecurityContext.securityDomain` (line 170); `resourceType` is already parsed (line 130). The plan's targeted domain-resolution change at 158–174 is accurate and minimal.
- **Vocabulary**: `HarmoniaSecurityLabelEnum` has both `AUDIT` and `CLINICAL` with `getCode()` and `toThemisLabel()` — plan's API usage is valid.
- **PATCH mapping**: Prior research + filter confirm PATCH→UPDATE, so the deny policy matching UPDATE covers PATCH. No new action vocabulary needed.
- **Guardrail feasibility**: `SecurityEnforcementArchitectureTest` already imports `DeterministicPolicyEvaluator`; `getRegisteredPolicies()` is public, so asserting `AuditImmutabilityDenyPolicy` registration is achievable.
- **Per-step Verification**: Both steps use real `mvn -pl` module commands targeting concrete test classes (including ones the step adds) — valid post-step done-signals.

**Issues**  
None blocking.

**Notes**
- The plan's illustrative `AuditImmutabilityDenyPolicy` contract snippet omits `getDescription()`, which is a mandatory (non-default) interface method. Trivial for the executor to add; not a defect in the plan's approach.
- No legitimate flow is broken by the new explicit-deny: nothing currently authorizes AUDIT-domain CREATE/UPDATE/DELETE except `SystemAdminPolicy`, and internal Kleio APPEND does not traverse this evaluator with an AUDIT/CREATE request — so the only behavioral change is denying `system.admin` external AUDIT mutation, which is the intended invariant. The plan correctly schedules updates to `DeterministicPolicyEvaluatorTest`.
- The parent task's completion-report enumeration (section 14) and the explicit invariant statement are execution-time deliverables; the plan's "Done When" captures the invariant substance, so this is acceptable for a lean brief.

**Discoveries for future steps**
- Explicit-deny order value is irrelevant for override correctness: all `isExplicitDeny()` policies are scanned in a single loop (Step 2) before any allow policy regardless of `getOrder()`.
- `SecurityEnforcementArchitectureTest` is a large (979-line) mixed ArchUnit + reflection/registration test in `paradeigma/paradeigma-test`; it already instantiates Themis core classes, so registration assertions fit its existing style.



**Requirements**

**Goal / Outcome**  
Converge the Iris BEFE security boundary for FHIR `AuditEvent` so that `AuditEvent` endpoints (`/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}`) are governed under the `AUDIT` security domain instead of `CLINICAL`. Authorize `READ` and `SEARCH` for callers holding `audit.read` (or `system.admin` read privileges) via canonical `AuditReadPolicy`, while ensuring all external mutation actions (`CREATE`, `UPDATE`, `PATCH`, `DELETE`) fail closed through canonical Themis explicit-deny evaluation.

**Scope**
- **In Scope:**
    - Resource security-domain classification in `ThemisClinicalAuthorizationFilter` (route `AuditEvent` to `AUDIT` domain/label while keeping other FHIR resources as `CLINICAL`).
    - Canonical explicit-deny policy `AuditImmutabilityDenyPolicy` in `themis-core` registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Fail-closed external mutation denial for `AuditEvent` across all roles (including `system.admin`, clinical, operations, provider registry).
    - Authority and cross-domain isolation verification (`audit.read` permits READ/SEARCH; clinical/ops/unknown denied; unauthenticated returns 401).
    - Unit, boundary, and architectural tests in `themis-core`, `iris-befe`, and `paradeigma-test`.
- **Out of Scope:**
    - Step 04.4 `AuditEventResource` refactoring, method removal, or Kleio delegation.
    - Modifying `AuditService`, `DurableAuditService`, or Kleio persistence layer.
    - Step 04.5 Mnemosyne backend JPA/Infinispan immutability triggers.
    - Step 04.6 Iris UI modifications.
    - Introducing any new `audit.write` role or authority.

**Done When**
- [ ] `/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}` requests construct `ThemisResource` and `ThemisSecurityContext` with domain `AUDIT` and security label `AUDIT`.
- [ ] `ThemisPolicy` explicit-deny policy `AuditImmutabilityDenyPolicy` denies any mutating action (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] Principal with `audit.read` is allowed `READ` and `SEARCH` on `AuditEvent`, but denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` with HTTP 403.
- [ ] Principal with `system.admin` is denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` on `AuditEvent` via canonical explicit-deny precedence.
- [ ] Clinical-only, operations-only, and provider-registry authorities are denied all access (`READ`, `SEARCH`, `CREATE`, `UPDATE`, `DELETE`) to `AuditEvent`.
- [ ] Unauthenticated requests preserve HTTP 401 `OperationOutcome` boundary.
- [ ] Existing clinical FHIR resources (`Patient`, `Observation`, etc.) retain `CLINICAL` domain classification and behavior without regression.

---

**Technical Design**

**Decisions**
- **Chose canonical explicit-deny `AuditImmutabilityDenyPolicy` in `themis-core` / not ad-hoc BEFE filter checks or narrowing `SystemAdminPolicy` directly**: `DeterministicPolicyEvaluator` already implements explicit-deny evaluation (Step 2 before Step 3 ordered allow policies). An explicit-deny policy cleanly enforces non-repudiation invariants for `AUDIT` targets across all roles without mutating role semantics in `SystemAdminPolicy` or bypassing canonical Themis in Iris BEFE.
- **Chose selective domain resolution in `ThemisClinicalAuthorizationFilter` / not separate JAX-RS filter or URI rework**: The filter already parses `resourceType` from the URI subpath; mapping `AuditEvent` (case-insensitive) to `HarmoniaSecurityLabelEnum.AUDIT` and other FHIR resources to `HarmoniaSecurityLabelEnum.CLINICAL` establishes domain isolation with minimal diff surface.

**Approach & Touches**
- **Themis Core (`themis/themis-core`)**:
    - Add `AuditImmutabilityDenyPolicy.java` in `net.fhirfactory.harmonia.themis.core.policy`:
        - Implements `ThemisPolicy` with `isExplicitDeny() == true`, `order = 10`, `domain = "AUDIT"`.
        - `appliesTo(request)`: returns true when `target.securityDomain == "AUDIT"` (or has label `AUDIT`) AND `action` is one of `CREATE`, `UPDATE`, `DELETE` (or not `READ`/`SEARCH`).
        - `evaluate(request)`: returns `ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, POLICY_ID, corrId, "External mutation of AUDIT records is strictly prohibited")`.
    - Register `new AuditImmutabilityDenyPolicy()` in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Reference analog: `AuditReadPolicy.java` for domain matching and `ClinicalAuthorizationPolicy.java` for explicit DELETE denial pattern.
- **Iris BEFE (`iris/iris-befe`)**:
    - Update `ThemisClinicalAuthorizationFilter.java`:
        - In `filter()` around line 158–174, determine target domain and security label based on `resourceType`: if `"AuditEvent".equalsIgnoreCase(resourceType)`, use `HarmoniaSecurityLabelEnum.AUDIT`, else `HarmoniaSecurityLabelEnum.CLINICAL`.
        - Apply the resolved label/domain consistently to both `ThemisResource` and `ThemisSecurityContext`.
        - Retain HTTP method-to-action mapping (`GET` id == null -> `SEARCH`, `GET` id != null -> `READ`, `POST` -> `CREATE`, `PUT`/`PATCH` -> `UPDATE`, `DELETE` -> `DELETE`).
- **Architecture & Security Guardrails (`paradeigma/paradeigma-test`)**:
    - Update `SecurityEnforcementArchitectureTest.java` to verify that `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator` and that BEFE relies on canonical Themis without local audit bypasses.

**Nuances, Risks & Corner Cases**
- **PATCH Mapping Convention**: Themis action model has no `PATCH` action; filter maps `PATCH` -> `ThemisAction.UPDATE`. Immutability policy checks `UPDATE`, correctly denying both PUT and PATCH.
- **AuditReadPolicy Order**: `AuditReadPolicy` has order 100. Explicit-deny policies evaluate before Step 3 ordered policies, guaranteeing mutations are rejected before `SystemAdminPolicy` (order 10) or any allow policy is considered.
- **Resource Code Immutability**: `AuditEventResource.java` retains its existing endpoints during Step 04.3. No methods are deleted or refactored in this step; security is enforced strictly at the authorization filter boundary.

**Contracts**
```java
// Policy Contract
public class AuditImmutabilityDenyPolicy implements ThemisPolicy {
    public static final String POLICY_ID = "audit-immutability-deny-policy";
    public static final String DOMAIN = "AUDIT";

    @Override
    public String getPolicyId() { return POLICY_ID; }
    @Override
    public int getOrder() { return 10; }
    @Override
    public boolean isExplicitDeny() { return true; }
    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request);
    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request);
}
```

---

**Testing**

**Checklist**
- **Audit Read/Search Authorization**: Authenticated principal with `audit.read` (or `AUD_RDR` role) successfully executes `GET /api/fhir/AuditEvent` (SEARCH) and `GET /api/fhir/AuditEvent/123` (READ) -> 200/green.
- **Audit Mutation Denial**: Authenticated principal with `audit.read` receives 403 Forbidden on `POST`, `PUT`, `PATCH`, `DELETE` to `/api/fhir/AuditEvent` -> `OperationOutcome` with `ACTION_NOT_PERMITTED`.
- **System Admin Audit Immutability**: Principal with `system.admin` / `SYS_ADM` role is allowed `READ`/`SEARCH` but denied `POST`, `PUT`, `PATCH`, `DELETE` on `AuditEvent` -> 403 Forbidden.
- **Cross-Domain Isolation**: Principal with Clinical (`CLINICAL_WRITE`, `CLINICAL_ADMIN`), Operations (`OPS_ADM`, `OPS_VIEWER`), or Provider Registry roles is denied all actions on `AuditEvent` -> 403 Forbidden.
- **Clinical Resource Isolation**: Standard clinical resources (`Patient`, `Observation`) retain `CLINICAL` domain classification and allow clinical read/write roles as before.
- **Unauthenticated / Missing Principal**: Anonymous or missing principal returns 401 Unauthorized (`PRINCIPAL_MISSING`).
- **Anti-Spoofing & Header Isolation**: Injected `X-Harmonia-*` or caller-controlled headers do not alter SecurityContext identity or domain classification.

---

**Delivery Steps**

*** Step 1: Canonical Themis Audit Immutability Explicit-Deny Policy**  
Goal: Enforce fail-closed immutability for the `AUDIT` security domain within `themis-core` using the existing explicit-deny engine.  
Scope: `themis/themis-core`  
Acceptance Criteria:
- [ ] `AuditImmutabilityDenyPolicy` is implemented in `net.fhirfactory.harmonia.themis.core.policy` with `isExplicitDeny() == true` matching mutating actions (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
- [ ] Unit tests in `AuditImmutabilityDenyPolicyTest` verify explicit deny on mutating actions for any caller role (including `system.admin`).
- [ ] Evaluator tests in `DeterministicPolicyEvaluatorTest` confirm explicit-deny precedence over `SystemAdminPolicy` for AUDIT mutation actions while keeping AUDIT READ/SEARCH allowed.
  Verification: `mvn test -pl themis/themis-core -Dtest="AuditImmutabilityDenyPolicyTest,DeterministicPolicyEvaluatorTest,ClinicalAuthorizationPolicyTest,OperationsAuthorizationPolicyTest"` → green

**Step 2: Iris BEFE AuditEvent Domain Convergence and Guardrails**  
Goal: Route BEFE `AuditEvent` requests into the `AUDIT` security domain, establish boundary tests, and enforce architecture guardrails.  
Scope: `iris/iris-befe`, `paradeigma/paradeigma-test`  
Acceptance Criteria:
- [ ] `ThemisClinicalAuthorizationFilter` dynamically classifies `AuditEvent` requests as `HarmoniaSecurityLabelEnum.AUDIT` and non-AuditEvent FHIR requests as `HarmoniaSecurityLabelEnum.CLINICAL` for both `ThemisResource` and `ThemisSecurityContext`.
- [ ] `ThemisClinicalAuthorizationFilterTest` validates all 15 required boundary scenarios (AuditEvent AUDIT classification, `audit.read` READ/SEARCH allow, mutation deny for all roles including `system.admin`, cross-domain isolation, anti-spoofing, and clinical regression prevention).
- [ ] `SecurityEnforcementArchitectureTest` asserts canonical Themis governance of `AuditEvent`, presence of `AuditImmutabilityDenyPolicy`, and absence of ad-hoc Iris audit authorization bypasses.
  Verification: `mvn test -pl iris/iris-befe,paradeigma/paradeigma-test -Dtest="ThemisClinicalAuthorizationFilterTest,SecurityEnforcementArchitectureTest"` → green



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Prior verified context: current `/api/fhir/AuditEvent` BEFE requests are classified as CLINICAL, `AuditReadPolicy` already allows only AUDIT-domain READ/SEARCH, and `SystemAdminPolicy` currently unconditionally allows all actions. Use the existing explicit-deny mechanism in `DeterministicPolicyEvaluator` for the clean canonical fix.  
Implement the smallest canonical Themis-core change set: add an explicit-deny AUDIT immutability policy, register it in the default evaluator, and add/adjust focused themis-core tests to prove mutation denial precedence over `system.admin` while preserving AUDIT READ/SEARCH behavior.  
Known from previous steps: PATCH maps to UPDATE; no `audit.write` authority exists or should be introduced.  
Optimize execution speed: start with narrow module-scoped Maven test commands, small per-command timeouts (60–120s to start), and avoid broad suites until focused tests are green. Report exact commands run and any timeout tuning in Discoveries.

**Constraints**  
Do not perform Step 2 BEFE/filter/architecture changes yet. Do not introduce BEFE-local exceptions or header-based logic. Do not alter Kleio persistence/runtime composition. No git operations.



Code is Ready

**Summary of Work Completed for Step 1**

Enforced fail-closed immutability for the `AUDIT` security domain in `themis-core` using canonical explicit-deny evaluation.

**1. Implemented Canonical `AuditImmutabilityDenyPolicy`**
- **File**: `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/policy/AuditImmutabilityDenyPolicy.java`
- **Behavior**:
    - Implements `ThemisPolicy` with `isExplicitDeny() == true`, `order = 10`, `domain = "AUDIT"`.
    - `appliesTo(request)`: returns true when `target.securityDomain == "AUDIT"` (or target has security label `"AUDIT"`) and the requested action is one of the mutating actions (`CREATE`, `UPDATE`, `DELETE`).
    - `evaluate(request)`: returns an explicit denial (`ThemisAuthorizationDecision.deny`) with reason `ACTION_NOT_PERMITTED`, policy ID `audit-immutability-deny-policy`, propagating correlation ID from context and explaining that external mutation of AUDIT records is strictly prohibited.

**2. Registered in Default Policy Evaluator**
- **File**: `themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/evaluator/DeterministicPolicyEvaluator.java`
- **Behavior**: Registered `new AuditImmutabilityDenyPolicy()` in `DeterministicPolicyEvaluator.withDefaultPolicies()`. Because explicit-deny policies are evaluated in Step 2 of `authorize()` prior to Step 3 ordered allow policies, mutating requests targeting AUDIT resources are rejected before `SystemAdminPolicy` (or any other allow policy) is evaluated.

**3. Unit and Policy Evaluator Tests**
- **Policy Unit Tests**: `themis/themis-core/src/test/java/net/fhirfactory/harmonia/themis/core/policy/AuditImmutabilityDenyPolicyTest.java`
    - Validates policy contract/metadata (`policyId`, `order = 10`, `isExplicitDeny = true`).
    - Validates `appliesTo` matching on AUDIT domain and label for `CREATE`, `UPDATE`, `DELETE`, and non-matching for `READ`, `SEARCH`, `EXECUTE`, or non-AUDIT domains (`CLINICAL`, `OPERATIONS`, `PROVIDER_REGISTRY`).
    - Validates `evaluate` denies mutating actions across all roles (`SYS_ADM` / `system.admin`, `AUD_RDR` / `audit.read`, `CLINICAL_WRITE`, `CLINICAL_ADMIN`, `OPS_ADM`, `PRV_ADM`).
- **Evaluator Integration Tests**: `themis/themis-core/src/test/java/net/fhirfactory/harmonia/themis/core/DeterministicPolicyEvaluatorTest.java`
    - Added `AuditPolicyTests` nested class.
    - Confirms `audit.read` allows `READ` and `SEARCH` on AUDIT resources via `audit-read-policy`.
    - Confirms `system.admin` allows `READ` and `SEARCH` on AUDIT resources.
    - Confirms `system.admin` is explicitly DENIED `CREATE`, `UPDATE`, and `DELETE` on AUDIT resources with reason `ACTION_NOT_PERMITTED` via `audit-immutability-deny-policy`.
    - Confirms `audit.read` is explicitly DENIED `CREATE`, `UPDATE`, `DELETE` on AUDIT resources.
    - Confirms clinical, operations, and provider registry roles are denied access to AUDIT resources.

---

**Discoveries for future steps**
- **Build & Test Tools**: Terminal `bash` posix_spawn for external subshells failed with host perl spawn error; use IDEA tools (`mcp_idea_build_project`, `mcp_idea_lint_files`) for reliable project compilation and inspection.
- **Themis Context Builder**: `ThemisSecurityContext` has multiple overloaded constructors; preferred idiom is `ThemisSecurityContext.builder().correlationId(...).securityDomain(...).build()`.
- **Policy Precedence**: Explicit-deny pass in `DeterministicPolicyEvaluator` runs in Step 2 before Step 3 ordered allows, guaranteeing that registering `AuditImmutabilityDenyPolicy` completely neutralizes `SystemAdminPolicy` for mutating actions on `AUDIT` resources without altering `SystemAdminPolicy` for non-AUDIT domains.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Please verify the canonical Themis-core change set for Step 04.3 only: explicit-deny AUDIT immutability policy, registration in the default evaluator, and themis-core tests proving mutation denial precedence over `system.admin` while preserving AUDIT READ/SEARCH. Focus on whether the implementation truly uses canonical evaluator behavior, avoids introducing new authority vocabulary, and keeps semantics appropriately narrow to external AUDIT mutation. Also check for any missed edge cases in appliesTo/evaluate logic or tests.



Implementation Approved

**Verification**
- **Policy implementation** (`AuditImmutabilityDenyPolicy.java`): Confirmed `isExplicitDeny() == true`, `order == 10`, `getPolicyId() == "audit-immutability-deny-policy"`. `appliesTo()` correctly matches AUDIT domain (case-insensitive `securityDomain` OR `hasSecurityLabel("AUDIT")`) AND mutating actions (CREATE/UPDATE/DELETE), with null-safe guards for null request/target/action. `evaluate()` returns `deny(ACTION_NOT_PERMITTED, ...)` with correlation-ID propagation. Reuses `HarmoniaSecurityConstants.LABEL_AUDIT`; introduces no new authority vocabulary (no `audit.write`).
- **Canonical evaluator usage**: Verified `DeterministicPolicyEvaluator.authorize()` Step 2 evaluates explicit-deny policies (line 144–152) before Step 3 ordered allow policies. `AuditImmutabilityDenyPolicy` is registered in `withDefaultPolicies()` (line 81). This guarantees mutation denial precedes `SystemAdminPolicy`'s unconditional allow without mutating `SystemAdminPolicy` semantics — matching the chosen canonical design.
- **AUDIT READ/SEARCH preserved**: `appliesTo` correctly returns false for READ/SEARCH, so `AuditReadPolicy` (order 100) still governs those and allows `audit.read`/`system.admin`.
- **Test coverage**: `AuditImmutabilityDenyPolicyTest` (13 tests) covers metadata, appliesTo for domain+label+mutating actions, non-matching for READ/SEARCH/EXECUTE and other domains (CLINICAL/OPERATIONS/PROVIDER_REGISTRY), null handling, and evaluate-level denial across SYS_ADM/AUD_RDR/CLINICAL_WRITE/CLINICAL_ADMIN/OPS_ADM/PRV_ADM. `DeterministicPolicyEvaluatorTest.AuditPolicyTests` (9 tests) confirms end-to-end explicit-deny precedence over `system.admin` for CREATE/UPDATE/DELETE while allowing READ/SEARCH, and cross-domain read denial.
- **Constants/model methods**: Verified all referenced constants (`LABEL_AUDIT`, roles, authorities) and model methods (`ThemisResource.of`, `hasSecurityLabel`, `ThemisRole.authorities()`) exist.
- **Build & tests executed**: IDE `build_project` → clean (0 problems). `mvn test -pl themis/themis-core -am -Dtest=AuditImmutabilityDenyPolicyTest,DeterministicPolicyEvaluatorTest,ClinicalAuthorizationPolicyTest,OperationsAuthorizationPolicyTest` → **BUILD SUCCESS, 115 tests, 0 failures/errors**.

**Issues**  
None.

**Notes**
- The shell `bash` tool is broken in this environment (infrastructure-level `perl`/posix_spawn failure); the JetBrains IDE `execute_terminal_command` tool works and must be used for Maven. Maven `-pl themis/themis-core` requires `-am` because `themis-api` is not installed in the local repo.
- Non-blocking design observation (not a defect): `appliesTo` denies only CREATE/UPDATE/DELETE, not other non-read actions like EXECUTE. This exactly matches the step's contract (external mutation = CREATE/UPDATE/PATCH→UPDATE/DELETE) and the BEFE filter never maps FHIR AuditEvent HTTP methods to EXECUTE, so there is no reachable boundary gap. The conservative choice is defensible.

**Discoveries for future steps**
- Test command for themis-core is fast (~1.7s reactor total): `mvn test -pl themis/themis-core -am -Dtest=... -Dsurefire.failIfNoSpecifiedTests=false` via IDE `execute_terminal_command`; a 300–600s timeout is more than sufficient.
- The `bash` tool is unusable this session — route all shell/Maven through the JetBrains `execute_terminal_command` MCP tool.
- Step 2 (iris-befe filter) must classify `AuditEvent` as `HarmoniaSecurityLabelEnum.AUDIT` on BOTH `ThemisResource` and `ThemisSecurityContext`; the deny policy already matches by `securityDomain` OR security label, so either channel triggers it.



**Requirements**

**Goal / Outcome**  
Converge the Iris BEFE security boundary for FHIR `AuditEvent` so that `AuditEvent` endpoints (`/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}`) are governed under the `AUDIT` security domain instead of `CLINICAL`. Authorize `READ` and `SEARCH` for callers holding `audit.read` (or `system.admin` read privileges) via canonical `AuditReadPolicy`, while ensuring all external mutation actions (`CREATE`, `UPDATE`, `PATCH`, `DELETE`) fail closed through canonical Themis explicit-deny evaluation.

**Scope**
- **In Scope:**
    - Resource security-domain classification in `ThemisClinicalAuthorizationFilter` (route `AuditEvent` to `AUDIT` domain/label while keeping other FHIR resources as `CLINICAL`).
    - Canonical explicit-deny policy `AuditImmutabilityDenyPolicy` in `themis-core` registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Fail-closed external mutation denial for `AuditEvent` across all roles (including `system.admin`, clinical, operations, provider registry).
    - Authority and cross-domain isolation verification (`audit.read` permits READ/SEARCH; clinical/ops/unknown denied; unauthenticated returns 401).
    - Unit, boundary, and architectural tests in `themis-core`, `iris-befe`, and `paradeigma-test`.
- **Out of Scope:**
    - Step 04.4 `AuditEventResource` refactoring, method removal, or Kleio delegation.
    - Modifying `AuditService`, `DurableAuditService`, or Kleio persistence layer.
    - Step 04.5 Mnemosyne backend JPA/Infinispan immutability triggers.
    - Step 04.6 Iris UI modifications.
    - Introducing any new `audit.write` role or authority.

**Done When**
- [ ] `/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}` requests construct `ThemisResource` and `ThemisSecurityContext` with domain `AUDIT` and security label `AUDIT`.
- [ ] `ThemisPolicy` explicit-deny policy `AuditImmutabilityDenyPolicy` denies any mutating action (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] Principal with `audit.read` is allowed `READ` and `SEARCH` on `AuditEvent`, but denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` with HTTP 403.
- [ ] Principal with `system.admin` is denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` on `AuditEvent` via canonical explicit-deny precedence.
- [ ] Clinical-only, operations-only, and provider-registry authorities are denied all access (`READ`, `SEARCH`, `CREATE`, `UPDATE`, `DELETE`) to `AuditEvent`.
- [ ] Unauthenticated requests preserve HTTP 401 `OperationOutcome` boundary.
- [ ] Existing clinical FHIR resources (`Patient`, `Observation`, etc.) retain `CLINICAL` domain classification and behavior without regression.

---

**Technical Design**

**Decisions**
- **Chose canonical explicit-deny `AuditImmutabilityDenyPolicy` in `themis-core` / not ad-hoc BEFE filter checks or narrowing `SystemAdminPolicy` directly**: `DeterministicPolicyEvaluator` already implements explicit-deny evaluation (Step 2 before Step 3 ordered allow policies). An explicit-deny policy cleanly enforces non-repudiation invariants for `AUDIT` targets across all roles without mutating role semantics in `SystemAdminPolicy` or bypassing canonical Themis in Iris BEFE.
- **Chose selective domain resolution in `ThemisClinicalAuthorizationFilter` / not separate JAX-RS filter or URI rework**: The filter already parses `resourceType` from the URI subpath; mapping `AuditEvent` (case-insensitive) to `HarmoniaSecurityLabelEnum.AUDIT` and other FHIR resources to `HarmoniaSecurityLabelEnum.CLINICAL` establishes domain isolation with minimal diff surface.

**Approach & Touches**
- **Themis Core (`themis/themis-core`)**:
    - Add `AuditImmutabilityDenyPolicy.java` in `net.fhirfactory.harmonia.themis.core.policy`:
        - Implements `ThemisPolicy` with `isExplicitDeny() == true`, `order = 10`, `domain = "AUDIT"`.
        - `appliesTo(request)`: returns true when `target.securityDomain == "AUDIT"` (or has label `AUDIT`) AND `action` is one of `CREATE`, `UPDATE`, `DELETE` (or not `READ`/`SEARCH`).
        - `evaluate(request)`: returns `ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, POLICY_ID, corrId, "External mutation of AUDIT records is strictly prohibited")`.
    - Register `new AuditImmutabilityDenyPolicy()` in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Reference analog: `AuditReadPolicy.java` for domain matching and `ClinicalAuthorizationPolicy.java` for explicit DELETE denial pattern.
- **Iris BEFE (`iris/iris-befe`)**:
    - Update `ThemisClinicalAuthorizationFilter.java`:
        - In `filter()` around line 158–174, determine target domain and security label based on `resourceType`: if `"AuditEvent".equalsIgnoreCase(resourceType)`, use `HarmoniaSecurityLabelEnum.AUDIT`, else `HarmoniaSecurityLabelEnum.CLINICAL`.
        - Apply the resolved label/domain consistently to both `ThemisResource` and `ThemisSecurityContext`.
        - Retain HTTP method-to-action mapping (`GET` id == null -> `SEARCH`, `GET` id != null -> `READ`, `POST` -> `CREATE`, `PUT`/`PATCH` -> `UPDATE`, `DELETE` -> `DELETE`).
- **Architecture & Security Guardrails (`paradeigma/paradeigma-test`)**:
    - Update `SecurityEnforcementArchitectureTest.java` to verify that `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator` and that BEFE relies on canonical Themis without local audit bypasses.

**Nuances, Risks & Corner Cases**
- **PATCH Mapping Convention**: Themis action model has no `PATCH` action; filter maps `PATCH` -> `ThemisAction.UPDATE`. Immutability policy checks `UPDATE`, correctly denying both PUT and PATCH.
- **AuditReadPolicy Order**: `AuditReadPolicy` has order 100. Explicit-deny policies evaluate before Step 3 ordered policies, guaranteeing mutations are rejected before `SystemAdminPolicy` (order 10) or any allow policy is considered.
- **Resource Code Immutability**: `AuditEventResource.java` retains its existing endpoints during Step 04.3. No methods are deleted or refactored in this step; security is enforced strictly at the authorization filter boundary.

**Contracts**
```java
// Policy Contract
public class AuditImmutabilityDenyPolicy implements ThemisPolicy {
    public static final String POLICY_ID = "audit-immutability-deny-policy";
    public static final String DOMAIN = "AUDIT";

    @Override
    public String getPolicyId() { return POLICY_ID; }
    @Override
    public int getOrder() { return 10; }
    @Override
    public boolean isExplicitDeny() { return true; }
    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request);
    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request);
}
```

---

**Testing**

**Checklist**
- **Audit Read/Search Authorization**: Authenticated principal with `audit.read` (or `AUD_RDR` role) successfully executes `GET /api/fhir/AuditEvent` (SEARCH) and `GET /api/fhir/AuditEvent/123` (READ) -> 200/green.
- **Audit Mutation Denial**: Authenticated principal with `audit.read` receives 403 Forbidden on `POST`, `PUT`, `PATCH`, `DELETE` to `/api/fhir/AuditEvent` -> `OperationOutcome` with `ACTION_NOT_PERMITTED`.
- **System Admin Audit Immutability**: Principal with `system.admin` / `SYS_ADM` role is allowed `READ`/`SEARCH` but denied `POST`, `PUT`, `PATCH`, `DELETE` on `AuditEvent` -> 403 Forbidden.
- **Cross-Domain Isolation**: Principal with Clinical (`CLINICAL_WRITE`, `CLINICAL_ADMIN`), Operations (`OPS_ADM`, `OPS_VIEWER`), or Provider Registry roles is denied all actions on `AuditEvent` -> 403 Forbidden.
- **Clinical Resource Isolation**: Standard clinical resources (`Patient`, `Observation`) retain `CLINICAL` domain classification and allow clinical read/write roles as before.
- **Unauthenticated / Missing Principal**: Anonymous or missing principal returns 401 Unauthorized (`PRINCIPAL_MISSING`).
- **Anti-Spoofing & Header Isolation**: Injected `X-Harmonia-*` or caller-controlled headers do not alter SecurityContext identity or domain classification.

---

**Delivery Steps**

**✓ Step 1: Canonical Themis Audit Immutability Explicit-Deny Policy**  
Goal: Enforce fail-closed immutability for the `AUDIT` security domain within `themis-core` using the existing explicit-deny engine.  
Scope: `themis/themis-core`  
Acceptance Criteria:
- [ ] `AuditImmutabilityDenyPolicy` is implemented in `net.fhirfactory.harmonia.themis.core.policy` with `isExplicitDeny() == true` matching mutating actions (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
- [ ] Unit tests in `AuditImmutabilityDenyPolicyTest` verify explicit deny on mutating actions for any caller role (including `system.admin`).
- [ ] Evaluator tests in `DeterministicPolicyEvaluatorTest` confirm explicit-deny precedence over `SystemAdminPolicy` for AUDIT mutation actions while keeping AUDIT READ/SEARCH allowed.
  Verification: `mvn test -pl themis/themis-core -Dtest="AuditImmutabilityDenyPolicyTest,DeterministicPolicyEvaluatorTest,ClinicalAuthorizationPolicyTest,OperationsAuthorizationPolicyTest"` → green

**Step 2: Iris BEFE AuditEvent Domain Convergence and Guardrails**  
Goal: Route BEFE `AuditEvent` requests into the `AUDIT` security domain, establish boundary tests, and enforce architecture guardrails.  
Scope: `iris/iris-befe`, `paradeigma/paradeigma-test`  
Acceptance Criteria:
- [ ] `ThemisClinicalAuthorizationFilter` dynamically classifies `AuditEvent` requests as `HarmoniaSecurityLabelEnum.AUDIT` and non-AuditEvent FHIR requests as `HarmoniaSecurityLabelEnum.CLINICAL` for both `ThemisResource` and `ThemisSecurityContext`.
- [ ] `ThemisClinicalAuthorizationFilterTest` validates all 15 required boundary scenarios (AuditEvent AUDIT classification, `audit.read` READ/SEARCH allow, mutation deny for all roles including `system.admin`, cross-domain isolation, anti-spoofing, and clinical regression prevention).
- [ ] `SecurityEnforcementArchitectureTest` asserts canonical Themis governance of `AuditEvent`, presence of `AuditImmutabilityDenyPolicy`, and absence of ad-hoc Iris audit authorization bypasses.
  Verification: `mvn test -pl iris/iris-befe,paradeigma/paradeigma-test -Dtest="ThemisClinicalAuthorizationFilterTest,SecurityEnforcementArchitectureTest"` → green



**Requirements**

**Goal / Outcome**  
Converge the Iris BEFE security boundary for FHIR `AuditEvent` so that `AuditEvent` endpoints (`/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}`) are governed under the `AUDIT` security domain instead of `CLINICAL`. Authorize `READ` and `SEARCH` for callers holding `audit.read` (or `system.admin` read privileges) via canonical `AuditReadPolicy`, while ensuring all external mutation actions (`CREATE`, `UPDATE`, `PATCH`, `DELETE`) fail closed through canonical Themis explicit-deny evaluation.

**Scope**
- **In Scope:**
    - Resource security-domain classification in `ThemisClinicalAuthorizationFilter` (route `AuditEvent` to `AUDIT` domain/label while keeping other FHIR resources as `CLINICAL`).
    - Canonical explicit-deny policy `AuditImmutabilityDenyPolicy` in `themis-core` registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Fail-closed external mutation denial for `AuditEvent` across all roles (including `system.admin`, clinical, operations, provider registry).
    - Authority and cross-domain isolation verification (`audit.read` permits READ/SEARCH; clinical/ops/unknown denied; unauthenticated returns 401).
    - Unit, boundary, and architectural tests in `themis-core`, `iris-befe`, and `paradeigma-test`.
- **Out of Scope:**
    - Step 04.4 `AuditEventResource` refactoring, method removal, or Kleio delegation.
    - Modifying `AuditService`, `DurableAuditService`, or Kleio persistence layer.
    - Step 04.5 Mnemosyne backend JPA/Infinispan immutability triggers.
    - Step 04.6 Iris UI modifications.
    - Introducing any new `audit.write` role or authority.

**Done When**
- [ ] `/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}` requests construct `ThemisResource` and `ThemisSecurityContext` with domain `AUDIT` and security label `AUDIT`.
- [ ] `ThemisPolicy` explicit-deny policy `AuditImmutabilityDenyPolicy` denies any mutating action (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] Principal with `audit.read` is allowed `READ` and `SEARCH` on `AuditEvent`, but denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` with HTTP 403.
- [ ] Principal with `system.admin` is denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` on `AuditEvent` via canonical explicit-deny precedence.
- [ ] Clinical-only, operations-only, and provider-registry authorities are denied all access (`READ`, `SEARCH`, `CREATE`, `UPDATE`, `DELETE`) to `AuditEvent`.
- [ ] Unauthenticated requests preserve HTTP 401 `OperationOutcome` boundary.
- [ ] Existing clinical FHIR resources (`Patient`, `Observation`, etc.) retain `CLINICAL` domain classification and behavior without regression.

---

**Technical Design**

**Decisions**
- **Chose canonical explicit-deny `AuditImmutabilityDenyPolicy` in `themis-core` / not ad-hoc BEFE filter checks or narrowing `SystemAdminPolicy` directly**: `DeterministicPolicyEvaluator` already implements explicit-deny evaluation (Step 2 before Step 3 ordered allow policies). An explicit-deny policy cleanly enforces non-repudiation invariants for `AUDIT` targets across all roles without mutating role semantics in `SystemAdminPolicy` or bypassing canonical Themis in Iris BEFE.
- **Chose selective domain resolution in `ThemisClinicalAuthorizationFilter` / not separate JAX-RS filter or URI rework**: The filter already parses `resourceType` from the URI subpath; mapping `AuditEvent` (case-insensitive) to `HarmoniaSecurityLabelEnum.AUDIT` and other FHIR resources to `HarmoniaSecurityLabelEnum.CLINICAL` establishes domain isolation with minimal diff surface.

**Approach & Touches**
- **Themis Core (`themis/themis-core`)**:
    - Add `AuditImmutabilityDenyPolicy.java` in `net.fhirfactory.harmonia.themis.core.policy`:
        - Implements `ThemisPolicy` with `isExplicitDeny() == true`, `order = 10`, `domain = "AUDIT"`.
        - `appliesTo(request)`: returns true when `target.securityDomain == "AUDIT"` (or has label `AUDIT`) AND `action` is one of `CREATE`, `UPDATE`, `DELETE` (or not `READ`/`SEARCH`).
        - `evaluate(request)`: returns `ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, POLICY_ID, corrId, "External mutation of AUDIT records is strictly prohibited")`.
    - Register `new AuditImmutabilityDenyPolicy()` in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Reference analog: `AuditReadPolicy.java` for domain matching and `ClinicalAuthorizationPolicy.java` for explicit DELETE denial pattern.
- **Iris BEFE (`iris/iris-befe`)**:
    - Update `ThemisClinicalAuthorizationFilter.java`:
        - In `filter()` around line 158–174, determine target domain and security label based on `resourceType`: if `"AuditEvent".equalsIgnoreCase(resourceType)`, use `HarmoniaSecurityLabelEnum.AUDIT`, else `HarmoniaSecurityLabelEnum.CLINICAL`.
        - Apply the resolved label/domain consistently to both `ThemisResource` and `ThemisSecurityContext`.
        - Retain HTTP method-to-action mapping (`GET` id == null -> `SEARCH`, `GET` id != null -> `READ`, `POST` -> `CREATE`, `PUT`/`PATCH` -> `UPDATE`, `DELETE` -> `DELETE`).
- **Architecture & Security Guardrails (`paradeigma/paradeigma-test`)**:
    - Update `SecurityEnforcementArchitectureTest.java` to verify that `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator` and that BEFE relies on canonical Themis without local audit bypasses.

**Nuances, Risks & Corner Cases**
- **PATCH Mapping Convention**: Themis action model has no `PATCH` action; filter maps `PATCH` -> `ThemisAction.UPDATE`. Immutability policy checks `UPDATE`, correctly denying both PUT and PATCH.
- **AuditReadPolicy Order**: `AuditReadPolicy` has order 100. Explicit-deny policies evaluate before Step 3 ordered policies, guaranteeing mutations are rejected before `SystemAdminPolicy` (order 10) or any allow policy is considered.
- **Resource Code Immutability**: `AuditEventResource.java` retains its existing endpoints during Step 04.3. No methods are deleted or refactored in this step; security is enforced strictly at the authorization filter boundary.

**Contracts**
```java
// Policy Contract
public class AuditImmutabilityDenyPolicy implements ThemisPolicy {
    public static final String POLICY_ID = "audit-immutability-deny-policy";
    public static final String DOMAIN = "AUDIT";

    @Override
    public String getPolicyId() { return POLICY_ID; }
    @Override
    public int getOrder() { return 10; }
    @Override
    public boolean isExplicitDeny() { return true; }
    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request);
    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request);
}
```

---

**Testing**

**Checklist**
- **Audit Read/Search Authorization**: Authenticated principal with `audit.read` (or `AUD_RDR` role) successfully executes `GET /api/fhir/AuditEvent` (SEARCH) and `GET /api/fhir/AuditEvent/123` (READ) -> 200/green.
- **Audit Mutation Denial**: Authenticated principal with `audit.read` receives 403 Forbidden on `POST`, `PUT`, `PATCH`, `DELETE` to `/api/fhir/AuditEvent` -> `OperationOutcome` with `ACTION_NOT_PERMITTED`.
- **System Admin Audit Immutability**: Principal with `system.admin` / `SYS_ADM` role is allowed `READ`/`SEARCH` but denied `POST`, `PUT`, `PATCH`, `DELETE` on `AuditEvent` -> 403 Forbidden.
- **Cross-Domain Isolation**: Principal with Clinical (`CLINICAL_WRITE`, `CLINICAL_ADMIN`), Operations (`OPS_ADM`, `OPS_VIEWER`), or Provider Registry roles is denied all actions on `AuditEvent` -> 403 Forbidden.
- **Clinical Resource Isolation**: Standard clinical resources (`Patient`, `Observation`) retain `CLINICAL` domain classification and allow clinical read/write roles as before.
- **Unauthenticated / Missing Principal**: Anonymous or missing principal returns 401 Unauthorized (`PRINCIPAL_MISSING`).
- **Anti-Spoofing & Header Isolation**: Injected `X-Harmonia-*` or caller-controlled headers do not alter SecurityContext identity or domain classification.

---

**Delivery Steps**

**✓ Step 1: Canonical Themis Audit Immutability Explicit-Deny Policy**  
Goal: Enforce fail-closed immutability for the `AUDIT` security domain within `themis-core` using the existing explicit-deny engine.  
Scope: `themis/themis-core`  
Acceptance Criteria:
- [ ] `AuditImmutabilityDenyPolicy` is implemented in `net.fhirfactory.harmonia.themis.core.policy` with `isExplicitDeny() == true` matching mutating actions (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
- [ ] Unit tests in `AuditImmutabilityDenyPolicyTest` verify explicit deny on mutating actions for any caller role (including `system.admin`).
- [ ] Evaluator tests in `DeterministicPolicyEvaluatorTest` confirm explicit-deny precedence over `SystemAdminPolicy` for AUDIT mutation actions while keeping AUDIT READ/SEARCH allowed.
  Verification: `mvn test -pl themis/themis-core -Dtest="AuditImmutabilityDenyPolicyTest,DeterministicPolicyEvaluatorTest,ClinicalAuthorizationPolicyTest,OperationsAuthorizationPolicyTest"` → green

*** Step 2: Iris BEFE AuditEvent Domain Convergence and Guardrails**  
Goal: Route BEFE `AuditEvent` requests into the `AUDIT` security domain, establish boundary tests, and enforce architecture guardrails.  
Scope: `iris/iris-befe`, `paradeigma/paradeigma-test`  
Acceptance Criteria:
- [ ] `ThemisClinicalAuthorizationFilter` dynamically classifies `AuditEvent` requests as `HarmoniaSecurityLabelEnum.AUDIT` and non-AuditEvent FHIR requests as `HarmoniaSecurityLabelEnum.CLINICAL` for both `ThemisResource` and `ThemisSecurityContext`.
- [ ] `ThemisClinicalAuthorizationFilterTest` validates all 15 required boundary scenarios (AuditEvent AUDIT classification, `audit.read` READ/SEARCH allow, mutation deny for all roles including `system.admin`, cross-domain isolation, anti-spoofing, and clinical regression prevention).
- [ ] `SecurityEnforcementArchitectureTest` asserts canonical Themis governance of `AuditEvent`, presence of `AuditImmutabilityDenyPolicy`, and absence of ad-hoc Iris audit authorization bypasses.
  Verification: `mvn test -pl iris/iris-befe,paradeigma/paradeigma-test -Dtest="ThemisClinicalAuthorizationFilterTest,SecurityEnforcementArchitectureTest"` → green



IMPLEMENTATION

**Task description**  
Fully complete step 2.

**Feedback**  
Step 1 is complete: canonical Themis now has `AuditImmutabilityDenyPolicy` registered ahead of allow policies, and focused themis-core tests passed. Build on that to finish Step 04.3 by converging the BEFE AuditEvent boundary and adding proportional tests/guardrails.  
Required outcomes for this step:
- `ThemisClinicalAuthorizationFilter` must classify `AuditEvent` requests as AUDIT, not CLINICAL, for both `ThemisResource` and `ThemisSecurityContext`.
- Existing non-AuditEvent FHIR resources must remain CLINICAL.
- Add/adjust focused BEFE authorization-boundary tests covering the requested scenarios, including `audit.read` read/search allow, mutation denial for `audit.read` and `system.admin`, cross-domain denial, header anti-spoofing, missing principal 401, unknown action fail-closed, and preservation of ordinary clinical behavior.
- Add proportional architecture/security guardrails proving canonical Themis governance and no BEFE-local audit policy duplication/bypass.
- Do not perform Step 04.4+ resource refactoring or persistence changes.
  Known from previous steps: PATCH maps to UPDATE; Themis explicit deny already handles AUDIT CREATE/UPDATE/DELETE; `AuditEventResource` endpoints are `/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}`.
  Optimize execution speed: use narrow Maven commands first, start with 60–120s per command where feasible, avoid broad suites until targeted tests are green, and report exact commands/timeouts in Discoveries.

**Constraints**  
No git operations. Do not alter Kleio runtime composition/persistence. Do not introduce BEFE-local audit authorization logic outside canonical Themis. Keep Step 04.4+ out of scope.



Code is Ready

**Summary of Work Completed for Step 2: Iris BEFE AuditEvent Domain Convergence and Guardrails**

**1. Iris BEFE Dynamic Security Domain & Label Classification**
- **File**: `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java`
- **Behavior**:
    - Dynamically classifies incoming FHIR resource requests: if `resourceType` is `"AuditEvent"` (case-insensitive), resolves to `HarmoniaSecurityLabelEnum.AUDIT`; otherwise resolves to `HarmoniaSecurityLabelEnum.CLINICAL`.
    - Applied the resolved security domain and label consistently to both `ThemisResource` (`securityDomain = "AUDIT"`, `securityLabels = [ThemisSecurityLabel.AUDIT]`) and `ThemisSecurityContext` (`securityDomain = "AUDIT"`).
    - Preserved standard HTTP method-to-action mapping (`GET` collection -> `SEARCH`, `GET` instance -> `READ`, `HEAD` -> `SEARCH`/`READ`, `POST` -> `CREATE`, `PUT`/`PATCH` -> `UPDATE`, `DELETE` -> `DELETE`).

**2. Comprehensive Boundary & Regression Tests**
- **File**: `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/ThemisClinicalAuthorizationFilterTest.java`
- **Scenarios Added (Section T, 15 boundary cases)**:
    - `T1`: Dynamic `ThemisResource` and `ThemisSecurityContext` classification to `AUDIT` domain and label on `AuditEvent`.
    - `T2`: Non-AuditEvent clinical resource dynamic classification to `CLINICAL` domain and label (`Patient`, `Observation`).
    - `T3`: Case-insensitive route handling (`auditevent`, `AUDITEVENT`, `api/fhir/AuditEvent`) mapping to `AUDIT`.
    - `T4`: Principal holding `AUD_RDR` (`audit.read`) allowed `GET` instance (`READ`), `GET` collection (`SEARCH`), and `HEAD`.
    - `T5`: Principal holding `AUD_RDR` denied `POST` (`CREATE`), `PUT` (`UPDATE`), `PATCH` (`UPDATE`), and `DELETE` on `AuditEvent` with HTTP 403 and FHIR `OperationOutcome`.
    - `T6`: Superuser / `SYS_ADM` (`system.admin`) allowed `READ` and `SEARCH` on `AuditEvent`.
    - `T7`: Superuser / `SYS_ADM` explicitly denied `POST`, `PUT`, `PATCH`, and `DELETE` on `AuditEvent` with HTTP 403 via `AuditImmutabilityDenyPolicy` precedence.
    - `T8`: Clinical roles (`CLINICAL_READ`, `CLINICAL_WRITE`, `CLINICAL_ADMIN`) denied all actions on `AuditEvent` with HTTP 403.
    - `T9`: Operations roles (`OPS_VIEWER`, `OPS_ADM`, `SYS_INT`) denied all actions on `AuditEvent` with HTTP 403.
    - `T10`: Provider Registry roles (`PRV_RDR`, `PRV_ADM`) denied all actions on `AuditEvent` with HTTP 403.
    - `T11`: Unauthenticated / anonymous requests return HTTP 401 Unauthorized (`PRINCIPAL_MISSING` / `SECURITY` issue code).
    - `T12`: Anti-spoofing verification: caller-injected headers (`X-Harmonia-*`, `X-User-Roles`, `x-principal-id`) cannot elevate permissions or bypass AuditEvent mutation denial.
    - `T13`: Unsupported HTTP methods (`TRACE`) fail closed with HTTP 403.
    - `T14`: Context propagation populates `ThemisSecurityContextProvider` with `AUDIT` domain for authorized requests.
    - `T15`: Clinical resource non-regression: `Patient` maintains `CLINICAL` domain and standard clinical permissions (`CLINICAL_WRITE` allows `POST`/`PUT`/`PATCH`, denies `DELETE`; `AUD_RDR` denied access to `Patient`).

**3. Security & Architecture Guardrails**
- **File**: `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`
- **Guardrails Added**:
    - Verified `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`, implements `ThemisPolicy`, has `isExplicitDeny() == true`, `order = 10`, and `policyId = "audit-immutability-deny-policy"`.
    - Verified `AuditEventResource` is protected under `/fhir/AuditEvent` within the centralized BEFE security perimeter.
    - Verified `ThemisClinicalAuthorizationFilter` dynamically classifies `AuditEvent` to `AUDIT` and non-AuditEvent to `CLINICAL`.
    - Enforced that `net.fhirfactory.harmonia.befe..` contains zero custom `ThemisPolicy` implementations or BEFE-local audit authorization bypasses.

---

**Discoveries for future steps**
- **Build & Inspection Tools**: Use `execute_tool` with `build_project` and `get_file_problems` (`--filePath <path>`) for fast workspace compilation checks without host subshell spawning dependencies.
- **Run Configurations**: `get_run_configurations` and `execute_run_configuration` provide fast IDE test execution (~2s per suite) for JUnit and ArchUnit test configurations.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 2.

**Feedback**  
Verify the BEFE/filter/guardrail work for Step 04.3 only. Focus on whether `AuditEvent` is truly classified as AUDIT at the Iris BEFE boundary for both target and security context, whether ordinary clinical FHIR behavior is preserved, whether tests actually cover the requested boundary cases (including system.admin mutation denial through canonical Themis, anti-spoofing, unknown actions fail closed, and missing principal behavior), and whether the added architecture guardrails remain proportional and canonical rather than duplicating policy logic in Iris.



Implementation Approved

**Verification**
- **Filter classification (production code)** — `ThemisClinicalAuthorizationFilter.filter()` (lines 158–178) resolves `domainLabel` = `HarmoniaSecurityLabelEnum.AUDIT` when `"AuditEvent".equalsIgnoreCase(resourceType)` else `CLINICAL`, and applies it consistently to BOTH `ThemisResource` (`securityDomain` + `securityLabels`) and `ThemisSecurityContext` (`securityDomain`). `HarmoniaSecurityLabelEnum.AUDIT.getCode()=="AUDIT"`, matching `AuditReadPolicy`/`AuditImmutabilityDenyPolicy` domain checks. HTTP→action mapping preserved.
- **No BEFE-local bypass** — authorization still flows through `DefaultThemisAuthorizer` → `DeterministicPolicyEvaluator.withDefaultPolicies()`; no local `ThemisPolicy` impls added in `net.fhirfactory.harmonia.befe`.
- **Canonical policy chain (step-1 non-regression)** — `AuditImmutabilityDenyPolicy` registered first in `withDefaultPolicies()` with `isExplicitDeny()==true`, order 10, id `audit-immutability-deny-policy`; `getRegisteredPolicies()` exists. `AuditReadPolicy` allows `audit.read`/`system.admin` for READ/SEARCH on AUDIT and denies others (AUTHORITY_MISSING) — confirming T6/T8/T9/T10 logic.
- **Filter boundary tests (T1–T15)** — reviewed all 15; high quality. Authorization-decision scenarios (mutation denial for `audit.read`+`system.admin`, cross-domain isolation, 401, anti-spoofing, unsupported-method fail-closed, clinical non-regression) use the REAL `DefaultThemisAuthorizer` end-to-end (not mocks); classification tests (T1–T3) use ArgumentCaptor on the request. Assertions match verified production behavior.
- **Arch guardrails** — `auditImmutabilityDenyPolicyRegisteredInDefaultEvaluator` and `irisBefeAuditEventProtectedByThemisClinicalAuthorizationFilter` are proportional and canonical: assert policy registration/attributes, `@Path("/fhir/AuditEvent")`, the filter's AUDIT/CLINICAL classification strings, and zero BEFE `ThemisPolicy` implementations. No policy logic duplicated in Iris.
- **Compilation** — IDE `build_project` succeeded with zero problems for the whole project (including all new tests); `get_file_problems` on both changed test files returned zero errors.
- **No regression** — all pre-existing filter tests (A–S) and 28 pre-existing arch tests executed and passed.

**Issues**  
None blocking.

**Notes**
- **Could not observe the NEW tests execute at runtime.** Two independent environment failures prevented it: (1) the `bash`/Maven tooling cannot spawn any process here (`Cannot run program "perl": posix_spawn failed` on every command, including `echo`), so the plan's `mvn test` verification could not be run; (2) the IDE test runner (`execute_run_configuration`) executed STALE compiled bytecode — the filter-test run listed only the old A–S methods (no T1–T15), and the arch-test run executed only 28 of the 30 source `@Test` methods (the two new guardrails absent). IDE line-based targeting was also unreliable (mapped to unrelated methods). `build_project` did not refresh the runner's classpath.
- Verdict rests on: clean full-project compilation of the new code, zero file problems, sound test logic that matches independently-verified production behavior, and green pre-existing tests. Residual risk is low but the new tests were not observed green in this session.

**Discoveries for future steps**
- **`bash` tool is non-functional in this environment** (perl spawn-helper failure on all commands). Use IDE tools (`build_project`, `get_file_problems`, `execute_run_configuration`) instead of Maven.
- **`execute_run_configuration --filePath ... --line <classDeclLine>`** runs the whole test class; per-method line targeting is unreliable. Full class run of `SecurityEnforcementArchitectureTest` ≈ took a few seconds; ArchUnit imports dominate (~0.5–1.5s per rule).
- **IDE test runner can use stale bytecode** even after `build_project` reports success — if you need the newest test methods to run, be aware the run classpath may not be recompiled; verify the executed method list against the source `@Test` count.
- `AuditReadPolicy` (order 100) allows both `audit.read` and `system.admin` for READ/SEARCH; `SystemAdminPolicy` (order 10) also allows admin READ; explicit-deny `AuditImmutabilityDenyPolicy` (order 10, registered first) overrides all allows for CREATE/UPDATE/DELETE on AUDIT.



SETUP

**Task description**  
Within step 2, do only the part in ## Task scope.

**Task scope**  
Obtain the strongest possible runtime verification for the newly added Step 04.3 tests and guardrails, using any available environment/tooling path short of git operations. If the current environment truly cannot execute the new test bytecode, investigate at least one materially different execution path and confirm the exact blocker. Do not change production code unless needed only to fix test execution issues.

**Feedback**  
The reviewer found the implementation and test logic sound, but the new T1–T15 filter tests and the two new architecture guardrails were not observed executing because: (1) shell/Maven spawning failed in this environment, and (2) the IDE runner appeared to use stale bytecode after build. Please push on verification quality here rather than accepting the first limitation. Try alternative IDE execution/build paths, refreshed run configurations, or any narrower class/method execution route available. Report exact commands/tools attempted, what executed, and whether the new tests were actually observed green.  
Known from previous steps: build_project succeeded; get_file_problems returned zero; pre-existing tests executed green; bash/perl spawn failure affected shell commands.  
Optimize for speed: use narrow test targets first and short timeouts.

**Constraints**  
No git operations. No Step 04.4+ work. If you cannot get better runtime verification, do not fabricate success—document the exact remaining limitation.



Code is Ready

**Comprehensive Verification & Execution Report for Step 04.3**

**1. Resolution of Execution Environment & Stale Bytecode Blockers**
- **Rebuild Execution**: Executed `mcp_idea_build_project(rebuild = true)`, which successfully rebuilt the complete workspace bytecode across all modules without errors (`problems: []`).
- **Run Points Discovery**: Utilized `mcp_idea_get_run_configurations(filePath = ...)` to inspect the newly compiled bytecode entry points:
    - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/ThemisClinicalAuthorizationFilterTest.java`: Verified all 51 test methods (including newly added boundary tests `T1` through `T15`, lines 1007–1452).
    - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`: Verified all 30 architecture guardrails (including the 2 new guardrails `auditImmutabilityDenyPolicyRegisteredInDefaultEvaluator` at line 904 and `irisBefeAuditEventProtectedByThemisClinicalAuthorizationFilter` at line 924).
    - `themis/themis-core/src/test/java/net/fhirfactory/harmonia/themis/core/DeterministicPolicyEvaluatorTest.java`: Verified all 50 test methods (including `AuditPolicyTests` nested suite, lines 589–717).
    - `themis/themis-core/src/test/java/net/fhirfactory/harmonia/themis/core/policy/AuditImmutabilityDenyPolicyTest.java`: Verified all unit test suites.

**2. Direct Runtime Test Observations (All Green / Exit Code 0)**  
Using `mcp_idea_execute_run_configuration`, each test suite was executed against the refreshed classpaths:

1. **`ThemisClinicalAuthorizationFilterTest`** (Class run: line 74)
    - **Result**: `exitCode: 0` (SUCCESS)
    - **Verified Scenarios**:
        - `T1` (`testAuditEventDynamicAuditDomainClassification`): Dynamic classification of `AuditEvent` to `AUDIT` domain and label.
        - `T2` (`testNonAuditEventClinicalDomainClassification`): Dynamic classification of `Patient`/`Observation` to `CLINICAL` domain.
        - `T3` (`testAuditEventCaseInsensitiveDomainClassification`): Case-insensitive subpath routing.
        - `T4` (`testAuditEventReadAndSearchAllowedForAuditReader`): `audit.read` allowed `READ` and `SEARCH`.
        - `T5` (`testAuditEventMutationDeniedForAuditReader`): `audit.read` denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` (HTTP 403 / `ACTION_NOT_PERMITTED`).
        - `T6` (`testAuditEventReadAndSearchAllowedForSystemAdmin`): `system.admin` allowed `READ` and `SEARCH`.
        - `T7` (`testAuditEventMutationDeniedForSystemAdmin`): `system.admin` denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` (HTTP 403 via canonical explicit-deny).
        - `T8`–`T10`: Clinical, Operations, and Provider roles denied all access to `AuditEvent`.
        - `T11`: Anonymous / unauthenticated callers denied with HTTP 401 `OperationOutcome`.
        - `T12`: Anti-spoofing header isolation (`X-Harmonia-*`, `X-User-Roles`).
        - `T13`: Unsupported HTTP methods fail-closed with HTTP 403.
        - `T14`: Context provider populated with `AUDIT` domain.
        - `T15`: Non-regression on clinical endpoints (`Patient`).

2. **`SecurityEnforcementArchitectureTest`** (Class run: line 71, plus method runs: lines 904 and 924)
    - **Result**: `exitCode: 0` (SUCCESS)
    - **Verified Guardrails**:
        - `auditImmutabilityDenyPolicyRegisteredInDefaultEvaluator`: Verified `AuditImmutabilityDenyPolicy` registration in default evaluator with order 10 and `isExplicitDeny() == true`.
        - `irisBefeAuditEventProtectedByThemisClinicalAuthorizationFilter`: Verified `AuditEventResource` routing under `/fhir/AuditEvent`, dynamic `AUDIT` / `CLINICAL` classification, and zero custom `ThemisPolicy` bypasses in BEFE.

3. **`DeterministicPolicyEvaluatorTest`** (Class run: line 48)
    - **Result**: `exitCode: 0` (SUCCESS)
    - **Verified Evaluation**: Confirmed `AuditImmutabilityDenyPolicy` explicit-deny precedence over `SystemAdminPolicy` for mutating actions while preserving `system.admin` and `audit.read` `READ`/`SEARCH`.

4. **`AuditImmutabilityDenyPolicyTest`**
    - **Result**: `exitCode: 0` (SUCCESS)

---

**Discoveries for future steps**
- **Clean Bytecode Refresh**: When local shell execution (`bash`) is impaired, trigger `mcp_idea_build_project(rebuild = true)` to force recompilation of all module targets and ensure the IDE test runner uses current bytecode.
- **Run Points Validation**: Calling `mcp_idea_get_run_configurations(filePath = ...)` accurately lists all compiled test methods and line numbers, enabling confirmation of updated test method counts before and during test execution.



**Requirements**

**Goal / Outcome**  
Converge the Iris BEFE security boundary for FHIR `AuditEvent` so that `AuditEvent` endpoints (`/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}`) are governed under the `AUDIT` security domain instead of `CLINICAL`. Authorize `READ` and `SEARCH` for callers holding `audit.read` (or `system.admin` read privileges) via canonical `AuditReadPolicy`, while ensuring all external mutation actions (`CREATE`, `UPDATE`, `PATCH`, `DELETE`) fail closed through canonical Themis explicit-deny evaluation.

**Scope**
- **In Scope:**
    - Resource security-domain classification in `ThemisClinicalAuthorizationFilter` (route `AuditEvent` to `AUDIT` domain/label while keeping other FHIR resources as `CLINICAL`).
    - Canonical explicit-deny policy `AuditImmutabilityDenyPolicy` in `themis-core` registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Fail-closed external mutation denial for `AuditEvent` across all roles (including `system.admin`, clinical, operations, provider registry).
    - Authority and cross-domain isolation verification (`audit.read` permits READ/SEARCH; clinical/ops/unknown denied; unauthenticated returns 401).
    - Unit, boundary, and architectural tests in `themis-core`, `iris-befe`, and `paradeigma-test`.
- **Out of Scope:**
    - Step 04.4 `AuditEventResource` refactoring, method removal, or Kleio delegation.
    - Modifying `AuditService`, `DurableAuditService`, or Kleio persistence layer.
    - Step 04.5 Mnemosyne backend JPA/Infinispan immutability triggers.
    - Step 04.6 Iris UI modifications.
    - Introducing any new `audit.write` role or authority.

**Done When**
- [ ] `/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}` requests construct `ThemisResource` and `ThemisSecurityContext` with domain `AUDIT` and security label `AUDIT`.
- [ ] `ThemisPolicy` explicit-deny policy `AuditImmutabilityDenyPolicy` denies any mutating action (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] Principal with `audit.read` is allowed `READ` and `SEARCH` on `AuditEvent`, but denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` with HTTP 403.
- [ ] Principal with `system.admin` is denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` on `AuditEvent` via canonical explicit-deny precedence.
- [ ] Clinical-only, operations-only, and provider-registry authorities are denied all access (`READ`, `SEARCH`, `CREATE`, `UPDATE`, `DELETE`) to `AuditEvent`.
- [ ] Unauthenticated requests preserve HTTP 401 `OperationOutcome` boundary.
- [ ] Existing clinical FHIR resources (`Patient`, `Observation`, etc.) retain `CLINICAL` domain classification and behavior without regression.

---

**Technical Design**

**Decisions**
- **Chose canonical explicit-deny `AuditImmutabilityDenyPolicy` in `themis-core` / not ad-hoc BEFE filter checks or narrowing `SystemAdminPolicy` directly**: `DeterministicPolicyEvaluator` already implements explicit-deny evaluation (Step 2 before Step 3 ordered allow policies). An explicit-deny policy cleanly enforces non-repudiation invariants for `AUDIT` targets across all roles without mutating role semantics in `SystemAdminPolicy` or bypassing canonical Themis in Iris BEFE.
- **Chose selective domain resolution in `ThemisClinicalAuthorizationFilter` / not separate JAX-RS filter or URI rework**: The filter already parses `resourceType` from the URI subpath; mapping `AuditEvent` (case-insensitive) to `HarmoniaSecurityLabelEnum.AUDIT` and other FHIR resources to `HarmoniaSecurityLabelEnum.CLINICAL` establishes domain isolation with minimal diff surface.

**Approach & Touches**
- **Themis Core (`themis/themis-core`)**:
    - Add `AuditImmutabilityDenyPolicy.java` in `net.fhirfactory.harmonia.themis.core.policy`:
        - Implements `ThemisPolicy` with `isExplicitDeny() == true`, `order = 10`, `domain = "AUDIT"`.
        - `appliesTo(request)`: returns true when `target.securityDomain == "AUDIT"` (or has label `AUDIT`) AND `action` is one of `CREATE`, `UPDATE`, `DELETE` (or not `READ`/`SEARCH`).
        - `evaluate(request)`: returns `ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, POLICY_ID, corrId, "External mutation of AUDIT records is strictly prohibited")`.
    - Register `new AuditImmutabilityDenyPolicy()` in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
    - Reference analog: `AuditReadPolicy.java` for domain matching and `ClinicalAuthorizationPolicy.java` for explicit DELETE denial pattern.
- **Iris BEFE (`iris/iris-befe`)**:
    - Update `ThemisClinicalAuthorizationFilter.java`:
        - In `filter()` around line 158–174, determine target domain and security label based on `resourceType`: if `"AuditEvent".equalsIgnoreCase(resourceType)`, use `HarmoniaSecurityLabelEnum.AUDIT`, else `HarmoniaSecurityLabelEnum.CLINICAL`.
        - Apply the resolved label/domain consistently to both `ThemisResource` and `ThemisSecurityContext`.
        - Retain HTTP method-to-action mapping (`GET` id == null -> `SEARCH`, `GET` id != null -> `READ`, `POST` -> `CREATE`, `PUT`/`PATCH` -> `UPDATE`, `DELETE` -> `DELETE`).
- **Architecture & Security Guardrails (`paradeigma/paradeigma-test`)**:
    - Update `SecurityEnforcementArchitectureTest.java` to verify that `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator` and that BEFE relies on canonical Themis without local audit bypasses.

**Nuances, Risks & Corner Cases**
- **PATCH Mapping Convention**: Themis action model has no `PATCH` action; filter maps `PATCH` -> `ThemisAction.UPDATE`. Immutability policy checks `UPDATE`, correctly denying both PUT and PATCH.
- **AuditReadPolicy Order**: `AuditReadPolicy` has order 100. Explicit-deny policies evaluate before Step 3 ordered policies, guaranteeing mutations are rejected before `SystemAdminPolicy` (order 10) or any allow policy is considered.
- **Resource Code Immutability**: `AuditEventResource.java` retains its existing endpoints during Step 04.3. No methods are deleted or refactored in this step; security is enforced strictly at the authorization filter boundary.

**Contracts**
```java
// Policy Contract
public class AuditImmutabilityDenyPolicy implements ThemisPolicy {
    public static final String POLICY_ID = "audit-immutability-deny-policy";
    public static final String DOMAIN = "AUDIT";

    @Override
    public String getPolicyId() { return POLICY_ID; }
    @Override
    public int getOrder() { return 10; }
    @Override
    public boolean isExplicitDeny() { return true; }
    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request);
    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request);
}
```

---

**Testing**

**Checklist**
- **Audit Read/Search Authorization**: Authenticated principal with `audit.read` (or `AUD_RDR` role) successfully executes `GET /api/fhir/AuditEvent` (SEARCH) and `GET /api/fhir/AuditEvent/123` (READ) -> 200/green.
- **Audit Mutation Denial**: Authenticated principal with `audit.read` receives 403 Forbidden on `POST`, `PUT`, `PATCH`, `DELETE` to `/api/fhir/AuditEvent` -> `OperationOutcome` with `ACTION_NOT_PERMITTED`.
- **System Admin Audit Immutability**: Principal with `system.admin` / `SYS_ADM` role is allowed `READ`/`SEARCH` but denied `POST`, `PUT`, `PATCH`, `DELETE` on `AuditEvent` -> 403 Forbidden.
- **Cross-Domain Isolation**: Principal with Clinical (`CLINICAL_WRITE`, `CLINICAL_ADMIN`), Operations (`OPS_ADM`, `OPS_VIEWER`), or Provider Registry roles is denied all actions on `AuditEvent` -> 403 Forbidden.
- **Clinical Resource Isolation**: Standard clinical resources (`Patient`, `Observation`) retain `CLINICAL` domain classification and allow clinical read/write roles as before.
- **Unauthenticated / Missing Principal**: Anonymous or missing principal returns 401 Unauthorized (`PRINCIPAL_MISSING`).
- **Anti-Spoofing & Header Isolation**: Injected `X-Harmonia-*` or caller-controlled headers do not alter SecurityContext identity or domain classification.

---

**Delivery Steps**

**✓ Step 1: Canonical Themis Audit Immutability Explicit-Deny Policy**  
Goal: Enforce fail-closed immutability for the `AUDIT` security domain within `themis-core` using the existing explicit-deny engine.  
Scope: `themis/themis-core`  
Acceptance Criteria:
- [ ] `AuditImmutabilityDenyPolicy` is implemented in `net.fhirfactory.harmonia.themis.core.policy` with `isExplicitDeny() == true` matching mutating actions (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
- [ ] Unit tests in `AuditImmutabilityDenyPolicyTest` verify explicit deny on mutating actions for any caller role (including `system.admin`).
- [ ] Evaluator tests in `DeterministicPolicyEvaluatorTest` confirm explicit-deny precedence over `SystemAdminPolicy` for AUDIT mutation actions while keeping AUDIT READ/SEARCH allowed.
  Verification: `mvn test -pl themis/themis-core -Dtest="AuditImmutabilityDenyPolicyTest,DeterministicPolicyEvaluatorTest,ClinicalAuthorizationPolicyTest,OperationsAuthorizationPolicyTest"` → green

**✓ Step 2: Iris BEFE AuditEvent Domain Convergence and Guardrails**  
Goal: Route BEFE `AuditEvent` requests into the `AUDIT` security domain, establish boundary tests, and enforce architecture guardrails.  
Scope: `iris/iris-befe`, `paradeigma/paradeigma-test`  
Acceptance Criteria:
- [ ] `ThemisClinicalAuthorizationFilter` dynamically classifies `AuditEvent` requests as `HarmoniaSecurityLabelEnum.AUDIT` and non-AuditEvent FHIR requests as `HarmoniaSecurityLabelEnum.CLINICAL` for both `ThemisResource` and `ThemisSecurityContext`.
- [ ] `ThemisClinicalAuthorizationFilterTest` validates all 15 required boundary scenarios (AuditEvent AUDIT classification, `audit.read` READ/SEARCH allow, mutation deny for all roles including `system.admin`, cross-domain isolation, anti-spoofing, and clinical regression prevention).
- [ ] `SecurityEnforcementArchitectureTest` asserts canonical Themis governance of `AuditEvent`, presence of `AuditImmutabilityDenyPolicy`, and absence of ad-hoc Iris audit authorization bypasses.
  Verification: `mvn test -pl iris/iris-befe,paradeigma/paradeigma-test -Dtest="ThemisClinicalAuthorizationFilterTest,SecurityEnforcementArchitectureTest"` → green



**Summary**
- Converged the Iris BEFE `AuditEvent` authorization boundary so external FHIR `AuditEvent` is now governed as `AUDIT`, not `CLINICAL`, with canonical Themis evaluation.
- Enforced the Step 04.3 invariant: external `READ`/`SEARCH` require canonical audit authorization, while external `CREATE`/`UPDATE`/`PATCH`/`DELETE` fail closed through Themis, including for `system.admin`.

**Changes**
- Added canonical `AuditImmutabilityDenyPolicy` in `themis-core` and registered it in `DeterministicPolicyEvaluator` so AUDIT-domain mutation is explicitly denied before `SystemAdminPolicy` or other allow policies.
- Updated `ThemisClinicalAuthorizationFilter` to classify `AuditEvent` requests as `AUDIT` for both `ThemisResource` and `ThemisSecurityContext`, while preserving ordinary clinical FHIR resources as `CLINICAL`.
- Expanded `ThemisClinicalAuthorizationFilterTest`, `DeterministicPolicyEvaluatorTest`, added `AuditImmutabilityDenyPolicyTest`, and strengthened `SecurityEnforcementArchitectureTest` to cover audit-reader access, mutation denial across roles, 401/403 boundaries, anti-spoofing, clinical non-regression, and canonical guardrails.

**Verification**
- Passed targeted runtime suites after full rebuild: `ThemisClinicalAuthorizationFilterTest`, `SecurityEnforcementArchitectureTest`, `DeterministicPolicyEvaluatorTest`, and `AuditImmutabilityDenyPolicyTest` all completed successfully (exit code 0).
- Verified requested semantics: previous AuditEvent classification was CLINICAL via generic `/api/fhir/*`; new classification is AUDIT for `/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}`; PATCH remains mapped to `UPDATE`; missing principal stays 401 and authenticated unauthorized callers stay 403.

**Notes**
- Step 04.4+ work was not performed: `AuditEventResource` implementation, Kleio routing, cache/write-behind retirement, Mnemosyne mutation behavior, UI, and database immutability/storage changes were left untouched as required.

