<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Retained validation development findings

The initial focused reconciliation tests exposed two synthetic-fixture defects:
the baseline contained an XSD-invalid empty Relationships container, and the
fixture omitted governed Property definitions already present in the real
candidate. The fixture was corrected to supply a retained Association and explicit
definitions. A classification-negative case also needed a remaining Property to
avoid an unrelated empty-Properties XSD error. No architectural model was changed
to pass those tests. The final focused suite passes nine tests, including
establishment-history protection, pure replay, tampering, collision and held-scope
cases.

The first full framework run is retained as
[framework-tests-attempt-001.log](framework-tests-attempt-001.log): 284 tests,
10 errors, 149.255 seconds. Every error stopped at the existing frozen authority
fingerprint for `docs/markdown/governance/baseline/README.md`. The newly added
Run-002 index paragraph was removed by restoring the exact before snapshot; the
dedicated accepted RADS source and readable amendment remain. No authority pin or
frozen publisher was weakened or refreshed. The suite was rerun only after this
concrete correction. This result was an authority/input guard failure, not evidence
of a reconciliation, production-code or transport failure.

Revision-1 review harness findings remain separately preserved in the original
review evidence. None of these development results is relabelled PASS.
