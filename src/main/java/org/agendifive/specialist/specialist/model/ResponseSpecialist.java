package org.agendifive.specialist.specialist.model;

import java.util.List;

public class ResponseSpecialist {
    private boolean success;
    private String message;
    private List<SpecialistDTO> data;

    private List<AvailabilityDto> availability;

    // Constructor completo
    public ResponseSpecialist(boolean success, String message, List<SpecialistDTO> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Constructor vacío
    public ResponseSpecialist() {
    }

    // Getters y Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SpecialistDTO> getData() {
        return data;
    }

    public void setData(List<SpecialistDTO> data) {
        this.data = data;
    }

    public List<AvailabilityDto> getAvailability() {
        return availability;
    }

    public void setAvailability(List<AvailabilityDto> availability) {
        this.availability = availability;
    }
}
