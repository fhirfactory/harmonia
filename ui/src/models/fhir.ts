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
