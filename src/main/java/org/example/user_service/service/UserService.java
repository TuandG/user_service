package org.example.user_service.service;

import org.example.user_service.dto.request.UserLoginRequest;
import org.example.user_service.dto.request.UserRequest;
import org.example.user_service.dto.response.CustomPageResponse;
import org.example.user_service.dto.response.UserLoginResponse;
import org.example.user_service.dto.response.UserResponse;
import org.example.user_service.exception.AppException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface UserService {
    void createUser(UserRequest userCreateRequest, MultipartFile avatarFile) throws AppException;
    UserLoginResponse login(UserLoginRequest userLoginRequest) throws RuntimeException, AppException;
    CustomPageResponse<UserResponse> getUsers(int page, int size, String username, String email, String phoneNumber, String firstName, String lastName);
    UserResponse getUserById(UUID userId) throws AppException;
    UserResponse getUserByUsername(String username) throws AppException;
    UserResponse updateUser(String username, UserRequest userRequest) throws AppException;
    void deleteUser(UUID userId) throws AppException;
}
