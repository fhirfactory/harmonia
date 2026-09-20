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

import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import IrisApplicationShell from '../components/shell/IrisApplicationShell.vue'
import IrisHeader from '../components/shell/IrisHeader.vue'
import IrisEnvironmentBar from '../components/shell/IrisEnvironmentBar.vue'
import IrisPrimaryNavigation from '../components/shell/IrisPrimaryNavigation.vue'
import IrisBreadcrumbs from '../components/shell/IrisBreadcrumbs.vue'
import IrisPage from '../components/shell/IrisPage.vue'

describe('Application Shell Components', () => {
  it('renders IrisApplicationShell with identity, navigation, and content slot', () => {
    const wrapper = mount(IrisApplicationShell, {
      global: {
        stubs: { RouterLink: true, 'router-view': true }
      },
      props: {
        title: 'HARMONIA',
        application: 'MONITOR',
        environment: 'PROD / microk8s-01',
        cluster: 'harmonia-01',
        healthState: 'HEALTHY',
        navigationItems: [
          { id: 'overview', label: 'Overview' },
          { id: 'subsystems', label: 'Subsystems', badge: 0 }
        ]
      },
      slots: {
        default: '<div class="test-body">Operational View</div>'
      }
    })

    expect(wrapper.text()).toContain('HARMONIA')
    expect(wrapper.text()).toContain('MONITOR')
    expect(wrapper.text()).toContain('Env:')
    expect(wrapper.text()).toContain('PROD / microk8s-01')
    expect(wrapper.text()).toContain('Cluster:')
    expect(wrapper.text()).toContain('harmonia-01')
    expect(wrapper.text()).toContain('Overview')
    expect(wrapper.text()).toContain('Subsystems')
    expect(wrapper.find('.test-body').text()).toBe('Operational View')
  })

  it('renders custom header and footer slots in IrisApplicationShell', () => {
    const wrapper = mount(IrisApplicationShell, {
      global: {
        stubs: { RouterLink: true, 'router-view': true }
      },
      slots: {
        header: '<div class="custom-header">Custom Header</div>',
        footer: '<div class="custom-footer">Custom Footer</div>'
      }
    })
    expect(wrapper.find('.custom-header').text()).toBe('Custom Header')
    expect(wrapper.find('.custom-footer').text()).toBe('Custom Footer')
  })

  it('does not impose monitor-specific identity defaults', () => {
    const wrapper = mount(IrisApplicationShell, {
      global: {
        stubs: { RouterLink: true, 'router-view': true }
      }
    })

    expect(wrapper.text()).toContain('HARMONIA')
    expect(wrapper.find('.iris-header__subtitle').exists()).toBe(false)
    expect(wrapper.text()).not.toContain('MONITOR')
    expect(wrapper.text()).not.toContain('Authoritative Operations Console')
  })

  it('emits nav-select when primary navigation item is selected', async () => {
    const wrapper = mount(IrisPrimaryNavigation, {
      global: {
        stubs: { RouterLink: true }
      },
      props: {
        items: [
          { id: 'overview', label: 'Overview' },
          { id: 'subsystems', label: 'Subsystems', badge: '2', badgeSeverity: 'warn' }
        ],
        modelValue: 'overview'
      }
    })

    const buttons = wrapper.findAll('.iris-primary-nav__button')
    expect(buttons.length).toBe(2)
    await buttons[1].trigger('click')

    expect(wrapper.emitted('select')?.[0]).toEqual([
      { id: 'subsystems', label: 'Subsystems', badge: '2', badgeSeverity: 'warn' }
    ])
    expect(wrapper.emitted('update:modelValue')?.[0]).toEqual(['subsystems'])
  })

  it('renders IrisBreadcrumbs with items and custom separator', () => {
    const wrapper = mount(IrisBreadcrumbs, {
      global: {
        stubs: { RouterLink: true }
      },
      props: {
        items: ['Harmonia', 'Integration & Transport', 'Petasos'],
        separator: '>'
      }
    })
    expect(wrapper.text()).toContain('Harmonia')
    expect(wrapper.text()).toContain('>')
    expect(wrapper.text()).toContain('Integration & Transport')
    expect(wrapper.text()).toContain('Petasos')
    expect(wrapper.find('li[aria-current="page"]').text()).toContain('Petasos')
  })

  it('renders IrisPage with header, toolbar and content slots', () => {
    const wrapper = mount(IrisPage, {
      props: {
        title: 'Platform Overview',
        subtitle: 'Health & Runtime Telemetry'
      },
      slots: {
        toolbar: '<div class="test-toolbar">Action Toolbar</div>',
        default: '<div class="test-content">Main Metrics</div>'
      }
    })
    expect(wrapper.text()).toContain('Platform Overview')
    expect(wrapper.text()).toContain('Health & Runtime Telemetry')
    expect(wrapper.find('.test-toolbar').exists()).toBe(true)
    expect(wrapper.find('.test-content').exists()).toBe(true)
  })

  it('renders IrisEnvironmentBar with cluster context and emits refresh', async () => {
    const wrapper = mount(IrisEnvironmentBar, {
      props: {
        environment: 'PROD / microk8s-01',
        cluster: 'harmonia-cluster-01',
        namespace: 'harmonia-core',
        lastUpdated: '11:42:00',
        status: 'HEALTHY',
        showRefresh: true
      }
    })

    expect(wrapper.text()).toContain('PROD / microk8s-01')
    expect(wrapper.text()).toContain('harmonia-cluster-01')
    expect(wrapper.text()).toContain('harmonia-core')
    expect(wrapper.text()).toContain('11:42:00')
    expect(wrapper.text()).toContain('Healthy')

    const refreshBtn = wrapper.find('.iris-env-bar__refresh-btn')
    expect(refreshBtn.exists()).toBe(true)
    await refreshBtn.trigger('click')
    expect(wrapper.emitted('refresh')).toBeTruthy()
  })
})
