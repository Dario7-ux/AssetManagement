package org.example.assets.infraestructure.repository;

import org.example.assets.domain.models.EstadoActivo;
import org.example.assets.infraestructure.entities.ActivoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivoJpaRepository extends JpaRepository<ActivoEntity, Long> {

    Optional<ActivoEntity> findByCodigoInventario(String codigoInventario);

    Optional<ActivoEntity> findByNumeroSerie(String numeroSerie);

    List<ActivoEntity> findByEstado(EstadoActivo estado);

    List<ActivoEntity> findByDepartamentoId(Long departamentoId);
}

