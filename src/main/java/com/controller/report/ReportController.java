package com.controller.report;

import java.io.IOException;
import java.sql.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ReportController extends HttpServlet {
    private ReportService reportService = new ReportService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Date startDate = Date.valueOf(req.getParameter("startDate"));
        Date endDate = Date.valueOf(req.getParameter("endDate"));

        byte[] pdfData = reportService.generateSalesReport(startDate, endDate);
        if (pdfData != null) {
            res.setContentType("application/pdf");
            res.setHeader("Content-Disposition", "attachment; filename=sales_report.pdf");
            res.getOutputStream().write(pdfData);
        }
    }
}