package com.snippet.gig.service.task;

import java.util.List;

import com.snippet.gig.dto.TaskDto;
import com.snippet.gig.entity.Comment;
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

    List<User> getUsersAssigned(Long taskId) throws ResourceNotFoundException;

    Project getProject(Long taskId) throws ResourceNotFoundException;

    List<Task> getAllTasks() throws ResourceNotFoundException;

    void updateStatus(Long taskId, String status);

    void updatePriority(Long taskId, String priority);

    void assignTaskToUser(Long taskId, Long userId) throws ResourceNotFoundException, AlreadyExistsException;

    void assignProjectToTask(Long projectId, Long taskId) throws ResourceNotFoundException, AlreadyExistsException;

    TaskDto convertTasktoTaskDto(Task task);
}
