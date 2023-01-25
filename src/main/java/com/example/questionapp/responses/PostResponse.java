package com.example.questionapp.responses;


import com.example.questionapp.entities.Post;
import lombok.Data;

@Data
public class PostResponse {  //bu classı kullanmamızın sebebi password gibi bilgileri dönmek istememiz. sadece istediğimiz bilgileri döneriz.
    // {"id": 102,"userId": 1,"username": "ysk","text": "neden haberler","title": "haberlerim" }

    private Long id;
    private Long userId;
    private String username;
    private String text;
    private String title;

    public PostResponse(Post post){ //constructor mapping
       this.id=post.getId();
       this.userId=post.getUser().getId();
       this.username=post.getUser().getUsername();
       this.title=post.getTitle();
       this.text=post.getText();
    }
}
