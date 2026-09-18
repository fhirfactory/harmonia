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
  TrendingUp, 
  Activity, 
  Clock, 
  Zap, 
  Layers, 
  HelpCircle 
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import type { TimeSeries, TimeSeriesPoint } from '../../models/operations';
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
    stroke: '#38bdf8', // sky
    unit: ' evt/s',
    description: 'Inbound message event ingestion rate'
  },
  {
    title: 'Events Out',
    series: getMetricSeries(['events_out', 'eventsOut']),
    stroke: '#818cf8', // indigo
    unit: ' evt/s',
    description: 'Outbound dispatched message rate'
  },
  {
    title: 'Tasks Processed',
    series: getMetricSeries(['tasks_processed', 'tasksProcessed']),
    stroke: '#34d399', // emerald
    unit: ' tasks',
    description: 'Successfully completed Erga & workflow tasks'
  },
  {
    title: 'Tasks Failed',
    series: getMetricSeries(['tasks_failed', 'tasksFailed']),
    stroke: '#f87171', // rose
    unit: ' tasks',
    description: 'Failed or errored activity executions'
  },
  {
    title: 'Processing Rate',
    series: getMetricSeries(['processing_rate', 'processingRate', 'throughput']),
    stroke: '#fbbf24', // amber
    unit: ' op/s',
    description: 'Mean operational throughput'
  },
  {
    title: 'Latency (P95)',
    series: getMetricSeries(['latency_p95', 'latencyP95', 'latency']),
    stroke: '#c084fc', // purple
    unit: ' ms',
    description: '95th percentile operational latency'
  }
]);
</script>

<template>
  <div class="bg-[#151c2c] border border-[#27344d] rounded-xl overflow-hidden shadow-sm">
    <!-- Panel Header with Time Window Selector -->
    <div class="px-4 py-3 bg-slate-900/60 border-b border-[#27344d] flex flex-wrap items-center justify-between gap-3">
      <div class="flex items-center gap-2">
        <BarChart2 :size="16" class="text-sky-400" />
        <h2 class="text-sm font-bold text-white tracking-tight uppercase">Operational Statistics &amp; Metrics</h2>
      </div>

      <!-- Time Window Selector Buttons -->
      <div class="flex items-center gap-1 bg-[#0b0f19] p-1 rounded-lg border border-slate-700/60">
        <button
          v-for="w in timeWindows"
          :key="w"
          @click="onSelectWindow(w)"
          class="px-2.5 py-1 rounded text-xs font-mono font-semibold transition cursor-pointer"
          :class="selectedWindow === w 
            ? 'bg-sky-500 text-white shadow-sm' 
            : 'text-slate-400 hover:text-white hover:bg-slate-800/60'"
          :aria-pressed="selectedWindow === w"
          :title="`View ${w} operational time window`"
        >
          {{ w }}
        </button>
      </div>
    </div>

    <!-- Micro-Charts Grid -->
    <div class="p-4 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
      <div
        v-for="card in metricCards"
        :key="card.title"
        class="p-4 rounded-xl bg-slate-900/40 border border-[#27344d]/80 flex flex-col justify-between hover:border-slate-700 transition"
      >
        <!-- Card Header -->
        <div class="flex items-start justify-between mb-2">
          <div>
            <span class="text-xs font-bold text-white tracking-tight block">{{ card.title }}</span>
            <span class="text-[10px] text-slate-400 leading-tight block">{{ card.description }}</span>
          </div>

          <!-- Value Pill -->
          <div class="text-right font-mono">
            <span 
              v-if="!getLatestValue(card.series).isNa" 
              class="text-base font-bold text-white"
            >
              {{ getLatestValue(card.series).val }}<span class="text-xs text-slate-400 font-normal">{{ card.unit }}</span>
            </span>
            <span v-else class="text-xs font-semibold text-slate-500 italic">
              N/A
            </span>
          </div>
        </div>

        <!-- SVG Sparkline Chart -->
        <div class="mt-2 pt-2 border-t border-slate-800/80">
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
    <div class="px-4 py-2 bg-slate-900/40 border-t border-[#27344d]/60 text-[11px] text-slate-500 font-mono flex items-center justify-between">
      <span>Zero fake metrics: unmeasured series surface as N/A</span>
      <span>Window: {{ selectedWindow }}</span>
    </div>
  </div>
</template>
