package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.DownloadInfoDto;
import com.ajalkarbill.website.service.DownloadInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/downloads")
public class DownloadController {
    private final DownloadInfoService service;

    public DownloadController(DownloadInfoService service) {
        this.service = service;
    }

    @GetMapping
    public List<DownloadInfoDto> getAllDownloads() {
        return service.getAllDownloads();
    }

    @GetMapping("/latest")
    public DownloadInfoDto getLatestDownload() {
        return service.getLatestDownloadInfo();
    }

    @GetMapping("/{id}")
    public DownloadInfoDto getDownloadById(@PathVariable Long id) {
        return service.getDownloadById(id);
    }

    @GetMapping("/{id}/file")
    public DownloadInfoDto getDownloadFile(@PathVariable Long id) {
        return service.getDownloadFile(id);
    }
}
