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
import PrimeVue from 'primevue/config'
import IrisSubsystemIdentity from '../components/presentation/IrisSubsystemIdentity.vue'
import IrisSection from '../components/presentation/IrisSection.vue'
import IrisDataTable from '../components/presentation/IrisDataTable.vue'
import IrisEmptyState from '../components/presentation/IrisEmptyState.vue'
import IrisLoadingState from '../components/presentation/IrisLoadingState.vue'
import IrisErrorState from '../components/presentation/IrisErrorState.vue'

describe('Iris Presentation Components', () => {
  it('renders IrisSubsystemIdentity with Greek name, English description and version', () => {
    const wrapper = mount(IrisSubsystemIdentity, {
      props: {
        name: 'Petasos',
        description: 'Messaging & Transport',
        version: '1.0.0',
        status: 'HEALTHY'
      }
    })
    expect(wrapper.find('.iris-subsystem-identity__name').text()).toBe('Petasos')
    expect(wrapper.find('.iris-subsystem-identity__description').text()).toBe('Messaging & Transport')
    expect(wrapper.find('.iris-subsystem-identity__version').text()).toBe('v1.0.0')
    expect(wrapper.text()).toContain('Healthy')
  })

  it('renders IrisSection and toggles collapsible content', async () => {
    const wrapper = mount(IrisSection, {
      props: {
        title: 'Runtime Instances',
        description: 'Active pods and broker nodes',
        collapsible: true
      },
      slots: {
        default: '<div class="section-body">Instance rows</div>'
      }
    })
    expect(wrapper.text()).toContain('Runtime Instances')
    expect(wrapper.text()).toContain('Active pods and broker nodes')
    expect(wrapper.find('.section-body').exists()).toBe(true)

    const header = wrapper.find('.iris-section__header')
    await header.trigger('click')
    expect(wrapper.find('.section-body').exists()).toBe(false)
    expect(wrapper.emitted('toggle')?.[0]).toEqual([true])
  })

  it('renders IrisDataTable with columns and rows', () => {
    const wrapper = mount(IrisDataTable, {
      global: {
        plugins: [PrimeVue]
      },
      props: {
        value: [
          { id: '1', name: 'MLLP Inbound', port: 2575, status: 'RUNNING' },
          { id: '2', name: 'FHIR Gateway', port: 8089, status: 'RUNNING' }
        ],
        columns: [
          { field: 'name', header: 'Gateway Name' },
          { field: 'port', header: 'Port' }
        ]
      }
    })
    expect(wrapper.text()).toContain('Gateway Name')
    expect(wrapper.text()).toContain('Port')
    expect(wrapper.text()).toContain('MLLP Inbound')
    expect(wrapper.text()).toContain('2575')
  })

  it('renders IrisEmptyState with title and description', () => {
    const wrapper = mount(IrisEmptyState, {
      props: {
        title: 'No Active Queues',
        description: 'There are no message queues configured.'
      }
    })
    expect(wrapper.text()).toContain('No Active Queues')
    expect(wrapper.text()).toContain('There are no message queues configured.')
  })

  it('renders IrisLoadingState with accessible attributes', () => {
    const wrapper = mount(IrisLoadingState, {
      props: {
        message: 'Polling telemetry from BEFE...'
      }
    })
    expect(wrapper.attributes('role')).toBe('status')
    expect(wrapper.attributes('aria-busy')).toBe('true')
    expect(wrapper.text()).toContain('Polling telemetry from BEFE...')
  })

  it('renders IrisErrorState and emits retry on button click', async () => {
    const wrapper = mount(IrisErrorState, {
      props: {
        title: 'Connection Refused',
        message: 'Unable to reach WildFly Operations endpoint on port 8090',
        retryable: true,
        retryLabel: 'Reconnect'
      }
    })
    expect(wrapper.attributes('role')).toBe('alert')
    expect(wrapper.text()).toContain('Connection Refused')
    expect(wrapper.text()).toContain('Unable to reach WildFly Operations endpoint on port 8090')

    const retryBtn = wrapper.find('.iris-error-state__retry-btn')
    expect(retryBtn.exists()).toBe(true)
    await retryBtn.trigger('click')
    expect(wrapper.emitted('retry')).toBeTruthy()
  })

  it('renders an optional IrisErrorState detail line without any stack trace', () => {
    const wrapper = mount(IrisErrorState, {
      props: {
        title: 'Operations API unavailable',
        message: 'The console could not reach the operations API.',
        detail: 'GET /operations/summary - no response'
      }
    })
    expect(wrapper.find('.iris-error-state__detail').text()).toBe(
      'GET /operations/summary - no response'
    )
    expect(wrapper.text()).not.toContain('at ')
  })

  it('applies column alignment when declared', () => {
    const wrapper = mount(IrisDataTable, {
      global: { plugins: [PrimeVue] },
      props: {
        value: [{ id: '1', depth: 4 }],
        columns: [{ field: 'depth', header: 'Depth', align: 'right' as const }]
      }
    })
    expect(wrapper.find('th').attributes('style')).toContain('text-align: right')
  })
})
