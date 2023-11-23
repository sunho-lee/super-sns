package com.example.controllerexample.follower;

import com.example.controllerexample.auth.CustomUserDetails;
import com.example.controllerexample.user.User;
import com.example.controllerexample.user.UserMapper;
import com.example.controllerexample.user.dto.UserResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1")
public class FollowerController {

    private final FollowerService followerService;
    private final UserMapper userMapper;

    public FollowerController(FollowerService followerService, UserMapper userMapper) {
        this.followerService = followerService;
        this.userMapper = userMapper;
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<?> getUserFollowers(@PathVariable Long userId, Pageable pageable){
        Slice<UserResponse> users = followerService.getUserFollowers(userId, pageable)
                .map(userMapper::userToUserResponse);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/followees/{userId}")
    public ResponseEntity<?> getUserFollowees(@PathVariable Long userId, Pageable pageable){
        Slice<UserResponse> users = followerService.getUserFollowees(userId, pageable)
                .map(userMapper::userToUserResponse);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/followers/{userId}")
    public ResponseEntity<?> followUser(@AuthenticationPrincipal CustomUserDetails me, @PathVariable Long userId){
        followerService.followUser(me, userId);
        return ResponseEntity.ok("follow successfully");
    }

    @DeleteMapping("/followers/{userId}")
    public ResponseEntity<?> unfollowUser(@AuthenticationPrincipal CustomUserDetails me, @PathVariable Long userId){
        followerService.unFollowUser(me, userId);
        return ResponseEntity.ok("unfollow successfully");
    }

}
