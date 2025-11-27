package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.DocumentoEntregaDTO;
import com.rutaflex.trackflexv3.entity.DocumentoEntrega;
import com.rutaflex.trackflexv3.mapper.DocumentoEntregaMapper;
import com.rutaflex.trackflexv3.repository.DocumentoEntregaRepository;
import com.rutaflex.trackflexv3.service.general.service.DocumentoEntregaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DocumentoEntregaServiceImpl implements DocumentoEntregaService {

    private final DocumentoEntregaRepository repository;
    private final DocumentoEntregaMapper mapper;

    @Override
    public List<DocumentoEntregaDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar documentos de entrega", e);
        }
    }

    @Override
    public Optional<DocumentoEntregaDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar documento de entrega con ID: " + id, e);
        }
    }

    @Override
    public List<DocumentoEntregaDTO> findByObject(DocumentoEntregaDTO dto) throws ServiceException {
        if (dto.getEntregaId() != null) {
            return repository.findByEntregaId(dto.getEntregaId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public DocumentoEntregaDTO save(DocumentoEntregaDTO dto) throws ServiceException {
        try {
            if (dto.getEntregaId() == null) {
                throw new ServiceException("El ID de entrega es obligatorio");
            }
            if (dto.getTipoDocumento() == null || dto.getTipoDocumento().isBlank()) {
                throw new ServiceException("El tipo de documento es obligatorio");
            }
            DocumentoEntrega entity = mapper.toEntity(dto);
            DocumentoEntrega saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el documento de entrega", e);
        }
    }

    @Override
    @Transactional
    public DocumentoEntregaDTO update(DocumentoEntregaDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de documento no proporcionado");
            }

            DocumentoEntrega existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Documento no encontrado con ID: " + dto.getId()));

            DocumentoEntrega updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            DocumentoEntrega saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el documento de entrega", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            DocumentoEntrega d = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Documento no encontrado con ID: " + id));
            d.setFechaSubida(null); // Opcional: marca como eliminado
            // Si hay campo 'estado', úsalo; sino, considera eliminación física
            repository.delete(d);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar el documento de entrega", e);
        }
    }
}