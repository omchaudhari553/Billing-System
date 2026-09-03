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

    public DownloadInfoDto getLatestDownloadInfo() {
        List<DownloadInfo> infos = repository.findAll();
        if (infos.isEmpty()) return null;
        DownloadInfo info = infos.get(0);
        return new DownloadInfoDto(info.getId(), info.getLatestVersion(), info.getDownloadLink(), info.getReleaseDate(), info.getOsSupport());
    }
}
