package org.example.assets.infraestructure.repository;

import org.example.assets.application.ports.out.AsignacionRepositoryPort;
import org.example.assets.domain.models.Asignacion;
import org.example.assets.domain.models.EstadoAsignacion;
import org.example.assets.infraestructure.entities.AsignacionEntity;
import org.example.assets.infraestructure.mappers.AsignacionMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AsignacionRepositoryAdapter implements AsignacionRepositoryPort {

    private final AsignacionJpaRepository asignacionJpaRepository;

    public AsignacionRepositoryAdapter(AsignacionJpaRepository asignacionJpaRepository) {
        this.asignacionJpaRepository = asignacionJpaRepository;
    }

    @Override
    public Asignacion guardar(Asignacion asignacion) {
        AsignacionEntity entity = AsignacionMapper.toEntity(asignacion);
        AsignacionEntity guardado = asignacionJpaRepository.save(entity);
        return AsignacionMapper.toDomain(guardado);
    }

    @Override
    public Optional<Asignacion> buscarPorId(Long id) {
        return asignacionJpaRepository.findById(id)
                .map(AsignacionMapper::toDomain);
    }

    @Override
    public Optional<Asignacion> buscarActivaPorActivoId(Long activoId) {
        return asignacionJpaRepository.findByActivoIdAndEstadoAsignacion(activoId, EstadoAsignacion.ACTIVA)
                .map(AsignacionMapper::toDomain);
    }

    @Override
    public List<Asignacion> listarTodas() {
        return asignacionJpaRepository.findAll().stream()
                .map(AsignacionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Asignacion> listarPorActivoId(Long activoId) {
        return asignacionJpaRepository.findByActivoId(activoId).stream()
                .map(AsignacionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Asignacion> listarPorUsuarioId(Long usuarioId) {
        return asignacionJpaRepository.findByUsuarioId(usuarioId).stream()
                .map(AsignacionMapper::toDomain)
                .toList();
    }
}

