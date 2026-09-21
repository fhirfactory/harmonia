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
import pkgJson from '../../package.json'

describe('Iris Design System Unidirectional Dependency & Decoupling', () => {
  it('enforces that package.json has zero dependencies on application SPAs', () => {
    const allDeps: Record<string, string> = {
      ...((pkgJson as any).dependencies || {}),
      ...((pkgJson as any).devDependencies || {}),
      ...((pkgJson as any).peerDependencies || {})
    }

    const forbiddenPackages = ['iris-console', 'iris-clinical', 'iris-administration', 'hie-iris-console', '@harmonia/iris-console', '@harmonia/iris-clinical', '@harmonia/iris-administration']
    for (const pkg of forbiddenPackages) {
      expect(allDeps[pkg]).toBeUndefined()
    }
  })

  it('enforces that all exported modules are domain-neutral and decoupled from SPAs', () => {
    // Vite glob import of all frontend source modules (excluding tests)
    const modules = import.meta.glob(['../*.ts', '../**/*.ts', '../**/*.vue', '!../__tests__/**'], {
      query: '?raw',
      import: 'default',
      eager: true
    })

    const fileKeys = Object.keys(modules)
    expect(fileKeys.length).toBeGreaterThan(0)

    const forbiddenImports = ['iris-console', 'iris-clinical', 'iris-administration', '../iris-']
    for (const [filePath, content] of Object.entries(modules)) {
      const code = content as string
      for (const forbidden of forbiddenImports) {
        expect(code, `File ${filePath} contains forbidden import ${forbidden}`).not.toContain(forbidden)
      }
    }
  })
})
