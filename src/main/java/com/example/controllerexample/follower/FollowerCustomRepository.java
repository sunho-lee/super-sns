package com.example.controllerexample.follower;

import com.example.controllerexample.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface FollowerCustomRepository {

    List<User> getUserFollowers(Long userId);

    List<User> getUserFollowees(Long userId);
}
