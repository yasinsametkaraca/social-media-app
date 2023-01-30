package com.example.questionapp.services;

import com.example.questionapp.dataAccess.PostRepository;
import com.example.questionapp.entities.Post;
import com.example.questionapp.entities.User;
import com.example.questionapp.requests.CreatePostRequest;
import com.example.questionapp.requests.UpdatePostRequest;
import com.example.questionapp.responses.LikeResponse;
import com.example.questionapp.responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private PostRepository postRepository;
    private UserService userService;
    private LikeService likeService;


    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Autowired
    public void setLikeService(@Lazy LikeService likeService) {
        this.likeService = likeService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {                       //optionalın mantığı parametre oladabilir olmayadabilir, ikisine özelde çalışır.
        List<Post> postList;
        if (userId.isPresent()) {                                                       //isPresent in mantığı eğer userId parametresi geldiyse
            postList = postRepository.findByUserId(userId.get());
        }else{
            postList = postRepository.findAll();                                         //eğer parametre userıd yoksa tüm postları çeker
        }
        return postList.stream().map(post -> {
            List<LikeResponse> likes = likeService.getAllLikes(Optional.ofNullable(null),Optional.of(post.getId()));
            return new PostResponse(post,likes);}).collect(Collectors.toList());       //postları teker teker PostResponse a mapledik.
    }
    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }
    public PostResponse getPostByIdWithLikes(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<LikeResponse> likes = likeService.getAllLikes(Optional.ofNullable(null),Optional.of(postId));
        return new PostResponse(post,likes);
    }

    public Post createPost(CreatePostRequest newPostRequest) {
        User user = userService.getUserById(newPostRequest.getUserId());            //önce user var mı onu kontrol ederiz.
        if(user==null){
            return null;
        }
        Post post = new Post();
        post.setId(newPostRequest.getId());
        post.setText(newPostRequest.getText());
        post.setTitle(newPostRequest.getTitle());
        post.setUser(user);
        post.setCreateDate(new Date());
        return postRepository.save(post);
    }

    public Post updatePostById(Long postId, UpdatePostRequest updatePostRequest) {  //bütün postu değiştirmicez ki sadece title ve text alanlarını değiştiricez bu yüzden requests in içine UpdatePostRequest oluşturduk.
        Optional <Post> post = postRepository.findById(postId);
        if(post.isPresent()){
            Post updatePost = post.get();
            updatePost.setText(updatePostRequest.getText());
            updatePost.setTitle(updatePostRequest.getTitle());
            postRepository.save(updatePost);
            return updatePost;
        }
        return null;
    }

    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
    }
}
