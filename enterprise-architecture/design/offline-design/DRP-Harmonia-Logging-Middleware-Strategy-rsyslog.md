<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Package — Harmonia Logging Middleware Strategy

**Subject:** Logging Services — Log-Satellite, messaging fabric, Log-Server and Log-Persistence  
**Date:** 2026-11-03  
**Disposition:** Proposed governed architecture input, subject to Solution Architect review and formal reconciliation  
**Strategic conclusion:** **Base Harmonia's Log-Server strategy on rsyslog.**  
**Scope:** Operational logging (`LogEvent`); not the separate AuditEvent/Provenance information model or dereferenced export design.

## 1. Purpose and decision context

This DRP consolidates the complete sequence of middleware investigation undertaken for Harmonia logging: initial Pass 1 capability discovery; revised Pass 1 satellite/server and messaging-fabric survey; Pass 2 architectural suitability; and Pass 2B examination of actual FOSS Log-Server products. It captures the Solution Architect's corrections and decisions, the candidate alternatives and their rationale, and the resulting rsyslog-based strategy. It is **not** an instruction to implement, commit, publish, or accept a RAD transition.

Harmonia's reuse-of-middleware strategy requires a literature survey before defining custom services. The intent is to reuse established FOSS functionality and implement only the missing Harmonia-specific integration. An early survey concentrated on logging frameworks, messaging and databases while overlooking reusable logging-server software; Pass 2B corrected that omission.

## 2. Architectural decisions and constraints

### 2.1 Agreed architecture

```text
┌──────────────────────────────────────────────────┐
│ Application JVM                                  │
│                                                  │
│  Application logging framework                   │
│      ├────► Local-Logger ─────────► Console      │
│      │                                           │
│      └────► Log-Satellite                        │
│                 ├─ InJVM-Log-Event-Receiver      │
│                 ├─ InJVM-Log-Event-Buffer        │
│                 └─ InJVM-Log-Event-Forwarder     │
└────────────────────────┬─────────────────────────┘
                         │
                  Messaging Fabric
                  (technology TBD)
                         │
             ┌───────────┴───────────┐
             ▼                       ▼
       Log-Server A             Log-Server B
       (rsyslog strategy; independently deployable)
             │                       │
             └───────────┬───────────┘
                         ▼
              Common Log-Persistence
              (technology TBD; outside Mnemosyne)

    Mnemosyne: separate governed persistence responsibility
```

The Log-Satellite is **in the application's JVM**, not a sidecar or per-node daemon. Its three explicitly named roles are `InJVM-Log-Event-Receiver`, `InJVM-Log-Event-Buffer` and `InJVM-Log-Event-Forwarder`. The Local-Logger writes to console **in parallel**, not through the satellite. The satellite has no durable local journal and application containers do not require dedicated persistent logging volumes. Log-Servers can be replicated and affinity-aware; Log-Persistence is a **common logical service**, not independent datasets owned by individual Log-Servers. Common does not imply one database process, one host, one storage volume or one failure domain.

### 2.2 Agreed delivery semantics

- The satellite uses a **bounded in-memory buffer**.
- When the buffer is exhausted, **backpressure** propagates to the producer; intentional event shedding is not permitted.
- An event remains the satellite's responsibility until the **Log-Server acknowledges receipt**; a durable-persistence acknowledgement is **not** required.
- A LogEvent is **not** an AuditEvent. AuditEvent/Provenance durability and immutable-information semantics must not be imported into ordinary LogEvent delivery by default.
- The Local-Logger remains a parallel console path; satellite/server outages should not create a dependency of console output on central forwarding. The exact effect of producer backpressure on a shared logging call path requires verification.
- Satellite memory does not survive JVM failure. A receipt ACK does not prove the event is durably committed; loss between receipt and persistence remains possible.
- A lost ACK or failover can cause retransmission and duplicates. Duplicate detection or suppression has **not** been accepted as a mandatory requirement.

### 2.3 Architectural and deployment boundaries

- **Log-Persistence is OUTSIDE Mnemosyne.** It is independent logging infrastructure, not a Mnemosyne `PersistenceServiceInstance`.
- Log-Persistence and Mnemosyne **may use the same underlying FOSS storage technology**, without sharing architectural responsibility or necessarily sharing service instances or data.
- Logging infrastructure remains subject to applicable security, privacy, operations, recovery and deployment governance. LogEvents may contain PHI; their protection cannot be presumed unnecessary.
- FOSS is mandatory. Harmonia itself is GPL-3.0-or-later; copyleft licensing is **not** intrinsically a disadvantage.
- Linux/Ubuntu and container-based deployments matter; affinity, discovery and failover are first-class evaluation concerns.
- The messaging fabric is **not selected**. JGroups is a candidate, not a decision. Efficient binary/persistent-connection transport is strongly preferred; HTTP ingestion is not the preferred internal logging transport. Unix-domain sockets are interesting for co-located peers but not sufficient by themselves for cross-host communication. HTTP may still be suitable for management or external export.
- The strategy is to reuse a **FOSS Log-Server**, rather than assuming Harmonia must write its own complete logging server.

## 3. Evaluation method and scope

The survey separated **functional suitability** (capture, buffer, route, store, retrieve, export) from **deployment suitability** (in-JVM satellite, affinity, clustered servers, storage locality, failure handling). The main criteria were FOSS licensing, architectural fit, acknowledgement and backpressure semantics, integration complexity, security, resilience, Java compatibility, operational footprint, temporal retrieval, bulk export and avoidance of unnecessary analytical infrastructure. Qualitative ratings below are literature-based, **not measured benchmarks or verified interoperability results**.

The survey considered five families: (1) application logging frameworks; (2) collection/processing agents and aggregators; (3) messaging fabrics and logging protocols; (4) embedded journals and databases; and (5) complete Log-Server products. Relational databases were also considered as common Log-Persistence candidates. Internal full-text analytics was not a requirement; external Elasticsearch/data-lake-style ingestion may be enabled by bulk export.

## 4. Pass 1 — initial capability discovery

### 4.1 Logging frameworks and collection

| Candidate | Relevant capabilities | Limitations / disposition |
|---|---|---|
| Apache Log4j 2 (Apache-2.0) | Java appenders, console output, filters, structured events, asynchronous buffering, rolling files, extensible delivery | Strong satellite/Local-Logger candidate; not complete Log-Server or persistence management |
| Logback / SLF4J | Java logging appenders and façade; console and asynchronous options | Retain as alternative/interoperability layer |
| Fluent Bit (Apache-2.0) | Collection, filtering, routing, filesystem buffering, forwarding, backpressure options | Useful collector; not a complete historical retrieval store; disk modes may drop records when limits reached |
| Vector (MPL-2.0; verify component distribution) | Collector/aggregator, transforms, memory/disk buffers, sockets, sinks | Reassess as Log-Server; generic socket reception lacks application-level receipt ACK |
| OpenTelemetry Collector (Apache-2.0) | Standardised receivers, processors, exporters; agent/gateway pattern | Observability interoperability, not itself durable Log-Persistence |
| Grafana Loki (AGPL-3.0; verify current edition) | Temporal log streams, indexed labels, querying, retention | Dedicated log platform; high-cardinality record identity and infrastructure coupling require care |

**Observation:** Capture, transport and persistence need not be the same middleware. An embedded application logging library can forward to a server, and that server can use independent persistence middleware.

### 4.2 Embedded storage, event journals and relational reference

| Candidate | Strengths | Constraints for common Log-Persistence |
|---|---|---|
| Chronicle Queue | In-JVM append-oriented persistent journal, rolling files, replay and sequential reading | Arbitrary indexed retrieval needs support; cross-host replication/FOSS boundary requires scrutiny; network filesystem unsupported |
| RocksDB | Embedded durable ordered key-value storage, efficient key lookup and iteration | Secondary indexing, export, shared-service replication/HA need integration; not a distributed store by itself |
| EclipseStore | Java-native object-graph persistence | Less naturally aligned with independent immutable log records and time-range export; recovery/HA needs assessment |
| SQLite | Embedded transactions, SQL, indexes, JSON, backup and simple export; public-domain | WAL single-writer and same-host constraints; cross-host common service/HA requires external architecture |
| PostgreSQL | Concurrent writers, SQL/indexes, partitions, `COPY`, mature backup and replication | Requires managed server/storage infrastructure; candidate common persistence engine, not selected |
| Apache Kafka | Durable distributed event streaming, replay and retention | Broker infrastructure may duplicate existing responsibilities and exceed logging requirements |

**Observation:** Embedded libraries remain viable **inside Log-Persistence**, even though application containers must not each mount persistent volumes. Reusing the same implementation does not mean using the same persistence instance. The choice of a common logical persistence service makes established concurrency and HA mechanisms especially relevant.

## 5. Revised Pass 1 — satellite/server topology and messaging fabrics

### 5.1 Topology correction

The first survey was overly storage-centric. The Solution Architect introduced a **Log-Satellite / Log-Server** distinction to avoid persistent volumes on every application container and permit real-world affinity. The satellite is specifically in-JVM, with receiver, buffer and forwarder; it works alongside the Local-Logger. The selected buffering policy is **backpressure**, and the release boundary is **server receipt**, not persistence commit.

Satellite/server deployment patterns in existing ecosystems (OpenTelemetry agent/gateway, Vector agent/aggregator, Fluent Bit forwarding) demonstrate that collection and aggregation can be separated. They are precedents, not automatically conformant implementations of Harmonia's exact in-JVM satellite.

### 5.2 Messaging-fabric candidates

| Candidate | Architectural advantages | Remaining responsibilities / concerns |
|---|---|---|
| **JGroups** (Apache-2.0) | JVM-native group membership, discovery, reliable unicast, failure detection, configurable TCP/UDP stacks and flow control | Application receipt ACK, affinity policy, configuration/discovery across deployments, group membership overhead; **candidate only** |
| **Netty** (Apache-2.0) | Efficient Java asynchronous TCP, persistent connections, framing, Linux native transports and Unix sockets | Cluster membership, server discovery, routing, affinity, retries and receipt ACK must be built/integrated |
| **Aeron** (Apache-2.0) | High-throughput UDP/IPC transport; optional archive/cluster capabilities | Media-driver operations and potentially excessive optimisation; receipt ACK and affinity integration |
| **JeroMQ / ZeroMQ** (JeroMQ MPL-2.0) | Pure-Java brokerless messaging patterns, TCP/in-process communication | Discovery, affinity, retry/ACK and reconnection policy; do not assume native Unix-socket semantics in JeroMQ |
| **NATS** (Apache-2.0) | Established clustered TCP messaging service and client ecosystem | Additional broker tier; Core NATS at-most-once semantics and broker ACK not equal Log-Server receipt; JetStream adds persistence infrastructure |

**Finding:** JGroups was provisionally strongest on combined messaging/membership capability; Netty strongest as a transport substrate. Neither was selected. The later discovery of native logging-server protocols means the transport comparison must include **complete server/protocol pairings**, not just generic fabrics.

## 6. Pass 2 — suitability of satellite, fabric and storage

### 6.1 Log-Satellite: Log4j 2 vs Logback

Both can provide console appenders and custom appenders, with bounded asynchronous facilities. Log4j 2 was the stronger preliminary fit because of its mature asynchronous and appender architecture. **However**, a standard async appender dequeues on handoff to a downstream appender, which is not automatically equivalent to retaining an event until remote Log-Server receipt ACK. An ACK-aware forwarder and/or explicit bounded in-flight queue may be required. Queue-full policies and fallback paths must be verified to avoid shedding, unexpected synchronous operations or deadlock. Logback remains a credible alternative. No framework was selected.

### 6.2 Messaging fabric

JGroups: higher-level membership/discovery/failure-detection reuse. Netty: smaller network abstraction, greater Harmonia coordination code. JeroMQ: brokerless messaging alternative. Aeron: potentially disproportionate footprint. Affinity-aware routing and server failover remain separate from raw network delivery. Reliable transport is **not** proof of Log-Server acceptance or durable persistence.

### 6.3 Log-Persistence

The initial qualitative assessment favoured **PostgreSQL** for a common logical service because it natively supports concurrent writers, transactional storage, indexed time-range retrieval, `COPY` export and established HA/replication patterns. Chronicle Queue, RocksDB and SQLite are credible engines but would generally need more architecture to present a common, cross-host, highly available service. This was a **provisional storage finding, not a storage selection**. The Log-Server survey later established that some servers already offer PostgreSQL sinks, making a native server/database pairing worth examining.

### 6.4 Provisional whole-system configurations (before Log-Server correction)

| Satellite | Fabric | Persistence | Finding |
|---|---|---|---|
| Log4j 2 | JGroups | PostgreSQL | Strong functional fit; **incorrectly assumed custom Log-Server** |
| Log4j 2 | Netty | PostgreSQL | Leaner network layer, more coordination logic; same server omission |
| Log4j 2 | JeroMQ | PostgreSQL | Brokerless alternative; same server omission |
| Log4j 2 | JGroups | Chronicle Queue | Additional distributed persistence/HA questions |
| Log4j 2 | Netty | RocksDB | Additional distributed persistence/HA questions |

**Correction:** These combinations are incomplete until the Log-Server is identified. The assumption that Harmonia would build a lightweight custom server was not justified by the reuse strategy.

## 7. Pass 2B — FOSS Log-Server survey

The corrected question: **Which existing FOSS server can receive, buffer, route, manage and persist LogEvents, while interoperating with an in-JVM satellite and common Log-Persistence?**

### 7.1 rsyslog — selected strategic foundation

**Role:** Mature modular logging daemon/server.  
**Licence:** GPL family for core (rsyslog core commonly GPLv3); components/plugins can have differing notices and must be checked against the chosen distribution. FOSS compliance is affirmative.  
**Principal capabilities:** TCP/UDP and RELP reception; input and output modules; rulesets, filters, transforms, memory/disk-assisted queues, flow control, native PostgreSQL output (`ompgsql`), and external-program integration (`omprog`). It can run as independent server instances with common downstream storage. Bulk historical export is primarily a **Log-Persistence** capability, not an inherent rsyslog retrieval API.

**Fit:** Strongest candidate for Harmonia's Log-Server strategy. rsyslog supplies substantial existing server functionality, reducing the need to implement a new receiving, routing and queue-management daemon. RELP is an important candidate native transport to investigate. JGroups or another independent fabric could still be used through an adapter if justified; **neither RELP nor JGroups is selected**. Verify precisely what rsyslog/RELP ACK represents, particularly when server queues are memory-based or disk-assisted, and whether that matches the agreed receipt boundary. The source/transport adapter for the in-JVM satellite remains to be determined.

**Licensing:** Harmonia is GPL-3.0-or-later, so GPL licensing is aligned with its FOSS approach; copyleft is not a selection penalty. Custom modifications and plugins are viable subject to ordinary licence notices, source-distribution and compatibility obligations. An independent service boundary is an architectural decision, **not** a workaround required by proprietary licensing.

**Limitations:** rsyslog is not JVM-native; custom JGroups reception would require a bridge/input extension. Historical query, retention and export depend substantially on storage architecture. Server-side queue durability and acceptance behaviour must be assessed against the agreed receipt ACK semantics. PHI handling, secure transport, operational recovery and configuration require explicit verification.

### 7.2 syslog-ng Open Source Edition

**Role:** Mature logging receiver/router/processor.  
**Capabilities:** TCP/UDP/Unix socket sources, structured name-value events, filters and transformations, memory/disk buffering, flow control, SQL destinations including PostgreSQL.  
**Strengths:** Good structured-event support, configurable routing, database integration.  
**Limitations:** Application receipt ACK and native protocol suitability require examination; custom fabric integration would need an adapter.  
**Disposition:** Strong alternative; not selected as strategic foundation.

### 7.3 Vector

**Role:** Modern log aggregator.  
**Capabilities:** Native TCP socket sources with framing/structured codecs, transforms, routing, memory/disk buffering, sink plugins, documented aggregator topology, and PostgreSQL sink (reported beta in survey).  
**Strengths:** Modern lightweight pipeline and binary-capable ingestion.  
**Limitations:** Generic socket source does not supply the required application-level receipt ACK; native Vector protocol ACK semantics differ and need verification; PostgreSQL sink maturity is a concern.  
**Disposition:** Strong alternative; not selected.

### 7.4 Fluentd

**Role:** Extensible logging aggregation server.  
**Capabilities:** Forward protocol over TCP/TLS, optional chunk ACKs, buffers, tags/routing, failover and output plugins.  
**Strengths:** An established native forwarding protocol could substantially reduce bespoke satellite/server messaging code.  
**Limitations:** Confirm exactly when Forward ACK is issued; Ruby runtime/plugin operational footprint; PostgreSQL output plugin maturity/integration.  
**Disposition:** Serious alternative; not selected.

### 7.5 Graylog Open

**Role:** Integrated centralised log management, processing, search and indexing platform.  
**Strengths:** Complete management and search environment.  
**Limitations:** Heavier, more tightly coupled storage/indexing and analytics than required; could duplicate external SEP analytical functions. Licensing/edition distinctions require verification.  
**Disposition:** Reference platform, not preferred.

### 7.6 Apache Flume

**Role:** Source/channel/sink event collection framework.  
**Strengths:** Architecturally relevant aggregation pattern.  
**Limitations:** Survey found dormant/rework status and explicit deployment caution; current project/release status must be checked before reconsideration.  
**Disposition:** Excluded from immediate shortlist.

### 7.7 Consolidated server comparison

| Criterion | rsyslog | syslog-ng | Vector | Fluentd | Graylog |
|---|---|---|---|---|---|
| FOSS server | Yes | Yes | Yes | Yes | Edition-specific check |
| Mature reception/routing | Strong | Strong | Strong | Strong | Strong |
| Server-side queues | Yes | Yes | Yes | Yes | Platform-dependent |
| Native TCP ingestion | Yes | Yes | Yes | Yes | Yes |
| ACK-capable protocol candidate | RELP | Needs verification | Native Vector protocol; generic socket lacks ACK | Forward | Input-dependent |
| PostgreSQL output | Native `ompgsql` | SQL destination | Beta sink in survey | Plugin-dependent | Not standard architecture |
| Independent common persistence | Strong fit | Strong fit | Strong fit | Strong fit | Weaker fit |
| Native JGroups integration | No | No | No | No | No |
| Historical export | Via storage | Via storage | Via storage | Via storage | Platform features |
| Operational footprint | Modest | Modest | Modest | Moderate | Higher |

The survey's qualitative result was to investigate **rsyslog and Fluentd first**, especially their established acknowledgement-capable protocols, while retaining syslog-ng and Vector as credible alternatives. The Solution Architect has now established **rsyslog as the strategic foundation**; the alternatives remain documented as evaluation evidence rather than equal strategic selections.

## 8. Transport alternatives after Log-Server selection

The earlier generic messaging-fabric shortlist must now be assessed against rsyslog's native capabilities. Avoid adding an independent fabric solely because it is familiar.

| Candidate integration | Reuse opportunity | Open issue |
|---|---|---|
| **RELP → rsyslog** | Established reliable logging protocol and native rsyslog integration | Java client availability/maintenance; exact ACK boundary; affinity and discovery |
| **JGroups → rsyslog adapter** | JVM membership, discovery, failure detection and routing | Additional receiving bridge/extension; complexity and failure boundary |
| **Netty/TCP → rsyslog input** | Lean binary framing and persistent connections | Must match an existing rsyslog input/protocol or implement extension; ACK contract |
| **Other native rsyslog protocols** | Potentially minimal custom integration | Confirm framing, ACK semantics, security and operational behaviour |
| **Fluent Forward** | Credible alternative in a Fluentd-based architecture | Not native rsyslog strategy; retained as comparative evidence |

**No transport selected.** Internal HTTP ingestion remains disfavoured. Efficient messaging, persistent connections, bounded queues, affinity-aware routing, discovery/failover and Log-Server receipt ACK are the evaluation targets. Unix-domain sockets may be useful for co-located deployments but cannot alone cover cross-host satellite/server traffic.

## 9. Log-Persistence and separation from Mnemosyne

The Log-Server population shares a **common logical Log-Persistence service**. It is independent of Mnemosyne, and it is **not** a Mnemosyne persistence instance or a Mneme façade. Common service does not mandate a single physical deployment. PostgreSQL is a **leading provisional candidate** because rsyslog has native PostgreSQL output and PostgreSQL offers concurrency, temporal SQL, bulk `COPY`, partitioning and established replication/backup. No storage technology or topology has been accepted.

Other FOSS engines (Chronicle Queue, RocksDB, SQLite, EclipseStore) remain potential implementations behind a suitable Log-Persistence service, but distributed concurrency, HA and operational complexity must be accounted for. Log-Persistence may reuse a technology also used by Mnemosyne without merging responsibilities, stored information or security boundaries. Sophisticated forensic analytics may be performed externally using governed, time-bounded bulk exports; native export capability and interfaces remain to be designed.

## 10. Risks and unresolved design questions

| ID | Issue | Status / required investigation |
|---|---|---|
| LS-R01 | Server crashes after receipt ACK but before persistence | Accepted consequence of receipt-not-commit boundary; assess risk, server buffering and recovery |
| LS-R02 | Lost ACK / failover causes retransmission | Duplicate delivery possible; deduplication policy **TBD**, not assumed |
| LS-R03 | Satellite queue fills during outage | Backpressure agreed; verify no shedding, deadlock or unintended logging-framework fallback |
| LS-R04 | Parallel Local-Logger and satellite share caller | Verify central-path blocking does not incorrectly make console output depend on forwarding |
| LS-R05 | Native rsyslog/RELP ACK semantics | Confirm exact acceptance boundary and interaction with memory/disk queues |
| LS-R06 | Fabric selection | RELP, JGroups+adapter, Netty/native protocol and alternatives remain **TBD** |
| LS-R07 | Affinity and failover | Define preferred-server selection, discovery, retry, handover and membership behaviour |
| LS-R08 | Security and PHI | Secure transport, identity, authorisation, storage access, isolation and PHI controls require review |
| LS-R09 | Server-side queue policy | Determine bounded capacity, flow control, recovery and whether server-side durable spooling is appropriate |
| LS-R10 | Common Log-Persistence HA | Choose technology/topology, replication, backup, retention and failure domains |
| LS-R11 | Export and retention | Define time-bounded bulk export, record format, indexing and governed retention without dereferencing |
| LS-R12 | Logging event schema | Define identifiers, temporal markers, source/correlation metadata and any duplicate-handling fields |
| LS-R13 | rsyslog extension boundary | Determine whether existing inputs/outputs suffice or whether GPL-compatible plugin/bridge work is justified |
| LS-R14 | Licensing verification | Verify actual source tree/package/module licences, third-party dependencies and distribution notices |
| LS-R15 | Operational footprint | Validate Ubuntu/container/OpenShift deployment, resource cost, health, shutdown and restart |
| LS-R16 | Performance | Benchmark realistic workloads; no comparative benchmark was performed during survey |

**Critical nuance:** Backpressure avoids intentional buffer-overflow loss but cannot make an in-memory satellite crash-safe. Receipt ACK shifts responsibility to the server without proving disk commit. These distinctions must remain explicit in future architectural or test documentation.

## 11. Verification programme — proposed, not authorised

1. **Satellite proof:** Compare Log4j 2 and Logback integration; console output alongside bounded in-JVM receiver/buffer/forwarder; queue exhaustion and backpressure; explicit ACK-aware in-flight handling; clean shutdown and outage recovery.
2. **rsyslog server proof:** Deploy multiple rsyslog instances; test native reception, queues, routing, input acknowledgement, failure and recovery; confirm exact RELP ACK meaning and interaction with configured server queues.
3. **Transport comparison:** Compare native rsyslog protocol (especially RELP) with JGroups/adapter and a minimal Netty-based alternative where justified; test affinity, discovery, failover, duplicates, throughput, latency, CPU and memory.
4. **Persistence proof:** Test multiple Log-Servers against common logical Log-Persistence (PostgreSQL as candidate); concurrency, temporal retrieval, retention, export, restart, recovery and HA behaviour.
5. **Governance and packaging:** Verify PHI handling, security controls, licence inventory, packaging, FOSS dependencies and Ubuntu/container/OpenShift operations.

The purpose of these experiments is to validate the **rsyslog-based strategy and choose supporting technologies**, not to reopen the accepted strategic direction without evidence.

## 12. Strategic decision and architectural disposition

### DRP-LOG-STRATEGY-01 — rsyslog-based Log-Server strategy

**Decision:** Harmonia's operational logging strategy shall be **based on rsyslog as the FOSS Log-Server middleware foundation**. Harmonia shall prefer rsyslog's established receiving, queueing, routing, filtering and persistence-integration facilities over constructing a new complete logging server. The Log-Satellite remains an in-JVM receiver/buffer/forwarder alongside a console Local-Logger; the server population uses common Log-Persistence outside Mnemosyne.

**Rationale:** The four survey passes show that rsyslog provides a mature FOSS server implementation and native storage integration, materially reducing custom middleware. Its GPL licensing aligns with Harmonia's GPL-3.0-or-later FOSS strategy. rsyslog is a better strategic fit than presuming a custom Log-Server, and its modularity permits independent evaluation of transport and persistence.

**Explicit non-decisions:** This decision does **not** select Log4j 2, Logback, RELP, JGroups, Netty, PostgreSQL, a particular rsyslog plugin, an HA topology, a server-side buffering durability policy, or a deduplication policy. It does **not** make Log-Persistence part of Mnemosyne. It does **not** apply ordinary LogEvent semantics to AuditEvent or Provenance.

**Governance status:** This document records the Solution Architect's expressed strategic direction for review and governed incorporation. It does not itself claim an accepted RAD baseline, completed HCRL, architecture publication, implementation authorisation or verified product conformance.

## 13. Literature and primary project documentation

The survey relied on project/vendor documentation and project repositories. These are **references for technical validation**, not claims that every capability or licence has been independently tested against a pinned version.

- Apache Log4j 2: https://logging.apache.org/log4j/2.x/manual/async.html ; https://logging.apache.org/log4j/2.x/manual/appenders/delegating.html
- Logback: https://logback.qos.ch/ ; SLF4J: https://www.slf4j.org/
- rsyslog documentation: https://docs.rsyslog.com/ ; licensing: https://docs.rsyslog.com/doc/licensing.html ; PostgreSQL module: https://docs.rsyslog.com/doc/configuration/modules/ompgsql.html ; external program module: https://docs.rsyslog.com/doc/configuration/modules/omprog.html
- syslog-ng: https://syslog-ng.github.io/admin-guide/
- Vector: https://vector.dev/docs/ ; aggregator: https://vector.dev/docs/setup/going-to-prod/arch/aggregator/ ; socket source: https://vector.dev/docs/reference/configuration/sources/socket/ ; PostgreSQL sink: https://vector.dev/docs/reference/configuration/sinks/postgres/
- Fluentd: https://docs.fluentd.org/ ; Forward output: https://docs.fluentd.org/output/forward.html
- Graylog: https://go2docs.graylog.org/ ; Fluent Bit: https://docs.fluentbit.io/
- OpenTelemetry Collector: https://opentelemetry.io/docs/collector/ ; Grafana Loki: https://grafana.com/docs/loki/latest/
- JGroups: https://www.jgroups.org/ ; manual: https://javagroups.sourceforge.net/manual5/index.html
- Netty: https://netty.io/ ; Aeron: https://aeron.io/docs/ ; ZeroMQ/JeroMQ: https://github.com/zeromq/jeromq ; NATS: https://docs.nats.io/
- Chronicle Queue: https://github.com/OpenHFT/Chronicle-Queue ; RocksDB: https://rocksdb.org/ ; EclipseStore: https://github.com/eclipse-store/store
- SQLite WAL: https://sqlite.org/wal.html ; PostgreSQL: https://www.postgresql.org/docs/current/ ; PostgreSQL `COPY`: https://www.postgresql.org/docs/current/sql-copy.html
- Apache Kafka: https://kafka.apache.org/ ; Apache Flume: https://github.com/apache/logging-flume

## 14. Review checklist

The Solution Architect should confirm: (a) the rsyslog strategic selection and scope; (b) the separation of Log-Persistence from Mnemosyne; (c) the satellite receipt ACK and backpressure semantics; (d) whether the remaining technical non-decisions are correctly recorded; and (e) whether this DRP is suitable as input to a future governed architecture ADD/reconciliation activity.
