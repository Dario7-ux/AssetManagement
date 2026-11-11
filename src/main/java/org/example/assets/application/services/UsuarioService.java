package org.example.assets.application.services;

import org.example.assets.application.ports.in.UsuarioIn;
import org.example.assets.application.ports.out.PasswordOut;
import org.example.assets.application.ports.out.UsuarioOut;
import org.example.assets.domain.exception.DatoInvalidoException;
import org.example.assets.domain.exception.RecursoNoEncontradoException;
import org.example.assets.domain.exception.ReglaNegocioException;
import org.example.assets.domain.models.Usuario;

import java.util.List;

public class UsuarioService implements UsuarioIn {

    private final UsuarioOut usuarioOut;
    private final PasswordOut passwordOut;

    public UsuarioService(UsuarioOut usuarioOut, PasswordOut passwordOut) {
        this.usuarioOut = usuarioOut;
        this.passwordOut = passwordOut;
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        if (usuario == null) {
            throw new DatoInvalidoException("El usuario no puede ser nulo");
        }
        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            throw new DatoInvalidoException("El username es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new DatoInvalidoException("La contrasena es obligatoria");
        }

        usuarioOut.buscarPorUsername(usuario.getUsername().trim()).ifPresent(u -> {
            throw new ReglaNegocioException("El nombre de usuario '" + usuario.getUsername() + "' ya se encuentra registrado");
        });

        // Cifrado de password con el puerto de salida
        usuario.setPassword(passwordOut.encode(usuario.getPassword()));

        return usuarioOut.guardar(usuario);
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new DatoInvalidoException("El username es obligatorio");
        }
        return usuarioOut.buscarPorUsername(username.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario '" + username + "' no encontrado"));
    }

    @Override
    public Usuario buscarPorId(Long id) {
        if (id == null) {
            throw new DatoInvalidoException("El id del usuario es obligatorio");
        }
        return usuarioOut.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con id " + id + " no encontrado"));
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioOut.listarTodos();
    }
}

