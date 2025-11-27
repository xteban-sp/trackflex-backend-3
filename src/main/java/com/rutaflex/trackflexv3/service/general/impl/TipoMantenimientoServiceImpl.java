package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.TipoMantenimientoDTO;
import com.rutaflex.trackflexv3.entity.TipoMantenimiento;
import com.rutaflex.trackflexv3.mapper.TipoMantenimientoMapper;
import com.rutaflex.trackflexv3.repository.TipoMantenimientoRepository;
import com.rutaflex.trackflexv3.service.general.service.TipoMantenimientoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TipoMantenimientoServiceImpl implements TipoMantenimientoService {

    private final TipoMantenimientoRepository repository;
    private final TipoMantenimientoMapper mapper;

    @Override
    public List<TipoMantenimientoDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar tipos de mantenimiento", e);
        }
    }

    @Override
    public Optional<TipoMantenimientoDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(t -> "A".equals(t.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar tipo de mantenimiento con ID: " + id, e);
        }
    }

    @Override
    public List<TipoMantenimientoDTO> findByObject(TipoMantenimientoDTO dto) throws ServiceException {
        return findAll();
    }

    @Override
    @Transactional
    public TipoMantenimientoDTO save(TipoMantenimientoDTO dto) throws ServiceException {
        try {
            TipoMantenimiento entity = mapper.toEntity(dto);
            entity.setEstado("A");
            TipoMantenimiento saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el tipo de mantenimiento", e);
        }
    }

    @Override
    @Transactional
    public TipoMantenimientoDTO update(TipoMantenimientoDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de tipo de mantenimiento no proporcionado");
            }

            TipoMantenimiento existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Tipo de mantenimiento no encontrado con ID: " + dto.getId()));

            TipoMantenimiento updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            TipoMantenimiento saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el tipo de mantenimiento", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            TipoMantenimiento t = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Tipo de mantenimiento no encontrado con ID: " + id));
            t.setEstado("I");
            repository.save(t);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente el tipo de mantenimiento", e);
        }
    }
}