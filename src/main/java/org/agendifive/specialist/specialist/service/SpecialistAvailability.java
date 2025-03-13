package org.agendifive.specialist.specialist.service;

import org.agendifive.specialist.specialist.model.AvailabilityDto;
import org.agendifive.specialist.specialist.model.Specialist;
import org.agendifive.specialist.specialist.model.booking.BookingResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SpecialistAvailability {

    public List<AvailabilityDto> getAvailabilitySpecialist(Specialist specialist, int serviceDuration, String date){

        if(specialist != null){
            //verifica q el dia este dentro del horario del specialist
         boolean isTodayInDayOfWeek = isTodayInDayOfWeek(specialist.getSpecialistSchedule().getDayOfWeek());
         if(isTodayInDayOfWeek){
             List<AvailabilityDto> availabilityDto = new ArrayList<>();
             List<AvailabilityDto> usedTimes = getScheduleBySpecialistAndDate(specialist.getId().intValue(),date);
             availabilityDto = generateAvailability(serviceDuration, usedTimes, specialist.getSpecialistSchedule().getStartTime(), specialist.getSpecialistSchedule().getEndTime(),date);
            return availabilityDto;
         }
         return null;
        }
        return null;
    }

    public boolean isTodayInDayOfWeek(String dayOfWeek) {
        // Obtener el día de la semana actual (1 = lunes, 7 = domingo)
        int today = LocalDate.now().getDayOfWeek().getValue();

        // Parsear el string day_of_week
        Set<Integer> validDays = parseDayOfWeek(dayOfWeek);

        // Verificar si el día actual está en la lista de días válidos
        return validDays.contains(today);
    }

    // Método para parsear day_of_week
    private Set<Integer> parseDayOfWeek(String dayOfWeek) {
        Set<Integer> validDays = new HashSet<>();

        // Dividir el string en partes usando la coma como separador
        String[] parts = dayOfWeek.split(",");

        for (String part : parts) {
            // Si la parte tiene un guion, es un rango de días
            if (part.contains("-")) {
                String[] range = part.split("-");
                int start = Integer.parseInt(range[0].trim());
                int end = Integer.parseInt(range[1].trim());
                for (int i = start; i <= end; i++) {
                    validDays.add(i);
                }
            } else {
                // Si es un solo día, agregarlo al conjunto
                validDays.add(Integer.parseInt(part.trim()));
            }
        }

        return validDays;
    }




    public List<AvailabilityDto> getScheduleBySpecialistAndDate(Integer specialistId, String date) {
        RestTemplate restTemplate = new RestTemplate();
        List<LocalTime> hours = new ArrayList<>();
        String url = "https://rcapruebas.unad.edu.co:8002/agendamiento-api/api/getschedulebyspecialistbydate/" + specialistId + "/" + date;
        // String url = "http://localhost:8080/api/api/getschedulebyspecialistbydate/" + specialistId + "/" + date;

        ResponseEntity<BookingResponse> responseEntity = restTemplate.getForEntity(url, BookingResponse.class);

        BookingResponse bookingResponse = responseEntity.getBody();
        List<AvailabilityDto> availabilityDTOList = new ArrayList<>();
        if(bookingResponse.getData() != null ){
            bookingResponse.getData().forEach(data->{
               AvailabilityDto availabilityDTO = new AvailabilityDto(data.getAppointmentTime(),data.getAppointmentTimeFinish(),"ocupado");
                availabilityDTOList.add(availabilityDTO);
            });
        }

        return availabilityDTOList;
    }

    private List<AvailabilityDto> generateAvailability(int serviceTimeMinutes, List<AvailabilityDto> usedTimes,
                                                       LocalTime startTime, LocalTime endTime, String date) {
        List<AvailabilityDto> availabilityList = new ArrayList<>();
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        // Convertir el String date a LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate appointmentDate = LocalDate.parse(date, formatter);

        // Ordenar usedTimes por hora de inicio en orden ascendente
        List<AvailabilityDto> sortedUsedTimes = usedTimes.stream()
                .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
                .collect(Collectors.toList());

        LocalTime current = startTime;

        while (!current.isAfter(endTime)) {
            // Buscar si current está dentro de un bloque ocupado
            LocalTime finalCurrent = current;
            boolean isOccupied = sortedUsedTimes.stream()
                    .anyMatch(used -> !finalCurrent.isBefore(used.getStartTime()) && finalCurrent.isBefore(used.getEndTime()));

            LocalTime next = current.plusMinutes(serviceTimeMinutes);

            if (isOccupied) {
                // Si está ocupado, obtener el fin del bloque y continuar desde ahí
                LocalTime finalCurrent1 = current;
                LocalTime occupiedUntil = sortedUsedTimes.stream()
                        .filter(used -> !finalCurrent1.isBefore(used.getStartTime()) && finalCurrent1.isBefore(used.getEndTime()))
                        .map(AvailabilityDto::getEndTime)
                        .max(LocalTime::compareTo)
                        .orElse(next);

                availabilityList.add(new AvailabilityDto(current, occupiedUntil, "ocupado"));
                current = occupiedUntil; // Saltar al final del bloque ocupado
            } else {
                // Solo marcar como ocupado si la fecha es hoy y la hora ya pasó
                String status = (appointmentDate.equals(today) && current.isBefore(now)) ? "ocupado" : "libre";

                if (!next.isAfter(endTime)) {
                    availabilityList.add(new AvailabilityDto(current, next, status));
                }
                current = next; // Avanzar al siguiente intervalo
            }
        }

        return availabilityList;
    }
}
