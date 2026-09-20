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

package net.fhirfactory.harmonia.model.petasos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Encapsulates the configuration and metadata for an Apache ActiveMQ Artemis message queue
 * stored in the HIE operations data store and Infinispan cache grid.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PetasosQueueDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("queueId")
    private String queueId;

    @JsonProperty("queueName")
    private String queueName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("routingType")
    private String routingType = "ANYCAST";

    @JsonProperty("durable")
    private boolean durable = false;

    @JsonProperty("maxConsumers")
    private Integer maxConsumers = -1;

    @JsonProperty("filter")
    private String filter;

    @JsonProperty("description")
    private String description;

    @JsonProperty("gatewayInstanceId")
    private String gatewayInstanceId;

    @JsonProperty("enabled")
    private boolean enabled = true;

    @JsonProperty("depth")
    private Long depth;

    @JsonProperty("consumerCount")
    private Integer consumerCount;

    @JsonProperty("producerCount")
    private Integer producerCount;

    @JsonProperty("enqueueRate")
    private Double enqueueRate;

    @JsonProperty("dequeueRate")
    private Double dequeueRate;

    @JsonProperty("dlqDepth")
    private Long dlqDepth;

    public PetasosQueueDefinition() {
    }

    public PetasosQueueDefinition(String queueName) {
        this.queueId = queueName;
        this.queueName = queueName;
        this.address = queueName;
        this.routingType = "ANYCAST";
        this.durable = false;
        this.enabled = true;
    }

    public PetasosQueueDefinition(String queueName, String routingType, boolean durable, String description) {
        this.queueId = queueName;
        this.queueName = queueName;
        this.address = queueName;
        this.routingType = routingType != null ? routingType : "ANYCAST";
        this.durable = durable;
        this.description = description;
        this.enabled = true;
    }

    public PetasosQueueDefinition(String queueId, String queueName, String address, String routingType, boolean durable, String description, String gatewayInstanceId) {
        this.queueId = queueId != null ? queueId : queueName;
        this.queueName = queueName;
        this.address = address != null ? address : queueName;
        this.routingType = routingType != null ? routingType : "ANYCAST";
        this.durable = durable;
        this.description = description;
        this.gatewayInstanceId = gatewayInstanceId;
        this.enabled = true;
    }

    public String getQueueId() {
        return queueId != null ? queueId : queueName;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
        if (this.queueName == null) {
            this.queueName = queueId;
        }
    }

    public String getQueueName() {
        return queueName != null ? queueName : queueId;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
        if (this.queueId == null) {
            this.queueId = queueName;
        }
        if (this.address == null) {
            this.address = queueName;
        }
    }

    public String getAddress() {
        return address != null ? address : getQueueName();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoutingType() {
        return routingType != null ? routingType : "ANYCAST";
    }

    public void setRoutingType(String routingType) {
        this.routingType = routingType;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public Integer getMaxConsumers() {
        return maxConsumers != null ? maxConsumers : -1;
    }

    public void setMaxConsumers(Integer maxConsumers) {
        this.maxConsumers = maxConsumers;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGatewayInstanceId() {
        return gatewayInstanceId;
    }

    public void setGatewayInstanceId(String gatewayInstanceId) {
        this.gatewayInstanceId = gatewayInstanceId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getDepth() {
        return depth;
    }

    public void setDepth(Long depth) {
        this.depth = depth;
    }

    public Integer getConsumerCount() {
        return consumerCount;
    }

    public void setConsumerCount(Integer consumerCount) {
        this.consumerCount = consumerCount;
    }

    public Integer getProducerCount() {
        return producerCount;
    }

    public void setProducerCount(Integer producerCount) {
        this.producerCount = producerCount;
    }

    public Double getEnqueueRate() {
        return enqueueRate;
    }

    public void setEnqueueRate(Double enqueueRate) {
        this.enqueueRate = enqueueRate;
    }

    public Double getDequeueRate() {
        return dequeueRate;
    }

    public void setDequeueRate(Double dequeueRate) {
        this.dequeueRate = dequeueRate;
    }

    public Long getDlqDepth() {
        return dlqDepth;
    }

    public void setDlqDepth(Long dlqDepth) {
        this.dlqDepth = dlqDepth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetasosQueueDefinition that = (PetasosQueueDefinition) o;
        return Objects.equals(getQueueId(), that.getQueueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQueueId());
    }

    @Override
    public String toString() {
        return "MessageQueueDefinition{" +
                "queueId='" + getQueueId() + '\'' +
                ", queueName='" + getQueueName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", routingType='" + getRoutingType() + '\'' +
                ", durable=" + durable +
                ", gatewayInstanceId='" + gatewayInstanceId + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
