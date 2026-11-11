package org.example.assets.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.assets.application.ports.in.CategoriaUseCase;
import org.example.assets.domain.models.Categoria;
import org.example.assets.infraestructure.controllers.dtos.ActionResponse;
import org.example.assets.infraestructure.controllers.dtos.CategoriaRequest;
import org.example.assets.infraestructure.controllers.dtos.CategoriaResponse;
import org.example.assets.infraestructure.mappers.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorias de Activos", description = "Gestion de clasificaciones y vidas utiles de activos")
@SecurityRequirement(name = "BearerAuth")
public class CategoriaController {

    private final CategoriaUseCase categoriaUseCase;

    public CategoriaController(CategoriaUseCase categoriaUseCase) {
        this.categoriaUseCase = categoriaUseCase;
    }

    @PostMapping
    @Operation(summary = "Registrar nueva categoria", description = "Crea una categoria para activos (Requiere ADMIN)")
    public ResponseEntity<ActionResponse<CategoriaResponse>> registrar(@Valid @RequestBody CategoriaRequest request) {
        Categoria nueva = Categoria.nueva(
                request.getCodigo(),
                request.getNombre(),
                request.getDescripcion(),
                request.getVidaUtilAnios(),
                request.getTasaDepreciacionAnual()
        );
        Categoria guardada = categoriaUseCase.registrar(nueva);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ActionResponse.ok("Categoria registrada exitosamente", DtoMapper.toCategoriaResponse(guardada)));
    }

    @GetMapping
    @Operation(summary = "Listar todas las categorias", description = "Retorna el catalogo de categorias de activos")
    public ResponseEntity<ActionResponse<List<CategoriaResponse>>> listarTodas() {
        List<CategoriaResponse> listado = categoriaUseCase.listarTodas().stream()
                .map(DtoMapper::toCategoriaResponse)
                .toList();
        return ResponseEntity.ok(ActionResponse.ok("Categorias obtenidas exitosamente", listado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID", description = "Obtiene los detalles de una categoria por su ID")
    public ResponseEntity<ActionResponse<CategoriaResponse>> buscarPorId(@PathVariable Long id) {
        Categoria categoria = categoriaUseCase.buscarPorId(id);
        return ResponseEntity.ok(ActionResponse.ok("Categoria encontrada", DtoMapper.toCategoriaResponse(categoria)));
    }
}

