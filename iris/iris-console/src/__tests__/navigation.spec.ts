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
import NavigationTopBar from '../components/common/NavigationTopBar.vue';
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

  it('renders all five canonical operational perspectives in NavigationTopBar', async () => {
    router.push('/subsystems');
    await router.isReady();

    const wrapper = mount(NavigationTopBar, {
      global: {
        plugins: [router]
      }
    });

    const text = wrapper.text().toUpperCase();
    expect(text).toContain('SUBSYSTEMS');
    expect(text).toContain('QUEUES');
    expect(text).toContain('WORKFLOWS');
    expect(text).toContain('EVENTS');
    expect(text).toContain('ALERTS');
  });

  it('highlights the active perspective route', async () => {
    router.push('/subsystems');
    await router.isReady();

    const wrapper = mount(NavigationTopBar, {
      global: {
        plugins: [router]
      }
    });

    const activeLink = wrapper.find('a[href="/subsystems"]');
    expect(activeLink.exists()).toBe(true);
    expect(activeLink.classes()).toContain('text-sky-300');
  });

  it('redirects root / to /subsystems', async () => {
    router.push('/');
    await router.isReady();

    expect(router.currentRoute.value.path).toBe('/subsystems');
  });

  it('supports direct bookmarkable routes for all perspectives', async () => {
    for (const path of ['/subsystems', '/queues', '/workflows', '/events', '/alerts']) {
      await router.push(path);
      expect(router.currentRoute.value.path).toBe(path);
      expect(router.currentRoute.value.matched.length).toBeGreaterThan(0);
    }
  });
});
