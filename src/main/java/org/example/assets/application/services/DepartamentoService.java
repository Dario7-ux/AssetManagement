package org.example.assets.application.services;

import org.example.assets.application.ports.in.DepartamentoUseCase;
import org.example.assets.application.ports.out.DepartamentoRepositoryPort;
import org.example.assets.domain.exception.DatoInvalidoException;
import org.example.assets.domain.exception.RecursoNoEncontradoException;
import org.example.assets.domain.exception.ReglaNegocioException;
import org.example.assets.domain.models.Departamento;

import java.util.List;

public class DepartamentoService implements DepartamentoUseCase {

    private final DepartamentoRepositoryPort departamentoRepositoryPort;

    public DepartamentoService(DepartamentoRepositoryPort departamentoRepositoryPort) {
        this.departamentoRepositoryPort = departamentoRepositoryPort;
    }

    @Override
    public Departamento registrar(Departamento departamento) {
        if (departamento == null) {
            throw new DatoInvalidoException("El departamento no puede ser nulo");
        }
        if (departamento.getCodigo() == null || departamento.getCodigo().isBlank()) {
            throw new DatoInvalidoException("El codigo del departamento es obligatorio");
        }
        if (departamento.getNombre() == null || departamento.getNombre().isBlank()) {
            throw new DatoInvalidoException("El nombre del departamento es obligatorio");
        }

        departamentoRepositoryPort.buscarPorCodigo(departamento.getCodigo().trim()).ifPresent(d -> {
            throw new ReglaNegocioException("Ya existe un departamento con el codigo '" + departamento.getCodigo() + "'");
        });

        return departamentoRepositoryPort.guardar(departamento);
    }

    @Override
    public Departamento buscarPorId(Long id) {
        if (id == null) {
            throw new DatoInvalidoException("El id del departamento es obligatorio");
        }
        return departamentoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Departamento con id " + id + " no encontrado"));
    }

    @Override
    public List<Departamento> listarTodos() {
        return departamentoRepositoryPort.listarTodos();
    }
}

