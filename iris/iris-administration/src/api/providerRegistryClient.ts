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

import { apiClient } from './client';
import type {
  Practitioner,
  PractitionerRole,
  Organization,
  Location,
  HealthcareService,
  Endpoint,
  Group,
  Bundle,
  BaseResource
} from '../models/fhir';
import { safeLogger } from '../utils/safeLogger';

// Initial authoritative reference dataset for Provider Registry
export const INITIAL_PRACTITIONERS: Practitioner[] = [
  {
    resourceType: 'Practitioner',
    id: 'PR-1001',
    active: true,
    identifier: [
      { system: 'http://ns.electronichealth.net.au/id/hi/hpii/1.0', value: '8003610833334444', type: { text: 'HPI-I' } },
      { system: 'http://medicare.gov.au/provider-number', value: '4567891A', type: { text: 'Medicare Provider' } }
    ],
    name: [{ family: 'Chen', given: ['Sarah', 'Ying'], prefix: ['Dr'], text: 'Dr. Sarah Ying Chen, MD' }],
    gender: 'female',
    birthDate: '1984-06-15',
    telecom: [
      { system: 'email', value: 's.chen@metropolitan-health.org', use: 'work' },
      { system: 'phone', value: '+61 3 9412 8800', use: 'work' }
    ],
    address: [{ line: ['Suite 402, 120 Victoria Parade'], city: 'East Melbourne', state: 'VIC', postalCode: '3002', country: 'AUS' }],
    qualification: [
      { code: { text: 'MBBS (Hons), FRACGP', coding: [{ system: 'http://snomed.info/sct', code: '394802001', display: 'General practice' }] } }
    ],
    meta: { versionId: '1', lastUpdated: '2026-09-01T08:30:00Z' }
  },
  {
    resourceType: 'Practitioner',
    id: 'PR-1002',
    active: true,
    identifier: [
      { system: 'http://ns.electronichealth.net.au/id/hi/hpii/1.0', value: '8003610911223344', type: { text: 'HPI-I' } },
      { system: 'http://medicare.gov.au/provider-number', value: '7891234B', type: { text: 'Medicare Provider' } }
    ],
    name: [{ family: 'Vance', given: ['Marcus', 'Alexander'], prefix: ['Dr'], text: 'Dr. Marcus Vance, FRACP' }],
    gender: 'male',
    birthDate: '1979-11-20',
    telecom: [
      { system: 'email', value: 'm.vance@regional-network.org', use: 'work' },
      { system: 'phone', value: '+61 2 9382 7111', use: 'work' }
    ],
    address: [{ line: ['Level 7, Specialist Tower, 50 High Street'], city: 'Randwick', state: 'NSW', postalCode: '2031', country: 'AUS' }],
    qualification: [
      { code: { text: 'MD, FRACP (Cardiology)', coding: [{ system: 'http://snomed.info/sct', code: '394579002', display: 'Cardiology' }] } }
    ],
    meta: { versionId: '3', lastUpdated: '2026-08-15T14:20:00Z' }
  },
  {
    resourceType: 'Practitioner',
    id: 'PR-1003',
    active: true,
    identifier: [
      { system: 'http://ns.electronichealth.net.au/id/hi/hpii/1.0', value: '8003610555667788', type: { text: 'HPI-I' } }
    ],
    name: [{ family: 'Patel', given: ['Aarav'], prefix: ['Dr'], text: 'Dr. Aarav Patel' }],
    gender: 'male',
    birthDate: '1990-03-12',
    telecom: [{ system: 'email', value: 'a.patel@cityhealth.org', use: 'work' }],
    qualification: [
      { code: { text: 'BSc (Med), MBBS, FANZCA', coding: [{ system: 'http://snomed.info/sct', code: '394577000', display: 'Anaesthetics' }] } }
    ],
    meta: { versionId: '1', lastUpdated: '2026-07-10T11:00:00Z' }
  }
];

export const INITIAL_ORGANIZATIONS: Organization[] = [
  {
    resourceType: 'Organization',
    id: 'ORG-001',
    active: true,
    name: 'Metropolitan Health Network',
    type: [{ text: 'Public Healthcare Network', coding: [{ system: 'http://hl7.org/fhir/organization-type', code: 'prov', display: 'Healthcare Provider' }] }],
    identifier: [{ system: 'http://ns.electronichealth.net.au/id/hi/hpio/1.0', value: '8003621566778899', type: { text: 'HPI-O' } }],
    telecom: [{ system: 'phone', value: '+61 3 9412 8000' }, { system: 'email', value: 'contact@metropolitan-health.org' }],
    address: [{ line: ['100 Victoria Parade'], city: 'East Melbourne', state: 'VIC', postalCode: '3002', country: 'AUS' }],
    meta: { versionId: '1', lastUpdated: '2026-06-01T09:00:00Z' }
  },
  {
    resourceType: 'Organization',
    id: 'ORG-002',
    active: true,
    name: 'Regional Care Network',
    type: [{ text: 'Regional Health Service', coding: [{ system: 'http://hl7.org/fhir/organization-type', code: 'prov', display: 'Healthcare Provider' }] }],
    identifier: [{ system: 'http://ns.electronichealth.net.au/id/hi/hpio/1.0', value: '8003629911223344', type: { text: 'HPI-O' } }],
    telecom: [{ system: 'phone', value: '+61 2 9382 7000' }],
    address: [{ line: ['50 High Street'], city: 'Randwick', state: 'NSW', postalCode: '2031', country: 'AUS' }],
    meta: { versionId: '2', lastUpdated: '2026-07-15T10:30:00Z' }
  }
];

export const INITIAL_ROLES: PractitionerRole[] = [
  {
    resourceType: 'PractitionerRole',
    id: 'PRR-5001',
    active: true,
    practitioner: { reference: 'Practitioner/PR-1001', display: 'Dr. Sarah Ying Chen, MD' },
    organization: { reference: 'Organization/ORG-001', display: 'Metropolitan Health Network' },
    code: [{ text: 'General Practitioner', coding: [{ system: 'http://snomed.info/sct', code: '309343006', display: 'Physician' }] }],
    specialty: [{ text: 'Family Medicine & Chronic Care', coding: [{ system: 'http://snomed.info/sct', code: '394802001', display: 'General practice' }] }],
    telecom: [{ system: 'email', value: 's.chen@metropolitan-health.org', use: 'work' }, { system: 'phone', value: '+61 3 9412 8801', use: 'work' }],
    location: [{ reference: 'Location/LOC-001', display: 'Metropolitan General Hospital - Wing A' }],
    healthcareService: [{ reference: 'HealthcareService/HS-001', display: 'Outpatient Primary Care Clinic' }],
    endpoint: [{ reference: 'Endpoint/EP-001', display: 'Metropolitan Secure Message Delivery (SMD)' }],
    meta: { versionId: '2', lastUpdated: '2026-09-02T10:00:00Z' }
  },
  {
    resourceType: 'PractitionerRole',
    id: 'PRR-5002',
    active: true,
    practitioner: { reference: 'Practitioner/PR-1002', display: 'Dr. Marcus Vance, FRACP' },
    organization: { reference: 'Organization/ORG-002', display: 'Regional Care Network' },
    code: [{ text: 'Consultant Cardiologist', coding: [{ system: 'http://snomed.info/sct', code: '17561000', display: 'Cardiologist' }] }],
    specialty: [{ text: 'Interventional Cardiology', coding: [{ system: 'http://snomed.info/sct', code: '394579002', display: 'Cardiology' }] }],
    telecom: [{ system: 'email', value: 'm.vance@regional-network.org', use: 'work' }],
    location: [{ reference: 'Location/LOC-002', display: 'Regional Specialist Centre' }],
    healthcareService: [{ reference: 'HealthcareService/HS-002', display: 'Cardiovascular Diagnostic Unit' }],
    meta: { versionId: '1', lastUpdated: '2026-08-20T12:00:00Z' }
  }
];

export const INITIAL_LOCATIONS: Location[] = [
  {
    resourceType: 'Location',
    id: 'LOC-001',
    status: 'active',
    name: 'Metropolitan General Hospital - Wing A',
    mode: 'instance',
    physicalType: { text: 'Building Wing', coding: [{ system: 'http://terminology.hl7.org/CodeSystem/location-physical-type', code: 'wi', display: 'Wing' }] },
    managingOrganization: { reference: 'Organization/ORG-001', display: 'Metropolitan Health Network' },
    address: { line: ['100 Victoria Parade'], city: 'East Melbourne', state: 'VIC', postalCode: '3002' },
    telecom: [{ system: 'phone', value: '+61 3 9412 8001' }]
  },
  {
    resourceType: 'Location',
    id: 'LOC-002',
    status: 'active',
    name: 'Regional Specialist Centre',
    mode: 'instance',
    physicalType: { text: 'Clinic Facility', coding: [{ system: 'http://terminology.hl7.org/CodeSystem/location-physical-type', code: 'bu', display: 'Building' }] },
    managingOrganization: { reference: 'Organization/ORG-002', display: 'Regional Care Network' },
    address: { line: ['50 High Street'], city: 'Randwick', state: 'NSW', postalCode: '2031' }
  }
];

export const INITIAL_SERVICES: HealthcareService[] = [
  {
    resourceType: 'HealthcareService',
    id: 'HS-001',
    active: true,
    name: 'Outpatient Primary Care Clinic',
    category: [{ text: 'General Practice' }],
    specialty: [{ text: 'Family Medicine' }],
    providedBy: { reference: 'Organization/ORG-001', display: 'Metropolitan Health Network' },
    location: [{ reference: 'Location/LOC-001', display: 'Metropolitan General Hospital - Wing A' }],
    telecom: [{ system: 'phone', value: '+61 3 9412 8850' }]
  },
  {
    resourceType: 'HealthcareService',
    id: 'HS-002',
    active: true,
    name: 'Cardiovascular Diagnostic Unit',
    category: [{ text: 'Specialist Cardiology' }],
    specialty: [{ text: 'Echocardiography & Angiography' }],
    providedBy: { reference: 'Organization/ORG-002', display: 'Regional Care Network' },
    location: [{ reference: 'Location/LOC-002', display: 'Regional Specialist Centre' }]
  }
];

export const INITIAL_ENDPOINTS: Endpoint[] = [
  {
    resourceType: 'Endpoint',
    id: 'EP-001',
    status: 'active',
    name: 'Metropolitan Secure Message Delivery (SMD)',
    connectionType: [{ text: 'HL7 Secure Message Delivery (SMD)', coding: [{ system: 'http://terminology.hl7.org/CodeSystem/endpoint-connection-type', code: 'hl7-fhir-rest' }] }],
    address: 'https://smd.metropolitan-health.org/fhir/v1',
    managingOrganization: { reference: 'Organization/ORG-001', display: 'Metropolitan Health Network' },
    payload: [{ type: [{ text: 'FHIR R5 Messaging' }], mimeType: ['application/fhir+json'] }]
  },
  {
    resourceType: 'Endpoint',
    id: 'EP-002',
    status: 'active',
    name: 'Regional Care Direct Referral Gateway',
    connectionType: [{ text: 'Direct REST Endpoint', coding: [{ system: 'http://terminology.hl7.org/CodeSystem/endpoint-connection-type', code: 'direct-project' }] }],
    address: 'https://gateway.regional-network.org/api/referrals',
    managingOrganization: { reference: 'Organization/ORG-002', display: 'Regional Care Network' },
    payload: [{ type: [{ text: 'Referral Document' }], mimeType: ['application/json'] }]
  }
];

export const INITIAL_GROUPS: Group[] = [
  {
    resourceType: 'Group',
    id: 'GRP-001',
    active: true,
    type: 'practitioner',
    membership: 'enumerated',
    name: 'Metropolitan Acute Care Physician Panel',
    quantity: 2,
    managingEntity: { reference: 'Organization/ORG-001', display: 'Metropolitan Health Network' },
    member: [
      { entity: { reference: 'Practitioner/PR-1001', display: 'Dr. Sarah Ying Chen, MD' } },
      { entity: { reference: 'Practitioner/PR-1003', display: 'Dr. Aarav Patel' } }
    ]
  }
];

// In-Memory store for standalone reactive UI demonstration and testing
class ProviderRegistryRepository {
  private practitioners: Map<string, Practitioner> = new Map();
  private roles: Map<string, PractitionerRole> = new Map();
  private organizations: Map<string, Organization> = new Map();
  private locations: Map<string, Location> = new Map();
  private services: Map<string, HealthcareService> = new Map();
  private endpoints: Map<string, Endpoint> = new Map();
  private groups: Map<string, Group> = new Map();

  constructor() {
    this.reset();
  }

  public reset() {
    this.practitioners.clear();
    INITIAL_PRACTITIONERS.forEach(p => this.practitioners.set(p.id!, { ...p }));

    this.organizations.clear();
    INITIAL_ORGANIZATIONS.forEach(o => this.organizations.set(o.id!, { ...o }));

    this.roles.clear();
    INITIAL_ROLES.forEach(r => this.roles.set(r.id!, { ...r }));

    this.locations.clear();
    INITIAL_LOCATIONS.forEach(l => this.locations.set(l.id!, { ...l }));

    this.services.clear();
    INITIAL_SERVICES.forEach(s => this.services.set(s.id!, { ...s }));

    this.endpoints.clear();
    INITIAL_ENDPOINTS.forEach(e => this.endpoints.set(e.id!, { ...e }));

    this.groups.clear();
    INITIAL_GROUPS.forEach(g => this.groups.set(g.id!, { ...g }));
  }

  // Generic resource access
  private getMap(resourceType: string): Map<string, any> {
    switch (resourceType) {
      case 'Practitioner': return this.practitioners;
      case 'PractitionerRole': return this.roles;
      case 'Organization': return this.organizations;
      case 'Location': return this.locations;
      case 'HealthcareService': return this.services;
      case 'Endpoint': return this.endpoints;
      case 'Group': return this.groups;
      default: throw new Error(`Unsupported resource type: ${resourceType}`);
    }
  }

  public async get<T extends BaseResource>(resourceType: string, id: string): Promise<T | null> {
    try {
      const response = await apiClient.get<T>(`/fhir/${resourceType}/${id}`);
      return response.data;
    } catch {
      // Fallback to local store
      const map = this.getMap(resourceType);
      const found = map.get(id);
      return found ? { ...found } as T : null;
    }
  }

  public async search<T extends BaseResource>(resourceType: string, params: Record<string, string> = {}): Promise<T[]> {
    try {
      const response = await apiClient.get<Bundle<T>>(`/fhir/${resourceType}`, { params });
      if (response.data && response.data.entry) {
        return response.data.entry.map(e => e.resource!).filter(Boolean);
      }
      return [];
    } catch {
      // Fallback to local store search
      const map = this.getMap(resourceType);
      let items: T[] = Array.from(map.values()) as T[];

      if (params.name) {
        const query = params.name.toLowerCase();
        items = items.filter(item => {
          const itemStr = JSON.stringify(item).toLowerCase();
          return itemStr.includes(query);
        });
      }
      if (params.active !== undefined) {
        const isActive = params.active === 'true';
        items = items.filter((item: any) => item.active === isActive || (item.status && item.status === 'active') === isActive);
      }
      if (params.practitioner) {
        items = items.filter((item: any) => item.practitioner?.reference?.includes(params.practitioner));
      }
      if (params.organization) {
        items = items.filter((item: any) => item.organization?.reference?.includes(params.organization) || item.managingOrganization?.reference?.includes(params.organization));
      }

      return items;
    }
  }

  public async save<T extends BaseResource>(resource: T): Promise<T> {
    const resourceType = resource.resourceType;
    const map = this.getMap(resourceType);

    if (!resource.id) {
      resource.id = `${resourceType.substring(0, 3).toUpperCase()}-${Date.now().toString().substring(7)}`;
    }

    const currentVersion = parseInt(resource.meta?.versionId || '0', 10) + 1;
    resource.meta = {
      ...resource.meta,
      versionId: String(currentVersion),
      lastUpdated: new Date().toISOString()
    };

    map.set(resource.id, { ...resource });
    safeLogger.info(`Saved ${resourceType}/${resource.id} (Version ${currentVersion})`);
    return { ...resource };
  }

  public async remove(resourceType: string, id: string): Promise<boolean> {
    const map = this.getMap(resourceType);
    const existing = map.get(id);
    if (existing) {
      existing.active = false;
      if (existing.status) existing.status = 'inactive';
      map.set(id, existing);
      return true;
    }
    return false;
  }
}

export const providerRegistryClient = new ProviderRegistryRepository();
