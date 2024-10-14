package com.snippet.gig.controller;

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

import com.snippet.gig.dto.TaskDto;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.requestDto.CreateTaskRequest;
import com.snippet.gig.requestDto.UpdateTaskRequest;
import com.snippet.gig.response.ApiResponse;
import com.snippet.gig.service.task.ITaskService;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/tasks")
public class TaskController {
    private final ITaskService taskService;

    @Autowired
    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task")
    public ResponseEntity<ApiResponse> createTask(
            @RequestBody CreateTaskRequest request) {
        Task task = taskService.createTask(request);
        TaskDto taskDto = taskService.convertTasktoTaskDto(task);
        return ResponseEntity.ok(new ApiResponse("Task Created Successfully", taskDto));
        /*try {
            Task task = taskService.createTask(request);
            TaskDto taskDto = taskService.convertTasktoTaskDto(task);
            return ResponseEntity.ok(new ApiResponse("Task Created Successfully", taskDto));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @GetMapping("/task")
    public ResponseEntity<ApiResponse> getTaskById(
            @RequestParam Long id) {
        Task task = taskService.getTaskById(id);
        TaskDto taskDto = taskService.convertTasktoTaskDto(task);
        return ResponseEntity.ok(new ApiResponse("Task Created Successfully", taskDto));
       /* try {
            Task task = taskService.getTaskById(id);
            TaskDto taskDto = taskService.convertTasktoTaskDto(task);
            return ResponseEntity.ok(new ApiResponse("Task Created Successfully", taskDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @PutMapping("/task")
    public ResponseEntity<ApiResponse> updateTask(
            @RequestBody UpdateTaskRequest request,
            @RequestParam Long id) {
        taskService.updateTask(request, id);
        return ResponseEntity.ok(new ApiResponse("Task updated Successfully", null));
        /*try {
            taskService.updateTask(request, id);
            return ResponseEntity.ok(new ApiResponse("Task updated Successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @DeleteMapping("/task")
    public ResponseEntity<ApiResponse> deleteTask(
            @RequestParam Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(new ApiResponse("Task deleted Successfully", null));
        /*try {
            taskService.deleteTask(id);
            return ResponseEntity.ok(new ApiResponse("Task deleted Successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @GetMapping("/getUsersAssigned")
    public ResponseEntity<ApiResponse> getUsersAssigned(
            @RequestParam Long id) {
        List<User> users = taskService.getUsersAssigned(id);
        return ResponseEntity.ok(new ApiResponse("All assigned users", users));
        /*try {
            List<User> users = taskService.getUsersAssigned(id);
            return ResponseEntity.ok(new ApiResponse("All assigned users", users));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @GetMapping("/getProject")
    public ResponseEntity<ApiResponse> getProject(
            @RequestParam Long taskId) {
        Project project = taskService.getProject(taskId);
        return ResponseEntity.ok(new ApiResponse("project assigned", project));
        /*try {
            Project project = taskService.getProject(taskId);
            return ResponseEntity.ok(new ApiResponse("project assigned", project));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(new ApiResponse("All tasks successfully fetched", tasks));
        /*try {
            List<Task> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(new ApiResponse("All tasks successfully fetched", tasks));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @PutMapping("/assignTaskToUser")
    public ResponseEntity<ApiResponse> assignTaskToUser(
            @RequestParam Long taskId,
            @RequestParam Long userId) {
        taskService.assignTaskToUser(taskId, userId);
        return ResponseEntity.ok(new ApiResponse("Success", null));
        /*try {
            taskService.assignTaskToUser(taskId, userId);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @PutMapping("/assignProjectToTask")
    public ResponseEntity<ApiResponse> assignProjectToTask(
            @RequestParam Long projectId,
            @RequestParam Long taskId) {
        taskService.assignProjectToTask(projectId = projectId,taskId = taskId);
        return ResponseEntity.ok(new ApiResponse("Success", null));
        /*try {
            taskService.assignProjectToTask(projectId = projectId,taskId = taskId);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    /* @DeleteMapping("/deleteUsersAssigned")
    public ResponseEntity<ApiResponse> deleteUsersAssigned(
            @RequestParam Long id) {
        try {
            taskService.deleteUsersAssigned(id);
            return ResponseEntity.ok(new ApiResponse("Users removed Successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    } */
}
