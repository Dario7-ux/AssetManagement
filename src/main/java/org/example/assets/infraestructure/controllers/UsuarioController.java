package org.example.assets.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.assets.application.ports.in.UsuarioIn;
import org.example.assets.domain.models.Rol;
import org.example.assets.domain.models.Usuario;
import org.example.assets.infraestructure.controllers.dtos.ActionResponse;
import org.example.assets.infraestructure.controllers.dtos.UsuarioRequest;
import org.example.assets.infraestructure.controllers.dtos.UsuarioResponse;
import org.example.assets.infraestructure.mappers.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestion administrativa de usuarios (Requiere rol ADMIN)")
@SecurityRequirement(name = "BearerAuth")
public class UsuarioController {

    private final UsuarioIn usuarioIn;

    public UsuarioController(UsuarioIn usuarioIn) {
        this.usuarioIn = usuarioIn;
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario con credenciales y rol")
    public ResponseEntity<ActionResponse<UsuarioResponse>> registrar(@Valid @RequestBody UsuarioRequest request) {
        Rol rol = Rol.USER;
        if (request.getRol() != null && !request.getRol().isBlank()) {
            try {
                rol = Rol.valueOf(request.getRol().trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        Usuario nuevo = Usuario.nuevo(request.getUsername(), request.getPassword(), rol);
        Usuario guardado = usuarioIn.registrar(nuevo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ActionResponse.ok("Usuario registrado exitosamente", DtoMapper.toUsuarioResponse(guardado)));
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Retorna el listado de usuarios del sistema")
    public ResponseEntity<ActionResponse<List<UsuarioResponse>>> listarTodos() {
        List<UsuarioResponse> listado = usuarioIn.listarTodos().stream()
                .map(DtoMapper::toUsuarioResponse)
                .toList();
        return ResponseEntity.ok(ActionResponse.ok("Listado de usuarios obtenido", listado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene el detalle de un usuario por su identificador")
    public ResponseEntity<ActionResponse<UsuarioResponse>> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioIn.buscarPorId(id);
        return ResponseEntity.ok(ActionResponse.ok("Usuario encontrado", DtoMapper.toUsuarioResponse(usuario)));
    }
}

