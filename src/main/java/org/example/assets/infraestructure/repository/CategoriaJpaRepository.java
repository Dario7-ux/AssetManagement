package org.example.assets.infraestructure.repository;

import org.example.assets.infraestructure.entities.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaJpaRepository extends JpaRepository<CategoriaEntity, Long> {

    Optional<CategoriaEntity> findByCodigo(String codigo);

    Optional<CategoriaEntity> findByNombre(String nombre);
}

