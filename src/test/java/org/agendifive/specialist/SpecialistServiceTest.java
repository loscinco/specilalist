package org.agendifive.specialist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.agendifive.specialist.specialist.model.ResponseSpecialist;
import org.agendifive.specialist.specialist.model.Specialist;
import org.agendifive.specialist.specialist.model.SpecialistDTO;
import org.agendifive.specialist.specialist.service.SpecialistRepository;
import org.agendifive.specialist.specialist.service.SpecialistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        specialist.setFirstName("Juan");
        specialist.setLastName("Perez");
        specialist.setEmail("juan@example.com");
        specialist.setPhone("123456");

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
}