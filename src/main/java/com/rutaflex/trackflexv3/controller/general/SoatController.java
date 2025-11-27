package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.SoatDTO;
import com.rutaflex.trackflexv3.service.general.service.SoatService;
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
@RequestMapping("/soat")
public class SoatController {

    private final SoatService soatService;

    @PreAuthorize("hasAuthority('GET_ALL_SOAT')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<SoatDTO> lista = soatService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar SOATs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_SOAT')")
    @GetMapping("/{id}")
    public ResponseEntity<SoatDTO> obtenerPorId(@PathVariable Long id) {
        return soatService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("SOAT con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_SOAT')")
    @PostMapping
    public ResponseEntity<SoatDTO> crear(@Valid @RequestBody SoatDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        SoatDTO nuevo = soatService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_SOAT')")
    @PutMapping("/{id}")
    public ResponseEntity<SoatDTO> actualizar(@PathVariable Long id, @Valid @RequestBody SoatDTO dto) {
        soatService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SOAT con ID " + id + " no encontrado"));
        dto.setId(id);
        SoatDTO actualizado = soatService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_SOAT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        soatService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SOAT con ID " + id + " no existe"));
        soatService.deleteLogic(id);
        return ResponseEntity.ok("SOAT desactivado correctamente");
    }

    // SOAT por vehículo
    @PreAuthorize("hasAuthority('GET_SOAT_POR_VEHICULO')")
    @GetMapping("/por-vehiculo/{vehiculoId}")
    public ResponseEntity<?> getPorVehiculo(@PathVariable Long vehiculoId) {
        try {
            SoatDTO filtro = new SoatDTO();
            filtro.setVehiculoId(vehiculoId);
            List<SoatDTO> lista = soatService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar SOAT por vehículo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}