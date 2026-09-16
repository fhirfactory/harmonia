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

export interface Coding {
  system?: string;
  version?: string;
  code?: string;
  display?: string;
  userSelected?: boolean;
}

export interface CodeableConcept {
  text?: string;
  coding?: Coding[];
}

export interface Period {
  start?: string;
  end?: string;
}

export interface Identifier {
  use?: 'usual' | 'official' | 'temp' | 'secondary' | 'old';
  type?: CodeableConcept;
  system?: string;
  value?: string;
  period?: Period;
  assigner?: Reference;
}

export interface HumanName {
  use?: 'usual' | 'official' | 'temp' | 'nickname' | 'anonymous' | 'old' | 'maiden';
  text?: string;
  family?: string;
  given?: string[];
  prefix?: string[];
  suffix?: string[];
  period?: Period;
}

export interface ContactPoint {
  system?: 'phone' | 'fax' | 'email' | 'pager' | 'url' | 'sms' | 'other';
  value?: string;
  use?: 'home' | 'work' | 'temp' | 'old' | 'mobile';
  rank?: number;
  period?: Period;
}

export interface Address {
  use?: 'home' | 'work' | 'temp' | 'old' | 'billing';
  type?: 'postal' | 'physical' | 'both';
  text?: string;
  line?: string[];
  city?: string;
  district?: string;
  state?: string;
  postalCode?: string;
  country?: string;
  period?: Period;
}

export interface Reference {
  reference?: string;
  type?: string;
  identifier?: Identifier;
  display?: string;
}

export interface Meta {
  versionId?: string;
  lastUpdated?: string;
  source?: string;
  profile?: string[];
  security?: Coding[];
  tag?: Coding[];
}

export interface BaseResource {
  id?: string;
  resourceType: string;
  meta?: Meta;
  implicitRules?: string;
  language?: string;
}

export interface Qualification {
  identifier?: Identifier[];
  code: CodeableConcept;
  period?: Period;
  issuer?: Reference;
}

export interface Practitioner extends BaseResource {
  resourceType: 'Practitioner';
  identifier?: Identifier[];
  active?: boolean;
  name?: HumanName[];
  telecom?: ContactPoint[];
  gender?: 'male' | 'female' | 'other' | 'unknown';
  birthDate?: string;
  address?: Address[];
  qualification?: Qualification[];
  communication?: Array<{ language: CodeableConcept; preferred?: boolean }>;
}

export interface AvailableTime {
  daysOfWeek?: Array<'mon' | 'tue' | 'wed' | 'thu' | 'fri' | 'sat' | 'sun'>;
  allDay?: boolean;
  availableStartTime?: string;
  availableEndTime?: string;
}

export interface NotAvailableTime {
  description: string;
  during?: Period;
}

export interface PractitionerRole extends BaseResource {
  resourceType: 'PractitionerRole';
  identifier?: Identifier[];
  active?: boolean;
  period?: Period;
  practitioner?: Reference;
  organization?: Reference;
  code?: CodeableConcept[];
  specialty?: CodeableConcept[];
  location?: Reference[];
  healthcareService?: Reference[];
  telecom?: ContactPoint[];
  availableTime?: AvailableTime[];
  notAvailable?: NotAvailableTime[];
  endpoint?: Reference[];
}

export interface OrganizationContact {
  purpose?: CodeableConcept;
  name?: HumanName;
  telecom?: ContactPoint[];
  address?: Address;
}

export interface Organization extends BaseResource {
  resourceType: 'Organization';
  identifier?: Identifier[];
  active?: boolean;
  type?: CodeableConcept[];
  name?: string;
  alias?: string[];
  telecom?: ContactPoint[];
  address?: Address[];
  partOf?: Reference;
  contact?: OrganizationContact[];
  endpoint?: Reference[];
}

export interface LocationPosition {
  longitude: number;
  latitude: number;
  altitude?: number;
}

export interface LocationHoursOfOperation {
  daysOfWeek?: string[];
  allDay?: boolean;
  openingTime?: string;
  closingTime?: string;
}

export interface Location extends BaseResource {
  resourceType: 'Location';
  identifier?: Identifier[];
  status?: 'active' | 'suspended' | 'inactive';
  name?: string;
  alias?: string[];
  description?: string;
  mode?: 'instance' | 'kind';
  type?: CodeableConcept[];
  telecom?: ContactPoint[];
  address?: Address;
  physicalType?: CodeableConcept;
  position?: LocationPosition;
  managingOrganization?: Reference;
  partOf?: Reference;
  hoursOfOperation?: LocationHoursOfOperation[];
  endpoint?: Reference[];
}

export interface HealthcareServiceEligibility {
  code?: CodeableConcept;
  comment?: string;
}

export interface HealthcareService extends BaseResource {
  resourceType: 'HealthcareService';
  identifier?: Identifier[];
  active?: boolean;
  providedBy?: Reference;
  category?: CodeableConcept[];
  type?: CodeableConcept[];
  specialty?: CodeableConcept[];
  location?: Reference[];
  name?: string;
  comment?: string;
  extraDetails?: string;
  telecom?: ContactPoint[];
  coverageArea?: Reference[];
  serviceProvisionCode?: CodeableConcept[];
  eligibility?: HealthcareServiceEligibility[];
  program?: CodeableConcept[];
  characteristic?: CodeableConcept[];
  communication?: CodeableConcept[];
  referralMethod?: CodeableConcept[];
  appointmentRequired?: boolean;
  availableTime?: AvailableTime[];
  notAvailable?: NotAvailableTime[];
  availabilityExceptions?: string;
  endpoint?: Reference[];
}

export interface EndpointPayload {
  type?: CodeableConcept[];
  mimeType?: string[];
}

export interface Endpoint extends BaseResource {
  resourceType: 'Endpoint';
  identifier?: Identifier[];
  status?: 'active' | 'suspended' | 'error' | 'off' | 'entered-in-error' | 'test';
  connectionType: CodeableConcept[];
  name?: string;
  description?: string;
  managingOrganization?: Reference;
  contact?: ContactPoint[];
  period?: Period;
  payload?: EndpointPayload[];
  address: string;
  header?: string[];
}

export interface GroupMember {
  entity: Reference;
  period?: Period;
  inactive?: boolean;
}

export interface GroupCharacteristic {
  code: CodeableConcept;
  valueCodeableConcept?: CodeableConcept;
  valueBoolean?: boolean;
  valueQuantity?: any;
  valueRange?: any;
  valueReference?: Reference;
  exclude: boolean;
  period?: Period;
}

export interface Group extends BaseResource {
  resourceType: 'Group';
  identifier?: Identifier[];
  active?: boolean;
  type: 'person' | 'animal' | 'practitioner' | 'device' | 'medication' | 'substance';
  membership: 'definitional' | 'conceptual' | 'enumerated';
  code?: CodeableConcept;
  name?: string;
  description?: string;
  quantity?: number;
  managingEntity?: Reference;
  characteristic?: GroupCharacteristic[];
  member?: GroupMember[];
}

export interface TaskInput {
  type: CodeableConcept;
  valueString?: string;
  valueReference?: Reference;
  valueCode?: string;
  valueResource?: any;
}

export interface TaskOutput {
  type: CodeableConcept;
  valueString?: string;
  valueReference?: Reference;
  valueCode?: string;
  valueResource?: any;
}

export interface TaskRestriction {
  repetitions?: number;
  period?: Period;
  recipient?: Reference[];
}

export interface Task extends BaseResource {
  resourceType: 'Task';
  identifier?: Identifier[];
  instantiatesCanonical?: string;
  instantiatesUri?: string;
  basedOn?: Reference[];
  groupIdentifier?: Identifier;
  partOf?: Reference[];
  status: 'draft' | 'requested' | 'received' | 'accepted' | 'rejected' | 'ready' | 'cancelled' | 'in-progress' | 'on-hold' | 'failed' | 'completed' | 'entered-in-error';
  statusReason?: CodeableConcept;
  businessStatus?: CodeableConcept;
  intent: 'unknown' | 'proposal' | 'plan' | 'order' | 'original-order' | 'reflex-order' | 'filler-order' | 'instance-order' | 'option';
  priority?: 'routine' | 'urgent' | 'asap' | 'stat';
  code?: CodeableConcept;
  description?: string;
  focus?: Reference;
  for?: Reference;
  encounter?: Reference;
  executionPeriod?: Period;
  authoredOn?: string;
  lastModified?: string;
  requester?: Reference;
  performerType?: CodeableConcept[];
  owner?: Reference;
  location?: Reference;
  reasonCode?: CodeableConcept;
  reasonReference?: Reference;
  insurance?: Reference[];
  note?: Array<{ text: string; time?: string; authorString?: string }>;
  relevantHistory?: Reference[];
  restriction?: TaskRestriction;
  input?: TaskInput[];
  output?: TaskOutput[];
}

export interface OperationOutcomeIssue {
  severity: 'fatal' | 'error' | 'warning' | 'information';
  code: string;
  details?: CodeableConcept;
  diagnostics?: string;
  location?: string[];
  expression?: string[];
}

export interface OperationOutcome extends BaseResource {
  resourceType: 'OperationOutcome';
  issue: OperationOutcomeIssue[];
}

export interface BundleEntry<T = BaseResource> {
  fullUrl?: string;
  resource?: T;
  search?: {
    mode?: 'match' | 'include' | 'outcome';
    score?: number;
  };
}

export interface Bundle<T = BaseResource> extends BaseResource {
  resourceType: 'Bundle';
  type: 'document' | 'message' | 'transaction' | 'transaction-response' | 'batch' | 'batch-response' | 'history' | 'searchset' | 'collection';
  total?: number;
  entry?: BundleEntry<T>[];
}
