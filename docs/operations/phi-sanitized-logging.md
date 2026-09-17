# PHI-Sanitized Logging & Diagnostic Formatting

Harmonia enforces strict, platform-wide privacy standards to prevent Protected Health Information (PHI) from appearing in log streams.

---

## 1. Zero-PHI Logging Invariants

1. **Payload Suppression**: Raw HL7 messages, FHIR JSON resource bodies, clinical observation values, diagnosis texts, patient names, and addresses must **never** be emitted into standard loggers (`INFO`, `WARN`, `ERROR`, `DEBUG`, `TRACE`).
2. **Correlation-Only Logging**: Logs emit only transaction IDs, correlation IDs, message types, and masked identifiers:
   ```json
   {
     "timestamp": "2026-09-17T08:00:00.000Z",
     "level": "INFO",
     "logger": "net.fhirfactory.harmonia.pylai.mllp.IncomingAdtMessageProcessor",
     "message": "Processed inbound ADT^A01 message",
     "correlationId": "c8a45e90-f0e2-4b3d-b4b1-9f939e0d720b",
     "messageType": "ADT_A01",
     "sourceSystem": "PARADEIGMA_PAS",
     "targetTask": "Task/10042",
     "durationMs": 42
   }
   ```
3. **Audit Stream Separation**: Authorized audit entries are sent exclusively via `ThemisAuditService` as non-PHI `ThemisAuditEvent` records tagged with the `AUDIT` security label.

---

## 2. Sanitization Helpers

- **`FhirSanitizer`**: Strips clinical payload attributes before generating operational telemetry.
- **`PhiLogRouting`**: Custom Logback filter that detects and suppresses accidental payload logging attempts.
