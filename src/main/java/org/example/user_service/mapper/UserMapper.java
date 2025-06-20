package org.example.user_service.mapper;

import org.example.user_service.dto.request.UserRequest;
import org.example.user_service.dto.response.UserResponse;
import org.example.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Mapper(componentModel = "spring", imports = {ServletUriComponentsBuilder.class})
public interface UserMapper {
    User userCreateRequestToUser(UserRequest userCreateRequest);
    @Mapping(target = "avatar", expression = "java(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + \"/\" + user.getAvatar())")
    UserResponse userToUserResponse(User user);
}
