package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.ControlDTO;
import com.rutaflex.trackflexv3.entity.Control;
import com.rutaflex.trackflexv3.mapper.ControlMapper;
import com.rutaflex.trackflexv3.repository.ControlRepository;
import com.rutaflex.trackflexv3.service.general.service.ControlService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ControlServiceImpl implements ControlService {

    private final ControlRepository repository;
    private final ControlMapper mapper;

    @Override
    public List<ControlDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar registros de auditoría", e);
        }
    }

    @Override
    public Optional<ControlDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar registro de auditoría con ID: " + id, e);
        }
    }

    @Override
    public List<ControlDTO> findByObject(ControlDTO dto) throws ServiceException {
        return findAll();
    }

    @Override
    @Transactional
    public ControlDTO save(ControlDTO dto) throws ServiceException {
        try {
            Control entity = mapper.toEntity(dto);
            Control saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (Exception e) {
            throw new ServiceException("Error al registrar en auditoría", e);
        }
    }

    @Override
    @Transactional
    public ControlDTO update(ControlDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de control no proporcionado");
            }
            Control existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Registro de auditoría no encontrado"));
            Control updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            Control saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar registro de auditoría", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            repository.deleteById(id); // Auditoría: no suele tener eliminación lógica
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar registro de auditoría", e);
        }
    }
}