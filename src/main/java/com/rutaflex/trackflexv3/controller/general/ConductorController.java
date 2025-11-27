package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.ConductorDTO;
import com.rutaflex.trackflexv3.service.general.service.ConductorService;
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
@RequestMapping("/conductores")
public class ConductorController {

    private final ConductorService conductorService;

    @PreAuthorize("hasAuthority('GET_ALL_CONDUCTORES')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<ConductorDTO> lista = conductorService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar conductores", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_CONDUCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ConductorDTO> obtenerPorId(@PathVariable Long id) {
        return conductorService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_CONDUCTOR')")
    @PostMapping
    public ResponseEntity<ConductorDTO> crear(@Valid @RequestBody ConductorDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        ConductorDTO nuevo = conductorService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_CONDUCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ConductorDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ConductorDTO dto) {
        conductorService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor con ID " + id + " no encontrado"));
        dto.setId(id);
        ConductorDTO actualizado = conductorService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_CONDUCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        conductorService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor con ID " + id + " no existe"));
        conductorService.deleteLogic(id);
        return ResponseEntity.ok("Conductor desactivado correctamente");
    }
}