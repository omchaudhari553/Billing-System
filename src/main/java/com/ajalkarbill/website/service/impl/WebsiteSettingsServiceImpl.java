package com.ajalkarbill.website.service.impl;

import com.ajalkarbill.website.dto.WebsiteSettingsRequest;
import com.ajalkarbill.website.dto.WebsiteSettingsResponse;
import com.ajalkarbill.website.entity.WebsiteSettings;
import com.ajalkarbill.website.repository.WebsiteSettingsRepository;
import com.ajalkarbill.website.service.WebsiteSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WebsiteSettingsServiceImpl implements WebsiteSettingsService {

    private final WebsiteSettingsRepository websiteSettingsRepository;

    public WebsiteSettingsServiceImpl(WebsiteSettingsRepository websiteSettingsRepository) {
        this.websiteSettingsRepository = websiteSettingsRepository;
    }

    @Override
    public WebsiteSettingsResponse getWebsiteSettings() {
        WebsiteSettings settings = websiteSettingsRepository.findFirstByOrderByIdAsc()
                .orElse(new WebsiteSettings());
        return mapToResponse(settings);
    }

    @Override
    public WebsiteSettingsResponse updateWebsiteSettings(WebsiteSettingsRequest request) {
        WebsiteSettings settings = websiteSettingsRepository.findFirstByOrderByIdAsc()
                .orElse(new WebsiteSettings());
        
        settings.setWebsiteName(request.getWebsiteName());
        settings.setLogoUrl(request.getLogoUrl());
        settings.setFaviconUrl(request.getFaviconUrl());
        settings.setContactEmail(request.getContactEmail());
        settings.setContactPhone(request.getContactPhone());
        settings.setAddress(request.getAddress());
        settings.setWhatsappNumber(request.getWhatsappNumber());
        settings.setFacebookUrl(request.getFacebookUrl());
        settings.setTwitterUrl(request.getTwitterUrl());
        settings.setInstagramUrl(request.getInstagramUrl());
        settings.setLinkedinUrl(request.getLinkedinUrl());
        settings.setFooterContent(request.getFooterContent());
        settings.setCopyrightText(request.getCopyrightText());
        
        WebsiteSettings savedSettings = websiteSettingsRepository.save(settings);
        return mapToResponse(savedSettings);
    }

    private WebsiteSettingsResponse mapToResponse(WebsiteSettings settings) {
        WebsiteSettingsResponse response = new WebsiteSettingsResponse();
        response.setId(settings.getId());
        response.setWebsiteName(settings.getWebsiteName());
        response.setLogoUrl(settings.getLogoUrl());
        response.setFaviconUrl(settings.getFaviconUrl());
        response.setContactEmail(settings.getContactEmail());
        response.setContactPhone(settings.getContactPhone());
        response.setAddress(settings.getAddress());
        response.setWhatsappNumber(settings.getWhatsappNumber());
        response.setFacebookUrl(settings.getFacebookUrl());
        response.setTwitterUrl(settings.getTwitterUrl());
        response.setInstagramUrl(settings.getInstagramUrl());
        response.setLinkedinUrl(settings.getLinkedinUrl());
        response.setFooterContent(settings.getFooterContent());
        response.setCopyrightText(settings.getCopyrightText());
        response.setCreatedAt(settings.getCreatedAt());
        response.setUpdatedAt(settings.getUpdatedAt());
        return response;
    }
}
