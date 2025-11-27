package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.AsignacionDTO;
import com.rutaflex.trackflexv3.entity.Asignacion;
import com.rutaflex.trackflexv3.mapper.AsignacionMapper;
import com.rutaflex.trackflexv3.repository.AsignacionRepository;
import com.rutaflex.trackflexv3.service.general.service.AsignacionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AsignacionServiceImpl implements AsignacionService {

    private final AsignacionRepository repository;
    private final AsignacionMapper mapper;

    @Override
    public List<AsignacionDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar asignaciones", e);
        }
    }

    @Override
    public Optional<AsignacionDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar asignación con ID: " + id, e);
        }
    }

    @Override
    public List<AsignacionDTO> findByObject(AsignacionDTO dto) throws ServiceException {
        if (dto.getServicioId() != null) {
            return repository.findByServicioId(dto.getServicioId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        if (dto.getConductorId() != null) {
            return repository.findByConductorId(dto.getConductorId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public AsignacionDTO save(AsignacionDTO dto) throws ServiceException {
        try {
            Asignacion entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Asignacion saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar la asignación", e);
        }
    }

    @Override
    @Transactional
    public AsignacionDTO update(AsignacionDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de asignación no proporcionado");
            }

            Asignacion existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Asignación no encontrada con ID: " + dto.getId()));

            Asignacion updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            Asignacion saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar la asignación", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Asignacion a = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Asignación no encontrada con ID: " + id));
            a.setEstado("I");
            repository.save(a);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente la asignación", e);
        }
    }
}