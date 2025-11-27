package com.rutaflex.trackflexv3.service.export;

import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import com.rutaflex.trackflexv3.service.general.service.MantenimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class VehiculoExportService {

    private final MantenimientoService mantenimientoService;

    public ByteArrayInputStream exportarVehiculosDisponibles(LocalDate fecha) throws IOException, Exception {
        java.util.List<VehiculoDTO> vehiculos = mantenimientoService.findVehiculosDisponiblesEnFecha(fecha);
        return com.rutaflex.trackflexv3.util.exporter.ExcelExporter.exportarVehiculos(vehiculos);
    }
}