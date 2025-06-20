package org.example.user_service.repository;

import org.example.user_service.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserRepository {
    Page<UserResponse> findUserCriteria(
            String username,
            String email,
            String phoneNumber,
            String firstName,
            String lastName,
            Pageable pageable
    );
}
