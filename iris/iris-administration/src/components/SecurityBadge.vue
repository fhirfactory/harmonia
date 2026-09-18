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
import { ref } from 'vue';
import { useSecurityStore } from '../stores/securityStore';
import { Shield, ShieldAlert, Key, UserCheck, X } from 'lucide-vue-next';

const securityStore = useSecurityStore();
const showAuthoritiesModal = ref(false);
</script>

<template>
  <div class="inline-flex items-center gap-2">
    <!-- Security Context Pill -->
    <button 
      @click="showAuthoritiesModal = true"
      class="security-badge-btn"
      title="Click to view Themis security authorities and classification"
    >
      <Shield v-if="securityStore.isAuthenticated" :size="13" class="text-sky-400" />
      <ShieldAlert v-else :size="13" class="text-amber-400" />
      <span class="text-xs font-semibold text-slate-200">
        {{ securityStore.principal?.displayName || 'Unauthenticated' }}
      </span>
      <span class="security-classification-tag">
        INTERNAL
      </span>
    </button>

    <!-- Authorities Modal -->
    <div v-if="showAuthoritiesModal" class="modal-overlay" @click.self="showAuthoritiesModal = false">
      <div class="modal-content">
        <div class="modal-header">
          <div class="flex items-center gap-2">
            <Shield :size="20" class="text-sky-400" />
            <h3 class="text-base font-bold text-white">Themis Security Context</h3>
          </div>
          <button @click="showAuthoritiesModal = false" class="btn btn-secondary btn-sm">
            <X :size="14" />
          </button>
        </div>

        <div class="modal-body space-y-4">
          <div>
            <span class="text-xs font-bold uppercase tracking-wider text-slate-400">Authenticated Principal</span>
            <div class="mt-1 p-3 bg-slate-950/80 rounded border border-slate-800 space-y-1 text-sm">
              <div class="flex justify-between">
                <span class="text-slate-400">Principal ID:</span>
                <span class="font-mono text-slate-200">{{ securityStore.principal?.id }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-slate-400">User:</span>
                <span class="font-semibold text-slate-200">{{ securityStore.principal?.displayName }} ({{ securityStore.principal?.username }})</span>
              </div>
              <div v-if="securityStore.principal?.practitionerId" class="flex justify-between">
                <span class="text-slate-400">Practitioner Ref:</span>
                <span class="font-mono text-sky-400">{{ securityStore.principal?.practitionerId }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-slate-400">Domain Classification:</span>
                <span class="font-bold text-emerald-400">PROVIDER_REGISTRY (INTERNAL)</span>
              </div>
              <div class="flex justify-between">
                <span class="text-slate-400">Correlation ID:</span>
                <span class="font-mono text-xs text-slate-400">{{ securityStore.correlationId }}</span>
              </div>
            </div>
          </div>

          <div>
            <span class="text-xs font-bold uppercase tracking-wider text-slate-400">Granted Themis Authorities</span>
            <div class="mt-1 flex flex-wrap gap-1.5 p-3 bg-slate-950/80 rounded border border-slate-800">
              <span 
                v-for="auth in securityStore.principal?.authorities" 
                :key="auth"
                class="badge badge-accent font-mono text-xs"
              >
                <Key :size="11" />
                {{ auth }}
              </span>
              <span v-if="!securityStore.principal?.authorities?.length" class="text-xs text-slate-500 italic">
                No active authorities assigned.
              </span>
            </div>
          </div>

          <div class="p-3 bg-sky-950/30 rounded border border-sky-800/40 text-xs text-sky-300">
            <strong>Security Boundary Notice:</strong> All mutations and query operations are authoritatively evaluated and enforced server-side by Themis policies. The browser UI reflects active permissions for guidance only.
          </div>
        </div>

        <div class="modal-footer">
          <button @click="showAuthoritiesModal = false" class="btn btn-secondary">
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.security-badge-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.45rem;
  background-color: rgba(15, 23, 42, 0.85);
  border: 1px solid #27344d;
  padding: 0.3rem 0.65rem;
  border-radius: 9999px;
  cursor: pointer;
  transition: all 0.15s ease;
}

.security-badge-btn:hover {
  border-color: #38bdf8;
  background-color: rgba(30, 41, 59, 0.9);
}

.security-classification-tag {
  background: rgba(16, 185, 129, 0.15);
  border: 1px solid rgba(16, 185, 129, 0.3);
  color: #34d399;
  font-size: 0.65rem;
  font-weight: 700;
  letter-spacing: 0.05em;
  padding: 0.1rem 0.35rem;
  border-radius: 4px;
}
</style>
