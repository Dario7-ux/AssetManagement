package org.example.assets.infraestructure.repository;

import org.example.assets.application.ports.out.CategoriaRepositoryPort;
import org.example.assets.domain.models.Categoria;
import org.example.assets.infraestructure.entities.CategoriaEntity;
import org.example.assets.infraestructure.mappers.CategoriaMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoriaRepositoryAdapter implements CategoriaRepositoryPort {

    private final CategoriaJpaRepository categoriaJpaRepository;

    public CategoriaRepositoryAdapter(CategoriaJpaRepository categoriaJpaRepository) {
        this.categoriaJpaRepository = categoriaJpaRepository;
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        CategoriaEntity entity = CategoriaMapper.toEntity(categoria);
        CategoriaEntity guardado = categoriaJpaRepository.save(entity);
        return CategoriaMapper.toDomain(guardado);
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaJpaRepository.findById(id)
                .map(CategoriaMapper::toDomain);
    }

    @Override
    public Optional<Categoria> buscarPorCodigo(String codigo) {
        return categoriaJpaRepository.findByCodigo(codigo)
                .map(CategoriaMapper::toDomain);
    }

    @Override
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaJpaRepository.findByNombre(nombre)
                .map(CategoriaMapper::toDomain);
    }

    @Override
    public List<Categoria> listarTodas() {
        return categoriaJpaRepository.findAll().stream()
                .map(CategoriaMapper::toDomain)
                .toList();
    }
}

