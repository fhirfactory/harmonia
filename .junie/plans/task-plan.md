---
sessionId: session-260922-072141-101j
---

# Delivery Steps

### ✓ Step 1: Implementation
SESSION CONTEXT

This is a new implementation session for the Harmonia repository.

A previous architecture/discovery exercise has already been completed.
The architectural decisions relevant to this task are included explicitly
below.

Treat the CURRENT CHECKED-IN REPOSITORY as the authoritative implementation
baseline.

Do not attempt to reconstruct, continue, or infer work from previous Junie
sessions.

First inspect the current repository sufficiently to understand the existing
implementation and contracts relevant to this task, then perform ONLY the
task specified below.

Do not broaden scope based on TODOs, comments, specifications, OpenSpec
documents, or adjacent architectural discrepancies discovered during the
work. Report such issues at completion instead.

HARMONIA — TASK 01
CLINICAL API THEMIS ENFORCEMENT

OBJECTIVE

Implement centralised Themis authorisation enforcement for the Iris BEFE
Clinical FHIR API.

This task establishes the authoritative server-side security boundary for:

    /api/fhir/*

The implementation must be DEFAULT DENY.

This is a deliberately small implementation task.

DO NOT begin Task 02 or any subsequent Harmonia task.


ARCHITECTURAL CONTEXT

The previous architecture analysis established that:

- iris-befe exposes Clinical FHIR endpoints under /api/fhir/*
- the existing Clinical FHIR endpoints do not currently enforce Themis
  authorisation;
- operations endpoints already contain examples of Themis authorisation;
- UI visibility is never an authorisation mechanism;
- authorisation must not depend upon every individual FHIR Resource class
  remembering to perform an explicit check;
- Themis is the Harmonia policy decision capability;
- server/API authorisation is authoritative;
- Harmonia security is default-deny.

The approved target pattern is:

    Request
       |
       v
    iris-befe
       |
       v
    Shared Clinical Authorisation Filter
       |
       +---- establish/obtain caller security context
       |
       +---- construct Themis authorisation request
       |
       +---- evaluate Themis
       |
       +---- DENY ----> 401/403 OperationOutcome
       |
       +---- PERMIT
                |
                v
          FHIR Resource
                |
                v
          existing processing


IMPORTANT SCOPE BOUNDARY

This task implements the BEFE authorisation enforcement boundary.

It does NOT implement the complete identity architecture.

Task 02 will separately address propagation of ThemisPrincipal /
ThemisSecurityContext into downstream application and persistence services.

Do not redesign Mnemosyne or FhirStorageService in this task.


1. DISCOVER EXISTING THEMIS IMPLEMENTATION

Before changing code, inspect the repository for:

- themis-api;
- themis-core;
- ThemisAuthorizer;
- ThemisPrincipal;
- ThemisSecurityContext;
- ThemisAuthorizationRequest;
- ThemisAuthorizationDecision;
- HarmoniaRoleEnum;
- HarmoniaAuthorityEnum;
- ThemisAction or equivalent;
- ThemisResource or equivalent;
- existing authorisation checks in OperationsResource;
- existing security filters/interceptors;
- existing authentication or trusted identity handling;
- existing OperationOutcome/error response helpers;
- existing tests for Themis and Operations authorisation.

Reuse existing Harmonia contracts wherever possible.

Do not invent parallel security abstractions if an appropriate one already
exists.


2. IMPLEMENT CENTRALISED CLINICAL AUTHORISATION

Implement a shared JAX-RS request filter/interceptor for the Clinical FHIR
API.

Preferred conceptual component:

    ThemisClinicalAuthorizationFilter

The exact implementation mechanism should follow the patterns and
capabilities already present in the repository.

The filter must apply to Clinical FHIR endpoints under:

    /api/fhir/*

It must execute BEFORE the FHIR Resource method performs the requested
operation.


3. DEFAULT-DENY BEHAVIOUR

The security boundary must fail closed.

A request must NOT reach the FHIR Resource implementation unless a positive
Themis authorisation decision has been obtained.

Conceptually:

    no usable identity
        -> DENY

    invalid identity
        -> DENY

    no matching authority/policy
        -> DENY

    explicit Themis denial
        -> DENY

    explicit Themis permit
        -> continue

Do not introduce:

    principal == null -> permit

or any equivalent implicit/system/superuser fallback.


4. ACTION MAPPING

Map the Clinical FHIR REST interaction to the appropriate existing Themis
action semantics.

Expected conceptual mapping:

    GET /api/fhir/{type}
        -> SEARCH

    GET /api/fhir/{type}/{id}
        -> READ

    POST /api/fhir/{type}
        -> CREATE

    PUT /api/fhir/{type}/{id}
        -> UPDATE

    DELETE /api/fhir/{type}/{id}
        -> DELETE

Use the existing Themis action types if their names differ.

Do not introduce duplicate action enums merely to match these names.


5. RESOURCE CONTEXT

Construct the Themis resource/policy context from the request.

Where supported by the existing Themis model, include:

    security domain = CLINICAL
    resource type   = FHIR resource type
    resource id     = logical id when present
    requested action
    caller/principal context

Examples:

    GET /api/fhir/Person
        domain       CLINICAL
        resourceType Person
        resourceId   null
        action       SEARCH

    GET /api/fhir/Person/abc123
        domain       CLINICAL
        resourceType Person
        resourceId   abc123
        action       READ

    PUT /api/fhir/Task/xyz789
        domain       CLINICAL
        resourceType Task
        resourceId   xyz789
        action       UPDATE


6. IDENTITY HANDLING — KEEP THIS TASK BOUNDED

Use the existing repository-supported identity/security-context mechanism.

Do NOT introduce Keycloak as a new architectural dependency.

Do NOT invent a new authentication system.

Do NOT blindly trust client-supplied identity or role headers such as:

    X-Harmonia-User
    X-Harmonia-Role

unless the existing repository architecture already establishes a trusted
mechanism that guarantees such headers are stripped from external requests
and inserted by an authenticated trusted component.

If the repository currently lacks sufficient authentication infrastructure
to establish a genuine principal, DO NOT weaken default-deny behaviour to
make the application work.

Instead:

- implement as much of the enforcement boundary as can be implemented
  correctly;
- identify the precise missing identity contract;
- fail closed;
- report the gap.

Task 02 will address principal/security-context propagation.


7. RESPONSE SEMANTICS

Use standard HTTP semantics:

    authentication absent/invalid
        -> 401 Unauthorized

    authenticated but not authorised
        -> 403 Forbidden

Where the existing BEFE/FHIR infrastructure supports it, return a FHIR R5
OperationOutcome rather than an ad-hoc JSON error.

Do not include:

- credentials;
- tokens;
- patient information;
- request payloads;
- query values;
- other PHI

in error messages or logs.


8. DO NOT IMPLEMENT AUDIT CHANGES YET

The architecture report recommends security audit checkpoints.

AuditEvent lifecycle and WORM semantics are Task 04.

Do not redesign AuditEvent in this task.

If there is an existing safe Themis security-decision audit mechanism that
is already part of normal Themis evaluation, preserve/use it.

Otherwise report the missing audit hook for Task 04 rather than expanding
this task.


9. TESTS

Add focused tests for the new Clinical authorisation boundary.

At minimum verify:

A. unauthenticated / no usable principal
       -> denied

B. invalid identity/context
       -> denied

C. authenticated principal without required authority
       -> 403 / denied

D. authorised SEARCH
       -> reaches resource

E. authorised READ
       -> reaches resource

F. authorised CREATE
       -> reaches resource

G. authorised UPDATE
       -> reaches resource

H. authorised DELETE
       -> reaches resource

I. unknown/new Clinical resource path
       -> does not accidentally bypass the security boundary

J. non-Clinical BEFE endpoints are not unintentionally altered by this
   filter.

Where appropriate, test the resulting FHIR OperationOutcome.


10. ARCHITECTURAL GUARDRAIL TEST

If practical using the repository's existing architecture-test approach,
add a guardrail preventing Clinical FHIR resources from being exposed
outside the common authorisation boundary.

The purpose is to make this failure mode difficult to reintroduce when a
future FHIR Resource class is added.

Do not create a large new architecture-testing framework solely for this
task.


11. OUT OF SCOPE

DO NOT modify as part of Task 01:

- Mnemosyne persistence architecture;
- FhirStorageService authorisation behaviour;
- principal propagation into Mnemosyne;
- Infinispan/Mneme read/write behaviour;
- FhirCacheService persistence semantics;
- ConcurrentHashMap fallback;
- authoritative search;
- AuditEvent CRUD/WORM semantics;
- CORS policy;
- Iris Clinical UI;
- iris-befe frontend components;
- PrimeVue;
- Clinical navigation;
- Patient implementation;
- Pylai;
- Petasos;
- Ponos;
- Docker/Kubernetes topology.

Those belong to subsequent tasks.


12. BACKEND BASELINE

The current Harmonia runtime/backend is a known-good baseline.

Do not alter unrelated dependency management, Artemis configuration,
Pylai/Petasos/Ponos behaviour, deployment topology or Maven packaging.

Keep changes narrowly scoped to the Clinical BEFE authorisation boundary
and directly required tests.


DEFINITION OF DONE

Task 01 is complete when:

1. /api/fhir/* is protected by a common Themis authorisation boundary;

2. the boundary is default-deny;

3. a FHIR Resource cannot execute without a positive authorisation
   decision;

4. REST interactions map consistently to existing Themis action semantics;

5. Clinical resource type and logical id are supplied to Themis where
   applicable;

6. denial produces appropriate 401/403 behaviour and, where supported,
   FHIR OperationOutcome;

7. no client-controlled role/authority mechanism has been introduced;

8. existing non-Clinical BEFE behaviour remains unchanged;

9. focused tests pass;

10. affected Maven modules build successfully.


WHEN COMPLETE

Do not begin Task 02.

Report:

1. Summary of implementation.
2. Exact changed files.
3. The existing Themis contracts/patterns reused.
4. How identity is currently obtained.
5. Action/resource mapping implemented.
6. Default-deny behaviour.
7. 401 vs 403 behaviour.
8. Tests added.
9. Commands executed and results.
10. Any missing identity/security contract discovered.
11. Any architectural discrepancy discovered.
12. Any deviation from this task specification.
13. Recommended considerations for Task 02 — but DO NOT implement them.

STOP AND WAIT FOR REVIEW.

This task has no prior planning phase. Before implementation, analyze the task and codebase, define acceptance criteria if not explicitly provided in the task description, and plan your approach. 
The Reviewer must independently define its own acceptance criteria and will verify them.