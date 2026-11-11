package org.example.assets.application.ports.out;

import org.example.assets.domain.models.Activo;
import org.example.assets.domain.models.EstadoActivo;

import java.util.List;
import java.util.Optional;

public interface ActivoRepositoryPort {

    Activo guardar(Activo activo);

    Optional<Activo> buscarPorId(Long id);

    Optional<Activo> buscarPorCodigoInventario(String codigoInventario);

    Optional<Activo> buscarPorNumeroSerie(String numeroSerie);

    List<Activo> listarTodos();

    List<Activo> listarPorEstado(EstadoActivo estado);

    List<Activo> listarPorDepartamentoId(Long departamentoId);

    void eliminar(Long id);
}

