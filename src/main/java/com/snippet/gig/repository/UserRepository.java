package com.snippet.gig.repository;

import com.snippet.gig.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByEmail(String email);

    Optional<User> findByUsername(String username);

//    List<User> findByRole(String role);

    // solved error by realising User is treated as an entity
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.project = null WHERE u.project.id = :projectId ")
    void setProjectIdNull(@Param("projectId") Long projectId);

}
