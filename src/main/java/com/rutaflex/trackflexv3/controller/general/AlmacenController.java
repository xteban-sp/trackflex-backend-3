package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.AlmacenDTO;
import com.rutaflex.trackflexv3.service.general.service.AlmacenService;
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
@RequestMapping("/almacenes")
public class AlmacenController {

    private final AlmacenService almacenService;

    @PreAuthorize("hasAuthority('GET_ALL_ALMACENES')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<AlmacenDTO> lista = almacenService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar almacenes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_ALMACEN')")
    @GetMapping("/{id}")
    public ResponseEntity<AlmacenDTO> obtenerPorId(@PathVariable Long id) {
        return almacenService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Almacén con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_ALMACEN')")
    @PostMapping
    public ResponseEntity<AlmacenDTO> crear(@Valid @RequestBody AlmacenDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        AlmacenDTO nuevo = almacenService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_ALMACEN')")
    @PutMapping("/{id}")
    public ResponseEntity<AlmacenDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AlmacenDTO dto) {
        almacenService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Almacén con ID " + id + " no encontrado"));
        dto.setId(id);
        AlmacenDTO actualizado = almacenService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_ALMACEN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        almacenService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Almacén con ID " + id + " no existe"));
        almacenService.deleteLogic(id);
        return ResponseEntity.ok("Almacén desactivado correctamente");
    }
}