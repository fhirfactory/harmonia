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
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useSelfServiceStore } from '../../stores/selfServiceStore';
import ValidationAlert from '../../components/ValidationAlert.vue';
import type { ValidationIssue } from '../../models/provider';
import { 
  Send, User, Briefcase, Building2, 
  CheckCircle2, AlertTriangle, ArrowRight, Shield 
} from 'lucide-vue-next';

const router = useRouter();
const selfServiceStore = useSelfServiceStore();

const changeTarget = ref<'practitioner' | 'role'>('practitioner');
const operation = ref<'UPDATE' | 'CREATE'>('UPDATE');

// Form model
const form = ref({
  familyName: '',
  givenName: '',
  email: '',
  phone: '',
  addressLine: '',
  city: '',
  state: 'VIC',
  postalCode: '',
  selectedRoleId: '',
  roleSpecialty: '',
  simulatedErrorTrigger: 'none'
});

const submissionOutcome = ref<{
  success: boolean;
  taskId?: string;
  status?: string;
  issues: ValidationIssue[];
} | null>(null);

onMounted(async () => {
  await selfServiceStore.loadProviderProfile();
  if (selfServiceStore.practitioner) {
    const p = selfServiceStore.practitioner;
    form.value.familyName = p.name?.[0]?.family || '';
    form.value.givenName = p.name?.[0]?.given?.[0] || '';
    form.value.email = p.telecom?.find(t => t.system === 'email')?.value || '';
    form.value.phone = p.telecom?.find(t => t.system === 'phone')?.value || '';
    if (p.address?.length) {
      form.value.addressLine = p.address[0].line?.[0] || '';
      form.value.city = p.address[0].city || '';
      form.value.state = p.address[0].state || 'VIC';
      form.value.postalCode = p.address[0].postalCode || '';
    }
  }
  if (selfServiceStore.roles.length > 0) {
    form.value.selectedRoleId = selfServiceStore.roles[0].id;
    form.value.roleSpecialty = selfServiceStore.roles[0].specialty.join(', ');
  }
});

const handleSubmit = async () => {
  submissionOutcome.value = null;

  try {
    let payload: any = {};
    let targetType = 'Practitioner';
    let targetId = selfServiceStore.practitioner?.id || 'PR-1001';

    if (changeTarget.value === 'practitioner') {
      targetType = 'Practitioner';
      targetId = selfServiceStore.practitioner?.id || 'PR-1001';

      let identifierValue = '8003610833334444';
      if (form.value.simulatedErrorTrigger === 'duplicate-hpii') {
        identifierValue = 'DUPLICATE';
      }

      payload = {
        resourceType: 'Practitioner',
        id: targetId,
        active: true,
        identifier: [
          { system: 'http://ns.electronichealth.net.au/id/hi/hpii/1.0', value: identifierValue, type: { text: 'HPI-I' } }
        ],
        name: [{
          family: form.value.familyName,
          given: [form.value.givenName],
          prefix: ['Dr'],
          text: `Dr. ${form.value.givenName} ${form.value.familyName}`
        }],
        telecom: [
          { system: 'email', value: form.value.email, use: 'work' },
          { system: 'phone', value: form.value.phone, use: 'work' }
        ],
        address: [{
          line: [form.value.addressLine],
          city: form.value.city,
          state: form.value.state,
          postalCode: form.value.postalCode,
          country: 'AUS'
        }]
      };
    } else {
      targetType = 'PractitionerRole';
      targetId = form.value.selectedRoleId;
      const rawRole = selfServiceStore.rawRoles.find(r => r.id === targetId);

      payload = {
        resourceType: 'PractitionerRole',
        id: targetId,
        active: true,
        practitioner: rawRole?.practitioner,
        organization: rawRole?.organization,
        code: rawRole?.code,
        specialty: form.value.roleSpecialty.split(',').map(s => ({ text: s.trim() })),
        telecom: rawRole?.telecom,
        location: rawRole?.location,
        healthcareService: rawRole?.healthcareService
      };
    }

    const result = await selfServiceStore.submitChangeRequest(
      targetType,
      operation.value,
      payload,
      targetId
    );

    submissionOutcome.value = {
      success: result.status !== 'REJECTED' && result.status !== 'FAILED',
      taskId: result.taskId,
      status: result.status,
      issues: result.outcomeIssues || []
    };
  } catch (err: any) {
    submissionOutcome.value = {
      success: false,
      issues: [{
        severity: 'fatal',
        code: 'PR-VAL-010',
        details: err.message || 'Error occurred while dispatching change request to Pylai gateway.'
      }]
    };
  }
};
</script>

<template>
  <div class="max-w-3xl mx-auto space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-white">Request Registry Change</h1>
      <p class="text-slate-400 text-sm mt-1">
        Submit governed change requests for review and authoritative validation. Changes will not directly overwrite production master data until validated and approved.
      </p>
    </div>

    <!-- Feedback Notice if Submitted -->
    <div v-if="submissionOutcome" class="space-y-4">
      <div 
        v-if="submissionOutcome.success" 
        class="card bg-emerald-950/20 border-emerald-500/40 p-5 space-y-3"
      >
        <div class="flex items-center gap-2 text-emerald-400 font-bold">
          <CheckCircle2 :size="20" />
          <span>Change Request Submitted Successfully (HTTP 202 Accepted)</span>
        </div>
        <p class="text-slate-300 text-xs">
          Your request has been enqueued into the Harmonia Ponos change pipeline with tracking identifier:
          <span class="font-mono text-sky-400 font-semibold">{{ submissionOutcome.taskId }}</span>.
        </p>
        <div class="pt-2 flex gap-3">
          <router-link to="/self-service/my-requests" class="btn btn-primary btn-sm">
            <span>View in My Requests</span>
            <ArrowRight :size="14" />
          </router-link>
        </div>
      </div>

      <div v-else>
        <ValidationAlert :issues="submissionOutcome.issues" title="Submission Validation Rejected" />
      </div>
    </div>

    <!-- Form Container -->
    <form @submit.prevent="handleSubmit" class="card space-y-6">
      <!-- Target Selector -->
      <div class="space-y-2">
        <label class="form-label">What information do you wish to update?</label>
        <div class="grid grid-cols-2 gap-3">
          <button 
            type="button"
            class="p-3.5 rounded-lg border text-left flex items-center gap-3 transition-all cursor-pointer"
            :class="changeTarget === 'practitioner' ? 'bg-sky-500/10 border-sky-500 text-white' : 'bg-slate-950 border-slate-800 text-slate-400 hover:border-slate-700'"
            @click="changeTarget = 'practitioner'"
          >
            <User :size="20" class="text-sky-400 shrink-0" />
            <div>
              <div class="font-bold text-sm">Personal Demographics</div>
              <div class="text-[11px] text-slate-400">Name, email, phone, practice address</div>
            </div>
          </button>

          <button 
            type="button"
            class="p-3.5 rounded-lg border text-left flex items-center gap-3 transition-all cursor-pointer"
            :class="changeTarget === 'role' ? 'bg-indigo-500/10 border-indigo-500 text-white' : 'bg-slate-950 border-slate-800 text-slate-400 hover:border-slate-700'"
            @click="changeTarget = 'role'"
          >
            <Briefcase :size="20" class="text-indigo-400 shrink-0" />
            <div>
              <div class="font-bold text-sm">Professional Role</div>
              <div class="text-[11px] text-slate-400">Specialty focus, practice locations</div>
            </div>
          </button>
        </div>
      </div>

      <!-- Practitioner Demographics Fields -->
      <div v-if="changeTarget === 'practitioner'" class="space-y-4 pt-2 border-t border-slate-800">
        <h3 class="text-sm font-bold text-slate-300 uppercase tracking-wider">Demographic Information</h3>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="form-group">
            <label class="form-label">First / Given Name *</label>
            <input v-model="form.givenName" class="form-input" required placeholder="e.g. Sarah" />
          </div>

          <div class="form-group">
            <label class="form-label">Last / Family Name *</label>
            <input v-model="form.familyName" class="form-input" required placeholder="e.g. Chen" />
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="form-group">
            <label class="form-label">Work Email *</label>
            <input v-model="form.email" type="email" class="form-input" required placeholder="name@domain.org" />
          </div>

          <div class="form-group">
            <label class="form-label">Direct Phone *</label>
            <input v-model="form.phone" class="form-input" required placeholder="+61 3 9412 8800" />
          </div>
        </div>

        <div class="space-y-3">
          <label class="form-label">Practice Postal / Physical Address</label>
          <div class="form-group">
            <input v-model="form.addressLine" class="form-input" placeholder="Street line (e.g. Suite 402, 120 Victoria Parade)" />
          </div>
          <div class="grid grid-cols-3 gap-3">
            <div class="form-group">
              <input v-model="form.city" class="form-input" placeholder="City / Suburb" />
            </div>
            <div class="form-group">
              <select v-model="form.state" class="form-select">
                <option value="VIC">VIC</option>
                <option value="NSW">NSW</option>
                <option value="QLD">QLD</option>
                <option value="WA">WA</option>
                <option value="SA">SA</option>
                <option value="TAS">TAS</option>
                <option value="ACT">ACT</option>
                <option value="NT">NT</option>
              </select>
            </div>
            <div class="form-group">
              <input v-model="form.postalCode" class="form-input" placeholder="Postcode" />
            </div>
          </div>
        </div>
      </div>

      <!-- PractitionerRole Fields -->
      <div v-else class="space-y-4 pt-2 border-t border-slate-800">
        <h3 class="text-sm font-bold text-slate-300 uppercase tracking-wider">Professional Role Details</h3>

        <div class="form-group">
          <label class="form-label">Target Role to Update</label>
          <select v-model="form.selectedRoleId" class="form-select">
            <option v-for="role in selfServiceStore.roles" :key="role.id" :value="role.id">
              {{ role.code }} &bull; {{ role.organizationName }} ({{ role.id }})
            </option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">Specialties &amp; Clinical Subspecialties (Comma separated)</label>
          <input v-model="form.roleSpecialty" class="form-input" placeholder="e.g. Family Medicine, Chronic Disease Management" />
          <span class="text-[11px] text-slate-500">Values are mapped to SNOMED CT and ANZSCO terminology standards.</span>
        </div>
      </div>

      <!-- Test Diagnostic Error Trigger -->
      <div class="p-3 bg-slate-950/80 rounded border border-slate-800 space-y-1.5">
        <label class="text-xs font-bold text-slate-400 flex items-center gap-1">
          <Shield :size="12" class="text-sky-400" />
          <span>Validation Simulation Trigger (Testing &amp; QA):</span>
        </label>
        <select v-model="form.simulatedErrorTrigger" class="form-select text-xs">
          <option value="none">Standard Submission (Valid &bull; Will be accepted)</option>
          <option value="duplicate-hpii">Simulate PR-VAL-003 Duplicate HPI-I Identifier Error</option>
        </select>
      </div>

      <div class="pt-4 border-t border-slate-800 flex justify-end gap-3">
        <router-link to="/self-service/my-details" class="btn btn-secondary">
          Cancel
        </router-link>
        <button type="submit" class="btn btn-primary" :disabled="selfServiceStore.loading">
          <Send :size="16" />
          <span>Submit Request (HTTP 202)</span>
        </button>
      </div>
    </form>
  </div>
</template>
