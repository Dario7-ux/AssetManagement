package org.example.assets.application.ports.in;

import org.example.assets.domain.models.Asignacion;

import java.time.LocalDate;

public interface AsignarActivoUseCase {

    Asignacion asignar(Long activoId, Long usuarioId, LocalDate fechaAsignacion, String observaciones);

    Asignacion devolver(Long asignacionId, LocalDate fechaDevolucion, String observacionesDevolucion);
}

