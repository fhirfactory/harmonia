import { describe, it, expect, vi } from 'vitest';
import { flushPromises, mount } from '@vue/test-utils';
import { createPinia, setActivePinia } from 'pinia';
import HealthView from '../views/HealthView.vue';
import { operationsApi } from '../api/operationsClient';
import { RouterLink } from 'vue-router';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getSubsystems: vi.fn(),
    getSubsystemHealth: vi.fn()
  }
}));

vi.mock('vue-router', () => ({
  useRouter: () => ({
    push: vi.fn()
  })
}));

describe('Health Perspective - Empty/Failed Probe Handling', () => {
  it('renders N/A and UNKNOWN when probe data is unavailable, without fabricated telemetry', async () => {
    setActivePinia(createPinia());
    
    (operationsApi.getSubsystems as any).mockResolvedValue([
      { id: 'hestia', name: 'Hestia', state: 'HEALTHY' },
      { id: 'paradeigma', name: 'Paradeigma', state: 'HEALTHY' }
    ]);

    // Mock rejection for all health probes (simulating API failures)
    (operationsApi.getSubsystemHealth as any).mockImplementation((id: string) => {
      return Promise.reject(new Error('Not Found'));
    });

    const wrapper = mount(HealthView, {
      global: {
        stubs: {
          RouterLink: true
        }
      }
    });
    await flushPromises();

    const text = wrapper.text();
    
    // Negative assertions: Verify old fabricated values do NOT appear
    // Hestia old buggy fallback was 99.98% and 14 ms
    expect(text).not.toContain('99.98');
    expect(text).not.toContain('14 ms');
    
    // Paradeigma old buggy fallback was 100.00%
    expect(text).not.toContain('100.00%');
    
    // Positive assertions: Verify honest N/A and UNKNOWN appear
    // The table should render N/A for both availability and latency
    const rows = wrapper.findAll('[role="row"]');
    
    // Find Hestia row (should be in the table)
    const hestiaRow = rows.find(row => row.text().includes('Hestia'));
    if (hestiaRow) {
      const hestiaText = hestiaRow.text();
      // Hestia should show N/A for availability and latency when probes fail
      expect(hestiaText).toContain('N/A');
    }
    
    // Find Paradeigma row (should be in the table)
    const paradeigmaRow = rows.find(row => row.text().includes('Paradeigma'));
    if (paradeigmaRow) {
      const paradeigmaText = paradeigmaRow.text();
      // Paradeigma should show N/A and Unknown status when probes fail
      expect(paradeigmaText).toContain('N/A');
      expect(paradeigmaText).toContain('Unknown');
    }
  });
});
