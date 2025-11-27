package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.EstadoDTO;
import com.rutaflex.trackflexv3.entity.Estado;
import com.rutaflex.trackflexv3.mapper.EstadoMapper;
import com.rutaflex.trackflexv3.repository.EstadoRepository;
import com.rutaflex.trackflexv3.service.general.service.EstadoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EstadoServiceImpl implements EstadoService {

    private final EstadoRepository repository;
    private final EstadoMapper mapper;

    @Override
    public List<EstadoDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar estados activos", e);
        }
    }

    @Override
    public Optional<EstadoDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(e -> "A".equals(e.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar estado con ID: " + id, e);
        }
    }

    @Override
    public List<EstadoDTO> findByObject(EstadoDTO dto) throws ServiceException {
        if (dto.getTipo() != null) {
            return repository.findByTipo(dto.getTipo()).stream()
                    .filter(e -> "A".equals(e.getEstado()))
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public EstadoDTO save(EstadoDTO dto) throws ServiceException {
        try {
            Estado entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Estado saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el estado", e);
        }
    }

    @Override
    @Transactional
    public EstadoDTO update(EstadoDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de estado no proporcionado");
            }

            Estado existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Estado no encontrado con ID: " + dto.getId()));

            Estado updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            Estado saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el estado", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Estado e = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Estado no encontrado con ID: " + id));
            e.setEstado("I");
            repository.save(e);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al dar de baja el estado", e);
        }
    }
}