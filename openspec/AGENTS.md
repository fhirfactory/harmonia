# Instructions for Junie

1. Read `project.md` and `MANIFEST.csv` before implementation.
2. Work one capability spec at a time unless a group is explicitly selected.
3. Treat every `### Requirement:` as normative and every `#### Scenario:` as a testable acceptance scenario.
4. Before coding, inspect the existing Harmonia codebase and existing OpenSpec specs to identify whether the capability is new, partially implemented, or already satisfied.
5. Do not infer missing healthcare policy, identity, consent, credentialing, endpoint, terminology, or interoperability rules. Record an open question when the source specification does not establish the rule.
6. Reuse shared domain models and services where appropriate, while retaining traceability to each source use case.
7. Implement security, audit, provenance, lifecycle and data-quality controls where the relevant spec requires them.
8. Add or update automated tests that demonstrate the WHEN/THEN scenarios.
9. Keep implementation-specific decisions out of the behavioural spec; document material architecture decisions separately.
10. Report completion by source use-case reference and spec path.
