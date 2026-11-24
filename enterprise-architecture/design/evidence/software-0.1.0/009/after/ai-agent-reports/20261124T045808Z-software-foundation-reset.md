Report-Produced-At: 2026-11-24T04:58:08.932432Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

# Software Foundation Reset — implementation report

**Task** [Issued reset assignment](../../before/ai-agent-tasks/unresolved-software-foundation-reset.md)  
**Work specification** [Backlog Item 0004, revision 001](../../../../../../../docs/backlog/BacklogItem-0004-Software-Foundation-Reset.001.md)  
**Review** [Software 0.1.0 / 009](../../../../../review/software-0.1.0/hcrl-software-0.1.0-009.pending.md)

The originating source is clean branch `feature/BL-CALLIOPE-OO-01` at
`718fc278be5b89b6138d4341feddefce1269eaa6` (tree
`7ddeade27675b9423a4e3b1bac89573350880de4`). GitHub's branch comparison returned
identical and zero commits ahead or behind. Architecture 1.1.26 is accepted.
No accepted software predecessor was established.

## Candidate and conformance

- **AC-1 — PASS.** All 1,326 approved checkpoint paths were deleted. The observed
  NUL-delimited, sorted path list has SHA-256
  `f5ec3eaa1b897357a33f03bd137ab2e3079660b1b03ca7dd2dd490017079e963`.
  All 12,837 retained tracked paths remain. Of those, 12,783 are unchanged and
  54 are the authorised edits (50 POMs and four review addenda). Preflight
  required the exact HEAD, clean index and tree, remote correspondence, checksum,
  regular files and valid proposed POMs before removal.
- **AC-2 — PASS.** Preserved the module hierarchy, artifact names and packaging.
  All 50 project versions and 49 parent references use `0.1.0-SNAPSHOT`.
  Removed the exploratory dependencies, BOM imports, runtime packaging,
  staging, frontend and container execution. Java 21, UTF-8 and the generic
  compiler, Surefire, JAR and WAR configuration remain in the root.
- **AC-3 — PASS.** Both bounded Maven invocations used Apache Maven 3.9.12
  with Ubuntu OpenJDK 21.0.13.1 at `/usr/lib/jvm/java-21-openjdk-amd64` and
  cached dependencies. `mvn -B -ntp -o clean verify` passed all 50 projects
  (Maven elapsed 4.027 s). `mvn -B -ntp -o -pl calliope -am verify` passed
  the parent and Calliope (1.418 s). Each invocation had a 150 s command
  deadline. Surefire ran zero tests. ZIP integrity and payload checks passed
  for 33 JARs and four WARs. Each contains the Maven manifest, embedded POM
  and properties, with no classes, libraries, assets or executable entry point.
- **AC-4 — PASS.** No Maven module retains an application source or frontend
  tree. No replacement class, compatibility shim or test was added. The full
  clean refreshed Maven targets. The 12 selected old npm/Node outputs and
  Shade-generated POMs were removed after checking evidence references.
  The filesystem check also found an empty pre-existing Clinical package
  hierarchy, which was pruned before the final build runs.
- **AC-5 — prepared for review.** Recorded [historical deployment observations](../historical-deployment-observations.md)
  and added attributable supersession notices to 001, 004, 006 and 008. Their
  historical review text and decisions remain retrievable. The accepted
  architecture and design contracts are unchanged. The [ArchUnit amendment](../../../../../review/1.1.27/hcrl-1.1.27-002.pending.md)
  is prepared at the next proposed Architecture patch target.

[Structural and archive evidence](../structural-verification.json) includes
path counts, exact archive file inventories and SHA-256 values. To reconstruct
the uncommitted software candidate, use the starting commit, the
[approved removal list](../../before/approved-delete-paths.txt), the
[gzipped patch of the 54 retained-path edits](../retained-path-edits.patch.gz)
and the new Backlog, review and evidence files in this worktree. Reverse patch
validation and `git diff --check` passed.

## Exceptions and limits

- The assignment and preceding adjudication authorise removal of the exploratory
  ArchUnit source and provide structural and reactor verification for the reset.
  The retired suite was not executed on the reset tree. Unit tests, application
  regression, runtime integration and deployment were outside the resulting
  skeleton's executed coverage. The Maven result establishes build and packaging
  health. No replacement architectural tests were introduced.
- Canonical validation initially could not start because the default Python
  environment lacked `jsonschema`. With cached EA dependencies from the prior
  F-01 review, the validator executed and returned **FAIL** on the pre-existing
  retained-input locator
  `enterprise-architecture/design/evidence/1.1.25/001/before/accepted-input/DRP-Topic-and-TopicMask.Final.001.000.md`.
  The checkpoint already lacks that exact path; the adjacent
  `DRP-Topic-and-TopicMask.Final.001.md` is present. No corrective architecture
  mutation was authorised here.
- The attributable instruction-entry timestamp is **UNRESOLVED**. The official
  Task emitter retained the unresolved field and filename prefix.
- The worktree contains unstaged deletions and edits and untracked review
  deliverables. The index and HEAD still correspond to the pushed checkpoint.
  The reset did not allocate a software release, tag or publish anything.
