package com.example.hrm.mapper;

import com.example.hrm.dto.request.UserCreateRequest;
import com.example.hrm.dto.request.UserUpdateRequest;
import com.example.hrm.dto.response.UserResponse;
import com.example.hrm.entity.User;
import com.example.hrm.entity.EmployeeProfile;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "profile.fullName", target = "fullName")
    @Mapping(source = "profile.phone", target = "phone")
    @Mapping(source = "profile.address", target = "address")
    @Mapping(source = "profile.imageUrl", target = "avatarUrl")
    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    User toUser(UserCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(source = "request.fullName", target = "fullName")
    @Mapping(source = "request.phone", target = "phone")
    @Mapping(source = "request.address", target = "address")
    @Mapping(source = "user", target = "user")
    EmployeeProfile toProfile(UserCreateRequest request, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
