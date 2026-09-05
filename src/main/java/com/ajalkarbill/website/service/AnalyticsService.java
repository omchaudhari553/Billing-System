package com.ajalkarbill.website.service;

import com.ajalkarbill.website.entity.WebsiteActivity;
import com.ajalkarbill.website.entity.WebsiteLead;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final WebsiteVisitorService visitorService;
    private final WebsiteActivityService activityService;
    private final WebsiteLeadService leadService;

    public AnalyticsService(WebsiteVisitorService visitorService,
            WebsiteActivityService activityService,
            WebsiteLeadService leadService) {
        this.visitorService = visitorService;
        this.activityService = activityService;
        this.leadService = leadService;
    }

    public Map<String, Object> getOverallStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Visitor statistics
        stats.put("totalVisitors", visitorService.getTotalVisitors());
        stats.put("todayVisitors", visitorService.getTodayVisitors());
        stats.put("uniqueVisitors", visitorService.getUniqueVisitors());
        stats.put("registeredVisitors", visitorService.getRegisteredVisitors());

        // Activity statistics
        stats.put("todayActivities", activityService.getTodayActivities());
        stats.put("pageViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.PAGE_VIEW));
        stats.put("registrations", activityService.countByEventType(WebsiteActivity.ActivityEventType.REGISTRATION));
        stats.put("logins", activityService.countByEventType(WebsiteActivity.ActivityEventType.LOGIN));
        stats.put("contactEnquiries",
                activityService.countByEventType(WebsiteActivity.ActivityEventType.CONTACT_ENQUIRY));
        stats.put("downloads", activityService.countByEventType(WebsiteActivity.ActivityEventType.DOWNLOAD));
        stats.put("faqViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.FAQ_VIEW));
        stats.put("featureViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.FEATURE_VIEW));
        stats.put("moduleViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.MODULE_VIEW));
        stats.put("leadSubmitted", activityService.countByEventType(WebsiteActivity.ActivityEventType.LEAD_SUBMITTED));
        stats.put("pricingViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.PRICING_VIEW));
        stats.put("homeViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.HOME_VIEW));

        // Lead statistics
        stats.put("totalLeads", leadService.getTotalLeads());
        stats.put("todayLeads", leadService.getTodayLeads());

        return stats;
    }

    public Map<String, Object> getVisitorStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalVisitors", visitorService.getTotalVisitors());
        stats.put("todayVisitors", visitorService.getTodayVisitors());
        stats.put("uniqueVisitors", visitorService.getUniqueVisitors());
        stats.put("registeredVisitors", visitorService.getRegisteredVisitors());

        return stats;
    }

    public Map<String, Object> getActivityStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("todayActivities", activityService.getTodayActivities());
        stats.put("pageViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.PAGE_VIEW));
        stats.put("registrations", activityService.countByEventType(WebsiteActivity.ActivityEventType.REGISTRATION));
        stats.put("logins", activityService.countByEventType(WebsiteActivity.ActivityEventType.LOGIN));
        stats.put("contactEnquiries",
                activityService.countByEventType(WebsiteActivity.ActivityEventType.CONTACT_ENQUIRY));
        stats.put("downloads", activityService.countByEventType(WebsiteActivity.ActivityEventType.DOWNLOAD));
        stats.put("faqViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.FAQ_VIEW));
        stats.put("featureViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.FEATURE_VIEW));
        stats.put("moduleViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.MODULE_VIEW));
        stats.put("leadSubmitted", activityService.countByEventType(WebsiteActivity.ActivityEventType.LEAD_SUBMITTED));
        stats.put("pricingViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.PRICING_VIEW));
        stats.put("homeViews", activityService.countByEventType(WebsiteActivity.ActivityEventType.HOME_VIEW));

        return stats;
    }

    public Map<String, Object> getLeadStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalLeads", leadService.getTotalLeads());
        stats.put("todayLeads", leadService.getTodayLeads());

        // Leads by source
        Map<String, Long> leadsBySource = new HashMap<>();
        leadsBySource.put("FREE_DEMO", leadService.countBySource(WebsiteLead.LeadSource.FREE_DEMO));
        leadsBySource.put("REQUEST_CALLBACK", leadService.countBySource(WebsiteLead.LeadSource.REQUEST_CALLBACK));
        leadsBySource.put("CONTACT_US", leadService.countBySource(WebsiteLead.LeadSource.CONTACT_US));
        leadsBySource.put("DOWNLOAD", leadService.countBySource(WebsiteLead.LeadSource.DOWNLOAD));
        leadsBySource.put("PRICING_ENQUIRY", leadService.countBySource(WebsiteLead.LeadSource.PRICING_ENQUIRY));
        leadsBySource.put("WHATSAPP", leadService.countBySource(WebsiteLead.LeadSource.WHATSAPP));
        leadsBySource.put("NEWSLETTER", leadService.countBySource(WebsiteLead.LeadSource.NEWSLETTER));
        leadsBySource.put("OTHER", leadService.countBySource(WebsiteLead.LeadSource.OTHER));

        stats.put("leadsBySource", leadsBySource);

        return stats;
    }

    public Map<String, Object> getContentEngagement() {
        Map<String, Object> engagement = new HashMap<>();

        // Most visited pages
        List<Object[]> mostVisitedPages = activityService.getMostVisitedPages();
        engagement.put("mostVisitedPages", formatPageData(mostVisitedPages));

        // Most viewed features
        List<Object[]> mostViewedFeatures = activityService.getMostViewedFeatures();
        engagement.put("mostViewedFeatures", formatFeatureData(mostViewedFeatures));

        // Most viewed modules
        List<Object[]> mostViewedModules = activityService.getMostViewedModules();
        engagement.put("mostViewedModules", formatModuleData(mostViewedModules));

        // Most viewed FAQs
        List<Object[]> mostViewedFAQs = activityService.getMostViewedFAQs();
        engagement.put("mostViewedFAQs", formatFAQData(mostViewedFAQs));

        return engagement;
    }

    public Map<String, Object> getDailyStatistics(LocalDate date) {
        Map<String, Object> stats = new HashMap<>();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        // Note: This would require additional repository methods for date range queries
        // For now, we'll return the overall statistics as a placeholder
        stats.put("date", date);
        stats.put("message", "Date-specific statistics require additional repository methods");

        return stats;
    }

    private List<Map<String, Object>> formatPageData(List<Object[]> data) {
        return data.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("pageUrl", row[0]);
                    item.put("pageTitle", row[1]);
                    item.put("viewCount", row[2]);
                    return item;
                })
                .toList();
    }

    private List<Map<String, Object>> formatFeatureData(List<Object[]> data) {
        return data.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("featureId", row[0]);
                    item.put("featureName", row[1]);
                    item.put("viewCount", row[2]);
                    return item;
                })
                .toList();
    }

    private List<Map<String, Object>> formatModuleData(List<Object[]> data) {
        return data.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("moduleId", row[0]);
                    item.put("moduleName", row[1]);
                    item.put("viewCount", row[2]);
                    return item;
                })
                .toList();
    }

    private List<Map<String, Object>> formatFAQData(List<Object[]> data) {
        return data.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("faqId", row[0]);
                    item.put("question", row[1]);
                    item.put("viewCount", row[2]);
                    return item;
                })
                .toList();
    }
}