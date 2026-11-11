package org.example.assets.application.ports.in;

import org.example.assets.domain.models.Usuario;

import java.util.List;

public interface UsuarioIn {

    Usuario registrar(Usuario usuario);

    Usuario buscarPorUsername(String username);

    Usuario buscarPorId(Long id);

    List<Usuario> listarTodos();
}

