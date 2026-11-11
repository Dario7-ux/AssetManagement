package org.example.assets.infraestructure.mappers;

import org.example.assets.domain.models.*;
import org.example.assets.infraestructure.controllers.dtos.*;

import java.math.BigDecimal;

public class DtoMapper {

    public static UsuarioResponse toUsuarioResponse(Usuario usuario) {
        if (usuario == null) return null;
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .rol(usuario.getRol() != null ? usuario.getRol().name() : "USER")
                .activo(usuario.isActivo())
                .creado(usuario.getCreado())
                .build();
    }

    public static CategoriaResponse toCategoriaResponse(Categoria categoria) {
        if (categoria == null) return null;
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .codigo(categoria.getCodigo())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .vidaUtilAnios(categoria.getVidaUtilAnios())
                .tasaDepreciacionAnual(categoria.getTasaDepreciacionAnual())
                .creado(categoria.getCreado())
                .build();
    }

    public static DepartamentoResponse toDepartamentoResponse(Departamento departamento) {
        if (departamento == null) return null;
        BigDecimal disponible = departamento.getPresupuestoMaximo() != null && departamento.getValorActualActivos() != null
                ? departamento.getPresupuestoMaximo().subtract(departamento.getValorActualActivos())
                : BigDecimal.ZERO;
        if (disponible.compareTo(BigDecimal.ZERO) < 0) {
            disponible = BigDecimal.ZERO;
        }

        return DepartamentoResponse.builder()
                .id(departamento.getId())
                .codigo(departamento.getCodigo())
                .nombre(departamento.getNombre())
                .edificio(departamento.getEdificio())
                .piso(departamento.getPiso())
                .presupuestoMaximo(departamento.getPresupuestoMaximo())
                .valorActualActivos(departamento.getValorActualActivos())
                .presupuestoDisponible(disponible)
                .creado(departamento.getCreado())
                .build();
    }

    public static ActivoResponse toActivoResponse(Activo activo) {
        if (activo == null) return null;
        return ActivoResponse.builder()
                .id(activo.getId())
                .codigoInventario(activo.getCodigoInventario())
                .nombre(activo.getNombre())
                .marca(activo.getMarca())
                .modelo(activo.getModelo())
                .numeroSerie(activo.getNumeroSerie())
                .costoAdquisicion(activo.getCostoAdquisicion())
                .moneda(activo.getMoneda())
                .estado(activo.getEstado())
                .fechaAdquisicion(activo.getFechaAdquisicion())
                .finGarantia(activo.getFinGarantia())
                .categoria(toCategoriaResponse(activo.getCategoria()))
                .departamento(toDepartamentoResponse(activo.getDepartamento()))
                .creado(activo.getCreado())
                .build();
    }

    public static AsignacionResponse toAsignacionResponse(Asignacion asignacion) {
        if (asignacion == null) return null;
        return AsignacionResponse.builder()
                .id(asignacion.getId())
                .activo(toActivoResponse(asignacion.getActivo()))
                .usuario(toUsuarioResponse(asignacion.getUsuario()))
                .fechaAsignacion(asignacion.getFechaAsignacion())
                .fechaDevolucion(asignacion.getFechaDevolucion())
                .estadoAsignacion(asignacion.getEstadoAsignacion())
                .observaciones(asignacion.getObservaciones())
                .creado(asignacion.getCreado())
                .build();
    }
}

