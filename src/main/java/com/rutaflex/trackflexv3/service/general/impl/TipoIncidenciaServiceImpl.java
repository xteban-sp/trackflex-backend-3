package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.TipoIncidenciaDTO;
import com.rutaflex.trackflexv3.entity.TipoIncidencia;
import com.rutaflex.trackflexv3.mapper.TipoIncidenciaMapper;
import com.rutaflex.trackflexv3.repository.TipoIncidenciaRepository;
import com.rutaflex.trackflexv3.service.general.service.TipoIncidenciaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TipoIncidenciaServiceImpl implements TipoIncidenciaService {

    private final TipoIncidenciaRepository repository;
    private final TipoIncidenciaMapper mapper;

    @Override
    public List<TipoIncidenciaDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar tipos de incidencia", e);
        }
    }

    @Override
    public Optional<TipoIncidenciaDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(t -> "A".equals(t.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar tipo de incidencia con ID: " + id, e);
        }
    }

    @Override
    public List<TipoIncidenciaDTO> findByObject(TipoIncidenciaDTO dto) throws ServiceException {
        return findAll();
    }

    @Override
    @Transactional
    public TipoIncidenciaDTO save(TipoIncidenciaDTO dto) throws ServiceException {
        try {
            TipoIncidencia entity = mapper.toEntity(dto);
            entity.setEstado("A");
            TipoIncidencia saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el tipo de incidencia", e);
        }
    }

    @Override
    @Transactional
    public TipoIncidenciaDTO update(TipoIncidenciaDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de tipo de incidencia no proporcionado");
            }

            TipoIncidencia existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Tipo de incidencia no encontrado con ID: " + dto.getId()));

            TipoIncidencia updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            TipoIncidencia saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el tipo de incidencia", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            TipoIncidencia t = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Tipo de incidencia no encontrado con ID: " + id));
            t.setEstado("I");
            repository.save(t);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente el tipo de incidencia", e);
        }
    }
}