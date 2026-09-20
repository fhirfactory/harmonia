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
  Server, 
  CheckCircle, 
  XCircle, 
  ChevronRight
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import type { OperationalInstance } from '../../models/operations';
import { IrisStatus } from '@harmonia/iris-befe';

const store = useOperationsStore();

const instances = computed(() => store.instances);

const onSelectInstance = (instance: OperationalInstance) => {
  store.openInstanceDrawer(instance);
};
</script>

<template>
  <div class="instance-table-container">
    <!-- Panel Header -->
    <div class="instance-table__header">
      <div class="instance-table__header-left">
        <div class="instance-table__icon-box">
          <Server :size="15" />
        </div>
        <h2 class="instance-table__title">Runtime Instances</h2>
        <span class="instance-table__count-badge">({{ instances.length }})</span>
      </div>
      <span class="instance-table__subtitle">Pod &amp; Cluster Telemetry</span>
    </div>

    <!-- Empty State -->
    <div v-if="instances.length === 0" class="instance-table__empty">
      <Server :size="32" class="instance-table__empty-icon" />
      <p class="instance-table__empty-title">No runtime instances currently discovered</p>
      <p class="instance-table__empty-desc">
        Awaiting Pod lifecycle reports or cluster module status registration for this subsystem.
      </p>
    </div>

    <!-- Table View -->
    <div v-else class="instance-table__wrapper">
      <table class="instance-table__table" role="table" aria-label="Subsystem Instances">
        <thead>
          <tr class="instance-table__thead-row">
            <th class="instance-table__th">Instance ID</th>
            <th class="instance-table__th">Role</th>
            <th class="instance-table__th">State</th>
            <th class="instance-table__th">Readiness</th>
            <th class="instance-table__th text-right">Restarts</th>
            <th class="instance-table__th">Uptime</th>
            <th class="instance-table__th text-right">CPU</th>
            <th class="instance-table__th text-right">Memory</th>
            <th class="instance-table__th text-right">Action</th>
          </tr>
        </thead>
        <tbody class="instance-table__tbody">
          <tr
            v-for="inst in instances"
            :key="inst.instanceId"
            @click="onSelectInstance(inst)"
            class="instance-table__row"
          >
            <!-- Instance Name / ID -->
            <td class="instance-table__td instance-table__td--id">
              <span 
                class="instance-table__status-dot" 
                :class="inst.ready ? 'is-ready' : 'is-not-ready'"
              ></span>
              <span class="instance-table__id-text">{{ inst.instanceId }}</span>
            </td>

            <!-- Role -->
            <td class="instance-table__td">
              <span class="instance-table__role-badge">
                {{ inst.role || 'Primary' }}
              </span>
            </td>

            <!-- State -->
            <td class="instance-table__td">
              <IrisStatus :status="inst.state" size="sm" />
            </td>

            <!-- Readiness -->
            <td class="instance-table__td">
              <span 
                class="instance-table__readiness"
                :class="inst.ready ? 'is-ready' : 'is-not-ready'"
              >
                <CheckCircle v-if="inst.ready" :size="13" />
                <XCircle v-else :size="13" />
                <span>{{ inst.ready ? 'Ready' : 'Not Ready' }}</span>
              </span>
            </td>

            <!-- Restarts -->
            <td class="instance-table__td text-right font-mono" :class="inst.restartCount > 0 ? 'text-amber' : ''">
              {{ inst.restartCount }}
            </td>

            <!-- Uptime -->
            <td class="instance-table__td font-mono">
              {{ inst.uptime || 'N/A' }}
            </td>

            <!-- CPU % (Honest N/A if null/unmeasured) -->
            <td class="instance-table__td text-right font-mono">
              <span v-if="inst.cpuPercent !== null && inst.cpuPercent !== undefined" class="text-sky">
                {{ inst.cpuPercent }}%
              </span>
              <span v-else class="text-na">N/A</span>
            </td>

            <!-- Memory MB (Honest N/A if null/unmeasured) -->
            <td class="instance-table__td text-right font-mono">
              <span v-if="inst.memoryMb !== null && inst.memoryMb !== undefined">
                {{ inst.memoryMb }} MB
              </span>
              <span v-else class="text-na">N/A</span>
            </td>

            <!-- Inspect Action Button -->
            <td class="instance-table__td text-right">
              <button
                type="button"
                @click.stop="onSelectInstance(inst)"
                class="instance-table__inspect-btn"
                title="Inspect instance details"
                aria-label="Inspect instance details"
              >
                <span>Inspect</span>
                <ChevronRight :size="12" class="instance-table__inspect-icon" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.instance-table-container {
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  font-family: var(--iris-font-sans, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif);
}

.instance-table__header {
  padding: 12px 16px;
  background-color: var(--iris-bg-page, #f8fafc);
  border-bottom: 1px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.instance-table__header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.instance-table__icon-box {
  padding: 4px;
  border-radius: 4px;
  background-color: #f0f9ff;
  color: #0284c7;
  border: 1px solid #e0f2fe;
  display: flex;
  align-items: center;
  justify-content: center;
}

.instance-table__title {
  font-size: 11px;
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
  letter-spacing: 0.05em;
  text-transform: uppercase;
  margin: 0;
}

.instance-table__count-badge {
  font-size: 11px;
  color: var(--iris-text-secondary, #64748b);
  font-family: var(--iris-font-mono, monospace);
  font-weight: 600;
}

.instance-table__subtitle {
  font-size: 11px;
  color: var(--iris-text-muted, #64748b);
}

.instance-table__empty {
  padding: 32px 16px;
  text-align: center;
  color: var(--iris-text-muted, #64748b);
}

.instance-table__empty-icon {
  margin: 0 auto 8px auto;
  color: #94a3b8;
}

.instance-table__empty-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--iris-text-primary, #0f172a);
  margin: 0 0 4px 0;
}

.instance-table__empty-desc {
  font-size: 12px;
  color: var(--iris-text-secondary, #64748b);
  max-width: 420px;
  margin: 0 auto;
}

.instance-table__wrapper {
  overflow-x: auto;
}

.instance-table__table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
  font-size: 12px;
}

.instance-table__thead-row {
  background-color: var(--iris-bg-page, #f8fafc);
  color: var(--iris-text-secondary, #475569);
  border-bottom: 1px solid var(--iris-border-default, #e2e8f0);
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.04em;
  text-transform: uppercase;
}

.instance-table__th {
  padding: 8px 14px;
}

.instance-table__tbody {
  color: var(--iris-text-primary, #0f172a);
}

.instance-table__row {
  border-bottom: 1px solid var(--iris-border-light, #f1f5f9);
  cursor: pointer;
  transition: background-color 0.15s;
}

.instance-table__row:hover {
  background-color: #f0f9ff;
}

.instance-table__td {
  padding: 8px 14px;
  vertical-align: middle;
}

.instance-table__td--id {
  font-family: var(--iris-font-mono, monospace);
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.instance-table__status-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  flex-shrink: 0;
}

.instance-table__status-dot.is-ready {
  background-color: #10b981;
}

.instance-table__status-dot.is-not-ready {
  background-color: #f43f5e;
}

.instance-table__role-badge {
  font-size: 10px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  padding: 2px 6px;
  border-radius: 4px;
  background-color: var(--iris-border-light, #f1f5f9);
  color: var(--iris-text-secondary, #475569);
  border: 1px solid var(--iris-border-default, #e2e8f0);
}

.instance-table__readiness {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  font-weight: 600;
  font-size: 11px;
}

.instance-table__readiness.is-ready {
  color: #047857;
}

.instance-table__readiness.is-not-ready {
  color: #b91c1c;
}

.instance-table__inspect-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  border-radius: 4px;
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #cbd5e1);
  color: var(--iris-text-secondary, #475569);
  font-size: 11px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.15s ease;
}

.instance-table__inspect-btn:hover {
  background-color: var(--iris-border-light, #f1f5f9);
  color: var(--iris-text-primary, #0f172a);
  border-color: #94a3b8;
}

.instance-table__inspect-icon {
  color: var(--iris-text-muted, #94a3b8);
}

.text-right {
  text-align: right;
}

.font-mono {
  font-family: var(--iris-font-mono, monospace);
}

.text-amber {
  color: #b45309;
  font-weight: 700;
}

.text-sky {
  color: #0369a1;
  font-weight: 600;
}

.text-na {
  color: var(--iris-text-muted, #94a3b8);
  font-style: italic;
}
</style>
