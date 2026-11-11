package org.example.assets.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.assets.application.ports.in.AsignarActivoUseCase;
import org.example.assets.application.services.AsignacionService;
import org.example.assets.domain.models.Asignacion;
import org.example.assets.infraestructure.controllers.dtos.ActionResponse;
import org.example.assets.infraestructure.controllers.dtos.AsignacionRequest;
import org.example.assets.infraestructure.controllers.dtos.AsignacionResponse;
import org.example.assets.infraestructure.controllers.dtos.DevolucionRequest;
import org.example.assets.infraestructure.mappers.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asignaciones")
@Tag(name = "Asignaciones y Custodia", description = "Control de prestamos, entrega y devolucion de activos a usuarios custodios")
@SecurityRequirement(name = "BearerAuth")
public class AsignacionController {

    private final AsignarActivoUseCase asignarActivoUseCase;
    private final AsignacionService asignacionService;

    public AsignacionController(AsignarActivoUseCase asignarActivoUseCase, AsignacionService asignacionService) {
        this.asignarActivoUseCase = asignarActivoUseCase;
        this.asignacionService = asignacionService;
    }

    @PostMapping
    @Operation(summary = "Asignar activo a custodio", description = "Entrega un activo a un usuario custodio. Valida que el activo este DISPONIBLE (Requiere ADMIN)")
    public ResponseEntity<ActionResponse<AsignacionResponse>> asignar(@Valid @RequestBody AsignacionRequest request) {
        Asignacion asignacion = asignarActivoUseCase.asignar(
                request.getActivoId(),
                request.getUsuarioId(),
                request.getFechaAsignacion(),
                request.getObservaciones()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ActionResponse.ok("Activo asignado exitosamente al custodio", DtoMapper.toAsignacionResponse(asignacion)));
    }

    @PutMapping("/{id}/devolver")
    @Operation(summary = "Registrar devolucion de activo", description = "Finaliza la asignacion y regresa el activo a estado DISPONIBLE (Requiere ADMIN)")
    public ResponseEntity<ActionResponse<AsignacionResponse>> devolver(
            @PathVariable Long id,
            @RequestBody(required = false) DevolucionRequest request) {

        DevolucionRequest req = (request != null) ? request : new DevolucionRequest();
        Asignacion devuelta = asignarActivoUseCase.devolver(id, req.getFechaDevolucion(), req.getObservaciones());

        return ResponseEntity.ok(ActionResponse.ok("Devolucion procesada exitosamente. Activo disponible nuevamente", DtoMapper.toAsignacionResponse(devuelta)));
    }

    @GetMapping
    @Operation(summary = "Listar todas las asignaciones", description = "Retorna el historial completo de asignaciones y prestamos")
    public ResponseEntity<ActionResponse<List<AsignacionResponse>>> listarTodas() {
        List<AsignacionResponse> listado = asignacionService.listarTodas().stream()
                .map(DtoMapper::toAsignacionResponse)
                .toList();
        return ResponseEntity.ok(ActionResponse.ok("Historial de asignaciones obtenido", listado));
    }

    @GetMapping("/activo/{activoId}")
    @Operation(summary = "Historial de asignaciones por activo", description = "Obtiene todas las asignaciones historicas de un activo")
    public ResponseEntity<ActionResponse<List<AsignacionResponse>>> listarPorActivo(@PathVariable Long activoId) {
        List<AsignacionResponse> listado = asignacionService.listarPorActivo(activoId).stream()
                .map(DtoMapper::toAsignacionResponse)
                .toList();
        return ResponseEntity.ok(ActionResponse.ok("Asignaciones del activo obtenidas", listado));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Historial de asignaciones por usuario", description = "Obtiene todas las asignaciones asociadas a un custodio")
    public ResponseEntity<ActionResponse<List<AsignacionResponse>>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<AsignacionResponse> listado = asignacionService.listarPorUsuario(usuarioId).stream()
                .map(DtoMapper::toAsignacionResponse)
                .toList();
        return ResponseEntity.ok(ActionResponse.ok("Asignaciones del usuario obtenidas", listado));
    }
}

