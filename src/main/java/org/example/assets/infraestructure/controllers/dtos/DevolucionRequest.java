package org.example.assets.infraestructure.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionRequest {

    private LocalDate fechaDevolucion; // Opcional, default fecha actual

    private String observaciones;
}

