package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.TipoDocDTO;
import com.rutaflex.trackflexv3.service.general.service.TipoDocService;
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
@RequestMapping("/tipos-doc")
public class TipoDocController {

    private final TipoDocService tipoDocService;

    @PreAuthorize("hasAuthority('GET_ALL_TIPOS_DOC')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<TipoDocDTO> lista = tipoDocService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar tipos de documento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_TIPO_DOC')")
    @GetMapping("/{id}")
    public ResponseEntity<TipoDocDTO> obtenerPorId(@PathVariable Long id) {
        return tipoDocService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de documento con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_TIPO_DOC')")
    @PostMapping
    public ResponseEntity<TipoDocDTO> crear(@Valid @RequestBody TipoDocDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        TipoDocDTO nuevo = tipoDocService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_TIPO_DOC')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoDocDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TipoDocDTO dto) {
        tipoDocService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de documento con ID " + id + " no encontrado"));
        dto.setId(id);
        TipoDocDTO actualizado = tipoDocService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_TIPO_DOC')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        tipoDocService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de documento con ID " + id + " no existe"));
        tipoDocService.deleteLogic(id);
        return ResponseEntity.ok("Tipo de documento desactivado correctamente");
    }
}