package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.TarifarioDTO;
import com.rutaflex.trackflexv3.service.general.service.TarifarioService;
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
@RequestMapping("/tarifarios")
public class TarifarioController {

    private final TarifarioService tarifarioService;

    @PreAuthorize("hasAuthority('GET_ALL_TARIFARIOS')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<TarifarioDTO> lista = tarifarioService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar tarifarios", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_TARIFARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<TarifarioDTO> obtenerPorId(@PathVariable Long id) {
        return tarifarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifario con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_TARIFARIO')")
    @PostMapping
    public ResponseEntity<TarifarioDTO> crear(@Valid @RequestBody TarifarioDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        TarifarioDTO nuevo = tarifarioService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_TARIFARIO')")
    @PutMapping("/{id}")
    public ResponseEntity<TarifarioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TarifarioDTO dto) {
        tarifarioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifario con ID " + id + " no encontrado"));
        dto.setId(id);
        TarifarioDTO actualizado = tarifarioService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_TARIFARIO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        tarifarioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifario con ID " + id + " no existe"));
        tarifarioService.deleteLogic(id);
        return ResponseEntity.ok("Tarifario desactivado correctamente");
    }

    // RF56–RF58: Tarifario por nivel, ruta y tipo
    @PreAuthorize("hasAuthority('GET_TARIFARIO_POR_CRITERIOS')")
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(
            @RequestParam Long nivelId,
            @RequestParam Long rutaId,
            @RequestParam String tipo) {
        try {
            TarifarioDTO filtro = new TarifarioDTO();
            // Aquí se requeriría un DTO con IDs, pero para simplificar:
            throw new BusinessException("Endpoint requiere DTO de búsqueda avanzada");
        } catch (Exception e) {
            log.error("Error al buscar tarifario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}