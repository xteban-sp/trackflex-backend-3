package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.TipoMantenimientoDTO;
import com.rutaflex.trackflexv3.service.general.service.TipoMantenimientoService;
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
@RequestMapping("/tipos-mantenimiento")
public class TipoMantenimientoController {

    private final TipoMantenimientoService tipoMantenimientoService;

    @PreAuthorize("hasAuthority('GET_ALL_TIPOS_MANTENIMIENTO')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<TipoMantenimientoDTO> lista = tipoMantenimientoService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar tipos de mantenimiento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_TIPO_MANTENIMIENTO')")
    @GetMapping("/{id}")
    public ResponseEntity<TipoMantenimientoDTO> obtenerPorId(@PathVariable Long id) {
        return tipoMantenimientoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de mantenimiento con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_TIPO_MANTENIMIENTO')")
    @PostMapping
    public ResponseEntity<TipoMantenimientoDTO> crear(@Valid @RequestBody TipoMantenimientoDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        TipoMantenimientoDTO nuevo = tipoMantenimientoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_TIPO_MANTENIMIENTO')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoMantenimientoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TipoMantenimientoDTO dto) {
        tipoMantenimientoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de mantenimiento con ID " + id + " no encontrado"));
        dto.setId(id);
        TipoMantenimientoDTO actualizado = tipoMantenimientoService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_TIPO_MANTENIMIENTO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        tipoMantenimientoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de mantenimiento con ID " + id + " no existe"));
        tipoMantenimientoService.deleteLogic(id);
        return ResponseEntity.ok("Tipo de mantenimiento desactivado correctamente");
    }
}