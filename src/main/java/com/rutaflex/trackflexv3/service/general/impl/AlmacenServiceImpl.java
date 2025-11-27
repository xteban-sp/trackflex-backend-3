package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.AlmacenDTO;
import com.rutaflex.trackflexv3.entity.Almacen;
import com.rutaflex.trackflexv3.mapper.AlmacenMapper;
import com.rutaflex.trackflexv3.repository.AlmacenRepository;
import com.rutaflex.trackflexv3.service.general.service.AlmacenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AlmacenServiceImpl implements AlmacenService {

    private final AlmacenRepository repository;
    private final AlmacenMapper mapper;

    @Override
    public List<AlmacenDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar almacenes activos", e);
        }
    }

    @Override
    public Optional<AlmacenDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(a -> "A".equals(a.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar almacén con ID: " + id, e);
        }
    }

    @Override
    public List<AlmacenDTO> findByObject(AlmacenDTO dto) throws ServiceException {
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            return repository.findByNombre(dto.getNombre()).stream()
                    .filter(a -> "A".equals(a.getEstado()))
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public AlmacenDTO save(AlmacenDTO dto) throws ServiceException {
        try {
            if (repository.existsByNombre(dto.getNombre())) {
                throw new ServiceException("Ya existe un almacén con el nombre: " + dto.getNombre());
            }
            Almacen entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Almacen saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el almacén", e);
        }
    }

    @Override
    @Transactional
    public AlmacenDTO update(AlmacenDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de almacén no proporcionado");
            }

            Almacen existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Almacén no encontrado con ID: " + dto.getId()));

            if (!existing.getNombre().equals(dto.getNombre()) && repository.existsByNombre(dto.getNombre())) {
                throw new ServiceException("Ya existe otro almacén con el nombre: " + dto.getNombre());
            }

            Almacen updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            Almacen saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el almacén", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Almacen a = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Almacén no encontrado con ID: " + id));
            a.setEstado("I");
            repository.save(a);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al dar de baja el almacén", e);
        }
    }
}