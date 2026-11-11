package org.example.assets.infraestructure.config;

import org.example.assets.application.ports.out.*;
import org.example.assets.application.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ActivoService activoService(
            ActivoRepositoryPort activoRepositoryPort,
            DepartamentoRepositoryPort departamentoRepositoryPort,
            CategoriaRepositoryPort categoriaRepositoryPort,
            NotificacionPort notificacionPort,
            TipoCambioPort tipoCambioPort) {
        return new ActivoService(
                activoRepositoryPort,
                departamentoRepositoryPort,
                categoriaRepositoryPort,
                notificacionPort,
                tipoCambioPort
        );
    }

    @Bean
    public AsignacionService asignacionService(
            AsignacionRepositoryPort asignacionRepositoryPort,
            ActivoRepositoryPort activoRepositoryPort,
            UsuarioOut usuarioOut,
            NotificacionPort notificacionPort) {
        return new AsignacionService(
                asignacionRepositoryPort,
                activoRepositoryPort,
                usuarioOut,
                notificacionPort
        );
    }

    @Bean
    public DepartamentoService departamentoService(DepartamentoRepositoryPort departamentoRepositoryPort) {
        return new DepartamentoService(departamentoRepositoryPort);
    }

    @Bean
    public CategoriaService categoriaService(CategoriaRepositoryPort categoriaRepositoryPort) {
        return new CategoriaService(categoriaRepositoryPort);
    }

    @Bean
    public UsuarioService usuarioService(UsuarioOut usuarioOut, PasswordOut passwordOut) {
        return new UsuarioService(usuarioOut, passwordOut);
    }
}

