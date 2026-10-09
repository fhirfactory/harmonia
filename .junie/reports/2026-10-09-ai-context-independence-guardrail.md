<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->

# AI Context Independence and Progressive Authority Loading Guardrail

Date: 2026-10-09

## 1. Summary

Completed the authorised governance update. The Architecture Completion Plan
and repository-level agent instructions now require architectural reasoning
to use repository-held authority and explicitly authorised task decisions,
without depending on previous AI-session interpretation. Fresh context is
the preferred default for a new substantive bounded task. Agents load minimum
sufficient authoritative context and expand it progressively when needed.
Missing, ambiguous or contradictory authority remains explicit under AX-17.

The applicable root and documentation AGENTS.md files, the current Completion
Plan and authoritative AX-17 were read before editing. Existing operating
rules were retained; no architecture-domain review or modification was made.

## 2. Files Changed

| File | Purpose |
| :--- | :--- |
| [docs/markdown/architecture-completion-plan.md](../../docs/markdown/architecture-completion-plan.md#81-ai-context-independence-and-progressive-authority-loading) | Added section 8.1 under Task Control, covering context independence, fresh-context exceptions, progressive authority loading and authoritative versus non-authoritative material. |
| [AGENTS.md](../../AGENTS.md#11-ai-context-and-architecture-authority) | Added section 1.1 under Architectural Authority with concise operational instructions and a link to the Completion Plan guardrail. |
| [This completion report](2026-10-09-ai-context-independence-guardrail.md) | Records the changes, authority boundary, scope, validation and navigation observations. |

The existing `docs/AGENTS.md` instructions remain applicable and unchanged.
The root rule applies repository-wide; its existing non-authority statements
for Junie records were not duplicated or replaced. Pre-existing working-tree
changes, including Domain 02 Step 2 work, were preserved and are not changes
made by this task.

## 3. Completion Plan Change

Section 8.1 establishes that each substantive architecture or implementation
task SHOULD be executable from fresh AI context using repository-held
authority. Previous conversation, AI-session state, memory, summaries and
AI-derived representations SHALL NOT be required to interpret architecture.

Progressive loading starts from operating/governance instructions and
applicable programme context, then considers target material, relevant
upstream authority and explicit references/dependencies/traceability. Broader
material is loaded when needed to establish sufficient authority, resolve a
dependency, validate a relationship or investigate a conflict. The activities
are guidance rather than a fixed mandatory reading order. Required task
inputs still apply; no token, document-count or arbitrary context budget was
established.

Fresh context is a preferred default for new substantive bounded work, not an
absolute restart requirement. Direct clarifications, mechanical corrections,
review before closure and other tightly coupled activities within the same
authorised boundary may continue in the existing session.

The section applies
[AX-17 — Architectural Authority and Explicit Uncertainty](../../docs/architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty):
missing authority remains missing; conflicts and ambiguity are raised rather
than resolved through previous interpretation, memory, convention or inference.
Navigation assistance does not establish authority or make a plausible
relationship authoritative. Existing authority rules and current authoritative
sources remain in force; no new general authority hierarchy was created.

## 4. AGENTS.md Change

The repository-level section 1.1 instructs agents to start substantive work
assuming no previous AI-session context, use repository-held authority and
explicitly authorised decisions, and load minimum sufficient context before
expanding through architectural dependencies, references and traceability.

It permits non-authoritative material to assist navigation/investigation and
requires agents to preserve and raise insufficient, ambiguous or contradictory
authority under AX-17. It also states the fresh-context exceptions and retains
required task inputs and operating rules. A direct link provides the fuller
programme guidance without creating a separate navigation artefact.

## 5. Authority Boundary

Canonical architecture, applicable governance/instructions, explicitly
authorised task decisions and repository artefacts explicitly assigned
authority by architecture are distinguished from discovery aids. This
distinction integrates with the existing rules rather than changing their
precedence.

Previous-session conversation/context, model memory, generated or compacted
summaries, previous-session explanations, `.junie/plans` and `.junie/reports`
were not made architectural authority. They may assist discovery or
investigation, but availability does not confer authority. Implementation
structure, framework conventions, lexical similarity and superseded
historical documentation likewise do not establish architecture by themselves.
This report is an execution record, not architectural authority.

## 6. Scope Confirmation

- No architecture-domain content, capability model, Value Stream or EC
  allocation changed; no new architecture was derived.
- No axiom changed; the entire central register, including AX-17, is unchanged
  from this task's entry state.
- Domain 02 Step 2 was not reopened; Domain 03 was not begun; Domains 04 and 05
  were not modified.
- No architecture index or equivalent new navigation artefact was created.
- No external `/docs` material was migrated, and no implementation code,
  build definition or test configuration changed.
- Architecture-domain status, programme position, completion sequence and
  freeze status were not changed.

Task-entry hash comparison covers 2,281 tracked and non-ignored untracked
files. Only `AGENTS.md` and the Completion Plan changed; this requested report
is the only newly created repository file. Removing the added subsections
from the two documents reproduces their task-entry text exactly.

## 7. Validation

### Documentation and semantic checks

Command: `python3 /tmp/validate-ai-context-guardrail.py` — **PASS** on the
final report-inclusive run. The temporary validator checks active local links
and heading anchors, balanced Markdown fences, the eight required report
sections, preservation of existing document text and task-entry scope hashes.
13 active local links/anchors across three documents passed.

Command: `git diff --check` — **PASS**, no whitespace errors. The new report
was also checked for trailing whitespace and fenced-block structure. No
applicable repository Markdown/link validation command was found in the
available document-generation tooling; focused static validation was used.

| Required check | Result |
| :--- | :--- |
| Completion Plan establishes AI Context Independence | PASS — section 8.1 explicitly establishes fresh-context executability and independence from AI-derived context. |
| Fresh context is a preferred default, with no absolute restart rule | PASS — same-task clarification/correction/review and tightly coupled exceptions stated in both documents. |
| Repository authority preferred over previous AI interpretation | PASS — explicit in both documents; existing authority precedence retained. |
| Progressive authority loading explicit | PASS — six contextual activities and dependency/reference/traceability expansion guidance. |
| Minimum sufficient context is not an arbitrary limit | PASS — no fixed reading order or token/document-count/context budgets; required inputs retained. |
| Conversation, memory and summaries non-authoritative | PASS — explicitly classified as aids rather than architectural authority. |
| Junie plans and reports non-authoritative | PASS — existing rules retained and classification explicit. |
| Unresolved authority explicit under AX-17 | PASS — absent, ambiguous and contradictory authority must be raised without inference. |
| No architecture index created | PASS — the requested report is the sole new repository file. |
| No architecture-domain content changed | PASS — task-entry scope hashes unchanged outside the two authorised governance documents and report. |
| AX-17 not modified | PASS — entire axiom register unchanged from task entry. |
| Implementation code not modified | PASS — scope comparison; no source, build or test changes. |

### Repository-required architecture tests

AGENTS.md section 4 requires all agents making repository modifications to
verify the ArchUnit suite; it contains no documentation/governance exemption.
The suite was therefore run for this task. A current filesystem check
(`os.statvfs` with `os.ST_RDONLY`) confirmed that the Maven dependency cache is
read-only. Cached dependencies allowed an offline run without dependency
metadata writes.

```bash
timeout --signal=TERM --kill-after=10s 300s mvn -o test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

Result: **PASS — BUILD SUCCESS**. **90 tests across 11 architecture suites**;
zero failures, errors or skips. Maven elapsed time: **25.335 seconds**. No
timeout or stall occurred. Results were cross-checked against Surefire XML.
Log: `/tmp/ai-context-guardrail-architecture.log`.

Warnings are separate from failures. Maven/Guava and ArchUnit-shaded Guava
emitted JDK deprecation warnings for `sun.misc.Unsafe::objectFieldOffset`.
ArchUnit 1.3.0 emitted 3,077 WARN entries while importing JDK classes and fell
back to simple import because its shaded ASM importer does not support class
file major version 69 (JDK 25). All suites passed, but full JDK-class import
was unavailable; these results do not establish checks requiring those JDK
internals. No implementation or harness configuration was changed to suppress
warnings. No failed normal Maven invocation was run during this task.

## 8. Observations

No blocking documentation-navigation weakness was observed within this bounded
task. The provided paths and existing authority references located the
required governance and AX-17 material. A repository-wide navigation review
was not performed, and this task does not establish the adequacy of navigation
for future architecture tasks.

Existing navigation remains in place for testing during subsequent
fresh-context work. If future work cannot efficiently locate sufficient
authoritative dependencies, that evidence should be recorded as a navigation
or traceability deficiency for separately authorised review. No index was
created and no navigation file was modified here.
