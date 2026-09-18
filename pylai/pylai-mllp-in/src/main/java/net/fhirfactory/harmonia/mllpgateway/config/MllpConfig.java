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

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;

@ApplicationScoped
public class MllpConfig {

    public static final String DEFAULT_GATEWAY_INSTANCE_ID = "mllp-gateway-default";
    public static final String DEFAULT_EVENT_QUEUE_PREFIX = "task.event.queue";

    private String gatewayInstanceId;
    private String eventQueuePrefix;
    private String host;
    private int port;
    private int mfnPort;
    private int pasAdtPort;
    private int lmsOruPort;
    private int rispacOruPort;
    private int emrOrmPort;
    private boolean autoAck;

    public MllpConfig() {
        this.gatewayInstanceId = getEnvOrProperty("MLLP_GATEWAY_INSTANCE_ID", DEFAULT_GATEWAY_INSTANCE_ID);
        this.eventQueuePrefix = getEnvOrProperty("MLLP_EVENT_QUEUE_PREFIX", DEFAULT_EVENT_QUEUE_PREFIX);
        this.host = getEnvOrProperty("MLLP_HOST", "0.0.0.0");
        this.port = Integer.parseInt(getEnvOrProperty("MLLP_PORT", "2575"));
        this.mfnPort = Integer.parseInt(getEnvOrProperty("MLLP_MFN_PORT", "2576"));
        this.pasAdtPort = Integer.parseInt(getEnvOrProperty("MLLP_PAS_ADT_PORT", "2101"));
        this.lmsOruPort = Integer.parseInt(getEnvOrProperty("MLLP_LMS_ORU_PORT", "2102"));
        this.rispacOruPort = Integer.parseInt(getEnvOrProperty("MLLP_RISPAC_ORU_PORT", "2103"));
        this.emrOrmPort = Integer.parseInt(getEnvOrProperty("MLLP_EMR_ORM_PORT", "2104"));
        this.autoAck = Boolean.parseBoolean(getEnvOrProperty("MLLP_AUTO_ACK", "true"));
    }

    public MllpConfig(String host, int port, boolean autoAck) {
        this(DEFAULT_GATEWAY_INSTANCE_ID, host, port, 2576, autoAck);
    }

    public MllpConfig(String host, int port, int mfnPort, boolean autoAck) {
        this(DEFAULT_GATEWAY_INSTANCE_ID, host, port, mfnPort, autoAck);
    }

    public MllpConfig(String gatewayInstanceId, String host, int port, boolean autoAck) {
        this(gatewayInstanceId, host, port, 2576, autoAck);
    }

    public MllpConfig(String gatewayInstanceId, String host, int port, int mfnPort, boolean autoAck) {
        this.gatewayInstanceId = StringUtils.isNotBlank(gatewayInstanceId) ? gatewayInstanceId.trim() : DEFAULT_GATEWAY_INSTANCE_ID;
        this.eventQueuePrefix = DEFAULT_EVENT_QUEUE_PREFIX;
        this.host = host;
        this.port = port;
        this.mfnPort = mfnPort;
        this.autoAck = autoAck;
    }

    private String getEnvOrProperty(String key, String defaultValue) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp.trim();
        }
        String envVal = System.getenv(key);
        if (envVal != null && !envVal.isBlank()) {
            return envVal.trim();
        }
        return defaultValue;
    }

    public String getGatewayInstanceId() {
        return gatewayInstanceId;
    }

    public void setGatewayInstanceId(String gatewayInstanceId) {
        this.gatewayInstanceId = gatewayInstanceId;
    }

    public String getEventQueuePrefix() {
        return eventQueuePrefix;
    }

    public void setEventQueuePrefix(String eventQueuePrefix) {
        this.eventQueuePrefix = eventQueuePrefix;
    }

    public String getDedicatedEventQueueName() {
        String prefix = StringUtils.isNotBlank(eventQueuePrefix) ? eventQueuePrefix.trim() : DEFAULT_EVENT_QUEUE_PREFIX;
        String gwId = StringUtils.isNotBlank(gatewayInstanceId) ? gatewayInstanceId.trim() : DEFAULT_GATEWAY_INSTANCE_ID;
        return prefix + "." + gwId;
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

    public int getMfnPort() {
        return mfnPort;
    }

    public void setMfnPort(int mfnPort) {
        this.mfnPort = mfnPort;
    }

    public int getPasAdtPort() {
        return pasAdtPort > 0 ? pasAdtPort : port;
    }

    public void setPasAdtPort(int pasAdtPort) {
        this.pasAdtPort = pasAdtPort;
    }

    public int getLmsOruPort() {
        return lmsOruPort;
    }

    public void setLmsOruPort(int lmsOruPort) {
        this.lmsOruPort = lmsOruPort;
    }

    public int getRispacOruPort() {
        return rispacOruPort;
    }

    public void setRispacOruPort(int rispacOruPort) {
        this.rispacOruPort = rispacOruPort;
    }

    public int getEmrOrmPort() {
        return emrOrmPort;
    }

    public void setEmrOrmPort(int emrOrmPort) {
        this.emrOrmPort = emrOrmPort;
    }

    public boolean isAutoAck() {
        return autoAck;
    }

    public void setAutoAck(boolean autoAck) {
        this.autoAck = autoAck;
    }
}
