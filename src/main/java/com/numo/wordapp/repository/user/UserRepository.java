package com.numo.wordapp.repository.user;

import com.numo.wordapp.entity.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUserId(String user_id);
    /*Optional<User> findByUser_id(String user_id);
    boolean existsByUser_id(String user_id);*/
}
