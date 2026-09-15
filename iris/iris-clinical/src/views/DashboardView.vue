<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.
-->

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { usePersonStore } from '../stores/personStore';
import { usePractitionerStore } from '../stores/practitionerStore';
import { useFacilityStore } from '../stores/facilityStore';
import { useGroupStore } from '../stores/groupStore';
import { useSecurityStore } from '../stores/securityStore';
import { useWorkflowStore } from '../stores/workflowStore';
import { 
  Server, Database, Cpu, Layers, HardDrive, 
  Users, UserCheck, Building2, MapPin, Stethoscope, UsersRound, 
  CheckCircle2, ArrowRight, ShieldCheck, Zap,
  GitBranch, ShieldAlert, FileCheck, CheckSquare, MessageSquare, FileText
} from 'lucide-vue-next';

const personStore = usePersonStore();
const practitionerStore = usePractitionerStore();
const facilityStore = useFacilityStore();
const groupStore = useGroupStore();
const securityStore = useSecurityStore();
const workflowStore = useWorkflowStore();

const refreshAll = async () => {
  await Promise.all([
    personStore.fetchPersons(),
    personStore.fetchRelatedPersons(),
    practitionerStore.fetchPractitioners(),
    practitionerStore.fetchPractitionerRoles(),
    facilityStore.fetchOrganizations(),
    facilityStore.fetchLocations(),
    facilityStore.fetchHealthcareServices(),
    groupStore.fetchGroups(),
    securityStore.fetchProvenances(),
    securityStore.fetchAuditEvents(),
    securityStore.fetchConsents(),
    workflowStore.fetchTasks(),
    workflowStore.fetchCommunications(),
    workflowStore.fetchDocumentReferences()
  ]);
};

onMounted(() => {
  refreshAll();
});
</script>

<template>
  <div class="space-y-6">
    <!-- Header Hero -->
    <div class="card flex flex-col md:flex-row items-start md:items-center justify-between gap-4" style="background: linear-gradient(135deg, #111827 0%, #172554 100%);">
      <div>
        <div class="flex items-center gap-2 mb-1">
          <span class="badge badge-blue flex items-center gap-1">
            <Activity :size="12" /> HL7 FHIR Release 5.0.0
          </span>
          <span class="badge badge-green flex items-center gap-1">
            <CheckCircle2 :size="12" /> Active Resource Repository
          </span>
        </div>
        <h1 class="text-2xl md:text-3xl font-extrabold text-white">FHIR R5 Resource Explorer</h1>
        <p class="subtitle mt-1">Browse, search, and manage clinical, demographic, workflow, and security FHIR resources.</p>
      </div>
      <button @click="refreshAll" class="btn btn-primary whitespace-nowrap">
        Refresh Resources
      </button>
    </div>

    <!-- 14 FHIR Resource Cards Overview -->
    <h3 class="text-lg font-bold text-white mt-6">FHIR R5 Resource Repository</h3>
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <!-- 1. Person -->
      <router-link to="/persons" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">1. Demographic</span>
          <Users :size="18" class="text-sky-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ personStore.persons.length }}</div>
        <div class="text-xs text-sky-400 mt-1 font-medium">Persons Registered</div>
      </router-link>

      <!-- 2. RelatedPerson -->
      <router-link to="/persons" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">2. Kin & Guardians</span>
          <UsersRound :size="18" class="text-indigo-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ personStore.relatedPersons.length }}</div>
        <div class="text-xs text-indigo-400 mt-1 font-medium">RelatedPersons Linked</div>
      </router-link>

      <!-- 3. Practitioner -->
      <router-link to="/practitioners" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">3. Clinical Staff</span>
          <UserCheck :size="18" class="text-emerald-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ practitionerStore.practitioners.length }}</div>
        <div class="text-xs text-emerald-400 mt-1 font-medium">Practitioners Active</div>
      </router-link>

      <!-- 4. PractitionerRole -->
      <router-link to="/practitioners" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">4. Medical Roles</span>
          <Stethoscope :size="18" class="text-teal-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ practitionerStore.practitionerRoles.length }}</div>
        <div class="text-xs text-teal-400 mt-1 font-medium">Practitioner Roles Assigned</div>
      </router-link>

      <!-- 5. Organization -->
      <router-link to="/organizations" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">5. Hierarchies</span>
          <Building2 :size="18" class="text-amber-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ facilityStore.organizations.length }}</div>
        <div class="text-xs text-amber-400 mt-1 font-medium">Organizations Managed</div>
      </router-link>

      <!-- 6. Location -->
      <router-link to="/locations" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">6. Physical Facilities</span>
          <MapPin :size="18" class="text-rose-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ facilityStore.locations.length }}</div>
        <div class="text-xs text-rose-400 mt-1 font-medium">Locations & Wards</div>
      </router-link>

      <!-- 7. HealthcareService -->
      <router-link to="/services" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">7. Clinical Services</span>
          <Zap :size="18" class="text-purple-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ facilityStore.healthcareServices.length }}</div>
        <div class="text-xs text-purple-400 mt-1 font-medium">Healthcare Services Listed</div>
      </router-link>

      <!-- 8. Group -->
      <router-link to="/groups" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">8. Clinical Cohorts</span>
          <UsersRound :size="18" class="text-cyan-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ groupStore.groups.length }}</div>
        <div class="text-xs text-cyan-400 mt-1 font-medium">Patient Groups & Cohorts</div>
      </router-link>

      <!-- 9. Provenance -->
      <router-link to="/provenance" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">9. Lifecycle & Origin</span>
          <GitBranch :size="18" class="text-orange-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ securityStore.provenances.length }}</div>
        <div class="text-xs text-orange-400 mt-1 font-medium">Provenance Records</div>
      </router-link>

      <!-- 10. AuditEvent -->
      <router-link to="/audit" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">10. Security Audit</span>
          <ShieldAlert :size="18" class="text-rose-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ securityStore.auditEvents.length }}</div>
        <div class="text-xs text-rose-400 mt-1 font-medium">Audit Events Logged</div>
      </router-link>

      <!-- 11. Consent -->
      <router-link to="/consent" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">11. Directives & Privacy</span>
          <FileCheck :size="18" class="text-emerald-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ securityStore.consents.length }}</div>
        <div class="text-xs text-emerald-400 mt-1 font-medium">Consents Registered</div>
      </router-link>

      <!-- 12. Task -->
      <router-link to="/tasks" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">12. Action Workflows</span>
          <CheckSquare :size="18" class="text-sky-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ workflowStore.tasks.length }}</div>
        <div class="text-xs text-sky-400 mt-1 font-medium">Clinical Tasks Queued</div>
      </router-link>

      <!-- 13. Communication -->
      <router-link to="/communication" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">13. Messaging & Alerts</span>
          <MessageSquare :size="18" class="text-teal-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ workflowStore.communications.length }}</div>
        <div class="text-xs text-teal-400 mt-1 font-medium">Communications Sent</div>
      </router-link>

      <!-- 14. DocumentReference -->
      <router-link to="/documents" class="card hover:border-sky-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">14. Clinical Documents</span>
          <FileText :size="18" class="text-indigo-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ workflowStore.documentReferences.length }}</div>
        <div class="text-xs text-indigo-400 mt-1 font-medium">Documents Indexed</div>
      </router-link>
    </div>
  </div>
</template>
