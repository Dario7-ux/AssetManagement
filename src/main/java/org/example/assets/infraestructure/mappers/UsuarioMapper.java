package org.example.assets.infraestructure.mappers;

import org.example.assets.domain.models.Rol;
import org.example.assets.domain.models.Usuario;
import org.example.assets.infraestructure.entities.UsuarioEntity;

public class UsuarioMapper {

    public static Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }
        Rol rol = null;
        if (entity.getRol() != null) {
            try {
                rol = Rol.valueOf(entity.getRol().toUpperCase());
            } catch (IllegalArgumentException e) {
                rol = Rol.USER;
            }
        }
        return new Usuario(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                rol,
                Boolean.TRUE.equals(entity.getActivo()),
                entity.getCreado(),
                entity.getModificado()
        );
    }

    public static UsuarioEntity toEntity(Usuario domain) {
        if (domain == null) {
            return null;
        }
        return UsuarioEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .password(domain.getPassword())
                .rol(domain.getRol() != null ? domain.getRol().name() : "USER")
                .activo(domain.isActivo())
                .creado(domain.getCreado())
                .modificado(domain.getModificado())
                .build();
    }
}

