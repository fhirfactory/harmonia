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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.core.naming.valuesets;

/**
 * Defines the containment depth matching semantics for a {@link net.fhirfactory.harmonia.model.core.naming.TopicMask}.
 * <p>
 * In accordance with the accepted Architecture Continuum:
 * <ul>
 *   <li><b>Exact:</b> Every specified level matches and candidate depth is exactly the mask depth.</li>
 *   <li><b>Inclusive:</b> Every specified level matches; further descendant levels are included.</li>
 * </ul>
 */
public enum MaskType {

    /**
     * Every specified level matches and candidate Topic depth is exactly the mask depth.
     */
    Exact,

    /**
     * Every specified level matches; further descendant levels in the candidate Topic are included.
     */
    Inclusive
}
