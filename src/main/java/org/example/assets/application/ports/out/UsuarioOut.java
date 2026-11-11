package org.example.assets.application.ports.out;

import org.example.assets.domain.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioOut {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorUsername(String username);

    Optional<Usuario> buscarPorId(Long id);

    List<Usuario> listarTodos();
}

