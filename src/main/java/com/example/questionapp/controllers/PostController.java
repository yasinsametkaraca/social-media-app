package com.example.questionapp.controllers;

import com.example.questionapp.entities.Post;
import com.example.questionapp.requests.CreatePostRequest;
import com.example.questionapp.services.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping  // posts?userId=userId  requestin içerisindeki parametrelere (header) bakar
    public List<Post> getAllPosts(@RequestParam Optional<Long> userId) {            //requestparam url içindeki parametre içinden userId ye parse et demek.  (posts?userId={userId}) //parametre yoksa eğer o zaman bütün postları getirir.
        return postService.getAllPosts(userId);
    }

    @PostMapping
    public Post createPost(@RequestBody CreatePostRequest newPostRequest) {
        return postService.createPost(newPostRequest);
    }

    @GetMapping("/{postId}")  // posts/postId  direk pathin kendisine bakar
    public Post getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }


}
