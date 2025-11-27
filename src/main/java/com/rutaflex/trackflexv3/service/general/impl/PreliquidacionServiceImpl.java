package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.PreliquidacionDTO;
import com.rutaflex.trackflexv3.entity.Preliquidacion;
import com.rutaflex.trackflexv3.mapper.PreliquidacionMapper;
import com.rutaflex.trackflexv3.repository.PreliquidacionRepository;
import com.rutaflex.trackflexv3.service.general.service.PreliquidacionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PreliquidacionServiceImpl implements PreliquidacionService {

    private final PreliquidacionRepository repository;
    private final PreliquidacionMapper mapper;

    @Override
    public List<PreliquidacionDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar preliquidaciones", e);
        }
    }

    @Override
    public Optional<PreliquidacionDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar preliquidación con ID: " + id, e);
        }
    }

    @Override
    public List<PreliquidacionDTO> findByObject(PreliquidacionDTO dto) throws ServiceException {
        if (dto.getServicioId() != null) {
            return repository.findByServicioId(dto.getServicioId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public PreliquidacionDTO save(PreliquidacionDTO dto) throws ServiceException {
        try {
            if (dto.getServicioId() == null) {
                throw new ServiceException("El ID del servicio es obligatorio");
            }
            if (dto.getFecha() == null) {
                throw new ServiceException("La fecha es obligatoria");
            }
            Preliquidacion entity = mapper.toEntity(dto);
            Preliquidacion saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al crear la preliquidación", e);
        }
    }

    @Override
    @Transactional
    public PreliquidacionDTO update(PreliquidacionDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de preliquidación no proporcionado");
            }

            Preliquidacion existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Preliquidación no encontrada con ID: " + dto.getId()));

            Preliquidacion updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            Preliquidacion saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar la preliquidación", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Preliquidacion p = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Preliquidación no encontrada con ID: " + id));
            repository.delete(p); // Sin campo 'estado', eliminación física
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar la preliquidación", e);
        }
    }
}