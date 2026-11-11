package org.example.assets.application.ports.in;

import org.example.assets.domain.models.Departamento;

import java.util.List;

public interface DepartamentoUseCase {

    Departamento registrar(Departamento departamento);

    Departamento buscarPorId(Long id);

    List<Departamento> listarTodos();
}

