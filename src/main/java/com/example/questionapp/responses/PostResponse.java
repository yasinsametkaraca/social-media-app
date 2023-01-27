package com.example.questionapp.responses;


import com.example.questionapp.entities.Like;
import com.example.questionapp.entities.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostResponse {  //bu classı kullanmamızın sebebi password gibi bilgileri dönmek istememiz. Sadece istediğimiz bilgileri döneriz.
    // {"id": 102,"userId": 1,"username": "ysk","text": "neden haberler","title": "haberlerim" }

    private Long id;
    private Long userId;
    private String username;
    private String text;
    private String title;

    private List<LikeResponse> postLikes;
    public PostResponse(Post post, List<LikeResponse> likes){ //constructor mapping
       this.id=post.getId();
       this.userId=post.getUser().getId();
       this.username=post.getUser().getUsername();
       this.title=post.getTitle();
       this.text=post.getText();
       this.postLikes=likes;
    }
}
