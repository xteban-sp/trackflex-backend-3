package com.rutaflex.trackflexv3.util.exporter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.rutaflex.trackflexv3.dto.ServicioDTO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfExporter {

    public static ByteArrayInputStream exportarHojaViaje(List<ServicioDTO> servicios) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Hoja de Viaje").setBold().setFontSize(18));
        document.add(new Paragraph("Fecha: " + java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        float[] columnWidths = {1, 2, 2, 2, 2};
        Table table = new Table(columnWidths);
        table.addHeaderCell(new Cell().add(new Paragraph("ID")));
        table.addHeaderCell(new Cell().add(new Paragraph("Código")));
        table.addHeaderCell(new Cell().add(new Paragraph("Cliente")));
        table.addHeaderCell(new Cell().add(new Paragraph("Vehículo")));
        table.addHeaderCell(new Cell().add(new Paragraph("Ruta")));

        for (ServicioDTO s : servicios) {
            table.addCell(String.valueOf(s.getId()));
            table.addCell(s.getCodigoServicio());
            table.addCell(s.getClienteId().toString());
            table.addCell(s.getVehiculoId().toString());
            table.addCell(s.getRutaId().toString());
        }

        document.add(table);
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}