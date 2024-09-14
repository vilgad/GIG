package com.snippet.gig.service;

import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;

import java.util.List;

public interface IUserService {
    // basic crud operations
    User createUser(User user);
    User getUserById(Long id);
    User updateUserDetails(Long id);
    void deleteUser(Long id, String username, String email);

    // Other Read Operations
    User getUserByEmail(String email);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    List<User> getUsersByRole(String role);
    List<Task> getUserTasks(Long id, String email, String username);

    // Other Update Operations
    void updateUserRole(Long id, String username, String email, String role);
    void changePassword(Long id, String username, String email, String password);

    // Other Delete Operations
    void deleteAllUsers();
    void deleteUsersByRole(String role);
}
