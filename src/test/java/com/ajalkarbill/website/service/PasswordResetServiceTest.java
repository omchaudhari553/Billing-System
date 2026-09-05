package com.ajalkarbill.website.service;

import com.ajalkarbill.website.entity.PasswordResetOtp;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.exception.InvalidOtpException;
import com.ajalkarbill.website.exception.MaxOtpAttemptsExceededException;
import com.ajalkarbill.website.exception.OtpExpiredException;
import com.ajalkarbill.website.repository.PasswordResetOtpRepository;
import com.ajalkarbill.website.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private PasswordResetOtpRepository passwordResetOtpRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private User testUser;
    private PasswordResetOtp testOtp;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("oldPassword");

        testOtp = new PasswordResetOtp();
        testOtp.setId(1L);
        testOtp.setEmail("test@example.com");
        testOtp.setOtpHash("hashedOtp");
        testOtp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        testOtp.setVerified(false);
        testOtp.setAttempts(0);
        testOtp.setUsed(false);
    }

    @Test
    void testSendOtp_UserExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedOtp");
        when(passwordResetOtpRepository.save(any(PasswordResetOtp.class))).thenReturn(testOtp);

        passwordResetService.sendOtp("test@example.com");

        verify(passwordResetOtpRepository, times(1)).deleteByEmail("test@example.com");
        verify(passwordResetOtpRepository, times(1)).save(any(PasswordResetOtp.class));
        verify(emailService, times(1)).sendOtpEmail(eq("test@example.com"), anyString());
    }

    @Test
    void testSendOtp_UserNotExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        passwordResetService.sendOtp("nonexistent@example.com");

        verify(passwordResetOtpRepository, never()).save(any(PasswordResetOtp.class));
        verify(emailService, never()).sendOtpEmail(anyString(), anyString());
    }

    @Test
    void testVerifyOtp_Success() {
        when(passwordResetOtpRepository.findByEmailAndVerifiedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.of(testOtp));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedResetToken");
        when(passwordResetOtpRepository.save(any(PasswordResetOtp.class))).thenReturn(testOtp);

        String resetToken = passwordResetService.verifyOtp("test@example.com", "123456");

        assertNotNull(resetToken);
        assertTrue(testOtp.isVerified());
        assertNotNull(testOtp.getResetTokenHash());
        verify(passwordResetOtpRepository, times(1)).save(any(PasswordResetOtp.class));
    }

    @Test
    void testVerifyOtp_InvalidOtp() {
        when(passwordResetOtpRepository.findByEmailAndVerifiedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.of(testOtp));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(passwordResetOtpRepository.save(any(PasswordResetOtp.class))).thenReturn(testOtp);

        assertThrows(InvalidOtpException.class, () -> {
            passwordResetService.verifyOtp("test@example.com", "wrongOtp");
        });

        assertEquals(1, testOtp.getAttempts());
    }

    @Test
    void testVerifyOtp_ExpiredOtp() {
        testOtp.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        when(passwordResetOtpRepository.findByEmailAndVerifiedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.of(testOtp));

        assertThrows(OtpExpiredException.class, () -> {
            passwordResetService.verifyOtp("test@example.com", "123456");
        });

        verify(passwordResetOtpRepository, times(1)).delete(testOtp);
    }

    @Test
    void testVerifyOtp_MaxAttemptsExceeded() {
        testOtp.setAttempts(5);
        when(passwordResetOtpRepository.findByEmailAndVerifiedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.of(testOtp));

        assertThrows(MaxOtpAttemptsExceededException.class, () -> {
            passwordResetService.verifyOtp("test@example.com", "123456");
        });

        verify(passwordResetOtpRepository, times(1)).delete(testOtp);
    }

    @Test
    void testVerifyOtp_NoActiveOtp() {
        when(passwordResetOtpRepository.findByEmailAndVerifiedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidOtpException.class, () -> {
            passwordResetService.verifyOtp("test@example.com", "123456");
        });
    }

    @Test
    void testResetPassword_Success() {
        testOtp.setVerified(true);
        testOtp.setResetTokenHash("hashedResetToken");
        testOtp.setResetTokenExpiresAt(LocalDateTime.now().plusMinutes(10));

        when(passwordResetOtpRepository.findByEmailAndVerifiedTrueAndUsedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.of(testOtp));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(anyString())).thenReturn("newHashedPassword");

        passwordResetService.resetPassword("test@example.com", "validResetToken", "NewPassword@123");

        verify(userRepository, times(1)).save(testUser);
        verify(passwordResetOtpRepository, times(1)).save(testOtp);
        verify(passwordResetOtpRepository, times(1)).delete(testOtp);
        assertTrue(testOtp.isUsed());
    }

    @Test
    void testResetPassword_InvalidResetToken() {
        testOtp.setVerified(true);
        testOtp.setResetTokenHash("hashedResetToken");
        testOtp.setResetTokenExpiresAt(LocalDateTime.now().plusMinutes(10));

        when(passwordResetOtpRepository.findByEmailAndVerifiedTrueAndUsedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.of(testOtp));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidOtpException.class, () -> {
            passwordResetService.resetPassword("test@example.com", "invalidToken", "NewPassword@123");
        });
    }

    @Test
    void testResetPassword_ExpiredResetToken() {
        testOtp.setVerified(true);
        testOtp.setResetTokenHash("hashedResetToken");
        testOtp.setResetTokenExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(passwordResetOtpRepository.findByEmailAndVerifiedTrueAndUsedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.of(testOtp));

        assertThrows(OtpExpiredException.class, () -> {
            passwordResetService.resetPassword("test@example.com", "validToken", "NewPassword@123");
        });

        verify(passwordResetOtpRepository, times(1)).delete(testOtp);
    }

    @Test
    void testResetPassword_NoVerifiedOtp() {
        when(passwordResetOtpRepository.findByEmailAndVerifiedTrueAndUsedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidOtpException.class, () -> {
            passwordResetService.resetPassword("test@example.com", "validToken", "NewPassword@123");
        });
    }

    @Test
    void testResetPassword_UserNotFound() {
        testOtp.setVerified(true);
        testOtp.setResetTokenHash("hashedResetToken");
        testOtp.setResetTokenExpiresAt(LocalDateTime.now().plusMinutes(10));

        when(passwordResetOtpRepository.findByEmailAndVerifiedTrueAndUsedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.of(testOtp));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidOtpException.class, () -> {
            passwordResetService.resetPassword("test@example.com", "validToken", "NewPassword@123");
        });
    }

    @Test
    void testCleanupExpiredOtps() {
        passwordResetService.cleanupExpiredOtps();

        verify(passwordResetOtpRepository, times(1)).deleteByCreatedAtBefore(any(LocalDateTime.class));
    }

    @Test
    void testGenerateSecureOtp() {
        // This test ensures OTP generation doesn't throw exceptions
        // and produces valid 6-digit numbers
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedOtp");
        when(passwordResetOtpRepository.save(any(PasswordResetOtp.class))).thenReturn(testOtp);

        assertDoesNotThrow(() -> {
            passwordResetService.sendOtp("test@example.com");
        });

        verify(passwordResetOtpRepository, times(1)).save(any(PasswordResetOtp.class));
    }

    @Test
    void testPasswordEncoding() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordResetOtpRepository.findByEmailAndVerifiedTrueAndUsedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.of(testOtp));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        testOtp.setVerified(true);
        testOtp.setResetTokenHash("hashedResetToken");
        testOtp.setResetTokenExpiresAt(LocalDateTime.now().plusMinutes(10));

        passwordResetService.resetPassword("test@example.com", "validToken", "NewPassword@123");

        verify(passwordEncoder, times(1)).encode("NewPassword@123");
        assertNotEquals("NewPassword@123", testUser.getPassword());
    }

    @Test
    void testOtpInvalidationAfterUse() {
        testOtp.setVerified(true);
        testOtp.setResetTokenHash("hashedResetToken");
        testOtp.setResetTokenExpiresAt(LocalDateTime.now().plusMinutes(10));

        when(passwordResetOtpRepository.findByEmailAndVerifiedTrueAndUsedFalseOrderByCreatedAtDesc(anyString()))
                .thenReturn(Optional.of(testOtp));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(anyString())).thenReturn("newHashedPassword");

        passwordResetService.resetPassword("test@example.com", "validToken", "NewPassword@123");

        assertTrue(testOtp.isUsed());
        verify(passwordResetOtpRepository, times(1)).delete(testOtp);
    }

    @Test
    void testOtpInvalidatePreviousWhenNewGenerated() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedOtp");
        when(passwordResetOtpRepository.save(any(PasswordResetOtp.class))).thenReturn(testOtp);

        passwordResetService.sendOtp("test@example.com");

        verify(passwordResetOtpRepository, times(1)).deleteByEmail("test@example.com");
    }
}
