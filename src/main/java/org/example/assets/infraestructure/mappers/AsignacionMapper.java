package org.example.assets.infraestructure.mappers;

import org.example.assets.domain.models.Asignacion;
import org.example.assets.infraestructure.entities.AsignacionEntity;

public class AsignacionMapper {

    public static Asignacion toDomain(AsignacionEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Asignacion(
                entity.getId(),
                ActivoMapper.toDomain(entity.getActivo()),
                UsuarioMapper.toDomain(entity.getUsuario()),
                entity.getFechaAsignacion(),
                entity.getFechaDevolucion(),
                entity.getEstadoAsignacion(),
                entity.getObservaciones(),
                entity.getCreado(),
                entity.getModificado()
        );
    }

    public static AsignacionEntity toEntity(Asignacion domain) {
        if (domain == null) {
            return null;
        }
        return AsignacionEntity.builder()
                .id(domain.getId())
                .activo(ActivoMapper.toEntity(domain.getActivo()))
                .usuario(UsuarioMapper.toEntity(domain.getUsuario()))
                .fechaAsignacion(domain.getFechaAsignacion())
                .fechaDevolucion(domain.getFechaDevolucion())
                .estadoAsignacion(domain.getEstadoAsignacion())
                .observaciones(domain.getObservaciones())
                .creado(domain.getCreado())
                .modificado(domain.getModificado())
                .build();
    }
}

