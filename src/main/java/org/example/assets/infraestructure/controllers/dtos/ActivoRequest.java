package org.example.assets.infraestructure.controllers.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivoRequest {

    @NotBlank(message = "El codigo de inventario es obligatorio")
    private String codigoInventario;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String marca;
    private String modelo;

    @NotBlank(message = "El numero de serie es obligatorio")
    private String numeroSerie;

    @NotNull(message = "El costo de adquisicion es obligatorio")
    @DecimalMin(value = "0.01", message = "El costo de adquisicion debe ser mayor a 0")
    private BigDecimal costoAdquisicion;

    private String moneda; // Opcional, default USD

    @NotNull(message = "La fecha de adquisicion es obligatoria")
    private LocalDate fechaAdquisicion;

    private LocalDate finGarantia;

    @NotNull(message = "El id de la categoria es obligatorio")
    private Long categoriaId;

    @NotNull(message = "El id del departamento es obligatorio")
    private Long departamentoId;
}

