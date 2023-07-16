package com.motus.fileoperations.repository;

import com.motus.fileoperations.model.RefreshToken;
import com.motus.fileoperations.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(UserEntity user);

    RefreshToken findByUser(UserEntity user);
}
