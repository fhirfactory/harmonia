<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# HCRL carry-forward reconciliation — software-0.1.0/004

[HCRL 001][previous] is the only preceding software-target review record in the worktree or local Git history. Neither an accepted software predecessor nor a corresponding `software/v0.1.0` tag was found. The original candidate remains `2fc15febe2a91c811fd48e83f3e7544dc37f8124`; its result and requested implementation acceptance are retained at their original scope. The new [HCRL 004][current] carries the nine original entries by ID and adds the separately authorised draft-design review. No attributable disposition closed any of DR-1–DR-9.

| Preceding matter | 004 treatment and intervening evidence |
| --- | --- |
| [DR-1](../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md#dr-1-whole-of-calliope-scope-and-candidate) · Whole-Calliope scope and candidate | **PENDING REVIEW.** Preserve acceptance of whole-Calliope execution scope for that particular assignment and the original acceptance/scope of `BL-CALLIOPE-OO-01` revision 1. The assignment-specific disposition's event time remains unestablished. The design work supplies no software-candidate disposition. [001 Task][task001] |
| [DR-2](../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md#dr-2-design-note-hierarchy-and-cohesive-package-organization) · Package hierarchy and cohesion | **PENDING REVIEW.** The second Calliope review raises service-interface placement and cohesion of access packages. The design's R5 dependency allocation leaves this conformance question open. [Second review][pass2] |
| [DR-3](../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md#dr-3-information-model-value-sets-and-construction) · Information model, value sets and construction | **PENDING REVIEW.** Both Calliope reviews raise model and translation questions, including preservation of security criteria, lifecycle state, canonical classifications, representation and version meaning. F-01.78 clarifies the draft designation and identity boundaries. The candidate's existing IU representation remains for review. [First review][pass1] · [Second review][pass2] |
| [DR-4](../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md#dr-4-consumer-migration-and-architectural-conformance) · Consumer migration and conformance | **PENDING REVIEW.** Original Java migration and unchanged Topic JSON apply to the exact 001 candidate. Later review findings on selector validation, security criteria and routing scope remain evidence for adjudication. Migration of the new F-01 design is a separate obligation. [First review][pass1] · [Second review][pass2] · [004 gaps][gaps] |
| [DR-5](../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md#dr-5-500-line-source-file-limit-and-documentation) · Source size and implementation standards | **PENDING REVIEW.** Keep the 104-file inventory and 473-line maximum. Later review comments on immutability, state ownership and cohesion inform the qualitative review. [First review][pass1] · [001 report][report001] |
| [DR-6](../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md#dr-6-software-010-candidate-verification-and-review-request) · Candidate verification and acceptance request | **PENDING REVIEW.** Preserve the 251 component tests, 90 architecture tests and 50-module reactor result and their limits at the 001 candidate. The 004 run confirms the existing ArchUnit suite at current HEAD (90 tests). Live external dependencies and F-01 implementation were outside that run. Establish the actual accepted predecessor or explicit first-release disposition before closure. [001 report][report001] |
| [DR-7](../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md#dr-7-pragma-fhir-security-context-authority) · FHIR Task security-context authority | **PENDING REVIEW.** The first Calliope review reiterates the unresolved input trust boundary. The F-01 draft leaves authorization with Themis. [First review][pass1] |
| [DR-8](../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md#dr-8-persistence-delete-operation-semantics) · Persistence DELETE | **PENDING REVIEW.** The first Calliope review reiterates the retention/disposition question. The operation and its intended meaning still require explicit Solution Architect treatment. [First review][pass1] |
| [DR-9](../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md#dr-9-legacy-information-unit-json-representation) · Legacy IU JSON | **PENDING REVIEW.** The historic wrapper compatibility decision remains open. The second review raises related schema evolution concerns. F-01.78 establishes the draft IU Topic designation role and supplies no wrapper migration evidence. [Second review][pass2] · [001 report][report001] |

## Evidence boundaries and checks

- **002 and 003** are earlier documentation and adjudication evidence. The repository iteration checker returns **FAIL** for each because its corresponding HCRL prerequisite is absent. Evidence remains at its original location. No intermediate HCRL is asserted or reconstructed. [002 decision delta][delta002] · [003 execution report][report003]
- **001** also returns **FAIL** with the current iteration checker. It reports missing conventional Task-Issued-At or Report-Produced-At fields for both Tasks and both Reports in the predecessor's formal record directories. This is a compliance result on the retained source. It does not determine attribution or resolve event times. Preserve the original submission, amendment and scope records and obtain explicit Solution Architect treatment of the outstanding provenance before closure. [001 Task][task001] · [001 report][report001]
- **004** is the expressly authorised preparation target. [DR-10][design] requests Solution Architect review of Draft.001.003. [DR-11][minutiae] records the supporting provenance qualification. HCRL 001's historical pending state and evidence remain intact.

[previous]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md
[current]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[design]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[minutiae]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-11
[task001]: ../../001/before/ai-agent-tasks/20261117T092256Z-calliope-model-engineering-task.md
[report001]: ../../001/after/ai-agent-reports/UNRESOLVED-calliope-model-engineering-report.md
[pass1]: ../../001/after/codex-1st-pass-review-of-Calliope-code.md
[pass2]: ../../001/after/codex-2nd-pass-review-of-Calliope-code.md
[delta002]: ../../002/after/F-01-Decision-Delta.md
[report003]: ../../003/after/F-01-Execution-Report.md
[gaps]: F-01-Implementation-Gap-Analysis.md
