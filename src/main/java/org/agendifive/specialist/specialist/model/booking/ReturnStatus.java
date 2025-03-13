package org.agendifive.specialist.specialist.model.booking;

import com.fasterxml.jackson.annotation.JsonProperty;



public class ReturnStatus {
    @JsonProperty("businessMessage")
    private String businessMessage;

    @JsonProperty("status")
    private String status;

    public String getBusinessMessage() {
        return businessMessage;
    }

    public void setBusinessMessage(String businessMessage) {
        this.businessMessage = businessMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}