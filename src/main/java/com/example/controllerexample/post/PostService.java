package com.example.controllerexample.post;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
@Service
public class PostService {

    public final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post getPost(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    public Post createPost(@Valid Post post) {
        return postRepository.save(post);
    }

    public Page<Post> getPostList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public void deletePost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            postRepository.deleteById(postId);
        } else {
            throw new PostNotFoundException(postId);
        }

    }

    public Post replacePost(Long postId,Post req) {
        return postRepository.findById(postId)
                .map(post -> {
                    post.setTitle(req.getTitle());
                    post.setDesc(req.getDesc());
                    return postRepository.save(post);
                }).orElseThrow(() -> new PostNotFoundException(postId));
    }
}
