package org.example.user_service.mapper;

import org.example.user_service.dto.request.UserRequest;
import org.example.user_service.dto.response.UserResponse;
import org.example.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userCreateRequestToUser(UserRequest userCreateRequest);
    UserResponse userToUserResponse(User user);
}
