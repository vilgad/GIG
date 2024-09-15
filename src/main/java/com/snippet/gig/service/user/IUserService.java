package com.snippet.gig.service.user;

import com.snippet.gig.dto.UserDto;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.requestDto.CreateUserRequest;
import com.snippet.gig.requestDto.UpdateUserRequest;

import java.util.List;

public interface IUserService {
    // basic crud operations
    User createUser(CreateUserRequest user) throws AlreadyExistsException;

    User getUserById(Long id) throws ResourceNotFoundException;

    User updateUserDetails(UpdateUserRequest request, Long id) throws ResourceNotFoundException;

    void deleteUser(Long id);

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

    // DTO method
    UserDto convertUserToDto(User user);
}
