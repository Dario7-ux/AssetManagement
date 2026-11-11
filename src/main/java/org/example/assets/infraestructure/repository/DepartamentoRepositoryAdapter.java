package org.example.assets.infraestructure.repository;

import org.example.assets.application.ports.out.DepartamentoRepositoryPort;
import org.example.assets.domain.models.Departamento;
import org.example.assets.infraestructure.entities.DepartamentoEntity;
import org.example.assets.infraestructure.mappers.DepartamentoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DepartamentoRepositoryAdapter implements DepartamentoRepositoryPort {

    private final DepartamentoJpaRepository departamentoJpaRepository;

    public DepartamentoRepositoryAdapter(DepartamentoJpaRepository departamentoJpaRepository) {
        this.departamentoJpaRepository = departamentoJpaRepository;
    }

    @Override
    public Departamento guardar(Departamento departamento) {
        DepartamentoEntity entity = DepartamentoMapper.toEntity(departamento);
        DepartamentoEntity guardado = departamentoJpaRepository.save(entity);
        return DepartamentoMapper.toDomain(guardado);
    }

    @Override
    public Optional<Departamento> buscarPorId(Long id) {
        return departamentoJpaRepository.findById(id)
                .map(DepartamentoMapper::toDomain);
    }

    @Override
    public Optional<Departamento> buscarPorCodigo(String codigo) {
        return departamentoJpaRepository.findByCodigo(codigo)
                .map(DepartamentoMapper::toDomain);
    }

    @Override
    public List<Departamento> listarTodos() {
        return departamentoJpaRepository.findAll().stream()
                .map(DepartamentoMapper::toDomain)
                .toList();
    }
}

