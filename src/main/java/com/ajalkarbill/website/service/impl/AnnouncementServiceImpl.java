package com.ajalkarbill.website.service.impl;

import com.ajalkarbill.website.dto.AnnouncementRequest;
import com.ajalkarbill.website.dto.AnnouncementResponse;
import com.ajalkarbill.website.entity.Announcement;
import com.ajalkarbill.website.repository.AnnouncementRepository;
import com.ajalkarbill.website.service.AnnouncementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncements() {
        return announcementRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AnnouncementResponse getAnnouncementById(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        return mapToResponse(announcement);
    }

    @Override
    public AnnouncementResponse createAnnouncement(AnnouncementRequest request) {
        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setDescription(request.getDescription());
        announcement.setButtonText(request.getButtonText());
        announcement.setButtonLink(request.getButtonLink());
        announcement.setImageUrl(request.getImageUrl());
        announcement.setIsActive(request.getIsActive());
        announcement.setStartDate(request.getStartDate());
        announcement.setEndDate(request.getEndDate());
        
        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return mapToResponse(savedAnnouncement);
    }

    @Override
    public AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest request) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        
        announcement.setTitle(request.getTitle());
        announcement.setDescription(request.getDescription());
        announcement.setButtonText(request.getButtonText());
        announcement.setButtonLink(request.getButtonLink());
        announcement.setImageUrl(request.getImageUrl());
        announcement.setIsActive(request.getIsActive());
        announcement.setStartDate(request.getStartDate());
        announcement.setEndDate(request.getEndDate());
        
        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return mapToResponse(savedAnnouncement);
    }

    @Override
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        announcementRepository.delete(announcement);
    }

    @Override
    public List<AnnouncementResponse> getActiveAnnouncements() {
        LocalDateTime now = LocalDateTime.now();
        return announcementRepository.findByIsActiveTrueAndStartDateBeforeAndEndDateAfterOrderByCreatedAtDesc(now, now).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AnnouncementResponse mapToResponse(Announcement announcement) {
        AnnouncementResponse response = new AnnouncementResponse();
        response.setId(announcement.getId());
        response.setTitle(announcement.getTitle());
        response.setDescription(announcement.getDescription());
        response.setButtonText(announcement.getButtonText());
        response.setButtonLink(announcement.getButtonLink());
        response.setImageUrl(announcement.getImageUrl());
        response.setIsActive(announcement.getIsActive());
        response.setStartDate(announcement.getStartDate());
        response.setEndDate(announcement.getEndDate());
        response.setCreatedAt(announcement.getCreatedAt());
        response.setUpdatedAt(announcement.getUpdatedAt());
        return response;
    }
}
