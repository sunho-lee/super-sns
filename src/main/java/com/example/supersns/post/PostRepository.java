package com.example.supersns.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
   Optional<Post> findById(long id);

   Optional<Post> findByIdAndUserId(long id, long userId);

   void deleteByIdAndUserId(long id, long userId);


}
