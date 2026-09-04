package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.TestimonialDto;
import com.ajalkarbill.website.dto.TestimonialSummaryDto;
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
                .map(t -> new TestimonialDto(t.getId(), t.getCustomerName(), t.getBusinessName(), t.getReview(),
                        t.getStarRating(), t.getPhotoUrl(), t.getIsFeatured()))
                .toList();
    }

    public List<TestimonialDto> getFeaturedTestimonials() {
        return repository.findByIsFeaturedTrue().stream()
                .map(t -> new TestimonialDto(t.getId(), t.getCustomerName(), t.getBusinessName(), t.getReview(),
                        t.getStarRating(), t.getPhotoUrl(), t.getIsFeatured()))
                .toList();
    }

    public List<TestimonialDto> getTestimonialsByRating(int rating) {
        return repository.findByStarRating(rating).stream()
                .map(t -> new TestimonialDto(t.getId(), t.getCustomerName(), t.getBusinessName(), t.getReview(),
                        t.getStarRating(), t.getPhotoUrl(), t.getIsFeatured()))
                .toList();
    }

    public TestimonialSummaryDto getTestimonialSummary() {
        List<com.ajalkarbill.website.entity.Testimonial> all = repository.findAll();
        int total = all.size();
        double average = all.stream()
                .mapToInt(com.ajalkarbill.website.entity.Testimonial::getStarRating)
                .average()
                .orElse(0.0);

        int fiveStar = (int) all.stream().filter(t -> t.getStarRating() == 5).count();
        int fourStar = (int) all.stream().filter(t -> t.getStarRating() == 4).count();
        int threeStar = (int) all.stream().filter(t -> t.getStarRating() == 3).count();
        int twoStar = (int) all.stream().filter(t -> t.getStarRating() == 2).count();
        int oneStar = (int) all.stream().filter(t -> t.getStarRating() == 1).count();

        return new TestimonialSummaryDto(total, average, fiveStar, fourStar, threeStar, twoStar, oneStar);
    }
}
