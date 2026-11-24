Optional spending limit; leave empty for no limit: 25
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Inspect the actual Iris frontend implementation for the AuditEvent UI before any modification. Produce a concrete map with file paths and approximate line numbers for:
- `AuditEventView.vue` and any related components actually used by the route
- audit-related frontend API/client services and their supported methods
- routes/navigation entries leading to the audit UI
- any create/emit/delete/edit/update controls, dialogs, forms, confirmation flows, mutation-specific state, validation, notifications, or messages
- any direct access assumptions about Mnemosyne versus Iris BEFE
- any calls or wrappers for POST/PUT/PATCH/DELETE AuditEvent
- any search controls and whether they rely on unsupported legacy params like `name` or `identifier`
- existing frontend tests covering the audit UI or audit API client
- shared components/helpers the audit UI depends on

Please verify behavior from actual code, not filenames alone. Include the smallest high-confidence map of current implementation, with special attention to exact mutation affordances that must be removed in Step 04.6.

**Constraints**  
Read-only exploration only. Do not modify files. Prioritize precise file paths, line numbers, and a concise inventory of what exists now.



Exploration is Ready

**Read-only exploration result**

No files were modified. The active AuditEvent UI is in `iris/iris-clinical`; the separate `iris/iris-befe/frontend` package is a design-system foundation and contains no AuditEvent references.

**1. Audit view and related components**

**Primary view:** `iris/iris-clinical/src/views/AuditEventView.vue`

- Imports Pinia security state and the `AuditEvent` model at lines 18–24.
- Imports `Plus`, `Search`, `Trash2`, `Eye`, `ShieldAlert`, and `X` at lines 21–22; `Plus`/`Trash2` identify the current mutation affordances.
- Shared state and mutation state:
    - `searchName`, `showCreateModal`, `showDetailModal`, and `selectedResource` at lines 24–28.
    - `newAudit` form state at lines 30–35, with default action, code, agent, and severity values.
- Initial list load occurs in `onMounted` at lines 37–39 through `store.fetchAuditEvents()`.
- Search handler at lines 41–43 passes the input to `store.fetchAuditEvents(searchName.value)`.
- Create handler at lines 45–60 constructs a `Partial<AuditEvent>` and calls `store.createAuditEvent(audit)` at line 57, then closes/resets the modal.
- Detail handler at lines 62–65 only selects an already-listed object and opens a local modal; it does not perform a `GET /AuditEvent/{id}`.

**Read/list surface:**

- Page heading and audit description: lines 68–76.
- “Emit Audit Event” button: lines 79–82, opening `showCreateModal`.
- Search field/button: lines 85–100. The UI calls the field `searchName`; its placeholder says `Filter action or agent...`.
- Table/list: lines 102–144.
    - Displays ID, action, code/description, severity, agent, and recorded date at lines 105–132.
    - Empty state at lines 117–119 says: `No Audit Events recorded. Click Emit Audit Event above.`
    - View/detail button at lines 133–136 calls `viewDetails(audit)`.
- Detail JSON modal: lines 198–211. It renders `JSON.stringify(selectedResource, null, 2)` and has only a close button; this is read-only but list-item based rather than an individual BEFE read call.

**2. Mutation affordances and code currently present**

**Create/emit:**

- Button: `AuditEventView.vue:79–82` (`Emit Audit Event`).
- Form modal: `AuditEventView.vue:146–196` (`Create Modal`).
- Form fields:
    - Code/description input with HTML `required`: lines 156–160.
    - Action selector including CREATE/READ/UPDATE/DELETE/EXECUTE: lines 162–171.
    - Severity selector: lines 173–181.
    - Responsible agent input: lines 185–188.
    - Cancel and Save controls: lines 190–193.
- Mutation-specific validation is limited to the HTML `required` attribute on the code field; there is no custom validation.
- There is no create confirmation dialog, success notification, or mutation-specific error message.

**Create API path:**

- `AuditEventView.vue:45–60` builds an audit payload and calls `store.createAuditEvent`.
- `iris/iris-clinical/src/stores/securityStore.ts:79–87` calls `fhirApi.create<AuditEvent>('AuditEvent', audit)`, which maps to an Axios POST.
- The store prepends the returned resource to `auditEvents` at lines 82–84.

**Delete:**

- Inline delete button: `AuditEventView.vue:137–139` calls `audit.id && store.deleteAuditEvent(audit.id)`.
- There is no delete confirmation workflow; the call is direct.
- `securityStore.ts:90–98` calls `fhirApi.delete('AuditEvent', id)` and removes the item from local state.

**Edit/update/patch:**

- No AuditEvent edit form, update button, edit handler, PUT call, or PATCH call was found in `iris/iris-clinical/src`.
- The generic client does expose an unused-for-AuditEvent `update` method (PUT), but no AuditEvent store/view code invokes it.
- The generic client has no PATCH method.

**Mutation-specific state/messages:**

- Mutation-only state is `showCreateModal` and `newAudit` in `AuditEventView.vue:26–35`.
- The only mutation-specific user-facing strings are the Emit/Create modal labels and the empty-state instruction to click Emit.
- No AuditEvent-specific success/error notification, toast, confirmation, or delete message exists.
- The shared store has an `error` ref (`securityStore.ts:27–28`) and search catch at lines 71–74, but `AuditEventView.vue` does not render `store.error` or `store.loading`; therefore current read errors are not visibly surfaced by this view.
- The generic search client logs caught errors via `console.error` at `api/fhirClient.ts:38–41`; no AuditEvent payload is explicitly logged by the view.

**3. Audit API/client services and methods**

**Shared client:** `iris/iris-clinical/src/api/fhirClient.ts`

- Axios base URL: lines 21–27, `import.meta.env.VITE_API_BASE_URL || '/api/fhir'`.
- `search<T>`: lines 30–42, performs `GET /{resourceType}` and maps FHIR Bundle entries to resources.
- `get<T>`: lines 44–47, performs `GET /{resourceType}/{id}`; this is not used by the current AuditEvent view/store path.
- `create<T>`: lines 49–52, performs `POST /{resourceType}`; used for AuditEvent creation through `securityStore.ts:82`.
- `update<T>`: lines 54–57, performs `PUT /{resourceType}/{id}`; no AuditEvent caller found.
- `delete`: lines 59–61, performs `DELETE /{resourceType}/{id}`; used for AuditEvent deletion through `securityStore.ts:93`.
- No PATCH wrapper exists.

**Shared store:** `iris/iris-clinical/src/stores/securityStore.ts`

- Imports `Provenance`, `AuditEvent`, and `Consent` together at lines 18–21.
- Audit collection and shared loading/error refs: lines 23–28.
- `fetchAuditEvents(name?: string)`: lines 65–77; creates `params`, puts the argument under `params.name` at lines 69–71, and calls `fhirApi.search<AuditEvent>('AuditEvent', params)`.
- `createAuditEvent`: lines 79–87; POST path described above.
- `deleteAuditEvent`: lines 90–98; DELETE path described above.
- All three AuditEvent functions are returned publicly from the store at lines 135–149, including mutation functions at lines 145–146.

**Model:** `iris/iris-clinical/src/models/fhir.ts:236–250`

- `AuditEvent` extends `BaseResource`, with action, severity, recorded, outcome, code, and agent fields.
- The model itself does not add client mutation behavior.

**4. Search parameter compatibility**

- The only AuditEvent search control is the free-text `searchName` input at `AuditEventView.vue:89–95`; it is sent as `name`, not `_id`.
- No `_id` search control exists.
- No `identifier` search control exists in `AuditEventView.vue` or the AuditEvent section of `securityStore.ts`.
- The placeholder claims action/agent filtering, but the store sends `name`; the backend contract does not support action/agent filtering through this parameter.
- The BEFE explicitly rejects `name` and `identifier` with HTTP 400. `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java:96–107` rejects either parameter and only builds an `AuditQuery` from `_id` at lines 109–115.
- Consequently the current AuditEvent search field is an obsolete/incorrect legacy search assumption and is likely to fail closed whenever non-empty.

**5. Routes and navigation**

- Route registration: `iris/iris-clinical/src/router/index.ts:27` imports `AuditEventView`; `:44` registers `{ path: '/audit', name: 'audit', component: AuditEventView }`.
- Main application shell: `iris/iris-clinical/src/App.vue:18–22` imports `Sidebar`/`Topbar`; `:47–72` renders the shell and `<router-view />`.
- Sidebar navigation: `iris/iris-clinical/src/components/Sidebar.vue:81–86` includes `Audit Events` at `/audit` under “Lineage & Governance”.
- Topbar route metadata: `iris/iris-clinical/src/components/Topbar.vue:37–50` labels `/audit` as `Audit Events` / `Lineage & Governance`.
- Legacy/secondary Navbar: `iris/iris-clinical/src/components/Navbar.vue:25–38` also includes `{ name: 'Audit', path: '/audit' }`.
- Dashboard entry/count: `iris/iris-clinical/src/views/DashboardView.vue:178–186` links to `/audit` and displays `securityStore.auditEvents.length`.
- No audit-specific route parameter or detail route (`/audit/:id`) exists.

**6. Direct Mnemosyne versus BEFE access**

- The clinical frontend has no direct Mnemosyne, JPA, PostgreSQL, or audit-persistence client reference in the AuditEvent path.
- All AuditEvent calls go through the generic FHIR client’s `/api/fhir` base path (`fhirClient.ts:21–23`).
- Production frontend routing confirms `/api/` is proxied to BEFE: `iris/iris-clinical/nginx.conf:27–33` proxies to `http://befe:8080/api/`.
- Development Vite routing proxies `/api` to `VITE_API_URL` or `http://localhost:8080`: `iris/iris-clinical/vite.config.ts:30–37`.
- No frontend code constructs an `AuditService`/trusted principal or directly appends audit evidence.
- The current frontend nevertheless exposes POST/DELETE paths through the generic client/store, despite the BEFE façade being read-only.

**7. Backend façade artifacts verified for context**

The frontend exploration was cross-checked against the existing BEFE surface:

- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java` declares only `@GET` search at lines 96–143 and `@GET @Path("/{id}")` read at lines 145–177.
- The resource delegates reads/searches to `AuditService` and maps `HarmoniaAuditEvent`; it has no append call, persistence provider, or mutation method.
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java:226–254` verifies unsupported `name` and `identifier` fail closed with 400 and no AuditService interaction.
- The same test’s architecture assertion at lines 286–308 verifies POST/PUT/DELETE/PATCH methods are absent, and lines 311–318 verify no `FhirCacheService` field is present.

**8. Tests and test conventions**

- No frontend tests matching `*.test.*` or `*.spec.*` were found under `iris/iris-clinical`; its `package.json:8–12` has only `dev`, `build`, and `preview` scripts and no test script.
- `iris/iris-befe/frontend` does have a Vitest setup (`package.json:22–25`) and tests under `src/__tests__`, but those tests cover the design-system foundation (`architecture`, hierarchy, presentation, shell, status, theme, toolbar) and contain no AuditEvent UI/API coverage.
- The only focused AuditEvent test currently found is the backend JUnit test above, not a frontend test.

**Smallest high-confidence Step 04.6 removal map**

The mutation surface is concentrated in two frontend files:

1. `iris/iris-clinical/src/views/AuditEventView.vue`
    - Remove create imports/state/handler/button/modal and mutation-only form strings.
    - Remove inline delete button/action.
    - Replace the current `name` search assumption with a supported read/search control or remove the obsolete control; do not preserve the misleading action/agent/name behavior.
    - Retain list rendering, local JSON detail view, loading/empty/error behavior as appropriate for the implementation.
2. `iris/iris-clinical/src/stores/securityStore.ts`
    - Remove `createAuditEvent` and `deleteAuditEvent` and their returned exports.
    - Rework `fetchAuditEvents` away from unsupported `name`; the existing generic client already supports GET search and GET-by-ID, but there is currently no AuditEvent-specific GET-by-ID store function.

The generic `fhirClient.ts` is shared by unrelated resources and currently contains legitimate create/update/delete methods for other Iris screens; the exploration found no reason to remove those generic methods as part of this focused slice. No files were changed or deleted, and no new audit mutation path was introduced.

**Discoveries for future steps**

- `iris-clinical` has no frontend test runner or test files; adding focused coverage would require first deciding whether to extend this package’s tooling or use an existing repository convention.
- The current non-empty AuditEvent search field sends unsupported `name`, while BEFE supports `_id`; this is a concrete compatibility bug, not merely a cosmetic label issue.
- Current AuditEvent detail is a local JSON modal over list data; there is no `/audit/:id` route or store call to the generic client’s existing `get` method.
- Backend BEFE read-only and unsupported-parameter guards are already covered by `AuditEventResourceTest`; no backend changes are indicated by this frontend exploration.



PLAN THE TASK

**Task description**  
Plan the whole task.

**Feedback**  
Use the completed frontend exploration and the user’s Step 04.6 requirements. The exploration established:
- Active UI is `iris/iris-clinical/src/views/AuditEventView.vue` with current mutation affordances: Emit/Create modal, delete button, mutation-specific state, and legacy `name` search.
- `securityStore.ts` currently exposes `fetchAuditEvents(name?: string)`, `createAuditEvent`, and `deleteAuditEvent`; create/delete call generic FHIR POST/DELETE.
- `fhirClient.ts` is shared and generic; do not remove legitimate generic CRUD methods used by other features unless strictly necessary.
- The current UI has only local list-based detail modal and no `/audit/:id` route; BEFE supports GET list/search and GET by id.
- `iris-clinical` appears to have no existing frontend test runner/tests, while `iris-befe/frontend` has Vitest but is unrelated to the active AuditEvent UI package. The plan must handle this truthfully and proportionately rather than assuming tests already exist.

Plan for the smallest truthful Step 04.6 path that:
- removes all AuditEvent mutation affordances/code from the Iris UI;
- converges search to supported BEFE semantics (likely `_id` if any search input remains) and removes unsupported legacy search controls;
- preserves useful read/list/detail/loading/empty/error behavior;
- avoids client-side authorization logic or new backend endpoints/services;
- adds the smallest proportionate regression/architecture guardrail consistent with actual Iris frontend test architecture, or explicitly identifies if there is no existing runner and what minimal approach is viable within project conventions;
- validates focused frontend behavior plus confirms backend invariants from Steps 04.3–04.5 remain unchanged.

Please explicitly address the testing/tooling reality in `iris-clinical` and avoid overcommitting to nonexistent frontend test conventions.

**Constraints**  
No git operations. Plan Step 04.6 only.



**Requirements**

Converge the Iris clinical frontend (`iris-clinical`) AuditEvent user interface to be strictly read-oriented over the immutable Kleio audit architecture exposed via the Iris BEFE AuditEvent façade.

- **Goal / Outcome**: Remove all AuditEvent mutation affordances, forms, and store mutation methods; align search with the supported `_id` parameter; preserve read, search, list, detail, loading, and error states; add focused frontend regression and architecture guardrail tests.
- **In Scope**:
    - `iris/iris-clinical/src/views/AuditEventView.vue`: Remove Emit/Create button, Create modal, Delete button, and mutation-specific state/validation; converge search input to `_id` filtering; add safe error/loading state handling.
    - `iris/iris-clinical/src/stores/securityStore.ts`: Remove `createAuditEvent` and `deleteAuditEvent` methods; update `fetchAuditEvents` to query by `_id`.
    - `iris/iris-clinical/package.json` & `vite.config.ts`: Wire standard Vitest test configuration mirroring `iris-console` and `iris-befe/frontend`.
    - `iris/iris-clinical/src/__tests__/auditEvent.spec.ts`: Add unit and architecture guardrail tests verifying read-only behavior, lack of mutation endpoints/affordances, and `_id` query mapping.
- **Out of Scope**:
    - Modifications to generic `fhirClient.ts` CRUD methods used by other resources (e.g. Provenance, Consent).
    - Backend modifications to BEFE `AuditEventResource`, Themis authorization, or Kleio persistence.
    - Petasos replay or transition auditing (Step 04.7 / Tasks 07–10).
    - Client-side role matrices, JWT parsing, or custom authorization logic.
- **Done When**:
    - `AuditEventView.vue` contains zero Create, Emit, Delete, or Edit buttons/modals.
    - `securityStore.ts` contains no `createAuditEvent` or `deleteAuditEvent` actions.
    - Search executes `GET /api/fhir/AuditEvent?_id=...` and rejects legacy `name`/`identifier` parameters.
    - Detail JSON modal and list rendering function reliably.
    - Vitest test suite passes in `iris-clinical` and BEFE backend tests remain green.

**Technical Design**

- **Decisions**:
    - *Retain generic `fhirClient.ts` CRUD methods*: Chose to keep `create`, `update`, and `delete` on `fhirClient` because other resources (e.g., `Provenance`, `Consent`) still use them; removing them would regress unrelated parts of Iris.
    - *Align search to `_id`*: Chose to replace the legacy `name` parameter with `_id` (e.g. `Filter by ID...`) because BEFE explicitly rejects `name` and `identifier` with HTTP 400 and only supports `_id`.
    - *Adopt repo-standard Vitest setup in `iris-clinical`*: Chose to configure Vitest, `@vue/test-utils`, and `jsdom` matching `iris-console` and `iris-befe/frontend` rather than inventing a separate harness.
- **Approach & Touches**:
    - `iris/iris-clinical/src/views/AuditEventView.vue`:
        - Remove imports for `Plus` and `Trash2` icons.
        - Remove `showCreateModal`, `newAudit`, and `handleCreate`.
        - Remove "Emit Audit Event" button and "Create Modal" markup.
        - Remove inline delete button in table rows.
        - Update empty state text from "No Audit Events recorded. Click Emit Audit Event above." to "No Audit Events recorded."
        - Update search binding to `searchId` (placeholder: "Filter by Audit ID (_id)...") and pass to `store.fetchAuditEvents(searchId.value)`.
        - Render `store.loading` spinner/indicator and bounded `store.error` banner.
        - Keep detail JSON modal for viewing resource payload.
    - `iris/iris-clinical/src/stores/securityStore.ts`:
        - Remove `createAuditEvent` and `deleteAuditEvent`.
        - Update `fetchAuditEvents(id?: string)` to set `params._id = id` when provided.
        - Remove `createAuditEvent` and `deleteAuditEvent` from store export.
    - `iris/iris-clinical/package.json` & `vite.config.ts`:
        - Add `"test": "vitest run"` to scripts and devDependencies for `@vue/test-utils`, `vitest`, `jsdom`.
        - Add `test: { globals: true, environment: 'jsdom' }` to `vite.config.ts`.
    - `iris/iris-clinical/src/__tests__/auditEvent.spec.ts`:
        - Component test: verify render of table, `_id` search input, detail modal; assert absence of Emit/Create button, Create modal, and Delete button.
        - Store test: verify `fetchAuditEvents` populates list and passes `_id`; verify absence of `createAuditEvent` and `deleteAuditEvent`.
        - Architecture guardrail test: scan component and store source code to assert zero occurrences of `createAuditEvent`, `deleteAuditEvent`, `POST`, `PUT`, `PATCH`, or `DELETE` for `AuditEvent`.
- **Nuances & Risks**:
    - *Safe Error Display*: Ensure error messages displayed in the UI do not expose raw stack traces, backend connection strings, or PHI payloads.
    - *No Client-Side Authorization*: Do not gate UI features with fake client-side permission checks; external mutation is forbidden for all callers.

**Testing**

- Verify `AuditEventView.vue` renders read-only table with columns for ID, Action, Description/Code, Severity, Agent, Recorded Date, and View Detail action.
- Verify "Emit Audit Event" button, Create form/modal, and Delete row actions are completely absent from DOM.
- Verify `securityStore` does not define or export `createAuditEvent` or `deleteAuditEvent`.
- Verify search query with non-empty input sends `_id` parameter to `fhirApi.search`.
- Verify detail modal opens and displays full JSON when clicking "View Detail" on a row.
- Verify safe display of loading and error states without sensitive information leaks.
- Run frontend test suite: `npm test` in `iris/iris-clinical`.
- Run frontend type check and build: `npm run build` in `iris/iris-clinical`.
- Run backend BEFE tests: `mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest -Dsurefire.failIfNoSpecifiedTests=false`.

**Assumptions & Open Questions**

- **Testing Environment Assumption**: `iris-clinical` previously lacked a test runner in `package.json`, whereas `iris-console` and `iris-befe/frontend` use Vitest with `@vue/test-utils` and `jsdom`. Implementing this standard configuration in `iris-clinical` is the minimal, standard-compliant approach to provide automated verification without violating repository conventions.
- **Search Parameter Assumption**: The previous free-text search sent `name`, which BEFE rejects with HTTP 400. Replacing it with `_id` aligns directly with the BEFE query contract (`GET /api/fhir/AuditEvent?_id=...`).

**Delivery Steps**

**Step 1: Converge Iris Clinical Audit UI to Read-Only and Add Guardrails**  
Goal: Eliminate all AuditEvent mutation code and UI affordances, align search to `_id`, preserve read/detail views, and add focused Vitest tests and architectural guardrails.  
Scope: `iris/iris-clinical/src/views/AuditEventView.vue`, `iris/iris-clinical/src/stores/securityStore.ts`, `iris/iris-clinical/package.json`, `iris/iris-clinical/vite.config.ts`, `iris/iris-clinical/src/__tests__/auditEvent.spec.ts`  
Acceptance Criteria:
- [ ] `AuditEventView.vue` contains no Emit/Create button, Create modal dialog, or Delete action button.
- [ ] Empty state text in `AuditEventView.vue` contains no mutation prompt.
- [ ] Search input in `AuditEventView.vue` and `securityStore.ts` binds to `_id` parameter and does not use `name` or `identifier`.
- [ ] Loading indicator and bounded, safe error messages are rendered in `AuditEventView.vue`.
- [ ] `securityStore.ts` removes `createAuditEvent` and `deleteAuditEvent` from implementation and exports.
- [ ] Vitest test configuration and dependencies are configured in `iris/iris-clinical`.
- [ ] `src/__tests__/auditEvent.spec.ts` verifies read/search/detail functionality, absence of mutation affordances, and architectural guardrail assertions against AuditEvent mutations.
- [ ] `npm test` and `npm run build` pass in `iris/iris-clinical`.
- [ ] Backend BEFE test `AuditEventResourceTest` passes without regression.
  Verification: `npm --prefix iris/iris-clinical test && npm --prefix iris/iris-clinical run build && mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest -Dsurefire.failIfNoSpecifiedTests=false` → green



REVIEW THE PLAN

**Task description**  
Review the current delivery plan.

**Feedback**  
Focus on whether the plan is the smallest truthful path for Step 04.6, especially:
- whether treating the whole task as one implementation step is acceptable or whether separate implementation/testing phases are needed for reliable acceptance;
- whether the plan handles the reality that `iris-clinical` currently appears to have no test runner/tests;
- whether adding Vitest tooling to `iris-clinical` is proportionate and consistent with project conventions, versus relying on existing backend tests plus minimal frontend guardrails elsewhere;
- whether the plan adequately preserves read/list/detail/loading/error behavior while removing mutation affordances and unsupported `name` search;
- whether it avoids accidental scope creep into backend redesign or unrelated UI refactors.

**Constraints**  
Review Step 04.6 only.



Plan Approved

**Verification**
- Confirmed `iris-clinical/package.json` has only `dev`/`build`/`preview` scripts and no test runner — the plan's "no existing tests" premise is truthful.
- Confirmed `iris-console/package.json` and `iris-befe/frontend/package.json` both use `vitest` + `@vue/test-utils` + `jsdom`, and `iris-console/vite.config.ts` contains the exact `test: { globals: true, environment: 'jsdom' }` block the plan proposes to mirror. Adding Vitest to `iris-clinical` is therefore proportionate and convention-consistent, not a new framework.
- Confirmed the mutation surface described exists exactly: `Plus`/`Trash2` imports (line 22), `showCreateModal`/`newAudit`/`handleCreate` (26–60), Emit button (79–82), Create modal (146–196), inline delete button `store.deleteAuditEvent(audit.id)` (137), legacy empty-state prompt (118).
- Confirmed `securityStore.ts` defines `createAuditEvent`/`deleteAuditEvent` and a `name`-based `fetchAuditEvents` (66–98), all publicly exported (144–146); `createProvenance`/`deleteConsent` etc. justify keeping generic `fhirClient` CRUD as the plan states.
- Confirmed BEFE test exists at `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/rest/AuditEventResourceTest.java`, so the Verification `mvn` command targets a real test.
- Single-step structure is appropriate: this is one checkable unit (UI + store + its tests) — production code and its tests correctly share the step.
- Scope discipline verified: no backend edits, generic `fhirClient` CRUD retained, no client-side auth logic, `_id` search aligns with the stated BEFE contract.

**Issues**  
None blocking.

**Notes**
- Guardrail precision (non-blocking): the read-only table legitimately renders `CREATE`/`READ`/`UPDATE`/`DELETE`/`EXECUTE` as action-type display labels (`AuditEventView.vue:123–124`) and these remain after mutation removal. The plan's guardrail as worded ("assert zero occurrences of ... `POST`, `PUT`, `PATCH`, or `DELETE`") must be scoped to mutation *invocations* (e.g. `createAuditEvent`, `deleteAuditEvent`, `fhirApi.create`/`.delete`) rather than raw substring scans, or it will false-positive on those labels. The plan's qualifier "for `AuditEvent`" signals the right intent; flagging so the executor scopes the assertion and avoids churn.
- The plan adds loading/error rendering that the current view does not have (exploration noted the view never renders `store.loading`/`store.error`). This is a small additive enhancement but proportionate and squarely within "preserve safe error handling / bounded messages."

**Discoveries for future steps**
- `iris-clinical/vite.config.ts` aliases `@harmonia/iris-befe` to `../iris-befe/frontend/src` and lacks `preserveSymlinks`/`dedupe` that `iris-console` has; executor may need `dedupe: ['vue']` for `@vue/test-utils` mounting to resolve a single Vue instance.
- `securityStore.ts` mutation functions are the same pattern used by Provenance/Consent; removing only the AuditEvent pair keeps the shared store intact.



**Requirements**

Converge the Iris clinical frontend (`iris-clinical`) AuditEvent user interface to be strictly read-oriented over the immutable Kleio audit architecture exposed via the Iris BEFE AuditEvent façade.

- **Goal / Outcome**: Remove all AuditEvent mutation affordances, forms, and store mutation methods; align search with the supported `_id` parameter; preserve read, search, list, detail, loading, and error states; add focused frontend regression and architecture guardrail tests.
- **In Scope**:
    - `iris/iris-clinical/src/views/AuditEventView.vue`: Remove Emit/Create button, Create modal, Delete button, and mutation-specific state/validation; converge search input to `_id` filtering; add safe error/loading state handling.
    - `iris/iris-clinical/src/stores/securityStore.ts`: Remove `createAuditEvent` and `deleteAuditEvent` methods; update `fetchAuditEvents` to query by `_id`.
    - `iris/iris-clinical/package.json` & `vite.config.ts`: Wire standard Vitest test configuration mirroring `iris-console` and `iris-befe/frontend`.
    - `iris/iris-clinical/src/__tests__/auditEvent.spec.ts`: Add unit and architecture guardrail tests verifying read-only behavior, lack of mutation endpoints/affordances, and `_id` query mapping.
- **Out of Scope**:
    - Modifications to generic `fhirClient.ts` CRUD methods used by other resources (e.g. Provenance, Consent).
    - Backend modifications to BEFE `AuditEventResource`, Themis authorization, or Kleio persistence.
    - Petasos replay or transition auditing (Step 04.7 / Tasks 07–10).
    - Client-side role matrices, JWT parsing, or custom authorization logic.
- **Done When**:
    - `AuditEventView.vue` contains zero Create, Emit, Delete, or Edit buttons/modals.
    - `securityStore.ts` contains no `createAuditEvent` or `deleteAuditEvent` actions.
    - Search executes `GET /api/fhir/AuditEvent?_id=...` and rejects legacy `name`/`identifier` parameters.
    - Detail JSON modal and list rendering function reliably.
    - Vitest test suite passes in `iris-clinical` and BEFE backend tests remain green.

**Technical Design**

- **Decisions**:
    - *Retain generic `fhirClient.ts` CRUD methods*: Chose to keep `create`, `update`, and `delete` on `fhirClient` because other resources (e.g., `Provenance`, `Consent`) still use them; removing them would regress unrelated parts of Iris.
    - *Align search to `_id`*: Chose to replace the legacy `name` parameter with `_id` (e.g. `Filter by ID...`) because BEFE explicitly rejects `name` and `identifier` with HTTP 400 and only supports `_id`.
    - *Adopt repo-standard Vitest setup in `iris-clinical`*: Chose to configure Vitest, `@vue/test-utils`, and `jsdom` matching `iris-console` and `iris-befe/frontend` rather than inventing a separate harness.
- **Approach & Touches**:
    - `iris/iris-clinical/src/views/AuditEventView.vue`:
        - Remove imports for `Plus` and `Trash2` icons.
        - Remove `showCreateModal`, `newAudit`, and `handleCreate`.
        - Remove "Emit Audit Event" button and "Create Modal" markup.
        - Remove inline delete button in table rows.
        - Update empty state text from "No Audit Events recorded. Click Emit Audit Event above." to "No Audit Events recorded."
        - Update search binding to `searchId` (placeholder: "Filter by Audit ID (_id)...") and pass to `store.fetchAuditEvents(searchId.value)`.
        - Render `store.loading` spinner/indicator and bounded `store.error` banner.
        - Keep detail JSON modal for viewing resource payload.
    - `iris/iris-clinical/src/stores/securityStore.ts`:
        - Remove `createAuditEvent` and `deleteAuditEvent`.
        - Update `fetchAuditEvents(id?: string)` to set `params._id = id` when provided.
        - Remove `createAuditEvent` and `deleteAuditEvent` from store export.
    - `iris/iris-clinical/package.json` & `vite.config.ts`:
        - Add `"test": "vitest run"` to scripts and devDependencies for `@vue/test-utils`, `vitest`, `jsdom`.
        - Add `test: { globals: true, environment: 'jsdom' }` to `vite.config.ts`.
    - `iris/iris-clinical/src/__tests__/auditEvent.spec.ts`:
        - Component test: verify render of table, `_id` search input, detail modal; assert absence of Emit/Create button, Create modal, and Delete button.
        - Store test: verify `fetchAuditEvents` populates list and passes `_id`; verify absence of `createAuditEvent` and `deleteAuditEvent`.
        - Architecture guardrail test: scan component and store source code to assert zero occurrences of `createAuditEvent`, `deleteAuditEvent`, `POST`, `PUT`, `PATCH`, or `DELETE` for `AuditEvent`.
- **Nuances & Risks**:
    - *Safe Error Display*: Ensure error messages displayed in the UI do not expose raw stack traces, backend connection strings, or PHI payloads.
    - *No Client-Side Authorization*: Do not gate UI features with fake client-side permission checks; external mutation is forbidden for all callers.

**Testing**

- Verify `AuditEventView.vue` renders read-only table with columns for ID, Action, Description/Code, Severity, Agent, Recorded Date, and View Detail action.
- Verify "Emit Audit Event" button, Create form/modal, and Delete row actions are completely absent from DOM.
- Verify `securityStore` does not define or export `createAuditEvent` or `deleteAuditEvent`.
- Verify search query with non-empty input sends `_id` parameter to `fhirApi.search`.
- Verify detail modal opens and displays full JSON when clicking "View Detail" on a row.
- Verify safe display of loading and error states without sensitive information leaks.
- Run frontend test suite: `npm test` in `iris/iris-clinical`.
- Run frontend type check and build: `npm run build` in `iris/iris-clinical`.
- Run backend BEFE tests: `mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest -Dsurefire.failIfNoSpecifiedTests=false`.

**Assumptions & Open Questions**

- **Testing Environment Assumption**: `iris-clinical` previously lacked a test runner in `package.json`, whereas `iris-console` and `iris-befe/frontend` use Vitest with `@vue/test-utils` and `jsdom`. Implementing this standard configuration in `iris-clinical` is the minimal, standard-compliant approach to provide automated verification without violating repository conventions.
- **Search Parameter Assumption**: The previous free-text search sent `name`, which BEFE rejects with HTTP 400. Replacing it with `_id` aligns directly with the BEFE query contract (`GET /api/fhir/AuditEvent?_id=...`).

**Delivery Steps**

*** Step 1: Converge Iris Clinical Audit UI to Read-Only and Add Guardrails**  
Goal: Eliminate all AuditEvent mutation code and UI affordances, align search to `_id`, preserve read/detail views, and add focused Vitest tests and architectural guardrails.  
Scope: `iris/iris-clinical/src/views/AuditEventView.vue`, `iris/iris-clinical/src/stores/securityStore.ts`, `iris/iris-clinical/package.json`, `iris/iris-clinical/vite.config.ts`, `iris/iris-clinical/src/__tests__/auditEvent.spec.ts`  
Acceptance Criteria:
- [ ] `AuditEventView.vue` contains no Emit/Create button, Create modal dialog, or Delete action button.
- [ ] Empty state text in `AuditEventView.vue` contains no mutation prompt.
- [ ] Search input in `AuditEventView.vue` and `securityStore.ts` binds to `_id` parameter and does not use `name` or `identifier`.
- [ ] Loading indicator and bounded, safe error messages are rendered in `AuditEventView.vue`.
- [ ] `securityStore.ts` removes `createAuditEvent` and `deleteAuditEvent` from implementation and exports.
- [ ] Vitest test configuration and dependencies are configured in `iris/iris-clinical`.
- [ ] `src/__tests__/auditEvent.spec.ts` verifies read/search/detail functionality, absence of mutation affordances, and architectural guardrail assertions against AuditEvent mutations.
- [ ] `npm test` and `npm run build` pass in `iris/iris-clinical`.
- [ ] Backend BEFE test `AuditEventResourceTest` passes without regression.
  Verification: `npm --prefix iris/iris-clinical test && npm --prefix iris/iris-clinical run build && mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest -Dsurefire.failIfNoSpecifiedTests=false` → green



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Implement the smallest truthful Step 04.6 change set in `iris/iris-clinical`.

Key requirements:
- Remove all AuditEvent mutation affordances/code from the active UI: Emit/Create button, Create modal, delete action, mutation-specific state, validation, prompts, and messages.
- Remove AuditEvent mutation methods from `securityStore.ts` and its public exports.
- Converge AuditEvent search away from unsupported legacy `name` semantics to supported BEFE `_id` semantics if a search control remains.
- Preserve useful read behavior: list/search/detail, loading, empty, not-found/safe error handling.
- Do not introduce client-side auth logic, new backend endpoints, or direct append paths.
- Keep generic `fhirClient.ts` CRUD methods intact unless strictly necessary for this step; they are shared by unrelated resources.
- Add the smallest proportionate frontend regression/guardrail tests consistent with actual repository conventions. Exploration found `iris-clinical` lacks a test runner today, while sibling frontend packages use Vitest + `@vue/test-utils` + `jsdom`; if you add that tooling here, keep it minimal and convention-aligned.
- Guardrail tests must target AuditEvent mutation *invocations/affordances* specifically, not naive string bans that would false-positive on legitimate displayed action labels like CREATE/READ/UPDATE/DELETE.
- Confirm BEFE backend invariants remain unchanged by running the focused `AuditEventResourceTest`.

Known from previous steps:
- Active view is `iris/iris-clinical/src/views/AuditEventView.vue`.
- Mutation surface today is concentrated in `AuditEventView.vue` and `iris/iris-clinical/src/stores/securityStore.ts`.
- Current search control sends `name`; BEFE supports `_id` and fails closed on `name` / `identifier`.
- Current detail view is a local JSON modal over listed resources; there is no `/audit/:id` route today.
- Backend Step 04.5 already removed Mnemosyne AuditEvent mutation paths; frontend must not reintroduce any mutation path.
- `iris-console` and `iris-befe/frontend` are useful local examples for Vitest config.

Optimize execution speed:
- Start with narrow commands and small timeouts (60–120s) for install/test/build runs.
- If you add tests, prefer fast focused selectors first.
- Report exact commands, timeouts, and what each verified in Discoveries.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Summary of Changes**

Step 1 has been completed to converge the Iris Clinical AuditEvent user interface (`iris-clinical`) to be strictly read-oriented over the immutable Kleio audit architecture exposed by the Iris BEFE facade:

1. **`iris/iris-clinical/src/views/AuditEventView.vue`**:
    - Removed all mutation affordances: "Emit Audit Event" button, Create form modal, inline table row Delete button, and unused icons (`Plus`, `Trash2`).
    - Removed mutation-specific state (`showCreateModal`, `newAudit`) and handlers (`handleCreate`).
    - Converged search input from legacy `name` to supported BEFE `_id` parameter (`searchId` with placeholder `"Filter by Audit ID (_id)..."`).
    - Added safe error and loading states (spinner during loading, bounded non-leaking error banner when `store.error` is present).
    - Updated empty state text to `"No Audit Events recorded."` removing any instructions to emit records.
    - Preserved list display and read-only JSON detail inspection modal.

2. **`iris/iris-clinical/src/stores/securityStore.ts`**:
    - Removed `createAuditEvent` and `deleteAuditEvent` methods and omitted them from store return exports.
    - Converged `fetchAuditEvents(id?: string)` to pass `params._id = id` when provided, avoiding unsupported query parameters like `name` or `identifier`.

3. **`iris/iris-clinical/package.json` & `iris/iris-clinical/vite.config.ts`**:
    - Added standard repository-consistent test runner configuration (`"test": "vitest run"` with `@vue/test-utils`, `jsdom`, `vitest`).
    - Configured `test: { globals: true, environment: 'jsdom' }` in `vite.config.ts`.

4. **`iris/iris-clinical/src/__tests__/auditEvent.spec.ts`**:
    - Added unit tests verifying read-only table rendering, `_id` search filtering, detail JSON modal, loading indicator, and error banners.
    - Added assertions ensuring the complete absence of Emit/Create buttons, form modals, and Delete buttons.
    - Added store unit tests verifying `_id` parameter mapping and the absence of mutation methods.
    - Added architectural guardrail tests analyzing source code to prevent reintroduction of AuditEvent mutation methods, legacy parameters, or HTTP mutation verbs.

**Verification Results**
- **Frontend Vitest Suite**: `npm --prefix iris/iris-clinical test` passed (13/13 tests green in ~0.6s).
- **Frontend Typecheck & Build**: `npm --prefix iris/iris-clinical run build` passed (`vue-tsc && vite build`).
- **Backend Facade Contract**: `mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest -Dsurefire.failIfNoSpecifiedTests=false` passed (14/14 tests green).
- **Architecture Invariants**: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` passed (57/57 tests green).

**Discoveries for future steps**
- **Execution commands & speed**:
    - `npm --prefix iris/iris-clinical test` runs Vitest tests in ~0.5s.
    - `npm --prefix iris/iris-clinical run build` completes in <1s.
    - `mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest -Dsurefire.failIfNoSpecifiedTests=false` completes in ~2s.
- **Recommendations**: In Vite/Vue-tsc setups without `@types/node`, use Vite's built-in `?raw` imports (e.g., `import content from './File.vue?raw'`) rather than `fs.readFileSync` for architecture guardrails to avoid node type declaration issues during `vue-tsc` builds.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Review the complete Step 04.6 scope implemented in `iris/iris-clinical`.

Focus on:
- whether all AuditEvent mutation affordances/code were actually removed from the active Iris clinical UI and store surface;
- whether the remaining search behavior truthfully aligns to supported BEFE `_id` semantics and does not retain unsupported legacy `name` / `identifier` AuditEvent search assumptions;
- whether read/list/detail/loading/empty/error behavior is preserved and safe, without leaking PHI or backend internals in the UI;
- whether the added frontend tests and guardrail assertions are proportional, accurate, and robust rather than brittle or false-positive prone;
- whether the new test tooling/configuration in `iris-clinical` is minimal and consistent with repository conventions;
- whether the focused verification claims (`npm test`, `npm run build`, BEFE `AuditEventResourceTest`, architecture tests) are plausible and sufficient.

Known context:
- Backend Step 04.5 already removed Mnemosyne AuditEvent mutation paths and BEFE is read-only.
- This step must not introduce client-side auth logic or new backend endpoints.
- Guardrails should target mutation invocations/affordances for AuditEvent, not naive substring bans on displayed action labels.

Do not review unrelated modules except as needed to verify Step 04.6 behavior.



Implementation Approved

**Verification**
- **View mutation affordances removed** (`AuditEventView.vue`): No "Emit Audit Event" button, no Create modal, no Delete row button, no `Plus`/`Trash2` icon imports, no `showCreateModal`/`newAudit`/`handleCreate`. Empty state text is now the clean "No Audit Events recorded." Confirmed by full-file read.
- **Search converged to `_id`**: View binds `searchId` with placeholder "Filter by Audit ID (_id)..." and calls `fetchAuditEvents(searchId.value.trim() || undefined)`; store's `fetchAuditEvents(id?)` sets `params._id = id` only when provided and never sends `name`/`identifier`. Verified in both files.
- **Store mutation methods removed** (`securityStore.ts`): `createAuditEvent`/`deleteAuditEvent` are gone from both implementation and the return export. Generic `fhirClient` CRUD and other resources' (Provenance/Consent) mutations correctly left intact (out of scope).
- **Loading/error/empty states**: Loading spinner row, bounded error banner rendering `store.error` (which is `err.message` — no stack traces, connection strings, or PHI), and clean empty state all present.
- **Safe error display**: Error banner only shows the generic message string set by the store; no raw payloads or internals leaked.
- **Test quality**: `auditEvent.spec.ts` (13 tests) covers mount/read/render, absence of mutation affordances, empty/loading/error states, `_id` search mapping (trimmed), detail modal open/close, store `_id` mapping + failure handling, and absence of `createAuditEvent`/`deleteAuditEvent`. Guardrails target specific mutation invocations/imports (not naive substring bans) and the component test explicitly asserts CREATE/READ/DELETE display labels still render — avoiding false positives.
- **Test tooling** minimal and convention-aligned with `iris-console`/`iris-befe/frontend` (Vitest + `@vue/test-utils` + `jsdom`, `test: { globals, environment: 'jsdom' }`, `"test": "vitest run"`).
- **Executed independently**: `npm test` → 13/13 passed (~0.6s); `npm run build` (vue-tsc + vite) → success; `mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest ...` → exit 0 (green). Grep confirmed no residual AuditEvent mutation refs outside the test file (other `searchName`/`showCreateModal` hits belong to unrelated views/resources, out of scope).

**Issues**  
None blocking.

**Notes**
- `AuditEventView.vue` lines 95–106 place `v-if`/`v-else-if`/`v-else` across three `<tr>` with `v-else` co-located on the `v-for` row. This compiles and renders correctly (build + list-render tests pass), though the `v-for`+`v-else` on one element is slightly unconventional; purely cosmetic, no functional impact.
- BEFE backend was unchanged by this step; the `AuditEventResourceTest` run confirms baseline remains green rather than validating a change.

**Discoveries for future steps**
- `npm --prefix iris/iris-clinical test` ~0.6s; `npm run build` ~1s; `mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest -Dsurefire.failIfNoSpecifiedTests=false -q` completed under the 600s timeout (first run incurs `-am` dependency compilation).
- Architecture guardrails use Vite `?raw` imports of `.vue`/`.ts` source for static assertions — works cleanly under `vue-tsc` build without `@types/node`.



**Requirements**

Converge the Iris clinical frontend (`iris-clinical`) AuditEvent user interface to be strictly read-oriented over the immutable Kleio audit architecture exposed via the Iris BEFE AuditEvent façade.

- **Goal / Outcome**: Remove all AuditEvent mutation affordances, forms, and store mutation methods; align search with the supported `_id` parameter; preserve read, search, list, detail, loading, and error states; add focused frontend regression and architecture guardrail tests.
- **In Scope**:
    - `iris/iris-clinical/src/views/AuditEventView.vue`: Remove Emit/Create button, Create modal, Delete button, and mutation-specific state/validation; converge search input to `_id` filtering; add safe error/loading state handling.
    - `iris/iris-clinical/src/stores/securityStore.ts`: Remove `createAuditEvent` and `deleteAuditEvent` methods; update `fetchAuditEvents` to query by `_id`.
    - `iris/iris-clinical/package.json` & `vite.config.ts`: Wire standard Vitest test configuration mirroring `iris-console` and `iris-befe/frontend`.
    - `iris/iris-clinical/src/__tests__/auditEvent.spec.ts`: Add unit and architecture guardrail tests verifying read-only behavior, lack of mutation endpoints/affordances, and `_id` query mapping.
- **Out of Scope**:
    - Modifications to generic `fhirClient.ts` CRUD methods used by other resources (e.g. Provenance, Consent).
    - Backend modifications to BEFE `AuditEventResource`, Themis authorization, or Kleio persistence.
    - Petasos replay or transition auditing (Step 04.7 / Tasks 07–10).
    - Client-side role matrices, JWT parsing, or custom authorization logic.
- **Done When**:
    - `AuditEventView.vue` contains zero Create, Emit, Delete, or Edit buttons/modals.
    - `securityStore.ts` contains no `createAuditEvent` or `deleteAuditEvent` actions.
    - Search executes `GET /api/fhir/AuditEvent?_id=...` and rejects legacy `name`/`identifier` parameters.
    - Detail JSON modal and list rendering function reliably.
    - Vitest test suite passes in `iris-clinical` and BEFE backend tests remain green.

**Technical Design**

- **Decisions**:
    - *Retain generic `fhirClient.ts` CRUD methods*: Chose to keep `create`, `update`, and `delete` on `fhirClient` because other resources (e.g., `Provenance`, `Consent`) still use them; removing them would regress unrelated parts of Iris.
    - *Align search to `_id`*: Chose to replace the legacy `name` parameter with `_id` (e.g. `Filter by ID...`) because BEFE explicitly rejects `name` and `identifier` with HTTP 400 and only supports `_id`.
    - *Adopt repo-standard Vitest setup in `iris-clinical`*: Chose to configure Vitest, `@vue/test-utils`, and `jsdom` matching `iris-console` and `iris-befe/frontend` rather than inventing a separate harness.
- **Approach & Touches**:
    - `iris/iris-clinical/src/views/AuditEventView.vue`:
        - Remove imports for `Plus` and `Trash2` icons.
        - Remove `showCreateModal`, `newAudit`, and `handleCreate`.
        - Remove "Emit Audit Event" button and "Create Modal" markup.
        - Remove inline delete button in table rows.
        - Update empty state text from "No Audit Events recorded. Click Emit Audit Event above." to "No Audit Events recorded."
        - Update search binding to `searchId` (placeholder: "Filter by Audit ID (_id)...") and pass to `store.fetchAuditEvents(searchId.value)`.
        - Render `store.loading` spinner/indicator and bounded `store.error` banner.
        - Keep detail JSON modal for viewing resource payload.
    - `iris/iris-clinical/src/stores/securityStore.ts`:
        - Remove `createAuditEvent` and `deleteAuditEvent`.
        - Update `fetchAuditEvents(id?: string)` to set `params._id = id` when provided.
        - Remove `createAuditEvent` and `deleteAuditEvent` from store export.
    - `iris/iris-clinical/package.json` & `vite.config.ts`:
        - Add `"test": "vitest run"` to scripts and devDependencies for `@vue/test-utils`, `vitest`, `jsdom`.
        - Add `test: { globals: true, environment: 'jsdom' }` to `vite.config.ts`.
    - `iris/iris-clinical/src/__tests__/auditEvent.spec.ts`:
        - Component test: verify render of table, `_id` search input, detail modal; assert absence of Emit/Create button, Create modal, and Delete button.
        - Store test: verify `fetchAuditEvents` populates list and passes `_id`; verify absence of `createAuditEvent` and `deleteAuditEvent`.
        - Architecture guardrail test: scan component and store source code to assert zero occurrences of `createAuditEvent`, `deleteAuditEvent`, `POST`, `PUT`, `PATCH`, or `DELETE` for `AuditEvent`.
- **Nuances & Risks**:
    - *Safe Error Display*: Ensure error messages displayed in the UI do not expose raw stack traces, backend connection strings, or PHI payloads.
    - *No Client-Side Authorization*: Do not gate UI features with fake client-side permission checks; external mutation is forbidden for all callers.

**Testing**

- Verify `AuditEventView.vue` renders read-only table with columns for ID, Action, Description/Code, Severity, Agent, Recorded Date, and View Detail action.
- Verify "Emit Audit Event" button, Create form/modal, and Delete row actions are completely absent from DOM.
- Verify `securityStore` does not define or export `createAuditEvent` or `deleteAuditEvent`.
- Verify search query with non-empty input sends `_id` parameter to `fhirApi.search`.
- Verify detail modal opens and displays full JSON when clicking "View Detail" on a row.
- Verify safe display of loading and error states without sensitive information leaks.
- Run frontend test suite: `npm test` in `iris/iris-clinical`.
- Run frontend type check and build: `npm run build` in `iris/iris-clinical`.
- Run backend BEFE tests: `mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest -Dsurefire.failIfNoSpecifiedTests=false`.

**Assumptions & Open Questions**

- **Testing Environment Assumption**: `iris-clinical` previously lacked a test runner in `package.json`, whereas `iris-console` and `iris-befe/frontend` use Vitest with `@vue/test-utils` and `jsdom`. Implementing this standard configuration in `iris-clinical` is the minimal, standard-compliant approach to provide automated verification without violating repository conventions.
- **Search Parameter Assumption**: The previous free-text search sent `name`, which BEFE rejects with HTTP 400. Replacing it with `_id` aligns directly with the BEFE query contract (`GET /api/fhir/AuditEvent?_id=...`).

**Delivery Steps**

**✓ Step 1: Converge Iris Clinical Audit UI to Read-Only and Add Guardrails**  
Goal: Eliminate all AuditEvent mutation code and UI affordances, align search to `_id`, preserve read/detail views, and add focused Vitest tests and architectural guardrails.  
Scope: `iris/iris-clinical/src/views/AuditEventView.vue`, `iris/iris-clinical/src/stores/securityStore.ts`, `iris/iris-clinical/package.json`, `iris/iris-clinical/vite.config.ts`, `iris/iris-clinical/src/__tests__/auditEvent.spec.ts`  
Acceptance Criteria:
- [ ] `AuditEventView.vue` contains no Emit/Create button, Create modal dialog, or Delete action button.
- [ ] Empty state text in `AuditEventView.vue` contains no mutation prompt.
- [ ] Search input in `AuditEventView.vue` and `securityStore.ts` binds to `_id` parameter and does not use `name` or `identifier`.
- [ ] Loading indicator and bounded, safe error messages are rendered in `AuditEventView.vue`.
- [ ] `securityStore.ts` removes `createAuditEvent` and `deleteAuditEvent` from implementation and exports.
- [ ] Vitest test configuration and dependencies are configured in `iris/iris-clinical`.
- [ ] `src/__tests__/auditEvent.spec.ts` verifies read/search/detail functionality, absence of mutation affordances, and architectural guardrail assertions against AuditEvent mutations.
- [ ] `npm test` and `npm run build` pass in `iris/iris-clinical`.
- [ ] Backend BEFE test `AuditEventResourceTest` passes without regression.
  Verification: `npm --prefix iris/iris-clinical test && npm --prefix iris/iris-clinical run build && mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest -Dsurefire.failIfNoSpecifiedTests=false` → green



**Summary**
- Converged `iris-clinical` AuditEvent UI to a strictly read-oriented surface over the BEFE read-only facade, with no Iris path to create, update, patch, delete, or append audit evidence.
- Preserved list/search/detail access while aligning search to supported `_id` semantics and keeping backend Step 04.3–04.5 invariants unchanged.

**Changes**
- Removed AuditEvent mutation affordances and code from `iris/iris-clinical/src/views/AuditEventView.vue` and `iris/iris-clinical/src/stores/securityStore.ts` (Emit/Create, delete action, mutation state/handlers/exports, legacy `name` search).
- Added safe loading/error/empty handling and retained read-only detail inspection in `AuditEventView.vue`.
- Added minimal repo-consistent Vitest tooling in `iris/iris-clinical/package.json` and `vite.config.ts`, plus focused regression/guardrail coverage in `iris/iris-clinical/src/__tests__/auditEvent.spec.ts`.

**Verification**
- Passed `npm --prefix iris/iris-clinical test` and `npm --prefix iris/iris-clinical run build`.
- Passed `mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest -Dsurefire.failIfNoSpecifiedTests=false` and `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`.

**Notes**
- Generic CRUD helpers in shared `fhirClient.ts` were intentionally left in place for unrelated Iris resources; no AuditEvent-specific mutation caller remains.

