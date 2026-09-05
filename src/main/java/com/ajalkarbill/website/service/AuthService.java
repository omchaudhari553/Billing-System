package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.ForgotPasswordRequestDto;
import com.ajalkarbill.website.dto.LoginRequestDto;
import com.ajalkarbill.website.dto.LoginResponseDto;
import com.ajalkarbill.website.dto.RegisterRequestDto;
import com.ajalkarbill.website.dto.ResetPasswordRequestDto;
import com.ajalkarbill.website.entity.PasswordResetToken;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.exception.DuplicateEmailException;
import com.ajalkarbill.website.exception.DuplicateMobileException;
import com.ajalkarbill.website.exception.InvalidTokenException;
import com.ajalkarbill.website.exception.TokenExpiredException;
import com.ajalkarbill.website.repository.PasswordResetTokenRepository;
import com.ajalkarbill.website.repository.UserRepository;
import com.ajalkarbill.website.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    private static final long TOKEN_EXPIRATION_HOURS = 1;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            PasswordResetTokenRepository passwordResetTokenRepository,
            EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    /**
     * USER LOGIN
     */
    public LoginResponseDto login(LoginRequestDto request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        String token = jwtUtil.generateToken(authentication);

        return new LoginResponseDto(
                true,
                "Login successful",
                token,
                "Bearer"
        );
    }

    /**
     * USER REGISTRATION
     */
    @Transactional
    public Map<String, Object> register(RegisterRequestDto request) {

        Map<String, Object> response = new HashMap<>();

        // Check password and confirm password
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            response.put("success", false);
            response.put("message", "Passwords do not match");
            return response;
        }

        // Check terms and conditions
        if (request.getTerms() == null || !request.getTerms()) {
            response.put("success", false);
            response.put(
                    "message",
                    "You must agree to the terms and conditions"
            );
            return response;
        }

        // Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(
                    "Email already registered"
            );
        }

        // Check duplicate mobile number
        if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new DuplicateMobileException(
                    "Mobile number already registered"
            );
        }

        // Create new user
        User user = new User();

        user.setFullName(request.getFullName());
        user.setBusinessName(request.getBusinessName());
        user.setMobileNumber(request.getMobileNumber());
        user.setEmail(request.getEmail());

        // Encode password before saving
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Save user
        userRepository.save(user);

        // Success response
        response.put("success", true);
        response.put("message", "Account created successfully");

        return response;
    }

    /**
     * FORGOT PASSWORD
     */
    @Transactional
    public Map<String, Object> forgotPassword(
            ForgotPasswordRequestDto request
    ) {

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {

            // Generate secure reset token
            String token = generateSecureToken();

            // Token expires after 1 hour
            LocalDateTime expiryDate =
                    LocalDateTime.now()
                            .plusHours(TOKEN_EXPIRATION_HOURS);

            // Delete previous reset token
            passwordResetTokenRepository.deleteByUser(user);

            // Create new reset token
            PasswordResetToken resetToken =
                    new PasswordResetToken(
                            token,
                            user,
                            expiryDate
                    );

            // Save token
            passwordResetTokenRepository.save(resetToken);

            // Send email
            emailService.sendPasswordResetEmail(
                    user.getEmail(),
                    token
            );
        });

        /*
         * Do not reveal whether the email exists.
         * This prevents user/account enumeration.
         */
        Map<String, Object> response = new HashMap<>();

        response.put("success", true);
        response.put(
                "message",
                "If the email exists, a password reset link has been sent."
        );

        return response;
    }

    /**
     * RESET PASSWORD
     */
    @Transactional
    public Map<String, Object> resetPassword(
            ResetPasswordRequestDto request
    ) {

        Map<String, Object> response = new HashMap<>();

        // Check new password and confirm password
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            response.put("success", false);
            response.put("message", "Passwords do not match");

            return response;
        }

        // Find reset token
        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByToken(request.getToken())
                        .orElseThrow(
                                () -> new InvalidTokenException(
                                        "Invalid reset token"
                                )
                        );

        // Check token expiry
        if (resetToken.isExpired()) {

            passwordResetTokenRepository.delete(resetToken);

            throw new TokenExpiredException(
                    "Reset token has expired"
            );
        }

        // Get user
        User user = resetToken.getUser();

        // Encode and update password
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        // Save updated user
        userRepository.save(user);

        // Delete used reset token
        passwordResetTokenRepository.delete(resetToken);

        // Success response
        response.put("success", true);
        response.put(
                "message",
                "Password reset successfully"
        );

        return response;
    }

    /**
     * GENERATE SECURE PASSWORD RESET TOKEN
     */
    private String generateSecureToken() {

        SecureRandom secureRandom = new SecureRandom();

        byte[] tokenBytes = new byte[32];

        secureRandom.nextBytes(tokenBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(tokenBytes);
    }
}