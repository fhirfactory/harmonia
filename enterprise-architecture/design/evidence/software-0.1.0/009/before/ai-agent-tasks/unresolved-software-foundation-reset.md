Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

# Execute Harmonia Software Foundation Reset

**Agent:** Codex. **Issuer:** Mark Hunter, Solution Architect, through the current Codex interaction. The attributable instruction-entry event time was unavailable. **Origin:** `feature/BL-CALLIOPE-OO-01` at `718fc278be5b89b6138d4341feddefce1269eaa6`, clean at preflight and confirmed on GitHub. **Architecture authority:** 1.1.26. **Candidate:** software-0.1.0/009, Maven `0.1.0-SNAPSHOT`. The completed Architecture 1.1.26 HCRL records that no accepted software predecessor was established.

The adjudicated cleanup specification is captured as [Backlog Item 0004, revision 001](../../../../../../../docs/backlog/BacklogItem-0004-Software-Foundation-Reset.001.md). That item records the scope issued here. The [exact deletion manifest](../approved-delete-paths.txt) is the input to application. The earlier Solution Architect adjudication explicitly allowed retirement of the exploratory ArchUnit suite and directed that later tests enter through authorised backlog items. The following instruction accepts the adjudicated proposal and supplies the verification scope for this candidate. The standing governance change is prepared for Architecture review, with a separate release boundary.

## Issued instruction

```markdown
# Assignment — Execute Harmonia Software Foundation Reset

**Repository:** `fhirfactory/harmonia`  
**Architecture baseline:** `1.1.26`  
**Software development target:** `0.1.0-SNAPSHOT`

## 1. Authorisation

The adjudicated Software Foundation Cleanup Proposal is accepted, subject to the following qualifications.

You are authorised to execute the organised cleanup of the existing exploratory software implementation.

The objective is to establish a clean Maven software foundation for incremental implementation against Harmonia's accepted architecture.

This is not acceptance of Software Release 0.1.0.

## 2. Git checkpoint verification

Before making changes:

1. Verify the current Git branch.
2. Verify that HEAD remains at `718fc278be5b89b6138d4341feddefce1269eaa6`.
3. Verify that the working tree is clean.
4. Confirm that the checkpoint remains available on the remote.
5. Verify the deletion manifest against its recorded checksum.

If these conditions cannot be established, stop and report the discrepancy.

## 3. Execute the approved deletion manifest

Remove the 1,326 tracked implementation files identified in the adjudicated manifest.

Preserve all 12,837 retained tracked paths, except for expressly authorised Maven and governance modifications.

The deletion includes:

- Exploratory Java production implementations.
- Exploratory Java tests.
- Implementation-specific resources.
- Existing Iris frontend implementations.
- Obsolete application deployment configurations.
- Obsolete compatibility adapters and associated implementation artifacts.

Do not create replacement implementation classes.

Do not retain obsolete consumers.

Do not introduce compatibility shims.

Preserve the entire accepted architectural, design and historical evidence record.

## 4. Maven restructuring

Retain the complete Maven module hierarchy and all 50 POMs.

Apply the approved Maven restructuring to support an essentially empty reactor.

Remove dependencies and plugin configurations associated solely with the discarded implementation.

Preserve architecturally justified module relationships in their authoritative documentation.

Retain appropriate generic Maven build configuration.

### Maven version

Set the Maven development version to:

`0.1.0-SNAPSHOT`

Apply this consistently to all project and parent references.

Architecture and software version identities remain independent.

This version change does not establish an accepted software release.

## 5. Historical deployment evidence

Record the deployment observations identified in the adjudicated proposal.

Preserve the relevant historical configuration paths and checkpoint references.

These observations represent historical implementation evidence, not current operational capabilities.

Do not modify live deployments, databases, persistent volumes, credentials or external infrastructure.

## 6. Governance reconciliation

Prepare the necessary Architecture HCRL amendment for retirement of the obsolete ArchUnit verification requirement.

Preserve the underlying architectural invariants and testing expectations.

Retain Architecture 1.1.26 as the accepted baseline.

Allocate the normative governance amendment to the next appropriate architecture release.

Do not publish a new Architecture release as part of this software cleanup.

Record the supersession of the relevant historical implementation candidates, including Naming 008 and V001 006.

Preserve existing design acceptance decisions and their qualifications.

Do not silently rewrite historical review decisions.

## 7. Verification

Perform verification of the resulting software foundation.

At minimum:

1. Confirm that every approved tracked deletion was performed.
2. Confirm that no unapproved tracked files were deleted.
3. Verify the protected architecture and documentation paths.
4. Verify that all 50 Maven POMs remain.
5. Verify consistent Maven version identity.
6. Execute a complete Maven reactor build using JDK 21.
7. Execute the targeted Calliope reactor build.
8. Inspect resulting Maven artifacts.
9. Confirm that no exploratory Java or frontend implementation remains.
10. Confirm that no placeholder implementation was introduced.

Record the actual verification outcomes.

Do not interpret an empty reactor's successful build as proof of application functionality.

## 8. Evidence and review

Establish the next available Software 0.1.0 HCRL iteration for this cleanup.

Create the appropriate Task, Report and verification evidence using the existing Software Development Praxis.

Document:

- The checkpoint used.
- The approved deletion manifest.
- Actual deletions.
- Maven adjustments.
- Governance changes or pending amendments.
- Verification results.
- Any exceptions.
- Final repository status.

Maintain traceability between the cleanup HCRL and the historical implementation candidates it supersedes.

## 9. Completion boundary

Do not begin implementing Naming.

Do not modify InformationUnit, Pragma or other model definitions.

Do not create a software release tag.

Do not automatically commit or push unless expressly authorised by the applicable governance process.

Return the cleanup results and HCRL for Solution Architect review.

The desired outcome is a clean, coherent Maven software foundation ready for the first authorised Naming implementation.
```
