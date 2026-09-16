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

export interface ValidationIssue {
  severity: 'fatal' | 'error' | 'warning' | 'information';
  code: string;
  details: string;
  expression?: string[];
  diagnostics?: string;
}

export interface ProviderSummaryView {
  id: string;
  name: string;
  identifier: string;
  gender: string;
  birthDate?: string;
  active: boolean;
  qualifications: string[];
  rolesCount: number;
  telecom: { system: string; value: string; use?: string }[];
  address?: string;
  lastUpdated?: string;
}

export interface ProviderRoleView {
  id: string;
  practitionerId: string;
  practitionerName?: string;
  organizationId: string;
  organizationName: string;
  code: string;
  specialty: string[];
  locationNames: string[];
  serviceNames: string[];
  active: boolean;
  telecom: { system: string; value: string; use?: string }[];
  period?: { start?: string; end?: string };
}

export interface OrganizationSummaryView {
  id: string;
  name: string;
  identifier: string;
  type: string;
  active: boolean;
  phone?: string;
  email?: string;
  address?: string;
  partOfName?: string;
}

export interface LocationSummaryView {
  id: string;
  name: string;
  status: string;
  mode?: string;
  physicalType?: string;
  organizationName?: string;
  address?: string;
  telecom?: string;
}

export interface HealthcareServiceSummaryView {
  id: string;
  name: string;
  category: string;
  specialty: string[];
  organizationName?: string;
  active: boolean;
  locationNames: string[];
  telecom?: string;
}

export interface EndpointSummaryView {
  id: string;
  name: string;
  status: string;
  connectionType: string;
  address: string;
  organizationName?: string;
  payloadTypes: string[];
}

export interface GroupSummaryView {
  id: string;
  name: string;
  type: string;
  membership: string;
  active: boolean;
  memberCount: number;
}

export interface ChangeRequestSummaryView {
  taskId: string;
  pragmaId?: string;
  correlationId: string;
  resourceType: string;
  resourceId?: string;
  operation: 'CREATE' | 'UPDATE';
  status: 'DRAFT' | 'REQUESTED' | 'ACCEPTED' | 'IN_PROGRESS' | 'VALIDATING' | 'COMMITTING' | 'COMPLETED' | 'REJECTED' | 'FAILED' | 'CANCELLED';
  submittedAt: string;
  requester: string;
  sourceSystem?: string;
  ifMatch?: string;
  resultingVersion?: string;
  diagnostics?: string;
  outcomeIssues?: ValidationIssue[];
  payload?: any;
  targetExistingResource?: any;
}

export interface AdministrationQueueItemView {
  taskId: string;
  correlationId: string;
  resourceType: string;
  resourceId?: string;
  operation: 'CREATE' | 'UPDATE';
  status: string;
  requesterName: string;
  submittedAt: string;
  summary: string;
  validationStatus: 'VALID' | 'WARNINGS' | 'INVALID' | 'UNCHECKED';
  issuesCount: number;
}

export interface ResourceDiffEntry {
  field: string;
  label: string;
  oldValue: string;
  newValue: string;
  status: 'added' | 'removed' | 'modified' | 'unchanged';
}
