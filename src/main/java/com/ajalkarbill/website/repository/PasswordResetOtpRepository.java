package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp> findByEmailAndVerifiedFalseOrderByCreatedAtDesc(String email);

    Optional<PasswordResetOtp> findByEmailAndVerifiedTrueAndUsedFalseOrderByCreatedAtDesc(String email);

    Optional<PasswordResetOtp> findByResetTokenHashAndUsedFalse(String resetTokenHash);

    List<PasswordResetOtp> findByEmail(String email);

    void deleteByEmail(String email);

    void deleteByResetTokenHash(String resetTokenHash);

    void deleteByCreatedAtBefore(LocalDateTime dateTime);
}
