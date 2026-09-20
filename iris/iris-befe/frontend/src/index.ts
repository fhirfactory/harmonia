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

import type { App, Plugin } from 'vue'

// Theme
import { installIrisTheme, IrisPreset, type IrisThemeOptions } from './theme'
import './theme/tokens.css'

// Shell components
import IrisApplicationShell from './components/shell/IrisApplicationShell.vue'
import IrisHeader from './components/shell/IrisHeader.vue'
import IrisEnvironmentBar from './components/shell/IrisEnvironmentBar.vue'
import IrisPrimaryNavigation from './components/shell/IrisPrimaryNavigation.vue'
import IrisUserMenu from './components/shell/IrisUserMenu.vue'
import IrisBreadcrumbs from './components/shell/IrisBreadcrumbs.vue'
import IrisPage from './components/shell/IrisPage.vue'
import IrisPageHeader from './components/shell/IrisPageHeader.vue'
import IrisToolbar from './components/shell/IrisToolbar.vue'

// Presentation components
import IrisStatus from './components/presentation/IrisStatus.vue'
import IrisSubsystemIdentity from './components/presentation/IrisSubsystemIdentity.vue'
import IrisSection from './components/presentation/IrisSection.vue'
import IrisHierarchy from './components/presentation/IrisHierarchy.vue'
import IrisDataTable from './components/presentation/IrisDataTable.vue'
import IrisEmptyState from './components/presentation/IrisEmptyState.vue'
import IrisLoadingState from './components/presentation/IrisLoadingState.vue'
import IrisErrorState from './components/presentation/IrisErrorState.vue'

// Types
export * from './types'

// Theme exports
export { installIrisTheme, IrisPreset, type IrisThemeOptions }

// Component exports
export {
  IrisApplicationShell,
  IrisHeader,
  IrisEnvironmentBar,
  IrisPrimaryNavigation,
  IrisUserMenu,
  IrisBreadcrumbs,
  IrisPage,
  IrisPageHeader,
  IrisToolbar,
  IrisStatus,
  IrisSubsystemIdentity,
  IrisSection,
  IrisHierarchy,
  IrisDataTable,
  IrisEmptyState,
  IrisLoadingState,
  IrisErrorState
}

// Plugin definition
export interface IrisPluginOptions extends IrisThemeOptions {
  registerComponents?: boolean
}

export function createIris(options?: IrisPluginOptions): Plugin {
  return {
    install(app: App) {
      installIrisTheme(app, options)

      if (options?.registerComponents !== false) {
        app.component('IrisApplicationShell', IrisApplicationShell)
        app.component('IrisHeader', IrisHeader)
        app.component('IrisEnvironmentBar', IrisEnvironmentBar)
        app.component('IrisPrimaryNavigation', IrisPrimaryNavigation)
        app.component('IrisUserMenu', IrisUserMenu)
        app.component('IrisBreadcrumbs', IrisBreadcrumbs)
        app.component('IrisPage', IrisPage)
        app.component('IrisPageHeader', IrisPageHeader)
        app.component('IrisToolbar', IrisToolbar)
        app.component('IrisStatus', IrisStatus)
        app.component('IrisSubsystemIdentity', IrisSubsystemIdentity)
        app.component('IrisSection', IrisSection)
        app.component('IrisHierarchy', IrisHierarchy)
        app.component('IrisDataTable', IrisDataTable)
        app.component('IrisEmptyState', IrisEmptyState)
        app.component('IrisLoadingState', IrisLoadingState)
        app.component('IrisErrorState', IrisErrorState)
      }
    }
  }
}

export default createIris
