package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.EntregaDTO;
import com.rutaflex.trackflexv3.service.general.service.EntregaService;
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
@RequestMapping("/entregas")
public class EntregaController {

    private final EntregaService entregaService;

    @PreAuthorize("hasAuthority('GET_ALL_ENTREGAS')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<EntregaDTO> lista = entregaService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar entregas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_ENTREGA')")
    @GetMapping("/{id}")
    public ResponseEntity<EntregaDTO> obtenerPorId(@PathVariable Long id) {
        return entregaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_ENTREGA')")
    @PostMapping
    public ResponseEntity<EntregaDTO> crear(@Valid @RequestBody EntregaDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        EntregaDTO nuevo = entregaService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_ENTREGA')")
    @PutMapping("/{id}")
    public ResponseEntity<EntregaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EntregaDTO dto) {
        entregaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega con ID " + id + " no encontrado"));
        dto.setId(id);
        EntregaDTO actualizado = entregaService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_ENTREGA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        entregaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega con ID " + id + " no existe"));
        entregaService.deleteLogic(id);
        return ResponseEntity.ok("Entrega eliminada lógicamente");
    }

    // RF17, RF23: Entregas pendientes
    @PreAuthorize("hasAuthority('GET_ENTREGAS_PENDIENTES')")
    @GetMapping("/pendientes")
    public ResponseEntity<?> getPendientes() {
        try {
            EntregaDTO filtro = new EntregaDTO();
            filtro.setEstadoRegistro("pendiente");
            List<EntregaDTO> lista = entregaService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar entregas pendientes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // RF38: Entregas finalizadas
    @PreAuthorize("hasAuthority('GET_ENTREGAS_FINALIZADAS')")
    @GetMapping("/finalizadas")
    public ResponseEntity<?> getFinalizadas() {
        try {
            EntregaDTO filtro = new EntregaDTO();
            filtro.setEstadoRegistro("entregado");
            List<EntregaDTO> lista = entregaService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar entregas finalizadas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}