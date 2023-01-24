package com.example.questionapp.services;

import com.example.questionapp.dataAccess.PostRepository;
import com.example.questionapp.entities.Post;
import com.example.questionapp.entities.User;
import com.example.questionapp.requests.CreatePostRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;
    private UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<Post> getAllPosts(Optional<Long> userId) {
        if (userId.isPresent()) {   //isPresent in mantığı eğer userId parametresi geldiyse
            return postRepository.findByUserId(userId.get());
        }
        return postRepository.findAll(); //eğer parametre userıd yoksa tüm postları çeker
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post createPost(CreatePostRequest newPostRequest) {
        User user = userService.getUserById(newPostRequest.getUserId());  //önce user var mı onu kontrol ederiz.
        if(user==null){
            return null;
        }
        Post post = new Post();
        post.setId(newPostRequest.getId());
        post.setText(newPostRequest.getText());
        post.setTitle(newPostRequest.getTitle());
        post.setUser(user);
        return postRepository.save(post);
    }
}
