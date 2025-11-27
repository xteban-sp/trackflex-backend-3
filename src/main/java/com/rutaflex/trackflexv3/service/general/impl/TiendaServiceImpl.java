package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.TiendaDTO;
import com.rutaflex.trackflexv3.entity.Tienda;
import com.rutaflex.trackflexv3.mapper.TiendaMapper;
import com.rutaflex.trackflexv3.repository.TiendaRepository;
import com.rutaflex.trackflexv3.service.general.service.TiendaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TiendaServiceImpl implements TiendaService {

    private final TiendaRepository repository;
    private final TiendaMapper mapper;

    @Override
    public List<TiendaDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar tiendas activas", e);
        }
    }

    @Override
    public Optional<TiendaDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(t -> "A".equals(t.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar tienda con ID: " + id, e);
        }
    }

    @Override
    public List<TiendaDTO> findByObject(TiendaDTO dto) throws ServiceException {
        if (dto.getDistritoId() != null) {
            return repository.findByDistritoId(dto.getDistritoId()).stream()
                    .filter(t -> "A".equals(t.getEstado()))
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        if (dto.getCodDestinatario() != null) {
            return repository.findByCodDestinatarioAndEstado(dto.getCodDestinatario(), "A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public TiendaDTO save(TiendaDTO dto) throws ServiceException {
        try {
            if (dto.getCodDestinatario() != null && repository.existsByCodDestinatario(dto.getCodDestinatario())) {
                throw new ServiceException("Ya existe una tienda con el código destinatario: " + dto.getCodDestinatario());
            }
            if (repository.existsByNombre(dto.getNombre())) {
                throw new ServiceException("Ya existe una tienda con el nombre: " + dto.getNombre());
            }
            Tienda entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Tienda saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar la tienda", e);
        }
    }

    @Override
    @Transactional
    public TiendaDTO update(TiendaDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de tienda no proporcionado");
            }

            Tienda existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Tienda no encontrada con ID: " + dto.getId()));

            if (dto.getCodDestinatario() != null && !existing.getCodDestinatario().equals(dto.getCodDestinatario()) &&
                    repository.existsByCodDestinatario(dto.getCodDestinatario())) {
                throw new ServiceException("Ya existe otra tienda con el código destinatario: " + dto.getCodDestinatario());
            }
            if (!existing.getNombre().equals(dto.getNombre()) && repository.existsByNombre(dto.getNombre())) {
                throw new ServiceException("Ya existe otra tienda con el nombre: " + dto.getNombre());
            }

            Tienda updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            Tienda saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar la tienda", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Tienda t = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Tienda no encontrada con ID: " + id));
            t.setEstado("I");
            repository.save(t);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al dar de baja la tienda", e);
        }
    }
}