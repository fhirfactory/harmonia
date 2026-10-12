<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# B1 Worked Example — Health Information Access

**Method validation example, 2026-10-13 — for Mark/ChatGPT adjudication.
No B1 PASS, B2 capability mapping or component allocation is established.**

## 1. Bounded Service and request

This example applies the [Step B method](step-b-method.md) within the established
[Health Information Access responsibility](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#23-health-information-access).
It validates the method rather than mapping all enterprise Business Capabilities
to this enabling capability. The Business Capability anchor for each later
operational use remains to be traced in B2; no consumer, ancestry, identifier or
catalogue replacement is invented here.

> **Health Information Retrieval Service** — enables a client to request health
> information applicable to an identified subject, purpose and context.

The Service represents a **request for information**:

```text
Metabolē.Context.ActivityContext = INFORMATION_REQUEST

request.action
request.content
```

The originating Service Request remains available within the progressing
[Metabolē](../../04-information-architecture/patterns/metabole.md), subject to
governing constraints. For example, `request.action = SEARCH` specifies an
action within the broader INFORMATION_REQUEST activity class. A Function
consuming the action does not thereby consume the content, or vice versa.

The following ordered sequence illustrates fulfilment. It requires no new named
Process, execution machinery or assignment of the Functions to Mneme, Themis,
Mnemosyne, Pylai or another component. Any Process decomposition follows the
canonical Function/Process semantics rather than a mandatory hierarchy.

## 2. Function-level information trace

### 2.1 Derive Information Access Scope

| Relationship | Information |
| :--- | :--- |
| **Requires** | `Metabolē.Context.ActivityContext`; Identity; applicable Capacity/Role; Purpose; applicable Criteria. |
| **Consumes** | `request.action`. |
| **Produces** | Information Access Scope. |

The transition establishes the applicable access scope. It **does not consume
`request.content` merely because the content is carried** with the request.

### 2.2 Contextualise Information Access Request

| Relationship | Information |
| :--- | :--- |
| **Requires** | Applicable contextual information. |
| **Consumes** | `request.content`; Information Access Scope. |
| **Produces** | Contextualised Information Request. |

The transition establishes the contextualised request from content and scope.
Carriage of `request.action` does not establish another consumes relationship.

### 2.3 Apply PEP to Information Request

| Relationship | Information |
| :--- | :--- |
| **Requires** | Identity; applicable Capacity/Role; applicable PEP Context / Criteria; `Metabolē.Context.ActivityContext`. |
| **Consumes** | Contextualised Information Request; Information Access Scope. |
| **Produces** | **Scoped/Filtered Information Request**; PEP Determination. |

This is a **request transformation under authorisation semantics**. It does not
merely attach an `authorised=true/false` flag. The resulting scoped/filtered
request is the request actually authorised to proceed. Themis retains
authorisation decision/enforcement responsibility; this functional trace
does not allocate an implementation or grant a Function its own authority.

### 2.4 Retrieve Information

| Relationship | Information |
| :--- | :--- |
| **Requires** | Applicable retrieval Context necessary to locate/access the relevant information source(s). |
| **Consumes** | Scoped/Filtered Information Request. |
| **Produces** | Retrieved Candidate Information. |

The transition establishes candidate information in response to the governed
request. Retrieval does not reinterpret the original ungoverned request or
independently redo authorisation. Candidate information is not yet the
authorised Service Result.

### 2.5 Apply PEP to Information Request Result

| Relationship | Information |
| :--- | :--- |
| **Requires** | Identity; applicable Capacity/Role; applicable PEP Context / Criteria; `Metabolē.Context.ActivityContext`. |
| **Consumes** | Retrieved Candidate Information; Information Access Scope. |
| **Produces** | **Scoped/Filtered Information Result**; PEP Determination. |

This is deliberately distinct from request-side PEP evaluation. It applies
authorisation semantics to the information actually retrieved and may
filter/remove information from the candidate result. The request-side and
result-side PEP Determinations retain their respective derivation and request
association; one does not overwrite the other's meaning.

### 2.6 Build Service Result

| Relationship | Information |
| :--- | :--- |
| **Requires** | Service Contract; applicable response/representation Context. |
| **Consumes** | Scoped/Filtered Information Result. |
| **Produces** | Service Result. |

This transition constructs the result required by the Service Contract. It may
encompass applicable projection, assembly, representation, packaging, response
metadata, formatting or serialisation. It acquires neither retrieval nor
authorisation responsibility. Applicable external publication continues to obey
AX-02/13 and Pylai's established boundary; accumulated internal information is
not automatically part of the response.

## 3. Progressive information establishment

```text
Service Request
      │
      ▼
Metabolē
  ├── Input
  │    └── request
  │         ├── action
  │         └── content
  ├── Context
  │    ├── ActivityContext = INFORMATION_REQUEST
  │    └── other applicable Context
  ├── Provenance / Derivation
  └── Output                              [initially 0..*]
      │
      ▼
Derive Information Access Scope
      │
      ▼
+ Information Access Scope
      │
      ▼
Contextualise Information Access Request
      │
      ▼
+ Contextualised Information Request
      │
      ▼
Apply PEP to Information Request
      │
      ▼
+ Scoped/Filtered Information Request
+ PEP Determination                       [request-side]
      │
      ▼
Retrieve Information
      │
      ▼
+ Retrieved Candidate Information
      │
      ▼
Apply PEP to Information Request Result
      │
      ▼
+ Scoped/Filtered Information Result
+ PEP Determination                       [result-side]
      │
      ▼
Build Service Result
      │
      ▼
+ Service Result
```

Each `+` means information is established in the progressing Metabolē, with
sufficient provenance/derivation to relate it to its source and the originating
request. Earlier information remains available subject to information-management,
security and lifecycle constraints. This conceptual progression prescribes no
physical envelope, unbounded retention, durable snapshot of every stage or
client visibility of unfiltered candidate information.

**Additive progression does not mean every Function consumes everything already
present within the Metabolē.** Section 2 is the primary information trace:
derivation of scope consumes action; contextualisation consumes content and scope;
retrieval consumes the scoped/filtered request; result construction consumes the
scoped/filtered result. Shared carriage does not broaden those relationships.

## 4. Validation against the method and retained boundary

| Method concern | What the example demonstrates |
| :--- | :--- |
| Dual anchor / no layer skipping (R01–02) | Established enabling responsibility and an explicit later Business Capability trace; no fabricated enterprise anchor or direct component shortcut. |
| Information semantics (R03, R10) | Each Function distinguishes requires, consumes and produces; action/content carriage does not imply equal use. |
| Behaviour and accountability (R04–08) | One client Service Contract and six bounded transitions; ordered fulfilment does not impose a mandatory Process or equate Functions with Services. |
| Additive progression (R09) | Originating request, Context and preceding information remain available under applicable constraints. |
| Provenance/evidence (R11) | Accumulated derivation and request association remain distinct from Kleio audit and Dokimasia assurance evidence. |
| Realisation (R12) | Metabolē is conceptual; Pragma and Pylai.Diadromí realise it in their own domains. The example chooses no runtime type, persistence model or common manager. |

This is a semantic method demonstration, not a successful runtime execution or a
claim that all graph relationships have been mapped. Denial, failure, empty
results and uncertain outcomes retain their distinct semantics under AX-07/14/15;
their detailed Service Contract dispositions are not invented here. Existing
Mneme, Mnemosyne, Themis, Calliope, Kleio, Dokimasia and Pylai responsibilities
remain as recorded in the [method's responsibility boundary](step-b-method.md#5-application-responsibility-restraint).
Detailed application allocation belongs to later Step B passes.
