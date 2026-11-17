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

package net.fhirfactory.harmonia.model.core.information.entity.factory;

import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.LifecycleContext;
import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/** Assembles durable retention/disposition facts and persistence handshake observations. */
public final class LifecycleContextBuilder {

    private Topic dispositionStatus;
    private Topic retentionPolicy;
    private String lifecycleStage;
    private Instant persistenceTimestamp;
    private AuthoritativeCommitOutcome commitOutcome;
    private boolean retained;
    private boolean archived;
    private final Map<String, String> coordinationAttributes = new LinkedHashMap<>();

    /** Sets the governed disposition Topic. */
    public LifecycleContextBuilder dispositionStatus(Topic status) {
        dispositionStatus = status;
        return this;
    }

    /** Sets the applicable retention-policy Topic. */
    public LifecycleContextBuilder retentionPolicy(Topic policy) {
        retentionPolicy = policy;
        return this;
    }

    /** Sets the operational persistence-handshake stage. */
    public LifecycleContextBuilder lifecycleStage(String stage) {
        lifecycleStage = stage;
        return this;
    }

    /** Sets the handshake timestamp. */
    public LifecycleContextBuilder persistenceTimestamp(Instant timestamp) {
        persistenceTimestamp = timestamp;
        return this;
    }

    /** Sets the persistence commit outcome. */
    public LifecycleContextBuilder commitOutcome(AuthoritativeCommitOutcome outcome) {
        commitOutcome = outcome;
        return this;
    }

    /** Sets whether the information remains retained. */
    public LifecycleContextBuilder retained(boolean retained) {
        this.retained = retained;
        return this;
    }

    /** Sets whether the information is archived. */
    public LifecycleContextBuilder archived(boolean archived) {
        this.archived = archived;
        return this;
    }

    /** Replaces coordination attributes. */
    public LifecycleContextBuilder coordinationAttributes(Map<String, String> attributes) {
        coordinationAttributes.clear();
        if (attributes != null) {
            coordinationAttributes.putAll(attributes);
        }
        return this;
    }

    /** Adds or removes one coordination attribute. */
    public LifecycleContextBuilder coordinationAttribute(String key, String value) {
        if (key != null) {
            if (value == null) {
                coordinationAttributes.remove(key);
            } else {
                coordinationAttributes.put(key, value);
            }
        }
        return this;
    }

    /** Builds an immutable lifecycle context. */
    public LifecycleContext build() {
        return new LifecycleContext(dispositionStatus, retentionPolicy, lifecycleStage, persistenceTimestamp,
                commitOutcome, retained, archived, coordinationAttributes);
    }
}