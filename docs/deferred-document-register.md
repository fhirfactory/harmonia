# Harmonia Deferred Document Register

This register records documentation tasks, wording adjustments, and architectural refinements that have been identified during architecture reviews but deferred to designated downstream passes or domains.

The register ensures governance traceability, prevents premature implementation during earlier passes, and provides an authoritative backlog of deferred documentation items across Harmonia documentation domains.

---

## 1. Active Deferred Items

Items 01–03 remain resolved by Domain 02 Strategy Pass C. Item 04 records subsequently recovered architectural documentation debt and remains **Deferred**.

### Item 04 — Capability Modelling Metamodel and Identifier Conventions

| Field | Record |
|---|---|
| **Status** | **Deferred** |
| **Identified / recovered** | 2026-10-08 |
| **Arose from** | Architectural review following the Domain 02 Health Service Assurance Strategy reconciliation. The [approved Health Service Assurance derivation](markdown/02-strategy/capability-maps/health-service-assurance-derivation.md) records that reconciliation's Strategy context; it is not asserted to document all recovered metamodel decisions below. |
| **Outstanding work** | Formally document and reconcile the recovered Capability Modelling Metamodel and identifier conventions into canonical architecture documentation in a separately authorised metamodel documentation task. |
| **Governing authority** | [AX-17 — Architectural Authority and Explicit Uncertainty](architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty). |

The following are recovered architectural decisions supplied for this documentation-control task, not new Codex proposals. Their capture preserves architectural knowledge independently of conversational history or AI memory. Recording this item does not perform canonical reconciliation, change the Capability Model or establish missing modelling details.

#### Recovered Decision 1 — Capability Tier Set Semantics

Capability Tiers are set concepts: `CT1` means Capability Tier 1, `CT2` means Capability Tier 2 and `CT3` means Capability Tier 3. A CT1 capability comprises/encompasses CT2 capability sets; a CT2 capability comprises/encompasses CT3 capability sets.

```text
CT1
  └── encompasses CT2
        └── encompasses CT3
```

This is capability-set inclusion / encompassing, rather than organisational hierarchy, implementation layering, specialisation or merely visual grouping. Existing Business Enabling contextual/natural views SHALL NOT be treated as Capability Tiers or identifier ancestors.

#### Recovered Decision 2 — Capability Element Types

Within the capability construct, the architecture may articulate:

| Type | Meaning |
|---|---|
| `FT` | Feature |
| `SN` | Service |
| `FN` | Function |
| `PS` | Process |

Services, Functions and Processes may be articulated at Strategy level where strategically relevant. That articulation SHALL NOT be interpreted as permission to prematurely derive downstream Application Architecture or implementation.

#### Recovered Decision 3 — Architectural Namespace / Model Prefix

Canonical identifiers distinguish the architectural capability model in which an element is defined:

| Prefix | Architectural capability context |
|---|---|
| `BL` | Business Layer |
| `BE` | Business Enabling |
| `EN` | Enterprise |

These prefixes distinguish architectural identity and SHALL NOT be treated merely as display formatting. Lexically similar elements in different namespaces SHALL NOT be assumed to be the same architectural element without an explicit architectural relationship.

#### Recovered Decision 4 — Canonical Structural Identifier

The recovered canonical Business Layer identifier structure is:

```text
BL.CT1-<id>.CT2-<id>.CT3-<id>.<element-type>-<id>
```

For example:

```text
BL.CT1-xxx.CT2-yyy.CT3-zzz.FT-aaa
```

The identifier expresses the element's canonical capability-set context. Shorter canonical forms apply where the element exists at a higher Capability Tier. Exact canonical structures for `BE` and `EN` remain explicitly unresolved in this capture and SHALL NOT be invented by extending the `BL` example.

#### Recovered Decision 5 — Reference / Summary Forms

Appropriately abbreviated references may be used where they remain unambiguous:

| Reference form | Recovered example |
|---|---|
| Canonical | `BL.CT1-xxx.CT2-yyy.CT3-zzz.FT-aaa` |
| Summarised | `BL.xxx.yyy.zzz.aaa` |
| Context-qualified | `CT3-zzz.FN-aaa` |

The canonical identifier remains authoritative. Abbreviated references SHALL NOT establish a different architectural identity. Feature names and other local element names are not assumed to be globally unique; sufficient capability context must be retained where required to make a reference unambiguous.

#### Recovered Decision 6 — Identifier Aliasing, Relocation and Retirement

Canonical structural identifiers reflect an architectural element's current capability-set placement. Where an existing Capability, Feature, Service, Function, Process or other governed capability element is relocated such that its canonical structural identifier changes:

- The new structural location receives the applicable canonical identifier.
- The previous identifier is retired from canonical use.
- The previous identifier is retained as an alias of the relocated architectural element.
- Retired identifiers SHALL NOT be reassigned to a different architectural element.

The alias mechanism preserves historical and referential continuity across architectural restructuring. Deletion, replacement, split and merge semantics are not established by this relocation decision and require explicit subsequent reconciliation.

#### Recovered Decision 7 — No “Cross-Cutting Capability” Construct

The Harmonia capability model does not recognise “cross-cutting capability” or “cross-cutting feature” as a special architectural classification. Apparent cross-cutting concerns indicate either insufficiently bounded or poorly defined capability responsibilities, or use/consumption by one capability of Features, Functions or Services defined within another capability.

A capability may use or consume Features, Functions or Services belonging to another capability. Such use SHALL NOT transfer ownership, change capability-set membership, change canonical identity or duplicate the consumed element within the consuming capability. Capability-set membership and capability dependency/use are separate architectural relationships.

#### Recovered Decision 8 — Capability-Scoped Information Ownership

Information Objects are defined and owned within the context of the capability responsible for their business meaning. They SHALL NOT be treated as free-floating enterprise objects without a defining capability context.

Other capabilities may reference, consume, exchange or otherwise use an Information Object defined by another capability without acquiring ownership or creating a duplicate definition. Ownership/definition and consumption/use are separate architectural relationships.

Data Objects are downstream representations/realisations of information within the appropriate later architectural layer and SHALL NOT silently redefine the meaning established by the capability-scoped Information Object. Exact canonical identifier abbreviations or syntax for Information Objects and Data Objects remain explicitly unresolved.

#### Required Future Reconciliation — Capability Model Quality Rules

The subsequent metamodel documentation task SHALL assess the recovered decisions against the existing [Capability Model quality rules](markdown/02-strategy/capabilities/business-capabilities.md#capability-modeling-quality-rules):

- **CM-R01 Coverage**
- **CM-R02 Orthogonality**
- **CM-R03 Semantic Clarity**
- **CM-R04 No Junk Drawers**
- **CM-R05 Global Capability Name Uniqueness**

Rejection of “cross-cutting capability” as a modelling construct reinforces orthogonality, semantic clarity and avoidance of junk-drawer capabilities. Global Capability Name Uniqueness must be assessed without assuming that Feature names or other local element names are globally unique.

The future task must identify applicable canonical documentation, reconcile the recovered decisions and report conflicts with existing authoritative material explicitly under AX-17. It must preserve the distinction between agreed decisions, unresolved details and any proposed resolutions. This register update supplies no authority for repository-wide identifier migration or normalisation of existing identifiers merely because they differ from the recovered convention.

#### Explicitly Unresolved Details and Prohibited Inference

The following remain unresolved and require explicit architectural reconciliation:

- Exact `BE` canonical identifier structure.
- Exact `EN` canonical identifier structure.
- Information Object identifier abbreviation/syntax.
- Data Object identifier abbreviation/syntax.
- Deletion semantics.
- Replacement semantics.
- Split semantics.
- Merge semantics.
- Any additional relationship names or semantics not explicitly recovered above.

Under AX-17, missing identifier syntax, relationship semantics, object abbreviations, lifecycle rules or other metamodel details SHALL NOT be completed through convention, lexical similarity, ArchiMate familiarity, TOGAF familiarity or anticipated implementation. Traceability must be truthful, not artificially complete.

#### Reason for Deferral and Completion Boundary

The decisions were recovered after the Health Service Assurance Strategy reconciliation, but their formal metamodel documentation and reconciliation remain outstanding. This task is authorised only to capture and control that documentation debt. Recording recovered decisions does not demonstrate that canonical architecture documentation has been reconciled or that unresolved details have been decided.

Item 04 SHALL remain **Deferred** after this update. Its eventual resolution requires a separately authorised metamodel documentation task, explicit treatment of the uncertainties above and verification against AX-17 and the Capability Model quality rules. This capture SHALL NOT modify Domain 01 Motivation, Domain 02 Strategy capability artefacts, Domain 03 Business Architecture, Domain 04 Information Architecture, Architectural Axioms, ADRs, source code, tests or capability identifiers used elsewhere. It SHALL NOT rename capabilities or Features, infer additional modelling rules, perform identifier migration or commence downstream architectural reconciliation.

---

## 2. Resolved Items

| Item # | Item Description | Arose From | Resolved In | Status | Canonical Files Modified & Resolution Summary |
|---|---|---|---|---|---|
| **01** | Remove residual implementation-prescriptive wording such as `cryptographic`, `tamper-proof`, and `immutable` from Business Enabling Features where the required behaviour can be stated independently of mechanism. | Strategy Pass A.1 review | Domain 02 Strategy Pass C (2026-10-06) | **Resolved** | Modified `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md` (features `FEAT-EM-05`, `FEAT-SA-16`, `FEAT-SA-29`, `FEAT-ISE-03`, `FEAT-ISE-12`, `FEAT-ISE-13`) and `enterprise-capabilities.md` (`EC-02`, `EC-07`). Replaced mechanism-specific terms with durable architectural qualities (`enduring`, `content integrity`, `tamper-evident`, `verifiable audit records`, `attributable security context`). |
| **02** | Replace wording describing Harmonia as providing the “authoritative historical clinical record” with wording reflecting Harmonia's governed, vendor-neutral longitudinal clinical representation and preservation responsibilities. | Strategy Pass A.1 review | Domain 02 Strategy Pass C (2026-10-06) | **Resolved** | Modified `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md` (`FEAT-ISE-03`). Explicitly established that Harmonia provides "governed, vendor-neutral longitudinal clinical representation and preservation with verifiable integrity controls" without claiming originating clinical authority for source medical assertions. |
| **03** | Review Business Capability “Harmonia Enabling Role” statements and remove residual solution-mechanism wording so they describe strategic enablement rather than premature solution design. | Strategy Pass A.1 review | Domain 02 Strategy Pass C (2026-10-06) | **Resolved** | Modified `docs/markdown/02-strategy/capabilities/business-capabilities.md` (Capabilities 03, 04, 06, 08, 12 and summary table). Removed solution-specific messaging protocols (HL7 v2 ORU/ORM, bulk FHIR API) and prescriptive terms (immutable audit, deterministic identity resolution), replacing them with strategic enablement and correlation descriptions. |

---

## 3. Maintenance & Lifecycle

- **Deferred**: Identified during review; intentionally held for a future authoring pass or domain.
- **In Progress**: Actively being addressed within the target pass.
- **Resolved**: Reconciled and verified against applicable Architectural Axioms and quality rules.
- **Superseded**: Rendered obsolete by an accepted Architecture Decision Record (ADR) or axiom update.
