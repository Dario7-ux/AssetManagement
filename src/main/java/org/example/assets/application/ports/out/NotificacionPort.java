package org.example.assets.application.ports.out;

import org.example.assets.domain.models.Activo;
import org.example.assets.domain.models.Asignacion;
import org.example.assets.domain.models.Departamento;

import java.math.BigDecimal;

public interface NotificacionPort {

    void enviarAlertaCupoExcedido(Departamento departamento, BigDecimal costoActivo);

    void enviarNotificacionAsignacion(Asignacion asignacion);

    void enviarNotificacionDevolucion(Asignacion asignacion);

    void enviarAlertaGarantiaVencida(Activo activo);
}

