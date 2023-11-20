package com.example.controllerexample.post;

import com.example.controllerexample.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    public Post createPost(User user, @Valid Post post) {
        post.setUser(user);
        return postRepository.save(post);
    }

    public Page<Post> getAllPostsWithPage(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Transactional
    public void removeMyPostById(Long postId, Long userId) {
        postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        postRepository.deleteByIdAndUserId(postId, userId);
    }

    public Post replaceMyPost(Long postId, @Valid Post req, Long userId) {
        return postRepository.findByIdAndUserId(postId, userId)
                .map(post -> {
                    post.setContent(req.getContent());
                    return postRepository.save(post);
                }).orElseThrow(() -> new PostNotFoundException(postId));
    }
}
