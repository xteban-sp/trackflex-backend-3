package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.TipoSoatDTO;
import com.rutaflex.trackflexv3.service.general.service.TipoSoatService;
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
@RequestMapping("/tipos-soat")
public class TipoSoatController {

    private final TipoSoatService tipoSoatService;

    @PreAuthorize("hasAuthority('GET_ALL_TIPOS_SOAT')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<TipoSoatDTO> lista = tipoSoatService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar tipos de SOAT", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_TIPO_SOAT')")
    @GetMapping("/{id}")
    public ResponseEntity<TipoSoatDTO> obtenerPorId(@PathVariable Long id) {
        return tipoSoatService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de SOAT con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_TIPO_SOAT')")
    @PostMapping
    public ResponseEntity<TipoSoatDTO> crear(@Valid @RequestBody TipoSoatDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        TipoSoatDTO nuevo = tipoSoatService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_TIPO_SOAT')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoSoatDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TipoSoatDTO dto) {
        tipoSoatService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de SOAT con ID " + id + " no encontrado"));
        dto.setId(id);
        TipoSoatDTO actualizado = tipoSoatService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_TIPO_SOAT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        tipoSoatService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de SOAT con ID " + id + " no existe"));
        tipoSoatService.deleteLogic(id);
        return ResponseEntity.ok("Tipo de SOAT desactivado correctamente");
    }
}