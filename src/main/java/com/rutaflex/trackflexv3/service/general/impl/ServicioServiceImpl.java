package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.ServicioDTO;
import com.rutaflex.trackflexv3.entity.Servicio;
import com.rutaflex.trackflexv3.mapper.ServicioMapper;
import com.rutaflex.trackflexv3.repository.ServicioRepository;
import com.rutaflex.trackflexv3.service.general.service.ServicioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository repository;
    private final ServicioMapper mapper;

    @Override
    public List<ServicioDTO> findAll() throws ServiceException {
        try {
            return repository.findByOrderByFechaDesc().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar servicios", e);
        }
    }

    @Override
    public Optional<ServicioDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar servicio con ID: " + id, e);
        }
    }

    @Override
    public List<ServicioDTO> findByObject(ServicioDTO dto) throws ServiceException {
        if (dto.getFecha() != null) {
            return repository.findByFecha(dto.getFecha()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        if ("Sin asignar".equals(dto.getEstadoRegistro())) {
            return repository.findByEstadoDescripcion("Sin asignar").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public ServicioDTO save(ServicioDTO dto) throws ServiceException {
        try {
            if (dto.getFecha() == null) {
                throw new ServiceException("La fecha del servicio es obligatoria");
            }
            Servicio entity = mapper.toEntity(dto);
            entity.setEstadoRegistro("A");
            Servicio saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el servicio", e);
        }
    }

    @Override
    @Transactional
    public ServicioDTO update(ServicioDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de servicio no proporcionado");
            }

            Servicio existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Servicio no encontrado con ID: " + dto.getId()));

            Servicio updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstadoRegistro("A");
            Servicio saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el servicio", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Servicio s = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Servicio no encontrado con ID: " + id));
            s.setEstadoRegistro("I");
            repository.save(s);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente el servicio", e);
        }
    }
}