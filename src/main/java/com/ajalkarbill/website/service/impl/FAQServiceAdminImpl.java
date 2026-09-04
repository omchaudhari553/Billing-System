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
        faq.setCategory(request.getCategory());
        faq.setHelpfulCount(request.getHelpfulCount() != null ? request.getHelpfulCount() : 0);
        faq.setIsFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false);
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
        faq.setCategory(request.getCategory());
        if (request.getHelpfulCount() != null) {
            faq.setHelpfulCount(request.getHelpfulCount());
        }
        if (request.getIsFeatured() != null) {
            faq.setIsFeatured(request.getIsFeatured());
        }
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

    public List<String> getCategories() {
        return faqRepository.findDistinctCategoryByIsActiveTrue();
    }

    public List<FAQResponse> getFAQsByCategory(String category) {
        return faqRepository.findByCategoryAndIsActiveTrue(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<FAQResponse> searchFAQs(String keyword) {
        return faqRepository.searchFAQs(keyword).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<FAQResponse> getPopularFAQs() {
        return faqRepository.findByIsFeaturedTrueAndIsActiveTrueOrderByHelpfulCountDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void markAsHelpful(Long id) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
        Integer currentCount = faq.getHelpfulCount();
        faq.setHelpfulCount(currentCount != null ? currentCount + 1 : 1);
        faqRepository.save(faq);
    }

    private FAQResponse mapToResponse(FAQ faq) {
        FAQResponse response = new FAQResponse();
        response.setId(faq.getId());
        response.setQuestion(faq.getQuestion());
        response.setAnswer(faq.getAnswer());
        response.setCategory(faq.getCategory());
        response.setHelpfulCount(faq.getHelpfulCount());
        response.setIsFeatured(faq.getIsFeatured());
        response.setIsActive(faq.getIsActive());
        response.setDisplayOrder(faq.getDisplayOrder());
        response.setCreatedAt(faq.getCreatedAt());
        response.setUpdatedAt(faq.getUpdatedAt());
        return response;
    }
}
