package org.agendifive.specialist.specialist.model.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class Appointment {
    @JsonProperty("id")
    private int id;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("service")
    private int service;

    @JsonProperty("specialist")
    private int specialist;

    @JsonProperty("appointmentDate")
    private String appointmentDate;

    @JsonProperty("appointmentTime")
    private LocalTime appointmentTime;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("status")
    private String status;

    @JsonProperty("appointmentTimeFinish")
    private LocalTime appointmentTimeFinish;

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getSpecialist() {
        return specialist;
    }

    public void setSpecialist(int specialist) {
        this.specialist = specialist;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalTime getAppointmentTimeFinish() {
        return appointmentTimeFinish;
    }

    public void setAppointmentTimeFinish(LocalTime appointmentTimeFinish) {
        this.appointmentTimeFinish = appointmentTimeFinish;
    }
}