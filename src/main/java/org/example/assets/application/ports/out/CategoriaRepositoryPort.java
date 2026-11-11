package org.example.assets.application.ports.out;

import org.example.assets.domain.models.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepositoryPort {

    Categoria guardar(Categoria categoria);

    Optional<Categoria> buscarPorId(Long id);

    Optional<Categoria> buscarPorCodigo(String codigo);

    Optional<Categoria> buscarPorNombre(String nombre);

    List<Categoria> listarTodas();
}

