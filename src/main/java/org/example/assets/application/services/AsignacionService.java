package org.example.assets.application.services;

import org.example.assets.application.ports.in.AsignarActivoUseCase;
import org.example.assets.application.ports.out.ActivoRepositoryPort;
import org.example.assets.application.ports.out.AsignacionRepositoryPort;
import org.example.assets.application.ports.out.NotificacionPort;
import org.example.assets.application.ports.out.UsuarioOut;
import org.example.assets.domain.exception.DatoInvalidoException;
import org.example.assets.domain.exception.RecursoNoEncontradoException;
import org.example.assets.domain.exception.ReglaNegocioException;
import org.example.assets.domain.models.Activo;
import org.example.assets.domain.models.Asignacion;
import org.example.assets.domain.models.Usuario;

import java.time.LocalDate;
import java.util.List;

public class AsignacionService implements AsignarActivoUseCase {

    private final AsignacionRepositoryPort asignacionRepositoryPort;
    private final ActivoRepositoryPort activoRepositoryPort;
    private final UsuarioOut usuarioOut;
    private final NotificacionPort notificacionPort;

    public AsignacionService(
            AsignacionRepositoryPort asignacionRepositoryPort,
            ActivoRepositoryPort activoRepositoryPort,
            UsuarioOut usuarioOut,
            NotificacionPort notificacionPort) {
        this.asignacionRepositoryPort = asignacionRepositoryPort;
        this.activoRepositoryPort = activoRepositoryPort;
        this.usuarioOut = usuarioOut;
        this.notificacionPort = notificacionPort;
    }

    public AsignacionService(
            AsignacionRepositoryPort asignacionRepositoryPort,
            ActivoRepositoryPort activoRepositoryPort,
            UsuarioOut usuarioOut) {
        this(asignacionRepositoryPort, activoRepositoryPort, usuarioOut, null);
    }

    @Override
    public Asignacion asignar(Long activoId, Long usuarioId, LocalDate fechaAsignacion, String observaciones) {
        if (activoId == null) {
            throw new DatoInvalidoException("El id del activo es obligatorio");
        }
        if (usuarioId == null) {
            throw new DatoInvalidoException("El id del usuario es obligatorio");
        }

        Activo activo = activoRepositoryPort.buscarPorId(activoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Activo con id " + activoId + " no encontrado"));

        Usuario usuario = usuarioOut.buscarPorId(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con id " + usuarioId + " no encontrado"));

        if (!usuario.isActivo()) {
            throw new ReglaNegocioException("No se puede asignar un activo a un usuario inactivo");
        }

        asignacionRepositoryPort.buscarActivaPorActivoId(activoId).ifPresent(asig -> {
            throw new ReglaNegocioException("El activo ya cuenta con una asignacion activa (ID: " + asig.getId() + ")");
        });

        // Crear asignacion (el modelo valida estado DISPONIBLE y cambia a ASIGNADO)
        Asignacion nuevaAsignacion = Asignacion.nueva(activo, usuario, fechaAsignacion, observaciones);

        // Guardar estado actualizado del activo y la nueva asignacion
        activoRepositoryPort.guardar(activo);
        Asignacion guardada = asignacionRepositoryPort.guardar(nuevaAsignacion);

        if (notificacionPort != null) {
            notificacionPort.enviarNotificacionAsignacion(guardada);
        }

        return guardada;
    }

    @Override
    public Asignacion devolver(Long asignacionId, LocalDate fechaDevolucion, String observacionesDevolucion) {
        if (asignacionId == null) {
            throw new DatoInvalidoException("El id de la asignacion es obligatorio");
        }

        Asignacion asignacion = asignacionRepositoryPort.buscarPorId(asignacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignacion con id " + asignacionId + " no encontrada"));

        // Registrar devolucion (el modelo cambia estado a DEVUELTO y libera el activo a DISPONIBLE)
        asignacion.registrarDevolucion(fechaDevolucion, observacionesDevolucion);

        // Persistir cambios
        if (asignacion.getActivo() != null) {
            activoRepositoryPort.guardar(asignacion.getActivo());
        }
        Asignacion actualizada = asignacionRepositoryPort.guardar(asignacion);

        if (notificacionPort != null) {
            notificacionPort.enviarNotificacionDevolucion(actualizada);
        }

        return actualizada;
    }

    public List<Asignacion> listarTodas() {
        return asignacionRepositoryPort.listarTodas();
    }

    public List<Asignacion> listarPorActivo(Long activoId) {
        if (activoId == null) {
            throw new DatoInvalidoException("El id del activo es obligatorio");
        }
        return asignacionRepositoryPort.listarPorActivoId(activoId);
    }

    public List<Asignacion> listarPorUsuario(Long usuarioId) {
        if (usuarioId == null) {
            throw new DatoInvalidoException("El id del usuario es obligatorio");
        }
        return asignacionRepositoryPort.listarPorUsuarioId(usuarioId);
    }
}

