package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.DistritoDTO;
import com.rutaflex.trackflexv3.service.general.service.DistritoService;
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
@RequestMapping("/distritos")
public class DistritoController {

    private final DistritoService distritoService;

    @PreAuthorize("hasAuthority('GET_ALL_DISTRITOS')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<DistritoDTO> lista = distritoService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar distritos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_DISTRITO')")
    @GetMapping("/{id}")
    public ResponseEntity<DistritoDTO> obtenerPorId(@PathVariable Long id) {
        return distritoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_DISTRITO')")
    @PostMapping
    public ResponseEntity<DistritoDTO> crear(@Valid @RequestBody DistritoDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        DistritoDTO nuevo = distritoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_DISTRITO')")
    @PutMapping("/{id}")
    public ResponseEntity<DistritoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody DistritoDTO dto) {
        distritoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito con ID " + id + " no encontrado"));
        dto.setId(id);
        DistritoDTO actualizado = distritoService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_DISTRITO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        distritoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito con ID " + id + " no existe"));
        distritoService.deleteLogic(id);
        return ResponseEntity.ok("Distrito desactivado correctamente");
    }
}