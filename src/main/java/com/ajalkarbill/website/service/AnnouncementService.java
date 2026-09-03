package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.AnnouncementRequest;
import com.ajalkarbill.website.dto.AnnouncementResponse;

import java.util.List;

public interface AnnouncementService {
    List<AnnouncementResponse> getAllAnnouncements();
    AnnouncementResponse getAnnouncementById(Long id);
    AnnouncementResponse createAnnouncement(AnnouncementRequest request);
    AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest request);
    void deleteAnnouncement(Long id);
    List<AnnouncementResponse> getActiveAnnouncements();
}
