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

/**
 * Discriminated load state for a single data slice.
 *
 * The purpose is honesty: a page must be able to distinguish "the operations API
 * returned nothing" (`empty`) from "the operations API could not be reached"
 * (`unavailable`). Neither may be presented as a healthy idle platform.
 */
export type LoadState =
  | { kind: 'idle' }
  | { kind: 'loading' }
  | { kind: 'loaded'; at: Date }
  | { kind: 'empty'; at: Date }
  | { kind: 'unavailable'; message: string; at: Date }
  | { kind: 'partial'; message: string; at: Date };

export const IDLE_STATE: LoadState = { kind: 'idle' };

export function loading(): LoadState {
  return { kind: 'loading' };
}

export function loaded(at: Date = new Date()): LoadState {
  return { kind: 'loaded', at };
}

export function empty(at: Date = new Date()): LoadState {
  return { kind: 'empty', at };
}

export function unavailable(message: string, at: Date = new Date()): LoadState {
  return { kind: 'unavailable', message, at };
}

export function partial(message: string, at: Date = new Date()): LoadState {
  return { kind: 'partial', message, at };
}

/**
 * Derives a concise, operator-facing message from a caught error without ever
 * exposing a stack trace or raw exception object to the presentation tier.
 */
export function describeFailure(err: unknown, subject: string): string {
  const raw = (err as any)?.message;
  const detail = typeof raw === 'string' && raw.trim() ? raw.trim().split('\n')[0] : '';
  return detail
    ? `Operations API unavailable while loading ${subject}: ${detail}`
    : `Operations API unavailable while loading ${subject}.`;
}

export function isUnavailable(state: LoadState): boolean {
  return state.kind === 'unavailable';
}

export function isEmpty(state: LoadState): boolean {
  return state.kind === 'empty';
}

export function isPending(state: LoadState): boolean {
  return state.kind === 'idle' || state.kind === 'loading';
}
