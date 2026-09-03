package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.FAQResponse;
import com.ajalkarbill.website.service.FAQServiceAdmin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
public class FAQController {
    private final FAQServiceAdmin service;

    public FAQController(FAQServiceAdmin service) {
        this.service = service;
    }

    @GetMapping
    public List<FAQResponse> getFAQs() {
        return service.getActiveFAQs();
    }
}
