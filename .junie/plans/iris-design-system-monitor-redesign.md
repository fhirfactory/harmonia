---
sessionId: session-260919-075939-56yf
---

# Requirements

### Overview & Goals

This plan establishes **`iris-befe`** as the shared, authoritative presentation and design-system foundation for the Harmonia Iris UI family, and redesigns **`iris-monitor`** (canonically `iris-console` in the repository) as the first substantial consumer and proving ground for that design system.

The core objectives are:
1. **Establish `iris-befe` Design System Foundation**: Centralize UI primitives (via PrimeVue 4), light theme definition, design tokens, application shell, navigation conventions, status semantics, and layout structures within `iris-befe/frontend`. Ensure `iris-befe` remains strictly domain-neutral with zero knowledge of Petasos, Ponos, Artemis, or clinical data models.
2. **Transform `iris-monitor` UI/UX**: Overhaul the operational monitor from a dark, card-heavy, component-dump dashboard into a light, highly readable, information-dense, and restrained operational console structured around Harmonia's authoritative architectural hierarchy.
3. **Preserve Operational Functionality & Contracts**: Retain all existing backend telemetry integrations (`/api/operations/*` on port 8090) while dramatically elevating information hierarchy, English-alongside-Greek descriptions, and architecture-to-runtime drill-down.
4. **Prepare Iris Family Consistency**: Lay the exact architectural foundation that allows `iris-clinical` and `iris-administration` to subsequently adopt the same shell, theme, and design tokens without architectural disruption.

---

### Target Users & Operational Role

`iris-monitor` is specifically engineered for:
- Harmonia integration operations engineers;
- Application support and platform administrators;
- Tier 2/3 technical support and on-call engineers;
- Developers diagnosing end-to-end operational behavior and message delivery.

`iris-monitor` is **not** a clinical record explorer (which belongs to `iris-clinical`) nor a provider self-service workbench (which belongs to `iris-administration`).

---

### Scope Boundaries

#### In Scope
- **Shared Iris Design System in `iris-befe`**:
  - Packaging `iris-befe/frontend` as an internal workspace source package consumed by `iris-monitor` via Vite path alias and file dependency.
  - PrimeVue 4 integration (`primevue@^4.3.0`, `@primevue/themes`) using a custom Aura Light preset defined via `definePreset`.
  - Centralized design token library (`tokens.css` / CSS custom properties) governing typography, compact desktop spacing, high-contrast text, surfaces, borders, elevation, and status colors.
  - Shared application shell components: `IrisApplicationShell`, `IrisHeader`, `IrisPrimaryNavigation`, `IrisUserMenu`, `IrisBreadcrumbs`, `IrisPage`, `IrisPageHeader`, `IrisToolbar`.
  - Shared presentation components: `IrisStatus`, `IrisSubsystemIdentity`, `IrisSection`, `IrisHierarchy`, `IrisDataTable`, `IrisEmptyState`, `IrisLoadingState`, `IrisErrorState`.
  - Unidirectional dependency enforcement (`iris-monitor -> iris-befe`, with zero reverse dependencies).
- **`iris-monitor` UI/UX Redesign**:
  - Light-by-default visual appearance with dark, high-contrast text and subtle neutral surfaces.
  - Persistent application header with environment, cluster, health state, active alert counts, clock, and refresh triggers.
  - Obvious primary navigation across the top: `Overview`, `Subsystems`, `Interfaces`, `Work`, `Messages`, `Health`.
  - Consistent page-level action bar using `IrisToolbar` across all views.
  - Subsystems view as the primary proving ground using a Split-Tree Master-Detail layout:
    - Left tree representing Harmonia's 6 architectural areas (Integration & Transport, Execution & Processing, Information & State, Security, Collaboration, Presentation) and child subsystems.
    - Right detail pane providing subsystem identity, actions, and tabbed inspection of Runtime Instances, Operational Health & Dependencies, and Telemetry Statistics.
    - Subordination of middleware (ActiveMQ Artemis, Infinispan, PostgreSQL, Synapse) under their consuming Harmonia capabilities (Petasos, Mneme, Mnemosyne, Agora).
    - English descriptions alongside Greek mythological names across all views.
  - Overview perspective answering "Is Harmonia healthy?", "Is work moving?", and "Are messages backing up?" with compact health strips and alert ribbons.
  - Messages perspective organizing ActiveMQ Artemis broker cluster and queue runtime details under Petasos messaging.
  - Work perspective organizing workflow execution under Energeia (Ponos, Ergon, Praxis, Pragma).
  - Interfaces perspective displaying Pylai MLLP and FHIR gateways in a compact operational table.
  - Formal tracking of backend telemetry gaps via `IRIS-API-GAP-<number>`.
- **Documentation & Verification**:
  - Design system and monitor architecture documentation in Markdown and LaTeX.
  - Component unit tests and accessibility tests.
  - Future migration roadmap assessment for `iris-clinical` and `iris-administration`.

#### Out of Scope
- Direct browser connections to backend databases, Infinispan grids, Artemis brokers, or Kubernetes APIs (preserves Invariant 3).
- Exposing clinical FHIR payloads, HL7 v2 messages, or patient PHI in operational logs/views (preserves Invariant 7).
- Full visual redesign of `iris-clinical` or `iris-administration` during this task (assessment only).
- Modifying backend JAX-RS endpoint contracts or data models in `iris-befe` Java code unless necessary for gap resolution.

---

### User Stories

- **US-1 (Operator Health Triage)**: As an operations engineer, I want an obvious, light-themed top navigation and an architecture-driven Subsystems view with clear English subtitles so that I can immediately identify whether any Harmonia subsystem is degraded without deciphering Greek names or searching through flat component lists.
- **US-2 (Architecture-to-Runtime Drill-Down)**: As a platform engineer, I want to navigate progressively from an architectural area (e.g. *Integration & Transport*) to a subsystem (*Petasos*) and down into runtime middleware (*ActiveMQ Artemis broker nodes & queues*) so that I can diagnose queue backlogs without confusing infrastructure with system architecture.
- **US-3 (Consistent Action & Filtering)**: As an integration support specialist, I want a standardized page action bar (`IrisToolbar`) across all pages with obvious refresh, filter, search, and export actions so that page interactions are predictable and muscle memory is preserved across the application.
- **US-4 (Accessible Status Recognition)**: As an operator, I want status indicators to always pair clear text (`Healthy`, `Degraded`, `Unavailable`, `Unknown`) with distinct symbols and restrained colors so that status is accessible and never conveyed through color alone.
- **US-5 (Cross-Application Cohesion)**: As a Harmonia UI user transitioning between Clinical, Administration, and Monitor, I want a shared header, navigation aesthetic, typography, and visual language so that all three applications feel like cohesive members of the Iris family.

---

### Functional Requirements

1. **Iris Application Shell (`IrisApplicationShell`)**:
   - Provide a persistent top-level layout accepting application identity (`application="Monitor"`), navigation model, user profile slot, global health status, and page body.
   - Support nested route rendering via `<router-view>` within a structured, responsive container.
2. **Top Navigation (`IrisPrimaryNavigation`)**:
   - Render persistent, high-contrast top-level links across the header bar: `Overview`, `Subsystems`, `Interfaces`, `Work`, `Messages`, `Health`.
   - Display dynamic operational badges (e.g. degraded subsystem count, active warning/critical alerts) on relevant navigation items.
   - Support keyboard navigation and accessible ARIA attributes (`aria-current="page"`).
3. **Harmonia Subsystem Hierarchy**:
   - Categorize all Harmonia components into the authoritative 6 architectural areas:
     - **Integration & Transport**: Pylai (Interface Gateways), Petasos (Messaging & Transport, with ActiveMQ Artemis runtime).
     - **Execution & Processing**: Energeia (Workflow Engine), Ponos (Task Processing Engine), Ergon (Task Definitions), Praxis (Workflow Sequences), Pragma (Task Executions).
     - **Information & State**: Calliope (Canonical Models), Mneme (Operational Cache, with Infinispan runtime), Mnemosyne (Durable State, with PostgreSQL runtime).
     - **Security**: Themis (Security & Policy Enforcement).
     - **Collaboration**: Agora (Collaboration Gateway, with Synapse Matrix Homeserver runtime).
     - **Presentation**: Iris (Presentation Services, BEFE Gateway & SPAs).
   - Display mandatory English architectural descriptions beneath or alongside every Greek name.
4. **Subsystems Page Interaction (Split-Tree Master-Detail)**:
   - Left tree panel: expandable/collapsible hierarchy displaying architectural areas, subsystems, English subtitles, and accessible status badges.
   - Right detail panel: displays selected subsystem header (`IrisSubsystemIdentity`), action toolbar (`IrisToolbar`), and tabbed detail views (`Runtime Instances`, `Operational Health & Dependencies`, `Telemetry Statistics`).
   - Allow deep linking to specific subsystems via route parameters (`/subsystems/:id`).
5. **Page Action Model (`IrisToolbar`)**:
   - Standardize placement of primary actions (Refresh, Filter, Search, Time Window selectors `15m|1h|6h|24h`, Export, Actions) across all views.
   - Support button grouping, disabled states during polling/fetching, and loading spinners.
6. **Breadcrumbs Navigation (`IrisBreadcrumbs`)**:
   - Dynamically render hierarchical location path based on architectural depth (e.g. `Harmonia / Execution & Processing / Energeia / Ponos`).
7. **Status Language & Presentation (`IrisStatus`)**:
   - Standardize status states: `HEALTHY`, `DEGRADED`, `UNAVAILABLE`, `UNKNOWN`.
   - Render textual status alongside accessible iconography (e.g., `● Healthy`, `▲ Degraded`, `✖ Unavailable`, `? Unknown`).
   - Support `stale` telemetry indicators when polling intervals lapse.
8. **Operational Perspectives (Overview, Interfaces, Work, Messages, Health)**:
   - **Overview**: High-level platform health strip, active alert ribbons, work execution counters, and direct links to degraded subsystems.
   - **Interfaces**: Compact operational table of Pylai MLLP and FHIR gateways (name, direction, protocol, port, status, throughput, error rate).
   - **Work**: Energeia/Ponos task processing rates, active Praxis executions, completed/failed tasks, and Pragma instance inspection.
   - **Messages**: Petasos messaging overview with ActiveMQ Artemis cluster node health, queue depth, consumers, producers, enqueue/dequeue rate, and DLQ depth.
   - **Health**: Cross-subsystem operational health matrix, dependency health latencies, and active alert management with operator guidance.

---

### Non-Functional Requirements

- **Visual Readability & Contrast**: Light page backgrounds (`#f8fafc` / `#ffffff`), dark high-contrast primary text (`#0f172a`), muted secondary text (`#475569`), restrained border lines (`#e2e8f0`), and WCAG 2.1 AA compliant contrast ratios (>= 4.5:1 for normal text, >= 3:1 for large text and UI components).
- **Information Density**: Desktop-optimized spacing, compact table row heights (36-40px), controlled padding, and zero gratuitous whitespace or giant empty cards.
- **Performance**: Sub-50ms client-side route transitions, instant HMR during local development, efficient reactive rendering of telemetry charts, and bundle size under 450 kB (gzip < 120 kB).
- **Security & Zero-PHI**: Themis default-deny RBAC evaluation preserved across all API calls; zero clinical payloads, patient identifiers, or server credentials rendered in browser views or console logs.
- **Architectural Decoupling**: Complete separation between presentation components and database drivers (Invariant 3); zero dependencies on Paradeigma simulation code (Invariant 1).
- **Accessibility**: Full keyboard navigability (Tab, Enter, Space, Arrows), visible focus rings (`focus-visible: ring-2 ring-sky-500`), semantic HTML5 landmarks (`<header>`, `<nav>`, `<main>`, `<section>`, `<footer>`), and ARIA attributes for dynamic status and drawers.

# Technical Design

### Current Frontend Baseline Report

Investigation of `iris/` and the existing SPAs reveals the current frontend baseline:

| Attribute | Current Value | Notes |
| :--- | :--- | :--- |
| **Vue Version** | `3.4.21` | Consistent across `iris-clinical`, `iris-console`, and `iris-administration`. |
| **Build & Tooling** | Vite `^5.2.0`, `vue-tsc` `^2.0.7`, TypeScript `^5.4.3` | Bundled with `frontend-maven-plugin` 1.15.0 (Node `v20.12.2`, npm `10.5.0`). |
| **UI Framework** | Custom handcrafted CSS / Tailwind-like utility classes | No shared component library; duplicate CSS across all three SPAs. |
| **Icon Framework** | `lucide-vue-next` `^0.363.0` | Used across all SPAs for SVG icons. |
| **Theming Mechanism** | Hardcoded Dark Theme (`--bg-main: #0b0f19`) | Hardcoded in `style.css` across all three apps; no dynamic theme engine. |
| **State Management** | Pinia `^2.1.7` | Modular stores (`operationsStore`, `queueStore`, `workflowStore`, `eventStore`). |
| **Routing** | Vue Router `^4.3.0` | Canonical 5-perspective operational routes in `iris-console`. |
| **Testing** | Vitest `^2.1.9`, `@vue/test-utils` `^2.5.1`, `jsdom` `^25.0.1` | 12 test suites, 84 passing unit/component tests in `iris-console`. |
| **`iris-befe` Role** | Pure WildFly Jakarta EE 10 backend WAR | Exposes `/api/operations/*` on port 8090; contains NO frontend package or components currently. |

---

### Architectural Decisions

#### Decision 1: Design System Packaging in `iris-befe`
- **Choice**: **Workspace Source Package (`iris-befe/frontend`)**
- **Rationale**: Organizing the Iris Design System within `iris/iris-befe/frontend` with its own `package.json` (`@harmonia/iris-befe`) and linking it to `iris-console` (and subsequent Iris SPAs) via Vite path aliases and `file:../iris-befe/frontend` dependencies guarantees instantaneous HMR during development without requiring an intermediate build step or separate release cycle, while honoring the authoritative hierarchy `iris-monitor -> iris-befe`.

#### Decision 2: PrimeVue Theming Strategy
- **Choice**: **Aura Token Preset via `definePreset`**
- **Rationale**: PrimeVue 4's `@primevue/themes` architecture allows defining a tailored Iris light theme preset extending Aura. This centralizes design tokens for typography, restrained desktop spacing, neutral surfaces, subtle borders, and semantic status colors directly into PrimeVue's token compiler, avoiding brittle ad-hoc CSS overrides while preserving full accessibility and keyboard navigation.

#### Decision 3: Subsystems Page Interaction Model
- **Choice**: **Split Tree and Detail Master-Detail Layout**
- **Rationale**: A persistent architectural tree on the left presenting Harmonia's 6 top-level areas combined with a structured right-hand detail pane (displaying subsystem identity, toolbar actions, and tabbed inspection for Runtime Instances, Operational Health & Dependencies, and Telemetry Statistics) provides superior desktop operational efficiency compared to deeply nested accordions or modal drawers.

---

### Iris Design System (`iris-befe`) Specification

The Iris Design System establishes the visual language, shared UI conventions, and application shell for all Harmonia presentation services:

```
iris/iris-befe/frontend/
├── package.json
├── tsconfig.json
├── src/
│   ├── index.ts                     # Main entrypoint exporting components, theme, tokens
│   ├── theme/
│   │   ├── index.ts                 # PrimeVue 4 Iris theme plugin configuration
│   │   ├── irisPreset.ts            # Aura-derived light theme preset via definePreset
│   │   └── tokens.css               # Centralized Iris CSS variables and tokens
│   ├── components/
│   │   ├── shell/
│   │   │   ├── IrisApplicationShell.vue
│   │   │   ├── IrisHeader.vue
│   │   │   ├── IrisPrimaryNavigation.vue
│   │   │   ├── IrisUserMenu.vue
│   │   │   ├── IrisBreadcrumbs.vue
│   │   │   ├── IrisPage.vue
│   │   │   ├── IrisPageHeader.vue
│   │   │   └── IrisToolbar.vue
│   │   └── presentation/
│   │       ├── IrisStatus.vue
│   │       ├── IrisSubsystemIdentity.vue
│   │       ├── IrisSection.vue
│   │       ├── IrisHierarchy.vue
│   │       ├── IrisDataTable.vue
│   │       ├── IrisEmptyState.vue
│   │       ├── IrisLoadingState.vue
│   │       └── IrisErrorState.vue
│   └── types/
│       └── index.ts                 # Shared UI models (NavPerspective, StatusState, SubsystemNode)
```

#### Design-System Provenance Matrix

The design system enforces strict three-tier provenance:

| UI Element | PrimeVue Primitive | Iris-BEFE Convention | Iris-Monitor Operational Semantics |
| :--- | :--- | :--- | :--- |
| **Application Shell** | — | `IrisApplicationShell`, `IrisHeader` | Harmonia cluster, environment, and operations backend telemetry |
| **Top Navigation** | `Menubar` | `IrisPrimaryNavigation` | 6 Operational Perspectives (`Overview`, `Subsystems`, `Interfaces`, `Work`, `Messages`, `Health`) |
| **Breadcrumbs** | `Breadcrumb` | `IrisBreadcrumbs` | Harmonia architectural drill-down (`Area / Subsystem / Component / Runtime`) |
| **Page Action Bar** | `Toolbar`, `Button`, `Select` | `IrisToolbar` | Refresh rate, time window (`15m|1h|6h|24h`), filter/search, retry, export |
| **Status Indicators** | `Tag`, `Badge` | `IrisStatus` | Operational states: `HEALTHY`, `DEGRADED`, `UNAVAILABLE`, `UNKNOWN`, with staleness flag |
| **Subsystem Identity** | — | `IrisSubsystemIdentity` | Greek mythological name + English architectural description + architectural icon slot |
| **Architectural Hierarchy** | `Tree`, `TreeTable` | `IrisHierarchy` | Harmonia 6 Architectural Areas and Subsystem Inventory tree model |
| **Operational Tables** | `DataTable`, `Column` | `IrisDataTable` | Pylai gateways, Petasos queues, Ponos workers, Pragma tasks, active alerts |
| **Empty / Error States** | `Message` | `IrisEmptyState`, `IrisErrorState` | Telemetry timeouts, empty search results, absent queues |

---

### Design Tokens & Iris Light Theme

The Iris Design System establishes explicit design tokens rooted in a light, high-readability visual direction:

```css
:root {
  /* Surfaces & Backgrounds */
  --iris-bg-page: #f8fafc;        /* Slate 50 - clean, glare-free light page background */
  --iris-bg-surface: #ffffff;     /* Pure white for panels, cards, and data surfaces */
  --iris-bg-subtle: #f1f5f9;      /* Slate 100 - navigation backgrounds, table headers */
  --iris-bg-hover: #e2e8f0;       /* Slate 200 - subtle interactive hover state */
  --iris-bg-selected: #e0f2fe;    /* Sky 100 - active selection surface */

  /* Borders & Dividers */
  --iris-border-default: #e2e8f0; /* Slate 200 - clean, restrained dividers */
  --iris-border-subtle: #cbd5e1;  /* Slate 300 - panel outlines */
  --iris-border-focus: #0284c7;   /* Sky 600 - accessible high-contrast focus ring */

  /* Typography & Text Contrast */
  --iris-font-sans: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  --iris-font-mono: 'JetBrains Mono', monospace;
  --iris-text-primary: #0f172a;   /* Slate 900 - dark, high-contrast readable text (13:1) */
  --iris-text-secondary: #475569; /* Slate 600 - readable metadata and subtitles (5.5:1) */
  --iris-text-muted: #64748b;     /* Slate 500 - auxiliary captions (4.6:1 WCAG AA) */

  /* Semantic Status Semantics */
  --iris-status-healthy-bg: #ecfdf5;   /* Emerald 50 */
  --iris-status-healthy-text: #065f46; /* Emerald 800 */
  --iris-status-healthy-border: #a7f3d0;
  --iris-status-degraded-bg: #fffbeb;  /* Amber 50 */
  --iris-status-degraded-text: #92400e;/* Amber 800 */
  --iris-status-degraded-border: #fde68a;
  --iris-status-unavailable-bg: #fef2f2;/* Rose 50 */
  --iris-status-unavailable-text: #991b1b;/* Rose 800 */
  --iris-status-unavailable-border: #fecaca;
  --iris-status-unknown-bg: #f8fafc;   /* Slate 50 */
  --iris-status-unknown-text: #475569; /* Slate 600 */
  --iris-status-unknown-border: #cbd5e1;

  /* Desktop Density Sizing */
  --iris-spacing-unit: 0.25rem;
  --iris-header-height: 48px;
  --iris-nav-height: 40px;
  --iris-toolbar-height: 44px;
  --iris-table-row-height: 38px;
  --iris-border-radius: 4px;      /* Restrained, professional corner rounding */
  --iris-shadow-subtle: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}
```

---

### Harmonia Subsystem Hierarchy & Information Architecture

The information architecture strictly reflects Harmonia's authoritative 6 architectural areas, positioning infrastructure and middleware underneath the consuming subsystems:

```
HARMONIA PLATFORM
├── 1. INTEGRATION & TRANSPORT
│   ├── Pylai (Interface Gateways)
│   │   ├── MLLP Inbound Gateway (:2575 / :8084)
│   │   ├── MLLP Outbound Gateway — HIS Instance (:8087)
│   │   ├── MLLP Outbound Gateway — LIS Instance (:8088)
���   │   └── FHIR Provider Registry Gateway (:8089)
│   └── Petasos (Messaging & Transport)
│       └── ActiveMQ Artemis Broker Cluster (Port 61616)
│           ├── Brokers: Primary Node 1, Replica Node 2
│           └── Queues: petasos.queue.*, DLQ, Expiry
├── 2. EXECUTION & PROCESSING
│   └── Energeia (Workflow & Activity Execution)
│       ├── Ponos (Task Processing Engine & Workers)
│       ├── Ergon (Task / Work Unit Activities & Transformers)
│       ├── Praxis (Workflow Task Sequences & Definitions)
│       └── Pragma (Task Instances & Runtime Checkpoints)
├── 3. INFORMATION & STATE
│   ├── Calliope (Canonical Model & Schema Library)
│   ├── Mneme (Operational In-Memory Cache Grid)
│   │   └── Infinispan Clustered Nodes (Ports 11222, 11223)
│   └── Mnemosyne (Durable Relational Persistence)
│       ├── Mnemosyne Clinical JPA (FHIR R5 Server, Ports 8081 / 8082)
│       ├── Mnemosyne Operations JPA (Operations Server, Ports 8085 / 8086)
│       └── PostgreSQL Authoritative Databases (Clinical & Operations)
├── 4. SECURITY & POLICY
│   └── Themis (Policy & Authorization Subsystem)
│       ├── Default-Deny Evaluation Engine
│       ├── Role-to-Authority RBAC Mappings
│       └── Non-PHI Decision Auditing
├── 5. COLLABORATION
│   └── Agora (Collaboration & Matrix Gateway)
│       ├── AS Protocol Bridge (:8095)
│       └── Synapse Matrix Homeserver (:8008)
└── 6. PRESENTATION
    └── Iris (Presentation Services)
        ├── Iris BEFE Gateway (:8080 Clinical, :8090 Operations)
        ├── Iris Clinical SPA (:3000)
        ├── Iris Monitor SPA (:3001)
        └── Iris Administration SPA (:3002)
```

---

### ASCII Wireframes

#### 1. Desktop Application Shell & Top Navigation

```
+---------------------------------------------------------------------------------------------------------+
| HARMONIA / IRIS MONITOR             Env: PROD / microk8s-01   Cluster: harmonia-01    Health: ● Healthy  |
+---------------------------------------------------------------------------------------------------------+
| [Overview]  [Subsystems]  [Interfaces]  [Work]  [Messages]  [Health]          | Live: 14:32:05 | [Refresh]|
+---------------------------------------------------------------------------------------------------------+
| Harmonia / Integration & Transport / Petasos                           [Filter: All] [Window: 1h] [Export] |
+---------------------------------------------------------------------------------------------------------+
|                                                                                                         |
|                                       PERSPECTIVE CONTENT REGION                                        |
|                                                                                                         |
+---------------------------------------------------------------------------------------------------------+
| Harmonia Operations Monitor • BEFE :8090 • Themis Default-Deny RBAC • Infinispan 15 • ActiveMQ Artemis  |
+---------------------------------------------------------------------------------------------------------+
```

#### 2. Subsystems View (Split-Tree Master-Detail)

```
+---------------------------------------------------------------------------------------------------------+
| ARCHITECTURAL INVENTORY (TREE)     | SELECTED SUBSYSTEM: PETASOS                                         |
+------------------------------------+--------------------------------------------------------------------+
| Search subsystems...            [Q]| PETASOS                                       ● Healthy            |
|                                    | Messaging & Transport                         Version: 1.0.0       |
| v Integration & Transport          +--------------------------------------------------------------------+
|   * Pylai                          | Actions: [Refresh Telemetry]  [Inspect Queues]  [Broker Topology]   |
|     Interface Gateways   ● Healthy +--------------------------------------------------------------------+
|   * Petasos                        | [Runtime Instances (2)]  [Health & Dependencies]  [Statistics (1h)] |
|     Messaging & Transport● Healthy +--------------------------------------------------------------------+
|                                    | INSTANCE ID    ROLE       POD / HOST       PORT   STATE    UPTIME  |
| > Execution & Processing  ● Healthy| petasos-node-1 Primary    artemis-node1    61616  ● RUN    4d 12h  |
|   Energeia (Ponos, Praxis)         | petasos-node-2 Backup     artemis-node2    61616  ● RUN    4d 12h  |
|                                    +--------------------------------------------------------------------+
| > Information & State     ● Healthy| MIDDLEWARE RUNTIME: Apache ActiveMQ Artemis Cluster                |
|   Calliope, Mneme, Mnemosyne       | - Ingress Queue Depth: 0 messages       - Dequeue Rate: 142 msg/s  |
|                                    | - Dead Letter Queue:  0 messages       - Active Consumers: 12      |
| > Security & Policy       ● Healthy+--------------------------------------------------------------------+
|   Themis                           | UPSTREAM / DOWNSTREAM DEPENDENCIES                                 |
|                                    | - Themis Security:     ● Healthy (2.1 ms)                          |
| > Collaboration           ▲ Degraded- Mneme Cache Grid:    ● Healthy (1.4 ms)                          |
|   Agora (Synapse: Unavailable)     | - Ponos Dispatch:      ● Healthy (0.8 ms)                          |
+------------------------------------+--------------------------------------------------------------------+
```

#### 3. Overview View (Concise Health Strips, Zero Giant Cards)

```
+---------------------------------------------------------------------------------------------------------+
| PLATFORM OVERVIEW                                                    Last refreshed: 14:32:05 [Refresh] |
+---------------------------------------------------------------------------------------------------------+
| PLATFORM STATUS: ● HEALTHY    | Subsystems: 9/9 Up | Active Queues: 18 | Executing Tasks: 4 | Alerts: 0 |
+---------------------------------------------------------------------------------------------------------+
| RECENT OPERATIONAL EXCEPTIONS / ALERTS                                                                  |
| All systems operating normally. Zero active critical or warning conditions detected in last 24h.       |
+---------------------------------------------------------------------------------------------------------+
| SUBSYSTEM SUMMARY BY ARCHITECTURAL AREA                                                                 |
| Area                     Subsystems Included        Status        Active Components    Throughput/Rates |
| Integration & Transport  Pylai, Petasos             ● Healthy     4 Gateways, 2 Brokers   184 msg/s     |
| Execution & Processing   Energeia (Ponos, Praxis)   ● Healthy     4 Workers, 6 Sequences   42 tasks/s   |
| Information & State      Calliope, Mneme, Mnemosyne ● Healthy     2 Caches, 2 DB Nodes     99.98% hit   |
| Security & Policy        Themis                     ● Healthy     1 Policy Engine         340 eval/s    |
| Collaboration            Agora                      ● Healthy     1 AS Bridge               0 msgs      |
| Presentation             Iris                       ● Healthy     1 BEFE, 3 SPAs           28 req/s     |
+---------------------------------------------------------------------------------------------------------+
```

#### 4. Messages View (Petasos-Centric with Artemis Subordination)

```
+---------------------------------------------------------------------------------------------------------+
| MESSAGES (PETASOS TRANSPORT)                             [Filter Queues...] [Include Empty: [x]] Refresh|
+---------------------------------------------------------------------------------------------------------+
| PETASOS QUEUE NAME               ADDRESS                 DEPTH   CONS   PROD   ENQ/s   DEQ/s   DLQ      |
| petasos.queue.pylai.mllp.in      pylai.mllp.in               0      2      1    24.2    24.2     0      |
| petasos.queue.ponos.dispatch     ponos.dispatch              2      4      2    18.0    17.8     0      |
| petasos.queue.mllp.outbound.his  mllp.outbound.his           0      2      1    12.4    12.4     0      |
| petasos.queue.mllp.outbound.lis  mllp.outbound.lis           0      2      1     8.1     8.1     0      |
| petasos.queue.agora.events       agora.events                0      1      1     0.0     0.0     0      |
| ActiveMQ.DLQ (Dead Letter)       ActiveMQ.DLQ                0      1      0     0.0     0.0     0      |
+---------------------------------------------------------------------------------------------------------+
```

---

### Component Classification

| Component | Current Location | Classification | Future Role / Destination |
| :--- | :--- | :--- | :--- |
| **`style.css` (dark theme)** | `iris-console/src/style.css` | **REPLACE** | Replaced by `@harmonia/iris-befe` Aura light preset and `tokens.css`. |
| **`NavigationTopBar.vue`** | `iris-console/src/components/common/` | **REPLACE** | Replaced by `IrisPrimaryNavigation.vue` from `iris-befe`. |
| **`GlobalOperationsHeader.vue`** | `iris-console/src/components/common/` | **REFACTOR** | Integrated into `IrisApplicationShell` / `IrisHeader` with light styling. |
| **`StatusBadge.vue`** | `iris-console/src/components/common/` | **MOVE TO IRIS-BEFE** | Promoted to domain-neutral `IrisStatus.vue` in `iris-befe`. |
| **`SvgTimeSeriesChart.vue`** | `iris-console/src/components/common/` | **RETAIN** | Preserved for reactive SVG sparklines; styled with Iris light theme tokens. |
| **`SubsystemSidebar.vue`** | `iris-console/src/components/subsystems/` | **REFACTOR** | Transformed into `SubsystemTreeNavigator.vue` structured by 6 Harmonia areas. |
| **`SubsystemHeader.vue`** | `iris-console/src/components/subsystems/` | **REFACTOR** | Replaced by `IrisSubsystemIdentity` with Greek and English typography. |
| **`InstanceTable.vue`** | `iris-console/src/components/subsystems/` | **REFACTOR** | Refactored using `IrisDataTable` with light theme and compact density. |
| **`InstanceDetailDrawer.vue`** | `iris-console/src/components/subsystems/` | **RETAIN** | Styled with Iris light theme and accessible drawer semantics. |
| **`HealthDependenciesPanel.vue`** | `iris-console/src/components/subsystems/` | **REFACTOR** | Styled with light surfaces, high-contrast latencies, and `IrisStatus`. |
| **`StatisticsPanel.vue`** | `iris-console/src/components/subsystems/` | **RETAIN** | Preserved with light background styling and time-window selector. |
| **`QueueTable.vue`** | `iris-console/src/components/queues/` | **REFACTOR** | Refactored with `IrisDataTable` and subordinated Artemis metadata. |
| **`WorkflowTable.vue`** | `iris-console/src/components/workflows/` | **REFACTOR** | Refactored to represent the Energeia Ponos/Ergon/Praxis hierarchy. |
| **`EventTimeline.vue`** | `iris-console/src/components/events/` | **REFACTOR** | Light theme timeline with high-contrast connecting stems and timestamps. |
| **`AlertTable.vue`** | `iris-console/src/components/alerts/` | **REFACTOR** | Restyled with accessible severity badges and operator guidance ribbons. |
| **`SubsystemsView.vue`** | `iris-console/src/views/` | **REFACTOR** | Rebuilt around the Split-Tree Master-Detail pattern. |
| **`QueuesView.vue`** | `iris-console/src/views/` | **REFACTOR** | Migrated to Messages perspective with Petasos-first organization. |
| **`WorkflowsView.vue`** | `iris-console/src/views/` | **REFACTOR** | Migrated to Work perspective with workflow sequence drill-down. |
| **`OperationsDashboardView.vue`** | `iris-console/src/views/` | **REPLACE** | Replaced by concise `OverviewView.vue` (eliminating giant dark cards). |
| **`operationsStore.ts`** | `iris-console/src/stores/` | **RETAIN** | Preserved backend polling and normalized telemetry state. |
| **`operationsClient.ts`** | `iris-console/src/api/` | **RETAIN** | Preserved REST client calling port 8090. |

---

### Architecture Diagram

```mermaid
graph TD
  subgraph Primitives [UI Primitives Layer]
    PV[PrimeVue 4 Core]
    PV_THEMES[@primevue/themes Aura Preset]
  end

  subgraph DesignSystem [Shared Iris Presentation Layer - iris-befe/frontend]
    TOKENS[Iris Design Tokens & Tokens.css]
    PRESET[Iris Aura Light Preset definePreset]
    SHELL[Application Shell: IrisApplicationShell, IrisHeader, IrisPrimaryNavigation, IrisToolbar]
    PRES[Presentation: IrisStatus, IrisSubsystemIdentity, IrisDataTable, IrisEmptyState]
    PV --> PRESET
    PV_THEMES --> PRESET
    TOKENS --> PRESET
    PRESET --> SHELL
    PRESET --> PRES
  end

  subgraph MonitorApp [Operational Proving Consumer - iris-monitor]
    MON_APP[App.vue with IrisApplicationShell]
    MON_SUBSYSTEMS[SubsystemsView: Split-Tree Architecture Navigator]
    MON_OVERVIEW[OverviewView: High-Density Health Summary]
    MON_MESSAGES[MessagesView: Petasos Transport & Artemis Runtime]
    MON_WORK[WorkView: Energeia / Ponos / Praxis Hierarchy]
    MON_INTERFACES[InterfacesView: Pylai Gateways Table]
    MON_EVENTS[EventsView: Cross-Subsystem Event Flowchart]
    MON_ALERTS[AlertsView: Severity Triage & Operator Guidance]
    
    SHELL --> MON_APP
    PRES --> MON_SUBSYSTEMS
    PRES --> MON_OVERVIEW
    PRES --> MON_MESSAGES
    PRES --> MON_WORK
    PRES --> MON_INTERFACES
    PRES --> MON_EVENTS
    PRES --> MON_ALERTS
  end

  subgraph FutureApps [Future Iris Family Consumers]
    CLINICAL[iris-clinical SPA - Port 3000]
    ADMIN[iris-administration SPA - Port 3002]
    SHELL -.->|Planned Migration| CLINICAL
    SHELL -.->|Planned Migration| ADMIN
  end

  subgraph BackendTier [Authoritative Backend Tier]
    BEFE_API[Iris BEFE Operations REST API - Port 8090]
    MON_APP -->|REST / JSON| BEFE_API
  end
```

---

### Architectural Guardrails & Invariants Compliance

- **Invariant 3 (Iris Presentation Decoupling)**: Verified that `iris-befe/frontend` and `iris-console` import zero JPA, Hibernate, or PostgreSQL classes. All communication remains strictly over REST and Hot Rod.
- **Directional Dependency (`monitor -> befe`)**: Enforces strict unidirectional flow. `iris-befe/frontend` contains zero imports from `iris-console`, `iris-clinical`, or `iris-administration`, verified via build checks.
- **Invariant 7 (Zero-PHI Diagnostic Logging)**: No patient names, MRNs, clinical observations, or secrets are rendered or logged in the UI. Only technical correlation IDs, message IDs, and subsystem statuses are displayed.
- **Themis Default-Deny Security**: UI views do not bypass backend authorization; Themis security evaluation remains authoritative on `/api/operations/*`.

---

### Technical Risks & Mitigations

1. **Risk: PrimeVue 4 / Vite Resolution Conflicts**:
   - *Mitigation*: Configure explicit path aliases in `vite.config.ts` and `compilerOptions.paths` in `tsconfig.json` mapping `@harmonia/iris-befe` to `../iris-befe/frontend/src`.
2. **Risk: Regression of Existing Unit Tests in `iris-console`**:
   - *Mitigation*: Retain existing Pinia store state structures and mock contracts while updating component mounting wrappers in `@vue/test-utils` to provide PrimeVue and Iris design system stubs.
3. **Risk: Telemetry API Gaps for Pylai Gateways**:
   - *Mitigation*: Catalogue any missing operational metrics under formal `IRIS-API-GAP-<number>` entries and present clear status badges with available health indicators rather than faking data.

# Testing

### Validation Approach

The validation approach ensures that both the shared Iris Design System in `iris-befe` and the redesigned `iris-monitor` (`iris-console`) meet all architectural, functional, accessibility, and quality criteria without regressions:

1. **Component & Store Unit Testing (Vitest)**:
   - Validate that all shared components in `iris-befe/frontend` render correctly with expected props, slots, and events.
   - Validate that `iris-console` perspectives render accurately under healthy, degraded, and unavailable states.
   - Maintain 100% pass rate across the existing 12 test suites in `iris-console` (84 tests), updating component selectors and wrappers for PrimeVue/Iris components.
2. **Visual Contrast & Accessibility Verification**:
   - Assert contrast ratios on text, borders, and status badges against WCAG 2.1 AA benchmarks using automated testing tools (`axe-core`).
   - Validate that all statuses render textual descriptions alongside icons so color is never the sole carrier of meaning.
   - Verify keyboard focus traversal (`Tab`, `Shift+Tab`, `Enter`, `Escape`) across the application shell, top navigation, tree navigator, and detail drawers.
3. **Build & Bundle Verification**:
   - Verify clean TypeScript compilation (`vue-tsc --noEmit`) and Vite production builds across all three SPAs:
     - `iris-console`: `npm run build`
     - `iris-clinical`: `npm run build` (assert zero regression)
     - `iris-administration`: `npm run build` (assert zero regression)
4. **Architecture & Invariant Verification**:
   - Execute ArchUnit test suites in `paradeigma/paradeigma-test`:
     - `IrisDecouplingArchitectureTest`: Asserts zero JPA/Hibernate/PostgreSQL dependencies in Iris.
     - `ParadeigmaIsolationArchitectureTest`: Asserts zero production dependencies on simulation code.
     - `PackageLayeringArchitectureTest`: Asserts strict package dependency layering.

---

### Key Scenarios & Test Matrix

| Scenario ID | Perspective / Component | Test Flow & Verification | Expected Outcome |
| :--- | :--- | :--- | :--- |
| **SC-01** | `IrisApplicationShell` | Mount application with mock route and store summary. Verify header, top navigation, breadcrumbs, and footer render. | Full shell renders with correct environment, cluster, health state, and active navigation highlight. |
| **SC-02** | `IrisStatus` Badge | Mount badge with `HEALTHY`, `DEGRADED`, `UNAVAILABLE`, and `UNKNOWN` states, and toggle `stale=true`. | Accessible text renders alongside distinct icons; stale indicator appends `(Stale)`. |
| **SC-03** | Subsystems Split-Tree View | Mount `SubsystemsView.vue` with canonical 6 Harmonia areas and child subsystems. Click `Petasos`. | Left tree highlights Petasos; right pane loads Petasos identity, English description, instances, and telemetry. |
| **SC-04** | Subsystem Drill-Down | In Petasos right pane, switch tabs to `Health & Dependencies` and inspect ActiveMQ Artemis runtime. | Middleware details render subordinately under Petasos with queue depth and broker node status. |
| **SC-05** | Overview Health Triage | Mount `OverviewView.vue` with degraded subsystem (`Agora`) and critical alert. | Overview renders platform state as `DEGRADED`, highlights Agora ribbon, and displays alert count. |
| **SC-06** | Messages Queue Inspection | Mount `MessagesView.vue` with Petasos queues. Filter by queue name `pylai`. | Table filters in real-time; displays consumer count, enqueue/dequeue rate, and zero DLQ count. |
| **SC-07** | Work Execution View | Mount `WorkView.vue`. Verify Energeia execution rates, Praxis sequences, and Pragma checkpoints render. | Ponos worker status and workflow sequence list render with accurate task metrics. |
| **SC-08** | Page Toolbar Actions | Click `Refresh` on `IrisToolbar` in `SubsystemsView.vue`. | Refresh trigger dispatches fetch action; button indicates loading state and disables during request. |
| **SC-09** | Clinical & Admin Isolation | Run `npm run build` in `iris-clinical` and `iris-administration`. | Both SPAs build cleanly without compilation errors or dependency collisions. |

---

### API Gap Tracking Register

Where existing `iris-befe` backend APIs do not supply the complete desired operational telemetry, entries are recorded in the formal gap register:

- **`IRIS-API-GAP-001` (Live Interface Throughput Metrics)**:
  - *Desired Information*: Real-time messages/sec and error counters per Pylai gateway instance (`pylai-mllp-in`, `pylai-mllp-out-his`, `pylai-mllp-out-lis`).
  - *Architectural Source*: Pylai Camel metrics or Infinispan `modulestatus-cache`.
  - *Current Limitation*: `PylaiHealthProvider` returns empty time-series for `events_in` and `events_out` unless populated by live Camel MBean hooks.
  - *UI Handling*: Display gateway connectivity, listening ports (`2575`, `8084`, `8087`, `8088`), and health status; display placeholder "Telemetry initializing" for throughput micro-charts until active traffic occurs.
- **`IRIS-API-GAP-002` (Granular Synapse Matrix Metrics)**:
  - *Desired Information*: Active Matrix room count and AS transaction rate.
  - *Architectural Source*: Agora AS transaction bridge (`agora-service`).
  - *Current Limitation*: `AgoraHealthProvider` reports status based on Synapse connectivity probe without exposing historical room event counters.
  - *UI Handling*: Display Synapse connection status (`HEALTHY` / `UNAVAILABLE`) and AS bridge port (`8095`), noting event telemetry as pending live probe.

# Documentation & Future Roadmap

### Documentation Updates

1. **`iris-befe` Design System Specification (`docs/architecture/iris-design-system.md`)**:
   - Establish formal documentation detailing `iris-befe` as the authoritative Iris Presentation & Design System foundation.
   - Document the component catalogue (`IrisApplicationShell`, `IrisHeader`, `IrisPrimaryNavigation`, `IrisToolbar`, `IrisStatus`, `IrisSubsystemIdentity`, `IrisHierarchy`, `IrisDataTable`).
   - Document the Aura Light theme preset, design token contracts (`tokens.css`), typography scale, and responsive conventions.
   - Include the authoritative 3-tier architecture diagram: `PrimeVue -> iris-befe -> (Clinical, Monitor, Administration)`.
2. **Operations Console Reference (`docs/architecture/iris-operations-console.md`)**:
   - Document `iris-monitor` information architecture, 6 operational perspectives (`Overview`, `Subsystems`, `Interfaces`, `Work`, `Messages`, `Health`), and Split-Tree interaction model.
   - Record the authoritative Harmonia 6-area hierarchy and English description mapping.
   - Document middleware subordination rules (Artemis under Petasos, Infinispan under Mneme, PostgreSQL under Mnemosyne, Synapse under Agora).
   - Maintain the `IRIS-API-GAP` register.
3. **Formal LaTeX Reference Manual**:
   - Update `docs/latex/chapters/07-presentation-iris.tex` and `docs/latex/chapters/appendix-iris-user-interfaces.tex` reflecting the design system architecture, light theme baseline, and component contracts.
   - Verify error-free compilation of the complete publication-grade PDF using `make -C docs/latex pdf`.

---

### Future Migration Assessment: `iris-clinical` & `iris-administration`

While full migration of `iris-clinical` and `iris-administration` is out of scope for this task, the design system is engineered to enable seamless adoption in future iterations:

#### 1. `iris-clinical` Migration Assessment
- **Current State**: Handcrafted dark theme (`style.css`), manual `Navbar.vue` and `Sidebar.vue`, 13 FHIR resource views.
- **Immediately Reusable Design-System Components**:
  - `IrisApplicationShell` with `application="Clinical"`.
  - `IrisPrimaryNavigation` configured with clinical routes (`Dashboard`, `Patients`, `Organizations`, `Practitioners`, `Tasks`, etc.).
  - `IrisToolbar` for FHIR search, resource filtering, and creation triggers.
  - `IrisDataTable` for high-density FHIR resource tables.
  - `IrisStatus` for resource status and provenance verification.
- **Migration Effort**: Low-to-Medium. Existing Pinia stores (`facilityStore`, `personStore`, `practitionerStore`) and `fhirClient.ts` remain 100% unchanged. Only layout and view templates need updating to replace dark CSS with Iris components.

#### 2. `iris-administration` Migration Assessment
- **Current State**: Handcrafted dark theme, duplicate `StatusBadge.vue`, `Sidebar.vue`, `Topbar.vue`, 17 administration views for Provider Registry and Self-Service.
- **Immediately Reusable Design-System Components**:
  - `IrisApplicationShell` with `application="Administration"`.
  - `IrisPrimaryNavigation` with role-aware tabs (`My Details`, `My Requests`, `Provider Administration`, `Data Quality`, `Work Queue`).
  - `IrisToolbar` for administrative workflow approval, rejection, and filter operations.
  - `IrisStatus` directly replacing local `StatusBadge.vue`.
  - `IrisDataTable` replacing table markup across Provider and Organization admin views.
- **Migration Effort**: Medium. All Themis security stores (`securityStore`), client services (`providerRegistryClient`), and validation rules remain completely intact.

---

### Checkpoint Review Cadence

- **Checkpoint 1 (Current Stage)**: Design System & Architecture Review (Baseline analysis, PrimeVue compatibility, design proposal, wireframes, and delivery plan).
- **Checkpoint 2 (Following Stage 2)**: Working Subsystems Prototype Review (Validation of `iris-befe` foundations, Aura light preset, top navigation, toolbar, and Split-Tree Subsystems view before wider rollout).
- **Checkpoint 3 (Following Stage 4)**: Monitor Application Migration Review (All 6 perspectives migrated, telemetry verified, API gaps catalogued).
- **Checkpoint 4 (Following Stage 5)**: Architecture, Documentation & Final Sign-Off (Tests passing, LaTeX compiling, zero regressions across clinical/admin).

# Delivery Steps

### ✓ Step 1: Establish Iris Design System in iris-befe with PrimeVue 4 and Aura Light Preset
`iris-befe/frontend` contains the reusable Iris Design System library with PrimeVue 4, custom Aura light theme preset, design tokens, and domain-neutral UI/shell components.

- Initialize the `@harmonia/iris-befe` frontend package structure inside `iris/iris-befe/frontend` with `package.json`, TypeScript configuration, and Vite library/export definitions.
- Install and configure PrimeVue 4 (`primevue@^4.3.0`, `@primevue/themes`) with an authoritative Iris light theme preset created via `definePreset` extending the Aura theme.
- Define centralized Iris design tokens (`tokens.css` / CSS custom properties) governing typography, compact desktop spacing, high-contrast text, neutral surfaces, restrained borders, elevation, and accessible status colors.
- Implement domain-neutral application shell components: `IrisApplicationShell.vue`, `IrisHeader.vue`, `IrisPrimaryNavigation.vue`, `IrisUserMenu.vue`, `IrisBreadcrumbs.vue`, `IrisPage.vue`, `IrisPageHeader.vue`, and `IrisToolbar.vue`.
- Implement shared presentation and status components: `IrisStatus.vue` (accessible icon + text status badges), `IrisSubsystemIdentity.vue` (Greek mythology name + English architectural description + icon slot), `IrisSection.vue`, `IrisDataTable.vue`, `IrisEmptyState.vue`, `IrisLoadingState.vue`, and `IrisErrorState.vue`.
- Establish Vitest unit tests in `iris-befe/frontend` verifying shell slot rendering, status badge accessibility, token propagation, and toolbar action dispatching.
- Configure path mapping and package dependency in `iris/iris-console` (and prepare for `iris-clinical` / `iris-administration`) with live Vite HMR support.

### ✓ Step 2: Implement Proving Subsystems Prototype in iris-monitor with Split-Tree Architecture
The Subsystems page in `iris-monitor` (`iris-console`) is redesigned as the approved proving ground, featuring the Iris light theme, top navigation, hierarchical split-tree architecture navigation, English descriptions, and runtime instance detail tabs.

- Configure `iris/iris-console` to import and consume `@harmonia/iris-befe` shell, theme, and presentation components.
- Refactor `App.vue` in `iris-console` to wrap application views within `<IrisApplicationShell>` providing persistent Harmonia identity, global health indicators, and top-level perspective navigation.
- Implement the primary Subsystems view (`SubsystemsView.vue`) using the Split-Tree Master-Detail pattern:
  - Left pane: `SubsystemTreeNavigator.vue` rendering the 6 authoritative Harmonia architectural areas (Integration & Transport, Execution & Processing, Information & State, Security, Collaboration, Presentation) with child subsystems, English descriptions, and status badges.
  - Right pane: Master-detail presentation providing subsystem identity (`IrisSubsystemIdentity`), top-level action toolbar (`IrisToolbar` with Refresh and Filter), and tabbed detail views (`Runtime Instances`, `Operational Health & Dependencies`, and `Telemetry Statistics`).
- Subordinate middleware components (ActiveMQ Artemis, Infinispan, PostgreSQL, Synapse) under their consuming Harmonia subsystems (Petasos, Mneme, Mnemosyne, Agora).
- Integrate with existing `iris-befe` Operations REST API endpoints (`/api/operations/subsystems`, `/api/operations/subsystems/{id}/instances`, `/api/operations/subsystems/{id}/health`, `/api/operations/subsystems/{id}/statistics`).
- Update existing vitest unit tests (`subsystemPerspective.spec.ts`, `instanceDrawer.spec.ts`, `statusBadge.spec.ts`, `navigation.spec.ts`) to validate the new component hierarchy and interaction flows.

### ✓ Step 3: Migrate Overview, Messages, and Work Operational Perspectives to the Design System
The Overview, Messages (Petasos/Artemis queues), and Work (Energeia/Ponos/Praxis workflows) perspectives are fully migrated onto the Iris light theme, shell, and consistent toolbar/table conventions.

- Redesign `OverviewView.vue` as a high-density, concise operational summary page answering "Is Harmonia healthy?", "Is work moving?", and "Are messages backing up?", eliminating giant KPI cards in favor of compact health strips, active alert summary ribbons, and direct subsystem links.
- Migrate `QueuesView.vue` to `MessagesView.vue` structuring messaging around Petasos first, with ActiveMQ Artemis broker cluster and queue runtime details (depth, consumers, enqueue/dequeue rate, DLQ) exposed subordinately beneath Petasos.
- Migrate `WorkflowsView.vue` to `WorkView.vue` organizing execution around the Harmonia workflow model (Energeia, Ponos, Ergon, Praxis, Pragma) with execution rate metrics and drill-down into concrete Pragma instances.
- Standardize all views with `IrisToolbar` (Refresh, Filter, Search, Action buttons) and `IrisDataTable` with restrained desktop row density and accessible sorting/filtering.
- Update Pinia stores (`operationsStore.ts`, `queueStore.ts`, `workflowStore.ts`) and Vitest specifications (`queuesPerspective.spec.ts`, `workflowsPerspective.spec.ts`) to ensure zero functional regressions.

### ✓ Step 4: Migrate Interfaces, Events, and Alerts Perspectives with API Gap Register
All remaining operational pages (Interfaces, Events diagnostic timeline, and Alerts) are migrated to the Iris Design System, and any telemetry limitations are catalogued in the formal API gap register.

- Implement `InterfacesView.vue` providing a compact operational table of Pylai inbound/outbound gateways (MLLP Inbound, MLLP Outbound HIS/LIS, FHIR Provider Registry) showing direction, protocol, port, status, throughput, and error rates.
- Migrate `EventsView.vue` to the Iris light theme, utilizing `IrisToolbar` for diagnostic filtering (correlation ID, causation ID, message ID, subsystem, event type) and rendering an accessible, high-contrast operational timeline.
- Migrate `AlertsView.vue` using `IrisToolbar`, severity filter tabs (Critical, Warning, Information), status toggles (Active, Acknowledged, Resolved), and structured operator guidance panels.
- Identify and formalize any backend telemetry limitations as `IRIS-API-GAP-<number>` entries in the architecture documentation (e.g. per-interface live throughput counters or granular gateway restart history).
- Run and update full test suite across all 12 console test suites (`vitest run`) ensuring all unit and component tests pass.

### ✓ Step 5: Enforce Architecture Invariants, Update Documentation, and Plan Future Migrations
Frontend architecture boundaries are verified, comprehensive Markdown and LaTeX documentation is published, and a detailed migration roadmap for `iris-clinical` and `iris-administration` is completed.

- Verify architectural invariants and package layering using ArchUnit tests (`ParadeigmaIsolationArchitectureTest`, `IrisDecouplingArchitectureTest`, `PackageLayeringArchitectureTest`) ensuring zero JPA/DB imports and zero production Paradeigma dependencies.
- Enforce strict unidirectional frontend dependency (`iris-monitor -> iris-befe`, `iris-clinical -> iris-befe`, `iris-administration -> iris-befe`) and assert zero reverse dependencies from `iris-befe` into application SPAs.
- Author comprehensive documentation for `iris-befe` as the shared Iris Presentation & Design System foundation in `docs/architecture/iris-design-system.md` and update `docs/architecture/iris-operations-console.md` and `docs/concepts/iris.md`.
- Update formal LaTeX technical reference chapters (`docs/latex/chapters/07-presentation-iris.tex`, `docs/latex/chapters/appendix-iris-user-interfaces.tex`) and compile the LaTeX manual (`make -C docs/latex pdf`).
- Produce a detailed future migration assessment for `iris-clinical` and `iris-administration` detailing reusable components, page shell migration paths, and anticipated effort.
- Execute full Maven build across Iris modules (`mvn clean test -pl iris/iris-befe,iris/iris-console`).