# Pragma Security Context & Task Serialization

## 1. Immutable Originating Context

A `Pragma` represents an asynchronous, state-tracked unit of work within Harmonia. It carries immutable security context established at the boundary where the request entered the system:

```java
public class Pragma {
    private ThemisPrincipal originatingPrincipal;
    private final Set<ThemisAuthority> originatingAuthorities;
    private ThemisSecurityContext originatingSecurityContext;
    private String policyVersion;
}
```

---

## 2. Serialization to FHIR R5 Task Extensions

When `Pragma` is persisted or transmitted as a FHIR R5 `Task`, its security context is mapped to standardized extensions:

```
http://harmonia.net/fhir/StructureDefinition/security-principal-id
http://harmonia.net/fhir/StructureDefinition/security-principal-type
http://harmonia.net/fhir/StructureDefinition/security-source-domain
http://harmonia.net/fhir/StructureDefinition/security-authority
http://harmonia.net/fhir/StructureDefinition/security-policy-version
```

### Security Invariants
1. **Unforgeable at Boundary**: Gateway submission logic populates originating security fields strictly from authenticated HTTP request context, preventing clients from overriding credentials.
2. **Tamper Detection**: If serialized security claims are corrupted or stripped in transit, downstream evaluators fail closed (`DENY`).
3. **No Credential Leakage**: Tokens, sessions, and passwords are never serialized into `Pragma` extensions.
