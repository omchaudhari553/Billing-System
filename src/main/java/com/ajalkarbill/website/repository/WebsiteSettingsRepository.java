package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.WebsiteSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebsiteSettingsRepository extends JpaRepository<WebsiteSettings, Long> {
    Optional<WebsiteSettings> findFirstByOrderByIdAsc();
}
