package org.example.assets.infraestructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.assets.domain.models.EstadoAsignacion;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activo_id", nullable = false)
    private ActivoEntity activo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_asignacion", nullable = false, length = 30)
    private EstadoAsignacion estadoAsignacion;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

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
        if (this.estadoAsignacion == null) {
            this.estadoAsignacion = EstadoAsignacion.ACTIVA;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.modificado = LocalDateTime.now();
    }
}

