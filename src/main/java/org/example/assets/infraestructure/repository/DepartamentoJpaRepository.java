package org.example.assets.infraestructure.repository;

import org.example.assets.infraestructure.entities.DepartamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartamentoJpaRepository extends JpaRepository<DepartamentoEntity, Long> {

    Optional<DepartamentoEntity> findByCodigo(String codigo);
}

