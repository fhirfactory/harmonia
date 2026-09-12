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

export interface Meta {
  versionId?: string;
  lastUpdated?: string;
  source?: string;
  profile?: string[];
}

export interface Identifier {
  use?: string;
  system?: string;
  value?: string;
}

export interface HumanName {
  use?: string;
  text?: string;
  family?: string;
  given?: string[];
  prefix?: string[];
  suffix?: string[];
}

export interface ContactPoint {
  system?: 'phone' | 'fax' | 'email' | 'pager' | 'url' | 'sms' | 'other';
  value?: string;
  use?: 'home' | 'work' | 'temp' | 'old' | 'mobile';
}

export interface Address {
  use?: 'home' | 'work' | 'temp' | 'old' | 'billing';
  line?: string[];
  city?: string;
  state?: string;
  postalCode?: string;
  country?: string;
}

export interface CodeableConcept {
  text?: string;
  coding?: Array<{
    system?: string;
    code?: string;
    display?: string;
  }>;
}

export interface Reference {
  reference?: string;
  type?: string;
  display?: string;
}

export interface BaseResource {
  id?: string;
  resourceType: string;
  meta?: Meta;
  implicitRules?: string;
  language?: string;
}

export interface Person extends BaseResource {
  resourceType: 'Person';
  identifier?: Identifier[];
  name?: HumanName[];
  telecom?: ContactPoint[];
  gender?: 'male' | 'female' | 'other' | 'unknown';
  birthDate?: string;
  address?: Address[];
  active?: boolean;
}

export interface RelatedPerson extends BaseResource {
  resourceType: 'RelatedPerson';
  identifier?: Identifier[];
  active?: boolean;
  patient?: Reference;
  relationship?: CodeableConcept[];
  name?: HumanName[];
  telecom?: ContactPoint[];
  gender?: 'male' | 'female' | 'other' | 'unknown';
  birthDate?: string;
  address?: Address[];
}

export interface Practitioner extends BaseResource {
  resourceType: 'Practitioner';
  identifier?: Identifier[];
  active?: boolean;
  name?: HumanName[];
  telecom?: ContactPoint[];
  gender?: 'male' | 'female' | 'other' | 'unknown';
  birthDate?: string;
  qualification?: Array<{
    identifier?: Identifier[];
    code?: CodeableConcept;
    period?: { start?: string; end?: string };
    issuer?: Reference;
  }>;
}

export interface PractitionerRole extends BaseResource {
  resourceType: 'PractitionerRole';
  identifier?: Identifier[];
  active?: boolean;
  practitioner?: Reference;
  organization?: Reference;
  code?: CodeableConcept[];
  specialty?: CodeableConcept[];
  location?: Reference[];
  healthcareService?: Reference[];
  telecom?: ContactPoint[];
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
  address?: Address[];
  managingOrganization?: Reference;
  partOf?: Reference;
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
  telecom?: ContactPoint[];
}

export interface GroupMember {
  entity?: Reference;
  inactive?: boolean;
}

export interface Group extends BaseResource {
  resourceType: 'Group';
  identifier?: Identifier[];
  active?: boolean;
  type?: 'person' | 'animal' | 'practitioner' | 'device' | 'careteam' | 'healthcareservice' | 'location' | 'organization' | 'relatedperson' | 'specimen';
  membership?: 'definitional' | 'enumerated';
  name?: string;
  description?: string;
  quantity?: number;
  managingEntity?: Reference;
  member?: GroupMember[];
}

export interface ProvenanceAgent {
  type?: CodeableConcept;
  role?: CodeableConcept[];
  who?: Reference;
  onBehalfOf?: Reference;
}

export interface ProvenanceEntity {
  role?: 'revision' | 'quotation' | 'derivation' | 'instantiates' | 'source' | string;
  what?: Reference;
  agent?: ProvenanceAgent[];
}

export interface Provenance extends BaseResource {
  resourceType: 'Provenance';
  target?: Reference[];
  occurredDateTime?: string;
  recorded?: string;
  policy?: string[];
  location?: Reference;
  authorization?: CodeableConcept[];
  activity?: CodeableConcept;
  patient?: Reference;
  agent?: ProvenanceAgent[];
  entity?: ProvenanceEntity[];
}

export interface AuditEventAgent {
  type?: CodeableConcept;
  role?: CodeableConcept[];
  who?: Reference;
  requestor?: boolean;
  location?: Reference;
  policy?: string[];
}

export interface AuditEvent extends BaseResource {
  resourceType: 'AuditEvent';
  type?: CodeableConcept;
  subtype?: CodeableConcept[];
  action?: 'C' | 'R' | 'U' | 'D' | 'E';
  severity?: 'emergency' | 'alert' | 'critical' | 'error' | 'warning' | 'notice' | 'informational' | 'debug';
  occurredDateTime?: string;
  recorded?: string;
  outcome?: {
    code?: CodeableConcept;
    detail?: CodeableConcept[];
  };
  code?: CodeableConcept;
  agent?: AuditEventAgent[];
}

export interface Consent extends BaseResource {
  resourceType: 'Consent';
  identifier?: Identifier[];
  status?: 'draft' | 'active' | 'inactive' | 'not-done' | 'entered-in-error' | 'unknown';
  category?: CodeableConcept[];
  subject?: Reference;
  date?: string;
  period?: { start?: string; end?: string };
  decision?: 'deny' | 'permit';
}

export interface Task extends BaseResource {
  resourceType: 'Task';
  identifier?: Identifier[];
  status?: 'draft' | 'requested' | 'received' | 'accepted' | 'rejected' | 'ready' | 'cancelled' | 'in-progress' | 'on-hold' | 'failed' | 'completed' | 'entered-in-error';
  statusReason?: CodeableConcept;
  intent?: 'unknown' | 'proposal' | 'plan' | 'order' | 'original-order' | 'reflex-order' | 'filler-order' | 'instance-order' | 'option';
  priority?: 'routine' | 'urgent' | 'asap' | 'stat';
  description?: string;
  focus?: Reference;
  for?: Reference;
  authoredOn?: string;
  lastModified?: string;
  requester?: Reference;
  owner?: Reference;
  note?: Array<{ text?: string }>;
}

export interface Communication extends BaseResource {
  resourceType: 'Communication';
  identifier?: Identifier[];
  status?: 'preparation' | 'in-progress' | 'not-done' | 'on-hold' | 'stopped' | 'completed' | 'entered-in-error' | 'unknown';
  statusReason?: CodeableConcept;
  category?: CodeableConcept[];
  priority?: 'routine' | 'urgent' | 'asap' | 'stat';
  subject?: Reference;
  sent?: string;
  received?: string;
  recipient?: Reference[];
  sender?: Reference;
  note?: Array<{ text?: string }>;
}

export interface DocumentReference extends BaseResource {
  resourceType: 'DocumentReference';
  identifier?: Identifier[];
  status?: 'current' | 'superseded' | 'entered-in-error';
  docStatus?: 'registered' | 'partial' | 'preliminary' | 'final' | 'amended' | 'corrected' | 'appended' | 'cancelled' | 'entered-in-error' | 'deprecated' | 'unknown';
  type?: CodeableConcept;
  category?: CodeableConcept[];
  subject?: Reference;
  date?: string;
  author?: Reference[];
  description?: string;
  content?: Array<{
    attachment?: {
      contentType?: string;
      title?: string;
      url?: string;
    };
  }>;
}

export interface BundleEntry<T extends BaseResource = BaseResource> {
  fullUrl?: string;
  resource: T;
}

export interface Bundle<T extends BaseResource = BaseResource> extends BaseResource {
  resourceType: 'Bundle';
  type: 'searchset' | 'transaction' | 'batch' | 'history' | 'collection';
  total?: number;
  entry?: BundleEntry<T>[];
}
