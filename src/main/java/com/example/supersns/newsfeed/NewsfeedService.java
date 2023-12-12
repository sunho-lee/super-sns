package com.example.supersns.newsfeed;

import com.example.supersns.follower.FollowerService;
import com.example.supersns.post.Post;
import com.example.supersns.post.PostService;
import com.example.supersns.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsfeedService {

    private final PostService postService;

    private final FollowerService followerService;

    public NewsfeedService(PostService postService, FollowerService followerService) {
        this.postService = postService;
        this.followerService = followerService;
    }


    public Slice<Post> getUserNewsfeed(Long id, Pageable pageable) {
       List<Long> users = followerService.getUserFollowees(id)
               .stream().map(User::getId).toList();
       return postService.getUserNewsfeed(id, users, pageable);
    }
}
