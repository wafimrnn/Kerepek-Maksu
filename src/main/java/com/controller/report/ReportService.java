package com.controller.report;

import java.sql.Date;
import java.util.List;

import com.dao.ReportDAO;
import com.model.SalesReport;

public class ReportService {
    private ReportDAO reportDAO = new ReportDAO();

    public byte[] generateSalesReport(Date startDate, Date endDate) {
        try {
            List<SalesReport> salesData = reportDAO.getSalesReport(startDate, endDate);
            return PdfGenerator.generateSalesReportPDF(salesData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
