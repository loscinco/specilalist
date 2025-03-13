package org.agendifive.specialist.specialist.model.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BookingResponse {
    @JsonProperty("data")
    private List<Appointment> data;

    @JsonProperty("returnStatus")
    private ReturnStatus returnStatus;

    // Getters y Setters
    public List<Appointment> getData() {
        return data;
    }

    public void setData(List<Appointment> data) {
        this.data = data;
    }

    public ReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(ReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }

}
