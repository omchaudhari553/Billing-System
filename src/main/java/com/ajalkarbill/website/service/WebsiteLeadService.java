package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.LeadRequestDto;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.entity.WebsiteLead;
import com.ajalkarbill.website.exception.DuplicateEmailException;
import com.ajalkarbill.website.exception.DuplicateMobileException;
import com.ajalkarbill.website.repository.UserRepository;
import com.ajalkarbill.website.repository.WebsiteLeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Service
public class WebsiteLeadService {

    private final WebsiteLeadRepository leadRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public WebsiteLeadService(WebsiteLeadRepository leadRepository, UserRepository userRepository, EmailService emailService) {
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Map<String, Object> createLead(LeadRequestDto request) {
        Map<String, Object> response = new HashMap<>();

        if (leadRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateMobileException("Phone number already registered as a lead");
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty() && leadRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already registered as a lead");
        }

        WebsiteLead lead = new WebsiteLead();
        lead.setFullName(request.getFullName());
        lead.setPhoneNumber(request.getPhoneNumber());
        lead.setEmail(request.getEmail());
        lead.setBusinessName(request.getBusinessName());
        lead.setSource(request.getSource());
        lead.setMessage(request.getMessage());
        lead.setVisitorId(request.getVisitorId());

        WebsiteLead savedLead = leadRepository.save(lead);

        response.put("success", true);
        response.put("message", "Lead submitted successfully");
        response.put("leadId", savedLead.getId());

        return response;
    }

    @Transactional
    public void associateLeadWithUser(Long leadId, Long userId) {
        Optional<WebsiteLead> leadOpt = leadRepository.findById(leadId);
        if (leadOpt.isPresent()) {
            WebsiteLead lead = leadOpt.get();
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                lead.setUser(userOpt.get());
                leadRepository.save(lead);
            }
        }
    }

    public List<WebsiteLead> getLeadsBySource(WebsiteLead.LeadSource source) {
        return leadRepository.findBySourceOrderByCreatedAtDesc(source);
    }

    public List<WebsiteLead> getLeadsByStatus(String status) {
        return leadRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public Long getTotalLeads() {
        return leadRepository.count();
    }

    public Long getTodayLeads() {
        return leadRepository.countTodayLeads();
    }

    public Long countBySource(WebsiteLead.LeadSource source) {
        return leadRepository.countBySource(source);
    }

    public List<Object[]> getLeadsBySource() {
        return leadRepository.findLeadsBySource();
    }
}