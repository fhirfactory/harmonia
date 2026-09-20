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
import { computed } from 'vue';
import { 
  HeartPulse, 
  Network, 
  Clock, 
  ArrowRight
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import { IrisStatus } from '@harmonia/iris-befe';

const store = useOperationsStore();

const health = computed(() => store.currentHealth);
const isStale = computed(() => store.isStale || health.value?.stale);

const dependencies = computed(() => health.value?.dependencies || []);
</script>

<template>
  <div class="health-dependencies-panel">
    <!-- Panel Header -->
    <div class="health-panel__header">
      <div class="health-panel__header-left">
        <div class="health-panel__icon-box">
          <HeartPulse :size="15" />
        </div>
        <h2 class="health-panel__title">Operational Health &amp; Dependencies</h2>
      </div>
      
      <div v-if="isStale" class="health-panel__stale-warning">
        <Clock :size="13" class="health-panel__stale-icon" />
        <span>Telemetry Snapshot (Stale)</span>
      </div>
      <div v-else class="health-panel__subtitle">
        Live Health Probes &amp; Upstream Latencies
      </div>
    </div>

    <!-- Health Overview Cards -->
    <div class="health-panel__cards-grid">
      <!-- Status -->
      <div class="health-panel__metric-card">
        <span class="health-panel__metric-label">Service Status</span>
        <div class="health-panel__status-wrapper">
          <IrisStatus :status="health?.status || 'UNKNOWN'" size="sm" :stale="isStale" />
        </div>
      </div>

      <!-- Dependencies Ratio -->
      <div class="health-panel__metric-card">
        <span class="health-panel__metric-label">Dependencies</span>
        <span class="health-panel__metric-value font-mono">
          {{ health?.dependenciesSummary || (dependencies.length > 0 ? `${dependencies.length} Connected` : 'None') }}
        </span>
      </div>

      <!-- Availability % -->
      <div class="health-panel__metric-card">
        <span class="health-panel__metric-label">Availability</span>
        <span 
          class="health-panel__metric-value font-mono" 
          :class="health?.availabilityPercent && health.availabilityPercent < 99.0 ? 'text-amber' : 'text-emerald'"
        >
          {{ health?.availabilityPercent !== null && health?.availabilityPercent !== undefined ? `${health.availabilityPercent}%` : 'N/A' }}
        </span>
      </div>

      <!-- P95 Latency -->
      <div class="health-panel__metric-card">
        <span class="health-panel__metric-label">P95 Latency</span>
        <span class="health-panel__metric-value font-mono text-sky">
          {{ health?.p95LatencyMs !== null && health?.p95LatencyMs !== undefined ? `${health.p95LatencyMs} ms` : 'N/A' }}
        </span>
      </div>

      <!-- Failed Operations -->
      <div class="health-panel__metric-card">
        <span class="health-panel__metric-label">Failed Ops</span>
        <span 
          class="health-panel__metric-value font-mono"
          :class="(health?.failedOperations || 0) > 0 ? 'text-rose' : ''"
        >
          {{ health?.failedOperations ?? 0 }}
        </span>
      </div>

      <!-- Restarts -->
      <div class="health-panel__metric-card">
        <span class="health-panel__metric-label">Restarts</span>
        <span 
          class="health-panel__metric-value font-mono"
          :class="(health?.restartCount || 0) > 0 ? 'text-amber' : ''"
        >
          {{ health?.restartCount ?? 0 }}
        </span>
      </div>
    </div>

    <!-- Dependencies Table -->
    <div class="health-panel__deps-section">
      <div class="health-panel__deps-header">
        <h3 class="health-panel__deps-title">
          <Network :size="14" class="health-panel__deps-icon" />
          <span>Subsystem Dependencies &amp; Round-Trip Latency</span>
        </h3>
        <span class="health-panel__deps-count font-mono">{{ dependencies.length }} Downstream Link{{ dependencies.length === 1 ? '' : 's' }}</span>
      </div>

      <div v-if="dependencies.length === 0" class="health-panel__deps-empty">
        No external subsystem dependencies registered for this component.
      </div>

      <div v-else class="health-panel__table-wrapper">
        <table class="health-panel__table" role="table" aria-label="Subsystem Dependencies">
          <thead>
            <tr class="health-panel__thead-row">
              <th class="health-panel__th">Target Subsystem</th>
              <th class="health-panel__th">Status</th>
              <th class="health-panel__th text-right">Round-Trip Latency</th>
              <th class="health-panel__th">Diagnostic Message</th>
            </tr>
          </thead>
          <tbody class="health-panel__tbody">
            <tr 
              v-for="dep in dependencies" 
              :key="dep.name"
              class="health-panel__row"
            >
              <td class="health-panel__td health-panel__td--target">
                <ArrowRight :size="12" class="health-panel__arrow-icon" />
                <span>{{ dep.name }}</span>
              </td>
              <td class="health-panel__td">
                <IrisStatus :status="dep.status" size="sm" />
              </td>
              <td class="health-panel__td text-right font-mono">
                <span v-if="dep.latencyMs !== null && dep.latencyMs !== undefined" class="text-sky font-semibold">
                  {{ dep.latencyMs }} ms
                </span>
                <span v-else class="text-na">N/A</span>
              </td>
              <td class="health-panel__td font-mono text-muted text-truncate" :title="dep.message || 'Operational check passed'">
                {{ dep.message || 'Operational check passed' }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style scoped>
.health-dependencies-panel {
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  font-family: var(--iris-font-sans, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif);
}

.health-panel__header {
  padding: 12px 16px;
  background-color: var(--iris-bg-page, #f8fafc);
  border-bottom: 1px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.health-panel__header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.health-panel__icon-box {
  padding: 4px;
  border-radius: 4px;
  background-color: #ecfdf5;
  color: #047857;
  border: 1px solid #a7f3d0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.health-panel__title {
  font-size: 11px;
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
  letter-spacing: 0.05em;
  text-transform: uppercase;
  margin: 0;
}

.health-panel__stale-warning {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 11px;
  font-family: var(--iris-font-mono, monospace);
  font-weight: 600;
  color: #7e22ce;
}

.health-panel__stale-icon {
  color: #9333ea;
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

.health-panel__subtitle {
  font-size: 11px;
  color: var(--iris-text-muted, #64748b);
}

.health-panel__cards-grid {
  padding: 14px 16px;
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 12px;
  border-bottom: 1px solid var(--iris-border-default, #e2e8f0);
  background-color: rgba(248, 250, 252, 0.5);
}

.health-panel__metric-card {
  padding: 10px 12px;
  border-radius: 6px;
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
}

.health-panel__metric-label {
  font-size: 10px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--iris-text-muted, #64748b);
  display: block;
  margin-bottom: 4px;
}

.health-panel__status-wrapper {
  margin-top: 2px;
}

.health-panel__metric-value {
  font-size: 13px;
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
  display: block;
}

.health-panel__deps-section {
  padding: 14px 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.health-panel__deps-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.health-panel__deps-title {
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

.health-panel__deps-icon {
  color: var(--iris-color-primary, #0284c7);
}

.health-panel__deps-count {
  font-size: 11px;
  color: var(--iris-text-secondary, #64748b);
  font-weight: 600;
}

.health-panel__deps-empty {
  padding: 16px;
  text-align: center;
  font-size: 12px;
  color: var(--iris-text-muted, #64748b);
  font-style: italic;
  background-color: var(--iris-bg-page, #f8fafc);
  border-radius: 6px;
  border: 1px solid var(--iris-border-default, #e2e8f0);
}

.health-panel__table-wrapper {
  overflow-x: auto;
  border-radius: 6px;
  border: 1px solid var(--iris-border-default, #e2e8f0);
}

.health-panel__table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
  font-size: 12px;
}

.health-panel__thead-row {
  background-color: var(--iris-bg-page, #f8fafc);
  color: var(--iris-text-secondary, #475569);
  border-bottom: 1px solid var(--iris-border-default, #e2e8f0);
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.04em;
  text-transform: uppercase;
}

.health-panel__th {
  padding: 8px 12px;
}

.health-panel__tbody {
  color: var(--iris-text-primary, #0f172a);
}

.health-panel__row {
  border-bottom: 1px solid var(--iris-border-light, #f1f5f9);
  transition: background-color 0.15s;
}

.health-panel__row:hover {
  background-color: #f0f9ff;
}

.health-panel__td {
  padding: 8px 12px;
  vertical-align: middle;
}

.health-panel__td--target {
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 6px;
}

.health-panel__arrow-icon {
  color: var(--iris-color-primary, #0284c7);
  flex-shrink: 0;
}

.text-right {
  text-align: right;
}

.font-mono {
  font-family: var(--iris-font-mono, monospace);
}

.font-semibold {
  font-weight: 600;
}

.text-amber {
  color: #b45309;
}

.text-emerald {
  color: #047857;
}

.text-rose {
  color: #b91c1c;
}

.text-sky {
  color: #0369a1;
}

.text-na {
  color: var(--iris-text-muted, #94a3b8);
  font-style: italic;
}

.text-muted {
  color: var(--iris-text-secondary, #64748b);
  font-size: 11px;
}

.text-truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 280px;
}

@media (max-width: 960px) {
  .health-panel__cards-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (max-width: 600px) {
  .health-panel__cards-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: .5; }
}
</style>
