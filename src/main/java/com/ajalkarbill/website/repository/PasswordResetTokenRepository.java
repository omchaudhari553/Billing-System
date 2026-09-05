package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.PasswordResetToken;
import com.ajalkarbill.website.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(User user);

    void deleteByUser(User user);

    void deleteByToken(String token);
}
