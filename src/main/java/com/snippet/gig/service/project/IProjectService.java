package com.snippet.gig.service.project;

import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;

import java.util.List;

public interface IProjectService {
    // basic crud operations
    Project createProject(Project project);
    Project getProject(Long id);
    Project updateProject(Project project);
    void deleteProject(Long id);

    // Other Read Operations
    List<Task> getTasks(Long projectId);

    // Other Update Operations
    // Other Delete Operations
}
