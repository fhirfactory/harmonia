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

package net.fhirfactory.hie.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.fhirfactory.hie.model.topic.Topic;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Lightweight event notification representing an action or status transition on a FHIR Task.
 * Contains essential metadata to identify the task, action, status, and {@link Topic},
 * allowing downstream processing pipelines to retrieve the full Task resource from cache.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String taskId;
    private String action;
    private String status;
    private Topic topic;
    private String gatewayInstanceId;
    private String messageType;
    private String triggerType;
    private String controlId;
    private String source;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date timestamp;

    public TaskEvent() {
        this.timestamp = new Date();
    }

    public TaskEvent(String taskId, String action, String status) {
        this();
        this.taskId = taskId;
        this.action = action;
        this.status = status;
    }

    public TaskEvent(String taskId, String action, String status, String source, String description) {
        this(taskId, action, status);
        this.source = source;
        this.description = description;
    }

    public TaskEvent(String taskId, String action, String status, Topic topic, String controlId, String description) {
        this(taskId, action, status);
        this.controlId = controlId;
        this.description = description;
        setTopic(topic);
    }

    public TaskEvent(String taskId, String action, String status, String gatewayInstanceId,
                     String messageType, String triggerType, String controlId,
                     String source, String description) {
        this(taskId, action, status, source, description);
        this.gatewayInstanceId = gatewayInstanceId;
        this.messageType = messageType;
        this.triggerType = triggerType;
        this.controlId = controlId;
        ensureTopicSync();
    }

    private void ensureTopicSync() {
        if (this.topic == null) {
            String gw = gatewayInstanceId != null ? gatewayInstanceId : source;
            this.topic = Topic.fromHl7(messageType != null ? messageType : "ADT",
                    triggerType != null ? triggerType : "",
                    gw);
        } else {
            if (gatewayInstanceId != null) this.topic.setSource(gatewayInstanceId);
            if (messageType != null) this.topic.setDataElement(messageType);
            if (triggerType != null) this.topic.setDataElementQualifier(triggerType);
        }
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Topic getTopic() {
        if (this.topic == null && (gatewayInstanceId != null || messageType != null || triggerType != null)) {
            ensureTopicSync();
        }
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
        if (topic != null) {
            if (topic.getSource() != null) {
                this.gatewayInstanceId = topic.getSource();
                this.source = topic.getSource();
            }
            if (topic.getDataElement() != null) {
                this.messageType = topic.getDataElement();
            }
            if (topic.getDataElementQualifier() != null) {
                this.triggerType = topic.getDataElementQualifier();
            }
        }
    }

    public String getGatewayInstanceId() {
        if (gatewayInstanceId == null && topic != null) {
            return topic.getSource();
        }
        return gatewayInstanceId;
    }

    public void setGatewayInstanceId(String gatewayInstanceId) {
        this.gatewayInstanceId = gatewayInstanceId;
        if (this.topic != null) {
            this.topic.setSource(gatewayInstanceId);
        }
    }

    public String getMessageType() {
        if (messageType == null && topic != null) {
            return topic.getDataElement();
        }
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
        if (this.topic != null) {
            this.topic.setDataElement(messageType);
        }
    }

    public String getTriggerType() {
        if (triggerType == null && topic != null) {
            return topic.getDataElementQualifier();
        }
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
        if (this.topic != null) {
            this.topic.setDataElementQualifier(triggerType);
        }
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public String getSource() {
        if (source == null && topic != null) {
            return topic.getSource();
        }
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        if (this.topic != null && this.topic.getSource() == null) {
            this.topic.setSource(source);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEvent taskEvent = (TaskEvent) o;
        return Objects.equals(taskId, taskEvent.taskId) &&
                Objects.equals(action, taskEvent.action) &&
                Objects.equals(status, taskEvent.status) &&
                Objects.equals(getGatewayInstanceId(), taskEvent.getGatewayInstanceId()) &&
                Objects.equals(getMessageType(), taskEvent.getMessageType()) &&
                Objects.equals(getTriggerType(), taskEvent.getTriggerType()) &&
                Objects.equals(controlId, taskEvent.controlId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, action, status, getGatewayInstanceId(), getMessageType(), getTriggerType(), controlId);
    }

    @Override
    public String toString() {
        return "TaskEvent{" +
                "taskId='" + taskId + '\'' +
                ", action='" + action + '\'' +
                ", status='" + status + '\'' +
                ", topic=" + topic +
                ", gatewayInstanceId='" + getGatewayInstanceId() + '\'' +
                ", messageType='" + getMessageType() + '\'' +
                ", triggerType='" + getTriggerType() + '\'' +
                ", controlId='" + controlId + '\'' +
                ", source='" + getSource() + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
