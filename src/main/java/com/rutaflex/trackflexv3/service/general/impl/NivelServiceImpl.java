package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.NivelDTO;
import com.rutaflex.trackflexv3.entity.Nivel;
import com.rutaflex.trackflexv3.mapper.NivelMapper;
import com.rutaflex.trackflexv3.repository.NivelRepository;
import com.rutaflex.trackflexv3.service.general.service.NivelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NivelServiceImpl implements NivelService {

    private final NivelRepository repository;
    private final NivelMapper mapper;

    @Override
    public List<NivelDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar niveles", e);
        }
    }

    @Override
    public Optional<NivelDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar nivel con ID: " + id, e);
        }
    }

    @Override
    public List<NivelDTO> findByObject(NivelDTO dto) throws ServiceException {
        return findAll();
    }

    @Override
    @Transactional
    public NivelDTO save(NivelDTO dto) throws ServiceException {
        try {
            if (repository.existsByCodigo(dto.getCodigo())) {
                throw new ServiceException("Ya existe un nivel con el código: " + dto.getCodigo());
            }
            if (dto.getM3Min() > dto.getM3Max()) {
                throw new ServiceException("m3Min no puede ser mayor que m3Max");
            }
            Nivel entity = mapper.toEntity(dto);
            Nivel saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el nivel", e);
        }
    }

    @Override
    @Transactional
    public NivelDTO update(NivelDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de nivel no proporcionado");
            }

            Nivel existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Nivel no encontrado con ID: " + dto.getId()));

            if (!existing.getCodigo().equals(dto.getCodigo()) && repository.existsByCodigo(dto.getCodigo())) {
                throw new ServiceException("Ya existe otro nivel con el código: " + dto.getCodigo());
            }
            if (dto.getM3Min() > dto.getM3Max()) {
                throw new ServiceException("m3Min no puede ser mayor que m3Max");
            }

            Nivel updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            Nivel saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el nivel", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            // Lógica de eliminación: no hay 'estado', pero podrías marcar como inactivo
            // Por ahora, no se permite eliminación (los niveles son fijos)
            throw new ServiceException("No se permite eliminar niveles");
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Operación no permitida", e);
        }
    }
}