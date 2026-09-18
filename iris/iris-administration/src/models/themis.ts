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

export type ThemisAuthority =
  | 'provider.read'
  | 'provider.search'
  | 'provider.resource.create'
  | 'provider.resource.update'
  | 'provider.resource.delete'
  | 'provider.change.process'
  | 'provider.admin';

export interface ThemisPrincipal {
  id: string;
  username: string;
  displayName: string;
  email: string;
  practitionerId?: string;
  organizationId?: string;
  authorities: ThemisAuthority[];
  token?: string;
}

export interface SecurityContextState {
  principal: ThemisPrincipal | null;
  isAuthenticated: boolean;
  correlationId: string;
  sourceSystem: string;
}

export interface PersonaProfile {
  id: string;
  name: string;
  description: string;
  principal: ThemisPrincipal;
}
