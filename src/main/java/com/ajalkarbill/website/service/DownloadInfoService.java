package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.DownloadInfoDto;
import com.ajalkarbill.website.entity.DownloadInfo;
import com.ajalkarbill.website.repository.DownloadInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DownloadInfoService {
    private final DownloadInfoRepository repository;

    public DownloadInfoService(DownloadInfoRepository repository) {
        this.repository = repository;
    }

    public List<DownloadInfoDto> getAllDownloads() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public DownloadInfoDto getDownloadById(Long id) {
        DownloadInfo info = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Download not found with id: " + id));
        return mapToDto(info);
    }

    public DownloadInfoDto getLatestDownloadInfo() {
        DownloadInfo info = repository.findFirstByOrderByIdDesc();
        if (info == null)
            return null;
        return mapToDto(info);
    }

    public DownloadInfoDto getDownloadFile(Long id) {
        DownloadInfo info = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Download not found with id: " + id));
        return mapToDto(info);
    }

    private DownloadInfoDto mapToDto(DownloadInfo info) {
        return new DownloadInfoDto(
                info.getId(),
                info.getLatestVersion(),
                info.getDownloadLink(),
                info.getReleaseDate(),
                info.getOsSupport(),
                info.getFileName(),
                info.getFileSize());
    }
}
