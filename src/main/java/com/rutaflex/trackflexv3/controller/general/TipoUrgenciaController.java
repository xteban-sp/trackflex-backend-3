package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.TipoUrgenciaDTO;
import com.rutaflex.trackflexv3.service.general.service.TipoUrgenciaService;
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
@RequestMapping("/tipos-urgencia")
public class TipoUrgenciaController {

    private final TipoUrgenciaService tipoUrgenciaService;

    @PreAuthorize("hasAuthority('GET_ALL_TIPOS_URGENCIA')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<TipoUrgenciaDTO> lista = tipoUrgenciaService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar tipos de urgencia", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_TIPO_URGENCIA')")
    @GetMapping("/{id}")
    public ResponseEntity<TipoUrgenciaDTO> obtenerPorId(@PathVariable Long id) {
        return tipoUrgenciaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de urgencia con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_TIPO_URGENCIA')")
    @PostMapping
    public ResponseEntity<TipoUrgenciaDTO> crear(@Valid @RequestBody TipoUrgenciaDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        TipoUrgenciaDTO nuevo = tipoUrgenciaService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_TIPO_URGENCIA')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoUrgenciaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TipoUrgenciaDTO dto) {
        tipoUrgenciaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de urgencia con ID " + id + " no encontrado"));
        dto.setId(id);
        TipoUrgenciaDTO actualizado = tipoUrgenciaService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_TIPO_URGENCIA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        tipoUrgenciaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de urgencia con ID " + id + " no existe"));
        tipoUrgenciaService.deleteLogic(id);
        return ResponseEntity.ok("Tipo de urgencia desactivado correctamente");
    }
}