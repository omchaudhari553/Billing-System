package com.ajalkarbill.website.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DownloadInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String latestVersion;
    private String downloadLink;
    private String releaseDate;
    private String osSupport;
    private String fileName;
    private Long fileSize;

    public DownloadInfo() {
    }

    public DownloadInfo(String latestVersion, String downloadLink, String releaseDate, String osSupport) {
        this.latestVersion = latestVersion;
        this.downloadLink = downloadLink;
        this.releaseDate = releaseDate;
        this.osSupport = osSupport;
    }

    public DownloadInfo(String latestVersion, String downloadLink, String releaseDate, String osSupport,
            String fileName, Long fileSize) {
        this.latestVersion = latestVersion;
        this.downloadLink = downloadLink;
        this.releaseDate = releaseDate;
        this.osSupport = osSupport;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOsSupport() {
        return osSupport;
    }

    public void setOsSupport(String osSupport) {
        this.osSupport = osSupport;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
