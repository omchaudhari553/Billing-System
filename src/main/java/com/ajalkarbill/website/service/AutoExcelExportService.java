package com.ajalkarbill.website.service;

import com.ajalkarbill.website.repository.WebsiteActivityRepository;
import com.ajalkarbill.website.repository.WebsiteLeadRepository;
import com.ajalkarbill.website.repository.WebsiteVisitorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AutoExcelExportService {

    private static final Logger logger = LoggerFactory.getLogger(AutoExcelExportService.class);
    private static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private final ExcelExportService excelExportService;
    private final WebsiteActivityRepository activityRepository;
    private final WebsiteLeadRepository leadRepository;
    private final WebsiteVisitorRepository visitorRepository;

    @Value("${app.auto.export.enabled:true}")
    private boolean autoExportEnabled;

    @Value("${app.auto.export.directory:./exports}")
    private String exportDirectory;

    @Value("${app.auto.export.interval.minutes:30}")
    private int exportIntervalMinutes;

    private LocalDateTime lastExportTime;

    public AutoExcelExportService(ExcelExportService excelExportService,
                                  WebsiteActivityRepository activityRepository,
                                  WebsiteLeadRepository leadRepository,
                                  WebsiteVisitorRepository visitorRepository) {
        this.excelExportService = excelExportService;
        this.activityRepository = activityRepository;
        this.leadRepository = leadRepository;
        this.visitorRepository = visitorRepository;
        this.lastExportTime = LocalDateTime.now();
    }

    @Scheduled(fixedRateString = "${app.auto.export.interval.minutes:30}0000")
    public void scheduledExport() {
        if (!autoExportEnabled) {
            logger.info("Auto export is disabled");
            return;
        }

        try {
            logger.info("Starting scheduled Excel export...");
            exportAllData();
            logger.info("Scheduled Excel export completed successfully");
        } catch (Exception e) {
            logger.error("Error during scheduled Excel export", e);
        }
    }

    public void exportAllData() throws IOException {
        exportAllData(true);
    }

    public void exportAllData(boolean withTimestamp) throws IOException {
        ensureExportDirectoryExists();

        String timestamp = withTimestamp ? "_" + LocalDateTime.now().format(FILE_NAME_FORMATTER) : "";
        
        // Export all data to a single Excel file
        byte[] excelData = excelExportService.exportAllToExcel();
        String fileName = "website_analytics_complete" + timestamp + ".xlsx";
        Path filePath = Paths.get(exportDirectory, fileName);
        
        try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
            outputStream.write(excelData);
        }

        lastExportTime = LocalDateTime.now();
        logger.info("Excel file exported successfully: {}", filePath.toAbsolutePath());
    }

    public void exportVisitors() throws IOException {
        ensureExportDirectoryExists();

        String timestamp = "_" + LocalDateTime.now().format(FILE_NAME_FORMATTER);
        byte[] excelData = excelExportService.exportVisitorsToExcel();
        String fileName = "website_visitors" + timestamp + ".xlsx";
        Path filePath = Paths.get(exportDirectory, fileName);
        
        try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
            outputStream.write(excelData);
        }

        lastExportTime = LocalDateTime.now();
        logger.info("Visitors Excel file exported successfully: {}", filePath.toAbsolutePath());
    }

    public void exportActivities() throws IOException {
        ensureExportDirectoryExists();

        String timestamp = "_" + LocalDateTime.now().format(FILE_NAME_FORMATTER);
        byte[] excelData = excelExportService.exportActivitiesToExcel();
        String fileName = "website_activities" + timestamp + ".xlsx";
        Path filePath = Paths.get(exportDirectory, fileName);
        
        try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
            outputStream.write(excelData);
        }

        lastExportTime = LocalDateTime.now();
        logger.info("Activities Excel file exported successfully: {}", filePath.toAbsolutePath());
    }

    public void exportLeads() throws IOException {
        ensureExportDirectoryExists();

        String timestamp = "_" + LocalDateTime.now().format(FILE_NAME_FORMATTER);
        byte[] excelData = excelExportService.exportLeadsToExcel();
        String fileName = "website_leads" + timestamp + ".xlsx";
        Path filePath = Paths.get(exportDirectory, fileName);
        
        try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
            outputStream.write(excelData);
        }

        lastExportTime = LocalDateTime.now();
        logger.info("Leads Excel file exported successfully: {}", filePath.toAbsolutePath());
    }

    public void exportOnNewData() throws IOException {
        // Export only the latest data without timestamp to update the main file
        exportAllData(false);
    }

    private void ensureExportDirectoryExists() throws IOException {
        Path directoryPath = Paths.get(exportDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
            logger.info("Created export directory: {}", directoryPath.toAbsolutePath());
        }
    }

    public LocalDateTime getLastExportTime() {
        return lastExportTime;
    }

    public boolean isAutoExportEnabled() {
        return autoExportEnabled;
    }

    public void setAutoExportEnabled(boolean autoExportEnabled) {
        this.autoExportEnabled = autoExportEnabled;
    }

    public String getExportDirectory() {
        return exportDirectory;
    }

    public void setExportDirectory(String exportDirectory) {
        this.exportDirectory = exportDirectory;
    }
}