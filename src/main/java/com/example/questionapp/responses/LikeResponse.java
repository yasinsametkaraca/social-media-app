package com.example.questionapp.responses;

import com.example.questionapp.entities.Like;
import lombok.Data;

@Data
public class LikeResponse {

    private Long id;
    private Long userId;
    private Long postId;

    public LikeResponse(Like like) {
        this.id = like.getId();
        this.userId = like.getUser().getId();
        this.postId = like.getPost().getId();
    }
}
