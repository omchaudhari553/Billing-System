package com.ajalkarbill.website.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FAQRequest {
    
    @NotBlank(message = "Question is required")
    private String question;
    
    @NotBlank(message = "Answer is required")
    private String answer;
    
    @NotNull(message = "Active status is required")
    private Boolean isActive;
    
    @NotNull(message = "Display order is required")
    private Integer displayOrder;

    public FAQRequest() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
