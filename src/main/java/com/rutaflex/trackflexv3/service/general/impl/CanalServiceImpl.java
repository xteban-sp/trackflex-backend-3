package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.CanalDTO;
import com.rutaflex.trackflexv3.entity.Canal;
import com.rutaflex.trackflexv3.mapper.CanalMapper;
import com.rutaflex.trackflexv3.repository.CanalRepository;
import com.rutaflex.trackflexv3.service.general.service.CanalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CanalServiceImpl implements CanalService {

    private final CanalRepository repository;
    private final CanalMapper mapper;

    @Override
    public List<CanalDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar canales activos", e);
        }
    }

    @Override
    public Optional<CanalDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(c -> "A".equals(c.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar canal con ID: " + id, e);
        }
    }

    @Override
    public List<CanalDTO> findByObject(CanalDTO dto) throws ServiceException {
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            return repository.findByNombre(dto.getNombre()).stream()
                    .filter(c -> "A".equals(c.getEstado()))
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public CanalDTO save(CanalDTO dto) throws ServiceException {
        try {
            if (repository.existsByNombre(dto.getNombre())) {
                throw new ServiceException("Ya existe un canal con el nombre: " + dto.getNombre());
            }
            Canal entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Canal saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el canal", e);
        }
    }

    @Override
    @Transactional
    public CanalDTO update(CanalDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de canal no proporcionado");
            }

            Canal existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Canal no encontrado con ID: " + dto.getId()));

            if (!existing.getNombre().equals(dto.getNombre()) && repository.existsByNombre(dto.getNombre())) {
                throw new ServiceException("Ya existe otro canal con el nombre: " + dto.getNombre());
            }

            Canal updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            Canal saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el canal", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Canal c = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Canal no encontrado con ID: " + id));
            c.setEstado("I");
            repository.save(c);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al dar de baja el canal", e);
        }
    }
}