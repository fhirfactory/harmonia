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
import IrisStatus from '../components/presentation/IrisStatus.vue'

describe('IrisStatus.vue', () => {
  it('renders HEALTHY status with accessible attributes and symbol', () => {
    const wrapper = mount(IrisStatus, {
      props: { status: 'HEALTHY' }
    })
    expect(wrapper.text()).toContain('Healthy')
    expect(wrapper.text()).toContain('●')
    expect(wrapper.attributes('role')).toBe('status')
    expect(wrapper.attributes('aria-label')).toBe('Healthy status')
    expect(wrapper.classes()).toContain('iris-status--healthy')
  })

  it('renders DEGRADED status correctly with warning symbol', () => {
    const wrapper = mount(IrisStatus, {
      props: { status: 'DEGRADED' }
    })
    expect(wrapper.text()).toContain('Degraded')
    expect(wrapper.text()).toContain('▲')
    expect(wrapper.attributes('aria-label')).toBe('Degraded operational warning')
    expect(wrapper.classes()).toContain('iris-status--degraded')
  })

  it('renders UNAVAILABLE status correctly with failure symbol', () => {
    const wrapper = mount(IrisStatus, {
      props: { status: 'UNAVAILABLE' }
    })
    expect(wrapper.text()).toContain('Unavailable')
    expect(wrapper.text()).toContain('✖')
    expect(wrapper.attributes('aria-label')).toBe('Unavailable failure state')
    expect(wrapper.classes()).toContain('iris-status--unavailable')
  })

  it('renders UNKNOWN status for null, empty or unrecognized values', () => {
    const wrapper = mount(IrisStatus, {
      props: { status: null }
    })
    expect(wrapper.text()).toContain('Unknown')
    expect(wrapper.text()).toContain('?')
    expect(wrapper.attributes('aria-label')).toBe('Unknown operational state')
    expect(wrapper.classes()).toContain('iris-status--unknown')
  })

  it('renders stale telemetry indicator when stale prop is true', () => {
    const wrapper = mount(IrisStatus, {
      props: { status: 'HEALTHY', stale: true }
    })
    expect(wrapper.text()).toContain('Stale Telemetry')
    expect(wrapper.attributes('aria-label')).toBe('Stale cached telemetry')
    expect(wrapper.classes()).toContain('iris-status--stale')
  })

  it('handles size prop variants (sm, md, lg)', () => {
    const sm = mount(IrisStatus, { props: { status: 'HEALTHY', size: 'sm' } })
    expect(sm.classes()).toContain('iris-status--sm')

    const md = mount(IrisStatus, { props: { status: 'HEALTHY', size: 'md' } })
    expect(md.classes()).toContain('iris-status--md')

    const lg = mount(IrisStatus, { props: { status: 'HEALTHY', size: 'lg' } })
    expect(lg.classes()).toContain('iris-status--lg')
  })

  it('supports uppercase formatting if requested', () => {
    const wrapper = mount(IrisStatus, {
      props: { status: 'HEALTHY', labelFormat: 'upper' }
    })
    expect(wrapper.text()).toContain('HEALTHY')
  })

  it('renders animated pulse ring when showPulse is true', () => {
    const wrapper = mount(IrisStatus, {
      props: { status: 'HEALTHY', showPulse: true }
    })
    expect(wrapper.find('.iris-status-pulse').exists()).toBe(true)
  })
})
