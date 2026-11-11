package org.example.assets.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.assets.infraestructure.controllers.dtos.ActionResponse;
import org.example.assets.infraestructure.controllers.dtos.LoginRequest;
import org.example.assets.infraestructure.controllers.dtos.LoginResponse;
import org.example.assets.infraestructure.securite.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticacion", description = "Endpoints para inicio de sesion y generacion de tokens JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Inicio de sesion", description = "Valida credenciales y genera un token JWT con el rol asignado")
    public ResponseEntity<ActionResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String rol = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER")
                .replace("ROLE_", "");

        String token = jwtService.generar(request.getUsername(), rol);

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .username(request.getUsername())
                .rol(rol)
                .build();

        return ResponseEntity.ok(ActionResponse.ok("Autenticacion exitosa", response));
    }
}

