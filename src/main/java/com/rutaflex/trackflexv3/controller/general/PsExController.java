package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.PsExDTO;
import com.rutaflex.trackflexv3.service.general.service.PsExService;
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
@RequestMapping("/psex")
public class PsExController {

    private final PsExService psExService;

    @PreAuthorize("hasAuthority('GET_ALL_PSEX')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<PsExDTO> lista = psExService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar PSEX", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_PSEX')")
    @GetMapping("/{id}")
    public ResponseEntity<PsExDTO> obtenerPorId(@PathVariable Long id) {
        return psExService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("PSEX con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_PSEX')")
    @PostMapping
    public ResponseEntity<PsExDTO> crear(@Valid @RequestBody PsExDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        PsExDTO nuevo = psExService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_PSEX')")
    @PutMapping("/{id}")
    public ResponseEntity<PsExDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PsExDTO dto) {
        psExService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PSEX con ID " + id + " no encontrado"));
        dto.setId(id);
        PsExDTO actualizado = psExService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_PSEX')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        psExService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PSEX con ID " + id + " no existe"));
        psExService.deleteLogic(id);
        return ResponseEntity.ok("PSEX desactivado correctamente");
    }

    // RF08–RF10: PSEX por almacén
    @PreAuthorize("hasAuthority('GET_PSEX_POR_ALMACEN')")
    @GetMapping("/por-almacen/{almacenId}")
    public ResponseEntity<?> getPorAlmacen(@PathVariable Long almacenId) {
        try {
            PsExDTO filtro = new PsExDTO();
            filtro.setAlmacenId(almacenId);
            List<PsExDTO> lista = psExService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar PSEX por almacén", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}