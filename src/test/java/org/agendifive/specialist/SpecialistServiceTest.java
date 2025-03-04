package org.agendifive.specialist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.agendifive.specialist.specialist.model.*;
import org.agendifive.specialist.specialist.service.SpecialistRepository;
import org.agendifive.specialist.specialist.service.SpecialistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

class SpecialistServiceTest {

    @Mock
    private SpecialistRepository specialistRepository;  // Mock del repositorio

    @InjectMocks
    private SpecialistService specialistService;  // Inyecta el mock en el servicio

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    void testGetSpecialisActive_NoSpecialistsFound() {

        when(specialistRepository.findActiveSpecialistsByEstablishment(1L))
                .thenReturn(Collections.emptyList());


        ResponseSpecialist response = specialistService.getSpecialisActive(1L);


        assertFalse(response.isSuccess());
        assertEquals("No se encuentran especialistas para el establecimiento", response.getMessage());
        assertNull(response.getData());


        verify(specialistRepository, times(1)).findActiveSpecialistsByEstablishment(1L);
    }

    @Test
    void testGetSpecialisActive_SpecialistsFound() {
        // Crear un especialista de prueba
        Specialist specialist = new Specialist();
        SpecialistSchedule specialistSchedule = new SpecialistSchedule();
        specialist.setFirstName("Juan");
        specialist.setLastName("Perez");
        specialist.setEmail("juan@example.com");
        specialist.setPhone("123456");

        specialistSchedule.setDayOfWeek("2");
        specialistSchedule.setStartTime(LocalTime.parse("09:00", DateTimeFormatter.ofPattern("HH:mm")));
        specialistSchedule.setEndTime(LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm")));

        specialist.setSpecialistSchedule(specialistSchedule);
        // Simular que hay especialistas activos
        when(specialistRepository.findActiveSpecialistsByEstablishment(1L))
                .thenReturn(List.of(specialist));

        // Ejecutar el método
        ResponseSpecialist response = specialistService.getSpecialisActive(1L);

        // Verificaciones
        assertTrue(response.isSuccess());
        assertEquals("Especialistas obtenidos correctamente", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());

        SpecialistDTO dto = response.getData().get(0);
        assertEquals("Juan", dto.getFirstName());
        assertEquals("Perez", dto.getLastName());

        // Verificar que el repositorio se llamó una vez
        verify(specialistRepository, times(1)).findActiveSpecialistsByEstablishment(1L);
    }

    @Test
    void testCreateSpecialist_Success() {
        // Configurar el objeto de solicitud
        SpecialistRequest request = new SpecialistRequest();
        request.setIdentification("12345");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPhone("5511234");
        request.setEstablishmentId(1L);
        request.setStatus('A');

        SpecialistScheduleRequest scheduleRequest = new SpecialistScheduleRequest();
        scheduleRequest.setDayOfWeek("1"); // Lunes
        scheduleRequest.setStartTime("09:00:15");
        scheduleRequest.setEndTime("17:00:15");
        request.setSchedules(scheduleRequest);

        // Mock del comportamiento del repositorio
        when(specialistRepository.save(any(Specialist.class))).thenReturn(new Specialist());

        // Llamar al método
        ResponseSpecialist response = specialistService.createSpecialist(request);

        // Verificar resultados
        assertTrue(response.isSuccess());
        assertEquals("se guardo correctamente el especialista", response.getMessage());

        // Verificar que se guardó el especialista
        verify(specialistRepository, times(1)).save(any(Specialist.class));
    }

    @Test
    void testCreateSpecialist_Fail_ValidationError() {
        // Configurar el objeto de solicitud con errores de validación
        SpecialistRequest request = new SpecialistRequest();
        request.setIdentification(""); // Identificación vacía
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPhone("555-1234");
        request.setEstablishmentId(1L);
        request.setStatus('A');

        // Llamar al método
        ResponseSpecialist response = specialistService.createSpecialist(request);

        // Verificar resultados
        assertFalse(response.isSuccess());
        assertNotNull(response.getMessage()); // El mensaje debe contener el error de validación
    }

}