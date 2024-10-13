package com.snippet.gig.repository;

import com.snippet.gig.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM Project t WHERE t.id = :projectId")
    void deleteProject(@Param("projectId") Long projectId);
}
