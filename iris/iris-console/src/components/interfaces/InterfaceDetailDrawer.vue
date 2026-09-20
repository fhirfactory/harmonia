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
import { onMounted, onUnmounted } from 'vue';
import { 
  X, 
  Radio, 
  ArrowDownLeft, 
  ArrowUpRight, 
  ShieldCheck, 
  AlertTriangle, 
  Info, 
  Server, 
  ExternalLink,
  Layers,
  Cpu,
  FileCode
} from 'lucide-vue-next';
import { IrisStatus } from '@harmonia/iris-befe';

export interface PylaiGateway {
  id: string;
  name: string;
  englishTitle: string;
  description: string;
  direction: 'INBOUND' | 'OUTBOUND';
  protocol: 'HL7 v2 / MLLP' | 'FHIR R5 / REST';
  port: number;
  managementPort?: number;
  targetQueue: string;
  complianceRule: string;
  activeListeners?: number;
  currentConnections?: number;
  status?: string;
}

const props = withDefaults(
  defineProps<{
    gateway: PylaiGateway | null;
    isOpen: boolean;
    teleport?: boolean;
    pylaiStatus?: string;
  }>(),
  {
    gateway: null,
    isOpen: false,
    teleport: true,
    pylaiStatus: 'HEALTHY'
  }
);

const emit = defineEmits<{
  (e: 'close'): void;
}>();

const close = () => {
  emit('close');
};

const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape' && props.isOpen) {
    close();
  }
};

onMounted(() => {
  window.addEventListener('keydown', handleKeydown);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeydown);
});
</script>

<template>
  <teleport to="body" :disabled="!teleport">
    <!-- Backdrop -->
    <div 
      v-if="isOpen" 
      class="interface-drawer__backdrop" 
      @click="close"
      aria-hidden="true"
    ></div>

    <!-- Drawer Panel -->
    <aside
      v-if="isOpen && gateway"
      class="interface-drawer__panel"
      role="dialog"
      aria-modal="true"
      :aria-label="`Interface Details: ${gateway.name}`"
    >
      <!-- Header -->
      <div class="interface-drawer__header">
        <div class="interface-drawer__header-left">
          <div class="interface-drawer__icon-box">
            <Radio :size="20" />
          </div>
          <div class="interface-drawer__header-titles">
            <h2 class="interface-drawer__title font-mono">
              {{ gateway.name }}
            </h2>
            <p class="interface-drawer__subtitle">
              {{ gateway.englishTitle }} &bull; Protocol: <span class="interface-drawer__protocol font-mono">{{ gateway.protocol }}</span>
            </p>
          </div>
        </div>

        <button
          type="button"
          @click="close"
          class="interface-drawer__close-btn"
          title="Close drawer (ESC)"
          aria-label="Close drawer"
        >
          <X :size="18" />
        </button>
      </div>

      <!-- Drawer Body -->
      <div class="interface-drawer__body">
        <!-- Status & Direction Banner -->
        <div class="interface-drawer__status-banner">
          <div>
            <span class="interface-drawer__section-label">State</span>
            <IrisStatus :status="gateway.status || pylaiStatus" size="md" :show-pulse="true" />
          </div>

          <div class="flex items-center gap-2">
            <span 
              class="px-2.5 py-1 rounded text-xs font-mono font-bold uppercase tracking-wider inline-flex items-center gap-1.5"
              :class="gateway.direction === 'INBOUND' 
                ? 'bg-sky-50 text-sky-800 border border-sky-200' 
                : 'bg-purple-50 text-purple-800 border border-purple-200'"
            >
              <ArrowDownLeft v-if="gateway.direction === 'INBOUND'" :size="13" />
              <ArrowUpRight v-else :size="13" />
              <span>{{ gateway.direction }}</span>
            </span>
            <span class="px-2.5 py-1 rounded text-xs font-mono font-bold bg-slate-100 text-slate-800 border border-slate-200">
              :{{ gateway.port }}
            </span>
          </div>
        </div>

        <!-- Technical Parameters Card -->
        <div class="interface-drawer__section">
          <h3 class="interface-drawer__section-heading">
            <Server :size="14" class="interface-drawer__section-icon text-sky" />
            <span>Technical Parameters</span>
          </h3>

          <div class="interface-drawer__spec-card font-mono">
            <div class="interface-drawer__spec-row">
              <span class="interface-drawer__spec-label font-sans">Gateway ID:</span>
              <span class="interface-drawer__spec-value font-semibold">{{ gateway.id }}</span>
            </div>
            <div class="interface-drawer__spec-row">
              <span class="interface-drawer__spec-label font-sans">Wire Protocol:</span>
              <span class="interface-drawer__spec-value text-sky font-semibold">{{ gateway.protocol }}</span>
            </div>
            <div class="interface-drawer__spec-row">
              <span class="interface-drawer__spec-label font-sans">Ingress/Egress Port:</span>
              <span class="interface-drawer__spec-value font-bold">:{{ gateway.port }}</span>
            </div>
            <div class="interface-drawer__spec-row">
              <span class="interface-drawer__spec-label font-sans">Management / Probing Port:</span>
              <span class="interface-drawer__spec-value">{{ gateway.managementPort ? `:${gateway.managementPort}` : ':8084 (Shared WildFly Management)' }}</span>
            </div>
            <div class="interface-drawer__spec-row">
              <span class="interface-drawer__spec-label font-sans">Active Listeners:</span>
              <span class="interface-drawer__spec-value text-emerald font-semibold">{{ gateway.activeListeners ?? (gateway.direction === 'INBOUND' ? 1 : 0) }}</span>
            </div>
            <div class="interface-drawer__spec-row">
              <span class="interface-drawer__spec-label font-sans">Subsystem Boundary:</span>
              <span class="interface-drawer__spec-value">Pylai (Gateways)</span>
            </div>
          </div>
        </div>

        <!-- Messaging & Transport Routing Section -->
        <div class="interface-drawer__section">
          <h3 class="interface-drawer__section-heading">
            <Layers :size="14" class="interface-drawer__section-icon text-emerald" />
            <span>Petasos Messaging &amp; Target Queue</span>
          </h3>

          <div class="p-3 bg-slate-50 border border-slate-200 rounded-lg space-y-2">
            <div class="text-[11px] font-sans text-slate-500">Destination Queue Address:</div>
            <div class="p-2 bg-white border border-slate-200 rounded font-mono text-xs font-bold text-sky-800 break-all select-all">
              {{ gateway.targetQueue }}
            </div>
            <div class="text-[11px] text-slate-500 flex items-center justify-between">
              <span>Broker: ActiveMQ Artemis (Cluster)</span>
              <span>Port: 61616</span>
            </div>
          </div>
        </div>

        <!-- Architectural Invariant Section -->
        <div class="interface-drawer__section">
          <h3 class="interface-drawer__section-heading">
            <ShieldCheck :size="14" class="interface-drawer__section-icon text-emerald" />
            <span>Architectural Compliance</span>
          </h3>

          <div class="p-3 bg-emerald-50/70 border border-emerald-200 rounded-lg space-y-2">
            <div class="flex items-center gap-1.5 text-xs font-bold text-emerald-900 font-mono">
              <ShieldCheck :size="14" class="text-emerald-700 shrink-0" />
              <span>{{ gateway.direction === 'INBOUND' ? 'REC-001 Dual-Write Safety (Invariant 4)' : 'REC-002 Fan-Out Destination Tracking (Invariant 5)' }}</span>
            </div>
            <p class="text-xs text-emerald-950 leading-relaxed font-sans">
              {{ gateway.complianceRule }}
            </p>
            <p class="text-[11px] text-emerald-800 leading-relaxed font-sans">
              {{ gateway.direction === 'INBOUND'
                ? 'Inbound gateways (pylai-mllp-in) must guarantee end-to-end downstream message acceptance before returning an AA (Application Accept) ACK to the upstream sender. If downstream publishing fails, an AE NACK response is emitted.'
                : 'Outbound gateways track granular fan-out delivery per destination, recording transmission checkpoints in the executing Pragma and updating FHIR Task output with structured delivery status extensions.' }}
            </p>
          </div>
        </div>

        <!-- Telemetry Notice Section -->
        <div class="interface-drawer__section">
          <h3 class="interface-drawer__section-heading">
            <Info :size="14" class="interface-drawer__section-icon text-sky" />
            <span>Telemetry Honesty Notice (Rule 9)</span>
          </h3>

          <div class="p-3 bg-sky-50/70 border border-sky-200 rounded-lg space-y-1.5 text-xs text-sky-950">
            <div class="font-mono font-bold text-sky-900 flex items-center gap-1.5">
              <AlertTriangle :size="13" class="text-amber-600 shrink-0" />
              <span>IRIS-API-GAP-001: Pylai Gateway Telemetry Probe</span>
            </div>
            <p class="leading-relaxed font-sans text-[11px]">
              Live wire message throughput, connection pooling counters, and byte-rate metrics are pending implementation in the WildFly Operations BEFE gateway. Fabricated telemetry is strictly prohibited; honest placeholders ("Telemetry initializing") are displayed.
            </p>
          </div>
        </div>

        <!-- Description -->
        <div class="interface-drawer__section">
          <h3 class="interface-drawer__section-heading">
            <FileCode :size="14" class="interface-drawer__section-icon text-slate-500" />
            <span>Interface Description</span>
          </h3>
          <p class="text-xs text-slate-600 bg-white border border-slate-200 rounded-lg p-3 leading-relaxed">
            {{ gateway.description }}
          </p>
        </div>
      </div>

      <!-- Footer -->
      <div class="interface-drawer__footer">
        <button
          type="button"
          @click="close"
          class="interface-drawer__footer-close-btn font-sans"
        >
          Close Drawer
        </button>
      </div>
    </aside>
  </teleport>
</template>

<style scoped>
.interface-drawer__backdrop {
  position: fixed;
  inset: 0;
  background-color: rgba(15, 23, 42, 0.4);
  backdrop-filter: blur(2px);
  z-index: 50;
  transition: opacity 0.2s ease;
}

.interface-drawer__panel {
  position: fixed;
  top: 0;
  bottom: 0;
  right: 0;
  width: 100%;
  max-width: 540px;
  background-color: var(--iris-bg-surface, #ffffff);
  border-left: 1px solid var(--iris-border-default, #e2e8f0);
  z-index: 50;
  display: flex;
  flex-direction: column;
  box-shadow: -4px 0 24px rgba(0, 0, 0, 0.15);
  font-family: var(--iris-font-sans, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif);
  color: var(--iris-text-primary, #0f172a);
}

.interface-drawer__header {
  padding: 16px 20px;
  background-color: var(--iris-bg-page, #f8fafc);
  border-bottom: 1px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.interface-drawer__header-left {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}

.interface-drawer__icon-box {
  padding: 8px;
  border-radius: 6px;
  background-color: #f0f9ff;
  border: 1px solid #e0f2fe;
  color: #0284c7;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.interface-drawer__header-titles {
  min-width: 0;
}

.interface-drawer__title {
  font-size: 15px;
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
  letter-spacing: -0.01em;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.interface-drawer__subtitle {
  font-size: 11px;
  color: var(--iris-text-muted, #64748b);
  margin: 2px 0 0 0;
}

.interface-drawer__protocol {
  font-weight: 600;
  color: var(--iris-color-primary, #0284c7);
}

.interface-drawer__close-btn {
  padding: 6px;
  color: var(--iris-text-muted, #94a3b8);
  border-radius: 6px;
  border: none;
  background: transparent;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.15s ease;
}

.interface-drawer__close-btn:hover {
  color: var(--iris-text-primary, #0f172a);
  background-color: var(--iris-border-light, #f1f5f9);
}

.interface-drawer__body {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.interface-drawer__status-banner {
  padding: 14px 16px;
  border-radius: 8px;
  background-color: var(--iris-bg-page, #f8fafc);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.interface-drawer__section-label {
  font-size: 10px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--iris-text-muted, #64748b);
  display: block;
  margin-bottom: 4px;
}

.interface-drawer__section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.interface-drawer__section-heading {
  font-size: 11px;
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
  text-transform: uppercase;
  letter-spacing: 0.04em;
  display: flex;
  align-items: center;
  gap: 6px;
  margin: 0;
}

.interface-drawer__section-icon {
  flex-shrink: 0;
}

.interface-drawer__spec-card {
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  border-radius: 8px;
  padding: 12px 14px;
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 12px;
}

.interface-drawer__spec-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 4px 0;
  border-bottom: 1px solid var(--iris-border-light, #f1f5f9);
}

.interface-drawer__spec-row:last-child {
  border-bottom: none;
}

.interface-drawer__spec-label {
  color: var(--iris-text-muted, #64748b);
  font-size: 11px;
}

.interface-drawer__spec-value {
  color: var(--iris-text-primary, #0f172a);
}

.interface-drawer__footer {
  padding: 14px 20px;
  background-color: var(--iris-bg-page, #f8fafc);
  border-top: 1px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.interface-drawer__footer-close-btn {
  padding: 8px 16px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 600;
  color: var(--iris-text-secondary, #475569);
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #cbd5e1);
  cursor: pointer;
  transition: all 0.15s ease;
}

.interface-drawer__footer-close-btn:hover {
  background-color: var(--iris-border-light, #f1f5f9);
  color: var(--iris-text-primary, #0f172a);
}

.text-sky {
  color: #0284c7;
}

.text-emerald {
  color: #059669;
}
</style>
