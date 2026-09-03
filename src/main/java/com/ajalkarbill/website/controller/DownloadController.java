package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.DownloadInfoDto;
import com.ajalkarbill.website.service.DownloadInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/download-info")
public class DownloadController {
    private final DownloadInfoService service;

    public DownloadController(DownloadInfoService service) {
        this.service = service;
    }

    @GetMapping
    public DownloadInfoDto getDownloadInfo() {
        return service.getLatestDownloadInfo();
    }
}
