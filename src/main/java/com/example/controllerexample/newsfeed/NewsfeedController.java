package com.example.controllerexample.newsfeed;

import com.example.controllerexample.auth.CustomUserDetails;
import com.example.controllerexample.post.PostMapper;
import com.example.controllerexample.post.PostResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class NewsfeedController {

    private final NewsfeedService newsfeedService;
    private final PostMapper postMapper;

    public NewsfeedController(NewsfeedService newsfeedService,
                              PostMapper postMapper) {
        this.newsfeedService = newsfeedService;
        this.postMapper = postMapper;
    }

    @GetMapping("/newsfeed")
    public ResponseEntity<?> getUserNewsfeed(@AuthenticationPrincipal CustomUserDetails me,
                                             Pageable pageable){
        Slice<PostResponse> posts = newsfeedService.getUserNewsfeed(me.getId(), pageable)
                .map(postMapper::postToPostResponseDto);
        return ResponseEntity.ok(posts);
    }

}
