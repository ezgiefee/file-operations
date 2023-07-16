package com.motus.fileoperations.security.service;

import com.motus.fileoperations.exception.TokenRefreshException;
import com.motus.fileoperations.model.RefreshToken;
import com.motus.fileoperations.repository.RefreshTokenRepository;
import com.motus.fileoperations.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.motus.fileoperations.security.jwt.SecurityConstants.JWT_EXPIRATION;

@Service
public class RefreshTokenService {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(String userName) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findByUsername(userName).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(JWT_EXPIRATION));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }


    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

}
