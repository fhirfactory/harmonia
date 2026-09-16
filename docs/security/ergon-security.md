# Ergon Security Envelopes & Activity Governance

## 1. Concept

An **Ergon** is an atomic processing activity executed within an asynchronous **Praxis** pipeline. To ensure defence in depth, every Ergon declares its authoritative execution security envelope via `ErgonSecurityDefinition`:

```java
public record ErgonSecurityDefinition(
    String ergonId,
    Set<ThemisAuthority> requiredExecutionAuthorities,
    Set<ThemisAction> permittedActions,
    Set<String> permittedResourceTypes,
    Set<String> permittedSecurityDomains
) implements Serializable {}
```

---

## 2. Exemplar: `PractitionerChangeErgon`

```java
ErgonSecurityDefinition.builder()
    .ergonId("ergon:practitioner-change")
    .requiredExecutionAuthority(ThemisAuthority.of("provider.change.process"))
    .requiredExecutionAuthority(ThemisAuthority.of("provider.resource.create"))
    .requiredExecutionAuthority(ThemisAuthority.of("provider.resource.update"))
    .permittedAction(ThemisAction.PROCESS)
    .permittedAction(ThemisAction.EXECUTE)
    .permittedAction(ThemisAction.UPDATE)
    .permittedAction(ThemisAction.CREATE)
    .permittedResourceType("Practitioner")
    .permittedSecurityDomain("PROVIDER_REGISTRY")
    .build();
```

---

## 3. Dual Checkpoints
1. **Ponos Dispatch Gate**: `PragmaWorkflowDispatcher` checks `requiredExecutionAuthorities` for `PROCESS` before routing into Camel pipeline.
2. **Activity Persistence Gate**: `AbstractProviderRegistryChangeErgon` checks `provider.resource.create` or `provider.resource.update` for `CREATE`/`UPDATE` before invoking `FhirStorageService`.
