package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.ModuleResponse;
import com.ajalkarbill.website.service.ModuleServiceAdmin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {
    private final ModuleServiceAdmin service;

    public ModuleController(ModuleServiceAdmin service) {
        this.service = service;
    }

    @GetMapping
    public List<ModuleResponse> getModules() {
        return service.getActiveModules();
    }
}
