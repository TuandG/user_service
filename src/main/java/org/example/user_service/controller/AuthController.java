package org.example.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.example.user_service.dto.request.UserLoginRequest;
import org.example.user_service.dto.request.UserRequest;
import org.example.user_service.exception.AppException;
import org.example.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
   private final UserService userService;

   @PostMapping("/register")
   public ResponseEntity<Object> createUser(
           @RequestPart("user") UserRequest userCreateRequest,
           @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
   ){
       try {
           userService.createUser(userCreateRequest, avatarFile);
           return new ResponseEntity<>(HttpStatus.CREATED);
       } catch (AppException e) {
           return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
       } catch (Exception ex) {
           return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginRequest userLoginRequest) {
        try {
            return ResponseEntity.ok(userService.login(userLoginRequest));
        } catch (AppException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
