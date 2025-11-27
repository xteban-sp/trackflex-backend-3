package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.DocumentoServicioDTO;
import com.rutaflex.trackflexv3.entity.DocumentoServicio;
import com.rutaflex.trackflexv3.mapper.DocumentoServicioMapper;
import com.rutaflex.trackflexv3.repository.DocumentoServicioRepository;
import com.rutaflex.trackflexv3.service.general.service.DocumentoServicioService;
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
public class DocumentoServicioServiceImpl implements DocumentoServicioService {

    private final DocumentoServicioRepository repository;
    private final DocumentoServicioMapper mapper;

    @Override
    public List<DocumentoServicioDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar documentos de servicio", e);
        }
    }

    @Override
    public Optional<DocumentoServicioDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id).map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar documento de servicio con ID: " + id, e);
        }
    }

    @Override
    public List<DocumentoServicioDTO> findByObject(DocumentoServicioDTO dto) throws ServiceException {
        if (dto.getServicioId() != null) {
            return repository.findByServicioId(dto.getServicioId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public DocumentoServicioDTO save(DocumentoServicioDTO dto) throws ServiceException {
        try {
            if (dto.getServicioId() == null) {
                throw new ServiceException("El ID del servicio es obligatorio");
            }
            if (dto.getTipoDocumento() == null || dto.getTipoDocumento().isBlank()) {
                throw new ServiceException("El tipo de documento es obligatorio");
            }
            DocumentoServicio entity = mapper.toEntity(dto);
            entity.setFechaSubida(LocalDate.now());
            DocumentoServicio saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el documento de servicio", e);
        }
    }

    @Override
    @Transactional
    public DocumentoServicioDTO update(DocumentoServicioDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de documento no proporcionado");
            }

            DocumentoServicio existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Documento no encontrado con ID: " + dto.getId()));

            // RF40: Solo se puede editar hasta 24h después del servicio
            // (validación se haría en un servicio de alto nivel, no aquí)

            DocumentoServicio updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setFechaSubida(existing.getFechaSubida()); // mantener fecha original
            DocumentoServicio saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el documento de servicio", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            DocumentoServicio d = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Documento no encontrado con ID: " + id));
            repository.delete(d); // Sin campo 'estado', eliminación física
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar el documento de servicio", e);
        }
    }
}