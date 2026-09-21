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

/**
 * Status presentation in iris-console is owned entirely by the shared
 * iris-befe IrisStatus primitive. The former local components/common/StatusBadge.vue
 * has been deleted; this spec is retargeted at its replacement so the console
 * keeps an explicit guarantee over the status treatment it renders.
 */

import { describe, it, expect } from 'vitest';
import { mount } from '@vue/test-utils';
import { IrisStatus } from '@harmonia/iris-befe';

describe('IrisStatus (replacement for the local StatusBadge)', () => {
  it('renders HEALTHY status with accessible attributes', () => {
    const wrapper = mount(IrisStatus, { props: { status: 'HEALTHY' } });
    expect(wrapper.text()).toContain('Healthy');
    expect(wrapper.attributes('role')).toBe('status');
    expect(wrapper.attributes('aria-label')).toBe('Healthy status');
  });

  it('renders DEGRADED status', () => {
    const wrapper = mount(IrisStatus, { props: { status: 'DEGRADED' } });
    expect(wrapper.text()).toContain('Degraded');
    expect(wrapper.attributes('aria-label')).toBe('Degraded operational warning');
  });

  it('renders UNAVAILABLE status', () => {
    const wrapper = mount(IrisStatus, { props: { status: 'UNAVAILABLE' } });
    expect(wrapper.text()).toContain('Unavailable');
    expect(wrapper.attributes('aria-label')).toBe('Unavailable failure state');
  });

  it('renders UNKNOWN when status is null or unrecognised', () => {
    expect(mount(IrisStatus, { props: { status: null } }).text()).toContain('Unknown');
    const odd = mount(IrisStatus, { props: { status: 'SOMETHING-ELSE' } });
    expect(odd.attributes('aria-label')).toBe('Unknown operational state');
  });

  it('renders queue idle-like states neutrally while preserving distinct operational state labels', () => {
    const states = [
      { status: 'IDLE', expected: 'Idle' },
      { status: 'PAUSED', expected: 'Paused' },
      { status: 'PENDING', expected: 'Pending' },
      { status: 'INACTIVE', expected: 'Inactive' },
      { status: 'QUEUED', expected: 'Queued' }
    ];
    for (const { status, expected } of states) {
      const wrapper = mount(IrisStatus, { props: { status } });
      expect(wrapper.attributes('aria-label')).toBe(`${expected} operational state`);
      expect(wrapper.classes()).toContain('iris-status--neutral');
      expect(wrapper.classes()).toContain('iris-status--idle');
      expect(wrapper.text()).toContain(expected);
    }
  });

  it('renders stale telemetry when the stale prop is set', () => {
    const wrapper = mount(IrisStatus, { props: { status: 'HEALTHY', stale: true } });
    expect(wrapper.text()).toContain('Stale Telemetry');
    expect(wrapper.attributes('aria-label')).toBe('Stale cached telemetry');
  });

  it('conveys status by symbol and text, not colour alone', () => {
    const wrapper = mount(IrisStatus, { props: { status: 'UNAVAILABLE' } });
    expect(wrapper.find('.iris-status__symbol').exists()).toBe(true);
    expect(wrapper.find('.iris-status__label').text().length).toBeGreaterThan(0);
  });

  it('supports the size variants the console uses', () => {
    expect(mount(IrisStatus, { props: { status: 'HEALTHY', size: 'sm' } }).classes())
      .toContain('iris-status--sm');
    expect(mount(IrisStatus, { props: { status: 'HEALTHY', size: 'lg' } }).classes())
      .toContain('iris-status--lg');
  });

  it('no longer ships the local StatusBadge or the orphaned shell components', () => {
    const modules = (import.meta as any).glob('../components/**/*.vue');
    const paths = Object.keys(modules);
    for (const dead of [
      '../components/common/StatusBadge.vue',
      '../components/Navbar.vue',
      '../components/Sidebar.vue',
      '../components/Topbar.vue'
    ]) {
      expect(paths).not.toContain(dead);
    }
  });
});
