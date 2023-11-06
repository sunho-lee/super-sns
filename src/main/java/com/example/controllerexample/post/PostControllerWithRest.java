package com.example.controllerexample.post;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/posts")
public class PostControllerWithRest {

    private final PostService postService;
    private final PostModelAssembler postAssembler;
    private final PagedResourcesAssembler<Post> pagedResourcesAssembler;
    private final PostMapper postMapper;

    public PostControllerWithRest(PostService postService,
                                  PostModelAssembler postAssembler,
                                  PagedResourcesAssembler<Post> pagedResourcesAssembler,
                                  PostMapper postMapper) {
        this.postService = postService;
        this.postAssembler = postAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.postMapper = postMapper;
    }

    @GetMapping("/{postId}")
    public EntityModel<PostResponse> getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        return postAssembler.toModel(post);
    }

    @GetMapping
    public PagedModel<EntityModel<PostResponse>> getPostList(
            @PageableDefault(sort = "id") Pageable pageable
    ) {
        Page<Post> posts = postService.getPostList(pageable);
        return pagedResourcesAssembler.toModel(posts, postAssembler);
    }

    @PostMapping
    public ResponseEntity<EntityModel<PostResponse>> createPost(
            @Valid @RequestBody PostRequest req) {
        Post post = postMapper.postRequestDtoToPost(req);
        Post savedPost = postService.createPost(post);
        EntityModel<PostResponse> res = postAssembler.toModel(savedPost);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPost.getId()).toUri();
        return ResponseEntity.created(location).body(res);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }

    @PutMapping("/{postId}")
    public EntityModel<PostResponse> replacePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest req) {
        Post post = postMapper.postRequestDtoToPost(req);
        Post updatedPost = postService.replacePost(postId, post);
        return postAssembler.toModel(updatedPost);
    }


}
