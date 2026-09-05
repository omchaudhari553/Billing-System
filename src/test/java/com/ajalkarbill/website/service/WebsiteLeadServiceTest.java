package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.PhoneCaptureDto;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.entity.WebsiteLead;
import com.ajalkarbill.website.exception.DuplicateMobileException;
import com.ajalkarbill.website.repository.UserRepository;
import com.ajalkarbill.website.repository.WebsiteLeadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebsiteLeadServiceTest {

    @Mock
    private WebsiteLeadRepository leadRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private WebsiteLeadService leadService;

    private PhoneCaptureDto phoneCaptureDto;
    private WebsiteLead websiteLead;
    private User user;

    @BeforeEach
    void setUp() {
        phoneCaptureDto = new PhoneCaptureDto();
        phoneCaptureDto.setVisitorId("VISITOR-10001");
        phoneCaptureDto.setPhoneNumber("9876543210");
        phoneCaptureDto.setFullName("Rahul Sharma");
        phoneCaptureDto.setEmail("rahul@gmail.com");
        phoneCaptureDto.setBusinessName("Sharma Enterprises");

        websiteLead = new WebsiteLead();
        websiteLead.setId(1L);
        websiteLead.setVisitorId("VISITOR-10001");
        websiteLead.setPhoneNumber("9876543210");
        websiteLead.setFullName("Rahul Sharma");
        websiteLead.setEmail("rahul@gmail.com");
        websiteLead.setBusinessName("Sharma Enterprises");
        websiteLead.setSource(WebsiteLead.LeadSource.REGISTRATION_FORM);
        websiteLead.setRegistered(false);

        user = new User();
        user.setId(1L);
        user.setFullName("Rahul Sharma");
        user.setBusinessName("Sharma Enterprises");
        user.setMobileNumber("9876543210");
        user.setEmail("rahul@gmail.com");
    }

    @Test
    void testCapturePhone_Success() {
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(false);
        when(leadRepository.existsByVisitorIdAndPhoneNumber(anyString(), anyString())).thenReturn(false);
        when(leadRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(leadRepository.save(any(WebsiteLead.class))).thenReturn(websiteLead);

        var response = leadService.capturePhone(phoneCaptureDto);

        assertTrue((Boolean) response.get("success"));
        assertEquals("Phone number captured successfully", response.get("message"));
        assertEquals(1L, response.get("leadId"));
        assertEquals(false, response.get("registered"));

        verify(leadRepository, times(1)).save(any(WebsiteLead.class));
    }

    @Test
    void testCapturePhone_DuplicateUser() {
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(true);

        assertThrows(DuplicateMobileException.class, () -> {
            leadService.capturePhone(phoneCaptureDto);
        });

        verify(leadRepository, never()).save(any(WebsiteLead.class));
    }

    @Test
    void testCapturePhone_SameVisitorSamePhone_ReturnsExisting() {
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(false);
        when(leadRepository.existsByVisitorIdAndPhoneNumber(anyString(), anyString())).thenReturn(true);
        when(leadRepository.findByVisitorIdAndPhoneNumber(anyString(), anyString())).thenReturn(Optional.of(websiteLead));

        var response = leadService.capturePhone(phoneCaptureDto);

        assertTrue((Boolean) response.get("success"));
        assertEquals("Phone number already captured for this visitor", response.get("message"));
        assertEquals(1L, response.get("leadId"));
        assertEquals(false, response.get("registered"));

        verify(leadRepository, never()).save(any(WebsiteLead.class));
    }

    @Test
    void testCapturePhone_DifferentVisitorSamePhone_ThrowsException() {
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(false);
        when(leadRepository.existsByVisitorIdAndPhoneNumber(anyString(), anyString())).thenReturn(false);
        when(leadRepository.existsByPhoneNumber(anyString())).thenReturn(true);

        assertThrows(DuplicateMobileException.class, () -> {
            leadService.capturePhone(phoneCaptureDto);
        });

        verify(leadRepository, never()).save(any(WebsiteLead.class));
    }

    @Test
    void testAssociateLeadWithUser_Success() {
        when(leadRepository.findById(1L)).thenReturn(Optional.of(websiteLead));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(leadRepository.save(any(WebsiteLead.class))).thenReturn(websiteLead);

        leadService.associateLeadWithUser(1L, 1L);

        assertEquals(user, websiteLead.getUser());
        assertTrue(websiteLead.getRegistered());

        verify(leadRepository, times(1)).save(any(WebsiteLead.class));
    }

    @Test
    void testAssociateLeadWithUser_LeadNotFound() {
        when(leadRepository.findById(1L)).thenReturn(Optional.empty());

        leadService.associateLeadWithUser(1L, 1L);

        verify(leadRepository, never()).save(any(WebsiteLead.class));
    }

    @Test
    void testAssociateLeadWithUser_UserNotFound() {
        when(leadRepository.findById(1L)).thenReturn(Optional.of(websiteLead));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        leadService.associateLeadWithUser(1L, 1L);

        verify(leadRepository, never()).save(any(WebsiteLead.class));
    }

    @Test
    void testFindAndAssociateLeadByVisitorAndPhone_ExactMatch() {
        when(leadRepository.findByVisitorIdAndPhoneNumber(anyString(), anyString())).thenReturn(Optional.of(websiteLead));
        when(leadRepository.findById(1L)).thenReturn(Optional.of(websiteLead));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(leadRepository.save(any(WebsiteLead.class))).thenReturn(websiteLead);

        leadService.findAndAssociateLeadByVisitorAndPhone("VISITOR-10001", "9876543210", 1L);

        verify(leadService, times(1)).associateLeadWithUser(1L, 1L);
    }

    @Test
    void testFindAndAssociateLeadByVisitorAndPhone_VisitorOnlyMatch() {
        when(leadRepository.findByVisitorIdAndPhoneNumber(anyString(), anyString())).thenReturn(Optional.empty());
        when(leadRepository.findByVisitorId(anyString())).thenReturn(Optional.of(websiteLead));
        when(leadRepository.findById(1L)).thenReturn(Optional.of(websiteLead));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(leadRepository.save(any(WebsiteLead.class))).thenReturn(websiteLead);

        leadService.findAndAssociateLeadByVisitorAndPhone("VISITOR-10001", "9876543210", 1L);

        verify(leadService, times(1)).associateLeadWithUser(1L, 1L);
    }

    @Test
    void testFindAndAssociateLeadByVisitorAndPhone_AlreadyRegistered() {
        websiteLead.setRegistered(true);
        when(leadRepository.findByVisitorIdAndPhoneNumber(anyString(), anyString())).thenReturn(Optional.empty());
        when(leadRepository.findByVisitorId(anyString())).thenReturn(Optional.of(websiteLead));

        leadService.findAndAssociateLeadByVisitorAndPhone("VISITOR-10001", "9876543210", 1L);

        verify(leadService, never()).associateLeadWithUser(anyLong(), anyLong());
    }

    @Test
    void testFindByVisitorIdAndPhoneNumber() {
        when(leadRepository.findByVisitorIdAndPhoneNumber(anyString(), anyString())).thenReturn(Optional.of(websiteLead));

        Optional<WebsiteLead> result = leadService.findByVisitorIdAndPhoneNumber("VISITOR-10001", "9876543210");

        assertTrue(result.isPresent());
        assertEquals("VISITOR-10001", result.get().getVisitorId());
        assertEquals("9876543210", result.get().getPhoneNumber());
    }

    @Test
    void testFindByVisitorId() {
        when(leadRepository.findByVisitorId(anyString())).thenReturn(Optional.of(websiteLead));

        Optional<WebsiteLead> result = leadService.findByVisitorId("VISITOR-10001");

        assertTrue(result.isPresent());
        assertEquals("VISITOR-10001", result.get().getVisitorId());
    }
}
