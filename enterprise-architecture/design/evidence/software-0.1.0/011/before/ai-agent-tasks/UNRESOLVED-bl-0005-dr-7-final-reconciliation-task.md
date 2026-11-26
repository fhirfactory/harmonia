<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Task — BL-0005 HCRL 011, DR-7 Final Reconciliation

**HCRL iteration** · [software-0.1.0 / 011](../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md) (pending; not advanced)

**Task-Issued-At** · `UNRESOLVED` — the attributable system timestamp of the instruction entering the agent interaction was not available. No time is inferred from file metadata, commits or the current clock.

**Issuer** · Solution Architect

**Operative instruction (verbatim)**

> **Junie — BL-0005 HCRL 011, DR-7 Final Reconciliation**
>
> The Solution Architect has adjudicated the remaining DR-7 engineering decisions.
>
> **1. Successor derivation — replace `deriveSuccessor()`**
>
> Provide two intention-specific operations:
>
> `deriveSuccessorAsClone()`
> - Creates a new IU with a new UUID.
> - Copies the existing data and associated metadata.
> - Preserves all existing relationships.
> - Adds a provenance relationship to the source IU.
> - Reinitialises lifecycle metadata.
>
> `deriveSuccessorWithMinimumMetadata()`
> - Creates a new IU with a new UUID.
> - Inherits SecurityMetadata and OriginMetadata.
> - Adds only the provenance relationship; existing relationships are not inherited.
> - Requires the caller to supply the remaining representation and metadata necessary for a complete IU.
> - Reinitialises lifecycle metadata.
>
> Both operations must produce valid, complete InformationUnits. Preserve the existing immutability and authority boundaries.
>
> **2. Origin idempotence — accepted as implemented**
>
> Established OriginMetadata Topics cannot change, including their versions.
>
> **3. Description and quality owner — accepted as implemented**
>
> - `description` is revisionable.
> - `InformationQuality.owner` is required, non-blank and lifetime-immutable.
>
> **4. Content validation — accepted as implemented**
>
> Retain the existing validation rules for UTF-8, absolute content references, dimensions, duration, language tags and checksum/algorithm pairing.
>
> **5. RAW ContentSchema**
>
> Keep `contentConcept` and `contentSchema` mandatory.
>
> Add a governed RAW ContentSchema Topic with this exact qualified path:
>
> ```text
> Domain=ICT
> .Owner=fhirfactory
> .Standard=harmonia
> .Concept=InformationUnit
> .ContentSchema=RAW
> ```
>
> Preserve the existing RAW container schema separately.
>
> **Implementation and review**
>
> Apply only the necessary implementation, test and documentation changes. Reconcile Draft 002.006 and Backlog Item 0005 revision 000.003 with these decisions through the existing documentation process.
>
> Follow `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`, including defensive programming, meaningful unhappy-path tests and educational documentation.
>
> Update HCRL software-0.1.0/011 with the decisions and verification evidence. Submit the corrected candidate for Solution Architect review.
>
> Do not commit, push, merge, close HCRL 011 or allocate a software release.

**Context** · Candidate-004 (`bl-0005-calliope-informationunit-candidate-004.patch`, SHA-256 `0a075d93cbf63d797b5e24c14eb95e6c8bdd755b8a935a11c0c8c78e5111a8ff`) and DR-7 in the pending HCRL. The task applies only the adjudicated successor, origin, quality/description, content-validation and RAW ContentSchema decisions; it does not authorise software acceptance, HCRL closure, a release allocation or a Git transition.