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
  CheckCircle2, Clock, AlertCircle, XCircle, 
  RotateCw, HelpCircle, ShieldCheck
} from 'lucide-vue-next';

const props = defineProps<{
  status: string | boolean;
  type?: 'task' | 'lifecycle' | 'active' | 'generic';
}>();

const formattedStatus = computed(() => {
  if (typeof props.status === 'boolean') {
    return props.status ? 'ACTIVE' : 'INACTIVE';
  }
  return (props.status || 'UNKNOWN').toUpperCase();
});

const badgeClass = computed(() => {
  const s = formattedStatus.value;
  switch (s) {
    case 'COMPLETED':
    case 'ACTIVE':
    case 'APPROVED':
    case 'SUCCESS':
      return 'badge-success';
    case 'ACCEPTED':
    case 'IN_PROGRESS':
    case 'VALIDATING':
    case 'COMMITTING':
    case 'DRAFT':
    case 'PENDING':
    case 'RECEIVED':
      return 'badge-info';
    case 'AWAITING_REVIEW':
    case 'SUSPENDED':
    case 'WARNING':
      return 'badge-warning';
    case 'REJECTED':
    case 'FAILED':
    case 'INACTIVE':
    case 'ERROR':
    case 'RETIRED':
      return 'badge-danger';
    default:
      return 'badge-neutral';
  }
});
</script>

<template>
  <span :class="['badge', badgeClass]">
    <CheckCircle2 v-if="['COMPLETED', 'ACTIVE', 'APPROVED', 'SUCCESS'].includes(formattedStatus)" :size="11" />
    <RotateCw v-else-if="['IN_PROGRESS', 'VALIDATING', 'COMMITTING'].includes(formattedStatus)" :size="11" class="animate-spin" />
    <Clock v-else-if="['ACCEPTED', 'RECEIVED', 'DRAFT', 'PENDING', 'AWAITING_REVIEW'].includes(formattedStatus)" :size="11" />
    <XCircle v-else-if="['REJECTED', 'FAILED', 'ERROR'].includes(formattedStatus)" :size="11" />
    <AlertCircle v-else-if="['INACTIVE', 'SUSPENDED', 'RETIRED'].includes(formattedStatus)" :size="11" />
    <HelpCircle v-else :size="11" />
    <span>{{ formattedStatus }}</span>
  </span>
</template>
