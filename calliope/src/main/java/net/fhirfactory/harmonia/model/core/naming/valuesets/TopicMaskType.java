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
 * Defines the matching basis for a {@link net.fhirfactory.harmonia.model.core.naming.TopicMask}.
 * <p>
 * In accordance with the accepted Architecture Continuum:
 * <ul>
 *   <li><b>TopicNameBased:</b> Matches explicitly sequenced regex patterns against corresponding individual unqualified Topic names.</li>
 *   <li><b>TopicCanonicalNameBased:</b> Matches explicitly sequenced pattern entries against corresponding entry qualifier and name independently.</li>
 * </ul>
 * A single {@link net.fhirfactory.harmonia.model.core.naming.TopicMask} has exactly one basis and does not combine both bases.
 */
public enum TopicMaskType {

    /**
     * SequenceNumber &rarr; NameRegex matching against corresponding individual unqualified Topic names.
     */
    TopicNameBased,

    /**
     * SequenceNumber &rarr; EntryPattern(qualifierRegex, nameRegex) matching against corresponding qualifier and name independently.
     */
    TopicCanonicalNameBased
}
