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

package net.fhirfactory.harmonia.mllpgateway.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.topic.Topic;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Data transfer model encapsulating an outbound MLLP transmission request.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutboundMllpRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("messageControlId")
    private String messageControlId;

    @JsonProperty("destinationId")
    private String destinationId;

    @JsonProperty("rawMessage")
    private String rawMessage;

    @JsonProperty("targetQueue")
    private String targetQueue;

    @JsonProperty("topic")
    private Topic topic;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date timestamp;

    @JsonProperty("priority")
    private int priority = 4;

    @JsonProperty("taskId")
    private String taskId;

    @JsonProperty("facility")
    private String facility;

    @JsonProperty("correlationId")
    private String correlationId;

    public OutboundMllpRequest() {
        this.requestId = UUID.randomUUID().toString();
        this.timestamp = new Date();
    }

    public OutboundMllpRequest(String rawMessage, String destinationId) {
        this();
        this.rawMessage = rawMessage;
        this.destinationId = destinationId;
    }

    public OutboundMllpRequest(String messageControlId, String destinationId, String rawMessage, Topic topic) {
        this();
        this.messageControlId = messageControlId;
        this.destinationId = destinationId;
        this.rawMessage = rawMessage;
        this.topic = topic;
    }

    public OutboundMllpRequest(String requestId, String messageControlId, String destinationId,
                               String rawMessage, String targetQueue, Topic topic,
                               Date timestamp, int priority, String taskId, String facility, String correlationId) {
        this.requestId = requestId != null ? requestId : UUID.randomUUID().toString();
        this.messageControlId = messageControlId;
        this.destinationId = destinationId;
        this.rawMessage = rawMessage;
        this.targetQueue = targetQueue;
        this.topic = topic;
        this.timestamp = timestamp != null ? timestamp : new Date();
        this.priority = priority;
        this.taskId = taskId;
        this.facility = facility;
        this.correlationId = correlationId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessageControlId() {
        return messageControlId;
    }

    public void setMessageControlId(String messageControlId) {
        this.messageControlId = messageControlId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public String getTargetQueue() {
        return targetQueue;
    }

    public void setTargetQueue(String targetQueue) {
        this.targetQueue = targetQueue;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutboundMllpRequest that = (OutboundMllpRequest) o;
        return priority == that.priority &&
                Objects.equals(requestId, that.requestId) &&
                Objects.equals(messageControlId, that.messageControlId) &&
                Objects.equals(destinationId, that.destinationId) &&
                Objects.equals(rawMessage, that.rawMessage) &&
                Objects.equals(targetQueue, that.targetQueue) &&
                Objects.equals(topic, that.topic) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(facility, that.facility) &&
                Objects.equals(correlationId, that.correlationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, messageControlId, destinationId, rawMessage, targetQueue, topic, priority, taskId, facility, correlationId);
    }

    @Override
    public String toString() {
        return "OutboundMllpRequest{" +
                "requestId='" + requestId + '\'' +
                ", messageControlId='" + messageControlId + '\'' +
                ", destinationId='" + destinationId + '\'' +
                ", targetQueue='" + targetQueue + '\'' +
                ", priority=" + priority +
                ", taskId='" + taskId + '\'' +
                ", facility='" + facility + '\'' +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }
}
