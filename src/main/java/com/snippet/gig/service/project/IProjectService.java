package com.snippet.gig.service.project;

import com.snippet.gig.dto.ProjectDto;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.requestDto.CreateProjectRequest;
import com.snippet.gig.requestDto.UpdateProjectRequest;

import java.util.List;

public interface IProjectService {
    // basic crud operations
    Project createProject(CreateProjectRequest project) throws AlreadyExistsException;

    Project getProject(Long id) throws ResourceNotFoundException;

    void updateProject(UpdateProjectRequest project, Long id) throws ResourceNotFoundException;

    void deleteProject(Long id) throws ResourceNotFoundException;

    // Other Read Operations
    List<Task> getTasks(Long projectId) throws ResourceNotFoundException;

    List<User> getUsers(Long projectId) throws ResourceNotFoundException;

    List<Project> getAll();

    void deleteAll();

    void assignProjectToUser(Long projectId, Long userId) throws ResourceNotFoundException, AlreadyExistsException;

    // Other Update Operations
    // Other Delete Operations

    ProjectDto convertProjectToProjectDto(Project project);
}
