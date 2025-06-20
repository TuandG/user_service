package org.example.user_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user_service.dto.request.UserLoginRequest;
import org.example.user_service.dto.request.UserRequest;
import org.example.user_service.dto.response.CustomPageResponse;
import org.example.user_service.dto.response.UserLoginResponse;
import org.example.user_service.dto.response.UserResponse;
import org.example.user_service.entity.Role;
import org.example.user_service.entity.RoleSystem;
import org.example.user_service.entity.User;
import org.example.user_service.exception.AppException;
import org.example.user_service.mapper.UserMapper;
import org.example.user_service.repository.CustomUserRepository;
import org.example.user_service.repository.RoleRepository;
import org.example.user_service.repository.UserRepository;
import org.example.user_service.service.UserService;
import org.example.user_service.utils.ExceptionUtils;
import org.example.user_service.utils.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomUserRepository customUserRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private static final Path ROOT = Paths.get(System.getProperty("user.dir"), "upload");

    @Value("${app.jwt.secret-key}")
    private String secretKey;

    @Value("${app.jwt.exp-acc}")
    private Long expireAccessTime;

    @Value("${app.jwt.exp-rf}")
    private Long expireRefreshTime;

    @Override
    public void createUser(UserRequest userCreateRequest, MultipartFile avatarFile) throws AppException {
        Role role = roleRepository.findByName(RoleSystem.USER.name()).orElseThrow(() -> ExceptionUtils.ERROR_MESSAGES.get(ExceptionUtils.Error.E_ROLE_NOT_FOUND));

        User user = userMapper.userCreateRequestToUser(userCreateRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(true);
        user.setRole(role);

        String avatarPath = null;
        Path imgDir = ROOT.resolve("avatar");

        try {
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String contentType = avatarFile.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new AppException("File is invalid format", HttpStatus.BAD_REQUEST);
                }

                Files.createDirectories(imgDir);
                String filename = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
                Path savedFile = imgDir.resolve(filename);
                avatarFile.transferTo(savedFile.toFile());

                avatarPath = "avatar/" + filename;
                user.setAvatar(avatarPath);
            } else {
                user.setAvatar("avatar/default-avatar.jpg");
            }

            userRepository.save(user);

        } catch (Exception ex) {
            if (avatarPath != null) {
                try {
                    Files.deleteIfExists(imgDir.resolve(Paths.get(avatarPath).getFileName()));
                } catch (IOException ignored) {}
            }
            throw new AppException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserLoginResponse login(UserLoginRequest userLoginRequest) throws AppException {
        Optional<User> userOpt = userRepository.findUserByUsername(userLoginRequest.getUsername());
        if(userOpt.isEmpty()) {
            throw ExceptionUtils.ERROR_MESSAGES.get(ExceptionUtils.Error.E_USER_NOT_FOUND);
        }

        User user = userOpt.get();
        if(!user.isStatus()) {
            throw ExceptionUtils.ERROR_MESSAGES.get(ExceptionUtils.Error.E_USER_DELETED);
        }

        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw ExceptionUtils.ERROR_MESSAGES.get(ExceptionUtils.Error.E_UNAUTHORIZED);
        }

        try {
            return UserLoginResponse.builder()
                    .accessToken(Token.generateToken(secretKey, expireAccessTime, user))
                    .refreshToken(Token.generateToken(secretKey, expireRefreshTime, user))
                    .build();
        } catch (Exception e) {
            throw ExceptionUtils.ERROR_MESSAGES.get(ExceptionUtils.Error.E_INTERNAL_SERVER);
        }
    }

    @Override
    public CustomPageResponse<UserResponse> getUsers(int page, int size, String username, String email, String phoneNumber, String firstName, String lastName) {
        Page<UserResponse> result = customUserRepository.findUserCriteria(username, email, phoneNumber, firstName, lastName, PageRequest.of(page, size));
        return new CustomPageResponse<>(result);
    }

    @Override
    public UserResponse getUserById(UUID userId) throws AppException {
        User user = userRepository.findUserById(userId).orElseThrow(() -> ExceptionUtils.ERROR_MESSAGES.get(ExceptionUtils.Error.E_USER_NOT_FOUND));
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) throws AppException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> ExceptionUtils.ERROR_MESSAGES.get(ExceptionUtils.Error.E_USER_NOT_FOUND));
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(String username, UserRequest userRequest) throws AppException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> ExceptionUtils.ERROR_MESSAGES.get(ExceptionUtils.Error.E_USER_NOT_FOUND));

        if (userRequest.getUsername() != null) user.setUsername(userRequest.getUsername());
        if (userRequest.getPassword() != null) user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        if (userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());
        if (userRequest.getPhoneNumber() != null) user.setPhoneNumber(userRequest.getPhoneNumber());
        if (userRequest.getDob() != null) user.setDob(userRequest.getDob());
        if (userRequest.getFirstName() != null) user.setFirstName(userRequest.getFirstName());
        if (userRequest.getLastName() != null) user.setLastName(userRequest.getLastName());
        userRepository.save(user);

        return userMapper.userToUserResponse(user);
    }

    @Override
    public void deleteUser(UUID userId) throws AppException {
        User user = userRepository.findUserById(userId).orElseThrow(() -> ExceptionUtils.ERROR_MESSAGES.get(ExceptionUtils.Error.E_USER_NOT_FOUND));
        user.setStatus(false);
        userRepository.save(user);
    }
}
