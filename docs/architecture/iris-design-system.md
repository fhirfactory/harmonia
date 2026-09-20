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

# Iris Design System & Presentation Architecture (`@harmonia/iris-befe`)

## 1. Executive Summary & Architectural Rationale

The **Iris Design System** establishes `iris-befe/frontend` as the shared, authoritative presentation and design-system foundation for all Harmonia Iris Single Page Applications (SPAs). It centralizes UI primitives, theme definitions, design tokens, application shell conventions, status semantics, and high-density desktop layout structures within an internal, domain-neutral workspace package (`@harmonia/iris-befe`).

Prior to this architecture, Harmonia's three presentation SPAs (`iris-clinical`, `iris-console`, and `iris-administration`) suffered from:
- Fragmented, handcrafted CSS utility classes and duplicated navigation components;
- An unyielding, dark-theme baseline (`--bg-main: #0b0f19`) ill-suited for dense operational triage and clinical reading;
- Absence of shared design tokens, resulting in divergent status palettes and visual inconsistency;
- Inverted dependency risks where UI components could inadvertently couple to backend domain entities.

The Iris Design System resolves these challenges through a strict three-tier architecture:
1. **UI Primitives Layer**: PrimeVue 4 (`primevue@^4.3.0`) and `@primevue/themes`.
2. **Shared Presentation Foundation Layer (`iris-befe/frontend`)**: Domain-neutral application shell, Aura Light preset (`definePreset`), design tokens (`tokens.css`), status components, and data tables.
3. **Application Layer (SPAs)**: Domain-specific consumers (`iris-console`, and future `iris-clinical`, `iris-administration`) consuming the design system via standard Vite path aliases and workspace file dependencies.

---

## 2. Unidirectional Dependency Architecture

The Iris presentation tier enforces strict unidirectional dependency flow across both build configurations and source imports:

```
+---------------------------------------------------------------------------------+
|                                 APPLICATION SPAS                                |
|   +--------------------+     +--------------------+     +-------------------+   |
|   |    iris-console    |     |   iris-clinical    |     |iris-administration|   |
|   | (Operations Portal)|     |  (Clinical Viewer) |     | (Provider Admin)  |   |
|   +---------+----------+     +---------+----------+     +---------+---------+   |
+-------------|--------------------------|--------------------------|-------------+
              |                          |                          |
              +--------------------------+--------------------------+
                                         |
                                         | imports (@harmonia/iris-befe)
                                         v
+---------------------------------------------------------------------------------+
|                    SHARED IRIS DESIGN SYSTEM (iris-befe/frontend)               |
|                                                                                 |
|   - Application Shell: IrisApplicationShell, IrisHeader, IrisPrimaryNavigation  |
|   - Action Model: IrisToolbar, IrisPage, IrisPageHeader, IrisBreadcrumbs        |
|   - Presentation: IrisStatus, IrisSubsystemIdentity, IrisDataTable, IrisSection |
|   - Diagnostics: IrisHierarchy, IrisEmptyState, IrisLoadingState, IrisErrorState|
|   - Theme & Tokens: Aura Light Preset (definePreset), tokens.css                |
|                                                                                 |
|   INVARIANT: STRICTLY DOMAIN-NEUTRAL (Zero Clinical/Ops Models, Zero DB Drivers)|
+----------------------------------------+----------------------------------------+
                                         |
                                         | built upon
                                         v
+---------------------------------------------------------------------------------+
|                      PRIMITIVES TIER (PrimeVue 4 & Aura)                         |
|   - primevue/config, @primevue/themes/aura, primevue/toolbar, primevue/datatable|
+---------------------------------------------------------------------------------+
```

### Architectural Guardrails
- **Zero Reverse Dependencies**: `iris-befe/frontend` MUST NEVER import from or declare package dependencies on `iris-console`, `iris-clinical`, or `iris-administration`. Verified by `IrisDecouplingArchitectureTest.java` and `architecture.spec.ts`.
- **Domain Neutrality**: `iris-befe/frontend` contains zero clinical FHIR definitions, zero HL7 segment models, and zero direct backend database drivers (satisfying Invariant 3).
- **Zero-PHI Diagnostic Safety**: Presentation components handle technical identifiers only, strictly excluding clinical observations or unmasked patient identifiers (satisfying Invariant 7).

---

## 3. Design Tokens & Aura Light Theme

The Iris visual language is rooted in a light, glare-free, high-contrast palette engineered for prolonged operational reading and clinical surveillance.

### 3.1 Design Tokens Contract (`tokens.css`)

```css
:root {
  /* Surfaces & Backgrounds */
  --iris-bg-page: #f8fafc;        /* Slate 50 - clean, glare-free page background */
  --iris-bg-surface: #ffffff;     /* Pure white for panels, cards, and tables */
  --iris-bg-subtle: #f1f5f9;      /* Slate 100 - headers, toolbar strips, table headers */
  --iris-bg-hover: #e2e8f0;       /* Slate 200 - subtle interactive hover state */
  --iris-bg-selected: #e0f2fe;    /* Sky 100 - active selection surface */

  /* Borders & Dividers */
  --iris-border-default: #e2e8f0; /* Slate 200 - restrained dividers */
  --iris-border-subtle: #cbd5e1;  /* Slate 300 - panel outlines */
  --iris-border-focus: #0284c7;   /* Sky 600 - accessible high-contrast focus ring */

  /* Typography & High-Contrast Text */
  --iris-font-sans: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  --iris-font-mono: 'JetBrains Mono', monospace;
  --iris-text-primary: #0f172a;   /* Slate 900 - dark primary text (13:1 contrast) */
  --iris-text-secondary: #475569; /* Slate 600 - metadata, subtitles (5.5:1 contrast) */
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
  --iris-border-radius: 4px;
  --iris-shadow-subtle: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}
```

### 3.2 PrimeVue 4 Aura Light Preset

The theme is configured using PrimeVue's `definePreset` extending Aura:
- Primary color scale maps to Slate/Sky neutrals;
- Surface colors configured for clean white containers on Slate 50 canvases;
- Typography configured for Inter with compact font sizes (`0.8125rem` to `0.875rem` for data grids);
- Form controls and buttons configured with subtle borders and clear focus rings.

---

## 4. Shared Component Catalogue

### 4.1 Shell Components (`src/components/shell/`)

| Component | Responsibility | Props & Slots |
| :--- | :--- | :--- |
| **`IrisApplicationShell`** | Top-level application layout wrapper. Manages header, persistent navigation bar, breadcrumbs, alert ribbons, and router-view slot. | `application`, `cluster`, `environment`, `status`, `activeAlerts`, `#user-menu`, `#header-actions`, `#default` |
| **`IrisHeader`** | Global application header bar displaying system identity, environment, cluster state, global health status badge, clock, and action buttons. | `applicationName`, `clusterId`, `environment`, `globalStatus`, `activeAlertCount`, `lastRefreshedAt` |
| **`IrisPrimaryNavigation`** | High-contrast horizontal perspective navigation bar with active route highlighting, perspective sub-labels, and dynamic alert count badges. | `items: NavPerspective[]` |
| **`IrisToolbar`** | Standardized page action bar. Integrates search inputs, time window selectors (`15m`, `1h`, `6h`, `24h`), refresh buttons with spin indicators, and custom action button slots. | `searchQuery`, `timeWindow`, `isRefreshing`, `#left`, `#filter`, `#right` |
| **`IrisBreadcrumbs`** | Hierarchical architectural and location trail. Supports deep-linking and keyboard navigation. | `items: BreadcrumbItem[]` |
| **`IrisPage`** & **`IrisPageHeader`** | High-density page container and header strip standardizing title typography, subtitle, and breadcrumbs. | `title`, `subtitle`, `badge`, `#actions` |
| **`IrisUserMenu`** | Current operator/clinician identity pill, security role tag, and logout action trigger. | `username`, `role`, `authorities` |

### 4.2 Presentation & Diagnostic Components (`src/components/presentation/`)

| Component | Responsibility | Props & Slots |
| :--- | :--- | :--- |
| **`IrisStatus`** | Accessible operational status badge. Pairs distinct iconography (`●`, `▲`, `✖`, `?`) with textual status (`Healthy`, `Degraded`, `Unavailable`, `Unknown`). Supports `stale` telemetry indicators and multiple label formats (`title`, `upper`, `lower`). | `status: StatusState`, `stale: boolean`, `labelFormat: 'title' \| 'upper' \| 'lower'` |
| **`IrisSubsystemIdentity`** | Standardized subsystem header card displaying authoritative Greek mythological name, mandatory English architectural subtitle, version tag, and live status badge. | `greekName`, `englishDescription`, `version`, `status`, `stale`, `#actions`, `#meta` |
| **`IrisDataTable`** | High-density desktop data table built upon PrimeVue `DataTable`. Enforces compact row heights (38px), subtle borders, hover highlights, and custom empty states. | `value: any[]`, `loading: boolean`, `paginator: boolean`, `rows: number`, `#empty` |
| **`IrisHierarchy`** | Architectural tree navigator representing Harmonia's 6 top-level areas and child subsystems with expandable nodes, status badges, and selection events. | `nodes: SubsystemNode[]`, `selectedKey`, `@select` |
| **`IrisSection`** | Card-based content section container with high-contrast header, optional action buttons, and clean white data surface. | `title`, `description`, `#actions`, `#default` |
| **`IrisEmptyState`**, **`IrisLoadingState`**, **`IrisErrorState`** | Standardized diagnostic feedback components for empty query results, asynchronous network fetches, and backend telemetry exceptions. | `title`, `message`, `icon`, `retryAction` |

---

## 5. Subsystems Split-Tree Master-Detail Layout

The primary proving ground for the Iris Design System is the redesigned **Subsystems** view in `iris-console`:

```
+----------------------------------------------------------------------------------------------------+
| LEFT: ARCHITECTURAL TREE (IrisHierarchy)        | RIGHT: SUBSYSTEM MASTER-DETAIL                   |
+-------------------------------------------------+--------------------------------------------------+
| Filter subsystems...                         [Q]| PETASOS                             ● Healthy    |
|                                                 | Messaging & Transport               v1.0.0       |
| v 1. INTEGRATION & TRANSPORT                    +--------------------------------------------------+
|   * Pylai (Interface Gateways)        ● Healthy | [Refresh Telemetry]  [Inspect Queues]  [Topology]  |
|   * Petasos (Messaging & Transport)   ● Healthy +--------------------------------------------------+
|                                                 | [Instances (2)] [Health & Deps] [Statistics (1h)]|
| > 2. EXECUTION & PROCESSING           ● Healthy +--------------------------------------------------+
|   Energeia (Ponos, Ergon, Praxis)               | INSTANCE ID     ROLE    HOST      PORT   STATE   |
|                                                 | petasos-node-1  Primary artemis1  61616  ● RUN   |
| > 3. INFORMATION & STATE              ● Healthy | petasos-node-2  Backup  artemis2  61616  ● RUN   |
|   Calliope, Mneme, Mnemosyne                    +--------------------------------------------------+
|                                                 | MIDDLEWARE RUNTIME: Apache ActiveMQ Artemis      |
| > 4. SECURITY & POLICY                ● Healthy | - Ingress Depth: 0 msgs    - Dequeue: 142 msg/s  |
|   Themis                                        | - DLQ Depth:     0 msgs    - Consumers: 12       |
|                                                 +--------------------------------------------------+
| > 5. COLLABORATION                    ▲ Degraded| DEPENDENCY HEALTH LATENCIES                      |
|   Agora (Matrix Synapse: Unavailable)           | - Themis Security:  ● Healthy (2.1 ms)           |
|                                                 | - Mneme Cache:      ● Healthy (1.4 ms)           |
+-------------------------------------------------+--------------------------------------------------+
```

### Key Subsystems View Rules
1. **Authoritative 6 Architectural Areas**:
   - `Integration & Transport`: Pylai, Petasos.
   - `Execution & Processing`: Energeia (Ponos, Ergon, Praxis, Pragma).
   - `Information & State`: Calliope, Mneme, Mnemosyne.
   - `Security & Policy`: Themis.
   - `Collaboration`: Agora.
   - `Presentation`: Iris.
2. **Middleware Subordination**:
   - ActiveMQ Artemis is subordinated under **Petasos**.
   - Infinispan Cache Grid is subordinated under **Mneme**.
   - PostgreSQL Authoritative DBs are subordinated under **Mnemosyne**.
   - Synapse Matrix Homeserver is subordinated under **Agora**.
3. **English Alongside Greek**: Every component, tree node, and view header displays an explanatory English description alongside its Greek mythological name.

---

## 6. Future Migration Roadmap: `iris-clinical` & `iris-administration`

The design system package (`@harmonia/iris-befe`) was explicitly structured to enable future migration of Harmonia's other two SPAs without architectural disruption.

### 6.1 `iris-clinical` Migration Plan
- **Current Baseline**: Vue 3.4 SPA, dark theme (`style.css`), custom `Navbar.vue` and `Sidebar.vue`, 13 FHIR resource views (Patient, Encounter, Observation, Condition, DiagnosticReport, etc.).
- **Reusable Design System Components**:
  - `IrisApplicationShell` with `application="Clinical"`.
  - `IrisPrimaryNavigation` configured with clinical views (`Patients`, `Encounters`, `Observations`, `Diagnostics`, `Care Teams`, `Audit Provenance`).
  - `IrisToolbar` for patient search, MRN lookup, date range selection, and FHIR resource creation triggers.
  - `IrisDataTable` providing compact desktop row height (38px) for FHIR resource lists.
  - `IrisStatus` for patient active status, consent directives, and encounter states.
- **Migration Strategy & Effort**:
  - *Effort*: **Low-to-Medium** (Estimated 2–3 engineering days).
  - *Zero Backend Changes*: Existing Pinia stores (`clinicalStore`, `patientStore`, `fhirClient.ts`) calling `iris-befe:8080/api/fhir/*` remain completely intact.
  - *Implementation Steps*:
    1. Add `"@harmonia/iris-befe": "file:../iris-befe/frontend"` dependency to `iris-clinical/package.json`.
    2. Import and install `installIrisTheme(app)` and register components in `main.ts`.
    3. Replace `App.vue` dark shell with `<IrisApplicationShell>`.
    4. Replace table markups with `<IrisDataTable>`.

### 6.2 `iris-administration` Migration Plan
- **Current Baseline**: Vue 3.4 SPA, duplicate `StatusBadge.vue`, `Sidebar.vue`, `Topbar.vue`, 17 views for Provider Registry management and change request governance.
- **Reusable Design System Components**:
  - `IrisApplicationShell` with `application="Administration"`.
  - `IrisPrimaryNavigation` configured with administrative tabs (`Provider Directory`, `Organizations`, `Locations`, `Practitioner Roles`, `Work Queue`, `Audit Governance`).
  - `IrisToolbar` for directory search, status filtering, and change request approval/rejection triggers.
  - `IrisStatus` replacing duplicate local status badges.
  - `IrisDataTable` for high-density directory tables.
- **Migration Strategy & Effort**:
  - *Effort*: **Medium** (Estimated 3–4 engineering days).
  - *Invariant Compliance*: Preserves Invariant 3 (frontend remains presentation client; zero server-side validation or direct DB mutation).
  - *Implementation Steps*:
    1. Add `@harmonia/iris-befe` workspace dependency in `iris-administration/package.json`.
    2. Register Iris theme and components in `main.ts`.
    3. Migrate layout shell to `IrisApplicationShell`.
    4. Refactor Work Queue view to utilize `IrisDataTable` and `IrisToolbar`.

---

## 7. Verification, Quality & Known Limitations

### 7.1 Automated Verification Suite
- **Architecture Tests (ArchUnit)**:
  - `IrisDecouplingArchitectureTest.java`: Asserts zero JPA/Hibernate/PostgreSQL dependencies, zero DB drivers in SPAs, and zero reverse dependencies in `iris-befe/frontend`.
  - `ParadeigmaIsolationArchitectureTest.java`: Asserts zero production dependencies on simulation code.
  - `PackageLayeringArchitectureTest.java`: Asserts strict package layering across modules.
- **Unit & Component Testing (Vitest)**:
  - `iris-befe/frontend`: 7 test suites, 35 unit tests verifying shell slot rendering, status accessibility, design token propagation, and unidirectional dependencies.
  - `iris-console`: 14 test suites, 107 unit tests verifying all 6 perspectives, store polling, and drawer interactions.
  - Both suites run in < 4 seconds.

### 7.2 Known Limitations & Unresolved Constraints
1. **Production Bundle Size Status**:
   - The compiled JavaScript bundle for `iris-console` (`index-*.js`) is **1,077 kB** (uncompressed) / **255 kB** (gzip-compressed).
   - *Target Benchmark*: The target non-functional requirement specified uncompressed bundle size $< 450$ kB and gzipped bundle size $< 120$ kB.
   - *Root Cause*: Inclusion of PrimeVue 4 core, Aura preset compiler, Lucide Vue icons, and comprehensive chart/table components in a single main chunk without dynamic code splitting.
   - *Status*: The bundle-size NFR is **exceeded and remains unresolved**. While acceptable for desktop operation on enterprise networks with $< 20$ ms route transitions, immediate priority for a subsequent release should be implementing route-level dynamic code splitting (`() => import(...)`) to reduce the primary bundle footprint below NFR thresholds.
2. **Backend Telemetry Gaps**:
   - Real-time Pylai interface throughput and Synapse Matrix counters rely on future Camel/Synapse telemetry hooks catalogued in `IRIS-API-GAP-001` through `IRIS-API-GAP-005`. Honest placeholders are rendered in the interim.
