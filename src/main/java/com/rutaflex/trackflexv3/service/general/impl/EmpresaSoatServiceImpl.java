package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.EmpresaSoatDTO;
import com.rutaflex.trackflexv3.entity.EmpresaSoat;
import com.rutaflex.trackflexv3.mapper.EmpresaSoatMapper;
import com.rutaflex.trackflexv3.repository.EmpresaSoatRepository;
import com.rutaflex.trackflexv3.service.general.service.EmpresaSoatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmpresaSoatServiceImpl implements EmpresaSoatService {

    private final EmpresaSoatRepository repository;
    private final EmpresaSoatMapper mapper;

    @Override
    public List<EmpresaSoatDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar empresas SOAT activas", e);
        }
    }

    @Override
    public Optional<EmpresaSoatDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(e -> "A".equals(e.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar empresa SOAT con ID: " + id, e);
        }
    }

    @Override
    public List<EmpresaSoatDTO> findByObject(EmpresaSoatDTO dto) throws ServiceException {
        return findAll();
    }

    @Override
    @Transactional
    public EmpresaSoatDTO save(EmpresaSoatDTO dto) throws ServiceException {
        try {
            EmpresaSoat entity = mapper.toEntity(dto);
            entity.setEstado("A");
            EmpresaSoat saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar la empresa SOAT", e);
        }
    }

    @Override
    @Transactional
    public EmpresaSoatDTO update(EmpresaSoatDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de empresa SOAT no proporcionado");
            }

            EmpresaSoat existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Empresa SOAT no encontrada con ID: " + dto.getId()));

            EmpresaSoat updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            EmpresaSoat saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar la empresa SOAT", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            EmpresaSoat e = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Empresa SOAT no encontrada con ID: " + id));
            e.setEstado("I");
            repository.save(e);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al dar de baja la empresa SOAT", e);
        }
    }
}