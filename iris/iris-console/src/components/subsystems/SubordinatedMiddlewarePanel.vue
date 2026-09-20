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
import { Cpu, Server, Layers, HardDrive, Radio, ShieldCheck, CheckCircle2 } from 'lucide-vue-next';
import type { SubordinatedMiddleware } from '../../models/subsystemHierarchy';

defineProps<{
  middleware: SubordinatedMiddleware;
  subsystemName: string;
}>();
</script>

<template>
  <div class="subordinated-middleware">
    <div class="subordinated-middleware__header">
      <div class="subordinated-middleware__header-left">
        <div class="subordinated-middleware__icon-box">
          <Layers :size="18" />
        </div>
        <div>
          <div class="subordinated-middleware__badge-row">
            <span class="subordinated-middleware__section-tag">Subordinated Middleware Runtime</span>
            <span class="subordinated-middleware__managed-badge">
              <CheckCircle2 :size="12" /> Managed
            </span>
          </div>
          <h3 class="subordinated-middleware__title">
            {{ middleware.name }}
          </h3>
        </div>
      </div>

      <div class="subordinated-middleware__header-right">
        <span class="subordinated-middleware__ports-badge">
          Ports: {{ middleware.ports }}
        </span>
      </div>
    </div>

    <p class="subordinated-middleware__description">
      {{ middleware.description }}
    </p>

    <div class="subordinated-middleware__grid">
      <!-- Clustered Runtime Nodes -->
      <div v-if="middleware.nodes && middleware.nodes.length > 0" class="subordinated-middleware__card">
        <div class="subordinated-middleware__card-title">
          <Server :size="14" class="subordinated-middleware__card-icon" />
          <span>Active Runtime Nodes / Pods</span>
        </div>
        <ul class="subordinated-middleware__node-list">
          <li
            v-for="(node, idx) in middleware.nodes"
            :key="idx"
            class="subordinated-middleware__node-item"
          >
            <span class="subordinated-middleware__node-name">{{ node }}</span>
            <span class="subordinated-middleware__online-badge">Online</span>
          </li>
        </ul>
      </div>

      <!-- Telemetry & Metric Highlights -->
      <div v-if="middleware.metrics && middleware.metrics.length > 0" class="subordinated-middleware__card">
        <div class="subordinated-middleware__card-title">
          <Radio :size="14" class="subordinated-middleware__card-icon" />
          <span>Operational Characteristics</span>
        </div>
        <ul class="subordinated-middleware__metric-list">
          <li
            v-for="(metric, idx) in middleware.metrics"
            :key="idx"
            class="subordinated-middleware__metric-item"
          >
            <span class="subordinated-middleware__metric-label">{{ metric.label }}</span>
            <span class="subordinated-middleware__metric-value">{{ metric.value }}</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
.subordinated-middleware {
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  border-radius: 8px;
  padding: 16px 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  font-family: var(--iris-font-sans, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif);
}

.subordinated-middleware__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--iris-border-light, #f1f5f9);
}

.subordinated-middleware__header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.subordinated-middleware__icon-box {
  padding: 8px;
  border-radius: 6px;
  background-color: #f0f9ff;
  color: #0284c7;
  border: 1px solid #e0f2fe;
  display: flex;
  align-items: center;
  justify-content: center;
}

.subordinated-middleware__badge-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.subordinated-middleware__section-tag {
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--iris-text-muted, #64748b);
}

.subordinated-middleware__managed-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 11px;
  font-weight: 600;
  padding: 1px 8px;
  border-radius: 9999px;
  background-color: #ecfdf5;
  color: #047857;
  border: 1px solid #a7f3d0;
}

.subordinated-middleware__title {
  font-size: 15px;
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
  margin: 2px 0 0 0;
  letter-spacing: -0.01em;
}

.subordinated-middleware__header-right {
  text-align: right;
  flex-shrink: 0;
}

.subordinated-middleware__ports-badge {
  font-size: 11px;
  font-family: var(--iris-font-mono, monospace);
  background-color: var(--iris-border-light, #f1f5f9);
  color: var(--iris-text-primary, #0f172a);
  padding: 4px 8px;
  border-radius: 4px;
  border: 1px solid var(--iris-border-default, #e2e8f0);
}

.subordinated-middleware__description {
  font-size: 12px;
  color: var(--iris-text-secondary, #475569);
  margin: 12px 0 0 0;
  line-height: 1.5;
}

.subordinated-middleware__grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
  margin-top: 14px;
  padding-top: 14px;
  border-top: 1px solid var(--iris-border-light, #f1f5f9);
}

.subordinated-middleware__card {
  background-color: var(--iris-bg-page, #f8fafc);
  border-radius: 6px;
  padding: 12px;
  border: 1px solid var(--iris-border-default, #e2e8f0);
}

.subordinated-middleware__card-title {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 11px;
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 0.03em;
}

.subordinated-middleware__card-icon {
  color: var(--iris-text-muted, #64748b);
}

.subordinated-middleware__node-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.subordinated-middleware__node-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 12px;
  font-family: var(--iris-font-mono, monospace);
  background-color: var(--iris-bg-surface, #ffffff);
  padding: 6px 10px;
  border-radius: 4px;
  border: 1px solid var(--iris-border-default, #e2e8f0);
  color: var(--iris-text-primary, #0f172a);
}

.subordinated-middleware__node-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.subordinated-middleware__online-badge {
  font-size: 10px;
  text-transform: uppercase;
  font-family: var(--iris-font-sans, sans-serif);
  font-weight: 700;
  color: #047857;
  background-color: #ecfdf5;
  padding: 1px 6px;
  border-radius: 4px;
  border: 1px solid #a7f3d0;
  flex-shrink: 0;
}

.subordinated-middleware__metric-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.subordinated-middleware__metric-item {
  font-size: 12px;
  background-color: var(--iris-bg-surface, #ffffff);
  padding: 6px 10px;
  border-radius: 4px;
  border: 1px solid var(--iris-border-default, #e2e8f0);
  color: var(--iris-text-secondary, #475569);
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.subordinated-middleware__metric-label {
  font-size: 10px;
  text-transform: uppercase;
  font-weight: 700;
  color: var(--iris-text-muted, #64748b);
}

.subordinated-middleware__metric-value {
  font-family: var(--iris-font-mono, monospace);
  color: var(--iris-text-primary, #0f172a);
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

@media (max-width: 640px) {
  .subordinated-middleware__grid {
    grid-template-columns: 1fr;
  }
}
</style>
