package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.ControlDTO;
import com.rutaflex.trackflexv3.service.general.service.ControlService;
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
@RequestMapping("/controles")
public class ControlController {

    private final ControlService controlService;

    @PreAuthorize("hasAuthority('GET_ALL_CONTROLES')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<ControlDTO> lista = controlService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar controles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_CONTROL')")
    @GetMapping("/{id}")
    public ResponseEntity<ControlDTO> obtenerPorId(@PathVariable Long id) {
        return controlService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Control con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_CONTROL')")
    @PostMapping
    public ResponseEntity<ControlDTO> crear(@Valid @RequestBody ControlDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        ControlDTO nuevo = controlService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_CONTROL')")
    @PutMapping("/{id}")
    public ResponseEntity<ControlDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ControlDTO dto) {
        controlService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Control con ID " + id + " no encontrado"));
        dto.setId(id);
        ControlDTO actualizado = controlService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_CONTROL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        controlService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Control con ID " + id + " no existe"));
        controlService.deleteLogic(id);
        return ResponseEntity.ok("Control eliminado");
    }
}