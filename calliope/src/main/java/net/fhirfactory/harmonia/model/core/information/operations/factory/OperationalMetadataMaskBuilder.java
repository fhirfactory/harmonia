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

package net.fhirfactory.harmonia.model.core.information.operations.factory;

import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.core.information.operations.OperationalMetadataMask;
import net.fhirfactory.harmonia.model.core.information.operations.OperationalRevision;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;

import java.util.UUID;

/** Assembles a neutral selector for operational metadata observations. */
public final class OperationalMetadataMaskBuilder {

    private UUID id;
    private TopicMask subjectMask;
    private TopicMask contextMask;
    private String operationalRevision;
    private String activeGeneration;
    private String lifecycleStage;
    private AuthoritativeCommitOutcome commitOutcome;

    /** Sets the mask identity. */
    public OperationalMetadataMaskBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    /** Sets the subject Topic mask. */
    public OperationalMetadataMaskBuilder subjectMask(TopicMask subjectMask) {
        this.subjectMask = subjectMask;
        return this;
    }

    /** Sets the operational context Topic mask. */
    public OperationalMetadataMaskBuilder contextMask(TopicMask contextMask) {
        this.contextMask = contextMask;
        return this;
    }

    /** Sets the required Mneme operational revision. */
    public OperationalMetadataMaskBuilder operationalRevision(OperationalRevision revision) {
        this.operationalRevision = revision != null ? revision.value() : null;
        return this;
    }

    /** Sets the required operational revision from its wire string. */
    public OperationalMetadataMaskBuilder operationalRevision(String revision) {
        this.operationalRevision = revision;
        return this;
    }

    /** Sets the active cache generation. */
    public OperationalMetadataMaskBuilder activeGeneration(String activeGeneration) {
        this.activeGeneration = activeGeneration;
        return this;
    }

    /** Sets the persistence-handshake stage. */
    public OperationalMetadataMaskBuilder lifecycleStage(String lifecycleStage) {
        this.lifecycleStage = lifecycleStage;
        return this;
    }

    /** Sets the persistence commit outcome. */
    public OperationalMetadataMaskBuilder commitOutcome(AuthoritativeCommitOutcome commitOutcome) {
        this.commitOutcome = commitOutcome;
        return this;
    }

    /** Builds an immutable operational metadata mask. */
    public OperationalMetadataMask build() {
        return new OperationalMetadataMask(id, subjectMask, contextMask, operationalRevision,
                activeGeneration, lifecycleStage, commitOutcome);
    }
}