package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.CanalDTO;
import com.rutaflex.trackflexv3.service.general.service.CanalService;
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
@RequestMapping("/canales")
public class CanalController {

    private final CanalService canalService;

    @PreAuthorize("hasAuthority('GET_ALL_CANALES')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<CanalDTO> lista = canalService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar canales", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_CANAL')")
    @GetMapping("/{id}")
    public ResponseEntity<CanalDTO> obtenerPorId(@PathVariable Long id) {
        return canalService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Canal con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_CANAL')")
    @PostMapping
    public ResponseEntity<CanalDTO> crear(@Valid @RequestBody CanalDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        CanalDTO nuevo = canalService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_CANAL')")
    @PutMapping("/{id}")
    public ResponseEntity<CanalDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CanalDTO dto) {
        canalService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canal con ID " + id + " no encontrado"));
        dto.setId(id);
        CanalDTO actualizado = canalService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_CANAL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        canalService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canal con ID " + id + " no existe"));
        canalService.deleteLogic(id);
        return ResponseEntity.ok("Canal desactivado correctamente");
    }
}