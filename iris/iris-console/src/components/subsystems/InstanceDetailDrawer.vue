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
import { computed, onMounted, onUnmounted } from 'vue';
import { 
  X, 
  Server, 
  Box, 
  Network, 
  Clock, 
  Activity, 
  AlertOctagon, 
  CheckCircle, 
  ShieldAlert
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import { IrisStatus } from '@harmonia/iris-befe';

const store = useOperationsStore();

const isOpen = computed(() => store.isInstanceDrawerOpen);
const instance = computed(() => store.selectedInstance);

const close = () => {
  store.closeInstanceDrawer();
};

const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape' && isOpen.value) {
    close();
  }
};

onMounted(() => {
  window.addEventListener('keydown', handleKeydown);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeydown);
});

const startedAtText = computed(() => {
  if (!instance.value?.startedAt) return 'Unknown';
  return new Date(instance.value.startedAt).toLocaleString();
});
</script>

<template>
  <teleport to="body">
    <!-- Backdrop -->
    <div
      v-if="isOpen"
      class="instance-drawer__backdrop"
      @click="close"
      aria-hidden="true"
    ></div>

    <!-- Drawer Panel -->
    <aside
      v-if="isOpen && instance"
      class="instance-drawer__panel"
      role="dialog"
      aria-modal="true"
      :aria-label="`Instance Details: ${instance.instanceId}`"
    >
      <!-- Header -->
      <div class="instance-drawer__header">
        <div class="instance-drawer__header-left">
          <div class="instance-drawer__icon-box">
            <Server :size="20" />
          </div>
          <div class="instance-drawer__header-titles">
            <h2 class="instance-drawer__title font-mono">
              {{ instance.instanceId }}
            </h2>
            <p class="instance-drawer__subtitle">
              Role: <span class="instance-drawer__role">{{ instance.role || 'Primary' }}</span>
              &bull; Subsystem: <span class="instance-drawer__subsystem font-mono">{{ instance.subsystemId }}</span>
            </p>
          </div>
        </div>

        <button
          type="button"
          @click="close"
          class="instance-drawer__close-btn"
          title="Close drawer (ESC)"
          aria-label="Close drawer"
        >
          <X :size="18" />
        </button>
      </div>

      <!-- Drawer Body -->
      <div class="instance-drawer__body">
        <!-- Status & Readiness Banner -->
        <div class="instance-drawer__status-banner">
          <div>
            <span class="instance-drawer__section-label">State</span>
            <IrisStatus :status="instance.state" size="md" :show-pulse="true" />
          </div>

          <div class="text-right">
            <span class="instance-drawer__section-label">Readiness Probe</span>
            <span 
              class="instance-drawer__readiness"
              :class="instance.ready ? 'is-ready' : 'is-not-ready'"
            >
              <CheckCircle v-if="instance.ready" :size="14" />
              <AlertOctagon v-else :size="14" />
              <span>{{ instance.ready ? 'Ready (Serving)' : 'Not Ready' }}</span>
            </span>
          </div>
        </div>

        <!-- Kubernetes Pod Metadata -->
        <div class="instance-drawer__section">
          <h3 class="instance-drawer__section-heading">
            <Box :size="14" class="instance-drawer__section-icon text-sky" />
            <span>Kubernetes Pod Spec</span>
          </h3>

          <div class="instance-drawer__spec-card font-mono">
            <div class="instance-drawer__spec-row">
              <span class="instance-drawer__spec-label font-sans">Pod Name:</span>
              <span class="instance-drawer__spec-value font-semibold">{{ instance.podName || instance.instanceId }}</span>
            </div>
            <div class="instance-drawer__spec-row">
              <span class="instance-drawer__spec-label font-sans">Namespace:</span>
              <span class="instance-drawer__spec-value text-sky font-semibold">{{ instance.namespace || 'harmonia' }}</span>
            </div>
            <div class="instance-drawer__spec-row">
              <span class="instance-drawer__spec-label font-sans">Node:</span>
              <span class="instance-drawer__spec-value">{{ instance.nodeName || 'microk8s-node-01' }}</span>
            </div>
            <div class="instance-drawer__spec-row">
              <span class="instance-drawer__spec-label font-sans">Pod IP:</span>
              <span class="instance-drawer__spec-value">{{ instance.ipAddress || '10.1.0.42' }}</span>
            </div>
            <div class="instance-drawer__spec-row">
              <span class="instance-drawer__spec-label font-sans">Container Image:</span>
              <span class="instance-drawer__spec-value text-truncate" :title="instance.containerImage || 'docker.io/fhirfactory/harmonia'">
                {{ instance.containerImage || 'harmonia/' + instance.subsystemId + ':1.0.0-SNAPSHOT' }}
              </span>
            </div>
            <div class="instance-drawer__spec-row">
              <span class="instance-drawer__spec-label font-sans">App Version:</span>
              <span class="instance-drawer__spec-value text-emerald font-semibold">{{ instance.appVersion || '1.0.0-SNAPSHOT' }}</span>
            </div>
          </div>
        </div>

        <!-- Runtime Metrics & Restarts -->
        <div class="instance-drawer__section">
          <h3 class="instance-drawer__section-heading">
            <Activity :size="14" class="instance-drawer__section-icon text-emerald" />
            <span>Runtime Resource Telemetry</span>
          </h3>

          <div class="instance-drawer__metrics-grid">
            <!-- Restarts -->
            <div class="instance-drawer__metric-tile">
              <span class="instance-drawer__tile-label">Restarts</span>
              <span 
                class="instance-drawer__tile-value font-mono"
                :class="instance.restartCount > 0 ? 'text-amber' : ''"
              >
                {{ instance.restartCount }}
              </span>
            </div>

            <!-- CPU -->
            <div class="instance-drawer__metric-tile">
              <span class="instance-drawer__tile-label">CPU</span>
              <span class="instance-drawer__tile-value font-mono text-sky">
                {{ instance.cpuPercent !== null && instance.cpuPercent !== undefined ? `${instance.cpuPercent}%` : 'N/A' }}
              </span>
            </div>

            <!-- Memory -->
            <div class="instance-drawer__metric-tile">
              <span class="instance-drawer__tile-label">Memory</span>
              <span class="instance-drawer__tile-value font-mono">
                {{ instance.memoryMb !== null && instance.memoryMb !== undefined ? `${instance.memoryMb}M` : 'N/A' }}
              </span>
            </div>
          </div>

          <div class="instance-drawer__started-row font-mono">
            <span class="font-sans">Started At:</span>
            <span>{{ startedAtText }}</span>
          </div>
        </div>

        <!-- Recent Operational Errors (Zero PHI) -->
        <div class="instance-drawer__section">
          <h3 class="instance-drawer__section-heading">
            <ShieldAlert :size="14" class="instance-drawer__section-icon text-amber" />
            <span>Recent Operational Errors</span>
          </h3>

          <div class="instance-drawer__errors-card">
            <div 
              v-if="instance.recentErrors && instance.recentErrors.length > 0"
              class="instance-drawer__error-list"
            >
              <div 
                v-for="(err, i) in instance.recentErrors"
                :key="i"
                class="instance-drawer__error-item font-mono"
              >
                {{ err }}
              </div>
            </div>
            <div v-else class="instance-drawer__no-errors">
              <CheckCircle :size="14" class="text-emerald shrink-0" />
              <span>No recent operational errors recorded on this runtime instance.</span>
            </div>
          </div>
        </div>

        <!-- Dependencies -->
        <div v-if="instance.dependencies && instance.dependencies.length > 0" class="instance-drawer__section">
          <h3 class="instance-drawer__section-heading">
            <Network :size="14" class="instance-drawer__section-icon text-sky" />
            <span>Associated Endpoints / Dependencies</span>
          </h3>

          <div class="instance-drawer__dep-pills">
            <span 
              v-for="dep in instance.dependencies"
              :key="dep"
              class="instance-drawer__dep-pill font-mono"
            >
              {{ dep }}
            </span>
          </div>
        </div>
      </div>

      <!-- Drawer Footer -->
      <div class="instance-drawer__footer font-mono">
        <span>Zero-PHI Presentation Tier</span>
        <button
          type="button"
          @click="close"
          class="instance-drawer__footer-close-btn font-sans"
        >
          Close Drawer
        </button>
      </div>
    </aside>
  </teleport>
</template>

<style scoped>
.instance-drawer__backdrop {
  position: fixed;
  inset: 0;
  background-color: rgba(15, 23, 42, 0.4);
  backdrop-filter: blur(2px);
  z-index: 50;
  transition: opacity 0.2s ease;
}

.instance-drawer__panel {
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

.instance-drawer__header {
  padding: 16px 20px;
  background-color: var(--iris-bg-page, #f8fafc);
  border-bottom: 1px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.instance-drawer__header-left {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}

.instance-drawer__icon-box {
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

.instance-drawer__header-titles {
  min-width: 0;
}

.instance-drawer__title {
  font-size: 15px;
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
  letter-spacing: -0.01em;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.instance-drawer__subtitle {
  font-size: 11px;
  color: var(--iris-text-muted, #64748b);
  margin: 2px 0 0 0;
}

.instance-drawer__role {
  font-weight: 600;
  color: var(--iris-text-secondary, #475569);
}

.instance-drawer__subsystem {
  color: var(--iris-color-primary, #0284c7);
  text-transform: uppercase;
  font-weight: 600;
}

.instance-drawer__close-btn {
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

.instance-drawer__close-btn:hover {
  color: var(--iris-text-primary, #0f172a);
  background-color: var(--iris-border-light, #f1f5f9);
}

.instance-drawer__body {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.instance-drawer__status-banner {
  padding: 14px 16px;
  border-radius: 8px;
  background-color: var(--iris-bg-page, #f8fafc);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.instance-drawer__section-label {
  font-size: 10px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--iris-text-muted, #64748b);
  display: block;
  margin-bottom: 4px;
}

.instance-drawer__readiness {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  font-weight: 600;
}

.instance-drawer__readiness.is-ready {
  color: #047857;
}

.instance-drawer__readiness.is-not-ready {
  color: #b91c1c;
}

.instance-drawer__section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.instance-drawer__section-heading {
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

.instance-drawer__section-icon {
  flex-shrink: 0;
}

.instance-drawer__spec-card {
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  border-radius: 8px;
  padding: 12px 14px;
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 12px;
}

.instance-drawer__spec-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 4px 0;
  border-bottom: 1px solid var(--iris-border-light, #f1f5f9);
}

.instance-drawer__spec-row:last-child {
  border-bottom: none;
}

.instance-drawer__spec-label {
  color: var(--iris-text-muted, #64748b);
  font-size: 11px;
}

.instance-drawer__spec-value {
  color: var(--iris-text-primary, #0f172a);
}

.instance-drawer__metrics-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 8px;
}

.instance-drawer__metric-tile {
  padding: 10px;
  background-color: var(--iris-bg-page, #f8fafc);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  border-radius: 6px;
  text-align: center;
}

.instance-drawer__tile-label {
  font-size: 10px;
  text-transform: uppercase;
  font-weight: 700;
  color: var(--iris-text-muted, #64748b);
  display: block;
  margin-bottom: 2px;
}

.instance-drawer__tile-value {
  font-size: 16px;
  font-weight: 700;
  display: block;
}

.instance-drawer__started-row {
  background-color: var(--iris-bg-page, #f8fafc);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  border-radius: 6px;
  padding: 10px 12px;
  font-size: 11px;
  display: flex;
  justify-content: space-between;
  color: var(--iris-text-secondary, #475569);
}

.instance-drawer__errors-card {
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  border-radius: 8px;
  padding: 12px;
}

.instance-drawer__error-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.instance-drawer__error-item {
  padding: 8px 10px;
  border-radius: 4px;
  background-color: #fff1f2;
  border: 1px solid #fecdd3;
  color: #9f1239;
  font-size: 11px;
  word-break: break-all;
}

.instance-drawer__no-errors {
  font-size: 12px;
  color: var(--iris-text-secondary, #475569);
  display: flex;
  align-items: center;
  gap: 8px;
}

.instance-drawer__dep-pills {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.instance-drawer__dep-pill {
  padding: 4px 8px;
  border-radius: 4px;
  background-color: var(--iris-border-light, #f1f5f9);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  font-size: 11px;
  color: var(--iris-text-secondary, #475569);
}

.instance-drawer__footer {
  padding: 12px 20px;
  background-color: var(--iris-bg-page, #f8fafc);
  border-top: 1px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 11px;
  color: var(--iris-text-muted, #64748b);
}

.instance-drawer__footer-close-btn {
  padding: 6px 12px;
  border-radius: 6px;
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #cbd5e1);
  color: var(--iris-text-primary, #0f172a);
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.15s ease;
}

.instance-drawer__footer-close-btn:hover {
  background-color: var(--iris-border-light, #f1f5f9);
  border-color: #94a3b8;
}

.text-right {
  text-align: right;
}

.font-mono {
  font-family: var(--iris-font-mono, monospace);
}

.font-sans {
  font-family: var(--iris-font-sans, sans-serif);
}

.font-semibold {
  font-weight: 600;
}

.text-sky {
  color: #0284c7;
}

.text-emerald {
  color: #047857;
}

.text-amber {
  color: #b45309;
}

.text-truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 280px;
}

.shrink-0 {
  flex-shrink: 0;
}
</style>
