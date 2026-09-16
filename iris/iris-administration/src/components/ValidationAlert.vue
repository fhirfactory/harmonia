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
import type { ValidationIssue } from '../models/provider';
import { PR_VAL_CODES } from '../models/operationOutcome';
import { AlertCircle, AlertTriangle, Info, XCircle } from 'lucide-vue-next';

const props = defineProps<{
  issues: ValidationIssue[];
  title?: string;
}>();

const hasErrors = computed(() => props.issues.some(i => i.severity === 'error' || i.severity === 'fatal'));
const hasWarnings = computed(() => props.issues.some(i => i.severity === 'warning'));

const getCodeDefinition = (code: string) => {
  return PR_VAL_CODES[code];
};
</script>

<template>
  <div v-if="issues && issues.length > 0" class="space-y-3">
    <div 
      class="p-4 rounded-lg border flex flex-col gap-3"
      :class="hasErrors ? 'bg-red-950/20 border-red-500/40 text-red-200' : hasWarnings ? 'bg-amber-950/20 border-amber-500/40 text-amber-200' : 'bg-sky-950/20 border-sky-500/40 text-sky-200'"
    >
      <div class="flex items-center gap-2 font-bold text-sm">
        <XCircle v-if="hasErrors" :size="18" class="text-red-400 shrink-0" />
        <AlertTriangle v-else-if="hasWarnings" :size="18" class="text-amber-400 shrink-0" />
        <Info v-else :size="18" class="text-sky-400 shrink-0" />
        <span>{{ title || (hasErrors ? 'Validation Errors Encountered' : 'Validation Notice') }}</span>
      </div>

      <div class="space-y-2.5">
        <div 
          v-for="(issue, idx) in issues" 
          :key="idx"
          class="p-3 bg-slate-950/60 rounded border border-slate-800/80 text-xs space-y-1.5"
        >
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-1.5">
              <span 
                class="badge text-[10px] font-mono font-bold"
                :class="issue.severity === 'error' || issue.severity === 'fatal' ? 'badge-danger' : issue.severity === 'warning' ? 'badge-warning' : 'badge-info'"
              >
                {{ issue.code }}
              </span>
              <span class="font-semibold text-white">
                {{ getCodeDefinition(issue.code)?.name || 'Registry Validation Rule' }}
              </span>
            </div>
            <span class="text-[11px] uppercase tracking-wider font-semibold opacity-70">
              {{ issue.severity }}
            </span>
          </div>

          <p class="text-slate-300">
            {{ issue.details }}
          </p>

          <div v-if="issue.expression && issue.expression.length" class="text-[11px] text-slate-400 font-mono">
            Field: <span class="text-sky-300">{{ issue.expression.join(', ') }}</span>
          </div>

          <div v-if="getCodeDefinition(issue.code)?.remediation" class="text-[11px] text-emerald-400/90 pt-1 border-t border-slate-800/50">
            <strong>Remediation:</strong> {{ getCodeDefinition(issue.code)?.remediation }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
