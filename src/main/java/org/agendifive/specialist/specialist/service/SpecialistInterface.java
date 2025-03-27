package org.agendifive.specialist.specialist.service;


import org.agendifive.specialist.specialist.model.ResponseSpecialist;
import org.agendifive.specialist.specialist.model.SpecialistRequest;
import org.springframework.web.bind.annotation.PathVariable;

public interface SpecialistInterface {

    ResponseSpecialist getSpecialisActive(Long establishmentId);
    ResponseSpecialist createSpecialist(SpecialistRequest request);
    ResponseSpecialist getSpecialistByIdService(Long id,Long establishmentId);
    ResponseSpecialist getSspecialistAvailabilityById(Long idspecialist,int serviceDuration,String date);
    ResponseSpecialist fallbackResponse(Long idspecialist, int serviceDuration, String date, Throwable t);
}
