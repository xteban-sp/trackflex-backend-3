package com.rutaflex.trackflexv3.service.export;

import com.rutaflex.trackflexv3.dto.ServicioDTO;
import com.rutaflex.trackflexv3.service.general.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HojaViajeService {

    private final ServicioService servicioService;

    public ByteArrayInputStream exportarHojaViaje(List<Long> servicioIds) throws IOException, Exception {

        List<ServicioDTO> servicios = servicioIds.stream()
                .map(id -> servicioService.findById(id).orElse(null))
                .filter(s -> s != null)
                .toList();

        return com.rutaflex.trackflexv3.util.exporter.PdfExporter.exportarHojaViaje(servicios);
    }
}