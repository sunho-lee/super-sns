package com.example.supersns.follower;

import com.example.supersns.user.UserMapper;
import com.example.supersns.user.dto.UserProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> getUserFollowers(@PathVariable Long userId) {
        List<UserProfileResponse> users = followerService.getUserFollowers(userId)
                .stream().map(userMapper::userToUserProfileResponse).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/followees/{userId}")
    public ResponseEntity<?> getUserFollowees(@PathVariable Long userId) {
        List<UserProfileResponse> users = followerService.getUserFollowees(userId)
                .stream().map(userMapper::userToUserProfileResponse).toList();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/followers/{myId}/{userId}")
    public ResponseEntity<?> followUser(@PathVariable Long myId, @PathVariable Long userId) {
        followerService.followUser(myId, userId);
        return ResponseEntity.ok("follow successfully");
    }

    @DeleteMapping("/followers/{myId}/{userId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long myId, @PathVariable Long userId) {
        followerService.unFollowUser(myId, userId);
        return ResponseEntity.ok("unfollow successfully");
    }

}
