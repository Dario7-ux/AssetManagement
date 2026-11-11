package org.example.assets.infraestructure.mappers;

import org.example.assets.domain.models.Activo;
import org.example.assets.infraestructure.entities.ActivoEntity;

public class ActivoMapper {

    public static Activo toDomain(ActivoEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Activo(
                entity.getId(),
                entity.getCodigoInventario(),
                entity.getNombre(),
                entity.getMarca(),
                entity.getModelo(),
                entity.getNumeroSerie(),
                entity.getCostoAdquisicion(),
                entity.getMoneda(),
                entity.getEstado(),
                entity.getFechaAdquisicion(),
                entity.getFinGarantia(),
                CategoriaMapper.toDomain(entity.getCategoria()),
                DepartamentoMapper.toDomain(entity.getDepartamento()),
                entity.getCreado(),
                entity.getModificado()
        );
    }

    public static ActivoEntity toEntity(Activo domain) {
        if (domain == null) {
            return null;
        }
        return ActivoEntity.builder()
                .id(domain.getId())
                .codigoInventario(domain.getCodigoInventario())
                .nombre(domain.getNombre())
                .marca(domain.getMarca())
                .modelo(domain.getModelo())
                .numeroSerie(domain.getNumeroSerie())
                .costoAdquisicion(domain.getCostoAdquisicion())
                .moneda(domain.getMoneda())
                .estado(domain.getEstado())
                .fechaAdquisicion(domain.getFechaAdquisicion())
                .finGarantia(domain.getFinGarantia())
                .categoria(CategoriaMapper.toEntity(domain.getCategoria()))
                .departamento(DepartamentoMapper.toEntity(domain.getDepartamento()))
                .creado(domain.getCreado())
                .modificado(domain.getModificado())
                .build();
    }
}

