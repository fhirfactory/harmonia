# Pylai Ingress Security Enforcement

```text
------- Legacy Content - Superseded ------- Start ------
Superseded by:
```

- [Identity carriage, Themis enforcement and separate evidence responsibilities](../markdown/05-information-systems-architecture/pylai-external-interoperability.md#pylai-governance)

```text
---------------------------------------------------------
```

## 1. Overview

**Pylai** hosts the external API boundaries of Harmonia, including the FHIR Provider Registry REST gateway.

`FhirSecurityInterceptor` intercepts incoming requests, derives the `ThemisPrincipal`, translates granted mnemonic roles (`X-User-Roles`) and scopes (`X-Security-Scopes`) into granular `ThemisAuthority` sets, and evaluates ingress policy via `ThemisService`.

```text
------- Legacy Content - Superseded ------- Finish ----
```


---

```text
------- Legacy Content - Superseded ------- Start ------
Superseded by:
```

- [Identity carriage, Themis enforcement and separate evidence responsibilities](../markdown/05-information-systems-architecture/pylai-external-interoperability.md#pylai-governance)
- [Server READ/SEARCH and governed change](../markdown/05-information-systems-architecture/pylai-external-interoperability.md#pylai-server-controller)

```text
---------------------------------------------------------
```

## 2. Ingress Interaction Mapping

| HTTP Method | FHIR Interaction | Mapped `ThemisAction` | Evaluated Policy |
| :--- | :--- | :--- | :--- |
| `GET /Practitioner/{id}` | Read | `READ` | `ProviderRegistryReadPolicy` |
| `GET /Practitioner?...` | Search | `SEARCH` | `ProviderRegistryReadPolicy` |
| `POST /Practitioner` | Create Change Request | `SUBMIT_CREATE` | `ProviderRegistrySubmitPolicy` |
| `PUT /Practitioner/{id}` | Update Change Request | `SUBMIT_UPDATE` | `ProviderRegistrySubmitPolicy` |
| `GET /metadata` | Capability Statement | Unrestricted (Public) | Unrestricted |

```text
------- Legacy Content - Superseded ------- Finish ----
```


---

```text
------- Legacy Content - Superseded ------- Start ------
Superseded by:
```

- [Identity carriage, Themis enforcement and separate evidence responsibilities](../markdown/05-information-systems-architecture/pylai-external-interoperability.md#pylai-governance)
- [Ingress Context and semantic processing](../markdown/05-information-systems-architecture/pylai-external-interoperability.md#pylai-pathway-ingress)
- [Protocol progress versus authoritative acceptance](../markdown/05-information-systems-architecture/pylai-external-interoperability.md#acceptance-and-acknowledgement)

```text
---------------------------------------------------------
```

## 3. Asynchronous Task Creation Flow

```mermaid
sequenceDiagram
    autonumber
    actor Client as External Client
    participant Pylai as Pylai REST Gateway
    participant Themis as Themis Policy Engine
    participant Ponos as Ponos / Praxis

    Client->>Pylai: PUT /Practitioner/PR-100 (Header: X-User-Roles=PRV_SUB)
    Pylai->>Themis: authorize(principal, {provider.change.submit}, SUBMIT_UPDATE, PROVIDER_REGISTRY)
    Themis-->>Pylai: ThemisAuthorizationDecision(ALLOW)
    Pylai->>Pylai: Build Pragma (attach caller context)
    Pylai-->>Client: HTTP 202 Accepted (Location: /Task/pragma-100)
    Pylai->>Ponos: Publish Pragma via Petasos
```

```text
------- Legacy Content - Superseded ------- Finish ----
```
