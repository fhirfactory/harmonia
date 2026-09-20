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
  Layers, 
  Clock, 
  Server,
  Radio,
  GitMerge,
  Cpu,
  Activity,
  Database,
  ShieldCheck,
  MessageSquare,
  Monitor
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import { findAuthoritativeSubsystem } from '../../models/subsystemHierarchy';
import { IrisSubsystemIdentity } from '@harmonia/iris-befe';

const store = useOperationsStore();

const subsystem = computed(() => store.selectedSubsystem);

const authoritativeInfo = computed(() => {
  if (!subsystem.value) return null;
  return findAuthoritativeSubsystem(subsystem.value.id);
});

const englishTitle = computed(() => {
  return authoritativeInfo.value?.englishTitle || '';
});

const lastUpdatedText = computed(() => {
  if (!subsystem.value?.lastUpdated) return 'Recently';
  return new Date(subsystem.value.lastUpdated).toLocaleTimeString();
});

const subsystemIcons: Record<string, any> = {
  pylai: Radio,
  petasos: Server,
  energeia: GitMerge,
  ponos: Cpu,
  praxis: Activity,
  ergon: GitMerge,
  pragma: Layers,
  mneme: Database,
  mnemosyne: Database,
  calliope: Layers,
  themis: ShieldCheck,
  agora: MessageSquare,
  iris: Monitor
};

const currentIcon = computed(() => {
  if (!subsystem.value) return Layers;
  return subsystemIcons[subsystem.value.id.toLowerCase()] || Layers;
});
</script>

<template>
  <div v-if="subsystem" class="subsystem-header">
    <IrisSubsystemIdentity
      :name="subsystem.name"
      :description="subsystem.description"
      :version="subsystem.version || '1.0.0'"
      :status="subsystem.state"
      :stale="subsystem.stale"
    >
      <template #icon>
        <component :is="currentIcon" :size="24" class="subsystem-header__icon" />
      </template>

      <template #badges>
        <span v-if="englishTitle" class="subsystem-header__english-title">
          — {{ englishTitle }}
        </span>
        <span class="subsystem-header__id-pill">({{ subsystem.id }})</span>
        <span 
          v-if="subsystem.stale" 
          class="subsystem-header__stale-pill"
          title="Telemetry from cached snapshot; live probe unacknowledged"
        >
          <Clock :size="12" />
          <span>Cached Snapshot</span>
        </span>
      </template>

      <template #metadata>
        <div class="subsystem-header__meta-row">
          <!-- Instance Count -->
          <div class="subsystem-header__meta-pill">
            <Layers :size="14" class="subsystem-header__meta-icon" />
            <span class="subsystem-header__meta-value">{{ subsystem.instanceCount }}</span>
            <span class="subsystem-header__meta-label">{{ subsystem.instanceCount === 1 ? 'Instance' : 'Instances' }}</span>
          </div>

          <!-- Last Updated -->
          <div class="subsystem-header__meta-pill">
            <Clock :size="14" class="subsystem-header__meta-icon-muted" />
            <span class="subsystem-header__meta-label">Updated {{ lastUpdatedText }}</span>
          </div>
        </div>
      </template>
    </IrisSubsystemIdentity>
  </div>

  <!-- Empty/Loading State -->
  <div v-else class="subsystem-header__loading">
    <span class="subsystem-header__loading-text">Loading subsystem telemetry...</span>
  </div>
</template>

<style scoped>
.subsystem-header {
  font-family: var(--iris-font-sans, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif);
}

.subsystem-header__icon {
  color: var(--iris-color-primary, #0284c7);
}

.subsystem-header__english-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--iris-text-secondary, #475569);
}

.subsystem-header__id-pill {
  font-size: 11px;
  font-family: var(--iris-font-mono, monospace);
  color: var(--iris-text-muted, #94a3b8);
}

.subsystem-header__stale-pill {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 2px 8px;
  border-radius: 9999px;
  font-size: 11px;
  font-weight: 600;
  background-color: #faf5ff;
  border: 1px solid #e9d5ff;
  color: #7e22ce;
}

.subsystem-header__meta-row {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  font-size: 12px;
  margin-top: 6px;
}

.subsystem-header__meta-pill {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  border-radius: 6px;
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
}

.subsystem-header__meta-icon {
  color: var(--iris-color-primary, #0284c7);
}

.subsystem-header__meta-icon-muted {
  color: var(--iris-text-muted, #94a3b8);
}

.subsystem-header__meta-value {
  font-weight: 700;
  font-family: var(--iris-font-mono, monospace);
  color: var(--iris-text-primary, #0f172a);
}

.subsystem-header__meta-label {
  color: var(--iris-text-secondary, #64748b);
  font-size: 11px;
}

.subsystem-header__loading {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  color: var(--iris-text-muted, #64748b);
}

.subsystem-header__loading-text {
  font-size: 13px;
}
</style>
