package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import com.rutaflex.trackflexv3.service.export.VehiculoExportService;
import com.rutaflex.trackflexv3.service.general.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/vehiculos")
public class VehiculoController {
    // Agrega esta dependencia
    private final VehiculoExportService vehiculoExportService;
    private final VehiculoService vehiculoService;

    @PreAuthorize("hasAuthority('GET_ALL_VEHICULOS')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<VehiculoDTO> lista = vehiculoService.findAll();
            if (isNull(lista) || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error al listar vehículos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('GET_ONE_VEHICULO')")
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDTO> obtenerPorId(@PathVariable Long id) {
        return vehiculoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo con ID " + id + " no existe"));
    }

    @PreAuthorize("hasAuthority('CREATE_VEHICULO')")
    @PostMapping
    public ResponseEntity<VehiculoDTO> crear(@Valid @RequestBody VehiculoDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        VehiculoDTO nuevo = vehiculoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PreAuthorize("hasAuthority('UPDATE_VEHICULO')")
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody VehiculoDTO dto) {
        vehiculoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo con ID " + id + " no encontrado"));
        dto.setId(id);
        VehiculoDTO actualizado = vehiculoService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAuthority('DELETE_VEHICULO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        vehiculoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo con ID " + id + " no existe"));
        vehiculoService.deleteLogic(id);
        return ResponseEntity.ok("Vehículo desactivado correctamente");
    }

    // RF01, RF03, RF05: Exportar lista de vehículos disponibles para una fecha
    @PreAuthorize("hasAuthority('EXPORT_VEHICULOS_DISPONIBLES')")
    @GetMapping("/exportar/disponibles")
    public ResponseEntity<?> exportarVehiculosDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            // Lógica de disponibilidad está en MantenimientoService, pero lo exponemos aquí
            throw new BusinessException("Endpoint de exportación requiere servicio de coordinación (pendiente)");
        } catch (Exception e) {
            log.error("Error al exportar vehículos disponibles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PreAuthorize("hasAuthority('EXPORT_VEHICULOS_DISPONIBLES')")
    @GetMapping("/exportar/excel")
    public ResponseEntity<Resource> exportarVehiculosExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            ByteArrayInputStream excelStream = vehiculoExportService.exportarVehiculosDisponibles(fecha);
            byte[] excelBytes = excelStream.readAllBytes(); // Java 11+
            // Si usas Java < 11, usa:
            // byte[] excelBytes = excelStream.readAllBytes(); → reemplaza por:
            // byte[] excelBytes = IOUtils.toByteArray(excelStream); (con Apache Commons IO)

            ByteArrayResource resource = new ByteArrayResource(excelBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vehiculos_disponibles_" + fecha + ".xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(excelBytes.length)
                    .body(resource);
        } catch (Exception e) {
            log.error("Error al exportar vehículos a Excel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}