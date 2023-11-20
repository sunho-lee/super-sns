package com.example.controllerexample.user;

import com.example.controllerexample.role.RoleMapper;
import com.example.controllerexample.user.dto.SignUpRequest;
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


    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    User signUpRequestToUser(SignUpRequest signUpRequest);

    User userRequestToUser(UserRequest userRequest);

}
