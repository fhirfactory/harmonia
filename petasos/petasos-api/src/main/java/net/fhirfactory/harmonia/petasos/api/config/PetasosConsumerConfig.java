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

package net.fhirfactory.harmonia.petasos.api.config;

import java.io.Serializable;

/**
 * Configuration specific to {@link net.fhirfactory.harmonia.petasos.api.consumer.PetasosConsumer}.
 */
public final class PetasosConsumerConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum AcknowledgeMode {
        CLIENT_ACKNOWLEDGE,
        AUTO_ACKNOWLEDGE
    }

    private final int concurrency;
    private final int prefetchSize;
    private final AcknowledgeMode acknowledgeMode;
    private final String messageSelector;
    private final boolean autoAcknowledgeOnSuccess;
    private final boolean autoRejectOnError;

    public PetasosConsumerConfig(
            int concurrency,
            int prefetchSize,
            AcknowledgeMode acknowledgeMode,
            String messageSelector,
            boolean autoAcknowledgeOnSuccess,
            boolean autoRejectOnError) {
        this.concurrency = concurrency > 0 ? concurrency : 1;
        this.prefetchSize = prefetchSize >= 0 ? prefetchSize : 10;
        this.acknowledgeMode = acknowledgeMode != null ? acknowledgeMode : AcknowledgeMode.CLIENT_ACKNOWLEDGE;
        this.messageSelector = messageSelector;
        this.autoAcknowledgeOnSuccess = autoAcknowledgeOnSuccess;
        this.autoRejectOnError = autoRejectOnError;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static PetasosConsumerConfig defaultConfiguration() {
        return builder().build();
    }

    public int getConcurrency() {
        return concurrency;
    }

    public int getPrefetchSize() {
        return prefetchSize;
    }

    public AcknowledgeMode getAcknowledgeMode() {
        return acknowledgeMode;
    }

    public String getMessageSelector() {
        return messageSelector;
    }

    public boolean isAutoAcknowledgeOnSuccess() {
        return autoAcknowledgeOnSuccess;
    }

    public boolean isAutoRejectOnError() {
        return autoRejectOnError;
    }

    public static final class Builder {
        private int concurrency = 1;
        private int prefetchSize = 10;
        private AcknowledgeMode acknowledgeMode = AcknowledgeMode.CLIENT_ACKNOWLEDGE;
        private String messageSelector;
        private boolean autoAcknowledgeOnSuccess = true;
        private boolean autoRejectOnError = true;

        public Builder concurrency(int concurrency) {
            this.concurrency = concurrency;
            return this;
        }

        public Builder prefetchSize(int prefetchSize) {
            this.prefetchSize = prefetchSize;
            return this;
        }

        public Builder acknowledgeMode(AcknowledgeMode acknowledgeMode) {
            this.acknowledgeMode = acknowledgeMode;
            return this;
        }

        public Builder messageSelector(String messageSelector) {
            this.messageSelector = messageSelector;
            return this;
        }

        public Builder autoAcknowledgeOnSuccess(boolean autoAcknowledgeOnSuccess) {
            this.autoAcknowledgeOnSuccess = autoAcknowledgeOnSuccess;
            return this;
        }

        public Builder autoRejectOnError(boolean autoRejectOnError) {
            this.autoRejectOnError = autoRejectOnError;
            return this;
        }

        public PetasosConsumerConfig build() {
            return new PetasosConsumerConfig(
                    concurrency,
                    prefetchSize,
                    acknowledgeMode,
                    messageSelector,
                    autoAcknowledgeOnSuccess,
                    autoRejectOnError
            );
        }
    }
}
