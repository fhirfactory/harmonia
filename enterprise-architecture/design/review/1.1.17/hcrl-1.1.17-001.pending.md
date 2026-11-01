<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Faithful incorporation of the accepted Iteration 001 architectural increment | ADD authorised by operative Task; incorporated-result confirmation pending | Source acceptance and resulting-state acceptance remain distinct. |
| [DR-2](#dr-2) | Reconciliation, identity preservation and historical contribution standing | Reconciliation authorised by operative Task; resulting mapping confirmation pending | Four identities reused/extended; other normative levels and independently scoped assertions remain distinct. |
| [DR-3](#dr-3) | Validation, retained uncertainties and human review checkpoints | Results and ArchiMate publication accepted; Iteration 001 reader-facing checkpoint remains open | The 2026-11-01 disposition defers only Iteration 002 reader-facing navigation/XML-folder correspondence review. |
| [DR-4](#dr-4) | Bounded supporting Minutiae | Not recorded | Minutiae-Accepted remains awaiting Solution Architect entry. |

# HCRL — 1.1.16 → 1.1.17: NFR-derived architectural elements, Iteration 001

**Pending resulting-state review.** The workspace user's [operative ADD Task](../../evidence/1.1.17/001/before/ai-agent-tasks/20261031T104506Z-nfr-derived-elements-add.md) expressly authorises incorporating the completed, Solution Architect-reviewed specification. It states: “This is an **authorised architectural ADD operation**, not a requirements-discovery, architectural reassessment or specification-development activity.” That issued direction supplies application authority; no prior HCRL disposition or final result confirmation is fabricated.

The [completed execution Report](../../evidence/1.1.17/001/after/ai-agent-reports/20261031T113057Z-nfr-derived-elements-add-execution.md) records actual incorporation, verification, limits and resulting Git state. Report-Produced-At is `2026-10-31T11:30:57.102390Z`, captured by the existing governed report-emission helper independently of Task issuance and validation.

The exact [source specification](../../offline-design/Harmoni-NFR-Derived%20Architetural%20Elements%20-%20Iteration%20001.md) is the unique repository-held counterpart of the Task's differently spelled locator. The [input record](../../evidence/1.1.17/001/before/baseline.json) pins its original Git edition/checksum and the clean baseline commit `d5fdddb31e9bff39ae6c08b2e0f2c71d309ab5f5`. Immediate accepted predecessor: **1.1.16**, confirmed in the [completed predecessor HCRL](../../register/1.1.16/hcrl-1.1.15-001.complete.md). Prepared result: **1.1.17**, iteration **001**, with one expected-current patch increment. No Git staging, commit, branch mutation or push has been performed.

[Architecture Governance](../../../governance/architecture-governance.md), [Review Praxis](../../../governance/architecture-review-praxis.md), [Change Provenance](../../../governance/architecture-change-provenance.md), [Continuum Representation](../../../governance/continuum-representation.md) and [Incorporation Praxis](../../../governance/continuum-incorporation-praxis.md) govern the activity. The reviewed source is incorporated faithfully; this HCRL supplies no independent architectural redesign.

<a id="dr-1"></a>
## DR-1 — Faithful incorporation of the accepted Iteration 001 architectural increment

**Authorised meaning and actual application.** All **150 active statements** are accounted for: 38 Principle «ArchitecturalAxiom», 16 native Principles, 34 Constraint «Mandate», 35 CourseOfAction «ImplementationGuideline», 13 Requirement «AssuranceRequirement», seven Requirement «NonFunctionalRequirement», five Requirement «FunctionalRequirement» and two native Goals. The source identifier prefix never determines type. Native bases and specialisations, descriptions, applicability and release qualifiers are preserved.

The executable profile/schema incorporates all nine source-defined specialisations: eight newly registered, plus the existing ImplementationGuideline. Axiom, InformationRequirement and TransitionRequirement have no active instance in this register; none is invented. DerivedRequirement remains provenance, not a specialisation. No Specialization edge between Axiom and ArchitecturalAxiom is asserted. Existing AX-01–AX-18 definitions retain their authority and unchanged bytes.

All **13 subject groups and 69 subgroups** are represented as native Grouping. They supply subject classification across normative types and native layers. **219 Compositions** express exactly 69 group-to-subgroup containments and 150 primary allocations. **Three Associations** represent NFR-063's explicit joint workload with NFR-066, NFR-068 and NFR-069. No ownership, component, Capability, BusinessFunction, logical Home, Realization or dependency is inferred. Generic Grouping supplies no naming ancestry. Native-layer statement navigation remains canonical once per element; the Cross-Cutting References / Architectural Subjects hierarchy links each subgroup to its allocated statements.

Result: **228 new canonical elements** (146 statements and 82 groups), **222 new relationships**, **1,842 elements** and **1,882 relationships** overall. The six reserved identifiers NFR-116/120/128/134/140/144 remain excluded from active source bindings. All existing relationship assertions and all **1,216 authored Views** are preserved. The source's full original bytes are retained unchanged within explicit supersession boundaries with canonical replacement references. Frozen predecessor publications remain unchanged.

The [complete reconciliation](../../evidence/1.1.17/001/after/reconciliation.md), [machine-readable mapping](../../evidence/1.1.17/001/after/reconciliation.json), [relationships](../../evidence/1.1.17/001/after/relationships.json) and [purpose-selected verification](../../evidence/1.1.17/001/after/increment-verification.json) support review without duplicating a model export in this HCRL. The [review index](../../evidence/1.1.17/001/after/review-index.md) identifies the governed representations and publications.

**Axiom consistency.** AX-03/AX-04 are preserved by native base semantics and existing governed machinery. AX-08/AX-09 are preserved by purpose-selected evidence rather than retention of every processing interaction. AX-14 is preserved by distinct normative levels, native relationships, subject classification, historical standing and canonical versus representation identity. AX-15/AX-17 are preserved through explicit source uncertainties and pending result acceptance. AX-05, AX-07, AX-10, AX-13 and AX-18 remain intact: this architectural ADD changes no runtime authority boundary, security enforcement, persistence ownership, failure semantics or external management boundary. No production source is changed. No unresolved substantive architectural conflict was identified during this bounded reconciliation.

<a id="dr-2"></a>
## DR-2 — Reconciliation, identity preservation and historical contribution standing

The [reconciliation notes](../../evidence/1.1.17/001/after/reconciliation-notes.md) explain same-instance correspondence and material non-identity distinctions. Four native Requirement identities are reused and extended with the accepted AssuranceRequirement classification:

| Source | Preserved canonicalId | Existing identity |
| --- | --- | --- |
| NFR-019 | `3e45e60affb44bf384d54de26d3add6a` | PROV-001 — End-to-End Provenance |
| NFR-057 | `b5d3447a05b249a8af154136fc36c4d9` | OPS-004 — Operational Observability |
| NFR-058 | `f2e504e7d6374e41a81303265adda6de` | CORE-004 — Correlation and Causation |
| NFR-059 | `4c02fec3af264b878ee3e0cfaecbf55f` | OPS-003 — Meaningful Health and Readiness |

Each preserves its Exchange identifier, native Requirement base, formal/friendly names, original bindings, identity history and existing relationships. Display names receive the governed specialisation prefix; accepted source names remain explicit aliases in body/metadata. Storage relocates to the specialisation directory. Only accepted Iteration 001 contributions acquire current standing. Original historical assertions retain their recorded standing and text; broader historical detail receives no implicit approval.

The other 1,610 baseline elements are byte-identical. Related but independently scoped assertions remain distinct. In particular, G02's cross-subject Information Management grouping does not replace the existing functional RequirementGroup; the accepted incomplete Information Durability During Platform Failure placeholder remains unchanged. Reconciliation introduces no implicit satisfaction/realisation links.

<a id="dr-3"></a>
## DR-3 — Validation, retained uncertainties and human review checkpoints

**Subsequent Solution Architect disposition — 2026-11-01.** Mark Hunter explicitly confirms acceptance of the ArchiMate publication and the results of both Iterations 001 and 002 in the [retained follow-up disposition](../../evidence/1.1.17/002/before/20261101T002003Z-solution-architect-reader-review-disposition.md), entered at `2026-11-01T00:20:03.781Z`. He acknowledges that reader-facing navigation has not been separately visually reviewed and authorises a deferral limited to the reader-facing navigation and XML-folder correspondence changes introduced by Iteration 002. No content change or removal of validation requirements is authorised. Earlier pending-result statements below retain the original review sequence; this paragraph records the subsequent result acceptance without asserting an additional visual inspection.

The Iteration 001 reader-facing review of the NFR subject hierarchy/allocations, native-layer separation and qualified reused contributions remains required by Review Praxis §5 and is outside that expressly limited Iteration 002 deferral. Common Governance permits explicitly recorded deferrals and bounded departures, but supplies no authority to enlarge their scope. Actual review or an explicit disposition covering this remaining Iteration 001 obligation is required before release closure. This preserves the scope of the Solution Architect's decision; it does not reopen the accepted architectural content or resolve existing naming/source uncertainties. Original execution/validation evidence is unchanged.

**Automated verification: PASS.** Canonical validation covers 1,842 elements/identities, 1,882 native relationships and 462 unchanged qualified predicates. Offline XSD, native endpoints, references, containment and information naming pass. All 84 tooling tests pass; all 90 Java architecture tests pass under JDK 21. Publication staleness/link checks and the 1,842-element browser smoke test pass. Isolated maintained-input reproduction passes with byte-identical derived artifacts and zero live identity allocation. The independent source check verifies every accepted description/classification/domain, all existing identities/relationships/Views, exact reused historical content, untouched predecessor publication, and preservation of the original specification bytes.

The [validation summary](../../evidence/1.1.17/001/after/validation-summary.json), [independence proof](../../evidence/1.1.17/001/after/reproducibility.json) and [increment verification](../../evidence/1.1.17/001/after/increment-verification.json) retain scope and limits. The final XML SHA-256 is `a35583c227e8cac0bfb856708c97f600be406fe2acdff53361b326677e012e0a`. The source-only LaTeX RAD and catalogue are regenerated; PDF compilation is not asserted. The initial stale navigation-parent expectation and temporary historical-example rebase were corrected and verified before submission; no accepted historical publication was changed.

The source leaves response-time thresholds (NFR-081), internal API traversal scope (NFR-092), and unspecified search-performance, object-size and availability targets (NFR-065/087/105). They are preserved explicitly in canonical unresolved properties; none is supplied by the agent. Deployment-specific resilience objectives remain deployment-specific. These are retained source boundaries, not newly discovered requirements.

Existing formal naming vocabulary remains unestablished for native Grouping and ImplementationGuideline. **117 new canonicalName values** remain explicitly unresolved (82 Groupings and 35 guidelines) under the existing Q-01 boundary. Determinate local names and all determinate Motivation formal names are populated. No new qualifier or subject-derived ancestry is invented.

**Temporal provenance:** Task-Issued-At is `2026-10-31T10:45:06.035Z`, recovered from the attributable current-interaction user-message entry. The complete operative wording matches after removing only its enclosing Markdown code fence. The [retained event evidence](../../evidence/1.1.17/001/before/task-event-provenance.json) records the source, event meaning and correspondence without making the local interaction log a continuing dependency. The Task filename and all current authority references are mechanically synchronised to that same event. The completed Report's own emission event is recorded independently; no capture, filesystem, Git or later clock time replaces either event.

**Required human checkpoints.** Under Review Praxis §5, import the exact prepared 1.1.17 Open Exchange publication into supported Archi 5.10 and structurally review its actual tree and native specialisation properties before HCRL acceptance. Retain representative evidence for that publication/native model. Review the generated reader-facing navigation, especially the subject hierarchy/allocations, native-layer separation and qualified reused historical contributions. Automated schema, comparison and same-file controls supply no actual Archi import/export or visual-inspection claim. Final Solution Architect confirmation of the incorporated result, retained uncertainty treatment and this semantic record remains pending.

<a id="dr-4"></a>
## DR-4 — Bounded supporting Minutiae

The bounded group comprises the formal Task/Report and baseline correspondence; selected validation evidence and source/accounting checks; type-catalogue/schema/template representation support; focused incompatible-type and projection/containment tests plus intentional population/navigation expectations; generated Manifest, Exchange, browser, index and LaTeX RAD publications; current publication links; and explicit source supersession preserving original bytes. These support DR-1–DR-3 and confer no architectural acceptance themselves. Transient application scripts and working logs remain working material rather than permanent evidence. No unrelated runtime change, historical migration, additional requirement or alternative governance process is included.

Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect comment]
