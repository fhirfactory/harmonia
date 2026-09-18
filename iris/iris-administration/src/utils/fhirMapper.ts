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

import type {
  Practitioner,
  PractitionerRole,
  Organization,
  Location,
  HealthcareService,
  Endpoint,
  Group,
  Task
} from '../models/fhir';
import type {
  ProviderSummaryView,
  ProviderRoleView,
  OrganizationSummaryView,
  LocationSummaryView,
  HealthcareServiceSummaryView,
  EndpointSummaryView,
  GroupSummaryView,
  ChangeRequestSummaryView,
  ResourceDiffEntry
} from '../models/provider';
import { parseOperationOutcome } from '../models/operationOutcome';

export function mapPractitionerToSummary(p: Practitioner, rolesCount: number = 0): ProviderSummaryView {
  const name = p.name && p.name.length > 0
    ? (p.name[0].text || `${(p.name[0].prefix || []).join(' ')} ${(p.name[0].given || []).join(' ')} ${p.name[0].family || ''}`.trim())
    : (p.id || 'Unknown Practitioner');

  const identifier = p.identifier && p.identifier.length > 0
    ? `${p.identifier[0].type?.text || p.identifier[0].system || 'ID'}: ${p.identifier[0].value || ''}`
    : (p.id || '');

  const qualifications = (p.qualification || []).map(q => q.code?.text || q.code?.coding?.[0]?.display || 'Qualification');

  const telecom = (p.telecom || []).map(t => ({
    system: t.system || 'other',
    value: t.value || '',
    use: t.use
  }));

  const address = p.address && p.address.length > 0
    ? `${(p.address[0].line || []).join(', ')} ${p.address[0].city || ''} ${p.address[0].state || ''} ${p.address[0].postalCode || ''}`.trim()
    : undefined;

  return {
    id: p.id || '',
    name,
    identifier,
    gender: p.gender || 'unknown',
    birthDate: p.birthDate,
    active: p.active !== false,
    qualifications,
    rolesCount,
    telecom,
    address,
    lastUpdated: p.meta?.lastUpdated
  };
}

export function mapPractitionerRoleToView(
  r: PractitionerRole, 
  orgNameMap: Record<string, string> = {}
): ProviderRoleView {
  const orgRef = r.organization?.reference || '';
  const orgId = orgRef.replace(/^Organization\//, '');
  const organizationName = r.organization?.display || orgNameMap[orgId] || (orgId ? `Org #${orgId}` : 'Independent Practice');

  const practitionerRef = r.practitioner?.reference || '';
  const practitionerId = practitionerRef.replace(/^Practitioner\//, '');

  const code = (r.code || []).map(c => c.text || c.coding?.[0]?.display || c.coding?.[0]?.code || '').filter(Boolean).join(', ') || 'Healthcare Practitioner';
  const specialty = (r.specialty || []).map(s => s.text || s.coding?.[0]?.display || s.coding?.[0]?.code || '').filter(Boolean);
  const locationNames = (r.location || []).map(l => l.display || l.reference?.replace(/^Location\//, '') || '');
  const serviceNames = (r.healthcareService || []).map(h => h.display || h.reference?.replace(/^HealthcareService\//, '') || '');

  const telecom = (r.telecom || []).map(t => ({
    system: t.system || 'other',
    value: t.value || '',
    use: t.use
  }));

  return {
    id: r.id || '',
    practitionerId,
    practitionerName: r.practitioner?.display,
    organizationId: orgId,
    organizationName,
    code,
    specialty,
    locationNames,
    serviceNames,
    active: r.active !== false,
    telecom,
    period: r.period
  };
}

export function mapOrganizationToSummary(org: Organization, partOfName?: string): OrganizationSummaryView {
  const identifier = org.identifier && org.identifier.length > 0
    ? `${org.identifier[0].type?.text || org.identifier[0].system || 'HPI-O'}: ${org.identifier[0].value || ''}`
    : (org.id || '');

  const type = (org.type || []).map(t => t.text || t.coding?.[0]?.display || '').filter(Boolean).join(', ') || 'Healthcare Organization';
  const phone = org.telecom?.find(t => t.system === 'phone')?.value;
  const email = org.telecom?.find(t => t.system === 'email')?.value;
  const address = org.address && org.address.length > 0
    ? `${(org.address[0].line || []).join(', ')} ${org.address[0].city || ''} ${org.address[0].state || ''}`.trim()
    : undefined;

  return {
    id: org.id || '',
    name: org.name || `Org #${org.id}`,
    identifier,
    type,
    active: org.active !== false,
    phone,
    email,
    address,
    partOfName: partOfName || org.partOf?.display
  };
}

export function mapLocationToSummary(loc: Location): LocationSummaryView {
  const address = loc.address && (loc.address.line?.length || loc.address.city)
    ? `${(loc.address.line || []).join(', ')} ${loc.address.city || ''} ${loc.address.state || ''}`.trim()
    : undefined;
  const telecom = loc.telecom?.map(t => t.value).filter(Boolean).join(', ');

  return {
    id: loc.id || '',
    name: loc.name || `Location #${loc.id}`,
    status: loc.status || 'active',
    mode: loc.mode,
    physicalType: loc.physicalType?.text || loc.physicalType?.coding?.[0]?.display,
    organizationName: loc.managingOrganization?.display,
    address,
    telecom
  };
}

export function mapHealthcareServiceToSummary(hs: HealthcareService): HealthcareServiceSummaryView {
  const category = (hs.category || []).map(c => c.text || c.coding?.[0]?.display || '').filter(Boolean).join(', ') || 'Clinical Service';
  const specialty = (hs.specialty || []).map(s => s.text || s.coding?.[0]?.display || '').filter(Boolean);
  const locationNames = (hs.location || []).map(l => l.display || l.reference || '').filter(Boolean);
  const telecom = hs.telecom?.map(t => t.value).filter(Boolean).join(', ');

  return {
    id: hs.id || '',
    name: hs.name || `Service #${hs.id}`,
    category,
    specialty,
    organizationName: hs.providedBy?.display,
    active: hs.active !== false,
    locationNames,
    telecom
  };
}

export function mapEndpointToSummary(ep: Endpoint): EndpointSummaryView {
  const connectionType = ep.connectionType?.map(c => c.text || c.coding?.[0]?.display || c.coding?.[0]?.code || '').filter(Boolean).join(', ') || 'Direct / REST';
  const payloadTypes = (ep.payload || []).map(p => p.type?.map(t => t.text || t.coding?.[0]?.display || '').filter(Boolean).join(', ') || '').filter(Boolean);

  return {
    id: ep.id || '',
    name: ep.name || `Endpoint #${ep.id}`,
    status: ep.status || 'active',
    connectionType,
    address: ep.address || '',
    organizationName: ep.managingOrganization?.display,
    payloadTypes
  };
}

export function mapGroupToSummary(grp: Group): GroupSummaryView {
  return {
    id: grp.id || '',
    name: grp.name || `Group #${grp.id}`,
    type: grp.type || 'practitioner',
    membership: grp.membership || 'enumerated',
    active: grp.active !== false,
    memberCount: grp.member?.length || grp.quantity || 0
  };
}

export function mapTaskToChangeRequest(task: Task, targetResource?: any): ChangeRequestSummaryView {
  // Extract inputs
  const opInput = task.input?.find(i => i.type?.text === 'operation' || i.type?.coding?.[0]?.code === 'operation');
  const operation = (opInput?.valueCode || opInput?.valueString || 'UPDATE').toUpperCase() as 'CREATE' | 'UPDATE';

  const resTypeInput = task.input?.find(i => i.type?.text === 'resourceType' || i.type?.coding?.[0]?.code === 'resourceType');
  const resourceType = resTypeInput?.valueCode || resTypeInput?.valueString || task.focus?.type || 'Practitioner';

  const resIdInput = task.input?.find(i => i.type?.text === 'resourceId' || i.type?.coding?.[0]?.code === 'resourceId');
  const resourceId = resIdInput?.valueString || task.focus?.reference?.split('/')[1];

  const payloadInput = task.input?.find(i => i.type?.text === 'payload' || i.valueResource);
  const payload = payloadInput?.valueResource || (payloadInput?.valueString ? JSON.parse(payloadInput.valueString) : null);

  // Status mapping
  const statusStr = (task.status || 'requested').toUpperCase();
  let status: ChangeRequestSummaryView['status'] = 'REQUESTED';
  if (['COMPLETED'].includes(statusStr)) status = 'COMPLETED';
  else if (['REJECTED'].includes(statusStr)) status = 'REJECTED';
  else if (['FAILED'].includes(statusStr)) status = 'FAILED';
  else if (['IN-PROGRESS', 'IN_PROGRESS'].includes(statusStr)) status = 'IN_PROGRESS';
  else if (['ACCEPTED'].includes(statusStr)) status = 'ACCEPTED';
  else if (['DRAFT'].includes(statusStr)) status = 'DRAFT';
  else if (['CANCELLED'].includes(statusStr)) status = 'CANCELLED';

  const requester = task.requester?.display || task.requester?.reference || 'Current Provider';
  const correlationId = task.identifier?.find(i => i.system?.includes('correlation'))?.value || `CORR-${task.id || 'auto'}`;

  // Check for OperationOutcome output
  const outcomeOutput = task.output?.find(o => o.type?.coding?.[0]?.code === 'validationOutcome' || o.valueResource?.resourceType === 'OperationOutcome');
  const outcomeIssues = outcomeOutput?.valueResource ? parseOperationOutcome(outcomeOutput.valueResource as any) : [];

  return {
    taskId: task.id || '',
    correlationId,
    resourceType,
    resourceId,
    operation,
    status,
    submittedAt: task.authoredOn || new Date().toISOString(),
    requester,
    diagnostics: task.statusReason?.text,
    outcomeIssues,
    payload,
    targetExistingResource: targetResource
  };
}

export function computeResourceDiff(existing: any, proposed: any): ResourceDiffEntry[] {
  if (!existing && !proposed) return [];
  const diffs: ResourceDiffEntry[] = [];

  const allKeys = new Set([...Object.keys(existing || {}), ...Object.keys(proposed || {})]);
  const ignoredKeys = ['meta', 'implicitRules', 'resourceType'];

  for (const key of Array.from(allKeys)) {
    if (ignoredKeys.includes(key)) continue;

    const oldVal = existing ? existing[key] : undefined;
    const newVal = proposed ? proposed[key] : undefined;

    const oldStr = oldVal !== undefined ? (typeof oldVal === 'object' ? JSON.stringify(oldVal, null, 2) : String(oldVal)) : '';
    const newStr = newVal !== undefined ? (typeof newVal === 'object' ? JSON.stringify(newVal, null, 2) : String(newVal)) : '';

    if (oldVal === undefined && newVal !== undefined) {
      diffs.push({ field: key, label: formatFieldLabel(key), oldValue: '-', newValue: newStr, status: 'added' });
    } else if (oldVal !== undefined && newVal === undefined) {
      diffs.push({ field: key, label: formatFieldLabel(key), oldValue: oldStr, newValue: '-', status: 'removed' });
    } else if (oldStr !== newStr) {
      diffs.push({ field: key, label: formatFieldLabel(key), oldValue: oldStr, newValue: newStr, status: 'modified' });
    } else {
      diffs.push({ field: key, label: formatFieldLabel(key), oldValue: oldStr, newValue: newStr, status: 'unchanged' });
    }
  }

  return diffs;
}

function formatFieldLabel(key: string): string {
  return key.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase());
}
