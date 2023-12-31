package com.example.supersns.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;


public interface PostCustomRepository {

    Slice<Post> getUserNewsfeed(Long userId, List<Long> users, Pageable pageable);
}
