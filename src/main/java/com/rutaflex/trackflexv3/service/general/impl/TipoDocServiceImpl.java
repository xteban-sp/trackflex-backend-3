package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.TipoDocDTO;
import com.rutaflex.trackflexv3.entity.TipoDoc;
import com.rutaflex.trackflexv3.mapper.TipoDocMapper;
import com.rutaflex.trackflexv3.repository.TipoDocRepository;
import com.rutaflex.trackflexv3.service.general.service.TipoDocService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TipoDocServiceImpl implements TipoDocService {

    private final TipoDocRepository repository;
    private final TipoDocMapper mapper;

    @Override
    public List<TipoDocDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar tipos de documento", e);
        }
    }

    @Override
    public Optional<TipoDocDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(t -> "A".equals(t.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar tipo de documento con ID: " + id, e);
        }
    }

    @Override
    public List<TipoDocDTO> findByObject(TipoDocDTO dto) throws ServiceException {
        return findAll();
    }

    @Override
    @Transactional
    public TipoDocDTO save(TipoDocDTO dto) throws ServiceException {
        try {
            TipoDoc entity = mapper.toEntity(dto);
            entity.setEstado("A");
            TipoDoc saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el tipo de documento", e);
        }
    }

    @Override
    @Transactional
    public TipoDocDTO update(TipoDocDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de tipo de documento no proporcionado");
            }

            TipoDoc existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Tipo de documento no encontrado con ID: " + dto.getId()));

            TipoDoc updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            TipoDoc saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el tipo de documento", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            TipoDoc t = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Tipo de documento no encontrado con ID: " + id));
            t.setEstado("I");
            repository.save(t);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente el tipo de documento", e);
        }
    }
}