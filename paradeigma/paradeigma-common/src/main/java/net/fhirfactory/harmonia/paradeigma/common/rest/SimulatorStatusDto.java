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

package net.fhirfactory.harmonia.paradeigma.common.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Status and runtime metrics DTO returned by simulated healthcare system REST endpoints.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimulatorStatusDto {

    private String systemName;        // PAS, EMR, LMS, RIS-PAC
    private boolean running;
    private int mllpPort;
    private long messagesSent;
    private long messagesReceived;
    private long ackAcceptCount;
    private long ackErrorCount;
    private long ackRejectCount;
    private long failureCount;
    private LocalDateTime startedAt;
    private LocalDateTime lastActivityAt;

    public SimulatorStatusDto() {
    }

    public SimulatorStatusDto(String systemName, boolean running, int mllpPort) {
        this.systemName = systemName;
        this.running = running;
        this.mllpPort = mllpPort;
        this.startedAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getMllpPort() {
        return mllpPort;
    }

    public void setMllpPort(int mllpPort) {
        this.mllpPort = mllpPort;
    }

    public long getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(long messagesSent) {
        this.messagesSent = messagesSent;
    }

    public long getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(long messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public long getAckAcceptCount() {
        return ackAcceptCount;
    }

    public void setAckAcceptCount(long ackAcceptCount) {
        this.ackAcceptCount = ackAcceptCount;
    }

    public long getAckErrorCount() {
        return ackErrorCount;
    }

    public void setAckErrorCount(long ackErrorCount) {
        this.ackErrorCount = ackErrorCount;
    }

    public long getAckRejectCount() {
        return ackRejectCount;
    }

    public void setAckRejectCount(long ackRejectCount) {
        this.ackRejectCount = ackRejectCount;
    }

    public long getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(long failureCount) {
        this.failureCount = failureCount;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }
}
