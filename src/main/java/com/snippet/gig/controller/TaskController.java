package com.snippet.gig.controller;

import com.snippet.gig.dto.TaskDto;
import com.snippet.gig.entity.Comment;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.enums.Priority;
import com.snippet.gig.enums.Status;
import com.snippet.gig.requestDto.CreateTaskRequest;
import com.snippet.gig.requestDto.UpdateTaskRequest;
import com.snippet.gig.response.ApiResponse;
import com.snippet.gig.service.task.ITaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/tasks")
@Tag(name = "Task APIs")
public class TaskController {
    private final ITaskService taskService;

    @Autowired
    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PostMapping("/private/create-task")
    public ResponseEntity<ApiResponse> createTask(
            @RequestBody CreateTaskRequest request) {
        Task task = taskService.createTask(request);
        TaskDto taskDto = taskService.convertTasktoTaskDto(task);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Task Created Successfully",
                        taskDto
                ));
    }

    @GetMapping("/public/get-task-by-id")
    public ResponseEntity<ApiResponse> getTaskById(
            @RequestParam Long taskId) {
        Task task = taskService.getTaskById(taskId);
        TaskDto taskDto = taskService.convertTasktoTaskDto(task);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Task Created Successfully",
                        taskDto
                ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/private/update-task")
    public ResponseEntity<ApiResponse> updateTask(
            @RequestBody UpdateTaskRequest request,
            @RequestParam Long taskId) {
        taskService.updateTask(request, taskId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Task updated Successfully",
                        null
                ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/private/update-status")
    public ResponseEntity<ApiResponse> updateStatus(
            @RequestParam Long taskId,
            @RequestParam String status) {
        taskService.updateStatus(taskId, status);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Task status updated Successfully",
                        null
                ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/private/update-priority")
    public ResponseEntity<ApiResponse> updatePriority(
            @RequestParam Long taskId,
            @RequestParam String priority) {
        taskService.updatePriority(taskId, priority);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Task priority updated Successfully",
                        null
                ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @DeleteMapping("/private/delete-task")
    public ResponseEntity<ApiResponse> deleteTask(
            @RequestParam Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Task deleted Successfully",
                        null
                ));
    }

    @GetMapping("/public/get-users-assigned")
    public ResponseEntity<ApiResponse> getUsersAssigned(
            @RequestParam Long taskId) {
        List<User> users = taskService.getUsersAssigned(taskId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "All assigned users",
                        users
                ));
    }

    @GetMapping("/public/get-project")
    public ResponseEntity<ApiResponse> getProject(
            @RequestParam Long taskId) {
        Project project = taskService.getProject(taskId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "project assigned",
                        project
                ));
        }

    @GetMapping("/public/get-all-tasks")
    public ResponseEntity<ApiResponse> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(new ApiResponse(
                "All tasks successfully fetched",
                tasks
        ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/private/assign-task-to-user")
    public ResponseEntity<ApiResponse> assignTaskToUser(
            @RequestParam Long taskId,
            @RequestParam Long userId) {
        taskService.assignTaskToUser(taskId, userId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Success",
                        null
                ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/private/assign-project-to-task")
    public ResponseEntity<ApiResponse> assignProjectToTask(
            @RequestParam Long projectId,
            @RequestParam Long taskId) {
        taskService.assignProjectToTask(projectId,taskId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Success",
                        null
                ));
    }
}
