package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.SoatDTO;
import com.rutaflex.trackflexv3.entity.Soat;
import com.rutaflex.trackflexv3.mapper.SoatMapper;
import com.rutaflex.trackflexv3.repository.SoatRepository;
import com.rutaflex.trackflexv3.service.general.service.SoatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SoatServiceImpl implements SoatService {

    private final SoatRepository repository;
    private final SoatMapper mapper;

    @Override
    public List<SoatDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar SOATs", e);
        }
    }

    @Override
    public Optional<SoatDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar SOAT con ID: " + id, e);
        }
    }

    @Override
    public List<SoatDTO> findByObject(SoatDTO dto) throws ServiceException {
        if (dto.getVehiculoId() != null) {
            return repository.findByVehiculoId(dto.getVehiculoId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        if (dto.getEmpresaSoatId() != null) {
            return repository.findByEmpresaSoatId(dto.getEmpresaSoatId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public SoatDTO save(SoatDTO dto) throws ServiceException {
        try {
            if (dto.getVehiculoId() == null || dto.getEmpresaSoatId() == null || dto.getTipoSoatId() == null) {
                throw new ServiceException("Vehículo, empresa SOAT y tipo SOAT son obligatorios");
            }
            Soat entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Soat saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el SOAT", e);
        }
    }

    @Override
    @Transactional
    public SoatDTO update(SoatDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de SOAT no proporcionado");
            }

            Soat existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("SOAT no encontrado con ID: " + dto.getId()));

            Soat updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            Soat saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el SOAT", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Soat s = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("SOAT no encontrado con ID: " + id));
            s.setEstado("I");
            repository.save(s);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar lógicamente el SOAT", e);
        }
    }
}