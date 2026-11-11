package org.example.assets.infraestructure.controllers.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequest {

    @NotBlank(message = "El codigo es obligatorio")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @Min(value = 1, message = "La vida util debe ser de al menos 1 anio")
    private Integer vidaUtilAnios;

    @DecimalMin(value = "0.01", message = "La tasa de depreciacion debe ser mayor a 0")
    private BigDecimal tasaDepreciacionAnual;
}

