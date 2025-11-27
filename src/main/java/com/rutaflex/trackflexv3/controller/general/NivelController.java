package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.NivelDTO;
import com.rutaflex.trackflexv3.service.general.service.NivelService;
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
@RequestMapping("/niveles")
public class NivelController {

    private final NivelService nivelService;

    @PreAuthorize("hasAuthority('GET_ALL_NIVELES')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<NivelDTO> lista = nivelService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar niveles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_NIVEL')")
    @GetMapping("/{id}")
    public ResponseEntity<NivelDTO> obtenerPorId(@PathVariable Long id) {
        return nivelService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_NIVEL')")
    @PostMapping
    public ResponseEntity<NivelDTO> crear(@Valid @RequestBody NivelDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        NivelDTO nuevo = nivelService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_NIVEL')")
    @PutMapping("/{id}")
    public ResponseEntity<NivelDTO> actualizar(@PathVariable Long id, @Valid @RequestBody NivelDTO dto) {
        nivelService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel con ID " + id + " no encontrado"));
        dto.setId(id);
        NivelDTO actualizado = nivelService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_NIVEL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        nivelService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel con ID " + id + " no existe"));
        nivelService.deleteLogic(id);
        return ResponseEntity.ok("Nivel no se puede eliminar (operación no permitida)");
    }
}