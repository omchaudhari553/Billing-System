package com.ajalkarbill.website.security;

import com.ajalkarbill.website.entity.Admin;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.repository.AdminRepository;
import com.ajalkarbill.website.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class WebsiteUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public WebsiteUserDetailsService(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
            return new WebsiteUserDetails(user);
        } catch (UsernameNotFoundException e) {
            try {
                Admin admin = adminRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));
                return new AdminUserDetails(admin);
            } catch (UsernameNotFoundException ex) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
        }
    }
}
