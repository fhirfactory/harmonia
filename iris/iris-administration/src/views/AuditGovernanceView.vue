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
import { FolderTree, Shield, Clock } from 'lucide-vue-next';
</script>

<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-white">Audit &amp; Lineage Governance</h1>
      <p class="text-slate-400 text-sm mt-1">
        Harmonia administrative services foundation for Provenance records and immutable AuditEvents.
      </p>
    </div>

    <div class="card space-y-4">
      <div class="flex items-center gap-2 text-indigo-400">
        <FolderTree :size="18" />
        <h3 class="text-base font-bold text-white">Administrative Services Extension Module</h3>
      </div>
      <p class="text-sm text-slate-300">
        This workspace accommodates future enterprise governance and audit inspections across Harmonia services.
      </p>
    </div>
  </div>
</template>
