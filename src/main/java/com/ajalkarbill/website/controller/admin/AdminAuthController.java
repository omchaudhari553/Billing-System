package com.ajalkarbill.website.controller.admin;

import com.ajalkarbill.website.dto.AdminDto;
import com.ajalkarbill.website.dto.AdminLoginRequest;
import com.ajalkarbill.website.dto.AdminLoginResponse;
import com.ajalkarbill.website.entity.Admin;
import com.ajalkarbill.website.repository.AdminRepository;
import com.ajalkarbill.website.security.AdminUserDetails;
import com.ajalkarbill.website.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@Tag(name = "Admin Authentication", description = "APIs for admin authentication including login, logout, and getting current admin details")
public class AdminAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AdminRepository adminRepository;

    public AdminAuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            AdminRepository adminRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.adminRepository = adminRepository;
    }

    @PostMapping("/login")
    @Operation(summary = "Admin login", description = "Authenticate admin user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<AdminLoginResponse> login(@Valid @RequestBody AdminLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication);

        AdminUserDetails userDetails = (AdminUserDetails) authentication.getPrincipal();
        Admin admin = userDetails.getAdmin();
        AdminDto adminDto = new AdminDto(admin.getId(), admin.getEmail(), admin.getName(), admin.getRole());

        return ResponseEntity.ok(new AdminLoginResponse(true, "Admin login successful", token, adminDto));
    }

    @PostMapping("/logout")
    @Operation(summary = "Admin logout", description = "Logout admin user and clear security context")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new AdminLoginResponse(true, "Admin logged out successfully", null, null));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current admin", description = "Get details of the currently authenticated admin user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin details retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - no valid token")
    })
    public ResponseEntity<AdminDto> getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AdminUserDetails) {
            AdminUserDetails userDetails = (AdminUserDetails) authentication.getPrincipal();
            Admin admin = userDetails.getAdmin();
            AdminDto adminDto = new AdminDto(admin.getId(), admin.getEmail(), admin.getName(), admin.getRole());
            return ResponseEntity.ok(adminDto);
        }
        return ResponseEntity.status(401).build();
    }
}
