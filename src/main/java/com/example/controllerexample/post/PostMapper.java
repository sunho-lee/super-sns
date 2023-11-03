package com.example.controllerexample.post;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper {

    PostResponse postToPostResposeDto(Post post);
    @Mapping(target = "id", ignore = true)
    Post postRequestDtoToPost(PostRequest postRequestDto);
}
