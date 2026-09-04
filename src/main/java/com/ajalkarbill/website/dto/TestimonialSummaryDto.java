package com.ajalkarbill.website.dto;

public record TestimonialSummaryDto(int totalTestimonials, double averageRating, int fiveStarCount, int fourStarCount, int threeStarCount, int twoStarCount, int oneStarCount) {
}