<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 Identity, Authentication and Authorisation Candidate consolidation

**Date:** 2026-10-12. **Outcome:** documentation reconciliation complete;
Identity is faithfully captured for Domain05 Candidate Architecture review.
**Programme boundary:** Step A, A2/A3 bounded adjudication/consolidation;
A4 acceptance remains pending, Step B has not started. No Domain05 completion,
MVP selection, implementation or runtime-programme milestone is claimed.

## Authority and supplied input

The repository [AGENTS.md](../../AGENTS.md), [documentation instructions](../../docs/AGENTS.md),
[Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md)
and [Completion Plan](../../docs/markdown/architecture-completion-plan.md)
govern this work. The supplied
`DesignNote-Identity-Authentication-and-Authorisation-Architecture.md` was read
in full from `/home/hunterm/Downloads/`; it was not assumed to exist in the
repository. Its working status does not itself supersede RADS. The explicit
task authorises reconciliation of its peer-adjudicated Candidate decisions.

The input is recorded as [U12](../../docs/markdown/05-information-systems-architecture/sources.md#u12).
Its SHA-256 at review was
`22cb431cda966d7670104379d6211f8a7e08c5166a1982fba9876636748ec623`.
No copy or alteration of that external file was made. Required incorporated
meaning is self-contained in RADS, without a Downloads dependency.

| Applicable axioms | Preserved architectural consequence |
| :--- | :--- |
| AX-02/03/04 | Standards-based external authentication; replaceable IdP; Harmonia-owned authorisation semantics; FHIR PractitionerRole remains distinct from Harmonia constructs. No policy-engine dependency or class/wire model selected. |
| AX-05/06 | Themis retains model/PRP semantics; Mneme provides governed access and Mnemosyne durable C&G state. Custody, professional assertions and authenticated Identity are distinct. |
| AX-07 | Established authenticated Identity, governed authority and local framework enforcement; no untagged-information bypass or audit-persistence exemption. |
| AX-08/09 | Derivation provenance and policy-significant Kleio evidence remain distinct from operational PEP-Context and diagnostics; no automatic context persistence or audit of every evaluation. |
| AX-10/14/15 | Valid-context local availability, required-context waiting and policy DENY remain distinct; temporal validity differs from revocation; technical unavailability creates no invented decision. |
| AX-17/18 | Deferred representations/mappings stay deferred; PractitionerCapacity, notification receipt, presentation and assurance do not confer authorisation or originating domain authority. |

No axioms or Domains01–04 were changed. Existing ADR-004/005 responsibility
statements were checked for consistency; no ADR was rescinded or silently
replaced. Implementation code and historical plans supplied no architectural
meaning.

## Files reviewed

Review scope was relevant sections and search hits unless stated otherwise.

| Area | Files / inspected material |
| :--- | :--- |
| Instructions and status | Root `AGENTS.md`; `docs/AGENTS.md`; `docs/markdown/architecture-completion-plan.md`, progressive authority loading, Step-A status and completion/report gates. |
| Axioms, principles, decisions | `docs/markdown/governance/architectural-axioms.md`, applicable axioms and responsibility/enforcement boundaries; `docs/markdown/01-motivation/principles/reclassified-principles.md`; `docs/architecture-decisions.md`, ADR-004/005 and relevant security/persistence navigation. |
| Motivation constraints | `docs/markdown/01-motivation/requirements-constraints/master-requirements-catalogue.md`, SEC-001/002/010 and PROV-001; `foundational-requirements.md`, independent-assurance constraints; `external-constraints.md`, identifier/privacy constraints. |
| Strategy | `docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md`, Iris/Twin and responsibility seams; `capabilities/business-capabilities.md`, Security, Privacy & Digital Trust; `capabilities/business-enabling-capabilities.md`, presentation/authentication exclusions. |
| Business | `docs/markdown/03-business-architecture/actors-roles/roles.md`, Business Roles, Practitioner, governance/management/assurance and no information-access grant; `actors-roles/actors.md`, actor kinds; `behaviours/01-entity-management.md`, person identity and Provider Administration; `behaviours/05-intrinsic-enablement.md`, Health Information Control; `information-responsibility/information-responsibility.md`, ownership constraints. |
| Information | `docs/markdown/04-information-architecture/information-families/practitioner.md`, full family; `metamodel/information-architecture-metamodel.md`, managed Information Unit, conceptual partition and state distinctions; `patterns/containment-and-collections.md`, containment/membership distinctions; `governance/authority-custody-provenance.md`, responsibility/authority/custody/consumption. |
| Current Domain05 | All ten files listed in the next table: relevant Identity/Themis/Calliope, Iris, Agora, Ergo/Praxis, Kleio, Dokimasia, PEP/PRP, persistence and gap/source sections. |
| Legacy Themis | `docs/concepts/themis.md`; `docs/concepts/themis-security.md`; `docs/modules/themis.md`; `docs/security/themis.md`, full documents and existing wrappers. |
| Other legacy orientation | `docs/modules/dokimasia.md`, supplied attachment read in full; `docs/concepts/calliope.md` and `docs/modules/calliope.md`, ownership/security nomenclature; `docs/getting-started/terminology.md`; `../../docs/latex-legacy`, Themis row. |
| Legacy publication / diagrams | `../../docs/latex-legacy`, full chapter; Themis section in `../../docs/latex-legacy`; `../../docs/latex-legacy` and `fig-themis-security-pipeline.tex`, relevant declarations and inclusion points. |
| Working source / navigation | Supplied Identity DesignNote, §§1–18 in full. Existing `.junie/reports/2026-10-11-domain05-kleio-themis-petasos-adjudication.md` was consulted only for report convention/navigation, not authority. |

## Files changed

Ten existing RADS views were reconciled; no additional Identity architecture
document was created.

| RADS file | Material change |
| :--- | :--- |
| [Domain05 orientation](../../docs/markdown/05-information-systems-architecture/README.md) | U12 summary, navigation and Candidate review boundary; earlier Themis summary aligned; audit conflict retained. |
| [Component register](../../docs/markdown/05-information-systems-architecture/component-register.md) | C01 nomenclature; C02 complete Identity/Themis model, terminology, responsibilities, constraints and target allocation; C10 terminology; C16 administrative interaction/professional credentials; C18 responsibility-versus-access boundary. |
| [System map](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md) | Responsibility tree and collaboration diagram aligned with external authentication, Calliope vocabulary and precomputed PEP-Criteria. |
| [Feature map](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md) | F01 nomenclature, F03 Identity/Themis allocation, F13 PEP-Criteria wording and F22 future operational responsibility boundary. |
| [Data/persistence](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md) | D03 governed model/context/provenance, D08 PractitionerRole distinctions and D16 ValueSets; no final schema invented. |
| [Middleware](../../docs/markdown/05-information-systems-architecture/middleware.md) | M07 constrained enforcement, trusted Identity and effective attributes; forwarding provenance; Kleio boundaries retained. |
| [Gaps](../../docs/markdown/05-information-systems-architecture/gaps.md) | CDG-15 historical context status and CDG-30 terminology; CDG-42 settled semantics versus twelve deferred items, upstream Business realisation boundary and unresolved audit exception. |
| [Sources](../../docs/markdown/05-information-systems-architecture/sources.md) | U12 provenance, explicit supersession/refinement of U08 wording, reviewed boundaries and exact legacy section dispositions. |
| [Context/persistence management](../../docs/markdown/05-information-systems-architecture/context-and-persistence-management.md) | Persistence-hosted enforcement uses PEP-Criteria through PEP-Context; semantic compartment ownership remains deferred. |
| [Pathways/Journey/handoff](../../docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md) | Local FORWARD wording aligned to PEP-Criteria through PEP-Context. |

Nine legacy files received only section/row supersession wrappers, as listed
below. This report is the only new repository file. Source code, dependencies,
runtime configuration, axioms and programme plans were not modified.

## Architectural statements and terminology

The detailed canonical model is in existing
[C01/C02](../../docs/markdown/05-information-systems-architecture/component-register.md#c02).
The task's seventeen invariants are represented as follows:

| Task invariants | Canonical location / meaning |
| :--- | :--- |
| 1–2 | [Authentication](../../docs/markdown/05-information-systems-architecture/component-register.md#identity-authentication): WHO, SEP, external OIDC/OAuth; Keycloak + PostgreSQL SHALL be the development/test/POC default. No Harmonia IAM/credential/password/MFA framework or Keycloak-specific dependency; authentication grants no authority. |
| 3 | [Nomenclature](../../docs/markdown/05-information-systems-architecture/component-register.md#authorisation-nomenclature): four exact release-bounded ValueSets; sufficient but bounded AuthorisationPoints; Calliope governance versus Themis association/resolution/enforcement. ACCESS/CHANGE/FORWARD is not an exhaustive final inventory. |
| 4–8 | [Criteria](../../docs/markdown/05-information-systems-architecture/component-register.md#authorisation-criteria) and [collections/assignments](../../docs/markdown/05-information-systems-architecture/component-register.md#criteria-collections-and-authorisationroles): dimensions AND; grants UNION; nested flattening preserves UNION; direct and AuthorisationRole grants add; no match means DENY; no negative Criteria, Boolean language or invented Role hierarchy. Same model for human/system Identities. |
| 9 | [Temporal validity/revocation](../../docs/markdown/05-information-systems-architecture/component-register.md#temporal-validity-and-revocation): predetermined bounds versus withdrawal of otherwise valid authority; Criterion/direct/role assignments and applicable Capacity context; no invented invalidation mechanism. |
| 10–11 | [PEP-Criteria/PEP-Context](../../docs/markdown/05-information-systems-architecture/component-register.md#pep-context): precomputed Identity-specific permitted combinations, resolved by Themis with derivation provenance; constrained deterministic local PEP, not a general-purpose interpreter. Valid-context availability retained. |
| 12 | [Effective attributes](../../docs/markdown/05-information-systems-architecture/component-register.md#effective-information-security-attributes): absent metadata is not absent security; governed deterministic values or fail closed; decision-time derivation is distinct from persistent metadata assignment. |
| 13–15 | [PractitionerCapacity](../../docs/markdown/05-information-systems-architecture/component-register.md#practitioner-capacity): eligible set from governed profile/context, one active Capacity for applicable session/operational/roster context, no authority from selection. Operational responsibility/notifications remain separate from access. |
| 16 | [Forwarding](../../docs/markdown/05-information-systems-architecture/component-register.md#themis-availability-and-delegation): Identity / PEP-Criteria pass-off requires FORWARD, preserving originating and receiving/acting Identity; no impersonation or separate delegation subsystem. |
| 17 | [Administration/target allocation](../../docs/markdown/05-information-systems-architecture/component-register.md#authorisation-administration): Themis PAP/PRP/PDP/model/compilation/PEP; Ergo/Praxis governed management/review/approval; Iris interaction; PRP persisted through Harmonia. |

The [terminology table](../../docs/markdown/05-information-systems-architecture/component-register.md#identity-terminology)
distinguishes Practitioner, FHIR PractitionerRole, PractitionerCapacity and
AuthorisationRole. Criterion/Criteria denotes governed model combinations;
PEP-Criteria denotes the precomputed Identity-specific result; PEP-Context is
its operational enforcement context. The Mneme working Context and Information
Unit Identity are not silently redefined. Professional credentials are explicitly
distinguished from IdP login credentials in Iris administration.

Calliope defines security/assurance nomenclature; Themis decides/enforces;
Dokimasia assures; Kleio audits. Governance, Audit, Assurance and Authorisation
remain separate. Existing Dokimasia terminal constraints and Kleio capture rules
were preserved. jCasbin, OPA and Cedar are not architectural dependencies.

## Diagrams and cross-references

The system-map responsibility tree now includes controlled nomenclature,
Criterion/collection/assignment semantics, PEP-Criteria/PEP-Context and the
Ergo/Praxis/Iris administrative boundary. The external authentication SEP is
explicitly outside Harmonia-owned responsibilities.

The existing system-map Mermaid collaboration diagram now shows external
OIDC/OAuth trusted Identity, Calliope nomenclature into Themis and precomputed
PEP-Criteria through PEP-Context to local Themis PEP hosts. A new Mermaid diagram
within C02 shows eligible Capacity selection, one active PractitionerCapacity,
Themis resolution/local enforcement and a dashed future operational-responsibility
branch. That branch establishes no Agora/Synapse mechanics. The conceptual nested
collection tree and conjunction/UNION expressions are textual semantic diagrams,
not class/wire models.

New stable anchors connect nomenclature, authentication, terminology, Criterion,
collections/roles, temporal validity/revocation, PractitionerCapacity, effective
attributes and administration. Existing C01/C02, PEP-Context, availability and
CDG-42 anchors remain valid. U12 links the source record to these meanings;
the orientation, feature/data/middleware/gap views and legacy wrappers point
to the same authority. Upstream Business/Information links are constraints and
terminology references, not newly asserted realisation mappings.

## Conflicts, incomplete descriptions and dispositions

| Finding | Disposition / authority |
| :--- | :--- |
| U08-era policy language and detailed Criteria semantics were left open. | U12 clearly supplies the newer adjudicated Candidate: conjunction within a Criterion, additive UNION across grants, no explicit negative Criteria/general-purpose language. Themis, README, maps and CDG-42 reconciled. Final representations remain open. |
| Criteria was described primarily as a PDP-derived contextual expression, blurring policy combinations and the local enforcement representation. | U12 distinguishes model Criterion/Criteria, precomputed Identity-specific PEP-Criteria and operational PEP-Context. Existing availability and distributed ownership are preserved. |
| Earlier delegation wording allowed a broad architectural possibility without settled semantics. | U12 unambiguously narrows the current position to Identity / PEP-Criteria forwarding with FORWARD and dual-Identity provenance. No general delegation machinery or broadening rule supplied. |
| Existing documentation lacked explicit AuthorisationRole, direct-grant UNION, nested containment, Capacity selection and temporal/revocation distinctions. | Consolidated the supplied adjudication in C02 and aligned the other views; no inferred FHIR mapping, Role hierarchy or invalidation protocol. |
| Authentication boundary existed, but SEP/external IdP/default FOSS position was incomplete; Iris credential language was ambiguous. | U12 authentication position added. Iris text identifies professional-credential proposals separately from IdP login credentials. Agora collaboration identity mapping does not imply IdP ownership. |
| Legacy explicit-deny pipelines, enum/role tables, wildcard/blanket-admin policies and automatic Task-context persistence contradict or overstate the Candidate. | Affected sections wrapped, original bodies unchanged. No enum inventory, wildcard authority, deny-overrides or persisted carrier adopted as current architecture. |
| Legacy Calliope anti-responsibility assigned identity assertions to Themis; glossary/publication descriptions combined authorisation and auditing. | Relevant bullet, rows and publication passages/figures wrapped to external authentication, Calliope nomenclature, Themis and Kleio boundaries. |
| Proposed Kleio audit-persistence exception versus AX-07. | **UNRESOLVED.** No subsequent explicit RADS resolution was found. Existing [CDG-42 conflict](../../docs/markdown/05-information-systems-architecture/gaps.md#retained-audit-authorisation-conflict) remains; no bypass adopted. Mark + ChatGPT must adjudicate essential capture/denied-activity evidence while retaining governed security policy. |
| Domain03 Health Information Control names consent, purpose of use, organisation, patient relationship and break-glass responsibilities without a settled realisation in U12's bounded model. | **UNRESOLVED relationship, not proven coverage failure.** [Recorded in CDG-42](../../docs/markdown/05-information-systems-architecture/gaps.md#upstream-business-security-realisation-boundary). No extra dimensions, negative Criteria, bypass or Capacity mapping invented. Later bounded review/adjudication if additional semantics are required; Step B not commenced. |
| Domain03 Role §3.3 says registered professional; Domain04 Practitioner entity is independent of registration status. | The layer-specific referents were not equated. C02 uses the Domain04 individual/workforce entity meaning and does not infer registration from authentication. Whether the Business Role's qualifier should align with the broader entity requires Mark + ChatGPT review if intended to describe the same population; neither upstream definition was changed. |
| Upstream broad ABAC/RBAC and digital-trust capability language. | Retained at its existing abstraction. It supplies no general policy-language or IdP-ownership decision. U12 specifies the Candidate semantics; no upstream capability/requirement coverage is asserted. |

## Deferred design preserved

The [canonical twelve-item register](../../docs/markdown/05-information-systems-architecture/gaps.md#identity-deferred-design)
explicitly retains:

1. Complete release-specific contents of the four authorisation ValueSets.
2. Exact temporal expression model.
3. Revocation propagation and PEP-Context/PEP-Criteria invalidation.
4. Criteria collection implementation name/representation.
5. PEP-Criteria wire/data representation and carrier relationships.
6. Effective Compartment/Classification derivation ownership and precedence.
7. Detailed PractitionerCapacity selection UI.
8. Exact PractitionerCapacity ↔ FHIR PractitionerRole mapping.
9. Agora/Synapse membership, responsibility, escalation, approval and notification mechanics.
10. Detailed PAP management Praxes/Erga, approval rules, UI and versioning.
11. Forwarding lifecycle and representation.
12. PEP-Criteria caching, refresh and distribution mechanics.

Existing detailed host, persistence and timeout/retry contracts remain deferred.
These deferrals do not reopen the settled Candidate semantics or justify
conventional IAM/RBAC/ABAC/FHIR assumptions.

## Legacy sections superseded

Twelve new wrappers across nine files implement AGENTS.md §1.2:

| Legacy file | Newly wrapped section / row |
| :--- | :--- |
| [Security service](../../docs/security/themis.md) | §1 overview; §4 explicit-deny predicate pipeline, including Mermaid diagram. |
| [Security concept](../../docs/concepts/themis-security.md) | §2 role/authority table; §3 Task-persisted PragmaSecurityContext account. |
| [Themis concept](../../docs/concepts/themis.md) | Built-in Policy Portfolio subsection. |
| [Themis module](../../docs/modules/themis.md) | Built-in Policy Portfolio list within §2.2, including blanket-admin claim. |
| [Calliope concept](../../docs/concepts/calliope.md) | Security evaluation/identity-assertion anti-responsibility bullet only. |
| [Terminology](../../docs/getting-started/terminology.md) | Themis row only, with table-compatible visible boundaries/replacement links. |
| [LaTeX glossary](../../docs/latex-legacy/chapters/appendix-a-glossary.tex) | Themis row only, with table-compatible visible boundaries/replacement links. |
| [LaTeX security chapter](../../docs/latex-legacy/chapters/21-themis-security.tex) | Opening paragraph/pipeline figure; four-gate section. Default-deny section unchanged. |
| [LaTeX application chapter](../../docs/latex-legacy/chapters/03-application-layer.tex) | Themis opening paragraph/multi-tier figure; detailed module descriptions unchanged. |

Removing only the newly inserted wrappers restores every affected legacy file
byte-for-byte to its original clean-worktree/HEAD content, including all prior
wrappers. No entire legacy document is superseded. Legacy diagram source,
implementation inventories, etymology and unrelated material remain unchanged.
The supplied Dokimasia orientation required no changes.

## Validation actually performed

| Check | Result / practical limit |
| :--- | :--- |
| Required architecture suite | **PASS:** `mvn -B -ntp test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`. 90 tests, 0 failures, 0 errors, 0 skipped; BUILD SUCCESS, Maven elapsed 19.909 s. Wrapped in 180-second `timeout` with 10-second termination grace; no timeout/stall occurred. This checks existing implementation architecture, not implementation of the new Candidate semantics. |
| Local Markdown files/anchors | **PASS:** 467 local link occurrences in modified Markdown, including this report, checked against existing files and explicit/GitHub-style heading anchors; zero broken targets/fragments. External network links were not fetched. |
| Markdown structure | **PASS:** balanced fences, unique rendered anchors and 46 rectangular rendered tables; the legacy terminology wrappers retain table structure. |
| Legacy preservation | **PASS:** reversed the recorded insertion manifest in memory and compared exact bytes/SHA-256 with original HEAD content for all nine legacy files; all twelve wrappers are insertion-only. Existing Dokimasia terminal-boundary wording was also compared and is unchanged. |
| Scope / semantic review | **PASS:** reviewed the seventeen invariant locations, all four exact ValueSet names, twelve deferrals, Candidate status, forwarding/availability and retained conflicts against the supplied note/task. No implementation changes or inferred representations. |
| Diagrams | **Manual source review:** new/changed Mermaid relationships agree with the prose; balanced fenced blocks checked. Mermaid module/CLI unavailable locally, so no rendered Mermaid validation is claimed. Historical diagram bodies remain unchanged within superseded publication sections. |
| LaTeX wrappers | **PASS:** all 11 inserted LaTeX replacement links resolve to canonical files/explicit anchors. **PASS, syntax probe:** bounded `pdflatex -interaction=nonstopmode -halt-on-error` compilation of the wrapped glossary row in a temporary tabularx harness produced a one-page PDF. Harness layout warnings are not full-publication layout validation; the complete legacy book was not rebuilt. |
| Whitespace / diff | **PASS:** `git diff --check`; changes confined to documentation and this report. |

Temporary validation scripts/logs were kept under `/tmp`; no test harness or
generated publication artefact was added to the repository.

### Canonical Documentation Assessment

Relevant architecture was found outside RADS in the supplied working Identity
DesignNote and the legacy Themis/Calliope/glossary/publication material listed
above. The note's adjudicated authentication, authorisation, Capacity, context,
management and responsibility semantics belong in Domain05 and were incorporated
under this authorisation. Conflicting legacy descriptions were reconciled through
section-specific supersession; original implementation/history remains available.

No Identity Candidate meaning needed for this review remains dependent solely
on the external note. Detailed mechanisms remain explicitly deferred in RADS;
the audit-authorisation conflict and Business-security realisation relationship
remain unresolved there. Future work must adjudicate those issues and derive
the deferred designs within their own bounded authorisations. This report is
an execution artefact, not architectural authority.

## Review readiness and remaining adjudication

**Yes: RADS now represents the supplied Identity Candidate Architecture faithfully
enough for Domain05 Candidate Architecture review.** Themis and Calliope ownership,
the four distinct Practitioner/Role/Capacity terms, conjunctive/UNION semantics,
temporal validity/revocation, precomputed local enforcement, untagged-information
control and governed administration are explicit and consistent across the views.

Mark + ChatGPT architecture adjudication remains necessary for the Kleio audit
exception and for any required resolution/extension of the Business-security
realisation relationship; the Practitioner registration qualifier is an adjacent
upstream terminology observation for review. The twelve deferred design items
remain deliberate later work. This conclusion is review readiness, not A4
acceptance, proof of Business-security coverage, implementation readiness or
whole-Domain05 closure.
