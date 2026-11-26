<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# BL-0005 Final Corrections — Task Details

Task-Issued-At: UNRESOLVED. The attributable system timestamp for this instruction is unavailable at sufficient precision. The supplied local date/time does not establish a timezone or UTC event time; no filename timestamp is inferred.

**Agent / role** · Junie · bounded software implementation corrections.  
**Target** · `software-0.1.0` · HCRL iteration `011`.  
**Related work** · [Implementation-review corrections candidate][prior-candidate] and [HCRL 011][hcrl].

## Authorised task

Apply the Solution Architect's final BL-0005 corrections while preserving the existing implementation, tests and accepted Naming behavior wherever unaffected. Update the design proposal and Backlog Item through successor documents, keep HCRL 011 pending, and submit the corrected candidate for Solution Architect review.

1. **Generic profile schemas.** Restore generic profile-schema support under the agreed `Domain → Owner → Standard` containment principle. Profile definitions belong under their own governing organisation and standard; do not restore the obsolete `System=Harmonia.Domain=Data...` path, add AU Core-specific vocabulary, or invent an organisation-specific hierarchy. Profile Topics are independent of any relationship to their underlying FHIR resource.
2. **ArchUnit/Paradeigma — HCRL 011 DR-5.** Retain the requirement; do not retire it or remove it from `AGENTS.md`. Defer execution until Harmonia begins implementing complete subsystems. For BL-0005, the existing JUnit, Maven verification and `jdeps` evidence is sufficient. Record the status as `NOT EXECUTED — DEFERRED`, not PASS.
3. **Implementation and reporting.** Apply the focused correction, update tests and documentation, run relevant verification, provide updated Implementation Guide Conformance evidence, record both dispositions in HCRL 011, and submit the candidate for Solution Architect review.

Do not commit, push, merge or declare software acceptance.

[prior-candidate]: ../../after/bl-0005-calliope-informationunit-candidate-002.patch
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md