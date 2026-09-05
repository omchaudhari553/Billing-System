package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.VisitorTrackRequest;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.entity.WebsiteVisitor;
import com.ajalkarbill.website.repository.UserRepository;
import com.ajalkarbill.website.repository.WebsiteVisitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class WebsiteVisitorService {

    private final WebsiteVisitorRepository visitorRepository;
    private final UserRepository userRepository;

    public WebsiteVisitorService(WebsiteVisitorRepository visitorRepository, UserRepository userRepository) {
        this.visitorRepository = visitorRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WebsiteVisitor trackVisitor(VisitorTrackRequest request) {
        Optional<WebsiteVisitor> existingVisitor = visitorRepository.findByVisitorId(request.getVisitorId());

        WebsiteVisitor visitor;
        if (existingVisitor.isPresent()) {
            visitor = existingVisitor.get();
            visitor.setLastVisitAt(LocalDateTime.now());
            visitor.setLastActivityAt(LocalDateTime.now());
            visitor.setVisitCount(visitor.getVisitCount() + 1);
            
            if (request.getPageUrl() != null) {
                visitor.setLastPageVisited(request.getPageUrl());
            }
            if (request.getSessionId() != null) {
                visitor.setSessionId(request.getSessionId());
            }
            if (request.getUserAgent() != null) {
                visitor.setUserAgent(request.getUserAgent());
            }
            if (request.getDeviceType() != null) {
                visitor.setDeviceType(request.getDeviceType());
            }
            if (request.getOperatingSystem() != null) {
                visitor.setOperatingSystem(request.getOperatingSystem());
            }
            if (request.getReferrer() != null) {
                visitor.setReferrer(request.getReferrer());
            }
        } else {
            visitor = new WebsiteVisitor();
            visitor.setVisitorId(request.getVisitorId());
            visitor.setSessionId(request.getSessionId());
            visitor.setFirstVisitAt(LocalDateTime.now());
            visitor.setLastVisitAt(LocalDateTime.now());
            visitor.setLastActivityAt(LocalDateTime.now());
            visitor.setVisitCount(1);
            visitor.setLastPageVisited(request.getPageUrl());
            visitor.setUserAgent(request.getUserAgent());
            visitor.setDeviceType(request.getDeviceType());
            visitor.setOperatingSystem(request.getOperatingSystem());
            visitor.setReferrer(request.getReferrer());
        }

        return visitorRepository.save(visitor);
    }

    @Transactional
    public void associateVisitorWithUser(String visitorId, Long userId) {
        Optional<WebsiteVisitor> visitorOpt = visitorRepository.findByVisitorId(visitorId);
        if (visitorOpt.isPresent()) {
            WebsiteVisitor visitor = visitorOpt.get();
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                visitor.setUser(userOpt.get());
                visitorRepository.save(visitor);
            }
        }
    }

    public Optional<WebsiteVisitor> getVisitorById(String visitorId) {
        return visitorRepository.findByVisitorId(visitorId);
    }

    public Long getTotalVisitors() {
        return visitorRepository.count();
    }

    public Long getTodayVisitors() {
        return visitorRepository.countTodayVisitors();
    }

    public Long getUniqueVisitors() {
        return visitorRepository.countUniqueVisitors();
    }

    public Long getRegisteredVisitors() {
        return visitorRepository.countRegisteredVisitors();
    }
}