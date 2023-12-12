package com.example.supersns.post;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "content", source = "content")
    PostResponse postToPostResponseDto(Post post);
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    Post postRequestDtoToPost(PostRequest postRequestDto);
}
