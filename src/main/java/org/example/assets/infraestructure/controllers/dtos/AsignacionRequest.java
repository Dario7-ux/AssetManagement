package org.example.assets.infraestructure.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionRequest {

    @NotNull(message = "El id del activo es obligatorio")
    private Long activoId;

    @NotNull(message = "El id del usuario custodio es obligatorio")
    private Long usuarioId;

    private LocalDate fechaAsignacion; // Opcional, default fecha actual

    private String observaciones;
}

