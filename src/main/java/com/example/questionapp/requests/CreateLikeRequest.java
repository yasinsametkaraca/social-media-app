package com.example.questionapp.requests;

import lombok.Data;

@Data
public class CreateLikeRequest {

    private Long id;
    private Long userId;
    private Long postId;

}