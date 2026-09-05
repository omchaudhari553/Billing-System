package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.WebsiteActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WebsiteActivityRepository extends JpaRepository<WebsiteActivity, Long> {

    List<WebsiteActivity> findByVisitorIdOrderByCreatedAtDesc(String visitorId);

    List<WebsiteActivity> findByEventTypeOrderByCreatedAtDesc(WebsiteActivity.ActivityEventType eventType);

    @Query("SELECT COUNT(a) FROM WebsiteActivity a WHERE a.eventType = :eventType")
    Long countByEventType(@Param("eventType") WebsiteActivity.ActivityEventType eventType);

    @Query("SELECT COUNT(a) FROM WebsiteActivity a WHERE DATE(a.createdAt) = CURRENT_DATE")
    Long countTodayActivities();

    @Query("SELECT a.pageUrl, COUNT(a) as visitCount FROM WebsiteActivity a WHERE a.eventType = 'PAGE_VIEW' GROUP BY a.pageUrl ORDER BY visitCount DESC")
    List<Object[]> findMostVisitedPages();

    @Query("SELECT a.additionalData, COUNT(a) as viewCount FROM WebsiteActivity a WHERE a.eventType = 'FEATURE_VIEW' GROUP BY a.additionalData ORDER BY viewCount DESC")
    List<Object[]> findMostViewedFeatures();

    @Query("SELECT a.additionalData, COUNT(a) as viewCount FROM WebsiteActivity a WHERE a.eventType = 'MODULE_VIEW' GROUP BY a.additionalData ORDER BY viewCount DESC")
    List<Object[]> findMostViewedModules();

    @Query("SELECT a.additionalData, COUNT(a) as viewCount FROM WebsiteActivity a WHERE a.eventType = 'FAQ_VIEW' GROUP BY a.additionalData ORDER BY viewCount DESC")
    List<Object[]> findMostViewedFAQs();
}