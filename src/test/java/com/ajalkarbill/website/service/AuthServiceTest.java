package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.RegisterRequestDto;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.exception.DuplicateEmailException;
import com.ajalkarbill.website.exception.DuplicateMobileException;
import com.ajalkarbill.website.repository.PasswordResetTokenRepository;
import com.ajalkarbill.website.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private WebsiteVisitorService visitorService;

    @Mock
    private WebsiteLeadService leadService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequestDto registerRequestDto;

    @BeforeEach
    void setUp() {
        registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setFullName("Rahul Sharma");
        registerRequestDto.setBusinessName("Sharma Enterprises");
        registerRequestDto.setMobileNumber("9876543210");
        registerRequestDto.setEmail("rahul@gmail.com");
        registerRequestDto.setPassword("Rahul@12345");
        registerRequestDto.setConfirmPassword("Rahul@12345");
        registerRequestDto.setTerms(true);
        registerRequestDto.setVisitorId("VISITOR-10001");
    }

    @Test
    void testRegister_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        Map<String, Object> response = authService.register(registerRequestDto);

        assertTrue((Boolean) response.get("success"));
        assertEquals("Account created successfully", response.get("message"));

        verify(visitorService, times(1)).associateVisitorWithUser("VISITOR-10001", 1L);
        verify(leadService, times(1)).findAndAssociateLeadByVisitorAndPhone("VISITOR-10001", "9876543210", 1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_PasswordMismatch() {
        registerRequestDto.setConfirmPassword("DifferentPassword");

        Map<String, Object> response = authService.register(registerRequestDto);

        assertFalse((Boolean) response.get("success"));
        assertEquals("Passwords do not match", response.get("message"));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_TermsNotAccepted() {
        registerRequestDto.setTerms(false);

        Map<String, Object> response = authService.register(registerRequestDto);

        assertFalse((Boolean) response.get("success"));
        assertEquals("You must agree to the terms and conditions", response.get("message"));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_DuplicateEmail() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> {
            authService.register(registerRequestDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_DuplicateMobile() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(true);

        assertThrows(DuplicateMobileException.class, () -> {
            authService.register(registerRequestDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_WithoutVisitorId() {
        registerRequestDto.setVisitorId(null);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        Map<String, Object> response = authService.register(registerRequestDto);

        assertTrue((Boolean) response.get("success"));
        assertEquals("Account created successfully", response.get("message"));

        verify(visitorService, never()).associateVisitorWithUser(anyString(), anyLong());
        verify(leadService, never()).findAndAssociateLeadByVisitorAndPhone(anyString(), anyString(), anyLong());
    }

    @Test
    void testRegister_WithEmptyVisitorId() {
        registerRequestDto.setVisitorId("");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        Map<String, Object> response = authService.register(registerRequestDto);

        assertTrue((Boolean) response.get("success"));
        assertEquals("Account created successfully", response.get("message"));

        verify(visitorService, never()).associateVisitorWithUser(anyString(), anyLong());
        verify(leadService, never()).findAndAssociateLeadByVisitorAndPhone(anyString(), anyString(), anyLong());
    }
}
