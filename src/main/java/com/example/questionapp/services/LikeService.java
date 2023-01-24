package com.example.questionapp.services;

import com.example.questionapp.dataAccess.CommentRepository;
import com.example.questionapp.dataAccess.LikeRepository;
import com.example.questionapp.entities.Like;
import com.example.questionapp.entities.Post;
import com.example.questionapp.entities.User;
import com.example.questionapp.requests.CreateLikeRequest;
import com.example.questionapp.responses.LikeResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {
    private LikeRepository likeRepository;
    private UserService userService;
    private PostService postService;

    public LikeService(LikeRepository likeRepository, UserService userService, PostService postService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public List<LikeResponse> getAllLikes(Optional<Long> userId, Optional<Long> postId) {
        List<Like> list;
        if(userId.isPresent() && postId.isPresent()) {
            list = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
        }else if(userId.isPresent()) {
            list = likeRepository.findByUserId(userId.get());
        }else if(postId.isPresent()) {
            list = likeRepository.findByPostId(postId.get());
        }else
            list = likeRepository.findAll();
        return list.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());
    }

    public Like getLikeById(Long LikeId) {
        return likeRepository.findById(LikeId).orElse(null);
    }

    public Like createLike(CreateLikeRequest CreateLikeRequest) {
        User user = userService.getUserById(CreateLikeRequest.getUserId());
        Post post = postService.getPostById(CreateLikeRequest.getPostId());
        if(user != null && post != null) {
            Like like = new Like();
            like.setId(CreateLikeRequest.getId());
            like.setPost(post);
            like.setUser(user);
            return likeRepository.save(like);
        }else
            return null;
    }

    public void deleteLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }

}
