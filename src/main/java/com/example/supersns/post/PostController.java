package com.example.supersns.post;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * The type Post controller.
 */
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


    /**
     * 게시물 1건 가져오기
     *
     * @param postId the post id
     * @return the post
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        PostResponse dto = postMapper.postToPostResponseDto(post);

        return ResponseEntity.ok(dto);
    }


    /**
     * 전체 게시물 가져오기
     *
     * @param pageable the pageable
     * @return the post list
     */
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPostList(@PageableDefault(sort = "id") Pageable pageable) {
        Page<PostResponse> posts = postService.getAllPostsWithPage(pageable)
                .map(postMapper::postToPostResponseDto);

        return ResponseEntity.ok().body(posts);
    }


    /**
     * Create post response entity.
     *
     * @param req  the req
     * @param userId the id of user who want to post
     * @return the response entity
     */
    @PostMapping()
    public ResponseEntity<PostResponse> createPost(@RequestParam Long userId, @Valid @RequestBody PostRequest req) {
        Post post = postMapper.postRequestDtoToPost(req);
        Post savedPost = postService.createPost(userId, post);
        PostResponse res = postMapper.postToPostResponseDto(savedPost);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPost.getId()).toUri();

        return ResponseEntity.created(location).body(res);
    }

    /**
     * Delete post.
     *
     * @param postId the post id
     * @param userId     user
     */
    @DeleteMapping("/{postId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId, @RequestParam Long userId) {
        postService.removeMyPostById(postId, userId);
    }

    /**
     * Replace post response entity.
     *
     * @param postId the post id
     * @param req    the req
     * @param userId   the user
     * @return the response entity
     */
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> replacePost(@PathVariable Long postId, @Valid @RequestBody PostRequest req,
                                                    @RequestParam Long userId) {
        Post post = postMapper.postRequestDtoToPost(req);
        Post updatedPost = postService.replaceMyPost(postId, post, userId);
        PostResponse res = postMapper.postToPostResponseDto(updatedPost);

        return ResponseEntity.ok().body(res);
    }


}
