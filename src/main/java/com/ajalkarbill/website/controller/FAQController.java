package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.FAQResponse;
import com.ajalkarbill.website.service.FAQServiceAdmin;
import com.ajalkarbill.website.service.impl.FAQServiceAdminImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faqs")
public class FAQController {
    private final FAQServiceAdmin service;
    private final FAQServiceAdminImpl serviceImpl;

    public FAQController(FAQServiceAdmin service, FAQServiceAdminImpl serviceImpl) {
        this.service = service;
        this.serviceImpl = serviceImpl;
    }

    @GetMapping
    public List<FAQResponse> getFAQs() {
        return service.getActiveFAQs();
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        List<String> categories = serviceImpl.getCategories();
        return categories != null ? categories : List.of();
    }

    @GetMapping("/search")
    public List<FAQResponse> searchFAQs(@RequestParam String keyword) {
        return serviceImpl.searchFAQs(keyword);
    }

    @GetMapping("/popular")
    public List<FAQResponse> getPopularFAQs() {
        return serviceImpl.getPopularFAQs();
    }

    @PostMapping("/{id}/helpful")
    public ResponseEntity<?> markAsHelpful(@PathVariable Long id) {
        try {
            serviceImpl.markAsHelpful(id);
            return ResponseEntity.ok().body(Map.of("message", "FAQ marked as helpful successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}
