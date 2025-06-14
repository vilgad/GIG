package com.snippet.gig.controller;

import com.snippet.gig.dto.ProjectDto;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.enums.Status;
import com.snippet.gig.requestDto.CreateProjectRequest;
import com.snippet.gig.requestDto.UpdateProjectRequest;
import com.snippet.gig.response.ApiResponse;
import com.snippet.gig.service.project.IProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("${api.prefix}/projects")
@Tag(name = "Project APIs")
public class ProjectController {
    private final IProjectService projectService;

    @Autowired
    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/create-project")
    public ResponseEntity<ApiResponse> createProject(
            @RequestBody CreateProjectRequest request
    ) {
        Project project = projectService.createProject(request);
        ProjectDto projectDto = projectService.convertProjectToProjectDto(project);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Project Created Successfully",
                        projectDto
                ));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/private/update-project")
    public ResponseEntity<ApiResponse> updateProject(
            @RequestBody UpdateProjectRequest request,
            @RequestParam Long id
    ) {
        projectService.updateProject(request, id);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Project Updated Successfully",
                        null
                ));
    }

    @GetMapping("/public/get-project-by-id")
    public ResponseEntity<ApiResponse> getProjectById(
            @RequestParam Long id
    ) {
        Project project = projectService.getProject(id);
        ProjectDto projectDto = projectService.convertProjectToProjectDto(project);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Project fetched Successfully",
                        projectDto
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete-project")
    public ResponseEntity<ApiResponse> deleteProject(
            @RequestParam Long id
    ) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Project deleted Successfully",
                        null
                ));
    }

    @GetMapping("/public/get-tasks")
    public ResponseEntity<ApiResponse> getTasks(
            @RequestParam Long projectId
    ) {
        List<Task> tasks = projectService.getTasks(projectId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Project Tasks fetched Successfully",
                        tasks
                ));
    }

    @GetMapping("/public/get-users")
    public ResponseEntity<ApiResponse> getUsers(
            @RequestParam Long projectId
    ) {
        List<User> users = projectService.getUsers(projectId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Users assigned to project fetched Successfully",
                        users
                ));
    }

    @GetMapping("/public/get-all-projects")
    public ResponseEntity<ApiResponse> getAll() {
        List<Project> projects = projectService.getAll();
        return ResponseEntity.ok(
                new ApiResponse(
                        "projects fetched Successfully",
                        projects
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete-all-projects")
    public ResponseEntity<ApiResponse> deleteAll() {
        projectService.deleteAll();
        return ResponseEntity.ok(
                new ApiResponse(
                        "projects deleted Successfully",
                        null
                ));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/private/assign-project-to-user")
    public ResponseEntity<ApiResponse> assignProjectToUser(
            @RequestParam Long projectId,
            @RequestParam Long userId
    ) {
        projectService.assignProjectToUser(projectId, userId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "project assigned to user Successfully",
                        null
                ));
    }

    @GetMapping("/public/get-kanban-board")
    public ResponseEntity<ApiResponse> getKanbanBoard(
            @RequestParam String projectName) {
        Map<Status, List<Task>> data = projectService.getKanbanBoard(projectName);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Success",
                        data
                ));
    }
}
