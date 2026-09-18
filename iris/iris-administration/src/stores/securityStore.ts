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

import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { ThemisPrincipal, ThemisAuthority, PersonaProfile } from '../models/themis';

export const PERSONA_PROFILES: PersonaProfile[] = [
  {
    id: 'provider-chen',
    name: 'Dr. Sarah Chen (Provider)',
    description: 'General Practitioner with Self-Service credentials update capability',
    principal: {
      id: 'USR-PRV-1001',
      username: 'dr.schen',
      displayName: 'Dr. Sarah Chen, MD',
      email: 's.chen@metropolitan-health.org',
      practitionerId: 'PR-1001',
      organizationId: 'ORG-001',
      authorities: ['provider.read', 'provider.resource.update']
    }
  },
  {
    id: 'officer-miller',
    name: 'David Miller (Departmental Officer)',
    description: 'Registry Officer with search, entity management, and work queue review rights',
    principal: {
      id: 'USR-ADM-2001',
      username: 'd.miller',
      displayName: 'David Miller (Registry Officer)',
      email: 'd.miller@dept-health.gov',
      authorities: ['provider.read', 'provider.search', 'provider.change.process', 'provider.resource.create', 'provider.resource.update']
    }
  },
  {
    id: 'multirole-vance',
    name: 'Dr. Marcus Vance (Clinical Director / Multi-Role)',
    description: 'Senior Clinician with both provider self-service and administrative review authorities',
    principal: {
      id: 'USR-DIR-3001',
      username: 'dr.mvance',
      displayName: 'Dr. Marcus Vance, FRACP',
      email: 'm.vance@regional-network.org',
      practitionerId: 'PR-1002',
      organizationId: 'ORG-002',
      authorities: ['provider.read', 'provider.search', 'provider.change.process', 'provider.resource.create', 'provider.resource.update']
    }
  },
  {
    id: 'system-admin',
    name: 'System Administrator (Full Admin)',
    description: 'Harmonia Security Superuser with provider.admin override',
    principal: {
      id: 'USR-SYS-9999',
      username: 'sysadmin',
      displayName: 'System Administrator',
      email: 'admin@harmonia.internal',
      authorities: ['provider.admin', 'provider.read', 'provider.search', 'provider.change.process', 'provider.resource.create', 'provider.resource.update', 'provider.resource.delete']
    }
  }
];

export const useSecurityStore = defineStore('security', () => {
  const currentPersonaId = ref<string>(PERSONA_PROFILES[0].id);
  const principal = ref<ThemisPrincipal>({ ...PERSONA_PROFILES[0].principal });
  const isAuthenticated = ref<boolean>(true);
  const correlationId = ref<string>(`CORR-${Date.now()}-${Math.random().toString(36).substring(2, 7)}`);
  const sourceSystem = ref<string>('iris-administration');

  function setPersona(personaId: string) {
    const found = PERSONA_PROFILES.find(p => p.id === personaId);
    if (found) {
      currentPersonaId.value = found.id;
      principal.value = { ...found.principal };
      isAuthenticated.value = true;
      refreshCorrelationId();
    }
  }

  function setUnauthenticated() {
    isAuthenticated.value = false;
    principal.value = {
      id: 'ANON',
      username: 'anonymous',
      displayName: 'Guest / Unauthenticated',
      email: '',
      authorities: []
    };
  }

  function refreshCorrelationId(): string {
    const id = `CORR-${Date.now()}-${Math.random().toString(36).substring(2, 7)}`;
    correlationId.value = id;
    return id;
  }

  function hasAuthority(authority: ThemisAuthority): boolean {
    if (!isAuthenticated.value || !principal.value) return false;
    if (principal.value.authorities.includes('provider.admin')) return true;
    return principal.value.authorities.includes(authority);
  }

  function hasAnyAuthority(authorities: ThemisAuthority[]): boolean {
    if (!isAuthenticated.value || !principal.value) return false;
    if (principal.value.authorities.includes('provider.admin')) return true;
    return authorities.some(a => principal.value.authorities.includes(a));
  }

  const canSelfService = computed(() => {
    return isAuthenticated.value && (hasAuthority('provider.read') || !!principal.value.practitionerId);
  });

  const canSearch = computed(() => {
    return isAuthenticated.value && (hasAuthority('provider.search') || hasAuthority('provider.read'));
  });

  const canAdminister = computed(() => {
    return isAuthenticated.value && (hasAuthority('provider.search') || hasAuthority('provider.admin') || hasAuthority('provider.change.process'));
  });

  const canProcessWorkQueue = computed(() => {
    return isAuthenticated.value && (hasAuthority('provider.change.process') || hasAuthority('provider.admin'));
  });

  const canMutateResources = computed(() => {
    return isAuthenticated.value && (hasAuthority('provider.resource.create') || hasAuthority('provider.resource.update') || hasAuthority('provider.admin'));
  });

  return {
    principal,
    isAuthenticated,
    correlationId,
    sourceSystem,
    currentPersonaId,
    personaProfiles: PERSONA_PROFILES,
    setPersona,
    setUnauthenticated,
    refreshCorrelationId,
    hasAuthority,
    hasAnyAuthority,
    canSelfService,
    canSearch,
    canAdminister,
    canProcessWorkQueue,
    canMutateResources
  };
});
