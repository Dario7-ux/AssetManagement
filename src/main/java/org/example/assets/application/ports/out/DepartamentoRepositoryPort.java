package org.example.assets.application.ports.out;

import org.example.assets.domain.models.Departamento;

import java.util.List;
import java.util.Optional;

public interface DepartamentoRepositoryPort {

    Departamento guardar(Departamento departamento);

    Optional<Departamento> buscarPorId(Long id);

    Optional<Departamento> buscarPorCodigo(String codigo);

    List<Departamento> listarTodos();
}

