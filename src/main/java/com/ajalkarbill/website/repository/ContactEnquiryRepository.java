package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.ContactEnquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ContactEnquiryRepository extends JpaRepository<ContactEnquiry, Long> {
    
    @Query("SELECT e FROM ContactEnquiry e WHERE " +
           "(:search IS NULL OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(e.enquiryId) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:startDate IS NULL OR e.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR e.createdAt <= :endDate)")
    Page<ContactEnquiry> findWithFilters(
            @Param("search") String search,
            @Param("status") ContactEnquiry.EnquiryStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
