package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.DocumentoServicioDTO;
import com.rutaflex.trackflexv3.service.general.service.DocumentoServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/documentos-servicio")
public class DocumentoServicioController {

    private final DocumentoServicioService documentoServicioService;

    @PreAuthorize("hasAuthority('GET_ALL_DOC_SERVICIO')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<DocumentoServicioDTO> lista = documentoServicioService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar documentos de servicio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_DOC_SERVICIO')")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoServicioDTO> obtenerPorId(@PathVariable Long id) {
        return documentoServicioService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Documento de servicio con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_DOC_SERVICIO')")
    @PostMapping
    public ResponseEntity<DocumentoServicioDTO> crear(@Valid @RequestBody DocumentoServicioDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        DocumentoServicioDTO nuevo = documentoServicioService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_DOC_SERVICIO')")
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoServicioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody DocumentoServicioDTO dto) {
        documentoServicioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento de servicio con ID " + id + " no encontrado"));
        dto.setId(id);
        DocumentoServicioDTO actualizado = documentoServicioService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_DOC_SERVICIO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        documentoServicioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento de servicio con ID " + id + " no existe"));
        documentoServicioService.deleteLogic(id);
        return ResponseEntity.ok("Documento de servicio eliminado");
    }

    // RF40, RF46: Documentos por servicio
    @PreAuthorize("hasAuthority('GET_DOC_SERVICIO_POR_SERVICIO')")
    @GetMapping("/por-servicio/{servicioId}")
    public ResponseEntity<?> getPorServicio(@PathVariable Long servicioId) {
        try {
            DocumentoServicioDTO filtro = new DocumentoServicioDTO();
            filtro.setServicioId(servicioId);
            List<DocumentoServicioDTO> lista = documentoServicioService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar documentos por servicio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}