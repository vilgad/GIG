package com.snippet.gig.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snippet.gig.dto.ProjectDto;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.requestDto.CreateProjectRequest;
import com.snippet.gig.requestDto.UpdateProjectRequest;
import com.snippet.gig.response.ApiResponse;
import com.snippet.gig.service.project.IProjectService;


@RestController
@RequestMapping("${api.prefix}/projects")
public class ProjectController {
    private final IProjectService projectService;

    @Autowired
    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/project")
    public ResponseEntity<ApiResponse> createProject(
        @RequestBody CreateProjectRequest request
    ) {
        try {
            Project project = projectService.createProject(request);
            ProjectDto projectDto = projectService.convertProjectToProjectDto(project);
            return ResponseEntity.ok(new ApiResponse("Project Created Successfully", projectDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/project")
    public ResponseEntity<ApiResponse> updateProject(
        @RequestBody UpdateProjectRequest request,
        @RequestParam Long id
    ) {
        try {
            projectService.updateProject(request, id);
            return ResponseEntity.ok(new ApiResponse("Project Updated Successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/project")
    public ResponseEntity<ApiResponse> getProjectById(
        @RequestParam Long id
    ) {
        try {
            Project project = projectService.getProject(id);
            ProjectDto projectDto = projectService.convertProjectToProjectDto(project);
            return ResponseEntity.ok(new ApiResponse("Project fetched Successfully", projectDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/project")
    public ResponseEntity<ApiResponse> deleteProject(
        @RequestParam Long id
    ) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.ok(new ApiResponse("Project deleted Successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getTasks")
    public ResponseEntity<ApiResponse> getTasks(
        @RequestParam Long projectId
    ) {
        try {
            List<Task> tasks = projectService.getTasks(projectId);
            return ResponseEntity.ok(new ApiResponse("Project Tasks fetched Successfully", tasks));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<ApiResponse> getUsers(
        @RequestParam Long projectId
    ) {
        try {
            List<User> users = projectService.getUsers(projectId);
            return ResponseEntity.ok(new ApiResponse("Users assigned to project fetched Successfully", users));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAll() {
        try {
            List<Project> projects = projectService.getAll();
            return ResponseEntity.ok(new ApiResponse("projects fetched Successfully", projects));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse> deleteAll() {
        try {
            projectService.deleteAll();
            return ResponseEntity.ok(new ApiResponse("projects deleted Successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("assignProjectToUser")
    public ResponseEntity<ApiResponse> assignProjectToUser(
        @RequestParam Long projectId,
        @RequestParam Long userId
    ) {
        try {
            projectService.assignProjectToUser(projectId, userId);
            return ResponseEntity.ok(new ApiResponse("project assigned to user Successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
