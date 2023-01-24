package com.example.questionapp.dataAccess;

import com.example.questionapp.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CommentRepository extends JpaRepository<Comment,Long> {


}
