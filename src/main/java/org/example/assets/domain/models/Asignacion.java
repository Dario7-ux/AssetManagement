package org.example.assets.domain.models;

import org.example.assets.domain.exception.DatoInvalidoException;
import org.example.assets.domain.exception.ReglaNegocioException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Asignacion {

    private Long id;
    private Activo activo;
    private Usuario usuario;
    private LocalDate fechaAsignacion;
    private LocalDate fechaDevolucion;
    private EstadoAsignacion estadoAsignacion;
    private String observaciones;
    private LocalDateTime creado;
    private LocalDateTime modificado;

    public Asignacion() {
    }

    public Asignacion(Long id, Activo activo, Usuario usuario, LocalDate fechaAsignacion,
                      LocalDate fechaDevolucion, EstadoAsignacion estadoAsignacion,
                      String observaciones, LocalDateTime creado, LocalDateTime modificado) {
        this.id = id;
        this.activo = activo;
        this.usuario = usuario;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaDevolucion = fechaDevolucion;
        this.estadoAsignacion = estadoAsignacion != null ? estadoAsignacion : EstadoAsignacion.ACTIVA;
        this.observaciones = observaciones;
        this.creado = creado;
        this.modificado = modificado;
    }

    public static Asignacion nueva(Activo activo, Usuario usuario, LocalDate fechaAsignacion, String observaciones) {
        if (activo == null) {
            throw new DatoInvalidoException("El activo a asignar es obligatorio");
        }
        if (usuario == null) {
            throw new DatoInvalidoException("El usuario custodio es obligatorio");
        }

        // Valida que el activo este disponible y lo marca como asignado
        activo.marcarComoAsignado();

        LocalDateTime ahora = LocalDateTime.now();
        LocalDate fecha = (fechaAsignacion != null) ? fechaAsignacion : LocalDate.now();

        return new Asignacion(
                null,
                activo,
                usuario,
                fecha,
                null,
                EstadoAsignacion.ACTIVA,
                observaciones,
                ahora,
                ahora
        );
    }

    /**
     * Regla de Negocio: Finaliza la asignacion, devuelve el activo y lo deja en estado DISPONIBLE.
     */
    public void registrarDevolucion(LocalDate fechaDevolucion, String observacionesDevolucion) {
        if (this.estadoAsignacion == EstadoAsignacion.DEVUELTO) {
            throw new ReglaNegocioException("Esta asignacion ya fue finalizada anteriormente");
        }
        this.fechaDevolucion = (fechaDevolucion != null) ? fechaDevolucion : LocalDate.now();
        this.estadoAsignacion = EstadoAsignacion.DEVUELTO;
        if (observacionesDevolucion != null && !observacionesDevolucion.isBlank()) {
            this.observaciones = (this.observaciones != null ? this.observaciones + " | " : "") + "Devolucion: " + observacionesDevolucion;
        }
        this.modificado = LocalDateTime.now();

        // Liberar el activo
        if (this.activo != null) {
            this.activo.liberarDeAsignacion();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activo getActivo() {
        return activo;
    }

    public void setActivo(Activo activo) {
        this.activo = activo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public EstadoAsignacion getEstadoAsignacion() {
        return estadoAsignacion;
    }

    public void setEstadoAsignacion(EstadoAsignacion estadoAsignacion) {
        this.estadoAsignacion = estadoAsignacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getCreado() {
        return creado;
    }

    public void setCreado(LocalDateTime creado) {
        this.creado = creado;
    }

    public LocalDateTime getModificado() {
        return modificado;
    }

    public void setModificado(LocalDateTime modificado) {
        this.modificado = modificado;
    }
}

