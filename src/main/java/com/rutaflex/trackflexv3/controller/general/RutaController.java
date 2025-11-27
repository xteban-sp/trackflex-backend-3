package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.RutaDTO;
import com.rutaflex.trackflexv3.service.general.service.RutaService;
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
@RequestMapping("/rutas")
public class RutaController {

    private final RutaService rutaService;

    @PreAuthorize("hasAuthority('GET_ALL_RUTAS')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<RutaDTO> lista = rutaService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar rutas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_RUTA')")
    @GetMapping("/{id}")
    public ResponseEntity<RutaDTO> obtenerPorId(@PathVariable Long id) {
        return rutaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_RUTA')")
    @PostMapping
    public ResponseEntity<RutaDTO> crear(@Valid @RequestBody RutaDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        RutaDTO nuevo = rutaService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_RUTA')")
    @PutMapping("/{id}")
    public ResponseEntity<RutaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody RutaDTO dto) {
        rutaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta con ID " + id + " no encontrado"));
        dto.setId(id);
        RutaDTO actualizado = rutaService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_RUTA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        rutaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta con ID " + id + " no existe"));
        rutaService.deleteLogic(id);
        return ResponseEntity.ok("Ruta desactivada correctamente");
    }
}