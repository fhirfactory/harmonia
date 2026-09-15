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
import { ref, reactive, computed, onMounted } from 'vue';
import { useSecurityStore } from '../stores/securityStore';
import { fhirApi } from '../api/fhirClient';
import type { Provenance, Reference } from '../models/fhir';
import { 
  Plus, Search, Trash2, Eye, GitBranch, X, 
  ChevronRight, ChevronDown, RotateCw, Layers,
  CornerDownRight, CheckCircle2, AlertCircle, FileText,
  Activity, User, MapPin, Box, Link2
} from 'lucide-vue-next';

interface LinkedResourceItem {
  key: string;
  role: string;
  roleType: 'target' | 'source' | 'entity' | 'patient' | 'agent' | 'location';
  reference: string;
  resourceType: string;
  resourceId: string;
  display?: string;
  detail?: string;
}

const store = useSecurityStore();
const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

// Expansion state for sublists
const expandedRows = ref<Set<string>>(new Set());

// Actual resource cache: reference string -> { data, loading, error }
const resourceCache = reactive<Record<string, { data?: any; loading?: boolean; error?: string | null }>>({});

const newProv = ref({
  target: '',
  sourceEntity: '',
  entityRole: 'source',
  patient: '',
  agent: '',
  activity: 'CREATE'
});

onMounted(() => {
  store.fetchProvenances();
});

const handleSearch = () => {
  store.fetchProvenances(searchName.value);
};

// Parse reference into [resourceType, resourceId]
const parseReference = (refStr?: string): { resourceType: string; resourceId: string } => {
  if (!refStr) return { resourceType: '', resourceId: '' };
  const cleanRef = refStr.replace(/^#/, '');
  const parts = cleanRef.split('/');
  if (parts.length >= 2) {
    return { resourceType: parts[0], resourceId: parts.slice(1).join('/') };
  }
  return { resourceType: '', resourceId: cleanRef };
};

// Extract all linked/associated resources from a Provenance resource
const getLinkedResources = (prov: Provenance): LinkedResourceItem[] => {
  const items: LinkedResourceItem[] = [];

  // 1. Target Resources
  if (prov.target && prov.target.length > 0) {
    prov.target.forEach((tgt, idx) => {
      const refStr = tgt.reference || '';
      const parsed = parseReference(refStr);
      items.push({
        key: `${prov.id || 'prov'}-target-${idx}-${refStr}`,
        role: 'Target Resource',
        roleType: 'target',
        reference: refStr,
        resourceType: tgt.type || parsed.resourceType || 'Resource',
        resourceId: parsed.resourceId,
        display: tgt.display || 'Primary Target of Provenance Activity',
        detail: 'Generated or modified resource'
      });
    });
  }

  // 2. Entity Resources (e.g. source Communication, derivation, etc.)
  if (prov.entity && prov.entity.length > 0) {
    prov.entity.forEach((ent, idx) => {
      const refStr = ent.what?.reference || '';
      const parsed = parseReference(refStr);
      const roleName = ent.role ? ent.role.toUpperCase() : 'SOURCE';
      const isSource = roleName === 'SOURCE';
      items.push({
        key: `${prov.id || 'prov'}-entity-${idx}-${refStr}`,
        role: isSource ? 'Source Entity' : `Entity (${roleName})`,
        roleType: isSource ? 'source' : 'entity',
        reference: refStr,
        resourceType: ent.what?.type || parsed.resourceType || 'Resource',
        resourceId: parsed.resourceId,
        display: ent.what?.display || `Input ${roleName.toLowerCase()} entity`,
        detail: `Role: ${roleName}`
      });
    });
  }

  // 3. Patient Context
  if (prov.patient && prov.patient.reference) {
    const refStr = prov.patient.reference;
    const parsed = parseReference(refStr);
    items.push({
      key: `${prov.id || 'prov'}-patient-${refStr}`,
      role: 'Patient Context',
      roleType: 'patient',
      reference: refStr,
      resourceType: prov.patient.type || parsed.resourceType || 'Patient',
      resourceId: parsed.resourceId,
      display: prov.patient.display || 'Associated Patient',
      detail: 'Subject of clinical event'
    });
  }

  // 4. Agent Resources (Transmitter, Assembler, Practitioner, Organization, etc.)
  if (prov.agent && prov.agent.length > 0) {
    prov.agent.forEach((ag, idx) => {
      const refStr = ag.who?.reference || '';
      const parsed = parseReference(refStr);
      const agentRoleName = ag.type?.coding?.[0]?.display || ag.type?.text || 'Agent';
      const displayStr = ag.who?.display || (parsed.resourceId ? `${agentRoleName}: ${parsed.resourceId}` : agentRoleName);
      
      // If there is a reference or a specific identifiable display
      items.push({
        key: `${prov.id || 'prov'}-agent-${idx}-${refStr || displayStr}`,
        role: `Agent (${agentRoleName})`,
        roleType: 'agent',
        reference: refStr,
        resourceType: ag.who?.type || parsed.resourceType || (refStr ? 'Device/Practitioner' : 'Actor'),
        resourceId: parsed.resourceId,
        display: displayStr,
        detail: `Participant Type: ${agentRoleName}`
      });

      if (ag.onBehalfOf && ag.onBehalfOf.reference) {
        const oboRef = ag.onBehalfOf.reference;
        const oboParsed = parseReference(oboRef);
        items.push({
          key: `${prov.id || 'prov'}-obo-${idx}-${oboRef}`,
          role: 'On Behalf Of',
          roleType: 'agent',
          reference: oboRef,
          resourceType: ag.onBehalfOf.type || oboParsed.resourceType || 'Organization',
          resourceId: oboParsed.resourceId,
          display: ag.onBehalfOf.display || 'Delegating Organization',
          detail: 'Custodial / Delegating Entity'
        });
      }
    });
  }

  // 5. Location Context
  if (prov.location && (prov.location.reference || prov.location.display)) {
    const refStr = prov.location.reference || '';
    const parsed = parseReference(refStr);
    items.push({
      key: `${prov.id || 'prov'}-location-${refStr || prov.location.display}`,
      role: 'Location',
      roleType: 'location',
      reference: refStr,
      resourceType: prov.location.type || parsed.resourceType || 'Location',
      resourceId: parsed.resourceId,
      display: prov.location.display || 'Encounter / Care Point Location',
      detail: 'Assigned Clinical Location'
    });
  }

  return items;
};

// Fetch actual resource from API
const fetchActualResource = async (refStr: string, force = false) => {
  if (!refStr) return;
  const { resourceType, resourceId } = parseReference(refStr);
  if (!resourceType || !resourceId) return;

  if (!force && resourceCache[refStr]?.data) {
    return;
  }

  resourceCache[refStr] = { loading: true, error: null };

  try {
    const data = await fhirApi.get(resourceType, resourceId);
    resourceCache[refStr] = { data, loading: false, error: null };
  } catch (err: any) {
    resourceCache[refStr] = { 
      loading: false, 
      error: err.response?.status === 404 ? 'Resource not in store (external reference)' : (err.message || 'Failed to load')
    };
  }
};

// Toggle expanding a Provenance row & load its sublist resources
const toggleExpand = (prov: Provenance) => {
  if (!prov.id) return;
  if (expandedRows.value.has(prov.id)) {
    expandedRows.value.delete(prov.id);
  } else {
    expandedRows.value.add(prov.id);
    // Fetch all linked resources
    const linked = getLinkedResources(prov);
    linked.forEach(item => {
      if (item.reference) {
        fetchActualResource(item.reference);
      }
    });
  }
};

const isExpanded = (provId?: string): boolean => {
  return !!provId && expandedRows.value.has(provId);
};

const isAllExpanded = computed(() => {
  return store.provenances.length > 0 && store.provenances.every(p => p.id && expandedRows.value.has(p.id));
});

const toggleExpandAll = () => {
  if (isAllExpanded.value) {
    expandedRows.value.clear();
  } else {
    store.provenances.forEach(p => {
      if (p.id) {
        expandedRows.value.add(p.id);
        const linked = getLinkedResources(p);
        linked.forEach(item => {
          if (item.reference) {
            fetchActualResource(item.reference);
          }
        });
      }
    });
  }
};

// Get summary badge / detail for actual loaded resource
const getActualResourceSummary = (item: LinkedResourceItem) => {
  const cached = item.reference ? resourceCache[item.reference] : null;
  if (!cached || !cached.data) return null;
  const data = cached.data;

  if (data.resourceType === 'Task') {
    return {
      status: data.status || 'unknown',
      statusClass: data.status === 'completed' ? 'badge-green' : data.status === 'in-progress' ? 'badge-blue' : 'badge-amber',
      priority: data.priority,
      intent: data.intent,
      authored: data.authoredOn,
      summary: data.description || (data.focus?.display ? `Focus: ${data.focus.display}` : '')
    };
  } else if (data.resourceType === 'Communication') {
    return {
      status: data.status || 'unknown',
      statusClass: data.status === 'completed' ? 'badge-green' : data.status === 'in-progress' ? 'badge-blue' : 'badge-amber',
      sent: data.sent,
      category: data.category?.[0]?.text || data.category?.[0]?.coding?.[0]?.code,
      summary: data.note?.[0]?.text || data.statusReason?.text || 'HL7 ADT Communication Payload'
    };
  } else if (data.resourceType === 'Person' || data.resourceType === 'Patient') {
    const nameObj = data.name?.[0];
    const fullName = nameObj ? (nameObj.text || [nameObj.given?.join(' '), nameObj.family].filter(Boolean).join(' ')) : '';
    return {
      status: data.active !== false ? 'active' : 'inactive',
      statusClass: data.active !== false ? 'badge-green' : 'badge-red',
      name: fullName,
      gender: data.gender,
      birthDate: data.birthDate,
      summary: [fullName, data.gender, data.birthDate].filter(Boolean).join(' • ')
    };
  } else if (data.resourceType === 'Practitioner') {
    const nameObj = data.name?.[0];
    const fullName = nameObj ? (nameObj.text || [nameObj.prefix?.join(' '), nameObj.given?.join(' '), nameObj.family].filter(Boolean).join(' ')) : '';
    return {
      status: data.active !== false ? 'active' : 'inactive',
      statusClass: data.active !== false ? 'badge-green' : 'badge-red',
      name: fullName,
      summary: fullName || data.id
    };
  } else if (data.resourceType === 'DocumentReference') {
    return {
      status: data.status || 'unknown',
      statusClass: data.status === 'current' ? 'badge-green' : 'badge-amber',
      docStatus: data.docStatus,
      summary: data.description || data.type?.text || 'Clinical Document'
    };
  }

  return {
    status: data.status || (data.active !== undefined ? (data.active ? 'active' : 'inactive') : 'present'),
    statusClass: 'badge-blue',
    summary: data.name || data.id
  };
};

const handleCreate = async () => {
  if (!newProv.value.target) return;
  const prov: Partial<Provenance> = {
    resourceType: 'Provenance',
    recorded: new Date().toISOString(),
    target: [{ reference: newProv.value.target }],
    activity: { text: newProv.value.activity },
    agent: [{
      who: { display: newProv.value.agent || 'System Orchestrator' }
    }]
  };

  if (newProv.value.sourceEntity) {
    prov.entity = [{
      role: newProv.value.entityRole || 'source',
      what: { reference: newProv.value.sourceEntity }
    }];
  }

  if (newProv.value.patient) {
    prov.patient = { reference: newProv.value.patient };
  }

  await store.createProvenance(prov);
  showCreateModal.value = false;
  newProv.value = { target: '', sourceEntity: '', entityRole: 'source', patient: '', agent: '', activity: 'CREATE' };
};

const viewDetails = (item: any) => {
  selectedResource.value = item;
  showDetailModal.value = true;
};

const viewLinkedResourceJson = (item: LinkedResourceItem) => {
  const cached = item.reference ? resourceCache[item.reference] : null;
  if (cached && cached.data) {
    selectedResource.value = cached.data;
  } else {
    // Show reference descriptor object
    selectedResource.value = {
      role: item.role,
      reference: item.reference,
      resourceType: item.resourceType,
      id: item.resourceId,
      display: item.display,
      detail: item.detail,
      statusMessage: cached?.error || 'Direct cached payload unavailable or external reference'
    };
  }
  showDetailModal.value = true;
};
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white flex items-center gap-2">
          <GitBranch class="text-orange-400" :size="24" />
          Provenance Records
        </h1>
        <p class="subtitle mt-1">Track origin, lifecycle mutations, lineage, and associated resources in FHIR R5.</p>
      </div>

      <div class="flex items-center gap-3">
        <button 
          v-if="store.provenances.length > 0" 
          @click="toggleExpandAll" 
          class="btn btn-secondary text-xs flex items-center gap-1.5"
          title="Toggle expanding all sublists"
        >
          <Layers :size="14" />
          <span>{{ isAllExpanded ? 'Collapse All Sublists' : 'Expand All Sublists' }}</span>
        </button>

        <button @click="showCreateModal = true" class="btn btn-primary">
          <Plus :size="16" />
          <span>Record Provenance</span>
        </button>
      </div>
    </div>

    <!-- Search Bar -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <div class="flex items-center gap-3">
        <span class="text-xs text-slate-400 font-semibold uppercase">Total Provenance Entries: {{ store.provenances.length }}</span>
        <span class="text-xs text-slate-500">•</span>
        <span class="text-xs text-slate-400">Expand any row to inspect actual linked resources sublist</span>
      </div>
      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter target, agent, or id..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- Provenance Table with Nested Actual Resources Sublist -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th class="w-10 text-center">Sublist</th>
            <th>ID</th>
            <th>Target Resource</th>
            <th>Source / Entity</th>
            <th>Activity</th>
            <th>Agent / Actor</th>
            <th>Recorded Date</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.provenances.length === 0">
            <td colspan="8" class="text-center text-slate-500 py-6">No Provenance records found. Click Record Provenance above.</td>
          </tr>
          <template v-for="prov in store.provenances" :key="prov.id">
            <!-- Parent Provenance Row -->
            <tr 
              class="cursor-pointer hover:bg-slate-800/40 transition-colors"
              :class="{ 'bg-slate-800/20 border-b-0': isExpanded(prov.id) }"
              @click="toggleExpand(prov)"
            >
              <!-- Expand / Collapse Button -->
              <td class="text-center py-3 px-2" @click.stop="toggleExpand(prov)">
                <button 
                  class="p-1 rounded hover:bg-slate-700/60 text-slate-400 hover:text-white transition-colors"
                  :title="isExpanded(prov.id) ? 'Collapse actual resources sublist' : 'Expand actual resources sublist'"
                >
                  <ChevronDown v-if="isExpanded(prov.id)" :size="16" class="text-orange-400" />
                  <ChevronRight v-else :size="16" />
                </button>
              </td>

              <!-- Provenance ID -->
              <td class="font-mono text-xs text-orange-400 font-semibold" @click.stop="viewDetails(prov)">
                <div class="flex items-center gap-1.5">
                  <GitBranch :size="13" class="text-orange-400 shrink-0" />
                  <span>{{ prov.id }}</span>
                </div>
              </td>

              <!-- Target Resource -->
              <td class="font-medium text-white font-mono text-xs">
                <div v-if="prov.target && prov.target.length > 0" class="flex flex-col gap-1">
                  <div class="flex items-center gap-1.5">
                    <span class="badge badge-blue text-[11px] font-mono">{{ prov.target[0].reference || prov.target[0].display || '-' }}</span>
                    <span v-if="prov.target.length > 1" class="text-[10px] text-slate-400 font-sans">+{{ prov.target.length - 1 }} more</span>
                  </div>
                </div>
                <span v-else class="text-slate-500">-</span>
              </td>

              <!-- Source / Entity -->
              <td class="font-mono text-xs">
                <div v-if="prov.entity && prov.entity.length > 0" class="flex items-center gap-1.5">
                  <span class="badge badge-purple text-[11px] font-mono">
                    {{ prov.entity[0].what?.reference || prov.entity[0].what?.display || '-' }}
                  </span>
                  <span v-if="prov.entity.length > 1" class="text-[10px] text-slate-400 font-sans">+{{ prov.entity.length - 1 }}</span>
                </div>
                <span v-else class="text-slate-500">-</span>
              </td>

              <!-- Activity -->
              <td>
                <span class="badge badge-amber">{{ prov.activity?.text || prov.activity?.coding?.[0]?.code || 'MUTATE' }}</span>
              </td>

              <!-- Agent / Actor -->
              <td class="text-slate-300 text-xs">
                <div>{{ prov.agent?.[0]?.who?.display || 'System' }}</div>
                <div v-if="prov.patient" class="text-[10px] text-emerald-400 flex items-center gap-1 mt-0.5">
                  <User :size="10" />
                  <span>{{ prov.patient.display || prov.patient.reference }}</span>
                </div>
              </td>

              <!-- Recorded Date -->
              <td class="text-slate-400 text-xs">{{ prov.recorded || '-' }}</td>

              <!-- Actions -->
              <td class="text-right space-x-2" @click.stop>
                <button @click="toggleExpand(prov)" class="btn btn-secondary py-1 px-2 text-xs" :title="isExpanded(prov.id) ? 'Hide sublist' : 'Show sublist'">
                  <Layers :size="14" class="text-orange-400" />
                  <span class="ml-1 text-[11px]">{{ getLinkedResources(prov).length }} Sub-resources</span>
                </button>
                <button @click="viewDetails(prov)" class="btn btn-secondary py-1 px-2 text-xs" title="View Provenance FHIR JSON">
                  <Eye :size="14" />
                </button>
                <button @click="prov.id && store.deleteProvenance(prov.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete Provenance">
                  <Trash2 :size="14" />
                </button>
              </td>
            </tr>

            <!-- Expanded Sublist Row: Actual Other Resources -->
            <tr v-if="isExpanded(prov.id)" class="bg-[#0c1322] border-b border-[#27344d]">
              <td colspan="8" class="p-0">
                <div class="px-6 py-4 bg-[#0d1424] border-l-4 border-orange-500 rounded-r shadow-inner">
                  <!-- Sublist Header -->
                  <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-2 pb-3 mb-3 border-b border-[#1e293b]">
                    <div class="flex items-center gap-2">
                      <CornerDownRight class="text-orange-400" :size="18" />
                      <h4 class="text-sm font-bold text-white uppercase tracking-wider flex items-center gap-2">
                        Linked Lineage Resources (Sublist of Provenance/{{ prov.id }})
                      </h4>
                      <span class="badge badge-amber text-xs font-mono">
                        {{ getLinkedResources(prov).length }} Resources
                      </span>
                    </div>

                    <div class="flex items-center gap-2 text-xs text-slate-400">
                      <span>Showing actual FHIR resources linked to this Provenance record</span>
                      <button 
                        @click="getLinkedResources(prov).forEach(i => i.reference && fetchActualResource(i.reference, true))"
                        class="btn btn-secondary py-0.5 px-2 text-xs flex items-center gap-1"
                        title="Reload actual resources from store"
                      >
                        <RotateCw :size="12" />
                        <span>Refresh Sublist</span>
                      </button>
                    </div>
                  </div>

                  <!-- Sublist Items Table -->
                  <div v-if="getLinkedResources(prov).length === 0" class="text-xs text-slate-400 py-3 text-center">
                    No linked target or source resources found on this Provenance entry.
                  </div>

                  <div v-else class="space-y-2.5">
                    <div 
                      v-for="item in getLinkedResources(prov)" 
                      :key="item.key"
                      class="card p-3 bg-[#131b2e] border border-[#222f48] hover:border-slate-600 transition-all flex flex-col md:flex-row md:items-center justify-between gap-3"
                    >
                      <!-- Left: Role & Reference -->
                      <div class="flex items-start md:items-center gap-3 min-w-[280px]">
                        <!-- Role Badge -->
                        <div class="shrink-0">
                          <span 
                            v-if="item.roleType === 'target'" 
                            class="badge badge-blue text-[11px] flex items-center gap-1 font-semibold"
                          >
                            <Box :size="11" />
                            Target
                          </span>
                          <span 
                            v-else-if="item.roleType === 'source'" 
                            class="badge badge-purple text-[11px] flex items-center gap-1 font-semibold"
                          >
                            <FileText :size="11" />
                            Source Entity
                          </span>
                          <span 
                            v-else-if="item.roleType === 'entity'" 
                            class="badge badge-purple text-[11px] flex items-center gap-1 font-semibold"
                          >
                            <Layers :size="11" />
                            {{ item.role }}
                          </span>
                          <span 
                            v-else-if="item.roleType === 'patient'" 
                            class="badge badge-green text-[11px] flex items-center gap-1 font-semibold"
                          >
                            <User :size="11" />
                            Patient Context
                          </span>
                          <span 
                            v-else-if="item.roleType === 'agent'" 
                            class="badge badge-amber text-[11px] flex items-center gap-1 font-semibold"
                          >
                            <Activity :size="11" />
                            {{ item.role }}
                          </span>
                          <span 
                            v-else 
                            class="badge badge-blue text-[11px] flex items-center gap-1 font-semibold"
                          >
                            <MapPin :size="11" />
                            Location
                          </span>
                        </div>

                        <!-- Resource Identity -->
                        <div>
                          <div class="flex items-center gap-1.5 font-mono text-xs font-semibold text-white">
                            <Link2 :size="12" class="text-slate-400 shrink-0" />
                            <span v-if="item.reference" class="text-sky-300">{{ item.reference }}</span>
                            <span v-else class="text-slate-400">{{ item.display }}</span>
                          </div>
                          <div class="text-[11px] text-slate-400 mt-0.5">
                            {{ item.display || item.detail }}
                          </div>
                        </div>
                      </div>

                      <!-- Middle: Actual Resource Attributes & State -->
                      <div class="flex-1 text-xs border-t md:border-t-0 md:border-l border-[#243350] md:pl-4 pt-2 md:pt-0">
                        <!-- If loading -->
                        <div v-if="item.reference && resourceCache[item.reference]?.loading" class="flex items-center gap-2 text-slate-400">
                          <RotateCw :size="13" class="animate-spin text-sky-400" />
                          <span>Loading actual FHIR resource from store...</span>
                        </div>

                        <!-- If actual resource data loaded -->
                        <div v-else-if="item.reference && resourceCache[item.reference]?.data" class="space-y-1">
                          <div class="flex flex-wrap items-center gap-2">
                            <span class="text-xs font-bold text-white">
                              Actual Resource: {{ resourceCache[item.reference]?.data.resourceType }}/{{ resourceCache[item.reference]?.data.id }}
                            </span>

                            <!-- Status Badge if present -->
                            <span 
                              v-if="getActualResourceSummary(item)?.status" 
                              class="badge text-[10px] uppercase font-semibold"
                              :class="getActualResourceSummary(item)?.statusClass"
                            >
                              Status: {{ getActualResourceSummary(item)?.status }}
                            </span>

                            <!-- Priority if Task -->
                            <span 
                              v-if="getActualResourceSummary(item)?.priority" 
                              class="badge badge-amber text-[10px]"
                            >
                              Priority: {{ getActualResourceSummary(item)?.priority }}
                            </span>

                            <!-- Category / Intent -->
                            <span 
                              v-if="getActualResourceSummary(item)?.intent" 
                              class="badge badge-blue text-[10px]"
                            >
                              Intent: {{ getActualResourceSummary(item)?.intent }}
                            </span>
                          </div>

                          <!-- Summary Info -->
                          <div class="text-slate-300 text-[11px] flex flex-wrap items-center gap-x-3 gap-y-1">
                            <span v-if="getActualResourceSummary(item)?.summary" class="text-slate-200">
                              {{ getActualResourceSummary(item)?.summary }}
                            </span>
                            <span v-if="getActualResourceSummary(item)?.authored" class="text-slate-400">
                              Authored: {{ getActualResourceSummary(item)?.authored }}
                            </span>
                            <span v-if="getActualResourceSummary(item)?.sent" class="text-slate-400">
                              Sent: {{ getActualResourceSummary(item)?.sent }}
                            </span>
                            <span v-if="resourceCache[item.reference]?.data.meta?.lastUpdated" class="text-slate-500">
                              Last Updated: {{ resourceCache[item.reference]?.data.meta.lastUpdated }}
                            </span>
                          </div>
                        </div>

                        <!-- If fetch error or reference only -->
                        <div v-else class="text-slate-400 text-[11px] flex items-center gap-1.5">
                          <AlertCircle :size="13" class="text-slate-500" />
                          <span v-if="resourceCache[item.reference]?.error">
                            {{ resourceCache[item.reference]?.error }}
                          </span>
                          <span v-else>
                            Referenced Participant / Context Descriptor
                          </span>
                        </div>
                      </div>

                      <!-- Right: Actions on Sublist Item -->
                      <div class="flex items-center gap-2 shrink-0 justify-end">
                        <button 
                          v-if="item.reference && item.resourceId"
                          @click="fetchActualResource(item.reference, true)"
                          class="btn btn-secondary py-1 px-2 text-xs"
                          title="Reload actual resource"
                        >
                          <RotateCw :size="13" />
                        </button>

                        <button 
                          @click="viewLinkedResourceJson(item)"
                          class="btn btn-secondary py-1 px-2.5 text-xs flex items-center gap-1 text-sky-300 hover:text-white"
                          title="View actual resource FHIR JSON"
                        >
                          <Eye :size="13" />
                          <span>View Resource</span>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>

    <!-- Create Modal with Target, Source Entity, and Agent -->
    <div v-if="showCreateModal" class="modal-overlay" @click.self="showCreateModal = false">
      <div class="modal-content max-w-xl">
        <div class="flex items-center justify-between pb-3 border-b border-[#27344d] mb-4">
          <h3 class="text-lg font-bold text-white flex items-center gap-2">
            <GitBranch class="text-orange-400" :size="20" />
            Record Provenance Entry
          </h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div class="space-y-4">
          <div class="form-group">
            <label class="form-label">Target Resource Reference *</label>
            <input v-model="newProv.target" type="text" placeholder="e.g. Task/MSG-CLI-1001 or DocumentReference/9001" class="form-input" required />
            <p class="text-[11px] text-slate-400 mt-1">The resource created, altered, or produced by this activity.</p>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Source Entity Reference (Optional)</label>
              <input v-model="newProv.sourceEntity" type="text" placeholder="e.g. Communication/comm-1001" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Entity Role</label>
              <select v-model="newProv.entityRole" class="form-select">
                <option value="source">SOURCE (Original data)</option>
                <option value="derivation">DERIVATION (Transformed)</option>
                <option value="revision">REVISION (Updated)</option>
                <option value="quotation">QUOTATION (Cited)</option>
              </select>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Activity Type</label>
              <select v-model="newProv.activity" class="form-select">
                <option value="CREATE">CREATE</option>
                <option value="UPDATE">UPDATE</option>
                <option value="TRANSFORM">TRANSFORM</option>
                <option value="VERIFY">VERIFY</option>
                <option value="DELETE">DELETE</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Performing Agent / System</label>
              <input v-model="newProv.agent" type="text" placeholder="e.g. HIE MLLP Gateway or Dr. Smith" class="form-input" />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Patient Reference (Optional)</label>
            <input v-model="newProv.patient" type="text" placeholder="e.g. Patient/PAT001" class="form-input" />
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreate" class="btn btn-primary">Save Provenance Record</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Detail JSON Modal -->
    <div v-if="showDetailModal" class="modal-overlay" @click.self="showDetailModal = false">
      <div class="modal-content max-w-3xl">
        <div class="flex items-center justify-between pb-3 border-b border-[#27344d] mb-4">
          <div class="flex items-center gap-2">
            <Eye class="text-sky-400" :size="18" />
            <h3 class="text-lg font-bold text-white font-mono">
              {{ selectedResource?.resourceType || 'Resource' }}/{{ selectedResource?.id || selectedResource?.reference || 'Details' }}
            </h3>
          </div>
          <button @click="showDetailModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>
        <pre class="code-view max-h-[70vh] overflow-y-auto">{{ JSON.stringify(selectedResource, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>
