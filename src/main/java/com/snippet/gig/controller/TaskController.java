package com.snippet.gig.controller;

import com.snippet.gig.dto.TaskDto;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
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
    @PostMapping("/private/task")
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

    @GetMapping("/public/task")
    public ResponseEntity<ApiResponse> getTaskById(
            @RequestParam Long id) {
        Task task = taskService.getTaskById(id);
        TaskDto taskDto = taskService.convertTasktoTaskDto(task);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Task Created Successfully",
                        taskDto
                ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/private/task")
    public ResponseEntity<ApiResponse> updateTask(
            @RequestBody UpdateTaskRequest request,
            @RequestParam Long id) {
        taskService.updateTask(request, id);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Task updated Successfully",
                        null
                ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @DeleteMapping("/private/task")
    public ResponseEntity<ApiResponse> deleteTask(
            @RequestParam Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Task deleted Successfully",
                        null
                ));
    }

    @GetMapping("/public/getUsersAssigned")
    public ResponseEntity<ApiResponse> getUsersAssigned(
            @RequestParam Long id) {
        List<User> users = taskService.getUsersAssigned(id);
        return ResponseEntity.ok(
                new ApiResponse(
                        "All assigned users",
                        users
                ));
    }

    @GetMapping("/public/getProject")
    public ResponseEntity<ApiResponse> getProject(
            @RequestParam Long taskId) {
        Project project = taskService.getProject(taskId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "project assigned",
                        project
                ));
        }

    @GetMapping("/public/all")
    public ResponseEntity<ApiResponse> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(new ApiResponse(
                "All tasks successfully fetched",
                tasks
        ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/private/assignTaskToUser")
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
    @PutMapping("/private/assignProjectToTask")
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
