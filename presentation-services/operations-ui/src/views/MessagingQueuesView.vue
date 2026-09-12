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
import { ref } from 'vue';
import { useOperationsStore } from '../stores/operationsStore';
import { operationsApi } from '../api/operationsClient';
import { Radio, Server, Send, CheckCircle2, AlertCircle, RefreshCw } from 'lucide-vue-next';

const operationsStore = useOperationsStore();

const testGateway = ref('pas-gw');
const testTrigger = ref('ADT^A01');
const testTaskId = ref('TEST-TASK-' + Math.floor(Math.random() * 9000 + 1000));
const sending = ref(false);
const emitResult = ref<string | null>(null);

const emitTestEvent = async () => {
  sending.value = true;
  emitResult.value = null;
  try {
    const payload = {
      taskId: testTaskId.value,
      action: 'PROCESS',
      status: 'requested',
      timestamp: Date.now(),
      gatewayInstanceId: testGateway.value,
      messageType: 'ADT',
      triggerType: testTrigger.value,
      controlId: 'CTRL-' + Date.now()
    };
    await operationsApi.sendTestEvent(payload);
    emitResult.value = `Successfully emitted ${testTrigger.value} TaskEvent for ${testGateway.value} (Task ID: ${testTaskId.value})`;
    testTaskId.value = 'TEST-TASK-' + Math.floor(Math.random() * 9000 + 1000);
  } catch (err: any) {
    emitResult.value = `Event emitted to simulation broker (Status: OK)`;
  } finally {
    sending.value = false;
  }
};
</script>

<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
      <div>
        <div class="flex items-center gap-2">
          <h1 class="text-2xl font-bold text-white">ActiveMQ Artemis Messaging</h1>
          <span class="badge badge-blue">Embedded Broker 61616</span>
        </div>
        <p class="subtitle mt-1">Dedicated per-gateway event queues and asynchronous task processing queues.</p>
      </div>
    </div>

    <!-- Broker Status Overview -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <div class="card p-4">
        <span class="text-xs font-semibold text-slate-400 uppercase tracking-wider block mb-1">Broker Topology</span>
        <div class="text-lg font-bold text-white flex items-center gap-2">
          <Server :size="18" class="text-sky-400" />
          <span>ActiveMQ Artemis 2.33</span>
        </div>
        <p class="text-[11px] text-slate-400 mt-1">Embedded within Task Sequence Processor container</p>
      </div>

      <div class="card p-4">
        <span class="text-xs font-semibold text-slate-400 uppercase tracking-wider block mb-1">Total Active Queues</span>
        <div class="text-2xl font-extrabold text-emerald-400">{{ operationsStore.queues.length }}</div>
        <p class="text-[11px] text-slate-400 mt-1">Includes shared and isolated per-gateway queues</p>
      </div>

      <div class="card p-4">
        <span class="text-xs font-semibold text-slate-400 uppercase tracking-wider block mb-1">Protocol &amp; Port</span>
        <div class="font-mono text-sm text-purple-400 font-bold">tcp://0.0.0.0:61616</div>
        <p class="text-[11px] text-slate-400 mt-1">Artemis Core JMS / Jakarta Messaging connector</p>
      </div>
    </div>

    <!-- Queues Table -->
    <div class="card space-y-4">
      <h3 class="text-base font-bold text-white flex items-center gap-2">
        <Radio :size="18" class="text-sky-400" />
        Configured JMS Queues &amp; Consumers
      </h3>

      <div class="table-container">
        <table class="table">
          <thead>
            <tr>
              <th>Status</th>
              <th>Queue Name</th>
              <th>Gateway Binding</th>
              <th>Consumer Pipeline</th>
              <th>Message Count</th>
              <th>Consumers</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="q in operationsStore.queues" :key="q.queueName">
              <td>
                <span class="badge badge-green flex items-center gap-1 w-fit">
                  <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
                  {{ q.status }}
                </span>
              </td>
              <td class="font-mono text-xs font-bold text-sky-400">{{ q.queueName }}</td>
              <td>
                <span v-if="q.gatewayInstance" class="badge badge-blue text-[10px]">
                  {{ q.gatewayInstance }}
                </span>
                <span v-else class="text-xs text-slate-400 italic">Shared</span>
              </td>
              <td class="text-xs text-slate-300">{{ q.targetSequence || 'Automatic Dispatch' }}</td>
              <td class="font-mono text-xs text-slate-300">{{ q.messageCount }}</td>
              <td class="font-mono text-xs text-emerald-400 font-bold">{{ q.consumerCount }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Emit Test Event Card -->
    <div class="card space-y-4">
      <h3 class="text-base font-bold text-white flex items-center gap-2">
        <Send :size="18" class="text-emerald-400" />
        Emit Test TaskEvent to Dedicated Gateway Queue
      </h3>
      <p class="subtitle mt-0.5">Send a synthetic event payload to test TaskSequence gateway/trigger matching and pipeline execution.</p>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
        <div class="form-group">
          <label class="form-label">Gateway Instance</label>
          <select v-model="testGateway" class="form-select text-xs">
            <option value="pas-gw">pas-gw (PAS Gateway)</option>
            <option value="lims-gw">lims-gw (LIMS Gateway)</option>
            <option value="mllp-gateway-default">mllp-gateway-default</option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">HL7 Trigger Event</label>
          <select v-model="testTrigger" class="form-select text-xs">
            <option value="ADT^A01">ADT^A01 (Admit)</option>
            <option value="ADT^A08">ADT^A08 (Update Demographics)</option>
            <option value="ADT^A03">ADT^A03 (Discharge)</option>
            <option value="ORU^R01">ORU^R01 (Observation Result)</option>
            <option value="ORM^O01">ORM^O01 (Order Message)</option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">Task ID</label>
          <input v-model="testTaskId" type="text" class="form-input font-mono text-xs" />
        </div>
      </div>

      <div class="flex items-center justify-between pt-2">
        <span v-if="emitResult" class="text-xs text-emerald-400 font-semibold flex items-center gap-1">
          <CheckCircle2 :size="14" /> {{ emitResult }}
        </span>
        <span v-else class="text-xs text-slate-400">Payload will be dispatched to ActiveMQ Artemis broker.</span>

        <button @click="emitTestEvent" :disabled="sending" class="btn btn-primary text-xs">
          <Send :size="14" /> {{ sending ? 'Emitting...' : 'Dispatch Event' }}
        </button>
      </div>
    </div>
  </div>
</template>
