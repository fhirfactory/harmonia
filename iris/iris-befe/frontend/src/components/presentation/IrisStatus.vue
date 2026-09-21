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
import { computed } from 'vue'
import {
  CheckCircle2,
  AlertTriangle,
  XCircle,
  HelpCircle,
  Clock,
  PauseCircle
} from 'lucide-vue-next'
import type { StatusState } from '../../types'

const props = withDefaults(
  defineProps<{
    status: StatusState | string | null | undefined
    size?: 'sm' | 'md' | 'lg'
    showPulse?: boolean
    stale?: boolean
    showIcon?: boolean
    showSymbol?: boolean
    labelFormat?: 'title' | 'upper' | 'raw'
  }>(),
  {
    size: 'md',
    showPulse: false,
    stale: false,
    showIcon: true,
    showSymbol: true,
    labelFormat: 'title'
  }
)

type SemanticCategory = 'HEALTHY' | 'DEGRADED' | 'UNAVAILABLE' | 'NEUTRAL' | 'UNKNOWN' | 'STALE'

const semanticCategory = computed<SemanticCategory>(() => {
  if (props.stale) return 'STALE'
  if (!props.status) return 'UNKNOWN'
  const s = String(props.status).toUpperCase()
  if (
    s === 'HEALTHY' ||
    s === 'UP' ||
    s === 'RUNNING' ||
    s === 'ACTIVE' ||
    s === 'READY' ||
    s === 'SUCCESS' ||
    s === 'RESOLVED'
  ) {
    return 'HEALTHY'
  }
  if (
    s === 'DEGRADED' ||
    s === 'WARNING' ||
    s === 'WARN' ||
    s === 'RETRYING' ||
    s === 'ACKNOWLEDGED'
  ) {
    return 'DEGRADED'
  }
  if (
    s === 'UNAVAILABLE' ||
    s === 'DOWN' ||
    s === 'FAILED' ||
    s === 'CRITICAL' ||
    s === 'ERROR' ||
    s === 'TERMINATED'
  ) {
    return 'UNAVAILABLE'
  }
  if (
    s === 'IDLE' ||
    s === 'PAUSED' ||
    s === 'PENDING' ||
    s === 'INACTIVE' ||
    s === 'QUEUED' ||
    s === 'NEUTRAL'
  ) {
    return 'NEUTRAL'
  }
  return 'UNKNOWN'
})

const label = computed(() => {
  if (props.stale) {
    return props.labelFormat === 'upper' ? 'STALE TELEMETRY' : 'Stale Telemetry'
  }
  if (!props.status) {
    return props.labelFormat === 'upper' ? 'UNKNOWN' : 'Unknown'
  }
  if (props.labelFormat === 'raw') {
    return String(props.status)
  }
  if (props.labelFormat === 'upper') {
    return String(props.status).toUpperCase()
  }

  // title case mapping - preserves actual operational state meaning
  const s = String(props.status).toUpperCase()
  switch (s) {
    case 'HEALTHY':
    case 'UP':
    case 'RUNNING':
    case 'ACTIVE':
    case 'READY':
    case 'SUCCESS':
    case 'RESOLVED':
      return 'Healthy'
    case 'DEGRADED':
    case 'WARNING':
    case 'WARN':
    case 'RETRYING':
    case 'ACKNOWLEDGED':
      return 'Degraded'
    case 'UNAVAILABLE':
    case 'DOWN':
    case 'FAILED':
    case 'CRITICAL':
    case 'ERROR':
    case 'TERMINATED':
      return 'Unavailable'
    case 'IDLE':
      return 'Idle'
    case 'PAUSED':
      return 'Paused'
    case 'PENDING':
      return 'Pending'
    case 'INACTIVE':
      return 'Inactive'
    case 'QUEUED':
      return 'Queued'
    case 'NEUTRAL':
      return 'Neutral'
    case 'UNKNOWN':
      return 'Unknown'
    default:
      return String(props.status).charAt(0).toUpperCase() + String(props.status).slice(1).toLowerCase()
  }
})

const config = computed(() => {
  switch (semanticCategory.value) {
    case 'HEALTHY':
      return {
        variantClass: 'iris-status--healthy',
        dotClass: 'iris-status-dot--healthy',
        icon: CheckCircle2,
        symbol: '●',
        aria: 'Healthy status'
      }
    case 'DEGRADED':
      return {
        variantClass: 'iris-status--degraded',
        dotClass: 'iris-status-dot--degraded',
        icon: AlertTriangle,
        symbol: '▲',
        aria: 'Degraded operational warning'
      }
    case 'UNAVAILABLE':
      return {
        variantClass: 'iris-status--unavailable',
        dotClass: 'iris-status-dot--unavailable',
        icon: XCircle,
        symbol: '✖',
        aria: 'Unavailable failure state'
      }
    case 'NEUTRAL':
      return {
        variantClass: 'iris-status--neutral iris-status--idle',
        dotClass: 'iris-status-dot--neutral iris-status-dot--idle',
        icon: PauseCircle,
        symbol: '‖',
        aria: `${label.value} operational state`
      }
    case 'STALE':
      return {
        variantClass: 'iris-status--stale',
        dotClass: 'iris-status-dot--stale',
        icon: Clock,
        symbol: '⏱',
        aria: 'Stale cached telemetry'
      }
    case 'UNKNOWN':
    default:
      return {
        variantClass: 'iris-status--unknown',
        dotClass: 'iris-status-dot--unknown',
        icon: HelpCircle,
        symbol: '?',
        aria: 'Unknown operational state'
      }
  }
})

const iconSize = computed(() => {
  switch (props.size) {
    case 'sm':
      return 12
    case 'lg':
      return 16
    case 'md':
    default:
      return 14
  }
})
</script>

<template>
  <span
    class="iris-status"
    :class="[config.variantClass, `iris-status--${props.size}`]"
    :aria-label="config.aria"
    :title="label"
    role="status"
  >
    <span v-if="showPulse" class="iris-status-pulse" aria-hidden="true">
      <span class="iris-status-pulse__ring" :class="config.dotClass"></span>
      <span class="iris-status-pulse__dot" :class="config.dotClass"></span>
    </span>

    <span v-if="showSymbol" class="iris-status__symbol" aria-hidden="true">
      {{ config.symbol }}
    </span>

    <component
      :is="config.icon"
      v-if="showIcon"
      :size="iconSize"
      class="iris-status__icon"
      aria-hidden="true"
    />

    <span class="iris-status__label">
      {{ label }}
      <span v-if="stale && props.status" class="iris-status__stale-tag">(Stale)</span>
    </span>
  </span>
</template>

<style scoped>
.iris-status {
  display: inline-flex;
  align-items: center;
  font-family: var(--iris-font-sans);
  font-weight: 500;
  border-radius: 9999px;
  border: 1px solid transparent;
  user-select: none;
  white-space: nowrap;
  transition: background-color 0.15s ease, border-color 0.15s ease;
}

.iris-status--sm {
  font-size: 11px;
  padding: 2px 8px;
  gap: 5px;
}

.iris-status--md {
  font-size: 12px;
  padding: 4px 10px;
  gap: 6px;
}

.iris-status--lg {
  font-size: 14px;
  padding: 6px 14px;
  gap: 8px;
}

.iris-status--healthy {
  background-color: var(--iris-status-healthy-bg);
  color: var(--iris-status-healthy-text);
  border-color: var(--iris-status-healthy-border);
}

.iris-status--degraded {
  background-color: var(--iris-status-degraded-bg);
  color: var(--iris-status-degraded-text);
  border-color: var(--iris-status-degraded-border);
}

.iris-status--unavailable {
  background-color: var(--iris-status-unavailable-bg);
  color: var(--iris-status-unavailable-text);
  border-color: var(--iris-status-unavailable-border);
}

.iris-status--neutral,
.iris-status--idle {
  background-color: var(--iris-status-neutral-bg, var(--iris-status-idle-bg));
  color: var(--iris-status-neutral-text, var(--iris-status-idle-text));
  border-color: var(--iris-status-neutral-border, var(--iris-status-idle-border));
}

.iris-status--stale {
  background-color: #f3e8ff;
  color: #6b21a8;
  border-color: #d8b4fe;
}

.iris-status--unknown {
  background-color: var(--iris-status-unknown-bg);
  color: var(--iris-status-unknown-text);
  border-color: var(--iris-status-unknown-border);
}

.iris-status__symbol {
  font-size: 0.8em;
  line-height: 1;
}

.iris-status__icon {
  flex-shrink: 0;
}

.iris-status__label {
  font-weight: 600;
  letter-spacing: 0.02em;
}

.iris-status__stale-tag {
  margin-left: 4px;
  font-weight: normal;
  opacity: 0.85;
}

.iris-status-pulse {
  position: relative;
  display: inline-flex;
  width: 8px;
  height: 8px;
}

.iris-status-pulse__ring {
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 9999px;
  opacity: 0.75;
  animation: iris-ping 1.5s cubic-bezier(0, 0, 0.2, 1) infinite;
}

.iris-status-pulse__dot {
  position: relative;
  width: 8px;
  height: 8px;
  border-radius: 9999px;
}

.iris-status-dot--healthy {
  background-color: var(--iris-status-healthy-text);
}

.iris-status-dot--degraded {
  background-color: var(--iris-status-degraded-text);
}

.iris-status-dot--unavailable {
  background-color: var(--iris-status-unavailable-text);
}

.iris-status-dot--neutral,
.iris-status-dot--idle {
  background-color: var(--iris-status-neutral-text, var(--iris-status-idle-text));
}

.iris-status-dot--stale {
  background-color: #6b21a8;
}

.iris-status-dot--unknown {
  background-color: var(--iris-status-unknown-text);
}

@keyframes iris-ping {
  75%, 100% {
    transform: scale(2);
    opacity: 0;
  }
}
</style>
