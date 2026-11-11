package org.example.assets.infraestructure.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.assets.domain.models.EstadoAsignacion;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionResponse {

    private Long id;
    private ActivoResponse activo;
    private UsuarioResponse usuario;
    private LocalDate fechaAsignacion;
    private LocalDate fechaDevolucion;
    private EstadoAsignacion estadoAsignacion;
    private String observaciones;
    private LocalDateTime creado;
}

