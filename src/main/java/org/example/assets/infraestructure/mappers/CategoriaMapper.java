package org.example.assets.infraestructure.mappers;

import org.example.assets.domain.models.Categoria;
import org.example.assets.infraestructure.entities.CategoriaEntity;

public class CategoriaMapper {

    public static Categoria toDomain(CategoriaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Categoria(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getVidaUtilAnios(),
                entity.getTasaDepreciacionAnual(),
                entity.getCreado(),
                entity.getModificado()
        );
    }

    public static CategoriaEntity toEntity(Categoria domain) {
        if (domain == null) {
            return null;
        }
        return CategoriaEntity.builder()
                .id(domain.getId())
                .codigo(domain.getCodigo())
                .nombre(domain.getNombre())
                .descripcion(domain.getDescripcion())
                .vidaUtilAnios(domain.getVidaUtilAnios())
                .tasaDepreciacionAnual(domain.getTasaDepreciacionAnual())
                .creado(domain.getCreado())
                .modificado(domain.getModificado())
                .build();
    }
}

