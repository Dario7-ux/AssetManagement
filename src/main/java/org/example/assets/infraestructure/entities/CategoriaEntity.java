package org.example.assets.infraestructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "vida_util_anios", nullable = false)
    private Integer vidaUtilAnios;

    @Column(name = "tasa_depreciacion_anual", precision = 5, scale = 2, nullable = false)
    private BigDecimal tasaDepreciacionAnual;

    @Column(name = "creado", updatable = false)
    private LocalDateTime creado;

    @Column(name = "modificado")
    private LocalDateTime modificado;

    @PrePersist
    public void prePersist() {
        if (this.creado == null) {
            this.creado = LocalDateTime.now();
        }
        if (this.modificado == null) {
            this.modificado = LocalDateTime.now();
        }
        if (this.vidaUtilAnios == null) {
            this.vidaUtilAnios = 5;
        }
        if (this.tasaDepreciacionAnual == null) {
            this.tasaDepreciacionAnual = new BigDecimal("20.00");
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.modificado = LocalDateTime.now();
    }
}

