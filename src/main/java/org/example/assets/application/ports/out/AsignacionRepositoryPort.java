package org.example.assets.application.ports.out;

import org.example.assets.domain.models.Asignacion;

import java.util.List;
import java.util.Optional;

public interface AsignacionRepositoryPort {

    Asignacion guardar(Asignacion asignacion);

    Optional<Asignacion> buscarPorId(Long id);

    Optional<Asignacion> buscarActivaPorActivoId(Long activoId);

    List<Asignacion> listarTodas();

    List<Asignacion> listarPorActivoId(Long activoId);

    List<Asignacion> listarPorUsuarioId(Long usuarioId);
}

