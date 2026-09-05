package com.ajalkarbill.website.service;

import com.ajalkarbill.website.entity.ContactEnquiry;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    private static final String ADMIN_EMAIL = "support@ajalkarinfotechindia.com";
    private static final String BRAND_COLOR = "#2563EB";

    @Async
    public void sendAdminNotification(ContactEnquiry enquiry) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(ADMIN_EMAIL);
            helper.setSubject("🚀 New Website Enquiry - AjalkarBill [" + enquiry.getEnquiryId() + "]");

            String htmlContent = buildAdminEmailTemplate(enquiry);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send admin email: " + e.getMessage());
        }
    }

    @Async
    public void sendCustomerConfirmation(ContactEnquiry enquiry) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(enquiry.getEmail());
            helper.setSubject("Thank You for Contacting AjalkarBill");

            String htmlContent = buildCustomerEmailTemplate(enquiry);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send customer email: " + e.getMessage());
        }
    }

    private String buildAdminEmailTemplate(ContactEnquiry e) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = e.getCreatedAt().format(formatter);

        return "<html><body style='font-family: Arial, sans-serif; color: #333; background-color: #f4f6f9; padding: 20px;'>"
                +
                "<div style='max-width: 600px; margin: 0 auto; background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1);'>"
                +
                "<div style='background: " + BRAND_COLOR + "; padding: 20px; text-align: center; color: white;'>" +
                "<h2>New Enquiry Received</h2>" +
                "</div>" +
                "<div style='padding: 20px;'>" +
                "<table style='width: 100%; border-collapse: collapse;'>" +
                "<tr><td style='padding: 10px; border-bottom: 1px solid #eee;'><strong>Enquiry ID:</strong></td><td style='padding: 10px; border-bottom: 1px solid #eee;'>"
                + e.getEnquiryId() + "</td></tr>" +
                "<tr><td style='padding: 10px; border-bottom: 1px solid #eee;'><strong>Customer Name:</strong></td><td style='padding: 10px; border-bottom: 1px solid #eee;'>"
                + e.getFullName() + "</td></tr>" +
                "<tr><td style='padding: 10px; border-bottom: 1px solid #eee;'><strong>Company Name:</strong></td><td style='padding: 10px; border-bottom: 1px solid #eee;'>"
                + (e.getCompanyName() != null ? e.getCompanyName() : "N/A") + "</td></tr>" +
                "<tr><td style='padding: 10px; border-bottom: 1px solid #eee;'><strong>Email:</strong></td><td style='padding: 10px; border-bottom: 1px solid #eee;'>"
                + e.getEmail() + "</td></tr>" +
                "<tr><td style='padding: 10px; border-bottom: 1px solid #eee;'><strong>Mobile:</strong></td><td style='padding: 10px; border-bottom: 1px solid #eee;'>"
                + e.getMobileNumber() + "</td></tr>" +
                "<tr><td style='padding: 10px; border-bottom: 1px solid #eee;'><strong>Type:</strong></td><td style='padding: 10px; border-bottom: 1px solid #eee;'>"
                + e.getEnquiryType() + "</td></tr>" +
                "<tr><td style='padding: 10px; border-bottom: 1px solid #eee;'><strong>Subject:</strong></td><td style='padding: 10px; border-bottom: 1px solid #eee;'>"
                + e.getSubject() + "</td></tr>" +
                "<tr><td style='padding: 10px; border-bottom: 1px solid #eee;'><strong>Date & Time:</strong></td><td style='padding: 10px; border-bottom: 1px solid #eee;'>"
                + formattedDate + "</td></tr>" +
                "<tr><td style='padding: 10px; border-bottom: 1px solid #eee;'><strong>Page Source:</strong></td><td style='padding: 10px; border-bottom: 1px solid #eee;'>"
                + e.getPageSource() + "</td></tr>" +
                "</table>" +
                "<div style='margin-top: 20px; padding: 15px; background: #f8fafc; border-left: 4px solid "
                + BRAND_COLOR + ";'>" +
                "<strong>Message:</strong><br/>" + e.getMessage().replace("\n", "<br/>") +
                "</div>" +
                "</div>" +
                "</div></body></html>";
    }

    private String buildCustomerEmailTemplate(ContactEnquiry e) {
        return "<html><body style='font-family: Arial, sans-serif; color: #333; background-color: #f4f6f9; padding: 20px;'>"
                +
                "<div style='max-width: 600px; margin: 0 auto; background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1);'>"
                +
                "<div style='background: " + BRAND_COLOR + "; padding: 20px; text-align: center; color: white;'>" +
                "<h2>Thank You for Contacting AjalkarBill</h2>" +
                "</div>" +
                "<div style='padding: 20px;'>" +
                "<p>Dear <strong>" + e.getFullName() + "</strong>,</p>" +
                "<p>Thank you for reaching out to us. We have successfully received your enquiry.</p>" +
                "<div style='margin: 20px 0; padding: 15px; background: #f8fafc; border-radius: 6px; text-align: center;'>"
                +
                "<strong>Reference Number:</strong> <span style='color: " + BRAND_COLOR + "; font-size: 1.2em;'>"
                + e.getEnquiryId() + "</span>" +
                "</div>" +
                "<p>Our team is currently reviewing your request. You can expect a response from our product experts <strong>within 24 hours</strong>.</p>"
                +
                "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'/>" +
                "<h4>Contact Details:</h4>" +
                "<p>Email: <a href='mailto:support@ajalkarinfotechindia.com'>support@ajalkarinfotechindia.com</a><br/>"
                +
                "Website: <a href='https://www.ajalkarbill.com'>www.ajalkarbill.com</a></p>" +
                "</div>" +
                "</div></body></html>";
    }

    @Async
    public void sendPasswordResetEmail(String email, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Password Reset Request - AjalkarBill");

            String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
            String htmlContent = buildPasswordResetEmailTemplate(resetUrl);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
        }
    }

    @Async
    public void sendOtpEmail(String email, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Password Reset OTP - Ajalkar Billing Website");

            String htmlContent = buildOtpEmailTemplate(otp);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
        }
    }

    private String buildPasswordResetEmailTemplate(String resetUrl) {
        return "<html><body style='font-family: Arial, sans-serif; color: #333; background-color: #f4f6f9; padding: 20px;'>"
                +
                "<div style='max-width: 600px; margin: 0 auto; background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1);'>"
                +
                "<div style='background: " + BRAND_COLOR + "; padding: 20px; text-align: center; color: white;'>" +
                "<h2>Password Reset Request</h2>" +
                "</div>" +
                "<div style='padding: 20px;'>" +
                "<p>You have requested to reset your password for your AjalkarBill account.</p>" +
                "<p>Click the button below to reset your password:</p>" +
                "<div style='margin: 20px 0; text-align: center;'>" +
                "<a href='" + resetUrl + "' style='background-color: " + BRAND_COLOR
                + "; color: white; padding: 12px 30px; text-decoration: none; border-radius: 6px; display: inline-block; font-weight: bold;'>Reset Password</a>"
                +
                "</div>" +
                "<p style='font-size: 12px; color: #666;'>Or copy and paste this link into your browser:</p>" +
                "<p style='font-size: 12px; color: #666; word-break: break-all;'>" + resetUrl + "</p>" +
                "<p style='margin-top: 20px; font-size: 12px; color: #999;'>This link will expire in 1 hour. If you did not request this password reset, please ignore this email.</p>"
                +
                "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'/>" +
                "<p style='font-size: 12px; color: #666;'>Need help? Contact us at <a href='mailto:support@ajalkarinfotechindia.com'>support@ajalkarinfotechindia.com</a></p>"
                +
                "</div>" +
                "</div></body></html>";
    }

    private String buildOtpEmailTemplate(String otp) {
        return "<html><body style='font-family: Arial, sans-serif; color: #333; background-color: #f4f6f9; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1);'>" +
                "<div style='background: " + BRAND_COLOR + "; padding: 20px; text-align: center; color: white;'>" +
                "<h2>Password Reset OTP</h2>" +
                "</div>" +
                "<div style='padding: 20px;'>" +
                "<p>Hello,</p>" +
                "<p>We received a request to reset your password.</p>" +
                "<p>Your OTP is:</p>" +
                "<div style='margin: 20px 0; padding: 20px; background: #f8fafc; border-radius: 6px; text-align: center;'>" +
                "<span style='color: " + BRAND_COLOR + "; font-size: 2em; font-weight: bold; letter-spacing: 5px;'>" + otp + "</span>" +
                "</div>" +
                "<p>This OTP is valid for 5 minutes.</p>" +
                "<p style='margin-top: 20px; font-size: 12px; color: #999;'>If you did not request a password reset, please ignore this email.</p>" +
                "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'/>" +
                "<p style='font-size: 12px; color: #666;'>Regards,<br/>Ajalkar Billing Website Team</p>" +
                "</div>" +
                "</div></body></html>";
    }
}
