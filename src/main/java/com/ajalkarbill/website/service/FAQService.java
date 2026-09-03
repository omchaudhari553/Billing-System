package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.FAQDto;
import com.ajalkarbill.website.repository.FAQRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FAQService {
    private final FAQRepository repository;

    public FAQService(FAQRepository repository) {
        this.repository = repository;
    }

    public List<FAQDto> getAllFAQs() {
        return repository.findAll().stream()
                .map(f -> new FAQDto(f.getId(), f.getQuestion(), f.getAnswer()))
                .toList();
    }
}
