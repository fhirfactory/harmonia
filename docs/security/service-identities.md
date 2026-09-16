# Harmonia Controlled Service Identities

## 1. Principle of Least Privilege for Internal Services

Internal Harmonia microservices and background components are **not** implicitly trusted. Every internal component operates under an explicit service identity with least-privilege authorities defined centrally in `HarmoniaServiceIdentities`.

---

## 2. Standard Service Catalogue

| Service Identifier | Subsystem | Assigned Granular Authorities |
| :--- | :--- | :--- |
| `service:pylai` | Pylai API Gateway | `provider.read`, `provider.search`, `provider.change.submit`, `system.integration` |
| `service:petasos` | Petasos Messaging | `system.integration` |
| `service:ponos` | Ponos WorkEngine | `provider.change.process`, `system.integration` |
| `service:provider-registry` | Provider Registry | `provider.resource.create`, `provider.resource.update`, `provider.resource.delete`, `provider.read`, `provider.search`, `provider.admin`, `system.integration` |
| `service:themis` | Themis Security Engine | `audit.read`, `system.admin` |
| `service:calliope` | Schema & Model Library | `system.integration` |
| `service:mnemosyne` | Clinical Storage Service | `provider.resource.create`, `provider.resource.update`, `provider.resource.delete`, `provider.read`, `provider.search`, `provider.admin`, `system.integration` |

---

## 3. Administrative Separation

System administration authority (`system.admin` / `SYS_ADM`) is conceptually separated from unrestricted clinical and provider data access. Operating platform infrastructure does not automatically grant blanket authorities to inspect or modify clinical and directory databases.
