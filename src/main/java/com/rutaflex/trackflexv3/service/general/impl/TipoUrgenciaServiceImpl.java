package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.TipoUrgenciaDTO;
import com.rutaflex.trackflexv3.entity.TipoUrgencia;
import com.rutaflex.trackflexv3.mapper.TipoUrgenciaMapper;
import com.rutaflex.trackflexv3.repository.TipoUrgenciaRepository;
import com.rutaflex.trackflexv3.service.general.service.TipoUrgenciaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TipoUrgenciaServiceImpl implements TipoUrgenciaService {

    private final TipoUrgenciaRepository repository;
    private final TipoUrgenciaMapper mapper;

    @Override
    public List<TipoUrgenciaDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar tipos de urgencia", e);
        }
    }

    @Override
    public Optional<TipoUrgenciaDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(t -> "A".equals(t.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar tipo de urgencia con ID: " + id, e);
        }
    }

    @Override
    public List<TipoUrgenciaDTO> findByObject(TipoUrgenciaDTO dto) throws ServiceException {
        return findAll();
    }

    @Override
    @Transactional
    public TipoUrgenciaDTO save(TipoUrgenciaDTO dto) throws ServiceException {
        try {
            TipoUrgencia entity = mapper.toEntity(dto);
            entity.setEstado("A");
            TipoUrgencia saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el tipo de urgencia", e);
        }
    }

    @Override
    @Transactional
    public TipoUrgenciaDTO update(TipoUrgenciaDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de tipo de urgencia no proporcionado");
            }

            TipoUrgencia existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Tipo de urgencia no encontrado con ID: " + dto.getId()));

            TipoUrgencia updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            TipoUrgencia saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el tipo de urgencia", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            TipoUrgencia t = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Tipo de urgencia no encontrado con ID: " + id));
            t.setEstado("I");
            repository.save(t);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente el tipo de urgencia", e);
        }
    }
}