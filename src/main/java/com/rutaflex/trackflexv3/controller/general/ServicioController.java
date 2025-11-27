package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.ServicioDTO;
import com.rutaflex.trackflexv3.service.general.service.ServicioService;
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
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    @PreAuthorize("hasAuthority('GET_ALL_SERVICIOS')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<ServicioDTO> lista = servicioService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar servicios", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_SERVICIO')")
    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> obtenerPorId(@PathVariable Long id) {
        return servicioService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_SERVICIO')")
    @PostMapping
    public ResponseEntity<ServicioDTO> crear(@Valid @RequestBody ServicioDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        ServicioDTO nuevo = servicioService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_SERVICIO')")
    @PutMapping("/{id}")
    public ResponseEntity<ServicioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ServicioDTO dto) {
        servicioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio con ID " + id + " no encontrado"));
        dto.setId(id);
        ServicioDTO actualizado = servicioService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_SERVICIO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        servicioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio con ID " + id + " no existe"));
        servicioService.deleteLogic(id);
        return ResponseEntity.ok("Servicio eliminado lógicamente");
    }

    // RF07: Servicios por fecha
    @PreAuthorize("hasAuthority('GET_SERVICIOS_POR_FECHA')")
    @GetMapping("/por-fecha")
    public ResponseEntity<?> getPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            ServicioDTO filtro = new ServicioDTO();
            filtro.setFecha(fecha);
            List<ServicioDTO> lista = servicioService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar servicios por fecha", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // RF11: Servicios en estado "Sin asignar"
    @PreAuthorize("hasAuthority('GET_SERVICIOS_SIN_ASIGNAR')")
    @GetMapping("/sin-asignar")
    public ResponseEntity<?> getSinAsignar() {
        try {
            ServicioDTO filtro = new ServicioDTO();
            filtro.setEstadoRegistro("Sin asignar");
            List<ServicioDTO> lista = servicioService.findByObject(filtro);
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al consultar servicios sin asignar", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}