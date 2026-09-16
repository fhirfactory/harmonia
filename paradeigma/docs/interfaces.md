# Harmonia Paradeigma — Interface Catalogue (PD-01 to PD-09)

| Interface ID | Interface Name | Source System | Destination System | HL7 Message Type & Event | Default Port | Transport Protocol |
|---|---|---|---|---|---|---|
| **PD-01** | PAS-ADT-IN | PAS Simulator (`paradeigma-pas`) | Harmonia Pylai Inbound | `ADT^A01`, `A02`, `A03`, `A04`, `A08`, `A11`, `A12`, `A13` | `2101` | MLLP / TCP |
| **PD-02** | LMS-ORU-IN | LMS Simulator (`paradeigma-lms`) | Harmonia Pylai Inbound | `ORU^R01` (Laboratory Observations) | `2102` | MLLP / TCP |
| **PD-03** | RISPAC-ORU-IN | RIS-PAC Simulator (`paradeigma-rispac`) | Harmonia Pylai Inbound | `ORU^R01` (Diagnostic Imaging Reports) | `2103` | MLLP / TCP |
| **PD-04** | EMR-ORM-IN | EMR Simulator (`paradeigma-emr`) | Harmonia Pylai Inbound | `ORM^O01` (Lab & Imaging Orders) | `2104` | MLLP / TCP |
| **PD-05** | EMR-ADT-OUT | Harmonia Pylai Outbound | EMR Simulator (`paradeigma-emr`) | `ADT` (Internal Fan-out) | `2201` | MLLP / TCP |
| **PD-06** | LMS-ADT-OUT | Harmonia Pylai Outbound | LMS Simulator (`paradeigma-lms`) | `ADT` (Internal Fan-out) | `2202` | MLLP / TCP |
| **PD-07** | RISPAC-ADT-OUT | Harmonia Pylai Outbound | RIS-PAC Simulator (`paradeigma-rispac`) | `ADT` (Internal Fan-out) | `2203` | MLLP / TCP |
| **PD-08** | LMS-ORM-OUT | Harmonia Pylai Outbound | LMS Simulator (`paradeigma-lms`) | `ORM^O01` (Routed Lab Order) | `2204` | MLLP / TCP |
| **PD-09** | RISPAC-ORM-OUT | Harmonia Pylai Outbound | RIS-PAC Simulator (`paradeigma-rispac`) | `ORM^O01` (Routed Imaging Order) | `2205` | MLLP / TCP |

---

### MLLP Transport Framing Specification
All MLLP interfaces operate strictly under standard HL7 Minimal Lower Layer Protocol framing:
- **Start Block**: `0x0B` (`<VT>`)
- **Payload**: Raw HL7 message text (UTF-8)
- **End Block**: `0x1C` (`<FS>`) + `0x0D` (`<CR>`)

### Synchronous Acknowledgement Protocol
Every inbound transmission requires a synchronous HL7 ACK response (`AA` = Application Accept, `AE` = Application Error, `AR` = Application Reject) referencing the original `MSH-10` in `MSA-2`.
