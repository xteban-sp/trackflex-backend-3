package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.DistritoDTO;
import com.rutaflex.trackflexv3.entity.Distrito;
import com.rutaflex.trackflexv3.mapper.DistritoMapper;
import com.rutaflex.trackflexv3.repository.DistritoRepository;
import com.rutaflex.trackflexv3.service.general.service.DistritoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DistritoServiceImpl implements DistritoService {

    private final DistritoRepository repository;
    private final DistritoMapper mapper;

    @Override
    public List<DistritoDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar distritos", e);
        }
    }

    @Override
    public Optional<DistritoDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar distrito con ID: " + id, e);
        }
    }

    @Override
    public List<DistritoDTO> findByObject(DistritoDTO dto) throws ServiceException {
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            return repository.findByNombre(dto.getNombre()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public DistritoDTO save(DistritoDTO dto) throws ServiceException {
        try {
            if (repository.existsByNombre(dto.getNombre())) {
                throw new ServiceException("Ya existe un distrito con el nombre: " + dto.getNombre());
            }
            Distrito entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Distrito saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el distrito", e);
        }
    }

    @Override
    @Transactional
    public DistritoDTO update(DistritoDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de distrito no proporcionado");
            }

            Distrito existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Distrito no encontrado con ID: " + dto.getId()));

            if (!existing.getNombre().equals(dto.getNombre()) && repository.existsByNombre(dto.getNombre())) {
                throw new ServiceException("Ya existe otro distrito con el nombre: " + dto.getNombre());
            }

            Distrito updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");

            Distrito saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el distrito", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Distrito entity = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Distrito no encontrado con ID: " + id));
            entity.setEstado("I");
            repository.save(entity);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente el distrito", e);
        }
    }
}