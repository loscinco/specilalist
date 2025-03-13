package org.agendifive.specialist.specialist.model;

public class SpecialistRequest {
    private String identification;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Long establishmentId;
    private String status;
    private SpecialistScheduleRequest schedules;

    // Getters y setters
    public String getIdentification() {
        return identification;
    }
    public void setIdentification(String identification) {
        this.identification = identification;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Long getEstablishmentId() {
        return establishmentId;
    }
    public void setEstablishmentId(Long establishmentId) {
        this.establishmentId = establishmentId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public SpecialistScheduleRequest getSchedules() {
        return schedules;
    }

    public void setSchedules(SpecialistScheduleRequest schedules) {
        this.schedules = schedules;
    }
}
