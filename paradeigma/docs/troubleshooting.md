# Harmonia Paradeigma — Troubleshooting Guide

### Common Issues & Diagnostic Steps

#### 1. MLLP Connection Refused (`MllpConnectionException: Connection refused`)
- **Root Cause**: The target simulator or Harmonia inbound gateway process is not running on the specified port.
- **Remediation**:
  - Verify that the target simulator is running:
    - PAS Inbound: port `2101`
    - LMS ORU Inbound: port `2102`
    - RIS-PAC ORU Inbound: port `2103`
    - EMR ORM Inbound: port `2104`
    - EMR ADT Inbound: port `2201`
    - LMS ADT Inbound: port `2202`
    - RIS-PAC ADT Inbound: port `2203`
    - LMS ORM Inbound: port `2204`
    - RIS-PAC ORM Inbound: port `2205`
  - Check simulator status endpoints (e.g. `curl http://localhost:8091/api/pas/status`).

---

#### 2. Read Timeout Waiting for HL7 ACK (`MllpTimeoutException`)
- **Root Cause**: Downstream system received the MLLP frame but did not return a trailing `<FS><CR>` delimited ACK within the timeout period.
- **Remediation**:
  - Check downstream logs for syntax errors or hanging database locks.
  - Check whether `FaultInjectionConfig` has `noAckProbability` or large `ackDelayMs` enabled.

---

#### 3. Application Error / Rejection (`AE` or `AR` ACK)
- **Root Cause**: The HL7 message was received and parsed, but failed application-level validation.
- **Remediation**:
  - Inspect the `MSA-3` text in the ACK response to identify the validation failure (e.g. missing PID-3 MRN, missing OBR-4 code).
  - Verify that the synthetic patient was registered before attempting admission or ordering.

---

#### 4. Corrupted Frame Delimiters (`IOException: Stream closed prematurely`)
- **Root Cause**: Non-MLLP client connected or transmission was severed before the `<FS><CR>` trailer.
- **Remediation**:
  - Ensure all clients use `MllpFrameCodec` / `<VT>...<FS><CR>` framing.
