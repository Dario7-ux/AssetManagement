package org.example.assets.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.assets.application.ports.in.GestionarActivoUseCase;
import org.example.assets.application.ports.in.RegistrarActivoUseCase;
import org.example.assets.domain.models.Activo;
import org.example.assets.domain.models.EstadoActivo;
import org.example.assets.infraestructure.controllers.dtos.ActionResponse;
import org.example.assets.infraestructure.controllers.dtos.ActivoRequest;
import org.example.assets.infraestructure.controllers.dtos.ActivoResponse;
import org.example.assets.infraestructure.controllers.dtos.CambioEstadoRequest;
import org.example.assets.infraestructure.mappers.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activos")
@Tag(name = "Activos", description = "Inventario, registro, consulta y control de ciclo de vida de activos fijos")
@SecurityRequirement(name = "BearerAuth")
public class ActivoController {

    private final RegistrarActivoUseCase registrarActivoUseCase;
    private final GestionarActivoUseCase gestionarActivoUseCase;

    public ActivoController(RegistrarActivoUseCase registrarActivoUseCase,
                            GestionarActivoUseCase gestionarActivoUseCase) {
        this.registrarActivoUseCase = registrarActivoUseCase;
        this.gestionarActivoUseCase = gestionarActivoUseCase;
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo activo", description = "Registra un activo en inventario validando cupo departamental y convirtiendo divisas si aplica (Requiere ADMIN)")
    public ResponseEntity<ActionResponse<ActivoResponse>> registrar(@Valid @RequestBody ActivoRequest request) {
        Activo registrado = registrarActivoUseCase.registrar(
                request.getCodigoInventario(),
                request.getNombre(),
                request.getMarca(),
                request.getModelo(),
                request.getNumeroSerie(),
                request.getCostoAdquisicion(),
                request.getMoneda(),
                request.getFechaAdquisicion(),
                request.getFinGarantia(),
                request.getCategoriaId(),
                request.getDepartamentoId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ActionResponse.ok("Activo registrado exitosamente en el inventario", DtoMapper.toActivoResponse(registrado)));
    }

    @GetMapping
    @Operation(summary = "Listar activos", description = "Retorna el inventario de activos con filtros opcionales por estado o departamento")
    public ResponseEntity<ActionResponse<List<ActivoResponse>>> listar(
            @RequestParam(required = false) EstadoActivo estado,
            @RequestParam(required = false) Long departamentoId) {

        List<Activo> activos;
        if (departamentoId != null) {
            activos = gestionarActivoUseCase.listarPorDepartamento(departamentoId);
        } else if (estado != null) {
            activos = gestionarActivoUseCase.listarPorEstado(estado);
        } else {
            activos = gestionarActivoUseCase.listarTodos();
        }

        List<ActivoResponse> listado = activos.stream()
                .map(DtoMapper::toActivoResponse)
                .toList();

        return ResponseEntity.ok(ActionResponse.ok("Inventario de activos obtenido", listado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar activo por ID", description = "Obtiene los detalles de un activo especifico")
    public ResponseEntity<ActionResponse<ActivoResponse>> buscarPorId(@PathVariable Long id) {
        Activo activo = gestionarActivoUseCase.buscarPorId(id);
        return ResponseEntity.ok(ActionResponse.ok("Activo encontrado", DtoMapper.toActivoResponse(activo)));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de un activo", description = "Actualiza el estado operativo del activo (DISPONIBLE, EN_MANTENIMIENTO, DE_BAJA) (Requiere ADMIN)")
    public ResponseEntity<ActionResponse<ActivoResponse>> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRequest request) {

        Activo actualizado = gestionarActivoUseCase.cambiarEstado(id, request.getEstado());
        return ResponseEntity.ok(ActionResponse.ok("Estado del activo actualizado a " + request.getEstado(), DtoMapper.toActivoResponse(actualizado)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un activo", description = "Elimina un activo si no esta asignado actualmente (Requiere ADMIN)")
    public ResponseEntity<ActionResponse<Void>> eliminar(@PathVariable Long id) {
        gestionarActivoUseCase.eliminar(id);
        return ResponseEntity.ok(ActionResponse.ok("Activo eliminado exitosamente", null));
    }
}

