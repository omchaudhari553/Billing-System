package com.ajalkarbill.website.config;

import com.ajalkarbill.website.entity.Admin;
import com.ajalkarbill.website.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataInitializer(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setEmail("admin@ajalkarbill.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setName("Super Admin");
            admin.setRole("ADMIN");
            
            adminRepository.save(admin);
            System.out.println("Default admin account created successfully!");
            System.out.println("Email: admin@ajalkarbill.com");
            System.out.println("Password: Admin@123");
            System.out.println("Please change the password after first login.");
        }
    }
}
