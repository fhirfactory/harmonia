<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { usePersonStore } from '../stores/personStore';
import { usePractitionerStore } from '../stores/practitionerStore';
import { useFacilityStore } from '../stores/facilityStore';
import { useGroupStore } from '../stores/groupStore';
import { 
  Server, Database, Cpu, Layers, HardDrive, 
  Users, UserCheck, Building2, MapPin, Stethoscope, UsersRound, 
  CheckCircle2, ArrowRight, ShieldCheck, Zap
} from 'lucide-vue-next';

const personStore = usePersonStore();
const practitionerStore = usePractitionerStore();
const facilityStore = useFacilityStore();
const groupStore = useGroupStore();

const refreshAll = async () => {
  await Promise.all([
    personStore.fetchPersons(),
    personStore.fetchRelatedPersons(),
    practitionerStore.fetchPractitioners(),
    practitionerStore.fetchPractitionerRoles(),
    facilityStore.fetchOrganizations(),
    facilityStore.fetchLocations(),
    facilityStore.fetchHealthcareServices(),
    groupStore.fetchGroups()
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
            <Zap :size="12" /> Sub-10ms Clustered Memory Grid
          </span>
          <span class="badge badge-green flex items-center gap-1">
            <ShieldCheck :size="12" /> Write-Behind Eventual Persistence
          </span>
        </div>
        <h1 class="text-2xl md:text-3xl font-extrabold text-white">5-Tier Clustered Health Information Exchange</h1>
        <p class="subtitle mt-1">High-availability HL7 FHIR Release 5.0.0 platform with replicated caching & asynchronous disk ingestion.</p>
      </div>
      <button @click="refreshAll" class="btn btn-primary whitespace-nowrap">
        Refresh Metrics
      </button>
    </div>

    <!-- 5-Tier Architecture Diagram -->
    <div class="card">
      <h3 class="text-base font-bold text-white mb-4 flex items-center gap-2">
        <Layers :size="18" class="text-sky-400" />
        Platform Architecture & Data Flow
      </h3>

      <div class="grid grid-cols-1 md:grid-cols-5 gap-3 text-center">
        <!-- Tier 1: UI -->
        <div class="p-3 rounded-lg bg-slate-950 border border-sky-500/40 relative">
          <div class="p-2 w-10 h-10 rounded-full bg-sky-500/10 text-sky-400 mx-auto flex items-center justify-center mb-2">
            <Cpu :size="20" />
          </div>
          <span class="text-xs font-bold text-sky-400 uppercase tracking-wider block">1. UI Tier</span>
          <h4 class="text-sm font-semibold text-white mt-1">Vue 3 + TypeScript</h4>
          <p class="text-[11px] text-slate-400 mt-1">Reactive SPA with Pinia state management & Axios client</p>
          <span class="badge badge-blue mt-2 text-[10px]">Client Web</span>
        </div>

        <!-- Tier 2: BEFE -->
        <div class="p-3 rounded-lg bg-slate-950 border border-indigo-500/40 relative">
          <div class="p-2 w-10 h-10 rounded-full bg-indigo-500/10 text-indigo-400 mx-auto flex items-center justify-center mb-2">
            <Server :size="20" />
          </div>
          <span class="text-xs font-bold text-indigo-400 uppercase tracking-wider block">2. BEFE Tier</span>
          <h4 class="text-sm font-semibold text-white mt-1">WildFly Jakarta EE</h4>
          <p class="text-[11px] text-slate-400 mt-1">JAX-RS endpoints, JSON-B/P, Hot Rod cache client</p>
          <span class="badge badge-purple mt-2 text-[10px]">Port 8080</span>
        </div>

        <!-- Tier 3: Infinispan Cluster -->
        <div class="p-3 rounded-lg bg-slate-950 border border-emerald-500/40 relative">
          <div class="p-2 w-10 h-10 rounded-full bg-emerald-500/10 text-emerald-400 mx-auto flex items-center justify-center mb-2">
            <Zap :size="20" />
          </div>
          <span class="text-xs font-bold text-emerald-400 uppercase tracking-wider block">3. In-Memory Grid</span>
          <h4 class="text-sm font-semibold text-white mt-1">Infinispan 15 HA</h4>
          <p class="text-[11px] text-slate-400 mt-1">Replicated cache topology with JGroups TCP discovery</p>
          <span class="badge badge-green mt-2 text-[10px]">Port 11222</span>
        </div>

        <!-- Tier 4: Persistence SPI -->
        <div class="p-3 rounded-lg bg-slate-950 border border-amber-500/40 relative">
          <div class="p-2 w-10 h-10 rounded-full bg-amber-500/10 text-amber-400 mx-auto flex items-center justify-center mb-2">
            <HardDrive :size="20" />
          </div>
          <span class="text-xs font-bold text-amber-400 uppercase tracking-wider block">4. Persistence SPI</span>
          <h4 class="text-sm font-semibold text-white mt-1">NonBlockingStore</h4>
          <p class="text-[11px] text-slate-400 mt-1">Async write-behind queue (1024 cap) with read-through loader</p>
          <span class="badge badge-amber mt-2 text-[10px]">Custom SPI</span>
        </div>

        <!-- Tier 5: HAPI FHIR JPA -->
        <div class="p-3 rounded-lg bg-slate-950 border border-rose-500/40 relative">
          <div class="p-2 w-10 h-10 rounded-full bg-rose-500/10 text-rose-400 mx-auto flex items-center justify-center mb-2">
            <Database :size="20" />
          </div>
          <span class="text-xs font-bold text-rose-400 uppercase tracking-wider block">5. Disk Persistence</span>
          <h4 class="text-sm font-semibold text-white mt-1">HAPI FHIR + Postgres</h4>
          <p class="text-[11px] text-slate-400 mt-1">FHIR R5 JPA resource providers backed by PostgreSQL</p>
          <span class="badge badge-blue mt-2 text-[10px]">PostgreSQL 16</span>
        </div>
      </div>
    </div>

    <!-- 8 FHIR Resource Cards Overview -->
    <h3 class="text-lg font-bold text-white mt-6">Active FHIR R5 Resources</h3>
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
    </div>
  </div>
</template>
