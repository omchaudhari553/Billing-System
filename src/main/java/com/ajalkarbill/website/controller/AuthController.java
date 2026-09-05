package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.*;
import com.ajalkarbill.website.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Public Authentication", description = "APIs for public website authentication including login, register, forgot password, and reset password")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Validation error or duplicate email/mobile")
    })
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequestDto request) {
        Map<String, Object> response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Send password reset link to user email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "If email exists, reset link sent"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<Map<String, Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        Map<String, Object> response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset password using valid token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successful"),
            @ApiResponse(responseCode = "400", description = "Invalid token, expired token, or validation error")
    })
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
        Map<String, Object> response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}
