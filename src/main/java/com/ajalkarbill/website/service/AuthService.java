package com.ajalkarbill.website.service;

import com.ajalkarbill.website.config.AdminProperties;
import com.ajalkarbill.website.dto.ForgotPasswordRequestDto;
import com.ajalkarbill.website.dto.LoginRequestDto;
import com.ajalkarbill.website.dto.LoginResponseDto;
import com.ajalkarbill.website.dto.RegisterRequestDto;
import com.ajalkarbill.website.dto.ResetPasswordRequestDto;
import com.ajalkarbill.website.dto.VerifyOtpRequestDto;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.exception.DuplicateEmailException;
import com.ajalkarbill.website.exception.DuplicateMobileException;
import com.ajalkarbill.website.repository.UserRepository;
import com.ajalkarbill.website.security.ConfiguredAdminUserDetails;
import com.ajalkarbill.website.security.JwtUtil;
import com.ajalkarbill.website.service.WebsiteLeadService;
import com.ajalkarbill.website.service.WebsiteVisitorService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

        private final AuthenticationManager authenticationManager;
        private final JwtUtil jwtUtil;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final PasswordResetService passwordResetService;
        private final WebsiteVisitorService visitorService;
        private final WebsiteLeadService leadService;
        private final AdminProperties adminProperties;

        public AuthService(
                        AuthenticationManager authenticationManager,
                        JwtUtil jwtUtil,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        PasswordResetService passwordResetService,
                        WebsiteVisitorService visitorService,
                        WebsiteLeadService leadService,
                        AdminProperties adminProperties) {
                this.authenticationManager = authenticationManager;
                this.jwtUtil = jwtUtil;
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.passwordResetService = passwordResetService;
                this.visitorService = visitorService;
                this.leadService = leadService;
                this.adminProperties = adminProperties;
        }

        /**
         * UNIFIED LOGIN - Checks admin credentials first, then database users
         */
        public LoginResponseDto login(LoginRequestDto request) {
                String email = request.getEmail();
                String password = request.getPassword();

                // First check if credentials match configured admin
                if (email.equals(adminProperties.getUsername()) && 
                    password.equals(adminProperties.getPassword())) {
                        
                        // Authenticate as configured admin
                        ConfiguredAdminUserDetails adminDetails = new ConfiguredAdminUserDetails(
                                adminProperties.getUsername(),
                                adminProperties.getPassword(),
                                adminProperties.getRole()
                        );
                        
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                adminDetails,
                                null,
                                adminDetails.getAuthorities()
                        );
                        
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String token = jwtUtil.generateToken(authentication);

                        return new LoginResponseDto(
                                true,
                                "Admin login successful",
                                token,
                                "Bearer",
                                adminProperties.getRole(),
                                adminProperties.getUsername()
                        );
                }

                // If not admin, authenticate against database
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                email,
                                                password));

                String token = jwtUtil.generateToken(authentication);
                
                // Extract role from authentication
                String role = authentication.getAuthorities().stream()
                                .findFirst()
                                .map(authority -> authority.getAuthority())
                                .orElse("ROLE_USER")
                                .replace("ROLE_", "");

                return new LoginResponseDto(
                                true,
                                "Login successful",
                                token,
                                "Bearer",
                                role,
                                email);
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
                                        "You must agree to the terms and conditions");
                        return response;
                }

                // Check duplicate email
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new DuplicateEmailException(
                                        "Email already registered");
                }

                // Check duplicate mobile number
                if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
                        throw new DuplicateMobileException(
                                        "Mobile number already registered");
                }

                // Create new user
                User user = new User();

                user.setFullName(request.getFullName());
                user.setBusinessName(request.getBusinessName());
                user.setMobileNumber(request.getMobileNumber());
                user.setEmail(request.getEmail());

                // Encode password before saving
                user.setPassword(
                                passwordEncoder.encode(request.getPassword()));

                // Save user
                userRepository.save(user);

                // Associate visitor with user if visitorId is provided
                if (request.getVisitorId() != null && !request.getVisitorId().isEmpty()) {
                        visitorService.associateVisitorWithUser(request.getVisitorId(), user.getId());

                        // Find and associate existing lead with the new user
                        leadService.findAndAssociateLeadByVisitorAndPhone(
                                        request.getVisitorId(),
                                        request.getMobileNumber(),
                                        user.getId());
                }

                // Success response
                response.put("success", true);
                response.put("message", "Account created successfully");

                return response;
        }

        /**
         * FORGOT PASSWORD - Send OTP
         */
        @Transactional
        public Map<String, Object> forgotPassword(
                        ForgotPasswordRequestDto request) {

                // Use PasswordResetService to send OTP
                passwordResetService.sendOtp(request.getEmail());

                /*
                 * Do not reveal whether the email exists.
                 * This prevents user/account enumeration.
                 */
                Map<String, Object> response = new HashMap<>();

                response.put("success", true);
                response.put(
                                "message",
                                "If an account exists with this email, an OTP has been sent.");

                return response;
        }

        /**
         * VERIFY OTP
         */
        @Transactional
        public Map<String, Object> verifyOtp(
                        VerifyOtpRequestDto request) {

                String resetToken = passwordResetService.verifyOtp(
                                request.getEmail(),
                                request.getOtp());

                Map<String, Object> response = new HashMap<>();

                response.put("success", true);
                response.put("message", "OTP verified successfully");
                response.put("resetToken", resetToken);

                return response;
        }

        /**
         * RESET PASSWORD
         */
        @Transactional
        public Map<String, Object> resetPassword(
                        ResetPasswordRequestDto request) {

                Map<String, Object> response = new HashMap<>();

                // Check new password and confirm password
                if (!request.getNewPassword()
                                .equals(request.getConfirmPassword())) {

                        response.put("success", false);
                        response.put("message", "Passwords do not match");

                        return response;
                }

                // Use PasswordResetService to reset password
                passwordResetService.resetPassword(
                                request.getEmail(),
                                request.getResetToken(),
                                request.getNewPassword());

                // Success response
                response.put("success", true);
                response.put(
                                "message",
                                "Password reset successfully");

                return response;
        }
}