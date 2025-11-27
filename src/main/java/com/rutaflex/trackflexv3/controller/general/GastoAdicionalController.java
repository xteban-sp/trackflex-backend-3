package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.GastoAdicionalDTO;
import com.rutaflex.trackflexv3.service.general.service.GastoAdicionalService;
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
@RequestMapping("/gastos-adicionales")
public class GastoAdicionalController {

    private final GastoAdicionalService gastoAdicionalService;

    @PreAuthorize("hasAuthority('GET_ALL_GASTOS_ADICIONALES')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<GastoAdicionalDTO> lista = gastoAdicionalService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar gastos adicionales", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_GASTO_ADICIONAL')")
    @GetMapping("/{id}")
    public ResponseEntity<GastoAdicionalDTO> obtenerPorId(@PathVariable Long id) {
        return gastoAdicionalService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto adicional con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_GASTO_ADICIONAL')")
    @PostMapping
    public ResponseEntity<GastoAdicionalDTO> crear(@Valid @RequestBody GastoAdicionalDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        GastoAdicionalDTO nuevo = gastoAdicionalService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_GASTO_ADICIONAL')")
    @PutMapping("/{id}")
    public ResponseEntity<GastoAdicionalDTO> actualizar(@PathVariable Long id, @Valid @RequestBody GastoAdicionalDTO dto) {
        gastoAdicionalService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto adicional con ID " + id + " no encontrado"));
        dto.setId(id);
        GastoAdicionalDTO actualizado = gastoAdicionalService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_GASTO_ADICIONAL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        gastoAdicionalService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto adicional con ID " + id + " no existe"));
        gastoAdicionalService.deleteLogic(id);
        return ResponseEntity.ok("Gasto adicional eliminado");
    }

    // RF43: Gastos por servicio
    @PreAuthorize("hasAuthority('GET_GASTOS_POR_SERVICIO')")
    @GetMapping("/por-servicio/{servicioId}")
    public ResponseEntity<?> getPorServicio(@PathVariable Long servicioId) {
        try {
            GastoAdicionalDTO filtro = new GastoAdicionalDTO();
            filtro.setServicioId(servicioId);
            List<GastoAdicionalDTO> lista = gastoAdicionalService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar gastos por servicio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}