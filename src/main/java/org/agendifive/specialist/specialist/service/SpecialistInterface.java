package org.agendifive.specialist.specialist.service;


import org.agendifive.specialist.specialist.model.ResponseSpecialist;
import org.agendifive.specialist.specialist.model.SpecialistRequest;

public interface SpecialistInterface {

    ResponseSpecialist getSpecialisActive(Long establishmentId);
    ResponseSpecialist createSpecialist(SpecialistRequest request);
}
