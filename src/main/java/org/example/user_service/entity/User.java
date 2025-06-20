package org.example.user_service.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "username", unique = true)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "phoneNumber", unique = true)
    private String phoneNumber;
    private LocalDate dob;
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
