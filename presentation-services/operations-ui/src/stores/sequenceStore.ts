/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { TaskSequence } from '../models/operations';
import { operationsApi } from '../api/operationsClient';

export const useSequenceStore = defineStore('sequence', () => {
  const sequences = ref<TaskSequence[]>([]);
  const selectedSequence = ref<TaskSequence | null>(null);
  const loading = ref(false);
  const syncing = ref(false);
  const syncMessage = ref<string | null>(null);
  const error = ref<string | null>(null);

  const fetchSequences = async () => {
    loading.value = true;
    error.value = null;
    try {
      const data = await operationsApi.getSequences();
      sequences.value = Array.isArray(data) ? data : [];
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch task sequences';
    } finally {
      loading.value = false;
    }
  };

  const fetchSequence = async (sequenceId: string) => {
    loading.value = true;
    error.value = null;
    try {
      const data = await operationsApi.getSequence(sequenceId);
      selectedSequence.value = data;
      return data;
    } catch (err: any) {
      error.value = err.message || `Failed to fetch sequence ${sequenceId}`;
      return null;
    } finally {
      loading.value = false;
    }
  };

  const saveSequence = async (seq: Partial<TaskSequence>) => {
    loading.value = true;
    error.value = null;
    try {
      let saved: TaskSequence;
      if (seq.sequenceId && sequences.value.some(s => s.sequenceId === seq.sequenceId)) {
        saved = await operationsApi.updateSequence(seq.sequenceId, seq);
      } else {
        saved = await operationsApi.createSequence(seq);
      }
      await fetchSequences();
      return saved;
    } catch (err: any) {
      error.value = err.message || 'Failed to save sequence';
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const toggleSequence = async (seq: TaskSequence) => {
    try {
      const updated = { ...seq, enabled: !seq.enabled };
      await saveSequence(updated);
    } catch (err: any) {
      error.value = err.message || 'Failed to toggle sequence status';
    }
  };

  const deleteSequence = async (sequenceId: string) => {
    loading.value = true;
    error.value = null;
    try {
      await operationsApi.deleteSequence(sequenceId);
      sequences.value = sequences.value.filter(s => s.sequenceId !== sequenceId);
    } catch (err: any) {
      error.value = err.message || `Failed to delete sequence ${sequenceId}`;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const syncSequences = async () => {
    syncing.value = true;
    error.value = null;
    syncMessage.value = null;
    try {
      const res = await operationsApi.sync();
      syncMessage.value = res.message || 'Queues and sequences synchronized successfully';
      await fetchSequences();
      return res;
    } catch (err: any) {
      error.value = err.message || 'Failed to synchronize with Task Sequence Processor';
      throw err;
    } finally {
      syncing.value = false;
      setTimeout(() => {
        syncMessage.value = null;
      }, 5000);
    }
  };

  return {
    sequences,
    selectedSequence,
    loading,
    syncing,
    syncMessage,
    error,
    fetchSequences,
    fetchSequence,
    saveSequence,
    toggleSequence,
    deleteSequence,
    syncSequences
  };
});
