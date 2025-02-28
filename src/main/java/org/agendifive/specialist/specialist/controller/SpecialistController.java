package org.agendifive.specialist.specialist.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.agendifive.specialist.specialist.model.ResponseSpecialist;
import org.agendifive.specialist.specialist.service.SpecialistInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specialists")
@Tag(name = "SpecialistController", description = "Controlador para obtenenr informacion de los especialistas")
public class SpecialistController {


    @Autowired
    private SpecialistInterface specialistInterface;

    @GetMapping("/active/{establishmentId}")
    public ResponseSpecialist getActiveSpecialistsByEstablishment(@PathVariable Long establishmentId) {
       return specialistInterface.getSpecialisActive(establishmentId);
    }
}