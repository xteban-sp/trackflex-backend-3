package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.DocumentoEntregaDTO;
import com.rutaflex.trackflexv3.service.general.service.DocumentoEntregaService;
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
@RequestMapping("/documentos-entrega")
public class DocumentoEntregaController {

    private final DocumentoEntregaService documentoEntregaService;

    @PreAuthorize("hasAuthority('GET_ALL_DOC_ENTREGA')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<DocumentoEntregaDTO> lista = documentoEntregaService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar documentos de entrega", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_DOC_ENTREGA')")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoEntregaDTO> obtenerPorId(@PathVariable Long id) {
        return documentoEntregaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Documento de entrega con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_DOC_ENTREGA')")
    @PostMapping
    public ResponseEntity<DocumentoEntregaDTO> crear(@Valid @RequestBody DocumentoEntregaDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        DocumentoEntregaDTO nuevo = documentoEntregaService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_DOC_ENTREGA')")
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoEntregaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody DocumentoEntregaDTO dto) {
        documentoEntregaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento de entrega con ID " + id + " no encontrado"));
        dto.setId(id);
        DocumentoEntregaDTO actualizado = documentoEntregaService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_DOC_ENTREGA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        documentoEntregaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento de entrega con ID " + id + " no existe"));
        documentoEntregaService.deleteLogic(id);
        return ResponseEntity.ok("Documento de entrega eliminado");
    }

    // RF19, RF35: Documentos por entrega
    @PreAuthorize("hasAuthority('GET_DOC_ENTREGA_POR_ENTREGA')")
    @GetMapping("/por-entrega/{entregaId}")
    public ResponseEntity<?> getPorEntrega(@PathVariable Long entregaId) {
        try {
            DocumentoEntregaDTO filtro = new DocumentoEntregaDTO();
            filtro.setEntregaId(entregaId);
            List<DocumentoEntregaDTO> lista = documentoEntregaService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar documentos por entrega", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}