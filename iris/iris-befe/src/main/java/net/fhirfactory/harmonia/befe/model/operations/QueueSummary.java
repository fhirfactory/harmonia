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

package net.fhirfactory.harmonia.befe.model.operations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Petasos message queue operational summary.
 * Captures message depths, consumer counts, enqueue/dequeue rates, redelivery and DLQ statistics.
 * In accordance with Invariant 7 (Zero-PHI), message bodies/payloads are strictly omitted.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueSummary implements Serializable {

    private String queueId;
    private String queueName;
    private String address;
    private String status;               // HEALTHY, DEGRADED, UNHEALTHY, UNKNOWN
    private long depth;
    private int consumerCount;
    private int producerCount;
    private double enqueueRate;          // msg/sec
    private double dequeueRate;          // msg/sec
    private long oldestMessageAgeSeconds;
    private long redeliveryCount;
    private long dlqDepth;
    private long expiryCount;
    private String associatedCapability;
    private List<TimeSeriesPoint> depthHistory = new ArrayList<>();

    public QueueSummary() {
    }

    public QueueSummary(String queueId, String queueName, String address, String status,
                        long depth, int consumerCount, int producerCount, double enqueueRate,
                        double dequeueRate, long oldestMessageAgeSeconds, long redeliveryCount,
                        long dlqDepth, long expiryCount, String associatedCapability) {
        this.queueId = queueId;
        this.queueName = queueName;
        this.address = address;
        this.status = status;
        this.depth = depth;
        this.consumerCount = consumerCount;
        this.producerCount = producerCount;
        this.enqueueRate = enqueueRate;
        this.dequeueRate = dequeueRate;
        this.oldestMessageAgeSeconds = oldestMessageAgeSeconds;
        this.redeliveryCount = redeliveryCount;
        this.dlqDepth = dlqDepth;
        this.expiryCount = expiryCount;
        this.associatedCapability = associatedCapability;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    public int getConsumerCount() {
        return consumerCount;
    }

    public void setConsumerCount(int consumerCount) {
        this.consumerCount = consumerCount;
    }

    public int getProducerCount() {
        return producerCount;
    }

    public void setProducerCount(int producerCount) {
        this.producerCount = producerCount;
    }

    public double getEnqueueRate() {
        return enqueueRate;
    }

    public void setEnqueueRate(double enqueueRate) {
        this.enqueueRate = enqueueRate;
    }

    public double getDequeueRate() {
        return dequeueRate;
    }

    public void setDequeueRate(double dequeueRate) {
        this.dequeueRate = dequeueRate;
    }

    public long getOldestMessageAgeSeconds() {
        return oldestMessageAgeSeconds;
    }

    public void setOldestMessageAgeSeconds(long oldestMessageAgeSeconds) {
        this.oldestMessageAgeSeconds = oldestMessageAgeSeconds;
    }

    public long getRedeliveryCount() {
        return redeliveryCount;
    }

    public void setRedeliveryCount(long redeliveryCount) {
        this.redeliveryCount = redeliveryCount;
    }

    public long getDlqDepth() {
        return dlqDepth;
    }

    public void setDlqDepth(long dlqDepth) {
        this.dlqDepth = dlqDepth;
    }

    public long getExpiryCount() {
        return expiryCount;
    }

    public void setExpiryCount(long expiryCount) {
        this.expiryCount = expiryCount;
    }

    public String getAssociatedCapability() {
        return associatedCapability;
    }

    public void setAssociatedCapability(String associatedCapability) {
        this.associatedCapability = associatedCapability;
    }

    public List<TimeSeriesPoint> getDepthHistory() {
        return depthHistory;
    }

    public void setDepthHistory(List<TimeSeriesPoint> depthHistory) {
        this.depthHistory = depthHistory != null ? new ArrayList<>(depthHistory) : new ArrayList<>();
    }
}
