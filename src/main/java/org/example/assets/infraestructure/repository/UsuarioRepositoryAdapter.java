package org.example.assets.infraestructure.repository;

import org.example.assets.application.ports.out.UsuarioOut;
import org.example.assets.domain.models.Usuario;
import org.example.assets.infraestructure.entities.UsuarioEntity;
import org.example.assets.infraestructure.mappers.UsuarioMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioOut {

    private final UsuarioJpaRepository usuarioJpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
        UsuarioEntity guardado = usuarioJpaRepository.save(entity);
        return UsuarioMapper.toDomain(guardado);
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioJpaRepository.findByUsername(username)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioJpaRepository.findById(id)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioJpaRepository.findAll().stream()
                .map(UsuarioMapper::toDomain)
                .toList();
    }
}

