package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.EmpresaSoatDTO;
import com.rutaflex.trackflexv3.service.general.service.EmpresaSoatService;
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
@RequestMapping("/empresas-soat")
public class EmpresaSoatController {

    private final EmpresaSoatService empresaSoatService;

    @PreAuthorize("hasAuthority('GET_ALL_EMPRESAS_SOAT')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<EmpresaSoatDTO> lista = empresaSoatService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar empresas SOAT", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_EMPRESA_SOAT')")
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaSoatDTO> obtenerPorId(@PathVariable Long id) {
        return empresaSoatService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa SOAT con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_EMPRESA_SOAT')")
    @PostMapping
    public ResponseEntity<EmpresaSoatDTO> crear(@Valid @RequestBody EmpresaSoatDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        EmpresaSoatDTO nuevo = empresaSoatService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_EMPRESA_SOAT')")
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaSoatDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EmpresaSoatDTO dto) {
        empresaSoatService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa SOAT con ID " + id + " no encontrado"));
        dto.setId(id);
        EmpresaSoatDTO actualizado = empresaSoatService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_EMPRESA_SOAT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        empresaSoatService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa SOAT con ID " + id + " no existe"));
        empresaSoatService.deleteLogic(id);
        return ResponseEntity.ok("Empresa SOAT desactivada correctamente");
    }
}