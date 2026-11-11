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
public class CategoriaResponse {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer vidaUtilAnios;
    private BigDecimal tasaDepreciacionAnual;
    private LocalDateTime creado;
}

