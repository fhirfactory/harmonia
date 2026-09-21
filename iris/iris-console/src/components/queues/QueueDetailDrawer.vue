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
import type { QueueSummary } from '../../models/operations';
import { IrisStatus } from '@harmonia/iris-befe';
import SvgTimeSeriesChart from '../common/SvgTimeSeriesChart.vue';
import { 
  X, 
  Radio, 
  Activity, 
  ShieldCheck, 
  Clock 
} from 'lucide-vue-next';

const props = withDefaults(
  defineProps<{
    queue: QueueSummary | null;
    isOpen: boolean;
    teleport?: boolean;
  }>(),
  {
    queue: null,
    isOpen: false,
    teleport: false
  }
);

const emit = defineEmits<{
  (e: 'close'): void;
}>();

function handleKeydown(e: KeyboardEvent) {
  if (e.key === 'Escape' && props.isOpen) {
    emit('close');
  }
}

onMounted(() => {
  window.addEventListener('keydown', handleKeydown);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeydown);
});

function formatRate(rate?: number | null): string {
  if (rate == null || isNaN(rate)) return 'N/A';
  return `${rate.toFixed(1)} msg/sec`;
}

function formatAge(seconds?: number | null): string {
  if (seconds == null || isNaN(seconds)) return 'N/A';
  if (seconds === 0) return '0s (no queued messages)';
  if (seconds < 60) return `${seconds}s`;
  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;
  if (mins < 60) return `${mins}m ${secs}s`;
  const hrs = Math.floor(mins / 60);
  return `${hrs}h ${mins % 60}m`;
}

function formatCount(val?: number | null): string {
  if (val == null || isNaN(val)) return 'N/A';
  return val.toLocaleString();
}

const depthPoints = computed(() => {
  if (!props.queue?.depthHistory || props.queue.depthHistory.length < 2) {
    return [];
  }
  return props.queue.depthHistory;
});
</script>

<template>
  <teleport to="body" :disabled="!teleport">
    <div
      v-if="isOpen && queue"
      class="queue-drawer"
      role="dialog"
      aria-modal="true"
      :aria-label="`Queue details: ${queue.queueName}`"
    >
      <div class="queue-drawer__backdrop" aria-hidden="true" @click="emit('close')"></div>

      <aside class="queue-drawer__panel">
        <!-- Header -->
        <div class="queue-drawer__header">
          <div class="queue-drawer__identity">
            <Radio :size="18" class="queue-drawer__identity-icon" aria-hidden="true" />
            <div class="queue-drawer__identity-text">
              <h2 class="queue-drawer__title">{{ queue.queueName }}</h2>
              <p v-if="queue.associatedCapability" class="queue-drawer__subtitle">
                {{ queue.associatedCapability }}
              </p>
            </div>
          </div>

          <button
            type="button"
            class="queue-drawer__close"
            aria-label="Close queue details drawer"
            title="Close drawer (Esc)"
            @click="emit('close')"
          >
            <X :size="16" aria-hidden="true" />
          </button>
        </div>

        <!-- Body -->
        <div class="queue-drawer__body">
          <div class="queue-drawer__banner">
            <div class="queue-drawer__banner-block">
              <span class="queue-drawer__label">Queue status</span>
              <IrisStatus :status="queue.status || 'UNKNOWN'" size="md" :show-pulse="true" label-format="upper" />
            </div>
            <div class="queue-drawer__banner-block queue-drawer__banner-block--end">
              <span class="queue-drawer__label">Address</span>
              <span class="queue-drawer__mono">{{ queue.address || queue.queueName }}</span>
            </div>
          </div>

          <section class="queue-drawer__section">
            <h3 class="queue-drawer__heading">
              <Activity :size="13" aria-hidden="true" />
              <span>Messaging telemetry</span>
            </h3>

            <ul class="queue-drawer__metrics">
              <li class="queue-drawer__metric">
                <span class="queue-drawer__label">Current depth</span>
                <span class="queue-drawer__metric-value">{{ formatCount(queue.depth) }}</span>
              </li>
              <li class="queue-drawer__metric">
                <span class="queue-drawer__label">Consumers</span>
                <span class="queue-drawer__metric-value">{{ formatCount(queue.consumerCount) }}</span>
              </li>
              <li class="queue-drawer__metric">
                <span class="queue-drawer__label">Producers</span>
                <span class="queue-drawer__metric-value">{{ formatCount(queue.producerCount) }}</span>
              </li>
              <li class="queue-drawer__metric">
                <span class="queue-drawer__label">DLQ messages</span>
                <span class="queue-drawer__metric-value">{{ formatCount(queue.dlqDepth) }}</span>
              </li>
            </ul>
          </section>

          <ul class="queue-drawer__facts">
            <li class="queue-drawer__fact">
              <span class="queue-drawer__fact-label">Enqueue rate</span>
              <span class="queue-drawer__mono">{{ formatRate(queue.enqueueRate) }}</span>
            </li>
            <li class="queue-drawer__fact">
              <span class="queue-drawer__fact-label">Dequeue rate</span>
              <span class="queue-drawer__mono">{{ formatRate(queue.dequeueRate) }}</span>
            </li>
            <li class="queue-drawer__fact">
              <span class="queue-drawer__fact-label">Oldest message age</span>
              <span class="queue-drawer__mono">{{ formatAge(queue.oldestMessageAgeSeconds) }}</span>
            </li>
            <li class="queue-drawer__fact">
              <span class="queue-drawer__fact-label">Associated capability</span>
              <span v-if="queue.associatedCapability">{{ queue.associatedCapability }}</span>
              <span v-else class="queue-drawer__not-reported">Not reported by the operations API</span>
            </li>
          </ul>

          <section class="queue-drawer__section">
            <h3 class="queue-drawer__heading">
              <Clock :size="13" aria-hidden="true" />
              <span>Historical depth trend</span>
            </h3>

            <div class="queue-drawer__chart">
              <SvgTimeSeriesChart
                v-if="depthPoints.length > 0"
                :data="depthPoints"
                color="#0284c7"
                :height="100"
                unit=" msgs"
              />
              <p v-else class="queue-drawer__not-reported">
                The operations API reported no depth history for this queue.
              </p>
            </div>
          </section>

          <p class="queue-drawer__phi" role="note">
            <ShieldCheck :size="14" aria-hidden="true" />
            <span>
              <strong>Zero-PHI safe telemetry.</strong>
              Petasos treats payload buffers as opaque streams. No patient names, MRNs or clinical
              observations are logged or inspected.
            </span>
          </p>
        </div>
      </aside>
    </div>
  </teleport>
</template>

<style scoped>
.queue-drawer {
  position: fixed;
  inset: 0;
  z-index: 50;
  overflow: hidden;
  font-family: var(--iris-font-sans);
}

.queue-drawer__backdrop {
  position: fixed;
  inset: 0;
  background-color: rgba(15, 23, 42, 0.4);
}

.queue-drawer__panel {
  position: fixed;
  top: 0;
  bottom: 0;
  right: 0;
  z-index: 50;
  display: flex;
  flex-direction: column;
  width: min(560px, 100%);
  background-color: var(--iris-bg-surface);
  border-left: 1px solid var(--iris-border-default);
  color: var(--iris-text-primary);
}

.queue-drawer__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  background-color: var(--iris-bg-subtle);
  border-bottom: 1px solid var(--iris-border-default);
}

.queue-drawer__identity {
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
  min-width: 0;
}

.queue-drawer__identity-icon {
  flex-shrink: 0;
  margin-top: 2px;
  color: var(--iris-text-accent);
}

.queue-drawer__identity-text {
  min-width: 0;
}

.queue-drawer__title {
  margin: 0;
  font-family: var(--iris-font-mono);
  font-size: 0.9375rem;
  font-weight: 700;
  word-break: break-all;
}

.queue-drawer__subtitle {
  margin: 2px 0 0 0;
  font-size: 0.75rem;
  color: var(--iris-text-muted);
}

.queue-drawer__close {
  flex-shrink: 0;
  display: inline-flex;
  padding: 4px;
  color: var(--iris-text-muted);
  background: transparent;
  border: 1px solid transparent;
  border-radius: var(--iris-border-radius);
  cursor: pointer;
}

.queue-drawer__close:hover {
  color: var(--iris-text-primary);
  background-color: var(--iris-bg-hover);
  border-color: var(--iris-border-default);
}

.queue-drawer__body {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 0.875rem;
  padding: 1rem;
}

.queue-drawer__banner {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 0.75rem;
  padding: 0.625rem 0.75rem;
  background-color: var(--iris-bg-subtle);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
}

.queue-drawer__banner-block {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  min-width: 0;
}

.queue-drawer__banner-block--end {
  align-items: flex-end;
  text-align: right;
}

.queue-drawer__label {
  font-size: 0.6875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--iris-text-muted);
}

.queue-drawer__mono {
  font-family: var(--iris-font-mono);
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--iris-text-primary);
  word-break: break-all;
}

.queue-drawer__section {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.queue-drawer__heading {
  display: flex;
  align-items: center;
  gap: 0.375rem;
  margin: 0;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--iris-text-secondary);
}

.queue-drawer__metrics {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(110px, 1fr));
  gap: 0.375rem;
  margin: 0;
  padding: 0;
  list-style: none;
}

.queue-drawer__metric {
  display: flex;
  flex-direction: column;
  gap: 0.125rem;
  padding: 0.375rem 0.5rem;
  background-color: var(--iris-bg-subtle);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
}

.queue-drawer__metric-value {
  font-family: var(--iris-font-mono);
  font-size: 1rem;
  font-weight: 700;
}

.queue-drawer__facts {
  display: flex;
  flex-direction: column;
  margin: 0;
  padding: 0.25rem 0.75rem;
  list-style: none;
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  font-size: 0.8125rem;
}

.queue-drawer__fact {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 0.75rem;
  padding: 0.3125rem 0;
  border-bottom: 1px solid var(--iris-border-subtle);
}

.queue-drawer__fact:last-child {
  border-bottom: none;
}

.queue-drawer__fact-label {
  color: var(--iris-text-muted);
}

.queue-drawer__chart {
  padding: 0.75rem;
  background-color: var(--iris-bg-subtle);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
}

.queue-drawer__not-reported {
  margin: 0;
  font-size: 0.75rem;
  font-style: italic;
  color: var(--iris-text-muted);
}

.queue-drawer__phi {
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
  margin: 0;
  padding: 0.5rem 0.75rem;
  font-size: 0.75rem;
  line-height: 1.45;
  color: var(--iris-text-secondary);
  background-color: var(--iris-bg-subtle);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
}

.queue-drawer__phi svg {
  flex-shrink: 0;
  margin-top: 1px;
}
</style>
