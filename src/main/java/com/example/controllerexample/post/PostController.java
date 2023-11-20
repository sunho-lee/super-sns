package com.example.controllerexample.post;

import com.example.controllerexample.auth.CustomUserDetails;
import com.example.controllerexample.user.User;
import jakarta.validation.Valid;
import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    public PostController(PostService postService,
                          PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        PostResponse dto = postMapper.postToPostResponseDto(post);

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPostList(@PageableDefault(sort = "id") Pageable pageable) {
        Page<PostResponse> posts = postService.getAllPostsWithPage(pageable)
                .map(postMapper::postToPostResponseDto);

        return ResponseEntity.ok().body(posts);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest req,
                                                   @AuthenticationPrincipal CustomUserDetails user) {
        Post post = postMapper.postRequestDtoToPost(req);
        Post savedPost = postService.createPost(user, post);
        PostResponse res = postMapper.postToPostResponseDto(savedPost);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPost.getId()).toUri();

        return ResponseEntity.created(location).body(res);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails me) {
        postService.removeMyPostById(postId, me.getId());
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> replacePost(@PathVariable Long postId, @Valid @RequestBody PostRequest req,
                                                    @AuthenticationPrincipal CustomUserDetails user) {
        Post post = postMapper.postRequestDtoToPost(req);
        Post updatedPost = postService.replaceMyPost(postId, post, user.getId());
        PostResponse res = postMapper.postToPostResponseDto(updatedPost);

        return ResponseEntity.ok().body(res);
    }


}
