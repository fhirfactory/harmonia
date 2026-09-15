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

import axios, { AxiosInstance } from 'axios';
import type { BaseResource, Bundle } from '../models/fhir';

const client: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api/fhir',
  headers: {
    'Content-Type': 'application/fhir+json',
    'Accept': 'application/fhir+json, application/json'
  },
  timeout: 10000
});

export const fhirApi = {
  async search<T extends BaseResource>(resourceType: string, params?: Record<string, string>): Promise<T[]> {
    try {
      const response = await client.get<Bundle<T>>(`/${resourceType}`, { params });
      if (response.data && response.data.entry) {
        return response.data.entry.map(e => e.resource);
      }
      return [];
    } catch (error) {
      console.error(`Error fetching ${resourceType}:`, error);
      throw error;
    }
  },

  async get<T extends BaseResource>(resourceType: string, id: string): Promise<T> {
    const response = await client.get<T>(`/${resourceType}/${id}`);
    return response.data;
  },

  async create<T extends BaseResource>(resourceType: string, resource: Partial<T>): Promise<T> {
    const response = await client.post<T>(`/${resourceType}`, resource);
    return response.data;
  },

  async update<T extends BaseResource>(resourceType: string, id: string, resource: Partial<T>): Promise<T> {
    const response = await client.put<T>(`/${resourceType}/${id}`, resource);
    return response.data;
  },

  async delete(resourceType: string, id: string): Promise<void> {
    await client.delete(`/${resourceType}/${id}`);
  }
};
