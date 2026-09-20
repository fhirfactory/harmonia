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

import { describe, it, expect } from 'vitest';
import { mount } from '@vue/test-utils';
import StatusBadge from '../components/common/StatusBadge.vue';

describe('StatusBadge.vue', () => {
  it('renders HEALTHY status correctly with accessible attributes', () => {
    const wrapper = mount(StatusBadge, {
      props: { status: 'HEALTHY' }
    });
    expect(wrapper.text()).toContain('HEALTHY');
    expect(wrapper.attributes('role')).toBe('status');
    expect(wrapper.attributes('aria-label')).toBe('Healthy status');
    expect(wrapper.classes()).toContain('text-emerald-400');
  });

  it('renders DEGRADED status correctly', () => {
    const wrapper = mount(StatusBadge, {
      props: { status: 'DEGRADED' }
    });
    expect(wrapper.text()).toContain('DEGRADED');
    expect(wrapper.attributes('aria-label')).toBe('Degraded operational warning');
    expect(wrapper.classes()).toContain('text-amber-400');
  });

  it('renders UNAVAILABLE status correctly', () => {
    const wrapper = mount(StatusBadge, {
      props: { status: 'UNAVAILABLE' }
    });
    expect(wrapper.text()).toContain('UNAVAILABLE');
    expect(wrapper.attributes('aria-label')).toBe('Unavailable failure state');
    expect(wrapper.classes()).toContain('text-rose-400');
  });

  it('renders UNKNOWN when status is null or unrecognized', () => {
    const wrapper = mount(StatusBadge, {
      props: { status: null }
    });
    expect(wrapper.text()).toContain('UNKNOWN');
    expect(wrapper.attributes('aria-label')).toBe('Unknown operational state');
  });

  it('renders STALE TELEMETRY when stale prop is true', () => {
    const wrapper = mount(StatusBadge, {
      props: { status: 'HEALTHY', stale: true }
    });
    expect(wrapper.text()).toContain('STALE TELEMETRY');
    expect(wrapper.attributes('aria-label')).toBe('Stale cached telemetry');
    expect(wrapper.classes()).toContain('text-purple-300');
  });

  it('handles size prop variants (sm, md, lg)', () => {
    const small = mount(StatusBadge, { props: { status: 'HEALTHY', size: 'sm' } });
    expect(small.classes()).toContain('text-[11px]');

    const large = mount(StatusBadge, { props: { status: 'HEALTHY', size: 'lg' } });
    expect(large.classes()).toContain('text-sm');
  });
});
