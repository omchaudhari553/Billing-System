package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.TestimonialDto;
import com.ajalkarbill.website.dto.TestimonialSummaryDto;
import com.ajalkarbill.website.service.TestimonialService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testimonials")
public class TestimonialController {
    private final TestimonialService service;

    public TestimonialController(TestimonialService service) {
        this.service = service;
    }

    @GetMapping
    public List<TestimonialDto> getTestimonials() {
        return service.getAllTestimonials();
    }

    @GetMapping("/featured")
    public List<TestimonialDto> getFeaturedTestimonials() {
        return service.getFeaturedTestimonials();
    }

    @GetMapping("/rating/{rating}")
    public List<TestimonialDto> getTestimonialsByRating(@PathVariable int rating) {
        return service.getTestimonialsByRating(rating);
    }

    @GetMapping("/summary")
    public TestimonialSummaryDto getTestimonialSummary() {
        return service.getTestimonialSummary();
    }
}
