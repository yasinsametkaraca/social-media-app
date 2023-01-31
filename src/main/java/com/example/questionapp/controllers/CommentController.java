package com.example.questionapp.controllers;


import com.example.questionapp.entities.Comment;
import com.example.questionapp.requests.CreateCommentRequest;
import com.example.questionapp.requests.UpdateCommentRequest;
import com.example.questionapp.responses.CommentResponse;
import com.example.questionapp.services.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentResponse> getAllComments(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> postId) {
        return commentService.getAllComments(userId, postId);
    }

    @PostMapping
    public Comment createComment(@RequestBody CreateCommentRequest createCommentRequest ){
        return commentService.createComment(createCommentRequest);
    }

    @GetMapping("/{commentId}")
    public Comment getCommentById(@PathVariable Long commentId){
        return commentService.getCommentById(commentId);
    }

    @PutMapping("/{commentId}")
    public Comment updateCommentById(@PathVariable Long commentId, @RequestBody UpdateCommentRequest updateCommentRequest){
        return commentService.updateCommentById(commentId,updateCommentRequest);
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentById(@PathVariable Long commentId){
        commentService.deleteCommentById(commentId);
    }





}
