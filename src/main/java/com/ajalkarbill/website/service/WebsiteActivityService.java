package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.ActivityTrackRequest;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.entity.WebsiteActivity;
import com.ajalkarbill.website.repository.UserRepository;
import com.ajalkarbill.website.repository.WebsiteActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WebsiteActivityService {

    private final WebsiteActivityRepository activityRepository;
    private final UserRepository userRepository;

    public WebsiteActivityService(WebsiteActivityRepository activityRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WebsiteActivity trackActivity(ActivityTrackRequest request, Long userId) {
        WebsiteActivity activity = new WebsiteActivity();
        activity.setVisitorId(request.getVisitorId());
        activity.setSessionId(request.getSessionId());
        activity.setEventType(request.getEventType());
        activity.setPageUrl(request.getPageUrl());
        activity.setPageTitle(request.getPageTitle());
        activity.setAdditionalData(request.getAdditionalData());

        if (userId != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            userOpt.ifPresent(activity::setUser);
        }

        return activityRepository.save(activity);
    }

    public List<WebsiteActivity> getActivitiesByVisitorId(String visitorId) {
        return activityRepository.findByVisitorIdOrderByCreatedAtDesc(visitorId);
    }

    public List<WebsiteActivity> getActivitiesByEventType(WebsiteActivity.ActivityEventType eventType) {
        return activityRepository.findByEventTypeOrderByCreatedAtDesc(eventType);
    }

    public Long getTodayActivities() {
        return activityRepository.countTodayActivities();
    }

    public Long countByEventType(WebsiteActivity.ActivityEventType eventType) {
        return activityRepository.countByEventType(eventType);
    }

    public List<Object[]> getMostVisitedPages() {
        return activityRepository.findMostVisitedPages();
    }

    public List<Object[]> getMostViewedFeatures() {
        return activityRepository.findMostViewedFeatures();
    }

    public List<Object[]> getMostViewedModules() {
        return activityRepository.findMostViewedModules();
    }

    public List<Object[]> getMostViewedFAQs() {
        return activityRepository.findMostViewedFAQs();
    }
}