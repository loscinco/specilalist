package org.agendifive.specialist.specialist.service;


import org.agendifive.specialist.specialist.model.ResponseSpecialist;

public interface SpecialistInterface {

    ResponseSpecialist getSpecialisActive(Long establishmentId);
}
