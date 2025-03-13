package org.agendifive.specialist.specialist.model;

import java.time.LocalTime;

public class AvailabilityDto {

    private LocalTime startTime;
    private LocalTime endTime;

    private String status;

    public AvailabilityDto(LocalTime startTime, LocalTime endTime, String status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
