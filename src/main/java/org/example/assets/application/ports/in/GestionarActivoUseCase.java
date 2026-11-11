package org.example.assets.application.ports.in;

import org.example.assets.domain.models.Activo;
import org.example.assets.domain.models.EstadoActivo;

import java.util.List;

public interface GestionarActivoUseCase {

    Activo buscarPorId(Long id);

    List<Activo> listarTodos();

    List<Activo> listarPorEstado(EstadoActivo estado);

    List<Activo> listarPorDepartamento(Long departamentoId);

    Activo cambiarEstado(Long activoId, EstadoActivo nuevoEstado);

    void eliminar(Long id);
}

