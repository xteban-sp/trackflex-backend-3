package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.MantenimientoDTO;
import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import com.rutaflex.trackflexv3.entity.Mantenimiento;
import com.rutaflex.trackflexv3.entity.Vehiculo;
import com.rutaflex.trackflexv3.mapper.MantenimientoMapper;
import com.rutaflex.trackflexv3.mapper.VehiculoMapper;
import com.rutaflex.trackflexv3.repository.MantenimientoRepository;
import com.rutaflex.trackflexv3.repository.VehiculoRepository;
import com.rutaflex.trackflexv3.service.general.service.MantenimientoService;
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
public class MantenimientoServiceImpl implements MantenimientoService {

    private final MantenimientoRepository repository;
    private final MantenimientoMapper mapper;
    private final VehiculoRepository vehiculoRepository;
    private final VehiculoMapper vehiculoMapper;

    @Override
    public List<MantenimientoDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar mantenimientos", e);
        }
    }

    @Override
    public Optional<MantenimientoDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar mantenimiento con ID: " + id, e);
        }
    }

    @Override
    public List<MantenimientoDTO> findByObject(MantenimientoDTO dto) throws ServiceException {
        if (dto.getVehiculoId() != null) {
            return repository.findByVehiculoId(dto.getVehiculoId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public MantenimientoDTO save(MantenimientoDTO dto) throws ServiceException {
        try {
            // Validar vehículo exista y esté activo
            vehiculoRepository.findById(dto.getVehiculoId())
                    .orElseThrow(() -> new ServiceException("Vehículo no encontrado"));

            // RF62: Validar que no haya otro mantenimiento el mismo día
            if (repository.existsByVehiculoIdAndFechaAndEstado(dto.getVehiculoId(), dto.getFecha(), "A")) {
                throw new ServiceException("Ya existe un mantenimiento programado para este vehículo en la fecha: " + dto.getFecha());
            }

            Mantenimiento entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Mantenimiento saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al programar el mantenimiento", e);
        }
    }

    @Override
    @Transactional
    public MantenimientoDTO update(MantenimientoDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de mantenimiento no proporcionado");
            }

            Mantenimiento existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Mantenimiento no encontrado con ID: " + dto.getId()));

            // Validar unicidad de fecha para el vehículo (excluyendo el propio registro)
            boolean conflicto = repository.existsByVehiculoIdAndFechaAndEstado(dto.getVehiculoId(), dto.getFecha(), "A");
            if (conflicto && !existing.getFecha().equals(dto.getFecha())) {
                throw new ServiceException("Ya existe un mantenimiento programado para este vehículo en la fecha: " + dto.getFecha());
            }

            Mantenimiento updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            Mantenimiento saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el mantenimiento", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Mantenimiento m = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Mantenimiento no encontrado con ID: " + id));
            m.setEstado("I");
            repository.save(m);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente el mantenimiento", e);
        }
    }

    // RF67, RF68: Cancelar mantenimiento = eliminar lógico
    @Override
    @Transactional
    public void cancelarMantenimiento(Long id) throws ServiceException {
        deleteLogic(id); // Reutiliza eliminación lógica
    }

    // RF01, RF03: Verificar disponibilidad de un vehículo en una fecha específica
    @Override
    public boolean isVehiculoDisponibleEnFecha(Long vehiculoId, LocalDate fecha) throws ServiceException {
        try {
            return !repository.existsByVehiculoIdAndFechaAndEstado(vehiculoId, fecha, "A");
        } catch (Exception e) {
            throw new ServiceException("Error al verificar disponibilidad del vehículo", e);
        }
    }

    // RF01, RF03, RF05: Listar vehículos disponibles para una fecha
    @Override
    public List<VehiculoDTO> findVehiculosDisponiblesEnFecha(LocalDate fecha) throws ServiceException {
        try {
            List<Vehiculo> vehiculosActivos = vehiculoRepository.findByEstado("A");
            return vehiculosActivos.stream()
                    .filter(v -> isVehiculoDisponibleEnFecha(v.getId(), fecha))
                    .map(vehiculoMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al consultar vehículos disponibles", e);
        }
    }
}