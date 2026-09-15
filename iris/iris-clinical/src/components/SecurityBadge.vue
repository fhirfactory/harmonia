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
import type { Coding, Meta } from '../models/fhir';
import { Shield, ShieldAlert, ShieldCheck, Lock } from 'lucide-vue-next';

const props = withDefaults(defineProps<{
  security?: Coding[];
  meta?: Meta;
  showIcon?: boolean;
  compact?: boolean;
}>(), {
  showIcon: true,
  compact: false
});

const confidentiality = computed(() => {
  const securityList = props.security || props.meta?.security;
  if (!securityList || securityList.length === 0) {
    return {
      code: 'N',
      display: 'Normal',
      badgeClass: 'badge-green',
      icon: Shield
    };
  }

  const primary = securityList[0];
  const code = (primary.code || 'N').toUpperCase();

  switch (code) {
    case 'R':
      return {
        code: 'R',
        display: primary.display || 'Restricted',
        badgeClass: 'badge-amber',
        icon: Lock
      };
    case 'V':
      return {
        code: 'V',
        display: primary.display || 'Very Restricted',
        badgeClass: 'badge-red',
        icon: ShieldAlert
      };
    case 'U':
      return {
        code: 'U',
        display: primary.display || 'Unrestricted',
        badgeClass: 'badge-blue',
        icon: ShieldCheck
      };
    case 'L':
      return {
        code: 'L',
        display: primary.display || 'Low',
        badgeClass: 'badge-gray',
        icon: Shield
      };
    case 'M':
      return {
        code: 'M',
        display: primary.display || 'Moderate',
        badgeClass: 'badge-amber',
        icon: Shield
      };
    case 'N':
    default:
      return {
        code: code,
        display: primary.display || 'Normal',
        badgeClass: 'badge-green',
        icon: Shield
      };
  }
});
</script>

<template>
  <span 
    class="badge inline-flex items-center gap-1 font-mono" 
    :class="confidentiality.badgeClass"
    :title="`Confidentiality: ${confidentiality.display} (${confidentiality.code})`"
  >
    <component :is="confidentiality.icon" v-if="showIcon" :size="12" class="shrink-0" />
    <span>{{ compact ? confidentiality.code : (confidentiality.display) }}</span>
  </span>
</template>
