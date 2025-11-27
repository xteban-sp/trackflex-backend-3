package com.rutaflex.trackflexv3.util.exporter;

import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {

    public static ByteArrayInputStream exportarVehiculos(List<VehiculoDTO> vehiculos) throws IOException {
        String[] columns = {"ID", "Placa", "Marca", "Modelo", "Nivel", "Estado"};
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Vehículos Disponibles");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Crear cabecera
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Llenar datos
            int rowNum = 1;
            for (VehiculoDTO v : vehiculos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(v.getId());
                row.createCell(1).setCellValue(v.getPlaca());
                row.createCell(2).setCellValue(v.getMarca());
                row.createCell(3).setCellValue(v.getModelo());
                row.createCell(4).setCellValue(v.getNivel() != null ? v.getNivel().getCodigo() : "");
                row.createCell(5).setCellValue(v.getEstado());
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
