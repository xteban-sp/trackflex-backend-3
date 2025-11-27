package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.EntregaDTO;
import com.rutaflex.trackflexv3.entity.Entrega;
import com.rutaflex.trackflexv3.mapper.EntregaMapper;
import com.rutaflex.trackflexv3.repository.EntregaRepository;
import com.rutaflex.trackflexv3.service.general.service.EntregaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EntregaServiceImpl implements EntregaService {

    private final EntregaRepository repository;
    private final EntregaMapper mapper;

    @Override
    public List<EntregaDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar entregas", e);
        }
    }

    @Override
    public Optional<EntregaDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar entrega con ID: " + id, e);
        }
    }

    @Override
    public List<EntregaDTO> findByObject(EntregaDTO dto) throws ServiceException {
        if ("pendiente".equalsIgnoreCase(dto.getEstadoRegistro())) {
            return repository.findEntregasPendientes().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        if ("entregado".equalsIgnoreCase(dto.getEstadoRegistro())) {
            return repository.findEntregasFinalizadas().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public EntregaDTO save(EntregaDTO dto) throws ServiceException {
        try {
            if (repository.existsByCodEntrega(dto.getCodEntrega())) {
                throw new ServiceException("Ya existe una entrega con el código: " + dto.getCodEntrega());
            }
            Entrega entity = mapper.toEntity(dto);
            entity.setEstadoRegistro("A");
            Entrega saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar la entrega", e);
        }
    }

    @Override
    @Transactional
    public EntregaDTO update(EntregaDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de entrega no proporcionado");
            }

            Entrega existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Entrega no encontrada con ID: " + dto.getId()));

            if (!existing.getCodEntrega().equals(dto.getCodEntrega()) && repository.existsByCodEntrega(dto.getCodEntrega())) {
                throw new ServiceException("Ya existe otra entrega con el código: " + dto.getCodEntrega());
            }

            Entrega updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstadoRegistro("A");
            Entrega saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar la entrega", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Entrega e = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Entrega no encontrada con ID: " + id));
            e.setEstadoRegistro("I");
            repository.save(e);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente la entrega", e);
        }
    }
}