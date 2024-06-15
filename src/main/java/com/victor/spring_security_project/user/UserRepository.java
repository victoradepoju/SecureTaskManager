package com.victor.spring_security_project.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    // TODO: instead of a new query, use get id using findByEmail above
    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Optional<Integer> findIdByEmail(@Param("email") String email);

}
