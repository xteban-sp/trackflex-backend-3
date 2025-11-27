package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.TipoGastoDTO;
import com.rutaflex.trackflexv3.service.general.service.TipoGastoService;
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
@RequestMapping("/tipos-gasto")
public class TipoGastoController {

    private final TipoGastoService tipoGastoService;

    @PreAuthorize("hasAuthority('GET_ALL_TIPOS_GASTO')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<TipoGastoDTO> lista = tipoGastoService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar tipos de gasto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_TIPO_GASTO')")
    @GetMapping("/{id}")
    public ResponseEntity<TipoGastoDTO> obtenerPorId(@PathVariable Long id) {
        return tipoGastoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de gasto con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_TIPO_GASTO')")
    @PostMapping
    public ResponseEntity<TipoGastoDTO> crear(@Valid @RequestBody TipoGastoDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        TipoGastoDTO nuevo = tipoGastoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_TIPO_GASTO')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoGastoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TipoGastoDTO dto) {
        tipoGastoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de gasto con ID " + id + " no encontrado"));
        dto.setId(id);
        TipoGastoDTO actualizado = tipoGastoService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_TIPO_GASTO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        tipoGastoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de gasto con ID " + id + " no existe"));
        tipoGastoService.deleteLogic(id);
        return ResponseEntity.ok("Tipo de gasto desactivado correctamente");
    }
}