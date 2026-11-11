package org.example.assets.domain;

import org.example.assets.domain.exception.ActivoNoDisponibleException;
import org.example.assets.domain.exception.CupoDepartamentoExcedidoException;
import org.example.assets.domain.exception.ReglaNegocioException;
import org.example.assets.domain.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ActivoDomainTest {

    private Categoria categoriaComputo;
    private Departamento departamentoTI;
    private Usuario usuarioCustodio;

    @BeforeEach
    void setUp() {
        categoriaComputo = Categoria.nueva("COMP", "Equipos de Computo", "Laptops y computadoras", 3, new BigDecimal("33.33"));
        categoriaComputo.setId(1L);

        departamentoTI = Departamento.nuevo("TI-01", "Tecnologia de la Informacion", "Edificio Central", "Piso 3", new BigDecimal("10000.00"));
        departamentoTI.setId(1L);

        usuarioCustodio = Usuario.nuevo("empleado1", "secret123", Rol.USER);
        usuarioCustodio.setId(10L);
    }

    @Test
    @DisplayName("Debe crear un activo valido con estado inicial DISPONIBLE")
    void testCrearActivoExitoso() {
        Activo activo = Activo.nuevo(
                "ACT-001",
                "Laptop Dell Latitude",
                "Dell",
                "5420",
                "SN-12345",
                new BigDecimal("1500.00"),
                "USD",
                LocalDate.now(),
                LocalDate.now().plusYears(3),
                categoriaComputo,
                departamentoTI
        );

        assertNotNull(activo);
        assertEquals("ACT-001", activo.getCodigoInventario());
        assertEquals(EstadoActivo.DISPONIBLE, activo.getEstado());
        assertEquals(new BigDecimal("1500.00"), activo.getCostoAdquisicion());
    }

    @Test
    @DisplayName("Debe cambiar el estado a ASIGNADO al crear una asignacion")
    void testAsignarActivoDisponible() {
        Activo activo = Activo.nuevo(
                "ACT-002",
                "MacBook Air M2",
                "Apple",
                "Air M2",
                "SN-APPL-777",
                new BigDecimal("1200.00"),
                "USD",
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                categoriaComputo,
                departamentoTI
        );

        Asignacion asignacion = Asignacion.nueva(activo, usuarioCustodio, LocalDate.now(), "Equipo para analista");

        assertEquals(EstadoActivo.ASIGNADO, activo.getEstado());
        assertEquals(EstadoAsignacion.ACTIVA, asignacion.getEstadoAsignacion());
        assertEquals(usuarioCustodio, asignacion.getUsuario());
        assertNull(asignacion.getFechaDevolucion());
    }

    @Test
    @DisplayName("Debe lanzar ActivoNoDisponibleException si se intenta asignar un activo que no esta DISPONIBLE")
    void testAsignarActivoNoDisponible() {
        Activo activo = Activo.nuevo(
                "ACT-003",
                "Servidor PowerEdge",
                "Dell",
                "R750",
                "SN-SRV-999",
                new BigDecimal("4000.00"),
                "USD",
                LocalDate.now(),
                LocalDate.now().plusYears(5),
                categoriaComputo,
                departamentoTI
        );

        activo.pasarAMantenimiento();
        assertEquals(EstadoActivo.EN_MANTENIMIENTO, activo.getEstado());

        assertThrows(ActivoNoDisponibleException.class, () ->
                Asignacion.nueva(activo, usuarioCustodio, LocalDate.now(), "Intento invalido")
        );
    }

    @Test
    @DisplayName("Debe devolver el activo y retornarlo a estado DISPONIBLE")
    void testDevolverActivoExitoso() {
        Activo activo = Activo.nuevo(
                "ACT-004",
                "Monitor UltraSharp 27",
                "Dell",
                "U2723QE",
                "SN-MON-555",
                new BigDecimal("600.00"),
                "USD",
                LocalDate.now(),
                LocalDate.now().plusYears(3),
                categoriaComputo,
                departamentoTI
        );

        Asignacion asignacion = Asignacion.nueva(activo, usuarioCustodio, LocalDate.now(), "Prestamo temporal");
        assertEquals(EstadoActivo.ASIGNADO, activo.getEstado());

        asignacion.registrarDevolucion(LocalDate.now(), "Devuelto en perfecto estado");

        assertEquals(EstadoAsignacion.DEVUELTO, asignacion.getEstadoAsignacion());
        assertEquals(EstadoActivo.DISPONIBLE, activo.getEstado());
        assertNotNull(asignacion.getFechaDevolucion());
    }

    @Test
    @DisplayName("Debe lanzar ReglaNegocioException si se intenta devolver una asignacion ya finalizada")
    void testDevolverAsignacionYaFinalizada() {
        Activo activo = Activo.nuevo(
                "ACT-005",
                "Teclado Mecanico MX",
                "Logitech",
                "MX Mechanical",
                "SN-KB-111",
                new BigDecimal("150.00"),
                "USD",
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                categoriaComputo,
                departamentoTI
        );

        Asignacion asignacion = Asignacion.nueva(activo, usuarioCustodio, LocalDate.now(), "Accesorios");
        asignacion.registrarDevolucion(LocalDate.now(), "Primera devolucion");

        assertThrows(ReglaNegocioException.class, () ->
                asignacion.registrarDevolucion(LocalDate.now(), "Segunda devolucion")
        );
    }

    @Test
    @DisplayName("Debe lanzar CupoDepartamentoExcedidoException si se supera el presupuesto maximo")
    void testValidarCupoDepartamentoExcedido() {
        departamentoTI.setPresupuestoMaximo(new BigDecimal("5000.00"));
        departamentoTI.setValorActualActivos(new BigDecimal("4500.00"));

        BigDecimal costoNuevoActivo = new BigDecimal("800.00"); // 4500 + 800 = 5300 > 5000

        assertThrows(CupoDepartamentoExcedidoException.class, () ->
                departamentoTI.validarCupoDisponible(costoNuevoActivo)
        );
    }

    @Test
    @DisplayName("No debe permitir reactivar un activo que fue dado de baja")
    void testActivoDadoDeBajaNoReactivable() {
        Activo activo = Activo.nuevo(
                "ACT-006",
                "Laptop Antigua",
                "Lenovo",
                "T430",
                "SN-OLD-001",
                new BigDecimal("800.00"),
                "USD",
                LocalDate.now().minusYears(8),
                LocalDate.now().minusYears(5),
                categoriaComputo,
                departamentoTI
        );

        activo.darDeBaja();
        assertEquals(EstadoActivo.DE_BAJA, activo.getEstado());

        assertThrows(ReglaNegocioException.class, activo::liberarDeAsignacion);
        assertThrows(ReglaNegocioException.class, activo::pasarAMantenimiento);
    }
}

