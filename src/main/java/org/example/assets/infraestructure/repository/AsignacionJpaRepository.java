package org.example.assets.infraestructure.repository;

import org.example.assets.domain.models.EstadoAsignacion;
import org.example.assets.infraestructure.entities.AsignacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionJpaRepository extends JpaRepository<AsignacionEntity, Long> {

    Optional<AsignacionEntity> findByActivoIdAndEstadoAsignacion(Long activoId, EstadoAsignacion estadoAsignacion);

    List<AsignacionEntity> findByActivoId(Long activoId);

    List<AsignacionEntity> findByUsuarioId(Long usuarioId);
}

