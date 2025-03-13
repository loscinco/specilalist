package org.agendifive.specialist.specialist.service;


import org.agendifive.specialist.specialist.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@Service
public class SpecialistService implements SpecialistInterface {

    @Autowired
    private SpecialistRepository specialistRepository;
    @Autowired
    private SpecialistAvailability specialistAvailability;
    @Override
    public ResponseSpecialist getSpecialisActive(Long establishmentId) {
        List<Specialist> specialistList = specialistRepository.findActiveSpecialistsByEstablishment(establishmentId);
        ResponseSpecialist responseSpecialist = new ResponseSpecialist(false,"No se encuentran especialistas para el establecimiento",null);
        if(specialistList.size()>0){
            responseSpecialist.setSuccess(true);
            responseSpecialist.setMessage("Especialistas obtenidos correctamente");
            responseSpecialist.setData(convertToDtoList(specialistList));
        }

        return responseSpecialist;
    }

    @Override
    @Transactional
    public ResponseSpecialist createSpecialist(SpecialistRequest request) {
        ResponseSpecialist responseSpecialist = new ResponseSpecialist(false,"",null);
        String errors = validateRequest(request);
        if(errors == null){
            Specialist specialist = new Specialist();
            specialist.setIdentification(request.getIdentification());
            specialist.setFirstName(request.getFirstName());
            specialist.setLastName(request.getLastName());
            specialist.setEmail(request.getEmail());
            specialist.setPhone(request.getPhone());
            specialist.setEstablishmentId(request.getEstablishmentId());
            specialist.setStatus(request.getStatus());
            specialist.setCreatedAt(LocalDateTime.now());

            if (request.getSchedules() != null) {
                String startTime = request.getSchedules().getStartTime();
                String endTime = request.getSchedules().getEndTime();
                DateTimeFormatter formatter = startTime.length() == 5 ? DateTimeFormatter.ofPattern("HH:mm") : DateTimeFormatter.ofPattern("HH:mm:ss");
                DateTimeFormatter formatter2 = endTime.length() == 5 ? DateTimeFormatter.ofPattern("HH:mm") : DateTimeFormatter.ofPattern("HH:mm:ss");
                SpecialistSchedule schedule = new SpecialistSchedule();
                schedule.setDayOfWeek(request.getSchedules().getDayOfWeek());
                schedule.setStartTime(LocalTime.parse(startTime, formatter));
                schedule.setEndTime(LocalTime.parse(endTime, formatter2));
                schedule.setSpecialist(specialist);
                specialist.setSpecialistSchedule(schedule);
                specialist.setStatus("A");
                specialistRepository.save(specialist);


                responseSpecialist.setSuccess(true);
                responseSpecialist.setMessage("se guardo correctamente el especialista");
                return responseSpecialist;
            }
        }
        responseSpecialist.setMessage(errors);
        return responseSpecialist;

    }

    @Override
    @Transactional
    public ResponseSpecialist getSpecialistByIdService(Long id, Long establishmentId) {
        ResponseSpecialist responseSpecialist = new ResponseSpecialist();
        responseSpecialist.setMessage("no se encuentran especialistas para el servicio y el establecimiento");
        responseSpecialist.setSuccess(false);
        List<Specialist> specialists= specialistRepository.findActiveSpecialistsByServiceId(id);
        if(specialists.size() > 0){
            List<SpecialistDTO> specialistDTOList = specialists.stream()
                    .filter(specialist -> "A".equals(specialist.getStatus()) && establishmentId.equals(specialist.getEstablishmentId()))
                    .map(specialist -> new SpecialistDTO(
                            specialist.getId(),
                            specialist.getFirstName(),
                            specialist.getLastName(),
                            specialist.getEmail(),
                            specialist.getPhone(),
                            specialist.getSpecialistSchedule().getDayOfWeek(),
                            specialist.getSpecialistSchedule().getStartTime(),
                            specialist.getSpecialistSchedule().getEndTime()
                    ))
                    .collect(Collectors.toList());
            if(specialistDTOList.size()>0){
                responseSpecialist.setMessage("se obtuvieron los especialistas con exito");
                responseSpecialist.setSuccess(true);
                responseSpecialist.setData(specialistDTOList);
            }

        }

        return responseSpecialist;
    }

    @Override
    public ResponseSpecialist getSspecialistAvailabilityById(Long idspecialist,int serviceDuration,String date) {
        ResponseSpecialist response = new ResponseSpecialist();
        response.setSuccess(false);
        response.setMessage("No se encontro disponibilidad");
        Specialist specialist = specialistRepository.findActiveSpecialistsById(idspecialist);
        if(specialist != null){
            List<AvailabilityDto> availabilityDtos = specialistAvailability.getAvailabilitySpecialist(specialist,serviceDuration,date);
            if(availabilityDtos.size() > 0){
                response.setSuccess(true);
                response.setMessage("se encontro disponibilidad");
                response.setAvailability(availabilityDtos);
            }
        }

        return response;
    }


    private List<SpecialistDTO> convertToDtoList(List<Specialist> specialists) {
        return specialists.stream()
                .map(specialist -> {
                    SpecialistDTO dto = new SpecialistDTO();
                    dto.setId(specialist.getId());
                    dto.setFirstName(specialist.getFirstName());
                    dto.setLastName(specialist.getLastName());
                    dto.setEmail(specialist.getEmail());
                    dto.setPhone(specialist.getPhone());
                    dto.setDayOfWeek(specialist.getSpecialistSchedule().getDayOfWeek());
                    dto.setStartTime(specialist.getSpecialistSchedule().getStartTime());
                    dto.setEndTime(specialist.getSpecialistSchedule().getEndTime());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private String validateRequest(SpecialistRequest request) {
        StringBuilder errors = new StringBuilder();

        if (request.getIdentification() == null || request.getIdentification().isEmpty()) {
            errors.append("Identification es requerida. ");
        }
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            errors.append("primer nombre es requerido. ");
        } else if (!request.getFirstName().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$")) {
            errors.append("solo se permiten letras para el nombre. ");
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            errors.append("apellido es requerido. ");
        } else if (!request.getLastName().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$")) {
            errors.append("solo se permiten letras para el apellido. ");
        }
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            errors.append("Email es requerido. ");
        } else if (!request.getEmail().matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$")) {
            errors.append("formato invalido del email. ");
        }
        if (request.getPhone() == null || !request.getPhone().matches("^\\d{7}|\\d{10}$")) {
            errors.append("el telefono debe tener 7 o 10 digitos. ");
        }
        if (request.getEstablishmentId() == null) {
            errors.append("el establecimiento es requerido. ");
        }
        if (request.getSchedules() == null) {
            errors.append("El horario es requerido ");
        }else {
            // Validar cada horario
            String dayOfWeek = request.getSchedules().getDayOfWeek();
            String startTime = request.getSchedules().getStartTime();
            String endTime = request.getSchedules().getEndTime();
                // Validación de dayOfWeek: solo números 1-7 permitidos, separados por comas opcionalmente
            if (dayOfWeek == null || !Pattern.matches("^[1-7](-[1-7])?(,[1-7](-[1-7])?)*$", dayOfWeek)) {
                errors.append("El día de la semana debe estar entre 1 y 7, opcionalmente separados por comas o en rangos con guion.");
            }

            // Validación de startTime y endTime entre 00:00:00 y 24:00:00
                if (startTime == null) {
                   String errors_time =parseTime(startTime);
                    if(errors_time!= null){
                        errors.append(errors_time);
                    }

                }
                if (endTime == null) {
                    String errors_time1 =parseTime(endTime);
                    if(errors_time1!= null){
                        errors.append(errors_time1);
                    }

                }

        }

        return errors.length() > 0 ? errors.toString().trim() : null;
    }

    private boolean isValidTime(String time) {
        return time.matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
    }

    private String parseTime(String timeString) {
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            // Intentamos convertir el String a LocalTime
            LocalTime.parse(timeString, formatter);
            return null;
        } catch (DateTimeParseException e) {
            // Si ocurre un error en el parseo, devolvemos un mensaje de error
            return "Error: El formato de la hora es incorrecto. Debe ser HH:mm:ss";
        }
    }



}
