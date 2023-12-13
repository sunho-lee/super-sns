package com.example.supersns.role;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoleMapper {

    default String roleToName(Role role) {
        return role.getName();
    }
}
