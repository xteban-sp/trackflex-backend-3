package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.RutaDTO;
import com.rutaflex.trackflexv3.entity.Ruta;
import com.rutaflex.trackflexv3.mapper.RutaMapper;
import com.rutaflex.trackflexv3.repository.RutaRepository;
import com.rutaflex.trackflexv3.service.general.service.RutaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RutaServiceImpl implements RutaService {

    private final RutaRepository repository;
    private final RutaMapper mapper;

    @Override
    public List<RutaDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar rutas activas", e);
        }
    }

    @Override
    public Optional<RutaDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(r -> "A".equals(r.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar ruta con ID: " + id, e);
        }
    }

    @Override
    public List<RutaDTO> findByObject(RutaDTO dto) throws ServiceException {
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            return repository.findByNombre(dto.getNombre()).stream()
                    .filter(r -> "A".equals(r.getEstado()))
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public RutaDTO save(RutaDTO dto) throws ServiceException {
        try {
            if (repository.existsByNombre(dto.getNombre())) {
                throw new ServiceException("Ya existe una ruta con el nombre: " + dto.getNombre());
            }
            Ruta entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Ruta saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar la ruta", e);
        }
    }

    @Override
    @Transactional
    public RutaDTO update(RutaDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de ruta no proporcionado");
            }

            Ruta existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Ruta no encontrada con ID: " + dto.getId()));

            if (!existing.getNombre().equals(dto.getNombre()) && repository.existsByNombre(dto.getNombre())) {
                throw new ServiceException("Ya existe otra ruta con el nombre: " + dto.getNombre());
            }

            Ruta updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            Ruta saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar la ruta", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Ruta r = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Ruta no encontrada con ID: " + id));
            r.setEstado("I");
            repository.save(r);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al dar de baja la ruta", e);
        }
    }
}