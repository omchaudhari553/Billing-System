package com.ajalkarbill.website.service;

import com.ajalkarbill.website.entity.WebsiteActivity;
import com.ajalkarbill.website.entity.WebsiteLead;
import com.ajalkarbill.website.entity.WebsiteVisitor;
import com.ajalkarbill.website.repository.WebsiteActivityRepository;
import com.ajalkarbill.website.repository.WebsiteLeadRepository;
import com.ajalkarbill.website.repository.WebsiteVisitorRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExcelExportService {

    private final WebsiteVisitorRepository visitorRepository;
    private final WebsiteActivityRepository activityRepository;
    private final WebsiteLeadRepository leadRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ExcelExportService(WebsiteVisitorRepository visitorRepository,
                              WebsiteActivityRepository activityRepository,
                              WebsiteLeadRepository leadRepository) {
        this.visitorRepository = visitorRepository;
        this.activityRepository = activityRepository;
        this.leadRepository = leadRepository;
    }

    public byte[] exportVisitorsToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Website Visitors");

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Create headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Visitor ID", "Full Name", "Mobile Number", "Email", "Business Name", 
                               "User ID", "Registered", "First Visit", "Last Visit"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fetch all visitors
            List<WebsiteVisitor> visitors = visitorRepository.findAll();
            
            // Preload lead information for all visitors
            Map<String, WebsiteLead> leadMap = new HashMap<>();
            for (WebsiteVisitor visitor : visitors) {
                Optional<WebsiteLead> leadOpt = leadRepository.findFirstByVisitorIdOrderByCreatedAtDesc(visitor.getVisitorId());
                leadOpt.ifPresent(lead -> leadMap.put(visitor.getVisitorId(), lead));
            }

            // Create data rows
            int rowNum = 1;
            for (WebsiteVisitor visitor : visitors) {
                Row row = sheet.createRow(rowNum++);
                
                WebsiteLead lead = leadMap.get(visitor.getVisitorId());
                
                row.createCell(0).setCellValue(visitor.getVisitorId());
                row.createCell(1).setCellValue(lead != null && lead.getFullName() != null ? lead.getFullName() : "");
                row.createCell(2).setCellValue(lead != null && lead.getPhoneNumber() != null ? lead.getPhoneNumber() : "");
                row.createCell(3).setCellValue(lead != null && lead.getEmail() != null ? lead.getEmail() : "");
                row.createCell(4).setCellValue(lead != null && lead.getBusinessName() != null ? lead.getBusinessName() : "");
                row.createCell(5).setCellValue(visitor.getUser() != null ? String.valueOf(visitor.getUser().getId()) : "");
                row.createCell(6).setCellValue(visitor.getUser() != null ? "TRUE" : "FALSE");
                row.createCell(7).setCellValue(visitor.getFirstVisitAt() != null ? 
                    visitor.getFirstVisitAt().format(DATE_FORMATTER) : "");
                row.createCell(8).setCellValue(visitor.getLastVisitAt() != null ? 
                    visitor.getLastVisitAt().format(DATE_FORMATTER) : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportActivitiesToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Website Activities");

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Create headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Visitor ID", "Event Type", "Page URL", "Full Name", 
                               "Mobile Number", "Email", "User ID", "Activity Date"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fetch all activities
            List<WebsiteActivity> activities = activityRepository.findAll();
            
            // Preload lead information for all visitors
            Map<String, WebsiteLead> leadMap = new HashMap<>();
            for (WebsiteActivity activity : activities) {
                if (!leadMap.containsKey(activity.getVisitorId())) {
                    Optional<WebsiteLead> leadOpt = leadRepository.findFirstByVisitorIdOrderByCreatedAtDesc(activity.getVisitorId());
                    leadOpt.ifPresent(lead -> leadMap.put(activity.getVisitorId(), lead));
                }
            }

            // Create data rows
            int rowNum = 1;
            for (WebsiteActivity activity : activities) {
                Row row = sheet.createRow(rowNum++);
                
                WebsiteLead lead = leadMap.get(activity.getVisitorId());
                
                row.createCell(0).setCellValue(activity.getVisitorId());
                row.createCell(1).setCellValue(activity.getEventType() != null ? activity.getEventType().name() : "");
                row.createCell(2).setCellValue(activity.getPageUrl() != null ? activity.getPageUrl() : "");
                row.createCell(3).setCellValue(lead != null && lead.getFullName() != null ? lead.getFullName() : "");
                row.createCell(4).setCellValue(lead != null && lead.getPhoneNumber() != null ? lead.getPhoneNumber() : "");
                row.createCell(5).setCellValue(lead != null && lead.getEmail() != null ? lead.getEmail() : "");
                row.createCell(6).setCellValue(activity.getUser() != null ? String.valueOf(activity.getUser().getId()) : "");
                row.createCell(7).setCellValue(activity.getCreatedAt() != null ? 
                    activity.getCreatedAt().format(DATE_FORMATTER) : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportLeadsToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Website Leads");

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Create headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Visitor ID", "Full Name", "Mobile Number", "Email", "Business Name", 
                               "Lead Source", "Message", "Status", "Registered"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fetch all leads
            List<WebsiteLead> leads = leadRepository.findAll();

            // Create data rows
            int rowNum = 1;
            for (WebsiteLead lead : leads) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(lead.getVisitorId() != null ? lead.getVisitorId() : "");
                row.createCell(1).setCellValue(lead.getFullName() != null ? lead.getFullName() : "");
                row.createCell(2).setCellValue(lead.getPhoneNumber() != null ? lead.getPhoneNumber() : "");
                row.createCell(3).setCellValue(lead.getEmail() != null ? lead.getEmail() : "");
                row.createCell(4).setCellValue(lead.getBusinessName() != null ? lead.getBusinessName() : "");
                row.createCell(5).setCellValue(lead.getSource() != null ? lead.getSource().name() : "");
                row.createCell(6).setCellValue(lead.getMessage() != null ? lead.getMessage() : "");
                row.createCell(7).setCellValue(lead.getStatus() != null ? lead.getStatus() : "");
                row.createCell(8).setCellValue(lead.getRegistered() != null && lead.getRegistered() ? "TRUE" : "FALSE");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportAllToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Add all three sheets
            createVisitorsSheet(workbook);
            createActivitiesSheet(workbook);
            createLeadsSheet(workbook);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createVisitorsSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Website Visitors");

        // Create header style
        CellStyle headerStyle = createHeaderStyle(workbook);

        // Create headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Visitor ID", "Full Name", "Mobile Number", "Email", "Business Name", 
                           "User ID", "Registered", "First Visit", "Last Visit"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Fetch all visitors
        List<WebsiteVisitor> visitors = visitorRepository.findAll();
        
        // Preload lead information for all visitors
        Map<String, WebsiteLead> leadMap = new HashMap<>();
        for (WebsiteVisitor visitor : visitors) {
            Optional<WebsiteLead> leadOpt = leadRepository.findFirstByVisitorIdOrderByCreatedAtDesc(visitor.getVisitorId());
            leadOpt.ifPresent(lead -> leadMap.put(visitor.getVisitorId(), lead));
        }

        // Create data rows
        int rowNum = 1;
        for (WebsiteVisitor visitor : visitors) {
            Row row = sheet.createRow(rowNum++);
            
            WebsiteLead lead = leadMap.get(visitor.getVisitorId());
            
            row.createCell(0).setCellValue(visitor.getVisitorId());
            row.createCell(1).setCellValue(lead != null && lead.getFullName() != null ? lead.getFullName() : "");
            row.createCell(2).setCellValue(lead != null && lead.getPhoneNumber() != null ? lead.getPhoneNumber() : "");
            row.createCell(3).setCellValue(lead != null && lead.getEmail() != null ? lead.getEmail() : "");
            row.createCell(4).setCellValue(lead != null && lead.getBusinessName() != null ? lead.getBusinessName() : "");
            row.createCell(5).setCellValue(visitor.getUser() != null ? String.valueOf(visitor.getUser().getId()) : "");
            row.createCell(6).setCellValue(visitor.getUser() != null ? "TRUE" : "FALSE");
            row.createCell(7).setCellValue(visitor.getFirstVisitAt() != null ? 
                visitor.getFirstVisitAt().format(DATE_FORMATTER) : "");
            row.createCell(8).setCellValue(visitor.getLastVisitAt() != null ? 
                visitor.getLastVisitAt().format(DATE_FORMATTER) : "");
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createActivitiesSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Website Activities");

        // Create header style
        CellStyle headerStyle = createHeaderStyle(workbook);

        // Create headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Visitor ID", "Event Type", "Page URL", "Full Name", 
                           "Mobile Number", "Email", "User ID", "Activity Date"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Fetch all activities
        List<WebsiteActivity> activities = activityRepository.findAll();
        
        // Preload lead information for all visitors
        Map<String, WebsiteLead> leadMap = new HashMap<>();
        for (WebsiteActivity activity : activities) {
            if (!leadMap.containsKey(activity.getVisitorId())) {
                Optional<WebsiteLead> leadOpt = leadRepository.findFirstByVisitorIdOrderByCreatedAtDesc(activity.getVisitorId());
                leadOpt.ifPresent(lead -> leadMap.put(activity.getVisitorId(), lead));
            }
        }

        // Create data rows
        int rowNum = 1;
        for (WebsiteActivity activity : activities) {
            Row row = sheet.createRow(rowNum++);
            
            WebsiteLead lead = leadMap.get(activity.getVisitorId());
            
            row.createCell(0).setCellValue(activity.getVisitorId());
            row.createCell(1).setCellValue(activity.getEventType() != null ? activity.getEventType().name() : "");
            row.createCell(2).setCellValue(activity.getPageUrl() != null ? activity.getPageUrl() : "");
            row.createCell(3).setCellValue(lead != null && lead.getFullName() != null ? lead.getFullName() : "");
            row.createCell(4).setCellValue(lead != null && lead.getPhoneNumber() != null ? lead.getPhoneNumber() : "");
            row.createCell(5).setCellValue(lead != null && lead.getEmail() != null ? lead.getEmail() : "");
            row.createCell(6).setCellValue(activity.getUser() != null ? String.valueOf(activity.getUser().getId()) : "");
            row.createCell(7).setCellValue(activity.getCreatedAt() != null ? 
                activity.getCreatedAt().format(DATE_FORMATTER) : "");
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createLeadsSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Website Leads");

        // Create header style
        CellStyle headerStyle = createHeaderStyle(workbook);

        // Create headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Visitor ID", "Full Name", "Mobile Number", "Email", "Business Name", 
                           "Lead Source", "Message", "Status", "Registered"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Fetch all leads
        List<WebsiteLead> leads = leadRepository.findAll();

        // Create data rows
        int rowNum = 1;
        for (WebsiteLead lead : leads) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(lead.getVisitorId() != null ? lead.getVisitorId() : "");
            row.createCell(1).setCellValue(lead.getFullName() != null ? lead.getFullName() : "");
            row.createCell(2).setCellValue(lead.getPhoneNumber() != null ? lead.getPhoneNumber() : "");
            row.createCell(3).setCellValue(lead.getEmail() != null ? lead.getEmail() : "");
            row.createCell(4).setCellValue(lead.getBusinessName() != null ? lead.getBusinessName() : "");
            row.createCell(5).setCellValue(lead.getSource() != null ? lead.getSource().name() : "");
            row.createCell(6).setCellValue(lead.getMessage() != null ? lead.getMessage() : "");
            row.createCell(7).setCellValue(lead.getStatus() != null ? lead.getStatus() : "");
            row.createCell(8).setCellValue(lead.getRegistered() != null && lead.getRegistered() ? "TRUE" : "FALSE");
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}