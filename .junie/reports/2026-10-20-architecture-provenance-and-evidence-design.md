<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture provenance and evidence design — 2026-10-20

**Design for Solution Architect review only.** This report proposes a contract, reference amendments and migration sequence. It installs no governance and authorises no migration, packaging, deletion, relocation, renaming or disposal. The complete proposed contract appears in section 1; `docs/markdown/governance/architecture-change-provenance.md` has deliberately **not** been created. Existing architecture, evidence, processing material and `.gitignore` remain unchanged.

The design follows the Solution Architect's instruction: **architectural evidence records the authorised incremental change between governed architecture states, not the execution history of the machinery that produced or validated the change.** A transition has exactly `before.tar.gz`, `after.tar.gz` and `change-review.md`. This report does not reopen that decision or propose a fourth evidence file.

The recovery boundary is **RAD-1.1.0**, root predecessor **1.0.10**, at commit **`8f3244b1af1481885b578f3749b35ebf0ffdc6a6`**. At task entry, HEAD and the local `origin/Harmonia-GovernedAccess` reference agreed; no fetch or independent server check was performed. The published 1.1.0 XML SHA-256 is **`4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1`**. No release increment is made for this unaccepted design/report. Later acceptance must apply the release policy then in force; this task supplies no new exception to it.

The supplied [repository-structure audit](2026-10-20-archimate-repository-structure-audit.md) is an investigation aid, not architectural authority. Its counts and historical inventory are attributed below. Authority was loaded independently from [root instructions](../../AGENTS.md), [documentation instructions](../../docs/AGENTS.md), [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md), both Praxes, the release policy, the Change Review contract and applicable governed source/promotion records. Static code, tests, manifests, Git history and selected retained reviews were inspected to corroborate the dependency mapping. No architecture generation, machinery-validation run, historical replay, Python test suite or Archi invocation was performed. The repository-required Maven ArchUnit check passed; its result is recorded in section 11.

Materially relevant axioms and consistency:

| Authority | Application to this proposal |
| --- | --- |
| AX-04 — Harmonia owns semantics; engines supply machinery | RADS defines provenance. Git, tar and architecture tools implement it and cannot redefine acceptance. |
| AX-08 — Evidence records meaning, not machinery | The durable record proves the authorised architectural delta and its decision, without making logs or machinery execution permanent architectural evidence. |
| AX-09 — Transient operational state is ephemeral by default | Processing remains recoverable while needed and can become disposable after explicit completion and dependency checks. Participation in processing gives no permanent evidentiary standing. |
| AX-14 — Semantic distinctions are preserved | Governed state, candidate, submission, acceptance, release, publication, processing attempt and historical replay remain distinct. |
| AX-15 — Uncertainty is preserved | Missing before bytes, incomplete authorisation, interrupted changes and absent historical proof are reported rather than replaced with regenerated facts. |
| AX-17 — Architectural authority and explicit uncertainty | This text stays candidate. Conflicting existing retention rules require express adjudication; historical runs and PASS results cannot supply that authority. |

These axioms support the proposed model without introducing a runtime Kleio service, a module/deployment boundary or a new architectural identity allocator. AX-08's runtime evidence responsibilities do not make Kleio the repository storage mechanism.

## 1. Candidate Architecture Change Provenance contract

**Proposed installation path:** `docs/markdown/governance/architecture-change-provenance.md`. The fenced block below is the complete proposed file, including its candidate status. Installation, coordinated amendments and promotion to governed standing are separate authorised work. Its MUST/SHALL language describes the proposed normative contract and has no new governing effect through this report.

````markdown
<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Architecture Change Provenance

## Status, authority and scope

**CANDIDATE — proposed for Solution Architect approval; not yet governed authority.**
Acceptance of this document and its coordinated governance amendments must be
explicitly recorded before its normative requirements take effect. Until then,
existing governed contracts apply and conflicts must be raised under AX-17.

Once approved, this document is the single maintained definition of Harmonia
architecture-change provenance. Agent instructions, Praxes, templates and tools
must reference it rather than establish competing provenance rules. It is
subordinate to the [Architectural Axioms](architectural-axioms.md) and applies
AX-04, AX-08, AX-09, AX-14, AX-15 and AX-17. The
[architecture release policy](architecture-release-policy.md) continues to
govern release allocation; provenance machinery never allocates a release.

The contract applies to authorised architecture work through Markdown/RADS,
ArchiMate and future representations and tools, whether performed by the
Solution Architect, Codex, Junie or another authorised contributor. It does
not confer acceptance on a representation, processing run or generated result.
It defines repository architecture-change evidence, not all runtime provenance,
diagnostic retention or software test reporting.

## Governing rule

Architectural evidence SHALL record the authorised incremental change between
two governed architecture states. It SHALL NOT record the execution history of
the machinery used to produce or validate that change.

For a governed transition `RAD-x → RAD-y`, durable architectural evidence
consists of exactly:

```text
before.tar.gz
after.tar.gz
change-review.md
```

Processing artefacts, intermediate candidates, machinery snapshots, test outputs,
logs, temporary states and copies of governance/framework source SHALL NOT become
architectural evidence merely because they participated in producing a change.

## Definitions

| Term | Meaning |
| --- | --- |
| Governed state | An explicitly identifiable, authorised state of Harmonia architecture held in the repository, with its applicable release, source boundaries, identities, decisions and standing. A state may preserve explicitly unresolved or candidate assertions without accepting them. A commit, version label, publication or PASS result alone does not establish architectural acceptance. |
| Architectural change | An authorised difference in governed architectural content or its governed representation between two governed states, including approved architectural governance, identity, semantic, documentation or presentation changes. A processing correction or machinery run is not inherently an architectural change. |
| Changed content | The bounded repository content whose state actually differs as part of the authorised architectural transition. It includes approved additions, modifications, removals and path changes and the necessary governed representation changes. It excludes unchanged dependencies and incidental processing output. |
| Before state | The originating governed state, restricted in the before archive to the original versions of the changed content that existed there. It is not an incoming proposal, an immediately preceding run or an arbitrary pre-correction workspace. |
| After state | The resulting governed state, restricted in the after archive to the resulting versions of that same changed content that exist there. It is not an unaccepted candidate or an alternative regenerated result. |
| Architectural evidence | The exact three-file record of a governed transition: the changed content before and after, and its reviewed change/authorisation record. |
| Processing artefact | Material used to discover, prepare, execute, recover, test or publish a change without itself being accepted changed architectural content. Its operational importance and possible temporary retention do not confer architectural-evidence standing. |
| Authorisation record | The attributable Solution Architect decision recorded in change-review.md, binding the exact proposed change and directives to their originating and resulting governed states, scope, conditions and exclusions. Generated dispositions or successful tests are not authorisation. |
| Three-file evidence package | The logical evidence unit for one governed transition, containing only before.tar.gz, after.tar.gz and change-review.md. This term prescribes no final repository directory layout. |

## Identifying the transition and changed content

Before applying a change, the authorised contributor SHALL establish the exact
originating governed state, applicable authority, intended scope and original
content. Before finalising evidence, the contributor SHALL establish the actual
resulting governed state and account for every difference in the approved scope.

The changed-content inventory SHALL be recorded in change-review.md. Each entry
SHALL identify its repository-relative path or corresponding paths, operation,
before/after existence, available byte hashes and relevant Change Request or
directive. Renames and release-representation succession SHALL record both paths
and their correspondence; architectural identity continuity SHALL remain separate
from file-path continuity.

Existing repository files are the normal byte-preservation unit. A changed
Markdown, JSON or XML file SHALL be retained in its original complete form on
each applicable side; the review identifies the architectural records or sections
that changed. Preserving a changed model file does not authorise capturing its
directory, dependency tree or entire repository. Any alternative content unit
requires explicit definition and approval before capture; it must preserve exact
content and unambiguous repository-path correspondence.

Supporting source, schemas, code, tests or governance SHALL NOT be copied into
the archives merely because a processor read them. A governance source belongs
in the archives only where that source itself is changed by the authorised
architectural transition. A machinery-only code change uses Git version history;
its participation in an architectural change set does not make it architectural
content. An ambiguous classification SHALL be raised rather than inferred.

Release-specific publication paths may differ. Where a changed logical
architecture representation has an original version at one release path and its
result at another, the archives SHALL retain those respective versions at their
actual repository-relative paths and the review SHALL map their succession.
The predecessor publication may remain an immutable governed release in the
repository; its inclusion as original changed architectural content does not
claim that its stored file was modified or deleted. This is not a filesystem
rename or two unrelated additions. Unchanged baseline/dependency files SHALL
NOT be included merely because they help interpret the transition. The inventory
must distinguish logical architectural change from repository storage operations.

## Archive contents and path preservation

before.tar.gz SHALL contain the original versions of the content actually
changed by the transition. after.tar.gz SHALL contain the resulting versions
of that same changed content. Both SHALL preserve the repository-relative paths
of the respective state, without an enclosing workspace/run prefix or rewritten
basename. Files are retained as actual content, not individually recompressed
snapshot copies with artificial .gz suffixes.

For a modification at one path, that path appears in both archives. For an
addition, no before member exists; for a removal, no after member exists.
change-review.md SHALL state ABSENT explicitly for the applicable side. An empty
file SHALL NOT stand for absence. A pure-addition or pure-removal transition may
therefore have one valid empty tar archive; both required archive files still
exist. A rename maps the old and new paths without fabricating a file in either
state. Release-representation succession likewise maps the original and resulting
versions at their different paths without claiming removal of the predecessor.
The logical changed-content set is the same on both sides even where the member
sets differ for these reasons.

Archive interpretation SHALL be deterministic. Member names SHALL be unique,
normalised repository-relative paths. Absolute paths, parent traversal and
members escaping that boundary SHALL be rejected. Any required links or special
file types need an explicitly approved preservation rule; tools SHALL NOT
silently dereference external content. Verification SHALL check member lists,
declared absences, content hashes and correspondence against the review.

The archives SHALL contain no permanent auxiliary manifest, log, plan, test-result,
decision or machinery files unless the particular file is itself changed governed
architectural source. It is forbidden to place processing material inside an
archive to evade the three-file limit. Required interpretation metadata belongs
in change-review.md; existing governed metadata already intrinsic to an archived
source file remains part of that exact file.

## change-review.md and authorisation

The reviewed change record SHALL describe the architecture change, its
dispositions/directives and Solution Architect authorisation. It SHALL contain:

- originating and resulting governed-state/release identities, immutable
  repository locators where available and the applicable authority;
- the bounded architectural purpose and differences, stable CR/directive
  identities where applicable, dependencies, exclusions and unresolved matters;
- the reviewed dispositions, instructions, reasons and material decision
  revisions, with attributable authorisation actor/date and conditions;
- the exact changed-content inventory, path/rename correspondence, existence
  states and before/after content hashes;
- the archive byte hashes and the checks needed to interpret and verify the
  transition, including relevant validation conclusions, limitations and distinct
  human acceptance, publication and inspection outcomes;
- for reconstructed historical evidence, the reconstruction date, provenance
  sources, original authorisation facts and explicit limits of what was recovered.

Only the Solution Architect supplies architectural acceptance or changes an
issued disposition. Processing status, Outcome and Outcome Reason remain distinct
from Disposition. A review may record held or rejected proposals without accepting
them. It SHALL preserve the decision facts needed to understand the authorised
transition without requiring a processing workspace to interpret them.

Material decision history may be quoted or incorporated into this one reviewed
record. It SHALL NOT require permanent companion decision files or a recursive
collection of older reviews. No review self-hash is required; the review is kept
outside the archives and its final version is bound by repository history and
Solution Architect approval. Archive hashes can therefore be recorded without a
circular hash dependency.

Authorisation binds the proposed content and permitted deterministic derivations,
not an unrestricted instruction to generate architecture. Where final archive
hashes or completion facts are recorded after the application authorisation,
they SHALL be distinguishable from the original decision. Final closure must
confirm that they describe its authorised result. A changed result or material
new scope requires renewed Solution Architect review. Tool success supplies no
missing approval and SHALL NOT overwrite the reviewed decision.

## Git and architectural evidence

Git is the normal version-history mechanism for maintained governance,
executable machinery, tests and configuration. Extra source snapshots require a
specific architectural/evidentiary reason; participation in a run is insufficient.

Git records file history. The three-file package records which incremental
architectural change was authorised and the actual changed content before and
after. Neither substitutes for the other's role. A Git commit can include
unrelated machinery and processing files; copying its entire diff or tree is not
an architectural-evidence selection rule. A file that was never committed at its
source path is not assumed recoverable there. Where needed changed architectural
bytes survive only in an older snapshot, they SHALL be recovered and verified
before that carrier becomes disposable.

Evidence and governed release identities SHALL remain durably retrievable under
the approved repository/recovery arrangements. References and hashes identify
content but do not replace missing content. The package is an incremental
record, not a full backup or a promise to rebuild an entire state without its
governed baseline.

## No recursive evidence capture

Evidence SHALL NOT recursively capture previous evidence. Archives SHALL NOT
contain previous packages, before/after archive trees, processing histories or
copies of their own evidence directory. Reference earlier governed states or
transitions by identity and locator in the review; do not embed their packages.

Content selection SHALL start from the authorised changed-content inventory,
not a recursive workspace walk. Genuine historical architecture text intrinsic
to a changed governed source remains byte-preserved in that source; this does
not justify copying the separate evidence files to which it refers.

## Transition verification versus historical machinery reproduction

Proving a transition means establishing its authorised scope, original and
resulting changed content, identity continuity, exclusions and actual governed
outcome. It does not require preserving every command, temporary candidate,
test result, tool version or environment that produced the outcome.

Reproducing a historical machinery execution is a separate diagnostic or
engineering activity. It may use Git-pinned code and purpose-specific regression
fixtures in an isolated workspace. It SHALL NOT define architectural acceptance,
enlarge the three-file package or become a dependency of current operation.
Any separately required diagnostic retention must have an explicit purpose and
must not be represented as a fourth architecture-evidence component.

Current authoring, validation and publication SHALL operate from maintained
governed source, maintained framework/configuration and declared fixtures or
governed releases as applicable. They SHALL NOT depend on historical processing
workspaces, run-local executables, hidden local logs or frozen framework copies.
Reusable mechanics SHALL be maintained as framework code before the processing
carrier is discarded. Fixtures retain testing purpose and no architectural
authority merely through their use by tests.

## Lifecycle, closure and migration

```text
governed state
      ↓
processing workspace
      ↓
change-review.md
      ↓
Solution Architect authorisation
      ↓
updated governed state
      ↓
before.tar.gz
after.tar.gz
change-review.md
      ↓
processing workspace disposable
```

Original before bytes SHALL be secured before mutation even though final package
closure follows the updated state. While work is open, processing material may
be needed for recovery, pending decisions or validation. Completion requires
verified evidence, an authorised result, preserved unique architectural content,
resolved current dependencies and applicable retention/disposal authority.
Disposable means eligible for an explicitly authorised disposal operation; it is
not an instruction to delete automatically.

A failed, diagnostic, superseded or no-change run with no governed architectural
transition has no architectural evidence package merely by having existed.
Uncertain effects must first be resolved or held explicitly under AX-15/AX-17.
An issued held/rejected decision needed for future architecture work must remain
available in the applicable reviewed record or maintained governed unresolved
matter; processing material SHALL NOT become a hidden architecture authority.

Historical conversion SHALL identify actual governed state boundaries and
authorisation before selecting content. It SHALL NOT relabel run-to-run changes
as governed transitions, invent retrospective authorisation, reconstruct missing
originals by regeneration, or rewrite frozen historical records. A retrospective
review may consolidate established decisions only with explicit Solution
Architect approval and transparent reconstruction provenance.

Existing retention/preservation obligations remain in force until explicitly
reconciled when this contract is adopted. No file is disposable solely because
of its filename, extension, age, duplication, compression or generated status.
Unknown architectural value or a live dependency blocks disposal of that content.
Physical layout, migration selection and disposal execution require separately
authorised work; this contract creates no blanket cleanup authority.
````

## 2. Definitions

The candidate's definitions are the proposed shared vocabulary. These interpretations matter in migration:

| Distinction | Application to this repository |
| --- | --- |
| Governed state versus processing state | RAD-1.0.10 and RAD-1.1.0 are the transition endpoints. Corrected Run-002 is an intermediate proposed model subsequently used by reconciliation, not the before governed state. |
| Architectural content versus implementation history | Accepted canonical IDs, native types, semantic relationships, authored Views and accepted governance decisions carry architecture. The Python implementation, test execution sequence and copies of tools ordinarily carry implementation history. |
| Maintained source versus source copy | RADS `baseline/architecture.json` is maintained governed source. Its byte-identical identity-establishment copy has historical purpose. Identical bytes do not make the maintained source redundant. |
| Release versus transition evidence | Exact published XMLs identify release representations/authoring baselines. The three-file package explains the authorised incremental change. Neither is a processing log archive. |
| Submitted content versus accepted content | Immutable exports may contain rejected, held or proposed material. Their presence in processing does not make every contained assertion governed. Preserve required accepted content and decision facts; classify the remaining submission by purpose. |
| Architectural uncertainty versus unknown execution effect | CR-155–158 remain explicit architectural questions. An interrupted writer or unavailable historical log creates a different uncertainty. Neither becomes PASS by inference. |
| Historical reconstruction versus new approval | A new consolidated review records when reconstruction occurred and preserves original decision facts. Approval of that consolidation does not pretend that the new review existed in 2026's original processing checkpoint. |

## 3. Three-file evidence contract

The package has **exactly three files** and is selected by an approved architectural delta. No `manifest.json`, `decision.json`, `plan.md`, verification script, test report, log directory or machinery bundle is proposed beside them. Nor may these be smuggled into tar members as support material. Existing changed governed architectural source remains eligible on its own merits, regardless of extension.

| File | Contents | Exclusions |
| --- | --- | --- |
| `before.tar.gz` | Exact original files actually affected by the authorised transition, with repository-relative paths. | Incoming proposals substituted for baseline content; whole-repository or run snapshots; unchanged dependencies; older evidence. |
| `after.tar.gz` | Exact resulting files for that same logical changed-content set, at their actual resulting paths. | Intermediate/rejected candidates, regenerated substitutes for actual accepted bytes, tool/test/log history. |
| `change-review.md` | Reviewed architectural delta, dispositions/directives, attributable authorisation, path/existence inventory, byte hashes, decision facts and material validation/inspection conclusions. | An automatic PASS promoted to acceptance; a link-only index requiring disposable processing to explain authorisation. |

**Worked interpretation, not an executed package:** if an approved transition modifies `docs/markdown/.../a.md`, adds `docs/markdown/.../b.json` and removes `docs/markdown/.../c.md`, the before archive contains original `a.md` and `c.md`; the after archive contains resulting `a.md` and `b.json`. The review records `b.json: ABSENT → hash`, `c.md: hash → ABSENT`, and both hashes for `a.md`. Paths are examples, not proposed repository changes. Empty files are never absence markers.

For a full XML file changed in place, complete original/resulting XML belongs on the applicable sides; semantic IDs and CRs in the review explain the narrower architectural delta. For the same model's architectural transition across versioned publication paths, put its original version at the old release path in `before.tar.gz` and its resulting version at the new release path in `after.tar.gz`. The review maps their logical succession and records that the predecessor publication remains intact in the repository. Do not forge a same-path modification, claim a filesystem rename/deletion or reduce a changed model to a new-file addition with no original architectural version. A reader uses the named governed baseline to interpret unchanged context. The precise release/source inventory must be approved before historical packaging; the whole baseline is not archive payload merely because it is the baseline.

Validation conclusions belong in the review as architectural facts and limitations. Full historical command sequences, environment captures and failed attempts do not need permanent preservation in this package. Their old manifests must remain unchanged during migration; replacement verification must expressly verify the new contract rather than pretend to satisfy the old execution-history contract with fewer files.

## 4. Agent/Praxis discoverability requirements

The proposed document is representation-neutral RADS governance. The entry points link to it; code, schemas and README procedures enforce/explain it. There must be no independent Codex or Junie definition of evidence.

| Entry point | Proposed amendment after approval | Reason |
| --- | --- | --- |
| [AGENTS.md](../../AGENTS.md), section 1 | Add a direct architecture-change provenance entry applying to **all** architectural representations and agents. | A future Markdown-only task must discover the rule without triggering ArchiMate processing. |
| AGENTS.md, “ArchiMate round-trip task entry” | Retain the engineering Praxis link and add the provenance link for both triggers. | Immediate discovery without searching historical runs. |
| AGENTS.md, section 6 | Add an explicit direct reference for Junie planning/reporting and architecture execution. | This is the actual Junie-facing maintained repository instruction. Plans/reports remain execution artefacts. |
| [Architecture Authoring Praxis](../../docs/markdown/governance/architecture-authoring-praxis.md), “Purpose and authority”, submission and completion passages | Cite the single provenance contract; distinguish working retention from final evidence. | Its current submission wording directs permanent retention through the old 2A run structure. |
| [ArchiMate Round-Trip Engineering Praxis](../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md), “Purpose and authority”, “Stop, recovery and evidence”, “Runs and RAD releases” | Cite the contract and qualify run snapshots as operational preservation pending closure. | Preserve recovery and human checkpoints without defining permanent evidence through run layout. |
| [Change Review contract](../../docs/markdown/governance/archimate-ingestion-change-review.md), “Processing record and audit evidence” and “Run placement and template” | Refer to the contract for durable evidence and state which detailed records are workspace recovery material. | Present requirements retain commands, unsuccessful attempts and complete processing evidence; a hyperlink alone cannot resolve that conflict. |
| [Change Review template](../../docs/markdown/governance/templates/archimate-ingestion-change-review-template.md) | Add state bindings, changed-content/existence table, archive hashes, authorisation/closure and reconstruction fields; make durable conclusions intelligible without run links. | One reviewed record must replace permanent decision/manifest sidecars. Preserve independent Disposition/Outcome semantics. |
| [Release policy](../../docs/markdown/governance/architecture-release-policy.md), machinery-validation retention passages | Explicitly adjudicate transition from “keep runs intact”/frozen machinery retention to the approved provenance model and scoped migration authority. | Current normative text still requires the old preservation model. The bounded RAD-1.0.10 release exception must not become a general exemption. |
| [Publication semantics](../../docs/markdown/governance/archimate-ingestion-publication-semantics.md) | Reference provenance wherever “evidence” or run preservation could imply execution-history retention; preserve semantic/View and input-integrity requirements. | Accepted presentation and identity must survive, without permanent processing-tree dependence. |
| [Governance index](../../docs/markdown/governance/README.md) | List the approved provenance document by purpose and standing. | Tool-independent RADS discovery. |
| [docs/AGENTS.md](../../docs/AGENTS.md) | Add a navigation link to the same contract, with no copied policy. | Scoped documentation contributors inherit the root rule. |
| [ArchiMate workspace](../../docs/archimate/README.md), [framework README](../../docs/archimate/framework/README.md), [processing README](../../docs/archimate/processing/README.md), ingestion and exchange indexes | Link the rule; label workspace retention, release storage, fixtures and historical examples by role. | Operational guidance must not present old preservation procedures as the evidence definition. Legacy preservation/wrapping applies. |

Proposed root instruction text, **not installed**:

```markdown
### Architecture-change provenance

For any architectural review, change, reconciliation, publication or evidence
migration, Codex, Junie and other agents MUST load
[Architecture Change Provenance](docs/markdown/governance/architecture-change-provenance.md)
and establish its approved standing. That document defines provenance across
representations; Praxes and machinery implement it. Processing runs, plans,
reports and source snapshots do not independently establish architectural evidence
or acceptance. Existing preservation obligations remain until explicitly reconciled.
```

Proposed direct Junie instruction for root section 6, **not installed**:

```markdown
For architecture-related planning or execution, Junie MUST apply
[Architecture Change Provenance](docs/markdown/governance/architecture-change-provenance.md).
Plans and completion reports are processing material unless particular content
has been explicitly reconciled into governed architecture or its reviewed change
record. They are not an additional component of the architectural evidence package.
```

Proposed common Praxis reference, **not installed** (the local link is relative to the intended Praxis files):

```markdown
[Architecture Change Provenance](architecture-change-provenance.md) defines
durable transition evidence for this lifecycle. Working input pins, recovery
snapshots and detailed processing results serve the active workspace; they do
not enlarge that contract. Final closure preserves the authorised architectural
delta and decision and removes current dependencies on processing history before
that workspace becomes disposable.
```

No separate `.junie/guidelines.md`, `.junie/AGENTS.md`, `.codex` instruction file or `.agents` instruction file exists in the inspected checkout. `.junie` contains plans and reports; root section 6 is the direct Junie instruction. **It needs the direct link above.** Creating a new Junie policy copy is unnecessary. If a future launcher adds a repository instruction file, it must link the same contract. The unrelated OpenSpec instruction file is not an architecture-processing entry point merely because it is an `AGENTS.md`; amend it only if its scope is later connected to this lifecycle.

**Adoption conflict to resolve:** existing release-policy and 2A retention statements are governed RADS, not disposable legacy prose. Their amendment needs Solution Architect acceptance in the same adoption change set. Outside RADS, reconciled legacy sections must preserve their original text inside the section-scoped supersession wrapper required by AGENTS.md section 1.2. Frozen historical snapshots are not edited to agree with the new policy. No reference changes are performed by this report.

## 5. Current repository mapping

All paths below are repository-relative. For compactness, **F** means `docs/archimate/framework`, **P** means `docs/archimate/processing/publishing`, **I** means `docs/archimate/processing/ingestion`, **M** means `docs/archimate/processing/machinery-validation/information-architecture`, and **B** means `docs/markdown/governance/baseline`. These abbreviations identify existing populations, not a proposed target tree.

The audit inventories **1,829 tracked ArchiMate files / 536.09 MiB**: framework 89 / 1.56 MiB; exchange 14 / 225.48 MiB; governance-tooling 39 / 20.30 MiB; processing 1,686 / 288.73 MiB. Run-002 accounts for 882 files; six `pre-*` archives account for 718. These are audit measurements, not new deletion candidates or promised savings. Future migration must classify content and consumers rather than target file-count reductions. Removing checkout files also does not erase their blobs from Git history; this proposal includes no Git-history rewrite.

| Population | Proposed role under the contract | Treatment required before any future disposal |
| --- | --- | --- |
| Root AGENTS, `docs/AGENTS.md`, current RADS contracts, metamodels, architecture definitions and accepted decisions | **Maintained source/governance** | Retain maintained originals and authority hierarchy. Amend only in separately accepted governance work. |
| B `architecture.json`, `motivation.json`, `business-homes.json`, `home-derived-metadata.json`, `information-architecture.json`, both `accepted-ingestion-*.json` | **Maintained governed architectural source** | Retain persisted identity and authored record content. They include frozen provenance bindings and amendments, so future source independence needs approved reconciliation, not blind flattening or reallocation. |
| B `correspondence.json`, documentation classification, presentation/containment selections, governed identity records | **Maintained governance/configuration**, with historical bindings embedded | Keep meaningful source and standing. Replace operational reliance on old workspaces through an approved successor source/validation contract. Do not erase old pins or treat them as wildcard exemptions. |
| B derived Markdown catalogues and `discrepancies.json` | **Governed projections/publication content** | Distinguish generators from accepted outputs; changed governed representations can belong to transition archives. Generated status is no disposal test. |
| B `release-1.1.0.json` | **Governed release-promotion decision** | Preserve the original release scope and historical pins; it is not a second current release allocator. Its processing-path dependencies must cease to be necessary for current rendering. |
| `exchange/publish/1.0.4`, `1.0.6`, `1.0.7`, `1.0.8`, `1.0.9`, `1.0.10`, `1.1.0` | **Governed release representations**, subject to their recorded scope/standing | Preserve exact publication identities and bytes. Seven XMLs are authoring/recovery baselines, not machinery runs. The publication index is maintained navigation. No 1.0.5 XML in this area is inferred. |
| `exchange/ingest/1.0.8`, `1.0.10`, native Archi models and backup | **Submission/working material**, potentially carrying unique architecture and useful fixtures | Review against accepted captures/releases and held proposals. Preserve unique accepted Views/semantics and decision-relevant facts before classifying the residual. Native format or `.bak` is not a disposable classification. |
| F tools, tests, schemas/provenance/licences, requirements, Makefile and current projection rules | **Maintained framework** | Use Git normally. Maintain reusable mechanics here; do not archive recursive framework copies as architectural evidence. Some current adapters remain release-specific and need an explicit historical purpose. |
| F `baseline/`, locks and declared fixture files | **Maintained test/recovery inputs**, not automatically current architectural source | Identify actual consumers and the minimum licensed, provenance-labelled specimens needed. A test fixture may deliberately contain old/candidate architecture without promoting it. |
| M shared `specimen.json` and Run-001 | **Processing/regression population; extraction required** | The specimen pins baseline, submission and actual no-edit control. Run-001 outputs support Run-002 generation/tests. No independent run-to-run architecture package is established. |
| M Run-002, including machinery and correction archives | **Processing population; extraction required** | Remove live publisher/test/governance dependencies first. Preserve unique accepted architectural content in governed source/evidence, and reusable checks in maintained framework. Intermediate correction snapshots are not automatically architectural before states. |
| I baseline 1.0.8, both runs, reviews and executions | **Mixed architectural decision/source carriers and processing history** | The two reviewed scopes contributed to RAD-1.0.9. Consolidate decision facts and exact accepted content; detection attempts and execution logs remain processing. |
| I baseline 1.0.10 Run-002 | **Mixed reviewed authorisation/source carrier and processing history; extraction required** | Preserve CR-001–154 and CR-155–158 standing, allocated IDs and accepted View content. Review revision 3 alone explicitly authorises no release; incorporate the separate promotion decision into the eventual transition record. |
| P release-specific reports, `before` states, controls, verifiers, manifests, validation and logs | **Mixed release content, transition-source carriers and processing history; extraction required** | Separate exact architecture versions and human decisions from machinery chronology. Preserve selected real controls as fixtures only where needed. Do not designate an entire P release directory as durable evidence. |
| `governance-tooling/1.0.14` and `migrations/2026-10-19-reclassify-1.0.14` | **Non-RAD processing/migration history; framework and decision extraction required** | Its manifest expressly says `rad_architecture_release: null`. Preserve the architectural classification decision in governed context/review; retain reusable namespace/path tests independently. Do not manufacture RAD-1.0.14 evidence. |
| `.junie/plans`, `.junie/reports` architecture-related material | **Execution/navigation material**, with possible unique architecture/decision/fixture carriers | Reconcile unique architectural meaning into RADS or the reviewed transition record before disposal is considered. Early XML controls are current fixture inputs. The unrelated runtime/security population is outside this bounded migration. |
| Historical governance/framework snapshots in `machinery/`, `pre-*`, execution `before/`, RADS `source-history/`, governance-tooling consumer snapshots | **Copies and historical versions; purpose-dependent holds** | Classify by actual content and role. Exact architectural before bytes may be recoverable only here; implementation variants may be Git history via committed compressed carriers. Do not carry full snapshot trees into new evidence. |
| `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft` and referenced legacy TeX | **Legacy recovery source, unique historical architecture and old machinery** | Current `recover_v02`/synchronisation still reads the pinned corpus. Incorporation into RADS requires reconciliation under AGENTS section 1.2; no wholesale copying, automatic adoption or present cleanup. |
| Ignored/untracked manifest-referenced logs/backup; interpreter caches | **Locally present processing/recovery material**, with distinct dependency and retention facts | Audit referenced paths individually. Caches have no inspected proof role; referenced logs/backup do. Preserve required facts or resolve claims before removing dependence. No ignore-rule change. |

“Durable architectural evidence” is the destination **only for the authorised changed-content versions and reviewed decision record selected from these mixed populations**. No existing large directory is declared to already be a conforming three-file package. Classification as processing identifies purpose; it does not itself authorise disposal.

## 6. Historical transition mapping

### 6.1 Release boundaries, rather than numeric directory order

Git history confirms root release JSON at commits `8afd7f21ef37` (1.0.1), `a3bc5aa7f38b` (1.0.2), `053f17a7f3cc` (1.0.3), `60175899e6ee` (1.0.4), `227895b66bc2` (1.0.5), `d060702d6055` (1.0.6), `107cf351f0a7` (1.0.7), `1ca0e659e5dc` (1.0.8), `f40fad55a47f` (1.0.9), `6628a9238cd0` (1.0.10), and the recovery commit (1.1.0). These are **navigation/recovery anchors**, not proof that every file in each commit was accepted architectural content or that every displayed version denotes an independent semantic change.

Intervening root values 1.0.10/1.0.13 in older governance/tooling work belong to the history explicitly reclassified by the release policy. In particular, the accepted 1.0.9 predecessor is **1.0.8**, not the immediate Git parent's displayed tooling number. The initial IA release then legitimately uses **1.0.10**. Full release provenance and accepted scope must disambiguate those uses. A literal directory number or `git diff HEAD^` cannot establish the architectural transition inventory.

### 6.2 Candidate transition reconstruction map

The table identifies recoverable carriers and gaps. It does not claim that consolidated historical reviews already exist, that all older human gates were completed, or that the inventories are packaging-ready. Each eventual package needs an approved exact path/content list.

| Transition/population | Actual architectural before content | Actual architectural after content | Reviewed record that represents or could become `change-review.md` | Processing excluded from the delta / unresolved prerequisite |
| --- | --- | --- | --- | --- |
| Initial 1.0.0 → 1.0.1; later 1.0.1 → 1.0.2 → 1.0.3 | Original changed governance/architecture files recovered through the cited release commits and predecessors, where accepted scope can be established. | Accepted changed source/governance/representation files at each corresponding boundary. | P `1.0.1/documentation-publication-report.md` and contemporaneous task reports are reconstruction inputs, **not assumed reviewed authority**. No single reviewed transition record was established here for 1.0.2/1.0.3. | Recover original approvals and inventories before deciding packages. Absence of P directories is not absence of a transition. No new authorisation may be inferred from a release bump. |
| 1.0.3 → 1.0.4 navigation publication | Applicable F baseline/semantic/navigation locks and changed source from the 1.0.3 boundary; preserve actual old paths. | `exchange/publish/1.0.4/Harmonia-Capability-Architecture.xml` and changed governed mapping/navigation material. | P `1.0.4/navigation-release-report.md` plus original reviewed decisions; a consolidated retrospective review needs Solution Architect approval. | `validation.json`, generation diagnostics and replay machinery are processing/test inputs. Locks and source correspondence may be unique and need role review before carrier disposal. |
| 1.0.4 → 1.0.5, bounded recovery “1.0.5A” | Exact 1.0.4 model and original changed architecture/source; pinned v0.2 material is historical input, not automatically the preceding governed state. | P `1.0.5/Harmonia-Architecture-1.0.5A.xml`, only to its authorised recovery standing, plus accepted changed governance/source. “1.0.5A” is a task label. | P `1.0.5/1.0.5A-recovery-report.md` and original authorisation are reconstruction inputs. Its “ready for human inspection” statement is not final acceptance. | The 75-file `source-manifest.json`, capture/TeX, crosswalk and old code require source/fixture separation. Establish which recovery result was governed; do not retroactively accept unresolved snapshot architecture. |
| 1.0.5 → 1.0.6 synchronisation | Exact approved recovery/current-RADS source boundary documented in B `correspondence.json`, plus original changed files. | B architecture/motivation captures and projections; `exchange/publish/1.0.6/Harmonia-Architecture-Baseline.xml`. | P `1.0.6/initial-synchronisation-report.md` plus attributable synchronisation decision and subsequent acceptance facts; consolidate/review. | Starting commit `227895b...` and frozen recovery inputs help reconstruction. Inspection audits and code/logs are processing. Unproven legacy/current correspondences remain unproven. |
| 1.0.6 → 1.0.7 canonical identity establishment | P `1.0.7/identity-establishment-1.0.7/prior-release/` and exact 1.0.6 governed files/publication. | Identity-bearing B captures, accepted identity/naming governance, derived catalogues and `exchange/publish/1.0.7/Harmonia-Architecture-1.0.7.xml`. | P `1.0.7/canonical-identity-establishment.md` and `.junie/reports/2026-10-18-archimate-canonical-identity-establishment.md`, with original authorisation/inspection facts, can support one consolidated reviewed record. | Preserve allocated identity values and source/representation distinctions. Export probes and real Archi control are processing or declared fixtures, not additional package files. |
| 1.0.7 → 1.0.8 workspace/tooling release | Approved original source/governance paths and exact 1.0.7 release representation. | Approved resulting source/governance paths and exact 1.0.8 representation. | P `1.0.8/publication-1.0.8.md`, `.junie/reports/2026-10-18-archimate-workspace-restructure.md` and original workspace authorisation; consolidate/review. | Report establishes no semantic/identity/relationship/presentation delta; model byte delta is one release value. Preserve that distinction. Relocation ledgers, all-file audits and reproduction scripts are not the architecture delta. A governed release/representation change may still require transition evidence. |
| **1.0.8 → 1.0.9** accepted Business/Home/presentation change set | Exact 1.0.8 governed source/publication; I `1.0.8/run-001/reconciliation/execution-001/before.json`, preserved source/review versions, and P `1.0.9/*completion/before*` help recover applicable original bytes. Later in-candidate snapshots are not all the transition before state. | B `accepted-ingestion-1.0.8.json`, business/Home amendments, changed governance/projections and final `exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml`. | **Both** I `1.0.8/run-001/change-review.md` and `run-002/change-review.md`, their issued decisions and authorised completion decisions. One consolidated review must retain both run-qualified CR namespaces, not overwrite identically numbered CRs. | 26 + 6 accepted CRs form one recorded accepted set; held interpretation cases and later completion scope remain distinguishable. P Home/View/containment completion runs are not independent release transitions merely because they have before snapshots. Final inspection standing must be established from attributable records, not generated “pending” prose or assumptions. |
| **1.0.9 → 1.0.10** initial IA and naming completion | Exact final 1.0.9 governed source/model; P `1.0.10/information-population/allocation/prior-release.json` and `canonical-name-completion/before/` supply selected originals. | B `information-architecture.json` and relevant governed naming/mapping changes; `exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml`. | `.junie/reports/2026-10-19-initial-information-architecture-population.md`, `2026-10-19-information-architecture-first-pass-change-review.md`, P population/naming-completion records and explicit Solution Architect task/naming decisions; a combined reviewed record still needs approval. | Preserve the 54 initial resource IDs, source assertions and approved BO/TOPIC naming. Canonical-name completion belonged to the same unsigned candidate under the release-policy decision; do not invent another release/package for it. |
| **1.0.10 → 1.1.0** accepted IA evolution/promotion | The exact governed 1.0.10 source state and published model, not Run-001 or provisional Run-002. Parent commit `6628a9238cd0` holds the same published baseline hash; selected before snapshots help recover uncommitted source/governance boundary details. | Accepted amended B source, promotion governance and exact 1.1.0 publication/changed governed representations; scoped inventory must include the complete authorised transition rather than only final publication edits. | I `1.0.10/run-002/change-review.md` revision 3 **plus** the separate governed B `release-1.1.0.json` promotion decision and material decision facts. A new reviewed consolidation is necessary; revision 3 alone expressly authorises no release. | Run-002 candidate, export, control, plan, establishment manifest and replay code are carriers/processing inputs. Unique accepted content must be secured and current consumers replaced before removal. Distinguish candidate inspection, reconciliation acceptance, promotion and final release inspection. |

### 6.3 Runs that do not independently establish transitions

| Population | Before / after / review determination | Proposed classification and blocker |
| --- | --- | --- |
| **Machinery-validation Run-001** | Naming preview starts from fixed 1.0.10 baseline/submission/control. Its candidate remains proposed; no resulting governed release exists for this run alone. No standalone architectural `before/after/review` package is established. | Processing/regression material. Run-002 requires its plain and gzip candidates, naming preview and rebindings; extract the needed fixture/check behaviour first. Governance changes made during the bounded proving cycle need their own approved architectural-scope mapping, not a fictitious Run-001 release. |
| **Machinery-validation Run-002** | Workspace/containment/naming corrections produce a candidate; that candidate is not the before state of 1.1.0. Its six correction snapshots are intermediate states. Architectural content accepted later is accounted for in 1.0.10 → 1.1.0. | Processing with architectural carriers and reusable checks. Current publisher, tests, history validation and replay depend on it. Do not package the entire run, relabel its marker to accepted or create evidence for Run-001 → Run-002. |
| **Ingestion 1.0.8 Run-001 and Run-002** | Original submitted bytes differ and decision scopes differ; both contributed to the same governed 1.0.9 transition. Neither run ID by itself proves a release boundary. | Consolidate accepted change/authorisation facts into one transition review; retain explicit source/run qualification. Detection attempts, review-generation implementation and execution chronology are processing. |
| **Ingestion 1.0.10 Run-002 reconciliation** | Reconciliation persists accepted content and 32 IDs while root release stays 1.0.10; decision explicitly excludes publication/release. Later accepted promotion establishes the 1.1.0 transition. | Preserve that decision distinction in the combined review. Do not invent a second public RAD state or treat reconciliation candidate XML as an independently published release. |
| **governance-tooling 1.0.14** | Explicitly non-RAD. Original normalisation work and namespace correction do not establish a RAD-1.0.14 architecture state. | Git retains machinery/version history. Preserve meaningful governance/classification decisions in their actual accepted transition; determine scope from approved authority rather than the tooling number. |
| Failed, sandboxed, diagnostic, superseded or unchanged verification attempts | No package if no governed architectural transition occurred. Failure alone does not prove no mutation. | Resolve unknown effects and unique content before disposal. Retain needed diagnostic facts during migration, without declaring a permanent architecture-evidence bundle. |

### 6.4 Detailed reconstruction design for RAD-1.0.10 → RAD-1.1.0

This is the strongest first migration candidate because the recovery boundary, immutable publication hashes, review and explicit promotion decision are available. It is a **design example**, not permission to package it now.

1. **Pin the real endpoints.** Before model SHA-256: `b1036f745e8ae3754951cd123b2b1a54a7eaa76c5fa83933abbd11ff2d4153bc`; after model SHA-256: `4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1`. The former is byte-identical in parent commit `6628a9238cd0` and the current retained publication. Before B `information-architecture.json` is also unchanged from that commit. This establishes useful recovery anchors, not a complete changed-file inventory.
2. **Establish the whole authorised scope.** The accepted delta is 32 new Topics, 38 BusinessObject-to-Topic evolutions/rebindings, 12 retained BusinessObject naming migrations, 59 Compositions, one Aggregation, 11 new Views and one revised View, plus approved identity/metadata/navigation and promotion consequences. All 1,394 elements, 1,137 relationships and 1,209 Views survive. There are 1,362 preserved established canonical identities and 32 allocated once during reconciliation; publication allocates zero. CR-155–158 remain On-Hold/Unresolved and unchanged. No Class/Package/ImplementationObject design is included.
3. **Identify original changed source at the governed boundary.** Compare governed source/authority at the 1.0.10 boundary with the actual accepted result. Use execution `before/manifest.json`, canonical-name-completion and machinery governance snapshots only to recover exact bytes where needed and to disambiguate which supporting governance corrections entered this transition. Do not use all files in those snapshots. For example, `B/information-architecture.md` is absent from the parent commit but has a later publication-before snapshot: that snapshot is not automatically a file that existed in the original governed state. Record addition/absence or the independently established earlier state. The final publication's three `before/` files capture only publication-stage edits and cannot alone represent the full architectural transition.
4. **Preserve accepted architectural content in stable source.** B `accepted-ingestion-1.0.10.json` already carries 82 element records, 60 relationships, 12 changed View XML records and allocated IDs. It does not by itself prove that all 1,209 preserved Views, View navigation, empty folders, model/property-definition content and unchanged model material can be reproduced without the candidate seed. Review and establish the required complete maintained architectural source or exact governed-release input; verify byte preservation of accepted authored presentation. Do not replace authored content by rerunning old generated-View logic.
5. **Construct one review retrospectively.** Current I `change-review.md` and execution `change-review-revision-003.md` share SHA-256 `478770553f18d3a2ee557f37743c14eb5a5ec500dc7e9080a96eca7916b43ffe`. Preserve the issued decision text and scope from `decision.json`/`reviewed-instructions.md` in the new review, then incorporate B `release-1.1.0.json`'s separate promotion authority, allowed residuals and exclusions. Record original dates and new consolidation date separately. Summarise significant inspection/validation facts and limitations; do not attach full machinery history. The supplied audit records the user's later final-inspection confirmation, while retained release records remain pending; migration closure must bind that completion to an attributable Solution Architect record without rewriting frozen originals or inventing an inspection date.
6. **Select exact archive members.** Include only approved actually changed architectural content, preserving full repository-relative paths. Modifications have original/resulting members; genuine additions and removals have explicit ABSENT sides. The model's logical succession maps original 1.0.10 XML in the before archive to resulting 1.1.0 XML in the after archive, without claiming removal of the stored 1.0.10 release. Keep that predecessor retrievable as a governed baseline as well. Exclude candidate/proof/plan/log/tool copies and unchanged baseline dependencies. No package is complete until the approved inventory and hashes close with the review.
7. **Verify independently and obtain closure approval.** Prove content/identity/View fidelity and directive coverage using maintained validators. Record what the new package proves and which historical execution claims it does not retain. Keep old material until both this closure and all dependency removals are verified and a bounded disposal is separately authorised.

## 7. Processing material classification

Classification uses **meaning, authority, unique content, active purpose and consumers**. A historical payload can have more than one relevant role; positive preservation of its architectural value and removal of its consumers precede any disposal decision.

| Class | Examples in the existing population | Future disposition condition |
| --- | --- | --- |
| Maintained architectural source/governance | RADS captures, accepted identity/name amendments, accepted decisions | Retain maintained form; changed versions can enter an authorised delta. Git supplies ordinary history. |
| Governed releases | Seven published exchange XMLs, identified source state/standing | Retain exact baseline/publication identity and approved recovery access. Release retention is not dictated by processing-tree size. |
| Architectural evidence to derive | Exact original/resulting changed source and reviewed authorisation facts across accepted transition carriers | Reduce to the exact three-file package only after scope/content/review approval. |
| Processing with unique architectural content | Incoming/candidate/native models with authored Views, source-era assertions, review amendments, persisted allocation/history | Keep under a migration hold until each unique fact is accounted for in maintained architecture, a reviewed transition record, an explicit governed unresolved matter or a justified fixture. No silent promotion of rejected/proposed meaning. |
| Processing with reusable functionality | Run-local replay, transport comparison, integrity checking, before-state/path resolution | Extract reviewed general mechanics into stable framework code and meaningful tests; do not copy hard-coded release counts and assumptions into a supposed general API. |
| Processing with active reference consumers | Governance transition archives, source-history snapshots, candidate outputs, ignored logs bound by manifests | Replace current consumers and disclose changed proof obligations. Existing manifests remain frozen; retaining only their hashes does not satisfy old checks. |
| Processing retained only for historical execution reproduction | Frozen tools/environment, correction attempts, all-file preservation ledgers, test traces | Git/recovery commit retains committed history. Historical execution reproduction is not a required architecture-evidence property; any continued separate retention needs an explicit purpose, without an expanded architecture package. |
| Incidental processing/cache | Bytecode, temporary diagnostics without unique content or consumers | Confirm role and closure first; future authorised disposal may remove them. Their suffix alone supplies no permission. |
| Unresolved classification | Unread or unreconciled old source, missing authorisation, ambiguous before state, ignored/untracked content with uncertain meaning | Hold the affected material and record the exact gap. Uncertainty is not a reason to keep every unrelated processing file indefinitely. |

Particular preservation cases:

- **Canonical allocation:** the 32 new 1.1.0 IDs and earlier 54-resource/identity allocations are information-significant source, not regenerable random output. Retain exact values and continuity; no replay allocation.
- **Authored presentation:** accepted geometry, style, membership, connection order and View navigation are architectural representation content under the current Praxis. A semantic graph or a list of 12 changed Views does not necessarily preserve the entire accepted presentation.
- **Historical original bytes:** the audit found 396 decompressed gzip occurrences with no current raw or reachable standalone Git-object match in its scope. That does not prove all are architectural or absent from Git: their compressed carriers are committed. Individually determine which contain actual architectural originals and which are only intermediate machinery. Use pinned recovery retrieval, not regeneration, where originals are required.
- **Human decision history:** reviews may have substantive issued instructions across revisions. The final durable review must carry the decisions/reasons needed to understand accepted, rejected and held scope. A generated summary or latest review filename cannot replace that examination.
- **Legacy explanatory architecture:** v0.2 TeX includes, reviewed BEC hierarchy/identity history and source-qualified pending assertions may carry material not present in current accepted captures. Reconcile relevant meaning; preserve candidate/history standing and AGENTS supersession rules. No migration of that whole corpus is authorised here.
- **Historical controls:** an actual no-edit Archi export records a real transport result, not a new architecture state. If current diagnostics need it, retain a deliberately declared fixture or release-associated control with its testing purpose. Do not make its original entire run a permanent evidence dependency. A fresh export cannot recreate the fact of an older execution.

## 8. Existing dependencies that must be removed

These are static, directly inspected dependencies, not test failures observed in this task. A documentary historical link and a mandatory file read have different consequences. The following read/execution paths block disposal until their current purpose is replaced or explicitly retired.

| ID | Consumer → historical dependency | What would break / required replacement |
| --- | --- | --- |
| D-01 | [publish_information_release.py](../../docs/archimate/framework/tools/publish_information_release.py), `inputs()`/`publish()` → B `release-1.1.0.json` → M Run-002 candidate/rebindings, I review/export/plan/establishment/reconciled output and plan-bound decision/instructions/control/original submission | Current publication and `test_information_release.py` replay/validate historical processing. Establish complete maintained accepted source and explicit governed-release inputs. Ordinary publication must not need the old plan/candidate or execute an old reconciliation to discover its source. Preserve the original promotion record as history, not a live source loader. |
| D-02 | [governed_metadata.py](../../docs/archimate/framework/tools/governed_metadata.py), `archive()`/`historical_bytes()`/`release()` → identity establishment `sourceHistory` and `prior-release/`; [reconcile_108.py](../../docs/archimate/framework/tools/reconcile_108.py), `read()`/`evidence_bytes()` → accepted source history and Home-completion naming snapshots | Source validation currently compares maintained captures with publication-era copies and before bytes. Carry the accepted source/identity constraints into a maintained successor validation contract, with historical comparison available explicitly rather than required for current operation. No legacy fallback or identity reallocation. |
| D-03 | [synchronise_baseline.py](../../docs/archimate/framework/tools/synchronise_baseline.py), `load_inputs()` → correspondence recovery inputs → [recover_v02.py](../../docs/archimate/framework/tools/recover_v02.py) source manifest, recovered XML/crosswalk and pinned legacy TeX/model/tools | Older/current source validation and historical tests retain a transitive recovery tree. Establish which capture/standing/history is required source, reconcile it, then make historical recovery an explicit optional operation using pinned Git/release inputs. Do not weaken original recovery pins or derive new authority from old code. |
| D-04 | [information_population.py](../../docs/archimate/framework/tools/information_population.py), `preserved_authority()` and synchronisation fallback → P `1.0.10/.../canonical-name-completion/manifest.json`, its before governance bytes, F `governance-transitions.json` → M Run-001/Run-002 governance manifests/snapshots via [governance_history.py](../../docs/archimate/framework/tools/governance_history.py) | Authority drift validation requires run-local frozen bytes. Introduce approved current authority bindings and transition verification independent of run paths. Historical chains remain verifiable against explicitly retrieved history/evidence where needed; no arbitrary live hash refresh or wildcard acceptance. |
| D-05 | [test_workspace_navigation.py](../../docs/archimate/framework/tests/test_workspace_navigation.py), frozen Run-002 setup and regression assertions → M Run-002 outputs, Run-001 outputs and `pre-containment-correction`/`pre-contained-leaf-correction` archives | Tests would lose expected candidates and old projection/semantic signatures. Extract minimal reviewed fixtures and invariant-based tests for historical/current relationship standing, leaf folders, naming/rebindings and View preservation. Keep purpose-labelled full-scale fixtures only where they provide necessary coverage. |
| D-06 | `test_information_naming.py`, `test_change_review.py`, `test_roundtrip_praxis.py` and other real-input tests → F `tests/fixtures/roundtrip/information-change-review.json` / M `specimen.json` → actual 1.0.10 baseline/submission/no-edit control; F `fixtures/roundtrip/manifest.json` → `.junie` 1.0.6/future-contract exports | Three nominal fixture files do not contain their full payload closure. Declare all actual inputs and expected standing under maintained fixture/release access, with provenance and licence. No test should discover an undeclared old run or report directory. |
| D-07 | [information_workspace_run.py](../../docs/archimate/framework/tools/information_workspace_run.py), `frozen_paths()`/`build()` → entire Run-001, old report, specimen directories, run-local replay script, governance archives; it writes compressed machinery/source snapshots | The historical adapter both depends on and propagates processing history. Isolate it as a historical adapter; new run execution must take explicit bounded inputs and use shared resolution/projectors without copying dependencies into architectural evidence. Retain guarded correction/recovery behaviour in operational workspaces. |
| D-08 | M Run-002 `validation/reproduce_candidate.py`, `verify_containment_correction.py`, `contained-leaf-correction/verify_candidate.py` → frozen `machinery/`, correction archives and four `validation/dependencies/` payloads selected by `replay-dependencies.json` | General restoration, input verification and comparison knowledge exists only in execution paths. Extract useful mechanics behind maintained interfaces; historical exact replay is separate, takes an explicit frozen context and never rewinds root release. A permanent replay bundle is not required by the new evidence contract. |
| D-09 | P `1.1.0/validation/verify-release.py` → `protected-inputs.json` (1,716 paths), publication outputs/machinery hashes, old processing, `test-results.json` and its ten raw log files | This verifies preservation of execution history, not just the architectural transition. Replace the current required check with explicit governed-source/release/evidence checks. Freeze the old verifier and its claims; do not edit its expected hashes to make the reduced population pass. Record the new proof's scope and old proof's limits. |
| D-10 | P `1.1.0/roundtrip/verify-transport.py` → published model, `Archi-no-edit-export.xml.gz`, `execution.json`, untracked `archi-cli.log`; repeated verifiers exist in P `1.0.9/roundtrip`, `home-context-completion/roundtrip`, and P `1.0.10/information-population/.../roundtrip` | Extract comparison mechanics for IDs, semantic/property/View preservation, navigation and classified residuals. Actual invocation observations belong to the current workspace; retain material verification/inspection conclusions in the review. A useful real control may become a fixture, without demanding old logs for current operation. |
| D-11 | [verify_governance_evidence.py](../../docs/archimate/framework/tools/verify_governance_evidence.py) → governance-tooling manifests, migration `protected-before.json`, I `1.0.8/.../verify-execution.py` executed via `runpy`, P `1.0.9/validation/rad109-preservation-final.json.gz`, Home before states; [workspace_paths.py](../../docs/archimate/framework/tools/workspace_paths.py) → `historical-locations.json` | Namespace/locator correctness and replay of a particular old all-file audit are coupled. Preserve namespace rules and safe explicit locator resolution as maintained code/configuration. Replace mandatory execution of historical scripts with data-driven current invariant checks. A locator map may navigate history but must not make historical processing mandatory. |
| D-12 | P `1.0.8/reproduce-1.0.7.py`, `verify-preservation.py`, `audit-workspace.py`, `compare-publications.py`, `run-command-checks.py`; P `1.0.6` inspection scripts; P `1.0.9/*completion/{verify.py,run-check.py}`; P `1.0.10` preservation/completion scripts; I execution/review validators | Reusable hash, path, XML, identity, preservation and bounded-process checks are mixed with fixed release assertions. Extract the mechanics that current verification needs; keep fixed historical expected results as fixtures or review facts only where justified. Curated I `1.0.8/.../prepare-review.py` is not a general review generator and must not become architectural adjudication code. |
| D-13 | Makefile, `verify_baseline_outputs.py`, report generators and generated report links → P output locations and old run-relative proof links | The current nine-output reproduction set contains model, reports, manifests and processing proofs. Split maintained governed release outputs from temporary validation output. Discover declared outputs by role rather than a fixed historical count. Report links required for closure must resolve to governed source/releases or the three-file record, not disposable history. Historical prose links can remain historical with retrievable context. |

The four explicit Run-002 replay dependencies are F `historical-locations.json`, F `requirements.txt`, B `architecture.json` and B `correspondence.json`. Their frozen versions solve an old import/replay problem; current use must read current maintained inputs or a declared isolated historical context, never silently substitute these snapshots for current authority.

### Ignored/untracked reference closure

A read-only check in this task corroborated the audit:

| Manifest | Referenced paths | Locally present but untracked |
| --- | ---: | ---: |
| P `1.1.0/protected-inputs.json` | 1,716 | 17 |
| P `1.1.0/release-verification-manifest.json` `files_sha256` | 98 | 19 |
| Union of untracked references | — | **36: 35 `.log` files and one `.archimate.bak`** |

All 36 are locally present and match their recorded hashes. This establishes local availability, **not** recovery from the pushed commit. The audit's wider Git/content analysis found some duplicate historical carriers but did not establish committed recovery for the complete set. The old verifiers directly read some of these paths. A clean-checkout verification cannot assume them present, and a successful local historical audit does not prove otherwise.

Proposed handling is to examine the referenced **meaning and consumer**: identify any unique architectural/authorisation fact, preserve it in the appropriate reviewed/governed content, replace current log-dependent verification, then explicitly accept retirement of the old execution-proof requirement where appropriate. Do not recreate historical logs, force-add the whole local population as architectural evidence, alter `.gitignore`, silently rewrite manifests, or include a permanent logs/manifest sidecar. The backup needs content/standing review before its carrier can be disposed of. If an unresolved fact still requires one of these files, keep that file on hold until the gap is decided.

## 9. Required stable-framework changes

These are proposed implementation work packages, not changes made in this task. They should reuse the existing secure parsers, identity/naming, containment, correlation and workspace projectors. There is no proposed replacement architecture engine or unattended prose-to-authority mechanism.

| Work package | Required maintained capability | Dependencies addressed / completion evidence |
| --- | --- | --- |
| SF-01 — source completeness | Define the accepted current architectural input set independently of processing runs, including persistent IDs, relationships, model/property definitions, all authored Views/navigation and standing. Establish any successor capture through architectural review. | D-01–03. Publication with historical processing unavailable preserves approved content and does not allocate IDs. Never silently back-derive new architectural meaning from machinery/legacy descriptions. |
| SF-02 — publication boundary | Make ordinary publication consume maintained source/configuration and explicit governed release inputs; keep acceptance/allocation separate. Send transient projection/comparison/validation outputs to a workspace. Retain current published release bytes as immutable baselines. | D-01, D-13. Reproduce the approved output contract independently; do not demand old plans, controls or execution reports to render current architecture. |
| SF-03 — governed transition capture | Shared representation-neutral inventory and tar writer/verifier implements the approved document: changed paths, original/resulting bytes, absence/rename mapping, hashes, exact three filenames, no recursive evidence or dependency capture. The review remains the human authorisation boundary. | D-07, D-09. Reject overbroad captures and extra files, detect incomplete originals, validate against approved scope. A tool cannot create missing architectural acceptance. |
| SF-04 — review closure | Extend the maintained review template and applicability checker to support state bindings, path inventory, authorisation, separate post-application facts, embedded material decision history and transparent reconstruction. Preserve CR/Disposition/Outcome distinctions. | D-01, D-09, D-12. A reviewer can understand the transition offline from its package and governed state references, without opening old workspaces. |
| SF-05 — authority verification | Validate current accepted authority bindings and explicitly approved governance changes without relying on run-local before hashes. Offer optional historical transition verification from approved package/Git locators. | D-02, D-04, D-11. Drift still fails closed; tests prove an unapproved authority edit cannot pass. Old pins and historical bytes are not rewritten. |
| SF-06 — verification/transport | Move reusable release integrity and actual Exchange comparison mechanics into maintained tools. Inputs/expected invariants are explicit; fixed release counts/hashes are data, not hidden constants in a required historical executable. Keep real transport, semantic validation, deterministic publication and human inspection separate. | D-08–10, D-12. Meaningful positive and negative tests; review records material conclusions/residuals without retaining all commands/logs. |
| SF-07 — fixtures | Register minimal purpose-specific synthetic cases and necessary real-input specimens with exact source/hash/licence and historical/candidate standing. Restore useful invariants from correction audits as maintained regressions. | D-05–06. Preserve coverage for historical relationships, Home/Composition, contained leaves, absent/misleading folders, identity rebinding and authored presentation. Neither entire runs nor unrelated snapshots become fixtures by default. |
| SF-08 — isolated historical replay | Where retained for engineering purposes, offer an explicit stable interface that retrieves approved pinned Git/release inputs into a temporary context and checks original release/authority there. Use bounded subprocesses and restore no files into current source. | D-03, D-08, D-11–12. Historical replay is optional to current operation and never weakens old baseline guards. No requirement to maintain a self-contained historical execution bundle alongside evidence. |
| SF-09 — dependency independence and discovery | Add read/dependency closure checks covering code, manifests, fixtures, subprocesses, dynamic loading and generated links. Document current commands from stable entry points. Keep genuine historical navigation separate from required current inputs. | All D rows. With historical processing inaccessible, normal authoring/intake, validation, tests and publication still work using declared maintained inputs. |

Proposed logical dependency direction:

```text
Architectural Axioms + approved provenance contract
                   ↓
agent/Praxis instructions + maintained source/accepted decisions
                   ↓
maintained framework + explicit governed releases/declared fixtures
                   ↓
bounded disposable processing workspace
                   ↓
reviewed authorised transition → exact three-file evidence

historical processing ── no required input to current operation
```

Archive assembly and verification are mechanical once scope is authorised. CR decomposition, interpretation of prose directives, classification of uncertain legacy architecture and Solution Architect acceptance remain governed human/agent work. Release-specific adapters can remain maintained for a declared purpose, but they cannot be the mandatory route through old processing to operate on current architecture.

## 10. Proposed migration sequence

**Do not execute this sequence under the present instruction.** Each phase has a concrete exit gate; retaining the recovery commit and leaving existing files in place precede all migration. No final physical repository tree is prescribed. The only proposed new normative document path and exact package filenames are those the task requested.

| Phase | Future activity | Exit gate / stop condition |
| --- | --- | --- |
| 0 — preserve and confirm | Pin RAD-1.1.0 recovery identity, publication/source hashes and initial tracked/ignored state. Check availability of local-only referenced bytes without altering them. | No mutation or cleanup. Missing/ambiguous content is recorded. Do not confuse local remote-tracking agreement with independent server verification. |
| 1 — approve governance | Solution Architect reviews this complete candidate and decisions in section 12. Install only through a separately authorised adoption change; update entry points, template and conflicting retention contracts coherently. Apply the release policy for the accepted change set. | One approved provenance authority; no competing run-defined rule; no accidental use of the expired proving-cycle release exception. Do not modify the frozen RAD-1.1.0 architecture to make new tooling work. |
| 2 — establish transition inventories | For each candidate historical transition, establish exact governed boundaries, authorised scope, original/resulting paths/hashes, decisions, exclusions and gaps. Inspect compressed carriers read-only as required. Keep this inventory in the developing review/workspace, not a fourth permanent evidence manifest. | A complete approved inventory for the bounded transition. Do not package every run or equate Git parent/version order with architectural predecessor. Missing originals or approvals block that transition only. |
| 3 — secure unique content | Reconcile accepted architectural meaning and presentation into maintained source; preserve unresolved/proposed standing separately. Consolidate material authorisation facts into the transition review. Identify licensed minimal fixture inputs. | Exact IDs and accepted semantic/View content accounted for; no required architectural fact survives solely in disposable processing. Legacy incorporation follows RADS review and section supersession; original legacy content is not rewritten. |
| 4 — extract stable mechanics | Implement SF-01–09 in bounded reviewed work packages. Generalise useful run-local checks and detach publication/source validation from historical replay. Separate optional historical adapters. | Meaningful regression tests establish preserved behaviour and fail-closed guards. No current executable is loaded from historical processing. Current output generation uses maintained source, not recreated history. |
| 5 — prove independence | From a clean isolated checkout/context, make historical processing unavailable and exercise supported current review/intake, validation, tests and publication with explicit test inputs. Keep the real working repository and ignored files intact. | All declared current capabilities work from maintained source/releases/fixtures. Read traces/static checks find no hidden history dependency. Any timeout/stall is diagnosed, not treated as failure of architecture or a reason to delete inputs. |
| 6 — reconstruct a pilot transition | Start with the bounded 1.0.10 → 1.1.0 mapping. Prepare the consolidated `change-review.md`, obtain Solution Architect approval of reconstructed facts/scope, then assemble exactly the two archives from actual original/resulting changed content. | Package contents/hashes/path correspondence close with the review; both authorisation layers and residuals are explicit. Original historical records remain unchanged. No recursion or processing extras. |
| 7 — verify pilot and recoverability | Independently inspect member lists and bytes, compare approved deltas and exclusions, verify retrieval from the approved recovery arrangements and distinguish fresh checks from retained historical assertions. | Solution Architect accepts the migration record and its limits. The package proves the architectural transition; no claim to reproduce every historical command is made. |
| 8 — convert remaining established transitions | Repeat only for boundaries with adequate scope/authority. Consolidate multi-run reviews as needed. Record no-transition runs as processing; identify unresolved histories for bounded holds rather than invented packages. | Each actual accepted transition has the three-file record or an explicit unresolved reconstruction gap. No accidental RAD-1.0.14 or Run-001 → Run-002 transition. |
| 9 — propose bounded disposal | Produce a concrete path/content disposition proposal showing unique-content closure, dependency removal, policy reconciliation and remaining holds. Solution Architect authorises the exact disposal operation separately. | Only then can specified processing content be removed under that later task. No blanket deletion by suffix, age, duplication or generated status. This report does not cross this gate. |
| 10 — verify after authorised migration | Recheck current operation, source/releases/evidence retrieval, links, invariants and absence of hidden local dependencies. Apply the normal accepted-change release policy. | Migration reports true outcomes and unresolved limitations. Recovery commit remains available; no Git-history rewrite or alteration of historical decision bytes is implied. |

The intended steady-state lifecycle is the candidate's requested sequence: governed state → processing workspace → reviewed `change-review.md` → Solution Architect authorisation → updated governed state → exact three-file evidence → workspace eligible for disposal. Secure original bytes before mutation, preserve recovery material until closure, and distinguish eligibility from authorisation to delete.

## 11. Verification/acceptance criteria

### Design-task verification actually performed

- Read the current authority independently of the supplied audit and identified AX-04/08/09/14/15/17 consistency and existing retention conflicts.
- Inspected the actual publisher input graph, governed source/decision bindings, source-history and governance-transition validation, run writer/restoration mechanisms, representative real-input regressions and release/transport verifiers.
- Confirmed root release/predecessor and Git boundary identities, 1.0.10 publication continuity from the parent commit, 1.1.0 publication hash, the matching revision-3 review hashes and all 36 locally present untracked manifest references with matching byte hashes.
- Confirmed all 12 required report sections, closed code fences, 27 existing local file-link targets outside proposed text, and no trailing whitespace. The candidate document is not installed. Git diff and diff-check show no tracked changes; root release, HEAD, published 1.1.0 hash and the supplied audit's hash are unchanged.

At final status inspection, a new untracked `../../docs/archimate/exchange/ingest/1.1.0` directory had appeared after the initial status check. It was neither created nor modified by this task and was not treated as a new authorised submission or inspected for architectural meaning. The design remains bounded to the stated RAD-1.1.0 recovery state. The only authored repository file is the requested report; the pre-existing untracked audit was preserved. The required Maven check produced ordinary build/test output, with no architecture-processing evidence run.

The dependency and contract checks above are **static design checks**, not runtime proof that proposed framework changes work. Existing historical PASS summaries are historical assertions. No Python framework suite or Archi invocation was performed.

To satisfy AGENTS.md section 4, the bounded repository ArchUnit command shown below was also executed: **PASS — BUILD SUCCESS, 90 tests, zero failures/errors/skips, Maven-reported elapsed time 17.970 seconds**, completed 2026-10-20 13:31:14 Australia/Sydney. The reactor completed all 36 selected modules. This checks existing repository architectural invariants; it does not implement or prove the proposed provenance tooling, and it creates no machinery-validation run. No implementation changes or rerun were needed.

### Acceptance criteria for future adoption and migration

| Criterion | Positive evidence required |
| --- | --- |
| A-01 — authority and discovery | Fresh Codex/Junie/other agent sessions can reach the approved rule from root instructions, both Praxes and the RADS index. A Markdown-only task reaches it without ArchiMate-specific knowledge. Root section 6 directly covers Junie. |
| A-02 — governance consistency | Existing run-retention and processing-evidence statements have approved, scoped reconciliation with the new contract. Frozen originals and legacy supersession obligations are preserved. Candidate text is not treated as governed before acceptance. |
| A-03 — endpoint and scope integrity | Each package names real governed endpoints and the attributable authorised change. Review scope/inventory matches actual differences; a failed/no-transition run cannot create a fictitious transition. |
| A-04 — exact package | Exactly `before.tar.gz`, `after.tar.gz`, `change-review.md`; no fourth file or disguised processing payload. Actual member bytes and SHA-256 values match the review. |
| A-05 — paths and absence | Repository-relative paths are preserved; additions/removals, empty-file versus ABSENT, renames and versioned-publication correspondence are deterministic. Missing originals fail closed; pure additions/removals retain a valid empty-side archive. |
| A-06 — no recursion or overcapture | Archive inventory contains only authorised changed architectural content. Previous evidence, entire workspaces, unchanged tool/governance dependencies and recursive snapshot trees are rejected. |
| A-07 — authorisation preserved | Issued CR decisions, material revisions, directives, exclusions and actor/date remain understandable from the review. Reconciliation, promotion, validation and inspection are distinct. New reconstructed review is approved without fabricated historical acceptance. |
| A-08 — unique architecture preserved | Exact canonical identities, source standing, accepted semantic records, authored Views/navigation and held matters survive. Tests prove no reallocation, no inferred classification and no unintended View regeneration. |
| A-09 — independent current operation | Clean-checkout current publication, applicable intake/review checks, source/authority validation and regression suite succeed while historical processing is unavailable. Include dynamic `runpy`/subprocess/import and manifest-driven reads; an `rg` scan alone is insufficient. |
| A-10 — declared input closure | All required maintained inputs/releases/fixtures are retrievable with verified bytes. No current check relies on ignored local logs, backups, caches or a developer's temporary workspace. Old frozen manifests retain their original limitations. |
| A-11 — proof purpose is accurate | The new verifier establishes authorised delta, content and governed outcome without asserting full historical machinery reproducibility. Historical replay, if offered, is explicit, isolated and bounded, and cannot change current root authority. |
| A-12 — disposal is bounded | Every proposed removed carrier has a demonstrated resolved purpose/dependency/unique-content disposition, while unresolved content remains held. Exact later disposal is authorised; report verification includes recovery and remaining uncertainty. |
| A-13 — release and architecture invariants | Accepted future implementation applies release governance and required repository checks, including the ArchUnit suite. Current release generation never increments authority. Frozen RAD-1.1.0 remains recoverable byte-for-byte. |

For future implementation, tests should target these behaviours: wrong baseline/hash, missing review authority, extra archive members, duplicate/escaping paths, recursive evidence, incorrect absence/rename mapping, omitted accepted content and undeclared processing reads must be detected. Additions to the suite are justified by the provenance/invariance behaviour, not by mirroring a tar writer's implementation.

Run the focused Python framework and affected root-script tests for framework changes, independent publication validation against the approved output contract, and the required repository architecture suite:

```bash
timeout --signal=TERM --kill-after=10s 600s mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
```

Use narrower evidenced timeouts for focused checks. Observe progress and investigate approximately two minutes without meaningful progress for ordinary tests, five for builds/dependency work, under AGENTS section 5.1. Do not run the current Makefile `all` casually as a read-only check: it includes generation. Appropriate isolated output and fixtures must be configured before a future verification run. PASS, FAIL, TIMEOUT, STALLED and UNRESOLVED remain distinct. No future check result is claimed by this report.

## 12. Matters requiring Solution Architect decision

The exact three-file model, the architectural-delta rule, prohibition on recursion and the absence of cleanup authority are **settled task constraints**, not questions reopened here. The following decisions are needed to turn this design into governed implementation:

| Decision | Proposed resolution for review |
| --- | --- |
| Approval and effective standing | Accept or amend the candidate contract, then explicitly authorise installation and coordinated references/retention-policy amendments. Keep this report an execution/design artefact. |
| Architectural file boundary | Adopt complete changed repository files as the default preservation unit, with CR/section detail in the review. Approve the concrete source/representation inventory and versioned-path correspondence for each historical transition before packaging. Do not infer membership from the full Git diff. |
| Old preservation obligations | Explicitly supersede the permanent-execution-history interpretation of release-policy/2A/Praxis run preservation for the authorised migration, while preserving recovery until verified closure. A hyperlink or tooling change alone is insufficient. |
| Maintained architectural input completeness | Approve how the complete accepted model/presentation and authority bindings become independent of historical processing. Existing captures plus an approved complete governed representation may be sufficient; do not assume that the 12 changed View records are the full source. Physical allocation remains downstream. |
| Historical reconstruction acceptance | Approve one consolidated review for each established transition, including the two run-qualified 1.0.8 reviews and the separate reconciliation/promotion decisions for 1.1.0. Accept transparent retrospective provenance without inventing contemporaneous reviews. |
| Incomplete older gates | Establish any still-unproven historical acceptance/inspection facts for 1.0.1–1.0.10 reconstruction. The user-confirmed final 1.1.0 inspection must acquire an attributable durable closure entry; frozen pending records remain unchanged. No missing fact is supplied by successful tooling. |
| Local-only proof and changed obligations | Decide which meaningful facts from the 36 local-only references must survive, resolve the native backup's unique content, and explicitly accept the limits/retirement of historical execution-proof obligations. No automatic log retention, fourth evidence file or ignore-rule change is proposed. |
| Historical replay and fixtures | Decide which full-scale real specimens and replay capabilities have an ongoing engineering purpose. Maintain only necessary reusable mechanics and declared fixtures. Do not make complete Run-002 execution reproducibility a prerequisite for architecture-transition evidence. |
| Migration bounds and release sequencing | Authorise separate adoption/framework/pilot work packages, using the current release policy after rechecking authority. Do not preallocate a minor release or use the expired 1.0.10 proving exception. No change to the committed 1.1.0 architecture is authorised here. |
| Disposal | After the evidence and independence gates pass, review a concrete bounded disposal proposal. No final physical tree, global retention schedule, history rewrite or cleanup operation is approved by this report. |

**Present task boundary:** the deliverable is this report, with the complete candidate contract, proposed references, population/transition mapping, dependency analysis and migration design. Work stops before installation, migration, packaging or cleanup.
