package com.snippet.gig.service.task;

import java.util.List;

import com.snippet.gig.dto.TaskDto;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.requestDto.CreateTaskRequest;
import com.snippet.gig.requestDto.UpdateTaskRequest;

public interface ITaskService {
    // basic crud operations
    Task createTask(CreateTaskRequest task);

    void updateTask(UpdateTaskRequest task, Long taskId) throws ResourceNotFoundException;

    Task getTaskById(Long taskId) throws ResourceNotFoundException;

    void deleteTask(Long id) throws ResourceNotFoundException;

    // Other Read Operations
    List<User> getUsersAssigned(Long taskId) throws ResourceNotFoundException;

    Project getProject(Long taskId) throws ResourceNotFoundException;

    List<Task> getAllTasks() throws ResourceNotFoundException;

    // Other Update Operations
    void updateStatus(Long taskId, String status);

    void updatePriority(Long taskId, String priority);

    void assignTaskToUser(Long taskId, Long userId) throws ResourceNotFoundException, AlreadyExistsException;

    void assignProjectToTask(Long projectId, Long taskId) throws ResourceNotFoundException, AlreadyExistsException;

    // DTO Method
    TaskDto convertTasktoTaskDto(Task task);

    // Other Delete Operations
    // void deleteUsersAssigned(Long id) throws ResourceNotFoundException;
}
