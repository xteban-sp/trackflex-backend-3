package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.TipoIncidenciaDTO;
import com.rutaflex.trackflexv3.service.general.service.TipoIncidenciaService;
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
@RequestMapping("/tipos-incidencia")
public class TipoIncidenciaController {

    private final TipoIncidenciaService tipoIncidenciaService;

    @PreAuthorize("hasAuthority('GET_ALL_TIPOS_INCIDENCIA')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<TipoIncidenciaDTO> lista = tipoIncidenciaService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar tipos de incidencia", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_TIPO_INCIDENCIA')")
    @GetMapping("/{id}")
    public ResponseEntity<TipoIncidenciaDTO> obtenerPorId(@PathVariable Long id) {
        return tipoIncidenciaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de incidencia con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_TIPO_INCIDENCIA')")
    @PostMapping
    public ResponseEntity<TipoIncidenciaDTO> crear(@Valid @RequestBody TipoIncidenciaDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        TipoIncidenciaDTO nuevo = tipoIncidenciaService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_TIPO_INCIDENCIA')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoIncidenciaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TipoIncidenciaDTO dto) {
        tipoIncidenciaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de incidencia con ID " + id + " no encontrado"));
        dto.setId(id);
        TipoIncidenciaDTO actualizado = tipoIncidenciaService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_TIPO_INCIDENCIA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        tipoIncidenciaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de incidencia con ID " + id + " no existe"));
        tipoIncidenciaService.deleteLogic(id);
        return ResponseEntity.ok("Tipo de incidencia desactivado correctamente");
    }
}