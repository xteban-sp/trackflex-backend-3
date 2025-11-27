package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.GastoAdicionalDTO;
import com.rutaflex.trackflexv3.entity.GastoAdicional;
import com.rutaflex.trackflexv3.mapper.GastoAdicionalMapper;
import com.rutaflex.trackflexv3.repository.GastoAdicionalRepository;
import com.rutaflex.trackflexv3.service.general.service.GastoAdicionalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GastoAdicionalServiceImpl implements GastoAdicionalService {

    private final GastoAdicionalRepository repository;
    private final GastoAdicionalMapper mapper;

    @Override
    public List<GastoAdicionalDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar gastos adicionales", e);
        }
    }

    @Override
    public Optional<GastoAdicionalDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar gasto adicional con ID: " + id, e);
        }
    }

    @Override
    public List<GastoAdicionalDTO> findByObject(GastoAdicionalDTO dto) throws ServiceException {
        if (dto.getServicioId() != null) {
            return repository.findByServicioId(dto.getServicioId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public GastoAdicionalDTO save(GastoAdicionalDTO dto) throws ServiceException {
        try {
            // Validación: servicio y tipo de gasto son obligatorios
            if (dto.getServicioId() == null) {
                throw new ServiceException("El ID del servicio es obligatorio");
            }
            if (dto.getTipoGastoId() == null) {
                throw new ServiceException("El tipo de gasto es obligatorio");
            }
            if (dto.getMonto() == null || dto.getMonto().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new ServiceException("El monto debe ser mayor que cero");
            }

            GastoAdicional entity = mapper.toEntity(dto);
            GastoAdicional saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el gasto adicional", e);
        }
    }

    @Override
    @Transactional
    public GastoAdicionalDTO update(GastoAdicionalDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de gasto adicional no proporcionado");
            }

            GastoAdicional existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Gasto adicional no encontrado con ID: " + dto.getId()));

            // Validaciones
            if (dto.getMonto() != null && dto.getMonto().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new ServiceException("El monto debe ser mayor que cero");
            }

            GastoAdicional updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            // Nota: en tu BD no hay 'estado', así que no se maneja eliminación lógica aquí
            GastoAdicional saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el gasto adicional", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            GastoAdicional gasto = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Gasto adicional no encontrado con ID: " + id));
            // ⚠️ Tabla `gasto_adicional` NO tiene columna `estado`
            // Por lo tanto, no se puede hacer eliminación lógica → se elimina físicamente
            repository.delete(gasto);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar el gasto adicional", e);
        }
    }
}