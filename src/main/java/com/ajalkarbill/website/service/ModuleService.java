package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.ModuleDto;
import com.ajalkarbill.website.entity.Module;
import com.ajalkarbill.website.repository.ModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {
    private final ModuleRepository repository;

    public ModuleService(ModuleRepository repository) {
        this.repository = repository;
    }

    public List<ModuleDto> getAllModules() {
        return repository.findAll().stream()
                .map(m -> new ModuleDto(m.getId(), m.getName(), m.getDescription(), m.getIcon()))
                .toList();
    }
}
