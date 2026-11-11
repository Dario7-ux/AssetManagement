package org.example.assets.infraestructure.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    private int totalOperaciones = 0;
    private int totalActivosRegistrados = 0;
    private int totalAsignacionesRealizadas = 0;

    @Around("execution(* org.example.assets.application.services..*(..))")
    public Object auditarYMedirTiempo(ProceedingJoinPoint joinPoint) throws Throwable {
        long inicio = System.currentTimeMillis();
        String clase = joinPoint.getTarget().getClass().getSimpleName();
        String metodo = joinPoint.getSignature().getName();

        totalOperaciones++;
        if (metodo.startsWith("registrar") && clase.contains("Activo")) {
            totalActivosRegistrados++;
        } else if (metodo.startsWith("asignar") || metodo.startsWith("devolver")) {
            totalAsignacionesRealizadas++;
        }

        log.info("[AUDITORIA ASSET-MANAGEMENT] -> Ejecutando: {}.{}() | Total Operaciones: {} | Activos Registrados: {} | Asignaciones: {}",
                clase, metodo, totalOperaciones, totalActivosRegistrados, totalAsignacionesRealizadas);

        try {
            Object resultado = joinPoint.proceed();
            long duracion = System.currentTimeMillis() - inicio;
            log.info("[TIEMPO RESPUESTA] -> {}.{}() completado exitosamente en {} ms",
                    clase, metodo, duracion);
            return resultado;
        } catch (Throwable ex) {
            long duracion = System.currentTimeMillis() - inicio;
            log.error("[ERROR AUDITORIA] -> {}.{}() fallo tras {} ms con excepcion: {} - {}",
                    clase, metodo, duracion, ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
    }
}

