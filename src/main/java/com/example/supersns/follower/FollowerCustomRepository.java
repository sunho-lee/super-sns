package com.example.supersns.follower;

import com.example.supersns.user.User;

import java.util.List;

public interface FollowerCustomRepository {

    List<User> getUserFollowers(Long userId);

    List<User> getUserFollowees(Long userId);
}
