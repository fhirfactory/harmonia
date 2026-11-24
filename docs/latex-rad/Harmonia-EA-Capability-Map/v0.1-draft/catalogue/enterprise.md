<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

## Managed Entity & Relationship
### Capability Overview
This reusable capability keeps managed healthcare entities identifiable and their relationships structurally intelligible. It supplies common identity, alias and relationship behaviour recurring in persons, practitioners, organisations, locations and devices. Reuse concerns those architectural responsibilities, allowing the healthcare-specific meaning of each entity to remain with its governing context.
### Boundary
Relationship integrity is not a physical database design. Correlating entities does not establish originating clinical identity authority or a new standalone Correlation capability.

## Context Management
### Capability Overview
Context Management preserves the circumstances in which information and activity have meaning as they cross processing boundaries. Subject, encounter, provider, service and security context must remain attributable and consistent rather than be reconstructed from convenient local assumptions. Its reuse is especially significant in multi-hop activity where a syntactically valid message can otherwise lose the context required for legitimate interpretation.
### Boundary
Context participation does not transfer semantic ownership. Generic context management and assurance-specific context semantics contribute together without allocating a new central Context Service.

## Managed State & Lifecycle
### Capability Overview
Managed State & Lifecycle provides reusable responsibility for valid progression, temporal applicability and non-destructive change. A directory relationship, clinical document and operational work item have different business states, yet all need an intelligible distinction between current standing, historical state and invalid transitions. The capability captures that recurrence without replacing each subject's lifecycle meaning.
### Boundary
Business lifecycle integrity does not prescribe locks, database isolation or worker implementation, and illustrative state names do not define every subject's transitions.

## Information Management
### Capability Overview
Information Management provides the recurring ability to represent and maintain governed information through validation, preservation, retrieval and versioned change. It accommodates both responsive active use and authoritative durable preservation while keeping those responsibilities separate. This distinction allows availability and working state to support care without turning a cache into durable truth.
### Boundary
Engine capabilities provide machinery under Harmonia semantics. Persistence success does not establish information authority or make the platform the originator of the represented facts.

## Search & Discovery
### Capability Overview
Search & Discovery makes information, entities and service endpoints findable through governed criteria and bounded queries. It addresses the recurring need to locate relevant material or a destination before it can be used. Security-qualified results preserve the distinction between knowing that something exists and being entitled to obtain or disclose it.
### Boundary
Discovery does not decide policy or prescribe a search engine, index format or query language.

## Policy & Control
### Capability Overview
Policy & Control supplies the reusable evaluation and enforcement of authority, consent, privacy and handling constraints. Its purpose is consistent governed action across ingress, information access, execution and presentation boundaries. Default-deny evaluation preserves the need for established authority rather than treating a caller's intent or a technically valid request as permission.
### Boundary
Participation at multiple enforcement points does not distribute policy ownership or turn operational security context into persistent clinical content.

## Provenance & Traceability
### Capability Overview
Provenance & Traceability preserves the meaningful attribution and history needed to explain information and significant activity. It connects source, transformation and required checkpoints without making every internal mechanism permanent evidence. Its recurring contribution supports accountability, reconstruction and assurance use while retaining the context that makes a recorded fact evidentially relevant.
### Boundary
Telemetry, provenance, audit and assurance evidence remain distinct. Recording evidence neither establishes an assurance conclusion nor automatically requires retention of routine technical activity.

## Interoperability & Exchange
### Capability Overview
Interoperability & Exchange moves information across participating boundaries through destination resolution, transformation, delivery and acknowledgements. It preserves the difference between technical transmission and the recipient's business response so that an exchange can be understood under its actual contract. Standards govern the external representation while internal management semantics remain private.
### Boundary
Egress ends Harmonia management of the emitted representation. Retained delivery evidence does not extend control over an external recipient or transfer payload authority.

## Event & Subscription
### Capability Overview
Event & Subscription connects significant changes with participants whose registered interests match them. It makes event ingestion, correlation, interest criteria and notification reusable across clinical and operational contexts. The architectural meaning is the relationship between a change and an interested consumer, rather than the broker constructs used to carry it.
### Boundary
Receiving a notification does not determine the consumer's business response; topics and queues do not define the capability.

## Activity & Execution
### Capability Overview
Activity & Execution provides reusable instantiation, matching, dispatch and supervision for governed activity units. Human Work Orders, human decision To Dos and automated Synthetic Tasks retain different performer and authority semantics while sharing a need for accountable progression. This reuse supports operational coordination without making a runtime engine the definition of the work.
### Boundary
Coordination does not confer clinical work allocation or judgement. The capability specifies neither worker threads nor a particular execution engine.

## Interaction & Experience
### Capability Overview
Interaction & Experience makes contextual information and governed actions available to human participants through presentation and collaboration integration. Clinical timelines, directory administration and operational dashboards have different purposes, but share a need to retain established identity, security and information context. The recurring responsibility is usable interaction within those boundaries.
### Boundary
Presentation does not own consent, authentication or clinical truth, and collaboration projection does not absorb clinical documentation semantics.

## Operational Assurance
### Capability Overview
Operational Assurance preserves processing integrity under concurrency, duplicate delivery and failure. It brings together resilience, durable ingress acceptance, recovery and operational telemetry so that technical degradation does not silently weaken the managed operation's semantics. Its contribution is collaborative across operational boundaries while remaining a bounded architectural responsibility.
### Boundary
Operational health is distinct from independent assessment of a governed subject. Assurance-specific criteria applicability, adjudication and conclusions belong to the established EC-14 contribution.

## Semantic Governance & Conformance
### Capability Overview
This capability makes canonical information models, terminology bindings, mappings and conformance rules governable across enterprise use. It addresses both what representations mean and whether they respect the published rules, keeping version and compatibility concerns explicit. A conformant representation can therefore be distinguished from one that merely uses a supported transport or parser.
### Boundary
Conformance does not establish clinical truth, information authority or disclosure permission. Calliope is an established responsibility centre, not the definition of the capability.

## Service Guardian
### Capability Overview
Service Guardian supplies the reusable assurance semantics that bind a governed subject to applicable criteria and trustworthy evidence, support assessment and adjudication, and establish reportable findings. Its contribution distinguishes explicit evidence insufficiency from a conclusion about the subject. Existing Enterprise Capabilities contribute collaboratively; EC-14 does not duplicate all their information, policy or execution responsibilities.

Independence concerns control of evaluation and conclusion. A subject may supply evidence but cannot solely determine, suppress or retrospectively change its own assurance outcome.
### Boundary
The capability does not manage or remediate the subject, replace operational assurance, or assure clinical adequacy. No application component, runtime service or deployment boundary is allocated, and the explanatory sequence is not a derived Business Process.
