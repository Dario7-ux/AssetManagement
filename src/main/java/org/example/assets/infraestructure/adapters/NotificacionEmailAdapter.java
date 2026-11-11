package org.example.assets.infraestructure.adapters;

import org.example.assets.application.ports.out.NotificacionPort;
import org.example.assets.domain.models.Activo;
import org.example.assets.domain.models.Asignacion;
import org.example.assets.domain.models.Departamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class NotificacionEmailAdapter implements NotificacionPort {

    private static final Logger log = LoggerFactory.getLogger(NotificacionEmailAdapter.class);

    @Value("${assetmanagement.notificaciones.destinatario:alertas@assetmanagement.com}")
    private String emailDestinatario;

    @Override
    public void enviarAlertaCupoExcedido(Departamento departamento, BigDecimal costoActivo) {
        log.error("=== [NOTIFICACIÓN ALERTA CRÍTICA: CUPO DEPARTAMENTAL EXCEDIDO] ===");
        log.error("Destinatario: {}", emailDestinatario);
        log.error("Departamento: '{}' ({})", departamento.getNombre(), departamento.getCodigo());
        log.error("Costo del activo intentado: ${}", costoActivo);
        log.error("Límite de presupuesto: ${} | Valor actual acumulado: ${}",
                departamento.getPresupuestoMaximo(), departamento.getValorActualActivos());
        log.error("===================================================================");
    }

    @Override
    public void enviarNotificacionAsignacion(Asignacion asignacion) {
        log.info("=== [NOTIFICACIÓN ACTA DE ENTREGA / ASIGNACIÓN DE ACTIVO] ===");
        log.info("Destinatario: {}", emailDestinatario);
        log.info("Activo: '{}' | Código: {} | Serie: {}",
                asignacion.getActivo().getNombre(),
                asignacion.getActivo().getCodigoInventario(),
                asignacion.getActivo().getNumeroSerie());
        log.info("Custodio Asignado: {}", asignacion.getUsuario().getUsername());
        log.info("Fecha de Asignación: {}", asignacion.getFechaAsignacion());
        log.info("Observaciones: {}", asignacion.getObservaciones() != null ? asignacion.getObservaciones() : "N/A");
        log.info("=============================================================");
    }

    @Override
    public void enviarNotificacionDevolucion(Asignacion asignacion) {
        log.info("=== [NOTIFICACIÓN DEVOLUCIÓN DE ACTIVO A INVENTARIO] ===");
        log.info("Destinatario: {}", emailDestinatario);
        log.info("Activo: '{}' ({})", asignacion.getActivo().getNombre(), asignacion.getActivo().getCodigoInventario());
        log.info("Devuelto por: {}", asignacion.getUsuario().getUsername());
        log.info("Fecha de Devolución: {}", asignacion.getFechaDevolucion());
        log.info("Estado del Activo: {}", asignacion.getActivo().getEstado());
        log.info("========================================================");
    }

    @Override
    public void enviarAlertaGarantiaVencida(Activo activo) {
        log.warn("=== [NOTIFICACIÓN ALERTA: GARANTÍA DE ACTIVO VENCIDA/PRÓXIMA] ===");
        log.warn("Destinatario: {}", emailDestinatario);
        log.warn("Activo: '{}' | Código: {}", activo.getNombre(), activo.getCodigoInventario());
        log.warn("Fecha Fin Garantía: {}", activo.getFinGarantia());
        log.warn("Departamento: {}", activo.getDepartamento() != null ? activo.getDepartamento().getNombre() : "N/A");
        log.warn("=================================================================");
    }
}

