package org.example.assets.application.ports.in;

import org.example.assets.domain.models.Categoria;

import java.util.List;

public interface CategoriaUseCase {

    Categoria registrar(Categoria categoria);

    Categoria buscarPorId(Long id);

    List<Categoria> listarTodas();
}

