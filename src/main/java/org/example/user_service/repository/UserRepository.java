package org.example.user_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.example.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(UUID userId);
}
