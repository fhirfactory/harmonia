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
import IrisHierarchy from '../components/presentation/IrisHierarchy.vue'
import type { HierarchyNode } from '../types'

const testNodes: HierarchyNode[] = [
  {
    id: 'integration',
    label: 'Integration & Transport',
    subtitle: 'Interface gateways and messaging transport',
    status: 'HEALTHY',
    children: [
      {
        id: 'pylai',
        label: 'Pylai',
        subtitle: 'Interface Gateways',
        status: 'HEALTHY'
      },
      {
        id: 'petasos',
        label: 'Petasos',
        subtitle: 'Messaging & Transport',
        status: 'HEALTHY'
      }
    ]
  },
  {
    id: 'execution',
    label: 'Execution & Processing',
    subtitle: 'Workflow & activity execution engine',
    status: 'HEALTHY',
    children: [
      {
        id: 'energeia',
        label: 'Energeia',
        subtitle: 'Workflow Engine',
        status: 'HEALTHY'
      }
    ]
  }
]

describe('IrisHierarchy.vue', () => {
  it('renders hierarchy tree with parent and child nodes', () => {
    const wrapper = mount(IrisHierarchy, {
      props: {
        nodes: testNodes,
        defaultExpandedAll: true
      }
    })
    expect(wrapper.text()).toContain('Integration & Transport')
    expect(wrapper.text()).toContain('Pylai')
    expect(wrapper.text()).toContain('Petasos')
    expect(wrapper.text()).toContain('Execution & Processing')
    expect(wrapper.text()).toContain('Energeia')
  })

  it('emits select event when a node row is clicked', async () => {
    const wrapper = mount(IrisHierarchy, {
      props: {
        nodes: testNodes,
        defaultExpandedAll: true
      }
    })
    const petasosRow = wrapper.findAll('.iris-hierarchy__node-row').find((r) => r.text().includes('Petasos'))
    expect(petasosRow).toBeDefined()
    await petasosRow!.trigger('click')

    expect(wrapper.emitted('select')).toBeTruthy()
    expect(wrapper.emitted('select')?.[0][0]).toMatchObject({ id: 'petasos' })
  })

  it('filters nodes based on search input query', async () => {
    const wrapper = mount(IrisHierarchy, {
      props: {
        nodes: testNodes,
        searchable: true
      }
    })
    const input = wrapper.find('.iris-hierarchy__search-input')
    await input.setValue('Petasos')

    expect(wrapper.text()).toContain('Petasos')
    expect(wrapper.text()).not.toContain('Energeia')
  })

  it('toggles collapse and expand on toggle button click', async () => {
    const wrapper = mount(IrisHierarchy, {
      props: {
        nodes: testNodes,
        defaultExpandedAll: false
      }
    })
    // Initially children are collapsed
    expect(wrapper.text()).not.toContain('Pylai')

    // Click toggle button for integration node
    const toggleBtn = wrapper.find('.iris-hierarchy__toggle-btn')
    await toggleBtn.trigger('click')

    expect(wrapper.text()).toContain('Pylai')
    expect(wrapper.emitted('toggle')).toBeTruthy()
  })

  it('renders multi-level nested children with proper indentation and selection', async () => {
    const multiLevelNodes: HierarchyNode[] = [
      {
        id: 'execution-area',
        label: 'Execution & Processing',
        count: 1,
        children: [
          {
            id: 'energeia',
            label: 'Energeia',
            subtitle: 'Workflow Engine',
            children: [
              { id: 'ponos', label: 'Ponos', subtitle: 'Task Execution' },
              { id: 'ergon', label: 'Ergon', subtitle: 'Activity Processing' }
            ]
          }
        ]
      }
    ]

    const wrapper = mount(IrisHierarchy, {
      props: {
        nodes: multiLevelNodes,
        selectedId: 'ponos',
        defaultExpandedAll: true
      }
    })

    expect(wrapper.text()).toContain('Execution & Processing')
    expect(wrapper.text()).toContain('Energeia')
    expect(wrapper.text()).toContain('Ponos')
    expect(wrapper.text()).toContain('Ergon')

    // Count badge
    const badge = wrapper.find('.iris-hierarchy__count-badge')
    expect(badge.exists()).toBe(true)
    expect(badge.text()).toBe('1')

    // Selected state on Ponos
    const ponosRow = wrapper.findAll('.iris-hierarchy__node-row').find((r) => r.text().includes('Ponos'))
    expect(ponosRow).toBeDefined()
    expect(ponosRow!.classes()).toContain('iris-hierarchy__node-row--selected')

    const ponosItem = wrapper.findAll('li[role="treeitem"]').find((i) => i.attributes('aria-selected') === 'true')
    expect(ponosItem).toBeDefined()
    expect(ponosItem!.text()).toContain('Ponos')
  })
})
