package org.example.assets.infraestructure.repository;

import org.example.assets.application.ports.out.ActivoRepositoryPort;
import org.example.assets.domain.models.Activo;
import org.example.assets.domain.models.EstadoActivo;
import org.example.assets.infraestructure.entities.ActivoEntity;
import org.example.assets.infraestructure.mappers.ActivoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ActivoRepositoryAdapter implements ActivoRepositoryPort {

    private final ActivoJpaRepository activoJpaRepository;

    public ActivoRepositoryAdapter(ActivoJpaRepository activoJpaRepository) {
        this.activoJpaRepository = activoJpaRepository;
    }

    @Override
    public Activo guardar(Activo activo) {
        ActivoEntity entity = ActivoMapper.toEntity(activo);
        ActivoEntity guardado = activoJpaRepository.save(entity);
        return ActivoMapper.toDomain(guardado);
    }

    @Override
    public Optional<Activo> buscarPorId(Long id) {
        return activoJpaRepository.findById(id)
                .map(ActivoMapper::toDomain);
    }

    @Override
    public Optional<Activo> buscarPorCodigoInventario(String codigoInventario) {
        return activoJpaRepository.findByCodigoInventario(codigoInventario)
                .map(ActivoMapper::toDomain);
    }

    @Override
    public Optional<Activo> buscarPorNumeroSerie(String numeroSerie) {
        return activoJpaRepository.findByNumeroSerie(numeroSerie)
                .map(ActivoMapper::toDomain);
    }

    @Override
    public List<Activo> listarTodos() {
        return activoJpaRepository.findAll().stream()
                .map(ActivoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Activo> listarPorEstado(EstadoActivo estado) {
        return activoJpaRepository.findByEstado(estado).stream()
                .map(ActivoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Activo> listarPorDepartamentoId(Long departamentoId) {
        return activoJpaRepository.findByDepartamentoId(departamentoId).stream()
                .map(ActivoMapper::toDomain)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        activoJpaRepository.deleteById(id);
    }
}

