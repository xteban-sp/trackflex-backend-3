package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.IncidenciaDTO;
import com.rutaflex.trackflexv3.service.general.service.IncidenciaService;
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
@RequestMapping("/incidencias")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;

    @PreAuthorize("hasAuthority('GET_ALL_INCIDENCIAS')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<IncidenciaDTO> lista = incidenciaService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar incidencias", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_INCIDENCIA')")
    @GetMapping("/{id}")
    public ResponseEntity<IncidenciaDTO> obtenerPorId(@PathVariable Long id) {
        return incidenciaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Incidencia con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_INCIDENCIA')")
    @PostMapping
    public ResponseEntity<IncidenciaDTO> crear(@Valid @RequestBody IncidenciaDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        IncidenciaDTO nuevo = incidenciaService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_INCIDENCIA')")
    @PutMapping("/{id}")
    public ResponseEntity<IncidenciaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody IncidenciaDTO dto) {
        incidenciaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incidencia con ID " + id + " no encontrado"));
        dto.setId(id);
        IncidenciaDTO actualizado = incidenciaService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_INCIDENCIA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        incidenciaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incidencia con ID " + id + " no existe"));
        incidenciaService.deleteLogic(id);
        return ResponseEntity.ok("Incidencia eliminada lógicamente");
    }

    // RF27–RF30: Incidencias por entrega
    @PreAuthorize("hasAuthority('GET_INCIDENCIAS_POR_ENTREGA')")
    @GetMapping("/por-entrega/{entregaId}")
    public ResponseEntity<?> getPorEntrega(@PathVariable Long entregaId) {
        try {
            IncidenciaDTO filtro = new IncidenciaDTO();
            filtro.setEntregaId(entregaId);
            List<IncidenciaDTO> lista = incidenciaService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar incidencias por entrega", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}