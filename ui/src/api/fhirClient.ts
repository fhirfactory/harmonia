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
