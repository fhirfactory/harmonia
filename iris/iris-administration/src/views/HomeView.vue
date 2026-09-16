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
import { useSecurityStore } from '../stores/securityStore';
import { 
  User, Briefcase, ClipboardList, Search, 
  Send, Shield, CheckCircle2, ArrowRight, Activity
} from 'lucide-vue-next';

const securityStore = useSecurityStore();
</script>

<template>
  <div class="space-y-6">
    <!-- Welcome Banner -->
    <div class="card bg-gradient-to-r from-sky-950/40 via-slate-900 to-indigo-950/40 border-sky-800/40 p-6">
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div class="space-y-1">
          <div class="flex items-center gap-2">
            <span class="badge badge-info text-xs">Iris Presentation Suite</span>
            <span class="badge badge-success text-xs">Themis ABAC Governed</span>
          </div>
          <h1 class="text-2xl font-bold text-white">Harmonia Administration Console</h1>
          <p class="text-slate-300 text-sm max-w-2xl">
            Unified administrative gateway for Provider Self-Service credential maintenance, departmental registry management, work-queue triage, and governance oversight.
          </p>
        </div>

        <div class="flex flex-wrap gap-2">
          <router-link 
            v-if="securityStore.canSelfService" 
            to="/self-service/request-change" 
            class="btn btn-primary btn-sm"
          >
            <Send :size="14" />
            <span>Request Change</span>
          </router-link>
          <router-link 
            v-if="securityStore.canAdminister" 
            to="/admin/work-queue" 
            class="btn btn-secondary btn-sm"
          >
            <ClipboardList :size="14" />
            <span>Review Work Queue</span>
          </router-link>
        </div>
      </div>
    </div>

    <!-- Active Workspace Quick Navigation Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
      <!-- Self Service Quick Card -->
      <div v-if="securityStore.canSelfService" class="card space-y-4 hover:border-sky-500/50">
        <div class="flex items-center justify-between">
          <div class="w-10 h-10 rounded-lg bg-sky-500/10 border border-sky-500/30 flex items-center justify-center text-sky-400">
            <User :size="20" />
          </div>
          <span class="badge badge-info">Self-Service</span>
        </div>
        <div>
          <h3 class="text-lg font-bold text-white">Provider Self-Service</h3>
          <p class="text-xs text-slate-400 mt-1">
            Review demographics, active roles, affiliated organizations, and submit governed change requests.
          </p>
        </div>
        <div class="pt-2 border-t border-slate-800 flex justify-between items-center text-xs">
          <router-link to="/self-service/my-details" class="text-sky-400 hover:text-sky-300 flex items-center gap-1 font-semibold">
            <span>View My Details</span>
            <ArrowRight :size="13" />
          </router-link>
          <router-link to="/self-service/my-requests" class="text-slate-400 hover:text-slate-200">
            Track Requests
          </router-link>
        </div>
      </div>

      <!-- Departmental Admin Quick Card -->
      <div v-if="securityStore.canAdminister" class="card space-y-4 hover:border-amber-500/50">
        <div class="flex items-center justify-between">
          <div class="w-10 h-10 rounded-lg bg-amber-500/10 border border-amber-500/30 flex items-center justify-center text-amber-400">
            <ClipboardList :size="20" />
          </div>
          <span class="badge badge-warning">Work Queue</span>
        </div>
        <div>
          <h3 class="text-lg font-bold text-white">Departmental Work Queue</h3>
          <p class="text-xs text-slate-400 mt-1">
            Review pending change requests, inspect validation outcomes, and approve or reject submissions.
          </p>
        </div>
        <div class="pt-2 border-t border-slate-800 flex justify-between items-center text-xs">
          <router-link to="/admin/work-queue" class="text-amber-400 hover:text-amber-300 flex items-center gap-1 font-semibold">
            <span>Open Work Queue</span>
            <ArrowRight :size="13" />
          </router-link>
          <router-link to="/admin/dashboard" class="text-slate-400 hover:text-slate-200">
            Metrics
          </router-link>
        </div>
      </div>

      <!-- Provider Search Quick Card -->
      <div v-if="securityStore.canSearch" class="card space-y-4 hover:border-emerald-500/50">
        <div class="flex items-center justify-between">
          <div class="w-10 h-10 rounded-lg bg-emerald-500/10 border border-emerald-500/30 flex items-center justify-center text-emerald-400">
            <Search :size="20" />
          </div>
          <span class="badge badge-success">Directory</span>
        </div>
        <div>
          <h3 class="text-lg font-bold text-white">Provider Search</h3>
          <p class="text-xs text-slate-400 mt-1">
            Multi-criteria search across practitioners, professional roles, organizations, and service locations.
          </p>
        </div>
        <div class="pt-2 border-t border-slate-800 flex justify-between items-center text-xs">
          <router-link to="/admin/search" class="text-emerald-400 hover:text-emerald-300 flex items-center gap-1 font-semibold">
            <span>Search Providers</span>
            <ArrowRight :size="13" />
          </router-link>
          <router-link to="/admin/data-quality" class="text-slate-400 hover:text-slate-200">
            Data Quality
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>
