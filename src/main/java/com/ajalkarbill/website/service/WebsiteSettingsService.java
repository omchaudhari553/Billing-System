package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.WebsiteSettingsRequest;
import com.ajalkarbill.website.dto.WebsiteSettingsResponse;

public interface WebsiteSettingsService {
    WebsiteSettingsResponse getWebsiteSettings();
    WebsiteSettingsResponse updateWebsiteSettings(WebsiteSettingsRequest request);
}
