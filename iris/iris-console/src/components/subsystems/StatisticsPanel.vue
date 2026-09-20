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
  BarChart2, 
  Clock 
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import type { TimeSeries } from '../../models/operations';
import SvgTimeSeriesChart from '../common/SvgTimeSeriesChart.vue';

const store = useOperationsStore();

const timeWindows: Array<'15m' | '1h' | '6h' | '24h'> = ['15m', '1h', '6h', '24h'];

const selectedWindow = computed(() => store.selectedWindow);
const stats = computed(() => store.statistics || {});

const onSelectWindow = (w: '15m' | '1h' | '6h' | '24h') => {
  store.setWindow(w);
};

// Helper to look up a metric with key fallbacks
const getMetricSeries = (keys: string[]): TimeSeries | null => {
  for (const k of keys) {
    if (stats.value[k]) return stats.value[k];
  }
  return null;
};

const getLatestValue = (series: TimeSeries | null): { val: string; isNa: boolean } => {
  if (!series || !series.points || series.points.length === 0) {
    return { val: 'N/A', isNa: true };
  }
  const last = series.points[series.points.length - 1];
  if (last.value === null || last.value === undefined || isNaN(last.value)) {
    return { val: 'N/A', isNa: true };
  }
  return { val: String(last.value), isNa: false };
};

// Metric Cards Configurations
const metricCards = computed(() => [
  {
    title: 'Events In',
    series: getMetricSeries(['events_in', 'eventsIn']),
    stroke: '#0284c7', // sky 600
    unit: ' evt/s',
    description: 'Inbound message event ingestion rate'
  },
  {
    title: 'Events Out',
    series: getMetricSeries(['events_out', 'eventsOut']),
    stroke: '#4f46e5', // indigo 600
    unit: ' evt/s',
    description: 'Outbound dispatched message rate'
  },
  {
    title: 'Tasks Processed',
    series: getMetricSeries(['tasks_processed', 'tasksProcessed']),
    stroke: '#059669', // emerald 600
    unit: ' tasks',
    description: 'Successfully completed Erga & workflow tasks'
  },
  {
    title: 'Tasks Failed',
    series: getMetricSeries(['tasks_failed', 'tasksFailed']),
    stroke: '#dc2626', // rose 600
    unit: ' tasks',
    description: 'Failed or errored activity executions'
  },
  {
    title: 'Processing Rate',
    series: getMetricSeries(['processing_rate', 'processingRate', 'throughput']),
    stroke: '#d97706', // amber 600
    unit: ' op/s',
    description: 'Mean operational throughput'
  },
  {
    title: 'Latency (P95)',
    series: getMetricSeries(['latency_p95', 'latencyP95', 'latency']),
    stroke: '#9333ea', // purple 600
    unit: ' ms',
    description: '95th percentile operational latency'
  }
]);
</script>

<template>
  <div class="statistics-panel">
    <!-- Panel Header with Time Window Selector -->
    <div class="stats-panel__header">
      <div class="stats-panel__header-left">
        <div class="stats-panel__icon-box">
          <BarChart2 :size="15" />
        </div>
        <h2 class="stats-panel__title">Operational Statistics &amp; Metrics</h2>
      </div>

      <!-- Time Window Selector Buttons -->
      <div class="stats-panel__window-selector">
        <button
          v-for="w in timeWindows"
          :key="w"
          type="button"
          @click="onSelectWindow(w)"
          class="stats-panel__window-btn"
          :class="{ 'is-active': selectedWindow === w }"
          :aria-pressed="selectedWindow === w"
          :title="`View ${w} operational time window`"
        >
          {{ w }}
        </button>
      </div>
    </div>

    <!-- Micro-Charts Grid -->
    <div class="stats-panel__grid">
      <div
        v-for="card in metricCards"
        :key="card.title"
        class="stats-panel__card"
      >
        <!-- Card Header -->
        <div class="stats-panel__card-header">
          <div>
            <span class="stats-panel__card-title">{{ card.title }}</span>
            <span class="stats-panel__card-desc">{{ card.description }}</span>
          </div>

          <!-- Value Pill -->
          <div class="stats-panel__card-value font-mono">
            <span 
              v-if="!getLatestValue(card.series).isNa" 
              class="stats-panel__val-num"
            >
              {{ getLatestValue(card.series).val }}<span class="stats-panel__val-unit">{{ card.unit }}</span>
            </span>
            <span v-else class="stats-panel__val-na">
              N/A
            </span>
          </div>
        </div>

        <!-- SVG Sparkline Chart -->
        <div class="stats-panel__chart-wrapper">
          <SvgTimeSeriesChart
            :points="card.series?.points || []"
            :stroke-color="card.stroke"
            :fill-color="card.stroke"
            :height="56"
            :unit="card.unit"
            :metric-name="card.title"
            :show-min-max="true"
            empty-text="N/A (No metric points)"
          />
        </div>
      </div>
    </div>

    <!-- Notice Footer -->
    <div class="stats-panel__footer">
      <span>Zero fake metrics: unmeasured series surface as N/A</span>
      <span>Window: {{ selectedWindow }}</span>
    </div>
  </div>
</template>

<style scoped>
.statistics-panel {
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  font-family: var(--iris-font-sans, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif);
}

.stats-panel__header {
  padding: 12px 16px;
  background-color: var(--iris-bg-page, #f8fafc);
  border-bottom: 1px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.stats-panel__header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stats-panel__icon-box {
  padding: 4px;
  border-radius: 4px;
  background-color: #f0f9ff;
  color: #0284c7;
  border: 1px solid #e0f2fe;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stats-panel__title {
  font-size: 11px;
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
  letter-spacing: 0.05em;
  text-transform: uppercase;
  margin: 0;
}

.stats-panel__window-selector {
  display: flex;
  align-items: center;
  gap: 2px;
  background-color: var(--iris-border-light, #f1f5f9);
  padding: 2px;
  border-radius: 6px;
  border: 1px solid var(--iris-border-default, #e2e8f0);
}

.stats-panel__window-btn {
  padding: 3px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-family: var(--iris-font-mono, monospace);
  font-weight: 600;
  border: 1px solid transparent;
  background: transparent;
  color: var(--iris-text-secondary, #475569);
  cursor: pointer;
  transition: all 0.15s ease;
}

.stats-panel__window-btn:hover {
  color: var(--iris-text-primary, #0f172a);
  background-color: rgba(255, 255, 255, 0.6);
}

.stats-panel__window-btn.is-active {
  background-color: var(--iris-bg-surface, #ffffff);
  color: var(--iris-color-primary, #0284c7);
  border-color: var(--iris-border-default, #e2e8f0);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.stats-panel__grid {
  padding: 16px;
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

.stats-panel__card {
  padding: 14px;
  border-radius: 6px;
  background-color: var(--iris-bg-page, #f8fafc);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  transition: border-color 0.15s;
}

.stats-panel__card:hover {
  border-color: #cbd5e1;
}

.stats-panel__card-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 8px;
}

.stats-panel__card-title {
  font-size: 12px;
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
  display: block;
  letter-spacing: -0.01em;
}

.stats-panel__card-desc {
  font-size: 10px;
  color: var(--iris-text-muted, #64748b);
  display: block;
  margin-top: 2px;
  line-height: 1.3;
}

.stats-panel__card-value {
  text-align: right;
}

.stats-panel__val-num {
  font-size: 14px;
  font-weight: 700;
  color: var(--iris-text-primary, #0f172a);
}

.stats-panel__val-unit {
  font-size: 11px;
  color: var(--iris-text-muted, #64748b);
  font-weight: 400;
}

.stats-panel__val-na {
  font-size: 12px;
  font-weight: 600;
  color: var(--iris-text-muted, #94a3b8);
  font-style: italic;
}

.stats-panel__chart-wrapper {
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid var(--iris-border-default, #e2e8f0);
}

.stats-panel__footer {
  padding: 8px 16px;
  background-color: var(--iris-bg-page, #f8fafc);
  border-top: 1px solid var(--iris-border-default, #e2e8f0);
  font-size: 11px;
  color: var(--iris-text-muted, #64748b);
  font-family: var(--iris-font-mono, monospace);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.font-mono {
  font-family: var(--iris-font-mono, monospace);
}

@media (max-width: 960px) {
  .stats-panel__grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 600px) {
  .stats-panel__grid {
    grid-template-columns: 1fr;
  }
}
</style>
