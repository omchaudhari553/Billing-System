package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.ContactEnquiryRequest;
import com.ajalkarbill.website.entity.ContactEnquiry;
import com.ajalkarbill.website.repository.ContactEnquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ContactEnquiryService {

    @Autowired
    private ContactEnquiryRepository repository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public ContactEnquiry createEnquiry(ContactEnquiryRequest request, String ipAddress) {

        ContactEnquiry enquiry = new ContactEnquiry();

        enquiry.setFullName(request.getFullName());
        enquiry.setCompanyName(request.getCompanyName());
        enquiry.setEmail(request.getEmail());
        enquiry.setMobileNumber(request.getMobileNumber());
        enquiry.setSubject(request.getSubject());
        enquiry.setEnquiryType(
                request.getEnquiryType() != null ? request.getEnquiryType() : "General Enquiry");
        enquiry.setMessage(request.getMessage());
        enquiry.setPageSource(
                request.getPageSource() != null ? request.getPageSource() : "Unknown");
        enquiry.setIpAddress(ipAddress);

        // Generate Enquiry ID BEFORE saving
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String unique = String.valueOf(System.currentTimeMillis()).substring(7);
        enquiry.setEnquiryId("AJB-ENQ-" + dateStr + "-" + unique);

        enquiry = repository.save(enquiry);

        // Send Emails asynchronously
        ContactEnquiry savedEnquiry = enquiry;
        new Thread(() -> {
            emailService.sendAdminNotification(savedEnquiry);
            emailService.sendCustomerConfirmation(savedEnquiry);
        }).start();

        return enquiry;
    }

    public Page<ContactEnquiry> getEnquiries(
            String search,
            ContactEnquiry.EnquiryStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {

        return repository.findWithFilters(search, status, startDate, endDate, pageable);
    }

    @Transactional
    public ContactEnquiry updateStatus(Long id, ContactEnquiry.EnquiryStatus status) {

        Optional<ContactEnquiry> opt = repository.findById(id);

        if (opt.isPresent()) {
            ContactEnquiry enquiry = opt.get();
            enquiry.setStatus(status);
            return repository.save(enquiry);
        }

        throw new RuntimeException("Enquiry not found");
    }

    @Transactional
    public void deleteEnquiry(Long id) {
        repository.deleteById(id);
    }
}