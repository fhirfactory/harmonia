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
  CheckCircle2, 
  AlertTriangle, 
  XCircle, 
  HelpCircle, 
  Clock 
} from 'lucide-vue-next';

const props = withDefaults(defineProps<{
  status: string | null | undefined;
  size?: 'sm' | 'md' | 'lg';
  showPulse?: boolean;
  stale?: boolean;
}>(), {
  size: 'md',
  showPulse: false,
  stale: false
});

type NormalizedStatus = 'HEALTHY' | 'DEGRADED' | 'UNAVAILABLE' | 'UNKNOWN' | 'STALE';

const normalized = computed<NormalizedStatus>(() => {
  if (props.stale) return 'STALE';
  if (!props.status) return 'UNKNOWN';
  const s = props.status.toUpperCase();
  if (s === 'HEALTHY' || s === 'UP' || s === 'RUNNING' || s === 'ACTIVE' || s === 'READY' || s === 'SUCCESS' || s === 'RESOLVED') {
    return 'HEALTHY';
  }
  if (s === 'DEGRADED' || s === 'WARNING' || s === 'WARN' || s === 'RETRYING' || s === 'ACKNOWLEDGED') {
    return 'DEGRADED';
  }
  if (s === 'UNAVAILABLE' || s === 'DOWN' || s === 'FAILED' || s === 'CRITICAL' || s === 'ERROR' || s === 'TERMINATED') {
    return 'UNAVAILABLE';
  }
  return 'UNKNOWN';
});

const label = computed(() => {
  if (props.stale) return 'STALE TELEMETRY';
  if (!props.status) return 'UNKNOWN';
  return props.status.toUpperCase();
});

const config = computed(() => {
  switch (normalized.value) {
    case 'HEALTHY':
      return {
        bg: 'bg-emerald-500/10',
        border: 'border-emerald-500/30',
        text: 'text-emerald-400',
        dot: 'bg-emerald-400',
        icon: CheckCircle2,
        aria: 'Healthy status'
      };
    case 'DEGRADED':
      return {
        bg: 'bg-amber-500/10',
        border: 'border-amber-500/30',
        text: 'text-amber-400',
        dot: 'bg-amber-400',
        icon: AlertTriangle,
        aria: 'Degraded operational warning'
      };
    case 'UNAVAILABLE':
      return {
        bg: 'bg-rose-500/10',
        border: 'border-rose-500/30',
        text: 'text-rose-400',
        dot: 'bg-rose-400',
        icon: XCircle,
        aria: 'Unavailable failure state'
      };
    case 'STALE':
      return {
        bg: 'bg-purple-500/10',
        border: 'border-purple-500/30',
        text: 'text-purple-300',
        dot: 'bg-purple-400',
        icon: Clock,
        aria: 'Stale cached telemetry'
      };
    case 'UNKNOWN':
    default:
      return {
        bg: 'bg-slate-700/20',
        border: 'border-slate-600/40',
        text: 'text-slate-400',
        dot: 'bg-slate-500',
        icon: HelpCircle,
        aria: 'Unknown operational state'
      };
  }
});

const sizeClasses = computed(() => {
  switch (props.size) {
    case 'sm':
      return 'text-[11px] px-2 py-0.5 gap-1.5';
    case 'lg':
      return 'text-sm px-3.5 py-1.5 gap-2';
    case 'md':
    default:
      return 'text-xs px-2.5 py-1 gap-1.5';
  }
});

const iconSize = computed(() => {
  switch (props.size) {
    case 'sm': return 12;
    case 'lg': return 16;
    case 'md': default: return 14;
  }
});
</script>

<template>
  <span 
    class="inline-flex items-center font-medium rounded-full border transition-colors shadow-sm select-none"
    :class="[config.bg, config.border, config.text, sizeClasses]"
    :aria-label="config.aria"
    :title="label"
    role="status"
  >
    <span v-if="showPulse" class="relative flex h-2 w-2">
      <span class="animate-ping absolute inline-flex h-full w-full rounded-full opacity-75" :class="config.dot"></span>
      <span class="relative inline-flex rounded-full h-2 w-2" :class="config.dot"></span>
    </span>
    <component :is="config.icon" :size="iconSize" class="shrink-0" aria-hidden="true" />
    <span class="font-semibold tracking-wide uppercase">{{ label }}</span>
  </span>
</template>
