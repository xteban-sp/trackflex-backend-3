package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import com.rutaflex.trackflexv3.entity.Vehiculo;
import com.rutaflex.trackflexv3.mapper.VehiculoMapper;
import com.rutaflex.trackflexv3.repository.VehiculoRepository;
import com.rutaflex.trackflexv3.service.general.service.VehiculoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository repository;
    private final VehiculoMapper mapper;

    @Override
    public List<VehiculoDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar vehículos activos", e);
        }
    }
    @Override
    public List<VehiculoDTO> findByIds(List<Long> ids) {
        // Asegura que solo se devuelvan vehículos activos que están en la lista de IDs
        List<Vehiculo> vehiculos = repository.findByIdInAndEstado(ids, "A");
        return vehiculos.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<VehiculoDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(v -> "A".equals(v.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar vehículo con ID: " + id, e);
        }
    }

    @Override
    public List<VehiculoDTO> findByObject(VehiculoDTO dto) throws ServiceException {
        // Solo devuelve activos; el filtrado por fecha se hace en MantenimientoService
        return findAll();
    }

    @Override
    @Transactional
    public VehiculoDTO save(VehiculoDTO dto) throws ServiceException {
        try {
            if (repository.existsByPlaca(dto.getPlaca())) {
                throw new ServiceException("Ya existe un vehículo con la placa: " + dto.getPlaca());
            }
            Vehiculo entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Vehiculo saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el vehículo", e);
        }
    }

    @Override
    @Transactional
    public VehiculoDTO update(VehiculoDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de vehículo no proporcionado");
            }

            Vehiculo existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Vehículo no encontrado con ID: " + dto.getId()));

            if (!existing.getPlaca().equals(dto.getPlaca()) && repository.existsByPlaca(dto.getPlaca())) {
                throw new ServiceException("Ya existe otro vehículo con la placa: " + dto.getPlaca());
            }

            Vehiculo updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            Vehiculo saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el vehículo", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Vehiculo v = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Vehículo no encontrado con ID: " + id));
            v.setEstado("I"); // Baja definitiva
            repository.save(v);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al dar de baja el vehículo", e);
        }
    }
    @Override
    public List<VehiculoDTO> findDisponibles(LocalDate fecha, Long nivelId) {
        List<Vehiculo> vehiculos = repository.findDisponibles(fecha, nivelId);
        return vehiculos.stream()
                .map(mapper::toDTO) // o como conviertas en tu proyecto
                .collect(Collectors.toList());
    }
}