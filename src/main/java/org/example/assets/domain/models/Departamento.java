package org.example.assets.domain.models;

import org.example.assets.domain.exception.CupoDepartamentoExcedidoException;
import org.example.assets.domain.exception.DatoInvalidoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Departamento {

    private Long id;
    private String codigo;
    private String nombre;
    private String edificio;
    private String piso;
    private BigDecimal presupuestoMaximo;
    private BigDecimal valorActualActivos;
    private LocalDateTime creado;
    private LocalDateTime modificado;

    public Departamento() {
    }

    public Departamento(Long id, String codigo, String nombre, String edificio, String piso,
                        BigDecimal presupuestoMaximo, BigDecimal valorActualActivos,
                        LocalDateTime creado, LocalDateTime modificado) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.edificio = edificio;
        this.piso = piso;
        this.presupuestoMaximo = presupuestoMaximo != null ? presupuestoMaximo : BigDecimal.ZERO;
        this.valorActualActivos = valorActualActivos != null ? valorActualActivos : BigDecimal.ZERO;
        this.creado = creado;
        this.modificado = modificado;
    }

    public static Departamento nuevo(String codigo, String nombre, String edificio, String piso, BigDecimal presupuestoMaximo) {
        LocalDateTime ahora = LocalDateTime.now();
        BigDecimal limite = (presupuestoMaximo != null && presupuestoMaximo.compareTo(BigDecimal.ZERO) > 0)
                ? presupuestoMaximo
                : new BigDecimal("50000.00");
        return new Departamento(null, codigo, nombre, edificio, piso, limite, BigDecimal.ZERO, ahora, ahora);
    }

    /**
     * Regla de Negocio: Evalúa si agregar un costo adicional excede el presupuesto máximo del departamento.
     */
    public void validarCupoDisponible(BigDecimal costoAdicional) {
        if (costoAdicional == null || costoAdicional.compareTo(BigDecimal.ZERO) < 0) {
            throw new DatoInvalidoException("El costo adicional a evaluar no puede ser negativo");
        }
        BigDecimal nuevoTotal = this.valorActualActivos.add(costoAdicional);
        if (nuevoTotal.compareTo(this.presupuestoMaximo) > 0) {
            throw new CupoDepartamentoExcedidoException(
                    "El departamento '" + this.nombre + "' ha excedido su presupuesto maximo de activos. Limite: $"
                            + this.presupuestoMaximo + ", Total con nuevo activo: $" + nuevoTotal
            );
        }
    }

    public void acumularValorActivo(BigDecimal costo) {
        if (costo != null && costo.compareTo(BigDecimal.ZERO) > 0) {
            this.valorActualActivos = this.valorActualActivos.add(costo);
            this.modificado = LocalDateTime.now();
        }
    }

    public void restarValorActivo(BigDecimal costo) {
        if (costo != null && costo.compareTo(BigDecimal.ZERO) > 0) {
            this.valorActualActivos = this.valorActualActivos.subtract(costo);
            if (this.valorActualActivos.compareTo(BigDecimal.ZERO) < 0) {
                this.valorActualActivos = BigDecimal.ZERO;
            }
            this.modificado = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public BigDecimal getPresupuestoMaximo() {
        return presupuestoMaximo;
    }

    public void setPresupuestoMaximo(BigDecimal presupuestoMaximo) {
        this.presupuestoMaximo = presupuestoMaximo;
    }

    public BigDecimal getValorActualActivos() {
        return valorActualActivos;
    }

    public void setValorActualActivos(BigDecimal valorActualActivos) {
        this.valorActualActivos = valorActualActivos;
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

