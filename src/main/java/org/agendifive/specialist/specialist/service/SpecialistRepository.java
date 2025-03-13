package org.agendifive.specialist.specialist.service;


import org.agendifive.specialist.specialist.model.Specialist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {

    // Obtener todos los especialistas activos (estado 'A')
    @Query("SELECT s FROM Specialist s WHERE s.status = 'A'")
    List<Specialist> findAllActiveSpecialists();

    // Obtener especialistas activos de un establecimiento específico
    @Query("SELECT s FROM Specialist s WHERE s.establishmentId = :establishmentId AND s.status = 'A'")
    List<Specialist> findActiveSpecialistsByEstablishment(Long establishmentId);

    @Query("SELECT s FROM Specialist s WHERE s.id = :specialistId AND s.status = 'A'")
    Specialist findActiveSpecialistsById(Long specialistId);

    @EntityGraph(attributePaths = {"services"})
    @Query("SELECT s FROM Specialist s JOIN s.services serv WHERE serv.id = :serviceId AND s.status = 'A'")
    List<Specialist> findActiveSpecialistsByServiceId(@Param("serviceId") Long serviceId);

    @EntityGraph(attributePaths = {"services"})
    @Query("SELECT s FROM Specialist s JOIN s.services serv WHERE s.id = :specialistId AND serv.id = :serviceId AND s.status = 'A' AND serv.status = 'A'")
    Specialist findActiveSpecialistByIdAndServiceId(
            @Param("specialistId") Long specialistId,
            @Param("serviceId") Long serviceId);
}