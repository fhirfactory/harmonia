<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | IU identity/classification and accepted G02 correspondence | **ACCEPTED — reconciled BL-0005 design** | `informationConcept : Topic` supersedes the G02 ConceptFDN profile. Applied. |
| [DR-2](#dr-2) | Cache lifecycle and peer OperationalMetadata allocation | **ACCEPTED — reconciled BL-0005 design** | `CacheLifecycleMetadata` is a direct `InformationMetadata` constituent. Applied. |
| [DR-3](#dr-3) | Explicit information-authority reference under AX-06 | **ACCEPTED — `informationAuthority : Topic` on `InformationMetadata`** | Required member; same-UUID revision semantics recorded in DR-9. |
| [DR-4](#dr-4) | Qualified RAW and release vocabulary authority | **ACCEPTED — corrected RAW/FHIR paths and media separation** | Applied per DR-9; no media-type concept vocabulary. |
| [DR-5](#dr-5) | Verification and candidate readiness | **ACCEPTED — ArchUnit/Paradeigma retained; execution deferred** | NOT EXECUTED — DEFERRED, not a pass. Existing JUnit, Maven verification and `jdeps` evidence is sufficient for BL-0005. |
| [DR-6](#dr-6) | Software 0.1.0 first-baseline and predecessor | **ACCEPTED — continue under `0.1.0-SNAPSHOT`** | No formal Software Release 0.1.0 or tag. HCRL 010 Naming is the preceding accepted development increment; the first formal baseline is deferred until a meaningful integrated implementation exists. |
| [DR-7](#dr-7) | Engineering decisions with semantic effect | **DISPOSITIONED — see DR-9 and DR-13** | Original candidate choices remain historical; accepted implementation semantics and corrections are recorded in the linked DRs. |
| [DR-8](#dr-8) | Architecture-target incorporation of the accepted design | NOT RECORDED | Separate Architecture-target obligation, including Themis-Krites boundary incorporation; not a blocker for this software review. |
| [DR-9](#dr-9) | Accepted implementation-review corrections | ACCEPTED AS DESIGN DIRECTIVES; IMPLEMENTATION CANDIDATE PENDING | Applied to corrected candidate; no software acceptance or HCRL closure inferred. |
| [DR-10](#dr-10) | Generic profile-schema support and final verification disposition | **ACCEPTED — generic profile-schema capability; DR-5 deferral confirmed** | Profiles use their own governing organisation and standard; no resource relationship is implied. ArchUnit remains required for later complete-subsystem work. |
| [DR-11](#dr-11) | Profile-schema helper contract versus generic Topic model | **CLARIFIED BY SOLUTION ARCHITECT; FINAL QUALIFICATIONS CLOSED IN DR-12** | Restrictions apply only to `InformationVocabulary.profileSchema`; DR-12 clarifies Draft §1.6 and adds permanent regression coverage. |
| [DR-12](#dr-12) | DR-11 final documentation and regression corrections | **APPLIED — CANDIDATE PENDING SOLUTION ARCHITECT ACCEPTANCE** | Candidate-004 records the clarified helper boundary and regression test; no software acceptance, release allocation or Git transition is inferred. |
| [DR-13](#dr-13) | DR-7 final reconciliation: successors, immutability and RAW ContentSchema | **APPLIED — ACCEPTED WITH CANDIDATE-005 (DR-14)** | Candidate-005 records the adjudicated successor contracts and separate RAW ContentSchema. |
| [DR-14](#dr-14) | Candidate-005 implementation acceptance and closure | **ACCEPTED — BL-0005 development increment** | Registered as [complete][register]; the ArchUnit deferral (DR-5) and Architecture incorporation obligation (DR-8) continue. |

# HCRL software-0.1.0 / 011 — BL-0005 Calliope InformationUnit

**Status** · **CLOSED — ACCEPTED; REGISTERED AS [COMPLETE][register].** The Solution Architect accepted candidate-005 as the BL-0005 Calliope InformationUnit development increment and recorded the DR-6 disposition (DR-14). Apart from these closure entries, this review record and its decision history are preserved.

**Status before closure (preserved)** · **PENDING SOLUTION ARCHITECT ACCEPTANCE OF THE INTEGRATED CANDIDATE-005.** This iteration first stopped for architectural adjudication. The Solution Architect then supplied attributable dispositions and instructed Junie to proceed. The implementation and evidence incorporate the accepted profile-schema, verification, successor, immutability and RAW ContentSchema decisions; DR-11/DR-12 preserve the profile-helper clarification and DR-13 records the final DR-7 reconciliation. Architectural acceptance of the reconciled design (DR-1 to DR-4 and DR-9 to DR-13) is **not** software implementation acceptance, release acceptance or Git-transition authority.

**Origin** · Accepted Architecture `1.1.26`; branch `feature/BL-0005-Calliope-InformationUnit`; `HEAD` commit `a2e68b9f871102c435dca4cc2583ea69d881dfe1`, tree `d1af1bb1069345c973a54e77729a616aed5f45cb`. At task entry the worktree already contained a modified Draft 002.004 and an untracked Backlog Item 0005. Both are preserved unchanged and are not part of the candidate patch.

**Assignment and evidence** · [Originating Task][task] · [Resumption Task][task-resume], which holds the earlier verbatim dispositions · [Review-Corrections Task][task-corrections] · [Final-Corrections Task][task-final] · [Helper-Clarification Task][task-clarify] · [DR-11 Final-Corrections Task][task-dr12] · [DR-7 Final-Reconciliation Task][task-dr13] · [Stop Report][report-stop] (preserved) · [Prior Completion Report][report-prior] · [Review-Corrections Report][report] · [Final-Corrections Report][report-final] · [Helper-Clarification Report][report-clarify] · [DR-11 Final-Corrections Report][report-dr12] · [DR-7 Final-Reconciliation Report][report-dr13] · [Candidate-003 patch][patch] · [Candidate-004 patch][patch-004] · [Current candidate-005 patch][patch-005] and [SHA-256][patch-005-sha]. Specifications: [Backlog Item 0005 revision `000.004`][backlog] and [Draft 002.007 §1][draft]. Earlier revisions remain preserved historical versions.

**Software predecessor** · Completed [HCRL 009][hcrl-009] established the `0.1.0-SNAPSHOT` development foundation without an accepted software predecessor. [HCRL 010][hcrl-010] accepted the standalone Naming development candidate but established neither a Software Release 0.1.0 nor a tag. This HCRL allocates no release.

**Architecture baseline and dependencies** · Architecture `1.1.26` remains the current incorporated baseline. [HCRL 1.1.28/001][hcrl-1.1.28] and [HCRL 1.1.27/002][hcrl-1.1.27] remain pending and are neither modified nor advanced here.

<a id="dr-1"></a>
## DR-1 — IU identity/classification and accepted G02 correspondence

**Original review conclusion (stop, preserved)** · Accepted G02 [Final.001.000 §2][g02] defines immutable `InformationUnit.informationProfile : ConceptFDN` with the fixed value `solution=harmonia.domain=internal.concept=InformationUnit`. Under G02 a Topic may be a derived projection, but it does not replace the profile. Draft 002.004 §1.1 and Backlog 0005 instead replace the profile with `informationConcept : Topic`. Accepted [software HCRL 010][hcrl-010] covers Naming only.

**Solution Architect disposition** · Recorded verbatim in the [Resumption Task][task-resume]: "The DRP was reviewed and approved to override the ConceptFDN with the InformationConcept name." Under the disposition-standing statement, Draft 002.004 does not override G02 by its own status. The authority is the Solution Architect's explicit acceptance of the reconciled BL-0005 design, after the differences were reviewed and adjudicated.

**Superseded differences** · G02 Final.001.000 §2 as it applies to the Calliope IU model:

1. `informationProfile : ConceptFDN` with a fixed profile value is replaced by the per-IU `informationConcept : Topic`.
2. The IU designation becomes `informationConcept.getName() + id`, with no separator and no legacy binding.

**Preserved obligations** · The UUID remains the sole instance identity. Topic value identity remains version-independent. The independent IU identity in HCRL 1.1.28/001 DR-1, and the IU qualifier vocabulary and specialised mappings deferred by its DR-4, are not decided here. No other G02 matter is affected.

**Application** · `InformationUnit.getId()`, `getInformationConcept()` and `getName()`. Evidence: `InformationUnitIdentityTest`, which covers the exact name with qualified, versioned Topics and MI-05 matching through `TopicMask.matches`.

<a id="dr-2"></a>
## DR-2 — Cache lifecycle and peer OperationalMetadata allocation

**Original review conclusion (stop, preserved)** · Completed [HCRL 1.1.25 DR-2][hcrl-1.1.25] allocates Mneme operational revision, per-manifestation Active Generation, cache/working facts and operation/handshake facts to associated peer `OperationalMetadata`. It explicitly rejects automatic IU constituency. Draft 002.004 §§1–2 make `CacheLifecycleMetadata` a direct constituent of `InformationMetadata`.

**Solution Architect disposition** · "The referenced DRP-Information-Unit-Framework.Draft.002.004.md is the reviewed and approved model baseline. It should override pre-existing spefications." This is read with the disposition-standing statement recorded in the [Resumption Task][task-resume].

**Superseded difference** · In the Calliope IU model, cache state and Active Generation move from HCRL 1.1.25 DR-2's peer `OperationalMetadata` into a direct `InformationMetadata.CacheLifecycleMetadata`. Likewise, Managed Revision, disposition and legal hold become `InformationMetadata.PersistenceLifecycleMetadata`.

**Preserved obligations** · Active state never becomes authoritative (Invariant 8). Managed Revision stays distinct from operational revision, Active Generation, Data Version, schema Topic versions and native payload versions. Mneme's operational revision and the persistence handshake remain behavioural responsibilities. They are **not** modelled as fields, because Draft §1.2 lists only cache state and Active Generation. Physical co-persistence is not asserted.

**Application** · `PersistenceLifecycleMetadata` and `CacheLifecycleMetadata`. Evidence: `InformationMetadataTest`, which covers independent axes, a non-regressing Managed Revision and bounded opaque generations.

<a id="dr-3"></a>
## DR-3 — Explicit information-authority reference under AX-06

**Original review conclusion (stop, preserved)** · Draft 002.004 §1.5 requires an explicit reference to the applicable governed information authority under [AX-06][axioms]. Its §1 model tree defines no member for it, and `Canonical` quality cannot substitute for one.

**Solution Architect disposition** · The final, superseding reply: "add informationAuthority : Topic directly to InformationMetadata, rather than InformationQuality." An earlier reply had placed authority concepts in `InformationQuality`. That reply is preserved in the Resumption Task and superseded for the member.

**Application** · `InformationMetadata.getInformationAuthority()` is required by `InformationMetadata.initial(Topic)`. It carries forward through coordinate revisions and derived successors. DR-9 authorizes changing it through a new governed revision under the same UUID while preserving the previous snapshot and expected Managed Revision. `InformationQuality` remains unchanged and holds only `owner`, `grade` and `certifications`. Evidence: `InformationMetadataTest.everyContainerIsInitialisedAndTheAuthorityIsExplicit`, `InformationQualityTest.everyGradeIsCarriedIndependentlyOfTheAuthorityReference` and `InformationUnitRevisionTest.authorityChangeCreatesANewSnapshotUnderTheSameUuidAndRetainsExpectedRevision`.

<a id="dr-4"></a>
## DR-4 — Qualified RAW and release vocabulary authority

**Original review conclusion (stop, preserved)** · Backlog 0005 requires an approved qualified `RAW` Topic and release-defined vocabulary builders, but the reviewed sources supplied neither.

**Original Solution Architect disposition (superseded in part by DR-9)** · RAW was supplied as `qualifier=System,value=Harmonia.qualifier=Domain,value=Data.qualifier=InformationContainer,value=RAW`. The schema vocabulary derives "from FHIR R4 (resource names) and MediaTypes". Media-format identifiers must not populate ContentSchema builders, and no new representation field is introduced.

**Current Solution Architect disposition** · DR-9 supplies the exact RAW path and exact FHIR resource containment path, distinguishes media types from concepts and schemas, and defines FHIR release/resource versioning.

**Application** · `InformationVocabulary.RAW_CONTAINER_SCHEMA` is `Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW`; RAW means Calliope imposes no additional container structure. `FhirR4ResourceType` lists all 146 concrete resource types and builds `Domain=Health.Owner=HealthLevelSevenInternational.Standard=FHIR.Release:4.0.1=R4.Resource=<name>`. `R4` is the recognized release designation; `4.0.1` is its specific version; resource definitions have no independent versions. The list is pinned by SHA-256 `07e47572…d9d1532ea` in `InformationVocabularyTest`.

`ContainerMetadata.containerMediaType` and `ContentMetadata.contentMediaType` carry required IANA media type identifiers, separate from concept and schema Topics. `MediaTopLevelType` supports syntax validation only; no media type is a content concept or schema. The hierarchy is `Domain → Owner → Standard`; each standard defines subordinate containment and the level at which versioning applies. Qualified Topic paths express containment without a separate relationship model. Draft 002.005 and Backlog Item 0005 revision `000.002` preserve these reconciled requirements while their earlier versions remain unchanged.

<a id="dr-5"></a>
## DR-5 — Verification and candidate readiness

The first table records verification of the initial candidate, before the successive corrections in DR-9 and DR-10. Later test totals reflect the added/revised coverage and are not conflicting results.

| Command / check | Result (UTC finish) |
| --- | --- |
| `mvn -B -ntp -pl calliope -am test` | **PASS** · `2026-11-26T02:10:37Z` · 318 tests, 0 failures, 0 errors, 0 skipped: 91 Naming regression and 227 InformationUnit tests. |
| `mvn -B -ntp verify` (solution-wide) | **PASS** · `2026-11-26T02:10:40Z` · all 50 reactor modules succeeded. Only Calliope contains tests; there are no integration tests to run. |
| `mvn -B -ntp -pl calliope -DskipTests javadoc:javadoc` | **PASS** · 0 warnings in the Calliope Javadoc output. |
| `jdeps --list-deps calliope/target/classes` | **PASS** · `java.base` and `java.xml`; `java.xml` comes from the existing Naming XML codec. |
| ArchUnit selector: `mvn -B -ntp -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false test` | **NOT EXECUTED — DEFERRED** · prior selector invocation finished `2026-11-26T02:10:41Z` after reporting "No tests to run". No ArchUnit test ran; **not a pass**. |

`InformationStructureTest` adds source-level checks for layering, size, licence headers, absence of logging, simulation and persistence, and non-`Serializable` model types. It complements the absent suite and does not replace it.

**Solution Architect disposition** · ArchUnit/Paradeigma is retained, not retired. Execution is deferred until Harmonia begins implementing complete subsystems. For BL-0005, the existing JUnit, Maven verification and `jdeps` evidence is sufficient. This is a bounded deferral, not a PASS, removal of the standing requirement, or edit to `AGENTS.md`; future complete-subsystem work must apply the retained requirement.

<a id="dr-6"></a>
## DR-6 — Software 0.1.0 first-baseline and predecessor

Unchanged from the stop review. [HCRL 009][hcrl-009] established the development foundation without an accepted software predecessor. [HCRL 010][hcrl-010] accepted Naming as a standalone development candidate. Before release closure, the Solution Architect establishes the first accepted Software 0.1.0 baseline, the exact resulting source and the predecessor treatment. No release is allocated and no Git transition is authorised.

**Solution Architect disposition · ACCEPTED** (preserved verbatim in the [Closure Task][task-closure]):

- Continue under `0.1.0-SNAPSHOT`.
- Do not allocate a formal Software Release 0.1.0 or create a release tag.
- Treat HCRL 010's accepted Naming implementation as the preceding accepted development increment.
- Establish the first formal software baseline when a meaningful integrated implementation is available.

This HCRL therefore records an accepted development increment, not a software release or a terminal tagged baseline. `0.1.0-SNAPSHOT` remains a mutable development identity and does not by itself locate accepted bytes. The accepted source is located by the commit and tree in the [register record][register].

<a id="dr-7"></a>
## DR-7 — Engineering decisions with semantic effect

The following items record choices made for the initial candidate. The original statements remain as history. DR-9 supersedes items 1–3 and 9; DR-13 adjudicates item 4 and replaces the initial item 8 fallback decision. Items 5–7 are accepted as implemented by DR-13; the remaining scope boundary in item 10 is unchanged.

1. **Initial candidate: coordinate revision clears the Managed Revision.** `reviseCoordinates` returned a snapshot with no Managed Revision until Mnemosyne established one. Superseded by DR-9: retain the originating revision as the next write's expected revision.
2. **Initial candidate: authority is required and has no change operation.** Superseded by DR-9: permit a new governed authority revision under the same UUID, preserving the previous snapshot.
3. **Initial candidate: security is strengthen-only.** Removing a requirement or changing its entry version was rejected. Superseded by DR-9: Themis-Krites adjudicates strengthening, weakening and replacement; Calliope makes no security-policy decision.
4. **Initial derived successors** (`deriveSuccessor`) carried forward concept, description, quality, representation, authority, coordinates and security. Lifecycle and origin started unknown. Relationships held one `INTERNAL_IU` provenance link, and the caller supplied a changed representation or quality. This candidate behavior is superseded by DR-13's separate clone and minimum-metadata successor operations.
5. **Origin idempotence** compares Topics by versioned canonical form. A re-versioned value counts as a change. **Accepted as implemented by DR-13:** established OriginMetadata Topics cannot change, including their versions.
6. **`description` is replaceable** through `withDescription`; the Draft does not make it immutable. **The quality `owner` is required and non-blank. Accepted as implemented by DR-13:** description is revisionable, and `InformationQuality.owner` is required, non-blank and lifetime-immutable.
7. **Content and detail validation.** `Content.inlineText` encodes UTF-8. Reference locators must be absolute URIs. `ContentDetail` requires size ≥ 0 and positive height, width, frames and pages, a non-negative duration, and a BCP-47-shaped language tag. A checksum and its algorithm must be supplied together. `SHA-256` is never applied silently. **Accepted as implemented by DR-13:** retain these validation rules.
8. **Initial candidate required `contentConcept` and `contentSchema`** but defined no content-schema fallback analogous to `RAW`. The absence of that fallback is superseded by DR-13; both fields remain mandatory and the new RAW ContentSchema Topic is distinct from the existing RAW container schema.
9. **Initial candidate: vocabulary Topic paths derived by Junie:**
   - `…ContentSchema:4.0.1=FHIR.Resource=<type>[.Profile[:v]=<name>]` (FHIR version on the `ContentSchema` entry)
   - `…ContentConcept=Media.MediaType=<top-level>`

   This initial interpretation is superseded by DR-9: media types are dedicated fields and are not content concepts; the approved RAW and FHIR paths and containment/versioning rules are specified there.
10. **Out of scope and not implemented:** Summary, codecs or serialisation, Store selection, persistence and cache operations, reference resolution, and Themis enforcement.

<a id="dr-8"></a>
## DR-8 — Architecture-target incorporation of the accepted design

DR-1 to DR-4 and DR-9 to DR-13 record accepted BL-0005 design decisions that supersede specified parts of accepted G02 Final.001.000 §2 and HCRL 1.1.25 DR-2 while preserving unrelated obligations. The Continuum IU Class, IU Metadata and related elements still describe earlier allocations. Draft 002.007 remains an offline-design document; Drafts 002.004 to 002.006 are preserved unchanged. Incorporating the accepted design, including generic profile-schema support, the Themis-Krites boundary and the DR-13 successor/content-schema decisions, into the Continuum and any Architecture patch increment is a separate Architecture-target transition with its own HCRL. This software HCRL neither performs nor duplicates that transition and allocates no Architecture release. The obligation is not a blocker to reviewing this software candidate.

<a id="dr-9"></a>
## DR-9 — Accepted implementation-review corrections

**Authority and standing** · The Solution Architect's implementation-review correction instruction, preserved in the [Review-Corrections Task][task-corrections], accepts the following design directives for BL-0005. These directives correct the implementation candidate and establish the corresponding successor design/backlog text. They do not accept the implementation candidate, close this HCRL, incorporate a Continuum element or allocate either a software or Architecture release.

**Superseded candidate decisions and affected differences** · This disposition supersedes DR-7 items 1–3 and 9 and the corresponding initial-candidate statements in the prior Completion Report. The earlier G02 ConceptFDN decision (DR-1) and HCRL 1.1.25 cache allocation decision (DR-2) remain as separately adjudicated; this correction does not reopen or broaden those decisions.

1. **Managed Revision:** local modifications retain the originating Managed Revision as the expected revision for the subsequent governed write. Mnemosyne establishes the successor Managed Revision only after successful persistence. This replaces the initial candidate's clearing behavior while preserving the distinct revision axes, immutable authoritative history and existing write-handshake obligations.
2. **Information authority:** required `InformationMetadata.informationAuthority : Topic` may change through a new governed IU revision under the same UUID. The earlier snapshot and its authority remain unchanged; the originating Managed Revision is retained as the next write's expected revision. `InformationQuality` remains unchanged.
3. **Security:** `SecurityMetadata` remains immutable, but the add-only restriction is withdrawn. Themis-Krites is the authority responsible for adjudicating every proposed change, including strengthening, weakening and replacement. Calliope stores and carries values and must not make security-policy decisions. This scope introduces the architectural boundary only: it does not implement a Krites service or JPMS and does not relax Themis default-deny or existing authorization obligations.
4. **Media types:** add required `ContainerMetadata.containerMediaType : String` and `ContentMetadata.contentMediaType : String`, using IANA media type identifiers. Keep them distinct from content concepts and schemas; remove media-type content-concept vocabulary values. The model validates identifier syntax and a recognized top-level token, not live IANA subtype-registry membership.
5. **RAW:** use exactly `Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW`. `RAW` means Calliope imposes no additional container structure.
6. **FHIR R4:** support all 146 resource types using `Domain=Health.Owner=HealthLevelSevenInternational.Standard=FHIR.Release:4.0.1=R4.Resource=<resource-name>`. `R4` is the recognized release designation; `4.0.1` is its specific version; resource definitions have no independent versions.
7. **Containment:** retain the common `Domain → Owner → Standard` hierarchy. The governing standard defines its subordinate containment and the level at which versioning applies. The qualified Topic path expresses containment; no separate relationship model is introduced.

**Application and evidence** · Draft 002.005 and Backlog Item 0005 revision `000.002` are successor documents; Draft 002.004 and Backlog Item revision `000.001` remain unchanged. The corrected candidate implements the Managed Revision and authority revisions, policy-neutral security value proposals, required media fields, and exact RAW/FHIR vocabulary. Evidence is in the [Review-Corrections Report][report] and corrected [candidate patch][patch]. The HCRL remains pending for Solution Architect review of software implementation and all unresolved dispositions.

**Post-correction verification** · Actual commands and results are recorded below after execution; UNEXECUTED and other incomplete results remain explicit.

| Command / check | Result (UTC finish) |
| --- | --- |
| `mvn -B -ntp -pl calliope -am test` | **PASS** · `2026-11-26T08:57:26Z` · 309 tests, 0 failures, 0 errors, 0 skipped: 91 Naming regression and 218 InformationUnit tests. |
| `mvn -B -ntp verify` | **PASS** · `2026-11-26T08:57:38Z` · all 50 reactor modules succeeded; no integration tests are configured outside the Calliope unit suite. |
| `mvn -B -ntp -pl calliope -DskipTests javadoc:javadoc` | **PASS** · `2026-11-26T08:57:49Z` · Javadoc generated successfully with no warnings in the command output. |
| `jdeps --list-deps calliope/target/classes` | **PASS** · `2026-11-26T08:58:38Z` · dependencies are `java.base` and `java.xml`; `java.xml` comes from the existing Naming XML codec. |
| ArchUnit selector: `mvn -B -ntp -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false test` | **NOT EXECUTED — DEFERRED** · prior selector invocation finished `2026-11-26T08:58:02Z` after reporting "No tests to run". No ArchUnit test ran; **not a pass** (disposition in DR-5/DR-10). |

`InformationStructureTest` also passes within the Calliope suite. It checks source-level layering, size, licence headers, absence of logging, simulation and persistence, and non-`Serializable` model types; it complements but does not replace the absent ArchUnit suite.

**Requested treatment** · Review and accept or reject the corrected software candidate. The ArchUnit/Paradeigma check is retained but its execution is deferred under DR-5/DR-10; no test pass or software acceptance is inferred.

**Minutiae** · Task-Issued-At and Report-Produced-At remain `UNRESOLVED` for all four Tasks and all four Reports. The attributable system event times were not available at sufficient precision, and no filename timestamp has been inferred. The verification finish times above are command-completion times, not provenance event times.

<a id="dr-10"></a>
## DR-10 — Generic profile-schema support and final verification disposition

**Authority and standing** · The Solution Architect's final-corrections instruction for this HCRL iteration authorizes restoration of generic profile-schema capability and confirms the verification treatment in DR-5. This is an attributable design disposition for BL-0005; it does not accept the implementation candidate, close HCRL 011, or authorize a release or Git transition.

**Generic profile-schema disposition** · Restore generic profile-schema support using the common `Domain → Owner → Standard` containment principle. Profile definitions belong under their own governing organisation and standard. Preserve any standard-defined intermediate containment and append the profile entry directly to that governing-standard Topic. A profile Topic is independent of any underlying FHIR resource and carries no implicit relationship to one. Do not restore the obsolete `System=Harmonia.Domain=Data...` path, add AU Core-specific vocabulary, or invent an organisation-specific hierarchy.

**Application and evidence** · `InformationVocabulary.governingStandard` builds the common prefix with optional standard-entry versioning; `profileSchema` appends an optionally versioned generic profile while preserving standard-defined intermediate entries. The builder rejects a base not beginning with `Domain`, `Owner`, `Standard`, and rejects resource-bound or nested-profile bases. Tests cover caller-specific governing owners and standards, intermediate containment, version identity/rendering, malformed inputs and separation from the FHIR resource vocabulary. The [final-corrections candidate][patch] and [Report][report-final] record the implementation and verification.

| Verification | Result |
| --- | --- |
| `mvn -B -ntp -pl calliope -Dtest=InformationVocabularyTest test` | **PASS** · 155 tests; includes all 146 FHIR R4 cases and five new profile-schema tests. |
| `mvn -B -ntp -pl calliope -am test` | **PASS** · `2026-11-26T10:12:54Z` · 314 tests, 0 failures, 0 errors, 0 skipped: 91 Naming and 223 InformationUnit tests. |
| `mvn -B -ntp verify` | **PASS** · `2026-11-26T10:13:03Z` · all 50 reactor modules succeeded. Empty-JAR warnings for empty modules were non-fatal. |
| `mvn -B -ntp -pl calliope -DskipTests javadoc:javadoc` | **PASS** · `2026-11-26T10:13:17Z` · Javadoc generated without warnings. |
| `jdeps --list-deps calliope/target/classes` | **PASS** · dependencies are `java.base` and `java.xml`. |
| ArchUnit/Paradeigma selector | **NOT EXECUTED — DEFERRED** · accepted disposition in DR-5; not a pass. |

**DR-5 verification disposition** · ArchUnit/Paradeigma is retained, not retired. Execution is **NOT EXECUTED — DEFERRED** until Harmonia begins implementing complete subsystems. This is not a PASS and does not remove or weaken the `AGENTS.md` requirement. For BL-0005, the existing JUnit, Maven verification and `jdeps` evidence is sufficient. The earlier selector invocation selected no tests; no ArchUnit tests ran. The verification performed for this final correction is recorded in the [Final-Corrections Report][report-final].

**Preserved obligations and scope** · No change is made to `AGENTS.md` or the architecture-level ArchUnit requirement. This disposition does not adjudicate unrelated HCRL 1.1.27 matters or alter Architecture release authority. HCRL 011 remains pending for Solution Architect review of the integrated software candidate.

<a id="dr-11"></a>
## DR-11 — Profile-schema helper contract versus generic Topic model

**Solution Architect clarification** · Preserved verbatim in the [Helper-Clarification Task][task-clarify]:

1. The generic `Topic` model must support valid containment hierarchies defined by the governing standard, without arbitrary structural restrictions.
2. `InformationVocabulary.profileSchema` may enforce restrictions appropriate to its documented helper contract.
3. Those restrictions must not limit the underlying Topic model or prevent callers from constructing other valid standard-defined hierarchies.

This clarifies the scope of DR-10. It does not change DR-10's disposition, accept the candidate or authorise a release or Git transition.

**Verification result at the time of the Helper-Clarification Report** · **CONFIRMED — already satisfied; no source or test code changed.** [Candidate-003][patch] and its SHA-256 were current at that point; DR-12 subsequently supersedes it with candidate-004.

- The `Domain → Owner → Standard` prefix check and the `Resource`/`Profile` base exclusions exist only in `InformationVocabulary.profileSchema`'s private base validation, and its Javadoc documents them as helper behaviour.
- Naming (`Topic`, `TopicBuilder`, the canonical parsers) is unchanged by BL-0005 and imposes no qualifier name, order, depth or repetition rule.
- `ContainerMetadata`, `ContentMetadata`, `InformationMetadata`, `InformationCoordinates` and `InformationUnit` accept any non-null Topic without inspecting its entries.
- The package documentation and `governingStandard` Javadoc direct callers to the Naming API for other hierarchies.

| Verification | Result |
| --- | --- |
| `mvn -B -ntp -q -pl calliope -am test` | **PASS** · finished ≈ `2026-11-26T21:56Z` (Surefire report write time) · 314 tests, 0 failures, 0 errors, 0 skipped: 91 Naming and 223 InformationUnit tests. |
| Scratch probe outside the repository, compiled against `calliope/target/classes` and not retained | **PASS** · Three Topic shapes were built through the Naming API: a profile beneath `Resource`, a nested profile, and a path not starting with `Domain`. Each round-tripped with its versions preserved and was accepted as concept and schema by `ContentMetadata.of` and `ContainerMetadata.of`. `profileSchema` rejected the first two as bases with `INVALID_INPUT` at `vocabulary.governingStandard`, without echoing values. |
| `mvn -B -ntp verify`, Javadoc, `jdeps` | Not rerun; no code changed. The DR-10 results stand. |
| ArchUnit/Paradeigma selector | **NOT EXECUTED — DEFERRED** under DR-5; not a pass. |

Among the existing tests, `InformationVocabularyTest.profileBaseMustBeAGoverningStandardAndCannotBeResourceBoundOrNested` constrains only helper inputs, and it first builds the rejected bases successfully as Topics. `InformationFixtures` supplies non-`Domain` (`System=Harmonia…`) concept and schema Topics throughout the IU tests.

**Qualifications closed by DR-12**

1. The Helper-Clarification Report noted that no permanent test directly asserted the distinction. DR-12 adds `StandardDefinedTopicHierarchyTest.validStandardDefinedTopicsRemainUsableAcrossTheInformationUnitModel`; the qualification is closed.
2. The Helper-Clarification Report noted that Draft 002.006 §1.6 could read as a general restriction. DR-12 clarifies that the `Resource`/`Profile` restrictions belong only to `InformationVocabulary.profileSchema`; the generic Topic model and other valid standard-defined hierarchies remain unrestricted by that helper contract. The qualification is closed in the existing Draft; no successor Draft is required for this correction.

**Minutiae** · Task-Issued-At and Report-Produced-At for this clarification remain `UNRESOLVED`; none is inferred. The Report is the [Helper-Clarification Report][report-clarify].

<a id="dr-12"></a>
## DR-12 — DR-11 final documentation and regression corrections

**Solution Architect disposition** · The Solution Architect accepts the helper-specific interpretation recorded in DR-11 and directs the two targeted corrections in the [DR-11 Final-Corrections Task][task-dr12]. This accepts the interpretation and authorizes these bounded corrections only; it is not acceptance of the software implementation, closure of HCRL 011, release allocation or Git-transition authority.

**Corrections applied**

1. **Draft 002.006 §1.6:** explicitly states that `InformationVocabulary.profileSchema` alone applies its `Domain → Owner → Standard` base requirement and rejects additional `Resource` or `Profile` entries after that prefix. These helper-specific restrictions do not constrain the generic Topic model or prevent other valid standard-defined containment hierarchies from being built through Naming and used by InformationUnit. The obsolete `System=Harmonia.Domain=Data...` schema path remains excluded. A Standing note records the clarification.
2. **Permanent regression test:** `StandardDefinedTopicHierarchyTest.validStandardDefinedTopicsRemainUsableAcrossTheInformationUnitModel` covers a versioned resource-bound profile, a versioned nested profile and a non-`Domain`-rooted path with a repeated qualifier. It verifies qualified path/version round-trips and accepts the Topics as IU concept, authority, coordinates, container and content values; authority revision preserves the prior snapshot. It also confirms the profile helper still rejects its two restricted bases.

The earlier DR-11 qualifications are closed. No production source, Naming implementation, accepted behavior outside this bounded correction, or candidate-003 bytes were changed.

| Verification | Result |
| --- | --- |
| `timeout 120s mvn -B -ntp -pl calliope -am -Dtest=StandardDefinedTopicHierarchyTest -Dsurefire.failIfNoSpecifiedTests=false test` | **PASS** · 1 test, 0 failures, 0 errors, 0 skipped · `2026-11-27T09:06:35+11:00`. |
| `timeout 150s mvn -B -ntp -pl calliope -am test` | **PASS** · 315 tests, 0 failures, 0 errors, 0 skipped: 91 Naming and 224 InformationUnit · `2026-11-27T09:07:14+11:00`. |
| `timeout 300s mvn -B -ntp verify` | **PASS** · all 50 reactor modules succeeded; Calliope's 315 tests passed · `2026-11-27T09:07:23+11:00`. Non-fatal empty-JAR warnings were emitted. |
| Candidate-003 temporary-index round-trip | **PASS** · applying candidate-003 to a temporary index and regenerating its binary diff reproduced its bytes exactly; the repository index was untouched. |
| Candidate-004 checksum and baseline apply/whitespace check | **PASS** · 45-file candidate; SHA-256 `0a075d93cbf63d797b5e24c14eb95e6c8bdd755b8a935a11c0c8c78e5111a8ff`; applies to `HEAD` with `git apply --cached --check --whitespace=error` against a temporary index. |
| Calliope Javadoc and `jdeps` | Not rerun: production sources and dependencies are unchanged. |
| ArchUnit/Paradeigma selector | **NOT EXECUTED — DEFERRED** under DR-5; not a pass. |

**Implementation Guide conformance** · The focused test and all 315 Calliope tests pass, and solution-wide verification passes. The test checks exact qualified/versioned round-trips and helper failure category/field; test Javadoc and Draft §1.6 explain the responsibility boundary. No production dependency, security, persistence or Naming behavior changed. The Topic samples are synthetic model-level paths; this test does not claim their illustrative organisation/standard entries are externally registered vocabulary.

**Current candidate and status** · [Candidate-004][patch-004], SHA-256 [sidecar][patch-004-sha], is submitted for Solution Architect review. HCRL 011 remains pending. No commit, push, merge, software release allocation or software acceptance was performed.

**Evidence** · The [DR-11 Final-Corrections Report][report-dr12] records implementation-guide conformance and actual command results. Task-Issued-At and Report-Produced-At remain `UNRESOLVED`; no timestamp is inferred.

<a id="dr-13"></a>
## DR-13 — DR-7 final reconciliation: successors, immutability and RAW ContentSchema

**Solution Architect disposition** · The attributable instruction is preserved in the [DR-7 Final-Reconciliation Task][task-dr13]. It adjudicates the remaining DR-7 engineering decisions for this BL-0005 candidate. This disposition authorizes the bounded implementation and documentation corrections recorded below; it does not accept the software candidate, close HCRL 011, allocate a release or authorize a Git transition.

**Successor derivation** · Replace the single `deriveSuccessor()` operation with two intention-specific operations, each producing a complete immutable IU with a new UUID and a provenance relationship to its source:

1. `deriveSuccessorAsClone()` copies the source data and associated metadata, preserves every existing relationship and adds the source-provenance relationship. Persistence and cache lifecycle metadata are reinitialised for the new identity.
2. `deriveSuccessorWithMinimumMetadata()` inherits `SecurityMetadata` and `OriginMetadata`, adds only source provenance, and does not inherit other relationships. Its caller supplies the remaining representation and metadata required for a valid IU; its caller-confined builder defaults omitted coordinates to unplaced and does not allow inherited origin, security or provenance to be overridden. Persistence and cache lifecycle metadata are reinitialised.

Both operations retain the source snapshot and its authority boundaries. As bounded engineering safeguards, both reject a reused source UUID; clone derivation additionally rejects a successor UUID that is already an inherited `INTERNAL_IU` relationship target, preventing a self-reference. The caller continues to select the provenance role.

**Accepted-as-implemented decisions** · The Solution Architect confirms the following existing semantics without authorizing unrelated changes:

- Established `OriginMetadata` Topics are immutable including entry versions; re-stating the exact versioned canonical Topic is idempotent.
- `description` is revisionable. `InformationQuality.owner` is required, non-blank and immutable for the IU lifetime.
- Existing validation remains in force for UTF-8 content text, absolute references, dimensions, duration, language tags and checksum/algorithm pairing.

**RAW ContentSchema** · `contentConcept` and `contentSchema` remain mandatory. Add the separate governed path `Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.ContentSchema=RAW` for content without a distinct schema. Retain the existing RAW container schema path `Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW` without conflating the two meanings.

**Application and documentation** · `InformationUnit` now exposes `deriveSuccessorAsClone` and `deriveSuccessorWithMinimumMetadata`; `MinimumMetadataSuccessorBuilder` validates all caller-required fields without losing prior inputs on a failed build. `InformationMetadata` creates clone metadata by appending provenance while preserving existing relationships and creates minimum-successor metadata with provenance only. `InformationVocabulary.rawContentSchema()` returns the exact new Topic while `rawContainerSchema()` remains unchanged. Draft 002.007 and Backlog Item 0005 revision `000.004` record these decisions; Draft 002.006 and backlog revisions `000.001`–`000.003` remain preserved. The Calliope README and package documentation explain the distinction.

**Verification evidence** · Detailed commands, outcomes, test methods, Implementation Guide conformance, limitations and the candidate checksum are recorded in the [DR-7 Final-Reconciliation Report][report-dr13].

| Command / check | Result |
| --- | --- |
| Focused revision, quality and vocabulary tests | **PASS** · 173 tests; 0 failures, 0 errors, 0 skipped. |
| `timeout 150s mvn -B -ntp -pl calliope -am test` | **PASS** · 318 tests; 0 failures, 0 errors, 0 skipped: 91 Naming and 227 InformationUnit tests, including the final lifecycle-reset assertions. |
| `timeout 300s mvn -B -ntp verify` | **PASS** · all 50 reactor modules succeeded after the lifecycle-reset assertions; 318 Calliope tests passed. Non-fatal warnings for empty JARs. |
| `timeout 180s mvn -B -ntp -pl calliope -DskipTests javadoc:javadoc` | **PASS** · Javadoc generated with no warnings. |
| `jdeps --list-deps calliope/target/classes` | **PASS** · `java.base`, `java.xml`. |
| Candidate-005 checksum and temporary-index apply/whitespace check | **PASS** · SHA-256 `4d056a5711986ac0d5d37dd71ec7cc228d9f6c651a222add7ef09573bac460b9`; applies to `HEAD` with `--check --whitespace=error`. |
| ArchUnit/Paradeigma selector | **NOT EXECUTED — DEFERRED** under DR-5; retained, not retired, and not a PASS. |

**Verification qualification** · The test suite was not executed against the pre-change implementation before production edits. The Report does not claim a captured pre-change failure; results establish post-change behavior. Task-Issued-At and Report-Produced-At remain `UNRESOLVED`.

**Candidate and status** · [Candidate-005][patch-005] with [SHA-256 sidecar][patch-005-sha] (SHA-256 `4d056a5711986ac0d5d37dd71ec7cc228d9f6c651a222add7ef09573bac460b9`) is submitted for Solution Architect review. HCRL 011 remains pending; no software acceptance, release allocation, commit, push or merge is claimed.

<a id="dr-14"></a>
## DR-14 — Candidate-005 implementation acceptance and closure

**Solution Architect disposition · ACCEPTED.** "The Solution Architect accepts candidate-005 as the BL-0005 Calliope InformationUnit development increment." The instruction is preserved verbatim in the [Closure Task][task-closure]. It also records the DR-6 disposition, preserves the DR-5 ArchUnit/Paradeigma deferral and the DR-8 Architecture-incorporation obligation, and directs closure under the Software Development Praxis without a release tag or change to the accepted Architecture baseline.

**Closure verification and Git transition** · Recorded in the [Closure Report][report-closure] and the [register record][register]. Before the commit, the working-tree candidate files reproduced exactly the tree of candidate-005 applied to `a2e68b9f871102c435dca4cc2583ea69d881dfe1`, and the full reactor verification, Calliope Javadoc and `jdeps` passed again. The accepted implementation was committed separately from these closure records. No push, merge, tag, software release or Architecture release was performed.

[task]: ../../evidence/software-0.1.0/011/before/ai-agent-tasks/UNRESOLVED-bl-0005-calliope-informationunit-implementation-task.md
[task-resume]: ../../evidence/software-0.1.0/011/before/ai-agent-tasks/UNRESOLVED-bl-0005-calliope-informationunit-resumption-task.md
[task-corrections]: ../../evidence/software-0.1.0/011/before/ai-agent-tasks/UNRESOLVED-bl-0005-implementation-review-corrections-task.md
[task-final]: ../../evidence/software-0.1.0/011/before/ai-agent-tasks/UNRESOLVED-bl-0005-final-corrections-task.md
[task-clarify]: ../../evidence/software-0.1.0/011/before/ai-agent-tasks/UNRESOLVED-bl-0005-profile-schema-helper-clarification-task.md
[task-dr12]: ../../evidence/software-0.1.0/011/before/ai-agent-tasks/UNRESOLVED-bl-0005-dr-11-final-corrections-task.md
[task-dr13]: ../../evidence/software-0.1.0/011/before/ai-agent-tasks/UNRESOLVED-bl-0005-dr-7-final-reconciliation-task.md
[task-closure]: ../../evidence/software-0.1.0/011/before/ai-agent-tasks/UNRESOLVED-bl-0005-hcrl-011-acceptance-closure-task.md
[report-closure]: ../../evidence/software-0.1.0/011/after/ai-agent-reports/UNRESOLVED-bl-0005-hcrl-011-acceptance-closure-report.md
[register]: ../../register/software-0.1.0/hcrl-software-0.1.0-011.complete.md
[report-stop]: ../../evidence/software-0.1.0/011/after/ai-agent-reports/UNRESOLVED-bl-0005-calliope-informationunit-implementation-report.md
[report-prior]: ../../evidence/software-0.1.0/011/after/ai-agent-reports/UNRESOLVED-bl-0005-calliope-informationunit-completion-report.md
[report]: ../../evidence/software-0.1.0/011/after/ai-agent-reports/UNRESOLVED-bl-0005-implementation-review-corrections-report.md
[report-final]: ../../evidence/software-0.1.0/011/after/ai-agent-reports/UNRESOLVED-bl-0005-final-corrections-report.md
[report-clarify]: ../../evidence/software-0.1.0/011/after/ai-agent-reports/UNRESOLVED-bl-0005-profile-schema-helper-clarification-report.md
[report-dr12]: ../../evidence/software-0.1.0/011/after/ai-agent-reports/UNRESOLVED-bl-0005-dr-11-final-corrections-report.md
[report-dr13]: ../../evidence/software-0.1.0/011/after/ai-agent-reports/UNRESOLVED-bl-0005-dr-7-final-reconciliation-report.md
[patch]: ../../evidence/software-0.1.0/011/after/bl-0005-calliope-informationunit-candidate-003.patch
[patch-sha]: ../../evidence/software-0.1.0/011/after/bl-0005-calliope-informationunit-candidate-003.patch.sha256
[patch-004]: ../../evidence/software-0.1.0/011/after/bl-0005-calliope-informationunit-candidate-004.patch
[patch-004-sha]: ../../evidence/software-0.1.0/011/after/bl-0005-calliope-informationunit-candidate-004.patch.sha256
[patch-005]: ../../evidence/software-0.1.0/011/after/bl-0005-calliope-informationunit-candidate-005.patch
[patch-005-sha]: ../../evidence/software-0.1.0/011/after/bl-0005-calliope-informationunit-candidate-005.patch.sha256
[backlog]: ../../../../docs/backlog/BacklogItem-0005-Calliope-InformationUnit.v000.004.md
[draft]: ../../offline-design/DRP-Information-Unit-Framework.Draft.002.007.md
[g02]: ../../offline-design/DRP-Calliope-Identity-and-Classification-Framework.Final.001.000.md
[axioms]: ../../../governance/architectural-axioms.md
[hcrl-1.1.25]: ../../register/1.1.25/hcrl-1.1.25-001.complete.md
[hcrl-1.1.28]: ../1.1.28/hcrl-1.1.28-001.pending.md
[hcrl-1.1.27]: ../1.1.27/hcrl-1.1.27-002.pending.md
[hcrl-010]: ../../register/software-0.1.0/hcrl-software-0.1.0-010.complete.md
[hcrl-009]: ../../register/software-0.1.0/hcrl-software-0.1.0-009.complete.md
[software-praxis]: ../../../governance/software-development-praxis.md
