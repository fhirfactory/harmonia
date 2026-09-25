---
sessionId: session-260924-213158-1lfm
---

# Requirements

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

# Technical Design

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

# Testing

- Verify `AuditEventView.vue` renders read-only table with columns for ID, Action, Description/Code, Severity, Agent, Recorded Date, and View Detail action.
- Verify "Emit Audit Event" button, Create form/modal, and Delete row actions are completely absent from DOM.
- Verify `securityStore` does not define or export `createAuditEvent` or `deleteAuditEvent`.
- Verify search query with non-empty input sends `_id` parameter to `fhirApi.search`.
- Verify detail modal opens and displays full JSON when clicking "View Detail" on a row.
- Verify safe display of loading and error states without sensitive information leaks.
- Run frontend test suite: `npm test` in `iris/iris-clinical`.
- Run frontend type check and build: `npm run build` in `iris/iris-clinical`.
- Run backend BEFE tests: `mvn test -pl iris/iris-befe -am -Dtest=AuditEventResourceTest -Dsurefire.failIfNoSpecifiedTests=false`.

# Assumptions & Open Questions

- **Testing Environment Assumption**: `iris-clinical` previously lacked a test runner in `package.json`, whereas `iris-console` and `iris-befe/frontend` use Vitest with `@vue/test-utils` and `jsdom`. Implementing this standard configuration in `iris-clinical` is the minimal, standard-compliant approach to provide automated verification without violating repository conventions.
- **Search Parameter Assumption**: The previous free-text search sent `name`, which BEFE rejects with HTTP 400. Replacing it with `_id` aligns directly with the BEFE query contract (`GET /api/fhir/AuditEvent?_id=...`).

# Delivery Steps

### ✓ Step 1: Converge Iris Clinical Audit UI to Read-Only and Add Guardrails
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