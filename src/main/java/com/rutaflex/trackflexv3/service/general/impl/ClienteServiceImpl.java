package com.rutaflex.trackflexv3.service.general.impl;

import com.rutaflex.trackflexv3.dto.ClienteDTO;
import com.rutaflex.trackflexv3.entity.Cliente;
import com.rutaflex.trackflexv3.mapper.ClienteMapper;
import com.rutaflex.trackflexv3.repository.ClienteRepository;
import com.rutaflex.trackflexv3.service.general.service.ClienteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    @Override
    public List<ClienteDTO> findAll() throws ServiceException {
        try {
            return repository.findByEstado("A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al listar clientes activos", e);
        }
    }

    @Override
    public Optional<ClienteDTO> findById(Long id) throws ServiceException {
        try {
            return repository.findById(id)
                    .filter(c -> "A".equals(c.getEstado()))
                    .map(mapper::toDTO);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar cliente con ID: " + id, e);
        }
    }

    @Override
    public List<ClienteDTO> findByObject(ClienteDTO dto) throws ServiceException {
        if (dto.getRuc() != null && !dto.getRuc().isBlank()) {
            return repository.findByRucAndEstado(dto.getRuc(), "A").stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    @Override
    @Transactional
    public ClienteDTO save(ClienteDTO dto) throws ServiceException {
        try {
            if (dto.getRuc() != null && repository.existsByRuc(dto.getRuc())) {
                throw new ServiceException("Ya existe un cliente con el RUC: " + dto.getRuc());
            }
            if (repository.existsByRazonSocial(dto.getRazonSocial())) {
                throw new ServiceException("Ya existe un cliente con la razón social: " + dto.getRazonSocial());
            }
            Cliente entity = mapper.toEntity(dto);
            entity.setEstado("A");
            Cliente saved = repository.save(entity);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al registrar el cliente", e);
        }
    }

    @Override
    @Transactional
    public ClienteDTO update(ClienteDTO dto) throws ServiceException {
        try {
            if (dto.getId() == null) {
                throw new ServiceException("ID de cliente no proporcionado");
            }

            Cliente existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ServiceException("Cliente no encontrado con ID: " + dto.getId()));

            if (dto.getRuc() != null && !existing.getRuc().equals(dto.getRuc()) && repository.existsByRuc(dto.getRuc())) {
                throw new ServiceException("Ya existe otro cliente con el RUC: " + dto.getRuc());
            }
            if (!existing.getRazonSocial().equals(dto.getRazonSocial()) && repository.existsByRazonSocial(dto.getRazonSocial())) {
                throw new ServiceException("Ya existe otro cliente con la razón social: " + dto.getRazonSocial());
            }

            Cliente updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setEstado("A");
            Cliente saved = repository.save(updated);
            return mapper.toDTO(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el cliente", e);
        }
    }

    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Cliente c = repository.findById(id)
                    .orElseThrow(() -> new ServiceException("Cliente no encontrado con ID: " + id));
            c.setEstado("I");
            repository.save(c);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al dar de baja al cliente", e);
        }
    }
}