# Dokimasia and Runtime AI — Orientation Capture

## Outcome and Placement

Recorded the agreed runtime-AI position in [the existing execution model, §6](../../docs/architecture/execution-model.md#6-runtime-ai-as-adjunct-ergo-execution-capability). Created [Dokimasia orientation](../../docs/modules/dokimasia.md) and added navigation in [the documentation index](../../docs/README.md). The established module convention is flat `docs/modules/<name>.md`; no module-directory README convention exists to extend. This report is execution history only, not architectural authority.

## Authority Reviewed and Decisions Preserved

Reviewed central Architectural Axioms, repository/docs AGENTS instructions, the Digital Twin strategic responsibility definition, Ergon/Praxis/Ponos concepts, the execution model, module conventions, approved Health Service Assurance Strategy, current Domain03 assurance behaviour and its linked Role/Function/Process/Service/Interaction definitions, existing Dokimasia findings and the supplied Domain04 G2 Block 1 report. Historical reports were context, not sources of assurance semantics.

Canonical references are linked from the two edited/new architectural pages: [Strategy derivation](../../docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md), [Business Architecture](../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md), [Digital Twin definition](../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md#component-7-digital-twin-entity-centred-operational-coordination-construct), and [approved conceptual Dokimasia finding](../../docs/markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md). Current approved Motivation/Strategy/Business Architecture governs assurance semantics; the older finding does not override subsequent reconciliation.

Material axioms: AX-04 preserves Harmonia semantics independently of execution machinery; AX-06/07/08 preserve authority, governed execution and meaningful evidence; AX-14 preserves responsibility distinctions; AX-17 preserves unestablished allocation. AX-05 remains relevant to existing state/execution seams. No new axiom was created.

Runtime AI is adjunct Ergo business-logic capability. AI use changes neither Ergo/Praxis/Twin responsibility nor authority, governance or execution semantics. Ergo and Digital Twin identity require no AI. AI-assisted development is explicitly separate from intentional runtime invocation. Assurance/Dokimasia are likely significant consumers without mandatory AI use, transfer of Service Guardian responsibility, or independent AI assurance authority. Assessment/adjudication use is neither approved nor prohibited; future approved allocation remains possible under governed authority. Assurance non-recursion remains intact, including when AI is invoked.

## Explicitly Deferred Questions and Existing Concern

Dokimasia is not established as a Digital Twin/subtype. Detailed Twin allocation, Dokimasia / Assurance Praxis / Ponos execution relationships and independence mechanisms remain unresolved. Detailed allocation of approved assurance Roles, Functions, Processes, Services and Interactions is not derived. AI-backed assessment/adjudication allocation, the AI execution model and any separately needed AI-specific governance, safety, provenance or controls remain future architecture.

No application components, APIs, information families/models, persistence, AI orchestration/providers/infrastructure or deployment are defined. No implementation files or Domain01–04 semantics changed; no domain was frozen/refrozen. No convergence/runtime implementation milestone was commenced.

A pre-existing concern was found in execution-model §4: its checkpoint “Durability” description specifies Mneme writes followed by asynchronous Mnemosyne write-behind. AX-05 establishes Mnemosyne's sole authoritative durable-state progression and convergence of Mneme following authoritative commit. The description requires architectural review/clarification of authority and ordering; it is not evidence of an approved authoritative write path. It was preserved outside this bounded task rather than silently corrected or used to derive assurance architecture.

## Files Changed

- `docs/architecture/execution-model.md` — appended the architectural position and assurance/development boundaries, explicitly without an implementation claim.
- `docs/modules/dokimasia.md` — concise orientation and canonical navigation.
- `docs/README.md` — module-tree and clickable orientation navigation.
- `.junie/reports/2026-10-09-dokimasia-runtime-ai-orientation.md` — this report.

## Validation

- PASS: manual semantic review against all requested boundaries; no competing assurance definitions or detailed solution allocation introduced.
- PASS: local file/anchor checks of all 31 links in the execution model and Dokimasia page; index navigation and report links additionally checked at completion.
- PASS: whitespace and changed-file scope checks at completion, including staged changes. No staging or commit command was issued by this task.
- Initial prescribed Maven invocation: environment error before tests, in 1.991 seconds, because dependency resolution attempted to write `/home/mhunter/.m2/repository/com/sun/mail/jakarta.mail/resolver-status.properties` on a read-only filesystem. No semantic test failure was established.
- Offline retry avoided dependency-cache writes: BUILD SUCCESS in 26.630 seconds; 90 tests, zero failures/errors/skips. Java 25 produced ArchUnit “Unsupported class file major version 69” import warnings, so verification was repeated under the installed Java 21 baseline.
- PASS: Java 21 offline verification, BUILD SUCCESS in 27.170 seconds; 90 tests, zero failures/errors/skips and no unsupported-class-file warnings. Command: `JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:$PATH timeout --signal=TERM --kill-after=15s 180s mvn -o test -pl paradeigma/paradeigma-test -am -Dtest='*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`. Logs retained in `/tmp/harmonia-dokimasia-architecture-tests{,-offline,-java21}.log`.
