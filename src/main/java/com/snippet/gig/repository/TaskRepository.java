package com.snippet.gig.repository;

import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Task t WHERE t.id = :taskId")
    void deleteAllUsersFromTask(@Param("taskId") Long taskId);

    @Transactional
    @Modifying
    @Query("UPDATE Task u SET u.project = null WHERE u.project.id = :projectId ")
    void setProjectIdNull(@Param("projectId") Long projectId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Task t WHERE t.project.id = :projectId")
    void deleteProjectTasks(@Param("projectId") Long projectId);

    List<Task> findAllByProjectAndStatus(Project project, Status status);
}
