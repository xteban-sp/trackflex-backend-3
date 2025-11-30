// src/main/java/com/rutaflex/trackflexv3/service/ExportService.java
package com.rutaflex.trackflexv3.service.export;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.UnitValue;
import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    public void exportVehiculosDisponibles(List<VehiculoDTO> vehiculos, String formato, HttpServletResponse response) throws IOException {
        if ("csv".equalsIgnoreCase(formato)) {
            exportarCsv(vehiculos, response);
        } else {
            throw new IllegalArgumentException("Formato no soportado: " + formato);
        }
    }
    private void exportarCsv(List<VehiculoDTO> vehiculos, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"vehiculos_disponibles.csv\"");

        PrintWriter writer = response.getWriter();
        writer.println("Placa,Marca,Modelo,Nivel,Capacidad Min,Capacidad Max");
        for (VehiculoDTO v : vehiculos) {
            writer.printf("%s,%s,%s,%s,%d,%d%n",
                    v.getPlaca(),
                    v.getMarca(),
                    v.getModelo(),
                    v.getNivel().getCodigo(),
                    v.getNivel().getM3Min(),
                    v.getNivel().getM3Max()
            );
        }
        writer.flush();
    }
}