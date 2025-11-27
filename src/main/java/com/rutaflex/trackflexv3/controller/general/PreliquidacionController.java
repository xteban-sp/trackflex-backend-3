package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.PreliquidacionDTO;
import com.rutaflex.trackflexv3.service.general.service.PreliquidacionService;
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
@RequestMapping("/preliquidaciones")
public class PreliquidacionController {

    private final PreliquidacionService preliquidacionService;

    @PreAuthorize("hasAuthority('GET_ALL_PRELIQUIDACIONES')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<PreliquidacionDTO> lista = preliquidacionService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar preliquidaciones", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_PRELIQUIDACION')")
    @GetMapping("/{id}")
    public ResponseEntity<PreliquidacionDTO> obtenerPorId(@PathVariable Long id) {
        return preliquidacionService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Preliquidación con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_PRELIQUIDACION')")
    @PostMapping
    public ResponseEntity<PreliquidacionDTO> crear(@Valid @RequestBody PreliquidacionDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        PreliquidacionDTO nuevo = preliquidacionService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_PRELIQUIDACION')")
    @PutMapping("/{id}")
    public ResponseEntity<PreliquidacionDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PreliquidacionDTO dto) {
        preliquidacionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Preliquidación con ID " + id + " no encontrado"));
        dto.setId(id);
        PreliquidacionDTO actualizado = preliquidacionService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_PRELIQUIDACION')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        preliquidacionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Preliquidación con ID " + id + " no existe"));
        preliquidacionService.deleteLogic(id);
        return ResponseEntity.ok("Preliquidación eliminada");
    }

    // RF55–RF58: Preliquidación por servicio
    @PreAuthorize("hasAuthority('GET_PRELIQUIDACION_POR_SERVICIO')")
    @GetMapping("/por-servicio/{servicioId}")
    public ResponseEntity<?> getPorServicio(@PathVariable Long servicioId) {
        try {
            PreliquidacionDTO filtro = new PreliquidacionDTO();
            filtro.setServicioId(servicioId);
            List<PreliquidacionDTO> lista = preliquidacionService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar preliquidación por servicio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}