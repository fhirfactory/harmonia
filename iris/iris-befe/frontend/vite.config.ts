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

/// <reference types="vitest" />
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  build: {
    lib: {
      entry: path.resolve(__dirname, 'src/index.ts'),
      name: 'HarmoniaIrisBefe',
      fileName: (format) => `index.${format === 'es' ? 'js' : 'cjs'}`
    },
    rollupOptions: {
      external: [
        'vue',
        'vue-router',
        'primevue',
        'primevue/config',
        /^primevue\/.*/,
        '@primevue/themes',
        /^@primevue\/themes\/.*/,
        'lucide-vue-next'
      ],
      output: {
        exports: 'named',
        globals: {
          vue: 'Vue',
          'vue-router': 'VueRouter',
          primevue: 'PrimeVue',
          'primevue/config': 'PrimeVue',
          '@primevue/themes': 'PrimeVueThemes',
          '@primevue/themes/aura': 'Aura',
          'lucide-vue-next': 'LucideVueNext',
          'primevue/datatable': 'DataTable',
          'primevue/column': 'Column'
        }
      }
    }
  },
  test: {
    globals: true,
    environment: 'jsdom'
  }
})
