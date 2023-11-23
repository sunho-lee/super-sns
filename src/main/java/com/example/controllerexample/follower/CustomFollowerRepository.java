package com.example.controllerexample.follower;

import com.example.controllerexample.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomFollowerRepository  {

    Slice<User> getUserFollowers(Long userId, Pageable pageable);

    Slice<User> getUserFollowees(Long userId, Pageable pageable);
}
