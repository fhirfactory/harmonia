<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Sole normative Continuum Principle authority

Task-Issued-At: UNRESOLVED

The operative user instruction entered this Codex interaction on 2026-10-22
(interaction date). No attributable instruction-entry timestamp is exposed to the
executing agent. The original event time and required sortable filename prefix
remain unresolved for Solution Architect treatment before closure; no current
clock, file timestamp, commit time or earlier acceptance is substituted.

Issuer: Mark Hunter, Solution Architect, through the operative user request.
Agent: Codex. Resulting release: **1.1.9**, predecessor **1.1.8**, HCRL iteration
**001**. Originating HEAD: `73e5c4f529743ca66d5e3044ebe7a5f743701659`, clean
working tree; root release `1.1.8`, previous `1.1.7`.

Supplied review: [HCRL 1.1.8-001](../../../../../review/1.1.8/hcrl-1.1.8-001.pending.md).
Its [completed register](../../../../../register/1.1.8/hcrl-1.1.8-001.complete.md)
establishes the immediate accepted predecessor. Earlier records remain unchanged.
The operative instruction directly authorises this bounded implementation;
final confirmation remains with the Solution Architect under the existing Praxis.

## Operative instruction — verbatim

## Goal

Remove duplicate current normative definitions of Harmonia architectural axioms and establish the Architecture Continuum Principle elements as their sole current normative authority.

Produce the change as the next governed architecture release: **1.1.9**, with previous version **1.1.8**.

The governing rule is:

> **One definition; many references.**

## Expected Outcome

The architectural Principles represented in the canonical Architecture Continuum are the sole current normative definitions of the Harmonia architectural axioms.

Current Enterprise Architecture governance, guidance, documentation and tooling may identify, cite, navigate to or discuss those Principles, but must not maintain independent normative copies of their content.

Duplicate current normative axiom text is replaced with references sufficient to identify and navigate to the corresponding canonical Continuum Principle.

Canonical Principle content is not rewritten as part of this work.

Historical material remains historical and is not rewritten merely to conform to the current authority model.

## Context

The Architecture Continuum is the canonical architectural baseline.

Previous governance analysis identified divergence risk because architectural axioms/Principles were represented both within the canonical Continuum and as normative text elsewhere in the Enterprise Architecture material.

Maintaining multiple normative copies creates unnecessary synchronisation requirements and allows the copies to diverge.

The desired authority relationship is:

```text
Architecture Continuum Principle
        │
        └── sole normative definition
                ↑
                │ referenced by
                │
        governance / guidance /
        documentation / tooling
```

References outside the Continuum may retain useful identifiers, names and explanatory/navigation context, but they must not become alternative normative definitions.

Legacy-RAD and historical release/evidence material are historical records. Their historical contents are not current authority and should not be rewritten merely because they contain earlier copies of axiom text.

## Constraints

Do **not** change the meaning or wording of the canonical Continuum Principle elements.

Do **not** create replacement Principle elements.

Do **not** alter canonical identity.

Do **not** perform a general review or rewrite of the axioms.

Do **not** rewrite Legacy-RAD archaeological material.

Do **not** rewrite historical release publications, completed HCRLs, historical evidence or other immutable historical records merely to remove old duplicated wording.

Do **not** redesign Enterprise Architecture governance.

Do **not** introduce a new axiom-management framework, registry or synchronisation mechanism.

Do **not** solve duplication by generating and maintaining another authoritative copy.

Where current material genuinely needs to discuss the implications or application of a Principle, retain that contextual material while making clear that the Principle itself is normatively defined by the Continuum.

Use the smallest coherent change necessary to establish the authority boundary.

## Authorised Activity / Scope

You are authorised to:

- identify current Enterprise Architecture material that reproduces architectural axiom/Principle definitions normatively;
- distinguish current material from historical/archaeological evidence;
- replace current duplicate normative definitions with references to the corresponding canonical Continuum Principles;
- retain Principle identifiers and names where useful for readability and navigation;
- retain non-normative contextual guidance about application of a Principle where it does not redefine the Principle;
- update current navigation, references, tooling or validation affected by the consolidation;
- add or adjust bounded validation sufficient to prevent current normative duplication from being accidentally reintroduced, if this can be done simply within existing machinery;
- update release metadata from 1.1.8 to 1.1.9;
- perform the existing deterministic validation/publication/release checks appropriate to this bounded change;
- update current review/register/evidence material required by the existing governance process.

If determining whether particular text is a duplicate definition or legitimate contextual guidance requires a new architectural interpretation, preserve it and report the ambiguity for Solution Architect review rather than deleting or rewriting it.

## Report

Retain the formal Task and Report as governed **1.1.9 evidence** using the current Enterprise Architecture evidence conventions:

- Task under the appropriate `enterprise-architecture/design/evidence/1.1.9/.../before/ai-agent-tasks/` location.
- Report under the corresponding `enterprise-architecture/design/evidence/1.1.9/.../after/ai-agent-reports/` location.

Agent-specific working locations such as `.junie/tasks/` or `.junie/reports/`, if used, are transient working state and are not substitutes for governed evidence.

The Report should identify:

- the current locations in which duplicate normative Principle/axiom content was found;
- which duplicates were replaced by references;
- any contextual/non-normative material deliberately retained;
- historical material deliberately left unchanged;
- any validation added or adjusted;
- deterministic checks performed;
- confirmation that canonical Principle meaning, wording and identity were not changed;
- any genuine ambiguity requiring Solution Architect review.

Do not perform another general analysis of the Continuum, governance framework, documentation framework or historical RAD migration.


## Completion follow-up — 2026-10-22 interaction date

Follow-up Task-Issued-At: UNRESOLVED

Mark Hunter, Solution Architect, supplied the following operative instruction,
linked to the updated [HCRL 1.1.9-001](../../../../../review/1.1.9/hcrl-1.1.9-001.pending.md):

> hcrl-1.1.9-001.pending.md updated. complete!

The exact reviewed pending input has SHA-256 `2c216e17162954e78d63d9cf99c26bd554b0ceff5c760f683a683e5a54c13410`.
Its DR-1 and DR-2 are Accept. DR-2 expressly accepts the bounded safeguards and
formal provenance, including the unavailable original Task instruction-entry
time, as sufficient for this transition with no further remediation required.
This follow-up confirms the incorporated result and directs completion within
the same 1.1.9 change set. Its instruction-entry timestamp is also unavailable;
no time or prefix is inferred. This records the actual temporal limit without
extending acceptance to any different transition or manufacturing recovered timing.
