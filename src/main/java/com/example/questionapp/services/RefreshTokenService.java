package com.example.questionapp.services;

import com.example.questionapp.dataAccess.RefreshTokenRepository;
import com.example.questionapp.entities.RefreshToken;
import com.example.questionapp.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${refresh.token.expires.in}")
    Long expireSeconds;

    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createRefreshToken(User user) {
        RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
        if(token == null) {
            token =	new RefreshToken();
            token.setUser(user);
        }
        token.setToken(UUID.randomUUID().toString());  //UUID ile random unique idler üretebiliriz.
        token.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));  //şuan ki zamana 3 gün ekleyerek set edicez.
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public RefreshToken getByUser(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

    public boolean isRefreshExpired(RefreshToken token) {  //refresh tokenin expire olup olmadığını kontrol eder.
        return token.getExpiryDate().before(new Date());
    }

}