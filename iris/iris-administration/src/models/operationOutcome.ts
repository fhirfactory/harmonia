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

import type { OperationOutcome, OperationOutcomeIssue } from './fhir';
import type { ValidationIssue } from './provider';

export interface ValidationCodeDefinition {
  code: string;
  name: string;
  defaultSeverity: 'fatal' | 'error' | 'warning' | 'information';
  description: string;
  remediation: string;
}

export const PR_VAL_CODES: Record<string, ValidationCodeDefinition> = {
  'PR-VAL-001': {
    code: 'PR-VAL-001',
    name: 'Structural Syntax Error',
    defaultSeverity: 'error',
    description: 'The submitted resource structure does not conform to the FHIR R5 schema specification.',
    remediation: 'Verify that all elements match the FHIR R5 specification datatypes and syntax.'
  },
  'PR-VAL-002': {
    code: 'PR-VAL-002',
    name: 'Missing Mandatory Field',
    defaultSeverity: 'error',
    description: 'A required field according to the Harmonia Provider Registry profile is missing.',
    remediation: 'Ensure all required fields (e.g. family name, active status, system code) are populated.'
  },
  'PR-VAL-003': {
    code: 'PR-VAL-003',
    name: 'Duplicate Identifier Conflict',
    defaultSeverity: 'error',
    description: 'The supplied business identifier (e.g. HPI-I, HPI-O, or registration number) is already assigned to another active registry entity.',
    remediation: 'Verify the identifier value and check whether the practitioner/organization is already registered.'
  },
  'PR-VAL-004': {
    code: 'PR-VAL-004',
    name: 'Referenced Entity Not Found',
    defaultSeverity: 'error',
    description: 'A referenced entity (e.g. Organization, Practitioner, or Location) does not exist in the registry.',
    remediation: 'Check the reference ID or search for the existing parent entity in the directory.'
  },
  'PR-VAL-005': {
    code: 'PR-VAL-005',
    name: 'Referenced Entity Inactive',
    defaultSeverity: 'warning',
    description: 'The referenced organization or parent facility is currently marked as inactive or suspended.',
    remediation: 'Confirm whether the affiliated organization should be reactivated before associating roles.'
  },
  'PR-VAL-006': {
    code: 'PR-VAL-006',
    name: 'Concurrency / Version Conflict',
    defaultSeverity: 'error',
    description: 'The record was updated by another user or system since it was loaded (If-Match / ETag version mismatch).',
    remediation: 'Reload the latest version of the record before re-applying your changes.'
  },
  'PR-VAL-007': {
    code: 'PR-VAL-007',
    name: 'Invalid Endpoint Configuration',
    defaultSeverity: 'error',
    description: 'The electronic communication endpoint address or connection type is invalid or malformed.',
    remediation: 'Verify URL format, port number, and MIME types supported by the endpoint.'
  },
  'PR-VAL-008': {
    code: 'PR-VAL-008',
    name: 'Invalid Group Hierarchy',
    defaultSeverity: 'error',
    description: 'Circular or invalid group membership structure detected.',
    remediation: 'Inspect group member references to ensure no circular parent-child chains exist.'
  },
  'PR-VAL-009': {
    code: 'PR-VAL-009',
    name: 'Unsupported Resource Type',
    defaultSeverity: 'error',
    description: 'The resource type is not managed by the Provider Registry domain.',
    remediation: 'Only Provider Registry resources (Practitioner, PractitionerRole, Organization, Location, HealthcareService, Endpoint, Group) can be processed.'
  },
  'PR-VAL-010': {
    code: 'PR-VAL-010',
    name: 'Business Rule Evaluation Failure',
    defaultSeverity: 'error',
    description: 'A domain business rule failed during Ergon pipeline execution.',
    remediation: 'Inspect the detailed diagnostics message returned by the server.'
  }
};

/**
 * Parses a FHIR OperationOutcome resource into an array of UI-friendly ValidationIssue items.
 */
export function parseOperationOutcome(outcome: OperationOutcome | null | undefined): ValidationIssue[] {
  if (!outcome || !outcome.issue || !Array.isArray(outcome.issue)) {
    return [];
  }

  return outcome.issue.map(issue => {
    // Extract code from issue details coding or fallback to issue.code
    let extractedCode = issue.code || 'PR-VAL-010';
    if (issue.details?.coding && issue.details.coding.length > 0) {
      extractedCode = issue.details.coding[0].code || extractedCode;
    }

    const valDef = PR_VAL_CODES[extractedCode];
    const details = issue.details?.text || issue.diagnostics || valDef?.description || 'Validation error encountered.';

    return {
      severity: issue.severity || valDef?.defaultSeverity || 'error',
      code: extractedCode,
      details,
      expression: issue.expression,
      diagnostics: issue.diagnostics
    };
  });
}
