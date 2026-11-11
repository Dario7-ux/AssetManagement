package org.example.assets.application.services;

import org.example.assets.application.ports.in.CategoriaUseCase;
import org.example.assets.application.ports.out.CategoriaRepositoryPort;
import org.example.assets.domain.exception.CategoriaNoEncontradaException;
import org.example.assets.domain.exception.DatoInvalidoException;
import org.example.assets.domain.exception.ReglaNegocioException;
import org.example.assets.domain.models.Categoria;

import java.util.List;

public class CategoriaService implements CategoriaUseCase {

    private final CategoriaRepositoryPort categoriaRepositoryPort;

    public CategoriaService(CategoriaRepositoryPort categoriaRepositoryPort) {
        this.categoriaRepositoryPort = categoriaRepositoryPort;
    }

    @Override
    public Categoria registrar(Categoria categoria) {
        if (categoria == null) {
            throw new DatoInvalidoException("La categoria no puede ser nula");
        }
        if (categoria.getCodigo() == null || categoria.getCodigo().isBlank()) {
            throw new DatoInvalidoException("El codigo de la categoria es obligatorio");
        }
        if (categoria.getNombre() == null || categoria.getNombre().isBlank()) {
            throw new DatoInvalidoException("El nombre de la categoria es obligatorio");
        }

        categoriaRepositoryPort.buscarPorCodigo(categoria.getCodigo().trim()).ifPresent(c -> {
            throw new ReglaNegocioException("Ya existe una categoria con el codigo '" + categoria.getCodigo() + "'");
        });

        categoriaRepositoryPort.buscarPorNombre(categoria.getNombre().trim()).ifPresent(c -> {
            throw new ReglaNegocioException("Ya existe una categoria con el nombre '" + categoria.getNombre() + "'");
        });

        return categoriaRepositoryPort.guardar(categoria);
    }

    @Override
    public Categoria buscarPorId(Long id) {
        if (id == null) {
            throw new DatoInvalidoException("El id de la categoria es obligatorio");
        }
        return categoriaRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoria con id " + id + " no encontrada"));
    }

    @Override
    public List<Categoria> listarTodas() {
        return categoriaRepositoryPort.listarTodas();
    }
}

