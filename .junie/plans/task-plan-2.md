---
sessionId: session-260922-103004-hkh5
---

# Delivery Steps

### ✓ Step 1: Implementation
<plan_task>
HARMONIA SECURITY — TASK 03
Clinical Authorisation Policy

Goal

Implement the Clinical authorisation policy used by Iris BEFE after successful
container-managed authentication.

Task 01 established the trusted Clinical authorisation boundary in
ThemisClinicalAuthorizationFilter.

Task 02 established container-managed OIDC/OAuth2 bearer authentication in
WildFly. A successfully authenticated caller is exposed to Iris BEFE through
Jakarta/JAX-RS SecurityContext.getUserPrincipal(), using the validated OIDC
`sub` claim, and is represented by Themis as PrincipalType.HUMAN.

Do not revisit or redesign either of those boundaries.

The purpose of this task is to define and implement what an authenticated HUMAN
principal is authorised to do against the Clinical FHIR API.


Architecture intent

Maintain the separation:

    WildFly / Elytron OIDC
            |
            | authenticated identity
            v
    Jakarta SecurityContext
            |
            v
    ThemisClinicalAuthorizationFilter
            |
            | ThemisPrincipal(HUMAN)
            v
    Clinical Authorisation Policy
            |
            +---- PERMIT
            |
            +---- DENY

Authentication answers:

    "Who is this caller?"

Themis answers:

    "Is this authenticated caller permitted to perform this action
     on this Clinical resource?"

FHIR REST resources must not contain their own authorisation policy.


Before making changes

Inspect the existing repository and determine:

1. the current Themis authorisation model and policy abstractions;
2. how roles/permissions/actions/resources are currently represented;
3. any existing Clinical role vocabulary;
4. how ThemisClinicalAuthorizationFilter constructs the authorisation request;
5. how HTTP/FHIR operations currently map to Themis actions;
6. whether policy is currently hard-coded, configured, registered or discovered;
7. existing tests and architecture rules protecting the security boundary.

Do not invent a second authorisation framework if the repository already
contains an appropriate Themis mechanism.


Required Clinical policy

Establish a small initial role vocabulary suitable for proving the
authorisation model.

Use these roles unless the existing repository already defines an equivalent
approved vocabulary:

    CLINICAL_READ
    CLINICAL_WRITE
    CLINICAL_ADMIN

Initial semantics:

CLINICAL_READ
    May perform read/search operations against Clinical FHIR resources.

CLINICAL_WRITE
    Includes CLINICAL_READ capability and may create/update Clinical FHIR
    resources.

CLINICAL_ADMIN
    Includes CLINICAL_WRITE capability and may perform administrative Clinical
    operations where such operations already exist.

Do not introduce delete permission merely because FHIR supports DELETE.
Determine whether deletion is actually supported/appropriate in the existing
Clinical API before granting it.

If the existing Themis model supports permissions/capabilities independently
of roles, prefer:

    role -> permission/capability -> action

rather than scattering direct role-name checks throughout application code.


FHIR action mapping

Inspect the existing implementation and establish an explicit mapping between
FHIR/HTTP operations and Themis actions.

At minimum consider:

    GET    resource instance     -> READ
    GET    search                -> SEARCH
    POST   create                -> CREATE
    PUT    update                -> UPDATE

Do not assume DELETE, PATCH, conditional operations, history, transaction,
batch or custom FHIR operations are authorised simply because an HTTP method
exists.

Where these operations already exist, identify them and either map them
explicitly or retain fail-closed behaviour pending a later decision.


Security requirements

The implementation MUST:

- remain default-deny;
- operate only on identities established by the trusted Task 02 container
  authentication boundary;
- preserve PrincipalType.HUMAN;
- preserve Task 01 anti-spoofing behaviour;
- prevent caller-controlled HTTP headers from establishing roles or
  permissions;
- centralise Clinical authorisation decisions in Themis;
- keep FHIR REST resources free of duplicated role checks;
- distinguish authentication failure (401) from authenticated-but-not-authorised
  failure (403);
- return the existing FHIR OperationOutcome form for Themis authorisation
  denials;
- avoid identity-provider-specific application logic.

Do NOT parse OIDC/JWT tokens in application code.


Important role-source question

Do not assume that OIDC token role claims are the authoritative source of
Clinical roles.

During exploration, determine how the current architecture expects an
authenticated HUMAN principal to acquire Themis roles/permissions.

If this is not yet defined, STOP at that architectural boundary and report:

- what mechanisms already exist;
- what information Themis currently requires;
- viable contained options;
- the smallest recommended approach.

Do not silently map arbitrary OIDC claims, Entra groups, Keycloak roles or
caller-supplied headers into Clinical permissions.


Testing

Add focused tests proving at least:

1. authenticated HUMAN with no Clinical permission -> 403;
2. CLINICAL_READ permits read/search;
3. CLINICAL_READ does not permit create/update;
4. CLINICAL_WRITE permits read/search/create/update;
5. CLINICAL_ADMIN includes the approved Clinical capabilities;
6. unknown role -> no additional access;
7. unknown/unmapped action -> deny;
8. unknown/unmapped resource -> deny where applicable;
9. spoofed X-Harmonia-* / role headers cannot elevate access;
10. unauthenticated principal behaviour from Task 01 remains unchanged;
11. existing architecture/security tests continue to pass.

Explicitly test the default-deny cases. They are part of the security model,
not incidental error behaviour.


Scope

IN SCOPE:

- Themis Clinical authorisation policy;
- Clinical roles/capabilities;
- mapping existing Clinical FHIR operations to Themis actions;
- centralised policy evaluation;
- focused unit/integration tests;
- minimal changes required to connect the existing Task 01 filter to the
  Clinical policy.

OUT OF SCOPE:

- WildFly/OIDC authentication changes;
- JWT parsing or validation;
- Entra/Keycloak/Auth0-specific integration;
- Operations API authorisation;
- Pylai authentication/authorisation remediation;
- Agora/Matrix security;
- service/workload identity;
- audit redesign;
- PHI logging;
- CORS;
- ingress/TLS;
- UI authorisation;
- broader provider/organisation governance;
- unrelated refactoring.


Working method

Treat this initially as an exploration task.

First inspect the repository and report:

1. current Themis policy architecture;
2. existing roles/actions/permissions and where they are defined;
3. current Clinical authorisation request flow;
4. how roles/permissions currently reach ThemisPrincipal;
5. gaps or conflicts with the target above;
6. smallest implementation approach;
7. exact files/modules likely to change;
8. proposed tests;
9. any architectural decision that must be made before implementation.

Do not modify files during this exploration.

Once the exploration is complete, stop and return the findings so the
implementation can be broken into small delivery steps.

Keep the scope strictly to Task 03.
</plan_task>

<plan_follow_up>
HARMONIA SECURITY — TASK 03 / STEP 1
Clinical Security Vocabulary

Implement only the Clinical role and authority vocabulary required by the
Clinical authorisation policy.

Do not implement ClinicalAuthorizationPolicy yet.
Do not modify ThemisClinicalAuthorizationFilter.
Do not modify authentication/OIDC configuration.

Add the following Clinical roles to the existing Harmonia security vocabulary:

    CLINICAL_READ
    CLINICAL_WRITE
    CLINICAL_ADMIN

Add granular Clinical authorities:

    clinical.read
    clinical.search
    clinical.create
    clinical.update
    clinical.admin

Role mappings:

    CLINICAL_READ
        clinical.read
        clinical.search

    CLINICAL_WRITE
        clinical.read
        clinical.search
        clinical.create
        clinical.update

    CLINICAL_ADMIN
        clinical.read
        clinical.search
        clinical.create
        clinical.update
        clinical.admin

Do NOT add clinical.delete.

DELETE must therefore remain fail-closed when the Clinical policy is
implemented in the next step.

Use the existing role -> authority structures and naming conventions already
used by Harmonia. Do not introduce another role or permission abstraction.

Expected implementation surface:

    calliope/.../HarmoniaAuthorityEnum.java
    calliope/.../HarmoniaRoleEnum.java
    themis/themis-core/.../HarmoniaSecurityConstants.java

Add/update focused tests where the existing security vocabulary is tested.

Important architectural boundary:

Themis trusts roles supplied by the authenticated container through
SecurityContext.isUserInRole(...).

Do not define how OIDC claims, Entra groups, Keycloak roles or other external
identity-provider constructs are mapped into those container roles as part of
this step.

Verification should demonstrate:

- CLINICAL_READ contains only read/search authorities.
- CLINICAL_WRITE contains read/search/create/update.
- CLINICAL_ADMIN contains the approved Clinical authorities.
- no Clinical role receives delete authority.
- existing Provider Registry, Audit and System roles remain unchanged.
- relevant module tests pass.

Keep the implementation strictly limited to Task 03 Step 1.

Do not run git commit, git push, create branches or create pull requests.

Report:
1. files changed;
2. vocabulary added;
3. tests added/updated;
4. exact verification commands and results;
5. any unexpected dependency or architectural issue discovered.
</plan_follow_up>

This task has no prior planning phase. Before implementation, analyze the task and codebase, define acceptance criteria if not explicitly provided in the task description, and plan your approach. 
The Reviewer must independently define its own acceptance criteria and will verify them.

### ✓ Step 2: Update / Follow-up
HARMONIA SECURITY — TASK 03 / STEP 2
Clinical Authorisation Policy

Implement the Themis Clinical authorisation policy using the Clinical
security vocabulary completed in Task 03 Step 1.

Keep this step focused on policy evaluation.

Do not modify:
- WildFly/OIDC authentication;
- ThemisClinicalAuthorizationFilter;
- FHIR REST resources;
- external identity-provider role mapping;
- Operations/Pylai security;
- audit, CORS or ingress/TLS configuration.

Goal

Implement a dedicated ClinicalAuthorizationPolicy within Themis that
authorises Clinical-domain actions using the existing granular
clinical.* authorities.

The policy must operate on ThemisAuthorizationRequest and use the
existing Themis policy/evaluator abstractions.

Do not introduce direct role-name checks into the policy.

The policy should evaluate authorities/capabilities, not roles.

Required mappings

For a resource in the CLINICAL security domain:

    ThemisAction.READ
        requires clinical.read

    ThemisAction.SEARCH
        requires clinical.search

    ThemisAction.CREATE
        requires clinical.create

    ThemisAction.UPDATE
        requires clinical.update

Administrative actions should require:

        clinical.admin

DELETE must remain denied.

There is deliberately no clinical.delete authority.

Do not make CLINICAL_ADMIN implicitly bypass the authority model.
Its permissions arise from the authorities assigned to the role in
Task 03 Step 1.

Security behaviour

The policy MUST remain fail-closed.

It must DENY when:

- the principal is missing;
- required request information is missing;
- the target is not a Clinical resource;
- the caller has no required Clinical authority;
- the action is unknown or unsupported;
- the action is DELETE;
- no explicit Clinical allow rule matches.

Do not infer permissions from:
- role names;
- HTTP headers;
- JWT/OIDC claims;
- identity-provider groups;
- PrincipalType alone.

Only authorities already present in ThemisAuthorizationRequest may
contribute to the decision.

Policy applicability

The policy must apply only to resources belonging to the CLINICAL
security domain / Clinical security label using the existing Harmonia
security-domain conventions.

It must not grant access to Provider Registry, Operations, Audit,
Administrative or other security domains.

Integration

Register ClinicalAuthorizationPolicy using the existing Themis policy
registration/evaluator mechanism.

Do not replace the existing deterministic policy evaluator and do not
create a second authorisation path.

If BEFE's existing DefaultThemisAuthorizer prevents the registered
Clinical policy from actually being evaluated, inspect that boundary
and report the smallest required change before modifying BEFE.

Do not broaden this step merely to make integration convenient.

Testing

Add focused Themis-core tests for ClinicalAuthorizationPolicy.

At minimum verify:

1. clinical.read + READ -> ALLOW
2. clinical.search + SEARCH -> ALLOW
3. clinical.create + CREATE -> ALLOW
4. clinical.update + UPDATE -> ALLOW
5. appropriate clinical.admin + supported administrative action -> ALLOW

And default-deny cases:

6. no authorities + READ -> DENY
7. clinical.read + CREATE -> DENY
8. clinical.create + READ -> DENY
9. clinical.update + DELETE -> DENY
10. clinical.admin + DELETE -> DENY
11. unknown/unmapped action -> DENY
12. non-Clinical resource -> policy must not grant access
13. missing principal/request/resource information -> DENY according to
    existing Themis fail-closed conventions
14. Provider/Audit/System authorities must not grant Clinical access

Also confirm that the role vocabulary from Step 1 produces the expected
policy outcomes:

    CLINICAL_READ
        READ, SEARCH -> ALLOW
        CREATE, UPDATE, DELETE -> DENY

    CLINICAL_WRITE
        READ, SEARCH, CREATE, UPDATE -> ALLOW
        DELETE -> DENY

    CLINICAL_ADMIN
        READ, SEARCH, CREATE, UPDATE and approved ADMINISTER -> ALLOW
        DELETE -> DENY

Do not test the JAX-RS filter in this step. That belongs to Task 03
Step 3.

Expected implementation surface

Primarily:

    themis/themis-core/.../policy/ClinicalAuthorizationPolicy.java

plus:

    existing Themis policy registration/evaluator wiring
    focused themis-core policy tests

Avoid changes outside themis-core unless they are genuinely required
for policy registration. If such a dependency is discovered, stop and
report it rather than expanding scope silently.

Verification

Run narrow Themis-core tests first.

Then run the complete themis-core test suite and the existing
architecture tests.

Do not run broad full-repository tests unless required by an actual
dependency.

Do not run git commit, git push, git checkout, create branches or
create pull requests.

Report:

1. files changed;
2. Clinical policy behaviour implemented;
3. how the policy is registered/discovered;
4. tests added;
5. exact verification commands and results;
6. any integration issue discovered with DefaultThemisAuthorizer;
7. any unexpected architectural issue.

### ! Step 3: Update / Follow-up
HARMONIA SECURITY — TASK 03 / STEP 3
Connect Iris Clinical Boundary to Themis Policy Evaluation

Goal

Complete Task 03 by connecting the existing
ThemisClinicalAuthorizationFilter to the deterministic Themis policy
evaluation pipeline established in Step 2.

Do not redesign authentication, Clinical policy, role vocabulary or FHIR
resources.

The intended runtime path is:

    WildFly / Elytron OIDC
            |
            v
    Jakarta SecurityContext
            |
            v
    ThemisClinicalAuthorizationFilter
            |
            | ThemisAuthorizationRequest
            v
    ThemisAuthorizer
            |
            v
    DeterministicPolicyEvaluator
            |
            v
    ClinicalAuthorizationPolicy
            |
            +---- ALLOW
            |
            +---- DENY -> HTTP 403 OperationOutcome


Known state

Task 01 established ThemisClinicalAuthorizationFilter and the trusted
anti-spoofing boundary.

Task 02 established container-managed OIDC authentication.

Task 03 Step 1 established:

    CLINICAL_READ
    CLINICAL_WRITE
    CLINICAL_ADMIN

and the corresponding granular clinical.* authorities.

Task 03 Step 2 established ClinicalAuthorizationPolicy and registered it
with DeterministicPolicyEvaluator.withDefaultPolicies().

Step 2 discovered that iris-befe DefaultThemisAuthorizer currently
evaluates only the Operations RBAC policy and therefore does not reach
ClinicalAuthorizationPolicy.

This step fixes that integration boundary.


Implementation approach

Inspect DefaultThemisAuthorizer and the ThemisAuthorizer abstraction before
modifying anything.

Make the smallest coherent change that causes Themis authorization requests
to be evaluated through the existing DeterministicPolicyEvaluator.

Prefer making DefaultThemisAuthorizer delegate to:

    DeterministicPolicyEvaluator.withDefaultPolicies()

rather than adding Clinical-specific branching such as:

    if clinical -> ClinicalAuthorizationPolicy
    else -> Operations policy

The authorizer should remain domain-neutral where practical.

Do not create a second Clinical-specific authorizer unless the existing
architecture genuinely requires one.


Important compatibility requirement

DefaultThemisAuthorizer may currently provide Operations behaviour that is
relied upon elsewhere.

Before replacing or changing that behaviour:

1. identify all usages of DefaultThemisAuthorizer;
2. identify which existing Operations tests depend on it;
3. determine whether DeterministicPolicyEvaluator already provides equivalent
   Operations behaviour;
4. preserve existing Operations behaviour.

If the existing Operations RBAC policy is NOT represented in the deterministic
evaluator, do not silently remove it.

In that case, stop and report the smallest architecture-safe integration
option before making a broad change.


Clinical filter behaviour

Do not redesign ThemisClinicalAuthorizationFilter.

It should continue to:

- trust only container-established SecurityContext identity;
- create PrincipalType.HUMAN;
- extract only trusted container roles through isUserInRole(...);
- convert those roles to existing Harmonia/Themis authorities;
- map HTTP/FHIR requests to Themis actions/resources;
- invoke ThemisAuthorizer;
- return the existing FHIR OperationOutcome on authorization denial;
- preserve the Task 01 anti-spoofing boundary.

Do not parse JWT/OIDC claims.

Do not consume X-Harmonia-* or other caller-controlled identity/role headers.


Required end-to-end authorization outcomes

For an authenticated HUMAN:

CLINICAL_READ

    GET read/search        -> permitted
    POST create            -> denied
    PUT update             -> denied
    DELETE                 -> denied

CLINICAL_WRITE

    GET read/search        -> permitted
    POST create            -> permitted
    PUT update             -> permitted
    DELETE                 -> denied

CLINICAL_ADMIN

    GET read/search        -> permitted
    POST create            -> permitted
    PUT update             -> permitted
    approved administrative action -> permitted where such mapping exists
    DELETE                 -> denied

Authenticated HUMAN with no Clinical role:

    Clinical request -> denied

Unknown/untrusted role:

    must not grant Clinical access


HTTP behaviour

Preserve the existing authentication/authorization distinction:

    missing/invalid authentication
        -> rejected by WildFly authentication boundary
        -> HTTP 401

    successfully authenticated but not authorized
        -> rejected by Themis
        -> HTTP 403 with existing FHIR OperationOutcome

Do not move authentication handling into application code merely to test 401.


Anti-spoofing

Explicitly retain and test the Task 01 anti-spoofing behaviour.

Caller-supplied headers such as:

    X-Harmonia-Principal
    X-Harmonia-Roles
    X-Harmonia-Authorities

or equivalent attacker-controlled values must not alter the trusted
ThemisPrincipal or its authorities.

A caller without CLINICAL_WRITE must not obtain write access by supplying
such headers.


Action mapping

Preserve the existing HTTP/FHIR action mapping unless a defect is discovered.

In particular, inspect and document the current PATCH behaviour.

Step 2 did not change the existing filter mapping.

Do not broaden DELETE access.

DELETE must continue through Themis and fail closed because no
clinical.delete authority/policy exists.


Testing

Add focused iris-befe tests proving the complete trusted boundary.

At minimum test:

1. CLINICAL_READ + GET/read -> allowed.
2. CLINICAL_READ + GET/search -> allowed.
3. CLINICAL_READ + POST -> 403.
4. CLINICAL_READ + PUT -> 403.
5. CLINICAL_WRITE + GET -> allowed.
6. CLINICAL_WRITE + POST -> allowed.
7. CLINICAL_WRITE + PUT -> allowed.
8. CLINICAL_WRITE + DELETE -> 403.
9. CLINICAL_ADMIN + approved operations -> allowed.
10. CLINICAL_ADMIN + DELETE -> 403.
11. authenticated HUMAN with no Clinical role -> 403.
12. unrelated Provider/Audit/System role does not grant Clinical access,
    except for any explicitly documented SystemAdmin behaviour already
    provided by existing Themis policy.
13. unknown role does not grant access.
14. spoofed identity/role/authority headers do not elevate access.
15. missing principal continues to follow the existing unauthenticated
    behaviour established by Task 01/Task 02.
16. existing Operations authorization behaviour remains unchanged.

Where tests use mocked SecurityContext, clearly state that they test the
application authorization boundary and not WildFly JWT validation.


Architecture verification

Confirm that:

- FHIR REST resources contain no authorization policy;
- ThemisClinicalAuthorizationFilter remains the Clinical JAX-RS enforcement
  boundary;
- ClinicalAuthorizationPolicy remains in themis-core;
- Iris BEFE depends only on the Themis authorizer/evaluator abstractions;
- no JWT parsing has entered application code;
- no IdP-specific role mapping has entered application code;
- default-deny behaviour remains intact.


Scope

IN SCOPE:

- DefaultThemisAuthorizer / Themis evaluator delegation required to reach the
  Clinical policy;
- minimal CDI/wiring changes required for that delegation;
- focused iris-befe integration/security tests;
- preservation tests for existing Operations authorization;
- architecture tests where appropriate.

OUT OF SCOPE:

- changes to Clinical role vocabulary;
- changes to ClinicalAuthorizationPolicy semantics;
- WildFly/OIDC configuration;
- Entra/Keycloak/Auth0 role mapping;
- FHIR REST resource redesign;
- Pylai authorization;
- Agora/Matrix security;
- service/workload identity;
- UI authorization;
- PHI logging;
- audit redesign;
- CORS;
- ingress/TLS;
- unrelated refactoring.


Verification

Run focused iris-befe security tests first.

Then run:

- complete iris-befe tests;
- themis-core tests;
- architecture tests.

Do not run the entire repository unless an actual dependency requires it.

Do not run git commit, git push, git checkout, create branches or create
pull requests.


Report

1. files changed;
2. how DefaultThemisAuthorizer now delegates/evaluates requests;
3. confirmation that ClinicalAuthorizationPolicy is reached;
4. confirmation that existing Operations behaviour remains intact;
5. Clinical role/action outcomes demonstrated;
6. anti-spoofing outcomes demonstrated;
7. 401 vs 403 behaviour preserved;
8. exact test commands and results;
9. any architectural issue discovered;
10. whether Task 03 can now be considered complete.

### ✓ Step 4: Update / Follow-up
HARMONIA SECURITY — TASK 03 / STEP 3A
Operations Policy Migration

Goal

Remove the architecture blocker discovered during Task 03 Step 3 by
representing the existing Iris BEFE Operations authorization behaviour
within the standard Themis deterministic policy evaluation model.

This is a prerequisite to completing Step 3.

Do NOT switch DefaultThemisAuthorizer to DeterministicPolicyEvaluator yet.
That will occur only after this step proves that existing Operations
behaviour has been preserved.


Background

Step 3 discovered that DefaultThemisAuthorizer currently contains
Operations-specific authorization behaviour that is not represented by:

    DeterministicPolicyEvaluator.withDefaultPolicies()

Replacing DefaultThemisAuthorizer with the deterministic evaluator now
would therefore regress existing Operations authorization.

The target architecture is:

    ThemisAuthorizationRequest
            |
            v
    DeterministicPolicyEvaluator
            |
            +-- SystemAdminPolicy
            +-- Provider policies
            +-- Audit policies
            +-- ClinicalAuthorizationPolicy
            +-- OperationsAuthorizationPolicy
            +-- ...
            |
            v
        ALLOW / DENY

Operations authorization must therefore become a normal Themis policy,
not special behaviour embedded in Iris BEFE.


Before making changes

Inspect the current implementation of DefaultThemisAuthorizer and identify
the complete existing Operations authorization semantics.

Explicitly document:

1. Operations authorities currently accepted;
2. System/integration authorities currently accepted;
3. Operations roles currently accepted;
4. any principal attribute fallback behaviour;
5. which Operations resources/actions are currently protected;
6. existing Operations tests that define current behaviour;
7. any behaviour that appears broader than the intended Operations domain.

Do not silently preserve accidental cross-domain authorization behaviour.

If existing DefaultThemisAuthorizer behaviour currently authorizes
Operations authorities without considering resource domain or action,
distinguish:

    a. intended Operations authorization semantics; from
    b. implementation leakage caused by the old BEFE-local authorizer.

Report that distinction before broadening any new Themis policy.


Implementation

Create an OperationsAuthorizationPolicy in themis-core using the existing:

    ThemisAuthorizationPolicy
    ThemisAuthorizationRequest
    ThemisAuthorizationDecision

and deterministic policy conventions.

The policy must be scoped to the existing Operations security
domain/resource conventions.

Do not introduce another policy framework.

Register OperationsAuthorizationPolicy with:

    DeterministicPolicyEvaluator.withDefaultPolicies()

using an appropriate deterministic order consistent with the existing
policies.


Compatibility

Preserve legitimate existing Operations behaviour relied upon by BEFE.

At minimum investigate the behaviour associated with:

    operations.read
    operations.admin
    system.integration
    OPS_VIEWER
    OPS_ADM
    SYS_INT
    SYS_ADM

Use the existing Harmonia role -> authority model wherever possible.

Prefer authorization based upon authorities already present in
ThemisAuthorizationRequest rather than direct role-name checks.

Do not introduce new role vocabulary unless genuinely required.


Important principal attribute behaviour

Step 3 discovered that DefaultThemisAuthorizer also accepts values from:

    principal.attributes().get("role")

for some Operations/System roles.

Inspect why this exists.

Do NOT automatically reproduce this fallback in the new policy.

Determine whether it is:

    - legitimate trusted identity information;
    - historical compatibility behaviour;
    - redundant with the current role/authority model; or
    - an unsafe alternate authorization path.

If removing it would cause a real compatibility change, report that
explicitly before changing the behaviour.

The preferred architecture remains:

    trusted container role
        -> Harmonia role
        -> Harmonia authorities
        -> ThemisAuthorizationRequest
        -> policy

rather than policies interpreting arbitrary principal attributes.


Security requirements

OperationsAuthorizationPolicy must:

- be default-deny;
- apply only to the intended Operations domain/resources;
- evaluate authorities rather than caller-controlled headers;
- not parse JWT/OIDC claims;
- not infer authorization from PrincipalType alone;
- not grant Clinical access;
- not grant Provider Registry access;
- not grant Audit access unless an existing explicit cross-domain policy
  already requires it;
- preserve SystemAdmin behaviour only through the existing SystemAdminPolicy
  where appropriate.


Clinical isolation

Explicitly prove that Operations authorities cannot authorize Clinical
resources.

For example:

    operations.read + Clinical READ       -> DENY
    operations.admin + Clinical UPDATE    -> DENY
    system.integration + Clinical READ    -> DENY

except where an existing higher-priority SystemAdminPolicy explicitly
provides SYS_ADM behaviour.

Do not change ClinicalAuthorizationPolicy.


Testing

Add focused themis-core tests for OperationsAuthorizationPolicy.

At minimum prove:

1. legitimate Operations read authority permits the corresponding
   Operations read action;
2. legitimate Operations admin authority permits approved Operations
   administrative actions;
3. system integration authority retains its intended Operations/system
   behaviour where applicable;
4. insufficient Operations authority -> DENY;
5. unknown authority -> DENY;
6. unsupported action -> DENY;
7. non-Operations resource -> policy does not grant;
8. Operations authority cannot grant Clinical access;
9. Operations authority cannot grant Provider Registry access;
10. Operations authority cannot grant Audit access;
11. missing principal/request/resource information -> fail closed;
12. existing SYS_ADM semantics remain governed by SystemAdminPolicy.

Where existing BEFE tests define legitimate Operations behaviour, reproduce
equivalent policy-level cases in themis-core.


Do not yet modify

Do NOT modify:

- ThemisClinicalAuthorizationFilter;
- ClinicalAuthorizationPolicy;
- Clinical role vocabulary;
- WildFly/OIDC configuration;
- FHIR resources;
- external IdP mappings;
- Pylai;
- Agora;
- audit;
- CORS;
- ingress/TLS.

Most importantly:

DO NOT YET replace DefaultThemisAuthorizer's implementation with the
deterministic evaluator.

This step establishes equivalence first.


Verification

Run focused OperationsAuthorizationPolicy tests.

Then run:

- complete themis-core tests;
- existing iris-befe Operations authorization tests;
- architecture tests.

Do not run the complete repository unless an actual dependency requires it.

Do not run git commit, git push, git checkout, create branches or create
pull requests.


Report

1. existing Operations semantics discovered;
2. any distinction between intended policy and legacy implementation leakage;
3. files changed;
4. OperationsAuthorizationPolicy behaviour;
5. how it is registered in DeterministicPolicyEvaluator;
6. treatment of principal.attributes()["role"];
7. compatibility with existing Operations behaviour;
8. proof that Operations authorities do not grant Clinical access;
9. exact test commands and results;
10. whether the architecture blocker identified in Step 3 has now been removed.

### ✓ Step 5: Update / Follow-up
HARMONIA SECURITY — TASK 03 / STEP 3B
Complete Iris BEFE Themis Policy Integration

Goal

Complete the previously paused Task 03 Step 3.

Task 03 Step 3A has removed the architecture blocker by implementing
OperationsAuthorizationPolicy in themis-core and registering it with:

    DeterministicPolicyEvaluator.withDefaultPolicies()

The deterministic evaluator now contains the required Operations policy
coverage as well as the Clinical policy established in Step 2.

Complete the smallest remaining integration change so Iris BEFE uses the
standard deterministic Themis policy evaluation pipeline.

Do not redesign any security boundary.


Target runtime path

    WildFly / Elytron OIDC
            |
            v
    Jakarta SecurityContext
            |
            v
    ThemisClinicalAuthorizationFilter
            |
            | ThemisAuthorizationRequest
            v
    ThemisAuthorizer
            |
            v
    DeterministicPolicyEvaluator
            |
            +-- SystemAdminPolicy
            +-- Provider policies
            +-- Audit policies
            +-- ClinicalAuthorizationPolicy
            +-- OperationsAuthorizationPolicy
            +-- ...
            |
            v
        ALLOW / DENY


Implementation

Modify DefaultThemisAuthorizer so that it delegates authorization decisions
to:

    DeterministicPolicyEvaluator.withDefaultPolicies()

DefaultThemisAuthorizer should become a thin BEFE adapter around the
standard Themis evaluator.

Do NOT reproduce its existing Operations-specific authorization logic
alongside the evaluator.

Do NOT add domain branching such as:

    if Clinical -> ClinicalAuthorizationPolicy
    if Operations -> OperationsAuthorizationPolicy

Policy selection and evaluation belong to the deterministic evaluator.

The BEFE authorizer must remain domain-neutral.


Legacy behaviour

Task 03 Step 3A established that legitimate Operations authorization
semantics are now represented by OperationsAuthorizationPolicy.

Therefore remove/bypass the legacy authorization behaviour previously
embedded in DefaultThemisAuthorizer where it is replaced by the
deterministic evaluator.

In particular, do NOT preserve:

    principal.attributes()["role"]

as an alternate authorization path merely for backwards compatibility.

Authorization should follow the standard model:

    trusted identity / trusted container role
            |
            v
    Harmonia role
            |
            v
    Harmonia authorities
            |
            v
    ThemisAuthorizationRequest
            |
            v
    DeterministicPolicyEvaluator
            |
            v
    domain policy
            |
            v
        ALLOW / DENY

Do not copy legacy cross-domain or any-action leakage into the new path.


Clinical boundary

Do not redesign ThemisClinicalAuthorizationFilter.

It must continue to:

- trust only the container-established SecurityContext identity;
- construct PrincipalType.HUMAN;
- obtain roles only through SecurityContext.isUserInRole(...);
- convert trusted Harmonia roles to their existing authorities;
- construct the ThemisAuthorizationRequest;
- map the existing HTTP/FHIR operation to the appropriate ThemisAction;
- invoke ThemisAuthorizer;
- return the existing FHIR OperationOutcome for authorization denial;
- preserve the existing anti-spoofing boundary.

Do not parse JWT/OIDC claims.

Do not consume caller-controlled X-Harmonia-* or equivalent headers for
identity, roles or authorities.


Clinical outcomes

Verify the completed path provides:

CLINICAL_READ

    READ        -> ALLOW
    SEARCH      -> ALLOW
    CREATE      -> DENY
    UPDATE      -> DENY
    DELETE      -> DENY

CLINICAL_WRITE

    READ        -> ALLOW
    SEARCH      -> ALLOW
    CREATE      -> ALLOW
    UPDATE      -> ALLOW
    DELETE      -> DENY

CLINICAL_ADMIN

    READ        -> ALLOW
    SEARCH      -> ALLOW
    CREATE      -> ALLOW
    UPDATE      -> ALLOW
    approved ADMINISTER -> ALLOW where an existing mapping exists
    DELETE      -> DENY

Authenticated HUMAN with no Clinical authority:

    Clinical request -> DENY

Unknown/untrusted role:

    Clinical request -> DENY


Operations compatibility

Verify that the Operations behaviour intentionally migrated during
Step 3A is now reached through:

    DefaultThemisAuthorizer
        -> DeterministicPolicyEvaluator
        -> OperationsAuthorizationPolicy

Do not require byte-for-byte reproduction of legacy
DefaultThemisAuthorizer behaviour where Step 3A explicitly identified that
behaviour as cross-domain leakage or an unsafe alternate authorization
path.

Compatibility means preserving the legitimate Operations semantics defined
by OperationsAuthorizationPolicy.

Confirm that:

    operations.read
    operations.admin
    system.integration

behave according to the policy established in Step 3A.

System administrator behaviour must continue to be governed by the existing
SystemAdminPolicy.


Cross-domain isolation

Explicitly verify that:

    Operations authority -> Clinical resource       DENY
    Operations authority -> Provider resource       DENY
    Operations authority -> Audit resource          DENY

and similarly that Clinical authorities do not grant Operations access.

Do not weaken existing Provider or Audit policy boundaries.


PATCH and DELETE

Retain the existing HTTP/FHIR mapping established by the Clinical filter.

Document the current PATCH mapping in the completion report.

Do not introduce clinical.delete.

DELETE must continue to reach Themis and fail closed for Clinical resources.


401 / 403 boundary

Preserve:

    authentication failure
        -> WildFly / Elytron
        -> HTTP 401

    authenticated but unauthorized
        -> Themis
        -> HTTP 403
        -> existing FHIR OperationOutcome

Do not add application-level JWT validation or authentication handling.


Testing

Update/add focused iris-befe tests for the completed integration.

At minimum demonstrate:

1. CLINICAL_READ permits read/search;
2. CLINICAL_READ denies create/update/delete;
3. CLINICAL_WRITE permits read/search/create/update;
4. CLINICAL_WRITE denies delete;
5. CLINICAL_ADMIN provides its approved authorities but not delete;
6. authenticated HUMAN without Clinical authority -> 403;
7. unknown role -> 403;
8. unrelated Operations/Provider/Audit authority cannot grant Clinical access;
9. Clinical authority cannot grant Operations access;
10. spoofed X-Harmonia-* identity/role/authority headers cannot elevate access;
11. missing principal retains the existing unauthenticated behaviour;
12. legitimate Operations behaviour from Step 3A remains available through
    DefaultThemisAuthorizer;
13. principal.attributes()["role"] is not an authorization bypass;
14. unknown/unmapped actions remain fail-closed;
15. DELETE remains fail-closed for Clinical resources.

Where SecurityContext is mocked, describe these tests accurately as
application authorization-boundary tests, not WildFly OIDC/JWT integration
tests.


Architecture verification

Confirm after implementation:

    DefaultThemisAuthorizer
            |
            v
    DeterministicPolicyEvaluator.withDefaultPolicies()

and that there is no remaining BEFE-local policy implementation inside
DefaultThemisAuthorizer.

Also confirm:

- ClinicalAuthorizationPolicy remains in themis-core;
- OperationsAuthorizationPolicy remains in themis-core;
- FHIR resources contain no authorization policy;
- ThemisClinicalAuthorizationFilter remains the Clinical JAX-RS enforcement
  boundary;
- no JWT parsing exists in application code;
- no IdP-specific role mapping was introduced;
- no caller-controlled header can establish authorization;
- default-deny remains intact.


Scope

IN SCOPE:

- replacing DefaultThemisAuthorizer's legacy decision logic with delegation
  to DeterministicPolicyEvaluator.withDefaultPolicies();
- minimal wiring required for that delegation;
- focused BEFE authorization/integration tests;
- Operations compatibility verification;
- Clinical boundary verification;
- relevant architecture tests.

OUT OF SCOPE:

- changes to ClinicalAuthorizationPolicy;
- changes to OperationsAuthorizationPolicy unless a genuine defect prevents
  this integration from working;
- changes to Clinical role vocabulary;
- WildFly/OIDC configuration;
- external IdP role mapping;
- FHIR REST resource redesign;
- Pylai security;
- Agora/Matrix security;
- audit redesign;
- service/workload identity;
- UI authorization;
- CORS;
- ingress/TLS;
- unrelated refactoring.

If a genuine defect in a Themis policy prevents integration, STOP and report
it rather than silently expanding this step.


Verification

Run focused iris-befe authorization/security tests first.

Then run:

    complete iris-befe tests
    complete themis-core tests
    relevant architecture tests

Do not run the complete repository unless an actual dependency requires it.

Do not run git commit, git push, git checkout, create branches or create
pull requests.


Report

1. files changed;
2. final DefaultThemisAuthorizer implementation approach;
3. confirmation that the legacy BEFE-local Operations policy has been removed;
4. confirmation ClinicalAuthorizationPolicy is reached through the evaluator;
5. confirmation OperationsAuthorizationPolicy is reached through the evaluator;
6. Clinical role/action results;
7. Operations compatibility results;
8. cross-domain isolation results;
9. anti-spoofing results;
10. treatment of principal.attributes()["role"];
11. PATCH behaviour;
12. 401 vs 403 boundary;
13. exact test commands and results;
14. any unexpected architectural issue;
15. whether Task 03 is now complete.

Keep this step narrowly focused on completing the integration already
designed in Task 03.