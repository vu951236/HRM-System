package com.example.hrm.mapper;

import com.example.hrm.dto.response.UserResponse;
import com.example.hrm.entity.Role;
import com.example.hrm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "role", target = "role", qualifiedByName = "mapRoleToString")
    @Mapping(source = "profile.fullName", target = "fullName")
    @Mapping(source = "profile.phone", target = "phone")
    @Mapping(source = "profile.address", target = "address")
    UserResponse toUserResponse(User user);

    @Named("mapRoleToString")
    default String mapRoleToString(Role role) {
        return role != null ? role.getName() : null;
    }
}




