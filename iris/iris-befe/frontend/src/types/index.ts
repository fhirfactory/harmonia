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

export type StatusState =
  | 'HEALTHY'
  | 'DEGRADED'
  | 'UNAVAILABLE'
  | 'UNKNOWN'
  | 'healthy'
  | 'degraded'
  | 'unavailable'
  | 'unknown';

export interface NavItem {
  id: string;
  label: string;
  subLabel?: string;
  to?: string | Record<string, any>;
  href?: string;
  badge?: string | number | null;
  badgeSeverity?: 'info' | 'warn' | 'danger' | 'success';
  icon?: any;
  children?: NavItem[];
}

export type NavPerspective = NavItem;

export interface BreadcrumbItem {
  label: string;
  to?: string | Record<string, any>;
  icon?: any;
}

export interface SubsystemNode {
  id: string;
  label: string;
  subtitle?: string;
  status?: StatusState;
  stale?: boolean;
  children?: SubsystemNode[];
  icon?: any;
  data?: any;
  count?: number;
}

export type HierarchyNode = SubsystemNode;

export interface ToolbarAction {
  id: string;
  label: string;
  icon?: any;
  disabled?: boolean;
  loading?: boolean;
  primary?: boolean;
  severity?: 'primary' | 'secondary' | 'success' | 'info' | 'warn' | 'danger' | 'contrast';
  onClick?: () => void;
}

export interface UserProfile {
  username?: string;
  name?: string;
  role?: string;
  avatarUrl?: string;
}

export interface DataTableColumn {
  field: string;
  header: string;
  sortable?: boolean;
  width?: string;
  align?: 'left' | 'center' | 'right';
}
