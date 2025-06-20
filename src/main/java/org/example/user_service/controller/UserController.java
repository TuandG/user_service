package org.example.user_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user_service.dto.request.UserRequest;
import org.example.user_service.exception.AppException;
import org.example.user_service.service.UserService;
import org.example.user_service.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getUsers(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "username", defaultValue = "", required = false) String username,
            @RequestParam(name = "email", defaultValue = "", required = false) String email,
            @RequestParam(name = "phoneNumber", defaultValue = "", required = false) String phoneNumber,
            @RequestParam(name = "firstName", defaultValue = "", required = false) String firstName,
            @RequestParam(name = "lastName", defaultValue = "", required = false) String lastName
    ) {
        try {
            return ResponseEntity.ok(userService.getUsers(page, size, username, email, phoneNumber, firstName, lastName));
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getUserByUserId(@PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(userService.getUserById(userId));
        } catch (AppException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile() {
        String username = SecurityUtils.getCurrentUser();
        try {
            return ResponseEntity.ok(userService.getUserByUsername(username));
        } catch (AppException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(
            @RequestBody UserRequest userUpdateRequest) {
        String username = SecurityUtils.getCurrentUser();
        try {
            return ResponseEntity.ok(userService.updateUser(username, userUpdateRequest));
        } catch (AppException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable UUID userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (AppException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
