package org.example.assets.infraestructure.mappers;

import org.example.assets.domain.models.Departamento;
import org.example.assets.infraestructure.entities.DepartamentoEntity;

public class DepartamentoMapper {

    public static Departamento toDomain(DepartamentoEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Departamento(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getEdificio(),
                entity.getPiso(),
                entity.getPresupuestoMaximo(),
                entity.getValorActualActivos(),
                entity.getCreado(),
                entity.getModificado()
        );
    }

    public static DepartamentoEntity toEntity(Departamento domain) {
        if (domain == null) {
            return null;
        }
        return DepartamentoEntity.builder()
                .id(domain.getId())
                .codigo(domain.getCodigo())
                .nombre(domain.getNombre())
                .edificio(domain.getEdificio())
                .piso(domain.getPiso())
                .presupuestoMaximo(domain.getPresupuestoMaximo())
                .valorActualActivos(domain.getValorActualActivos())
                .creado(domain.getCreado())
                .modificado(domain.getModificado())
                .build();
    }
}

