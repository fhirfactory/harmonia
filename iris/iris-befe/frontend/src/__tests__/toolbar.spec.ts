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
import IrisToolbar from '../components/shell/IrisToolbar.vue'

describe('IrisToolbar.vue', () => {
  it('dispatches refresh event when refresh button is clicked', async () => {
    const wrapper = mount(IrisToolbar, {
      props: { showRefresh: true }
    })
    const refreshBtn = wrapper.find('button[aria-label="Refresh telemetry data"]')
    expect(refreshBtn.exists()).toBe(true)
    await refreshBtn.trigger('click')
    expect(wrapper.emitted('refresh')).toBeTruthy()
  })

  it('disables refresh button when refreshing prop is true', () => {
    const wrapper = mount(IrisToolbar, {
      props: { showRefresh: true, refreshing: true }
    })
    const refreshBtn = wrapper.find('button[aria-label="Refresh telemetry data"]')
    expect(refreshBtn.attributes('disabled')).toBeDefined()
    expect(refreshBtn.classes()).toContain('iris-toolbar__btn--loading')
  })

  it('handles search input two-way binding', async () => {
    const wrapper = mount(IrisToolbar, {
      props: {
        showSearch: true,
        searchQuery: ''
      }
    })
    const input = wrapper.find('input[type="search"]')
    expect(input.exists()).toBe(true)
    await input.setValue('petasos')
    expect(wrapper.emitted('update:searchQuery')?.[0]).toEqual(['petasos'])
  })

  it('emits update:timeWindow when time window option is clicked', async () => {
    const wrapper = mount(IrisToolbar, {
      props: {
        showTimeWindow: true,
        timeWindow: '1h',
        timeWindowOptions: ['15m', '1h', '6h', '24h']
      }
    })
    const buttons = wrapper.findAll('.iris-toolbar__group-btn')
    expect(buttons.length).toBe(4)
    await buttons[0].trigger('click')
    expect(wrapper.emitted('update:timeWindow')?.[0]).toEqual(['15m'])
  })

  it('dispatches custom action events when action buttons are clicked', async () => {
    const wrapper = mount(IrisToolbar, {
      props: {
        actions: [
          { id: 'inspect', label: 'Inspect Topology', primary: true },
          { id: 'purge', label: 'Purge' }
        ]
      }
    })
    const buttons = wrapper.findAll('.iris-toolbar__btn')
    const inspectBtn = buttons.find((b) => b.text().includes('Inspect Topology'))
    expect(inspectBtn).toBeDefined()
    await inspectBtn!.trigger('click')
    expect(wrapper.emitted('action')?.[0]).toEqual(['inspect'])
  })

  it('dispatches export event when export button is clicked', async () => {
    const wrapper = mount(IrisToolbar, {
      props: { showExport: true }
    })
    const exportBtn = wrapper.find('button[aria-label="Export data"]')
    expect(exportBtn.exists()).toBe(true)
    await exportBtn.trigger('click')
    expect(wrapper.emitted('export')).toBeTruthy()
  })
})
