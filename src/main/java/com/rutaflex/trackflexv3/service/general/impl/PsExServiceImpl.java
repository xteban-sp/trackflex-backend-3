package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.PsExDTO;
import com.rutaflex.trackflexv3.entity.PsEx;
import com.rutaflex.trackflexv3.mapper.PsExMapper;
import com.rutaflex.trackflexv3.repository.PsExRepository;
import com.rutaflex.trackflexv3.service.general.service.PsExService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PsExServiceImpl implements PsExService {

    private final PsExRepository repository;
    private final PsExMapper mapper;

    @Override
    public List<PsExDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar PSEX activos", e);
        }
    }

    @Override
    public Optional<PsExDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(p -> "A".equals(p.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar PSEX con ID: " + id, e);
        }
    }

    @Override
    public List<PsExDTO> findByObject(PsExDTO dto) throws ServiceException {
        if (dto.getAlmacenId() != null) {
            return repository.findByAlmacenId(dto.getAlmacenId()).stream()
                    .filter(p -> "A".equals(p.getEstado()))
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public PsExDTO save(PsExDTO dto) throws ServiceException {
        try {
            if (dto.getAlmacenId() == null) {
                throw new ServiceException("El almacén es obligatorio");
            }
            PsEx entity = mapper.toEntity(dto);
            entity.setEstado("A");
            PsEx saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el PSEX", e);
        }
    }

    @Override
    @Transactional
    public PsExDTO update(PsExDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de PSEX no proporcionado");
            }

            PsEx existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("PSEX no encontrado con ID: " + dto.getId()));

            PsEx updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            PsEx saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el PSEX", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            PsEx p = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("PSEX no encontrado con ID: " + id));
            p.setEstado("I");
            repository.save(p);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al dar de baja el PSEX", e);
        }
    }
}