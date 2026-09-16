# Themis Policy Model & Precedence

## 1. Policy Structure

Each policy implements `ThemisPolicy`:
- `getPolicyId()`: Unique policy identifier string.
- `getOrder()`: Evaluation precedence ordering (lower numbers evaluate earlier).
- `appliesTo(ThemisAuthorizationRequest request)`: Target domain, action, and resource matching predicate.
- `evaluate(ThemisAuthorizationRequest request)`: Evaluates authorities and contextual predicates, returning `ThemisAuthorizationDecision`.

---

## 2. Default Initial Policies

### `ProviderRegistryReadPolicy`
- **Target Domain**: `PROVIDER_REGISTRY`
- **Actions**: `READ`, `SEARCH`
- **Required Authority**: `provider.read` or `provider.search` or `provider.admin`

### `ProviderRegistrySubmitPolicy`
- **Target Domain**: `PROVIDER_REGISTRY`
- **Actions**: `SUBMIT_CREATE`, `SUBMIT_UPDATE`
- **Required Authority**: `provider.change.submit` or `provider.admin`

### `ProviderRegistryProcessPolicy`
- **Target Domain**: `PROVIDER_REGISTRY`
- **Actions**: `PROCESS`, `EXECUTE`
- **Required Authority**: `provider.change.process` or `provider.admin`

### `ProviderRegistryPersistPolicy`
- **Target Domain**: `PROVIDER_REGISTRY`
- **Actions**: `CREATE`, `UPDATE`, `DELETE`
- **Required Authority**: `provider.resource.create` (for CREATE), `provider.resource.update` (for UPDATE), `provider.resource.delete` (for DELETE), or `provider.admin`

### `SystemAdminPolicy`
- **Target Domain**: `*`
- **Actions**: `ADMINISTER`, `*`
- **Required Authority**: `system.admin`

---

## 3. Policy Precedence & Preemption Rules
1. **Explicit Deny**: Any policy returning `DENY` immediately halts further rule evaluation and rejects the request.
2. **First Matching Allow**: When multiple policies match a domain without explicit deny, any matching allow grants access.
3. **Default Fallback**: If no active policy matches the target, action, or principal, the engine falls back to `DEFAULT DENY`.
