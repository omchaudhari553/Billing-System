package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.WebsiteLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WebsiteLeadRepository extends JpaRepository<WebsiteLead, Long> {

    Optional<WebsiteLead> findByPhoneNumber(String phoneNumber);

    Optional<WebsiteLead> findByEmail(String email);

    Optional<WebsiteLead> findByVisitorId(String visitorId);

    Optional<WebsiteLead> findByVisitorIdAndPhoneNumber(String visitorId, String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByVisitorIdAndPhoneNumber(String visitorId, String phoneNumber);

    List<WebsiteLead> findBySourceOrderByCreatedAtDesc(WebsiteLead.LeadSource source);

    List<WebsiteLead> findByStatusOrderByCreatedAtDesc(String status);

    @Query("SELECT COUNT(l) FROM WebsiteLead l WHERE DATE(l.createdAt) = CURRENT_DATE")
    Long countTodayLeads();

    @Query("SELECT COUNT(l) FROM WebsiteLead l WHERE l.source = :source")
    Long countBySource(@Param("source") WebsiteLead.LeadSource source);

    @Query("SELECT l.source, COUNT(l) as leadCount FROM WebsiteLead l GROUP BY l.source ORDER BY leadCount DESC")
    List<Object[]> findLeadsBySource();
}