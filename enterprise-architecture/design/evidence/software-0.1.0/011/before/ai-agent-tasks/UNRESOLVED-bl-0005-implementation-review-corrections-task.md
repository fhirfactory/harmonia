<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# BL-0005 Implementation Review Corrections — Task Details

Task-Issued-At: UNRESOLVED. The attributable system timestamp for this instruction is unavailable at sufficient precision; the supplied local date/time does not establish a timezone or UTC event time. No filename timestamp is inferred.

**Agent / role** · Junie · bounded software implementation corrections.  
**Target** · `software-0.1.0` · HCRL iteration `011`.  
**Related work** · [BL-0005 implementation candidate][hcrl] and its [Completion Report][prior-report].

## Authorised task

Reconcile the existing Calliope InformationUnit implementation with the Solution Architect's decisions arising from review of HCRL software-0.1.0/011. Preserve unaffected implementation, tests and accepted Naming behavior. Update the DRP and Backlog Item through successor documents, preserve their earlier versions, and keep HCRL 011 pending for Solution Architect review.

Apply these accepted corrections:

1. **Managed Revision.** Preserve the originating Managed Revision as the expected revision for a subsequent governed write. Mnemosyne establishes the successor after successful persistence. Local modifications must not clear the originating revision.
2. **Information authority.** Keep required `InformationMetadata.informationAuthority : Topic`. Permit an authorized authority change through a new governed IU revision under the same UUID and preserve the previous revision. Leave `InformationQuality` unchanged.
3. **Themis-Krites boundary.** Krites adjudicates all proposed `SecurityMetadata` changes, including strengthening, weakening and replacement. Keep `SecurityMetadata` immutable; withdraw the add-only restriction; do not make policy decisions in Calliope; do not implement a Krites service; do not introduce JPMS.
4. **Media types.** Add required `ContainerMetadata.containerMediaType : String` and `ContentMetadata.contentMediaType : String` using IANA media type identifiers. Keep them separate from content concepts and schemas; remove media types from the content-concept vocabulary.
5. **RAW path.** Use exactly `Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW`. It means Calliope imposes no additional container structure.
6. **FHIR R4 vocabulary.** Support all 146 FHIR R4 resource types under `Domain=Health.Owner=HealthLevelSevenInternational.Standard=FHIR.Release:4.0.1=R4.Resource=<resource-name>`. `R4` is the release designation, `4.0.1` its specific version, and resource definitions have no independent versions.
7. **Containment.** Retain `Domain → Owner → Standard`; each standard defines its subordinate containment and versioning levels. Express containment through qualified Topic paths, not a separate relationship model.

## Required work and validation

- Modify the existing implementation and focused tests, including negative media-type cases, revision and authority snapshot semantics, and security proposal behavior.
- Update the InformationUnit design proposal and Backlog Item as successor versions; preserve their prior versions unchanged.
- Update HCRL 011 and its evidence without completing or advancing the HCRL.
- Run `mvn -B -ntp -pl calliope -am test` and applicable regression, architecture and documentation checks. Report exact commands and observed outcomes.
- Include an Implementation Guide Conformance report addressing functional behavior, unhappy paths and defensive behavior, educational documentation, architecture/security boundaries, limitations and unresolved matters.

Do not commit, push, merge, declare software acceptance, or claim release acceptance. Do not implement Themis-Krites or alter the software release identity.

[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md
[prior-report]: ../../after/ai-agent-reports/UNRESOLVED-bl-0005-calliope-informationunit-completion-report.md