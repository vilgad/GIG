package com.snippet.gig.service;

import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;

import java.util.List;

public interface ITaskService {
    // basic crud operations
    Task createTask(Task task);
    Task updateTask(Task task);
    Task getTaskById(Long id);
    void deleteTask(Long id);

    // Other Read Operations
    List<User> getUsersAssigned(Long taskId);
    List<Project> getProject(Long taskId);

    // Other Update Operations
    void updateStatus(Long id, String status);
    void updatePriority(Long id, String priority);
    void changeDueDate(Long id, String dueDate);

    // Other Delete Operations
}
