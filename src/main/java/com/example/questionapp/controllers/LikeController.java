package com.example.questionapp.controllers;


import com.example.questionapp.entities.Like;
import com.example.questionapp.requests.CreateLikeRequest;
import com.example.questionapp.responses.LikeResponse;
import com.example.questionapp.services.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    public List<LikeResponse> getAllLikes(@RequestParam Optional<Long> userId,@RequestParam Optional<Long> postId) {
        return likeService.getAllLikes(userId, postId);
    }

    @PostMapping
    public Like createLike(@RequestBody CreateLikeRequest createLikeRequest) {
        return likeService.createLike(createLikeRequest);
    }

    @GetMapping("/{likeId}")
    public Like getLikeById(@PathVariable Long likeId) {
        return likeService.getLikeById(likeId);
    }

    @DeleteMapping("/{likeId}")
    public void deleteLikeById(@PathVariable Long likeId) {
        likeService.deleteLikeById(likeId);
    }
}
