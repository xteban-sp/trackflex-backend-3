package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.TipoGastoDTO;
import com.rutaflex.trackflexv3.entity.TipoGasto;
import com.rutaflex.trackflexv3.mapper.TipoGastoMapper;
import com.rutaflex.trackflexv3.repository.TipoGastoRepository;
import com.rutaflex.trackflexv3.service.general.service.TipoGastoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TipoGastoServiceImpl implements TipoGastoService {

    private final TipoGastoRepository repository;
    private final TipoGastoMapper mapper;

    @Override
    public List<TipoGastoDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar tipos de gasto", e);
        }
    }

    @Override
    public Optional<TipoGastoDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(t -> "A".equals(t.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar tipo de gasto con ID: " + id, e);
        }
    }

    @Override
    public List<TipoGastoDTO> findByObject(TipoGastoDTO dto) throws ServiceException {
        return findAll();
    }

    @Override
    @Transactional
    public TipoGastoDTO save(TipoGastoDTO dto) throws ServiceException {
        try {
            if (repository.existsByDescripcion(dto.getDescripcion())) {
                throw new ServiceException("Ya existe un tipo de gasto con la descripción: " + dto.getDescripcion());
            }
            TipoGasto entity = mapper.toEntity(dto);
            entity.setEstado("A");
            TipoGasto saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el tipo de gasto", e);
        }
    }

    @Override
    @Transactional
    public TipoGastoDTO update(TipoGastoDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de tipo de gasto no proporcionado");
            }

            TipoGasto existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Tipo de gasto no encontrado con ID: " + dto.getId()));

            if (!existing.getDescripcion().equals(dto.getDescripcion()) && repository.existsByDescripcion(dto.getDescripcion())) {
                throw new ServiceException("Ya existe otro tipo de gasto con la descripción: " + dto.getDescripcion());
            }

            TipoGasto updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            TipoGasto saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el tipo de gasto", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            TipoGasto t = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Tipo de gasto no encontrado con ID: " + id));
            t.setEstado("I");
            repository.save(t);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente el tipo de gasto", e);
        }
    }
}