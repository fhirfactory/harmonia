<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Dokimasia ADD candidate — reconciliation and review basis

**Standing:** candidate prepared under HCRL 1.1.19-001 DR-18/DR-19; Solution
Architect intent agreement is recorded in the operative Task, while acceptance
of this exact representation remains pending. DR-1's scope/status are unchanged.

## Authority, baseline and source correspondence

The [operative Task](../../before/ai-agent-tasks/unresolved-dokimasia-provenance-add.md)
is the available attributable account of the DR-1 discussion and agreed intent.
No separate earlier conversation was supplied or reconstructed. The Task was
retained and DR-18/19 registered before canonical mutation. DR-17 was the last
allocated DR; two new independently adjudicable subjects follow it without
renumbering or disposition changes. No further iteration or release is allocated.

Baseline is current maintained canonical Markdown at Git
`32014bb5843ad7dffdda9ccc404173b758cec690` (initial working tree clean):
1,994 elements / 2,236 native relationships / 1,288 Views. Pre-ADD projection
reproduces accepted DR-17 final-naming specimen SHA-256
`27ef3c237ef7dca9cc7577d8ebbde2ff47930ea6a33249468b36969293c7b5af`.
The XML comparison corroborates canonical state; historical exports are not authority.
Root release remains 1.1.19 / predecessor 1.1.18. A pending candidate consumes no
release increment under the common policy; accepted-baseline publication is excluded.

[Discovery](discovery.json) records scoped search hits, definitions, standing,
qualifications and outgoing native assertions. It separates historical capability
identity from its current successor. Search names only locate subjects. The semantic
review concentrates on the core components/information, independence constraints,
Provenance principles/requirements and their governed references listed below;
the broader name-match inventory does not imply acceptance of every listed assertion.

## Responsibility reconciliation

| Task responsibility | Existing representation and gap | Candidate treatment |
| --- | --- | --- |
| 2.1 Ownership | [Dokimasia](../../../../../../continuum/application/application-component/d739c75dffe2453081735710b285f03b.md) already owns independent Assurance execution; information-assertion governance distinguishes responsibility, authority, custody and consumption. No explicit Provenance Management framework/lifecycle owner is recorded. | ADD bounded ownership/lifecycle clarification to Dokimasia; separately represent [Provenance](../../../../../../continuum/application/data-object/e282811b51274649816566aac7edeb8a.md) as the managed information. Persistence remains distinct. |
| 2.2 Distributed generation | Existing component/framework reuse and Kleio's distinct audit capture library do not represent a reusable Provenance client library. No equivalent current component exists. | ADD [Provenance Client Library](../../../../../../continuum/application/application-component/171f05d188384cedb7d5c493eeaee519.md) as a logical ApplicationComponent; Dokimasia provides it and originating components/activities generate through it. Creation, reference capture and reporting/publication are its common work. No implementation, network/service, API or deployment inference. |
| 2.3 Persistence | [Mneme](../../../../../../continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md) and [Mnemosyne](../../../../../../continuum/application/application-component/7985e647bf9e4ca3ba70faf9e3e4ae02.md) already establish governed information management versus authoritative durable persistence. No explicit allocation for Provenance is present. | Clarify application to Provenance and add native Access assertions. Existing mechanisms and reliability/durability/timeliness requirements apply; no separate store, new SLA or persistence-domain/shard choice. |
| 2.4 References | Information Assertion describes origin/derivation and relevant assertions/activities/requests. InformationUnit, Pragma, Ergon, Context, identity and version semantics already exist. Journey explicitly is not complete Pragma provenance. No separate Provenance information concept/reference responsibility is represented. | ADD one native DataObject and four labelled Association assertions to existing InformationUnit, Pragma, Ergon activity and Context. Reference semantics preserve governed identities/versions and do not require contents embedded. No duplicate identity, execution-attempt entity, cardinality or schema. |
| 2.5 Assurance | Dokimasia, REQ-FND-005, Praxis, Journey, Dokimasia Ergon, AI Assurance Ergon and observation/inference constraints already establish independent assessment, basis, evidence subject and common execution. Provenance is distinct from conclusions; its foundational evidentiary role is not explicit. | ADD qualified text and Dokimasia Access to Provenance for lifecycle management and foundational evidence use. Preserve observed evidence, describing Provenance, basis and assessment/findings/outcomes as separate meanings; no new Assurance family, finding/result DataObjects or workflow. |
| 2.6 Audit boundary | [Kleio](../../../../../../continuum/application/application-component/fefda110e2274884ad58906733606efd.md) and Dokimasia explicitly establish Audit ownership, historical evidence input, no assessment by Kleio and independent Assurance. These parts are already adequately represented. | Preserve every existing assertion; clarify non-transfer alongside new Provenance ownership and add a qualified Association stating historical audit evidence may contribute. Do not equate AuditEvent, Provenance and Assurance, or change historical reference/necessary-replication rules. |

## Reuse and native classification

New Provenance is native **DataObject**, without Class/Topic specialisation: the
Task establishes a governed logical information concept, not its detailed schema,
FHIR resource, information family or implementation class. Existing Information
Assertion remains the assertion-governance concept; Data Provenance Capture remains
a capability, with its historical predecessor separately reserved. Neither is the
same architectural instance as the missing logical information concept.

New Provenance Client Library is native **ApplicationComponent**, representing the
explicit reusable library responsibility. It is not Dokimasia itself, Dokimasia
Ergon, Kleio's audit client contract/library, an API or another execution platform.
These two genuinely distinct concepts were absent; no other element is added.
One UUIDv4 per element was allocated and persisted through the governed `create`
operation. Creation resumed after an agent-side post-write read error by inspecting
and reusing the persisted Provenance identity; it did not reallocate or duplicate it.
New canonicalName values remain qualified nulls because formal qualifiers for native
DataObject/ApplicationComponent are not established (existing HCRL 1.1.5-002 DR-5).
No new naming vocabulary, parentage or Home is invented. Current lexical friendlyName
rules apply; existing names and source bindings remain intact.

Navigation reuses Application / Information Architecture / Reusable Information
Concepts for Provenance and Dokimasia / Component Architecture for the library.
Only two entries are added; no pre-existing entry, path or order is changed.
Navigation does not establish semantic ownership or containment.

## Relationship treatment

[Relationship additions](relationship-additions.json) preserves exact identities,
source/target bindings, type, meaning, standing and Task selectors for ten assertions:

- Dokimasia — Association — client library: provision responsibility, without Composition/Home.
- Dokimasia — Access ReadWrite — Provenance: lifecycle management and foundational Assurance evidence use.
- Client library — Access Write — Provenance: common creation/reference/reporting work for originators.
- Mneme — Access ReadWrite — Provenance: existing governed management.
- Mnemosyne — Access ReadWrite — Provenance: existing authoritative durable persistence.
- Provenance — four directed Associations — InformationUnit, Pragma, Ergon and Context: relevant references/activity description only.
- Kleio — Association — Dokimasia: historical audit evidence may contribute to independent Assurance.

Access states architectural information responsibilities and use, without a new
read/write endpoint or invocation contract. Association is deliberately limited to
the established labelled relation; no unestablished Flow, Composition, Home, Serving,
shared lifecycle or deployment semantics are asserted. Ergon Association references
activity performed by the component, without manufacturing an Ergon Information
Resource. Existing Praxis/Journey and common-execution meaning is reused from their
canonical definitions rather than duplicating their elements or adding speculative edges.

## Axioms and consistency

| Canonical axiom | Candidate consistency |
| --- | --- |
| AX-04 | Reuse common execution and governed information/persistence; library responsibility chooses no technology or parallel platform. |
| AX-05 | Mneme active management remains reconstructable; Mnemosyne alone establishes authoritative durable state/version. Framework ownership does not confer persistence authority. |
| AX-06 | Origin, reference, custody and durable recording do not establish originating information or assessment authority. Existing PROV-003 dual-actor and PROV-004 authority distinctions remain untouched. |
| AX-08/09 | Provenance describes significant activity/information; transient machinery does not automatically become durable evidence. Kleio's durable audit-evidence responsibility is preserved. |
| AX-10/11 | Existing reliability, durability, recoverability and responsive governed-access requirements apply. No sync/async choice, acknowledgement protocol, new performance threshold or weakened semantics is introduced. |
| AX-14 | Audit, describing Provenance, observed evidence, applicable basis, assessment/findings/outcomes, identity/version and active/durable domains remain distinct. |
| AX-17 | Existing candidate/current/historical standing and definitions are preserved. New representations remain visibly candidate pending acceptance; detailed unknowns are not completed by inference. |

The axiom definitions are the canonical Principle resources reached through
[the governed axiom navigation](../../../../../../governance/architectural-axioms.md).
No implementation is changed or used to reverse-derive architecture.

## Conflicts and preserved qualifications

**No conflicting canonical assertion was identified within this bounded review.**
AX-08 says “Kleio preserves durable evidence”; it does not assign exclusive ownership
of every kind of evidence. Its Audit responsibility and the separate Mneme/Mnemosyne
persistence boundary remain intact. Information Assertion's “Provenance / derivation
≠ Audit Evidence ≠ Assurance Evidence” and REQ-FND-005's “Provenance ≠ Assurance”
preserve distinct meanings; foundational evidence input does not make Provenance an
assessment, finding or conclusion. Journey's incomplete provenance qualification
is preserved. Existing Kleio “provenance responsibilities” concern attribution of
audit evidence, not an independently established Provenance Management framework.
No supersession or authority transfer is inferred from those phrases.

Dokimasia's existing detailed information-preservation/representation, independence,
workflow, Praxis/Journey, AI-discovery and O&A candidate qualifications remain intact.
The Task establishes governed Provenance persistence responsibility without resolving
all detailed assurance result/finding persistence structures or assigning a persistence
domain. The finite Dokimasia boundary and prohibition on a separate own-execution
AuditEvent are unchanged; no recursive self-assessment obligation is introduced.
REQ-FND-005 permits evidence contributions without conferring conclusion control.

All six Task §4.5 questions remain unresolved: original versus linked retry Pragma;
execution-attempt representation; sync/async publication; acknowledgement/recovery;
Provenance schema/cardinalities/lifecycle states; client-library contracts/deployment.
The new elements record these explicitly; they do not gate the agreed ADD.
The Task instruction-entry event/prefix is independently UNRESOLVED for Solution
Architect treatment before closure; current time does not repair it.

## Review boundary

Candidate canonical sources and derived evidence are reviewable; successful automated
checks confer no acceptance. Solution Architect candidate/structural inspection and
explicit disposition are outstanding. Actual Archi transport and its strict/raw limits
are recorded separately. DR-1–DR-17 text/dispositions and prior evidence/publications
are preserved; DR-18/19 and the HCRL stay open. No release transition, accepted-baseline
publication, commit or push is performed.
