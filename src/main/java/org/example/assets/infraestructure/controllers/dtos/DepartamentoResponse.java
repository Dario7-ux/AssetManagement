package org.example.assets.infraestructure.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoResponse {

    private Long id;
    private String codigo;
    private String nombre;
    private String edificio;
    private String piso;
    private BigDecimal presupuestoMaximo;
    private BigDecimal valorActualActivos;
    private BigDecimal presupuestoDisponible;
    private LocalDateTime creado;
}

