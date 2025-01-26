package com.controller.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.model.SalesReport;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfGenerator {
    public static byte[] generateSalesReportPDF(List<SalesReport> data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Sales Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5); // Example with 5 columns
            table.setWidthPercentage(100);
            table.addCell("Sale ID");
            table.addCell("Date");
            table.addCell("User");
            table.addCell("Product");
            table.addCell("Total");

            for (SalesReport report : data) {
                table.addCell(String.valueOf(report.getSaleId()));
                table.addCell(report.getSaleDate().toString());
                table.addCell(report.getUserName());
                table.addCell(report.getProductName());
                table.addCell(report.getTotalAmount().toString());
            }
            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}