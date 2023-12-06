package com.example.controllerexample.user;

import com.example.controllerexample.role.RoleMapper;
import com.example.controllerexample.user.dto.SignUpRequest;
import com.example.controllerexample.user.dto.UserProfileResponse;
import com.example.controllerexample.user.dto.UserRequest;
import com.example.controllerexample.user.dto.UserResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {RoleMapper.class}
)
public interface UserMapper {

    @Mapping(target = "roles", source = "roles")
    UserResponse userToUserResponse(User user);

    UserProfileResponse userToUserProfileResponse(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "followees", ignore = true)
    User signUpRequestToUser(SignUpRequest signUpRequest);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "followees", ignore = true)
    User userRequestToUser(UserRequest userRequest);

}
