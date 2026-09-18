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

import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios';
import { safeLogger } from '../utils/safeLogger';

export const apiClient: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: {
    'Content-Type': 'application/fhir+json',
    'Accept': 'application/fhir+json'
  },
  timeout: 30000
});

// Request interceptor for Correlation and Security headers
apiClient.interceptors.request.use(
  (config) => {
    // Dynamically retrieve security headers
    const correlationId = `CORR-${Date.now()}-${Math.random().toString(36).substring(2, 7)}`;
    config.headers['X-Correlation-Id'] = config.headers['X-Correlation-Id'] || correlationId;
    config.headers['X-Source-System'] = 'iris-administration';
    config.headers['X-Requester'] = config.headers['X-Requester'] || 'iris-admin-user';

    safeLogger.info(`[HTTP ${config.method?.toUpperCase()}] ${config.url}`, {
      correlationId: config.headers['X-Correlation-Id'],
      sourceSystem: config.headers['X-Source-System']
    });

    return config;
  },
  (error) => {
    safeLogger.error('HTTP Request Interceptor Error', error);
    return Promise.reject(error);
  }
);

// Response interceptor for Logging and Error formatting
apiClient.interceptors.response.use(
  (response: AxiosResponse) => {
    safeLogger.info(`[HTTP Response ${response.status}] ${response.config.url}`);
    return response;
  },
  (error) => {
    const status = error.response?.status;
    const url = error.config?.url;
    safeLogger.warn(`[HTTP Error ${status}] ${url}`, {
      status,
      correlationId: error.config?.headers?.['X-Correlation-Id']
    });
    return Promise.reject(error);
  }
);
