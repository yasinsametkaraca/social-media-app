package com.example.questionapp.dataAccess;

import com.example.questionapp.entities.Like;
import com.example.questionapp.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {


}
