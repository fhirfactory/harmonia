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

package net.fhirfactory.harmonia.mllpgateway.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Configuration definition for an outbound MLLP target endpoint/destination.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MllpDestinationConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_OUTBOUND_QUEUE_PREFIX = "petasos.queue.mllp.outbound";
    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 5000;
    public static final int DEFAULT_READ_TIMEOUT_MS = 10000;
    public static final String DEFAULT_CHARSET = "UTF-8";

    private String destinationId;
    private String name;
    private String host;
    private int port;
    private int connectTimeoutMs = DEFAULT_CONNECT_TIMEOUT_MS;
    private int readTimeoutMs = DEFAULT_READ_TIMEOUT_MS;
    private String charset = DEFAULT_CHARSET;
    private boolean keepAlive = true;
    private boolean autoAck = true;
    private String queueName;
    private String queuePrefix = DEFAULT_OUTBOUND_QUEUE_PREFIX;
    private boolean sslEnabled = false;
    private boolean enabled = true;
    private String facility;
    private String description;

    public MllpDestinationConfig() {
    }

    public MllpDestinationConfig(String destinationId, String host, int port) {
        this.destinationId = destinationId;
        this.name = destinationId;
        this.host = host;
        this.port = port;
    }

    public MllpDestinationConfig(String destinationId, String name, String host, int port) {
        this.destinationId = destinationId;
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public MllpDestinationConfig(String destinationId, String name, String host, int port,
                                String facility, String queueName) {
        this.destinationId = destinationId;
        this.name = name;
        this.host = host;
        this.port = port;
        this.facility = facility;
        this.queueName = queueName;
    }

    /**
     * Resolves the effective Petasos Artemis queue name assigned to this destination endpoint.
     */
    public String getEffectiveQueueName() {
        if (StringUtils.isNotBlank(queueName)) {
            return queueName.trim();
        }
        String prefix = StringUtils.isNotBlank(queuePrefix) ? queuePrefix.trim() : DEFAULT_OUTBOUND_QUEUE_PREFIX;
        String id = StringUtils.isNotBlank(destinationId) ? destinationId.trim().toLowerCase() : "default";
        return prefix + "." + id;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isAutoAck() {
        return autoAck;
    }

    public void setAutoAck(boolean autoAck) {
        this.autoAck = autoAck;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueuePrefix() {
        return queuePrefix;
    }

    public void setQueuePrefix(String queuePrefix) {
        this.queuePrefix = queuePrefix;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MllpDestinationConfig that = (MllpDestinationConfig) o;
        return port == that.port &&
                connectTimeoutMs == that.connectTimeoutMs &&
                readTimeoutMs == that.readTimeoutMs &&
                keepAlive == that.keepAlive &&
                autoAck == that.autoAck &&
                sslEnabled == that.sslEnabled &&
                enabled == that.enabled &&
                Objects.equals(destinationId, that.destinationId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(host, that.host) &&
                Objects.equals(charset, that.charset) &&
                Objects.equals(queueName, that.queueName) &&
                Objects.equals(queuePrefix, that.queuePrefix) &&
                Objects.equals(facility, that.facility) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationId, name, host, port, connectTimeoutMs, readTimeoutMs,
                charset, keepAlive, autoAck, queueName, queuePrefix, sslEnabled, enabled, facility, description);
    }

    @Override
    public String toString() {
        return "MllpDestinationConfig{" +
                "destinationId='" + destinationId + '\'' +
                ", name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", connectTimeoutMs=" + connectTimeoutMs +
                ", readTimeoutMs=" + readTimeoutMs +
                ", charset='" + charset + '\'' +
                ", keepAlive=" + keepAlive +
                ", autoAck=" + autoAck +
                ", queueName='" + queueName + '\'' +
                ", queuePrefix='" + queuePrefix + '\'' +
                ", sslEnabled=" + sslEnabled +
                ", enabled=" + enabled +
                ", facility='" + facility + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
