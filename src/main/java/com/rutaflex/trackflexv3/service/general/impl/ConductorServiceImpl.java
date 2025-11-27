package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.ConductorDTO;
import com.rutaflex.trackflexv3.entity.Conductor;
import com.rutaflex.trackflexv3.mapper.ConductorMapper;
import com.rutaflex.trackflexv3.repository.ConductorRepository;
import com.rutaflex.trackflexv3.service.general.service.ConductorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ConductorServiceImpl implements ConductorService {

    private final ConductorRepository repository;
    private final ConductorMapper mapper;

    @Override
    public List<ConductorDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar conductores activos", e);
        }
    }

    @Override
    public Optional<ConductorDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(c -> "A".equals(c.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar conductor con ID: " + id, e);
        }
    }

    @Override
    public List<ConductorDTO> findByObject(ConductorDTO dto) throws ServiceException {
        return findAll();
    }

    @Override
    @Transactional
    public ConductorDTO save(ConductorDTO dto) throws ServiceException {
        try {
            if (dto.getDni() != null && repository.existsByDni(dto.getDni())) {
                throw new ServiceException("Ya existe un conductor con el DNI: " + dto.getDni());
            }
            if (dto.getLicencia() != null && repository.existsByLicencia(dto.getLicencia())) {
                throw new ServiceException("Ya existe un conductor con la licencia: " + dto.getLicencia());
            }
            Conductor entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Conductor saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el conductor", e);
        }
    }

    @Override
    @Transactional
    public ConductorDTO update(ConductorDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de conductor no proporcionado");
            }

            Conductor existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Conductor no encontrado con ID: " + dto.getId()));

            if (dto.getDni() != null && !existing.getDni().equals(dto.getDni()) && repository.existsByDni(dto.getDni())) {
                throw new ServiceException("Ya existe otro conductor con el DNI: " + dto.getDni());
            }
            if (dto.getLicencia() != null && !existing.getLicencia().equals(dto.getLicencia()) && repository.existsByLicencia(dto.getLicencia())) {
                throw new ServiceException("Ya existe otro conductor con la licencia: " + dto.getLicencia());
            }

            Conductor updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            Conductor saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el conductor", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Conductor c = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Conductor no encontrado con ID: " + id));
            c.setEstado("I");
            repository.save(c);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al dar de baja al conductor", e);
        }
    }
}