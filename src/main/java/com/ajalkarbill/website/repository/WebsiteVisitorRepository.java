package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.WebsiteVisitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WebsiteVisitorRepository extends JpaRepository<WebsiteVisitor, Long> {

    Optional<WebsiteVisitor> findByVisitorId(String visitorId);

    Optional<WebsiteVisitor> findBySessionId(String sessionId);

    boolean existsByVisitorId(String visitorId);

    @Query("SELECT COUNT(v) FROM WebsiteVisitor v WHERE DATE(v.createdAt) = CURRENT_DATE")
    Long countTodayVisitors();

    @Query("SELECT COUNT(DISTINCT v.visitorId) FROM WebsiteVisitor v")
    Long countUniqueVisitors();

    @Query("SELECT COUNT(v) FROM WebsiteVisitor v WHERE v.user IS NOT NULL")
    Long countRegisteredVisitors();
}