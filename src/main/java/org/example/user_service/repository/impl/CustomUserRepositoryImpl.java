package org.example.user_service.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.user_service.dto.response.UserResponse;
import org.example.user_service.repository.CustomUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<UserResponse> findUserCriteria(
            String username,
            String email,
            String phoneNumber,
            String firstName,
            String lastName,
            Pageable pageable) {

        StringBuilder selectSql = new StringBuilder("""
            SELECT
              u.id,
              u.username,
              u.email,
              u.phone_number AS phoneNumber,
              u.first_name AS firstName,
              u.last_name AS lastName,
              u.dob,
              u.status
            FROM users u
            WHERE 1=1
            """);

        StringBuilder countSql = new StringBuilder("""
            SELECT COUNT(*) FROM users u WHERE 1=1
            """);

        Map<String, Object> params = new HashMap<>();

        if (username != null && !username.isBlank()) {
            selectSql.append(" AND u.username = :username");
            countSql.append(" AND u.username = :username");
            params.put("username", username);
        }
        if (email != null && !email.isBlank()) {
            selectSql.append(" AND u.email = :email");
            countSql.append(" AND u.email = :email");
            params.put("email", email);
        }
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            selectSql.append(" AND u.phone_number = :phoneNumber");
            countSql.append(" AND u.phone_number = :phoneNumber");
            params.put("phoneNumber", phoneNumber);
        }
        if (firstName != null && !firstName.isBlank()) {
            selectSql.append(" AND u.first_name = :firstName");
            countSql.append(" AND u.first_name = :firstName");
            params.put("firstName", firstName);
        }
        if (lastName != null && !lastName.isBlank()) {
            selectSql.append(" AND u.last_name = :lastName");
            countSql.append(" AND u.last_name = :lastName");
            params.put("lastName", lastName);
        }

        Query nativeQuery = entityManager.createNativeQuery(selectSql.toString());
        nativeQuery.setFirstResult((int) pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());

        Query countQuery  = entityManager.createNativeQuery(countSql.toString());

        params.forEach((name, value) -> {
            nativeQuery.setParameter(name, value);
            countQuery.setParameter(name, value);
        });

        List<Object[]> rows = nativeQuery.getResultList();
        List<UserResponse> content = rows.stream()
                .map(cols -> new UserResponse(
                        UUID.fromString(cols[0].toString()),
                        cols[1].toString(),
                        cols[2].toString(),
                        cols[3] != null ? cols[3].toString() : null,
                        cols[4] != null ? cols[4].toString() : null,
                        cols[5] != null ? cols[5].toString() : null,
                        cols[6] != null ? LocalDate.parse(cols[6].toString()) : null,
                        cols[7] != null && (boolean) cols[7]
                ))
                .toList();
        Number totalCount = (Number) countQuery.getSingleResult();
        return new PageImpl<>(content, pageable,  totalCount.longValue());
    }
}
