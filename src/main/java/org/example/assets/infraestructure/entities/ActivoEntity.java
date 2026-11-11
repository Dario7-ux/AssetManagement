package org.example.assets.infraestructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.assets.domain.models.EstadoActivo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "activos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_inventario", nullable = false, unique = true, length = 50)
    private String codigoInventario;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 100)
    private String marca;

    @Column(length = 100)
    private String modelo;

    @Column(name = "numero_serie", nullable = false, unique = true, length = 100)
    private String numeroSerie;

    @Column(name = "costo_adquisicion", precision = 14, scale = 2, nullable = false)
    private BigDecimal costoAdquisicion;

    @Column(nullable = false, length = 10)
    private String moneda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoActivo estado;

    @Column(name = "fecha_adquisicion", nullable = false)
    private LocalDate fechaAdquisicion;

    @Column(name = "fin_garantia")
    private LocalDate finGarantia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaEntity categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departamento_id", nullable = false)
    private DepartamentoEntity departamento;

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
        if (this.estado == null) {
            this.estado = EstadoActivo.DISPONIBLE;
        }
        if (this.moneda == null) {
            this.moneda = "USD";
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.modificado = LocalDateTime.now();
    }
}

