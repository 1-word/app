package com.numo.wordapp.security.repository;

import com.numo.wordapp.security.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
    /*Optional<User> findByUser_id(String user_id);
    boolean existsByUser_id(String user_id);*/
}
