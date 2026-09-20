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
import { onMounted, onUnmounted, computed } from 'vue';
import { 
  LayoutDashboard, 
  Server, 
  Network, 
  Radio, 
  GitMerge, 
  Activity, 
  AlertTriangle,
  AlertOctagon,
  Layers,
  HeartPulse,
  ExternalLink
} from 'lucide-vue-next';
import { useOperationsStore } from './stores/operationsStore';
import { IrisApplicationShell, type NavPerspective } from '@harmonia/iris-befe';

const store = useOperationsStore();

onMounted(async () => {
  await store.fetchSummary();
  store.startPolling(10000);
});

onUnmounted(() => {
  store.stopPolling();
});

const environment = computed(() => store.summary?.environment || 'PROD / microk8s-01');
const cluster = computed(() => store.summary?.cluster || 'harmonia-cluster-01');
const platformStatus = computed(() => store.summary?.platformStatus || 'HEALTHY');

const totalSubsystems = computed(() => store.summary?.totalSubsystems ?? store.subsystems.length ?? 9);
const degradedSubsystems = computed(() => store.summary?.degradedSubsystems ?? 0);
const criticalAlerts = computed(() => store.criticalAlertsCount);
const warningAlerts = computed(() => store.warningAlertsCount);

const lastRefreshedText = computed(() => {
  if (!store.lastRefreshed) return 'Just now';
  return store.lastRefreshed.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
});

const navigationItems = computed<NavPerspective[]>(() => [
  {
    id: 'overview',
    label: 'Overview',
    to: '/overview',
    icon: LayoutDashboard
  },
  {
    id: 'subsystems',
    label: 'Subsystems',
    to: '/subsystems',
    icon: Server,
    badge: degradedSubsystems.value > 0 ? String(degradedSubsystems.value) : undefined,
    badgeSeverity: 'warn'
  },
  {
    id: 'health',
    label: 'Health',
    subLabel: 'Matrix',
    to: '/health',
    icon: HeartPulse
  },
  {
    id: 'interfaces',
    label: 'Interfaces',
    subLabel: 'Gateways',
    to: '/interfaces',
    icon: Network
  },
  {
    id: 'messages',
    label: 'Messages',
    subLabel: 'Queues',
    to: '/messages',
    icon: Radio
  },
  {
    id: 'work',
    label: 'Work',
    subLabel: 'Workflows',
    to: '/work',
    icon: GitMerge
  },
  {
    id: 'events',
    label: 'Events',
    to: '/events',
    icon: Activity
  },
  {
    id: 'alerts',
    label: 'Alerts',
    to: '/alerts',
    icon: AlertTriangle,
    badge: (criticalAlerts.value + warningAlerts.value) > 0 ? String(criticalAlerts.value + warningAlerts.value) : undefined,
    badgeSeverity: criticalAlerts.value > 0 ? 'danger' : 'warn'
  }
]);

const handleRefresh = async () => {
  await store.refreshAll();
};
</script>

<template>
  <IrisApplicationShell
    application="Operations Console"
    title="HARMONIA"
    :environment="environment"
    :cluster="cluster"
    :health-state="platformStatus"
    :navigation-items="navigationItems"
    :last-updated="lastRefreshedText"
    :refreshing="store.refreshing"
    footer-text="Harmonia Operations Console • Operations Backend :8090 • Themis Default-Deny RBAC • Infinispan 15 • ActiveMQ Artemis • Zero-PHI Presentation Boundary"
    @refresh="handleRefresh"
  >
    <!-- Secondary Nav Links in Nav Right slot -->
    <template #nav-right>
      <div class="iris-console-secondary-nav">
        <router-link to="/sequences" class="iris-console-secondary-link">Task Sequences</router-link>
        <span class="iris-console-secondary-dot">&bull;</span>
        <router-link to="/caches" class="iris-console-secondary-link">Caches</router-link>
      </div>
    </template>

    <!-- Operational Metric Pills & Clinical Link in Environment Bar -->
    <template #env-right>
      <div class="iris-console-env-metrics">
        <!-- Subsystems count badge pill -->
        <div class="iris-console-pill" title="Subsystem operational status">
          <Layers :size="13" class="iris-console-pill-icon" />
          <span class="iris-console-pill-val">{{ totalSubsystems }}</span>
          <span class="iris-console-pill-label">Subsystems</span>
          <span v-if="degradedSubsystems > 0" class="iris-console-pill-degraded">
            ({{ degradedSubsystems }} Degraded)
          </span>
        </div>

        <!-- Critical Alerts pill -->
        <router-link
          to="/alerts"
          class="iris-console-pill iris-console-pill--clickable"
          :class="{ 'iris-console-pill--danger': criticalAlerts > 0 }"
          title="Active Critical Alerts"
        >
          <AlertOctagon :size="13" :class="criticalAlerts > 0 ? 'iris-console-pill-danger-icon' : 'iris-console-pill-icon'" />
          <span class="iris-console-pill-val">{{ criticalAlerts }}</span>
          <span class="iris-console-pill-label">Critical</span>
        </router-link>

        <!-- Warning Alerts pill -->
        <router-link
          to="/alerts"
          class="iris-console-pill iris-console-pill--clickable"
          :class="{ 'iris-console-pill--warn': warningAlerts > 0 }"
          title="Active Warning Alerts"
        >
          <AlertTriangle :size="13" :class="warningAlerts > 0 ? 'iris-console-pill-warn-icon' : 'iris-console-pill-icon'" />
          <span class="iris-console-pill-val">{{ warningAlerts }}</span>
          <span class="iris-console-pill-label">Warn</span>
        </router-link>

        <!-- Link to Clinical FHIR Explorer (Port 3000) -->
        <a 
          href="http://localhost:3000" 
          target="_blank" 
          rel="noopener noreferrer"
          class="iris-console-fhir-btn"
          title="Open Clinical FHIR Resource Explorer"
        >
          <span>FHIR UI</span>
          <ExternalLink :size="11" />
        </a>
      </div>
    </template>

    <!-- Main Active Perspective Route View -->
    <router-view />
  </IrisApplicationShell>
</template>

<style scoped>
.iris-console-secondary-nav {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.75rem;
  font-family: var(--iris-font-sans, inherit);
}

.iris-console-secondary-link {
  color: var(--iris-text-muted, #64748b);
  text-decoration: none;
  font-weight: 500;
  transition: color 0.15s ease;
}

.iris-console-secondary-link:hover {
  color: var(--iris-text-primary, #0f172a);
}

.iris-console-secondary-dot {
  color: var(--iris-border-strong, #cbd5e1);
}

.iris-console-env-metrics {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.iris-console-pill {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  padding: 2px 8px;
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  border-radius: var(--iris-border-radius, 4px);
  font-size: 0.71875rem;
  font-family: var(--iris-font-mono, monospace);
  color: var(--iris-text-secondary, #475569);
  text-decoration: none;
}

.iris-console-pill--clickable {
  cursor: pointer;
  transition: background-color 0.15s ease, border-color 0.15s ease;
}

.iris-console-pill--clickable:hover {
  background-color: var(--iris-bg-hover, #f1f5f9);
}

.iris-console-pill-icon {
  color: var(--iris-text-muted, #64748b);
}

.iris-console-pill-val {
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
}

.iris-console-pill-label {
  font-family: var(--iris-font-sans, inherit);
  color: var(--iris-text-secondary, #475569);
}

.iris-console-pill-degraded {
  font-family: var(--iris-font-sans, inherit);
  font-weight: 700;
  color: #b45309;
  margin-left: 0.25rem;
}

.iris-console-pill--danger {
  background-color: #fee2e2;
  border-color: #fca5a5;
  color: #991b1b;
}

.iris-console-pill--danger:hover {
  background-color: #fecaca;
}

.iris-console-pill-danger-icon {
  color: #dc2626;
}

.iris-console-pill--danger .iris-console-pill-val {
  color: #991b1b;
}

.iris-console-pill--warn {
  background-color: #fef3c7;
  border-color: #fde68a;
  color: #92400e;
}

.iris-console-pill--warn:hover {
  background-color: #fde68a;
}

.iris-console-pill-warn-icon {
  color: #d97706;
}

.iris-console-pill--warn .iris-console-pill-val {
  color: #92400e;
}

.iris-console-fhir-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  padding: 2px 8px;
  background-color: #e0f2fe;
  border: 1px solid #bae6fd;
  border-radius: var(--iris-border-radius, 4px);
  color: #0369a1;
  font-size: 0.71875rem;
  font-weight: 600;
  text-decoration: none;
  transition: background-color 0.15s ease;
}

.iris-console-fhir-btn:hover {
  background-color: #bae6fd;
}
</style>
