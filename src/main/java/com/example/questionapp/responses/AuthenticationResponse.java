package com.example.questionapp.responses;

import lombok.Data;

@Data
public class AuthenticationResponse {

    String message;
    Long userId;
    String refreshToken;
    String accessToken;


}

