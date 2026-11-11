package org.example.assets.application.ports.in;

import org.example.assets.domain.models.Activo;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RegistrarActivoUseCase {

    Activo registrar(Activo activo);

    Activo registrar(
            String codigoInventario,
            String nombre,
            String marca,
            String modelo,
            String numeroSerie,
            BigDecimal costo,
            String moneda,
            LocalDate fechaAdquisicion,
            LocalDate finGarantia,
            Long categoriaId,
            Long departamentoId
    );
}

