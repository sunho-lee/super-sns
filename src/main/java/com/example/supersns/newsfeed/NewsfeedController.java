package com.example.supersns.newsfeed;

import com.example.supersns.post.PostMapper;
import com.example.supersns.post.PostResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<?> getUserNewsfeed(@RequestParam Long myId, Pageable pageable) {
        Slice<PostResponse> posts = newsfeedService.getUserNewsfeed(myId, pageable)
                .map(postMapper::postToPostResponseDto);
        return ResponseEntity.ok(posts);
    }

}
