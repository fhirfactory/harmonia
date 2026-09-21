/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import type { ConfiguredInterface } from '../models/interfaces';

/**
 * Declared Pylai gateway inventory.
 *
 * This is CONFIGURATION, not runtime discovery. Harmonia does not currently
 * expose an interfaces runtime API, so this module is the only source of the
 * gateway inventory and every consumer must label it as configured.
 *
 * Runtime-sounding values (active listeners, current connections, throughput,
 * error counts) are deliberately absent. When a runtime interfaces API exists,
 * it replaces the source behind `interfacesStore` without a layout change.
 */
export const CONFIGURED_INTERFACES: readonly ConfiguredInterface[] = Object.freeze([
  {
    id: 'pylai-mllp-in',
    name: 'MLLP Inbound Gateway',
    englishTitle: 'Ingress Interface',
    description: 'Dual-write ACK gateway translating external clinical wire protocols (hospital ADT/ORM inbound streams) into Petasos events.',
    direction: 'INBOUND',
    protocol: 'HL7 v2 / MLLP',
    port: 2575,
    managementPort: 8084,
    targetQueue: 'petasos.queue.pylai.mllp.in',
    complianceRule: 'Invariant 4 (Dual-Write Safety: AA ACK issued only after downstream Petasos enqueue)',
    provenance: 'CONFIGURED'
  },
  {
    id: 'pylai-mllp-out-his',
    name: 'MLLP Outbound HIS',
    englishTitle: 'HIS Distribution Interface',
    description: 'Outbound HL7 v2 clinical messaging gateway distributing messages to Hospital Information System.',
    direction: 'OUTBOUND',
    protocol: 'HL7 v2 / MLLP',
    port: 8087,
    managementPort: 8084,
    targetQueue: 'petasos.queue.mllp.outbound.his',
    complianceRule: 'Invariant 5 (Destination Fan-Out Tracking: Checkpoint on HIS transmission)',
    provenance: 'CONFIGURED'
  },
  {
    id: 'pylai-mllp-out-lis',
    name: 'MLLP Outbound LIS',
    englishTitle: 'LIS Distribution Interface',
    description: 'Outbound HL7 v2 pathology/lab distribution gateway to Laboratory Information System.',
    direction: 'OUTBOUND',
    protocol: 'HL7 v2 / MLLP',
    port: 8088,
    managementPort: 8084,
    targetQueue: 'petasos.queue.mllp.outbound.lis',
    complianceRule: 'Invariant 5 (Destination Fan-Out Tracking: Checkpoint on LIS transmission)',
    provenance: 'CONFIGURED'
  },
  {
    id: 'pylai-fhir-registry',
    name: 'FHIR Provider Registry Gateway',
    englishTitle: 'REST Registry Ingress',
    description: 'FHIR R5 Practitioner & Organization practitioner self-service and directory interface.',
    direction: 'INBOUND',
    protocol: 'FHIR R5 / REST',
    port: 8089,
    managementPort: 8084,
    targetQueue: 'petasos.queue.ponos.dispatch',
    complianceRule: 'Invariant 3 & 6 (Themis Default-Deny RBAC; zero-JPA presentation boundary)',
    provenance: 'CONFIGURED'
  }
] as ConfiguredInterface[]);

/**
 * Operator-facing notice. Any view rendering the inventory must display this so
 * the reader never mistakes declared configuration for runtime discovery.
 */
export const CONFIGURED_INVENTORY_NOTICE =
  'This interface inventory is configured in the Harmonia deployment, not discovered at runtime. Harmonia does not yet expose a runtime interfaces API.';
