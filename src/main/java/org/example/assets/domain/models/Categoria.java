package org.example.assets.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Categoria {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer vidaUtilAnios;
    private BigDecimal tasaDepreciacionAnual;
    private LocalDateTime creado;
    private LocalDateTime modificado;

    public Categoria() {
    }

    public Categoria(Long id, String codigo, String nombre, String descripcion, Integer vidaUtilAnios, BigDecimal tasaDepreciacionAnual, LocalDateTime creado, LocalDateTime modificado) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.vidaUtilAnios = vidaUtilAnios;
        this.tasaDepreciacionAnual = tasaDepreciacionAnual;
        this.creado = creado;
        this.modificado = modificado;
    }

    public static Categoria nueva(String codigo, String nombre, String descripcion, Integer vidaUtilAnios, BigDecimal tasaDepreciacionAnual) {
        LocalDateTime ahora = LocalDateTime.now();
        return new Categoria(null, codigo, nombre, descripcion, vidaUtilAnios != null ? vidaUtilAnios : 5, tasaDepreciacionAnual != null ? tasaDepreciacionAnual : new BigDecimal("20.00"), ahora, ahora);
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getVidaUtilAnios() {
        return vidaUtilAnios;
    }

    public void setVidaUtilAnios(Integer vidaUtilAnios) {
        this.vidaUtilAnios = vidaUtilAnios;
    }

    public BigDecimal getTasaDepreciacionAnual() {
        return tasaDepreciacionAnual;
    }

    public void setTasaDepreciacionAnual(BigDecimal tasaDepreciacionAnual) {
        this.tasaDepreciacionAnual = tasaDepreciacionAnual;
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

