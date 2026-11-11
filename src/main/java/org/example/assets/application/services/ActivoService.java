package org.example.assets.application.services;

import org.example.assets.application.ports.in.GestionarActivoUseCase;
import org.example.assets.application.ports.in.RegistrarActivoUseCase;
import org.example.assets.application.ports.out.ActivoRepositoryPort;
import org.example.assets.application.ports.out.CategoriaRepositoryPort;
import org.example.assets.application.ports.out.DepartamentoRepositoryPort;
import org.example.assets.application.ports.out.NotificacionPort;
import org.example.assets.application.ports.out.TipoCambioPort;
import org.example.assets.domain.exception.CategoriaNoEncontradaException;
import org.example.assets.domain.exception.CupoDepartamentoExcedidoException;
import org.example.assets.domain.exception.DatoInvalidoException;
import org.example.assets.domain.exception.RecursoNoEncontradoException;
import org.example.assets.domain.exception.ReglaNegocioException;
import org.example.assets.domain.models.Activo;
import org.example.assets.domain.models.Categoria;
import org.example.assets.domain.models.Departamento;
import org.example.assets.domain.models.EstadoActivo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ActivoService implements RegistrarActivoUseCase, GestionarActivoUseCase {

    private final ActivoRepositoryPort activoRepositoryPort;
    private final DepartamentoRepositoryPort departamentoRepositoryPort;
    private final CategoriaRepositoryPort categoriaRepositoryPort;
    private final NotificacionPort notificacionPort;
    private final TipoCambioPort tipoCambioPort;

    public ActivoService(
            ActivoRepositoryPort activoRepositoryPort,
            DepartamentoRepositoryPort departamentoRepositoryPort,
            CategoriaRepositoryPort categoriaRepositoryPort,
            NotificacionPort notificacionPort,
            TipoCambioPort tipoCambioPort) {
        this.activoRepositoryPort = activoRepositoryPort;
        this.departamentoRepositoryPort = departamentoRepositoryPort;
        this.categoriaRepositoryPort = categoriaRepositoryPort;
        this.notificacionPort = notificacionPort;
        this.tipoCambioPort = tipoCambioPort;
    }

    public ActivoService(
            ActivoRepositoryPort activoRepositoryPort,
            DepartamentoRepositoryPort departamentoRepositoryPort,
            CategoriaRepositoryPort categoriaRepositoryPort,
            NotificacionPort notificacionPort) {
        this(activoRepositoryPort, departamentoRepositoryPort, categoriaRepositoryPort, notificacionPort, null);
    }

    @Override
    public Activo registrar(
            String codigoInventario,
            String nombre,
            String marca,
            String modelo,
            String numeroSerie,
            BigDecimal costo,
            String moneda,
            LocalDate fechaAdquisicion,
            LocalDate finGarantia,
            Long categoriaId,
            Long departamentoId) {

        if (categoriaId == null) {
            throw new DatoInvalidoException("El id de la categoria es obligatorio");
        }
        if (departamentoId == null) {
            throw new DatoInvalidoException("El id del departamento es obligatorio");
        }

        Categoria categoria = categoriaRepositoryPort.buscarPorId(categoriaId)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoria con id " + categoriaId + " no encontrada"));

        Departamento departamento = departamentoRepositoryPort.buscarPorId(departamentoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Departamento con id " + departamentoId + " no encontrado"));

        String monedaFinal = (moneda != null && !moneda.isBlank()) ? moneda.trim().toUpperCase() : "USD";
        BigDecimal costoBase = costo;

        // Conversion a USD si la moneda es extranjera y se cuenta con el puerto de tipo de cambio
        if (tipoCambioPort != null && !"USD".equalsIgnoreCase(monedaFinal) && costo != null) {
            costoBase = tipoCambioPort.convertir(costo, monedaFinal, "USD");
        }

        Activo nuevoActivo = Activo.nuevo(
                codigoInventario,
                nombre,
                marca,
                modelo,
                numeroSerie,
                costoBase,
                monedaFinal,
                fechaAdquisicion,
                finGarantia,
                categoria,
                departamento
        );

        return registrar(nuevoActivo);
    }

    @Override
    public Activo registrar(Activo activo) {
        if (activo == null) {
            throw new DatoInvalidoException("El activo no puede ser nulo");
        }

        // Validar unicidad de codigo de inventario y numero de serie
        if (activoRepositoryPort.buscarPorCodigoInventario(activo.getCodigoInventario()).isPresent()) {
            throw new ReglaNegocioException("Ya existe un activo registrado con el codigo de inventario '" + activo.getCodigoInventario() + "'");
        }
        if (activoRepositoryPort.buscarPorNumeroSerie(activo.getNumeroSerie()).isPresent()) {
            throw new ReglaNegocioException("Ya existe un activo registrado con el numero de serie '" + activo.getNumeroSerie() + "'");
        }

        // Obtener entidades actualizadas
        Long deptoId = activo.getDepartamento().getId();
        Departamento departamento = departamentoRepositoryPort.buscarPorId(deptoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Departamento con id " + deptoId + " no encontrado"));

        Long catId = activo.getCategoria().getId();
        Categoria categoria = categoriaRepositoryPort.buscarPorId(catId)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoria con id " + catId + " no encontrada"));

        activo.setDepartamento(departamento);
        activo.setCategoria(categoria);

        // Regla de Negocio: Validar presupuesto maximo del departamento
        try {
            departamento.validarCupoDisponible(activo.getCostoAdquisicion());
        } catch (CupoDepartamentoExcedidoException e) {
            if (notificacionPort != null) {
                notificacionPort.enviarAlertaCupoExcedido(departamento, activo.getCostoAdquisicion());
            }
            throw e;
        }

        // Guardar el activo
        Activo guardado = activoRepositoryPort.guardar(activo);

        // Actualizar valor acumulado del departamento
        departamento.acumularValorActivo(guardado.getCostoAdquisicion());
        departamentoRepositoryPort.guardar(departamento);

        return guardado;
    }

    @Override
    public Activo buscarPorId(Long id) {
        if (id == null) {
            throw new DatoInvalidoException("El id del activo es obligatorio");
        }
        return activoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Activo con id " + id + " no encontrado"));
    }

    @Override
    public List<Activo> listarTodos() {
        return activoRepositoryPort.listarTodos();
    }

    @Override
    public List<Activo> listarPorEstado(EstadoActivo estado) {
        if (estado == null) {
            return listarTodos();
        }
        return activoRepositoryPort.listarPorEstado(estado);
    }

    @Override
    public List<Activo> listarPorDepartamento(Long departamentoId) {
        if (departamentoId == null) {
            throw new DatoInvalidoException("El id del departamento es obligatorio");
        }
        return activoRepositoryPort.listarPorDepartamentoId(departamentoId);
    }

    @Override
    public Activo cambiarEstado(Long activoId, EstadoActivo nuevoEstado) {
        if (nuevoEstado == null) {
            throw new DatoInvalidoException("El nuevo estado es obligatorio");
        }
        Activo activo = buscarPorId(activoId);

        switch (nuevoEstado) {
            case DISPONIBLE -> activo.liberarDeAsignacion();
            case EN_MANTENIMIENTO -> activo.pasarAMantenimiento();
            case DE_BAJA -> {
                activo.darDeBaja();
                // Al dar de baja, se descuenta del valor actual del departamento
                if (activo.getDepartamento() != null && activo.getCostoAdquisicion() != null) {
                    activo.getDepartamento().restarValorActivo(activo.getCostoAdquisicion());
                    departamentoRepositoryPort.guardar(activo.getDepartamento());
                }
            }
            case ASIGNADO -> activo.marcarComoAsignado();
        }

        return activoRepositoryPort.guardar(activo);
    }

    @Override
    public void eliminar(Long id) {
        Activo activo = buscarPorId(id);
        if (activo.getEstado() == EstadoActivo.ASIGNADO) {
            throw new ReglaNegocioException("No se puede eliminar un activo que se encuentra asignado actualmente");
        }
        activoRepositoryPort.eliminar(id);
    }
}

