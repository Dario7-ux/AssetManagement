package org.example.assets.infraestructure.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.assets.domain.models.EstadoActivo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivoResponse {

    private Long id;
    private String codigoInventario;
    private String nombre;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private BigDecimal costoAdquisicion;
    private String moneda;
    private EstadoActivo estado;
    private LocalDate fechaAdquisicion;
    private LocalDate finGarantia;
    private CategoriaResponse categoria;
    private DepartamentoResponse departamento;
    private LocalDateTime creado;
}

