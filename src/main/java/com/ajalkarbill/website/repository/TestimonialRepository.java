package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
}
