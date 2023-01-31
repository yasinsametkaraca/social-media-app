package com.example.questionapp.requests;

import lombok.Data;

@Data
public class RefreshTokenRequest {

    private Long userId;
    private String refreshToken;
}
