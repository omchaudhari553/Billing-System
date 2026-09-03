package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.FAQRequest;
import com.ajalkarbill.website.dto.FAQResponse;

import java.util.List;

public interface FAQServiceAdmin {
    List<FAQResponse> getAllFAQs();
    FAQResponse getFAQById(Long id);
    FAQResponse createFAQ(FAQRequest request);
    FAQResponse updateFAQ(Long id, FAQRequest request);
    void deleteFAQ(Long id);
    List<FAQResponse> getActiveFAQs();
}
