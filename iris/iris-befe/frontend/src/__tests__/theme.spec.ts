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
import { createApp } from 'vue'
import { IrisPreset, installIrisTheme } from '../theme'
import { createIris } from '../index'

describe('Iris Theme & Tokens', () => {
  it('defines IrisPreset extending Aura theme with light semantic palette', () => {
    expect(IrisPreset).toBeDefined()
    const preset = IrisPreset as any
    expect(preset.semantic).toBeDefined()
    expect(preset.semantic.primary).toBeDefined()
    expect(preset.semantic.colorScheme?.light?.surface).toBeDefined()
  })

  it('installs Iris theme plugin into Vue app', () => {
    const app = createApp({ template: '<div></div>' })
    installIrisTheme(app)
    expect(app.config.globalProperties.$primevue).toBeDefined()
  })

  it('createIris registers all Iris design system components globally when enabled', () => {
    const app = createApp({ template: '<div></div>' })
    const iris = createIris({ registerComponents: true })
    app.use(iris)

    expect(app.component('IrisApplicationShell')).toBeDefined()
    expect(app.component('IrisHeader')).toBeDefined()
    expect(app.component('IrisEnvironmentBar')).toBeDefined()
    expect(app.component('IrisPrimaryNavigation')).toBeDefined()
    expect(app.component('IrisUserMenu')).toBeDefined()
    expect(app.component('IrisBreadcrumbs')).toBeDefined()
    expect(app.component('IrisPage')).toBeDefined()
    expect(app.component('IrisPageHeader')).toBeDefined()
    expect(app.component('IrisToolbar')).toBeDefined()
    expect(app.component('IrisStatus')).toBeDefined()
    expect(app.component('IrisSubsystemIdentity')).toBeDefined()
    expect(app.component('IrisSection')).toBeDefined()
    expect(app.component('IrisHierarchy')).toBeDefined()
    expect(app.component('IrisDataTable')).toBeDefined()
    expect(app.component('IrisEmptyState')).toBeDefined()
    expect(app.component('IrisLoadingState')).toBeDefined()
    expect(app.component('IrisErrorState')).toBeDefined()
  })
})
