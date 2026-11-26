<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Task — BL-0005 Profile-Schema Helper Clarification

**HCRL iteration** · [software-0.1.0 / 011](../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md) (pending; not advanced)

**Task-Issued-At** · `UNRESOLVED` — the attributable system timestamp of the instruction entering the agent interaction was not available. No time is inferred from file metadata, commits or the current clock.

**Issuer** · Solution Architect

**Operative instruction (verbatim)**

> **Junie — BL-0005 Profile-Schema Helper Clarification**
>
> The Solution Architect confirms:
>
> - The generic `Topic` model must support valid containment hierarchies defined by the governing standard, without arbitrary structural restrictions.
> - `InformationVocabulary.profileSchema` may enforce restrictions appropriate to its documented helper contract.
> - Those restrictions must not limit the underlying Topic model or prevent callers from constructing other valid standard-defined hierarchies.
>
> Please verify this distinction against the current implementation and tests.
>
> If already satisfied, report confirmation without modifying code. Otherwise, make only the necessary correction and rerun affected tests.
>
> Record the result in HCRL software-0.1.0/011.
>
> No commit, push, merge or software acceptance is authorised.

**Context** · Candidate `bl-0005-calliope-informationunit-candidate-003.patch` (SHA-256 `96283de013212e5b2407b033aefc67b23e2000f558575ea2895e02877659c951`); HCRL 011 DR-10.
