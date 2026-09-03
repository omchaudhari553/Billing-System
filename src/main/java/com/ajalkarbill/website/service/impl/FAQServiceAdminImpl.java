package com.ajalkarbill.website.service.impl;

import com.ajalkarbill.website.dto.FAQRequest;
import com.ajalkarbill.website.dto.FAQResponse;
import com.ajalkarbill.website.entity.FAQ;
import com.ajalkarbill.website.repository.FAQRepository;
import com.ajalkarbill.website.service.FAQServiceAdmin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FAQServiceAdminImpl implements FAQServiceAdmin {

    private final FAQRepository faqRepository;

    public FAQServiceAdminImpl(FAQRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    @Override
    public List<FAQResponse> getAllFAQs() {
        return faqRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FAQResponse getFAQById(Long id) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
        return mapToResponse(faq);
    }

    @Override
    public FAQResponse createFAQ(FAQRequest request) {
        FAQ faq = new FAQ();
        faq.setQuestion(request.getQuestion());
        faq.setAnswer(request.getAnswer());
        faq.setIsActive(request.getIsActive());
        faq.setDisplayOrder(request.getDisplayOrder());
        
        FAQ savedFAQ = faqRepository.save(faq);
        return mapToResponse(savedFAQ);
    }

    @Override
    public FAQResponse updateFAQ(Long id, FAQRequest request) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
        
        faq.setQuestion(request.getQuestion());
        faq.setAnswer(request.getAnswer());
        faq.setIsActive(request.getIsActive());
        faq.setDisplayOrder(request.getDisplayOrder());
        
        FAQ savedFAQ = faqRepository.save(faq);
        return mapToResponse(savedFAQ);
    }

    @Override
    public void deleteFAQ(Long id) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
        faqRepository.delete(faq);
    }

    @Override
    public List<FAQResponse> getActiveFAQs() {
        return faqRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private FAQResponse mapToResponse(FAQ faq) {
        FAQResponse response = new FAQResponse();
        response.setId(faq.getId());
        response.setQuestion(faq.getQuestion());
        response.setAnswer(faq.getAnswer());
        response.setIsActive(faq.getIsActive());
        response.setDisplayOrder(faq.getDisplayOrder());
        response.setCreatedAt(faq.getCreatedAt());
        response.setUpdatedAt(faq.getUpdatedAt());
        return response;
    }
}
