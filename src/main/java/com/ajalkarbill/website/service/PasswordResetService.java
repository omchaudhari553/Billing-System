package com.ajalkarbill.website.service;

import com.ajalkarbill.website.entity.PasswordResetOtp;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.exception.InvalidOtpException;
import com.ajalkarbill.website.exception.MaxOtpAttemptsExceededException;
import com.ajalkarbill.website.exception.OtpExpiredException;
import com.ajalkarbill.website.repository.PasswordResetOtpRepository;
import com.ajalkarbill.website.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final int OTP_EXPIRY_MINUTES = 15;
    private static final int RESET_TOKEN_EXPIRY_MINUTES = 60;
    private static final int MAX_OTP_ATTEMPTS = 5;

    // Development mode flag - set to true to log OTP to console instead of sending
    // email
    private static final boolean DEVELOPMENT_MODE = false;

    public PasswordResetService(
            PasswordResetOtpRepository passwordResetOtpRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.passwordResetOtpRepository = passwordResetOtpRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Generate and send OTP for password reset
     */
    @Transactional
    public void sendOtp(String email) {
        // Check if user exists
        userRepository.findByEmail(email).ifPresent(user -> {
            // Invalidate previous OTPs for this email
            passwordResetOtpRepository.deleteByEmail(email);

            // Generate secure 6-digit OTP
            String otp = generateSecureOtp();
            String otpHash = passwordEncoder.encode(otp);

            // Create OTP record
            PasswordResetOtp passwordResetOtp = new PasswordResetOtp(
                    email,
                    otpHash,
                    LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));

            passwordResetOtpRepository.save(passwordResetOtp);

            // In development mode, log OTP to console instead of sending email
            if (DEVELOPMENT_MODE) {
                logger.info("========================================");
                logger.info("DEVELOPMENT MODE - OTP LOGGED TO CONSOLE");
                logger.info("Email: {}", email);
                logger.info("OTP: {}", otp);
                logger.info("Expires in: {} minutes", OTP_EXPIRY_MINUTES);
                logger.info("========================================");
            } else {
                // Send OTP email
                emailService.sendOtpEmail(email, otp);
            }
        });
    }

    /**
     * Verify OTP and generate reset token
     */
    @Transactional
    public String verifyOtp(String email, String otp) {
        // Find active OTP for this email
        PasswordResetOtp passwordResetOtp = passwordResetOtpRepository
                .findByEmailAndVerifiedFalseOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new InvalidOtpException("Invalid or expired OTP"));

        // Check if OTP is expired
        if (passwordResetOtp.isExpired()) {
            passwordResetOtpRepository.delete(passwordResetOtp);
            throw new OtpExpiredException("OTP has expired");
        }

        // Check if max attempts exceeded
        if (passwordResetOtp.hasMaxAttemptsExceeded()) {
            passwordResetOtpRepository.delete(passwordResetOtp);
            throw new MaxOtpAttemptsExceededException("Maximum OTP attempts exceeded. Please request a new OTP.");
        }

        // Verify OTP
        if (!passwordEncoder.matches(otp, passwordResetOtp.getOtpHash())) {
            passwordResetOtp.incrementAttempts();
            passwordResetOtpRepository.save(passwordResetOtp);
            throw new InvalidOtpException("Invalid OTP");
        }

        // Mark OTP as verified
        passwordResetOtp.setVerified(true);
        passwordResetOtpRepository.save(passwordResetOtp);

        // Generate reset token
        String resetToken = generateSecureResetToken();
        String resetTokenHash = passwordEncoder.encode(resetToken);

        // Store reset token hash
        passwordResetOtp.setResetTokenHash(resetTokenHash);
        passwordResetOtp.setResetTokenExpiresAt(LocalDateTime.now().plusMinutes(RESET_TOKEN_EXPIRY_MINUTES));
        passwordResetOtpRepository.save(passwordResetOtp);

        return resetToken;
    }

    /**
     * Reset password using reset token
     */
    @Transactional
    public void resetPassword(String email, String resetToken, String newPassword) {
        // Find verified OTP with reset token
        PasswordResetOtp passwordResetOtp = passwordResetOtpRepository
                .findByEmailAndVerifiedTrueAndUsedFalseOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new InvalidOtpException("Invalid reset token"));

        // Check if reset token is expired
        if (passwordResetOtp.isResetTokenExpired()) {
            passwordResetOtpRepository.delete(passwordResetOtp);
            throw new OtpExpiredException("Reset token has expired");
        }

        // Verify reset token
        if (!passwordEncoder.matches(resetToken, passwordResetOtp.getResetTokenHash())) {
            throw new InvalidOtpException("Invalid reset token");
        }

        // Get user and update password
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidOtpException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark OTP as used
        passwordResetOtp.setUsed(true);
        passwordResetOtpRepository.save(passwordResetOtp);

        // Clean up used OTPs
        passwordResetOtpRepository.delete(passwordResetOtp);
    }

    /**
     * Generate secure 6-digit OTP
     */
    private String generateSecureOtp() {
        SecureRandom secureRandom = new SecureRandom();
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Generate secure reset token
     */
    private String generateSecureResetToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    /**
     * Clean up expired OTPs (can be scheduled)
     */
    @Transactional
    public void cleanupExpiredOtps() {
        passwordResetOtpRepository.deleteByCreatedAtBefore(LocalDateTime.now().minusDays(1));
    }
}
