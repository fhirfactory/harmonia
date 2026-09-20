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

import { describe, it, expect, beforeEach } from 'vitest';
import { mount } from '@vue/test-utils';
import { createPinia, setActivePinia } from 'pinia';
import { createRouter, createMemoryHistory } from 'vue-router';
import { createIris } from '@harmonia/iris-befe';
import App from '../App.vue';
import { routes } from '../router';

describe('Navigation and Routing', () => {
  let router: any;

  beforeEach(async () => {
    setActivePinia(createPinia());
    router = createRouter({
      history: createMemoryHistory(),
      routes
    });
  });

  it('renders all canonical operational perspectives in the application shell', async () => {
    router.push('/subsystems');
    await router.isReady();

    const wrapper = mount(App, {
      global: {
        plugins: [router, createIris() as any]
      }
    });

    const text = wrapper.text().toUpperCase();
    expect(text).toContain('OVERVIEW');
    expect(text).toContain('SUBSYSTEMS');
    expect(text).toContain('INTERFACES');
    expect(text).toContain('MESSAGES');
    expect(text).toContain('QUEUES');
    expect(text).toContain('WORK');
    expect(text).toContain('WORKFLOWS');
    expect(text).toContain('EVENTS');
    expect(text).toContain('ALERTS');
    expect(text).toContain('HEALTH');
  });

  it('highlights the active perspective route with styled active state', async () => {
    router.push('/subsystems');
    await router.isReady();

    const wrapper = mount(App, {
      global: {
        plugins: [router, createIris() as any]
      }
    });

    const activeLink = wrapper.find('a[href="/subsystems"]');
    expect(activeLink.exists()).toBe(true);
    expect(activeLink.classes()).toContain('iris-primary-nav__link--active');
    expect(activeLink.attributes('aria-current')).toBe('page');
  });

  it('redirects root / to /subsystems', async () => {
    router.push('/');
    await router.isReady();

    expect(router.currentRoute.value.path).toBe('/subsystems');
  });

  it('supports direct bookmarkable routes for all perspectives', async () => {
    for (const path of ['/overview', '/subsystems', '/health', '/interfaces', '/queues', '/workflows', '/events', '/alerts']) {
      await router.push(path);
      expect(router.currentRoute.value.path).toBe(path);
      expect(router.currentRoute.value.matched.length).toBeGreaterThan(0);
    }
  });

  it('renders standard IrisApplicationShell masthead and environment in App.vue', async () => {
    router.push('/subsystems');
    await router.isReady();

    const wrapper = mount(App, {
      global: {
        plugins: [router, createIris() as any]
      }
    });

    expect(wrapper.find('.iris-shell').exists()).toBe(true);
    expect(wrapper.text()).toContain('HARMONIA');
    expect(wrapper.text()).toContain('Operations Console');
    expect(wrapper.text()).toContain('PROD / microk8s-01');
    expect(wrapper.text()).toContain('Subsystems');
    expect(wrapper.text()).toContain('Critical');
    expect(wrapper.text()).toContain('Warn');
  });
});
