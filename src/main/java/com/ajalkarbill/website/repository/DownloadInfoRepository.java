package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.DownloadInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadInfoRepository extends JpaRepository<DownloadInfo, Long> {
}
