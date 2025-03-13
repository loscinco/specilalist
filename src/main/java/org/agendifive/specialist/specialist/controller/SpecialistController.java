package org.agendifive.specialist.specialist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.agendifive.specialist.specialist.model.ResponseSpecialist;
import org.agendifive.specialist.specialist.model.SpecialistRequest;
import org.agendifive.specialist.specialist.service.SpecialistInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/specialists")
@Tag(name = "SpecialistController", description = "Controlador para obtenenr informacion de los especialistas")
public class SpecialistController {


    @Autowired
    private SpecialistInterface specialistInterface;

    @GetMapping("/active/{establishmentId}")
    @Operation(
            summary = "Obtener especialistas activos por establecimiento",
            description = "Retorna una lista de especialistas activos según el ID del establecimiento proporcionado."
    )
    public ResponseSpecialist getActiveSpecialistsByEstablishment(@PathVariable Long establishmentId) {
        return specialistInterface.getSpecialisActive(establishmentId);
    }

    @PostMapping
    @Operation(
            summary = "Crear un nuevo especialista",
            description = "Registra un nuevo especialista en el sistema con los datos proporcionados en la solicitud."
    )
    public ResponseSpecialist createSpecialist(@RequestBody SpecialistRequest request) {
        return specialistInterface.createSpecialist(request);
    }

    @GetMapping("specialistbyid/{idservice}/{establishmentId}")
    @Operation(
            summary = "Obtener especialistas activos por servicio",
            description = "Retorna una lista de especialistas activos según el ID del servicio y el establishmentId."
    )
    public ResponseSpecialist getSpecialistById(@PathVariable Long idservice, @PathVariable Long establishmentId) {
        return specialistInterface.getSpecialistByIdService(idservice, establishmentId);
    }

    @GetMapping("specialistAvailability/{idspecialist}/{serviceDuration}/{date}")
    @Operation(
            summary = "Obtener disponibilidad de especialista",
            description = "Retorna disponibilidad de especialista consultado."
    )
    public ResponseSpecialist getSspecialistAvailabilityById(@PathVariable Long idspecialist,@PathVariable int serviceDuration,@PathVariable String date ) {
        return specialistInterface.getSspecialistAvailabilityById(idspecialist,serviceDuration,date);
    }
}