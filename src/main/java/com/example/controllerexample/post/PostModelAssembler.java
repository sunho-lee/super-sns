package com.example.controllerexample.post;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
class PostModelAssembler implements RepresentationModelAssembler<Post, EntityModel<PostResponse>> {


    private final PostMapper postMapper;

    PostModelAssembler(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Override
    public EntityModel<PostResponse> toModel(Post post) {

        PostResponse dto = postMapper.postToPostResposeDto(post);

        return EntityModel.of(dto,
                linkTo(methodOn(PostControllerWithRest.class).getPost(post.getId())).withSelfRel(),
                linkTo(PostControllerWithRest.class).withRel("posts")
        );


    }

    @Override
    public CollectionModel<EntityModel<PostResponse>> toCollectionModel(Iterable<? extends Post> entities) {
        List<EntityModel<PostResponse>> posts = new ArrayList<>();
        for (Post post : entities) {
            PostResponse dto = postMapper.postToPostResposeDto(post);
            posts.add(EntityModel.of(dto,
                    linkTo(methodOn(PostControllerWithRest.class).getPost(post.getId())).withSelfRel(),
                    linkTo(PostControllerWithRest.class).withRel("posts")));
        }
        return CollectionModel.of(posts);
    }

}
