package org.example.assets.infraestructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "departamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 100)
    private String edificio;

    @Column(length = 50)
    private String piso;

    @Column(name = "presupuesto_maximo", precision = 14, scale = 2, nullable = false)
    private BigDecimal presupuestoMaximo;

    @Column(name = "valor_actual_activos", precision = 14, scale = 2, nullable = false)
    private BigDecimal valorActualActivos;

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
        if (this.presupuestoMaximo == null) {
            this.presupuestoMaximo = new BigDecimal("50000.00");
        }
        if (this.valorActualActivos == null) {
            this.valorActualActivos = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.modificado = LocalDateTime.now();
    }
}

