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

import type { App } from 'vue'
import PrimeVue from 'primevue/config'
import { IrisPreset } from './irisPreset'
import './tokens.css'

export { IrisPreset }

export interface IrisThemeOptions {
  ripple?: boolean
  inputVariant?: 'outlined' | 'filled'
  [key: string]: any
}

export function installIrisTheme(app: App, options?: IrisThemeOptions) {
  app.use(PrimeVue, {
    ripple: options?.ripple ?? false,
    inputVariant: options?.inputVariant ?? 'outlined',
    theme: {
      preset: IrisPreset,
      options: {
        darkModeSelector: false,
        cssLayer: false
      }
    },
    ...options
  })
}
