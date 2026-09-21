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

/**
 * Provenance marker. Every interface fact carries the reason it is believed to
 * be true: it is either declared configuration, or an observation returned by
 * the operations API. Configuration must never be presented as observation.
 */
export type InterfaceProvenance = 'CONFIGURED' | 'OBSERVED';

/**
 * A gateway declared in the Harmonia deployment configuration.
 *
 * Deliberately contains no runtime fields. Listener counts, connection counts
 * and throughput are runtime observations; the platform does not currently
 * expose an interfaces runtime API, so they are absent rather than invented.
 */
export interface ConfiguredInterface {
  id: string;
  name: string;
  englishTitle: string;
  description: string;
  direction: 'INBOUND' | 'OUTBOUND';
  protocol: string;
  port: number;
  managementPort?: number;
  targetQueue: string;
  complianceRule?: string;
  provenance: 'CONFIGURED';
}

/**
 * Runtime facts about the Pylai subsystem that the operations API genuinely
 * returned. Absent or null values mean "not measured", never "zero".
 */
export interface InterfaceRuntimeObservation {
  subsystemId: 'pylai';
  instanceState?: string | null;
  lastUpdated?: string | null;
  provenance: 'OBSERVED';
}
