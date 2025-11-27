package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.IncidenciaDTO;
import com.rutaflex.trackflexv3.entity.Incidencia;
import com.rutaflex.trackflexv3.mapper.IncidenciaMapper;
import com.rutaflex.trackflexv3.repository.IncidenciaRepository;
import com.rutaflex.trackflexv3.service.general.service.IncidenciaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class IncidenciaServiceImpl implements IncidenciaService {

    private final IncidenciaRepository repository;
    private final IncidenciaMapper mapper;

    @Override
    public List<IncidenciaDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar incidencias", e);
        }
    }

    @Override
    public Optional<IncidenciaDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar incidencia con ID: " + id, e);
        }
    }

    @Override
    public List<IncidenciaDTO> findByObject(IncidenciaDTO dto) throws ServiceException {
        if (dto.getEntregaId() != null) {
            return repository.findByEntregaId(dto.getEntregaId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public IncidenciaDTO save(IncidenciaDTO dto) throws ServiceException {
        try {
            if (dto.getEntregaId() == null) {
                throw new ServiceException("El ID de entrega es obligatorio");
            }
            Incidencia entity = mapper.toEntity(dto);
            Incidencia saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar la incidencia", e);
        }
    }

    @Override
    @Transactional
    public IncidenciaDTO update(IncidenciaDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de incidencia no proporcionado");
            }

            Incidencia existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Incidencia no encontrada con ID: " + dto.getId()));

            Incidencia updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            Incidencia saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar la incidencia", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Incidencia i = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Incidencia no encontrada con ID: " + id));
            i.setEstadoRegistro("I");
            repository.save(i);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente la incidencia", e);
        }
    }
}