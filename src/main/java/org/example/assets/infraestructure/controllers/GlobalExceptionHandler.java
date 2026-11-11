package org.example.assets.infraestructure.controllers;

import org.example.assets.domain.exception.ActivoNoDisponibleException;
import org.example.assets.domain.exception.CupoDepartamentoExcedidoException;
import org.example.assets.domain.exception.DatoInvalidoException;
import org.example.assets.domain.exception.RecursoNoEncontradoException;
import org.example.assets.domain.exception.ReglaNegocioException;
import org.example.assets.infraestructure.controllers.dtos.ActionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ActionResponse<Void>> handleRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        log.warn("[404 NOT FOUND] {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ActionResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ActivoNoDisponibleException.class)
    public ResponseEntity<ActionResponse<Void>> handleActivoNoDisponible(ActivoNoDisponibleException ex) {
        log.warn("[REGLA NEGOCIO - ACTIVO NO DISPONIBLE] {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ActionResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(CupoDepartamentoExcedidoException.class)
    public ResponseEntity<ActionResponse<Void>> handleCupoExcedido(CupoDepartamentoExcedidoException ex) {
        log.warn("[REGLA NEGOCIO - CUPO DEPARTAMENTAL EXCEDIDO] {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ActionResponse.error(ex.getMessage()));
    }

    @ExceptionHandler({ReglaNegocioException.class, DatoInvalidoException.class})
    public ResponseEntity<ActionResponse<Void>> handleReglaNegocio(RuntimeException ex) {
        log.warn("[400 BAD REQUEST] {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ActionResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ActionResponse<Void>> handleValidacionCampos(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("[VALIDACION FALLIDA] {}", mensaje);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ActionResponse.error(mensaje));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ActionResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("[401 AUTH] Credenciales invalidas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ActionResponse.error("Credenciales invalidas: usuario o contrasena incorrectos"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ActionResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("[403 FORBIDDEN] Acceso denegado");
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ActionResponse.error("Acceso denegado: permisos insuficientes para esta operacion"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ActionResponse<Void>> handleGeneral(Exception ex) {
        log.error("[500 ERROR INTERNO] {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ActionResponse.error("Error interno del servidor: " + ex.getMessage()));
    }
}

