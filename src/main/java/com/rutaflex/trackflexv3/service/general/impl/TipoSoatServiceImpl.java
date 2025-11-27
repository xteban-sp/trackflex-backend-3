package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.TipoSoatDTO;
import com.rutaflex.trackflexv3.entity.TipoSoat;
import com.rutaflex.trackflexv3.mapper.TipoSoatMapper;
import com.rutaflex.trackflexv3.repository.TipoSoatRepository;
import com.rutaflex.trackflexv3.service.general.service.TipoSoatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TipoSoatServiceImpl implements TipoSoatService {

    private final TipoSoatRepository repository;
    private final TipoSoatMapper mapper;

    @Override
    public List<TipoSoatDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar tipos de SOAT", e);
        }
    }

    @Override
    public Optional<TipoSoatDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(t -> "A".equals(t.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar tipo de SOAT con ID: " + id, e);
        }
    }

    @Override
    public List<TipoSoatDTO> findByObject(TipoSoatDTO dto) throws ServiceException {
        return findAll();
    }

    @Override
    @Transactional
    public TipoSoatDTO save(TipoSoatDTO dto) throws ServiceException {
        try {
            TipoSoat entity = mapper.toEntity(dto);
            entity.setEstado("A");
            TipoSoat saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el tipo de SOAT", e);
        }
    }

    @Override
    @Transactional
    public TipoSoatDTO update(TipoSoatDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de tipo de SOAT no proporcionado");
            }

            TipoSoat existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Tipo de SOAT no encontrado con ID: " + dto.getId()));

            TipoSoat updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            TipoSoat saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el tipo de SOAT", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            TipoSoat t = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Tipo de SOAT no encontrado con ID: " + id));
            t.setEstado("I");
            repository.save(t);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente el tipo de SOAT", e);
        }
    }
}