package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.TarifarioDTO;
import com.rutaflex.trackflexv3.entity.Tarifario;
import com.rutaflex.trackflexv3.mapper.TarifarioMapper;
import com.rutaflex.trackflexv3.repository.TarifarioRepository;
import com.rutaflex.trackflexv3.service.general.service.TarifarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TarifarioServiceImpl implements TarifarioService {

    private final TarifarioRepository repository;
    private final TarifarioMapper mapper;

    @Override
    public List<TarifarioDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar tarifarios", e);
        }
    }

    @Override
    public Optional<TarifarioDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar tarifario con ID: " + id, e);
        }
    }

    @Override
    public List<TarifarioDTO> findByObject(TarifarioDTO dto) throws ServiceException {
        if (dto.getNivel() != null && dto.getRuta() != null && dto.getTipo() != null) {
            List<Tarifario> list = repository.findByNivelIdAndRutaIdAndTipo(
                    dto.getNivel().getId(),
                    dto.getRuta().getId(),
                    dto.getTipo()
            );
            return list.stream().map(mapper::toDTO).collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public TarifarioDTO save(TarifarioDTO dto) throws ServiceException {
        try {
            if (dto.getNivel() == null || dto.getRuta() == null || dto.getTipo() == null) {
                throw new ServiceException("Nivel, ruta y tipo son obligatorios");
            }
            Tarifario entity = mapper.toEntity(dto);
            Tarifario saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el tarifario", e);
        }
    }

    @Override
    @Transactional
    public TarifarioDTO update(TarifarioDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de tarifario no proporcionado");
            }

            Tarifario existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Tarifario no encontrado con ID: " + dto.getId()));

            Tarifario updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            Tarifario saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el tarifario", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Tarifario t = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Tarifario no encontrado con ID: " + id));
            t.setEstado("I"); // si tu tabla tarifario tiene 'estado'
            repository.save(t);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente el tarifario", e);
        }
    }
}