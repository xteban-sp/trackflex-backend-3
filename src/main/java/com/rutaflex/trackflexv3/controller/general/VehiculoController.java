package com.rutaflex.trackflexv3.controller.general;

import com.rutaflex.trackflexv3.controller.error.BusinessException;
import com.rutaflex.trackflexv3.controller.error.ResourceNotFoundException;
import com.rutaflex.trackflexv3.dto.EnvioVehiculosSeleccionadosDTO;
import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import com.rutaflex.trackflexv3.service.export.EmailService;
import com.rutaflex.trackflexv3.service.export.ExportService;
import com.rutaflex.trackflexv3.service.general.service.VehiculoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/vehiculos")
@CrossOrigin(
        origins = "http://localhost:4200",
        allowCredentials = "true"
)
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final ExportService exportService;
    private final EmailService emailService;

    @PreAuthorize("hasAuthority('GET_VEHICULOS_DISPONIBLES')")
    @GetMapping("/disponibles")
    public ResponseEntity<List<VehiculoDTO>> getDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Long nivelId) {
        return ResponseEntity.ok(vehiculoService.findDisponibles(fecha, nivelId));
    }

    @PreAuthorize("hasAuthority('EXPORT_VEHICULOS_DISPONIBLES')")
    @PostMapping("/disponibles/exportar")
    public void exportarDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Long nivelId,
            @RequestParam String formato,
            HttpServletResponse response) throws IOException {
        List<VehiculoDTO> vehiculos = vehiculoService.findDisponibles(fecha, nivelId);
        exportService.exportVehiculosDisponibles(vehiculos, formato, response);
    }

    @PreAuthorize("hasAuthority('EXPORT_VEHICULOS_DISPONIBLES')")
    @PostMapping("/disponibles/enviar-correo")
    public ResponseEntity<String> enviarPorCorreo(
            @RequestBody EnvioVehiculosSeleccionadosDTO envioDto, // <-- Nuevo DTO
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Long nivelId) {
        List<VehiculoDTO> vehiculos;

        // Si se enviaron IDs específicos, úsalos. Si no, usa todos los disponibles.
        if (envioDto.getVehiculoIds() != null && !envioDto.getVehiculoIds().isEmpty()) {
            vehiculos = vehiculoService.findByIds(envioDto.getVehiculoIds());
        } else {
            // Comportamiento anterior (por si acaso)
            vehiculos = vehiculoService.findDisponibles(fecha, nivelId);
        }

        emailService.enviarVehiculosDisponibles(vehiculos, envioDto.getDestinatarios(), fecha);
        return ResponseEntity.ok("Enviado");
    }

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

}