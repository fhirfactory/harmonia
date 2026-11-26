<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Task — BL-0005 DR-11 Final Corrections

**HCRL iteration** · [software-0.1.0 / 011](../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md) (pending; not advanced)

**Task-Issued-At** · `UNRESOLVED` — the attributable system timestamp of the instruction entering the agent interaction was not available. No time is inferred from file metadata, commits or the current clock.

**Issuer** · Solution Architect

**Operative instruction (verbatim)**

> **Junie — BL-0005 DR-11 Final Corrections**
>
> The Solution Architect accepts the helper-specific interpretation recorded in DR-11.
>
> Please make two targeted corrections:
>
> 1. **Documentation:** Update Draft 002.006 §1.6 to explicitly state that the restrictions on `Resource` and `Profile` ancestors apply only to `InformationVocabulary.profileSchema`, not to the generic Topic model or other valid standard-defined containment hierarchies.
> 2. **Regression test:** Add a focused permanent test confirming that Topics outside the `profileSchema` helper's restrictions remain valid in the InformationUnit model, including their qualified paths and versions.
>
> Preserve all other accepted behaviour.
>
> Update the existing HCRL 011 evidence, rerun affected tests and report the results and revised candidate checksum.
>
> Keep HCRL 011 pending for Solution Architect acceptance. Do not commit, push, merge or allocate a software release.

**Context** · HCRL 011 DR-11; previous candidate `bl-0005-calliope-informationunit-candidate-003.patch` (SHA-256 `96283de013212e5b2407b033aefc67b23e2000f558575ea2895e02877659c951`). The task makes no production-code change and authorizes only the clarified Draft wording, focused regression coverage and corresponding HCRL/evidence updates.