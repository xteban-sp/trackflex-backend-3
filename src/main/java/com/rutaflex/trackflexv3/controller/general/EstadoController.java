package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.EstadoDTO;
import com.rutaflex.trackflexv3.service.general.service.EstadoService;
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
@RequestMapping("/estados")
public class EstadoController {

    private final EstadoService estadoService;

    @PreAuthorize("hasAuthority('GET_ALL_ESTADOS')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<EstadoDTO> lista = estadoService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar estados", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_ESTADO')")
    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> obtenerPorId(@PathVariable Long id) {
        return estadoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Estado con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_ESTADO')")
    @PostMapping
    public ResponseEntity<EstadoDTO> crear(@Valid @RequestBody EstadoDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        EstadoDTO nuevo = estadoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_ESTADO')")
    @PutMapping("/{id}")
    public ResponseEntity<EstadoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EstadoDTO dto) {
        estadoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado con ID " + id + " no encontrado"));
        dto.setId(id);
        EstadoDTO actualizado = estadoService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_ESTADO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        estadoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado con ID " + id + " no existe"));
        estadoService.deleteLogic(id);
        return ResponseEntity.ok("Estado desactivado correctamente");
    }
}