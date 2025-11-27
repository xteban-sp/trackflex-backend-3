package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.AsignacionDTO;
import com.rutaflex.trackflexv3.service.general.service.AsignacionService;
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
@RequestMapping("/asignaciones")
public class AsignacionController {

    private final AsignacionService asignacionService;

    @PreAuthorize("hasAuthority('GET_ALL_ASIGNACIONES')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<AsignacionDTO> lista = asignacionService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar asignaciones", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_ASIGNACION')")
    @GetMapping("/{id}")
    public ResponseEntity<AsignacionDTO> obtenerPorId(@PathVariable Long id) {
        return asignacionService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_ASIGNACION')")
    @PostMapping
    public ResponseEntity<AsignacionDTO> crear(@Valid @RequestBody AsignacionDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        AsignacionDTO nuevo = asignacionService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_ASIGNACION')")
    @PutMapping("/{id}")
    public ResponseEntity<AsignacionDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AsignacionDTO dto) {
        asignacionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación con ID " + id + " no encontrado"));
        dto.setId(id);
        AsignacionDTO actualizado = asignacionService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_ASIGNACION')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        asignacionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación con ID " + id + " no existe"));
        asignacionService.deleteLogic(id);
        return ResponseEntity.ok("Asignación eliminada lógicamente");
    }

    // RF12, RF13: Asignaciones por servicio
    @PreAuthorize("hasAuthority('GET_ASIGNACIONES_POR_SERVICIO')")
    @GetMapping("/por-servicio/{servicioId}")
    public ResponseEntity<?> getPorServicio(@PathVariable Long servicioId) {
        try {
            AsignacionDTO filtro = new AsignacionDTO();
            filtro.setServicioId(servicioId);
            List<AsignacionDTO> lista = asignacionService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar asignaciones por servicio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}