package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.LeadRequestDto;
import com.ajalkarbill.website.dto.PhoneCaptureDto;
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

    public WebsiteLeadService(WebsiteLeadRepository leadRepository, UserRepository userRepository,
            EmailService emailService) {
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

        if (request.getEmail() != null && !request.getEmail().isEmpty()
                && leadRepository.existsByEmail(request.getEmail())) {
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
    public Map<String, Object> capturePhone(PhoneCaptureDto request) {
        Map<String, Object> response = new HashMap<>();

        // Check if phone number already belongs to a registered user
        if (userRepository.existsByMobileNumber(request.getPhoneNumber())) {
            throw new DuplicateMobileException("Phone number already registered as a user");
        }

        // Check if same visitor already submitted this phone number
        if (request.getVisitorId() != null
                && leadRepository.existsByVisitorIdAndPhoneNumber(request.getVisitorId(), request.getPhoneNumber())) {
            // Return existing lead instead of creating duplicate
            Optional<WebsiteLead> existingLead = leadRepository.findByVisitorIdAndPhoneNumber(request.getVisitorId(),
                    request.getPhoneNumber());
            if (existingLead.isPresent()) {
                response.put("success", true);
                response.put("message", "Phone number already captured for this visitor");
                response.put("leadId", existingLead.get().getId());
                response.put("registered", existingLead.get().getRegistered());
                return response;
            }
        }

        // Check if phone number exists as a lead from different visitor
        if (leadRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateMobileException("Phone number already captured by another visitor");
        }

        // Create new lead with minimal information
        WebsiteLead lead = new WebsiteLead();
        lead.setVisitorId(request.getVisitorId());
        lead.setPhoneNumber(request.getPhoneNumber());
        lead.setFullName(request.getFullName());
        lead.setEmail(request.getEmail());
        lead.setBusinessName(request.getBusinessName());
        lead.setSource(WebsiteLead.LeadSource.REGISTRATION_FORM);
        lead.setRegistered(false);

        WebsiteLead savedLead = leadRepository.save(lead);

        response.put("success", true);
        response.put("message", "Phone number captured successfully");
        response.put("leadId", savedLead.getId());
        response.put("registered", false);

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
                lead.setRegistered(true);
                leadRepository.save(lead);
            }
        }
    }

    @Transactional
    public void findAndAssociateLeadByVisitorAndPhone(String visitorId, String phoneNumber, Long userId) {
        // First try to find by visitorId and phoneNumber combination
        Optional<WebsiteLead> leadOpt = leadRepository.findByVisitorIdAndPhoneNumber(visitorId, phoneNumber);

        if (leadOpt.isPresent()) {
            // Found exact match - associate it
            associateLeadWithUser(leadOpt.get().getId(), userId);
        } else {
            // Try to find by visitorId only (in case phone number changed)
            Optional<WebsiteLead> byVisitorId = leadRepository.findByVisitorId(visitorId);
            if (byVisitorId.isPresent() && !byVisitorId.get().getRegistered()) {
                associateLeadWithUser(byVisitorId.get().getId(), userId);
            }
        }
    }

    public Optional<WebsiteLead> findByVisitorIdAndPhoneNumber(String visitorId, String phoneNumber) {
        return leadRepository.findByVisitorIdAndPhoneNumber(visitorId, phoneNumber);
    }

    public Optional<WebsiteLead> findByVisitorId(String visitorId) {
        return leadRepository.findByVisitorId(visitorId);
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