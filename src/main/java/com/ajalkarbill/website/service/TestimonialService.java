package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.TestimonialDto;
import com.ajalkarbill.website.repository.TestimonialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestimonialService {
    private final TestimonialRepository repository;

    public TestimonialService(TestimonialRepository repository) {
        this.repository = repository;
    }

    public List<TestimonialDto> getAllTestimonials() {
        return repository.findAll().stream()
                .map(t -> new TestimonialDto(t.getId(), t.getCustomerName(), t.getBusinessName(), t.getReview(), t.getStarRating(), t.getPhotoUrl()))
                .toList();
    }
}
