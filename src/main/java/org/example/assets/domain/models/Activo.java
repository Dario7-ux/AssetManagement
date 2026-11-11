package org.example.assets.domain.models;

import org.example.assets.domain.exception.ActivoNoDisponibleException;
import org.example.assets.domain.exception.DatoInvalidoException;
import org.example.assets.domain.exception.ReglaNegocioException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Activo {

    private Long id;
    private String codigoInventario;
    private String nombre;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private BigDecimal costoAdquisicion;
    private String moneda;
    private EstadoActivo estado;
    private LocalDate fechaAdquisicion;
    private LocalDate finGarantia;
    private Categoria categoria;
    private Departamento departamento;
    private LocalDateTime creado;
    private LocalDateTime modificado;

    public Activo() {
    }

    public Activo(Long id, String codigoInventario, String nombre, String marca, String modelo,
                  String numeroSerie, BigDecimal costoAdquisicion, String moneda,
                  EstadoActivo estado, LocalDate fechaAdquisicion, LocalDate finGarantia,
                  Categoria categoria, Departamento departamento,
                  LocalDateTime creado, LocalDateTime modificado) {
        this.id = id;
        this.codigoInventario = codigoInventario;
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.costoAdquisicion = costoAdquisicion;
        this.moneda = moneda != null ? moneda.toUpperCase() : "USD";
        this.estado = estado != null ? estado : EstadoActivo.DISPONIBLE;
        this.fechaAdquisicion = fechaAdquisicion;
        this.finGarantia = finGarantia;
        this.categoria = categoria;
        this.departamento = departamento;
        this.creado = creado;
        this.modificado = modificado;
    }

    public static Activo nuevo(String codigoInventario, String nombre, String marca, String modelo,
                               String numeroSerie, BigDecimal costoAdquisicion, String moneda,
                               LocalDate fechaAdquisicion, LocalDate finGarantia,
                               Categoria categoria, Departamento departamento) {
        if (codigoInventario == null || codigoInventario.isBlank()) {
            throw new DatoInvalidoException("El codigo de inventario es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new DatoInvalidoException("El nombre del activo es obligatorio");
        }
        if (numeroSerie == null || numeroSerie.isBlank()) {
            throw new DatoInvalidoException("El numero de serie es obligatorio");
        }
        if (costoAdquisicion == null || costoAdquisicion.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DatoInvalidoException("El costo de adquisicion debe ser mayor a cero");
        }
        if (fechaAdquisicion == null) {
            throw new DatoInvalidoException("La fecha de adquisicion es obligatoria");
        }
        if (categoria == null) {
            throw new DatoInvalidoException("El activo debe pertenecer a una categoria valida");
        }
        if (departamento == null) {
            throw new DatoInvalidoException("El activo debe pertenecer a un departamento valido");
        }

        LocalDateTime ahora = LocalDateTime.now();
        return new Activo(
                null,
                codigoInventario.trim(),
                nombre.trim(),
                marca != null ? marca.trim() : null,
                modelo != null ? modelo.trim() : null,
                numeroSerie.trim(),
                costoAdquisicion,
                moneda != null && !moneda.isBlank() ? moneda.trim().toUpperCase() : "USD",
                EstadoActivo.DISPONIBLE,
                fechaAdquisicion,
                finGarantia,
                categoria,
                departamento,
                ahora,
                ahora
        );
    }

    /**
     * Regla de Negocio: Valida que el activo este en estado DISPONIBLE para ser asignado.
     */
    public void validarDisponibleParaAsignacion() {
        if (this.estado != EstadoActivo.DISPONIBLE) {
            throw new ActivoNoDisponibleException(
                    "El activo '" + this.nombre + "' (" + this.codigoInventario + ") no se puede asignar. Estado actual: " + this.estado
            );
        }
    }

    public void marcarComoAsignado() {
        validarDisponibleParaAsignacion();
        this.estado = EstadoActivo.ASIGNADO;
        this.modificado = LocalDateTime.now();
    }

    public void liberarDeAsignacion() {
        if (this.estado == EstadoActivo.DE_BAJA) {
            throw new ReglaNegocioException("No se puede reactivar un activo que ha sido dado de baja");
        }
        this.estado = EstadoActivo.DISPONIBLE;
        this.modificado = LocalDateTime.now();
    }

    public void pasarAMantenimiento() {
        if (this.estado == EstadoActivo.DE_BAJA) {
            throw new ReglaNegocioException("Un activo dado de baja no puede ser enviado a mantenimiento");
        }
        this.estado = EstadoActivo.EN_MANTENIMIENTO;
        this.modificado = LocalDateTime.now();
    }

    public void darDeBaja() {
        this.estado = EstadoActivo.DE_BAJA;
        this.modificado = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoInventario() {
        return codigoInventario;
    }

    public void setCodigoInventario(String codigoInventario) {
        this.codigoInventario = codigoInventario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public BigDecimal getCostoAdquisicion() {
        return costoAdquisicion;
    }

    public void setCostoAdquisicion(BigDecimal costoAdquisicion) {
        this.costoAdquisicion = costoAdquisicion;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public EstadoActivo getEstado() {
        return estado;
    }

    public void setEstado(EstadoActivo estado) {
        this.estado = estado;
    }

    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public LocalDate getFinGarantia() {
        return finGarantia;
    }

    public void setFinGarantia(LocalDate finGarantia) {
        this.finGarantia = finGarantia;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
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

