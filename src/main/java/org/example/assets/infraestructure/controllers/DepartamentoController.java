package org.example.assets.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.assets.application.ports.in.DepartamentoUseCase;
import org.example.assets.domain.models.Departamento;
import org.example.assets.infraestructure.controllers.dtos.ActionResponse;
import org.example.assets.infraestructure.controllers.dtos.DepartamentoRequest;
import org.example.assets.infraestructure.controllers.dtos.DepartamentoResponse;
import org.example.assets.infraestructure.mappers.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departamentos")
@Tag(name = "Departamentos", description = "Gestion de areas organizacionales y control de cupos de inversion")
@SecurityRequirement(name = "BearerAuth")
public class DepartamentoController {

    private final DepartamentoUseCase departamentoUseCase;

    public DepartamentoController(DepartamentoUseCase departamentoUseCase) {
        this.departamentoUseCase = departamentoUseCase;
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo departamento", description = "Crea un area con limite presupuestario de activos (Requiere ADMIN)")
    public ResponseEntity<ActionResponse<DepartamentoResponse>> registrar(@Valid @RequestBody DepartamentoRequest request) {
        Departamento nuevo = Departamento.nuevo(
                request.getCodigo(),
                request.getNombre(),
                request.getEdificio(),
                request.getPiso(),
                request.getPresupuestoMaximo()
        );
        Departamento guardado = departamentoUseCase.registrar(nuevo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ActionResponse.ok("Departamento registrado exitosamente", DtoMapper.toDepartamentoResponse(guardado)));
    }

    @GetMapping
    @Operation(summary = "Listar todos los departamentos", description = "Retorna las areas organizacionales y sus balances de activos")
    public ResponseEntity<ActionResponse<List<DepartamentoResponse>>> listarTodos() {
        List<DepartamentoResponse> listado = departamentoUseCase.listarTodos().stream()
                .map(DtoMapper::toDepartamentoResponse)
                .toList();
        return ResponseEntity.ok(ActionResponse.ok("Departamentos obtenidos exitosamente", listado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar departamento por ID", description = "Obtiene los datos de un departamento y su estado de presupuesto")
    public ResponseEntity<ActionResponse<DepartamentoResponse>> buscarPorId(@PathVariable Long id) {
        Departamento depto = departamentoUseCase.buscarPorId(id);
        return ResponseEntity.ok(ActionResponse.ok("Departamento encontrado", DtoMapper.toDepartamentoResponse(depto)));
    }
}

