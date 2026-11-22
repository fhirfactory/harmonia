<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Ergon Authorisation and Identity Delegation

**Decision Record Proposal** · Draft 001.002 · Prepared by Codex for Solution Architect review\
**Solution Architect** · Mark Hunter\
**Basis** · `fhirfactory/harmonia` · `feature/BL-CALLIOPE-OO-01` · Architecture 1.1.26 · `Software-Version-0.1.0-Snapshot-004` at `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`. [Architecture release][release] · [Iteration 005 review][issues]

## 1. Feature realisation and authority

G01 applies **LocalEnforcementConformance** under **Boundary-Enforced Access**
at the Ergon boundaries. It uses **Security Context & Passport Propagation** and
**Operational Passport Currency** to make trusted context and current authority
available at governed internal interactions. The related business services are
**Security Context Validation** and **Policy Evaluation & Authorisation**.
[LocalEnforcementConformance][local-enforcement] · [Boundary-Enforced Access][boundary-access] · [Security Context & Passport Propagation][propagation] · [Operational Passport Currency][currency] · [Security Context Validation][context-validation] · [Policy Evaluation & Authorisation][policy-evaluation]

The directly applicable requirements are governed information authorisation
(NFR-035), authorisation boundary enforcement (NFR-038), encryption at rest
(NFR-077) and internal encryption in transit (NFR-078). SEC-004 and SEC-005
provide related contextual-authorisation and end-to-end context requirements
with their existing standing. [NFR-035][nfr035] · [NFR-038][nfr038] · [NFR-077][nfr077] · [NFR-078][nfr078] · [SEC-004][sec004] · [SEC-005][sec005]

AX-07 requires trusted context before governed action and places policy evaluation
with Themis. The approved distributed-enforcement and Passport-availability
Principles support local PEPs and availability by governed reference. AX-05,
AX-11 and AX-18 support timely operational availability through Mneme while
preserving Themis policy authority. AX-14 preserves the distinctions between
identity, permissions and information requirements. AX-13 bounds external
management, AX-15 preserves uncertainty in failed or unavailable determinations,
and AX-17 requires explicit treatment of the wording and contract questions
below. [Architectural Axioms][axioms] · [Distributed Authorisation Enforcement][distributed-pep] · [Passport Propagation / Availability][passport-principle]

## 2. Proposal

Record the Solution Architect's G01 adjudication for the common Ergon framework.
The common framework enforces Themis policy before processing and before release.
Ergon operates under its own processing identity. Identity delegation supplies
the calling agent's ID for Themis to resolve and validate applicable authority
at invocation. The IU supplies the protected information's own security
requirements. [Operative G01 adjudication][task] · [Draft 001.002 instruction][revision-task]

## 3. Design context

- **A01** concerns whether a FHIR Task carrying Pragma context is a trusted
  representation on each supported path. The converter reconstructs security
  context from extensions. The review left producer authentication, the
  transport trust boundary and binding to the receiving operation unverified.
  G01 defines the authority that the boundary must enforce.
  [G01 and A01][issues] · [HCRL.004 DR-7][dr7] · [Original review][first-review]
- **A02** concerns fidelity of IU security criteria. A typed action mask was
  lost on JSON round-trip and omitted from emptiness, equality and hashing.
  The earlier consumer review also traced a legacy action name through a
  name-pattern adapter. The demonstrated loss is an implementation defect.
  Which typed and legacy bindings are supported, and their literal or regular
  expression semantics, still need evidence and review.
  [G01 and A02][issues] · [Original review][first-review] · [003 consumer analysis][gaps003] · [HCRL.004 DR-3][dr3] · [HCRL.004 DR-4][dr4]

## 4. Architectural definition

1. **Security context.** The determination uses `IU.SecurityMetadata` for the
   protected information's requirements, `Ergon.Process.Role` for the
   processing identity and authorisation profile, and `Themis.PRP` for the
   applicable rules. Themis derives and evaluates the applicable authority
   against the requirements for the operation.
2. **Ergon.PEP responsibilities.** The common Ergon framework places
   Themis-governed PEPs before and after processing.

   ```text
   Ergon.In → Ergon.PEP.In → Ergon.Process → Ergon.PEP.Out → Ergon.Out
   ```

   - **Inbound.** `Ergon.PEP.In` controls admission to `Ergon.Process`. Its
     failure-handling responsibility is to prevent processing from starting
     when inbound authorisation is denied or cannot be established.
   - **Outbound.** `Ergon.PEP.Out` controls release or handoff after
     `Ergon.Process`. Its failure-handling responsibility is to withhold
     the result when outbound authorisation is denied or cannot be established.
     The processing outcome and the release outcome are distinct.

   Each PEP distinguishes policy denial from an unavailable determination.
3. **Processing identity.** `Ergon.Process` executes under its own Role and
   applicable Passport.
4. **Service invocation.** When `Ergon.Process` calls a service, the serving
   component receives its processing identity within the applicable Passport
   context and enforces policy at its own operation boundary. The existing
   Passport-availability Principle permits a governed reference. Any identity
   delegation in that interaction follows decision 5.
5. **Identity delegation.** Between processing components, identity delegation
   propagates only the calling agent's ID. The delegation exchange excludes
   credentials, the agent's Passport and its authorisation profile. Themis
   resolves applicable authority for the ID under trusted context. Applicable
   FORWARD authority and provenance of the originating and acting identities
   still apply.
6. **Representation.** `IU.InformationRepresentation` determines whether its
   content carries a delegated identity that requires consideration. It
   supplies the identity indication. Themis evaluates its eligibility to
   contribute authority within the authenticated invocation context.
7. **Passport resolution.** At invocation, Themis resolves the applicable
   Passport for each required identity, including the processing Ergon and
   any required delegated identity. Resolution selects valid, applicable
   authority for the operation.
8. **Effective profile.** Themis combines the Ergon's own authority with
   validated and permitted delegated authority under the agreed OR semantics.
   An applicable complete grant from either eligible source can satisfy the
   authorisation criteria for the operation. Each grant retains its own
   conjunctive dimensions, scope and temporal constraints. Themis applies all
   other operation and forwarding rules and defaults to denial where no
   applicable authority grants the action.
9. **Joint Passport availability obligation.** Themis and Mneme must jointly
   make applicable Passports for supplied agent identities available in time
   for the required enforcement decision. Themis owns policy, Passport
   resolution and validity. Mneme provides governed operational access to
   the applicable Passports.
10. **Preservation.** Processing, access and delegation preserve the IU's
    `SecurityMetadata` and security requirements. Request and session authority
    remain operational context under the established IU contract.
11. **Encryption.** All IUs are encrypted in transit and at rest. The
    Harmonia management boundary and external interoperability contracts
    govern how this obligation applies at egress.
12. **Petasos boundary.** `Petasos.Queue` and `Petasos.Router` convey and route
    information within their governed responsibilities. Whether an Ergon
    may process an IU is enforced at the Ergon boundary. Petasos hosts the
    Themis PEP for its own governed FORWARD operation.

## 5. Implementation design

The architectural responsibilities above leave the wire encoding, API and
class allocation, Passport retrieval and distribution mechanism, time budgets,
cryptography and key management to subsequent governed design. The detailed
trust contract for each supported transport, security-criteria bindings and
the inbound and outbound failure contracts need the dispositions in §9.

## 6. Evidence and rationale

The 1.1.26 Continuum provides current authority with each element's recorded
standing. The G01 instruction is the attributable decision input for this
draft. The 005 findings, original review and 003 analysis supply evidence about
the software candidate and its representations. The offline Themis design is
related candidate context. This draft revises the wording of decisions 2, 5
and 9 from Draft 001.001 and makes the Themis reconciliation explicit.
[Architecture release][release] · [G01 adjudication][task] · [005 findings][issues] · [Original review][first-review] · [003 consumer analysis][gaps003] · [Offline Themis draft][themis-draft] · [Draft 001.001][predecessor] · [Revision instruction][revision-task]

## 7. Relationship to current architecture

| Subject | G01 reconciliation |
| --- | --- |
| Themis and Kleio | Themis retains PAP/PRP/PDP and distributed PEP ownership. Authentication remains separately provided. The G01 OR rule operates across complete eligible grants, preserving conjunction within each Criterion, applicable FORWARD authority and provenance. Kleio retains mandatory authoritative audit capture under applicable policy. [Themis][themis] · [Criteria][criteria] · [FORWARD constraint][forward] · [AX-07 audit capture][kleio-capture] |
| IU | `IU.SecurityMetadata` is used here for the existing IU-side security requirement concern, represented in the Continuum as the SecurityContext concern of InformationMetadata. Governed derivation of effective attributes for evaluation preserves the stored requirements. `IU.InformationRepresentation` retains its single governed representation and native Content semantics. G01 uses that representation to determine whether a delegated identity is present. [IU metadata][iu-metadata] · [InformationRepresentation][representation] · [Effective security attributes][effective-attributes] |
| Ergon, Mneme and Mnemosyne | The framework adds enforcement responsibilities around bounded Ergon processing. Existing load, unload, context-save and recovery responsibilities have their established owners. Mneme provides governed active access and supports operational Passport availability. Mnemosyne retains durable state authority. [Ergon][ergon] · [Mneme][mneme] · [AX-05][ax05] |
| Petasos and Pylai | Petasos retains queue, routing and handoff responsibilities, including applicable Themis enforcement for FORWARD. Pylai retains the external interoperability boundary and its handling of Harmonia-private operational context. [Petasos][petasos] · [Pylai][pylai] · [AX-13][ax13] |

### Identity delegation and Themis enforcement context

Between processing components, G01's identity delegation carries only the
calling agent's ID. Within the Themis enforcement model, Themis resolves the
applicable authority and supplies Identity-specific PEP-Criteria through
PEP-Context to its distributed PEPs. This includes PEPs embedded in processing
components. The approved Passport-availability Principle permits governed
references for making the applicable Passport available. [Themis PEP-Context][pep-context] · [Passport Propagation / Availability][passport-principle]

The source-qualified Themis design describes delegation as **Identity /
PEP-Criteria pass-off (forwarding)**. It leaves the forwarding representation
and PDP interaction open. Its FORWARD policy and provenance of both identities
apply to G01. A requirement for the calling component to send PEP-Criteria or
an authority-bearing context as identity delegation on the same boundary would
conflict with decision 5 and require Solution Architect adjudication.
[Themis delegation][themis-delegation] · [FORWARD constraint][forward]

## 8. Conformance expectations

These are evidence obligations for a later implementation and review.

| Area | Evidence needed |
| --- | --- |
| A01 — trusted representation | Identify each supported path and demonstrate how the producer, carried identity indication and receiving operation are bound to established context. Show that caller-controlled Task extensions cannot establish authority. Verify the serving operation uses the Ergon identity and applicable Passport context, and that identity delegation between processing components carries only the calling agent's ID while Themis supplies the applicable enforcement context. [HCRL.004 DR-7][dr7] · [Repository Invariant 6][guardrails] |
| A02 — criteria fidelity | Verify that each approved typed or legacy binding preserves effective action, compartment and other required criteria across boundaries, including round-trip and observable value behaviour where the representation exposes emptiness, equality or hashing. Establish the actual literal or regular expression meaning of legacy inputs. Show a bounded, fail-closed outcome for unsupported or ambiguous criteria and no widening of matches. [005 findings][issues] · [Original review][first-review] · [003 consumer analysis][gaps003] |
| Passport resolution and availability | Verify resolution for each required identity under applicable policy, including current, absent, unavailable, expired or revoked authority. Show that a missing required context prevents the affected operation, that an unavailable determination is distinguishable from denial, and that only valid and sufficient established context can support in-flight activity under the existing rule. Verify that Themis and Mneme together make applicable Passports for supplied IDs available in time for enforcement. [Themis availability][themis-delegation] · [Operational Passport Currency][currency] |
| Authorisation | Exercise authority from Ergon alone, from valid permitted delegated authority, and from both, within each case's applicable context. Exercise cases with no complete eligible grant, an invalid or impermissible delegated contribution, or contextual or FORWARD restrictions. Demonstrate inbound refusal before processing, outbound refusal before release and enforcement at serving and forwarding boundaries independently of route eligibility. [Themis Criteria][criteria] · [Distributed Authorisation Enforcement][distributed-pep] |
| IU preservation and controls | Demonstrate preservation of IU security requirements across processing, access and delegation. Verify encryption over managed transit and storage paths and retain required security evidence through Kleio. [IU metadata][iu-metadata] · [NFR-077][nfr077] · [NFR-078][nfr078] · [AX-07 audit capture][kleio-capture] |

## 9. Matters for Solution Architect review

- Confirm the scope of the reconciliation in §7, including whether the
  Themis pass-off wording requires a particular representation on the same
  G01 boundary. The precise mapping of `Ergon.Process.Role` to established
  Themis identity and role terminology and the detailed Passport, PEP-Context
  and PEP-Criteria contract correspondence remain open.
  [Themis][themis] · [Themis delegation][themis-delegation]
- For A01, determine which Pragma FHIR Task paths support trusted context and
  operation binding. For A02, confirm the supported typed and legacy bindings
  and any migration treatment that depends on G03 selector semantics.
  [005 findings][issues] · [HCRL.004 DR-7][dr7]
- Confirm which distinct failure outcomes and obligations the inbound and
  outbound boundary contracts must expose. The existing facts that processing
  can precede outbound refusal and that unavailable policy is distinct from
  denial constrain that review. [Themis availability][themis-delegation] · [Ergon][ergon]

## 10. Continuum correspondence for review

The expected correspondence is with the common Ergon framework and its
IngressEndpoint/EgressEndpoint, Themis enforcement and Passport availability,
and the existing IU metadata and representation contracts. The encryption and
forwarding mandates provide cross-cutting constraints. Any eventual
incorporation needs governed review of exact definitions, relationships and
standing. [Ergon][ergon] · [Themis][themis] · [IU metadata][iu-metadata] · [InformationRepresentation][representation] · [Incorporation Praxis][incorporation]

## 11. Review decision

**Pending Solution Architect review of this draft and its reconciliation.**

[release]: ../../../architecture-release.json
[issues]: ../evidence/software-0.1.0/005/after/Calliope-Architectural-Issue-Groups-005.md#g01--security-context-provenance-and-preservation-of-security-criteria
[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g01-ergon-authorisation-and-identity-delegation-task.md
[predecessor]: DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.001.md
[revision-task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g01-ergon-authorisation-and-identity-delegation-draft-001-002-task.md
[dr3]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-3-information-model-value-sets-and-construction
[dr4]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-4-consumer-migration-and-architectural-conformance
[dr7]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-7-pragma-fhir-security-context-authority
[first-review]: ../evidence/software-0.1.0/001/after/codex-1st-pass-review-of-Calliope-code.md
[gaps003]: ../evidence/software-0.1.0/003/after/F-01-Implementation-Gap-Analysis.md
[local-enforcement]: ../../continuum/strategy/capability/feature/aeecdc314b3a4e47a0ba46eed5dc8ce6.md#architectural-content
[boundary-access]: ../../continuum/strategy/capability/f3a812ac9b0f4d6cb4e88eaf66588492.md#architectural-content
[propagation]: ../../continuum/strategy/capability/39da082e53a949e6b6652081148b7e28.md#architectural-content
[currency]: ../../continuum/strategy/capability/e01dc2cb39e94febac2f525ab54aca18.md#architectural-content
[context-validation]: ../../continuum/business/business-service/f1ff9e6542004a7584a385062cb0ed21.md#architectural-content
[policy-evaluation]: ../../continuum/business/business-service/a025ac002b6a461abaac07fcb87f411f.md#architectural-content
[nfr035]: ../../continuum/motivation/constraint/mandate/8812fcd52d3f4afcbeef41b60bda5874.md#architectural-content
[nfr038]: ../../continuum/motivation/constraint/mandate/e04672ef7f2341b68c09a0348742a861.md#architectural-content
[nfr077]: ../../continuum/motivation/constraint/mandate/4522bf0326334607a4a6177c819e61b6.md#architectural-content
[nfr078]: ../../continuum/motivation/constraint/mandate/4b540b55db1b4c1197482d8f29d70e45.md#architectural-content
[sec004]: ../../continuum/motivation/requirement/1f6822aef4bc44548c6708fcc018e4ba.md#architectural-content
[sec005]: ../../continuum/motivation/requirement/d70e0a51440640b1bd17562b6ea4ff98.md#architectural-content
[axioms]: ../../governance/architectural-axioms.md
[ax05]: ../../continuum/motivation/principle/0412a2084f0145babc4f022c71fea65f.md#architectural-content
[ax13]: ../../continuum/motivation/principle/b6a55decfeec4f1bb3d7f18534a2ffea.md#architectural-content
[kleio-capture]: ../../continuum/motivation/principle/architectural-axiom/5de2ae2c3b794c2d9f3a14efd51d564f.md#kleio-mandatory-capture-authority
[distributed-pep]: ../../continuum/motivation/principle/architectural-axiom/172720628fa540928e34b52562c7067f.md#architectural-content
[passport-principle]: ../../continuum/motivation/principle/architectural-axiom/284b53f4a3c54acdba9e707124e997b8.md#architectural-content
[forward]: ../../continuum/motivation/constraint/5aed12f6540445ed85c1866adbef93af.md#architectural-content
[themis]: ../../continuum/application/application-component/899d34ec491948b68aad09e507dea6d1.md#architectural-content
[pep-context]: ../../continuum/application/application-component/899d34ec491948b68aad09e507dea6d1.md#pep-context
[criteria]: ../../continuum/application/application-component/899d34ec491948b68aad09e507dea6d1.md#authorisation-criteria
[effective-attributes]: ../../continuum/application/application-component/899d34ec491948b68aad09e507dea6d1.md#effective-information-security-attributes
[themis-delegation]: ../../continuum/application/application-component/899d34ec491948b68aad09e507dea6d1.md#themis-availability-and-delegation
[themis-draft]: DRP-Themis-Policy-Passport-Enforcement.draft.001.md
[ergon]: ../../continuum/application/application-component/49c9eb2bef0c47b8b5328621d5fc5479.md#architectural-content
[iu-metadata]: ../../continuum/application/data-object/c05b7e37278141488ecc90b9a9ef96eb.md#current-information-and-operational-allocation
[representation]: ../../continuum/application/data-object/b9891b88150840ada3daefdec6f26c35.md#architectural-content
[mneme]: ../../continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md#architectural-content
[petasos]: ../../continuum/application/application-component/195e5f10db8e419391354197b5d7a8cc.md#architectural-content
[pylai]: ../../continuum/application/application-component/e3e4147fea3640198dc251ed41ee854d.md#architectural-content
[guardrails]: ../../../AGENTS.md#invariant-6-default-deny-security-governance-themis
[incorporation]: ../../governance/continuum-incorporation-praxis.md
