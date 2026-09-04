package com.ajalkarbill.website.dto;

public record DownloadInfoDto(Long id, String latestVersion, String downloadLink, String releaseDate, String osSupport,
        String fileName, Long fileSize) {
}
