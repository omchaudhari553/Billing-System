package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.TestimonialDto;
import com.ajalkarbill.website.service.TestimonialService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
