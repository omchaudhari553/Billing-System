package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByIsActiveTrueAndStartDateBeforeAndEndDateAfterOrderByCreatedAtDesc(
            LocalDateTime beforeDate, LocalDateTime afterDate);
}
