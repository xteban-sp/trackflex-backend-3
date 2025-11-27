package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.MantenimientoDTO;
import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import com.rutaflex.trackflexv3.service.general.service.MantenimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    @PreAuthorize("hasAuthority('GET_ALL_MANTENIMIENTOS')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<MantenimientoDTO> lista = mantenimientoService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar mantenimientos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_MANTENIMIENTO')")
    @GetMapping("/{id}")
    public ResponseEntity<MantenimientoDTO> obtenerPorId(@PathVariable Long id) {
        return mantenimientoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_MANTENIMIENTO')")
    @PostMapping
    public ResponseEntity<MantenimientoDTO> programar(@Valid @RequestBody MantenimientoDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        MantenimientoDTO nuevo = mantenimientoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_MANTENIMIENTO')")
    @PutMapping("/{id}")
    public ResponseEntity<MantenimientoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody MantenimientoDTO dto) {
        mantenimientoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento con ID " + id + " no encontrado"));
        dto.setId(id);
        MantenimientoDTO actualizado = mantenimientoService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    // RF67, RF68: Cancelar mantenimiento (reactivar vehículo)
    @PreAuthorize("hasAuthority('CANCEL_MANTENIMIENTO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelar(@PathVariable Long id) throws Exception {
        mantenimientoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento con ID " + id + " no existe"));
        mantenimientoService.cancelarMantenimiento(id);
        return ResponseEntity.ok("Mantenimiento cancelado y vehículo reactivado");
    }

    // RF01, RF03: Listar vehículos disponibles en una fecha
    @PreAuthorize("hasAuthority('GET_VEHICULOS_DISPONIBLES')")
    @GetMapping("/vehiculos/disponibles")
    public ResponseEntity<?> getVehiculosDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            List<VehiculoDTO> vehiculos = mantenimientoService.findVehiculosDisponiblesEnFecha(fecha);
            if (isNull(vehiculos) || vehiculos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(vehiculos);
        } catch (Exception e) {
            log.error("Error al consultar vehículos disponibles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}