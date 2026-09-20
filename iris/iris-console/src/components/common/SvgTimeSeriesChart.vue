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
import type { TimeSeriesPoint } from '../../models/operations';

const props = withDefaults(defineProps<{
  points?: TimeSeriesPoint[];
  strokeColor?: string;
  fillColor?: string;
  height?: number;
  width?: string | number;
  unit?: string;
  metricName?: string;
  showMinMax?: boolean;
  emptyText?: string;
}>(), {
  points: () => [],
  strokeColor: '#38bdf8',
  fillColor: '#38bdf8',
  height: 64,
  width: '100%',
  unit: '',
  showMinMax: false,
  emptyText: 'N/A'
});

const chartId = `chart-${Math.random().toString(36).substring(2, 9)}`;

const validPoints = computed(() => {
  if (!props.points || !Array.isArray(props.points)) return [];
  return props.points.filter(p => typeof p.value === 'number' && !isNaN(p.value));
});

const hasData = computed(() => validPoints.value.length >= 2);

const stats = computed(() => {
  if (!hasData.value) return { min: 0, max: 0, latest: 0 };
  const values = validPoints.value.map(p => p.value);
  const min = Math.min(...values);
  const max = Math.max(...values);
  const latest = values[values.length - 1];
  return { min, max, latest };
});

const viewBoxWidth = 300;
const viewBoxHeight = computed(() => props.height);
const padY = 6;
const padX = 4;

const pathData = computed(() => {
  if (!hasData.value) return { line: '', area: '' };

  const pts = validPoints.value;
  const n = pts.length;
  let min = stats.value.min;
  let max = stats.value.max;
  
  if (min === max) {
    min = min - 1;
    max = max + 1;
  }

  const range = max - min;
  const usableH = viewBoxHeight.value - padY * 2;
  const usableW = viewBoxWidth - padX * 2;

  const coords = pts.map((pt, idx) => {
    const x = padX + (idx / (n - 1)) * usableW;
    const y = viewBoxHeight.value - padY - ((pt.value - min) / range) * usableH;
    return { x, y };
  });

  const linePath = coords.map((c, i) => `${i === 0 ? 'M' : 'L'} ${c.x.toFixed(1)} ${c.y.toFixed(1)}`).join(' ');
  const areaBottom = viewBoxHeight.value - padY;
  const areaPath = `${linePath} L ${coords[coords.length - 1].x.toFixed(1)} ${areaBottom} L ${coords[0].x.toFixed(1)} ${areaBottom} Z`;

  return { line: linePath, area: areaPath };
});
</script>

<template>
  <div class="relative w-full flex flex-col justify-end" :style="{ height: `${height}px` }">
    <div v-if="!hasData" class="flex items-center justify-center h-full text-slate-500 text-xs italic font-mono select-none">
      {{ emptyText }}
    </div>

    <svg
      v-else
      class="w-full h-full overflow-visible"
      :viewBox="`0 0 ${viewBoxWidth} ${viewBoxHeight}`"
      preserveAspectRatio="none"
      role="img"
      :aria-label="metricName ? `${metricName} trend chart` : 'Time series sparkline'"
    >
      <defs>
        <linearGradient :id="`grad-${chartId}`" x1="0%" y1="0%" x2="0%" y2="100%">
          <stop offset="0%" :stop-color="fillColor" stop-opacity="0.28" />
          <stop offset="100%" :stop-color="fillColor" stop-opacity="0.0" />
        </linearGradient>
      </defs>

      <!-- Fill Area -->
      <path
        :d="pathData.area"
        :fill="`url(#grad-${chartId})`"
      />

      <!-- Trend Line -->
      <path
        :d="pathData.line"
        fill="none"
        :stroke="strokeColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
      />
    </svg>

    <div v-if="hasData && showMinMax" class="flex justify-between items-center text-[10px] text-slate-400 font-mono mt-1 px-1">
      <span>Min: {{ stats.min }}{{ unit }}</span>
      <span>Max: {{ stats.max }}{{ unit }}</span>
    </div>
  </div>
</template>
